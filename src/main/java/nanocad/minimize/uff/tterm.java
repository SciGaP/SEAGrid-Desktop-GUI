package nanocad.minimize.uff;

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

import java.lang.Math;
import java.util.Vector;
import nanocad.*;

public class tterm extends nanocad.term
{
  private double vphi;
  private int n;
  private double phi0;
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
	    myGroup = ((atom)v.elementAt(0)).getGroup();
	    if(myGroup.needsToGetTypes()) myGroup.setTypes();
	    int atom1 = ((atom)v.elementAt(0)).getUFFTypeNum();
	    int atom2 = ((atom)v.elementAt(1)).getUFFTypeNum();
	    int atom3 = ((atom)v.elementAt(2)).getUFFTypeNum();
	    int atom4 = ((atom)v.elementAt(3)).getUFFTypeNum();  
	
	    if (atom1 < atom4 && atom2 < atom3)
	    {
		tterm t = new tterm ((atom) v.elementAt(0),
			   (atom) v.elementAt(1),
			   (atom) v.elementAt(2),
			   (atom) v.elementAt(3));
	  	if (t.vphi != 0.0)
	    	{
		    termList.addElement(t);
		    //System.out.println("added: " + atom1 + " " + atom2 + " " + atom3 + " " + atom4);
		}
	    }
	    else if (atom2 == atom3 && atom1 <= atom4)
	    {
		if ( ((atom)v.elementAt(1)).x[0] > ((atom)v.elementAt(2)).x[0])
	    	{
	   	    tterm t = new tterm ((atom) v.elementAt(0),
			   (atom) v.elementAt(1),
			   (atom) v.elementAt(2),
			   (atom) v.elementAt(3));
	  	    if (t.vphi != 0.0)
	    	    {
			termList.addElement(t);
		    	//System.out.println("added: " + atom1 + " " + atom2 + " " + atom3 + " " + atom4);
		    }
	        }
	    }
	}

  public double computePotential(double phi){
	double temp1 = (n*phi0)%(2*Math.PI);
	double temp2 = (n*phi)%(2*Math.PI);
/*
	if(temp1 > 1) temp1 = 1;
	if(temp1 < -1) temp1 = -1;
	if(temp2 > 1) temp2 = 1;
	if(temp2 < -1) temp2 = -1;
*/
	return 0.5*vphi*(1-Math.cos(temp1)*Math.cos(temp2));
  }

  public double computePotentialDerivative(double phi){
	double temp1 = (n*phi0)%(2*Math.PI);
	double temp2 = (n*phi)%(2*Math.PI);
/*
	if(temp1 > 1) temp1 = 1;
	if(temp1 < -1) temp1 = -1;
	if(temp2 > 1) temp2 = 1;
	if(temp2 < -1) temp2 = -1;
*/

	// The following line gives the force a stopping condition.
	// If things didn't force a movement of a certain size, this
	//  wouldn't have been necessary.
	if (computePotential(phi) < 0.001) return 0;
	return 0.5*n*vphi*Math.cos(temp1)*Math.sin(temp2);
  }

    public double computeForces()
    {
	double ij[], kj[], kl[], mj[], nk[], dxdi[], dxdj[], dxdk[], dxdl[];
	ij = new double[3]; kj = new double[3];
	kl = new double[3]; mj = new double[3];
	nk = new double[3]; dxdi = new double[3];
	dxdj = new double[3]; dxdk = new double[3]; dxdl = new double[3];

	double phi = CalculateDihedral();
	potential = computePotential(phi);
	double dedth = computePotentialDerivative(phi);

	for (int i = 0; i < 3; i++)
	{
		ij[i] = myAtoms[0].x[i] - myAtoms[1].x[i];
		kj[i] = myAtoms[2].x[i] - myAtoms[1].x[i];
		kl[i] = myAtoms[2].x[i] - myAtoms[3].x[i];
        }

	mj = crossProduct(ij,kj);
	nk = crossProduct(kj,kl);

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
	  return new String(" torsion "+vphi+" "+n);
	}
/**
 * Insert the method's description here.
 * Creation date: (6/15/00 1:40:30 PM)
 */
protected void setCalculationValues() {
	//System.out.println("Creating tterm");
	myGroup = myAtoms[0].getGroup();
	if(myGroup.needsToGetTypes()) myGroup.setTypes();
	int atomOneType = myAtoms[0].getUFFTypeNum();
	int atomTwoType = myAtoms[1].getUFFTypeNum();
	int atomThreeType = myAtoms[2].getUFFTypeNum();
	int atomFourType = myAtoms[3].getUFFTypeNum();
/*
	System.out.print("Torsion term");
	for (int i = 0; i < 4; i++)
		System.out.print(" | "+myAtoms[i].getUFFTypeString()+" "+myAtoms[i].myGeom);
	System.out.print("\n");
*/
	if ((atomOneType < 0) || (atomTwoType < 0) || (atomThreeType < 0) || (atomFourType < 0)) {
		System.out.println("ERROR: No UFF Type for "+myAtoms[0].toString()+", "+myAtoms[1].toString()+", "+myAtoms[2].toString()+", "+myAtoms[3].toString());
		return;
	}

	// Need to determine the group and hybridization of the atoms, so we
	// can determine the form vphi should take.  The following order is important;
	// the first if stmts are exceptions to the more general rules that follow.
	// Order of atoms is 0,3:edge, 1,2: central

	// to determine if either central atom is from group 6
	boolean bfromG6 = false;
	boolean cfromG6 = false;

	// to determine the sp3 params for atoms b,c; easier to do it here than
	// to reproduce it in each sp3 case.
	double Uj = 0.0;
	double Uk = 0.0;
	double Vj = 0.0;
	double Vk = 0.0;
	switch (myAtoms[1].atomicNumber()){
	case 6: Vj = 2.119;
		break;
	case 7: Vj = 0.450;
		break;
	case 8: Vj = 0.018;
		break;
	case 14: Vj = 1.225;
		break;
	case 15: Vj = 2.400;
		break;
	case 16: Vj = 0.484;
		break;
	case 32: Vj = 0.701;
		break;
	case 33: Vj = 1.5;
		break;
	case 34: Vj = 0.335;
		break;
	case 50: Vj = 0.199;
		break;
	case 51: Vj = 1.1;
		break;
	case 52: Vj =0.3 ;
		break;
	case 82: Vj = 0.1;
		break;
	case 83: Vj = 1.0;
		break;
	case 84: Vj = 0.3;
		break;
	default: Vj = 0.0;
	}

	switch (myAtoms[2].atomicNumber()){
	case 6: Vk = 2.119;
		break;
	case 7: Vk = 0.450;
		break;
	case 8: Vk = 0.018;
		break;
	case 14: Vk = 1.225;
		break;
	case 15: Vk = 2.400;
		break;
	case 16: Vk = 0.484;
		break;
	case 32: Vk = 0.701;
		break;
	case 33: Vk = 1.5;
		break;
	case 34: Vk = 0.335;
		break;
	case 50: Vk = 0.199;
		break;
	case 51: Vk = 1.1;
		break;
	case 52: Vk =0.3;
		break;
	case 82: Vk = 0.1;
		break;
	case 83: Vk = 1.0;
		break;
	case 84: Vk = 0.3;
		break;
	default: Vk = 0.0;
	}

	// to determine bond order between the central atoms
	double order = (myAtoms[1].getBond(myAtoms[2])).apporder();
	int an1 = myAtoms[1].atomicNumber();
	int an2 = myAtoms[2].atomicNumber();

	// if either central atom (ca) is from a B-group, or either is linear, vphi = 0
	if((an1 == 8) || (an1 == 16) || (an1 == 34) || (an1 == 52) || (an1 == 84)){
		bfromG6 = true;
	}
	if((an2 == 8) || (an2 == 16) || (an2 == 34) || (an2 == 52) || (an2 == 84)){
		cfromG6 = true;
	}

	// if non main group, vphi = 0
	if(( ((an1>20) && (an1<30)) || ((an1>38) && (an1<49)) || ((an1>57) && (an1<80)) || ((an1>88) && (an1<113)) ) || ( ((an2>20) && (an2<30)) || ((an2>38) && (an2<49)) || ((an2>57) && (an2<80)) || ((an2>88) && (an2<113)) )){
		vphi = 0;
		n = 0;
		phi0 = 0;
	}

	// if one ca is sp3 and the other is sp2 with the fringe atom connected to sp2 also sp2
	else if( (order == 1) && (((myAtoms[1].myGeom == 3) && (myAtoms[2].myGeom == 2) && (myAtoms[3].myGeom == 2)) || ((myAtoms[2].myGeom == 3) && (myAtoms[1].myGeom == 2) && (myAtoms[0].myGeom == 2)) ) ){
		n = 3;
		phi0 = Math.PI; // 180 degrees
		vphi = 2;
	}

	// if one ca is sp3 from group 6 and the other is sp2 not from group 6
	else if( (order == 1) && ( ((bfromG6) && (myAtoms[1].myGeom == 3) && (!cfromG6) && (myAtoms[2].myGeom == 2)) || ((cfromG6) && (myAtoms[2].myGeom == 3) && (!bfromG6) && (myAtoms[1].myGeom == 2)) ) ){
		// set Uj, Uk based on the row
		Uj = 0.1;
		if(an1 < 55) Uj = 0.2;
		if(an1 < 37) Uj = 0.7;
		if(an1 < 19) Uj = 1.25;
		if(an1 < 11) Uj = 2;

		Uk = 0.1;
		if(an2 < 55) Uk = 0.2;
		if(an2 < 37) Uk = 0.7;
		if(an2 < 19) Uk = 1.25;
		if(an2 < 11) Uk = 2;

		n = 2;
		phi0 = Math.PI/2; // 90 degrees
		vphi = 5*Math.sqrt(Uj*Uk)*(1+4.18*Math.log(order));
	}

	// if both ca's are sp3 from group 6
	else if( (order == 1) && ((myAtoms[1].myGeom == 3) && (myAtoms[2].myGeom == 3) && (bfromG6) && (cfromG6)) ){
		// set Uj, Uk to 2 for oxygen, 6.8 otherwise
		if(an1 == 8) Vj = 2;
		else Vj = 6.8;
		if(an2 == 8) Vk = 2;
		else Vk = 6.8;

		vphi = Math.sqrt(Vj*Vk);
		n = 2;
		phi0 = Math.PI/2; // 90 degrees
	}

	// if both ca's are sp2
	else if((myAtoms[1].myGeom == 2) && (myAtoms[2].myGeom == 2)){
		// get Uj, Uk based on the row
		Uj = 0.1;
		if(an1 < 55) Uj = 0.2;
		if(an1 < 37) Uj = 0.7;
		if(an1 < 19) Uj = 1.25;
		if(an1 < 11) Uj = 2;

		Uk = 0.1;
		if(an2 < 55) Uk = 0.2;
		if(an2 < 37) Uk = 0.7;
		if(an2 < 19) Uk = 1.25;
		if(an2 < 11) Uk = 2;
		n = 2;
		vphi = 5*Math.sqrt(Uj*Uk)*(1+4.18*Math.log(order));
		phi0 = CalculateDihedral();
		if (order == 1.5 || order == 2)  // fudge factor from DREIDING
		{ if (Math.abs(180 - phi0) > phi0)
			phi0 = 0;
		  else phi0 = Math.PI;
		}
		else
		{ if (Math.abs(180 - phi0) > Math.abs(phi0 - 60))
			phi0 = Math.PI/3; // 60 degrees
		  else phi0 = Math.PI; // 180 degrees
		}
	}

	// if one ca is sp3 and the other is sp2
	else if( ((myAtoms[1].myGeom == 2) && (myAtoms[2].myGeom == 3)) || ((myAtoms[2].myGeom == 2) && (myAtoms[1].myGeom == 3)) )
	{
		n = 6;
		vphi = 1;
		phi0 = 0;
	}

	// if both ca's are sp3
	else if((myAtoms[1].myGeom == 3) && (myAtoms[2].myGeom == 3)){
		vphi = Math.sqrt(Vj*Vk);
		n = 3;
		phi0 = CalculateDihedral();
		if (Math.abs(180 - phi0) > Math.abs(phi0 - 60))
			phi0 = Math.PI/3; // 60 degrees
		else phi0 = Math.PI; // 180 degrees
	}

	// else vphi = 0 and n = 0
	else{
		vphi = 0;
		n = 0;
		phi0 = 0;
	}
//	System.out.println("Vphi = "+vphi+"; n = "+n+"; phi_0 = "+phi0+"; order = "+order);
  }

    public int termLength() { return 4; }
    public String name() { return "Torsion"; }

    // Jeff Stenhouse 02/05/2003
    // A method that calculates the dihedral angle of 4 atoms.
    public double CalculateDihedral()
    {
	double dihedral, dp2, dotProduct;
	double pq[] = {0.0, 0.0, 0.0};
	double rq[] = {0.0, 0.0, 0.0};
	double rs[] = {0.0, 0.0, 0.0};
	double normal[] = {0.0, 0.0, 0.0};
	double normal2[] = {0.0, 0.0, 0.0};
	for (int i = 0; i < 3; i++)
	{   pq[i] = myAtoms[0].x[i] - myAtoms[1].x[i];
	    rq[i] = myAtoms[2].x[i] - myAtoms[1].x[i];
	    rs[i] = myAtoms[2].x[i] - myAtoms[3].x[i];
	}
	normal = crossProduct(pq,rq);
	normal2 = crossProduct(rq,rs);
	dotProduct = dotProduct(normal,normal); // length^2 of normal
	dp2 = dotProduct(normal2,normal2); // length^2 of normal2
	for (int i = 0; i < 3; i++)
	{ normal[i] = normal[i]/Math.sqrt(dotProduct);
	  normal2[i]=normal2[i]/Math.sqrt(dp2);
	}
	dihedral = dotProduct(normal,normal2); // cos(theta)
	dihedral = Math.acos(dihedral);
	double direction = dotProduct(rq,
		crossProduct(crossProduct(pq,rq),crossProduct(rq,rs)));
	if (direction < 0) dihedral = -dihedral;
	return dihedral;
    }
}
