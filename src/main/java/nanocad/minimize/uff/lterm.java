package nanocad.minimize.uff;

/**
 * lterm.java - MM2-style length energy term
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
 * 3. All advertising materials mentioning features or use of this software
 *    or its derived works must display the following acknowledgement:
 * 	This product includes software developed by Will Ware.
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

public class lterm extends term
{
	/**
	 * Represents the bond stretch term of UFF.  
	 * E = 0.5*k(r-rij)^2
	 * rij = ri + rj + rbo + ren
	 * rbo = -0.1332*(ri + rj)*ln(n)
	 * n = bond order
	 * ren = ri*rj*(sqrt(xi) - sqrt(xj))^2 / (xi*ri + xj*rj)
	 * k = 664.12*zi*zj / rij^3
	 * ri, rj, xi, xj, zi, zj are parameters found in uffdata.txt
	 * k and rij are computed when terms are read from uffdata.txt
	 *
	 * Creation date: (6/08/02)
	 */

	double k;	// force constant
	double rij;	// natural stretch distance

	public lterm() {}
	public lterm(atom a1, atom a2) {
		myAtoms = new atom[2];
		myAtoms[0] = a1;
		myAtoms[1] = a2;
		setCalculationValues();
	}

	protected void buildTerm (Vector v, Vector termList)
    {
		myGroup = ((atom)v.elementAt(0)).getGroup();
		if(myGroup.needsToGetTypes()) myGroup.setTypes();
		int atom1 = ((atom)v.elementAt(0)).getUFFTypeNum();
		int atom2 = ((atom)v.elementAt(1)).getUFFTypeNum();

		if (atom1 < atom2)
		{
		    lterm t = new lterm ((atom) v.elementAt(0), (atom) v.elementAt(1));
		    termList.addElement(t);
		}
		else if(atom1 == atom2)
		{
		    if ( ((atom)v.elementAt(0)).x[0] < ((atom)v.elementAt(1)).x[0] )
		    {
				lterm t = new lterm ((atom) v.elementAt(0), (atom) v.elementAt(1));
				termList.addElement (t);
		    }
		}
    }

	public double computeForces()
	{	int i;

		// compute forces on each atom, add it to the atom's force vector
		double[] diff = new double[3];
		double r = 0.0, m = 0.0;
		for (i = 0; i < 3; i++)
		{	diff[i] = myAtoms[0].x[i] - myAtoms[1].x[i];
			r += diff[i] * diff[i];
		}

		if(r <= 0) r = uffMinimizeAlgorythm.TINY;
		r = Math.sqrt(r);
		potential = computePotential(r);
		
		m = computePotentialDerivative(r);

		// at this point, m is du/dr
		m /= r;
		// m > 0 attract, m < 0 repel
		for (i = 0; i < 3; i++)
		{	myAtoms[0].f[i] -= m * diff[i];
			myAtoms[1].f[i] += m * diff[i];
		}
		double jtemp1 = dotProduct(diff,diff);
		if(jtemp1 <= 0) jtemp1 = uffMinimizeAlgorythm.TINY;
		return m * Math.sqrt(jtemp1);
		//return m*r;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (6/15/00 1:22:01 PM)
	 * @return double
	 * @param lengthDifference double
	 */
	protected double computePotentialDerivative(double stretch)
	{ //	if (computePotential(stretch) < 0.00001) return 0;
		return (k*(stretch - rij)); }

	protected double computePotential(double stretch)
	{	return (0.5*k*(stretch - rij)*(stretch - rij)); }

	protected String repr2()
	{	return " length " + (new Double(k)).toString() + " " + (new Double(rij)).toString(); }

	/**
	 * Sets the force constant and the natural radius.
	 *
	 * Creation date: (6/08/02)
	 */
	protected void setCalculationValues()
	{
		System.out.println("Creating lterm");
		double ri = 0, rj = 0;	// natural bond distance, for regular stretch
		double xi = 0, xj = 0;	// natural non-bond distance, for electronegativity
		double zi = 0, zj = 0;	// effective charge, for electronegativity
		myGroup = myAtoms[0].getGroup();
		if(myGroup.needsToGetTypes()) myGroup.setTypes();
		int atomOneType = myAtoms[0].getUFFTypeNum();
		int atomTwoType = myAtoms[1].getUFFTypeNum();
		if (atomOneType < 0 || atomTwoType < 0)
		{	// This should never happen, all atom types are > 0 in uff
			System.out.println("ERROR: Atom types not found "+myAtoms[0].repr()+", "+myAtoms[1].repr());
			return;
		}
	
		try {
			AtomDataFile dataFile = new AtomDataFile(newNanocad.txtDir + 
					newNanocad.fileSeparator + "uffdata.txt");
			//AtomDataFile dataFile = new AtomDataFile("uffdata.txt");
			if (dataFile.findData(atomOneType, 1)) {
				ri = dataFile.parseDouble(2);
				xi = dataFile.parseDouble(4);
				zi = dataFile.parseDouble(7);
				//System.out.println("ri:xi:zi");
				//System.out.println(ri+" : "+xi+" : "+zi);
			}
			else System.out.println("ERROR: Data not found for "+atomOneType);

			dataFile = new AtomDataFile(newNanocad.txtDir + 
					newNanocad.fileSeparator + "uffdata.txt");
			//dataFile = new AtomDataFile("uffdata.txt");
			if(dataFile.findData(atomTwoType, 1))
			{	rj = dataFile.parseDouble(2);
				xj = dataFile.parseDouble(4);
				zj = dataFile.parseDouble(7);
				//System.out.println("rj:xj:zj");
				//System.out.println(rj+" : "+xj+" : "+zj);
			}
			else // This should never happen; uff has terms for every atom.
			{ System.out.println("ERROR: Data not found for "+atomTwoType); }
		}
		catch (java.io.IOException e)
		{	System.err.println("Length data lookup error");
			e.printStackTrace();
		}

		// Get the bond order by finding the bond between i and j.
		bond myBond = myAtoms[0].getBond(myAtoms[1]);
		System.out.println("BO = "+myBond.apporder());
		double rbo = -0.1332*(ri + rj)*Math.log(myBond.apporder());

		// Note here that x,r are > 0 for all atoms
		double temp = Math.sqrt(xi) - Math.sqrt(xj);
		double ren = ri*rj*temp*temp / (xi*ri + xj*rj);

		rij = ri + rj + rbo + ren;
		k = 664.12*zi*zj / (rij*rij*rij);
		System.out.println("length K = "+k);
		System.out.println("length rij = "+rij);
	  }

	public int termLength() { return 2; }
	public String name() { return "Length"; }
}
