/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software") to deal with the Software without
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

// GetDataFile.java

package nanocad;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import org.gridchem.client.common.Settings;

public class GetDataFile
{
    public File file;
    public String system;
    public String httpsGateway;
    public String userid;

    public GetDataFile(String fileName)
    {
	file = new File(fileName);
	system = "mss.ncsa.uiuc.edu";

	String setsfile = ".settings";
	String line;
	StringTokenizer st;
	String temp;
	
	try
	{
		//lixh_4/27/05
	    File sets = new File(newNanocad.applicationDataDir + 
	    		newNanocad.fileSeparator + setsfile);
	    //File sets = new File(System.getProperty("user.dir") +
		//		System.getProperty("file.separator") + setsfile);
	    BufferedReader br = new BufferedReader(new FileReader(sets));
	
	    line = br.readLine();
	    st = new StringTokenizer(line);
	    temp = st.nextToken();
	    userid = st.nextToken();
	    line = br.readLine();
	    st = new StringTokenizer(line);
	    temp = st.nextToken();
	    httpsGateway = st.nextToken();
	
	    br.close();
	}
	catch (IOException ioe) {}

//	httpsGateway = "https://swarna.ncsa.uiuc.edu/GAUSMON/";
	
	try
	{
	    URL cgiURL = new URL(httpsGateway + "getzipfile.cgi");
	    initCGI(file, system, cgiURL);
	}
	catch (MalformedURLException mue)
	{
	    System.err.println("GetDataFile: Malformed URLException");
	}
    }

    public GetDataFile(File f)
    {
	file = f;
	system = "mss.ncsa.uiuc.edu";
	//httpsGateway = "https://swarna.ncsa.uiuc.edu/GAUSMON/";
	
	String setsfile = ".settings";
	String line;
	StringTokenizer st;
	String temp;
	
	try
	{
		File sets = new File(newNanocad.applicationDataDir + 
	    		newNanocad.fileSeparator + setsfile);
	    //File sets = new File(System.getProperty("user.dir") +
		//		System.getProperty("file.separator") + setsfile);
	    BufferedReader br = new BufferedReader(new FileReader(sets));
	
	    line = br.readLine();
	    st = new StringTokenizer(line);
	    temp = st.nextToken();
	    userid = st.nextToken();
	    line = br.readLine();
	    st = new StringTokenizer(line);
	    temp = st.nextToken();
	    httpsGateway = st.nextToken();

	    br.close();
	}
	catch (IOException ioe) {}

	try
	{
	    URL cgiURL = new URL(httpsGateway + "getzipfile.cgi");
	    initCGI(file, system, cgiURL);
	}
	catch (MalformedURLException mue)
	{
	    System.err.println("GetDataFile: Malformed URLException");
	}
    }
    public GetDataFile(String fileName, String sysName)
    {
	file = new File(fileName);
	system = sysName;
	//httpsGateway = "https://swarna.ncsa.uiuc.edu/GAUSMON/";
	String setsfile = ".settings";
	String line;
	StringTokenizer st;
	String temp;
	
	try
	{
		File sets = new File(newNanocad.applicationDataDir + 
	    		newNanocad.fileSeparator + setsfile);
	    //File sets = new File(System.getProperty("user.dir") +
		//		System.getProperty("file.separator") + setsfile);
	    BufferedReader br = new BufferedReader(new FileReader(sets));
	
	    line = br.readLine();
	    st = new StringTokenizer(line);
	    temp = st.nextToken();
	    userid = st.nextToken();
	    line = br.readLine();
	    st = new StringTokenizer(line);
	    temp = st.nextToken();
	    httpsGateway = st.nextToken();
	
	    br.close();
	}
	catch (IOException ioe) {}
	try
	{
	    URL cgiURL = new URL(httpsGateway + "getzipfile.cgi");
	    initCGI(file, system, cgiURL);
	}
	catch (MalformedURLException mue)
	{
	    System.err.println("GetDataFile: Malformed URLException");
	}
    }

    public GetDataFile(File f, String sysName)
    {
	file = f;
	system = sysName;
	//httpsGateway = "https://swarna.ncsa.uiuc.edu/GAUSMON/";

	String setsfile = ".settings";
	String line;
	StringTokenizer st;
	String temp;
	
	try
	{
		File sets = new File(newNanocad.applicationDataDir + 
	    		newNanocad.fileSeparator + setsfile);
	    //File sets = new File(System.getProperty("user.dir") +
		//		System.getProperty("file.separator") + setsfile);
	    BufferedReader br = new BufferedReader(new FileReader(sets));
	
	    line = br.readLine();
	    st = new StringTokenizer(line);
	    temp = st.nextToken();
	    userid = st.nextToken();
	    line = br.readLine();
	    st = new StringTokenizer(line);
	    temp = st.nextToken();
	    httpsGateway = st.nextToken();
	
	    br.close();
	}
	catch (IOException ioe) {}

	try
	{
	    URL cgiURL = new URL(httpsGateway + "getzipfile.cgi");
	    initCGI(file, system, cgiURL);
	}
	catch (MalformedURLException mue)
	{
	    System.err.println("GetDataFile: Malformed URLException");
	}
    }

    void initCGI(File f, String system, URL cgiURL)
    {
	String line;
	boolean append = false;
	String dirOnMss = "/chembiodata/nanocad/";
	try
	{
	    URLConnection connex = cgiURL.openConnection();
	    connex.setDoOutput(true);
	    PrintWriter outStream = new PrintWriter(connex.getOutputStream());
	    //String userName = URLEncoder.encode(Settings.name.getText());
	    String userName = URLEncoder.encode(userid);
	    String fName = URLEncoder.encode(dirOnMss+f.getName());
	    String sys = URLEncoder.encode(system);

        if (Settings.authenticatedGridChem) {
    	    userName = URLEncoder.encode("ccguser");
            outStream.println("IsGridChem=" + URLEncoder.encode("true"));
            System.err.println("GetDataFile:IsGridChem=" + "true");
        } else {
            outStream.println("IsGridChem=" + URLEncoder.encode("false"));
            System.err.println("GetDataFile:IsGridChem=" + "false");
        }
	    outStream.println("Username=" + userName);
	    System.err.println("GetDataFile:Username=" + userName);
	    outStream.println("GridChemUsername=" + Settings.gridchemusername);
	    System.err.println("GetDataFile:GridChemUsername=" + Settings.gridchemusername);
	    outStream.println("System=" + sys);
	    System.err.println("GetDataFile:System=" + sys);
	    outStream.println("fileName=" + fName);
	    System.err.println("GetDataFile:fileName=" + fName);

	    outStream.close();

	 //   BufferedReader inStream = new BufferedReader(new
//			    InputStreamReader(connex.getInputStream()));
	    InputStream inStream = connex.getInputStream();
	    //OutputStreamWriter fw = new OutputStreamWriter(new 
	//		    FileOutputStream(f, append));
	    FileOutputStream fw = new FileOutputStream(f, append);
	    /*
	    while ((line = inStream.readLine()) != null)
	    {
		int m = line.length();
		if (m > 0)
		{
		    fw.write(line + "\n");
		    //System.err.println(line);
		}
	    }
	    */
	    int sChunk = 8192;
	    byte[] buffer = new byte[sChunk];
	    
	    int length;
	    while ((length = inStream.read(buffer, 0, sChunk)) != -1)
		fw.write(buffer, 0, length);
	    
	    fw.close();
	}
	catch (IOException ioe)
	{
	    System.err.println("GetDataFile:initCGI:IOException");
	    System.err.println(ioe.toString());
	    ioe.printStackTrace();
	}
    }
}    
