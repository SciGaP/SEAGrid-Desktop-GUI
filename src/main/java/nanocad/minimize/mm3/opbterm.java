/******************************************
 *opbterm.java -- nanocad
 *Andrew Knox 06/25/01
 *implements out of plane angle bend term
 ******************************************/

package nanocad.minimize.mm3;

import java.lang.Math;
import java.util.Vector;
import nanocad.*;

public class opbterm extends nanocad.term
{
    private static final double opbunit = 0.02191418;
    private static final double B = .014;
    private final static double C = .00005;
    private final static double D = .0000007;
    private final static double E = .0000000009;          
    private double ks;
    private double angle;

    public opbterm() {}

    public opbterm(atom center, atom bent, atom other1, atom other2)
    {
	myAtoms = new atom[4];
	
	myAtoms[1] = bent;
	myAtoms[0] = center;
	myAtoms[2] = other1;
	myAtoms[3] = other2;
	setCalculationValues();
    }

    public void enumerate(Vector atomList, Vector termList)
    {
	for(int i = 0; i < atomList.size(); i++)
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
	opbterm b = new opbterm((atom)v.elementAt(0), (atom)v.elementAt(1), (atom)v.elementAt(2), (atom)v.elementAt(3));
	if (b.ks != 0.0)
	{
	    termList.addElement(b);
	    //System.out.println("added opb " + ((atom)v.elementAt(0)).getMM3TypeNum() + " " + ((atom)v.elementAt(1)).getMM3TypeNum() + " " + ((atom)v.elementAt(2)).getMM3TypeNum() + ((atom)v.elementAt(3)).getMM3TypeNum());
	}
	b = new opbterm((atom)v.elementAt(0), (atom)v.elementAt(2), (atom)v.elementAt(3), (atom)v.elementAt(1));
	if (b.ks != 0.0)
	{
	    termList.addElement(b);
	    //System.out.println("added opb " + ((atom)v.elementAt(0)).getMM3TypeNum() + " " + ((atom)v.elementAt(2)).getMM3TypeNum() + " " + ((atom)v.elementAt(3)).getMM3TypeNum() + ((atom)v.elementAt(1)).getMM3TypeNum());
	}
	b = new opbterm((atom)v.elementAt(0), (atom)v.elementAt(3), (atom)v.elementAt(1), (atom)v.elementAt(2));
    	if (b.ks != 0.0)
	{
	    termList.addElement(b);
	    //System.out.println("added opb " + ((atom)v.elementAt(0)).getMM3TypeNum() + " " + ((atom)v.elementAt(3)).getMM3TypeNum() + " " + ((atom)v.elementAt(1)).getMM3TypeNum() + ((atom)v.elementAt(2)).getMM3TypeNum());
	}
    }

    public double computeForces()
    {
	//calculate angle
	double pq[], rq[], qs[], normal[], dthdpq[], dthdrq[], dthdqs[];  
	pq = new double[3]; rq = new double[3]; qs = new double[3]; normal = new double[3]; 
	dthdpq = new double[3]; dthdrq = new double[3]; dthdqs = new double[3];
	double qsdotqs, ndotn, ndotqs, denominator, dEdth; 

	for (int i = 0; i < 3; i++)   
	{            
            pq[i] = myAtoms[0].x[i] - myAtoms[3].x[i]; 
            rq[i] = myAtoms[0].x[i] - myAtoms[2].x[i]; 
            qs[i] = myAtoms[1].x[i] - myAtoms[0].x[i];      
	}   
   
	normal[0] = pq[1]*rq[2] - pq[2]*rq[1];    
	normal[1] = pq[2]*rq[0] - pq[0]*rq[2];
	normal[2] = pq[0]*rq[1] - pq[1]*rq[0];      

	ndotn = normal[0]*normal[0] + normal[1]*normal[1] + normal[2]*normal[2];
	qsdotqs = qs[0]*qs[0] + qs[1]*qs[1] + qs[2]*qs[2];
	ndotqs = normal[0]*qs[0] + normal[1]*qs[1] + normal[2]*qs[2];

	if(ndotn <= 0) ndotn = mm3MinimizeAlgorythm.TINY;
	if(qsdotqs <= 0) qsdotqs = mm3MinimizeAlgorythm.TINY;

	double jtemp1 = ndotn * qsdotqs - ndotqs * ndotqs;
	if(jtemp1 <= 0) jtemp1 = mm3MinimizeAlgorythm.TINY;
	denominator = Math.sqrt(jtemp1);

	double jtemp2 = ndotqs / Math.sqrt(ndotn * qsdotqs);
	if(jtemp2 < -1) jtemp2 = -1;
	if(jtemp2 > 1) jtemp2 = 1;
	angle = Math.PI/2 - Math.acos(jtemp2);
    
	potential = computePotential(angle);
	dEdth = computePotentialDerivative(angle); 

	dthdpq[0] = (rq[2]*qs[1] - rq[1]*qs[2] - (rq[2]*normal[1] - rq[1]*normal[2]) * ndotqs / ndotn) / denominator;
	dthdpq[1] = (rq[0]*qs[2] - rq[2]*qs[0] - (rq[0]*normal[2] - rq[2]*normal[0]) * ndotqs / ndotn) / denominator;
	dthdpq[2] = (rq[1]*qs[0] - rq[0]*qs[1] - (rq[1]*normal[0] - rq[0]*normal[1]) * ndotqs / ndotn) / denominator;

	dthdrq[0] = (pq[1]*qs[2] - pq[2]*qs[1] - (pq[1]*normal[2] - pq[2]*normal[1]) * ndotqs / ndotn) / denominator;
	dthdrq[1] = (pq[2]*qs[0] - pq[0]*qs[2] - (pq[2]*normal[0] - pq[0]*normal[2]) * ndotqs / ndotn) / denominator;
	dthdrq[2] = (pq[0]*qs[1] - pq[1]*qs[0] - (pq[0]*normal[1] - pq[1]*normal[0]) * ndotqs / ndotn) / denominator;

	dthdqs[0] = (normal[0] - qs[0] * ndotqs / qsdotqs) / denominator;
	dthdqs[1] = (normal[1] - qs[1] * ndotqs / qsdotqs) / denominator;
	dthdqs[2] = (normal[2] - qs[2] * ndotqs / qsdotqs) / denominator;
	
	for (int i = 0; i < 3; i++)
	{
	    myAtoms[0].f[i] += dEdth * (-dthdqs[i] + dthdrq[i] + dthdpq[i]);
	    myAtoms[1].f[i] += dEdth * dthdqs[i];
	    myAtoms[2].f[i] += dEdth * -dthdrq[i];
	    myAtoms[3].f[i] += dEdth * -dthdpq[i];
	}
    	return myAtoms[0].f[0];
    	//return dTheta;
    }

    public void setCalculationValues()
    {
	int atomBentType = myAtoms[1].getMM3TypeNum();
	int atomCenterType = myAtoms[0].getMM3TypeNum();

	try 
	{
	    AtomDataFile dataFile = new AtomDataFile(newNanocad.txtDir + 
				newNanocad.fileSeparator + "mm3opbdata.txt");
	    //AtomDataFile dataFile = new AtomDataFile("mm3opbdata.txt");
	    if (dataFile.findData(atomCenterType, atomBentType, 0, 1))
		ks = dataFile.parseDouble(2);
	    else
	    {
		    //System.out.println("no data for " + atomBentType + " " + atomCenterType + " bend");
		    ks = 0.0;
	    }
	}
	catch (java.io.IOException e)
	{
	    System.err.println("Bend data lookup error");
	    e.printStackTrace();
	}
    }

    protected double computePotential(double angle){
	return 0.5*ks*angle*angle;
    }

    protected double computePotentialDerivative(double angle)
    {
	double force = ks * angle;
	return force;
    }
    
    public int termLength()
    {
	return 4;
    }
    
    public String name()
    {
	return "Out of Plane Bend";
    }
}
