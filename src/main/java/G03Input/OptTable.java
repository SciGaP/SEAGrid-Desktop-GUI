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
 * Created on Mar 22, 2005
 * @author Michael Sheetz 
 * @author Sandeep Kumar Seethaapathy @author Shashank Jeedigunta   
 * 
 */


package G03Input;


import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;


public class OptTable extends JFrame implements ItemListener,ActionListener{
	Color bgColor= new Color(236,233,216);
	Color foreColor =new Color(0,78,152);
	JPanel donePanel;
	JButton doneButton,clearButton,exitButton;
	public static JTable table;
	JFrame OptFrame;
	public static int optListen;
	public static int optC,optFlag,optionsC;
	public static Vector<JRadioButton> optClear;
	public static Vector<String> optVopt;
	public static String maxSel,maxSt,readN,chkN,sadN,iharN,pathN,maxCy;
	
	// Opt Table Radio Buttons
	public static JRadioButton OMaxCy =new JRadioButton("MaxCycle=N");
	public static JRadioButton ORedun =new JRadioButton("Redundant");
	public static JRadioButton OEstmF =new JRadioButton("EstmFC");
	public static JRadioButton OTight =new JRadioButton("Tight");
	public static JRadioButton OMicro=new JRadioButton("Micro");
	public static JRadioButton OHFErr=new JRadioButton("HFError");
	public static JRadioButton OMaxSt =new JRadioButton("MaxStep=N");
	public static JRadioButton OZMatr=new JRadioButton("Z-Matrix");
	public static JRadioButton OReadFC=new JRadioButton("ReadFC");
	public static JRadioButton OVery=new JRadioButton("VeryTight");
	public static JRadioButton  ONoMic=new JRadioButton("NoMicro");
	public static JRadioButton OFineG=new JRadioButton("FineGridError");
	public static JRadioButton ONatur=new JRadioButton("NaturalOrbitals");
	public static JRadioButton OCarte=new JRadioButton("Cartesian");
	public static JRadioButton OEigen=new JRadioButton("EigenTest");
	public static JRadioButton OQuadM=new JRadioButton("QuadMacro");
	public static JRadioButton OSG1Er=new JRadioButton("SG1Error");
	public static JRadioButton OTS =new JRadioButton("TS");
	public static JRadioButton OStar=new JRadioButton("StarOnly");
	public static JRadioButton ONoEig=new JRadioButton("NoEigenTest");
	public static JRadioButton ONoQua=new JRadioButton("NoQuadMacro");
	public static JRadioButton OReadErr=new JRadioButton("ReadError");
	public static JRadioButton OSadd=new JRadioButton("Saddle=N");
	public static JRadioButton OFCCard=new JRadioButton("FCCards");
	public static JRadioButton OExper=new JRadioButton("Expert");
	public static JRadioButton OCheck=new JRadioButton("CheckCoordinates");
	public static JRadioButton OQST2=new JRadioButton("QST2");
	public static JRadioButton ORCFC=new JRadioButton("RCFC");
	public static JRadioButton ONoExp=new JRadioButton("NoExpert");
	public static JRadioButton OLinear=new JRadioButton("Linear");
	public static JRadioButton OQST3=new JRadioButton("QST3");
	public static JRadioButton OCalcH=new JRadioButton("CalcHHFC");
	public static JRadioButton OLoose=new JRadioButton("Loose");
	public static JRadioButton ONoLinear=new JRadioButton("NoLinear");
	public static JRadioButton OPathN=new JRadioButton("Path=N");
	public static JRadioButton OCalcFc=new JRadioButton("CalcFC");
	public static JRadioButton OTrust=new JRadioButton("TrustUpdate");
	public static JRadioButton OOptRe=new JRadioButton("OptReactant");
	public static JRadioButton OCalcAll=new JRadioButton("CalcAll");
	public static JRadioButton ONoTrust=new JRadioButton("NoTrustUpdate");
	public static JRadioButton ONoOpt=new JRadioButton("NoOptReactant");
	public static JRadioButton OVCD=new JRadioButton("VCD");
	public static JRadioButton ORFO=new JRadioButton("RFO");
	public static JRadioButton OBioMol=new JRadioButton("BioMolecular");
	public static JRadioButton ONoRam=new JRadioButton("NoRaman");
	public static JRadioButton OGDIIS=new JRadioButton("GDIIS");
	public static JRadioButton OOptProd=new JRadioButton("OptProduct");
	public static JRadioButton ONewton=new JRadioButton("Newton");
	public static JRadioButton  ONoOptPro=new JRadioButton("NoOptProduct");
	public static JRadioButton ONRScale=new JRadioButton("NRScale");
	public static JRadioButton OConical=new JRadioButton("Conical");
	public static JRadioButton ONoNRS=new JRadioButton("NoNRScale");
	public static JRadioButton ORestart=new JRadioButton("Restart");
	public static JRadioButton OEF=new JRadioButton("EF");
	public static JRadioButton ONoFreeze=new JRadioButton("NoFreeze");
	public static JRadioButton OSteep=new JRadioButton("Steep");
	public static JRadioButton OModRed=new JRadioButton("ModRedundant");
	public static JRadioButton OUpdate=new JRadioButton("UpdateMethod=keyword");
	public static JRadioButton OInitHar=new JRadioButton("InitialHarmonic=N");
	public static JRadioButton OBig=new JRadioButton("Big");
	public static JRadioButton OChkHar=new JRadioButton("ChkHarmonic=N");
	public static JRadioButton OSmall=new JRadioButton("Small");
	public static JRadioButton OReadHar=new JRadioButton("ReadHarmonic=N");
    
	
	
    public OptTable(){
    	
    OptFrame = new JFrame("Opt Options" );
   
    
    //UIDefaults ui = UIManager.getLookAndFeel().getDefaults();
    
  //UIManager.put("RadioButton.focus", ui.getColor("control"));
    
    DefaultTableModel dm = new DefaultTableModel();
   
    OMaxCy.setBackground(Color.WHITE);
    ORedun.setBackground(Color.WHITE);OEstmF.setBackground(Color.WHITE);OTight.setBackground(Color.WHITE);OMicro.setBackground(Color.WHITE);OHFErr.setBackground(Color.WHITE);
	
	OMaxSt.setBackground(Color.WHITE);OZMatr.setBackground(Color.WHITE);OReadFC.setBackground(Color.WHITE);OVery.setBackground(Color.WHITE);ONoMic.setBackground(Color.WHITE);OFineG.setBackground(Color.WHITE);
	
	ONatur.setBackground(Color.WHITE);OCarte.setBackground(Color.WHITE);OStar.setBackground(Color.WHITE);OEigen.setBackground(Color.WHITE);OQuadM.setBackground(Color.WHITE);OSG1Er.setBackground(Color.WHITE);
	
	OTS.setBackground(Color.WHITE);OFCCard.setBackground(Color.WHITE);ONoEig.setBackground(Color.WHITE);ONoQua.setBackground(Color.WHITE);OReadErr.setBackground(Color.WHITE);
	
	OSadd.setBackground(Color.WHITE);ORCFC.setBackground(Color.WHITE);OExper.setBackground(Color.WHITE);OCheck.setBackground(Color.WHITE);
	OQST2.setBackground(Color.WHITE);OCalcH.setBackground(Color.WHITE);ONoExp.setBackground(Color.WHITE);OLinear.setBackground(Color.WHITE);
	OQST3.setBackground(Color.WHITE);OCalcFc.setBackground(Color.WHITE);OLoose.setBackground(Color.WHITE);ONoLinear.setBackground(Color.WHITE);
	OPathN.setBackground(Color.WHITE);OCalcAll.setBackground(Color.WHITE);OTrust.setBackground(Color.WHITE);
	OOptRe.setBackground(Color.WHITE);OVCD.setBackground(Color.WHITE);ONoTrust.setBackground(Color.WHITE);
	ONoOpt.setBackground(Color.WHITE);ONoRam.setBackground(Color.WHITE);ORFO.setBackground(Color.WHITE);
	OBioMol.setBackground(Color.WHITE);OGDIIS.setBackground(Color.WHITE);
	OOptProd.setBackground(Color.WHITE);ONewton.setBackground(Color.WHITE);
	ONoOptPro.setBackground(Color.WHITE);ONRScale.setBackground(Color.WHITE);
	OConical.setBackground(Color.WHITE);ONoNRS.setBackground(Color.WHITE);
	ORestart.setBackground(Color.WHITE);OEF.setBackground(Color.WHITE);
	ONoFreeze.setBackground(Color.WHITE);OSteep.setBackground(Color.WHITE);
	OModRed.setBackground(Color.WHITE);OUpdate.setBackground(Color.WHITE);
	OInitHar.setBackground(Color.WHITE);OBig.setBackground(Color.WHITE);
	OChkHar.setBackground(Color.WHITE);OSmall.setBackground(Color.WHITE);
	OReadHar.setBackground(Color.WHITE);
  
	//-----ItemListeners
	if(optListen==0)
	{
		optClear=new Vector<JRadioButton>(60);
		//optClear.addElement(OMaxCy);
		optClear.addElement(ORedun);optClear.addElement(OEstmF);
		optClear.addElement(OTight);
		optClear.addElement(OMicro);
		optClear.addElement(OHFErr);
		//optClear.addElement(OMaxSt);
		optClear.addElement(OZMatr);
		optClear.addElement(OReadFC);
		optClear.addElement(OVery);
		optClear.addElement(ONoMic);
		optClear.addElement(OFineG);
		optClear.addElement(ONatur);
		optClear.addElement(OCarte);
		optClear.addElement(OEigen);
		optClear.addElement(OQuadM);
		optClear.addElement(OSG1Er);
		optClear.addElement(OTS);
		optClear.addElement(OStar);
		optClear.addElement(ONoEig);
		optClear.addElement(ONoQua);
		optClear.addElement(OReadErr);
		//optClear.addElement(OSadd);
		optClear.addElement(OFCCard);
		optClear.addElement(OExper);
		optClear.addElement(OQST2);
		optClear.addElement(ORCFC);
		optClear.addElement(ONoExp);
		optClear.addElement(OLinear);
		optClear.addElement(OQST3);
		optClear.addElement(OCalcH);
		optClear.addElement(OLoose);
		optClear.addElement(ONoLinear);
		//optClear.addElement(OPathN);
		optClear.addElement(OCalcFc);
		optClear.addElement(OTrust);
		optClear.addElement(OOptRe);
		optClear.addElement(OCalcAll);
		optClear.addElement(ONoTrust);
		optClear.addElement(ONoOpt);
		optClear.addElement(OVCD);
		optClear.addElement(ORFO);
		optClear.addElement(OBioMol);
		optClear.addElement(ONoRam);
		optClear.addElement(OGDIIS);
		optClear.addElement(OOptProd);
		optClear.addElement(ONewton);
		optClear.addElement(ONoOptPro);
		
		optClear.addElement(ONRScale);
		optClear.addElement(OConical);
		optClear.addElement(ONoNRS);
		optClear.addElement(ORestart);
		
		optClear.addElement(OEF);
		optClear.addElement(ONoFreeze);
		optClear.addElement(OSteep);
		optClear.addElement(OModRed);
		optClear.addElement(OUpdate);
	//	optClear.addElement(OInitHar);
		
		optClear.addElement(OBig);
		//optClear.addElement(OChkHar);
		optClear.addElement(OSmall);
		//optClear.addElement(OReadHar);
			
	OMaxCy.addItemListener(this);
    ORedun.addItemListener(this);OEstmF.addItemListener(this);OTight.addItemListener(this);OMicro.addItemListener(this);OHFErr.addItemListener(this);
	OMaxSt.addItemListener(this);OZMatr.addItemListener(this);OReadFC.addItemListener(this);OVery.addItemListener(this);ONoMic.addItemListener(this);OFineG.addItemListener(this);
	
	ONatur.addItemListener(this);OCarte.addItemListener(this);OStar.addItemListener(this);OEigen.addItemListener(this);OQuadM.addItemListener(this);OSG1Er.addItemListener(this);
	
	OTS.addItemListener(this);OFCCard.addItemListener(this);ONoEig.addItemListener(this);ONoQua.addItemListener(this);OReadErr.addItemListener(this);
	
	OSadd.addItemListener(this);ORCFC.addItemListener(this);OExper.addItemListener(this);OCheck.addItemListener(this);
	OQST2.addItemListener(this);OCalcH.addItemListener(this);ONoExp.addItemListener(this);OLinear.addItemListener(this);
	OQST3.addItemListener(this);OCalcFc.addItemListener(this);OLoose.addItemListener(this);ONoLinear.addItemListener(this);
	OPathN.addItemListener(this);OCalcAll.addItemListener(this);OTrust.addItemListener(this);
	OOptRe.addItemListener(this);OVCD.addItemListener(this);ONoTrust.addItemListener(this);
	ONoOpt.addItemListener(this);ONoRam.addItemListener(this);ORFO.addItemListener(this);
	OBioMol.addItemListener(this);OGDIIS.addItemListener(this);
	OOptProd.addItemListener(this);ONewton.addItemListener(this);
	ONoOptPro.addItemListener(this);ONRScale.addItemListener(this);
	OConical.addItemListener(this);ONoNRS.addItemListener(this);
	ORestart.addItemListener(this);OEF.addItemListener(this);
	ONoFreeze.addItemListener(this);OSteep.addItemListener(this);
	OModRed.addItemListener(this);OUpdate.addItemListener(this);
	OInitHar.addItemListener(this);OBig.addItemListener(this);
	OChkHar.addItemListener(this);OSmall.addItemListener(this);
	OReadHar.addItemListener(this);
    optListen++;
	}
	
  /*   if(InsertNode.nodeExists("Opt "))
    InsertNode.deleteNode("Opt ");
     RouteClass.keywordBuffer[optFlag]=new StringBuffer();
     RouteClass.writeRoute();
    */ 
    dm.setDataVector(
            
      new Object[][]{
        {OMaxCy,ORedun,OEstmF,OTight,OMicro,OHFErr},
		
		{OMaxSt,OZMatr,OReadFC,OVery,ONoMic,OFineG},
		
		{ONatur,OCarte,OStar,OEigen,OQuadM,OSG1Er},
		
		{OTS,null,OFCCard,ONoEig,ONoQua,OReadErr},
		
		{OSadd,null,ORCFC,OExper,OCheck,null},
		{OQST2,null,OCalcH,ONoExp,OLinear,null},
		{OQST3,null,OCalcFc,OLoose,ONoLinear,null},
		{OPathN,null,OCalcAll,null,OTrust,null},
		{OOptRe,null,OVCD,null,ONoTrust,null},
		{ONoOpt,null,ONoRam,null,ORFO,null},
		{OBioMol,null,null,null,OGDIIS,null},
		{OOptProd,null,null,null,ONewton,null},
		{ONoOptPro,null,null,null,ONRScale,null},
		{OConical,null,null,null,ONoNRS,null},
		{ORestart,null,null,null,OEF,null},
		{ONoFreeze,null,null,null,OSteep,null},
		{OModRed,null,null,null,OUpdate,null},
		{OInitHar,null,null,null,OBig,null},
		{OChkHar,null,null,null,OSmall,null},
		{OReadHar,null,null,null,null,null},
      
      },
        
      new Object[]{"General Procedural","Coordinate System Selection","Initial Force Constants","Convergence","Numerical Algorithms","Error Interpretation"});
                     
    JTable table = new JTable(dm) {
      public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        repaint();
      }
    };
    
    
   /*ButtonGroup group1 = new ButtonGroup();
    group1.add((JRadioButton)dm.getValueAt(0,1));
    group1.add((JRadioButton)dm.getValueAt(1,1));
    group1.add((JRadioButton)dm.getValueAt(2,1));
    ButtonGroup group2 = new ButtonGroup();
    group2.add((JRadioButton)dm.getValueAt(3,1));
    group2.add((JRadioButton)dm.getValueAt(4,1));*/
   table.getColumn("General Procedural").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("General Procedural").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    
    table.getColumn("Coordinate System Selection").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Coordinate System Selection").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    table.getColumn("Initial Force Constants").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Initial Force Constants").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    
    table.getColumn("Convergence").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Convergence").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    table.getColumn("Numerical Algorithms").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Numerical Algorithms").setCellEditor(new RadioButtonEditor(new JCheckBox()));
  
    table.getColumn("Error Interpretation").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Error Interpretation").setCellEditor(new RadioButtonEditor(new JCheckBox()));
  
    
    
    
    doneButton = new JButton("Done");
    doneButton.addActionListener(this);
    //clearButton = new JButton("Clear");
    exitButton = new JButton("Cancel");
    //doneButton.addActionListener(this);
   // clearButton.addActionListener(this);
    exitButton.addActionListener(this);
    
    donePanel = new JPanel();
    donePanel.add(doneButton);
    donePanel.add(exitButton);
    
    /*table.setBackground(bgColor);
    table.setForeground(foreColor);
    */
    
    
    donePanel.setBackground(bgColor);
    donePanel.setForeground(foreColor);
    
    
    table.getTableHeader().setReorderingAllowed(false);
    
    
    
    OptFrame.getContentPane().setLayout(new BorderLayout());
    
       
    JScrollPane scroll = new JScrollPane(table);
    OptFrame.getContentPane().add( scroll,BorderLayout.CENTER);
    OptFrame.getContentPane().add(donePanel,BorderLayout.SOUTH);
    OptFrame.setSize(600,200);
    OptFrame.setLocation(200,75);
    OptFrame.setVisible(true);
  }
    public void actionPerformed(ActionEvent ae)
    {
     	if(ae.getSource()==doneButton){
     		//InsertNode.deleteChildren("Op");
     	   	System.out.println("The value of freq guessV = "+optC);
     	   	if(RouteClass.initCount==0)
     	   	{
     	    System.out.println(RouteClass.initCount+" \tInside init");
     	   	RouteClass.initBuffer();
     	   	RouteClass.initCount++;
     	   	}
     	   	if(optC==0)
     	   	{
     	   	System.out.println(optC+" \t Inside optC");
     	   	optFlag=RouteClass.keyIndex;
     	   	RouteClass.keyIndex++;
     	   	optC++;
     	   	}
     	   	else
     	   	{
     	   		System.out.println("opt Check");
     	   		RouteClass.keywordBuffer[optFlag]=new StringBuffer();
     	   	}
     	   	RouteClass.keywordBuffer[optFlag].append("Opt");
     	   	
     	   	System.out.println("Checking opt table man"+RouteClass.keywordBuffer[optFlag]);
     	   	
     		if(!(InsertNode.nodeExists("Opt")))
     		    InsertNode.insertNode("Job","Opt");
     		try {
     			if(optionsC!=0)
     			{     			
				if(optVopt.size()>1)
				{
					
				 RouteClass.keywordBuffer[optFlag].append("=(");
				 for(int count=0;count<optVopt.size();count++)
				    {
				    	RouteClass.keywordBuffer[optFlag].append(optVopt.get(count));
				    	if(!(count==(optVopt.size()-1)))
				    	{
				    	RouteClass.keywordBuffer[optFlag].append(",");
				    	}
				    }
				    RouteClass.keywordBuffer[optFlag].append(")");
				  }
				 else
				 {
				 	if(optVopt.size()>0)
				 	RouteClass.keywordBuffer[optFlag].append("="+"("+optVopt.get(0)+")");
				 }
			} 
     		}catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     		 //optFlag=RouteClass.keyIndex;
     		 //RouteClass.keyIndex++;
     		 RouteClass.writeRoute();
     		 OptFrame.dispose();
     	   }
     	
     	if(ae.getSource()==exitButton)
     	{
     		OptFrame.dispose();
     	}
     	
     	
     	
    }     
    
    
    
    public void itemStateChanged(ItemEvent e)
    {
        if((e.getItem()==ORedun)||	(e.getItem()==OEstmF)||
    			(e.getItem()==OTight)||	(e.getItem()==OMicro)||	(e.getItem()==OHFErr)||
    			 (e.getItem()==OZMatr)||	(e.getItem()==OReadFC)||(e.getItem()==OVery)||
    			 (e.getItem()==ONoMic)|| (e.getItem()==OFineG)||
    			(e.getItem()==ONatur)|| (e.getItem()==OCarte)|| (e.getItem()==OEigen)||
    			(e.getItem()==OQuadM)|| (e.getItem()==OSG1Er)|| (e.getItem()==OTS)||
    			(e.getItem()==OStar)||(e.getItem()==ONoEig) ||
    			(e.getItem()==ONoQua)|| (e.getItem()==OReadErr)|| (e.getItem()==OFCCard)||
    			(e.getItem()==OExper)|| (e.getItem()==OCheck)|| (e.getItem()==OQST2)||
    			(e.getItem()==ORCFC)||(e.getItem()==ONoExp) ||
    			(e.getItem()==OLinear)|| (e.getItem()==OQST3)|| (e.getItem()==OCalcH)||
    			(e.getItem()==OLoose)|| (e.getItem()==ONoLinear)|| 
    			(e.getItem()==OCalcFc)||(e.getItem()==OTrust) ||
    			(e.getItem()==OCalcAll)|| (e.getItem()==ONoTrust)|| (e.getItem()==ONoOpt)||
    			(e.getItem()==OVCD)|| (e.getItem()==ORFO)|| (e.getItem()==OBioMol)||
    			(e.getItem()==ONoRam)||(e.getItem()==OGDIIS) ||
    			(e.getItem()==OOptProd)|| (e.getItem()==ONewton)|| (e.getItem()==ONoOptPro)||
    			(e.getItem()==ONRScale)|| (e.getItem()==OConical)|| (e.getItem()==ONoNRS)||
    			(e.getItem()==ONoFreeze)||(e.getItem()==OSteep) ||
    			(e.getItem()==OModRed)|| (e.getItem()==OUpdate)|| (e.getItem()==OBig)||
    			(e.getItem()==OSmall)	
    			) 
    	
        	
        	
    			{
        	
        	System.out.println("Test case for opt 2"+(JRadioButton)e.getItem());
    		if(((JRadioButton)e.getItem()).isSelected())
    			
        	
    		{
    			G03Listener.optTabF++;
    			if(optionsC==0)
        	{
        		System.out.println("Options QQQQq");
        	optVopt=new Vector<String>(10);
        	optionsC++;
        	}
    		System.out.println("OPPPOPOPP");
    		if((!(InsertNode.nodeExists("Opt  ")))|| MenuListeners.optfreq.equals("optfreq"))
    		    InsertNode.insertNode("Job","Opt  ");
    		InsertNode.insertNode("Opt  ",RadioButtonEditor.button.getActionCommand());
    		((JRadioButton)e.getItem()).setEnabled(true);
    		String opts  =RadioButtonEditor.button.getActionCommand();
    		optVopt.addElement(opts);
    		}
    		else
    		{
    		System.out.println("Deleted node is in Opt "+((JRadioButton)e.getItem()).getActionCommand());
    		if(InsertNode.nodeExists(((JRadioButton)e.getItem()).getActionCommand()))
    		InsertNode.deleteNode(((JRadioButton)e.getItem()).getActionCommand());
    		optVopt.removeElement(((JRadioButton)e.getItem()).getActionCommand());
    			
    		
    		}
    		}

        //if MaxCycle=N
        
        if(e.getItem()==OMaxCy)
    	{
           	if(optionsC==0)
        	{
           	G03Listener.optTabF++;
        	optVopt=new Vector<String>(10);
        	optionsC++;
        	}

        	if(((JRadioButton)e.getItem()).isSelected())
        	{
         	JTextField stepField;
        	JPanel txt;
        	txt = new JPanel(new GridBagLayout());
        	GridBagConstraints c = new GridBagConstraints();
     		((JRadioButton)e.getItem()).setEnabled(true); 
    	stepField = new JTextField(3);
    	stepField.setSize(2,1);
    	Object[] obj = new Object[2];
    	obj[0] = "Set maximum number of optimization";
    	JLabel s  = new JLabel("steps to N ="); //+stepField;
    	c.insets=new Insets(0,0,0,0);
    	txt.add(s,c);
    	c.insets = new Insets(0,0,0,110);
    	txt.add(stepField,c);
    	obj[1] = txt;
    	int ch=JOptionPane.showConfirmDialog(null,obj,"MaxCycle=N",JOptionPane.OK_OPTION);
    	 G03MenuTree.freOptString="";
    	 				if(ch==0)
    	 					{
    	 				    	if(!(InsertNode.nodeExists("Opt  ")))
    	 				    		{InsertNode.insertNode("Job","Opt  ");
    	 				    			}
    	 				    		// Delete Existing node and then add it
    	 				    		if(InsertNode.nodeExists("MaxCycle"))
    	 				    		{
    	 				    		   	InsertNode.deleteNode("MaxCycle");
    	 				    		}
    	 				    		   	InsertNode.insertNode("Opt  ","MaxCycle="+stepField.getText());
    	 				    	
    	 				    	maxCy = new String();
    	 						maxCy = "MaxCycle="+stepField.getText();
    	 				     	optVopt.addElement(maxCy);
    	 					}
    					if(ch==1) // pressed no remove step
    					{
    					    if((InsertNode.nodeExists("MaxCycle")))
    					    {
    					        //Delete Node if exists
    					        InsertNode.deleteNode("MaxCycle");
    					        optVopt.removeElement("MaxCycle="+maxCy);
    					    }
    					
    					}
        	}
        	
        	else // if it is deselected
        	{
        		  if((InsertNode.nodeExists("MaxCycle")))
        		    InsertNode.deleteNode("MaxCycle");
            	    System.out.println("TTTTTT"+maxCy);
            	    optVopt.removeElement("MaxCycle="+maxCy);
    	 
        	}
    	}
       
       //End of MaxCycle 

        //MaxStep=N
        
        if(e.getItem()== OMaxSt)
    	{
        	if(optionsC==0)
        	{
        		G03Listener.optTabF++;
        		optVopt=new Vector<String>(10);
        	optionsC++;
        	}

            if(((JRadioButton)e.getItem()).isSelected())
        	{
        	JTextField stepField;
        	JPanel txt;
        	txt = new JPanel(new GridBagLayout());
        	GridBagConstraints c = new GridBagConstraints();
     		((JRadioButton)e.getItem()).setEnabled(true);
    	System.out.println("OOO");
    	stepField = new JTextField(3);
    	stepField.setSize(2,1);
    	Object[] obj = new Object[2];
    	stepField.setText("30");
    	obj[0] = "Set maximum size of optimization";
    	JLabel s  = new JLabel("steps to N ="); //+stepField;
    	c.insets=new Insets(0,0,0,0);
    	txt.add(s,c);
    	c.insets = new Insets(0,0,0,110);
    	txt.add(stepField,c);
    	obj[1] = txt;
    	int ch=JOptionPane.showConfirmDialog(null,obj,"MaxStep=N",JOptionPane.OK_OPTION);
    	 G03MenuTree.freOptString="";
    	 
    	 				if(ch==0)
    	 					{  // Pressed Yes
    	 				    if(!(InsertNode.nodeExists("Opt  ")))
    	 				    {InsertNode.insertNode("Job","Opt  ");
    	 				    }
    	 				    //	Delete Existing
    	 				    if(InsertNode.nodeExists("MaxStep="))
    	 				        InsertNode.deleteNode("MaxStep=");
    		    
    	 				    InsertNode.insertNode("Opt  ","MaxStep="+stepField.getText());
    	 				   
	 				    	maxSt = new String();
	 						maxSt = stepField.getText();
	 				     	optVopt.addElement("MaxStep="+maxSt);
    	 					}
    					if(ch==1) // pressed no remove step
    					{
    					    if((InsertNode.nodeExists("MaxStep")))
    					    {
    					        //Delete Node if exists
    					        InsertNode.deleteNode("MaxStep");
    					        optVopt.removeElement("MaxStep="+maxSt);
    					    }
    					
    					}
    	  	}
            else
            {if((InsertNode.nodeExists("MaxStep")))
    		    InsertNode.deleteNode("MaxStep");
    	    System.out.println("TTTTTT"+maxSel);
    	    optVopt.removeElement("MaxStep="+maxSt);         
                
            }
    	}
              
        //End of MaxStep=N
        
        //if Saddle=N
        
        if(e.getItem()==OSadd)
    	{
        	if(optionsC==0)
        	{
        		G03Listener.optTabF++;
        	optVopt=new Vector<String>(10);
        	optionsC++;
        	}
            if(((JRadioButton)e.getItem()).isSelected())
        	{
        	JTextField stepField;
        	JPanel txt;
        	txt = new JPanel(new GridBagLayout());
        	GridBagConstraints c = new GridBagConstraints();
        	
    		((JRadioButton)e.getItem()).setEnabled(true);
        	System.out.println("OOO");
        	stepField = new JTextField(3);
        	
        	Object[] obj = new Object[2];
    	
    	obj[0] = "Optimize to a saddle point";
    	JLabel s  = new JLabel("of order N ="); //+stepField;
    	c.insets=new Insets(0,0,0,0);
    	txt.add(s,c);
    	c.insets = new Insets(0,0,0,110);
    	txt.add(stepField,c);
    	obj[1] = txt;
    	int ch=JOptionPane.showConfirmDialog(null,obj,"Saddle=N",JOptionPane.OK_OPTION);
    	 G03MenuTree.freOptString="";
    	if(ch==0)
    	{
    	 if(!(InsertNode.nodeExists("Opt  ")))
    	 {
    			InsertNode.insertNode("Job","Opt  ");
    			
     	}
    	 //Delete Existing and then write
    	 if(InsertNode.nodeExists("Saddle"))
    	     InsertNode.deleteNode("Saddle");
    	 InsertNode.insertNode("Opt  ","Saddle="+stepField.getText());	
    	 maxSel=new String();
	     maxSel=stepField.getText();
	     sadN = new String();
		 sadN = stepField.getText();
	     optVopt.addElement("Saddle="+sadN);
       	}
        	
        if(ch==1)
        { //Pressed No
            if(InsertNode.nodeExists("Saddle"))
       	     InsertNode.deleteNode("Saddle");
              optVopt.removeElement("Saddle="+sadN);    
        }
        	
        	
        	}    
        else {
        	if((InsertNode.nodeExists("Saddle")))
    		    InsertNode.deleteNode("Saddle");
    	        optVopt.removeElement("Saddle="+sadN);         
            
        }
        	
    	
    	}//End of Saddle 
        
        //Path=N
        
        if(e.getItem()=="Path")
    	{
        	if(optionsC==0)
        	{
        		G03Listener.optTabF++;
        	optVopt=new Vector<String>(10);
        	optionsC++;
        	}
        
        	if(((JRadioButton)e.getItem()).isSelected())
        	{
        	JTextField stepField;
        	JPanel txt;
        	txt = new JPanel(new GridBagLayout());
        	GridBagConstraints c = new GridBagConstraints();
    		((JRadioButton)e.getItem()).setEnabled(true);
    	System.out.println("OOO");
    	stepField = new JTextField(3);
    	
    	Object[] obj = new Object[2];
    	
    	obj[0] = "Simultaneously optimize a transition";
    	
    	JLabel s  = new JLabel("state and an M="); //+stepField;
    	c.insets=new Insets(0,0,0,0);
    	txt.add(s,c);
    	c.insets = new Insets(0,0,0,0);
    	JLabel s1 = new JLabel("-point reaction path");
    	
    	txt.add(stepField,c);
    	
    	c.insets = new Insets(0,0,0,30);
    	txt.add(s1,c);
    	obj[1] = txt;
    	
    	
    	int ch=JOptionPane.showConfirmDialog(null,obj,"Path=N",JOptionPane.OK_OPTION);
    	 G03MenuTree.freOptString="";
    	 if(ch==0)
    		{
    		    if(!(InsertNode.nodeExists("Opt  ")))
    			 InsertNode.insertNode("Job","Opt  ");
    		    
    		    //Delete Existing and Write
    		    if(InsertNode.nodeExists("Path"))
    		        InsertNode.deleteNode("Path=");
    		    InsertNode.insertNode("Opt  ","Path="+stepField.getText());
    		    maxSel=new String();
    		     maxSel=stepField.getText();
    		     pathN = new String();
    			 pathN = stepField.getText();
    		     optVopt.addElement("Path="+pathN);
    		    
    		}
    					if(ch==1) // pressed no remove step
    					{
    					    if((InsertNode.nodeExists("Path")))
    					    {
    					        //Delete Node if exists
    					        InsertNode.deleteNode("Path");
    					        optVopt.removeElement("Path="+pathN);
    					    }
    					
    					}
    	 
        	}
            else{
            	if((InsertNode.nodeExists("Path")))
        		    InsertNode.deleteNode("Path");
        	    System.out.println("TTTTTT"+maxSel);
        	    optVopt.removeElement("Path="+pathN);     
            }
    	}
        
        //End of Path=N
        
        //InitialHarmonic=N
        if(e.getItem()==OInitHar)
    	{
        	if(optionsC==0)
        	{
        		G03Listener.optTabF++;
        	optVopt=new Vector<String>(10);
        	optionsC++;
        	}
        
            if(((JRadioButton)e.getItem()).isSelected())
        	{
        	JTextField stepField;
        	JPanel txt;
        	txt = new JPanel(new GridBagLayout());
        	GridBagConstraints c = new GridBagConstraints();
        	((JRadioButton)e.getItem()).setEnabled(true);
    	System.out.println("OOO");
    	stepField = new JTextField(3);
    	stepField.setSize(2,1);
    	Object[] obj = new Object[2];
    	
    	obj[0] = "Add harmonic contraints to intial structure with force";
    	JLabel s  = new JLabel("constant N/1000 Hartree/Bohr*Bohr with N ="); //+stepField;
    	c.insets=new Insets(0,0,0,0);
    	txt.add(s,c);
    	c.insets = new Insets(0,0,0,30);
    	txt.add(stepField,c);
    	obj[1] = txt;
    	int ch=JOptionPane.showConfirmDialog(null,obj,"InitialHarmonic=N",JOptionPane.OK_OPTION);
    	 G03MenuTree.freOptString="";
    	 if(ch==0)
    		{
    		    if(!(InsertNode.nodeExists("Opt  ")))
    			 {InsertNode.insertNode("Job","Opt  ");
    			}
    		      
    		    if((InsertNode.nodeExists("InitialHarmonic")))
			    {
			        //Delete Node if exists
			        InsertNode.deleteNode("InitialHarmonic");
			    }
    		    InsertNode.insertNode("Opt  ","InitialHarmonic="+stepField.getText());
    		    maxSel=new String();
    		     maxSel=stepField.getText();
    		     iharN = new String();
    			 iharN = stepField.getText();
    		     optVopt.addElement("InitialHarmonic=N"+iharN);
    		}
    					if(ch==1) // pressed no remove step
    					{
    					    if((InsertNode.nodeExists("InitialHarmonic")))
    					    {
    					        //Delete Node if exists
    					        InsertNode.deleteNode("InitialHarmonic");
    					        optVopt.removeElement("InitialHarmonic"+iharN);
    					    }
    					
    					}
        	}
            else{if((InsertNode.nodeExists("InitialHarmonic")))
    		    InsertNode.deleteNode("InitialHarmonic");
    	    System.out.println("TTTTTT"+maxSel);
    	    optVopt.removeElement("InitialHarmonic="+iharN);
            }
    	}// InitialHarmonic
        
        //ChkHarmonic
        if(e.getItem()==OChkHar)
    	{

        	if(optionsC==0)
        	{
        		G03Listener.optTabF++;
        	optVopt=new Vector<String>(10);
        	optionsC++;
        	}
        
            if(((JRadioButton)e.getItem()).isSelected())
        	{
        	JTextField stepField;
        	JPanel txt;
        	txt = new JPanel(new GridBagLayout());
        	GridBagConstraints c = new GridBagConstraints();
        	((JRadioButton)e.getItem()).setEnabled(true);
    	System.out.println("OOO");
    	stepField = new JTextField(3);
    	stepField.setSize(2,1);
    	Object[] obj = new Object[3];
    	
    	obj[0] = "Add harmonic contraints to intial structure";
    	obj[1] = "saved on the chk file with force constant";
    	
    	JLabel s  = new JLabel("N/1000 Hartree/Bohr*Bohr with N ="); //+stepField;
    	c.insets=new Insets(0,0,0,0);
    	txt.add(s,c);
    	c.insets = new Insets(0,0,0,30);
    	txt.add(stepField,c);
    	obj[2] = txt;
    	int ch=JOptionPane.showConfirmDialog(null,obj,"ChkHarmonic=N",JOptionPane.OK_OPTION);
    	 G03MenuTree.freOptString="";
    	 if(ch==0)
    		{
    		    if(!(InsertNode.nodeExists("Opt  ")))
    			 {InsertNode.insertNode("Job","Opt  ");
    			}
    		    if((InsertNode.nodeExists("ChkHarmonic")))
			    {
			        //Delete Node if exists
			        InsertNode.deleteNode("ChkHarmonic");
			    }
    		    InsertNode.insertNode("Opt  ","ChkHarmonic="+stepField.getText());
    		    maxSel=new String();
   		     maxSel=stepField.getText();
   		    chkN = new String();
   			 chkN = stepField.getText();
   			 optVopt.addElement("ChkHarmonic="+chkN);
    		}
    					if(ch==1) // pressed no remove step
    					{
    					    if((InsertNode.nodeExists("ChkHarmonic")))
    					    {
    					        //Delete Node if exists
    					        InsertNode.deleteNode("ChkHarmonic");
    					        optVopt.removeElement("ChkHarmonic="+chkN);
    					    }
    					
    					}
      
        	}
            
            else{
            	if((InsertNode.nodeExists("ChkHarmonic")))
        		    InsertNode.deleteNode("ChkHarmonic");
        	    System.out.println("TTTTTT"+maxSel);
        	    optVopt.removeElement("ChkHarmonic="+chkN);
            }
    	} //end of chkharmonic
        
        //ReadHarmonic
        
        if(e.getItem()==OReadHar)
    	{

        	if(optionsC==0)
        	{
        		G03Listener.optTabF++;
        	optVopt=new Vector<String>(10);
        	optionsC++;
        	}
        
            if(((JRadioButton)e.getItem()).isSelected())
        	{
        	JTextField stepField;
        	JPanel txt;
        	txt = new JPanel(new GridBagLayout());
        	GridBagConstraints c = new GridBagConstraints();
        	((JRadioButton)e.getItem()).setEnabled(true);
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
    	int ch=JOptionPane.showConfirmDialog(null,obj,"ReadHarmonic=N",JOptionPane.OK_OPTION);
    	 G03MenuTree.freOptString="";
    	 
    	 if(ch==0)
    		{
    		    if(!(InsertNode.nodeExists("Opt  ")))
    			 {InsertNode.insertNode("Job","Opt  ");
    			}
    		    
    		    if((InsertNode.nodeExists("ReadHarmonic")))
			    {
			        //Delete Node if exists
			        InsertNode.deleteNode("ReadHarmonic");
			    }
    		    InsertNode.insertNode("Opt  ","ReadHarmonic="+stepField.getText());
    		    readN= new String();
   			 readN = stepField.getText();
   		     optVopt.addElement("ReadHarmonic="+readN);
   		
    		}
    					if(ch==1) // pressed no remove step
    					{
    					    if((InsertNode.nodeExists("ReadHarmonic")))
    					    {
    					        //Delete Node if exists
    					        InsertNode.deleteNode("ReadHarmonic");
    					        optVopt.removeElement("ReadHarmonic="+readN);
    					    }
    					
    				}
         	     	 
        	}
            
            else{
            	if((InsertNode.nodeExists("ReadHarmonic")))
        		    InsertNode.deleteNode("ReadHarmonic");
        	  
        	    optVopt.removeElement("ReadHarmonic="+readN);
            }
    	} //end of Read Harmonic
        
    
        
        
        
        
    }//end of ITemStateChanged
  public static void main(String[] args) {
    OptTable frame = new OptTable();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
  }
}

