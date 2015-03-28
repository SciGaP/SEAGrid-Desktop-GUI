/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Jun 23, 2005
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

package org.gridchem.client.util.file;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;

import org.gridchem.client.util.Env;

/**
 * This class handles retrieving default input files and templates distributed with the jar.
 * 
 * @author Rion Dooley < dooley [at] cct [dot] lsu [dot] edu >
 *
 */
public class FileUtility {
    
    
    
    public static ArrayList<File> getDefaultInputFiles(String application) {
        ArrayList<File> files = new ArrayList<File>();
        
        System.out.println("Retrieving " + application +  " input files.");
        
        // Get a handle on the software template dir
        File f = new File(Env.getApplicationSoftwareTemplateDir() 
                 + File.separator + application.toLowerCase() );
        
        System.out.println("Looking in " + f.getAbsolutePath());
        // For every file in that directory, include it in the job's default
        // input file set
        if (f.exists()) {
            if (f.isDirectory()) {
                for (File inFile: f.listFiles()) {
                    files.add(inFile);
                }
            } 
        }
        
        return files;
    }
    
    public static void printDefaultInput(String application, String filename, String input) {
        File f = new File(Env.getApplicationSoftwareTemplateDir() 
                + File.separator + application.toLowerCase() + File.separator + filename);
        
        
        try {
            
            if (!f.getParentFile().exists()) {
                f.mkdirs();
            }
            
            f.createNewFile();
            
            FileWriter fw = new FileWriter(f);
            fw.write(input);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Input for " + application + "\nFile name: " + f.getAbsolutePath() + "\n" + input);
        }
    }
}
