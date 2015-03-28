/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Apr 17, 2006
 * 
 * Developed by: CCT, Center for Computation and Technology, 
 * 				NCSA, University of Illinois at Urbana-Champaign
 * 				OSC, Ohio Supercomputing Center
 * 				TACC, Texas Advanced Computing Center
 * 				UKy, University of Kentucky
 * 
 * https://www.gridchem.org/
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal with the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimers.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimers in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
 *    University of Illinois at Urbana-Champaign, nor the names of its contributors 
 *    may be used to endorse or promote products derived from this Software without 
 *    specific prior written permission.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS WITH THE SOFTWARE.
*/

package org.gridchem.client.gui.panels.myccg;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gridchem.client.GridChem;
import org.gridchem.client.Invariants;
import org.gridchem.client.common.Settings;
import org.gridchem.client.gui.panels.WarningDialog;
import org.gridchem.client.gui.panels.myccg.job.JobPanel;
import org.gridchem.client.gui.panels.myccg.project.UsagePanel;
import org.gridchem.client.gui.panels.myccg.resource.ResourcePanel;
import org.gridchem.client.interfaces.Timeable;
import org.gridchem.client.util.Env;
import org.gridchem.client.util.GMS3;
import org.gridchem.client.util.timer.RefreshVOTimer;
import org.gridchem.service.beans.ComputeBean;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.LoadBean;
import org.gridchem.service.beans.ProjectBean;
import org.gridchem.service.beans.UsageBean;
import org.gridchem.service.beans.UserBean;
import org.gridchem.service.model.enumeration.AccessType;
import org.gridchem.service.model.enumeration.JobStatusType;

/**
 * Base container to hold the tabbed monitoring panels for jobs, resource status, and
 * user project usage.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class MonitorVO extends JFrame implements Timeable {
    private RefreshVOTimer refreshTimer;
    private MonitorPanel monitorPanel = null;
//    private VO userVO;
    static int currentTabIndex = 0;
    public static WarningDialog warningDialog = null;
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public MonitorVO() {
        super("My CCG");
        
        monitorPanel = new MonitorPanel();
        
        this.getContentPane().add(monitorPanel,
                BorderLayout.CENTER);
        
        monitorPanel.setOpaque(true); //content panes must be opaque
        
        //Make sure we have nice window decorations.
        setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setPreferredSize(new Dimension(850,525));
        
        this.pack();
        
        this.setVisible(true);
        
        refreshTimer = new RefreshVOTimer(Settings.REFRESH_RATE,this);
    }
    
    
    /**
     * Update the tabbed panes on this panel with fresh information. 
     * This method will create a new MonitorPanel if none exist.  
     * Otherwise, it will call the MonitorPanel.refresh() with the
     * current VO.
     * 
     * @param userVO
     */
    public void refresh() {
        
        // Create and set up the content pane.
        if (monitorPanel == null) {
            monitorPanel = new MonitorPanel();
            this.getContentPane().add(monitorPanel,
                    BorderLayout.CENTER);
            monitorPanel.setOpaque(true); //content panes must be opaque
            
            Dimension currentSize = this.getSize();
            
            this.setPreferredSize(currentSize);
            
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    monitorPanel.refresh();
                }
            });
        } 
       
        
//        this.getContentPane().add(new MonitorPanel(userVO),
//                                 BorderLayout.CENTER);
        //Display the window.
        monitorPanel.tabbedPane.setSelectedIndex(currentTabIndex);
        
        this.pack();
        //this.setVisible(true);
    }
    
    public MonitorPanel getMonitorPanel() {
        return monitorPanel;
    }
    
    /**
     * Cleans up the MonitorVO frame and closes any
     * dialogs its various panels may have opened.
     */
    public void dispose() {
        setUpdate(false);
        getMonitorPanel().getJobPanel().closeDialoges();
        setUpdate(false);
        GridChem.oc.monitorWindow = null;
        super.dispose();
    }
    
    /**
     * Starts or stops the refresh timer on this table. If the timer
     * is started, it runs every Settings.REFRESH_RATE seconds.
     * 
     * @param update
     */
    public void setUpdate(boolean update) {
        if (update) {
            if (refreshTimer != null) {
                refreshTimer.restart(Settings.REFRESH_RATE);
            } else {
                refreshTimer = new RefreshVOTimer(Settings.REFRESH_RATE,this);
            }
        } else {
            if (refreshTimer != null) 
                refreshTimer.cancel();
        }
    }
 
//    public static VO getUserVO() {
//        // Init info for champion
//        LoadBean load1 = new LoadBean();
//        load1.setCpu(10);
//        load1.setDisk(15);
//        load1.setMemory(80);
//        load1.setQueue(60);
//        
//        ComputeBean hpc1 = new ComputeBean();
//        hpc1.setLoad(load1);
//        hpc1.setName("Champion");
//        
//        // Init info for supermike
//        LoadBean load2 = new LoadBean();
//        load2.setCpu(50);
//        load2.setDisk(25);
//        load2.setMemory(10);
//        load2.setQueue(5);
//        
//        ComputeBean hpc2 = new ComputeBean();
//        hpc2.setLoad(load2);
//        hpc2.setName("SuperMike");
//        
//        HashSet resources = new HashSet();
//        resources.add(hpc1);
//        resources.add(hpc2);
//        
//        UsageBean usage = new UsageBean(123456,10000,113456);
//        
//        ProjectBean project = new ProjectBean();
//        project.setName("Test Project");
//        
//        project.setUsage(usage);
////        HashSet projects = new HashSet();
////        projects.add(project);
////        
//        JobBean job = new JobBean();
//        job.setSoftwareName("Gaussian03");
//        job.setName("Test Job");
//        job.setSystemName("Champion");
//        job.setStartTime(new Date());
//        job.setStopTime(new Date());
//        job.setExperimentName("VO Test Suite");
//        job.setQueueName("default");
//        job.setProjectId(project.getId());
//        job.setId(new Long(5));
//        job.setLocalId("10643");
//        job.setStatus(JobStatusType.FINISHED);
//        
//        LinkedHashSet<JobBean> jobs = new LinkedHashSet<JobBean>();
//        jobs.add(job);
//        
//        UserBean user = new UserBean();
//        user.setJobs(jobs);
//        HashSet<ProjectBean> projects = new HashSet<ProjectBean>();
//        projects.add(project);
//        user.setProjects(projects);
//        //System.out.println(user.toString());
//        
//        return userVO;
//    }
    
    public static void main(String[] args) {
        String username;
        String password;
        AccessType projectType;
        String myproxyUsername = "";
        String myproxyPassword = "";
        
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        //      Establish session with the GMS_WS
        GridChem gc = new GridChem();
        
        Properties props = new Properties();
        
        Settings.WEBSERVICE = true;
        
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
        
        setMiddleware(props.getProperty("run.mode"));
        
//        try {
//            GMSSession.getInstance().establishSession();
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
        
        username = props.getProperty("gridchem.username");
        
        password = props.getProperty("gridchem.password");
        
        // Authenticate with the GMS_WS
        if (Settings.DEBUG)
            System.out.println("Logging " + username + " into the CCG.");
        try {
            GMS3.login(username,password,AccessType.COMMUNITY, new HashMap<String,String>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Settings.authenticated = true;
        Settings.authenticatedGridChem = true;
        Settings.gridchemusername = username;
        
        System.out.println("project type: " + (String)props.getProperty("access.type"));
//      Load the user's resources into the session.
        ProjectBean project = null;
        if(((String)props.getProperty("access.type"))
                .toUpperCase().equals(AccessType.COMMUNITY.toString())) {
            projectType = AccessType.COMMUNITY;
            System.out.println("selected a community project");
        } else {
            System.out.println("project type is " + (String)props.getProperty("access.type"));
            projectType = AccessType.EXTERNAL;
            myproxyUsername = props.getProperty("myproxy.username");
            myproxyPassword = props.getProperty("myproxy.password");
        }
        
        try {
            for(ProjectBean p: GMS3.getProjects()) {
                if(p.getType().equals(projectType)) {
                    project = p;
                }
            }
            
            GMS3.setCurrentProject(project);
            
            if (Settings.DEBUG)
                System.out.println("Successfully loaded user's VO");
            
            GridChem.user = GMS3.getProfile();
            
//            GridChem.userVO = GMS3.getUserVo();
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    GridChem.oc.monitorWindow = new MonitorVO();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GridChem.oc.monitorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
        
        
    }
    
    private static void setMiddleware(String mode) {
        if (mode == null) {
            // do nothing, use production infrastructure
        } else if (mode.toLowerCase().equals("devel")) {
            Invariants.wsGateway = "http://129.114.4.7:8080/wsrf/services/GMSService";
        } else if (mode.toLowerCase().equals("failover")) {
            Invariants.wsGateway = "http://129.114.4.7:8443/wsrf/services/GMSService";
        } else if (mode.toLowerCase().equals("local")) {
            Invariants.wsGateway = "http://127.0.0.1:8080/wsrf/services/GMSService";
        } else {
            // do nothing, use production infrastructure
        } 
    }
    
    public class MonitorPanel extends JPanel {
        private static final int JOB_TAB = 0;
        private static final int RESOURCE_TAB = 0;
        private static final int PROJECT_TAB = 0;
        
        boolean refreshing = false;
        
        private JobPanel jobPanel;
        private UsagePanel usagePanel;
        private ResourcePanel resourcePanel;
        
        JTabbedPane tabbedPane;

        public MonitorPanel() {
            super(new GridLayout(1, 1));
    
            tabbedPane = new JTabbedPane();
            //ImageIcon icon = createImageIcon("etc/ccglogo1.jpg");
    
            jobPanel = new JobPanel(GridChem.jobs);
            //JComponent panel1 = makeTextPanel("Job History");
            tabbedPane.addTab("Job History", null, jobPanel,
                              "Show historical job information.");
            tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
    
            resourcePanel = new ResourcePanel(GridChem.systems);
            //JComponent panel2 = makeTextPanel("Resource Status");
            tabbedPane.addTab("Resource Status", null, resourcePanel,
                              "Show current status of HPC resources on the CCG");
            tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
    
            //usagePanel = new UsagePanel(GridChem.projects);
            //tabbedPane.addTab("Project Usage", null, usagePanel,
            //                  "Show current usage information.");
            //usagePanel.setPreferredSize(new Dimension(410, 50));
            //tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
            
            tabbedPane.addChangeListener(new TabChangeListener());
//            tabbedPane.addMouseListener(new TabMouseAdapter());
            
            //Add the tabbed pane to this panel.
            add(tabbedPane);
            
            //Uncomment the following line to use scrolling tabs.
            //tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
            
        }
        
        public boolean hasSearchResults() {
            return jobPanel.isUpdatedWithSearchResults();
        }
    
        protected JComponent makeTextPanel(String text) {
            JPanel panel = new JPanel(false);
            JLabel filler = new JLabel(text);
            filler.setHorizontalAlignment(JLabel.CENTER);
            panel.setLayout(new GridLayout(1, 1));
            panel.add(filler);
            return panel;
        }
    
        /** Returns an ImageIcon, or null if the path was invalid. */
        protected ImageIcon createImageIcon(String path) {
            java.net.URL imgURL = MonitorVO.class.getResource(Env.getGridChemLogoLocation());
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        }
    
        /**
         * @return the jobPanel
         */
        public JobPanel getJobPanel() {
            return jobPanel;
        }

        /**
         * @param jobPanel the jobPanel to set
         */
        public void setJobPanel(JobPanel jobPanel) {
            this.jobPanel = jobPanel;
        }

        /**
         * @return the resourceStatusPanel
         */
        public ResourcePanel getResourcePanel() {
            return resourcePanel;
        }

        /**
         * @param resourceStatusPanel the resourceStatusPanel to set
         */
        public void setResourcePanel(ResourcePanel resourcePanel) {
            this.resourcePanel = resourcePanel;
        }

        /**
         * @return the usagePanel
         */
        public UsagePanel getUsagePanel() {
            return usagePanel;
        }

        /**
         * @param usagePanel the usagePanel to set
         */
        public void setUsagePanel(UsagePanel usagePanel) {
            this.usagePanel = usagePanel;
        }
        
        
        public int getFocusedPanel() {
            return tabbedPane.getSelectedIndex();
        }
        
        public void setFocusedPanel(int index) {
            Component comp = tabbedPane.getComponent(index);
            tabbedPane.setSelectedIndex(index);
            tabbedPane.setSelectedComponent(comp);
        }
        
        private void setSelectedTab(int index) {
            if (index > -1) {
                currentTabIndex = index;
//                System.out.println("Currently selected tab " + index);
            } else {
//                System.out.println("No tab selected");
            }
        }
        
        /**
         * This is the method used to refresh each of the monitoring panels
         * tied to the tabbed pane on the MonitorVO window.  This calls in this
         * method are order dependent.  
         */
        private void refresh() {
//          The jobPanel has a no-arg refresh method because
//          it directly queries the GMS_WS.  The result of that query is stored
//          in the GridChem.userVO variable which is passed to the remaining 
//          objects to reuse.  The usagePanel has a no-arg refresh method because
//          project usage isn't updated anywhere else in the client, so it 
//          has to be updated here by default.
            refreshing = true;
            jobPanel.refresh();
            resourcePanel.refresh();
            usagePanel.refresh();
            refreshing = false;
        }
        
        class TabChangeListener implements ChangeListener {

            public void stateChanged(ChangeEvent e) {
                if (!refreshing) {
                    setSelectedTab(((JTabbedPane)e.getSource()).getSelectedIndex());
                } else {
                    tabbedPane.setSelectedIndex(currentTabIndex);
                    tabbedPane.getSelectedComponent().repaint();
                }
            }
            
        }
        
        class TabMouseAdapter extends MouseAdapter {
            public void mousePressed(MouseEvent e) {
                setSelectedTab(((JTabbedPane)e.getSource()).getSelectedIndex());
            }
        }
    }    
}
