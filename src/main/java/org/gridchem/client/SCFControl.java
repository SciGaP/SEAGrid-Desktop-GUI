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


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

import org.gridchem.client.Branches;
import org.gridchem.client.ControlPanel;
import org.gridchem.client.DataTree;

import java.io.*;

/** @author John J. Lee, NCSA
    @version $Id: SCFControl.java,v 1.2 2005/05/14 23:51:01 xli16 Exp $
 */
public class SCFControl {

  private static boolean DEBUG = true;
  private String idLong;
  private String data;
  private String htm;
  private final String keyword = " KEYWORDS:";
  private ControlPanel cp; 
  private JTable table;
  private int[] selectedRows;
  private int firstSelectedRow;

  public SCFControl(String idLong, String data, String htm) {
    this.idLong = idLong;
    this.data = data;
    this.htm = htm;
    cp = new ControlPanel(idLong+keyword, data);
    scfa();
    scfb();
    Branches branch = new Branches();
    branch.createBranch(idLong, htm, cp);
  }// end constructor




  private void scfa() {

    try { 
      SCFaParser p = new SCFaParser(
	new SCFaLexer(
	  new FileReader(DataTree.getOutputFile())
	  )); 
      Object result = p.parse().value; 
    } catch (Exception e) { 
      System.err.println("SCFControl:  Exception from SCFaParser or SCFaLexer");
    } 

    // make table & get selection info.
    String tableLabel = CUP$SCFaParser$actions.getTableLabel();
    table = CUP$SCFaParser$actions.getTable();
    
    // make checkbox to allow selection of log-abs plots
    JCheckBox logButton = new JCheckBox("Plot log(abs(...))");
    logButton.setSelected(false);
    PlotAdapter.plotLog = false;
    logButton.setToolTipText(
      "Click to place a check here and plots will show logs of the absolute values of the selected data"
      );
    if (DEBUG) System.out.println(
      "SCFControl:scfa():  PlotAdapter.plotLog --> "+PlotAdapter.plotLog
      );
    logButton.addItemListener(new ItemListener() {
	public void itemStateChanged(ItemEvent e) {
	  if (ItemEvent.SELECTED == e.getStateChange()) PlotAdapter.plotLog = true;
	  if (ItemEvent.DESELECTED == e.getStateChange()) PlotAdapter.plotLog = false;
	  if (DEBUG) System.out.println(
	    "SCFControl:scfa():  PlotAdapter.plotLog --> "+PlotAdapter.plotLog
	    );
	}
      });

    // make plot button & listen for actions
    JButton button = new JButton("Plot Selection");
    button.setToolTipText(
      "Select a row to plot, then click here.  "+
      "Use the shift or ctrl keys to select multiple rows."
      );
    button.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {

	  SCFControl.this.table = CUP$SCFaParser$actions.getTable();
	  SCFControl.this.firstSelectedRow = SCFControl.this.table.getSelectedRow();
	  boolean noTableSelection = true;
	  if (SCFControl.this.firstSelectedRow > -1) noTableSelection = false;
	  SCFControl.this.selectedRows = SCFControl.this.table.getSelectedRows();

	  if (noTableSelection) {
	    JOptionPane.showMessageDialog(
	      null,
	      "Please select at least one row from the table using your mouse",
	      "WE HAVE A PROBLEM",
	      JOptionPane.ERROR_MESSAGE
	      );
	  } else {// instantiate plot classes
	    String title = "SCF";
	    PlotAdapter a = new PlotAdapter(
	      SCFControl.this.table, SCFControl.this.selectedRows, title
	      );
	  }
	}
      });

    if (null == tableLabel) tableLabel = "";
    if (null != table) cp.addToControlPanel(tableLabel, table, logButton, button);

  }// end private void scfa()




  private void scfb() {

    try { 
      SCFbParser p = new SCFbParser(
	new SCFbLexer(
	  new FileReader(DataTree.getOutputFile())
	  )); 
      Object result = p.parse().value; 
    } catch (Exception e) { 
      System.err.println("SCFControl:  Exception from SCFbParser or SCFbLexer");
    } 

    String tableLabel = CUP$SCFbParser$actions.getTableLabel();
    JTable table = CUP$SCFbParser$actions.getTable();

    if (null == tableLabel) tableLabel = "";
    if (null != table) cp.addToControlPanel(tableLabel, table);

  }// end private void scfb()




}
