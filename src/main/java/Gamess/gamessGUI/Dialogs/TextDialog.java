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
 * @author Shreeram
 * @author Michael Sheetz
 */

package Gamess.gamessGUI.Dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

import org.w3c.dom.*;

import Gamess.gamessGUI.Dictionary;
import Gamess.gamessGUI.UndoRedoHandler;
import Gamess.gamessGUI.InputFileHandlers.InputFileWriter;
import Gamess.gamessGUI.Storage.Repository;

public class TextDialog extends JDialog {

	/**
	 * private variables declaration
	 */
	private static final long serialVersionUID = 1L;
	private JDialog ThisDialog = null;
	private JFrame frame = new JFrame();
	private JMenuBar menuBar = new JMenuBar();
	private JTextArea textArea = new JTextArea();
	private JScrollPane txtscrl = new JScrollPane();
	private JPanel buttonPanel = new JPanel();
	private JButton doneBtn = new JButton("Done");
	private JButton clearBtn = new JButton("Clear");
	private JButton exitBtn = new JButton("Exit");
	private ActionListener buttonListener = null;
	private ActionListener menuListener = null;
	private UndoManager editManager = new UndoManager();
	private JFileChooser fc = null;
	private String CurrentGroup = "";
	
	public TextDialog(Frame parentFrame, String DefaultGroup , Node refNode) {
		super(parentFrame);
		
		ThisDialog = this;
		
		CurrentGroup = DefaultGroup;
		if(refNode != null && refNode.getAttributes().getNamedItem("Group") != null)
		{
			CurrentGroup = refNode.getAttributes().getNamedItem("Group").getNodeValue();
		}
			
		setTitle(CurrentGroup);
		this.setSize(500, 400);
		this.setLayout(new BorderLayout());
		frame.setLayout(new BorderLayout());
		this.getContentPane().add(frame.getContentPane());
		this.setLocationRelativeTo(parentFrame);
		
		//Add the Menubar in the top for basic operation
		setJMenuBar(menuBar);
		//frame.setJMenuBar(menuBar);
		//Add menuitems
		BuildMenu();
		
		//Add the TextArea to type or paste the content
		txtscrl.setViewportView(textArea);
		frame.getContentPane().add(txtscrl,BorderLayout.CENTER);
		textArea.getDocument().addUndoableEditListener(new UndoRedoListener());
		
		//Add the Button panal
		buttonPanel.setLayout(new FlowLayout());
		frame.getContentPane().add(buttonPanel , BorderLayout.SOUTH);
		
		//Add the buttons here
		buttonPanel.add(doneBtn);
		buttonPanel.add(clearBtn);
		buttonPanel.add(exitBtn);
		
		buttonListener = new ButtonListener();
		doneBtn.addActionListener(buttonListener);
		clearBtn.addActionListener(buttonListener);
		exitBtn.addActionListener(buttonListener);
		
		//////////////////////////////////////////////////////////////////////
		//			Register the entity	with the Organized document		   	//
		//			This is used for the ContentAssist
			Dictionary.Register(CurrentGroup,Dictionary.TEXT_DIALOG);
			Dictionary.registerDialog(CurrentGroup, ThisDialog);
		//																	//
		//////////////////////////////////////////////////////////////////////
	}
	
	private void BuildMenu()
	{
		menuListener = new MenuListener();
		JMenuItem menuItem = null;
		
		//
		JMenu fileMenu = new JMenu("File");
		
		menuItem = new JMenuItem("Open");
		menuItem.addActionListener(menuListener);
		fileMenu.add(menuItem);
		
		menuItem = new JMenuItem("Save");
		menuItem.addActionListener(menuListener);
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Close");
		menuItem.addActionListener(menuListener);
		fileMenu.add(menuItem);

		//
		JMenu editMenu = new JMenu("Edit");

		menuItem = new JMenuItem("Undo");
		menuItem.addActionListener(menuListener);
		editMenu.add(menuItem);

		menuItem = new JMenuItem("Redo");
		menuItem.addActionListener(menuListener);
		editMenu.add(menuItem);

		editMenu.addSeparator();
		
		menuItem = new JMenuItem("Cut");
		menuItem.addActionListener(menuListener);
		editMenu.add(menuItem);

		menuItem = new JMenuItem("Copy");
		menuItem.addActionListener(menuListener);
		editMenu.add(menuItem);

		menuItem = new JMenuItem("Paste");
		menuItem.addActionListener(menuListener);
		editMenu.add(menuItem);
		
		//
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == doneBtn)
			{
				UndoRedoHandler.toggleGroupClassifier();
				UndoRedoHandler.setLock();
				
				InputFileWriter.getInstance().Write(CurrentGroup,  textArea.getText());
				Repository.getInstance().Store(CurrentGroup,  textArea.getText());
				
				UndoRedoHandler.releaseLock();
				ThisDialog.dispose();
			}
			else if(e.getSource() == clearBtn)
			{
				textArea.setText("");
			}
			else if(e.getSource() == exitBtn)
			{
				ThisDialog.dispose();
			}
		}
	}

	class UndoRedoListener implements UndoableEditListener
	{
		public void undoableEditHappened(UndoableEditEvent e) {
			editManager.addEdit(e.getEdit());
		}
	}
	
	class MenuListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Open"))
			{
				if(fc == null)
				{
					fc = new JFileChooser();
				}
				fc.setDialogTitle("Open file");
				int fcResult = fc.showOpenDialog(ThisDialog);
				if(fcResult == JFileChooser.APPROVE_OPTION)
				{
					//read the content of the file into the text area
					try 
					{
						textArea.setText("");
						FileReader in = new FileReader(fc.getSelectedFile());
						BufferedReader bin = new BufferedReader(in);
						String line = "";
						while( ( line = bin.readLine()) != null)
							textArea.append(line);
						bin.close();
						in.close();
					} 
					catch (IOException ex) 
					{
						ex.printStackTrace();
					}
				}
			}
			else if(e.getActionCommand().equals("Save"))
			{
				if(fc == null)
				{
					fc = new JFileChooser();
				}
				fc.setDialogTitle("Save file");
				int fcResult = fc.showSaveDialog(ThisDialog);
				if(fcResult == JFileChooser.APPROVE_OPTION)
				{
					try
					{
						if(fc.getSelectedFile().exists())
						{
							int response = JOptionPane.showConfirmDialog(ThisDialog , "Overwrite existing file?" , "Confirm Overwrite",
									JOptionPane.OK_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE);
							if(response == JOptionPane.CANCEL_OPTION || response == JOptionPane.CLOSED_OPTION)
								return;
						}
						PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter(fc.getSelectedFile())));
						out.print(textArea.getText());
						out.flush();
						out.close();
					}
					catch(IOException ex)
					{
						ex.printStackTrace();
					}
				}
			}
			else if(e.getActionCommand().equals("Close"))
			{
				ThisDialog.dispose();
			}
			else if(e.getActionCommand().equals("Undo"))
			{
				if(editManager.canUndo())
					editManager.undo();
			}
			else if(e.getActionCommand().equals("Redo"))
			{
				if(editManager.canRedo())
					editManager.redo();
			}
			else if(e.getActionCommand().equals("Cut"))
			{
				textArea.cut();
			}
			else if(e.getActionCommand().equals("Copy"))
			{
				textArea.copy();
			}
			else if(e.getActionCommand().equals("Paste"))
			{
				textArea.paste();
			}
		}
	}
}
