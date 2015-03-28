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

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import org.gridchem.client.common.Settings;


import java.awt.FlowLayout;

/**
 * Show a scrollable text windows with content passed to the constructor.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class ScrollableDisplay extends JFrame{

	private static final long serialVersionUID = 1L;

	private JButton okButton;
	private JTextArea messageBoard;
   
	private int prefWidth = 550;
	private int prefHeight = 450;
	
   
	public ScrollableDisplay(String title, String message) {
		super(title);
		//Create message box
		JScrollPane jscrollPane;
		messageBoard = new JTextArea(message);
		//messageBoard.setSize(prefWidth,prefHeight);
		messageBoard.setEnabled(false);
		messageBoard.setDisabledTextColor(Color.BLACK);
		messageBoard.setFont(new Font("Times", Font.PLAIN, 14));
        messageBoard.setLineWrap(true);
		//messageBoard.setMaximumSize(new Dimension(prefWidth,prefHeight));
		jscrollPane = new JScrollPane(messageBoard); 
		jscrollPane.isWheelScrollingEnabled();
		jscrollPane.setPreferredSize(new Dimension(prefWidth,prefHeight));
		Container messageBox = Box.createVerticalBox();
		messageBox.add(jscrollPane);
		
		//Create buttonPane
		okButton = new JButton("Close");
		okButton.setVerticalTextPosition(AbstractButton.CENTER);
		okButton.setHorizontalTextPosition(AbstractButton.RIGHT);
		okButton.setToolTipText("Click to close the window");
		okButton.addKeyListener(new ButtonKeyListener());
		okButton.addActionListener(new ButtonListener());
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0,0,10,5));
		buttonPane.add(okButton);
		
		//Create insideStuff
		JPanel insideStuff = new JPanel();
		insideStuff.setLayout(new BoxLayout(insideStuff,BoxLayout.Y_AXIS));
		insideStuff.add(messageBox);
		insideStuff.add(buttonPane);
		insideStuff.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		//Arrange final layout	 
		this.setContentPane(insideStuff);
		//this.setPreferredSize(new Dimension(prefWidth,prefHeight));
	    //this.addWindowListener(new WindowCloser(this, true));
	    this.pack();
	    //this.setPreferredSize(new Dimension(550,500));
	    this.setResizable(true);
   		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    
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
			if(evt.getSource() == okButton) {
				ScrollableDisplay.this.getDefaultCloseOperation();
				ScrollableDisplay.this.setVisible(false);
			}
		}
	}// end private class ButtonListener


	private class ButtonKeyListener implements KeyListener {
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_ENTER) {
			    ScrollableDisplay.this.getDefaultCloseOperation();
			    ScrollableDisplay.this.setVisible(false);
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
	    //String[] userType = new String[1];
	    String message = "Welcome to GridChem: Portal to the Computational Chemistry Grid!!\n" +
		"Developed by: \n\n" +
		"CCS, University of Kentucky\n" +
		"CCT, Louisiana State University\n" +
		"NCSA, University of Illinois at Urbana-Champaign\n" +
		"OSC, Ohio Supercomputer Center\n" +
		"TACC, University of Texas at Austin\n\n" +
		"https://www.gridchem.org/\n\n" +
		"Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.\n\n" +
		"Developed by:\n" +
		"Chemistry and Computational Biology Group\n\n" +
		"NCSA, University of Illinois at Urbana-Champaign\n\n" +
		"Permission is hereby granted, free of charge, to any person obtaining a copy of \n" +
		"this software and associated documentation files (the \"Software\"),to deal with \n" +
		"the Software without restriction, including without limitation the rights to use, \n" +
		"copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the \n" +
		"Software, and to permit persons to whom the Software is furnished to do so, \n" +
		"subject to the following conditions:\n\n" +
		"1. Redistributions of source code must retain the above copyright notice, \n" +
		"   this list of conditions and the following disclaimers.\n" +
		"2. Redistributions in binary form must reproduce the above copyright notice, \n" +
		"   this list of conditions and the following disclaimers in the documentation \n" +
		"   and/or other materials provided with the distribution.\n" +
		"3. Neither the names of Chemistry and Computational Biology Group , NCSA, \n" +
		"   University of Illinois at Urbana-Champaign, nor the names of its contributors \n" +
		"   may be used to endorse or promote products derived from this Software without \n" +
		"   specific prior written permission.\n\n" +
		"THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, \n" +
		"EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF \n" +
		"MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.\n" +
		"IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR \n" +
		"ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,\n" +
		"TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE\n" +
		"OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.\n\n";
	    ScrollableDisplay sd = new ScrollableDisplay("test", message);
	    sd.addWindowListener(new WindowCloser(sd, true));
	    sd.pack();
	    sd.setSize(550,325);
	    sd.setVisible(true);
   		sd.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

}
