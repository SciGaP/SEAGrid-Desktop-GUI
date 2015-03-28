package nanocad;

/**
 * group.java - group of atoms and terms
 * Copyright (c) 1997,1998,1999 Will Ware, all rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and other materials provided with the distribution.
 *
 * This software is provided "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability
 * or fitness for any particular purpose are disclaimed. In no event shall
 * Will Ware be liable for any direct, indirect, incidental, special,
 * exemplary, or consequential damages (including, but not limited to,
 * procurement of substitute goods or services; loss of use, data, or
 * profits; or business interruption) however caused and on any theory of
 * liability, whether in contract, strict liability, or tort (including
 * negligence or otherwise) arising in any way out of the use of this
 * software, even if advised of the possibility of such damage.
 */

import java.awt.*;
import java.lang.Math;
import java.util.Vector;

public class group
{
    public static final String rcsid =
	"$Id: group.java,v 1.2 2005/05/14 23:51:01 xli16 Exp $";
    public Vector atomList;
    public Vector bondList;
    public boolean changedSinceLastSave = false;
    private boolean needToEnumerateTerms;
    private boolean needToGetTypes = true;
    
    private boolean showForces = false;

    private Vector drawingList;
    public drawPanel mypanel;
    public view v;
    public double forceMultiplier = 100.0;
    private textwin tw;

    //two group for rest group and changed group;
    public group chgGrp; 
    public group restGrp;
    
    /* these are for formatting strings and numbers */
    private final static int LEFT = 0;
    private final static int RIGHT = 1;
    private String scanString;

    //these are used for coordintes changes
    public double perspDist = 400;
    public double zoomFactor = 25;
    private double xCenter = 200;
    private double yCenter = 200;
    private double zCenter = 0;
    private double x1[], r1;

  /* Let's support two standards for molecule file formats, PDB and XYZ.
   * Also, support a "native" file format with hybridization and bond order.
   * This enum must agree with the order of menu items in nanocad.java.
   * That's ugly, and should be fixed.
   */

    public final static int SAVEPDB = 0;
    public final static int SAVEXYZ = 1;
    
    public int fileMode;

    /**
     *Following are different kinds of constructor for group
     */
    public group ()
    {
	v = new view ();
	empty ();
    }

    public group (drawPanel p)
    {
	mypanel = p;
	v = new view ();
	empty ();
    }
    
    public group (drawPanel p, textwin d)
    {
	mypanel = p;
	v = new view ();
	empty ();
	tw = d;
    }
    
    /**
     *This is a constructor from two sub-groups
     */ 
    public group (group g1, group g2, drawPanel p){
	empty();
	mypanel = g2.mypanel;
	
	v = g2.v;
		
	for (int i = 0; i<g1.atomList.size(); i++)    
	    atomList.addElement((atom)g1.atomList.elementAt(i));
				

	for (int i = 0; i<g2.atomList.size(); i++)
	    atomList.addElement((atom)g2.atomList.elementAt(i));

	for (int i = 0; i<g1.bondList.size(); i++)
	    bondList.addElement((bond)g1.bondList.elementAt(i));
							
	for (int i =0; i<g2.bondList.size(); i++)
	{    
	    boolean add = true;												//avoid adding duplicate bonds
	    for (int j = 0; j < this.bondList.size(); j++)					//if cordinates of atoms on ends of bonds are equal,
	    {	
		atom tmpa1 = ((atom)((bond)this.bondList.elementAt(j)).a1);		//bonds are duplicate
		atom tmpa2 = ((atom)((bond)this.bondList.elementAt(j)).a2);
		atom tmpb1 = ((atom)((bond)g2.bondList.elementAt(j)).a1);
		atom tmpb2 = ((atom)((bond)g2.bondList.elementAt(j)).a2);
		if (tmpa1.x[0] == tmpb1.x[0] && tmpa1.x[1] == tmpb1.x[1] && tmpa1.x[2] == tmpb1.x[2])
		    if (tmpa2.x[0] == tmpb2.x[0] && tmpa2.x[1] == tmpb2.x[1] && tmpa2.x[2] == tmpb2.x[2])
		    	add = false;
	    }
	    if (add == true)
	        bondList.addElement((bond)g2.bondList.elementAt(i));
	}			

	changedSinceLastSave =g2.changedSinceLastSave;
	needToEnumerateTerms = g2.needToEnumerateTerms;
	needToGetTypes = g2.needToGetTypes;
	showForces = g1.showForces;
	forceMultiplier = g1.forceMultiplier;
	tw = g1.tw;
	scanString = g1.scanString;
    }

    /**
     *copy constructor
     */	
    public group (group grp)
    {
	empty();
	mypanel = grp.mypanel;
	v = grp.v;
	Graphics g = mypanel.getGraphics();        

	atomList = (Vector)grp.atomList.clone();
	bondList = (Vector)grp.bondList.clone();
	
	changedSinceLastSave = grp.changedSinceLastSave;
	needToEnumerateTerms = grp.needToEnumerateTerms;
	needToGetTypes = grp.needToGetTypes;
	showForces = grp.showForces;
	forceMultiplier = grp.forceMultiplier;
	tw = grp.tw;
	scanString = grp.scanString;
    }

    /**
     *form a simple group from atom and bond vector				
     *Note: this constructor doesn't form a functional group unless it is passed
     *both a valid atom list and bond list
     */
    public group (Vector atomVector,Vector bondVector, drawPanel p)
    {

	mypanel = p;

	atomList= (Vector) atomVector.clone();
	bondList = (Vector) bondVector.clone();
	
	v = new view ();
    }

    /**
    *this function makes a copy of a group with refrences to its own set of atoms and bonds
    *(as opposed to the copy constructor, which has refrences to the same atoms and bonds).
    *The code is a little shaky, so it's probably better not to use it if you don't have to.
    */

    public void copy(group grp)
    {
	
	atomList = new Vector();

	for (int i = 0; i < grp.atomList.size(); i++)
	{
	    atom tmpAtom = (atom)grp.atomList.elementAt(i);
	    tmpAtom = new GeneralAtom((GeneralAtom)tmpAtom);
	    atomList.addElement(tmpAtom);
	    tmpAtom.setGroup(this);
	}
	bondList = new Vector();

	for (int i = 0; i < grp.bondList.size(); i++)
	{
	    bond tmpBond = new bond((bond)grp.bondList.elementAt(i));
	    bondList.addElement(tmpBond);
	}
	
	for (int j = 0; j < grp.bondList.size(); j++)	//this code connects bonds to atoms and atoms to bonds in the copy
	{
	    int index = grp.atomList.indexOf(((bond)grp.bondList.elementAt(j)).a1);
	    ((bond)bondList.elementAt(j)).a1 = (atom)atomList.elementAt(index);
	    ((atom)atomList.elementAt(index)).bonds.addElement((bond)bondList.elementAt(j));

	    index = grp.atomList.indexOf(((bond)grp.bondList.elementAt(j)).a2);
	    ((bond)bondList.elementAt(j)).a2 = (atom)atomList.elementAt(index);
	    ((atom)atomList.elementAt(index)).bonds.addElement((bond)bondList.elementAt(j));
	}
    }
              
    /**
     *translation
     */
    public void pan (int dx, int dy)
    {
	for (int i=0; i< atomList.size();i++){
	    atom newatom = (atom)atomList.elementAt(i);
		// How does this work?  Won't you have to return this somehow? (stenhous 11/19/02)
		
	    newatom.x[0] = newatom.x[0] + dx / zoomFactor * perspectiveFactor(newatom.x[2]); 
	    newatom.x[1] = newatom.x[1] + dy / zoomFactor * perspectiveFactor(newatom.x[2]);
	    newatom.x[2] = newatom.x[2];
	}
    }
    
    /**
     *choose the rotation center, better not choose Hydrogen if there is other choice
     */
    public atom chooseCenter(Vector atomL){
	atom center = null;
	for (int i=0; i< atomL.size();i++){
	    atom newatom = (atom) atomL.elementAt(i);
	    	    
	    //determine the center atom;
	    if (!newatom.symbol().equals("H") && center == null){
		center = newatom;
		break;
	    }
	}
	return center;
    }
    
   /**
    * Same as above, but with atomList as default.  Sort of silly to have 2 functions.
	* Changed by stenhous 11/17/02
    */
    public atom chooseCenter(){
		return chooseCenter(atomList);
	}

    public void rotate(double xAngle, double yAngle)
	// Does this work with returning nothing? (stenhous 11/19/02)
    {
	double x, y, z;
	double sax = Math.sin(xAngle);
	double cax = Math.cos(xAngle);
	double say = Math.sin(yAngle);
	double cay = Math.cos(yAngle);

	for (int i = 0; i < atomList.size(); i++)
	{
	    atom newAtom = (atom)atomList.elementAt(i);

	    // x rotation  (actually, it's really a y translation)
	    
	    x = newAtom.x[2] * sax + newAtom.x[0] * cax;
	    // y = y
	    z = newAtom.x[2] * cax - newAtom.x[0] * sax;

	    newAtom.x[0] = x;
	    newAtom.x[2] = z;

	    // y rotation (this one is really a x rotation)

	    // x = x
	    y = newAtom.x[1] * cay - newAtom.x[2] * say;
	    z = newAtom.x[1] * say + newAtom.x[2] * cay;

	    newAtom.x[1] = y;
	    newAtom.x[2] = z;
	}
    }

   /**
    * different ways to add atom 
    */
    public void addAtom (atom a)
    {
		needToEnumerateTerms = true;
		needToGetTypes = true;
		changedSinceLastSave = true;
		atomList.addElement (a);
		a.setGroup (this);
    }
    
    
    public void addAtom (atom a, double[] scrPos)
    {
		needToEnumerateTerms = true;
		needToGetTypes = true;
		changedSinceLastSave = true;
		a.x = v.screenToXyz (scrPos);
		atomList.addElement (a);
		a.setGroup (this);
    }
    
    public void addAtom (atom a, double x0, double x1, double x2)
    {
	  needToEnumerateTerms = true;
	  needToGetTypes = true;
	  changedSinceLastSave = true;
	  a.x[0] = x0;
	  a.x[1] = x1;
	  a.x[2] = x2;
	  atomList.addElement (a);
	  a.setGroup (this);
	}

   /**
    *overloaded add bond
    */
    public void addBond (int a1, int a2)
    {
	    atom at1 = (atom) atomList.elementAt (a1),
		// Where is at2 defined as an atom?
		at2 = (atom) atomList.elementAt (a2);
	    addBond (at1, at2);
	}
    
    public void addBond (int a1, int a2, int ord)
    {
		atom at1 = (atom) atomList.elementAt (a1),
		// Same ? as above.  Do these just never get used?
		at2 = (atom) atomList.elementAt (a2);
		addBond (at1, at2, ord);
    }
    
    public void addBond (atom a1, atom a2)
    {
	bond b;
	if (a1 == null || a2 == null)
	    return;
	b = a1.bondWith(a2);
	if (b != null)
	    b.incrOrder();
	else
	    {
		b = new bond (a1, a2);
		  bondList.addElement (b);
	    }
	needToEnumerateTerms = true;
	needToGetTypes = true;
	changedSinceLastSave = true;
    }
    
    public void addBond (atom a1, atom a2, int ord)
	{
	  if (a1 == null || a2 == null || ord < 1 || ord > 3)
	      return;
	  while (true)
	      {
		  bond b = a1.bondWith(a2);
		  if (b != null)
		      {
			  if (b.order() == ord)
			      break;
			  else
			      b.incrOrder();
		      }
		  else
		      {
			  b = new bond (a1, a2);
			  bondList.addElement (b);
		      }
	      }
	  needToEnumerateTerms = true;
	  needToGetTypes = true;
	  changedSinceLastSave = true;
	}
    
    /**
     *different paint functions
     */
    private void bagLine ()
    {
	int i;
	for (i = 0;
	     i < scanString.length() &&
		 scanString.charAt(i) != '\n';
	     i++);
	if (scanString.length() > (i + 1))
	    scanString =
		scanString.substring(i + 1);
	else
	    scanString = "";
    }
    
    public void bubblePaint()
    {
	int i;
	Vector dlist = new Vector ();
	dl_atom dla = null;
	for (i = 0; i < atomList.size (); i++)
	    {
		dla = new dl_atom ((atom) atomList.elementAt (i), v);
		dlist.addElement (dla);
	    }
	if (dla != null)
	    dla.quickpaint (dlist, mypanel.getGraphics ());
    }
    
    /**
     *recenter atoms
     */
    public void centerAtoms ()
    {
	int i, j;
	atom a;
	double[] x = { 0, 0, 0 };
	for (i = 0; i < atomList.size (); i++)
	    {
		a = (atom) atomList.elementAt (i);
		for (j = 0; j < 3; j++)
		    x[j] += a.x[j];
	    }
	for (j = 0; j < 3; j++)
	    x[j] /= atomList.size ();
	for (i = 0; i < atomList.size (); i++)
	    {
		a = (atom) atomList.elementAt (i);
		for (j = 0; j < 3; j++)
		    a.x[j] -= x[j];
	    }
	updateViewSize ();
    }
    
    /**
     *delete atom
     */
    public void deleteAtom (atom a)
    {
	int i;
	if (atomList.size () == 0)
	    return;
	needToEnumerateTerms = true;
	needToGetTypes = true;
	changedSinceLastSave = true;
	// remove all bonds connected to the atom
	while (a.bonds.size() > 0)
	    {
		bond b = (bond) a.bonds.elementAt(0);
		b.delete();
		bondList.removeElement(b);
	    }
	// remove the atom
	atomList.removeElement (a);
    }
    
    /**
     *delete bond
     */
    public void deleteBond (atom a1, atom a2)
    {
	bond b = a1.bondWith (a2);
	if (b == null)
	    {
		b = a2.bondWith(a1);
		if (b == null)
		    return;
	    }
	needToEnumerateTerms = true;
	needToGetTypes = true;
	changedSinceLastSave = true;
	bondList.removeElement (b);
	a1.bonds.removeElement (b);
	a2.bonds.removeElement (b);
	a1.rehybridize ();
	a2.rehybridize ();
    }

    /**
     *paint function
     */
    public void drawLineToAtom (atom a, double x, double y)
    {
	dl_atom dummy = new dl_atom (a, v);
	dummy.drawLineToAtom (a, x, y, mypanel.getGraphics ());
    }
    
    /**
     *empty group data members
     */
    public void empty ()
    {
		needToEnumerateTerms = true;
		needToGetTypes = true;
		changedSinceLastSave = true;
		atomList = new Vector ();
		bondList = new Vector ();
    }
    
    /**
     *data format
     */
    private String formatDouble (double x, int intpart, int fracpart)
    {
	boolean neg = false;
	String fraction;
	if (x < 0.0)
	{
	    neg = true;
	    x = -x;
	}
	fraction = formatFractionalPart (x, fracpart);
	if (neg)
	    {
		if (((int) x) == 0)
		    {
			String intstr = "-0";
			while (intstr.length() < intpart)
			    intstr = " " + intstr;
			return intstr + "." + fraction;
	    }
		else
	    return formatInt (-((int) x), intpart, RIGHT) + "." + fraction;
	    }
	else
	    return formatInt (((int) x), intpart, RIGHT) + "." + fraction;
    }
    
    private String formatFractionalPart (double x, int fracpart)
    {
	String fracstr = "";
	if (x < 0.0) x = -x;
	x -= (int) x;
	while (fracstr.length() < fracpart)
	    {
		int xi;
		x *= 10;
		xi = (int) x;
		x -= xi;
		fracstr = fracstr + (char) (xi + '0');
	    }
	return fracstr;
    }

    private String formatInt (int x, int spaces, int direction)
    {
	String intstr = "";
	boolean neg = false;
	if (x < 0)
	    {
		neg = true;
		x = -x;
	    }
	if (x == 0)
	    intstr = "0";
	else
	    while (x > 0)
		{
		    intstr = ((char) ((x % 10) + '0')) + intstr;
		    x /= 10;
		}
	if (neg)
	    intstr = '-' + intstr;
	  if (direction == LEFT)
	      while (intstr.length() < spaces)
		  intstr = intstr + ' ';
	  else
	      while (intstr.length() < spaces)
		  intstr = ' ' + intstr;
	  return intstr;
	}
    
    private String formatString (String x, int spaces, int direction)
    {
	int i;
	String spc = "";
	for (i = 0; i < spaces - x.length(); i++)
	    spc += " ";
	if (direction == LEFT)
	    return x + spc;
	else
	    return spc + x;
    }
    
  private String formatUnsignedInt (int x, int spaces, int direction)
    {
	String intstr = "";
	if (x == 0)
	    intstr = "0";
	else
	    while (x > 0)
		{
		    intstr = ((char) ((x % 10) + '0')) + intstr;
		    x /= 10;
		}
	if (direction == LEFT)
	    while (intstr.length() < spaces)
		intstr = intstr + ' ';
	else
	    while (intstr.length() < spaces)
		intstr = ' ' + intstr;
	return intstr;
    }

    /**
     * Insert the method's description here.
     * Creation date: (5/29/00 3:22:26 PM)
     * @return java.util.Vector
     */
    
    public Vector getAllPairs() {
		return null;  // Should there be some other function also?
    }
    
    /**
     *get the native format for save, load or export
     */
    public String getNativeFormat()
    {
	System.out.println("new into group");
	int i;
	changedSinceLastSave = false;
	
	String s = atomList.size() + "\n";
	for (i = 0; i < atomList.size(); i++) 
	    s += ((atom) atomList.elementAt(i)).repr() + "\n";
	
	s += bondList.size() + "\n";
	for (i = 0; i < bondList.size(); i++)
	    s += ((bond) bondList.elementAt(i)).repr() + "\n";
	
	
	String c = "connectivity:\n " ;
	int[] arr1 = new int[bondList.size()];
	int[] arr2 = new int[bondList.size()];
	double[] arr3 = new double[bondList.size()];
	
	for (int k = 0; k< bondList.size(); k++) {
	    bond tmp = (bond)bondList.elementAt(k);
	    arr1[k]= tmp.a1.index();
	    arr2[k] =tmp.a2.index();
	    arr3[k]= tmp.order();
	}
	
	int t1,t2;
	double t3;
	for (int pass = 1; pass <arr1.length; pass++){
	    for (int m= 0; m<arr1.length-1; m++){
		if (arr1[m]>arr1[m+1]){
		    
		    t1 = arr1[m];
		    arr1[m]=arr1[m+1];
		    arr1[m+1]= t1;
		    
		    t2 = arr2[m];
		    arr2[m]=arr2[m+1];
		    arr2[m+1]= t2;
		    
		    t3 = arr3[m];
		    arr3[m]=arr3[m+1];
		    arr3[m+1]= t3;
		}
	    }
	    
	}   
	
	for (int l = 0; l < arr1.length; l++){
	    c += "<atom" + arr1[l]+ "  atom" + arr2[l]+" bond num is " + arr3[l] +">\n ";
	} 
	  
	System.out.println("the connectivity is :" +c);
	return (s + c);
    }
 
    /**
     *get the PDB format
     */
    public String getPDB(boolean includeOne)
    {
	 //change new sym from "C" to "C1";
	Vector v = newSymbol(atomList); 
	
	int i;
	changedSinceLastSave = false;
	
	String s ="";
	for (i = 0; i < atomList.size(); i++)
	    {
		atom a = (atom) atomList.elementAt(i);
		s += "ATOM  " +
		    formatInt(i+1, 5, RIGHT) +
		    "  " +
		    //formatString(a.symbol()+"*", 4, LEFT) +
		    formatString((String)v.elementAt(i)+"*", 4, LEFT);
		if (includeOne == true)
		    s +=  "    1        ";
		s +=formatDouble(a.x[0], 4, 3) +
		    formatDouble(a.x[1], 4, 3) +
		    formatDouble(a.x[2], 4, 3) +
		    "\n";
	    }//14 & 13 blank
	
	if (true)
	    for (i = 0; i < atomList.size(); i++)
	    {
		int maxBondOrder = 0;
		atom current = (atom) atomList.elementAt(i);
		for (int k = 0; k < current.bonds.size(); k++)
		    maxBondOrder = Math.max(maxBondOrder, ((bond)current.bonds.elementAt(k)).order());

		for (int k = 1; k <= maxBondOrder; k++)
		{
		    s += "CONECT";
		    s += formatInt(i + 1, 5, RIGHT);
		    for (int j = 0; j < current.bonds.size(); j++)
		    {
			if ( ((bond)current.bonds.elementAt(j)).order() >= k)
			    s += formatInt( ((bond)current.bonds.elementAt(j)).otherAtom(current).index()+1, 5, RIGHT);
		    }
		    s += "\n";
		}
	    }
	return s;
    }


	/**
     * keeps solidarity between new and old code.
     */
    public String getPDB() {return getPDB(true);}
    
    /**
     *update symbol expression from "C" to "C1...Cn"
     */
    public Vector newSymbol(Vector atomL){
	String tmp;
	boolean change = false;
	int j=0;
	atom a, b;
	Vector symV = new Vector();
	
	for (int i = 0; i< atomL.size(); i++){ //21, 0->20
	    
	    a = (atom) atomL.elementAt(i);
	    
	    if(i<atomList.size()-1)	b = (atom) atomL.elementAt(i+1);  // i < 20
		else if (i == atomList.size()-1) b= (atom)atomL.elementAt(atomL.size()-1); // point to the last one (i = 20);
		else break;
	    
	    if (a.symbol().equals(b.symbol())){
		++j;
		tmp =a.symbol()+(new Integer(j)).toString(); 
	    }
		else {
		tmp =a.symbol()+(new Integer(j+1)).toString();
		j=0;
	    }
	    
	    symV.addElement(tmp);
	}

	return symV;
    }
    
    /**
     *get XYZ format
     */
    public String getXYZ()
    {
	int i;
	changedSinceLastSave = false;
	String s = atomList.size() + "\n";
	s += "Molecule has :\n";
	Vector preAtom = new Vector();
	
	for (i = 0; i < atomList.size(); i++)
	    {
		atom a = (atom) atomList.elementAt(i);
		s += a.symbol();
		s += " ";
		s += a.x[0];
		s += " ";
		s += a.x[1];
		s += " ";
		s += a.x[2];
		s += "\n";
	    }
	return s;
    }
    
    
   /* This will do for small structures, but may become painful
    * for big ones, as it is O(N^2). If performance suffers, it
    * can be improved by sorting atoms into spatial partitions
    * (an O(N) operation) and then searching for potential bond
    * mates only in nearby partitions (also O(N)).
	*-----------------------------------------------------------
	* Errr... that's impossible.  Figuring out which partition
	* the element is in is O(n) with respect to the number of
	* partitions.  And you'll have to do it with n atoms.  If
	* you store which ones are in some structure, then you might
	* be able to redo it in faster time, but that's just silly.
	* - stenhous 11/19/02
	*/
    private void guessBondInfo()
    {
	int i, j;
	atom a1, a2;
	for (i = 0; i < atomList.size() - 1; i++)
	    {
		a1 = (atom) atomList.elementAt(i);
		for (j = i + 1; j < atomList.size(); j++)
		    {
			int k;
			double d = 0.0;
			a2 = (atom) atomList.elementAt(j);
			for (k = 0; k < 3; k++)
			    d += (a1.x[k] - a2.x[k]) * (a1.x[k] - a2.x[k]);
			if (Math.sqrt(d) <
			    (a1.covalentRadius() + a2.covalentRadius() + 0.5))
			    addBond(a1,a2);
		    }
	    }
    }

    private boolean isBlank (char c, boolean includeNewline)
    {
	if (includeNewline)
	    return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	else
	    return c == ' ' || c == '\t';
    }

    /**
     * Insert the method's description here.
     * Creation date: (7/25/2000 3:02:31 PM)
     * @return boolean
     */
    public boolean needsToEnumerateTerms() { return needToEnumerateTerms; }
    public boolean needsToGetTypes(){ return needToGetTypes; }

    // jstrutz
    // Sets the typeNums (nanocad, MM3, and UFF)
    public void setTypes(){
		// getMM3TypeNum() sets ncad type num as the first thing it does.
		for(int i=0; i<atomList.size(); i++)
		{	((atom)(atomList.elementAt(i))).setMM3TypeNum();
			((atom)(atomList.elementAt(i))).setUFFTypeNum();
		}

		// Resonance must be set after the uffTypeNum has been set,
		// because uff needs to know what the hybridization is for
		// every atom before it can determine if anything is
		// resonant.  Hybridization is determined in getUFFTypeNum().
		for(int i=0; i<atomList.size(); i++)
		{	((atom)(atomList.elementAt(i))).setUFFResonance();
		}
		needToGetTypes = false;
    }

	// Made slightly more concise (stenhous 11/19/02)
    public void paint ()
    {	// Graphics g = mypanel.getGraphics(); this.paint(g);
		this.paint(mypanel.getGraphics()); }

    /**
     *group paint
     */
    public void paint (Graphics g)
    {
		int i, j;
		dl_atom dla = null;
		dl_bond dlb = null;
		Vector dlist = new Vector ();
		//if (showForces) computeForces ();
		for (i = 0; i < atomList.size(); i++)
	    {
			dla = new dl_atom ((atom) atomList.elementAt(i), v);
			dlist.addElement (dla);
		}
		for (i = 0; i < atomList.size(); i++)
		{
			atom a1 = (atom) atomList.elementAt (i);
			for (j = 0; j < a1.bonds.size(); j++)
		    {
				bond b = (bond) a1.bonds.elementAt(j);
				atom a2 = b.otherAtom(a1);
				if (a1.x[0] < a2.x[0])
			    {
					dlb = new dl_bond (b, v);
					dlist.addElement (dlb);
			    }
		    }
			if (showForces)
		    {
				dlforce dlf = new dlforce (a1.x, a1.f, v);
				dlf.setForceMultiplier (forceMultiplier);
				dlist.addElement (dlf);
		    }
	    }
		if (dla != null) dla.paint (dlist, mypanel.getGraphics ());
		else if (dlb != null) dlb.paint (dlist, mypanel.getGraphics ());
		// else do nothing.
    }

    /**
     *scan different numeric formats
     */
    private double scanDouble ()
    {
		double x = 0.0;
		skipBlanks();
		String w = scanWord();
		try { x = (new Double(w)).doubleValue(); }
		catch (Exception e) { }
		return x;
	}
    
    private int scanInt () { return (int) scanDouble(); }

    private String scanWord ()
    {
		String w = "";
//		int i, n = scanString.length(); // n is unused, right?
		skipBlanks();
		int i = 0;
		while(i < scanString.length() && !isBlank(scanString.charAt(i), true))
		     i++;
		w = scanString.substring(0, i);
		scanString = scanString.substring(i);
		return w;
    }
    
   /**
    *select atom on the screen
    */
    public atom selectedAtom (double[] scrPos, boolean picky)
    {
	int i;
	atom a, amin;
	double sqDist, minSqDist = 0;
	amin = null;
	
	if (atomList.size() == 0)
	    return null;

	for (i = 0; i < atomList.size (); i++)
	    {
		a = (atom) atomList.elementAt (i);
		double[] atomPos = v.xyzToScreen (a.x);
		double dx = atomPos[0] - scrPos[0];
		double dy = atomPos[1] - scrPos[1];
		sqDist = dx * dx + dy * dy;
		if (sqDist < minSqDist || i == 0)
		    {
			minSqDist = sqDist;
			amin = a;
		    }
	    }
	
	// if we're picky, we need to be right on top of the atom
	if (!picky || Math.sqrt(minSqDist) < .55 * v.zoomFactor / perspectiveFactor(amin.x[2]) * amin.covalentRadius())
	    return amin;
	else
	    return null;
    }
    
    /**
     *set different formats
     */
    public void setNativeFormat()
    {
	int numatoms, numbonds;
	  empty();
	  numatoms = scanInt();
	  bagLine();
	  while (numatoms-- > 0)
	      {
		  String zs, symbol, hybrid;
		  double x, y, z;
		  int h = atom.SP3;
		  if (scanString.charAt(0) != '<')
		      continue;
		  scanString = scanString.substring(1);
		  symbol = scanWord();
		  hybrid = scanWord();
		  x = scanDouble();
		  y = scanDouble();
		  zs = scanWord();
		  if (zs.charAt(zs.length() - 1) != '>')
		      continue;
		  try { z = (new Double(zs.substring(0,zs.length() - 1))).doubleValue(); }
		  catch (Exception e) { z = 2.0; }
		  
		  /* hybridizations */
		  if (hybrid.equals("NONE"))
		      h = atom.NONE;
		  else if (hybrid.equals("SP3"))
		      h = atom.SP3;
		  else if (hybrid.equals("SP2"))
		      h = atom.SP2;
		  else if (hybrid.equals("SP"))
		      h = atom.SP;
		  
		  /* which element */
		  if (symbol.equals("H"))
		      addAtom (new hydrogen (x, y, z));
		  else if (symbol.equals("C"))
		      addAtom (new carbon (h, x, y, z));
		  else if (symbol.equals("O"))
		      addAtom (new oxygen (h, x, y, z));
		  else if (symbol.equals("N"))
		      addAtom (new nitrogen (h, x, y, z));
		  bagLine();
	      }
	  
	  numbonds = scanInt();
	  bagLine();
	  while (numbonds-- > 0)
	      {
		  String ordstr;
		  int index1, index2, order;
		  if (!scanString.substring(0,6).equals("<Bond "))
		      continue;
		  scanString = scanString.substring(6);
		  index1 = scanInt();
		  index2 = scanInt();
		  ordstr = scanWord();
		  if (ordstr.charAt(ordstr.length() - 1) != '>')
		      continue;
		  ordstr = ordstr.substring(0, ordstr.length() - 1);
		  try { order = (new Integer(ordstr)).intValue(); }
		  catch (Exception e) { order = 1; }
		  addBond(index1, index2, order);
		  bagLine();
	      }
    }
    
    public void setPDB()
	{
	    /* not implemented yet */
	    System.out.println("setPDB");
	}
    
    public void setShowForces (boolean sf)
    {
	showForces = sf;
    }
    
    public void setTextwin(textwin d)
    {
	int i;
	tw = d;
    }
    
    public void setXYZ()
    {
	empty();
	int numatoms = scanInt();
	String symbol;
	double x, y, z;
	bagLine();
	bagLine();
	  while (numatoms-- > 0)
	      {
		  symbol = scanWord();
		  x = scanDouble();
		  y = scanDouble();
		  z = scanDouble();
		  if (symbol.equals("H"))
		      addAtom (new hydrogen (x, y, z));
		  else if (symbol.equals("C"))
		      addAtom (new carbon (atom.SP3, x, y, z));
		  else if (symbol.equals("O"))
		      addAtom (new oxygen (atom.SP3, x, y, z));
		  else if (symbol.equals("N"))
		      addAtom (new nitrogen (atom.SP3, x, y, z));
	      }
	  guessBondInfo();
    }
    
    private void skipBlanks ()
    {
	int i;
	for (i = 0;
	     i < scanString.length() &&
		 isBlank(scanString.charAt(i), false);
	     i++);
	scanString = scanString.substring(i);
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (7/25/2000 3:21:45 PM)
     */
    public void termsEnumerated()
    {
	needToEnumerateTerms = false;
    }
    
    /**
     *notify text window for events
     */
    public void textWindowNotify(String s)
    {
	int i;
	scanString = s;
	  
	if (scanString.substring(0,5).equals("Paste"))
	    {
		// If user left "Paste your..." in the window, skip over it. 
		bagLine ();
	    }
	skipBlanks();
	
	switch (fileMode)
	    {		
	    case SAVEPDB:
		getPDB();
		break;
		  
	    case SAVEXYZ:
		getXYZ();
		break;
		
	    case 2:
		getPDB();
		break;
		  
	    default:
		break;
	    }
    }
    
    /**
     *update view size
     */
    public void updateViewSize ()
	{
	  Rectangle r = mypanel.bounds();
	  v.updateSize (r.width, r.height);
	}

    /**
     *paint with wire frame only
     */
    public void wireframePaint ()
	{
	  int i, j;
	  Vector dlist = new Vector ();
	  dl_atom dla = null;
	  dl_bond dlb = null;
	  
	  for (i = 0; i < atomList.size (); i++)
	      {
		  atom a = (atom) atomList.elementAt (i);
		  if (a.currentNumBonds () == 0)
		      {
			  dla = new dl_atom (a, v);
			  dlist.addElement (dla);
		      }
	      }

	  for (i = 0; i < atomList.size (); i++)
	      {
		  atom a1 = (atom) atomList.elementAt (i);
		  for (j = 0; j < a1.bonds.size (); j++)
		      {
			  bond b = (bond) a1.bonds.elementAt(j);
			  atom a2 = b.otherAtom(a1);
			  if (a1.x[0] < a2.x[0])
			      {
				  dlb = new dl_bond (b, v);
				  dlist.addElement (dlb);
			      }
		      }
	      }
	  
	  if (dla != null) {
	      dla.quickpaint (dlist, mypanel.getGraphics ());
	  }
	  else if (dlb != null) {
	      dlb.quickpaint (dlist, mypanel.getGraphics ());
	  }
	}
      
 
    /**
     *consider the perspective factor
     */
    public double perspectiveFactor (double z)
    {
	double denom = perspDist - z;
	if (denom < 5) denom = 5;
	  return perspDist / denom;
    }

    /**
     *xyz to screen transformation
     */
/*    public double[] xyzToScreen (double[] xyz)
    {
	double x, y, z, denom;
	double[] rvec = new double[3];
	// rotate
	x = m[0] * xyz[0] + m[1] * xyz[1] + m[2] * xyz[2];
	y = m[3] * xyz[0] + m[4] * xyz[1] + m[5] * xyz[2];
	z = m[6] * xyz[0] + m[7] * xyz[1] + m[8] * xyz[2];
	// zoom
	x = zoomFactor * x;
	y = zoomFactor * y;
	z = zoomFactor * z;
	// perspective
	double perspective = perspectiveFactor (z);
	x *= perspective;
	y *= perspective;
	// translation
	rvec[0] = x + xCenter;
	rvec[1] = y + yCenter;
	rvec[2] = z + zCenter;
	return rvec;
    } */   

}
