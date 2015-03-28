package Gamess.gamessGUI.Storage;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import Gamess.gamessGUI.Dictionary;


public class DefaultGroupStorage implements DBInterface {

	private String Group = "";
	protected  StorageHashTable storage = new StorageHashTable();
	protected String KeywordValueSeperator = "=";
	protected ArrayList<IDBChangeListener> listenerList = null;
	
	public void setGroup(String Group) 
	{
		this.Group = Group;
	}
	
	public boolean Overwrite(String fullGroupData) {
		//Convert the full group data to hash table
		StorageHashTable currentGroupTable = ConvertGroupData(fullGroupData);
		if(currentGroupTable == null)
		{
			//The group is empty. Remove full group
			RemoveAll();
			storage.clear();
			return true;
		}
		//Compare the two list and make the DB consistent
		return CompareAndWrite(storage , currentGroupTable);
	}

	public boolean Remove(String Data) {
		//Check the format of the Data
		String Keyword = Data.trim(); 
		if(isSeperatorPresent(Data))
		{
			//Seperator is present. Format keyword '=' value
			//Get the keyword from the Data
			Keyword = Data.split(KeywordValueSeperator)[0].trim();
		}
		//Check if the keyword is available
		if(!isAvailable(Keyword))
		{
			//The keyword is not available. So return true.
			return true;
		}

		String removedValue = storage.get(Keyword);
		
		//The keyword is available. remove the keyword
		storage.remove(Keyword);
		
		//Fire the remove event for the incompatibility
		Keyword = Dictionary.getFormattedKeyword(Keyword);
		for (int i = 0; i < listenerList.size(); i++)
		{
			listenerList.get(i).DataRemoved(Group + " " + Keyword + " " + removedValue);
			listenerList.get(i).DataRemoved(Group + " " + Keyword);
		}
		return true;
	}

	public String Retrieve(String Data) {
		//Check the format of the Data
		//Check if the data is empty
		if(Data.trim().length() == 0)
		{
			//The Data is empty. Return the full list of keywords
			StringBuilder returnValue = new StringBuilder();
			for( Enumeration<String> keys = storage.keys() ; keys.hasMoreElements() ;) {
				returnValue.append(keys.nextElement() + " ");
			} 
			return returnValue.toString();
		}
		
		String keyword = Data.trim();
		//Check if the Data contains seperator
		if(isSeperatorPresent(Data))
		{
			//The Data contains seperator.
			//The format is Keyword '=' value
			//get the keyword form data
			keyword = Data.split(KeywordValueSeperator)[0].trim();
		}
		
		//Check if the keyword is available
		if(!isAvailable(keyword))
			return null;
		return storage.get(keyword);
	}

	public boolean Store(String Data) {
		if(!isSeperatorPresent(Data))
		{
			//wrong format. return false
			return false;
		}
		//The format is Keyword '=' value
		//seperate keyword and value
		String[] splitData = getDataSplit(Data.trim());
		String Keyword = splitData[0].trim();
		String Value = splitData[1].trim();
		//check if storage already contains keyword
		if(storage.containsKey(Keyword))
		{
			if(((String)storage.get(Keyword)).equals(Value))
			{
				//Same keyword and value is present
				//return true
				return true;
			}
			//Value is different
			Remove(Keyword);
		}
		storage.put(Keyword, Value);
		//Fire the event add for incompatibility
		Keyword = Dictionary.getFormattedKeyword(Keyword);
		for (int i = 0; i < listenerList.size(); i++)
		{
			listenerList.get(i).DataAdded(Group + " " + Keyword);
			listenerList.get(i).DataAdded(Group + " " + Keyword + " " + Value);
		}
		return true;
	}

	public boolean equal(String Data) {
		//Check if the data is in the correct format
		if(!isSeperatorPresent(Data))
		{
			//The seperator is not present. So return if the 
			//keyword is available or not.
			return isAvailable(Data);
		}
		//The format is Keyword '='value
		//seperate keyword and value
		String[] splitData = getDataSplit(Data.trim());
		String Keyword = splitData[0].trim();
		String Value = splitData[1].trim();
		//Check if the keyword is available
		if(!isAvailable(Keyword))
			return false;
		return ((String)storage.get(Keyword)).equals(Value);
	}

	public boolean isAvailable(String Data) {
		//Check if the data is in the correct format
		if(isSeperatorPresent(Data))
		{
			//The format is Keyword '='value
			//Check if it is equal
			return equal(Data);
		}
		//The data contains only keyword. Check if the keyword is present
		return storage.containsKey(Data.trim());
	}
	
	public String getKeywordValueSeperator() {
		return KeywordValueSeperator;
	}
	
	public boolean RemoveAll() {
		for(Enumeration<String> keys = storage.keys(); keys.hasMoreElements();)
			Remove(keys.nextElement());
		return true;
	}
	
	public void registerDBChangeListeners(ArrayList<IDBChangeListener> listeners) {
		listenerList = listeners;
	}
	
	protected boolean isSeperatorPresent(String Data)
	{
		if(Data.trim().indexOf(KeywordValueSeperator) == -1)
			return false;
		return true;
	}
	
	protected StorageHashTable ConvertGroupData(String GroupData)
	{
		//The group contains no data. Return null
		if(GroupData.trim().length() == 0)
			return null;
		StorageHashTable returnTable = new StorageHashTable();
		//Split the complete group with =
		String[] splitGroup = GroupData.trim().split(KeywordValueSeperator);
		
		//Get the first keyword
		String CurrentKeyword = splitGroup[0].trim();
		returnTable.put(CurrentKeyword, "");
		
		for (int i = 1; i < splitGroup.length; i++) {
			//Add each keyword and value to the table
			//The format here will be "value1 keyword2"
			String valueKeyword = splitGroup[i].trim();
			int splitIndex = valueKeyword.lastIndexOf(" ");
			if(splitIndex == -1 || i == splitGroup.length-1)
			{
				returnTable.put(CurrentKeyword, valueKeyword);
				continue;
			}
			String CurrentValue = valueKeyword.substring(0,splitIndex);
			returnTable.put(CurrentKeyword, CurrentValue.trim());
			CurrentKeyword = valueKeyword.substring(splitIndex).trim();
		}
		//return the built table
		return returnTable;
	}
	
	protected boolean CompareAndWrite(StorageHashTable DBList, StorageHashTable GroupList)
	{
		for(Enumeration<String> dbList = DBList.keys() ; dbList.hasMoreElements();)
		{
			//Get the first key
			String dbkey = dbList.nextElement();
			//Check if the key is present in the Grouplist
			if(!GroupList.containsKey(dbkey))
			{
				//The current db key is not present in the group in the input file.
				//Remove the current db key from the DBlist
				Remove(dbkey);
			}
			else
			{
				//The group list contains the db key.
				//The key is same in both the places
				//Get the value in InputFile and write it here
				String Data = dbkey + KeywordValueSeperator + (String)GroupList.get(dbkey);
				Store(Data);
				//note: Store writes the value only if they are different
				GroupList.remove(dbkey);
			}
		}
		
		//Get the list of remaining keys in the grouplist and add it in this DB
		for(Enumeration<String> remainKeys = GroupList.keys() ; remainKeys.hasMoreElements() ;)
		{
			String inputkey = remainKeys.nextElement();
			String Data = inputkey + KeywordValueSeperator + (String)GroupList.get(inputkey);
			Store(Data);
		}
		return true;
	}
	
	protected String[] getDataSplit(String Data)
	{
		String[] splitData = Data.split(KeywordValueSeperator);
		if(splitData.length == 1)
		{
			String[] newSplitData = new String[2];
			newSplitData[0] = splitData[0];
			newSplitData[1] = "";
			return newSplitData;
		}
		return splitData;
	}
	
	protected class StorageHashTable extends Hashtable<String, String>
	{
		private static final long serialVersionUID = -1644748080983543058L;

		@Override
		public synchronized String get(Object oKey) {
			//First check if the key is of type string
			if(oKey instanceof String)
			{
				String key = (String)oKey;
				if(key == null)
					return null;
				
				//Check if the key contains an array index starting
				key = Dictionary.getFormattedKeyword(key);
				
				//Convert the keyword to uppercase
				return super.get(key.toUpperCase());
			}
			return super.get(oKey);
		}
		
		@Override
		public synchronized String put(String key, String value) {

			if(key == null)
				return null;

			//Check if the key contains an array index starting
			key = Dictionary.getFormattedKeyword(key);

			//Convert the keyword and value to uppercase
			return super.put(key.toUpperCase(), (value == null)? null : value.toUpperCase());
		}
		
		@Override
		public synchronized boolean containsKey(Object oKey) {
			//First check if the key is of type string
			if(oKey instanceof String)
			{
				String key = (String)oKey;
				if(key == null)
					return super.containsKey(oKey);

				//Check if the key contains an array index starting
				key = Dictionary.getFormattedKeyword(key);

				//Convert the keyword to uppercase
				return super.containsKey(key.toUpperCase());
			}
			return super.containsKey(oKey);
		}
	}
}
