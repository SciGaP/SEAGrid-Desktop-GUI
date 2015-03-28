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
//import parser; 
import java_cup.runtime.Symbol; 
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import org.gridchem.client.*;

import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/** @author John J. Lee, NCSA
    @version $Id: ParseMP2to5.java,v 1.2 2005/05/14 23:51:01 xli16 Exp $
 */
public class ParseMP2to5 {

  private static boolean DEBUG = false;
  public static JTable table;
  public static ArrayList mp2to5;
  private static class Pair {
    public String name;
    public String value;
    public String toString(){
      return "ParseMP2to5:Pair:  name="+name+", value="+value;
    }
  }


  public static void init() {
    if (DEBUG) System.out.println("ParseMP2to5:init:  entry");
    ///Get null pointer here if **synchronized** is attempted:
    mp2to5 = new ArrayList(100);
  }



  public static void put(String aName, String aValue) {
    if (DEBUG) System.out.println("ParseMP2to5:put:  entry");
    //Creating too many objects?
    Pair p = new Pair();
    p.name = aName;
    p.value = aValue;
    if (DEBUG) System.out.println("ParseMP2to5:put:  "+p.name+" "+p.value);
    synchronized (mp2to5) {
      mp2to5.add(p);
    }
  }


/*
  public static void updateControlPanel() {
    if (DEBUG) System.out.println("ParseMP2to5:updateControlPanel:  OK");
    NodeInfo current = (NodeInfo)DataTree.nodeOnDeck.getUserObject();
    String s = current.dataString;
    String htm = current.nodeURL.toString();
    synchronized (mp2to5) {
      JTable t = ParseMP2to5.getTable();
      ControlPanel cp = new ControlPanel(s, htm, t);
      ///DEBUG:
      ///Branches b = new Branches();
      ///b.createBranch(s, htm, cp);
    }
  }
*/


  public static void setTable(String[] columnNames) {
  //Uses:  for (Iterator i = mp2to5.iterator(); i.hasNext();)
  //           System.out.println(i.next());

    if (DEBUG) {
      System.out.println("ParseMP2to5:setTable:  OK");
      synchronized (mp2to5) {
	for (Iterator i = mp2to5.iterator(); i.hasNext();)
	  System.out.println("ParseMP2to5:setTable:mp2to5.iterator  "+i.next().toString());
      }
    }

    ///CAUSES UNEXPECTED EXCEPTION:
    ///Pair[] pair1D = (Pair[])object1D;

    Pair p = new Pair();
    String[][] sArray = new String[mp2to5.size()][2];
    synchronized (mp2to5) {
      int j = 0;
      for(Iterator i = mp2to5.iterator(); i.hasNext();) {
	p = (Pair)i.next();
	sArray[j][0] = p.name;
	sArray[j][1] = p.value;
	j++;
      }
    }
    ParseMP2to5.table = new JTable(sArray, columnNames);
    ParseMP2to5.table.setPreferredScrollableViewportSize(new Dimension(80, 60));
    /// if (DEBUG) System.out.println("ParseMP2to5:setTable:table.toString  "+ParseMP2to5.table.toString());
  }


/*
  //CALL MEMBMER FUNCTIONS FROM LexerParserBroker instead...

  public static void main(argv, String[] columns) {
    ParseMP2to5.init();
    try { 
      MP2to5Parser p = new MP2to5Parser(MP2to5Lexer(new FileReader(argv))); 
      Object result = p.parse().value; 
    } catch (Exception e) { 
      System.err.println("ParseMP2to5:main:  Exception");
    } finally { 
      System.err.println("ParseMP2to5:main:  finally");
    } 
    ParseMP2to5.setTable(columns);
    mp2to5.clear();
  }
*/


}
