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

/** PutFile.java
Put a file into mass storage perhaps via remSyst ( local HPC system)
*/

package org.gridchem.client;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JList.*;
import javax.swing.text.*;

import org.gridchem.client.common.Settings;


public class PutFile
{
    public File file;

    public PutFile(String fileName, String HPCsys)
    {
	file = new File(fileName);
	try
	{
	    URL cgiURL = new URL(Invariants.httpsGateway + "putfile.cgi");
	    System.err.println("PutFile: URL cgiURL " + cgiURL.toString() + 
			    " successfully initialized");
	    initCGI(file, HPCsys, cgiURL);
	    System.err.println("PutFile: initCGI completed");
	}
	catch (MalformedURLException me)
	{
	    System.err.println("PutFile: Malformed URLException");
	}
    }

    void initCGI(File f, String HPCsys, URL cgiURL)
    {
	try
	{
	    URLConnection connex = cgiURL.openConnection();
//	    char[] filetext = {};
	    connex.setDoOutput(true);
	    PrintWriter outStream = new PrintWriter(connex.getOutputStream());
	    String userName;

	    String fName = URLEncoder.encode(f.getName());
//	    try
//	    {
//		FileReader fr = new FileReader(f);
//		fr.read(filetext);
		BufferedReader br = new BufferedReader(new FileReader(f));
		
//		br.read(filetext);
//		String line = new String(filetext);
		String filetext;// = new String();
		String line;// = new String();
		String s;
		filetext = "";
		while ((line = br.readLine()) != null)
		{
		    //System.err.println(line);
//		    filetext.concat(line + "\n");
		    filetext = filetext + line + "\n";
		}
		String fText = URLEncoder.encode(filetext);
	     if (Settings.authenticatedGridChem) {
	     	userName = URLEncoder.encode("ccguser");
            outStream.println("IsGridChem=" + URLEncoder.encode("true"));
            System.err.println("PutFile:IsGridChem=" + "true");
        } else {
        	userName = URLEncoder.encode(Settings.name.getText());
            outStream.println("IsGridChem=" + URLEncoder.encode("false"));
            System.err.println("PutFile:IsGridChem=" + "false");
        }
		outStream.println("Username=" + userName);
		System.err.println("PutFile:Username="+userName);
	    outStream.println("GridChemUsername="+URLEncoder.encode(Settings.gridchemusername));
	    System.err.println("PutFile:GridChemUsername=" + URLEncoder.encode(Settings.gridchemusername));
		outStream.println("fileName=" + fName);
		System.err.println("PutFile:FileName="+fName);
		outStream.println("fileText=" + fText);
                // Send the System Name ( HPC) for mss may not be reachable directly
                outStream.println("HPCsys="+ HPCsys); 
		System.err.println("PutFile:fileText="+fText);
   
		outStream.close();
		BufferedReader inStream = new BufferedReader(new 
				InputStreamReader(connex.getInputStream()));
		while ((s = inStream.readLine()) != null)
		{
		    int sLength = s.length();
		}
		inStream.close();

		
	    }
	    catch (IOException ioe)
	    {
		System.err.println("PutFile:initCGI:IOException");
		System.err.println(ioe.toString());
		ioe.printStackTrace();
	    }
//	}
//	catch
//	{
//	}

    }
}

