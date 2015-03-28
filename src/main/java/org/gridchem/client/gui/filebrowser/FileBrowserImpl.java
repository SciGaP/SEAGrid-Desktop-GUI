/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Jun 14, 2006
 * 
 * Developed by: CCT, Center for Computation and Technology, 
 * 				NCSA, University of Illinois at Urbana-Champaign
 * 				OSC, Ohio Supercomputing Center
 * 				TACC, Texas Advanced Computing Center
 * 				UKy, University of Kentucky
 * 
 * https://www.gridchem.org/
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal with the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimers.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimers in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
 *    University of Illinois at Urbana-Champaign, nor the names of its contributors 
 *    may be used to endorse or promote products derived from this Software without 
 *    specific prior written permission.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS WITH THE SOFTWARE.
*/

package org.gridchem.client.gui.filebrowser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.gridchem.client.common.MimeHandler;
import org.gridchem.client.gui.panels.PathInputPanelImpl;
import org.gridchem.client.gui.panels.myccg.job.JobPanel;
import org.gridchem.client.util.Env;
import org.gridchem.service.beans.FileBean;
import org.gridchem.service.beans.JobBean;

/**
 * Base class for file browsings.  This class contains the gui code and offloads 
 * a lot of the actual work to the <code>FileBrowserWorker class</code>.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class FileBrowserImpl extends JPanel implements Serializable, FileBrowser {
    private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(FileBrowserImpl.class);
    
    //utility declarations
    //private GridCommandManager gcm;
    private FileBrowserWorkerImpl dbworker;
    private MimeHandler mimehandler;
    private ButtonListener buttonListener;
    //top panel declarations
    private JPanel topPanel;
    private static PathInputPanelImpl topURIPanel;
    private JButton disconnectButton;
    private JButton goButton;
    
    //middle declarations (all these in scrollpane)
    protected JTree tree;
    private JPopupMenu rightClickPopup;
    
    //bottom declarations (all these in a bottom panel)
    private static JProgressBar progress = new JProgressBar();
    private static JLabel progressInfo = new JLabel(" ");
    private static JButton stopButton;
    
    //popupmenu declarations
    private JMenuItem newDirectory; 
    private JMenuItem deleteEntry; 
    private JMenuItem getInfo;
    private JMenuItem downloadFile;
    private JMenuItem downloadAll;
    private JMenuItem goInto; 
    private JMenuItem refresh; 
    
    private boolean connected;
    
    public FileBrowserImpl(JobBean job) throws Exception {
    }

    public FileBrowserImpl(FileBean file) throws Exception {
        
    }
    
    public FileBrowserImpl(String path) {
        super(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        dbworker = new FileBrowserWorkerImpl(this);
        mimehandler = new MimeHandler();
        
        //create all the action listener classes
        buttonListener = new ButtonListener();
        
        //Build the top panel
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new Insets(2,2,2,2);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        topPanel = this.createTopPanel();
        this.add(topPanel, c);
        topURIPanel.setPath(path);
        
        //build the icon panel
        c.gridy = 1;
        this.add(this.createIconPanel(), c);
        
        //build the tree scroll window
        c.gridy=2;
        c.weightx = 2.0;
        c.weighty = 2.0;
        c.fill = GridBagConstraints.BOTH;
        this.add(this.createTreePanel(), c);
        
        //build the bottom panel
        c.gridy = 3;
        c.weightx =0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.createBottomPanel(), c) ;
        
        //Build the popup menu
        rightClickPopup = this.createPopupMenu();
        
        //JDialog outputDialog = new JDialog((JFrame)null, "Directory Browser Log");
        //outputDialog.getContentPane().add(logArea);
        //outputDialog.setVisible(true);
        
        dbworker.goButtonPushed();
    }
    
    //protected JTextArea getLogWindow() {
    //  return logArea;
    //}
    
    /**
     * Execute the commands to notify the user that we are waiting for something.
     *
     */
    protected void startWaiting() {
        this.startWaiting(" ", false);
    }
    
    /** 
     * Execute the commands to notify the user that we are waiting for something and
     * display a message above the progress bar.
     * @param message String to display above the message bar.
     */
    protected void startWaiting(String message, boolean canStop){
        String queue = (SwingUtilities.isEventDispatchThread())? 
                "event dispatch thread." : 
                    "a separate thread (" + Thread.currentThread().getName() + ").";
        System.out.println("Start waiting called from " + queue);
        progress.setIndeterminate(true);
        progressInfo.setText(message);
        topURIPanel.setEnabled(false);
        if(canStop) {
            stopButton.setEnabled(true);
        }
    }
    
    /**
     * Execute the commands to notify the user that we are done waiting.
     *
     */
    protected static void stopWaiting() {
        String queue = (SwingUtilities.isEventDispatchThread())? 
                "event dispatch thread." : 
                    "a separate thread (" + Thread.currentThread().getName() + ").";
        System.out.println("Stop waiting called from " + queue);
        progress.setIndeterminate(false);
        progress.setValue(0);
        progressInfo.setText(" ");
        topURIPanel.setEnabled(true);
        stopButton.setEnabled(false);
    }
    
    protected void startDownload(int increments) {
        progress.setIndeterminate(false);
        progress.setMaximum(increments);
        progress.setValue(1);
        progressInfo.setText("Downloading...");
    }
    
    protected void updateDownloadProgress(int value) {
        progress.setValue(value);
    }
    
    protected void stopDownload() {
        progressInfo.setText("Complete");
        progress.setValue(0);
    }
    
    /**
     * Notify the user of something but do not indicate any waiting.  Probably for a background
     * process.
     * @param message
     */
    protected void notifyUser(String message) {
        String queue = (SwingUtilities.isEventDispatchThread())? 
                "event dispatch thread." : 
                    "a separate thread (" + Thread.currentThread().getName() + ").";
        System.out.println("Notify user called from " + queue);
        progressInfo.setText(message);
    }
    
    protected FileBrowserWorkerImpl getWorker() {
        return dbworker;
    }
    
///Methods that return information about the currently selected item/// 
    /**
     * Get the <code>TreePath</code> of the selected item.
     * @return
     */
    protected TreePath getSelectedItemsTreePath() {
        return tree.getSelectionPath();
    }
    
    /**
     * Return the FileBean object of the selected item.
     * @return
     */
    protected FileBean getSelectedItemsGridFile() {
        try{
            return (FileBean) ((GridFileTreeNode) this.getSelectedItemsTreePath().getLastPathComponent()).getUserObject();
        } catch(NullPointerException e) {
            return null;
        }
    }
    
    /**
     * Return the selected items uri
     * @return
     */
    protected String getSelectedItemsPath() {
        return dbworker.processPathFromTopPanel(dbworker.getPathFromTreePath(getSelectedItemsTreePath()));
    }
    
    /**
     * If the selected item is a directory, return its uri, otherwise return the uri 
     * of its parent.
     * @return
     */
    protected String getSelectedItemsDirPath() {
        return dbworker.processPathFromTopPanel(dbworker.getDirectoryPathFromTreePath(getSelectedItemsTreePath()));
    }

    
    
///Methods that create panels///
    /**
     * Create and return a JPanel with the icons that contain the icons to be placed at the top of the panel.
     * @return
     */
    private JPanel createIconPanel() {
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
        //These are loaded from the cog-resources jar file.
        JButton goHomeButton = new JButton(new ImageIcon(Env.getApplicationDataDir() + "/images/navigation/folder-home.png"));//should be arrow-up
        JButton upDirectoryButton = new JButton(new ImageIcon(Env.getApplicationDataDir() + "/images/navigation/up.png"));//should be arrow-up
        JButton reloadButton = new JButton(new ImageIcon(Env.getApplicationDataDir() + "/images/navigation/arrow-reload.png"));
        JButton newDirectoryButton = new JButton(new ImageIcon(Env.getApplicationDataDir() + "/navigation/images/window-new.png"));
        JButton getInfoButton = new JButton(new ImageIcon(Env.getApplicationDataDir() + "/images/navigation/window-about.png"));
        
        goHomeButton.addActionListener(buttonListener);
        upDirectoryButton.addActionListener(buttonListener);
        reloadButton.addActionListener(buttonListener);
        newDirectoryButton.addActionListener(buttonListener);
        getInfoButton.addActionListener(buttonListener);
        
        goHomeButton.setToolTipText("Home");
        upDirectoryButton.setToolTipText("Up Directory");
        reloadButton.setToolTipText("Refresh");
        newDirectoryButton.setToolTipText("New Directory");
        getInfoButton.setToolTipText("Get Info");
        
        goHomeButton.setBorder(BorderFactory.createEmptyBorder(2,2,0,0));
        upDirectoryButton.setBorder(BorderFactory.createEmptyBorder(2,2,0,0));
        reloadButton.setBorder(BorderFactory.createEmptyBorder(2,2,0,0));
        newDirectoryButton.setBorder(BorderFactory.createEmptyBorder(2,2,0,0));
        getInfoButton.setBorder(BorderFactory.createEmptyBorder(2,2,0,0));
        
        iconPanel.add(goHomeButton);
        iconPanel.add(upDirectoryButton);
        iconPanel.add(reloadButton);
        //iconPanel.add(newDirectoryButton);
        iconPanel.add(getInfoButton);
        
        return iconPanel;
    }
    
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        
        TopKeyListener topKeyListener = new TopKeyListener();
        topURIPanel = new PathInputPanelImpl();
        topURIPanel.addKeyListener(topKeyListener);
        
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,0));
        goButton = new JButton(new ImageIcon(Env.getApplicationDataDir() + "/images/navigation/button-ok.png"));
        goButton.addActionListener(buttonListener);
        goButton.setToolTipText("Connect");
        goButton.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        
        disconnectButton = new JButton(new ImageIcon(Env.getApplicationDataDir() + "/images/navigation/button-cancel.png"));
        disconnectButton.addActionListener(buttonListener);
        disconnectButton.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        disconnectButton.setToolTipText("Disconnect");
        disconnectButton.setEnabled(false);
        
        topPanel.add(topURIPanel);
        topPanel.add(goButton);
        topPanel.add(disconnectButton);
        return topPanel;
    }
    
    private JScrollPane createTreePanel() {
        TreeListener treeListener = new TreeListener();
        TreeMouseListener treeMouseListener = new TreeMouseListener();
        //Build the directory tree area
        tree = new JTree(dbworker.getTreeDataModel());
        tree.addTreeWillExpandListener(treeListener);
        tree.addMouseListener(treeMouseListener);
        ///tree.addMouseMotionListener(treeMouseListener);
        tree.setRootVisible(false);
        
        //Drag and drop stuff
        tree.setDragEnabled(false);
//        tree.setTransferHandler(new DirectoryBrowserTransferHandler(this));
//        tree.setDropTarget(new DropTarget(tree,new DirectoryBrowserDropTargetListener(this)));
        return new JScrollPane(tree);
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth=2;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        bottomPanel.add(progressInfo, c);
        c.weightx=0;
        c.weighty=0;
        c.gridwidth=1;
        c.gridy=1;
        c.fill = GridBagConstraints.NONE;
        bottomPanel.add(progress, c);
        stopButton = new JButton(new ImageIcon(Env.getApplicationDataDir() + "/images/navigation/button-cancel.png"));
        stopButton.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        stopButton.setToolTipText("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(buttonListener);
        c.gridx = 1;
        bottomPanel.add(stopButton,c);
        
        return bottomPanel;
    }
    
    private JPopupMenu createPopupMenu() {
        PopupListener popupListener = new PopupListener();
        rightClickPopup = new JPopupMenu();
        downloadFile = new JMenuItem("Download");
        downloadAll = new JMenuItem("Download All files");
        newDirectory = new JMenuItem("New Directory");
        deleteEntry = new JMenuItem("Delete Item");
        getInfo = new JMenuItem("Get Info");
        goInto = new JMenuItem("Go Into");
        refresh = new JMenuItem("Refresh");
        
        rightClickPopup.add(downloadFile);
        rightClickPopup.add(newDirectory);
        rightClickPopup.add(deleteEntry);
        rightClickPopup.add(getInfo);
        rightClickPopup.addSeparator();
        rightClickPopup.add(goInto);
        rightClickPopup.add(refresh);
        
        //newDirectory.addActionListener(popupListener);
        deleteEntry.addActionListener(popupListener);
        getInfo.addActionListener(popupListener);
        goInto.addActionListener(popupListener);
        refresh.addActionListener(popupListener);
        downloadFile.addActionListener(popupListener);
        downloadAll.addActionListener(popupListener);
        return rightClickPopup;
    }
    
    
    
///Methods that create dialogs///
    /** Create the dialog to be popped up asking the user for their username 
     * and password if they are not connecting to a gridftp server.
     *
     */
    protected void createUsernameDialog() {
        final JDialog usernameDialog = new JDialog((JFrame) null, "Username & Password", true);
        Container content = usernameDialog.getContentPane();
        content.setLayout(new BorderLayout());
        
        // contains the labels
        JPanel west = new JPanel(new GridLayout(0,1));
        west.add(new JLabel("Username:"));
        west.add(new JLabel("Password"));
        
        // contains the username and password fields and goes in content.CENTER
        JPanel middle = new JPanel(new GridLayout(0,1));
        final JTextField usernameField = new JTextField(20);
        final JPasswordField passwordField = new JPasswordField(20);        
        
        middle.add(usernameField);
        middle.add(passwordField);
        
        // contains anonymousPanel and buttonPanel and goes in content.SOUTH
        JPanel controls = new JPanel(new GridLayout(1,0));
        
        JPanel anonymousPanel = new JPanel(new GridLayout(1,0));
        final JCheckBox isAnonymous = new JCheckBox("Anonymous");
        isAnonymous.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aEvent) {
                if(isAnonymous.isSelected()) {
                    usernameField.setText("anonymous");
                }
            }
        });
        anonymousPanel.add(isAnonymous);        
        
        JPanel buttonPanel = new JPanel(new GridLayout(1,0));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent aEvent) {
                usernameDialog.dispose();
            }
        });
        JButton okButton = new JButton("Ok");       
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dbworker.setUsername(usernameField.getText());
                dbworker.setPassword(new String(passwordField.getPassword()));
                usernameDialog.dispose();
            }
        });
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        
        // add to controls panel
        controls.add(anonymousPanel);
        controls.add(buttonPanel);
        
        // add to content
        content.add(west,BorderLayout.WEST);
        content.add(middle,BorderLayout.CENTER);
        content.add(controls,BorderLayout.SOUTH);
        
        usernameDialog.pack();
        usernameDialog.setVisible(true);
    }
    
    /**
     * create the confirmation dialog for deleting an object.
     * @param itemToDelete String representation of item to delete.
     * @return boolean whether or not to delete the item
     */
    private boolean createDeleteDialog(String itemToDelete) {
        Object[] choices = {"Delete", "Do not delete"};
        int finalChoice = JOptionPane.showOptionDialog(this, "Are you sure you want to delete " + 
                itemToDelete +"?\nThis operation cannot be undone!", "Delete?", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, choices, choices[1]);
        if(finalChoice == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * create the dialog that asks the user the name of the directory to create.
     * @param parentsURI 
     * @return
     */
    private String createNewDirectoryDialog() {
        return (String) JOptionPane.showInputDialog(this,
                "What is the name of the new directory?", "New Directory", JOptionPane.QUESTION_MESSAGE);
    }
    
    /**
     * Create the Get Info dialog that pops up after a user clicks on an item and selects "Get Info"
     * @param gridFile The <code>FileInfo</code> object of the selected item.
     */
    private void createGetInfoDialog(FileBean gridFile) {
        if(gridFile==null)
            return;
        
        String kindString = "";
        if(gridFile.isDirectory()) {
            kindString = "Directory";
        } else {
            kindString = "File";
        }
//        } else if(gridFile.isSoftLink()) {
//            kindString = "Link";
//        } else if (gridFile.isDevice()) {
//            kindString = "Device";
//        }
        
        JDialog getInfoDialog = new JDialog();
        JPanel getInfoDialogPanel = new JPanel(new GridLayout(6,1));
        ((GridLayout) getInfoDialogPanel.getLayout()).setVgap(5);
        getInfoDialog.setContentPane(getInfoDialogPanel);
        getInfoDialogPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Information about " + gridFile.getName()));
        
        JLabel kind = new JLabel("<html><b>Kind:</b> " + kindString + "<html>");
        getInfoDialogPanel.add(kind);
        JLabel size = new JLabel("<html><b>Size:</b> " + formatSize(gridFile.getLength()) + "<html>");
        getInfoDialogPanel.add(size);
        JLabel modified = new JLabel("<html><b>Modified:</b> " + formatDate(gridFile.getLastModified()) +"<html>");
        getInfoDialogPanel.add(modified);
        JLabel path = new JLabel("<html><b>Remote Path:</b> " + gridFile.getPath() + "<html>");
        getInfoDialogPanel.add(path);
        JLabel access = new JLabel("<html><b>Access:</b> GSIFTP<html>");
        getInfoDialogPanel.add(access);
        
//        if (gridFile.isDirectory()) {
//            JLabel children = new JLabel("<html><b>Children:</b> " + gridFile.getChildren().size() + "<html");
//            getInfoDialogPanel.add(children);
//        }
        
//        String userPermissionsString ="";
//        String groupPermissionsString = "";
//        String worldPermissionsString = "";
        
        // GAT does not currently support file permission info
//        if(gridFile.userCanRead()) {
//            userPermissionsString = "Read";
//        }
//        if(gridFile.userCanWrite()) {
//            if(userPermissionsString!="") userPermissionsString += ", ";
//            userPermissionsString += "Write";
//        }
//        if(gridFile.userCanExecute()) {
//            if(userPermissionsString!="") userPermissionsString += ", ";
//            userPermissionsString += "Execute";
//        }
//        
//        if(gridFile.groupCanRead()) {
//            groupPermissionsString = "Read";
//        }
//        if(gridFile.groupCanWrite()) {
//            if(groupPermissionsString!="") groupPermissionsString += ", ";
//            groupPermissionsString += "Write";
//        }
//        if(gridFile.groupCanExecute()) {
//            if(groupPermissionsString!="") groupPermissionsString += ", ";
//            groupPermissionsString += "Execute";
//        }
//        
//        if(gridFile.allCanRead()) {
//            worldPermissionsString = "Read";
//        }
//        if(gridFile.allCanWrite()) {
//            if(worldPermissionsString!="") worldPermissionsString += ", ";
//            worldPermissionsString += "Write";
//        }
//        if(gridFile.allCanExecute()) {
//            if(worldPermissionsString!="") worldPermissionsString += ", ";
//            worldPermissionsString += "Execute";
//        }
        
//        JLabel userPermissions = new JLabel("<html><b>User Permissions:</b> " + userPermissionsString + "</html>");
//        JLabel groupPermissions = new JLabel("<html><b>Group Permissions:</b> " + groupPermissionsString + "</html>");
//        JLabel worldPermissions = new JLabel("<html><b>Others Permissions:</b> " + worldPermissionsString + "</html>");
        
        
        
//        getInfoDialogPanel.add(userPermissions);
//        getInfoDialogPanel.add(groupPermissions);
//        getInfoDialogPanel.add(worldPermissions);
        
        getInfoDialog.setTitle(gridFile.getName() + " Info");
        getInfoDialog.pack();
        getInfoDialog.setVisible(true);
    }
    
    private String formatSize(long size) {
    	// file is over 1kb
    	if (size > Math.pow(2, 10)) {
    		//file is over 1mb
    		if (size > Math.pow(2, 20)) {
    			// file is over 1gb
    			if (size > Math.pow(2, 30)) {
    				// file is over 1tb
    				if (size > Math.pow(2, 40)) {
    					// file is over 1pb
    					if (size > Math.pow(2, 50)) {
    						return String.valueOf(formatDecimal(size / Math.pow(2,50))) + " PB";
    					} else {
    						return String.valueOf(formatDecimal(size / Math.pow(2,40))) + " TB";
    					}
    				} else {
    					return String.valueOf(formatDecimal(size / Math.pow(2,30))) + " GB";
    				}
    			} else {
    				return String.valueOf(formatDecimal(size / Math.pow(2,20))) + " MB";
    			}
    		} else {
    			return String.valueOf(formatDecimal(size / Math.pow(2,10))) + " KB";
    		}
    	} else {
    		return size + " B";
    	}
    }
    
    /**
     * Rounds number to two decimal places
     * 
     * @param val
     * @return
     */
    private double formatDecimal(double val) {
    	return (double)Math.round(val * 10) / 10.0;
    }
    
    private String formatDate(Date date) {
    	Calendar now = Calendar.getInstance();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	
    	// if file was modified this year
    	if (cal.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
    		// if it was modified this month
    		if (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)) {
    			// if it was modified today
    			if (cal.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
    				// just display Today HH:mm a
    				return "Today " + new SimpleDateFormat("HH:mm a").format(date);
    			} else if (cal.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH) - 1) {
    				return "Yesterday " + new SimpleDateFormat("h:mm a").format(date);
    			} 
    		} 
    	}
    	
    	return new SimpleDateFormat("MMM d, yyyy h:mm a").format(date);
    }
    
///Methods called when operations are completed///
    
    protected void listingCompleted() {
        this.stopWaiting();
    }
    
    protected void disconnectCompleted() {
        stopWaiting();
        disconnectButton.setEnabled(false);
        goButton.setEnabled(true);
        topURIPanel.setEnabled(true);
        connected = false;
    }
    
    protected void getCurrentDirectoryCompleted(boolean isInConnectSequence) {
        if(isInConnectSequence) {
            topURIPanel.setEnabled(true);
            goButton.setEnabled(false);
            disconnectButton.setEnabled(true);
            connected = true;
        }
    }
    
    protected void fileTransferCompleted() {
        this.notifyUser("Transfer Complete!");
    }
    

    
///Methods called when errors occur///
    protected void errorOpen() {
        this.connectFailed("Connection failed.\nCould not open connection with server.");
    }
    
    protected void errorCopy() {
        JOptionPane.showMessageDialog(this, "Could not retrieve file.\nMost likely this is due to the delay retrieving\ndata from mass storage's tape drive. This \nmatter will usually be resolved on it's own.\nPlease try again in a few moments.",
                "Error",
                JOptionPane.WARNING_MESSAGE);
        this.stopWaiting();
    }
    
    protected void errorProtocol() {
        this.connectFailed("Browser only supports grid file management. Please enter an addresses beginning with 'gsiftp'.");
    }
    
    protected void errorSetCurrentDirectory(boolean isInConnectSequence) {
        if(isInConnectSequence) {
            //close the connection and let the user know what happened
            dbworker.disconnectButtonPushed();
            this.connectFailed("Connection failed.\nThe specified directory may not exist.");
        } else {
        JOptionPane.showMessageDialog(this, "Could not set current directory.",
                "Error",
                JOptionPane.WARNING_MESSAGE);
        disconnectButton.setEnabled(true);
        this.stopWaiting();
        }
        
    }
    
    protected void errorGetCurrentDirectory(boolean isInConnectSequence) {
        if(isInConnectSequence) {
            //close the connection and let the user know what happened
            dbworker.disconnectButtonPushed();
            this.connectFailed("Connection failed.\nCould not get the current directory.");
        } else {
        JOptionPane.showMessageDialog(this, "Could not get current directory.",
                "Error",
                JOptionPane.WARNING_MESSAGE);
        disconnectButton.setEnabled(true);
        this.stopWaiting();
        }
    }
    
    protected void errorList() {
        JOptionPane.showMessageDialog(this, "Could not open directory.",
                "Error",
                JOptionPane.WARNING_MESSAGE);
        this.stopWaiting();
    }
    
    protected void errorMimeType() {
        JOptionPane.showMessageDialog(this, "No class associated with that type.",
                "Error",
                JOptionPane.WARNING_MESSAGE);
        this.stopWaiting();
    }
    
    protected void errorDelete() {
        JOptionPane.showMessageDialog(this, "Could not delete item.",
                "Error",
                JOptionPane.WARNING_MESSAGE);
        this.stopWaiting();
    }
  
    /**
     * What to do when one of the methods called during connecting fails.
     * 
     * @param message More detailed information as to the nature of the failure.
     */
    protected void connectFailed(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Could not connect",
                JOptionPane.WARNING_MESSAGE);
        this.stopWaiting();
        disconnectButton.setEnabled(false);
        goButton.setEnabled(true);
        topURIPanel.setEnabled(true);
        connected = false;
    }
    
    /**
     * Return the <code>Dimension</code> of the top panel and the extra pixels on the side
     * so the window only gets as small as the top panel.
     * 
     * @return Dimension <code>Dimension</code> of the top panel.
     */
      public Dimension getMinimumSize() {
        Dimension returnDimension = new Dimension();
        returnDimension.setSize(topPanel.getPreferredSize().getWidth()+4, topPanel.getPreferredSize().getHeight()+4);
        return returnDimension;
      }
    
    public void setPath(String path) {
        topURIPanel.setPath(path);
    }

    public String getPath() {
        topURIPanel.get();
        return topURIPanel.getPath();
    }

    public String getSelectedPath() {
        return this.getSelectedItemsPath();
    }
    
    public String getSelectedURIDir() {
        return this.getSelectedItemsDirPath();
    }
    
    public void setSelected(String filename) {

    }

    public String getSelected() {
        return getSelectedItemsGridFile().getName();
    }

    public void update() {
        //gridface.update();
    }

    public Date lastUpdateTime() {
        return new Date();
        
        //gridface.lastUpdateTime();
    }

    public void setName(String name) {
        //gridface.setName(name);
    }

    public void setLabel(String label) {
        //gridface.setLabel(label);
    }

    public void register(Object connection) {
        //gridface.register(connection);
    }
    
    public boolean close() {
        if(connected) {
            dbworker.disconnectButtonPushed();
        }
        return true;
    }

    
    private void showPopupMenu(Component component, int x, int y) {
        if(!(((GridFileTreeNode) this.getSelectedItemsTreePath().getLastPathComponent()).getAllowsChildren())){
            goInto.setEnabled(false);
            refresh.setEnabled(false);
        } else {
            goInto.setEnabled(true);
            refresh.setEnabled(true);
        }
        System.out.println("Showing right click popup menu");
        rightClickPopup.show(component, x, y);
    }
    
    protected class TreeMouseListener implements MouseListener {
        
        public void mouseClicked(MouseEvent mEvent) {
            
            int mouseRow = tree.getRowForLocation(mEvent.getX(), mEvent.getY());
            int[] mouseRows = tree.getSelectionRows();
            if (mouseRows != null){
            	System.out.println("Clicked multiple real rows");
            	tree.setSelectionRows(mouseRows);
            	//if a single right click, popup the menu
                if(isRightClickEvent(mEvent)) {
                    System.out.println("Right clicked, showing popup menu.");
                    showPopupMenu(tree,mEvent.getX(), mEvent.getY());
                }
                else if ((mEvent.getButton()==MouseEvent.BUTTON1) && (mEvent.getClickCount()==2)) {
                	//String path = dbworker.getPathFromTreePath(getSelectedItemsTreePath());
                	TreePath[] paths = tree.getSelectionPaths();
                
                 for (TreePath pathT : paths ){
                	 String pathf = dbworker.getPathFromTreePath(pathT);
                    logger.debug("path = " + pathf);
                    int index = pathf.lastIndexOf(".") + 1;
                    String extension =  "";
                    if(index > 0) {
                        extension = pathf.substring(index);
                    }
                    logger.debug("extension = " + extension);
                    try {  
                      // have a provider
                      if(isProvider(pathf)) {
                          System.out.println("Downloading file");
                          dbworker.downloadFile(pathT);
                      
                      } else {
                          System.out.println("No provider, starting program");
//                          startProgram(lookupURI);
                          JobPanel.openEditor(pathf);
                      }
                     }catch(Exception exception) {
                         logger.error("exception ",exception);
                         //startProgram(lookupURI);
                     }
                	}
                	
                }
            }
            	
            if (mouseRow != -1){
                System.out.println("Clicked on a real row");
                
                //If we have clicked a real row, select it, and continue with our logic
                tree.setSelectionRow(mouseRow);
                //if a single right click, popup the menu
                if(isRightClickEvent(mEvent)) {
                    System.out.println("Right clicked, showing popup menu.");
                    showPopupMenu(tree, mEvent.getX(), mEvent.getY());
                //if a double click:    
                } else if ((mEvent.getButton()==MouseEvent.BUTTON1) && (mEvent.getClickCount()==2)) {
                    TreePath treePath = tree.getPathForRow(mouseRow);
                    GridFileTreeNode selectedNode = (GridFileTreeNode) treePath.getLastPathComponent();
                    if(selectedNode.getAllowsChildren()){
                    //If here, user has double clicked on a directory
                    } else {
//                        JDialog newDialog = new JDialog();
                        String path = dbworker.getPathFromTreePath(getSelectedItemsTreePath());
//                        JPanel newPanel = (JPanel) mimehandler.lookup(dbworker.processURIForTopPanel(lookupURI));
//                        if(newPanel==null) {
//                            errorMimeType();
//                        } else {
//                            newDialog.getContentPane().add(newPanel);
//                            newDialog.pack();
//                            newDialog.setVisible(true);
//                        }
                        
                        logger.debug("path = " + path);
                        int index = path.lastIndexOf(".") + 1;
                        String extension =  "";
                        if(index > 0) {
                            extension = path.substring(index);
                        }
                        logger.debug("extension = " + extension);
                        try {  
                          // have a provider
                          if(isProvider(path)) {
                              System.out.println("Downloading file");
                              dbworker.downloadFile(treePath);
                          
                          } else {
                              System.out.println("No provider, starting program");
//                              startProgram(lookupURI);
                              JobPanel.openEditor(path);
                          }
                        }catch(Exception exception) {
                             logger.error("exception ",exception);
                             //startProgram(lookupURI);
                        }
                    }
                }
            }
        }
        
        /**
         * Check to see if the user right clicks with a single button mouse.
         * @param ev
         * @return
         */
        private boolean isRightClickEvent(MouseEvent ev) {
            int mask = InputEvent.BUTTON1_MASK - 1;
            int mods = ev.getModifiers() & mask;
            if (mods == 0) {
                return false;
            } else {
                return true;
            }
        }
        
        /**
         * Should be in the mime handler
         * @param lookupURI
         * @return
         */
        public boolean isProvider(String path) {
//            String provider = lookupURI.getScheme();
//            
//            if(provider == null) {
//                return false;
//            }
//            
//            Iterator iProviders = AbstractionProperties.getProviders().iterator();
//            while(iProviders.hasNext()) {
//                Object item = iProviders.next();
//                if(provider.equals(item)) {
//                    return true;
//                }
//            }
//            
            return true;
        }

        /**
         * Should be in the mime handler
         * @param lookupURI
         */
          public void startProgram(URI lookupURI) {
            JDialog newDialog = new JDialog();
            JPanel newPanel = (JPanel) mimehandler.lookup(lookupURI);
            if(newPanel==null) {
                errorMimeType();
            } else {
                newDialog.getContentPane().add(newPanel);
                newDialog.setSize(500,500);     
                newDialog.setVisible(true);
                newDialog.toFront();
                newDialog.setTitle("View: "+lookupURI);
            }   
          }

        public void mousePressed(MouseEvent event) {
        }

        public void mouseReleased(MouseEvent event) {
        }
        
        public void mouseEntered(MouseEvent event) {
        }

        public void mouseExited(MouseEvent event) {
        }
    }
    
    protected class TreeListener implements TreeWillExpandListener {
        public void treeWillExpand(TreeExpansionEvent tee) {
            startWaiting("Getting directory listing...", true);
            dbworker.treeWillExpand(tee.getPath());
        }
        
        public void treeWillCollapse(TreeExpansionEvent tee) {
            dbworker.treeWillCollapse(tee.getPath());
        }
        
    }
    
    protected class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String label = ((JButton) event.getSource()).getToolTipText();
            if(label.equals("Connect")) {
                startWaiting("Connecting...", false);
                topURIPanel.setEnabled(false);
                goButton.setEnabled(false);
                disconnectButton.setEnabled(false);
                dbworker.goButtonPushed();
            } else if (label.equals("Disconnect")) {
                startWaiting("Disconnecting...", false);
                dbworker.disconnectButtonPushed();
            } else if (label.equals("Home")) {
                System.out.println("Clicked home.");
//                if(connected) {
                    startWaiting("Connecting...", true);
                    dbworker.goHome();
//                }
            } else if (label.equals("Up Directory")) {
                System.out.println("Clicked Up Directory.");
//              if(connected) {
                  startWaiting("Loading parent directory...", true);
                  //Check to see if the user has permission
                  dbworker.goUpDirectory();
//              }
            } else if (label.equals("Refresh")) {
                System.out.println("Clicked refresh.");
//                if(connected) {
                    startWaiting("Refreshing...", true);
                    dbworker.refresh();
//                }
            } else if (label.equals("New Directory")) {
                System.out.println("Clicked create new directory.");
//                if(connected) {
                    dbworker.makeDirectory(createNewDirectoryDialog());
                    startWaiting("Creating new directory...", false);
//                }
            } else if (label.equals("Get Info")) {
                System.out.println("Clicked get info.");
//                if(connected) {
                    createGetInfoDialog(getSelectedItemsGridFile());
//                }
            } else if (label.equals("Stop")) {
                stopWaiting();
                topURIPanel.setEnabled(true);
                if(connected){
                    goButton.setEnabled(false);
                    disconnectButton.setEnabled(true);
                } else {
                    goButton.setEnabled(true);
                    disconnectButton.setEnabled(false);
                }
                dbworker.stopButtonPushed();
                
            }
            
        }
    }
    
    protected class PopupListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JMenuItem item = (JMenuItem)event.getSource();
            System.out.println("Popup action occurred");
            if(item == getInfo){
                System.out.println("Info popup button pushed");
                createGetInfoDialog(getSelectedItemsGridFile());
            } else if(item == newDirectory) {
                System.out.println("New Directory popup button pushed");
                dbworker.makeDirectory(getSelectedItemsTreePath(), createNewDirectoryDialog());
                startWaiting("Creating new directory...", false);
            } else if(item == deleteEntry) {
                System.out.println("Delete popup button pushed");
                if(createDeleteDialog(getSelectedItemsGridFile().getName()))
                    startWaiting("Deleting item...", false);
                    dbworker.deleteItem(getSelectedItemsTreePath());
            } else if(item == goInto) {
                System.out.println("GoInto popup button pushed");
                startWaiting("Going into directory...", true);
                dbworker.goInto(getSelectedItemsTreePath());
            } else if(item == refresh) {
                System.out.println("Refresh popup button pushed");
                startWaiting("Refreshing...", true);
                dbworker.refresh(getSelectedItemsTreePath());
            } else if(item == downloadFile) {
            	System.out.println("Download popup button pushed");
            	
            	JFileChooser chooser = new JFileChooser();
            	chooser.setDialogTitle("Please select a directory");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                
                int returnVal = chooser.showOpenDialog(FileBrowserImpl.this);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                   System.out.println("You chose to save to this folder: " +
                		   chooser.getSelectedFile().getAbsolutePath());
                   dbworker.downloadFileToPath(getSelectedItemsTreePath(), chooser.getSelectedFile().getAbsolutePath());
                }
         /*   } else if(item == downloadAll) {
            	System.out.println("DownloadAll popup button pushed");
            	
            	JFileChooser chooser = new JFileChooser();
            	chooser.setDialogTitle("Please select a directory for files");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                
                int returnVal = chooser.showOpenDialog(FileBrowserImpl.this);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                   System.out.println("You chose to save all files to this folder: " +
                		   chooser.getSelectedFile().getAbsolutePath());
                   TreePath[] = 
                   for (TreePath pathT : dbWorker.){
                	   dbWorker.downloadFilePath.{
                   }
                   dbworker.downloadFileToPath(, chooser.getSelectedFile().getAbsolutePath());
                }
                
                   dbworker.downloadFileToPath(getSelectedItemsTreePath(), chooser.getSelectedFile().getAbsolutePath());
                }
                */
                else {
                	System.out.println("You cancelled");
                }
            }
        }
    }

protected class TopKeyListener implements KeyListener {
    public void keyTyped(KeyEvent arg0) {
    }

    public void keyPressed(KeyEvent keyevent) {
    	if (keyevent.getKeyCode() == KeyEvent.VK_ENTER){
//        	System.out.println("Connected is " + connected);
//            if(!connected){
                startWaiting("Connecting...", true);
                topURIPanel.setEnabled(false);
                goButton.setEnabled(false);
                disconnectButton.setEnabled(false);
                dbworker.goButtonPushed();
//            }
        } else if (keyevent.getKeyCode() == KeyEvent.VK_F5){
//        	System.out.println("Connected is " + connected);
//          if(!connected){
              startWaiting("Refreshing...", true);
              topURIPanel.setEnabled(false);
              goButton.setEnabled(false);
              disconnectButton.setEnabled(false);
              dbworker.refresh();
//          }
      }
    }

    public void keyReleased(KeyEvent arg0) {
    }   
}

//    public void errorOnDelete() {
//        
//    }
    
    public void errorOnGoHome() {
        JOptionPane.showMessageDialog(this, "Cannot open requested directory.\nPermission Denied!!",
                "Error",
                JOptionPane.WARNING_MESSAGE);
        this.stopWaiting();
        
    }

     /**
     * Should be in the mime handler
     * @param lookupURI
     */
    protected void startProgram(URI lookupURI) {
        JDialog newDialog = new JDialog();
        JPanel newPanel = (JPanel) mimehandler.lookup(lookupURI);
        if(newPanel==null) {
            errorMimeType();
        } else {
            newDialog.getContentPane().add(newPanel);
            newDialog.setSize(500,500);     
            newDialog.setVisible(true);
            newDialog.toFront();
            newDialog.setTitle("View: "+lookupURI);
        }   
    }

}
