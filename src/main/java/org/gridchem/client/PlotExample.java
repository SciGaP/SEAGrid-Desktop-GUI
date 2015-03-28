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

// This class is not in the ptolemy.plot package so that it is a
// more realistic example.
//mport ptolemy.plot.Plot;
package org.gridchem.client;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JFrame;


// The java.io imports are only necessary for the right hand plot.
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

//////////////////////////////////////////////////////////////////////////
//// PlotExample
/**
TwoPlotExample is a simple example that uses displays two plots side by side
To compile and run this application, do the following:
<pre>
javac -classpath ../../.. TwoPlotExample.java
java -classpath ../../.. ptolemy.plot.demo.TwoPlotExample
</pre>

@author Christopher Hylands
@version $Id: PlotExample.java,v 1.2 2005/05/14 23:51:01 xli16 Exp $
@since Ptolemy II 0.2
*/


public class PlotExample extends JFrame {

    /** We use a constructor here so that we can call methods
     *  directly on the Frame.  The main method is static
     *  so getting at the Frame is a little trickier.
     */
    PlotExample(String str) {
        // Instantiate the plot.
        Plot rightPlot = new Plot();
        
        rightPlot.setTitle("Energy versus iteration"); 
        setSize(400, 400);
        rightPlot.setButtons(false);
        rightPlot.setMarksStyle("none");
//       rightPlot.setImpulses(true);
        File file = new File(".", str);
        try {
//           rightPlot.clear(true);
            rightPlot.read(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + file + " : " + ex);
        } catch (IOException ex) {
            System.err.println("Error reading input: " + file +
                    " : " + ex);
        }
        // Override the title in the file.


        // Layout the two plots
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        getContentPane().setLayout(gridbag);

        // Handle the leftPlot

        // Handle the rightPlot
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        gridbag.setConstraints(rightPlot, c);
        getContentPane().add(rightPlot);

        show();
    }


}
