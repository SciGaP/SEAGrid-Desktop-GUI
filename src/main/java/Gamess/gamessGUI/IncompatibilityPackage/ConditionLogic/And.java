package Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic;

import java.util.ArrayList;

import org.w3c.dom.Node;

public class And implements Condition {
	
	ArrayList<Condition> conditionList = new ArrayList<Condition>();

	public And()
	{
	}
	
	public And(Condition cond)
	{
		add(cond);
	}
	
	public And(Node condNode)
	{
		add(condNode);
	}
	
	public And(String Data)
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
		//if the current node is And the start from the first child
		if(condNode.getNodeName().equalsIgnoreCase("And"))
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
			//move to the next node
			currentNode = currentNode.getNextSibling();
		}
	}

	public void add(String Data) 
	{
		//Add a new entity to the list of conditions
		conditionList.add(new Entity(Data)); 
	}

	public boolean test() {
		if(conditionList.size() == 0)
			return false;
		//Evaluate the And logic
		//This returns true only if all the values returns true
		for (int i = 0; i < conditionList.size(); i++) 
		{
			if(conditionList.get(i).test() == false)
			{
				//one of the condition evaluated to false so return false
				//a && B && c
				return false;
			}
		}
		return true;
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
					returnString.append(" and ");
				if(!(currentCondition instanceof Entity))
					returnString.append(" ( " + currentCondition.toString() + " ) ");
				else
					returnString.append(currentCondition.toString());
			}
		} 
		return returnString.toString();
	}
}
