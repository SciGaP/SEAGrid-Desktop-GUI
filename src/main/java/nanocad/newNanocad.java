
 package nanocad; 
import nanocad.minimize.mm3.mm3MinimizeAlgorythm;   
import nanocad.minimize.uff.uffMinimizeAlgorythm;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.net.*;
import java.io.*;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.gridchem.client.SubmitJobsWindow;
import org.gridchem.client.stuffInside;
import org.gridchem.client.optsComponent;
import org.gridchem.client.common.Settings;
import org.gridchem.client.util.Env;
import org.gridchem.client.Invariants;

import G03Input.OptTable;
import G03Input.RouteClass;
import G03Input.showMolEditor;
import Gamess.gamessGUI.GamessGUI;

public class newNanocad extends Applet implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener  
{
    public static final String rcsid =
	"$Id: newNanocad.java,v 1.18 2007/11/16 20:11:32 srb Exp $";   // any significance of $id ?
    
    //data members
    LinkedList tosend = new LinkedList();
    private boolean seePTable = false;
    private GeneralAtom currentAtomType;
    private group grp, grp1;
    private String selectedItem = null;
	private String spaces = "                                "; // 32 spaces
    private atom atom1;
    private double atom1z;
    private boolean inDrawingArea, needToRepaint;
	private double geomValue;
    private int xxx, yyy;
    private int x0, y0;  // xxx,yyy tracks the mouse, x0,y0 stands still
    private boolean dragFlag = false, grpFlag = false, clearFlag =true,
		grpClearFlag = false, geometryFlag = false, undoGrpFlag = false, initFlag = false, newFF = true;
    private int mouseModifiers;
    public energyMinimizeAlgorythm energyMinimizer;
    public boolean isUFF = true;	// Tells us whether to use UFF or not
    private double currentPotential = 0.0;

    //lixh_4/27/05
    public static String applicationDataDir = Env.getApplicationDataDir();
    public static String fileSeparator = Env.separator(); 
    public static String txtDir = applicationDataDir + fileSeparator + "txt";
    public static String defaultFile;
    //
    public static String VMDPath;
	public static String VMDPathFileLoc;
    
	//numeric value for periodic table
    protected static int ptableHSize = 450;
    protected static int ptableHOffset = 25;
    protected static int ptableVOffset = 10;
    protected static int ptableVSize = 300;
    
    //group member
    private Vector atomtmp = new Vector();
    private Vector selectedAtomList = new Vector();
    private Vector undoSelectedAtomList = new Vector();
    private group chgGroup;
    private group tmpGroup;
    private group undoGroup;

    //GUI members
    private Choice emin;
    private Checkbox showForces, geometry, groupMode;
    private Button knownStructure, clear, undo, funcDel, funcRot, rotEnd, help, getPotential,addHydro;
    private Choice getSetStructure;
    //private Template t;
    public Template t; //lixh_3_4_05
    private drawPanel drawingArea;
    private Panel blabs;
    private Choice ffSelect;
    private Label atomInfoBlab;
    private Image offScreenImage;
    private Dimension offScreenSize;
    private Graphics offScreenGraphics;
    public Panel controls = new Panel();
    public Panel controls2 = new Panel();
    public Panel structure = new Panel();
    public display dis;
    private indianaWindow indiana;
    private defineWindow define;
    //text area
    private textwin saveWin;
    private textwin meswin;
    private savewin tosave;
    private textwin debugWindow;
    private textwin aboutWin;
    private Button about;
    private InstPanel instrucaba;
    private TextArea instrucs;
    private java.awt.List browser;
    java.awt.TextField tf1;
    // text field
    TextField CompoundName;
    TextField Formula;    
    private newNanocad nano;
 
    //parser for reloading structure
    private Parser par;
    private Vector atomV = new Vector();
    private Vector bondV = new Vector();
    private int fileBrowser;
    public int callDatabase;
    public int opensavedPDB;
    String msg = "";
    String val = " ";
    
    public static String exportedApplication = "";

    String pathvar = ".";
    String sepStr = java.io.File.separator;
    char sepChar = java.io.File.separatorChar;
    boolean runAsApplication = true;

    //copy right claim
    private String instructions =     
	
	"NanoCAD in Java, January 1999\n" +
	"Copyright (c) 1997,1998,1999 Will Ware, all rights reserved.\n" +
	"\n" +
	"Redistribution and use in source and binary forms, with or without\n" +
	"modification, are permitted provided that the following conditions\n" +
	"are met:\n" +
	"1. Redistributions of source code must retain the above copyright\n" +
	"   notice, this list of conditions and the following disclaimer.\n" +
	"2. Redistributions in binary form must reproduce the above copyright\n" +
	"   notice, this list of conditions and the following disclaimer in the\n" +
	"   documentation and other materials provided with the distribution.\n" +
	"\n" +
	"This software is provided \"as is\" and any express or implied warranties,\n" +
	"including, but not limited to, the implied warranties of merchantability\n" +
	"or fitness for any particular purpose are disclaimed. In no event shall\n" +
	"Will Ware be liable for any direct, indirect, incidental, special,\n" +
	"exemplary, or consequential damages (including, but not limited to,\n" +
	"procurement of substitute goods or services; loss of use, data, or\n" +
	"profits; or business interruption) however caused and on any theory of\n" +
	"liability, whether in contract, strict liability, or tort (including\n" +
	"negligence or otherwise) arising in any way out of the use of this\n" +
	"software, even if advised of the possibility of such damage.\n";


    /**
     *action handler
     */
    public boolean action (Event e, Object arg)
    {
	String s;
	if (e.target == emin)
    {
		switch(emin.getSelectedIndex())
		{
			case 1: // Conjugate gradient
				updateUndo();
				energyMinimize(true);
				emin.select(0);
				break;
			case 2: // Steepest descent
				updateUndo();
				energyMinimize(false);
				emin.select(0);
				break;
			default: break;
		}
		return true;
    }
	if (e.target == ffSelect)
    {
		switch(ffSelect.getSelectedIndex())
		{
			case 1: // UFF
				isUFF = true;
				newFF = true;
				break;
			case 2: // MM3
				isUFF = false;
				newFF = true;
				break;
			default: break;
		}
		return true;
    }
	else if (e.target == groupMode)  
    {
		grpFlag = groupMode.getState();
	   	if (!grpFlag)
		{
		    //end grp act
		    grpClearFlag = false;
			
		    chgGroup = new group(drawingArea);  

		    for (int i = 0; i < selectedAtomList.size(); i++)
			((atom)selectedAtomList.elementAt(i)).setSelected(false);
		    selectedAtomList.removeAllElements();
		}
    }
	else if (e.target == showForces)
    {
		grp.setShowForces (showForces.getState());
		repaint();
		return true;
    }
	else if (e.target == geometry)
    {
		geometryFlag = geometry.getState();
		drawingArea.setGeometryMode(geometryFlag);
		if (!geometryFlag)
		{   for (int i = 0; i < selectedAtomList.size(); i++)
			((atom)selectedAtomList.elementAt(i)).setSelected(false);
			repaint();
		}
		else selectedAtomList.removeAllElements();
    }
	else if (e.target == knownStructure)
	    {
		//lixh_3_4_05
		if (t == null)
		{
			//System.out.println("***test1*****");
		    t = new Template("Import Structure", this);
		    repaint();
		    energyMinimizer = null;
		    grp.setShowForces (showForces.getState());
		    //debugWindow.clear();
		    grp.setTextwin(debugWindow);
		
		    grp.updateViewSize ();
		
		    repaint ();
		    return true;
		}
		//
	        else
		    t.setVisible(true);
	    }
	else if (e.target == clear)
	    {
		//to get rid of the line at bottom
		atomInfo(spaces+spaces+spaces+spaces+spaces+spaces+spaces);
		newFF = true;
		if (grpFlag) 
		{
		    //grp delete
            //this is to keep a group from being selected if it is undone
		    for (int j = 0; j < grp.atomList.size(); j++)
		         ((atom)grp.atomList.elementAt(j)).setSelected(false);

		    updateUndo();
		    for (int j =0; j <grp.atomList.size();j++)
		    {    
			atom tmpAtom = (atom) grp.atomList.elementAt(j);
			if (belongTo(tmpAtom, selectedAtomList)) 
			{
			    grp.deleteAtom(tmpAtom);
			    j--;
			}
		    }
			    
		    selectedAtomList.removeAllElements();
		    drawingArea.clear();
		    grp.paint();
			
		    grpFlag = false;
		    e.target = groupMode;
		    groupMode.setState(false);
		}
		else
		{
		    drawingArea.clear();
		    clearFlag = true;
		    updateUndo();
		    grp.empty();
		    chgGroup.empty();
		    tmpGroup.empty();
		    selectedAtomList.removeAllElements();
		    grpFlag = false;	
		    return true;
		}
	    }
	else if (e.target == getPotential)
	{	getPotential();
		return true;
	}
	else if (e.target == addHydro)
	{
		addHydrogens();
		return true;
	}
	else if (e.target == undo)
	    {
		tmpGroup.copy(undoGroup);
		undoGroup.copy(grp);
		grp.copy(tmpGroup);

		grpFlag = false;

		groupMode.setState(grpFlag);
		e.target = groupMode;
		repaint();
		
		if (grp.atomList.size() == 0)
		    clearFlag = true;
		else
		    clearFlag = false;
	    }
	else if (e.target == getSetStructure)
	    {
		//int i = 0;
		if(stuffInside.selectedGUI==1)
    	{   // G03InputGUI selected.. redirect to gaussian out default
    		System.out.println("gaussian GUI! used  defaulting to Gaussian!: "+stuffInside.selectedGUI);
    		//getSetStructure.select(6);
    	}
		
		switch (getSetStructure.getSelectedIndex())
		{
		default:		break;
		
		case 11: //View with VMD
			saveFile("vmd.pdb");
			new VMDClass(1);
			getSetStructure.select(0);
			break;
        case 10: // View PDB;
        	if(clearFlag) break; 

        	saveWin = new textwin("PDB file as text", "", false);
        	saveWin.setVisible(true);
        	saveWin.setText(grp.getPDB(true));  //to obtain default PDB
        	getSetStructure.select(0);
        	break;
		  		  
		case 4: // View XYZ;
			if(clearFlag) break;

			saveWin = new textwin("XYZ file as text","", false);
			saveWin.setVisible(true);
			saveWin.setText(grp.getXYZ());
			getSetStructure.select(0);
			break;
			
		case 5: // View Native Format
			if (clearFlag) break;

            saveWin = new textwin("Native Format as text","", false);
            saveWin.setVisible(true);
            saveWin.setText(grp.getNativeFormat());
			getSetStructure.select(0);
            break;

                    
		/*case 2: //Export files to Waltz server; 
			if(clearFlag) break; 
			
			//saveWin = new textwin("Export to Waltz","", false);
        		//saveWin.setVisible(true);

			String waltz;		
			if (!runAsApplication) waltz = getParameter("waltzurl");
			
			//saveWin.setText("User is "+getParameter("username")+"\n"+"File will be saved to:"+ waltz +"\n"+"the content is :\n"+grp.getPDB(false)); 
			if (!runAsApplication)
			{   sendFile("http://pine.ncsa.uiuc.edu/cgi-bin/savewaltzpdb.cgi",
				grp.getPDB(true), "waltzsim.pdb");
			    waltz = getParameter("waltzurl");
			    callWaltz(e,waltz);
			}
			else
			{   saveFile("./waltzsim.pdb");
			    atomInfo("Waltz has loaded and will begin upon closing Nanocad.");
			    try
			    {	Runtime.getRuntime().exec("java -jar offwaltz.jar");	}
			    catch (java.io.IOException excep)
			    {   System.err.println( excep.toString());	}
			      catch (java.lang.SecurityException exceps)
			    {   System.err.println( exceps.toString());	}
			}
			System.exit(0);
			break;
		 */
            
		case 6: // Create a Gaussian Template close and go back to edit mode //lixh_3_4
			
			if(clearFlag) 
				break; 
			if(optsComponent.selectedFrontPanel==1){
				//confirmation to save pdb file or not
				int result = JOptionPane.showConfirmDialog(
	                    this,
	                    "This will open the Gaussian GUI, " +
	                    "and the current molecule will be exported there.\n" +
	                    "                         Do you want to proceed?",
	                    "Confirmation Message",
	                    JOptionPane.YES_NO_OPTION
	                    );
				//if yes (save as pdb file and exit nanocad)
				if (result == 0){
					stuffInside.selectedGUI=1;
					
					RouteClass.keyIndex=0;
	    	        RouteClass.initCount=0;
	    	        OptTable.optC=0;
	    	    	
	    	        //Call the new Gaussian GUI.    
					stuffInside.showNewGUI();
					showMolEditor forString=new showMolEditor();
					
					String gaussOut = GaussianOutput(grp.getXYZ());
					forString.tempmol=gaussOut;
					JOptionPane.showMessageDialog(null, "WARNING: Molecule information" +
							" has been exported correctly. Make sure\n" +
							"to edit other sections of GUI.",
							      "GridChem: Gaussian GUI",
							      JOptionPane.WARNING_MESSAGE);
					// write to a file now
	            	boolean append = false;
	            	try
					{
	            		File f = new File(applicationDataDir+fileSeparator
	            				+ "tmp.txt");
	            		FileWriter fw = new FileWriter(f, append);
	            		fw.write(gaussOut);
	            		System.err.println("gaussOut = ");
	            		System.err.println(gaussOut);
	            		fw.close();

	                    exportedApplication = Invariants.APP_NAME_GAUSSIAN;
					}
	            	catch (IOException ioe)
					{
	            		System.err.println("newNanocad:output Gaussian:" +
							"IOException");
	            		System.err.println(ioe.toString());
	            		ioe.printStackTrace();
					}
	    	    	
				if ( t != null){
            		t.setVisible(false);
            	}
            	this.setVisible(false);
            	optsComponent.selectedFrontPanel=0;
            	break;
				}else{
					getSetStructure.select(0);
					optsComponent.selectedFrontPanel=1;
					break;	
				}
				
			}
			else{
                // runAsApplication 
            	System.out.println(" Case 6 Gaussian Input Template being Generated");
		        // 
                // call editjobpaneltextwindow and add text to display
            	// RHB: change the text on the editJobPanel!!!
            	// This should not take too terribly long to do now that
            	// I have figured out where it goes
            	String gaussOut = GaussianOutput(grp.getXYZ());
            	// write to a file now
            	boolean append = false;
            	try
				{
            		File f = new File(applicationDataDir+fileSeparator
            				+ "tmp.txt");
            		FileWriter fw = new FileWriter(f, append);
            		fw.write(gaussOut);
            		System.err.println("gaussOut = ");
            		System.err.println(gaussOut);
            		fw.close();

                    exportedApplication = Invariants.APP_NAME_GAUSSIAN;
				}
            	catch (IOException ioe)
				{
            		System.err.println("newNanocad:output Gaussian:" +
						"IOException");
            		System.err.println(ioe.toString());
            		ioe.printStackTrace();
				}
            	
                // close molecular editor
            	// close the structure panel 
            	if ( t != null){
            		t.setVisible(false);
            	}
            	this.setVisible(false);
                break;
                
            }

		case 7: // Create a Gamess Input Template close and go back to edit mode //lixh_3_4
			if(clearFlag) 
				break; 
			if(optsComponent.selectedFrontPanel==1){
				//confirmation to save pdb file or not
				int result = JOptionPane.showConfirmDialog(
	                    this,
	                    "This will open the GAMESS GUI, " +
	                    "and the current molecule will be exported there.\n" +
	                    "                         Do you want to proceed?",
	                    "Confirmation Message",
	                    JOptionPane.YES_NO_OPTION
	                    );
				//if yes (save as pdb file and exit nanocad)
				if (result == 0)
				{
					if (!runAsApplication)
					{
						tosave = new savewin("Save file", "", false, this);
						tosave.setVisible(true);
						String tosavefile = null;
						String fileName = new String();
					}
					else
					{
						stuffInside.selectedGUI = 1;
		    			//Open the Gamess and export the molecule to it
						String gamessOut = GamessOutput(grp.getXYZ());
		                // write to a file now
		                boolean append = false;
		                try
						{
		                	File f = new File(applicationDataDir+fileSeparator
		                			+ "tmp.txt");
		                	FileWriter fw = new FileWriter(f, append);
		                	fw.write(gamessOut);
		                	System.err.println("gamessOut = ");
		                	System.err.println(gamessOut);
		                	fw.close();

		                    exportedApplication = Invariants.APP_NAME_GAMESS;
		                    GamessGUI.main(null);
		                    GamessGUI.molSpec.nanoCadHandler.nanWin = new nanocadFrame2();
		                    GamessGUI.molSpec.nanoCadHandler.nanWin.nano = this;
		                    GamessGUI.molSpec.nanoCadHandler.componentHidden(null);
						}
		                catch (IOException ioe)
						{
		                	System.err.println("newNanocad:output Gamess:" +
								"IOException");
		                	System.err.println(ioe.toString());
		                	ioe.printStackTrace();
						}
					}
					if ( t != null){
						t.setVisible(false);
            		}
            		this.setVisible(false);
            		optsComponent.selectedFrontPanel=0;
            		break;
				}else{
					getSetStructure.select(0);
					optsComponent.selectedFrontPanel=1;
					break;	
				}
				
			}
			else{
			if (clearFlag == true) break;
            else
            {
            
            	// runAsApplication 
                System.out.println(" Case 7 Gamess Input Template being Generated");
                // 
                // call editjobpaneltextwindow and add text to display
                // RHB: change the text on the editJobPanel!!!
                // This should not take too terribly long to do now that
                // I have figured out where it goes
                String gamessOut = GamessOutput(grp.getXYZ());
                // write to a file now
                boolean append = false;
                try
				{
                	File f = new File(applicationDataDir+fileSeparator
                			+ "tmp.txt");
                	FileWriter fw = new FileWriter(f, append);
                	fw.write(gamessOut);
                	System.err.println("gamessOut = ");
                	System.err.println(gamessOut);
                	fw.close();

                    exportedApplication = Invariants.APP_NAME_GAMESS;
				}
                catch (IOException ioe)
				{
                	System.err.println("newNanocad:output Gamess:" +
						"IOException");
                	System.err.println(ioe.toString());
                	ioe.printStackTrace();
				}

                // close molecular editor
//              close the structure panel 
            	if ( t != null){
            		t.setVisible(false);
            	}
                this.setVisible(false);
                break;
            }
		}
		case 8: // Create a NWchem Input Template close and go back to edit mode //lixh_3_4
			if(clearFlag) 
				break; 
			if(optsComponent.selectedFrontPanel==1){
				//confirmation to save pdb file or not
				int result = JOptionPane.showConfirmDialog(
	                    this,
	                    "Exporting to NWChem is not possible from here. Go to Nanocad through Submit Jobs to use this option.\n" +
	                    "                  Do you want to save current Nanocad workspace as a PDB file and close Nanocad?",
	                    "Confirmation Message",
	                    JOptionPane.YES_NO_OPTION
	                    );
				//if yes (save as pdb file and exit nanocad)
				if (result == 0){
				if (!runAsApplication)
				{
					tosave = new savewin("Save file", "", false, this);
					tosave.setVisible(true);
					String tosavefile = null;
					String fileName = new String();
				}
				else
				{
		    		JFileChooser chooser = new JFileChooser();
		    		chooser.setDialogTitle("Save as pdb file");
		    		int retVal = chooser.showSaveDialog(null);
		    		if (retVal == JFileChooser.APPROVE_OPTION)
		    		{
						String filename = chooser.getSelectedFile().getAbsolutePath();
						if (filename != null)
					    {
					    	if (filename.indexOf(".pdb") == -1) filename = filename + ".pdb";
					    	getSetStructure.select(0);
					    	chooser.setSelectedFile(new File(filename));
					    	saveFile(filename);
					    }
						
		    		}
		    		
					}
				if ( t != null){
            		t.setVisible(false);
            	}
            	this.setVisible(false);
            	optsComponent.selectedFrontPanel=0;
            	break;
				}else{
					getSetStructure.select(0);
					optsComponent.selectedFrontPanel=1;
					break;	
				}
				
			}	
		else{
			if (clearFlag == true) break;
            else
            {
            	// runAsApplication 
                System.out.println(" Case 8 NWchem Input Template being Generated");
                // 
                // call editjobpaneltextwindow and add text to display
                // RHB: change the text on the editJobPanel!!!
                // This should not take too terribly long to do now that
                // I have figured out where it goes
                String nwchemOut = NWchemOutput(grp.getXYZ());
                // write to a file now
                boolean append = false;
                try
				{
                	File f = new File(applicationDataDir+fileSeparator
                			+ "tmp.txt");
                	FileWriter fw = new FileWriter(f, append);
                	fw.write(nwchemOut);
                	System.err.println("nwchemOut = ");
                	System.err.println(nwchemOut);
                	fw.close();

                    exportedApplication = Invariants.APP_NAME_NWCHEM;
				}
                catch (IOException ioe)
				{
                	System.err.println("newNanocad:output Gamess:" +
						"IOException");
                	System.err.println(ioe.toString());
                	ioe.printStackTrace();
				}

                // close molecular editor
//              close the structure panel 
            	if ( t != null){
            		t.setVisible(false);
            	}
                this.setVisible(false);
                break;
            }
		}
		case 9: // Create a Molpro Input Template close and go back to edit mode //lixh_3_4
			if(clearFlag) 
				break; 
			if(optsComponent.selectedFrontPanel==1){
				//confirmation to save pdb file or not
				int result = JOptionPane.showConfirmDialog(
	                    this,
	                    "Exporting to Molpro is not possible from here. Go to Nanocad through Submit Jobs to use this option.\n" +
	                    "                  Do you want to save current Nanocad workspace as a PDB file and close Nanocad?",
	                    "Confirmation Message",
	                    JOptionPane.YES_NO_OPTION
	                    );
				//if yes (save as pdb file and exit nanocad)
				if (result == 0){
				if (!runAsApplication)
				{
					tosave = new savewin("Save file", "", false, this);
					tosave.setVisible(true);
					String tosavefile = null;
					String fileName = new String();
				}
				else
				{
		    		JFileChooser chooser = new JFileChooser();
		    		chooser.setDialogTitle("Save as pdb file");
		    		int retVal = chooser.showSaveDialog(null);
		    		if (retVal == JFileChooser.APPROVE_OPTION)
		    		{
						String filename = chooser.getSelectedFile().getAbsolutePath();
						if (filename != null)
					    {
					    	if (filename.indexOf(".pdb") == -1) filename = filename + ".pdb";
					    	getSetStructure.select(0);
					    	chooser.setSelectedFile(new File(filename));
					    	saveFile(filename);
					    }
						
		    		}
		    		
					}
				if ( t != null){
            		t.setVisible(false);
            	}
            	this.setVisible(false);
            	optsComponent.selectedFrontPanel=0;
            	break;
				}else{
					getSetStructure.select(0);
					optsComponent.selectedFrontPanel=1;
					break;	
				}
				
			}
			else{
			if (clearFlag == true) break;
            else
            {
            	// runAsApplication 
                System.out.println(" Case 9 Molpro Input Template being Generated");
                // 
                // call editjobpaneltextwindow and add text to display
                // RHB: change the text on the editJobPanel!!!
                // This should not take too terribly long to do now that
                // I have figured out where it goes
                String molproOut = MolproOutput(grp.getXYZ());
                // write to a file now
                boolean append = false;
                try
				{
                	File f = new File(applicationDataDir+fileSeparator
                			+ "tmp.txt");
                	FileWriter fw = new FileWriter(f, append);
                	fw.write(molproOut);
                	System.err.println("molproOut = ");
                	System.err.println(molproOut);
                	fw.close();
                    
                    exportedApplication = Invariants.APP_NAME_MOLPRO;
                    
				}
                
                catch (IOException ioe)
				{
                	System.err.println("newNanocad:output Gamess:" +
						"IOException");
                	System.err.println(ioe.toString());
                	ioe.printStackTrace();
				}

                // close molecular editor
//              close the structure panel 
            	if ( t != null){
            		t.setVisible(false);
            	}
                this.setVisible(false);
                break;
            }
		}
		// lixh_4/27/05
		case 1: //open saved PDB/MOL2
			if (clearFlag)
				break;
			else 
			{
	    		JFileChooser chooser = new JFileChooser();
	            chooser.addChoosableFileFilter(new PDBorMOL2FileFilter());
	            chooser.setDialogTitle("Open pdb/mol2 file");
	    		int retVal = chooser.showOpenDialog(null);
	    		if (retVal == JFileChooser.APPROVE_OPTION)
	    		{
					String filename = chooser.getSelectedFile().getAbsolutePath();
					if (filename.endsWith("pdb") || filename.endsWith("mol2") )
					{
						loadFile(filename,"");	
					}
	    		}
	    		break;
				/*
				//runAsApplication
				FileDialog f = new FileDialog((JFrame)this.getParent(),"Open PDB/MOL2 File", FileDialog.LOAD);
                
				f.setFile("*.pdb;*.mol2");
				f.setVisible(true);
				 
				String filename = f.getFile();
				String dirname = f.getDirectory();
				if (filename.endsWith("pdb") || filename.endsWith("mol2") )
				{
					loadFile(filename,dirname);	
				}
				break;
				*/
			}
		//	
			
		case 2: //Saving files; 
			if(clearFlag) 
				break; 
			else if (!runAsApplication)
			{
				tosave = new savewin("Save file", "", false, this);
				tosave.setVisible(true);
				String tosavefile = null;
				String fileName = new String();
			}
			else
			{
	    		JFileChooser chooser = new JFileChooser();
	    		chooser.setDialogTitle("Save as pdb file");
	    		int retVal = chooser.showSaveDialog(null);
	    		if (retVal == JFileChooser.APPROVE_OPTION)
	    		{
					String filename = chooser.getSelectedFile().getAbsolutePath();
					if (filename != null)
				    {
				    	if (filename.indexOf(".pdb") == -1) filename = filename + ".pdb";
				    	getSetStructure.select(0);
				    	chooser.setSelectedFile(new File(filename));
				    	saveFile(filename);
				    }
					
	    		}
	    		
				/*
			    FileDialog f = new FileDialog((Frame)this.getParent(), "Save PDB File", FileDialog.SAVE);
			    f.setVisible(true);
			    String filename = f.getFile();
			    String dirname = f.getDirectory();
			    if (filename != null)
			    {
			    	if (filename.indexOf(".pdb") == -1)
			    		filename = filename + ".pdb";
			    	getSetStructure.select(0);
			    	saveFile(dirname+filename);
			    }
			    */
			}
			getSetStructure.select(0);
			break;

		 case 3: //View with chime
                      if(clearFlag) break; 

                      else if (!runAsApplication)
		      {
			String urlis= null;
                	try {
			    urlis = "http://chemviz.ncsa.uiuc.edu/cgi-bin/chimepdb.cgi";

			    sendFile(urlis, grp.getPDB(true), "chimepdb.cgi");
			    String output_url ="http://pine.ncsa.uiuc.edu/csd/temp/"+getParameter("username")+"/chime.pdb";

			    callWaltz(e,output_url);
                        }catch(Exception e1) 
                     	    { System.err.println(e1); }
                      }
		      else
		      {
		    	  String URL = applicationDataDir+sepStr+ "chimeout.pdb";
		    	  saveFile(URL);
		
			try
			{
			    
		    //Runtime.getRuntime().exec(new String[] {"C:\\Program Files\\Internet Explorer\\IEXPLORE", URL});
		    
		    openURL(URL);
			}
			//catch (java.io.IOException excep)
			  //  {   System.err.println( excep.toString() );    }
			catch (java.lang.SecurityException exceps)
			    {   System.err.println( exceps.toString() );    }
		      }
		      break; 		      
		     			
		}
		return true;
	    }
	else if (e.target instanceof TextField) 
	{
		repaint();
		return true;  
	}
	else if (e.target == help)
	{

		if(!runAsApplication)
		{
		    try
		    {
			URL helpURL = new URL("http://pine.ncsa.uiuc.edu/nanocad/NanocadHelp.html");
			getAppletContext().showDocument(helpURL, "_blank");
		    }
		    catch (Exception roar)
		    {    System.err.println(roar);	}

		    return true;
		}
		else
		{
			//lixh_4/30/05
			/*
		    try
		    {
			String osName = System.getProperty("os.name" ); 
			if (osName.equals("Windows NT")||osName.equals("Windows 2000")||osName.equals("Windows XP"))
			    Runtime.getRuntime().exec(new String[] {"cmd /w /q start",
				pathvar+sepStr+"NanocadHelp.html"});
			else Runtime.getRuntime().exec(new String[] {"start", pathvar+sepStr+"NanocadHelp.html"});
		    
		    }
		    catch (java.io.IOException excep)
			{   System.out.println( excep.toString() );    }
		    catch (java.lang.SecurityException exceps)
			{   System.out.println( exceps.toString() );    }
			*/
			
		    try
		    {
			String osName = System.getProperty("os.name" ); 
			if (osName.equals("Windows NT")||osName.equals("Windows 2000")||osName.equals("Windows XP"))
			    Runtime.getRuntime().exec(new String[] {"\"C:\\Program Files\\Internet Explorer\\iexplore.exe\"",
			    		"http://chemviz.ncsa.uiuc.edu/content/doc-nanocad.html"});
			else Runtime.getRuntime().exec(new String[] {"start", pathvar+sepStr+"NanocadHelp.html"});
		    
		    }
		    catch (java.io.IOException excep)
			{   System.out.println( excep.toString() );    }
		    catch (java.lang.SecurityException exceps)
			{   System.out.println( exceps.toString() );    }
			/*
			JDialog helpDialog = new JDialog();
			JEditorPane editorPane = new JEditorPane();
			editorPane.setEditable(false);
		    try
		    {
		    	URL url = new URL("http://chemviz.ncsa.uiuc.edu/content/doc-nanocad.html");
				editorPane.setPage(url);
				helpDialog.add(editorPane);
				helpDialog.setSize(800,800);
				helpDialog.setVisible(true);
		    }
		    catch (Exception roar)
		    {    System.err.println(roar);	}
		    */
		}
	}
	else if (e.target == about)
	{
	    aboutWin = new textwin("About Nanocad", "", false);
	    aboutWin.setVisible(true);
	    aboutWin.setText(instructions);
	    return true;
	}
	return false;	
    }
   
    /**
     *redirect to waltz 
     */
    public void callWaltz(Event event, String redirUrl){
	URL newUrl = null;
	try{
	    newUrl = new URL(redirUrl);
	    getAppletContext().showDocument(newUrl);
	}catch(Exception e){
	    System.err.println(e);
	}
    }

    public void getFilesInDirectory(java.awt.List filelist, String directory)
    {
	if (runAsApplication && (directory.charAt(directory.length()-1) != sepChar))
		directory = directory + sepStr;
	else if (directory.charAt(directory.length()-1) != '/')
		directory = directory + "/";
	String encodedDirName = URLEncoder.encode(directory);  

	try
	{
	    URL url = new URL("http://chemviz.ncsa.uiuc.edu/cgi-bin/openpdbfromdir.cgi");
                              
            URLConnection conn = url.openConnection();
	    conn.setDoOutput(true);
	    PrintWriter out = new PrintWriter(conn.getOutputStream());
	    out.println(directory);
	    out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while((line = in.readLine())!= null)
	    {
                //System.out.println("stretch Line is "+line);
               	filelist.add(line);
	    }
            in.close();
        }catch(IOException e1){}
    }
 
    /**
     *reload new structure
     *Notice: if "Clear" is pressed, then it will be a completely new 
     *structure, otherwise, it will be a combined one for functional group use
     */
    public class VMDClass {
        Timer timer;

        public VMDClass(int milliseconds) {
            timer = new Timer();
            timer.schedule(new RemindTask(), milliseconds*1);
    	}

        class RemindTask extends TimerTask {
            public void run() {
            	viewVMDJob();
                System.out.println("Time's up!");
                timer.cancel(); //Terminate the timer thread
            }
        }
        
        
    }
    public void viewVMDJob(){
      	String VMD="";
      	String VMD_mac_linux="";
      	VMDPathFileLoc = Env.getApplicationDataDir()+File.separator+"VMDPathFile.inp";
      	System.out.println("VMDpathfile: "+VMDPathFileLoc);
      	File VMDF = new File(VMDPathFileLoc);
          try {
              
      	if (!VMDF.exists())
          {
          	int result = JOptionPane.showConfirmDialog(
  		  			null,
  		  			"It seems GridChem Client does not know location of VMD binary file on your machine.\n"
  		  			+ "Pressing 'OK' button will open a File Browser. Take browser to VMD directory.\n"+
  		  			"Select VMD executable (binary file) and then press open button.\n"+
  		  			"If you dont have VMD program, download and install it first.\n"+ 
  		  			"You can get it from 'http://www.ks.uiuc.edu/Research/vmd/'",
  		  			"VMD Path Locator",
  		  			JOptionPane.OK_CANCEL_OPTION,
  		  			JOptionPane.QUESTION_MESSAGE);
          	if (result==0)
          	{
          		saveVMDPath();
          		System.out.println("pressed OK");
          	}
          	if (VMDF.exists())
          	{
          		FileReader VMDfile = new FileReader(VMDF);
          	       
              	BufferedReader br = new BufferedReader(VMDfile);
              	VMD=br.readLine();
              	VMD_mac_linux=VMD;
          	}
          }else {
          	FileReader VMDfile = new FileReader(VMDF);
         
          	BufferedReader br = new BufferedReader(VMDfile);
          	VMD=br.readLine();
          	VMD_mac_linux=VMD;
          }}catch (IOException ie){
          	System.err.println("error in reading molden location file");
          }
          /*
      	String time = new SimpleDateFormat("yyMMdd").format(job.getCreated());
          
          Settings.jobDir = Settings.defaultDirStr + File.separator + 
                  job.getResearchProjectName() + File.separator + 
                  job.getName() + "." + job.getSubmitMachine() + "." + 
                  job.getLocalJobID() + "." + time;
          jobName = job.getName();
          dataFileName = Settings.jobDir + File.separator + job.getName()+".out";
          String dataFileString = '"'+dataFileName+'"';
          File dataFile = new File(dataFileName);
          */
          //String molden = Env.getApplicationDataDir() + File.separator + "molden"+File.separator+"molden.exe";
      	//String molden_mac_linux = Env.getApplicationDataDir() + File.separator + "molden"+File.separator+"molden";
          
          String VMDString = '"'+VMD+'"';
      	  File VMDfile =new File(VMD);
          //if(dataFile.exists()){
          	if(VMDfile.exists()){
          		String osName = System.getProperty("os.name");
                  try {

                      if (osName.startsWith("Windows")) { 
                        
                    	//String windowsCommandStr = "cmd.exe /c "+VMDString + " " + dataFileString;
                    	String windowsCommandStr = "cmd.exe /c "+VMDString + " vmd.pdb";
                    	//System.out.println("datafilestring:  "+dataFileString);
                        //Runtime.getRuntime().exec("cmd.exe /c copy "+dataFileString+" vmd.out");
                        
                        //Runtime.getRuntime().exec("cmd.exe /c cd "+moldenFolder);
                        System.out.println("Windows command str: "+windowsCommandStr);
                        InputStream in = Runtime.getRuntime().exec(windowsCommandStr).getInputStream();
        			   
        			    StringBuffer sb = new StringBuffer();
        			    //System.out.println("Executed ipconfig on windows");
        	            
        	            int c;
        	            while ((c = in.read()) != -1) 
        	            	{
        	            	sb.append((char) c);
        	            	}
        				
        	              String comOutput = sb.toString();
        	                        
        	              System.out.println("value of output form command prompt after running molden is: "+ comOutput);
        	              /*
        	              if (comOutput.equals("")){
        	            	JOptionPane.showMessageDialog(null, "Perhaps X-server (e.g. Exceed) on this machine is not running.\n"+
        	            			"If this is the case, please go ahead and start it first and then try again.",
        	            			"X Server Problem!!", JOptionPane.WARNING_MESSAGE);
        	              }
        	              */
                        //Runtime.getRuntime().exec(windowsCommandStr);
                       } else if (osName.startsWith("Mac")) {
//                      	copying output file to a file with common name molden.out. This file is deleted when client is closed.
                      	 //Runtime.getRuntime().exec("cp "+dataFileName+" ./molden.out");
                      	 //exporting display for bash shell, for running xterm
                      	 Runtime.getRuntime().exec("export DISPLAY=:0.0");
                      	 
                      	 //launching molden output file using molden binary executable
                      	 //System.out.println("/usr/X11R6/bin/xterm -e "+ VMD_mac_linux+" "+dataFileName);
                      	 InputStream in = Runtime.getRuntime().exec("/usr/X11R6/bin/xterm -e "+ VMD_mac_linux+" vmd.pdb").getInputStream();
              			 
                      	 //following peice of code prompts user to run x-server before launching molden
             			    StringBuffer sb = new StringBuffer();
             			    //System.out.println("Executed ipconfig on windows");
             	            
             	            int c;
             	            while ((c = in.read()) != -1) 
             	            	{
             	            	sb.append((char) c);
             	            	}
             				
             	              String comOutput = sb.toString();
             	                        
             	              System.out.println("value of output form command prompt after running molden is: "+ comOutput);
             	              /*
             	              if (comOutput.equals("")){
             	            	JOptionPane.showMessageDialog(null, "Perhaps X-server (e.g. Exceed) on this machine is not running.\n"+
             	            			"If this is the case, please go ahead and start it first and then try again.",
             	            			"X Server Problem!!", JOptionPane.WARNING_MESSAGE);
             	              }
             	              */
                       
                       } else { 
                      	//Runtime.getRuntime().exec("cp "+dataFileName+" ./molden.out");
                     	//System.out.println("cp "+dataFileName+" "+dataFileName);
                      	Runtime.getRuntime().exec("xterm -e "+ VMD_mac_linux+" vmd.pdb");
                     	System.out.println("xterm -e "+ VMD_mac_linux+" vmd.pdb");
                      }

                  } catch (Exception e) {
                  	System.out.println("problem in running VMD");
                  }
          	}else {
          		JOptionPane.showMessageDialog(null, "Client cannot find VMD binary file on this machine\n"+
          				"It seems recently you have deleted or changed location of molden binary file.\n"+
          				"Please first varify it and then come back.",
          			    "Problem reading file", JOptionPane.WARNING_MESSAGE);
          		VMDF.delete();
          		//System.out.println("Program Molden does not exist on this machine");
          	}
          /*
          }else{
          	try{
          	System.out.println("Ouput File does not exist on local machine hence downloading it...");
          	int result = JOptionPane.showConfirmDialog(
  		  			null,
  		  			"Ouput File does not exist on local machine.\n"
  		  			+ "Kindly press 'OK' to download from server first. Downloading file takes a while. \n"+
  		  			"Remember if you press 'Cancel' button, this option wont do anything.\n", 
  		  			"File Download Confirm",
  		  			JOptionPane.OK_CANCEL_OPTION,
  		  			JOptionPane.QUESTION_MESSAGE);
          	if (result==0){
          	doMolden=1;
          	GETOUTPUTCommand getOutputCommand = new GETOUTPUTCommand(gms,this);
              getOutputCommand.getArguments().put("job", job);
              statusChanged(new StatusEvent(getOutputCommand,Status.START));
              System.out.println("Downloaded output file. Launching VMD");
          	}
          	}catch (Exception ig){
          		System.err.println("error in thread sleep");
          	}
         }
         */
       }
      public void saveVMDPath()
      {
  		JFileChooser chooser = new JFileChooser();
  		
  		int result = chooser.showOpenDialog(this);
  		String inp = new String();
  		if (result == JFileChooser.CANCEL_OPTION) return;
  		try
  		{
  		   File file = chooser.getSelectedFile();
  		   VMDPath = file.getAbsolutePath();
  		   System.out.println("this is path of VMD file: "+VMDPath);
  		   
  		   FileWriter VMDfile = new FileWriter(VMDPathFileLoc);
  		   VMDfile.write(VMDPath);
  		   VMDfile.close();
  		    		    
  		}catch(Exception e)
  		{
  		    JOptionPane.showMessageDialog(null, "Reading error",
  			    "Problem reading file", JOptionPane.INFORMATION_MESSAGE);
  		}

      }
    public void saveFile(String filename)
    {
	File toSave = new File(filename);
	try
	{
	    PrintWriter out1 = new PrintWriter((OutputStream)new FileOutputStream(toSave));
	    out1.println(grp.getPDB(true));
	    out1.close();
	
	}
	catch (java.io.IOException f)
	{  System.out.println("You can't see me");  }
    }	

    public void loadFile(String filename, String directory)
    {
	if (!runAsApplication)
	{   if ((!directory.equals(""))&&(directory.charAt(directory.length()-1) != '/'))
		directory = directory + "/";
	}
	else
	{   if ((!directory.equals(""))&&(directory.charAt(directory.length()-1) != sepChar))
		directory = directory + sepStr;
	}
	int pos = filename.indexOf("pdb");
	String tryfile, data;
		
	if( (pos = filename.indexOf("pdb")) != -1)
	{
	    //System.out.println("pdb");
	    //System.out.println(directory+filename);
	    String ext = filename.substring(pos, filename.length());
	    data = receiveFile(filename, ext, directory);
	    //drawFile(data,ext);
	    drawFile(directory+filename,data,ext);
	} 

	else if ( (pos = filename.indexOf("mol2")) != -1)
	{	
	    //System.out.println("mol2");
	    //System.out.println(directory+filename);
	    String ext = filename.substring(pos, filename.length());
	    data = receiveFile(filename, ext, directory);
	    drawFile(directory+filename,data, ext);
	}
	else
	{
	    pos = (filename.length() + 1);
	    tryfile = new String(filename + ".pdb");
	    //System.out.println("try1:" + tryfile);
	    data = receiveFile(tryfile, "pdb", directory);
	    //System.out.println(data);
	    if (!((String)data.substring(0, 4)).equals("<h2>") && !data.equals("error") )
			//drawFile(data,"pdb");
	    	drawFile(directory+tryfile,data,"pdb");
	    else
	    {
			tryfile = new String(filename + ".mol2");
			//System.out.println("try2:" + tryfile);
			data = receiveFile(tryfile, "mol2", directory);
			drawFile(directory+tryfile,data,"mol2");
	    }
	}
	return;
    }

	// Wrapper so Jason's code likes Andrew's (stenhous 11/14/02)
	public void drawFile(String filedata)
	{	drawFile(filedata,"pdb");	}

	public void drawFile(String Filedata, String ext)
	{	if (ext.equals("mol2"))
	    par = new Parser(Filedata);   // why the need to parse? 
		//par = new mol2parser(filename,Filedata);
	else
	    //par = new PDBParser(filedata);
		par = new PDBParser(Filedata);

	try{
	    par.process();
	}catch(IOException ioe){
	    System.err.println(ioe);}
	 drawstructure(); 
	
	 }
    public void drawFile(String fileName, String filedata, String ext)
    {
	if (ext.equals("mol2")){
	    par = new mol2parser(fileName, filedata);   // why the need to parse? 
	}
	else{
	    //par = new PDBParser(filedata);
		par = new PDBParsernew(fileName, filedata);
	}
	try{
	    par.process();
	}catch(IOException ioe){
	    System.err.println(ioe);}
	
	 drawstructure();
	
    }
    
    public void drawstructure() 
    {
	atomV = addAtoms(par.sym,par.atomX,par.atomY,par.atomZ);
	bondV = addBonds(par.bAtom1,par.bAtom2,par.bOrder);
	
	if(!clearFlag){
	    for (int i =0; i<grp.atomList.size(); i++){
		atom oldatom = (atom) grp.atomList.elementAt(i);
		atomV.addElement(oldatom);   // update atom vector by taking atoms from atomList...diff b/w atomList and vector?
	    }
	    
	    for (int i =0; i<grp.bondList.size(); i++){
		bond oldbond = (bond) grp.bondList.elementAt(i);
		bondV.addElement(oldbond);
	    }
	 }

	grp = new MolTemplate(atomV, bondV, drawingArea);    //just for drawing/display
	drawingArea.setGroupToPaint(grp);
	grp.centerAtoms();
	newFF = true;

	for (int j = 0; j < grp.atomList.size(); j++)
	    //System.out.println( ((atom)grp.atomList.elementAt(j)).getTypeNum(true) );
	
		repaint();
        if(callDatabase==2)
        {
	        atomInfo("Structure may have many sub-structures from unit cell");
        }
        else{
			// This sets the empty space (width) for the bottom panel.
			atomInfo(spaces+spaces+spaces+spaces+spaces+spaces+spaces+" ");
		clearFlag = false;
        }
    }
   

    public group getGroup()
    {
	return grp;
    }

    /**
     *overload atom information
     */
    public void atomInfo()
    {
		atomInfoBlab.setBackground(this.getBackground ());
		blabs.setBackground(this.getBackground ());
		atomInfoBlab.setText("");
    }
    
    public void atomInfo(String s)
    {
		atomInfoBlab.setBackground(this.getBackground());
		blabs.setBackground(this.getBackground ());
		atomInfoBlab.setText(s);
		instrucaba.setWarning(false);
	}
    
    public void atomInfo(atom a)   // see whr atomInfo used
    {
		if(a == null) return;
		blabs.setBackground(this.getBackground ());
		atomInfo(a.toString());
    }
    
    public void atomInfoWarning (String s)
    {
		atomInfoWarning(s,Color.orange);
    }

    public void atomInfoWarning (String s, Color theColor)
    {
		blabs.setBackground (theColor);
		atomInfoBlab.setBackground (theColor);
		atomInfoBlab.setText (atomInfoBlab.getText()+"   Warning: "+s);
		instrucaba.setWarning(true);
    }

    /**
     * two GridBagConstrains
     */
    private void constrain (Container container, Component component,
			  int gridX, int gridY, int gridW, int gridH)
    {
	  constrain (container, component, gridX, gridY, gridW, gridH,
		     GridBagConstraints.NONE, GridBagConstraints.NORTHWEST,
		     0.0, 0.0, 0, 0, 0, 0);
    }
    
    private void constrain (Container container, Component component,
			    int gridX, int gridY, int gridW, int gridH,
			    int fill, int anchor, double weightX,
			    double weightY, int top, int left, int bottom,
			    int right)
    {
	GridBagConstraints c = new GridBagConstraints ();
	c.gridx = gridX; c.gridy = gridY;
	c.gridwidth = gridW; c.gridheight = gridH;
	  c.fill = fill; c.anchor = anchor;
	  c.weightx = weightX; c.weighty = weightY;
	  if (top + bottom + left + right > 0)
	c.insets = new Insets (top, left, bottom, right);
	  ((GridBagLayout) container.getLayout()).setConstraints(component, c);
    }

    /**
     *paint the periodic table
     */
    public void drawPTable(Graphics g) {
	int boxWidth = (ptableHSize-17)/18;
	int boxHeight = (ptableVSize-9)/10;
	int fontHeight = boxHeight-2;
	Font font = new Font("Helvetica", Font.BOLD, boxHeight-2);
	g.setFont(font);
	while(g.getFontMetrics().stringWidth("HHH") > boxWidth - 2) {
	    fontHeight--;
	    font = new Font("Helvetica",Font.BOLD, fontHeight);
	    g.setFont(font);
	}
	FontMetrics fm = g.getFontMetrics();
	if((boxWidth > 2) && (boxHeight > 2)) {
	    try {

// NOTE! I'm cheating.  Leave it to me to abuse classes :)  stenhous 1/16/03
// eData.txt is not an AtomDataFile, but it's just easier to call it one
//  because AtomDataFile supports everything needed and takes care of permissions.
		AtomDataFile eDataFile = new AtomDataFile(newNanocad.txtDir + 
	    		newNanocad.fileSeparator + "eData.txt");
		//AtomDataFile eDataFile = new AtomDataFile("eData.txt");
		for (int j = 0; j < 10; j++)
		{   for (int i = 0; i < 18; i++)			
		    {	String symbol = eDataFile.getLine();
			eDataFile.skipLines(1);
			if(!symbol.equals(""))
			{
			    if ((i > 1 && i < 11+j) || j > 7)
				g.setColor(Color.cyan);
			    else g.setColor(Color.green);
			    g.fillRect(i*(boxWidth+1) + ptableHOffset,j*(boxHeight+1)+ptableVOffset,boxWidth,boxHeight);
			    g.setColor(Color.black);
			    int Hoffset = (boxWidth - fm.stringWidth(symbol))/2;
			    int Voffset = (boxHeight - fontHeight)/2;
			    g.drawString(symbol, i*(boxWidth+1) + Hoffset + ptableHOffset, (j+1)*(boxHeight+1) - (1+Voffset)+ ptableVOffset);
			}
		    }
		}
	    }
	    catch (Exception e) {
		System.err.println("Error reading ptable data file.");
		System.err.println(e.toString());
		atomInfo(e.toString());
		e.printStackTrace();
	    }
	}
    }

    /**
     *energy minimize
     */
    private void energyMinimize(boolean useCG)
    {
	atomInfo("Initializing... (This could take a minute)");
	if(newFF)
	{
		if(isUFF) energyMinimizer = new uffMinimizeAlgorythm(grp, this);
		else energyMinimizer = new mm3MinimizeAlgorythm(grp, this);
		currentPotential = energyMinimizer.potentialOnly(this);
		newFF = false;
	}
	if(!energyMinimizer.minimizeGroup(this,useCG))
	    atomInfoWarning("Data missing- form may be incorrect.");
/* This code was meant to check to see if minimizations were doing any good.
   It "works" but you need to add some specific code in the if statement.
	if(currentPotential - energyMinimizer.potentialOnly(this) < 0.0001)
	{
	    ;
	}
	currentPotential = energyMinimizer.potentialOnly(this);
*/
	repaint();
    }
    

    /**
     * Insert the method's description here.
     * Creation date: (7/25/2000 3:16:55 PM)
     * @return nanocad.energyMinimizeAlgorythm
     */
	public energyMinimizeAlgorythm getEnergyMinimizer() {return energyMinimizer;}

    /**
     * Gets and displays potential.
     */
    private void getPotential()
	{
	atomInfo("Initializing... (This could take a minute)");
	if(newFF)
	{
		if(isUFF) energyMinimizer = new uffMinimizeAlgorythm(grp, this);
		else energyMinimizer = new mm3MinimizeAlgorythm(grp, this);
		newFF = false;
	}
	energyMinimizer.potentialOnly(this);
	repaint();
    }

    /**
     *constructor initialization
     */
    public void init ()
    {
	//setting default atom type
	currentAtomType = new GeneralAtom();
	currentAtomType.setName("Hydrogen");
	currentAtomType.setSymbol("H");
	currentAtomType.setAtomicNumber(1);
	currentAtomType.setMass(1.007);
	currentAtomType.setColor(new Color(255, 255, 255));
	currentAtomType.setCovalentRadius(.3);
	currentAtomType.setVdwEnergy(.382);
	currentAtomType.setVdwRadius(1.2);
	currentAtomType.setCorrectNumBonds(1);
	
	int p = 200;
	this.setBackground (new Color (p, p, p));
	Panel coorAxis = new Panel ();
	drawingArea = new drawPanel ();
	
	//debugWindow = new textwin("Debug window", "", false);
	//debugWindow.setVisible(false);
	GridBagLayout gridbag = new GridBagLayout ();
	this.setLayout (gridbag);
	
	instrucaba = new InstPanel(new FlowLayout(FlowLayout.RIGHT));
	instrucaba.setSize(500, 50);
	about = new Button("About");
	instrucaba.add(about);

	constrain (this, instrucaba, 0, 0, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.NORTH, .155, .155, 0, 0, 0, 0);

	constrain (this, drawingArea, 0, 1, 1, 1,
		   GridBagConstraints.BOTH,
		   GridBagConstraints.NORTH,
		   1.0, 1.0, 0, 0, 0, 0);

	groupMode = new Checkbox ("Group");
	groupMode.setState (false);
	showForces = new Checkbox ("Forces");
	showForces.setState (false);
	
	geometry = new Checkbox ("Geometry");
	geometry.setState (false);

	emin = new Choice();
	emin.addItem("--Minimize--");
	emin.addItem("Conj. Grad.");
	emin.addItem("Stp. Desc.");

	ffSelect = new Choice();
	ffSelect.addItem("--Force Field--");
	ffSelect.addItem("UFF");
	ffSelect.addItem("MM3");

	knownStructure = new Button("Structure");
	clear = new Button("Clear");
	help = new Button ("Help");
	undo = new Button ("Undo");	
	addHydro = new Button("Add H");
	getPotential = new Button("Get Potential");
	String osName=System.getProperty("os.name");
	getSetStructure = new Choice ();
	getSetStructure.addItem ("--Input/Output Menu--");
    getSetStructure.addItem ("Open saved PDB/Mol2"); //lixh_add
	getSetStructure.addItem ("Save as PDB");
	
	if (osName.startsWith("Windows"))
		getSetStructure.addItem ("View with Chime");
	else
		getSetStructure.addItem ("View with RasMol");
	getSetStructure.addItem ("View XYZ");
	getSetStructure.addItem ("View Native");
	getSetStructure.addItem ("Gaussian Input");
	getSetStructure.addItem ("GAMESS Input");
	getSetStructure.addItem ("NWChem Input");
	getSetStructure.addItem ("Molpro Input");
	getSetStructure.addItem ("View PDB");
	getSetStructure.addItem("View with VMD");
	
	grp = new group (drawingArea);
	grp1 =  new group(drawingArea);
	drawingArea.setGroupToPaint (grp);

	//if (debugWindow.isVisible())
	//    debugWindow.clear();
	grp.setTextwin(debugWindow);
	grp.v.zoomFactor = 25;
	
	tmpGroup = new group(grp);
	tmpGroup.v.zoomFactor = 25;
	
	chgGroup = new group(grp);
	chgGroup.v.zoomFactor = 25;
		
	undoGroup = new group(grp);
	updateUndo();
        
	controls.add (groupMode);
	controls.add (geometry);
	controls.add (showForces);
	controls.add (help);
	controls.add (knownStructure);
	controls.add (clear);
	controls.add (undo);
	controls.add (addHydro);

	controls2.add (getPotential);
	controls2.add (emin);
	controls2.add (ffSelect);
	controls2.add (getSetStructure);

	constrain (this, controls, 0, 2, 1, 1, GridBagConstraints.BOTH,
			GridBagConstraints.NORTH,0.0,0.0,0,0,0,0); 
	constrain (this, controls2, 0, 3, 1, 1, GridBagConstraints.BOTH,
			GridBagConstraints.NORTH,0.0,0.0,0,0,0,0);

	((FlowLayout)controls.getLayout()).setAlignment(FlowLayout.CENTER);
	((FlowLayout)controls2.getLayout()).setAlignment(FlowLayout.CENTER);
	
	atomInfoBlab = new Label("");
	blabs = new Panel();
	blabs.add (atomInfoBlab);
	
	constrain (this, blabs, 0, 4, 1, 1, GridBagConstraints.BOTH,
			GridBagConstraints.NORTH,0.0,0.0,0,0,0,0);
	((FlowLayout)blabs.getLayout()).setAlignment(FlowLayout.CENTER);

	this.add (instrucaba);
	this.add (drawingArea); 
	this.add (controls);
	this.add (controls2);
	this.add (blabs);

	drawingArea.addMouseListener(this);
	addMouseListener(this);
	drawingArea.addMouseMotionListener(this);
	addMouseMotionListener(this);

	grp.v.updateSize(600, 300);
	tmpGroup.v.updateSize(600, 300);
	chgGroup.v.updateSize(600, 300);

	// This code puts the molecule in the center for the local version.
	// Probably doesn't hurt the web one really :)
	if (runAsApplication) drawingArea.setSize(692, 366);
	grp.centerAtoms();

	initFlag = true;
	
    }
 public static void openURL(String url) {
        String osName = System.getProperty("os.name");
        String errMsg = "For more information, visit www.gridchem.org";
        //System.out.println("OS name is: "+osName);
        try {
           if (osName.startsWith("Mac OS")) {
        	   String currentDir = applicationDataDir;
      	   String sep_str = System.getProperty("file.separator");
      		if (!(currentDir.endsWith(sep_str)))
    			currentDir=currentDir+sep_str;
   			System.out.println("in OpenURL: applicationDatadir: "+currentDir);
    		     String ras_dir = currentDir+"rasmol"+sep_str;
    		  String rasmol = ras_dir+"RasMac_PPC_32BIT";
       		 System.out.println("1272: rasmol "+rasmol);
        	    String rasmolBin = ras_dir+"RasMac_PPC_32BIT.bin";
               File browser = new File(rasmol);
               if (browser.exists()){
               String run_ras =  "open -a "+ rasmol + " " + url;
               Runtime.getRuntime().exec(run_ras);
               System.out.println("1278: "+ run_ras);
               //System.out.println("1271");
                                    }
      		  else{
      			//System.out.println("1273");
      			if (Runtime.getRuntime().exec("open "+ rasmolBin).waitFor()==0){
      				JOptionPane.showMessageDialog(null,
      					    "Press OK after Rasmol Installation is over",
      					    "Installing Rasmol",
      					    JOptionPane.INFORMATION_MESSAGE
      					    );
      			String run_ras =  "open -a "+ rasmol + " " + url;
      			Runtime.getRuntime().exec(run_ras);
      			System.out.println("1291: "+run_ras);
                                                                }
                           }
              }
           else if (osName.startsWith("Windows"))
              {//System.out.println("test");
          	 Runtime.getRuntime().exec("cmd.exe /c start iexplore " + url);}
          	 
           else { //assume Unix or Linux
              String[] browsers = {
                 "mozilla", "netscape", "firefox", "konqueror", "opera" };
              String browser = null;
              for (int count = 0; count < browsers.length && browser == null; count++)
                 if (Runtime.getRuntime().exec(
                       new String[] {"which", browsers[count]}).waitFor() == 0)
                    browser = browsers[count];
              if (browser == null)
                 throw new Exception("Could not find web browser.");
              else
                 Runtime.getRuntime().exec("xterm -e " + browser + " " + url);
              }
           }
        catch (Exception e) {
           JOptionPane.showMessageDialog(null, errMsg);
           }
        }
    public void start()
    {	
    /*
	if(initFlag)
	{
		
	    if (!runAsApplication)
		loadFile("ethanol.mol2", "/work/chemviz/mol2/Molecule/Organic/");
	    else loadFile("aspirin.pdb", pathvar+sepStr+"common"+sepStr+
		"Molecule"+sepStr+"Organic"+sepStr);
	    initFlag = false;
		//DemoWindow test = new DemoWindow();
		//test.setVisible(true);
	    t = new Template("Import Structure", this);
	    t.setVisible(false);
	    energyMinimizer = null;
	    grp.setShowForces (showForces.getState());
	    grp.setTextwin(debugWindow);
	    needToRepaint = true;
	    atomInfo("               "+spaces+spaces+"Welcome to NCSA Nanocad!"+spaces+spaces+"               ");
	    drawingArea.setGroupToPaint(grp);
	    grp.updateViewSize();
	    getSetStructure.select(0);
	    grp.centerAtoms();
	    this.repaint();
	}
	*/

    	//lixh_4/29/05
    	if(initFlag == true)
    	{	
    		//String applicationDataDir = System.getProperty("user.dir");
    		//String defaultFile;
    		defaultFile = applicationDataDir + fileSeparator +
				"common" + fileSeparator + "Molecule" + fileSeparator 
				+ "Inorganic" + fileSeparator + "water.pdb";
    		try
			{
    			//System.out.println("Trying to check retrieved structure information in  "+ Settings.jobDir);
    			//File f = new File(applicationDataDir + fileSeparator + "loadthis");
    			File f = new File(Settings.jobDir + fileSeparator + "loadthis");
    			if (f.exists())
    			{
    				BufferedReader br = new BufferedReader(new FileReader(f));
    				String filetext;
    				while ((filetext = br.readLine()) != null)
    					defaultFile = filetext;
    				//System.out.println("newNanocad:Current Active File = " + defaultFile);
    			}
			}
    		catch (IOException ioe)
			{
    			System.err.println("newNanocad:start:IOException");
    			System.err.println(ioe.toString());
    			ioe.printStackTrace();
			}
             //if (runAsApplication == false){
    	    //loadFile("aspirin.pdb", "/work/chemviz/mol2/Molecule/Organic");
             //}
             //else {
    	 
    	 //lixh_4/27/05
    	 //File ftemp = new File(applicationDataDir + fileSeparator + defaultFile);
    	 File ftemp = new File(defaultFile);  
    	 if (ftemp.exists())
    	 {
    	 	//System.out.println("Trying to Load " + defaultFile);
            //loadFile(defaultFile, applicationDataDir);
    	 	loadFile(defaultFile,"");
            //System.out.println(" Successfully loaded default input!");
        /*    //******************
            //public static void openURL(String url) {
                String url = defaultFile;
                String errMsg = "For more information, visit www.gridchem.org";
            	String osName = System.getProperty("os.name");
                System.out.println("OS name is: "+osName);
                try {
                   if (osName.startsWith("Mac OS")) {
                      Class macUtils = Class.forName("com.apple.mrj.MRJFileUtils");
                      Method openURL = macUtils.getDeclaredMethod("openURL",
                         new Class[] {String.class});
                      openURL.invoke(null, new Object[] {url});
                      }
                   else if (osName.startsWith("Windows"))
                      {System.out.println("test");
                  	 Runtime.getRuntime().exec("cmd.exe /c start iexplore " + url);}
                  	 
                   else { //assume Unix or Linux
                      String[] browsers = {
                         "firefox", "opera", "konqueror", "mozilla", "netscape" };
                      String browser = null;
                      for (int count = 0; count < browsers.length && browser == null; count++)
                         if (Runtime.getRuntime().exec(
                               new String[] {"which", browsers[count]}).waitFor() == 0)
                            browser = browsers[count];
                      if (browser == null)
                         throw new Exception("Could not find web browser.");
                      else
                         Runtime.getRuntime().exec("xterm -e " + browser + " " + url);
                      }
                   }
                catch (Exception e) {
                   JOptionPane.showMessageDialog(null, errMsg);
                   }
//              ********************   } */ 
               //}
    	    initFlag = false;
    	    //DemoWindow test = new DemoWindow();
    	    //test.setVisible(true);
              // Center atoms
            needToRepaint = true;
    		atomInfo ();
    		grp.updateViewSize ();
            grp.centerAtoms();
            
            //lixh_4/30/05
       		atomInfoBlab.setBackground(this.getBackground());
    		blabs.setBackground(this.getBackground ());
    		atomInfoBlab.setText("Show atom information here                              ");
    		instrucaba.setWarning(false);
    		//
    		
            this.repaint();
            
    	   }
    	   else
    	   {
    	   	System.err.println("file " + defaultFile + " does not exist");
    	   }
    	}
    	//
    	 
    } 
    

    /** Display only .pdb and .mol2 files */
    class PDBorMOL2FileFilter extends FileFilter {

        /** Accept only .pdb or .mol2 files.
         *  @param file The file to be checked.
         *  @return true if the file is a directory, a .pdb or .mol2 file.
         */
        public boolean accept(File fileOrDirectory) {
            if (fileOrDirectory.isDirectory()) {
                return true;
            }

            String fileOrDirectoryName = fileOrDirectory.getName();
            int dotIndex = fileOrDirectoryName.lastIndexOf('.');
            if (dotIndex == -1) {
                return false;
            }
            String extension =
                fileOrDirectoryName
                .substring(dotIndex);

            if (extension != null) {
                if (extension.equalsIgnoreCase(".pdb")
                        || extension.equalsIgnoreCase(".mol2")) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        /**  The description of this filter */
        public String getDescription() {
            return ".pdb and .mol2 files";
        }
    }

    
	  
    /**
     *here we have the mouse functions.  Only 3 are needed, but definitions have to
     *be given for them all, since we are using an interface.  If nanocad is ever
     *made to use swing, mouseInputAdapter should be used instead of mouseListener
     *and mouseMotionListener
     */

    public void mouseClicked(MouseEvent e)
    {
	;//do nothing, since we are dealing with the mouse being pressed and released
    }

    public void mouseEntered(MouseEvent e)
    {
	;//do squat
    }

    public void mouseExited(MouseEvent e)
    {
	;//you get the idea
    }

    public void mouseMoved (MouseEvent e)
    {
	;
    }

    public void mousePressed (MouseEvent e)
    {
	int x = e.getX();
	int y = e.getY();
	int ourFlag = InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK |
					InputEvent.ALT_MASK | InputEvent.META_MASK;
	Rectangle r = drawingArea.bounds();
	inDrawingArea = y < r.height;
	needToRepaint = false;
	double[] scrPos = { x, y, 0 };
	mouseModifiers = e.getModifiers();
	mouseModifiers = mouseModifiers & ourFlag;
	atom1 = grp.selectedAtom (scrPos, true);
	
	dragFlag = false;

	if (atom1 != null)
	    {
		double[] atomScrPos = grp.v.xyzToScreen (atom1.x);
		atom1z = atomScrPos[2];

		if (geometryFlag)	//Andrew Knox 06/01/01
		{
		    int size = selectedAtomList.size();
		    int i = selectedAtomList.indexOf(atom1);

		    if (size == 0)
		    {	
				atom1.setSelected(true);
				selectedAtomList.addElement(atom1);
				size++;
		    }

		    else if (i != -1)
		    {
				while (selectedAtomList.size() > i)
				{
				    ((atom)selectedAtomList.elementAt(size - 1)).setSelected(false);
				    selectedAtomList.removeElementAt(size - 1);
				    size--;
				}
		    }

		    else
		    {
				//only add if it is next to the last atom
				for (i = 0; i < ((atom)selectedAtomList.elementAt(size - 1)).bonds.size(); i++)
				{     
				    if ( ((bond) ((atom)selectedAtomList.elementAt(size - 1)).bonds.elementAt(i)).a1 == atom1)
				    {
						if ((size == 1) || (atom1 != selectedAtomList.elementAt(size - 2)))
						{
						    atom1.setSelected(true);
						    selectedAtomList.addElement(atom1);
						    size++;
						    break;
						}
				    }
				    else if ( ((bond) ((atom)selectedAtomList.elementAt(size - 1)).bonds.elementAt(i)).a2 == atom1)
				    {
						if ((size == 1) || (atom1 != selectedAtomList.elementAt(size - 2)))
						{
						    atom1.setSelected(true);
						    selectedAtomList.addElement(atom1);
						    size++;
						    break;
						}
				    }
				}
		    }

		    //now find appropriate information
		    if (size == 2)	//bond length
		    {	geomValue = calculateBondLength();
		    }
		   
		    else if (size == 3) //bond angle
		    {	geomValue = calculateBondAngle();
		    }

		    else if (size == 4) //dihedral
		    {	geomValue = calculateDihedral();
		    }
		    
		    else ; //default
		    drawingArea.setGeometryValue(size, geomValue);
		    updateUndo();
		}    

		//Andrew Knox 5/24/01 get a group of all atoms connected to one atom
		else if (e.isAltDown() && e.isControlDown())
		{
		    selectedAtomList.removeAllElements();
		    selectedAtomList.addElement(atom1);

		    boolean dontAdd = false;
		    boolean elementAdded = true;
		    int firstToCheck = selectedAtomList.size();  	 //the position of the first atom we need to check in the next step
		    int i, j, k;

		    for (i = 0; i < atom1.bonds.size(); i++)	//get atoms bonded to that atom
		    {
			if (atom1 == ((bond)atom1.bonds.elementAt(i)).a1)
			    selectedAtomList.addElement(((bond)atom1.bonds.elementAt(i)).a2);
			else
			    selectedAtomList.addElement(((bond)atom1.bonds.elementAt(i)).a1);
		    }

		    while (elementAdded)			//get atoms bonded to those atoms, etc.
		    {
			elementAdded = false;

			for (i = firstToCheck; i < selectedAtomList.size(); i++)
			{
			    atom1 = (atom)selectedAtomList.elementAt(i);
			    for (j = 0; j < atom1.bonds.size(); j++)
			    {
				if (atom1 == ((bond)atom1.bonds.elementAt(j)).a1)
				{
				    for (k = 0; k < firstToCheck; k++)
					if (((bond)atom1.bonds.elementAt(j)).a2 == selectedAtomList.elementAt(k))
					    dontAdd = true;
					
		    		    if (!dontAdd)
				    {
					selectedAtomList.addElement(((bond)atom1.bonds.elementAt(j)).a2);
					elementAdded = true;
				    }
				}
				else
				{
				    for (k = 0; k < firstToCheck; k++)
					if(((bond)atom1.bonds.elementAt(j)).a1 == selectedAtomList.elementAt(k))
					    dontAdd = true;

				    if (!dontAdd)
				    {
					selectedAtomList.addElement(((bond)atom1.bonds.elementAt(j)).a1);
					elementAdded = true;
				    }
				}
			    dontAdd = false;
			    }
			firstToCheck = selectedAtomList.size();
			}
		    }

		    for (i = 0; i < selectedAtomList.size(); i++)
		    {
			((atom)selectedAtomList.elementAt(i)).setSelected(true);
		    }
		}

		else if (e.isAltDown() && !e.isControlDown() && !e.isShiftDown()) { //alt

		    if (!(atom1.isSelected()))
		    {
			atom1.setSelected(true); 

		    	//04/12 mark the atom directly not index 
		    	for (int i=0; i<(grp.atomList).size(); i++){
			    if (((atom)(grp.atomList.elementAt(i)))==atom1){
			    	selectedAtomList.addElement(atom1);
			
			    }
		    	}
		    }
		    else					//if atom is already selected, unselect it
		    {
			atom1.setSelected(false);

			for (int i = 0; i < (grp.atomList).size(); i++)
			    if (((atom)(grp.atomList.elementAt(i)))==atom1)
				selectedAtomList.removeElement(atom1);
		    }
		}
	        
		else 
		    updateUndo();
	    }
	else
	    {
		atom1z = 0;
	    }
	
	atomInfo (atom1);
	xxx = x; yyy = y;
	x0 = x; y0 = y;
    }
 
    /**
     *mouse drag operation handler 
     */

    public void mouseDragged (MouseEvent e)
    {
	//don't move molecule if choosing element
	if (seePTable) return;
	
	int x = e.getX();
	int y = e.getY();
	//mouseModifiers = e.getModifiers();

	// if moving atom, no need for extra line
	boolean movingAtom = false; 

	if (!dragFlag && !geometryFlag)
	    if (x < x0 - 2 || x > x0 + 2 || y < y0 - 2 || y > y0 + 2)
		dragFlag = true;
	if (!clearFlag)
	{
	    needToRepaint = true;
	    if (atom1 == null)
		{
		    //keep normal mode
		    switch (mouseModifiers)
			{
			default: //no specific atom, in blank area for drag, so rotate
			    
			    if (grpFlag)			//group rotation
			    {
				
				tmpGroup = new group(grp);
				chgGroup = new group(grp);

				for (int j =0; j < tmpGroup.atomList.size(); j++)
				{
				    atom tmpAtom = (atom) tmpGroup.atomList.elementAt(j);
				    if (belongTo(tmpAtom, selectedAtomList)){ 
				        //this keeps the bond very important!!!					    
				            tmpGroup.atomList.removeElement(tmpAtom);
					    j--;
				    }
				}
	
				for (int k = 0; k < chgGroup.atomList.size(); k++)
				{
				    atom tmpAtom = (atom) chgGroup.atomList.elementAt(k);
				    if (!belongTo(tmpAtom, selectedAtomList))
				    {
					chgGroup.atomList.removeElement(tmpAtom);
				    	k--;
				    }
				}

				chgGroup.rotate(0.01*(x-xxx), -0.01*(y-yyy));
				grp = new group (tmpGroup, chgGroup, drawingArea);
			    	drawingArea.setGroupToPaint(grp);
			    }
			    else					//molecule rotation
			    {
				grp.rotate (0.01 * (x - xxx), -0.01 * (y - yyy));
			    }
		            break;
			
			    
			case Event.SHIFT_MASK:
			    if (grpFlag)			//group translation
			    {
				tmpGroup = new group(grp);
				chgGroup = new group(grp);

				for (int j =0; j <tmpGroup.atomList.size();j++)
				{
				    atom tmpAtom = (atom) tmpGroup.atomList.elementAt(j);
				    if (belongTo(tmpAtom, selectedAtomList))
				    {
					//still keep the bondList
					tmpGroup.atomList.removeElement(tmpAtom);
			        	j--;
				    } 
			    	}
		    
				for (int k = 0; k < chgGroup.atomList.size(); k++)
				{
				    atom tmpAtom = (atom) chgGroup.atomList.elementAt(k);
				    if (!belongTo(tmpAtom, selectedAtomList))
				    {
					chgGroup.atomList.removeElement(tmpAtom);
					k--;
				    }
				} 

				chgGroup.pan((x-xxx),(y-yyy));
				grp = new group (tmpGroup, chgGroup, drawingArea);
				drawingArea.setGroupToPaint(grp);		    
			    }
			    else grp.v.pan (x - xxx, y - yyy); //molecule translation
			    break;
			    
			case Event.CTRL_MASK:
			    grp.v.zoomFactor *= Math.exp (0.01 * (x - xxx));
			    //grp.v.perspDist *= Math.exp (0.01 * (y - yyy));
			    break;

			case Event.META_MASK:
			    grp.v.zoomFactor *= Math.exp (0.01 * (x - xxx));
			    //grp.v.perspDist *= Math.exp (0.01 * (y - yyy));
			    break;

			}
		drawingArea.clear();
		grp.wireframePaint();

		}
	    else if (geometryFlag) return;
		else
		//select atom or click on the atom
		{
		    switch (mouseModifiers)
			{
			default: //==0; drag without alt , shift or ctrl
				double[] scrPos = { x, y, atom1z };
				atom1.x = grp.v.screenToXyz (scrPos);
				movingAtom = true;
				drawingArea.clear();
				grp.wireframePaint ();
			    break;
			    
			case Event.SHIFT_MASK: //==1
			    drawingArea.clear();
			    grp.bubblePaint ();
			    break;

			case Event.CTRL_MASK: //==2
			    drawingArea.clear();
			    grp.bubblePaint ();
			    break;

			case Event.META_MASK:
			    drawingArea.clear();
			    grp.bubblePaint ();
			    break;

			case Event.ALT_MASK: //==8, do nothing except marked already
			    break;
			}
		}

	    xxx = x; yyy = y;
	    if (atom1 != null && !movingAtom) grp.drawLineToAtom (atom1, x, y);
	}
    }

    /**
     *determine whether an atom belongs to an atom vector or not
     */
    public boolean belongTo(atom a, Vector b){
	boolean contain = false;
	
	for (int i = 0; i< b.size(); i++) {
	    atom c = (atom) b.elementAt(i);
	    if (a == c){
		contain = true;
		break;
	    }
	}

	return contain;
    }

    /**
     *mouse up operation handler
     */

    public void mouseReleased (MouseEvent e)
    {
	int x = e.getX();
	int y = e.getY();
	int selectCount = 0;
	
	//if periodic table is up, select appropiate atom
	if(seePTable)
	    {
		int pTableX = (x-ptableHOffset)/((ptableHSize-17)/18 + 1);
		int pTableY = (y-ptableVOffset)/((ptableVSize-9)/10 + 1);
		
		try
		{
		    if((pTableX >= 0) && (pTableY >= 0))
		    {
			AtomDataFile dFile = new AtomDataFile(newNanocad.txtDir + 
		    		newNanocad.fileSeparator + "pdata.txt");
			//AtomDataFile dFile = new AtomDataFile("pdata.txt");
			if(dFile.findData(pTableY, pTableX, 1, 2))
			{
			    GeneralAtom selected = new GeneralAtom();
			    selected.setName(dFile.parseString(3));
			    selected.setSymbol(dFile.parseString(4));
			    selected.setAtomicNumber(dFile.parseInt(0));
			    selected.setMass(dFile.parseDouble(5));
			    selected.setColor(new Color(dFile.parseInt(6), dFile.parseInt(7), dFile.parseInt(8)));
			    selected.setCovalentRadius(dFile.parseDouble(9));
			    selected.setVdwEnergy(dFile.parseDouble(10));
			    selected.setVdwRadius(dFile.parseDouble(11));
			    selected.setCorrectNumBonds(dFile.parseInt(12));
			    currentAtomType = selected;
			    t.currentElement.setText("Current Element: "+dFile.parseString(4));
			    seePTable = false;
			}
		    }
		}

		catch(IOException ex)
		{
		    ex.printStackTrace();
		}
		repaint();
		return;
	    }
	
	double[] scrPos = { x, y, atom1z };
	Graphics g = this.getGraphics ();
	atom atom2 = grp.selectedAtom (scrPos, false);

	if ((atom1 != null) && !geometryFlag)
	    {
		if (dragFlag)
		    {
			//drag on an atom
			switch (mouseModifiers)
			{
			default: //copy the dragged atom
			    atom1.x = grp.v.screenToXyz (scrPos);
			    atom2 = atom1;
			    break;
				
			case Event.SHIFT_MASK:
			    if (atom1 != atom2 && atom1 != null && atom2 != null)
			    {
				updateUndo();
				grp.addBond (atom1, atom2);
			    }
			    break;

			case Event.CTRL_MASK:
			    if (atom1 != atom2 && atom1 != null && atom2 != null)
			    {
				updateUndo();
				grp.deleteBond (atom1, atom2);
			    }
			    atomInfo (atom2);
			    break;
			}
		    }
		//not drag, but click when up is detected
		else 
		    {
			//click on an atom
			switch(mouseModifiers)
			{
			case Event.SHIFT_MASK:	
			    if (atom1 != null)
			    {
				updateUndo();
				grp.deleteAtom (atom1);
			    }
			    else
				atomInfo();
				
			    needToRepaint = true;
			    atomInfo ();
			    break;

			case Event.ALT_MASK:
			     needToRepaint = true;
			}
		    }
	    }
	//no atom
	else { 
	    //no atom and no drag
	    if (!dragFlag && !geometryFlag)
		{
		//click on empty space
		    switch (mouseModifiers)
		    {
		    case Event.SHIFT_MASK:
			int sp; // What is this?
			atom a = null;
			needToRepaint = true;
			    
			a = (atom) currentAtomType.copy();
			if (a != null)
			{
			    updateUndo();
			    grp.addAtom (a, scrPos);
			    atomInfo (a);
			    clearFlag = false;        //we know there is at least one atom
			}
		        break;

		    case Event.CTRL_MASK:
			needToRepaint = true;
			atomInfo ();
			grp.updateViewSize ();
			grp.centerAtoms ();
			break;

		    case Event.META_MASK:
			needToRepaint = true;
			atomInfo ();
			grp.updateViewSize ();
			grp.centerAtoms ();
			break;

		    case Event.ALT_MASK:
			needToRepaint = false;
			break;
		    }
		}
	}
	
	this.repaint ();
    }
 
    /**
     *paint
     */
    public void paint (Graphics g)
    {
	super.paint(g);

	drawingArea.clear();
	drawingArea.repaint();

	drawingArea.clear();
	drawingArea.repaint();

	if(seePTable)
	{   drawingArea.clear();
	    drawPTable(drawingArea.getGraphics());

	    drawingArea.clear();
	    drawPTable(drawingArea.getGraphics());
 	}
	needToRepaint = false;
    }

    public final synchronized void update (Graphics g) 
    {
        paint(g);
    }


    /**
     *main function for loading structure through socket, no use anymore,
     */
    protected String receiveFile()
    {
	try
	    {
		/* 
		   NanoDataPacket pkt = new NanoDataPacket();
		   pkt.setStatus(NanoDataPacket.readPacket);
		   pkt.setName(getParameter("fname"));
		   byte[] data = pkt.byteArray();
		*/
		
		byte[] data = new byte[50000];
		InetAddress addr = InetAddress.getByName(getCodeBase().getHost()); 
		//give read command to server;
		String comm = "r";
		byte cData[] = comm.getBytes();
		
		DatagramPacket spack =  new DatagramPacket(cData, cData.length, addr, Integer.parseInt(getParameter("myPort"))); 
		DatagramSocket ds = new DatagramSocket(); 
		ds.send(spack);
	
		DatagramPacket rpack=  new DatagramPacket(new byte[50000], 50000); 
			    
		ds.receive(rpack); 
		ds.close();
		
		return new String(rpack.getData()); 
		
		/*pkt = new NanoDataPacket(rpack.getData());
		  return new String(pkt.getData());
		*/
		
	    }catch ( IOException e ) 
		{    
		    System.out.println( e ); 
		    return new String(e.toString());
		}
    }
    
    /**
     *send file to specific url
     */
    protected void sendFile(String urlname, String filedata, String filename) {  // to send file frm Nanocad to Waltz, u can use sendFile??
	String username = getParameter("username");
	String encodedUsername = URLEncoder.encode(username);
	String encodedFilename = URLEncoder.encode(filename);
	String encodedData =  URLEncoder.encode(filedata);
	
	try 
	    {	
		URL url1 = new URL(urlname);
	        //System.out.println("URL savepdb.cgi assigned to"+urlname);	
                		
	        URLConnection conn1 = url1.openConnection(); 
		System.out.println("in sendFile: url connection opened");

		conn1.setDoOutput(true);
		PrintWriter out1 = new PrintWriter(conn1.getOutputStream());

		out1.println("userid=" + encodedUsername + "&");
		//System.out.println("id=" + encodedUsername);
		out1.println("filename=" + encodedFilename + "&");
		//System.out.println("filename=" + encodedFilename);
		out1.println("data=" + encodedData);
		//System.out.println("data=" + encodedData);
		out1.close();

		//System.out.println("now the molecule data is sent out to server....\n");
		
               //read conformation from the URL ;   // Anupama What data are we reading here?? Is it the sent file or smthin else
		String line = null;
		BufferedReader inStr1 = new BufferedReader (new InputStreamReader(conn1.getInputStream()));
		while ((line = inStr1.readLine()) !=null) 
		    {
			//System.out.println(line);
		    }
               
                inStr1.close();
	    }catch(MalformedURLException mx){
		saveWin = new textwin("URL Exception","",false);
		saveWin.setVisible(true);
		saveWin.setText("Exception : "+ mx.toString());
	    }catch ( IOException e){ 
		saveWin = new textwin("IO Exception","",false);
		saveWin.setVisible(true);
		saveWin.setText("Exception : "+e.toString());
		System.out.println(e.getMessage());
	    }
    }


    /**
     *main function for reloading structure through url
     */
    protected String receiveFile(String filename, String type, String directory)
    {
	try
	    {
		URL url;
		StringBuffer b = new StringBuffer(); 
		String encodedFilename = null;
	
		encodedFilename = URLEncoder.encode(filename); 
		if (type.equals("mol2") )
		    url = new URL("http://pine.ncsa.uiuc.edu/cgi-bin/openpdb-II.cgi"); // what r we doing here?
                else if(callDatabase ==1)
		          url = new URL(" http://chemviz.ncsa.uiuc.edu/cgi-bin/reloadpdbIndiana.cgi");
		else if(callDatabase ==2)
		          url = new URL(" http://chemviz.ncsa.uiuc.edu/cgi-bin/reloadCsd.cgi");
		else
		          url = new URL("http://pine.ncsa.uiuc.edu/cgi-bin/openpdb-II.cgi");


         	if (!runAsApplication)
		{
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		
		    PrintWriter out = new PrintWriter(conn.getOutputStream());
		    out.print("filename=" + encodedFilename);
		    out.print("&" + "directory=" + directory);
		    out.close();

		    //read result mol2 file back through the URL again;          
		    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line = null;
		    while((line = in.readLine())!=null){
			b.append(line);
		    }
		
		    in.close();
		}
		else
		{
		    File toOpen = new File(directory+filename);
		    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(toOpen)));
		    String line = null;
		    while((line = in.readLine()) !=null)
			b.append(line);

		    in.close();
		}
		return new String(b);

	    }catch(MalformedURLException mx){
		System.out.println(mx);
		return new String(mx.toString());
	    }catch ( IOException e){ 
		System.out.println(e.getMessage()); 
		return new String("error");
	    }
    }
  
    /**
     * Insert the method's description here.
     * Creation date: (7/25/2000 3:16:55 PM)
     * @param newEnergyMinimizer nanocad.energyMinimizeAlgorythm
     */
    public void setEnergyMinimizer(energyMinimizeAlgorythm newEnergyMinimizer) {
	energyMinimizer = newEnergyMinimizer;
    }
    
    /**
     *add atoms by cooridates
     */
    public Vector addAtoms(Vector aSym,Vector aX,Vector aY,Vector aZ){
	
    Vector atomL = new Vector();
	atom a = null;
	double x, y, z;
	for (int i=0; i<aSym.size(); i++){
	    //cast first
	    x = ((Double)aX.elementAt(i)).doubleValue();
	    y = ((Double)aY.elementAt(i)).doubleValue();
	    z = ((Double)aZ.elementAt(i)).doubleValue();
	    //determine atom type first;
	    a = determineAtom((String)aSym.elementAt(i), x,y,z);
	    //System.out.println(a.name());
	    atomL.addElement(a);
	}
	return atomL;
    }	

    /**
     *determine atom type and coordinates
     */
    public atom determineAtom(String sym, double x0, double y0, double z0){
	atom a = null;

	try{
	    AtomDataFile dFile = new AtomDataFile(newNanocad.txtDir + 
	    		newNanocad.fileSeparator + "pdata.txt");
	    //AtomDataFile dFile = new AtomDataFile("pdata.txt");
	    
	    if(dFile.determineData(sym))
		{
		    GeneralAtom selected = new GeneralAtom();
		    selected.setName(dFile.name);
		    selected.setSymbol(dFile.symbol); //may be commented out;
		    selected.setAtomicNumber(dFile.atomicNum);
		    selected.setMass(dFile.mass);
		    selected.setColor(new Color(dFile.color1, dFile.color2, dFile.color3));
		    selected.setCovalentRadius(dFile.covalentRadius);
		    selected.setVdwEnergy(dFile.vdwEnergy);
		    selected.setVdwRadius(dFile.vdwRadius);
		    selected.setCorrectNumBonds(dFile.correctNumBonds);
		    a = (atom)selected;
		    a.x[0] = x0;
		    a.x[1] = y0;
		    a.x[2] = z0;
		}
	}
	catch (Exception e){

	    System.err.println(e);
	    return null;
	}
	return a;
    }
    
    /**
     *add bonds with correct order
     */
    public Vector addBonds(Vector bA1,Vector bA2, Vector bO){
	Vector bondL = new Vector();
	bond b = null;
	
	int order;
	atom atom1 = null;
	atom atom2 = null;
	int index1, index2;
	System.out.println("bA1.size: "+bA1.size());
	for (int i =0; i<bA1.size(); i++){
	    order = ((Double)bO.elementAt(i)).intValue();
	    index1 = ((Double)bA1.elementAt(i)).intValue();
        index2 = ((Double)bA2.elementAt(i)).intValue();
        int atomVsize = atomV.size();
        int ba2size=bA2.size();
        int ba1size=bA1.size();
        System.out.println("ba1size: "+ba1size+",bA2size: "+ba2size+",atomVsize: "+atomVsize+" i: "+i+" order: "+order+" index1: "+index1+" index2: "+index2);
	    atom1 = (atom)atomV.elementAt(index1-1);
	    atom2 = (atom)atomV.elementAt(index2-1);
	    
	    b = new bond(atom1, atom2, order); 
	    bondL.addElement(b);
	}
	return bondL;
    }

    public void getDirectoriesInDirectory(java.awt.List dirlist, String directory)
    {
	if (runAsApplication && (directory.charAt(directory.length()-1) != sepChar))
		directory = directory + sepStr;
	else if (directory.charAt(directory.length()-1) != '/')
		directory = directory + "/";
	String encodedDirName = URLEncoder.encode(directory);  
        
	try
	{
	    URL url = new URL("http://pine.ncsa.uiuc.edu/cgi-bin/readdirfromdir.cgi");
                              
            URLConnection conn = url.openConnection();
	    conn.setDoOutput(true);
	    PrintWriter out = new PrintWriter(conn.getOutputStream());
	    out.println(directory);
	    out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while((line = in.readLine())!= null)
	    {
               	dirlist.add(line);
	    }
            in.close();
        }catch(IOException e1){System.out.println("Doh!");}
    }

    public NcadMenuBar CreateMenu(NcadMenuBar bar, Template t)
    {
	NcadMenu temp;
	NcadMenu[] toadd;
	MenuItem tempItem;
	int idc = 0;
	java.awt.List dirl = new java.awt.List(50, false);
	
	if (!runAsApplication)
	    getDirectoriesInDirectory(dirl, "/work/chemviz/mol2/");
	/**
	else
	{
	    File directoryFile = new File(pathvar+sepStr+"common"+sepStr);
	    String[] directoryStrings = directoryFile.list();
	    File[] directories = directoryFile.listFiles();

	    for (int i = 0; i < directories.length; i++)
	    {
		if (directories[i].isDirectory())
		{   idc++;
		    dir.add(directories[i].getName());
		}
	    }
	}

	int items;
	if (runAsApplication) items = idc + 3;
	else items = dir.getItemCount() + 3;
	toadd = new NcadMenu[items];

	toadd[0] = new NcadMenu("Atom");
	tempItem = new MenuItem("Select element...");
	tempItem.addActionListener(t);
	toadd[0].add(tempItem);

	toadd[1] = new NcadMenu("Database");
// Indiana is commented out because it's worthless. :)
//	tempItem = new MenuItem("Indiana Database");
//	tempItem.addActionListener(t);
//	toadd[1].add(tempItem);
	tempItem = new MenuItem("CSD");
	tempItem.addActionListener(t);
	toadd[1].add(tempItem);
	if (runAsApplication)
	{
		tempItem = new MenuItem("CSD/Chime setup");
		tempItem.addActionListener(t);
		toadd[1].add(tempItem);
	}

	toadd[2] = new NcadMenu("My Files");
	tempItem = new MenuItem("Load saved file");
	tempItem.addActionListener(t);
	toadd[2].add(tempItem);

	for (int i = 3; i < items; i++)
	{
	    String nospace = removeSpace(dir.getItem(i-3));
	    temp = new NcadMenu(nospace);
	    if (!runAsApplication)
		CreateMenu((Menu)temp.getMenu(), "/work/chemviz/mol2/" + ((Menu)temp.getMenu()).getLabel(), t);
	    else
		CreateMenu((Menu)temp.getMenu(), pathvar+sepStr+"common"+sepStr+
			((Menu)temp.getMenu()).getLabel(), t);
	    toadd[i] = temp;
	}

	bar = new NcadMenuBar(toadd, items);
	return bar;
	*/
	
	//lixh_3_4_05
	else
	{
//	    File directoryFile = new File("C:\\nanocad\\common");
	    // if it doesn't exist, download from mss and unzip it!
	    if (!((new File(applicationDataDir + fileSeparator + "common")).exists()))
	    {
	    	String zipFileName = applicationDataDir + fileSeparator + "nanocaddata.zip";
	    	if (!((new File(zipFileName)).exists()))
	    	{
	    		GetDataFile gf = new GetDataFile(zipFileName);
	    	}
	    	//Unzip uz = new Unzip("nanocad" + fileSeparator + zipFileName);
	    	ZipExtractor uz = new ZipExtractor(zipFileName);
	    	System.err.println("Done with unzipping stuff");
	    }
	    File directoryFile = new File(applicationDataDir + fileSeparator 
			    + "common");
	    String[] directoryStrings = directoryFile.list();
	    //File[] directories = new File[directoryStrings.length];
        File[] directories = directoryFile.listFiles();
          
	    //for (int i = 0; i < directoryStrings.length; i++)
		//directories[i] = new File(directoryStrings[i]);
          
	    for (int i = 0; i < directories.length; i++)
	    {
		if (directories[i].isDirectory() == true)
            { idc++;
		    dirl.add(directories[i].getName());
              //System.out.println(dirl.getItemCount()+ idc +" Directory "+directories[i].getName()+" found");
	      }
          }

	}

	//int items = 3 + dirl.getItemCount();
    int items = 3 + idc;
    //System.out.println("Found "+items+" less 3 directories in local data base");
	toadd = new NcadMenu[items];

	toadd[0] = new NcadMenu("Atom");
	tempItem = new MenuItem("Select element...");
	tempItem.addActionListener(t);
	toadd[0].add(tempItem);

	toadd[1] = new NcadMenu("Database");
	tempItem = new MenuItem("Indiana Database");
	tempItem.addActionListener(t);
	toadd[1].add(tempItem);
	tempItem = new MenuItem("CSD");
	tempItem.addActionListener(t);
	toadd[1].add(tempItem);

	toadd[2] = new NcadMenu("My Files");
	tempItem = new MenuItem("Load saved file");
	tempItem.addActionListener(t);
	toadd[2].add(tempItem);

	for (int i = 3; i < items; i++)
	{
	    String nospace = removeSpace(dirl.getItem(i-3));
	    temp = new NcadMenu(nospace);
	    if (runAsApplication == false){
		CreateMenu( (Menu)temp.getMenu(), "/work/chemviz/mol2/" + ((Menu)temp.getMenu()).getLabel(), t);
	    }
          else
          {
//		CreateMenu( (Menu)temp.getMenu(), "C:\\nanocad\\common\\" + ((Menu)temp.getMenu()).getLabel(), t);
		CreateMenu( (Menu)temp.getMenu(), applicationDataDir + 
				fileSeparator +  "common" + fileSeparator + 
				((Menu)temp.getMenu()).getLabel(), t);
		System.err.println("Where am I now?");
	    }
          toadd[i] = temp;
	}

	bar = new NcadMenuBar(toadd, items);
	return bar;
    }	


    public String removeSpace(String s)
    {
	if ( s.charAt(0) == ' ')
	    s = s.substring(1, s.length());
	if ( s.charAt(s.length() - 1) == ' ')
	    s = s.substring(0, s.length() - 1);
	return s;
    }

    public Menu CreateMenu(Menu m, String directory, Template t)
    {
	MenuItem temp;
	Menu tempMenu = null;
	java.awt.List files = new java.awt.List(50, false);
	java.awt.List dir = new java.awt.List(50, false);

	if (!runAsApplication)
	{   getDirectoriesInDirectory(dir, directory);
	    getFilesInDirectory(files, directory);
	}
	else
	{
	    File directoryFile = new File(directory);
	    String[] directoryStrings = directoryFile.list();
	    File[] directories = directoryFile.listFiles();
	    for (int i = 0; i < directories.length; i++)
	    {
		if (directories[i].isDirectory())
		    dir.add(directories[i].getName());
	    }

	    directoryFile = new File(directory);
	    directoryStrings = directoryFile.list();
	    File[] fileList = directoryFile.listFiles();
	    for (int i = 0; i < fileList.length; i++)
	    {
		if (!fileList[i].isDirectory())
		    files.add(fileList[i].getName());
	    }
	}
	    
	int numfiles = files.getItemCount();

	for (int i = 0; i < numfiles; i++)
	{
	    String name = removeSpace(files.getItem(i));
	    int pos = name.indexOf(".pdb");
	    if (pos != -1)
	    {
		name = name.substring(0, pos);
		temp = new MenuItem(name);
		temp.addActionListener(t);
		m.add(temp);
	    }
	    else if ((pos = name.indexOf(".mol2")) != -1)
	    {
		name = name.substring(0, pos);
		temp = new MenuItem(name);
		temp.addActionListener(t);
		m.add(temp);
	    }
	}	

	for (int i = 0; i < dir.getItemCount(); i++)
	{
	    tempMenu = new Menu( removeSpace(dir.getItem(i)) );
	    if (!runAsApplication) CreateMenu(tempMenu, directory + "/" + removeSpace(tempMenu.getLabel()), t);
	    else CreateMenu(tempMenu, directory + sepStr + removeSpace(tempMenu.getLabel()), t);
	    if (tempMenu != null)
		m.add(tempMenu);
	}
	return m;
    }

    /**
     *inner class "Template" for structure reloading   //Anupama use this code while making menu dynamic!
     */
    public class Template extends java.awt.Frame implements java.awt.event.ActionListener   
    {
	private NcadMenuBar myMenuBar;
	private NcadMenu[] m; 
	private java.awt.Menu[] molSubMenu, molSubMenu_1; 
	private java.awt.MenuItem atomItem, myfilesItem;
    private java.awt.MenuItem[] databaseItem;
	private java.awt.MenuItem[] orgItem, inorgItem, aminItem, ionsItem, funcGrpItem, bioItem;
	private Panel templatecontrols;
	private Label currentElement, elementLabel;
	private NcadMenu test1, test2;
	private TextField elementField;
	private Button select;
	private String openMenu;

	public Template (String name, newNanocad n1){
	    super(name);
	    nano = n1;
	    setLayout(new GridBagLayout());
    
	    templatecontrols = new Panel();
	    elementLabel = new Label("Change current element to:");
	    elementField = new TextField("H", 3);
	    select = new Button("Select");
	    select.addActionListener(this);

	    myMenuBar = CreateMenu(myMenuBar, this);
	    myMenuBar.setParent(this);

	    currentElement = new Label("Current Element: " + currentAtomType.symbol());
	    templatecontrols.add(elementLabel);
	    templatecontrols.add(elementField);
	    templatecontrols.add(select);
	    templatecontrols.add(currentElement);
	    
	    constrain(this, myMenuBar, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
	    constrain(this, templatecontrols, 0, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST, 0.0, 1.0, 0, 0, 0, 0);


//***************ANU TRYING*******************/
                 structure = new Panel();
         //write applet code for popping up a window
                 CompoundName = new TextField(7);								       
		 Formula = new TextField(7);
                 Label lCompoundName    = new Label("Compound Name: ",Label.RIGHT);
                 Label lFormula         = new Label("Formula: ",Label.RIGHT);
                 Button  okbutt = new Button("OK");
                  structure.add(lCompoundName);
                  structure.add(CompoundName);
                  structure.add(lFormula);
                  structure.add(Formula);
                  structure.add(okbutt);
                  constrain(this,structure,0,3,3,3,GridBagConstraints.BOTH,GridBagConstraints.NORTHWEST,0.0,3.0,3,3,3,3);

//***************ANU TRYING*******************/
	    setVisible(true);
	    add(myMenuBar);
	    add(templatecontrols);

	    pack();
	    setResizable(false);
	    setSize(475,105);

	    addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent e){
			setVisible(false);
			//System.exit(0);
		    }
		});
	    templatecontrols.paint(templatecontrols.getGraphics()); 
	}
	
	/**
	 *template handler
	 */
	public void actionPerformed(java.awt.event.ActionEvent e){
	    String s = e.getActionCommand();
	    selectedItem = s;
	    //System.out.println( s.toString() );
	    if(s.equals("Select element...")) {
		drawingArea.clear();
		drawPTable(drawingArea.getGraphics());
		seePTable = true;
	    } //Actual code for INDIANA thing!
	    
	    else if (s.equals("Indiana Database")){
                callDatabase = 1; 	//this variable is used directly by indianaWindow.class
                indiana = new indianaWindow(nano);
                indiana.setVisible(true);

                //Now indianaWindow calls a cgi script which will put the appropriate pdb file in fileFromIndiana.pdb so we need toload that file.
 
                this.setVisible(false);  

                // Check if foll code needed !
                addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent e){
			setVisible(false);
			//System.exit(0);
		    }
		});
	    	structure.paint(structure.getGraphics()); 
         	pack();
            }

	    // This gives you the ability to get the place where other people's executables
	    //  are stored for CSD and Chime.  I never had a chance to implement loading either of them
	    //  because we don't have them on density (as far as I know).  Good luck with that!  -- Jeff
	    else if (s.equals("CSD/Chime setup")){
		define = new defineWindow(nano); 
		define.setVisible(true);

		this.setVisible(false);

		addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent e){
			setVisible(false);
			//System.exit(0);
		    }
		});
		structure.paint(structure.getGraphics());
		pack();
	    }

	    else if (s.equals("CSD")){
             // TO ADD ACTION CORR TO CSD!
                 callDatabase = 2;	//this variable is used directly by the indianawindow class


                 //System.out.println("*** I am in CSD ***");

                 indiana = new indianaWindow(nano);
                 indiana.setVisible(true);
            //Now indianaWindow calls a cgi script which will put the appropriate pdb file in /work/csd/temp/csdsearch-<process id>.mol2 so we need toload that file.
 
                 this.setVisible(false);  
               // Check if foll code needed !
               addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent e){
			setVisible(false);
		    }
		});
	    	structure.paint(structure.getGraphics()); 
         	pack();
	    }
              
	    else if (s.equals("Load saved file"))
	    {	
		if (!runAsApplication)
		{
  		    browser=new java.awt.List(50,false);
		    String dirName ="/work/csd/temp/"+getParameter("username")+"/";
		    getFilesInDirectory(browser, dirName);

                    try{
                	String username = getParameter("username");
                	String dirname = "http://pine.ncsa.uiuc.edu/csd/temp/" + username+"/";
                	//System.out.println("Creating display.");
			dis=new display(nano,browser,dirname,username);
                	dis.setVisible(true);
                	//System.out.println("Sending List.");
 
                	// Now the above code whn repainted will produce file browser window
		    }catch(Exception e1)
                	{  System.err.println(e1);   }       
  
		    browser.setVisible(true);
		}
		else
		{
		    FileDialog f = new FileDialog(this, "Load File", FileDialog.LOAD);
		    f.show();
		    String file = f.getFile();
		    String directory = f.getDirectory();
		    loadFile(file, directory);
		    
            //lixh_4/30/05
    		atomInfoBlab.setText("Show atom information here                                        ");
    		instrucaba.setWarning(false);
    		
            this.repaint();
            //
		}
	    }
	    
	    else if (s.equals("Select"))
	    {
		//popup.show(elementLabel, 0, 0);
		try
		{
		    AtomDataFile dFile = new AtomDataFile(newNanocad.txtDir + 
		    		newNanocad.fileSeparator + "pdata.txt");
		    //AtomDataFile dFile = new AtomDataFile("pdata.txt");
		    String symbol = elementField.getText();
		    if (symbol.length() == 1)
			symbol.toUpperCase();
		    if(dFile.findData(symbol, 4))
		    {
			GeneralAtom selected = new GeneralAtom();
			selected.setName(dFile.parseString(3));
			selected.setSymbol(dFile.parseString(4));
			selected.setAtomicNumber(dFile.parseInt(0));
			selected.setMass(dFile.parseDouble(5));
			selected.setColor(new Color(dFile.parseInt(6), dFile.parseInt(7), dFile.parseInt(8)));
			selected.setCovalentRadius(dFile.parseDouble(9));
			selected.setVdwEnergy(dFile.parseDouble(10));
			selected.setVdwRadius(dFile.parseDouble(11));
			selected.setCorrectNumBonds(dFile.parseInt(12));
			currentAtomType = selected;
			currentElement.setText("Current Element: "+ symbol);
		    }
		}
		catch (IOException er)
		{
		    er.printStackTrace();
		}	
	    }

	    else {
		 //for url only
		if ( s.charAt(0) == ' ') s = s.substring(1, s.length());
		String name = s;
		MenuItem temp = (MenuItem) ((MenuItem)e.getSource()).getParent();
		
		do
		{
		    if (runAsApplication) name = temp.getLabel() + sepStr + name;
		    else name = temp.getLabel() + "/" + name;
		    if ( (((Object) temp.getParent()).toString()).equals("NcadMenu") )
			break;
		    //System.out.println(((Object)temp.getParent()).toString());
		    temp = (MenuItem)temp.getParent();
		} while (true);

		if (!runAsApplication)
		    loadFile(name, "/work/chemviz/mol2/");
		else
		{
			//lixh_4/27/05
		    //loadFile(pathvar+sepStr+"common"+sepStr+name, "");
            loadFile(applicationDataDir+fileSeparator+"common"+fileSeparator+name, "");	
            //lixh_4/30/05
            atomInfoBlab.setText("Show atom information here                                  ");
            instrucaba.setWarning(false);
		
            this.repaint();
		}
	    }
	}
    } 
    
    //Andrew Knox 06/01/01
    public double calculateBondLength()
    {
	double p[] = {0.0, 0.0, 0.0}; 
	double q[] = {0.0, 0.0, 0.0};
	double length;

	for (int i = 0; i < 3; i++)
	{
	    p[i] = ((atom)selectedAtomList.elementAt(0)).x[i];
	    q[i] = ((atom)selectedAtomList.elementAt(1)).x[i];
	}

	length = Math.sqrt( Math.pow(q[0] - p[0], 2) + Math.pow(q[1] - p[1], 2) + Math.pow(q[2] - p[2], 2) );
    	return length;
    }

    //Andrew Knox 06/01/01
    public double calculateBondAngle()
    {
	double angle, dotproduct, normpq, normqr;
	double p[] = {0.0, 0.0, 0.0};
	double q[] = {0.0, 0.0, 0.0};
	double r[] = {0.0, 0.0, 0.0};
	double pq[] = {0.0, 0.0, 0.0};
	double qr[] = {0.0, 0.0, 0.0};

	for (int i = 0; i < 3; i++)
	{	
	    p[i] = ((atom)selectedAtomList.elementAt(0)).x[i];
	    q[i] = ((atom)selectedAtomList.elementAt(1)).x[i];
	    r[i] = ((atom)selectedAtomList.elementAt(2)).x[i];

	    pq[i] = p[i] - q[i];	//this order might not be right
	    qr[i] = r[i] - q[i];
	}

	dotproduct = pq[0]*qr[0] + pq[1]*qr[1] + pq[2]*qr[2];
	normpq = Math.sqrt (Math.pow(pq[0], 2) + Math.pow(pq[1], 2) + Math.pow(pq[2], 2));
	normqr = Math.sqrt (Math.pow(qr[0], 2) + Math.pow(qr[1], 2) + Math.pow(qr[2], 2));

	angle = Math.acos (dotproduct / (normpq * normqr));
	angle = angle / Math.PI * 180;		//convert to degrees
	return angle;
    }

    protected double[] crossProduct (double[] x, double[] y)
    {
	double[] z = new double[3];
	z[0] = x[1] * y[2] - x[2] * y[1];
	z[1] = x[2] * y[0] - x[0] * y[2];
	z[2] = x[0] * y[1] - x[1] * y[0];
	return z;
    }
    protected double dotProduct (double[] x, double[] y)
    {
	return x[0] * y[0] + x[1] * y[1] + x[2] * y[2];
    }

    // Jeff Stenhouse 02/03/03
    public double calculateDihedral()
    {
	double dihedral, dp2, dotProduct;
	double pq[] = {0.0, 0.0, 0.0};
	double qr[] = {0.0, 0.0, 0.0};
	double rs[] = {0.0, 0.0, 0.0};
	double normal[] = {0.0, 0.0, 0.0};
	double normal2[] = {0.0, 0.0, 0.0};

	for (int i = 0; i < 3; i++)
	{	
	    pq[i] = ((atom)selectedAtomList.elementAt(0)).x[i] - ((atom)selectedAtomList.elementAt(1)).x[i];
	    qr[i] = ((atom)selectedAtomList.elementAt(1)).x[i] - ((atom)selectedAtomList.elementAt(2)).x[i];
	    rs[i] = ((atom)selectedAtomList.elementAt(2)).x[i] - ((atom)selectedAtomList.elementAt(3)).x[i];
	}

	normal = crossProduct(pq,qr);
	normal2 = crossProduct(qr,rs);
	dotProduct = dotProduct(normal,normal); // length^2 of normal
	dp2 = dotProduct(normal2,normal2); // length^2 of normal2
	dihedral = dotProduct(normal,normal2); // |normal|*|normal2|*cos(theta)
	dihedral = dihedral/(Math.sqrt(dotProduct*dp2)); // normalizing
	dihedral = Math.acos(dihedral);
	double direction = dotProduct(crossProduct(normal,normal2),((atom)selectedAtomList.elementAt(1)).x);
	if (direction < 0) dihedral = -dihedral;
	return 180 * dihedral / Math.PI;
	}

    // Written by Jeff Stenhouse on 13 Jan 02
    // Adds hydrogens based on valences.  For the lazy people :)
    public void addHydrogens()
    {
	updateUndo();
/*	isUFF = false;
	ffSelect.select(1);
	newFF = true;
*/	for (int i = 0; i < grp.atomList.size(); i++)
	{   atom currAtom = ((atom)grp.atomList.elementAt(i));
	    int atomNumber = currAtom.atomicNumber();
/*	    if(atomNumber == 13 || (atomNumber > 20 && atomNumber < 33) ||
		(atomNumber > 38 && atomNumber < 52) ||
		(atomNumber > 56 && atomNumber < 85) || atomNumber > 88)
	    {	isUFF = true;
		ffSelect.select(2);
	    }
*/	    if((currAtom.correctNumBonds() > currAtom.currentNumBonds()) &&
		atomNumber != 1)
	    {
		int numHydro = currAtom.correctNumBonds() - currAtom.currentNumBonds();
		for (int k = 0; k < numHydro; k++)
		{
                    GeneralAtom newAtom = new GeneralAtom();
                    newAtom.setName("Hydrogen");
                    newAtom.setSymbol("H");
                    newAtom.setAtomicNumber(1);
                    newAtom.setMass(1.007);
                    newAtom.setColor(new Color(255, 255, 255));
                    newAtom.setCovalentRadius(.3);
                    newAtom.setVdwEnergy(.382);
                    newAtom.setVdwRadius(1.2);
                    newAtom.setCorrectNumBonds(1);
		    double pos[] = new double[numHydro*3];
		    Random rn = new Random(System.currentTimeMillis()*(currAtom.atomicNumber()+(k*i*i*i+(k+1))));
		    pos[3*k+0] = rn.nextGaussian();
		    pos[3*k+1] = rn.nextGaussian();
		    pos[3*k+2] = rn.nextGaussian();
		    double length = Math.sqrt(pos[3*k+0]*pos[3*k+0]+
					pos[3*k+1]*pos[3*k+1]+pos[3*k+2]*pos[3*k+2]);
		    pos[3*k+0] = pos[3*k+0]*1.1/length + currAtom.x[0];
                    pos[3*k+1] = pos[3*k+1]*1.1/length + currAtom.x[1];
                    pos[3*k+2] = pos[3*k+2]*1.1/length + currAtom.x[2];
		    grp.addAtom(newAtom, pos[3*k+0], pos[3*k+1], pos[3*k+2]);
		    grp.addBond(newAtom, currAtom);
		}
	    }
	}
	energyMinimize(false);
    }

    public void updateUndo()
    {
	undoGroup.copy(grp);
    }  

    //lixh_3_4_05
    public String GaussianOutput(String molInfo)
    {
    	String templateTop = "%nprocshared=1\n"+
    	"#P RHF/6-31g* Opt gfinput gfprint iop(6/7=3)" +
		" scf=conventional\n" + " \n" +
		"Gaussian Template from GridChem\n" +
		"Molecule Built by NCSA-Nanocad \n" + " \n" +
		"0 1\n";
    	//ArrayList molInfsplit = (ArrayList) Arrays.asList(molInfo.split("\n"));
    	
    	//added by Shashank and Sandeep@ CCS,UKY
    	if(stuffInside.selectedGUI==1)
    	{   // G03InputGUI selected.. default template should be null
    		templateTop="";
    	}
    	
    	
    	
    	
    	
    	String text = templateTop;
    	StringTokenizer molTok = new StringTokenizer(molInfo, "\n");
        //int n = molInfsplit.size();
        //int i;
        // ignore first line of molInfo
    	String line = molTok.nextToken();
    	int i = 0;
    	while (molTok.hasMoreTokens())
    	{
    		line = molTok.nextToken();
    		if ((line.length() > 0) && (i > 0))
    		{
    			text = text + line + "\n";
    			System.err.println(line);
    		}
    		i++;
    	}
    	return text;
    }
    
    public String NWchemOutput(String molInfo)
    {
    	String templateTop =  "start \n"+"\n";
    	String templateBottom = "task scf optimize \n" + "\n";
    	
    	String text = templateTop;
    	text = text + "geometry units angstroms \n";
    	StringTokenizer molTok = new StringTokenizer(molInfo, "\n");
        //int n = molInfsplit.size();
        //int i;
        // ignore first line of molInfo
    	String line = molTok.nextToken();
    	int i = 0;
    	while (molTok.hasMoreTokens())
    	{
    		line = molTok.nextToken();
    		if ((line.length() > 0) && (i > 0))
    		{
    			text = text + line + "\n";
    			System.err.println(line);
    		}
    		i++;
    	}
    	text = text + "end \n"+"\n";
    	text = text + templateBottom;
    	
    	return text;
    }
    
    public String MolproOutput(String molInfo)
    {
    	String text;
    	String templateTop="***,GridChem Molpro (SCF,MP2,CASSCF and properties) Template Title\n"+
    	"memory,500,m \n"+
    	"basis=aug-cc-pVDZ \n" +
    	"gprint,basis,orbital,civector; \n";
    	 
    	
    	text = templateTop;
    	text = text + "geometry={ \n";
    	StringTokenizer molTok = new StringTokenizer(molInfo, "\n");
        //int n = molInfsplit.size();
        //int i;
        // ignore first line of molInfo
    	String line = molTok.nextToken();
    	int i = 0;
    	while (molTok.hasMoreTokens())
    	{
    		line = molTok.nextToken();
    		if ((line.length() > 0) && (i > 0))
    		{
    			text = text + line + "\n";
    			System.err.println(line);
    		}
    		i++;
    	}
    	text = text + " } \n";
    	String templateBottom;
    	templateBottom="hf \n"+
                "optg \n"+
        "method,diis       !Optimization method: Geometry DIIS \n" +
    	"mp2         !mp2 calculation \n" +
    	"forces      !mp2 gradients \n" +
   	 "property {       !call property program \n" +
   	 "orbital  !read mp2 orbitals \n" +
   	 "density  !read mp2 density \n " +
   	 "dm   !compute dipole moments and print orbital contributions \n" +
   	 "qm     } !compute quadrupole moments and print orbital contributions \n" +
     "{multi;state,2;dm  !do full-valence CASSCF \n" +
     "natorb,state=1.1   !compute natural orbitals for state 1.1 \n" +
     "natorb,state=2.1}  !compute natural orbitals for state 2.1 \n" +
     "{property          !call property program \n" +
     "orbital,state=1.1  !read casscf natural orbitals for state 1.1 \n" +
     "density,state=1.1  !read casscf density matrix for state 1.1 \n" +
     "dm     !compute dipole moments and print orbital contributions \n" +
     "qm}    !compute quadrupole moments and print orbital contributions \n" +
     "forces !casscf gradient \n";  
    	//text = "It has not been supported yet. \n" 
    		//+ "Please type input by yourself";
    	text = text+templateBottom;
    	return text;
    }
    
    public String GamessOutput(String molInfo)
    {
	String templateTop =
	    " $CONTRL SCFTYP=RHF RUNTYP=OPTIMIZE NZVAR=5 $END\n" +
	    " $SYSTEM TIMLIM=20 MEMORY=500000 $END\n" +
	    " $DFT    DFTTYP=SVWN $END\n" +
	    " $BASIS  GBASIS=N31 NGAUSS=6 NDFUNC=1 $END\n" +
	    " $GUESS  GUESS=HUCKEL $END\n" +
	    " $STATPT OPTTOL=0.00001 $END\n" +
	    " $DATA\n" +
	    "Acetylene geometry optimization in internal coordinates\n" +
	    "Dnh      4\n";

	String templateBottom =
	    " $END\n" +
	    " $ZMAT  IZMAT(1)=1,1,2,   1,1,3,   1,2,4,\n" +
	    "                 5,1,2,4,    5,2,1,3  $END\n" +
	    "------- XZ is 1st plane for both bends -------\n" +
	    " $LIBE  APTS(1)=1.0,0.0,0.0,1.0,0.0,0.0 $END\n";

		if(stuffInside.selectedGUI == 1)
		{
			templateTop = templateBottom = "";
		}
        String text = templateTop;
        StringTokenizer molTok = new StringTokenizer(molInfo, "\n");
        // ignore first line of molInfo
        String line = molTok.nextToken();
        int i = 0;
        while (molTok.hasMoreTokens())
        {
        	line = molTok.nextToken();
        	if ((line.length() > 0) && (i > 0))
        	{
        		StringTokenizer lineTok = new StringTokenizer(line);
        		int j = 0;
        		while (lineTok.hasMoreTokens())
        		{
        			String word = lineTok.nextToken();
        			text = text + " " + word;
        			System.err.println(word);
        			if (j == 0)
        			{
        				int atomnum = lookupAtomNum(word);
        				text = text + " " + atomnum;
        				System.err.println(atomnum);
        			}
        			j++;
			}
        		text = text + "\n";
        		System.err.println(line);
        	}
        	i++;
        }
        text = text + templateBottom;
        return text;
    }

    public int lookupAtomNum(String atomname)
    {
        int ressnum= 0;
	if (atomname.equals("H"))  { ressnum = 1; }
	else if (atomname.equals("HE"))  { ressnum = 2; }
	else if (atomname.equals("LI"))  { ressnum = 3; }
	else if (atomname.equals("BE"))  { ressnum = 4; }
	else if (atomname.equals("B"))  { ressnum = 5; }
	else if (atomname.equals("C"))  { ressnum = 6; }
	else if (atomname.equals("N"))  { ressnum = 7; }
	else if (atomname.equals("O"))  { ressnum = 8; }
	else if (atomname.equals("F"))  { ressnum = 9; }
	else if (atomname.equals("NE"))  { ressnum = 10; }
	else if (atomname.equals("NA"))   { ressnum = 11; }
	else if (atomname.equals("MG"))   { ressnum = 12; }
	else if (atomname.equals("AL"))   { ressnum = 13; }
	else if (atomname.equals("SI"))   { ressnum = 14; }
	else if (atomname.equals("P"))   { ressnum = 15; }
	else if (atomname.equals("S"))   { ressnum = 16; }
	else if (atomname.equals("CL"))   { ressnum = 17; }
	else if (atomname.equals("AR"))   { ressnum = 18; }
	else if (atomname.equals("K"))   { ressnum = 19; }
	else if (atomname.equals("CA"))   { ressnum = 20; }
	else if (atomname.equals("SC"))   { ressnum = 21; }
	else if (atomname.equals("TI"))   { ressnum = 22; }
	else if (atomname.equals("V"))   { ressnum = 23; }
	else if (atomname.equals("CR"))   { ressnum = 24; }
	else if (atomname.equals("MN"))   { ressnum = 25; }
	else if (atomname.equals("FE"))   { ressnum = 26; }
	else if (atomname.equals("CO"))   { ressnum = 27; }
	else if (atomname.equals("NI"))   { ressnum = 28; }
	else if (atomname.equals("CU"))   { ressnum = 29; }
	else if (atomname.equals("ZN"))   { ressnum = 30; }
	else if (atomname.equals("GA"))   { ressnum = 31; }
	else if (atomname.equals("GE"))   { ressnum = 32; }
	else if (atomname.equals("AS"))   { ressnum = 33; }
	else if (atomname.equals("SE"))   { ressnum = 34; }
	else if (atomname.equals("BR"))   { ressnum = 35; }
	else if (atomname.equals("KR"))   { ressnum = 36; }
	else if (atomname.equals("RB"))   { ressnum = 37; }
	else if (atomname.equals("SR"))   { ressnum = 38; }
	else if (atomname.equals("Y"))   { ressnum = 39; }
	else if (atomname.equals("ZR"))   { ressnum = 40; }
	else if (atomname.equals("NB"))   { ressnum = 41; }
	else if (atomname.equals("MO"))   { ressnum = 42; }
	else if (atomname.equals("TC"))   { ressnum = 43; }
	else if (atomname.equals("RU"))   { ressnum = 44; }
	else if (atomname.equals("RH"))   { ressnum = 45; }
	else if (atomname.equals("PD"))   { ressnum = 46; }
	else if (atomname.equals("AG"))   { ressnum = 47; }
	else if (atomname.equals("CD"))   { ressnum = 48; }
	else if (atomname.equals("IN"))   { ressnum = 49; }
	else if (atomname.equals("SN"))   { ressnum = 50; }
	else if (atomname.equals("SB"))   { ressnum = 51; }
	else if (atomname.equals("TE"))   { ressnum = 52; }
	else if (atomname.equals("I"))   { ressnum = 53; }
	else if (atomname.equals("XE"))   { ressnum = 54; }
	else if (atomname.equals("CS"))   { ressnum = 55; }
	else if (atomname.equals("BA"))   { ressnum = 56; }
	else if (atomname.equals("LA"))   { ressnum = 57; } 
	else if (atomname.equals("CE"))   { ressnum = 58; }
	else if (atomname.equals("PR"))   { ressnum = 59; }
	else if (atomname.equals("ND"))   { ressnum = 60; }
	else if (atomname.equals("PM"))   { ressnum = 61; }
	else if (atomname.equals("SM"))   { ressnum = 62; }
	else if (atomname.equals("EU"))   { ressnum = 63; }
	else if (atomname.equals("GD"))   { ressnum = 64; }
	else if (atomname.equals("TB"))   { ressnum = 65; }
	else if (atomname.equals("DY"))   { ressnum = 66; }
	else if (atomname.equals("HO"))   { ressnum = 67; }
	else if (atomname.equals("ER"))   { ressnum = 68; }
	else if (atomname.equals("TM"))   { ressnum = 69; }
	else if (atomname.equals("YB"))   { ressnum = 70; }
	else if (atomname.equals("LU"))   { ressnum = 71; }
	else if (atomname.equals("HF"))   { ressnum = 72; }
	else if (atomname.equals("TA"))   { ressnum = 73; }
	else if (atomname.equals("W"))   { ressnum = 74; }
	else if (atomname.equals("RE"))   { ressnum = 75; }
	else if (atomname.equals("OS"))   { ressnum = 76; }
	else if (atomname.equals("IR"))   { ressnum = 77; }
	else if (atomname.equals("PT"))   { ressnum = 78; }
	else if (atomname.equals("AU"))   { ressnum = 79; }
	else if (atomname.equals("HG"))   { ressnum = 80; }
	else if (atomname.equals("TI"))   { ressnum = 81; }
	else if (atomname.equals("PB"))   { ressnum = 82; }
	else if (atomname.equals("BI"))   { ressnum = 83; }
	else if (atomname.equals("PO"))   { ressnum = 84; }
	else if (atomname.equals("AT"))   { ressnum = 85; }
	else if (atomname.equals("RN"))   { ressnum = 86; }
	else if (atomname.equals("FR"))   { ressnum = 87; }
	else if (atomname.equals("RA"))   { ressnum = 88; }
	else if (atomname.equals("AC"))   { ressnum = 89; }
	else if (atomname.equals("TH"))   { ressnum = 90; }
	else if (atomname.equals("PA"))   { ressnum = 91; }
	else if (atomname.equals("U"))   { ressnum = 92; }
	else if (atomname.equals("NP"))   { ressnum = 93; }
	else if (atomname.equals("PU"))   { ressnum = 94; }
	else if (atomname.equals("AM"))   { ressnum = 95; }
	else if (atomname.equals("CM"))   { ressnum = 96; }
	else if (atomname.equals("BK"))   { ressnum = 97; }
	else if (atomname.equals("CF"))   { ressnum = 98; }
	else if (atomname.equals("ES"))   { ressnum = 99; }
	else if (atomname.equals("FM"))   { ressnum = 100; }
	else if (atomname.equals("MD"))   { ressnum = 101; }
	else if (atomname.equals("NO"))   { ressnum = 102; }
	else if (atomname.equals("LR"))   { ressnum = 103; }
	else if (atomname.equals("RF"))   { ressnum = 104; }
	else if (atomname.equals("DB"))   { ressnum = 105; }
	else if (atomname.equals("SG"))   { ressnum = 106; }
	else if (atomname.equals("BH"))   { ressnum = 107; }
	else if (atomname.equals("HS"))   { ressnum = 108; }
	else if (atomname.equals("MT"))   { ressnum = 109; }
	return  ressnum;
    }
    
}

