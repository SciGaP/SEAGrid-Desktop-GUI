/*Copyright (c) 2007, Center for Computational Sciences, University of Kentucky.  All rights reserved.

Developed by:

Center for Computational Sciences, University of Kentucky

http://www.ccs.uky.edu

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
3. Neither the names of Center for Computational Sciences, University of Kentucky 
   nor the names of its contributors may be used to endorse or promote products 
   derived from this Software without specific prior written permission.
    
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS WITH THE SOFTWARE.

*/
/**
 * @author Michael Sheetz 
 * @author Pavithra Koka
 * @author Shreeram
 */

package Gamess.gamessGUI;


import nanocad.newNanocad;

import org.gridchem.client.GridChem;
import org.gridchem.client.Invariants;
import org.gridchem.client.SubmitJobsWindow;
import org.gridchem.client.editJobPanel;
import org.gridchem.client.common.Settings;
import org.gridchem.client.gui.jobsubmission.EditJobPanel;

import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import javax.swing.text.*;




public class checkInputFile implements ActionListener{
	int temp;
	final public int YES=1;
    final public int NO=0;
    int keywordpresent=NO;
    int keywordinlist=NO;
	ArrayList basisInputFile=new ArrayList();
    ArrayList controlInputFile=new ArrayList();
    StyledDocument document1;
    
    String inputDocumentName="gamessInput.xml";
    String inputDocumentName1="Input.xml";

	
    public void actionPerformed(ActionEvent event) {
	try{
		if(!Settings.authenticated)
		{
			JOptionPane.showMessageDialog(GamessGUI.frame, "You are not authenticated to use this feature.\nPlease sign in to GridChem to use this feature." , "Access restriction", JOptionPane.ERROR_MESSAGE);
			return;
		}

		//Check if the document is a valid one
		//Validate the complete document once again
		
		UpdateInputFile.getInstance().UpdateAll();
		
		//Check for inconsistencies in the file
		if(UpdateInputFile.isConsistent == false)
		{
			int response = JOptionPane.showConfirmDialog (GamessGUI.frame,"There are some errors in the document.\n\n Are you sure you want to proceed in submitting the inputfile?","Errors",
			        JOptionPane.YES_NO_OPTION,
			        JOptionPane.ERROR_MESSAGE);
					if (response == JOptionPane.NO_OPTION)
						return;
		}
		
		//Check for exclude incompatibilities
		MessageBox.excludes.UpdateList();
		if(MessageBox.excludes.incompList.size() != 0)
		{
			int response = JOptionPane.showConfirmDialog (GamessGUI.frame,"There are some inconsistencies in the document.\nSome items need to be excluded to make the inputfile valid.\nPlease see the \"Exclude incompatibility\" tab in the message window below\n\n Do you still want to proceed in submitting the inputfile?", "Input incompatibilities",
			        JOptionPane.YES_NO_OPTION,
			        JOptionPane.ERROR_MESSAGE);
					if (response == JOptionPane.NO_OPTION)
						return;
		}
		
		//Check for Required incompatibilities
		MessageBox.requires.UpdateList();
		if(MessageBox.requires.incompList.size() != 0)
		{
			int response = JOptionPane.showConfirmDialog (GamessGUI.frame,"There are some inconsistencies in the document.\nSome items need to be included to make the inputfile valid.\nPlease see the \"Required incompatibility\" tab in the message window below\n\n Do you still want to proceed in submitting the inputfile?", "Input incompatibilities",
			        JOptionPane.YES_NO_OPTION,
			        JOptionPane.ERROR_MESSAGE);
					if (response == JOptionPane.NO_OPTION)
						return;
		}
		JOptionPane.showMessageDialog(GamessGUI.frame, "Your file is submitted." , ":)", JOptionPane.INFORMATION_MESSAGE);
		
//		if(GridChem.oc.sjw == null)
//            GridChem.oc.doSubmission();
		SubmitJobsWindow.getInstance();
        EditJobPanel ejp = new EditJobPanel();
        newNanocad.exportedApplication = Invariants.APP_NAME_GAMESS;
        ejp.changeApp(newNanocad.exportedApplication);
        ejp.populateMachineList(newNanocad.exportedApplication);
//        ejp.changeInputText(GamessGUI.inputFilePane.getText());
        //SubmitJobsWindow.si.mainFrame.dispose();
	}
	catch(Exception ee)
    {
	     ee.printStackTrace();
               }//catch      
	}
}//end of class
