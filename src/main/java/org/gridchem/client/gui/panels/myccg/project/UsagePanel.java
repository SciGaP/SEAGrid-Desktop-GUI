/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Feb 27, 2007
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

package org.gridchem.client.gui.panels.myccg.project;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
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
import org.gridchem.client.gui.charts.UsageChart;
import org.gridchem.client.gui.charts.UsageChart.ChartType;
import org.gridchem.client.gui.jobsubmission.commands.GETUSAGECommand;
import org.gridchem.client.gui.jobsubmission.commands.JobCommand;
import org.gridchem.client.gui.login.LoginDialog;
import org.gridchem.client.gui.panels.CancelCommandPrompt;
import org.gridchem.client.gui.panels.myccg.MonitorVO;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.util.Env;
import org.gridchem.client.util.GMS3;
import org.gridchem.service.beans.Address;
import org.gridchem.service.beans.CollaboratorBean;
import org.gridchem.service.beans.ComputeBean;
import org.gridchem.service.beans.LoadBean;
import org.gridchem.service.beans.ProjectBean;
import org.gridchem.service.beans.QueueBean;
import org.gridchem.service.beans.SiteBean;
import org.gridchem.service.beans.SoftwareBean;
import org.gridchem.service.beans.UsageBean;
import org.gridchem.service.beans.UserBean;
import org.gridchem.service.exceptions.PermissionException;
import org.gridchem.service.model.enumeration.AccessType;
import org.gridchem.service.model.enumeration.ProjectStatusType;
import org.gridchem.service.model.enumeration.ResourceStatusType;
import org.gridchem.service.model.enumeration.ResourceType;
import org.gridchem.service.model.enumeration.UserClassificationType;
import org.gridchem.service.model.enumeration.UserPermissionType;

/**
 * Panel displaying usage information on the user's projects.  This panel is embedded
 * under the projects tab of the MyCCG panel.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class UsagePanel extends JPanel 
implements StatusListener {

    Dimension minimumLowerPanelSize = new Dimension(200,175);
    Dimension minimumUpperPanelSize = new Dimension(200,300);
    Dimension preferredPanelSize = new Dimension(700,475);
    
    protected Hashtable<ProjectBean,List<CollaboratorBean>> projectTable;
    
    protected JTree m_tree = null;
    protected DefaultTreeModel m_model = null;
    // container control buttons
    private JButton reloadButton;
    private JButton cancelButton;
    private JComboBox viewComboBox;
    
    private UsageChart usageChart;
    
    private static ChartType CURRENT_CHARTTYPE = ChartType.PROJECT;
    
    private CancelCommandPrompt progressCancelPrompt;
    
    public UsagePanel(List<ProjectBean> projects) {
        
        setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.gridheight = 2;
        c.gridx = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        add(createProjectTreePanel(projects),c);
        
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridheight = 1;
        c.gridy = 0;
        c.gridx = 1;
        c.anchor = GridBagConstraints.NORTHEAST;
        add(createButtonPanel(),c);
      
        usageChart = new UsageChart(projectTable);
        
        try {
            c.gridy = 1;
            c.anchor = GridBagConstraints.SOUTHEAST;
            add(usageChart,c);
            
            setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setMinimumSize(new Dimension(565,358));
        
//        GETUSAGECommand command = new GETUSAGECommand(this);
//        statusChanged(new StatusEvent(command,Status.START));
        
    }
    
    protected Component createProjectTreePanel(List<ProjectBean> projectList) {
        
        projectTable = createProjectUsageTableData(projectList);
        
        // here is the root node
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("CCG");
        
        // iterate through the project collab table parsing out each access type in turn
        for (AccessType projectType: AccessType.values()){
            
            DefaultMutableTreeNode typeNode = new DefaultMutableTreeNode(projectType);
            
            for (ProjectBean p: projectTable.keySet()) {
                
            	// if the project type matches the iterative type, add it
            	if (p.getType().equals(projectType)) {
	                
            		DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(
	                        new ProjectNode(p));
	                
	                for(CollaboratorBean collab: projectTable.get(p)) {
	                
	                    DefaultMutableTreeNode newUserNode = new DefaultMutableTreeNode(
	                            new UserNode(collab));
	                    
	                    projectNode.add(newUserNode);
	                }
	            
	                typeNode.add(projectNode);
            	}
            }
            
            rootNode.add(typeNode);
        }
        
//      create the resource tree
        m_model = new DefaultTreeModel(rootNode);
        m_tree = new JTree(m_model);
        
        TreeCellRenderer renderer = new
            IconCellRenderer();
        m_tree.setCellRenderer(renderer);
        m_tree.setShowsRootHandles(true);
        m_tree.setEditable(false);
        m_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        m_tree.addTreeSelectionListener(new
            ProjectTreeSelectionListener());
        
        // place the resource tree and site info panel in scroll panels
        // and place the objects in a JSplit pane to make the left side
        // of our display.
        JScrollPane treeScrollPane = new JScrollPane();
        treeScrollPane.getViewport().add(m_tree);
        treeScrollPane.setPreferredSize(minimumUpperPanelSize);
        
        // add tool tips to the tree panel
        ToolTipManager.sharedInstance().registerComponent(m_tree);

        // make the mouseover tool tips to appear immediately
        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setReshowDelay(50);
        
        // make the tool tips appear until the mouse is removed
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        
        return treeScrollPane;
        
        
    }
    
//    private List<CollaboratorBean> sortCollaborators(List<CollaboratorBean> collabs) {
//        List<CollaboratorBean> sortedSet = new ArrayList<CollaboratorBean>();
//        
//        // set the ordering pi then admin then user
//        UserPermissionType[] typeVals = {UserPermissionType.PI,UserPermissionType.ADMIN,UserPermissionType.USER};
//        
//        for(int i=0;i<typeVals.length;i++){
//            for(CollaboratorBean collab: collabs) {
//                if (collab.get.getAdminType().equals(typeVals[i])) {
//                    sortedSet.add(c);
//                }
//            }
//        }
//        
//        return sortedSet;
//    }
    
    private void updateUsage() {
        GETUSAGECommand command = 
            new GETUSAGECommand(this);
        
        statusChanged(new StatusEvent(command,Status.START));
    }
    
    private Component createButtonPanel() {
        Dimension buttonSize = new Dimension(150,100);
        
        reloadButton = new JButton("Refresh");
        cancelButton = new JButton("Close");
        viewComboBox = new JComboBox(new String[]{"User"});
        
        // define an action listener for the buttons
        ActionListener buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (event.getSource() == reloadButton) {
                    // refresh the usage with a call to the GMS
                    updateUsage();
                } else {
                    // dispose of entire MyCCG window
                    GridChem.oc.monitorWindow.dispose();
                }
            }
            
        };
        
        // add all event listeners here 
        reloadButton.addActionListener(buttonListener);
        cancelButton.addActionListener(buttonListener);
        
        // add tool tip texts to buttons
        reloadButton.setToolTipText(
                "<html>Refresh the projects in the current window with the latest" + 
                "<br>usage infomation. All usage information is subject to, at " + 
                "<br>worst, a 60 second lag.</html>");
        
        cancelButton.setToolTipText("Close this window.");
        
        // lay them out in the button panel
        JPanel buttonBoxPane = new JPanel();
        JPanel buttonBox = new JPanel();
        
        buttonBox.setLayout(new GridLayout(3,1,5,5));
        buttonBox.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        
        Border eBorder1 = BorderFactory.createEmptyBorder(0,10,0,0);
        
        buttonBoxPane.setBorder(eBorder1);
        buttonBoxPane.setLayout(new BoxLayout(buttonBoxPane,BoxLayout.Y_AXIS));
        buttonBoxPane.setPreferredSize(buttonSize);
        buttonBoxPane.add(buttonBox);
        buttonBox.add(reloadButton);
        buttonBox.add(cancelButton);
        buttonBox.add(viewComboBox);
        
        return buttonBoxPane;
    }
    
    private Hashtable<ProjectBean,List<CollaboratorBean>> createProjectUsageTableData(List<ProjectBean> projects) {
        
        Hashtable<ProjectBean,List<CollaboratorBean>> projectTable = new Hashtable<ProjectBean,List<CollaboratorBean>>();
        System.out.println("There are " + projects.size() + " projects for the user.");
        
        /*for (ProjectBean p: projects) {
            
            List<CollaboratorBean> collabs = GMS3.getCollaborators(p.getId());
            
            // if not, then add the project type and the project to the table
            projectTable.put(p, collabs);
        }*/
        
        return projectTable;
    }
    
    
    public static void main(String[] argv) {
        ArrayList<ProjectBean> projects = generateData();
        JFrame frame = new JFrame();
        frame.getContentPane().add(new UsagePanel(projects));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700,500));
        frame.pack();
        frame.setVisible(true);
        
    }
    
    private static ArrayList<ProjectBean> generateData() {
        
        UserBean user = new UserBean();
        user.setFirstName("Mike");
        user.setLastName("Sula");
        
        // create a couple dummy sites
        SiteBean[] sites = new SiteBean[3];
        
        sites[0] = new SiteBean();
        sites[0].setName("Nation Center for Supercomputing Applications");
        sites[0].setAcronym("NCSA"); 
        sites[0].setDescription("Now the joy of my world is in Zion");
        
        sites[1] = new SiteBean();
        sites[1].setName("Texas Advanced Computing Center");
        sites[1].setAcronym("TACC"); 
        sites[1].setDescription("Now the joy of my world is in Zion");
        
        sites[2] = new SiteBean();
        sites[2].setName("Center for Computational Science");
        sites[2].setAcronym("CCS"); 
        sites[2].setDescription("Now the joy of my world is in Zion");
        
        CollaboratorBean[] collaborators = new CollaboratorBean[6];
        Address address = new Address("1313 Mockingbird Ln","","Springfield","IL","52501","USA");
        
        collaborators[0] = new CollaboratorBean();
        collaborators[0].setFirstName("Rion");
        collaborators[0].setLastName("Dooley");
        collaborators[0].setClassification(UserClassificationType.FACULTY.name());
        collaborators[0].setTotalUsage(new UsageBean(100,50,50));
        collaborators[0].setEmail("dooley@home.com");
        collaborators[0].setAddress(address);
        collaborators[0].setPhone("(000) 000-0000");
        collaborators[0].setFax("(000) 000-0000");
        collaborators[0].setIm("anon");
        collaborators[0].setImProvider("anon");
        collaborators[0].setDepartment("pain");
        collaborators[0].setInstitute("hard knocks");
        collaborators[0].setPermission(UserPermissionType.USER.name());
        collaborators[0].addUsageRecord("alpha.test.org",new UsageBean(1,1,0));
        collaborators[0].addUsageRecord("alpha.test.org",new UsageBean(10,5,5));
        collaborators[0].addUsageRecord("alpha.test.org",new UsageBean(8,2,6));
        
        collaborators[1] = new CollaboratorBean();
        collaborators[1].setFirstName("Chuck");
        collaborators[1].setLastName("Norris");
        collaborators[1].setClassification(UserClassificationType.GRADUATE.name());
        collaborators[1].setTotalUsage(new UsageBean(200,150,50));
        collaborators[1].setEmail("death@home.com");
        collaborators[1].setAddress(address);
        collaborators[1].setPhone("(111) 111-1111");
        collaborators[1].setFax("(111) 111-1111");
        collaborators[1].setIm("anon");
        collaborators[1].setImProvider("anon");
        collaborators[1].setDepartment("pain");
        collaborators[1].setInstitute("hard knocks");
        collaborators[1].setPermission(UserPermissionType.USER.name());
        collaborators[1].addUsageRecord("alpha.test.org",new UsageBean(1,1,0));
        collaborators[1].addUsageRecord("alpha.test.org",new UsageBean(8,2,6));
        collaborators[1].addUsageRecord("alpha.test.org",new UsageBean(1,1,0));
        
        collaborators[2] = new CollaboratorBean();
        collaborators[2].setFirstName("Jack");
        collaborators[2].setLastName("Bauer");
        collaborators[2].setClassification(UserClassificationType.POSTDOCTORATE.name());
        collaborators[2].setTotalUsage(new UsageBean(200,50,150));
        collaborators[2].setEmail("reaper@home.com");
        collaborators[2].setAddress(address);
        collaborators[2].setPhone("(222) 222-2222");
        collaborators[2].setFax("(222) 222-2222");
        collaborators[2].setIm("anon");
        collaborators[2].setImProvider("anon");
        collaborators[2].setDepartment("pain");
        collaborators[2].setInstitute("hard knocks");
        collaborators[2].setPermission(UserPermissionType.ADMIN.name());
        collaborators[2].addUsageRecord("alpha.test.org",new UsageBean(10,5,5));
        collaborators[2].addUsageRecord("alpha.test.org",new UsageBean(8,2,6));
        collaborators[2].addUsageRecord("alpha.test.org",new UsageBean(25,3,22));
        
        collaborators[3] = new CollaboratorBean();
        collaborators[3].setFirstName("Jet");
        collaborators[3].setLastName("Lee");
        collaborators[3].setClassification(UserClassificationType.POSTDOCTORATE.name());
        collaborators[3].setTotalUsage(new UsageBean(200,50,150));
        collaborators[3].setEmail("jlee@home.com");
        collaborators[3].setAddress(address);
        collaborators[3].setPhone("(333) 333-3333");
        collaborators[3].setFax("(333) 333-3333");
        collaborators[3].setIm("anon");
        collaborators[3].setImProvider("anon");
        collaborators[3].setDepartment("pain");
        collaborators[3].setInstitute("hard knocks");
        collaborators[3].setPermission(UserPermissionType.USER.name());
        collaborators[3].addUsageRecord("alpha.test.org",new UsageBean(10,5,5));
        collaborators[3].addUsageRecord("alpha.test.org",new UsageBean(8,2,6));
        collaborators[3].addUsageRecord("alpha.test.org",new UsageBean(100,3,22));
        
        collaborators[4] = new CollaboratorBean();
        collaborators[4].setFirstName("Ralph");
        collaborators[4].setLastName("Maccio");
        collaborators[4].setClassification(UserClassificationType.UNIVERSITY_RESEARCH_STAFF.name());
        collaborators[4].setTotalUsage(new UsageBean(200,50,150));
        collaborators[4].setEmail("crane@home.com");
        collaborators[4].setAddress(address);
        collaborators[4].setPhone("(444) 444-4444");
        collaborators[4].setFax("(444) 444-4444");
        collaborators[4].setIm("anon");
        collaborators[4].setImProvider("anon");
        collaborators[4].setDepartment("pain");
        collaborators[4].setInstitute("hard knocks");
        collaborators[4].setPermission(UserPermissionType.ADMIN.name());
        collaborators[4].addUsageRecord("alpha.test.org",new UsageBean(10,5,5));
        collaborators[4].addUsageRecord("alpha.test.org",new UsageBean(8,2,6));
        collaborators[4].addUsageRecord("alpha.test.org",new UsageBean(100,3,22));

        collaborators[5] = new CollaboratorBean();
        collaborators[5].setFirstName("Ben");
        collaborators[5].setLastName("Watt");
        collaborators[5].setClassification(UserClassificationType.FACULTY.name());
        collaborators[5].setTotalUsage(new UsageBean(200,50,150));
        collaborators[5].setEmail("buzzinfly@home.com");
        collaborators[5].setAddress(address);
        collaborators[5].setPhone("(555) 555-5555");
        collaborators[5].setFax("(555) 555-5555");
        collaborators[5].setIm("anon");
        collaborators[5].setImProvider("anon");
        collaborators[5].setDepartment("pain");
        collaborators[5].setInstitute("hard knocks");
        collaborators[5].setPermission(UserPermissionType.PI.name());
        collaborators[5].addUsageRecord("alpha.test.org",new UsageBean(10,5,5));
        collaborators[5].addUsageRecord("alpha.test.org",new UsageBean(8,2,6));
        collaborators[5].addUsageRecord("alpha.test.org",new UsageBean(100,3,22));
        
        
        // create a few dummy resources for each site
        HashSet<ComputeBean> hpcs = new HashSet<ComputeBean>();
        ComputeBean hpc = new ComputeBean();
        hpc.setName("alpha");
        hpc.setHostname("alpha.test.org");
        hpc.setIpAddress("111.111.111.111");
        hpc.setComment("Not for production use");
        hpc.setSoftware(new ArrayList<SoftwareBean>());
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.DOWN);
        hpc.setLoad(new LoadBean(1,5,10,15,20,25,30));
        hpc.setQueues(new ArrayList<QueueBean>());
        hpc.setSite(sites[0]);
        hpcs.add(hpc);
        
        hpc = new ComputeBean();
        hpc.setName("beta");
        hpc.setHostname("beta.test.org");
        hpc.setIpAddress("222.222.222.222");
        hpc.setComment("Not for production use");
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UNKNOWN);
        hpc.setLoad(new LoadBean(31,35,40,45,50,55,60));
        hpc.setQueues(new ArrayList<QueueBean>());
        hpc.setSite(sites[0]);
        hpcs.add(hpc);
        
        hpc = new ComputeBean();
        hpc.setName("gamma");
        hpc.setHostname("gamma.test.org");
        hpc.setIpAddress("333.333.333.333");
        hpc.setComment("Not for production use");
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UP);
        hpc.setLoad(new LoadBean(61,65,70,75,80,85,90));
        hpc.setQueues(new ArrayList<QueueBean>());
        hpc.setSite(sites[1]);
        hpcs.add(hpc);
        
        hpc = new ComputeBean();
        hpc.setName("delta");
        hpc.setHostname("delta.test.org");
        hpc.setIpAddress("444.444.444.444");
        hpc.setComment("Not for production use");
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UP);
        hpc.setLoad(new LoadBean(61,65,70,75,80,85,90));
        hpc.setQueues(new ArrayList<QueueBean>());
        hpc.setSite(sites[1]);
        hpcs.add(hpc);
        
        hpc = new ComputeBean();
        hpc.setName("hoffa");
        hpc.setHostname("hoffa.test.org");
        hpc.setIpAddress("5.5.5.5");
        hpc.setComment("Not for production use");
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UP);
        hpc.setLoad(new LoadBean(51,55,50,55,50,55,50));
        hpc.setQueues(new ArrayList<QueueBean>());
        hpc.setSite(sites[2]);
        hpcs.add(hpc);
        
        hpc = new ComputeBean();
        hpc.setName("brasco");
        hpc.setHostname("brasco.test.org");
        hpc.setIpAddress("5.5.5.5");
        hpc.setComment("Not for production use");
        hpc.setTotalDisk(100);
        hpc.setTotalCpu(100);
        hpc.setStatus(ResourceStatusType.UP);
        hpc.setLoad(new LoadBean(61,65,60,65,60,65,60));
        hpc.setQueues(new ArrayList<QueueBean>());
        hpc.setSite(sites[2]);
        hpcs.add(hpc);

        ArrayList<ProjectBean> projects = new ArrayList<ProjectBean>();
        ProjectBean project = new ProjectBean();
        project.setName("Project1");
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setType(AccessType.COMMUNITY);
        project.setStatus(ProjectStatusType.PENDING);
        project.setComment("nothign to say");
        project.setCurrent(false);
        UsageBean usg = new UsageBean();
        usg.setAllocated(1000);
        usg.setUsed(250);
        usg.setBalance(750);
        project.setUsage(usg);
        projects.add(project);
        
        project = new ProjectBean();
        project.setName("Project2");
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setType(AccessType.TERAGRID);
        project.setStatus(ProjectStatusType.ACTIVE);
        project.setCurrent(false);
        project.setComment("nothign to say");
        usg = new UsageBean();
        usg.setAllocated(100);
        usg.setUsed(50);
        usg.setBalance(50);
        project.setUsage(usg);
        projects.add(project);
        
        project = new ProjectBean();
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setType(AccessType.EXTERNAL);
        project.setName("external_proj");
        project.setComment("nothign to say");
        project.setStatus(ProjectStatusType.EXPIRED);
        usg = new UsageBean();
        usg.setAllocated(200);
        usg.setUsed(150);
        usg.setBalance(50);
        project.setUsage(usg);
        project.setCurrent(false);
        projects.add(project);
        
//        user.setProjects(projects);
//        
//        VO vo = new VO();
//        vo.setUser(user);
        
        return projects;
    }

    DefaultMutableTreeNode getTreeNode(TreePath path) {
        // if no path is selected, select the parent path.
        if (path == null)
            return (DefaultMutableTreeNode)m_tree.getPathForRow(0).getLastPathComponent();
        
        return (DefaultMutableTreeNode)path.getLastPathComponent();
    }
    
    public void refresh() {
        ProjectBean project = null;
        CollaboratorBean collaborator = null;
        AccessType accessType = null;
        boolean isRoot = false;
        TreePath selectedPath = m_tree.getSelectionPath();
        // recored the previous values
        DefaultMutableTreeNode node = getTreeNode(m_tree.getSelectionPath());
        if (node.getUserObject() instanceof UserNode) {
            accessType = (AccessType)((DefaultMutableTreeNode)((DefaultMutableTreeNode)node.getParent()).getParent()).getUserObject();
            project = ((ProjectNode)((DefaultMutableTreeNode)node.getParent()).getUserObject()).getProject();
            collaborator = ((UserNode)node.getUserObject()).getCollaborator();
        } else if (node.getUserObject() instanceof ProjectNode) {
            accessType = (AccessType)((DefaultMutableTreeNode)node.getParent()).getUserObject();
            project = ((ProjectNode)node.getUserObject()).getProject();
        } else if (node.getUserObject() instanceof AccessType) {
            accessType = (AccessType)node.getUserObject();
        } else {
            // only root was selected
            isRoot = true;
        }
        
        // rebuild the site table and add the new model to the table. everything else
        // will refresh on the next click.
        projectTable = createProjectUsageTableData(GridChem.projects);
        
        // here is the root node
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("CCG");
        
        // iterate through the project collab table parsing out each access type in turn
        for (AccessType projectType: AccessType.values()){
            
            DefaultMutableTreeNode typeNode = new DefaultMutableTreeNode(projectType);
            
            for (ProjectBean p: projectTable.keySet()) {
                
            	// if the project type matches the iterative type, add it
            	if (p.getType().equals(projectType)) {
	                
            		DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(
	                        new ProjectNode(p));
	                
	                for(CollaboratorBean collab: projectTable.get(p)) {
	                
	                    DefaultMutableTreeNode newUserNode = new DefaultMutableTreeNode(
	                            new UserNode(collab));
	                    
	                    projectNode.add(newUserNode);
	                }
	            
	                typeNode.add(projectNode);
            	}
            }
            
            rootNode.add(typeNode);
        }
        
        m_tree.setModel(m_model = new DefaultTreeModel(rootNode));
        
        // reselect the proper node in the tree to refesh the panel
        
        if (isRoot) {
            // select tree root
            usageChart.setProjects(projectTable);
        } else {
        	m_tree.setSelectionPath(selectedPath);
        } 
//        else if (accessType != null  && project == null) {
//        }
//            // else set the chart to show summary of all 'accessType' projects
//            Hashtable<AccessType,HashSet<ProjectBean>> projectSubsetTable = 
//                new Hashtable<AccessType,HashSet<ProjectBean>>();
//            
//            projectSubsetTable.put(projectTable.get(accessType));
//            
//            usageChart.setProjects(projectSubsetTable);
//            
//        } else if (project != null) {
//            boolean projectFound = false;
//            
//            DefaultMutableTreeNode accessTypeNode = null;
//            DefaultMutableTreeNode rt = (DefaultMutableTreeNode) m_model.getRoot();
//            
//            for(int k=0;k<rt.getChildCount();k++) {
//                if (((AccessType)((DefaultMutableTreeNode)rt.getChildAt(k)).getUserObject()).equals(accessType)) {
//                    accessTypeNode = (DefaultMutableTreeNode)rt.getChildAt(k);
//                }
//            }
//            
//            for(int i=0; i < accessTypeNode.getChildCount(); i++) {
//                DefaultMutableTreeNode treeNode = 
//                    (DefaultMutableTreeNode)accessTypeNode.getChildAt(i);
//                
//                
//                ProjectNode projectNode = 
//                    (ProjectNode)treeNode.getUserObject();
//                
//                if (projectNode.getProject().getProjectName().equals(project.getProjectName())) {
//                    projectFound = true;
//                    if (collaborator == null) {
//                        usageChart.setProject(projectNode.getProject());
//                    } else {
//                        boolean collabFound = false;
//                        for (int j=0; j<treeNode.getChildCount(); j++) {
//                            UserNode userNode = 
//                                (UserNode)((DefaultMutableTreeNode)treeNode.getChildAt(j)).getUserObject();
//                            if (userNode.getCollaborator().equals(collaborator)) {
//                                m_tree.setSelectionPath(new TreePath(
//                                        ((DefaultMutableTreeNode)treeNode.getChildAt(j)).getPath()));
//                                collabFound = true;
//                                System.out.println("Found collaborator: " + collaborator.getFirstName() + 
//                                        " " + collaborator.getLastName());
//                                break;
//                            }
//                        }
//                        
//                        if (!collabFound) {
//                            usageChart.setProject(projectNode.getProject());
//                            JOptionPane.showMessageDialog(
//                                this,
//                                "User " + collaborator.getFirstName() + 
//                                " " + collaborator.getLastName() + 
//                                " is no longer associated with project\n" + 
//                                project.getName() + "\n" +
//                                "Please direct any questions to consulting at\n" + 
//                                "http://www.gridchem.org/consult.",
//                                "Usage Management Exception", JOptionPane.ERROR_MESSAGE
//                            );
//                        }
//                    }
//                    break;
//                }
//            }
//            if (!projectFound) {
//                usageChart.clear();
//                JOptionPane.showMessageDialog(
//                    this,
//                    "Project " + project.getName() + " is no longer available.\n" + 
//                    "Please direct any questions to consulting at\n" + 
//                    "http://www.gridchem.org/consult.",
//                    "Usage Management Exception", JOptionPane.ERROR_MESSAGE
//                );
//            }
//        }
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
                    System.out.println("Starting update usage command");
                
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
                                    "Retrieving updated usage info...",
                                    worker);
                        } // if the flag is false, don't show the progress bar
                    } else {
                        startWaiting("Progress...", 
                                "Retrieving updated usage info...",
                                worker);
                    }
                } else {
                    updateProgress("Retrieving updated usage info...");
                }
                    
                    worker.start();
                
            } else if (status.equals(Status.COMPLETED)) {
                if (Settings.VERBOSE)
                    System.out.println(command.getCommand() + " Command Completed");
                
                if (command.getCommand().equals(JobCommand.GETUSAGE)) {
                    Trace.note("Finished UPDATE, refreshing user's VO");
                    
                    GridChem.projects = ((GETUSAGECommand)command).getOutput();
                    
                    refresh();
                    
                }
            } else {
                if (Settings.VERBOSE)
                    System.out.println(command.getCommand() + " Command Failed!");
                
                if (command.getCommand().equals(JobCommand.GETUSAGE)) {
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
    
    

    class ProjectTreeSelectionListener
        implements TreeSelectionListener {

        public void valueChanged(final TreeSelectionEvent e) {
            
            final Object treeNode = getTreeNode(e.getPath()).getUserObject();
            
            Thread runner = new Thread() {

                public void run() {
                
                    Runnable runnable = new Runnable() {
                        public void run() {
        
                            // if it's a site, then update the site info and clear the resource 
                            // info panel and display summary charts of all the resources on the site.
                            if (treeNode instanceof ProjectNode ) {
                            	ProjectBean project = ((ProjectNode)treeNode).getProject();
                                usageChart.setProject(project,projectTable.get(project));
                            
                               // if it's a resource, then show the site info, resoruce info, and display
                            // all the load charts for that resource.
                            } else if (treeNode instanceof UserNode) {
                            
                                // TODO: change the pie chart to display user info
//                                ProjectBean newProject = new ProjectBean();
                                ProjectBean oldProject = ((ProjectNode)
                                      ((DefaultMutableTreeNode)getTreeNode(e.getPath())
                                      .getParent()).getUserObject()).getProject();

                                usageChart.setProject(oldProject, ((UserNode)treeNode).getCollaborator());
                            
                            // if they select the project type node, then display for all
                            // projects of that type
                            } else if (treeNode instanceof AccessType) {
                                // change the pie chart to display info for all projects
                                // of type treeNode
                            	Hashtable<ProjectBean,List<CollaboratorBean>> tempTable = 
                                    new Hashtable<ProjectBean,List<CollaboratorBean>>();
                                
                                HashSet<ProjectBean> projects = new HashSet<ProjectBean>();
                                
                                DefaultMutableTreeNode node = getTreeNode(e.getPath());
                                
                                for(int i=0;i<node.getChildCount();i++) {
                                	ProjectBean project = ((ProjectNode)((DefaultMutableTreeNode)node.getChildAt(i)).getUserObject()).getProject();
                                	tempTable.put(project, projectTable.get(project));
                                }
                                
                                usageChart.setProjects(tempTable);
                                    
                            // otherwise it's the ccg, then show the ccg info, no resource info, and 
                            // all the load charts for the entire vo
                            } else if (treeNode instanceof String) {
                                
                            	Hashtable<ProjectBean,List<CollaboratorBean>> tempTable = 
                                    new Hashtable<ProjectBean,List<CollaboratorBean>>();
                                
                                DefaultMutableTreeNode root = getTreeNode(e.getPath());
                                
                                for(int j=0;j<root.getChildCount();j++) {
                                    
                                    DefaultMutableTreeNode projectTypeNode = (DefaultMutableTreeNode)root.getChildAt(j);
                                    
                                    HashSet<ProjectBean> projects = new HashSet<ProjectBean>();
                                    
                                    for(int i=0;i<projectTypeNode.getChildCount();i++) {
                                    	ProjectBean project = (((ProjectNode)((DefaultMutableTreeNode)projectTypeNode
                                                .getChildAt(i)).getUserObject()).getProject());
                                    	tempTable.put(project, projectTable.get(project));
                                    }
                                }
                                
                                usageChart.setProjects(tempTable);
                                
                            } else {
                                
                                usageChart.clear();
                                
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
        
        if (obj instanceof UserNode) {
            setIcon(((UserNode)obj).m_symbol);
            
            double used = ((UserNode)obj).getCollaborator().getTotalUsage().getUsed();
            double allocated = ((UserNode)obj).getCollaborator().getTotalUsage().getAllocated();
            long percentage = Math.round(100.0 * used / allocated);
            
            String user = GridChem.user.getFirstName() + 
                GridChem.user.getLastName() + GridChem.user.getEmail();
            
            String coll = ((UserNode)obj).getCollaborator().getFirstName() + 
                ((UserNode)obj).getCollaborator().getLastName() + 
                ((UserNode)obj).getCollaborator().getEmail();
            
            String text;
            
            ProjectBean p = ((ProjectNode)((DefaultMutableTreeNode)node.getParent()).getUserObject()).getProject(); 
            
            if (p.getStatus().equals(ProjectStatusType.ACTIVE)) {
                text = "<span color=\"" + ((UserNode)obj).m_color + 
                    "\"><em>" + obj.toString() + "</em></span>";
                if (p.isCurrent()) {
                    if (user.equals(coll)) {
                        text = "<b>" + text + "</b>";
                    }
                }
            } else {
                text = "<span color=\"gray\"><em>" + obj.toString() + "</em></span>";
            }
            
            
            
            String lineItem = "<html><table width=\"100%\">" + 
                                "<tr><td>" + text + "</td>" + 
                                "<td></td><td></td><td></td><td></td><td></td>" + 
                                "<td>" + Fraction.digits(used,2) + "</td>" + 
                                "<td>" + percentage  + "%</td>" +
                                "</tr></table></html>";
            
            setText(lineItem);
            
            setHorizontalAlignment(JLabel.RIGHT);
            
            // create the panel for the mouseover event containing user info
            setToolTipText(getUserToolTipText(((UserNode)obj).getCollaborator()));
            
        } else if (obj instanceof ProjectNode){
            setIcon(((ProjectNode)obj).m_symbol);
            
            ProjectBean project = ((ProjectNode)obj).getProject();
            
//            double used = 0;
//            double allocated = 0;
//            
//            for (CollaboratorBean c: ) {
//                used += c.getTotalUsage().getUsed();
//                allocated += c.getTotalUsage().getAllocated();
//            }

            long percentage = Math.round(100.0 * project.getUsage().getUsed() / project.getUsage().getAllocated());
            
            String text = project.getName();
            
            if (project.isCurrent()) {
                text = "<b>" + text + "</b>";
            }
            
            if (!project.getStatus().equals(ProjectStatusType.ACTIVE)) {
                text = "<span color=\"" + ((ProjectNode)obj).m_color + 
                    "\"><em>" + text + " (" + project.getStatus() + 
                    ")</em></span>";
            }  
            
            String lineItem = "<html><table width=\"100%\">" + 
                "<tr><td>" + text + "</td>" + 
                "<td></td><td></td><td></td><td></td><td></td>" + 
                "<td>" + Fraction.digits(project.getUsage().getUsed(),2) + "</td>" + 
                "<td>" + percentage  + "%</td>" +
                "</tr></table></html>";
            
            setText(lineItem);
            
            setHorizontalAlignment(JLabel.RIGHT);
            
            // create the panel for the mouseover event containing project info
            setToolTipText(getProjectToolTipText(((ProjectNode)obj).getProject()));
            
        } else if (obj instanceof AccessType) {
            
            setIcon(null);
            
            setText(obj.toString());
            
            setToolTipText("");
        
        } else {
        
            setIcon(null);
            
            setText(obj.toString());
            
            setToolTipText("");
            
        }
        
        setSize(new Dimension(400,30));
        setMinimumSize(new Dimension(400,30));
        setMaximumSize(new Dimension(400,30));
        
        return result;
    
    }
    
    private String getUserToolTipText(CollaboratorBean collab) {
        String toolTipHTML = "";
        
        toolTipHTML += "<html><body bgcolor=\"#666666\"><table bgcolor=\"#666666\">";
        toolTipHTML += "<tr><th colspan=\"2\"  bgcolor=\"#9999FF\">Project Collaborator</th></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Name:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + collab.getFirstName() + " " + collab.getLastName() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Address:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + collab.getAddress().toString() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Phone:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + collab.getPhone() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Email:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + collab.getEmail() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>IM:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + collab.getIm() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>IM Provider:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + collab.getImProvider() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Institute:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + collab.getInstitute() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Department:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + collab.getDepartment() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Research Designation:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + collab.getClassification() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>User Type:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + collab.getPermission() + "</FONT></td></tr>";
                 
        toolTipHTML += "<tr><th colspan=\"2\"  bgcolor=\"#9999FF\"><b>Current Usage in SUs</th></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Allocated:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + collab.getTotalUsage().getAllocated() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Used:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + Fraction.digits(collab.getTotalUsage().getUsed(),2) + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Balance:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + Fraction.digits(collab.getTotalUsage().getBalance(),2) + "</FONT></td></tr>";
                      
        toolTipHTML += "</table></body></html>";
         
        return toolTipHTML;
    }
    
    private String getProjectToolTipText(ProjectBean project) {
        String toolTipHTML = "";
        
        toolTipHTML += "<html><body bgcolor=\"#666666\"><table bgcolor=\"#666666\">";
        toolTipHTML += "<tr><th colspan=\"2\"  bgcolor=\"#9999FF\">Project Collaborator</th></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Name:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + project.getName() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Start Date:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + project.getStartDate() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>End Date:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + project.getEndDate() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Funding Org:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + 
            project.getFundingOrganization() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Classification:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + project.getType() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Status:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + project.getStatus() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Description:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + project.getComment() + "</FONT></td></tr>";
        toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Current Usage:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + project.getUsage().getUsed() + "</FONT></td></tr>";
        
        toolTipHTML += "<tr><th colspan=\"2\"  bgcolor=\"#9999FF\"><b>Current Usage in SU's</th></tr>";
//        for (CollaboratorBean collab: project.getCollaborators()) {
//            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>" + collab.getFirstName() + " " + collab.getLastName() + 
//                ": </b></FONT></td><td><FONT COLOR=\"#FFFFFF\">" + Fraction.digits(collab.getTotalUsage().getUsed(),2) + "</FONT></td></tr>";
//        }
        
        toolTipHTML += "</table></body></html>";
         
        return toolTipHTML;
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

/**
 * Class containing the project collaborator objects and an icon representing 
 * the current classification of the collaborator.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
class UserNode {
    
    public static ImageIcon ICON_FACULTY = new ImageIcon(Env.getImagesDir() +"/icons/faculty.jpg");
    public static ImageIcon ICON_GRADUATE = new ImageIcon(Env.getImagesDir() +"/icons/graduate.jpg");
    public static ImageIcon ICON_UNDERGRAD = new ImageIcon(Env.getImagesDir() +"/icons/undergrad.jpg");
    public static ImageIcon ICON_POSTDOC = new ImageIcon(Env.getImagesDir() +"/icons/postdoc.jpg");
    public static ImageIcon ICON_RESEARCH_STAFF = new ImageIcon(Env.getImagesDir() +"/icons/research_staff.jpg");
    public static ImageIcon ICON_NON_RESEARCH_STAFF = new ImageIcon(Env.getImagesDir() +"/icons/staff.jpg");
    public static ImageIcon ICON_OTHER = new ImageIcon(Env.getImagesDir() +"/icons/other.jpg");
    
    protected CollaboratorBean m_collab;
    
    public ImageIcon m_symbol;
    
    public String m_color;
    
    public UserNode(CollaboratorBean collaborator) {
        m_collab = collaborator;
        
        if (collaborator.getClassification().equals(UserClassificationType.FACULTY)) {
            m_symbol = ICON_FACULTY;
        } else if (collaborator.getClassification().equals(UserClassificationType.GRADUATE)) {
            m_symbol = ICON_GRADUATE;
        } else if (collaborator.getClassification().equals(UserClassificationType.UNDERGRADUATE)) {
            m_symbol = ICON_UNDERGRAD;
        } else if (collaborator.getClassification().equals(UserClassificationType.POSTDOCTORATE)) {
            m_symbol = ICON_POSTDOC;
        } else if (collaborator.getClassification()
                .equals(UserClassificationType.UNIVERSITY_RESEARCH_STAFF)) {
            m_symbol = ICON_RESEARCH_STAFF;
        } else if (collaborator.getClassification()
                .equals(UserClassificationType.UNIVERSITY_NON_RESEARCH_STAFF)) {
            m_symbol = ICON_NON_RESEARCH_STAFF;
        } else {
            m_symbol = ICON_OTHER;
        } 
        
        double used = m_collab.getTotalUsage().getUsed();
        double allocated = m_collab.getTotalUsage().getAllocated();
        long percentage = Math.round(100.0 * used / allocated);
        
        if (used >= allocated) {
            m_color = "RED";
        } else if (percentage >= 90.0) {
            m_color = "BLUE";
        } else {
            m_color = "BLACK";
        }
    }
    
    public CollaboratorBean getCollaborator() {
        return m_collab;
    }
    
    public String toString() {
        return m_collab.getFirstName() + " " + m_collab.getLastName();
    }
}

/**
 * Class containing the projectBean object and an icon representing the current state
 * of the project determined by it's status, expiration date, and usage.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
class ProjectNode {
    
    public static ImageIcon ICON_SAFE = new ImageIcon(Env.getImagesDir() +"/icons/project_valid.jpg");
    public static ImageIcon ICON_PENDING = new ImageIcon(Env.getImagesDir() +"/icons/project_pending.jpg");
    public static ImageIcon ICON_WARNING = new ImageIcon(Env.getImagesDir() +"/icons/project_warning.jpg");
    public static ImageIcon ICON_SATIATED = new ImageIcon(Env.getImagesDir() +"/icons/project_full.jpg");
    public static ImageIcon ICON_EXPIRED = new ImageIcon(Env.getImagesDir() +"/icons/project_expired.jpg");
    
    protected ProjectBean m_project;
    
    public ImageIcon m_symbol;
    
    public String m_color;

    public ProjectNode(ProjectBean project) {
        m_project = project;
        m_color = "BLACK";
        
        Date edate = project.getEndDate();
		Date bdate = project.getStartDate();
		
		if (project.getStatus().equals(ProjectStatusType.ACTIVE)) {
		    if (edate.getTime() < new Date().getTime()) {
		        // expiration date of project has passed
		        m_symbol = ICON_EXPIRED;
		        m_color = "RED";
		    } else if (bdate.getTime() > new Date().getTime()) {
		        // project start date has not come yet
		        m_symbol = ICON_PENDING;
		        m_color = "GRAY";
		    } else if (project.getUsage().getUsed() >= project.getUsage().getAllocated()) {
		        m_symbol = ICON_SATIATED;
		        m_color = "RED";
		    } else if (project.getUsage().getUsed() >= (project.getUsage().getAllocated() * .9)) {
		        m_symbol = ICON_WARNING;
		        m_color = "BLUE";
		    } else {
		        m_symbol = ICON_SAFE;
		    }
		} else if (project.getStatus().equals(ProjectStatusType.PENDING)) {
		    m_symbol = ICON_PENDING;
		    m_color = "GRAY";
		} else if (project.getStatus().equals(ProjectStatusType.DEACTIVATED)) {
		    m_symbol = ICON_EXPIRED;
		    m_color = "RED";
		} else if (project.getStatus().equals(ProjectStatusType.EXPIRED)) {
		    m_symbol = ICON_EXPIRED;
		    m_color = "RED";
		} else {
		    m_symbol = ICON_SAFE;
		}
        
    }

    public ProjectBean getProject() {
        return m_project;
    }

    public String toString() {
        return m_project.getName();
    }
}

/**
*   -description :<p> Sets the number of decimal places to the argument specified
*     the class is overloaded to return float and double data types. The parameter
*     named 'places' is the number of decimal places returned by the method.
*     Note: the parameter int will also work (without amendment) for byte and short.</p>
*     // <b>EXAMPLE:</b><br>
*     <code>
*          double d = 1.250550123456789;
*          System.out.print( Fraction.digits(d,4) );
*     // will output the double as : 1.2506
*          float f = (float)1.2505012;
*          System.out.print( Fraction.digits(f,3) );
*     // will output the double as : 1.251
*     </code>
*     This has the advantage over other methods in that the data type retains its
*     integrity as a double or a float data-type, instead of converting it to a String
*     as in java.text.DecimalFormat and rounding off the additional data.<br>
*     The class could be also used to avoid the pitfalls of floating point
*     innacuracies;<br>
*     // <b>EXAMPLE:</b><br>
*     <code>
*         double d1 = 3.0 * 0.1;
*         System.out.println(d1*10);
*     // will output : 3.0000000000000004
*         double d2 = 3.0 * 0.1;
*         byte b = (byte)14; // works with other data type widths
*         System.out.println(Fraction.digits(d2*10, b)); // slice off the last digit
*     // will output : 3.0
*     </code>
*  @author Sum-shusSue
*/
class Fraction {
 
   public static double digits(double d, int places) {
 
      return (long)(d*Math.pow(10,places)+0.5)/Math.pow(10,places);
 
   }
 
   public static double digits(double d, long places) {
 
      return (long)(d*Math.pow(10,places)+0.5)/Math.pow(10,places);
 
   }
 
   public static float digits(float d, int places) {
 
      return (int)(d*Math.pow(10,places)+0.5)/(float)Math.pow(10,places);
 
   }
 
   public static float digits(float d, long places) {
 
      return (int)(d*Math.pow(10,places)+0.5)/(float)Math.pow(10,places);
 
   }
 
}

