/******************************************
 *opbterm.java -- nanocad
 *Andrew Knox 06/25/01
 *implements out of plane angle bend term
 ******************************************/

package nanocad.minimize.uff;

import java.lang.Math;
import java.util.Vector;
import nanocad.*;     

public class opbterm extends nanocad.term
{
    private double k0, k1, k2;
    private double angle;

    public opbterm() {}

    public opbterm(atom center, atom bent, atom other1, atom other2)
    {
	myGroup = center.getGroup();
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
	opbterm b;
	for (int i = 0; i<3; i++)
	{
		b = new opbterm((atom)v.elementAt(0), (atom)v.elementAt((i%3)+1), 
			(atom)v.elementAt(((i+1)%3)+1), (atom)v.elementAt(((i+2)%3)+1));
		if (b.k0 != 0.0)
		{
		    termList.addElement(b);
		}
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

	ndotn = dotProduct(normal,normal);
	qsdotqs = dotProduct(qs,qs);
	ndotqs = dotProduct(normal,qs);

	if(ndotn <= 0) ndotn = uffMinimizeAlgorythm.TINY;
	if(qsdotqs <= 0) qsdotqs = uffMinimizeAlgorythm.TINY;

	double jtemp1 = ndotn * qsdotqs - ndotqs * ndotqs;
	if(jtemp1 <= 0) jtemp1 = uffMinimizeAlgorythm.TINY;
	denominator = Math.sqrt(jtemp1);

	double jtemp2 = ndotqs / Math.sqrt(ndotn * qsdotqs);
	if(jtemp2 < -1) jtemp2 = -1;
	if(jtemp2 > 1) jtemp2 = 1;
	angle = Math.PI/2 - Math.acos(jtemp2);
    
	// These are divided by 3 because we consider each
	// of the 3 bent atoms separately.
	potential = computePotential(angle) / 3;
	dEdth = computePotentialDerivative(angle) / 3; 

	double RQcrossQS[] = crossProduct(rq,qs);
	double RQcrossNorm[] = crossProduct(rq,normal);
	dthdpq[0] = (-RQcrossQS[0] + RQcrossNorm[0] * ndotqs / ndotn) / denominator;
	dthdpq[1] = (-RQcrossQS[1] + RQcrossNorm[1] * ndotqs / ndotn) / denominator;
	dthdpq[2] = (-RQcrossQS[2] + RQcrossNorm[2] * ndotqs / ndotn) / denominator;

	double PQcrossQS[] = crossProduct(pq,qs);
	double PQcrossNorm[] = crossProduct(pq,normal);
	dthdrq[0] = (PQcrossQS[0] + PQcrossNorm[0] * ndotqs / ndotn) / denominator;
	dthdrq[1] = (PQcrossQS[1] + PQcrossNorm[1] * ndotqs / ndotn) / denominator;
	dthdrq[2] = (PQcrossQS[2] + PQcrossNorm[2] * ndotqs / ndotn) / denominator;

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
	myGroup = myAtoms[0].getGroup();
	if(myGroup.needsToGetTypes()) myGroup.setTypes();
	int atomBentType = myAtoms[1].getUFFTypeNum();
	int atomCenterType = myAtoms[0].getUFFTypeNum();

	// Force constants for this guy came from solving 12
	// linear equations in 12 unknowns for the Group V hydrides.
	// A three term cosine fourier expansion was used, with constants
	// fit to have a minimum (derivative is 0) at the natural angle,
	// and that minimum is set to 0.  Also, at 0 degrees, the function
	// has a maximum of Ebarrier.
	// Atom	Angle	Ebarrier
	// N	107.5	5.8
	// P	93.5	37
	// As	91.8	40
	// Sb	91.3	46
	// Bi	?	?
	// Energies are in kilocalories per mole.  Note that the angle given is
	// a published value for the H-X-H angle, while UFF instead uses the angle
	// between the IL axis and the IJK plane, for a opbterm consisting of atoms
	// IJKL.  Values for Bismuth were unavailable, so Bi and Po are assumed
	// to have the same values as Sb and Te.
	// 
	// Terms for carbon come directly from the literature.

	switch(myAtoms[0].atomicNumber()){
	case 7: // Nitrogen and Oxygen
	case 8: k0 = 18.2169897839;
		k1 = -24.4237325078;
		k2 = 12.0067427239;
		break;
	case 15: // Phosphorus and Sulfur
	case 16: k0 = 22.6500766206;
		 k1 = -7.94615918416;
		 k2 = 22.2960825616;
		 break;
	case 33: // Arsenic and Selenium
	case 34: k0 = 22.0248498374;
		 k1 = -3.96032299431;
		 k2 = 21.9354731569;
		 break;
	case 51: // Antimony, Tellurium, Bismuth, and Polonium
	case 52:
	case 83:
	case 84: k0 = 25.1558772986;
		 k1 = -3.25887125444;
		 k2 = 25.1089939559;
		 break;
	case 6: // Carbon, the most complicated one.
		if(atomBentType == 19){
			// if it's bonded to O_2...
			k0 = 50;
			k1 = -50;
			k2 = 0;
		}
		else{
			k0 = 6;
			k1 = -6;
			k2 = 0;
		}
		break;
	default: k0 = 0;
		 k1 = 0;
		 k2 = 0;
	}

    }

    protected double computePotential(double angle)
    {	return k0 + k1*Math.cos(angle) + k2*Math.cos(2*angle);	}

    protected double computePotentialDerivative(double angle)
    {	return -k1*Math.sin(angle) - 2*k2*Math.sin(2*angle);	}

    public String repr2() { return " OPB"; }
    
    public int termLength()
    {	return 4;	}
    
    public String name()
    {	return "Out of Plane Bend";	}
}
