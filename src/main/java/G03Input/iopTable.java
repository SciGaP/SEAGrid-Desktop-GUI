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
 * 
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta  @author Sandeep Kumar Seethaapathy 
 * 
 */


package  G03Input;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;


public class iopTable extends JFrame implements ActionListener{
   
	public static JTable iopTable;
    JFrame iopFrame;
public static String temp1;       //String that holds all iop options
    public static String iopOption;
	public static JTextField[] iopText;//=new JTextField[15];  
	public static JButton doneButton,exitButton;
	public static JPanel donePanel;
	public static DefaultTableModel dm;
	public static String oV[],oP[],oN[];
	public static String[] initValues= {"","","","","","","",""};
    public static Vector selectedRows;
    public static int selectedIndex[] =new int[8];
    int v,p,n;
    public static int noEntry;                  // to keep track of the number od entries in the table
    String Value="";
    public String[] iopNames = {"Ov","Op","N"};
    
    public iopTable()
    {
    oV = new String[10];
    oP = new String[10];
    oN = new String[10];
    Object[][] data = {
            {"", "",""},
            {"", "",""},
            {"", "",""},
            {"", "",""},
            {"", "",""},
        };

    iopFrame = new JFrame("Iop Options");
    //dm = new DefaultTableModel(iopNames,7);
   final JTable iopTable = new JTable(data,iopNames);
	//{
 /*    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        repaint();
      }
    };*/   
 // JTextField tf1 = new JTextField();
  //MyCellEditor ce = new MyCellEditor(tf1);
  //iopTable.getColumn("Ov").setCellEditor(ce);
  //iopTable.getColumn("Op").setCellEditor(ce);
  //iopTable.getColumn("N").setCellEditor(ce);
 // pbctable.setDefaultEditor( new RadioButtonEditor(new JCheckBox()), ce );

    doneButton = new JButton("Done");
   doneButton.addActionListener(this);
    exitButton = new JButton("Exit");
    exitButton.addActionListener(this);
    donePanel = new JPanel();
    donePanel.add(doneButton);
    donePanel.add(exitButton);
    iopTable.getTableHeader().setReorderingAllowed(false);
    iopFrame.getContentPane().setLayout(new BorderLayout());
    JScrollPane scroll = new JScrollPane(iopTable);
    iopFrame.getContentPane().add( scroll,BorderLayout.CENTER);
    iopFrame.getContentPane().add(donePanel,BorderLayout.SOUTH);
    iopFrame.setSize( 400,175);
    iopFrame.setLocation(200,75);
    iopFrame.setVisible(true);
    iopTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                printDebugData(iopTable);
            }
        });
    }
    public void actionPerformed(ActionEvent e)
    {
    	if(e.getSource()==exitButton)
    	{
    		iopFrame.dispose();
    	}
    	if(e.getSource()==doneButton)
    	{
    	 temp1= "Iop";
    		String temp2 = "";
    		if( (oV[0]!="") && (oP[0]!="") && (oN[0] !=""))
    			{
    			temp1 += "(";
    			for(int y=0;y<5;y++)
    				{
    				if(!(oV[y].equals(""))){
    	            temp2 += oV[y]+"/"+oP[y]+"="+oN[y];}
    				else {
				    break;}
    				if(!(oV[y+1].equals(""))){
			        temp2 += ",";	}
    				}
    		    temp2 += ")";
    	        temp1+=temp2;
    	        iopOption = temp1;
    	    	otherKeyTable.otherKeys.addElement(temp1);
    		    }
    			else{
    				otherKeyTable.otherKeys.addElement("Iop");	
    			}
    		iopFrame.dispose();
        }
    }
    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();
        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
            	if(j==0)
            	{
            		oV[i] = model.getValueAt(i,j).toString();
            	}
            	if(j==1)
            	{
            		oP[i] = model.getValueAt(i,j).toString();
               	}
            	if(j==2)
            	{
            		oN[i] = model.getValueAt(i,j).toString();
            	}
                System.out.println("  " + model.getValueAt(i, j));
                System.out.println("Opvector" + oP[i] +"\n"  + "oVVector" +oV[i] + "\n" + "Onvector" + oN[i]);
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    
    
    
    
    
    
    
    

}