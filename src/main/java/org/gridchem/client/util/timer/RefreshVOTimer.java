/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Apr 26, 2006
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
import java.awt.Toolkit;

import javax.swing.JOptionPane;

import org.gridchem.client.common.Settings;
import org.gridchem.client.interfaces.Timeable;
import org.gridchem.client.util.GMS3;

/**
 * Basic timer class for synching user VO with the GMS. 
 * This object is instantiated at service startup and executes 
 * every 'interval' minutes thereafter.
 * 
 * Code for this class was adapted from the Sun Developer's Site's 
 * Timer tutorial.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 * @see GMS
 */
public class RefreshVOTimer {
    private Toolkit toolkit;
    private Timer timer;
    private Timeable timedObject;
    private static int failedAttempts = 0;
    
    public RefreshVOTimer(int interval, Timeable timedObject) {
        
        this.timedObject = timedObject;
        
        toolkit = Toolkit.getDefaultToolkit();
        
        timer = new Timer();

        timer.schedule(new UpdateTask(), 
                interval*1000,        //initial delay
                interval*1000);  //subsequent rate
    }
    
    public void cancel() {
        if (Settings.DEBUG) 
            System.out.println("Canceling RefreshVOTimer.");
        timer.cancel();
    }
    
    public void restart(int interval) {
        timer.schedule(new UpdateTask(),
                interval*1000,        //initial delay
                interval*1000);  //subsequent rate
    }

    class UpdateTask extends TimerTask {
        public void run() {
            try {
                
                timedObject.refresh();
                
            } catch (Exception se) {
                System.out.println("Failed to update user profile: " + se.getMessage());
                
                if (failedAttempts++ > 10) {
                    if (Settings.DEBUG) 
                        System.out.println("Canceling RefreshVOTimer.");
                    
                    JOptionPane.showMessageDialog( null,
                            "The connection with the server was lost.\n" + 
                            "Job and resource information will not be\n" +
                            "updated until the connection is reestablished.\n" +
                            "This is a non-critical error. Check the announce-\n" +
                            "ments for more information.",
                            "Connection Lost",
                            JOptionPane.WARNING_MESSAGE);
                    
                    this.cancel();
                }
            }
        }
    }
}
