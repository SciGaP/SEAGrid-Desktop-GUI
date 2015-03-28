/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Mar 15, 2007
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

package org.gridchem.client.gui.panels.myccg.job.steering;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.NotificationBean;
import org.gridchem.service.beans.StorageBean;
import org.gridchem.service.model.enumeration.JobStatusType;
import org.gridchem.service.model.enumeration.ResourceType;

/**
 * Dialog to control the steering parameters associated with a job.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class JobSteeringDialog extends JDialog {

    private static final String OUTPUT = "Mass Storage";
    private static final String NOTIFICATION = "Notification";
    private static final String CHECKPOINTING = "Checkpointing";
    private static final String RESUBMISSION = "Resubmission";
    
    private String[] steeringParameters = {NOTIFICATION,CHECKPOINTING,RESUBMISSION,OUTPUT};
    private static Dimension minimumUpperPanelSize = new Dimension(200,300);
    private static Dimension preferredPanelSize = new Dimension(700,475);
    private static int WIDTH = 200;
    private static int HEIGHT = 400;
    
    private JobSteeringTableModel usageModel;
    
    private JobBean job = null;
    
    private StatusListener statusListener;
    
    protected JSplitPane splitPane;
    protected JScrollPane jscrollPane;
    private JPanel dataPanel;
    
    private StorageServerPanel storagePanel;
    private NotificationPanel notificationPanel;
    private CheckpointPanel checkpointPanel;
    private ResubmissionPanel resubmissionPanel;
    
    private static boolean changed;
    
    protected StorageBean newStorageServer;
    protected boolean checkpointable = false;
    protected boolean changedNotification = false;
    protected ArrayList<NotificationBean> notifications;
    protected boolean resubmittable = false;
    protected int maxResubmissions = -1;
    
    
    public JobSteeringDialog() {
        super();
        
        this.changed = false;
        
        this.job = createDummyData();
        
        init();
    }
    
    private JobSteeringDialog(StatusListener statusListener, JobBean job) {
        
        super();
        
        this.changed = false;
        
        this.statusListener = statusListener;
        
        this.job = job;
        
        init();
    } 
    
    public static JobSteeringDialog getInstance(StatusListener statusListener, JobBean job) {
        return new JobSteeringDialog(statusListener,job);
    }
    
    protected void init() {
        
        usageModel = new JobSteeringTableModel(job);

        storagePanel = new StorageServerPanel(job);
        notificationPanel = new NotificationPanel(job);
        checkpointPanel = new CheckpointPanel(job);
        resubmissionPanel = new ResubmissionPanel(job);
        
        // create the scroll pane for the parameter editor panels
        jscrollPane = new JScrollPane(); 
        jscrollPane.setAutoscrolls(true);
        jscrollPane.isWheelScrollingEnabled();
        
        JList list = new JList(steeringParameters);
        list.addListSelectionListener(new SteeringTableModelListener());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedValue(steeringParameters[0], true); // this loads the parameter editing panel
        
        Container accountingBox = Box.createVerticalBox();
        accountingBox.add(jscrollPane);
        
        // now add both split panes to the main left/right split pane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,list,jscrollPane);
        splitPane.setDividerSize(6);
        splitPane.setResizeWeight(0.2);
        splitPane.setContinuousLayout(false);
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerLocation((int)minimumUpperPanelSize.getWidth());
        splitPane.setPreferredSize(preferredPanelSize);
        
        // layout the final display
        JPanel contentPanel = new JPanel(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTH;
        contentPanel.add(splitPane,c);
        
        c.gridy = 1;
        c.weighty = 0;
        c.weightx = 0;
        c.anchor = GridBagConstraints.SOUTH;
        contentPanel.add(createButtonPane(),c);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(contentPanel);
        setTitle(job.getName() + " Steering Dialog");
        
        setResizable(true);
        setPreferredSize(new Dimension(600,400));
        pack();
        setVisible(true);
        
    }
    
    private JPanel createButtonPane() {
        Dimension buttonSize = new Dimension(150,100);
        
        final JButton updateButton = new JButton("OK");
        final JButton cancelButton = new JButton("Close");
        
        // define an action listener for the buttons
        ActionListener buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (event.getSource() == updateButton) {
                    // refresh the usage with a call to the GMS
                    updateJob();
                    dispose();
                } else {
                    // dispose of entire MyCCG window
                    dispose();
                }
            }
            
        };
        
        // add all event listeners here 
        updateButton.addActionListener(buttonListener);
        cancelButton.addActionListener(buttonListener);
        
        // add tool tip texts to buttons
        updateButton.setToolTipText(
                "<html>Apply the current settings.</html>");
        
        cancelButton.setToolTipText("Close this window.");
        
        // lay them out in the button panel
        JPanel buttonInterior = new JPanel();
        buttonInterior.setLayout(new GridLayout(1,2,5,0));
        buttonInterior.add(cancelButton);
        buttonInterior.add(updateButton);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonInterior);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,5));
        
        return buttonPanel;
    }
    
    private JPanel createStoragePanel() {
        if (storagePanel == null) {
            storagePanel = new StorageServerPanel(job);
        }
        
        return storagePanel;
    }
    
    private JPanel createNotificationPanel() {
        
        if (notificationPanel == null) {
            notificationPanel = new NotificationPanel(job);
        }
        
        return notificationPanel;
    }
    
    private JPanel createCheckpointPanel() {
        if (checkpointPanel == null) {
            checkpointPanel = new CheckpointPanel(job);
        }
        
        return checkpointPanel;
    }
    
    private JPanel createResubmitPanel() {
        if (resubmissionPanel == null) {
            resubmissionPanel = new ResubmissionPanel(job);
        }
        
        return resubmissionPanel;
    }
    
    protected void updateJob() {
        //TODO: send the job to the GMS to update it's new
        // sterring parameters and make any necessary registrations
        // with the GRMS.
        
        // see if they changed the storage server
        if (storagePanel.isChanged()) {
            job.setStorageResource(storagePanel.getNewStorageServer().getName());
            System.out.println("Mss is now changed " + storagePanel.getNewStorageServer());
        } else {
            System.out.println("Mss remains unchanged");
        }
        
        // checkpoint capability is strictly a boolean value
        // the name of the checkpoint file will be determined
        // by the job description.
        if (checkpointPanel.isChanged()) {
            job.setCheckpointable(checkpointable);
            System.out.println("ckpt is now changed " + checkpointPanel.isCheckpointable());
        } else {
            System.out.println("ckpt remains unchanged");
        }
        
        
        // if resubmission is possible, then they need to specify
        // the number of retries.
        if (resubmissionPanel.isChanged()) {
            job.setResubmittable(resubmissionPanel.isResubmittable()) ;
            job.setMaxResubmissions(resubmissionPanel.getMaxResubmissions());
            System.out.println("resubmission is now changed " + ((resubmissionPanel.isResubmittable())?resubmissionPanel.getMaxResubmissions() + " attempts":false));
        } else {
            System.out.println("resubmission remains unchanged");
        }
        
        
        // the notification set is rather mixed up, so we simply
        // see if it's changed and then forward the set to the 
        // middleware to deal with.
//        if (notificationPanel.isChanged()) {
//            job.setNotifications(notificationPanel.getNotifications());
//            System.out.println("notification is now changed ");
////            for(NotificationBean n: notificationPanel.getNotifications()) {
////                System.out.println(n.toString());
////            }
//        } else {
//            System.out.println("notifications remain unchanged");
//        }
        
        
        // STEERINGCommand command = new STEERINGCommand(GMS.getInstance(),statusListener);
        // command.getArguments().put("job", job);
        // this.listener.statusChanged(new StatusEvent(command,this.status));
        
        System.out.println("Job " + job.getName() + " has been updated.");
    }
    
    private JobBean createDummyData() {
        JobBean job = new JobBean();
        job.setSoftwareName("Gaussian03");
        job.setName("Test Job");
        job.setSystemName("Champion");
        job.setStartTime(new Date());
        job.setStopTime(new Date());
        job.setExperimentName("VO Test Suite");
        job.setQueueName("default");
        job.setProjectName("mjk");
        job.setId(new Long(5));
        job.setLocalId("10643");
        job.setStatus(JobStatusType.FINISHED);
        
        // no resubmissions
        job.setResubmittable(false);
        
//        // no notifications
//        for(int i=0;i<JobStatus.values().length;i++) {
//            job.getNotifications().add(
//                    new NotificationBean(
//                            NotificationType.EMAIL,JobStatus.values()[i],""));
//        };
        
        // no checkpoint
        job.setCheckpointable(false);
        
        job.setStorageResource("mss.ncsa.uiuc.edu");
        
        
        return job;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JobSteeringDialog jsd = new JobSteeringDialog();
            }
        });
    }
    
    class SteeringTableModelListener implements ListSelectionListener {

        public void valueChanged(final ListSelectionEvent e) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    JPanel newPanel = new JPanel();
            
                    if (((String)((JList)e.getSource()).getSelectedValue()).equals(OUTPUT)) {
                        newPanel.add(createStoragePanel());
                        System.out.println("Displaying storage panel");
                    } else if (((String)((JList)e.getSource()).getSelectedValue()).equals(NOTIFICATION)) {
                        JScrollPane scrollPane = new JScrollPane();
                        scrollPane.setAutoscrolls(true);
                        scrollPane.getViewport().add(createNotificationPanel());
                        newPanel.add(scrollPane);
                        
                        System.out.println("Displaying notification panel");
                    } else if (((String)((JList)e.getSource()).getSelectedValue()).equals(CHECKPOINTING)) {
                        newPanel.add(createCheckpointPanel());
                        System.out.println("Displaying checkpointing panel");
                    } else {
                        newPanel.add(createResubmitPanel());
                        System.out.println("Displaying resubmission panel");
                    }
                
                    jscrollPane.getViewport().removeAll();
                    jscrollPane.getViewport().add(newPanel);
                    jscrollPane.revalidate();
                    
                    splitPane.revalidate();
                    splitPane.updateUI();
                }
            });
        }
        
    }

    /**
     * Underlying table model of the resource table.
     * 
     * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
     *
     */
    class JobSteeringTableModel extends AbstractTableModel {
        final public String[] m_columns = {"Job Property"};
        
        protected Vector m_vector;
        
        public JobSteeringTableModel(JobBean job) {
            m_vector = new Vector();
            
            setJobData(job);
        }
        
        public JobSteeringTableModel() {
            m_vector = new Vector();
         }
        
        public void setJobData(JobBean job) {
            m_vector.removeAllElements();
            m_vector.addElement("Output");
            m_vector.addElement("Notification");
            m_vector.addElement("Checkpointing");
            m_vector.addElement("Resubmission");
        
            fireTableDataChanged();
        }
        
        public int getRowCount() {
            return m_vector == null ? 0 : m_vector.size();
        }

        public int getColumnCount() {
            return m_columns.length;
        }
        
        public String getColumnName(int column) {
            return m_columns[column];
        }



        public Object getValueAt(int nRow, int nCol) {
            if (nRow < 0 || nRow >= getRowCount()) 
                return "";
            
            return m_vector.elementAt(nRow);
            
        }
    }
}
