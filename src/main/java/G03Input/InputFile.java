/*

Copyright (c) 2005, Center for Computational Sciences, University of Kentucky.  All rights reserved.

Developed by:

Center for Computational Sciences, University of Kentucky

http://www.ccs.uky.edu/

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
3. Neither the names of Center for Computational Sciences, University of Kentucky, 
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
 * Created on Apr 6, 2005
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta  @author Sandeep Kumar Seethaapathy 
 * 
 */

package G03Input;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.io.*;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.StyledDocument;

public class InputFile extends JFrame implements ActionListener {

    private static JFileChooser inputChooser;
    private static FileFilter inputFilter;
    public static JFrame inputFrame; 
    public static JMenuBar inputBar;
    public static JMenu fileMenu,editMenu,molEditor;
    public static JMenuItem open,close,save,saveas,cut,copy,paste,selectall;
    public static JPanel inputPanel,textPanel;
    public static JTextArea inputText;
    public static String tempinput="";
    public static int inputsaved=0,choiceclose,inputfetched;
    
    
    public InputFile()
    {
       textPanel=new JPanel(new GridBagLayout());
       GridBagConstraints c = new GridBagConstraints();
       c.fill=GridBagConstraints.BOTH;
       
       
       JFrame.setDefaultLookAndFeelDecorated(true);
    	JDialog.setDefaultLookAndFeelDecorated(true);
    	inputFrame = new JFrame("Input File");
    	inputFrame.setSize(500,250);
    	inputFrame.setLocation(25,50);
    	
    	inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	//mainFrame.setSize(screenSize.width-20,screenSize.height-100);
    	//molFrame.setSize(screenSize.width-280,screenSize.height-240);
    	
    	
    	inputPanel = new JPanel(new BorderLayout());
    	
    	inputBar = new JMenuBar();
    	fileMenu = new JMenu("File");
    	editMenu = new JMenu("Edit");
    	
    	
    	open = new JMenuItem("Open");
    	close = new JMenuItem("Close");
    	save = new JMenuItem("Save");
    	saveas = new JMenuItem("SaveAs");
    	
    	cut = new JMenuItem("Cut");
    	copy = new JMenuItem("Copy");
    	paste = new JMenuItem("Paste");
    	selectall  = new JMenuItem("Select All");
    	
    	
    	fileMenu.add(open);
    	fileMenu.add(save);
    	fileMenu.add(new JSeparator());
    	fileMenu.add(close);
    	editMenu.add(cut);
    	editMenu.add(copy);
    	editMenu.add(paste);
    	editMenu.add(new JSeparator());
    	editMenu.add(selectall);
    	
    
    	cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        selectall.addActionListener(this);
        save.addActionListener(this);
        close.addActionListener(this);
        open.addActionListener(this);
    	
    	
    	inputBar.add(fileMenu);
    	inputBar.add(editMenu);
    	
    	
    	inputPanel.add(inputBar,BorderLayout.NORTH);
    	
    	inputText = new JTextArea("",35,70);
    	
    	System.out.println(tempinput);
    	if(tempinput!=null)
    	{
    	    inputText.setText(tempinput);
    	    inputText.setCaretPosition(tempinput.length());
    	}
    	//
    	 else
    	inputText.setCaretPosition(0);
    	inputText.setMargin(new Insets(5,5,5,5));
    	inputText.setLineWrap(true);
       // StyledDocument styledDoc = inputText.getStyledDocument();
      
         
    	//
    	JScrollPane mScroll = new JScrollPane(inputText);
    	mScroll.setWheelScrollingEnabled(true);
    	
    	inputPanel.add(mScroll,BorderLayout.CENTER);
    	//inputPanel.add(textPanel,BorderLayout.CENTER);
    	inputFrame.getContentPane().add(inputPanel);
    	//molFrame.getContentPane().add(textPanel);
    	inputFrame.pack();
    	inputFrame.setResizable(false);
    	inputFrame.setVisible(false);
    }
    
    
    
    public static void getFile(String filepath)
    {
        // copies the file from user specified location to
        // the buffer tempinput
        try
		{
            // Open the file that is the first 
            FileInputStream fstream = new FileInputStream(filepath);
                                   
			DataInputStream in =  new DataInputStream(fstream);
			tempinput="";
            while (in.available() !=0)
			{
                                    // Print file line to screen
				tempinput+=in.readLine();
				tempinput+="\n";
			}

			in.close();
		} 
        
        catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,"File input error","Error",JOptionPane.ERROR_MESSAGE);
		}

        inputfetched=1;
        new G03Input.InputfileReader();
        
        
        
        /*Give the Geometry Section of the file to MolEditor for Display
          It will be saved in tempmolFromInput String					*/
        showMolEditor.tempmol = new String(InputfileReader.geom);
        G03MenuTree.nanocadNotice.setText("Molecular Specification Imported from Inputfile");
      //  System.out.println("This is  a test"+showMolEditor.tempmolFromInput);
    }
    
    public void doFetchInputFile()
    {
        //      Open a FileChooser with *.com Filter    
        
        inputChooser = new JFileChooser();
    	inputFilter = new FileFilter()
		{
    		public boolean accept(File f)
    		{
    		if(f.isDirectory()) return true;
    		return f.getName().endsWith(".com");
    		}
    
    		public String getDescription()
    		{
    			return "(*.com) Input Files";
    		}
   
    };
        inputChooser.setFileFilter(inputFilter);
    	            	
    	int state = inputChooser.showOpenDialog(null);
        File file = inputChooser.getSelectedFile();

        if(file != null &&
          state == JFileChooser.APPROVE_OPTION) {
         // JOptionPane.showMessageDialog(
         //             null, file.getPath());
          getFile(file.getPath());
          
        }
        else if(state == JFileChooser.CANCEL_OPTION) {
          JOptionPane.showMessageDialog(
                      null, "Input File Selection Canceled");
        }
        
     
    }

    void replaceMolFile(){
    	BufferedReader br= new BufferedReader(new StringReader(InputFile.tempinput));
    	boolean matchedcharge=false,matchedmol=false;
    	StringBuffer molString=new StringBuffer();
    	String temp="$";
    	System.out.println("InputFile.java -> Inside replaceMolFile");
    	
    	//Reads the input file until the charge and multiplicity are obtained
    	while(matchedcharge==false)
    	{
    		try {
				temp=br.readLine();
				
				if(temp==null) //EOF no luck
					{
					System.out.println("InputFile.java -> Inside replaceMolFile -> temp== null EOF");
					break;
					}
				
				matchedmol=Pattern.matches("[A-Z][a-z]?[\\s]+[\\+|\\-]?[0-9]+\\.?[0-9]+[\\s]+[\\+|\\-]?[0-9]+\\.?[0-9]+[\\s]+[\\+|\\-]?[0-9]+\\.?[0-9]+[\\s]*",temp);
				
				if(matchedmol==true) // Charge & multiplicity not yet entered Mol. Specification exists 
				{
					System.out.println("InputFile.java -> Inside replaceMolFile -> Found Mol Specification Getting out of the Loop");
					break;
					
				}
					
				matchedcharge= Pattern.matches("[\\s]*[\\+|\\-]?[0-9]+\\.?[0-9]*[\\s]*[\\+|\\-]?[0-9]+\\.?[0-9]*[\\s]*",temp);
								
				System.out.println("InputFile.java -> Inside replaceMolFile -> Still in the Loop");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    	
    	while(temp!=null) // Till the end of file
    	{
    		System.out.println("InputFile.java -> Inside replaceMolFile() -> Second Loop");
    		try {
				if(matchedmol!=true)
				{	temp=br.readLine();
					System.out.println("InputFile.java -> Inside replaceMolFile() -> Second Loop ->  matchedmol is not true");
					matchedmol=true;
				}
				
				molString.append(temp+"\n");
				System.out.println("InputFile.java -> Inside replaceMolFile() -> Second Loop-> Appended the temp"+temp+"$$");
				temp=br.readLine();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
    	}
    	
    	showMolEditor.tempmol = molString.toString();
    	/*inputString.append(tempmol);
    	InputFile.tempinput=inputString.toString();
    	*/
    	
    }
    
    
    
    
    public void actionPerformed(ActionEvent ae)
    {
        if(ae.getSource()== G03MenuTree.importFile)
        {
        	
       //Code to check if the input file needs to be overwritten if there are GUI selections
        	//  Uky 09/08/05
        	
        	if((G03MenuTree.nooflpText.getText().toString().length() > 0)||
        			(G03MenuTree.noofspText.getText().toString().length() > 0)||
        			(G03MenuTree.dynmemText.getText().toString().length() > 0)||
        			(RouteClass.routeBuffer.toString().length() > 4)||
					(G03MenuTree.molCharge.getText().toString().length() > 0) ||
					(G03MenuTree.molMultiplicity.getText().toString().length() > 0) ||
        			(G03MenuTree.filnamArea.getText().toString().length() > 0)||
					(G03MenuTree.nooflpText.getText().toString().length() > 0))
		
        	{
        	        	int inputChoice = JOptionPane.showConfirmDialog(null,"GUI selections already made for Input File. Do you still want to Fetch an input File","Overwrite Warning",JOptionPane.YES_NO_OPTION);
        	        	if(inputChoice == 0)
        	        	{
        	        		doFetchInputFile();
        	        	}
        	        	else
        	        	{
        	        	}
        	}
        	else
        	{
        	if(inputfetched == 0)
            	{
            		doFetchInputFile();
            	}
        	else
               	{
        				//  input already fetched .. overwrite it??
        		int inputChoice = 	JOptionPane.showConfirmDialog(null,"You just fetched an Input File. Do you still want to fetch another input file and overwrite the existing one?","Overwrite Warning",JOptionPane.YES_NO_OPTION);
	        	if(inputChoice == 0)
	        	{
	        		doFetchInputFile();
	        	}
	        	else
	        	{
	        		// Do nothing.. the file first fetched will remain the same.
	        		
	        	}
        		
        		
        		
        		
        		
               	}
        	}	
        		//inputfetched = 0;
        	
        	}
        	
        
        	
        	
        /* Commented for modi      	
        doFetchInputFile();    
        }    Commented for modi */
        
        if(ae.getSource()== open)
        {
            inputFrame.dispose();
            doFetchInputFile();
            new InputFile();
            inputFrame.setVisible(true);
        }
        if(ae.getSource()== G03MenuTree.editFile)
        {
        
        RouteClass.createInput(); 
        new InputFile();
        inputFrame.setVisible(true);
        
        
        }
        
        if(ae.getSource()== G03MenuTree.copyFile)
        {
            
        
        
        }
        
        if(ae.getSource()==cut)
            inputText.cut();
            if(ae.getSource()==copy)
                inputText.copy();
            if(ae.getSource()==paste)
                inputText.paste();
            if(ae.getSource()==selectall)
                inputText.selectAll();
            if(ae.getSource()==save)
            { tempinput=inputText.getText();
            
            	if(InputFile.inputfetched==1)
                new InputfileReader(); // Added on Sep 15 2005 @ UKY
            	
            	else // file not imported
            	{
            		System.out.println("InputFile.java -> About to Replace the Mol");
            		replaceMolFile();
            		           		
            	}
              inputsaved=1;
            }
            if(ae.getSource()==close)
            {
                if(inputText.getText().length()==0 || (inputText.getText().length()==tempinput.length()))
                inputsaved=1;
                
            if(inputsaved==0)   // not saved 
              choiceclose= JOptionPane.showConfirmDialog(null,"The Changes you made will not be saved.Do you want to continue?","Do you want to Continue?",JOptionPane.YES_NO_OPTION);
            if(choiceclose==0||inputsaved==1)
                inputFrame.dispose();
            inputsaved=0;
            }
        
    }
}
