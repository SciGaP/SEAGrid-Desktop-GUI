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
 * Created on Mar 21, 2005
 * @author Michael Sheetz 
 * @author Sandeep Kumar Seethaapathy @author Shashank Jeedigunta 
 * 
 */

package G03Input;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.util.*;
  class TextFieldRenderer implements TableCellRenderer {
  public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
    if (value==null) return null;
    return (Component)value;
  }
}

class TextFieldEditor  extends   DefaultCellEditor
                        implements ActionListener {
   
	public static JTextField text;
	public TextFieldEditor (JTextField textField) {
		super(textField);
     }

    public Component getTableCellEditorComponent(JTable table, Object value,
                   boolean isSelected, int row, int column) {
    if (value==null) return null;
    text = (JTextField)value;
   
    text.addActionListener(this);
   
    iopKeymodify.rowCount++;
    return (Component)value;
    }

  public Object getCellEditorValue() {
    text.removeActionListener(this);
    return text;
  }
  public void actionPerformed(ActionEvent e)
  {
	
	  super.fireEditingCanceled();
  }
  }

public class iopKeymodify extends JFrame implements ActionListener{
public static JFrame iopFrame;
Color bgColor= new Color(236,233,216);
Color foreColor =new Color(0,78,152);
JPanel donePanel;
JButton doneButton,clearButton,exitButton;

public static JTable table; 
public static String oV[],oP[],oN[];
public static String iopOp;
public static int iopFlag,errorFlag,reloadFlag;
public static JTextField[] inputFields = new JTextField[15];

public static   int rowCount;
public iopKeymodify(){
	
	super("Options for Iop");
	if(reloadFlag!=0)	
	{
		iopFrame.show();
	}
	else
	{
    rowCount=0;
	iopFlag++;
	oV = new String[20];
	oP = new String[20];
	oN = new String[20];
	for(int i=0;i<15;i++)
	{
		inputFields[i]=new JTextField();
	}
	for(int y=0;y<15;y++)
		{
			inputFields[y].addActionListener(this);
		}
	JFrame.setDefaultLookAndFeelDecorated(true);
  	
  	iopFrame = new JFrame("Options for IOp");
  	iopFrame.setBackground(bgColor);
  	iopFrame.setForeground(foreColor);
    DefaultTableModel dm = new DefaultTableModel();
    dm.setDataVector(
      new Object[][]{
        {inputFields[0],inputFields[1],inputFields[2]},
        {inputFields[3],inputFields[4],inputFields[5]},
        {inputFields[6],inputFields[7],inputFields[8]},
        {inputFields[9],inputFields[10],inputFields[11]},
        {inputFields[12],inputFields[13],inputFields[14]}},
        new Object[]{"Op","Ov","N"});
  	
     table = new JTable(dm) {
      public void tableChanged(TableModelEvent e) {
        
        super.tableChanged(e);
        repaint();
        
      }
    };
  
  table=new JTable(dm);
    table.getColumn("Op").setCellRenderer(new TextFieldRenderer());
    table.getColumn("Op").setCellEditor(new TextFieldEditor(new JTextField()));
    table.getColumn("Ov").setCellRenderer(new TextFieldRenderer());
    table.getColumn("Ov").setCellEditor(new TextFieldEditor(new JTextField()));
    table.getColumn("N").setCellRenderer(new TextFieldRenderer());
    table.getColumn("N").setCellEditor(new TextFieldEditor(new JTextField()));
    donePanel = new JPanel();
    doneButton = new JButton("Done");
    clearButton = new JButton("Reset");
    exitButton = new JButton("Exit");
    doneButton.addActionListener(this);
    clearButton.addActionListener(this);
    exitButton.addActionListener(this);
    
    
    donePanel.add(doneButton);
    donePanel.add(clearButton);
    donePanel.add(exitButton);
    
    
    donePanel.setBackground(bgColor);
    donePanel.setForeground(foreColor);
    table.getTableHeader().setReorderingAllowed(false);
    iopFrame.getContentPane().setLayout(new BorderLayout());
    JScrollPane scroll = new JScrollPane(table);
    iopFrame.getContentPane().add( scroll,BorderLayout.CENTER);
    iopFrame.getContentPane().add(donePanel,BorderLayout.SOUTH);
    iopFrame.setSize( 500,150);
    iopFrame.setLocation(200,75);
    iopFrame.setVisible(true);
	} //End of else    
   
    }

public static void loadValues()
{
	iopOp ="Iop";
	  for(int p =0;p<iopKeymodify.rowCount/3;p++)
	   {
		 
		  int l = (p*2)+p;
		  for(int c = 0;c<3;c++)
		  {	  if(c==0)
			  oV[p] = inputFields[l].getText().toString();
		  	  if(c==1)
			  oP[p] = inputFields[l+1].getText().toString();
			  if(c==2)
			  oN[p] = inputFields[l+2].getText().toString();
		  }
	   }
	  iopOp += "(";
	  String temp = "";
	  if(iopKeymodify.rowCount==0)
	  {
		  
	  }
	  else
	  {
		  for(int p=0;p<iopKeymodify.rowCount/3;p++)
		  {
			  temp += oV[p]+"/"+oP[p]+"="+oN[p];
				if(p==((iopKeymodify.rowCount/3)-1))
						
					{
						temp += ")";
						break;
					}
					else 
					{
						temp +=",";
						continue;
				    }
				  }
			iopOp += temp;
			if(!(InsertNode.nodeExists("Iop")))
				InsertNode.insertNode("Key","Iop");
			  otherKeyTable.otherKeys.addElement(iopOp);
			  reloadFlag++;
			  iopFrame.hide(); 
	  }
}


public void actionPerformed(ActionEvent e)
{
   
   if(e.getSource()==exitButton)
   {
   iopFrame.dispose();
   }
   if(e.getSource()==clearButton)
   {
	   reloadFlag=0;
	   iopFrame.dispose();
	   new iopKeymodify();
   }
   if(e.getSource()==doneButton)
   {
	   if(rowCount==0)
	   {
		   JOptionPane.showMessageDialog(this,"Iop keyword should have atleast one set of Options","Error",JOptionPane.OK_OPTION);
	   }
	   if((iopKeymodify.rowCount%3)!=0)
		   
	   {
		   JOptionPane.showMessageDialog(this,"Please Check your Input Values","Error",JOptionPane.OK_OPTION);
		   errorFlag=1;
	   }
   for(int p = 0;p<iopKeymodify.rowCount/3;p++)
   {
	   System.out.println(iopKeymodify.rowCount);
	   int l = (p*2)+p;
	       if(inputFields[l].getText().toString().length()==0)
	       {
	    	   JOptionPane.showMessageDialog(this,"Please Check your Input Values","Error",JOptionPane.OK_OPTION);
	    	   errorFlag=1;
	    	   break;
	       }else{System.out.println(inputFields[l].getText().toString());} 
	   
	       if(inputFields[l+1].getText().toString().length()==0)
	       {
		   JOptionPane.showMessageDialog(this,"Please Check your Input Values","Error",JOptionPane.OK_OPTION);
		   errorFlag=1;
		   break;
	       }else{System.out.println(inputFields[l+1].getText().toString());}
	   
	   	   if(inputFields[l+2].getText().toString().length()==0)
	   	   {
		   JOptionPane.showMessageDialog(this,"Please Check your Input Values","Error",JOptionPane.OK_OPTION);
		   errorFlag=1;
		   break;
	   	   }else {System.out.println(inputFields[l+2].getText().toString());}
	   
   }
   if(errorFlag==0)
   {
   loadValues();
   }
   if(errorFlag==1)
   {
	   iopFrame.dispose();
	   errorFlag=0;
	   new iopKeymodify();
   }
   }
   
 }

  public static void main(String[] args) {
 
    iopKeymodify frame = new iopKeymodify();
      frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
  }

}
