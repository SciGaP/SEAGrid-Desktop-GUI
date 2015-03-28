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

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.gridchem.client.GridChem;
import org.gridchem.client.SwingWorker;
import org.gridchem.client.Trace;
import org.gridchem.client.optsComponent;
import org.gridchem.client.common.Settings;
import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.exceptions.GMSException;
import org.gridchem.client.exceptions.SessionException;
import org.gridchem.client.gui.filebrowser.commands.CDCommand;
import org.gridchem.client.gui.filebrowser.commands.DownloadCommand;
import org.gridchem.client.gui.filebrowser.commands.FileCommand;
import org.gridchem.client.gui.filebrowser.commands.GETCommand;
import org.gridchem.client.gui.filebrowser.commands.LSCommand;
import org.gridchem.client.gui.filebrowser.commands.MKDIRCommand;
import org.gridchem.client.gui.filebrowser.commands.OPENCommand;
import org.gridchem.client.gui.filebrowser.commands.PWDCommand;
import org.gridchem.client.gui.filebrowser.commands.RMCommand;
import org.gridchem.client.gui.filebrowser.commands.STOPCommand;
import org.gridchem.client.gui.login.LoginDialog;
import org.gridchem.client.gui.panels.myccg.job.JobPanel;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.service.beans.FileBean;
import org.gridchem.service.exceptions.FileManagementException;
import org.gridchem.service.exceptions.PermissionException;

/**
 * This is the controller class for the FileBrowserImpl.  It handles the status change
 * events, forks off actions to the swing worker, and generally does the meat of the 
 * work for the file browser gui.  For those of you following along at home, notice 
 * that the calls to the GMS are performed within a SwingWorker, thus releasing the 
 * event thread to do more user-friendly tasks, like tell the user what's going on.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class FileBrowserWorkerImpl implements StatusListener{
//    private FileTransferObject fileTrans;
    
    private DefaultTreeModel model;
    private GridFileTreeNode top;

    private String pwd;
//    private URI topURI;
    private String rootPath; // Root path of the user directory
    private String slash = File.separator;
    
    //private GridCommandManager gcm;
    private FileBrowserImpl dbgui;
    
    private Hashtable<String, Object> submittedCommands = new Hashtable<String, Object>();
    private FileCommand executingCommand;
//    @SuppressWarnings("unchecked")
	private Vector<String> stoppedCommands = new Vector<String>(5,1);
    
    static Logger logger = Logger.getLogger(FileBrowserWorkerImpl.class.getName());
    
    //private DirectoryBrowserLoggerImpl logger = new DirectoryBrowserLoggerImpl();
    //private LoggerImpl logger = LoggerImpl.createInstance(DirectoryBrowserImpl.class.getName());
    //private int defaultLogLevel = LoggerImpl.DEBUG;
    
    public FileBrowserWorkerImpl(FileBrowserImpl dbgui) {
        this.dbgui = dbgui;
        //Build the directory tree area
        top = new GridFileTreeNode(new FileBean());
        model = new DefaultTreeModel(top, true);
//        fileTrans = new FileTransferObjectImpl(this, gcm);
        //logger.setOutput(dbgui.getLogWindow());
    }
    
//    /**
//     * Sets the file separator based on the protocol.  If this is local, use the local file
//     * separator, otherwise use a slash;
//     * @param protocol
//     */
//    private void setFileSeparator(String protocol) {
//        //if(protocol.equals("file"))
//        //  fileSeparator = File.separator;
//        //else
//            slash = "/";
//    }
    
    /**
     * Return the FileBean object of the selected item.
     * @return
     */
    protected FileBean getGridFileFromTreePath(TreePath path) {
        return (FileBean) ((GridFileTreeNode) path.getLastPathComponent()).getUserObject();
    }
    
    protected String processPathFromTopPanel(String path) {
        
        if (path.equals("")) {
            return ".";
        } else {
        	return path;
        }
       
    }
    
//    protected URI processURIForTopPanel(URI uri) {
//        URI returnURI = null;
//        String authority = topURI.getAuthority();
//        if(authority ==null)
//            authority = "";
//        try {
//            returnURI = new URI(topURI.getScheme(), authority, slash + uri.normalize().getPath(), null, null);
//        } catch (URISyntaxException e) {
//            
//        }
//        return returnURI;
//    }
    
    
    /**
     * Given a selected item's <code>TreePath</code> construct and return its relative path as a string.
     * @param treePath The <code>TreePath</code> of the selected item.
     * @return The path of the item in string form
     */
    protected String getPathFromTreePath(TreePath treePath) {
        int count = treePath.getPathCount();
        String path = ((FileBean)((GridFileTreeNode)model.getRoot()).getUserObject()).getPath() ; //commented -nik + "/";
        
        path = path + "/";
        System.out.println("BEFORE resolving treepath " + path);
        
        for(int i =1; i<count;i++) {
            path+=treePath.getPathComponent(i) + "/" ; 
        }
        
        if (path != null)
            System.out.println("PWD is " + path);
        else 
            System.out.println("PWD is not available");
        
        //If the last node is not a directory, remove the fileseparator
        if(!getGridFileFromTreePath(treePath).isDirectory() && path.endsWith("/")) {
            path = path.substring(0, (path.length()-1));
        }
        
        System.out.println("path from tree path: " + path);
        
        return path;
        
//        URI returnURI = null;
//            
//        try{
//            returnURI = new URI(null, path, null);
//        } catch (URISyntaxException e) {
//        }
//        
//        //the URI must be normalized first to put in the proper number of fileseps...
//        //return this.normalizeAbsoluteURI(returnURI);
//        return returnURI.normalize();
    }
    
    
    /**
     * Returns the uri of the treepath if the last node in the path is a directory.
     * Otherwise the uri of the parent of the last node is returned.
     * Only a directory uri is returned.
     * @param treePath
     * @return
     */
    protected String getDirectoryPathFromTreePath(TreePath treePath) {
        int count = treePath.getPathCount();
        String path = ((FileBean)top.getUserObject()).getPath();
        
        int i = 1;
        while(i<count && ((DefaultMutableTreeNode) treePath.getPathComponent(i)).getAllowsChildren()) {
            path+=treePath.getPathComponent(i) + slash;
            i++;
        }   
        
        return path;
//        
//        URI returnURI = null;
//        
//        try{
//            returnURI = new URI(null, path, null);
//        } catch (URISyntaxException e) {
//        }
//        return returnURI.normalize();
    }
    
    
    /**
     * Returns the string representation of the last item in the tree path.
     * @param path
     * @return
     */
    protected String getStringOfTreePath(TreePath path) {
        return path.getLastPathComponent().toString();
    }
    
    
    /**
     * Return the datamodel of the tree...used in JTree creation.
     * @return data model for directory tree
     */
    protected DefaultTreeModel getTreeDataModel() {
        return model;
    }
    
    
    /**
     * Called when the user asks to delete an item.  
     * @param path TreePath of item to delete.
     */
    protected void deleteItem(final TreePath path) {
        logger.info("Deleting " + this.getStringOfTreePath(path));
        
        final FileCommand deleteCommand = new RMCommand(this);
        
        try {
            dbgui.startWaiting("Deleting remote directory...",true);
            SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    
                    deleteCommand.getArguments().put("path",getPathFromTreePath(path));
                    
                    setExecutingCommand(deleteCommand);
                    
                    addCommand(deleteCommand, path);
                    
                    try {
                        deleteCommand.execute();
                    } catch (SessionException e) {

                    	optsComponent.monitorWindow.setUpdate(false);
                        
                        int viewLog = JOptionPane.showConfirmDialog(
                                optsComponent.monitorWindow,
                                "Your session has expired. Would\n" + 
                                "you like to reauthenticate to the CCG?",
                                "Session Timeout",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE
                        );
                            
                        if (viewLog == 0) {
                            LoginDialog.clearLogin();
                            GridChem.oc.doAuthentication();
                        }
                    } catch (Exception e) {
                        logger.error("Could not connect!", e);
                        dbgui.connectFailed(e.getLocalizedMessage());
                    }
                    
                    return null;
                }
                public void finished() {
                    //dbgui.stopWaiting();
                }
            };
            
            worker.start();
            
        } catch (Exception except) {
            dbgui.errorList();
        }
    }
    
    
    /**
     * We throw a warning here since the user is always put at their root home directory.
     */
    protected void goHome() {
        FileBean topFileInfo = new FileBean();
        topFileInfo.setName(".");
        topFileInfo.setDirectory(true);
        topFileInfo.setPath(".");
        //topFileInfo.setFileType(GridFile.DIRECTORY);
        top.setUserObject(topFileInfo);
        
        treeWillExpand(new TreePath(top.getPath()));
//        Trace.exit();  
    }
    
    /**
     * Move to the parent directory of the currently listed URI. Notice that
     * whatever value is in the file browser's uri field will be taken as
     * the current directory when this is called.
     */
    protected void goUpDirectory() {
        FileBean topFileInfo = new FileBean();
        String child = dbgui.getPath();
        System.out.println("FileBrowserWorkerImpl_344:current root is " + child);
        if (child.charAt(child.length()-1) == '/') {
            child = child.substring(0,child.length()-1);
            System.out.println("modified current root to " + child);
        }
        String parent = dbgui.getPath().substring(0,child.lastIndexOf("/"));
        System.out.println("new parent directory is " + parent);
        /*
        topFileInfo.setName(parent);
        topFileInfo.setDirectory(true);
        topFileInfo.setPath(parent);
        //topFileInfo.setFileType(GridFile.DIRECTORY);
        top.setUserObject(topFileInfo);
        */
        
        //checking permissions
        if (Settings.authenticatedGridChem) {
        	System.out.println("FileBrowserWorkerImpl: File Path Check: "+dbgui.getPath());
            if(parent.equalsIgnoreCase("internal") || parent.equalsIgnoreCase("/home/ccguser/mss/internal")) {
                JOptionPane.showMessageDialog(dbgui, 
                        "Contents of directory restricted. Access denied.",
                        "Browse Error", 
                        JOptionPane.ERROR_MESSAGE);
                //finishFileListAction();
              FileBrowserImpl.stopWaiting();
                return;
            } else {
                System.out.println("ALERT!!!! directory is " + parent + "\n this is ok!?!?" );          
                return;
              }
    } else {
            if(parent.equalsIgnoreCase("external") || parent.equalsIgnoreCase("/home/ccguser/mss/external")) {
                JOptionPane.showMessageDialog(dbgui, 
                        "Contents of directory restricted. Access denied.",
                        "Browse Error", 
                        JOptionPane.ERROR_MESSAGE);
                //finishFileListAction();
                FileBrowserImpl.stopWaiting();
                return;
            } /*else {
                System.out.println("ALERT!!!! directory is " + parent + "\n this is ok!?!?" );
                return;
               }*/
           
        }

        
        //done checking permission
       
        //String parent = dbgui.getPath().substring(0,child.lastIndexOf("/"));
        //System.out.println("new parent directory is " + parent);
        topFileInfo.setName(parent);
        topFileInfo.setDirectory(true);
        topFileInfo.setPath(parent);
        //topFileInfo.setFileType(GridFile.DIRECTORY);
        top.setUserObject(topFileInfo);
        dbgui.setPath(parent);
        
        treeWillExpand(new TreePath(top.getPath()));
    }
    
    
   
    /**
     * Creates a directory with the given name under the current root
     * directory.
     * 
     * @param newDirName
     */
    protected void makeDirectory(String newDirName) {
        if (newDirName != null) {
            this.makeDirectory(new TreePath(top), newDirName);
        }
    }
    
    /**
     * Called when the user asks to create a new directory.
     * 
     * @param path TreePath of selection. (ie) where to put the directory.
     * @param newDirName The name of the new directory
     */
    protected void makeDirectory(TreePath path, String newDirName) {
        logger.info("Creating directory " + newDirName);
        
        String nodePath = null;
        TreePath hashPath = null; //helps us decide what node to refresh
        
        //if our selection allows kids (is a directory) create the directory under it.
        //else create the directory under the parent (which has to be a directory)
        if(((DefaultMutableTreeNode) path.getLastPathComponent()).getAllowsChildren()) {
        	nodePath = this.getPathFromTreePath(path);
            hashPath = path;
        } else {
        	nodePath = this.getPathFromTreePath(path.getParentPath());
            hashPath = path.getParentPath(); //refresh the parent node in this case
        }
        //create the newURI to make
        final String newPath = nodePath + slash + newDirName;
        
        final TreePath parentPath = hashPath;
        final FileCommand mkdirCommand = new MKDIRCommand(this);
        
        try {
            dbgui.startWaiting("Creating remote directory...",true);
            SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    
                    mkdirCommand.getArguments().put("path",newPath);
                    
                    addCommand(mkdirCommand, parentPath.getLastPathComponent());
                    
                    setExecutingCommand(mkdirCommand);
                    
                    try {
                        mkdirCommand.execute();
                    } catch (SessionException e) {

                    	optsComponent.monitorWindow.setUpdate(false);
                        
                        int viewLog = JOptionPane.showConfirmDialog(
                        		optsComponent.monitorWindow,
                                "Your session has expired. Would\n" + 
                                "you like to reauthenticate to the CCG?",
                                "Session Timeout",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE
                        );
                            
                        if (viewLog == 0) {
                        	LoginDialog.clearLogin();
                            GridChem.oc.doAuthentication();
                        }
                    } catch (GMSException e) {
                        logger.error("Could not connect!");
                        dbgui.errorOpen();
                        e.printStackTrace();
                    } catch (Exception e) {
                        logger.error("Could not connect!");
                        dbgui.errorOpen();
                        e.printStackTrace();
                    }
                    
                    return null;
                }
                public void finished() {
                    //dbgui.stopWaiting();
                }
            };
            
            worker.start();
            
        } catch (Exception except) {
            dbgui.errorList();
        }
    }
    
    
    /**
     * Called when the user asks to 'go into' a directory rather than just expanding it.
     * @param path TreePath of selected item.
     */
    protected void goInto(TreePath path) {
        logger.info("Going into " + this.getStringOfTreePath(path));
        
        model.setRoot((GridFileTreeNode) path.getLastPathComponent());
        
        top = (GridFileTreeNode) path.getLastPathComponent();
        
        final String sPath = getPathFromTreePath(path);
        final FileCommand chdirCommand = new CDCommand(this);
        
        try {
            dbgui.startWaiting("Retrieving folder listing...",true);
            SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    
                    chdirCommand.getArguments().put("path",sPath);
                    
                    setExecutingCommand(chdirCommand);
                    
                    dbgui.setPath(processPathFromTopPanel(sPath));
                    
                    try {
                        chdirCommand.execute();
                    } catch (SessionException e) {

                        optsComponent.monitorWindow.setUpdate(false);
                        
                        int viewLog = JOptionPane.showConfirmDialog(
                                optsComponent.monitorWindow,
                                "Your session has expired. Would\n" + 
                                "you like to reauthenticate to the CCG?",
                                "Session Timeout",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE
                        );
                            
                        if (viewLog == 0) {
                            LoginDialog.clearLogin();
                            GridChem.oc.doAuthentication();
                        }
                    } catch (Exception e) {
                        logger.error("Could not connect!", e);
                        dbgui.connectFailed(e.getLocalizedMessage());
                    }
                    
                    return null;
                }
                public void finished() {
                    dbgui.stopWaiting();
                }
            };
            
            worker.start();
            
        } catch (Exception except) {
            dbgui.errorList();
        }
    }
    
    
    
    /**
     * Gets a new directory listing for the current directory and
     * rebuilds the tree.
     */
    protected void refresh() {
        this.refresh(new TreePath(top));
    }
    
    /**
     * Called when the user asks to refresh an already open folder.
     * @param path TreePath of selected item.
     */
    protected void refresh(TreePath path) {
        logger.info("Refreshing " + this.getStringOfTreePath(path));
        treeWillExpand(path);
    }
    
    protected void downloadFileToPath(TreePath path, String localPath) {
    	final String sPath = getPathFromTreePath(path);
        final FileCommand getCommand = new DownloadCommand(this);
        final FileBean file = dbgui.getSelectedItemsGridFile();
        Hashtable<String, Object> args = new Hashtable<String, Object>();
        args.put("localFilePath", localPath);
        getCommand.setArguments(args);
        
        try {
            dbgui.startWaiting("Requesting file...",true);
            SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    
                    getCommand.getArguments().put("path",sPath);
                    //getCommand.getArguments().put("host", "gridchem-mw.ncsa.illinois.edu");
                    getCommand.getArguments().put("host", "gridchem.uits.iu.edu");
                    getCommand.getArguments().put("size",new Long(file.getLength()));
                    setExecutingCommand(getCommand);
                    
                    dbgui.setPath(processPathFromTopPanel(sPath));
                    
                    try {
                        getCommand.execute();
                    } catch (SessionException e) {

                        optsComponent.monitorWindow.setUpdate(false);
                        
                        int viewLog = JOptionPane.showConfirmDialog(
                                optsComponent.monitorWindow,
                                "Your session has expired. Would\n" + 
                                "you like to reauthenticate to the CCG?",
                                "Session Timeout",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE
                        );
                            
                        if (viewLog == 0) {
                            LoginDialog.clearLogin();
                            GridChem.oc.doAuthentication();
                        }
                    } catch (FileManagementException e) { 
                    	logger.error("Remote file not found");
                    	dbgui.errorCopy();
                    	e.printStackTrace();
                    } catch (Exception e) {
                        logger.error("Could not connect!", e);
                        dbgui.connectFailed(e.getLocalizedMessage());
                        e.printStackTrace();
                        
                    }
                    
                    return null;
                }
                public void finished() {
                    dbgui.stopWaiting();
                }
            };
            
            worker.start();
            
        } catch (Exception except) {
            dbgui.errorList();
        }

    }
    
    /**
     * Retrieves the file represented by the current tree path to 
     * the local machine.  The file will be saved using the same
     * path structure within the user's data directory as on the 
     * remote machine.
     * 
     * @param path
     */
    protected void downloadFile(TreePath path) {
        
        final String sPath = getPathFromTreePath(path);
        final FileCommand getCommand = new GETCommand(this);
        final FileBean file = dbgui.getSelectedItemsGridFile();
        
        try {
            dbgui.startWaiting("Requesting file...",true);
            SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    
                    getCommand.getArguments().put("path",sPath);
                    //getCommand.getArguments().put("host", "gridchem-mw.ncsa.illinois.edu");
                    getCommand.getArguments().put("host", "gridchem.uits.iu.edu");
                    getCommand.getArguments().put("size",new Long(file.getLength()));
                    setExecutingCommand(getCommand);
                    
                    dbgui.setPath(processPathFromTopPanel(sPath));
                    
                    try {
                        getCommand.execute();
                    } catch (SessionException e) {

                        optsComponent.monitorWindow.setUpdate(false);
                        
                        int viewLog = JOptionPane.showConfirmDialog(
                                optsComponent.monitorWindow,
                                "Your session has expired. Would\n" + 
                                "you like to reauthenticate to the CCG?",
                                "Session Timeout",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE
                        );
                            
                        if (viewLog == 0) {
                            LoginDialog.clearLogin();
                            GridChem.oc.doAuthentication();
                        }
                    } catch (FileManagementException e) { 
                    	logger.error("Remote file not found");
                    	dbgui.errorCopy();
                    	e.printStackTrace();
                    } catch (Exception e) {
                        logger.error("Could not connect!", e);
                        dbgui.connectFailed(e.getLocalizedMessage());
                        e.printStackTrace();
                        
                    }
                    
                    return null;
                }
                public void finished() {
                    dbgui.stopWaiting();
                }
            };
            
            worker.start();
            
        } catch (Exception except) {
            dbgui.errorList();
        }
    }
    
    /**
     * A tree is expanded so all data for the corresponding directory is fetched.
     * @param path TreePath of the node being expanded.
     */
    protected void treeWillExpand(final TreePath path) {
        
        String lsPath;
        
        //just perform a regular ls if the top is expanding
        if(path.getLastPathComponent()==top) {
            logger.info("Listing contents of present working directory");
            lsPath = this.getPathFromTreePath(new TreePath(top));
        } else {
            logger.info("Listing contents of " + this.getStringOfTreePath(path));
            //if not expanding the top, get the path of the selected item and get its contents
            lsPath = getPathFromTreePath(path);
        }
        
        final String sPath = lsPath;
        
        final FileCommand lsCommand = new LSCommand(this);
        
        try {
            dbgui.startWaiting("Retrieving listing...",true);
            SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    
                    lsCommand.getArguments().put("path",sPath);
                    
                    addCommand(lsCommand, path.getLastPathComponent());
                    
                    setExecutingCommand(lsCommand);
                    
                    try {
                        lsCommand.execute();
                    } catch (SessionException e) {

                        optsComponent.monitorWindow.setUpdate(false);
                        
                        int viewLog = JOptionPane.showConfirmDialog(
                                optsComponent.monitorWindow,
                                "Your session has expired. Would\n" + 
                                "you like to reauthenticate to the CCG?",
                                "Session Timeout",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE
                        );
                            
                        if (viewLog == 0) {
                            LoginDialog.clearLogin();
                            GridChem.oc.doAuthentication();
                        }
                    } catch (Exception e) {
                        logger.error("Could not connect!", e);
                        dbgui.connectFailed(e.getLocalizedMessage());
                    }
                    
                    return null;
                }
                public void finished() {
                    dbgui.stopWaiting();
                }
            };
            
            worker.start();
            
        } catch (Exception except) {
            dbgui.errorList();
        }
    }
    
    /**
     * When a tree is collapsed all the subnodes are cleared since we're refetching them anyway.
     * This keeps the user from briefly seeing the old nodes.
     * @param path TreePath of the node being collapsed.
     */
    protected void treeWillCollapse(TreePath path) {
        GridFileTreeNode node = (GridFileTreeNode) path.getLastPathComponent();
        node.removeAllChildren();
        model.reload(node);
    }
    

    
    /**
     * @param username
     * @deprecated all interaction is done through the GMS_WS
     */
    protected void setUsername(String username) {
        logger.debug("setUsername("+username+")");
        Settings.gridchemusername = username;
    }
    
    
    /**
     * @param password
     * @deprecated all interaction is done through the GMS_WS
     */
    protected void setPassword(String password) {   
        Settings.pass.selectAll();
        Settings.pass.replaceSelection(password);
    }
    
    
    /**
     * The method performed when the go button is pushed.  This sets up the first
     * directory, making it visible if its the first location that has been loaded.
     *
     */
    protected void goButtonPushed() {
        logger.info("Connecting....");
        Trace.entry();
        try {
            rootPath = dbgui.getPath();
        
            if (rootPath == null){
                dbgui.errorOpen();
                return;
            }
            
//            String uriScheme = topURI.getScheme();
//            String uriHost = topURI.getHost();
//            if (uriScheme == null || "".equals(uriScheme)) {
//                uriScheme = "gridftp";
//            }
//            if (uriHost == null || "".equals(uriScheme)) {
//                uriHost = dbgui.getURI().toString();
//            }
            
//            logger.debug("provider="+uriScheme);
//            logger.debug("host="+uriHost);
//            
//            //set the file separator to the local machine separator or the remote machine separator
//            this.setFileSeparator(uriScheme);
//            
//            if(!(uriScheme.equals("gridftp")||uriScheme.equals("gsiftp")||uriScheme.equals("file"))){
//                dbgui.errorProtocol();
//                topURI = null;
//            }
//            
////            FileCommand openCommand = new OPENCommand(this.gms,this);
////            
////            openCommand.getArguments().put("uri",dbgui.getURI());
////            
////            this.setExecutingCommand(openCommand);
////
////            Trace.note("Listing files at " + topURI.toString());
////            
////            dbgui.startWaiting("Connecting to server...",true);
////            openCommand.execute();
//            
            final FileCommand openCommand = new OPENCommand(this);
            
            dbgui.startWaiting("Connecting to server...",true);
            SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    
                    openCommand.getArguments().put("uri",dbgui.getPath());
                    
                    setExecutingCommand(openCommand);
                    
                    try {
                        openCommand.execute();
                    } catch (SessionException e) {

                        optsComponent.monitorWindow.setUpdate(false);
                        
                        int viewLog = JOptionPane.showConfirmDialog(
                                optsComponent.monitorWindow,
                                "Your session has expired. Would\n" + 
                                "you like to reauthenticate to the CCG?",
                                "Session Timeout",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.ERROR_MESSAGE
                        );
                            
                        if (viewLog == 0) {
                            LoginDialog.clearLogin();
                            GridChem.oc.doAuthentication();
                        }
                    } catch (Exception e) {
                        logger.error("Could not connect!", e);
                        dbgui.connectFailed(e.getLocalizedMessage());
                    }
                    
                    return null;
                }
                public void finished() {}
            };
            
            worker.start();
           
        } catch (Exception except) {
            except.printStackTrace();
            logger.error(except);
            dbgui.errorList();
        }

        Trace.exit();
        
    }
    
    
    /**
     * Disconnect when the button is pushed.  Called by an actionlistener.
     * Also clear out all the old stuff in the window.
     */
    protected void disconnectButtonPushed() {
        logger.info("Disconnecting...");
        top.removeAllChildren();
        model.reload(top);
        
        try {
            STOPCommand stopCommand = new STOPCommand(this);
            stopCommand.execute();
        } catch (SessionException e) {

            optsComponent.monitorWindow.setUpdate(false);
            
            int viewLog = JOptionPane.showConfirmDialog(
                    optsComponent.monitorWindow,
                    "Your session has expired. Would\n" + 
                    "you like to reauthenticate to the CCG?",
                    "Session Timeout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE
            );
                
            if (viewLog == 0) {
                LoginDialog.clearLogin();
                GridChem.oc.doAuthentication();
            }
        } catch (GMSException e) {
            logger.error("Could not connect!");
            dbgui.errorOpen();
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Could not connect!");
            dbgui.errorOpen();
            e.printStackTrace();
        }
        //fileTrans.disConnect();
    }
    
    
    /**
     * This simply resets the panel.  The SwingWorker task is still running.
     * In the future, this needs to call the interrupt method of the swing
     * worker to kill the task.
     */
    protected void stopButtonPushed() {
        try{
            //stoppedCommands.add(executingCommand.getIdentity());
            stoppedCommands.add(executingCommand.getId().toString());
        } catch(NullPointerException e) {
            
        }
    }
    
//    private void removeStoppedCommand(GridCommand command) {
//        //stoppedCommands.remove(command.getIdentity());
//        stoppedCommands.remove(command.getId().toString());
//    }
    
    /**
     * Creates a subtree under the parent node based on the results of an LSCOMMAND.
     * 
     * @param uri where to perform the ls
     * @param parent the node on which to add the new children
     */
    private void createTree(List<FileBean> fileBeans, DefaultMutableTreeNode parent) {
        Trace.entry();
        try {
            
            parent.removeAllChildren();
            
            System.out.println("After removing children");
            System.out.println("Root is: "+ ((FileBean)parent.getUserObject()));
            
            //parent.add(new GridFileTreeNode((FileBean)parent.getUserObject()));
            if (!fileBeans.isEmpty())
                ((DefaultMutableTreeNode) parent).setAllowsChildren(true);
            
            for(FileBean child: fileBeans) {
            	if (child.getName().equals(".") || child.getName().equals("..")) continue;
//                System.out.println("Adding child: " + child.getName());
                parent.add(new GridFileTreeNode(child));
//                System.out.println("Total children: " + parent.getChildCount());
                if(child.isDirectory()) {
                    //a directory
                    ((DefaultMutableTreeNode) parent.getLastChild()).setAllowsChildren(true);
    //              The GAT can't support soft links yet
    //            } else if(fileInfo.isSoftLink()) {
    //                //a softlink
    //                ((DefaultMutableTreeNode) parent.getLastChild()).setAllowsChildren(false);
    //                String name = ((FileBean)((DefaultMutableTreeNode) parent.getLastChild()).getUserObject()).getName();
    //                
    //                //split the  name into the actual name and the thing its linking to
    //                String[] parts = name.split(" -> ");
    //                ((FileBean)((DefaultMutableTreeNode) parent.getLastChild()).getUserObject()).setName(parts[0]);
    //                
    //                URI isDirURI = getPathFromTreePath(new TreePath(parent.getPath()));
    //            
    //                //resolve the uri with the second part and normalize it for the grid
    //                GridCommand isDirCommand = fileTrans.isDirectory(isDirURI.resolve(parts[1]));
    //                
    //                addCommand(isDirCommand, parent.getLastChild());
    //                try {
    //                    fileTrans.execute(isDirCommand, true);
    //                } catch (Exception e1) {
    //                }
    //                
                } else {
                    //not a directory or a softlink
                    ((DefaultMutableTreeNode) parent.getLastChild()).setAllowsChildren(false);
                }
            }
            
            //SHOULD JUST HAVE TO RELOAD NODE, NOT THE ENTIRE TREE.
            model.reload(parent);
 
            Trace.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    protected void addCommand(FileCommand command, Object obj) {
        //submittedCommands.put(command.getIdentity().toString(), obj);
        submittedCommands.put(command.getId(), obj);
    }
    
    private Object getCommand(FileCommand command) {
        //return submittedCommands.get(command.getIdentity().toString());
        return submittedCommands.get(command.getId());
    }
    
    private void removeCommand(FileCommand command) {
        //submittedCommands.remove(command.getIdentity().toString());
        submittedCommands.remove(command.getId());
    }
    
    /**
     * This sets the currently executing command so that users can cancel it if they wish 
     * with the stop button.  
     * @param command
     */
    private void setExecutingCommand(FileCommand command) {
        executingCommand = command;
    }
    
    private boolean hasCommandBeenStopped(FileCommand command) {
        //return stoppedCommands.contains(command.getIdentity());
        return stoppedCommands.contains(command.getId());
    }
    
    private void removeStoppedCommand(FileCommand command) {
        //stoppedCommands.remove(command.getIdentity());
        stoppedCommands.remove(command.getId());
    }
    
    /**
     * This is the status changed listener that is called whenever a command completes, 
     * fails, etc.  Much of the work is done here, but beware, this does not always
     * run in the event queue thread.
     */
    public void statusChanged(StatusEvent event) {
        Trace.entry();
        Status status = event.getStatus();
//        System.out.println("Status changed to: " + status.name());
//        System.out.println("StatusListener is: " + event.getSource().getClass().getName());
        
        final FileCommand command = (FileCommand) event.getSource();
        logger.debug("stats=" + status.name() + ", type=" + command.getClass());
        try {
            //What to do if things complete successfully.
            if (status.equals(Status.COMPLETED)) {
                System.out.println(command.getCommand() + " Command Completed");
                if(this.hasCommandBeenStopped(command)) {
                    this.removeStoppedCommand(command);
                    return;
                }
                
                if (command.getCommand().equals(FileCommand.START)) {
                    //open returns sessionid for future reference
                    //fileTrans.setSessionId((Identity) command.getOutput());
                    //set the current directory to what the user has entered
                    Trace.note("Executing LS command");
                    FileBean topFileInfo = new FileBean();
                    topFileInfo.setName(rootPath);
                    topFileInfo.setDirectory(true);
                    topFileInfo.setPath(rootPath);
                    //topFileInfo.setFileType(GridFile.DIRECTORY);
                    top.setUserObject(topFileInfo);
                    
                    treeWillExpand(new TreePath(top.getPath()));
                    
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            dbgui.getCurrentDirectoryCompleted(true);
                        }
                    });
                } else if (command.getCommand().equals(FileCommand.CD)) {
                    final String isConnect = (String) getCommand(command);
                    Trace.note("Finished CD, Executing PWD command");
                    
                    //get and save the current directory 
                    //this is required to get the _absolute_ path of the current dir 
                    //so we can construct URI's in the future.
                    final FileCommand pwdCommand = new PWDCommand(this);
                    
                    dbgui.startWaiting("Getting present working directory...",true);
                    SwingWorker worker = new SwingWorker() {
                        public Object construct() {

                            // are we doing this in the connect sequence? if so, 
                            // pass on that information to the next step in the 
                            // sequence, getCurrentDirectory
                            if(isConnect !=null && isConnect.equals("connect")) {
                                addCommand(pwdCommand, "connect");
                                removeCommand(command);
                            }
                            
                            try {
                                pwdCommand.execute();
                            } catch (SessionException e) {

                                optsComponent.monitorWindow.setUpdate(false);
                                
                                int viewLog = JOptionPane.showConfirmDialog(
                                        optsComponent.monitorWindow,
                                        "Your session has expired. Would\n" + 
                                        "you like to reauthenticate to the CCG?",
                                        "Session Timeout",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.ERROR_MESSAGE
                                );
                                    
                                if (viewLog == 0) {
                                    GridChem.appendMessage("Resetting user authentication...");
                                    LoginDialog.clearLogin();
                                    GridChem.appendMessage("Complete\n");
                                    GridChem.oc.updateAuthenticatedStatus();
                                    GridChem.oc.doAuthentication();
                                }
                            } catch (Exception e) {
                                logger.error("Could not connect!", e);
                                dbgui.connectFailed(e.getLocalizedMessage());
                            }
                            
                            return null;
                        }
                        public void finished() {
                            dbgui.stopDownload();
                            dbgui.stopWaiting();
                        }
                    };
                    
                    worker.start();
                    
                } else if (command.getCommand().equals(FileCommand.PWD)) {
                    final String isConnect = (String) getCommand(command);
                    //this is important but we probably do it only if we're first connecting
                    Trace.note("Finished PWD, beginning connect");
                    pwd = (String)command.getOutput();
                    
                    if(!pwd.endsWith(slash))
                        pwd += slash;
                    
                    //the following should only occur if first connecting or 'going into'
                    FileBean topFileInfo = new FileBean();
                    topFileInfo.setName(rootPath);
                    topFileInfo.setDirectory(true);
                    //topFileInfo.setFileType(GridFile.DIRECTORY);
                    top.setUserObject(topFileInfo);
                    
                    treeWillExpand(new TreePath(top.getPath()));
                    
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            boolean isInConnectSequence = false;
                            if(isConnect !=null && isConnect.equals("connect")) {
                                isInConnectSequence = true;
                            } 
                            dbgui.getCurrentDirectoryCompleted(isInConnectSequence);
                        }
                    });
                    
                } else if (command.getCommand().equals(FileCommand.LS)) {
    //                final Collection lsEnum = (Collection) command.getOutput();
    //                final GridFileTreeNode parentNode = (GridFileTreeNode) getCommand(command);
                    Trace.note("Finished LS, creating tree");
                    System.out.println("Command returned: " + ((LSCommand)command).getOutput());
                    final List<FileBean> beans = ((LSCommand)command).getOutput();
                    final GridFileTreeNode parentNode = (GridFileTreeNode) getCommand(command);
                    removeCommand(command);
                    
                    // LS command finished successfully, now update the tree with the new
                    // tree.  This must be done in the event thread.
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            createTree(beans, parentNode);
                            dbgui.listingCompleted();
                        }
                    });
                } else if (command.getCommand().equals(FileCommand.MKDIR)) {
                    Trace.note("Finished mkdir, expanding tree");
                    final TreePath path = (TreePath) getCommand(command);
                    removeCommand(command);
                    
                    // MKDIR command finished successfully, now update the tree 
                    // with the new tree.  This must be done in the event thread.
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            treeWillExpand(path);
                        }
                    });
                } else if (command.getCommand().equals(FileCommand.RM)) {
                    Trace.note("Finished deteting item, expanding tree");
                    final TreePath path = (TreePath) getCommand(command);
                    removeCommand(command);
                    
                    // item was deleted.  now update the tree.
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            treeWillExpand(path.getParentPath());
                        }
                    });
                
                } else if (command.getCommand().equals(FileCommand.RMDIR)) {
                    Trace.note("Finished rmdir, expanding tree");
                    final TreePath path = (TreePath) getCommand(command);
                    removeCommand(command);
                    
                    
                    // dir was deleted.  now update the tree.
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            treeWillExpand(path.getParentPath());
                        }
                    });
                } else if (command.getCommand().equals(FileCommand.STOP)) {
                    Trace.note("Finished stop, disconnecting");
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            dbgui.disconnectCompleted();
                        }
                    });
                } else if (command.getCommand().equals(FileCommand.DP) ||
                	command.getCommand().equals(FileCommand.GETBLOCK)) {
                	Trace.note("Finished retrieving file, moving file");
                	final String localPath = (String)command.getArguments().get("localFilePath");
                    
                    final File file = (File)command.getOutput();
                    
                    // file downloaded successfully.  now move the file to new
                    // directory
                    if(file.exists()) {
                        removeCommand(command);
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                dbgui.fileTransferCompleted();
                                System.out.println("Download successful, starting program");                             
                                file.renameTo(new File(localPath, file.getName()));
                                JOptionPane.showMessageDialog(dbgui, "File successfully downloaded");
                            }
                        });
                    } else {
                    	JOptionPane.showMessageDialog(dbgui, "Download failed");
                    }
                } else if (command.getCommand().equals(FileCommand.CP) ||
                        command.getCommand().equals(FileCommand.GETBLOCK)) {
                    Trace.note("Finished retrieving file, launching viewer");
                    
                    final File file = (File)command.getOutput();
                    
                    // file downloaded successfully.  now open with the default
                    // system text editor.  we should register the system editor
                    // with the mimehandler class rather than call it explicitly.
                    if(file!=null) {
                        removeCommand(command);
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                dbgui.fileTransferCompleted();
                                System.out.println("Download successful, starting program");
//                                dbgui.startProgram(file.toURI());
                                JobPanel.openEditor(file.getAbsolutePath());
                            }
                        });
                    }
                } else if(command.getCommand().equals(FileCommand.URLCOPY)||command.getCommand().equals(FileCommand.PUTDIR)
                         || command.getCommand().equals(FileCommand.PUTFILE) ||command.getCommand().equals(FileCommand.GETDIR)) {
                    Trace.note("Finished get/put, expanding tree");
                    final TreePath path = (TreePath) getCommand(command);
                    
                    // we should probably open up a file browser window if 
                    // a directory was just downloaded.
                    if(path!=null) {
                        removeCommand(command);
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                treeWillExpand(path);
                                dbgui.fileTransferCompleted();
                            }
                        });
                    }
                } else if (command.getCommand().equals(FileCommand.ISDIRECTORY)){
                    Trace.note("Finished isdirectory, rebuilding tree");
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) getCommand(command);
                    final DefaultMutableTreeNode finalnode = (DefaultMutableTreeNode) getCommand(command);
                    //this means the thing we're linking to is a directory
                    if(((Boolean)command.getOutput()).booleanValue()) {
                        node.setAllowsChildren(true);
                    }
                    removeCommand(command);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            model.reload(finalnode);
                        }
                    });
                    
                }
////           File retrieval was broken into multiple parts to enable large files
////           to be downloaded without having to hold the entire thing in memory.
////           to do this, we download 56K blocks and append them into a single
////           file.  
              // this has been deprecated in favor of the MTOM support in axis2
            } else if (status.equals(Status.DOWNLOADING)) {
                EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        dbgui.startDownload(
                                ((Long)command.getArguments().get("totalBlocks")).intValue());
                    }
                });
            } else if (status.equals(Status.READY)) {
            	EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        dbgui.updateDownloadProgress(
                                ((Long)command.getArguments().get("blocksReceived")).intValue());
                    }
                });
            //What to do if things fail:    
            } else if (status.equals(Status.FAILED)) {
                Trace.note("Command failed");
                logger.error("A command failed.");
                System.out.println(command.getCommand() + " command failed");
                if(command.getCommand().equals(FileCommand.START)) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            logger.error("Could not connect! ");
                            dbgui.errorOpen();
                        }});
                } else if(command.getCommand().equals(FileCommand.LS)) {
                    final List<FileBean> fileBeans = (List<FileBean>) ((LSCommand)command).getOutput();
                    final GridFileTreeNode parentNode = (GridFileTreeNode) getCommand(command);
                    
                    if (fileBeans != null) 
                        removeCommand(command);
                    
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if (fileBeans == null || fileBeans.isEmpty()) {
                                System.out.println("Could not list directory!");
                                dbgui.errorList();
                                dbgui.disconnectCompleted();
                            } else {
                                createTree(fileBeans, parentNode);
                                dbgui.listingCompleted();
                            }
                        }
                    });
                    //error in setcurrentdirectory, let the gui know if we're in the connect sequence
                    //or not
                } else if(command.getCommand().equals(FileCommand.CD)) {
                    final String isConnect = (String) getCommand(command);
                    removeCommand(command);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            boolean isInConnectSequence = false;
                            if(isConnect !=null && isConnect.equals("connect")) {
                                isInConnectSequence = true;
                            } 
                            logger.error("Could not set current directory!");
                            dbgui.errorSetCurrentDirectory(isInConnectSequence);
                        }
                    });
                    //error in getcurrentdirectory, let the gui know if we're in the connect sequence
                    //or not
                } else if(command.getCommand().equals(FileCommand.PWD)) {
                    final String isConnect = (String) getCommand(command);
                    removeCommand(command);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            boolean isInConnectSequence = false;
                            if(isConnect !=null && isConnect.equals("connect")) {
                                isInConnectSequence = true;
                            } 
                            logger.error("Could not get the name of the current directory!");
                            dbgui.errorGetCurrentDirectory(isInConnectSequence);
                        }
                    });
                } else if(command.getCommand().equals(FileCommand.UPDIR)) {
                    final String isConnect = (String) getCommand(command);
                    removeCommand(command);
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            boolean isInConnectSequence = false;
                            if(isConnect !=null && isConnect.equals("connect")) {
                                isInConnectSequence = true;
                            } 
                            logger.error("Error on up directory!");
                            dbgui.errorList();
                        }
                    });
                } else if(command.getCommand().equals(FileCommand.RMFILE) || command.getCommand().equals(FileCommand.RMDIR)) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            dbgui.errorDelete();
                        }
                    });
                } else if (command.getCommand().equals(FileCommand.STOP)) {
                    Trace.note("Finished stop, disconnecting");
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            dbgui.disconnectCompleted();
                        }
                    });
                } else if (command.getCommand().equals(FileCommand.CP)) {
                    Trace.note("Copy failed");
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            logger.error("Error on file copy!");
                            dbgui.errorCopy();
    //                        dbgui.disconnectCompleted();
                        }
                    });
                }
            }
            Trace.exit();
        } catch (PermissionException e) {
            JOptionPane.showMessageDialog(null, "A session error has occurred.\n" + 
                    "Please check your connection\nand authenticate again.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            LoginDialog.clearLogin();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
}
