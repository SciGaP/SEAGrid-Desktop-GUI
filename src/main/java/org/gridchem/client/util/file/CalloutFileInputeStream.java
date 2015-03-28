package org.gridchem.client.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.asprise.util.ui.progress.ProgressDialog;


/**
     * Custom FileInputStream to perform callouts to the progress dialog embedded
     * in the job submission popup.
     * 
     * @author dooley
     *
     */
    class CalloutFileInputeStream extends FileInputStream {
    	
    	private ProgressDialog progressDialog;
    	
		public CalloutFileInputeStream(File file, ProgressDialog progressDialog) throws FileNotFoundException {
			super(file);
			this.progressDialog = progressDialog;
		}
		
		@Override
		public int read() throws IOException {
			progressDialog.subWorked(1);
            if (progressDialog.isCanceled()) {
                this.close();
                return -1;
            }
            int read = super.read();
            if (read != -1) {
            	progressDialog.subWorked(read);
            } else {
            	progressDialog.subWorked(1);
            }
            
            return read;
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			if (progressDialog.isCanceled()) {
                this.close();
                return -1;
            }
            int read = super.read(b, off, len);
            if (read != -1) {
            	progressDialog.subWorked(read);
            } else {
            	progressDialog.subWorked(1);
            }
            
            return read;
		}

		public int read(byte[] b) throws IOException {
			progressDialog.subWorked(1);
            if (progressDialog.isCanceled()) {
                this.close();
                return -1;
            }
            int read = super.read(b);
            if (read != -1) {
            	progressDialog.subWorked(read);
            } else {
            	progressDialog.subWorked(1);
            }
            
            return read;
		}
		
		public void close() throws IOException {
			
			super.close();
		}
    	
    }