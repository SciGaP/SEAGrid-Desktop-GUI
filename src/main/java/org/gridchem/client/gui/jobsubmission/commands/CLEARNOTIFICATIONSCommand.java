/* 
 * Created on May 30, 2008
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
public class CLEARNOTIFICATIONSCommand extends JobCommand {

    /**
     * @param statusListener
     */
    public CLEARNOTIFICATIONSCommand(StatusListener statusListener) {
        super(statusListener);
        this.id = CLEARNOTIFICATIONS;
    }

    /* (non-Javadoc)
     * @see org.gridchem.client.interfaces.GridCommand#execute()
     */
    public void execute() throws Exception {
        Trace.entry();
        
        GMS3.clearNotifications(this);
        
        Trace.exit();
    }

}
