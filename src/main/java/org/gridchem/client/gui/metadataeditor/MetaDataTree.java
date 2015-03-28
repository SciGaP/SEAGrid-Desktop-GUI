/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Jul 5, 2006
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
 * Insert Template description here.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
/*
 * This code is based on an example provided by Richard Stanford, 
 * a tutorial reader.
 */

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.gridchem.service.beans.JobBean;

public class MetaDataTree extends JPanel {
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();

    private JPopupMenu rightClickPopup;
    
    //popupmenu declarations
    private JMenuItem editValue; 
    private JMenuItem newAttribute; 
    private JMenuItem deleteAttribute; 
    private JMenuItem renameAttribute;
    private JMenuItem jobInfo;
    
    MetaDataEditor mdeditor;
    
    public MetaDataTree(MetaDataEditor mdeditor) {
        super(new GridLayout(1,0));
        
        this.mdeditor = mdeditor;
        
        TreeMouseListener treeMouseListener = new TreeMouseListener();
        
        rootNode = new DefaultMutableTreeNode("Job MetaData");
        treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new MyTreeModelListener());

        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.addMouseListener(treeMouseListener);
        
        //Build the popup menu
        rightClickPopup = this.createPopupMenu();
        
        JScrollPane scrollPane = new JScrollPane(tree);
        
        add(scrollPane);
        
    }
    
    private JPopupMenu createPopupMenu() {
        PopupListener popupListener = new PopupListener();
        rightClickPopup = new JPopupMenu();
        renameAttribute = new JMenuItem("Rename Attribute");
        editValue = new JMenuItem("Edit Value");
        newAttribute = new JMenuItem("Add Attribute");
        deleteAttribute = new JMenuItem("Delete Attribute");
        jobInfo = new JMenuItem("Get Info");
        
        
        rightClickPopup.add(renameAttribute);
        rightClickPopup.add(editValue);
        rightClickPopup.add(newAttribute);
        rightClickPopup.add(deleteAttribute);
        rightClickPopup.addSeparator();
        rightClickPopup.add(jobInfo);
        
        renameAttribute.addActionListener(popupListener);
        editValue.addActionListener(popupListener);
        newAttribute.addActionListener(popupListener);
        deleteAttribute.addActionListener(popupListener);
        jobInfo.addActionListener(popupListener);
        
        return rightClickPopup;
    }

    /** Remove all nodes except the root node. */
    public void clear() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }

    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                
                // update the job with the new data
                updateJob((DefaultMutableTreeNode)currentNode.getParent());
                return;
            }
        } 

        // Either there was no selection, or the root was selected.
        
    }
    
    /**
     * returns the selected tree node.
     * @return
     */
    public DefaultMutableTreeNode getCurrentNode() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode current = (DefaultMutableTreeNode)
                (currentSelection.getLastPathComponent());
            if (currentSelection.getPathCount() > 2) {
                current.setAllowsChildren(false);
            }
            return current;
        } else 
            return null;
    }

    /** Add child to the currently selected node. */
    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)
                         (parentPath.getLastPathComponent());
        }
        
        // update the job with the new attribute
        updateJob(parentNode);
        
        return addObject(parentNode, child, true);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child) {
        return addObject(parent, child, false);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child, 
                                            boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode = 
                new DefaultMutableTreeNode(child);

        if (parent == null) {
            parent = rootNode;
        }

        treeModel.insertNodeInto(childNode, parent, 
                                 parent.getChildCount());

        //Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        
        // update the job with the new attribute
        updateJob(parent);
        
        return childNode;
    }
    
    protected void updateJob(DefaultMutableTreeNode jobNode) {
       // pull the job from the user's vo
        // insert new metadata into job
        // send job to GMS for update
    }
    
    protected void saveAll() {
        //walk the tree and udpate each job
        // send all jobs to GMS for update
        
    }

    class MyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)
                     (e.getTreePath().getLastPathComponent());

            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */
            try {
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)
                       (node.getChildAt(index));
            } catch (NullPointerException exc) {}

            updateJob(node);
            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());
        }
        public void treeNodesInserted(TreeModelEvent e) {
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }
    
    private void showPopupMenu(Component component, int x, int y) {
        if(getCurrentNode().getAllowsChildren()) {
            deleteAttribute.setEnabled(false);
        } else {
            deleteAttribute.setEnabled(true);
        }
        System.out.println("Showing right click popup menu");
        rightClickPopup.show(component, x, y);
    }
    
    protected class TreeMouseListener implements MouseListener {
        
        public void mouseClicked(MouseEvent mEvent) {
            DefaultMutableTreeNode current = getCurrentNode();
            
            // update the value area and check to see if they right clicked
            if (current != null) {
                if (current.getAllowsChildren()) {
                    mdeditor.setValueTextArea("");
                } else {
                    mdeditor.setValueTextArea(((AttributeNode)current.getUserObject()).getValue());
                }
                
                //if a single right click, popup the menu
                if(isRightClickEvent(mEvent)) {
                    System.out.println("Right clicked, showing popup menu.");
                    showPopupMenu(tree, mEvent.getX(), mEvent.getY());
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

        public void mousePressed(MouseEvent event) {
        }

        public void mouseReleased(MouseEvent event) {
        }
        
        public void mouseEntered(MouseEvent event) {
        }

        public void mouseExited(MouseEvent event) {
        }
    }
    
    
    private void createJobInfoDialog(JobBean job) {
        
        JDialog getInfoDialog = new JDialog();
        
        JPanel getInfoDialogPanel = new JPanel(new GridLayout(6,1));
        
        ((GridLayout) getInfoDialogPanel.getLayout()).setVgap(5);
        
        getInfoDialog.setContentPane(getInfoDialogPanel);
        
        getInfoDialogPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Information about job [" + job.getId() + "]: " + job.getName()));
                
        JLabel name = new JLabel("<html><b>Name:</b> " + job.getName() + "<html>");
        getInfoDialogPanel.add(name);
        JLabel researchProject = new JLabel("<html><b>Research Project:</b> " + job.getExperimentName() + " bytes<html>");
        getInfoDialogPanel.add(researchProject);
        JLabel project = new JLabel("<html><b>Allocation:</b> " + job.getProjectName() + "<html>");
        getInfoDialogPanel.add(project);
        JLabel app = new JLabel("<html><b>Application:</b> " + job.getSoftwareName() + "<html>");
        getInfoDialogPanel.add(app);
        JLabel hpc = new JLabel("<html><b>HPC System:</b> " + job.getSystemName() + "<html>");
        getInfoDialogPanel.add(hpc);
        JLabel queue = new JLabel("<html><b>Queue:</b> " + job.getQueueName() + "<html>");
        getInfoDialogPanel.add(queue);
        JLabel rcpu = new JLabel("<html><b>Requested CPUs:</b> " + job.getRequestedCpus() + "<html>");
        getInfoDialogPanel.add(rcpu);
        JLabel rmem = new JLabel("<html><b>Requested Memory:</b> " + job.getRequestedMemory() + "<html>");
        getInfoDialogPanel.add(rmem);
        JLabel status = new JLabel("<html><b>Status:</b> " + job.getStatus() + "</br><html>");
        getInfoDialogPanel.add(status);
        JLabel start = new JLabel("<html><b>Start Time:</b> " + job.getStartTime() + "<html>");
        getInfoDialogPanel.add(start);
        JLabel stop = new JLabel("<html><b>Stop Time:</b> " + job.getStopTime() + "<html>");
        getInfoDialogPanel.add(stop);
        JLabel ucpu = new JLabel("<html><b>Used CPUs:</b> " + job.getUsedCpus() + "<html>");
        getInfoDialogPanel.add(ucpu);
        JLabel umem = new JLabel("<html><b>Used Memory:</b> " + job.getUsedMemory() + "<html>");
        getInfoDialogPanel.add(umem);
        JLabel cost = new JLabel("<html><b>Cost:</b> " + job.getCost() + "<html>");
        getInfoDialogPanel.add(cost);
        
        getInfoDialog.setTitle(job.getName() + " Info");
        getInfoDialog.pack();
        getInfoDialog.setVisible(true);
        
    }
    
    private String createGetValueDialog(DefaultMutableTreeNode node) {
        return (String) JOptionPane.showInputDialog(this,
                "What is the new value of " + ((AttributeNode)node.getUserObject()).getName(), 
                "New Attribute Value", JOptionPane.QUESTION_MESSAGE);
    }
    
    private boolean createDeleteAttributeDialog(String itemToDelete) {
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
    
    protected String createNewAttributeDialog() {
        return (String) JOptionPane.showInputDialog(this,
                "What is the name of the new attribute?", "New Attribute", JOptionPane.QUESTION_MESSAGE);
    }
    
    private String renameAttributeDialog() {
        return (String) JOptionPane.showInputDialog(this,
                "What is the new name of the attribute?", "New Attribute", JOptionPane.QUESTION_MESSAGE);
    }
        
    protected class PopupListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JMenuItem item = (JMenuItem)event.getSource();
            
            System.out.println("Popup action occurred");
            
            DefaultMutableTreeNode current = getCurrentNode();
            
            if (item == renameAttribute) {
                System.out.println("Rename attribute popup button pushed");
                String newval = renameAttributeDialog();
                if (newval != null && !newval.equals("")) {
                    if(!current.getAllowsChildren()) {
                        ((AttributeNode)current.getUserObject()).setName(newval);
                        treeModel.reload(current);
                    } else {
                        addObject((DefaultMutableTreeNode)current.getParent(),newval);
                    }
                }
            } else if(item == editValue){
                System.out.println("Get value popup button pushed");
                if(!current.getAllowsChildren()) {
                    ((AttributeNode)current.getUserObject())
                        .setValue(createGetValueDialog(current));
                    treeModel.reload(current);
                }
            } else if(item == newAttribute) {
                System.out.println("New attribute popup button pushed");
                if(current.getAllowsChildren()) {
                    String newval = createNewAttributeDialog();
                    if (newval != null && !newval.equals("")) {
                        addObject(current,newval);
                    }
                } else {
                    addObject((DefaultMutableTreeNode)current.getParent(),
                            createNewAttributeDialog());
                }
            } else if(item == deleteAttribute) {
                System.out.println("Delete attribute popup button pushed");
                if(!current.getAllowsChildren()) {
                    if (createDeleteAttributeDialog(((AttributeNode)current.getUserObject()).getName())) {
                        removeCurrentNode();
                    }
                }
            } else if (item == jobInfo) {
                System.out.println("Get Job Info popup button pushed");
                if(current.getAllowsChildren()) {
                    createJobInfoDialog((JobBean)current.getUserObject());
                } else {
                    createJobInfoDialog((JobBean)((DefaultMutableTreeNode)current.getParent()).getUserObject());
                }
            }
        }
    }
}
