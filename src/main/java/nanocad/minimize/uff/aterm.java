package nanocad.minimize.uff;

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

public class aterm extends term
{
	private static final double convert = pi / 180; // degrees to radians
	double k,k0,k1,k2, th0;	// force constants, and standard bond angle.  It would be preferable
				// to use just one force constant, and have it calculated and assigned
				// during setCalculationValues, but k depends on theta, which is not
				// available until the calculation begins.  Therefore, k is broken
				// into 3 separate constants.
	int n;			// constant multiplier; 1,3,4,or 4 for linear, trigonal-planar,
				// square-planar, or octahedral respectively.  Not used for
				// general non-linear cases (where geometry is with respect to 
				// the central atom in the term).
	double rij, rjk;	// natural bond lengths, as explained in lterm.java.  These should
				// probably be moved to be member variables of bond.java, if possible.

	/**
	 * Calculates forces and potential for the angle term.
	 * For linear or planar geometries (wrt the central atom),
	 *  E = k/n^2*(1-cos(n*theta);
	 *  n depends on the orientation, see below where n is assigned
	 * For general non-linear,
	 *  E = k*(c0 + c1*cos(theta) + c2*cos(2*theta))
	 *  c2 = 1/(4*sin^2(th0));
	 *  c1 = -4*c2*cos(th0);
	 *  c0 = c2*(2*cos^2(2*th0) + 1);
	 * k = 664.12/(rij*rjk)*zi*zk/(rik^5)*rij*rjk*(3*rij*rjk*(1-cos^2(th0)) - rik^2*cos(th0));
	 *
	 * Creation date: (6/08/02 jstrutz)
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
		myGroup = ((atom)v.elementAt(0)).getGroup();
		if(myGroup.needsToGetTypes()) myGroup.setTypes();
		int atom1 = ((atom)v.elementAt(0)).getUFFTypeNum();
		int atom2 = ((atom)v.elementAt(1)).getUFFTypeNum();
		int atom3 = ((atom)v.elementAt(2)).getUFFTypeNum();

		if ((atom1 < atom3) || ((atom1 == atom3)&&(((atom)v.elementAt(0)).x[0] < ((atom)v.elementAt(2)).x[0])))
		{
		    aterm t = new aterm ((atom) v.elementAt(0),
			   (atom) v.elementAt(1),
			   (atom) v.elementAt(2));
			termList.addElement(t);
		}
    }

	public double computeForces() {
		int i;
		// compute forces on each atom, add it to the atom's force vector
		double[] ab = new double[3];
		double[] bc = new double[3];
		double abdotab = 0.0, abdotbc = 0.0, bcdotbc = 0.0, th, duDth = 0.0;
		for (i = 0; i < 3; i++) {
			ab[i] = myAtoms[0].x[i] - myAtoms[1].x[i];
			bc[i] = myAtoms[2].x[i] - myAtoms[1].x[i]; // isn't this cb?
		}
		abdotab = dotProduct(ab,ab);
		abdotbc = dotProduct(ab,bc);
		bcdotbc = dotProduct(bc,bc);
		if(abdotab <= 0) abdotab = uffMinimizeAlgorythm.TINY;
		if(bcdotbc <= 0) bcdotbc = uffMinimizeAlgorythm.TINY;

//		if (abdotab > 3.0 || bcdotbc > 3.0) return 0.0; // Why?

		double jtemp1 = abdotbc/Math.sqrt(abdotab * bcdotbc);
		if(jtemp1 < -1) jtemp1 = -1;
		if(jtemp1 > 1) jtemp1 = 1;
		th = Math.acos(jtemp1);

		// now we can finish computing k
		double jtemp3 = rij*rij + rjk*rjk - 2*rij*rjk*Math.cos(th);
		if(jtemp3 <= 0) jtemp3 = uffMinimizeAlgorythm.TINY;
		double rik = Math.sqrt(jtemp3);
		k = k0/(jtemp3*jtemp3*rik)*(k1 - k2*jtemp3); // Keep in mind jtemp3 = rik^2.

		// potential and force depend on the angle, different
		// expressions are used depending on the geometry.
		potential = computePotential(th);
		duDth = computePotentialDerivative(th);
	
		double[] dthda = new double[3];
		double[] dthdc = new double[3];
	
		double jtemp2 = abdotab * bcdotbc - abdotbc*abdotbc;
		if(jtemp2 <= 0) jtemp2 = uffMinimizeAlgorythm.TINY;
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
	protected double computePotentialDerivative(double theta) {	
		if(computePotential(theta) < 0.00001) return 0;
		if(n == 0){ // general non-linear case
			double c2 = 0.25/(Math.sin(th0)*Math.sin(th0));
			double c1 = -4*c2*Math.cos(th0);
			return (-k*(c1*Math.sin(theta) + 2*c2*Math.sin(2*theta)));
		}
		// specific linear case, simplified 2 term fourier expansion
		return (k/n*(Math.sin(n*theta) - 2/n*(1 - Math.cos(n*theta))));
	}

	protected double computePotential(double theta){
	        if(n == 0){ // general non-linear case
			double c2 = 0.25/(Math.sin(th0)*Math.sin(th0));
			double c1 = -4*c2*Math.cos(th0);
			double c0 = c2*(2*Math.cos(th0)*Math.cos(th0) + 1);
			return (k*(c0 + c1*Math.cos(theta) + c2*Math.cos(2*theta)));
		}
		// specific linear case, simplified 2 term fourier expansion
		return (k/n/n*(1 - Math.cos(n*theta)));
	}

    protected String repr2()
	{
		return " angle " +
		(new Double(k)).toString() + " " +
		(new Double(th0)).toString()+ " " +
		(new Double(n)).toString();
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (6/15/00 12:41:20 PM)
	 */
	protected void setCalculationValues() {
		System.out.println("Creating aterm");
		double ri=0, rj=0, rk=0;	// natural bond distance
		double xi=0, xj=0, xk=0;	// natural non-bond distance
		double zi=0, zj=0, zk=0;	// effective charge
		myGroup = myAtoms[0].getGroup();
		if(myGroup.needsToGetTypes()) myGroup.setTypes();
		int atomOneType = myAtoms[0].getUFFTypeNum();
		int atomTwoType = myAtoms[1].getUFFTypeNum();
		int atomThreeType = myAtoms[2].getUFFTypeNum();
		if ((atomOneType < 0) || (atomTwoType < 0) || (atomThreeType < 0)) {
			// Should never happen; uff has types for all atoms.
			System.out.println("ERROR: Atom types not found "+myAtoms[0].repr()+", "+myAtoms[1].repr()+", "+myAtoms[2].repr());
			return;
		}
	
		try {
			AtomDataFile dataFile = new AtomDataFile(newNanocad.txtDir + 
					newNanocad.fileSeparator + "uffdata.txt");
			//AtomDataFile dataFile = new AtomDataFile("uffdata.txt");
			if (dataFile.findData(atomTwoType, 1)) {
				th0 = dataFile.parseDouble(3)*convert;
				rj = dataFile.parseDouble(2);
				xj = dataFile.parseDouble(4);
				zj = dataFile.parseDouble(7);
				n = dataFile.parseInt(8);
				System.out.println("th0:rj:xj:zj");
				System.out.println(th0/convert+" : "+rj+" : "+xj+" : "+zj);
			}
			else {
				// Should never happen; uff has parameters for all atoms.
				System.out.println("ERROR: Angle parameters not found for "+myAtoms[1].repr());
				return;
			}

			dataFile = new AtomDataFile(newNanocad.txtDir + 
					newNanocad.fileSeparator + "uffdata.txt");
			//dataFile = new AtomDataFile("uffdata.txt");
			if (dataFile.findData(atomOneType, 1)) {
				ri = dataFile.parseDouble(2);
				xi = dataFile.parseDouble(4);
				zi = dataFile.parseDouble(7);
				System.out.println("ri:xi:zi");
				System.out.println(ri+" : "+xi+" : "+zi);
			}
			else{
				// should never happen; uff has parameters for all atoms.
				System.out.println("ERROR: Angle parameters not found for "+myAtoms[0].repr());
				return;
			}

			dataFile = new AtomDataFile(newNanocad.txtDir + 
					newNanocad.fileSeparator + "uffdata.txt");
			//dataFile = new AtomDataFile("uffdata.txt");
			if (dataFile.findData(atomThreeType, 1)) {
				rk = dataFile.parseDouble(2);
				xk = dataFile.parseDouble(4);
				zk = dataFile.parseDouble(7);
				System.out.println("rk:xk:zk");
				System.out.println(rk+" : "+xk+" : "+zk);
			}
			else{
				// should never happen; uff has parameters for all atoms.
				System.out.println("ERROR: Angle parameters not found for "+myAtoms[2].repr());
				return;
			}
		}
		catch (java.io.IOException e) {
			System.err.println("Angle data lookup error");
			e.printStackTrace();
		}

		// Change the value of n so it matches the required term.
		// Linear = 1
		// Trigonal = 3
		// Square-planar = 4
		// Octahedral = 4
		// General non-linear = 0
		// Basically, these terms ensure that the energy falls to 0 when
		// theta approaches theta0 (th0 = 180, 120, 90, 90/180, respectively).
		// The general non-linear is 0 because a more descriptive, 3 term cosine
		// fourier expansion is used.
		switch(n){
			// NOTE!! This has been changed to 2, because the expression is 
			// E = k/n^2 * (1 - cos(n*th)), which leads to a maximum at th = 180.
			case 1: n = 2;
				break;
			case 2: n = 3;
				break;
			case 3: n = 0;
				break;
		//	case 4: n = 4;
		//		break;
			case 5: n = 0;
				break;
			case 6: n = 4;
				break;
			default: n = 0;
				break;
		}

		// Find the ij bond order
		bond myBond1 = myAtoms[0].getBond(myAtoms[1]);
		double rbo1 = -0.1332*(ri + rj)*Math.log(myBond1.apporder());

		// Note here that x,r are > 0 for all atoms
		double temp1 = Math.sqrt(xi) - Math.sqrt(xj);
		double ren1 = ri*rj*temp1*temp1 / (xi*ri + xj*rj);
		rij = ri + rj + rbo1 + ren1;

		// Find the jk bond order
		bond myBond2 = myAtoms[2].getBond(myAtoms[1]);
		double rbo2 = -0.1332*(rk + rj)*Math.log(myBond2.apporder());

		// Note here that x,r are > 0 for all atoms
		double temp2 = Math.sqrt(xk) - Math.sqrt(xj);
		double ren2 = rk*rj*temp2*temp2 / (xk*rk + xj*rj);
		rjk = rk + rj + rbo2 + ren2;

		k0 = 664.12*zi*zk/rij/rjk;
		k1 = 3*rij*rjk*(1-Math.cos(th0)*Math.cos(th0));
		k2 = Math.cos(th0);
		//System.out.println("*************************");
		//System.out.println("Angle rij = "+rij);
		//System.out.println("Angle rjk = "+rjk);
		//System.out.println("Angle k0 = "+k0);
		//System.out.println("Angle k1 = "+k1);
		//System.out.println("Angle k2 = "+k2);
		//System.out.println("*************************");
	}

	public int termLength() { return 3; }

	public String name()
	{
		return "Angle";
	}
}
