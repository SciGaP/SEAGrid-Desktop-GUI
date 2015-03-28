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
 * Created on Apr 8, 2005
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta  @author Sandeep Kumar Seethaapathy 
 * 
 */



package G03Input;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

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




class MyCellEditor extends DefaultCellEditor {
       JTextField textfield;
       
      public MyCellEditor( JTextField t ) {
          super(t);
          textfield = t;
       }
}
public class pbcTable extends JFrame implements ItemListener,ActionListener{
    public static JTable pbctable;
    
	JFrame pbcFrame;
	public static JRadioButton[] pbcOptions= {new JRadioButton("CellRange="),
	        new JRadioButton("GammaOnly"),
	        new JRadioButton("NKPoint="),
	        new JRadioButton("NCellMax="),
	        new JRadioButton("NCellMin="),
	        new JRadioButton("NCellDFT="),
	        new JRadioButton("NCellK="),
	        new JRadioButton("NCellE2=")
	        };
	
	public static JTextField[] pbcText=new JTextField[8];  
	public static JButton doneButton,exitButton;
	public static JPanel donePanel;
	public static DefaultTableModel dm;
	public static String[] initValues= {"","","","","","","",""};
    public static Vector<String> pbcOpt;
    public static int pbcC,pbcOptC,pbcOptCount;
    public static int pbcFlag,pbcListen ;
    public static Vector selectedRows;
    public static int selectedIndex[] =new int[8];
    static int k=0,lindex;
    String Value="";
    
    public pbcTable(){
    	
    pbcFrame = new JFrame("PBC Options");
   
    if(pbcListen==0)
   {
        for(int i=0;i<pbcOptions.length;i++)
        {
            pbcOptions[i].addItemListener(this);
            pbcOptions[i].setBackground(Color.WHITE);
        
        }
    
    pbcListen++;
   }
      
    
    dm = new DefaultTableModel();
    dm.setDataVector(
      new Object[][]{
              {pbcOptions[0],initValues[0]},	
              {pbcOptions[1],initValues[1]},
              {pbcOptions[2],initValues[2]},
              {pbcOptions[3],initValues[3]},
              {pbcOptions[4],initValues[4]},
              {pbcOptions[5],initValues[5]},
              {pbcOptions[6],initValues[6]},
              {pbcOptions[7],initValues[7]},
        },
        
      new Object[]{"Option","N"});
                     
    JTable pbctable = new JTable(dm) {
      public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        repaint();
      }
    };
    
    
    pbctable.getColumn("Option").setCellRenderer(new RadioButtonRenderer());
    pbctable.getColumn("Option").setCellEditor(new RadioButtonEditor(new JCheckBox()));
   JTextField tf1 = new JTextField();
      MyCellEditor ce = new MyCellEditor(tf1);

   //  pbctable.getColumn("N").setCellRenderer(JTable.DefaultTableCellRenderer);
  pbctable.getColumn("N").setCellEditor(ce);
 // pbctable.setDefaultEditor( new RadioButtonEditor(new JCheckBox()), ce );

    doneButton = new JButton("Done");
    doneButton.addActionListener(this);
   // clearButton = new JButton("Clear");
    exitButton = new JButton("Exit");
    //doneButton.addActionListener()
   // clearButton.addActionListener(this);
    exitButton.addActionListener(this);
    
    donePanel = new JPanel();
    donePanel.add(doneButton);
  //  donePanel.add(clearButton);
    donePanel.add(exitButton);
    
    for(int i=0;i<8;i++)
    {
        // Enable the text fields only when they select it.
     //  pbcText[i]=new JTextField();
      // pbcText[i].setEnabled(false);
    }
        
    
    
  
    
    pbctable.getTableHeader().setReorderingAllowed(false);
    
    
    
    pbcFrame.getContentPane().setLayout(new BorderLayout());
    JScrollPane scroll = new JScrollPane(pbctable);
    pbcFrame.getContentPane().add( scroll,BorderLayout.CENTER);
    pbcFrame.getContentPane().add(donePanel,BorderLayout.SOUTH);
    pbcFrame.setSize( 400,200);
    pbcFrame.setLocation(200,75);
    pbcFrame.setVisible(true);
  }
    public void actionPerformed(ActionEvent ae)
   {
    	if(ae.getSource()==doneButton){
    	
    	   System.out.println("The value of freq guessV = "+pbcC);
    	   	
    	   	if(RouteClass.initCount==0)
    	   	{
    	    System.out.println(RouteClass.initCount+" \tInside init");
    	   	RouteClass.initBuffer();
    	   	RouteClass.initCount++;
    	   	}
    	   	
    	   	if(pbcC==0)
    	   	{
    	   	System.out.println(pbcC+" \t Inside pbcC");
    	   	pbcFlag=RouteClass.keyIndex;
    	   	RouteClass.keyIndex++;
    	   	pbcC++;
    	   	}
    	   	else
    	   	{
    	   		System.out.println("PBC Check");
    	   		RouteClass.keywordBuffer[pbcFlag]=new StringBuffer();
    	   	}
    	   	RouteClass.keywordBuffer[pbcFlag].append("PBC");
    		
    	    if(!(InsertNode.nodeExists("PBC")))
                InsertNode.insertNode("Key","PBC");
    	   	
    	   	//if(pbcOpt.size()>0)
    		//{
    		
    	   	int smallIndex=9;
    		for(int ll=0;ll<8;ll++)
    		{
    		    if(selectedIndex[ll]==1)
    		    {
    		      if(ll<smallIndex)
    		      smallIndex=ll;
    		    
    		    }
    		    
    		    
    		}
    		        try {
    		        	
    		        	
						for(int j=0;j<8;j++)
						{  Value="";
						    if(selectedIndex[j]==1)
						    {   
						    	if(j==smallIndex)
						    	    RouteClass.keywordBuffer[pbcFlag].append("=("); // add only for first char.
						      if(j!=1)
						       {Value=(dm.getValueAt(j,1)).toString();
						       	initValues[j]=Value;
						       }
						       System.out.println("VALU"+pbcText[j]);
						      System.out.println("Selected index"+pbcOptions[j].getActionCommand());
						      RouteClass.keywordBuffer[pbcFlag].append(pbcOptions[j].getActionCommand()+Value);
						      RouteClass.keywordBuffer[pbcFlag].append(",");
						      
						    }
						}
						System.out.println(RouteClass.keywordBuffer[pbcFlag]);
						lindex=RouteClass.keywordBuffer[pbcFlag].lastIndexOf(",");
		       RouteClass.keywordBuffer[pbcFlag].setCharAt(lindex,')');
					} catch (NullPointerException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
					catch(StringIndexOutOfBoundsException e)
					{
						
					}
    		    	
    		 // }
    		    
    		 /*
    		 else
    		 {
    		 	if(pbcOpt.size()>0)
    		 	RouteClass.keywordBuffer[pbcFlag].append("="+"("+pbcOpt.get(0)+")");
    		 }
    		*/
    		 pbcFrame.dispose();
    		 RouteClass.writeRoute();
    	   }
    	
    	if(ae.getSource()==exitButton)
    	{
    		pbcFrame.dispose();
    	}
    	
    	
   }
    	 
	 
	public void itemStateChanged(ItemEvent e)
	{
	  
	    System.out.println("ITE<<M>>");
	    for(int i=0;i<8;i++)
			{
	        System.out.println("Inside for in itemstate changed");
	        if(e.getItem()==pbcOptions[i])
	        {
	            System.out.println("Inside e.getItem() ... OptionSelected is"+pbcOptions[i]);
	            
	            if(((JRadioButton)e.getItem()).isSelected())
	            {
	            	G03Listener.pbcTabF++;
	                if(pbcOptC==0)
	    			{
	    				pbcOpt = new Vector<String>(8);
	    				pbcOptC++;
	    			}
	                
	                //pbcText[i].setEnabled(true);
	               // pbcText[1].setEnabled(false);
	                
	                
	                if(!(InsertNode.nodeExists("PBC")))
	                InsertNode.insertNode("Key","PBC");
	                
	                String pbcOptionName =new String(((JRadioButton)e.getItem()).getActionCommand().toString());
	                if(pbcOptionName.endsWith("="))
	                    pbcOptionName=pbcOptionName.substring(0,pbcOptionName.length()-1);
	            	    InsertNode.insertNode("PBC",pbcOptionName);
	            	//	String pbcOptionName =RadioButtonEditor.button.getActionCommand();
	            		selectedIndex[i]=1;// index of the item selected
	            		
	            		pbcOpt.addElement(pbcOptionName);
	                	//pbcCount++;
	           	}
	            			
	         	else // deselected
	            {
	         	   String pbcOName =new String(((JRadioButton)e.getItem()).getActionCommand().toString());
	                if(pbcOName.endsWith("="))
	                    pbcOName=pbcOName.substring(0,pbcOName.length()-1);
	         	    
	         	    if(InsertNode.nodeExists(pbcOName))
	         	    InsertNode.deleteNode(pbcOName);
	         	 
	         	    pbcOpt.removeElement(((JRadioButton)e.getItem()).getActionCommand().toString());
	         	 selectedIndex[i]=0;
	            	
	          }
	                
	                
	       }
	            
			}      
	            
	            
	        }

}
	
	


