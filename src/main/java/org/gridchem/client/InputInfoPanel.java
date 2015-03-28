package org.gridchem.client;

/***
 * This JPanel is provided as a component in Job Editor (editingStuff.java)
 * for preparing inputs for an application
 * @author kimjh
 */

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.Dimension;

import javax.swing.JTabbedPane;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.lang.StringBuilder;


public class InputInfoPanel extends JPanel implements ActionListener, ItemListener{
	   
	private JTabbedPane bottomTabbedPane ;
	private JPanel topFileInfoPanel ; 
	private JSplitPane splitPane ;
	private JButton addInputButton; 

	private String appName = null;
	public ArrayList<File> inputFiles ; 
	public ArrayList<JTextField> inputFileNames;
	private int numInputWithJTextArea = 1;
	public ArrayList<JTextArea> inputTexts;

	private ArrayList<String> inpFileTypesToolTipStrings;
	
	static final String title = "Input File Information";
	private String warnMsgLargeFileDownLoad = "The size of this input file seems to be large. \n Continue to download it?";
	

	//The following should come from the server
	public final String[] INPUT_DESC_LIST = {"Main Input"};   
    public final String[] INPUT_DESC_LIST_AMBER_SANDER = {"mdin","inpcrd","prmtop"};  
    public final String[] INPUT_DESC_LIST_DMOL3 = {"car","input"};
    public final String[] INPUT_DESC_LIST_CASTEP = {"cell","param"};
	
	    // constructor 
	public InputInfoPanel(String appName, ArrayList<String> fileNames, ArrayList<String> inps){
		 this.appName = appName;
		 this.inputFiles = new ArrayList<File>();
	     this.inputFileNames = new ArrayList<JTextField>();
	     this.inputTexts = new ArrayList<JTextArea>();     
 
	     int textAreaRow = 19;
         if (fileNames.size() > 1){
        	 textAreaRow = 14;
         }
	     for (int i = 0; i < fileNames.size(); i++){
			 this.inputFiles.add(new File(fileNames.get(i)));
		     this.inputFileNames.add(new JTextField(fileNames.get(i)));
		     this.inputTexts.add(new JTextArea(inps.get(i), textAreaRow, 35));
		 }
		 
		 initGUI();
	 }
	    	    
	 public void initGUI() {

		 topFileInfoPanel = new JPanel();
	     topFileInfoPanel.setLayout(new GridBagLayout());
	     topFileInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	     GridBagConstraints c = new GridBagConstraints();
	        
	     JLabel fileTypeLabel = new JLabel("File Description", JLabel.CENTER);
	     fileTypeLabel.setBackground(Color.GRAY);
	     fileTypeLabel.setOpaque(true);
	     c.fill = GridBagConstraints.HORIZONTAL;
	     c.gridx = 0;
	     c.gridy = 0;
	     c.insets = new Insets(0,10, 5, 0);
	     c.ipadx = 10;
	     c.ipady = 10;
	     topFileInfoPanel.add(fileTypeLabel,c);
	     JLabel fileNameLabel = new JLabel("File Name", JLabel.CENTER);
	     fileNameLabel.setBackground(Color.GRAY);
	     fileNameLabel.setOpaque(true);
	     c.fill = GridBagConstraints.HORIZONTAL;
	     c.gridx = 1;
	     c.gridy = 0;
	     topFileInfoPanel.add(fileNameLabel,c);        
	     JLabel loadLabel = new JLabel("Load", JLabel.CENTER);	 
	     loadLabel.setBackground(Color.GRAY);
	     loadLabel.setOpaque(true);
	     c.fill = GridBagConstraints.HORIZONTAL;
	     c.gridx = 2;
	     c.gridy = 0;
	     c.ipadx = 5;
	     topFileInfoPanel.add(loadLabel, c);	       

	     JLabel saveLabel = new JLabel("Save", JLabel.CENTER);	 
	     saveLabel.setBackground(Color.GRAY);
	     saveLabel.setOpaque(true);
	     c.fill = GridBagConstraints.HORIZONTAL;
	     c.gridx = 3;
	     c.gridy = 0;
	     c.ipadx = 5;
	     topFileInfoPanel.add(saveLabel, c);	
	     
	     if(this.inputFileNames.size() > this.numInputWithJTextArea){
		     JLabel deleteLabel = new JLabel("Delete", JLabel.CENTER);	 
		     deleteLabel.setBackground(Color.GRAY);
		     deleteLabel.setOpaque(true);
		     c.fill = GridBagConstraints.HORIZONTAL;
		     c.gridx = 4;
		     c.gridy = 0;
		     c.ipadx = 5;
		     topFileInfoPanel.add(deleteLabel, c);	
	     }
	     
	     
	     c.insets = new Insets(0, 10, 0, 0);      
	     
	     if(this.appName.equalsIgnoreCase(Invariants.APP_NAME_AMBER_SANDER)){
	    	 this.numInputWithJTextArea = 3;
	     }else if(this.appName.equalsIgnoreCase(Invariants.APP_NAME_DMOL3)){
	    	 this.numInputWithJTextArea = 2;	        
	     }else if(this.appName.equalsIgnoreCase(Invariants.APP_NAME_CASTEP)){
	    	 this.numInputWithJTextArea = 2;
	     }else {
	    	 this.numInputWithJTextArea = 1;
	     }
	     
	     for (int i = 0 ; i < this.inputFileNames.size() ; i++) {
		     c.fill = GridBagConstraints.HORIZONTAL;
		     c.gridx = 0;
		     c.gridy = i+1;
		     c.ipadx = 5;
		        
		     String fname = this.inputFiles.get(i).getName();
		     String tmpStr = fname.substring(fname.lastIndexOf(".")+1);
		     //later the string will come from the server
		     String inpFileType = null;
		     if(i < this.numInputWithJTextArea){
		    	 if(this.appName.equalsIgnoreCase(Invariants.APP_NAME_AMBER_SANDER)){
		    		 inpFileType = INPUT_DESC_LIST_AMBER_SANDER[i];
		    	 }else if(this.appName.equalsIgnoreCase(Invariants.APP_NAME_DMOL3)){
		    		 inpFileType = INPUT_DESC_LIST_DMOL3[i];
		    	 }else if(this.appName.equalsIgnoreCase(Invariants.APP_NAME_CASTEP)){
		    		 inpFileType = INPUT_DESC_LIST_CASTEP[i];
		    	 }else{
		    		 inpFileType = INPUT_DESC_LIST[0];
		    	 }
		     }else{
		    	 inpFileType = tmpStr;
		     }
		     
		     topFileInfoPanel.add(new JLabel(inpFileType, JLabel.CENTER),c);
		     c.fill = GridBagConstraints.HORIZONTAL;
		     c.gridx = 1;
		     c.gridy = i+1;
		     c.ipadx = 5;

		     this.inputFileNames.get(i).setActionCommand("fileNameJTextField"+i);
		     this.inputFileNames.get(i).addActionListener(this);
		     topFileInfoPanel.add(this.inputFileNames.get(i),c);
            
		     JCheckBox fileLoad = new JCheckBox();
		     fileLoad.setActionCommand("fileLoadJCheckBox"+i);
		     fileLoad.setSelected(false);
		     
		     fileLoad.addActionListener(this);
		     c.fill = GridBagConstraints.HORIZONTAL;
		     c.gridx = 2;
		     c.gridy = i+1;
		     c.ipadx = 5;
		     topFileInfoPanel.add(fileLoad,c);
	            
		     JCheckBox fileSave = new JCheckBox();
		     fileSave.setActionCommand("fileSaveJCheckBox"+i);

		     fileSave.setSelected(false);
	
		     fileSave.addActionListener(this);
		     c.fill = GridBagConstraints.HORIZONTAL;
		     c.gridx = 3;
		     c.gridy = i+1;
		     c.ipadx = 5;
		     topFileInfoPanel.add(fileSave,c);
		     
		     if((this.inputFileNames.size() > this.numInputWithJTextArea)&&
		    		 (i >= this.numInputWithJTextArea)){
			     JCheckBox fileDelete = new JCheckBox();
			     fileDelete.setActionCommand("fileDeleteJCheckBox"+i);

			     fileDelete.setSelected(false);
		
			     fileDelete.addActionListener(this);
			     c.fill = GridBagConstraints.HORIZONTAL;
			     c.gridx = 4;
			     c.gridy = i+1;
			     c.ipadx = 5;
			     topFileInfoPanel.add(fileDelete,c);	    	 
		     }
	     }
	     
	     if( this.appName.equalsIgnoreCase(Invariants.APP_NAME_DMOL3)
	    		 || this.appName.equalsIgnoreCase(Invariants.APP_NAME_CASTEP) ){
	    	 addInputButton = new JButton("Load Input");
	    	 addInputButton.setBorder(new LineBorder(Color.GRAY, 3));
	    	 addInputButton.setToolTipText("Click this button to load an additional input file");
	    	 addInputButton.addActionListener(this);
		        
	    	 c.fill = GridBagConstraints.HORIZONTAL;
		     c.gridx = 1;
		     c.gridy = this.inputFileNames.size()+1;
		     c.ipady = 5;   
		     c.insets = new Insets(15,0,0,0);
		     topFileInfoPanel.add(addInputButton,c);
	     }

        if(this.inputFileNames.size() == this.numInputWithJTextArea){
   	     	bottomTabbedPane = new JTabbedPane();

   	     	for (int i = 0 ; i < this.numInputWithJTextArea; i++) {
        		 String title =  this.inputFiles.get(i).getName();
        		 bottomTabbedPane.add(title, new JLabel(title));
        		 //inputTexts.add(new JTextArea(inps.get(i)));
        		 bottomTabbedPane.setComponentAt(i,new JScrollPane(inputTexts.get(i)));
   	     	}
   	     	bottomTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
   	     	splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
	        		 topFileInfoPanel, bottomTabbedPane);
   	     	splitPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 2, 0));
        	
        	add(splitPane);
       
         }else{
        	 topFileInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 2, 0));

        	 add(topFileInfoPanel, BorderLayout.CENTER);
         }
	     this.setPreferredSize(new Dimension(500,500));
	     
	 }

	 public void updateInputFileInfo(){

		 this.removeAll();
		 this.invalidate();
		 this.repaint();
		 
		 
		 this.initGUI();
		 this.validate();
	}

	 public void changeInputFileName(int id, File file){
		 
		    this.inputFileNames.get(id).setText(file.getName());
		    this.bottomTabbedPane.setTitleAt(id, file.getName());
		 
	 }
	 
	 private int getID(String command){
	    	int id = 0;
    		for (int i = 0; i < inputFileNames.size(); i++){
    			if (command.contains(String.valueOf(i)) ){
    				id = i;
    				break;
    			}
    		}    	
	    	return id;    	
	  
	 }
	    
	 public String getInputText(int i){
		 String text = null;
		 text = inputTexts.get(i).getText();
		 
		return  text;
	 }
	 
	 public ArrayList<String> getInputTexts(){
		 ArrayList<String> texts = new ArrayList<String>();
		 for(int i=0; i< inputTexts.size(); i++)
			 texts.add(inputTexts.get(i).getText());
		 return texts;
	 }
	 
	 
	 public String getInpFileNameText(int i){
		 String text = null;
		 text = inputFileNames.get(i).getText();
		 
		return  text;
	 }
	 
	 public ArrayList<String> getInpFileNameTexts(){
		 ArrayList<String> texts = new ArrayList<String>();
		 for(int i=0; i< inputFileNames.size(); i++)
			 texts.add(inputFileNames.get(i).getText());
		 return texts;
	 }
	 
	 
	 public void itemStateChanged(ItemEvent e) {
        	if(e.getStateChange() == ItemEvent.DESELECTED){
        		return;
        	}
        	
        	if ( ((String)(e.getItem())).equals("something")) {
        	}
	 }
	 
	 public String readTextArea(File f) throws IOException {
			String line = null;
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			BufferedReader br = null;
			StringBuilder ta = new StringBuilder();
			
			try {
				fis = new FileInputStream(f);
				bis = new BufferedInputStream(fis);
				br = new BufferedReader(new InputStreamReader(bis));
			} catch (Throwable e) {
				JOptionPane.showMessageDialog(null,
						"There was a problem reading the file...", "NewJob",
						JOptionPane.ERROR_MESSAGE);
				System.err.println("LowLevelIO:readTextArea:  error opening file");
				System.err.println(e.toString());
				e.printStackTrace();
			}

			try {
				int i = 0;
				while ((line = br.readLine()) != null) {
					ta.append(line + "\n");
				}
				fis.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"There was a problem reading the file...", "NewJob",
						JOptionPane.ERROR_MESSAGE);
				System.err.println("LowLevelIO:readTextArea:  error reading file");
				System.err.println(e.toString());
				e.printStackTrace();
			}	
			return ta.toString();
	}
	    
	 public void actionPerformed(ActionEvent e){
	    	String command = e.getActionCommand();
	    	int id;
	    	
	    	if (command.contains("fileLocJComboBox") ){
	    		id = getID(command);
	    		
	    		// need the code for checking whether this is changed 
	    		// and if so, where is the file and change the corresponding fileLoadView 
	    		
	    	} else if ( command.contains("fileLoadJCheckBox") ){
	    		id = getID(command);
	    		JCheckBox jcheckbox = (JCheckBox) e.getSource() ;
	    		// add the code for loading and validating and others...
	    		if (jcheckbox.isSelected()){
	    			
	    			JFileChooser chooser = new JFileChooser();
	    			
	    			int result = chooser.showOpenDialog(this);
	    			String inp = new String();
	    			if (result == JFileChooser.CANCEL_OPTION) {

	    				((JCheckBox)e.getSource()).setSelected(false);
	    				return;
	    			}
	    			try{
	    			    File file = chooser.getSelectedFile();
	    			    
	    			    inp = readTextArea(file);
	    			    this.inputTexts.get(id).setText(inp);
	    			    this.inputFileNames.get(id).setText(file.getName());
	    			    if(id < this.numInputWithJTextArea){
	    			    	this.bottomTabbedPane.setTitleAt(id, file.getName());
	    			    }
	    			    
	    			    //changeFileName(file.getName());
	    			    String fName = file.getName();
	    			    String [] filename = new String[6];
	    			    StringTokenizer ojt = new StringTokenizer(fName,".");
	    			    int i = 0;
	    				while (ojt.hasMoreTokens()){
	    				    filename[i] = ojt.nextToken();
	    				    i++;
	    				}
	    				((JCheckBox)e.getSource()).setSelected(false);
	    //			    jobNameText.setText(filename[0]);
	    			}catch (FileNotFoundException e1){
	    			    JOptionPane.showMessageDialog(null, "File not found",
	    				    "Problem opening file", JOptionPane.INFORMATION_MESSAGE);
	    			    System.out.println("File not found\n");
	    			}catch(Exception e1){
	    			    JOptionPane.showMessageDialog(null, "Reading error",
	    				    "Problem reading file", JOptionPane.INFORMATION_MESSAGE);
	    			}
	    			this.update((Graphics) (this.getGraphics()));

	    		} 
	    		
	    	} else if ( command.contains("fileSaveJCheckBox") ){
	    		id = getID(command);
	    		JCheckBox jcheckbox = (JCheckBox) e.getSource() ;
	    		// add the code for loading and validating and others...
	    		if (jcheckbox.isSelected()){
	    			JFileChooser chooser = new JFileChooser();
		    		int retVal = chooser.showSaveDialog(this);

		    		try {
		    			if (retVal == JFileChooser.APPROVE_OPTION) {
		    				File file = chooser.getSelectedFile();
		    				// save the file data
		    				FileWriter fw = new FileWriter(file);
		    				String outp = getInputText(id);
		    				fw.write(outp);
		    				fw.close();

		    				changeInputFileName(id, file);
		    				((JCheckBox)e.getSource()).setSelected(false);
		    			}else{

		    				((JCheckBox)e.getSource()).setSelected(false);

		    				return;
		    			}
		    		} catch (IOException ex) {
		    			JOptionPane.showMessageDialog(null, "Error writing to file",
		    					"Save File Error", JOptionPane.INFORMATION_MESSAGE);
		    		}
	
	    		
	    		} 
	    		
	    	} else if(command.contains("fileName")){
	    		id = getID(command);
	    		JTextField jtextfield = (JTextField) e.getSource() ;
	    		if(id < this.numInputWithJTextArea){
	    			this.bottomTabbedPane.setTitleAt(id, jtextfield.getText());
	    			Graphics g = this.getGraphics();
	    			this.update(g);
	    		}
	    	}else if ( command.contains("Load Input") ){
	    			
	    		JFileChooser chooser = new JFileChooser();
	    			
	    		int result = chooser.showOpenDialog(this);
	    		String inp = new String();
	    		if (result == JFileChooser.CANCEL_OPTION) {
	    				return;
	    		}
	    			
	    		try{
	    			File file = chooser.getSelectedFile();
	    			    
	    			inp = readTextArea(file);
	    			this.inputFiles.add(file);
	    			this.inputTexts.add(new JTextArea(inp));
	    			this.inputFileNames.add(new JTextField(file.getName()));
	    			

	    		}catch (FileNotFoundException e1){
	    			 JOptionPane.showMessageDialog(null, "File not found",
	    				    "Problem opening file", JOptionPane.INFORMATION_MESSAGE);
	    			 System.out.println("File not found\n");
	    		}catch(Exception e1){
	    			 JOptionPane.showMessageDialog(null, "Reading error",
	    				    "Problem reading file", JOptionPane.INFORMATION_MESSAGE);
	    		}


	    		updateInputFileInfo();

	    	}else if ( command.contains("fileDeleteJCheckBox") ){
	    		id = getID(command);
	    		
	    		this.inputFiles.remove(id);
	    		this.inputFileNames.remove(id);
	    		this.inputTexts.remove(id);
	    		
	    		updateInputFileInfo();
	    		
	    	}
	    	
	  }
	    

}

