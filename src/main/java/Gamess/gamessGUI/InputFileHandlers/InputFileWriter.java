package Gamess.gamessGUI.InputFileHandlers;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import Gamess.gamessGUI.GamessGUI;


public class InputFileWriter {

	private static InputFileWriter instance = new InputFileWriter();
	private JTextPane inputFilePane = null;
	private StyledDocument inputFile = null;
	private String GroupOrder = "CONTRL,BASIS";
	
	private InputFileWriter()
	{
		if(GamessGUI.inputFilePane != null)
		{
			inputFilePane = GamessGUI.inputFilePane;
			inputFile = GamessGUI.inputFilePane.getStyledDocument();
		}
	}
	
	public static InputFileWriter getInstance()
	{
		return instance;
	}
	
	public void Write(String Group, String Data)
	{
		//Get the group position where the keyword has to be written
		int groupStartPos = getKeywordGroupPosition(Group);
		groupStartPos += Group.length() + 1;
		
		//Get the end position of the text
		int groupEndPos = getFullInputText().toUpperCase().indexOf("$END", groupStartPos);
		groupEndPos--;
		
		//if the group does not have an  ending then add it
		if(groupEndPos < 0)
		{
			int caretPosition = inputFilePane.getCaretPosition();
			insertTextAt(inputFile.getLength() , " $END");
			groupEndPos = getFullInputText().toUpperCase().indexOf("$END", groupStartPos);
			groupEndPos--;
			inputFilePane.setCaretPosition(caretPosition);
		}
		if(groupEndPos < 0)
			return;
			
		//Get the keyword writer and write the data
		KeywordRW KeywordWriter= RWGetter.getReadWriteHandle(Group, inputFilePane);
		KeywordWriter.write(Group, Data, groupStartPos, groupEndPos);
	}
	
	public void Remove(String Group, String Data)
	{
		//Check if the Group is present
		int groupStartPos = getGroupPosition(Group);
		if(groupStartPos == -1)
		{
			//Error: group not found
			return;
		}
		groupStartPos += Group.length() + 1;
		
		//Get the end position of the text
		int groupEndPos = getFullInputText().toUpperCase().indexOf("$END", groupStartPos);
		groupEndPos--;
		
		//Get the keyword writer and write the data
		KeywordRW KeywordWriter= RWGetter.getReadWriteHandle(Group, inputFilePane);
		KeywordWriter.remove(Group, Data, groupStartPos, groupEndPos);
	}
	
	//////////////////////////////////////////////////////////////////
	//			Get the position where group is						//
	//			If group is not found write it						//
	//////////////////////////////////////////////////////////////////
	private int getKeywordGroupPosition(String Group)
	{
		int groupPos = -1;
		//check if the group is present
		//if yes return the group position
		//if no create the group position and return it
		if( (groupPos = getGroupPosition(Group)) == -1)
		{
			//Group is not present so create it
			
			//First check the rule for the order in which the
			//Group has to appear
			
			//Check if this group is there in the order
			if(GroupOrder.indexOf(Group) == -1)
			{
				//Group is  not there in the order. So append the group
				groupPos = inputFile.getLength();
				if(groupPos != 0)
				{
					if(!getFullInputText().endsWith("\n"))
					{
						insertTextAt(inputFile.getLength(), "\n");
						groupPos += "\n".length();
					}
				}
			}
			else
			{
				//Group is there  in some order.
				//Resolve the order
				String[] OrderSplit = GroupOrder.split(",");
				int GroupLocation = -1;
				for (int i = 0; i < OrderSplit.length; i++) {
					if(OrderSplit[i].equals(Group))
					{
						GroupLocation = i;
						break;
					}
				}
				
				//Check if you can insert this group after the previous group
				groupPos = insertGroupBefore(OrderSplit, GroupLocation);
				
				//else check if you can insert this group before next available group
				if(groupPos == -1)
				{
					groupPos = insertGroupAfter(OrderSplit, GroupLocation);
				}
			}
			
			//Create the group and write the text start and end
			insertTextAt(groupPos, " $" + Group.toUpperCase() + " $END");
			groupPos++; //for the space before the group starting.(see above line)
		}
		
		return groupPos;
	}
	
	private int insertGroupBefore(String[] GroupsBefore , int currGrpPos)
	{
		//Find the last group in the set that is available and insert after that
		//reverse direction
		for (int i = currGrpPos - 1 , insPos; i >= 0 ; i--) {
			if(GroupsBefore[i].equals("..."))
				return -1;
			if((insPos = getGroupPosition(GroupsBefore[i])) != -1)
			{
				//Last group that is available is found
				//find the end of the group
				insPos = getFullInputText().toUpperCase().indexOf("$END", insPos);
				insPos += "$END".length();
				insPos = insertTextAt(insPos, "\n");
				return insPos;
			}
		}
		
		//No group is found before this. So write the group in the first
		if(inputFile.getLength() != 0)
			insertTextAt(0, "\n");
		return 0;
	}
	
	private int insertGroupAfter(String[] GroupsAfter , int currGrpPos)
	{
		//Control comes here only if there is ... before the current group
		
		//Find the first group in the set that is available and insert before that
		//forward direction
		for (int i = currGrpPos + 1 , insPos; i < GroupsAfter.length; i++) {
			if(GroupsAfter[i].equals("..."))
				continue;
			if((insPos = getGroupPosition(GroupsAfter[i])) != -1)
			{
				insertTextAt(insPos, "\n");
				return insPos;
			}
		}
		
		//No group is found after this group. So append this at the end
		if(inputFile.getLength() != 0)
		{
			if(!getFullInputText().endsWith("\n"))
				insertTextAt(inputFile.getLength(), "\n");
		}
		return inputFile.getLength();
	}
	
	private int insertTextAt(int pos , String text)
	{
		try
		{
			inputFile.insertString(pos, text , null);
		}
		catch (BadLocationException e) {}
		return pos + text.length();
	}
	
	private int getGroupPosition(String Group)
	{
		String fullText = getFullInputText().toUpperCase();
		//Get the start index
		for(int startIndex, possibleStartIndex = fullText.indexOf("$" + Group.toUpperCase()); possibleStartIndex != -1 ; possibleStartIndex = fullText.indexOf("$" + Group,startIndex + 1))
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

	//////////////////////////////////////////////////////////////////
	
	private String getFullInputText()
	{
		try {
			return inputFile.getText(0, inputFile.getLength());
		} catch (BadLocationException e) {}
		return "";
	}
}
