package org.gridchem.service.karnak;

import java.text.*;
import java.util.*;
import org.w3c.dom.*;

public class QueuedPrediction {
    public String clusterName = null;
    public String queueName = null;
    public String jobIdentifier = null;
    public int processors = -1;
    public int requestedRunTimeSecs = -1;
    public Date submitTime = null;
    public Date predictedStartTime = null;
    public int predictedWaitTimeSecs = -1;
    public int confidenceIntervalSecs = -1;
    public int levelOfConfidence = -1;
    public Date generatedAt = null;


    public static QueuedPrediction fromDom(Document doc) {
	QueuedPrediction pred = new QueuedPrediction();

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	df.setTimeZone(TimeZone.getTimeZone("GMT"));

	try {
	    pred.generatedAt = df.parse(doc.getDocumentElement().getAttribute("time"));
	} catch (ParseException e) {}
	pred.clusterName = doc.getElementsByTagName("System").item(0).getTextContent();
	pred.queueName = doc.getElementsByTagName("Queue").item(0).getTextContent();
	pred.jobIdentifier = doc.getElementsByTagName("Identifier").item(0).getTextContent();
	try {
	    pred.submitTime = df.parse(doc.getElementsByTagName("SubmitTime").item(0).getTextContent());
	} catch (ParseException e) {}
	pred.processors = Integer.parseInt(doc.getElementsByTagName("Processors").item(0).getTextContent());
	pred.clusterName = doc.getElementsByTagName("System").item(0).getTextContent();

	Element reqRunTime = (Element)doc.getElementsByTagName("RequestedWallTime").item(0);
	// assume units is minutes
	pred.requestedRunTimeSecs = Math.round(Float.parseFloat(reqRunTime.getTextContent()) * 60);

	try {
	    pred.predictedStartTime = df.parse(doc.getElementsByTagName("StartTime").item(0).getTextContent());
	    pred.predictedWaitTimeSecs = (int)(pred.predictedStartTime.getTime() - pred.generatedAt.getTime()) / 1000;
	} catch (NullPointerException e) {
	    // no StartTime element, but that's ok - there will be a WaitTime one
	} catch (ParseException e) {}

	Element waitTime = (Element)doc.getElementsByTagName("WaitTime").item(0);
	try {
	    // assume units is minutes
	    pred.predictedWaitTimeSecs = Math.round(Float.parseFloat(waitTime.getTextContent()) * 60);
	    pred.predictedStartTime = new Date(pred.generatedAt.getTime() + pred.predictedWaitTimeSecs * 1000);
	} catch (NullPointerException e) {
	    // no WaitTime element, but that's ok - there should have been a StartTime one
	}

	Element conf = (Element)doc.getElementsByTagName("ConfidenceInterval").item(0);
	try {
	    if (conf.getAttribute("units").equals("minutes")) {
		pred.confidenceIntervalSecs = Math.round(Float.parseFloat(conf.getTextContent()) * 60);
	    }
	    pred.levelOfConfidence = Integer.parseInt(conf.getAttribute("confidence"));
	} catch (NullPointerException e) {
	    // no ConfidenceInterval element, but that's ok - the job has already started and the exact time is known
	    pred.confidenceIntervalSecs = 0;
	    pred.levelOfConfidence = 100;
	}

	return pred;
    }

    public QueuedPrediction() {
    }

    public String toString() {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	df.setTimeZone(TimeZone.getTimeZone("GMT"));

	String str = String.format("prediction for job %s on cluster %s generated at %s%n",
				   jobIdentifier,clusterName,df.format(generatedAt));
	str += String.format("  queue: %s%n",queueName);
	str += String.format("  processors: %d%n",processors);
	str += String.format("  requested run time: %s%n",Util.hms(requestedRunTimeSecs));
	str += String.format("  submit time: %s%n",df.format(submitTime));
	str += String.format("  predicted start time: %s +- %s%n",
			     df.format(predictedStartTime),Util.hms(confidenceIntervalSecs));
	str += String.format("            (wait time: %20s +- %s)%n",
			     Util.hms(predictedWaitTimeSecs),Util.hms(confidenceIntervalSecs));

	return str;
    }

    public static void main(String[] args) {
	if (args.length != 2) {
	    System.err.println("usage: QueuedPrediction <cluster> <job id>");
	    System.exit(1);
	}

	Karnak k = new Karnak();
	try {
	    System.out.print(k.queuedPrediction(args[0],args[1]));
	} catch (KarnakException e) {
	    e.printStackTrace();
	}
    }

}