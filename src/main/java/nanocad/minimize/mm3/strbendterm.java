////////////////////////////////////
// strbend.java
// written 8/6/01 by Andrew Knox
// implements stretch-bend term of 
// energy minimization
////////////////////////////////////

package nanocad.minimize.mm3;

import java.lang.Math;
import java.util.Vector;
import nanocad.*;

public class strbendterm extends nanocad.term
{
    private final double stbnunit =  2.51118;
    private final double convert = 3.1415926 / 180;
    private double idealAngle;
    private double idealLength1;
    private double idealLength2;
    private double kBondAngle;

    public strbendterm()  {}

    public strbendterm(atom center, atom other1, atom other2)
    {
	myAtoms = new atom[3];
        myAtoms[0] = center;
	myAtoms[1] = other1;
	myAtoms[2] = other2;
	setCalculationValues();
    }
	

    public void buildTerm(Vector v, Vector termList)
    {
	int atom1 = ((atom)v.elementAt(0)).getMM3TypeNum();
	int atom2 = ((atom)v.elementAt(1)).getMM3TypeNum();
	int atom3 = ((atom)v.elementAt(2)).getMM3TypeNum();
	
	strbendterm b = new strbendterm((atom)v.elementAt(0), (atom)v.elementAt(1), (atom)v.elementAt(2));
	if ( b.kBondAngle != 0.0 && b.idealLength1 != 0.0 && b.idealLength2 != 0.0 && b.idealAngle != 0.0)
	{
	    if (atom1 == atom3)
	    {
	        if (((atom)v.elementAt(0)).x[0] < ((atom)v.elementAt(2)).x[0])
		{
		    termList.addElement(b);
		    System.out.println("added:" + atom1 + " " + atom2 + " " + atom3);
		    System.out.println("k:" + b.kBondAngle + " angle:" + b.idealAngle + " len1:" + b.idealLength1 + " len2:" +b.idealLength2);
		}
	    }
	    else
	    {
		termList.addElement(b);
		System.out.println("added:" + atom1 + " " + atom2 + " " + atom3);
	    }
	}
    }

    public double computeForces()
    {
	double ab[], bc[], abdotbc, abdotab, bcdotbc, dotnorm, angle, dedr, deds, dedth, s, r, 
	drda[], drdb[], dsdb[], dsdc[], dthda[], dthdb[], dthdc[], denominator;
	ab = new double[3]; bc = new double[3]; drda = new double[3]; drdb = new double[3]; dsdb = new double[3];
	dsdc = new double[3]; dthda = new double[3]; dthdb = new double[3]; dthdc = new double[3];

	for (int i = 0; i < 3; i++)
	{
	    ab[i] = myAtoms[0].x[i] - myAtoms[1].x[i];
	    bc[i] = myAtoms[2].x[i] - myAtoms[1].x[i];
	}

	abdotbc = ab[0] * bc[0] + ab[1] * bc[1] + ab[2] * bc[2];
	abdotab = ab[0] * ab[0] + ab[1] * ab[1] + ab[2] * ab[2];
	bcdotbc = bc[0] * bc[0] + bc[1] * bc[1] + bc[2] * bc[2];
	r = Math.sqrt(abdotab);
	s = Math.sqrt(bcdotbc);

	angle = Math.acos(abdotbc / (r * s));

	dedr = deds = stbnunit * kBondAngle * (angle - idealAngle);
	dedth = stbnunit * kBondAngle * (r - idealLength1 + s - idealLength2);
	
	denominator = Math.sqrt(abdotab * bcdotbc - abdotbc * abdotbc);

	for (int i = 0; i < 3; i++)
	{
	    drda[i] = ab[i] / r;
	    drdb[i] = -drda[i];
	    dsdc[i] = bc[i] / r;
	    dsdb[i] = -dsdc[i];
	    dthda[i] = (bc[i] - ab[i] * abdotbc / abdotab) / denominator;
	    dthdc[i] = (ab[i] - bc[i] * abdotbc / abdotab) / denominator;
	    dthdb[i] = -(dthda[i] + dthdc[i]);

	    myAtoms[0].f[i] += dedr * drdb[i] + deds * dsdb[i] + dedth * dthdb[i];
	    myAtoms[1].f[i] += dedr * drda[i] + dedth * dthda[i];
	    myAtoms[2].f[i] += deds * dsdc[i] + dedth * dthdc[i];
	}

	return myAtoms[0].f[0];
    }


    public void setCalculationValues()
    {
	int atom1 = myAtoms[0].getMM3TypeNum();
	int atom2 = myAtoms[1].getMM3TypeNum();
	int atom3 = myAtoms[2].getMM3TypeNum();

	if (atom1 < atom3 || atom1 < 1 || atom2 < 1 || atom3 < 1)		
	{    
	    kBondAngle = 0.0;
	    return;
	}
	
	try
	{
	    AtomDataFile dataFile = new AtomDataFile(newNanocad.txtDir + 
    			newNanocad.fileSeparator + "mm3strbenddata.txt");
	    //AtomDataFile dataFile = new AtomDataFile("mm3strbenddata.txt");
	    if (dataFile.findData(atom1, 0))
		kBondAngle = dataFile.parseDouble(1);
	    else
	    {
		kBondAngle = 0.0;
		return;
	    }
	    
	    dataFile = new AtomDataFile(newNanocad.txtDir + 
    			newNanocad.fileSeparator + "mm3adata.txt");
	    //dataFile = new AtomDataFile("mm3adata.txt");
	    if (dataFile.findData(atom2, atom1, atom3, 1, 0, 2))
		idealAngle = convert * dataFile.parseDouble(4);
	    else
	    {
		idealAngle = 0.0;
		return;
	    }

	    dataFile = new AtomDataFile(newNanocad.txtDir + 
    			newNanocad.fileSeparator + "mm3ldata.txt");
	    //dataFile = new AtomDataFile("mm3ldata.txt");
	    if (dataFile.findData(Math.min(atom1, atom2), Math.max(atom1, atom2), 0, 1))
		idealLength1 = dataFile.parseDouble(3);
	    else
	    {
		idealLength1 = 0.0;
		return;
	    }

	    if (dataFile.findData(Math.min(atom2, atom3), Math.max(atom2, atom3), 0, 1))
		idealLength2 = dataFile.parseDouble(3);
	    else
	    {
		idealLength2 = 0.0;
		return;
	    }
	} catch(java.io.IOException e)
	{
	    System.err.println("Stretch-bend data lookup error");
	    e.printStackTrace();
	}
    }

    public int termLength()
    {
	return 3;
    }
    
    public String name()
    {
	return "Stretch-bend";
    }
}	
