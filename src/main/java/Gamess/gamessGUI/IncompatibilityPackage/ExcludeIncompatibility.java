package Gamess.gamessGUI.IncompatibilityPackage;

import Gamess.gamessGUI.GlobalParameters;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.AvailableOnlyIfRestriction;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.ExcludedIfRestriction;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.IbuildList;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.RestrictionsHolder;

public class ExcludeIncompatibility extends IncompatibilityBase
{
	private static ExcludeIncompatibility instance = new ExcludeIncompatibility(true);
	private static ExcludeIncompatibility provisionalInstance = new ExcludeIncompatibility(false);
	
	public static ExcludeIncompatibility getInstance()
	{
		if(GlobalParameters.isProvisionalMode)
			return provisionalInstance;
		return instance;
	}
	
	private ExcludeIncompatibility(boolean isMainObject)
	{
		//Check if it is the main object.
		//If it is then initialize it
		if(isMainObject)
			Initialize();
	}
	
	private void Initialize()
	{
		//test all the conditions first and remove all that is not satisfied
		//test all the availableOnlyIf condition
		AvailableOnlyIfRestriction availOnlyIfList = new AvailableOnlyIfRestriction();
		((IbuildList)availOnlyIfList).buildList("*");
		addIncompatibilityList(availOnlyIfList);
		
		//test all the excludedIf condition
		ExcludedIfRestriction excludedIfList = new ExcludedIfRestriction();
		((IbuildList)excludedIfList).buildList("*");
		addIncompatibilityList(excludedIfList);
	}

	@Override
	void callAddIncompatibilityList(RestrictionsHolder restrictions) 
	{
		//exclude
		addIncompatibilityList(restrictions.getExcludeRestriction());
		//excludedIf
		addIncompatibilityList(restrictions.getExcludedIfRestriction());
		//availableOnlyIf
		addIncompatibilityList(restrictions.getAvailableOnlyIfRestriction());
	}

	@Override
	void callRemoveIncompatibilityList(RestrictionsHolder restrictions) 
	{
		//exclude
		removeIncompatibilityList(restrictions.getExcludeRestriction());
		//excludedIf
		removeIncompatibilityList(restrictions.getExcludedIfRestriction());
		//availableOnlyIf
		removeIncompatibilityList(restrictions.getAvailableOnlyIfRestriction());
	}

	@Override
	boolean isIncompatible(Incompatible incompatible) {
		//this is incompatible if the item is excluded and it is available
		if(incompatible != null)
			return incompatible.isAvailable();
		return false;
	}
}
