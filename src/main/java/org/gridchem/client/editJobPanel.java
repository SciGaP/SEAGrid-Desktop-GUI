/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChemtopBox.add(new JLabel("Choose a machine: "));
	topBox.add(machCombo);

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

/* editJobPanel.java  by Rebecca Hartman-Baker
   This opens the job editing panel */

package org.gridchem.client;

import javax.swing.JFrame;
import javax.swing.UIManager;

import nanocad.nanocadFrame;

import org.gridchem.client.common.Settings;
import org.gridchem.client.gui.jobsubmission.EditJobPanel;
import org.gridchem.service.beans.JobBean;

public class editJobPanel
{
    public static JFrame frame;
    //public JFrame frame;
	public EditJobPanel es;
    public nanocadFrame nanoFRM;

    //    public editJobPanel() {}

    public editJobPanel(int n)
    {
    	frame = new JFrame("GridChem: Job Editor");
    	es = new EditJobPanel();
    	frame.getContentPane().add(es);
    	frame.pack();
    	frame.setVisible(true);
    	frame.setResizable(true);
        frame.setFocusable(true);
    	
    }
    
    
    /**
     * Edit an existing job from the user's history
     * 
     * @param job
     */
    public editJobPanel(JobBean job) {
        frame = new JFrame("GridChem: Job Editor");
        es = new EditJobPanel(null,job);
        frame.getContentPane().add(es);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setFocusable(true);
    }
    
    public static void main(String[] args)
    {
    	//lixh_4/29/05
        // The Java look & feel is pretty lame, so we use the native
        // look and feel of the platform we are running on.
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignore exceptions, which only result in the wrong look and feel.
        }
        
    	Settings settings = Settings.getInstance();
    	editJobPanel ej = new editJobPanel(-1);
    	editJobPanel.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

// Moved to editingStuff.java @ CCS,UKy