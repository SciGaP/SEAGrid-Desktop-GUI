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

import G03Input.*;
import Gamess.gamessGUI.GamessGUI;
import org.apache.airavata.gridchem.AiravataManager;
import org.apache.airavata.gridchem.experiment.ExperimentHandler;
import org.apache.airavata.model.experiment.ExperimentModel;
import org.apache.airavata.model.experiment.ExperimentSummaryModel;
import org.gridchem.client.gui.buttons.ApplicationMenuItem;
import org.gridchem.client.gui.jobsubmission.EditJobPanel;
import org.gridchem.client.gui.panels.CancelCommandPrompt;
import org.gridchem.client.gui.panels.myccg.MonitorVO;
import org.gridchem.client.gui.project.NewProjDialog;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// TODO: refactor as internal class of SubmitJobsWindow
public class stuffInside extends JComponent // implements ListSelectionListener
{
	public static JFrame mainFrame;
	public static int selectedGUI = 0;
	JPanel buttonBox;
	JSplitPane queueSplitPane;

	JButton editButton;
	JButton newJButton;
	JButton newProjButton;
//	DropDownButton inputGeneratorGuiButton;
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

	private CancelCommandPrompt progressCancelPrompt;

	private void startWaiting(String title, String labelText, SwingWorker worker) {
		progressCancelPrompt =
				new CancelCommandPrompt(this,title,labelText,-1,worker);

	}

	private void updateProgress(int value) {
		progressCancelPrompt.updateStatus();
	}

	private void updateProgress(String message) {
		progressCancelPrompt.updateStatus(message);
	}

	private void updateProgress(String message,int value) {
		progressCancelPrompt.updateStatus();
		progressCancelPrompt.updateStatus(message);
	}

	private void stopWaiting() {
		if (progressCancelPrompt != null) {
			progressCancelPrompt.finished();
			progressCancelPrompt = null;
		}
	}

	public stuffInside(ExperimentSummaryModel experiment) {
		this();
		doEditNewJob(experiment);
	}

	public void initLists(){
		queueJobList = new JobList(AiravataManager.getQueuedExperiments());
		doneJobList = new JobList(AiravataManager.getLaunchedExperiments());

		ArrayList serializedJobList = queueJobList.getJobNamesList();
		System.out.println("ListOfJobs:nl is empty:"
				+ serializedJobList.isEmpty() + "\n");
		queueModel = new DefaultListModel();
		int N = serializedJobList.size();
		for (int i = 0; i < N; i++) {
			queueModel.addElement((String) serializedJobList.get(i));
		}
		queueList = new JList(queueModel);

		ArrayList nldone = doneJobList.getJobNamesList();
		System.out.println("ListOfJobsdone:nldone is empty:" + nldone.isEmpty()
				+ "\n");
		doneModel = new DefaultListModel();
		int Ndone = nldone.size();

		for (int i = 0; i < Ndone; i++) {
			doneModel.addElement((String) nldone.get(i));
		}
		doneList = new JList(doneModel);

	}
	public stuffInside() {

		// dsb.stop();

		// Border
		Border eBorder1 = BorderFactory.createEmptyBorder(0, 10, 0, 0);
		Border leBorder = BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED);

		// buttonBoxPane
		editButton = new JButton("Edit An Experiment");
		newJButton = new JButton("Create New Experiment");
		newProjButton = new JButton("Create New Project");
//		inputGeneratorGuiButton = createDDB();
		delButton = new JButton("Delete Selected Experiment");
		submButton = new JButton("Launch Selected Experiments");
		suballButton = new JButton("Launch All Experiments"); // lixh_3_3
		cancelButton = new JButton("Close");


		JPanel buttonBox = new JPanel();
		// buttonBox.setPreferredSize(new Dimension(200,400));
		buttonBox.setLayout(new GridLayout(7, 1, 0, 5));
		buttonBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

		buttonBox.add(newProjButton);
		buttonBox.add(newJButton);
		buttonBox.add(editButton);
		newJButton.requestFocusInWindow(); // Create job gets initial focus
//		buttonBox.add(inputGeneratorGuiButton);
		buttonBox.add(delButton);
		buttonBox.add(submButton);
		buttonBox.add(suballButton); // lixh_3_3
		buttonBox.add(cancelButton);

		//getting done and pending experiments from Airavata

		initLists();

		queueList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				setButtonsEnabled(!queueList.isSelectionEmpty());

				/*if (e.getClickCount() >= 2) {

					int n = queueList.getSelectedIndex();

					if (n >= 0) {
						jobEditor = new EditJobPanel(null, queueJobList
								.getJob(n));
					}
				}*/
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
				leBorder, "Unlaunched Experiments", TitledBorder.LEFT,
				TitledBorder.DEFAULT_POSITION);
		queueBoxPane.setBorder(BorderFactory.createCompoundBorder(
				appPaneTitled1, eBorder));
		queueBoxPane.setLayout(new BoxLayout(queueBoxPane, BoxLayout.Y_AXIS));

		// add(queueBoxPane);

		// queuedonescrollPane

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

		// queuedoneBoard.setName("Submitted");
		// queuedoneBoard.TOOL_TIP_TEXT_KEY;
		doneList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				setButtonsEnabled(!doneList.isSelectionEmpty());

				if (e.getClickCount() >= 2) {

					int n = doneList.getSelectedIndex();

					if (n >= 0) {
						/*JobBean oldJob = doneJobList.getJob(n);
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

						jobEditor = new EditJobPanel(null, newJob);*/
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
				leBorder, "Launched Experiments", TitledBorder.LEFT,
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
		newProjButton.addActionListener(b);
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
		queueJobList.addAll(AiravataManager.getQueuedExperiments());
		doneJobList.clear();
		doneJobList.addAll(AiravataManager.getLaunchedExperiments());

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

//	private DropDownButton createDDB() {
//		PopupListener popupListener = new PopupListener();
//
//		inputGeneratorGuiButton = new DropDownButton("Open Gaussian GUI");
//		// ((JButton)inputGeneratorGuiButton.getComponent(1)).setBorderPainted(true);
//
//		// buttonPopup = new JPopupMenu("Open Application GUI");
//		// //leftClickPopup.setMnemonic(KeyEvent.VK_A);
//		// buttonPopup.getAccessibleContext().setAccessibleDescription("Drop Down Menu for selecting GUI");
//		//
//		gaussianMenuItem = new ApplicationMenuItem("Gaussian", KeyEvent.VK_1);
//		gaussianMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
//				ActionEvent.ALT_MASK));
//		gaussianMenuItem.getAccessibleContext().setAccessibleDescription(
//				"Opens the Gaussian GUI");
//		gaussianMenuItem.addActionListener(popupListener);
//
//		gamessMenuItem = new ApplicationMenuItem("GAMESS", KeyEvent.VK_2);
//		gamessMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
//				ActionEvent.ALT_MASK));
//		gamessMenuItem.getAccessibleContext().setAccessibleDescription(
//				"Opens the GAMESS GUI");
//		gamessMenuItem.addActionListener(popupListener);
//
//		inputGeneratorGuiButton.getMenu().add(gaussianMenuItem);
//		inputGeneratorGuiButton.getMenu().add(gamessMenuItem);
//
//		return inputGeneratorGuiButton;
//	}

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

//			inputGeneratorGuiButton.getButton().setText(
//					"Open " + item.getText() + " GUI");
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

				SwingWorker worker = new SwingWorker() {
					@Override
					public Object construct() {
						RouteClass.keyIndex = 0;
						RouteClass.initCount = 0;
						OptTable.optC = 0;
						selectedGUI = 0;
						doEditNewExperiment();
						return null;
					}

					@Override
					public void finished() {
						stopWaiting();
					}
				};
				startWaiting("Opening Create New Experiment Window", "Please wait few seconds", worker);
				worker.start();

			} else if (e.getSource() == newProjButton) {

				SwingWorker worker = new SwingWorker() {
					@Override
					public Object construct() {
						RouteClass.keyIndex = 0;
						RouteClass.initCount = 0;
						OptTable.optC = 0;
						selectedGUI = 0;
						doCreateNewProject();
						return null;
					}

					@Override
					public void finished() {
						stopWaiting();
					}
				};
				startWaiting("Opening Create New Project Window", "Please wait few seconds", worker);
				worker.start();

			} else if (e.getSource() == delButton) {

				int index = queueList.getSelectedIndex();
				if (index == -1) {
					JOptionPane.showMessageDialog(SubmitJobsWindow.frame,
							"No experiment selected!!", "Delete Experiment",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int response = JOptionPane.showConfirmDialog(null, "The experiment data will be completely deleted",
							"Do you want to continue", JOptionPane.YES_NO_OPTION);
					if(response == JOptionPane.OK_OPTION){
						SwingWorker worker = new SwingWorker() {
							@Override
							public Object construct() {
								RouteClass.keyIndex = 0;
								RouteClass.initCount = 0;
								OptTable.optC = 0;
								selectedGUI = 0;
								doDeleteExperiment();
								return null;
							}

							@Override
							public void finished() {
								stopWaiting();
							}
						};
						startWaiting("Deleting Experiment", "Please wait few seconds", worker);
						worker.start();
					}
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
						doEditNewExperiment();
					}
				} else {
					SwingWorker worker = new SwingWorker() {
						public Object construct() {
							RouteClass.keyIndex = 0;
							RouteClass.initCount = 0;
							OptTable.optC = 0;
							selectedGUI = 0;
							doSubmitExperiments();
							if(GridChem.oc.monitorWindow==null) {
								GridChem.oc.monitorWindow = new MonitorVO();
							}else{
								GridChem.oc.monitorWindow.getMonitorPanel().getJobPanel().refresh();
							}
							return null;
						}

						@Override
						public void finished() {
							stopWaiting();
						}
					};

					startWaiting("Launching selected Experiments", "Please wait few seconds", worker);
					worker.start();

				}
			} else if (e.getSource() == suballButton) {
				if (queueModel.getSize() == 0) {
					int createNewJobResponse = JOptionPane
							.showConfirmDialog(
									SubmitJobsWindow.frame,
									"No job exists in the queue panel.\n"
											+ "Would you like to create a new job now?",
									"Job Submission Error",
									JOptionPane.YES_NO_OPTION);
					if (createNewJobResponse == JOptionPane.YES_OPTION) {
						doEditNewExperiment();
					}
				} else {

					SwingWorker worker = new SwingWorker() {

						public Object construct() {
							RouteClass.keyIndex = 0;
							RouteClass.initCount = 0;
							OptTable.optC = 0;
							selectedGUI = 0;
							doSubmitAllExperiment();
							GridChem.oc.monitorWindow = new MonitorVO();
							return null;
						}

						@Override
						public void finished() {
							stopWaiting();
						}

					};
					startWaiting("Launching all Experiments", "Please wait few seconds", worker);
					worker.start();
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
			ExperimentSummaryModel experimentSummaryModel = (ExperimentSummaryModel)queueJobList.get(n);
			ExperimentModel experimentModel = AiravataManager.getExperiment(experimentSummaryModel.getExperimentId());
			if(experimentModel!=null){
				jobEditor = new EditJobPanel(null, experimentModel);
			}
		}
	}

	public void doCreateNewProject() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				NewProjDialog dialog = new NewProjDialog(new javax.swing.JFrame(), true);
				dialog.setLocationRelativeTo(null);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

	public void doEditNewExperiment() {
		jobEditor = new EditJobPanel();
	}

	public void doEditNewJob(ExperimentSummaryModel experiment) {
		ExperimentModel experimentModel = AiravataManager.getExperiment(experiment.getExperimentId());
		if(experimentModel != null){
			jobEditor = new EditJobPanel(null, experimentModel);

			int size = queueModel.getSize();

			queueList.setSelectedIndex(size + 1);

			System.err.println("job index is: " + (size + 1));
		}

	}

	public void doDeleteExperiment() {

		try{
			int[] indices = queueList.getSelectedIndices();

			for (int i = 0; i < indices.length; i++) {
				ExperimentSummaryModel experimentModel = SubmitJobsWindow.si.queueJobList.get(indices[i]);
				AiravataManager.deleteExperiment(experimentModel.getExperimentId());
				update();
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}

	}

	public void doSubmitAllExperiment(){
		try {
			ExperimentHandler experimentHandler = new ExperimentHandler();
			while(queueList.getModel().getSize()>0){
				ExperimentSummaryModel experiment = SubmitJobsWindow.si.queueJobList.get(0);
				experimentHandler.launchExperiment(experiment.getExperimentId());
				update();
			}
		}catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainFrame, "Error at launching experiment",
					e.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
	}

	public void doSubmitExperiments(){
		try {
			ExperimentHandler experimentHandler = new ExperimentHandler();
			ArrayList<String> expIds = new ArrayList();
			int[] indices = queueList.getSelectedIndices();
			for(int index : indices){
				ExperimentSummaryModel experiment = SubmitJobsWindow.si.queueJobList.get(index);
				expIds.add(experiment.getExperimentId());
			}
			for(String expId : expIds) {
				experimentHandler.launchExperiment(expId);
				update();
			}
		}catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(mainFrame, "Error at launching experiment",
					e.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
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

}
