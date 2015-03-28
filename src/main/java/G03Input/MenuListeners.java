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
 * Created on Mar 9, 2005
 * @author Michael Sheetz 
 * @author Sandeep Kumar Seethaapathy @author Shashank Jeedigunta   
 * 
 */

package G03Input;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Position;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import java.io.*;
import java.util.Enumeration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.awt.*;

public class MenuListeners extends JFrame implements ActionListener{
    public static JFrame optionsFrame;
	public static JLabel polLabel,diffLabel;
	public static int denC,denFlag;                            //Keeping track of the Density keyowrd
	public static JTable allDftTable;                        //JTable for List all DFT functionals
	public static JLabel g631Dfn,g631Ffn,g631Pfn,g631Dfn2,placeLabel;
	public static JTextField g631Text1,g631Text2,g631Text3,g631Text4;
	public static JTextField g6311Text1,g6311Text2,g6311Text3,g6311Text4;
	public static JTextField d95Text1,d95Text2,d95Text3,d95Text4;
	public static JRadioButton g321Pol1,g321Pol2,g321Diff,g631d1,g631d2,d95vPol1,d95vPol2,g6311d1,g6311d2,d95d1,d95d2; 
    public static JPanel optPanel,okcancelPanel;
    public static JRadioButton d95vd1,d95vd2;
    public static JButton d95vokBtn,d95vcancelBtn,d95okBtn,d95cancelBtn,g321okBtn,g321cancelBtn,g631okBtn,g631cancelBtn,g6311okBtn,g6311cancelBtn;
    public static String optfreq="";
    public static int optfreqflag=0,optircmaxflag=0,optpolarflag=0;
    
    public static JRadioButton hfsB,lypB,hfbB,pw9B,xalB,p86B,mpwB,plB,g96B,b95B,pbeB,mpbB,mpeB,vwnB,oB,vw5B;
    
  
    /* 
    public static JRadioButton hfsB=new JRadioButton("HFS(S)");
	
    public static JRadioButton lypB=new JRadioButton("LYP");
	
    public static JRadioButton hfbB= new JRadioButton("HFB(B)");
	
    public static JRadioButton pw9B=new JRadioButton("PW91");
	
    public static JRadioButton xalB= new JRadioButton("Xalpha(XA)");
	
    public static JRadioButton p86B= new JRadioButton("P86");
	
    public static JRadioButton mpwB= new JRadioButton("MPW");
	
    public static JRadioButton plB= new JRadioButton("PL");
	
    public static JRadioButton g96B= new JRadioButton("G96");
	
    public static JRadioButton b95B= new JRadioButton("B95");
	
    public static JRadioButton pbeB = new JRadioButton("PBE");
	
    public static JRadioButton mpbB = new JRadioButton("MPBE");
	
    public static JRadioButton mpeB = new JRadioButton("MPBE");
	
    public static JRadioButton vwnB= new JRadioButton("VWN");
	
    public static JRadioButton oB= new JRadioButton("O");
	
    public static JRadioButton vw5B= new JRadioButton("VWN5");
    
    public static ButtonGroup exchngGroup =new ButtonGroup();
    public static ButtonGroup corrGroup =new ButtonGroup();
	*/
	//corrGroup.add(hfsB);
	/*corrGroup.add(hfbB);
	corrGroup.add(xalB);
	corrGroup.add(mpwB);
	corrGroup.add(g96B);
	corrGroup.add(pbeB);
	corrGroup.add(mpeB);
	corrGroup.add(oB);

	exchngGroup.add(lypB);
	exchngGroup.add(pw9B);
	exchngGroup.add(p86B);
	exchngGroup.add(plB);
	exchngGroup.add(b95B);
	exchngGroup.add(mpbB);
	exchngGroup.add(vwnB);
	exchngGroup.add(vw5B);
    
    */
    
    
    
    
    
    
    
    
    
    
  
    
    public static JButton dftdoneButton,clearButton,exitButton,doneButton1,exitButton1;
    public static JPanel buttonPanel;
    public static JPanel donePanel;
    public static String jDenHelp  = "The Selected option for his Keyword specify which density to use for population and other "+
    "analysis procedures (default option is to use the density matrix for the current method(DENSITY=CURRENT)"+
	"when no option has been specified";
    
    public static String jFreqHelp = "Help for Keyword \"Freq:\" \n Performs a frequency and thermochemical Analysis."+
	"\n Frequencies must be computed only for an optimized geometry and the method used with Freq must be the same method"+
	"\n used for to compute this optimized geometry(see optFreq).\n"+
	"Frequencies that have been determined analytically (the default for RHF ,UHF ,MP2 ,CIS ,CASSCF, and all DFT methods)" +
	"may not be physically meaningful if the HF and DFT wave functions can be tested using the keyword \"Stable\" "; 
    
    public static String jGuessHelp = "Controls the initial guess for the Hartree-Fock wae function." +
	"An option must be selected in order for this keyword to be maningful.The default option is Huckel whn atoms heavier"+
	"than Xe are present, otherwise the default is Harris";
    
    public static String jOptHelp= "Performs a Geometry optimization";
    public static String jOptFreqHelp = "Performs an optimized geometry and then automatically computes frequecies on "+
	"frequencies on this geometry using the same method used for the geometry optimization";
    
    public static String jSpHelp  = "Computes Single point energy(default when no job type keyword has been specified)";
    
    public static JTable dftTable;
    public static JFrame dftFrame;
    
    public static Object[][] dftListAll = 
    {
    		{"HFS(S)","LYP"},{"HFB(B)","PW91"},{"Xalpha(XA)","P86"},{"MPW","PL"},{"G96","B95"},{"PBE","MPBE"},{"MPBE","VWN"},{"O","VWN5"}
    };
    public static String[] dftListHeader= {"Exchange Functionals","Correlation Functionals"};
    
    public void actionPerformed(ActionEvent e)
    {   
        if(e.getSource()==G03MenuTree.cartItem)
    	{   
            new showMolEditor();
            
    	}
               
    	if(e.getSource()==G03MenuTree.dListAllFunc)
    	{   
    		   	    String[] exchStr={"HFS(S)","HFB(B)","Xalpha(XA)","MPW","G96","PBE","MPBE","O"};
    		   	    String[] corrStr={"LYP","PW91","P86","PL","B95","MPBE","VWN","VWN5"};
    		   	     System.out.println("DLIST COUNT"+MenuMethodListener.count);
    		   	 if(MenuMethodListener.count==0)
    		   	 {
    		   	 MenuMethodListener.createHTable();
    		   	
    		   	 }  	    
    		   	
    	    hfsB=new JRadioButton("HFS(S)");
    	    hfsB.addItemListener(new MenuMethodListener());
    		hfsB.setBackground(Color.WHITE);
    		lypB=new JRadioButton("LYP");
    		lypB.addItemListener(new MenuMethodListener());
    		lypB.setBackground(Color.WHITE);
    		hfbB= new JRadioButton("HFB(B)");
    		hfbB.addItemListener(new MenuMethodListener());
    		hfbB.setBackground(Color.WHITE);
    		pw9B=new JRadioButton("PW91");
			pw9B.addItemListener(new MenuMethodListener());
			pw9B.setBackground(Color.WHITE);
    		xalB= new JRadioButton("Xalpha(XA)");
    		xalB.addItemListener(new MenuMethodListener());
    		xalB.setBackground(Color.WHITE);
			p86B= new JRadioButton("P86");
			p86B.addItemListener(new MenuMethodListener());
			p86B.setBackground(Color.WHITE);
			mpwB= new JRadioButton("MPW");
			mpwB.addItemListener(new MenuMethodListener());
			mpwB.setBackground(Color.WHITE);
			plB= new JRadioButton("PL");
			plB.addItemListener(new MenuMethodListener());
			plB.setBackground(Color.WHITE);
			g96B= new JRadioButton("G96");
			g96B.addItemListener(new MenuMethodListener());
			g96B.setBackground(Color.WHITE);
			b95B= new JRadioButton("B95");
			b95B.addItemListener(new MenuMethodListener());
			b95B.setBackground(Color.WHITE);
			pbeB = new JRadioButton("PBE");
			pbeB.addItemListener(new MenuMethodListener());
			pbeB.setBackground(Color.WHITE);
			mpbB = new JRadioButton("MPBE");
			mpbB.addItemListener(new MenuMethodListener());
			mpbB.setBackground(Color.WHITE);
			mpeB = new JRadioButton("MPBE");
			mpeB.addItemListener(new MenuMethodListener());
			mpeB.setBackground(Color.WHITE);
			vwnB= new JRadioButton("VWN");
			vwnB.addItemListener(new MenuMethodListener());
			vwnB.setBackground(Color.WHITE);
			oB= new JRadioButton("O");
			oB.addItemListener(new MenuMethodListener());
			oB.setBackground(Color.WHITE);
			vw5B= new JRadioButton("VWN5");
			vw5B.addItemListener(new MenuMethodListener());
    		vw5B.setBackground(Color.WHITE);
    		
    		if(MenuMethodListener.count>0)
    		{
    		    System.out.println("HDFDFSDSGF");
    		    System.out.println(MenuMethodListener.exchng);
    		    MenuMethodListener.exchngFlag=1;
    		    MenuMethodListener.corrFlag=1;
    		    
    		    System.out.println("FLAG VAL"+MenuMethodListener.exchngFlag);
    		    if(MenuMethodListener.exchng.get("HFS(S)").toString().equals("1"))
    		    {
    		        System.out.println(":D");
    		        hfsB.setSelected(true);
    		    }
    		    else if(MenuMethodListener.exchng.get("HFB(B)").equals(new Integer(1)))
    		        hfbB.setSelected(true);
    		    else if(MenuMethodListener.exchng.get("Xalpha(XA)").equals(new Integer(1)))
    		        xalB.setSelected(true);
    		    else if(MenuMethodListener.exchng.get("MPW").equals(new Integer(1)))
    		        mpwB.setSelected(true);
    		    else if(MenuMethodListener.exchng.get("G96").equals(new Integer(1)))
    		        g96B.setSelected(true);
    		    else if(MenuMethodListener.exchng.get("PBE").equals(new Integer(1)))
    		        pbeB.setSelected(true);
    		    else if(MenuMethodListener.exchng.get("MPBE").equals(new Integer(1)))
    		        mpeB.setSelected(true);
    		    else if(MenuMethodListener.exchng.get("O").equals(new Integer(1)))
    		        oB.setSelected(true);
    		    
    		    if(MenuMethodListener.corr.get("LYP").equals(new Integer(1)))
    		        lypB.setSelected(true);
    		    if(MenuMethodListener.corr.get("PW91").equals(new Integer(1)))
    		        pw9B.setSelected(true);
    		    if(MenuMethodListener.corr.get("P86").equals(new Integer(1)))
    		        p86B.setSelected(true);
    		    if(MenuMethodListener.corr.get("PL").equals(new Integer(1)))
    		        plB.setSelected(true);
    		    if(MenuMethodListener.corr.get("B95").equals(new Integer(1)))
    		        b95B.setSelected(true);
    		    if(MenuMethodListener.corr.get("MPBE").equals(new Integer(1)))
    		        mpbB.setSelected(true);
    		    if(MenuMethodListener.corr.get("VWN").equals(new Integer(1)))
    		        vwnB.setSelected(true);
    		    if(MenuMethodListener.corr.get("VWN5").equals(new Integer(1)))
    		        vw5B.setSelected(true);
    		    
    		    
    		}
    		
    		
    		
    		
    		
    		
    		JFrame.setDefaultLookAndFeelDecorated(true);
			dftFrame = new JFrame("DFT Functionals");
			 DefaultTableModel dftModel = new DefaultTableModel();
			      dftModel.setDataVector(
			      new Object[][]{
			        {hfsB,lypB},
			        {hfbB,pw9B},
			        {xalB,p86B},
			        {mpwB,plB},
			        {g96B,b95B},
			        {pbeB,mpbB},
			        {mpeB,vwnB},
			        {oB,vw5B}},
					new Object[]{"Exchange Functionals","Correlation Functionals"});
			        
		    		 
			     allDftTable = new JTable(dftModel) {
			      public void tableChanged(TableModelEvent e) {
			        
			        super.tableChanged(e);
			        repaint();
			        
			      }
			   		     
			     
			     
			     };
			  
			    allDftTable.getColumn("Exchange Functionals").setCellRenderer(new RadioButtonRenderer());
			    allDftTable.getColumn("Exchange Functionals").setCellEditor(new RadioButtonEditor(new JCheckBox()));
			    
			    allDftTable.getColumn("Correlation Functionals").setCellRenderer(new RadioButtonRenderer());
			    allDftTable.getColumn("Correlation Functionals").setCellEditor(new RadioButtonEditor(new JCheckBox()));
			    
			    
	    		
	    		
			    donePanel = new JPanel();
			    dftdoneButton = new JButton("Done");
			    dftdoneButton.addActionListener(new MenuMethodListener());
			    exitButton = new JButton("Exit");
			    		    
			    donePanel.add(dftdoneButton);
			
			    donePanel.add(exitButton);
			    allDftTable.getTableHeader().setReorderingAllowed(false);
			    dftFrame.getContentPane().setLayout(new BorderLayout());
			    JScrollPane scroll = new JScrollPane(allDftTable);
			    dftFrame.getContentPane().add( scroll,BorderLayout.CENTER);
			    dftFrame.getContentPane().add(donePanel,BorderLayout.SOUTH);
			    dftFrame.setSize(450,200);
			    dftFrame.setLocation(200,260);
			    dftFrame.setResizable(false);
			    dftFrame.setVisible(true);
			  }
			
			
			
    	
    	if(e.getSource() == G03MenuTree.jcDensity)
    	{   
    		System.out.println("Hi");
    		
    		String str = new String() ;//\\"\n";
    		int oldlen=0;//str.length();
            str+=jDenHelp;
            int newlen=str.length();
            System.out.println(newlen);
        /* //   StyledDocument doc =G03MenuTree.keyoptArea.getStyledDocument();
            
        //    Style style = G03MenuTree.keyoptArea.addStyle("Blue", null);
            StyleConstants.setForeground(style, Color.BLUE);
            StyleConstants.setFontFamily(style,"Arial");
            StyleConstants.setFontSize(style,12);
            G03MenuTree.keyoptArea.setText(str);
      //      doc.setParagraphAttributes(oldlen,newlen-oldlen,G03MenuTree.keyoptArea.getStyle("Blue"), true);
          */
            G03MenuTree.keyoptArea.setEditable(false);
            
    	}
    	
    	if(e.getSource() == G03MenuTree.jcFreq)
    	{   
    
                new FreqOptTable();
            
            
            
    	}
    	if(e.getSource() == G03MenuTree.jcGuess)
    	{   
    		/* System.out.println("Hi");
    		
    		String str = new String() ;//\\"\n";
    		int oldlen=0;//str.length();
            str+=jGuessHelp;
            int newlen=str.length();
            System.out.println(newlen);
            StyledDocument doc =G03MenuTree.keyoptArea.getStyledDocument();
            Style style = G03MenuTree.keyoptArea.addStyle("Blue", null);
            StyleConstants.setForeground(style, Color.BLUE);
            StyleConstants.setFontFamily(style,"Arial");
            StyleConstants.setFontSize(style,12);
            G03MenuTree.keyoptArea.setText(str);
            doc.setParagraphAttributes(oldlen,newlen-oldlen,G03MenuTree.keyoptArea.getStyle("Blue"), true);
            G03MenuTree.keyoptArea.setEditable(false);
           */
    		new GuessOptTable();
            
    	}
    	if(e.getSource() == G03MenuTree.jcOpt)
    	{   
    		/*
    		 System.out.println("Hi");
    		
    		String str = new String() ;//\\"\n";
    		int oldlen=0;//str.length();
            str+=jOptHelp;
            int newlen=str.length();
            System.out.println(newlen);
            StyledDocument doc =G03MenuTree.keyoptArea.getStyledDocument();
            Style style = G03MenuTree.keyoptArea.addStyle("Blue", null);
            StyleConstants.setForeground(style, Color.BLUE);
            StyleConstants.setFontFamily(style,"Arial");
            StyleConstants.setFontSize(style,12);
            G03MenuTree.keyoptArea.setText(str);
            doc.setParagraphAttributes(oldlen,newlen-oldlen,G03MenuTree.keyoptArea.getStyle("Blue"), true);
            G03MenuTree.keyoptArea.setEditable(false);
            */
    		new OptTable();
    	}
    	

    	if(e.getSource() == G03MenuTree.jcOptFreq)
    	{   
    		String str = new String() ;//\\"\n";
    		int oldlen=0;//str.length();
            str+=jOptFreqHelp;
            int newlen=str.length();
        /*    System.out.println(newlen);
            StyledDocument doc =G03MenuTree.keyoptArea.getStyledDocument();
            Style style = G03MenuTree.keyoptArea.addStyle("Blue", null);
            StyleConstants.setForeground(style, Color.BLUE);
            StyleConstants.setFontFamily(style,"Arial");
            StyleConstants.setFontSize(style,12);
            G03MenuTree.keyoptArea.setText(str);
            doc.setParagraphAttributes(oldlen,newlen-oldlen,G03MenuTree.keyoptArea.getStyle("Blue"), true);*/
            G03MenuTree.keyoptArea.setEditable(false);
            optfreq="optfreq";
    	}
    	if(e.getSource() == G03MenuTree.jcSp)
    	{   
    		 System.out.println("Hi");
    		
    		String str = new String() ;//\\"\n";
    		int oldlen=0;//str.length();
            str+=jSpHelp;
            int newlen=str.length();
            System.out.println(newlen);
         /*   StyledDocument doc =G03MenuTree.keyoptArea.getStyledDocument();
            Style style = G03MenuTree.keyoptArea.addStyle("Blue", null);
            StyleConstants.setForeground(style, Color.BLUE);
            StyleConstants.setFontFamily(style,"Arial");
            StyleConstants.setFontSize(style,12);
            G03MenuTree.keyoptArea.setText(str);
            doc.setParagraphAttributes(oldlen,newlen-oldlen,G03MenuTree.keyoptArea.getStyle("Blue"), true);
         */   G03MenuTree.keyoptArea.setEditable(false);
            
    	}
    	
    	
    	/* Density Sub Menu Listeners*/
    	if(e.getSource()==G03MenuTree.dcis)
    	{
    	    JTextField dcistext=new JTextField(3);
    	    
    	    JPanel dcispane=new JPanel(new GridBagLayout());
    	    GridBagConstraints c=new GridBagConstraints();
    	    JLabel dcislabel=new JLabel(" for state N=");
    	
    	    Object[] obj=new Object[2];
    	    c.insets=new Insets(0,0,0,0);
    	    dcispane.add(dcislabel,c);//,BorderLayout.WEST);
    	    c.insets=new Insets(0,0,0,90);
    	    dcispane.add(dcistext,c);//,BorderLayout.CENTER);
    	    obj[0]="Use total unrelaxed density";
    	    
    	    
    	    obj[1]=dcispane;
    	    
    	    
    	   int choice=JOptionPane.showConfirmDialog(null, obj,
    	             "Enter the value of N",
    	            JOptionPane.OK_OPTION);    
    	    if(choice==0)
    	    {
    	    	if(RouteClass.initCount==0)
        	   	{
        	   	System.out.println(RouteClass.initCount+" \tInside init");
        	   	RouteClass.initBuffer();
        	   	RouteClass.initCount++;
        	   	}
        		if(denC==0)
        	   	{
        	   	System.out.println(denC+" \t Inside freQC");
        	   	denFlag=RouteClass.keyIndex;
        	   	RouteClass.keyIndex++;
        	   	denC++;
        	   	}
        	   	else
        	   	{
        	   		//System.out.println("Reinitialize");
        	   		RouteClass.keywordBuffer[denFlag]=new StringBuffer();
        	   	}
      	        if(!(InsertNode.nodeExists("Density")))
      	    	    InsertNode.insertNode("Job","Density");
      	        InsertNode.deleteChildren("Den");    
    	        InsertNode.insertNode("Density","CIS="+dcistext.getText());
    	        RouteClass.keywordBuffer[denFlag].append("Density=");
      	        RouteClass.keywordBuffer[denFlag].append("(CIS=("+dcistext.getText()+"))");
      	        RouteClass.writeRoute();
    	    
    	    }
    	    
    	}
    	
    	if(e.getSource()==G03MenuTree.dtran)
    	{
    	    JTextField dtrantextm=new JTextField(3);
    	    JTextField dtrantextn=new JTextField(3);
    	    JPanel dtranPanel = new JPanel(new GridBagLayout());
    	    GridBagConstraints c=new GridBagConstraints();
    	    
    	    JLabel lbl1=new JLabel("M=");
    	    c.insets=new Insets(0,-80,0,0);
    	    c.gridy=0;
    	    dtranPanel.add(lbl1,c);
    	    c.insets=new Insets(0,-80,0,0);
    	    dtranPanel.add(dtrantextm,c);
    	    //c.gridy=0;
    	    
    	    JLabel lbl2=new JLabel("(ground state :M =0) ");
    	    c.insets=new Insets(0,-30,0,0);
    	    dtranPanel.add(lbl2,c);
    	    c.gridy=1;
    	    c.insets=new Insets(0,-30,0,0);
    	    dtranPanel.add(new JLabel("and state N ="),c);
    	    c.insets=new Insets(0,0,0,0);
    	    dtranPanel.add(dtrantextn,c);
    	    
    	    
    	    Object[] obj=new Object[2];
    	    
    	    obj[0]="Use CIS transition density between state";
    	    obj[1]=dtranPanel;
    	    
    	    //JOptionPane.showOptionDialog(null,"Enter the value of N",JOptionPane.OK_CANCEL_OPTION);
    	    
    	  int choice=  JOptionPane.showConfirmDialog(null, obj,
    	             "Enter the value of M and N ",
    	            JOptionPane.OK_OPTION);    
    	  if(choice==0)
  	    {
    	  	if(RouteClass.initCount==0)
    	   	{
    	   	System.out.println(RouteClass.initCount+" \tInside init");
    	   	RouteClass.initBuffer();
    	   	RouteClass.initCount++;
    	   	}
    		if(denC==0)
    	   	{
    	   	System.out.println(denC+" \t Inside freQC");
    	   	denFlag=RouteClass.keyIndex;
    	   	RouteClass.keyIndex++;
    	   	denC++;
    	   	}
    	   	else
    	   	{
    	   		//System.out.println("Reinitialize");
    	   		RouteClass.keywordBuffer[denFlag]=new StringBuffer();
    	   	}
  	        if(!(InsertNode.nodeExists("Density")))
  	    	    InsertNode.insertNode("Job","Density");
  	        InsertNode.deleteChildren("Den");    
  	        InsertNode.insertNode("Density","Transition=("+dtrantextn.getText()+",["+dtrantextm.getText()+"])");
  	        RouteClass.keywordBuffer[denFlag].append("Density=");
  	        RouteClass.keywordBuffer[denFlag].append("(Transition=("+dtrantextn.getText()+",["+dtrantextm.getText()+"]))");
  	        RouteClass.writeRoute();
  	
  	    }
    	  
    	}
    	/*if(e.getSource()==G03MenuTree.dCurrent)
    	{
    		if(RouteClass.initCount==0)
    	   	{
    	   	System.out.println(RouteClass.initCount+" \tInside init");
    	   	RouteClass.initBuffer();
    	   	RouteClass.initCount++;
    	   	}
    		if(denC==0)
    	   	{
    	   	System.out.println(denC+" \t Inside freQC");
    	   	denFlag=RouteClass.keyIndex;
    	   	RouteClass.keyIndex++;
    	   	denC++;
    	   	}
    	   	else
    	   	{
    	   		//System.out.println("Reinitialize");
    	   		RouteClass.keywordBuffer[denFlag]=new StringBuffer();
    	   	}
    	    
            if(!(InsertNode.nodeExists("Density")))
    	    InsertNode.insertNode("Job","Density");
            InsertNode.deleteChildren("Den");
            InsertNode.insertNode("Density",e.getActionCommand());
            RouteClass.keywordBuffer[denFlag].append("Density=");
            RouteClass.keywordBuffer[denFlag].append(e.getActionCommand());
            RouteClass.writeRoute();
    		
    	}*/
    	
    	if(e.getSource()==G03MenuTree.dAll ||
    	        e.getSource()==G03MenuTree.dscf ||
    	        e.getSource()==G03MenuTree.dmp2 ||
    	        e.getSource()==G03MenuTree.dci ||
    	        e.getSource()==G03MenuTree.dqci ||
    	        e.getSource()==G03MenuTree.dchkpoint||
    	        e.getSource()==G03MenuTree.dallt ||
    	       e.getSource()==G03MenuTree.dcurrent
    	  	)
    	{
    		if(RouteClass.initCount==0)
    	   	{
    	   	System.out.println(RouteClass.initCount+" \tInside init");
    	   	RouteClass.initBuffer();
    	   	RouteClass.initCount++;
    	   	}
    		if(denC==0)
    	   	{
    	   	System.out.println(denC+" \t Inside freQC");
    	   	denFlag=RouteClass.keyIndex;
    	   	RouteClass.keyIndex++;
    	   	denC++;
    	   	}
    	   	else
    	   	{
    	   		//System.out.println("Reinitialize");
    	   		RouteClass.keywordBuffer[denFlag]=new StringBuffer();
    	   	}
    	    
            if(!(InsertNode.nodeExists("Density")))
    	    InsertNode.insertNode("Job","Density");
            InsertNode.deleteChildren("Den");
            InsertNode.insertNode("Density",e.getActionCommand());
            if(e.getActionCommand().equals("Current(default)"))
            {
            	RouteClass.keywordBuffer[denFlag].append("Density");
                //RouteClass.keywordBuffer[denFlag].append(e.getActionCommand());
                RouteClass.writeRoute();
        	
            }
            else
            {
            RouteClass.keywordBuffer[denFlag].append("Density=");
            RouteClass.keywordBuffer[denFlag].append(e.getActionCommand());
            RouteClass.writeRoute();
            }
    	}
    	
    	
    	if(e.getSource()==G03MenuTree.jcOptFreq)
    	{
    	    if(!(InsertNode.nodeExists(e.getActionCommand())))
    	    {InsertNode.insertNode("Job",e.getActionCommand());
    		optfreqflag=1;
    	    RouteClass.writeRoute();
    	    }
    	}
    	
    	
    	
    	
    	if(     e.getSource()==G03MenuTree.jcSp ||
    	        e.getSource()==G03MenuTree.jlAdmp ||
    	        e.getSource()==G03MenuTree.jlArchive ||
    	        e.getSource()==G03MenuTree.jlBomp ||
    	        e.getSource()==G03MenuTree.jlForce ||
    	        e.getSource()==G03MenuTree.jlIrc ||
    	        e.getSource()==G03MenuTree.jlIrcMax ||
    	        e.getSource()==G03MenuTree.jlPolar||
    	        e.getSource()==G03MenuTree.jlScan ||
    	        e.getSource()==G03MenuTree.jlStable ||
    	        e.getSource()==G03MenuTree.jlVolume)
    	{
    	    if(!(InsertNode.nodeExists(e.getActionCommand())))
    	    {InsertNode.insertNode("Job",e.getActionCommand());
    		if(RouteClass.initCount==0)
    	   	{
    	   		System.out.println(RouteClass.initCount+" \tInside init");
    	   	RouteClass.initBuffer();
    	   	RouteClass.initCount++;
    	   	}
    	    RouteClass.keywordBuffer[RouteClass.keyIndex]=new StringBuffer();
    	    RouteClass.keywordBuffer[RouteClass.keyIndex].append(e.getActionCommand());
    	    RouteClass.keyIndex++;
    	    RouteClass.writeRoute();
    	    }
    	}
    	
    	if(e.getSource()==G03MenuTree.jlOptIrcMax)
    	{
    	   /* if(!(InsertNode.nodeExists(e.getActionCommand())))
    	    {InsertNode.insertNode("Job",e.getActionCommand());
    		if(RouteClass.initCount==0)
    	   	{
    	    System.out.println(RouteClass.initCount+" \tInside init");
    	   	RouteClass.initBuffer();
    	   	RouteClass.initCount++;
    	   	}
    	    RouteClass.keywordBuffer[RouteClass.keyIndex]=new StringBuffer();
    	    RouteClass.keywordBuffer[RouteClass.keyIndex].append("Opt IRC-Max");
    	    RouteClass.keyIndex++;
    	    RouteClass.writeRoute();
    	    }*/
    	    
    	    if(!(InsertNode.nodeExists(e.getActionCommand())))
    	    {InsertNode.insertNode("Job",e.getActionCommand());
    		optircmaxflag=1;
    	    RouteClass.writeRoute();
    	    }
    	    
    	    
    	}
    	
    	if(e.getSource()==G03MenuTree.jlOptPolar)
    	{
    	    if(!(InsertNode.nodeExists(e.getActionCommand())))
    	    {InsertNode.insertNode("Job",e.getActionCommand());
    		optpolarflag=1;
    	    RouteClass.writeRoute();
    	    }
    	    
    	    
    	   /* if(!(InsertNode.nodeExists(e.getActionCommand())))
    	    {InsertNode.insertNode("Job",e.getActionCommand());
    		
    	    if(RouteClass.initCount==0)
    	   	{
    	    System.out.println(RouteClass.initCount+" \tInside init");
    	   	RouteClass.initBuffer();
    	   	RouteClass.initCount++;
    	   	}
    	    RouteClass.keywordBuffer[RouteClass.keyIndex]=new StringBuffer();
    	    RouteClass.keywordBuffer[RouteClass.keyIndex].append("Opt Polar");
    	    RouteClass.keyIndex++;
    	    RouteClass.writeRoute();
    	    }*/
    	}
}
    
    
    
    
    
    
    
    
}
