package Gamess.gamessGUI.Storage;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import Gamess.gamessGUI.GlobalParameters;

public class Repository implements IRepository {

	private static Repository provisionalInstance = new Repository();
	private static Repository instance = new Repository();
	private Groups groups = new Groups();
	private ArrayList<IDBChangeListener> listenerList = new ArrayList<IDBChangeListener>();

	private Repository()
	{
		groups.setListeners(listenerList);
	}
	
	public static Repository getInstance()
	{
		if(GlobalParameters.isProvisionalMode)
			return provisionalInstance;
		return instance;
	}
	
	public void DropDB() 
	{
		groups.removeAll();
		// Fire an event to drop all the incompatibilities generated
		for (int i = 0; i < listenerList.size(); i++) 
			listenerList.get(i).DropDB();
	}

	public boolean Overwrite(String Group, String Data) {
		//Check if the group is available
		if(!isAvailable(Group))
		{
			//if not available create it
			groups.CreateGroupInterface(Group);
		}
		DBInterface storage = groups.getGroupInterface(Group);
		return storage.Overwrite(Data);
	}
	
	public boolean Remove(String Group) {
		//Check if the group is available
		if(!isAvailable(Group))
		{
			//The group is not available. So return true
			return true;
		}
		groups.removeGroup(Group);
		return true;
	}

	public boolean Remove(String Group, String Data) {
		//Check if the group is available
		if(!isAvailable(Group))
		{
			//The group itself is not available.
			//Cannot remove the data from the group
			return false;
		}
		DBInterface storage = groups.getGroupInterface(Group);
		return storage.Remove(Data);
	}

	public String Retrieve(String Group) {
		//Check if the group is available
		if(!isAvailable(Group))
		{
			//Group is not avaialble. Nothing to return.
			//Check if this instance is provisionalInstance
			//if it is then you are searching in the temporary DB
			//try checking if it is available in the Main DB too.
			if(this.equals(provisionalInstance))
			{
				return instance.Retrieve(Group);
			}
			//return null
			return null;
		}
		DBInterface storage = groups.getGroupInterface(Group);
		//return complete group
		return storage.Retrieve("");
	}

	public String Retrieve(String Group, String Data) {
		//Check if the group is available
		if(!isAvailable(Group))
		{
			//Group is not avaialble. Nothing to return.
			//Check if this instance is provisionalInstance
			//if it is then you are searching in the temporary DB
			//try checking if it is available in the Main DB too.
			if(this.equals(provisionalInstance))
			{
				return instance.Retrieve(Group, Data);
			}
			//return null
			return null;
		}
		DBInterface storage = groups.getGroupInterface(Group);
		//return complete group
		return storage.Retrieve(Data);
	}

	public boolean Store(String Group, String Data) {
		//Check if the group is available
		if(!isAvailable(Group))
		{
			//Group is not available. 
			//Create the group and store the value in it.
			groups.CreateGroupInterface(Group);
		}
		DBInterface storage = groups.getGroupInterface(Group);
		return storage.Store(Data);
	}

	public boolean equal(String Group, String Data) {
		//Check if the group is available
		if(!isAvailable(Group))
		{
			//The group is not available.
			//Check if this instance is provisionalInstance
			//if it is then you are searching in the temporary DB
			//try checking if it is available in the Main DB too.
			if(this.equals(provisionalInstance))
			{
				return instance.equal(Group, Data);
			}
			//nothing to compare. So return false
			return false;
		}
		DBInterface storage = groups.getGroupInterface(Group);
		boolean isEqual = storage.equal(Data); 
		//Check if this instance is provisionalInstance
		//if it is then you are searching in the temporary DB
		//try checking if it is available in the Main DB too.
		if(this.equals(provisionalInstance) && isEqual == false)
		{
			isEqual = instance.equal(Group, Data);
		}
		
		return isEqual;
	}

	public boolean isAvailable(String Group) {
		//Return is the group available in Main/Provisional DB
		boolean isAvail = groups.isAvailable(Group);
		return isAvail;
	}

	public boolean isAvailable(String Group, String Data) {
		//Check if the group is available
		if(!isAvailable(Group))
		{
			//The group itself is not available.
			//Check if this instance is provisionalInstance
			//if it is then you are searching in the temporary DB
			//try checking if it is available in the Main DB too.
			if(this.equals(provisionalInstance))
			{
				return instance.isAvailable(Group, Data);
			}
			//So return false.
			return false;
		}
		DBInterface storage = groups.getGroupInterface(Group);
		boolean isAvail = storage.isAvailable(Data);
		//Check if this instance is provisionalInstance
		//if it is then you are searching in the temporary DB
		//try checking if it is available in the Main DB too.
		if(this.equals(provisionalInstance) && isAvail == false)
			isAvail = instance.isAvailable(Group, Data);
		return isAvail;
	}

	public String getKeywordValueSeperator(String Group) {
		//Check if the group is available
		if(!isAvailable(Group))
		{
			//The group itself is not available.
			//Check if this instance is main instance
			//if it is then try searching it in the provisional DB too.
			if(this.equals(instance))
			{
				return provisionalInstance.getKeywordValueSeperator(Group);
			}
			//Create a new group in the provisional DB.
			groups.CreateGroupInterface(Group);
		}
		DBInterface storage = groups.getGroupInterface(Group);
		return storage.getKeywordValueSeperator();
	}

	public void registerDBChangeListener(IDBChangeListener listener) 
	{
		listenerList.add(listener);
	}
	
	private class Groups
	{
		private ArrayList<IDBChangeListener> listenerList = new ArrayList<IDBChangeListener>();
		private Hashtable<String, DBInterface> groups = new Hashtable<String, DBInterface>();
		private ArrayList<String> plainDataGroup = null;
		
		public Groups()
		{
			//Loading plain data group
			plainDataGroup = GlobalParameters.plainDataGroup;
		}
		
		public boolean isAvailable(String Group)
		{
			if(groups.containsKey(Group.toUpperCase()))
			{
				return true;
			}
			return false;
		}
		
		public void CreateGroupInterface(String Group)
		{
			Group = Group.toUpperCase();
			removeGroup(Group);
			DBInterface storageInterface = null;
			if(Group.equals(("TDHFX")))
			{
				//Create TDHFX group storage
				storageInterface = new TDHFXGroupStorage();
				groups.put("TDHFX", storageInterface);
			}
			else if(plainDataGroup.contains(Group))
			{
				//if the Group belongs to the plain data group. 
				//Create a plain data group
				storageInterface = new PlainDataStorage();
				groups.put(Group, storageInterface);
			}
			else
			{
				//Create the default group
				storageInterface = new DefaultGroupStorage();
				groups.put(Group, storageInterface);
			}
			//Add the listeners
			storageInterface.registerDBChangeListeners(listenerList);
			//Set the group the object belongs to
			storageInterface.setGroup(Group);
			// Fire an event "add group" to the incompatibility
			for (int i = 0; i < listenerList.size(); i++) 
				listenerList.get(i).DataAdded(Group);
		}
		
		public DBInterface getGroupInterface(String Group)
		{
			Group = Group.toUpperCase();
			if(isAvailable(Group))
				return groups.get(Group);
			return null;
		}
		
		public void removeGroup(String Group)
		{
			//remove the group if it is aviailable
			if(isAvailable(Group))
			{
				((DBInterface)groups.get(Group.toUpperCase())).RemoveAll();
				groups.remove(Group.toUpperCase());
				// Fire an event "remove group" to the incompatibility
				for (int i = 0; i < listenerList.size(); i++) 
					listenerList.get(i).DataRemoved(Group);
			}
		}
		
		public void removeAll()
		{
			for(Enumeration<String> group = groups.keys(); group.hasMoreElements();)
			{
				groups.get(group.nextElement()).RemoveAll();
			}
			groups.clear();
		}
		
		public void setListeners(ArrayList<IDBChangeListener> listeners)
		{
			listenerList = listeners;
		}
	}
}
