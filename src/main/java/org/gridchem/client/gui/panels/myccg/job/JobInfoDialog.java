/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Aug 28, 2006
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

package org.gridchem.client.gui.panels.myccg.job;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import org.gridchem.service.beans.JobBean;
import org.gridchem.service.model.enumeration.JobStatusType;

/**
 * Displays inforamtion about a job with clipboard copy support for each field.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class JobInfoDialog extends JDialog {
    private JPopupMenu jobInfoPopup;
    private JMenuItem jobFieldCopyMenuItem;
    private JLabel selectedLabel;
    JPanel getInfoDialogPanel;
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private String formattedDate;
    
    private KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK,false);
    private KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,ActionEvent.CTRL_MASK,false);
    private KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
    
    ActionListener keyListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().compareTo("Copy")==0) {
                System.out.println("Copy key event on label: " + selectedLabel.getText());
                copyJobInfoField();
            } else if (e.getActionCommand().compareTo("Escape")==0) {
                System.out.println("Escape key event on label: " + selectedLabel.getText());
                exit();
            }
        }
    };
    
    public JobInfoDialog(JobBean job) {
    	this(job, "NULL");
    }
    
    public JobInfoDialog(JobBean job, String estStartTime) {
        super();
        getInfoDialogPanel = new JPanel(new GridLayout(6,1,5,5));

        //          ((GridLayout) getInfoDialogPanel.getLayout()).setVgap(5);
        String jobStatus = job.getStatus().name();
        
        getInfoDialogPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "Information about job [" + job.getId() + "]: " + job.getName()));
              
        createJobInfoPopupMenu();
        
        String text = "<html><b>Name: </b><span style='background:#F0F0F0'>" + job.getName() + "</span></html>";
        String toolTip = "The name of the job.";
        JLabel name = createLabel(text,toolTip);
        
        
        text = "<html><b>Research Project: </b><span style='background:#F0F0F0'>" + 
                ((job.getExperimentName().length() > 20)?
                        job.getExperimentName().substring(0,20):job.getExperimentName()) +
                " </span><html>";
        toolTip = "The research project under study by this job: " + job.getExperimentName();
        JLabel researchProject = createLabel(text,toolTip);
        
        text = "<html><b>User Project: </b><span style='background:#F0F0F0'>" + 
                ((job.getProjectName().length() > 20)?
                        job.getProjectName().substring(0,20):job.getProjectName()) +
                "</span></html>";
        toolTip = "The CCG project associated with this job: " + job.getProjectName();
        JLabel project= createLabel(text,toolTip);
        
        text = "<html><b>Application: </b><span style='background:#F0F0F0'>" + job.getSoftwareName() + "</span></html>";
        toolTip = "The application run by this job.";
        JLabel app = createLabel(text,toolTip); 
        
        
        text = "<html><b>HPC System: </b><span style='background:#F0F0F0'>" + job.getSystemName() + "</span></html>";
        toolTip = "The HPC system on which this job ran.";
        JLabel hpc = createLabel(text,toolTip); 

        text = "<html><b>Queue: </b><span style='background:#F0F0F0'>" + job.getQueueName() + "</span></html>";
        toolTip = "<html>The queue in which this job ran. This may<br>" + 
                            "not necessarily be the same queue requested<br>during job submission.</html>";
        JLabel queue = createLabel(text,toolTip); 
        
        text = "<html><b>Local Job ID: </b><span style='background:#F0F0F0'>" + job.getLocalId() + "</span></html>";
        toolTip = "The local job id of this job on the remote resource.";
        JLabel localID = createLabel(text,toolTip);
        
        text = "<html><b>Requested CPUs: </b><span style='background:#F0F0F0'>" + job.getRequestedCpus() + "</span></html>";
        toolTip = "The number of cpu's requested during job submission.";
        JLabel rcpu = createLabel(text,toolTip);
        
        text = "<html><b>Requested Memory: </b><span style='background:#F0F0F0'>" + 
            ((job.getRequestedMemory() == null)?" ---":job.getRequestedMemory()) + 
            "</span></html>";
        toolTip = "The amount of memory requested during job submission.";
        JLabel rmem = createLabel(text,toolTip);
        
        text = "<html><b>Requested WallTime: </b><span style='background:#F0F0F0'>" + 
        ((job.getRequestedCpuTime() == null)?" ---":resolveTimeLimit(job.getRequestedCpuTime())) + 
        "</span></html>";
        toolTip = "The amount of wall clock time requested during job submission.";
        JLabel rwct = createLabel(text,toolTip);
        
        text = "<html><b>Status: </b><span style='background:#F0F0F0'>" + jobStatus + "</span></html>";
        toolTip = "The current status of the job.";
        JLabel status = createLabel(text,toolTip);
        
        
        text = "<html><b>Used CPUs: </b><span style='background:#F0F0F0'>" + job.getUsedCpus() + "</span></html>";
        toolTip = "The number of CPU's actually used by this job.";
        JLabel ucpu = createLabel(text,toolTip);         
        
        text = "<html><b>Used Memory: </b><span style='background:#F0F0F0'>" + 
            ((job.getUsedMemory() == null)?" ---":job.getUsedMemory()) + 
            "</span></html>";
        toolTip = "The amount of memory actually allocated during the running of this job.";
        JLabel umem = createLabel(text,toolTip);         
        
        text = "<html><b>Cost: </b><span style='background:#F0F0F0'>" + job.getCost() + "</span></html>";
        toolTip = "The number of SU's charged to the CCG project associated with this job.";
        JLabel cost = createLabel(text,toolTip);         
        
        text = "<html><b>Est. Start Time: </b><span style='background:#F0F0F0'>" + estStartTime + "</span></html>";
        toolTip = "Expected start time for job";
        JLabel eStartTime = createLabel(text,toolTip);
        
        try {
            Calendar start = Calendar.getInstance();
            start.setTimeInMillis(job.getStartTime().getTime());
            System.out.println("Date year is " + start.get(Calendar.YEAR));
            if (job.getStartTime() == null || start.get(Calendar.YEAR) == 3000) {
                System.out.println("Job " + job.getId() + " start time is " + job.getStartTime());
                formattedDate = " ---";
            } else {
                formattedDate = new SimpleDateFormat("E MMM d, yyyy hh:mm a").format(job.getStartTime());
            }
        } catch (Exception e) {
            System.out.println("Error with start time of " + job.getStartTime());
            
            formattedDate = " ---";
        }
        text = "<html><b>Start Date: </b><span style='background:#F0F0F0'>" + formattedDate + "</span></html>";
        toolTip = "The start date and time of this job.";
        JLabel start = createLabel(text,toolTip);         
        
        
        try {
            Calendar stop = Calendar.getInstance();
            stop.setTimeInMillis(job.getStopTime().getTime());
            System.out.println("Date year is " + stop.get(Calendar.YEAR));
            if (job.getStopTime() == null || stop.get(Calendar.YEAR) == 3000) {
                System.out.println("Job " + job.getId() + " stop time is " + job.getStopTime());
                formattedDate = " ---";
            } else {
                formattedDate = new SimpleDateFormat("E MMM d, yyyy hh:mm a").format(job.getStopTime());
            }
        } catch (Exception e) {
            System.out.println("Error with stop time of " + job.getStopTime());
            formattedDate = " ---";
        }
        
        text = "<html><b>Stop Date: </b><span style='background:#F0F0F0'>" + formattedDate + "</span></html>";
        toolTip = "The stop date and time of this job.";
        JLabel stop = createLabel(text,toolTip);
        
        // panel prints out in 1x3 rows
        getInfoDialogPanel.add(name);
        getInfoDialogPanel.add(hpc);
        getInfoDialogPanel.add(rcpu);
        
        getInfoDialogPanel.add(researchProject);
        getInfoDialogPanel.add(queue);
        getInfoDialogPanel.add(rmem);
        
        getInfoDialogPanel.add(project);
        getInfoDialogPanel.add(localID);
        getInfoDialogPanel.add(rwct);
        
        getInfoDialogPanel.add(app);
        getInfoDialogPanel.add(ucpu);
        getInfoDialogPanel.add(start);
        
        getInfoDialogPanel.add(status);
        getInfoDialogPanel.add(umem);
        getInfoDialogPanel.add(stop);
        
        getInfoDialogPanel.add(cost);
        if (job.getStatus().equals(JobStatusType.SUBMITTING) ||
        job.getStatus().equals(JobStatusType.INITIAL) ||
        job.getStatus().equals(JobStatusType.SCHEDULED) ||
        job.getStatus().equals(JobStatusType.MIGRATING) || 
        job.getStatus().equals(JobStatusType.NOT_IN_QUEUE))
        getInfoDialogPanel.add(eStartTime);
        else {
        	getInfoDialogPanel.add(new JLabel(""));
        }
        getInfoDialogPanel.add(new JLabel(""));
        
        
        // make it so the panel disappears when the user presses the escape button
        //getInfoDialogPanel.addKeyListener(new DialogKeyListener(this));
        
        class ActionExample extends AbstractAction {
            public ActionExample() {
                super("escape");
            }
         
            public void actionPerformed(ActionEvent evt) {
                System.out.println("action triggered");
                exit();
            }
        }
        
        ActionExample action = new ActionExample();
        Object binding = action.getValue("escape");
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, binding);
        this.getRootPane().getActionMap().put(binding, action);
        this.setContentPane(getInfoDialogPanel);
        this.setTitle(job.getName() + " Info");
        this.pack();
        this.setVisible(true);
    }
    
    private JLabel createLabel(String text,String toolTip) {
        JLabel newLabel = new JLabel(text);
        newLabel.setToolTipText(toolTip);
        newLabel.setFocusable(true);
        newLabel.setRequestFocusEnabled(true);
        newLabel.add(jobInfoPopup);
        newLabel.addMouseListener(new JobInfoMouseAdapter());
        newLabel.registerKeyboardAction(keyListener,"Copy",copy,JComponent.WHEN_FOCUSED);
        newLabel.registerKeyboardAction(keyListener,"Escape",copy,JComponent.WHEN_FOCUSED);
        return newLabel;
    }
    
    private void exit() {
        this.setVisible(false);
        this.dispose();
    }
    
    private void createJobInfoPopupMenu() {
        jobInfoPopup = new JPopupMenu();
        
        jobFieldCopyMenuItem = new JMenuItem("Copy");
        jobFieldCopyMenuItem.setToolTipText("Copy the selected field to the clipboard");
        jobFieldCopyMenuItem.addActionListener(new PopupListener());
        
        jobInfoPopup.add(jobFieldCopyMenuItem);
        
    }
    
    private void copyJobInfoField() {
        String labelValue = selectedLabel.getText();
        StringSelection stsel;
        System.out.println("Parsing and copying \"" + labelValue + "\" to the clipboard.");
        labelValue = labelValue.substring(labelValue.indexOf("'>") + 2, 
                labelValue.indexOf("</span>"));
        
        stsel  = new StringSelection(labelValue);
        clipboard.setContents(stsel,stsel);
        selectedLabel.setText(selectedLabel.getText().replaceFirst("blue", "#F0F0F0"));
    }
    
    /**
     * Create a string representation of the Calendar object in hh:mm format
     * and where the hours field ranges from 0 - 32k
     * 
     * 
     */
    private String resolveTimeLimit(Calendar cal) {
    	
        int days = (cal.get(Calendar.DAY_OF_YEAR) - 1) * 24;
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        
        return (days + hours) + ":" + ((minutes == 0)?"00":minutes);
    }
    
    class DialogKeyListener implements KeyListener {
        private JDialog dialog;
        
        public DialogKeyListener(JDialog dialog) {
            this.dialog = dialog;
        }
        public void keyTyped(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
        public void keyPressed(KeyEvent e) {
            System.out.println("Key pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        }
    }
    
    class PopupListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JMenuItem item = (JMenuItem)event.getSource();
            
            if(item == jobFieldCopyMenuItem){
                copyJobInfoField();
            }
        }
    }
    
    class JobInfoMouseAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            Point p = e.getPoint();
            selectedLabel = (JLabel)e.getSource();
            
            if(isRightClickEvent(e)) {
                System.out.println("Right click on label: " + selectedLabel.getText());
                selectedLabel.setText(selectedLabel.getText().replaceFirst("#F0F0F0", "blue"));
                // make sure the right click selects the underlying row in the table.
                jobInfoPopup.show((JLabel)e.getSource(), p.x,p.y);
            }
       }
        
        /**
         * Check to see if the user right clicks with a single button mouse.
         * This is needed for Mac laptops.
         * 
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
    }
}
