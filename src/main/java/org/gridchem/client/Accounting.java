/**
 Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

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
package org.gridchem.client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import org.gridchem.client.common.Settings;


import java.awt.FlowLayout;

/**
 * @author Xiaohai Li, NCSA, 7/8/2005
 * 
 * Display accounting information
 *
 */
public class Accounting extends JFrame{

	private static final long serialVersionUID = 1L;

	private JButton enterButton;
    private JTable accountingBoard;
    private static DefaultTableModel accountingModel;
    
	private int prefWidth = 550;
	private int prefHeight = 180;
	
	public static String[] columnNames = {"Allocated projects","P_alloc","P_usage"}; 
	public static int num_column = columnNames.length;//
    
	public Accounting(String[] usertype) {
		super("GridChem: Allocation Information");

		
		//Retrieve accounting information
		accountingModel = new DefaultTableModel();
		Object columndata;
		for (int j = 0; j < num_column; j++) {
			columndata = columnNames[j];
			accountingModel.addColumn(columndata);
		}

		// Extract Account Name, Account Allocation, and Account Usage for each account
		for(int k=3; k<usertype.length; k += 3){
			Object[] rowdata = {usertype[k],usertype[k+1],usertype[k+2]};
			accountingModel.addRow(rowdata);
		}

		//Create accountingBox
		JScrollPane jscrollPane;
		TableSorter sorter = new TableSorter(accountingModel);
		accountingBoard = new JTable(sorter);
		sorter.setTableHeader(accountingBoard.getTableHeader());
	    accountingBoard.setPreferredScrollableViewportSize(new Dimension(prefWidth,prefHeight));
		jscrollPane = new JScrollPane(accountingBoard); 
		jscrollPane.isWheelScrollingEnabled();
		//jscrollPane.setPreferredSize(new Dimension(500,100));
		Container accountingBox = Box.createVerticalBox();
		accountingBox.add(jscrollPane);
		
		//Create buttonPane
		enterButton = new JButton("Close");
		enterButton.setVerticalTextPosition(AbstractButton.CENTER);
		enterButton.setHorizontalTextPosition(AbstractButton.RIGHT);
		enterButton.setMnemonic(KeyEvent.VK_E);
		enterButton.setToolTipText("Click to close the window");
	    enterButton.addKeyListener(new KeyListener() {
	        public void keyTyped(KeyEvent e) {}
	        public void keyReleased(KeyEvent e) {}
	        public void keyPressed(KeyEvent e) {
	      		int key = e.getKeyCode();
	      		if (key == KeyEvent.VK_ENTER) {	
	      			Accounting.this.setVisible(false);
	      		}
	        }
	        });
	    enterButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	  Accounting.this.setVisible(false);}
	    });
	    	
		//enterButton.addKeyListener(new ButtonKeyListener());
		//enterButton.addActionListener(new ButtonListener());
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		buttonPane.add(enterButton);
		
		//Create insideStuff
		JPanel insideStuff = new JPanel();
		insideStuff.setLayout(new BoxLayout(insideStuff,BoxLayout.Y_AXIS));
		insideStuff.add(accountingBox);
		insideStuff.add(buttonPane);
		insideStuff.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		//Arrange final layout	 
		this.setContentPane(insideStuff);
		//this.setPreferredSize(new Dimension(prefWidth,prefHeight));
	    this.addWindowListener(new WindowCloser(this, false));
	    this.pack();
	    this.setResizable(true);
    		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    
		//Centering the frame on the screen
		Toolkit kit = this.getToolkit();
		Dimension screenSize = kit.getScreenSize();
	    int screenWidth = screenSize.width;
	    int screenHeight = screenSize.height;
	    Dimension windowSize = this.getSize();
	    int windowWidth = windowSize.width;
	    int windowHeight = windowSize.height;
	    int upperLeftX = (screenWidth - windowWidth)/2;
	    int upperLeftY = (screenHeight - windowHeight)/2;   
	    this.setLocation(upperLeftX, upperLeftY);
	}

	/**********************************************************
	 The actionPerformed method in this private inner class
	 is called when the user presses the start button.
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			if(evt.getSource() == enterButton) {
				Accounting.this.getDefaultCloseOperation();
				Accounting.this.setVisible(false);
			}
		}
	}// end private class ButtonListener


	private class ButtonKeyListener implements KeyListener {
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_E) {
				Accounting.this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				Accounting.this.setVisible(false);
			}
		}
	}


	public static void main(String[] args) {
 		try {
	        UIManager.setLookAndFeel(
	                UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	        // Ignore exceptions, which only result in the wrong look and feel.
	    }
	    String[] userType = new String[1];
	    Accounting ac = new Accounting(userType);
	    ac.addWindowListener(new WindowCloser(ac, false));
	    ac.pack();
	    ac.setVisible(true);
    		//ac.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    		ac.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

}
