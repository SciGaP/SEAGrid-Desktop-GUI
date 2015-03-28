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

/* PreferencesWindow.java  by Xiaohai Li
   This is the GUI for when you press the "Preferences" button.
*/

package org.gridchem.client;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory; 
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import org.gridchem.client.common.Preferences;
import org.gridchem.client.common.Settings;



public class PreferencesWindow
{
    public static JFrame frame;
    public static InternalStuff3 si;
    
    public PreferencesWindow()
    {
    //JFrame.setDefaultLookAndFeelDecorated(true);
	frame = new JFrame("Preferences");
	si = new InternalStuff3();
	//Border insideBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    //Border outsideBorder = BorderFactory.createEmptyBorder(5,0,5,0);
	si.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    //	frame.getContentPane().add(new stuffInside(ListOfJobs));
	frame.getContentPane().add(si);
	frame.pack();
//	 Centering the frame on the screen
	Toolkit kit = frame.getToolkit();
	Dimension screenSize = kit.getScreenSize();
    int screenWidth = screenSize.width;
    int screenHeight = screenSize.height;
    Dimension windowSize = frame.getSize();
    int windowWidth = windowSize.width;
    int windowHeight = windowSize.height;
    int upperLeftX = (screenWidth - windowWidth)/2;
    int upperLeftY = (screenHeight - windowHeight)/2;   
    frame.setLocation(upperLeftX, upperLeftY);
    //
	frame.setVisible(true);
	frame.setResizable(true);
    }
    
    public static void main(String[] args)
    {
    	Settings settings = Settings.getInstance();
    	PreferencesWindow pw = new PreferencesWindow();
   	    PreferencesWindow.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void setVisible(boolean isVisible) {
        frame.setVisible(isVisible);
    }
}
   

class InternalStuff3 extends JComponent 
{
	JPanel hpcsysPane;
	JPanel mssPane;
	
	BasicArrowButton upButton;
	BasicArrowButton downButton;
	JButton enterButton;
	JButton exitButton;
	JButton browseButton;
	
	ButtonGroup mssRBGroup;
	
	JTextField setPlace;
	
	public JList hpcsysBoard;
	public static DefaultListModel hpcsysModel;
	
	
	public InternalStuff3()
	{	
		hpcsysPane = new JPanel();
		mssPane = new JPanel();
		
		makeButtons();
		
		// Border
		Border leBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border eBorder1 = BorderFactory.createEmptyBorder(0,5,0,5);
	    Border eBorder2 = BorderFactory.createEmptyBorder(5,5,5,5);
	    Border eBorder3 = BorderFactory.createEmptyBorder(5,0,5,0);
	    
		// hpcsysPane
	    	// -->hpcsysLabel
	    JLabel hpcsysLabel = new JLabel("Set up the order of HPC systems");
	    JPanel hpcsysLabelPane = new JPanel();
	    hpcsysLabelPane.setLayout(new GridLayout(1,1));
	    hpcsysLabelPane.add(hpcsysLabel);
	    hpcsysLabelPane.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
	    
	    	// -->hpcsysBoxPane
	    Container hpcsysBox = Box.createVerticalBox();
	    //ArrayList nl = new ArrayList();
	    hpcsysModel = new DefaultListModel();
	    //String fName = Settings.prefFilename; 
	    //String fName = "preferences.hist";
	    /*File f = new File(fName);
	    if (f.exists())
	    {
	    	System.out.println("parsePref");
	    	parsePref(f, nl);
	    }
	    else
	    {
	    	System.out.println("writeDefault2File");
	    	writeDefault2File(f,nl);
	    }*/
	    String[] hpcSystems = Preferences.getString("hpc_machines").split(",");
	    int machineCount = hpcSystems.length;
		for (int i=0;i<machineCount;i++)
		{
		    //hpcsysModel.addElement((String) nl.get(i));
		    hpcsysModel.addElement(hpcSystems[i]);
		}
		hpcsysBoard = new JList(hpcsysModel);
		hpcsysBoard.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		hpcsysBoard.setDragEnabled(true);
		JScrollPane hpcsysScrollPane = new JScrollPane(hpcsysBoard);
		hpcsysScrollPane.setPreferredSize(new Dimension(450,150));
		hpcsysBox.add(hpcsysScrollPane);
		
		JPanel arrowButtonPane = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
	    c.gridwidth = GridBagConstraints.REMAINDER; //next-to-last
	    c.fill = GridBagConstraints.VERTICAL;      //reset to default
	    c.anchor = GridBagConstraints.SOUTH;
	    c.weightx = 1.0;
	    gridbag.setConstraints(upButton, c);
	    arrowButtonPane.add(upButton);
	    c.gridwidth = GridBagConstraints.REMAINDER;     //end row
	    c.fill = GridBagConstraints.VERTICAL;
	    c.anchor = GridBagConstraints.NORTH;
	    c.weightx = 1.0;
	    gridbag.setConstraints(downButton, c);
	    arrowButtonPane.add(downButton);
		arrowButtonPane.setLayout(gridbag);
		arrowButtonPane.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
		
		JPanel hpcsysBoxPane = new JPanel();
		hpcsysBoxPane.setLayout(new BoxLayout(hpcsysBoxPane,BoxLayout.X_AXIS));
		hpcsysBoxPane.add(hpcsysBox);
		hpcsysBoxPane.add(arrowButtonPane);
	    
	    hpcsysPane.setLayout(new BoxLayout(hpcsysPane,BoxLayout.Y_AXIS));
	    hpcsysPane.add(hpcsysLabelPane);
	    hpcsysPane.add(hpcsysBoxPane);	    
	    TitledBorder hpcsystemTitled = BorderFactory.createTitledBorder(leBorder,"HPC System",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION);
	    hpcsysPane.setBorder(BorderFactory.createCompoundBorder(hpcsystemTitled,eBorder2));
	    
		// mssPane
	    JRadioButton ncsamssRB = new JRadioButton("mss@NCSA");
	    ncsamssRB.setActionCommand("mss.ncsa.uiuc.edu");
	    ncsamssRB.setSelected(true);
	    JRadioButton taccmssRB = new JRadioButton("mss@TACC");
	    taccmssRB.setActionCommand("mss@TACC");
	    taccmssRB.setEnabled(false);
	    mssRBGroup = new ButtonGroup();
	    mssRBGroup.add(ncsamssRB);
	    mssRBGroup.add(taccmssRB);
	    Settings.mss = mssRBGroup.getSelection().getActionCommand();
	    System.out.print("mss = " + Settings.mss + "\n");
	    mssPane.setLayout(new GridLayout(3,1));
	    mssPane.add(ncsamssRB);
	    mssPane.add(taccmssRB);
	    TitledBorder mssTitled = BorderFactory.createTitledBorder(leBorder,"Mass Storage System",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION);
	    mssPane.setBorder(BorderFactory.createCompoundBorder(mssTitled,eBorder2));
	    
	    // choosePane
		setPlace = new JTextField(35);
		String defaultdir = Preferences.getString("user_data_directory");
		setPlace.setText(defaultdir);
		setPlace.setEnabled(false);
		setPlace.setDisabledTextColor(Color.BLACK);
		browseButton = new JButton("Browse...");
		
		JPanel choosePane = new JPanel();
//		choosePane.setPreferredSize(new Dimension(400,5));
		choosePane.setLayout(new BorderLayout());
		choosePane.add(setPlace,BorderLayout.WEST);
		choosePane.add(browseButton,BorderLayout.EAST);
		choosePane.setBorder(eBorder2);
	    JPanel choosePaneOuter = new JPanel();
	    choosePaneOuter.add(choosePane);
	    TitledBorder chooseOuterTitled = BorderFactory.createTitledBorder(leBorder,"Set default place for output",TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION);
	    choosePaneOuter.setBorder(BorderFactory.createCompoundBorder(eBorder3,chooseOuterTitled));
	    
	    // buttonPane
	    JPanel buttoninterior = new JPanel();
	    buttoninterior.setLayout(new GridLayout(1,2,5,0));
	    buttoninterior.add(enterButton);
	    buttoninterior.add(exitButton);
	    JPanel buttonPane = new JPanel();
	    buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
	    buttonPane.add(buttoninterior);
	    buttonPane.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
	    
		// Final Layout
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(hpcsysPane);
		add(mssPane);
		add(choosePaneOuter);
		add(buttonPane);
		
		//Add all action listeners
		ActionListener bl = new ButtonListener();
		browseButton.addActionListener(bl);
		
	}
	
	private void makeButtons() {
		
		//upButton
		upButton = new BasicArrowButton(BasicArrowButton.NORTH);
		upButton.addActionListener(new ButtonListener());
		
		//downButton
		downButton = new BasicArrowButton(BasicArrowButton.SOUTH);	
		downButton.addActionListener(new ButtonListener());
		
		//enterButton
	    enterButton = new JButton("Apply");
	    enterButton.setVerticalTextPosition(AbstractButton.CENTER);
	    enterButton.setHorizontalTextPosition(AbstractButton.CENTER);
	    enterButton.setMnemonic(KeyEvent.VK_N);
	    enterButton.addKeyListener(new KeyListener() {
	        public void keyTyped(KeyEvent e) {}
	        public void keyReleased(KeyEvent e) {}
	        public void keyPressed(KeyEvent e) {
	      		int key = e.getKeyCode();
	      		if (key == KeyEvent.VK_ENTER) {
	      		    updatePreferences();
	      		    GridChem.appendMessage("Preferences updated.");
	      		    //PreferencesWindow.frame.setVisible(false);
	      		}
	        }
	        });
	    enterButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	    updatePreferences();
	    	    GridChem.appendMessage("Preferences updated.");
	    		//PreferencesWindow.frame.setVisible(false);
	    	}
	          });
	    
	    //exitButton
	    exitButton = new JButton("Close");
	    exitButton.setVerticalTextPosition(AbstractButton.CENTER);
	    exitButton.setHorizontalTextPosition(AbstractButton.CENTER);
	    exitButton.setMnemonic(KeyEvent.VK_E);
	    exitButton.setToolTipText("Click to exit this window");
	    exitButton.addKeyListener(new KeyListener() {
	        public void keyTyped(KeyEvent e) {}
	        public void keyReleased(KeyEvent e) {}
	        public void keyPressed(KeyEvent e) {
	      		int key = e.getKeyCode();
	      		if (key == KeyEvent.VK_ENTER) {	
	      			PreferencesWindow.frame.setVisible(false);
	      		}
	        }
	        });
	    exitButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	  PreferencesWindow.frame.setVisible(false);
	    	}
	          });
	    
	}
	
	/** Update the local preference file with the info in the GUI.
	 * 
	 */
	public void updatePreferences() {
        int i;
	    // update machine list
	    int machineCount = hpcsysModel.size();
		String machineList = "";
		machineList = hpcsysModel.toString();
		machineList = machineList.replaceAll(" ","");
		machineList = machineList.substring(1);
		machineList = machineList.substring(0,machineList.length()-1);
		Preferences.putString("hpc_machines",machineList);
    }
  /**********************************************************
    The actionPerformed method in this private inner class
    is called when the user presses the up/down button.
  */
 private class ButtonListener implements ActionListener {
   public void actionPerformed(ActionEvent evt) {
   		Object item1;
   		Object item2;
   		
   		if (evt.getSource() == browseButton)
   		{
   			JFileChooser chooser;
   			if (setPlace.getText() != null) {
   			    chooser = new JFileChooser(setPlace.getText());
   			} else {
   				chooser = new JFileChooser();
   			}
	    		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    		int retVal = chooser.showOpenDialog(SaveDataWindow.si);
	    		if (retVal == JFileChooser.APPROVE_OPTION)
	    		{
	    			String selectedDir = chooser.getSelectedFile().getPath(); 
	    			int size = selectedDir.length();
	    			if (selectedDir.substring(size-1).equals(Settings.fileSeparator))
	    				selectedDir = selectedDir.substring(0,size-1);
	    			setPlace.setText(selectedDir);
	    			Settings.defaultDirStr = selectedDir;
	    			if(!chooser.getSelectedFile().exists()) {
		    			try {
	                        chooser.getSelectedFile().createNewFile();
	                    } catch (IOException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }
	    			}
	    			Preferences.putString("user_data_directory",selectedDir);
	    		}
   		}
   		
   		int selectedIndex = hpcsysBoard.getSelectedIndex();
   		if (selectedIndex >= 0)
   		{
   			if (evt.getSource() == upButton)
   			{
   				item1 = hpcsysModel.get(selectedIndex);
   				if (selectedIndex > 0)
   				{
   					item2 = hpcsysModel.get(selectedIndex-1);
   					hpcsysModel.set(selectedIndex,item2);
   					hpcsysModel.set(selectedIndex-1,item1);
   					hpcsysBoard.setSelectedIndex(selectedIndex-1);
   					hpcsysBoard.ensureIndexIsVisible(selectedIndex-1);
   				}
   			}
   			else if (evt. getSource() == downButton)
   			{
   				item1 = hpcsysModel.get(selectedIndex);
   				if (selectedIndex < hpcsysModel.getSize()-1)
   				{
   					item2 = hpcsysModel.get(selectedIndex+1);
   					hpcsysModel.set(selectedIndex,item2);
   					hpcsysModel.set(selectedIndex+1,item1);
   					hpcsysBoard.setSelectedIndex(selectedIndex+1);
   					hpcsysBoard.ensureIndexIsVisible(selectedIndex+1);
   				}
   			}
   		}
   }
 }// end private class ButtonListener
	
}

