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

/* SubmitJobsWindow.java  by Rebecca Hartman-Baker
   This is the GUI for when you press the "Submit Jobs" button.
*/

package org.gridchem.client;

import org.apache.airavata.model.experiment.ExperimentModel;
import org.gridchem.service.exceptions.JobException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

//TODO: the submitted jobs in the lower panel shoudl be refreshed (if status is displayed) when the monitorVO job panel refreshes
//TODO: the monitorVO job panel should be effectively launched and refreshed after a job is submitted.
//TODO: the new editingStuff dialog should be available to launch via resubmit from the monitorVO jobpanel
public class SubmitJobsWindow
{
    public static JFrame frame;
    public static stuffInside si;

    public static ArrayList<ExperimentModel> jobQueue = new ArrayList<ExperimentModel>();  // replaces the JobList
    public static ArrayList<ExperimentModel> jobSubmitted = new ArrayList<ExperimentModel>();

    public static void getInstance() {
        if (frame == null) {
            si = new stuffInside();
            init();
        } else {
            si.update();
            frame.setVisible(true);
        }
    }
    
    public static void getInstance(ExperimentModel job) {
        if (frame == null) {
            si = new stuffInside(job);
            init();
        } else {
            si.update();
            si.doEditNewJob(job);
            frame.setVisible(true);
        }
    }
    
    private static void init() { 
    	frame = new JFrame("GridChem: Submit Jobs");
    	//	frame.getContentPane().add(new stuffInside(ListOfJobs));
    	frame.getContentPane().add(si);
    	frame.pack();
        frame.setFocusable(true);
        //	 Centering the frame on the screen
    	Toolkit kit = frame.getToolkit();
    	Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        Dimension windowSize = frame.getSize();
        int windowWidth = windowSize.width;
        int windowHeight = windowSize.height;
        int upperLeftX = (screenWidth - windowWidth)/2;
        int upperLeftY = (screenHeight - windowHeight)/2;   
        frame.setLocation(upperLeftX, upperLeftY);
        //
    	frame.setVisible(true);
    	//frame.setResizable(false);
    }
    
    public void setVisible(boolean isVisible) {
        frame.setVisible(isVisible);
        
        if (!isVisible) {
            if (si.jobEditor != null) 
                si.jobEditor.dispose();
        
            frame.dispose();
        }
    }

    public void setJobFocus(int jobid) {
    		si.setSelectedIndex(jobid);
    }
    
    public static void addJob(ExperimentModel experiment) {
        jobQueue.add(experiment);
    }

    public static void updateJob(ExperimentModel experiment) {
        int index = jobQueue.indexOf(experiment);
        if (index > -1) {
            jobQueue.remove(index);
            jobQueue.add(index, experiment);
        } else {
            throw new JobException("Could not locate job in the existing job queue.  Job was not updated.");
        }
    }
}