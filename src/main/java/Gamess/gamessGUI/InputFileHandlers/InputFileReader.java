package Gamess.gamessGUI.InputFileHandlers;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import Gamess.gamessGUI.GamessGUI;

public class InputFileReader {
	private static InputFileReader instance = new InputFileReader();
	private JTextPane inputFilePane = null;
	private StyledDocument inputFile = null;
	
	private InputFileReader()
	{
		if(GamessGUI.inputFilePane != null)
		{
			inputFilePane = GamessGUI.inputFilePane;
			inputFile = GamessGUI.inputFilePane.getStyledDocument();
		}
	}
	
	public static InputFileReader getInstance()
	{
		return instance;
	}
	
	public String Read(String Group)
	{
		//Read the full Group
		int groupStartPosition = -1;
		if((groupStartPosition = getGroupStartPosition(Group)) == -1)
		{
			return null;
		}
		groupStartPosition += Group.length() + 1;
		
		int groupEndPosition = getFullInputText().indexOf("$END", groupStartPosition);
		groupEndPosition--;
		
		KeywordRW KeywordReader = RWGetter.getReadWriteHandle(Group, inputFilePane);
		return KeywordReader.read(Group, groupStartPosition, groupEndPosition);
	}
	
	public String Read(String Group, String Keyword)
	{
		//Read the value of the keyword
		int groupStartPosition = -1;
		if((groupStartPosition = getGroupStartPosition(Group)) == -1)
		{
			return null;
		}
		groupStartPosition += Group.length() + 1;
		
		int groupEndPosition = getFullInputText().indexOf("$END", groupStartPosition);
		groupEndPosition--;
		
		KeywordRW KeywordReader = RWGetter.getReadWriteHandle(Group, inputFilePane);
		return KeywordReader.read(Group, Keyword, groupStartPosition, groupEndPosition);
	}
	
	public boolean isAvailable(String Group)
	{
		if(Read(Group) == null)
			return false;
		return true;
	}
	
	public boolean isAvailable(String Group, String Keyword)
	{
		int valueIndex = -1;
		if( (valueIndex = Keyword.indexOf("=")) == -1)
		{
			if(Read(Group,Keyword) == null)
				return false;
		}
		else
		{
			String value = Read(Group,Keyword.substring(0, valueIndex));
			if(value == null || !value.equalsIgnoreCase(Keyword.substring(valueIndex + 1)))
				return false;
		}
		return true;
	}
	
	private int getGroupStartPosition(String Group)
	{
		String fullText = getFullInputText();
		//Get the start index
		for(int startIndex, possibleStartIndex = fullText.indexOf("$" + Group); possibleStartIndex != -1 ; possibleStartIndex = fullText.indexOf("$" + Group,startIndex + 1))
		{
			startIndex = possibleStartIndex;
		
			//Try to hit the line starting backwards with empty spaces
			while(--possibleStartIndex >= 0 && fullText.charAt(possibleStartIndex) == ' ');

			//Check if it is the starting of the text
			if(possibleStartIndex == -1)
				return startIndex;
			//Check if there are some other characters in between
			if(fullText.charAt(possibleStartIndex) != '\n')
				continue;
			
			return startIndex;
		}
		return -1;
	}
	
	private String getFullInputText()
	{
		try {
			return inputFile.getText(0, inputFile.getLength());
		} catch (BadLocationException e) {}
		return "";
	}
}
