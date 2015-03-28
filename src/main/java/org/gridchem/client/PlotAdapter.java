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
import javax.swing.table.*;

import org.gridchem.client.*;

import java.io.*;

/** @author John Lee
    @version $Id: PlotAdapter.java,v 1.2 2005/05/14 23:51:01 xli16 Exp $
 */
public class PlotAdapter {




  public static boolean DEBUG = true;
  private String title;
  private int numSets;
  private String[] abscissaLabels;
  private String   abscissaLabel = "";
  private String   abscissaLabelShort = "";
  private String[] ordinateLabels;
  private String   ordinateLabel = "";
  private double[][] plotMatrix;
  private int xmin, xmax;
  private double ymin, ymax;
  private static double tmp; // for fast storage on the stack/heap
  public static boolean plotLog;

  /* TO CHANGE:
     FOR GREATER PORTABILITY, "\n" SHOULD BE REPLACED WITH
     OBJECTS SIMILAR TO BufferedWriter.newLine()
   */
  private final String ptplotHeader = 
  "<?xml version=\"1.0\" standalone=\"yes\"?>\n" +
  "<!DOCTYPE plot PUBLIC \"-//UC Berkeley//DTD PlotML 1" +
  "//EN\" \"http://ptolemy.eecs.berkeley.edu/xml/dtd/PlotML_1.dtd\">\n" +
  "<plot>\n" +
  "<!-- Ptolemy plot, version 5.1p1 , PlotML format. -->\n";





  //====================================================================  
  public PlotAdapter (JTable table, int[] selectedRows, String title) {

    this.title = title;
    TableColumnModel columnModel = table.getColumnModel();
    TableColumn tc;
    TableModel model = table.getModel();
    numSets = selectedRows.length;
    xmin = 1; // don't want to include x = 0.0, which holds the ordinate label
    xmax = columnModel.getColumnCount();

    //handle abscissas
    abscissaLabels = new String[xmax];
    for (int c = 0; c < xmax; c++) {
      tc = columnModel.getColumn(c);
      abscissaLabels[c] = (String)tc.getIdentifier();
      abscissaLabel += (String)tc.getIdentifier() + " ";
    }
    abscissaLabelShort = abscissaLabels[1];
    if (DEBUG) System.out.println("PlotAdapter:  abscissaLabel --> "+abscissaLabel);
    if (DEBUG) System.out.println("PlotAdapter:  abscissaLabelShort --> "+abscissaLabelShort);

    //handle ordinates
    ordinateLabels = new String[numSets];
    for (int r = 0; r < numSets; r++) {
      ordinateLabels[r] = (String)model.getValueAt(selectedRows[r], 0);
      ordinateLabel += (String)model.getValueAt(selectedRows[r], 0) + " ";
    }
    if (DEBUG) System.out.println("PlotAdapter:  ordinateLabel --> "+ordinateLabel);

    // assign plotMatrix, min'mums, max'mums
    ymin = Double.POSITIVE_INFINITY;
    ymax = Double.NEGATIVE_INFINITY;
    plotMatrix = new double[numSets][xmax];
    for (int r = 0; r < numSets; r++) {
      for (int c = 1; c < xmax; c++) {
	String s = (String)model.getValueAt(selectedRows[r], c);
	if (null == s || "" == s || " " == s) {
	  plotMatrix[r][c] = 0;
	  tmp = 0;
	} else {
          // NEED TO DO SOMETHING ABOUT DOUBLES IN THE FORM [+|-]*.****D[+|-]**
	  String s2 = s.replace('D','E');
	  String s3 = s2.replace('d','e');
	  plotMatrix[r][c] = Double.parseDouble(s2);
	  tmp = Double.parseDouble(s2);
	}
	if (DEBUG) System.out.println("PlotAdapter:  plotMatrix["+r+"]["+c+"] = "+tmp);
	if (tmp > ymax) ymax = tmp;
	if (tmp < ymin) ymin = tmp;
      }// end for (int c...
    }// end for (int r...

    String plotBuffer;
    if (!plotLog) {
      plotBuffer = setPlotMLBuffer();
    } else {
      plotBuffer = setPlotLogMLBuffer();
    }
    PlotPlotML plot = new PlotPlotML();
    try {
      plot.plotString(plotBuffer);
    } catch (IOException ioe) {
      System.err.println("PlotAdapter:constructor:  failed to execute plot.plotString(plotBuffer)");
      System.err.println(ioe.toString());
      ioe.printStackTrace();
    }

  }// end constructor




  //====================================================================  
  public String setPlotMLBuffer() {

    if (DEBUG) System.out.println("PlotAdapter:setPlotMLBuffer:  enter");
    StringBuffer sb = new StringBuffer();
    sb.append(ptplotHeader);
    sb.append("<title>"+title+"</title>\n");
    sb.append("<xLabel>"+abscissaLabelShort+"</xLabel>\n");
    sb.append("<yLabel>"+ordinateLabel+"</yLabel>\n");
    int xmax2 = xmax - 1;
    sb.append("<xRange min=\""+xmin+"\" max=\""+xmax2+"\"/>\n");
    sb.append("<yRange min=\""+ymin+"\" max=\""+ymax+"\"/>\n");
    sb.append("<default stems=\"yes\"/>\n");
    for (int set = 0; set < numSets; set++) {
      sb.append("<dataset>\n");
      for (int x = xmin; x < xmax; x++) {
	sb.append("<p x=\""+x+"\" y=\""+plotMatrix[set][x]+"\"/>;\n");
      }
      sb.append("</dataset>\n");
    }
    sb.append("</plot>\n");
    if (DEBUG) {
      System.out.println("PlotAdapter:setPlotMLString():  ");
      System.out.println(sb);
    }
    return sb.toString();

  }// end public String setPlotMLBuffer()




  //====================================================================  
  public String setPlotLogMLBuffer() {

    if (DEBUG) System.out.println("PlotAdapter:setPlotLogMLBuffer:  enter");
    StringBuffer sb = new StringBuffer();
    sb.append(ptplotHeader);
    sb.append("<title>"+"log abs "+title+"</title>\n");
    sb.append("<xLabel>"+abscissaLabelShort+"</xLabel>\n");
    sb.append("<yLabel>"+"log abs "+ordinateLabel+"</yLabel>\n");
    int xmax2 = xmax - 1;
    sb.append("<xRange min=\""+xmin+"\" max=\""+xmax2+"\"/>\n");
    double lnTen = Math.log(10.0);
    double laYmin = Math.log(Math.abs(ymin))/lnTen;
    double laYmax = Math.log(Math.abs(ymax))/lnTen;
    sb.append("<yRange min=\""+laYmin+"\" max=\""+laYmax+"\"/>\n");

    ///-------------------------------
    ///sb.append("<yLog>on</yLog>\n");
    ///-------------------------------

    sb.append("<default stems=\"yes\"/>\n");
    for (int set = 0; set < numSets; set++) {
      sb.append("<dataset>\n");
      for (int x = xmin; x < xmax; x++) {
	double laPlotMatrix = Math.log(Math.abs(plotMatrix[set][x]))/lnTen;
	sb.append("<p x=\""+x+"\" y=\""+laPlotMatrix+"\"/>;\n");
      }
      sb.append("</dataset>\n");
    }
    sb.append("</plot>\n");
    if (DEBUG) {
      System.out.println("PlotAdapter:setPlotMLString():  ");
      System.out.println(sb);
    }
    return sb.toString();

  }// end public String setPlotMLBuffer()




}// end public class PlotAdapter
