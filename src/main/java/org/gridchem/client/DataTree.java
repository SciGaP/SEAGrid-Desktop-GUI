package org.gridchem.client;

/** @author Dodi Heryadi, NCSA
    @version $Id: DataTree.java,v 1.9 2006/03/03 16:56:50 spamidig Exp $
    This is where the Parsing of output is being called.  Will need more parsing
    for Gaussian outputs.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.gridchem.client.common.Settings;
import org.gridchem.client.util.Env;


import nanocad.GetDataFile;
import nanocad.ZipExtractor;
import nanocad.nanocadFrame2;

import java.io.*;
import java.net.URL;
import java.util.*;
import java_cup.*;

public final class DataTree extends JFrame {

  //Optionally play with line styles of the tree.  Possible values are:
  //"Angled", "Horizontal", "None" (the default).
  private boolean playWithLineStyle = true;
  private String lineStyle = "Angled"; 
  //private String prefix = "file:" 
  //                        + System.getProperty("user.dir")
  //                        + System.getProperty("file.separator");
  private String prefix = "file:" + Env.getApplicationDataDir() + 
  	Settings.fileSeparator;
  private static String datafile;

  //the tree structures demand that many fields be static;
  //this is worth optimizing
  public static DefaultMutableTreeNode nodeOnDeck;
  public static String dataFileName;
  public static JSplitPane horiSplitPane;
  public static JSplitPane vertSplitPane; 
  public static ControlPanel controlPane;
  public static JScrollPane controlView;
  public static JEditorPane htmlPane;
  public static URL helpURL;

  public JScrollPane htmlView;
  public JScrollPane treeView;






  public DataTree() {
    super("Explore Data");

    //Launch the Conf lexer on configuration file "qcrjm.conf"
    Conf.main(new String[] {Settings.jobDir + 
    		Settings.fileSeparator + "qcrjm.conf"});

    //Create the rootlet nodes.
    DefaultMutableTreeNode top = new DefaultMutableTreeNode(
      "Available Data");
    createRootlets(top);

    //Create a tree that allows one selection at a time.
    final JTree tree = new JTree(top);
    tree.getSelectionModel().setSelectionMode(
      TreeSelectionModel.SINGLE_TREE_SELECTION);

    //__________________________________________________________
    //Listen for when the selection changes.
    tree.addTreeSelectionListener(new TreeSelectionListener() {
	public void valueChanged(TreeSelectionEvent e) {

	  DefaultMutableTreeNode node = 
	    (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
	  if (node == null) return;
	  Object nodeInfo = node.getUserObject();
	  if (node.getLevel() > 1) {

	    NodeInfo current = (NodeInfo)nodeInfo;
	    try{

              //Update htmlView
	      DataTree.displayURL(current.nodeURL);
	      if(Settings.DEBUG) System.out.println(
		"DataTree:valueChanged:current.nodeURL:  "+current.nodeURL);
	    } catch (Exception e2) {

	      DataTree.displayURL(DataTree.helpURL); // show logo
	    }
	    try{

 	      //Update controlView
	      DataTree.controlPane = (ControlPanel)current.controls;
	      DataTree.controlView = new JScrollPane(DataTree.controlPane);
	      DataTree.controlView.setMinimumSize(Settings.MIN_SIZE);
	      vertSplitPane.setTopComponent(DataTree.controlView);
	      horiSplitPane.setRightComponent(vertSplitPane);
	      horiSplitPane.setDividerLocation(Settings.HORI_DIVIDER_LOC); //XXX: ignored in some releases
                                                                           //of Swing. bug 4101306
	      //Workaround for Swing. bug 4101306
	      getContentPane().add(horiSplitPane, BorderLayout.CENTER);

	    } catch (Exception e1) {

	      if(Settings.DEBUG) System.out.print(
		"Couldn't update DataTree.controlPane...\n"); 
	    }
	  } else {// node.getLevel() <= 1

	    DataTree.displayURL(DataTree.helpURL); // show logo
	  }
	  if(Settings.DEBUG) System.out.println(nodeInfo.toString()); 
	}
      });
    //end anonymous inner class
    //_________________________________________________________


    if (playWithLineStyle) { // of the tree
      tree.putClientProperty("JTree.lineStyle", lineStyle);
    }

    //Create the tree pane.
    treeView = new JScrollPane(tree);
    treeView.setMinimumSize(Settings.MIN_SIZE); 

    //Create the HTML pane.
//   DataTree.initHelp();
    DataTree.htmlPane = new JEditorPane();
    DataTree.htmlPane.setEditable(false);
    htmlView = new JScrollPane(DataTree.htmlPane);
    htmlView.setMinimumSize(Settings.MIN_SIZE); 
    DataTree.initHelp();
    //System.out.println("DataTree:146:helpURL "+DataTree.helpURL);
    //DataTree.displayURL(DataTree.helpURL); // show logo

    //Create the controls pane.
    DataTree.controlPane = new ControlPanel("", "");
    DataTree.controlView = new JScrollPane(DataTree.controlPane);
    DataTree.controlView.setMinimumSize(Settings.MIN_SIZE); 

    //Create the split panes.
    vertSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    vertSplitPane.setDividerLocation(Settings.VERT_DIVIDER_LOC); //XXX: ignored in some releases
                                                                //of Swing. bug 4101306
    //Workaround for Swing. bug 4101306
    //vertSplitPane.setPreferredSize(new Dimension(Settings.VERT_DIVIDER_LOC, Settings.VERT_DIVIDER_LOC));
    vertSplitPane.setTopComponent(DataTree.controlView);
    vertSplitPane.setBottomComponent(htmlView);

    horiSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    horiSplitPane.setLeftComponent(treeView);
    horiSplitPane.setRightComponent(vertSplitPane);
    horiSplitPane.setDividerLocation(Settings.HORI_DIVIDER_LOC); //XXX: ignored in some releases
                                                                 //of Swing. bug 4101306
    //Workaround for Swing. bug 4101306
    //horiSplitPane.setPreferredSize(new Dimension(Settings.HORI_DIVIDER_LOC, Settings.HORI_DIVIDER_LOC));
    horiSplitPane.setPreferredSize(Settings.FRAME_DIM);

    //Add the split panes to this frame.
    getContentPane().add(horiSplitPane, BorderLayout.CENTER);
    pack();

  }// end constructor






  private void createRootlets(DefaultMutableTreeNode top) {
    // initialize Nodes
    DefaultMutableTreeNode job = null;
    DefaultMutableTreeNode raw = null;

    //____________________________________________________________
    // create a branch for each datafile
    for(Iterator i = Conf.datafileList.iterator(); i.hasNext();) {

      datafile = (String)i.next();
      dataFileName = datafile;
      if(Settings.DEBUG) System.out.println("datafile = " + datafile); 

      // push job nodes
      job = new DefaultMutableTreeNode("job named " + getNameNoSuffix());
      if(Settings.DEBUG) System.out.println("job named " + getNameNoSuffix());
      top.add(job);

      // push raw nodes
      raw = new DefaultMutableTreeNode(new NodeInfo
				       ("raw output for " + getNameNoSuffix(), 
					prefix + getOutputFile()));
      job.add(raw);

      // create branches of parsed info from each datafile
      // via the hierarchy of lexers
      WhichProgram.main(new String[] {getOutputFile()});

//     DataTree.nodeOnDeck = null;
     DataTree.nodeOnDeck = new DefaultMutableTreeNode(new NodeInfo
                            ("data from output", prefix+"qcrjm2002logo.htm"));

    /** KEY ENTRY SITE:
       look at the output file.  Is it Gaussian or GAMESS?  And then check
       the runtype.  Execute The parser based on the information

       */


/* First remove the following files if they exist: runtype1, runtype2,
   runtype, Energy_data, temporary2, temporary3, Gradient_data, temporary5,
   temporary6, Gradient_dataa, Gradient_datab */

   try {
       File file1 = new File(Env.getApplicationDataDir() + 
       		Settings.fileSeparator + "runtype1");
       if (file1.exists()) {boolean bl1 = file1.delete();}
        File file2 = new File(Env.getApplicationDataDir() + 
           		Settings.fileSeparator +"runtype2");
       if (file2.exists()) {boolean bl2 = file2.delete();}
        File file3 = new File(Env.getApplicationDataDir() + 
           		Settings.fileSeparator +"runtype");
       if (file3.exists()) {boolean bl3 = file3.delete();}

        File file4 = new File(Env.getApplicationDataDir() + 
           		Settings.fileSeparator +"Energy_data");
       if (file4.exists()) {boolean bl4 = file4.delete();}
        File file5 = new File(Env.getApplicationDataDir() + 
           		Settings.fileSeparator +"temporary2");
       if (file5.exists()) {boolean bl5 = file5.delete();}
        File file6 = new File(Env.getApplicationDataDir() + 
           		Settings.fileSeparator +"temporary3");
       if (file6.exists()) {boolean bl6 = file6.delete();}
       File file7 = new File(Env.getApplicationDataDir() + 
       		Settings.fileSeparator +"Gradient_data");
       if (file7.exists()) {boolean bl7 = file7.delete();}
       File file8 = new File(Env.getApplicationDataDir() + 
       		Settings.fileSeparator +"temporary5");
       if (file8.exists()) {boolean bl8 = file8.delete();}
       File file9 = new File(Env.getApplicationDataDir() + 
       		Settings.fileSeparator +"temporary6");
       if (file9.exists()) {boolean bl9 = file9.delete();}
       File file10 = new File(Env.getApplicationDataDir() + 
       		Settings.fileSeparator +"Gradient_dataa");
       if (file10.exists()) {boolean bl10 = file10.delete();}
        File file11 = new File(Env.getApplicationDataDir() + 
           		Settings.fileSeparator +"Gradient_datab");
       if (file11.exists()) {boolean bl11 = file11.delete();}
        File file12 = new File(Env.getApplicationDataDir() + 
           		Settings.fileSeparator +"finalcoord.pdb");
       if (file12.exists()) {boolean bl12 = file12.delete();}
        File file13 = new File(Env.getApplicationDataDir() + 
           		Settings.fileSeparator +"numatom");
       if (file13.exists()) {boolean bl13 = file13.delete();}

      }
      catch (Exception ie) { System.out.println("Error in file deletion");}



   



//WhichProgram.format  finds which one.  If it's Gaussian then:
  System.out.println(WhichProgram.format);
  if (WhichProgram.format == "Gauss03") {
  	try {
	
//First find the method
  		MethodParser pp = new MethodParser(
  				new MethodLexer(
  						new FileReader(DataTree.getOutputFile())
  				)); 
  		Object result = pp.parse().value;  
// then find the wavefunction
  		WavefunctionParser pp11 = new WavefunctionParser(
  				new WavefunctionLexer(
  						new FileReader(DataTree.getOutputFile())
  				));
  		Object result11 = pp11.parse().value;

// concatenate runtyp1  and runtype2 into runtype
  		InputStream mylist1a = new FileInputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "runtype1");
  		InputStream mylist2a = new FileInputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "runtype2");
  		SequenceInputStream str4a = new SequenceInputStream(mylist1a, mylist2a);
  		PrintStream temp4a = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "runtype"));       
  		
  		int ccc1;
        while ((ccc1 = str4a.read()) != -1)
           temp4a.write(ccc1);

// read the runtype file 
        FileInputStream fis = new FileInputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "runtype");
        DataInputStream dis = new DataInputStream(new BufferedInputStream(fis));
        String record = dis.readLine();
        System.out.println("From reading runtype:"+record); 
        String record1 = String.valueOf(new char [] {'o', 'p', 't', 'R', 'H', 'F'} );
        String record1a = String.valueOf(new char [] {'o', 'p', 't', 'B', '3', 'L', 'Y', 'P'} );

        String record1b = String.valueOf(new char [] {'o', 'p', 't', 'c', 'a', 's',
        		's', 'c', 'f'} );

        String record1c = String.valueOf(new char [] {'o', 'p', 't', 'c', 'c', 's',
        'd'} );
        String record1d = String.valueOf(new char [] {'s', 'c', 'f', 'R', 'H', 'F'} );       
        String record1e= String.valueOf(new char [] {'o', 'p', 't', 'B', '3', 'P', 'W', '9', '1'} );
        String record1f= String.valueOf(new char [] {'o', 'p', 't', 'B', '1', 'B', '9', '5'} );
        String record3= String.valueOf(new char [] {'h', 'f', 'o', 'p', 't'} );
        String record2= String.valueOf(new char [] {'o', 'p', 't', 'M', 'P', '2'} );
        String record4= String.valueOf(new char [] {'G', '1', 'g', 'e', 'o', 'm'} );

        String record5= String.valueOf(new char [] {'C', 'B', 'S', '-', 'Q', 'g', 'e', 'o', 'm'} );
        
        //this is for SCF, B3LYP, B3PW91(?), MP2, CASSCF Optimization
        if(record1.equals(record) || record1a.equals(record) || record1b.equals(record) || record1c.equals(record)|| record1e.equals(record) ||record1f.equals(record)) 
        {
        	System.out.println("IN THE GOPTParser");
        	try {
        		GOPTParser pp1 = new GOPTParser
				(new GOPTLexer(
						new FileReader(DataTree.getOutputFile())
				));
        		Object result1 = pp1.parse().value;



        		// check whether temporary* files exist
        		File file1 = new File(Env.getApplicationDataDir() + 
          	       		Settings.fileSeparator + "Energy_data");
        		File file2 = new File(Env.getApplicationDataDir() + 
          	       		Settings.fileSeparator + "temporary2");
        		File file3 = new File(Env.getApplicationDataDir() + 
          	       		Settings.fileSeparator + "temporary3");

        		//if the files don't exist, printout the message:
        		if ( !file2.exists() || !file3.exists() || !file1.exists() ) {
        			System.out.println("348:DataTree: commented Joption message where i says calculation not completed");
        		/*	JOptionPane.showMessageDialog(null, 
        					"The calculation has not completed yet.  Please click the OK button to continue", 
							"MainPanel:Gaussian Optimization", 
							JOptionPane.INFORMATION_MESSAGE); */
        			} 
        
        		// concatenate temporary2 and temporary3 into Gradient_data
        		if (file2.exists()) {
        			if (file3.exists()) {
        				if (file1.exists()) {
        					InputStream mylist1 = new FileInputStream(Env.getApplicationDataDir() + 
        			  	       		Settings.fileSeparator + "temporary2");
        					InputStream mylist2 = new FileInputStream(Env.getApplicationDataDir() + 
        			  	       		Settings.fileSeparator + "temporary3");
        					SequenceInputStream str4 = new SequenceInputStream(mylist1, mylist2);
        					PrintStream temp4 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
        			  	       		Settings.fileSeparator + "Gradient_data"));      
        					
        					int ccc;
        					while ((ccc = str4.read()) != -1)
        						temp4.write(ccc);
 
        					/*
        					TwoPlotExample plotstuff1 = new TwoPlotExample(Env.getApplicationDataDir() + 
        			  	       		Settings.fileSeparator + "Energy_data", Env.getApplicationDataDir() + 
					  	       		Settings.fileSeparator + "Gradient_data"); }
					  	    */
        					OnePlotExample plotstuff1 = new OnePlotExample(Env.getApplicationDataDir() + 
        			  	       		Settings.fileSeparator + "Energy_data");
        					OnePlotExample plotstuff2 = new OnePlotExample(Env.getApplicationDataDir() + 
        			  	       		Settings.fileSeparator + "Gradient_data");
        				}
        			} }

        	}
        	catch (Exception ie) {System.out.println("Exception in GOPT"+ie);}



// printing out the final coordinates:  InputParser and InputLexer for all 
// coordinates (i.e. in each iteration)
        	try {
        		FinalCoordParser pp1a = new FinalCoordParser
				(new FinalCoordLexer(
						new FileReader(DataTree.getOutputFile())
				));
        		Object result1a = pp1a.parse().value;
        	}
        	catch (Exception ie) {System.out.println("Exception in Inputa"+ie);}

   
// 11/13/2002: for the connectivity
        	Connect1 con = new Connect1(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "connect.pdb");
        	// concatenate final.pdb  and connect into finalcoord.pdb
        	InputStream mylist1a1 = new FileInputStream(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "final.pdb");
        	InputStream mylist2a1 = new FileInputStream(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "connect");
        	SequenceInputStream str4a1 = new SequenceInputStream(mylist1a1, mylist2a1);
        	PrintStream temp4a1 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "finalcoord.pdb"));
        	int ccc1a;
        	while ((ccc1a = str4a1.read()) != -1)
        		temp4a1.write(ccc1a);


 }


//this is for MP2 optimization 
        else if(record2.equals(record))
        {
        	System.out.println("IN THE GMP2OPTParser");
        	GMP2OPTParser pp1 = new GMP2OPTParser
			(new GMP2OPTLexer(
					new FileReader(DataTree.getOutputFile())
			));
        	Object result1 = pp1.parse().value;

// check whether temporary* files exist
        	File file1 = new File(Env.getApplicationDataDir() + 
        				Settings.fileSeparator + "Energy_data");
        	File file2 = new File(Env.getApplicationDataDir() + 
         		Settings.fileSeparator + "temporary2");
        	File file3 = new File(Env.getApplicationDataDir() + 
        			Settings.fileSeparator + "temporary3");
        
//if the files don't exist, printout the message:
        	if ( !file2.exists() || !file3.exists() || !file1.exists() ) {
        		JOptionPane.showMessageDialog(null, 
        				"The calculation has not completed yet.  Please click the OK button to continue", 
						"MainPanel:Gaussian MP2 Optimization", 
						JOptionPane.INFORMATION_MESSAGE); } 
// concatenate temporary2 and temporary3 into Gradient_data
        	if (file2.exists()) {
        		if (file3.exists()) {
        			if (file1.exists()) {
        				InputStream mylist1 = new FileInputStream(Env.getApplicationDataDir() + 
        		  	       		Settings.fileSeparator + "temporary2");
        				InputStream mylist2 = new FileInputStream(Env.getApplicationDataDir() + 
        		  	       		Settings.fileSeparator + "temporary3");
        				SequenceInputStream str4 = new SequenceInputStream(mylist1, mylist2);
        				PrintStream temp4 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
        		  	       		Settings.fileSeparator + "Gradient_data"));       
        				int ccc;
        				while ((ccc = str4.read()) != -1)
        					temp4.write(ccc);
 
        				/*
        				TwoPlotExample plotstuff1 = new TwoPlotExample(Env.getApplicationDataDir() + 
        		  	       		Settings.fileSeparator + "Energy_data", Env.getApplicationDataDir() + 
				  	       		Settings.fileSeparator + "Gradient_data");
        				*/
        				OnePlotExample plotstuff1 = new OnePlotExample(Env.getApplicationDataDir() + 
    			  	       		Settings.fileSeparator + "Energy_data");
    					OnePlotExample plotstuff2 = new OnePlotExample(Env.getApplicationDataDir() + 
    			  	       		Settings.fileSeparator + "Gradient_data");
        			} } }



// printing out the final coordinates:  InputParser and InputLexer for all 
// coordinates (i.e. in each iteration)
        	try {
        		FinalCoordParser pp1a = new FinalCoordParser
				(new FinalCoordLexer(
						new FileReader(DataTree.getOutputFile())
				));
        		Object result1a = pp1a.parse().value;
        	}
        	catch (Exception ie) {System.out.println("Exception in Inputa"+ie);}

// 11/13/2002: for the connectivity
        	Connect1 con = new Connect1(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "connect.pdb");
// concatenate final.pdb  and connect into finalcoord.pdb
        	InputStream mylist1a1 = new FileInputStream(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "final.pdb");
        	InputStream mylist2a1 = new FileInputStream(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "connect");
        	SequenceInputStream str4a1 = new SequenceInputStream(mylist1a1, mylist2a1);
        	PrintStream temp4a1 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "finalcoord.pdb"));
        	int ccc1a;
        	while ((ccc1a = str4a1.read()) != -1)
        		temp4a1.write(ccc1a);

        }

        else if(record3.equals(record))
        {
        	System.out.println("IN THE GOPTParser");
        	GOPTParser pp1 = new GOPTParser
			(new GOPTLexer(
					new FileReader(DataTree.getOutputFile())
			));
        	Object result1 = pp1.parse().value;
 
        	// check whether temporary* files exist
        	File file1 = new File(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "Energy_data");
        	File file2 = new File(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "temporary2");
        	File file3 = new File(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "temporary3");
        
// concatenate temporary2 and temporary3 into Gradient_data
        	if (file2.exists()) {
        		if (file3.exists()) {
        			if (file1.exists()) {
        				InputStream mylist1 = new FileInputStream(Env.getApplicationDataDir() + 
        		  	       		Settings.fileSeparator + "temporary2");
        				InputStream mylist2 = new FileInputStream(Env.getApplicationDataDir() + 
        		  	       		Settings.fileSeparator + "temporary3");
        				SequenceInputStream str4 = new SequenceInputStream(mylist1, mylist2);
        				PrintStream temp4 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
        		  	       		Settings.fileSeparator + "Gradient_data"));        
        				int ccc;
 
        				while ((ccc = str4.read()) != -1)
        					temp4.write(ccc);
 
        				/*
        				TwoPlotExample plotstuff1 = new TwoPlotExample(Env.getApplicationDataDir() + 
        		  	       		Settings.fileSeparator + "Energy_data", Env.getApplicationDataDir() + 
				  	       		Settings.fileSeparator + "Gradient_data");
        				*/
        				OnePlotExample plotstuff1 = new OnePlotExample(Env.getApplicationDataDir() + 
    			  	       		Settings.fileSeparator + "Energy_data");
    					OnePlotExample plotstuff2 = new OnePlotExample(Env.getApplicationDataDir() + 
    			  	       		Settings.fileSeparator + "Gradient_data");
        			} } }

// printing out the final coordinates:  InputParser and InputLexer for all
// coordinates (i.e. in each iteration)
        	try {
        		FinalCoordParser pp1a = new FinalCoordParser
				(new FinalCoordLexer(
						new FileReader(DataTree.getOutputFile())
				));
        		Object result1a = pp1a.parse().value;
        	}
        		catch (Exception ie) {System.out.println("Exception in Inputa");}


// 11/13/2002: for the connectivity
         Connect1 con = new Connect1(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "connect.pdb");
// concatenate final.pdb  and connect into finalcoord.pdb
         InputStream mylist1a1 = new FileInputStream(Env.getApplicationDataDir() + 
         		Settings.fileSeparator + "final.pdb");
         InputStream mylist2a1 = new FileInputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "connect");
         SequenceInputStream str4a1 = new SequenceInputStream(mylist1a1, mylist2a1);
         PrintStream temp4a1 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
         		Settings.fileSeparator + "finalcoord.pdb"));
         int ccc1a;
         while ((ccc1a = str4a1.read()) != -1)
         	temp4a1.write(ccc1a);
}



// this is for SCF energy

        else if (record1d.equals(record))
        {
        	System.out.println("IN THE SCFaParser");
        	SCFaParser pp1 = new SCFaParser
			(new SCFaLexer(
					new FileReader(DataTree.getOutputFile())
			));
	            Object result1 = pp1.parse().value; 
	// check whether temporary* files exist
	            File file1 = new File(Env.getApplicationDataDir() + 
	      	       		Settings.fileSeparator + "Gradient_data");
        
//if the files don't exist, printout the message:
	            if (!file1.exists() ) {
	            	JOptionPane.showMessageDialog(null, 
	            			"The calculation has not completed yet.  Please click the OK button to continue", 
							"MainPanel:MCSCF Energy", 
							JOptionPane.INFORMATION_MESSAGE); }
 
// concatenate temporary2 and temporary3 into Gradient_data
	            if (file1.exists()) {
	            	/*
	            	PlotExample plotstuff3 = new PlotExample(Env.getApplicationDataDir() + 
	          	       		Settings.fileSeparator + "Gradient_data");*/
	            	OnePlotExample plotstuff3 = new OnePlotExample(Env.getApplicationDataDir() + 
	          	       		Settings.fileSeparator + "Gradient_data");}



// coordinates (i.e. in each iteration)
	            try {
     	       FinalCoordParser pp1a = new FinalCoordParser
			   (new FinalCoordLexer(
			   		new FileReader(DataTree.getOutputFile())
			   ));
     	       Object result1a = pp1a.parse().value;
	            }
	            catch (Exception ie) {System.out.println("Exception in FinalCoordParser");}


// 11/13/2002: for the connectivity
	            Connect1 con = new Connect1(Env.getApplicationDataDir() + 
	      	       		Settings.fileSeparator + "connect.pdb");
// concatenate final.pdb  and connect into finalcoord.pdb
	            InputStream mylist1a1 = new FileInputStream(Env.getApplicationDataDir() + 
	             		Settings.fileSeparator + "final.pdb");
	            InputStream mylist2a1 = new FileInputStream(Env.getApplicationDataDir() + 
	      	       		Settings.fileSeparator + "connect");
	            SequenceInputStream str4a1 = new SequenceInputStream(mylist1a1, mylist2a1);
	            PrintStream temp4a1 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
	             		Settings.fileSeparator + "finalcoord.pdb"));
	            int ccc1a;
 
	            while ((ccc1a = str4a1.read()) != -1)
	            	temp4a1.write(ccc1a);

        }
 
// end of SCF energy

// this is for G1 method
        else if(record4.equals(record)) 
        {
        	G1Parser gp = new G1Parser(new G1Lexer(
        				new  FileReader(DataTree.getOutputFile())
            ));
        	Object result4 = gp.parse().value;

// concatenate temporary2 and temporary3 into Gradient_dataa
        	InputStream mylist1 = new FileInputStream(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "temporary2");
        	InputStream mylist2 = new FileInputStream(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "temporary3");
        	SequenceInputStream str4 = new SequenceInputStream(mylist1, mylist2);
        	PrintStream temp4 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "Gradient_dataa"));   
        	int ccc; 
        	while ((ccc = str4.read()) != -1)
        		temp4.write(ccc);
 
        	/*
			TwoPlotExample plotstuff1 = new TwoPlotExample(Env.getApplicationDataDir() + 
	  	       		Settings.fileSeparator + "Energy_data", Env.getApplicationDataDir() + 
	  	       		Settings.fileSeparator + "Gradient_dataa");
			*/
			OnePlotExample plotstuff1 = new OnePlotExample(Env.getApplicationDataDir() + 
	  	       		Settings.fileSeparator + "Energy_data");
			OnePlotExample plotstuff2 = new OnePlotExample(Env.getApplicationDataDir() + 
	  	       		Settings.fileSeparator + "Gradient_dataa");

// concatenate temporary5 and temporary6 into Gradient_datab
        	InputStream mylist1aa = new FileInputStream(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "temporary5");
        	InputStream mylist2aa = new FileInputStream(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "temporary6");
        	SequenceInputStream str4aa = new SequenceInputStream(mylist1aa, mylist2aa);
        	PrintStream temp4aa = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "Gradient_datab"));    
        	int ccca;
        	while ((ccca = str4aa.read()) != -1)
        		temp4aa.write(ccca);
        	/*
			TwoPlotExample plotstuff1 = new TwoPlotExample(Env.getApplicationDataDir() + 
	  	       		Settings.fileSeparator + "Energy_data", Env.getApplicationDataDir() + 
	  	       		Settings.fileSeparator + "Gradient_datab");
			*/
			plotstuff1 = new OnePlotExample(Env.getApplicationDataDir() + 
	  	       		Settings.fileSeparator + "Energy_data");
			plotstuff2 = new OnePlotExample(Env.getApplicationDataDir() + 
	  	       		Settings.fileSeparator + "Gradient_datab");

        }

//this is for CBS-Q 
        else if(record5.equals(record))
        {
        	System.out.println("before SCFaParser");
        	CBSQParser pp1 = new CBSQParser(new CBSQLexer(
        			new FileReader(DataTree.getOutputFile())
        	));
        	Object result1 = pp1.parse().value;
        	/*
        	PlotExample plotstuff3 = new PlotExample(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "Gradient_data");*/
        	OnePlotExample plotstuff3 = new OnePlotExample(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "Gradient_data");
        }
        /*
        else if(record6.equals(record))
        {
        	System.out.println("before SCFaParser");
        	B3PW91Parser pp1 = new B3PW91Parser(new B3PW91Lexer(
        			new FileReader(DataTree.getOutputFile())
        	));
        	Object result1 = pp1.parse().value;
        	//
        	//PlotExample plotstuff3 = new PlotExample(Env.getApplicationDataDir() + 
      	     //  		Settings.fileSeparator + "Gradient_data");
        	OnePlotExample plotstuff3 = new OnePlotExample(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "Gradient_data");
        }
  	    */
        else
        { System.out.println("this is for else statement");

        JOptionPane.showMessageDialog(null, 
        		"There is no data to plot.  Please click the OK button to continue", 
				"MainPanel:Gaussian Optimization", 
				JOptionPane.INFORMATION_MESSAGE);  
        
        }
  	} catch (Exception e) { 
  		System.err.println("DataTree:  Exception from PoundParser or Pound "+e);
  		return;
  	} 

//next bracket curl is for the end of if for Gaussian
  }


//  now for GAMESS:

  if (WhichProgram.format == "GAMESS") {


  	try { 


//First find the method
  		MethodParser pp = new MethodParser(
  				new MethodLexer(
  						new FileReader(DataTree.getOutputFile())
  				)); 
  		Object result = pp.parse().value;  
// then find the wavefunction
  		WavefunctionParser pp11 = new WavefunctionParser(
  				new WavefunctionLexer(
  						new FileReader(DataTree.getOutputFile())
  				));
  		Object result11 = pp11.parse().value;

// concatenate runtyp1  and runtype2 into runtype
  		InputStream mylist1a = new FileInputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "runtype1");
  		InputStream mylist2a = new FileInputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "runtype2");
  		SequenceInputStream str4a = new SequenceInputStream(mylist1a, mylist2a);
        PrintStream temp4a = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "runtype"));       
        int ccc1;
 
        while ((ccc1 = str4a.read()) != -1)
        	temp4a.write(ccc1);

// read the runtype file 
        FileInputStream fis = new FileInputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "runtype");
        DataInputStream dis = new DataInputStream(new BufferedInputStream(fis));
        String record = dis.readLine();
        System.out.println("From reading runtype:"+record); 

// now use the LexerParser based on the record
        String record1 = String.valueOf(new char [] { 'o', 'p', 't', 'i', 'm',
 'i', 'z', 'e', 'R', 'H', 'F'} );
        String record2 = String.valueOf(new char [] { 'e', 'n', 'e', 'r', 'g',
 'y', 'R', 'H', 'F'} );

        String record3 = String.valueOf(new char [] { 'e', 'n', 'e', 'r', 'g',
 'y', 'm', 'c', 's', 'c', 'f'} );

        String record4 = String.valueOf(new char [] { 'e', 'n', 'e', 'r', 'g',
 'y', 'g', 'v', 'b'} );


// for MCSCF energy

        if (record3.equals(record))
        {
        	System.out.println("IN THE GMCSCFaParser");
            GMCSCFaParser pp1 = new GMCSCFaParser
			(new GMCSCFaLexer(
					new FileReader(DataTree.getOutputFile())
			));
            Object result1 = pp1.parse().value; 
// check whether temporary* files exist
            File file1 = new File(Env.getApplicationDataDir() + 
      	       		Settings.fileSeparator + "Gradient_data");
        
//if the files don't exist, printout the message:
            if (!file1.exists() ) {
            	JOptionPane.showMessageDialog(null, 
            			"The calculation has not completed yet.  Please click the OK button to continue", 
						"MainPanel:MCSCF Energy", 
						JOptionPane.INFORMATION_MESSAGE); }
 
// concatenate temporary2 and temporary3 into Gradient_data
            if (file1.exists()) {
            	/*
            	PlotExample plotstuff3 = new PlotExample(Env.getApplicationDataDir() + 
          	       		Settings.fileSeparator + "Gradient_data");*/
            	OnePlotExample plotstuff3 = new OnePlotExample(Env.getApplicationDataDir() + 
          	       		Settings.fileSeparator + "Gradient_data");}



// coordinates (i.e. in each iteration)
            try {
            	GFinalCoordParser pp1a = new GFinalCoordParser
				(new GFinalCoordLexer(
						new FileReader(DataTree.getOutputFile())
            ));
            	Object result1a = pp1a.parse().value;
            }
            catch (Exception ie) {System.out.println("Exception in GFinalParser");}

// 11/13/2002: for the connectivity
         Connect con = new Connect(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "connect.pdb");
// concatenate final.pdb  and connect into finalcoord.pdb
       InputStream mylist1a1 = new FileInputStream(Env.getApplicationDataDir() + 
     		Settings.fileSeparator + "final.pdb");
        InputStream mylist2a1 = new FileInputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "connect");
     SequenceInputStream str4a1 = new SequenceInputStream(mylist1a1, mylist2a1);
        PrintStream temp4a1 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
         		Settings.fileSeparator + "finalcoord.pdb"));
      int ccc1a;
 
        while ((ccc1a = str4a1.read()) != -1)
           temp4a1.write(ccc1a);

 }
//end of MCSCF energy

// for GVB energy
        else if (record4.equals(record))
           {
             System.out.println("IN THE GVBParser");
            GVBParser pp1 = new GVBParser
          (new GVBLexer(
            new FileReader(DataTree.getOutputFile())
            ));
            Object result1 = pp1.parse().value;
// check whether temporary* files exist
        File file1 = new File(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "Gradient_data");
 
//if the files don't exist, printout the message:
        if (!file1.exists() ) {
	    JOptionPane.showMessageDialog(null, 
 "The calculation has not completed yet.  Please click the OK button to continue", 
					  "MainPanel: GVB Energy", 
					  JOptionPane.INFORMATION_MESSAGE); } 
// concatenate temporary2 and temporary3 into Gradient_data
       if (file1.exists()) {
        PlotExample plotstuff3 = new PlotExample(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "Gradient_data");}


// coordinates (i.e. in each iteration)
              try {
            GFinalCoordParser pp1a = new GFinalCoordParser
          (new GFinalCoordLexer(
            new FileReader(DataTree.getOutputFile())
            ));
        Object result1a = pp1a.parse().value;
       }
   catch (Exception ie) {System.out.println("Exception in GFinalParser");}

// 11/13/2002: for the connectivity
         Connect con = new Connect(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "connect.pdb");
// concatenate final.pdb  and connect into finalcoord.pdb
       InputStream mylist1a1 = new FileInputStream(Env.getApplicationDataDir() + 
     		Settings.fileSeparator + "final.pdb");
        InputStream mylist2a1 = new FileInputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "connect");
     SequenceInputStream str4a1 = new SequenceInputStream(mylist1a1, mylist2a1);
        PrintStream temp4a1 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
         		Settings.fileSeparator + "finalcoord.pdb"));
      int ccc1a;
 
        while ((ccc1a = str4a1.read()) != -1)
           temp4a1.write(ccc1a);

 }
//end of GVB

      else if(record1.equals(record))
           {    
             System.out.println("IN THE DFTParser");   
            DFTParser pp1 = new DFTParser
          (new DFTLexer(
            new FileReader(DataTree.getOutputFile())
            ));
        Object result1 = pp1.parse().value;
        
// check whether temporary* files exist
        File file1 = new File(Env.getApplicationDataDir() + 
	       		Settings.fileSeparator +"Energy_data");
         File file2 = new File(Env.getApplicationDataDir() + 
	       		Settings.fileSeparator +"temporary2");
        File file3 = new File(Env.getApplicationDataDir() + 
	       		Settings.fileSeparator +"temporary3");
        
//if the files don't exist, printout the message:
        if ( !file2.exists() || !file3.exists() || !file1.exists() ) {
	    JOptionPane.showMessageDialog(null, 
	            "The calculation has not completed yet.  Please click the OK button to continue", 
				  "MainPanel:GAMESS Optimization", 
				  JOptionPane.INFORMATION_MESSAGE); } 
// concatenate temporary2 and temporary3 into Gradient_data
       if (file2.exists()) {
         if (file3.exists()) {
           if (file1.exists()) {
       InputStream mylist1 = new FileInputStream(Env.getApplicationDataDir() + 
	       		Settings.fileSeparator + "temporary2");
        InputStream mylist2 = new FileInputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "temporary3");
        SequenceInputStream str4 = new SequenceInputStream(mylist1, mylist2);
        PrintStream temp4 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "Gradient_data"));        int ccc;
 
        while ((ccc = str4.read()) != -1)
           temp4.write(ccc);
        /*
		TwoPlotExample plotstuff1 = new TwoPlotExample(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "Energy_data", Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "Gradient_data");
		*/
		OnePlotExample plotstuff1 = new OnePlotExample(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "Energy_data");
		OnePlotExample plotstuff2 = new OnePlotExample(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "Gradient_data");
   }}}




// coordinates (i.e. in each iteration)
              try {
            GFinalCoordParser pp1a = new GFinalCoordParser
          (new GFinalCoordLexer(
            new FileReader(DataTree.getOutputFile())
            ));
        Object result1a = pp1a.parse().value;
       }
   catch (Exception ie) {System.out.println("Exception in GFinalParser");}

// 11/13/2002: for the connectivity
         Connect con = new Connect(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "connect.pdb");
// concatenate final.pdb  and connect into finalcoord.pdb
       InputStream mylist1a1 = new FileInputStream(Env.getApplicationDataDir() + 
     		Settings.fileSeparator + "final.pdb");
        InputStream mylist2a1 = new FileInputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "connect");
     SequenceInputStream str4a1 = new SequenceInputStream(mylist1a1, mylist2a1);
        PrintStream temp4a1 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
         		Settings.fileSeparator + "finalcoord.pdb"));
      int ccc1a;
 
        while ((ccc1a = str4a1.read()) != -1)
           temp4a1.write(ccc1a);


 } 

// for SCF energy

       if (record2.equals(record))
           {
             System.out.println("IN THE GSCFaParser");
            GSCFaParser pp1 = new GSCFaParser
          (new GSCFaLexer(
            new FileReader(DataTree.getOutputFile())
            ));
            Object result1 = pp1.parse().value; 
// check whether temporary* files exist
        File file1 = new File(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "Gradient_data");
        
//if the files don't exist, printout the message:
        if (!file1.exists() ) {
	    JOptionPane.showMessageDialog(null, 
 "The calculation has not completed yet.  Please click the OK button to continue", 
					  "MainPanel:MCSCF Energy", 
					  JOptionPane.INFORMATION_MESSAGE); }
 
// concatenate temporary2 and temporary3 into Gradient_data
       if (file1.exists()) {
    	/*
    	PlotExample plotstuff3 = new PlotExample(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "Gradient_data");*/
    	OnePlotExample plotstuff3 = new OnePlotExample(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "Gradient_data");}



// coordinates (i.e. in each iteration)
              try {
            GFinalCoordParser pp1a = new GFinalCoordParser
          (new GFinalCoordLexer(
            new FileReader(DataTree.getOutputFile())
            ));
        Object result1a = pp1a.parse().value;
       }
   catch (Exception ie) {System.out.println("Exception in GFinalParser");}

// 11/13/2002: for the connectivity
         Connect con = new Connect(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "connect.pdb");
// concatenate final.pdb  and connect into finalcoord.pdb
       InputStream mylist1a1 = new FileInputStream(Env.getApplicationDataDir() + 
     		Settings.fileSeparator + "final.pdb");
        InputStream mylist2a1 = new FileInputStream(Env.getApplicationDataDir() + 
  	       		Settings.fileSeparator + "connect");
     SequenceInputStream str4a1 = new SequenceInputStream(mylist1a1, mylist2a1);
        PrintStream temp4a1 = new PrintStream(new FileOutputStream(Env.getApplicationDataDir() + 
         		Settings.fileSeparator + "finalcoord.pdb"));
      int ccc1a;
 
        while ((ccc1a = str4a1.read()) != -1)
           temp4a1.write(ccc1a);

 }
//end of SCF energy


//else:  for handling cases when the parsing is unavailable
        else {
	    JOptionPane.showMessageDialog(null, 
 "There isn't any data to plot.  Please click the OK button to continue", 
					  "MainPanel:GAMESS Output Parsing", 
					  JOptionPane.INFORMATION_MESSAGE); } 

//       Object result = plotstuff.parse().value;
//       Object result = pp.parse().value;

      } catch (Exception e) { 
	System.err.println("DataTree:  Exception from PoundParser or Pound");
          }

//next curly bracket is for the end of if for gamess
}
      job.add(DataTree.nodeOnDeck);

    }//___________________________________________________________    

  }//end private void createRootlets







  public static String getInputFile() {
    return datafile;
  }

  /** drop the 3-character suffix from datafile and replace it with "out"
   */
  public static String getOutputFile() {
    return getNameNoSuffix() + "out";
  }

  /** drop the 3-character suffix from datafile
   */
  public static String getNameNoSuffix() {
    StringBuffer rev = new StringBuffer(datafile.trim()).reverse();
    StringBuffer trunc = new StringBuffer(rev.substring(3));
    StringBuffer forw = trunc.reverse();
    return forw.toString();
  }

  private static void initHelp() {
    String s = null;
    try {
//      s = "file:" 
//	+ System.getProperty("user.dir")
//	+ System.getProperty("file.separator")
//	+ "qcrjm2002logo.htm";
      s = "file:" + Env.getApplicationDataDir() 
	  	+ Settings.fileSeparator
    	+ "gridchemlogo.html";
      if (Settings.DEBUG) System.out.println("Help URL is " + s);
      DataTree.helpURL = new URL(s);
      //DataTree.displayURL(DataTree.helpURL); // show logo
    } catch (Exception eHelp) {
      System.err.println("DataTree:Couldn't create help URL: " + s);
    }
  }

  public static void displayURL(URL url) {
    try {
      DataTree.htmlPane.setPage(url);
    } catch (IOException eURL) {
      System.err.println("DataTree:displauURL:Attempted to read a bad URL: " + eURL);
    }
  }

  public static void main(String[] args) {
  	Settings settings = Settings.getInstance();
    JFrame frame = new DataTree();
	nanocadFrame2 nanWin = new nanocadFrame2();
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//    frame.addWindowListener(new WindowAdapter() {
//    	public void windowClosing(WindowEvent e) {
//    	  System.exit(0);
//    	}
//      });  
    frame.setVisible(true);
  }
}
