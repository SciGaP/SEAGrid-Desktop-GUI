/* 
 * Created on May 5, 2008
 * 
 * Developed by: Rion Dooley - dooley [at] tacc [dot] utexas [dot] edu
 * 				 TACC, Texas Advanced Computing Center
 * 
 * https://www.tacc.utexas.edu/
 */

package org.gridchem.client.gui.jobsubmission;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.ToolTipManager;
import javax.swing.Box.Filler;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicArrowButton;

import nanocad.nanocadFrame2;
import nanocad.newNanocad;

import org.gridchem.client.FileUtilities;
import org.gridchem.client.GridChem;
import org.gridchem.client.InputInfoPanel;
import org.gridchem.client.Invariants;
import org.gridchem.client.SubmitJobsWindow;
import org.gridchem.client.Trace;
import org.gridchem.client.common.Preferences;
import org.gridchem.client.common.Settings;
import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.exceptions.CharmmInputFileParsingException;
import org.gridchem.client.gui.jobsubmission.commands.GETHARDWARECommand;
import org.gridchem.client.gui.panels.myccg.MonitorVO;
import org.gridchem.client.util.Env;
import org.gridchem.client.util.GMS3;
import org.gridchem.client.util.file.CharmmInputFileParser;
import org.gridchem.client.util.file.FileUtility;
import org.gridchem.service.beans.ComputeBean;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.LogicalFileBean;
import org.gridchem.service.beans.ProjectBean;
import org.gridchem.service.beans.QueueBean;
import org.gridchem.service.beans.SoftwareBean;
import org.gridchem.service.exceptions.JobException;
import org.gridchem.service.model.enumeration.AccessType;


/**
 * Reimplementation of editingStuff panel into a JDialog so it can be
 * called independently of the SubmitJobsWindow. 
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class EditJobPanel extends JDialog implements ActionListener,
WindowListener, ComponentListener {

    public static final String SCHEDULER = "Grid Scheduler";
    public static final String UNSPECIFIED = "Unspecified";
    public static final Hashtable<String, HashSet> APP_MODULE_HASHTABLE= new Hashtable<String, HashSet>();
    public static final HashSet<String> APP_NAME_HASHSET = new HashSet<String>();
    
    private JPanel rightPanel = new JPanel();
    private JPanel leftPanelForchoicesBox = new JPanel();
    private JPanel reqPane = new JPanel();
    private JPanel timePanel = new JPanel();
    private JPanel buttonBox = new JPanel();
    
    private InputFilePanel inputFilePanel;
    
    private BasicArrowButton upButton;
    private BasicArrowButton downButton;
    
    private JTextField jobNameText;
    private JTextField projNameText;

    private GridBagLayout rpgbl = new GridBagLayout();
    private GridBagConstraints gbcons = new GridBagConstraints();

    private JLabel numProcEdLabel;
    private JLabel psnLabel;
    private JLabel qLabel;
    private JLabel timeLable;
    private JLabel appModuleLabel;
    private JLabel memSizeLabel;
    
    private JComboBox projCombo;
    private JComboBox qCombo;
    private JComboBox appCombo;
    private JComboBox appModuleCombo;

    private JSpinner hr; // Seconds removed
    private JSpinner min;
    private JSpinner numProcSpin = new JSpinner();

    private SpinnerNumberModel hrnm, minnm;
    private SpinnerNumberModel numProcnm;

    private JButton edbumolButton;
    private JButton OKButton;
    private JButton CancelButton;
    
    private JTextField memSizeTextField;
    
    private JList hpcList;
    
    private DefaultListModel hpcListModel;

    private JScrollPane apphpcScrollPane;

    protected JobBean job; // the job being edited
    
    private nanocadFrame2 nanWin;
    
    private Object timeInputText = "";
    
    private String application = Invariants.APP_NAME_GAUSSIAN; // default app is Gaussian
    
    private boolean validTime = true;
    private boolean loadingQueues = false;
    
    private boolean isLoading = false;
    
    private boolean isUpdating = false;
    
    private boolean isSubmittingScript = false;
    
    private static Set<String> dsLmpUserIDSet = new HashSet<String>();
    
    private Preferences preferences = Preferences.getInstance();
    
    /**
     * Create a new job.
     * 
     * @throws HeadlessException
     */
    public EditJobPanel() throws HeadlessException {
        super();
        
        if (dsLmpUserIDSet.isEmpty()) { 
        	dsLmpUserIDSet.add("x_baya");
        	dsLmpUserIDSet.add("spamidig");
        	dsLmpUserIDSet.add("mvanmoer");
        	dsLmpUserIDSet.add("sxc033");
        	dsLmpUserIDSet.add("dspearot");
        }
        
        this.job = new JobBean();
        this.job.setName("default_job");
        this.job.setExperimentName(GridChem.user.getUserName() + "_proj");
        
        ComputeBean hw = GridChem.getMachineList().get(0);
        for (ComputeBean cb : GridChem.getMachineList()) {
        	System.out.println("*******************************");
        	System.out.println(cb.getName());
        	if (cb.getName().equals(Preferences.getString("last_machine"))) {
        		System.out.println("Found last used machine");
        		hw = cb;
        	}
        }
        
        this.job.setSystemName(hw.getName());
        //SoftwareBean sw = GridChem.getSoftwareforMachine(hw.getName()).get(0);
        SoftwareBean sw = GridChem.getSoftwareforMachine(hw.getName()).get(0);
        for (SoftwareBean cb : GridChem.getSoftwareforMachine(hw.getName())) {
        	System.out.println("*******************************");
        	System.out.println(cb.getName());
        	if (cb.getName().equals(Preferences.getString("last_app"))) {
        		System.out.println("Found last used application");
        		sw = cb;
        	}
        }
        
        for (String modName : getModuleList(sw.getName())) {
        	if (modName.equals(Preferences.getString("last_module"))) {
        		System.out.println("Found last used module");
        		this.job.setModuleName(modName);
        	}
        }
        this.application = sw.getName();
        this.job.setSoftwareName(this.application);
        this.job.setAllocationName(hw.getAllocations().iterator().next());
        this.job.setQueueName(hw.getQueues().get(0).getName());
        this.job.setRequestedCpus(new Long(1));
        
        ArrayList<LogicalFileBean> inFiles = new ArrayList<LogicalFileBean>();
        for(File f: FileUtility.getDefaultInputFiles(this.application)) {
            LogicalFileBean lf = new LogicalFileBean();
            lf.setJobId(-1);
            lf.setLocalPath(f.getAbsolutePath());
            inFiles.add(lf);
        }
        this.job.setInputFiles(inFiles);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.add(Calendar.MINUTE,30);
        
        this.job.setRequestedCpuTime(cal);
        
        init();
        
    }

    public EditJobPanel(String input, String appName) {
    	this();
    	
        appCombo.setSelectedItem(appName);
        changeModuleList(GridChem.getSoftware((String) (appName)));
        populateMachineList(appName);
        
        this.inputFilePanel.addTextInput(input);
    }
    
    public EditJobPanel(ArrayList<File> files) {
        this();
        
        this.inputFilePanel.addMultipleFileInput(files);
    }
    
    /**
     * Edit an existing job.
     * 
     * @param owner
     * @throws HeadlessException
     */
    public EditJobPanel(Frame owner, JobBean job) throws HeadlessException {
        super(owner);
        
        this.job = job;
        
        this.isUpdating = true;
        
        init();
        
    }
    
    public EditJobPanel(Frame owner, JobBean job, ArrayList<File> files) throws HeadlessException {
        this(owner,job);
     
        this.inputFilePanel.clearFileInput();
        
        this.inputFilePanel.addMultipleFileInput(files);
    }
    
    private void init() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        jbInit();
        
        try {
            isLoading = true;
            // populate fields with given job information
            changeJobNameField(job.getName());
            changeResearchProjectNameField(job.getExperimentName());
//            changeAppPackage(job.getSystemName());
//            changeModule(job.getApplication());
            populateMachineList(job.getSoftwareName());

            System.out.println("Loading machine " + job.getSystemName());
            ComputeBean hpc = GridChem.getMachineByName(job.getSystemName());

            String machineName = "";
            if (hpc != null) {
                System.out
                        .println("Found machine in user's VO: "
                                + job.getSystemName() + " = "
                                + hpc.getHostname());
                machineName = hpc.getName();
            } else {
                
                machineName = (String)hpcListModel.get(hpcList.getSelectedIndex());
                
                System.out.println("Did not find machine in user's VO: "
                        + job.getSystemName());

                JOptionPane.showMessageDialog(null,
                        "The machine associated with this job\n"
                                + "is no longer available. Please\n"
                                + "select a different machine.",
                        "Resubmission Error", JOptionPane.INFORMATION_MESSAGE);
            }

            changeMachine(machineName);

            //populateProjects(machineName);

            //changeProject(job.getProjectName());
            
            populateQueues(machineName);

            changeQueue(job.getQueueName());

            changeNumProc(((job.getRequestedCpus() == null)?0:job.getRequestedCpus().intValue()));

//            updateInputInfoPanel(job,FileUtility.getDefaultInputFiles(job.getApplication()));

            System.out.println("Updated editing stuff with values for job "
                    + job.getName());
            
            
            
            isLoading = false;
            
            this.inputFilePanel.addMultipleLogicalFileInput(job.getInputFiles());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void jbInit() {
        
//        verifyInputsForMultipleInputApp();
        
        // Border
        Border eBorder1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border eBorder2 = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        Border leBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        // left panel for choicesBoxs
        // --->namePane
        jobNameText = new JTextField(20);
        jobNameText.setText(this.job.getName());
        projNameText = new JTextField(20);
        projNameText.setText(this.job.getExperimentName());
        JLabel jobNameLabel = new JLabel("Job name: ");
        jobNameLabel.setLabelFor(jobNameText);
        JLabel projNameLabel = new JLabel("Research project name: ");
        projNameLabel.setLabelFor(projNameText);
        JPanel namePane = new JPanel();
        
        TitledBorder namePaneTitled = BorderFactory.createTitledBorder(
                leBorder, "Project/Job name", TitledBorder.LEFT,
                TitledBorder.DEFAULT_POSITION, new Font("Sansserif", Font.BOLD,14));

        namePane.setBorder(BorderFactory.createCompoundBorder(namePaneTitled,
                eBorder2));
        namePane.setLayout(new GridLayout(2, 2));
        namePane.add(projNameLabel);
        namePane.add(projNameText);
        namePane.add(jobNameLabel);
        namePane.add(jobNameText);
        
        
        // ---> AppPane
        Container apphpcBox = Box.createVerticalBox();
        hpcListModel = new DefaultListModel();
        hpcList = new JList(hpcListModel);
        //this.job.setSoftwareName(Invariants.APP_NAME_GAUSSIAN);
        //populateMachineList(Invariants.APP_NAME_GAUSSIAN);
        //this.job.setSystemName("Cobalt");
        System.out.println("312:init editingstuff=" + this.job.getSystemName());
        if (this.job.getSoftwareName() == null) {
            System.out.println("get app is blank");
            populateMachineList(Invariants.APP_NAME_GAUSSIAN);
        } else {
            //populateMachineList(this.job.getSystemName());
            populateMachineList(this.job.getSoftwareName());
            changeMachine(this.job.getSystemName());
            // reqPane.remove(numProcEdLabel);
        }
        machListSelectionListener ms = new machListSelectionListener();
        // apphpcBoard.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        hpcList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        hpcList.setCellRenderer(new HPCCellRenderer());

        apphpcScrollPane = new JScrollPane(hpcList);
        apphpcBox.setMinimumSize(new Dimension(250, 100));
        apphpcBox.setPreferredSize(new Dimension(250, 150));
        apphpcBox.add(apphpcScrollPane);
        // apphpcBoard.setSelectedIndex(0);
        
        int selectedIndices = ((DefaultListModel)hpcList.getModel()).indexOf(this.job.getSystemName());
        hpcList.setSelectedIndex(selectedIndices);
//        hpcList.setSelectedIndices(new int[]{selectedIndices});
        hpcList.ensureIndexIsVisible(selectedIndices);
    
        makeButtons();
        JPanel arrowButtonPane = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER; // next-to-last
        c.fill = GridBagConstraints.VERTICAL; // reset to default
        c.anchor = GridBagConstraints.SOUTH;
        c.weightx = 1.0;
        gridbag.setConstraints(upButton, c);
        arrowButtonPane.add(upButton);
        c.gridwidth = GridBagConstraints.REMAINDER; // end row
        c.fill = GridBagConstraints.VERTICAL;
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 1.0;
        gridbag.setConstraints(downButton, c);
        arrowButtonPane.add(downButton);
        arrowButtonPane.setLayout(gridbag);
        arrowButtonPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JPanel apphpcBoxPane = new JPanel();
        apphpcBoxPane.setLayout(new BoxLayout(apphpcBoxPane, BoxLayout.X_AXIS));
//        apphpcBoxPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        apphpcBoxPane.add(apphpcBox);
        apphpcBoxPane.add(arrowButtonPane);

        // create applications combo box + application moddules combo box (JK)
//        getAppPackageAndModuleListFromDatabase();
        
        appModuleLabel = new JLabel("Module"); 

        if (Settings.WEBSERVICE) {
        	List<SoftwareBean> software = GridChem.getSoftware();
        	List<String> softwareNames = new ArrayList<String>();
        	for (SoftwareBean bean: software) {
        		softwareNames.add(bean.getName());
        	}
            appCombo = new JComboBox(softwareNames.toArray());
            appCombo.setSelectedItem(job.getSoftwareName());
            
            
            if (job.getSoftwareName().equals("Lammps")) {
         		if (dsLmpUserIDSet.contains(GridChem.user.getUserName())) {
         			appModuleCombo = new JComboBox(getModuleList(job.getSoftwareName()));
        		} else {        			
        			appModuleCombo = new JComboBox(new String[] {"lmp"});
        			
        		}
        	} else {
        		appModuleCombo = new JComboBox();
        		changeModuleList(GridChem.getSoftware(job.getSoftwareName()));
        	}
            
            //appModuleCombo = new JComboBox(getModuleList(job.getSoftwareName()));
            //appModuleCombo.setSelectedIndex(0);
            if ((job.getModuleName() != null) && (!job.getModuleName().equals(""))) {
            	appModuleCombo.setSelectedItem(job.getModuleName());
            } else {
            	appModuleCombo.setSelectedIndex(0);
            }
        } else {
            String[] appItems = GridChem.getAvailableApplications();
            appCombo = new JComboBox(appItems);
            appModuleCombo = new JComboBox(getModuleList(this.job.getSystemName())); 
        }
       
        appCombo.setPreferredSize(new Dimension(50, 30));
        appModuleCombo.setPreferredSize(new Dimension(50, 30));
        
        System.out.println("374:name of HPC System: " + this.job.getSystemName());
   
        appCombo.addItemListener(new appComboListener());
        appModuleCombo.addItemListener(new appModuleComboListener());

        JPanel appPane = new JPanel();
        TitledBorder appPaneTitled = BorderFactory.createTitledBorder(leBorder,
                "Application", TitledBorder.LEFT,
                TitledBorder.DEFAULT_POSITION, new Font("Sansserif", Font.BOLD,
                        14));
        appPane.setBorder(BorderFactory.createCompoundBorder(appPaneTitled,
                eBorder2));
        appPane.setLayout(new BoxLayout(appPane, BoxLayout.X_AXIS));
        appPane.add(appCombo);
        appPane.add(Box.createRigidArea(new Dimension(30, 0)));
        appPane.add(appModuleLabel); // JK
        appPane.add(appModuleCombo); // JK
        
        // Add another title for systems
        JPanel HPCsysPane = new JPanel();
        TitledBorder appPaneTitled1 = BorderFactory.createTitledBorder(
                leBorder, "HPC Systems", TitledBorder.LEFT,
                TitledBorder.DEFAULT_POSITION, new Font("Sansserif", Font.BOLD,
                        14));
        apphpcBoxPane.setBorder(BorderFactory.createCompoundBorder(appPaneTitled1,
                eBorder2));
//        HPCsysPane.setLayout(new BoxLayout(HPCsysPane, BoxLayout.X_AXIS));
//        HPCsysPane.add(apphpcBoxPane);

        psnLabel = new JLabel("Choose a project:");
        
        /*if (Settings.WEBSERVICE) {
        	// trying to set projects for default app and system. Sudhakar
        	projCombo = new JComboBox(getMachineProjects(this.job.getSystemName()));
        	//projCombo = new JComboBox(GridChem.getMachineProjects(this.job.getSystemName()));
        } else {
        	String[] projItems = GridChem.getMachineProjects(this.job.getSystemName());
        	projCombo = new JComboBox(projItems);
        }*/
    	projCombo = new JComboBox();
        
        populateProjects(hpcList.getSelectedValue().toString());
        if ((this.job.getAllocationName()!=null) && (!this.job.getAllocationName().equals(""))) {
        	projCombo.setSelectedItem(this.job.getAllocationName());
        }
        projCombo.setEditable(true);

        // create queue drop down box
        qLabel = new JLabel("Choose a queue:");
        qCombo = new JComboBox();
        /*if (Settings.WEBSERVICE) {
            //qCombo = new JComboBox(getMachineQueues());
            //System.out.println("wahahaahahhahahahahahah");
        } else {
            String[] qItems = GridChem.getMachineQueues(this.job.getSystemName());
            qCombo = new JComboBox(qItems);
        }*/
        populateQueues(hpcList.getSelectedValue().toString());
        qCombo.setEditable(true);
        qCombo.setRenderer(new QueueComboBoxRenderer());

        // create time wall clock time spinner boxes
        timeLable = new JLabel("Est. walltime (hr:min):");

        int maximum = 2048, maxmint = 59, minimum = 0, initial = 0, step = 1; 

        if (this.job != null) {
        	Calendar timewallCal = this.job.getRequestedCpuTime();
        	Calendar baseCal = Calendar.getInstance();
            baseCal.clear();
            int diffInMinutes = (int)(timewallCal.getTimeInMillis() - baseCal.getTimeInMillis())/1000/60;
            int tHours = diffInMinutes / 60;
            int tMins = diffInMinutes % 60;
            hrnm = new SpinnerNumberModel(tHours, 0, 60, 1);
            minnm = new SpinnerNumberModel(tMins, 0, 59, 1);
        } else {
        	hrnm = new SpinnerNumberModel(0, 0, 60, 1);
        	initial = 30;
            minnm = new SpinnerNumberModel(30, 0, 59, 1);
        }
        // hrnm.addChangeListener(tscl);
        hr = new JSpinner(hrnm);
        ((JSpinner.NumberEditor) hr.getEditor()).getTextField()
                .setInputVerifier(new HourFieldVerifier());
        min = new JSpinner(minnm);
        ((JSpinner.NumberEditor) min.getEditor()).getTextField()
                .setInputVerifier(new MinuteFieldVerifier());
        // insert keyboard action event event to validate the text entered, so
        // we can tell them when
        // they enter a bad value and it is reset by the spinnernumbermodel
        GridBagLayout timeGridBag = new GridBagLayout();
        GridBagConstraints timeConstraints = new GridBagConstraints();
        timePanel = new JPanel();
        timePanel.setLayout(timeGridBag);
        timeConstraints.weightx = 1.0;
        timeConstraints.gridx = 0; // next-to-last
        timeConstraints.fill = GridBagConstraints.HORIZONTAL;
        timePanel.add(hr, timeConstraints);
        timeConstraints.weightx = 0.25;
        timeConstraints.gridx = 1; // next-to-last
        timeConstraints.fill = GridBagConstraints.NONE;
        JLabel timeSeparator = new JLabel(":");
        timeSeparator.setPreferredSize(new Dimension(15, 15));
        timePanel.add(timeSeparator, timeConstraints);
        timeConstraints.weightx = 1.0;
        timeConstraints.gridx = 2;
        timeConstraints.fill = GridBagConstraints.HORIZONTAL;
        timePanel.add(min, timeConstraints);
        
        /*final JTextComponent jTextComponent = (JTextComponent) projCombo.getEditor().getEditorComponent();
        jTextComponent.getDocument().addDocumentListener(new DocumentListener() {
			
			public void removeUpdate(DocumentEvent e) {
				job.setAllocationName(projCombo.getEditor().getItem().toString());
			}
			
			public void insertUpdate(DocumentEvent e) {
				job.setAllocationName(projCombo.getEditor().getItem().toString());
			}
			
			public void changedUpdate(DocumentEvent e) {
				job.setAllocationName(projCombo.getEditor().getItem().toString());
			}
		});*/
        
        projCombo.addActionListener(new ActionListener() {
        	
        	public void actionPerformed(ActionEvent e){        		 
        	  // changeProjectid 
        	   String selp = getSelectedProj();
         	   int selectedMachineIndex = hpcList.getSelectedIndex();
        	   String hpcName = (String) hpcListModel.get(selectedMachineIndex);
        	   job.setHostName(hpcName);
        	   ComputeBean hpc = GridChem.getMachineByName(hpcName);
        	   System.out.println("Setting project selection for job for "+hpcName);
        	   if (!hpcName.equals("Grid Scheduler")) {
        	    	 for (String p : hpc.getAllocations()) {
        	             if (p.equals((String) projCombo.getSelectedItem())) {
        	                 selp = p;
        	             }
        	    	 }
        	    	 //String currproj = projCombo.getSelectedItem().toString();
        	    	 job.setAllocationName(selp); //currproj);
        	    	 System.out.println("projCombo: The Project for this job is "+selp); //currproj);
        	   }
        	}
        });
        System.out.println("ProjComboaction: The Project for this job is "+job.getAllocationName());
        
        
        qCombo.addActionListener(new ActionListener() {
        	
            public void actionPerformed(ActionEvent e) {
                QueueBean q = getSelectedQueue();
                 
                
                if (q != null) {
                    Calendar qWallLimit = q.getMaxWallClockTime();

                    // hour spinner
                    ((SpinnerNumberModel) ((JSpinner) timePanel.getComponent(0))
                            .getModel()).setMaximum(new Integer(
                            getIntegerHours(qWallLimit)));

                    // minute spinner
                    if (qWallLimit.get(Calendar.MINUTE) > 0) {
                        ((SpinnerNumberModel) ((JSpinner) timePanel
                                .getComponent(2)).getModel())
                                .setMaximum(new Integer(qWallLimit
                                        .get(Calendar.MINUTE)));
                    } else {
                        ((SpinnerNumberModel) ((JSpinner) timePanel
                                .getComponent(2)).getModel())
                                .setMaximum(new Integer(59));
                    }

                    // cpu spinner
                    ((SpinnerNumberModel) numProcSpin.getModel())
                            .setMaximum(new Integer(getSelectedQueue()
                                    .getMaxCpus()));

                    if (!loadingQueues) {
                        validateTimeLimit(q);
                        validateCpuLimit(q);
                    }
                }
            }
        
 
            
            
            protected void validateCpuLimit(QueueBean q) {

                if (q != null) {
                    // make sure it's a valid value in the range
                    // 1-q.getMaxCpus()
                    if (numProcnm.getNumber().intValue() > q.getMaxCpus()) {
                        JOptionPane
                                .showMessageDialog(
                                        null,
                                        "The requested number of CPU's, "
                                                + numProcnm.getNumber()
                                                + ", exceeds "
                                                + "\nthe maximum number of processors allowed"
                                                + "\non the "
                                                + getSelectedQueue()
                                                + " queue of "
                                                + hpcList
                                                        .getSelectedValue()
                                                        .toString()
                                                + ". The requested\nCPU field will be reset to "
                                                + "the maximum CPU\ncount of the "
                                                + getSelectedQueue()
                                                + " queue.",
                                        "Requested CPU Error",
                                        JOptionPane.OK_OPTION);

                        System.out.println("The requested number of CPU's, "
                                + numProcnm.getNumber() + ", exceeds "
                                + "\nthe maximum number of processors allowed"
                                + "\non the " + getSelectedQueue()
                                + " queue of "
                                + hpcList.getSelectedValue().toString()
                                + ". The requested\nCPU field will be reset to"
                                + "the maximum\nCPU count of the "
                                + getSelectedQueue() + " queue.");

                        // reset the value
                        numProcnm.setValue(new Integer(q.getMaxCpus()));

                        // select the text
                        ((NumberEditor) numProcSpin.getEditor()).getTextField()
                                .selectAll();

                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a value in the range [1:"
                                    + q.getMaxCpus() + "].",
                            "Queue Wall Time Error", JOptionPane.OK_OPTION);

                    // reset the value
                    numProcnm.setValue(new Integer(q.getMaxCpus()));

                    // select the text
                    ((NumberEditor) numProcSpin.getEditor()).getTextField()
                            .selectAll();
                }
            }

            protected void validateTimeLimit(QueueBean q) {

                Calendar qWallLimit = q.getMaxWallClockTime();

                // save the current values
                double hours = hrnm.getNumber().doubleValue()
                        + minnm.getNumber().doubleValue() / 60;

                // reset the previous value to that value or the limit for the
                // current qdto.

                if (hours > getDoubleHours(qWallLimit)) {

                    try {
                        JOptionPane.showMessageDialog(null,
                                "Current wall time value of "
                                        + hrnm.getNumber().intValue()
                                        + ":"
                                        + minnm.getNumber().intValue()
                                        + " hours exceeds "
                                        + "\nthe maximum queue wall time of "
                                        + resolveTimeLimit(qWallLimit)
                                        + " hours\non "
                                        + hpcList.getSelectedValue()
                                                .toString() + "'s "
                                        + getSelectedQueue()
                                        + " queue. Time will be reset"
                                        + "\nto the maximum time limit of the "
                                        + getSelectedQueue() + "\nqueue.",
                                "Queue Wall Time Error", JOptionPane.OK_OPTION);

                        System.out.println("Current wall time value of "
                                + hrnm.getNumber().intValue()
                                + ":"
                                + minnm.getNumber().intValue()
                                + " hours exceeds "
                                + "\nthe maximum queue wall time of "
                                + resolveTimeLimit(qWallLimit)
                                + " hours\non "
                                + hpcList.getSelectedValue().toString()
                                + "'s "
                                + getSelectedQueue()
                                + " queue. Time will be reset"
                                + "\nto the maximum time limit of the "
                                + getSelectedQueue()
                                + "\nqueue."
                                + " which is "
                                + new String(getIntegerHours(qWallLimit) + ":"
                                        + qWallLimit.get(Calendar.MINUTE)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // reset the hour time to the max hour value for the queue
                    hrnm.setValue(new Integer(getIntegerHours(qWallLimit)));

                    // select the text
                    ((NumberEditor) hr.getEditor()).getTextField().selectAll();

                    // if the minute time is over, reset that as well to the max
                    // minute value
                    if (minnm.getNumber().intValue() > qWallLimit
                            .get(Calendar.MINUTE)) {
                        minnm.setValue(new Integer(0));
                    }
                }
            }
        });

        // create processor count spinner box
        minimum = 1;
        maximum = getSelectedQueue().getMaxCpus();
        initial = 1;
        numProcEdLabel = new JLabel("Number of Processors:");
        numProcnm = new SpinnerNumberModel(initial, minimum, maximum, step);
        numProcSpin.setModel(numProcnm);
        numProcSpin.setInputVerifier(new NumProcFieldVerifier());

        // Create Memory Configuration
        memSizeLabel = new JLabel("Preferred Memory (Mbytes):");
        memSizeTextField = new JTextField("1000");

        // create layout for requirements panel
        layoutRequirementsPane();

        //changeNumProc(this.job.getRequestedCpus().intValue());

        leftPanelForchoicesBox = new JPanel();
        
        namePane.setMaximumSize(new Dimension(350,200));
        appPane.setMaximumSize(new Dimension(350,200));
        apphpcBoxPane.setMaximumSize(new Dimension(350, 255));
        leftPanelForchoicesBox.setPreferredSize(new Dimension(350, 700));
//        HPCsysPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftPanelForchoicesBox.setLayout(new BoxLayout(leftPanelForchoicesBox, BoxLayout.Y_AXIS));
//        leftPanelForchoicesBox.setBorder(eBorder1);
        leftPanelForchoicesBox.add(namePane);
        leftPanelForchoicesBox.add(appPane);
        leftPanelForchoicesBox.add(apphpcBoxPane);
        leftPanelForchoicesBox.add(reqPane);
        leftPanelForchoicesBox.add(new Box.Filler(new Dimension(0,0), new Dimension(0,1), new Dimension(0,Short.MAX_VALUE)));
        
        // right panel for inputInfoPanel and buttonBox
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
//        rightPanel.setBorder(eBorder1);
        
//        GridBagConstraints rightPanelConstraints = new GridBagConstraints();
        
        // inputFilePanel
        System.out.println("\n(DEBUG) # of inputs: " + this.job.getInputFiles().size());
        for(LogicalFileBean lFile: this.job.getInputFiles()) {
            System.out.println("\n(DEBUG) inputFile :"+ lFile.getLocalPath());
            System.out.println("\n(DEBUG) inputLength :\n"+ new File(lFile.getLocalPath()).length());
        }
        
        inputFilePanel = new InputFilePanel(this);
        
        // We do not add defaut input files to inputFilePanel if isUpdating is true (editing a job) 
        if (this.isUpdating == false) {
        	inputFilePanel.addMultipleFileInput(FileUtility.getDefaultInputFiles(this.job.getSoftwareName()));
        }
        
        inputFilePanel.setMinimumSize(new Dimension(250,700));
        TitledBorder inputInfoTitled = BorderFactory.createTitledBorder(
                leBorder, "Input File Information", TitledBorder.LEFT,
                TitledBorder.DEFAULT_POSITION, new Font("Sansserif", Font.BOLD,14));
        inputFilePanel.setBorder(BorderFactory.createCompoundBorder(inputInfoTitled, 
                BorderFactory.createEmptyBorder(5, 0, 0, 0)));

        // buttonBox
        OKButton = new JButton("Submit");
        CancelButton = new JButton("Cancel");
        // defaultButton = new JButton("Create Default Job");
        // loadButton = new JButton("Load");
        // saveButton = new JButton("Save");
        edbumolButton = new JButton("Edit/Build");

        Dimension minSize = new Dimension(5, 0);
        Dimension prefSize = new Dimension(5, 0);
        Dimension maxSize = new Dimension(Short.MAX_VALUE, 0);
        buttonBox = new JPanel();
//        buttonBox.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 10));
        buttonBox.setLayout(new BoxLayout(buttonBox,BoxLayout.X_AXIS));
        buttonBox.add(new Box.Filler(minSize, prefSize, maxSize));
        buttonBox.add(edbumolButton);
        buttonBox.add(OKButton);
        buttonBox.add(CancelButton);
        
        rightPanel.add(inputFilePanel);
        rightPanel.add(buttonBox);
        
        // layout with gridbag --- too much space surrounding req pane when readjust
        setLayout(new GridBagLayout());
//        
        GridBagConstraints constraint = new GridBagConstraints();
//        constraint.fill = GridBagConstraints.VERTICAL;
////        constraint.anchor = GridBagConstraints.FIRST_LINE_START;
//        constraint.weightx = 0.0;
        constraint.gridx = 0;
        constraint.gridy = 0;
//        add(leftPanelForchoicesBox,constraint);
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 1.0;
        constraint.weighty = 1.0;
//        constraint.gridx = 1;
//        constraint.gridy = 0;
//        add(rightPanel,constraint);
        
        // layout with boxlayout
        JPanel layoutPanel = new JPanel();
        layoutPanel.setLayout(new BoxLayout(layoutPanel,BoxLayout.X_AXIS));
        layoutPanel.add(leftPanelForchoicesBox);
        layoutPanel.add(rightPanel);
        layoutPanel.setMinimumSize(new Dimension(750,500));
        layoutPanel.setPreferredSize(new Dimension(750,500));
        this.add(layoutPanel,constraint);
//      
        // Add all the action listeners here
        edbumolButton.addActionListener(this);
        // loadButton.addActionListener(this);
        // saveButton.addActionListener(this);
//      defaultButton.addActionListener(this);
        OKButton.addActionListener(this);
        CancelButton.addActionListener(this);

        hpcList.addListSelectionListener(ms);

        // make the mouseover tool tips to appear immediately
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setReshowDelay(0);

        // make the tool tips appear until the mouse is removed
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        
        pack();
        setVisible(true);
    }
    
    
   private String[] getMachineProjects(String hpcName) {
		// TODO Auto-generated method stub
	   	System.out.println("HPC name is: " + hpcName);
        ArrayList<String> projects = new ArrayList<String>();
        //for (ComputeBean hpc : GridChem.systems) {
        System.out.println("GetMachineProjects for the HPC System "+hpcName);
        ComputeBean hpcBean = GridChem.getMachineByName(hpcName);
        String[] a = {};
        System.out.println("computebean stuff "+hpcBean);
        	// TODO: need to get the remote allocation names available to the user on the remote machine.
        //hpc.getAllocations().toArray(a);
        
       /* try { */
            for (String allocation : hpcBean.getAllocations()) {
                //if (projects.indexOf(allocation) == -1) {
                    projects.add(allocation);
                //}
            } 
        
        /* }finally  {
            System.out.println("getMachineProjects 868 nullpointer execption");
        	projects.add("cqj");
        
        } 	
        */
        
      
       
        System.out.println("getMachineProjects(hpcName): Found " + projects.size() + " projects "+ projects );
        
        
       
        return projects.toArray(a);

		//return null;
	}

	private void layoutRequirementsPane() {

        Border eBorder2 = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        Border leBorder = BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED);
        TitledBorder reqPaneTitled = BorderFactory.createTitledBorder(leBorder,
                "Requirements", TitledBorder.LEFT,
                TitledBorder.DEFAULT_POSITION, new Font("Sansserif", Font.BOLD,
                        14));

        // clear the requirements pane and reset the container
        reqPane.removeAll();

        reqPane.setBorder(BorderFactory.createCompoundBorder(reqPaneTitled,
                eBorder2));
        reqPane.setLayout(rpgbl);
        gbcons.fill = GridBagConstraints.BOTH;
        gbcons.weightx = 1.0;
        gbcons.gridwidth = GridBagConstraints.RELATIVE;
        gbcons.gridx = 0;
        rpgbl.setConstraints(psnLabel, gbcons);
        if (Settings.userType.equals(AccessType.EXTERNAL)){
        reqPane.add(psnLabel);
        }
        gbcons.gridwidth = GridBagConstraints.REMAINDER;
        gbcons.gridx = 1;
        rpgbl.setConstraints(projCombo, gbcons);
        // If the user authenticated is a GridChem Community User then do not add projCombo
        if (Settings.userType.equals(AccessType.EXTERNAL)){
        reqPane.add(projCombo);
        }
        gbcons.weightx = 1.0;
        gbcons.gridwidth = GridBagConstraints.RELATIVE;
        gbcons.gridx = 0;
        rpgbl.setConstraints(qLabel, gbcons);
        reqPane.add(qLabel);
        gbcons.gridwidth = GridBagConstraints.REMAINDER;
        gbcons.gridx = 1;
        rpgbl.setConstraints(qCombo, gbcons);
        reqPane.add(qCombo);
        gbcons.weightx = 0.0;
        gbcons.gridwidth = GridBagConstraints.RELATIVE;
        gbcons.gridx = 0;
        rpgbl.setConstraints(timeLable, gbcons);
        reqPane.add(timeLable);
        gbcons.gridwidth = GridBagConstraints.REMAINDER;
        gbcons.gridx = 1;
        rpgbl.setConstraints(timePanel, gbcons);
        reqPane.add(timePanel);

        // here we display the number of processors
        // differently depending on the application
        if (getAppPackageName().equalsIgnoreCase(Invariants.APP_NAME_GAUSSIAN)) {
            numProcEdLabel
                    .setText("Use %NprocShared(SMP)/%NProcLinda(Clusters) in the G09 input");
            numProcSpin.setValue(4);// This is a dummy value and will be reset
                                    // in getnumProc method Sudhakar
            memSizeLabel.setText("Use %mem in the G09 input");
            memSizeTextField.setText("1000");
            gbcons.weightx = 1.0;
            gbcons.fill = GridBagConstraints.HORIZONTAL;
            gbcons.gridx = 0;
            rpgbl.setConstraints(numProcEdLabel, gbcons);
            reqPane.add(numProcEdLabel);

        } else {
            numProcEdLabel.setText("Number of Processors:");
            memSizeLabel.setText("Preferred Memory (Mbytes):");
            gbcons.weightx = 0.0;
            gbcons.gridwidth = GridBagConstraints.RELATIVE;
            gbcons.gridx = 0;
            rpgbl.setConstraints(numProcEdLabel, gbcons);
            reqPane.add(numProcEdLabel);
            gbcons.gridwidth = GridBagConstraints.REMAINDER;
            gbcons.gridx = 1;
            rpgbl.setConstraints(numProcSpin, gbcons);
            reqPane.add(numProcSpin);
        }
        
        gbcons.weightx = 0.0;
        gbcons.gridwidth = GridBagConstraints.RELATIVE;
        gbcons.gridx = 0;
        rpgbl.setConstraints(memSizeLabel, gbcons);
        reqPane.add(memSizeLabel);
        gbcons.gridwidth = GridBagConstraints.REMAINDER;
        gbcons.gridx = 1;
        rpgbl.setConstraints(memSizeTextField, gbcons);
        reqPane.add(memSizeTextField);

        reqPane.setMaximumSize(new Dimension(350, 250));
        reqPane.repaint();
    }

    public void numProcMethod() {
        System.out.println("reqPane has " + reqPane.getComponents().length);

        if (application.equalsIgnoreCase("Gaussian")) {
            gbcons.weightx = 0.0;
            // gbcons.gridwidth=2;
            // reqPane.remove(numProcEdLabel);
            numProcEdLabel
                    .setText("Use %NprocShared(SMP)/%NProcLinda(Clusters) in the G09 input");
            numProcSpin.setValue(4);// This is a dummy value and will be reset
                                    // in getnumProc method Sudhakar
            memSizeLabel.setText("Use %mem in the G09 input");
            memSizeTextField.setText("1000");
            gbcons.gridwidth = GridBagConstraints.REMAINDER;
            // rpgbl.setConstraints(numProcEdLabel, gbcons);
            reqPane.add(numProcEdLabel);
            reqPane.repaint();

        } else {
            try {
                System.out.println("reqPane has "
                        + reqPane.getComponents().length);
                gbcons.weightx = 0.0;
                reqPane.remove(numProcEdLabel);
                numProcEdLabel.setText("Number of Processors:");
                memSizeLabel.setText("Preferred Memory (Mbytes):");
                gbcons.gridwidth = GridBagConstraints.RELATIVE;
                rpgbl.setConstraints(numProcEdLabel, gbcons);
                reqPane.add(numProcEdLabel);
                gbcons.gridwidth = GridBagConstraints.REMAINDER;
                rpgbl.setConstraints(numProcSpin, gbcons);
                reqPane.add(numProcSpin);
                reqPane.repaint();
                // gbcons.gridwidth = GridBagConstraints.REMAINDER;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private String[] getMachineQueues() {
        ArrayList<String> queues = new ArrayList<String>();

        //for (ComputeBean hpc : GridChem.systems) {
        for (ComputeBean hpc : GridChem.getMachineList()) {	
            for (QueueBean q : hpc.getQueues()) {
                if (queues.indexOf(q) == -1) {
                    queues.add(q.getName());
                }
            }
        }

        System.out.println("Found ******************************" + queues.size() + " queues");
        String[] a = {};
        return queues.toArray(a);
    }

    private String[] getModuleList(String appName){
        
//        HashSet<String> moduleNames = new HashSet<String>();
        String[] a = {};
//        
//        String appPackageName = appPackageAndModuleName(appName)[0];
//        
//        moduleNames = APP_MODULE_HASHTABLE.get(appPackageName);
//        
//        System.out.println("\n (DEBUG) module list "+ moduleNames);
//        
//        return moduleNames.toArray(a);
        
        
        return GridChem.getSoftware(appName).getModules().toArray(a);
    }

//    // Make a list for app package name appearing in appCombo box from 
//    //  all available applications registered in GMS_WS database
//    private void getAppPackageAndModuleListFromDatabase(){
//        
//        ArrayList<String> appPackageNames = GridChem.getSoftwareNames();
//        
//        for(String str : appNames){
//            String ap_name = null;
//            if(str.contains("_")){
//                ap_name = str.split("_")[0];
//            }else{
//                ap_name = str;
//            }
//
//            if (!appPackageNames.contains(ap_name)) {
//                appPackageNames.add(ap_name);
//            }
//            
//            if(!APP_NAME_HASHSET.contains(str)){
//                APP_NAME_HASHSET.add(str);
//            }
//        }
//        
//        System.out.println("\n(INFO) Found " + appNames.size() + " applications" + " in "
//                + appPackageNames.size() + " app Packages.");   
//        
//        System.out.println("\n(DEBUG)  " + appNames.toArray());
//        
//        for(String apnStr : appPackageNames){
//            HashSet<String> set = new HashSet<String>();
//            
//            for(String anStr : appNames){
//                String mname = null;
//                String aname = null;
//                if(anStr.contains("_")){
//                    mname = anStr.split("_")[1].toLowerCase();
//                    aname = anStr.split("_")[0];
//                }else{
//                    mname = anStr.toLowerCase();
//                    aname = anStr;
//                }
//                if(apnStr.equalsIgnoreCase(aname)){
//                    set.add(mname);
//                }
//            }
//            
//            APP_MODULE_HASHTABLE.put(apnStr, new HashSet<String> (set) ) ;
//        }
//        
//    }
    
    
//    private String[] getAppPackageList(){
//
//        ArrayList<String> appPackageNames = new ArrayList<String>();
//        
//        for(Enumeration e = APP_MODULE_HASHTABLE.keys(); e.hasMoreElements(); ){
//            appPackageNames.add((String) e.nextElement());
//        }
//        
//        String[] a = {};
//        
//        return appPackageNames.toArray(a);
//    }
        
        
    
    
    private String[] getMachineProjects() {
        ArrayList<String> projects = new ArrayList<String>();
        for (ComputeBean hpc : GridChem.systems) {
        //for (ComputeBean hpc : GridChem.getSoftwareMachineList(application)){
        
        	// TODO: need to get the remote allocation names available to the user on the remote machine. 
            for (String allocation : hpc.getAllocations()) {
                if (projects.indexOf(allocation) == -1) {
                    projects.add(allocation);
                }
            }
        }
        System.out.println("Found " + projects.size() + " projects");
        String[] a = {};
        return projects.toArray(a);

    }

    // Amr
    // Now change the machines according to which ones have the application.
    public void populateMachineList(String application) {
    	
//        ArrayList appMachineList = new ArrayList();
        if (Settings.WEBSERVICE) {
            hpcListModel.removeAllElements();
            for (ComputeBean hpc: GridChem.getSoftwareMachineList(application)) {
                hpcListModel.addElement(hpc.getName());
            }
            //hpcListModel.addElement(SCHEDULER);
            System.out.println("EJP:1142:Webservice: MachineList for "+application+" is"+hpcListModel.toString());
        } else {
//            ArrayList machines = (ArrayList) GridChem.resourceHash
//                    .get("machines");
//            for (int j = 0; j < machines.size(); j++) {
//                Integer isPresent = (Integer) GridChem.resourceHash
//                        .get(application + "_" + machines.get(j));
//                if (isPresent != null) {
//                    appMachineList.add(machines.get(j));
//                }
//
//            }
//            hpcListModel.removeAllElements();
//            for (int i = 0; i < appMachineList.size(); i++) {
//                if (GridChem.machineContainsApplication((String) appMachineList
//                        .get(i), application)) {
//                    hpcListModel.addElement((String) appMachineList.get(i));
//                }
//            }
        }
        hpcList.setModel(hpcListModel);
        hpcList.setSelectedIndex(0);

    }

    /**
     * Populate project dropdown box with the projects corresponding to the
     * machine currently visible in the machine dropdown box
     * 
     * @param machine
     *            Name of machine whose project info will be used to populate
     *            the project dropdown box.
     */
    public void populateProjects(String machine) {
        projCombo.removeAllItems();
        if (Settings.WEBSERVICE) {
            if (machine.equals(SCHEDULER)) {

                projCombo.addItem(UNSPECIFIED);

            } else {
            	if (GridChem.accessType.equals(AccessType.COMMUNITY)) {
            		ComputeBean bean = GridChem.getMachineByName(machine);
            		for (String allocation: bean.getAllocations()) {
            			projCombo.addItem(allocation);
            			System.out.println("WEBSERVICE:System Specified project "+allocation+" added to list projCombo");
            		}
            		System.out.println("EditJobPanel:WEBSERVICE projects");
            		return;
            	}
            	else {
            		projCombo.addItem("default");
            		projCombo.setSelectedItem("default");
            		return;
            	}
            }
        } else {
            ArrayList items = GridChem.getMachineProjectsList(machine);
            int i;
            for (i = 0; i < items.size(); i++) {
                projCombo.addItem((String) items.get(i));
                System.out.println("EditJobPanel:nonWEBSERVICE projects added");
            }
        }
    }

    /**
     * Populate project dropdown box with the projects corresponding to the
     * machine currently visible in the machine dropdown box
     * 
     * @param machine
     *            Name of machine whose queue info will be used to populate the
     *            queue dropdown box.
     */
    private void populateQueues(String machine) {
        qCombo.removeAllItems();

        if (Settings.WEBSERVICE) {
            if (machine.equals(SCHEDULER)) {
                qCombo.addItem(UNSPECIFIED);
            } else {
            	ComputeBean bean = GridChem.getMachineByName(machine);
            	for (QueueBean queue: bean.getQueues()) {            
                    qCombo.addItem(queue.getName());
                    if (queue.isDefaultQueue()) {
                    	qCombo.setSelectedItem(queue.getName());
                    }
                }
            }
        } else {
            ArrayList items = GridChem.getMachineQueuesList(machine);

            for (int i = 0; i < items.size(); i++) {
                qCombo.addItem((String) items.get(i));
            }
        }
    }

    // end Amr

//    private void doSelectMach(String[] mach) {
//        int N = mach.length;
//        int[] indices = new int[N];
//        int j;
//
//        // System.out.println("N =" + N + "\n");
//        // System.out.println(mach[0]+"\n");
//        // System.out.println(apphpcModel.getElementAt(0).toString()+"\n");
//
//        for (int i = 0; i < N; i++) {
//            j = 0;
//            while (j < (hpcListModel.getSize() - 1)
//                    && !mach[i].equals(hpcListModel.getElementAt(j).toString())) {
//                j++;
//            }
//            indices[i] = j;
//        }
////        return indices;
//    }

    private ArrayList parsePref(String prefName) {
        Trace.entry();
        ArrayList nl = new ArrayList();
        String s;
        String[] hpcSystems = Preferences.getString(prefName).split(",");
        int machineCount = hpcSystems.length;
        for (int i = 0; i < machineCount; i++) {
            nl.add(hpcSystems[i]);
        }
        Trace.exit();
        return nl;
    }

    private void makeButtons() {

        // upButton
        upButton = new BasicArrowButton(BasicArrowButton.NORTH);
        upButton.addActionListener(new ButtonListener());

        // downButton
        downButton = new BasicArrowButton(BasicArrowButton.SOUTH);
        downButton.addActionListener(new ButtonListener());

    }

    /***************************************************************************
     * The actionPerformed method in this private inner class is called when the
     * user presses the up/down button.
     */
    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            Object item1;
            Object item2;
            int selectedIndex = hpcList.getSelectedIndex();
            if (selectedIndex >= 0) {
                if (evt.getSource() == upButton) {
                    item1 = hpcListModel.get(selectedIndex);
                    if (selectedIndex > 0) {
                        item2 = hpcListModel.get(selectedIndex - 1);
                        hpcListModel.set(selectedIndex, item2);
                        hpcListModel.set(selectedIndex - 1, item1);
                        hpcList.setSelectedIndex(selectedIndex - 1);
                        // apphpcScrollPane.scrollRectToVisible(apphpcBoard.getCellBounds(selectedIndex-1,selectedIndex));
                        hpcList.ensureIndexIsVisible(selectedIndex - 1);
                    }
                } else if (evt.getSource() == downButton) {
                    item1 = hpcListModel.get(selectedIndex);
                    if (selectedIndex < hpcListModel.getSize() - 1) {
                        item2 = hpcListModel.get(selectedIndex + 1);
                        hpcListModel.set(selectedIndex, item2);
                        hpcListModel.set(selectedIndex + 1, item1);
                        hpcList.setSelectedIndex(selectedIndex + 1);
                        // apphpcScrollPane.scrollRectToVisible(apphpcBoard.getCellBounds(selectedIndex,selectedIndex+1));
                        hpcList.ensureIndexIsVisible(selectedIndex + 1);
                    }
                }
            }
        }
    }// end private class ButtonListener

    public void actionPerformed(ActionEvent e) {
        // /*if (DEBUG)*/ System.out.println(e.getActionCommand());
        if (e.getActionCommand() == "Load") {

            // doLoadFile();

        } else if (e.getActionCommand() == "Edit/Build") {

            // call nanocad molecular editor and save file back to this
            // interface
            System.out.println("Calling Nanocad  Molecular Editor");

            doCallNanocad();

        } else if (e.getActionCommand() == "Create Default Job") {

            doMakeDefaultJob();

        } else if (e.getActionCommand() == "Submit") {
        	preferences.put("last_module", getModuleName());
        	preferences.put("last_app", getAppPackageName());
        	preferences.put("last_machine", getSubmitMachine());
            boolean ok = false;
            try {
                ok = verifyInput();
            } catch (IOException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, e1.getMessage(), "Input File Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            if(ok){
                if (validTime) {

                	if (isSubmittingScript == false) {
                		if (isUpdating == false) {
                			doAddJobToQueue();
                		} else {
                			doUpdateJobInQueue();
                		}
                	} else {
                		doAddJobsToQueue();
                	}

                    doCancel();
                }
            }

        } else if (e.getActionCommand() == "Cancel") {

            doCancel();
//            System.out.println("Size:" + leftPanelForchoicesBox.getWidth() + ", " + leftPanelForchoicesBox.getHeight());
        }  else if (e.getSource() == this.inputFilePanel.scriptInput) {
        	
        	if (this.inputFilePanel.scriptInput.isSelected()) {
        		disableReqPane();
        		isSubmittingScript = true;
        	} else {
        		enableReqPane();
        		isSubmittingScript = false;
        	}
        	
        } else {

            JOptionPane.showMessageDialog(null, "huh?", " should not happen",
                    JOptionPane.INFORMATION_MESSAGE);

        }

    }
    
    private void enableReqPane() {
    	
    	this.projCombo.setEnabled(true);
    	this.qCombo.setEnabled(true);
    	this.hr.setEnabled(true);
    	this.min.setEnabled(true);
    	this.numProcSpin.setEnabled(true);
    	this.memSizeTextField.setEnabled(true);
    }
    
    private void disableReqPane() {
    
    	this.projCombo.setEnabled(false);
    	this.qCombo.setEnabled(false);
    	this.hr.setEnabled(false);
    	this.min.setEnabled(false);
    	this.numProcSpin.setEnabled(false);
    	this.memSizeTextField.setEnabled(false);
    }

    /**
     * Read a textual file into a string. This routine has a history of
     * performing poorly on large files: FileInputStream was replaced with
     * BufferedReader by K. Kotwani; Line based reading was removed and a
     * StringBuffer was added by S. Brozell. It is not clear whether we have
     * balanced the tradeoffs optimally. For example, a FileInputStream with our
     * own buffering may be better.
     * 
     * More work related to exception handling and preferences is needed.
     * 
     * @param file
     *            read from this File.
     * @return a String with the file contents.
     */
    // temporarily commenting method below as it was adding square characters at
    // the end of text area for blank lines at the end of file (K Kotwani)
    // Need to look for a fix for this problem
    /*
     * static final int BUFFER_SIZE = 65536; // assert( BUFFER_SIZE >= 0 )
     * static final char[] buffer = new char[BUFFER_SIZE]; public String
     * readTextArea(File file) throws IOException { StringBuffer text = new
     * StringBuffer(BUFFER_SIZE); BufferedReader br = null; try { // this output
     * should be controlled by a verbosity preference
     * System.out.println("Opening file " + file.getName() ); br = new
     * BufferedReader(new FileReader(file)); int numRead = 0; synchronized
     * (buffer) { while ((numRead = br.read(buffer)) > -1) { // -1 is EOF
     * text.append(buffer); // this output should be controlled by a verbosity
     * preference System.out.println("Read " + numRead + " characters." ); } } }
     * catch(Throwable e) { JOptionPane.showMessageDialog( null, "There was a
     * problem reading the file...", "NewJob", JOptionPane.ERROR_MESSAGE );
     * System.err.println("LowLevelIO:readTextArea: error opening file");
     * System.err.println(e.toString()); e.printStackTrace(); } finally { //
     * this output should be controlled by a verbosity preference
     * System.out.println("Closing file " + file.getName() ); if (br != null) {
     * br.close(); } } String filetext = text.toString();
     * System.out.println("filetext: "+filetext); return text.toString(); }
     */
    // implemented this old method where it reads line by line
    public String readTextArea(File f) throws IOException {
        String line = "";
        FileInputStream fin = null;
        BufferedInputStream bis = null;
        BufferedReader br = null;
        String ta = "";
        try {
            fin = new FileInputStream(f);
            bis = new BufferedInputStream(fin);
            br = new BufferedReader(new InputStreamReader(bis));
        } catch (Throwable e) {

            JOptionPane.showMessageDialog(null,
                    "There was a problem reading the file...", "NewJob",
                    JOptionPane.ERROR_MESSAGE);
            System.err.println("LowLevelIO:readTextArea:  error opening file");
            System.err.println(e.toString());
            e.printStackTrace();
        }
        try {

            // ta.setText("");
            int i = 0;
            while ((line = br.readLine()) != null) {
                ta = ta.concat(line + "\n");
                i++;
            }
            fin.close();
        } catch (IOException e) {

            JOptionPane.showMessageDialog(null,
                    "There was a problem reading the file...", "NewJob",
                    JOptionPane.ERROR_MESSAGE);
            System.err.println("LowLevelIO:readTextArea:  error reading file");
            System.err.println(e.toString());
            e.printStackTrace();
        }
        return ta;
    }

//    public void doSaveFile() {
//        JFileChooser chooser = new JFileChooser();
//        int retVal = chooser.showSaveDialog(this);
//
//        try {
//            if (retVal == JFileChooser.APPROVE_OPTION) {
//                File file = chooser.getSelectedFile();
//                // save the file data
//                FileWriter fw = new FileWriter(file);
//                String outp = getInput();
//                fw.write(outp);
//                fw.close();
//                // changeFileName(file.getName());
//
//                ((InputInfoPanel) inputInfoBox).changeInputFileName(0, file);
//
//            }
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Error writing to file",
//                    "Save File Error", JOptionPane.INFORMATION_MESSAGE);
//        }
//        // changeFileName(file.getName());
//    }

    public void doMakeDefaultJob() {
        
        String app = appName(getAppPackageName(), getModuleName());

        createAndShowSampleJob(app);

    }

    public void doAddJobToQueue() {
        
        this.job.setName(getJobName().replaceAll("\\s", ""));
        this.job.setExperimentName(getResProj());
        this.job.setAllocationName(getProject());
        this.job.setSystemName(
                (getSubmitMachine().equals(SCHEDULER))?null:getSubmitMachine());
        
        this.job.setSoftwareName(getAppPackageName());
        
        /* ************************************************ */
        this.job.setModuleName(getModuleName());
        this.job.setUsedMemory(Long.parseLong(memSizeTextField.getText()));
        /* ************************************************ */
        
        this.job.setProjectName(GridChem.project.getName());
        this.job.setUserId(GridChem.user.getId());
        this.job.setQueueName(getQueueName());
        this.job.setRequestedCpus(new Long(getNumProc()));
        
        this.job.setRequestedCpuTime(getCalendarTime());
        
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.add(Calendar.MINUTE, Integer.parseInt(this.min.getValue().toString()));
        cal.add(Calendar.HOUR, Integer.parseInt(this.hr.getValue().toString()));
        
        this.job.setRequestedCpuTime(cal);
        this.job.getInputFiles().clear();
        
        // Job is already populated.  Now add in the input files 
        for (File file: getInputFiles()) {
            LogicalFileBean lFile = new LogicalFileBean();
            lFile.setJobId(-1);
            lFile.setLocalPath(file.getAbsolutePath());
            this.job.getInputFiles().add(lFile);
        }
        
        try {
            
            System.out.println(this.job.toString());

            SubmitJobsWindow.addJob(this.job);

//            System.out.println("doAddJobToQueue: after adding the new job");
//
//            // Do something about adding the new job to the list pane or
//            // whatever
//            stuffInside
//                    .AddElement(j.getResProj() + " " + j.getJobName() + " "
//                            + j.getApp() + " "
//                            + ((mach == null) ? SCHEDULER : mach[0]));
//
//            System.out
//                    .println("doAddJobToQueue: after adding job to the big list");

        } catch (JobException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Queue Wall Time Error", JOptionPane.OK_OPTION);
            e.printStackTrace();
        }

    }
    
    public void doAddJobsToQueue() {
    	List<File> inputs = getInputFiles();
    	
    	if (inputs.size() == 0) {
    		return;
    	}
    	
    	JobScriptParser parser = new JobScriptParser(this, inputs.get(0).getAbsolutePath());
    	parser.parse();
    	
    	List<JobBean> jobList = parser.getJobList();
    	
    	for (JobBean localJob : jobList) {
    		try {
                
                System.out.println(localJob.toString());

                SubmitJobsWindow.addJob(localJob);

            } catch (JobException e) {
                e.printStackTrace();
            }

    	}
    }

    public void doCancel() {
        
        SubmitJobsWindow.getInstance();
        
        this.dispose();
        // EditJobPanel.frame.setVisible(false);
        // editSSHJobPanel.frame.setVisible(false);
    }

    public void doUpdateJobInQueue() {
        this.job.setId(null);
        this.job.setName(getJobName());
        this.job.setExperimentName(getResProj());
        this.job.setSystemName(
                (getSubmitMachine().equals(SCHEDULER))?null:getSubmitMachine());
        this.job.setSoftwareName(getAppPackageName());
        this.job.setModuleName(getModuleName());
        this.job.setProjectName(getProject());
        this.job.setQueueName(getQueueName());
        this.job.setRequestedCpus(new Long(getNumProc()));
        
        this.job.setRequestedCpuTime(getCalendarTime());
        
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.add(Calendar.MINUTE, Integer.parseInt(this.min.getValue().toString()));
        cal.add(Calendar.HOUR, Integer.parseInt(this.hr.getValue().toString()));
        
        this.job.setRequestedCpuTime(cal);
        
        // Job is already populated.  Now add in the input files
        this.job.getInputFiles().clear();
        
        for (File file: getInputFiles()) {
            LogicalFileBean lFile = new LogicalFileBean();
            lFile.setJobId(-1);
            lFile.setLocalPath(file.getAbsolutePath());
            job.getInputFiles().add(lFile);
        }
        
        try {
            
            if(SubmitJobsWindow.jobQueue.contains(this.job)) {
            
                //SubmitJobsWindow.updateJob(this.job);
                
//                System.out
//                        .println("doRepJobToQueue: after replacing with the new job");
//
//                stuffInside.qbModel.setElementAt(j.getResProj() + " "
//                        + j.getJobName() + " " + j.getApp() + " "
//                        + j.getMachine()[0], Index);
//                System.out
//                        .println("doAddJobToQueue: after replacing job to the big list");

            }
            
        } catch (JobException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Queue Wall Time Error", JOptionPane.OK_OPTION);
            e.printStackTrace();
        }
        // SubmitJobsWindow.si.delButton.setEnabled(true);
        // SubmitJobsWindow.si.newJButton.setEnabled(true);
        // SubmitJobsWindow.si.submButton.setEnabled(true);
        // SubmitJobsWindow.si.suballButton.setEnabled(true);
    }

    public void doCallNanocad() {
        System.out.println(" Calling Nanocad");
        String setsfile = ".settings";
        boolean append = false;
        File sets = new File(Settings.defaultDirStr + Settings.fileSeparator
        // File sets = new File(Env.getApplicationDataDir() +
        // Settings.fileSeparator
                + setsfile);
        try {
            FileWriter fw = new FileWriter(sets, append);
            fw.write("Username= " + Settings.name.getText() + "\n");
            fw.write("CGI= " + Invariants.httpsGateway + "\n");
            fw.close();
            FileWriter fw2 = new FileWriter(Settings.defaultDirStr
                    + Settings.fileSeparator + "loadthis", append);
            fw2.write(Settings.defaultDirStr + Settings.fileSeparator
                    + "common" + Settings.fileSeparator + "Molecule"
                    + Settings.fileSeparator + "Inorganic"
                    + Settings.fileSeparator + "water.pdb\n");
            fw2.close();
        } catch (IOException ioe) {
        }
        String tmpfile = "tmp.txt";

        // File fa = new File(Settings.defaultDirStr+
        File fa = new File(Env.getApplicationDataDir() + Settings.fileSeparator
                + tmpfile);
        if (fa.exists()) {
            fa.delete();
            // new File(Settings.defaultDirStr + Settings.fileSeparator +
            // tmpfile).delete();
        }
        // launch nanocad
        System.out.println("Calling nanocadMain");
        nanWin = new nanocadFrame2();
        // WindowListener wl = new WindowListener();
        nanWin.addWindowListener(this);
        nanWin.nano.addComponentListener(this);

        System.out.println(" Done with Nanocad");
        // System.err.println("Now put yer data from the file into the text
        // thing");
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {

        // check for temp file and if it exists, load into text box
        System.err.println("editingStuff:load tmp.txt file here!");

        // File f = new File(Settings.defaultDirStr +
        File f = new File(Env.getApplicationDataDir() + Settings.fileSeparator
                + "tmp.txt");
        if (f.exists()) {
           
            this.changeAppPackage(newNanocad.exportedApplication);
            this.changeModule(newNanocad.exportedApplication);
            
            this.populateMachineList(newNanocad.exportedApplication);
            this.populateProjects((String) hpcListModel.getElementAt(0));
            numProcMethod();

            ArrayList<File> newInputs = new ArrayList<File>();
            newInputs.add(f);
            changeInputFiles(newInputs);

           
        }

        nanWin.dispose();

        if (nanWin.nano.t != null) {

            nanWin.nano.t.setVisible(false);
        }
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

    // }

    public void componentHidden(ComponentEvent e) {
        // check for temp file and if it exists, load into text box
        System.err.println("editingStuff:load tmp.txt file here!");

        // File f = new File(Settings.defaultDirStr +
        File f = new File(Env.getApplicationDataDir() + Settings.fileSeparator
                + "tmp.txt");

        if (f.exists()) {
            this.changeAppPackage(newNanocad.exportedApplication);
            this.changeModule(newNanocad.exportedApplication);
            
            this.populateMachineList(newNanocad.exportedApplication);
            this.populateProjects((String) hpcListModel.getElementAt(0));

            System.out.println("****Application name: " + application);
            application = newNanocad.exportedApplication;
            ArrayList<File> newInputs = new ArrayList<File>();
            newInputs.add(f);
            changeInputFiles(newInputs);
        }

        nanWin.dispose();

        if (nanWin.nano.t != null) {

            nanWin.nano.t.setVisible(false);
        }

        JOptionPane.showMessageDialog(null, "WARNING: The input "
                + "appearing here is taken from a template.\n"
                + "The molecule information is correct, but "
                + "make sure to edit\nthe other parts of the " + "text.",
                "GridChem: Job Editor", JOptionPane.WARNING_MESSAGE);
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    // methods to access the fields
    protected String getResProj() {
        return this.projNameText.getText();
    }

    private String getJobName() {
        return this.jobNameText.getText();
    }

    private String getProject() {
        //return (String) this.projCombo.getSelectedItem();
    	return (String) this.projCombo.getEditor().getItem();
    }

    private String getSubmitMachine() {

         return (String) this.hpcList.getSelectedValue();
//        int[] indices = hpcList.getSelectedIndex()
//        int N = indices.length;
//        String[] apphpc = new String[N];
//        for (int i = 0; i < N; i++) {
//            // System.out.println("indices["+i+"]="+indices[i]+"\n");
//            apphpc[i] = hpcListModel.getElementAt(indices[i]).toString();
//            System.out.print("apphpc[" + i + "]=" + apphpc[i] + "\n");
//        }
//        return apphpc;
    }

    private String getQueueName() {
        return (String) this.qCombo.getSelectedItem();
    }

//    public String getStringTime() {
//        String time;
//        String temp;
//        // time = (String) this.hr.getSelectedItem() + ":";
//        temp = ((Integer) this.hr.getValue()).toString();
//        if (temp.length() == 1)
//            temp = "0" + temp;
//        // time = ((Integer) this.hr.getValue()).toString() + ":";
//        time = temp + ":";
//        // time = (String) this.min.getSelectedItem() + ":";
//        temp = ((Integer) this.min.getValue()).toString();
//        if (temp.length() == 1)
//            temp = "0" + temp;
//        time = time + temp + ":";
//        // time = (String) this.sec.getSelectedItem();
//        // temp = ((Integer) this.sec.getValue()).toString();
//        // Sec removed but set to 0 here pvs 24 Apr 06 temp = ((Integer)
//        // this.sec.getValue()).toString();
//        temp = "0";
//        if (temp.length() == 1)
//            temp = "0" + temp;
//        time = time + temp;
//        return time;
//    }
    
    private Calendar getCalendarTime() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.add(Calendar.MINUTE, ((Integer)hrnm.getValue()).intValue());
        cal.add(Calendar.HOUR, ((Integer)hrnm.getValue()).intValue());
        return cal;
    }

    protected String getAppPackageName() {
        System.out.println("Getting application name");
        return (String) this.appCombo.getSelectedItem();
    }

    private int getNumProc() {
        int np;
        Integer NP;
        // return this.numProcSpin.getSelectedItem();
        if (getAppPackageName().equalsIgnoreCase(Invariants.APP_NAME_GAUSSIAN)) {
            // For Gaussian this value need to be obtained form input
            // Read input and grep %nproclinda and %nprocshared
            // The np should be %nprocshared X %nproclinda Sudhakar
            // Pamidighantam
            String inptxt = getInputText(getInputFiles().get(0));
            int npl = -1;
            int nps = -1;
            int lpt = 0, spt = 0;
            StringTokenizer ginpt = new StringTokenizer(inptxt);

            while (ginpt.hasMoreTokens()) {
                String NextInpToken = ginpt.nextToken().toLowerCase();

                if (NextInpToken.contains("%nproclinda")) {
                    String lpts = NextInpToken.substring(NextInpToken
                            .indexOf("=") + 1, NextInpToken.length());
                    NP = java.lang.Integer.parseInt(lpts.trim(), 10);
                    npl = NP.intValue();
                } else if (NextInpToken.contains("%nprocshared")) {
                    String spts = NextInpToken.substring(NextInpToken
                            .indexOf("=") + 1, NextInpToken.length());
                    NP = java.lang.Integer.parseInt(spts.trim(), 10);
                    nps = NP.intValue();
                }

            }
            System.out
                    .println("Number of Processor for the job for G03 from input (linda) "
                            + npl + " (shared) " + nps);

            if (nps < 0) {
                nps = 2;
                System.out
                        .println("Number of shared memory Processors defaulted to 2");
            }

            if (npl < 0) {
                npl = 1;
                System.out.println("Number of Linda Processors defaulted to 1");
            }

            np = npl * nps;
        } else {
            NP = (Integer) this.numProcSpin.getValue();
            System.out.println("##################### DEBUG ################### " + NP);
            np = NP.intValue();

        }
        return np;
    }

    
    /** Get input files from embedded input file panel.
     * @return
     */
    private ArrayList<File> getInputFiles() {
        // return this.inputText.getText();
        return this.inputFilePanel.getInputFiles();
    }
    
    private String getInputText(File f) {
        String inText = "";
        
        try {
            FileInputStream fis = new FileInputStream(f);
        
            byte[] bin = new byte[512];
            int size = 0;
            
            while((size = fis.read(bin)) > -1) {
                inText += bin.toString();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return inText;
        
    }
    
    protected String getModuleName(){

        String name = (String) this.appModuleCombo.getSelectedItem();
        System.out.println("Getting module name "+name);
        
        return name;
    }
    
    
    // methods to change the fields

    private void changeJobNameField(String newname) {
        jobNameText.selectAll();
        jobNameText.replaceSelection(newname);
    }

    private void changeResearchProjectNameField(String newname) {
        projNameText.selectAll();
        projNameText.replaceSelection(newname);
    }

    private void changeProject(String p) {
        projCombo.setSelectedItem(p);
        System.out.println("Getting account name "+p);
    }

    private void changeMachine(String m) {
        int size = hpcList.getModel().getSize();
        int index = 0;
        System.out.println("There are items " + size
                + " in the list. Currently seeking " + m);
        for (int i = 0; i < size; i++) {
            System.out.println(hpcList.getModel().getElementAt(i));

            if (hpcList.getModel().getElementAt(i).equals(m))
                index = i;
        }
        hpcList.ensureIndexIsVisible(index);
        hpcList.setSelectedIndex(index);
    }

    private void changeProjectPSN(String p){
    	projCombo.setSelectedItem(p);
    }
    private void changeQueue(String q) {
        qCombo.setSelectedItem(q);
    }


    private void changeModuleList(SoftwareBean software) {
        
        appModuleCombo.removeAllItems();
        
//        HashSet<String> set = new HashSet<String>(APP_MODULE_HASHTABLE.get((Object) appPackageName));
    
        if (software.getModules().isEmpty()) {
        	appModuleCombo.addItem(software.getAcronym().toLowerCase());
        } else {
        	
        	if (software.getName().equals("Lammps")) {
         		if (dsLmpUserIDSet.contains(GridChem.user.getUserName())) {
        			for(String module: software.getModules()){
            			appModuleCombo.addItem(module);
            		}
        		} else {        			
        			appModuleCombo.addItem("lmp");
        		}
        	} else {
        		for(String module: software.getModules()){
        			appModuleCombo.addItem(module);
        		}
        	}
        }
        
        appModuleCombo.setSelectedIndex(0);
    
    }

    public void changeAppPackage(String system) {
        // not sure how this should change since a change in system does not trigger a 
    	// change in software, but rather a change in software triggers a change in
    	// systems.  My guess is that there is a relationship between the module and
    	// the compute resource, which would mean that there is a joint relationship
    	// between module, software, and resource.  If so, then we need to refresh
    	// the entire module combo box list ever time a resource is changed, and we
    	// need to change the database schema to reflect this association.
    	
    	// as it stands now, I'm disregarding this call since all it will do is 
    	// selecte the same app and module every time.
//    	SoftwareBean software = GridChem.getSoftware(appName);
//        
//        System.out.println("changing application name :" + software.getName());
//        
//        appCombo.setSelectedItem(software.getName());
//        changeModuleList(software); 
        // numProcMethod();
    }

//   only for compatibility
    public void changeApp(String appName) {
        changeAppPackage(appName);
    }
    
    private void changeModule(String softwareName) {

        String moduleName = GridChem.getSoftware(softwareName).getModules().get(0);
        System.out.println("changing module "+ moduleName);

        appModuleCombo.setSelectedItem(moduleName);
    }
    
    
    private void changeNumProc(int n) {
        // numProcSpin.setSelectedItem(n);
        numProcnm.setValue(new Integer(n));
    }

    public void changeInputFiles(ArrayList<File> newFiles) {

//        this.job.setInputFiles(newFiles);
        updateInputInfoPanel(this.job,newFiles);

    }

    public void updateInputInfoPanel(JobBean job,ArrayList<File> files) {
    	
        if (!this.inputFilePanel.isEmpty()) {
            int keepFiles = JOptionPane.showConfirmDialog(this, "Remove the existing input files?", "", JOptionPane.YES_NO_OPTION);
            if (keepFiles == JOptionPane.OK_OPTION) {
    //            this.rightPanel.remove(inputFilePanel);
    //    
    //            Border leBorder = BorderFactory
    //                    .createEtchedBorder(EtchedBorder.LOWERED);
    //            TitledBorder inputInfoTitled = BorderFactory.createTitledBorder(
    //                    leBorder, "Input File Information", TitledBorder.LEFT,
    //                    TitledBorder.DEFAULT_POSITION, new Font("Sansserif", Font.BOLD,14));
    //    
    //            inputFilePanel = new InputFilePanel(this);
                inputFilePanel.clearFileInput();
                inputFilePanel.addMultipleFileInput(files);
                
                System.out.println("(DEBUG)\nnow updateInputInfoPanel : # of inputs = "+ inputFilePanel.getInputFiles().size());
                
    //            inputFilePanel.setBorder(BorderFactory.createCompoundBorder(
    //                    inputInfoTitled, BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            }
        }
        else {
        	inputFilePanel.addMultipleFileInput(files);
            System.out.println("(DEBUG)\nnow updateInputInfoPanel : # of inputs = "+ inputFilePanel.getInputFiles().size());
        }  
    	

//        GridBagConstraints rightPanelConstraints = new GridBagConstraints();
//
//        rightPanelConstraints.weighty = 1.0;
//        rightPanelConstraints.gridheight = 6;
//        rightPanelConstraints.gridy = 0;
//        rightPanelConstraints.gridx = 0;
//        rightPanelConstraints.fill = GridBagConstraints.BOTH;
//        rightPanelConstraints.anchor = GridBagConstraints.PAGE_START;

        //this.rightPanel.add(inputFilePanel, 0);

        this.rightPanel.repaint();
    }

    private void setMaxQueueTime(int hours, int minutes) {

    }

    /**
     * Get rid of all the web characters.
     * 
     * @param string
     * @return
     */
    private String removeWebCharacters(String string) {
        return string.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"").replaceAll("&amp;amp;#x0D;", "\n");
    }

    /**
     * Create a string representation of the Calendar object in hh:mm format and
     * where the hours field ranges from 0 - 32k
     * 
     * 
     */
    private String resolveTimeLimit(Calendar cal) {

        int days = (cal.get(Calendar.DAY_OF_YEAR) - 1) * 24;
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);

        return (days + hours) + ":" + ((minutes == 0) ? "00" : minutes);
    }
    
    private String getSelectedProj() {
    	String selectedProj = "";
    	  int selectedMachineIndex = hpcList.getSelectedIndex();
        String hpcName = (String) hpcListModel.get(selectedMachineIndex);
    	ComputeBean hpc = GridChem.getMachineByName(hpcName);
    
    	    System.out.println("EJP:2111: hpc allocations for HPC system "+hpcName+"\n");
    	    
    	    if (hpcName.equals("Grid Scheduler")){
    	    	// skip getting allocation name apriori
    	    	// temporarily switch it to Cobalt
    	    	hpcName="Ember";
    	    }
    	    else if (hpcName.equals(this.job.getSystemName())) {
    	      System.out.println(" are "+hpc.getAllocations()+"\n");
    	    
    	      for (String p : hpc.getAllocations()) {
               if (p.equals((String) projCombo.getSelectedItem())) {
                   selectedProj = p;
               }
    	      }
    	    }
    	//
    	    // Forcing project
    	      selectedProj="dck";
    	System.out.println(" getSelectedProj: selectedProj is "+selectedProj);
    	return selectedProj;
    }
    
    
    private QueueBean getSelectedQueue() {

        QueueBean selectedQueue = null;

        int selectedMachineIndex = hpcList.getSelectedIndex();

        String hpcName = (String) hpcListModel.get(selectedMachineIndex);

        if (hpcName.equals(SCHEDULER)) {

        	selectedQueue = new QueueBean();
        	selectedQueue.setName(UNSPECIFIED);
        	selectedQueue.setDefaultQueue(true);
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.add(Calendar.DAY_OF_YEAR, 30);
            selectedQueue.setMaxCpuTime(cal);
            selectedQueue.setMaxWallClockTime(cal);
            selectedQueue.setMaxCpus(1024);
            selectedQueue.setMaxNodes(2048);

        } else {

            ComputeBean hpc = GridChem.getMachineByName(hpcName);

            for (QueueBean q : hpc.getQueues()) {
                if (q.getName().equals((String) qCombo.getSelectedItem())) {
                    selectedQueue = q;
                }
            }
        }

        return selectedQueue;
    }

    private int getIntegerHours(Calendar cal) {

        int days = (cal.get(Calendar.DAY_OF_YEAR) - 1) * 24;
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        return days + hours;
    }

    private double getDoubleHours(Calendar cal) {

        double days = (cal.get(Calendar.DAY_OF_YEAR) - 1) * 24;
        double hours = cal.get(Calendar.HOUR_OF_DAY);
        double minutes = cal.get(Calendar.MINUTE);
        return days + hours + (minutes / 60);
    }

    // lixh_add
    // called when the different application is selected
    private class appComboListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                return;
            }
            isLoading = true;
            changeModuleList(GridChem.getSoftware((String) (e.getItem())));
            populateMachineList((String) (e.getItem()));
            System.out.println("(Debug) choosen app package name:"+e.getItem());
            
            String app = appName((String)e.getItem(), getModuleName());
            isLoading = false;
            
            //createAndShowSampleJob(app);
            hpcList.setSelectedIndex(0);
            
//            printDefaultInputFile(app);
    
        }
        
        private void printDefaultInputFile(String app) {
            String newInput = "";
            
            if (app.equalsIgnoreCase(Invariants.APP_NAME_GAUSSIAN)) {
                System.out.println("Trying Gaussian Run");
                newInput = "%chk=water.chk\n"
                        + "%nprocshared=1\n"
                        + "%mem=500MB\n"
                        + "#P RHF/6-31g* opt pop=reg gfinput gfprint iop(6/7=3) SCF=direct \n"
                        + " \n" + "Gaussian Test Job 00\n"
                        + "Water with archiving\n" + " \n" + "0 1\n" + "O\n"
                        + "H 1 0.96\n" + "H 1 0.96 2 109.471221\n\n";
                
                FileUtility.printDefaultInput(app,"gaussian_sample0.inp",newInput);
                                
            } else if (app.equalsIgnoreCase(Invariants.APP_NAME_GAMESS)) {

                System.out.println("Trying GAMESS Run");
                newInput = "! GAMESS Test Job 00. \n\n"
                        + " $CONTRL SCFTYP=RHF RUNTYP=OPTIMIZE COORD=ZMT NZVAR=0 $END \n"
                        + " $SYSTEM TIMLIM=2 MEMORY=500000 $END \n"
                        + " $STATPT OPTTOL=1.0E-5  $END \n"
                        + " $BASIS  GBASIS=STO NGAUSS=2 $END \n"
                        + " $GUESS  GUESS=HUCKEL $END \n" + " $DATA \n"
                        + "Methylene...1-A-1 state...RHF/STO-2G\n" + "Cnv  2\n"
                        + " \n" + "C\n" + "H  1 rCH\n" + "H  1 rCH  2 aHCH\n"
                        + " \n" + "rCH=1.09\n" + "aHCH=110.0 \n" + " $END\n\n";
                
                FileUtility.printDefaultInput(app,"gamess_sample0.inp",newInput);
                                
            } else if (app.equalsIgnoreCase("gamess-xml")) {
                System.out.println("Trying Gamess-XML Run");
                newInput = "start h2o_scf \n\n" + "geometry units au\n"
                        + "  O 0.00000000 0.00000000 0.24029800\n"
                        + "  H 0.00000000 1.43256600 -0.96119100\n"
                        + "  H 0.00000000 -1.43256600 -0.96119100\n" + "end\n\n"
                        + "basis noprint\n" + " H library sto-3g\n"
                        + " O library sto-3g\n" + "end\n\n" + "scf\n"
                        + "  thresh 1.e-10\n" + "  print low\n" + "end\n\n"
                        + "gradients\n" + "  print none\n" + "end\n\n" + "md\n"
                        + " system h2o_scf\n" + " equil 0 data 10 step 0.0005\n"
                        + " print step 1 stat 10\n" + " record scoor 1 prop 1\n"
                        + " test 10\n" + "end\n\n" + "task scf dynamics\n\n";
                
                FileUtility.printDefaultInput(app,"gamess-XML_sample0.inp",newInput);
                
            } else if (app.equalsIgnoreCase(Invariants.APP_NAME_QMCPACK)) {
                System.out.println("Trying QMC Run");
                newInput = "<?xml version=\"1.0\"?>\n"
                        + "<simulation xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                        + "  xsi:noNamespaceSchemaLocation=\"http://www.mcc.uiuc.edu/qmc/schema/molecu.xsd\"> \n"
                        + "  <project id=\"H2O.GTO\" series=\"0>\" \n"
                        + "<application name=\"qmcapp\" role=\"molecu\" class=\"serial\" version=\"0.2\"> \n"
                        + "QMC of H2O molecule using Slater-type Orbitals by Aung, Pitzer and Chan,\n"
                        + "JCP 49 2071 (1968) of wavefunction II.  See JCP, 77, 5593 (1982). \n"
                        + " </application> \n"
                        + " <random parallel=\"true\" seed=\"-1\"/> \n"
                        + "</project>\n"
                        + " \n"
                        + "  <include href=\"H2O.GamesXml.xml\"/> \n"
                        + " <wavefunction name=\"psi0\" target=\"e\"> \n"
                        + " <!-- add correlation functions --> \n"
                        + " <jastrow name=\"Jee\" type=\"Two-Body\" function=\"pade\"> \n"
                        + " <distancetable source=\"e\" target=\"e\"/> \n"
                        + " <correlation speciesA=\"e\" speciesB=\"e\"> \n"
                        + "<parameter id=\"juu_a\" name=\"A\">-0.5</parameter> \n"
                        + "<parameter id=\"juu_b\" name=\"B\">2.60015</parameter> \n"
                        + "</correlation>\n"
                        + "</jastrow> \n"
                        + "\n"
                        + "                           </wavefunction> \n"
                        + "<hamiltonian name=\"h0\" type=\"generic\" target=\"e\"> \n"
                        + " <pairpot name=\"ElecElec\" type=\"coulomb\" source=\"e\" target=\"e\"/> \n"
                        + "<pairpot name=\"Coulomb\" type=\"coulomb\" source=\"i\" target=\"e\"/> \n"
                        + " <constant name=\"IonIon\" type=\"coulomb\" source=\"i\" target=\"i\"/> \n"
                        + "</hamiltonian> \n" + "<qmc method=\"vmc\"> \n"
                        + " <parameter name=\"blocks\">100</parameter> \n"
                        + "<parameter name=\"steps\">100</parameter>  \n"
                        + "<parameter name=\"walkers\">20</parameter> \n"
                        + "<parameter name=\"timestep\">0.01</parameter> \n"
                        + "</qmc> \n" + "<qmc method=\"vmc\"> \n"
                        + " <parameter name=\"blocks\">500</parameter>\n"
                        + " <parameter name=\"steps\">100</parameter> \n"
                        + "<parameter name=\"timestep\">0.01</parameter> \n"
                        + " </qmc> \n" + "<qmc method=\"dmc-ptcl\"> \n"
                        + " <parameter name=\"num_gen\">30</parameter> \n"
                        + " <parameter name=\"target_walkers\">200</parameter> \n"
                        + " <parameter name=\"blocks\">500</parameter> \n"
                        + " <parameter name=\"steps\">200</parameter> \n"
                        + " <parameter name=\"timestep\">1.0e-3</parameter> \n"
                        + " </qmc> \n" + " \n" + "                      \n\n";
                
                FileUtility.printDefaultInput(app,"qmcpack_sample0.inp",newInput);
                
            } else if (app.equalsIgnoreCase(Invariants.APP_NAME_NWCHEM)) {

                System.out.println("Trying NWChem Run");
                newInput = "start h2o_scf \n\n" + "geometry units au\n"
                        + "  O 0.00000000 0.00000000 0.24029800\n"
                        + "  H 0.00000000 1.43256600 -0.96119100\n"
                        + "  H 0.00000000 -1.43256600 -0.96119100\n" + "end\n\n"
                        + "basis noprint\n" + " H library sto-3g\n"
                        + " O library sto-3g\n" + "end\n\n" + "scf\n"
                        + "  thresh 1.e-10\n" + "  print low\n" + "end\n\n"
                        + "gradients\n" + "  print none\n" + "end\n\n" + "md\n"
                        + " system h2o_scf\n" + " equil 0 data 10 step 0.0005\n"
                        + " print step 1 stat 10\n" + " record scoor 1 prop 1\n"
                        + " test 10\n" + "end\n\n" + "task scf dynamics\n\n";
                
                FileUtility.printDefaultInput(app,"nwchem_sample0.inp",newInput);
                
            } else if (app.equalsIgnoreCase(Invariants.APP_NAME_MOLPRO)) {
                System.out.println("Trying Molpro Run");
                newInput = "***, Allene geometry optimization\n"
                        + "memory,1,m\n\n"
                        + "basis=sto-3g\n"
                        + "ierr=0\n\n"
                        + "text,optimized values:\n"
                        + "e_old=[-114.42171910,-114.42171910,-114.42171910,-114.42171910,-114.42171910,-114.42171910]\n"
                        + "step_old=[5,5,5,5,5,5]\n\n"
                        + "bmat=['  ','BMAT']\n"
                        + "optm=['RF','AH','DIIS']\n\n"
                        + "i=0\n"
                        + "do ibmat=1,#bmat\n"
                        + "do imeth=1,#optm\n"
                        + "clear,x*,y*,z*\n"
                        + "i=i+1\n\n"
                        + "text,start geometry\n"
                        + "rcc=1.32 ang\n"
                        + "rch=1.08 ang\n"
                        + "acc=120 degree\n\n"
                        + "Geometry={C1;\n"
                        + "         C2,c1,rcc\n"
                        + "          Q1,c1,rcc,c2,45\n"
                        + "          C3,c2,rcc,c1,180,q1,0\n"
                        + "          h1,c1,rch,c2,acc,q1,0\n"
                        + "          h2,c1,rch,c2,acc,h1,180\n"
                        + "          h3,c3,rch,c2,acc,h1,90\n"
                        + "          h4,c3,rch,c2,acc,h2,90}\n\n"
                        + "int\n"
                        + "hf;\n"
                        + "optg,grad=1.d-4\n"
                        + "coord,$bmat(ibmat)\n"
                        + "method,$optm(imeth);\n"
                        + "method(i)='$optm(imeth) $bmat(ibmat)'\n"
                        + "e1(i)=energy\n"
                        + "rcc_opt(i)=rcc\n"
                        + "rch_opt(i)=rch\n"
                        + "acc_opt(i)=acc\n\n"
                        + "steps(i)=optstep\n"
                        + "de(i)=abs(e1(i)-e_old(i))\n"
                        + "if(de(i).gt.1.d-7.or.steps(i).gt.step_old(i)) ierr=1\n\n"
                        + "enddo\n"
                        + "enddo\n\n"
                        + "demax=max(de)\n\n"
                        + "if(ierr.eq.0) then\n"
                        + "table,method,e1,rcc_opt,rch_opt,acc_opt,steps\n"
                        + "save,test.log\n"
                        + "title,Results for job allene_opt.test\n"
                        + "title,No errors detected. Max error: de=$demax\n\n"
                        + "else\n\n"
                        + "table,method,e1,e_old,de,rcc_opt,rch_opt,acc_opt,steps,step_old\n"
                        + "save,test.log\n"
                        + "title,Results for job allene_opt.test\n"
                        + "title,ERRORS DETECTED. Max error: de=$demax\n"
                        + "endif\n\n";
                
                FileUtility.printDefaultInput(app,"molpro_sample0.inp",newInput);
                
            } else if (app.equalsIgnoreCase(Invariants.APP_NAME_AMBER_SANDER)) {

                System.out.println("Trying Amber Run");
                  
                newInput = "(Use 1 processor!) Very simple bond minimization with a water molecule\n"
                + " &cntrl\n"
                + "  imin = 1, maxcyc=200,\n"
                + "  ntb=0, cut = 30.0,"
                + "  ntpr = 5,\n"
                + "/ \n";
                
                FileUtility.printDefaultInput(app,"amber_sample0.inp",newInput);
                
                newInput =      "TP3\n"
                + "     3\n"
                + "   0.0000000   0.0000000   0.0000000\n"
                + "   0.9572000   0.0000000   0.0000000\n"
                + "  -0.2399880   0.9266270   0.0000000\n";
                FileUtility.printDefaultInput(app,"amber_sample0.inpcrd",newInput);
                
                newInput =  "%VERSION  VERSION_STAMP = V0001.000  DATE = 08/25/06  10:38:44\n"
                + "%FLAG TITLE\n"
                + "%FORMAT(20a4)\n"
                + "TP3\n"
                + "%FLAG POINTERS\n"
                + "%FORMAT(10I8)\n"
                + "       3       2       3       0       0       0       0       0       0       0\n"
                + "       4       1       0       0       0       2       0       0       2       1\n"
                + "       0       0       0       0       0       0       0       0       3       0\n"
                + "       0\n"
                + "%FLAG ATOM_NAME\n"
                + "%FORMAT(20a4)\n"
                + "O   H1  H2\n"
                + "%FLAG CHARGE\n"
                + "%FORMAT(5E16.8)\n"
                + " -1.51973982E+01  7.59869910E+00  7.59869910E+00\n"
                + "%FLAG MASS\n"
                + "%FORMAT(5E16.8)\n"
                + "  1.60000000E+01  1.00800000E+00  1.00800000E+00\n"
                + "%FLAG ATOM_TYPE_INDEX\n"
                + "%FORMAT(10I8)\n"
                + "       1       2       2\n"
                + "%FLAG NUMBER_EXCLUDED_ATOMS\n"
                + "%FORMAT(10I8)\n"
                + "       2       1       1\n"
                + "%FLAG NONBONDED_PARM_INDEX\n"
                + "%FORMAT(10I8)\n"
                + "       1      -1      -1       3\n"
                + "%FLAG RESIDUE_LABEL\n"
                + "%FORMAT(20a4)\n"
                + "WAT\n"
                + "%FLAG RESIDUE_POINTER\n"
                + "%FORMAT(10I8)\n"
                + "       1\n"
                + "%FLAG BOND_FORCE_CONSTANT\n"
                + "%FORMAT(5E16.8)\n"
                + "  5.53000000E+02  5.53000000E+02\n"
                + "%FLAG BOND_EQUIL_VALUE\n"
                + "%FORMAT(5E16.8)\n"
                + "  1.51360000E+00  9.57200000E-01\n"
                + "%FLAG ANGLE_FORCE_CONSTANT\n"
                + "%FORMAT(5E16.8)\n"
                + "\n"
                + "%FLAG ANGLE_EQUIL_VALUE\n"
                + "%FORMAT(5E16.8)\n"
                + "\n"
                + "%FLAG DIHEDRAL_FORCE_CONSTANT\n"
                + "%FORMAT(5E16.8)\n"
                + "\n"
                + "%FLAG DIHEDRAL_PERIODICITY\n"
                + "%FORMAT(5E16.8)\n"
                + "\n"
                + "%FLAG DIHEDRAL_PHASE\n"
                + "%FORMAT(5E16.8)\n"
                + "\n"
                + "%FLAG SOLTY\n"
                + "%FORMAT(5E16.8)\n"
                + "  0.00000000E+00  0.00000000E+00\n"
                + "%FLAG LENNARD_JONES_ACOEF\n"
                + "%FORMAT(5E16.8)\n"
                + "  5.81935564E+05  0.00000000E+00  0.00000000E+00\n"
                + "%FLAG LENNARD_JONES_BCOEF\n"
                + "%FORMAT(5E16.8)\n"
                + "  5.94825035E+02  0.00000000E+00  0.00000000E+00\n"
                + "%FLAG BONDS_INC_HYDROGEN\n"
                + "%FORMAT(10I8)\n"
                + "       3       6       1       0       3       2       0       6       2\n"
                + "%FLAG BONDS_WITHOUT_HYDROGEN\n" + "%FORMAT(10I8)\n"
                + "\n" + "%FLAG ANGLES_INC_HYDROGEN\n" + "%FORMAT(10I8)\n"
                + "\n" + "%FLAG ANGLES_WITHOUT_HYDROGEN\n"
                + "%FORMAT(10I8)\n" + "\n"
                + "%FLAG DIHEDRALS_INC_HYDROGEN\n" + "%FORMAT(10I8)\n"
                + "\n" + "%FLAG DIHEDRALS_WITHOUT_HYDROGEN\n"
                + "%FORMAT(10I8)\n" + "\n" + "%FLAG EXCLUDED_ATOMS_LIST\n"
                + "%FORMAT(10I8)\n" + "       2       3       3       0\n"
                + "%FLAG HBOND_ACOEF\n" + "%FORMAT(5E16.8)\n"
                + "  0.00000000E+00\n" + "%FLAG HBOND_BCOEF\n"
                + "%FORMAT(5E16.8)\n" + "  0.00000000E+00\n"
                + "%FLAG HBCUT\n" + "%FORMAT(5E16.8)\n"
                + "  0.00000000E+00\n" + "%FLAG AMBER_ATOM_TYPE\n"
                + "%FORMAT(20a4)\n" + "OW  HW  HW\n"
                + "%FLAG TREE_CHAIN_CLASSIFICATION\n" + "%FORMAT(20a4)\n"
                + "BLA BLA BLA\n" + "%FLAG JOIN_ARRAY\n"
                + "%FORMAT(10I8)\n" + "       0       0       0\n"
                + "%FLAG IROTAT\n" + "%FORMAT(10I8)\n"
                + "       0       0       0\n" + "%FLAG RADIUS_SET\n"
                + "%FORMAT(1a80)\n" + "modified Bondi radii (mbondi)\n"
                + "%FLAG RADII\n" + "%FORMAT(5E16.8)\n"
                + "  1.50000000E+00  8.00000000E-01  1.20000000E+00\n"
                + "%FLAG SCREEN\n" + "%FORMAT(5E16.8)\n"
                + "  8.50000000E-01  8.50000000E-01  8.50000000E-01\n";
                
                FileUtility.printDefaultInput(app,"amber_sample0.top",newInput);          
                
            }else if (app.equalsIgnoreCase(Invariants.APP_NAME_DMOL3)) {

                System.out.println("Now DMOL3 Run");
                  
                newInput = "Put or load your input text here";
                FileUtility.printDefaultInput(app,"dmol3.car",newInput);
                
                newInput =  "Put or load your input text here"  ;
                FileUtility.printDefaultInput(app,"dmol3.input",newInput);
                    
            }else if (app.equalsIgnoreCase(Invariants.APP_NAME_CASTEP)) {

                System.out.println("Now CASTEP Run");
                  
                newInput = "Put or load your input text here";
                FileUtility.printDefaultInput(app,"castep.cell",newInput);
                
                newInput =  "Put or load your input text here"  ;
                FileUtility.printDefaultInput(app,"castep.param",newInput);
                
            } else if (app.equalsIgnoreCase(Invariants.APP_NAME_ADF)) {

                System.out.println("Trying ADF Run");
                newInput = "Title Water Opt\n" + "\n" + "Atoms\n"
                        + "   O   0.0     0.0        0.0\n"
                        + "   H   0.0    -0.68944   -0.578509\n"
                        + "   H   0.0     0.689440  -0.578509\n" + "End\n" + "\n"
                        + "Basis\n" + "  Type TZP\n" + "  Core Small\n" + "End\n"
                        + "\n" + "Geometry\n" + "  Optim Deloc\n" + "End\n" + "\n"
                        + "End Input\n";

                FileUtility.printDefaultInput(app,"adf_sample0.inp",newInput);
                
            } else if (app.equalsIgnoreCase(Invariants.APP_NAME_WIEN2K)) {

                System.out.println("Trying Wien2k Run");
                newInput = "Not yet";
                FileUtility.printDefaultInput(app,"wien2k_sample0.inp",newInput);
                
            }else if (app.equalsIgnoreCase(Invariants.APP_NAME_ACES3)) {

                System.out.println("Trying ACES3 Run");
                newInput = "O2 \n"
                    + "O \n"
                    + "O 1 B1 \n"
                    + "\n B1 = 1.68420053 \n"
                    + "\n *ACES2(CALC=CCSD,BASIS=CC-PVTZ,MEMORY=10000000,REF=RHF,SPHERICAL=ON, \n"
                    + "DIRECT=ON,INTEGRALS=GAMESS, MULT=1, CC_CONV=8, SYMMETRY=OFF) \n"
                    + "\n\n"
                    + "*SIP \n"
                    + "MAXMEM= 2000 \n"
                    + "COMPANY = 1 1 3 0 \n"
                    + "IOCOMPANY = 2 1 1 0 \n"
                    + "SIAL_PROGRAM = scf_rhf_isymm_diis10.sio  \n"
                    + "SIAL_PROGRAM = lccd_rhf.sio \n" ;

                FileUtility.printDefaultInput(app,"aces3_ZMAT",newInput);;
                
            }else if (app.equalsIgnoreCase(Invariants.APP_NAME_ADF_QUILD)) {

                System.out.println("Trying ADF_Quild Run");
                newInput = "title Geometry optimization \n"
                    + "EPRINT \n"
                    + "  SFO  NOEIG  NOOVL \n"
                    + "END\n"
                    + "XC \n"
                    + " GGA BLYP \n"
                    + "END\n"
                    + "ATOMS\n"
                    + "O   0.000000   0.000000    0.000000\n"
                    + "C   0.000000   0.000000    0.000000\n"
                    + "END\n"
                    + "BASIS\n"
                    + "  type DZ\n" 
                    + "  core NONE\n" 
                    + "END\n"
                    + "GEOMETRY\n"
                    + "END\n"
                    + "SCF\n"
                    + " converge  1.0e-5  1.03-5\n"
                    + " diis ok=0.01\n"
                    + "END\n"
                    + "QUILD\n"
                    + "  cvg_grid 1.0e-4\n"
                    + "  numgrid 1\n"
                    + "  SMETAGGA B3LYP(VWN5)\n"
                    + "END\n"
                    + "METAGGA\n"
                    + "HFEXCHANGE\n"
                    + "INTEGRATION 5.0  5.0\n"
                    + "endinput\n";

                FileUtility.printDefaultInput(app,"quild_sample0.inp",newInput);
                
            } else {
                
                System.out.println("Trying"+app+" Run");
                newInput = "You can load your input here by clicking addfile button";
                FileUtility.printDefaultInput(app,"default.inp",newInput);
                
            }
        }
    }
    
    private class appModuleComboListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                return;
            }

            System.out.println("(Debug) choosen module name:"+e.getItem());
            
            String app = appName(getAppPackageName(), (String)e.getItem());

            createAndShowSampleJob(app);
    
        }
    }
    
    
    

    // called when the different machine is selected
    private class machListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {
                if (hpcList.getSelectedValue() == null) {
                    return;
                } else {
                    if (GridChem.isCCG_MW1Active()) { // do it the CCG way
                        populateProjects(hpcList.getSelectedValue()
                                .toString());
                        populateQueues(hpcList.getSelectedValue()
                                .toString());
                    } else { // do it the Swarna way!
                        if (hpcList.getSelectedValue().toString().equals(
                                "cu.ncsa.uiuc.edu")) {
                            changeProject("kts");
                            changeQueue("batch");
                        } else if (hpcList.getSelectedValue().toString()
                        		.equals("co.ncsa.uiuc.edu")) {
                            changeProject("dck");
                            changeQueue("standard");
                        } else if (hpcList.getSelectedValue().toString()
                                .equals("ccg-login.ncsa.uiuc.edu")) {
                            changeProject("mjk");
                            changeQueue("gridchem");
                        } else if (hpcList.getSelectedValue().toString()
                                .equals("longhorn.tacc.utexas.edu")) {
                            changeProject("A-gridchem");
                            changeQueue("normal");
                        } else if (hpcList.getSelectedValue().toString()
                                .equals("mike4.cct.lsu.edu")) {
                            changeProject("gaussian");
                            changeQueue("workq");
                        }
                        // else if
                        // (apphpcBoard.getSelectedValue().toString().equals("agt-login.ccs.uky.edu"))
                        // {
                        // // is this misspelling of gaussian correct? srb;aug
                        // 05
                        // changeProject("guassian");
                        // changeQueue("debug");
                        // }
                        else if (hpcList.getSelectedValue().toString()
                                .equals("sdx.uky.edu")) {
                            changeProject("mjk");
                            changeQueue("gaussian");
                        } else if (hpcList.getSelectedValue().toString()
                                .equals("ccg-login.epn.osc.edu")) {
                            changeProject("gaussian");
                            changeQueue("agt_pbs");
                        } else if (hpcList.getSelectedValue().toString()
                                .equals("tg-login1.osc.edu")) {
                            changeProject("gaussian");
                            changeQueue("parallel");
                        } else if (hpcList.getSelectedValue().toString()
                                .equals("co.ncsa.uiuc.edu")) {
                            changeProject("dck");
                            changeQueue("standard");
                        }
                    }
                }
            }
        }
    }

    /**
     * We extend the DefaultListCellRender class to provide custom tool tips for
     * the HPC machine list. This will allow us to dynamically generate tool
     * tips based on current resource load as the user goes about their normal
     * work.
     * 
     * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
     * 
     */
    private class HPCCellRenderer extends DefaultListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);
            setToolTipText(createTTTHTML((String) value));
            return this;
        }

        private String createTTTHTML(String hpcName) {
            String toolTipHTML = "";

            if (hpcName.equals(SCHEDULER)) {
                toolTipHTML += "<html><body bgcolor=\"#666666\"><table bgcolor=\"#666666\">";
                toolTipHTML += "<tr><th colspan=\"2\"  bgcolor=\"#9999FF\">Machine Summary</th></tr>";
                toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Name:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                        + SCHEDULER + "</FONT></td></tr>";
                toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Description:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                        + "<p>This option will enable the CCG middleware to select</p>"
                        + "<p>a resource for you based on the values you have entered</p>"
                        + "<p>for this job.</FONT></td></tr>";
                toolTipHTML += "</table></body></html>";

                return toolTipHTML;

            } else {

                ComputeBean hpc = GridChem.getMachineByName(hpcName);

                toolTipHTML += "<html><body bgcolor=\"#666666\"><table bgcolor=\"#666666\">";
                toolTipHTML += "<tr><th colspan=\"2\"  bgcolor=\"#9999FF\">Machine Summary</th></tr>";
                toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Name:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                        + hpc.getName() + "</FONT></td></tr>";
                toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Location:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                        + hpc.getSite().getAcronym() + "</FONT></td></tr>";
                toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Description:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                        + hpc.getSite().getDescription() + "</FONT></td></tr>";
                toolTipHTML += "<tr><th colspan=\"2\"  bgcolor=\"#9999FF\"><b>Current Loads</th></tr>";
                // toolTipHTML += "<tr><td><FONT
                // COLOR=\"#FFFFFF\"><b>Queue:</b></FONT></td><td><FONT
                // COLOR=\"#FFFFFF\">" + hpc.getLoad().getQueueName() +
                // "</FONT></td></tr>";

                if (hpc.getName().equals("Condor")) {
                    toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Running CPU:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                            + hpc.getLoad().getJobsRunning()
                            + "</FONT></td></tr>";
                    toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Idle CPU:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                            + hpc.getLoad().getJobsQueued()
                            + "</FONT></td></tr>";
                    toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Utilization:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                            + hpc.getLoad().getCpu() + "%</FONT></td></tr>";
                } else {
                    toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>CPU:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                            + hpc.getLoad().getCpu() + "%</FONT></td></tr>";
                    toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Memory:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                            + hpc.getLoad().getMemory() + "%</FONT></td></tr>";
                    toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Disk:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                            + hpc.getLoad().getDisk() + "%</FONT></td></tr>";
                    toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Queue:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                            + hpc.getLoad().getJobsRunning()
                            + "R/"
                            + hpc.getLoad().getJobsQueued()
                            + "Q/"
                            + hpc.getLoad().getJobsOther()
                            + "O"
                            + "</FONT></td></tr>";
                }

                toolTipHTML += "</table></body></html>";

                return toolTipHTML;
            }
        }
    }

    /**
     * We extend the JLabel and use it to render the cells of a JComboBox. Each
     * cell contains a Jlabel with a class to provide custom tool tips for the
     * HPC machine list. This will allow us to dynamically generate tool tips
     * based on current resource load as the user goes about their normal work.
     * 
     * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
     * 
     */
    private class QueueComboBoxRenderer extends JLabel implements
            ListCellRenderer {

        public QueueComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            ComputeBean hpc = null;

            QueueBean selectedQueue = null;

            if (((String) value).equals(UNSPECIFIED)) {

            	selectedQueue = new QueueBean();
            	selectedQueue.setName(UNSPECIFIED);
            	selectedQueue.setDefaultQueue(true);


            } else {
                int selectedMachineIndex = hpcList.getSelectedIndex();

                String hpcName = (String) hpcListModel.get(selectedMachineIndex);

                hpc = GridChem.getMachineByName(hpcName);
                if (hpc == null) {
                	selectedQueue = new QueueBean();
                	selectedQueue.setName(UNSPECIFIED);
                	selectedQueue.setDefaultQueue(true);
                } else {
	                for (QueueBean q : hpc.getQueues()) {
	
	                    if (q.getName().equals((String) value)) {
	
	                        selectedQueue = q;
	
	                    }
	                }
                }

            }

            // display the default queue in bold
            if (selectedQueue.isDefaultQueue()) {

                Font font = new Font(getFont().getName(), Font.BOLD, getFont()
                        .getSize());

                setFont(font);

            } else {

                Font font = new Font(getFont().getName(), Font.PLAIN, getFont()
                        .getSize());

                setFont(font);

            }

            setText(selectedQueue.getName());

            setToolTipText(createTTTHTML(selectedQueue, hpc));

            return this;
        }

        private String createTTTHTML(QueueBean q, ComputeBean hpc) {

            String toolTipHTML = "";
            if (q.getName().equals(UNSPECIFIED)) {
                toolTipHTML += "<html><body bgcolor=\"#666666\"><table bgcolor=\"#666666\">";
                toolTipHTML += "<tr><th colspan=\"2\"  bgcolor=\"#9999FF\">Queue Summary</th></tr>";
                toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Name:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                        + UNSPECIFIED + "</FONT></td></tr>";
                toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Description:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                        + "<p>You have selected the grid scheduler option in the</p>"
                        + "<p>machine list. A appropriate queue based on the run</p>"
                        + "<p>time you request will be selected for you when you</p>"
                        + "<p>submit this job.</p></FONT></td></tr>";
                toolTipHTML += "</table></body></html>";

                return toolTipHTML;
            }

            toolTipHTML += "<html><body bgcolor=\"#666666\"><table bgcolor=\"#666666\">";
            toolTipHTML += "<tr><th colspan=\"2\"  bgcolor=\"#9999FF\">Queue Summary</th></tr>";
            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Name:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + q.getName() + "</FONT></td></tr>";
            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Status:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + q.getStatus() + "</FONT></td></tr>";
            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Max Queue Size:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + q.getMaxQueuedJobs() + "</FONT></td></tr>";
            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Max Running Jobs :</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + q.getMaxRunningJobs() + "</FONT></td></tr>";

            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Wall Clock Limit (hh:mm):</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + resolveTimeLimit(q.getMaxWallClockTime())
                    + "</FONT></td></tr>";

            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>CPU Time Limit (hh:mm):</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + resolveTimeLimit(q.getMaxCpuTime())
                    + "</FONT></td></tr>";

            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Max Nodes Per Job:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + q.getMaxNodes() + "</FONT></td></tr>";
            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Max CPUs Per Job:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + q.getMaxCpus() + "</FONT></td></tr>";
            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Memory Limit Per CPU (MB):</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + q.getMaxCpuMem() + "</FONT></td></tr>";
            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Description:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + q.getComment() + "</FONT></td></tr>";

            toolTipHTML += "<tr><th colspan=\"2\"  bgcolor=\"#9999FF\"><b>Current Loads</th></tr>";
            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Running:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + q.getRunning() + "</FONT></td></tr>";
            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Waiting:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + q.getWaiting() + "</FONT></td></tr>";
            toolTipHTML += "<tr><td><FONT COLOR=\"#FFFFFF\"><b>Other:</b></FONT></td><td><FONT COLOR=\"#FFFFFF\">"
                    + q.getOther() + "</FONT></td></tr>";

            toolTipHTML += "<tr><th colspan=\"2\"  bgcolor=\"#9999FF\"><b>This is "
                    + ((q.isDefaultQueue()) ? "" : "not")
                    + " the default queue for " + hpc.getName() + "</th></tr>";

            toolTipHTML += "</table></body></html>";

            return toolTipHTML;
        }

        /**
         * Create a string representation of the Calendar object in hh:mm format
         * and where the hours field ranges from 0 - 32k
         */
        private String resolveTimeLimit(Calendar cal) {
            if (cal == null) {
                return null;
            } else {
                int hours = getIntegerHours(cal);
                return ((hours == 0) ? "00" : hours) + ":"
                        + cal.get(Calendar.MINUTE);
            }
        }

        private int getIntegerHours(Calendar cal) {
            int days = (cal.get(Calendar.DAY_OF_YEAR) - 1) * 24;
            int hours = cal.get(Calendar.HOUR_OF_DAY);
            return days + hours;
        }
    }

    class HourFieldVerifier extends InputVerifier {

        public boolean verify(JComponent input) {
            System.out.println("trapped text verification");
            Integer newTimeUnit = null;

            try {
                if (input instanceof JFormattedTextField) {
                    JFormattedTextField ftf = (JFormattedTextField) input;

                    AbstractFormatter formatter = ftf.getFormatter();
                    if (formatter != null) {
                        String text = ftf.getText();
                        try {
                            newTimeUnit = new Integer(text);
                            System.out.println("value was caught");
                        } catch (Exception pe) {
                            JOptionPane
                                    .showMessageDialog(null,
                                            "Please enter a valid value in the time field.");

                            System.out.println("Time of " + text
                                    + " is not valid.");
                        }
                    }

                    QueueBean q = getSelectedQueue();

                    if (q != null) {
                        Calendar qWallLimit = q.getMaxWallClockTime();
                        System.out.println("Queue " + q.getName() + " is "
                                + ((q.isDefaultQueue()) ? "" : "not")
                                + " default.");

                        if (newTimeUnit.intValue() > getIntegerHours(qWallLimit)
                                && newTimeUnit.intValue() > 1) {
                            // if the value was reset

                            JOptionPane
                                    .showMessageDialog(
                                            null,
                                            "Current wall time value of "
                                                    + newTimeUnit.intValue()
                                                    + ":"
                                                    + minnm.getNumber()
                                                            .intValue()
                                                    + " hours exceeds "
                                                    + "\nthe maximum queue wall time of "
                                                    + resolveTimeLimit(qWallLimit)
                                                    + " hours\non "
                                                    + hpcList
                                                            .getSelectedValue()
                                                            .toString()
                                                    + "'s "
                                                    + getSelectedQueue()
                                                    + " queue. Time will be reset"
                                                    + "\nto the maximum time limit of the "
                                                    + getSelectedQueue()
                                                    + "\nqueue.",
                                            "Queue Wall Time Error",
                                            JOptionPane.OK_OPTION);

                            System.out.println("Current wall time value of "
                                    + newTimeUnit.intValue() + ":"
                                    + minnm.getNumber().intValue()
                                    + " hours exceeds "
                                    + "\nthe maximum queue wall time of "
                                    + resolveTimeLimit(qWallLimit)
                                    + " hours\non "
                                    + hpcList.getSelectedValue().toString()
                                    + "'s " + getSelectedQueue()
                                    + " queue. Time will be reset"
                                    + "\nto the maximum time limit of the "
                                    + getSelectedQueue() + "\nqueue.");

                            // ftf.setText(((Integer)hrnm.getMaximum()).toString());
                            ftf
                                    .setText(new Integer(
                                            getIntegerHours(qWallLimit))
                                            .toString());
                            minnm.setValue(qWallLimit.get(Calendar.MINUTE));
                            // minnm.setValue(minnm.getMaximum());
                            ftf.selectAll();

                            // set the flag so the job will not be submitted
                            validTime = false;

                            return false;
                        }
                        /*
                         * else { String message = ""; if
                         * (getIntegerHours(qWallLimit) == 0 && ) { message =
                         * "Maximum wall time for this queue is\nless than 1
                         * hour."; JOptionPane.showMessageDialog(null, message,
                         * "Queue Wall Time Error", JOptionPane.OK_OPTION);
                         * ftf.selectAll(); // set the flag so the job will not
                         * be submitted validTime = false;
                         * 
                         * return false; }
                         *  }
                         */
                    }
                }
            } catch (Exception ex) {
                System.out.println("value was bad in spinner");
                ((JFormattedTextField) input).selectAll();
                // set the flag so the job will not be submitted
                validTime = false;

                return false;
            }

            validTime = true;

            return true;
        }
    }

    class MinuteFieldVerifier extends InputVerifier {

        public boolean verify(JComponent input) {
            Integer newTimeUnit = null;

            try {
                if (input instanceof JFormattedTextField) {
                    JFormattedTextField ftf = (JFormattedTextField) input;

                    AbstractFormatter formatter = ftf.getFormatter();
                    if (formatter != null) {
                        String text = ftf.getText();
                        try {
                            newTimeUnit = new Integer(text);
                            System.out.println("value was caught");
                        } catch (Exception pe) {
                            JOptionPane
                                    .showMessageDialog(null,
                                            "Please enter a valid value in the minute wall time field.");
                            validTime = false;
                            System.out.println("Time of " + text
                                    + " is not valid.");
                        }
                    }

                    QueueBean q = getSelectedQueue();

                    if (q != null) {
                        Calendar qWallLimit = q.getMaxWallClockTime();
                        System.out.println("Queue " + q.getName() + " is "
                                + ((q.isDefaultQueue()) ? "" : "not")
                                + " default.");

                        Calendar requestedTime = Calendar.getInstance();
                        requestedTime.clear();
                        requestedTime.add(Calendar.HOUR_OF_DAY, ((Integer) hrnm
                                .getValue()).intValue());
                        requestedTime.add(Calendar.MINUTE, newTimeUnit
                                .intValue());

                        // make sure it's a valid value in the range 0-59
                        if (newTimeUnit.intValue() > 0
                                && (newTimeUnit.intValue() < 60 || ((Integer) minnm
                                        .getMaximum()).intValue() < 60)) {
                            // if it's valid, make sure the overall time request
                            // is valid. this checks
                            // that if the time limit on the queue is 4:30 that
                            // they can enter 3:59, but
                            // 4:59 is caught.
                            if (requestedTime.getTimeInMillis() > qWallLimit
                                    .getTimeInMillis()) {

                                JOptionPane
                                        .showMessageDialog(
                                                null,
                                                "Current wall time value of "
                                                        + hrnm.getNumber()
                                                                .intValue()
                                                        + ":"
                                                        + newTimeUnit
                                                                .intValue()
                                                        + " hours exceeds "
                                                        + "\nthe maximum queue wall time of "
                                                        + resolveTimeLimit(qWallLimit)
                                                        + " hours\non "
                                                        + hpcList
                                                                .getSelectedValue()
                                                                .toString()
                                                        + "'s "
                                                        + getSelectedQueue()
                                                        + " queue. Time will be reset"
                                                        + "\nto the maximum time limit of the "
                                                        + getSelectedQueue()
                                                        + "\nqueue.",
                                                "Queue Wall Time Error",
                                                JOptionPane.OK_OPTION);

                                System.out
                                        .println("Current wall time value of "
                                                + hrnm.getNumber().intValue()
                                                + ":"
                                                + newTimeUnit.intValue()
                                                + " hours exceeds "
                                                + "\nthe maximum queue wall time of "
                                                + resolveTimeLimit(qWallLimit)
                                                + " hours\non "
                                                + hpcList
                                                        .getSelectedValue()
                                                        .toString()
                                                + "'s "
                                                + getSelectedQueue()
                                                + " queue. Time will be reset"
                                                + "\nto the maximum time limit of the "
                                                + getSelectedQueue()
                                                + "\nqueue.");

                                // ftf.setText(((Integer)minnm.getMaximum()).toString());
                                ftf.setText(new Integer(qWallLimit
                                        .get(Calendar.MINUTE)).toString());
                                ftf.selectAll();

                                // set the flag so the job will not be submitted
                                validTime = false;

                                return false;
                            }
                        } else {
                            JOptionPane
                                    .showMessageDialog(
                                            null,
                                            "Please enter a value in the range [0:59].",
                                            "Queue Wall Time Error",
                                            JOptionPane.OK_OPTION);
                            ftf.selectAll();
                            // set the flag so the job will not be submitted
                            validTime = false;

                            return false;
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("value was bad in spinner");
                ((JFormattedTextField) input).selectAll();
                // set the flag so the job will not be submitted
                validTime = false;
                return false;
            }
            validTime = true;
            return true;
        }
    }

    class NumProcFieldVerifier extends InputVerifier {

        public boolean verify(JComponent input) {
            Integer newProcCount = null;

            try {
                if (input instanceof JFormattedTextField) {
                    JFormattedTextField ftf = (JFormattedTextField) input;

                    AbstractFormatter formatter = ftf.getFormatter();
                    if (formatter != null) {
                        String text = ftf.getText();
                        try {
                            newProcCount = new Integer(text);
                            System.out.println("processor value was caught");
                        } catch (Exception pe) {
                            JOptionPane
                                    .showMessageDialog(null,
                                            "Please enter a valid value in the minute wall time field.");
                            validTime = false;
                            System.out.println("Time of " + text
                                    + " is not valid.");
                        }
                    }

                    QueueBean q = getSelectedQueue();

                    if (q != null) {
                        // make sure it's a valid value in the range 0-59
                        if (newProcCount.intValue() <= q.getMaxCpus()
                                && newProcCount.intValue() > 0) {
                            JOptionPane
                                    .showMessageDialog(
                                            null,
                                            "The requested number of CPU's, "
                                                    + newProcCount
                                                    + ", exceeds "
                                                    + "\nthe maximum number of processors allowed on the "
                                                    + getSelectedQueue()
                                                    + "\nqueue of "
                                                    + hpcList
                                                            .getSelectedValue()
                                                            .toString()
                                                    + ". The requested CPU field "
                                                    + "\nwill be reset to the maximum CPU count of the\n"
                                                    + getSelectedQueue()
                                                    + "\nqueue.",
                                            "Requested CPU Error",
                                            JOptionPane.OK_OPTION);

                            System.out
                                    .println("The requested number of CPU's, "
                                            + newProcCount
                                            + ", exceeds\nthe maximum number of processors allowed on the "
                                            + getSelectedQueue()
                                            + "\nqueue of "
                                            + hpcList.getSelectedValue()
                                                    .toString()
                                            + ". The requested CPU field "
                                            + "\nwill be reset to the maximum CPU count of the\n"
                                            + getSelectedQueue() + "\nqueue.");

                            // ftf.setText(((Integer)minnm.getMaximum()).toString());
                            ftf.setText(new Integer(q.getMaxCpus()).toString());
                            ftf.selectAll();

                            // set the flag so the job will not be submitted
                            validTime = false;

                            return false;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Please enter a value in the range [1:"
                                        + q.getMaxCpus() + "].",
                                "Queue Wall Time Error", JOptionPane.OK_OPTION);

                        ftf.selectAll();

                        // set the flag so the job will not be submitted
                        validTime = false;

                        return false;
                    }

                }
            } catch (Exception ex) {
                System.out.println("value was bad in spinner");
                ((JFormattedTextField) input).selectAll();
                // set the flag so the job will not be submitted
                validTime = false;
                return false;
            }
            validTime = true;
            return true;
        }
    }
    
//    // the following two functions are used for mapping between app name registered in GMS_WS and 
//    // a pair of name (an appPakcage name and a module name) appearing in GUI Job Editor panel. 
//    // This part is still a kind of tricky, but it can hide any complicated details on mapping.
//    private String[] appPackageAndModuleName(String appName){
//        
//        String[] names = {null, null};
//        
//        for(Enumeration keys = APP_MODULE_HASHTABLE.keys(); keys.hasMoreElements(); ){
//
//            Object appPackage_name = keys.nextElement();
//            HashSet<String> set = new HashSet<String>(APP_MODULE_HASHTABLE.get(appPackage_name));
//                
//            for(Object ob : set){
//                String str = (String)ob;
//                
//                if(appName.toUpperCase().contains(str.toUpperCase())){
//                    names[1] = str;
//                    names[0] = (String) appPackage_name;
//                }
//            }
//        }
//        System.out.println("\n(DEBUG) appName : " + appName +" Then, appPackageName :"+names[0] + "  module name  :" + names[1] + "\n");
//        
//        return names ;
//        
//    }

    public String appName(String appPackageName, String moduleName){
        
        String appName = null;
        
        if(appPackageName.equalsIgnoreCase(moduleName)){
            appName = appPackageName;
        }else{
            appName = appPackageName + "_" + moduleName ;
        }
        
        for(String str: APP_NAME_HASHSET){
            if(str.equalsIgnoreCase(appName)){
                appName = str;
            }
        }
        
        System.out.println("\n(DEBUG) appName :"+appName + " from appPacakge : "+ appPackageName + " moduleName " + moduleName);
        
        return appName ;
    }   
    
    
    public void createAndShowSampleJob(String app){
        
        updateInputInfoPanel(this.job,FileUtility.getDefaultInputFiles(app));
        layoutRequirementsPane();
    }

    public JobBean createSampleJob(String app){
        
        
//        newJob.setApplication(app);
        
//        ComputeBean hw = GridChem.hardware.get(0);
//        newJob.setSubmitMachine(hw.getName());
//        SoftwareBean sw = hw.getSoftware().iterator().next();
//        newJob.setApplication(app);
//        newJob.setProjectName(hw.getResourceProjects().iterator().next());
//        newJob.setQueue(hw.getQueueNames().iterator().next().getName());
//        newJob.setRequestedCpus(new Long(1));
//        
//        Calendar cal = Calendar.getInstance();
//        cal.clear();
//        cal.add(Calendar.MINUTE,30);
//        
//        newJob.setRequestedCpuTime(cal);
        return new JobBean();
        
    }
    
    // TODO: find better way to validate file names
    public boolean verifyInput() throws IOException {
    // some application specific validification process before sending a job
        boolean ok = true;
        String appName = appName(getAppPackageName(), getModuleName());
        
        if(appName.equalsIgnoreCase(Invariants.APP_NAME_ACES3)) {
            int reqCpu = 0;
            
            try { 
                BufferedReader br = new BufferedReader(new FileReader(getInputFiles().get(0)));
                String line = "";
                String fileText = "";
                while ((line = br.readLine()) != null){
                    if((line.toUpperCase()).startsWith("COMPANY")){
                        String[] token = (line.split("="))[1].trim().split(" ");
                        reqCpu = reqCpu + Integer.parseInt(token[2]);
                    
                        System.out.println("(Debug) "+"token0 :"+token[0] +" token1 : " + token[1] + " token2 :" + token[2] + " token3 :" + token[3]);
                        System.out.println("\n(Info) # of cpu from COMPANY : " + Integer.parseInt(token[2]) );
                    }
                    if((line.toUpperCase()).startsWith("IOCOMPANY")){
                        String[] token = (line.split("="))[1].trim().split(" ");
                        reqCpu = reqCpu + Integer.parseInt(token[2]);
                    
                        System.out.println("\n(Info) # of cpu from ICOMPANY : " + Integer.parseInt(token[2]) );
                    }           
                }
            } catch (IOException e) {
                ok = false;
            }
            
            if (reqCpu != getNumProc()){
                
                JOptionPane.showMessageDialog(null, "Requested number of cpu inconsistent with input file. \n  Your input and Requirements ask different # of cpus", 
                        "Change the number of CPUs",JOptionPane.INFORMATION_MESSAGE);
        
                ok = false;
            }
            
        } else if(appName.equalsIgnoreCase(Invariants.APP_NAME_DMOL3)){
            
            String jobName = getJobName();
            
            boolean validCar = false;
            boolean validInput = false;
            
            if (getInputFiles().size() > 2) {
                JOptionPane.showMessageDialog(null, "Number of input files exceeds the max allowed.", 
                        "Input file violation",JOptionPane.WARNING_MESSAGE);

                ok = false;
                
            } else {
                File carFile = null;
                File inputFile = null;
            
                // there should only be 2 files: a car and an input
                for (File file: getInputFiles()) {
                    if (file.getName().endsWith(".car"))
                        carFile = file;
                    else if (file.getName().endsWith(".input"))
                        inputFile = file;
                }
                
                // both files should be present
                if (carFile == null ) {
                    JOptionPane.showMessageDialog(null, "Please specify a file ending in .car", 
                            "Car file name violation",JOptionPane.INFORMATION_MESSAGE);
            
                    ok = false;
                }
                //fixed bug -nik
                if (inputFile == null) {
                    JOptionPane.showMessageDialog(null, "Please specify a file ending in .input", 
                            "Input file name violation",JOptionPane.INFORMATION_MESSAGE);
            
                    ok = false;
                }
                
                int inputFileBase = inputFile.getName().lastIndexOf('.');
                int carFileBase = carFile.getName().lastIndexOf('.');
                if (!carFile.getName().substring(0,carFileBase).equals(inputFile.getName().substring(0,inputFileBase)) ){
                    
                    JOptionPane.showMessageDialog(null, "Input files should have the same basename", 
                            "Base name violation",JOptionPane.INFORMATION_MESSAGE);
    
                    ok = false;
                    
                }
                
                if (ok == true){
                    if (jobName != inputFile.getName().substring(0,inputFileBase) ){
                        String newJobName = inputFile.getName().substring(0,inputFileBase);
                     
                        JOptionPane.showMessageDialog(null, "job name "+ jobName + " will be changed to the basename of input files :" + newJobName, 
                            "Job name change",JOptionPane.OK_OPTION);
    
                        changeJobNameField(newJobName);
                    
                    }
                }
            }
        } else if(appName.equalsIgnoreCase(Invariants.APP_NAME_NAMD)){
            
            String jobName = getJobName();
            
            if (getInputFiles().size() > 4) {
                JOptionPane.showMessageDialog(null, "Number of input files exceeds the max allowed.", 
                        "Input file violation",JOptionPane.WARNING_MESSAGE);

                ok = false;
                
            } else {
                File namdFile = null;
                File paramsFile = null;
                File pdbFile = null;
                File psfFile = null;
            
                // there should only be 4 files: 
                for (File file: getInputFiles()) {
                    if (file.getName().endsWith(".namd"))
                    	namdFile = file;
                    else if (file.getName().endsWith(".params"))
                    	paramsFile = file;
                    else if (file.getName().endsWith(".pdb"))
                    	pdbFile = file;
                    else if (file.getName().endsWith(".psf"))
                    	psfFile = file;
                }
               
                if (namdFile == null ) {
                    JOptionPane.showMessageDialog(null, "Please specify a file ending in .namd", 
                            "Car file name violation",JOptionPane.INFORMATION_MESSAGE);
            
                    ok = false;
                }
                
                if (paramsFile == null) {
                    JOptionPane.showMessageDialog(null, "Please specify a file ending in .params", 
                            "Input file name violation",JOptionPane.INFORMATION_MESSAGE);
            
                    ok = false;
                }
             
                if (pdbFile == null ) {
                    JOptionPane.showMessageDialog(null, "Please specify a file ending in .pdb", 
                            "Car file name violation",JOptionPane.INFORMATION_MESSAGE);
            
                    ok = false;
                }
                
                if (psfFile == null) {
                    JOptionPane.showMessageDialog(null, "Please specify a file ending in .psf", 
                            "Input file name violation",JOptionPane.INFORMATION_MESSAGE);
            
                    ok = false;
                }
                
                int namdFileBase = namdFile.getName().lastIndexOf('.');
                int paramsFileBase = paramsFile.getName().lastIndexOf('.');
                if (!namdFile.getName().substring(0,namdFileBase).equals(paramsFile.getName().substring(0,paramsFileBase)) ){
                    
                    JOptionPane.showMessageDialog(null, "Input files should have the same basename", 
                            "Base name violation",JOptionPane.INFORMATION_MESSAGE);
    
                    ok = false;
                    
                }
                
                if (ok == true){
                    if (jobName != namdFile.getName().substring(0,namdFileBase) ){
                        String newJobName = paramsFile.getName().substring(0,namdFileBase);
                     
                        JOptionPane.showMessageDialog(null, "job name "+ jobName + " will be changed to the basename of input files :  " + newJobName, 
                            "Job name change",JOptionPane.OK_OPTION);
    
                        changeJobNameField(newJobName);
                    
                    }
                }
            }
        } else if (appName.equalsIgnoreCase(Invariants.APP_NAME_FLUENT)) {
        	String jobName = getJobName();
        	
        	File inFile = null;
        	
        	for (File file: getInputFiles()) {
        		if (file.getName().endsWith(".in")) {
        			inFile = file;
        		}
        	}
        	
        	if (inFile == null ) {
                JOptionPane.showMessageDialog(null, "Please specify a file ending in .in", 
                        "Input file name violation",JOptionPane.INFORMATION_MESSAGE);
        
                ok = false;
            }
        	
        	int inFileBase = inFile.getName().lastIndexOf('.');
        	if (ok == true){
                if (jobName != inFile.getName().substring(0, inFileBase) ){
                    String newJobName = inFile.getName().substring(0, inFileBase);
                 
                    JOptionPane.showMessageDialog(null, "job name "+ jobName + " will be changed to the basename of input files :  " + newJobName, 
                        "Job name change",JOptionPane.OK_OPTION);

                    changeJobNameField(newJobName);
                
                }
            }
        } else if (appName.equalsIgnoreCase(Invariants.APP_NAME_ABAQUS)) {
        	
        	
        }
        else if(appName.equalsIgnoreCase(Invariants.APP_NAME_CASTEP)){
            
            String jobName = new String(getJobName());
            
            if (getInputFiles().size() > 2) {
                JOptionPane.showMessageDialog(null, "Number of input files exceeds the max allowed.", 
                        "Input file violation",JOptionPane.WARNING_MESSAGE);

                ok = false;
                
            } else {
                File cellFile = null;
                File paramFile = null;
            
                // there should only be 2 files: a car and an input
                for (File file: getInputFiles()) {
                    if (file.getName().endsWith(".cell"))
                        cellFile = file;
                    else if (file.getName().endsWith(".param"))
                        paramFile = file;
                }
                
                // both files should be present
                if (cellFile == null ) {
                    JOptionPane.showMessageDialog(null, "Please specify a file ending in .cell", 
                            "Cell file name violation",JOptionPane.INFORMATION_MESSAGE);
            
                    ok = false;
                }
                //fixed bug -nik
                if (paramFile == null) {
                    JOptionPane.showMessageDialog(null, "Please specify a file ending in .param", 
                            "Parameter file name violation",JOptionPane.INFORMATION_MESSAGE);
            
                    ok = false;
                }
                
                int inputFileBase = paramFile.getName().lastIndexOf('.');
                int carFileBase = cellFile.getName().lastIndexOf('.');
                if (!cellFile.getName().substring(0,carFileBase).equals(paramFile.getName().substring(0,inputFileBase)) ){
                    
                    JOptionPane.showMessageDialog(null, "Input files should have the same basename", 
                            "Base name violation",JOptionPane.INFORMATION_MESSAGE);
    
                    ok = false;
                    
                }
                
                if (ok == true){
                    if (jobName != paramFile.getName().substring(0,inputFileBase) ){
                        String newJobName = paramFile.getName().substring(0,inputFileBase);
                     
                        JOptionPane.showMessageDialog(null, "job name "+ jobName + " will be changed to the basename of input files :" + newJobName, 
                            "Job name change",JOptionPane.OK_OPTION);
    
                        changeJobNameField(newJobName);
                    
                    }
                }
            }
            
        } else if(appName.equalsIgnoreCase(Invariants.APP_NAME_AMBER_SANDER)){
            
            if((getNumProc() == 1)&&(getSubmitMachine().equalsIgnoreCase("Mercury"))){
            
                JOptionPane.showMessageDialog(null, "A single processor job for amber is not supported with Mercury\nUse cobalt at NCSA", 
                    "Mercury Amber single processor violation",JOptionPane.INFORMATION_MESSAGE);

                ok = false;
            }
            if((getNumProc() == 1)&&(getSubmitMachine().equalsIgnoreCase("Tungsten"))){
                
                JOptionPane.showMessageDialog(null, "A single processor job for amber is not supported with Tungsten\nUse cobalt at NCSA", 
                    "Tungsten Amber single processor violation",JOptionPane.INFORMATION_MESSAGE);

                ok = false;
            }           
            
        } else if (appName.equalsIgnoreCase(Invariants.APP_NAME_CHARMM_MPI)) {
        	String jobName = getJobName();
        	File inputFile = null;
        	
        	// Looking for main input file
            for (File file: getInputFiles()) {
                if (file.getName().endsWith(".inp")) {
                    inputFile = file;
                    break;
                }
            }
            
            if (null == inputFile) {
            	ok = false;
            	JOptionPane.showMessageDialog(null,"Main input file not found. It should have .inp as extension", 
						"Error",JOptionPane.ERROR_MESSAGE);
            } else {
            	try {
            		
            		List<String> dataFiles = CharmmInputFileParser.parse(inputFile);
            		for (String dataFile : dataFiles) {
            			boolean containsFlag = false;
            			for (File file : getInputFiles()) {
            				if (file.getName().contains(dataFile)) {
            					containsFlag = true;
            					break;
            				}
            			}
            			
            			if (false == containsFlag) {
            				ok = false;
        					JOptionPane.showMessageDialog(null,"Data file missing, please upload file " + dataFile, 
            						"Error",JOptionPane.ERROR_MESSAGE);
        					break;
        				}
            		}
            	} catch (CharmmInputFileParsingException e) {
            		JOptionPane.showMessageDialog(null, e.getMessage(), 
                            "Error",JOptionPane.ERROR_MESSAGE);
            		ok = false;
            	}
            }          
        	
        	if (ok == true){
        		int inputFileBase = inputFile.getName().lastIndexOf('.');
        		inputFileBase = (inputFileBase > 14) ? 14 : inputFileBase; // limite the job name under 15 characters.
                if (jobName != inputFile.getName().substring(0,inputFileBase) ){
                    String newJobName = inputFile.getName().substring(0,inputFileBase);
                 
                    JOptionPane.showMessageDialog(null, "job name "+ jobName + " will be changed to the basename of input files :" + newJobName, 
                        "Job name change",JOptionPane.OK_OPTION);

                    changeJobNameField(newJobName);
                
                }
            }
        	
        } else if (appName.contains(Invariants.APP_NAME_DDSCAT)) {
        	
        	if (getInputFiles().size() > 6) {
        		throw new IOException("Error specifying input files: No more than six input files are allowed for " + appName + " jobs.");
            }
        	
        } else {
        	
            if (getInputFiles().isEmpty() && inputFilePanel.retrieveTextInput().isEmpty()) {
            	ok = false;
                throw new IOException("Please specify an input file.");
            } else if (getInputFiles().size() > 6) {
                ok = false;
                throw new IOException("Error specifying input files: No more than three input files are allowed for " + appName + " jobs.");
            } 
            
            File f;
            if (!getInputFiles().isEmpty()) {
            	f = getInputFiles().get(0);
            } else {
            	f = new File(Env.getApplicationDataDir() + Settings.fileSeparator
                        + "temp.txt");
            	FileUtilities.writeStringToFile(f, inputFilePanel.retrieveTextInput());
            }
            
            //this.inputFilePanel.addFileInput(f);
            
            LogicalFileBean lf = new LogicalFileBean();
            lf.setJobId(-1);
            lf.setLocalPath(f.getAbsolutePath());
            
            this.job.addInputFile(lf);
            //this.job.getInputFiles().clear();
            //this.job.getInputFiles().add(lf);
            
            System.out.println("Verifying file " + f.getAbsolutePath() + " for application " + appName);
            
            if(appName.equalsIgnoreCase(Invariants.APP_NAME_NWCHEM)) {
                
                if (!f.getName().substring(f.getName().lastIndexOf(".")+1).equals("nw")) {
                    ok = false;
                    throw new IOException("Error specifying input files: " + appName + " input files must end in \".nw\".");
                }

            } else if(appName.equalsIgnoreCase(Invariants.APP_NAME_QMCPACK)) {
                if (!f.getName().substring(f.getName().lastIndexOf(".")+1).equals("xml")) {
                    ok = false;
                    throw new IOException("Error specifying input files: " + appName + " input files must end in \".xml\".");
                }                
            }
            
        }
        
        return ok;
        
    }
    
    public static void main(String[] args) {
        String username;
        String password;
        AccessType projectType;
        String myproxyUsername = "";
        String myproxyPassword = "";
        
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        //      Establish session with the GMS_WS
        GridChem gc = new GridChem();
        
        Properties props = new Properties();
        
        Settings.WEBSERVICE = true;
        
        // Read in user information from the configuration file
        try {
            props.load(new FileInputStream("etc/test.properties"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        username = props.getProperty("gridchem.username");
        
        password = props.getProperty("gridchem.password");
        
        // Authenticate with the GMS_WS
        if (Settings.DEBUG)
            System.out.println("Logging " + username + " into the CCG.");
        try {
            GMS3.login(username,password, AccessType.COMMUNITY, new HashMap<String,String>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Settings.authenticated = true;
        Settings.authenticatedGridChem = true;
        Settings.gridchemusername = username;
        
        System.out.println("project type: " + (String)props.getProperty("access.type"));
        
        // Load the user's resources into the session.
        ProjectBean project = null;
        if(((String)props.getProperty("access.type"))
                .toUpperCase().equals(AccessType.COMMUNITY.toString())) {
            projectType = AccessType.COMMUNITY;
            System.out.println("selected a community project");
        } else {
            System.out.println("project type is " + (String)props.getProperty("access.type"));
            projectType = AccessType.EXTERNAL;
            myproxyUsername = props.getProperty("myproxy.username");
            myproxyPassword = props.getProperty("myproxy.password");
        }
        
        try {
            for(ProjectBean p: GMS3.getProjects()) {
                if(p.getType().equals(projectType)) {
                    project = p;
                }
            }
            
            GMS3.setCurrentProject(project);
            
            if (Settings.DEBUG)
                System.out.println("Successfully loaded user's VO");
            
            GridChem.user = GMS3.getProfile();
            
            GridChem.systems = GMS3.getHardware(GridChem.project.getId());
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    EditJobPanel ejp = new EditJobPanel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        });
    }
}

//    // (Jan. 2007) This is a temporary strategy until JobBean and FileBean are used.
//    //  Briefly, input text delivered with JobBean contains the main input and file names and other files are transferred
//    // via PutFile.
//    // In the server-side, there will be a perl script that extracts the main input as well as file names
//    String prepareInputForMultipleInputApp(String appName){
// // called in doAddJobToQueue()
//        String header1 = "[Gridchem_multiple_input] \n";
//        String header2 = "[App_Name : "+appName+"] \n";
//        String file_start = "[File_Name:";
//        String file_end = "[End:";
//        
//        String inp = "" + header1 + header2;
//        
//        for(int i=0 ; i < getFileNames().size(); i++){
//            String fname = getFileNames().get(i);
//            String input = getInputs().get(i);
//    
//            // we will try sending only the main input and filenames but additional files will be sent separately
//            // using CGI (currently) or FileTransfer Techniques
//
//            if (fname.contains(File.separator)){
//                String tmpStr = fname.substring(fname.lastIndexOf(File.separator)+1);
//                fname = tmpStr;
//            }
//            
//            if (i==0){
//                inp = inp + file_start+fname+"] \n" + input + "\n" + file_end+fname+"] \n";
//            }else{
//                File tmpFile = new File(getFileNames().get(i));
//                if(tmpFile.exists()){
//                    PutInputFile(fname);
//                }else{
//                    PutInputFile(fname, input);
//                }
//                inp = inp + file_start+fname+"] \n"  + file_end+fname+"] \n";
//            }
//        }
//
//        return inp;
//    }
    
//    private void  PutInputFile(String fName){
//        
//        try{
//            System.setProperty("javax.net.ssl.trustStore", Env.getTrustedCaDir()+ File.separator +"ccgkeystore");
//            System.out.println("trustStore is in :"+Env.getTrustedCaDir() + File.separator +"ccgkeystore");
//            
//            URL cgiURL = new URL(Invariants.httpsGateway + "putinputfile.cgi");
//            System.out.println("PutFile: URL cgiURL " + cgiURL.toString() + 
//                    " successfully initialized");
//
//            URLConnection connex = cgiURL.openConnection();
//            connex.setDoOutput(true);
//
//            PrintWriter outStream = new PrintWriter(connex.getOutputStream());
//
//            File f = new File(fName);   
//            BufferedReader br ;
//            StringBuilder fileText = new StringBuilder();
//            String line=null;
//            String s=null;      
//            String userName;
//            
//            try{
//                br = new BufferedReader(new FileReader(f));
//                while ((line = br.readLine()) != null){
//                    fileText = fileText.append(line + "\n");
//                }
//                
//            }catch(IOException ioex){
//                System.out.println("File reading error :" + ioex + "with " + fName);
//            }
//            
//            String fText = URLEncoder.encode(fileText.toString());
//             
//            if (Settings.authenticatedGridChem) {
//                    userName = URLEncoder.encode("ccguser");
//                    outStream.println("IsGridChem=" + URLEncoder.encode("true"));
//                    System.err.println("PutInutFile:IsGridChem=" + "true");
//            } else {
//                    userName = URLEncoder.encode(Settings.name.getText());
//                    outStream.println("IsGridChem=" + URLEncoder.encode("false"));
//                    System.err.println("PutInputFile:IsGridChem=" + "false");
//            }
//                
//            outStream.println("Username=" + userName);
//            System.err.println("PutFile:Username="+userName); 
//            
//            outStream.println("GridChemUsername="+URLEncoder.encode(Settings.gridchemusername));
//            System.out.println("PutInputFile:GridChemUsername=" + URLEncoder.encode(Settings.gridchemusername));
//            
//            outStream.println("fileName=" + fName);
//            System.out.println("PutInputFile:FileName="+fName);
//            
//            outStream.println("fileText=" + fText);
//            System.out.println("PutFile:fileText="+fText);
//        
//            outStream.close();
//            
//            BufferedReader inStream = new BufferedReader(new InputStreamReader(connex.getInputStream()));
//            
//            while ((s = inStream.readLine()) != null){
//                    int sLength = s.length();
//            }
//                
//            inStream.close();
//            
//        }catch (IOException ioe){
//                System.out.println("PutInputFile in editingStuff:IOException");
//                System.out.println(ioe.toString());
//                ioe.printStackTrace();
//        }
//
//    }
//    
//    private void  PutInputFile(String fName, String fileText){
//        System.out.println("\n(DEBUG) this is in PutInputFile with inputs generated without a saved file :"+fName); 
//        
//        try{
//            
//            System.setProperty("javax.net.ssl.trustStore", Env.getTrustedCaDir()+ File.separator +"ccgkeystore");
//            System.out.println("trustStore is in :"+Env.getTrustedCaDir() + File.separator +"ccgkeystore");
//            
//            URL cgiURL = new URL(Invariants.httpsGateway + "putinputfile.cgi");
//            System.out.println("PutFile: URL cgiURL " + cgiURL.toString() + 
//                    " successfully initialized");
//            URLConnection connex = cgiURL.openConnection();
////          char[] filetext = {};
//            connex.setDoOutput(true);
//            PrintWriter outStream = new PrintWriter(connex.getOutputStream());
//            String s;
//            String userName;
//            
//            String fText = URLEncoder.encode(fileText);
//             
//            if (Settings.authenticatedGridChem) {
//                    userName = URLEncoder.encode("ccguser");
//                    outStream.println("IsGridChem=" + URLEncoder.encode("true"));
//                    System.err.println("PutInutFile:IsGridChem=" + "true");
//            } else {
//                    userName = URLEncoder.encode(Settings.name.getText());
//                    outStream.println("IsGridChem=" + URLEncoder.encode("false"));
//                    System.err.println("PutInputFile:IsGridChem=" + "false");
//            }
//                
//            outStream.println("Username=" + userName);
//            System.err.println("PutFile:Username="+userName); 
//            
//            outStream.println("GridChemUsername="+URLEncoder.encode(Settings.gridchemusername));
//            System.out.println("PutInputFile:GridChemUsername=" + URLEncoder.encode(Settings.gridchemusername));
//            
//            outStream.println("fileName=" + fName);
//            System.out.println("PutInputFile:FileName="+fName);
//            
//            outStream.println("fileText=" + fText);
//            System.out.println("PutFile:fileText="+fText);
//       
//            outStream.close();
//            
//            BufferedReader inStream = new BufferedReader(new 
//                    InputStreamReader(connex.getInputStream()));
//            
//            while ((s = inStream.readLine()) != null){
//                    int sLength = s.length();
//            }
//                
//            inStream.close();
//
//            
//        }catch (IOException ioe){
//                System.out.println("SendInputFile in editingStuff:IOException");
//                System.out.println(ioe.toString());
//                ioe.printStackTrace();
//        }
//
//    }

    
    
    
    
//    private void verifyInputsForMultipleInputApp(){
//// called in init()     
//        
//        ArrayList<File> inputFiles = new ArrayList<File>();
//        ArrayList<String> inputs = new ArrayList<String>();
//        String headerString1 = "[Gridchem_multiple_input]";
//        String headerString2 = "[App_Name :";
//        String fileStartString = "[File_Name:";
//        String fileEndString = "[End:"; 
//        
//        String[] lines = this.job.getInput().split("\n");
//
//        if(lines[0].contains(headerString1)){
//            String inputText = "";
//            for(String line : lines){
//                if(line.contains(headerString2)){
//                    String App_Name = line.split(":")[1].replace("]","").trim();
//                    System.out.println("\n(INFO) Now found "+ App_Name + " from a merged input");
//                }else if(line.contains(fileStartString)){
//                    String FileName = line.split(":")[1].replace("]","").trim();
//                    System.out.println("\nNow "+ FileName + " is extracted \n");
//                    File newFile = new File(FileName);
//                    inputFiles.add(newFile);
//                    inputText = "";
//                }else if(line.contains(fileEndString)){
//                    System.out.println("\nExtraction of "+ inputFiles.get(inputFiles.size()-1).getName()+ " is finished \n");
//                
//                    inputs.add(inputText);
//                    
//                    System.out.println("\nDEBUG input"+inputText);
//                }else {
//                    if(!(line.equals(lines[0]))){
//                        inputText = inputText.concat(line + "\n");
//                    }
//                }
//            }
//            
//            this.job.setInputs(inputs);
//            this.job.setInputFiles(inputFiles);
//        }
//        
//    }
//
//}
