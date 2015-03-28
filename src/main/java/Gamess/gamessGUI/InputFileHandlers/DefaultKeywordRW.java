package Gamess.gamessGUI.InputFileHandlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;

public class DefaultKeywordRW extends KeywordRW {

	private static DefaultKeywordRW instance = null;
	protected String CurrentKeyword = "";
	protected String KeywordValueDelimiter = "=";
	
	protected DefaultKeywordRW(JTextPane inputFilePane)
	{
		super(inputFilePane);
	}
	
	public static DefaultKeywordRW getInstance(JTextPane inputFilePane )
	{
		if(instance == null)
		{
			instance = new DefaultKeywordRW(inputFilePane);
		}
		return instance;
	}

	//////////////////////////////////////////////////////////////
	
	protected boolean CheckDataFormat(String Data)
	{
		if(Data.indexOf(KeywordValueDelimiter) == -1)
			return false;
		else
			return true;
	}
	
	protected int findKeyword(String keyword)
	{
		Pattern keywordPattern = Pattern.compile("\\s+" + 
				keyword.replace("(", "\\(").replace(")", "\\)") + 
				"\\s*" + KeywordValueDelimiter + "\\s*" , Pattern.CASE_INSENSITIVE);
		Matcher keywordMatches = keywordPattern.matcher(CurrentGroupconText);
		if(keywordMatches.find())
		{
			return keywordMatches.end();
		}
		return -1;
	}
	
	protected void writeNewKeyword(String Data)
	{
		insertTextAt(groupOffset + CurrentGroupconText.length() , DefaultKeywordSeperator + Data);
	}
	
	protected String getValueAt(int valueLocation)
	{
		//get the value end location
		int valueEndLoc = CurrentGroupconText.indexOf(DefaultKeywordSeperator , valueLocation);
		if(valueEndLoc == -1)
		{
			valueEndLoc = CurrentGroupconText.length();
		}
		//get the value
		return getTextAt(groupOffset + valueLocation, valueEndLoc - valueLocation);
	}
	
	protected void writeValue(int valueLocation , String newValue )
	{
		//Get the current value to the keyword
		String currentValue = getValueAt(valueLocation);
		//Check if the value is same.
		if(!currentValue.equalsIgnoreCase(newValue))
		{
			//The current value and the new Value are different.
			//Replace the current value with the new value
			replaceTextAt(
					groupOffset + valueLocation,
					currentValue.length(), 
					newValue);
		}
	}

	/**
	 * Get the offset from where the keyword starts
	 * This gives the offset in the local Group String
	 * @param keyword The keyword that has to be searched 
	 * @return The location of the keyword in current group string 
	 */
	protected int getKeywordStarting(String keyword)
	{
		return CurrentGroupconText.indexOf(keyword);
	}
	
	protected String[] getKeywordValueSplit(String Data)
	{
		return Data.split(KeywordValueDelimiter);
	}
	
	//////////////////////////////////////////////////////////////

	@Override
	public void write(String Group, String Data , int grpStart , int grpEnd ) {
		//load the Group in the context
		loadText(Group, grpStart , grpEnd - grpStart);
		if(!CheckDataFormat(Data))
		{
			//Error: Not correct format 
			return;
		}
		String[] datapart = getKeywordValueSplit(Data);
		String keyword = datapart[0].trim();
		CurrentKeyword = keyword;
		String value = datapart[1].trim();
		
		//Check if the keyword is already present there
		//yes = replace value ; no = insert value
		int valueLocation = findKeyword(keyword);
		if(valueLocation == -1)
		{
			//Keyword not found. So write the keyword at the end of the list
			writeNewKeyword(Data);
			return;
		}
		
		//keyword is found. So replace the value
		writeValue(valueLocation, value);
	}

	@Override
	void remove(String Group, String Data, int grpStart, int grpEnd) {
		//load the Group in the context
		loadText(Group, grpStart , grpEnd - grpStart);
		
		String keyword = Data;
		if(CheckDataFormat(Data))
		{
			//Error: KeywordValueDelimiter is present. You can only remove keyword
			//return;
			String[] datapart = getKeywordValueSplit(Data);
			keyword = datapart[0].trim();
		}
		CurrentKeyword = keyword;
		int keywordEnding = findKeyword(keyword);
		if(keywordEnding == -1) return;
		int keywordStarting = getKeywordStarting(keyword);
		keywordStarting--; 	//for space
		
		//Remove the value
		writeValue(keywordEnding, "");

		//Remove the keyword
		replaceTextAt(
				groupOffset + keywordStarting, 
				keywordEnding - keywordStarting,
				"");
	}

	@Override
	String read(String Group, int grpStart, int grpEnd) {
		//load the Group in the context
		loadText(Group, grpStart , grpEnd - grpStart);
		return CurrentGroupconText;
	}

	@Override
	String read(String Group, String Keyword, int grpStart, int grpEnd) {
		//load the Group in the context
		loadText(Group, grpStart , grpEnd - grpStart);
		if(CheckDataFormat(Keyword))
		{
			//Error: KeywordValueDelimiter is present. You can only remove keyword
			//return;
			String[] datapart = getKeywordValueSplit(Keyword);
			Keyword = datapart[0].trim();
		}
		int keywordLocation = findKeyword(Keyword);
		if(keywordLocation == -1)
			return null;
		return getValueAt(keywordLocation);
	}
}
