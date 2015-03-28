package Nanocad3d;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JFrame;

import org.gridchem.client.CheckAuth;
import org.gridchem.client.SubmitJob;
import org.gridchem.client.SwingWorker;
import org.gridchem.client.optsComponent;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.LogicalFileBean;

import com.asprise.util.ui.progress.ProgressDialog;

public class parametrization /*extends Nanocad3D**/ {

	/** default serial id */
	private static final long serialVersionUID = 1L;

	private static JobBean jbean;
	private static ProgressDialog progressDialog;
	
	public void parametrizeMol2File(final JFrame paramFrame, String inpFile) {
		
		//System.out.println(GridChem.user.getUserName());
		// check if user is authenticated
		CheckAuth ca = new CheckAuth();
		if (ca.authorized) {
		//if (true) {						
			
			jbean = setDataToJobBean(inpFile);						
			
			/*SwingWorker s = */new SwingWorker() {

				public Object construct() {
					
					//ProgressDialog progressDialog = new ProgressDialog(paramFrame, "Job Submission Progress");
					progressDialog = new ProgressDialog(paramFrame, "Job Submission Progress");
					progressDialog.millisToPopup = 0;
					progressDialog.millisToDecideToPopup = 0;
					progressDialog.displayTimeLeft = false;					
					
					SubmitJob sj = new SubmitJob(jbean);
					sj.addProgressMonitor(progressDialog);
					sj.submit();
					
    				System.out.println("before return statement");
					return progressDialog;
				}				
				
				
                public void finished() {
                	String fileName = "";
                	//System.out.println("finished called");
    				progressDialog.worked(2);
    				progressDialog.finished();
    				
    				//System.out.println("project name:" + jbean.getProjectName());
    				fileName = jbean.getProjectName();
    				fileName = separateOutputs(fileName);
    				System.out.println("project name:" + jbean.getProjectName());
    				
    				Nanocad3D gui = optsComponent.nano3DWindow;
    				gui.getAtomTypes(fileName);
    				gui.readDihedrals();
                }
                
												
			}.start();			
																							
		} else {
			optsComponent opts = new optsComponent();
			opts.doWarning();
			opts.updateAuthenticatedStatus();
			opts.doAuthentication();
		}
		//return fileName;
	}

	private JobBean setDataToJobBean(String inpFile) {
		
		String fname = "";		
		JobBean jBean = new JobBean();

		// set absolute path of input file to an array
		File inputFile = new File(inpFile);
		fname = inputFile.getAbsolutePath();
		
		ArrayList <LogicalFileBean> inFiles = new ArrayList <LogicalFileBean>();
		LogicalFileBean lf = new LogicalFileBean();
		lf.setJobId(-1);
		lf.setLocalPath(fname);
		inFiles.add(lf);
				
		// set file name array to JobBean
		jBean.setInputFiles(inFiles);
		
		// set job name
		fname = inputFile.getName();
		fname = fname.substring(0,fname.lastIndexOf("."));
		//jBean.setName("cgenff_" + fname);
		jBean.setName(fname);
		
		Date now = new Date();  		   
		DateFormat df = new SimpleDateFormat("yyyyMMdd");  
		fname = fname + "_"+ df.format(now);
		jBean.setProjectName(fname);
		
		// set project name
		jBean.setExperimentName("cgenff_project");		

		// set IP address
		jBean.setHostName(getIpAddress());
		
		// set module name
		jBean.setModuleName("cgenff");

		return jBean;
	}

	private String getIpAddress() {
		String ipAddress = "";
		try {
			InetAddress thisIp = InetAddress.getLocalHost();
			ipAddress = thisIp.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipAddress;
	}
	
	private String separateOutputs(String fileName) {
		
		String data, optFile = "";
		String baseName = "";
		StringBuffer buffer = new StringBuffer();
		File err_str = null;
		Scanner fileScan = null;
		
		try{
			
			baseName = fileName.substring(0,fileName.lastIndexOf("err_str"));
			//baseName = fileName.substring(0,fileName.lastIndexOf("out"));
			
			err_str = new File(fileName);
			fileScan = new Scanner(err_str);			
			
			// extract error file info
			optFile = baseName + "err";	
            while (fileScan.hasNextLine())         	
            {
            	data = (fileScan.nextLine()).trim();

                 if (data.length() > 0 && data.equalsIgnoreCase("ENDOFFILE"))
                 {
                	 break;                	                 	 
                 } else {
                	 buffer.append(data +"\r\n");
                 }
            }        
            
            // write error file
            writeOutput(buffer, optFile);

            // write output file
            buffer = new StringBuffer();
            optFile = baseName + "str";
            while (fileScan.hasNextLine())
            {
            	data = (fileScan.nextLine()).trim();
            	buffer.append(data + "\r\n");
            	//System.out.println(data);
            }      
            
            // write output file
            writeOutput(buffer, optFile); 
            
		}catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}finally{
			fileScan.close();
			err_str.delete();        
		}
		return optFile;
		
	}
	
	private void writeOutput(StringBuffer outputBuf, String fileName){
		
		FileWriter fstream = null;
		BufferedWriter out = null;
		
		try{
			
            fstream = new FileWriter(fileName);
            out = new BufferedWriter(fstream);
            out.write(outputBuf.toString());
            out.flush();

            //Close the output stream
            out.close();        
            fstream.close();

		}catch ( IOException ioe ){
			ioe.printStackTrace();
		}
		
		
	}
		
}



