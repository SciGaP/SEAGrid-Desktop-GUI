package Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic;

import java.util.Hashtable;

import org.w3c.dom.Node;

public class ConditionBuilder 
{
	private static Hashtable<Object, Condition> ConditionCache = new Hashtable<Object, Condition>();
	
	public static Condition buildCondition(String Data)
	{
		//if there is a hit then return the value
		if(hit(Data))
			return ConditionCache.get(Data);
		
		//Create a new And Condition and return it
		Condition cond = new And(Data);
		cond.trim();
		//Add the condition to the Cache 
		ConditionCache.put(Data, cond);
		return cond;
	}
	
	public static Condition buildCondition(Node condNode)
	{
		if(condNode == null)
			return null;
		//if there is a hit then return the value
		if(hit(condNode))
			return ConditionCache.get(condNode);
		
		Condition cond = null;
		
		//Check if the current node has any siblings.
		//if it has then create a new And logic and return it
		Node nextSibNode = null;
		for(nextSibNode = condNode.getNextSibling(); nextSibNode != null && nextSibNode.getNodeType() == Node.TEXT_NODE; nextSibNode = nextSibNode.getNextSibling());
		if(nextSibNode != null)
			cond = new And(condNode);
		
		//Else check if the current node belongs to And/Or/Not/Entity. 
		//Create a new logic based on that and return it.
		if(cond == null && condNode.getNodeName().equalsIgnoreCase("And"))
			cond = new And(condNode);
		else if(cond == null && condNode.getNodeName().equalsIgnoreCase("Or"))
			cond = new Or(condNode);
		else if(cond == null && condNode.getNodeName().equalsIgnoreCase("Not"))
			cond = new Not(condNode);
		else
			cond = new And(condNode);
		
		cond.trim();
		//Add the condition to the Cache 
		ConditionCache.put(condNode, cond);
		return cond;
	}
	
	private static boolean hit(Object obj)
	{
		return ConditionCache.containsKey(obj);
	}
}
