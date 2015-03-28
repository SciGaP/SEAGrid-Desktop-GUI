

/**
 * new class by nikhil to get output file
 */
package org.gridchem.client.util.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.gui.jobsubmission.commands.*;

/**
 * @author dooley
 *
 */
public class CalloutJobOutputStream extends FileOutputStream {
	
	JobCommand command;
	
	long blocksWritten = 0;
	
	public CalloutJobOutputStream(File file, JobCommand command) throws FileNotFoundException {
		super(file);
		this.command = command;
	}

	/* (non-Javadoc)
	 * @see java.io.FileOutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		super.write(b, off, len);
		blocksWritten += b.length;
		command.getArguments().put("blocksReceived", new Long(blocksWritten));
		System.out.println("Wrote " + b.length + "/" + blocksWritten);
		command.statusChanged(new StatusEvent(null,Status.READY));
		
	}

	/* (non-Javadoc)
	 * @see java.io.FileOutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
		blocksWritten += b.length;
		command.getArguments().put("blocksReceived", new Long(blocksWritten));
		System.out.println("Wrote " + b.length + "/" + blocksWritten);
		command.statusChanged(new StatusEvent(null,Status.READY));
		
	}

	/* (non-Javadoc)
	 * @see java.io.FileOutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		super.write(b);
		blocksWritten++;
		command.getArguments().put("blocksReceived", new Long(blocksWritten));
		System.out.println("Wrote 1" + "/" + blocksWritten);
		command.statusChanged(new StatusEvent(null,Status.READY));
		
	}
	

}

