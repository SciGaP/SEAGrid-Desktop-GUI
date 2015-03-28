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
 * Created on Mar 21, 2005
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

import java.util.*;
class RadioButtonRenderer implements TableCellRenderer {
  public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
    if (value==null) return null;
    return (Component)value;
  }
}

class RadioButtonEditor extends   DefaultCellEditor
                        implements ItemListener {
  
	public static JRadioButton button;
  public RadioButtonEditor(JCheckBox checkBox) {
    super(checkBox);
   
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
                   boolean isSelected, int row, int column) {
    if (value==null) return null;
    button = (JRadioButton)value;
    System.out.println(row);
    G03MenuTree.freOptString=button.getActionCommand();
    System.out.println("sasa"+ value.toString());
    //InsertNode.insertNode("Jo",G03MenuTree.freOptString);
    button.addItemListener(this);
    System.out.println((Component)value);
    System.out.println(value.getClass());
    return (Component)value;
  }

  public Object getCellEditorValue() {
    button.removeItemListener(this);
    return button;
  }

  public void itemStateChanged(ItemEvent e) {
  	
    super.fireEditingCanceled();
    
  }
}

public class FreqOptTable extends JFrame implements ItemListener,ActionListener{
public static JFrame freqOptFrame;
Color bgColor= new Color(236,233,216);
Color foreColor =new Color(0,78,152);
JPanel donePanel;
JButton doneButton,clearButton,exitButton;
public static JTable table; 
public static int freqOptCount;
public static int opop;
public static Vector<String> freqOpt;
public static String[] archiveFreq;
public static JTextField stepField;
public static int freqFlag,freqC,freqOptC;
 String stepSel;



// List of all JButton in FreqTable
 public static JRadioButton FRaman=  new JRadioButton("Raman");

 public static JRadioButton FReadf = new JRadioButton("ReadFC");
 public static JRadioButton FHpmod = new JRadioButton("HPModes");
 public static JRadioButton FAnaly = new JRadioButton("Analytic");
 public static JRadioButton FProje = new JRadioButton("Projected");
 public static JRadioButton FNrama = new JRadioButton("NRaman");
 public static JRadioButton FReada = new JRadioButton("ReadAnharm");
 public static JRadioButton FIntmo = new JRadioButton("IntModes");
 public static JRadioButton FNumer = new JRadioButton("Numerical");
 public static JRadioButton FHinde = new JRadioButton("Hindered Rotor");
 public static JRadioButton FNnram	 = new JRadioButton("NNRaman");
 public static JRadioButton FModre = new JRadioButton("ModRedundant");
 public static JRadioButton FEnonl = new JRadioButton("EnOnly");
 public static JRadioButton FNoram = new JRadioButton("NoRaman");
 public static JRadioButton FReadi = new JRadioButton("ReadIsotopes");
 public static JRadioButton FCubic = new JRadioButton("Cubic");
 public static JRadioButton FVcd = new JRadioButton("VCD");
 public static JRadioButton FResta = new JRadioButton("Restart");
 public static JRadioButton FStep = new JRadioButton("Step=N");
 public static JRadioButton FVibro = new JRadioButton("VibRot");
 public static JRadioButton FAnhar = new JRadioButton("Anharmonic");
 public static Vector<JRadioButton> freqClear;
public FreqOptTable(){
	super("Options for Freq");
	if(opop==0)
	{
		freqClear=new Vector<JRadioButton>(30);
		freqClear.addElement(FRaman);
	freqClear.addElement(FReadf);
	freqClear.addElement(FHpmod);
	freqClear.addElement(FAnaly);
	freqClear.addElement(FProje);
	freqClear.addElement(FNrama);
	freqClear.addElement(FReada);
	freqClear.addElement(FIntmo);
	freqClear.addElement(FNumer);
	freqClear.addElement(FHinde);
	freqClear.addElement(FNnram);
	freqClear.addElement(FModre);
	freqClear.addElement(FEnonl);
	freqClear.addElement(FNoram);
	freqClear.addElement(FReadi);
	freqClear.addElement(FCubic);
	freqClear.addElement(FVcd);
	freqClear.addElement(FResta);
	//freqClear.addElement(FStep);
	freqClear.addElement(FVibro);
	freqClear.addElement(FAnhar);
	
	FRaman.addItemListener(this);
	FRaman.setBackground(Color.WHITE);
	FReadf.addItemListener(this);
	FReadf.setBackground(Color.WHITE);
	FHpmod.setBackground(Color.WHITE);
	FHpmod.addItemListener(this);
	FAnaly.setBackground(Color.WHITE);
	FAnaly.addItemListener(this);
	FProje.setBackground(Color.WHITE);
	FProje.addItemListener(this);
	FNrama.setBackground(Color.WHITE);
	FNrama.addItemListener(this);
	FReada.setBackground(Color.WHITE);
	FReada.addItemListener(this);
	FIntmo.setBackground(Color.WHITE);
	FIntmo.addItemListener(this);
	FNumer.setBackground(Color.WHITE);
	FNumer.addItemListener(this);
	FHinde.setBackground(Color.WHITE);
	FHinde.addItemListener(this);
	FNnram.setBackground(Color.WHITE);
	FNnram.addItemListener(this);
	FModre.setBackground(Color.WHITE);
	FModre.addItemListener(this);
	FEnonl.setBackground(Color.WHITE);
	FEnonl.addItemListener(this);
	FNoram.setBackground(Color.WHITE);
	FNoram.addItemListener(this);
	FReadi.setBackground(Color.WHITE);
	FReadi.addItemListener(this);
	FCubic.setBackground(Color.WHITE);
	FCubic.addItemListener(this);
	FVcd.setBackground(Color.WHITE);
	FVcd.addItemListener(this);
	FResta.setBackground(Color.WHITE);
	FResta.addItemListener(this);
	FStep.setBackground(Color.WHITE);
	FStep.addItemListener(this);
	FVibro.setBackground(Color.WHITE);
	FVibro.addItemListener(this);
	FAnhar.setBackground(Color.WHITE);
	FAnhar.addItemListener(this);   
  
	opop=1;
	}
	System.out.println(opop);
	System.out.println("WQWQWQ");
  	JFrame.setDefaultLookAndFeelDecorated(true);
  	
  	freqOptFrame = new JFrame("Options for Freq");
  	freqOptFrame.setBackground(bgColor);
  	freqOptFrame.setForeground(foreColor);
    DefaultTableModel dm = new DefaultTableModel();
  dm.setDataVector(
      new Object[][]{
        {FRaman,FReadf,FHpmod,FAnaly,FProje},
        {FNrama,FReada,FIntmo,FNumer,FHinde},
        {FNnram,FModre,null,FEnonl,null},
        {FNoram,FReadi,null,FCubic,null},
        {FVcd,FResta,null,FStep,null},
        {FVibro,null,null,null,null},
        {FAnhar,null,null,null,null}},
		new Object[]{"Frequency and Intensity","Data Input","Output","Numerical Differentiation","Additional Options"});
                     
     table = new JTable(dm) {
      public void tableChanged(TableModelEvent e) {
        
        super.tableChanged(e);
        repaint();
        
      }
    };
  
  table=new JTable(dm);

    
       
   
   table.getColumn("Frequency and Intensity").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Frequency and Intensity").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    
    table.getColumn("Data Input").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Data Input").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    table.getColumn("Output").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Output").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    
    table.getColumn("Numerical Differentiation").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Numerical Differentiation").setCellEditor(new RadioButtonEditor(new JCheckBox()));
    
    table.getColumn("Additional Options").setCellRenderer(new RadioButtonRenderer());
    table.getColumn("Additional Options").setCellEditor(new RadioButtonEditor(new JCheckBox()));
  
    donePanel = new JPanel();
    doneButton = new JButton("Done");
   // clearButton = new JButton("Clear");
    exitButton = new JButton("Exit");
    doneButton.addActionListener(this);
   // clearButton.addActionListener(this);
    exitButton.addActionListener(this);
    
    
    donePanel.add(doneButton);
    //donePanel.add(clearButton);
    donePanel.add(exitButton);
    
    
    donePanel.setBackground(bgColor);
    donePanel.setForeground(foreColor);
    
    
    table.getTableHeader().setReorderingAllowed(false);
    freqOptFrame.getContentPane().setLayout(new BorderLayout());
    JScrollPane scroll = new JScrollPane(table);
    freqOptFrame.getContentPane().add( scroll,BorderLayout.CENTER);
    freqOptFrame.getContentPane().add(donePanel,BorderLayout.SOUTH);
    freqOptFrame.setSize( 600,200);
    freqOptFrame.setLocation(200,75);
    freqOptFrame.setVisible(true);
    
   
    }


public void actionPerformed(ActionEvent e)
{
   if(e.getSource()==doneButton)
   {
   	
   	System.out.println("The value of freq Flagv = "+freqC);
   	if(RouteClass.initCount==0)
   	{
   		System.out.println(RouteClass.initCount+" \tInside init");
   	RouteClass.initBuffer();
   	RouteClass.initCount++;
   	}
   	if(freqC==0)
   	{
   		
   
   	System.out.println(freqC+" \t Inside freQC");
   	freqFlag=RouteClass.keyIndex;
   	RouteClass.keyIndex++;
   	freqC++;
   	}
   	else
   	{
   		//System.out.println("Reinitialize");
   		RouteClass.keywordBuffer[freqFlag]=new StringBuffer();
   	}
   	RouteClass.keywordBuffer[freqFlag].append("Freq");
   	if(!(InsertNode.nodeExists("Freq")))
	    InsertNode.insertNode("Job","Freq");
	try {
		if(freqOpt.size()>1)
		{
		 RouteClass.keywordBuffer[freqFlag].append("=(");
		 for(int count=0;count<freqOpt.size();count++)
		    {
		    	RouteClass.keywordBuffer[freqFlag].append(freqOpt.get(count));
		    	if(!(count==(freqOpt.size()-1)))
		    	{
		    	RouteClass.keywordBuffer[freqFlag].append(",");
		    	}
		    }
		    RouteClass.keywordBuffer[freqFlag].append(")");
		  }
		 else
		 {
		 	if(freqOpt.size()>0)
		 	RouteClass.keywordBuffer[freqFlag].append("="+"("+freqOpt.get(0)+")");
		 }
	}
	catch (NullPointerException e1) {
		// TODO Auto-generated catch block
		//e1.printStackTrace();
	}
	 //freqFlag=RouteClass.keyIndex;
	 //RouteClass.keyIndex++;
	 RouteClass.writeRoute();
	 freqOptFrame.dispose();
	 System.out.println("Has focus" +table.hasFocus());
   }
   if(e.getSource()==exitButton)
   {
   freqOptFrame.dispose();
   
   }
 }

public void itemStateChanged(ItemEvent e)
	{
	System.out.println("chaeck");
    if((e.getItem()==FRaman)||(e.getItem()==FReadf)||	(e.getItem()==FHpmod)||
			(e.getItem()==FAnaly)||	(e.getItem()==FProje)||	(e.getItem()==FNrama)||
			(e.getItem()==FReada)|| (e.getItem()==FIntmo)||	(e.getItem()==FNumer)||
			(e.getItem()==FHinde)||	(e.getItem()==FNnram)|| (e.getItem()==FModre)||
			(e.getItem()==FEnonl)|| (e.getItem()==FNoram)|| (e.getItem()==FReadi)||
			(e.getItem()==FCubic)|| (e.getItem()==FVcd)|| (e.getItem()==FResta)||
			(e.getItem()==FVibro)||(e.getItem()==FAnhar)) 
			{
    	
    	System.out.println("Coming here");
    	if(((JRadioButton)e.getItem()).isSelected())
		{
    		G03Listener.freqTabF++;
    		System.out.println("Comin in");
			if(freqOptC==0)
	    	{
	    		freqOpt = new Vector<String>(10);
	    		freqOptC++;
	    	}
		System.out.println("\n Item get: = \t "+RadioButtonEditor.button.getActionCommand());
		if(!(InsertNode.nodeExists("Freq")))
		    InsertNode.insertNode("Job","Freq");
		InsertNode.insertNode("Freq",RadioButtonEditor.button.getActionCommand());
		((JRadioButton)e.getItem()).setEnabled(true);
		String freqs  =RadioButtonEditor.button.getActionCommand();
		freqOpt.addElement(freqs);
    	freqOptCount++;
    	}
		else
		{
			if(InsertNode.nodeExists(((JRadioButton)e.getItem()).getActionCommand()))
	    		InsertNode.deleteNode(((JRadioButton)e.getItem()).getActionCommand());
	    		freqOpt.removeElement(((JRadioButton)e.getItem()).getActionCommand());

		}
			}

    if((e.getItem()==FStep))
    { 
    	
    	if(freqOptC==0)
    	{
    		G03Listener.freqTabF++;
    		freqOpt = new Vector<String>(10);
    		freqOptC++;
    	}
    	 stepField=new JTextField(3);
		 stepField.setSize(2,1);
    	if(((JRadioButton)e.getItem()).isSelected())
		{
    	    	//POP UP A FRAME TO GET 'N'
    	    //JTextField stepField;
        	JPanel txt;
        	txt = new JPanel(new FlowLayout());
        	System.out.println("OOO");
    	//stepField = new JTextField(3);
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
    	    if(!(InsertNode.nodeExists("Freq")))
    		 {InsertNode.insertNode("Job","Freq");
    		 }
    	    //Delete Existing node and then add it
		    if((InsertNode.nodeExists("Step")))
		    {
    	    InsertNode.deleteNode("Step=");
		    }
    	    InsertNode.insertNode("Freq","Step="+stepField.getText());
    	    stepSel=new String();
    	    stepSel=stepField.getText();
    	    String sa = new String();
			sa = "Step="+stepField.getText();
	     	freqOpt.addElement(sa);
    	    //freqOptCount++;
    	}
   		if(ch==1) // pressed no remove step
    	{
   		if((InsertNode.nodeExists("Step")))
    		{
    		   //Delete Node if exists
    		   InsertNode.deleteNode("Step");
    		 }
    				
    	}

		}
    	else
		{
    	    if((InsertNode.nodeExists("Step="+stepSel)))
		    InsertNode.deleteNode("Step");
    	   
    	    freqOpt.removeElement("Step="+stepSel);
		}
    }
	
	
	}
public static void addListeners()
{
	FRaman.addItemListener(new FreqOptTable());
	FReadf.addItemListener(new FreqOptTable());FHpmod.addItemListener(new FreqOptTable());
    FAnaly.addItemListener(new FreqOptTable());
	FProje.addItemListener(new FreqOptTable());FNrama.addItemListener(new FreqOptTable());FReada.addItemListener(new FreqOptTable());
	FIntmo.addItemListener(new FreqOptTable());
	FNumer.addItemListener(new FreqOptTable());FHinde.addItemListener(new FreqOptTable());FNnram.addItemListener(new FreqOptTable());
	FModre.addItemListener(new FreqOptTable());FEnonl.addItemListener(new FreqOptTable());FNoram.addItemListener(new FreqOptTable());
	FReadi.addItemListener(new FreqOptTable());FCubic.addItemListener(new FreqOptTable());FVcd.addItemListener(new FreqOptTable());
	FResta.addItemListener(new FreqOptTable());FStep.addItemListener(new FreqOptTable());FVibro.addItemListener(new FreqOptTable());
	FAnhar.addItemListener(new FreqOptTable());
//  
}
  public static void main(String[] args) {
    FreqOptTable frame = new FreqOptTable();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
  }

}
