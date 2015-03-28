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
 * @author Sandeep Kumar Seethaapathy @author Shashank Jeedigunta  
 * 
 */


package G03Input;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.Vector;

public class GuessOptTable extends JFrame implements ItemListener,ActionListener {
	Color bgColor= new Color(236,233,216);
	Color foreColor =new Color(0,78,152);
	JPanel donePanel;
	JButton doneButton,clearButton,exitButton;
	public static JTable table;
	JFrame guessOptFrame;
	public static JRadioButton GHarri=new JRadioButton("Harris");
	public static JRadioButton GLowsy=new JRadioButton("LowSymm");
	public static JRadioButton GPermu = new JRadioButton("Permute");
	public static JRadioButton GPrint = new JRadioButton("Print");
	public static JRadioButton GProje= new JRadioButton("Projected");
	public static JRadioButton GNosym =new JRadioButton("NoSymm");
	public static JRadioButton GAlter = new JRadioButton("Alter");
	public static JRadioButton GNatur =new JRadioButton("NaturalOrbitals");
	public static JRadioButton GHucke = new JRadioButton("Huckel");
	public static JRadioButton GMix = new JRadioButton("Mix");
	public static JRadioButton GOldhu = new JRadioButton("OldHuckel");
	public static JRadioButton GLocal = new JRadioButton("Local");
	public static JRadioButton  GIndo = new JRadioButton("INDO");
	public static JRadioButton GCards= new JRadioButton("Cards");
	public static JRadioButton GAm1 = new JRadioButton("AM1");
	public static JRadioButton GForce = new JRadioButton("ForceAbelianSymmetry");
	public static JRadioButton GCore = new JRadioButton("Core");
	public static JRadioButton GRead = new JRadioButton("Read");
	public static JRadioButton  GAlway =new JRadioButton("Always");
	public static JRadioButton GOnly = new JRadioButton("Only");
	public static JRadioButton GDensi =new JRadioButton("DensityMix[=N]");
	public static JRadioButton GTrans = new JRadioButton("Translate");
	public static JRadioButton GNotra=new JRadioButton("NoTranslate");
	public static JRadioButton GAlpha = new JRadioButton("Alpha");
	public static JRadioButton GExtra = new JRadioButton("Extra");
	public static JRadioButton GNoext = new JRadioButton("NoExtra");
    public static Vector<String> guessOpt;
    public static Vector<JRadioButton> guessClear;
public static int guessC,guessOptC;
public static int guessFlag,guessListen;
    public GuessOptTable(){
    	
    guessOptFrame = new JFrame("Guess Options");
   if(guessListen==0)
   {
   	guessClear=new Vector<JRadioButton>(40);
   	guessClear.addElement(GHarri);
   	guessClear.addElement(GLowsy);
   	guessClear.addElement(GPermu);
   	guessClear.addElement(GPrint);
   	guessClear.addElement(GProje);
   	guessClear.addElement(GNosym);
   	guessClear.addElement(GAlter);
   	guessClear.addElement(GNatur);
   	guessClear.addElement(GHucke);
   	guessClear.addElement(GMix);
   	guessClear.addElement(GOldhu);
   	guessClear.addElement(GLocal);
   	guessClear.addElement(GIndo);
   	guessClear.addElement(GCards);
   	guessClear.addElement(GAm1);
   	guessClear.addElement(GForce);
   	guessClear.addElement(GCore);
   	guessClear.addElement(GRead);
   	guessClear.addElement(GAlway);
   	guessClear.addElement(GOnly);
   	guessClear.addElement(GTrans);
   	guessClear.addElement(GNotra);
   	guessClear.addElement(GAlpha);
   	guessClear.addElement(GExtra);
   	guessClear.addElement(GNoext);
   	
   	GHarri.addItemListener(this);
    GHarri.setBackground(Color.WHITE);
    GLowsy.addItemListener(this);
    GLowsy.setBackground(Color.WHITE);
    GPermu.addItemListener(this);
    GPermu.setBackground(Color.WHITE);
    GPrint.addItemListener(this);
    GPrint.setBackground(Color.WHITE);
    GProje.addItemListener(this);
    GProje.setBackground(Color.WHITE);
    GAlter.addItemListener(this);
    GAlter.setBackground(Color.WHITE);
    GNosym.addItemListener(this);
    GNosym.setBackground(Color.WHITE);
    GNatur.addItemListener(this);
    GNatur.setBackground(Color.WHITE);
    GHucke.addItemListener(this);
    GHucke.setBackground(Color.WHITE);
    GMix.addItemListener(this);
    GMix.setBackground(Color.WHITE);
    GOldhu.addItemListener(this);
    GOldhu.setBackground(Color.WHITE);
    GLocal.addItemListener(this);
    GLocal.setBackground(Color.WHITE);
    GIndo.addItemListener(this);
    GIndo.setBackground(Color.WHITE);
    GCards.addItemListener(this);
    GCards.setBackground(Color.WHITE);
    GAm1.addItemListener(this);
    GAm1.setBackground(Color.WHITE);
    GForce.addItemListener(this);
    GForce.setBackground(Color.WHITE);
    GCore.addItemListener(this);
    GCore.setBackground(Color.WHITE);
    GRead.addItemListener(this);
    GRead.setBackground(Color.WHITE);
    GAlway.addItemListener(this);
    GAlway.setBackground(Color.WHITE);
    GOnly.addItemListener(this);
    GOnly.setBackground(Color.WHITE);
  //  GSave.addItemListener(this);
  //  GSave.setBackground(Color.WHITE);
    GDensi.addItemListener(this);
    GDensi.setBackground(Color.WHITE);
    GTrans.addItemListener(this);
    GTrans.setBackground(Color.WHITE);
    GNotra.addItemListener(this);
    GNotra.setBackground(Color.WHITE);
    GAlpha.addItemListener(this);
    GAlpha.setBackground(Color.WHITE);
    GExtra.addItemListener(this);
    GExtra.setBackground(Color.WHITE);
    GNoext.addItemListener(this);
    GNoext.setBackground(Color.WHITE);
    guessListen++;
   }
      
    //UIDefaults ui = UIManager.getLookAndFeel().getDefaults();
    
  //UIManager.put("RadioButton.focus", ui.getColor("control"));
    guessOptFrame.setBackground(bgColor);
  	guessOptFrame.setForeground(foreColor);
    DefaultTableModel dm = new DefaultTableModel();
    dm.setDataVector(
      new Object[][]{
      		
        {GHarri,GLowsy,GPermu,GPrint},
        {GProje,GNosym,GAlter,GNatur},
		{GHucke,null,GMix,null},
        {GOldhu,null,GLocal,null},
        {GIndo,null,GCards,null},
        {GAm1,null,GForce,null},
        {GCore,null,null,null},
        {GRead,null,null,null},
        {GAlway,null,null,null},
        {GOnly,null,null,null},
       // {GSave,null,null,null},
        {GDensi,null,null,null},
        {GTrans,null,null,null},
        {GNotra,null,null,null},
        {GAlpha,null,null,null},
        {GExtra,null,null,null},
        {GNoext,null,null,null},},
        
      new Object[]{"Initial Guess","Orbital Symmetry","Orbital Manipulation","Additional Options"});
                     
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
   table.getColumn("Initial Guess").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Initial Guess").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    
    table.getColumn("Orbital Symmetry").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Orbital Symmetry").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    table.getColumn("Orbital Manipulation").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Orbital Manipulation").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    
    table.getColumn("Additional Options").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Additional Options").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    
    doneButton = new JButton("Done");
    doneButton.addActionListener(this);
    //clearButton = new JButton("Clear");
    exitButton = new JButton("Exit");
    //doneButton.addActionListener()
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
    
    
    
    guessOptFrame.getContentPane().setLayout(new BorderLayout());
    JScrollPane scroll = new JScrollPane(table);
    guessOptFrame.getContentPane().add( scroll,BorderLayout.CENTER);
    guessOptFrame.getContentPane().add(donePanel,BorderLayout.SOUTH);
    guessOptFrame.setSize( 600,200);
    guessOptFrame.setLocation(200,75);
    guessOptFrame.setVisible(true);
  }
    public void actionPerformed(ActionEvent ae)
   {    if(ae.getSource()==exitButton)
   {
   	guessOptFrame.dispose();
   }
    	if(ae.getSource()==doneButton){
    	   	System.out.println("The value of freq guessV = "+guessC);
    	   	if(RouteClass.initCount==0)
    	   	{
    	    System.out.println(RouteClass.initCount+" \tInside init");
    	   	RouteClass.initBuffer();
    	   	RouteClass.initCount++;
    	   	}
    	   	if(guessC==0)
    	   	{
    	   	System.out.println(guessC+" \t Inside guessC");
    	   	guessFlag=RouteClass.keyIndex;
    	   	RouteClass.keyIndex++;
    	   	guessC++;
    	   	}
    	   	else
    	   	{
    	   		System.out.println("Guess Check");
    	   		RouteClass.keywordBuffer[guessFlag]=new StringBuffer();
    	   	}
    	   	RouteClass.keywordBuffer[guessFlag].append("Guess");
    		if(!(InsertNode.nodeExists("Guess")))
    		    InsertNode.insertNode("Job","Guess");
    		try {
				if(guessOpt.size()>1)
				{
				 RouteClass.keywordBuffer[guessFlag].append("=(");
				 for(int count=0;count<guessOpt.size();count++)
				    {
				    	RouteClass.keywordBuffer[guessFlag].append(guessOpt.get(count));
				    	if(!(count==(guessOpt.size()-1)))
				    	{
				    	RouteClass.keywordBuffer[guessFlag].append(",");
				    	}
				    }
				    RouteClass.keywordBuffer[guessFlag].append(")");
				  }
				 else
				 {
				 	if(guessOpt.size()>0)
				 	RouteClass.keywordBuffer[guessFlag].append("="+"("+guessOpt.get(0)+")");
				 }
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			}
    		 //guessFlag=RouteClass.keyIndex;
    		 guessOptFrame.dispose();
    		 RouteClass.writeRoute();
    	   }
   }
    	 
	 
	public void itemStateChanged(ItemEvent e)
	{if((e.getItem()==GHarri)||
			(e.getItem()==GLowsy)||
			(e.getItem()==GPermu)||
			(e.getItem()==GPrint)||
			(e.getItem()==GProje)||
			(e.getItem()==GNosym)||
			(e.getItem()==GAlter)||   
			(e.getItem()==GNatur)||
			(e.getItem()==GHucke)||
			(e.getItem()==GMix)||
			(e.getItem()==GOldhu)||
			(e.getItem()==GLocal)||
			(e.getItem()==GIndo)||
			(e.getItem()==GCards)||
			(e.getItem()==GAm1)||
			(e.getItem()==GForce)||
			(e.getItem()==GRead)||
			(e.getItem()==GAlway)||
			(e.getItem()==GOnly)||
	//		(e.getItem()==GSave)||
			(e.getItem()==GTrans)||
			(e.getItem()==GNotra)||
			(e.getItem()==GAlpha)||
			(e.getItem()==GExtra)||
			(e.getItem()==GNoext))
			{
		G03Listener.guessTabF++;
		if(((JRadioButton)e.getItem()).isSelected())
		{
			if(guessOptC==0)
			{
				guessOpt = new Vector<String>(10);
				guessOptC++;
			}
		if(!(InsertNode.nodeExists("Guess")))
		
			InsertNode.insertNode("Jo","Guess");
		InsertNode.insertNode("Guess",RadioButtonEditor.button.getActionCommand());
		String guess  =RadioButtonEditor.button.getActionCommand();
		guessOpt.addElement(guess);
    	//guessOptCount++;
		}//System.out.println(e.getActionCommand());
			
		else
		{
			if(InsertNode.nodeExists(((JRadioButton)e.getItem()).getActionCommand()))
	    		InsertNode.deleteNode(((JRadioButton)e.getItem()).getActionCommand());
	    		guessOpt.removeElement(((JRadioButton)e.getItem()).getActionCommand());
		}
			}
	}
	
  public static void main(String[] args) {
    GuessOptTable frame = new GuessOptTable();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
  }
}
