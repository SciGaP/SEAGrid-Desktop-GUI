package org.gridchem.service.karnak;

import java.io.*;

// for printDocument
import javax.xml.transform.*; 
import javax.xml.transform.dom.*; 
import javax.xml.transform.stream.*; 

import org.w3c.dom.*;

public class Util {

    public static int getChildContentInt(Element element, String tagName) {
	try {
	    return Integer.parseInt(element.getElementsByTagName(tagName).item(0).getTextContent());
	} catch (NullPointerException e) {
	    return -1;
	}
    }

    public static int getChildContentHms(Element element, String tagName) {
	try {
	    String[] hms = element.getElementsByTagName(tagName).item(0).getTextContent().split(":");
	    return Integer.parseInt(hms[2]) + 60 * (Integer.parseInt(hms[1]) + 60 * Integer.parseInt(hms[0]));
	} catch (NullPointerException e) {
	    return -1;
	}
    }

    public static String hms(int seconds) {
	int hours = (int)Math.floor(seconds / (60*60));
	seconds = seconds - hours * (60*60);
	int minutes = (int)Math.floor(seconds / 60);
	seconds = seconds - minutes * 60;
	return String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }

    public static void printDocument(Document doc) {
	try {
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	    transformer.transform(new DOMSource(doc), 
				  new StreamResult(new OutputStreamWriter(System.out, "UTF-8")));
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
