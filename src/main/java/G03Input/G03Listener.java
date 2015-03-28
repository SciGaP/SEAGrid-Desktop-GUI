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
 * Created on Apr 10, 2005
 * @author Michael Sheetz 
 * @author Sandeep Kumar Seethaapathy @author Shashank Jeedigunta 
 * 
 */



package G03Input;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.*;

import org.gridchem.client.GridChem;
import org.gridchem.client.SubmitJobsWindow;
import org.gridchem.client.editJobPanel;
import org.gridchem.client.gui.jobsubmission.EditJobPanel;
import org.gridchem.client.optsComponent;
import org.gridchem.client.stuffInside;
import org.gridchem.client.common.Settings;




//import GridChem.Settings;





public class G03Listener implements ActionListener,KeyListener{
	JTextField fileNameText;
	 public static int optTabF,freqTabF,guessTabF,geomTabF,popTabF,pbcTabF;    //Flags for the tables
    public void actionPerformed(ActionEvent ae)
    {

    	if(ae.getSource()==G03MenuTree.viewMolStructure)
    	{
    		//G03MenuTree.molCharge.setText("charge");
    		//G03MenuTree.molMultiplicity.setText("Multiplicity");
    	//Try writing a flag that will set if the user uses an exported molecular structure fromNanocad
    		try {
    			if(InputFile.inputfetched==1)
    				showMolEditor.tempmol=InputfileReader.geom;
    			/*
			     if(showMolEditor.nanoFlag==1)
    				showMolEditor.tempmol=showMolEditor.exportedMol;	*/
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 		
    		new showMolEditor();
    		showMolEditor.molFrame.setVisible(true);
    	}
    	/*
    	if(ae.getSource()== G03MenuTree.nanoItem){
    		 showMolEditor.doCallNanocad();
    		 showMolEditor.molFrame.dispose();
    	}*/
    	
    	
        if(ae.getSource()==G03MenuTree.doneButton)
        {
    		if(!Settings.authenticated)
    		{
    			JOptionPane.showMessageDialog(null,"You are not authenticated to use this feature.\nPlease sign in to GridChem to use this feature." , "Access restriction", JOptionPane.ERROR_MESSAGE);
    			return;
    		}
        	showMolEditor.nanoFlag=0;
        	RouteClass.createInput();
            // change routeBuffer to inputfile string once it is done
            
            //Copied From SubmitJobsWindow.java
        	if(RouteValidator.validateRoute())
            {
            /*if(Settings.authenticatedSSH == true)
            {
        //  GridChem.editSSHJobPanel ejp= new GridChem.editSSHJobPanel(-1);
            }
            else
            {*/
        		
        		new InputfileReader();
        		int charge, mul, noofelectrons=0,nproc=0,nmem=0;
        		boolean evenTotalElectrons, evenMultiplicity,validStructure;
        		charge= InputfileReader.charge;
        		mul= InputfileReader.spinmult;
        		
        		nproc=InputfileReader.nproc;
        		nmem=InputfileReader.mbRequested;
        		System.out.println("Length of the Route is #"+ InputfileReader.route.length()+ "and" +
        				"route is "+InputfileReader.route);
        		if(InputfileReader.route.length() < 2)
        		{
        			JOptionPane.showMessageDialog(null,"Route Section is incomplete","ERROR",JOptionPane.ERROR_MESSAGE);
        		}
        		else if(InputfileReader.chrgStr==null || InputfileReader.mulStr==null)
        		{
        			JOptionPane.showMessageDialog(null,"Charge or Multiplicity  cannot be empty","ERROR",JOptionPane.ERROR_MESSAGE);
        		}
        		
        		else{
        		        		
        		MolEditorHelp.hashFunction();
        		if(GeometryEditor.GeometryReader.atomSymb !=null)
        		for(int k=0;k<GeometryEditor.GeometryReader.atomSymb.length;k++)
        		{
        			System.out.println("Atom symbol is "+(GeometryEditor.GeometryReader.atomSymb[k]).toString());
        			System.out.println("And the number returned is "+MolEditorHelp.htAtomicNumber.get(GeometryEditor.GeometryReader.atomSymb[k]).toString());
        			noofelectrons+=Integer.parseInt(MolEditorHelp.htAtomicNumber.get(GeometryEditor.GeometryReader.atomSymb[k]).toString());
        		}
        		
        		if(noofelectrons==0)
        		{
        			JOptionPane.showMessageDialog(null,"Molecular Coordinates not specified","ERROR",JOptionPane.ERROR_MESSAGE);
        		}
        		else{
        		
        			noofelectrons-=charge;
        			
        			
        			if(noofelectrons%2==0) // Total Even Electrons
        			{
        				evenTotalElectrons=true;
        			}
        			else
        				evenTotalElectrons=false;
        			
        			
        			if(mul%2==0)
        				evenMultiplicity=true;
        			else
        				evenMultiplicity=false;
        			
        			
        			if((evenTotalElectrons==true)&& (evenMultiplicity==false))
        			{
        				validStructure=true;
        			}
        			else if((evenTotalElectrons==false)&& (evenMultiplicity==true))
        			{
        				validStructure=true;
        			}
        			else
        				validStructure=false;
        			
        			if(validStructure==false)
        			{
        				if(evenTotalElectrons==true)
        				{
        					JOptionPane.showMessageDialog(null,"Even Multiplicity is incompatible with even total number of electrons","ERROR",JOptionPane.ERROR_MESSAGE);
        				}
        				else if(evenTotalElectrons==false)
        				{
        					JOptionPane.showMessageDialog(null,"Odd Multiplicity is incompatible with odd total number of electrons","ERROR",JOptionPane.ERROR_MESSAGE);
        				} 
        					
        			}
        		
            		if (validStructure==true) {
    //                if(GridChem.oc.sjw==null)
    //                GridChem.oc.doSubmission();
            		    SubmitJobsWindow.getInstance();
                    
                        EditJobPanel ejp = new EditJobPanel(InputFile.tempinput,"Gaussian");
                        
                        clearButtonFn();
                        showMolEditor.tempmol=null;
                        SubmitJobsWindow.si.mainFrame.dispose();
                    
                    
            		}// valid structure is true
                
        		} // else part of noofelectrons=0
        	}	// else for route
        		
            //}
            }//Route Validate
            
        	
            
        }
        if(ae.getSource()==G03MenuTree.clearButton)
        {
        	clearButtonFn();	
        }
        if (ae.getSource()== G03MenuTree.saveButton)
        {
        	RouteClass.createInput();
        	new InputfileReader();
        	JFileChooser chooser = new JFileChooser();
        	chooser.setDialogTitle("Save as Guassian Input file");
        	
        	int retVal = chooser.showSaveDialog(null);
        	try
        	{
        	    if (retVal == JFileChooser.APPROVE_OPTION)
        	    {
        		File file = chooser.getSelectedFile();
        		// save the file data
        		FileWriter fw = new FileWriter(file);
        		new InputfileReader();
        		String outp = InputFile.tempinput;
        		System.out.println("*************");
        		System.out.println(InputFile.tempinput);
        		fw.write(outp);
        		fw.close();
//        		changeFileName(file.getName());
        		//changeFileName(file.getAbsolutePath());
        		/*
        		String filename = file.getName();
            	if (filename.indexOf(".")!=-1)
        		filename = filename + ".inp";
            	
        		File fileRenamed = new File(filename);
        		file.renameTo(fileRenamed);
        		*/
        	    }
        	}
        	catch (IOException e)
        	{
        	    JOptionPane.showMessageDialog(null, "Error writing to file",
        		    "Save File Error", JOptionPane.INFORMATION_MESSAGE);
        	}	
        }
        if(ae.getSource()==G03MenuTree.dontsave)
        {
        	G03MenuTree.filnamArea.setEditable(true);
        }
        if(ae.getSource()==G03MenuTree.exitButton)
        {
        	int ch;
        	ch=JOptionPane.showConfirmDialog(null,"Are you sure?","Exit from Gaussian03",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE);
        	if(ch==0)
        	{        	
        	try {
        	

        		if(G03MenuTree.mainFrame != null)
        		{
            		G03MenuTree.mainFrame.dispose();
            		G03MenuTree.mainFrame = null;
        		}
        		else if(stuffInside.mainFrame != null)
        		{
        			stuffInside.mainFrame.dispose();
        			stuffInside.mainFrame = null;
        		}
        		else if(optsComponent.mainFrame != null)
        		{
        			optsComponent.mainFrame.dispose();
        			optsComponent.mainFrame = null;
        		}

             
            } catch (NullPointerException ex) {
                System.out.println(ex);
                
            }
        	}
        	else
        	{
        		
        	}
        }
    
    
    
    
    
    
    }
    public void changeFileName(String f) {
    	//	this.fileNameText = new JTextField(f);
    	fileNameText.selectAll();
    	fileNameText.replaceSelection(f);
    	//	fileNameText.append(f);
    }
    public void keyTyped(KeyEvent ke)
	
	{
		
		G03MenuTree.dontsave.setSelected(true);
	}

    public void keyPressed(KeyEvent ke)
    {
    
    }
    public void keyReleased(KeyEvent ke)
    {
    	
    } 
    
    
    
    
    public static void clearButtonFn()
    {
    	try
		{
    	
    	System.out.println("Clear");
    	InsertNode.deleteChildren("Met");
    	InsertNode.deleteChildren("Basis");
    	InsertNode.deleteChildren("Jo");
    	InsertNode.deleteChildren("Key");
    
    	 if(G03Listener.optTabF>0)
         {
         	for(int h=0;h<OptTable.optClear.size();h++)
         	{
         		if(((JRadioButton)(OptTable.optClear.get(h))).isSelected())
         		{
         			((JRadioButton)(OptTable.optClear.get(h))).setSelected(false);
                }
         	}
         	OptTable.optionsC=0;
         
         	if(OptTable.OMaxCy.isSelected())
         	{
   	  	    	OptTable.OMaxCy.setSelected(false);
         	}
   
   
   if(OptTable.OMaxSt.isSelected())
   {
   		OptTable.OMaxSt.setSelected(false);
   }
   
   if(OptTable.OSadd.isSelected())
   {
   		OptTable.OSadd.setSelected(false);
   }
   
    
   if(OptTable.OPathN.isSelected())
   {	
   		OptTable.OPathN.setSelected(false);
   }

   if(OptTable.OChkHar.isSelected())
   {	
   	OptTable.OChkHar.setSelected(false);
   	
   }  
   if(OptTable.OInitHar.isSelected())
   {
   		OptTable.OInitHar.setSelected(false);
   	 	
   }
   if(OptTable.OReadHar.isSelected())
   {
   		OptTable.OReadHar.setSelected(false);
   	
   }
   
   OptTable.optC=0;
   OptTable.optFlag=0;
   OptTable.optionsC=0;
  // OptTable.optVopt=new Vector();
   
         }
     
    	
    	if(G03Listener.freqTabF>0)
    	{
    	for(int h=0;h<FreqOptTable.freqClear.size();h++)
        {
        	if(((JRadioButton)(FreqOptTable.freqClear.get(h))).isSelected())
              {
              	System.out.println("Inside Freq clear button");
                	((JRadioButton)(FreqOptTable.freqClear.get(h))).setSelected(false);
              		FreqOptTable.freqOpt.removeElement((FreqOptTable.freqClear.get(h)));
                	}
               
              }
        
    
    	if(FreqOptTable.FStep.isSelected())
    	{
    		FreqOptTable.FStep.setSelected(false);
    	}
    	FreqOptTable.freqC=0;
    	FreqOptTable.freqOptC=0;
    	//FreqOptTable.opop=0;
    }
    	System.out.println("Whether it comes here");  
   
    	if(G03Listener.guessTabF>0)
    {
    for(int h=0;h<GuessOptTable.guessClear.size();h++)
    {
    if(((JRadioButton)(GuessOptTable.guessClear.get(h))).isSelected())
    {
    	  	((JRadioButton)(GuessOptTable.guessClear.get(h))).setSelected(false);
    	
      	}
     
     }
    GuessOptTable.guessOptC=0;
    GuessOptTable.guessC=0;
    }
    System.out.println("Whether it comes here before pop");  
    
    if(G03Listener.popTabF>0)
    {       
for(int h=0;h<popOptTable.popClear.size();h++)
{
    if(((JRadioButton)(popOptTable.popClear.get(h))).isSelected())
    {
    		((JRadioButton)(popOptTable.popClear.get(h))).setSelected(false);
    }

	}

popOptTable.popOptC=0;
popOptTable.popC=0;
    }
    
    System.out.println("Whether it comes here before PBC");  

    
    if(G03Listener.pbcTabF>0)
    {
    	 for(int j=0;j<8;j++)
         {                     
             
             
                 pbcTable.pbcOptions[j].setSelected(false);
                 pbcTable.selectedIndex[j]=0;
                 pbcTable.initValues[j]="";
             
         }
    }

System.out.println("after PBC");  
    
    if(G03Listener.geomTabF>0)
    {
     for(int h=0;h<geomOptTable.geomClear.size();h++)
     {
     if(((JRadioButton)(geomOptTable.geomClear.get(h))).isSelected())
     	{
    	System.out.println("Test inside if selected");
   		((JRadioButton)(geomOptTable.geomClear.get(h))).setSelected(false);
     	}
     }
geomOptTable.geomOptC=0;
geomOptTable.geomC=0;
G03MenuTree.keyoptArea=null;

	}   
    RouteClass.methodBuffer=new StringBuffer();
    RouteClass.basisBuffer=new StringBuffer();
    otherKeyTable.otherKeys.removeAllElements();
    otherKeyTable.otherFlag=0;
    try {
		for(int o=0;o<40;o++)
		{
			RouteClass.keywordBuffer[o]=new StringBuffer();
		}
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	RouteClass.initCount=0;
    RouteClass.poundOpt=new StringBuffer();
    RouteClass.keyIndex=0;
    G03MenuTree.filnamArea.setText("");
    G03MenuTree.jobArea.setText("");
    G03MenuTree.dontsave.setSelected(true);
    G03MenuTree.nooflpText.setText("");
    G03MenuTree.noofspText.setText("");
    G03MenuTree.dynmemText.setText("");
    G03MenuTree.routeArea.setText("#");
    // Newly added Sep 21,2005
    InputfileReader.geom=null;
    InputFile.inputText.setText("");
    InputFile.tempinput=new String();
    System.out.println("After clearing tempinput"+InputFile.tempinput);
    InputFile.inputfetched=0;
    //showMolEditor.tempmol="";
    showMolEditor.molText.setText("");
    showMolEditor.tempmol=new String(); //Newly added 
    
    RouteClass.writeRoute();
    
		}
    catch(NullPointerException e)
	{
    	
	}
    }

    }



        
        
        
        
        
        
        
        
        
        
        
        
        
