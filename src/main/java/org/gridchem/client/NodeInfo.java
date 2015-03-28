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
package org.gridchem.client;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;
import java.net.URL;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/** @author John J. Lee, NCSA
    @version $Id: NodeInfo.java,v 1.1.1.1 2005/04/26 16:34:00 dooley Exp $
 */
public class NodeInfo {

  public URL nodeURL;
  public String dataString;
  public ControlPanel controls;

  public NodeInfo(String s, String htm) {
    dataString = s;
    try {
      nodeURL = new URL(htm);
    } catch (java.net.MalformedURLException exc) {
      nodeURL = DataTree.helpURL;
    }
  }// end constructor

  public NodeInfo(String s, String htm, ControlPanel cp) {
    dataString = s;
    controls = cp;
    try {
      nodeURL = new URL(htm);
    } catch (java.net.MalformedURLException exc) {
      nodeURL = DataTree.helpURL;
    }
  }// end constructor

  public String toString() {
    return dataString;
  }



}// end public class NodeInfo




