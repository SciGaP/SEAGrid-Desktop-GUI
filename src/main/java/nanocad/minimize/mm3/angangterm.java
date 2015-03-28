package nanocad.minimize.mm3;

import java.io.FileReader;
import java.lang.Math;
import java.util.Vector;
import nanocad.*;

public class angangterm extends nanocad.term
{
    private static final double angangunit = -0.02191418;
    private static final double convert = 3.1415926 / 180;
    private double k, idealAngle1, idealAngle2;
    public static final double B = .014;
    public final static double C = .00005;
    public final static double D = .0000007;
    public final static double E = .0000000009;

    public angangterm() {}
    public angangterm (atom center, atom both, atom left, atom right)
    {
	myAtoms = new atom[4];
	myAtoms[0] = center;
	myAtoms[1] = both;
	myAtoms[2] = left;
	myAtoms[3] = right;
	setCalculationValues();
    }

    public void enumerate(Vector atomList, Vector termList)
    {
	for (int i = 0; i < atomList.size(); i++)
	{
	    if ( ((atom)atomList.elementAt(i)).bonds.size() == 3)
	    {
		atom tmp;
		Vector v = new Vector();
		v.addElement((atom)atomList.elementAt(i));
		for (int j = 0; j < 3; j++)
		{
		    if (((bond)((atom)v.elementAt(0)).bonds.elementAt(j)).a1 == (atom)v.elementAt(0))
			v.addElement(((bond)((atom)v.elementAt(0)).bonds.elementAt(j)).a2);
		    else
			v.addElement(((bond)((atom)v.elementAt(0)).bonds.elementAt(j)).a1);
		}
		this.buildTerm(v, termList);
	    }
	}
    }

    protected void buildTerm(Vector v, Vector termList)
    {
	angangterm b = new angangterm((atom)v.elementAt(0), (atom)v.elementAt(1), (atom)v.elementAt(2), (atom)v.elementAt(3));
	if (b.k != 0.0)
	{
	    termList.addElement(b);
	}
	b = new angangterm((atom)v.elementAt(0), (atom)v.elementAt(2), (atom)v.elementAt(3), (atom)v.elementAt(1));
	if (b.k != 0.0)
	{
	    termList.addElement(b);
	}
	b = new angangterm((atom)v.elementAt(0), (atom)v.elementAt(3), (atom)v.elementAt(1), (atom)v.elementAt(2));
	if (b.k != 0.0)
	{
	    termList.addElement(b);
	}
    }

    public double computeForces()
    {
	double[] ab = new double[3]; double[] bc = new double[3]; double[] bd = new double[3]; double[] dthda = new double[3]; double[] dthdb = new double[3];
	double[] dthdc = new double[3]; double[] dalda = new double[3]; double[] daldb = new double[3]; double daldd[] = new double[3];
	double theta, alpha, abdotbc, abdotbd, abdotab, bcdotbc, bddotbd, dedth, dedal, cdenominator, ddenominator;

	for (int i = 0; i < 3; i++)
	{
	    ab[i] = myAtoms[1].x[i] - myAtoms[0].x[i];
	    bc[i] = myAtoms[2].x[i] - myAtoms[0].x[i];
	    bd[i] = myAtoms[3].x[i] - myAtoms[0].x[i];
	}
	
	abdotab = ab[0]*ab[0] + ab[1]*ab[1] + ab[2]*ab[2];
	abdotbc = ab[0]*bc[0] + ab[1]*bc[1] + ab[2]*bc[2];
	abdotbd = ab[0]*bd[0] + ab[1]*bd[1] + ab[2]*bd[2];
	bcdotbc = bc[0]*bc[0] + bc[1]*bc[1] + bc[2]*bc[2];
	bddotbd = bd[0]*bd[0] + bd[1]*bd[1] + bd[2]*bd[2];

	if(abdotab <= 0) abdotab = mm3MinimizeAlgorythm.TINY;
	if(bcdotbc <= 0) bcdotbc = mm3MinimizeAlgorythm.TINY;
	if(bddotbd <= 0) bddotbd = mm3MinimizeAlgorythm.TINY;

	// Makes sure we don't get a divide by 0 or sqrt(negative).
	double jtemp0 = abdotab * bcdotbc;
	if(jtemp0 <= 0) jtemp0 = mm3MinimizeAlgorythm.TINY;
	double jtemp1 = abdotab * bddotbd;
	if(jtemp1 <= 0) jtemp1 = mm3MinimizeAlgorythm.TINY;
	theta = Math.acos(abdotbc / Math.sqrt(jtemp0));
	alpha = Math.acos(abdotbd / Math.sqrt(jtemp1));

	dedth = angangunit * k * (alpha - idealAngle2);
	dedal = angangunit * k * (theta - idealAngle1);
 
	double jtemp2 = abdotab * bcdotbc - abdotbc * abdotbc;
	if(jtemp2 == 0) jtemp2 = mm3MinimizeAlgorythm.TINY;
	double jtemp3 = abdotab * bddotbd - abdotbd * abdotbd;
	if(jtemp3 == 0) jtemp3 = mm3MinimizeAlgorythm.TINY;
	cdenominator = Math.sqrt(jtemp2);
	ddenominator = Math.sqrt(jtemp3);

	for (int i = 0; i < 3; i++)
	{
	    dthda[i] = (bc[i] - abdotbc * ab[i] / abdotab) / cdenominator;
	    dthdc[i] = (ab[i] - abdotbc * bc[i] / abdotab) / cdenominator;
	    dthdb[i] = -(dthda[i] + dthdc[i]);
	    dalda[i] = (bd[i] - abdotbd * ab[i] / abdotab) / ddenominator;
	    daldd[i] = (ab[i] - abdotbd * bd[i] / abdotab) / ddenominator;
	    daldb[i] = -(dalda[i] + daldd[i]);
	
	    myAtoms[0].f[i] += dedth * dthdb[i] + dedal * daldb[i];
	    myAtoms[1].f[i] += dedth * dthda[i] + dedal * dalda[i];
	    myAtoms[2].f[i] += dedth * dthdc[i];
	    myAtoms[3].f[i] += dedal * daldd[i];
	}

	potential = computePotential(theta,alpha);
	return myAtoms[0].f[0];
    }

    public double computePotential(double theta, double alpha){
	return k*angangunit*(theta-idealAngle1)*(alpha-idealAngle2);
    }
    
    public void setCalculationValues()
    {
	int atomCenterType = myAtoms[0].getMM3TypeNum();
	int atomBothType = myAtoms[1].getMM3TypeNum();
	int atomLeftType = myAtoms[2].getMM3TypeNum();
	int atomRightType = myAtoms[3].getMM3TypeNum();

	try
	{
	    AtomDataFile dataFile = new AtomDataFile(newNanocad.txtDir + 
    			newNanocad.fileSeparator + "mm3angangdata.txt");
	    //AtomDataFile dataFile = new AtomDataFile("mm3angangdata.txt");
	    if (dataFile.findData(atomCenterType, 0))
		k = dataFile.parseDouble(2);
	    else
	    {
		//System.out.println("no data for " + atomBothType + " " + atomCenterType + " angle angle");
		k = 0.0;
	    }
	
	    dataFile = new AtomDataFile(newNanocad.txtDir + 
    			newNanocad.fileSeparator + "mm3adata.txt");
	    //dataFile = new AtomDataFile("mm3adata.txt");
	    if (dataFile.findData(atomCenterType, min(atomBothType, atomLeftType), max(atomBothType, atomLeftType), 1, 0, 2))
	        idealAngle1 = convert * dataFile.parseDouble(4);
	    else
	    {
		//System.out.println("no data for " + atomBothType + " " + atomCenterType + " angle angle");
		k = 0.0;
	    }

	    dataFile = new AtomDataFile(newNanocad.txtDir + 
    			newNanocad.fileSeparator + "mm3adata.txt");
	    //dataFile = new AtomDataFile("mm3adata.txt");
	    if (dataFile.findData(atomCenterType, min(atomBothType, atomRightType), max(atomBothType, atomRightType), 1, 0, 2))
		idealAngle2 = convert * dataFile.parseDouble(4);
	    else
	    {
		//System.out.println("no data for " + atomBothType + " " + atomCenterType + " angle angle");
		k = 0.0;
	    }
	}    
	catch (java.io.IOException e)
	{
	    System.err.println("Bend data lookup error");
	    e.printStackTrace();
	}
    }     

    public int min(int a, int b)
    {
	if (a < b) 
	    return a;
	return b;
    }

    public int max(int a, int b)
    {
	if (a > b)
	    return a;
	return b;
    }

    public int termLength()
    {
	return 4;
    }

    public String name()
    {
	return "Angle Angle";
    }
}
