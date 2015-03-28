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
/*
 * Created on Sep 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package G03Input;

/**
 * @author SandeepKumar Seethaapathy 
 * @author Shashank Jeedigunta
 *  
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.util.*;
import javax.swing.event.*;

class  iopKey implements ActionListener, TableModelListener
{
public static JFrame iopFrame;
JTable table;
public static Vector<String> columns;
public static Vector< Vector<String> > rows;
DefaultTableModel tabModel;
JScrollPane scrollPane;
JLabel lblMessage;
JButton cmdAdd,cmdDelete,cmdSetValue,cmdGetValue;
JPanel mainPanel,buttonPanel;
public static String oV[],oP[],oN[];
public static String iopOp;
public static int iopFlag;
    public static void main(String[] args) 
    {
iopKey t=new iopKey();
    }

iopKey()
{
	iopFlag++;
	
	
	oV = new String[20];
	oP = new String[20];
	oN = new String[20];
	
rows=new Vector< Vector<String> >();
columns= new Vector<String>();
String[] columnNames = 
{ 
"Ov", 
"Op",
"N"
};
addColumns(columnNames);

tabModel=new DefaultTableModel();
tabModel.setDataVector(rows,columns);

table = new JTable(tabModel);
scrollPane= new JScrollPane(table);//ScrollPane

table.setRowSelectionAllowed(false);

table.getModel().addTableModelListener(this);

lblMessage=new JLabel("");


buttonPanel=new JPanel();
cmdAdd=new JButton("Add Row");
cmdDelete=new JButton("Delete") ;
cmdSetValue=new JButton("Set Values");
cmdGetValue=new JButton("Done");

buttonPanel.add(cmdAdd);
buttonPanel.add(cmdDelete);
buttonPanel.add(cmdSetValue);
buttonPanel.add(cmdGetValue);

cmdAdd.addActionListener(this);
cmdDelete.addActionListener(this);
cmdSetValue.addActionListener(this);
cmdGetValue.addActionListener(this);

mainPanel=new JPanel();
iopFrame=new JFrame("Options for Iop");
iopFrame.setSize(400,150);
//iopFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
mainPanel.setLayout(new BorderLayout());
mainPanel.add("Center",scrollPane);
mainPanel.add("South",buttonPanel);
mainPanel.setBackground(Color.white);
buttonPanel.setBackground(Color.white);
table.getParent().setBackground(Color.white);
iopFrame.getContentPane().add(mainPanel);
iopFrame.setVisible(true);

}
 public void addColumns(String[] colName)//Table Columns
{
for(int i=0;i<colName.length;i++)
columns.addElement(colName[i]);
}

public void addRow() //Add Row
{
Vector<String> r=new Vector<String>();
r=createBlankElement();
rows.addElement(r);
table.addNotify();

}

public Vector<String> createBlankElement() 
{
Vector<String> t = new Vector<String>();
t.addElement(" ");
t.addElement(" ");
t.addElement(" ");
return t;
}

 void deleteRow(int index) 
   {
     if(index!=-1)//At least one Row in Table
      { 
        rows.removeElementAt(index);
        table.addNotify();
       }

   }//Delete Row

 public void tableChanged(javax.swing.event.TableModelEvent source)     {
                 String msg="";
                 TableModel tabMod = (TableModel)source.getSource();
          switch (source.getType())
                   {
                       case TableModelEvent.UPDATE:
                    /*   msg="Table Value Updated for  cell "+table.getSelectedRow()+","+table.getSelectedColumn()+"\nWhich is "+table.getValueAt(table.getSelectedRow(),table.getSelectedColumn()).toString();
              JOptionPane.showMessageDialog(null,msg,"Table Example",JOptionPane.INFORMATION_MESSAGE);*/
                break;

                   }

    }//Table Changed Method

public void selectCell(int row,int col)
    {
         if(row!=-1 && col !=-1)            
          {
          table.setRowSelectionInterval(row,row);
          table.setColumnSelectionInterval(col,col);
          }
    }

public void actionPerformed(ActionEvent source)
    {
         if (source.getSource()==(JButton) cmdAdd)
         {
             addRow();
         }
         if (source.getSource()==(JButton) cmdDelete)
         {
             deleteRow(table.getSelectedRow());
         }
         if (source.getSource()==(JButton) cmdSetValue)
         {
         	for(int i=0;i<rows.size();i++)
         	{
         		System.out.println("Value at "+table.getValueAt(i,0).toString().length());
         		if(table.getValueAt(i,0).toString().length()==1)
         		{
         		for(int j=0;j<=2;j++)
         		{
         		 if(j==0) {
         	    String CName=JOptionPane.showInputDialog(null,"Enter Value for oV["+i+"]","",JOptionPane.INFORMATION_MESSAGE);
                if(!CName.trim().equals("") && table.getRowCount()>0)
	              {
	                  selectCell(i,j);
	                  table.setValueAt(CName,i,j);
	              }  
	     }
         		if(j==1){
                String CName=JOptionPane.showInputDialog(null,"Enter Value for oP["+i+"]",""+JOptionPane.INFORMATION_MESSAGE);
                if(!CName.trim().equals("") && table.getRowCount()>0)
	              {
	                  selectCell(i,j);
	                  table.setValueAt(CName,i,j);
	              }  
	     
         		}
         		if(j==2){
                 String CName=JOptionPane.showInputDialog(null,"Enter Value for oN["+i+"]",""+JOptionPane.INFORMATION_MESSAGE);
                 if(!CName.trim().equals("") && table.getRowCount()>0)
	              {
	                  selectCell(i,j);
	                  table.setValueAt(CName,i,j);
	              }  
	      		}
           			
         		}
         		}
         		else
         		{
         		continue;	
         		}
         	}
         		
         }
   
         if (source.getSource()==(JButton) cmdGetValue)
         {
        	iopOp ="Iop";
            if(table.getRowCount()>0)
             {
            	for(int u = 0;u<rows.size();u++)
            	{
            		for(int c=0;c<=2;c++)
            		{
            			System.out.println("Row size "+rows.size());
            			if(c==0)
            				oV[u]= table.getValueAt(u,c).toString();
            			if(c==1)
            				oP[u]= table.getValueAt(u,c).toString();
            			if(c==2)
            				oN[u]= table.getValueAt(u,c).toString();
            		}
            	}
            	iopOp += "(";
            	String temp = "";
            	if(rows.size()==0)
            	{}
            	else
            	{
            	for(int p=0;p<rows.size();p++)
            	{
            		temp += oV[p]+"/"+oP[p]+"="+oN[p];
            		if(p==rows.size()-1)
            		{
            			temp += ")";
            			break;
            		}
            		else 
            		{
            			temp +=",";
            			continue;}
            	}
            	iopOp += temp;
            	            	
            	// iopOption = iopOp;
            	if(!(InsertNode.nodeExists("Iop")))
					InsertNode.insertNode("Key","Iop");
                  otherKeyTable.otherKeys.addElement(iopOp);
                  iopFrame.hide();
            	}
             
             }
            else
            {
            	JOptionPane.showMessageDialog(null,"Iop keyword should have atleast one set of Options","Error",JOptionPane.OK_OPTION);
                //iopFrame.dispose();    
            }
     }
    
    }//ActionList

}


