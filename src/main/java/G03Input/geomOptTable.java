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
 * Created on Mar 30, 2005
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta @author Sandeep Kumar Seethaapathy  
 * 
 */


package G03Input;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class geomOptTable implements ItemListener,ActionListener{
	Color bgColor= new Color(236,233,216);
	Color foreColor =new Color(0,78,152);
	JPanel donePanel;
	JButton doneButton,clearButton,exitButton;
	public static JTable table;
	JFrame geomOptFrame;
	public static int geomC,geomFlag,geomOptC,geomListen;
	public static Vector<JRadioButton> geomClear;
	public static Vector<String> geomOpt;
	public static String geoSt,geoRe,geoCh,geoIh;
	public static JRadioButton GeCheck =new JRadioButton("Checkpoint");
	public static JRadioButton GeDist = new JRadioButton("Distance");
	public static JRadioButton GeKeep =new JRadioButton("KeepConstants");
	public static JRadioButton GeModea = new JRadioButton("ModelA");
	public static JRadioButton GeAll= new JRadioButton("AllCheck");
	public static JRadioButton GeAng = new JRadioButton("Angle");
	public static JRadioButton GeKeepd =new JRadioButton("KeepDefinition");
	public static JRadioButton GeModeb =new JRadioButton("ModelB");
	public static JRadioButton GeStep =new JRadioButton("Step=N");
	public static JRadioButton GeCang =new JRadioButton("CAngle");
	public static JRadioButton GeRedun = new JRadioButton("NewRedundant");
	public static JRadioButton GePrin =new JRadioButton("Print");
	public static JRadioButton GeModr =new JRadioButton("ModRedundant");
	public static JRadioButton Gedihed = new JRadioButton("Dihedral");
	public static JRadioButton Gecrow =new JRadioButton("Crowd");
	public static JRadioButton GeModi = new JRadioButton("Modify");
	public static JRadioButton Gecdih = new JRadioButton("CDihedral");
	public static JRadioButton GeIndep =new JRadioButton("Independent");
	public static JRadioButton GeConn = new JRadioButton("Connect");
	public static JRadioButton GePrint =new JRadioButton("PrintInputOrient");
	public static JRadioButton GeMod = new JRadioButton("ModConnect");
	public static JRadioButton GeZmco = new JRadioButton("ZMConnect");
	public static JRadioButton GeIhar = new JRadioButton("IHarmonic=N");
	public static JRadioButton GeChk = new JRadioButton("ChkHarmonic=N");
	public static JRadioButton GeRead =new JRadioButton("ReadHarmonic=N");
	
	
	 public geomOptTable(){
     //geomOpt= new Vector(10);
	geomOptFrame=new JFrame("Geom Options");
	if(geomListen==0)
	{
		geomClear=new Vector<JRadioButton>(30);
		geomClear.addElement(GeCheck);
		geomClear.addElement(GeDist);
		geomClear.addElement(GeKeep);
		geomClear.addElement(GeModea);
		geomClear.addElement(GeAll);
		geomClear.addElement(GeAng);
		geomClear.addElement(GeKeepd);
		geomClear.addElement(GeModeb);
		//geomClear.addElement(GeStep);
		geomClear.addElement(GeCang);
		geomClear.addElement(GeRedun);
		geomClear.addElement(GePrin);
		geomClear.addElement(GeModr);
		geomClear.addElement(Gedihed);
		geomClear.addElement(GeDist);
		geomClear.addElement(Gecrow);
		geomClear.addElement(GeModi);
		geomClear.addElement(Gecdih);
		geomClear.addElement(GeIndep);
		geomClear.addElement(GeConn);
		geomClear.addElement(GePrint);
		geomClear.addElement(GeMod);
		geomClear.addElement(GeZmco);
	//	geomClear.addElement(GeIhar);
		//geomClear.addElement(GeChk);
	//	geomClear.addElement(GeRead);
		GeCheck.setBackground(Color.WHITE);
	 GeCheck.addItemListener(this);
	 GeDist.setBackground(Color.WHITE);
    GeDist.addItemListener(this);
    GeKeep.setBackground(Color.WHITE);
    GeKeep.addItemListener(this);
    GeModea.setBackground(Color.WHITE);
    GeModea.addItemListener(this);
    GeAll.setBackground(Color.WHITE);
    GeAll.addItemListener(this);
    GeAng.setBackground(Color.WHITE);
    GeAng.addItemListener(this);
    GeKeepd.setBackground(Color.WHITE);
    GeKeepd.addItemListener(this);
    GeModeb.setBackground(Color.WHITE);
    GeModeb.addItemListener(this);
    GeStep.setBackground(Color.WHITE);
    GeStep.addItemListener(this);
    GeCang.setBackground(Color.WHITE);
    GeCang.addItemListener(this);
    GeRedun.setBackground(Color.WHITE);
    GeRedun.addItemListener(this);
    GePrin.setBackground(Color.WHITE);
    GePrin.addItemListener(this);
    GeModr.setBackground(Color.WHITE);
    GeModr.addItemListener(this);
    Gedihed.setBackground(Color.WHITE);
    Gedihed.addItemListener(this);
    Gecrow.setBackground(Color.WHITE);
    Gecrow.addItemListener(this);
    
    GeModi.setBackground(Color.WHITE);
    GeModi.addItemListener(this);
    Gecdih.setBackground(Color.WHITE);
    Gecdih.addItemListener(this);
    GeIndep.setBackground(Color.WHITE);
    GeIndep.addItemListener(this);
    GeConn.setBackground(Color.WHITE);
    GeConn.addItemListener(this);
    GePrint.setBackground(Color.WHITE);
    GePrint.addItemListener(this);
    GeMod.setBackground(Color.WHITE);
    GeMod.addItemListener(this);
    GeZmco.setBackground(Color.WHITE);
    GeZmco.addItemListener(this);
    GeIhar.setBackground(Color.WHITE);
    GeIhar.addItemListener(this);
    GeChk.setBackground(Color.WHITE);
    GeChk.addItemListener(this);
    GeRead.setBackground(Color.WHITE);
    GeRead.addItemListener(this);
	geomListen++;
	}
    
    
    
    
    //UIDefaults ui = UIManager.getLookAndFeel().getDefaults();
    
  //UIManager.put("RadioButton.focus", ui.getColor("control"));
   
    DefaultTableModel dm = new DefaultTableModel();
    dm.setDataVector(
      new Object[][]{
      		
      		
        {GeCheck,GeDist,GeKeep,GeModea},
        {GeAll,GeAng,GeKeepd,GeModeb},
        {GeStep,GeCang,GeRedun,GePrin},
		{GeModr,Gedihed,Gecrow,null},
        {GeModi,Gecdih,GeIndep,null},
        {GeConn,GePrint,null,null},
        {GeMod,null,null,null},
        {GeZmco,null,null,null},
        {GeIhar,null,null,null},
        {GeChk,null,null,null},
        {GeRead,null,null,null},
        			},
        
      new Object[]{"Item Selection","Output Related","Geometric Specification and Checking","Model Builder"});
                     
    JTable table = new JTable(dm) {
      public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        repaint();
      }
    };
    
    table.getColumn("Item Selection").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Item Selection").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    
    table.getColumn("Output Related").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Output Related").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    table.getColumn("Geometric Specification and Checking").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Geometric Specification and Checking").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    
    table.getColumn("Model Builder").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Model Builder").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    
    doneButton = new JButton("Done");
    doneButton.addActionListener(this);
   // clearButton = new JButton("Clear");
    exitButton = new JButton("Exit");
    
    donePanel = new JPanel();
    donePanel.add(doneButton);
    //donePanel.add(clearButton);
    donePanel.add(exitButton);
    exitButton.addActionListener(this);
    donePanel.setBackground(bgColor);
    donePanel.setForeground(foreColor);
    table.getTableHeader().setReorderingAllowed(false);
    geomOptFrame.getContentPane().setLayout(new BorderLayout());
    JScrollPane scroll = new JScrollPane(table);
    geomOptFrame.getContentPane().add( scroll,BorderLayout.CENTER);
    geomOptFrame.getContentPane().add(donePanel,BorderLayout.SOUTH);
    geomOptFrame.setSize( 600,200);
    geomOptFrame.setLocation(200,75);
    geomOptFrame.setVisible(true);
  }

	 public void itemStateChanged(ItemEvent e)
	 {
	 	
	 	if((e.getItem()==GeCheck)||
	 		(e.getItem()==GeDist)||
			(e.getItem()==GeKeep)||
			(e.getItem()==GeModea)||
			(e.getItem()==GeAll)||
			(e.getItem()==GeAng)||
			(e.getItem()==GeKeepd)||   
			(e.getItem()==GeModeb)||
			//(e.getItem()==GeStep)||
			(e.getItem()==GeCang)||
			(e.getItem()==GeRedun)||
			(e.getItem()==GePrin)||
			(e.getItem()==GeModr)||
			(e.getItem()==Gedihed)||
			(e.getItem()==Gecrow)||
			(e.getItem()==GeModi)||
			(e.getItem()==Gecdih)||
			(e.getItem()==GeIndep)||
			(e.getItem()==GeConn)||
			(e.getItem()==GePrint)||
			(e.getItem()==GeMod)||
			(e.getItem()==GeZmco))
	
			{
	 		G03Listener.geomTabF++;
		if(((JRadioButton)e.getItem()).isSelected())
		{
			if(geomOptC==0)
			{
				geomOpt= new Vector<String>(10);
				geomOptC++;
			}
		if(!(InsertNode.nodeExists("Geom")))
		
			InsertNode.insertNode("Key","Geom");
		InsertNode.insertNode("Geom",RadioButtonEditor.button.getActionCommand());
		String geoms  =RadioButtonEditor.button.getActionCommand();
		geomOpt.addElement(geoms);
    			
		}//System.out.println(e.getActionCommand());
			
		else
		{
			if(InsertNode.nodeExists(((JRadioButton)e.getItem()).getActionCommand()))
	    		InsertNode.deleteNode(((JRadioButton)e.getItem()).getActionCommand());
	    		geomOpt.removeElement(((JRadioButton)e.getItem()).getActionCommand());
		}
			}
	 
	 	if(e.getItem()==GeStep)
	 	{
	 		  	if(((JRadioButton)e.getItem()).isSelected())
	 			{
	 		  		G03Listener.geomTabF++;
	 				if(geomOptC==0)
	 				{
	 					geomOpt= new Vector<String>(10);
	 					geomOptC++;
	 				}
	 			
	 	       	    JTextField stepField;
	 	        	JPanel txt;
	 	        	txt = new JPanel(new FlowLayout());
	 	       	stepField = new JTextField(3);
	 	    	stepField.setSize(2,1);
	 	    	Object[] obj = new Object[2];
	 	    	obj[0] = " Use Step-size of 0.0001N Angstroms ";
	 	    	JLabel s  = new JLabel("for Numerical differentiation with N="); //+stepField;
	 	    	txt.add(s);
	 	    	txt.add(stepField);
	 	    	obj[1] = txt;
	 	    	int ch= JOptionPane.showConfirmDialog(null,obj,"Option Step=N",JOptionPane.OK_OPTION);
	 	    	if(ch==0)
	 	    	{
	 	    	    if(!(InsertNode.nodeExists("Geom")))
	 	    		 {InsertNode.insertNode("Key","Geom");
	 	    		 }
	 	    	    //Delete Existing node and then add it
	 			    if((InsertNode.nodeExists("Step")))
	 			    {
	 	    	    InsertNode.deleteNode("Step=");
	 			    }
	 	    	    InsertNode.insertNode("Geom","Step="+stepField.getText());
	 	    	    
	 	    		geoSt = new String();
				     geoSt = stepField.getText();
				     geomOpt.addElement("Step="+geoSt);
 					}
				if(ch==1) // pressed no remove step
				{
				    if((InsertNode.nodeExists("MaxStep")))
				    {
				        //Delete Node if exists
				        InsertNode.deleteNode("Step=");
				        geomOpt.removeElement("Step="+geoSt);
				    }
				
				}
	 			}
	 		  	else
	 		  	{	
	 		  			if((InsertNode.nodeExists("Step=")))
	                    InsertNode.deleteNode("Step=");
	 		  			geomOpt.removeElement("Step="+geoSt);         
	 		  	}
	 		}
	 	    	  	
		if(e.getItem()==GeRead)
	 	{
			G03Listener.geomTabF++;
	 		  	if(((JRadioButton)e.getItem()).isSelected())
	 			{
	 		  		if(geomOptC==0)
	 				{
	 					geomOpt= new Vector<String>(10);
	 					geomOptC++;
	 				}
	 			
	 		  		JTextField stepField;
	 		    	JPanel txt;
	 		    	txt = new JPanel(new GridBagLayout());
	 		    	GridBagConstraints c = new GridBagConstraints();
	 			System.out.println("OOO");
	 			stepField = new JTextField(3);
	 			stepField.setSize(2,1);
	 			Object[] obj = new Object[3];
	 			
	 			obj[0] = "Add harmonic contraints to a structure read in";
	 			obj[1] = " from the input stream with force constant";
	 			
	 			JLabel s  = new JLabel(" N/1000 Hartree/Bohr*Bohr with N ="); //+stepField;
	 			c.insets=new Insets(0,0,0,0);
	 			txt.add(s,c);
	 			c.insets = new Insets(0,0,0,30);
	 			txt.add(stepField,c);
	 			obj[2] = txt;
	 			int ch = JOptionPane.showConfirmDialog(null,obj,"ReadHarmonic=N",JOptionPane.OK_OPTION);
	 	    	if(ch==0)
	 	    	{
	 	    	    if(!(InsertNode.nodeExists("Geom")))
	 	    		 {InsertNode.insertNode("Key","Geom");
	 	    		 }
	 	    	    //Delete Existing node and then add it
	 			    if((InsertNode.nodeExists("Step")))
	 			    {
	 	    	    InsertNode.deleteNode("Step=");
	 			    }
	 	    	    InsertNode.insertNode("Geom","ReadHarmonic="+stepField.getText());
	 	    		geoRe = new String();
				     geoRe = stepField.getText();
				     geomOpt.addElement("ReadHarmonic="+geoRe);
	 	    	    
	 	    	    
	 	    	    
	 	    	    
	 	    	}
	 	    				if(ch==1) // pressed no remove step
	 	    				{
	 	    				    if((InsertNode.nodeExists("ReadH")))
	 	    				    {
	 	    				        //Delete Node if exists
	 	    				        InsertNode.deleteNode("ReadH");
	 	    				       geomOpt.removeElement("ReadHarmonic="+geoRe);
	 	    				    }
	 	    				
	 	    				}
	 			}
	 	    	else
	 			{
	 	    		if((InsertNode.nodeExists("ReadHarmonic=")))
	                    InsertNode.deleteNode("ReadHarmonic=");
	 		  			geomOpt.removeElement("ReadHarmonic="+geoRe);    
	 			}
	 	    }
	 			
		if(e.getItem()==GeChk)
	 	{
			G03Listener.geomTabF++;
	 		  	if(((JRadioButton)e.getItem()).isSelected())
	 			{
	 		  		if(geomOptC==0)
	 				{
	 					geomOpt= new Vector<String>(10);
	 					geomOptC++;
	 				}
	 			
	 		  		JTextField stepField;
	 		    	JPanel txt;
	 		    	txt = new JPanel(new GridBagLayout());
	 		    	GridBagConstraints c = new GridBagConstraints();
	 			stepField = new JTextField(3);
	 			stepField.setSize(2,1);
	 			Object[] obj = new Object[3];
	 			obj[0] = "Add harmonic contraints to a structure read in";
	 			obj[1] = " from the input stream with force constant";
	 			JLabel s  = new JLabel(" N/1000 Hartree/Bohr*Bohr with N ="); //+stepField;
	 			c.insets=new Insets(0,0,0,0);
	 			txt.add(s,c);
	 			c.insets = new Insets(0,0,0,30);
	 			txt.add(stepField,c);
	 			obj[2] = txt;
	 			int ch = JOptionPane.showConfirmDialog(null,obj,"CheckHarmonic=N",JOptionPane.OK_OPTION);
	 			
	 	    	if(ch==0)
	 	    	{
	 	    	    if(!(InsertNode.nodeExists("Geom")))
	 	    		 {InsertNode.insertNode("Key","Geom");
	 	    		 }
	 	    	    //Delete Existing node and then add it
	 			    if((InsertNode.nodeExists("Check")))
	 			    {
	 	    	    InsertNode.deleteNode("Check=");
	 			    }
	 	    	    InsertNode.insertNode("Geom","CheckHarmonic="+stepField.getText());
	 	    	     geoCh = new String();
				     geoCh = stepField.getText();
				     geomOpt.addElement("CheckHarmonic="+geoCh);
	 	    	    
	 	    	    
	 	    	    
	 	    	    
	 	    	    
	 	    	    
	 	    	}
	 	    				if(ch==1) // pressed no remove step
	 	    				{
	 	    				    if((InsertNode.nodeExists("CheckHarmonic")))
	 	    				    {
	 	    				        //Delete Node if exists
	 	    				        InsertNode.deleteNode("CheckHarmonic");
	 	    				       geomOpt.removeElement("CheckHarmonic="+geoCh);
	 	    				    }
	 	    				
	 	    				}
	 			}
	 	    	else
	 			{
	 	    	    if((InsertNode.nodeExists("CheckHarmonic")))
	 			    InsertNode.deleteNode("CheckHarmonic");
	 	    	   geomOpt.removeElement("CheckHarmonic="+geoCh);    
	 			}
	 	    }
		
		if(e.getItem()==GeIhar)
	 	{
			G03Listener.geomTabF++;
	 		  	if(((JRadioButton)e.getItem()).isSelected())
	 			{
	 		  		if(geomOptC==0)
	 				{
	 					geomOpt= new Vector<String>(10);
	 					geomOptC++;
	 				}
	 			
	 		  		JTextField stepField;
	 		    	JPanel txt;
	 		    	txt = new JPanel(new GridBagLayout());
	 		    	GridBagConstraints c = new GridBagConstraints();
	 			stepField = new JTextField(3);
	 			stepField.setSize(2,1);
	 			Object[] obj = new Object[3];
	 			obj[0] = "Add harmonic contraints to a structure read in";
	 			obj[1] = " from the input stream with force constant";
	 			JLabel s  = new JLabel(" N/1000 Hartree/Bohr*Bohr with N ="); //+stepField;
	 			c.insets=new Insets(0,0,0,0);
	 			txt.add(s,c);
	 			c.insets = new Insets(0,0,0,30);
	 			txt.add(stepField,c);
	 			obj[2] = txt;
	 			int ch = JOptionPane.showConfirmDialog(null,obj,"InitialHarmonic=N",JOptionPane.OK_OPTION);
	 	    	if(ch==0)
	 	    	{
	 	    	    if(!(InsertNode.nodeExists("Geom")))
	 	    		 {InsertNode.insertNode("Key","Geom");
	 	    		 }
	 	    	    //Delete Existing node and then add it
	 			    if((InsertNode.nodeExists("Initial")))
	 			    {
	 	    	    InsertNode.deleteNode("Initial");
	 			    }
	 	    	    InsertNode.insertNode("Geom","InitialHarmonic="+stepField.getText());
	 	    	   geoIh = new String();
				     geoIh = stepField.getText();
				     geomOpt.addElement("CheckHarmonic="+geoIh);
	 	    	    
	 	    	}
	 	    				if(ch==1) // pressed no remove step
	 	    				{
	 	    				    if((InsertNode.nodeExists("InitialHarmonic")))
	 	    				    {
	 	    				        //Delete Node if exists
	 	    				        InsertNode.deleteNode("InitialHarmonic");
	 	    				       geomOpt.removeElement("CheckHarmonic="+geoIh);
	 	    				    }
	 	    				
	 	    				}
	 			}
	 	    	else
	 			{
	 	    	    if((InsertNode.nodeExists("InitialHarmonic")))
	 			    InsertNode.deleteNode("InitialHarmonic");
	 	    	   geomOpt.removeElement("CheckHarmonic="+geoIh);
	 			}
	 	    }
		 	}
	 public void actionPerformed(ActionEvent e)
	 {  
	 	if(e.getSource()==exitButton)
	 	{
	 		geomOptFrame.dispose();
	 	}
 	    if(e.getSource()==doneButton)
	    {
	    	
	    	System.out.println("The value of freq Flagv = "+geomC);
	    	if(RouteClass.initCount==0)
	    	{
	    		System.out.println(RouteClass.initCount+" \tInside init");
	    	RouteClass.initBuffer();
	    	RouteClass.initCount++;
	    	}
	    	if(geomC==0)
	    	{
	    	System.out.println(geomC+" \t Inside geomC");
	    	geomFlag=RouteClass.keyIndex;
	    	RouteClass.keyIndex++;
	    	geomC++;
	    	}
	    	else
	    	{
	    		//System.out.println("Reinitialize the Geom Buffer");
	    		RouteClass.keywordBuffer[geomFlag]=new StringBuffer();
	    	}
	    	//RouteClass.keywordBuffer[geomFlag].append("Geom");
	 	try {
			if(geomOpt.size()>1)
			{
			RouteClass.keywordBuffer[geomFlag].append("Geom");
			 RouteClass.keywordBuffer[geomFlag].append("=(");
			 for(int count=0;count<geomOpt.size();count++)
			    {
			    	RouteClass.keywordBuffer[geomFlag].append(geomOpt.get(count));
			    	if(!(count==(geomOpt.size()-1)))
			    	{
			    	RouteClass.keywordBuffer[geomFlag].append(",");
			    	}
			    }
			    RouteClass.keywordBuffer[geomFlag].append(")");
			  }
			 else
			 {
			 	if(geomOpt.size()>0)
			 		RouteClass.keywordBuffer[geomFlag].append("Geom");
			 	RouteClass.keywordBuffer[geomFlag].append("="+"("+geomOpt.get(0)+")");
			 }
		} catch (NullPointerException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
	 	 //geomFlag=RouteClass.keyIndex;
	 	 //RouteClass.keyIndex++;
	 	 RouteClass.writeRoute();
	 	 geomOptFrame.dispose();
	    }
	    if(e.getSource()==exitButton)
	    {
	    	geomOptFrame.dispose();
	    }
	  }
	 
	 
	 
	 
	 
		 	}
	 	
	 	

	 	
	 	
	 	
	 
	 
	 
	 
	 
  



