////////////////////////////////
// strtorterm.java
// written by Andrew Knox
// May 29, 2002
// the stretch-torsion term for mm3 energy minimization
////////////////////////////////

package nanocad.minimize.mm3;

import java.lang.Math;
import java.util.Vector;
import nanocad.*;

public class strtorterm extends nanocad.term
{
    private double v1, v2, v3, r0;
    public static double strtorunit = -5.9975;
    public strtorterm() {}

    public strtorterm(atom a1, atom a2, atom a3, atom a4) 
    {
	int i;
	boolean found;
	myAtoms = new atom[4];
	myAtoms[0] = a1;
	myAtoms[1] = a2;
	myAtoms[2] = a3;
	myAtoms[3] = a4;
	setCalculationValues();
    }

    protected void buildTerm (Vector v, Vector termList)
    {
	int atom1 = ((atom)v.elementAt(0)).getMM3TypeNum();
	int atom2 = ((atom)v.elementAt(1)).getMM3TypeNum();
	int atom3 = ((atom)v.elementAt(2)).getMM3TypeNum();
	int atom4 = ((atom)v.elementAt(3)).getMM3TypeNum();  
	
	if (atom2 < atom3 && atom1 < atom4)
	{
	    strtorterm t = new strtorterm ((atom) v.elementAt(0),
		   (atom) v.elementAt(1),
		   (atom) v.elementAt(2),
		   (atom) v.elementAt(3));

		
	    if (t.v3 != 0.0 || t.r0 != 0.0)
	    {
		termList.addElement(t);
		System.out.println("added stor: " + atom1 + " " + atom2 + " " + atom3 + " " + atom4);
	    }
	}
	else if (atom2 == atom3 && atom1 < atom4)
	{
	    if ( ((atom)v.elementAt(1)).x[0] >= ((atom)v.elementAt(2)).x[0])              
	    {
		strtorterm t = new strtorterm ((atom) v.elementAt(0),           
			(atom) v.elementAt(1),
			(atom) v.elementAt(2),
			(atom) v.elementAt(3));
		
	    	if (t.v3 != 0.0 || t.r0 != 0.0)
	    	{
		    termList.addElement(t);
		    System.out.println("added stor: " + atom1 + " " + atom2 + " " + atom3 + " " + atom4);
	    	}
	    }
	}
	else if (atom2 == atom3 && atom1 == atom4)
	{
	    if ( ((atom)v.elementAt(1)).x[0] >= ((atom)v.elementAt(2)).x[0] )
	    {
		
		strtorterm t = new strtorterm ((atom) v.elementAt(0),           
			(atom) v.elementAt(1),
			(atom) v.elementAt(2),
			(atom) v.elementAt(3));
		
	    	if (t.v3 != 0.0 || t.r0 != 0.0)
	    	{
		    termList.addElement(t);
		    System.out.println("added stor: " + atom1 + " " + atom2 + " " + atom3 + " " + atom4);
		}
	    }
	}
    }

    public double computeForces()
    {
    	double ab[], bc[], cd[], na[], nd[], nadotna, nddotnd, nadotnd, theta, dedth, dthdab[], dthdbc[], dthdcd[], denominator;
	double r, dedr, drdb[], drdc[];
	ab = new double[3]; bc = new double[3]; cd = new double[3]; na = new double[3]; nd = new double[3]; dthdab = new double[3];
	dthdbc = new double[3]; dthdcd = new double[3]; drdb = new double[3]; drdc = new double[3];

	for (int i = 0; i < 3; i++)
	{
	    ab[i] = myAtoms[1].x[i] - myAtoms[0].x[i];
	    bc[i] = myAtoms[2].x[i] - myAtoms[1].x[i];
	    cd[i] = myAtoms[3].x[i] - myAtoms[2].x[i];	  
	}

	na[0] = ab[1]*bc[2] - ab[2]*bc[1];
	na[1] = ab[2]*bc[0] - ab[0]*bc[2];
	na[2] = ab[0]*bc[1] - ab[1]*bc[0];

	nd[0] = bc[1]*cd[2] - bc[2]*cd[1];
	nd[1] = bc[2]*cd[0] - bc[0]*cd[2];
	nd[2] = bc[0]*cd[1] - bc[1]*cd[0];

	nadotna = na[0]*na[0] + na[1]*na[1] + na[2]*na[2];
	nadotnd = na[0]*nd[0] + na[1]*nd[1] + na[2]*nd[2];
	nddotnd = nd[0]*nd[0] + nd[1]*nd[1] + nd[2]*nd[2];

	theta = Math.acos(nadotnd / Math.sqrt(nadotna * nddotnd));
	r = Math.sqrt(bc[0]*bc[0] + bc[1]*bc[1] + bc[2]*bc[2]);

	dedr = ( v1/2 * (1 + Math.cos(theta)) + v2/2 * (1 + Math.cos(2 * theta)) + v3/2 * (1 + Math.cos(3 * theta)) );
	dedth = (r - r0) * (-v1 / 2 * Math.sin(theta) - v2 * Math.sin(2 * theta) - v3 * 3 / 2 * Math.sin(3 * theta));   

	denominator = Math.sqrt(nadotna * nddotnd - nadotnd * nadotnd);

	dthdab[0] = (nd[1]*bc[2] - nd[2]*bc[1] + (na[2]*bc[1] - na[1]*bc[2]) * nadotnd / nadotna) / denominator;
	dthdbc[0] = (nd[2]*ab[1] - nd[1]*ab[2] + na[1]*cd[2] - na[2]*cd[1] + (na[1]*ab[2] - na[2]*ab[1]) * nadotnd / nadotna
		+ (nd[2]*cd[1] - nd[1]*cd[2]) * nadotnd / nddotnd) / denominator;
	dthdcd[0] = (na[2]*bc[1] - na[1]*bc[2] + (nd[1]*bc[2] - nd[2]*bc[1]) * nadotnd / nddotnd) / denominator;

	dthdab[1] = (nd[2]*bc[0] - nd[0]*bc[2] + (na[0]*bc[2] - na[2]*bc[0]) * nadotnd / nadotna) / denominator;
	dthdbc[1] = (nd[0]*ab[2] - nd[2]*ab[0] + na[2]*cd[0] - na[0]*cd[2] + (na[2]*ab[0] - na[0]*ab[2]) * nadotnd / nadotna
		+ (nd[0]*cd[2] - nd[2]*cd[0]) * nadotnd / nddotnd) / denominator;
	dthdcd[1] = (na[0]*bc[2] - na[2]*bc[0] + (nd[2]*bc[0] - nd[0]*bc[2]) * nadotnd / nddotnd) / denominator;
	
	dthdab[2] = (nd[0]*bc[1] - nd[1]*bc[0] + (na[1]*bc[0] - na[0]*bc[1]) * nadotnd / nadotna) / denominator;
	dthdbc[2] = (nd[1]*ab[0] - nd[0]*ab[1] + na[0]*cd[1] - na[1]*cd[0] + (na[0]*ab[1] - na[1]*ab[0]) * nadotnd / nadotna
		+ (nd[1]*cd[0] - nd[0]*cd[1]) * nadotnd / nddotnd) / denominator;
	dthdcd[2] = (na[1]*bc[0] - na[0]*bc[1] + (nd[0]*bc[1] - nd[1]*bc[0]) * nadotnd / nddotnd) / denominator;

	for (int i = 0; i < 3; i++)
	{
	    drdb[i] = -bc[i] / r;
	    drdc[i] = bc[i] / r;
	}

	for (int i = 0; i < 3; i++)
	{
	    myAtoms[0].f[i] += (dedth * -dthdab[i]);
	    myAtoms[1].f[i] += (dedth * (dthdab[i] - dthdbc[i]) + dedr * drdb[i]);
	    myAtoms[2].f[i] += (dedth * (dthdbc[i] - dthdcd[i]) + dedr * drdc[i]);
	    myAtoms[3].f[i] += (dedth * dthdcd[i]);
	}
	return myAtoms[0].f[0];
    }

    protected double computePotential(double theta, double r)
    {
	return (r - r0) * (v1/2 * (1+Math.cos(theta)) + v2/2 * (1+Math.cos(2*theta)) + v3/2 * (1+Math.cos(3*theta)) );
    }

    protected void setCalculationValues() 
    {
	int atomOneType = myAtoms[0].getMM3TypeNum();
        int atomTwoType = myAtoms[1].getMM3TypeNum();
   	int atomThreeType = myAtoms[2].getMM3TypeNum();
	if (atomOneType < 0 || atomTwoType < 0) 
	{
	    setDefaultCalculationValues();
	    return;
	}

	try
	{
	    AtomDataFile dataFile = new AtomDataFile(newNanocad.txtDir + 
    			newNanocad.fileSeparator + "mm3ldata.txt");
	    //AtomDataFile dataFile = new AtomDataFile("mm3ldata.txt");
	    if (dataFile.findData(atomTwoType, atomThreeType, 0, 1))
	    {
		r0 = dataFile.parseDouble(3);
	    }
//	    else if (dataFile.findData(atomThreeType, atomTwoType, 0, 1))
//	    {
//		r0 = dataFile.parseDouble(3);
//	    }

	    dataFile = new AtomDataFile(newNanocad.txtDir + 
    			newNanocad.fileSeparator + "mm3strtordata.txt");
	    //dataFile = new AtomDataFile("mm3strtordata.txt");
	    if (dataFile.findData(atomTwoType, atomThreeType, 0, 1))
	    {
		v1 = dataFile.parseDouble(2);
		v2 = dataFile.parseDouble(3);
		v3 = dataFile.parseDouble(4);
	    }
	} 
	catch (java.io.IOException e) {
	    System.err.println("Stretch-torsion data lookup error");
	    e.printStackTrace();
	}
    }

    public void setDefaultCalculationValues()
    {
	defaultsUsed = true;
	v3 = 0.0;
	System.out.println("No data for " + myAtoms[0].getMM3TypeNum() + " " + myAtoms[1].getMM3TypeNum() + " " + myAtoms[2].getMM3TypeNum() + " " + myAtoms[3].getMM3TypeNum() + " torsion");
    }
    
    public int termLength()
    {
	return 4;
    }
  
    public String name()
    {
	return "Stretch-Torsion";
    }
}
