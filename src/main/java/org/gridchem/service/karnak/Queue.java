package org.gridchem.service.karnak;

import java.util.*;

import org.w3c.dom.*;

public class Queue {
    public String name = null;
    public String clusterName = null;
    public JobSummary summary = null;
    public List<JobStatistics> stats = new Vector<JobStatistics>();

    public static Queue fromDom(Element element) {
	Queue queue = new Queue(element.getElementsByTagName("Name").item(0).getTextContent());
	queue.summary = JobSummary.fromDom(element);
	NodeList nodes = element.getElementsByTagName("JobStatistics");
	for(int i=0;i<nodes.getLength();i++) {
	    JobStatistics s = JobStatistics.fromDom((Element)nodes.item(i));
	    queue.stats.add(s);
	}
	return queue;
    }

    public Queue(String name) {
	this.name = name;
    }

    public String toString() {
	String endl = System.getProperty("line.separator");
	String str = String.format("Queue %s on cluster %s%n",name,clusterName);
	if (summary != null) {
	    str += summary.toString().replaceAll("(?m)^", "  ");
	}
	for(JobStatistics s: stats) {
	    str += s.toString().replaceAll("(?m)^", "  ");
	}
	return str;
    }

    public static void main(String[] args) {
	if (args.length > 2) {
	    System.err.println("usage: Queue <cluster> [queue]");
	    System.exit(1);
	}

	String clusterName = args[0];
	String queueName = "all_jobs";
	if (args.length == 2) {
	    queueName = args[1];
	}

	Karnak k = new Karnak();
	try {
	    System.out.print(k.queue(clusterName,queueName));
	} catch (KarnakException e) {
	    e.printStackTrace();
	}
    }
}
