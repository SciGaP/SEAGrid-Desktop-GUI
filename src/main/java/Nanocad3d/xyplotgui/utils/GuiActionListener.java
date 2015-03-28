package Nanocad3d.xyplotgui.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.gridchem.client.CheckAuth;
import org.gridchem.client.gui.login.LoginDialog;
import org.gridchem.client.util.Env;
import org.gridchem.client.util.GMS3;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.exceptions.FileManagementException;
import org.gridchem.service.model.enumeration.JobStatusType;
import org.gridchem.service.stub.GMSClient;

public class GuiActionListener implements ActionListener {
	
	private String sourceType;
	private String sourceName;
	private GMSClient client;
	private LoginDialog login;
	private static int MAXCOUNT = 30;
	private static int TIMEOUT = 2000;
	private static String EMBER = "gridftp-ember.ncsa.teragrid.org" ; //"gridftp-ember.ncsa.teragrid.org";
	private static String MSS = "mss.ncsa.uiuc.edu";
	private static String EMBER_ROOTPATH="/gpfs2/scratch/users/ccguser/";
	private static String MSS_ROOTPATH="/UROOT/u/ac/ccguser/internal/";

	public GuiActionListener(String name) {
		// TODO Auto-generated constructor stub
		this.sourceName = name;
	}

	public GuiActionListener() {
		// TODO Auto-generated constructor stub
	}

	public void actionPerformed(ActionEvent e) {
		
		this.sourceType = e.getSource().getClass().getName();		
//		System.out.println(this.sourceName + " : " + this.sourceType);
		JButton curButton = (JButton)e.getSource();
		HashMap OperationPanelHash = new HashMap<String,Component>();
//		OperationPanelHash = Nanocad3d.xyplotgui.utils.ComponentUtil.createComponentMap(curButton.getParent());
//		JTextField RemoteFilePath = (JTextField)Nanocad3d.xyplotgui.utils.ComponentUtil.getComponentByName(OperationPanelHash,"RemoteFilePath");
		JFrame parentWindow = (JFrame) SwingUtilities.windowForComponent(curButton);
		HashMap parentWindowHash = Nanocad3d.xyplotgui.utils.ComponentUtil.createComponentMap(parentWindow);
		JLabel StatusLabel = (JLabel)Nanocad3d.xyplotgui.utils.ComponentUtil.getComponentByName(parentWindowHash,"xyplotStatusBar");
		JTextField RemoteFilePath = (JTextField)Nanocad3d.xyplotgui.utils.ComponentUtil.getComponentByName(parentWindowHash,"RemoteFilePath");
		
		if(this.sourceName.equals("SaveRemoteFileTo") ){
		
		}else if (this.sourceName == "GenerateXYPlotFrom"){			

			JPanel PreviousXYPlotPanel = (JPanel) Nanocad3d.xyplotgui.utils.ComponentUtil.getComponentByName(parentWindowHash,"XYPlotPanel");
			
			if(PreviousXYPlotPanel != null) { // have previous xyplot
				PreviousXYPlotPanel.setVisible(false);
				StatusLabel.setText("Clear previous xyplot");
			}else {
				StatusLabel.setText("Start downloading file");
			}
			
			String RemotePath = RemoteFilePath.getText();
			JobBean job = createDummyData(); // create dummy job object
			
        	String fileUri = EMBER_ROOTPATH+ job.getName() + "/" + job.getProjectName() + "/" + 
            job.getQueueName() + "/data.txt"; 
        	
			if(!RemotePath.equals(RemoteFilePath.getToolTipText())){ // if user changed the default text inside JTextField
				fileUri = RemoteFilePath.getText();
			}
			
        	RemoteFilePath.setText(fileUri); // set JTextField to current downloading file path
        
            CheckAuth ca = new CheckAuth();
            if (! ca.authorized) { // login again if user is not authenticated
            	doLoginDialog();
            }
					
	    	int count = 0; //count number of times for retries
	    	File dataHandler = null;
	    	java.io.File localfile = new File(Env.getGridchemDataDir() + File.separator + fileUri);
	    	boolean overWrite = true;
	    	
	    	if (localfile.exists()) { // ask user about overwriting local file	    		
	    		Object[] OverWriteOptions = {"OK",
	                    "Cancel",
	                    "Help"};
	    		JFrame frame = new JFrame("OverWrite local file");
	    		int n = JOptionPane.showOptionDialog(frame,
	    				"Would you like to download remote file and overwrite the local copy?"
	    				,"OverWrite local file", JOptionPane.YES_NO_CANCEL_OPTION,
	    				JOptionPane.QUESTION_MESSAGE,
	    				null,
	    				OverWriteOptions,
	    				OverWriteOptions[0]);
	    		if (n==1){ // do not overwrite
	    			overWrite = false;
	    			StatusLabel.setText("Cancel downloading remote file and plot from local existing file at $HOME/gridchem/data");
	    			}
	    		else if (n==0){ // file exist and overwrite
					Object[] DownloadOptions = {"Download From HPC",
		                    "Download From Mass Storage",
		                    "Help"};
		    		frame = new JFrame("Choose Resource to download from");
		    		n = JOptionPane.showOptionDialog(frame,
		    				"Where Do you want to download the file?"
		    				,"Download from HPC", JOptionPane.YES_NO_CANCEL_OPTION,
		    				JOptionPane.QUESTION_MESSAGE,
		    				null,
		    				DownloadOptions,
		    				DownloadOptions[0]);
		    		if (n==0){ // Download From HPC
		    			StatusLabel.setText("Download From HPC");
		    			job.setStorageResource(EMBER);		    			
		    		}else if (n==1){
		    			StatusLabel.setText("Download From Mass Storage");
		    			job.setStorageResource(MSS);
		    			fileUri = fileUri.replaceFirst(EMBER_ROOTPATH, MSS_ROOTPATH);
		    		}
		    		else{
		    			overWrite = false;
		    			JOptionPane.showMessageDialog(null, "Choose where to download remote file: Files will only be available  from HPC for limited time and be always available from mass storage in ncsa.");
		    		}
	    		}
	    		else{
	    			overWrite = false;
	    			JOptionPane.showMessageDialog(null, "Choose whether you want to download remote file or use local file for xyplot");
	    		}
	    	}
	    	else{ // local file does not exist and download	automatically	
				Object[] DownloadOptions = {"Download From HPC",
	                    "Download From Mass Storage",
	                    "Help"};
	    		JFrame frame = new JFrame("Choose Resource to download from");
	    		int n = JOptionPane.showOptionDialog(frame,
	    				"Where Do you want to download the file?"
	    				,"Download from HPC", JOptionPane.YES_NO_CANCEL_OPTION,
	    				JOptionPane.QUESTION_MESSAGE,
	    				null,
	    				DownloadOptions,
	    				DownloadOptions[0]);
	    		if (n==0){ // Download From HPC
	    			StatusLabel.setText("Download From HPC");
	    			job.setStorageResource(EMBER);	    		
	    		}else if(n==1){ // Download from ember
	    			StatusLabel.setText("Download From Mass Storage");
	    			job.setStorageResource(MSS);
	    			fileUri = fileUri.replaceFirst(EMBER_ROOTPATH, MSS_ROOTPATH);
	    		}
	    		else{
	    			overWrite = false;
	    			JOptionPane.showMessageDialog(null, "Choose where to download remote file: Files will only be available  from HPC for limited time and be always available from mass storage in ncsa.");
	    		}
	    	}
	    	
//	    	if(overWrite){
//	    		new RetrieveRemotTask(StatusLabel, MAXCOUNT, job.getStorageResource(), fileUri).execute();
//	    	}
			File DataFile = new File(Env.getGridchemDataDir() + File.separator + fileUri.trim());
	    	
			while (overWrite && dataHandler == null && count < MAXCOUNT){ //overwrite
			try {				
					count = count+1;
//					Thread.currentThread().sleep(TIMEOUT);
					System.out.println("try: " + count);
					System.out.println("fileUri is :" + fileUri);
					System.out.println("machine is :" + job.getStorageResource());	        
//					new RetrieveRemotTask(StatusLabel,count, job.getStorageResource(), fileUri).execute();
					dataHandler = GMS3.getFile(job.getStorageResource(), fileUri.trim());
//					if(DataFile.exists() && DataFile.length() != 0){
//						StatusLabel.setText("File downloaded successfully to $HOME/gridchem/data");
//						parentWindow.validate();
//						System.out.println("File retireved successfully");
//					}
					if(dataHandler != null && count <= MAXCOUNT){
					StatusLabel.setText("File downloaded successfully to $HOME/gridchem/data");
					parentWindow.validate();
					System.out.println("File retireved successfully");
					}
			} catch (Exception e1) {				
					if (count >=MAXCOUNT && dataHandler == null){
//					e1.printStackTrace();
					StatusLabel.setText("Fail to retrieve file in " +MAXCOUNT+" times.Please try later");
//					throw new FileManagementException("Failed to download remote file",e1);
					}				
			}
			}


			if(DataFile.exists() && DataFile.length() != 0){
				JPanel XYplotPanel = Nanocad3d.xyplotgui.frame.XYSeriesPlotPanel.createDemoPanel(DataFile);
				XYplotPanel.setBorder(BorderFactory.createTitledBorder("XYPlot Panel"));
				XYplotPanel.setName("XYPlotPanel");
				parentWindow.add(XYplotPanel,BorderLayout.CENTER);
				parentWindow.validate();
				StatusLabel.setText("Finish the xyplot with data at " + Env.getGridchemDataDir() + File.separator+ "gridchem" + File.separator+"data"+File.separator);
				

			}	
		}
}
    private void doLoginDialog() {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame() {
			public Dimension getPreferredSize() {
				return new Dimension(200, 100);
			}
		};
		frame.setTitle("Debugging frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(false);
		 login = new LoginDialog(frame, true);
}
	
	private JobBean createDummyData() {
        JobBean job = new JobBean();        
        job.setSoftwareName("Charmm");
        job.setName("ning"); // Mimic User Name
        job.setSystemName("Champion");
        job.setStartTime(new Date());
        job.setStopTime(new Date());
        job.setExperimentName("XYPlot Test Suite");
        job.setQueueName("step5_040264502"); // Mimic workflow name
        job.setProjectName("test"); // Mimic project name
        job.setId(new Long(5));
        job.setLocalId("10643");
        job.setStatus(JobStatusType.FINISHED);
        
        // no resubmissions
        job.setResubmittable(false);

        job.setCheckpointable(false);
        
        job.setStorageResource(EMBER);        
        return job;
    }
}



class RetrieveRemotTask extends SwingWorker<File, Integer> {
	 
	private static final long TIMEOUT = 2000;
	private JLabel progressLabel;
	private String host;
	private String path;
	private int count;
	private JFrame parentWindow;

	RetrieveRemotTask(JLabel progressLabel, int count, String host, String path) { 
	     //initialize
		 this.progressLabel = progressLabel;
		 parentWindow = (JFrame) SwingUtilities.windowForComponent(this.progressLabel);
		 this.count = count;
		 this.host = host;
		 this.path = path;
	 }

	@Override
	protected File doInBackground() throws Exception {
		// TODO Auto-generated method stub
			File RemoteFile = null;
			RemoteFile = GMS3.getFile(this.host, this.path);
			this.progressLabel.setText("Please wait while retireving file : retry " + this.count);
			Thread.currentThread().wait(TIMEOUT);
			this.parentWindow.validate();
			return RemoteFile;
	}
	
    @Override
    protected void done() {
        File remoteFile = null;
        String text = null;
        try {
        	remoteFile = get();
        	if (remoteFile != null) {
        		this.progressLabel.setText("Download File Successfully");        		
        		this.parentWindow.validate();
        	}
        } catch (Exception ignore) {
        	this.parentWindow.validate();
        }
    }
}
