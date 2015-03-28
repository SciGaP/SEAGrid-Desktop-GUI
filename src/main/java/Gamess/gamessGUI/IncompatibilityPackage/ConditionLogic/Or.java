package Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic;

import java.util.ArrayList;

import org.w3c.dom.Node;

public class Or implements Condition {

	ArrayList<Condition> conditionList = new ArrayList<Condition>();
	
	public Or()
	{
	}
	
	public Or(Condition cond)
	{
		add(cond);
	}
	
	public Or(Node condNode)
	{
		add(condNode);
	}
	
	public Or(String Data)
	{
		add(Data);
	}
	
	public void add(Condition cond) 
	{
		if(!conditionList.contains(cond))
			conditionList.add(cond);

	}

	public void add(Node condNode) 
	{
		//if the current node is Or the start from the first child
		if(condNode.getNodeName().equalsIgnoreCase("Or"))
		{
			condNode = condNode.getFirstChild();
		}
		
		Node currentNode = condNode;
		while( currentNode != null)
		{
			if(currentNode.getNodeType() != Node.TEXT_NODE)
			{
				//Check if the current node is an And node
				if(currentNode.getNodeName().equalsIgnoreCase("And"))
				{
					conditionList.add(new And(currentNode));
				}
				//Check if the current node is an Or node
				else if(currentNode.getNodeName().equalsIgnoreCase("Or"))
				{
					conditionList.add(new Or(currentNode));
				}
				//Check if the current node is an Not node
				else if(currentNode.getNodeName().equalsIgnoreCase("Not"))
				{
					conditionList.add(new Not(currentNode));
				}
				//If the above three did not happen then the node is an Entity node
				else
				{
					conditionList.add(new Entity(currentNode));
				}
			}
			
			//Move to the next node
			currentNode = currentNode.getNextSibling();
		}
	}

	public void add(String Data) 
	{
		conditionList.add(new Entity(Data));
	}

	public boolean test() {
		//Evaluate the Or condition.
		//Or condition returns true if any one of the conditions return true
		for (int i = 0; i < conditionList.size(); i++) 
		{
			//a || b || C
			if(conditionList.get(i).test() == true)
				return true;
		}
		return false;
	}

	public void trim() {
		conditionList.trimToSize();
		for (int i = 0; i < conditionList.size(); i++) {
			conditionList.get(i).trim();
		}
	}

	@Override
	public String toString() {
		StringBuilder returnString = new StringBuilder();
		for (int i = 0; i < conditionList.size(); i++) {
			Condition currentCondition = conditionList.get(i);
			//if(currentCondition.test())
			{
				if(i != 0)
					returnString.append(" or ");
				if(!(currentCondition instanceof Entity))
					returnString.append(" ( " + currentCondition.toString() + " ) ");
				else
					returnString.append(currentCondition.toString());
			}
		} 
		return returnString.toString();
	}
}
