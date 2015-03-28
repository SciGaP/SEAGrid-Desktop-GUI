package Gamess.gamessGUI.InputFileHandlers;

import java.util.ArrayList;

import javax.swing.JTextPane;

import Gamess.gamessGUI.GlobalParameters;

public class RWGetter {
	private static ArrayList<String> plainDataGroup = new ArrayList<String>();
	private static ArrayList<String> gridGroup = new ArrayList<String>();
	
	//public static void loadHandleDetails()
	static
	{
		//Loading plain data group
		plainDataGroup = GlobalParameters.plainDataGroup;
		
		//Loading grid group
		gridGroup = GlobalParameters.gridGroup;
	}
	
	public static KeywordRW getReadWriteHandle(String Group , JTextPane inputFilePane)
	{
		if(plainDataGroup.contains(Group.toUpperCase()))
		{
			//The group is a plain Data Group
			//return the plainDataGroup handler instance
			return PlainDataRW.getInstance(inputFilePane);
		}
		else if(gridGroup.contains(Group.toUpperCase()))
		{
			//The group is a grid group
			//return the grid group handler instance
			return GridKeywordRW.getInstance(inputFilePane);
		}
		else if(Group.toUpperCase().equals("TDHFX"))
		{
			//The group is a TDHFX group
			//return the TDHFX group handler instance
			return TDHFXKeywordRW.getInstance(inputFilePane);
		}
		else
		{
			//The group does not belong to any above group. This should be a default group
			//return the default group handler instance
			return DefaultKeywordRW.getInstance(inputFilePane);
		}
	}
}
