/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on May 3, 2005
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

package org.gridchem.client;

import org.gridchem.client.common.Settings;
import org.gridchem.client.gui.panels.myccg.MonitorVO;
import org.gridchem.service.beans.JobBean;

/**
 * Helper class to call the main SubmitJob class to submit a job to the queue.
 * 
 * @author lots of other people
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class doSubmitJobs
{
	public static int nsubmitted;
	public static int selectedindex;
	
    doSubmitJobs() throws Exception
    {
		JobBean j = new JobBean();
		SubmitJob sj = null;;
		nsubmitted = 0;
		boolean hadSuccess = false;
        
        int[] selectedJobsIndices = stuffInside.queueList.getSelectedIndices();
        
        System.out.println("User requested " + selectedJobsIndices.length + " jobs be submitted.");
        for(int i=selectedJobsIndices.length-1; i>=0; i--) {
            j = SubmitJobsWindow.jobQueue.get(selectedJobsIndices[i]);
            System.out.println("Preparing to submit job " + i + ": " + j.getName());
            selectedindex = i;
            // submit the Job j to the queue
            if (Settings.WEBSERVICE){
                // turn off update timer in the monitor window while
                // we are submitting these jobs.  the timer interfears
                // with the update chain of events from taking place
                // and the submit routine will auto refresh the monitor
                // window after every submission anyway.
                if (GridChem.oc.monitorWindow != null) {
                    GridChem.oc.monitorWindow.setUpdate(false);
                }
                
                hadSuccess = sj.isJobSubmitted();
                
                if (hadSuccess) {
                    System.out.println("Finished submitting job " + i + 
                            ": " + j.getName());
                } else {
                    System.out.println("Failed to submit job " + i + 
                            ": " + j.getName());
                }
            } else {
                sj = new SubmitJob(j);
            }
        }
        
        // we need to be careful not to open the monitorVO panel or schedule an
        // update if no job succeeded. To do this, we keep a boolean flag, 
        // hadSuccess, which will be set to true if ANY of the queue of jobs
        // succeeded.  If a single job submitted, the monitorVO panel will be
        // opened.  If none succeeded, it will not be opened because there will
        // be nothing new to see.
        if (hadSuccess) {
            // restart the timer
            if (GridChem.oc.monitorWindow != null) {
                GridChem.oc.monitorWindow.setUpdate(true);
                GridChem.oc.monitorWindow.refresh();
            } else {
                GridChem.oc.monitorWindow = new MonitorVO();
                GridChem.oc.monitorWindow.refresh();
            }
        }
        
//        stuffInside.dsb.stop();
//		stuffInside.dsb.done();
    }
    
//    private JobBean convertJobToBean(Job j) throws ParseException {
//        
//        JobBean jobBean = new JobBean();
//        
//        jobBean.setApplication(j.getApp());
//        jobBean.setInput(j.getInput());
//        jobBean.setName(j.getJobName());
//        jobBean.setProjectName(j.getProject());
//        jobBean.setQueue(j.getQueue());
//        jobBean.setRequestedCpus(new Long(j.getNumProc()));
//        
//        String[] time = j.getTime().split(":");
//        System.out.println("Job time is " + time[0] + ":" + time[1]);
//        
//        Calendar requestedCpuTime = Calendar.getInstance();
//        requestedCpuTime.clear();
//        requestedCpuTime.add(Calendar.MINUTE, new Integer(time[1]).intValue());
//        requestedCpuTime.add(Calendar.HOUR_OF_DAY, new Integer(time[0]).intValue());
//        
//        jobBean.setRequestedCpuTime(requestedCpuTime);
//        
//        System.out.println("Job requested " + 
//                (((double)requestedCpuTime.get(Calendar.DAY_OF_YEAR) - 1) * 24 + 
//                        (double) requestedCpuTime.get(Calendar.HOUR_OF_DAY) + 
//                        (double)requestedCpuTime.get(Calendar.MINUTE) / 60) + 
//                " hours.");
//        
//        //jobBean.setScratchDir(j.getScrd());
//        jobBean.setRequestedMemory(new Long(1024));
//        jobBean.setSubmitMachine(j.getMachine(0));
//        jobBean.setResearchProjectName(j.getResProj());
//        
//        return jobBean;
//    }
}

























