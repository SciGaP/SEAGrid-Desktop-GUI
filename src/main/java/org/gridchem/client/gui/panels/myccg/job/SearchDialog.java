/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Jul 17, 2006
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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.gui.jobsubmission.commands.SEARCHCommand;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.SearchBean;
import org.gridchem.service.model.enumeration.JobSearchFilterType;
import org.gridchem.service.model.enumeration.JobSearchParameterTypes;
import org.gridchem.service.model.enumeration.JobStatusType;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.autocomplete.ComboBoxAdaptor;

/**
 * Dialog box for searching job history by various attributes.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class SearchDialog extends JDialog implements ActionListener,KeyListener,ItemListener {
    private static final String BEFORE = "BEFORE";
    private static final String AFTER = "AFTER";
    private static final String GT = ">";
    private static final String GEQ = ">=";
    private static final String LT = "<";
    private static final String LEQ = "<=";
    private static final String EQ = "=";
    private static final String NEQ = "!=";
    private static final String LIKE = "LIKE";
    private static final String NOT_LIKE = "NOT LIKE";
    private static final String PREFIX = "BEGINS WITH";
    private static final String SUFFIX = "ENDS WITH";
    
    private static String[] dateSearchTerms = 
        {BEFORE,AFTER,EQ,NEQ};
    private static String[] textualSearchTerms = 
        {EQ,NEQ,LIKE,NOT_LIKE,PREFIX,SUFFIX};
    private static String[] numericSearchTerms = {EQ,NEQ,GT,GEQ,LT,LEQ};
    
    private JTextField jobName;
    private JTextField researchProjectName;
    private JTextField application;
    private JTextField computeResource;
    private JTextField jobID;
    private JTextField localJobID;
    private JComboBox jobStatus;
    
    private JComboBox jobNameMatchCombo;
    private JComboBox researchProjectNameMatchCombo;
    private JComboBox applicationMatchCombo;
    private JComboBox computeResourceMatchCombo;
    private JComboBox jobIDMatchCombo;
    private JComboBox localJobIDMatchCombo;
    private JComboBox jobStatusMatchCombo;
    private JComboBox afterSpinnerComboBox;
    private JComboBox beforeSpinnerComboBox;
    private JXDatePicker afterDatePicker;
    private JXDatePicker beforeDatePicker;
    
    private JLabel jobNameLabel;
    private JLabel researchProjectNameLabel;
    private JLabel applicationLabel;
    private JLabel computeResourceLabel;
    private JLabel jobIDLabel;
    private JLabel localJobIDLabel;
    private JLabel jobStatusLabel;
    
    private JButton searchButton;
    private JButton cancelButton;
    private JButton clearButton;
    
    private JobBean job = null;
    private SearchBean search = new SearchBean();
    
    boolean changedAfterDate = false;
    boolean changedBeforeDate = false;
    boolean changedJobStatusType = false;
    
    Status status;
    StatusListener listener;
    
    protected SearchDialog(StatusListener listener) {
        this();
        
        this.status = Status.UNKNOWN;
        
        this.listener = listener;
    }
    
    protected SearchDialog() {
    
        super();
    
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.addKeyListener(this);
        JPanel criteriaPanel = new JPanel(new GridLayout(3,1,10,10));
        
        ((GridLayout) criteriaPanel.getLayout()).setVgap(5);
        
        Border titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Search Criteria");
        Border bufferBorder = BorderFactory.createEmptyBorder(10,10,10,10);
        
        criteriaPanel.setBorder(
                BorderFactory.createCompoundBorder(titledBorder, bufferBorder));
        
        criteriaPanel.addKeyListener(this);
        
        //getInfoDialogPanel.setPreferredSize(new Dimension(430,215));
        criteriaPanel.setLayout(new BoxLayout(criteriaPanel,BoxLayout.Y_AXIS));
        criteriaPanel.add(createUpperCriteriaPanel());
        criteriaPanel.add(createDivider());
        criteriaPanel.add(createLowerCriteriaPanel());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHEAST;
        
        searchPanel.add(criteriaPanel,c);
        
        c.gridy = 1;
        c.anchor = GridBagConstraints.SOUTHEAST;
        searchPanel.add(createButtonPanel(),c);
  
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setContentPane(searchPanel);
        setTitle("Job Search Dialog");
        
        setResizable(false);
        pack();
        setVisible(true);
    }
    
    /**
     * Create upper search attribute panel.
     * 
     * @return
     */
    private JPanel createUpperCriteriaPanel() {
        JPanel upperPanel = new JPanel(new GridLayout(1,6,5,15));
        
        // declare the 6 textual search parameters along with the combo box
        // allowing them to choose what type of search to perform on each field
        jobName = new JTextField("");
        jobName.addKeyListener(this);
        jobNameLabel = new JLabel("Job Name");
        jobNameMatchCombo = new JComboBox(textualSearchTerms);
        jobNameMatchCombo.setSelectedIndex(2);
        
        researchProjectName = new JTextField("");
        researchProjectName.addKeyListener(this);
        researchProjectNameLabel = new JLabel("Research Project");
        researchProjectNameLabel.setLabelFor(researchProjectName);
        researchProjectNameMatchCombo = new JComboBox(textualSearchTerms);
        
        application = new JTextField("");
        application.addKeyListener(this);
        applicationLabel = new JLabel("Application");
        applicationLabel.setLabelFor(application);
        applicationMatchCombo = new JComboBox(textualSearchTerms);
        
        computeResource = new JTextField("");
        computeResource.addKeyListener(this);
        computeResourceLabel = new JLabel("HPC System");
        computeResourceLabel.setLabelFor(computeResource);
        computeResourceMatchCombo = new JComboBox(textualSearchTerms);
        
        jobID = new JTextField("");
        jobID.addKeyListener(this);
        jobIDLabel = new JLabel("CCG Job ID");
        jobIDLabel.setLabelFor(jobID);
        jobIDMatchCombo = new JComboBox(numericSearchTerms);
        
        localJobID = new JTextField("");
        localJobID.addKeyListener(this);
        localJobIDLabel = new JLabel("Local Job ID");
        localJobIDLabel.setLabelFor(localJobID);
        localJobIDMatchCombo = new JComboBox(textualSearchTerms);
        
        JPanel p11 = new JPanel();
        p11.setLayout(new GridLayout(3,1,5,0));
        p11.add(jobNameLabel);
        p11.add(applicationLabel);
        p11.add(computeResourceLabel);
        
        JPanel p12 = new JPanel();
        p12.setLayout(new GridLayout(3,1,5,5));
        p12.add(jobNameMatchCombo);
        p12.add(applicationMatchCombo);
        p12.add(computeResourceMatchCombo);
        
        JPanel p13 = new JPanel();
        p13.setLayout(new GridLayout(3,1,5,5));
        p13.add(jobName);
        p13.add(application);
        p13.add(computeResource);
        
        JPanel p21 = new JPanel();
        p21.setLayout(new GridLayout(3,1,5,0));
        p21.add(jobIDLabel);
        p21.add(localJobIDLabel);
        p21.add(researchProjectNameLabel);
        
        JPanel p22 = new JPanel();
        p22.setLayout(new GridLayout(3,1,5,5));
        p22.add(jobIDMatchCombo);
        p22.add(localJobIDMatchCombo);
        p22.add(researchProjectNameMatchCombo);
        
        JPanel p23 = new JPanel();
        p23.setLayout(new GridLayout(3,1,5,5));
        p23.add(jobID);
        p23.add(localJobID);
        p23.add(researchProjectName);
        
        
        upperPanel.add(p11);
        upperPanel.add(p12);
        upperPanel.add(p13);
        upperPanel.add(p21);
        upperPanel.add(p22);
        upperPanel.add(p23);
        
        return upperPanel;
    }
    
    private JPanel createDivider() {
        JPanel divider = new JPanel(new GridLayout(1,1));
        
        divider.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JLabel lineLabel = new JLabel("<html><div align=\"center\" width=\"700\"><HR></div></html>");
        
        divider.add(lineLabel);
        
        return divider;
    }
    
    /**
     * Create lower search attribute panel
     * 
     * @return
     */
    private JPanel createLowerCriteriaPanel() {
        
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new GridLayout(1,3,5,0));
        
        jobStatus = new JComboBox(JobStatusType.values());
        jobStatus.addActionListener(new ComboBoxAdaptor(jobStatus));
        jobStatus.insertItemAt("",0);
        jobStatus.setSelectedIndex(0);
        jobStatus.addKeyListener(this);
        jobStatus.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                changedJobStatusType = true;
            }
        });
        jobStatusLabel = new JLabel("Status");
        jobStatusLabel.setLabelFor(jobStatus);
        
        JPanel statusPanel = new JPanel();
        statusPanel.add(jobStatusLabel);
        statusPanel.add(jobStatus);
        
        JPanel afterPanel = new JPanel();
        afterDatePicker = addDateSelector(afterPanel,
                "Search After",
                "Find jobs starting after this date.",true);
//        afterSpinner.addKeyListener(this);
//        
        JPanel beforePanel = new JPanel();
        beforeDatePicker = addDateSelector(beforePanel,
                "Search Before",
                "Find jobs ending before the given date.",false);
//        beforeSpinner.addKeyListener(this);
        
        lowerPanel.add(statusPanel);
        lowerPanel.add(afterPanel);
        lowerPanel.add(beforePanel);
        
        return lowerPanel;
    }
    
    /**
     * Create the button panel and layout the buttons created in the
     * makeButtons() call.
     * 
     * @return
     */
    private JPanel createButtonPanel() {
        
        makeButtons();
        
        JPanel buttonInterior = new JPanel();
        buttonInterior.setLayout(new GridLayout(1,3,5,0));
        buttonInterior.add(searchButton);
        buttonInterior.add(clearButton);
        buttonInterior.add(cancelButton);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(buttonInterior);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,5));
        
        return buttonPanel;
    }
    
    /**
     * Add a spinner to the given JPanel with the given label and tool tip text.
     * 
     * @param lowerInterior
     * @param label
     * @param ttt
     * @return
     */
    private JXDatePicker addDateSelector(JPanel panel,
            String label, String ttt, boolean after) {
        
        Calendar cal = Calendar.getInstance(); // today is the default before date
        if (after) { 
            cal.add(Calendar.MONTH, -1); // one month ago is the default after date
        } 
        JXDatePicker datePicker = new JXDatePicker(cal.getTime());
        datePicker.setFormats("MM/dd/yyyy");
        datePicker.setToolTipText(ttt);
        
        panel.add(new JLabel("Search " + (after?"After":"Before")),BorderLayout.EAST);
        panel.add(datePicker,BorderLayout.WEST);
        
        return datePicker;
    }
    
//    /**
//     * Return the formatted text field used by the editor, or
//     * null if the editor doesn't descend from JSpinner.DefaultEditor.
//     */
//    public JFormattedTextField getTextField(JSpinner spinner) {
//        JComponent editor = spinner.getEditor();
//        if (editor instanceof JSpinner.DefaultEditor) {
//            return ((JSpinner.DefaultEditor)editor).getTextField();
//        } else {
//            System.err.println("Unexpected editor type: "
//                               + spinner.getEditor().getClass()
//                               + " isn't a descendant of DefaultEditor");
//            return null;
//        }
//    }
//    
    /**
//     * Add the spinner and label to the given container
//     * 
//     * @param c
//     * @param label
//     * @param model
//     * @return
//     */
//    static protected JSpinner addLabeledSpinner(Container c, 
//            String label,
//            SpinnerModel model) {
//        JLabel l = new JLabel(label);
//        c.add(l);
//        
//        JSpinner spinner = new JSpinner(model);
//        l.setLabelFor(spinner);
//        c.add(spinner);
//        
//        return spinner;
//    }
    
    
    /**
     * Create and format the buttons on the panel
     */
    private void makeButtons() {
        
        searchButton = new JButton("Search");
        searchButton.setVerticalTextPosition(AbstractButton.CENTER);
        searchButton.setHorizontalTextPosition(AbstractButton.RIGHT);
        searchButton.setToolTipText(
                "Search your job history for jobs matching the given criteria.");
        searchButton.addKeyListener(this);
        searchButton.addActionListener(this);
        
        clearButton = new JButton("Clear");
        clearButton.setVerticalTextPosition(AbstractButton.CENTER);
        clearButton.setHorizontalTextPosition(AbstractButton.CENTER);
        clearButton.setToolTipText(
                "Clear the current search criteria.");
        clearButton.addKeyListener(this);
        clearButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        cancelButton.setVerticalTextPosition(AbstractButton.CENTER);
        cancelButton.setHorizontalTextPosition(AbstractButton.CENTER);
        cancelButton.setToolTipText("Click to close this window");
        cancelButton.addActionListener(this);
        cancelButton.addKeyListener(this); 
    }
    
    /**
     * Returns the results of the search dialog form in a JobBean object.
     * 
     * @return
     */
    public JobBean getResults() {
        return job;
    }
    
    private void exit() {
        this.setVisible(false);
        this.dispose();
    }
    
//    /**
//     * Translate the text in the combo box into a FilterType
//     * 
//     * @param textFilter
//     * @return
//     */
//    private FilterType resolveSearchFilter(String textFilter) {
//        FilterType type = null;
//        
//        if (textFilter.equals(BEFORE)) {
//            type = FilterType.BEFORE;
//        } else if (textFilter.equals(AFTER)) {
//            type = FilterType.AFTER;
//        } else if (textFilter.equals(EQ)) {
//            type = FilterType.EQ;
//        } else if (textFilter.equals(GT)) {
//            type = FilterType.GT;
//        } else if (textFilter.equals(GEQ)) {
//            type = FilterType.GEQ;
//        } else if (textFilter.equals(LT)) {
//            type = FilterType.LT;
//        } else if (textFilter.equals(LEQ)) {
//            type = FilterType.LEQ;
//        } else if (textFilter.equals(NEQ)) {
//            type = FilterType.NEQ;
//        } else if (textFilter.equals(LIKE)) {
//            type = FilterType.LIKE;
//        } else if (textFilter.equals(NOT_LIKE)) {
//            type = FilterType.NOT_LIKE;
//        } else if (textFilter.equals(PREFIX)) {
//            type = FilterType.PREFIX;
//        } else if (textFilter.equals(SUFFIX)) {
//            type = FilterType.SUFFIX;
//        }
//        
//        return type;
//    }
    
    /**
     * Translates from display values to enumerated JobSearchFilterType
     * 
     * @param val
     * @return
     */
    private JobSearchFilterType resolveComparisonValueToEnum(String val) {
    	
    	if (val.equals(EQ)) {
    		return JobSearchFilterType.EQ;
    	} else if (val.equals(NEQ)) {
    		return JobSearchFilterType.NEQ;
    	} else if (val.equals(GT)) {
    		return JobSearchFilterType.GT;
    	} else if (val.equals(GEQ)) {
    		return JobSearchFilterType.GEQ;
    	} else if (val.equals(LT)) {
    		return JobSearchFilterType.LT;
    	} else if (val.equals(LEQ)) {
    		return JobSearchFilterType.LEQ;
    	} else if (val.equals(BEFORE)) {
    		return JobSearchFilterType.BEFORE;
    	} else if (val.equals(AFTER)) {
    		return JobSearchFilterType.AFTER;
    	} else if (val.equals(LIKE)) {
    		return JobSearchFilterType.LIKE;
    	} else if (val.equals(NOT_LIKE)) {
    		return JobSearchFilterType.NOT_LIKE;
    	} else if (val.equals(PREFIX)) {
    		return JobSearchFilterType.PREFIX;
    	} else {
    		return JobSearchFilterType.SUFFIX;
    	} 
    }
    /**
     * Place all the search criteria into a JobBean object.
     */
    private void createCriteria() {
        
//        filter = new JobSearchFilter();
        job = new JobBean();
        
        if (jobID.getText() != null && !jobID.getText().equals("")) {
//            filter.addSearchParameter(ID, 
//                    resolveSearchFilter((String)jobIDMatchCombo.getSelectedItem()),jobID.getText());
//            job.setId(new Long(jobID.getText()));
            search.addCriteria(JobSearchParameterTypes.ID, resolveComparisonValueToEnum((String)jobIDMatchCombo.getSelectedItem()), jobID.getText());
        }
        
        if (localJobID.getText() != null && !localJobID.getText().equals("")) {
//            filter.addSearchParameter(LOCALID, 
//                    resolveSearchFilter((String)localJobIDMatchCombo.getSelectedItem()),localJobID.getText());
//            job.setLocalId(localJobID.getText());
        	search.addCriteria(JobSearchParameterTypes.LOCALID, resolveComparisonValueToEnum((String)localJobIDMatchCombo.getSelectedItem()), localJobID.getText());
        }
        
        if (jobName.getText() != null && !jobName.getText().equals("")) {
//            filter.addSearchParameter(NAME, 
//                    resolveSearchFilter((String)jobNameMatchCombo.getSelectedItem()),jobName.getText());
//            job.setName(jobName.getText());
            search.addCriteria(JobSearchParameterTypes.NAME, resolveComparisonValueToEnum((String)jobNameMatchCombo.getSelectedItem()), jobName.getText());
        }
        
        if (researchProjectName.getText() != null && !researchProjectName.getText().equals("")) {
//            filter.addSearchParameter(RESEARCH_PROJECT, 
//                    resolveSearchFilter((String)researchProjectNameMatchCombo.getSelectedItem()),researchProjectName.getText());
            job.setExperimentName(researchProjectName.getText());
            search.addCriteria(JobSearchParameterTypes.RESEARCH_PROJECT, resolveComparisonValueToEnum((String)researchProjectNameMatchCombo.getSelectedItem()), researchProjectName.getText());
        }
        
        if (application.getText() != null && !application.getText().equals("")) {
//            filter.addSearchParameter(APPLICATION, 
//                    resolveSearchFilter((String)applicationMatchCombo.getSelectedItem()),application.getText());
            job.setSoftwareName(application.getText());
            search.addCriteria(JobSearchParameterTypes.APPLICATION, resolveComparisonValueToEnum((String)applicationMatchCombo.getSelectedItem()), application.getText());
        }
        
        if (computeResource.getText() != null && !computeResource.getText().equals("")) {
//            filter.addSearchParameter(SUBMIT_MACHINE, 
//                    resolveSearchFilter((String)computeResourceMatchCombo.getSelectedItem()),computeResource.getText());
            job.setSystemName(computeResource.getText());
            search.addCriteria(JobSearchParameterTypes.SUBMIT_MACHINE, resolveComparisonValueToEnum((String)computeResourceMatchCombo.getSelectedItem()), computeResource.getText());
        }
        
        // do job status dropdown here
        if (changedJobStatusType &&
            jobStatus.getSelectedItem() != null &&
            ! jobStatus.getSelectedItem().equals("")) {
//            filter.addSearchParameter(STATUS, FilterType.EQ,
//                                      ((JobStatusType)jobStatus.getSelectedItem()).name());
            job.setStatus((JobStatusType)jobStatus.getSelectedItem());
            search.addCriteria(JobSearchParameterTypes.STATUS, JobSearchFilterType.EQ, ((JobStatusType)jobStatus.getSelectedItem()).name());
            System.out.println("Searching for jobs with status: " + job.getStatus());
        } else {
            job.setStatus(null);
        }
        
        
//        filter.addSearchParameter(CREATED,FilterType.AFTER,
//                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
//                    .format(afterDatePicker.getDate()));
        job.setStartTime(afterDatePicker.getDate());
        search.addCriteria(JobSearchParameterTypes.START_TIME, JobSearchFilterType.AFTER, afterDatePicker.getDate().getTime()+"");
        
//        filter.addSearchParameter(CREATED,FilterType.BEFORE,
//                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
//                .format(beforeDatePicker.getDate()));
        job.setStopTime(beforeDatePicker.getDate());
        search.addCriteria(JobSearchParameterTypes.STOP_TIME, JobSearchFilterType.BEFORE, beforeDatePicker.getDate().getTime()+"");
        
        
    }
    
    private void clearForm() {
        
        jobID.selectAll();
        jobID.replaceSelection("");
        jobIDMatchCombo.setSelectedIndex(0);
        
        localJobID.selectAll();
        localJobID.replaceSelection("");
        localJobIDMatchCombo.setSelectedIndex(0);
        
        jobStatus.setSelectedIndex(0);
        changedJobStatusType = false;
        
        researchProjectName.selectAll();
        researchProjectName.replaceSelection("");
        researchProjectNameMatchCombo.setSelectedIndex(0);
        
        application.selectAll();
        application.replaceSelection("");
        applicationMatchCombo.setSelectedIndex(0);
        
        computeResource.selectAll();
        computeResource.replaceSelection("");
        computeResourceMatchCombo.setSelectedIndex(0);
        
        jobName.selectAll();
        jobName.replaceSelection("");
        jobNameMatchCombo.setSelectedIndex(0);
        
        jobID.selectAll();
        jobID.replaceSelection("");
        jobIDMatchCombo.setSelectedIndex(0);
        
        Calendar cal = Calendar.getInstance(); // today is the default before date
        beforeDatePicker.setDate(cal.getTime());
        
        cal.add(Calendar.MONTH, -1); // one month ago is the default after date
        afterDatePicker.setDate(cal.getTime());
        
        
    }
    
//    private Date getSpinnerDate(JSpinner spinner) throws ParseException {
//        
//        String spinnerString = getTextField(spinner).getText() + " 00:00:01";
//        
//        Date date = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").parse(spinnerString);
//        
//        return date;
//    }

    // action listener interface implementation
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == searchButton) {
            createCriteria();
            setStatus(Status.START);
            //this.setVisible(false);
        } else if (event.getSource() == cancelButton) {
            job = null;
            clearForm();
            this.setVisible(false);
        } else if (event.getSource() == clearButton) {
            clearForm();
        }
        
    }

    // key listener interface implementation
    public void keyTyped(KeyEvent arg0) {}
    public void keyReleased(KeyEvent arg0) {}
    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();
          if (key == KeyEvent.VK_ENTER) {
              createCriteria();
              setStatus(Status.START);
              //this.setVisible(false);
          } else if(key == KeyEvent.VK_ESCAPE) {
              exit();
          }
    }
    
//    // change listener interface implementation
//    public class SpinnerListener implements ChangeListener {
//        public void stateChanged(ChangeEvent evt) {
//            JSpinner spinner = (JSpinner)evt.getSource();
//            if (spinner.getName().equals("Search After")) {
//                changedAfterDate = true;
//            } else if (spinner.getName().equals("Search Before")) {
//                changedBeforeDate = true;
//            }
//        }
//    }
    
    // item listener interface implementation
    public void itemStateChanged( ItemEvent event ) {
        if( event.getSource() == jobStatus
                && event.getStateChange() == ItemEvent.SELECTED ) {
            changedJobStatusType = true;
        }
    }
    
    public void showSearchDialog(StatusListener jobPanel) {
        SearchDialog sd = new SearchDialog(jobPanel);
    }
    
    public void setStatus(Status status) {
        this.status = status;
        SEARCHCommand search = new SEARCHCommand(this.listener);
        search.getArguments().put("search", this.job);
//        search.getArguments().put("filter", filter);
        this.listener.statusChanged(new StatusEvent(search,this.status));
    }    
    
    public static void main(String[] args) {
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SearchDialog sd = new SearchDialog();
            }
        });
    }

}


