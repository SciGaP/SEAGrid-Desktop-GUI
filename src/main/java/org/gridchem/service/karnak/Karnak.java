package org.gridchem.service.karnak;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*; 

import org.w3c.dom.*;
 
// need Apache http client from http://hc.apache.org/downloads.cgi
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class Karnak {

    String host = "karnak.xsede.org";

    public Karnak() {
    }

    public Karnak(String host) {
	this.host = host;
    }

    public List<Cluster> clusters() throws KarnakException {
	Document doc = get("http://"+host+"/karnak/system/status.xml");
	//printDocument(doc);

	Vector<Cluster> clusters = new Vector<Cluster>();

	NodeList clusterNodes = doc.getElementsByTagName("System");
	for(int i=0;i<clusterNodes.getLength();i++) {
	    clusters.add(Cluster.fromDom((Element)clusterNodes.item(i)));
	}
	
	return clusters;
    }

    public Cluster cluster(String name) throws KarnakException {
	Document doc = get("http://"+host+"/karnak/system/"+name+"/status.xml");
	//printDocument(doc);

	try {
	    throw new KarnakException(doc.getElementsByTagName("Error").item(0).getTextContent());
	} catch (NullPointerException e) {}

	Cluster cluster = Cluster.fromDom(doc.getDocumentElement());

	NodeList queueNodes = doc.getElementsByTagName("Queue");
	for(int i=0;i<queueNodes.getLength();i++) {
	    Queue queue = Queue.fromDom((Element)queueNodes.item(i));
	    if (queue.name.equals("all_jobs")) {
		cluster.summary = queue.summary;
	    } else {
		cluster.queues.add(queue);
	    }
	}

	return cluster;
    }

    public Queue queue(String clusterName, String queueName) throws KarnakException {
	Document doc = get("http://"+host+"/karnak/system/"+clusterName+"/queue/"+queueName+"/summary.xml");
	//printDocument(doc);

	try {
	    throw new KarnakException(doc.getElementsByTagName("Error").item(0).getTextContent());
	} catch (NullPointerException e) {}

	Cluster cluster = Cluster.fromDom(doc.getDocumentElement());
	Queue queue = Queue.fromDom((Element)doc.getElementsByTagName("Queue").item(0));
	queue.clusterName = cluster.name;

	return queue;
    }

    public List<Job> waitingJobs(String clusterName) throws KarnakException {
	Document doc = get("http://"+host+"/karnak/system/"+clusterName+"/job/waiting.xml");
	//printDocument(doc);

	Vector<Job> jobs = new Vector<Job>();

	NodeList nodes = doc.getElementsByTagName("Job");
	for(int i=0;i<nodes.getLength();i++) {
	    Job job = Job.fromDom((Element)nodes.item(i));
	    job.clusterName = clusterName;
	    jobs.add(job);
	}

	return jobs;
    }

    public QueuedPrediction queuedPrediction(String clusterName, String jobIdentifier) throws KarnakException {
	// this could also be to waittime.xml - it doesn't matter
	Document doc = get("http://"+host+"/karnak/system/"+clusterName+"/job/"+jobIdentifier+
			   "/prediction/starttime.xml");
	//printDocument(doc);

	try {
	    throw new KarnakException(doc.getElementsByTagName("Error").item(0).getTextContent());
	} catch (NullPointerException e) {}

	return QueuedPrediction.fromDom(doc);
    }

    public QueuePrediction queuePrediction(int processors, int requestedRunTimeSecs,
					   String clusterName, String queueName) throws KarnakException {

	String body = String.format("<Predictions xmlns='http://tacc.utexas.edu/karnak/protocol/1.0'>%n");
	body += String.format("  <Processors>%d</Processors>%n",processors);
	body += String.format("  <RequestedWallTime units='minutes'>%d</RequestedWallTime>%n",
			      requestedRunTimeSecs/60);
	body += String.format("  <Confidence>%d</Confidence>%n",90);
	body += String.format("  <Location system='%s' queue='%s'/>%n",clusterName,queueName);
	body += String.format("</Predictions>%n");

	DefaultHttpClient httpClient = new DefaultHttpClient();
	// /waittime/prediction/ would also work
	HttpPost hpost = new HttpPost("http://"+host+"/karnak/starttime/prediction/");
	hpost.addHeader("Content-type", "text/xml");
	hpost.addHeader("Accept", "text/xml");
	try {
	    hpost.setEntity(new StringEntity(body));
	} catch (UnsupportedEncodingException e) {
	    throw new KarnakException(e);
	}

	HttpResponse response = null;
	try {
	    response = httpClient.execute(hpost);
	} catch (IOException e) {
	    throw new KarnakException(e);
	}
	if (response.getStatusLine().getStatusCode() != 200) {
	    throw new KarnakException("HTTP POST failed: " + response.getStatusLine().getStatusCode());
	}

	String path = null;
	try {
	    Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
	    path = s.hasNext() ? s.next() : "";
	} catch (IOException e) {}

	httpClient.getConnectionManager().shutdown();

	Document doc = get("http://"+host+path);
	//printDocument(doc);

	try {
	    throw new KarnakException(doc.getElementsByTagName("Error").item(0).getTextContent());
	} catch (NullPointerException e) {}

	return QueuePrediction.fromDom(doc);
    }

    protected Document get(String url) throws KarnakException {
	DefaultHttpClient httpClient = new DefaultHttpClient();
	//System.out.println("getting "+url);
	HttpGet hget = new HttpGet(url);
	//hget.addHeader("accept", "application/xml");
 
	HttpResponse response = null;
	try {
	    response = httpClient.execute(hget);
	} catch (IOException e) {
	    throw new KarnakException(e);
	}
	if (response.getStatusLine().getStatusCode() != 200) {
	    throw new KarnakException("HTTP request failed: " + response.getStatusLine().getStatusCode());
	}

	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = null;
	try {
	    db = dbf.newDocumentBuilder(); 
	} catch (ParserConfigurationException e) {
	    throw new KarnakException(e);
	}
	Document doc;
	try {
	    doc = db.parse(response.getEntity().getContent());
	} catch (Exception e) {
	    throw new KarnakException(e);
	}

	httpClient.getConnectionManager().shutdown();

	if (doc.getDocumentElement().getTagName().equals("Error")) {
	    throw new KarnakException(doc.getDocumentElement().getTextContent());
	}

	return doc;
    }

}