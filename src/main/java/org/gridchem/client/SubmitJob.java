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

/** @author Sudhakar Pamidighantam, John Lee, and Xaiohai Li, NCSA
 @author Rion Dooley, CCT
 @version $Id: SubmitJob.java,v 1.17 2006/01/10 21:54:43 srb Exp $

 @return JOptionPane.ERROR_MESSAGE if app field of the Job object is not supported
 @return JOptionPane.ERROR_MESSAGE if authentication expires or has other problems
 @return JOptionPane.ERROR_MESSAGE if there is an unforeseen problem with the CGI

 This class must always remain thread-safe.  
 It will be wrapped in a SwingWrapper, since it may take a long time
 to execute; it will have a progress bar accompanying it.
 It understands the networking layer and also file I/O for data
 archiving.  
 */

package org.gridchem.client;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.exceptions.GMSException;
import org.gridchem.client.exceptions.SessionException;
import org.gridchem.client.gui.jobsubmission.commands.JobCommand;
import org.gridchem.client.gui.jobsubmission.commands.QSTATCommand;
import org.gridchem.client.gui.jobsubmission.commands.SUBMITCommand;
import org.gridchem.client.gui.jobsubmission.commands.SUBMITMANYCommand;
import org.gridchem.client.gui.login.LoginDialog;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.exceptions.PermissionException;

import com.asprise.util.ui.progress.ProgressDialog;

public final class SubmitJob implements HardWorker, StatusListener {

	private static String SUBMISSION = "org.gridchem.service.gms.exceptions.JobSubmissionException";
	private static String SCHEDULING = "org.gridchem.service.gms.exceptions.JobSchedulingException";
	private static String PERMISSION = "org.gridchem.service.gms.exceptions.PermissionException";
	private static String JOB_DESCRIPTION = "org.gridchem.service.gms.exceptions.JobException";
	public static String localJobID = "";
	private static boolean DEBUG = true;
	private int percentProgress = 0; // internally mutable!
	private int duration = 100;
	private boolean jobSubmitted = false; // internally mutable!
	private boolean mustWaitForQueues = false; // internally mutable!
	private String gbatchFilename; // internally mutable!
	private String histFilename;
	private static int SLEEP_TIME = 60000; // millisec
	private int num_try = 0; // number of try to submit a job
	private boolean psn_check = true; // internally mutable
	private boolean check_diskfull = true; // internally mutable
	private int jobIndexInQueue;
	// private WarningDialog warningDialog;
	private JobBean job;
	private List<JobBean> jobList;
	private ProgressDialog progressDialog;
	private List<ProgressDialog> progressDialogList;
	
	private boolean isSubmittingMultiple = false;

	/**
	 * Constructor used with the ws version. Takes a JobBean object and sends it
	 * to the GMS_WS for sanity checking and submission.
	 * 
	 * @param jobBean
	 */
	public SubmitJob(JobBean jobBean) {
		if (DEBUG)
			System.err.println("SubmitJob:constructor:  JobBean --> "
					+ jobBean.getName());
		job = jobBean;

	}

	public SubmitJob(List<JobBean> jobBeanList) {
		this.jobList = jobBeanList;
	}

	public SubmitJob() {
	}

	public void submit() {
		SUBMITCommand submitCommand = new SUBMITCommand(this);

		submitCommand.getArguments().put("job", job);
		submitCommand.getArguments().put("progressDialog", progressDialog);

		statusChanged(new StatusEvent(submitCommand, Status.START));
		//job.notify();

		System.out.println("**stage 7: Local Job ID SubmitJob.java: 156: "
				+ job.getLocalId());
	}

	public void submitAll() {

		SUBMITMANYCommand submitCommand = new SUBMITMANYCommand(this);

		submitCommand.getArguments().put("jobs", this.jobList);
		submitCommand.getArguments().put("progressDialog", progressDialog);

		statusChanged(new StatusEvent(submitCommand, Status.START));

		System.out.println("Multiple jobs submitted");

	}

	public void setSumitMultiple() {
		this.isSubmittingMultiple = true;
	}
	
	public void submitAll1() {
		SUBMITCommand submitCommand = new SUBMITCommand(this);

		submitCommand.getArguments().put("job", job);
		submitCommand.getArguments().put("progressDialog", progressDialog);

		statusChanged(new StatusEvent(submitCommand, Status.START));
		//job.notify();

		System.out.println("**stage 7: Local Job ID SubmitJob.java: 156: "
				+ job.getLocalId());
	}

	public void addProgressMonitor(ProgressDialog pm) {
		this.progressDialog = pm;
	}
	
	public void addProgressMonitors(List<ProgressDialog> pmList) {
		this.progressDialogList = pmList;
	}

	private void moveJobToDoneList(final JobBean job) {
		//SwingUtilities.invokeLater(new Runnable() {
			//public void run() {

				jobIndexInQueue = stuffInside.queueJobList.getJobIndex(job);

				System.out.println("\n\nMOVE JOB TO DONE LIST CALLED FOR JOB "
						+ job.getName() + "...");
				System.out.println("Selected index in queue is "
						+ jobIndexInQueue);
				System.out.println("The job in the qModel "
						+ stuffInside.queueModel.getElementAt(jobIndexInQueue));

				// remove the job from the static job queue
				SubmitJobsWindow.jobSubmitted.add(job);
				// unselect the job in the queue list
				stuffInside.queueList.removeSelectionInterval(jobIndexInQueue,
						jobIndexInQueue);
				// remove the job from the queued list and model.
				String jobLineItem = stuffInside.queueModel.remove(
						jobIndexInQueue).toString();
				// remove the job from the job list
				stuffInside.queueJobList.removeJob(jobIndexInQueue);

				// add to the static submitted job queue
				SubmitJobsWindow.jobQueue.remove(job);
				// create the entry for the done list by adding the local job id
				// GMS id is available localid need to be extracted!
				// String doneJobLineItem = job.getLocalId() + " " +
				// jobLineItem;
				String doneJobLineItem = "GridChem Job " + job.getId() + " "
						+ jobLineItem;
				// add to the done list and model.
				stuffInside.doneModel.addElement(doneJobLineItem);
				// add to the jobdto object list
				stuffInside.doneJobList.add(job);
				// select the newly submitted job and set it as selected
				stuffInside.doneList.setSelectedValue(doneJobLineItem, true);
				stuffInside.doneList.ensureIndexIsVisible(stuffInside.doneList
						.getSelectedIndex());
			//}
		//});
	}

	public void printMessages(String message, boolean dialog) {
		if (DEBUG)
			System.err.println(message);

		if (dialog == true) {
			JOptionPane.showMessageDialog(SubmitJobsWindow.frame, message,
					"Submit Job", JOptionPane.INFORMATION_MESSAGE);
		}

		GridChem.appendMessage(message);
	}

	// public static factory
	public static SubmitJob getInstance(JobBean job) {
		return new SubmitJob(job);
	}

	// utility method
	public boolean waiting() {
		return mustWaitForQueues;
	}

	public boolean isJobSubmitted() {
		return jobSubmitted;
	}

	// implementation of HardWorker interface
	public void startTask() {
	}

	public void stopTask() {
	}

	public void setDuration() {
	}

	public int mutableDuration() {
		return duration;
	}

	public int mutableProgress() {
		return percentProgress;
	}

	public boolean taskDone() {
		if (percentProgress >= duration)
			return true;
		else
			return false;
	}

	String ParseInp(String s) {
		StringTokenizer st = new StringTokenizer(s);
		String outp = "", app = "", jobid = "", queue = "", inp = "";
		String dat = "", sta = "";
		int i = 0;
		String wasteful;
		while (st.hasMoreTokens()) {
			wasteful = st.nextToken();
			if (i == 0) {
				app = "Application: " + wasteful;
			} else if (i == 3) {
				jobid = "JobID: " + wasteful;
			} else if (i == 9)
				queue = "Queue: " + wasteful;
			else if (i == 5) {
				inp = "Job Name: " + wasteful;
			} else if (i == 11)
				dat = "Date: " + wasteful;
			else if ((i == 13) || (i == 12))
				dat = dat + " " + wasteful;
			else if (i == 15)
				sta = "Status: " + wasteful;
			i++;
		}
		String spaces = "\n    ";
		outp = "Job Successfully submitted: " + spaces + inp + spaces + jobid
				+ spaces + app + spaces + queue + spaces + dat + spaces + sta
				+ "\n";
		return outp;
	}

	private String resolveException(String e) {
		if (e.getClass().getName().equals(SUBMISSION)) {
			return SUBMISSION;
		} else if (e.getClass().getName().equals(SCHEDULING)) {
			return SCHEDULING;
		} else if (e.getClass().getName().equals(PERMISSION)) {
			return PERMISSION;
		} else if (e.getClass().getName().equals(JOB_DESCRIPTION)) {
			return JOB_DESCRIPTION;
		} else if (e.getClass().getName().equals(GMSException.class.getName())) {
			return GMSException.class.getName();
		} else {
			return null;
		}
	}

	public void statusChanged(StatusEvent event) {
		Trace.entry();
		Status status = event.getStatus();

		System.out.println("Status changed to: " + status.name());
		System.out.println("StatusListener is: "
				+ event.getSource().getClass().getName());

		JobCommand command = (JobCommand) event.getSource();
		System.out.println("stats=" + status.name() + ", type="
				+ command.getClass());

		if (status.equals(Status.START)) {

			try {
				System.out
						.println("Stage 3: SubmitJob.java:909 - isEventDispatchThread: "
								+ SwingUtilities.isEventDispatchThread());

				command.execute();

			} catch (SessionException e) {

				if (GridChem.oc.monitorWindow != null) {
					GridChem.oc.monitorWindow.setUpdate(false);
				}

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						stuffInside.queueList.removeSelectionInterval(
								jobIndexInQueue, jobIndexInQueue);
					}
				});

				int viewLog = JOptionPane.showConfirmDialog(
						GridChem.oc.monitorWindow,
						"Your session has expired. Would\n"
								+ "you like to reauthenticate to the CCG?",
						"Session Timeout", JOptionPane.YES_NO_OPTION,
						JOptionPane.ERROR_MESSAGE);

				if (viewLog == 0) {
					GridChem.appendMessage("Resetting user authentication...");
					LoginDialog.clearLogin();
					GridChem.appendMessage("Complete\n");
					GridChem.oc.updateAuthenticatedStatus();
					GridChem.oc.doAuthentication();
				}

				throw e;

			} catch (GMSException e) {

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						stuffInside.queueList.removeSelectionInterval(
								jobIndexInQueue, jobIndexInQueue);
					}
				});

				updateWarning("Unknown Exception", e.getMessage());

				throw e;

			} catch (ConnectException e) {

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						stuffInside.queueList.removeSelectionInterval(
								jobIndexInQueue, jobIndexInQueue);
					}
				});

				updateWarning("Permission Exception", e.getMessage());

				throw new PermissionException(e);

			} catch (Exception e) {
				updateWarning("Unknown Exception", e.getMessage());

				throw new GMSException(e);

			}

			// File retrieval was broken into multiple parts to enable large
			// files
			// to be downloaded without having to hold the entire thing in
			// memory.
			// to do this, we download 56K blocks and append them into a single
			// file.
		} else if (status.equals(Status.UPLOADING)) {
			final JobCommand jcommand = command;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					int blocks = ((Long) jcommand.getArguments().get(
							"totalBlocks")).intValue();
					String file = (String) jcommand.getArguments().get(
							"fileName");
				}
			});
		} else if (status.equals(Status.READY)) {
			final JobCommand jcommand = command;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					int blocks = ((Long) jcommand.getArguments().get(
							"blocksReceived")).intValue();

				}
			});
		} else if (status.equals(Status.COMPLETED)) {
			System.out.println(command.getCommand() + " Command Completed");
			System.out.println("Stage 5: SubmitJob.java: 987");
			if (command.getCommand().equals(JobCommand.SUBMIT)) {

				System.out.println("\n\nSTATUS CHANGED TO Submitted FOR JOB "
						+ job.getName() + "\n\n");
				System.out.println(" Job Meta Data " + job.getMetaData());
				System.out.println("******** Job Last Updated at "
						+ job.getLastUpdated());

				System.out.println("******** GridChem jobID " + job.getId());
				System.out.println("********local Job ID: " + job.getLocalId());
				Trace.note("Finished job submission. Updating file list.");

				jobSubmitted = true;

				// SwingUtilities.invokeLater(new Runnable() {
				// public void run() {
				moveJobToDoneList((JobBean) command.getArguments().get("job"));
				System.out
						.println("stage 6: SubmitJob.java:1003(395) (independent runnable)");
				// }
				// });
				
				if (this.isSubmittingMultiple == true) {
					//System.out.println("I'm here !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + SubmitJobsWindow.jobQueue.size());
					//System.out.println("I'm here !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + stuffInside.queueJobList.size());
					if (SubmitJobsWindow.jobQueue.size() != 0) {
						this.job = SubmitJobsWindow.jobQueue.get(0);
						this.progressDialog = new ProgressDialog(
								SubmitJobsWindow.frame,
								"Job \"" + this.job.getName() + "\" Submission Progress");
						this.progressDialog.millisToPopup = 0;
						this.progressDialog.millisToDecideToPopup = 0;
						this.progressDialog.displayTimeLeft = false;
						submitAll1();
					} else {
						this.isSubmittingMultiple = false;
					}
				}

			} else if (command.getCommand().equals(JobCommand.SUBMIT_MANY)) {

				System.out.println("\n\nSTATUS CHANGED TO COMPLETED FOR JOB "
						+ job.getName() + "... \n\n");
				System.out.println("********local JOb ID: " + job.getLocalId());
				Trace.note("Finished job submission. Updating file list.");

				jobSubmitted = true;

				// SwingUtilities.invokeLater(new Runnable() {
				// public void run() {
				// moveJobToDoneList();
				System.out
						.println("stage 6: SubmitJob.java:1003(410) (independent runnable)");
				// }
				// });

			} else if (command.getCommand().equals(JobCommand.QSTAT)) {
				Trace.note("Finished UPDATE, refreshing user's VO");

				GridChem.jobs = ((QSTATCommand) command).getOutput();

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {

					}
				});
			}
			// What to do when commands fail.
		} else {
			System.out.println(command.getCommand() + " Command Failed!");

			if (command.getCommand().equals(JobCommand.SUBMIT)) {
				Throwable exception = (Throwable) command.getArguments().get(
						"exception");

				System.out
						.println("Submit Jobs received failed exception of type "
								+ exception.getClass().getName());

				if (exception != null) {

					updateWarning("Job Submission Exception",
							exception.getMessage());

					// } else {
					//
					// int viewLog = JOptionPane.showConfirmDialog(
					// SubmitJobsWindow.si,
					// "Job submission failed.\n" +
					// "View error log?",
					// "Submit Job Error",
					// JOptionPane.YES_NO_OPTION,
					// JOptionPane.ERROR_MESSAGE
					// );
					//
					// if (viewLog == 0) {
					// Trace.note( "Opening editor with" +
					// (String)command.getArguments().get("error.log.file"));
					// JobPanel.openEditor(
					// (String)command.getArguments().get("error.log.file"));
					// }
					//
					// }

				} else {
					updateWarning("Job Submission Exception",
							"Job submission failed for unknown reason.");

					// JOptionPane.showMessageDialog(this,
					// "Job submission failed for unknown reason.",
					// "Job Submission Exception", JOptionPane.ERROR_MESSAGE);
				}

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						stuffInside.queueList.removeSelectionInterval(
								jobIndexInQueue, jobIndexInQueue);
						SubmitJobsWindow.si.suballButton.setEnabled(true);
						SubmitJobsWindow.si.submButton.setEnabled(true);
					}
				});

			} else if (command.getCommand().equals(JobCommand.QSTAT)) {
				updateWarning("Job Submission Exception",
						"Failed to update job listing.");

				// JOptionPane.showMessageDialog(
				// SubmitJob.this,
				// "Failed to update job listing.",
				// "Submit Job Error", JOptionPane.ERROR_MESSAGE
				// );

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						stuffInside.queueList.removeSelectionInterval(
								jobIndexInQueue, jobIndexInQueue);
					}
				});
			}
		}

	}

	/**
	 * A null-safe update of the warning dialog's message. If none exists, one
	 * is created and displayed.
	 * 
	 * @param message
	 */
	public void updateWarning(String message) {
		JOptionPane.showMessageDialog(SubmitJobsWindow.frame, message, "",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * A null-safe update of the warning dialog's message and title. If none
	 * exists, one is created and displayed.
	 * 
	 * @param message
	 */
	public void updateWarning(String title, String message) {
		JOptionPane.showMessageDialog(SubmitJobsWindow.frame, message, title,
				JOptionPane.ERROR_MESSAGE);
	}
}