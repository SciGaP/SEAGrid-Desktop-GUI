/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChemtopBox.add(new JLabel("Choose a machine: "));
	topBox.add(machCombo);

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

/* editSSHJobPanel.java  by Rion Dooley
   This opens the ssh job editing panel */
package org.gridchem.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import org.gridchem.service.beans.JobBean;

public class JobHistory {
	public void JobHistory(){}
	public void write(JobBean j, String jobid, String jhf) {
		boolean append = true; 
		
		try {
			FileWriter histWriter = new FileWriter(jhf, append);

			System.err.println("JobHistory: opening ssh job output file");
			
			// Get correct date format for the history file
			Calendar cal = Calendar.getInstance(TimeZone.getDefault());
			String DATE_FORMAT = "MM/dd/yyyy";
			String TIME_FORMAT = "hh:mm";
			java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat(DATE_FORMAT);
			String currentDate = sdf1.format(cal.getTime());
			java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat(TIME_FORMAT);
			String currentTime = sdf2.format(cal.getTime());
			
			// Write the complete job description to the ssh history file...really both
			// job types should be doing this, but oh well, hack away.
			if(jobid.equals("")) jobid = "unknown";
			
			histWriter.write(jobid + " " + 
							j.getExperimentName() + " " +
							j.getName() + " " +
							j.getProjectName() + " " +
							j.getSystemName() + " " +
							j.getQueueName() + " " +
							j.getRequestedCpuTime() + " " +
							j.getSoftwareName() + " " +
							//j.getArgs() + " " +
							j.getRequestedCpus() + " " +
							currentTime + " " +
							currentDate + " " + 
							"\r\n");
			histWriter.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void read(File f, ArrayList list) {
		String s;
		try
		{
		    BufferedReader br = new BufferedReader(new FileReader(f));
		    while ((s = br.readLine()) != null)
		    {	
				int m = s.length();
				if (m > 0)
				{
				    // append s to the list
				    list.add(s);
				}
		    }
		    br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
    }
}