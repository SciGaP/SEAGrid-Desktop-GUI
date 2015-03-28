/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software"), to deal with the Software without 
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

/*
 * Created on Apr 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.gridchem.client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;


/**
 * @author Xiaohai Li
 *
 * OnePlotExample is used to display a plot.
 * 
 */
public class OnePlotExample extends JFrame {
	
	OnePlotExample(String str) {
		
		/*
		//Initiate one plot
        Plot myPlot = new Plot();
        String xlabel;
        String ylabel;
        String title;
        
        //Set the size of the toplevel window.
        setSize(400, 400);
        
        myPlot.setButtons(false);
        myPlot.setMarksStyle("none");
        myPlot.setImpulses(true);
        try {
        	myPlot.clear(true);
            myPlot.read(new FileInputStream(str));
            xlabel = myPlot.getXLabel();
            ylabel = myPlot.getYLabel();
            title = myPlot.getTitle();
            myPlot.setXLabel(xlabel);
            myPlot.setYLabel(ylabel);
            myPlot.setTitle(title);
            System.err.println(xlabel + "\n" + ylabel + "\n" + title);
            myPlot.repaint();
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " +ex);
        } catch (IOException ex) {
            System.err.println("Error reading input: " 
                     + ex);
        }
        
        //layout
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        getContentPane().setLayout(gridbag);

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gridbag.setConstraints(myPlot, c);
        getContentPane().add(myPlot);
        setVisible(true);
        
        */
		
		String [] args = {str};
        try {
            new PlotApplication(new Plot(), args);
        } catch (Exception ex) {
            System.err.println(ex.toString());
            ex.printStackTrace();
        }
        
	}

}
