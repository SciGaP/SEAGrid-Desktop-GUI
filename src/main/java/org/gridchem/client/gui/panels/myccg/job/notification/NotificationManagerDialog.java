/* 
 * Created on May 30, 2008
 * 
 * Developed by: Rion Dooley - dooley [at] tacc [dot] utexas [dot] edu
 * 				 TACC, Texas Advanced Computing Center
 * 
 * https://www.tacc.utexas.edu/
 */

package org.gridchem.client.gui.panels.myccg.job.notification;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.gridchem.client.SwingWorker;
import org.gridchem.client.common.Settings;
import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.gui.jobsubmission.commands.ADDNOTIFICATIONCommand;
import org.gridchem.client.gui.jobsubmission.commands.CLEARNOTIFICATIONSCommand;
import org.gridchem.client.gui.jobsubmission.commands.GETNOTIFICATIONSCommand;
import org.gridchem.client.gui.jobsubmission.commands.JobCommand;
import org.gridchem.client.gui.jobsubmission.commands.REMOVENOTIFICATIONCommand;
import org.gridchem.client.gui.jobsubmission.commands.UPDATENOTIFICATIONCommand;
import org.gridchem.client.gui.util.ETable;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.util.Env;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.NotificationBean;
import org.gridchem.service.model.enumeration.JobStatusType;
import org.gridchem.service.model.enumeration.NotificationType;

import com.asprise.util.ui.progress.ProgressDialog;

/**
 * Dialog to manage the user's job notifications.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class NotificationManagerDialog extends JDialog implements ActionListener, StatusListener {

    public static ImageIcon ICON_ADD = new ImageIcon(Env.getApplicationDataDir() + 
            File.separator + "images" + File.separator + "icons" + File.separator + "addinput.gif");
    public static ImageIcon ICON_SUB = new ImageIcon(Env.getApplicationDataDir() + 
            File.separator + "images" + File.separator + "icons" + File.separator + "subinput.gif");
    
    private JobBean job;
    private Frame parent;
    
    private JButton btnAdd;
    private JButton btnSub;
    private JButton btnClear;
    
    private ETable tblNotifications;
    
    private NotificationTableModel m_data;
    
    private ArrayList<NotificationBean> notifications;
    
    protected boolean isLoading = false;
    
    public static void getInstance(Frame parent, JobBean job) {
        new  NotificationManagerDialog(parent,job);
    }
    
    private NotificationManagerDialog(Frame parent, JobBean job) {
        super(parent);
        
        this.job = job;
        this.parent = parent;
        
        setTitle("Job " + job.getId() + " Notifications");
        
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
//        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
       
        // Create the layout for the input file table
        Box bList = Box.createVerticalBox();
        bList.add(createTable());
        bList.add(createButtonFooter());
        bList.setMinimumSize(new Dimension(300,200));
        bList.setMaximumSize(new Dimension(Short.MAX_VALUE,200));
        bList.setPreferredSize(new Dimension(250,200));
        bList.setBorder(BorderFactory.createEtchedBorder());
        
        // we fix the size of the dialog, so the notifications are scrollable.
        JScrollPane spNotificationList = new JScrollPane();
        spNotificationList.setAutoscrolls(true);
        spNotificationList.getViewport().add(bList);
        
        add(spNotificationList);
        
        pack();
        
        setResizable(false);
        
        setVisible(true);
        
        GETNOTIFICATIONSCommand command = new GETNOTIFICATIONSCommand(this);
        command.getArguments().put("job", job);
        command.getArguments().put("progressDialog", new ProgressDialog(parent,""));
        statusChanged(new StatusEvent(command,Status.START));
    }
    
    private Box createTable() {
        Box jobBox = Box.createVerticalBox();
        m_data = new NotificationTableModel(this);
        
        tblNotifications = new ETable();
        tblNotifications.setAutoCreateColumnsFromModel(false);
        tblNotifications.getTableHeader().setReorderingAllowed(false);
        tblNotifications.setColumnSelectionAllowed(false);
        tblNotifications.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tblNotifications.setRowSelectionAllowed(true);
        
        for (int k=0; k < m_data.getColumnCount(); k++) {
            TableColumn column = new TableColumn(k,
                    NotificationTableModel.m_columns[k].m_width,
                    new DefaultTableCellRenderer(),
                    null);
            column.setHeaderValue(NotificationTableModel.m_columns[k].m_title);
//            column.setHeaderRenderer(createDefaultRenderer());
            tblNotifications.addColumn(column);
        }
        
        tblNotifications.setModel(m_data);
        
        tblNotifications.addMouseListener(new MouseAdapter() {

            /* (non-Javadoc)
             * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
             */
            @Override
            public void mouseClicked(final MouseEvent event) {
//                super.mouseClicked(arg0);
                if (event.getClickCount() == 2) {
                    doEditNotification(m_data.getNotification(tblNotifications.rowAtPoint(event.getPoint())));
//                    SwingUtilities.invokeLater(new Runnable() {
//
//                        public void run() {
//                                    
//                        }
//                        
//                    });
//                } else {
//                    System.out.println("Ignored double click on " + 
//                            m_data.getNotification(tblNotifications.rowAtPoint(event.getPoint())).toString() + 
//                            " because notifications for " + 
//                            job.getName() + " is currently loading.");
//                }
                }
            }
            
        });
        
        JScrollPane ps = new JScrollPane();
        ps.getViewport().setBackground(tblNotifications.getBackground());
        ps.getViewport().add(tblNotifications);
       
        jobBox.add(ps, BorderLayout.CENTER);
        jobBox.setPreferredSize(new Dimension(600,350));
        
        return jobBox;
    }
    
    private Box createButtonFooter() {
        Box buttonBox = Box.createHorizontalBox();
        
        btnAdd = new JButton("+");
        btnAdd.setToolTipText("Add an input file for this job.");
        btnAdd.setIcon(ICON_ADD);
        btnAdd.addActionListener(this);
        
        btnSub = new JButton("-");
        btnSub.setToolTipText("Remove the selected input file for this job.");
        btnSub.setIcon(ICON_SUB);
        btnSub.addActionListener(this);
        
        btnClear = new JButton("-");
        btnClear.setToolTipText("Clear all notifications for this job.");
        btnClear.addActionListener(this);
        
        buttonBox.add(btnAdd);
        buttonBox.add(btnSub);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(btnClear);
        
        return buttonBox;
        
    }
    
    protected void doEditNotification(NotificationBean oldNotification) {
        NotificationChooserPanel pnlNotificationChooser = new NotificationChooserPanel(oldNotification);
        System.out.println("Editing old notificaiton " + oldNotification.toString());
        int response = JOptionPane.showOptionDialog(this,
                pnlNotificationChooser,
                "Notification Editor", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.INFORMATION_MESSAGE, 
                null, 
                new String[] {"OK","Cancel"}, 
                "OK");
        
        System.out.println("Response was " + response);
        if (response == JOptionPane.OK_OPTION) {
            NotificationBean newNotification = pnlNotificationChooser.getNotification();
            System.out.println("New notificaiton " + newNotification.toString());
            if (!newNotification.getType().toString().equals(oldNotification.getType().toString()) ||
                    !newNotification.getStatus().toString().equals(oldNotification.getStatus())) {
                System.out.println("The notificaiton was changed.");
                UPDATENOTIFICATIONCommand command = new UPDATENOTIFICATIONCommand(this);
                command.getArguments().put("notification",newNotification);
                command.getArguments().put("progressDialog", new ProgressDialog(parent,""));
                
                statusChanged(new StatusEvent(command,Status.START));
            } else {
                System.out.println("The notificaiton was not changed.");
            }
        } else {
            System.out.println("Cancel was clicked.  The notificaiton was not changed.");
        }
        
    }
    
    public void actionPerformed(ActionEvent event) {
        
        if (event.getSource() == btnAdd) {
            NotificationChooserPanel pnlNotificationChooser = new NotificationChooserPanel();
            
            int response = JOptionPane.showOptionDialog(this,
                    pnlNotificationChooser,
                    "Notification Editor", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.INFORMATION_MESSAGE, 
                    null, 
                    new String[] {"OK","Cancel"}, 
                    "Cancel");
            
            if (response == JOptionPane.OK_OPTION) {
                NotificationBean notification = pnlNotificationChooser.getNotification();
                
                ADDNOTIFICATIONCommand command = new ADDNOTIFICATIONCommand(this);
                command.getArguments().put("progressDialog", new ProgressDialog(parent,""));
                command.getArguments().put("notification",notification);
                
                statusChanged(new StatusEvent(command,Status.START));
            }
            
        } else if (event.getSource() == btnSub) {
            
            REMOVENOTIFICATIONCommand command = new REMOVENOTIFICATIONCommand(this);
            command.getArguments().put("notification",m_data.getNotification(tblNotifications.getSelectedRow()));
            command.getArguments().put("progressDialog", new ProgressDialog(parent,""));
            
            statusChanged(new StatusEvent(command,Status.START));
            
        } else if (event.getSource() == btnClear) {
            
            CLEARNOTIFICATIONSCommand command = new CLEARNOTIFICATIONSCommand(this);
            command.getArguments().put("progressDialog", new ProgressDialog(parent,""));
            command.getArguments().put("job",job);
            
            statusChanged(new StatusEvent(command,Status.START));
        }
    }
    
    public void statusChanged(StatusEvent event) {
        Status status = event.getStatus();
        System.out.println("Status changed to: " + status.name());
        System.out.println("StatusListener is: " + event.getSource().getClass().getName());
        
        final JobCommand command = (JobCommand) event.getSource();
        System.out.println("stats=" + status.name() + ", type=" + command.getClass());
        
        //What to do if things complete successfully.
        
        if (status.equals(Status.START)) {
            if (Settings.VERBOSE)
                System.out.println("Starting " + command.getCommand() + " command");
           
            String title = "";
            String message = "";
            
            SwingWorker worker = new SwingWorker() {
                
                public Object construct() {
                    try {
                        
                        command.execute();
                        
                        if (Thread.interrupted()) {
                            throw new InterruptedException(command.getCommand() + 
                                    " Command cancelled by user.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } 
                    
                    return null;
                }
                
//                public void finish() {
//                    m_data.addAll(((JobBean)command.getArguments().get("job")).getLogicalInputFiles());
//                    stopWaiting();
//                }
            };
            
            startWaiting();
            
            worker.start();
            
        } else if (status.equals(Status.COMPLETED)) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (command.getCommand().equals(JobCommand.GETNOTIFICATIONS)) {
                        
                        m_data.addAll((ArrayList<NotificationBean>)command.getOutput());
                    
                    } else if (command.getCommand().equals(JobCommand.ADDNOTIFICATION)) {
                    
                        m_data.add((NotificationBean)command.getOutput());
                    
                    } else if (command.getCommand().equals(JobCommand.REMOVENOTIFICATION)) {
                    
                        m_data.remove((NotificationBean)command.getArguments().get("notification"));
                    
                    } else if (command.getCommand().equals(JobCommand.CLEARNOTIFICATIONS)) {
                    
                        m_data.clear();
                    } else if (command.getCommand().equals(JobCommand.UPDATENOTIFICATION)) {
                        int nRow = tblNotifications.getSelectedRow();
                        m_data.replace(nRow,(NotificationBean)command.getArguments().get("notification"));
                    }
                    
                    stopWaiting();
                }
            });
            
            
        } else {
            
            Exception e = (Exception)command.getArguments().get("exception");
            
            JOptionPane.showMessageDialog(this, e.getMessage(), "Notification Error", JOptionPane.OK_OPTION);
            
            stopWaiting();
        } 
    }
    
    private void startWaiting() {
        btnAdd.setEnabled(false);
        btnSub.setEnabled(false);
        tblNotifications.setEnabled(false);
    }
    
    private void stopWaiting() {
        btnAdd.setEnabled(true);
        btnSub.setEnabled(true);
        tblNotifications.setEnabled(true);
    }

    class NotificationChooserPanel extends javax.swing.JPanel {
        private JComboBox cmbType;
        private JComboBox cmbStatus;
        private JLabel lblMessage;
        
        private NotificationBean notification = null;   //new NotificationBean(job.getJobID(),; 
//                                                                JobStatusType.FINISHED,
//                                                                NotificationType.EMAIL);
        
        public NotificationChooserPanel() {
            super(new BorderLayout(0,5));
            
            lblMessage = new JLabel("<html><body><p>Select the type and event of</br>the notification you would like</br>to receive.</p></body</html>");
            
            add (lblMessage, BorderLayout.NORTH);
            
            JPanel subPanel = new JPanel();
            cmbType = new JComboBox(new DefaultComboBoxModel(NotificationType.values()));
            subPanel.add(cmbType);
            cmbStatus = new JComboBox(new DefaultComboBoxModel(JobStatusType.values()));
            subPanel.add(cmbStatus);
            
            add(subPanel,BorderLayout.CENTER);
        }
        
        public NotificationChooserPanel(NotificationBean notification) {
            super(new BorderLayout(0,5));
            
            this.notification = notification;
            
            lblMessage = new JLabel("<html><body><p>Select the type and event of</br>the notification you would like</br>to receive.</p></body</html>");
            
            add (lblMessage, BorderLayout.NORTH);
            
            JPanel subPanel = new JPanel();
            cmbType = new JComboBox(new DefaultComboBoxModel(NotificationType.values()));
            cmbType.setSelectedItem(notification.getType());
            subPanel.add(cmbType);
            cmbStatus = new JComboBox(new DefaultComboBoxModel(JobStatusType.values()));
            cmbStatus.setSelectedItem(notification.getStatus());
            subPanel.add(cmbStatus);
            
            add(subPanel,BorderLayout.CENTER);
        }
        
        public NotificationBean getNotification() {
            notification.setType((NotificationType)cmbType.getSelectedItem());
            notification.setStatus((JobStatusType)cmbStatus.getSelectedItem());
            notification.setJobId(job.getId());
            return notification;
        }
    }
}

class ColumnData {

    public String   m_title;
    public int       m_width;
    public int       m_alignment;

    public ColumnData(String title, int width, int alignment) {
        m_title = title;
        m_width = width;
        m_alignment = alignment;
    }
}

class NotificationTableModel extends AbstractTableModel{
    private static double[] arrSize = new double[]{1<<10, 1<<20, 1<<30, 1<<40};
    
    static final public ColumnData m_columns[] = {
        new ColumnData( "Type", 100, JLabel.LEFT ),
        new ColumnData( "Event",100, JLabel.LEFT ) };
    
    protected Vector m_vector;
    protected Date   m_date;
    protected int m_columnsCount = m_columns.length;    

    public int      m_sortCol = 0;
    public boolean m_sortAsc = false;
    
    public StatusListener statusListener;
    
    public NotificationTableModel(StatusListener statusListener) {
        m_vector = new Vector<NotificationBean>();
        this.statusListener = statusListener;
    }
    
    public int getColumnCount() {
        return m_columnsCount;
    }

    public int getRowCount() {
        return m_vector==null ? 0 : m_vector.size();
    }

    public Object getValueAt(int nRow, int nCol) {
        if (nRow < 0 || nRow>=getRowCount())
            return "";
        NotificationBean row = (NotificationBean)m_vector.elementAt(nRow);
        switch (nCol) {
            case 0: return row.getType();
            case 1: return row.getStatus();
        }
        return "";
    }
    
    public void sortData() {
        Collections.sort(m_vector);
    }
    
    public NotificationBean getNotification(int nRow) {
        return (NotificationBean)m_vector.get(nRow);
    }
    
    public void add(NotificationBean notification) {
        if (!m_vector.contains(notification)) {
            m_vector.add(notification);
            this.fireTableRowsInserted(m_vector.size()-1, m_vector.size()-1);
            
            ADDNOTIFICATIONCommand command = new ADDNOTIFICATIONCommand(statusListener);
            command.getArguments().put("notification",notification);
            
            statusListener.statusChanged(new StatusEvent(command,Status.START));
        }
    }
    
    public void addAll(ArrayList<NotificationBean> notifications) {
        int oldSize = m_vector.size();
        for (NotificationBean notification: notifications) {
            if (!m_vector.contains(notification)) {
                m_vector.add(notification);
            }
        }
        this.fireTableRowsInserted(oldSize, m_vector.size()-1);
        
    }
    
    public void remove(int nRow) {
        m_vector.remove(nRow);
        this.fireTableRowsDeleted(nRow, nRow);
    }
    
    public void remove(NotificationBean notification) {
        int nRow = m_vector.indexOf(notification);
        m_vector.remove(notification);
        this.fireTableRowsDeleted(nRow, nRow);
    }
    
    public void replace(int nRow, NotificationBean notification) {
        m_vector.remove(nRow);
        m_vector.insertElementAt(notification, nRow);
        this.fireTableDataChanged();
    }
    
    public void clear() {
        m_vector.clear();
        this.fireTableDataChanged();
        
    }
    
}
