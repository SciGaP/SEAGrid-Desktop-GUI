/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Jun 29, 2006
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

package org.gridchem.client.gui.jobsubmission.commands;

import java.util.Hashtable;

import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.interfaces.GridCommand;
import org.gridchem.client.interfaces.StatusListener;

public abstract class JobCommand implements GridCommand {
    public static final String SUBMIT = "submit";
    public static final String SUBMIT_MANY = "submit_many";
    public static final String QSTAT = "qstat";
    public static final String KILL = "kill";
    public static final String PREDICT=  "predicttime";
    public static final String GETOUTPUT = "get_output";
    public static final String CP = "cp"; //added nikhil
    public static final String UPDATE = "update";
    public static final String SEARCH = "search";
    public static final String START = "start";
    public static final String DELETE = "delete";
    public static final String HIDE = "hide";
    public static final String SHOW_HIDDEN = "show_hidden";
    public static final String GETUSAGE = "get_usage";
    public static final String GETHARDWARE = "get_hardware";
    public static final String GETSOFTWARE = "get_software";
    public static final String ADDNOTIFICATION = "add_notification";
    public static final String REMOVENOTIFICATION = "remove_notification";
    public static final String CLEARNOTIFICATIONS = "clear_notifications";
    public static final String GETNOTIFICATIONS = "get_notifications";
    public static final String UPDATENOTIFICATION = "update_notification";
    
    protected String id;
    protected Status status;
    protected StatusListener statusListener;
    
    protected Object output;
    
    protected Hashtable<String,Object> arguments = new Hashtable<String,Object>();
    
    public JobCommand(StatusListener statusListener) {
        this.statusListener = statusListener;
        this.output = new Object();
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    public String getCommand() {
        return id;
    }
    
    public Object getOutput() {
        return output;
    }
    
    public void setOutput(Object output) {
        this.output = output;
    }
    
    public StatusListener getStatusListener() {
        return this.statusListener;
    }
    
    public Hashtable<String,Object> getArguments() {
        return arguments;
    }
    
    public void setArguments(Hashtable<String,Object> args) {
        this.arguments = args;
    }
    
    public void statusChanged(StatusEvent e) {
        this.status = e.getStatus();
        this.statusListener.statusChanged(new StatusEvent(this,this.status));
    }
    
    
    
}