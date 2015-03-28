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
 * Created on Mar 29, 2005
 * @author Michael Sheetz 
 * @author Sandeep Kumar Seethaapathy @author Shashank Jeedigunta   
 * 
 */



package G03Input;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;

public class popKeyOptTable extends JFrame implements ActionListener{
	public static JFrame keyOptFrame;
	public static JPanel helpPanel;
	public static JTextArea helpTextArea;
	public void actionPerformed(ActionEvent e)
    {
		if(e.getSource()==G03MenuTree.keyOptCombination)
		{
		
		Color bgColor= new Color(236,233,216);
		Color foreColor =new Color(0,78,152);
		JPanel donePanel;
		JButton doneButton,clearButton,exitButton;
		JTable table; 
		JFrame.setDefaultLookAndFeelDecorated(true);
	  	keyOptFrame = new JFrame("Options for Freq");
		  	keyOptFrame.setBackground(bgColor);
		  	keyOptFrame.setForeground(foreColor);
		    DefaultTableModel dm = new DefaultTableModel();
		    dm.setDataVector(
		      new Object[][]{
		        {new JRadioButton("Opt=ReadFC"),null},
		        {new JRadioButton("Opt=CalcHFFC"),null},
		        {new JRadioButton("Opt=CalcFC"),null},
		        {new JRadioButton("Opt=CalcAll"),null}},
		        	new Object[]{"Geometry Optimization","l"});
		         table = new JTable(dm) {
		         	public void tableChanged(TableModelEvent e) {
		         		super.tableChanged(e);
		         		repaint();
		         	}
		         };

		        
		   
		   table.getColumn("Geometry Optimization").setCellRenderer(new RadioButtonRenderer());
		   table.getColumn("Geometry Optimization").setCellEditor(new RadioButtonEditor(new JCheckBox()));
		    
		   table.getColumn("l").setCellRenderer(new RadioButtonRenderer());
		   table.getColumn("l").setCellEditor(new RadioButtonEditor(new JCheckBox()));
		    donePanel = new JPanel(new GridBagLayout());
		    GridBagConstraints c  = new GridBagConstraints();
		   
		    c.gridy=0;
		    c.gridx=0;
		    c.insets = new Insets(10,60,80,100);
		    helpTextArea = new JTextArea();
		    donePanel.add(helpTextArea,c);
		    
		    
		    c.gridx=0;
		    c.gridy=1;
		    c.insets = new Insets(0,0,0,0); 
		    doneButton = new JButton("Done");
		    //clearButton = new JButton("Clear");
		    exitButton = new JButton("Exit");
		    donePanel.add(doneButton,c);
		    c.gridx=2;
		    //donePanel.add(clearButton,c);
		    c.gridx=3;
		    donePanel.add(exitButton,c);
		    c.gridx=4;
		    donePanel.setBackground(bgColor);
		    donePanel.setForeground(foreColor);
		    table.getTableHeader().setReorderingAllowed(false);
		    
		    keyOptFrame.getContentPane().setLayout(new BorderLayout());
		    
		    
		    
		    JScrollPane scroll = new JScrollPane(table);
		    scroll.add(helpTextArea);
		    keyOptFrame.getContentPane().add( scroll,BorderLayout.NORTH);
		    keyOptFrame.getContentPane().add(donePanel,BorderLayout.CENTER);
		    keyOptFrame.setSize( 600,200);
		    keyOptFrame.setLocation(200,75);
		    keyOptFrame.setVisible(true);
		    helpPanel=new JPanel(new GridBagLayout());
		    
		  
		   
		   // helpPanel.add(helpTextArea,c);
		    //keyOptFrame.getContentPane().add(helpPanel,BorderLayout.SOUTH);
		  }	
    }
}

