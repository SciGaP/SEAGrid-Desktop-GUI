package Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import Gamess.gamessGUI.Storage.Repository;

public class Entity implements Condition {

	Repository  database = null;
	String Group = "";
	String Keyword = "";
	String Value = "";
	String Seperator = "";
	
	public Entity()
	{
	}
	
	public Entity(Condition cond)
	{
		add(cond);
	}
	
	public Entity(Node condNode)
	{
		add(condNode);
	}
	
	public Entity(String Data)
	{
		add(Data);
	}
	
	public void add(Condition cond) 
	{
		return;
	}

	public void add(Node condNode) 
	{
		NamedNodeMap nodeMap = condNode.getAttributes();
		Node entityNode = nodeMap.getNamedItem("Group");
		if(entityNode != null)
			Group = entityNode.getNodeValue().trim();
		
		entityNode = nodeMap.getNamedItem("Keyword");
		if(entityNode != null)
			Keyword = entityNode.getNodeValue().trim();
		
		entityNode = nodeMap.getNamedItem("Value");
		if(entityNode != null)
			Value = entityNode.getNodeValue().trim();
		
		Seperator = Repository.getInstance().getKeywordValueSeperator(Group);
	}

	public void add(String Data) 
	{
		String[] splitData = Data.split(" ");
		Group = splitData[0].trim();
		if(Group.length() == 0)
			return;
		if(splitData.length > 1)
			Keyword = splitData[1].trim();
		if(splitData.length > 2)
			Value = splitData[2].trim();
		
		Seperator = Repository.getInstance().getKeywordValueSeperator(Group);
	}

	public boolean test() {
		database = Repository.getInstance();
		//if group is not available return false
		if(!database.isAvailable(Group))
			return false;
		
		//if only group is specified return true;
		if(Keyword.length() == 0 && Value.length() == 0)
			return true;
		
		//if Group and keyword are only specified return true if keyword is available
		if(Value.length() == 0)
			return database.isAvailable(Group, Keyword);
		
		//if control comes here then it means that all the three are specified
		return database.equal(Group, Keyword + Seperator + Value);
	}

	public void trim() 
	{
		//No implementation needed
	}

	@Override
	public String toString() {
		if(Value.length() != 0)
			return "$" + Group + " " + Keyword + Seperator + Value;
		return "$" + Group + " " + Keyword;
	}
}
