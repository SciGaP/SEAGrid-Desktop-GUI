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

package Gamess.gamessGUI.Dialogs;


import java.awt.event.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import javax.swing.event.MenuListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

import Gamess.gamessGUI.Dictionary;
import Gamess.gamessGUI.EditMenuListener;
import Gamess.gamessGUI.FileMenuListener;
import Gamess.gamessGUI.MolDisplay;
import Gamess.gamessGUI.UndoRedoHandler;
import Gamess.gamessGUI.InputFileHandlers.InputFileReader;
import Gamess.gamessGUI.InputFileHandlers.InputFileWriter;
import Gamess.gamessGUI.Storage.Repository;

import nanocad.nanocadFrame2;
import nanocad.newNanocad;

import org.gridchem.client.Invariants;
import org.gridchem.client.stuffInside;
import org.gridchem.client.common.Settings;
import javax.xml.xpath.*;

public class MolecularSpecification extends JDialog implements ItemListener{

	private static final long serialVersionUID = 1L;
	static JDialog ThisDialog= null;
	
	JPanel northPanel,titlePanel,textPanel, basePanel,buttonPanel;
		
	JMenuBar menuBar=new JMenuBar();
	
	static JMenu fileMenu=new JMenu("File");
	static JMenu editMenu=new JMenu("Edit");
	
	JMenuItem openMenu,saveMenu,saveAsMenu,closeMenu,undoMenu,redoMenu,cutMenu,copyMenu,pasteMenu;
	
	public static JTextArea titleBox;
	public static JTextArea dataFileDisplayArea;
	public static JTextArea chargeBox,multiplicityBox;
	
	public static JComboBox symmetryBox,nBox;
	
	public static JButton doneBtn;
	JButton clearBtn,exitBtn;
	
	String dataFile;
	String symmetryBoxValue=null,nBoxValue=null;
	StringBuffer dataFileDisplay = new StringBuffer();
	
	UndoManager undoManager = new UndoManager();
	
	public NanocadHandler nanoCadHandler;
	
	MenuListener listener;
	MolDisplay geom;
	
	public MolecularSpecification(MolDisplay geom1 , Frame parentFrame){
		super(parentFrame,true);
		
		ThisDialog = this;
		this.setSize(600,500);
		this.setLocationRelativeTo(parentFrame);
		this.setTitle("Molecular Specification");
		
		geom=geom1;

		createNorthPanel();
		createTitlePanel();
		createTextAreaPanel();
		createBasePanel();

		createFileMenu();
		createEditMenu();

		System.out.println("Molecular Specification");
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		
		this.setJMenuBar(menuBar);
		
		//UndoRedoHandler is not used here because the extra datastructure used for
		//grouping the edits is not necessary here.
		dataFileDisplayArea.getDocument().addUndoableEditListener
		(
				new UndoableEditListener()
				{
					public void undoableEditHappened(UndoableEditEvent e) 
					{
						undoManager.addEdit(e.getEdit());
					}
				}
		);
		
		//
		setChargeAndMult();
		
		//////////////////////////////////////////////////////////////////////
		//			Register the entity	with the Organized document		   	//
		//			This is used for the ContentAssist
			Dictionary.Register("DATA",Dictionary.CUSTOM_DIALOG);
			Dictionary.Register("CONTRL", "ICHARG", Dictionary.CUSTOM_DIALOG);
			Dictionary.Register("CONTRL", "MULT", Dictionary.CUSTOM_DIALOG);
			
			Dictionary.registerDialog( "DATA", ThisDialog);
			Dictionary.registerDialog("CONTRL ICHARG", ThisDialog);
			Dictionary.registerDialog("CONTRL MULT", ThisDialog);
		//																	//
		//////////////////////////////////////////////////////////////////////
	}

	public void setChargeAndMult()
	{
		//Check if CONTRL group contains charge and multiplicity values
		//If it is not available then set them to default.
		InputFileReader reader = InputFileReader.getInstance();
		
		//Charge
		String charge = reader.Read("CONTRL" , "ICHARG");
		chargeBox.setText( (charge == null || charge.trim().length() == 0) ? "0" : charge);
		
		//Multiplicity
		String multiplicity = reader.Read("CONTRL" , "MULT");
		multiplicityBox.setText( (multiplicity == null || multiplicity.trim().length() == 0) ? "1" : multiplicity);
		
	}
	
	public String ReadUpdate()
	{
		boolean isModified = false;
		
		String chargeBoxText = chargeBox.getText();
		String multBoxText = multiplicityBox.getText();
		
		setChargeAndMult();
		
		if( chargeBoxText.equalsIgnoreCase(chargeBox.getText()) == false || multBoxText.equalsIgnoreCase(multiplicityBox.getText()) == false )
			isModified = true;
		
		String DataContent = InputFileReader.getInstance().Read("DATA");
		if(DataContent == null)
			return "Invalid group content";
		else
		{
			DataContent = DataContent.trim();
			
			//Get the job title
			int jobTitleEnding = DataContent.indexOf("\n");
			if(jobTitleEnding == -1)
				return "No JobTitle Found";
			String jobTitle = DataContent.substring(0, jobTitleEnding).trim();
			if(jobTitle.split(" ").length >1)
				return "Invalid Title";
			
			//Set the job title
			titleBox.setText(jobTitle);
			
			DataContent = DataContent.substring(jobTitleEnding + 1);
			
			//Get the Symmetry and N
			int Symmetry_N_Ending = DataContent.indexOf("\n");
			if(Symmetry_N_Ending == -1)
				return "No JobTitle Found";
			String symmetry_N = DataContent.substring(0, Symmetry_N_Ending).trim();
			String[] Symmetry_N_Vals = symmetry_N.split(" ");
			//Set the nbox value first
			if(Symmetry_N_Vals.length > 1)
				nBox.setSelectedItem(Symmetry_N_Vals[1]);
			symmetryBox.setSelectedItem(Symmetry_N_Vals[0]);
			
			DataContent = DataContent.substring(Symmetry_N_Ending + 1).trim();
			
			if(dataFileDisplayArea.getText().trim().equalsIgnoreCase(DataContent) == false)
				isModified = true;
			//Set the Data file content
			dataFileDisplayArea.setText(DataContent);
			
			if(isModified)
				doneBtn.doClick();
		}
		return null;
	}
	
	public void createNorthPanel()
	{
		//Charge
		JLabel chargeLbl=new JLabel("Charge:");
		chargeBox=new JTextArea();
		chargeBox.setPreferredSize(new Dimension(50,30));
		chargeBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,1));

		//Multiplicity
		JLabel multiplicityLbl=new JLabel("       Multiplicity:");
		multiplicityBox=new JTextArea();
		multiplicityBox.setPreferredSize(new Dimension(50,30));
		multiplicityBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,1));
		
		//N : set nBox values
		JLabel nLbl=new JLabel("      n:");
		String nBoxValues[]={"1","2","3","4","5","6","7","8","9"};
		nBox=new JComboBox(nBoxValues);
		nBox.setPreferredSize(new Dimension(50,30));
		nBox.addItemListener(this);

		//Symmetry : set symmetryBox values
		JLabel symmetryLbl=new JLabel("        Symmetry:");
		String symmetryValues[]={"C1","Cs","Ci","Cn","S2n","Cnh","Cnv","Dn","Dnh","Dnd","T","Th","Td","O","Oh"};
		symmetryBox=new JComboBox(symmetryValues);
		symmetryBox.setPreferredSize(new Dimension(70,30));
		symmetryBox.addItemListener(this);
		symmetryBoxValue=symmetryBox.getItemAt(0).toString();
		
		//Add all the above components to the north panel
		northPanel=new JPanel();
		northPanel.add(chargeLbl);
		northPanel.add(chargeBox);
		northPanel.add(multiplicityLbl);
		northPanel.add(multiplicityBox);
		northPanel.add(symmetryLbl);
		northPanel.add(symmetryBox);
		northPanel.add(nLbl);
		northPanel.add(nBox);
	}
	
	public void createTitlePanel(){
		titlePanel=new JPanel();
		
		titleBox=new JTextArea();
		titleBox.setText("default_Job");
		titleBox.setPreferredSize(new Dimension(300,30));
		titleBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,1));
		titleBox.setRows(2);
		titleBox.setLineWrap(true);
		
		JLabel titleLbl =new JLabel("Title: ");
		titlePanel.add(titleLbl);
		titlePanel.add(titleBox);
	}

	public void createTextAreaPanel(){
		//Add the text area
		textPanel=new JPanel();
		textPanel.setLayout(new BorderLayout());
		
		dataFileDisplayArea=new JTextArea();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(250,320));
		scrollPane.setViewportView(dataFileDisplayArea);
        textPanel.add(scrollPane);
		
		// Adding Buttons
		buttonPanel=new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		doneBtn=new JButton("Done");
		clearBtn=new JButton("Clear");
		exitBtn=new JButton("Exit");
		
		doneBtn.addActionListener(new buttonListener());
		clearBtn.addActionListener(new buttonListener());
		exitBtn.addActionListener(new buttonListener());
		
		buttonPanel.add(doneBtn);
		buttonPanel.add(clearBtn);
		buttonPanel.add(exitBtn);
		
		textPanel.add(buttonPanel,BorderLayout.SOUTH);
	}
	
	public void createBasePanel(){
		basePanel=new JPanel(new BorderLayout());
		basePanel.add(northPanel,BorderLayout.NORTH);
		basePanel.add(titlePanel,BorderLayout.CENTER);
		basePanel.add(textPanel,BorderLayout.SOUTH);
		this.getContentPane().add(basePanel,BorderLayout.CENTER);
	}
	
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()==symmetryBox){
			Object item=e.getItem();
			if(e.getStateChange()==ItemEvent.SELECTED){
				symmetryBoxValue=item.toString();
				if(item.toString().equals("Cn")||item.toString().equals("S2n")||item.toString().equals("Cnh")||item.toString().equals("Cnv")||item.toString().equals("Dn")||item.toString().equals("Dnh")||item.toString().equals("Dnd")){
					nBox.setEnabled(true);
					nBoxValue=nBox.getItemAt(0).toString();
				}
				else{
					nBox.setEnabled(false);
					nBoxValue=null;
				}
			}
		}
		if(e.getSource()==nBox){
			Object item=e.getItem();
			nBoxValue=item.toString();
		}
	}

	public class buttonListener implements ActionListener{
		 // pass it to the buildInputFile1
		
		public void actionPerformed(ActionEvent e) {
			//Done button
			if((JButton)e.getSource()==doneBtn){
				dataFileDisplay.delete(0, dataFileDisplay.length());
				dataFile = "";
				// get text from the textArea and put it into a string 
				//pass it to the build file and readGeomerty()
				//String dataFile is used to pass the value to ReadGeometry
				
				dataFile=dataFileDisplayArea.getText();
				
				boolean consistent = false;
				try
				{
					boolean invokeSymmetry = false;
					if(JOptionPane.showConfirmDialog(ThisDialog, "Use point group symmetry to display molecule?","Invoke Symmetry", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					{
						invokeSymmetry = true;
					}
					consistent=geom.setStrGeom(dataFile,chargeBox.getText().trim(),multiplicityBox.getText().trim(), invokeSymmetry, symmetryBoxValue, Integer.parseInt(nBox.getSelectedItem().toString()));
				}
				catch(Exception ex )
				{
					JOptionPane.showMessageDialog(ThisDialog, "Improper molecular specification format\n" +
							"Please enter specification either in Z-matrix or Cartesian coordinates format\n" +
							"Please refer Help for further clarification","ERROR", JOptionPane.ERROR_MESSAGE);
					geom.setStrGeom("", chargeBox.getText(), multiplicityBox.getText(), false, symmetryBoxValue, Integer.parseInt(nBox.getSelectedItem().toString()));
					return;
				}

				// ICHARG and MULT belong to $CONTRL group.
				String charge=chargeBox.getText().trim();
				String multiplicity=multiplicityBox.getText().trim();
				consistent = true;
				if(consistent==true){
					//CONTRL Group values
					UndoRedoHandler.toggleGroupClassifier();
					UndoRedoHandler.setLock();
					
					Repository db = Repository.getInstance();
					InputFileWriter writer = InputFileWriter.getInstance();
					
					db.Store("CONTRL", "ICHARG=" + charge);
					writer.Write("CONTRL", "ICHARG=" + charge);
					db.Store("CONTRL", "MULT=" + multiplicity);
					writer.Write("CONTRL", "MULT=" + multiplicity);
									
					/* TO DISPLAY IN THE INPUT FILE TEXT AREA OF THE MAIN GUI*/
					if(!dataFile.equals("")){
						if(titleBox.getText().trim()=="")
							dataFileDisplay.append("default_Job"+"\n");
						else
							dataFileDisplay.append(titleBox.getText().trim()+"\n"); // add title
				
						//get the symmetry and n values
						// symmetryBoxValues and nBoxValues have the values
						dataFileDisplay.append(symmetryBoxValue);
						// If nBoxValue is not 'null' append it to the 'dataFileDisplay'
						if(nBoxValue!=null)
							dataFileDisplay.append(" "+nBoxValue+"\n");
						
						//INPUT.DOC: For C1 group, there is no card -3- or -4-.
						if(symmetryBoxValue=="C1")
							dataFileDisplay.append("\n"); 
						dataFileDisplay.append("\n"+dataFile); 
				
						//dataFileDisplay.append("\n"+dataFile); 
				
						//dataFileDisplay contains the data under the $DATA group
						db.Store("DATA", dataFileDisplay.toString() );
						writer.Write("DATA", dataFileDisplay.toString() );
					}
					UndoRedoHandler.releaseLock();
					
					MolDisplay.glcanvas.repaint();
					ThisDialog.dispose();
				}
				else
				{
					//Display error message.
					String message = "ERROR ! A spin multiplicity of " + multiplicity + " is inconsistent with the net charge of " + charge + " on this molecule";
					JOptionPane.showMessageDialog(ThisDialog, message,"ERROR", JOptionPane.ERROR_MESSAGE);
				    
				}
			}
			//Clear button
			else if((JButton)e.getSource()==clearBtn){
				dataFileDisplayArea.setText("");
				dataFileDisplay.delete(0, dataFileDisplay.length());
				
			}
			//Exit button
			else if((JButton)e.getSource()==exitBtn){
				ThisDialog.dispose();
			}
		}
		
	}
	
	public void createFileMenu(){
    	fileMenu=new JMenu("File");
    	menuBar.add(fileMenu);
    	
    	ActionListener molSpecListener = new FileMenuListener(ThisDialog, dataFileDisplayArea , null , undoManager , false); 
    	
    	//Open
    	openMenu=new JMenuItem("Open");
    	openMenu.addActionListener(molSpecListener);
    	fileMenu.add(openMenu);
    	
    	//Save
    	saveMenu=new JMenuItem("Save");
    	saveMenu.addActionListener(molSpecListener);
    	fileMenu.add(saveMenu);
    	
    	//SaveAs
    	saveAsMenu=new JMenuItem("Save As");
    	saveAsMenu.addActionListener(molSpecListener);
    	fileMenu.add(saveAsMenu);
    	
    	fileMenu.addSeparator();
    	
    	//Import : JMolEditor/Nanocad
    	JMenu importMenu = new JMenu("Import");
    	fileMenu.add(importMenu);
//    	Import SubMenu
    	{
    		JMenuItem jMolEditor = new JMenuItem("JMolEditor");
    		importMenu.add(jMolEditor);
    		jMolEditor.setEnabled(false);
    		JMenuItem nanoCad = new JMenuItem("Nanocad ");
    		importMenu.add(nanoCad);
    		nanoCad.addActionListener(nanoCadHandler = new NanocadHandler());
    	}
    	
    	fileMenu.addSeparator();
    	
    	//Close
    	closeMenu=new JMenuItem("Close");
    	closeMenu.addActionListener(molSpecListener);
    	fileMenu.add(closeMenu);
    }
	
    public void createEditMenu(){
    	editMenu=new JMenu("Edit");
    	menuBar.add(editMenu);
    	
    	ActionListener editMenuListener = new EditMenuListener(dataFileDisplayArea , undoManager);
    	
    	//Undo
    	undoMenu= new JMenuItem("Undo");
    	undoMenu.addActionListener(editMenuListener);
    	editMenu.add(undoMenu);
    	
    	//Redo
    	redoMenu= new JMenuItem("Redo");
    	redoMenu.addActionListener(editMenuListener);
    	editMenu.add(redoMenu);

    	//
    	editMenu.addSeparator();
    	
    	//Cut
    	cutMenu= new JMenuItem("Cut");
    	cutMenu.addActionListener(editMenuListener);
    	editMenu.add(cutMenu);
    	
    	//Copy
    	copyMenu= new JMenuItem("Copy");
    	copyMenu.addActionListener(editMenuListener);
    	editMenu.add(copyMenu);
    	
    	//Paste
    	pasteMenu= new JMenuItem("Paste");
    	pasteMenu.addActionListener(editMenuListener);
    	editMenu.add(pasteMenu);
    }
    
    public class NanocadHandler implements ActionListener , ComponentListener , WindowListener
    {
    	public nanocadFrame2 nanWin = null;
   	
		public void actionPerformed(ActionEvent arg0) 
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

	    	// if the output file is present then delete it
	    	String tmpfile = "tmp.txt";
	    	File fa = new File(newNanocad.applicationDataDir+Settings.fileSeparator+tmpfile);
	    	if ( fa.exists())
	    	{
	    		fa.delete();
	    	}

	    	// launch nanocad
	    	System.out.println("Calling nanocadMain");
	    	nanWin = new nanocadFrame2();
	    	nanWin.addWindowListener(this);
	    	nanWin.nano.addComponentListener(this);
	    	ThisDialog.setVisible(false);
	    	nanWin.setVisible(true);
	    	nanWin.toFront();
	    	
	    	System.out.println(" Done with Nanocad");
		}

		public void componentHidden(ComponentEvent arg0) 
		{
			if(nanWin.nano.exportedApplication.equals(Invariants.APP_NAME_GAMESS))
			{
				System.err.println("load temp file here!");
				File f = new File(newNanocad.applicationDataDir +
						Settings.fileSeparator + "tmp.txt");
				if ((f.exists()) )
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
							}
						}
						inStream.close();
						dataFileDisplayArea.setText(text);
					}
					catch (IOException ioe)
					{
						System.err.println("IOException in showMolEditor");
					}
				}
			
				try 
				{
					nanWin.setVisible(false);
					nanWin.dispose();
					if (nanWin.nano.t !=null)	
						nanWin.nano.t.setVisible(false);
				} 
				catch (HeadlessException e1) 
				{
					e1.printStackTrace();
				}
			}
			else
				nanWin.setVisible(false);
			
			if(!ThisDialog.isVisible())
			{
				ThisDialog.setVisible(true);
			}
		}

		public void componentMoved(ComponentEvent arg0) {
		}

		public void componentResized(ComponentEvent arg0) {
		}

		public void componentShown(ComponentEvent arg0) {
		}

		public void windowActivated(WindowEvent arg0) {
		}

		public void windowClosed(WindowEvent arg0) {
		}

		public void windowClosing(WindowEvent arg0) {
		}

		public void windowDeactivated(WindowEvent arg0) {
		}

		public void windowDeiconified(WindowEvent arg0) {
		}

		public void windowIconified(WindowEvent arg0) {
		}

		public void windowOpened(WindowEvent arg0) {
		}
    	
    }
}// end of class

	       