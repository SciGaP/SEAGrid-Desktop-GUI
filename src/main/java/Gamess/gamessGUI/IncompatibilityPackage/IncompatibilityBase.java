package Gamess.gamessGUI.IncompatibilityPackage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import Gamess.gamessGUI.IncompatibilityPackage.Restriction.IIncompatibilityList;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.RestrictionType;
import Gamess.gamessGUI.IncompatibilityPackage.Restriction.RestrictionsHolder;
import Gamess.gamessGUI.Storage.IDBChangeListener;
import Gamess.gamessGUI.Storage.Repository;

public abstract class IncompatibilityBase implements IDBChangeListener
{
	protected Hashtable<String, Incompatible> incompatibilityTable = new Hashtable<String, Incompatible>();
	
	public void DataAdded(String Data) 
	{
		//A new data is added. So build the incompatibility 
		//Get the Incompatibility for the Data from the IncompatibilityGetter
		//The RestrictionHolder is returned
		RestrictionsHolder restrictions = IncompatibilityGetter.getIncompatibility(Data);
		//Get the Restrictions one by one.
		//Test each restriction and add it to the Table
		//Call the subclass implementation for adding the items to the incompatibilityList
		callAddIncompatibilityList(restrictions);
		//Add the availability of the current added item if available
		Incompatible incompatible = incompatibilityTable.get(Data);
		if(incompatible != null)
			setIsAvailable(Data, incompatible);
	}

	public void DataRemoved(String Data) 
	{
		//The Data is removed from the storage.
		//Get the List of Incompatibilities from the IncompatibilityGetter
		RestrictionsHolder restrictions = IncompatibilityGetter.getIncompatibility(Data);
		//Test each Incompatibility and remove those from the Table
		//Call the subclass implementation for adding the items to the incompatibilityList
		callRemoveIncompatibilityList(restrictions);
		//Add the availability of the current added item if available
		Incompatible incompatible = incompatibilityTable.get(Data);
		if(incompatible != null)
			setIsAvailable(Data, incompatible);
	}

	public void DropDB() 
	{
		ArrayList<RestrictionType> incompatibleList = new ArrayList<RestrictionType>();
		//Remove all the incompatible objects
		for (Iterator<Incompatible> incompatibleItem = incompatibilityTable.values().iterator(); incompatibleItem.hasNext();) 
			incompatibleList.add(incompatibleItem.next());

		for (int i = 0; i < incompatibleList.size(); i++) 
			removeIncompatibility(incompatibleList.get(i));
	}

	protected void addIncompatibilityList(IIncompatibilityList incompatibilities)
	{
		//Get the list of required's/excluded's
		ArrayList<RestrictionType> incompatibilityList = incompatibilities.getIncompatibilityList();
		for (int i = 0; i < incompatibilityList.size(); i++) 
		{
			//get the requireds/excludeds one by one and test it
			RestrictionType restriction = incompatibilityList.get(i);
			
			//Test the condition
			if(restriction.testCondition())
			{
				//if the condition is true then add it to the list
				addIncompatibility(restriction);
			}
			//Control comes here if the testcondition is false
			//Check if the input is already available.
			else
			{
				//if the control comes here then the testcondition failed.
				//So the restriction does not exist. Remove it from the table if it completely fails
				removeIncompatibility(restriction);
			}
			
		}
	}
	
	protected void addIncompatibility(RestrictionType restriction)
	{
		String Input = restriction.getInput().trim();
		
		//if incompatibilityTable does not contain the restriction 
		if(!incompatibilityTable.containsKey(Input))
		{
			//create a new incompatible object with the new restriction
			Incompatible incompatible= new Incompatible(restriction);
			//test if the Input is available. Set the isAvailable variable
			setIsAvailable(Input, incompatible);
			//add a new incompatible object to the table
			incompatibilityTable.put(restriction.getInput(), incompatible);
			return;
		}
		//else get the incompatible object
		Incompatible incompatible = incompatibilityTable.get(Input);
		//Add the condition to the Or condition inside it 
		incompatible.addCondition(restriction.getCondition());
		//test if the Input is available. Set the isAvailable variable
		setIsAvailable(Input, incompatible);
	}
	
	protected void removeIncompatibilityList(IIncompatibilityList incompatibilities)
	{
		//Get the list of required/excluded
		ArrayList<RestrictionType> incompatibilityList = incompatibilities.getIncompatibilityList();
		for (int i = 0; i < incompatibilityList.size(); i++) 
		{
			//Get the incompatibilities one by one 
			RestrictionType restriction = incompatibilityList.get(i);
			//Check and see if the restriction is false.
			if(restriction.testCondition() == false)
			{
				//if it is false then try removing it from the table
				//test it and remove it
				removeIncompatibility(restriction);
			}
			else
			{
				//Control comes here is the restriction condition is true
				//So this is a restriction. add this restriction to the table
				addIncompatibility(restriction);
			}
		}
	}
	
	protected void removeIncompatibility(RestrictionType restriction)
	{
		//if incompatibilityTable does contain the restriction 
		if(incompatibilityTable.containsKey(restriction.getInput()))
		{
			//get the incompatibility retriction and test it.
			Incompatible incompatible = incompatibilityTable.get(restriction.getInput());
			//if the test condition is false then remove it
			if(!incompatible.testCondition())
				incompatibilityTable.remove(restriction.getInput());
			else
				setIsAvailable(restriction.getInput(), incompatible);
		}
	}
	
	protected void setIsAvailable(String Input , Incompatible incompatible)
	{
		String Group = "" , Keyword = "" , Value = "";
		//get the group and the Data
		int splitIndex = Input.indexOf(" ");
		if(splitIndex == -1)
		{
			Group = Input;
			incompatible.setAvailable(Repository.getInstance().isAvailable(Group));
		}
		else
		{
			Group = Input.substring(0,splitIndex).trim(); 
			//The format here is "GROUP KEYWORD VALUE" 
			//Convert it to "GROUP KEYWORD=VALUE"
			String[] Data = Input.substring(splitIndex).trim().split(" ");
			Keyword = Data[0];
			if(Data.length > 1)
				Value = Data[1];
			
			Repository DB = Repository.getInstance();
			
			//Check if the group is available
			if(!DB.isAvailable(Group))
				//Group is not available. so this incompatible object is not available
				incompatible.setAvailable(false);
			//Group is available. Check if the Keyword in that Group is available
			else if(Keyword.length() > 0 && !DB.isAvailable(Group, Keyword))
				//Keyword in this Group is not available. So this incompatible object is not available
				incompatible.setAvailable(false);
			//Group and Keyword are available. Check if the Value to that Keyword is available
			else if(Value.length() != 0)
				incompatible.setAvailable(DB.equal(Group, Keyword + DB.getKeywordValueSeperator(Group) + Value));
			else
				incompatible.setAvailable(true);
		}
		
		
	}
	
	public boolean isLikelyToBecomeIncompatible(String Data)
	{
		//Check if there is an entry for the incompatibility
		Incompatible incompatible = incompatibilityTable.get(Data);
		if(incompatible == null)
			return false;
		//if there is an entry check if it is incompatible
		//return isIncompatible(incompatible);
		return true;
	}
	
	
	public ArrayList<Incompatible> getIncompatibilityList() {
		ArrayList<Incompatible> incompatibles = new ArrayList<Incompatible>();
		for (Iterator<Incompatible> iter = incompatibilityTable.values().iterator(); iter.hasNext();) {
			Incompatible element = (Incompatible) iter.next();
			if(isIncompatible(element))
				incompatibles.add(element);
		} 
		return incompatibles;
	}
	
	abstract void callAddIncompatibilityList(RestrictionsHolder restrictions);
	abstract void callRemoveIncompatibilityList(RestrictionsHolder restrictions);
	abstract boolean isIncompatible(Incompatible incompatible);
}
