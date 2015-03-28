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
 * @author Sandeep Kumar Seethaapathy @author Shashank Jeedigunta  
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


public class popOptTable extends JFrame implements ItemListener,ActionListener 
{
		Color bgColor= new Color(236,233,216);
		Color foreColor =new Color(0,78,152);
		JPanel donePanel;
		JButton doneButton,clearButton,exitButton;
		public static JTable table;
		JFrame popOptFrame;
		public static JRadioButton PNone=new JRadioButton("None");
		public static JRadioButton PMini=new JRadioButton("Minimal");
		public static JRadioButton PRegu = new JRadioButton("Reg");
		public static JRadioButton PFull = new JRadioButton("Full");
		public static JRadioButton PBond= new JRadioButton("Bonding");
		public static JRadioButton PNatur =new JRadioButton("Natural Orbitals");
		public static JRadioButton PNaturS = new JRadioButton("NaturalSpinOrbitals");
		public static JRadioButton PAlpha = new JRadioButton("AlphaNatural");
		public static JRadioButton PBeta = new JRadioButton("BetaNatural");
		public static JRadioButton PSpin = new JRadioButton("SpinNatural");
		public static JRadioButton PNbo = new JRadioButton("NBO");
		public static JRadioButton PNpa= new JRadioButton("NPA");
		public static JRadioButton PNboRe = new JRadioButton("NBORead");
		public static JRadioButton PNboD = new JRadioButton("NBODel");
		public static JRadioButton PSavNB = new JRadioButton("SaveNBOs");
		public static JRadioButton PSaveLMNOs = new JRadioButton("SaveLMNOs");
		public static JRadioButton PSavmix =new JRadioButton("SaveMixed");
		public static Vector<String> popOpt;
	    public static Vector<JRadioButton> popClear;
	public static int popOptC;
	public static int popFlag,popListen,popC;
	    public popOptTable(){
	    	
	    popOptFrame = new JFrame("Pop Options");
	   if(popListen==0)
	   {
	   	popClear=new Vector<JRadioButton>(40);
	   	popClear.addElement(PNone);
	   	popClear.addElement(PMini);
	   	popClear.addElement(PRegu);
	   	popClear.addElement(PFull);
	   	popClear.addElement(PBond);
	   	popClear.addElement(PNatur);
	   	popClear.addElement(PNaturS);
	   	popClear.addElement(PAlpha);
	   	popClear.addElement(PBeta);
	   	popClear.addElement(PSpin);
	   	popClear.addElement(PNbo);
	   	popClear.addElement(PNpa);
	   	popClear.addElement(PNboRe);
	   	popClear.addElement(PNboD);
	   	popClear.addElement(PSavNB);
	   	popClear.addElement(PSaveLMNOs);
	   	popClear.addElement(PSavmix);
	   	
	   	PNone.addItemListener(this);
	    PNone.setBackground(Color.WHITE);
	    PMini.addItemListener(this);
	    PMini.setBackground(Color.WHITE);
	    PRegu.addItemListener(this);
	    PRegu.setBackground(Color.WHITE);
	    PFull.addItemListener(this);
	    PFull.setBackground(Color.WHITE);
	    PBond.addItemListener(this);
	    PBond.setBackground(Color.WHITE);
	    PNatur.setBackground(Color.WHITE);
	    PNatur.addItemListener(this);
	    PNaturS.setBackground(Color.WHITE);
	    PNaturS.addItemListener(this);
	    PAlpha.setBackground(Color.WHITE);
	    PAlpha.addItemListener(this);
	    PBeta.setBackground(Color.WHITE);
	    PBeta.addItemListener(this);
	    PSpin.setBackground(Color.WHITE);
	    PSpin.addItemListener(this);
	    PNbo.setBackground(Color.WHITE);
	    
	    PNbo.addItemListener(this);
	    PNpa.addItemListener(this);
	    PNpa.setBackground(Color.WHITE);
	    PNboRe.addItemListener(this);

	    PNboRe.setBackground(Color.WHITE);
	    
	    PNboD.addItemListener(this);
	    PNboD.setBackground(Color.WHITE);
	    PSavNB.setBackground(Color.WHITE);
	    PSavNB.addItemListener(this);
	    PSaveLMNOs.setBackground(Color.WHITE);
	    PSaveLMNOs.addItemListener(this);
	    PSavmix.setBackground(Color.WHITE);
	    PSavmix.addItemListener(this);
	   
	    popListen++;
	   }
	      
	    //UIDefaults ui = UIManager.getLookAndFeel().getDefaults();
	    
	  //UIManager.put("RadioButton.focus", ui.getColor("control"));
	    popOptFrame.setBackground(bgColor);
	  	popOptFrame.setForeground(foreColor);
	    DefaultTableModel dm = new DefaultTableModel();
	    dm.setDataVector(
	      new Object[][]{
	      		
	        {PNone,PBond,PNatur,PNbo},
	        {PMini,null,PNaturS,PNpa},
			{PRegu,null,PAlpha,PNboRe},
	        {PFull,null,PBeta,PNboD},
	        {null,null,null,PSavNB},
	        {null,null,null,PSaveLMNOs},
	        {null,null,null,PSavmix},
	        {null,null,null,null},},
	        
	      new Object[]{"OutputFile","Bonding Analysis","Natural Orbitals","Natural Bond Orbitals"});
	                     
	    JTable table = new JTable(dm) {
	      public void tableChanged(TableModelEvent e) {
	        super.tableChanged(e);
	        repaint();
	      }
	    };
	    
	    table.getColumn("OutputFile").setCellRenderer(new RadioButtonRenderer());
	    table.getColumn("OutputFile").setCellEditor(new RadioButtonEditor(new JCheckBox()));
	    
	    table.getColumn("Bonding Analysis").setCellRenderer(new RadioButtonRenderer());
	    table.getColumn("Bonding Analysis").setCellEditor(new RadioButtonEditor(new JCheckBox()));
	    table.getColumn("Natural Orbitals").setCellRenderer(new RadioButtonRenderer());
	    table.getColumn("Natural Orbitals").setCellEditor(new RadioButtonEditor(new JCheckBox()));
	    
	    table.getColumn("Natural Bond Orbitals").setCellRenderer(new RadioButtonRenderer());
	    table.getColumn("Natural Bond Orbitals").setCellEditor(new RadioButtonEditor(new JCheckBox()));
	    
	    doneButton = new JButton("Done");
	    doneButton.addActionListener(this);
	    //clearButton = new JButton("Clear");
	    exitButton = new JButton("Exit");
	    //doneButton.addActionListener()
	   // clearButton.addActionListener(this);
	    exitButton.addActionListener(this);
	    
	    donePanel = new JPanel();
	    donePanel.add(doneButton);
	   
	   // donePanel.add(clearButton);
	    donePanel.add(exitButton);
	    
	    /*table.setBackground(bgColor);
	    table.setForeground(foreColor);
	    */
	    
	    
	    donePanel.setBackground(bgColor);
	    donePanel.setForeground(foreColor);
	    
	    
	    table.getTableHeader().setReorderingAllowed(false);
	    
	    
	    
	    popOptFrame.getContentPane().setLayout(new BorderLayout());
	    JScrollPane scroll = new JScrollPane(table);
	    popOptFrame.getContentPane().add( scroll,BorderLayout.CENTER);
	    popOptFrame.getContentPane().add(donePanel,BorderLayout.SOUTH);
	    popOptFrame.setSize( 600,200);
	    popOptFrame.setLocation(200,75);
	    popOptFrame.setVisible(true);
	  }
	    public void actionPerformed(ActionEvent ae)
	   {
	    	if(ae.getSource()==exitButton)
	    	{
	    		popOptFrame.dispose();
	    	}
	     	System.out.println("flagwwwww"+popOptC);
	    	if(ae.getSource()==doneButton){
	    	   	
	    	   	if(RouteClass.initCount==0)
	    	   	{
	    	    System.out.println(RouteClass.initCount+" \tInside init");
	    	   	RouteClass.initBuffer();
	    	   	RouteClass.initCount++;
	    	   	}
	    	   	if(popC==0)
	    	   	{
	    	   	System.out.println(popOptC+" \t Inside guessC");
	    	   	popFlag=RouteClass.keyIndex;
	    	   	
	    	   	RouteClass.keyIndex++;
	    	   	System.out.println("flag"+RouteClass.keyIndex);
	    	   	popC++;
	    	   	}
	    	   	else
	    	   	{
	    	   		System.out.println("flag"+popFlag);
	    	   		RouteClass.keywordBuffer[popFlag]=new StringBuffer();
	    	   	}
	    	   	RouteClass.keywordBuffer[popFlag].append("Pop");
	    		if(!(InsertNode.nodeExists("Pop")))
	    		    InsertNode.insertNode("Key","Pop");
	    			try {
					if(popOpt.size()>1)
					{
					 RouteClass.keywordBuffer[popFlag].append("=(");
					 System.out.println("inside si" + popOpt.size());
					 for(int count=0;count<popOpt.size();count++)
					    {
					    	RouteClass.keywordBuffer[popFlag].append(popOpt.get(count));
					    	
					    	if(!(count==(popOpt.size()-1)))
					    	{
					    	RouteClass.keywordBuffer[popFlag].append(",");
					    	}
					    }
					    RouteClass.keywordBuffer[popFlag].append(")");
					  }
					 else
					 {
					 	if(popOpt.size()>0)
					 	RouteClass.keywordBuffer[popFlag].append("="+"("+popOpt.get(0)+")");
					 }
					 //guessFlag=RouteClass.keyIndex;
					System.out.println("buffer"+RouteClass.keywordBuffer[popFlag]);
					 popOptFrame.dispose();
					 RouteClass.writeRoute();
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
				//	e.printStackTrace();
				}
	    	   }
	   }
	    	 
		 
		public void itemStateChanged(ItemEvent e)
		
						
		{if((e.getItem()==PNone)||
				(e.getItem()==PBond)||
				(e.getItem()==PNatur)||
				(e.getItem()==PNbo)||
				(e.getItem()==PMini)||
				(e.getItem()==PNaturS)||
				(e.getItem()==PNbo)||
				(e.getItem()==PNpa)||   
				(e.getItem()==PRegu)||
				(e.getItem()==PAlpha)||
				(e.getItem()==PNboRe)||
				(e.getItem()==PFull)||
				(e.getItem()==PBeta)||
				(e.getItem()==PNboD)||
				(e.getItem()==PSavNB)||
				(e.getItem()==PSaveLMNOs)||
				(e.getItem()==PSavmix))
								{

			if(((JRadioButton)e.getItem()).isSelected())
			{
				if(popOptC==0)
				{
					G03Listener.popTabF++;
					popOpt = new Vector<String>(10);
					popOptC++;
				}
			if(!(InsertNode.nodeExists("Pop")))
			
				InsertNode.insertNode("Key","Pop");
			InsertNode.insertNode("Pop",RadioButtonEditor.button.getActionCommand());
			String guess  =RadioButtonEditor.button.getActionCommand();
			popOpt.addElement(guess);
			System.out.println("size of the vector"+popOpt.size());
	    	//popOptCount++;
			}//System.out.println(e.getActionCommand());
				
			else
			{
				if(InsertNode.nodeExists(((JRadioButton)e.getItem()).getActionCommand()))
		    		InsertNode.deleteNode(((JRadioButton)e.getItem()).getActionCommand());
		    		popOpt.removeElement(((JRadioButton)e.getItem()).getActionCommand());
			}
				}
		}
		
	  public static void main(String[] args) {
	    popOptTable frame = new popOptTable();
	    frame.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });
	  }
	}

	
	


