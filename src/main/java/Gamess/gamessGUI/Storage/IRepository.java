package Gamess.gamessGUI.Storage;

public interface IRepository
{
	boolean isAvailable(String Group);
	boolean isAvailable(String Group, String Data);
	boolean equal(String Group, String Data);
	boolean Store(String Group, String Data);
	String Retrieve(String Group);
	String Retrieve(String Group, String Data);
	boolean Remove(String Group);
	boolean Remove(String Group, String Data);
	boolean Overwrite(String Group, String Data);
	void DropDB();
	String getKeywordValueSeperator(String group);
	void registerDBChangeListener(IDBChangeListener listener);
}
