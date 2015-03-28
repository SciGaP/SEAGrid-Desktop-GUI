/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Feb 16, 2007
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

package org.gridchem.client.gui.panels.myccg.resource;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;

import org.gridchem.client.gui.util.ETable;
import org.gridchem.service.beans.ComputeBean;
import org.gridchem.service.beans.QueueBean;
import org.gridchem.service.beans.SoftwareBean;
import org.gridchem.service.model.enumeration.ResourceType;

/**
 * Container to hold resource information in the ResourcePanel
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
@SuppressWarnings("serial")
public class ResourceInfoPanel extends JPanel {

//    private String DEFAULT_MESSAGE = "<html><p align=\"center\"><em>No resource selected.</em></p></html>";
    
    protected JTextArea contentTextArea;
    protected JScrollPane scrollPane;
    protected JLabel infoLabel;
    protected ETable m_table;
    protected ResourceInfoTableData m_data;
    
    public ResourceInfoPanel() {
        super();
        
        m_data = new ResourceInfoTableData();
        
        init();
    }
    
    /**
     * Create a display panel containing a label showing an 
     * HTML display of the information associated with this
     * HPC resource.
     * 
     * @param hpc Resource whose info will be shown in this panel.
     */
    public ResourceInfoPanel(ComputeBean hpc) {
        super();
        
        m_data = new ResourceInfoTableData();
        
        init();
        
    }
    
    private void init() {
        
        m_table = new ETable();
        m_table.setAutoCreateColumnsFromModel(true);
        
        m_table.setModel(m_data);
        
        m_table.setPreferredSize(new Dimension(575,445));
        
        add(m_table,BorderLayout.CENTER);
        
        
    }
    
    public void setResource(ComputeBean hpc) {
        
        ((ResourceInfoTableData)m_table.getModel()).setResourceData(hpc);
        
    }
    
    public void clearResource() {
        
        ((ResourceInfoTableData)m_table.getModel()).setResourceData(null);
        
    }
    
}

/**
 * Object holding the data for each row.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
class ResourceInfo {
    public String attribute;
    public String value;
    
    public ResourceInfo (String attr, String val) {
        this.attribute = attr;
        this.value = val;
    }
    
    public ResourceInfo (String attr, long val) {
        this.attribute = attr;
        this.value = new Long(val).toString();
    }
}

/**
 * Underlying table model of the resource table.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
@SuppressWarnings("serial")
class ResourceInfoTableData extends AbstractTableModel {
    static final public String[] m_columns = {"Attribute","Value"};
    
    protected Vector<ResourceInfo> m_vector;
    
    public ResourceInfoTableData(ComputeBean hpc) {
        m_vector = new Vector<ResourceInfo>();
        
        setResourceData(hpc);
    }
    
    public ResourceInfoTableData() {
        m_vector = new Vector<ResourceInfo>();
     }
    
	public void setResourceData(ComputeBean hpc) {
        m_vector.removeAllElements();
        if (hpc != null) {
            m_vector.addElement(new ResourceInfo("Machine Summary",""));
            m_vector.addElement(new ResourceInfo("Name",hpc.getName()));
            m_vector.addElement(new ResourceInfo("Clasification",ResourceType.COMPUTE.name()));
            m_vector.addElement(new ResourceInfo("Site",hpc.getSite().getAcronym()));
            m_vector.addElement(new ResourceInfo("Scheduling System",hpc.getScheduler().toString()));
            m_vector.addElement(new ResourceInfo("Total Cores",hpc.getTotalCpu()));
            m_vector.addElement(new ResourceInfo("Peak Performance",new Double(hpc.getPeakPerformance()).toString()));
            m_vector.addElement(new ResourceInfo("Memory",hpc.getTotalDisk()));
            m_vector.addElement(new ResourceInfo("Total Nodes",hpc.getTotalNodes()));
            m_vector.addElement(new ResourceInfo("Description",hpc.getComment()));
            
            m_vector.addElement(new ResourceInfo("Load Summary",""));
            if (hpc.getName().equals("Condor")) {
                m_vector.addElement(new ResourceInfo("Running CPU", hpc.getLoad().getJobsRunning()));
                m_vector.addElement(new ResourceInfo("Idle CPU", hpc.getLoad().getJobsQueued()));
                m_vector.addElement(new ResourceInfo("Utilization", hpc.getLoad().getCpu()));
            } else {
                m_vector.addElement(new ResourceInfo("CPU", hpc.getLoad().getCpu()));
                m_vector.addElement(new ResourceInfo("Memory", hpc.getLoad().getMemory()));
                m_vector.addElement(new ResourceInfo("Disk",hpc.getLoad().getDisk()));
                m_vector.addElement(new ResourceInfo("Queues" ,hpc.getLoad().getJobsRunning() + "R/" +
                    hpc.getLoad().getJobsQueued() + "Q/" + hpc.getLoad().getJobsOther() + "O"));
            }
            
            m_vector.addElement(new ResourceInfo("Software",""));
            for(SoftwareBean sw: hpc.getSoftware()) {
                m_vector.addElement(new ResourceInfo(sw.getName(), 
                        ((sw.getVersionDate() == null)?
                                "Built on " + new SimpleDateFormat("EEE, MMM m, yyyy").format(sw.getVersionDate())
                                : "")));
                m_vector.addElement(new ResourceInfo("",sw.getShortDescription()));
            }
            
            m_vector.addElement(new ResourceInfo("Queues",""));
            for(QueueBean q: hpc.getQueues()) {
                m_vector.addElement(new ResourceInfo("Name",q.getName()));
                m_vector.addElement(new ResourceInfo("CPU Time Limit",resolveTimeLimit(q.getMaxCpuTime())));
                m_vector.addElement(new ResourceInfo("Wall Time Limit",resolveTimeLimit(q.getMaxWallClockTime())));
                m_vector.addElement(new ResourceInfo("Memory Limit",q.getMaxCpuMem()));
                m_vector.addElement(new ResourceInfo("Queued Job Limit",q.getMaxQueuedJobs()));
                m_vector.addElement(new ResourceInfo("Running Job Limit",q.getMaxRunningJobs()));
                m_vector.addElement(new ResourceInfo("CPU Per Job Limit",q.getMaxCpus()));
                m_vector.addElement(new ResourceInfo("Nodes Per Job Limit",q.getMaxNodes()));
                m_vector.addElement(new ResourceInfo("Current Status","R:" + q.getRunning() + 
                        " W:" + q.getWaiting() + " O: " + q.getOther()));
                m_vector.addElement(new ResourceInfo("Notes",q.getComment()));
            }
        }
        
        fireTableDataChanged();
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
            return ((hours == 0)? "00":hours) + ":" + cal.get(Calendar.MINUTE);
        }
    }
    
    private int getIntegerHours(Calendar cal) {
        int days = (cal.get(Calendar.DAY_OF_YEAR) - 1) * 24;
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        return days + hours;
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
        
        ResourceInfo row = (ResourceInfo)m_vector.elementAt(nRow);
        
        if(nCol == 0) {
            return row.attribute;
        } else if (nCol == 1) {
            return row.value;
        } else {
            return "";
        }
    }
    
    
   
}