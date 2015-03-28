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
/** @author John J. Lee, NCSA
    @version $Id: LexerParserBroker.java,v 1.2 2005/05/14 23:51:01 xli16 Exp $

    Most other lexers/parsers are instantiated or called from here.
    Since the #line contains all the specifiable job specifications
    for Gaussian 98, it makes sense that this class which parses
    the #line brokers the lexer/parser hierarchy.

    @see pound.flex
    @see *.flex
    @see *.cup
*/

package org.gridchem.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import org.gridchem.client.*;

import java.io.*;

public final class LexerParserBroker {

  public static boolean DEBUG = false;
  private final String label = " KEYWORDS:";





  /** This method is the main broker of consequent parsing tasks.  
   */
  public void method(String id, String idLong, String s, String htm) {

    if("mp"==id) {
      ControlPanel cp = new ControlPanel(idLong+label, s);

      // mp2to5a
      ParseMP2to5.init();
      try { 
	MP2to5aParser p = new MP2to5aParser(
	  new MP2to5aLexer(
	    new FileReader(DataTree.getOutputFile())
	    )); 
	Object result = p.parse().value; 
      } catch (Exception e) { 
	System.err.println("LexerParserBroker:method:mp:  Exception");
      } finally { 
	System.err.println("LexerParserBroker:method:mp:  finally");
      } 
      ParseMP2to5.setTable(new String[] {"", ""});
      ParseMP2to5.mp2to5.clear();
      cp.addToControlPanel("Variation of Moeller-Plesset:", ParseMP2to5.table);

      // mp2to5b
      ParseMP2to5.init();
      try { 
	MP2to5bParser p = new MP2to5bParser(
	  new MP2to5bLexer(
	    new FileReader(DataTree.getOutputFile())
	    )); 
	Object result = p.parse().value; 
      } catch (Exception e) { 
	System.err.println("LexerParserBroker:method:mp:  Exception");
      } finally { 
	System.err.println("LexerParserBroker:method:mp:  finally");
      } 
      ParseMP2to5.setTable(new String[] {"", ""});
      ParseMP2to5.mp2to5.clear();
      cp.addToControlPanel("Frozen-core Options:", ParseMP2to5.table);

      // mp2to5c
      ParseMP2to5.init();
      try { 
	MP2to5cParser p = new MP2to5cParser(
	  new MP2to5cLexer(
	    new FileReader(DataTree.getOutputFile())
	    )); 
	Object result = p.parse().value; 
      } catch (Exception e) { 
	System.err.println("LexerParserBroker:method:mp:  Exception");
      } finally { 
	System.err.println("LexerParserBroker:method:mp:  finally");
      } 
      ParseMP2to5.setTable(new String[] {"", ""});
      ParseMP2to5.mp2to5.clear();
      cp.addToControlPanel("Algorithm Options:", ParseMP2to5.table);

      // mp2to5d
      ParseMP2to5.init();
      try { 
	MP2to5dParser p = new MP2to5dParser(
	  new MP2to5dLexer(
	    new FileReader(DataTree.getOutputFile())
	    )); 
	Object result = p.parse().value; 
      } catch (Exception e) { 
	System.err.println("LexerParserBroker:method:mp:  Exception");
      } finally { 
	System.err.println("LexerParserBroker:method:mp:  finally");
      } 
      ParseMP2to5.setTable(new String[] {"", "Hartrees/molecule"});
      ParseMP2to5.mp2to5.clear();
      cp.addToControlPanel("Energies:", ParseMP2to5.table);

      // create branch
      Branches branch = new Branches();
      branch.createBranch(idLong, htm, cp);
    }




/* 
"am1"
"bd"
"cbs"
"ccd"
"ci"
"complex"
"fmm"
"force"
"freq"
"g1"
"guess"
"gvb"
"hf" 
"indo"
"integral"
"mindo3"
"mndo"
"mm"
"ovgf"
"pm3"
"pop"
"rpa"
"scan"
"sparse"
"stable"
"symm"
"testmo"
"trans"
"zindo"
*/



  }




  public void basis(String id, String s, String htm) {
    Branches b = new Branches();
    ControlPanel cp = new ControlPanel(id+label, s);
    b.createBranch(id, htm, cp);
  }

  public void route(String id, String s, String htm) {
    Branches b = new Branches();
    ControlPanel cp = new ControlPanel(id+label, s);
    b.createBranch(id, htm, cp);
    // create panel
  }

  public void scf(String idLong, String data, String htm) {
    SCFControl scfControl = new SCFControl(idLong, data, htm);
  }

  public void material(String id, String s, String htm) {
    Branches b = new Branches();
    ControlPanel cp = new ControlPanel(id+label, s);
    b.createBranch(id, htm, cp);
  }

  public void geometry(String id, String s, String htm) {
    Branches b = new Branches();
    ControlPanel cp = new ControlPanel(id+label, s);
    b.createBranch(id, htm, cp);
  }

  public void density(String id, String s, String htm) {
    Branches b = new Branches();
    ControlPanel cp = new ControlPanel(id+label, s);
    b.createBranch(id, htm, cp);
  }

  public void system(String id, String s, String htm) {
    Branches b = new Branches();
    ControlPanel cp = new ControlPanel(id+label, s);
    b.createBranch(id, htm, cp);
  }

  public void etc(String id, String s, String htm) {
    Branches b = new Branches();
    ControlPanel cp = new ControlPanel(id+label, s);
    b.createBranch(id, htm, cp);
  }

  public void obsolete(String id, String s, String htm) {
    Branches b = new Branches();
    ControlPanel cp = new ControlPanel(id+label, s);
    b.createBranch(id, htm, cp);
  }

  public void unknown(String id, String s) {
    Branches b = new Branches();
    ControlPanel cp = new ControlPanel(id+label, s);
    b.createBranch(id, "", cp);
  }

  public void runTests() {
  }

  public static void main(String[] args) {
    LexerParserBroker p = new LexerParserBroker();
    p.runTests();
  }
}
