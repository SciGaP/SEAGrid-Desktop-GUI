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
 * @author Shashank Jeedigunta @author Sandeep Kumar Seethaapathy  
 * 
 */

package G03Input;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.lang.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.border.*;
import java.awt.Font;
import java.awt.Color;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.*;
import java.util.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import java.io.*;

import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

public class G03MenuTree extends JFrame implements MouseListener
{
    //Redesign Components
	JPanel routePanel,molPanel;
	public static JTextField molCharge,molMultiplicity;
	public static JButton viewMolStructure;
	JSplitPane verticalSplit,horizonSplit;
	public static JPanel molBrowser=new JPanel();
	JPanel linkoPanel,linkDiv1,linkDiv2,jobNamePanel,otherItemsPanel,helpDonePanel,helpSection;
	JPanel centerBasePanel,routeTitleMolPanel;
	static int screenWidth,screenHeight;
	public static HTMLEditorKit htmlKit;
	public static HTMLDocument htmlDoc;
    public static JMenuBar mainMenubar;
    public static JPanel bottomPanel,basePanel,treePanel;
    public static JScrollPane treeScroll;
    public static JMenu methodMenu,basisSetMenu,freqSubMenu,addSubMenu,molMenu;
    public static JMenuItem nanoItem,cartItem,zItem;
    public static String freOptString="",optString="";
    public static String[] key_words= {"Methods","Basis Sets","Job Types","Keywords"};
    public static JRadioButton pRadio,nRadio,tRadio,noneRadio;
    public static ButtonGroup routeChoice;
    
    // Frequently used Menu's
    public static JMenu fsto3gMenu,f621gMenu,f431gMenu,fccpvdMenu,fccpv5Menu,f321Menu;
    
    //Additional Basis Set Menu's
    public static JMenu acep4Menu,acep31Menu,acep121Menu,ashcMenu,augbsMenu,ad95vMenu;
    
    public static JMenuItem f631gItem,f6311gItem,flanl2Item;
    public static Color bgColor,foreColor;
    public static JMenuItem description,availability,restrictions;
    public static JMenuItem mapItem,descItem,availItem,restItem,keyOptCombination;
    
    
    public static JMenuItem fsto3gItem,fsto3g1Item,f621gItem,f621gdItem;
    public static JMenuItem f321gItem,f321g1Item,f321pgItem,f321pg1Item,f321g11Item,f321pg11Item;
    public static JMenuItem f431gItem,f431g1Item,f431g2Item;
    public static JMenuItem fccpvdItem,fccpvtItem,fccpvqItem,faugccdItem,faugcctItem,faugccqItem;
    public static JMenuItem fccpv5Item,fccpv6Item,faugcc5Item,faugcc6Item;
    
    //Additional Basis Set Menu Items
    public static JMenuItem acbs631gdaggerItem,acbs631gdaggerdaggerItem,ad95Item,adgItem,adg2Item,adgtItem;
    public static JMenuItem aeprItem,aepr2Item,alanItem,amidiItem,amtItem,asddItem,asdd2Item;
    //  Additional Basis Set 2-Level Menu Items
    public static JMenuItem acep4gItem,acep4g1Item,acep31gItem,acep31g1Item,acep121gItem,acep121g1Item;
    public static JMenuItem ad95vpItem,ad95vppItem,ad95v1Item,ad95v11Item,ad95vp1Item,ad95vp11Item,ad95vpp11Item,ad95vpp1Item;
    public static JMenuItem ashcItem,ashc1Item,augbsItem,augbs1Item,augbs2Item,augbs3Item;
    
    public static JFrame mainFrame;
	public static JComboBox jobTypeCombo,jobTypeOptionsCat,jobTypeOptions,addnKeyword,addnKeyOptions,addnKeyOptionsCat;
	
	public static JTextField jobEnergyNumber,keyEnergyNumber;
	public static JLabel jobEnergyLabel,keyEnergyLabel;
	public static JButton doneButton,clearButton,exitButton,saveButton;
	public static JLabel jobTypeLabel,addnKeyLabel,jobOptCatLabel,addnKeyOptCatLabel,jobOptLabel,addnKeyOptLabel;
	
	public static JMenu commonJobMenu,otherJobMenu,jobTypeMenu,keyWordMenu;
	public static JMenuItem keyGeom;
    public static JMenuItem jcDensity,jcFreq,jcGuess,jcOpt,jcOptFreq,jcSp;
    public static JMenuItem jlAdmp,jlBomp,jlForce,jlIrc,jlIrcMax,jlOptIrcMax,jlPolar,jlOptPolar,jlArchive,jlScan,jlStable,jlVolume;
    public static JPanel keyWordPanel,buttonPanel,comboPanel,titlePanel,noPanel,inputPanel;
	
	public static JTextArea routeArea,jobArea;
	public static JTextArea filnamArea;
	public static JTextField noofspText,nooflpText,dynmemText;
	public static JLabel routeLabel,jobLabel,filnamLabel,noofspLabel,nooflpLabel,dynmemLabel;
	public static JCheckBox dontsave;
	public static JButton importFile,editFile,copyFile;
	
	//public static JTextPane keyoptArea;
	public static JEditorPane keyoptArea;
	public static Statement typeStmt;
	public static ResultSet rs1;
	 
	//Method Menus
	public static JMenu mdftMenu,mmpnMenu,mccMenu,mciMenu,motherMenu;
	public static JMenuItem rhfItem,uhfItem,rohfItem,mhfItem,mcasscfmItem;
	public static JMenuItem mp2Item,mp3Item,mp4Item,mp4dItem,mp4sItem,mp5Item;
	public static JMenuItem ccdItem,ccsItem,ccsdItem,mcasscfItem;
	public static JMenuItem cidItem,cisdcItem,cisItem,cisdItem,sacItem;
	public static JMenuItem tdItem,tddItem,bdItem,gvbItem,ovgfItem,w1Item,zinItem;
	public static JMenu semiMenu,cbsMenu,gnMenu;
	public static JMenuItem am1Item,pm3Item,pm3mmItem,cndoItem,indoItem,mindoItem,mndoItem;
	public static JMenuItem cbs4Item,cbslItem,cbsqItem,cbsqbItem,cbsaItem;
	public static JMenuItem g1Item,g2Item,g2mp2Item,g3Item,g3mp2Item,g3b3Item,g3mp2b3Item;
	
	
	// DFT Methods
	public static JMenu dcHybridFunc,doHybridFunc,dstdAloneFunc,duserDefinedFunc;
	public static JMenuItem dListAllFunc;
	public static JMenuItem comB3ly,comB3pw,comB3p8,comB1ly,comPbe1,comMpw1;
	public static JMenuItem othB1b9,othB98,othB971,othB972,othBhan,othBhlyp,othLsda;
	public static JMenuItem stdVsxc,stdHcth,stdHct9,stdHct14,stdHct40;
	
	
	//Code for the additional keywords to be added 
	//06/14/05    Modified @ UKy
	
	public static JMenu freqKeyMenu,mmMenu,genMenu,genecpMenu;
	public static JMenuItem otherKeyMenuItem,optKeyAmb,optKeyArc,optKeyCha,optKeyChk,optKeyCom,optKeyCon,optKeyCou,optKeyCph,optKeyDen,optKeyExt,optKeyExtB,optKeyExtD,optKeyFie;
	public static JMenuItem mmAmbItem,optKeyFmm,optKeyGfi,optKeygfp,optKeyInt,optKeyIop,optKeyNam,optKeyOut,optKeyPre,optKeyPro,optKeyPse,optKeyPun,optKeySca,optKeySpa,optKeySym,optKeyTem,optKeyTesM,optKeyTra,optKeyTras,optKeyUni;
	
	
	
	//Menu Items for Density
	
	public static JMenuItem dcurrent,dAll,dscf,dmp2,dci,dqci,dchkpoint,dallt,dcis,dtran;
	
	//Keyword Menu
	public static JMenuItem pbcItem,popItem;
	
	
	//TREE COMPONENTS
	
	  public static DefaultListModel model=new DefaultListModel();
	  //public static String[] key_words= {"Methods","Basis Sets","Job Types","Molecular Structure"};
	  public static String[][] options = new String[5][5];
	  public static JTree tree;
	  public static  DefaultMutableTreeNode root,child,grandChild;
	  //public static TreeSelectionModel treeModel;
	  public static DefaultTreeModel treeModel;

	  public static Hashtable hashTable;
	  public static DefaultTreeModel tm; 
	  public  JPopupMenu  stPopupMenu;
	
	  // JLabel for displaying the nanocad status
	  public static JLabel nanocadNotice= new JLabel();
	  
	  
	  
	  
	public G03MenuTree()
	{
		
	super("Gaussian 03 Input GUI");    
	//showMolEditor.molFrame.setVisible(false);
	//createtitlePanel();
	createkeyWordPanel();
    createtreePanel();
  
    
    //Redesign Methods 

    createLinkoPanel();
    createRoutePanel();
    createJobtitlePanel();   
    createMolPanel();
    createOtherPanel();
    createhelpPanel();
    createSplitPanel();
    
    createbasePanel();
	
	
    
    
    
    Container contentPane = getContentPane();
	contentPane.setLayout(new BorderLayout());
	contentPane.add(basePanel,BorderLayout.CENTER);
	}
	

	//Modified at Uky 06/22/05
	// -->Redesigned Panels
	
//	Function for getting the linko parameters
	void createLinkoPanel()
	{
   	    noofspLabel=new JLabel("%nproc");
		nooflpLabel=new JLabel("%lproc");
		dynmemLabel=new JLabel("%mem(in MB)");
	    filnamLabel=new JLabel("CheckPoint File   ");
	    
		
	    noofspText=new JTextField(5);
	    nooflpText=new JTextField(5);
	    dynmemText=new JTextField(5);
	    filnamArea=new JTextArea();
		
		linkoPanel = new JPanel();
		TitledBorder linkTitle = new TitledBorder("LinkO Section");
		linkoPanel.setBorder(linkTitle);
		
		linkoPanel.setLayout(new BorderLayout());
		
		linkDiv1 = new JPanel();       //linkDiv1 contains the memory and processor requirements fields
		linkDiv1.setLayout(new FlowLayout());
		linkDiv1.add(noofspLabel);
		linkDiv1.add(noofspText);
		linkDiv1.add(nooflpLabel);
		linkDiv1.add(nooflpText);
		linkDiv1.add(dynmemLabel);
		linkDiv1.add(dynmemText);
	
		linkDiv2 = new JPanel();
		linkDiv2.setLayout(new BorderLayout());
		linkDiv2.add(filnamLabel,BorderLayout.WEST);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	//mainFrame.setSize(screenSize.width-20,screenSize.height-100);
    	screenWidth=screenSize.width-200;
    	screenHeight=screenSize.height-150;
		System.out.println(screenWidth+"Height\t"+screenHeight);
		
		filnamArea.setPreferredSize(new Dimension(screenWidth/2,screenHeight/20));
		filnamArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,1));
		linkDiv2.add(filnamArea,BorderLayout.CENTER);
		
		linkoPanel.add(linkDiv1,BorderLayout.NORTH);
		linkoPanel.add(linkDiv2,BorderLayout.SOUTH);
		
		}
//  Function for showing the Route Section 	
	void createRoutePanel()
	{
		routePanel=new JPanel(new BorderLayout());
		TitledBorder routeTitle = new TitledBorder("Route Section");
		routePanel.setBorder(routeTitle);
	
		JPanel optionPanel = new JPanel();//(new BorderLayout());
		
		nRadio=new JRadioButton("Use #N");
		pRadio=new JRadioButton("Use #P");
	    tRadio=new JRadioButton("Use #T");
	   
	    routeChoice = new ButtonGroup();
	    
	    routeChoice.add(nRadio);
	    routeChoice.add(pRadio);
	    routeChoice.add(tRadio);
	   
   	   nRadio.addActionListener(new RouteClass());
	   pRadio.addActionListener(new RouteClass());
	   tRadio.addActionListener(new RouteClass());
	   
	   //Default selection is #N
	   nRadio.setSelected(true);
	   	
	   //Add the choices to the optionPanel
	   optionPanel.add(nRadio);//,BorderLayout.EAST);
	   optionPanel.add(pRadio);//,BorderLayout.CENTER);
	   optionPanel.add(tRadio);//,BorderLayout.WEST);
	   
	   //Create Route Area 
	    routeArea= new JTextArea();
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	//mainFrame.setSize(screenSize.width-20,screenSize.height-100);
    	screenWidth=screenSize.width-200;
    	screenHeight=screenSize.height-150;
		System.out.println(screenWidth+"Height\t"+screenHeight);
		
	//	routeArea.setPreferredSize(new Dimension(screenWidth-40,screenHeight/8));
		routeArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,0));
	    routeArea.setText("#");
	   routeArea.setAutoscrolls(true);
	   //RouteArea cannot be Edited manually... 
	    routeArea.setEditable(false);
	   routeArea.setLineWrap(true);
	   routeArea.setRows(3);
	    
	    routeArea.setToolTipText("<html>Route Section appears here.<strong><u>The contents cannot be manually altered.</u></strong>" +
	    		"<br>Remove the child nodes from the Tree appearing on the Left side to delete a particular option</html>");
	    JScrollPane scrpane=new JScrollPane(routeArea);
	    scrpane.setAutoscrolls(true);
	    //scrpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    //scrpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    System.out.println("Scrollpane visible??");
	   // scrpane.setVisible(true);
	    
	    /*
	    //It is a dummy panel, it does nothing
	    JPanel dpanel=new JPanel();
    	JLabel lbl= new JLabel("Route Section cannot be modified");
    	Color c = dpanel.getBackground();
    	lbl.setForeground(c);
    	dpanel.add(lbl);
	    */
		
	    routePanel.add(optionPanel,BorderLayout.NORTH);
		routePanel.add(scrpane,BorderLayout.CENTER);
		//routePanel.add(dpanel,BorderLayout.SOUTH);
	}
	
//	Function that takes care of the Job title 
	void createJobtitlePanel()
	{

	    jobArea=new JTextArea();
	  	jobArea.setText("default_job");
	    jobArea.setToolTipText("Enter the name of the Job");
	    jobNamePanel = new JPanel(new BorderLayout());
	    TitledBorder jobPanelTitle = new TitledBorder("Job Name");
	    jobNamePanel.setBorder(jobPanelTitle);
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	//mainFrame.setSize(screenSize.width-20,screenSize.height-100);
    	screenWidth=screenSize.width-200;
    	screenHeight=screenSize.height-150;
    	
    	//dpanel is a dummy panel, it does nothing
    	/*
    	JPanel dpanel=new JPanel();
    	JLabel lbl= new JLabel("Job Title");
    	Color c = dpanel.getBackground();
    	lbl.setForeground(c);
    	dpanel.add(lbl);
    	
    	dpanel.setMinimumSize(new Dimension(screenWidth-80,screenHeight/12));
    	dpanel.setEnabled(false);
    	//dpanel.setVisible(false);
    	*/
		System.out.println(screenWidth+"Height\t"+screenHeight);
		//jobArea.setPreferredSize(new Dimension(screenWidth-80,screenHeight/12));
		
		 jobArea.setAutoscrolls(true);
		  
		   jobArea.setLineWrap(true);
		   jobArea.setRows(2);
		
		jobArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,1));
		
		JScrollPane scrpane=new JScrollPane(jobArea);
		scrpane.setAutoscrolls(true);
		
		
		jobNamePanel.add(scrpane,BorderLayout.CENTER);
	//	jobNamePanel.add(dpanel,BorderLayout.SOUTH);
		
	}
	
//  Function for getting Charge and Multiplicity	
	void createMolPanel(){
		//This Panel holds the Charge and Multiplicity of the Molecular specification
		JLabel chargeLabel, multiplicityLabel;
		chargeLabel= new JLabel("Charge");
		multiplicityLabel=new JLabel("Multiplicity");
		molCharge=new JTextField(5);
		molCharge.setToolTipText("Enter the Charge here");
		molMultiplicity=new JTextField(5);
		molMultiplicity.setToolTipText("Enter the Multiplicity here");
		viewMolStructure = new JButton("View/Edit Structure");
		viewMolStructure.setToolTipText("Click to View the Current Molecular Structure");
		molPanel=new JPanel(new BorderLayout());
		JPanel mainMolPanel= new JPanel(); 
		JPanel nanocadLabelPanel = new JPanel();
		
		TitledBorder molTitle = new TitledBorder("Molecular Specification");
		molPanel.setBorder(molTitle);
		mainMolPanel.add(chargeLabel);
		mainMolPanel.add(molCharge);
		mainMolPanel.add(multiplicityLabel);
		mainMolPanel.add(molMultiplicity);
		mainMolPanel.add(viewMolStructure);
		
		
		nanocadNotice.setText("Molecular Specification not selected");
		nanocadLabelPanel.add(nanocadNotice);
		
		molPanel.add(nanocadLabelPanel,BorderLayout.SOUTH);
		molPanel.add(mainMolPanel,BorderLayout.CENTER);
		
	    viewMolStructure.addActionListener(new G03Listener());
	}

	void createOtherPanel()
	{
		importFile=new JButton("Fetch Input");
	    editFile=new JButton("Edit File");
	    importFile.addActionListener(new InputFile());
	    editFile.addActionListener(new InputFile());
	    otherItemsPanel = new JPanel();
	    TitledBorder otherOptions = new TitledBorder("Input File Options");
	    otherItemsPanel.setBorder(otherOptions);
	    otherItemsPanel.add(importFile);
	    otherItemsPanel.add(editFile);
	    
	}
	
	void createhelpPanel()
	{
		helpDonePanel = new JPanel();
		TitledBorder help = new TitledBorder("Help Section");
		helpDonePanel.setBorder(help);
		
		doneButton = new JButton("Done & Export");
	    doneButton.addActionListener(new G03Listener());
	    saveButton = new JButton("Save as File");
	    saveButton.addActionListener(new G03Listener());
	    clearButton = new JButton("CLEAR");
	    clearButton.setEnabled(false);
	    clearButton.addActionListener(new G03Listener());
	    exitButton = new JButton("EXIT");
	    exitButton.addActionListener(new G03Listener());	  
	    
		helpDonePanel.setLayout(new BorderLayout());
		
		helpSection = new JPanel();
		//keyoptArea=new JTextPane();
		keyoptArea=new JEditorPane();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	//mainFrame.setSize(screenSize.width-20,screenSize.height-100);
    	screenWidth=screenSize.width-200;
    	screenHeight=screenSize.height-150;
		System.out.println(screenWidth+"Height\t"+screenHeight);
		
		keyoptArea.setPreferredSize(new Dimension(screenWidth-40,screenHeight/12));
		keyoptArea.setBackground(Color.WHITE);
		Font helpFont = new Font("Garamond", Font.BOLD, 14);
		keyoptArea.setFont(helpFont);
		htmlKit=new HTMLEditorKit();
		keyoptArea.setEditorKit(htmlKit);
		htmlDoc = new HTMLDocument();
		keyoptArea.setDocument(htmlDoc);
		//keyoptArea.setStyledDocument(htmlDoc);
		//StyledDocument doc = keyoptArea.getStyledDocument();
		//addStylesToDocument(doc);
		
		//Added June 28'05 
		//Content type is set to display html data so that superscripts can be accommodated.
		keyoptArea.setContentType("text/html");
		try {
			//insertHTML(keyoptArea,"<html>Use <b>Shift + Left </b>Click to modify the Tree that displays the Input selections.</html>",0);
			insertHTML(keyoptArea,"Use <b>Shift + Left </b>Click to modify the Tree that displays the Input selections.");
			insertHTML(keyoptArea,otherKeyToolTip.molecularTip);
			insertHTML(keyoptArea,otherKeyToolTip.molTip);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//keyoptArea.setText("Use <b>Shift + Left </b>Click to modify the Tree that displays the Input selections.");
		keyoptArea.setToolTipText("Help notes for Input File Selection appears here");
		
		keyoptArea.setEditable(false);
	    JScrollPane scrollPane;
	    scrollPane = new JScrollPane(keyoptArea);
	    	
	    buttonPanel = new JPanel();
	    buttonPanel.add(doneButton);
	    buttonPanel.add(saveButton);
	    buttonPanel.add(clearButton);
	    buttonPanel.add(exitButton);
   
	    helpSection.add(scrollPane);
	    keyoptArea.setAutoscrolls(true);
	    
	    helpDonePanel.add(helpSection,BorderLayout.CENTER);
	    helpDonePanel.add(buttonPanel,BorderLayout.SOUTH);
	    
	    
	    
	    
	}
	
//	Function for Showing the Molecule and Tree together	
	void createSplitPanel(){
		//molBrowser=new JPanel();
		molBrowser.setMinimumSize(new Dimension(250,250));
		System.out.println(atomCoordinateParser.atomIndex);
		//if(atomCoordinateParser.atomIndex!=0){
			//new DisplayMolecule();
			//JComponent(DisplayMolecule.canvas);
			//molBrowser.add(DisplayMolecule.container);
		//}
		
		verticalSplit= new JSplitPane(JSplitPane.VERTICAL_SPLIT,treeScroll,molBrowser);
		verticalSplit.setContinuousLayout(true);
		verticalSplit.setOneTouchExpandable(true);
		
	}
	

	public void createtreePanel()
	{
	    root =
	        new DefaultMutableTreeNode("G '03 Input Selections");
	          
	      for(int i=0;i<key_words.length;i++)
	      {
	      	    child = new DefaultMutableTreeNode(key_words[i]);
	      	    root.add(child);
	      	 /* for(int j=0;j<5;j++)
	      	    {
	      		grandChild=new DefaultMutableTreeNode(options[i][j]);
	      		child.add(grandChild);
	      	  }
	      	  */
	      }
	         
	      tree = new JTree(root);
	      tree.setEditable(false);
	     // treeModel=tree.getSelectionModel();
	      tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
          treeScroll=new JScrollPane(tree);
          tree.addMouseListener(this);
          treeModel = new DefaultTreeModel(root);
       
          //  tree.addTreeSelectionListener(new G03TreeListener());
       //   treeModel.addTreeModelListener(new G03TreeListener());
          //tree.addTreeSelectionListener(new G03TreeListener());
	}
	
	public void createbasePanel()
	{
	    /*basePanel=new JPanel(new BorderLayout());
	    
	    basePanel.add(treeScroll,BorderLayout.WEST);
	    basePanel.add(keyWordPanel,BorderLayout.CENTER);
	    //NEWLY ADDED
	    basePanel.add(bottomPanel,BorderLayout.SOUTH);
	    basePanel.add(mainMenubar,BorderLayout.NORTH); 
	    //END OF NEWLY ADDED
	    */
		
		JPanel routeJobPanel= new JPanel(new BorderLayout());
		
		routeJobPanel.add(routePanel,BorderLayout.CENTER);
		routeJobPanel.add(jobNamePanel,BorderLayout.SOUTH);
		
		
		routeTitleMolPanel=new JPanel(new BorderLayout());
		
		//routeTitleMolPanel.add(jobNamePanel,BorderLayout.CENTER);
		routeTitleMolPanel.add(molPanel,BorderLayout.CENTER);
		routeTitleMolPanel.add(routeJobPanel,BorderLayout.NORTH);
		
		centerBasePanel=new JPanel(new BorderLayout());
		centerBasePanel.add(linkoPanel,BorderLayout.NORTH);
		centerBasePanel.add(otherItemsPanel,BorderLayout.SOUTH);
		centerBasePanel.add(routeTitleMolPanel,BorderLayout.CENTER);
		
		horizonSplit= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,verticalSplit,centerBasePanel);
		horizonSplit.setContinuousLayout(true);
		horizonSplit.setOneTouchExpandable(true);
		
		basePanel= new JPanel(new BorderLayout());
		basePanel.add(mainMenubar,BorderLayout.NORTH);
		//basePanel.add(verticalSplit,BorderLayout.WEST);
		basePanel.add(helpDonePanel,BorderLayout.SOUTH);
		//basePanel.add(centerBasePanel,BorderLayout.CENTER);
		basePanel.add(horizonSplit,BorderLayout.CENTER);
	}
	
	
/*	
	public static void createtitlePanel()
	{
	      
	    
	    
	    
	    bgColor= new Color(236,233,216);
	    foreColor =new Color(0,78,152);
	    
	    
	    
	    bottomPanel=new JPanel(new BorderLayout());
	    titlePanel=new JPanel(new GridBagLayout());
	  	    
	    pRadio=new JRadioButton("Use #P");
	    nRadio=new JRadioButton("Use #N");
	    tRadio=new JRadioButton("Use #T");
	   
	    routeChoice = new ButtonGroup();
	    
	    routeChoice.add(nRadio);
	    routeChoice.add(pRadio);
	    routeChoice.add(tRadio);
	   
	   nRadio.addActionListener(new RouteClass());
	   pRadio.addActionListener(new RouteClass());
	   tRadio.addActionListener(new RouteClass());
	   
	   GridBagConstraints c = new GridBagConstraints();
	   c.fill = GridBagConstraints.BOTH;
	    
	  	
	  	routeArea= new JTextArea();
	    
	    routeArea.setEditable(false);
	    routeArea.setLineWrap(true);
	    JScrollPane scrpane=new JScrollPane(routeArea);
	   
	    //routeArea.setAutoscrolls(true);
	    
	    routeLabel =new JLabel("Route Section");
	    
	    
	   
	    
	    c.gridx=0;
	    c.gridy=0;
	    c.weightx=0.5;
	    c.insets=new Insets(40,60,40,0);
	    titlePanel.add(routeLabel,c);
	    
	    c.gridx=1;
	    c.gridwidth=4;
	    //c.insets=new Insets(20,-200,20,10);
	    //c.insets=new Insets(20,-60,10,10);
	    c.insets=new Insets(20,-53,10,10);
	    titlePanel.add(scrpane,c);
	    
	    c.gridwidth=1;
	    c.gridy=2;
	    c.ipadx=-10;
	    c.insets=new Insets(-10,50,10,0);
	    c.gridx=3;
	    titlePanel.add(pRadio,c);
	    c.gridx=2;
	    c.insets=new Insets(-10,0,10,0);
	    titlePanel.add(nRadio,c);
	    c.gridx=4;
	    c.insets=new Insets(-10,0,10,0);
	    titlePanel.add(tRadio,c);
	    /*c.gridx=4;
	    c.insets=new Insets(-10,0,10,250);
	    titlePanel.add(noneRadio,c);
	    
	    nRadio.setSelected(true);
	    doneButton = new JButton("DONE");
	    doneButton.addActionListener(new G03Listener());
	    clearButton = new JButton("CLEAR");
	    clearButton.addActionListener(new G03Listener());
	    exitButton = new JButton("EXIT");
	    exitButton.addActionListener(new G03Listener());	  
	    
	    ImageIcon exitIcon = new ImageIcon("exit.png");
	    exitButton.setIcon(exitIcon);
	    
	   ImageIcon doneIcon = new ImageIcon("done.png");
	    doneButton.setIcon(doneIcon);
	    
	    ImageIcon clearIcon = new ImageIcon("clear.png");
	    clearButton.setIcon(clearIcon);
	    
	    buttonPanel = new JPanel();
	  
	    buttonPanel.add(doneButton);
	    buttonPanel.add(clearButton);
	    buttonPanel.add(exitButton);
   
	    bottomPanel.add(titlePanel,BorderLayout.CENTER);
	    bottomPanel.add(buttonPanel,BorderLayout.SOUTH);
	
	}*/
	
	public static void createkeyWordPanel()
	{
	    /*bgColor= new Color(236,233,216);
	    foreColor =new Color(0,78,152);*/
	    keyWordPanel = new JPanel(new BorderLayout());
	    
	    
	    mainMenubar = new JMenuBar();
	    
	    
	    molMenu=new JMenu("Molecular Specification");
	    
	    //nanoItem=new JMenuItem("Molecular Editor");
	    //cartItem = new JMenuItem("Cartesian Coordinates");
	    //zItem= new JMenuItem("Z-matrix Format");
    	
	    //molMenu.add(cartItem);
	   // molMenu.add(nanoItem);
	    //molMenu.add(zItem);
	   // zItem.setEnabled(false);
	   // nanoItem.addActionListener(new showMolEditor());
	    //cartItem.addActionListener(new MenuListeners());
	   // zItem.addActionListener(new MenuListeners());
	    
	    
	    /* Key word Menus */
	    methodMenu = new JMenu("Methods");
	    //methodMenu.setBackground(Color.lightGray);
	    
	    
	    mainMenubar.add(molMenu);
	    mainMenubar.add(methodMenu);
	    
	    keyWordMenu = new JMenu("Keywords");
	    
	    //Modified by Uky 06/14/2005
	    freqKeyMenu = new JMenu("Common Keywords");
	    otherKeyMenuItem = new JMenuItem("Other KeyWords");
	    
	    
	    
	    
	    
	    
	    
	    keyGeom = new JMenuItem("Geom");
	    pbcItem = new JMenuItem("PBC");
	    popItem = new JMenuItem("Pop");
	    mmMenu = new JMenu("MM Methods");
	    mmMenu.setEnabled(false);
	    mmAmbItem= new JMenuItem("Amber");
	    mmAmbItem.setEnabled(false);
	    genMenu =new JMenu("Gen");
	    genMenu.setEnabled(false);
	    genecpMenu = new JMenu("Gen ECP");
	    genecpMenu.setEnabled(false);
//	  Modified by Uky 06/14/2005
	    keyWordMenu.add(freqKeyMenu);
	    keyWordMenu.add(otherKeyMenuItem);
		freqKeyMenu.add(keyGeom);
	    freqKeyMenu.add(pbcItem);
	    freqKeyMenu.add(popItem);
	    
	    mmAmbItem.addActionListener(new keywordListener());
	    pbcItem.addActionListener(new keywordListener());
	    keyGeom.addActionListener(new keywordListener());
	    popItem.addActionListener(new keywordListener());
	    otherKeyMenuItem.addActionListener(new keywordListener());
	    
	    basisSetMenu= new JMenu("Basis Sets");
	    //basisSetMenu.setBackground(Color.lightGray);
	    
	   //basisSetMenu.addItemListener(new G03InputListeners());
	    mainMenubar.add(basisSetMenu);
	
	    jobTypeMenu=new JMenu("Job Types");
	    
	    
	    mainMenubar.add(jobTypeMenu);

	    mainMenubar.add(keyWordMenu);
	    mainMenubar.add(mmMenu);
	    mainMenubar.add(genMenu);
	    mainMenubar.add(genecpMenu);
	    
	    commonJobMenu = new JMenu("Common-Used Types");
	    keyOptCombination = new JMenuItem("Popular Keyword/Option Combination");
	    keyOptCombination.setEnabled(false);
	    //keyOptCombination.addActionListener(new popKeyOptTable());
	    otherJobMenu = new JMenu("Other Types");
	    
	    jcDensity = new JMenu("Density");
	    jcDensity.addActionListener(new MenuListeners());
	    jcFreq = new JMenuItem("Freq");
	    jcFreq.addActionListener(new MenuListeners());
	    jcGuess = new JMenuItem("Guess");
	    jcGuess.addActionListener(new MenuListeners());
	    jcOpt = new JMenuItem("Opt  ");
	    jcOpt.addActionListener(new MenuListeners());
	    jcOptFreq = new JMenuItem("OptFreq");
	    jcOptFreq.addActionListener(new MenuListeners());
	    jcSp = new JMenuItem("SP");
	    jcSp.addActionListener(new MenuListeners());
	    
	    
	    
	    dcurrent=new JMenuItem("Current(default)");
	    dAll=new JMenuItem("All");
	    dscf=new JMenuItem("SCF");
	    dmp2=new JMenuItem("MP2");
	    dci=new JMenuItem("CI");
	    dqci=new JMenuItem("QCI");
	    dchkpoint=new JMenuItem("Checkpoint");
	    dallt=new JMenuItem("AllTransistion");
	    dcis=new JMenuItem("CIS=N");
	    dtran=new JMenuItem("Transition=(N,[M])");
	   
	    
	    jcDensity.add(dcurrent);
	    jcDensity.add(dAll);
	    jcDensity.add(dscf);
	    jcDensity.add(dmp2);
	    jcDensity.add(dci);
	    jcDensity.add(dqci);
	    jcDensity.add(dchkpoint);
	    jcDensity.add(dallt);
	    jcDensity.add(new JSeparator());
	    jcDensity.add(dcis);
	    jcDensity.add(dtran);
	    
	    dcurrent.addActionListener(new MenuListeners());
	    dAll.addActionListener(new MenuListeners());
	    dscf.addActionListener(new MenuListeners());
	    dmp2.addActionListener(new MenuListeners());
	    dci.addActionListener(new MenuListeners());
	    dqci.addActionListener(new MenuListeners());
	    dchkpoint.addActionListener(new MenuListeners());
	    dallt.addActionListener(new MenuListeners());
	    dcis.addActionListener(new MenuListeners());
	    dtran.addActionListener(new MenuListeners());
	    
	    
	    jlAdmp = new JMenuItem("ADMP");
	    jlBomp = new JMenuItem("BOMP");
	    jlForce = new JMenuItem("Force");
	    jlIrc = new JMenuItem("IRC");
	    jlIrcMax = new JMenuItem("IRC-Max");
	    jlOptIrcMax = new JMenuItem("Opt_IRC-Max");
	    jlPolar = new JMenuItem("Polar");
	    jlOptPolar = new JMenuItem("Opt_Polar");
	    jlArchive = new JMenuItem("ReArchive");
	    jlScan = new JMenuItem("Scan");
	    jlStable = new JMenuItem("Stable");
	    jlVolume = new JMenuItem("Volume");
	    
	    jlAdmp.addActionListener(new MenuListeners());
	    jlBomp.addActionListener(new MenuListeners());
	    jlForce.addActionListener(new MenuListeners());
	    jlIrc.addActionListener(new MenuListeners());
	    jlIrcMax.addActionListener(new MenuListeners());
	    jlOptIrcMax.addActionListener(new MenuListeners());
	    jlPolar.addActionListener(new MenuListeners());
	    jlOptPolar.addActionListener(new MenuListeners());
	    jlArchive.addActionListener(new MenuListeners());
	    jlScan.addActionListener(new MenuListeners());
	    jlStable.addActionListener(new MenuListeners());
	    jlVolume.addActionListener(new MenuListeners());
	    
	    
	    commonJobMenu.add(jcDensity);
	    commonJobMenu.add(jcFreq);
	    commonJobMenu.add(jcGuess);
	    commonJobMenu.add(jcOpt);
	    commonJobMenu.add(jcOptFreq);
	    commonJobMenu.add(jcSp);
	    otherJobMenu.add(jlAdmp);
	    otherJobMenu.add(jlBomp);
	    otherJobMenu.add(jlForce);
	    otherJobMenu.add(jlIrc);
	    otherJobMenu.add(jlIrcMax);
	    otherJobMenu.add(jlOptIrcMax);
	    otherJobMenu.add(jlPolar);
	    otherJobMenu.add(jlOptPolar);
	    otherJobMenu.add(jlArchive);
	    otherJobMenu.add(jlScan);
	    otherJobMenu.add(jlStable);
	    otherJobMenu.add(jlVolume);
	     jobTypeMenu.add(commonJobMenu);
	     
	     jobTypeMenu.add(otherJobMenu);	    
	     jobTypeMenu.add(keyOptCombination);
	    freqSubMenu= new JMenu("Frequently-used basis sets");
	    addSubMenu= new JMenu("Additional basis sets");
	
	    
	    
	    
	    
	    
	    
	    
	    // first level Freq used
	    fsto3gMenu = new JMenu("STO-3G");
	    f321Menu = new JMenu("3-21G");
         	   
	    //f321Menu.addActionListener(new BasisSetListeners());
	     
	    f621gMenu= new JMenu("6-21G");
	    f431gMenu= new JMenu("4-31G");
	    f631gItem=new JMenuItem("6-31G");
	    
	    f631gItem.addActionListener(new BasisSetListeners());
	    f6311gItem=new JMenuItem("6-311G");
	    f6311gItem.addActionListener(new BasisSetListeners());
	    flanl2Item=new JMenuItem("LanL2DZ");
	    flanl2Item.addActionListener(new BasisSetListeners());
	    fccpvdMenu=new JMenu("cc-pv(D,T,Q)Z");
	    
	    fccpv5Menu=new JMenu("cc-pv(5,6)Z");

	    
	    //secondlevel freq used
	    
	    fsto3gItem= new JMenuItem("STO-3G");
	    fsto3g1Item= new JMenuItem("STO-3G*");
	    
	    f321gItem=new JMenuItem("3-21G");
	    f321g1Item=new JMenuItem("3-21G*");
	    f321g11Item=new JMenuItem("3-21G**");
	    f321pgItem=new JMenuItem("3-21+G");
	    f321pg1Item=new JMenuItem("3-21+G*");
	    f321pg11Item=new JMenuItem("3-21+G**");
	    
	    
	    f621gItem=new JMenuItem("6-21G");
	    f621gdItem=new JMenuItem("6-21G(d)");
	    
	    f431gItem=new JMenuItem("4-31G");
	    f431g1Item=new JMenuItem("4-31G*");
	    f431g2Item=new JMenuItem("4-31G**");
	    
	    fccpvdItem=new JMenuItem("cc-pVDZ");
	    fccpvtItem=new JMenuItem("cc-pVTZ");
	    fccpvqItem=new JMenuItem("cc-pVQZ");
	    faugccdItem=new JMenuItem("AUG-cc-pVDZ");
	    faugcctItem=new JMenuItem("AUG-cc-pVTZ");
	    faugccqItem=new JMenuItem("AUG-cc-pVQZ");
	    
	    fccpv5Item=new JMenuItem("cc-pV5Z");
	    fccpv6Item=new JMenuItem("cc-pV6Z");
	    faugcc5Item=new JMenuItem("AUG-cc-pV5Z");
	    faugcc6Item=new JMenuItem("AUG-cc-pV6Z");
	    
	    //Add action listeners for the freq. used Basis Sets Level1 
	    fsto3gItem.addActionListener(new BasisSetListeners());
	    fsto3g1Item.addActionListener(new BasisSetListeners());
	    
	    f321gItem.addActionListener(new BasisSetListeners());
	    f321g1Item.addActionListener(new BasisSetListeners());
	    f321g11Item.addActionListener(new BasisSetListeners());
	    f321pgItem.addActionListener(new BasisSetListeners());
	    f321pg1Item.addActionListener(new BasisSetListeners());
	    f321pg11Item.addActionListener(new BasisSetListeners());
	    
	    f621gItem.addActionListener(new BasisSetListeners());
	    f621gdItem.addActionListener(new BasisSetListeners());
	    
	    f431gItem.addActionListener(new BasisSetListeners());
	    f431g1Item.addActionListener(new BasisSetListeners());
	    f431g2Item.addActionListener(new BasisSetListeners());
	    
	    fccpvdItem.addActionListener(new BasisSetListeners());
	    fccpvtItem.addActionListener(new BasisSetListeners());
	    fccpvqItem.addActionListener(new BasisSetListeners());
	    faugccdItem.addActionListener(new BasisSetListeners());
	    faugcctItem.addActionListener(new BasisSetListeners());
	    faugccqItem.addActionListener(new BasisSetListeners());
	    fccpvdItem.addActionListener(new BasisSetListeners());
	    fccpv5Item.addActionListener(new BasisSetListeners());
	    fccpv6Item.addActionListener(new BasisSetListeners());
	    faugcc5Item.addActionListener(new BasisSetListeners());
	    faugcc6Item.addActionListener(new BasisSetListeners());
	    
	    
	    //first level additional basis sets

	    // Petersson et al. Complete Basis Set method basis sets.
	    // These can have diffuse and polarization functions.
	    // There appears to be no general method for handling diffuse and
	    // polarization functions; so these are ignored for now.  S. Brozell Nov 2006
	    acbs631gdaggerItem=new JMenuItem("CBS 6-31G\u2020");
	    acbs631gdaggerdaggerItem=new JMenuItem("CBS 6-31G\u2020\u2020");

	    acep4Menu=new JMenu("CEP-4G"); 
	    acep31Menu=new JMenu("CEP-31G");
	    acep121Menu=new JMenu("CEP-121G");
	    ad95Item=new JMenuItem("D95");
	    
	    ad95vMenu=new JMenu("D95V");
	    
	    
	    adgItem=new JMenuItem("DGDZVP");
	    adg2Item=new JMenuItem("DGDZVP2");
	    adgtItem=new JMenuItem("DGTZVP");
	    aeprItem=new JMenuItem("EPR-II");
	    aepr2Item=new JMenuItem("EPR-III");
	    alanItem=new JMenuItem("LanL2MB");
	    amidiItem=new JMenuItem("MidiX");
	    amtItem=new JMenuItem("MTSmall");
	    
	    asddItem=new JMenuItem("SDD");
	    asdd2Item=new JMenuItem("SDDAll");
	    ashcMenu=new JMenu("SHC(SEC)");
	    augbsMenu=new JMenu("UGBS");
	    
	    //secondlevel additional basis set
	    
	    acep4gItem=new JMenuItem("CEP-4G");
	    acep4g1Item=new JMenuItem("CEP-4G*(Li-Ar only)");
	    acep31gItem=new JMenuItem("CEP-31G");
	    acep31g1Item=new JMenuItem("CEP-31G*(Li-Ar only)");
	    acep121gItem=new JMenuItem("CEP-121G");
	    acep121g1Item=new JMenuItem("CEP-121G*(Li-Ar only)");
	    
	    ad95vpItem=new JMenuItem("D-95V+");
	    ad95vppItem=new JMenuItem("D-95V++");
	    ad95v1Item=new JMenuItem("D-95V*");
	    ad95v11Item=new JMenuItem("D-95V**");
	    ad95vp1Item=new JMenuItem("D-95V+*");
	    ad95vp11Item=new JMenuItem("D-95V+**");
	    ad95vpp11Item=new JMenuItem("D-95V++**");
	    ad95vpp1Item=new JMenuItem("D-95V++*");
	    
	    
	    ashcItem=new JMenuItem("SHC");
	    ashc1Item=new JMenuItem("SHC*");
	    augbsItem=new JMenuItem("UGBS");
	    augbs1Item=new JMenuItem("UGBS1P");
	    augbs2Item=new JMenuItem("UGBS2P");
	    augbs3Item=new JMenuItem("UGBS3P");
	    
        //	Add action listeners for the addn. Basis Sets Level1
	    acbs631gdaggerItem.addActionListener(new BasisSetListeners());
	    acbs631gdaggerdaggerItem.addActionListener(new BasisSetListeners());
	    ad95Item.addActionListener(new BasisSetListeners());
	    //ad95vMenu.addActionListener(new BasisSetListeners());
	    
	    adgItem.addActionListener(new BasisSetListeners());
	    adg2Item.addActionListener(new BasisSetListeners());
	    adgtItem.addActionListener(new BasisSetListeners());
	    aeprItem.addActionListener(new BasisSetListeners());
	    aepr2Item.addActionListener(new BasisSetListeners());
	    alanItem.addActionListener(new BasisSetListeners());
	    amidiItem.addActionListener(new BasisSetListeners());
	    amtItem.addActionListener(new BasisSetListeners());
	    asddItem.addActionListener(new BasisSetListeners());
	    asdd2Item.addActionListener(new BasisSetListeners());
	    
	    acep4gItem.addActionListener(new BasisSetListeners());
	    acep4g1Item.addActionListener(new BasisSetListeners());
	    acep31gItem.addActionListener(new BasisSetListeners());
	    acep31g1Item.addActionListener(new BasisSetListeners());
	    acep121gItem.addActionListener(new BasisSetListeners());
	    acep121g1Item.addActionListener(new BasisSetListeners());
	    
	    ad95vpItem.addActionListener(new BasisSetListeners());
	    ad95vppItem.addActionListener(new BasisSetListeners());
	    ad95v1Item.addActionListener(new BasisSetListeners());
	    ad95v11Item.addActionListener(new BasisSetListeners());
	    ad95vp1Item.addActionListener(new BasisSetListeners());
	    ad95vp11Item.addActionListener(new BasisSetListeners());
	    ad95vpp11Item.addActionListener(new BasisSetListeners());
	    ad95vpp1Item.addActionListener(new BasisSetListeners());
	    
	    
	    ashcItem.addActionListener(new BasisSetListeners());
	    ashc1Item.addActionListener(new BasisSetListeners());
	    augbsItem.addActionListener(new BasisSetListeners());
	    augbs1Item.addActionListener(new BasisSetListeners());
	    augbs2Item.addActionListener(new BasisSetListeners());
	    augbs3Item.addActionListener(new BasisSetListeners());
	    
	    
	    basisSetMenu.add(freqSubMenu);
	    basisSetMenu.add(addSubMenu);
	        
	    // adding menuitems to Level1 menus
	    fsto3gMenu.add(fsto3gItem);
	    fsto3gMenu.add(fsto3g1Item);
	    
	    f321Menu.add(f321gItem);
	    f321Menu.add(f321g1Item);
	    f321Menu.add(f321g11Item);
	    f321Menu.add(f321pgItem);
	    f321Menu.add(f321pg1Item);
	    f321Menu.add(f321pg11Item);
	    
	    
	    
	    f621gMenu.add(f621gItem);
	    f621gMenu.add(f621gdItem);
	    
	    f431gMenu.add(f431gItem);
	    f431gMenu.add(f431g1Item);
	    f431gMenu.add(f431g2Item);
	    
	    fccpvdMenu.add(fccpvdItem);
	    fccpvdMenu.add(fccpvtItem);
	    fccpvdMenu.add(fccpvqItem);
	    fccpvdMenu.add(faugccdItem);
	    fccpvdMenu.add(faugcctItem);
	    fccpvdMenu.add(faugccqItem);
	    
	    fccpv5Menu.add(fccpv5Item);
	    fccpv5Menu.add(fccpv6Item);
	    fccpv5Menu.add(faugcc5Item);
	    fccpv5Menu.add(faugcc6Item);
	    
	    //adding everything to submenu freq used
	    freqSubMenu.add(fsto3gMenu);
	    freqSubMenu.add(f321Menu);
	    freqSubMenu.add(f621gMenu);
	    freqSubMenu.add(f431gMenu);
	    freqSubMenu.add(f631gItem);
	    freqSubMenu.add(f6311gItem);
	    freqSubMenu.add(flanl2Item);
	    freqSubMenu.add(fccpvdMenu);
	    freqSubMenu.add(fccpv5Menu);
	    
	    // adding menuitems to Level1 menus
	    acep4Menu.add(acep4gItem);
	    acep4Menu.add(acep4g1Item);
	    acep31Menu.add(acep31gItem);
	    acep31Menu.add(acep31g1Item);
	    acep121Menu.add(acep121gItem);
	    acep121Menu.add(acep121g1Item);
	    
	    ad95vMenu.add(ad95vpItem);
	    ad95vMenu.add(ad95vppItem);
	    ad95vMenu.add(ad95v1Item);
	    ad95vMenu.add(ad95v11Item);
	    ad95vMenu.add(ad95vp1Item);
	    ad95vMenu.add(ad95vp11Item);
	    ad95vMenu.add(ad95vpp11Item);
	    ad95vMenu.add(ad95vpp1Item);
	    
	    
	    ashcMenu.add(ashcItem);
	    ashcMenu.add(ashc1Item);
	    augbsMenu.add(augbsItem);
	    augbsMenu.add(augbs1Item);
	    augbsMenu.add(augbs2Item);
	    augbsMenu.add(augbs3Item);
	    
	    
	    
	    
	    // adding every item to ADDN BASIS SET Sub menu
	    addSubMenu.add(acbs631gdaggerItem);
	    addSubMenu.add(acbs631gdaggerdaggerItem);
	    addSubMenu.add(acep4Menu);
	    addSubMenu.add(acep31Menu);
	    addSubMenu.add(acep121Menu);
	    addSubMenu.add(ad95Item);
	    addSubMenu.add(ad95vMenu);
	    addSubMenu.add(adgItem);
	    addSubMenu.add(adg2Item);
	    addSubMenu.add(adgtItem);
	    addSubMenu.add(aeprItem);
	    addSubMenu.add(aepr2Item);
	    addSubMenu.add(alanItem);
	    addSubMenu.add(amidiItem);
	    addSubMenu.add(amtItem);
	    addSubMenu.add(asddItem);
	    addSubMenu.add(asdd2Item);
	    addSubMenu.add(ashcMenu);
	    addSubMenu.add(augbsMenu);
	    //basisSetMenu.add(restItem);
	 //Method Menus
	    mhfItem=new JMenuItem("HF");
	    mcasscfmItem=new JMenuItem("CASSCF MP2");
	    mdftMenu=new JMenu("DFT");
	    mmpnMenu=new JMenu("MPn");
	    mccMenu=new JMenu("CC");
	    mciMenu=new JMenu("CI");
	    mcasscfItem=new JMenuItem("CASSCF");
	    motherMenu=new JMenu("Other");
	    
	    //Level 2
	        
	    
	    mp2Item=new JMenuItem("MP2");
	    mp3Item=new JMenuItem("MP3");
	    mp4Item=new JMenuItem("MP4(MP4(SDTQ))");
	    mp4dItem=new JMenuItem("MP4(DQ)");
	    
	    
	
	    
	    dcHybridFunc = new JMenu("Common Hybrid Functionals");
	    doHybridFunc = new JMenu("Other Hybrid Functionals");
	    dListAllFunc = new JMenuItem("List All Functionals");
	    dListAllFunc.addActionListener(new MenuListeners());
	    
	    
	    dstdAloneFunc = new JMenu("StandAlone Functionals");
	    duserDefinedFunc = new JMenu("User-defined DFT Models");
	    comB3ly = new JMenuItem("B3LYP");
	    comB3pw = new JMenuItem("B3PW91");
	    comB3p8 = new JMenuItem("B3P86");
	    comB1ly = new JMenuItem("B1LYP");
	    comPbe1 = new JMenuItem("PBE1PBE");
	    comMpw1 = new JMenuItem("MPW1PW91");
	    
	    
	    dcHybridFunc.add(comB3ly);
	    dcHybridFunc.add(comB3pw);
	    dcHybridFunc.add(comB3p8);
	    dcHybridFunc.add(comB1ly);
	    dcHybridFunc.add(comPbe1);
	    dcHybridFunc.add(comMpw1);
	    
	    mdftMenu.add(dcHybridFunc);
	    
	    othB1b9 = new JMenuItem("B1B95");
	    othB98 = new JMenuItem("B98");
	    othB971 =  new JMenuItem("B971");
	    othB972 = new JMenuItem("B972");
	    othBhan = new JMenuItem("BHandH");
	    othBhlyp = new JMenuItem("othBhlyp");
	    othLsda = new JMenuItem("LSDA");
	    
	    doHybridFunc.add(othB1b9);
	    doHybridFunc.add(othB98);
	    doHybridFunc.add(othB971);
	    doHybridFunc.add(othB972);
	    doHybridFunc.add(othBhan);
	    doHybridFunc.add(othBhlyp);
	    doHybridFunc.add(othLsda);

	    mdftMenu.add(doHybridFunc);
	    mdftMenu.add(dListAllFunc);
	    
	    stdVsxc = new JMenuItem("VSXC");
	    stdHcth = new JMenuItem("HCTH");
	    stdHct9 = new JMenuItem("HCTH93");
	    stdHct14 = new JMenuItem("HCTH147");
	    stdHct40 = new JMenuItem("HCTH407");
	    
	    dstdAloneFunc.add(stdVsxc);
	    dstdAloneFunc.add(stdHcth);
	    dstdAloneFunc.add(stdHct9);
	    dstdAloneFunc.add(stdHct14);
	    dstdAloneFunc.add(stdHct40);
	    
	    
	    mdftMenu.add(dstdAloneFunc);
	    //Leel2
	    mp4sItem=new JMenuItem("MP4(SDQ)");
	    mp5Item=new JMenuItem("MP5(UMP5)");
	    
	    ccdItem=new JMenuItem("CCD(CC,QCID)");
	    ccsItem=new JMenuItem("CCSD");
	    ccsdItem=new JMenuItem("CCSD(T)");
	    
	    cidItem=new JMenuItem("CID");
	    cisdcItem=new JMenuItem("CISD(CI,CIDS)");
	    cisItem=new JMenuItem("CIS");
	    cisdItem=new JMenuItem("CIS(D)");
	    sacItem=new JMenuItem("SAC-CI");
	    
	    semiMenu=new JMenu("Semi-empirical");
	    am1Item=new JMenuItem("AM1");
	    pm3Item=new JMenuItem("PM3");
	    pm3mmItem=new JMenuItem("PM3MM");
	    cndoItem=new JMenuItem("CNDO");
	    indoItem=new JMenuItem("INDO");
	    mindoItem=new JMenuItem("MINDO3");
	    mndoItem=new JMenuItem("MNDO");
	    
	    tdItem=new JMenuItem("TD(HF)");
	    tddItem=new JMenuItem("TD DFT");
	    
	    
	    bdItem=new JMenuItem("BD");
	    
	    cbsMenu=new JMenu("CBS");
	    cbs4Item=new JMenuItem("CBS-4M");
	    cbslItem=new JMenuItem("CBS-Lq");
	    cbsqItem=new JMenuItem("CBS-Q");
	    cbsqbItem=new JMenuItem("CBS-QB3");
	    cbsaItem=new JMenuItem("CBS-APNO");
	    
	    gnMenu=new JMenu("Gn");
	    g1Item=new JMenuItem("G1");
	    g2Item=new JMenuItem("G2");
	    g2mp2Item=new JMenuItem("G2MP2");
	    g3Item=new JMenuItem("G3");
	    g3mp2Item=new JMenuItem("G3MP2");
	    g3b3Item=new JMenuItem("G3B3");
	    g3mp2b3Item=new JMenuItem("G3MP2B3");
	    
	    
	    gvbItem=new JMenuItem("GVB");
	    ovgfItem=new JMenuItem("OVGF");
	    w1Item=new JMenuItem("W1");
	    zinItem=new JMenuItem("ZINDO");
	    
	    
	    semiMenu.add(am1Item);
	    semiMenu.add(pm3Item);
	    semiMenu.add(pm3mmItem);
	    semiMenu.add(cndoItem);
	    semiMenu.add(indoItem);
	    semiMenu.add(mindoItem);
	    semiMenu.add(mndoItem);
	    
	  
	    
	    mmpnMenu.add(mp2Item);
	    mmpnMenu.add(mp3Item);
	    mmpnMenu.add(mp4Item);
	    mmpnMenu.add(mp4dItem);
	    mmpnMenu.add(mp4sItem);
	    mmpnMenu.add(mp5Item);
	    
	    mccMenu.add(ccdItem);
	    mccMenu.add(ccsItem);
	    mccMenu.add(ccsdItem);
	    
	    mciMenu.add(cidItem);
	    mciMenu.add(cisdcItem);
	    mciMenu.add(cisItem);
	    mciMenu.add(cisdItem);
	    mciMenu.add(sacItem);
	    
	    motherMenu.add(semiMenu);
	    motherMenu.add(new JSeparator());
	    motherMenu.add(tdItem);
	    motherMenu.add(tddItem);
	    motherMenu.add(bdItem);
	    motherMenu.add(cbsMenu);
	    motherMenu.add(gnMenu);
	    motherMenu.add(gvbItem);
	    motherMenu.add(ovgfItem);
	    motherMenu.add(w1Item);
	    motherMenu.add(zinItem);
	  
	    
	    
	    cbsMenu.add(cbs4Item);
	    cbsMenu.add(cbslItem);
	    cbsMenu.add(cbsqItem);
	    cbsMenu.add(cbsqbItem);
	    cbsMenu.add(cbsaItem);
	    
	    gnMenu.add(g1Item);
	    gnMenu.add(g2Item);
	    gnMenu.add(g2mp2Item);
	    gnMenu.add(g3Item);
	    gnMenu.add(g3mp2Item);
	    gnMenu.add(g3b3Item);
	    gnMenu.add(g3mp2b3Item);
	    
	    
	    methodMenu.add(mhfItem);
	    methodMenu.add(mdftMenu);
	    methodMenu.add(mmpnMenu);
	    methodMenu.add(mccMenu);
	    methodMenu.add(mciMenu);
	    methodMenu.add(mcasscfItem);
	    methodMenu.add(mcasscfmItem);
	    methodMenu.add(new JSeparator());
	    methodMenu.add(motherMenu);
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    /* Combo panel */
	    	    /*comboPanel = new JPanel(new GridLayout(8,4));*/
	/*    
	    jobArea=new JTextArea();
	    jobArea.setText("default_job");
	    filnamArea=new JTextArea();
	    //filnamArea.setEditable(false);
	    filnamArea.addKeyListener(new G03Listener());
	    noofspText=new JTextField(5);
	    nooflpText=new JTextField(5);
	    dynmemText=new JTextField(5);
	    jobLabel=new JLabel("Job Title");
	    filnamLabel=new JLabel("Chk Filename");
	   dontsave=new JCheckBox("Save Checkpoint File");
	  //dontsave.setSelected(true);
	   dontsave.addActionListener(new G03Listener());
	   // noofspLabel=new JLabel("No. of shared Memory Processors requested");
	   // nooflpLabel=new JLabel("No. of Linda Processors requested");
	   // dynmemLabel=new JLabel("Dynamic Memory requested(in MB)");
	    
	    noofspLabel=new JLabel("%nproc");
		nooflpLabel=new JLabel("%lproc");
		dynmemLabel=new JLabel("%mem(in MB)");
	    
		comboPanel = new JPanel(new GridBagLayout());
	    
	    
	    GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.BOTH;
	    
	    
		    
	    
	    c.gridx=0;
		c.gridy=0;
		c.weightx=0.5;
		c.insets=new Insets(10,15,20,5);
		comboPanel.add(jobLabel,c);
		
		c.gridx=1;
	//	c.insets=new Insets(10,-50,0,-130);
		c.insets=new Insets(10,-50,0,-195);
		JScrollPane jobSpane=new JScrollPane(jobArea);
		jobArea.setLineWrap(true);
		comboPanel.add(jobSpane,c);
		
		c.gridy=2;
		c.gridx=0;
		c.insets=new Insets(10,15,20,5);
		comboPanel.add(filnamLabel,c);
		
		c.gridx=1;
		c.insets=new Insets(10,-50,0,-195);
		JScrollPane filSpane=new JScrollPane(filnamArea);
		filnamArea.setLineWrap(true);
		comboPanel.add(filSpane,c);
			
		c.gridy=4;
		c.insets=new Insets(10,-50,0,-15);
		comboPanel.add(dontsave,c);
		
		
		noPanel=new JPanel();
		
		
		noPanel.add(noofspLabel);
		
		noPanel.add(noofspText);
		
		noPanel.add(nooflpLabel);
		
		noPanel.add(nooflpText);
		
		noPanel.add(dynmemLabel);
		
		noPanel.add(dynmemText);
		
		c.gridy=5;
		c.gridwidth=4;
		comboPanel.add(noPanel,c);
		
	
		
		
		
		
		c.gridy=6;
		c.gridx=0;
		c.ipadx=0;
		c.insets = new Insets(20,10,0,-50);
		c.gridwidth=2;
		//c.gridheight=144;
		c.weightx=1;
		c.weighty= 0.5;
		
	   
		 keyoptArea=new JTextPane();
		 keyoptArea.setBackground(Color.WHITE);
		 Font helpFont = new Font("Garamond", Font.BOLD, 14);
		 keyoptArea.setFont(helpFont);
		 keyoptArea.setText("Use Shift + Left Click to modify the Input Tree");
		 keyoptArea.setEditable(false);
	    JScrollPane scrollPane;
	    scrollPane = new JScrollPane(keyoptArea);
	    
	     
	    importFile=new JButton("Fetch Input");
	    editFile=new JButton("Edit File");
	   // copyFile=new JButton("Copy Input");
	      
	    importFile.addActionListener(new InputFile());
	    editFile.addActionListener(new InputFile());
	  //  copyFile.addActionListener(new InputFile());
	    
	    
	    inputPanel=new JPanel(new GridLayout(3,0));
	    inputPanel.add(importFile);
	    inputPanel.add(editFile);
	    //inputPanel.add(copyFile);
	   
	    
	    /* inputPanel=new JPanel(new GridBagLayout());
	     * GridBagConstraints inputc= new GridBagConstraints();
	    inputc.gridx=0;
	    inputc.gridy=0;
	    inputc.weightx=0.5;
	   // inputc.insets =new Insets(5,25,0,-35);
	    inputc.insets =new Insets(30,-20,0,-70);
	    inputPanel.add(importFile,inputc);
	    inputc.gridy=1;
	    inputc.insets =new Insets(5,10,0,-45);
	    inputPanel.add(editFile,inputc);
	    inputc.gridy=2;
	    inputPanel.add(copyFile,inputc);
	    
	    */
	    /*keyoptArea.setAutoscrolls(true);
	    //keyoptArea.setLineWrap(true);
	    
	    comboPanel.add(scrollPane,c);
	    c.gridx=3;
	    c.ipadx=-160;
	    /*c.insets =new Insets(70,80,40,20);
	    c.insets =new Insets(70,80,40,10);
	    comboPanel.add(inputPanel,c);
	    

	 JPanel combPanel=new JPanel(new BorderLayout());

	 combPanel.add(comboPanel,BorderLayout.CENTER);
	 
 
	    /* Adding all the panels to the KeywordPanel 
		keyWordPanel.add(combPanel,BorderLayout.CENTER);

	/* End of the Interface  */ 	
	
		/* Listeners ----> */
		
		mhfItem.addActionListener(new MenuMethodListener());
	    comB3ly.addActionListener(new MenuMethodListener());
	    comB3pw.addActionListener(new MenuMethodListener());
	    comB3p8.addActionListener(new MenuMethodListener());
	    comB1ly.addActionListener(new MenuMethodListener());
	    comPbe1.addActionListener(new MenuMethodListener());
	    comMpw1.addActionListener(new MenuMethodListener());
	    
	    othB1b9.addActionListener(new MenuMethodListener());
	    othB98.addActionListener(new MenuMethodListener());
	    othB971.addActionListener(new MenuMethodListener());
	    othB972.addActionListener(new MenuMethodListener());
	    othBhan.addActionListener(new MenuMethodListener());
	    othBhlyp.addActionListener(new MenuMethodListener());
	    othLsda.addActionListener(new MenuMethodListener());
	    
	    stdVsxc.addActionListener(new MenuMethodListener());
	    stdHcth.addActionListener(new MenuMethodListener());
	    stdHct9.addActionListener(new MenuMethodListener());
	    stdHct14.addActionListener(new MenuMethodListener());
	    stdHct40.addActionListener(new MenuMethodListener());
	    
		
		mp2Item.addActionListener(new MenuMethodListener());
		mp3Item.addActionListener(new MenuMethodListener());
		mp4Item.addActionListener(new MenuMethodListener());
		mp4dItem.addActionListener(new MenuMethodListener());
		mp4sItem.addActionListener(new MenuMethodListener());
		mp5Item.addActionListener(new MenuMethodListener());
		
		ccdItem.addActionListener(new MenuMethodListener());
		ccsItem.addActionListener(new MenuMethodListener());
		ccsdItem.addActionListener(new MenuMethodListener());
		
		cidItem.addActionListener(new MenuMethodListener());
		cisdcItem.addActionListener(new MenuMethodListener());
		cisItem.addActionListener(new MenuMethodListener());
		cisdItem.addActionListener(new MenuMethodListener());
		sacItem.addActionListener(new MenuMethodListener());
		
		tdItem.addActionListener(new MenuMethodListener());
		tddItem.addActionListener(new MenuMethodListener());
		bdItem.addActionListener(new MenuMethodListener());
		gvbItem.addActionListener(new MenuMethodListener());
		mcasscfmItem.addActionListener(new MenuMethodListener());
		mcasscfItem.addActionListener(new MenuMethodListener());
		ovgfItem.addActionListener(new MenuMethodListener());
		w1Item.addActionListener(new MenuMethodListener());
		zinItem.addActionListener(new MenuMethodListener());
		
		am1Item.addActionListener(new MenuMethodListener());
		
		pm3Item.addActionListener(new MenuMethodListener());
		pm3mmItem.addActionListener(new MenuMethodListener());
		cndoItem.addActionListener(new MenuMethodListener());
		indoItem.addActionListener(new MenuMethodListener());
		
		mindoItem.addActionListener(new MenuMethodListener());
		mndoItem.addActionListener(new MenuMethodListener());
		
		cbs4Item.addActionListener(new MenuMethodListener());
		cbslItem.addActionListener(new MenuMethodListener());
		cbsqItem.addActionListener(new MenuMethodListener());
		cbsqbItem.addActionListener(new MenuMethodListener());
		cbsaItem.addActionListener(new MenuMethodListener());
	
		g1Item.addActionListener(new MenuMethodListener());
		g2Item.addActionListener(new MenuMethodListener());
		g2mp2Item.addActionListener(new MenuMethodListener());
		g3Item.addActionListener(new MenuMethodListener());
		g3mp2Item.addActionListener(new MenuMethodListener());
		g3b3Item.addActionListener(new MenuMethodListener());
		g3mp2b3Item.addActionListener(new MenuMethodListener());
		
		
	}
	
	
	public void mouseExited(MouseEvent me)
	{
	    
	
	}
	
	public void mouseReleased(MouseEvent me)
	{
	    
	    /*
	    int xCoord = me.getX();
	    int yCoord = me.getY();
	    System.out.println("into mouseReelesas");
	    if (me.isPopupTrigger()){
	       //TreePath path = getPathForLocation(xCoord, yCoord); 
	        Object obj = me.getComponent();
	        TreePath path = ((JTree) obj).getSelectionPath();
	        tree.setSelectionPath(path);
	        System.out.println(path);
	      
	        JPopupMenu popUp = new JPopupMenu();//createPopupMenu(false, true, true);
	        popUp.add("Remove");
	        if(path!=null)
	        popUp.show((Component)obj, xCoord, yCoord);
	    							}

	    */
	    
	    //newly added
	    
     
	    
	    
	    //end of newly added
	    
	    
	    
	    
	    
	    
	
	}
	
	public void mouseClicked(MouseEvent me)
	{
	   if(me.isShiftDown())
	    {
	        
	        Object obj = me.getComponent();
	        TreePath path = ((JTree) obj).getSelectionPath();
	        System.out.println(path.getLastPathComponent());
	         if((path.getLastPathComponent().toString().equals("Methods"))
	        || (path.getLastPathComponent().toString().equals("Basis Sets"))       
	        || (path.getLastPathComponent().toString().equals("Job Types"))
	        || (path.getLastPathComponent().toString().equals("Keywords"))
	        || (path.getLastPathComponent().toString().equals("G '03 Input Selections"))
	        )
	            JOptionPane.showMessageDialog(this,"Invalid Operation !!!","ERROR",JOptionPane.ERROR_MESSAGE);
	     else
	     {   
	        int n = JOptionPane.showConfirmDialog( this, "Are You Sure?",
	                "Delete the Node", JOptionPane.YES_NO_OPTION,
	                JOptionPane.QUESTION_MESSAGE, null );
	                if (n == JOptionPane.YES_OPTION) {
	              
	                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
	                    if(node == null) return;
	                    System.out.println("Node Selected" + node.toString());
	                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)(node.getParent());
	                    System.out.println("ParentNode selected" +parentNode.toString());
	                    if(parentNode == null) return;
	                    
	                    
	                    // code for removing other keys from Route section when removed from tree
	                  if(
						   
						   //(node.toString()=="Field")||
						   
						   (node.toString()=="TestMO")||
						   (node.toString()=="TrackIO")||
						   (node.toString()=="Transformation")||
						   (node.toString()=="Units"))
						   
	                    {
	                    	otherKeyTable.otherKeys.removeElement(node.toString());
	                     	RouteClass.writeRoute();
	                    }
	                	if(node.toString()=="Archive")
                    	{
                            otherKeyTable.otherKeys.removeElement(node.toString());
                    		RouteClass.writeRoute();
                    	}
                       	if(node.toString()=="Charge")
	                   	{
	               		otherKeyTable.otherKeys.removeElement(node.toString()+"="+otherKeyListener.charge_Opt);
	             		RouteClass.writeRoute();
	                   	}
	                   	if(node.toString()=="ExtraDensityBasis")
	                   	{
	                   	otherKeyTable.otherKeys.removeElement(node.toString());
	               		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="Constants")
	                    	{
	                    		System.out.println(otherKeyListener.con_Opt);
                		  		otherKeyTable.otherKeys.removeElement(node.toString()+"="+otherKeyListener.con_Opt);
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="Complex")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement(node.toString());
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="GFInput")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement(node.toString());
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="GFPrint")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement(node.toString());
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="CounterPoise")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement(node.toString()+"="+otherKeyListener.counter_Opt);
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="ChkBasis")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement("ChkBasis");
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="CPHF")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement(node.toString()+"="+otherKeyListener.cphfOpt);
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="DensityFit")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement(node.toString()+"="+otherKeyListener.densityOpt);
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="FMM")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement(node.toString()+"="+otherKeyListener.fmmOpt);
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="External")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement(node.toString());
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="ExtraBasis")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement(node.toString());
	                    		RouteClass.writeRoute();
	                    	}
	                    	
	                    	if(node.toString()=="NMR")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement(node.toString()+"="+otherKeyListener.nmrOpt);
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="Output")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement(node.toString()+"= "+otherKeyListener.output_Opt);
	                    		RouteClass.writeRoute();
	                    	}
	                    	
	                    	if(node.toString()=="Iop")
	                    	{
	                    		otherKeyTable.okIop.setSelected(false);
	                    		otherKeyTable.otherKeys.removeElement(iopKeymodify.iopOp);
	                    		
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="Pressure")
	                    	{
	                    		otherKeyTable.otherKeys.removeElement(otherKeyTable.pressureOpt);
	                    		RouteClass.writeRoute();
	                    	}
	                    	if(node.toString()=="Prop")
	                    	
	                    	{
	                    		System.out.println("In listener for Prop"+otherKeyListener.prop_Opt);
	                    		otherKeyTable.otherKeys.removeElement("Prop="+otherKeyListener.prop_Opt);
	                    		RouteClass.writeRoute();
	                       	}
	                    	if(node.toString()=="Pseudo")
		                    {
	                    			otherKeyTable.otherKeys.removeElement("Pseudo="+otherKeyListener.pseudo_Opt);
		                    		RouteClass.writeRoute();
		                    }
	                    	if(node.toString()=="Punch")
		                    {
	                    			otherKeyTable.otherKeys.removeElement("Pseudo="+otherKeyListener.punch_Opt);
		                    		RouteClass.writeRoute();
		                    }
	                    	if(node.toString()=="Scale")
		                    {
	                    			otherKeyTable.otherKeys.removeElement(otherKeyTable.scale_Opt);
		                    		RouteClass.writeRoute();
		                    }

	                    	if(node.toString()=="Name")
		                    {
	                    			otherKeyTable.otherKeys.removeElement(node.toString());
		                    		RouteClass.writeRoute();
		                    }
	                    	
	                    	if(node.toString()=="Sparse")
		                    {
	                    			otherKeyTable.otherKeys.removeElement("Sparse="+otherKeyListener.sparse_Opt);
		                    		RouteClass.writeRoute();
		                    }
	                    	if(node.toString()=="Symmetry")
		                    {
	                    			otherKeyTable.otherKeys.removeElement("Symmetry="+otherKeyListener.symm_Opt);
		                    		RouteClass.writeRoute();
		                    }
	                    	if(node.toString()=="Temperature")
		                    {
	                    			otherKeyTable.otherKeys.removeElement(otherKeyTable.temp_Opt);
		                    		RouteClass.writeRoute();
		                    }
                    

	                    
	                    
	        /*            if(otherKeyTable.okVector.size()>= 1)
	                    {
	                    for(int h=0;h< otherKeyTable.okVector.size();h++)
	                    {  	
	                    	System.out.println("To check" +node.toString());
            	                   if(((JRadioButton)(otherKeyTable.okVector.get(h))).getActionCommand().toString().equals(node.toString()))    
	                    {
		                   	if(((JRadioButton)(otherKeyTable.okVector.get(h))).isSelected())
	                          {
	                          //	((JRadioButton)(otherKeyTable.okVector.get(h))).setSelected(false);
	                          }
		                   	
	                           
	                       }
	                    }  
	                    }
		      */              
	                    if(parentNode.toString().equals("Freq"))
	                    {
	                       System.out.println(FreqOptTable.table.getSelectionModel().toString());
	                       for(int h=0;h<FreqOptTable.freqClear.size();h++)
	                       {
	                       	if(((JRadioButton)(FreqOptTable.freqClear.get(h))).getActionCommand().toString().equals(node.toString()))    
	                       	{
	                       	if(((JRadioButton)(FreqOptTable.freqClear.get(h))).isSelected())
	                             {
	                             	
	    	                       	    System.out.println("Test case1 "+(JRadioButton)(FreqOptTable.freqClear.get(h)));
	                             		((JRadioButton)(FreqOptTable.freqClear.get(h))).setSelected(false);
	                             		FreqOptTable.freqOpt.removeElement(node.toString());
	    	                       	}
	    	                      
	                             }
	                       }
	                             if(FreqOptTable.FStep.isSelected())
	                             {
	                             	if(node.toString().startsWith("Step="))
	                             	{
	                             		FreqOptTable.FStep.setSelected(false);
	                             		FreqOptTable.freqOpt.removeElement(node.toString());
	                             	}
	                             }
	                          }
	                    if(parentNode.toString().equals("Guess"))
	                    {
	                       for(int h=0;h<GuessOptTable.guessClear.size();h++)
	                       {
	                             if(((JRadioButton)(GuessOptTable.guessClear.get(h))).isSelected())
	                             {
	                             	if(((JRadioButton)(GuessOptTable.guessClear.get(h))).getActionCommand().toString().equals(node.toString()))
	    	                       	{
	                             		((JRadioButton)(GuessOptTable.guessClear.get(h))).setSelected(false);
	                             		GuessOptTable.guessOpt.removeElement(node.toString());
	    	                       	}
	    	                      
	                             }
	                   
	                       }
	                       
	                       }
	                   
	                    if(parentNode.toString().equals("Pop"))
	                    {
	                       System.out.println("The deleted node"+node);	
	                      // System.out.println(GuessOptTable.table.getSelectionModel().toString());
	                           
	                       for(int h=0;h<popOptTable.popClear.size();h++)
	                       {
	                             if(((JRadioButton)(popOptTable.popClear.get(h))).isSelected())
	                             {
	                             	if(((JRadioButton)(popOptTable.popClear.get(h))).getActionCommand().toString().equals(node.toString()))
	    	                       	{
	                             		((JRadioButton)(popOptTable.popClear.get(h))).setSelected(false);
	                             		popOptTable.popOpt.removeElement(node.toString());
	    	                       	}
	    	                      
	                             }
	                   
	                       }
	                       
	                       }
	                    if(parentNode.toString().equals("PBC"))
	                    {
	                        System.out.println("in PBC");
	                        //Clear the selected items
	                        for(int j=0;j<8;j++)
	                        {   
	                            System.out.println("Checking For loop"+j);
	                            System.out.println("Option Name"+pbcTable.pbcOptions[j].getLabel());
	                            System.out.println(node.toString());
	                            if(pbcTable.pbcOptions[j].getLabel().toString().startsWith(node.toString()))
	                            {
	                                System.out.println("IF LOOP ::: "+node.toString());
	                                pbcTable.pbcOptions[j].setSelected(false);
	                                pbcTable.selectedIndex[j]=0;
	                                pbcTable.initValues[j]="";
	                            }
	                        }
	                        
	                        
	                    }


                 if(node.toString().equals("PBC"))
	                    {
	                        System.out.println("in PBC");
	                        //Clear the selected items
	                        for(int j=0;j<8;j++)
	                        {                     
	                            
	                            
	                                pbcTable.pbcOptions[j].setSelected(false);
	                                pbcTable.selectedIndex[j]=0;
	                                pbcTable.initValues[j]="";
	                            
	                        }
	                        
	                        
	                    }
	                    
	                    if(parentNode.toString().equals("Opt  "))
	                    {
	                       System.out.println("The deleted node"+node);	
	                     //  System.out.println(OptTable.table.getSelectionModel().toString());
	                           
	                       for(int h=0;h<OptTable.optClear.size();h++)
	                       {
	                             if(((JRadioButton)(OptTable.optClear.get(h))).isSelected())
	                             {
	                             	if(((JRadioButton)(OptTable.optClear.get(h))).getActionCommand().toString().equals(node.toString()))
	    	                       	{
	                             		((JRadioButton)(OptTable.optClear.get(h))).setSelected(false);
	                             		OptTable.optVopt.removeElement(node.toString());
	    	                       	}
	    	                      
	                             }
	                   
	                       }
	                       
	                       if(OptTable.OMaxCy.isSelected())
                           {
                           	if(node.toString().startsWith("MaxCycle="))
                           	{
                           	    OptTable.OMaxCy.setSelected(false);
                           		OptTable.optVopt.removeElement(node.toString());
                           	}
                           }
	                       
	                       if(OptTable.OMaxSt.isSelected())
                           {
                           	if(node.toString().startsWith("MaxStep="))
                           	{
                           		OptTable.OMaxSt.setSelected(false);
                           		OptTable.optVopt.removeElement(node.toString());
                           	}
                           }
	                       
	                       if(OptTable.OSadd.isSelected())
                           {
                           	if(node.toString().startsWith("Step="))
                           	{
                           		OptTable.OSadd.setSelected(false);
                           		OptTable.optVopt.removeElement(node.toString());
                           	}
                           }
	                       
	                       
	                       if(OptTable.OPathN.isSelected())
                           {
                           	if(node.toString().startsWith("Path="))
                           	{
                           		OptTable.OPathN.setSelected(false);
                           		OptTable.optVopt.removeElement(node.toString());
                           	}
                           }
                 
	                       if(OptTable.OChkHar.isSelected())
                           {
                           	if(node.toString().startsWith("CheckHarmonic="))
                           	{
                           		OptTable.OChkHar.setSelected(false);
                           		OptTable.optVopt.removeElement(node.toString());
                           	}
                           }  
	                       if(OptTable.OInitHar.isSelected())
                           {
                           	if(node.toString().startsWith("InitialHarmonic="))
                           	{
                           		OptTable.OInitHar.setSelected(false);
                           		OptTable.optVopt.removeElement(node.toString());
                           	}
                           }
	                       if(OptTable.OReadHar.isSelected())
                           {
                           	if(node.toString().startsWith("ReadHarmonic="))
                           	{
                           		OptTable.OReadHar.setSelected(false);
                           		OptTable.optVopt.removeElement(node.toString());
                           	}
                           }
	                       }
	                    if(parentNode.toString().equals("Geom"))
	                    {
	                                      
	                       for(int h=0;h<geomOptTable.geomClear.size();h++)
	                       {
	                             if(((JRadioButton)(geomOptTable.geomClear.get(h))).isSelected())
	                             {
	                             	if(((JRadioButton)(geomOptTable.geomClear.get(h))).getActionCommand().toString().equals(node.toString()))
	    	                       	{
	                             		((JRadioButton)(geomOptTable.geomClear.get(h))).setSelected(false);
	                             		geomOptTable.geomOpt.removeElement(node.toString());
	    	                       	}
	    	                      
	                             }
	                   
	                       }
	                       if(geomOptTable.GeRead.isSelected())
                           {
                           	if(node.toString().startsWith("ReadHarmonic="))
                           	{
                           		geomOptTable.GeRead.setSelected(false);
                           		geomOptTable.geomOpt.removeElement(node.toString());
                           	}
                           }
	                       if(geomOptTable.GeStep.isSelected())
                           {
                           	if(node.toString().startsWith("Step="))
                           	{
                           		geomOptTable.GeStep.setSelected(false);
                           		geomOptTable.geomOpt.removeElement(node.toString());
                           	}
                           }
	                       if(geomOptTable.GeCheck.isSelected())
                           {
                           	if(node.toString().startsWith("CheckHarmonic="))
                           	{
                           		geomOptTable.GeCheck.setSelected(false);
                           		geomOptTable.geomOpt.removeElement(node.toString());
                           	}
                           }
	                 }
	                    RouteClass.rewriteRoute(node.toString(),parentNode.toString());
	                    System.out.println("Archive testing+ parent "  + parentNode.toString());
	                    
	                    if(InsertNode.nodeExists(node.toString()))
	                    parentNode.remove(node);
	                    ((DefaultTreeModel)tree.getModel()).reload(parentNode);
	                }
	                else if (n == JOptionPane.NO_OPTION) {
	                    return;
	                } else {
	                    return;
	                }

	    }
	                
	                
	                }
	}
	








	public void mousePressed(MouseEvent me)
	{
	    
	
	}
	
	public void mouseEntered(MouseEvent me)
	{
	    
	
	}
	

	 public static void main(String[] args) 
	 {
           javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() 
            {
           JFrame.setDefaultLookAndFeelDecorated(true);
        	JDialog.setDefaultLookAndFeelDecorated(true);
        	
                MetalTheme theme = new ColorTheme();  
                // set the chosen theme
             MetalLookAndFeel.setCurrentTheme(theme);
                // Show name of the theme as window title
           //  this.setTitle(theme.getName());
             mainFrame = new G03MenuTree();
             try
             {
                 UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                 SwingUtilities.updateComponentTreeUI(mainFrame);
             }
             catch(Exception e)
             {
                 System.out.println(e);
             }
        	
        	
        	
        	
        	
        	mainFrame.setLocation(5,30);
        	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	mainFrame.pack();
        	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        	//mainFrame.setSize(screenSize.width-20,screenSize.height-100);
        	screenWidth=screenSize.width-200;
        	screenHeight=screenSize.height-150;
        	mainFrame.setSize(screenSize.width-200,screenSize.height-150);
        	mainFrame.setResizable(true);
        	mainFrame.setVisible(true);
            }
        });
     }
     
	 // Allows you to add content to the Editorpane
	  
	 public static void insertHTML
	  (JEditorPane editor, String html)
	                                 throws IOException,BadLocationException {
	  //assumes editor is already set to "text/html" type
	 	int location;
	  HTMLEditorKit kit =
	    (HTMLEditorKit) editor.getEditorKit();
	  Document doc = editor.getDocument();
	  location=doc.getLength();
	  StringReader reader = new StringReader(html);
	  kit.read(reader, doc, location);
	  editor.setCaretPosition(editor.getDocument().getLength());
	}




}
	
	


