package nanocad;
/**
 * atom.java - definition of an atom, elements are subclasses of atom
 * Copyright (c) 1997,1998 Will Ware, all rights reserved.
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
 * including, but not limited to, the implied warranties of merchantabilityx
 * or fitness for any particular purpose are disclaimed. In no event shall
 * Will Ware be liable for any direct, indirect, incidental, special,
 * exemplary, or consequential damages (including, but not limited to,
 * procurement of substitute goods or services; loss of use, data, or
 * profits; or business interruption) however caused and on any theory of
 * liability, whether in contract, strict liability, or tort (including
 * negligence or otherwise) arising in any way out of the use of this
 * software, even if advised of the possibility of such damage.
 */

import java.awt.Color;
import java.util.Vector;
import nanocad.util.*;

public abstract class atom
{

  public static final String rcsid =
	"$Id: atom.java,v 1.3 2007/11/16 20:11:31 srb Exp $";
	// hybridizations are a virtual enum
    public static final int SP3 = 0;
    public static final int SP2 = 1;
    public static final int SP  = 2;
    public static final int NONE = 3;
    private static final String hybridnames[] = { "SP3", "SP2", "SP", "NONE" };
    
	// these should be instance variables
    public int Charge;
    public double fractionalCharge;
    public int hybridization;
    public double[] x;
    //public static double[] x;
    public double[] v;
    public double[] f;
    public boolean isTetrahedral = false;
    public Vector bonds;
    public boolean isUFF = true;	// Determines if this is a UFF simulation
									// or an MM3 simulation.
    public boolean resonant = false;	// Flags whether this is part of a resonant chain.
    public int myGeom = 0;		// Flags the geometry of the atom:
								// 1 = linear, 2 = trigonal, 3 = tetrahedral,
								// 4 = square planar, 5 = bipyramidal, 6 = octahedral

    // type numbers for nanocad, mm3, uff
    private int ncadTypeNum, mm3TypeNum, uffTypeNum;
    private int ePairs, geom;
    private String ncadTypeString = "";
    private String mm3TypeString = "";
    private String uffTypeString = "";

    private group myGroup;
    
    private static AtomTypeFile typeFile;
    private boolean marked = false;
    private boolean highlighted = false;
    private boolean selected = false;
    private boolean keep = false;

    public atom ()
	{
	    hybridization = NONE;
	    bonds = new Vector();
	    Charge = 0;
	    fractionalCharge = 0.0;
	    double zvec[] = { 0.0, 0.0, 0.0 };
	    x = v = f = zvec;
	}

    public abstract int atomicNumber ();  
    
    public atom(atom a)
    {
	hybridization = a.hybridization;
	Charge = a.Charge;
	fractionalCharge = a.fractionalCharge;
	x = new double[3];
	v = new double[3];
	f = new double[3];

	for (int i = 0; i < 3; i++)
	{
	    x[i] = a.x[i];
	    v[i] = a.v[i];
	    f[i] = a.f[i];
	}
	selected = a.isSelected();
	bonds = new Vector();		//since the atom is a copy, it is not necessairly bonded to the same atoms
    }

    public bond bondWith (atom a)
    {
	  int i;
	  if (bonds == null)
	      return null;
	  for (i = 0; i < bonds.size(); i++)
	      {
		  bond b = (bond) bonds.elementAt(i);
		  if (b.contains(a))
		      return b;
	      }
	  return null;
    }
    
    public boolean isBondedWith(atom a)
    {
	for (int i = 0; i < bonds.size(); i++)
	{
	    if (((bond)bonds.elementAt(i)).a1 == a || ((bond)bonds.elementAt(i)).a2 == a)
		return true;
	}
	return false;
    }

    /***
     * method getBond(atom a)
     * returns the bond between this atom and atom a
     * created 6/08/02, jstrutz
    ***/
    public bond getBond(atom a){
	for (int i=0; i<bonds.size(); i++){
		if(((bond)bonds.elementAt(i)).a1 == a || ((bond)bonds.elementAt(i)).a2 == a)
			return (bond)bonds.elementAt(i);
	}
	System.out.println("ERROR: Bond not found for atom "+this.repr()+", "+a.repr());
	return null;
    }

	// These are implemented in seperate atom classes.
	public abstract Color color (); 
    public abstract int correctNumBonds ();  
    public abstract double covalentRadius ();  

	public int currentNumBonds ()
	{
	  int i, total;
	  if (bonds == null)
	      return 0;
	  for (i = total = 0; i < bonds.size(); i++)
	      total += ((bond) bonds.elementAt(i)).order();
	  return total;
	}

    public int currentSigmaBonds ()
    {
	  int i, total;
	  if (bonds == null)
	      return 0;
	  return bonds.size();
	}

    public group getGroup() {
	return myGroup;
    }

    public int setUFFTypeNum() {
	return setUFFTypeNum(false);
    }

    public int setUFFTypeNum(boolean withPopups){
	// Get a generic typenum based on the atomic number, and sort it out in the huge
	// switch statement that follows.
	try{
		// lookup the data using the atomic number
		AtomDataFile theFile = new AtomDataFile(newNanocad.txtDir + 
	    		newNanocad.fileSeparator + "uffdata.txt");
		//AtomDataFile theFile = new AtomDataFile("uffdata.txt");
		theFile.findData(atomicNumber(),0);
		uffTypeNum = theFile.parseInt(1);

		// Large amount of code to change the default values to other atom types 
		// for H,C,N,O,P,S,Ti,Fe,Mo,W,Re based on the geometry of the molecule
		// Incidentally, this method can also be used to figure out the charge
		// on a particular atom in a molecule.
		switch(uffTypeNum){
		case 1:		// Hydrogen
				if(currentNumBonds() == 2){
					// Bridging hydrogen
					uffTypeNum = 2;
				}
//				else uffTypeNum = 1;
				break;
		case 6:		// Boron
				ePairs = 5-currentNumBonds();
				geom = ePairs/2 + currentSigmaBonds();
				if (geom != 4)
				{   uffTypeNum = 7;
				}
				else isTetrahedral = true;
/*				if(geom == 4) uffTypeNum = 6; // tetrahedral
				else uffTypeNum = 7; // trigonal planar
*/
				break;
		case 8:		// Carbon
				ePairs = 4-currentNumBonds();
				geom = ePairs/2 + currentSigmaBonds();
				if (geom == 3) uffTypeNum = 10;
				else if (geom != 4) uffTypeNum = 11;
				else isTetrahedral = true;
/*				if(geom == 4) uffTypeNum = 8; // tetrahedral
				else if(geom == 3) uffTypeNum = 10; // trigonal planar
				else uffTypeNum = 11; // linear
*/
				break;
		case 12:	// Nitrogen
				ePairs = 5-currentNumBonds();
				geom = ePairs/2 + currentSigmaBonds();
				if (geom == 3) uffTypeNum = 14;
				else if (geom != 4) uffTypeNum = 15;
				else // check for amide nitrogens - stenhous 1/9/03
		                { for (int i=0; i < bonds.size(); i++)
                		  { atom carbcheck = ((bond)bonds.elementAt(i)).otherAtom(this);
                    		if (carbcheck.currentSigmaBonds() == 3 && carbcheck.atomicNumber() == 6)
                          		uffTypeNum = 13;
				}}
				if (uffTypeNum == 12) isTetrahedral = true;
/*				if(geom == 4) uffTypeNum = 12; // tetrahedral
				else if(geom == 3) uffTypeNum = 14; // trigonal planar
				else uffTypeNum = 15; // linear
*/
				break;
		case 16:	// Oxygen
				ePairs = 6-currentNumBonds();
				geom = ePairs/2 + currentSigmaBonds();
				if (geom == 3) uffTypeNum = 19;
				else if (geom != 4) uffTypeNum = 20;
				else isTetrahedral = true;
/*				if(geom == 4) uffTypeNum = 16; // tetrahedral
				else if(geom == 3) uffTypeNum = 19; // trigonal planar					
				else uffTypeNum = 20; // linear
*/
				break;
		case 27:	// Phosphorus
				int myBonds = currentSigmaBonds();
				if(myBonds == 4){
					// +3 ox state
					// Cycle through all bonds to find if this is bonded
					// to a transition metal.  If so, uffTypeNum = 29.
					boolean isMetal = false;
					int i = 0;
					while((i < bonds.size()) && (!isMetal)){
						bond myBond = ((bond)(bonds.elementAt(i)));
						atom a;
						if(myBond.a1 == this) a = myBond.a2;
						else a = myBond.a1;
						int an = a.atomicNumber();
						if( ((an > 22) && (an < 31)) || ((an > 38) && (an < 49)) ||
							((an > 56) && (an < 81)) || (an > 88) ){
							// This is an organo-metallic phosphine
							isMetal = true;
						}
						i++;
					}
					if(isMetal) uffTypeNum = 29;	// else uffTypeNum = 27;
				}
				else if(myBonds == 3){
					// +5 ox state
					uffTypeNum = 28;
				}
				break;
		case 30:	// Sulfur
				myBonds = currentNumBonds();
				ePairs = 6-currentNumBonds();
				geom = ePairs/2 + currentSigmaBonds();
				if (geom != 4) uffTypeNum = 34;
				else isTetrahedral = true;
				break;
		case 40:	// Titanium
				myBonds = currentSigmaBonds();
				if(myBonds == 6){
					// octahedral
					uffTypeNum = 41;
				}
				else isTetrahedral = true;
				break;
		case 45:	// Iron
				myBonds = currentSigmaBonds();
				if(myBonds == 6){
					// octahedral
					uffTypeNum = 46;
				}
				else isTetrahedral = true;
				break;
		case 62:	// Molybdenum
				myBonds = currentSigmaBonds();
				if(myBonds == 4){
					// tetrahedral
					uffTypeNum = 63;
					isTetrahedral = true;
				}
				break;
		case 95:	// Tungsten
				myBonds = currentSigmaBonds();
				if(myBonds == 4){
					// tetrahedral
					uffTypeNum = 96;
					isTetrahedral = true;
				}
				break;
		case 98:	// Rhenium
				myBonds = currentSigmaBonds();
				if(myBonds == 4){
					// tetrahedral
					uffTypeNum = 97;
					isTetrahedral = true;
				}
				break;
		default:
		}

		theFile.findData(uffTypeNum, 1);
		myGeom = theFile.parseInt(8);
		uffTypeString = "UFF: #"+uffTypeNum+" "+theFile.parseString(9);
	}
	catch(Exception e){
		System.out.println("Error in atom type lookup");
		uffTypeString = "UFF: Type Not Found, using Hydrogen";
		uffTypeNum = 1;
		e.printStackTrace();
	}
	return uffTypeNum;
    }

    public void setUFFResonance(){
	// Resonant only if sp2, bonded to two other sp2 atoms.  The
	// other sp2 atoms bonded to it are also resonant.
	System.out.println("Setting resonance");
	// In some circumstances, the apporder might have already been set
	// as resonant.  If the atom becomes non-resonant, the apporder should
	// become _order.  This takes care of it.

	// Cycle through all bonds, and set apporder to _order.
	for(int i = 0; i < bonds.size(); i++){
		bond b = (bond)(bonds.elementAt(i));
		b.setAppOrder(b.order());
	}
	if(myGeom == 2){
		System.out.println("Found sp2");
		int numSP2 = 0;			// number of sp2 atoms bonded to this
		atom[] resAtoms = new atom[2];	// the actual sp2 atoms bonded to this
		// this is sp2
		// cycle through all bonds, and check if that atom is sp2.
		// Store that atom, and set it resonant if necessary.
		for(int i=0; ((i < bonds.size()) && (numSP2 < 2)); i++){
			bond b = (bond)(bonds.elementAt(i));
			atom a;
			if(b.a1 == this) a = b.a2;
			else a = b.a1;
			if(a.myGeom == 2){
				resAtoms[numSP2] = a;
				numSP2 ++;
			}
		}
		if(numSP2 == 2){
			System.out.println("Found resonant structure");
			// Found resonance!
			resonant = true;
			resAtoms[0].resonant = true;
			resAtoms[1].resonant = true;

			switch(this.uffTypeNum){
			case 10: // carbon
				uffTypeNum = 9;
				break;
			case 14: // nitrogen
				uffTypeNum = 13;
				break;
			case 19: // oxygen
				uffTypeNum = 18;
				break;
			case 34: // sulfur
				uffTypeNum = 33;
				break;
			default:
			}

			switch(resAtoms[0].uffTypeNum){
			case 10: resAtoms[0].uffTypeNum = 9; break;  // C
			case 14: resAtoms[0].uffTypeNum = 13; break; // N
			case 19: resAtoms[0].uffTypeNum = 18; break; // O
			case 34: resAtoms[0].uffTypeNum = 33; break; // S
			default:
			}

			switch(resAtoms[1].uffTypeNum){
			case 10: resAtoms[1].uffTypeNum = 9; break;  // C
			case 14: resAtoms[1].uffTypeNum = 13; break; // N
			case 19: resAtoms[1].uffTypeNum = 18; break; // O
			case 34: resAtoms[1].uffTypeNum = 33; break; // S
			default:
			}

			// Have to change the typeStrings:
			try{
				//AtomDataFile theFile = new AtomDataFile("uffdata.txt");
				AtomDataFile theFile = new AtomDataFile(newNanocad.txtDir + 
			    		newNanocad.fileSeparator + "uffdata.txt");
				theFile.findData(uffTypeNum,1);
				uffTypeString = "UFF: #"+uffTypeNum+" "+theFile.parseString(9);

				theFile.findData(resAtoms[0].uffTypeNum,1);
				resAtoms[0].uffTypeString = "UFF: #"+resAtoms[0].uffTypeNum+" "+theFile.parseString(9);

				theFile.findData(resAtoms[1].uffTypeNum,1);
				resAtoms[1].uffTypeString = "UFF: #"+resAtoms[1].uffTypeNum+" "+theFile.parseString(9);
			}
			catch(Exception e){
				System.out.println("ERROR: Cannot find UFF data in resonance detection.");
				uffTypeString = uffTypeString + " Resonant";
				resAtoms[0].uffTypeString = resAtoms[0].uffTypeString + " Resonant";
				resAtoms[1].uffTypeString = resAtoms[1].uffTypeString + " Resonant";
				e.printStackTrace();
			}

			// Have to set apparent bond orders
			getBond(resAtoms[0]).setAppOrder(1.5);
			getBond(resAtoms[1]).setAppOrder(1.5);
		}
	}
    }

    public int setMM3TypeNum() { return setMM3TypeNum(false); }
    
    public int setMM3TypeNum(boolean withPopups) {
		int typeNum;
		try
		    {
			//AtomDataFile file = new AtomDataFile("pmm3convert.txt");
			AtomDataFile file = new AtomDataFile(newNanocad.txtDir + 
		    		newNanocad.fileSeparator + "pmm3convert.txt");
			typeNum = setTypeNum(withPopups);
			System.out.println("------------------------");
			System.out.println("ncadTypeNum = "+typeNum);
			System.out.println("------------------------");
			if(typeNum < 0)
			{
				ncadTypeString = "NCAD: Type Not Found";
				mm3TypeString = "MM3: Type Not Found";
				typeNum = -1;
				mm3TypeNum = -1;
				return -1;
			}

			file.skipLines(typeNum);
			mm3TypeNum = file.parseInt(1);
			if (mm3TypeNum == 50)
			    mm3TypeNum = 2;
			if (mm3TypeNum == 5)
			{
			    for(int i = 0; i < bonds.size(); i++)
			    {
				atom testAtom = ((bond)bonds.elementAt(i)).otherAtom(this);
				if (testAtom.atomicNumber() == 7) // N
					mm3TypeNum = 23;
				else if (testAtom.atomicNumber() == 8) // O
					mm3TypeNum = 21;
				else if (testAtom.atomicNumber() == 16) // S
					mm3TypeNum = 44;
			    }
	 		}
			ncadTypeString = "NCAD: #"+typeNum;
			mm3TypeString = "MM3: #"+mm3TypeNum+" "+file.parseString(2);
		}
		catch(Exception e)
		{
			System.err.println("Error occurred in MM3 Type Conversion read.");
			ncadTypeString = "NCAD: Type Not Found";
			mm3TypeString = "MM3: Type Not Found";
			e.printStackTrace();
			mm3TypeNum = -1;
			return -1;
		}
		return mm3TypeNum;
	}

	public int setTypeNum(boolean withPopups)
	{
		verifyTypeFile();
		if(withPopups) ncadTypeNum =  typeFile.getType(myGroup, this);
		else ncadTypeNum = typeFile.getTypeWithoutPopups(myGroup, this);
		return ncadTypeNum;
	}

	public int getTypeNum(){ return getTypeNum(false); }
	public int getTypeNum(boolean withPopups)
	{
		verifyTypeFile();
		int typeNum;
 
		if(withPopups)
			typeNum = typeFile.getType(myGroup, this);
		else
			typeNum = typeFile.getTypeWithoutPopups(myGroup, this);
    
		return typeNum;
	}

	public int getMM3TypeNum(){ return mm3TypeNum; }
	public int getUFFTypeNum(){ return uffTypeNum; }
	public String getUFFTypeString() { return uffTypeString; }
	public int index () { return myGroup.atomList.indexOf (this); }

/**
 * Insert the method's description here.
 * Creation date: (7/19/2000 2:19:42 PM)
 * @return boolean
 */
    public boolean isHighlighted() { return highlighted; }

/**
 * Insert the method's description here.
 * Creation date: (7/7/2000 4:04:26 PM)
 * @return boolean
 */
  	public boolean isMarked() { return marked; }
	public boolean isSelected() { return selected; }
    public boolean isKept() { return keep; }

    public abstract double mass ();  
    // these should be defined within elements, as class variables

    public abstract String name ();  
    // overload me, unless I'm hydrogen

    public void rehybridize ()
	{
	    hybridization = NONE;
	}
    
    public String repr()
    {
	return "<" + symbol() + " " +
	hybridnames[hybridization] + " " +
	    (new Double(x[0])).toString() + " " +
	    (new Double(x[1])).toString() + " " +
	    (new Double(x[2])).toString() + ">";
    }
    
    public void setGroup (group g)
    {
	  myGroup = g;
    }
    
    /**
     * Insert the method's description here.
     * Creation date: (7/19/2000 2:19:42 PM)
     * @param newHighlighted boolean
     */
    
    public void setHighlighted(boolean newHighlighted) {
	highlighted = newHighlighted;
    }

    /**
     * Insert the method's description here.
     * Creation date: (7/7/2000 4:04:26 PM)
     * @param newMarked boolean
     */

    //for shift
    public void setMarked(boolean newMarked) {
	marked = newMarked;
    }
  
    //for alt  
    public void setSelected(boolean newSelected){
	selected = newSelected;
    }

    public void setKept(boolean newKept){
	keep = newKept;
    }
    
    public abstract String symbol ();  
    public String toString()
    {
	// Checks to see if the types need to be determined, then
	// sets types for all atoms before returning the typeString.
	if(myGroup.needsToGetTypes()) myGroup.setTypes();
	return ncadTypeString+" "+mm3TypeString+" "+uffTypeString;
    }
    
    public abstract double vdwEnergy ();  
    public abstract double vdwRadius ();  
    /**
     * Insert the method's description here.
     * Creation date: (6/26/00 10:47:01 AM)
	 */

	private static void verifyTypeFile()
	{
		if(typeFile == null)
		{
			typeFile = new AtomTypeFile();
			typeFile.readFile("aData2.dat");
		}
	}

    public void zeroForce ()
	{
	  double newf[] = { 0.0, 0.0, 0.0 };  // Why don't we just set f = {0.0,0.0,0.0} ?
	  f = newf;
	}
}
