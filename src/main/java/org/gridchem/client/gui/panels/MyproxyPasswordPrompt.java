/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Jun 29, 2005
 * 
 * Developed by: CCT, Center for Computation and Technology, 
 * 				NCSA, University of Illinois at Urbana-Champaign
 * 				OSC, Ohio Supercomputing Center
 * 				TACC, Texas Advanced Computing Center
 * 				UKy, University of Kentucky
 * 
 * https://www.gridchem.org/
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal with the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimers.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimers in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
 *    University of Illinois at Urbana-Champaign, nor the names of its contributors 
 *    may be used to endorse or promote products derived from this Software without 
 *    specific prior written permission.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS WITH THE SOFTWARE.
*/

package org.gridchem.client.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;


import org.gridchem.client.GridChem;
//import org.gridchem.client.authentication.access.GlobusTask;
import org.gridchem.client.common.Settings;


/**
 * Basically JOptionPane with a password field instead of text box.
 * 
 * @author Rion Dooley < dooley [at] cct [dot] lsu [dot] edu >
 *
 */
public class MyproxyPasswordPrompt extends JFrame{
    final JFrame f;
    JPasswordField passwordField;
    JButton okButton;
    
    public MyproxyPasswordPrompt(String message) {
	      f = new JFrame(message);
	      
	      passwordField = new JPasswordField(10);
	      passwordField.setEchoChar('*');
	      passwordField.addKeyListener(new ButtonKeyListener());
	      okButton = new JButton("OK");
	      okButton.addActionListener(new ButtonListener());
	      okButton.addKeyListener(new ButtonKeyListener());
	      JPanel contentPane = new JPanel(new BorderLayout());
	      contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	      contentPane.add(okButton, BorderLayout.WEST);
	      contentPane.add(passwordField, BorderLayout.CENTER);
	
	      f.setContentPane(contentPane);
	      f.pack();
	      f.setResizable(true);
	      
	      Toolkit kit = f.getToolkit();
	      Dimension screenSize = kit.getScreenSize();
	      int screenWidth = screenSize.width;
	      int screenHeight = screenSize.height;
	      Dimension windowSize = f.getSize();
	      int windowWidth = windowSize.width;
	      int windowHeight = windowSize.height;
	      int upperLeftX = (screenWidth - windowWidth)/2;
	      int upperLeftY = (screenHeight - windowHeight)/2;
	      f.setLocation(upperLeftX, upperLeftY);
	      
	      f.setVisible(true);
          f.addWindowListener(new WindowListener() {
              public void windowIconified(WindowEvent e) {}
              public void windowDeiconified(WindowEvent e) {}
              public void windowActivated(WindowEvent e) {}
              public void windowDeactivated(WindowEvent e) {}
              public void windowOpened(WindowEvent arg0) {}
              public void windowClosing(WindowEvent e) {}
              public void windowClosed(WindowEvent e) {
//                GridChem.oc.lp.ls2.showProgressDialog(false);
              }
          });
	      
    }
    
    public String getPassword() {
        return this.passwordField.getText();
    }
    
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
      		Settings.myproxyPass = passwordField;
      		f.setVisible(false);
//      		GlobusTask.loadMyproxy(getPassword());
        }
    }
    
    private class ButtonKeyListener implements KeyListener {
        public void keyTyped(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
      		if (key == KeyEvent.VK_ENTER) {
          		Settings.myproxyPass = passwordField;
          		f.setVisible(false);
//          		GlobusTask.loadMyproxy(getPassword());
      		}
        }
    }
}