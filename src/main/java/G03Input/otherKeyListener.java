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
 * @author Sandeep Kumar Seethaapathy  @author Shashank Jeedigunta  
 * 
 */






package G03Input;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import java.util.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;

public class otherKeyListener implements ActionListener,ItemListener{
	
	public static String charge_Opt="Angstroms";// Default option for Charge
	public static String con_Opt = "1998";
	public static String counter_Opt = "NewGhost";
	public static String cphfOpt;
	public static String densityOpt,fmmOpt,nmrOpt;
	public static String output_Opt="",symm_Opt="",sparse_Opt="",punch_Opt="",pseudo_Opt="",prop_Opt="",prop_Opt1="",prop_Opt2="";
	
	
	
	
	public void itemStateChanged(ItemEvent ie) throws NullPointerException
	{
		

		
		
	if(ie.getSource() == otherKeyTable.soOpt)
			{
				if(otherKeyTable.soOpt.isSelected())
				{
				G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+otherKeyToolTip.charSO);
				charge_Opt="Standard Orientation";
				}
				else
			    charge_Opt = "Angstroms";
			}
			if(ie.getSource() == otherKeyTable.angOpt)
			{
				if(otherKeyTable.angOpt.isSelected())
				charge_Opt="Angstroms";   // Default
			}
			if(ie.getSource() == otherKeyTable.bohrOpt)
			{
				if(otherKeyTable.bohrOpt.isSelected())
				{
				charge_Opt="Bohrs";
				}
				else
					charge_Opt = "Angstroms";
			}
			if(ie.getSource() == otherKeyTable.checkOpt)
			{
				if(otherKeyTable.checkOpt.isSelected())
				charge_Opt="Check";
				else
					charge_Opt = "Angstroms";
			}
			
			//to be integrated
			if(ie.getSource() == otherKeyTable.con98Opt)
			{
				if(otherKeyTable.con98Opt.isSelected())
				con_Opt="1998";
			}
			if(ie.getSource() == otherKeyTable.con86Opt)
			{
				if(otherKeyTable.con86Opt.isSelected())
				con_Opt="1986";
			}
			
			if(ie.getSource() == otherKeyTable.con79Opt)
			{
				if(otherKeyTable.con79Opt.isSelected())
				con_Opt="1979";
			}
			
			//counterpoise
			if(ie.getSource() == otherKeyTable.newOpt)
			{
				if(otherKeyTable.newOpt.isSelected())
				counter_Opt="NewGhost";
			}
			if(ie.getSource() == otherKeyTable.oldOpt)
			{
				if(otherKeyTable.oldOpt.isSelected())
				counter_Opt="OldGhost";
			}
						//cphf
			
			if((ie.getSource() == otherKeyTable.rdfOpt)||
					(ie.getSource() == otherKeyTable.eqsOpt)||
					(ie.getSource() == otherKeyTable.simulOpt)||
					(ie.getSource() == otherKeyTable.sepOpt)||
					(ie.getSource() == otherKeyTable.xyOpt)||
					(ie.getSource() == otherKeyTable.zveOpt)||
					(ie.getSource() == otherKeyTable.aoOpt)||
					(ie.getSource() == otherKeyTable.moOpt)||
					(ie.getSource() == otherKeyTable.canOpt)||
					(ie.getSource() == otherKeyTable.modOpt))
			{
				if(((JRadioButton)ie.getItem()).isSelected())
				cphfOpt = new String((((JRadioButton)ie.getItem()).getLabel()));
				else
					cphfOpt="";
			}
			if((ie.getSource() == otherKeyTable.gridOpt))
			{
				if(((JRadioButton)ie.getItem()).isSelected())
				otherKeyTable.grid.setEditable(true);
			   	cphfOpt = "Grid";
			}
			if((ie.getSource() == otherKeyTable.maxOpt))
			{
				if(((JRadioButton)ie.getItem()).isSelected())
				otherKeyTable.max.setEditable(true);
				cphfOpt = "MaxInv";
			}
			
			if((ie.getSource() == otherKeyTable.conOpt))
			{
				if(((JRadioButton)ie.getItem()).isSelected())
				otherKeyTable.conver.setEditable(true);
				cphfOpt = "Conver";
			}
			
			//
			if((ie.getSource() == otherKeyTable.iterOpt))
			{
				if(((JRadioButton)ie.getItem()).isSelected())
						{
						densityOpt = "Iterative";
					}
			}
			if((ie.getSource() == otherKeyTable.invOpt))
			{
			if(((JRadioButton)ie.getItem()).isSelected())
			{
				otherKeyTable.invToler.setEditable(true);
				densityOpt = "InvToler";
			}
			}
			
			if((ie.getSource() == otherKeyTable.convOpt))
			{
			if(((JRadioButton)ie.getItem()).isSelected())
				{
				otherKeyTable.converDen.setEditable(true);
				densityOpt = "Convergence";
				}
		
			}
			
			
			//fmm
			if((ie.getSource() == otherKeyTable.lmaxOpt))
			{
				if(((JRadioButton)ie.getItem()).isSelected())
				{
					otherKeyTable.lmax.setEditable(true);
					fmmOpt = "Lmax";
				}
			}
			if((ie.getSource() == otherKeyTable.levelOpt))
			{
				if(((JRadioButton)ie.getItem()).isSelected())
				{
					otherKeyTable.level.setEditable(true);
					fmmOpt = "Levels";
				}
			}
			if((ie.getSource() == otherKeyTable.tolOpt))
			{
				if(((JRadioButton)ie.getItem()).isSelected())
				{
					otherKeyTable.tol.setEditable(true);
					fmmOpt = "Tolerance";
				}
			}
			if((ie.getSource() == otherKeyTable.boxOpt))
			{
				if(((JRadioButton)ie.getItem()).isSelected())
				{
					otherKeyTable.box.setEditable(true);
					fmmOpt = "BoxLen";
				}
			}
			if((ie.getSource() == otherKeyTable.allnearOpt))
			{
				if(((JRadioButton)ie.getItem()).isSelected())
				{
					fmmOpt = "AllNearField";
				}
			}
			
			
			//nmr
			if((ie.getSource() == otherKeyTable.spinOpt)||
					(ie.getSource() == otherKeyTable.csgtOpt)||
					(ie.getSource() == otherKeyTable.giaoOpt)||
					(ie.getSource() == otherKeyTable.igaimOpt)||
					(ie.getSource() == otherKeyTable.singleOpt)||
					(ie.getSource() == otherKeyTable.allOpt)||
					(ie.getSource() == otherKeyTable.printOpt))
					{
					if(((JRadioButton)ie.getItem()).isSelected())
					nmrOpt = new String((((JRadioButton)ie.getItem()).getLabel()));
					else
						nmrOpt="";
					}
			
			
			
			
				
			  //For PSEUDO
			
		if(isPseudoItemChanged(ie)){}
			
		
		//For Prop
		
		else if(isPropItemChanged(ie)){}
			
			
		//For PUNCH
		
		else if(isPunchItemChanged(ie)){}
		
		//FOR OUTPUT
		else if(isOutputItemChanged(ie)){}	
			
		//FOR SPARSE
		else if(isSparseItemChanged(ie)){}
		
             
		//For Symmetry
		else if(isSymmetryEventChanged(ie))
			{
			}		
			
		
		
		
		}
		
	
	boolean isPseudoItemChanged(ItemEvent ie){
		
		if(ie.getSource()== otherKeyTable.pseudoOptions[0]||
				ie.getSource()== otherKeyTable.pseudoOptions[1]||
				ie.getSource()== otherKeyTable.pseudoOptions[2]||
				ie.getSource()== otherKeyTable.pseudoOptions[3]||
				ie.getSource()== otherKeyTable.pseudoOptions[4]||
				ie.getSource()== otherKeyTable.pseudoOptions[5])
				
		{
			
			if(!(InsertNode.nodeExists("Pseudo")))
				InsertNode.insertNode("Key","Pseudo");
			return true;
		}	
	
	
	
	return false;
	}
		
		
		boolean isSymmetryEventChanged(ItemEvent ie){
			
			if(ie.getSource()== otherKeyTable.symmetryOptions[9])
				{
					//PG=Group
					if(otherKeyTable.symmetryOptions[9].isSelected())
					{
						//Enable the TextField
						otherKeyTable.symmetryPGText.setEnabled(true);
						otherKeyTable.symmetryPGText.setToolTipText("<html> Enter the PG=<b>group</b> value here </html>");
						G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+G03Input.otherKeyToolTip.symmpgTip);
						//Insert into the Tree
						if(!(InsertNode.nodeExists("Symmetry")))
							InsertNode.insertNode("Key","Symmetry");
					}
					
					else{
						//Disable the textfield
						otherKeyTable.symmetryPGText.setText("");
						otherKeyTable.symmetryPGText.setEnabled(false);
						//Insert into the Tree
						if((InsertNode.nodeExists("Symmetry")))
							InsertNode.deleteNode("Symmetry");
					}
				return true;
				}
			
			
			else if(ie.getSource()== otherKeyTable.symmetryOptions[10])
			{
				if(otherKeyTable.symmetryOptions[10].isSelected()){
					
					//Enable the Combo Box
					otherKeyTable.symmetryAxisCombo.setEnabled(true);
					otherKeyTable.symmetryAxisCombo.setToolTipText("<html> Choose the <b>Axis</b> value here </html>");
					G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+G03Input.otherKeyToolTip.symmaxisTip);
					//Insert into the Tree
					if(!(InsertNode.nodeExists("Symmetry")))
						InsertNode.insertNode("Key","Symmetry");
				
					return true;
				}
				
				else
				{
					otherKeyTable.symmetryAxisCombo.setEnabled(false);
					if((InsertNode.nodeExists("Symmetry")))
						InsertNode.deleteNode("Symmetry");
					return false;
				}
			}
			
			
			else  // Any Item other than ComboBox or TextField
			{
				for(int j=0;j<9;j++)	
					if(ie.getSource()== otherKeyTable.symmetryOptions[j])
					{
						if(otherKeyTable.symmetryOptions[j].isSelected())
						{
						// any other symmetry
						if(!(InsertNode.nodeExists("Symmetry")))
							InsertNode.insertNode("Key","Symmetry");
						//Show that particular Tooltip
						G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+G03Input.otherKeyToolTip.symmetryOptionTips[j]);
						return true;
						}
					}
			
				if(ie.getSource()== otherKeyTable.symmetryOptions[11]){
					// any other symmetry
					if(otherKeyTable.symmetryOptions[11].isSelected())
					{
					if(!(InsertNode.nodeExists("Symmetry")))
						InsertNode.insertNode("Key","Symmetry");
					//Show that particular Tooltip
					G03MenuTree.keyoptArea.setText(G03MenuTree.keyoptArea.getText()+G03Input.otherKeyToolTip.symmonTip);
					return true;
					}
				}
				
			}
		return false;
		}
	
	boolean isPunchItemChanged(ItemEvent ie){
		System.out.println("In Punch ItemListener");
			for(int j=0;j<9;j++)	
				if(ie.getSource()== otherKeyTable.punchOptions[j])
				{
					// any other symmetry
					if(!(InsertNode.nodeExists("Punch")))
						InsertNode.insertNode("Key","Punch");
					//Show that particular Tooltip
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,G03Input.otherKeyToolTip.punchOptionTips[j]);
						return true;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
		
		return false;
	}
	
	boolean isPropItemChanged(ItemEvent ie){
		System.out.println("In Prop ItemListener");
			for(int j=0;j<10;j++)	
				if(ie.getSource()== otherKeyTable.propOptions[j])
				{
					// any other symmetry
					if(!(InsertNode.nodeExists("Prop")))
						InsertNode.insertNode("Key","Prop");
					//Show that particular Tooltip
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,G03Input.otherKeyToolTip.propOptionTips[j]);
						return true;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
		
		return false;
	}
	
	boolean isOutputItemChanged(ItemEvent ie)
	{
		
			if(ie.getSource().toString().equals(otherKeyTable.outputOptions[0].toString()))
			{
				//WFN=()
				if(otherKeyTable.outputOptions[0].isSelected())
				{
					//Enable the TextField
					otherKeyTable.outputWFNText.setEnabled(true);
					otherKeyTable.outputWFNText.setToolTipText("<html> Enter the <b><i>filename.wfn</i></b></html>");
									
					//Insert into the Tree
					if(!(InsertNode.nodeExists("Output")))
					InsertNode.insertNode("Key","Output");
					
					return true;
				}
				
				else{
					//Disable the textfield
					otherKeyTable.outputWFNText.setText("");
					otherKeyTable.outputWFNText.setEnabled(false);
					
					
				}
			}
		
		
		
		
			for(int j=1;j<3;j++)	
				if(ie.getSource()== otherKeyTable.outputOptions[j])
				{
					if(otherKeyTable.outputOptions[j].isSelected()){
					// any other option
					if(!(InsertNode.nodeExists("Output")))
						InsertNode.insertNode("Key","Output");
					
					return true;
					}
				}
		
			
	return false;	
	}
	
	
	
	boolean isSparseItemChanged(ItemEvent ie)
	{
		
			if(ie.getSource().toString().equals(otherKeyTable.sparseOptions[3].toString()))
			{
				//N=()
				if(otherKeyTable.sparseOptions[3].isSelected())
				{
					//Enable the TextField
					otherKeyTable.sparseNText.setEnabled(true);
					otherKeyTable.sparseNText.setToolTipText("<html> Enter the value of <b> N </b></html>");
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,G03Input.otherKeyToolTip.sparseOptionTips[3]);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					//Insert into the Tree
					if(!(InsertNode.nodeExists("Sparse")))
					InsertNode.insertNode("Key","Sparse");
					
					return true;
				}
				
				else{
					//Disable the textfield
					otherKeyTable.sparseNText.setText("");
					otherKeyTable.sparseNText.setEnabled(false);
					
					
				}
			}
		
		
		
		
			for(int j=0;j<3;j++)	
				if(ie.getSource()== otherKeyTable.sparseOptions[j])
				{
					if(otherKeyTable.sparseOptions[j].isSelected()){
					// any other symmetry
					if(!(InsertNode.nodeExists("Sparse")))
						InsertNode.insertNode("Key","Sparse");
					//Show that particular Tooltip
					try {
						G03MenuTree.insertHTML(G03MenuTree.keyoptArea,G03Input.otherKeyToolTip.sparseOptionTips[j]);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return true;
					}
				}
		
			
	return false;	
	}
	
	
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == otherKeyTable.doneButton)
		{
			RouteClass.writeRoute();
			otherKeyTable.otherKeyFrame.hide();
		}
		
		//code for a Clear Button
		
		if(e.getSource() == otherKeyTable.clearButton)
		{
			  for(int h=0;h< otherKeyTable.okVector.size();h++)
              {
              	if(((JRadioButton)(otherKeyTable.okVector.get(h))).isSelected())
                    {
                    	((JRadioButton)(otherKeyTable.okVector.get(h))).setSelected(false);
                    	if(InsertNode.nodeExists((((JRadioButton)(otherKeyTable.okVector.get(h))).getActionCommand().toString()))){
                    	InsertNode.deleteNode((((JRadioButton)(otherKeyTable.okVector.get(h))).getActionCommand().toString()));	
                    	}
                    }
                     
              }
        	otherKeyTable.otherKeys.removeAllElements();
        	RouteClass.writeRoute();
        	otherKeyTable.otherKeyFrame.dispose();
			/*
			
			if(otherKeyTable.okArc.isSelected())
			{
				
				if(InsertNode.nodeExists("Archive"))
				{
					InsertNode.deleteNode("Archive");
				}
				otherKeyTable.okArc.setSelected(false);
				otherKeyTable.otherKeyFrame.dispose();
				otherKeyTable.otherKeyFrame.getContentPane().add(otherKeyTable.scroll,BorderLayout.CENTER);
				otherKeyTable.otherKeyFrame.getContentPane().add(otherKeyTable.donePanel,BorderLayout.SOUTH);
			}
			if(otherKeyTable.okCha.isSelected())
			{
				otherKeyTable.okCha.setSelected(false);
				if(InsertNode.nodeExists("Charge"))
				{
					InsertNode.deleteNode("Charge");
				}
			}*/
		/*	otherKeyTable.otherKeys.removeAllElements();
			RouteClass.writeRoute();
			/*{okArc,okCha,okChk,okCom,okCon},
			{okCou,okCph,okDen,okExt,okExtB},
			{okExtD,okFie,okFmm,okGfi,okGfp},
			{okInt,okIop,okNam,okOut,okPre},
			{okPro,okPse,okPun,okSca,okSpa},
			{okSym,okTem,okTesM,okTra,okTrans},
			{okUni,okNmr,null,null,null},}, 
			}
			*/
		}
				//counter
		if(e.getSource()==otherKeyTable.counterOK)
		{
			if(otherKeyTable.otherFlag==0)
			{
				otherKeyTable.otherKeys = new Vector<String>();
				otherKeyTable.otherFlag++;
			}
			otherKeyTable.otherKeys.addElement("CounterPoise="+ otherKeyListener.counter_Opt);
			if(!(InsertNode.nodeExists("CounterPoise")))
				InsertNode.insertNode("Key","CounterPoise");
			otherKeyTable.counterFrame.dispose();
		}


		
		
		//To be integrated
		
			if(e.getSource() == otherKeyTable.exitButton)
			{
			otherKeyTable.otherKeyFrame.dispose();	
			}
			if(e.getSource()==otherKeyTable.chargeOK)
			{
				if(otherKeyTable.otherFlag==0)
				{
					otherKeyTable.otherKeys = new Vector<String>();
					otherKeyTable.otherFlag++;
				}
				otherKeyTable.otherKeys.addElement("Charge="+otherKeyListener.charge_Opt);
				if(!(InsertNode.nodeExists("Charge")))
				InsertNode.insertNode("Key","Charge");
				otherKeyTable.chargeFrame.dispose();
			}
			
			
			//code to get constant
			if(e.getSource()==otherKeyTable.constantOK)
			{
				if(otherKeyTable.otherFlag==0)
				{
					otherKeyTable.otherKeys = new Vector<String>();
					otherKeyTable.otherFlag++;
				}
				otherKeyTable.otherKeys.addElement("Constants="+otherKeyListener.con_Opt);
				if(!(InsertNode.nodeExists("Constants")))
					InsertNode.insertNode("Key","Constants");
				otherKeyTable.constantsFrame.dispose();
			}
			
			
			
			
			if(e.getSource()==otherKeyTable.cphfOK)
			{
				if(otherKeyTable.otherFlag==0)
				{
					otherKeyTable.otherKeys = new Vector<String>();
					otherKeyTable.otherFlag++;
				}
				if(cphfOpt.equals("Grid"))
				{
					cphfOpt = "(Grid="+otherKeyTable.grid.getText().toString()+")";
				}
				if(cphfOpt.equals("MaxInv"))
				{
					cphfOpt = "(MaxInv="+otherKeyTable.max.getText().toString()+")";
				}
				if(cphfOpt.equals("Conver"))
				{
					cphfOpt = "(Conver="+otherKeyTable.conver.getText().toString()+")";
				}
				otherKeyTable.otherKeys.addElement("CPHF="+ otherKeyListener.cphfOpt);
				if(!(InsertNode.nodeExists("CPHF")))
					InsertNode.insertNode("Key","CPHF");
				otherKeyTable.cphfFrame.dispose();
			}
			
			//nmr
			if(e.getSource()==otherKeyTable.nmrOk)
			{
				if(otherKeyTable.otherFlag==0)
				{
					otherKeyTable.otherKeys = new Vector<String>();
					otherKeyTable.otherFlag++;
				}
				otherKeyTable.otherKeys.addElement("NMR="+otherKeyListener.nmrOpt);
				if(!(InsertNode.nodeExists("NMR")))
					InsertNode.insertNode("Key","NMR");
				otherKeyTable.nmrFrame.dispose();
			}
			
			
			
			if(e.getSource()==otherKeyTable.densityOK)
			{
				if(otherKeyTable.otherFlag==0)
				{
					otherKeyTable.otherKeys = new Vector<String>();
					otherKeyTable.otherFlag++;
				}
				if(densityOpt.equals("InvToler"))
				{
					densityOpt = "(MaxInv="+otherKeyTable.invToler.getText().toString()+")";
				}
				if(densityOpt.equals("Convergence"))
				{
					densityOpt = "(Convergence="+otherKeyTable.converDen.getText().toString()+")";
				}
				otherKeyTable.otherKeys.addElement("DensityFit="+ otherKeyListener.densityOpt);
				if(!(InsertNode.nodeExists("DensityFit")))
					InsertNode.insertNode("Key","DensityFit");
				otherKeyTable.densityFrame.dispose();
			}
			

			if(e.getSource()==otherKeyTable.fmmOK)
			{
				if(otherKeyTable.otherFlag==0)
				{
					otherKeyTable.otherKeys = new Vector<String>();
					otherKeyTable.otherFlag++;
				}
				if(fmmOpt.equals("Lmax"))
				{
					fmmOpt = "(Lmax="+otherKeyTable.lmax.getText().toString()+")";
				}
				if(fmmOpt.equals("Levels"))
				{
					fmmOpt = "(Levels="+otherKeyTable.level.getText().toString()+")";
				}
				if(fmmOpt.equals("Tolerance"))
				{
					fmmOpt = "(Tolerance="+otherKeyTable.tol.getText().toString()+")";
				}
				if(fmmOpt.equals("BoxLen"))
				{
					fmmOpt = "(BoxLen="+otherKeyTable.box.getText().toString()+")";
				}
				otherKeyTable.otherKeys.addElement("FMM="+ otherKeyListener.fmmOpt);
				if(!(InsertNode.nodeExists("FMM")))
					InsertNode.insertNode("Key","FMM");
				otherKeyTable.fmmFrame.dispose();
			}
			
			

		//To be integrated
		
		//		PROP
		
		if(e.getSource() == otherKeyTable.propDone){

			for(int i=0;i<4;i++)
			{
				//Check the CheckBoxes that is selected in the ButtonGroup
				//Add it to the RouteSection.
				if(otherKeyTable.propOptions[i].isSelected())
				{
					
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					
					
					prop_Opt1=otherKeyTable.propOptions[i].getText().toString();
									
				}
			}
			
			for(int i=4;i<10;i++)
			{
				//Check the CheckBoxes that is selected in the ButtonGroup
				//Add it to the RouteSection.
				if(otherKeyTable.propOptions[i].isSelected())
				{
					
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					
					
					prop_Opt2=otherKeyTable.propOptions[i].getText().toString();
									
				}
			}
			
			
			if(prop_Opt2.length()==0 && prop_Opt1.length()!=0) //Input Source is not selected
				{
				otherKeyTable.otherKeys.addElement("Prop="+prop_Opt1);
				prop_Opt=prop_Opt1;
				System.out.println("checking3"+prop_Opt);
				}
			else if(prop_Opt1.length()==0 && prop_Opt2.length()!=0) //Property Selection is not selected
				{
				otherKeyTable.otherKeys.addElement("Prop="+prop_Opt2);
				prop_Opt=prop_Opt2;
				System.out.println("checking2"+prop_Opt);
				}
			
			else if (prop_Opt1.length()!=0 && prop_Opt2.length()!=0)
			{
				// If both of them are selected
				otherKeyTable.otherKeys.addElement("Prop=("+prop_Opt1+","+prop_Opt2+")");
				prop_Opt="("+prop_Opt1+","+prop_Opt2+")";
				System.out.println("checking"+prop_Opt);
				
			}
			
			else
			{ 	// None selected
				
				otherKeyTable.okPro.setSelected(false);
			
			}
			otherKeyTable.propFrame.dispose();
		}
		
		
		
		
		
		
		//PUNCH
		
		if(e.getSource() == otherKeyTable.punchDone){
			for(int i=0;i<otherKeyTable.punchOptions.length;i++)
			{
				//Check the CheckBoxes that is selected in the ButtonGroup
				//Add it to the RouteSection.
				if(otherKeyTable.punchOptions[i].isSelected())
				{
					
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					
					
					punch_Opt=otherKeyTable.punchoptions[i];
					
					
					otherKeyTable.otherKeys.addElement("Punch= "+punch_Opt);
					otherKeyTable.punchFrame.dispose();
					
				}
				
				
				
			}
			
			
		}
		
		
		//		PSEUDO
		
		if(e.getSource() == otherKeyTable.pseudoDone){
			System.out.println("Pseudo... before for...");
			for(int i=0;i<otherKeyTable.pseudoOptions.length;i++)
			{
				//Check the CheckBoxes that is selected in the ButtonGroup
				//Add it to the RouteSection.
				if(otherKeyTable.pseudoOptions[i].isSelected())
				{
					
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					
					
					pseudo_Opt=otherKeyTable.pseudooptions[i];
					
					
					otherKeyTable.otherKeys.addElement("Pseudo="+pseudo_Opt);
					otherKeyTable.pseudoFrame.dispose();
					System.out.println("Pseudo... Frame Disposed...");		
				}
				
				
				
			}
			
			
		}
		
		
		
		
		//Sparse 
		
		if(e.getSource() == otherKeyTable.sparseDone){
			for(int i=0;i<otherKeyTable.sparseOptions.length;i++)
			{
				//Check the RadioButton that is selected in the ButtonGroup
				//Add it to the RouteSection.
				if(otherKeyTable.sparseOptions[i].isSelected())
				{
					
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					
					if(i==3)  // Option N=...
					{
						sparse_Opt="(N="+otherKeyTable.sparseNText.getText()+")";
					}
					else{
						sparse_Opt=otherKeyTable.sparseoptions[i];
					}
					
					otherKeyTable.otherKeys.addElement("Sparse="+sparse_Opt);
					otherKeyTable.sparseFrame.dispose();
				
					if(!(InsertNode.nodeExists("Sparse")))
						InsertNode.insertNode("Key","Sparse");
							
					
				}
				
				
				
			}
			
			
		}
		
		//Output
				
		if(e.getSource() == otherKeyTable.outputDone){
			for(int i=0;i<otherKeyTable.outputOptions.length;i++)
			{
				//Check the RadioButton that is selected in the ButtonGroup
				//Add it to the RouteSection.
				if(otherKeyTable.outputOptions[i].isSelected())
				{
					
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					
					if(i==0)  // Option N=...
					{
						output_Opt="(WFN="+otherKeyTable.outputWFNText.getText()+")";
					}
					else{
						output_Opt=otherKeyTable.outputoptions[i];
					}
					
					otherKeyTable.otherKeys.addElement("Output= "+output_Opt);
					otherKeyTable.outputFrame.dispose();
				
					if(!(InsertNode.nodeExists("Output")))
						InsertNode.insertNode("Key","Output");
							
					
				}
				
				
				
			}
			
			
		}
		
		
		
		//Symmetry
		if(e.getSource() == otherKeyTable.symmetryDone){
			for(int i=0;i<otherKeyTable.symmetryOptions.length;i++)
			{
				//Check the RadioButton that is selected in the ButtonGroup
				//Add it to the RouteSection.
				if(otherKeyTable.symmetryOptions[i].isSelected())
				{
					
					if(otherKeyTable.otherFlag==0)
					{
						otherKeyTable.otherKeys = new Vector<String>();
						otherKeyTable.otherFlag++;
					}
					
					if(i==9)  // Option PG=group
					{
						symm_Opt="(PG="+otherKeyTable.symmetryPGText.getText()+")";
					}
					else if(i==10) //Axis == X,Y,Z
					{
						int new_index=otherKeyTable.symmetryAxisCombo.getSelectedIndex();
						char axis='X';
						if(new_index==0)axis='X';
						else if (new_index==1)axis='Y';
						else if (new_index==2)axis='Z';
						symm_Opt="(Axis="+axis+")";	
					}
					else{
						symm_Opt=otherKeyTable.options[i];
					}
					
					otherKeyTable.otherKeys.addElement("Symmetry="+symm_Opt);
					otherKeyTable.symmetryFrame.dispose();
				
					if(!(InsertNode.nodeExists("Symmetry")))
						InsertNode.insertNode("Key","Symmetry");
					
				}
				
				
				
			}
			
			
		}
		
		
		
		
		
		
		
		
		
		
	}
	
}
