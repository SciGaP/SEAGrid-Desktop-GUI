/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Jul 4, 2006
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

package org.gridchem.client.gui.metadataeditor;

/**
 * Generic metadata editor.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import org.apache.log4j.Logger;
import org.gridchem.client.common.Settings;
import org.gridchem.service.beans.*;

public class Tree1 extends JFrame {
    Logger logger = Logger.getLogger(Tree1.class);
    
    protected JTree  tree = null;

    protected DefaultTreeModel model = null;
    protected JTextField display;
    
    Object[] nodes;
    DefaultMutableTreeNode top;

    public Tree1(JobBean job) {
  
        super(job.getName() + " Meta Data");
        
        setSize(400, 300);
        
        model = new DefaultTreeModel(top);
        tree = new JTree(formatData(job));
    
//        DefaultTreeCellRenderer renderer = new 
//          DefaultTreeCellRenderer();
//        renderer.setOpenIcon(new ImageIcon("opened.gif"));
//        renderer.setClosedIcon(new ImageIcon("closed.gif"));
//        renderer.setLeafIcon(new ImageIcon("leaf.gif"));
//        tree.setCellRenderer(renderer);
    
        tree.setShowsRootHandles(true); 
        tree.setEditable(false);
        TreePath path = new TreePath(nodes);
        tree.setSelectionPath(path);
    
        tree.addTreeSelectionListener(new 
          OidSelectionListener());
    
        JScrollPane s = new JScrollPane();
        s.getViewport().add(tree);
        getContentPane().add(s, BorderLayout.CENTER);
    
        display = new JTextField();
        display.setEditable(false);
        getContentPane().add(display, BorderLayout.SOUTH);
    
        WindowListener wndCloser = new WindowAdapter()
        {
          public void windowClosing(WindowEvent e) 
          {
            System.exit(0);
          }
        };
        
        addWindowListener(wndCloser);
        
        setVisible(true);
    }

    /**
     * Opens a new instance of the metadata browser with dummy data
     * @param args
     */
    public static void main(String[] args) {
        new Tree1(createJob());
    }
  
    /**
     * Creates dummy data for the standalone metadata browser
     * @return
     */
    private static JobBean createJob() {
        
        Hashtable<String,String> metadata = new Hashtable<String,String>();
        
        for (int i=1;i<5;i++) {
            metadata.put("attribute" + i,"insert data for attribute" + i);
        }
        
        JobBean job = new JobBean();
        
        job.setName("test job");
        job.setMetaData(Settings.xstream.toXML(metadata));
        
        return job;
    }
  
    /**
     * @param jobs
     */
    protected void createTree(ArrayList<JobBean> jobs) {
        top = new DefaultMutableTreeNode("Job Metadata");
        
        DefaultMutableTreeNode parent = top;
        
        nodes[0] = top;
        
        int i = 0;
        
        for(JobBean job: jobs) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(job);
            
            Hashtable<String,String> metaData = (Hashtable<String,String>)Settings.xstream.fromXML(job.getMetaData());
            
            if(metaData != null) {
                Enumeration keys = metaData.keys();
                
                while (keys.hasMoreElements()) {
                    String key = (String)keys.nextElement();
                    node.add(new DefaultMutableTreeNode(new AttributeNode(key,metaData.get(key))));
                }
            }
            
            parent.add(node);
            
            nodes[i++] = node;
            
        }
    }
    
    private Hashtable<String,AttributeNode[]> formatData(JobBean job) {
        Hashtable<String,String> data = (Hashtable<String,String>)Settings.xstream.fromXML(job.getMetaData());
        Hashtable<String,AttributeNode[]> treeData = new Hashtable<String,AttributeNode[]>();
        
        if (data != null) {
            Enumeration keys = data.keys();
            
            int i = 0;
            
            AttributeNode[] attributes = new AttributeNode[data.size()];
            while (keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                System.out.println(key);
                System.out.println(data.get(key));
                attributes[i++] = new AttributeNode(key,data.get(key));
                
            }
            treeData.put(job.getName(),attributes);
        }
        
        return treeData; 
    }

//    protected class TreeMouseListener implements MouseListener {
//        
//        public void mouseClicked(MouseEvent mEvent) {
//            
//            int mouseRow = tree.getRowForLocation(mEvent.getX(), mEvent.getY());
//            
//            if (mouseRow != -1) {
//                System.out.println("Clicked on a real row");
//                
//                //If we have clicked a real row, select it, and continue with our logic
//                tree.setSelectionRow(mouseRow);
//                //if a single right click, popup the menu
//                if(isRightClickEvent(mEvent)) {
//                    System.out.println("Right clicked, showing popup menu.");
//                    showPopupMenu(tree, mEvent.getX(), mEvent.getY());
//                //if a double click:    
//                } else if ((mEvent.getButton()==MouseEvent.BUTTON1) && (mEvent.getClickCount()==2)) {
//                    TreePath treePath = tree.getPathForRow(mouseRow);
//                    GridFileTreeNode selectedNode = (GridFileTreeNode) treePath.getLastPathComponent();
//                    if(selectedNode.getAllowsChildren()){
//                    //If here, user has double clicked on a directory
//                    } else {
//                        JDialog newDialog = new JDialog();
//                        URI lookupURI = null;
//                        try {
//                            String port = String.valueOf(topURIPanel.getURI().getPort());
//                            String host = topURIPanel.getURI().getHost();
//                            
//                            if(port != null & !port.equals("-1")) {
//                                port = ":"+port;
//                            }else {
//                                port = "";
//                            }
//                            
//                            if(host == null) {
//                                host = "";
//                            }
//                            
//                            
//                            lookupURI = new URI(topURIPanel.getURI().getScheme()+"://"+host+port+ mdworker.getPathFromTreePath(getSelectedItemsTreePath()) );
//                            
//                            logger.debug("lookupURI="+lookupURI);
//                        } catch (URISyntaxException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        String uriPath = lookupURI.getPath();
////                        JPanel newPanel = (JPanel) mimehandler.lookup(mdworker.processURIForTopPanel(lookupURI));
////                        if(newPanel==null) {
////                            errorMimeType();
////                        } else {
////                            newDialog.getContentPane().add(newPanel);
////                            newDialog.pack();
////                            newDialog.setVisible(true);
////                        }
//                        
//                        logger.debug("uriPath="+uriPath);
//                        int index = uriPath.lastIndexOf(".")+1;
//                        String extension =  "";
//                        if(index > 0) {
//                            extension = uriPath.substring(index);
//                        }
//                        logger.debug("extension="+extension);
//                        try {  
//                          // have a provider
//                          if(isProvider(lookupURI)) {
//                              System.out.println("Downloading file");
//                              mdworker.downloadFile(treePath);
//                          }else {
//                              System.out.println("No provider, starting program");
//                              startProgram(lookupURI);
//                          }
//                        }catch(Exception exception) {
//                             logger.error("exception ",exception);
//                             //startProgram(lookupURI);
//                        }
//                    }
//                }
//            }
//        }
//        
//        /**
//         * Check to see if the user right clicks with a single button mouse.
//         * @param ev
//         * @return
//         */
//        private boolean isRightClickEvent(MouseEvent ev) {
//            int mask = InputEvent.BUTTON1_MASK - 1;
//            int mods = ev.getModifiers() & mask;
//            if (mods == 0) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//        
//        public void mousePressed(MouseEvent event) {
//        }
//
//        public void mouseReleased(MouseEvent event) {
//        }
//        
//        public void mouseEntered(MouseEvent event) {
//        }
//
//        public void mouseExited(MouseEvent event) {
//        }
//    }
//    
//    protected class TreeListener implements TreeWillExpandListener {
//        public void treeWillExpand(TreeExpansionEvent tee) {
//            startWaiting("Getting directory listing...", true);
//            mdworker.treeWillExpand(tee.getPath());
//        }
//        
//        public void treeWillCollapse(TreeExpansionEvent tee) {
//            mdworker.treeWillCollapse(tee.getPath());
//        }
//        
//    }
//    
//    protected class ButtonListener implements ActionListener {
//        public void actionPerformed(ActionEvent event) {
//            String label = ((JButton) event.getSource()).getToolTipText();
//            if(label.equals("Connect")) {
//                startWaiting("Connecting...", false);
//                topURIPanel.setEnabled(false);
//                goButton.setEnabled(false);
//                disconnectButton.setEnabled(false);
//                mdworker.goButtonPushed();
//            } else if (label.equals("Disconnect")) {
//                startWaiting("Disconnecting...", false);
//                mdworker.disconnectButtonPushed();
//            } else if (label.equals("Home")) {
//                System.out.println("Clicked home.");
//                if(connected) {
//                    startWaiting("Connecting...", true);
//                    mdworker.goHome();
//                }
//            } else if (label.equals("Refresh")) {
//                System.out.println("Clicked refresh.");
//                if(connected) {
//                    startWaiting("Refreshing...", true);
//                    mdworker.refresh();
//                }
//            } else if (label.equals("New Directory")) {
//                System.out.println("Clicked create new directory.");
//                if(connected) {
//                    mdworker.makeDirectory(createNewDirectoryDialog());
//                    startWaiting("Creating new directory...", false);
//                }
//            } else if (label.equals("Get Info")) {
//                System.out.println("Clicked get info.");
//                if(connected) {
//                    createGetInfoDialog(getSelectedItemsGridFile());
//                }
//            } else if (label.equals("Stop")) {
//                stopWaiting();
//                topURIPanel.setEnabled(true);
//                if(connected){
//                    goButton.setEnabled(false);
//                    disconnectButton.setEnabled(true);
//                } else {
//                    goButton.setEnabled(true);
//                    disconnectButton.setEnabled(false);
//                }
//                mdworker.stopButtonPushed();
//                
//            }
//            
//        }
//    }
//    
//    protected class PopupListener implements ActionListener {
//        public void actionPerformed(ActionEvent event) {
//            JMenuItem item = (JMenuItem)event.getSource();
//            System.out.println("Popup action occurred");
//            if(item == getInfo){
//                System.out.println("Info popup button pushed");
//                createGetInfoDialog(getSelectedItemsGridFile());
//            } else if(item == newDirectory) {
//                System.out.println("New Directory popup button pushed");
//                mdworker.makeDirectory(getSelectedItemsTreePath(), createNewDirectoryDialog());
//                startWaiting("Creating new directory...", false);
//            } else if(item == deleteEntry) {
//                System.out.println("Delete popup button pushed");
//                if(createDeleteDialog(getSelectedItemsGridFile().getName()))
//                    startWaiting("Deleting item...", false);
//                    mdworker.deleteItem(getSelectedItemsTreePath());
//            } else if(item == goInto) {
//                System.out.println("GoInto popup button pushed");
//                startWaiting("Going into directory...", true);
//                mdworker.goInto(getSelectedItemsTreePath());
//            } else if(item == refresh) {
//                System.out.println("Refresh popup button pushed");
//                startWaiting("Refreshing...", true);
//                mdworker.refresh(getSelectedItemsTreePath());
//            }
//        }
//    }
    
    class OidSelectionListener implements TreeSelectionListener { 
      
        public void valueChanged(TreeSelectionEvent e) {
            TreePath path = e.getPath();
            Object[] nodes = path.getPath();
            String text = "";
            for (int k=0; k<nodes.length; k++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)nodes[k];
                if(node.getAllowsChildren()) {
                    text = (String)node.getUserObject();
                } else {
                    text = ((AttributeNode)node.getUserObject()).getValue();
                }
            }
            display.setText(text);
        }
    }
}

class AttributeNode {
    private String name;
    private String value;
    
    public AttributeNode(String name, String value) {
      this.name = name;
      this.value = value;
    }
    
    public String getName() {
      return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String toString() {
      return name;
    }
}