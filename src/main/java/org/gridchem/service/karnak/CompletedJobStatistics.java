package org.gridchem.service.karnak;

import org.w3c.dom.Element;

public class CompletedJobStatistics {
    public int numJobs = -1;
    public int meanProcessors = -1;
    public int meanRequestedRunTimeSecs = -1;
    public int meanRunTimeSecs = -1;

    public static CompletedJobStatistics fromDom(Element element) {
	if (element == null) {
	    return null;
	}

	CompletedJobStatistics stats = new CompletedJobStatistics();
	try {
	    stats.numJobs = Util.getChildContentInt(element,"NumJobs");
	} catch (NullPointerException e) {}
	try {
	    stats.meanProcessors = Util.getChildContentInt(element,"MeanProcessors");
	} catch (NullPointerException e) {}
	try {
	    stats.meanRequestedRunTimeSecs = Util.getChildContentHms(element,"MeanRequestedRunTime");
	} catch (NullPointerException e) {}
	try {
	    stats.meanRunTimeSecs = Util.getChildContentHms(element,"MeanRunTime");
	} catch (NullPointerException e) {}
	return stats;
    }

    public CompletedJobStatistics() {
    }

    public String toString() {
	String str = "";
	if (numJobs != -1) {
	    str += String.format("jobs: %d%n",numJobs);
	}
	if (meanProcessors != -1) {
	    str += String.format("mean processors: %d%n",meanProcessors);
	}
	if (meanRequestedRunTimeSecs != -1) {
	    str += String.format("mean requested run time: %s%n",Util.hms(meanRequestedRunTimeSecs));
	}
	if (meanRunTimeSecs != -1) {
	    str += String.format("mean run time: %s%n",Util.hms(meanRunTimeSecs));
	}
	return str;
    }
}
