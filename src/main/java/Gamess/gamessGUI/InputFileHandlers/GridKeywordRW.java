package Gamess.gamessGUI.InputFileHandlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;

public class GridKeywordRW extends DefaultKeywordRW {

	private static GridKeywordRW instance = null;
	
	protected GridKeywordRW(JTextPane inputFilePane) {
		super(inputFilePane);
	}
	
	public static GridKeywordRW getInstance(JTextPane inputFilePane )
	{
		if(instance == null)
		{
			instance = new GridKeywordRW(inputFilePane);
		}
		return instance;
	}


	@Override
	protected String getValueAt(int valueLocation) {
		//if the Group is ZMAT and Keyword is either DLC or AUTO, Call the base function
		if(CurrentGroup.equalsIgnoreCase("ZMAT") && (CurrentKeyword.equalsIgnoreCase("DLC") || CurrentKeyword.equalsIgnoreCase("AUTO")))
			return super.getValueAt(valueLocation);
		
		//else match the Grid format
		Pattern keywordPattern = Pattern.compile("\\d+(\\s*,\\s*\\d+)*" , Pattern.CASE_INSENSITIVE);
		Matcher keywordMatches = keywordPattern.matcher(CurrentGroupconText.substring(valueLocation));
		if(keywordMatches.find())
		{
			return keywordMatches.group();
		}
		return "";
	}
}
