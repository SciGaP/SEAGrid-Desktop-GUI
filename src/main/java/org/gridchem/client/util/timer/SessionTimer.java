/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Oct 6, 2006
 * 
 * Developed by: CCT, Center for Computation and Technology, 
 * 				NCSA, University of Illinois at Urbana-Champaign
 * 				OSC, Ohio Supercomputing Center
 * 				TACC, Texas Advanced Computing Center
 * 				UKy, University of Kentucky
 * 
 * https://www.gridchem.org/
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal with the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimers.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimers in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
 *    University of Illinois at Urbana-Champaign, nor the names of its contributors 
 *    may be used to endorse or promote products derived from this Software without 
 *    specific prior written permission.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS WITH THE SOFTWARE.
*/

package org.gridchem.client.util.timer;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import org.gridchem.client.GridChem;
import org.gridchem.client.common.Settings;
import org.gridchem.client.util.GMS3;

/**
 * Watchdog to ensure the user's session is not terminated without first
 * prompting them for a warning Settings.SESSION_MINUTE_WARNING_BEFORE_TIMEOUT
 * minutes before it expires.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class SessionTimer {

    protected Timer timer;
    
    // timer will run the duration of a single session 
    private static final long sessionDuration = 
        Settings.SESSION_LENGTH_MINUTES * 60 * 1000;
    
    // the timer task will fire 'advanceWarning' minutes before the 
    // end of a session.
    private static final long advanceWarning = 
        Settings.SESSION_MINUTE_WARNING_BEFORE_TIMEOUT * 60 * 1000;
    
    /**
     * Creates a session timer and sets it to go off 
     * Settings.SESSION_MINUTE_WARNING_BEFORE_TIMEOUT minutes
     * before their session expires. Session length in minutes 
     * is defined by the Settings.SESSION_LENGTH_MINUTES
     * variable.
     */
    public SessionTimer() {
        timer = new Timer();
        
        timer.schedule(new SessionExpriationPromptTask(),
                sessionDuration - advanceWarning,        //initial delay
                sessionDuration);  //subsequent rate minus five minutes
    }
    
    /**
     * Stops the session timer.  No warning will be given before the
     * user's session expires is this is called.
     */
    public void cancel() {
        timer.cancel();
    }
    
    /**
     * Restarts the session timer.  It will not resume from its 
     * previous point, but rather start again from the beginning
     * and wait Settings.SESSION_LENGTH_MINUTES - 
     * Settings.SESSION_MINUTE_WARNING_BEFORE_TIMEOUT minutes to 
     * trigger.
     */
    public void restart() {
        timer.schedule(new SessionExpriationPromptTask(),
                sessionDuration - advanceWarning,        //initial delay
                sessionDuration);  //subsequent rate minus five minutes
    }
}

class SessionExpriationPromptTask extends TimerTask {
 
    public void run() {
        if (GridChem.oc.monitorWindow != null) {
            GridChem.oc.monitorWindow.setUpdate(false);
        }
        
        int viewLog = JOptionPane.showConfirmDialog(
                GridChem.oc,
                "Your session will expire in " + 
                Settings.SESSION_MINUTE_WARNING_BEFORE_TIMEOUT + 
                " minutes.\n" + 
                "Would you like to renew your session?",
                "Session Timeout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
        );
            
        if (viewLog == 0) {
            try {
                GMS3.renewSession();
                
                JOptionPane.showMessageDialog(GridChem.oc,
                        "Your session has been renewed for " + 
                        Settings.SESSION_LENGTH_MINUTES + " minutes.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(GridChem.oc,
                        "Unable to renew your session.\n" + 
                        "Your session will expire in " + 
                        Settings.SESSION_MINUTE_WARNING_BEFORE_TIMEOUT + 
                        "minutes.");
            }
        } 
        
        this.cancel();
        
    }
}

