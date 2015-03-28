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

public class vdwterm extends nanocad.term
{
	private double xij;
	private double Dij;

/**
 * Insert the method's description here.
 * Creation date: (6/19/00 4:09:33 PM)
 */
    public vdwterm() {}
    public vdwterm(atom a1, atom a2) {
	myGroup = a1.getGroup();
	myAtoms = new atom[2];
	myAtoms[0] = a1;
	myAtoms[1] = a2;
	setCalculationValues();
    }

    protected void buildTerm (Vector v, Vector termList)
    { System.out.println("buildTerm for vdw term should never be called."); }

    public double computeForces() {
	int i;
	// compute forces on each atom, add it to the atom's force vector
	double[] diff = new double[3];
	double r, m;
	for (i = 0, r = 0.0; i < 3; i++) {
		diff[i] = myAtoms[0].x[i] - myAtoms[1].x[i];
		r += diff[i] * diff[i];
	}

	if(r <= 0) r = uffMinimizeAlgorythm.TINY;
	r = Math.sqrt(r);

	potential = computePotential(r);
	m = computePotentialDerivative(r)/r; // at this point, m is (1/r)*(du/dr)
	// m > 0 attract, m < 0 repel
	for (i = 0; i < 3; i++) {
		myAtoms[0].f[i] -= m * diff[i];
		myAtoms[1].f[i] += m * diff[i];
	}
	return m * Math.sqrt(dotProduct(diff,diff));
	//return m*r;
    }

    protected double computePotentialDerivative(double x)
    {	//p is the acutal distance between atoms
	return Dij*(12*Math.pow(xij,6)*Math.pow(x,-7) - 12*Math.pow(xij,12)*Math.pow(x,-13)); }

    public double computePotential(double x)
    {	return (Dij*(-2*Math.pow(xij/x, 6) +  Math.pow(xij / x, 12))); }

    public void enumerate(Vector atomList, Vector termList)
    {
	for(int i = 0; i < atomList.size(); i++)
	{
	    atom currentAtom = (atom) atomList.elementAt(i);
	    for(int j = i + 1; j < atomList.size(); j++)
	    {
		atom otherAtom = (atom) atomList.elementAt(j);
		if(currentAtom.bondWith(otherAtom) == null)
		{
		    boolean oneAtomSeperates = false;
		    for(int k = 0; (k < otherAtom.bonds.size()) && !oneAtomSeperates; k++)
			if(((bond) otherAtom.bonds.elementAt(k)).otherAtom(otherAtom).bondWith(currentAtom) != null)
				oneAtomSeperates = true;
		    if(!oneAtomSeperates)
		    {
			vdwterm t = new vdwterm(currentAtom, otherAtom);
			termList.addElement(t);
		    }
		}
	    }	
	}
    }

  protected String repr2() {
	return " VDW " + (new Double(xij)).toString() + " "
		+ (new Double(Dij)).toString(); }

/**
 * Insert the method's description here.
 * Creation date: (6/15/00 1:36:02 PM)
 */
    protected void setCalculationValues() {
	System.out.println("Creating vdwterm");
	double Di = 0, Dj = 0, xi = 0, xj = 0;	// Natural non-bond energies and distances.
	myGroup = myAtoms[0].getGroup();
	if(myGroup.needsToGetTypes()) myGroup.setTypes();
	int atomOneType = myAtoms[0].getUFFTypeNum();
	int atomTwoType = myAtoms[1].getUFFTypeNum();
	if (atomOneType < 0 || atomTwoType < 0) {
		setDefaultCalculationValues();
		return;
	}
	int biggerValue = Math.max(atomOneType, atomTwoType);
	int smallerValue = Math.min(atomOneType, atomTwoType);
	try {
		AtomDataFile dataFile = new AtomDataFile(newNanocad.txtDir + 
				newNanocad.fileSeparator + "uffdata.txt");
		//AtomDataFile dataFile = new AtomDataFile("uffdata.txt");
		if (dataFile.findData(smallerValue, 1)) {
			xi = dataFile.parseDouble(4);
			Di = dataFile.parseDouble(5);
		} else {
			setDefaultCalculationValues();
			return;
		}
		if (dataFile.findData(biggerValue, 1)) {
			xj = dataFile.parseDouble(4);
			Dj = dataFile.parseDouble(5);
		}
		Dij = Math.sqrt(Di*Dj);
		xij = Math.sqrt(xi*xj);
		System.out.println("Xij = "+xij);
		System.out.println("Dij = "+Dij);
	} catch (java.io.IOException e) {
		System.err.println("VDW data lookup error");
		e.printStackTrace();
	}
    }

    protected void setDefaultCalculationValues() {  defaultsUsed = true;  }
    public int termLength() {  return 2;  }
    public String name() {  return "Van der Waals";  }
}
