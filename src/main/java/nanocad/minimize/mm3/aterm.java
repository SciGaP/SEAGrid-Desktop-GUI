package nanocad.minimize.mm3;

/**
 * aterm.java - MM2-style angle energy term
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

 
import java.lang.Math;
import java.util.Vector;
import nanocad.*;

public class aterm extends nanocad.term
{
  public static final String rcsid =
	"$Id: aterm.java,v 1.2 2005/05/14 23:51:02 xli16 Exp $";
  private static final double convert = 3.1415926 / 180; // degrees to radians
  private double kth, th0;
	public static final double B = .014;
	public final static double C = .00005;
	public final static double D = .0000007;
	public final static double E = .0000000009;
/**
 * Insert the method's description here.
 * Creation date: (6/19/00 4:10:14 PM)
 */
public aterm() {}
public aterm(atom a1, atom a2, atom a3) {
	myAtoms = new atom[3];
	myAtoms[0] = a1;
	myAtoms[1] = a2;
	myAtoms[2] = a3;
	setCalculationValues();
}
  protected void buildTerm (Vector v, Vector termList)
    {
	int atom1 = ((atom)v.elementAt(0)).getMM3TypeNum();
	int atom2 = ((atom)v.elementAt(1)).getMM3TypeNum();
	int atom3 = ((atom)v.elementAt(2)).getMM3TypeNum();

	if (atom1 < atom3)
	{
	    
	    aterm t = new aterm ((atom) v.elementAt(0),
			   (atom) v.elementAt(1),
			   (atom) v.elementAt(2));
	    termList.addElement(t);
	}
	else if (atom1 == atom3)
	{
	    if ( ((atom)v.elementAt(0)).x[0] < ((atom)v.elementAt(2)).x[0])
	    {	
		aterm t = new aterm ((atom) v.elementAt(0),
			   (atom) v.elementAt(1),
			   (atom) v.elementAt(2));
		termList.addElement (t);
	    }
	}
    }
public double computeForces() {
	if (kth == 0.0)
		return 0.0;
	int i;
	// compute forces on each atom, add it to the atom's force vector
	double[] ab = new double[3];
	double[] bc = new double[3];
	double abdotab = 0.0, abdotbc = 0.0, bcdotbc = 0.0, th, tdif, duDth;
	for (i = 0; i < 3; i++) {
		ab[i] = myAtoms[0].x[i] - myAtoms[1].x[i];
		bc[i] = myAtoms[2].x[i] - myAtoms[1].x[i];
		abdotab += ab[i] * ab[i];
		abdotbc += ab[i] * bc[i];
		bcdotbc += bc[i] * bc[i];
	}
	if(abdotab <= 0) abdotab = mm3MinimizeAlgorythm.TINY;
	if(bcdotbc <= 0) bcdotbc = mm3MinimizeAlgorythm.TINY;

	if (abdotab > 3.0 || bcdotbc > 3.0)
		return 0.0;

	double jtemp1 = abdotbc/Math.sqrt(abdotab * bcdotbc);
	if(jtemp1 < -1) jtemp1 = -1;
	if(jtemp1 > 1) jtemp1 = 1;
	th = Math.acos(jtemp1);
	tdif = th - th0;
	if(tdif == 0.0){
		potential = 0.0;
		return 0.0;
	}
	potential = computePotential(tdif);
	duDth = computePotentialDerivative(tdif);
	
	double[] dthda = new double[3];
	double[] dthdc = new double[3];
	
	double jtemp2 = abdotab * bcdotbc - abdotbc*abdotbc;
	if(jtemp2 <= 0) jtemp2 = mm3MinimizeAlgorythm.TINY;
	double denominator = Math.sqrt(jtemp2);
	for (i = 0; i < 3; i++)
	{
	    dthda[i] = -(ab[i] * abdotbc / abdotab - bc[i]) / denominator;
	    dthdc[i] = -(bc[i] * abdotbc / bcdotbc - ab[i]) / denominator;
	    myAtoms[0].f[i] += duDth * dthda[i];
	    myAtoms[1].f[i] += duDth * (-dthda[i] - dthdc[i]);
	    myAtoms[2].f[i] += duDth * dthdc[i];
	}
	return duDth * dthda[0];
}
/**
 * Insert the method's description here.
 * Creation date: (6/15/00 12:53:22 PM)
 * @return double
 * @param deltaTheta double
 */
protected double computePotentialDerivative(double deltaTheta) {		
	double deltaThetaSquared = deltaTheta*deltaTheta;
	double bTerm = 3*B*deltaTheta;
	double cTerm = 4*C*deltaThetaSquared;
	double dTerm = 5*D*deltaThetaSquared*deltaTheta;
	double eTerm = 6*E*deltaThetaSquared*deltaThetaSquared;
	double force = kth*deltaTheta*(2 - bTerm + cTerm - dTerm + eTerm);
	return force;
}

  protected double computePotential(double dT){
	return 0.5*kth*dT*dT*(1 - B*dT + C*dT*dT - D*dT*dT*dT + E*dT*dT*dT*dT);
  }

  protected String repr2()
	{
	  return " angle " +
	(new Double(kth)).toString() + " " +
	(new Double(th0)).toString();
	}
/**
 * Insert the method's description here.
 * Creation date: (6/15/00 12:41:20 PM)
 */
protected void setCalculationValues() {
	int atomOneType = myAtoms[0].getMM3TypeNum();
	int atomTwoType = myAtoms[1].getMM3TypeNum();
	int atomThreeType = myAtoms[2].getMM3TypeNum();
	if ((atomOneType < 0) || (atomTwoType < 0) || (atomThreeType < 0)) {
		setDefaultCalculationValues();
		return;
	}
	
	try {
		AtomDataFile dataFile = new AtomDataFile(newNanocad.txtDir + 
				newNanocad.fileSeparator + "mm3adata.txt");
		//AtomDataFile dataFile = new AtomDataFile("mm3adata.txt");
		if (dataFile.findData(atomTwoType, atomOneType, atomThreeType, 1, 0, 2)) {
			kth = dataFile.parseDouble(3);
			th0 = dataFile.parseDouble(4) * convert;
		} else {
			setDefaultCalculationValues();
			return;
		}
	} catch (java.io.IOException e) {
		System.err.println("Angle data lookup error");
		e.printStackTrace();
	}
}
/**
 * Insert the method's description here.
 * Creation date: (6/19/00 2:10:38 PM)
 */
protected void setDefaultCalculationValues() {
	defaultsUsed = true;
	kth = 0.3;
	th0 = 120.0 * convert;
	//System.out.println("No data for " + myAtoms[0].getMM3TypeNum() + " " + myAtoms[1].getMM3TypeNum() + " " + myAtoms[2].getMM3TypeNum() + " angle");
	}
  public int termLength()
	{
	  return 3;
	}
  public String name()
  {
	return "Angle";
  }
}
