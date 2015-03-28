/* 
 * Created on May 1, 2008
 * 
 * Developed by: Rion Dooley - dooley [at] tacc [dot] utexas [dot] edu
 * 				 TACC, Texas Advanced Computing Center
 * 
 * https://www.tacc.utexas.edu/
 */

package org.gridchem.client.gui.jobsubmission;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.gridchem.client.SubmitJobsWindow;
import org.gridchem.client.SwingWorker;
import org.gridchem.client.common.Settings;
import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.gui.jobsubmission.commands.GETINPUTCommand;
import org.gridchem.client.gui.jobsubmission.commands.JobCommand;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.util.Env;
import org.gridchem.client.util.file.FileUtility;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.LogicalFileBean;

import com.asprise.util.ui.progress.ProgressDialog;
//import com.sun.jdi.connect.Connector.SelectedArgument;


/**
 * Insert Template description here.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class InputFilePanel extends JPanel implements ActionListener, StatusListener {
    
    public static ImageIcon ICON_ADD = new ImageIcon(Env.getImagesDir() + 
            File.separator + "icons" + File.separator + "addinput.jpg");
    public static ImageIcon ICON_SUB = new ImageIcon(Env.getImagesDir() + 
            File.separator + "icons" + File.separator + "subinput.jpg");
    
    public static JFileChooser chooser = new JFileChooser();
    
    private JTable table;
    private InputFileTableModel m_data;
    
    private JButton bAdd = new JButton("");
    private JButton bSub = new JButton("");
    protected JCheckBox scriptInput = new JCheckBox("Check for script input", false);
    
    protected FilePreviewPanel preview;
    protected EditJobPanel parent;
    
    private int selectedFileIndex = -1;
    
    protected boolean isLoading = false;
    
    public InputFilePanel(EditJobPanel parent) {
        super();
        
        this.parent = parent;
        
        
//        JPanel panel = new JPanel();
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        // Create the layout for the input file table
        Box bList = Box.createVerticalBox();
        bList.add(createTable());
        bList.add(createButtonFooter());
        bList.setMinimumSize(new Dimension(300,150));
        //bList.setMaximumSize(new Dimension(Short.MAX_VALUE,150));
        bList.setPreferredSize(new Dimension(250,150));
        bList.setBorder(BorderFactory.createEtchedBorder());
        
        // Create the preview pane
        preview = new FilePreviewPanel(this);
        preview.setBorder(BorderFactory.createEtchedBorder());
        add(bList);
        add(preview);
//        setPreferredSize(new Dimension(250,500));
//        add(panel);
//        this.setVisible(true);
    }
    
    public void addTextInput(String input) {
        preview.setEditPaneContent(input);
    }
    
    public String retrieveTextInput() {
    	return preview.getEditPaneContent();
    }

    public void addMultipleFileInput(List<File> files) {
        m_data.addAll(files);
    }
    
    public void addMultipleLogicalFileInput(List<LogicalFileBean> files) {
        for (LogicalFileBean file: files) {
            //TODO: if the file doesn't exist, download it from the file server.  need a handle to the job to do this, though...refactor :)
            File f = new File(file.getLocalPath());
            // if any of the input files are no longer there, we download all
            // job input files fresh from the file service. 
            if (f.exists())  {
                m_data.add(f);
            } else {
                // remove existing files since well be getting a copy.
                m_data.clear();
                
                // monitor the progress of the downloading files.
                ProgressDialog progressDialog = new ProgressDialog(SubmitJobsWindow.frame,
                    "Job Submission Progress");
                progressDialog.millisToPopup = 0;
                progressDialog.millisToDecideToPopup = 0;
                progressDialog.displayTimeLeft = false;
            
                GETINPUTCommand command = new GETINPUTCommand(this);
                command.getArguments().put("job", parent.job);
                command.getArguments().put("progressDialog", progressDialog);
                
                statusChanged(new StatusEvent(this,Status.START));
                
                // once we find one missing file, we found out what we 
                // wanted to know.
                break;
            }
        }
    }
    
    public void addFileInput(File file) {
        m_data.add(file);
    }
    
    public void removeSelectedInputFile() {
    	if (selectedFileIndex >= 0) {
    		m_data.remove(selectedFileIndex);
    		preview.clear();
    		selectedFileIndex = m_data.getRowCount() - 1;
    	} else {
    		// Throw an exception
    	}
    }
    
    public void clearFileInput() {
        m_data.clear();
        preview.clear();
    }
    
    private Box createTable() {
        Box jobBox = Box.createVerticalBox();
        m_data = new InputFileTableModel();
        
        table = new JTable();
        table.setAutoCreateColumnsFromModel(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table.setRowSelectionAllowed(true);
        
        for (int k=0; k < m_data.getColumnCount(); k++) {
            TableColumn column = new TableColumn(k,
                    InputFileTableModel.m_columns[k].m_width,
                    new DefaultTableCellRenderer(),
                    null);
            column.setHeaderValue(InputFileTableModel.m_columns[k].m_title);
//            column.setHeaderRenderer(createDefaultRenderer());
            table.addColumn(column);
        }
        
        table.setModel(m_data);
        
        table.addMouseListener(new MouseAdapter() {

            /* (non-Javadoc)
             * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
             */
            @Override
            public void mouseClicked(final MouseEvent event) {
//                super.mouseClicked(arg0);
                if (event.getClickCount() == 2) {
                    if (!isLoading) {
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                            	selectedFileIndex = table.rowAtPoint(event.getPoint());
                                preview.loadFile(m_data.getFile(table.rowAtPoint(event.getPoint())));        
                            }
                            
                        });
                        
                    } else {
                        System.out.println("Ignored double click on " + m_data.getFile(table.rowAtPoint(event.getPoint())).getName() + " because " + 
                                preview.getCurrentFile().getName() + " is currently loading.");
                    }
                }
            }
            
        });
        
//        JTableHeader header = table.getTableHeader();
//        header.setUpdateTableInRealTime(true);
//        header.addMouseListener(new ColumnListener());
        
        JScrollPane ps = new JScrollPane();
        ps.getViewport().setBackground(table.getBackground());
        ps.getViewport().add(table);
       
        jobBox.add(ps, BorderLayout.CENTER);
        jobBox.setPreferredSize(new Dimension(600,350));
        
        return jobBox;
    }
    
    private Box createButtonFooter() {
        Box buttonBox = Box.createHorizontalBox();
        
        Image addImage = ICON_ADD.getImage();
        Image newAddImage = addImage.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        
        Image removeImage = ICON_SUB.getImage();
        Image newRemoveImage = removeImage.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        
        bAdd.setToolTipText("Add an input file for this job.");
        bAdd.setIcon(new ImageIcon(newAddImage));
        bAdd.addActionListener(this);
        
        bSub.setToolTipText("Remove the selected input file for this job.");
        bSub.setIcon(new ImageIcon(newRemoveImage));
        bSub.addActionListener(this);
        
        scriptInput.addActionListener(parent);
        
        buttonBox.add(bAdd);
        buttonBox.add(bSub);
        buttonBox.add(scriptInput);
        buttonBox.add(Box.createHorizontalGlue());
        
        return buttonBox;
        
    }
    
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == bAdd) {
            //JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                m_data.add(chooser.getSelectedFile());
            }
        } else if (event.getSource() == bSub) {
        	if (table.getSelectedRow() == -1) return;
        	
        	File sModel = m_data.getFile(table.getSelectedRow());
        	File previewFile = preview.getCurrentFile();
        	System.out.println(" File to be removed from Panel is "+sModel.getAbsolutePath());
        			//System.out.println(" Compared to "+ previewFile.getAbsolutePath());
        	//if (sModel.getAbsolutePath().equals(previewFile.getAbsolutePath())) {
            //	preview.clear();
            //}
        	m_data.remove(table.getSelectedRow());
            
        }
        
    }
    
    public ArrayList<File> getInputFiles() {
        ArrayList<File> files = new ArrayList<File>();
        files.addAll(m_data.m_vector);
        
        return files;
    }
    
    public void addInputFile(File file) {
        m_data.add(file);
    }
    
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("File Upload GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new InputFilePanel(null));
        frame.setPreferredSize(new Dimension(300,600));
        frame.pack();
        frame.setVisible(true);
        
    }
    
    protected TableCellRenderer createDefaultRenderer() {
        DefaultTableCellRenderer label = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                             boolean isSelected, boolean hasFocus, int row, int column) {
                if (table != null) {
                    JTableHeader header = table.getTableHeader();
                    if (header != null) {
                        setForeground(header.getForeground());
                        setBackground(header.getBackground());
                        setFont(header.getFont());
                    }
                }

                setText((value == null) ? "" : value.toString());
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                return this;
            }
        };
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
    
    class ColumnListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            TableColumnModel colModel = table.getColumnModel();
            int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
            int modelIndex = colModel.getColumn(columnModelIndex).getModelIndex();

            if (modelIndex < 0)
                return;
            if (m_data.m_sortCol == modelIndex)
                m_data.m_sortAsc = !m_data.m_sortAsc;
            else
                m_data.m_sortCol = modelIndex;

            for (int i=0; i < m_data.getColumnCount(); i++) {
                TableColumn column = colModel.getColumn(i);
                int index = column.getModelIndex();
            }
            table.getTableHeader().repaint();

            m_data.sortData();
            table.tableChanged(new TableModelEvent(m_data));
            table.repaint();
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
//                    m_data.addAll(((JobDTO)command.getArguments().get("job")).getLogicalInputFiles());
//                    stopWaiting();
//                }
            };
            
            startWaiting();
            
            worker.start();
            
        } else if (status.equals(Status.COMPLETED)) {
            for (LogicalFileBean logicalFile: ((JobBean)command.getArguments().get("job")).getInputFiles()) {
                File file = new File(logicalFile.getLocalPath());
                if (file.exists()) {
                    m_data.add(file);
                } else {
                    String message = "Could not locate input file " + logicalFile.getLocalPath() + 
                            ".  Please check to make sure the download succeeded.";
                    System.out.println(message);
                    JOptionPane.showMessageDialog(this,message,"File Transfer Error",JOptionPane.ERROR_MESSAGE);
                }
            }
            stopWaiting();
        } else {
            Exception e = (Exception)command.getArguments().get("exception");
            int loadDefaultInputFiles = JOptionPane.showConfirmDialog(this, e.getMessage(), "File Tranfer Error", JOptionPane.YES_NO_OPTION);
            
            if (loadDefaultInputFiles == JOptionPane.YES_OPTION) {
                addMultipleFileInput(FileUtility.getDefaultInputFiles(parent.job.getSoftwareName()));
            }
            
            stopWaiting();
        } 
    }
    
    private void startWaiting() {
        bAdd.setEnabled(false);
        bSub.setEnabled(false);
        table.setEnabled(false);
    }
    
    private void stopWaiting() {
        bAdd.setEnabled(true);
        bSub.setEnabled(true);
        table.setEnabled(true);
    }

	public boolean isEmpty() {
		return m_data.isEmpty();
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

class InputFileTableModel extends AbstractTableModel{
    private static double[] arrSize = new double[]{1<<10, 1<<20, 1<<30, 1<<40};
    
    static final public ColumnData m_columns[] = {
        new ColumnData( "Name", 160, JLabel.LEFT ),
        new ColumnData( "Size", 40, JLabel.LEFT ) };
    
    protected Vector<File> m_vector;
    protected Date   m_date;
    protected int m_columnsCount = m_columns.length;    

    public int      m_sortCol = 0;
    public boolean m_sortAsc = false;
    
    public InputFileTableModel() {
        m_vector = new Vector<File>();
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
        File row = (File)m_vector.elementAt(nRow);
        switch (nCol) {
            case 0: return row.getName();
            case 1: return formatSize(row.length());
        }
        return "";
    }
    
    public void sortData() {
        Collections.sort(m_vector);
    }
    
    public File getFile(int nRow) {
        return m_vector.get(nRow);
    }
    
    public void add(File file) {
        if (!m_vector.contains(file)) {
            m_vector.add(file);
            this.fireTableRowsInserted(m_vector.size()-1, m_vector.size()-1);
        }
    }
    
    public void addAll(List<File> files) {
        int oldSize = m_vector.size();
        for (File file: files) {
            if (!m_vector.contains(file)) {
                m_vector.add(file);
            }
        }
        this.fireTableRowsInserted(oldSize, m_vector.size()-1);
        
    }
    
    public void remove(int nRow) {
        m_vector.remove(nRow);
        this.fireTableRowsDeleted(nRow, nRow);
    }
    
    public void clear() {
        m_vector.clear();
        this.fireTableDataChanged();
    }
    
    private String formatSize(long nSize) {
        String sSize;
        NumberFormat f = NumberFormat.getInstance();
        f.setMaximumFractionDigits(1);

        if(nSize < arrSize[0]){
            sSize = String.valueOf(nSize) + " B";
        }else if(nSize < arrSize[1]){
            sSize = String.valueOf(f.format(nSize / arrSize[0])) + " KB";
        }else if(nSize < arrSize[2]){
            sSize = String.valueOf(f.format(nSize / arrSize[1])) + "MB";
        }
        else{
            sSize = String.valueOf(f.format(nSize / arrSize[2])) + "GB";
        }
        return sSize;
    }
    
    public boolean isEmpty() {
    	return m_vector.isEmpty();
    }
    
}