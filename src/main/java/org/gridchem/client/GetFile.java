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

/** GetFile.java
 * Get a file from scratch or from mass storage
 * The indentation in this file is inconsistent.
 */

package org.gridchem.client;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.gridchem.client.common.Settings;


public class GetFile
{
	
    public File file;
    public String system;

    public static boolean getfileisdone = false; //lixh_add_2_9
    /* What is the purpose of getfileisdone ?
       Since this is the only static entity and since it is assigned
       to true in the constructors (on most but not all execution
       paths), one might guess that getfileisdone indicates that
       a GetFile object has been successfully used to get a file.
       But because the catch statements herein do not perform any
       recovery, it is possible that getfileisdone is true and 
       that no file has been gotten.
       Furthermore, how should we recover if a GetFile object has been
       instantiated and yet getfileisdone is still false ?  See
       bugzilla bug 2.  Let's rename this variable when we discover
       its purpose: the getfile part is redundant and the done part
       needs to be clarified.  SRB Nov 22, 2005
    */
    
    public GetFile(String fileName)
    {
		Trace.entry();
		Trace.note( "fileName = " + fileName );
		file = new File(fileName);
		system = "mss.ncsa.uiuc.edu";
		
		try
		{
		    URL cgiURL = new URL(Invariants.httpsGateway + "getfile.cgi");
		    initCGI(file, system, cgiURL);
		}
		catch (MalformedURLException mue)
		{
		    System.err.println("GetFile: Malformed URLException");
		}
		
		Trace.exit();
    }

    public GetFile(File f)
    {
	Trace.entry();
	file = f;
	system = "mss.ncsa.uiuc.edu";
	
	try
	{
	    URL cgiURL = new URL(Invariants.httpsGateway + "getfile.cgi");
	    initCGI(file, system, cgiURL);
	}
	catch (MalformedURLException mue)
	{
	    System.err.println("GetFile: Malformed URLException");
	}
	Trace.exit();
    }
    
    
    public GetFile(String fileName, String sysName)
    {
	Trace.entry();
		file = new File(fileName);
		system = sysName;
		
		if(Settings.authenticatedSSH)
        {
                /** Transfer file from remote host to client machine via scp
                 we'll wait until v1.x for this feature.  It might not be
                 necessary with ssh to do this. **/
                // ScpFrom scp = new ScpFrom();
                // System.out.println("GetFile: Insert code here!!");
                getfileisdone = true;
//                LoginPanel.ishistFile = true;
//                LoginPanel.isprefFile = true;
                
        } else {
                try
                {
                    URL cgiURL = new URL(Invariants.httpsGateway + "getfile.cgi");
                    initCGI(file, system, cgiURL);
                }
                catch (MalformedURLException mue)
                {
                    System.err.println("GetFile: Malformed URLException");
                }
        }
	Trace.exit();
    }

    public GetFile(File f, String sysName)
    {
	Trace.entry();
	file = f;
	system = sysName;
	try
	{
	    URL cgiURL = new URL(Invariants.httpsGateway + "getfile.cgi");
	    initCGI(file, system, cgiURL);
	}
	catch (MalformedURLException mue)
	{
	    System.err.println("GetFile: Malformed URLException");
	}
	Trace.exit();
    }
    
    public GetFile(String fileName, String sysName, String HPCsys)
    {
	Trace.entry();
        file = new File(fileName);
        system = sysName;
        
        try
        {
            URL cgiURL = new URL(Invariants.httpsGateway + "getfile.cgi");
            init1CGI(file, system, HPCsys, cgiURL);
        }
        catch (MalformedURLException mue)
        {
            System.err.println("GetFile: Malformed URLException");
        }
	Trace.exit();
    }
    
    public GetFile(File f, String sysName, String HPCsys)
    {
	Trace.entry();
        file = f;
        system = sysName;
        try
        {
            URL cgiURL = new URL(Invariants.httpsGateway + "getfile.cgi");
            init1CGI(file, system, HPCsys, cgiURL);
        }
        catch (MalformedURLException mue)
        {
            System.err.println("GetFile: Malformed URLException");
        }
	Trace.exit();
    }

    public GetFile(File from, File to, String sysName, String HPCsys)
    {
	Trace.entry();
    	system = sysName;
    	try
		{
    		URL cgiURL = new URL(Invariants.httpsGateway + "getfile.cgi");
    		init2CGI(from, to, system, HPCsys, cgiURL);			
		}
    	catch (MalformedURLException mue)
		{
    		System.err.println("GetFile: Malformed URLException");
		}
	Trace.exit();
    }
    
    void initCGI(File f, String system, URL cgiURL)
    {
	Trace.entry();
	String line;
	boolean append = false;
	try
	{
	    java.net.URLConnection connex = cgiURL.openConnection();
	    connex.setDoOutput(true);
	    PrintWriter outStream = new PrintWriter(connex.getOutputStream());
	    String fName = URLEncoder.encode(f.getName());
	    String sys = URLEncoder.encode(system);

	    String userName;
        if (Settings.authenticatedGridChem) {
    	    		userName = URLEncoder.encode("ccguser");
    	    		outStream.println("IsGridChem=" + URLEncoder.encode("true"));
    	    		System.err.println("GetFile:IsGridChem=" + "true");
        } else {
    	    		userName = URLEncoder.encode(Settings.name.getText());
    	    		outStream.println("IsGridChem=" + URLEncoder.encode("false"));
    	    		System.err.println("GetFile:IsGridChem=" + "false");
        }
	    outStream.println("Username=" + userName);
	    System.err.println("GetFile:Username=" + userName);
	    outStream.println("GridChemUsername=" + Settings.gridchemusername);
	    System.err.println("GetFile:GridChemUsername=" + Settings.gridchemusername);
	    outStream.println("System=" + sys);
	    System.err.println("GetFile:System=" + sys);
	    outStream.println("fileName=" + fName);
	    System.err.println("GetFile:fileName=" + fName);


	    outStream.close();

	    BufferedReader inStream = new BufferedReader(new
			    InputStreamReader(connex.getInputStream()));
	    FileWriter fw = new FileWriter(f, append);
	    while ((line = inStream.readLine()) != null) {
		    int m = line.length();
			if (m > 0) {
				if (m != 5 && line != "ERROR"){
			    fw.write(line + "\n");
				}
			    //System.err.println(line);
			}
	    }
	    fw.close();
	} catch (IOException ioe) {
	    System.err.println("GetFile:initCGI:IOException");
	    System.err.println(ioe.toString());
	    ioe.printStackTrace();
	}
	// Use Preferences statically since Preferences construction may use GetFile
   // Temporarily disable getting the preferences from mss.
	//if (f.getPath().indexOf(Preferences.getLocalPrefFilename()) >= 0)
	{
//		LoginPanel.isprefFile = true;
	}
	if (f.getPath().equals(Settings.histFilename))
	{
//		LoginPanel.ishistFile = true;
	}
	getfileisdone = true;   //lixh_add
	Trace.exit();
    }
    
    void init1CGI(File f, String system, String HPCsys, URL cgiURL)
    {
    		Trace.entry();
        String line;
        boolean append = false;
        try {
        	URLConnection connex = cgiURL.openConnection();
            connex.setDoOutput(true);
            PrintWriter outStream = new PrintWriter(connex.getOutputStream());
            String fName = URLEncoder.encode(f.getName());
            String sys = URLEncoder.encode(system);
            HPCsys = URLEncoder.encode(HPCsys);

    	    String userName;
            if (Settings.authenticatedGridChem) {
        	    userName = URLEncoder.encode("ccguser");
                outStream.println("IsGridChem=" + URLEncoder.encode("true"));
                System.err.println("GetFile:IsGridChem=" + "true");
            } else {
        	    userName = URLEncoder.encode(Settings.name.getText());
                outStream.println("IsGridChem=" + URLEncoder.encode("false"));
                System.err.println("GetFile:IsGridChem=" + "false");
            }
            outStream.println("Username=" + userName);
            System.err.println("GetFile:Username=" + userName);
    	    	outStream.println("GridChemUsername=" + Settings.gridchemusername);
    	    System.err.println("GetFile:GridChemUsername=" + Settings.gridchemusername);
            outStream.println("System=" + sys);
            System.err.println("GetFile:System=" + sys);
            outStream.println("fileName=" + fName);
            System.err.println("GetFile:fileName=" + fName);
            outStream.println("HPCsys=" + HPCsys);
            System.err.println("GetFile:HPCsys=" + HPCsys);


            outStream.close();

            BufferedReader inStream = new BufferedReader(new
                            InputStreamReader(connex.getInputStream()));
            FileWriter fw = new FileWriter(f, append);
            int j = 0; // lixh_add
            while ((line = inStream.readLine()) != null)
            {
            	j++;	
                int m = line.length();
                if (m > 0 && j > 3)
                {
                	if (m != 5 && line != "ERROR"){
                    fw.write(line + "\n");
                	}
                    System.err.println(line);
                }
            }
            fw.close();
        }
        catch (IOException ioe)
        {
            System.err.println("GetFile:initCGI:IOException");
            System.err.println(ioe.toString());
            ioe.printStackTrace();
        }
    	//if (f.getPath().equals(Settings.prefFilename))
    	{
//    		LoginPanel.isprefFile = true;
    	}
    	if (f.getPath().equals(Settings.histFilename))
    	{
//    		LoginPanel.ishistFile = true;
    	}
        getfileisdone = true;  //lixh_add_2_9
	Trace.exit();
    }
    
	void init2CGI(File from, File to, String system, String HPCsys, URL cgiURL)
	{
	    Trace.entry();
        String line;
        boolean append = false;
        System.out.println("GetFile(2):init2CGI: File " + from.getPath() +" to be obtained from " + system + " through " + HPCsys);
        try
        {
            URLConnection connex = cgiURL.openConnection();
            connex.setDoOutput(true);
            PrintWriter outStream = new PrintWriter(connex.getOutputStream());
            //String fromName = URLEncoder.encode(from.getName());//Sudhakar 30 Sep 05
            String fromName = URLEncoder.encode(from.getPath());
            String sys = URLEncoder.encode(system);
            HPCsys = URLEncoder.encode(HPCsys);
            String userName;
            
            if (Settings.authenticatedGridChem) {
        	    		userName = URLEncoder.encode("ccguser");
        	    		outStream.println("IsGridChem=" + URLEncoder.encode("true"));
        	    		if (Settings.DEBUG)
        	    		    System.err.println("GetFile2:IsGridChem=" + "true");
            } else {
        	    		userName = URLEncoder.encode(Settings.name.getText());
        	    		outStream.println("IsGridChem=" + URLEncoder.encode("false"));
        	    		if (Settings.DEBUG)
        	    		    System.err.println("GetFile2:IsGridChem=" + "false");
            }
            
            outStream.println("Username=" + userName);
            outStream.println("GridChemUsername=" + URLEncoder.encode(Settings.gridchemusername));
            outStream.println("System=" + sys);
            outStream.println("fileName=" + fromName);
            outStream.println("HPCsys=" + HPCsys);
            
            if (Settings.DEBUG) {
                System.err.println("GetFile2:Username=" + userName);
                System.err.println("GetFile2:GridChemUsername=" + URLEncoder.encode(Settings.gridchemusername));
                System.err.println("GetFile2:System=" + sys);
                System.err.println("GetFile2:fileName=" + fromName);
                System.err.println("GetFile2:HPCsys=" + HPCsys);
            }
            
            if (Settings.authenticatedGridChem) {
                outStream.println("IsGridChem=" + URLEncoder.encode("true"));
                if (Settings.DEBUG)
                    System.err.println("GetFile2:IsGridChem=" + "true");
            } else {
                outStream.println("IsGridChem=" + URLEncoder.encode("false"));
                if (Settings.DEBUG)
                    System.err.println("GetFile2:IsGridChem=" + "false");
            }

            outStream.close();

            BufferedReader inStream = new BufferedReader(new
                            InputStreamReader(connex.getInputStream()));
            FileWriter fw = new FileWriter(to, append);
            int j = 0; // lixh_add
            while ((line = inStream.readLine()) != null)
            {
            		j++;	
                int m = line.length();
                if (m > 0 && j > 3)
                {
                	if (m != 5 && line != "ERROR"){
                    fw.write(line + "\n");
                }
                    if (Settings.DEBUG)
                        System.err.println(line);
                }
            }
            fw.close();
        }
        catch (IOException ioe)
        {
            System.err.println("GetFile:initCGI:IOException");
            System.err.println(ioe.toString());
            ioe.printStackTrace();
        }
        getfileisdone = true;  //lixh_add_2_9
        System.out.println("GetFile2: File "+from.getPath()+" retrieved successfully to "+to.getPath());
	Trace.exit();
	}

}    

