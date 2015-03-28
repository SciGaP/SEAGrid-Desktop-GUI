package Gamess.gamessGUI.IncompatibilityPackage;

import java.util.Enumeration;

import Gamess.gamessGUI.GlobalParameters;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.IbuildList;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.RequiredIfRestriction;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.RequiresRestriction;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.RestrictionsHolder;

public class RequiresIncompatibility extends IncompatibilityBase
{
	private static RequiresIncompatibility instance = new RequiresIncompatibility(true);
	private static RequiresIncompatibility provisionalInstance = new RequiresIncompatibility(false);
	
	public static RequiresIncompatibility getInstance()
	{
		if(GlobalParameters.isProvisionalMode)
			return provisionalInstance;
		return instance;
	}
	
	private RequiresIncompatibility(boolean isMainObject) 
	{
		if(isMainObject)
			initiallize();
	}
	
	private void initiallize()
	{
		//requires
		RequiresRestriction requires = new RequiresRestriction();
		((IbuildList)requires).buildList("*");
		addIncompatibilityList(requires);
		
		//requiredIf
		RequiredIfRestriction required = new RequiredIfRestriction();
		((IbuildList)required).buildList("*");
		addIncompatibilityList(required);
	}
	
	@Override
	void callAddIncompatibilityList(RestrictionsHolder restrictions) 
	{
		//requires
		addIncompatibilityList(restrictions.getRequiresRestriction());
		//requiredIf
		addIncompatibilityList(restrictions.getRequiredIfRestriction());
	}

	@Override
	void callRemoveIncompatibilityList(RestrictionsHolder restrictions) 
	{
		//requires
		removeIncompatibilityList(restrictions.getRequiresRestriction());
		//requiredIf
		removeIncompatibilityList(restrictions.getRequiredIfRestriction());
	}

	@Override
	boolean isIncompatible(Incompatible incompatible) {
		//this is incompatible if the item is required and it is not available
		if(incompatible != null)
			return !incompatible.isAvailable();
		return false;
	}
	
	public String getPartialIncompatibility(String Data)
	{
		StringBuilder requiredList = new StringBuilder();
		for (Enumeration<String> keys = incompatibilityTable.keys(); keys.hasMoreElements() ;) {
			String currentKey = keys.nextElement();
			if(currentKey.startsWith(Data))
			{
				currentKey = currentKey.substring(Data.length()).trim();
				if(requiredList.length() != 0)
					requiredList.append("|");
				requiredList.append(currentKey);
			}
		}
		return requiredList.toString();
	}
}
