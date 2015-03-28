 package nanocad.minimize.mm3;
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
import nanocad.minimize.*;
import nanocad.term;

public class mm3MinimizeAlgorythm extends nanocad.energyMinimizeAlgorythm
{
/*	public void initTemps(Vector myAtomList){
	  // This is a temporary vector that represents a clone of the
	  // original group.  Save initializations later by doing them
	  // here one time, then just changing the position in f1dim().
	  templist = (Vector) myAtomList.clone();
	  temptermList = new Vector();
	  term t = enumToTerm(templist, temptermList);
	  tempsInited = true;
	}
*/
	protected term enumToTerm(Vector argAtomList, Vector argTermList)
	{
	  term t = new lterm();
	  t.enumerate(argAtomList, argTermList);
	  t = new aterm();
	  t.enumerate(argAtomList, argTermList);
	  t = new tterm();
	  t.enumerate(argAtomList, argTermList);
	  t = new vdwterm();
	  t.enumerate(argAtomList, argTermList);
	  t = new hbondterm();
	  t.enumerate(argAtomList, argTermList);
	  t = new opbterm();
	  t.enumerate(argAtomList, argTermList);
	  t = new estaticterm();
	  t.enumerate(argAtomList, argTermList);
	  t = new strbendterm();
	  t.enumerate(argAtomList, argTermList);
	  t = new strtorterm();
	  t.enumerate(argAtomList, argTermList);
	  t = new angangterm();
	  t.enumerate(argAtomList, argTermList);

	  return t;
	}


	/**
	* Insert the method's description here.
	* Creation date: (7/25/2000 3:07:45 PM)
	* @param newGroup nanocad.group
	*/

	public mm3MinimizeAlgorythm(group newGroup, newNanocad windowForUpdates)
	{
		funEvals = 0;
//		tempsInited = false;
		this.windowForUpdates = windowForUpdates;
		groupToMinimize = newGroup;
		atomList = groupToMinimize.atomList;
		enumerateTerms();
	}

	/**
	* Insert the method's description here.
	* Creation date: (7/25/2000 3:04:23 PM)
	*/
	protected void enumerateTerms() 
	{	termList = new Vector();
		term t = enumToTerm(atomList, termList);
		groupToMinimize.termsEnumerated();
	}
}
