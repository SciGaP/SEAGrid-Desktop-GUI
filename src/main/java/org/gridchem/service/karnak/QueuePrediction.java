package org.gridchem.service.karnak;

import java.text.*;
import java.util.*;
import org.w3c.dom.*;

public class QueuePrediction {
    public String clusterName = null;
    public String queueName = null;
    public int processors = -1;
    public int requestedRunTimeSecs = -1;
    public Date submitTime = null;
    public Date predictedStartTime = null;
    public int predictedWaitTimeSecs = -1;
    public int confidenceIntervalSecs = -1;
    public int levelOfConfidence = -1;
    public Date generatedAt = null;

    public static QueuePrediction fromDom(Document doc) throws KarnakException {
	QueuePrediction pred = new QueuePrediction();

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	df.setTimeZone(TimeZone.getTimeZone("GMT"));

	try {
	    pred.generatedAt = df.parse(doc.getDocumentElement().getAttribute("time"));
	} catch (ParseException e) {}

	try {
	    pred.submitTime = df.parse(doc.getElementsByTagName("SubmitTime").item(0).getTextContent());
	} catch (ParseException e) {}

	pred.processors = Integer.parseInt(doc.getElementsByTagName("Processors").item(0).getTextContent());

	Element reqRunTime = (Element)doc.getElementsByTagName("RequestedWallTime").item(0);
	// assume units is minutes
	pred.requestedRunTimeSecs = Math.round(Float.parseFloat(reqRunTime.getTextContent()) * 60);

	pred.levelOfConfidence = Integer.parseInt(doc.getElementsByTagName("Confidence").item(0).getTextContent());

	NodeList locationNodes = doc.getElementsByTagName("Location");
	if (locationNodes.getLength() > 1) {
	    throw new KarnakException("can only handle 1 Location per document");
	}

	Element location = (Element)locationNodes.item(0);
	pred.clusterName = location.getAttribute("system");
	pred.queueName = location.getAttribute("queue");
	try {
	    pred.predictedStartTime = df.parse(location.getElementsByTagName("StartTime").item(0).getTextContent());
	    pred.predictedWaitTimeSecs = (int)(pred.predictedStartTime.getTime() - pred.generatedAt.getTime()) / 1000;
	} catch (NullPointerException e) {
	    // no StartTime element, but that's ok - there will be a WaitTime one
	} catch (ParseException e) {}

	Element waitTime = (Element)location.getElementsByTagName("WaitTime").item(0);
	try {
	    // assume units is minutes
	    pred.predictedWaitTimeSecs = Math.round(Float.parseFloat(waitTime.getTextContent()) * 60);
	    pred.predictedStartTime = new Date(pred.generatedAt.getTime() + pred.predictedWaitTimeSecs * 1000);
	} catch (NullPointerException e) {
	    // no WaitTime element, but that's ok - there should have been a StartTime one
	}

	Element conf = (Element)location.getElementsByTagName("Confidence").item(0);
	if (conf.getAttribute("units").equals("minutes")) {
	    pred.confidenceIntervalSecs = Math.round(Float.parseFloat(conf.getTextContent()) * 60);
	}

	return pred;
    }

    public QueuePrediction() {
    }

    public String toString() {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	df.setTimeZone(TimeZone.getTimeZone("GMT"));

	String str = String.format("prediction for job on cluster %s generated at %s%n",
				   clusterName,df.format(generatedAt));
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
	if (args.length != 4) {
	    System.err.println("usage: QueuePrediction <cluster> <queue> <processors> <run time (hh:mm:ss)>");
	    System.exit(1);
	}

	Karnak k = new Karnak();
	try {
	    String hms[] = args[3].split(":");
	    int runTimeSecs = Integer.parseInt(hms[2])+60*(Integer.parseInt(hms[1])+60*Integer.parseInt(hms[0]));
	    System.out.print(k.queuePrediction(Integer.parseInt(args[2]),runTimeSecs,args[0],args[1]));
	} catch (KarnakException e) {
	    e.printStackTrace();
	}
    }

}