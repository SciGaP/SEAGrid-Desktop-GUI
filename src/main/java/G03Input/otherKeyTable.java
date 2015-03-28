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




package G03Input;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.text.html.*;
import javax.swing.text.*;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.util.*;



public class otherKeyTable extends JFrame implements ItemListener,ActionListener 
{
	Color bgColor= new Color(236,233,216);
	Color foreColor =new Color(0,78,152);
	public static JPanel donePanel;
	
	JTextField tempText,pressureText,scaleText; //Temperature, Pressure, Scale
	
	/*Other Keywords --> Symmetry Options */
	public static JScrollPane scroll;
	public static DefaultTableModel dm;
	public static JTable table;
	public static JFrame symmetryFrame;
	public static JTextField symmetryPGText;
	public static JComboBox symmetryAxisCombo;
	static JRadioButton[] symmetryOptions; //Options for Symmetry
	static ButtonGroup symmetryGroup;
	public static JButton symmetryDone;
	public static String options[] = { "Int", "NoInt", "Grad","NoGrad", "SCF","NoSCF",
			"Loose","Tight","Follow","PG = ","Axis","On"};
	
	
	/*Other Keywords --> SPARSE Options */
	public static JFrame sparseFrame;
	public static JTextField sparseNText;
	//static JRadioButton[] sparseOptions; //Options for Symmetry
	static JCheckBox[] sparseOptions=new JCheckBox[4];
	static ButtonGroup sparseGroup;
	public static JButton sparseDone;
	public static String sparseoptions[] = { "Loose", "Medium", "Tight","N"};
	
	
	/*Other Keywords --> PUNCH Options */
	public static JFrame punchFrame;
	static JCheckBox[] punchOptions=new JCheckBox[9];
	static ButtonGroup punchGroup;
	public static JButton punchDone;
	public static String punchoptions[] = { "Archive", "Title", "Coord","Derivatives","MO","NaturalOrbitals","HondoInput","GAMESSInput","All"};
	
	/*Other Keywords --> PSEUDO Options */
	public static JFrame pseudoFrame;
	public static String pseudooptions[] = { "Read", "Old", "CHF","SHC","LANL1","LANL2"};
	static JCheckBox[] pseudoOptions=new JCheckBox[6];
	static ButtonGroup pseudoGroup;
	public static JButton pseudoDone;
	
	
	/*Other Keywords --> PROP Options */
	public static JFrame propFrame;
	public static String propoptions[] = { "EFG", "Potential", "Field","EPR","Read","Opt","FitCharge","Dipole","Grid","(Read,Opt)"};
	static JCheckBox[] propOptions=new JCheckBox[10];
	static ButtonGroup propertyGroup;
	static ButtonGroup propInputGroup;
	public static JButton propDone;
	
	/*Other Keywords --> Output Options */
	public static JFrame outputFrame;
	public static JTextField outputWFNText;
	static JCheckBox[] outputOptions=new JCheckBox[3];
	static ButtonGroup outputGroup;
	public static JButton outputDone;
	public static String outputoptions[] = {"WFN=", "Pickett", "ReadAtoms"};
	
	//Other Keywords-Pressure options
	public static String pressureOpt,scale_Opt,temp_Opt;
	
	
	public static JButton doneButton,clearButton,exitButton;
	
	public static JFrame otherKeyFrame;
	public static JFrame chargeFrame,constantsFrame,counterFrame,cphfFrame,densityFrame,fmmFrame,nmrFrame;
	public static JRadioButton angOpt,bohrOpt,soOpt,checkOpt,con98Opt,con86Opt,con79Opt,newOpt,oldOpt;
	public static JButton chargeOK,constantOK,counterOK;
	
	
	
	//to be integrated
	//public static JTable iopTable;
	public static JRadioButton gridOpt,rdfOpt,eqsOpt,simulOpt,sepOpt,xyOpt,zveOpt,aoOpt,moOpt,maxOpt,conOpt,canOpt,modOpt,iterOpt,convOpt,invOpt;
	public static JRadioButton lmaxOpt,levelOpt,tolOpt,boxOpt,allnearOpt;
	public static JTextField grid,max,conver,invToler,converDen,lmax,level,tol,box;
	public static JButton cphfOK,densityOK,fmmOK,nmrOk;
	
	public static JRadioButton spinOpt,csgtOpt,giaoOpt,igaimOpt,singleOpt,allOpt,printOpt;
	
	
	
	public static Vector<String> otherKeys;   //should be accessible by the route section
	static int otherFlag;
	public static String chargeOpt = "";     //To be accessible to otherKeyListener   
	//Used to store the option of the Keyword "Charge" 
	//
//Code for Clearing
	
	
	public static Vector<JRadioButton> okVector;

	
	
	public static JRadioButton okArc=new JRadioButton("Archive");
	public static JRadioButton okCha = new JRadioButton("Charge");
	public static JRadioButton okChk = new JRadioButton("ChkBasis");
	public static JRadioButton okCom= new JRadioButton("Complex");
	public static JRadioButton okCon =new JRadioButton("Constants");
	public static JRadioButton okCou = new JRadioButton("CounterPoise");
	public static JRadioButton okCph = new JRadioButton("CPHF");
	public static JRadioButton okDen = new JRadioButton("DensityFit");
	public static JRadioButton okExt = new JRadioButton("External");
	public static JRadioButton okExtB = new JRadioButton("ExtraBasis");
	public static JRadioButton okExtD = new JRadioButton("ExtraDensityBasis");
	public static JRadioButton okFie= new JRadioButton("Field");
	public static JRadioButton okFmm = new JRadioButton("FMM");
	public static JRadioButton okGfi = new JRadioButton("GFInput");
	public static JRadioButton okGfp = new JRadioButton("GFPrint");
	public static JRadioButton okInt = new JRadioButton("Integral");
	public static JRadioButton okIop =new JRadioButton("IOp");
	public static JRadioButton okNam = new JRadioButton("Name");
	public static JRadioButton okOut = new JRadioButton("Output");
	public static JRadioButton okPre = new JRadioButton("Pressure");
	public static JRadioButton okPro = new JRadioButton("Prop");
	public static JRadioButton okPse= new JRadioButton("Pseudo");
	public static JRadioButton okPun = new JRadioButton("Punch");
	public static JRadioButton okSca = new JRadioButton("Scale");
	public static JRadioButton okSpa = new JRadioButton("Sparse");
	public static JRadioButton okSym = new JRadioButton("Symmetry");
	public static JRadioButton okTem =new JRadioButton("Temperature");
	public static JRadioButton okTesM= new JRadioButton("TestMO");
	public static JRadioButton okTra = new JRadioButton("TrackIO");
	public static JRadioButton okTrans = new JRadioButton("Transformation");
	public static JRadioButton okUni = new JRadioButton("Units");
	public static JRadioButton okNmr = new JRadioButton("NMR");
	
	public static Vector otherKey;   //popOpt
	public static int otherKeyListen; // Counts the no. of times table has been initialized
	
	public otherKeyTable(){
		
		otherKeyFrame = new JFrame("Additional Keywords");
		if(otherKeyListen==0)
		{
			okVector = new Vector<JRadioButton>(40);
			okArc.addItemListener(this);
			okArc.setBackground(Color.WHITE);
			okVector.addElement(okArc);
			
			okCha.addItemListener(this);
			okCha.setBackground(Color.WHITE);
			okVector.addElement(okCha);
			
			okChk.addItemListener(this);
			okChk.setBackground(Color.WHITE);
			okVector.addElement(okChk);
			
			okCom.addItemListener(this);
			okCom.setBackground(Color.WHITE);
			okVector.addElement(okCom);
			
			okCon.setBackground(Color.WHITE);
			okCon.addItemListener(this);
			okVector.addElement(okCon);
			
			okCou.setBackground(Color.WHITE);
			okCou.addItemListener(this);
			okVector.addElement(okCou);
			
			okCph.setBackground(Color.WHITE);
			okCph.addItemListener(this);
			okVector.addElement(okCph);
			
			
			okDen.setBackground(Color.WHITE);
			//updated 
			okDen.addItemListener(this);
			okVector.addElement(okDen);
			
			
			okExt.addItemListener(this);
			okExt.setBackground(Color.WHITE);
			okVector.addElement(okExt);
			
			okExtB.addItemListener(this);
			okExtB.setBackground(Color.WHITE);
			okVector.addElement(okExtB);
			
			
			okExtD.addItemListener(this);
			okExtD.setBackground(Color.WHITE);
			okVector.addElement(okExtD);
			
						
			okFie.addItemListener(this);
			okFie.setBackground(Color.WHITE);
			okVector.addElement(okFie);
			okFie.setEnabled(false);
			
			okFmm.addItemListener(this);
			okFmm.setBackground(Color.WHITE);
			okVector.addElement(okFmm);
			
			
			okGfi.addItemListener(this);
			okGfi.setBackground(Color.WHITE);
			okVector.addElement(okGfi);
			
			okGfp.addItemListener(this);
			okGfp.setBackground(Color.WHITE);
			okVector.addElement(okGfp);
			
			
			okInt.addItemListener(this);
			okInt.setBackground(Color.WHITE);
			okVector.addElement(okInt);
			okInt.setEnabled(false);
			okIop.addItemListener(this);
			okIop.setBackground(Color.WHITE);
			okVector.addElement(okIop);
						
			okNam.addItemListener(this);
			okNam.setBackground(Color.WHITE);
			okVector.addElement(okNam);
			
			okOut.addItemListener(this);
			okOut.setBackground(Color.WHITE);
			okVector.addElement(okOut);
			
						
			okPre.addItemListener(this);
			okPre.setBackground(Color.WHITE);
			okVector.addElement(okPre);
			
			
			okPro.addItemListener(this);
			okPro.setBackground(Color.WHITE);
			okVector.addElement(okPro);
			
			okPse.addItemListener(this);
			okPse.setBackground(Color.WHITE);
			okVector.addElement(okPse);
			
			
			okPun.addItemListener(this);
			okPun.setBackground(Color.WHITE);
			okVector.addElement(okPun);
			okSca.addItemListener(this);
			okSca.setBackground(Color.WHITE);
			okVector.addElement(okSca);
			okSpa.addItemListener(this);
			okSpa.setBackground(Color.WHITE);
			okVector.addElement(okSpa);
			okSym.addItemListener(this);
			okSym.setBackground(Color.WHITE);
			okVector.addElement(okSym);
			okTem.addItemListener(this);
			okTem.setBackground(Color.WHITE);
			okVector.addElement(okTem);
			okTesM.addItemListener(this);
			okTesM.setBackground(Color.WHITE);
			okVector.addElement(okTesM);
			okTesM.setEnabled(false);
			okTra.addItemListener(this);
			
			okTra.setBackground(Color.WHITE);
			okVector.addElement(okTra);
			okTrans.addItemListener(this);
			okTra.setEnabled(false);
			okTrans.setBackground(Color.WHITE);
			okVector.addElement(okTrans);
			okUni.addItemListener(this);
			okTrans.setEnabled(false);
			okUni.setBackground(Color.WHITE);
			okVector.addElement(okUni);
			okUni.setEnabled(false);
			//			updated
			okNmr.addItemListener(this);
			okNmr.setBackground(Color.WHITE);
			okVector.addElement(okNmr);
			otherKeyListen++;
		}
		
		otherKeyFrame.setBackground(bgColor);
		otherKeyFrame.setForeground(foreColor);
		dm = new DefaultTableModel();
		dm.setDataVector(
				new Object[][]{
						{okArc,okCha,okChk,okCom,okCon},
						{okCou,okCph,okDen,okExt,okExtB},
						{okExtD,okFie,okFmm,okGfi,okGfp},
						{okInt,okIop,okNam,okOut,okPre},
						{okPro,okPse,okPun,okSca,okSpa},
						{okSym,okTem,okTesM,okTra,okTrans},
						{okUni,okNmr,null,null,null},},
						new Object[]{"1","2","3","4","5"});
		
	    table = new JTable(dm) {
			public void tableChanged(TableModelEvent e) {
				super.tableChanged(e);
				repaint();
			}
		};
		
		table.getColumn("1").setCellRenderer(new RadioButtonRenderer());
		table.getColumn("1").setCellEditor(new RadioButtonEditor(new JCheckBox()));
		
		table.getColumn("2").setCellRenderer(new RadioButtonRenderer());
		table.getColumn("2").setCellEditor(new RadioButtonEditor(new JCheckBox()));
		table.getColumn("3").setCellRenderer(new RadioButtonRenderer());
		table.getColumn("3").setCellEditor(new RadioButtonEditor(new JCheckBox()));
		
		table.getColumn("4").setCellRenderer(new RadioButtonRenderer());
		table.getColumn("4").setCellEditor(new RadioButtonEditor(new JCheckBox()));
		table.getColumn("5").setCellRenderer(new RadioButtonRenderer());
		table.getColumn("5").setCellEditor(new RadioButtonEditor(new JCheckBox()));
		
		
		
		
		doneButton = new JButton("Done");
		doneButton.addActionListener(new otherKeyListener());
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new otherKeyListener());
		exitButton = new JButton("Exit");
		exitButton.addActionListener(new otherKeyListener());
		
		donePanel = new JPanel();
		donePanel.add(doneButton);
		donePanel.add(clearButton);
		donePanel.add(exitButton);
		donePanel.setBackground(bgColor);
		donePanel.setForeground(foreColor);
		table.getTableHeader().setReorderingAllowed(false);
		otherKeyFrame.getContentPane().setLayout(new BorderLayout());
		 scroll= new JScrollPane(table);
		otherKeyFrame.getContentPane().add( scroll,BorderLayout.CENTER);
		otherKeyFrame.getContentPane().add(donePanel,BorderLayout.SOUTH);
		otherKeyFrame.setSize( 600,200);
		otherKeyFrame.setLocation(200,75);
		otherKeyFrame.setVisible(true);
	}
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==exitButton)
		{
			otherKeyFrame.dispose();
		}
		
		if(ae.getSource()==doneButton)
		{
			
			
			
		}
		
	}
	
	
	public void itemStateChanged(ItemEvent e)
	{
		
		if(
				(e.getItem()==okArc)||
				(e.getItem()==okCha)||
				(e.getItem()==okChk)||
				(e.getItem()==okCom)||
				(e.getItem()==okCon)||
				(e.getItem()==okCou)||
				(e.getItem()==okCph)||   
				(e.getItem()==okDen)||
				(e.getItem()==okExt)||
				(e.getItem()==okExtB)||
				(e.getItem()==okExtD)||
				(e.getItem()==okFie)||
				(e.getItem()==okFmm)||
				(e.getItem()==okGfi)||
				(e.getItem()==okGfp)||
				(e.getItem()==okInt)||
				(e.getItem()==okIop)||
				(e.getItem()==okNam)||
				(e.getItem()==okOut)||
				(e.getItem()==okPre)||
				(e.getItem()==okPro)||
				(e.getItem()==okPse)||
				(e.getItem()==okPun)||
				(e.getItem()==okSca)||
				(e.getItem()==okSpa)||
				(e.getItem()==okSym)|| 
				(e.getItem()==okTem)||
				(e.getItem()==okTesM)||
				(e.getItem()==okTra)||
				(e.getItem()==okTrans)||
				(e.getItem()==okUni)||
				(e.getItem()==okNmr)
		)
			
		{
			
			if(((JRadioButton)e.getItem()).isSelected())
			{
				if(RadioButtonEditor.button.getActionCommand()=="Archive" && (okArc.isSelected()==true))
				{
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					otherKeyTable.otherKeys.addElement("Archive");
					if(!(InsertNode.nodeExists("Archive")))
						InsertNode.insertNode("Key","Archive");
					
				}	
				
				if(RadioButtonEditor.button.getActionCommand()=="Charge" && (okCha.isSelected()==true))
				{
					if(okCha.isSelected()==true){
						// Charge is selected popup a window with the list of options
						
						JFrame.setDefaultLookAndFeelDecorated(true);
						chargeFrame = new JFrame("Options for Charge");
						JPanel panel1 = new JPanel(new GridBagLayout());
						GridBagConstraints c  = new GridBagConstraints();
						
						JLabel optionLabel=new JLabel("Select an option");
						angOpt = new JRadioButton("Angstroms");
						bohrOpt = new JRadioButton("Bohrs");
						soOpt = new JRadioButton("StandardOrientation");
						soOpt.addItemListener(new otherKeyListener());
						checkOpt = new JRadioButton("Check");
						
						chargeOK = new JButton("OK");
						ButtonGroup chargeOpt=new ButtonGroup();
						
						chargeOpt.add(angOpt);
						chargeOpt.add(bohrOpt); 
						chargeOpt.add(soOpt);
						chargeOpt.add(checkOpt);
						
						angOpt.setSelected(true);
						
						angOpt.addItemListener(new otherKeyListener());
						bohrOpt.addItemListener(new otherKeyListener());
						checkOpt.addItemListener(new otherKeyListener());
						
						chargeOK.addActionListener(new otherKeyListener());
						c.gridy=0;
						c.gridx=0;
						c.weightx=0.5;
						c.insets= new Insets(10,-20,10,90);
						panel1.add(optionLabel,c);	
						
						c.gridx=0;
						c.gridy=1;
						c.weightx=0.5;
						//						c.weighty=0.15;
						c.insets= new Insets(0,-90,10,90);
						panel1.add(angOpt,c);	
						
						
						c.gridy=2;
						c.gridx=0;
						panel1.add(bohrOpt,c);
						c.gridy=3;
						c.gridx=0;
						panel1.add(soOpt,c);
						c.gridy=4;
						c.gridx=0;
						panel1.add(checkOpt,c);
						c.gridy=5;
						c.gridx=0;
						c.insets = new Insets(0,90,10,90);
						panel1.add(chargeOK,c);
						
						chargeFrame.getContentPane().add(panel1);
						chargeFrame.pack();
						chargeFrame.setLocation(200,200);
						chargeFrame.setSize(250,225);
						chargeFrame.setVisible(true);
						chargeFrame.setResizable(false);
						chargeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);}
				}
				if(RadioButtonEditor.button.getActionCommand()=="ChkBasis" && (okChk.isSelected()==true))
				{	
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.chkBasis);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+G03Input.otherKeyToolTip.chkBasis);
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherFlag ++;
					}
					otherKeyTable.otherKeys.addElement("ChkBasis");
					
					//Insert into the Tree
					InsertNode.insertNode("Key",RadioButtonEditor.button.getActionCommand());
				}
				
				//To be integrated
				if(RadioButtonEditor.button.getActionCommand()=="Complex" && (okCom.isSelected()==true))
				{	
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.complex);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					// G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+G03Input.otherKeyToolTip.complex);
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherFlag ++;
					}
					otherKeyTable.otherKeys.addElement("Complex");
					
					//Insert into the Tree
					InsertNode.insertNode("Key",RadioButtonEditor.button.getActionCommand());
				}
				
				if(RadioButtonEditor.button.getActionCommand()=="Constants" && (okCon.isSelected()==true))
				{
					// Charge is selected popup a window with the list of options
					
					JFrame.setDefaultLookAndFeelDecorated(true);
					constantsFrame = new JFrame("Options for Constants");
					JPanel panel1 = new JPanel(new GridBagLayout());
					GridBagConstraints c  = new GridBagConstraints();
					
					JLabel optionLabel=new JLabel("Select an option");
					con98Opt = new JRadioButton("1998");
					con86Opt = new JRadioButton("1986");
					con79Opt = new JRadioButton("1979");
					
					constantOK = new JButton("OK");
					ButtonGroup constantOpt=new ButtonGroup();
					
					constantOpt.add(con98Opt);
					constantOpt.add(con86Opt); 
					constantOpt.add(con79Opt);
					
					
					con98Opt.setSelected(true);
					
					con98Opt.addItemListener(new otherKeyListener());
					con86Opt.addItemListener(new otherKeyListener());
					con79Opt.addItemListener(new otherKeyListener());
					
					constantOK.addActionListener(new otherKeyListener());
					c.gridy=0;
					c.gridx=0;
					c.weightx=0.5;
					c.insets= new Insets(10,-20,10,90);
					panel1.add(optionLabel,c);	
					
					c.gridx=0;
					c.gridy=1;
					c.weightx=0.5;
					//					c.weighty=0.15;
					c.insets= new Insets(0,-90,10,90);
					panel1.add(con98Opt,c);	
					
					c.gridy=2;
					c.gridx=0;
					panel1.add(con86Opt,c);
					c.gridy=3;
					c.gridx=0;
					panel1.add(con79Opt,c);
					c.gridy=4;
					c.gridx=0;
					c.insets = new Insets(0,90,10,90);
					panel1.add(constantOK,c);
					
					constantsFrame.getContentPane().add(panel1);
					constantsFrame.pack();
					constantsFrame.setLocation(200,200);
					constantsFrame.setSize(250,225);
					constantsFrame.setVisible(true);
					constantsFrame.setResizable(false);
					constantsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				
				if(RadioButtonEditor.button.getActionCommand()=="CounterPoise" && (okCou.isSelected()==true))
				{
					// Charge is selected popup a window with the list of options
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.counter);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+G03Input.otherKeyToolTip.counter);
					JFrame.setDefaultLookAndFeelDecorated(true);
					counterFrame = new JFrame("Options for Counterpoise");
					JPanel panel1 = new JPanel(new GridBagLayout());
					GridBagConstraints c  = new GridBagConstraints();
					
					JLabel optionLabel=new JLabel("Select an option");
					newOpt = new JRadioButton("NewGhost");
					oldOpt = new JRadioButton("OldGhost");
					
					
					counterOK = new JButton("OK");
					ButtonGroup counterOpt=new ButtonGroup();
					
					counterOpt.add(newOpt);
					counterOpt.add(oldOpt); 
					newOpt.setSelected(true);
					newOpt.addItemListener(new otherKeyListener());
					oldOpt.addItemListener(new otherKeyListener());
					counterOK.addActionListener(new otherKeyListener());
					
					c.gridy=0;
					c.gridx=0;
					c.weightx=0.5;
					c.insets= new Insets(10,-20,10,90);
					panel1.add(optionLabel,c);	
					
					c.gridx=0;
					c.gridy=1;
					c.weightx=0.5;
					//					c.weighty=0.15;
					c.insets= new Insets(0,-90,10,90);
					panel1.add(newOpt,c);	
					
					c.gridy=2;
					c.gridx=0;
					panel1.add(oldOpt,c);
					c.gridy=3;
					c.gridx=0;
					c.insets = new Insets(0,90,10,90);
					panel1.add(counterOK,c);
					
					counterFrame.getContentPane().add(panel1);
					counterFrame.pack();
					counterFrame.setLocation(200,200);
					counterFrame.setSize(250,190);
					counterFrame.setVisible(true);
					counterFrame.setResizable(false);
					counterFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				
				//CPHF
				if(RadioButtonEditor.button.getActionCommand()=="CPHF" && (okCph.isSelected()==true))
				{
					// Charge is selected popup a window with the list of options
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+G03Input.otherKeyToolTip.cphf);
					JFrame.setDefaultLookAndFeelDecorated(true);
					cphfFrame = new JFrame("Options for CPHF");
					JPanel panel1 = new JPanel(new GridLayout(2,5));
					JPanel panel2 = new JPanel(new FlowLayout());
					JPanel panel3 = new JPanel(new FlowLayout());
					GridBagConstraints c = new GridBagConstraints();
					
					JLabel l1 = new JLabel(" = ");
					JLabel l2 = new JLabel(" = ");
					JLabel l3 = new JLabel(" = ");
					
					
					JLabel optionLabel = new JLabel("Select an option");
					gridOpt = new JRadioButton("Grid");
					grid = new JTextField(10);
					//grid.setEditable(false);
					rdfOpt = new JRadioButton("RdFreq");
					eqsOpt = new JRadioButton("EqSolv");
					simulOpt = new JRadioButton("Simultaneous");
					sepOpt = new JRadioButton("Separate");
					xyOpt  = new JRadioButton("XY");
					zveOpt = new JRadioButton("ZVector");
					aoOpt = new JRadioButton("AO");
					moOpt = new JRadioButton("MO");
					maxOpt = new JRadioButton("MaxInv");
					max = new JTextField(10);
					//max.setEditable(false);
					conOpt = new JRadioButton("Conver");
					conver = new JTextField(10);
					//conver.setEditable(false);
					canOpt = new JRadioButton("Canonical");
					modOpt = new JRadioButton("MOD");
					
					
					cphfOK = new JButton("OK");
					ButtonGroup cphfOpt=new ButtonGroup();
					
					cphfOpt.add(gridOpt);
					cphfOpt.add(rdfOpt);
					cphfOpt.add(eqsOpt);
					cphfOpt.add(simulOpt);
					cphfOpt.add(sepOpt);
					cphfOpt.add(xyOpt);
					cphfOpt.add(zveOpt);
					cphfOpt.add(aoOpt);
					cphfOpt.add(moOpt);
					cphfOpt.add(maxOpt);
					cphfOpt.add(conOpt);
					cphfOpt.add(canOpt);
					cphfOpt.add(modOpt);
					
					gridOpt.addItemListener(new otherKeyListener());
					rdfOpt.addItemListener(new otherKeyListener());
					eqsOpt.addItemListener(new otherKeyListener());
					simulOpt.addItemListener(new otherKeyListener());
					sepOpt.addItemListener(new otherKeyListener());
					xyOpt.addItemListener(new otherKeyListener());
					zveOpt.addItemListener(new otherKeyListener());
					aoOpt.addItemListener(new otherKeyListener());
					moOpt.addItemListener(new otherKeyListener());
					maxOpt.addItemListener(new otherKeyListener());
					conOpt.addItemListener(new otherKeyListener());
					canOpt.addItemListener(new otherKeyListener());
					modOpt.addItemListener(new otherKeyListener());
					panel2.add(gridOpt);
					panel2.add(l1);
					panel2.add(grid);
					panel2.add(maxOpt);
					panel2.add(l2);
					panel2.add(max);
					panel2.add(conOpt);
					panel2.add(l3);
					panel2.add(conver);
					panel3.add(cphfOK);
					cphfOK.addActionListener(new otherKeyListener());
					panel1.add(rdfOpt);		
					panel1.add(eqsOpt);
					panel1.add(simulOpt);
					panel1.add(sepOpt);
					panel1.add(xyOpt);
					panel1.add(zveOpt);
					panel1.add(aoOpt);
					panel1.add(moOpt);
					panel1.add(canOpt);
					panel1.add(modOpt);
					cphfFrame.getContentPane().setLayout(new BorderLayout());
					cphfFrame.getContentPane().add(panel1,BorderLayout.NORTH);
					cphfFrame.getContentPane().add(panel2,BorderLayout.CENTER);
					cphfFrame.getContentPane().add(panel3,BorderLayout.SOUTH);
					cphfFrame.pack();
					cphfFrame.setLocation(200,200);
					cphfFrame.setSize(620,150);
					cphfFrame.setVisible(true);
					cphfFrame.setResizable(false);
					cphfFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				
				if(RadioButtonEditor.button.getActionCommand()== "DensityFit" && (okDen.isSelected()==true))
				{
					// Charge is selected popup a window with the list of options
					JFrame.setDefaultLookAndFeelDecorated(true);
					densityFrame = new JFrame("Options for DensityFit");
					JPanel panel1 = new JPanel(new GridBagLayout());
					GridBagConstraints c  = new GridBagConstraints();
					JLabel optionLabel = new JLabel("Select an option");
					
					iterOpt = new JRadioButton("Iterative");
					invOpt = new JRadioButton("InvToler");
					convOpt = new JRadioButton("Convergence");
					
					invToler = new JTextField(10);
					converDen = new JTextField(10);
					
					invToler.setEditable(false);
					converDen.setEditable(false);
					JLabel l1 = new JLabel("=");
					JLabel l2 = new JLabel("=");
					
					densityOK = new JButton("OK");
					ButtonGroup densityOpt=new ButtonGroup();
					
					densityOpt.add(iterOpt);
					densityOpt.add(invOpt); 
					densityOpt.add(convOpt);
					
					iterOpt.addItemListener(new otherKeyListener());
					invOpt.addItemListener(new otherKeyListener());
					convOpt.addItemListener(new otherKeyListener());
					
					densityOK.addActionListener(new otherKeyListener());
					c.gridy=0;
					c.gridx=0;
					c.weightx=0.5;
					c.insets= new Insets(10,-20,10,90);
					panel1.add(optionLabel,c);	
					
					c.gridx=0;
					c.gridy=1;
					c.weightx=0.5;
					//					c.weighty=0.15;
					c.insets= new Insets(0,-90,10,90);
					panel1.add(iterOpt,c);	
					
					c.gridy=2;
					c.gridx=0;
					//  c.insets= new Insets(0,-90,10,90);
					panel1.add(invOpt,c);
					c.gridx=1;
					c.insets = new Insets(0,-90,10,90);
					panel1.add(l1,c);
					c.gridx=2;
					c.insets = new Insets(0,-90,10,90);
					panel1.add(invToler,c);
					
					
					c.gridy=3;
					c.gridx=0;
					panel1.add(convOpt,c);
					c.gridx=1;
					c.insets = new Insets(0,-90,10,90);
					panel1.add(l2,c);
					c.gridx=2;
					c.insets = new Insets(0,-90,10,90);
					panel1.add(converDen,c);
					
					
					c.gridy=4;
					c.gridx=0;
					c.insets = new Insets(0,90,10,90);
					panel1.add(densityOK,c);
					
					densityFrame.getContentPane().add(panel1);
					densityFrame.pack();
					densityFrame.setLocation(200,200);
					densityFrame.setSize(365,210);
					densityFrame.setVisible(true);
					densityFrame.setResizable(false);
					densityFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+otherKeyToolTip.densityFit);
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.densityFit);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
				}
				
				
				if(RadioButtonEditor.button.getActionCommand()== "FMM" && (okFmm.isSelected()==true))
				{
					// Charge is selected popup a window with the list of options
					JFrame.setDefaultLookAndFeelDecorated(true);
					fmmFrame = new JFrame("Options for FMM");
					JPanel panel1 = new JPanel(new GridBagLayout());
					GridBagConstraints c  = new GridBagConstraints();
					JLabel optionLabel = new JLabel("Select an option");
					
					
					lmaxOpt = new JRadioButton("Lmax");
					levelOpt = new JRadioButton("Levels");
					tolOpt = new JRadioButton("Tolerance");
					boxOpt = new JRadioButton("BoxLen");
					allnearOpt = new JRadioButton("AllNearField");
					
					lmax = new JTextField(10);
					level = new JTextField(10);
					tol = new JTextField(10);
					box = new JTextField(10);
					
					
					lmax.setEditable(false);
					level.setEditable(false);
					tol.setEditable(false);
					box.setEditable(false);
					
					JLabel l1 = new JLabel("=");
					JLabel l2 = new JLabel("=");
					JLabel l3 = new JLabel("=");
					JLabel l4 = new JLabel("=");
					fmmOK = new JButton("OK");
					ButtonGroup fmmOpt=new ButtonGroup();
					
					fmmOpt.add(lmaxOpt);
					fmmOpt.add(levelOpt); 
					fmmOpt.add(tolOpt);
					fmmOpt.add(boxOpt);
					
					lmaxOpt.addItemListener(new otherKeyListener());
					levelOpt.addItemListener(new otherKeyListener());
					tolOpt.addItemListener(new otherKeyListener());
					boxOpt.addItemListener(new otherKeyListener());
					
					fmmOK.addActionListener(new otherKeyListener());
					c.gridy=0;
					c.gridx=0;
					c.weightx=0.5;
					c.insets= new Insets(10,-20,10,90);
					panel1.add(optionLabel,c);	
					
					c.gridx=0;
					c.gridy=1;
					c.weightx=0.5;
					//					c.weighty=0.15;
					c.insets= new Insets(0,-90,10,90);
					panel1.add(lmaxOpt,c);
					c.gridx=1;
					c.insets= new Insets(0,-90,10,90);
					panel1.add(l1,c);
					c.gridx=2;
					panel1.add(lmax,c);
					
					c.gridy=2;
					c.gridx=0;
					//  c.insets= new Insets(0,-90,10,90);
					panel1.add(levelOpt,c);
					c.gridx=1;
					c.insets = new Insets(0,-90,10,90);
					panel1.add(l2,c);
					c.gridx=2;
					c.insets = new Insets(0,-90,10,90);
					panel1.add(level,c);
					
					
					c.gridy=3;
					c.gridx=0;
					panel1.add(tolOpt,c);
					c.gridx=1;
					c.insets = new Insets(0,-90,10,90);
					panel1.add(l3,c);
					c.gridx=2;
					c.insets = new Insets(0,-90,10,90);
					panel1.add(tol,c);
					
					
					c.gridy=4;
					c.gridx=0;
					panel1.add(boxOpt,c);
					c.gridx=1;
					c.insets = new Insets(0,-90,10,90);
					panel1.add(l4,c);
					c.gridx=2;
					c.insets = new Insets(0,-90,10,90);
					panel1.add(box,c);
					
					
					c.gridy=5;
					c.gridx=0;
					panel1.add(allnearOpt,c);
					
					c.gridy=6;
					c.gridx=0;
					c.insets = new Insets(0,90,10,90);
					panel1.add(fmmOK,c);
					
					fmmFrame.getContentPane().add(panel1);
					fmmFrame.pack();
					fmmFrame.setLocation(200,200);
					fmmFrame.setSize(375,285);
					fmmFrame.setVisible(true);
					fmmFrame.setResizable(false);
					fmmFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.fmm);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+otherKeyToolTip.fmm);
				}
				if(RadioButtonEditor.button.getActionCommand()=="NMR" && (okNmr.isSelected()==true))
				{
					
					JFrame.setDefaultLookAndFeelDecorated(true);
					JPanel panel1 = new JPanel();
					JPanel panel2 = new JPanel();
					
					nmrFrame = new JFrame("Select Options for NMR");
					nmrOk = new JButton("OK");
					
					spinOpt = new JRadioButton("SpinSpin");
					csgtOpt = new JRadioButton("CSGT");
					giaoOpt = new JRadioButton("GIAO");
					igaimOpt = new JRadioButton("IGAIM");
					singleOpt = new JRadioButton("SingleOrigin");
					allOpt  = new JRadioButton("All");
					printOpt = new JRadioButton("PrintEigenvectors");
					ButtonGroup nmrOpt=new ButtonGroup();
					
					nmrOpt.add(spinOpt);
					nmrOpt.add(csgtOpt);
					nmrOpt.add(giaoOpt);
					nmrOpt.add(igaimOpt);
					nmrOpt.add(singleOpt);
					nmrOpt.add(allOpt);
					nmrOpt.add(printOpt);
					
					spinOpt.addItemListener(new otherKeyListener());
					csgtOpt.addItemListener(new otherKeyListener());
					giaoOpt.addItemListener(new otherKeyListener());
					igaimOpt.addItemListener(new otherKeyListener());
					singleOpt.addItemListener(new otherKeyListener());
					allOpt.addItemListener(new otherKeyListener());
					printOpt.addItemListener(new otherKeyListener());
					
					
					panel1.add(spinOpt);
					panel1.add(csgtOpt);
					panel1.add(giaoOpt);
					panel1.add(igaimOpt);
					panel1.add(singleOpt);
					panel1.add(allOpt);
					panel1.add(printOpt);
					
					nmrOk.addActionListener(new otherKeyListener());
					panel2.add(nmrOk);
					nmrFrame.getContentPane().setLayout(new BorderLayout());
					nmrFrame.getContentPane().add(panel1,BorderLayout.CENTER);
					nmrFrame.getContentPane().add(panel2,BorderLayout.SOUTH);
					nmrFrame.pack();
					nmrFrame.setLocation(200,200);
					nmrFrame.setSize(520,150);
					nmrFrame.setVisible(true);
					nmrFrame.setResizable(false);
					nmrFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				
				if(RadioButtonEditor.button.getActionCommand()=="External" && (okExt.isSelected()==true))
				{
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.external);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					otherKeyTable.otherKeys.addElement("External");
					if(!(InsertNode.nodeExists("External")))
						InsertNode.insertNode("Key","External");
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+otherKeyToolTip.external);
				}
				if(RadioButtonEditor.button.getActionCommand()=="ExtraBasis" && (okExtB.isSelected()==true))
				{
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.extrabasis);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					otherKeyTable.otherKeys.addElement("ExtraBasis");
					if(!(InsertNode.nodeExists("ExtraBasis")))
						InsertNode.insertNode("Key","ExtraBasis");
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+otherKeyToolTip.extrabasis);
				}	
				if(RadioButtonEditor.button.getActionCommand()=="ExtraDensityBasis" && (okExtD.isSelected()==true))
				{
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.extradensitybasis);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					otherKeyTable.otherKeys.addElement("ExtraDensityBasis");
					if(!(InsertNode.nodeExists("ExtraDensityBasis")))
						InsertNode.insertNode("Key","ExtraDensityBasis");
					
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+otherKeyToolTip.extradensitybasis);
				}	
				
				if(RadioButtonEditor.button.getActionCommand()=="GFInput" && (okGfi.isSelected()==true))
				{
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.GFInput);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					otherKeyTable.otherKeys.addElement("GFInput");
					if(!(InsertNode.nodeExists("GFInput")))
						InsertNode.insertNode("Key","GFInput");
					//	G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+otherKeyToolTip.GFInput);
				}
				
				if(RadioButtonEditor.button.getActionCommand()=="GFPrint" && (okGfp.isSelected()==true))
				{
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					otherKeyTable.otherKeys.addElement("GFPrint");
					if(!(InsertNode.nodeExists("GFPrint")))
						InsertNode.insertNode("Key","GFPrint");
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.GFPrint);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					
					
					
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+otherKeyToolTip.GFPrint);
				}
				
				if(RadioButtonEditor.button.getActionCommand()=="Name" && (okNam.isSelected()==true))
				{
						try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.Name);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					
					
					
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					otherKeyTable.otherKeys.addElement("Name");
					if(!(InsertNode.nodeExists("Name")))
						InsertNode.insertNode("Key","Name");
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+otherKeyToolTip.Name);
				}
			
				if(RadioButtonEditor.button.getActionCommand()=="Integral" && (okInt.isSelected()==true))
				{
					
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.Integral);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					otherKeyTable.otherKeys.addElement("Integral");
					if(!(InsertNode.nodeExists("Integral")))
						InsertNode.insertNode("Key","Integral");
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+otherKeyToolTip.Integral);
				}	

				
				
			//	if(RadioButtonEditor.button.getActionCommand()=="IOp" && (okIop.isSelected()==false))
				
				
				
				
				if(RadioButtonEditor.button.getActionCommand()=="IOp" && (okIop.isSelected()==true))
				{
					
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					//if(iopKeymodify.iopFlag==0)
				//{
				
					new iopKeymodify();
				//}
				//else
					//{
						//iopKeymodify.iopFrame.show();
				//	} 
			/*		if(!(InsertNode.nodeExists("Iop")))
						InsertNode.insertNode("Key","Iop");        */
					
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.IOp);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				//**************************
				//Paste the other ifz here
				//**************************
				
				
				//				To be integrated
				if(RadioButtonEditor.button.getActionCommand()=="Temperature")
				{	
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+G03Input.otherKeyToolTip.tempTip);
					
					
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.tempTip);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					
					
					
					tempText=new JTextField(5);
					tempText.setText("298.15");
					String message="Enter the value of Temperature";
					int result = JOptionPane.showOptionDialog(this,
							new Object[] { message, tempText},
							"Temperature=N", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null, null, null);
					if(result==0)
					{
						if(otherKeyTable.otherFlag==0)
						{
							otherKeyTable.otherKeys = new Vector<String>();
							otherFlag ++;
						}
						temp_Opt = "Temperature="+tempText.getText();
						otherKeyTable.otherKeys.addElement("Temperature="+tempText.getText());
						
						//Insert into the Tree
						InsertNode.insertNode("Key",RadioButtonEditor.button.getActionCommand());
					}
					
					else //Cancel Option
					{
						otherKeyTable.okTem.setSelected(false);
					}
					
				}
				
				//SYMMETRY
				if(RadioButtonEditor.button.getActionCommand()=="Symmetry")
				{
					symmetryFrame=new JFrame("Options for Symmetry");
					
					
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,otherKeyToolTip.symmetryTip);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					
					
					
					//G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+G03Input.otherKeyToolTip.symmetryTip);
					symmetryPGText = new JTextField(10);
					symmetryAxisCombo=new JComboBox();
					symmetryDone=new JButton("Done");
					symmetryAxisCombo.addItem("X");
					symmetryAxisCombo.addItem("Y");
					symmetryAxisCombo.addItem("Z");
					
					
					symmetryOptions= new JRadioButton[12];
					symmetryGroup = new ButtonGroup();
					for(int i=0;i<symmetryOptions.length;i++)
					{
						symmetryOptions[i]=new JRadioButton(options[i]);
						symmetryGroup.add(symmetryOptions[i]);
					}
					
					JPanel symmetryPanel=new JPanel();
					JPanel symmetryMainPanel=new JPanel(new BorderLayout());
					symmetryPGText.setEnabled(false);
					symmetryAxisCombo.setEnabled(false);
					symmetryPanel.setLayout(new GridLayout(2,4));
					
					
					for(int i=0;i<10;i++)
					{
						symmetryPanel.add(symmetryOptions[i]);
						symmetryOptions[i].addItemListener(new otherKeyListener());
					}
					
					
					symmetryPanel.add(symmetryPGText);
					symmetryPanel.add(symmetryOptions[10]);
					symmetryPanel.add(symmetryAxisCombo);
					symmetryPanel.add(symmetryOptions[11]);
					symmetryOptions[10].addItemListener(new otherKeyListener());
					symmetryOptions[11].addItemListener(new otherKeyListener());
					symmetryAxisCombo.addItemListener(new otherKeyListener());
					symmetryDone.addActionListener(new otherKeyListener());
					
					symmetryMainPanel.add(symmetryDone,BorderLayout.SOUTH);
					symmetryMainPanel.add(symmetryPanel,BorderLayout.CENTER);
					
					symmetryFrame.getContentPane().add(symmetryMainPanel);
					symmetryFrame.setSize(600,100);
					symmetryFrame.setVisible(true);
					symmetryFrame.setResizable(false);
					symmetryFrame.setLocation(100,100);
					
				}
				
				//If it is Sparse
				if(RadioButtonEditor.button.getActionCommand()=="Sparse")
				{
					
					if(okSpa.isSelected()==true){	
						sparseFrame=new JFrame("Options for Sparse");
						
						try {
							G03MenuTree.insertHTML(G03MenuTree.keyoptArea,G03Input.otherKeyToolTip.sparseTip);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						sparseNText = new JTextField(10);
						
						sparseDone=new JButton("Done");
						
						
						
						//sparseOptions= new JRadioButton[4];
						
						sparseGroup = new ButtonGroup();
						for(int i=0;i<sparseOptions.length;i++)
						{
							//sparseOptions[i]=new JRadioButton(sparseoptions[i]);
							sparseOptions[i]=new JCheckBox(sparseoptions[i]);
							sparseGroup.add(sparseOptions[i]);
						}
						
						JPanel sparsePanel=new JPanel(new GridBagLayout());
						GridBagConstraints c= new GridBagConstraints();
						c.gridx=0;
						c.gridy=0;
						JPanel sparseMainPanel=new JPanel(new BorderLayout());
						sparseNText.setEnabled(true);
						
						
						
						for(int i=0;i<4;i++)
						{
							if(i==2)
							{
								c.gridx=0;c.gridy=1;
							}
							sparsePanel.add(sparseOptions[i],c);
							c.gridx++;
							sparseOptions[i].addItemListener(new otherKeyListener());
						}
						
						
						sparsePanel.add(sparseNText,c);
						
						
						
						
						sparseDone.addActionListener(new otherKeyListener());
						
						sparseMainPanel.add(sparseDone,BorderLayout.SOUTH);
						sparseMainPanel.add(sparsePanel,BorderLayout.CENTER);
						
						sparseFrame.getContentPane().add(sparseMainPanel);
						sparseFrame.setSize(300,150);
						sparseFrame.setVisible(true);
						sparseFrame.setResizable(false);
						sparseFrame.setLocation(100,100);
					}
				}
				
				
				// For Scale=N
				
				if(RadioButtonEditor.button.getActionCommand()=="Scale")
				{	
					
					//Display the message in Help Area
					
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,G03Input.otherKeyToolTip.scaleTip);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					
					
					scaleText=new JTextField(5);
					String message="Enter the value of Scale";
					int result = JOptionPane.showOptionDialog(this,
							new Object[] { message, scaleText},
							"Scale=N", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null, null, null);
					if(result==0)
					{
						if(scaleText.getText().length()==0)
						{
							JOptionPane.showMessageDialog(this,"Please enter the value of N","ERROR",JOptionPane.ERROR_MESSAGE);
							otherKeyTable.okSca.setSelected(false);
						}
						else{ 	
							if(otherKeyTable.otherFlag==0)
							{
								otherKeyTable.otherKeys = new Vector<String>();
								otherFlag ++;
							}
							scale_Opt = "Scale="+scaleText.getText();
							otherKeyTable.otherKeys.addElement(scale_Opt);
							
							//Insert into the Tree
							InsertNode.insertNode("Key",RadioButtonEditor.button.getActionCommand());
						}
					}
					
					else //Cancel Option
					{
						otherKeyTable.okSca.setSelected(false);
					}
					
				}
				
				//If it is PUNCH
				
				if(RadioButtonEditor.button.getActionCommand()=="Punch")
				{
					punchFrame=new JFrame("Options for Punch");
					
					//					Display the message in Help Area
					
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,G03Input.otherKeyToolTip.punchTip);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					
					punchDone=new JButton("Done");
					//punchOptions= new JCheckBox[9];
					punchGroup = new ButtonGroup();
					for(int i=0;i<punchOptions.length;i++)
					{
						punchOptions[i]=new JCheckBox(punchoptions[i]);
						punchGroup.add(punchOptions[i]);
					}
					
					JPanel punchPanel=new JPanel();
					JPanel punchMainPanel=new JPanel(new BorderLayout());
					punchPanel.setLayout(new GridLayout(2,4));
					
					
					for(int i=0;i<punchOptions.length;i++)
					{
						punchPanel.add(punchOptions[i]);
						punchOptions[i].addItemListener(new otherKeyListener());
					}
					
					punchDone.addActionListener(new otherKeyListener());
					
					punchMainPanel.add(punchDone,BorderLayout.SOUTH);
					punchMainPanel.add(punchPanel,BorderLayout.CENTER);
					
					
					punchFrame.getContentPane().add(punchMainPanel);
					punchFrame.setSize(600,100);
					punchFrame.setVisible(true);
					punchFrame.setResizable(false);
					punchFrame.setLocation(100,100);
					
				}
				
				//				If it is PSEUDO
				
				if(RadioButtonEditor.button.getActionCommand()=="Pseudo")
				{
					pseudoFrame=new JFrame("Options for Pseudo");
					
					//					Display the message in Help Area
					
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,G03Input.otherKeyToolTip.pseudoTip);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					
					pseudoDone=new JButton("Done");
					//pseudoOptions= new JCheckBox[6];
					pseudoGroup = new ButtonGroup();
					for(int i=0;i<pseudoOptions.length;i++)
					{
						pseudoOptions[i]=new JCheckBox(pseudooptions[i]);
						pseudoGroup.add(pseudoOptions[i]);
					}
					
					JPanel pseudoPanel=new JPanel();
					JPanel pseudoMainPanel=new JPanel(new BorderLayout());
					pseudoPanel.setLayout(new GridLayout(2,3));
					
					
					for(int i=0;i<pseudoOptions.length;i++)
					{
						pseudoPanel.add(pseudoOptions[i]);
						pseudoOptions[i].addItemListener(new otherKeyListener());
					}
					
					pseudoDone.addActionListener(new otherKeyListener());
					
					pseudoMainPanel.add(pseudoDone,BorderLayout.SOUTH);
					pseudoMainPanel.add(pseudoPanel,BorderLayout.CENTER);
					
					pseudoFrame.getContentPane().add(pseudoMainPanel);
					pseudoFrame.setSize(300,100);
					pseudoFrame.setVisible(true);
					pseudoFrame.setResizable(false);
					pseudoFrame.setLocation(100,100);
					
				}
				
				
				
				
				//		If it is PROP
				
				if(RadioButtonEditor.button.getActionCommand()=="Prop")
				{
					propFrame=new JFrame("Options for Prop");
					
					//	Display the message in Help Area
					
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,G03Input.otherKeyToolTip.propTip);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					
					propDone=new JButton("Done");
					//pseudoOptions= new JCheckBox[6];
					propertyGroup = new ButtonGroup();
					propInputGroup=new ButtonGroup();
					
					for(int i=0;i<4;i++)
					{
						propOptions[i]=new JCheckBox(propoptions[i]);
						propertyGroup.add(propOptions[i]);
					}
					
					for(int i=4;i<10;i++)
					{
						propOptions[i]=new JCheckBox(propoptions[i]);
						propInputGroup.add(propOptions[i]);
					}
					
					JPanel propLeftPanel=new JPanel();
					JPanel propRightPanel=new JPanel();
					JPanel propMainPanel=new JPanel(new BorderLayout());
					
					propLeftPanel.setLayout(new GridLayout(4,1));
					propRightPanel.setLayout(new GridLayout(6,1));
					
					for(int i=0;i<4;i++)
					{
						propLeftPanel.add(propOptions[i]);
						propOptions[i].addItemListener(new otherKeyListener());
					}
					for(int i=4;i<10;i++)
					{
						propRightPanel.add(propOptions[i]);
						propOptions[i].addItemListener(new otherKeyListener());
					}
					
					propDone.addActionListener(new otherKeyListener());
					
					propMainPanel.add(propDone,BorderLayout.SOUTH);
					propMainPanel.add(propLeftPanel,BorderLayout.WEST);
					propMainPanel.add(propRightPanel,BorderLayout.EAST);
					propFrame.getContentPane().add(propMainPanel);
					propFrame.setSize(200,250);
					propFrame.setVisible(true);
					propFrame.setResizable(false);
					propFrame.setLocation(100,100);
					
				}
				
				// For Pressure=N
				
				if(RadioButtonEditor.button.getActionCommand()=="Pressure")
				{	
					
					//Display the message in Help Area
					
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,G03Input.otherKeyToolTip.pressTip);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					pressureText=new JTextField(5);
					String message="Enter the value of Pressure";
					int result = JOptionPane.showOptionDialog(this,
							new Object[] { message, pressureText},
							"Pressure=N", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null, null, null);
					if(result==0)
					{
						if(pressureText.getText().length()==0)
						{
							JOptionPane.showMessageDialog(this,"Please enter the value of N","ERROR",JOptionPane.ERROR_MESSAGE);
							otherKeyTable.okPre.setSelected(false);
						}
						else{ 	
							if(otherKeyTable.otherFlag==0)
							{
								otherKeyTable.otherKeys = new Vector<String>();
								otherFlag ++;
							}
							pressureOpt = "Pressure="+pressureText.getText();
							otherKeyTable.otherKeys.addElement(pressureOpt);
							
							//Insert into the Tree
							InsertNode.insertNode("Key",RadioButtonEditor.button.getActionCommand());
						}
					}
					
					else //Cancel Option
					{
						otherKeyTable.okPre.setSelected(false);
					}
					
				}
				
				//	If it is Output
				if(RadioButtonEditor.button.getActionCommand()=="Output")
				{
					outputFrame=new JFrame("Options for Output");
					
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,G03Input.otherKeyToolTip.outputTip);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					outputWFNText = new JTextField(10);
					
					outputDone=new JButton("Done");
					
					outputOptions= new JCheckBox[3];
					outputGroup = new ButtonGroup();
					for(int i=0;i<outputOptions.length;i++)
					{
						outputOptions[i]=new JCheckBox(outputoptions[i]);
						outputGroup.add(outputOptions[i]);
					}
					JPanel outputMainPanel=new JPanel(new BorderLayout());
					JPanel outputPanel=new JPanel(new GridBagLayout());
					GridBagConstraints c= new GridBagConstraints();
					c.gridx=0;
					c.gridy=0;
					
					outputWFNText.setEnabled(false);
					
					
					int i=0;
					for(i=0;i<3;i++)
					{
						if(i==1)
						{
							outputPanel.add(outputWFNText,c);
						}
						else{
							outputPanel.add(outputOptions[i],c);
							outputOptions[i].addItemListener(new otherKeyListener());
						}
						
						c.gridx++;
					}
					
					outputPanel.add(outputOptions[1],c);
					outputOptions[1].addItemListener(new otherKeyListener());
					
					outputDone.addActionListener(new otherKeyListener());
					
					outputMainPanel.add(outputDone,BorderLayout.SOUTH);
					outputMainPanel.add(outputPanel,BorderLayout.CENTER);
					
					outputFrame.getContentPane().add(outputMainPanel);
					outputFrame.setSize(450,100);
					outputFrame.setVisible(true);
					outputFrame.setResizable(false);
					outputFrame.setLocation(100,100);
					
				}
				
		} // end of if for isSelected
			else
			{
				if(RadioButtonEditor.button.getActionCommand()=="Archive")
				{
					otherKeys.removeElement("Archive");
					if(InsertNode.nodeExists("Archive"))
						InsertNode.deleteNode("Archive");
				}
				if(RadioButtonEditor.button.getActionCommand()=="External")
				{
					otherKeys.removeElement("External");
					if(InsertNode.nodeExists("External"))
						InsertNode.deleteNode("External");
				}
				if(RadioButtonEditor.button.getActionCommand()=="ExtraBasis")
				{
					otherKeys.removeElement("ExtraBasis");
					if(InsertNode.nodeExists("ExtraBasis"))
						InsertNode.deleteNode("ExtraBasis");
				}
				if(RadioButtonEditor.button.getActionCommand()=="ExtraDensityBasis")
				{
					otherKeys.removeElement("ExtraDensityBasis");
					if(InsertNode.nodeExists("ExtraDensityBasis"))
						InsertNode.deleteNode("ExtraDensityBasis");
				}
				if(RadioButtonEditor.button.getActionCommand()=="GFInput")
				{
					otherKeys.removeElement("GFInput");
					if(InsertNode.nodeExists("GFInput"))
						InsertNode.deleteNode("GFInput");
				}
				if(RadioButtonEditor.button.getActionCommand()=="GFPrint")
				{
					otherKeys.removeElement("GFPrint");
					if(InsertNode.nodeExists("GFPrint"))
						InsertNode.deleteNode("GFPrint");
				}
				if(RadioButtonEditor.button.getActionCommand()=="Complex")
				{
					otherKeys.removeElement("Complex");
					if(InsertNode.nodeExists("Complex"))
						InsertNode.deleteNode("Complex");
				}
				if(RadioButtonEditor.button.getActionCommand()=="CounterPoise")
				{
					otherKeys.removeElement("CounterPoise="+otherKeyListener.counter_Opt);
					if(InsertNode.nodeExists("CounterPoise"))
						InsertNode.deleteNode("CounterPoise");
				}
				if(RadioButtonEditor.button.getActionCommand()=="Charge")
				{
					otherKeys.removeElement("Charge="+otherKeyListener.charge_Opt);
					if(InsertNode.nodeExists("Charge"))
						InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="DensityFit")
				{
					otherKeys.removeElement("DensityFit="+otherKeyListener.densityOpt);
					if(InsertNode.nodeExists("DensityFit"))
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="Name")
				{
					otherKeys.removeElement("Name");
					if(InsertNode.nodeExists("Name"))
						InsertNode.deleteNode("Name");
				}
				
				if(RadioButtonEditor.button.getActionCommand()=="CPHF")
				{
					otherKeys.removeElement("CPHF="+otherKeyListener.cphfOpt);
					if(InsertNode.nodeExists("CPHF"))
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="FMM")
				{
					otherKeys.removeElement("FMM="+otherKeyListener.fmmOpt);
					if(InsertNode.nodeExists("FMM"))
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="IOp")
				{
					otherKeys.removeElement(iopKeymodify.iopOp);
					
					if(InsertNode.nodeExists("IOp"))
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="NMR")
				{
					otherKeys.removeElement("NMR="+otherKeyListener.nmrOpt);
					if(InsertNode.nodeExists("NMR"))
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="Constants")
				{
					otherKeys.removeElement("Constants="+otherKeyListener.con_Opt);
					if(InsertNode.nodeExists("Constants"))
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="ChkBasis")
				{
					otherKeys.removeElement(RadioButtonEditor.button.getActionCommand());
					if(InsertNode.nodeExists("ChkBasis"))
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="Temperature")
				{
					otherKeys.removeElement("Temperature="+tempText.getText());
					if(InsertNode.nodeExists("Temperature"))
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				
				
				
				if(RadioButtonEditor.button.getActionCommand()=="Symmetry")
				{
					System.out.println("Tracking>>>>"+otherKeyListener.symm_Opt);
					otherKeys.removeElement("Symmetry= "+otherKeyListener.symm_Opt);
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="Sparse")
				{
					System.out.println("Tracking>>>>"+otherKeyListener.sparse_Opt);
					//if(okSpa.isSelected()==false)
					//{
					otherKeys.removeElement("Sparse="+otherKeyListener.sparse_Opt);
					RouteClass.writeRoute();
					//}
					if(InsertNode.nodeExists("Sparse"))
						InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
					
				}
				if(RadioButtonEditor.button.getActionCommand()=="Output")
				{
					
					otherKeys.removeElement("Output= "+otherKeyListener.output_Opt);
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="Scale")
				{
					otherKeys.removeElement("Scale="+scaleText.getText());
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="Punch")
				{
					otherKeys.removeElement("Punch= "+otherKeyListener.punch_Opt);
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="Pseudo")
				{
					otherKeys.removeElement("Pseudo= "+otherKeyListener.pseudo_Opt);
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="Prop")
				{
					otherKeys.removeElement("Prop= "+otherKeyListener.prop_Opt);
					System.out.println("PROP OPT "+otherKeyListener.prop_Opt);
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				if(RadioButtonEditor.button.getActionCommand()=="Pressure")
				{
					otherKeys.removeElement("Pressure="+pressureText.getText());
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}
				
				
			/*	else
				{
					otherKeys.removeElement(RadioButtonEditor.button.getActionCommand());
					InsertNode.deleteNode(RadioButtonEditor.button.getActionCommand());
				}    */
			}
			
			
			
			
		}	// End of IF
		
		
	}
	
	
	public static void main(String[] args) {
		otherKeyTable frame = new otherKeyTable();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}


