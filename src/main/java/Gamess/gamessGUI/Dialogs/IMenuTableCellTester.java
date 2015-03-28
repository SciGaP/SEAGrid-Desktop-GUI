package Gamess.gamessGUI.Dialogs;

public interface IMenuTableCellTester 
{
	void recomputeConsistency();
	boolean isConsistent();
	boolean isDefault();
	String getGroup();
	String getKeyword();
	String getValue();
	void setGroup(String Group);
	void setKeyword(String Keyword);
	void resetValues();
}
