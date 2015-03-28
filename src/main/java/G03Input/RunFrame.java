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
the Software is furnished to do so, subject to the following f
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
 * Created on Mar 16, 2005
 * @author Michael Sheetz 
 * @author Sandeep Kumar Seethaapathy @author Shashank Jeedigunta   
 * 
 */


package G03Input;
import java.io.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

public class RunFrame extends JFrame{
public static JFrame runFrame1,runFrame2;
public static JPanel panel1,panel2;
public static JRadioButton unRest,restClose,restOpen;
public static JButton ok1,ok2;
public static JLabel frameHead;
public static ButtonGroup mOpt;

/**
 * Why is there duplicated code:
 * RunFrame::createThreeOptionFrame
 * is the same as
 * MenuMethodListener::showOptionsFrame
 */
public static void createThreeOptionFrame() 
{
	JFrame.setDefaultLookAndFeelDecorated(true);
	runFrame1 = new JFrame("Select Wavefunction Type");
	panel1 = new JPanel(new GridBagLayout());
	GridBagConstraints c  = new GridBagConstraints();
	frameHead = new JLabel("Run calculation as");
	restClose = new JRadioButton("R - Restricted closed-shell");
	restOpen = new JRadioButton("RO - Restricted Open-shell");
	unRest = new JRadioButton("U - Unrestricted open-shell");
	ok1 = new JButton("OK");
	mOpt=new ButtonGroup();
	
	mOpt.add(restClose);
	mOpt.add(restOpen); 
	mOpt.add(unRest);
	restClose.setSelected(true);
	
	
	ok1.addActionListener(new MenuMethodListener());
	c.gridx=0;
	c.gridy=0;
	c.weightx=0.5;
	//c.weighty=0.15;
	c.insets= new Insets(0,10,10,100);
	panel1.add(frameHead,c);
	
	c.insets= new Insets(0,-90,10,-20);
    c.gridy=1;
    c.gridx=0;
	panel1.add(restClose,c);
	c.gridy=2;
	c.gridx=0;
	panel1.add(restOpen,c);
	c.gridy=3;
	c.gridx=0;
	panel1.add(unRest,c);
	c.gridy=4;
	c.gridx=0;
	c.insets = new Insets(0,90,10,90);
	panel1.add(ok1,c);
	
	runFrame1.getContentPane().add(panel1);
	runFrame1.pack();
	runFrame1.setLocation(200,200);
	runFrame1.setSize(300,225);
	runFrame1.setVisible(true);
	runFrame1.setResizable(true);
	runFrame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	
} 

public static void createTwoOptionFrame() 
{
	JFrame.setDefaultLookAndFeelDecorated(true);
	runFrame2 = new JFrame("Select Wavefunction Type");
	panel2 = new JPanel(new GridBagLayout());
	GridBagConstraints c  = new GridBagConstraints();
	frameHead = new JLabel("Run calculation as");
	
	restClose = new JRadioButton("R - Restricted closed-shell");
	restOpen = new JRadioButton("U - Unrestricted open-shell");
	ok2 = new JButton("OK");
	
	mOpt=new ButtonGroup();
	mOpt.add(restClose);
	mOpt.add(restOpen); 
	restClose.setSelected(true);
	c.gridx=0;
	c.weightx=0.5;
	//c.weighty=0.5;
	c.insets= new Insets(0,10,10,100);
	panel2.add(frameHead,c);
	c.insets= new Insets(0,-90,10,-20);
	c.gridy=1;
	c.gridx=0;
	panel2.add(restClose,c);
	c.gridy=2;
	panel2.add(restOpen,c);
	c.gridy=3;
	c.gridx=0;
	c.insets = new Insets(0,90,10,90);
	panel2.add(ok2,c);
	ok2.addActionListener(new MenuMethodListener());
	runFrame2.getContentPane().add(panel2);
	runFrame2.pack();
	runFrame2.setLocation(200,200);
	runFrame2.setSize(300,200);
	runFrame2.setVisible(true);
	runFrame2.setResizable(true);
	runFrame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
}





}


