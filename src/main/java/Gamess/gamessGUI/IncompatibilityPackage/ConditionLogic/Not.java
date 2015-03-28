package Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic;

import org.w3c.dom.Node;

public class Not implements Condition {

	Condition condition = null;
	
	public Not()
	{
	}
	
	public Not(Condition cond)
	{
		add(cond);
	}
	
	public Not(Node condNode)
	{
		add(condNode);
	}
	
	public Not(String Data)
	{
		add(Data);
	}
	
	public void add(Condition cond) 
	{
		condition = cond;
	}

	public void add(Node condNode) 
	{
		//Check if the current node is Not. If it is then start from the first child
		if(condNode.getNodeName().equalsIgnoreCase("Not"))
		{
			condNode = condNode.getFirstChild();
			while(condNode != null && condNode.getNodeType() == Node.TEXT_NODE)
				condNode = condNode.getNextSibling();
		}
		
		if(condNode == null)
			return;
		
		//Check if the current node has any siblings.
		//if it is then add the nodes to And logic and add the And to this Not.
		Node nextSibNode = null;
		for(nextSibNode = condNode.getNextSibling(); nextSibNode != null && nextSibNode.getNodeType() == Node.TEXT_NODE; nextSibNode = nextSibNode.getNextSibling());
		if(nextSibNode != null)
		{
			condition = new And(condNode);
			return;
		}
		
		//Check if the current node is an And node
		if(condNode.getNodeName().equalsIgnoreCase("And"))
		{
			condition = new And(condNode);
		}
		//Check if the current node is an Or node
		else if(condNode.getNodeName().equalsIgnoreCase("Or"))
		{
			condition = new Or(condNode);
		}
		//Check if the current node is an Not node
		else if(condNode.getNodeName().equalsIgnoreCase("Not"))
		{
			condition = new Not(condNode);
		}
		//If the above three did not happen then the node is an Entity node
		else
		{
			condition = new Entity(condNode);
		}
	}

	public void add(String Data) 
	{
		condition = new Entity(Data);
	}

	public boolean test() {
		if(condition == null)
			return false;
		
		//Not always returns the opposite of the boolean value received
		return (!condition.test());
	}

	public void trim() {
		condition.trim();
	}

	@Override
	public String toString() {
		StringBuilder returnString = new StringBuilder();
		Condition currentCondition = condition;
		//if(!currentCondition.test())
			returnString.append(" not( " + currentCondition.toString() + " ) ");
		return returnString.toString();
	}
}
