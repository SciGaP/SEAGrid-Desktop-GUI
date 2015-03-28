/*  (Joohyun Kim)  Feb. 2007 Modified version of TwoExamplePlot for Spectra Viewing
	 * 1. Design
	 * a. Try to be a consistent look with gausian optimization
	 * b. One main frame with one spectra panel and two more pannel can be
	 * popped up if necessary. (Two more panels include more fine zoom spectra,
	 * and vibrational animation)
	 * 
	 * 2. Original codes are commented out, but should find the original code in demo dir. */



/* A simple plot application with two plots



 Copyright (c) 1998-2006 The Regents of the University of California.

 All rights reserved.

 Permission is hereby granted, without written agreement and without

 license or royalty fees, to use, copy, modify, and distribute this

 software and its documentation for any purpose, provided that the above

 copyright notice and the following two paragraphs appear in all copies

 of this software.



 IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY

 FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES

 ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF

 THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF

 SUCH DAMAGE.



 THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,

 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF

 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE

 PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF

 CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,

 ENHANCEMENTS, OR MODIFICATIONS.



 PT_COPYRIGHT_VERSION_2

 COPYRIGHTENDKEY

 */

package org.gridchem.client;



import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;

import org.gridchem.client.common.Settings;
import org.gridchem.service.beans.JobBean;



//////////////////////////////////////////////////////////////////////////
/*

 @author Christopher Hylands

 @version $Id: SpectraViewer.java,v 1.2.2.1.4.1.2.1 2014/09/25 21:55:28 yefan Exp $

 @since Ptolemy II 0.2

 @Pt.ProposedRating red (eal)

 @Pt.AcceptedRating red (cxh)

@author Joohyun Kim (2007 Feb-March)

 *
 *
 */

public class SpectraViewer extends JFrame {
	
    public SpectraViewer(JobBean job) {

    	String time = new SimpleDateFormat("yyMMdd").format(job.getCreated());
        
        Settings.jobDir = Settings.defaultDirStr + File.separator + 
                job.getExperimentName() + File.separator + 
                job.getName() + "." + job.getHostName() + "." + 
                job.getLocalId() + "." + time;
        
        System.out.println("Settings.jobDir");
        
        String goutFileName = job.getName()+".out";
        
        SpectraPlot spectraPlot = new SpectraPlot(Settings.jobDir, goutFileName);
        
 
  	    this.setDefaultLookAndFeelDecorated(true);
     	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        


        // Set the size of the toplevel window.

        setSize(800, 300);

        
        spectraPlot.setButtons(true);

        spectraPlot.setTitle("Vibrational Spectra "+"(IR)");
        spectraPlot.setXLabel("wavenumber (1/cm)");
        spectraPlot.setYLabel("Intensity");

        spectraPlot.setConnected(false,0);      // should be located before reading dataset
   //     spectraPlot.setImpulses(true);
    
        spectraPlot._checkDatasetIndex(0);
		
   
        

        GridBagLayout gridbag = new GridBagLayout();

        GridBagConstraints c = new GridBagConstraints();

        getContentPane().setLayout(gridbag);



        c.gridx = 1;

        c.gridy = 0;

        c.gridwidth = 1;

        c.fill = GridBagConstraints.BOTH;

        c.weightx = 1.0;

        c.weighty = 1.0;

        gridbag.setConstraints(spectraPlot, c);
               
        getContentPane().add(spectraPlot);



        setVisible(true);

    }




    /** main method called in a standalone java application.

     *  We simple instantiate this class, most of the work

     *  happens in the constructor.

     */
/*
    public static void main(String[] args) {

        // We execute everything in the Swing Event Thread, see

        // the comment

        Runnable doAction = new Runnable() {

            public void run() {

                new SpectraViewer("g.out");  // with a default file name

            }

        };



        SwingUtilities.invokeLater(doAction);

    }
*/
    
    
    
    
}

