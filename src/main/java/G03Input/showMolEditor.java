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
 * Created on Apr 2, 2005
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta  @author Sandeep Kumar Seethaapathy 
 * 
 */

package G03Input;

import java.awt.Dimension;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import javax.swing.filechooser.FileFilter;
import javax.swing.text.*;

import org.gridchem.client.Invariants;
import org.gridchem.client.stuffInside;
import org.gridchem.client.common.Settings;

import java.util.regex.*;



import nanocad.nanocadFrame2;
import nanocad.newNanocad;
import nanocad.newNanocad.*;





public class showMolEditor extends JFrame implements ActionListener,WindowListener,ComponentListener{
    
    
    public static JFrame molFrame; 
    public static JMenuBar molBar;
    public static JMenu fileMenu,editMenu,molEditor;
    public static JMenuItem open,close,validate,save,saveas,cut,copy,paste
    ,selectall,openmol,viewcur;
    public static JPanel molPanel,textPanel;
    public static JTextArea molText;
    public static String tempmol="";
    public static String exportedMol="";
 //   public static String tempmolFromInput="";
    public static JFileChooser inputChooser;
    static int saved=0,choiceclose,inputfileOverWrite;
    public static int nanoFlag;
    //  From ---lixh_4/27/05

    public static String defaultDirStr = Settings.defaultDirStr;
    public static String fileSeparator = Settings.fileSeparator;
    public static String txtDir = defaultDirStr + fileSeparator + "txt";

    
      //
    
    
    

    nanocadFrame2 nanWin;   
    public showMolEditor()
    {
       textPanel=new JPanel(new GridBagLayout());
       GridBagConstraints c = new GridBagConstraints();
       c.fill=GridBagConstraints.BOTH;
       
       
       JFrame.setDefaultLookAndFeelDecorated(true);
    	JDialog.setDefaultLookAndFeelDecorated(true);
    	molFrame = new JFrame("Molecular Specification");
    	molFrame.setSize(500,250);
    	molFrame.setLocation(25,50);
    
    	molFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  
    	
    	molPanel = new JPanel(new BorderLayout());
    	
    	molBar = new JMenuBar();
    	fileMenu = new JMenu("File");
    	editMenu = new JMenu("Edit");
    	molEditor = new JMenu("Molecular Editor");
    	

    	open = new JMenuItem("Open");
    	close = new JMenuItem("Close");
    	save = new JMenuItem("Save/DisplayMol");
    	//validate=new JMenuItem("Validate");
    	saveas = new JMenuItem("SaveAs");
    	
    	cut = new JMenuItem("Cut");
    	copy = new JMenuItem("Copy");
    	paste = new JMenuItem("Paste");
    	selectall  = new JMenuItem("Select All");
    	
    	openmol = new JMenuItem("Molecular Editor");
    	viewcur=new JMenuItem("View Current Structure");
    	
    	openmol.setEnabled(true);
    	openmol.addActionListener(this);
    	viewcur.setEnabled(true);
    	fileMenu.add(open);
    	fileMenu.add(save);
    //	fileMenu.add(validate);
    	fileMenu.add(new JSeparator());
    	fileMenu.add(close);
    	editMenu.add(cut);
    	editMenu.add(copy);
    	editMenu.add(paste);
    	editMenu.add(new JSeparator());
    	editMenu.add(selectall);
    	
    	//validate.addActionListener(this);
    	cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        selectall.addActionListener(this);
        save.addActionListener(this);
        close.addActionListener(this);
        open.addActionListener(this);
        
    	molEditor.add(openmol);
    	molEditor.add(viewcur);
    	
    	molBar.add(fileMenu);
    	molBar.add(editMenu);
    	molBar.add(molEditor);
    	
    	molPanel.add(molBar,BorderLayout.NORTH);
    	
    	molText = new JTextArea("",35,70);
    	
    	System.out.println(tempmol);
    
    	if(tempmol!=null)
    	{
    		if(InputFile.inputfetched==0) 
    		{
    		molText.setText(tempmol);
    	    molText.setCaretPosition(tempmol.length());
    		}
    		
    		/* If input file is fetched, then get the tempmol from InputFile */
    			if(InputFile.inputfetched==1) 
    	     	{
        		molText.setText(tempmol);
        	    molText.setCaretPosition(tempmol.length());
        		}
    			/*else
    			{
    					molText.setText(tempmol);
    		    	    molText.setCaretPosition(tempmol.length());
    			}*/
    		
    	}
    	//
    	 else
    	molText.setCaretPosition(0);
    	molText.setMargin(new Insets(5,5,5,5));
        
         
    	//
    	JScrollPane mScroll = new JScrollPane(molText);
    	mScroll.setWheelScrollingEnabled(true);

    	
    	molPanel.add(mScroll,BorderLayout.CENTER);
    	molFrame.getContentPane().add(molPanel);
    	//molFrame.getContentPane().add(textPanel);
    	molFrame.pack();
    	molFrame.setResizable(false);
    	//molFrame.setVisible(true);  
    	molFrame.setVisible(false);
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
			tempmol="";
            while (in.available() !=0)
			{
                                    // Print file line to screen
				tempmol+=in.readLine();
				tempmol+="\n";
			}
            
            
			in.close();
			
			if(InputFile.inputfetched==1)
			{
			    inputfileOverWrite=JOptionPane.showConfirmDialog(null,"You already have an imported input file." +
			    		" Do you want to over write the existing input file?","Do you want to Continue?",JOptionPane.YES_NO_OPTION);
			    if(inputfileOverWrite==0)
			    {
			        //YES 
			        InputFile.inputfetched=0;
			        G03MenuTree.nanocadNotice.setText("Molecular Specification Imported from External File");
			    }
			   
			    	
			}
			else
			{
				G03MenuTree.nanocadNotice.setText("Molecular Specification Imported from External File");
			}
			
			
			
			
		} 
        
        catch (Exception e)
		{
			JOptionPane.showMessageDialog(null,"File input error","Error",JOptionPane.ERROR_MESSAGE);
		}

        
    }
    
    public void doFetchInputFile()
    {
        //      Open a FileChooser with *.com Filter    
        
        inputChooser = new JFileChooser();
    	    	            	
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
    
    void replaceInputFile()
    {
    	BufferedReader br= new BufferedReader(new StringReader(InputFile.tempinput));
    	boolean matchedcharge=false,matchedmol=false;
    	StringBuffer inputString=new StringBuffer();
    	String temp="$";
    	
    	//Reads the input file until the charge and multiplicity are obtained
    	while(matchedcharge==false)
    	{
    		try {
				temp=br.readLine();
				
				if(temp==null) //EOF no luck
					break;
				
				matchedmol=Pattern.matches("[A-Z][a-z]?[\\s]+[\\+|\\-]?[0-9]+\\.?[0-9]+[\\s]+[\\+|\\-]?[0-9]+\\.?[0-9]+[\\s]+[\\+|\\-]?[0-9]+\\.?[0-9]+[\\s]*",temp);
				
				if(matchedmol==true) // Charge & multiplicity not yet entered Mol. Specification exists 
				break;	
				
				inputString.append(temp+"\n");
				matchedcharge= Pattern.matches("[\\s]*[\\+|\\-]?[0-9]+\\.?[0-9]*[\\s]*[\\+|\\-]?[0-9]+\\.?[0-9]*[\\s]*",temp);
		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    	inputString.append(tempmol);
    	InputFile.tempinput=inputString.toString();
    	//InputFile.tempinput.replaceAll();
    	
    	
    }
    public void actionPerformed(ActionEvent e)
    {
        
        // Molecular editor stuff... calling nanocad...
        if(e.getSource()==openmol || e.getSource()== G03MenuTree.nanoItem)
        {
        // need to show the nano cad copied from editingStuff.java
        	nanoFlag=1;
            doCallNanocad();
            molFrame.dispose();
        }
        
        /*
        if(e.getSource()==validate)
            atomCoordinateParser.getCoordinates();
          * Commented to integrate both the buttons       */  
        if(e.getSource()==cut)
        molText.cut();
        if(e.getSource()==copy)
            molText.copy();
        if(e.getSource()==paste)
            molText.paste();
        if(e.getSource()==selectall)
            molText.selectAll();
        if(e.getSource()==save)
        {	
        	if(InputFile.inputfetched==1)
        	{
        		int save = JOptionPane.showConfirmDialog(this,"Do you want to save the changes on the imported molecular coordinates","Warning",JOptionPane.YES_NO_OPTION);
        		if(save==0)
        		{
        			InputfileReader.geom = molText.getText();
        			tempmol = InputfileReader.geom;
        			replaceInputFile();
        			G03MenuTree.nanocadNotice.setText("Molecular Structure Updated");
        			
        			
        			
        		}
        		else{
        			//tempmol = molText.getText();
        			}
        		
        	}
        	
        	
        		tempmol=molText.getText();
        		System.out.println(" tempmol: "+tempmol);
        		new GeometryEditor(tempmol);
        		G03MenuTree.nanocadNotice.setText("Molecular Structure Updated");
        		saved=1;
        	   	atomCoordinateParser.getCoordinates();
        }
        if(e.getSource()==open)
        {
            molFrame.dispose();
            doFetchInputFile();
            new showMolEditor();
            molFrame.setVisible(true);
        }
   
        
        
        
        
        if(e.getSource()==close)
        {
        	if(molText.getText().length()==0 || (molText.getText().length()==tempmol.length()) )
            saved=1;
            if(InputFile.inputfetched==1 && (molText.getText().length()==tempmol.length()))
            	saved=1;
        	
        if(saved==0)   // not saved 
          choiceclose= JOptionPane.showConfirmDialog(null,"The Changes you made will not be saved.Do you want to continue?","Do you want to Continue?",JOptionPane.YES_NO_OPTION);
        if(choiceclose==0||saved==1)
            molFrame.dispose();
        saved=0;
        }
            
        
    }
    
    public void windowOpened(WindowEvent e) {}

	public void windowClosing(WindowEvent e)
	{
	//	check for temp file and if it exists, load into text box
	System.err.println("load temp file here!");

	
	
	
	//File f = new File(defaultDirStr + 
	File f = new File(nanocad.newNanocad.applicationDataDir +
		fileSeparator + "tmp.txt");                 
	if ((f.exists()) )//&& !(f.isEmpty()))
	{
	try
	{
    	BufferedReader inStream = new BufferedReader(new FileReader(f));
    	String text = "";
    	String line;
    	while ((line = inStream.readLine()) != null)
 	{
		int n = line.length();
		if (n > 0)
		{
	    	text = text + line + "\n";
	    	System.err.println(line);
		}
    	}
    	inStream.close();
	changeInputText(text);
	}
	
	
	
	catch (IOException ioe)
	{
	     System.err.println("IOException in editJobPanel");
	}
    }
    
    
    
    nanWin.dispose();
    if (nanWin.nano.t != null)
    nanWin.nano.t.setVisible(false);
    
    
    //setVisible(false);
	}

	public void windowClosed(WindowEvent e) 
	{
	}

	public void windowIconified(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowActivated(WindowEvent e) {}

	public void windowDeactivated(WindowEvent e) {}
    
	public void componentMoved(ComponentEvent e) {}
	public void componentResized(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e)
	{
		System.err.println("load temp file here!");
		
		
		//File f = new File(Settings.defaultDirStr + 
				File f = new File(newNanocad.applicationDataDir +
			Settings.fileSeparator + "tmp.txt");
		if ((f.exists()) )//&& !(f.isEmpty()))
		{
		try
		{
	    	BufferedReader inStream = new BufferedReader(new FileReader(f));
	    	String text = "";
	    	String line;
	    	while ((line = inStream.readLine()) != null)
	 	{
			int n = line.length();
			if (n > 0)
			{
		    	text = text + line + "\n";
		    	System.err.println(line);
			}
	    	}
	    	inStream.close();
		changeInputText(text);
		}
		catch (IOException ioe)
		{
		     System.err.println("IOException in showMolEditor");
		}
	    }
		try {
			nanWin.dispose();
			//if (nanWin.nano.t.isActive()) 
				//if (nanWin.nano.t.isVisible())
				if (nanWin.nano.t !=null)	
			   nanWin.nano.t.setVisible(false);
				
			if (stuffInside.selectedGUI == 0){
				System.out.println(" G03 GUI is not selected. Printing Input Template Warning"+stuffInside.selectedGUI);

			JOptionPane.showMessageDialog(null, "WARNING: The input" +
					" appearing here is taken from a template.\n" +
					"The molecule information is correct, but" +
					" make sure to edit \n the other parts of the" +
					" text.",
					      "GridChem: Job Editor",
					      JOptionPane.WARNING_MESSAGE);
			}else
			{
				JOptionPane.showMessageDialog(null, "WARNING: Molecule information" +
						" has been exported correctly. Make sure\n" +
						"to edit other sections of GUI.",
						      "GridChem: Gaussian GUI",
						      JOptionPane.WARNING_MESSAGE);
			}
		} catch (HeadlessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
//		displaying molecule on GUI panel after it has been exported to GUI from Nanocad
		/*
		tempmol=molText.getText();
		System.out.println("Printing: tempmol"+tempmol);
		new GeometryEditor(tempmol);
		G03MenuTree.nanocadNotice.setText("Molecular Structure Updated");
		saved=1;
	   	atomCoordinateParser.getCoordinates();
	   	*/
		
	}
	public void changeInputText(String i)
    {
	try {
			//	this.inputText = new JTextArea(i, 20,40);
//inputText.selectAll();
//inputText.replaceSelection(i);
//	inputText.append(i);
//	String coordString=i;
//	String chargMul=new String(G03MenuTree.molCharge.getText())+" "+G03MenuTree.molMultiplicity.getText();
/*	tempmol=new String(G03MenuTree.molCharge.getText());
			tempmol+=" "+G03MenuTree.molMultiplicity.getText();*/
			
		//exportedMol=i;
		tempmol=i;
		//Set the Label in G03MenuTree
		G03MenuTree.nanocadNotice.setText("Molecular Specification Imported from Nanocad");
		
		
			//tempmol+="\n"+i;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	

	public void doCallNanocad()
    {
		
	
    
    	System.out.println(" Calling Nanocad");
    	String setsfile = ".settings";
    	boolean append = false;
   	File sets = new File(Settings.defaultDirStr + Settings.fileSeparator
    			+ setsfile);
    	try
		{
    		FileWriter fw = new FileWriter(sets, append);
    		fw.write("Username= " + Settings.name.getText() + "\n");
    		fw.write("CGI= " + Invariants.httpsGateway + "\n");
    		fw.close();
    		FileWriter fw2 = new FileWriter(Settings.defaultDirStr + Settings.fileSeparator 
    				+ "loadthis", append);
    		fw2.write(Settings.defaultDirStr + Settings.fileSeparator +
    				"common" + Settings.fileSeparator + "Molecule" + Settings.fileSeparator 
					+ "Inorganic" + Settings.fileSeparator
					+ "water.pdb\n");
    		fw2.close();
		}
    	catch (IOException ioe) {}
    	String tmpfile = "tmp.txt";
      
    	//File fa = new File(Settings.defaultDirStr+Settings.fileSeparator+tmpfile);
    	File fa = new File(newNanocad.applicationDataDir+Settings.fileSeparator+tmpfile);
    	if ( fa.exists())
    	{
    		fa.delete();
//	   new File(Settings.defaultDirStr + Settings.fileSeparator + tmpfile).delete();
    	}
      // launch nanocad
    	System.out.println("Calling nanocadMain");
    	nanWin = new nanocadFrame2();
     // WindowListener wl = new WindowListener();
    	nanWin.addWindowListener(this);
    	nanWin.nano.addComponentListener(this);

    	System.out.println(" Done with Nanocad");
//      System.err.println("Now put yer data from the file into the text thing");
    
	

    }
}
