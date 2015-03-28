package Gamess.gamessGUI.InputFileHandlers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;

public class TDHFXKeywordRW extends DefaultKeywordRW {

	private static TDHFXKeywordRW instance = null;
	private ArrayList<String> set1 = new ArrayList<String>();
	private ArrayList<String> set2 = new ArrayList<String>();
	private String Keyword = "";
	private String Value = "";
	private String emptySpaces = "\n   ";
	
	protected TDHFXKeywordRW(JTextPane inputFilePane) {
		super(inputFilePane);
		DefaultKeywordSeperator = "\n";
		KeywordValueDelimiter = " ";
		
		//Load set1 values 
		set1.add("ALLDIRS");
		set1.add("DIR");
		set1.add("USE_C");
		set1.add("USE_Q");
		
		//Load set2 values
		set2.add("FREQ");
		set2.add("FREQ2");
	}

	public static TDHFXKeywordRW getInstance(JTextPane inputFilePane)
	{
		if(instance == null)
		{
			instance = new TDHFXKeywordRW(inputFilePane);
		}
		return instance;
	}
	
	@Override
	protected boolean CheckDataFormat(String Data) {
		String[] split = getKeywordValueSplit(Data);
		Keyword = split[0];
		Value = split[1];
		
		//Check if the format is normal
		//if this group dose not end with a newline add a newline at the end
		if(!getTextAt(groupOffset + CurrentGroupconText.length(), 1).equals("\n"))
		{
			replaceTextAt(groupOffset + CurrentGroupconText.length(), 1 , "\n");
		}
		return true;
	}
	
	@Override
	protected int findKeyword(String keyword) {
		//Check if the keyword is FREE
		if(!keyword.equalsIgnoreCase("FREE"))
		{
			//Get the normal pattern matching to get the keyword
			Pattern keywordPattern = Pattern.compile("\\s*" + 
					keyword.replace("(", "\\(").replace(")", "\\)") + 
					"[ \t]*", Pattern.CASE_INSENSITIVE);
			Matcher keywordMatches = keywordPattern.matcher(CurrentGroupconText);
			if(keywordMatches.find())
			{
				return keywordMatches.end();
			}
		}
		else
		{
			//Get the FREE keyword: based on the value
			//Check to which pattern the value belongs
			String matchedPattern = "";
			String freePattern1 = "\\d+\\.?\\d*+\\s*";
			String freePattern2 = "\\d+\\.?\\d*+\\s+\\d+\\.?\\d*+\\s*";
			String freePattern3 = "\\d+\\.?\\d*+\\s+\\d+\\.?\\d*+\\s+\\d+\\.?\\d*+\\s*";
			
			//Test for pattern1
			Pattern keywordPattern = Pattern.compile(freePattern1 , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			Matcher keywordMatches = keywordPattern.matcher(Value);
			if(keywordMatches.find())
			{
				matchedPattern = freePattern1;
			}
			
			//Test for pattern2
			keywordPattern = Pattern.compile(freePattern2 , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			keywordMatches = keywordPattern.matcher(Value);
			if(keywordMatches.find())
			{
				matchedPattern = freePattern2;
			}
			
			//Test for pattern3
			keywordPattern = Pattern.compile(freePattern3 , Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			keywordMatches = keywordPattern.matcher(Value);
			if(keywordMatches.find())
			{
				matchedPattern = freePattern3;
			}
			
			if(matchedPattern.length() == 0)
				return -1;
			
			//Test for the matching keyword in the input file
			keywordPattern = Pattern.compile("^(\\s*" + 
					keyword.replace("(", "\\(").replace(")", "\\)") + 
					"\\s+)" + matchedPattern + "$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			keywordMatches = keywordPattern.matcher(CurrentGroupconText);
			if(keywordMatches.find())
			{
				keywordMatches.find(1);
				return keywordMatches.end(1);
			}
		}
		return -1;
	}
	
	@Override
	protected void writeNewKeyword(String Data) {
		//Check if keyword is in set1
		if(set1.contains(Keyword))
		{
			//The keyword belongs to set 1.
			//Find the last set 1 item and write the keyword after that
			insertTextAt( groupOffset + findLastSet1Keyword() , emptySpaces + Data);
		}
		//else check if the keyword is in set 2
		else if(set2.contains(Keyword))
		{
			//The keyword belongs to set 1.
			insertTextAt( groupOffset + findLastSet2Keyword() , emptySpaces + Data);
		}
		//else the keyword is in set 3. So append the data
		else
		{
			//append the keyword to the last
			insertTextAt( groupOffset + CurrentGroupconText.length() , emptySpaces + Data);
		}
	}
	
	@Override
	protected int getKeywordStarting(String keyword) {
		int keywordStart = CurrentGroupconText.substring(0 , findKeyword(keyword)).lastIndexOf("\n");
		keywordStart++;
		return keywordStart;
	}

	@Override
	protected String[] getKeywordValueSplit(String Data) {
		String[] splitValue = Data.split(KeywordValueDelimiter);
		if(splitValue.length == 2)
			return splitValue;
		if(splitValue.length > 2)
		{
			String[] returnValue = new String[2];
			returnValue[0] = splitValue[0];
			returnValue[1] = "";
			for (int i = 1; i < splitValue.length; i++) {
				returnValue[1] += splitValue[i] + " ";
			}
			returnValue[1] = returnValue[1].trim();
			return returnValue;
		}
		String[] returnValue = new String[2];
		returnValue[0] = splitValue[0];
		returnValue[1] = "";
		return returnValue;
	}
	
	private int findLastSet1Keyword()
	{
		int lastLocation = 0;
		for (int i = 0; i < set1.size(); i++) 
		{
			int keywordLoc = CurrentGroupconText.indexOf(set1.get(i));
			if(keywordLoc > lastLocation)
				lastLocation = keywordLoc;
		}
		if(lastLocation != 0)
		{
			int endLocation = CurrentGroupconText.indexOf("\n" , lastLocation);
			if(endLocation != -1)
			{
				lastLocation = endLocation;
			}
			else
			{
				lastLocation += CurrentGroupconText.substring(lastLocation).length();
			}
		}
		return lastLocation;
	}
	
	private int findLastSet2Keyword()
	{
		int lastSet1Location = findLastSet1Keyword();
		int lastLocation = lastSet1Location;
		for (int i = 0; i < set2.size(); i++) 
		{
			int keywordLoc = CurrentGroupconText.indexOf(set2.get(i) , lastLocation);
			if(keywordLoc > lastLocation)
				lastLocation = keywordLoc;
		}
		if(lastLocation != lastSet1Location)
		{
			int endLocation = CurrentGroupconText.indexOf("\n" , lastLocation);
			if(endLocation != -1)
			{
				lastLocation = endLocation;
			}
			else
			{
				lastLocation += CurrentGroupconText.substring(lastLocation).length();
			}
		}
		return lastLocation;
	}
}
