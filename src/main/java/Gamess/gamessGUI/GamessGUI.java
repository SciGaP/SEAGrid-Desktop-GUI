/*Copyright (c) 2007, Center for Computational Sciences, University of Kentucky.  All rights reserved.

Developed by:

Center for Computational Sciences, University of Kentucky

http://www.ccs.uky.edu

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
3. Neither the names of Center for Computational Sciences, University of Kentucky 
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
 * @author Michael Sheetz 
 * @author Pavithra Koka
 * @author Shreeram Sridharan
 */

package Gamess.gamessGUI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import Gamess.gamessGUI.Dialogs.HelpDisplayer;
import Gamess.gamessGUI.Dialogs.MolecularSpecification;
import Gamess.gamessGUI.IncompatibilityPackage.ExcludeIncompatibility;
import Gamess.gamessGUI.IncompatibilityPackage.RequiresIncompatibility;
import Gamess.gamessGUI.InputFileHandlers.InputFileWriter;
import Gamess.gamessGUI.Storage.Repository;
/*import Gamess.gamessGUI.IncompatibilityPackage.ExcludeIncompatibility;
import Gamess.gamessGUI.IncompatibilityPackage.IncompatibilityGetter;
import Gamess.gamessGUI.IncompatibilityPackage.RequiresIncompatibility;
import Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic.And;
import Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic.Condition;
import Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic.ConditionBuilder;
import Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic.Not;
import Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic.Or;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.AvailableOnlyIfRestriction;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.ExcludeRestriction;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.ExcludedIfRestriction;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.RequiredIfRestriction;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.RequiresRestriction;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.RestrictionsHolder;
import Gamess.gamessGUI.InputFileHandlers.InputFileReader;
import Gamess.gamessGUI.InputFileHandlers.InputFileWriter;
import Gamess.gamessGUI.InputFileHandlers.RWGetter;
import Gamess.gamessGUI.Storage.IDBChangeListener;
import Gamess.gamessGUI.Storage.Repository;
*/
import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Element;

import org.gridchem.client.util.Env;

//import javax.swing.plaf.metal.MetalLookAndFeel;
//import javax.swing.plaf.metal.MetalTheme;

import java.io.*;
import java.util.*;


public class GamessGUI extends JFrame{

	private static final long serialVersionUID = -2311360325546782256L;
	//GUI COMPONENTS
	public static JFrame frame;
	private static JMenuBar menuBar ;
      
	public static JMenuItem newMenu,openMenu,saveMenu,saveAsMenu, importMenu,exportMenu,closeMenu;
    public static JMenuItem undoMenu,redoMenu,cutMenu,copyMenu,pasteMenu;

    public static JTextArea msgDisplayArea;
    public static JTextPane inputFilePane;
    
    public static JPanel menuPanel,textPanel,mainPanel,msgDisplayPanel,basePanel,submitButtonPanel,molDisplayPanel;
     
    public static JButton submitInputFile;
    public static JButton saveInputFile;
    public static JButton updateInputFile;
  
    ActionListener fileMenuListener , editMenuListener;
    
    /* VARIBLES FOR OTHER CLASSES */
    public static MolecularSpecification molSpec;
    public MolDisplay molDisp;
    
    TitledBorder textPanelBorder = null;
    
    public GamessGUI(String title)
    {
    	this();
    	setTitle(title);
    }
   	  	
    /* CONSTRUCTOR */
    public GamessGUI() 
    {
    	menuBar = new JMenuBar();
    	setJMenuBar(menuBar);
    	   	
    	//Change the menu from lightweight to heavyweight
    	JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        createInputFileDisplayPanel();
        createMolDisplayPanel();
        createSubmitButtonPanel();
        createBasePanel();

    	fileMenuListener = new FileMenuListener(this, inputFilePane , textPanelBorder , GlobalParameters.undoRedoHandle ,true);
    	editMenuListener = new EditMenuListener(inputFilePane , GlobalParameters.undoRedoHandle);
        
        createFileMenu();
        createEditMenu();


//      Creating Global Document
        try
        {
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setIgnoringComments(true);
			dbFactory.setValidating(false);
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder = dbFactory.newDocumentBuilder();

			GlobalParameters.doc = builder.parse(new File( Env.getApplicationDataDir() + Env.getFileSeparator() + "GamessFiles" + Env.getFileSeparator() + "Incompatibility.xml"));
			GlobalParameters.userNotesAndToolTip = builder.parse(new File( Env.getApplicationDataDir() + Env.getFileSeparator() + "GamessFiles" + Env.getFileSeparator() + "UserNotesAndToolTips.xml"));
        }
        catch(Exception ex)
        {
        	
        }
        
        ContentAssist.CreateInstance();
        
        Hashtable<String, JDialog> CustomDialogs = new Hashtable<String, JDialog>();
        CustomDialogs.put("Data", molSpec = new MolecularSpecification(molDisp , frame));
        CustomDialogs.put("Help", new HelpDisplayer(frame , GlobalParameters.HelpFile));
        /* functions to populate the different menus in the Main menu bar*/
        MenuBuilder mb =  new MenuBuilder(menuBar , this , GlobalParameters.GamessMenuNew , CustomDialogs);
        mb.BuildMenu();
        
        
        /*try
        {
	        DOMSource ds = new DOMSource(Dictionary.organizedDoc);
	        StreamResult sr = new StreamResult(new File("OrganizedInput.xml"));
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer trans = tf.newTransformer();
	        trans.transform(ds, sr);
        }
        catch(TransformerException e){e.printStackTrace();}*/
        
        inputFilePane.setFont(new Font("Courier" , Font.PLAIN , 12));
        inputFilePane.getDocument().addUndoableEditListener(GlobalParameters.undoRedoHandle);
        ((AbstractDocument)inputFilePane.getStyledDocument()).setDocumentFilter(new InputFileFilter(inputFilePane));
//*/
        /*
        RWGetter.loadHandleDetails();
        /*InputFileWriter ifw = InputFileWriter.getInstance();
        ifw.Write("SYSTEM", "PARALLEL=.True.");
        ifw.Write("SYSTEM", "TILIM=RHF");
        ifw.Write("CONTROL", "PARALLEL=.True.");
        ifw.Write("SYSTEM", "PARALLEL=.FALSE.");
        ifw.Remove("SYSTEM", "PARALLEL=.FALSE.");
        
        ifw.Write("LIBE", "APTS(1)=1,3 ,1,2");
        ifw.Write("LIBE", "APTS(1)=1,2 ,1,2,3");
        ifw.Remove("LIBE", "APTS(1)");
        
        ifw.Write("DATA", "C1 1\nC\nH 1");
        ifw.Write("DATA", "C1 1\nC\nH 1 1.09");
        ifw.Remove("DATA", " dhdghds ");
        
        ifw.Write("TDHFX", "USE_Q");
        ifw.Write("TDHFX", "FREQ");
        ifw.Write("TDHFX", "FREE 0.02");
        ifw.Write("TDHFX", "ALLDIRS");
        ifw.Write("TDHFX", "HRAMAN 0.02");                
        ifw.Write("TDHFX", "FREE 0.02 0.03");
        ifw.Write("TDHFX", "FREE 0.02 0.03 0.04");
        ifw.Write("TDHFX", "FREE 0.01 0.02");
        ifw.Write("TDHFX", "USE_C");
        ifw.Remove("TDHFX", "USE_Q");
        //ifw.Remove("TDHFX", "HRAMAN");
        
        InputFileReader ifr = InputFileReader.getInstance();
        System.out.println(ifr.Read("SYSTEM" , "PARALLEL" ));*/
        
        //Storage Testing
        Repository db = Repository.getInstance();
        db.registerDBChangeListener(ExcludeIncompatibility.getInstance());
        db.registerDBChangeListener(RequiresIncompatibility.getInstance());
        db.registerDBChangeListener(MessageBox.notes);
        
        GlobalParameters.switchToProvisionalMode();
        db = Repository.getInstance();
        db.registerDBChangeListener(ExcludeIncompatibility.getInstance());
        db.registerDBChangeListener(RequiresIncompatibility.getInstance());
        db.registerDBChangeListener(MessageBox.notes);
        
        GlobalParameters.switchToNormalMode();
        /*db = Repository.getInstance();
        //
        
        db.Store("SYSTEM", "PARALLEL=.FALSE.");
        db.Store("SYSTEM", "TILIM=RHF");
        GlobalParameters.switchToProvisionalMode();
        db = Repository.getInstance();
        System.out.println(db.equal("SYSTEM" , "PARALLEL=.FALSE."));
        db.Store("CONTROL", "PARALLEL=.FALSE.");
        db.Overwrite("SYSTEM", "SCFTYP=RHF PARALLEL=.TRUE. MPLEVEL=2");
        GlobalParameters.switchToNormalMode();
        db = Repository.getInstance();
        System.out.println(db.Retrieve("SYSTEM"));
        System.out.println(db.Retrieve("SYSTEM","PARALLEL"));
        
        db.Store("TDHFX", "ITERMAX 100");
        db.Store("TDHFX", "USE_Q");
        db.Store("TDHFX", "ALLDIRS");
        db.Remove("TDHFX","USEQ");
        db.Overwrite("TDHFX", "USE_C \n USE_Q  \n ITERMAX 110");
        System.out.println(db.equal("TDHFX", "USE_Q"));
        System.out.println(db.isAvailable("TDHFX" , "USE_Q"));
        System.out.println(db.Retrieve("TDHFX"));
        System.out.println(db.Retrieve("TDHFX","ITERMAX"));
        
        db.Store("DATA", "C\nH 1 1.09");
        db.Remove("DATA", " ");
        db.Overwrite("DATA", "C\nH 1 1.01");
        System.out.println(db.equal("DATA","\nC\nH 1 1.09\n"));
        System.out.println(db.isAvailable("DATA","adfa"));
        System.out.println(db.isAvailable("DATA"));
        System.out.println(db.Retrieve("DATA" , "vsfs"));
        
        db.Store("ZMAT", "IZMAT=1,2 ,1,2,3");
        db.Store("ZMAT", "IRZMAT = 1,4,3");
        //db.Remove("ZMAT");
        db.Remove("ZMAT", "ICZMAT");
        System.out.println(db.equal("ZMAT" , "IRZMAT=1,4,3"));
        db.Store("LIBE", "PARALLEL=.FALSE.");
        db.Overwrite("ZMAT", "IZMAT=1,2 ,1,2,3 IXZMAT = 1,4 ,421");
        System.out.println(db.Retrieve("ZMAT"));
        System.out.println(db.Retrieve("ZMAT","IXZMAT"));
        /*/
        /*/Condition Testing 
        Condition cond1 = new And("SYSTEM MPLEVEL 3");
        //Condition cond1 = ConditionBuilder.buildCondition("SYSTEM MPLEVEL 3");
        cond1.add("LIBE PARALLEL .FALSE.");
        System.out.println(cond1.test());
        
        Condition cond2 = new Or("SYSTEM MPLEVEL 2");
        //Condition cond2 = ConditionBuilder.buildCondition("SYSTEM MPLEVEL 3");
        cond2.add("LIBE PARALLEL .TRUE.");
        cond2.add(cond1);
        System.out.println(cond2.test());
        
        Condition cond3 = new Not();
        cond3.add(cond1);
        System.out.println(cond3.test());
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        InputSource inputSource = null;
		try {
			inputSource=new InputSource(new FileInputStream("Incompatibility.xml"));
			Node node = (Node)xpath.evaluate("/root/Not[Entity[@Group and @Keyword and not(@Value)]]", inputSource , XPathConstants.NODE);
			Condition andCond = ConditionBuilder.buildCondition(node);
			andCond = ConditionBuilder.buildCondition(node);
			System.out.println(andCond.test());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//
		ExcludeRestriction er = new ExcludeRestriction();
		er.buildList("SYSTEM MPLEVEL");
		//er.getExcludeList();
        RequiresRestriction er1 = new RequiresRestriction();
		er1.buildList("SYSTEM MPLEVEL 2");
        ExcludedIfRestriction er2 = new ExcludedIfRestriction();
        er2.buildList("LIBE PARALLEL");
        //er.buildList("*");
        RequiredIfRestriction er3 = new RequiredIfRestriction();
        er3.buildList("LIBE PARALLEL");
        //er.buildList("*");
        AvailableOnlyIfRestriction er4 = new AvailableOnlyIfRestriction();
        er4.buildList("LIBE PARALLEL");
        //er.buildList("*");
        RestrictionsHolder rh = IncompatibilityGetter.getIncompatibility("LIBE PARALLEL");
        IDBChangeListener incomp = ExcludeIncompatibility.getInstance();
        incomp.DataAdded("SYSTEM MPLEVEL 2");
        incomp.DataRemoved("SYSTEM MPLEVEL 2");
        
        incomp = RequiresIncompatibility.getInstance();
        incomp.DataAdded("SYSTEM MPLEVEL 2");
        incomp.DataRemoved("SYSTEM MPLEVEL 2");
        /*/
        
        /*/
        //Testing MenuTable
        XPath xpath = XPathFactory.newInstance().newXPath();
        InputSource inputSource = null;
        Node node = null;
		try {
			inputSource=new InputSource(new FileInputStream("GamessMenuNew.xml"));
			node = (Node)xpath.evaluate("//MenuItem[@DisplayName='Geometry']/GroupFrame[@DisplayName='Geometry']", inputSource , XPathConstants.NODE);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        MenuTableDialog menuTable = new MenuTableDialog("SYSTEM" , "SYSTEM" ,node , frame);
		/*/
        //frame.setVisible(true);
        setVisible(true);
        //ToolTipManager.sharedInstance().setDismissDelay(10000);
        //menuTable.setVisible(true);
        inputFilePane.addMouseMotionListener(new ToolTipDisplayer());
        try
        {
        	molDisp.setSelected(true);
        }
        catch(Exception exp)
        {}
        
        //Initial values set
        UndoRedoHandler.setLock();
        
        /*InputFileWriter.getInstance().Write("SYSTEM", "PARALL=.TRUE.");
        Repository.getInstance().Store("SYSTEM", "PARALL=.TRUE.");
        
        InputFileWriter.getInstance().Write("SYSTEM", "TIMLIM=1");
        Repository.getInstance().Store("SYSTEM", "TIMLIM=1");
        
        InputFileWriter.getInstance().Write("SYSTEM", "MEMDDI=1");
        Repository.getInstance().Store("SYSTEM", "MEMDDI=1");
        */
        UndoRedoHandler.releaseLock();
        
        //MessageBox.notes.write("The above values are used for running the computation parallelly");
        /*MessageBox.notes.write("message1");
        MessageBox.notes.write("message2");
        MessageBox.notes.write("message3");
        MessageBox.notes.write("message4");*/
        
        MessageBox.excludes.UpdateList();
        MessageBox.requires.UpdateList();
        
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
    }
   
    public void createFileMenu(){
    	
    	JMenu fileMenu = new JMenu("File");
    	menuBar.add(fileMenu);
    	
    	newMenu=new JMenuItem("New");
    	newMenu.addActionListener(fileMenuListener);
    	fileMenu.add(newMenu);
    	
    	openMenu=new JMenuItem("Open");
    	openMenu.addActionListener(fileMenuListener);
    	fileMenu.add(openMenu);
    	
    	saveMenu=new JMenuItem("Save");
    	saveMenu.addActionListener(fileMenuListener);
    	fileMenu.add(saveMenu);
    	
    	saveAsMenu=new JMenuItem("Save As");
    	saveAsMenu.addActionListener(fileMenuListener);
    	fileMenu.add(saveAsMenu);
    	
    	fileMenu.addSeparator();
    	
    	importMenu=new JMenuItem("Import");
    	importMenu.addActionListener(fileMenuListener);
    	fileMenu.add(importMenu);
    	importMenu.setEnabled(false);
    	
    	exportMenu=new JMenuItem("Export");
    	exportMenu.addActionListener(fileMenuListener);
    	fileMenu.add(exportMenu);
    	exportMenu.setEnabled(false);
    	
    	fileMenu.addSeparator();
    	
    	closeMenu=new JMenuItem("Close");
    	closeMenu.addActionListener(fileMenuListener);
    	fileMenu.add(closeMenu);
    	
    }
    
    public void createEditMenu(){
    	JMenu editMenu =new JMenu("Edit");
    	menuBar.add(editMenu);
    	
    	undoMenu= new JMenuItem("Undo");
    	undoMenu.addActionListener(editMenuListener);    	
    	editMenu.add(undoMenu);
    	
    	redoMenu= new JMenuItem("Redo");
    	redoMenu.addActionListener(editMenuListener);
    	editMenu.add(redoMenu);
    	
    	editMenu.addSeparator();
    	
    	cutMenu= new JMenuItem("Cut");
    	cutMenu.addActionListener(editMenuListener);
    	editMenu.add(cutMenu);
    	
    	copyMenu= new JMenuItem("Copy");
    	copyMenu.addActionListener(editMenuListener);
    	editMenu.add(copyMenu);
    	
    	pasteMenu= new JMenuItem("Paste");
    	pasteMenu.addActionListener(editMenuListener);
    	editMenu.add(pasteMenu);
    	
    	
    	JMenuItem updateAll = new JMenuItem("Update All");
    	updateAll.setActionCommand("Update All");
    	updateAll.addActionListener(UpdateInputFile.getInstance());
    	editMenu.add(updateAll);
    }
    
    //FUNCTION TO CREATE TEXTPANE TO DISPLAY THE INPUT FILE

    void createInputFileDisplayPanel()
    {
    	textPanel =new JPanel();
    	textPanel.setLayout(new BorderLayout());
    	
    	inputFilePane =new JTextPane();
    	inputFilePane.setEditable(true);
    	
    	textPanelBorder = new TitledBorder("INPUT FILE"+" - untitled");    	
    	textPanelBorder.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,1));
    	
    	JScrollPane scrpane=new JScrollPane(inputFilePane);
    	scrpane.setPreferredSize(new Dimension(getCurrentResolutionWidth(624), getCurrentResolutionHeight(470)));
    	scrpane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,2));
    	scrpane.setAutoscrolls(true);
     
    	textPanel.add(scrpane);
    	textPanel.setBorder(textPanelBorder);
   
    }
    
    void createMolDisplayPanel()
    {
       	molDisp = new MolDisplay();
    }
    
    
    
    void createSubmitButtonPanel()
    {
       	submitButtonPanel= new JPanel(new FlowLayout());
       	
       	submitInputFile = new JButton("SUBMIT");
       	updateInputFile = new JButton("UPDATE");
       	saveInputFile = new JButton("SAVE");
       	
   		submitInputFile.addActionListener(new checkInputFile());
   		updateInputFile.addActionListener(UpdateInputFile.getInstance());
   		saveInputFile.addActionListener(fileMenuListener);
   		
   		saveInputFile.setActionCommand("Save");
   		
   		submitButtonPanel.add(updateInputFile);
   		submitButtonPanel.add(submitInputFile);
   		submitButtonPanel.add(saveInputFile);
   		
   		updateInputFile.setEnabled(false);
    }
        
    void createBasePanel()
    {
    	SpringLayout centralLayout = new SpringLayout();
    	JPanel centerPanel= new JPanel( centralLayout );
    	centerPanel.add(textPanel);
    	centerPanel.add(molDisp);
    	
    	//Row constraints
    	centralLayout.putConstraint(SpringLayout.WEST , textPanel , 2 , SpringLayout.WEST , centerPanel);
    	centralLayout.putConstraint(SpringLayout.WEST , molDisp , 2 , SpringLayout.EAST , textPanel);
    	centralLayout.putConstraint(SpringLayout.EAST , centerPanel , 2 , SpringLayout.EAST , molDisp);
    	
    	//Column constraints
    	centralLayout.putConstraint(SpringLayout.NORTH , textPanel , 2 , SpringLayout.NORTH , centerPanel);
    	centralLayout.putConstraint(SpringLayout.SOUTH , centerPanel , 2 , SpringLayout.SOUTH , textPanel);
    	centralLayout.putConstraint(SpringLayout.NORTH , molDisp , 10 , SpringLayout.NORTH , centerPanel);
    	centralLayout.putConstraint(SpringLayout.SOUTH , molDisp , -4 , SpringLayout.SOUTH , centerPanel);
    	
    	basePanel=new JPanel(new BorderLayout());
    	
    	basePanel.add(centerPanel, BorderLayout.CENTER);
    	
    	JPanel southPanel = new JPanel();
    	southPanel.setLayout(new BorderLayout());
    	southPanel.add(new MessageBox(), BorderLayout.CENTER);
    	southPanel.add(submitButtonPanel,BorderLayout.SOUTH);
    	southPanel.setPreferredSize(new Dimension( getCurrentResolutionWidth(990) ,  getCurrentResolutionHeight(195)));
    	
    	basePanel.add(southPanel,BorderLayout.SOUTH);
    	
    	getContentPane().add(basePanel,BorderLayout.CENTER);
         
    }

    public static void main(String s[ ]) 
    {
       frame = new GamessGUI("GAMESS Input GUI");
       frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       
       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       screenSize.height -= 32;
       frame.setSize(screenSize);

       frame.setResizable(true);
       frame.setVisible(true);
    }
    
    public static int getCurrentResolutionHeight(int _1024X768_Resolution)
    {
    	return ((Double)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() * ((double)_1024X768_Resolution/768))).intValue() ;
    }
    
    public static int getCurrentResolutionWidth(int _1024X768_Resolution)
    {
    	return ((Double)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() * ((double)_1024X768_Resolution/1024))).intValue() ;
    }
    
    private class ToolTipDisplayer implements MouseMotionListener
    {

		public void mouseDragged(MouseEvent arg0) {}

		public void mouseMoved(MouseEvent e) 
		{
			//Get offset of the text under the mouse pointer
			Point mousePosition = new Point(e.getX(),e.getY());
    		int postionOfMouse = inputFilePane.viewToModel(mousePosition);
    		//Get the element at that offset
    		Element elementAtMousePosition = inputFilePane.getStyledDocument().getCharacterElement(postionOfMouse);
    		
    		//form the rectangle of mouse
    		/*try
    		{
    			//System.out.println(inputFilePane.getText(elementAtMousePosition.getStartOffset(), elementAtMousePosition.getEndOffset() - elementAtMousePosition.getStartOffset()));
    			System.out.println(elementAtMousePosition.getAttributes().getAttribute("TOOLTIP"));
    			Rectangle elementBound = new Rectangle(inputFilePane.modelToView(elementAtMousePosition.getStartOffset()));
    			if(inputFilePane.getText().length() > elementAtMousePosition.getEndOffset())
    				elementBound = elementBound.union(inputFilePane.modelToView(elementAtMousePosition.getEndOffset()));
    			if(!elementBound.contains(mousePosition))
    				inputFilePane.setToolTipText(null);
    			return;
    		}
    		catch(BadLocationException ex){
    			ex.printStackTrace();
    		}*/
    		
    		//Check if the element has any tooltip to be displayed.
    		//if it is then display it
    		String toolTip = null;
    		if( (toolTip = (String)elementAtMousePosition.getAttributes().getAttribute("TOOLTIP") ) != null)
    			inputFilePane.setToolTipText(toolTip);
    		else
    			inputFilePane.setToolTipText(null);
		}
    }
    
    public static void setUpdateMode(boolean UpdateMode)
    {
    	boolean updateStatus = !UpdateMode;
    	updateInputFile.setEnabled(UpdateMode);
    	for (int i = 0; i < menuBar.getMenuCount() ; i++) 
    	{
			JMenu currentMenu = menuBar.getMenu(i);
			if(currentMenu != null && (currentMenu.getText().equalsIgnoreCase("File") || currentMenu.getText().equalsIgnoreCase("Edit") || currentMenu.getText().equalsIgnoreCase("Help")))
				continue;
			if(currentMenu != null)
				currentMenu.setEnabled(updateStatus);
		}
    }
} // end of main class
