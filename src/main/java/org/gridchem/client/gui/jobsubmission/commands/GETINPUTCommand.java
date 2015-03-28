/* 
 * Created on May 21, 2008
 * 
 * Developed by: Rion Dooley - dooley [at] tacc [dot] utexas [dot] edu
 * 				 TACC, Texas Advanced Computing Center
 * 
 * https://www.tacc.utexas.edu/
 */

package org.gridchem.client.gui.jobsubmission.commands;

import org.gridchem.client.Trace;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.util.GMS3;

/**
 * Insert Template description here.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class GETINPUTCommand extends JobCommand {

    /**
     * @param statusListener
     */
    public GETINPUTCommand(StatusListener statusListener) {
        super(statusListener);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.gridchem.client.interfaces.GridCommand#execute()
     */
    public void execute() throws Exception {
        Trace.entry();
        
//        GMS3.getCachedInputFile(this);
        
        Trace.exit();
    }

}
