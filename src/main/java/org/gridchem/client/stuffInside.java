/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software"), to deal with the Software without 
restriction, including without limitation the rights to use, 
copy, modify, merge, publish, distribute, sublicense, and/or 
sell copies of the Software, and to permit persons to whom 
the Software is furnished to do so, subject to the following 
conditions:
1. Redistributions of source code must retain the above copyright notice, 
   this list of conditions and the following disclaimers.
2. Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimers in the documentation
   and/or other materials provided with the distribution.
3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
   University of Illinois at Urbana-Champaign, nor the names of its contributors 
   may be used to endorse or promote products derived from this Software without 
   specific prior written permission.
    
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS WITH THE SOFTWARE.

 */

/*
 * Created on Apr 13, 2005
 * Moved From SubmitJobsWindow.java @ CCS,Uky
 * 
 */
package org.gridchem.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.gridchem.client.gui.buttons.ApplicationMenuItem;
import org.gridchem.client.gui.buttons.DropDownButton;
import org.gridchem.client.gui.jobsubmission.EditJobPanel;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.LogicalFileBean;

import G03Input.InputFile;
import G03Input.InputfileReader;
import G03Input.OptTable;
import G03Input.RouteClass;
import G03Input.showMolEditor;
import Gamess.gamessGUI.GamessGUI;

import com.asprise.util.ui.progress.ProgressDialog;

// TODO: refactor as internal class of SubmitJobsWindow
public class stuffInside extends JComponent // implements ListSelectionListener
{
	public static JFrame mainFrame;
	public static int selectedGUI = 0;
	JobBean job;
	JPanel buttonBox;
	JSplitPane queueSplitPane;

	JButton editButton;
	JButton newJButton;
	DropDownButton inputGeneratorGuiButton;
	JButton delButton;
	JButton submButton;
	JButton suballButton;
	JButton cancelButton;

	JPopupMenu buttonPopup;
	ApplicationMenuItem gaussianMenuItem;
	ApplicationMenuItem gamessMenuItem;

	ButtonListener b;

	int editJobIndex = 9999;
	EditJobPanel jobEditor;
	public com.asprise.util.ui.progress.ProgressDialog progressDialog;

	public static JList queueList;
	public static DefaultListModel queueModel;
	public static JobList queueJobList;

	public static DefaultListModel doneModel;
	public static JList doneList;
	public static JobList doneJobList;

	public boolean submittingJob = false;

	// static doSubBetween dsb = new doSubBetween();

	// private javax.swing.Timer timer;

	public stuffInside(JobBean job) {
		this();

		doEditNewJob(job);
	}

	public stuffInside() {

		// dsb.stop();

		// Border
		Border eBorder1 = BorderFactory.createEmptyBorder(0, 10, 0, 0);
		Border leBorder = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);

		// buttonBoxPane
		editButton = new JButton("Edit a job");
		newJButton = new JButton("Create New Job");
		inputGeneratorGuiButton = createDDB();
		delButton = new JButton("Delete Selected Job");
		submButton = new JButton("Submit Selected Jobs to Queue");
		suballButton = new JButton("Submit All Jobs to Queue"); // lixh_3_3
		cancelButton = new JButton("Close");

		// inputGeneratorGuiButton.setSize(5,6)

		// add(leftClickPopup);
		/*
		 * leftClickPopup.addMenuListener(new MenuListener( ) { public void
		 * menuCanceled(MenuEvent e){ }
		 * 
		 * public void menuDeselected(MenuEvent e){ }
		 * 
		 * public void menuSelected(MenuEvent e) {
		 * 
		 * int a,i,j; String
		 * causedGroup=null,causedKeyword=null,causedValue=null
		 * ,incompatibilityType=null; String
		 * affectedGroup=null,affectedKeyword=null,affectedValue=null;
		 * 
		 * JMenu menuSelected=(JMenu)e.getSource(); String
		 * keywordSelected=menuSelected.getText();
		 * System.out.println("listener called:"+keywordSelected);
		 * 
		 * } });
		 */

		JPanel buttonBox = new JPanel();
		// buttonBox.setPreferredSize(new Dimension(200,400));
		buttonBox.setLayout(new GridLayout(7, 1, 0, 5));
		buttonBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		buttonBox.add(editButton);
		buttonBox.add(newJButton);
		newJButton.requestFocusInWindow(); // Create job gets initial focus
		buttonBox.add(inputGeneratorGuiButton);
		buttonBox.add(delButton);
		buttonBox.add(submButton);
		buttonBox.add(suballButton); // lixh_3_3
		buttonBox.add(cancelButton);

		// I just added this line, does it help?
		queueJobList = new JobList(SubmitJobsWindow.jobQueue);
		doneJobList = new JobList(SubmitJobsWindow.jobSubmitted);
		ArrayList serializedJobList = queueJobList.getJobNamesList();
		System.out.println("ListOfJobs:nl is empty:"
				+ serializedJobList.isEmpty() + "\n");
		queueModel = new DefaultListModel();

		// something in here is not quite right, which is why
		// we have to do that job shuffle nonsense at the beginning
		// qbModel.addElement(j.getJobName());
		// try the following instead:
		int N = serializedJobList.size();
		for (int i = 0; i < N; i++) {
			queueModel.addElement((String) serializedJobList.get(i));
		}
		queueList = new JList(queueModel);

		System.out.println("***set name of queueboard here******");

		queueList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				setButtonsEnabled(!queueList.isSelectionEmpty());

				if (e.getClickCount() >= 2) {

					int n = queueList.getSelectedIndex();

					if (n >= 0) {
						jobEditor = new EditJobPanel(null, queueJobList
								.getJob(n));
					}
				}
			}
		});

		// One thing selected at a time
		queueList.setSelectedIndex(0);

		JScrollPane queuescrollPane = new JScrollPane(queueList);

		queuescrollPane.setMinimumSize(new Dimension(300, 100));
		queuescrollPane.isWheelScrollingEnabled();
		Border eBorder = BorderFactory.createEmptyBorder(5, 10, 5, 10);

		Container queueupperBox = Box.createVerticalBox();
		queueupperBox.add(queuescrollPane);

		JPanel queueBoxPane = new JPanel();
		queueBoxPane.add(queueupperBox);
		TitledBorder appPaneTitled1 = BorderFactory.createTitledBorder(
				leBorder, "Queued Jobs", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION);
		queueBoxPane.setBorder(BorderFactory.createCompoundBorder(
				appPaneTitled1, eBorder));
		queueBoxPane.setLayout(new BoxLayout(queueBoxPane, BoxLayout.Y_AXIS));

		// add(queueBoxPane);

		// queuedonescrollPane
		ArrayList nldone = doneJobList.getJobNamesList();
		System.out.println("ListOfJobsdone:nldone is empty:" + nldone.isEmpty()
				+ "\n");
		doneModel = new DefaultListModel();
		doneModel.addListDataListener(new ListDataListener() {

			public void intervalAdded(ListDataEvent arg0) {
				suballButton.setEnabled(true);
			}

			public void intervalRemoved(ListDataEvent arg0) {
				if (doneModel.size() > 0) {
					if (!submittingJob) {

						suballButton.setEnabled(doneModel.size() > 0);

						boolean isJobSelected = doneList.isSelectionEmpty();

						editButton.setEnabled(isJobSelected);
						delButton.setEnabled(isJobSelected);
						submButton.setEnabled(isJobSelected);
					}
				} else {
					setButtonsEnabled(false);
				}
			}

			public void contentsChanged(ListDataEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		int Ndone = nldone.size();

		for (int i = 0; i < Ndone; i++) {
			doneModel.addElement((String) nldone.get(i));
		}

		doneList = new JList(doneModel);
		// queuedoneBoard.setName("Submitted");
		// queuedoneBoard.TOOL_TIP_TEXT_KEY;
		doneList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				setButtonsEnabled(!doneList.isSelectionEmpty());

				if (e.getClickCount() >= 2) {

					int n = doneList.getSelectedIndex();

					if (n >= 0) {
						JobBean oldJob = doneJobList.getJob(n);
						JobBean newJob = new JobBean();
						newJob.setName(oldJob.getName());
						newJob.setExperimentName(oldJob.getExperimentName());
						newJob.setSystemName(oldJob.getSystemName());
						newJob.setSoftwareName(oldJob.getSoftwareName());
						newJob.setProjectName(oldJob.getProjectName());
						newJob.setQueueName(oldJob.getQueueName());
						newJob.setRequestedCpus(oldJob.getRequestedCpus());
						newJob.setRequestedCpuTime(oldJob.getRequestedCpuTime());
						newJob.getInputFiles().clear();

						// Job is already populated. Now add in the input files
						for (LogicalFileBean file : oldJob.getInputFiles()) {
							LogicalFileBean lFile = new LogicalFileBean();
							lFile.setJobId(file.getJobId());
							lFile.setLocalPath(file.getLocalPath());
							lFile.setCreated(file.getCreated());
							lFile.setId(file.getId());
							lFile.setRemotePath(file.getRemotePath());
							lFile.setUuid(file.getUuid());
							lFile.setUserId(file.getUserId());
							newJob.getInputFiles().add(lFile);
						}

						jobEditor = new EditJobPanel(null, newJob);
					}
				}
			}
		});

		doneList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane queuedonescrollPane = new JScrollPane(doneList);
		queuedonescrollPane.setMinimumSize(new Dimension(300, 100));

		Container queuelowerBox = Box.createVerticalBox();
		queuelowerBox.add(queuedonescrollPane);

		JPanel jobDonePane = new JPanel();
		jobDonePane.add(queuelowerBox);
		TitledBorder appPaneTitled2 = BorderFactory.createTitledBorder(
				leBorder, "Submitted Jobs", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION);
		jobDonePane.setBorder(BorderFactory.createCompoundBorder(
				appPaneTitled2, eBorder));
		jobDonePane.setLayout(new BoxLayout(jobDonePane, BoxLayout.Y_AXIS));

		// add(jobDonePane);
		// commented split panel

		// queueSplitPane
		queueSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				queueBoxPane, jobDonePane);
		queueSplitPane.setOneTouchExpandable(true);
		queueSplitPane.setDividerLocation(150);
		queueSplitPane.setPreferredSize(new Dimension(400, 300));
		Container queueBox = Box.createVerticalBox();
		queueBox.add(queueSplitPane);

		// Final Layout
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		// setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
		// add(queueBox);
		add(queueBox);
		add(buttonBox);

		// Add all action listener
		b = new ButtonListener();
		editButton.addActionListener(b);
		newJButton.addActionListener(b);
		// inputGeneratorGuiButton.addActionListener(b);
		delButton.addActionListener(b);
		submButton.addActionListener(b);
		suballButton.addActionListener(b); // lixh_3_3
		cancelButton.addActionListener(b);
		// queueList.addListSelectionListener(this);

		setButtonsEnabled(SubmitJobsWindow.jobQueue.size() == 0);

	}

	protected void update() {

		// rebuild the job queue list
		queueJobList.clear();
		queueJobList.addAll(SubmitJobsWindow.jobQueue);
		doneJobList.clear();
		doneJobList.addAll(SubmitJobsWindow.jobSubmitted);

		queueModel.clear();
		for (String entry : queueJobList.getJobNamesList()) {
			queueModel.addElement(entry);
		}

		// rebuild the job done list

		doneModel.clear();
		for (String entry : doneJobList.getJobNamesList()) {
			doneModel.addElement(entry);
		}

	}

	private DropDownButton createDDB() {
		PopupListener popupListener = new PopupListener();

		inputGeneratorGuiButton = new DropDownButton("Open Gaussian GUI");
		// ((JButton)inputGeneratorGuiButton.getComponent(1)).setBorderPainted(true);

		// buttonPopup = new JPopupMenu("Open Application GUI");
		// //leftClickPopup.setMnemonic(KeyEvent.VK_A);
		// buttonPopup.getAccessibleContext().setAccessibleDescription("Drop Down Menu for selecting GUI");
		//
		gaussianMenuItem = new ApplicationMenuItem("Gaussian", KeyEvent.VK_1);
		gaussianMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
				ActionEvent.ALT_MASK));
		gaussianMenuItem.getAccessibleContext().setAccessibleDescription(
				"Opens the Gaussian GUI");
		gaussianMenuItem.addActionListener(popupListener);

		gamessMenuItem = new ApplicationMenuItem("GAMESS", KeyEvent.VK_2);
		gamessMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
				ActionEvent.ALT_MASK));
		gamessMenuItem.getAccessibleContext().setAccessibleDescription(
				"Opens the GAMESS GUI");
		gamessMenuItem.addActionListener(popupListener);

		inputGeneratorGuiButton.getMenu().add(gaussianMenuItem);
		inputGeneratorGuiButton.getMenu().add(gamessMenuItem);

		return inputGeneratorGuiButton;
	}

	public class PopupListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			ApplicationMenuItem item = (ApplicationMenuItem) event.getSource();

			if (item.equals(gaussianMenuItem)) {
				selectedGUI = 1;
				showNewGUI();
				gaussianMenuItem.setLastSelected(true);
				gamessMenuItem.setLastSelected(false);
			} else if (item.equals(gamessMenuItem)) {
				selectedGUI = 1;
				GamessGUI.main(null);
				/*
				 * JFrame frame = new JFrame("GAMESS");
				 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				 * frame.setJMenuBar(new GamessGUI( )); frame.setSize(1000,700);
				 */
				// JFrame.setDefaultLookAndFeelDecorated(true);
				/*
				 * MetalTheme theme = new ColorTheme();
				 * MetalLookAndFeel.setCurrentTheme(theme);
				 */
				// frame.pack( );
				// frame.setVisible(true);
				System.out
						.println("This is dummy right now...Later I will add Gamess Call");
				gaussianMenuItem.setLastSelected(false);
				gamessMenuItem.setLastSelected(true);
			}

			inputGeneratorGuiButton.getButton().setText(
					"Open " + item.getText() + " GUI");
		}
	}

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == editButton) {
				editJobIndex = queueList.getSelectedIndex();
				if (editJobIndex == -1) {
					JOptionPane.showMessageDialog(SubmitJobsWindow.frame,
							"No job selected!!", "Edit Job",
							JOptionPane.ERROR_MESSAGE);
				} else {
					doEditJobPanel();
				}

			} else if (e.getSource() == newJButton) {
				RouteClass.keyIndex = 0;
				RouteClass.initCount = 0;
				OptTable.optC = 0;
				selectedGUI = 0;
				doEditNewJob();

			} else if (e.getSource() == inputGeneratorGuiButton) {

				showApplicationPopupMenu(inputGeneratorGuiButton,
						inputGeneratorGuiButton.getLocationOnScreen());

			} else if (e.getSource() == delButton) {
				int index = queueList.getSelectedIndex();
				if (index == -1) {
					JOptionPane.showMessageDialog(SubmitJobsWindow.frame,
							"No job selected!!", "Delete Job",
							JOptionPane.ERROR_MESSAGE);
				} else {
					doDeleteJob();
				}
			} else if (e.getSource() == submButton) {
				int index = queueList.getSelectedIndex();
				System.out.println("stuffinside index: " + index);
				if (index == -1) {
					int createNewJobResponse = JOptionPane
							.showConfirmDialog(
									SubmitJobsWindow.frame,
									"No job is selected in the queue panel.\n"
											+ "Would you like to create a new job now?",
									"Job Submission Error",
									JOptionPane.YES_NO_OPTION);
					if (createNewJobResponse == JOptionPane.YES_OPTION) {
						doEditNewJob();
					}
				} else {
					// setButtonsEnabled(false);
					// timer = doSubTimer();
					// dsb.go();
					// timer.start();
					System.out.println("stage 10 :stuffinside.java:349");
					// doSubmitJobs();
					new SwingWorker() {

						public Object construct() {

							submittingJob = true;
							JobBean job = SubmitJobsWindow.jobQueue
									.get(queueList.getSelectedIndex());

							progressDialog = new ProgressDialog(
									SubmitJobsWindow.frame,
									"Job \"" + job.getName() + "\" Submission Progress");
							progressDialog.millisToPopup = 0;
							progressDialog.millisToDecideToPopup = 0;
							progressDialog.displayTimeLeft = false;

							SubmitJob sj = new SubmitJob(job);
							sj.addProgressMonitor(progressDialog);
							sj.submit();
							//
							return progressDialog;
						}

						// public void finished() {
						// SwingUtilities.invokeLater(new Runnable() {
						// public void run() {
						// setButtonsEnabled(true);
						// }
						// });
						// }
					}.start();
				}
			} else if (e.getSource() == suballButton) {
				// int size = queueModel.getSize();
				// size = queueList.getModel().getSize();
				// int[] indices = new int[size];

				if (queueModel.getSize() == 0) {
					int createNewJobResponse = JOptionPane
							.showConfirmDialog(
									SubmitJobsWindow.frame,
									"No job exists in the queue panel.\n"
											+ "Would you like to create a new job now?",
									"Job Submission Error",
									JOptionPane.YES_NO_OPTION);
					if (createNewJobResponse == JOptionPane.YES_OPTION) {
						doEditNewJob();
					}
				} else {
					// for (int i=0; i<size; i++) {
					// indices[i] = i;
					// }
					// queueList.setSelectionInterval(0, queueModel.getSize() -
					// 1);
					// setButtonsEnabled(false);
					// timer = doSubTimer();
					// dsb.go();
					// timer.start();
					// doSubmitJobs();
					// setButtonsEnabled(true);

					new SwingWorker() {

						public Object construct() {

							submittingJob = true;

							// JobBean job =
							// SubmitJobsWindow.jobQueue.get(queueList.getSelectedIndex());

							progressDialog = new ProgressDialog(
									SubmitJobsWindow.frame,
									"Job Submission Progress");
							progressDialog.millisToPopup = 0;
							progressDialog.millisToDecideToPopup = 0;
							progressDialog.displayTimeLeft = false;

							SubmitJob sj = new SubmitJob(
									SubmitJobsWindow.jobQueue.get(0));
							sj.addProgressMonitor(progressDialog);
							sj.setSumitMultiple();
							sj.submitAll1();

							return progressDialog;
						}

					}.start();
				}
			} else if (e.getSource() == cancelButton) {
				SubmitJobsWindow.frame.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(null, "huh?",
						"This should not happen",
						JOptionPane.INFORMATION_MESSAGE);
			}

		}
	}

	private void showApplicationPopupMenu(Component c, Point p) {

		// p.y += c.getHeight();
		//
		// buttonPopup.setLocation(c.getLocation());

		buttonPopup.show(c, p.x, p.y);
	}

	/**
	 * Convenience method to enable or diable all the functional buttons on the
	 * SubmitJobsWindow frame.
	 * 
	 * @param enable
	 */
	public void setButtonsEnabled(boolean enable) {
		submButton.setEnabled(enable);
		suballButton.setEnabled(enable);
		delButton.setEnabled(enable);
		editButton.setEnabled(enable);
	}

	// public void valueChanged(ListSelectionEvent e) {
	//
	// if (e.getValueIsAdjusting() == false){
	// // System.out.println("dsb max = " + dsb.getLengthOfTask() +
	// // ", dsb value = " + dsb.getCurrent());
	// System.out.println("stage 9: stuffInside.java: 407");
	// if (dsb.done()) {
	// if (queueList.getSelectedIndex() == -1) {
	// editButton.setEnabled(false);
	// submButton.setEnabled(false); //lixh_add
	// delButton.setEnabled(false);
	// } else {
	// editButton.setEnabled(true);
	// delButton.setEnabled(true);
	// submButton.setEnabled(true); //lixh_add
	// }
	//
	// if (queueModel.getSize() > 0) {
	// suballButton.setEnabled(true);
	// } else {
	// suballButton.setEnabled(false);
	// } //
	// } //end if (dsb.done)
	// }
	// }

	public static void showNewGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		InputFile.tempinput = new String();
		InputfileReader.route = new String();
		showMolEditor.tempmol = new String();
		InputFile.inputfetched = 0;
		InputfileReader.chrgStr = null;
		InputfileReader.mulStr = null;

		mainFrame = new G03Input.G03MenuTree();
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		mainFrame.setSize(screenSize.width - 200, screenSize.height - 150);
		mainFrame.setResizable(true);
		mainFrame.setVisible(true);

	}

	public void doEditJobPanel() {
		int n;
		// determine which job was selected
		n = queueList.getSelectedIndex();

		if (n == -1) {
			JOptionPane.showMessageDialog(null, "You must select a job",
					"Error", JOptionPane.INFORMATION_MESSAGE);
		} else {// edit the job that was selected
			jobEditor = new EditJobPanel(null, SubmitJobsWindow.jobQueue.get(n));
		}
	}

	public void doEditNewJob() {

		jobEditor = new EditJobPanel();

		int size = queueModel.getSize();

		queueList.setSelectedIndex(size + 1);

		System.err.println("job index is: " + (size + 1));
	}

	public void doEditNewJob(JobBean job) {
		jobEditor = new EditJobPanel(null, job);

		int size = queueModel.getSize();

		queueList.setSelectedIndex(size + 1);

		System.err.println("job index is: " + (size + 1));
	}

	public void doDeleteJob() {
		int index = queueList.getSelectedIndex();

		int[] indices = queueList.getSelectedIndices();

		for (int i = 0; i < indices.length; i++) {
			queueModel.remove(indices[i] - i);
			SubmitJobsWindow.jobQueue.remove(indices[i] - i);
		}

		int size = queueModel.getSize();

		if (size == 0) {
			delButton.setEnabled(false);
			submButton.setEnabled(false); // lixh_add
		} else {
			if (index == queueModel.getSize()) {
				index--;
			}
			queueList.setSelectedIndex(index);
		}
	}

	public void doSubmitJobs() {
		int size = queueModel.getSize();

		final stuffInside sj = this;
		System.out.println("doSubmitJob: Invoked in event dispatch thread "
				+ SwingUtilities.isEventDispatchThread());
		new Thread() {
			public void run() {

				JobBean job = SubmitJobsWindow.jobQueue.get(queueList
						.getSelectedIndex());
				// SubmitJobsWindow.jobQueue.remove(0);
				System.out
						.println("SJ Worker: Invoked in event dispatch thread "
								+ SwingUtilities.isEventDispatchThread());

				ProgressDialog progressDialog = new ProgressDialog(sj,
						"Job Submission Progress");
				progressDialog
						.beginTask("Submitting " + job.getName(), 5, true);

				// submit the Job j to the queue
				SubmitJob sj = new SubmitJob(job);
				sj.addProgressMonitor(progressDialog);
				sj.submit();

				GridChem.appendMessage("Job " + job.getName()
						+ " successfully submitted to machine \n"
						+ job.getSystemName() + "\n");

				// return null;
			}
		}.start();

	}

	public static void AddElement(String s) {
		queueModel.addElement(s);
		queueList.setSelectedIndex(queueModel.getSize() - 1);
		queueList.ensureIndexIsVisible(queueModel.getSize() - 1);
	}

	int getSelectedIndex() {
		return queueList.getSelectedIndex();
	}

	static void setSelectedIndex(int i) {
		queueList.setSelectedIndex(i);
	}

	int qbModelGetSize() {
		return queueModel.getSize();
	}

	// private javax.swing.Timer doSubTimer() {
	// return new javax.swing.Timer(Invariants.ONE_SECOND, new ActionListener()
	// {
	// public void actionPerformed(ActionEvent evt) {
	// if (dsb.done()) {
	// timer.stop();
	// delButton.setEnabled(true);
	// submButton.setEnabled(true);
	// suballButton.setEnabled(true);
	// editButton.setEnabled(true);
	// }
	// }
	// });
	// }

}
