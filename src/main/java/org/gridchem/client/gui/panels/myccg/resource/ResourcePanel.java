package org.gridchem.client.gui.panels.myccg.resource;
/* 
 *  Copyright 1999-2002 Matthew Robinson and Pavel Vorobiev. 
 *  All Rights Reserved. 
 * 
 *  =================================================== 
 *  This program contains code from the book "Swing" 
 *  2nd Edition by Matthew Robinson and Pavel Vorobiev 
 *  http://www.spindoczine.com/sbe 
 *  =================================================== 
 * 
 *  The above paragraph must be included in full, unmodified 
 *  and completely intact in the beginning of any source code 
 *  file that references, copies or uses (in any way, shape 
 *  or form) code contained in this file. 
 */ 

//package org.gridchem.client.gui.panels.myccg.resource;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.gridchem.client.GridChem;
import org.gridchem.client.SwingWorker;
import org.gridchem.client.Trace;
import org.gridchem.client.common.Settings;
import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.exceptions.GMSException;
import org.gridchem.client.exceptions.SessionException;
import org.gridchem.client.gui.jobsubmission.commands.GETHARDWARECommand;
import org.gridchem.client.gui.jobsubmission.commands.JobCommand;
import org.gridchem.client.gui.login.LoginDialog;
import org.gridchem.client.gui.panels.CancelCommandPrompt;
import org.gridchem.client.gui.panels.myccg.MonitorVO;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.util.Env;
import org.gridchem.service.beans.ComputeBean;
import org.gridchem.service.beans.LoadBean;
import org.gridchem.service.beans.QueueBean;
import org.gridchem.service.beans.SiteBean;
import org.gridchem.service.beans.SoftwareBean;
import org.gridchem.service.exceptions.PermissionException;
import org.gridchem.service.model.enumeration.ResourceStatusType;

public class ResourcePanel extends JPanel 
implements StatusListener {

	protected JTree  m_tree = null;
	protected DefaultTreeModel m_model = null;
	protected JTextField m_display;
    protected JSplitPane leftSplitPane;
    protected JSplitPane rightSplitPane;
    
    protected SiteInfoPanel siteInfoPanel = null;
    protected HPCChartPanel chartPanel = null;
    protected ResourceInfoPanel resourceInfoPanel = null;
    
    protected Hashtable<String,HashSet<ComputeBean>> siteTable = null;

    protected CancelCommandPrompt progressCancelPrompt;
    
	public ResourcePanel(List<ComputeBean> hpcs) {
		
	    super();
        
        Dimension minimumLowerPanelSize = new Dimension(200,175);
        Dimension minimumUpperPanelSize = new Dimension(200,300);
        Dimension preferredPanelSize = new Dimension(700,475);
        
        siteTable = createSiteTableData(hpcs);
        
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(
                "CCG");
        
        for(String siteName: siteTable.keySet()){
            
            DefaultMutableTreeNode newSiteNode = new DefaultMutableTreeNode(
                    new SiteNode(siteTable.get(siteName).iterator().next().getSite()));
            
            for(ComputeBean hpc: siteTable.get(siteName)) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(
                        new HpcNode(hpc));
                newSiteNode.add(node);
            }
            
            top.add(newSiteNode);
        }
        
        // create the resource tree
        m_model = new DefaultTreeModel(top);
		m_tree = new JTree(m_model);

        TreeCellRenderer renderer = new
			IconCellRenderer();
		m_tree.setCellRenderer(renderer);
		m_tree.setShowsRootHandles(true);
		m_tree.setEditable(false);
        m_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		m_tree.addTreeSelectionListener(new
			SiteTreeSelectionListener());
		// place the resource tree and site info panel in scroll panels
        // and place the objects in a JSplit pane to make the left side
        // of our display.
		JScrollPane treeScrollPane = new JScrollPane();
        treeScrollPane.getViewport().add(m_tree);
        treeScrollPane.setPreferredSize(minimumUpperPanelSize);
        
        siteInfoPanel = new SiteInfoPanel();
        JScrollPane siteInfoScrollPane = new JScrollPane();
        siteInfoScrollPane.getViewport().add(siteInfoPanel);
        siteInfoScrollPane.setPreferredSize(minimumLowerPanelSize);
        siteInfoScrollPane.setAutoscrolls(true);
        
        
        leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,treeScrollPane,siteInfoPanel);
        leftSplitPane.setDividerSize(6);
        leftSplitPane.setContinuousLayout(false);
        leftSplitPane.setOneTouchExpandable(true);
        leftSplitPane.setDividerLocation((int)minimumUpperPanelSize.getHeight());
        
        // place the chart panel and resource info panels in scroll panels
        // and place the objects in a JSplit pane to make the right side
        // of our display.
        chartPanel = new HPCChartPanel(this);
        JScrollPane chartScrollPane = new JScrollPane();
        chartScrollPane.getViewport().add(chartPanel);
        chartScrollPane.setWheelScrollingEnabled(true);
        chartScrollPane.setPreferredSize(minimumUpperPanelSize);
        
        resourceInfoPanel = new ResourceInfoPanel();
        JScrollPane resourceInfoScrollPane = new JScrollPane();
        resourceInfoScrollPane.getViewport().add(resourceInfoPanel);
        resourceInfoScrollPane.setWheelScrollingEnabled(true);
        resourceInfoScrollPane.setPreferredSize(minimumLowerPanelSize);
        
        rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,chartScrollPane,resourceInfoScrollPane);
        rightSplitPane.setDividerSize(6);
        rightSplitPane.setContinuousLayout(false);
        rightSplitPane.setOneTouchExpandable(true);
        rightSplitPane.setDividerLocation((int)minimumUpperPanelSize.getHeight());
        
        // now add both split panes to the main left/right split pane
        JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftSplitPane,rightSplitPane);
        centerSplitPane.setDividerSize(6);
        centerSplitPane.setResizeWeight(0.2);
        centerSplitPane.setContinuousLayout(false);
        centerSplitPane.setOneTouchExpandable(true);
        centerSplitPane.setDividerLocation((int)minimumUpperPanelSize.getWidth());
        centerSplitPane.setPreferredSize(preferredPanelSize);
        
        // layout the final display
        setLayout(new GridLayout(1,2));
        add(centerSplitPane, BorderLayout.CENTER); 
        
        GETHARDWARECommand command = new GETHARDWARECommand(this);
        command.getArguments().put("project.id", GridChem.project.getId());
        statusChanged(new StatusEvent(command,Status.START));
        
	}
    
    private Hashtable<String,HashSet<ComputeBean>> createSiteTableData(List<ComputeBean> hpcs) {
        Hashtable<String,HashSet<ComputeBean>> siteTable = new Hashtable<String,HashSet<ComputeBean>>();
        
        
        for (ComputeBean hpc: hpcs) {
            HashSet<ComputeBean> siteResources = siteTable.get(hpc.getSite().getAcronym());
            
            if (siteResources == null || siteResources.size() == 0) {
                
                siteResources = new HashSet<ComputeBean>();
                siteResources.add(hpc);
                siteTable.put(hpc.getSite().getAcronym(),siteResources);
                
            } else {
                
                siteResources.add(hpc);
                siteTable.remove(hpc.getSite().getAcronym());
                siteTable.put(hpc.getSite().getAcronym(),siteResources);
                
            }
            
        }
        
        return siteTable;
    }

	public static void main(String argv[]) {
	    List<ComputeBean> hpcs = generateData();
        JFrame frame = new JFrame();
        frame.getContentPane().add(new ResourcePanel(hpcs));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
    
    private static List<ComputeBean> generateData() {
        // create a couple dummy sites
        SiteBean[] sites = new SiteBean[3];
        
        sites[0] = new SiteBean();
        sites[0].setName("Nation Center for Supercomputing Applications");
        sites[0].setAcronym("NCSA");
        sites[0].setDescription("Now the joy of my world is in Zion");
        
        sites[1] = new SiteBean();
        sites[0].setName("Texas Advanced Computing Center");
        sites[0].setAcronym("TACC");
        sites[0].setDescription("Now the joy of my world is in Zion");
        
        sites[2] = new SiteBean();
        sites[2].setName("Center for Computational Science");
        sites[2].setAcronym("CCS");
        sites[3].setDescription("Permission to buzz the tower");
        
        // create a few dummy resources for each site
        List<ComputeBean> hpcs = new ArrayList<ComputeBean>();
        
        ComputeBean hpc = new ComputeBean();
        hpc.setName("alpha");
        hpc.setHostname("alpha.test.org");
        hpc.setIpAddress("111.111.111.111");
        hpc.setSystem("Alpha cluster");
        hpc.setLoad(new LoadBean(5000,100, 50000, 200, 250, 100, 10));
        hpc.setSite(sites[0]);
        hpc.setSoftware(new ArrayList<SoftwareBean>());
        hpc.setPeakPerformance(new Double(16).longValue());
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UP);
        hpc.setQueues(new ArrayList<QueueBean>());
        hpc.setSite(sites[0]);
        hpcs.add(hpc);
        
        hpc = new ComputeBean();
        hpc.setName("beta");
        hpc.setHostname("beta.test.org");
        hpc.setIpAddress("222.222.222.222");
        hpc.setSystem("Beta cluster");
        hpc.setLoad(new LoadBean(61,65,70,75,80,85,90));
        hpc.setSite(sites[0]);
        hpc.setSoftware(new ArrayList<SoftwareBean>());
        hpc.setPeakPerformance(new Double(17).longValue());
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UP);
        hpc.setQueues(new ArrayList<QueueBean>());
        hpc.setSite(sites[0]);
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UNKNOWN);
        hpc.setQueues(new ArrayList<QueueBean>());
        hpcs.add(hpc);
        
        hpc = new ComputeBean();
        hpc.setName("delta");
        hpc.setHostname("delta.test.org");
        hpc.setIpAddress("333.333.333.333");
        hpc.setSystem("Delta cluster");
        hpc.setLoad(new LoadBean(61,65,70,75,80,85,90));
        hpc.setSite(sites[0]);
        hpc.setSoftware(new ArrayList<SoftwareBean>());
        hpc.setPeakPerformance(new Double(17).longValue());
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UP);
        hpc.setQueues(new ArrayList<QueueBean>());
        hpc.setSite(sites[1]);
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UNKNOWN);
        hpc.setQueues(new ArrayList<QueueBean>());
        hpcs.add(hpc);
        
        
        hpc = new ComputeBean();
        hpc.setName("hoffa");
        hpc.setHostname("hoffa.test.org");
        hpc.setIpAddress("444.444.444.444");
        hpc.setSystem("Hoffa cluster");
        hpc.setLoad(new LoadBean(51,55,50,55,50,55,50));
        hpc.setSite(sites[2]);
        hpc.setSoftware(new ArrayList<SoftwareBean>());
        hpc.setPeakPerformance(new Double(17).longValue());
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UP);
        hpc.setQueues(new ArrayList<QueueBean>());
        hpc.setSite(sites[0]);
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UNKNOWN);
        hpc.setQueues(new ArrayList<QueueBean>());
        hpcs.add(hpc);
        
        hpc = new ComputeBean();
        hpc.setName("brasco");
        hpc.setHostname("brasco.test.org");
        hpc.setIpAddress("666.666.666.666");
        hpc.setSystem("Delta cluster");
        hpc.setLoad(new LoadBean(61,65,60,65,60,65,60));
        hpc.setSite(sites[0]);
        hpc.setSoftware(new ArrayList<SoftwareBean>());
        hpc.setPeakPerformance(new Double(17).longValue());
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UP);
        hpc.setQueues(new ArrayList<QueueBean>());
        hpc.setSite(sites[0]);
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UNKNOWN);
        hpc.setQueues(new ArrayList<QueueBean>());
        hpcs.add(hpc);
        
//        HashSet<ProjectBean> projects = new HashSet<ProjectBean>();
//        ProjectBean project = new ProjectBean();
//        project.setResources(hpcs);
//        project.setCurrent(true);
//        projects.add(project);
//        
//        UserBean user = new UserBean();
//        user.setProjects(projects);
//        user.setFirstName("Mike");
//        user.setLastName("Sula");
//        VO vo = new VO();
//        vo.setUser(user);
        
        return hpcs;
    }
    
    DefaultMutableTreeNode getTreeNode(TreePath path) {
        // if no path is selected, select the parent path.
        if (path == null)
            return (DefaultMutableTreeNode)m_tree.getPathForRow(0).getLastPathComponent();
        
        return (DefaultMutableTreeNode)path.getLastPathComponent();
    }
    
    public void refresh() {
        SiteBean site = null;
        ComputeBean hpc = null;
        
        // recored the previous values
        DefaultMutableTreeNode node = getTreeNode(m_tree.getSelectionPath());
        if (node.getUserObject() instanceof SiteNode) {
            site = ((SiteNode)node.getUserObject()).getSite();
        } else if (node.getUserObject() instanceof HpcNode) {
            hpc = ((HpcNode)node.getUserObject()).getResource();
            site = hpc.getSite();
        } else if (node.getUserObject() instanceof String) {
            // The root tree node, CCG, was selected  
        } else {}
        
        // rebuild the site table and add the new model to the table. everything else
        // will refresh on the next click.
        siteTable = createSiteTableData(GridChem.getMachineList());
        
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(
                "CCG");
        
        for(String s: siteTable.keySet()){
            
            DefaultMutableTreeNode newSiteNode = new DefaultMutableTreeNode(
                    new SiteNode(siteTable.get(s).iterator().next().getSite()));
            
            for(ComputeBean hw: siteTable.get(s)) {
                DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(
                        new HpcNode(hw));
                newSiteNode.add(treeNode);
            }
            
            top.add(newSiteNode);
        }
        
        m_tree.setModel(m_model = new DefaultTreeModel(top));
        
        // reselect the proper node in the tree to refesh the panel
        if (site == null || siteTable.get(site.getAcronym()) == null) {
            siteInfoPanel.clearSite();
            chartPanel.clearCharts();
            resourceInfoPanel.clearResource();
        } else {
            boolean siteFound = false;
            for(int i=0; i < ((DefaultMutableTreeNode)m_model.getRoot()).getChildCount(); i++) {
                DefaultMutableTreeNode treeNode = 
                    (DefaultMutableTreeNode)((DefaultMutableTreeNode)m_model.getRoot()).getChildAt(i);
                SiteNode siteNode = 
                    (SiteNode)treeNode.getUserObject();
                
                if (site.getAcronym().equals(siteNode.getSite().getAcronym())) {
                    siteFound = true;
                    if (hpc == null) {
                        if (siteTable.get(siteNode.getSite()) == null) {
                            chartPanel.clearCharts();
                            resourceInfoPanel.clearResource();
                        } else {
                            chartPanel.setResources(siteTable.get(siteNode.getSite()));
                            resourceInfoPanel.clearResource();
                        }
                    } else {
                        boolean hpcFound = false;
                        for (int j=0; j<treeNode.getChildCount(); j++) {
                            HpcNode hpcNode = 
                                (HpcNode)((DefaultMutableTreeNode)treeNode.getChildAt(j)).getUserObject();
                            if (hpcNode.getResource().getName().equals(hpc.getName())) {
                                m_tree.setSelectionPath(new TreePath(
                                        ((DefaultMutableTreeNode)treeNode.getChildAt(j)).getPath()));
                                hpcFound = true;
                                System.out.println("Found resource: " + hpcNode.getResource().getName());
                                break;
                            }
                        }
                        
                        if (!hpcFound) {
                            chartPanel.setResources(siteTable.get(siteNode.getSite().getAcronym()));
                            resourceInfoPanel.clearResource();
                            JOptionPane.showMessageDialog(
                                this,
                                "Resource " + hpc.getName() + " is no longer available.\n" + 
                                "Please check the announcements for details.",
                                "Resource Management Error", JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                    break;
                }
            }
            if (!siteFound) {
                siteInfoPanel.clearSite();
                chartPanel.clearCharts();
                resourceInfoPanel.clearResource();
                JOptionPane.showMessageDialog(
                    this,
                    "Site " + site.getName() + " is no longer available.\n" + 
                    "Please check the announcements for details.",
                    "Site Management Error", JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    /**
     * Create a progress panel that allows the current task to be killed if the 
     * user clicks on the cancel button.
     * 
     * @param title
     * @param labelText
     * @param worker
     */
    private void startWaiting(String title, String labelText, SwingWorker worker) {
        progressCancelPrompt = 
            new CancelCommandPrompt(this,title,labelText,-1,worker);
          
    }
    
    /**
     * Updates the value of a progress panel
     * 
     * @param message
     * @param value
     */
    private void updateProgress(int value) {
        progressCancelPrompt.updateStatus();
    }
    
    /**
     * Updates the message of a progress panel
     * 
     * @param message
     * @param value
     */
    private void updateProgress(String message) {
        progressCancelPrompt.updateStatus(message);
    }
    
    /**
     * Updates both the message and value of a progress panel
     * 
     * @param message
     * @param value
     */
    private void updateProgress(String message,int value) {
        progressCancelPrompt.updateStatus();
        progressCancelPrompt.updateStatus(message);
    }
    
    /**
     * Remove the progress panel.
     */
    private void stopWaiting() {
        if (progressCancelPrompt != null) {
            progressCancelPrompt.finished();
            progressCancelPrompt = null;
        }
    }
    
    public void statusChanged(StatusEvent event) {
        Trace.entry();
        Status status = event.getStatus();
        System.out.println("Status changed to: " + status.name());
        System.out.println("StatusListener is: " + event.getSource().getClass().getName());
        
        final JobCommand command = (JobCommand) event.getSource();
        System.out.println("stats=" + status.name() + ", type=" + command.getClass());
        try {
            //What to do if things complete successfully.
            if (status.equals(Status.START)) {
                if (Settings.VERBOSE)
                    System.out.println("Starting search command");
                
                //GridChem.oc.monitorWindow.setUpdate(false);
                SwingWorker worker = new SwingWorker() {
                    
                    public Object construct() {
                        try {
                            
                            command.execute();
                            
                        } catch (SessionException e) {
        
                            GridChem.oc.monitorWindow.setUpdate(false);
                            
                            int viewLog = JOptionPane.showConfirmDialog(
                                    GridChem.oc.monitorWindow,
                                    "Your session has expired. Would\n" + 
                                    "you like to reauthenticate to the CCG?",
                                    "Session Timeout",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.ERROR_MESSAGE
                            );
                                
                            if (viewLog == 0) {
                                GridChem.appendMessage("Resetting user authentication...");
                                LoginDialog.clearLogin();
                                GridChem.appendMessage("Complete\n");
                                GridChem.oc.updateAuthenticatedStatus();
                                GridChem.oc.doAuthentication();
                            }
                        } catch (GMSException e) {
                            
                        } catch (ConnectException e) {
                            if(MonitorVO.warningDialog == null) {
                                updateProgress("A connection error has occurred.\n" + 
                                        "Please check your connection\nand authenticate again.");
                            } else {
                                updateProgress("A connection error has occurred.\n" + 
                                        "Please check your connection\nand authenticate again.");
                            }
                        } catch (Exception e) {
                            updateProgress("An unknown exception occurred");
                            e.printStackTrace();
                        }
                        
                        return command;
                    }
                    public void finished() {
                        stopWaiting();
                    }
                };
                
                if (progressCancelPrompt == null) {
                    
                    String showGuiFlag = (String)command.getArguments().get("show.progress");
                    
                    if (showGuiFlag != null) {
                        if(showGuiFlag.equals("true")) {
                            startWaiting("Progress...",
                                    "Retrieving updated resource info...",
                                    worker);
                        } // if the flag is false, don't show the progress bar
                    } else {
                        startWaiting("Progress...", 
                                "Retrieving updated resource info...",
                                worker);
                    }
                } else {
                    updateProgress("Retrieving updated resource info...");
                }
                    
                    worker.start();
                
            } else if (status.equals(Status.COMPLETED)) {
                if (Settings.VERBOSE)
                    System.out.println(command.getCommand() + " Command Completed");
                
                if (command.getCommand().equals(JobCommand.GETHARDWARE)) {
                    Trace.note("Finished UPDATE, refreshing user's VO");
                    
                    GridChem.systems = ((GETHARDWARECommand)command).getOutput();
                    
                    refresh();
                    
                }
            } else {
                if (Settings.VERBOSE)
                    System.out.println(command.getCommand() + " Command Failed!");
                
                if (command.getCommand().equals(JobCommand.UPDATE)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to update job listing.",
                            "Job Management Error", JOptionPane.ERROR_MESSAGE
                        );
    //                    pm.setProgress(100);
                      
                }
            }
        } catch (PermissionException e) {
            JOptionPane.showMessageDialog(this, "A session error has occurred.\n" + 
                    "Please check your connection\nand authenticate again.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            LoginDialog.clearLogin();
        } 
    }
    
    

	class SiteTreeSelectionListener
		implements TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent e) {
            
            final Object treeNode = getTreeNode(e.getPath()).getUserObject();
            
            Thread runner = new Thread() {

                public void run() {
                
                    Runnable runnable = new Runnable() {
                        public void run() {
        
                            // if it's a site, then update the site info and clear the resource 
                            // info panel and display summary charts of all the resources on the site.
                            if (treeNode instanceof SiteNode ) {
                               if (siteInfoPanel.getSite() == null || 
                                       !((SiteNode)treeNode).getSite().getAcronym()
                                       .equals(siteInfoPanel.getSite().getAcronym())) {
                                   siteInfoPanel.setSite(((SiteNode)treeNode).getSite());
                               }
                               resourceInfoPanel.clearResource();
                               chartPanel.setResources(siteTable.get(((SiteNode)treeNode).getSite().getAcronym()));
                               
                            // if it's a resource, then show the site info, resoruce info, and display
                            // all the load charts for that resource.
                            } else if (treeNode instanceof HpcNode) {
                                chartPanel.setResource(((HpcNode)treeNode).getResource());
                                if (siteInfoPanel.getSite() == null || 
                                        !((HpcNode)treeNode).getResource().getSite().getAcronym()
                                        .equals(siteInfoPanel.getSite().getAcronym())) {
                                    siteInfoPanel.setSite(((HpcNode)treeNode).getResource().getSite());
                                }
                                resourceInfoPanel.setResource(((HpcNode)treeNode).getResource());
                                
                            // otherwise it's the ccg, then show the ccg info, no resource info, and 
                            // all the load charts for the entire vo
                            } else if (treeNode instanceof String) {
                                resourceInfoPanel.clearResource();
                                HashSet<ComputeBean> resources = new HashSet<ComputeBean>();
                                for(String siteName: siteTable.keySet()) {
                                    resources.addAll(siteTable.get(siteName));
                                }
                                chartPanel.setResources(resources);
                                siteInfoPanel.clearSite(); 
                                
                            } else {
                                siteInfoPanel.clearSite(); 
                                resourceInfoPanel.clearResource();
                                chartPanel.clearCharts();
                                
                            }
        
                        }
        
                    };
        
                    SwingUtilities.invokeLater(runnable);
                    
                }
            };
            runner.start();
        }
    }
}

class IconCellRenderer extends DefaultTreeCellRenderer {

    public IconCellRenderer() {
    }

    public Component getTreeCellRendererComponent(JTree tree,
        Object value, boolean sel, boolean expanded, boolean leaf,
        int row, boolean hasFocus) {
    
        // Invoke default implementation
        Component result = super.getTreeCellRendererComponent(tree,
            value, sel, expanded, leaf, row, hasFocus);
        
        DefaultMutableTreeNode node =
            (DefaultMutableTreeNode)value;
        
        Object obj = node.getUserObject();
        
        setText(obj.toString());
        
    
        if (obj instanceof HpcNode) {
            setIcon(((HpcNode)obj).m_symbol);
        } else {
            setIcon(HpcNode.ICON_UP);
        }
        return result;
    
    }
    
    
}

class IconData {
    public ImageIcon m_icon;
    public Object m_data;
    
    public IconData(ImageIcon icon, Object data) {
        m_icon = icon;
        m_data = data;
    }

    public String toString() {
        return m_data.toString();
    }
}

class HpcNode {
    
    public static ImageIcon ICON_UP = new ImageIcon(Env.getImagesDir() +"/icons/green_light..jpg");
    public static ImageIcon ICON_DOWN = new ImageIcon(Env.getImagesDir() +"/icons/red_light.jpg");
    public static ImageIcon ICON_BLANK = new ImageIcon(Env.getImagesDir() +"/icons/blank.jpg");
    
    protected ComputeBean m_hpc;
    
    public ImageIcon m_symbol;
    
    public HpcNode(ComputeBean hpc) {
        m_hpc = hpc;
        if (hpc.getStatus().equals(ResourceStatusType.DOWN) ||
                hpc.getStatus().equals(ResourceStatusType.OFFLINE)) {
            m_symbol = ICON_DOWN;
        } else {
            m_symbol = ICON_UP;
        }
    }
    
    public ComputeBean getResource() {
        return m_hpc;
    }
    
    public String toString() {
        return m_hpc.getName();
    }
}

class SiteNode {
	protected SiteBean m_site;

	public SiteNode(SiteBean site) {
		m_site = site;
	}

	public SiteBean getSite() {
		return m_site;
	}

	public String toString() {
		return m_site.getAcronym();
	}
}

