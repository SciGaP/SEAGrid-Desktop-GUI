package nanocad.minimize.mm3;

/**
 * tterm.java - MM2-style torsion energy term
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

import java.io.FileReader;
import java.lang.Math;
import java.util.Vector;
import nanocad.*;

public class tterm extends nanocad.term
{
  public static final String rcsid =
	"$Id: tterm.java,v 1.2 2005/05/14 23:51:02 xli16 Exp $";
  private double v1, v2, v3;
  private final static double PI = 3.14159265258979;
  private final static double torsionunit = 0.125;
/**
 * Insert the method's description here.
 * Creation date: (6/19/00 4:09:57 PM)
 */
public tterm() {}
public tterm(atom a1, atom a2, atom a3, atom a4) {
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
	
	    if (atom1 < atom4 && atom2 < atom3)
	    {
		tterm t = new tterm ((atom) v.elementAt(0),
			   (atom) v.elementAt(1),
			   (atom) v.elementAt(2),
			   (atom) v.elementAt(3));

		
	  	if (t.v1 != 0.0 || t.v2 != 0.0 || t.v3 != 0.0)
	    	{
		    termList.addElement(t);
		    //System.out.println("added: " + atom1 + " " + atom2 + " " + atom3 + " " + atom4);
		}
	    }
	    else if (atom2 == atom3 && atom1 < atom4)
	    {
		if ( ((atom)v.elementAt(1)).x[0] >= ((atom)v.elementAt(2)).x[0])
	    	{
	   	    tterm t = new tterm ((atom) v.elementAt(0),
			   (atom) v.elementAt(1),
			   (atom) v.elementAt(2),
			   (atom) v.elementAt(3));
	  	    	if (t.v1 != 0.0 || t.v2 != 0.0 || t.v3 != 0.0)
	    	    {
			termList.addElement(t);
		    	//System.out.println("added: " + atom1 + " " + atom2 + " " + atom3 + " " + atom4);
		    }
	        }
	    }
	    else if (atom2 == atom3 && atom1 == atom4)
	    {
		if ( ((atom)v.elementAt(1)).x[0] >= ((atom)v.elementAt(2)).x[0] )
		{
		    tterm t = new tterm ((atom) v.elementAt(0),
			   (atom) v.elementAt(1),
			   (atom) v.elementAt(2),
			   (atom) v.elementAt(3));
	  	    if (t.v1 != 0.0 || t.v2 != 0.0 || t.v3 != 0.0)
	    	    {
		    	termList.addElement(t);
		    	//System.out.println("added: " + atom1 + " " + atom2 + " " + atom3 + " " + atom4);
		    }
		}
	    }
	}

  public double computePotential(double theta){
	return torsionunit*0.5*(v1*(1+Math.cos(theta)) + v2*(1-Math.cos(2*theta)) + v3*(1+Math.cos(3*theta)));
  }

  public double computeForces ()
  {
	double ij[], kj[], kl[], mj[], nk[], dxdi[], dxdj[], dxdk[], dxdl[];
	ij = new double[3]; kj = new double[3]; kl = new double[3];
	mj = new double[3]; nk = new double[3];
	dxdi = new double[3]; dxdj = new double[3]; dxdk = new double[3]; dxdl = new double[3];
	for (int i = 0; i < 3; i++)
	{
		ij[i] = myAtoms[0].x[i] - myAtoms[1].x[i];
		kj[i] = myAtoms[2].x[i] - myAtoms[1].x[i];
		kl[i] = myAtoms[2].x[i] - myAtoms[3].x[i];
        }
	mj = crossProduct(ij,kj);
	nk = crossProduct(kj,kl);

	double theta = dotProduct(mj,nk) / Math.sqrt(dotProduct(mj,mj)*dotProduct(nk,nk));
	if(theta < -1) theta = -1;
	if(theta > 1) theta = 1;
	theta = Math.acos(theta);
	double dedth = torsionunit * (-v1/2 * Math.sin(theta) - v2 * Math.sin(2*theta) - v3*1.5 * Math.sin(3*theta));
	potential = computePotential(theta);
	if (potential < 0.0001) return 0.0;
	for (int i = 0; i < 3; i++)
	{
	    dxdi[i] = Math.sqrt(dotProduct(kj,kj))/dotProduct(mj,mj)*mj[i];
	    dxdk[i] = -Math.sqrt(dotProduct(kj,kj))/dotProduct(nk,nk)*nk[i];
	    dxdj[i] = ((dotProduct(ij,kj) / dotProduct(kj,kj)) - 1.0)*dxdi[i] -
		(dotProduct(kl,kj) / dotProduct(kj,kj))*dxdl[i];
	    dxdk[i] = ((dotProduct(kl,kj) / dotProduct(kj,kj)) - 1.0)*dxdl[i] -
		(dotProduct(ij,kj) / dotProduct(kj,kj))*dxdi[i];
	    myAtoms[0].f[i] -= dedth * dxdi[i];
	    myAtoms[1].f[i] -= dedth * dxdj[i];
	    myAtoms[2].f[i] -= dedth * dxdk[i];
	    myAtoms[3].f[i] -= dedth * dxdl[i];
	}
	return myAtoms[0].f[0];
    } 


  protected String repr2()
	{
	  return " torsion " +
	(new Double(v1)).toString() + " " +
	(new Double(v2)).toString() + " " +
	(new Double(v3)).toString();
	}
/**
 * Insert the method's description here.
 * Creation date: (6/15/00 1:40:30 PM)
 */
protected void setCalculationValues() {
	int atomOneType = myAtoms[0].getMM3TypeNum();
	int atomTwoType = myAtoms[1].getMM3TypeNum();
	int atomThreeType = myAtoms[2].getMM3TypeNum();
	int atomFourType = myAtoms[3].getMM3TypeNum();
	if ((atomOneType < 0) || (atomTwoType < 0) || (atomThreeType < 0) || (atomFourType < 0)) {
		setDefaultCalculationValues();
		return;
	}
	try {
		AtomDataFile dataFile = new AtomDataFile(newNanocad.txtDir + 
    			newNanocad.fileSeparator + "mm3tdata.txt");
		//AtomDataFile dataFile = new AtomDataFile("mm3tdata.txt");
		boolean dataFound = false;

		//it isn't necessary to make sure the atoms are in increasing order, since each bond chain is added both forwards and backwards.

		if (atomOneType == atomFourType && atomTwoType == atomThreeType)
		{
		    if (myAtoms[0].x[0] > myAtoms[3].x[0])
			dataFound = dataFile.findData(atomOneType, atomTwoType, atomThreeType, atomFourType, 0, 1, 2, 3);
		}
		else	
		    dataFound = dataFile.findData(atomOneType, atomTwoType, atomThreeType, atomFourType, 0, 1, 2, 3);
		
		if (dataFound) {
			v1 = dataFile.parseDouble(4);
			v2 = dataFile.parseDouble(7);
			v3 = dataFile.parseDouble(10);
		} else {
			setDefaultCalculationValues();
			return;
		}
	} catch (java.io.IOException e) {
		System.err.println("Torsion data lookup error");
		e.printStackTrace();
	}
}
/**
 * Insert the method's description here.
 * Creation date: (6/19/00 3:02:53 PM)
 */
public void setDefaultCalculationValues() {
	defaultsUsed = true;
	v1 = v2 = v3 = 0.0;
		//System.out.println("No data for " + myAtoms[0].getMM3TypeNum() + " " + myAtoms[1].getMM3TypeNum() + " " + myAtoms[2].getMM3TypeNum() + " " + myAtoms[3].getMM3TypeNum() + " torsion");
}
  public int termLength()
	{
	  return 4;
	}
  public String name()
  {
	return "Torsion";
  }
}
