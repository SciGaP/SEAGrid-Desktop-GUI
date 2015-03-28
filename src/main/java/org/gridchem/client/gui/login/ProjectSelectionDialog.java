/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Aug 1, 2006
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

package org.gridchem.client.gui.login;

/*
Code revised from Desktop Java Live:
http://www.sourcebeat.com/downloads/
*/
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gridchem.client.GridChem;
import org.gridchem.client.Invariants;
import org.gridchem.client.common.Preferences;
import org.gridchem.client.common.Settings;
import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.util.Env;
import org.gridchem.client.util.GMS3;
import org.gridchem.service.beans.ProjectBean;
import org.gridchem.service.model.enumeration.AccessType;

/**
 * Selection dialog to allow user's to select the project with which
 * they wish to authenticate
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 * @param <sychronized>
 *
 */
public class ProjectSelectionDialog<sychronized> extends JDialog 
implements ActionListener, KeyListener,ListSelectionListener {
    
    private static JList projectList;
    
    private static JLabel projectDetailsLabel;
    
    private JButton selectButton;
    private JButton cancelButton;
    
    private Status status;
    private StatusListener listener;
    
    private static List<ProjectBean> projects;
    
    private static ProjectBean project; 
    
    private LoginDialog parent;
    
    public ProjectSelectionDialog(StatusListener listener, 
            List<ProjectBean> projects, LoginDialog parent) {
        
        this(projects, parent);
        
        this.status = Status.UNKNOWN;
        
        this.listener = listener;
        
    }
    
    public ProjectSelectionDialog(List<ProjectBean> projects, LoginDialog parent) {
    	super(parent, true);
    	
    	this.parent = parent;
    	
        this.projects = projects;
        
        JPanel getProjectDialogPanel = new JPanel(new GridLayout(3,1,10,10));
        
        ((GridLayout) getProjectDialogPanel.getLayout()).setVgap(5);
        
        Border titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder());
        Border bufferBorder = BorderFactory.createEmptyBorder(10,10,10,10);
        
        getProjectDialogPanel.setBorder(
                BorderFactory.createCompoundBorder(titledBorder, bufferBorder));
        
        getProjectDialogPanel.addKeyListener(this);
        getProjectDialogPanel.setPreferredSize(new Dimension(450,230));
        getProjectDialogPanel.setLayout(new BoxLayout(getProjectDialogPanel,BoxLayout.Y_AXIS));
        getProjectDialogPanel.add(createTitlePanel());
        getProjectDialogPanel.add(createProjectPanel());
        getProjectDialogPanel.add(createButtonPanel());
  
        setContentPane(getProjectDialogPanel);
        setTitle("Project Selection Dialog");
        setResizable(false);
        pack();
        
        // Centering the frame on the screen
        Toolkit kit = this.getToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        Dimension windowSize = this.getSize();
        int windowWidth = windowSize.width;
        int windowHeight = windowSize.height;
        int upperLeftX = (screenWidth - windowWidth)/2;
        int upperLeftY = (screenHeight - windowHeight)/2;   
        this.setLocation(upperLeftX, upperLeftY);
        
        this.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent arg0) {}
            public void windowIconified(WindowEvent arg0) {}
            public void windowDeiconified(WindowEvent arg0) {}
            public void windowActivated(WindowEvent arg0){}
            public void windowDeactivated(WindowEvent arg0) {}
            public void windowClosed(WindowEvent arg0) {}
            public void windowClosing(WindowEvent arg0) {
                project = null;
                setVisible(false);
//                setStatus(Status.FAILED);
            }
        });
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        setVisible(true);
    }
    
    /**
     * Create the description label showing the attributes of the selected project.
     * @return
     */
    private Component createTitlePanel() {
        JPanel titlePanel = new JPanel();
        
        projectDetailsLabel = new JLabel("Please select a project");
        
        titlePanel.add(projectDetailsLabel);
        
        return titlePanel;
    }
    
    

    private Component createProjectPanel() {
       
        String projectArray[] = new String[projects.size()];
        
        int i=0;
        
        int communityIndex = -1;
        
        // look up the user's default project type in their preferences.
        String preferredProject = Preferences.getString("gridchem_usertype");
        for(ProjectBean project:projects) {
            
            if (preferredProject != null && 
                    project.getType().equals(preferredProject.toUpperCase())) {
                
                communityIndex = i;
                projectDetailsLabel.setText(project.getType() + " PROJECT");
            }
            projectArray[i++] = project.getName();
        }
        
        projectList = new JList(projectArray);
        
        // set the project list to default to a community project.
        if (communityIndex != -1) {
            projectList.setSelectedIndex(communityIndex);
        } else {
            projectList.setSelectedIndex(0);
        }
        
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 ) {
                    projectDetailsLabel.setText(getSelectedProject().getType() + " PROJECT");
                } else if (e.getClickCount() == 2) {
                    project = getSelectedProject();
                    setVisible(false);
//                    if (project.getType().equals("COMMUNITY")) {
//                        setStatus(Status.START);
//                        setVisible(false);
//                    } else {
//                        LoginPanel.ls2 = new LoginStage2();
//                        GridChem.oc.lp.setContentPane(LoginPanel.ls2);
//                        setVisible(false);
//                        GridChem.oc.lp.pack();
//                        GridChem.oc.lp.setVisible(true);
//                    }
                 }
            }
        };
        
        projectList.addMouseListener(mouseListener);
        projectList.addListSelectionListener(this);
        projectList.addKeyListener(this);
        JScrollPane scrollPane = new JScrollPane(projectList);
        
        return scrollPane;
    }
    
    private Component createButtonPanel() {
        makeButtons();
        
        JPanel buttonInterior = new JPanel();
        buttonInterior.setLayout(new GridLayout(1,2,5,0));
        buttonInterior.add(selectButton);
        buttonInterior.add(cancelButton);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonInterior);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,5));
        
        return buttonPanel;
    }
    /**
     * Create and format the buttons on the panel
     */
    private void makeButtons() {
        
        selectButton = new JButton("Select");
        selectButton.setVerticalTextPosition(AbstractButton.CENTER);
        selectButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        selectButton.setToolTipText(
                "Continue logging into the CCG under this project.");
        selectButton.addKeyListener(this);
        selectButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        cancelButton.setVerticalTextPosition(AbstractButton.CENTER);
        cancelButton.setHorizontalTextPosition(AbstractButton.CENTER);
        cancelButton.setToolTipText("Click to close this window");
        cancelButton.addActionListener(this);
        cancelButton.addKeyListener(this); 
    }
    
    private ProjectBean getSelectedProject() {
        return (ProjectBean)projects.toArray()[projectList.getSelectedIndex()];
    }

    public static void main(String[] a){
        String username;
        String password;
        String projectType;
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
//            GMSSession.getClient();
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
        
        String key = "";
        
        // Authenticate with the GMS_WS
        if (Settings.DEBUG)
            System.out.println("Logging " + username + " into the CCG.");
        try {
            GMS3.login(username, password, AccessType.COMMUNITY, new HashMap<String,String>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Settings.authenticated = true;
        Settings.authenticatedGridChem = true;
        Settings.gridchemusername = username;
        
        // Load the user's resources into the session.
        final List<ProjectBean> projects;
        
        try {
            projects = GMS3.getProjects();
            
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    showProjectDialog(projects);
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("<html><p>Failed to get user projects from GMS.<br>" + 
                    e.getMessage() + "</p></html>");
        }
        
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
    
    /**
     * Accessor method for AuthenticationForm to get the project the user selected.
     * 
     * @return ProjectBean or null if no project was selected
     */
    public ProjectBean getProject() {
    	return this.project;
    }
    
    private static void showProjectDialog(List<ProjectBean> projects) {
        JFrame f = new JFrame("Project Selection Test");
        JDialog psd = new ProjectSelectionDialog(projects, new LoginDialog(f,true));
        f.add(psd);
        f.pack();
        f.setVisible(true);
    }
    
    

    // action listener interface implementation
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == selectButton) {
            project = getSelectedProject();
            setVisible(false);
//            setStatus(Status.START);
        } else if (event.getSource() == cancelButton) {
            project = null;
            setVisible(false);
//            setStatus(Status.FAILED);
        }
        
    }

    // key listener interface implementation
    public void keyTyped(KeyEvent arg0) {}
    public void keyReleased(KeyEvent arg0) {}
    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();
          if (key == KeyEvent.VK_ENTER) {
              project = getSelectedProject();
              setVisible(false);
//              setStatus(Status.START);
          } else if (key == KeyEvent.VK_UP || 
                  key == KeyEvent.VK_DOWN) {
              projectDetailsLabel.setText(getSelectedProject().getType() + " PROJECT");
          } else if (key == KeyEvent.VK_ESCAPE) {
              project = null;
              setVisible(false);
//              setStatus(Status.FAILED);
          }
    }
    
    public void valueChanged(ListSelectionEvent arg0) {
        selectButton.setEnabled(true);
        projectDetailsLabel.setText(getSelectedProject().getType() + " PROJECT");
    }
    
//    public void setStatus(Status status) {
//        if (listener != null) {
//            this.status = status;
//            LoginCommand command;
//            
//            if (status.equals(Status.FAILED)) {
//                command = new CANCELCommand(this.listener);
//                
//            } else {
//                command = new SETPROJECTCommand(this.listener);
//                
//                command.getArguments().put("project", project);
//                command.getArguments().put("macAddress",Env.getMacAddress());
//            }
//            
//            this.listener.statusChanged(new StatusEvent(command,this.status));
//        } else {
//            System.out.println("Start loadVo action here");
//        }
//    }
    
}
