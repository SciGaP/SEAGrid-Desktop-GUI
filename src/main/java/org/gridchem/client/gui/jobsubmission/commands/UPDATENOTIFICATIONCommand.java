/* 
 * Created on May 30, 2008
 * 
 * Developed by: Rion Dooley - dooley [at] tacc [dot] utexas [dot] edu
 * 				 TACC, Texas Advanced Computing Center
 * 
 * https://www.tacc.utexas.edu/
 */

package org.gridchem.client.gui.jobsubmission.commands;

import java.util.ArrayList;

import org.gridchem.client.Trace;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.util.GMS3;
import org.gridchem.service.beans.NotificationBean;

/**
 * Insert Template description here.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class UPDATENOTIFICATIONCommand extends JobCommand {

    /**
     * @param statusListener
     */
    public UPDATENOTIFICATIONCommand(StatusListener statusListener) {
        super(statusListener);
        this.id = UPDATENOTIFICATION;
        this.output = new ArrayList<NotificationBean>();
    }

    /* (non-Javadoc)
     * @see org.gridchem.client.interfaces.GridCommand#execute()
     */
    public void execute() throws Exception {
        Trace.entry();
        
        GMS3.updateNotification(this);
        
        Trace.exit();

    }

}
