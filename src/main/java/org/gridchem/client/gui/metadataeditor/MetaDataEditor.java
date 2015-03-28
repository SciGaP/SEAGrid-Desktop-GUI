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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import org.gridchem.client.common.Settings;
import org.gridchem.service.beans.JobBean;

public class MetaDataEditor extends JPanel 
                             implements ActionListener {
    private int newNodeSuffix = 1;
    private static String ADD_COMMAND = "add";
    private static String SAVE_COMMAND = "save";
    private static String CANCEL_COMMAND = "cancel";
    
    private static JFrame frame;
    
    private MetaDataTree treePanel;
    
    JTextArea attributeValueText;

    public MetaDataEditor(ArrayList<JobBean> jobs) {
        super(new BorderLayout());
        init(jobs);
    }
    
    public MetaDataEditor(JobBean job) {
        super(new BorderLayout());
        
        ArrayList<JobBean> jobs = new ArrayList<JobBean>();
        
        jobs.add(job);
        
        frame = new JFrame("MetaDataEditor");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MetaDataEditor newContentPane = new MetaDataEditor(jobs);
        newContentPane.setOpaque(true); //content panes must be opaque
        
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        
        frame.setVisible(true);
    }
    
    private void init(ArrayList<JobBean> jobs) {
        
        
        //Create the tree.
        treePanel = new MetaDataTree(this);
        
        populateTree(treePanel,jobs);

        // Create the buttons
        JButton addButton = new JButton("Add");
        addButton.setActionCommand(ADD_COMMAND);
        addButton.addActionListener(this);
        
        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand(SAVE_COMMAND);
        saveButton.addActionListener(this);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand(CANCEL_COMMAND);
        cancelButton.addActionListener(this);

        JPanel panel = new JPanel(new GridLayout(1,5));
        panel.add(addButton);
        panel.add(saveButton);
        panel.add(cancelButton);
        
        // Create the value display area
        Border eBorder2 = BorderFactory.createEmptyBorder(5,10,5,10);
        attributeValueText = new JTextArea("",10,20);
//        attributeValueText.addFocusListener(new FocusListener() {
//            public void focusGained(FocusEvent e) {
//            }
//            public void focusLost(FocusEvent e) {
//                DefaultMutableTreeNode current = treePanel.getCurrentNode();
//                if(!current.getAllowsChildren()) {
//                    ((AttributeNode)current.getUserObject())
//                        .setValue(attributeValueText.getText());
//                    treePanel.treeModel.reload(current);
//                }
//                treePanel.updateJob(treePanel.getCurrentNode());
//            }
//        });
        
        JPanel attributeValueTextPane = new JPanel();
        TitledBorder inputTextPaneTitled = BorderFactory.createTitledBorder("Attribute Value");
        attributeValueTextPane.setBorder(BorderFactory.createCompoundBorder(inputTextPaneTitled,eBorder2));
        attributeValueTextPane.setSize(200,280);
        attributeValueTextPane.setLayout(new BoxLayout(attributeValueTextPane,BoxLayout.Y_AXIS));
        attributeValueTextPane.add(new JScrollPane(attributeValueText));
        
        //Lay everything out.
        treePanel.setPreferredSize(new Dimension(200, 300));
        
        add(treePanel, BorderLayout.WEST);
        add(attributeValueTextPane, BorderLayout.EAST); 
        
        add(panel, BorderLayout.SOUTH);
    }

    public void populateTree(MetaDataTree treePanel, ArrayList<JobBean> jobs) {
        DefaultMutableTreeNode node;

        for(JobBean job: jobs) {
            node = treePanel.addObject(null,job);
            
            Hashtable<String,String> metaData = (Hashtable<String,String>)Settings.xstream.fromXML(job.getMetaData());
            
            if(metaData != null) {
                Enumeration keys = metaData.keys();
                
                while (keys.hasMoreElements()) {
                    String key = (String)keys.nextElement();
                    treePanel.addObject(node,new AttributeNode(key,metaData.get(key)));
                }
            }
        }
    }
    
    protected void setValueTextArea(String newText) {
        attributeValueText.selectAll();
        attributeValueText.replaceSelection(newText);
    }
    
    
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (ADD_COMMAND.equals(command)) {
            //Add button clicked
            if (treePanel.getCurrentNode().getAllowsChildren()) {
                treePanel.addObject(new AttributeNode("Attribute" + newNodeSuffix++,""));
            } else {
                treePanel.addObject(
                        (DefaultMutableTreeNode)treePanel.getCurrentNode().getParent(), 
                        new AttributeNode(treePanel.createNewAttributeDialog(),""));
            }
        } else if (CANCEL_COMMAND.equals(command)) {
            //Cancel button clicked
            this.setVisible(false);
            frame.dispose();
        } else if (SAVE_COMMAND.equals(command)) {
            //Clear button clicked.
            DefaultMutableTreeNode current = treePanel.getCurrentNode();
            if(!current.getAllowsChildren()) {
                ((AttributeNode)current.getUserObject())
                    .setValue(attributeValueText.getText());
                treePanel.treeModel.reload(current);
            }
            treePanel.updateJob(treePanel.getCurrentNode());
            treePanel.saveAll();
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        frame = new JFrame("MetaDataEditor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MetaDataEditor newContentPane = new MetaDataEditor(createJobs());
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Creates dummy data for the standalone metadata browser
     * @return
     */
    private static ArrayList<JobBean> createJobs() {
        ArrayList<JobBean> jobs = new ArrayList<JobBean>();
        
        for(int j=0;j<3;j++) {
            Hashtable<String,String> metadata = new Hashtable<String,String>();
        
            for (int i=1;i<5;i++) {
                metadata.put("attribute" + i+j,"insert data for attribute" + i+j);
            }
            
            JobBean job = new JobBean();
            
            job.setName("test job"+j);
            job.setMetaData(Settings.xstream.toXML(metadata));
            
            jobs.add(job);
        }
        
        return jobs;
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
