package Gamess.gamessGUI.IncompatibilityPackage.Restriction;



public class RestrictionsHolder
{
	private ExcludeRestriction exclude = new ExcludeRestriction();
	private RequiresRestriction requires = new RequiresRestriction();
	private ExcludedIfRestriction excludedif = new ExcludedIfRestriction();
	private RequiredIfRestriction requiredif = new RequiredIfRestriction();
	private AvailableOnlyIfRestriction availableonlyif = new AvailableOnlyIfRestriction();
	
	public void buildRestriction(String Data)
	{
		((IbuildList)exclude).buildList(Data);
		((IbuildList)requires).buildList(Data);
		((IbuildList)excludedif).buildList(Data);
		((IbuildList)requiredif).buildList(Data);
		((IbuildList)availableonlyif).buildList(Data);
	}
	
	public IIncompatibilityList getExcludeRestriction()
	{
		return exclude;
	}
	
	public IIncompatibilityList getExcludedIfRestriction()
	{
		return excludedif;
	}
	
	public IIncompatibilityList getAvailableOnlyIfRestriction()
	{
		return availableonlyif;
	}
	
	public IIncompatibilityList getRequiresRestriction()
	{
		return requires;
	}
	
	public IIncompatibilityList getRequiredIfRestriction()
	{
		return requiredif;
	}
}
