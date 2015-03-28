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
 * Created on Mar 17, 2005
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta  @author Sandeep Kumar Seethaapathy 
 * 
 */


package G03Input;
import java.awt.event.*;

import java.awt.event.ActionListener;
import java.awt.*;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.*;

import java.util.*;
import javax.swing.tree.TreePath;

public class MenuMethodListener  extends JFrame implements ActionListener,ItemListener {

public static int selectOpt;
//public static Vector corrFunctionals = new Vector();
//public static Vector exchngFunctionals= new Vector();
public static Hashtable<String,Integer> exchng
    = new Hashtable<String,Integer>();
public static Hashtable<String,Integer> corr = new Hashtable<String,Integer>();
public static int exchngFlag=0,corrFlag=0;
public static int count=0;
public static int flag;
public static int onOk=0;
public static String str="",str1="";
public static String fnName="",cfnName="";
public static String exchngName="",corrName="",restrictione="",restrictionc="";

  DefaultMutableTreeNode child;
  DefaultMutableTreeNode grandChild;
  public static JFrame listAllFrame;
  public static JPanel listAllPanel;
  public static JRadioButton restrictc,restricto,unRestrict;
  public static JLabel listAllLabel;
  public static JButton listAllok;
  public static ButtonGroup restGroup;
  
 
  
  
  
  
  
  /**
   * Why is there duplicated code:
   * RunFrame::createThreeOptionFrame
   * is the same as
   * MenuMethodListener::showOptionsFrame
   */
  public  void showOptionsFrame() 
  {
  	JFrame.setDefaultLookAndFeelDecorated(true);
  	listAllFrame = new JFrame("Select Wavefunction Type");
  	listAllPanel = new JPanel(new GridBagLayout());
  	GridBagConstraints c  = new GridBagConstraints();
  	listAllLabel = new JLabel("Run calculation as");
  	restrictc = new JRadioButton("R - Restricted closed-shell");
  	restricto = new JRadioButton("RO - Restricted Open-shell");
  	unRestrict = new JRadioButton("U - Unrestricted open-shell");
  	
  	listAllok = new JButton("OK");
  	restGroup=new ButtonGroup();
  	
  	restGroup.add(restrictc);
  	restGroup.add(restricto); 
  	restGroup.add(unRestrict);
  	
  	restrictc.setSelected(true);
  	
  	
  	listAllok.addActionListener(this);
  	
  	c.gridx=0;
  	c.gridy=0;
  	c.weightx=0.5;
  	//c.weighty=0.15;
  	c.insets= new Insets(0,10,10,100);
  	listAllPanel.add(listAllLabel,c);
  	
  	c.insets= new Insets(0,-90,10,-20);
	c.gridy=1;
	c.gridx=0;
  	listAllPanel.add(restrictc,c);
  	c.gridy=2;
  	c.gridx=0;
  	listAllPanel.add(restricto,c);
  	c.gridy=3;
  	c.gridx=0;
  	listAllPanel.add(unRestrict,c);
  	c.gridy=4;
  	c.gridx=0;
  	c.insets = new Insets(0,90,10,90);
  	listAllPanel.add(listAllok,c);
  	
  	listAllFrame.getContentPane().add(listAllPanel);
  	listAllFrame.pack();
  	listAllFrame.setLocation(200,200);
  	listAllFrame.setSize(300,225);
  	listAllFrame.setVisible(true);
  	//listAllFrame.setResizable(false);
  	listAllFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 	
  } 
  
  
  
  
  
  
    public void actionPerformed(ActionEvent e)
    {  // if(count==0) {createHTable();count++;}
        if(e.getSource()==MenuListeners.dftdoneButton)
        {   
            count++;
            
            InsertNode.deleteChildren("Me");
            RouteClass.methodBuffer = new StringBuffer();
            if(exchngName.equals("HFS(S)")||
                    exchngName.equals("HFB(B)")|| 
                    exchngName.equals("Xalpha(XA)") )
            {
                
                
                if(corrName.length()>2)
         	   {
         	       // Remove extra (B) (S) (XA)
                    exchng.put(exchngName,new Integer(1));
                    int findex,lindex;
         	       findex=exchngName.indexOf("(");
         	       lindex=exchngName.indexOf(")");
         	       if(lindex>findex) // checking
         	       exchngName= exchngName.substring(findex+1,lindex);
         	           	                	       
         	   }
         	  else {
         	      		// corrname exists 
         	     exchng.put(exchngName,new Integer(1));
         	     int findex,lindex;
       	       findex=exchngName.indexOf("(");
       	       lindex=exchngName.indexOf(")");
       	       if(lindex>findex) // checking
       	       exchngName= exchngName.substring(0,findex);
         	      
         	      
         	  		}
                
                
                System.out.println(exchng);
                InsertNode.insertNode("Me",restrictione+exchngName);
           	
         	RouteClass.methodBuffer.append(restrictione+exchngName);
       	    //RouteClass.writeRoute();
         	 
         	}
          else 
          {
      	    if(corrName.length()<2)
      	    { // Pop an error
      	        
      	      JOptionPane.showMessageDialog(null,"You need to select a Correlation Functional","Error",JOptionPane.ERROR_MESSAGE);
              
      	    }
      	    
      	    else
      	    { // if they select corrName
      	        
      	      InsertNode.insertNode("Me",restrictione+exchngName);
      	      
      		RouteClass.methodBuffer.append(restrictione+exchngName);
      	    }
      	    
     	    
      	}
            if(corrName.length()>2){
            InsertNode.insertNode("Me",restrictionc+corrName);
        
            corr.put(corrName,new Integer(1));
            System.out.println(corr);
        	RouteClass.methodBuffer.append(restrictionc+corrName);}
        	RouteClass.writeRoute();
        	MenuListeners.dftFrame.dispose();
        }     
            
        
        
        
        
        
        
        if(e.getSource()== listAllok)
        {
           String restriction="";
            //Do entire Stuff
            if(unRestrict.isSelected())
            {
                restriction="U";
            
            }
            else if(restrictc.isSelected())
            {
                restriction="R";
            
            }
            else if(restricto.isSelected())
            {
                restriction="RO";
            
            }
            
          if(flag==1) //exchange
              restrictione=restriction;
          if(flag==2)
              restrictionc=restriction;
          
          
          listAllFrame.dispose();
            
        }
        if(e.getSource()== RunFrame.ok1)
        {   
             if(RunFrame.unRest.isSelected())
                 selectOpt=1;
             else if(RunFrame.restClose.isSelected())
                 selectOpt=2;
             else
                 selectOpt=3;
             RunFrame.runFrame1.dispose();
             if(selectOpt==1)
                str="U";
                else if(selectOpt==2)
         	      str="R";
                	else if(selectOpt==3)
                	    str="RO";
                
                	 InsertNode.deleteChildren("Me");
                	 InsertNode.insertNode("Me",str+str1);
                	
                	 //RouteSection methods
                	 
                	 RouteClass.methodBuffer = new StringBuffer();
                	 RouteClass.methodBuffer.append(str+str1);
                	 RouteClass.writeRoute();
         }
 
        if(e.getSource()== RunFrame.ok2)
        {   selectOpt=0;
             if(RunFrame.restOpen.isSelected())
                 selectOpt=1;
             else if(RunFrame.restClose.isSelected())
                 selectOpt=2;
             RunFrame.runFrame2.dispose();
             
             if(selectOpt==1)
                str="U";
                else if(selectOpt==2)
         	      str="R";
                	 
                InsertNode.deleteChildren("Me"); 
               InsertNode.insertNode("Me",str+str1);
               
               //  RouteSection methods
          	 
          	 RouteClass.methodBuffer = new StringBuffer();
          	 RouteClass.methodBuffer.append(str+str1);
          	 RouteClass.writeRoute();
                
 
        }
        
        
        if(e.getSource()==G03MenuTree.comB3ly||
                e.getSource()==G03MenuTree.comB3p8 ||
                e.getSource()==G03MenuTree.comB1ly || 
                e.getSource()==G03MenuTree.comPbe1 ||
                e.getSource()==G03MenuTree.comMpw1 ||
                e.getSource()==G03MenuTree.comB3pw ||
                e.getSource()==G03MenuTree.mhfItem ||
                e.getSource()==G03MenuTree.mp2Item ||
                e.getSource()== G03MenuTree.othB1b9 ||
                e.getSource()== G03MenuTree.othB98 ||
                e.getSource()== G03MenuTree.othB971 ||
                e.getSource()== G03MenuTree.othB972 ||
                e.getSource()== G03MenuTree.othBhan ||
                e.getSource()== G03MenuTree.othBhlyp ||
                e.getSource()== G03MenuTree.othLsda ||
                e.getSource()== G03MenuTree.stdVsxc ||
                e.getSource()== G03MenuTree.stdHcth ||
                e.getSource()== G03MenuTree.stdHct9 ||
                e.getSource()== G03MenuTree.stdHct14 ||
                e.getSource()== G03MenuTree.stdHct40
                )
        {
        System.out.println("Eureka!!!");
        G03MenuTree.basisSetMenu.setEnabled(true);     
  	   RunFrame.createThreeOptionFrame();
  	   str1=e.getActionCommand();
  	   
  	   System.out.println(selectOpt);
  	    }  
        
        
        if(e.getSource()== G03MenuTree.pm3Item ||
                e.getSource()== G03MenuTree.am1Item||
                e.getSource()== G03MenuTree.mindoItem||
                e.getSource()== G03MenuTree.mndoItem )
        
        {
           //Semi Emperical Disable the Basis Set Menu.
           G03MenuTree.basisSetMenu.setEnabled(false);         
           RunFrame.createThreeOptionFrame();
      	   str1=e.getActionCommand();
      	   System.out.println(selectOpt);
      	   // if a basis set is already selected delete it....
      	   InsertNode.deleteChildren("Basis");
      	   RouteClass.basisBuffer= new StringBuffer();
      	   
      	   
        }  
        
        
       
        if(e.getSource()== G03MenuTree.mp3Item ||
                e.getSource()== G03MenuTree.mp4Item ||
                e.getSource()== G03MenuTree.mp4dItem ||
                e.getSource()== G03MenuTree.mp4sItem ||
                e.getSource()== G03MenuTree.ccdItem ||
                e.getSource()== G03MenuTree.ccsItem ||
                e.getSource()== G03MenuTree.ccsdItem ||
                e.getSource()== G03MenuTree.cidItem ||
                e.getSource()== G03MenuTree.cisdcItem ||
                e.getSource()== G03MenuTree.cisItem||
                e.getSource()== G03MenuTree.cisdItem||
                e.getSource()== G03MenuTree.sacItem ||
                e.getSource()== G03MenuTree.tdItem ||
                e.getSource()== G03MenuTree.tddItem ||
                e.getSource()== G03MenuTree.bdItem ||
                e.getSource()== G03MenuTree.gvbItem ||
                e.getSource()== G03MenuTree.ovgfItem ||
                e.getSource()== G03MenuTree.w1Item ||
                e.getSource()== G03MenuTree.zinItem ||
                e.getSource()== G03MenuTree.g1Item ||
                e.getSource()== G03MenuTree.g2Item ||
                e.getSource()== G03MenuTree.g2mp2Item ||
                e.getSource()== G03MenuTree.g3Item ||
                e.getSource()== G03MenuTree.g3mp2Item ||
                e.getSource()== G03MenuTree.g3b3Item ||
                e.getSource()== G03MenuTree.g3mp2b3Item||
				e.getSource()==G03MenuTree.mcasscfItem||
				e.getSource()==G03MenuTree.mcasscfmItem
                  
                )
        {
            G03MenuTree.basisSetMenu.setEnabled(true);     
           RunFrame.createTwoOptionFrame();
       	   str1=e.getActionCommand();
       	   System.out.println(selectOpt);
        }
        
        
        if(    e.getSource()== G03MenuTree.pm3mmItem ||
                e.getSource()== G03MenuTree.cndoItem ||
                e.getSource()== G03MenuTree.indoItem ||
                e.getSource()== G03MenuTree.cbs4Item ||
                e.getSource()== G03MenuTree.cbsaItem ||
                e.getSource()== G03MenuTree.cbslItem ||
                e.getSource()== G03MenuTree.cbsqItem ||
                e.getSource()== G03MenuTree.cbsqbItem )
        {
            //Semi Emperical disable the Basis Set Menu. &
            // CBS Menu Item
            G03MenuTree.basisSetMenu.setEnabled(false);
            RunFrame.createTwoOptionFrame();
        	   str1=e.getActionCommand();
        	   System.out.println(selectOpt);
//        	 if a basis set is already selected delete it....
          	   InsertNode.deleteChildren("Basis");
          	   RouteClass.basisBuffer= new StringBuffer();
            
        }
        
        
        
        if(e.getSource()== G03MenuTree.mp5Item)
        {
            InsertNode.deleteChildren("Me");
            InsertNode.insertNode("Me",e.getActionCommand());
            G03MenuTree.basisSetMenu.setEnabled(true);          
        }
        
     
        
        
          
        
        
        
        
        
}
    
    public void itemStateChanged(ItemEvent ie)
    {
        if(ie.getItem()==MenuListeners.hfsB ||
        		ie.getItem()==MenuListeners.lypB ||
        		ie.getItem()==MenuListeners.hfbB ||
        		ie.getItem()==MenuListeners.pw9B ||
        		ie.getItem()==MenuListeners.xalB ||
        		ie.getItem()==MenuListeners.p86B ||
        		ie.getItem()==MenuListeners.mpwB ||
        		ie.getItem()==MenuListeners.plB ||
        		ie.getItem()==MenuListeners.g96B ||
        		ie.getItem()==MenuListeners.b95B ||
        		ie.getItem()==MenuListeners.pbeB ||
        		ie.getItem()==MenuListeners.mpbB ||
        		ie.getItem()==MenuListeners.mpeB ||
        		ie.getItem()==MenuListeners.vwnB ||
        		ie.getItem()==MenuListeners.oB ||
        		ie.getItem()==MenuListeners.vw5B)
        {        	
        System.out.println("Eureka!!!");
        
            
            if(ie.getItem()==MenuListeners.hfsB ||
            		ie.getItem()==MenuListeners.hfbB ||
            		
            		ie.getItem()==MenuListeners.xalB ||
            		
            		ie.getItem()==MenuListeners.mpwB ||
            		
            		ie.getItem()==MenuListeners.g96B ||
            		
            		ie.getItem()==MenuListeners.pbeB ||
            		
            		ie.getItem()==MenuListeners.mpeB ||
            		
            		ie.getItem()==MenuListeners.oB )
            {
            // Exchange Functionals    
                flag=1;
                if(((JRadioButton)ie.getItem()).isSelected())
                { 
               
                
                if(exchngFlag>1)
                { //Show an Error
                    
                 JOptionPane.showMessageDialog(null,"You can select only one Exchange Functional","Error",JOptionPane.ERROR_MESSAGE);
                 ((JRadioButton)ie.getItem()).setSelected(false);
                 exchngFlag--;
                }
                else{
                    // Only one Item Selected 
                    fnName=ie.getItem().toString();
                    System.out.print(fnName);
                    exchngName=RadioButtonEditor.button.getActionCommand();
                    //fnName=ie.getItem();
                    //exchngName=exchng.get(fnName).toString();
                    //System.out.println(exchngName);
                  showOptionsFrame();
                }
            
                exchngFlag++;
                }
                
                else //deselected
                {
                    
                    if(exchngName.equals(RadioButtonEditor.button.getActionCommand()))
                    {
                        exchngName="";
                    }
                    exchngFlag--;
                }
                
                
                
            }
            
            if(		ie.getItem()==MenuListeners.lypB ||
            	
            		ie.getItem()==MenuListeners.pw9B ||
            		
            		ie.getItem()==MenuListeners.p86B ||
            		
            		ie.getItem()==MenuListeners.plB ||
            		
            		ie.getItem()==MenuListeners.b95B ||
            		
            		ie.getItem()==MenuListeners.mpbB ||
            		
            		ie.getItem()==MenuListeners.vwnB ||
            		
            		ie.getItem()==MenuListeners.vw5B)
            { 
                // Correlation Function
                flag=2;
                if(((JRadioButton)ie.getItem()).isSelected())
                { 
                corrFlag++;
                
                if(corrFlag>1)
                { //Show an Error
                    
                 JOptionPane.showMessageDialog(null,"You can select only one Correlational Functional","Error",JOptionPane.ERROR_MESSAGE);
                 ((JRadioButton)ie.getItem()).setSelected(false);
                 corrFlag--;
                }
                else{
                    // Only one Item Selected 
                    //cfnName=ie.getItem().toString();
                    corrName=RadioButtonEditor.button.getActionCommand();
                    //corrName=corr.get(cfnName).toString();
                    System.out.println(corrName);
                    showOptionsFrame();
                }
            
                
                }
                
                else //deselected
                {
                    if(corrName.equals(RadioButtonEditor.button.getActionCommand()))
                    {
                        corrName="";
                    }
                    corrFlag--;
                }
                
                
            }
            
         }  
        
    }




    public static void createHTable()
    {
        exchng.put("HFS(S)",new Integer(0));
        exchng.put("HFB(B)",new Integer(0));
        exchng.put("Xalpha(XA)",new Integer(0));
        exchng.put("MPW",new Integer(0));
        exchng.put("G96",new Integer(0));
        exchng.put("PBE",new Integer(0));
        exchng.put("MPBE",new Integer(0));
        exchng.put("O",new Integer(0));
        
        corr.put("LYP",new Integer(0));
        corr.put("PW91",new Integer(0));
        corr.put("P86",new Integer(0));
        corr.put("PL",new Integer(0));
        corr.put("B95",new Integer(0));
        corr.put("MPBE",new Integer(0));
        corr.put("VWN",new Integer(0));
        corr.put("VWN5",new Integer(0));
        
        
        
       
    }

}

