package org.gridchem.client.gui.panels.myccg.job;

/* Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Apr 17, 2006
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nanocad.nanocadFrame2;
import nanocad.newNanocad;

import org.gridchem.client.DataTree;
import org.gridchem.client.GridChem;
import org.gridchem.client.SpectraOutputParser;
import org.gridchem.client.SpectraViewer;
import org.gridchem.client.SwingWorker;
import org.gridchem.client.TableSorter;
import org.gridchem.client.Trace;
import org.gridchem.client.common.Settings;
import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.exceptions.GMSException;
import org.gridchem.client.exceptions.SessionException;
import org.gridchem.client.exceptions.VisualizationException;
import org.gridchem.client.gui.filebrowser.FileBrowserImpl;
import org.gridchem.client.gui.jobsubmission.EditJobPanel;
import org.gridchem.client.gui.jobsubmission.commands.DELETECommand;
import org.gridchem.client.gui.jobsubmission.commands.GETOUTPUTCommand;
import org.gridchem.client.gui.jobsubmission.commands.HIDECommand;
import org.gridchem.client.gui.jobsubmission.commands.JobCommand;
import org.gridchem.client.gui.jobsubmission.commands.KILLCommand;
import org.gridchem.client.gui.jobsubmission.commands.PredictTimeCommand;
import org.gridchem.client.gui.jobsubmission.commands.QSTATCommand;
import org.gridchem.client.gui.jobsubmission.commands.SEARCHCommand;
import org.gridchem.client.gui.jobsubmission.commands.UNHIDECommand;
import org.gridchem.client.gui.jobsubmission.commands.UPDATECommand;
import org.gridchem.client.gui.login.LoginDialog;
import org.gridchem.client.gui.metadataeditor.MetaDataEditor;
import org.gridchem.client.gui.panels.CancelCommandPrompt;
import org.gridchem.client.gui.panels.WarningDialog;
import org.gridchem.client.gui.panels.myccg.MonitorVO;
import org.gridchem.client.gui.panels.myccg.job.notification.NotificationManagerDialog;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.util.Env;
import org.gridchem.service.beans.ComputeBean;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.exceptions.PermissionException;
import org.gridchem.service.model.enumeration.AccessType;
import org.gridchem.service.model.enumeration.JobStatusType;

import cct.dialogs.JChoiceDialog;
import cct.dialogs.JEditorFrame;
import cct.gamess.GamessOutput;
import cct.gaussian.ParseGaussianOutput;
import cct.interfaces.AtomInterface;
import cct.interfaces.MoleculeInterface;
import cct.modelling.CCTAtomTypes;
import cct.modelling.MolecularGeometry;
import cct.modelling.Molecule;
import cct.tools.ui.JShowText;
import cct.vecmath.vPoint3f;

/**
 * Panel to display and provide manipulation for user jobs. This was initially
 * adapted from the InternalStuff panel inside the ManageWindow class. The
 * difference between the two is that this class expands the job display window,
 * provides mouseover events for the individual jobs, and popup panels providing
 * job manipulation previously available from several different places.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 * 
 */
public class JobPanel extends JPanel implements StatusListener, ActionListener,
		ListSelectionListener {
	private static String DELETE = "org.gridchem.service.gms.exceptions.JobDeleteException";
	private static String KILL = "org.gridchem.service.gms.exceptions.JobKillException";

	JPanel buttonBox;
	Container queueBox;

	// nanocadFrame2 nanWin;

	private static ArrayList<JobBean> searchResults = new ArrayList<JobBean>();
	private Container jobBox;
	public JTable jobTable;
	protected JobTableData m_data;
	public static TableSorter sorter;
	public static String dataFileName;
	public static int doNanocad = 0;
	public static int doMolden = 0;
	public static int doJMol = 0; // this is JMolEditor
	public static int doJMOL = 0; // this is JMOL
	public static int doAbaqus = 0;
	public static int doVMD = 0;
	public static int doSpectra = 0;
	public static int jEditorFrame = 0;
	JEditorFrame frame;
	public static String moldenPath;
	public static String VMDPath;
	public static String JMOLPath;
	public static String moldenPathFileLoc;
	public static String VMDPathFileLoc;
	public static String JMOLPathFileLoc;
	public static String AbaqusCAEPathLoc;
	public static nanocadFrame2 nanWin;
	public static String jobName;
	public static String[] oldJobs;
	public static String HPCsys;
	public static String[] columnNames =
	// {"Name","Research Project","Date","Time","Machine","Queue","Allocation","Status","ID"};
	{ "ID", "Name", "Research Project", "Date", "Time", "Machine",
			"LocalJobID", "Status" };

	int defaultColumns[] = { 0, 1, 13, 4, 5, 7, 10 };

	public static int num_column = columnNames.length;
	private List<JobBean> jobs;
	private Clipboard clipboard;
	private JFrame jobFrame;

	// main panel buttons
	private JButton statusButton;
	private JButton estTimeButton;
	private JButton dataButton;
	private JButton moldenButton;
	private JButton refreshButton;
	private JButton killButton;
	private JButton retrieveButton;
	private JButton deleteButton;
	private JButton cancelButton;
	private JButton searchButton;

	// menu and declarations
	private JPopupMenu rightClickPopup;

	private JMenuItem copyMenuItem;
	private JMenuItem deleteMenuItem;
	private JMenuItem hideMenuItem;
	private JMenuItem showHiddenMenuItem;
	private JMenuItem clearSearchMenuItem;
	private JMenuItem getInfoMenuItem;
	private JMenuItem estTimeMenuItem;
	private JMenuItem viewOutputMenuItem;
	private JMenuItem browseFilesMenuItem;
	private JMenuItem editTagsMenuItem;
	private JMenuItem resubmitMenuItem;
	private JMenuItem editNotificationsMenuItem;
	private JMenuItem refreshMenuItem;
	private JMenuItem killMenuItem;

	private JMenu postProcessingMenu;
	private JMenuItem viewMoldenItem;
	private JMenuItem viewVMDItem;
	private JMenuItem viewSpectraItem;
	private JMenuItem viewJMolItem; // this is JMolEditor in fact
	private JMenuItem viewJMOLItem; // this is JMOL
	private JMenuItem viewAbaqusCAEItem; // this is Abaqus CAE

	private JMenu viewMenu;

	private static boolean updatedWithSearchResults = false;

	private SearchDialog searchDialog;
	private JDialog getInfoDialog;
	private static JobCommand lastSearch;
	private CancelCommandPrompt progressCancelPrompt;

	protected ArrayList<String> hiddenJobs = new ArrayList<String>(); // list of
																		// jobs
																		// in
																		// the
																		// current
																		// job
																		// set
																		// which
																		// are
																		// hidden
	// if a showall action is cancelled, these jobs are rehidden

	private boolean commandCancelled = false; // flag to determine if we need to
												// stop processing
												// the current task and roll
												// back the actions thus far.

	private boolean inProcess = false;

	KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,
			ActionEvent.CTRL_MASK, false);
	KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,
			ActionEvent.CTRL_MASK, false);

	public JobPanel(List<JobBean> jobs) {

		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		this.jobs = jobs;
		// setLayout(new BoxLayout(this,BoxLayout.X_AXIS));

		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridheight = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(createJobContainer(this.jobs), c);

		c.fill = GridBagConstraints.NONE;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridheight = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(createButtonPanel(), c);

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		rightClickPopup = createPopupMenu();

		refresh();

	}

	/**
	 * Creates the popup menu the user sees when they right click on the jobs
	 * table. In general, the popup menu provides job specific functionality,
	 * whereas the panel buttons provide general job related functionality.
	 * 
	 * @return
	 */
	private JPopupMenu createPopupMenu() {
		PopupListener popupListener = new PopupListener();
		rightClickPopup = new JPopupMenu();
		copyMenuItem = new JMenuItem("Copy");
		deleteMenuItem = new JMenuItem("Delete");
		hideMenuItem = new JMenuItem("Hide");
		showHiddenMenuItem = new JMenuItem("Show Hidden");
		clearSearchMenuItem = new JMenuItem("Clear Search");
		getInfoMenuItem = new JMenuItem("Get Job Status");
		estTimeMenuItem = new JMenuItem("Est. Start/End Time");
		viewOutputMenuItem = new JMenuItem("Monitor Output");

		browseFilesMenuItem = new JMenuItem("Browse Mass Storage");
		editTagsMenuItem = new JMenuItem("Edit Tags");
		resubmitMenuItem = new JMenuItem("Resubmit");
		killMenuItem = new JMenuItem("Kill");
		refreshMenuItem = new JMenuItem("Refresh");
		editNotificationsMenuItem = new JMenuItem("Edit Notifications");
		postProcessingMenu = new JMenu("Post-Processing");
		viewMoldenItem = new JMenuItem("Output in Molden");
		viewVMDItem = new JMenuItem("Output in VMD");
		viewJMolItem = new JMenuItem("Output in JMolEditor");
		viewJMOLItem = new JMenuItem("Output in JMOL");
		viewAbaqusCAEItem = new JMenuItem("Output in Abaqus CAE");
		viewSpectraItem = new JMenuItem("Spectra");
		viewMenu = new JMenu("View...");

		TableColumnModel model = jobTable.getColumnModel();
		HashSet<TableColumn> hiddenColumnns = new HashSet<TableColumn>();
		for (int k = 0; k < m_data.getColumnCount(); k++) {
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(
					JobTableData.m_columns[k].m_title);

			boolean defaultColumn = false;

			item.setSelected(false);
			for (int i : defaultColumns) {
				if (k == i) {
					item.setSelected(true);
					defaultColumn = true;
					break;
				}
			}

			TableColumn column = model.getColumn(k);
			item.addActionListener(new ColumnKeeper(column,
					JobTableData.m_columns[k]));
			viewMenu.add(item);
			if (!defaultColumn) {
				hiddenColumnns.add(column);
			}

		}

		for (TableColumn column : hiddenColumnns) {
			model.removeColumn(column);
		}
		postProcessingMenu.add(viewMoldenItem);
		postProcessingMenu.add(viewVMDItem);
		postProcessingMenu.add(viewSpectraItem);
		postProcessingMenu.add(viewJMolItem);
		postProcessingMenu.add(viewJMOLItem);
		postProcessingMenu.add(viewAbaqusCAEItem);

		rightClickPopup.add(getInfoMenuItem);
		rightClickPopup.add(viewMenu);
		rightClickPopup.add(copyMenuItem);
		rightClickPopup.add(hideMenuItem);
		rightClickPopup.add(showHiddenMenuItem);
		rightClickPopup.add(deleteMenuItem);
		rightClickPopup.add(clearSearchMenuItem);
		rightClickPopup.add(refreshMenuItem);
		rightClickPopup.addSeparator();
		rightClickPopup.add(resubmitMenuItem);
		rightClickPopup.add(viewOutputMenuItem);
		// rightClickPopup.add(editNotificationsMenuItem);
		rightClickPopup.add(postProcessingMenu);
		rightClickPopup.add(editTagsMenuItem);
		rightClickPopup.add(browseFilesMenuItem);
		rightClickPopup.add(killMenuItem);

		// rightClickPopup.add(estTimeMenuItem);

		copyMenuItem.addActionListener(popupListener);
		hideMenuItem.addActionListener(popupListener);
		showHiddenMenuItem.addActionListener(popupListener);
		deleteMenuItem.addActionListener(popupListener);
		clearSearchMenuItem.addActionListener(popupListener);
		getInfoMenuItem.addActionListener(popupListener);
		estTimeMenuItem.addActionListener(popupListener);
		viewOutputMenuItem.addActionListener(popupListener);
		editNotificationsMenuItem.addActionListener(popupListener);
		viewMoldenItem.addActionListener(popupListener);
		viewVMDItem.addActionListener(popupListener);
		viewSpectraItem.addActionListener(popupListener);
		viewJMolItem.addActionListener(popupListener);
		viewJMOLItem.addActionListener(popupListener);
		viewAbaqusCAEItem.addActionListener(popupListener);
		browseFilesMenuItem.addActionListener(popupListener);
		editTagsMenuItem.addActionListener(popupListener);
		resubmitMenuItem.addActionListener(popupListener);
		refreshMenuItem.addActionListener(popupListener);
		killMenuItem.addActionListener(popupListener);

		return rightClickPopup;
	}

	public Container getJobContainer() {
		return jobBox;
	}

	/**
	 * Creates the button panel visible on the main job panel. The buttons
	 * represent general job related functionality that may or may not relate to
	 * a specific job. This is not a strict statement right now, however, due to
	 * the migration from pre-ws to ws.
	 * 
	 * @return
	 */
	private JPanel createButtonPanel() {

		// create all buttons
		statusButton = new JButton("Get Job Status");
		estTimeButton = new JButton("Est. Start/End Time");
		dataButton = new JButton("Monitor Output");
		moldenButton = new JButton("View in Molden");
		killButton = new JButton("Kill Job");
		retrieveButton = new JButton("Browse Mass Storage");
		deleteButton = new JButton("Delete Job from List");
		refreshButton = new JButton("Refresh Jobs");
		searchButton = new JButton("Search for Jobs");
		cancelButton = new JButton("Close");

		// lay them out in the button panel
		JPanel buttonBoxPane = new JPanel();
		JPanel buttonBox = new JPanel();

		buttonBox.setLayout(new GridLayout(8, 1, 5, 5));
		buttonBox.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

		Border eBorder1 = BorderFactory.createEmptyBorder(0, 10, 0, 0);
		Border leBorder = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);

		buttonBoxPane.setBorder(eBorder1);
		buttonBoxPane.setLayout(new BoxLayout(buttonBoxPane, BoxLayout.Y_AXIS));
		buttonBoxPane.add(buttonBox);
		buttonBox.add(statusButton);
		// buttonBox.add(estTimeButton);
		buttonBox.add(dataButton);
		buttonBox.add(moldenButton);
		buttonBox.add(killButton);
		buttonBox.add(searchButton);
		buttonBox.add(retrieveButton);
		buttonBox.add(refreshButton);
		buttonBox.add(cancelButton);

		// add all event listeners here
		statusButton.addActionListener(this);
		statusButton.setToolTipText("Find the status of the selected job.");
		estTimeButton.addActionListener(this);
		estTimeButton
				.setToolTipText("Estimate Start/End of Time of a Scheduled Job");
		dataButton.addActionListener(this);
		moldenButton.addActionListener(this);
		moldenButton
				.setToolTipText("Visualize and Post-Process Output using Molden");
		dataButton
				.setToolTipText("<html>View the progress of the selected job."
						+ "<br>The current output will be retrieved and post-processed."
						+ "<br>Convergence information may be plotted."
						+ "</html>");
		killButton.addActionListener(this);
		killButton.setToolTipText("Kill the selected job.");
		retrieveButton.addActionListener(this);
		retrieveButton
				.setToolTipText("<html>Obtain a file listing of the mass storage area for"
						+ "<br>the selected job.  In addition to output files,"
						+ "<br>the input file and checkpoint file may be available."
						+ "<br>The population of mass storage occurs after the job"
						+ "<br>terminates and may require a significant amount of time."
						+ "</html>");
		refreshButton.addActionListener(this);
		refreshButton
				.setToolTipText("<html>Refresh the jobs in the current window with the latest"
						+ "<br>status infomation.  All job information is subject to, at "
						+ "<br>worst, a 60 second lag.</html>");
		searchButton.addActionListener(this);
		searchButton
				.setToolTipText("Search for jobs matching a specific description.");
		cancelButton.addActionListener(this);
		cancelButton.setToolTipText("Close this window.");

		return buttonBoxPane;
	}

	/**
	 * Creates, populates, and initializes the container for the job table.
	 * 
	 * @return
	 */
	private Container createJobContainer(List<JobBean> jobs) {
		jobBox = Box.createVerticalBox();

		m_data = new JobTableData(jobs);

		jobTable = new JTable();
		jobTable.setAutoCreateColumnsFromModel(false);
		jobTable.setModel(m_data);
		jobTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jobTable.setRowSelectionAllowed(true);
		jobTable.setColumnSelectionAllowed(false);

		// set default table properties to ID,Name,Research
		// Project,Date,Time,Machine, and Status
		for (int k = 0; k < m_data.getColumnCount(); k++) {
			DefaultTableCellRenderer renderer = new ColoredTableCellRenderer();
			renderer.setHorizontalAlignment(JobTableData.m_columns[k].m_alignment);
			TableColumn column = new TableColumn(k,
					JobTableData.m_columns[k].m_width, renderer, null);
			column.setHeaderRenderer(createDefaultRenderer());
			jobTable.addColumn(column);
		}

		JTableHeader header = jobTable.getTableHeader();
		header.setUpdateTableInRealTime(true);
		header.addMouseListener(new ColumnListener());
		header.setReorderingAllowed(true);

		jobTable.getColumnModel().addColumnModelListener(m_data);

		JScrollPane ps = new JScrollPane();
		ps.getViewport().setBackground(jobTable.getBackground());
		ps.getViewport().add(jobTable);

		jobBox.add(ps, BorderLayout.CENTER);
		jobBox.setPreferredSize(new Dimension(600, 350));

		ListSelectionModel newModel = jobTable.getSelectionModel();
		newModel.addListSelectionListener(this);
		jobTable.setSelectionModel(newModel);
		jobTable.addMouseListener(new JobMouseAdapter());
		jobTable.setComponentPopupMenu(rightClickPopup);
		ActionListener copyListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().compareTo("Copy") == 0) {
					copyJob();
				}
			}
		};

		jobTable.registerKeyboardAction(copyListener, "Copy", copy,
				JComponent.WHEN_FOCUSED);

		return jobBox;
	}

	protected TableCellRenderer createDefaultRenderer() {
		DefaultTableCellRenderer label = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
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

	private void copyJob() {
		StringBuffer sbf = new StringBuffer();
		StringSelection stsel;

		// Check to ensure we have selected only a contiguous block of cells
		int numcols = jobTable.getModel().getColumnCount();
		int numrows = jobTable.getSelectedRowCount();
		int[] rowsselected = jobTable.getSelectedRows();
		int[] colsselected = jobTable.getSelectedColumns();

		// TODO: make work with right click popup menu. showing index out of
		// bounds exception: 0 at line 518
		if (!((numrows - 1 == rowsselected[rowsselected.length - 1]
				- rowsselected[0] && numrows == rowsselected.length) && (numcols - 1 == colsselected[colsselected.length - 1]
				- colsselected[0] && numcols == colsselected.length))) {
			JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
					"Invalid Copy Selection", JOptionPane.ERROR_MESSAGE);
			return;
		}

		for (int i = 0; i < numrows; i++) {
			for (int j = 0; j < numcols; j++) {
				sbf.append(jobTable
						.getValueAt(rowsselected[i], colsselected[j]));

				if (j < numcols - 1)
					sbf.append("\t");
			}
			sbf.append("\n");
		}

		stsel = new StringSelection(sbf.toString());
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stsel, stsel);
	}

	/**
	 * This is the external refresh method called by the MonitorVO container.
	 * This refresh deals with determining what has to be refreshed and then
	 * taking appropriate action. If search results are present, it refreshes
	 * those. If no search results are present, it refreshes the user's VO and
	 * displays the refreshed job list.
	 */
	public void refresh() {
		// we dont' want to do 2 things here. First, we need to make sure we
		// don't clear the user's search results from the table. the desired
		// action is to refresh whatever is in the job table, regardless of
		// how they found it. Second, we don't want to annoy the user, so we
		// set the "show.progress" flag to "false" so the JobPanel won't
		// pop up a progress panel when it's refreshing on the timer.
		JobCommand command;

		if (!isInProcess()) {
			if (isUpdatedWithSearchResults()) {
				command = getLastSearchCommand();
			} else {
				command = new UPDATECommand(this);
			}
			command.getArguments().put("show.progress", "false");
			statusChanged(new StatusEvent(command, Status.START));
		} else {
			if (Settings.DEBUG)
				System.out
						.println("Skipped update because gui was already busy.");
		}
	}

	/**
	 * This is the internal refresh jobs method. It will update the appropriate
	 * listing.
	 */
	protected void refreshJobs() {
		if (updatedWithSearchResults) {
			statusChanged(new StatusEvent(lastSearch, Status.START));
		} else {
			try {
				m_data.retrieveData();
			} catch (Exception e) {
				System.out.println("Error refeshing monitoring info: "
						+ e.getMessage());
			}
		}
	}

	/**
	 * Refresh the job windows with a new set of jobs. This is useful after a
	 * search and when synching with the GMS_WS.
	 * 
	 * @param jobs
	 */
	public synchronized void updateJobTable(List<JobBean> jobs) {

		jobTable.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		if (isUpdatedWithSearchResults()) {
			System.out.println("Search is present.  Loading new jobs...");
		} else {
			System.out.println("Refeshing jobs...");
		}

		m_data.loadData(jobs);

		jobTable.setCursor(Cursor.getDefaultCursor());

	}

	public JobCommand getLastSearchCommand() {

		return this.lastSearch;
	}

	/**
	 * Closes search dialog boxes.
	 */
	public void closeDialoges() {
		if (searchDialog != null) {
			searchDialog.setVisible(false);
			searchDialog.dispose();
		}

		if (getInfoDialog != null) {
			getInfoDialog.setVisible(false);
			getInfoDialog.dispose();
		}

		stopWaiting();

		if (jobFrame != null) {
			jobFrame.setVisible(false);
			jobFrame.dispose();
		}
	}

	private void enableButtonActions(boolean enable) {
		statusButton.setEnabled(enable);
		estTimeButton.setEnabled(enable);
		dataButton.setEnabled(enable);
		moldenButton.setEnabled(enable);
		searchButton.setEnabled(enable);
		killButton.setEnabled(enable);
		retrieveButton.setEnabled(enable);
		deleteButton.setEnabled(enable);
	}

	public void actionPerformed(ActionEvent e) {
		JobBean job = getSelectedJob();
		if (e.getSource() == statusButton) {
			if (job == null) {
				JOptionPane.showMessageDialog(null, "Please select a job.", "",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			System.out
					.println("Estimating Start and End Time of submitted Job");
			if (!job.getStatus().equals(JobStatusType.FINISHED)
					& !job.getStatus().equals(JobStatusType.STOPPED)
					& !job.getStatus().equals(JobStatusType.TIME_ELAPSED)) {

				PredictTimeCommand predicttimeCommand = new PredictTimeCommand(
						this);

				predicttimeCommand.getArguments().put("job", job);

				statusChanged(new StatusEvent(predicttimeCommand, Status.START));

			} else {
				createJobInfoDialog(job);
			}

			/*
			 * new SwingWorker() { public Object construct() { int range = 10;
			 * com.asprise.util.ui.progress.ProgressDialog progressDialog = new
			 * com.asprise.util.ui.progress.ProgressDialog(frame, "Progress");
			 * progressDialog.beginTask("Number Counting", range, true);
			 * 
			 * for(int i=0; i<range; i++) { if(progressDialog.isCanceled()) {
			 * System.out.println("CANCELED.\n\n"); break; }
			 * //JTextArea.append() is thread-safe. System.out.println(i +
			 * " of " + range + "\n"); progressDialog.worked(1); try {
			 * Thread.sleep(500); } catch (InterruptedException e) {
			 * e.printStackTrace(); } } return progressDialog; } }.start();
			 */

		} else if (e.getSource() == estTimeButton) {
			System.out.println("Right now this is a just dummy Button");
		} else if (e.getSource() == searchButton) {

			// search requires no job selection
			doSearchJobs();

		} else if (e.getSource() == killButton) {
			if (job == null) {
				JOptionPane.showMessageDialog(null, "Please select a job.", "",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			doKillJob(job);

		} else if (e.getSource() == dataButton) {
			if (job == null) {
				JOptionPane.showMessageDialog(null, "Please select a job.", "",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			doVisualizeJob(job);

		} else if (e.getSource() == moldenButton) {
			if (job == null) {
				JOptionPane.showMessageDialog(null, "Please select a job.", "",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			new moldenClass().start();

		} else if (e.getSource() == retrieveButton) {

			// if job is null, file browser will open at user's root directory
			doBrowseFiles(getSelectedJob());

		} else if (e.getSource() == refreshButton) {
			// refresh requires no job selection since the entire list of jobs
			// is refreshed every time
			refreshJobs();

		} else if (e.getSource() == cancelButton) {

			GridChem.oc.monitorWindow.dispose();

		} else {
			JOptionPane.showMessageDialog(null, "huh?",
					"This should not happen", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static String parseShowstart() {
		String timeString = "Feature not supported at this time";
		// timeString = GMS3.estStartTime;
		/*
		 * StringTokenizer timeToken=new StringTokenizer(GMS.estStartTime,"\n");
		 * while (timeToken.hasMoreTokens()){ String line1 =
		 * timeToken.nextToken(); line1 = line1.trim(); String consolePrint =
		 * "Estimated Rsv based start in"; int consoleLen =
		 * consolePrint.length(); if (line1.startsWith(consolePrint)){
		 * timeString = line1.substring(consoleLen,line1.indexOf("on"));
		 * timeString = timeString.trim(); if
		 * (timeString.toLowerCase().startsWith("infinity")){ timeString =
		 * "Strat time Unpredictable!!"; }else if (timeString.startsWith("-")) {
		 * timeString = "Job has started 'Running'. Its no longer scheduled.";
		 * 
		 * } else { timeString =
		 * timeString.substring(0,(timeString.length()-3)); if
		 * (timeString.equals("00:00")){
		 * 
		 * timeString= "Job expected to start anytime soon."; }else if
		 * (timeString.equals("")){ timeString =
		 * "Not able to predict start time."; }else { timeString =
		 * "Job expected to start in time (hh:mm): "+timeString; } } break;
		 * }else if (line1.toLowerCase().startsWith("error")){ timeString=
		 * "Either job has successfully finished or job is transitioning state from one to another.\n"
		 * +
		 * "      Try 'Monitor Job Output' and have a look at output file to find more about job."
		 * ; break; } } timeString = timeString.trim();
		 * System.out.println("value of timeString: "+timeString);
		 */
		return timeString;

	}

	public void estimateTime(JobBean job) {
		System.out.println("Estimating Start and End Time of submitted Job");
		if (!job.getStatus().equals(JobStatusType.FINISHED)
				& !job.getStatus().equals(JobStatusType.STOPPED)
				& !job.getStatus().equals(JobStatusType.TIME_ELAPSED)) {

			PredictTimeCommand predicttimeCommand = new PredictTimeCommand(this);

			predicttimeCommand.getArguments().put("job", job);

			statusChanged(new StatusEvent(predicttimeCommand, Status.START));

		} else {
			createJobInfoDialog(job);
		}
	}

	private void createJobInfoDialog(JobBean job) {
		getInfoDialog = new JobInfoDialog(job);
	}
	
	private void createJobInfoDialog(JobBean job, String estStartTime) {
		getInfoDialog = new JobInfoDialog(job, estStartTime);
	}

	/**
	 * open a MetaDataEditor to display and edit metadata for objects.
	 * 
	 * @param job
	 * 
	 */
	private void createJobMetadataDialog(JobBean job) {
		MetaDataEditor mde = new MetaDataEditor(job);
	}

	/**
	 * Show a popup dialog displaying the selected job's status.
	 * 
	 * @param job
	 */
	/*
	 * private void doShowJobStatusType(JobBean job) {
	 * JOptionPane.showMessageDialog( null, "This job is currently " +
	 * job.getStatus() + ".\n", "Job Status Update",
	 * JOptionPane.INFORMATION_MESSAGE ); } /** open a new editingStuff panel
	 * and load the fields with the information from the currently selected job
	 * 
	 * @param job
	 */
	private void doResubmit(final JobBean job) {
		SwingWorker worker = new SwingWorker() {
			public Object construct() {
				EditJobPanel jobEditor = new EditJobPanel(null, job);
				return jobEditor;
			}
		};
		worker.start();
	}

	private void doKillJob(final JobBean job) {
		if (job.getStatus().equals(JobStatusType.RUNNING)
				|| job.getStatus().equals(JobStatusType.SUBMITTING)
				|| job.getStatus().equals(JobStatusType.INITIAL)
				|| job.getStatus().equals(JobStatusType.SCHEDULED)
				|| job.getStatus().equals(JobStatusType.MIGRATING)) {

			KILLCommand killCommand = new KILLCommand(this);

			killCommand.getArguments().put("jobIDs", job.getId());

			statusChanged(new StatusEvent(killCommand, Status.START));

		} else {
			JOptionPane.showMessageDialog(this, "Job is already stopped.",
					"Job Management Error", JOptionPane.OK_OPTION);
		}
	}

	private void doSteerJob(JobBean job) {
		NotificationManagerDialog.getInstance(this.frame, job);
	}

	public void launchdownloadfile(JobBean job) {
		try {
			System.out
					.println("Output File does not exist on local machine; downloading it...");
			int result = JOptionPane
					.showConfirmDialog(
							null,
							"Output File does not exist on local machine.\n"
									+ "Please press 'OK' to download from server first. Downloading file takes a while. \n"
									+ "Remember if you press 'Cancel' button, this option wont do anything.\n",
							"File Download Confirm",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE);

			if (result == 0) {
				doSpectra = 1;
				GETOUTPUTCommand getOutputCommand = new GETOUTPUTCommand(this);
				getOutputCommand.getArguments().put("job", job);
				statusChanged(new StatusEvent(getOutputCommand, Status.START));
				System.out.println("Downloaded output file. Launching Molden");
			}
		} catch (Exception ig) {
			System.err.println("error in thread sleep");
		}
	}

	public void doViewSpectraPlot(final JobBean job) {

		String time = new SimpleDateFormat("yyMMdd").format(job.getCreated());

		Settings.jobDir = Settings.defaultDirStr + File.separator
				+ job.getExperimentName() + File.separator + job.getName()
				+ "." + job.getHostName() + "." + job.getLocalId() + "." + time;
		jobName = job.getName();
		dataFileName = Settings.jobDir + File.separator + job.getName()
				+ ".out";

		String jobDir = Settings.jobDir;
		String goutFileName = jobName + ".out";
		String goutFileNameWithPath = jobDir + File.separator
				+ getSelectedJob().getName() + ".out";
		String delete1 = Env.getApplicationDataDir() + File.separator
				+ "vibrational_analysis" + File.separator + "*.out";
		delete1 = '"' + delete1 + '"';

		System.out.println(delete1);
		System.out.println(goutFileNameWithPath);

		String delete2 = Env.getApplicationDataDir() + File.separator
				+ "vibrational_analysis" + File.separator + "*.wrl";
		delete2 = '"' + delete2 + '"';

		String osName = System.getProperty("os.name");
		if (osName.startsWith("Windows")) {
			try {
				Runtime.getRuntime().exec("cmd.exe /c del " + delete1);
				Runtime.getRuntime().exec("cmd.exe /c del " + delete2);
			} catch (Exception e) {
				System.out
						.println("problem in deleting files inside vibrational_analysis folder");
			}
		}

		File gFile = new File(goutFileNameWithPath);
		File irFile = new File(jobDir + File.separator + "IR.plt");

		boolean isOKtoGO = true;
		if (gFile.exists()) {

			if (!irFile.exists()) { // assume that existence of IR.plt means
									// after gaussian output parser
				SpectraOutputParser gp = new SpectraOutputParser(
						Settings.jobDir, goutFileName);

				boolean iflag = gp.makePlotFiles();
				if (!iflag) {
					System.out
							.println("No frequency information in output file");

					// String helpString =
					// "No frequency information in output file" ;
					// JOptionPane.showMessageDialog(null,helpString);

					isOKtoGO = false;
				}

			}

			if (isOKtoGO == true) {

				SpectraViewer sv = new SpectraViewer(getSelectedJob());

			}

		} else {
			String helpString = "Please download the output file first with Monitor Output button";
			JOptionPane.showMessageDialog(null, helpString);
		}

	}

	public class moldenClass extends Thread {
		public void run() {
			viewMoldenJob(getSelectedJob());
		}
	}

	// VMD is launched as a separate thread....Kailash Kotwani
	public class VMDClass {
		Timer timer;

		public VMDClass(int milliseconds) {
			timer = new Timer();
			timer.schedule(new RemindTask(), milliseconds * 1);
		}

		class RemindTask extends TimerTask {
			public void run() {
				viewVMDJob(getSelectedJob());
				System.out.println("Time's up!");
				timer.cancel(); // Terminate the timer thread
			}
		}

	}

	// JMol Editor is launched as a separate thread....Kailash Kotwani
	public class JMolClass {
		Timer timer;

		public JMolClass(int milliseconds) {
			timer = new Timer();
			timer.schedule(new RemindTask(), milliseconds * 1);
		}

		class RemindTask extends TimerTask {
			public void run() {
				viewJMolJob(getSelectedJob());
				System.out.println("Time's up!");
				timer.cancel(); // Terminate the timer thread
			}
		}

	}

	// JMOL is launched as a separate thread....
	public class JMOLJobClass {
		Timer timer;

		public JMOLJobClass(int milliseconds) {
			timer = new Timer();
			timer.schedule(new RemindTask(), milliseconds * 1);
		}

		class RemindTask extends TimerTask {
			public void run() {
				viewJMOLJob(getSelectedJob());
				System.out.println("Time's up!");
				timer.cancel(); // Terminate the timer thread
			}
		}
	}

	// Run Abaqus CAE editor in a separate thread
	public class AbaqusCAEClass implements Runnable {

		public void run() {
			viewAbaqusCAEJob(getSelectedJob());
		}

	}

	// Method below launches molden for all three OS. It confirms whether output
	// file
	// ...file exist locally, if not downloads first. For first time user
	// provides file browser
	// ...for locating molden locally. This also confirms whether X-server is
	// running locally in case of Windows.....Kailash Kotwani
	public void viewMoldenJob(final JobBean job) {
		String molden = "";
		String molden_mac_linux = "";
		moldenPathFileLoc = Env.getApplicationDataDir() + File.separator
				+ "moldenPathFile.inp";
		System.out.println("moldenpathfile: " + moldenPathFileLoc);
		File moldenF = new File(moldenPathFileLoc);
		try {

			if (!moldenF.exists()) {
				int result = JOptionPane
						.showConfirmDialog(
								null,
								"Unknown location of the MOLDEN executable on the local machine!\n"
										+ "Press 'OK' to open a File Browser;\n"
										+ "select the Molden executable (binary file).\n"
										+ "Molden can be downloaded from\n"
										+ "'http://www.cmbi.ru.nl/molden/molden.html'",
								"Molden Path Locator",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (result == 0) {
					saveMoldenPath();
					System.out.println("pressed OK");
				}
				if (moldenF.exists()) {
					FileReader moldenfile = new FileReader(moldenF);

					BufferedReader br = new BufferedReader(moldenfile);
					molden = br.readLine();
					molden_mac_linux = molden;
				}
			} else {
				FileReader moldenfile = new FileReader(moldenF);

				BufferedReader br = new BufferedReader(moldenfile);
				molden = br.readLine();
				molden_mac_linux = molden;
			}
		} catch (IOException ie) {
			System.err.println("Error reading Molden location file");
		}

		String time = new SimpleDateFormat("yyMMdd").format(job.getCreated());
		// edited nik changed system to host
		Settings.jobDir = Settings.defaultDirStr + File.separator
				+ job.getExperimentName() + File.separator + job.getName()
				+ "." + job.getHostName() + "." + job.getLocalId() + "." + time;
		jobName = job.getName();
		dataFileName = Settings.jobDir + File.separator + job.getName()
				+ ".out";
		String dataFileString = '"' + dataFileName + '"';
		File dataFile = new File(dataFileName);
		// String molden = Env.getApplicationDataDir() + File.separator +
		// "molden"+File.separator+"molden.exe";
		// String molden_mac_linux = Env.getApplicationDataDir() +
		// File.separator + "molden"+File.separator+"molden";
		String moldenString = '"' + molden + '"';
		File moldenfile = new File(molden);

		if (dataFile.exists()) {
			if (moldenfile.exists()) {
				String osName = System.getProperty("os.name");
				try {

					if (osName.startsWith("Windows")) {
						String windowsCommandStr = "cmd.exe /c " + moldenString
								+ " molden.out";
						System.out
								.println("datafilestring:  " + dataFileString);
						Runtime.getRuntime().exec(
								"cmd.exe /c copy " + dataFileString
										+ " molden.out");

						// Runtime.getRuntime().exec("cmd.exe /c cd "+moldenFolder);
						System.out.println("Windows command str: "
								+ windowsCommandStr);
						InputStream in = Runtime
								.getRuntime()
								.exec("cmd.exe /c " + moldenString
										+ " molden.out").getInputStream();

						StringBuffer sb = new StringBuffer();
						// System.out.println("Executed ipconfig on windows");

						int c;
						while ((c = in.read()) != -1) {
							sb.append((char) c);
						}

						String comOutput = sb.toString();

						System.out
								.println("value of output form command prompt after running molden is: "
										+ comOutput);
						if (comOutput.equals("")) {
							JOptionPane
									.showMessageDialog(
											null,
											"Perhaps X-server (e.g. Exceed) on this machine is not running.\n"
													+ "If this is the case, please go ahead and start it first and then try again.",
											"X Server Problem!!",
											JOptionPane.WARNING_MESSAGE);
						}
						// Runtime.getRuntime().exec(windowsCommandStr);
					} else if (osName.startsWith("Mac")) {
						// copying output file to a file with common name
						// molden.out. This file is deleted when client is
						// closed.
						Runtime.getRuntime().exec(
								"cp " + dataFileName + " ./molden.out");

						// exporting display for bash shell, for running xterm
						// Runtime.getRuntime().exec("export DISPLAY=:0.0");

						// launching molden output file using molden binary
						// executable
						System.out.println("/usr/X11R6/bin/xterm -e "
								+ molden_mac_linux + " molden.out");
						InputStream in = Runtime
								.getRuntime()
								.exec("/usr/X11R6/bin/xterm -e "
										+ molden_mac_linux + " molden.out")
								.getInputStream();

						// following peice of code prompts user to run x-server
						// before launching molden
						StringBuffer sb = new StringBuffer();
						// System.out.println("Executed ipconfig on windows");

						int c;
						while ((c = in.read()) != -1) {
							sb.append((char) c);
						}

						String comOutput = sb.toString();

						System.out
								.println("value of output form command prompt after running molden is: "
										+ comOutput);
						if (comOutput.equals("")) {
							JOptionPane
									.showMessageDialog(
											null,
											"Perhaps X-server (e.g. Exceed) on this machine is not running.\n"
													+ "If this is the case, please go ahead and start it first and then try again.",
											"X Server Problem!!",
											JOptionPane.WARNING_MESSAGE);
						}

					} else {
						Runtime.getRuntime().exec(
								"cp " + dataFileName + " ./molden.out");
						System.out.println("cp " + dataFileName
								+ " ./molden.out");
						Runtime.getRuntime().exec(
								"xterm -e " + molden_mac_linux + " molden.out");
						System.out.println("xterm -e " + molden_mac_linux
								+ " molden.out");
					}

				} catch (Exception e) {
					System.out.println("Problem running Molden");
				}
			} else {
				JOptionPane
						.showMessageDialog(
								null,
								"Cannot find the MOLDEN executable on the local machine!\n"
										+ "Perhaps its location has changed recently.\n"
										+ "Please verify the location and try again.",
								"Problem reading file",
								JOptionPane.WARNING_MESSAGE);
				moldenF.delete();
				System.out.println("Cannot find the Molden executable");
			}
		} else {
			try {
				System.out
						.println("Output File does not exist on local machine; downloading it...");
				int result = JOptionPane
						.showConfirmDialog(
								null,
								"Output File does not exist on local machine.\n"
										+ "Please press 'OK' to download from server first. Downloading file takes a while. \n"
										+ "Remember if you press 'Cancel' button, this option wont do anything.\n",
								"File Download Confirm",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (result == 0) {
					doMolden = 1;
					GETOUTPUTCommand getOutputCommand = new GETOUTPUTCommand(
							this);
					getOutputCommand.getArguments().put("job", job);

					String sPath = job.getExperimentName() + File.separator
							+ job.getName() + "." + job.getHostName() + "."
							+ job.getLocalId() + "." + time + File.separator
							+ job.getName() + ".out";
					getOutputCommand.getArguments().put("host",
							"mss.ncsa.uiuc.edu");
					getOutputCommand.getArguments().put("path", sPath);
					getOutputCommand.getArguments().put("localFile",
							Settings.defaultDirStr + File.separator + sPath);

					statusChanged(new StatusEvent(getOutputCommand,
							Status.START));
					System.out
							.println("Downloaded output file. Launching Molden");
				}
			} catch (Exception ig) {
				System.err.println("error in thread sleep");
			}
		}
	}

	/*
	 * Method below launches JMOL (not JMolEditor) for all three OS for Gamess,
	 * Gaussian, and NWChem files. It confirms whether output file ...file exist
	 * locally, if not downloads first. Then, it tris to run JMOL based on saved
	 * path information. If the path was not saved before, the file keeping the
	 * path will be created after asking a user for the path
	 */

	public void saveJMOLPath() {
		JFileChooser chooser = new JFileChooser();

		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.CANCEL_OPTION)
			return;
		try {
			File file = chooser.getSelectedFile();
			JMOLPath = file.getAbsolutePath();
			System.out.println("this is path of JMOL file: " + JMOLPath);

			FileWriter JMOLfile = new FileWriter(JMOLPathFileLoc);
			JMOLfile.write(JMOLPath);
			JMOLfile.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Reading error",
					"Problem reading a file for the path of JMOL file",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	public void checkJMOLPath(String JMOLPathFileLoc) {

		File JMOLF = new File(JMOLPathFileLoc);
		String path = null;
		if (JMOLF.exists()) {
			try {
				FileReader JMOLfile = new FileReader(JMOLF);
				BufferedReader br = new BufferedReader(JMOLfile);
				path = br.readLine();
			} catch (IOException e) {
				System.out.println("Exception when JMOLPathfile is opened :"
						+ e);
			}

		}
		if (!path.substring(path.lastIndexOf(File.separator) + 1)
				.equals("jmol")) {
			JMOLF.delete();
		}

	}

	public void viewJMOLJob(final JobBean job) {

		String time = new SimpleDateFormat("yyMMdd").format(job.getCreated());

		Settings.jobDir = Settings.defaultDirStr + File.separator
				+ job.getExperimentName() + File.separator + job.getName()
				+ "." + job.getSystemName() + "." + job.getLocalId() + "."
				+ time;
		jobName = job.getName();
		dataFileName = Settings.jobDir + File.separator + job.getName()
				+ ".out";
		File dataFile = new File(dataFileName);

		if (!dataFile.exists()) {
			try {
				System.out
						.println("Output File does not exist on local machine; downloading it...");
				int result = JOptionPane
						.showConfirmDialog(
								null,
								"Output File does not exist on local machine.\n"
										+ "Please press 'OK' to download from server first. Downloading file takes a while. \n"
										+ "Remember if you press 'Cancel' button, this option won't do anything.\n",
								"File Download Confirm",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (result == 0) {
					doJMOL = 1;
					GETOUTPUTCommand getOutputCommand = new GETOUTPUTCommand(
							this);
					getOutputCommand.getArguments().put("job", job);
					statusChanged(new StatusEvent(getOutputCommand,
							Status.START));
					System.out
							.println("Downloaded output file now, and then try JMOL again");
				}
			} catch (Exception ig) {
				System.err.println("error in thread sleep");
			}
		} else {
			String JMOL = null;
			String JMOL_mac_linux = null;
			JMOLPathFileLoc = Env.getApplicationDataDir() + File.separator
					+ "JMOLPathFile.inp";
			System.out.println("JMOLpathfile: " + JMOLPathFileLoc);
			File JMOLF = new File(JMOLPathFileLoc);

			try {
				checkJMOLPath(JMOLPathFileLoc);
				if (!JMOLF.exists()) {
					int result = JOptionPane
							.showConfirmDialog(
									null,
									"Unknown location of the JMOL executable on the local machine!\n"
											+ "Press 'OK' to open a File Browser;\n"
											+ "select the JMOL executable (for example, in case of JMOL v11, select jmol in JMOL_HOME ).\n"
											+ "If you don't have JMOL, it can be downloaded from\n"
											+ "http://jmol.sourceforge.net",
									"JMOL Path Locator",
									JOptionPane.OK_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE);
					if (result == 0) {
						saveJMOLPath();
						System.out.println("pressed OK");
					}
				}

				if (JMOLF.exists()) {
					FileReader JMOLfile = new FileReader(JMOLF);
					BufferedReader br = new BufferedReader(JMOLfile);
					JMOL = br.readLine();
					JMOL_mac_linux = JMOL;
				}

			} catch (IOException iex) {
				System.err.println("Error reading JMOL location file");
			}

			if (JMOLF.exists()) {

				String osName = System.getProperty("os.name");
				String JMOL_HOME = JMOL.substring(0,
						JMOL.lastIndexOf(File.separator));
				System.out.println("\n\n\nJMOL_HOME : " + JMOL_HOME);
				File JMOLDir = new File(JMOL_HOME);

				try {
					String cmd = null;
					if (osName.startsWith("Windows")) {
						cmd = "cmd.exe /c " + JMOL + " \"" + dataFileName
								+ "\"";
					} else {
						// assume unix shell-like environment
						cmd = JMOL_mac_linux + " " + dataFileName;
					}

					System.out.println(cmd);
					try {
						Runtime.getRuntime().exec(cmd, null, JMOLDir);
					} catch (Exception ex) {
						System.out
								.println("exception when running JMOL is attempted, exception is: "
										+ ex);
					}

				} catch (Exception e) {
					System.out.println("Problem running JMOL");
				}
			}

		}
	}

	public void viewAbaqusCAEJob(final JobBean job) {

		String abaqusExec = "";
		AbaqusCAEPathLoc = Env.getApplicationDataDir() + File.separator
				+ "AbaqusCAEPath.inp";
		System.out.println("AbaqusCAEPath.inp: " + AbaqusCAEPathLoc);
		File abaqusCAEFile = new File(AbaqusCAEPathLoc);

		try {
			if (!abaqusCAEFile.exists()) {
				int result = JOptionPane
						.showConfirmDialog(
								null,
								"Unknown location of the Abaqus CAE executable on the local machine!\n"
										+ "Press 'OK' to open a File Browser;\n"
										+ "select the Molden executable (binary file).\n"
										+ "Molden can be downloaded from\n"
										+ "'http://www.cmbi.ru.nl/molden/molden.html'",
								"Molden Path Locator",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (result == 0) {
					saveAbaqusPath();
				}

				if (abaqusCAEFile.exists()) {
					FileReader abaqusCAEFileReader = new FileReader(
							abaqusCAEFile);

					BufferedReader br = new BufferedReader(abaqusCAEFileReader);
					abaqusExec = br.readLine();
				}
			} else {
				FileReader abaqusCAEFileReader = new FileReader(abaqusCAEFile);

				BufferedReader br = new BufferedReader(abaqusCAEFileReader);
				abaqusExec = br.readLine();
			}
		} catch (IOException ie) {
			System.err.println("Error reading Abaqus location file");
		}

		String time = new SimpleDateFormat("yyMMdd").format(job.getCreated());
		String sPath = job.getExperimentName() + File.separator + job.getName()
				+ "." + job.getHostName() + "." + job.getLocalId() + "." + time
				+ File.separator + job.getName() + ".odb";
		String dataFilePath = Settings.defaultDirStr + File.separator + sPath;
		File dataFile = new File(dataFilePath);
		System.out.println(dataFilePath);
		if (!dataFile.exists()) {
			try {
				System.out
						.println("Output File does not exist on local machine; downloading it...");
				int result = JOptionPane
						.showConfirmDialog(
								null,
								"Output File does not exist on local machine.\n"
										+ "Please press 'OK' to download from server first. Downloading file takes a while. \n"
										+ "Remember if you press 'Cancel' button, this option won't do anything.\n",
								"File Download Confirm",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (result == 0) {
					doAbaqus = 1;
					GETOUTPUTCommand getOutputCommand = new GETOUTPUTCommand(
							this);
					getOutputCommand.getArguments().put("job", job);

					getOutputCommand.getArguments().put("host",
							"mss.ncsa.uiuc.edu");
					getOutputCommand.getArguments().put("path",
							sPath.replace('\\', '/'));
					getOutputCommand.getArguments().put("localFile",
							dataFilePath);

					statusChanged(new StatusEvent(getOutputCommand,
							Status.START));
					System.out
							.println("Downloaded output file. Launching Abaqus CAE");
				}
			} catch (Exception ig) {
				System.err.println("error in thread sleep");
			}
		} else {
			String osName = System.getProperty("os.name");

			try {
				if (osName.startsWith("Windows")) {
					String command = abaqusExec + " cae " + "odb="
							+ dataFilePath;
					System.out.println("Command is :" + command);
					Process p = Runtime.getRuntime().exec(command);
				}
			} catch (Exception e) {
				System.out.println("Problem running Abaqus CAE");
			}
		}
	}

	// Method below launches JMolEditor for all three OS for Gamess and Gaussian
	// files. It confirms whether output file
	// ...file exist locally, if not downloads first.....Kailash Kotwani

	public void viewJMolJob(final JobBean job) {

		String time = new SimpleDateFormat("yyMMdd").format(job.getCreated());

		Settings.jobDir = Settings.defaultDirStr + File.separator
				+ job.getExperimentName() + File.separator + job.getName()
				+ "." + job.getSystemName() + "." + job.getLocalId() + "."
				+ time;
		jobName = job.getName();
		dataFileName = Settings.jobDir + File.separator + job.getName()
				+ ".out";
		String dataFileString = '"' + dataFileName + '"';
		File dataFile = new File(dataFileName);

		if (dataFile.exists()) {
			if (frame == null) {
				frame = new JEditorFrame();
				frame.setDefaultHelper();
				frame.setSize(640, 480);
				JMenuBar menuBar = frame.getJMenuBar();
				menuBar.remove(4);
				JToolBar toolBar = frame.getJToolBar();
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				jEditorFrame = 1;
			}
			frame.setVisible(true);
			System.out.println("*********job application= "
					+ job.getSoftwareName());
			if (job.getSoftwareName().equals("GAMESS")) {
				// if outputfile is of gamess
				try {
					// System.out.println("*********job application= "+job.getSoftwareName());
					GamessOutput gamess = new GamessOutput();
					gamess.parseGamessOutputFile(dataFileName);
					MoleculeInterface mol = new Molecule();
					gamess.getMolecularInterface(mol);
					Molecule.guessCovalentBonds(mol);
					Molecule.guessAtomTypes(mol, AtomInterface.CCT_ATOM_TYPE,
							CCTAtomTypes.getElementMapping());
					frame.setMolecule(mol);

				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					/*
					 * JOptionPane.showMessageDialog(new
					 * Frame(),"Error Loading GAMESS Output file : " +
					 * ex.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
					 */
					return;
				}
			} else if (job.getSoftwareName().equals("Gaussian")) {
				// A sample code to parse Gaussian output file:

				System.out.println("*********job application= "
						+ job.getSoftwareName());
				MoleculeInterface m = Molecule.getNewInstance();
				ParseGaussianOutput parseGaussianOutput = new ParseGaussianOutput();

				HashMap results = (HashMap) parseGaussianOutput
						.parseGaussianOutputFile(m, dataFileName, false);

				m = (MoleculeInterface) results.get("molecule");
				ArrayList Geometries = (ArrayList) results.get("geometries");
				if (Geometries == null || Geometries.size() == 0) {
					JOptionPane.showMessageDialog(null,
							"No molecule found in file", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// --- To show a summary of the Gaussian calculation

				JShowText showResume = new JShowText(new Frame(),
						"Gaussian Results", false);
				showResume.setSize(600, 640);
				showResume.setTitle("Gaussian Results: " + dataFileName);
				showResume.setLocationByPlatform(true);
				showResume.setText(parseGaussianOutput.getOutputResume());
				showResume.setVisible(true);

				// --- in the case of geometry optimization there are several
				// geometries
				if (Geometries.size() > 1) {
					JChoiceDialog selectG = new JChoiceDialog(new Frame(),
							"Select Structure", true);
					for (int i = 0; i < Geometries.size(); i++) {
						MolecularGeometry gm = (MolecularGeometry) Geometries
								.get(i);
						selectG.addItem(gm.getName());
					}
					selectG.selectIndex(Geometries.size() - 1);
					selectG.pack();

					// selectG.setLocationRelativeTo(parent);
					selectG.setVisible(true);
					if (selectG.isApproveOption()) {
						int n = selectG.getSelectedIndex();
						if (n != -1) {
							setupSelectedGeometry(m, n, Geometries);
							Molecule.guessCovalentBonds(m);
							Molecule.guessAtomTypes(m,
									AtomInterface.CCT_ATOM_TYPE,
									CCTAtomTypes.getElementMapping());
							frame.getJ3DUniverse().addMolecule(m);
						}

					} else if (Geometries.size() == 1) {
						// geometrySelection.setEnabled(false); deactivate
						// geometry selection menu
						setupSelectedGeometry(m, 0, Geometries);
						Molecule.guessCovalentBonds(m);
						Molecule.guessAtomTypes(m, AtomInterface.CCT_ATOM_TYPE,
								CCTAtomTypes.getElementMapping());
						frame.getJ3DUniverse().addMolecule(m);
					} else {
						JOptionPane.showMessageDialog(null,
								"Didn't find geometries in output file",
								"Warning", JOptionPane.WARNING_MESSAGE);
					}

				}

				frame.setMolecule(m);
			}

		} else {
			try {
				System.out
						.println("Output File does not exist on local machine; downloading it...");
				int result = JOptionPane
						.showConfirmDialog(
								null,
								"Output File does not exist on local machine.\n"
										+ "Please press 'OK' to download from server first. Downloading file takes a while. \n"
										+ "Remember if you press 'Cancel' button, this option wont do anything.\n",
								"File Download Confirm",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (result == 0) {
					doJMol = 1;
					GETOUTPUTCommand getOutputCommand = new GETOUTPUTCommand(
							this);
					getOutputCommand.getArguments().put("job", job);
					statusChanged(new StatusEvent(getOutputCommand,
							Status.START));
					System.out
							.println("Downloaded output file. Launching Molden");
				}
			} catch (Exception ig) {
				System.err.println("error in thread sleep");
			}
		}

	}

	public int setupSelectedGeometry(MoleculeInterface molec, int n,
			ArrayList Geometries) {
		MolecularGeometry geom = (MolecularGeometry) Geometries.get(n);

		for (int i = 0; i < molec.getNumberOfAtoms(); i++) {
			vPoint3f point = geom.getCoordinates(i);
			AtomInterface atom = molec.getAtomInterface(i);
			atom.setXYZ(point);
		}
		// selectedGeometry = n;
		return n;
	}

	// Method below launches VMD for all three OS. It confirms whether output
	// file
	// ...file exist locally, if not downloads first. For first time user is
	// provided with file browser
	// ...for locating VMD locally. ..Kailash Kotwani
	public void viewVMDJob(final JobBean job) {
		String VMD = "";
		String VMD_mac_linux = "";
		VMDPathFileLoc = Env.getApplicationDataDir() + File.separator
				+ "VMDPathFile.inp";
		System.out.println("VMDpathfile: " + VMDPathFileLoc);
		File VMDF = new File(VMDPathFileLoc);
		try {

			if (!VMDF.exists()) {
				int result = JOptionPane.showConfirmDialog(null,
						"Unknown location of the VMD executable on the local machine!\n"
								+ "Press 'OK' to open a File Browser;\n"
								+ "select the VMD executable (binary file).\n"
								+ "VMD can be downloaded from\n"
								+ "'http://www.ks.uiuc.edu/Research/vmd/'",
						"VMD Path Locator", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (result == 0) {
					saveVMDPath();
					System.out.println("pressed OK");
				}
				if (VMDF.exists()) {
					FileReader VMDfile = new FileReader(VMDF);

					BufferedReader br = new BufferedReader(VMDfile);
					VMD = br.readLine();
					VMD_mac_linux = VMD;
				}
			} else {
				FileReader VMDfile = new FileReader(VMDF);

				BufferedReader br = new BufferedReader(VMDfile);
				VMD = br.readLine();
				VMD_mac_linux = VMD;
			}
		} catch (IOException ie) {
			System.err.println("Error reading VMD location file");
		}

		String time = new SimpleDateFormat("yyMMdd").format(job.getCreated());

		Settings.jobDir = Settings.defaultDirStr + File.separator
				+ job.getExperimentName() + File.separator + job.getName()
				+ "." + job.getSystemName() + "." + job.getLocalId() + "."
				+ time;
		jobName = job.getName();
		dataFileName = Settings.jobDir + File.separator + job.getName()
				+ ".out";
		String dataFileString = '"' + dataFileName + '"';
		File dataFile = new File(dataFileName);
		// String molden = Env.getApplicationDataDir() + File.separator +
		// "molden"+File.separator+"molden.exe";
		// String molden_mac_linux = Env.getApplicationDataDir() +
		// File.separator + "molden"+File.separator+"molden";
		String VMDString = '"' + VMD + '"';

		File VMDfile = new File(VMD);

		if (dataFile.exists()) {
			if (VMDfile.exists()) {
				String osName = System.getProperty("os.name");
				try {

					if (osName.startsWith("Windows")) {

						// String windowsCommandStr = "cmd.exe /c "+VMDString +
						// " " + dataFileString;
						String windowsCommandStr = "cmd.exe /c " + VMDString
								+ " vmd.out";
						System.out
								.println("datafilestring:  " + dataFileString);
						Runtime.getRuntime().exec(
								"cmd.exe /c copy " + dataFileString
										+ " vmd.out");

						// Runtime.getRuntime().exec("cmd.exe /c cd "+moldenFolder);
						System.out.println("Windows command str: "
								+ windowsCommandStr);
						InputStream in = Runtime.getRuntime()
								.exec(windowsCommandStr).getInputStream();

						StringBuffer sb = new StringBuffer();
						// System.out.println("Executed ipconfig on windows");

						int c;
						while ((c = in.read()) != -1) {
							sb.append((char) c);
						}

						String comOutput = sb.toString();

						System.out
								.println("value of output form command prompt after running VMD is: "
										+ comOutput);
						/*
						 * if (comOutput.equals("")){
						 * JOptionPane.showMessageDialog(null,
						 * "Perhaps X-server (e.g. Exceed) on this machine is not running.\n"
						 * +
						 * "If this is the case, please go ahead and start it first and then try again."
						 * , "X Server Problem!!", JOptionPane.WARNING_MESSAGE);
						 * }
						 */
						// Runtime.getRuntime().exec(windowsCommandStr);
					} else if (osName.startsWith("Mac")) {
						// copying output file to a file with common name
						// molden.out. This file is deleted when client is
						// closed.
						// Runtime.getRuntime().exec("cp "+dataFileName+" ./molden.out");
						// exporting display for bash shell, for running xterm
						Runtime.getRuntime().exec("export DISPLAY=:0.0");

						// launching molden output file using molden binary
						// executable
						System.out.println("/usr/X11R6/bin/xterm -e "
								+ VMD_mac_linux + " " + dataFileName);
						InputStream in = Runtime
								.getRuntime()
								.exec("/usr/X11R6/bin/xterm -e "
										+ VMD_mac_linux + " " + dataFileName)
								.getInputStream();

						// following peice of code prompts user to run x-server
						// before launching molden
						StringBuffer sb = new StringBuffer();
						// System.out.println("Executed ipconfig on windows");

						int c;
						while ((c = in.read()) != -1) {
							sb.append((char) c);
						}

						String comOutput = sb.toString();

						System.out
								.println("value of output form command prompt after running VMD is: "
										+ comOutput);
						/*
						 * if (comOutput.equals("")){
						 * JOptionPane.showMessageDialog(null,
						 * "Perhaps X-server (e.g. Exceed) on this machine is not running.\n"
						 * +
						 * "If this is the case, please go ahead and start it first and then try again."
						 * , "X Server Problem!!", JOptionPane.WARNING_MESSAGE);
						 * }
						 */

					} else {
						// Runtime.getRuntime().exec("cp "+dataFileName+" ./molden.out");
						System.out.println("cp " + dataFileName + " "
								+ dataFileName);
						Runtime.getRuntime().exec(
								"xterm -e " + VMD_mac_linux + " "
										+ dataFileName);
						System.out.println("xterm -e " + VMD_mac_linux + " "
								+ dataFileName);
					}

				} catch (Exception e) {
					System.out.println("Problem running VMD");
				}
			} else {
				JOptionPane
						.showMessageDialog(
								null,
								"Cannot find the VMD executable on the local machine!\n"
										+ "Perhaps its location has changed recently.\n"
										+ "Please verify the location and try again.",
								"Problem reading file",
								JOptionPane.WARNING_MESSAGE);
				VMDF.delete();
				System.out.println("Cannot find the VMD executable");
			}
		} else {
			try {
				System.out
						.println("Output File does not exist on local machine; downloading it...");
				int result = JOptionPane
						.showConfirmDialog(
								null,
								"Output File does not exist on local machine.\n"
										+ "Please press 'OK' to download from server first. Downloading file takes a while. \n"
										+ "Remember if you press 'Cancel' button, this option wont do anything.\n",
								"File Download Confirm",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (result == 0) {
					doVMD = 1;
					GETOUTPUTCommand getOutputCommand = new GETOUTPUTCommand(
							this);
					getOutputCommand.getArguments().put("job", job);
					statusChanged(new StatusEvent(getOutputCommand,
							Status.START));
					System.out.println("Downloaded output file. Launching VMD");
				}
			} catch (Exception ig) {
				System.err.println("error in thread sleep");
			}
		}
	}

	public void saveVMDPath() {
		JFileChooser chooser = new JFileChooser();

		int result = chooser.showOpenDialog(this);
		String inp = new String();
		if (result == JFileChooser.CANCEL_OPTION)
			return;
		try {
			File file = chooser.getSelectedFile();
			VMDPath = file.getAbsolutePath();
			System.out.println("this is path of VMD file: " + VMDPath);

			FileWriter VMDfile = new FileWriter(VMDPathFileLoc);
			VMDfile.write(VMDPath);
			VMDfile.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Reading error",
					"Problem reading file", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	public void saveMoldenPath() {
		JFileChooser chooser = new JFileChooser();

		int result = chooser.showOpenDialog(this);
		String inp = new String();
		if (result == JFileChooser.CANCEL_OPTION)
			return;
		try {
			File file = chooser.getSelectedFile();
			moldenPath = file.getAbsolutePath();
			System.out.println("this is path of molden file: " + moldenPath);

			FileWriter moldenfile = new FileWriter(moldenPathFileLoc);
			moldenfile.write(moldenPath);
			moldenfile.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Reading error",
					"Problem reading file", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	public void saveAbaqusPath() {
		JFileChooser chooser = new JFileChooser();

		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.CANCEL_OPTION)
			return;
		try {
			File file = chooser.getSelectedFile();
			String abaqusCAEPath = file.getAbsolutePath();
			System.out.println("this is path of Abaqus CAE file: "
					+ abaqusCAEPath);

			File abaqusFile = new File(AbaqusCAEPathLoc);
			FileWriter abaqusCAEWriter = new FileWriter(abaqusFile);
			abaqusCAEWriter.write(abaqusCAEPath);
			abaqusCAEWriter.close();

		} catch (Exception e) {
			e.printStackTrace();
			// JOptionPane.showMessageDialog(null, "Reading error",
			// "Problem reading file", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	private void doVisualizeJob(final JobBean job) {

		String time = new SimpleDateFormat("yyMMdd").format(job.getCreated());

		Settings.jobDir = Settings.defaultDirStr + File.separator
				+ job.getExperimentName() + File.separator
				+
				// replaced job.getSystemName with job.getHostName
				job.getName() + "." + job.getHostName() + "."
				+ job.getLocalId() + "." + time;
		System.out.println(" Inside doVisualizeJob " + Settings.jobDir);
		// Writing qcrjm.conf file to job dir folder
		try {

			// make sure jobdir is there before writing the file.
			File jobDir = new File(Settings.jobDir);

			jobDir.mkdirs();

			jobName = job.getName();

			dataFileName = Settings.jobDir + File.separator + job.getName()
					+ ".out";

			String qcrjmConf = Settings.jobDir + File.separator + "qcrjm.conf";

			FileWriter fw = new FileWriter(qcrjmConf);

			fw.write("qcrjm2002" + "\n");

			fw.write("datafile=" + dataFileName);

			fw.close();

		} catch (IOException e) {
			System.err.println("error in writing qcrjm.conf file");
			e.printStackTrace();
		}

		// GETOUTPUTCommand getOutputCommand = new GETOUTPUTCommand(this);
		//
		// getOutputCommand.getArguments().put("job", job);
		//
		// progressCancelPrompt("Cancel View Output",
		// "Retrieving file(s) from mass storage...",worker);
		//
		// // javax.swing.SwingUtilities.invokeLater(new Runnable() {
		// // public void run() {
		// // createProgressPanel("Retrieving file from mass storage");
		// // }
		// // });
		//
		// try {
		// getOutputCommand.execute();
		// except.printStackTrace();
		// }

		// First Check whether output file already exists on machine

		File outputFile = new File(dataFileName);

		if (outputFile.exists()) {
			int downloadFile = JOptionPane.showConfirmDialog(null,
					"Output file found on local system. Use this file?",
					"Monitor Job Output", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (downloadFile == JOptionPane.YES_OPTION) {
				// dont download file and straightway launching nanocad
				launchNanoCad(outputFile);
				return;
			} else {
				outputFile.delete();
			}
		}

		if (!outputFile.exists()) {
			doNanocad = 1;

			String fileUri = "";
			String time1 = new SimpleDateFormat("yyMMdd").format(job
					.getCreated());

			if (job.getHostName().contains("ember")) {
				if (GridChem.accessType.equals(AccessType.COMMUNITY)) {
					fileUri = "/scratch/users/ccguser/"
							+ Settings.gridchemusername + "/"
							+ job.getExperimentName() + "/" + job.getName()
							+ "." + job.getHostName() + "." + job.getLocalId()
							+ "." + time1 + "/" + job.getName() + ".out";
				} else if (GridChem.accessType.equals(AccessType.TERAGRID)) {
					fileUri = "/scratch/users/" + GridChem.externalUsername
							+ "/" + job.getExperimentName() + "/"
							+ job.getName() + "." + job.getHostName() + "."
							+ job.getLocalId() + "." + time1 + "/"
							+ job.getName() + ".out";
				}
			} else if (job.getHostName().contains("blacklight")) {
				if (GridChem.accessType.equals(AccessType.COMMUNITY)) {
					fileUri = "/brashear/gcommuni/" + Settings.gridchemusername
							+ "/" + job.getExperimentName() + "/"
							+ job.getName() + "." + job.getHostName() + "."
							+ job.getLocalId() + "." + time1 + "/"
							+ job.getName() + ".out";
				} else if (GridChem.accessType.equals(AccessType.TERAGRID)) {
					fileUri = "/brashear/" + GridChem.externalUsername + "/"
							+ job.getExperimentName() + "/" + job.getName()
							+ "." + job.getHostName() + "." + job.getLocalId()
							+ "." + time1 + "/" + job.getName() + ".out";
				}
			} else if (job.getHostName().contains("trestles")) {
				if (GridChem.accessType.equals(AccessType.COMMUNITY)) {
					// fileUri = "/home/gridchem/scratch/"+
					// Settings.gridchemusername +"/"+job.getExperimentName() +
					// "/" + job.getName() + "." + job.getHostName() + "." +
					// job.getLocalId() + "." + time1 + "/" +
					// job.getName()+".out";

					fileUri = "/home/gridchem/" + Settings.gridchemusername
							+ "/" + job.getExperimentName() + "/"
							+ job.getName() + "." + job.getHostName() + "."
							+ job.getLocalId() + "." + time1 + "/"
							+ job.getName() + ".out";
				} else if (GridChem.accessType.equals(AccessType.TERAGRID)) {
					fileUri = "/home/" + GridChem.externalUsername + "/"
							+ job.getExperimentName() + "/" + job.getName()
							+ "." + job.getHostName() + "." + job.getLocalId()
							+ "." + time1 + "/" + job.getName() + ".out";
				}
			} else if (job.getHostName().contains("gordon")) {
				if (GridChem.accessType.equals(AccessType.COMMUNITY)) {
					// fileUri = "/oasis/projects/nsf/uic151/gridchem/"+
					// Settings.gridchemusername +"/"+job.getExperimentName() +
					// "/" + job.getName() + "." + job.getHostName() + "." +
					// job.getLocalId() + "." + time1 + "/" +
					// job.getName()+".out";

					fileUri = "/home/gridchem/" + Settings.gridchemusername
							+ "/" + job.getExperimentName() + "/"
							+ job.getName() + "." + job.getHostName() + "."
							+ job.getLocalId() + "." + time1 + "/"
							+ job.getName() + ".out";

				} else if (GridChem.accessType.equals(AccessType.TERAGRID)) {
					fileUri = "/home/" + GridChem.externalUsername
							+ "/scratch/" + job.getExperimentName() + "/"
							+ job.getName() + "." + job.getHostName() + "."
							+ job.getLocalId() + "." + time1 + "/"
							+ job.getName() + ".out";
				}
			} else if (job.getHostName().contains("stampede")) {
				if (GridChem.accessType.equals(AccessType.COMMUNITY)) {
					fileUri = "/work/00421/ccguser/"
							+ Settings.gridchemusername + "/"
							+ job.getExperimentName() + "/" + job.getName()
							+ "." + job.getHostName() + "." + job.getLocalId()
							+ "." + time1 + "/" + job.getName() + ".out";
				} else if (GridChem.accessType.equals(AccessType.TERAGRID)) {
					fileUri = "/home/" + GridChem.externalUsername
							+ "/scratch/" + job.getExperimentName() + "/"
							+ job.getName() + "." + job.getHostName() + "."
							+ job.getLocalId() + "." + time1 + "/"
							+ job.getName() + ".out";
				}
			}

			System.out.println("###################################" + fileUri);

			GETOUTPUTCommand getOutputCommand = new GETOUTPUTCommand(this);
			getOutputCommand.getArguments().put("path", fileUri);
			if (job.getHostName().contains("ember")) {
				getOutputCommand.getArguments().put("host", job.getHostName());
				System.out.println("###################################"
						+ job.getHostName());
			} else if (job.getHostName().contains("blacklight")) {
				getOutputCommand.getArguments().put("host",
						"blacklight.psc.teragrid.org");
				System.out
						.println("###################################blacklight.psc.teragrid.org");
			} else if (job.getHostName().contains("trestles")) {
				getOutputCommand.getArguments()
						.put("host", "trestles.sdsc.edu");
				System.out
						.println("###################################trestles.sdsc.edu");
			} else if (job.getHostName().contains("gordon")) {
				getOutputCommand.getArguments().put("host", "gordon.sdsc.edu");
				System.out
						.println("###################################gordon.sdsc.edu");
			} else if (job.getHostName().contains("stampede")) {
				getOutputCommand.getArguments().put("host",
						"stampede.tacc.utexas.edu");
				System.out
						.println("###################################stampede.tacc.utexas.edu");
			}
			// getOutputCommand.getArguments().put("jobId", job.getId());
			getOutputCommand.getArguments().put("localFile", dataFileName);
			statusChanged(new StatusEvent(getOutputCommand, Status.START));

		}
	}

	private void doBrowseFiles(JobBean job) {
		try {

			String fileUri = "";

			if (Settings.authenticatedGridChem) {
				fileUri = "/home/ccguser/mss/internal/";
				// fileUri = "internal/";
			} else {
				fileUri = "/home/ccguser/mss/external/";
				// fileUri = "external/";
			}

			// System.out.println("JOB DESC *"+job.getCreated().toString()+"*"+
			// job.getExperimentName()
			// +"*"+ job.getName()+"*"+ job.getSystemName() +"*"+
			// job.getLocalId()+"*\n");

			if (job == null || job.getCreated() == null
					|| job.getExperimentName() == null
					|| job.getExperimentName().equals("")
					|| job.getName() == null || job.getName().equals("")
					|| job.getSystemName() == null
					|| job.getSystemName().equals("")
					|| job.getLocalId() == null || job.getLocalId().equals("")) {
				fileUri = fileUri + Settings.gridchemusername;
				// fileUri = "";
				JOptionPane
						.showMessageDialog(
								null,
								"Unable to determine job directory.\n"
										+ "Redirecting browser to user home \ndirectory.",
								"Browsing Error",
								JOptionPane.INFORMATION_MESSAGE);
				GridChem.appendMessage("Retrieving user home directory\n");
			} else {
				String time = new SimpleDateFormat("yyMMdd").format(job
						.getCreated());
				// fileUri = fileUri + Settings.gridchemusername + "/" +
				fileUri = job.getExperimentName() + "/" + job.getName() + "."
						+ job.getHostName() + "." + job.getLocalId() + "."
						+ time + "/";
				GridChem.appendMessage("Retrieving directory listing for job "
						+ job.getId() + "\n");
			}

			if (Settings.DEBUG)
				System.err.println("Remote URI: " + fileUri);

			// final URI uri = new URI(fileUri);

			// javax.swing.SwingUtilities.invokeLater(new Runnable() {
			// public void run() {
			jobFrame = new JFrame("GridChem File Browser");
			jobFrame.getContentPane().add(new FileBrowserImpl(fileUri));
			jobFrame.pack();
			jobFrame.setVisible(true);
			jobFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			// }
			// });
		} catch (HeadlessException e1) {
			e1.printStackTrace();
			// } catch (URISyntaxException e1) {
			// GridChem.appendMessage("Error opening file browser. " +
			// "Invalid file location.");
			// e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Opens a search dialog box for the user to enter search parameters. The
	 * search dialog will start a JobTask and register this class as a status
	 * listener. When the task completes this class will be notified and the job
	 * table updated as necessary.
	 */
	private void doSearchJobs() {
		if (searchDialog == null) {
			searchDialog = new SearchDialog(this);
		} else {
			searchDialog.setVisible(true);
		}
	}

	/**
	 * Hide the currently selected jobs.
	 * 
	 * @param jobs
	 */
	private void doHideJobs(JobBean[] jobs) {
		HIDECommand command = new HIDECommand(this);
		String jobIDs = jobs[0].getId().toString();
		for (int i = 1; i < jobs.length; i++) {
			jobIDs += ";" + jobs[i].getId();
		}
		command.getArguments().put("jobIDs", jobIDs);
		System.out.println("Hiding job(s) " + jobIDs);
		statusChanged(new StatusEvent(command, Status.START));
	}

	// private void doUnhideJob(JobBean job) {
	// UNHIDECommand command = new UNHIDECommand(this);
	// command.getArguments().put("jobID", job.getId().toString());
	// statusChanged(new StatusEvent(command,Status.START));
	// }

	/**
	 * Unhide all the user's jobs, even those not in the current result set.
	 * 
	 */
	private void doUnhideAllJobs() {
		UNHIDECommand command = new UNHIDECommand(this);
		statusChanged(new StatusEvent(command, Status.START));
	}

	/**
	 * Prompt the user to make sure they mean to delete the given job. If so,
	 * then call the gms to delete the job and refresh the work space.
	 * 
	 * @param job
	 */
	private void doDeleteJobs(JobBean[] jobs) {
		String message = (jobs.length > 1) ? "Delete selected jobs?"
				: "Delete job " + jobs[0].getId();

		int confirmed = JOptionPane.showConfirmDialog(this, message, "",
				JOptionPane.YES_NO_OPTION);

		if (confirmed == 0) {

			// progressBar.setIndeterminate(true);

			DELETECommand command = new DELETECommand(this);
			String jobIDs = jobs[0].getId().toString();
			for (int i = 1; i < jobs.length; i++) {
				jobIDs += ";" + jobs[i].getId();
			}
			command.getArguments().put("jobIDs", jobIDs);
			statusChanged(new StatusEvent(command, Status.START));
		}
	}

	/**
	 * Return the Bean for the selection job. Returns null if no row is
	 * selected.
	 * 
	 * @return the JobBean associated with the selected row
	 */
	public JobBean getSelectedJob() {
		int k = jobTable.getSelectedRow();

		if (k == -1) {
			return null;
		}
		// Long jobID = (Long)
		// ((DefaultTableModel)sorter.getTableModel()).getValueAt(k,0);

		return m_data.getJobAtRow(k);
	}

	/**
	 * Return an array of Bean for the selection job. Returns null if no rows
	 * are selected.
	 * 
	 * @return the JobBean associated with the selected row
	 */
	public JobBean[] getSelectedJobs() {
		JobBean[] jobs;

		int[] k = jobTable.getSelectedRows();

		if (k.length > 0) {
			jobs = new JobBean[k.length];
			for (int i = 0; i < k.length; i++) {
				System.out.println("Job " + m_data.getJobAtRow(k[i]));
				jobs[i] = m_data.getJobAtRow(k[i]);
			}
		} else {
			return null;
		}

		return jobs;
	}

	/**
	 * Returns a string of all the hidden job ids in the current job collection.
	 * The current job collection may be either a search result or the user's
	 * VO. This method is mainly useful for undoing an unhideAll action.
	 * 
	 * @return
	 */
	public String getHiddenJobs() {
		String[] jobIDs = (String[]) hiddenJobs.toArray();
		String idString = jobIDs[0];

		for (int i = 1; i < jobIDs.length; i++) {
			idString += ";" + jobIDs[i];
		}

		return idString;
	}

	public void addHiddenJob(JobBean job) {
		hiddenJobs.add(job.getId().toString());
	}

	/**
	 * Create a progress panel that allows the current task to be killed if the
	 * user clicks on the cancel button.
	 * 
	 * @param title
	 * @param labelText
	 * @param worker
	 */
	private void startWaiting(String title, String labelText, SwingWorker worker) {
		progressCancelPrompt = new CancelCommandPrompt(this, title, labelText,
				-1, worker);

	}

	private void startWaitingForDownload(String message, int blockCount) {
		progressCancelPrompt = new CancelCommandPrompt(this,
				"Starting Download", message, -1, null);

	}

	/**
	 * Updates the value of a progress panel
	 * 
	 * @param message
	 * @param value
	 */
	private void updateProgress(int value) {
		progressCancelPrompt.updateStatus();
	}

	/**
	 * Updates the message of a progress panel
	 * 
	 * @param message
	 * @param value
	 */
	private void updateProgress(String message) {
		progressCancelPrompt.updateStatus(message);
	}

	/**
	 * Updates both the message and value of a progress panel
	 * 
	 * @param message
	 * @param value
	 */
	private void updateProgress(String message, int value) {
		progressCancelPrompt.updateStatus();
		progressCancelPrompt.updateStatus(message);
	}

	/**
	 * Remove the progress panel.
	 */
	private void stopWaiting() {
		if (progressCancelPrompt != null) {
			progressCancelPrompt.finished();
			progressCancelPrompt = null;
		}
	}

	/**
	 * Put date object into yymmdd format
	 * 
	 * @param date
	 * @return
	 */
	public String formatDate(Date date) {
		DateFormat formatter = new SimpleDateFormat("yyMMdd");
		return formatter.format(date);
	}

	/**
	 * Put string date into yymmdd format
	 * 
	 * @param date
	 * @return
	 */
	public String stringToDate(String dateString) {
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		DateFormat formatter2 = new SimpleDateFormat("yyMMdd");
		try {
			return formatter2.format(formatter.parse(dateString));
		} catch (ParseException e) {
		}

		return "unknown";
	}

	public synchronized boolean isUpdatedWithSearchResults() {
		return updatedWithSearchResults;
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			// if (jobTable.getSelectedRow() == -1) {
			// enableButtonActions(false);
			// } else {
			// enableButtonActions(true);
			// }

		}
	}

	private void showPopupMenu(Component component, Point p) {
		int row = jobTable.rowAtPoint(p);

		JobBean job = getSelectedJob();

		// disable kill depending on job status
		if (job.getStatus().equals(JobStatusType.RUNNING)
				|| job.getStatus().equals(JobStatusType.SCHEDULED)
				|| job.getStatus().equals(JobStatusType.MIGRATING)) {
			killMenuItem.setEnabled(true);
			editNotificationsMenuItem.setEnabled(true);
		} else {
			killMenuItem.setEnabled(false);
			editNotificationsMenuItem.setEnabled(false);
		}

		// disable clear search depending on existence of search results
		if (updatedWithSearchResults) {
			clearSearchMenuItem.setEnabled(true);
		} else {
			clearSearchMenuItem.setEnabled(false);
		}

		// option only shows up if there are hidden jobs in the table model
		if (m_data.hasHiddenJobs()) {
			showHiddenMenuItem.setEnabled(true);
		} else {
			showHiddenMenuItem.setEnabled(false);
		}

		rightClickPopup.show(component, p.x, p.y);
	}

	class ColumnListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			TableColumnModel colModel = jobTable.getColumnModel();
			int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
			int modelIndex = colModel.getColumn(columnModelIndex)
					.getModelIndex();

			if (modelIndex < 0)
				return;
			if (m_data.m_sortCol == modelIndex)
				m_data.m_sortAsc = !m_data.m_sortAsc;
			else
				m_data.m_sortCol = modelIndex;

			for (int i = 0; i < m_data.getColumnCount(); i++) {
				TableColumn column = colModel.getColumn(i);
				int index = column.getModelIndex();
				JLabel renderer = (JLabel) column.getHeaderRenderer();
				renderer.setIcon(m_data.getColumnIcon(index));
			}
			jobTable.getTableHeader().repaint();

			m_data.sortData();
			jobTable.tableChanged(new TableModelEvent(m_data));
			jobTable.repaint();
		}
	}

	class ColumnKeeper implements ActionListener {
		protected TableColumn m_column;
		protected ColumnData m_colData;

		public ColumnKeeper(TableColumn column, ColumnData colData) {
			m_column = column;
			m_colData = colData;
		}

		public void actionPerformed(ActionEvent e) {
			JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
			TableColumnModel model = jobTable.getColumnModel();
			if (item.isSelected()) {
				model.addColumn(m_column);
			} else {
				model.removeColumn(m_column);
			}
			jobTable.tableChanged(new TableModelEvent(m_data));
			jobTable.repaint();
		}
	}

	/**
	 * Mouse listener for the job table. If a right click occurs, a popup menu
	 * is shown. If a double click occurs, the job info panel is opened for that
	 * job. Single clicks are ignored.
	 * 
	 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
	 * 
	 */
	class JobMouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			Point p = e.getPoint();
			int row = jobTable.rowAtPoint(p);
			int column = jobTable.columnAtPoint(p);

			if (isRightClickEvent(e)) {
				// make sure the right click selects the underlying row in the
				// table.
				//jobTable.setRowSelectionInterval(row, row);
				//jobTable.setColumnSelectionInterval(0,
				//		jobTable.getColumnCount() - 1);
				showPopupMenu(e.getComponent(), e.getPoint());
			} else if (e.getClickCount() == 2) {
				createJobInfoDialog(m_data.getJobAtRow(row));
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

	/**
	 * Action listener for pop.up menu. Calls the appropriate method for each
	 * button selection.
	 * 
	 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
	 * 
	 */
	protected class PopupListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JMenuItem item = (JMenuItem) event.getSource();

			if (item == getInfoMenuItem) {
				estimateTime(getSelectedJob());
			} else if (item == estTimeMenuItem) {
				System.out.println("This is a just dummy option");
			} else if (item == editNotificationsMenuItem) {
				doSteerJob(getSelectedJob());
			} else if (item == viewOutputMenuItem) {
				doVisualizeJob(getSelectedJob());
			} else if (item == viewMoldenItem) {
				if (!getSelectedJob().getStatus().toString()
						.startsWith("SCHEDULED")) {
					System.out.println("job status: "
							+ getSelectedJob().getStatus().toString());

					new moldenClass().start();
				} else {
					String helpString = "This option not valid for a scheduled job";
					JOptionPane.showMessageDialog(null, helpString);
				}

			} else if (item == viewVMDItem) {
				if (!getSelectedJob().getStatus().toString()
						.startsWith("SCHEDULED")) {
					System.out.println("job status: "
							+ getSelectedJob().getStatus().toString());

					new VMDClass(1);
				} else {
					String helpString = "This option not valid for a scheduled job";
					JOptionPane.showMessageDialog(null, helpString);
				}

			} else if (item == viewJMolItem) {
				if (!getSelectedJob().getStatus().toString()
						.startsWith("SCHEDULED")) {
					System.out.println("job status: "
							+ getSelectedJob().getStatus().toString());
					new JMolClass(1);
					// viewJMolJob(getSelectedJob());
				} else {
					String helpString = "This option not valid for a scheduled job";
					JOptionPane.showMessageDialog(null, helpString);
				}
			} else if (item == viewJMOLItem) {
				if (!getSelectedJob().getStatus().toString()
						.startsWith("SCHEDULED")) {
					System.out.println("job status: "
							+ getSelectedJob().getStatus().toString());
					new JMOLJobClass(1);
				} else {
					String helpString = "This option not valid for a scheduled job";
					JOptionPane.showMessageDialog(null, helpString);
				}
			} else if (item == viewAbaqusCAEItem) {
				if (!getSelectedJob().getStatus().toString()
						.startsWith("SCHEDULED")) {
					System.out.println("job status: "
							+ getSelectedJob().getStatus().toString());
					new AbaqusCAEClass().run();
				} else {
					String helpString = "This option not valid for a scheduled job";
					JOptionPane.showMessageDialog(null, helpString);
				}
			} else if (item == viewSpectraItem) {
				if (!getSelectedJob().getStatus().toString()
						.equals("SCHEDULED")) {
					doViewSpectraPlot(getSelectedJob());
				} else {
					String helpString = "This option not valid for a scheduled job";
					JOptionPane.showMessageDialog(null, helpString);
				}

			} else if (item == browseFilesMenuItem) {
				doBrowseFiles(getSelectedJob());
			} else if (item == clearSearchMenuItem) {
				updatedWithSearchResults = false;
				refreshJobs();
			} else if (item == editTagsMenuItem) {
				createJobMetadataDialog(getSelectedJob());
			} else if (item == resubmitMenuItem) {
				doResubmit(getSelectedJob());
			} else if (item == killMenuItem) {
				doKillJob(getSelectedJob());
			} else if (item == refreshMenuItem) {
				// refreshing the panel does not require a job to be selected
				refreshJobs();
			} else if (item == copyMenuItem) {
				copyJob();
			} else if (item == hideMenuItem) {
				doHideJobs(getSelectedJobs());
			} else if (item == showHiddenMenuItem) {
				doUnhideAllJobs();
			} else if (item == deleteMenuItem) {
				doDeleteJobs(getSelectedJobs());
			}
		}
	}

	/**
	 * Listener for the nanocat window. Not sure why this has to be in here
	 * instead of in the nanocad class, but we'll leave it for now.
	 * 
	 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
	 * 
	 */
	class NanoCadListener implements WindowListener, ComponentListener {
		public void windowOpened(WindowEvent e) {
		}

		public void windowClosing(WindowEvent e) {
			// launching job editor

			System.err.println("load temp file here!");
			String text = "";
			File f = new File(Env.getApplicationDataDir()
					+ Settings.fileSeparator + "tmp.txt");
			if (f.exists()) {
				try {

					BufferedReader inStream = new BufferedReader(
							new FileReader(f));

					String line;
					while ((line = inStream.readLine()) != null) {
						int n = line.length();
						if (n > 0) {
							text = text + line + "\n";
							System.err.println(line);
						}
					}
					inStream.close();

				} catch (IOException ioe) {
					System.err.println("IOException in editJobPanel");
				}
			}
			// updating job editor panel

			// closing nanocad and associated panel
			nanWin.dispose();
			if (nanWin.nano.t != null)
				nanWin.nano.t.setVisible(false);

		}

		public void windowClosed(WindowEvent e) {
		}

		public void windowIconified(WindowEvent e) {
		}

		public void windowDeiconified(WindowEvent e) {
		}

		public void windowActivated(WindowEvent e) {
		}

		public void windowDeactivated(WindowEvent e) {
		}

		public void componentHidden(ComponentEvent e) {

			// launching job editor
			System.err.println("load temp file here!");
			String text = "";

			File f = new File(Env.getApplicationDataDir()
					+ Settings.fileSeparator + "tmp.txt");
			ArrayList<File> iFiles = new ArrayList<File>();
			iFiles.add(f);

			EditJobPanel ejp = new EditJobPanel(iFiles);
			ejp.changeApp(newNanocad.exportedApplication);
			ejp.numProcMethod();
			// if (f.exists()) {
			// try {
			//
			// BufferedReader inStream = new BufferedReader(new FileReader(f));
			//
			// String line;
			// while ((line = inStream.readLine()) != null) {
			// int n = line.length();
			// if (n > 0) {
			// text = text + line + "\n";
			// System.err.println(line);
			// }
			// }
			// inStream.close();
			//
			//
			//
			// }
			// catch (IOException ioe)
			// {
			// System.err.println("IOException in editJobPanel");
			// }
			// }

			// ejp.populateMachineList(newNanocad.exportedApplication);
			// // ejp.populateProjects((String)
			// ejp.apphpcModel.getElementAt(0));

			// closing nanocad and associated panel
			nanWin.dispose();
			if (nanWin.nano.t != null)
				nanWin.nano.t.setVisible(false);

			JOptionPane.showMessageDialog(null, "WARNING: The input"
					+ " appearing here is taken from a template."
					+ "  The molecule information is correct, \nbut"
					+ " make sure to edit the other parts of the" + " text.",
					"GridChem: Job Editor", JOptionPane.INFORMATION_MESSAGE);

		}

		public void componentMoved(ComponentEvent e) {
		}

		public void componentResized(ComponentEvent e) {
		}

		public void componentShown(ComponentEvent e) {
		}

	}

	/**
	 * Utility method to determine if the gui is already performing an action.
	 * 
	 * @return
	 */
	public boolean isInProcess() {
		return inProcess;
	}

	/**
	 * Given a set of jobs that were selected in the table before it refreshed,
	 * find those jobs in the new table and select them again. We don't just
	 * reselect the same interval as before because a job may have been added or
	 * removed between refresh calls.
	 * 
	 * @param selectedJobs
	 */
	private void resetSelectedTableItems(JobBean[] selectedJobs) {
		int i = 0;
		int startSelectedRowInterval = -1;
		int endSelectedRowInterval = -1;

		if (selectedJobs == null || selectedJobs.length <= 0) {
			return;
		}

		System.out.println("There were " + selectedJobs.length
				+ " job(s) selected before the refresh operation began.");

		while (startSelectedRowInterval == -1 && i < selectedJobs.length) {
			startSelectedRowInterval = m_data.getRowOfJob(selectedJobs[i]
					.getId());
			i++;
		}

		if (startSelectedRowInterval == -1) {
			System.out
					.println("None of the selected jobs were found in the updated table.");
			return;
		} else {
			System.out.println("Beginning selection "
					+ startSelectedRowInterval
					+ " was found in the updated table at position " + i);

			endSelectedRowInterval = -1;
			i = selectedJobs.length;

			while (endSelectedRowInterval == -1 && i > startSelectedRowInterval) {
				endSelectedRowInterval = m_data.getRowOfJob(selectedJobs[i]
						.getId());
				i--;
			}

			if (endSelectedRowInterval == -1) {
				System.out
						.println("None of the remaining selected jobs from the old table were found in the updated table.");

				jobTable.setRowSelectionInterval(startSelectedRowInterval,
						startSelectedRowInterval);
				jobTable.setColumnSelectionInterval(0,
						jobTable.getColumnCount() - 1);
			} else {
				System.out.println("Last selection " + endSelectedRowInterval
						+ " was found in the updated table at position " + i);

				jobTable.setRowSelectionInterval(startSelectedRowInterval,
						endSelectedRowInterval);
				jobTable.setColumnSelectionInterval(0,
						jobTable.getColumnCount() - 1);
			}
		}
	}

	public void statusChanged(StatusEvent event) {
		Trace.entry();
		Status status = event.getStatus();
		System.out.println("Status changed to: " + status.name());
		System.out.println("StatusListener is: "
				+ event.getSource().getClass().getName());

		final JobCommand command = (JobCommand) event.getSource();
		System.out.println("stats=" + status.name() + ", type="
				+ command.getClass());

		// What to do if things complete successfully.
		try {
			if (status.equals(Status.START)) {
				if (Settings.VERBOSE)
					System.out.println("Starting " + command.getCommand()
							+ " command");
				try {

					String title = "";
					String message = "";

					// we will use a boolean flag to determine when to turn the
					// progress
					// bar on and off. since all threaded tasks are run through
					// this method
					// it's often times inconvenient to handle if the progress
					// panel is
					// opened or closed when we need to use it.
					boolean killAfter = false;
					title = "Progress...";
					if (command.getCommand().equals(JobCommand.GETOUTPUT)) {
						message = "Requesting file...";
						killAfter = true;
					} else if (command.getCommand().equals(JobCommand.KILL)) {
						message = "Killing job "
								+ command.getArguments().get("jobIDs") + "...";
						/* added nik for copying from hpc */} else if (command
							.getCommand().equals(JobCommand.CP)) {
						message = "Requesting file...";
						killAfter = true;
					}

					else if (command.getCommand().equals(JobCommand.UPDATE)) {
						message = "Refreshing job list...";
						killAfter = true;
					} else if (command.getCommand().equals(JobCommand.SEARCH)) {
						message = "Searching for jobs...";
						killAfter = true;
					} else if (command.getCommand().equals(JobCommand.DELETE)) {
						message = "Deleting job "
								+ ((String) command.getArguments()
										.get("jobIDs")) + "...";
					} else if (command.getCommand().equals(JobCommand.HIDE)) {
						message = "Hiding job(s)...";
					} else if (command.getCommand().equals(
							JobCommand.SHOW_HIDDEN)) {
						message = "Un-hiding job(s)...";
					} else if (command.getCommand().equals(
							JobCommand.SHOW_HIDDEN)) {
						message = "Unhiding all jobs...";
					} else if (command.getCommand().equals(JobCommand.QSTAT)) {
						message = "Refreshing job table...";
						killAfter = true;
					} else if (command.getCommand().equals(JobCommand.PREDICT)) {
						message = "Updating status for job "
								+ ((JobBean) command.getArguments().get("job"))
										.getId() + "...";
						// killAfter = true;
					}

					final boolean killProgressPanelWhenFinished = killAfter;

					SwingWorker worker = new SwingWorker() {

						public Object construct() {
							try {
								inProcess = true;

								command.execute();

								if (Thread.interrupted()) {
									updateProgress("Cleaning up...");
									throw new InterruptedException(
											command.getCommand()
													+ " Command cancelled by user.");
								}
							} catch (InterruptedException e) {
								// if the user pressed the cancel button, then
								// the
								// running task will be killed by the exception.
								// we
								// still need to dispose of the progress bar
								// properly.
								// fortunately for us, the finish method is
								// called
								// regardless of how this method exited, thus we
								// do
								// not need to explicitly need to handle this
								// exception
								// other than to disregard it and let the normal
								// cleanup occur. Normal cleanup would be
								// reversing
								// all action taken during this command. This,
								// however,
								// is not possible since a job may have already
								// been
								// submitted, deleted, etc by the time the
								// cancel
								// button is pushed. As a result, we do nothing
								// until
								// we can modify the service API to accommodate
								// this
								// feature request.

								// commandCancelled = true;
								// gms.undoCurrentAction(command);

							} catch (SessionException e) {

								GridChem.oc.monitorWindow.setUpdate(false);

								int viewLog = JOptionPane
										.showConfirmDialog(
												GridChem.oc.monitorWindow,
												"Your session has expired. Would\n"
														+ "you like to reauthenticate to the CCG?",
												"Session Timeout",
												JOptionPane.YES_NO_OPTION,
												JOptionPane.ERROR_MESSAGE);

								if (viewLog == 0) {
									GridChem.appendMessage("Resetting user authentication...");
									LoginDialog.clearLogin();
									GridChem.appendMessage("Complete\n");
									GridChem.oc.updateAuthenticatedStatus();
									GridChem.oc.doAuthentication();
								}
							} catch (GMSException e) {
							} catch (ConnectException e) {
								if (MonitorVO.warningDialog == null) {
									updateProgress("A connection error has occurred.\n"
											+ "Please check your connection\nand authenticate again.");
								} else {
									updateProgress("A connection error has occurred.\n"
											+ "Please check your connection\nand authenticate again.");
								}
							} catch (Exception e) {
								updateProgress("An unknown exception occurred");
								e.printStackTrace();
							}

							return null;
						}

						// this method will be called after the construct method
						// finishes execution. The finish method will run in the
						// event thread, so it's ok to modify the GUI in here.
						public void finished() {
							if (killProgressPanelWhenFinished) {
								stopWaiting();
								inProcess = false;
							}
						}
					};

					if (progressCancelPrompt == null) {

						String showGuiFlag = (String) command.getArguments()
								.get("show.progress");

						if (showGuiFlag != null) {
							if (showGuiFlag.equals("true")) {
								startWaiting(title, message, worker);
							} // if the flag is false, don't show the progress
								// bar
						} else {
							// startWaiting(title,message,worker);
						}
					} else {
						updateProgress(message);
					}

					worker.start();

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (status.equals(Status.COMPLETED)) {
				if (Settings.VERBOSE)
					System.out.println(command.getCommand()
							+ " Command Completed");

				if (command.getCommand().equals(JobCommand.CP)) { // changed
																	// from
																	// GET_OUTPUT
																	// -nik
					Trace.note("Finished GETOUTPUT, launching nanocad");

					// updateProgress("File Successfully retrieved. Launching Application...");

					// System.out.println("jobpanel:1314");

					/* Hotfix To Be changed */
					/*
					 * if (doAbaqus!=1) { doViewSpectraPlot(getSelectedJob()); }
					 */

					SwingWorker worker = new SwingWorker() {
						public Object construct() {
							if (doNanocad == 1) {
								File outputFile = new File(dataFileName);
								launchNanoCad(outputFile);
								doNanocad = 0;
							}
							if (doAbaqus == 1) {
								viewAbaqusCAEJob(getSelectedJob());
								doAbaqus = 0;
							}
							if (doMolden == 1) {
								viewMoldenJob(getSelectedJob());
								doMolden = 0;
							}
							/*
							 * if (doSpectra==1){
							 * doViewSpectraPlot(getSelectedJob());
							 * 
							 * }
							 */

							if (doVMD == 1) {
								viewVMDJob(getSelectedJob());
								doVMD = 0;
							}
							if (doJMol == 1) {
								viewJMolJob(getSelectedJob());
								doJMol = 0;
							}
							if (doJMOL == 1) {
								viewJMOLJob(getSelectedJob());
								doJMOL = 0;
							}
							// launchNanoCad((File)((JobCommand)command).getOutput());
							return null;
						}

						// the running of this thread is non-blocking, thus the
						// progress
						// panel will already have been terminated by the time
						// the
						// finished() method is called. Since the launchNanoCad
						// routine will pop up several obvious indications to
						// it's
						// success or failure, we do no worry about updating the
						// user
						// with a progress bar message here.
						public void finished() {
						}
					};

					worker.start();

				} else if (command.getCommand().equals(JobCommand.KILL)) {

					Trace.note("Finished killing job. Updating file list.");

					// updateProgress("Job " +
					// command.getArguments().get("jobIDs") +
					// " killed.\nUpdating job listing...");

					final QSTATCommand qstatCommand = new QSTATCommand(this);

					statusChanged(new StatusEvent(qstatCommand, Status.START));

				} else if (command.getCommand().equals(JobCommand.PREDICT)) {
					Trace.note("Finished Predicting Job Start Time");
					stopWaiting();
					//String estStartTime = parseShowstart();
					/*
					 * if (estStartTime.equals(
					 * "Job has started 'Running'. Its no longer scheduled.")) {
					 * System.out.println ("from - sign change status"); final
					 * QSTATCommand qstatCommand = new QSTATCommand(this);
					 * 
					 * statusChanged(new
					 * StatusEvent(qstatCommand,Status.START)); }
					 */
					//final QSTATCommand qstatCommand = new QSTATCommand(this);

					//statusChanged(new StatusEvent(qstatCommand, Status.START));
					/*
					 * JOptionPane.showMessageDialog(this, estStartTime,
					 * "Start Time Prediction",
					 * JOptionPane.INFORMATION_MESSAGE);
					 */
					createJobInfoDialog(getSelectedJob(), (String) command.getOutput());

				} else if (command.getCommand().equals(JobCommand.UPDATE)) {
					Trace.note("Finished UPDATE, refreshing user's VO");

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							// JobBean[] selectedJobs = getSelectedJobs();
							GridChem.jobs = ((UPDATECommand) command)
									.getOutput();
							enableButtonActions(true);
							if (GridChem.oc.monitorWindow != null)
								updateJobTable(GridChem.jobs);
							// resetSelectedTableItems(selectedJobs);
						}
					});
				} else if (command.getCommand().equals(JobCommand.SEARCH)) {
					Trace.note("Finished SEARCH, refreshing table model");

					if (((SEARCHCommand) command).getOutput().size() == 0) {
						JOptionPane.showMessageDialog(this,
								"Search returned zero results.",
								"Job Search Error", JOptionPane.ERROR_MESSAGE);
					} else {

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								if (!commandCancelled) {
									// JobBean[] selectedJobs =
									// getSelectedJobs();
									lastSearch = command;
									updatedWithSearchResults = true;
									searchResults = ((SEARCHCommand) command)
											.getOutput();
									System.out.println("Search returned "
											+ searchResults.size()
											+ " results.");
									enableButtonActions(true);
									ArrayList<JobBean> jlist = new ArrayList<JobBean>();
									jlist.addAll(searchResults);
									updateJobTable(jlist);
									System.out.println(jlist.toString());
									// resetSelectedTableItems(selectedJobs);
								}
							}
						});
					}

					stopWaiting();
				} else if (command.getCommand().equals(JobCommand.DELETE)) {
					Trace.note("Finished DELETE, refreshing table model");
					// since the service might cache the job record,
					// we go ahead here and hide the deleted job manually.
					// they will more than likely show up in the next query.
					m_data.deleteJobs((String) command.getArguments().get(
							"jobIDs"));
					stopWaiting();
					// QSTATCommand qstatCommand = new QSTATCommand(this);
					//
					// statusChanged(new
					// StatusEvent(qstatCommand,Status.START));

				} else if (command.getCommand().equals(JobCommand.HIDE)) {
					Trace.note("Finished HIDE, refreshing table model");
					// since the service might cache the job record,
					// we go ahead here and hide them manually.
					// they will more than likely reflect his in the next query.
					try {
						m_data.hideJobs((String) command.getArguments().get(
								"jobIDs"));
					} catch (Exception e) {
						e.printStackTrace();
					}

					stopWaiting();
					// QSTATCommand qstatCommand = new QSTATCommand(this);
					//
					// statusChanged(new
					// StatusEvent(qstatCommand,Status.START));

				} else if (command.getCommand().equals(JobCommand.SHOW_HIDDEN)) {
					Trace.note("Finished SHOW_HIDDEN, refreshing table model");
					// since the service might cache the job record,
					// we go ahead here and unhide them manually.
					// they will more than likely show up in the next query.
					m_data.unhideJobs();
					stopWaiting();
					// QSTATCommand qstatCommand = new QSTATCommand(this);
					//
					// statusChanged(new
					// StatusEvent(qstatCommand,Status.START));

				} else if (command.getCommand().equals(JobCommand.QSTAT)) {
					Trace.note("Finished QSTAT, refreshing table model");

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {

							// when the table refreshes, whatever row(s) that
							// were
							// highlighted are deselected in the new table
							// model.
							// to avoid this, we store the saved state of the
							// table
							// and then restore it afterwards. We use a utility
							// method to find and reselect those jobs in the new
							// table.
							// this didn't work
							// JobBean[] selectedJobs = getSelectedJobs();

							GridChem.jobs = ((QSTATCommand) command)
									.getOutput();

							enableButtonActions(true);

							if (GridChem.oc.monitorWindow != null) {
								if (!commandCancelled) {
									updateJobTable(GridChem.jobs);
								}
							}

							// resetSelectedTableItems(selectedJobs);
						}
					});
				}

				// File retrieval was broken into multiple parts to enable large
				// files
				// to be downloaded without having to hold the entire thing in
				// memory.
				// to do this, we download 56K blocks and append them into a
				// single
				// file.
			} else if (status.equals(Status.DOWNLOADING)) {

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						startWaitingForDownload(
								"Downloading file...",
								((Integer) command.getArguments().get(
										"totalBlocks")).intValue());
					}
				});

			} else if (status.equals(Status.READY)) {

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						updateProgress(((Long) command.getArguments().get(
								"blocksReceived")).intValue());
					}
				});

				// What to do if things fail:
			} else {
				if (Settings.VERBOSE)
					System.out.println(command.getCommand()
							+ " Command Failed!");
				// modified by nik to take care of CP
				if (command.getCommand().equals(JobCommand.GETOUTPUT)
						|| command.getCommand().equals(JobCommand.CP)) {
					Exception e = (Exception) command.getArguments().get(
							"exception");
					System.out.println(e.getMessage() + "\n\n\n");
					if (e != null && e.getMessage() != null) {
						if (e.getMessage().indexOf(
								"No file information available") > -1) {
							updateProgress("File donwload failed due to:\n"
									+ e.getMessage().substring(
											e.getMessage().indexOf(":") + 1));
						} else if (e.getMessage().indexOf(
								"Remote file not found") > -1
								|| e.getMessage()
										.indexOf(
												"No file information available for job") > -1
								|| e.getMessage().indexOf(
										"No file found for the given job") > -1) {
							int browseForFile = JOptionPane
									.showConfirmDialog(
											this,
											"No output file(s) found for this job.\n"
													+ "Would you like to launch file the browser\n"
													+ "to locate the job manually?",
											"File Not Present",
											JOptionPane.YES_NO_OPTION);

							if (browseForFile == 0) {
								doBrowseFiles((JobBean) command.getArguments()
										.get("job"));
							}
							// progressDialog.setVisible(false);
							// progressDialog.dispose();

						}
						if (e.getMessage().indexOf("Invocation") > -1) {
							stopWaiting();
							JOptionPane
									.showMessageDialog(
											this,
											"Could not retrieve file.\nMost likely "
													+ "this is due to the delay retrieving\ndata from mass "
													+ "storage's tape drive.\nThis matter will usually be "
													+ "resolved on it's own.\nPlease try again in a few moments.",
											"File Retrieval Error",
											JOptionPane.OK_OPTION);
						} else {
							updateProgress("File donwload failed due to:\n"
									+ e.getMessage());
						}
					} else {
						updateProgress("Error occurred while launching nanocad.");
					}
				} else if (command.getCommand().equals(JobCommand.PREDICT)) {
					// System.out.println("predict command failed: database error***********");
					
					createJobInfoDialog(getSelectedJob(), "N/A");
					stopWaiting();
					/*
					 * JOptionPane.showMessageDialog( this,
					 * "Cannot Predict Job Start Time: " +
					 * ((Exception)command.getArguments
					 * ().get("exception")).getMessage(),
					 * "Time Prediciton Error", JOptionPane.ERROR_MESSAGE );
					 */

				} else if (command.getCommand().equals(JobCommand.KILL)
						|| command.getCommand().equals(JobCommand.DELETE)) {
					String exception = (String) command.getArguments().get(
							"exception");

					System.out
							.println("Kill/Delete Jobs received failed exception of type "
									+ exception);

					if (exception != null) {

						String message = exception.substring(
								exception.indexOf(":") + 2, exception.length());

						exception = exception.substring(0,
								exception.indexOf(":"));

						System.out.println(exception);

						if (exception.equals(DELETE) || exception.equals(KILL)) {

							stopWaiting();

							int viewLog = JOptionPane
									.showConfirmDialog(
											JobPanel.this,
											"Job "
													+ ((exception
															.equals(DELETE)) ? "deletion"
															: "kill")
													+ " failed.\n"
													+ "View error log?",
											((exception.equals(DELETE)) ? "Delete"
													: "Kill")
													+ " Job Error",
											JOptionPane.YES_NO_OPTION,
											JOptionPane.ERROR_MESSAGE);

							if (viewLog == 0) {
								Trace.note("Opening editor with"
										+ (String) command.getArguments().get(
												"error.log.file"));
								JobPanel.openEditor((String) command
										.getArguments().get("error.log.file"));
							}
						} else {
							updateProgress(message);
						}
					} else {
						updateProgress("Job kill failed for unknown reason.");
					}
				} else if (command.getCommand().equals(JobCommand.UPDATE)) {
					JOptionPane.showMessageDialog(this,
							"Failed to update job listing.",
							"Job Management Error", JOptionPane.ERROR_MESSAGE);
				} else if (command.getCommand().equals(JobCommand.SEARCH)) {
					JOptionPane.showMessageDialog(
							this,
							"Job Search Failed: "
									+ ((Exception) command.getArguments().get(
											"exception")).getMessage(),
							"Job Search Error", JOptionPane.ERROR_MESSAGE);
				} else if (command.getCommand().equals(JobCommand.HIDE)) {
					JOptionPane.showMessageDialog(
							this,
							"Hide Job Failed: "
									+ ((Exception) command.getArguments().get(
											"exception")).getMessage(),
							"Job Management Error", JOptionPane.ERROR_MESSAGE);
				} else if (command.getCommand().equals(JobCommand.SHOW_HIDDEN)) {
					JOptionPane.showMessageDialog(
							this,
							"Show Hidden Jobs Failed: "
									+ ((Exception) command.getArguments().get(
											"exception")).getMessage(),
							"Job Management Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (PermissionException e) {
			stopWaiting();

			if (MonitorVO.warningDialog == null) {
				MonitorVO.warningDialog = new WarningDialog(
						this,
						"Connection Error",
						"A session error has occurred.\n"
								+ "Please check your connection\nand authenticate again.");
			} else {
				MonitorVO.warningDialog
						.updateWarning(
								"Connection Error",
								"A session error has occurred.\n"
										+ "Please check your connection\nand authenticate again.");
			}

			// JOptionPane.showMessageDialog(this,
			// "A session error has occurred.\n" +
			// "Please check your connection\nand authenticate again.",
			// "Connection Error", JOptionPane.ERROR_MESSAGE);
			LoginDialog.clearLogin();
		}
		Trace.exit();
	}

	private void launchNanoCad(File file) {
		final ProgressMonitor pm;
		String file_name = dataFileName;
		File flocal = new File(file_name);
		pm = new ProgressMonitor(null, "Monitor Job Output Progress",
				"Parsing output file...", 0, 100);
		pm.setProgress(25);
		pm.setMillisToPopup(0);
		pm.setMillisToDecideToPopup(0);
		pm.setNote("Generating temporary input files....");
		pm.setProgress(10);
		try {
			FileWriter fw = new FileWriter(Settings.jobDir
					+ Settings.fileSeparator + "loadthis", false);
			// fw.write(fw.write(Settings.jobDir +
			System.out.println("getSoftwareNamedatadir: "
					+ Env.getApplicationDataDir());
			fw.write(Env.getApplicationDataDir() + Settings.fileSeparator
					+ "finalcoord.pdb\n");
			// File fb = new File(Settings.jobDir +
			File fb = new File(Env.getApplicationDataDir()
					+ Settings.fileSeparator + "finalcoord.pdb");
			if (fb.exists()) {
				fb.delete();
			}
			// fw.write("water.pdb\n");
			fw.close();
		} catch (IOException ioe) {
			throw new VisualizationException("Could not open needed file!!",
					ioe);
		}

		String tmpfile = "tmp.txt";

		File fa = new File(Settings.defaultDirStr + Settings.fileSeparator
				+ tmpfile);
		if (fa.exists()) {
			fa.delete();
		}

		// Parse output file and display energy and gradient data plots
		JFrame frame = new DataTree();

		// opening output file in local text editor
		pm.setNote("Opening outfile in text Editor");
		pm.setProgress(50);

		// Checking the length of output file
		System.out.println("The size of output file in KB is "
				+ flocal.length());
		if (flocal.length() == 0) {
			pm.close();
			JOptionPane
					.showMessageDialog(
							null,
							"Output file is empty!! This generally means that the data\n"
									+ "from your job is not available. You can retry after some more time or \n"
									+ " Check to see that your job exited\n"
									+ "normally using the \"Get Job Status\" button, and that the data is\n"
									+ "saved to mass storage  using the \"Browse Mass Storage\" button.\n\n"
									+ "Direct further questions to help@www.gridchem.org.",
							"Monitor Job Output",
							JOptionPane.INFORMATION_MESSAGE);
		} else {
			// open output file in local editor
			markEndOfFile(dataFileName);
			openEditor(dataFileName);
		}

		pm.setNote("Launching Nanocad");
		pm.setProgress(80);

		File fcpdb = new File(Env.getApplicationDataDir()
				+ Settings.fileSeparator + "finalcoord.pdb");
		if (fcpdb.exists()) {
			if (fcpdb.length() > 0) {
				System.out.println(" File size for " + fcpdb.getName() + " is "
						+ fcpdb.length());

				nanWin = new nanocadFrame2();

				NanoCadListener nanlistener = new NanoCadListener();

				nanWin.addWindowListener(nanlistener);

				nanWin.nano.addComponentListener(nanlistener);
				pm.setNote("Launching Nanocad and output");
				boolean isactive = frame.isActive();
				boolean isfocusable = frame.isFocusableWindow();
				System.err.println("isactive = " + frame.isActive()
						+ " isfocusable = " + frame.isFocusableWindow());
			} else {
				pm.close();
				JOptionPane
						.showMessageDialog(
								null,
								"There are no coordinates to display.\n"
										+ "You can retry by retrieving a more recent copy of the output.\n"
										+ "Report unresolved issuses at http://www.gridchem.org/consult\n"
										+ "Please click the OK button to continue.",
								"Nanocad Molecule Viewer in Output Parsing",
								JOptionPane.INFORMATION_MESSAGE);
			}
		}

		pm.setProgress(100);
		pm.close();
	}

	public static void markEndOfFile(String dataFileName) {
		try {
			// mark the end of the output file: this is useful for the case when
			// the calculation has not completed. 10/21/02
			RandomAccessFile outFile = new RandomAccessFile(dataFileName, "rw");

			// find the size of the file:
			long lg = outFile.length();
			// go to the end of the file

			outFile.seek(lg - 65);
			long outPointer = outFile.getFilePointer();
			System.out.println("value of filepointer= " + outPointer
					+ " length of file= " + lg);
			// now printout the marker if it has not been marked already
			String fileEndMarker = "THE_END_OF_FILE";
			// Mark end of file if writerEnder==1
			int writeEnder = 1;
			while (outPointer != lg) {

				if (outFile.readLine().equals(fileEndMarker))
					writeEnder = 0;
				System.out.println("writeEnder= " + writeEnder);
				outPointer = outFile.getFilePointer();
			}

			if (writeEnder == 1) {

				outFile.writeBytes("\n");
				outFile.writeBytes("\n");
				outFile.writeBytes("THE_END_OF_FILE\n");
				outFile.writeBytes("THE_END_OF_FILE\n");
				outFile.writeBytes("THE_END_OF_FILE\n");
				outFile.writeBytes("THE_END_OF_FILE\n");
				outFile.writeBytes("THE_END_OF_FILE");
				outFile.writeBytes("\n");
				outFile.writeBytes("\n");
			}

		} catch (IOException ie) {
			System.err.println("Error in writing output.txt file" + ie);
		}
	}

	public static void openEditor(String file_name) {
		String errMsg = "For more information, visit www.gridchem.org";
		String osName = System.getProperty("os.name");
		int len = file_name.length();
		String commonCopy = Env.getApplicationDataDir() + File.separator
				+ jobName + ".output";

		// String copyfile = file_name.substring(0,(len-4));
		// copying file as filename_copy.out
		// copyfile = copyfile+ "_copy.out";

		try {

			if (osName.startsWith("Windows")) {

				// System.out.println("***********file_name: "+file_name);
				String nfName = '"' + file_name + '"';

				commonCopy = '"' + commonCopy + '"';

				// String newfName = '"'+file_name+".in";
				// newfName = file_name + '"';
				Runtime.getRuntime().exec(
						"cmd.exe /c copy " + nfName + " " + commonCopy);

				System.out.println("filename is " + file_name);
				if (file_name.endsWith(".out")) {
					Runtime.getRuntime().exec("write " + commonCopy);
				} else {
					Desktop.getDesktop().open(new File(file_name));
				}
				// Runtime.getRuntime().exec("write " + commonCopy);

			} else if (osName.startsWith("Mac")) {
				// String copyfile = file_name+".inp";
				System.out.println("filename is " + file_name);
				System.out.println("common copy is " + commonCopy);
				System.out.println("job name is " + jobName);
				Runtime.getRuntime().exec("cp " + file_name + " " + commonCopy);
				if (jobName == null) {
					Runtime.getRuntime().exec("open -a TextEdit " + file_name);
				} else {
					Runtime.getRuntime().exec("open -a TextEdit " + commonCopy);
				}
			} else { // assume Unix or Linux

				// TODO; vi and vim do not work on at least some platforms
				// because they cannot be spawned as graphical programs;
				// They need to be spawned via an xterm. srb 9/29/06
				String[] editors = { "gedit", "gvim", "vi", "vim" };

				String editor = null;
				for (int count = 0; count < editors.length && editor == null; count++) {
					if (Runtime.getRuntime()
							.exec(new String[] { "which", editors[count] })
							.waitFor() == 0) {
						editor = editors[count];
					}
				}

				if (editor == null) {
					throw new Exception("Could not find any editor");
				} else {
					// System.out.println("name of editor " + editor);
					// System.out.println("Name of file " + file_name);
					// The command array form of exec does not use a
					// StringTokenizer
					// to process the argument; this is desired because
					// it may contain special characters, whitespace, etc. srb
					// 9/29/06
					// TODO; exec can throw several exceptions which should be
					// handled here; srb 9/29/06
					// String copyfile = file_name+".inp";
					Runtime.getRuntime().exec(
							"cp " + file_name + " " + commonCopy);
					Runtime.getRuntime().exec(
							new String[] { editor, commonCopy });
				}
			}

		} catch (IOException e) {
			JOptionPane
					.showMessageDialog(
							null,
							"The specified file has no associated application or the associated application fails to be launched");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, errMsg);
		}
	}
}

class ColoredTableCellRenderer extends DefaultTableCellRenderer {

	public void setValue(Object value) {
		if (value instanceof ColorData) {
			ColorData cvalue = (ColorData) value;
			setForeground(cvalue.m_color);
			setText(cvalue.m_data.toString());
		} else if (value instanceof IconData) {
			IconData ivalue = (IconData) value;
			setIcon(ivalue.m_icon);
			setText(ivalue.m_data.toString());
		} else
			super.setValue(value);
	}
}

class ColorData {

	public Color m_color;
	public Object m_data;

	public static Color RED = Color.red;
	public static Color ORANGE = Color.orange;
	public static Color GREEN = Color.green;
	public static Color BLUE = Color.blue;
	public static Color CYAN = Color.cyan;
	public static Color GRAY = Color.gray;
	public static Color BLACK = Color.black;
	public static Color YELLOW = Color.yellow;
	public static Color PINK = Color.pink;

	public ColorData(Color color, Object data) {
		m_color = color;
		m_data = data;
	}

	public ColorData(JobStatusType status) {
		if (status.equals(JobStatusType.FAILED)
				|| status.equals(JobStatusType.REMOVED)
				|| status.equals(JobStatusType.RUNTIME_ERROR)
				|| status.equals(JobStatusType.SUBMISSION_ERROR)) {
			m_color = RED;
		} else if (status.equals(JobStatusType.FINISHED)
				|| status.equals(JobStatusType.NOT_IN_QUEUE)) {
			m_color = BLUE;
		} else if (status.equals(JobStatusType.REMOVED)
				|| status.equals(JobStatusType.STOPPED)) {
			m_color = PINK;
		} else if (status.equals(JobStatusType.INITIAL)
				|| status.equals(JobStatusType.SUBMITTING)) {
			m_color = ORANGE;
		} else if (status.equals(JobStatusType.RUNNING)
				|| status.equals(JobStatusType.MIGRATING)) {
			m_color = GREEN;
		} else {
			m_color = CYAN;
		}
		m_data = status;
	}

	public String toString() {
		return m_data.toString();
	}
}

class IconData {

	public ImageIcon m_icon;
	public Object m_data;

	public IconData(ImageIcon icon, Object data) {
		m_icon = icon;
		m_data = data;
	}

	public String toString() {
		return m_data.toString();
	}
}

class JobData {

	public static ImageIcon ICON_UP = new ImageIcon(Env.getApplicationDataDir()
			+ File.separator + "images" + File.separator + "icons"
			+ File.separator + "sortup.gif");
	public static ImageIcon ICON_DOWN = new ImageIcon(
			Env.getApplicationDataDir() + File.separator + "images"
					+ File.separator + "icons" + File.separator
					+ "sortdown.gif");
	public static ImageIcon ICON_BLANK = new ImageIcon("blank.gif");

	public JobBean jobBean;
	public ColorData status;

	public JobData(JobBean jobBean) {
		this.jobBean = jobBean;
		this.status = new ColorData(jobBean.getStatus());
	}

	public static ImageIcon getIcon(double change) {
		return (change > 0 ? ICON_UP : (change < 0 ? ICON_DOWN : ICON_BLANK));
	}
}

class ColumnData {

	public String m_title;
	public int m_width;
	public int m_alignment;

	public ColumnData(String title, int width, int alignment) {
		m_title = title;
		m_width = width;
		m_alignment = alignment;
	}
}

class JobTableData extends AbstractTableModel implements
		TableColumnModelListener {

	static final public ColumnData m_columns[] = {
			new ColumnData("ID", 75, JLabel.LEFT),
			new ColumnData("Name", 160, JLabel.LEFT),
			new ColumnData("Research Project", 160, JLabel.RIGHT),
			new ColumnData("Project", 75, JLabel.RIGHT),
			new ColumnData("Application", 100, JLabel.RIGHT),
			new ColumnData("Machine", 160, JLabel.RIGHT),
			new ColumnData("Queue", 100, JLabel.RIGHT),
			new ColumnData("Local Job ID", 85, JLabel.RIGHT),
			new ColumnData("Requested CPUs", 50, JLabel.RIGHT),
			new ColumnData("Requested Memory", 75, JLabel.RIGHT),
			new ColumnData("Status", 150, JLabel.RIGHT),
			new ColumnData("Start Time", 100, JLabel.RIGHT),
			new ColumnData("Stop Time", 100, JLabel.RIGHT),
			new ColumnData("Created", 100, JLabel.RIGHT),
			new ColumnData("Used CPUs", 50, JLabel.RIGHT),
			new ColumnData("Used Memory", 75, JLabel.RIGHT),
			new ColumnData("Cost", 75, JLabel.RIGHT),
			new ColumnData("Requested CPU Time", 75, JLabel.RIGHT) };

	public static ImageIcon COLUMN_UP = new ImageIcon(
			Env.getApplicationDataDir() + File.separator + "images"
					+ File.separator + "icons" + File.separator + "sortup.gif");
	public static ImageIcon COLUMN_DOWN = new ImageIcon(
			Env.getApplicationDataDir() + File.separator + "images"
					+ File.separator + "icons" + File.separator
					+ "sortdown.gif");

	protected Vector<JobData> m_vector;
	protected Vector<JobData> hidden_vector;
	protected Date m_date;
	protected int m_columnsCount = m_columns.length;

	public int m_sortCol = 0;
	public boolean m_sortAsc = false;

	public JobTableData(List<JobBean> jobs) {
		m_vector = new Vector<JobData>();
		hidden_vector = new Vector<JobData>();
		setDefaultData(jobs);
	}

	/**
	 * Builds 2 vectors, m_vector and hidden_vector of JobData elements. The
	 * former holds all visible job rows. The latter holds all hidden job rows.
	 * 
	 * @param jobs
	 */
	public void setDefaultData(List<JobBean> jobs) {
		m_vector.removeAllElements();
		hidden_vector.removeAllElements();
		for (JobBean job : jobs) {
			// if (!job.isHidden()) {
			m_vector.add(new JobData(job));
			// } else {
			// hidden_vector.add(new JobData(job));
			// }
		}

		sortData();
	}

	/**
	 * Removes the job at 'row' from the hidden_vector.
	 * 
	 * @param row
	 */
	public void deleteJobAtRow(int row) {
		m_vector.remove(row);
		sortData();
	}

	/**
	 * Removes the first job with id = 'jobID' from the hidden_vector.
	 * 
	 * @param jobID
	 */
	public void deleteJob(Long jobID) {
		deleteJobAtRow(getRowOfJob(jobID));
		fireTableDataChanged();
		sortData();
	}

	/**
	 * Removes all jobs with id's matching the semicolon delimited list of
	 * jobid's
	 * 
	 * @param jobIDs
	 */
	public void deleteJobs(String jobIDs) {
		if (jobIDs == null || jobIDs.equals("")) {
			return;
		}
		if (jobIDs.indexOf(";") > -1) {
			String[] ids = jobIDs.split(";");
			for (int i = 0; i < ids.length; i++) {
				deleteJob(new Long(ids[i]));
			}
		} else {
			deleteJob(new Long(jobIDs));
		}
	}

	/**
	 * Removes JobData from m_vector and places in the hidden_vector. Since
	 * m_vector is essentially the table model data, this will hide the job
	 * record from the user's view.
	 * 
	 * @param row
	 */
	public void hideJobAtRow(int row) {
		hidden_vector.add((JobData) m_vector.elementAt(row));
		m_vector.remove(row);
		sortData();
	}

	/**
	 * Removes the first job with id = jobID from m_vector and places it in
	 * hidden_vector. This method looks up the row of the job and calls
	 * hideJobAtRow(jobRow).
	 * 
	 * @param jobID
	 */
	public void hideJob(Long jobID) {
		hideJobAtRow(getRowOfJob(jobID));
		sortData();
	}

	/**
	 * Removes all jobs matching the semicolon delimited list of jobid's from
	 * m_vector and places them in hidden_vector. This method calls hideJob()
	 * with each job id.
	 * 
	 * @param jobID
	 */
	public void hideJobs(String jobIDs) {
		if (jobIDs == null || jobIDs.equals("")) {
			return;
		}
		if (jobIDs.indexOf(";") > -1) {
			String[] ids = jobIDs.split(";");
			for (int i = 0; i < ids.length; i++) {
				hideJob(new Long(ids[i]));
			}
		} else {
			hideJob(new Long(jobIDs));
		}

		fireTableDataChanged();
	}

	/**
	 * Adds the contents of hidden_vector to m_vector and empties hidden_vector.
	 * This will restore all jobs to the user's view and cause hasHiddenJobs to
	 * return false.
	 */
	public void unhideJobs() {
		for (JobData jobData : hidden_vector) {
			m_vector.add(jobData);
		}
		hidden_vector.removeAllElements();

		fireTableDataChanged();
		sortData();
	}

	/**
	 * Returns true if the hidden_vector has any elements. Returns false
	 * otherwise.
	 * 
	 * @return true if there are jobs in hidden_vector
	 */
	public boolean hasHiddenJobs() {
		return (hidden_vector.size() > 0);
	}

	public int getRowCount() {
		return m_vector == null ? 0 : m_vector.size();
	}

	public int getColumnCount() {
		return m_columnsCount;
	}

	public String getColumnName(int column) {
		return m_columns[column].m_title;
	}

	public Icon getColumnIcon(int column) {
		if (column == m_sortCol) {
			File f = new File(Env.getApplicationDataDir() + File.separator
					+ "images" + File.separator + "icons" + File.separator
					+ (m_sortAsc ? "sortup.gif" : "sortdown.gif"));
			System.out.println("Image icon " + f.getPath() + " exists "
					+ f.exists());
			return m_sortAsc ? COLUMN_UP : COLUMN_DOWN;
		}
		return null;
	}

	public boolean isCellEditable(int nRow, int nCol) {
		return false;
	}

	public Object getValueAt(int nRow, int nCol) {
		if (nRow < 0 || nRow >= getRowCount())
			return "";
		JobData row = (JobData) m_vector.elementAt(nRow);
		switch (nCol) {
		case 0:
			return row.jobBean.getId();
		case 1:
			return row.jobBean.getName();
		case 2:
			return row.jobBean.getExperimentName();
		case 3:
			return row.jobBean.getProjectName();
		case 4:
			return row.jobBean.getSoftwareName();
		case 5:
			return row.jobBean.getSystemName();
		case 6:
			return row.jobBean.getQueueName();
		case 7:
			return row.jobBean.getLocalId();
		case 8:
			return row.jobBean.getRequestedCpus();
		case 9:
			return row.jobBean.getRequestedMemory();
		case 10:
			return row.status;
		case 11:
			if (row.jobBean.getStartTime() == null
					|| row.jobBean.getStartTime().equals(new Date(0))) {
				return "---";
			} else {
				return new SimpleDateFormat("MM/dd/yyyy h:mm a")
						.format(row.jobBean.getStartTime());
			}
		case 12:
			if (row.jobBean.getStopTime() == null
					|| row.jobBean.getStopTime().equals(new Date(0))) {
				return "---";
			} else {
				return new SimpleDateFormat("MM/dd/yyyy h:mm a")
						.format(row.jobBean.getStopTime());
			}
		case 13:
			if (isToday(row.jobBean.getCreated())) {
				return new SimpleDateFormat("hh:mm a").format(row.jobBean
						.getCreated());
			} else {
				return new SimpleDateFormat("MM/dd/yyyy").format(row.jobBean
						.getCreated());
			}
		case 14:
			return row.jobBean.getUsedCpus();
		case 15:
			return row.jobBean.getUsedMemory();
		case 16:
			return row.jobBean.getCost();
		case 17:
			return resolveTimeLimit(row.jobBean.getRequestedCpuTime());
		}
		return "";
	}

	/**
	 * Create a string representation of the Calendar object in hh:mm format and
	 * where the hours field ranges from 0 - 32k
	 * 
	 * 
	 */
	private String resolveTimeLimit(Calendar cal) {
		if (cal == null) {
			return "---";
		}

		int days = (cal.get(Calendar.DAY_OF_YEAR) - 1) * 24;
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);

		return (days + hours) + ":" + ((minutes == 0) ? "00" : minutes);
	}

	private boolean isToday(Date date) {
		String today = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		String jobDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
		return today.equals(jobDate);
	}

	public String getTitle() {
		return "Job Data for " + GridChem.user.getUserName();
	}

	public void sortData() {
		Collections.sort(m_vector, new JobComparator(m_sortCol, m_sortAsc));
	}

	public void retrieveData() throws Exception {

		try {
			JobPanel jp = GridChem.oc.monitorWindow.getMonitorPanel()
					.getJobPanel();

			UPDATECommand updateCommand = new UPDATECommand(jp);

			jp.statusChanged(new StatusEvent(updateCommand, Status.START));

		} catch (Exception e) {
			throw e;
		}
	}

	public void loadData(final List<JobBean> jobs) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				m_vector.removeAllElements();
				m_vector = new Vector();
				for (JobBean job : jobs) {
					// if (!job.isHidden()) {
					m_vector.add(new JobData(job));
					// } else {
					// hidden_vector.add(new JobData(job));
					// }
				}
				fireTableDataChanged();
				sortData();
			}
		});
	}

	// TableColumnModelListener implementation

	public void columnAdded(TableColumnModelEvent e) {
		m_columnsCount++;
	}

	public void columnRemoved(TableColumnModelEvent e) {
		m_columnsCount--;
		if (m_sortCol >= m_columnsCount)
			m_sortCol = -1;
	}

	public JobBean getJobAtRow(int row) {
		return ((JobData) m_vector.elementAt(row)).jobBean;
	}

	public int getRowOfJob(Long jobID) {
		int row = 0;
		for (JobData rowItem : m_vector) {
			if (rowItem.jobBean.getId().equals(jobID)) {
				return row;
			}
			row++;
		}

		return -1;
	}

	public void columnMarginChanged(ChangeEvent e) {
	}

	public void columnMoved(TableColumnModelEvent e) {
	}

	public void columnSelectionChanged(ListSelectionEvent e) {
	}
}

class JobComparator implements Comparator {

	protected int m_sortCol;
	protected boolean m_sortAsc;

	public JobComparator(int sortCol, boolean sortAsc) {
		m_sortCol = sortCol;
		m_sortAsc = sortAsc;
	}

	public int compare(Object o1, Object o2) {
		if (!(o1 instanceof JobData) || !(o2 instanceof JobData))
			return 0;
		JobData s1 = (JobData) o1;
		JobData s2 = (JobData) o2;
		int result = 0;
		Long l1, l2;
		switch (m_sortCol) {
		case 0: // jobID
			l1 = s1.jobBean.getId();
			l2 = (Long) s2.jobBean.getId();
			result = l1 < l2 ? -1 : (l1 > l2 ? 1 : 0);
			break;
		case 1: // name
			result = s1.jobBean.getName().compareTo(s2.jobBean.getName());
			break;
		case 2: // research project
			result = s1.jobBean.getExperimentName().compareTo(
					s2.jobBean.getExperimentName());
			break;
		case 3: // project
			result = s1.jobBean.getProjectName().compareTo(
					s2.jobBean.getProjectName());
			break;
		case 4: // application
			result = s1.jobBean.getSoftwareName().compareTo(
					s2.jobBean.getSoftwareName());
			break;
		case 5: // hpc system
			result = s1.jobBean.getSystemName().compareTo(
					s2.jobBean.getSystemName());
			break;
		case 6: // queue
			result = s1.jobBean.getQueueName().compareTo(
					s2.jobBean.getQueueName());
			break;
		case 7: // local job id
			result = s1.jobBean.getLocalId().compareTo(s2.jobBean.getLocalId());
			break;
		case 8: // requested cpus
			l1 = s1.jobBean.getRequestedCpus();
			l2 = s2.jobBean.getRequestedCpus();
			result = l1 < l2 ? -1 : (l1 > l2 ? 1 : 0);
			break;
		case 9: // requested memory
			l1 = s1.jobBean.getRequestedMemory();
			l2 = s2.jobBean.getRequestedMemory();
			result = l1 < l2 ? -1 : (l1 > l2 ? 1 : 0);
			break;
		case 10: // status
			result = s1.jobBean.getStatus().compareTo(s2.jobBean.getStatus());
			break;
		case 11: // start time
			result = s1.jobBean.getStartTime().compareTo(
					s2.jobBean.getStartTime());
			break;
		case 12: // stop time
			result = s1.jobBean.getStopTime().compareTo(
					s2.jobBean.getStopTime());
			break;
		case 13: // created
			result = s1.jobBean.getCreated().compareTo(s2.jobBean.getCreated());
			break;
		case 14: // used cpus
			l1 = s1.jobBean.getUsedCpus();
			l2 = s2.jobBean.getRequestedMemory();
			result = l1 < l2 ? -1 : (l1 > l2 ? 1 : 0);
			break;
		case 15: // used memory
			l1 = s1.jobBean.getUsedMemory();
			l2 = s2.jobBean.getUsedMemory();
			result = l1 < l2 ? -1 : (l1 > l2 ? 1 : 0);
			break;
		case 16: // cost
			Double d1 = s1.jobBean.getCost();
			Double d2 = s2.jobBean.getCost();
			result = d1 < d2 ? -1 : (d1 > d2 ? 1 : 0);
			break;
		}

		if (!m_sortAsc)
			result = -result;
		return result;
	}

	public boolean equals(Object obj) {
		if (obj instanceof JobComparator) {
			JobComparator compObj = (JobComparator) obj;
			return (compObj.m_sortCol == m_sortCol)
					&& (compObj.m_sortAsc == m_sortAsc);
		}
		return false;
	}
}