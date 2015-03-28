package Gamess.gamessGUI.IncompatibilityPackage.Restriction;

import Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic.Condition;

public class RestrictionType 
{
	protected String Input = "";
	protected Condition condition = null;
	
	public String getInput()
	{
		return Input;
	}
	
	public void setInput(String _Input)
	{
		Input = _Input;
	}
	
	
	public void addCondition(Condition cond)
	{
		condition = cond;
	}
	
	public Condition getCondition()
	{
		return condition;
	}
	
	public boolean testCondition()
	{
		return condition.test();
	}
}
