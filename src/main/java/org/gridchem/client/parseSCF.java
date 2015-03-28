package org.gridchem.client;

/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software") to deal with the Software without
restriction, including without limitation the rights to use, 
copy, modify, merge, publish, distribute, sublicense, and/or 
sell copies of the Software, and to permit persons to whom 
the Software is furnished to do so, subject to the following 
conditions:
1. Redistributions of source code must retain the above copyright notice, 
   this list of conditions and the following disclaimers.
2. Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimers in the documentation
   and/or other materials provided with the distribution.
3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
   University of Illinois at Urbana-Champaign, nor the names of its contributors 
   may be used to endorse or promote products derived from this Software without 
   specific prior written permission.
    
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS WITH THE SOFTWARE.

*/
///import parser; 
import java_cup.runtime.Symbol; 
import javax.swing.*;

import org.gridchem.client.*;
import org.gridchem.client.common.Settings;

import java.awt.*;
import java.util.*;
/*************************************************************************** 
    <b>parseSCF</b> is heavily used by lexers & parsers that process 
    the SCF keywords of Gaussian 98.

    @author John J. Lee, NCSA
    @version $Id: parseSCF.java,v 1.3 2005/07/05 21:47:22 dooley Exp $
*/
public class parseSCF {




  public static boolean DEBUG = false;
  private       static boolean	layeredMapFormCycleInt = false;
  private       static boolean	layeredMapFormInt = false;
  private       static boolean	flatMap = false;
  private       static boolean	flatMapLabel = false;
  private       static int numEpochs = 0;
  private       static int numFields = 0;
  public        static LayeredHashMap map; // contains epochs x fields




/** ----------------------------------------------
    CONSTRUCTOR
 */
  public parseSCF() {
    map = new LayeredHashMap("via ParseSCF");
  }




/** --------------------------------------------------------
 */
  public static void putField(String k, String v) {
    map.put(k, v);
  }
  public void putField(String k, String k2, String v) {
    map.put(k, k2, v);
  }




/** --------------------------------------
*/
  public static LayeredHashMap getMap() {
    if (null == map) {
      System.err.println("parseSCF:getTable:  map is empty");
      System.err.println("                     returning null");
      return null; // GIVING UP
    }
    return map;
  }




/** --------------------------------------------------------
 */
  public static JTable getTable() {
    if (null == getMap()) {
      System.err.println("parseSCF:getTable:  getMap() returned nothing");
      System.err.println("                     returning null");
      return null; // GIVING UP
    } else {

      // get map & count of fields for the 1st epoch
      // this may fail
      LayeredHashMap fieldsMap = new LayeredHashMap("fieldsMap");
      try{
	fieldsMap = (LayeredHashMap)map.get("cycle 1");
	numEpochs = map.size();
	numFields = map.size("cycle 1");
	layeredMapFormCycleInt = true;
	System.out.println("ParseSCF:getTable:  numEpoch --> "+String.valueOf(numEpochs));
	System.out.println("ParseSCF:getTable:  numFields --> "+String.valueOf(numFields));
      } catch (Exception e) {
	try{
	  fieldsMap = (LayeredHashMap)map.get("1");
	  numEpochs = map.size();
	  numFields = map.size("1");
	  layeredMapFormInt = true;
	  System.out.println("ParseSCF:getTable:  numEpoch --> "+String.valueOf(numEpochs));
	  System.out.println("ParseSCF:getTable:  numFields --> "+String.valueOf(numFields));
	} catch (Exception e2) {
	  //Assuming there is only one epoch
	  fieldsMap = map;
	  numEpochs = 1;
	  numFields = map.size();
	  flatMap = true;
	  System.out.println("ParseSCF:getTable:  numEpoch --> "+String.valueOf(numEpochs));
	  System.out.println("ParseSCF:getTable:  numFields --> "+String.valueOf(numFields));
	}
      }

      System.out.println("ParseSCF:getTable:  layeredMapFormCycleInt --> "+layeredMapFormCycleInt);
      System.out.println("ParseSCF:getTable:  layeredMapFormInt --> "+layeredMapFormInt);
      System.out.println("ParseSCF:getTable:  flatMap --> "+flatMap);

      if (flatMap) {

	// Create an ArrayList that holds an ordered list of the 
	// "cycle 1" keySet; keys not in the "cycle 1" keySet will be ignored; 
	ArrayList fieldsKeyset = new ArrayList(fieldsMap.keySet());

	// Check consistency
	if (numFields != fieldsKeyset.size()) {
	  System.out.println("parseSCF:getTable:  INCONSISTENT FIELDSKEYSET SIZES");
	  System.out.println("                     numFields --> "+numFields);
	  System.out.println("                     fieldsKeyset.size() --> "+fieldsKeyset.size());
	  System.err.println("                     returning null");
	  return null; // GIVING UP
	}

	// epoch labels will be put on the table
	String[] epochLabels = new String[2]; 

	// fill the top of the table with labels for the epochs
	// which should have been assigned to map.label
	if (flatMapLabel) {
	  epochLabels[0] = "";
	  epochLabels[1] = "value";
	} else {
	  epochLabels[0] = "";
	  epochLabels[1] = "";
	}

	// fields contains a field matrix for use in table;
	// fields' rows have fields;
	// fields' cols have epochs; 
	// provide an extra col for holding field labels
	String[][] fields = new String[numFields][2];
	
	// fill the left hand side of the table with the names of field keys
	for (int k = 0; k < numFields; k++) {
	  fields[k][0] = (String)fieldsKeyset.get(k);
	}

	// populate the remainder of the table
	for (int i = 0; i < numFields; i++) {
	    if (null == map.get((String)fieldsKeyset.get(i))) {
	      fields[i][1] = "";
	    } else {
	      fields[i][1] = (String)map.get((String)fieldsKeyset.get(i));
	    }
	}

	// print fields for debugging
	if (DEBUG) {
	  System.out.println("ParseSCF:getTable:  fields[][]");
	  for (int j = 0; j < numEpochs + 1; j++) {
	    for (int i = 0; i < numFields; i++) {
	      System.out.println("fields["+i+"]["+j+"] --> "+fields[i][j]);
	    }
	  }
	  System.out.println("ParseSCF:getTable:  epochLabels[]");
	  for (int k = 0; k < numEpochs; k++) {
	    System.out.println("epochLabels["+k+"] --> "+epochLabels[k]);
	  }
	}

	// final assembly
	JTable table = new JTable(fields, epochLabels);
	table.setPreferredScrollableViewportSize(Settings.TABLE_VIEWPORT);
	System.out.println("parseSCF:getTable:  successful exit; flatMap --> "+flatMap);
	return table;

      } else {// !flatMap

	// Create an ArrayList that holds an ordered list of the 
	// epochs keySet
	ArrayList epochsKeyset = new ArrayList();
	if (layeredMapFormCycleInt) {
	  for (int ek = 0; ek < map.size(); ek++) {
	    epochsKeyset.add(ek, "cycle "+String.valueOf(ek + 1));
	  }
	} else {
	  // layeredMapFormInt
	  for (int ek = 0; ek < map.size(); ek++) {
	    epochsKeyset.add(ek, String.valueOf(ek + 1));
	  }
	}

	// Check consistency
	if (numEpochs != epochsKeyset.size()) {
	  System.out.println("parseSCF:getTable:  INCONSISTENT EPOCHSKEYSET SIZES");
	  System.out.println("                     numEpochs --> "+numEpochs);
	  System.out.println("                     epochsKeyset.size() --> "+epochsKeyset.size());
	  System.err.println("                     returning null");
	  return null; // GIVING UP
	}
	
	// epoch labels will also be put on the table
	String[] epochLabels = new String[numEpochs + 1]; 

	// fill the top of the table with labels for the epochs
	// which also should have been assigned to map.label
	epochLabels[0] = "";
	for (int k = 0; k < numEpochs; k++) {
	  epochLabels[k + 1] = (String)epochsKeyset.get(k);
	}

	// Create an ArrayList that holds an ordered list of the 
	// "cycle 1" keySet; keys not in the "cycle 1" keySet will be ignored; 
	ArrayList fieldsKeyset = new ArrayList(fieldsMap.keySet());

	// Check consistency
	if (numFields != fieldsKeyset.size()) {
	  System.out.println("parseSCF:getTable:  INCONSISTENT FIELDSKEYSET SIZES");
	  System.out.println("                     numFields --> "+numFields);
	  System.out.println("                     fieldsKeyset.size() --> "+fieldsKeyset.size());
	  System.err.println("                     returning null");
	  return null; // GIVING UP
	}

	// fields contains a field matrix for use in table;
	// fields' rows have fields;
	// fields' cols have epochs; 
	// provide an extra col for holding field labels
	String[][] fields = new String[numFields][numEpochs + 1];
	
	// fill the left hand side of the table with the names of field keys
	for (int k = 0; k < numFields; k++) {
	  fields[k][0] = (String)fieldsKeyset.get(k);
	}

	// populate the remainder of the table
	for (int i = 0; i < numFields; i++) {
	  for (int j = 0; j < numEpochs; j++) {
	    if (null == map.get((String)epochsKeyset.get(j), (String)fieldsKeyset.get(i))) {
	      fields[i][j + 1] = "";
	    } else {
	      fields[i][j + 1] = (String)map.get((String)epochsKeyset.get(j), (String)fieldsKeyset.get(i));
	    }
	  }
	}

	// print fields for debugging
	if (DEBUG) {
	  System.out.println("ParseSCF:getTable:  fields[][]");
	  for (int j = 0; j < numEpochs + 1; j++) {
	    for (int i = 0; i < numFields; i++) {
	      System.out.println("fields["+i+"]["+j+"] --> "+fields[i][j]);
	    }
	  }
	  System.out.println("ParseSCF:getTable:  epochLabels[]");
	  for (int k = 0; k < numEpochs; k++) {
	    System.out.println("epochLabels["+k+"] --> "+epochLabels[k]);
	  }
	}

	// final assembly
	JTable table = new JTable(fields, epochLabels);
	table.setPreferredScrollableViewportSize(Settings.TABLE_VIEWPORT);
	System.out.println("parseSCF:getTable:  successful exit; flatMap --> "+flatMap);
	return table;

      }// end if (flatMap) then {} else {}
    }// end if (null == getMap()) then {} else {}
  }// end method getTable




  public static void main(String[] argv) {
  }




}
