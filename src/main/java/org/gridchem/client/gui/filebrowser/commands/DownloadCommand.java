package org.gridchem.client.gui.filebrowser.commands;

import java.io.File;

import org.gridchem.client.Trace;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.util.GMS3;

public class DownloadCommand extends FileCommand {

	public DownloadCommand(StatusListener statusListener) {
		super(statusListener);

        this.id = DP;
        
        this.output = new File("");
	}

	public File getOutput() {
        
        return (File)output;
        
    }
    
    public void setOutput(File file) {
        this.output = file;
    }

    public void execute() throws Exception{
        Trace.entry();
        GMS3.getFile2((String)arguments.get("host"), (String)arguments.get("path"),this);
        Trace.exit();
    }

}
