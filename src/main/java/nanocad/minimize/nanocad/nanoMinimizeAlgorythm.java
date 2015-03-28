package nanocad.minimize.nanocad;

/**
 * Copyright (c) 1997 Will Ware, all rights reserved.
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

public abstract class nanoMinimizeAlgorythm extends energyMinimizeAlgorythm
{

public boolean minimizeGroup(group g) {
	int i;
	Vector atomList = g.atomList;
	Vector termList = new Vector();
	for (i = 0; i < atomList.size(); i++)
		 ((atom) atomList.elementAt(i)).rehybridize();
	termList = new Vector();
	atom a = new carbon();
	term t;
	t = new lterm(a, a);
	t.enumerate(atomList, termList);
	t = new aterm(a, a, a);
	t.enumerate(atomList, termList);
	t = new tterm(a, a, a, a);
	t.enumerate(atomList, termList);
	for (double stepsize = 0.1; stepsize > 0.0001; stepsize *= 0.9)
		//step loop
		{
		for (i = 0; i < atomList.size(); i++)
			 ((atom) atomList.elementAt(i)).zeroForce();
		for (i = 0; i < termList.size(); i++)
			 ((term) termList.elementAt(i)).computeForces();
		for (i = 0; i < atomList.size(); i++) {
			int j;
			double flensq, m;
			a = (atom) atomList.elementAt(i);
			for (j = 0, flensq = 0.0; j < 3; j++)
				flensq += a.f[j] * a.f[j];
			if (flensq > 0.0) {
				m = stepsize / Math.sqrt(flensq);
				for (j = 0; j < 3; j++)
					a.x[j] += m * a.f[j];
			}
		}
	} //end step loop
	return true;
} //end minimize group function
}
