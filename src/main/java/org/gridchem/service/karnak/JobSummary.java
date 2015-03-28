package org.gridchem.service.karnak;

import org.w3c.dom.*;

public class JobSummary {
    public int numRunningJobs = -1;
    public int numWaitingJobs = -1;
    public int usedProcessors = -1;

    public static JobSummary fromDom(Element element) {
	JobSummary summary = new JobSummary();
	try {
	    summary.numRunningJobs = Util.getChildContentInt(element,"NumRunningJobs");
	} catch (NullPointerException e) {}
	try {
	    summary.numWaitingJobs = Util.getChildContentInt(element,"NumWaitingJobs");
	} catch (NullPointerException e) {}
	try {
	    summary.usedProcessors = Util.getChildContentInt(element,"UsedProcessors");
	} catch (NullPointerException e) {}

	if ((summary.numRunningJobs == -1) && (summary.numWaitingJobs == -1) && (summary.usedProcessors == -1)) {
	    return null;
	} else {
	    return summary;
	}
    }

    public JobSummary() {
    }

    public String toString() {
	String endl = System.getProperty("line.separator");
	String str = "";
	if (numRunningJobs != -1) {
	    str += "running jobs: "+numRunningJobs+endl;
	}
	if (numWaitingJobs != -1) {
	    str += "waiting jobs: "+numWaitingJobs+endl;
	}
	if (usedProcessors != -1) {
	    str += "used processors: "+usedProcessors+endl;
	}
	return str;
    }
}