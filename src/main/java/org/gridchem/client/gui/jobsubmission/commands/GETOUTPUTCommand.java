/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Jul 3, 2006
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

import java.io.File;

import org.gridchem.client.Trace;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.util.GMS3;

public class GETOUTPUTCommand extends JobCommand{

    /**
     * @param gms
     * @param statusListener
     */
    public GETOUTPUTCommand(StatusListener statusListener) {
        super(statusListener);
     
         //this.id = GETOUTPUT; commented nik
        this.id = CP;
    }

    /* (non-Javadoc)
     * @see org.gridchem.client.gui.jobsubmission.commands.JobCommand#getOutput()
     */
    public File getOutput() {
        return (File)output;
    }

    /* (non-Javadoc)
     * @see org.gridchem.client.gui.jobsubmission.commands.JobCommand#setOutput(java.lang.Object)
     */
    public void setOutput(File output) {
        this.output = output;
    }



    public void execute() throws Exception {
        Trace.entry();
        GMS3.getFile1((String)arguments.get("host"), (String)arguments.get("path"),this);
 //       GMS3.getJobOutput(this);
        
        Trace.exit();
        
    }
    
    

}
