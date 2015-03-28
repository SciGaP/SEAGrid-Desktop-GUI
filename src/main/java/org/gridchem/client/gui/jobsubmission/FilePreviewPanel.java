/* 
 * Created on May 1, 2008
 * 
 * Developed by: Rion Dooley - dooley [at] tacc [dot] utexas [dot] edu
 * 				 TACC, Texas Advanced Computing Center
 * 
 * https://www.tacc.utexas.edu/
 */

package org.gridchem.client.gui.jobsubmission;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.swing.*;

import org.gridchem.client.SwingWorker;
import org.gridchem.client.util.Env;

/**
 * Class to display the input file data.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class FilePreviewPanel extends JInternalFrame implements ActionListener {

    private JMenuBar mnuMain;
    private JMenu mFile;
    private JMenuItem mFileSave;
    private JMenuItem mFileSaveAs;
    private JMenuItem mFileClose;
    private JMenu mEdit;
    private JMenuItem mEditCut;
    private JMenuItem mEditCopy;
    private JMenuItem mEditPaste;
    private JMenu mView;
    private JMenuItem mViewText;
    private JMenuItem mViewXml;
    
    private JTextArea editPane;
    private JScrollPane editScrollPane;
    
    private File currentFile;
    private Clipboard clipboard;
    
    private InputFilePanel parent;
    
    public FilePreviewPanel(InputFilePanel parent) {
        super();
        
        this.parent = parent;
        
        createMenuBar();
        createTextArea();
        
        setJMenuBar(mnuMain);
        add(editScrollPane);
        
//        setMinimumSize(new Dimension(Short.MIN_VALUE,200));
        setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
        
        pack();
        
        setVisible(true);
        
    }
    
    public FilePreviewPanel(InputFilePanel parent, File file) {
        this(parent);
        
        loadFile(file);
    }
    
    public FilePreviewPanel(InputFilePanel parent, String text) {
        this(parent);
        
        loadTextInput(text);
    }
    
    private void createMenuBar() {
        mnuMain = new JMenuBar();
//        mnuMain.setAlignmentX(Component.LEFT_ALIGNMENT);
//        mnuMain.setMaximumSize(new Dimension(150,25));
//        mnuMain.setMinimumSize(new Dimension(150,25));
//        mnuMain.setPreferredSize(new Dimension(150,25));
//        mnuMain.setBorder(BorderFactory.createEtchedBorder());
        mFile = new JMenu("File");
        mFileSave = new JMenuItem("Save");
        mFileSave.addActionListener(this);
        mFileSaveAs = new JMenuItem("Save as ...");
        mFileSaveAs.addActionListener(this);
        mFileClose = new JMenuItem("Close");
        mFileClose.addActionListener(this);
        mFile.add(mFileSave);
        mFile.add(mFileSaveAs);
        mFile.add(mFileClose);
        mEdit = new JMenu("Edit");
        mEditCut = new JMenuItem("Cut");
        mEditCut.addActionListener(this);
        mEditCopy = new JMenuItem("Copy");
        mEditCopy.addActionListener(this);
        mEditPaste = new JMenuItem("Paste");
        mEditPaste.addActionListener(this);
        mEdit.add(mEditCut);
        mEdit.add(mEditCopy);
        mEdit.add(mEditPaste);
        mView = new JMenu("View");
        mViewText = new JMenuItem("Text Editor");
        mViewText.addActionListener(this);
        mViewXml = new JMenuItem("XML Editor");
        mViewXml.addActionListener(this);
        mViewXml.setEnabled(false);
        mView.add(mViewXml);
        mView.add(mViewText);
        
        mnuMain.add(mFile);
        mnuMain.add(mEdit);
        mnuMain.add(mView);

    }

    private void createTextArea() {
        this.editPane = new JTextArea();
        editScrollPane = new JScrollPane(editPane);
//        ps.setAlignmentX(Component.LEFT_ALIGNMENT);
        editScrollPane.setPreferredSize(new Dimension(250,200));
        editScrollPane.getViewport().setBackground(editPane.getBackground());
        //editScrollPane.getViewport().add(editPane);
        
//        JTree tree = new JTree();
//        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
//        root.setUserObject("");
//        root.add(new DefaultMutableTreeNode(ps));
        
    }
    
    public void actionPerformed(ActionEvent event) {
        
        if (event.getSource() == mFileSave) {
            doSave();
        } else if (event.getSource() == mFileSaveAs) {
        	doSaveAs();
        } else if (event.getSource() == mFileClose) {
            doClose();
        } else if (event.getSource() == mEditCut) {
            doCut();
        } else if (event.getSource() == mEditCopy) {
            doCopy();
        } else if (event.getSource() == mEditPaste) {
            doPaste();
//        } else if (event.getSource() == mViewText) {
//            doMarkupText();
        } else if (event.getSource() == mViewXml) {
            doMarkupXml();
        } 
    }
    
    private void doSave() {
        try {
            FileWriter fw = new FileWriter(currentFile);
            fw.write(editPane.getText());
            fw.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving file", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void doSaveAs() {
    	String folderPath = Env.getUserCustomedInputFilesDir();
    	String newFileName = JOptionPane.showInputDialog(this, "Please input a file name", "", JOptionPane.QUESTION_MESSAGE);
    	
    	if ((newFileName == null) || newFileName.isEmpty()) {
    		return;
    	} else {
    		try {
    			FileWriter fw = new FileWriter(folderPath + "/" + newFileName);
    			fw.write(editPane.getText());
                fw.close();
    		} catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error saving file", "File Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    	}
    	
    	parent.removeSelectedInputFile();
    	File newFile = new File(folderPath + "/" + newFileName);
    	parent.addFileInput(newFile);
    	loadFile(newFile);
    	
    }

    private void doClose() {
        int save = JOptionPane.showConfirmDialog(this, "Save before closing?");
        
        if (save == JOptionPane.OK_OPTION) {
            doSave();
            mFileClose.setEnabled(false);
        } else if (save == JOptionPane.NO_OPTION){
            editPane.setText("");
            mFileClose.setEnabled(false);
        }
    }
    
    private void doCut() {
        doCopy();
        
        editPane.replaceSelection("");
    }
    
    private void doCopy() {
        String sSelection = editPane.getText();
        
        StringSelection stsel = null;
        stsel = new StringSelection(sSelection);
        
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stsel,stsel);
    }
    
    private void doPaste() {
        Transferable cbContent = clipboard.getContents(this);
        
        if ((cbContent != null) &&
                (cbContent.isDataFlavorSupported(DataFlavor.stringFlavor))) {
            try {
                String sTemp = (String) cbContent.getTransferData(DataFlavor.stringFlavor);
                int cursorPosition = editPane.getCaretPosition();
                editPane.setSelectionStart(cursorPosition);
                editPane.setSelectionEnd(cursorPosition);
                String sSelection = editPane.getSelectedText();
                editPane.replaceSelection(sSelection + sTemp);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error pasting contents from clipboard", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }
    
//    private void doMarkupText() {
//        if (!(editPane instanceof JTextPane)) {
//            editPane = new XmlEditPane();
//            editPane.setText(editPane.getText());
//        }
//    }
    
    private void doMarkupXml() {
        if (!(editPane instanceof JTextArea)) {
            editPane = new JTextArea();
            editPane.setText(editPane.getText());
        }
    }
    
    protected void clear() {
        editPane.setText("");
    }
    
    public void startWaiting() {
        // notify the user visually that the file is loading...
        System.out.println("Starting to load file " + currentFile.getName() + "...");
        parent.isLoading = true;
        editPane.setText("         Loading...");
        editPane.setEnabled(false);
    }
    
    public void stopWaiting() {
        System.out.println("Finished loading file " + currentFile.getName() + ".");
        editPane.setEnabled(true);
        editPane.setCaretPosition(0);
        parent.isLoading = false;
        mFileClose.setEnabled(true);
    }
    
    public File getCurrentFile() {
        return this.currentFile;
    }
    
    public void loadFile(File file) {
        currentFile = file;
        
        //...checks on aFile are elided
        startWaiting();
        
        final SwingWorker worker = new SwingWorker() {
            public Object construct() {
                try {
            
                    //use buffering, reading one line at a time
                    //FileReader always assumes default encoding is OK!
                    BufferedReader input =  new BufferedReader(new FileReader(currentFile));
                
                    try {
                        String line = null; //not declared within while loop
                        /*
                        * readLine is a bit quirky :
                        * it returns the content of a line MINUS the newline.
                        * it returns null only for the END of the stream.
                        * it returns an empty String if two newlines appear in a row.
                        */
                        editPane.setText("");
                        while (( line = input.readLine()) != null){
                            editPane.append(line + "\n");
                        }
                    }
                    finally {
                        input.close();
                    }
                }
                catch (IOException ex){
                    JOptionPane.showMessageDialog(editPane, "Error loading file", "File Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    stopWaiting();
                }
            });
            
            return null;

            }
        };
        
        worker.start();
        
        
    }

    
    public void loadTextInput(String sText) {
        currentFile = new File(Env.getGridchemDataDir() + File.separator + "tmp" + 
                File.separator + "default.tmp." + new Date().getTime());
        
        try {
            
            if (!currentFile.getParentFile().exists()) {
                currentFile.getParentFile().mkdirs();
            }
            
            if (!currentFile.exists())
                currentFile.createNewFile();
        }
        catch (IOException ex){
            JOptionPane.showMessageDialog(this, "Error loading file", "File Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        
        editPane.setText(sText);
        parent.addInputFile(currentFile);
    }
    
    public void setEditPaneContent(String text) {
    	editPane.setText(text);
    }
    
    public String getEditPaneContent() {
    	return editPane.getText();
    }
    
}
