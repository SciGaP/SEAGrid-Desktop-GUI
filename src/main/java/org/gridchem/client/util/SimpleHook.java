/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Oct 5, 2005
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

package org.gridchem.client.util;

import java.io.File;

import org.gridchem.client.GridChem;
import org.gridchem.client.Trace;
import org.gridchem.client.common.Settings;


/**
 * Insert Template description here.
 * 
 * @author Rion Dooley < dooley [at] cct [dot] lsu [dot] edu >
 *
 */
public class SimpleHook {

    public static void main(String[] args) {
        new SimpleHook();
    }

    public SimpleHook() {
        // set up service termination hook (gets called
        // when the JVM terminates from a signal):
        MyShutdown sh = new MyShutdown(this);
        Runtime.getRuntime().addShutdownHook(sh);
    }

    /**
     * Free resources
     */
    public void freeResources() {
        Trace.entry();
        try {
            if (Settings.WEBSERVICE) {
                
//                GMSSession.getInstance().destroySession();
                
            } else {
                if(Settings.authenticatedGlobus || Settings.authenticatedGridChem || 
                    Settings.authenticatedKerberos || Settings.authenticatedSSH)
                {
                    /*JOptionPane.showMessageDialog(null, "Bye Now! " + 
                            "Saving personal preferences and job history on CCG.",
                            "GridChem Shutdown", 
                            JOptionPane.INFORMATION_MESSAGE);
                    */
                    
                    //GridChem.oc.getTopLevelAncestor().setVisible(false);
                    
                    if(Settings.DEBUG) 
                        System.out.println("Saving job history on CCG.");
                    // Local and mss saving of preferences is now an atomic
                    // operation.  The same for history is needed. Nov 28, 2005 SRB
//                    PutFile pf2 = new PutFile(Settings.histFilename,Settings.mss);
                    if(Settings.DEBUG) 
                        System.out.println("Simplehook: Done with saving files");  
        
    	    		 	File directoryFile = new File(Settings.defaultDirStr + 
    	    				    Settings.fileSeparator + "common");
    	    		    
    	    		    if (directoryFile.exists()) {
    	    		        GridChem.oc.doDeletion(directoryFile);
    	    		        directoryFile.delete();
    	    		    }
    	    		    if (GridChem.oc.getTopLevelAncestor() != null ) {
    	    		    	System.out.println(" SimpleHook:Closing GridChem Components");
    	    		    //GridChem.oc.getTopLevelAncestor().setVisible(false);
    	    		    //GridChem.oc.getTopLevelAncestor().setEnabled(false); 
    	    		    /*commented out above statements as they were hanging exit process
    	    		     * at this point
    	    		     */
    	    		    	
    	    		    System.out.println("SimpleHook: Closed Top Level GridChem Windows");
    	    		    }
                }
            }
            //System.out.println("*********test********");
            System.out.println("Shutdown complete.\nGoodbye!");
            
        } catch (Exception ee) {
            System.out.println("Error closing application: " + ee);
            ee.printStackTrace();
        }
        Trace.exit();
    }

    /**
     * The finalize method will be called to close the Hibernate session
     *
     * @throws Throwable
     */
    protected void finalize() throws Throwable {
        try {
            freeResources();
        } finally {
            super.finalize();
        }
    }
}



/**
 * Shutdown hook class
 */
class MyShutdown extends Thread {
    public MyShutdown(SimpleHook managedClass) {
        super();
        this.managedClass = managedClass;
    }

    private SimpleHook managedClass;
    
    public void run() {
        System.out.println("Shutting down GridChem client...");
        try {
            String KillPhrase = "KILL /F /FI " + '"' + "WINDOWTITLE eq GridChem "+Env.version + '"' + " /IM javaw.exe /T";
            String TaskKillPhrase = "TASKKILL /F /FI " + '"' + "WINDOWTITLE eq GridChem "+Env.version + '"' + " /IM javaw.exe /T";
            String commonCopy=Env.getApplicationDataDir()+File.separator+"*.output";
            managedClass.freeResources();
            
            String osName = System.getProperty("os.name");
            System.out.println("OS is: " + osName);
            if (osName.startsWith("Windows")){
            	commonCopy= '"'+commonCopy+'"';
            	Runtime.getRuntime().exec("cmd.exe /c del " +commonCopy);
            	Runtime.getRuntime().exec("cmd.exe /c del molden.out");
            	System.out.println("cmd.exe /c del " +commonCopy);
            	System.out.println("cmd.exe /c del molden.out");
            if (osName.startsWith("Windows XP") ||osName.startsWith("Windows 2003")) {
                Runtime.getRuntime().exec("cmd.exe /c " + TaskKillPhrase);
                System.out.println("SimpleHook: " + TaskKillPhrase);
            } else {
               Runtime.getRuntime().exec("cmd.exe /c " + KillPhrase);
               System.out.println("SimpleHook: " + KillPhrase);}
            } else {
            	//os is linux or MAC
            	Runtime.getRuntime().exec("rm " + commonCopy);
            	Runtime.getRuntime().exec("rm molden.out");
            }
            System.out.println(" Simplehook: Freed Resources");
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
}

