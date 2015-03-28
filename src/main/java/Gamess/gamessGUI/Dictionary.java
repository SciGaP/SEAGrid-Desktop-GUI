package Gamess.gamessGUI;

import java.util.Hashtable;

import javax.swing.JDialog;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Dictionary 
{
	public static Document organizedDoc = null;
	public static final String GRID_DIALOG = "GRID_DIALOG";
	public static final String TEXT_DIALOG = "TEXT_DIALOG";
	public static final String CUSTOM_DIALOG = "CUSTOM_DIALOG";
	public static final String TEXTBOX_VALUE = "TEXTBOX_VALUE";
	
	public static final Hashtable<String, JDialog> dialogs = new Hashtable<String, JDialog>();
	public static void registerDialog(String key, JDialog dialog)
	{
		
		//if not found create it and add it to the table
		//TODO change this to proper way
		key = getFormattedKeyword(key);

		dialogs.put(key, dialog);
	}
		
	private static Element root = null;
	public static Hashtable<String, Element> groupsStartingNodes = new Hashtable<String, Element>();
	private static Hashtable<String, Element> groupKeywordNodes = new Hashtable<String, Element>();
	private static Hashtable<String, Element> keywordValueNodes = new Hashtable<String, Element>();
	static
	{
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			DOMImplementation di = db.getDOMImplementation();
			
			organizedDoc = di.createDocument(null, "root", null);
			root = organizedDoc.getDocumentElement();
		}
		catch(ParserConfigurationException e){}
	}
	
	
	public static Element Register(String group) {
		group = group.toUpperCase();
		
		//Try to find the element of group in the groupStartingNode table.
		if(groupsStartingNodes.containsKey(group))
		{
			return groupsStartingNodes.get(group);
		}
		//if not found create it and add it to the table
		Element groupElement = organizedDoc.createElement(group);
		root.appendChild(groupElement);
		groupsStartingNodes.put(group, groupElement);
		return groupElement;
	}
	
	public static Element Register(String group, String keyword) {
		group = group.toUpperCase();
		keyword = keyword.toUpperCase();

		//if not found create it and add it to the table
		keyword = getFormattedKeyword(keyword);
		
		//Try to find the element of keyword in the groupKeywordNodes table.
		if(groupKeywordNodes.containsKey(group + " " + keyword))
		{
			return groupKeywordNodes.get(group + " " + keyword);
		}

		Element keywordElement = organizedDoc.createElement(keyword);
		Register(group).appendChild(keywordElement);
		groupKeywordNodes.put(group + " " + keyword, keywordElement);
		return keywordElement;
	}
	
	public static Element Register(String group, String keyword, String value)	{
		group = group.toUpperCase();
		keyword = keyword.toUpperCase();
		value = value.toUpperCase();

		keyword = getFormattedKeyword(keyword);
		
		//Try to find the element of keyword in the groupKeywordNodes table.
		if(keywordValueNodes.containsKey(group + " " + keyword + " " + value))
		{
			return keywordValueNodes.get(group + " " + keyword + " " + value);
		}
		//if not found create it and add it to the table
		Element valueElement = organizedDoc.createElement("Value");
		//The reason for setting the value as attribute is because the value can have 
		//some special characters like "(" and numbers which are not valid names
		valueElement.setAttribute("value", value);
		Register(group, keyword).appendChild(valueElement);
		keywordValueNodes.put(group + " " + keyword + " " + value, valueElement);
		return valueElement;
	}
	
	public static Element get()
	{
		return root;
	}
	
	public static Element get(String group)
	{
		group = group.toUpperCase();
		if(groupsStartingNodes.containsKey(group))
		{
			return groupsStartingNodes.get(group);
		}
		return null;
	}
	
	public static Element get(String group, String keyword)
	{
		group = group.toUpperCase();
		keyword = keyword.toUpperCase();
		
		keyword = getFormattedKeyword(keyword);
		
		if(groupKeywordNodes.containsKey(group + " " + keyword))
		{
			return groupKeywordNodes.get(group + " " + keyword);
		}
		return null;
	}
	
	public static Element get(String group, String keyword , String value)
	{
		group = group.toUpperCase();
		keyword = keyword.toUpperCase();
		value = value.toUpperCase();
		
		keyword = getFormattedKeyword(keyword);
		
		if(keywordValueNodes.containsKey(group + " " + keyword + " " + value))
		{
			return keywordValueNodes.get(group + " " + keyword + " " + value);
		}
		return null;
	}
	
	public static String getFormattedKeyword(String keyword)
	{
		int arrayIndexStarting = -1;
		
		//Check if the keyword contains an array index that starts with '(' and ends with ')'
		if( (arrayIndexStarting = keyword.indexOf("(")) != -1 && keyword.endsWith(")"))
		{
			try
			{
				String IndexValue = keyword.substring(arrayIndexStarting + 1, keyword.length() - 1);
				Integer.parseInt(IndexValue);
			}
			catch (NumberFormatException e) {
				//if the array index is not an integer then return keyword as it is 
				return keyword;
			}
			//replace ( and ) with _
			//keyword = keyword.replace("(", "__").replace(")", "_");
			keyword = keyword.substring(0, arrayIndexStarting);
		}
		
		return keyword;
	}
}
