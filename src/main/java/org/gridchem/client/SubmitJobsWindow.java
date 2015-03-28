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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JFrame;

import org.gridchem.client.common.Settings;
import org.gridchem.client.util.GMS3;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.ProjectBean;
import org.gridchem.service.exceptions.JobException;
import org.gridchem.service.model.enumeration.AccessType;

//TODO: the submitted jobs in the lower panel shoudl be refreshed (if status is displayed) when the monitorVO job panel refreshes
//TODO: the monitorVO job panel should be effectively launched and refreshed after a job is submitted.
//TODO: the new editingStuff dialog should be available to launch via resubmit from the monitorVO jobpanel
public class SubmitJobsWindow
{
    public static JFrame frame;
    public static stuffInside si;

    public static ArrayList<JobBean> jobQueue = new ArrayList<JobBean>();  // replaces the JobList
    public static ArrayList<JobBean> jobSubmitted = new ArrayList<JobBean>();
    
    public static void main (String[] args) {
    
        GridChem gc = new GridChem();
        
        Properties props = new Properties();
        
        Settings.WEBSERVICE = true;
        
//        GMSSession session = GMSSession.getInstance();
//                
//        try {
//            session.establishSession();
//        } catch (ConnectException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//            JOptionPane.showMessageDialog(null, "A session error has occurred.\n" + 
//                    "Please check your connection\nand authenticate again.",
//                    "Connection Error", JOptionPane.ERROR_MESSAGE);
//        } catch (SessionException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//            JOptionPane.showMessageDialog(null, "A session error has occurred.\n" + 
//                    "Please check your connection\nand authenticate again.",
//                    "Connection Error", JOptionPane.ERROR_MESSAGE);
//        } catch (PermissionException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "A session error has occurred.\n" + 
//                    "Please check your connection\nand authenticate again.",
//                    "Connection Error", JOptionPane.ERROR_MESSAGE);
//        } 
        
//        final GMS gms = GMS.getInstance();
//        
        // Read in user information from the configuration file
        try {
            props.load(new FileInputStream("etc/test.properties"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        String username = props.getProperty("gridchem.username");
        
        String password = props.getProperty("gridchem.password");
        
        // Authenticate with the GMS_WS
        if (Settings.DEBUG)
            System.out.println("Logging " + username + " into the CCG.");
        String key = "";
        try {
            GMS3.login(username,password,AccessType.COMMUNITY,new HashMap<String,String>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Settings.authenticated = true;
        Settings.authenticatedGridChem = true;
        Settings.gridchemusername = username;
        
        try {
            
            // Load the user's resources into the session.
            ProjectBean project = null;
            
            for(ProjectBean p: GMS3.getProjects()) {
                if(p.getType().equals(AccessType.COMMUNITY)) {
                    project = p;
                }
            }
            
            if (Settings.DEBUG)
                System.out.println("Successfully loaded user's VO");
            
//            TODO: figure out how to work around this vo issue.
//            GridChem.userVO = GMS3.getUserVo();
            
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // Start up the file browser in standalone mode
                    SubmitJobsWindow sw = new SubmitJobsWindow();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            });
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static void getInstance() {
        if (frame == null) {
            si = new stuffInside();
            init();
        } else {
            si.update();
            frame.setVisible(true);
        }
    }
    
    public static void getInstance(JobBean job) {
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
    
    public static void addJob(JobBean job) {
        jobQueue.add(job);
    }

    public static void updateJob(JobBean job) {
        int index = jobQueue.indexOf(job);
        if (index > -1) {
            jobQueue.remove(index);
            jobQueue.add(index, job);
        } else {
            throw new JobException("Could not locate job in the existing job queue.  Job was not updated.");
        }
    }
}

// Moved to stuffInside.java @CCS,UKy

//class doSubBetween extends MyLongTask
//{
//    int wha;
//    
//    doSubBetween() {
//    	super();
//    	wha = 0;
//    }
//    
//    public void go() {
//    	current = 0;
//    	final SwingWorker worker = new SwingWorker() {
//    	    public Object construct() {
//    	        try {
//                    new doSubmitJobs();
//                } catch (Exception e) {
//                    SubmitJobsWindow.si.setButtonsEnabled(true);
//                }
//                
//                return null;
//    	    }
//    	};
//        
//    	worker.start();
//    }
//
//}