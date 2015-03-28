/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Feb 27, 2007
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

package org.gridchem.client.gui.charts;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

import org.gridchem.client.GridChem;
import org.gridchem.client.util.GMS3;
import org.gridchem.service.beans.CollaboratorBean;
import org.gridchem.service.beans.ComputeBean;
import org.gridchem.service.beans.ProjectBean;
import org.gridchem.service.beans.UsageBean;
import org.gridchem.service.beans.UserBean;
import org.gridchem.service.model.enumeration.AccessType;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Simple Pie chart displaying user's relative usage load.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class UsageChart extends JPanel {
    public enum ChartType {
        PROJECT("project"),
        JOB("job"),
        RESOURCE("resource"),
        USER("user");
        
        String value;
        
        private ChartType(String value) {
            this.value = value;
        }
        
        public String value() {
            return value;
        }
        
        public ChartType next() {
            ChartType[] values = this.values();
            int i;
            
            for(i=0;i<values.length;i++) {
                if (value.equals(values[i].value())) {
                    if (i == values.length - 1) {
                        i=0;
                        break;
                    } else { 
                        i++;
                        break;
                    }
                }
            }
            
            return values[i];
        }
        
        public ChartType previous() {
            ChartType[] values = this.values();
            int i;
            
            for(i=0;i<values.length;i++) {
                if (value.equals(values[i].value())) {
                    if (i == 0) {
                        i = values.length -1 ;
                        break;
                    } else { 
                        i--;
                        break;
                    }
                }
            }
            
            return values[i];
        }
    }

    protected static ChartType CURRENT_CHARTTYPE;
    protected static Hashtable<ProjectBean,List<CollaboratorBean>> projectCollabTable;
    protected JPanel navPanel;
    
    private JSpinner chartTypeSpinner;
    private CyclingSpinnerListModel chartTypeListModel;
    
    private JFreeChart chart = null;
    private ChartPanel chartPanel = null;
    protected DefaultPieDataset dataset = null;
    
    private Dimension size = new Dimension(200,200);
    
    private int defaultProjectIndex = 0;
    
    private static CollaboratorBean collab = null;
    
    public UsageChart(Hashtable<ProjectBean,List<CollaboratorBean>> projectTable) {
        super();
        
        UsageChart.projectCollabTable = projectTable;
        
        CURRENT_CHARTTYPE = ChartType.PROJECT;
        
        navPanel = createSelectionBar();
        
        init();
        
    }
    
    public UsageChart(Hashtable<ProjectBean,List<CollaboratorBean>> projectTable, ChartType type) {
        super();
        
        this.projectCollabTable = projectTable;
        
        CURRENT_CHARTTYPE = type;
        
        navPanel = createSelectionBar();
        
        init();
        
    }
    
    public UsageChart(ProjectBean project, List<CollaboratorBean> collabs, 
            ChartType type) {
        
        super();
        
        CURRENT_CHARTTYPE = type;
        
        projectCollabTable = new Hashtable<ProjectBean,List<CollaboratorBean>>();
        
        HashSet<ProjectBean> projects = new HashSet<ProjectBean>();
        projects.add(project);
        
        projectCollabTable.put(project,collabs);
        
        navPanel = createSelectionBar();
        
        init();
        
    }
    
    private void init() {
        
        removeAll();
        String title = "";
        
        if (CURRENT_CHARTTYPE.equals(ChartType.JOB)) {
//            dataset = createJobDataset(projectTable);
            dataset = new DefaultPieDataset();
        } else if (CURRENT_CHARTTYPE.equals(ChartType.PROJECT)) {
            dataset = createProjectDataset(projectCollabTable);
            title = "CCG Utilization by Project";
        } else if (CURRENT_CHARTTYPE.equals(ChartType.RESOURCE)) {
            dataset = createResourceDataset(projectCollabTable);
            title = "CCG Utilization by Resource";
        } else if (CURRENT_CHARTTYPE.equals(ChartType.USER)) {
            dataset = createUserDataset(projectCollabTable);
            title = "CCG Utilization by User";
        } 
        
        chart = ChartFactory.createPieChart(
                title,  // chart title
                dataset, // data
                false,                               // include legend
                true,                               // tooltips?
                false                               // URLs?
        );
        
        if (projectCollabTable.size() == 1) {
            ProjectBean project = projectCollabTable.keySet().iterator().next();
            chart.addSubtitle(new TextTitle("Project " + project.getName() + 
                    " expires on " + project.getEndDate()));
        }
        
        ((PiePlot)chart.getPlot()).setCircular(true);
        
//        ((PiePlot)chart.getPlot()).setExplodePercent(new Integer(defaultProjectIndex), 25);
       
        if (CURRENT_CHARTTYPE.equals(ChartType.JOB)) {
            chart.getPlot().setNoDataMessage("Comprehensive job information is not currently available.");
        }
        
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(size);
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        add(chartPanel,c);
        
        GridBagConstraints c1 = new GridBagConstraints();
        c1.weightx = 0;
        c1.weighty = 0;
        c1.gridx = 0;
        c1.gridy = 1;
        c1.fill = GridBagConstraints.BOTH;
        add(navPanel,c1);
        revalidate();
        
//        setPreferredSize(size);
        
    }

    private void init(CollaboratorBean collab) {
        
        removeAll();
        
        // TODO: Add title and footer with expiration date to chart
        String title = "";
        ProjectBean project = projectCollabTable.keySet().iterator().next();
//        
        if (CURRENT_CHARTTYPE.equals(ChartType.JOB)) {
//          dataset = createJobDataset(project);
            dataset = new DefaultPieDataset();
        } else if (CURRENT_CHARTTYPE.equals(ChartType.PROJECT)) {
            dataset = createProjectDataset(projectCollabTable, collab);
            title = "Overall Utilization of Project " + project.getName() + 
            " by " + collab.getFirstName() + " " + collab.getLastName();;
        } else if (CURRENT_CHARTTYPE.equals(ChartType.RESOURCE)) {
            dataset = createResourceDataset(projectCollabTable, collab);
            title = "CCG Resource Utilization for Project " + project.getName() + 
            " by " + collab.getFirstName() + " " + collab.getLastName();;
        } else if (CURRENT_CHARTTYPE.equals(ChartType.USER)) {
            dataset = createUserDataset(projectCollabTable, collab);
            title = "User Utilization of Project " + project.getName() + 
                " by " + collab.getFirstName() + " " + collab.getLastName();
        } 
      
        JFreeChart chart = ChartFactory.createPieChart(
              title,  // chart title
              dataset, // data
              false,                               // include legend
              true,                               // tooltips?
              false                               // URLs?
        );
      
        chart.addSubtitle(new TextTitle("Project " + project.getName() + 
                " expires on " + project.getEndDate()));
        
        if (CURRENT_CHARTTYPE.equals(ChartType.JOB)) {
            chart.getPlot().setNoDataMessage("Comprehensive job information is not currently available.");
        }
        
        ((PiePlot)chart.getPlot()).setCircular(true);
        
//        ((PiePlot)chart.getPlot()).setToolTipGenerator(new PieToolTipGenerator() {
//
//            public String generateToolTip(PieDataset ds, Comparable arg1) {
//                // TODO Auto-generated method stub
//                return ";
//            }
//            
//        });
        
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(size);
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        add(chartPanel,c);
        
        GridBagConstraints c1 = new GridBagConstraints();
        c1.weightx = 0;
        c1.weighty = 0;
        c1.gridx = 0;
        c1.gridy = 1;
        c1.fill = GridBagConstraints.BOTH;
        add(navPanel,c1);
        revalidate();
    }
    
    private JPanel createSelectionBar() {

        chartTypeListModel = new CyclingSpinnerListModel(ChartType.values());
        chartTypeSpinner = new JSpinner(chartTypeListModel);
        
        
        JFormattedTextField ftf = ((JSpinner.DefaultEditor)chartTypeSpinner.getEditor()).getTextField();
        ftf.setColumns(6); //specify more width than we need
        ftf.setHorizontalAlignment(JTextField.CENTER);
        ftf.setEditable(false);
        
        JLabel spinnerLabel = new JLabel("Chart Type");
        spinnerLabel.setLabelFor(chartTypeSpinner);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(spinnerLabel,BorderLayout.CENTER);
        buttonPanel.add(chartTypeSpinner,BorderLayout.LINE_END);
        
        return buttonPanel;
    }
    
    /**
     * Return a dataset representing all jobs run across the given
     * projects and their associated costs in SU's.  This is currently 
     * not implemented due to the cost of such a computation and data
     * transfer.
     * @param projectTable
     * @return
     */
    private DefaultPieDataset createJobDataset(
            Hashtable<AccessType,HashSet<ProjectBean>> projectTable) {
        
        DefaultPieDataset pds = new DefaultPieDataset();
        
        // add job summary info
        //pds.setValue("Used", hpc.getLoad().getQueue());
        
        return pds;
    }
    
    
    /**
     * Returns a dataset representing the relative usage of the given
     * projects against the given collaborator.
     * 
     * @param projectCollabTable
     * @return
     */
    private DefaultPieDataset createProjectDataset(
    		Hashtable<ProjectBean,List<CollaboratorBean>> projectCollabTable, 
    		CollaboratorBean collab) {
        
        DefaultPieDataset pds = new DefaultPieDataset();
        
        // add project summary info
        
        for(ProjectBean project: projectCollabTable.keySet()) {
        	CollaboratorBean collabBean = projectCollabTable.get(project).get(projectCollabTable.get(project).indexOf(collab));
        	if (collabBean != null) {
                pds.setValue(project.getName() + " Used",
                        new Double(collabBean.getTotalUsage().getUsed()));
                pds.setValue(project.getName() + " Avail.",
                        new Double(collabBean.getTotalUsage().getBalance()));
                // keep track of the current project so we can explode that piece
                // of the pie.
                if (project.equals(GridChem.project)) {
                    defaultProjectIndex = pds.getItemCount() - 1;
                }
        	}
        }
        
        
        return pds;
    }
    
    /**
     * Returns a dataset representing the cumulative resource usage across all
     * projects.
     * 
     * @param projectCollabTable
     * @return
     */
    @SuppressWarnings("unused")
	private DefaultPieDataset createResourceDataset(
            Hashtable<ProjectBean,List<CollaboratorBean>> projectCollabTable, CollaboratorBean collab) {
        
        DefaultPieDataset pds = new DefaultPieDataset();
        
        Hashtable<String,Double> resourceUsageTable = new Hashtable<String,Double>();
        
        // for each project find the collaborator's usage on each resource
        for(ProjectBean project: projectCollabTable.keySet()) {
            
        	List<CollaboratorBean> collabs = projectCollabTable.get(project);
        	
        	if (projectCollabTable.get(project).contains(collab)) {
    			
    			CollaboratorBean projectCollab = projectCollabTable.get(project).get(projectCollabTable.get(project).indexOf(collab));
    			
    			for (String systemName: projectCollab.getUsageTable().keySet()) {
    			
    				if (resourceUsageTable.containsKey(systemName)) {
    					double previousUsage = resourceUsageTable.get(systemName).doubleValue();
    	    			resourceUsageTable.remove(systemName);
    	    			resourceUsageTable.put(systemName, new Double(previousUsage + projectCollab.getUsageTable().get(systemName).getUsed()));
    	    		} else {
    	    			resourceUsageTable.put(systemName, new Double(projectCollab.getUsageTable().get(systemName).getUsed()));
    	    		}
    			}
            }
    	}
         
        // now put the tallies in the dataset
        for(String systemName: resourceUsageTable.keySet()) {
            pds.setValue(systemName,resourceUsageTable.get(systemName).doubleValue());
        }      
        
        return pds;
    }
    
    /**
     * Returns a dataset representing the cumulative usage of each user
     * across the set of projects.  
     * 
     * @param projectCollabTable
     * @return
     */
    private DefaultPieDataset createUserDataset(
    		Hashtable<ProjectBean,List<CollaboratorBean>> usageTable, CollaboratorBean collab) {
        
        DefaultPieDataset pds = new DefaultPieDataset();

        Hashtable<String,Double> userUsageTable = new Hashtable<String,Double>();
        
        // for every project 
        for(ProjectBean project: usageTable.keySet()) {
            // if the user is part of this project
        	if (usageTable.get(project).contains(collab)) {
        		userUsageTable.put(project.getName(), usageTable.get(project).get(usageTable.get(project).indexOf(collab)).getTotalUsage().getUsed());
        	}
        }
        
        // now put the tallies in the dataset
        for(String userName: userUsageTable.keySet()) {
            pds.setValue(userName, userUsageTable.get(userName).doubleValue());
        }      
        
        return pds;
    }
    
    /**
     * Return a dataset representing all jobs run under this project
     * and their associated costs in SU's.  This is currently not
     * implemented due to the cost of such a computation and data
     * transfer.
     * 
     * @param project
     * @return
     */
    private DefaultPieDataset createJobDataset(
            ProjectBean project) {
        
        DefaultPieDataset pds = new DefaultPieDataset();
        
        return pds;
    }
    
    /**
     * Returns a dataset representing the current usage of this project
     * and the unused portion of the allocation.
     * 
     * @param project
     * @return
     */
    private DefaultPieDataset createProjectDataset(
            Hashtable<ProjectBean, List<CollaboratorBean>> projectUsageTable) {
        
        DefaultPieDataset pds = new DefaultPieDataset();
        // for every project 
        for(ProjectBean project: projectUsageTable.keySet()) {
        	pds.setValue("Used", new Double(project.getUsage().getUsed()));
//        	pds.setValue("Available", new Double((project.getUsage().getAllocated() 
//                - project.getUsage().getUsed())));
        }
        return pds;
    }
    
    /**
     * Returns a dataset representing the normalized usage of this project
     * on each resource in the CCG.  The values shown will be the current
     * usage on each resource, however, in terms of display, they will
     * appear as relative to each other.
     * 
     * @param project
     * @return
     */
    private DefaultPieDataset createResourceDataset(Hashtable<ProjectBean,List<CollaboratorBean>> projectCollabTable) {
        
        DefaultPieDataset pds = new DefaultPieDataset();
        
        Hashtable<String,Double> resourceUsageTable = new Hashtable<String,Double>();
        
        for (ProjectBean project: projectCollabTable.keySet()) {
        	List<CollaboratorBean> collabs = projectCollabTable.get(project);
        
	        for(CollaboratorBean collab: collabs) {
			
				for (String systemName: collab.getUsageTable().keySet()) {
					UsageBean usage = collab.getUsageTable().get(systemName);
					
					if (resourceUsageTable.containsKey(systemName)) {
						double previousUsage = resourceUsageTable.get(systemName).doubleValue();
						resourceUsageTable.remove(systemName);
						resourceUsageTable.put(systemName, new Double(previousUsage + usage.getUsed()));
					} else {
						resourceUsageTable.put(systemName, new Double(usage.getUsed()));
					}
				}
	        }
        }
		
         
        // now put the tallies in the dataset
        for(String systemName: resourceUsageTable.keySet()) {
            pds.setValue(systemName,resourceUsageTable.get(systemName).doubleValue());
        }      
        
        return pds;
        
//        System.out.println("found specified collaborator " + collab.getLastName() + 
//                " with " + collab.getUsageTable().size() + " resource records.");
//        
//        for(String key: collab.getUsageTable().keySet()) {
//            pds.setValue(key, collab.getUsageTable().get(key).getUsed());
//        }
//        
//        return pds;
    }
    
    /**
     * Returns a dataset representing the consumption of this project's
     * allocation by each collaborator including the current user.
     * 
     * @param project
     * @return
     */
    private DefaultPieDataset createUserDataset(
    		Hashtable<ProjectBean,List<CollaboratorBean>> projectCollabTable) {
        
        DefaultPieDataset pds = new DefaultPieDataset();

        Hashtable<String,Double> userUsageTable = new Hashtable<String,Double>();
        
        for (ProjectBean project: projectCollabTable.keySet()) {
        	List<CollaboratorBean> collabs = projectCollabTable.get(project);
        
	        for(CollaboratorBean collab: collabs) {
	        	String key = collab.getFirstName() + " " + collab.getLastName();
	        	if (userUsageTable.containsKey(key)) {
	        		double oldVal = userUsageTable.get(key).doubleValue();
	        		userUsageTable.remove(key);
	        		userUsageTable.put(key, new Double(oldVal + collab.getTotalUsage().getUsed()));
	        	} else {
	        		userUsageTable.put(key,new Double(collab.getTotalUsage().getUsed()));
	        	}
	        }
        }
        
        // now put the tallies in the dataset
        for(String key: userUsageTable.keySet()) {
            pds.setValue(key,userUsageTable.get(key).doubleValue());
        }      
        
        return pds;
    }
    
    
    
    public void clear() {
        this.removeAll();
        this.projectCollabTable.clear();
    }
    
    public void setProjects(Hashtable<ProjectBean,List<CollaboratorBean>> projectCollabTable) {
        
        this.projectCollabTable = projectCollabTable;
        
        init();
        
    }
    
    public void setProject(ProjectBean project, CollaboratorBean collab) {
        
        UsageChart.collab = collab;
        
        projectCollabTable.clear();
        
        projectCollabTable.put(project,Arrays.asList(collab));
        
        init(collab);
        
    }
    
    public void setProject(ProjectBean project, List<CollaboratorBean> collabs) {
        
        collab = null;
        
        projectCollabTable.clear();
        
        projectCollabTable.put(project,collabs);
        
        init();
        
    }
    
    public void setChartType(ProjectBean project, List<CollaboratorBean> collabs, ChartType type) {
        
        CURRENT_CHARTTYPE = type;
        
        projectCollabTable.clear();
        
        projectCollabTable.put(project,collabs);
        
        if (collab == null) {
            init();
        } else {
            init(collab);
        }
    }
    
    public void setChartType(ChartType type) {
        
        CURRENT_CHARTTYPE = type;
        
        if (collab == null) {
            init();
        } else {
            init(collab);
        }
    }
    
    public class CyclingSpinnerListModel extends SpinnerListModel {
        Object firstValue, lastValue;
        SpinnerModel linkedModel = null;

        public CyclingSpinnerListModel(ChartType[] values) {
            super(values);
            firstValue = values[0];
            lastValue = values[values.length - 1];
        }

        public void setLinkedModel(SpinnerModel linkedModel) {
            this.linkedModel = linkedModel;
        }

        public Object getNextValue() {
            Object value = super.getNextValue();
            if (value == null) {
                value = firstValue;
                if (linkedModel != null) {
                    linkedModel.setValue(linkedModel.getNextValue());
                }
            }
            
            System.out.println("Changing type to " + (ChartType)value);
            
            setChartType((ChartType)value);
            
            return value;
        }

        public Object getPreviousValue() {
            Object value = super.getPreviousValue();
            if (value == null) {
                value = lastValue;
                if (linkedModel != null) {
                    linkedModel.setValue(linkedModel.getPreviousValue());
                }
            }
            System.out.println("Changing type to " + (ChartType)value);
            
            setChartType((ChartType)value);
            
            return value;
        }
    }

}
