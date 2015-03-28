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

/* ManageWindow.java  by Rebecca Hartman-Baker
   This is the GUI for when you press the "Manage Jobs" button.
*/

package org.gridchem.client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ProgressMonitor;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import nanocad.nanocadFrame2;

import org.gridchem.client.common.Settings;
import org.gridchem.client.util.Env;

public class ManageWindow extends JobManagementWindow
{
    public static InternalStuff si;
    
    public ManageWindow()
    {
	    //JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame("GridChem: Manage Jobs");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		si = new InternalStuff();
		//	frame.getContentPane().add(new stuffInside(ListOfJobs));
		frame.getContentPane().add(si);
		frame.pack();
		frame.setVisible(true);
		
    }
    
    public static void refresh() {
        frame.getContentPane().remove(si);
        si = new InternalStuff();
        frame.getContentPane().add(si);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args)
    {
       
        UIManager.LookAndFeelInfo[] installed =
            UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < installed.length; i++) {
            System.out.println(installed[i].getName());
        }
        System.out.println(
            "\n The current look and feel is "
            + UIManager.getLookAndFeel().getName());
          
          // The Java look & feel is pretty lame, so we use the native
          // look and feel of the platform we are running on.
          //try {
          //      UIManager.setLookAndFeel(
          //              installed[2].getClassName());
          //      SwingUtilities.updateComponentTreeUI(frame);;
          //  } catch (Exception e) {
                 //Ignore exceptions, which only result in the wrong look and feel.
          // }
          
	    	Settings.getInstance();
	    	frame = new JFrame("GridChem: Manage Jobs");
	    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	si = new InternalStuff();
	    	frame.getContentPane().add(si);
	    	frame.pack();
	    	frame.setVisible(true);
    }
    
}

class InternalStuff extends JComponent implements ListSelectionListener, WindowListener, ComponentListener
{
    JPanel buttonBox;
    Container queueBox;

    nanocadFrame2 nanWin;
    JButton statusButton;
    JButton dataButton;
    JButton editButton;
    JButton killButton;
    JButton retrieveButton; 
    JButton deleteButton;
    JButton cancelButton;

    ButtonListener b;
    ButtonSelectionListener bs;

    public JTable jobBoard;
    public static DefaultTableModel jbModel;
    public static String[] oldJobs;
    public static String HPCsys;
    
    static doStatBetween2 dsb = new doStatBetween2();
    static doVisBetween2 dvb = new doVisBetween2();
    static doKillBetween2 dkb = new doKillBetween2();
    static doRetrieveBetween2 drb = new doRetrieveBetween2();
    private javax.swing.Timer timer;
    
    public static String[] columnNames = {"Date","Time","Research Project","Name",
    		"Machine","Queue","ID"   		
    }; 
	public static int num_column = columnNames.length;//
	



    public InternalStuff()
    {
		statusButton = new JButton("Get Job Status");
		dataButton = new JButton("Monitor Job Output");
		//visButton = new JButton("Visualize Job Output");
		editButton = new JButton("Edit Log File");
		killButton = new JButton("Kill Selected Job"); //lixh_3_3
		retrieveButton = new JButton("Browse Job Output");
		deleteButton = new JButton("Delete Job from List");
		cancelButton = new JButton("Close");
		
		JPanel buttonBox = new JPanel();
		Container jobBox = Box.createVerticalBox();
	
		buttonBox.setLayout(new GridLayout(6,1,5,5));
		buttonBox.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
		JPanel buttonBoxPane = new JPanel();
		Border eBorder1 = BorderFactory.createEmptyBorder(0,10,0,0);
//		Border leBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		buttonBoxPane.setBorder(eBorder1);
		buttonBoxPane.setLayout(new BoxLayout(buttonBoxPane,BoxLayout.Y_AXIS));
		buttonBoxPane.add(buttonBox);
		buttonBox.add(statusButton);
		buttonBox.add(dataButton);
		buttonBox.add(killButton);
		buttonBox.add(retrieveButton);
		buttonBox.add(deleteButton);
		buttonBox.add(cancelButton);
		
		JScrollPane jscrollPane;
		
		// I need something like this here
		//ArrayList nl = oldJobs.getJobNamesList();
		ArrayList<String> nl = new ArrayList<String>();
		//System.out.println(columnNames+"\n");
		jbModel = new DefaultTableModel();
		Object columndata;
		for (int j = 0; j < num_column; j++) {
			columndata = columnNames[j];
			jbModel.addColumn(columndata);
		}

		String fName = Settings.histFilename; //"qcrjm.hist";
		
		File f = new File(fName);
		
		// now retrieve data from said URL and put it into the file
		if (f.exists()) { 
			parseHist(f, nl);
		    int N = nl.size();

		    for (int i = N-1; i >= 0; i--) {
		   		String[] List = parseOldJob((String) nl.get(i));
		   		if (List.length == 7) {
		    			if (List[6] == null) {
		    				Object[] rowdata = {List[0],List[1]," ",List[3],
		    						List[4],List[5],List[2]};
		    				jbModel.addRow(rowdata);
		   			} else {
		    				Object[] rowdata = {List[0],List[1],List[2],
		    						List[3],List[4],List[5],List[6]};
		    				jbModel.addRow(rowdata);
		   			}
		   		}
		    }
		}
	    
		TableSorter sorter = new TableSorter(jbModel);
		jobBoard = new JTable(sorter);
		sorter.setTableHeader(jobBoard.getTableHeader());

		// One thing selected at a time
	    jobBoard.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    if (nl.size() >= 1) jobBoard.setRowSelectionInterval(0,0);
	    jobBoard.setPreferredScrollableViewportSize(new Dimension(600, 100));
		jscrollPane = new JScrollPane(jobBoard); 
		jscrollPane.isWheelScrollingEnabled();
		jscrollPane.setPreferredSize(new Dimension(500,100));
		jobBox.add(jscrollPane);
	
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		add(jobBox);
		add(buttonBoxPane);
	
		// add all event listeners here
		b = new ButtonListener();
		bs = new ButtonSelectionListener(); //lixh_3_14
	
		statusButton.addActionListener(b);
		statusButton.setToolTipText("Find the status of the currently selected job");
		dataButton.addActionListener(b);
		dataButton.setToolTipText("View the convergence progress of the currently selected job");
		//dataButton.setEnabled(false);
		//visButton.addActionListener(b);
		editButton.addActionListener(b);
		editButton.setToolTipText("Edit the log file associated with the currently selected job");
		killButton.addActionListener(b); //lixh_3_4
		killButton.setToolTipText("Kill the currently selected job");
		retrieveButton.addActionListener(b);
		retrieveButton.setToolTipText("Retrieve ouput data from previously run jobs");
		deleteButton.addActionListener(b);
		deleteButton.setToolTipText("Delete the currently selected job from user history");
		cancelButton.addActionListener(b);
		cancelButton.setToolTipText("Close this window");
		
		ListSelectionModel newModel = jobBoard.getSelectionModel();
		newModel.addListSelectionListener(bs);
		jobBoard.setSelectionModel(newModel);
		
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	
    }

    private javax.swing.Timer doKillTimer(){
    	return new javax.swing.Timer(Invariants.ONE_SECOND, new ActionListener(){
    	  	public void actionPerformed(ActionEvent evt) {
        		if (dkb.done()) {
        			timer.stop();
        			killButton.setEnabled(true);
        		}
        		}
        	      });
    }
    
    private javax.swing.Timer doStatTimer(){
    	return new javax.swing.Timer(Invariants.ONE_SECOND, new ActionListener() {
    	public void actionPerformed(ActionEvent evt) {
    		if (dsb.done()) {
    			timer.stop();
    			statusButton.setEnabled(true);
    		}
    		}
    	      });
    }
    
    private javax.swing.Timer doVisTimer(){
    	return new javax.swing.Timer(Invariants.ONE_SECOND, new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
        	if (dvb.done()){
        	timer.stop();
        	dataButton.setEnabled(true);
        	}
        		}
        	      });
    } //
    
//    private javax.swing.Timer doRetrieveTimer(){
//    	return new javax.swing.Timer(Invariants.ONE_SECOND, new ActionListener() {
//        public void actionPerformed(ActionEvent evt) {
//        	if (drb.done()){
//        	timer.stop();
//        	retrieveButton.setEnabled(true);
//        	}
//        		}
//        	      });
//    } //
    
    class ButtonSelectionListener implements ListSelectionListener
	{
    	public void valueChanged(ListSelectionEvent e) 
        {
    	if (e.getValueIsAdjusting() == false)
    	{
    	    if (jobBoard.getSelectedRow() == -1) 
    	    {
    		statusButton.setEnabled(false);
    		dataButton.setEnabled(false);
    		editButton.setEnabled(false);
    		killButton.setEnabled(false); //lixh_3_4
    		retrieveButton.setEnabled(false);
    		deleteButton.setEnabled(false);
    	    }
    	    else 
    	    {
    		statusButton.setEnabled(true);
    		dataButton.setEnabled(true);
    		editButton.setEnabled(true);
    		killButton.setEnabled(true); //lixh_3_4
    		retrieveButton.setEnabled(true);
    		deleteButton.setEnabled(true);
    	    }

    	}
        }

	}//
    
    class ButtonListener implements ActionListener
    {
	public void actionPerformed(ActionEvent e)
	{
	    if (e.getSource() == statusButton) {
	        ((JButton)e.getSource()).setEnabled(false);
			timer = doStatTimer();
			dsb.go();
			timer.start();
	    } else if (e.getSource() == killButton) {
		    ((JButton)e.getSource()).setEnabled(false);
		    	timer = doKillTimer();
		    	dkb.go();
		    	timer.start();
	    } else if (e.getSource() == dataButton) {
	    		if(Settings.authenticatedSSH == true) {
	    		    JOptionPane.showMessageDialog(
	    		  			null,
	    		  			"Feature not currently supported for SSH",
	    		  			"Get log file",
	    		  			JOptionPane.INFORMATION_MESSAGE
	    		  			);
	    			
	    			((JButton)e.getSource()).setEnabled(false);
	    		} else {
	    		    /*JOptionPane.showMessageDialog(
	    		  			null,
	    		  			"Feature not currently supported.",
	    		  			"Monitor Job Output",
	    		  			JOptionPane.INFORMATION_MESSAGE
	    		  			);
	    			
	    			((JButton)e.getSource()).setEnabled(false);*/
			    ((JButton)e.getSource()).setEnabled(false);
				System.err.println("Disabled button?");
				String[] currentJob = getSelectedJob();
                	// <user_data_dir>/res-project/jobName.site.jobid/jobname.out
                	Settings.jobDir = Settings.defaultDirStr + Env.separator() +
                		currentJob[2] + Env.separator() + currentJob[3] + "." + currentJob[4] + "." + 
                		currentJob[6] + "." + ManageWindow.si.formatDate(currentJob[0]);
                	File touch = new File(Settings.jobDir);
                	if(!touch.exists())
                	    touch.mkdirs();
                	
                	timer = doVisTimer();
				dvb.go();
				timer.start();
			}
	    } else if (e.getSource() == retrieveButton) {
	        // Check to see that only one job is selected, or just
	        //get the first job selected
	        // Get host, working directory, and authentication type
	        //from the selected job
	        try {
	            String[] oldJob = getSelectedJob();
		        String path = oldJob[2] + "/" + 
		        		oldJob[3] + "." + oldJob[4] + "." + oldJob[6] + "." + 
		        		formatDate(oldJob[0]) + "/";
		        
		        if(Settings.DEBUG) System.err.println("Remote path is " + path);
		        
		        GridChem.appendMessage(
		                "Retrieving directory listing from " + path + "\n");
		        
//		        FileBrowser browser = new FileBrowser(new JDialog(), 
//	                    path);
//	            browser.show();
            } catch (HeadlessException e1) {
               e1.printStackTrace();
            }
	    }
	    else if (e.getSource() == editButton)
	    {
		doEditList();
	    }
	    else if (e.getSource() == deleteButton)
	    {
		doDeleteJob();
	    }
	    else if (e.getSource() == cancelButton)
	    {
	    	GridChem.oc.mw.setVisible(false);
	    	//GridChem.oc.mw.frame.dispose();
	    	}
	    else
	    {
		JOptionPane.showMessageDialog(null, "huh?",
					  "This should not happen",
					  JOptionPane.INFORMATION_MESSAGE);
	    }
	}
    }
    
    public static String[] getSelectedJob() {
        // First of all, figure out which job we are talking about here!
		
        int k = ManageWindow.si.jobBoard.getSelectedRow();
		String oldJob = "";
		for (int i = 0; i < InternalStuff.num_column; i++) 
		{
			oldJob = oldJob +" "+ (String) InternalStuff.jbModel.getValueAt(k,i);
		}

		return parseOldJob(oldJob);
    }
    

	    
    public void valueChanged(ListSelectionEvent e) 
    {
	if (e.getValueIsAdjusting() == false)
	{
	    if (jobBoard.getSelectedRow() == -1) 
	    {
		statusButton.setEnabled(false);
		dataButton.setEnabled(false);
		editButton.setEnabled(false);
		killButton.setEnabled(false); //lixh_3_4
		retrieveButton.setEnabled(false);
		deleteButton.setEnabled(false);
	    }
	    else 
	    {
		statusButton.setEnabled(true);
		dataButton.setEnabled(true);
		editButton.setEnabled(true);
		killButton.setEnabled(true); //lixh_3_4
		retrieveButton.setEnabled(true);
		deleteButton.setEnabled(true);
	    }

	}
    }

    /** Format date string from mm/dd/yyyy to yy/mm/dd
     * @param date
     */
    public static String formatDate(String date) {
        String[] parsedDate;
        parsedDate = date.split("/");
        return parsedDate[2].substring(2) + parsedDate[0] + parsedDate[1];
    }

    public void doStatusPanel2() 
    {
	// First of all, which job are we talking about here?
	// Find out the status of said job
	// then report it in an informational message.
	String oldJob = "";
	String status;
	int k = jobBoard.getSelectedRow();
	
	for (int i = 0; i < num_column; i++) {
		oldJob = oldJob +" "+ (String) jbModel.getValueAt(k,i);
	}
	
	if(Settings.authenticatedSSH)
	{
		status = checkSSHStatus(oldJob);
	} else {
		status = checkStatus(oldJob);
	}
	
	JOptionPane.showMessageDialog(null, status,
	                              "ManageWindow",
				      JOptionPane.INFORMATION_MESSAGE);
	System.err.println("ManageWindow:doStatusPanel2: Status = \n" +status);
    }

    public void doDodisStuff() 
    {
        // First of all, figure out which job we're talking about here! 
        int k = jobBoard.getSelectedRow();
		String oldJob = "";
		
		for (int i = 0; i < num_column; i++) {
			oldJob = oldJob +" "+ (String) jbModel.getValueAt(k,i);
		}
		
		System.err.println("ManageWindow:doDodisStuff: oldJob = " + oldJob);
		String wasteful ;//= "";
		String finalStatus = "Unknown";
		String sys = "";
		//String HPCsys = "";
		// Find out the status of oldJob
		String status = checkStatus(oldJob);
		System.err.println("ManageWindow:doDodisStuff:status = ");
		System.err.println(status);
		StringTokenizer st = new StringTokenizer(status);
		
		int i = 0;
		while (st.hasMoreTokens()) {
		    wasteful = st.nextToken();
		    if (wasteful.equals("Unknown")) {
		        finalStatus = wasteful;
		    } else if (wasteful.equals("Finished") || 
		            wasteful.equals("DONE")) {
		        finalStatus = "Finished";
		    } else if (wasteful.equals("Started:") || 
		            wasteful.equals("RUN") || 
		            wasteful.equals("EXIT")) {
		        finalStatus = "Running";
		    } else if (wasteful.equals("Queued") || 
		            wasteful.equals("PEND")) {
		        finalStatus = "Queued";
		    }
		    
		    System.err.println("wasteful = " +wasteful);
		    i++;
		}
		
		System.err.println("ManageWindow:doDodisStuff:finalStatus = " 
				+ finalStatus);
		String[] pList = parseOldJob(oldJob);
		String fName = pList[3] + ".out";
		File f = new File(fName);
	
		// Something about if status says it's done, then
		// get it from mass storage; if it's queued, pop up a
		// message; otherwise, get it from mass storage
		// (But for now, just get it from mass storage)
		System.err.println("System = " + pList[4]);
		if (finalStatus == "Running")
		    sys = pList[4];
		else if (pList[4].equals("sdx.uky.edu"))
		    sys = pList[4];
		else {
			if (!Settings.authenticatedGridChem) {
			    sys = "mss.ncsa.uiuc.edu";
			} else {
				sys = pList[4];
			}
		}
			
		HPCsys = pList[4];
		// If kerberos authentication we need an additional step to retrieve
		// from mass storage to local hpc system and then to the proxy server
		// so set the sys to hpc
		if (Settings.authenticatedKerberos == true )
			sys = pList[2];
		System.out.println("ManageWindow: Kerberos Auth and system for GetFile set to " + sys);
		
		// retrieve the file in question
		JFrame progressFrame = new JFrame("Monitor Job Output Progress");
		progressFrame.setSize(75, 25);
		JLabel progress = new JLabel("Retrieving file from remote system...");
		JProgressBar pBar = new JProgressBar(0, 100);
		progressFrame.getContentPane().add(progress);
		progressFrame.getContentPane().add(pBar);
		progressFrame.setVisible(true);
	
		GetFile gf = new GetFile(f, sys, HPCsys); // will be GetFile(f, sys)	
	    progress.setText("File retrieved; parsing file");
		pBar.setValue(95);
		
		try
		{
		    PrintStream qcrjm = new PrintStream(new FileOutputStream(Settings.jobDir +
                    Settings.fileSeparator + "qcrjm.conf"),false);
            qcrjm.print("qcrjm2002\n");
            qcrjm.print("datafile="+fName);
		    
		    // then mark the end of the output file: this is useful for the case when
		    // the calculation has not completed.  10/21/02
		    RandomAccessFile outFile = new RandomAccessFile(fName, "rw");
		    
		    // find the size of the file:
		    long lg = outFile.length();
		    
		    // go to the end of the file
		    outFile.seek(lg); 
		    
		    //now printout the marker:
		    outFile.writeBytes("\n");
		    outFile.writeBytes("THE_END_OF_FILE\n");
		    outFile.writeBytes("THE_END_OF_FILE\n");
		    outFile.writeBytes("THE_END_OF_FILE\n");
		    outFile.writeBytes("THE_END_OF_FILE\n"); 

		    // that's it

          } 
          catch (IOException ie) { 
              System.out.println("Error in ManageWindow");
          }
		
          // then, do Dodi's stuff.  This is Dodi's stuff: 09/27/02
          pBar.setValue(100);
          progressFrame.setVisible(false);
          JFrame frame = new DataTree();
          frame.toFront();
    }



    public void doEditList()
    {
		int n;
	//	editJobPanel ejp;
		JOptionPane.showMessageDialog(null, "Edit list",
		                              "Testing ManageWindow",
					      JOptionPane.INFORMATION_MESSAGE);
		// determine which job was selected
		n = jobBoard.getSelectedRow();
		if (n == -1)
		{
		    JOptionPane.showMessageDialog(null, "You must select a job",
						  "Error",
					          JOptionPane.INFORMATION_MESSAGE);
		}      
		else // edit the job that was selected
		{
	//	    need to delete the selected job?
	//	    jbModel.remove(n);
		    // update the qcrjm.hist file
		}
    }

    public void doDeleteJob() {
		int n;
//		int Nmodel = jbModel.getRowCount();
		
		n = jobBoard.getSelectedRow();
		
		if (n == -1) {
		    JOptionPane.showMessageDialog(null, "You must select a job",
				    "Error", JOptionPane.INFORMATION_MESSAGE);
		} else {
		    jbModel.removeRow(n);
		    writeToFile();
		}
    }

    int getSelectedIndex() {
        return jobBoard.getSelectedRow();
    }


    void intCGI(File f, URL cgiURL)
    {
		try {
		    URLConnection connex = cgiURL.openConnection();
		    connex.setDoOutput(true);
		    PrintWriter outStream = new PrintWriter(connex.getOutputStream());
		    String userName = URLEncoder.encode(Settings.name.getText());
	
//		    String fName = f.getName();
	
		    outStream.println("Username=" + userName);
		    outStream.close();
		    
		    // Now write the input into the file
		    // something like the below
		    BufferedReader inStream = new BufferedReader(new 
				InputStreamReader(connex.getInputStream()));
		    
		    boolean append = false;
		    
		    String inLine;
		    
		    try {
		        FileWriter fw = new FileWriter(f, append);
				
		        while((inLine = inStream.readLine()) != null) {
				    int inLineLen = inLine.length();
				    if (inLineLen > 0) {
						fw.write(inLine + "\n");
						System.out.println(inLine);
				    }
				}
				
		        fw.close();
		    } 
		    catch (IOException ioe) {
		        System.err.println("ManageWindow:intCGI:IOException");
		        System.err.println(ioe.toString());
		        ioe.printStackTrace();
		    }
		}
		catch (IOException ioe) {
		   System.err.println("ManageWindow:intCGI:IOException");
		   System.err.println(ioe.toString());
		   ioe.printStackTrace();
		}
    }

    void parseHist(File f, ArrayList<String> nl)
    {
		String s;
		try
		{
		    BufferedReader br = new BufferedReader(new FileReader(f));
		    while ((s = br.readLine()) != null)
		    {	
			int m = s.length();
			if (m > 0)
			{
			    // append s to the list
			    nl.add(s);
			}
		    }
		    br.close();
		}
		catch (IOException fnfe) { // could be IOException instead 
		    System.err.println("ManageWindow:parseHist:FileNotFound");
		    System.err.println(fnfe.toString());
		    fnfe.printStackTrace();
		}
	}
    
    /**
     * Determine the status of the job
     */ 
    @SuppressWarnings("deprecation")
	public String checkStatus(String oldJob) 
    { 
		String [] list = parseOldJob(oldJob);
		String jobID = list[6];
		String jobName = list[3];
		String mach = list[4];
		String line = "";
		
		// Send the above info to a cgi file and get it to check on
		// the status of the job
		try {
		    URL cgiURL = new URL(Invariants.httpsGateway + "jbhist2.cgi");
		    String line2;
		    URLConnection connex = cgiURL.openConnection();
		    connex.setDoOutput(true);
		    PrintWriter outStream = new PrintWriter(connex.getOutputStream());
		    String userName = URLEncoder.encode(Settings.name.getText());
		    String JoBID = URLEncoder.encode(jobID);
//		    String JoBName = URLEncoder.encode(jobName);
		    String sys = URLEncoder.encode(mach);
		    
	
		    if (Settings.authenticatedGridChem) {
		        userName = URLEncoder.encode("ccguser");
		        outStream.println("IsGridChem=" + URLEncoder.encode("true"));
	            System.err.println("ManageWindow:IsGridChem=" + "true");
	        } else {
	            userName = URLEncoder.encode(Settings.name.getText());
	            outStream.println("IsGridChem=" + URLEncoder.encode("false"));
	            System.err.println("ManageWindow:IsGridChem=" + "false");
	        }
	
		    outStream.println("JoBID="+JoBID);
		    System.err.println("ManageWindow:checkStatus JoBID="+JoBID);
		    outStream.println("Username="+userName);
		    System.err.println("ManageWindow:checkStatus Username="+userName);
		    outStream.println("GridChemUsername=" + Settings.gridchemusername);
		    System.err.println("ManageWindow:GridChemUsername=" + Settings.gridchemusername);
		    outStream.println("Sysnm="+sys);
		    System.err.println("ManageWindow:checkStatus Sysnm="+sys);
		    
		    outStream.close();
	
		    BufferedReader inStream = new BufferedReader(new 
				    InputStreamReader(connex.getInputStream()));
		    while ((line2 = inStream.readLine()) != null) {
		    int m = line2.length();
			        if (m > 0)
			        {
				    line = line2;
				    System.err.println(line);
				}
		    }
		}
		catch (IOException ioe) {
		    System.err.println("ManageWindow:checkStatus: IOException");
		    System.err.println(ioe.toString());
		    ioe.printStackTrace();
		}
		
		return line;
    }

    /** determine the status of the ssh job by directly querying 
    remote machine via ssh **/
   public String checkSSHStatus(String oldJob)
   {
	    	String [] list = parseOldJob(oldJob);
	    	String jobID = list[0];
	    	
	    	String command = new String("qstat " + jobID);
   	
//	    	SSHUtils sshutil = new SSHUtils();
	    	
//	    	return sshutil.exec(command);
	    	
	    	return "";
   		
   }
   
    /*  Parse the cryptic line of the old job from qcrjm.hist.
     *  The format is:
     *    - date (mm/dd/yyyy)
     *    - time (hh:mm)
     *    - project name
     *    - job name
     *    - resource fqdn
     *    - queue
     *    - jobid
     */
    public static String[] parseOldJob(String oldJob) 
    {
		String [] oldJ = new String[7];
		if (Settings.DEBUG)
		    System.err.println("ManageWindow:parseOldJob: " +
		        "oldJob = " + oldJob);
		StringTokenizer ojt = new StringTokenizer(oldJob," ");
		
		int i = 0;
		
		while (ojt.hasMoreTokens()) {
		    oldJ[i] = ojt.nextToken();
		    System.err.println(oldJ[i]);
		    i++;
		}
		
		return oldJ;
    }

    public void windowOpened(WindowEvent e) {}

    public void windowClosing(WindowEvent e)
    {
        // check for temp file and if it exists, load into text box
		System.err.println("load temp file here!");
	
		File f = new File(Settings.defaultDirStr + 
			Settings.fileSeparator + "tmp.txt");
		
		if ((f.exists()) ) {
		    try
		    {
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
				
		    	}
		    catch (IOException ioe) {
		        System.err.println("IOException in editJobPanel");
		    }
		}
		nanWin.dispose();
		//setVisible(false);
    }

    public void windowClosed(WindowEvent e) {}

    public void windowIconified(WindowEvent e) {}

    public void windowDeiconified(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}

    public void windowDeactivated(WindowEvent e) {}
    
    public void componentHidden(ComponentEvent e)
    {
		System.err.println("load temp file here!");
		
		File f = new File(Settings.defaultDirStr + 
			Settings.fileSeparator + "tmp.txt");
		if ((f.exists()) ) {
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
		    }
		    catch (IOException ioe) {	    
		        System.err.println("IOException in editJobPanel");
		    }
		}

		nanWin.nano.t.setVisible(false);
		nanWin.dispose();
		JOptionPane.showMessageDialog(null, "WARNING: The input" +
				" appearing here is taken from a template." +
				"  The molecule information is correct, \nbut" +
				" make sure to edit the other parts of the" +
				" text.", "GridChem: Job Editor",
				JOptionPane.INFORMATION_MESSAGE);
    }

    public void componentMoved(ComponentEvent e) {}
    public void componentResized(ComponentEvent e) {}
    public void componentShown(ComponentEvent e) {}

    public void writeToFile()
    {
	int N = jbModel.getRowCount();
	int i;
	boolean append = false;
	try
	{
	    File f = new File(Settings.histFilename);  //lixh_change_2_15
	    FileWriter fw = new FileWriter(f, append);
	    String line;
	    for (i = N-1; i >0; i--)   //lixh_add_2_15 (i++ --> i--)
	    {
	    	line = "";
	    	for (int j = 0; j < num_column; j++) {
	    		line = line +" "+ (String) jbModel.getValueAt(i,j);
	    	}
		fw.write(line + "\n");
	    }
	    line = "";
    	for (int j = 0; j < num_column; j++) {
    		line = line +" "+ (String) jbModel.getValueAt(0,j);
    	}
	    fw.write(line);
	    fw.close();
	}
	catch (IOException ioe) {}
    }
}

// Retrieve data from Mass Storage (currently from mss.ncsa.uiuc.edu)
class doRetrieve2
{
	doRetrieve2()
	{	
	    // First of all, figure out which job we're talking about here!
		int k = ManageWindow.si.jobBoard.getSelectedRow();
		String oldJob = "";
		for (int i = 0; i < InternalStuff.num_column; i++) {
			oldJob = oldJob +" "+ (String) InternalStuff.jbModel.getValueAt(k,i);
		}
		System.err.println("ManageWindow:doDodisStuff: oldJob = " + oldJob);
		String wasteful ;//= "";
		String finalStatus = "Unknown";
		String sys = "";
		String HPCsys = "";	
		String[] pList = parseOldJob(oldJob);
		String fromfName = pList[3] + ".out"; //jobName.out
		File fromFile = new File(fromfName);
		
		final ProgressMonitor pm = new ProgressMonitor(null, 
				"Retrieve Job Output Progress", 
				"Checking job status on remote system...",
				0, 100);
		pm.setMillisToPopup(0);
		pm.setMillisToDecideToPopup(0);
		pm.setProgress(10);

		// Find out the status of oldJob
		String status = checkStatus(oldJob);
		pm.setProgress(45);
		System.err.println("ManageWindow:doDodisStuff:status = ");
		System.err.println(status);
		StringTokenizer st = new StringTokenizer(status);
		int i = 0;
		while (st.hasMoreTokens())
		{
		    wasteful = st.nextToken();
		    if (wasteful.equals("Unknown"))
			finalStatus = wasteful;
		    else if (wasteful.equals("Finished"))
			finalStatus = wasteful;
		    else if (wasteful.equals("Started:") || 
			    wasteful.equals("RUN") || wasteful.equals("DONE")
		       	    || wasteful.equals("EXIT"))
			finalStatus = "Running";
		    else if (wasteful.equals("Queued") || 
				    wasteful.equals("PEND"))
			finalStatus = "Queued";
		    System.err.println("wasteful = " +wasteful);
		    i++;
		}
		System.err.println("ManageWindow:doRetrieve2:finalStatus = " 
				+ finalStatus);

		// Something about if status says it's done, then
		// get it from mass storage; if it's queued, pop up a
		// message; (But for now, just get it from mass storage)
		if (finalStatus == "Running")
		    sys = pList[4];
		else if (pList[4].equals("sdx.uky.edu"))
		    sys = pList[4];
		else {
			if (!Settings.authenticatedGridChem) {
			    sys = "mss.ncsa.uiuc.edu";
			} else {
				sys = pList[4];
			}
		}
	    HPCsys = pList[4];
		
		
		//pop up a window, let user choose the place to save date
		
	    String tofName;
	    File toFile ;
	    JFileChooser chooser = new JFileChooser();
		int retVal = chooser.showSaveDialog(ManageWindow.si);
		if (retVal == JFileChooser.APPROVE_OPTION)
		{
			toFile = chooser.getSelectedFile();
			tofName = toFile.getName(); 			
			// retrieve the file in question
			pm.setProgress(55);
			pm.setNote("Retrieving file from remote system...");
			GetFile gf = new GetFile(fromFile, toFile, sys, HPCsys);
			
		}
		else
		{
			tofName = Settings.jobDir + Settings.fileSeparator +
			  fromfName;
			toFile = new File(tofName);
			// retrieve the file in question
			pm.setProgress(55);
			pm.setNote("Retrieving file from remote system...");
			GetFile gf = new GetFile(fromFile, toFile, sys, HPCsys);
		}
		
			pm.setNote("File retrieved; parsing file...");
			pm.setProgress(95);
		
	        try 
			{
	            PrintStream qcrjm = new PrintStream(new FileOutputStream(Settings.defaultDirStr + Settings.fileSeparator 
	        			+ "qcrjm.conf"),false);
	            qcrjm.print("qcrjm2002\n");
	            qcrjm.print("datafile=" + tofName);

	            //then mark the end of the output file: this is useful for the case when
	            //the calculation has not completed.  10/21/02
	            RandomAccessFile outFile = new RandomAccessFile(tofName, "rw");
	            //find the size of the file:
	            long lg = outFile.length();   
	            //go to the end of the file
	            outFile.seek(lg); 
	            //now printout the marker:
	            outFile.writeBytes("\n");
	            outFile.writeBytes("THE_END_OF_FILE\n");
	            outFile.writeBytes("THE_END_OF_FILE\n"); 
	            outFile.writeBytes("THE_END_OF_FILE\n"); 
	            outFile.writeBytes("THE_END_OF_FILE\n"); 
	        } catch (IOException ie) { System.out.println("Error in ManageWindow:doRetrieve2"); }
	    
        pm.setProgress(100);
        InternalStuff.drb.stop();
        InternalStuff.drb.done();
        
	}		

    public String checkStatus(String oldJob) 
    { 
	String [] list = parseOldJob(oldJob);
	String jobID = list[6];
	String jobName = list[3];
	String mach = list[4];
	String line = "";
	// Send the above info to a cgi file and get it to check on
	// the status of the job
	try
	{
	    URL cgiURL = new URL(Invariants.httpsGateway + "jbhist2.cgi");
	    String line2;
	    URLConnection connex = cgiURL.openConnection();
	    connex.setDoOutput(true);
	    PrintWriter outStream = new PrintWriter(connex.getOutputStream());
	    String userName = URLEncoder.encode(Settings.name.getText());
	    String JoBID = URLEncoder.encode(jobID);
	    String JoBName = URLEncoder.encode(jobName);
	    String sys = URLEncoder.encode(mach);
	    
	
	    if (Settings.authenticatedGridChem) {
	        userName = URLEncoder.encode("ccguser");
	        outStream.println("IsGridChem=" + URLEncoder.encode("true"));
            System.err.println("ManageWindow:IsGridChem=" + "true");
        } else {
            userName = URLEncoder.encode(Settings.name.getText());
            outStream.println("IsGridChem=" + URLEncoder.encode("false"));
            System.err.println("ManageWindow:IsGridChem=" + "false");
        }

	    outStream.println("JoBID="+JoBID);
	    System.err.println("ManageWindow:checkStatus JoBID="+JoBID);
	    outStream.println("Username="+userName);
	    System.err.println("ManageWindow:checkStatus Username="+userName);
	    outStream.println("GridChemUsername=" + Settings.gridchemusername);
	    System.err.println("ManageWindow:GridChemUsername=" + Settings.gridchemusername);
	    outStream.println("Sysnm="+sys);
	    System.err.println("ManageWindow:checkStatus Sysnm="+sys);
	    
	    outStream.close();

	    BufferedReader inStream = new BufferedReader(new 
			    InputStreamReader(connex.getInputStream()));
	    while ((line2 = inStream.readLine()) != null)
	    {
		int m = line2.length();
	        if (m > 0)
	        {
		    line = line2;
		    System.err.println(line);
		}
	    }
	}
	catch (IOException ioe)
	{
	    System.err.println("ManageWindow:checkStatus: IOException");
	    System.err.println(ioe.toString());
	    ioe.printStackTrace();
	}
	return line;
    }
    
    /*  Parse the cryptic line of the old job from qcrjm.hist.
     *  The format is:
     *    - date (mm/dd/yyyy)
     *    - time (hh:mm)
     *    - project name
     *    - job name
     *    - resource fqdn
     *    - queue
     *    - jobid
     */
    public String[] parseOldJob(String oldJob) 
    {
	String [] oldJ = new String[7];
	System.err.println("ManageWindow:parseOldJob: oldJob = " + oldJob);
	StringTokenizer ojt = new StringTokenizer(oldJob," ");
	int i = 0;
	while (ojt.hasMoreTokens())
	{
	    oldJ[i] = ojt.nextToken();
	    System.err.println(oldJ[i]);
	    i++;
	}
	return oldJ;
    }
}

class doVisualization2 implements WindowListener, ComponentListener
{
    nanocadFrame2 nanWin;

    doVisualization2()
    {
	    final ProgressMonitor pm; 
	    String fName;
	    String fremoteName;
	    String jobDirName;
	    	boolean present = false;
	    	
		// First of all, figure out which job we're talking about here!
		int k = ManageWindow.si.jobBoard.getSelectedRow();
		String oldJob = "";
		for (int i = 0; i < InternalStuff.num_column; i++) {
			oldJob = oldJob +" "+ (String) InternalStuff.jbModel.getValueAt(k,i);
		}
		System.err.println("ManageWindow:doDodisStuff: oldJob = " + oldJob);
		String wasteful ;//= "";
		String finalStatus = "Unknown";
		String sys = "";
		String HPCsys = "";	
		String[] pList = parseOldJob(oldJob);
		/*jobDirName = Settings.defaultDirStr + Settings.fileSeparator +
			pList[2] + Settings.fileSeparator + pList[3] + "." + pList[4] + "." + 
			pList[6] + "." + ManageWindow.si.formatDate(pList[0]);
		File jobDir = new File(jobDirName);
		jobDir.mkdirs();*/
	
		//fName = jobDirName + Settings.fileSeparator + pList[3] + ".out";
		fName = Settings.jobDir + Settings.fileSeparator + pList[3] + ".out";
	
	    // now add the path to the file.  the path follows the following scheme:
	    // [internal,external]/<username>/<project>/<jobname>.<fullhostname>.<jobid>.<submitdate>/<jobname>.out
	
	    fremoteName =  pList[2] + "/" + pList[3] + "." + pList[4] + "." + 
			pList[6] + "." + ManageWindow.si.formatDate(pList[0]) + "/" +    
			pList[3] + ".out";
	    System.out.println(" Remote File Name is " + fremoteName);
	    File flocal = new File(fName);
	    File fremote = new File(fremoteName);
	    
	    // To see if output is available on local machine
	    //int selectedGUI= JOptionPane.showConfirmDialog(null,"Has the output file already been available on the local machine?",
		// 		"Select an option",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		
	    //if (selectedGUI == 1){
		if (flocal.exists() && flocal.length() > 0)	 {
		    int selectedGUI = JOptionPane.showConfirmDialog(
		  			null,
		  			"Output file found on local system. Use this file?",
		  			"Monitor Job Output",
		  			JOptionPane.YES_NO_OPTION,
		  			JOptionPane.QUESTION_MESSAGE);
		    if (selectedGUI == 1) {
		        present = false;
		    } else {
		        present = true;
		    }
		}
		
		if (!present) {
			pm = new ProgressMonitor(ManageWindow.frame, 
					"Monitor Job Output Progress", 
					"Checking job status on remote system...",
					0, 100);
			pm.setMillisToPopup(0);
			pm.setMillisToDecideToPopup(0);
			//try { Thread.sleep(1000);} catch (InterruptedException ie) {}
	
			// Find out the status of oldJob
			pm.setProgress(30);
			String status = checkStatus(oldJob);
			System.err.println("ManageWindow:doDodisStuff:status = ");
			System.err.println(status);
			StringTokenizer st = new StringTokenizer(status);
			int i = 0;
			while (st.hasMoreTokens())
			{
				wasteful = st.nextToken();
				if (wasteful.equals("Unknown"))
					finalStatus = wasteful;
				else if (wasteful.equals("Finished"))
					finalStatus = wasteful;
				else if (wasteful.equals("Started:") || 
						wasteful.equals("RUN") || wasteful.equals("DONE")
						|| wasteful.equals("EXIT"))
					finalStatus = "Running";
				else if (wasteful.equals("Queued") || 
						wasteful.equals("PEND"))
					finalStatus = "Queued";
				System.err.println("wasteful = " +wasteful);
				i++;
			}
			System.err.println("ManageWindow:doDodisStuff:finalStatus = " 
					+ finalStatus);
	
			// Something about if status says it's done, then
			// get it from mass storage; if it's queued, pop up a
			// message; otherwise, get it from mass storage
			// (But for now, just get it from mass storage)
				
			if (finalStatus == "Running")
			    sys = pList[4];
			else if (pList[4].equals("sdx.uky.edu"))
				sys = pList[4];
			else {
				if (!Settings.authenticatedGridChem) {   //if GridChem authentication is selected, then do not retrieve file from mss
				    sys = "mss.ncsa.uiuc.edu";
				} else {
					sys = pList[4];
				}
					
			}
			HPCsys = pList[4];
	
			// retrieve the file in question
			pm.setNote("Retrieving file from remote system...");
			pm.setProgress(45);
	
			//GetFile gf = new GetFile(f, sys, HPCsys);
			if (Settings.DEBUG)
			    System.out.println("ManageWindow: GetFile for remote file " + fremote.getPath() +
					" to local file " + flocal.getPath());
			GetFile gf = new GetFile(fremote, flocal, sys, HPCsys);
			
			pm.setNote("File retrieved; parsing file...");
		} else {
	        if (Settings.DEBUG) 
		        System.out.println("ManageWindow: Found local ouput file: " + 
		                fName);

	        FileDialog fd = new FileDialog(ManageWindow.frame, "Open output file", FileDialog.LOAD);
	        fd.setVisible(true);
	        if (fd.getFile() != null) {
	            fName = fd.getDirectory()+ fd.getFile();
	            System.out.print("fName = " + fName + "\n");
	        }
				
	        pm = new ProgressMonitor(null, 
	                "Monitor Job Output Progress", 
	                "Parsing file...",
	                0, 100);
	        pm.setProgress(20);
	        pm.setMillisToPopup(0);
	        pm.setMillisToDecideToPopup(0);
		}
			
		pm.setNote("Parsing file...");
		pm.setProgress(95);
		
		if (Settings.DEBUG) 
		    System.out.println("Length of file retrieved: " + flocal.length());
		if (flocal.length() == 0) {
		    JOptionPane.showMessageDialog(
		            null,
		            "Output file is empty!! This generally means that the data\n" +
		            "from your job is not available. Check to see that your job exited\n" +
		            "normally using the \"Get Job Status\" button, and that the data is\n" +
		            "indeed there using the \"Browse Job Output\" button.\n\n" +
		            "Direct further questions to help@www.gridchem.org.",
		            "Monitor Job Output",
		            JOptionPane.INFORMATION_MESSAGE
		  			);
			pm.setProgress(100);
		} else {
			// write the output file name to qcrjm.conf
		    try {
		        PrintStream qcrjm = new PrintStream(new FileOutputStream(Settings.jobDir +
		                Settings.fileSeparator + "qcrjm.conf"),false);
		        qcrjm.print("qcrjm2002\n");
		        qcrjm.print("datafile="+fName);
		
		        // then mark the end of the output file: this is useful for the case when
		        // the calculation has not completed.  10/21/02
			    	RandomAccessFile outFile = new RandomAccessFile(fName, "rw");
			    	// find the size of the file:
			    	long lg = outFile.length();   
			    	// go to the end of the file
			    	outFile.seek(lg); 
			    	//now printout the marker:
			    	outFile.writeBytes("\n");
			    	outFile.writeBytes("THE_END_OF_FILE\n");
			    	outFile.writeBytes("THE_END_OF_FILE\n"); 
			    	outFile.writeBytes("THE_END_OF_FILE\n"); 
			    	outFile.writeBytes("THE_END_OF_FILE\n"); 
			    	// that's it
		
		    } 
		    catch (IOException ie) { 
		        System.out.println("Error in ManageWindow"); 
		    }
		    pm.setProgress(100);
			
			// then, do Dodi's stuff.  This is Dodi's stuff: 09/27/02
		    try {
		    		FileWriter fw = new FileWriter(Settings.jobDir + Settings.fileSeparator 
							+ "loadthis", false);
				//fw.write(fw.write(Settings.jobDir +
				fw.write(Env.getApplicationDataDir() +
						Settings.fileSeparator + "finalcoord.pdb\n");
				//File fb = new File(Settings.jobDir + 
				File fb = new File(Env.getApplicationDataDir() +
						Settings.fileSeparator + "finalcoord.pdb");
				if (fb.exists())
				{
				    fb.delete();
				}
				//fw.write("water.pdb\n");
				fw.close();
			} catch (IOException ioe) {}
		  
			String tmpfile = "tmp.txt";
		  
			File fa = new File(Settings.defaultDirStr + Settings.fileSeparator + tmpfile);
			if ( fa.exists()) {
			    fa.delete();
			}
	
			JFrame frame = new DataTree();
	      
			nanWin = new nanocadFrame2();
			nanWin.addWindowListener(this);
			nanWin.nano.addComponentListener(this);
			boolean isactive = frame.isActive();
			boolean isfocusable = frame.isFocusableWindow();
			System.err.println("isactive = " + isactive + " isfocusable = " +
			        isfocusable);
			frame.toFront();
		}
		InternalStuff.dvb.stop();
    }
   
    public String checkStatus(String oldJob) 
    { 
		String [] list = parseOldJob(oldJob);
		String jobID = list[6];
		String jobName = list[3];
		String mach = list[4];
		String line = "";
		// Send the above info to a cgi file and get it to check on
		// the status of the job
		try
		{
		    URL cgiURL = new URL(Invariants.httpsGateway + "jbhist2.cgi");
		    String line2;
		    URLConnection connex = cgiURL.openConnection();
		    connex.setDoOutput(true);
		    PrintWriter outStream = new PrintWriter(connex.getOutputStream());
		    String userName = URLEncoder.encode(Settings.name.getText());
		    String JoBID = URLEncoder.encode(jobID);
		    String JoBName = URLEncoder.encode(jobName);
		    String sys = URLEncoder.encode(mach);
		    
		    System.err.println("ManageWindow:checkStatus JoBID="+JoBID);
		    if (Settings.authenticatedGridChem) {
		        userName = URLEncoder.encode("ccguser");
		        outStream.println("IsGridChem=" + URLEncoder.encode("true"));
	            System.err.println("ManageWindow:IsGridChem=" + "true");
	        } else {
	            userName = URLEncoder.encode(Settings.name.getText());
	            outStream.println("IsGridChem=" + URLEncoder.encode("false"));
	            System.err.println("ManageWindow:IsGridChem=" + "false");
	        }
	
		    outStream.println("JoBID="+JoBID);
		    System.err.println("ManageWindow:checkStatus JoBID="+JoBID);
		    outStream.println("Username="+userName);
		    System.err.println("ManageWindow:checkStatus Username="+userName);
		    outStream.println("GridChemUsername=" + Settings.gridchemusername);
		    System.err.println("ManageWindow:GridChemUsername=" + Settings.gridchemusername);
		    outStream.println("Sysnm="+sys);
		    System.err.println("ManageWindow:checkStatus Sysnm="+sys);
		    
		    outStream.close();
	
		    BufferedReader inStream = new BufferedReader(new 
				    InputStreamReader(connex.getInputStream()));
		    
		    while ((line2 = inStream.readLine()) != null) {
		        int m = line2.length();
		        if (m > 0) {
				    line = line2;
				    System.err.println(line);
		        }
		    }
		}
		catch (IOException ioe) {
		    System.err.println("ManageWindow:checkStatus: IOException");
		    System.err.println(ioe.toString());
		    ioe.printStackTrace();
		}
		return line;
    }
    
    /*  Parse the cryptic line of the old job from qcrjm.hist.
     *  The format is:
     *    - date (mm/dd/yyyy)
     *    - time (hh:mm)
     *    - project name
     *    - job name
     *    - resource fqdn
     *    - queue
     *    - jobid
     */
    public String[] parseOldJob(String oldJob) 
    {
		String [] oldJ = new String[7];
		System.err.println("ManageWindow:parseOldJob: oldJob = " + oldJob);
		StringTokenizer ojt = new StringTokenizer(oldJob," ");
		int i = 0;
		while (ojt.hasMoreTokens())
		{
		    oldJ[i] = ojt.nextToken();
		    System.err.println(oldJ[i]);
		    i++;
		}
		return oldJ;
    }

    public void windowOpened(WindowEvent e) {}

    public void windowClosing(WindowEvent e)
    {
        // check for temp file and if it exists, load into text box
		System.err.println("load temp file here!");
	
		File f = new File(Settings.defaultDirStr + 
			Settings.fileSeparator + "tmp.txt");
		if ((f.exists()) )//&& !(f.isEmpty()))
		{
		    try
		    {
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
				
		    }		    
		    catch (IOException ioe) {
			        System.err.println("IOException in editJobPanel");
		    }
		}
		nanWin.dispose();
    }

    public void windowClosed(WindowEvent e) {}

    public void windowIconified(WindowEvent e) {}

    public void windowDeiconified(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}

    public void windowDeactivated(WindowEvent e) {}
    
    public void componentHidden(ComponentEvent e)
    {
	System.err.println("load temp file here!");
	
	File f = new File(Settings.defaultDirStr + 
		Settings.fileSeparator + "tmp.txt");
	if ((f.exists()) )//&& !(f.isEmpty()))
	{
	    try
	    {
	    	BufferedReader inStream = new BufferedReader(new FileReader(f));
	    	String text = "";
	    	String line;
	    	while ((line = inStream.readLine()) != null)
	 	{
		    int n = line.length();
		    if (n > 0)
		    {
		    	text = text + line + "\n";
		    	System.err.println(line);
		    }
	    	}
	    	inStream.close();
		//changeInputText(text);
	    }
	    catch (IOException ioe)
	    {
		System.err.println("IOException in editJobPanel");
	    }
	}
    	nanWin.nano.t.setVisible(false);
	nanWin.dispose();
	JOptionPane.showMessageDialog(null, "WARNING: The input" +
			" appearing here is taken from a template." +
			"  The molecule information is correct, \nbut" +
			" make sure to edit the other parts of the" +
			" text.", "GridChem: Job Editor",
			JOptionPane.INFORMATION_MESSAGE);
    }

    public void componentMoved(ComponentEvent e) {}
    public void componentResized(ComponentEvent e) {}
    public void componentShown(ComponentEvent e) {}
}

class doVisBetween2 extends MyLongTask
{
    int wha;
    doVisBetween2()
    {
	super();
	wha = 0;
    }
    
    public void go() {
	current = 0;
	final SwingWorker worker = new SwingWorker() {
	    public Object construct() {
		return new doVisualization2();
	    }
	};
	worker.start();
    }
}

class doRetrieveBetween2 extends MyLongTask
{
    int wha;
    doRetrieveBetween2()
    {
	super();
	wha = 0;
    }
    
    public void go() {
	current = 0;
	final SwingWorker worker = new SwingWorker() {
	    public Object construct() {
		return new doRetrieve2();
	    }
	};
	worker.start();
    }
}

class doStatusPanel2
{
	//lixh_4/28/05
	final ProgressMonitor pm = new ProgressMonitor(null, 
			"Check Job Status", 
			"Making connection to remote system...",
			0, 100);
	
    doStatusPanel2() 
    {
		// First of all, which job are we talking about here?
		// Find out the status of said job
		// then report it in an informational message.
		int k = ManageWindow.si.jobBoard.getSelectedRow();
		String oldJob = "";
		for (int i = 0; i < InternalStuff.num_column; i++) {
			oldJob = oldJob +" "+ (String) InternalStuff.jbModel.getValueAt(k,i);
		}	
		
		pm.setProgress(5);
		pm.setMillisToPopup(0);
		pm.setMillisToDecideToPopup(0);
		
		String status = checkStatus(oldJob);
	    InternalStuff.dsb.stop();
		JOptionPane.showMessageDialog(null, status,
		                              "ManageWindow",
					      JOptionPane.INFORMATION_MESSAGE);
		System.err.println("ManageWindow:doStatusPanel2: Status = " +status);
    }
    
    public String checkStatus(String oldJob) 
    { 
		String [] list = parseOldJob(oldJob);
		String jobID = list[6];
		String jobName = list[3];
		String mach = list[4];
		String line = "";
		// Send the above info to a cgi file and get it to check on
		// the status of the job
		try
		{
		    URL cgiURL = new URL(Invariants.httpsGateway + "jbhist2.cgi");
		    String line2;
		    URLConnection connex = cgiURL.openConnection();
		    connex.setDoOutput(true);
		    PrintWriter outStream = new PrintWriter(connex.getOutputStream());
		    String JoBID = URLEncoder.encode(jobID);
		    String JoBName = URLEncoder.encode(jobName);
		    String sys = URLEncoder.encode(mach);
		    String userName;
	

		    System.err.println("ManageWindow:checkStatus JoBID="+JoBID);
		    if (Settings.authenticatedGridChem) {
		        userName = URLEncoder.encode("ccguser");
		        outStream.println("IsGridChem=" + URLEncoder.encode("true"));
	            System.err.println("ManageWindow:IsGridChem=" + "true");
	        } else {
	            userName = URLEncoder.encode(Settings.name.getText());
	            outStream.println("IsGridChem=" + URLEncoder.encode("false"));
	            System.err.println("ManageWindow:IsGridChem=" + "false");
	        }
		    outStream.println("JoBID="+JoBID);
		    outStream.println("Username="+userName);
		    System.err.println("ManageWindow:checkStatus Username="+userName);
		    outStream.println("GridChemUsername=" + Settings.gridchemusername);
		    System.err.println("ManageWindow:GridChemUsername=" + Settings.gridchemusername);
		    outStream.println("Sysnm="+sys);
		    System.err.println("ManageWindow:checkStatus Sysnm="+sys);
		    
		    outStream.close();
		    
		    pm.setProgress(50);
		    pm.setNote("Retrieving response ...");
		    
		    BufferedReader inStream = new BufferedReader(new 
				    InputStreamReader(connex.getInputStream()));
		    
		    while ((line2 = inStream.readLine()) != null) {
		        int m = line2.length();
		    
		        if (m > 0) {
		            line = line2;
		            System.err.println(line);
		        }
		    }
		    
		    pm.setProgress(90);
		}
		catch (IOException ioe) {
		    System.err.println("ManageWindow:checkStatus: IOException");
		    System.err.println(ioe.toString());
		    ioe.printStackTrace();
		}
	    
	    pm.setProgress(100);
		return line;
    }
    
    // parse the cryptic line of the old job from qcrjm.hist
    public String[] parseOldJob(String oldJob) 
    {
		String [] oldJ = new String[7];
		System.err.println("ManageWindow:parseOldJob: oldJob = " + oldJob);
		StringTokenizer ojt = new StringTokenizer(oldJob," ");
		int i = 0;
		while (ojt.hasMoreTokens()) {
		    oldJ[i] = ojt.nextToken();
		    System.err.println(oldJ[i]);
		    i++;
		}
		return oldJ;
    }
}

class doKillPanel2 {
		doKillPanel2() {
			int k = ManageWindow.si.jobBoard.getSelectedRow();
			String oldJob = "";
			for (int i = 0; i < InternalStuff.num_column; i++) {
				oldJob = oldJob +" "+ (String) InternalStuff.jbModel.getValueAt(k,i);
			}

			String status = killJob(oldJob);
		    InternalStuff.dkb.stop();
			JOptionPane.showMessageDialog(null, status,
			                              "ManageWindow",
						      JOptionPane.INFORMATION_MESSAGE);
			System.err.println("ManageWindow:doKillPanel2: Status = " +status);
		}
		
	    public String killJob(String oldJob)
	    {
	    	String [] list = parseOldJob(oldJob);
	    	String jobID = list[6];
	    	String jobName = list[3];
	    	String mach = list[4];
	    	String line = "";
		    
	    	// Send the above info to a cgi file and try to kill the job
	    	try
	    	{
	    		URL cgiURL = new URL(Invariants.httpsGateway + "killjob1.cgi");
	    		String line2;
	    		URLConnection connex = cgiURL.openConnection();
	    		connex.setDoOutput(true);
	    		PrintWriter outStream = new PrintWriter(connex.getOutputStream());
	    		String JoBID = URLEncoder.encode(jobID);
	    		String JoBName = URLEncoder.encode(jobName);
	    		String sys = URLEncoder.encode(mach);
	    		String userName;

	    		if (Settings.authenticatedGridChem) {
			        userName = URLEncoder.encode("ccguser");
			        outStream.println("IsGridChem=" + URLEncoder.encode("true"));
		            System.err.println("ManageWindow:IsGridChem=" + "true");
		        } else {
		            userName = URLEncoder.encode(Settings.name.getText());
		            outStream.println("IsGridChem=" + URLEncoder.encode("false"));
		            System.err.println("ManageWindow:IsGridChem=" + "false");
		        }
			    
			    outStream.println("JoBID="+JoBID);
			    System.err.println("ManageWindow:killJob JoBID="+JoBID);
			    outStream.println("Username="+userName);
			    System.err.println("ManageWindow:killJob Username="+userName);
			    outStream.println("GridChemUsername=" + Settings.gridchemusername);
			    System.err.println("ManageWindow:GridChemUsername=" + Settings.gridchemusername);
			    outStream.println("Sysnm="+sys);
			    System.err.println("ManageWindow:killJob Sysnm="+sys);
			    
			    outStream.close();

			    BufferedReader inStream = new BufferedReader(new 
					    InputStreamReader(connex.getInputStream()));
			    
			    while ((line2 = inStream.readLine()) != null) {
					int m = line2.length();
				        if (m > 0) {
						    line = line2;
						    System.err.println(line);
					}
			    }
			}
			catch (IOException ioe)
			{
				    System.err.println("ManageWindow:killJob: IOException");
				    System.err.println(ioe.toString());
				    ioe.printStackTrace();
			}
			
			return line;
	    }
		    
		    // parse the cryptic line of the old job from qcrjm.hist
		    public String[] parseOldJob(String oldJob) 
		    {
			String [] oldJ = new String[7];
			System.err.println("ManageWindow:parseOldJob: oldJob = " + oldJob);
			StringTokenizer ojt = new StringTokenizer(oldJob," ");
			int i = 0;
			while (ojt.hasMoreTokens())
			{
			    oldJ[i] = ojt.nextToken();
			    System.err.println(oldJ[i]);
			    i++;
			}
			return oldJ;
	    }
}

class doStatBetween2 extends MyLongTask
{
    int wha;
    doStatBetween2()
    {
	super();
	wha = 0;
    }
    
    public void go() {
	current = 0;
	final SwingWorker worker = new SwingWorker() {
	    public Object construct() {
		return new doStatusPanel2();
	    }
	};
	worker.start();
    }

}

//lixh_3_4
class doKillBetween2 extends MyLongTask{
	int wha;
    doKillBetween2()
    {
	super();
	wha = 0;
    }
    
    public void go() {
	current = 0;
	final SwingWorker worker = new SwingWorker() {
	    public Object construct() {
		return new doKillPanel2();
	    }
	};
	worker.start();
    }
}//

/** Display only .out files */
class OUTFileFilter extends FileFilter {

    /** Accept only .out files
     *  @param file The file to be checked.
     *  @return true if the file is a directory, a .out file.
     */
    public boolean accept(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            return true;
        }

        String fileOrDirectoryName = fileOrDirectory.getName();
        int dotIndex = fileOrDirectoryName.lastIndexOf('.');
        if (dotIndex == -1) {
            return false;
        }
        String extension =
            fileOrDirectoryName
            .substring(dotIndex);

        if (extension != null) {
            if (extension.equalsIgnoreCase(".out")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    
    /**  The description of this filter */
    public String getDescription() {
        return ".out files";
    }
}
