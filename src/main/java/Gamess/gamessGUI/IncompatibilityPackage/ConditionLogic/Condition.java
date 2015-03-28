package Gamess.gamessGUI.IncompatibilityPackage.ConditionLogic;

import org.w3c.dom.Node;


public interface Condition 
{
	void add(Condition cond);
	void add(Node condNode);
	void add(String Data);
	boolean test();
	void trim();
	String toString();
}
