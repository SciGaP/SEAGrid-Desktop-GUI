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
 */

package Gamess.gamessGUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import Gamess.gamessGUI.Storage.Repository;

import java.awt.Window;
import java.awt.event.*;
import java.io.*;

public class FileMenuListener implements ActionListener
{
	private File currentFile = null;
	private JFileChooser fileChooser = new JFileChooser ();
	private Window parentWindow = null;
	private JTextComponent textEditor = null;
	private TitledBorder border = null;
	private boolean updateDatabase = false;
	private UndoManager undoManager = null;
	
	/**
	 * This is the constructor of the FileMenuListener
	 * @param _parentWindow The parent window of the save and open dialog. Value cannot be <b>null</b>
	 * @param _textEditor The textEditor present in the window. Value cannot be <b>null</b>
	 * @param _border The border sourrounding the textEditor. Can be <b>null</b>
	 * @param _undoManager The undo/redo manager that is associated with the textEditor. Can be <b>null</b>
	 * @param _updateDatabase Value specifying whether to update the Database when a new file is opeaned.
	 */
	public FileMenuListener(Window _parentWindow , JTextComponent _textEditor ,TitledBorder _border , UndoManager _undoManager , boolean _updateDatabase) 
	{
		parentWindow = _parentWindow;
		textEditor = _textEditor;
		updateDatabase = _updateDatabase;
		undoManager = _undoManager;
		border = _border;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
	    String menuName = e.getActionCommand ();
	    
	    if(menuName.equals ("New")) 
	    {
	    	//Reset the current text
	    	textEditor.setText("");
	    	//Reset current file
	    	currentFile = null;
	    	//Reset the title
	    	ResetInputFileWindowTitle("untitled");
	    	//Since a new file has been opeaned clear all the undo and redo events
	    	if(undoManager != null)
	    		undoManager.discardAllEdits();
	    	//Drop the DB if updateDatabase == true
	    	if(updateDatabase)
	    		Repository.getInstance().DropDB();
	     }
	     else if  (menuName.equals ("Open")) 
	     {
	    	// Open a file
	    	openFile ();
	    	//Since a new file has been opeaned clear all the undo and redo events
	    	if(undoManager != null)
	    		undoManager.discardAllEdits();
	    	//Drop the DB if updateDatabase == true
	    	if(updateDatabase)
	    		Repository.getInstance().DropDB();
	     } 
	     else if (menuName.equals ("Save")) 
	   	 {
	         // Save a file
	   		 if(currentFile == null)
	   			 saveFile ("Save");
	   		 else
	   		 { 
	   			 //Saving on the same file name
	   			 //Check if the writing is success(true)
	   			 writeFile (currentFile, textEditor.getText ());
	   		 }
	   	 } 
	     else if (menuName.equals ("Save As")) 
	     {
	    	 // Save the current input as a file with different name
	    	 saveFile("Save As");
	     } 
	     else if (menuName.equals ("Close") ) 
	    	 parentWindow.dispose ();
		
	} // end of actionPerformed()
	
	 /**
     * Use a JFileChooser in Open mode to select files
     * to open. Use a filter for FileFilter subclass to select
     * for *.java files. If a file is selected then read the
     * file and place the string into the textarea.
    **/
	private void openFile () 
	{
		fileChooser.setDialogTitle ("Open File");

		// Now open chooser
		int result = fileChooser.showOpenDialog (parentWindow);

		if (result == JFileChooser.APPROVE_OPTION) 
		{
           currentFile = fileChooser.getSelectedFile ();

           //Reset the title in the main window 
           ResetInputFileWindowTitle(currentFile.getName().substring(0 , currentFile.getName().indexOf(".")) ); 

           // Invoke the readFile method in this class
           String file_string = readFile (currentFile);

           if (file_string != null)
           {
        	   textEditor.setText (file_string);
        	   if(updateDatabase)
        	   {
        		   //TODO update the Database
        	   }
           }
           return;
		}
	} // openFile


	/**
     * Use a JFileChooser in Save mode to select files
     * to open. Use a filter for FileFilter subclass to select
     * for "*.java" files. If a file is selected, then write the
     * the string from the textarea into it.
    **/
    private void saveFile (String title) 
    {
    	File oldFile = currentFile;
		
    	// Set to a default name for save.
		fileChooser.setSelectedFile (currentFile);
		fileChooser.setDialogTitle(title);
		
		// Open chooser dialog
		int result = fileChooser.showSaveDialog (parentWindow);
		
		if (result == JFileChooser.APPROVE_OPTION) 
		{
			currentFile = fileChooser.getSelectedFile ();
			if (currentFile.exists ()) 
			{
				int response = JOptionPane.showConfirmDialog (null,"Overwrite existing file?","Confirm Overwrite",
		        JOptionPane.OK_CANCEL_OPTION,
		        JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.CANCEL_OPTION)
				{
					//reset currentFile to the old file
					currentFile = oldFile;
					return;
				}
			}
			//Reset the title in the main window
			ResetInputFileWindowTitle(currentFile.getName().substring(0 , currentFile.getName().indexOf(".") ));
			writeFile (currentFile, textEditor.getText ());
		} 
    } // saveFile

    
   /** Use a BufferedReader wrapped around a FileReader to read
     * the text data from the given file.
    **/
   public static String readFile (File file) 
   {
	   StringBuffer fileBuffer;
	   String fileString=null;
	   String line;
	   
	   try 
	   {
		   FileReader in = new FileReader (file);
		   BufferedReader dis = new BufferedReader (in);
		   fileBuffer = new StringBuffer () ;

		   while ((line = dis.readLine ()) != null)
			   fileBuffer.append (line + "\n");

		   in.close ();
		   fileString = fileBuffer.toString ();
	   }
	   catch  (IOException e ) {
		   return null;
	   }
	   return fileString;
   	} // readFile


   /**
     * Use a PrintWriter wrapped around a BufferedWriter, which in turn
     * is wrapped around a FileWriter, to write the string data to the
     * given file.
    **/
   public static void writeFile (File file, String dataString) 
   {
	   try 
	   {
		   PrintWriter out = new PrintWriter (new BufferedWriter (new FileWriter (file)));
		   out.print (dataString);
		   out.flush ();
		   out.close ();
	   }
	   catch (IOException e) 
	   {
		   JOptionPane.showMessageDialog (null,"IO error in saving file!!", "File Save Error", JOptionPane.ERROR_MESSAGE);
	   }
   } // writeFile
   
   /**
    * @author Shreeram
    * This is common function that is used to set the title on the 
    * window that is above the input file textarea in the main window. 
    *
    */
   private void ResetInputFileWindowTitle(String title)
   {
	   if(border != null)
	   {
		   border.setTitle("INPUT FILE"+" - "+ title);
		   GamessGUI.textPanel.repaint();
	   }
   }
}//end of class
