package Gamess.gamessGUI.Storage;

import java.util.ArrayList;

public class PlainDataStorage implements DBInterface {

	public String Group = "";
	private String storage = "";
	ArrayList<IDBChangeListener> listenerList = null;
	
	public void setGroup(String Group) 
	{
		this.Group = Group;
	}
	
	public boolean Overwrite(String fullGroupData) {
		storage = fullGroupData;
		return true;
	}

	public boolean Remove(String Data) {
		storage = "";
		return true;
	}

	public String Retrieve(String Data) {
		return storage;
	}

	public boolean Store(String Data) {
		storage = Data;
		return true;
	}

	public boolean equal(String Data) {
		if(storage.trim().equals(Data.trim()))
			return true;
		return false;
	}

	public boolean isAvailable(String Data) {
		if(storage.trim().length() > 0)
			return true;
		return false;
	}

	public String getKeywordValueSeperator() {
		return "";
	}

	public boolean RemoveAll() {
		storage = "";
		return true;
	}

	public void registerDBChangeListeners(ArrayList<IDBChangeListener> listeners) {
		listenerList = listeners;
	}

}
