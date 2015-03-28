package Gamess.gamessGUI.IncompatibilityPackage.Restriction;

import Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic.Condition;
import Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic.Not;

public class AvailableOnlyIfRestriction extends ExcludedIfRestriction
{
	public AvailableOnlyIfRestriction()
	{
		xpathPreExpr = "/root/input/availableonlyif[descendant::Entity";
	}
	
	@Override
	protected void addRestrictionCondition(RestrictionType restriction, Condition condition) {
		restriction.addCondition(new Not(condition));
	}
}
