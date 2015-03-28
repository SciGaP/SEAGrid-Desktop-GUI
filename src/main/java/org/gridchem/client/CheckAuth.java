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


/** CheckAuth.java
Put a file into mass storage
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


public class CheckAuth
{
    public boolean authorized;

    public CheckAuth()
    {
	if (!(Settings.authenticated))
	{
	    authorized = false;
	}
	else
	{
	    authorized = true;
	    /*
	    try
	    {
		URL url  = new URL("https://swarna.ncsa.uiuc.edu:8443/GAUSMON/auth_check.cgi");
		URLConnection conauth = url.openConnection();
		conauth.setDoOutput(true);
		PrintWriter athoutStr = new PrintWriter(conauth.getOutputStream());
		String rem_usr = URLEncoder.encode(tfname.getText());
		athoutStr.println("Rem_User=" + rem_usr);
		String rem_usr_paswd = URLEncoder.encode(tfpaswd.getText());
		athoutStr.println("Rem_User_Paswd=" + rem_usr_paswd);
		athoutStr.close();


		BufferedReader athinStr = new BufferedReader(new InputStreamReader(conauth.getInputStream()));
       //      String authinpLine;
		authinpLine = athinStr.readLine();
       //      while ( (authinpLine = athinStr.readLine()) != null )
		while ( authinpLine != null )
		{
         //System.out.println("auth_Check: " + authinpLine);
         //System.out.println("auth_Check:sse " + authinpLine.substring(0,5));
		    if (authinpLine.substring(0,4).equals("User"))
		    { 
			auth_mesg  = "OK";
            //System.out.println("User Authenticated ");
			return;
		    }
		    else if (authinpLine.substring(0,5).equals("ERROR")) 
		    { 
			auth_mesg  = "ERROR";
            // String errmesg = authinpLine + ": Try Again! ";
            // ErrorDialog authnote = new ErrorDialog(new Frame(), "Authentication Protocol", errmesg);
            // authnote.setVisible(true);
			System.out.println("Error in Authentication ");
			return;
		    }
		}
	    }
	    catch (IOException ioe) {}
	    */
	}
    }
}
