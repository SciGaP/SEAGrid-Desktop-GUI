/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Jun 14, 2006
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

package org.gridchem.client.gui.filebrowser.commands;

import java.util.Hashtable;

import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.interfaces.GridCommand;
import org.gridchem.client.interfaces.StatusListener;

/**
 * This the base wrapper class for file commands to the GMS service.
 * It should serve as a pattern for interaction with the middleware.
 * All commands are derived from this class.  The child declarations 
 * define the id of the command (ie LS, CP, MKDIR) and handle the 
 * return of the data appropriately.  This class is also very useful
 * because it acts as a <code>StatusListener</code> for the calling 
 * object.  In this manner, the calling object is notified when 
 * a task completes and appropriate actions can be taken.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public abstract class FileCommand implements GridCommand, StatusListener{
    public static final String LS = "ls";
    public static final String PWD = "pwd";
    public static final String MKDIR = "mkdir";
    public static final String RM = "rm";
    public static final String REFRESH = "refresh";
    public static final String CP = "cp";
    public static final String DP = "dp";
    public static final String MV = "mv";
    public static final String CD = "cd";
    public static final String START = "start";
    public static final String RMFILE = "rmfile";
    public static final String RMDIR = "rmdir";
    public static final String PUTFILE = "putfile";
    public static final String GETFILE = "getfile";
    public static final String PUTDIR = "putdir";    
    public static final String GETDIR = "getdir";
    public static final String URLCOPY = "urlcopy";
    public static final String STOP = "stop";
    public static final String ISDIRECTORY = "isdirectory";
    public static final String UPDIR = "updir";
    public static final String GETBLOCK = "getblock";
    
    
    protected String id;
    protected Status status;
    protected StatusListener statusListener;
    
    protected Object output;
    
    protected Hashtable<String,Object> arguments = new Hashtable<String,Object>();
    
    public FileCommand(StatusListener statusListener) {
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
