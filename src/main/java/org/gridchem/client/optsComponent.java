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
 * Created on Apr 13, 2005
 * Moved from GridChem.java @ CCS,UKy 
 * Indentation is four; tab stops is eight.
 * some code at the tail does not follow this indentation.
 *  
 */

package org.gridchem.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.text.html.HTMLEditorKit;

import nanocad.nanocadFrame2;

import org.gridchem.client.common.Settings;
import org.gridchem.client.gui.buttons.ApplicationMenuItem;
import org.gridchem.client.gui.buttons.DropDownButton;
import org.gridchem.client.gui.login.LoginDialog;
import org.gridchem.client.gui.panels.BareBonesBrowserLaunch;
import org.gridchem.client.gui.panels.RSSViewer;
import org.gridchem.client.gui.panels.parseRSS;
import org.gridchem.client.gui.panels.myccg.MonitorVO;
import org.gridchem.client.help.HelpBrowser;
import org.gridchem.client.util.Env;
import org.gridchem.client.util.GMS3;
import org.gridchem.service.beans.JobBean;

import G03Input.G03MenuTree;
import G03Input.InputFile;
import G03Input.InputfileReader;
import G03Input.showMolEditor;
import Gamess.gamessGUI.GamessGUI;
import Nanocad3d.Nanocad3D;


public class optsComponent extends JComponent implements ActionListener, WindowListener, ComponentListener
{
//    public static LoginPanel lp;
    public static Accounting acw;
    public static PreferencesWindow pw;
    public static SubmitJobsWindow sjw;
    public static JobManagementWindow mw;
    public static MonitorVO monitorWindow;
    public static SaveDataWindow sdw;
    public static RSSViewer rssw;
    public static HelpBrowser helpBrowser;
    public static JFrame mainFrame;
    public static Nanocad3D nano3DWindow; // added for nanocad 3D - Narendra Kumar Polani (CCS, UKY)
    public static nanocadFrame2 nanWindow;
    public static int selectedFrontPanel=0;
// lixh_add
    private JobBean j;

    JPanel buttonBox;
    Container messageBox;
    
    JButton authButton;
    JButton readButton;
    JButton prefButton;
    JButton usageButton;
    JButton g03guiButton;
    //JButton submButton;
    JButton mangButton;
    //JButton nanocadButton;
    JButton licenseButton;
    JButton exitButton;
    JButton helpButton;
    
    DropDownButton inputGeneratorGuiButton;
    DropDownButton moleditorGuiButton; //added -nik
    DropDownButton submGuiButton; //added -nik
    ApplicationMenuItem jobMenuItem; //added -nik
    ApplicationMenuItem flowMenuItem; //added -nik
    ApplicationMenuItem gaussianMenuItem;
    ApplicationMenuItem gamessMenuItem;
    
    public JTextPane dyninfoPane;
    public static JTextArea messageBoard;
    public JTextArea inputText;
    
    ApplicationMenuItem nano3dMenuItem; // added for nanocad 3d - Narendra Kumar Polani (CCS,UKY)
    //start addition nik
    ApplicationMenuItem nanoMenuItem;
    ApplicationMenuItem molMenuItem;
    ApplicationMenuItem gdisMenuItem;
    ApplicationMenuItem jmolMenuItem;
    ApplicationMenuItem tubeMenuItem;
    ApplicationMenuItem javaMenuItem;
    //end addition nik
    
    public boolean nanbool = false;
    
    //static JobList ListOfJobs = new JobList();

    public optsComponent()
    {
        // insert main control buttons
        authButton = new JButton("Sign In");
        readButton = new JButton("Announcements");
        prefButton = new JButton("Preferences");
        usageButton = new JButton("Show Usage");
        submGuiButton = createJobDDB();
        //submButton = new JButton("Submit Jobs");
        mangButton = new JButton("My CCG");
        moleditorGuiButton = createMolEdDDB();
        //nanocadButton = new JButton("Molecular Editor");
        inputGeneratorGuiButton = createDDB();
        licenseButton = new JButton("View License");
        exitButton = new JButton("Exit");
        helpButton = new JButton("Help");
        
        // set their tool tip texts
        authButton.setToolTipText("<html><p>Authenticate to the CCG.</p><html>");
        readButton.setToolTipText("<html><p>View real-time announcements</p>" + 
                "<p>from across the CCG.");
        prefButton.setToolTipText("<html><p>View and edit user preferences.</p><html>");
        usageButton.setToolTipText("<html><p>View comprehensive individual</p>" + 
                "<p>and group usage across all projects.</p><html>");
        //submButton.setToolTipText("<html><p>Create and submit jobs.</p><html>");
        mangButton.setToolTipText(
                "<html><p>Manage your jobs, monitor</p>" + 
                "<p>CCG resources and view </p>" + 
                "<p>individual and group</p>" + 
                "<p>usage across all projects.</p><html>");
        //nanocadButton.setToolTipText("<html><p>Launch the Nanocad editor.</p><html>");
        moleditorGuiButton.setToolTipText("<html><p>Launch the Molecular editor.</p><html>");
        licenseButton.setToolTipText("<html><p>View the full GridChem</p>" + 
                "<p>licensing agreement.</p><html>");
        exitButton.setToolTipText("<html><p>Exit GridChem.</p><html>");
        helpButton.setToolTipText("<html><p>View comprehensive help:</p>" + 
                "<p>documentation on GridChem and </p>" + 
                "<p>its supported applications.</p><html>");
        inputGeneratorGuiButton.setDropDownToolTipText("<html><p>Launch the Gaussian/GAMESS input builder.</p>" +
        		"<p>Press Alt + 1 for Gaussian</p>" +
        		"<p>Press Alt + 2 for GAMESS</p><html>");
        submGuiButton.setDropDownToolTipText("<html><p>Launch Job/Work Flow Editor.</p>+" +
        		"<p> Press Alt + 3 for Job Editor</p>" +
        		"<p> Press Alt + 4 for Work Flow Editor </p>");
        
        JPanel buttonBox = new JPanel();
        Container messageBox = Box.createVerticalBox();
        
        if(Settings.WEBSERVICE) {
            buttonBox.setLayout(new GridLayout(10,1,0,10));
        } else {
            buttonBox.setLayout(new GridLayout(11,1,0,10));
        }
        // Changed from 7 to 8 @CCS,UKy
    
        Border rbBorder = BorderFactory.createRaisedBevelBorder();
        Border eBorder1 = BorderFactory.createEmptyBorder(0,10,0,0);
        Border leBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        buttonBox.setBorder(eBorder1); 
        //buttonBox.setPreferredSize(new Dimension(180,280));
        JPanel buttonBoxouter = new JPanel();
        //buttonBoxouter.setBorder(BorderFactory.createCompoundBorder(eBorder1,leBorder));
        buttonBoxouter.setBorder(eBorder1);
        buttonBoxouter.setLayout(new BorderLayout());
        buttonBoxouter.add(buttonBox,BorderLayout.CENTER);

        buttonBox.add(authButton);
        buttonBox.add(mangButton);
        //buttonBox.add(submButton);
        //buttonBox.add(prefButton);
        buttonBox.add(submGuiButton);
        buttonBox.add(moleditorGuiButton);
        // commented nik buttonBox.add(nanocadButton);
        buttonBox.add(inputGeneratorGuiButton);
        if (!Settings.WEBSERVICE) {
            buttonBox.add(usageButton);
        }
        buttonBox.add(readButton);
        buttonBox.add(licenseButton);
        buttonBox.add(helpButton);
        buttonBox.add(exitButton);


        final ImageIcon logo = new ImageIcon(Env.getGridChemLogoLocation());
         
        //RSSDisplayPanel rdp = new RSSDisplayPanel();
        parseRSS prss = new parseRSS();
        String imgtext = "<img src=\"File:///"+Env.getGridChemLogoLocation()+"\" height=50 width=50>";
        //System.out.println("Image info "+ Env.getGridChemLogoLocation()+" "+imgtext);
        String textinfo1 = "<div style=\"background-color:#E7EEF6; color:#000000\">" +
                "<div style=\"background-color:#A7B3C7; color:#FFFFFF;\">" +
                imgtext + "<font size=5> Welcome to GridChem: " +
                "Portal to the Computational Chemistry Grid!! </font>" +
                "<p><br></div>"  +
                "You are running the " +
                "<Font color='green'>AXIS2 Web Service </font>" +
                "version of the client with the <Font color='blue'>" +
                ((Settings.DEVEL)?"DEVELOPMENT":
                    ((Settings.FAILOVER)?"FAILOVER":
                        ((Settings.LOCAL)?"LOCAL":"PRODUCTION"))) +
                " </font> cyberinfrastructure. <p>" +                  
                "Developed by: CCS(UKy), CCT(LSU), NCSA(UIUC), OSC(OSU), and TACC(UTAustin).\n\n<p>";
        String textinfo2 ="For more information, " +
        "please visit http://www.gridchem.org/</div>";
        URL rssurl;  
        try {
        rssurl = new URL(Invariants.CCGRSSFeed);
        dyninfoPane = new JTextPane();
        dyninfoPane.setEditorKit( new HTMLEditorKit() );
        dyninfoPane.setText(textinfo1+prss.parseRSS(rssurl)+textinfo2);
        dyninfoPane.setCaretPosition(0);
    
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //messageBoard.setEditable(false);
        JScrollPane jscrollpaned = new JScrollPane(dyninfoPane);
        jscrollpaned.setWheelScrollingEnabled(true);
        jscrollpaned.setPreferredSize(new Dimension(550,450));
        jscrollpaned.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5,5,5,5),
                                    jscrollpaned.getBorder()));
        //lixh_add for automatic moving the scrollbar
        
            //jscrollpane.setWheelScrollingEnabled(true);
        messageBoard = new JTextArea("",5,55){};
        JScrollPane jscrollpane = new JScrollPane(messageBoard);
        messageBox.add(jscrollpaned);    
        messageBox.add(jscrollpane);
    
        // set up the layout of the buttons
        setBorder(BorderFactory.createEmptyBorder(25,25,25,25)); //lixh_add
            setLayout(new BoxLayout(this,BoxLayout.X_AXIS)); //lixh_add
    
        // add each box to the layout
        add(messageBox);       
        add(buttonBoxouter);
    
    
        // listen for an action for each button
        authButton.addActionListener(this);
        readButton.addActionListener(this);
        prefButton.addActionListener(this);
        //submButton.addActionListener(this); commented nik
        mangButton.addActionListener(this);
        usageButton.addActionListener(this);
        licenseButton.addActionListener(this);
        exitButton.addActionListener(this);
        helpButton.addActionListener(this);
        // commented nik nanocadButton.addActionListener(this);
        
        updateAuthenticatedStatus();
    }

    private DropDownButton createMolEdDDB() {
        PopupListener popupListener = new PopupListener();
        
        moleditorGuiButton = new DropDownButton("Open Nano CAD GUI");
        moleditorGuiButton.getButton().setToolTipText(
                "Open the Nanocad Molecualr Editor.");
        nanoMenuItem = new ApplicationMenuItem("Nano CAD",KeyEvent.VK_5);
        nanoMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_5, ActionEvent.ALT_MASK));
        nanoMenuItem.getAccessibleContext().setAccessibleDescription(
                "Opens the Nano CAD Molecular Editor");
        nanoMenuItem.addActionListener(popupListener);
        
        // begins: added for nanocad 3d - Narendra Kumar Polani (CCS, UKY)        
        nano3dMenuItem = new ApplicationMenuItem("NanoCAD 3D",KeyEvent.VK_A);
        nano3dMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_A, ActionEvent.ALT_MASK));
        nano3dMenuItem.getAccessibleContext().setAccessibleDescription(
                "Opens the NanoCAD 3D Molecular Editor");
        nano3dMenuItem.addActionListener(popupListener);
        // end: added for nanocad 3D
        
        javaMenuItem = new ApplicationMenuItem("Java ANU",KeyEvent.VK_6);
        javaMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_6, ActionEvent.ALT_MASK));
        javaMenuItem.getAccessibleContext().setAccessibleDescription(
                "Opens the Java ANU Molecular Editor");
        javaMenuItem.addActionListener(popupListener);
        
        molMenuItem = new ApplicationMenuItem("Mol Den",KeyEvent.VK_7);
        molMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_7, ActionEvent.ALT_MASK));
        molMenuItem.getAccessibleContext().setAccessibleDescription(
                "Opens the Mol Den Molecular Editor");
        molMenuItem.addActionListener(popupListener);
        
        gdisMenuItem = new ApplicationMenuItem("GDIS",KeyEvent.VK_8);
        gdisMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_8, ActionEvent.ALT_MASK));
        gdisMenuItem.getAccessibleContext().setAccessibleDescription(
                "Opens the GDIS Molecular Editor");
        gdisMenuItem.addActionListener(popupListener);
        
        jmolMenuItem = new ApplicationMenuItem("JMol",KeyEvent.VK_9);
        jmolMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_9, ActionEvent.ALT_MASK));
        jmolMenuItem.getAccessibleContext().setAccessibleDescription(
                "Opens the JMol Molecular Editor");
        jmolMenuItem.addActionListener(popupListener);
        
        tubeMenuItem = new ApplicationMenuItem("Tube Gen",KeyEvent.VK_0);
        tubeMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_0, ActionEvent.ALT_MASK));
        tubeMenuItem.getAccessibleContext().setAccessibleDescription(
                "Opens the Tube Gen Molecular Editor");
        tubeMenuItem.addActionListener(popupListener);

        moleditorGuiButton.getMenu().add(nano3dMenuItem);	// added for nanocad 3d - Narendra Kumar Polani (CCS, UKY)
        moleditorGuiButton.getMenu().add(nanoMenuItem);
        moleditorGuiButton.getMenu().add(javaMenuItem);
        moleditorGuiButton.getMenu().add(molMenuItem);
        moleditorGuiButton.getMenu().add(gdisMenuItem);
        moleditorGuiButton.getMenu().add(jmolMenuItem);
        moleditorGuiButton.getMenu().add(tubeMenuItem);
        
        return moleditorGuiButton;
    }
    
    private DropDownButton createJobDDB() {
        PopupListener popupListener = new PopupListener();
        
        submGuiButton = new DropDownButton("Create Job");
        submGuiButton.getButton().setToolTipText(
                "Open the Job/Work Flow Editor.");
        jobMenuItem = new ApplicationMenuItem("Job",KeyEvent.VK_3);
        jobMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_3, ActionEvent.ALT_MASK));
        jobMenuItem.getAccessibleContext().setAccessibleDescription(
                "Opens the Job Editor");
        jobMenuItem.addActionListener(popupListener);
        
        flowMenuItem = new ApplicationMenuItem("Work Flow",KeyEvent.VK_4);
        flowMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_4, ActionEvent.ALT_MASK));
        flowMenuItem.getAccessibleContext().setAccessibleDescription(
                "Opens the Work Flow Editor");
        flowMenuItem.addActionListener(popupListener);
              
        submGuiButton.getMenu().add(jobMenuItem);
        submGuiButton.getMenu().add(flowMenuItem);
        
        return submGuiButton;
    }
    
    private DropDownButton createDDB() {
        PopupListener popupListener = new PopupListener();
        
        inputGeneratorGuiButton = new DropDownButton("Open Gaussian GUI");
        inputGeneratorGuiButton.getButton().setToolTipText(
                "Open the Gaussian input builder.");
        gaussianMenuItem = new ApplicationMenuItem("Gaussian",KeyEvent.VK_1);
        gaussianMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        gaussianMenuItem.getAccessibleContext().setAccessibleDescription(
                "Opens the Gaussian GUI");
        gaussianMenuItem.addActionListener(popupListener);
        
        gamessMenuItem = new ApplicationMenuItem("GAMESS",KeyEvent.VK_2);
        gamessMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, ActionEvent.ALT_MASK));
        gamessMenuItem.getAccessibleContext().setAccessibleDescription(
                "Opens the GAMESS GUI");
        gamessMenuItem.addActionListener(popupListener);
              
        inputGeneratorGuiButton.getMenu().add(gaussianMenuItem);
        inputGeneratorGuiButton.getMenu().add(gamessMenuItem);
        
        return inputGeneratorGuiButton;
    }

    public class PopupListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            ApplicationMenuItem item = (ApplicationMenuItem)event.getSource();
            
            if(item.equals(gaussianMenuItem)){
            	stuffInside.selectedGUI = 1;
            	showNewGUI();
            	gaussianMenuItem.setLastSelected(true);
                gamessMenuItem.setLastSelected(false);
            } else if (item.equals(gamessMenuItem)){
            	stuffInside.selectedGUI=1;
            	GamessGUI.main(null);
                gaussianMenuItem.setLastSelected(false);
                gamessMenuItem.setLastSelected(true);
            } 
            else if(item.equals(nano3dMenuItem)){
            	// added for nanocad 3d - Narendra Kumar Polani (CCS, UKY)
            	selectedFrontPanel = 1;
            	doCallNanoCad3D();            	
            }            
            else if (item.equals(nanoMenuItem)) {
            	//jayeeta added following two lines. 
            	System.out.println("Launching Nanocad Molecular Editor");
                selectedFrontPanel=1;
            	doCallNanocad();
      
            } else if (item.equals(javaMenuItem)){
            	doCallJavaANU();
     	  
            } 
            else if (item.equals(molMenuItem))
        	{
            	doCallMolDen();
        	}
            else if (item.equals(gdisMenuItem))
        	{
            	doCallGDIS();
        	}
            else if (item.equals(jmolMenuItem))
        	{
            	doCallJMol();
        	}
            else if (item.equals(tubeMenuItem))
        	{
            	doCallTubeGen();
        	}
            else if (item.equals(jobMenuItem)){//else if (e.getSource() == submButton) {
                CheckAuth ca = new CheckAuth();
                if (ca.authorized) {
                    if (Settings.WEBSERVICE) {
//                        if(sjw == null) {
//                            doSubmission();
//                        } else {
//                            SubmitJobsWindow.frame.setVisible(true);
//                        }
                        SubmitJobsWindow.getInstance();
                    }
//                    else {
//                        if (GetFile.getfileisdone && LoginPanel.isprefFile) {
//                            SubmitJobsWindow.getInstance();
//                        } else {
//                            JOptionPane.showMessageDialog(
//                                this,
//                                "Getting preferences log file " +
//                                "(preferences.hist)... Please wait\n",
//                                "Get Preferences",
//                                JOptionPane.INFORMATION_MESSAGE
//                                );
//                        }
//                    }
                } else {
                    doWarning();
                }
            } else if (item.equals(flowMenuItem)){
            	//final String[] newarray = new String[14];
            	//The following try block added to read the proxy information from a
            	//file instead of hardcoding it here.. -nikhil dec 14 2009
            	/*try{
                FileInputStream fis = new FileInputStream(Env.getApplicationDataDir() + 
          	       		Settings.fileSeparator + "xbaya_proxy");
                DataInputStream dis = new DataInputStream(new BufferedInputStream(fis));
            	
            	for(int i =0; i < 14; i++){
            		newarray[i]=dis.readLine();
            	}
            	dis.close();
            	fis.close();
            	//System.out.println("this is new array "+newarray[10]);
            	}catch (FileNotFoundException ie) {
            	newarray[0] = "-exitOnClose";
            	newarray[1] = "false";
            	newarray[2] = "-myProxyServer";
            	newarray[3] = "portal.leadproject.org";
            	newarray[4] = "-myProxyUsername";
            	newarray[5] = "gridchem";
            	newarray[6] = "-myProxyPassphrase";
            	newarray[7] = "gc2009";
            	newarray[8] = "-xRegistryURL";
            	newarray[9] = "https://gw26.quarry.iu.teragrid.org:6666/xregistry";
            	newarray[10] = "-gfacRegistryURL";
            	newarray[11] = "https://tyr16.cs.indiana.edu:23443";
            	newarray[12] = "-gfacURL";
            	newarray[13] = "https://tyr16.cs.indiana.edu:23443";
            	ie.printStackTrace();
                }
            	catch (Exception ie) {System.out.println("Exception in optsComponent"+ie);}*/
            
            	CheckAuth ca = new CheckAuth();
            	if (ca.authorized) { 		
            		try {
						Process p = Runtime.getRuntime().exec("javaws " + Invariants.XBayaJnlpURL);
						System.out.println("Exit XBaya");
					} catch (IOException e) {
						e.printStackTrace();
					}
                }
                else {
                	doWarning();
                }
            }            
            
            if(item.equals(nanoMenuItem)||item.equals(javaMenuItem)
            		||item.equals(molMenuItem)||item.equals(gdisMenuItem)
            		||item.equals(jmolMenuItem)||item.equals(tubeMenuItem)){
    	            moleditorGuiButton.getButton().setText("Open " + item.getText() + " GUI");
    	            moleditorGuiButton.getButton().setToolTipText("Open " + item.getText() + " GUI");
                } else if(item.equals(gaussianMenuItem)||item.equals(gamessMenuItem)){
    	            inputGeneratorGuiButton.getButton().setText("Open " + item.getText() + " GUI");
    	            inputGeneratorGuiButton.getButton().setToolTipText("Open " + item.getText() + " GUI");
                }
                else if(item.equals(jobMenuItem)){
                	submGuiButton.getButton().setText("Create Job");
                	submGuiButton.getButton().setToolTipText("Open Job Editor GUI");
                }
                else if(item.equals(flowMenuItem)){
                	submGuiButton.getButton().setText("Create Work Flow");
                	submGuiButton.getButton().setToolTipText("Open Work Flow Editor GUI");
                }
            //inputGeneratorGuiButton.getButton().setText("Open " + item.getText() + " GUI");
            //inputGeneratorGuiButton.getButton().setToolTipText("Open " + item.getText() + " GUI");
        }
    }
    
    public static void showNewGUI() {  
	    JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		
		InputFile.tempinput = new String();
		InputfileReader.route = new String();
		showMolEditor.tempmol = new String();
		InputFile.inputfetched = 0;
		InputfileReader.chrgStr=null;
		InputfileReader.mulStr=null;
        
        mainFrame = new G03Input.G03MenuTree();
	    mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.pack();
		
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		mainFrame.setSize(screenSize.width-200,screenSize.height-150);
		mainFrame.setResizable(true);
		mainFrame.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        Trace.entry();
        if (e.getSource() == authButton) {
            // Authentication is handled a bit differently now
            // Here when a user selects to reauthenticate, we
            // wipe their entire session clean of preferences,
            // Settings variables, their VO, projects, and 
            // their session EPR.  We also reset every panel. 
            // This is done in LoginPanel.clearLogin().
            if (Settings.authenticated) {
                int result = JOptionPane.showConfirmDialog(
                    this,
                    "Do you really want to disconnect from "+
                    "this \n resource and authenticate to " +
                    "another resource?",
                    "Authentication",
                    JOptionPane.YES_NO_OPTION
                    );
                if (result == 0) {
                    GridChem.appendMessage("Resetting user authentication...");
                    
                    LoginDialog.clearLogin();
                    
                    GridChem.appendMessage("Complete\n");
                    
                    updateAuthenticatedStatus();
                    doAuthentication();
                }
            } else {
                updateAuthenticatedStatus();
                doAuthentication();
            }
        } else if (e.getSource() == readButton) {
            if (Settings.WEBSERVICE) {
                if(rssw == null) {
                    doReadAnnouncements();
                } else {
                    rssw.frame.setVisible(true);
                }
            } else {
                doLaunchBrowser();
            }
        } else if (e.getSource() == prefButton) {
            CheckAuth ca = new CheckAuth();
            if (ca.authorized) {
                if (Settings.WEBSERVICE) {
                    if (pw == null) {
                        doPreferences();
                    } else {
                        PreferencesWindow.frame.setVisible(true);
                    }
                
//                } else {
//                    Trace.note( "GetFile.getfileisdone = " + GetFile.getfileisdone);
//                    Trace.note( "LoginPanel.isprefFile = " + LoginPanel.isprefFile);
//                    if (GetFile.getfileisdone && LoginPanel.isprefFile) {
//                        if (pw == null) {
//                            doPreferences();
//                        } else {
//                            PreferencesWindow.frame.setVisible(true);
//                        }
//                    } else {
//                    JOptionPane.showMessageDialog(
//                        this,
//                        "Getting preferences log file " +
//                        "(preferences.hist)... Please wait\n",
//                        "Get Preferences",
//                        JOptionPane.INFORMATION_MESSAGE
//                        );
//                    }
                } 
            }else {
                doWarning();
            }
//        }else if (e.getSource() == usageButton) {
//            // Usage button in only in pre-ws client.  in ws client, usage info
//            // is in the MonitorVO panel.
//            CheckAuth ca = new CheckAuth();
//            if (ca.authorized) {
//                Trace.note( "GetFile.getfileisdone = " + GetFile.getfileisdone);
//                if (GetFile.getfileisdone && LoginPanel.isprefFile) {
//                    if (acw == null) {
//                        acw = new Accounting(LoginStage1.reply);
//                        acw.pack();
//                        acw.setVisible(true);
//                    } else {
//                        acw.setVisible(true);
//                    }
//                } else {
//                JOptionPane.showMessageDialog(
//                    this,
//                    "Getting usage log file " +
//                    "... Please wait\n",
//                    "Get Usage Table",
//                    JOptionPane.INFORMATION_MESSAGE
//                    );
//                }
//            } else {
//                doWarning();
//            }
        } else if (e.getSource() == mangButton) {
            /* Expose the monitoring and file management utilities
             * contained in the ManageJobs or MonitorVO Panel depending
             * on client version.
             */
            CheckAuth ca = new CheckAuth();
            if (ca.authorized) {
                if (Settings.DEBUG) {
                    System.out.println("mw = "+mw+"\n");
                }
                if (Settings.WEBSERVICE) {
                    if(GridChem.user != null) {
                        doMonitor();
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "Monitoring information not available. \n" +
                            "User did not successfully log into the CCG.",
                            "Authentication Failed",
                            JOptionPane.INFORMATION_MESSAGE
                            );
                    }
                } 
//                else {
//                    if (GetFile.getfileisdone && LoginPanel.ishistFile) {
//                        doManagement();
//                    } else {
//                        JOptionPane.showMessageDialog(
//                            this,
//                            "Getting job log file(qcrjm.hist)... " +
//                            "Please wait\n",
//                            "Get log file",
//                            JOptionPane.INFORMATION_MESSAGE
//                            );
//                    }
//                }
            } else {
                doWarning();
            }
        }
        
        else if (e.getSource() == licenseButton) {
            doLicense();        
        } else if (e.getSource() == helpButton) { 
            doHelp();
        } else if (e.getSource() == exitButton) {
            int result1 = JOptionPane.showConfirmDialog(
                    this,
                    "Do you really want to close the client? ",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
                    );
            if (result1==0) {
            	if (Settings.authenticated) { 
            		GMS3.logout();
            	}
            	doShutdown();
            }
        } 
        
        /*
        else if (e.getSource() == nanocadButton) {
            System.out.println("Launching Nanocad Molecular Editor");
            CheckAuth ca = new CheckAuth();
            selectedFrontPanel=1;
            //if (ca.authorized) {
                if (Settings.WEBSERVICE)
                    doCallNanocad();
//                else {
//                    if (GetFile.getfileisdone && LoginPanel.ishistFile) {
//                        doCallNanocad();
//                    } else {
//                        JOptionPane.showMessageDialog(
//                            this,
//                            "Getting job log file(qcrjm.hist)... " +
//                            "Please wait\n",
//                            "Get log file",
//                            JOptionPane.INFORMATION_MESSAGE
//                            );
//                    }
//                }
           // } else {
           //     doWarning();
            //}
        }*/
        Trace.exit();
    }  // End of public void actionPerformed(ActionEvent e)

    public void setAuthButton(String text, String tooltiptext) {
        authButton.setText(text);
        authButton.setToolTipText(tooltiptext);
    }
    
    public void doShutdown() {
//        if(lp != null) lp.setVisible(false);
        if(acw != null) acw.setVisible(false);
        if(pw != null) pw.setVisible(false);
        if(sjw != null) sjw.setVisible(false);
        if(mw != null) mw.setVisible(false);
        if(monitorWindow != null) monitorWindow.setVisible(false);
        if(sdw != null) sdw.si.setVisible(false);
        if(rssw != null) rssw.setVisible(false);
        if(nanWindow != null) nanWindow.setVisible(false);
        System.exit(0);
    }
    /**
     * Open the authentication panel where users can authenticate
     * to the GridChem middleware server. This must be the first
     * action the user takes in order to perform accountable 
     * actions with the client. Since the LoginPanel is disposed 
     * at shutdown and when the user confirms they wish to re-
     * authenticate, we get a new instance of LoginPanel every time
     * this is called.
     *
     */
    public void doAuthentication()
    {
    	messageBoard.append("Authentication requested...\n");
        messageBoard.setCaretPosition( messageBoard.getDocument().getLength()); //lixh_add
        
        new LoginDialog(mainFrame, true);
    }

    /**
     * Open the announcement panel where the user can find
     * new and exciting information on GridChem via RSS from
     * the gridchem website.
     *
     */
    public void doReadAnnouncements() {
        /*messageBoard.append("Reading announcements...\n");*/
        //BareBonesBrowserLaunch.openURL("http://www.gridchem.org");
        rssw = new RSSViewer(Invariants.CCGRSSFeed);
        //rssw.setSize(new Dimension(350,400));
    }
    
    /**
     * pre-ws client launches a browser to veiw announcements.
     */
    public void doLaunchBrowser() {
        BareBonesBrowserLaunch.openURL("http://www.gridchem.org");
    }

    /**
     * Open user preferences pane where user can specify the look and feel
     * they wish to remember from session to session.
     *
     */
    public void doPreferences()
    {
        if(pw == null) {
            pw = new PreferencesWindow();
        } else {
            pw.setVisible(true);
        }
        messageBoard.append("Checking preferences...\n");
        messageBoard.setCaretPosition( messageBoard.getDocument().getLength()); //lixh_3/27/06
        
    }

//    /**
//     * Open submit jobs window where user can submit a job
//     * based on their authentication method.
//     *
//     */
//    public void doSubmission()
//    {
//        if(sjw == null) {
//            sjw = SubmitJobsWindow.getInstance();
//        } else {
//            sjw.setVisible(true);
//        }
//        messageBoard.append("Submit Jobs Window opened...\n");
//        messageBoard.setCaretPosition( messageBoard.getDocument().getLength()); //lixh_add
//    }

    /**
     * Open manage job window where the user can monitor jobs
     * they have submitted with the client and manage the data
     * associated with each job through our file browser and
     * output parsers.
     *
     */
    public void doManagement() 
    {
        if(mw == null)
        {
            if(Settings.authenticatedSSH) {
//                    mw = new SSHManageWindow();
            } else {
                mw = new ManageWindow();
            }
        } else {
            mw.setVisible(true);
        }
        messageBoard.append("Manage Jobs window opened...\n");
        messageBoard.setCaretPosition( messageBoard.getDocument().getLength());
    }

    /**
     * Open tabbed pane displaying job, resource, and usage information for the 
     * user's current VO.
     * 
     */
    public void doMonitor() {
        if(monitorWindow == null) {
            monitorWindow = new MonitorVO();
        } else {
            monitorWindow.refresh();
            monitorWindow.setVisible(true);
        }
        GridChem.appendMessage("Monitoring window opened...\n");
    }
    
    /**
     * Download remote data for viewing.  
     * 
     * @deprecated This method is deprecated as of GridChem 0.3.
     */
    public void doExplore() 
    {
        messageBoard.append("Explore data...\n");
        JOptionPane.showMessageDialog( null,
                "Coming soon...","explore data",
                JOptionPane.INFORMATION_MESSAGE);
    }

    
    /**
     * Display GridChem public license in dialog box.
     */
    public void doLicense() {
        String message = "GridChem: Portal to the Computational Chemistry Grid!!\n\n" +
            "Developed by: \n\n" +
            "CCS, University of Kentucky\n" +
            "CCT, Louisiana State University\n" +
            "NCSA, University of Illinois at Urbana-Champaign\n" +
            "OSC, Ohio Supercomputer Center\n" +
            "TACC, University of Texas at Austin\n\n" +
            "http://www.gridchem.org/\n\n" +
            "Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.\n\n" +
            "Permission is hereby granted, free of charge, to any person obtaining a copy \n" +
            "of this software and associated documentation files (the \"Software\"),to deal with \n" +
            "the Software without restriction, including without limitation the rights to use, \n" +
            "copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the \n" +
            "Software, and to permit persons to whom the Software is furnished to do so, \n" +
            "subject to the following conditions:\n\n" +
            "1. Redistributions of source code must retain the above copyright notice, \n" +
            "   this list of conditions and the following disclaimers.\n" +
            "2. Redistributions in binary form must reproduce the above copyright notice, \n" +
            "   this list of conditions and the following disclaimers in the documentation \n" +
            "   and/or other materials provided with the distribution.\n" +
            "3. Neither the names of Chemistry and Computational Biology Group , NCSA, \n" +
            "   University of Illinois at Urbana-Champaign, nor the names of its contributors \n" +
            "   may be used to endorse or promote products derived from this Software without \n" +
            "   specific prior written permission.\n\n" +
            "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY\n" +
            "KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE\n" +
            "WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR\n" +
            "PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
            "CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,\n" +
            "DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,\n" +
            "TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH\n" + 
            "THE SOFTWARE OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.\n\n";
            
        ScrollableDisplay sd = new ScrollableDisplay("GridChem License", message);
        sd.setVisible(true);
        /*JOptionPane.showMessageDialog(null,
                "Welcome to GridChem: Portal to the Computational Chemistry Grid!!\n" +
                    "Developed by: \n\n" +
                    "CCS, University of Kentucky\n" +
                    "CCT, Louisiana State University\n" +
                    "NCSA, University of Illinois at Urbana-Champaign\n" +
                    "OSC, Ohio Supercomputer Center\n" +
                    "TACC, University of Texas at Austin\n\n" +
                    "https://www.gridchem.org/\n\n" +
                    "Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.\n\n" +
                    "Permission is hereby granted, free of charge, to any person obtaining a copy of " +
                    "this software and associated documentation files (the \"Software\"),to deal with " +
                    "the Software without restriction, including without limitation the rights to use, " +
                    "copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the " +
                    "Software, and to permit persons to whom the Software is furnished to do so, " +
                    "subject to the following conditions:\n\n" +
                    "1. Redistributions of source code must retain the above copyright notice, \n" +
                    "   this list of conditions and the following disclaimers.\n" +
                    "2. Redistributions in binary form must reproduce the above copyright notice, \n" +
                    "   this list of conditions and the following disclaimers in the documentation \n" +
                    "   and/or other materials provided with the distribution.\n" +
                    "3. Neither the names of Chemistry and Computational Biology Group , NCSA, \n" +
                    "   University of Illinois at Urbana-Champaign, nor the names of its contributors \n" +
                    "   may be used to endorse or promote products derived from this Software without \n" +
                    "   specific prior written permission.\n\n" +
                    "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, " +
                    "EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF " +
                    "MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT." +
                    "IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR " +
                    "ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, " +
                    "TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE " +
                    "OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.\n\n",
                    "GridChem Public License",
                    JOptionPane.INFORMATION_MESSAGE);*/
    }
    
    /**
     * Throw up an authentication warning.  This should be handled in
     * an error code.
     */
    public void doWarning()
    {
        JOptionPane.showMessageDialog(null, "You must authenticate " +
            "to use this function", "GridChem", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Delete the files on the local system associated with the job.
     * @param directoryFile
     */
    public void doDeletion(File directoryFile)
    {
        java.util.List list = Arrays.asList(directoryFile.listFiles());
    
        ArrayList directories = new ArrayList(list);
        ArrayList toBeDeleted = new ArrayList();
        ArrayList dirsToBeDeleted = new ArrayList();
     
        for (int i = 0; i < directories.size(); i++) {
            File f = (File) directories.get(i);
            if (f.isDirectory() == true) { 
                dirsToBeDeleted.add(f);
                doDeletion(f);
            } else {
                toBeDeleted.add(f);
            }
        }
        
        for (int i = 0; i < dirsToBeDeleted.size(); i++) {
            File g = (File) dirsToBeDeleted.get(i);
            System.err.println("doDeletion: deleting file " + 
                    g.getName());
            g.delete();
        }
        
        for (int i = 0; i < toBeDeleted.size(); i++) {
            File t = (File) toBeDeleted.get(i);
            System.err.println("doDeletion: deleting file " + 
                    t.getName());
            t.delete();
        }
    }
    
    public void doHelp() {
        if (helpBrowser == null) {
            helpBrowser = new HelpBrowser();
        } else {
            helpBrowser.showHelp();
        }
    }
    
    
	/*Added nikhil*/
	
	public void doCallMolDen()
	{
		JOptionPane.showMessageDialog(null, "doCallMolGen"); 
		try {
			Runtime.getRuntime().exec(".\\etc\\MolDen\\molden_windows_nt_95\\molden.exe");
		}
	    catch (Exception err) {
	    	err.printStackTrace();
	    }
	}
	public void doCallGDIS()
	{
		//JOptionPane.showMessageDialog(null, "doCallGDIS"); 
		try {
			Runtime.getRuntime().exec(".\\etc\\GDIS\\gdis\\gdis.exe");
		}
	    catch (Exception err) {
	    	err.printStackTrace();
	    }
	}
	public void doCallJMol()
	{
		try {
			String[] cmdarray = { "javaws", 
					Invariants.JmolJnlpURL};
			Runtime.getRuntime().exec(cmdarray);
		}
	    catch (Exception err) {
	    	err.printStackTrace();
	    }
	}
	public void doCallTubeGen()
	{
		
		JOptionPane.showMessageDialog(null, "doCallTubeGen"); 
		try {
			Runtime.getRuntime().exec(".\\etc\\TubeGen\\tubegen.exe");
		}
	    catch (Exception err) {
	    	err.printStackTrace();
	    }
	}
	public void doCallJavaANU()
	{
		try {
			String[] cmdarray = { "javaws", 
					Invariants.JamberooJnlpURL};
			Runtime.getRuntime().exec(cmdarray);
		}
	    catch (Exception err) {
	    	err.printStackTrace();
	    }
	}
	
	/* End additions */
	
	
	public void doCallNanoCad3D() {
		Trace.entry();
        System.out.println(" Calling NanoCAD 3D");
        String setsfile = ".settings";
        boolean append = false;
        File sets = new File(Settings.defaultDirStr + Settings.fileSeparator
                + setsfile);
        try {
            FileWriter fw = new FileWriter(sets, append);
            fw.write("Username= " + Settings.name.getText() + "\n");
            fw.write("CGI= " + Invariants.httpsGateway + "\n");
            fw.close();
            FileWriter fw2 = new FileWriter(Settings.defaultDirStr
                    + Settings.fileSeparator + "loadthis", append);
            fw2.write(Settings.defaultDirStr + Settings.fileSeparator
                    + "common" + Settings.fileSeparator + "Molecule"
                    + Settings.fileSeparator + "Inorganic"
                    + Settings.fileSeparator + "water.pdb\n");
            fw2.close();
        } catch (IOException ioe) {
        }
        
        String tmpfile = "tmp.txt";
        File fa = new File(Env.getApplicationDataDir() + Settings.fileSeparator
                + tmpfile);
        
        if (fa.exists()) {
            fa.delete();
        }        
                
        // launch nanocad
        if (Settings.VERBOSE) System.out.println("Calling NanoCAD 3D Main");
        
        nano3DWindow = new Nanocad3D();
        nano3DWindow.setVisible(true);        
        
        nano3DWindow.addWindowListener(this);
        nano3DWindow.addComponentListener(this);
        //Nanocad3D.glcanvas.requestFocusInWindow();

        System.out.println(" Done with NanoCAD 3D");
        Trace.exit();
    }
	
    public void doCallNanocad() {
        System.out.println(" Calling Nanocad");
        
        String setsfile = ".settings";
        boolean append = false;
        File sets = new File(Settings.defaultDirStr + Settings.fileSeparator
                + setsfile);
        try {
            FileWriter fw = new FileWriter(sets, append);
            fw.write("Username= " + Settings.name.getText() + "\n");
            fw.write("CGI= " + Invariants.httpsGateway + "\n");
            fw.close();
            FileWriter fw2 = new FileWriter(Settings.defaultDirStr
                    + Settings.fileSeparator + "loadthis", append);
            fw2.write(Settings.defaultDirStr + Settings.fileSeparator
                    + "common" + Settings.fileSeparator + "Molecule"
                    + Settings.fileSeparator + "Inorganic"
                    + Settings.fileSeparator + "water.pdb\n");
            fw2.close();
        } catch (IOException ioe) {
        }
        
        String tmpfile = "tmp.txt";

        File fa = new File(Env.getApplicationDataDir() + Settings.fileSeparator
                + tmpfile);
        
        if (fa.exists()) {
            fa.delete();
        }
        
        // launch nanocad
        if (Settings.VERBOSE) System.out.println("Calling nanocadMain");
        
        nanWindow = new nanocadFrame2();
        
        nanWindow.addWindowListener(this);
        
        nanWindow.nano.addComponentListener(this);

        System.out.println(" Done with Nanocad");
    }
    
    public void changeInputText(String i) {
    	try {
        // this.inputText = new JTextArea(i, 20,40);
        //inputText.selectAll();
        //inputText.replaceSelection(i);
        //inputText.setCaretPosition(0);
        // inputText.append(i);
    	
    	//jayeeta added following lines
    	showMolEditor.tempmol=i;
		//Set the Label in G03MenuTree
		G03MenuTree.nanocadNotice.setText("Molecular Specification Imported from Nanocad");
		
		
			//tempmol+="\n"+i;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    }
    
//    public void enableButtons(boolean enable) {
//        readButton.setEnabled(enable);
//        prefButton.setEnabled(enable);
//        submButton.setEnabled(enable);
//        saveButton.setEnabled(enable);
//        nanocadButton.setEnabled(enable);
//        licenseButton.setEnabled(enable);
//        helpButton.setEnabled(enable);
//        mangButton.setEnabled(enable);
//        if (!Settings.WEBSERVICE) {
//            usageButton.setEnabled(enable);
//        }
//    }
    
    public void updateAuthenticatedStatus() {
        prefButton.setEnabled(Settings.authenticated);
        mangButton.setEnabled(Settings.authenticated);
        // commented nik submButton.setEnabled(Settings.authenticated);
        //submGuiButton.setEnabled(Settings.authenticated);
        submGuiButton.setEnabled(true);
        if (!Settings.WEBSERVICE) {
            usageButton.setEnabled(Settings.authenticated);
        }
        
        if (Settings.authenticated) {
            setAuthButton("Sign Out",
                          "<html><p>Disconnect from the CCG.</p><html>");
        } else {
            setAuthButton("Sign In",
            "<html><p>Authenticate to the CCG.</p><html>");
        }
    }
    
    /* WindowListener interface definition methods */
    public void windowOpened(WindowEvent e) {}

    public void windowClosed(WindowEvent e) {}

    public void windowIconified(WindowEvent e) {}
    
    public void windowDeiconified(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}

    public void windowDeactivated(WindowEvent e) {}
    
    public void windowClosing(WindowEvent e) {
    	
    	Trace.entry();    	
    	String name = "";
        // check for temp file and if it exists, load into text box
        System.err.println("editingStuff:load tmp.txt file here!");

        // File f = new File(Settings.defaultDirStr +
        File f = new File(Env.getApplicationDataDir() + Settings.fileSeparator
                + "tmp.txt");
        if ((f.exists()))// && !(f.isEmpty()))
        {
            try {
                BufferedReader inStream = new BufferedReader(new FileReader(f));
                String text = "";
                String line;
                while ((line = inStream.readLine()) != null) {
                    int n = line.length();
                    if (n > 0) {
                        text = text + line + "\n";
                        System.err.println(line);
                    }
                }
                inStream.close();
                changeInputText(text);
            } catch (IOException ioe) {
                System.err.println("IOException in editJobPanel");
            }
        }
        
        name = e.getWindow().getClass().getName();
        name = name.substring(name.lastIndexOf(".")+1);
        if (name.equalsIgnoreCase("Nanocad3D")) {
        	
        	nano3DWindow.dispose();        	
        	Nanocad3D.glcanvas.setVisible(false);
        	/*if(nano3DWindow.glcanvas != null){
        			nano3DWindow.glcanvas.setVisible(false);
        	}*/        	
        	nanbool = false;
        	optsComponent.selectedFrontPanel=0;  
            
        }else if (name.equalsIgnoreCase("nanocadFrame2")){
        	nanWindow.dispose();
            if (nanWindow.nano.t != null)
            	nanWindow.nano.t.setVisible(false);
            nanbool = false;
            optsComponent.selectedFrontPanel=0;
        }
        Trace.exit();               
    }

    /* ComponentListener interface definition methods */
    public void componentMoved(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {}
    
    public void componentHidden(ComponentEvent e) {
        System.err.println("load temp file here!");

        // File f = new File(Settings.defaultDirStr +
        File f = new File(Env.getApplicationDataDir() + Settings.fileSeparator
                + "tmp.txt");
        
        if ((f.exists())) {
            try {
                BufferedReader inStream = new BufferedReader(new FileReader(f));
                String text = "";
                String line;
                while ((line = inStream.readLine()) != null) {
                    int n = line.length();
                    if (n > 0) {
                        text = text + line + "\n";
                        System.err.println(line);
                    }
                }
                inStream.close();
                changeInputText(text);
            } catch (IOException ioe) {
                System.err.println("IOException in editJobPanel");
            }
        }
        
        /* if (nanWindow.nano.t.isActive()) {
            if (nanWindow.nano.t.isVisible()) {
                nanWindow.nano.t.setVisible(false);
            }
        }*/
        nanWindow.dispose();
        optsComponent.selectedFrontPanel=0;
        /*
        JOptionPane.showMessageDialog(null, "Go to Nanocad via the 'Submit Jobs' Window to use this option",
            "Information Message", JOptionPane.INFORMATION_MESSAGE);
            */
    }
}
