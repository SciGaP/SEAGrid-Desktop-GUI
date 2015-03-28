package Gamess.gamessGUI.IncompatibilityPackage.Restriction;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Gamess.gamessGUI.GlobalParameters;
import Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic.Condition;
import Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic.ConditionBuilder;

public class ExcludeRestriction implements IbuildList, IIncompatibilityList 
{
	private ArrayList<RestrictionType> excludeList = new ArrayList<RestrictionType>();
	
	String xpathPreExpr = "/root/input";
	String xpathCondExpr = "";
	String xpathPostExpr = "/exclude/Entity";
	private static XPath xpath = XPathFactory.newInstance().newXPath();
	
	public void buildList(String Data) 
	{
		try 
		{
	        //InputSource inputSource = new InputSource(new FileInputStream("Incompatibility.xml"));;
			
	        //build the condition matching given input
	        String Group,Keyword,Value;
	        String[] splitData = Data.split(" ");
	        
	        Group = (splitData.length > 0) ? "@Group='" + splitData[0] + "' and " : "not(@Group) and " ;
	        Keyword = (splitData.length > 1) ? "@Keyword='" + splitData[1] + "' and " : "not(@Keyword) and ";
	        Value = (splitData.length > 2) ? "@Value='" + splitData[2] + "'" : "not(@Value)";
			
	        xpathCondExpr = "[" + Group + Keyword + Value  + "]";
	        
	        //get the xml node list matching the given condition
			NodeList excludeNodes = (NodeList)xpath.evaluate(xpathPreExpr + xpathCondExpr + xpathPostExpr , GlobalParameters.doc , XPathConstants.NODESET);
			
			//The condition which excludes the nodes is the condition which is searched for i.e. Data
			//XYZ is excluded because of ABC
			Condition excludeCondition = ConditionBuilder.buildCondition(Data.trim());
			//Get the list of exclude nodes
			for (int i = 0; i < excludeNodes.getLength(); i++) 
			{
				//For each exclude entity get the condition and add it to the restriction list
				Node currentExcludeEntity = excludeNodes.item(i);
					 
				RestrictionType restriction = new RestrictionType();
				excludeList.add(restriction);
					
				restriction.addCondition(excludeCondition);
					
				NamedNodeMap inputAttributes = currentExcludeEntity.getAttributes();
				
				Node AttributeNode = inputAttributes.getNamedItem("Group");
				String Input = (AttributeNode != null) ? AttributeNode.getNodeValue() + " " : ""; 
				
				AttributeNode = inputAttributes.getNamedItem("Keyword");
				Input += (AttributeNode != null) ? AttributeNode.getNodeValue() + " " : "";
				
				AttributeNode = inputAttributes.getNamedItem("Value");
				Input += (AttributeNode != null) ? AttributeNode.getNodeValue() + " " : "";
				
				restriction.setInput(Input.trim());
			}
			excludeList.trimToSize();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<RestrictionType> getIncompatibilityList() {
		return excludeList;
	}


}
