package org.gridchem.client.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.gridchem.client.GridChem;
import org.gridchem.client.common.Settings;
import org.gridchem.client.util.GMS3;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.LogicalFileBean;
import org.gridchem.service.socket.FileUploadThread;
import org.gridchem.service.stub.file.FileServiceStub.PutCachedFile;

import com.asprise.util.ui.progress.ProgressDialog;

public class FileUploader {
	private JobBean job;
	private File file;
	private ProgressDialog progressDialog;

	public FileUploader(String file, JobBean job) throws IOException {
		this.job = job;
		this.file = new File(file);
		if (!this.file.exists()) {
			throw new IOException("Could not locate file " + file
					+ " for uploading.");
		}

	}

	public LogicalFileBean send1() throws Exception {
		
		if (progressDialog != null) {
			progressDialog.beginSubTask("Uploading file " + file.getName(),
					(int) file.length() + 1);
		}
    	
    	System.out.println("******************In send1 function: ");
    	System.out.println(GridChem.user.getUserName());
    	System.out.println(job.getExperimentName());
    	System.out.println(job.getName());
    	System.out.println(file.getName());
    	System.out.println(file.getAbsolutePath());
		
		FileUploadThread thread = new FileUploadThread(GridChem.user.getUserName(), job.getExperimentName(),
				job.getName(), file.getName(), file.getAbsolutePath());

		thread.start();

		synchronized (thread) {
			thread.wait();
		}
		
		LogicalFileBean lb = new LogicalFileBean();
		
		String remotePath =  "/tmp/" + GridChem.user.getUserName() + "/" + job.getExperimentName()
		+ "/" + job.getName() + "/" + file.getName();
		
		System.out.println("Remote path is: " + remotePath);
		
		lb.setLocalPath(remotePath);
		lb.setSlocalPath(remotePath);
		
		lb.setJobId(-1);
	
		lb.setRemotePath(file.getAbsolutePath());
		
		progressDialog.subWorked(1);
		
		return lb;
	}

	public LogicalFileBean send() throws Exception {
		// read file and write it into form...

		if (progressDialog != null) {
			progressDialog.beginSubTask("Uploading file " + file.getName(),
					(int) file.length() + 1);
		}

		FileDataSource source = new FileDataSource(file) {
			@Override
			public InputStream getInputStream() throws IOException {
				return new CalloutFileInputeStream(getFile(), progressDialog);
			}
		};

		DataHandler handler = new DataHandler(source);

		PutCachedFile params = new PutCachedFile();
		params.setArgs0(GMS3.sessionKey);
		params.setArgs1(job.getExperimentName());
		params.setArgs2(job.getName());
		params.setArgs3(file.getName());
		params.setArgs4(handler);

		String results = GMS3.getClient().getFileService()
				.putCachedFile(params).get_return();

		System.out.println("*** Server response for FileUpload is: *** \n"
				+ results
				+ " \n *** End of Server Response for FileUpload*** \n");
		progressDialog.subWorked(1);

		return (LogicalFileBean) Settings.xstream.fromXML(results);

	}

	public void addProgressDialog(ProgressDialog ccp) {
		this.progressDialog = ccp;
	}

}
