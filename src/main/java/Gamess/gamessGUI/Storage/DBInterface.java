package Gamess.gamessGUI.Storage;

import java.util.ArrayList;


public interface DBInterface {
	boolean isAvailable(String Data);
	boolean equal(String Data);
	boolean Store(String Data);
	String Retrieve(String Data);
	boolean Remove(String Data);
	boolean RemoveAll();
	boolean Overwrite(String fullGroupData);
	String getKeywordValueSeperator();
	void registerDBChangeListeners(ArrayList<IDBChangeListener> listeners);
	void setGroup(String Group);
}
