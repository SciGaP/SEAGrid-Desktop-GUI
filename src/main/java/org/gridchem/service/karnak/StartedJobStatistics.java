package org.gridchem.service.karnak;

import java.text.*;
import java.util.*;
import org.w3c.dom.*;

public class StartedJobStatistics {
    public int numJobs = -1;
    public int meanProcessors = -1;
    public int meanRequestedRunTimeSecs = -1;
    public int meanWaitTimeSecs = -1;

    public static StartedJobStatistics fromDom(Element element) {
	if (element == null) {
	    return null;
	}

	StartedJobStatistics stats = new StartedJobStatistics();
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
	    stats.meanWaitTimeSecs = Util.getChildContentHms(element,"MeanWaitTime");
	} catch (NullPointerException e) {}
	return stats;
    }

    public StartedJobStatistics() {
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
	if (meanWaitTimeSecs != -1) {
	    str += String.format("mean wait time: %s%n",Util.hms(meanWaitTimeSecs));
	}
	return str;
    }
}
