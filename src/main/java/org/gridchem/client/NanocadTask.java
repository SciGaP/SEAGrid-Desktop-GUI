/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software"), to deal with the Software without 
restriction, including without limitation the rights to use, 
copy, modify, merge, publish, distribute, sublicense, and/or 
sell copies of the Software, and to permit persons to whom 
the Software is furnished to do so, subject to the following 
conditions:
1. Redistributions of source code must retain the above copyright notice, 
   this list of conditions and the following disclaimers.
2. Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimers in the documentation
   and/or other materials provided with the distribution.
3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
   University of Illinois at Urbana-Champaign, nor the names of its contributors 
   may be used to endorse or promote products derived from this Software without 
   specific prior written permission.
    
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS WITH THE SOFTWARE.

*/

/*
 * Created on May 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.gridchem.client;

import java.io.File;

import javax.swing.JFrame;

import nanocad.GetDataFile;
import nanocad.nanocadFrame2;

import org.gridchem.client.common.Settings;
import org.gridchem.client.util.Env;
 
/**
 * @author Xiaohai Li
 *
 * @see nanocadFrame2
 * @see MyLongTask
 * NanocadTask subclasses MyLongTask which uses a SwingWorker to 
 * perform a time-consuming task.
 * 
 */
public class NanocadTask extends MyLongTask implements MyLongTaskInterface {
    
    public static boolean DEBUG = true;
    private JFrame frame;

    public NanocadTask(JFrame jf) {
        super();
        frame = jf;
    }
      
    public void go() {
        current = 0; // from MyLongTask
        final SwingWorker worker = new SwingWorker() {
            public Object construct() {
                return new ActualTask();
            }
        };
        worker.start();
    }
      
}

/************************************************************
 Inner class ActualTask is need for the implementation of
 a subclass of MyLongTask.  It cannot be made static b/c of 
 the design of MyLongTask
 */
final class ActualTask {
    ActualTask () {
        //Check if molecular database is available on local machine
        if (!((new File(Settings.defaultDirStr + Settings.fileSeparator + "common")).exists()))
        {
            //nanocadFrame2.progressLabel.setText("Retrieve Molecule Database Progress");
            String zipFileName =  Env.getApplicationDataDir() + Settings.fileSeparator + "nanocaddata.zip";
            if (!((new File(zipFileName)).exists()))
            {
                nanocadFrame2.progressLabel.setText("Progress: Retrieve Molecule database from remote system...");
                GetDataFile gf = new GetDataFile(zipFileName);        
            }
            //nanocadFrame2.progressLabel.setText("Progress: unzipping molecule database");
            //ZipExtractor uz = new ZipExtractor(zipFileName);
            //System.err.println("Done with unzipping nanocaddata.zip");
        }
              
        //Check if txt files needed by nanocad is available on local machine
        if (!((new File(Settings.defaultDirStr + Settings.fileSeparator + "txt")).exists()))
        {
            //nanocadFrame2.progressLabel.setText("Retrieve .txt files Progress");
            String zipFileName = Env.getApplicationDataDir() + Settings.fileSeparator + "txt.zip";
            if (!((new File(zipFileName)).exists()))
            {
                nanocadFrame2.progressLabel.setText("Progress: Retrieve .txt files from remote system...");
                GetDataFile gf = new GetDataFile(zipFileName);        
            }
            //nanocadFrame2.progressLabel.setText("Done Retrieving files");
            //nanocadFrame2.progressLabel.setText("Progress: unzipping .txt files");
            //ZipExtractor uz = new ZipExtractor(zipFileName);
            //System.err.println("Done with unzipping txt.zip");
        }
             
        nanocadFrame2.nanocadTask.stop();
        System.err.println("nanocadTask is done");
                               
    }
}
