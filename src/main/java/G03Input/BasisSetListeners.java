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
 * Created on Apr 2, 2005
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta @author Sandeep Kumar Seethaapathy 
 * 
 */

package G03Input;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.*;

import java.io.*;
import java.util.Enumeration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.EventListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BasisSetListeners implements ActionListener,ChangeListener{
    
    public static JFrame optionsFrame;
    public static JLabel polLabel,diffLabel;
	public static JTable allDftTable;                        //JTable for List all DFT functionals
	public static JLabel g631Dfn,g631Ffn,g631Pfn,g631Dfn2,placeLabel;
	//public static JTextField g631Text1,g631Text2,g631Text3,g631Text4;
	public static JSpinner g631Text1,g631Text2,g631Text3,g631Text4;
	public static SpinnerNumberModel g631model1,g631model2,g631model3,g631model4;
	//public static JTextField g6311Text1,g6311Text2,g6311Text3,g6311Text4;
	public static SpinnerNumberModel g6311model1,g6311model2,g6311model3,g6311model4;
	public static JSpinner g6311Text1,g6311Text2,g6311Text3,g6311Text4;
	
	public static JSpinner d95Text1,d95Text2,d95Text3,d95Text4;
	public static SpinnerNumberModel d95model1,d95model2,d95model3,d95model4;
	
	public static JRadioButton g321Pol1,g321Pol2,g321Diff,g631d1,g631d2,d95vPol1,d95vPol2,g6311d1,g6311d2,d95d1,d95d2; 
    public static JPanel optPanel,okcancelPanel;
    public static JRadioButton d95vd1,d95vd2;
    public static JButton d95vokBtn,d95vcancelBtn,d95okBtn,d95cancelBtn,g321okBtn,g321cancelBtn,g631okBtn,g631cancelBtn,g6311okBtn,g6311cancelBtn;
    
    
    public static JRadioButton hfsB,lypB,hfbB,pw9B,xalB,p86B,mpwB,plB,g96B,b95B,pbeB,mpbB,mpeB,vwnB,oB,vw5B;
    
    
    public static JButton doneButton,clearButton,exitButton,doneButton1,exitButton1;
    public static JPanel buttonPanel;
    public static JPanel donePanel;
    
  
    
    
    public  void stateChanged(ChangeEvent ce) {
        System.out.println(ce.getSource());
        if(ce.getSource()==g631Text1)
        {
            System.out.println(g631Text1.toString());
            }
	  }
	      
    
    
    public void actionPerformed(ActionEvent e)
    {
        // Action Listeners for the Frequently used Basis-Set Options
        
        //6-31G
        if(e.getSource()==G03MenuTree.f631gItem)
    	{
            int ch;
    		ch = JOptionPane.showConfirmDialog(null,"Add Polarization and/Or Diffuse Functions","6-31G Options",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
    		if(ch==0)
    		{
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			optionsFrame = new JFrame("6-31G Options");
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			g631Dfn = new JLabel("Set(s) of d functions on Heavy Atoms");
    			g631Ffn = new JLabel("Set of f functions on Heavy Atoms");
    			g631Pfn = new JLabel("Set(s) of p functions on H Atoms");
    			g631Dfn2 = new JLabel("Set of d functions on H Atoms");
    			polLabel = new JLabel("Polarization Functions");
    			diffLabel = new JLabel("Diffuse Functions");
    			placeLabel = new JLabel("Place");
    			
    			g631model1 =
    		        new SpinnerNumberModel(0,0,3,1);//initial,min,max,step
    			g631model2 =
    		        new SpinnerNumberModel(0,0,1,1);//initial,min,max,step
    			g631model3 =
    		        new SpinnerNumberModel(0,0,3,1);//initial,min,max,step
    			g631model4 =
    		        new SpinnerNumberModel(0,0,1,1);//initial,min,max,step
    			

    			
    			g631Text1 = new JSpinner(g631model1);
    			g631Text2 = new JSpinner(g631model2);
    			g631Text3 = new JSpinner(g631model3);
    			g631Text4 = new JSpinner(g631model4);
    			
    			 g631Text1.addChangeListener(this);
    			 
    			g631d1 = new JRadioButton("6-31+G");
    			g631d2 = new JRadioButton("6-31++G");
    			ButtonGroup Bg631 = new ButtonGroup();
    			
    			Bg631.add(g631d1);
    			Bg631.add(g631d2);
    			g631okBtn=new JButton("Ok");
    			g631cancelBtn=new JButton("Cancel");
    			
    			optPanel = new JPanel(new GridBagLayout());
    			
    			GridBagConstraints c = new GridBagConstraints();
    		    c.fill = GridBagConstraints.BOTH;
    			
    		    c.gridx = 0;
    			c.weightx=1;
    			c.insets = new Insets(10,20,0,0);
    			optPanel.add(polLabel,c);
    			
    			
    			c.gridy=1;
    			c.insets = new Insets(10,150,0,-10);
    			optPanel.add(placeLabel,c);
    						
    			c.gridx=1;
    			c.insets = new Insets(10,20,0,0);
    			optPanel.add(g631Text1,c);
    			
    			c.gridx=2;
    			optPanel.add(g631Dfn,c);
    			
    			
    			c.gridy=2;c.gridx=1;
    			optPanel.add(g631Text2,c);
    			
    			
    			c.gridx=2;
    			optPanel.add(g631Ffn,c);
    			
    			c.gridy=3;c.gridx=1;
    			optPanel.add(g631Text3,c);
    			
    			
    			c.gridx=2;
    			optPanel.add(g631Pfn,c);
    			
    			
    			c.gridy=4;c.gridx=1;
    			optPanel.add(g631Text4,c);
    			
    			
    			c.gridx=2;
    			optPanel.add(g631Dfn2,c);
    			
    			
    			c.gridy=5;c.gridx=0;
    			optPanel.add(diffLabel,c);
    			
    			
    			c.gridy=6;c.gridx=1;
    			optPanel.add(g631d1,c);
    			c.gridy=7;
    			optPanel.add(g631d2,c);
    			
    			
    			okcancelPanel=new JPanel();
    			okcancelPanel.add(g631okBtn);
    		    okcancelPanel.add(g631cancelBtn);
    		    g631okBtn.addActionListener(this);
    		    g631cancelBtn.addActionListener(this);
    		    
    		    optionsFrame.getContentPane().setLayout(new BorderLayout());
    		    optionsFrame.getContentPane().add(optPanel,BorderLayout.CENTER);
    		    optionsFrame.getContentPane().add(okcancelPanel,BorderLayout.SOUTH);
    		    optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    		
    			
    			
    			optionsFrame.setSize(500,300);
    			optionsFrame.setLocation(200,250);
    			optionsFrame.setVisible(true);
    			optionsFrame.setResizable(false);
    			//optionsFrame.getContentPane().add(optPanel);
    			
    		}
    		
    		if(ch==1)
    		{
    		InsertNode.deleteChildren("Bas");
    		InsertNode.insertNode("Bas","6-31G");    
    		writeBasis("6-31G");
    		}
    		
    	}
        
        // 6-31 G OK Btn ---- Doubt
        if(e.getSource()==BasisSetListeners.g631okBtn)
		{   InsertNode.deleteChildren("Bas");
			String g631String1=""; 
			String g631String2="";
		
			
			if(!(g631model1.getNumber().intValue()==0 && g631model2.getNumber().intValue()==0 ))
			{
			g631String2="(";
			g631String2+=returnPol(g631model1.getNumber().intValue(),g631model2.getNumber().intValue(),0);
			//g631String2+=",";
			g631String2+=returnPol(g631model3.getNumber().intValue(),g631model4.getNumber().intValue(),1);
			g631String2+=")";
			}
			System.out.println(g631String2);
			
			//g631model1
			if(g631d1.isSelected())
			{
				InsertNode.insertNode("Bas",g631d1.getText());
				g631String1=g631d1.getText();
			}
			else if(g631d2.isSelected())
			{
				InsertNode.insertNode("Bas",g631d2.getText());
				g631String1=g631d2.getText();
			}
			else if((!(g631d1.isSelected()))&&
					(!(g631d2.isSelected())))
					{
					InsertNode.insertNode("Bas","6-31G");
					g631String1="6-31G";
					}
			optionsFrame.dispose();
			writeBasis(g631String1+g631String2);
		}
        
        // 6-31 G Cancel Btn
		if(e.getSource()==BasisSetListeners.g631cancelBtn)
		{InsertNode.deleteChildren("Bas");
		InsertNode.insertNode("Bas","6-31G");	
		writeBasis("6-31G");
		optionsFrame.dispose();
		}

        // 6-311G Item
    	if(e.getSource()==G03MenuTree.f6311gItem)
    	{
    		int ch;
    		ch = JOptionPane.showConfirmDialog(null,"Add Polarization and/Or Diffuse Functions","6-311G Options",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
    		if(ch==0)
    		{
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			optionsFrame = new JFrame("6-311G Options");
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			ButtonGroup g6311bg1=new ButtonGroup();
    			
    			g631Dfn = new JLabel("Set(s) of d functions on Heavy Atoms");
    			g631Ffn = new JLabel("Set of f functions on Heavy Atoms");
    			g631Pfn = new JLabel("Set(s) of p functions on H Atoms");
    			g631Dfn2 = new JLabel("Set of d functions on H Atoms");
    			polLabel = new JLabel("Polarization Functions");
    			diffLabel = new JLabel("Diffuse Functions");
    			placeLabel = new JLabel("Place");
    			g6311model1 =
    		        new SpinnerNumberModel(0,0,3,1);//initial,min,max,step
    			g6311model2 =
    		        new SpinnerNumberModel(0,0,1,1);//initial,min,max,step
    			g6311model3 =
    		        new SpinnerNumberModel(0,0,3,1);//initial,min,max,step
    			g6311model4 =
    		        new SpinnerNumberModel(0,0,1,1);//initial,min,max,step
    			

    			
    			g6311Text1 = new JSpinner(g6311model1);
    			g6311Text2 = new JSpinner(g6311model2);
    			g6311Text3 = new JSpinner(g6311model3);
    			g6311Text4 = new JSpinner(g6311model4);
    			
    			g6311d1 = new JRadioButton("6-311+G");
    			g6311d2 = new JRadioButton("6-311++G");
    			
    			g6311bg1.add(g6311d1);
    			g6311bg1.add(g6311d2);
    			
    			g6311okBtn=new JButton("Ok");
    			g6311cancelBtn=new JButton("Cancel");
    			
    			optPanel = new JPanel(new GridBagLayout());
    			
    			GridBagConstraints c = new GridBagConstraints();
    		    c.fill = GridBagConstraints.BOTH;
    			
    		    c.gridx = 0;
    			c.weightx=1;
    			c.insets = new Insets(10,20,0,0);
    			optPanel.add(polLabel,c);
    			
    			
    			c.gridy=1;
    			c.insets = new Insets(10,150,0,-10);
    			optPanel.add(placeLabel,c);
    						
    			c.gridx=1;
    			c.insets = new Insets(10,20,0,0);
    			optPanel.add(g6311Text1,c);
    			
    			c.gridx=2;
    			optPanel.add(g631Dfn,c);
    			
    			
    			c.gridy=2;c.gridx=1;
    			optPanel.add(g6311Text2,c);
    			
    			
    			c.gridx=2;
    			optPanel.add(g631Ffn,c);
    			
    			c.gridy=3;c.gridx=1;
    			optPanel.add(g6311Text3,c);
    			
    			
    			c.gridx=2;
    			optPanel.add(g631Pfn,c);
    			
    			
    			c.gridy=4;c.gridx=1;
    			optPanel.add(g6311Text4,c);
    			
    			
    			c.gridx=2;
    			optPanel.add(g631Dfn2,c);
    			
    			
    			c.gridy=5;c.gridx=0;
    			optPanel.add(diffLabel,c);
    			
    			
    			c.gridy=6;c.gridx=1;
    			optPanel.add(g6311d1,c);
    			c.gridy=7;
    			optPanel.add(g6311d2,c);
    			
    			
    			okcancelPanel=new JPanel();
    			okcancelPanel.add(g6311okBtn);
    		    okcancelPanel.add(g6311cancelBtn);
    		    
    		    g6311okBtn.addActionListener(this);
    		    g6311cancelBtn.addActionListener(this);
    		    
    		    optionsFrame.getContentPane().setLayout(new BorderLayout());
    		    optionsFrame.getContentPane().add(optPanel,BorderLayout.CENTER);
    		    optionsFrame.getContentPane().add(okcancelPanel,BorderLayout.SOUTH);
    		    optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    		
    			
    			
    			optionsFrame.setSize(520,310);
    			optionsFrame.setLocation(200,250);
    			optionsFrame.setVisible(true);
    			optionsFrame.setResizable(false);
    			//optionsFrame.getContentPane().add(optPanel);
    			
    		}
    		if(ch==1)
    		{
    		InsertNode.deleteChildren("Bas");
    		InsertNode.insertNode("Bas","6-311G");    
    		writeBasis("6-311G");
    		}
    	}
    	
    	// 6-311G OK
    	if(e.getSource()==BasisSetListeners.g6311okBtn)
		{
    	    InsertNode.deleteChildren("Bas");
    	    
    	    String g6311String1=""; 
			String g6311String2="";
		
			
			if(!(g6311model1.getNumber().intValue()==0 && g6311model2.getNumber().intValue()==0))
			{
			g6311String2="(";
			g6311String2+=returnPol(g6311model1.getNumber().intValue(),g6311model2.getNumber().intValue(),0);
			//g6311String2+=",";
			g6311String2+=returnPol(g6311model3.getNumber().intValue(),g6311model4.getNumber().intValue(),1);
			g6311String2+=")";
			}
			System.out.println(g6311String2);
			if(g6311d1.isSelected())
			{
				InsertNode.insertNode("Bas",g6311d1.getText());
				g6311String1=g6311d1.getText();
				
			}
			else if(g6311d2.isSelected())
			{
				InsertNode.insertNode("Bas",g6311d2.getText());
				g6311String1=g6311d2.getText();
			}
			else if((!(g6311d1.isSelected()))&&
					(!(g6311d2.isSelected())))
					{
					InsertNode.insertNode("Bas","6-311G");
					g6311String1="6-311G";
					}
			optionsFrame.dispose();
			writeBasis(g6311String1+g6311String2);
		}
    	
    	//  6-311G CAncel
    	if(e.getSource()==BasisSetListeners.g6311cancelBtn)
		{   // same as NO
    	    InsertNode.deleteChildren("Bas");
    	    InsertNode.insertNode("Bas","6-311G");
    	    writeBasis("6-311G");
    	    optionsFrame.dispose();
		}
		
    	
    	// 3-21G OK
    	if(e.getSource()==g321okBtn)
    	{
    	    InsertNode.deleteChildren("Bas");
    	if(g321Pol1.isSelected())    
    	    {        G03Input.InsertNode.insertNode("Ba",g321Pol1.getText());
    	    }
    	if(g321Pol2.isSelected())
    	   {        G03Input.InsertNode.insertNode("Ba",g321Pol2.getText());
    	    }
    	if(g321Diff.isSelected())
    	    {        G03Input.InsertNode.insertNode("Ba",g321Diff.getText());
    	     }
    	if(!(g321Diff.isSelected())&& (!(g321Pol2.isSelected()))
    	&& (!(g321Pol1.isSelected()))        
    	)
    	{
    	// none selected .. just add 3-21G
    	    InsertNode.insertNode("Ba","3-21G");
    	
    	}
    	
    	optionsFrame.dispose();
      	
    	}
    	
    	// 3-21 G
    	
    	if(e.getSource()==G03MenuTree.f321Menu)
    	{
    		int ch;
    		ch = JOptionPane.showConfirmDialog(null,"Add Polarization and/Or Diffuse Functions","3-21G Options",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
    		if(ch==0)
    		{
    		    ButtonGroup group321 = new ButtonGroup();
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			optionsFrame = new JFrame("3-21G Options");
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			polLabel = new JLabel("Polarization Functions:");
    			diffLabel = new JLabel("Diffuse Functions:");
    			g321Pol1 = new JRadioButton("3-21G*");
    			g321Pol2 = new JRadioButton("3-21G**(3-21G(d,p))");
    			g321Diff = new JRadioButton("3-21+G");
    			
    			group321.add(g321Pol1);
    			group321.add(g321Pol2);
    			
    			optionsFrame.setSize(500,160);
    			optionsFrame.setLocation(200,250);
    			optionsFrame.setVisible(true);
    			optionsFrame.setResizable(false);
    		    optPanel = new JPanel(new GridBagLayout());
    		    GridBagConstraints c = new GridBagConstraints();
    		    c.fill = GridBagConstraints.BOTH;
    		    c.gridx=0;
    		    c.weightx=1;
    		    c.weighty=1;
    		    c.insets=new Insets(10,50,10,20);
    		    optPanel.add(polLabel,c);
    		    c.gridx=1;
    		    c.insets = new Insets(10,-100,10,20);
    		    optPanel.add(g321Pol1,c);
    		   //c.insets=new Insets(10,50,10,20);
    		    c.gridy=1;c.gridx=1;
    		    optPanel.add(g321Pol2,c);
    		    c.gridx=0;c.gridy=2;
    		    c.insets=new Insets(10,50,10,20);
    		    optPanel.add(diffLabel,c);
    		    c.gridx=1;
    		    c.insets = new Insets(10,-100,10,20);
    		    optPanel.add(g321Diff,c);
    		    okcancelPanel = new JPanel();
    		    
    		    g321okBtn = new JButton("OK");
    		    g321okBtn.addActionListener(this);
    		    g321cancelBtn = new JButton("CANCEL");
    		    g321cancelBtn.addActionListener(this);
    		    
    		    okcancelPanel.add(g321okBtn);
    		    okcancelPanel.add(g321cancelBtn);
    		    optionsFrame.getContentPane().setLayout(new BorderLayout());
    		    optionsFrame.getContentPane().add(optPanel,BorderLayout.CENTER);
    		    optionsFrame.getContentPane().add(okcancelPanel,BorderLayout.SOUTH);
    		    optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    		}
    		
    		if(ch==1)
    		{   InsertNode.deleteChildren("Bas");
    		    InsertNode.insertNode("Bas",e.getActionCommand());
    		}
       	}
    	 
    	
    	
    	
    	//3-21G Cancel
    	if(e.getSource()==g321cancelBtn)
    	{    InsertNode.deleteChildren("Bas");
    	    InsertNode.insertNode("Ba","3-21G");
    	    optionsFrame.dispose();
    	}
    	  	
    	
    	if((e.getSource()==G03MenuTree.fsto3gItem)||
    			//(e.getSource()==G03MenuTree.f321MenuItem)||
    			(e.getSource()==G03MenuTree.fsto3g1Item)||
    			(e.getSource()==G03MenuTree.f621gItem)||
    			(e.getSource()==G03MenuTree.f621gdItem)||
				(e.getSource()==G03MenuTree.f431gItem)||
				(e.getSource()==G03MenuTree.f431g1Item)||
				(e.getSource()==G03MenuTree.f431g2Item)||
				(e.getSource()==G03MenuTree.fccpvdItem)||
				(e.getSource()==G03MenuTree.fccpvtItem)||
				(e.getSource()==G03MenuTree.fccpvqItem)||
				(e.getSource()==G03MenuTree.faugccdItem)||
				(e.getSource()==G03MenuTree.faugcctItem)||
				(e.getSource()==G03MenuTree.faugccqItem)||
				(e.getSource()==G03MenuTree.fccpv5Item)||
				(e.getSource()==G03MenuTree.fccpv6Item)||
				(e.getSource()==G03MenuTree.faugcc5Item)||
				(e.getSource()==G03MenuTree.faugcc6Item)||
				(e.getSource()==G03MenuTree.flanl2Item)||
				(e.getSource()==G03MenuTree.f321gItem)||
				(e.getSource()==G03MenuTree.f321g1Item)||
				(e.getSource()==G03MenuTree.f321pgItem)||
				(e.getSource()==G03MenuTree.f321pg1Item)||
				(e.getSource()==G03MenuTree.f321g11Item)||
				(e.getSource()==G03MenuTree.f321pg11Item))
    			{
    	    InsertNode.deleteChildren("Bas");
    				InsertNode.insertNode("Bas",e.getActionCommand());
    				writeBasis(e.getActionCommand());
    			}
    			
    		   		
    		
    	
    	/* Action Listeners for the Additional Basis-Set Options */

        // Petersson et al. Complete Basis Set method basis sets.
        // These can have diffuse and polarization functions.
        // There appears to be no general method for handling diffuse and
        // polarization functions; so these are ignored for now.  S. Brozell Nov 2006
    	if(e.getSource()==G03MenuTree.acbs631gdaggerItem)
    	{
    	    InsertNode.deleteChildren("Bas");
    	    InsertNode.insertNode( "Bas", "6-31G(d')" );
    	    writeBasis( "6-31G(d')" );
    	}
    	if(e.getSource()==G03MenuTree.acbs631gdaggerdaggerItem)
    	{
    	    InsertNode.deleteChildren("Bas");
    	    InsertNode.insertNode( "Bas", "6-31G(d',p')" );
    	    writeBasis( "6-31G(d',p')" );
    	}


    	if(e.getSource()==G03MenuTree.ad95Item)
    	{
    		int ch;
    		ch = JOptionPane.showConfirmDialog(null,"Add Polarization and/Or Diffuse Functions","D95 Options",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
    		if(ch==0)
    		{
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			optionsFrame = new JFrame("D95 Options");
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			g631Dfn = new JLabel("Set(s) of d functions on Heavy Atoms");
    			g631Ffn = new JLabel("Set of f functions on Heavy Atoms");
    			g631Pfn = new JLabel("Set(s) of p functions on H Atoms");
    			g631Dfn2 = new JLabel("Set of d functions on H Atoms");
    			polLabel = new JLabel("Polarization Functions");
    			diffLabel = new JLabel("Diffuse Functions");
    			placeLabel = new JLabel("Place");
    			
    			d95model1 =
    		        new SpinnerNumberModel(0,0,3,1);//initial,min,max,step
    			d95model2 =
    		        new SpinnerNumberModel(0,0,1,1);//initial,min,max,step
    			d95model3 =
    		        new SpinnerNumberModel(0,0,3,1);//initial,min,max,step
    			d95model4 =
    		        new SpinnerNumberModel(0,0,1,1);//initial,min,max,step
    			

    			
    			d95Text1 = new JSpinner(d95model1);
    			d95Text2 = new JSpinner(d95model2);
    			d95Text3 = new JSpinner(d95model3);
    			d95Text4 = new JSpinner(d95model4);
    			
    			
    			d95d1 = new JRadioButton("D95+");
    			d95d2 = new JRadioButton("D95++");
    			ButtonGroup Bd95 = new ButtonGroup();
    			
    			Bd95.add(d95d1);
    			Bd95.add(d95d2);
    			
    			d95okBtn=new JButton("Ok");
    			d95cancelBtn=new JButton("Cancel");
    			
    			optPanel = new JPanel(new GridBagLayout());
    			
    			GridBagConstraints c = new GridBagConstraints();
    		    c.fill = GridBagConstraints.BOTH;
    			
    		    c.gridx = 0;
    			c.weightx=1;
    			c.insets = new Insets(10,20,0,0);
    			optPanel.add(polLabel,c);
    			
    			
    			c.gridy=1;
    			c.insets = new Insets(10,150,0,-10);
    			optPanel.add(placeLabel,c);
    						
    			c.gridx=1;
    			c.insets = new Insets(10,20,0,0);
    			optPanel.add(d95Text1,c);
    			
    			c.gridx=2;
    			optPanel.add(g631Dfn,c);
    			
    			
    			c.gridy=2;c.gridx=1;
    			optPanel.add(d95Text2,c);
    			
    			
    			c.gridx=2;
    			optPanel.add(g631Ffn,c);
    			
    			c.gridy=3;c.gridx=1;
    			optPanel.add(d95Text3,c);
    			
    			
    			c.gridx=2;
    			optPanel.add(g631Pfn,c);
    			
    			
    			c.gridy=4;c.gridx=1;
    			optPanel.add(d95Text4,c);
    			
    			
    			c.gridx=2;
    			optPanel.add(g631Dfn2,c);
    			
    			
    			c.gridy=5;c.gridx=0;
    			optPanel.add(diffLabel,c);
    			
    			
    			c.gridy=6;c.gridx=1;
    			optPanel.add(d95d1,c);
    			c.gridy=7;
    			optPanel.add(d95d2,c);
    			
    			
    			okcancelPanel=new JPanel();
    			okcancelPanel.add(d95okBtn);
    		    okcancelPanel.add(d95cancelBtn);
    		    
    		    d95okBtn.addActionListener(this);
    		    d95cancelBtn.addActionListener(this);
    		    optionsFrame.getContentPane().setLayout(new BorderLayout());
    		    optionsFrame.getContentPane().add(optPanel,BorderLayout.CENTER);
    		    optionsFrame.getContentPane().add(okcancelPanel,BorderLayout.SOUTH);
    		    optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    		
    			
    			
    			optionsFrame.setSize(520,310);
    			optionsFrame.setLocation(200,250);
    			optionsFrame.setVisible(true);
    			optionsFrame.setResizable(false);
    			//optionsFrame.getContentPane().add(optPanel);
    			
    		}
    		if(ch==1) // Pressed No
    		{
    		 //Add  D95 to the tree 
    		InsertNode.deleteChildren("Bas");
    		InsertNode.insertNode("Bas","D95");
    		writeBasis("D95");
    		}
    	}
    	
    	
    	if(e.getSource()==d95okBtn)
    	{
    	    // Add every thing that has been selected
    	    InsertNode.deleteChildren("Bas");
    		String d95String1=""; 
			String d95String2="";
			
			if(!(d95model1.getNumber().intValue()==0 && d95model2.getNumber().intValue()==0))
			{
			d95String2="(";
			

d95String2+=returnPol(d95model1.getNumber().intValue(),d95model2.getNumber().intValue(),0);
			//d95String2+=",";
			

d95String2+=returnPol(d95model3.getNumber().intValue(),d95model4.getNumber().intValue(),1);
			d95String2+=")";
			}
			System.out.println(d95String2);
			
    	    
    	    if(d95d1.isSelected()) //D95+
    	    {
    	        
    	        InsertNode.insertNode("Bas",d95d1.getText());
    	        d95String1=d95d1.getText();
    	    }
    	   else if(d95d2.isSelected()) //D95++
    	    {
    	        InsertNode.insertNode("Bas",d95d2.getText());
    	        d95String1=d95d2.getText();
    	    }
    	    else if((!(d95d1.isSelected())) &&
    	            (!(d95d2.isSelected())))
    	    {
    	     // Just Pressed Ok without selecting
    	        //Same as Cancel Or "No"
    	        InsertNode.insertNode("Bas","D95");
    	        d95String1="D95";
    	    }
    	    writeBasis(d95String1+d95String2);
    	    optionsFrame.dispose();
    	}
    	
    	if(e.getSource()==d95cancelBtn)
    	{
    	    // Same as "No"
    	    InsertNode.deleteChildren("Bas");
    	    InsertNode.insertNode("Bas","D95");
    	    writeBasis("D95");
    	    optionsFrame.dispose();
    	}
    	
    	
    	/*if(e.getSource()==G03MenuTree.ad95vMenu)
    	{
    		int ch;
    		ch = JOptionPane.showConfirmDialog(null,"Add Polarization and/Or Diffuse Functions","D95V Options",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
    		if(ch==0)
    		{
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			optionsFrame = new JFrame("D95V Options");
    			JFrame.setDefaultLookAndFeelDecorated(true);
    			polLabel = new JLabel("Polarization Functions:");
    			diffLabel = new JLabel("Diffuse Functions:");
    			ButtonGroup d95vGroup = new ButtonGroup();
    			d95vPol1 = new JRadioButton("D95V(d)");
    			d95vPol2 = new JRadioButton("D95V(d,p)");
    			d95vd1 = new JRadioButton("D95V+");
    			//d95vd2 = new JRadioButton("D95V++");
    			
    			d95vGroup.add(d95vPol1);
    			d95vGroup.add(d95vPol2);
    			
    			d95vokBtn=new JButton("Ok");
    			d95vcancelBtn=new JButton("Cancel");
    			optionsFrame.setSize(500,160);
    			optionsFrame.setLocation(200,250);
    			optionsFrame.setVisible(true);
    			optionsFrame.setResizable(false);
    		    
    			optPanel = new JPanel(new GridBagLayout());
    		    GridBagConstraints c = new GridBagConstraints();
    		    c.fill = GridBagConstraints.BOTH;
    		    
    		    c.gridx=0;
    		    c.weightx=1;
    		    c.weighty=1;
    		    c.insets=new Insets(10,50,10,20);
    		    optPanel.add(polLabel,c);
    		    c.gridx=1;
    		    c.insets = new Insets(10,-100,10,20);
    		    optPanel.add(d95vPol1,c);
    		    c.gridy=1;c.gridx=1;
    		    optPanel.add(d95vPol2,c);
    		    c.gridx=0;c.gridy=2;
    		    c.insets=new Insets(10,50,10,20);
    		    optPanel.add(diffLabel,c);
    		    c.gridx=1;
    		    c.insets = new Insets(10,-100,10,20);
    		    optPanel.add(d95vd1,c);
    		    c.gridx=1;
    		    c.gridy=3;
    		    optPanel.add(d95vd2,c);
    		    
    		    okcancelPanel=new JPanel();
    		    okcancelPanel.add(d95vokBtn);
    		    okcancelPanel.add(d95vcancelBtn);
    		    
    		    d95vokBtn.addActionListener(this);
    		    d95vcancelBtn.addActionListener(this);
    		    
    		    optionsFrame.getContentPane().setLayout(new BorderLayout());
    		    optionsFrame.getContentPane().add(optPanel,BorderLayout.CENTER);
    		    optionsFrame.getContentPane().add(okcancelPanel,BorderLayout.SOUTH);
    		    optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    		}
    		if(ch==1) // Pressed No
    		{
    		 //Add  D95V to the tree 
    		    InsertNode.deleteChildren("Bas");
    		InsertNode.insertNode("Bas","D95V");
    		}
       	}
    	*/
    	if(e.getSource()==d95vokBtn)
    	{
    	    InsertNode.deleteChildren("Bas");
    	    // Add every thing that has been selected
    	    if(d95vd1.isSelected()) //D95V+
    	    {
    	        InsertNode.insertNode("Bas",d95vd1.getText());
    	        writeBasis(d95vd1.getText());
    	    }
    	    
    	    if(d95vPol1.isSelected()) //D95(d..)
    	    {
    	        InsertNode.insertNode("Bas",d95vPol1.getText());
    	        writeBasis(d95vPol1.getText());
    	    }
    	    if(d95vPol2.isSelected()) //D95(d..)
    	    {
    	        InsertNode.insertNode("Bas",d95vPol2.getText());
    	        writeBasis(d95vPol2.getText());
    	    }
    	    if((!(d95vd1.isSelected())) &&
    	            (!(d95vPol1.isSelected())) &&
    	            (!(d95vPol2.isSelected()))        
    	    )
    	    {
    	     // Just Pressed Ok without selecting
    	        //Same as Cancel Or "No"
    	        InsertNode.insertNode("Bas","D95V");
    	        writeBasis("D95V");
    	    }
    	    
    	    optionsFrame.dispose();
    	 	
    	}
 	
    	if(e.getSource()==d95vcancelBtn)
    	{
    	    // Same as "No"
    	    InsertNode.deleteChildren("Bas");
    	    InsertNode.insertNode("Bas","D95V");
    	    writeBasis("D95V");
    	    optionsFrame.dispose();
    	}
    	
    	
    	
    	if(
    	        e.getSource()==G03MenuTree.adgItem ||
    	        e.getSource()==G03MenuTree.adg2Item ||
    	        e.getSource()==G03MenuTree.adgtItem ||
    	        e.getSource()==G03MenuTree.aeprItem ||
    	        e.getSource()==G03MenuTree.aepr2Item ||
    	        e.getSource()==G03MenuTree.alanItem ||
    	        e.getSource()==G03MenuTree.amidiItem ||
    	        e.getSource()==G03MenuTree.amtItem ||
    	        e.getSource()==G03MenuTree.asddItem ||
    	        e.getSource()==G03MenuTree.asdd2Item ||
    	        e.getSource()==G03MenuTree.acep4gItem ||
    	        e.getSource()==G03MenuTree.acep4g1Item ||
    	        e.getSource()==G03MenuTree.acep31gItem ||
    	        e.getSource()==G03MenuTree.acep31g1Item ||
    	        e.getSource()==G03MenuTree.acep121gItem ||
    	        e.getSource()==G03MenuTree.acep121g1Item ||
    	        e.getSource()==G03MenuTree.ashcItem ||
    	        e.getSource()==G03MenuTree.ashc1Item ||
    	        e.getSource()==G03MenuTree.augbsItem ||
    	        e.getSource()==G03MenuTree.augbs1Item ||
    	        e.getSource()==G03MenuTree.augbs2Item ||
    	        e.getSource()==G03MenuTree.augbs3Item ||
    	        e.getSource()==G03MenuTree.ad95v11Item ||
    	        e.getSource()==G03MenuTree.ad95v1Item ||
    	        e.getSource()==G03MenuTree.ad95vp11Item ||
    	        e.getSource()==G03MenuTree.ad95vp1Item ||
    	        e.getSource()==G03MenuTree.ad95vpItem ||
    	        e.getSource()==G03MenuTree.ad95vpp11Item ||
    	        e.getSource()==G03MenuTree.ad95vpp1Item ||
    	        e.getSource()==G03MenuTree.ad95vppItem )
    	    
    	{
    	    InsertNode.deleteChildren("Bas");
    	    InsertNode.insertNode("Ba",e.getActionCommand());
    	    writeBasis(e.getActionCommand());
    	}
    
    	
    
    }
    
    
    public static void writeBasis(String basisStr)
    {
         RouteClass.basisBuffer = new StringBuffer();
     	 RouteClass.basisBuffer.append(basisStr);
     	 RouteClass.writeRoute();
    
    }
    
    public static String returnPol(int a,int b,int type)
    {
        String str="";
        if(type==0)
        {
            if(a==1)
                str="d";
            else if(a>1)
                str=a+"d";
            
            if(b!=0)
                str+="f";
            
        }   
        if(type==1)
        {   
            if(a==1)
                str=",p";
            else if(a>1)
                str=","+a+"p";
            
            if(b!=0)
                str+="d";
            
        }   
        
        
    return str;
    }
    
}
