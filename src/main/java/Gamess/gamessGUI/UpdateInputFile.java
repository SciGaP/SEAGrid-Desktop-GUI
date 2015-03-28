/*Copyright (c) 2007, Center for Computational Sciences, University of Kentucky.  All rights reserved.

Developed by:

Center for Computational Sciences, University of Kentucky

http://www.ccs.uky.edu

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software"), to deal with the Software without 
restriction, including without limitation the rights to use, 
copy, modify, merge, publish, distribute, sublicense, and/or 
sell copies of the Software, and to permit persons to whom 
the Software is furnished to do so, subject to the following 
conditions:
1. Redistributions of source code must retain the above copyright notice, 
   this list of conditions and the following disclaimers.
2. Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimers in the documentation
   and/or other materials provided with the distribution.
3. Neither the names of Center for Computational Sciences, University of Kentucky 
   nor the names of its contributors may be used to endorse or promote products 
   derived from this Software without specific prior written permission.
    
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS WITH THE SOFTWARE.

*/
/**
 * @author Michael Sheetz 
 * @author Pavithra Koka
 */

package Gamess.gamessGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

import Gamess.gamessGUI.Dialogs.FormatParser;
import Gamess.gamessGUI.Dialogs.MatrixDialog;
import Gamess.gamessGUI.Dialogs.MolecularSpecification;
import Gamess.gamessGUI.Storage.Repository;

public class UpdateInputFile implements ActionListener
{

	public static UpdateInputFile instance = new UpdateInputFile();
	public static Hashtable<String, StringLocation> snapshotGroup = null;
	public static boolean isConsistent = true;
	
	private UpdateInputFile()
	{
	}
	
	public static UpdateInputFile getInstance()
	{
		return instance;
	}
	
	public static void startUpdate()
	{
		if(UpdateInputFile.snapshotGroup == null)
		{
			UpdateInputFile.snapshotGroup = UpdateInputFile.getInstance().getValidInputGroups(false);
			GamessGUI.setUpdateMode(true);
		}
	}
	
	private void stopUpdate()
	{
		GamessGUI.setUpdateMode(false);
		snapshotGroup = null;
	}
	
	public void actionPerformed(ActionEvent evt) {
		//Do the update part
		if(evt.getActionCommand().equalsIgnoreCase("update all"))
		{
			UpdateAll();
		}
		else
		{
			if(snapshotGroup != null)
				UpdateDifferences();
			else
				UpdateAll();
		}
	}

	//Full update
	public boolean UpdateAll()
	{
		isConsistent = true;
		Repository database = Repository.getInstance();
		//Drop the current DB
		database.DropDB();
		
		//Updates All the group freshly
		Hashtable<String, StringLocation> validGroups = null;
		validGroups = getValidInputGroups(true);
		for (Enumeration<String> validGroup = validGroups.keys(); validGroup.hasMoreElements() ;) 
		{
			String group = validGroup.nextElement();
			StringLocation groupContents = validGroups.get(group);
			//groupContents = groupContents.replaceAll("\\s*!.*", " ").replace("\n", " ");
			if(checkFormat(group, groupContents))
				database.Overwrite(group.substring(1), validGroups.get(group).content);
			else
				isConsistent = false;
		}
		
		MessageBox.excludes.UpdateList();
		MessageBox.requires.UpdateList();
		
		stopUpdate();
		
		return true;
	}

	//Update differences
	public void UpdateDifferences()
	{
		//If there is no snapshot then do nothing
		if(snapshotGroup == null)
			return;
	
		isConsistent = true;
		
		Repository database = Repository.getInstance();
		
		//else get the current group
		Hashtable<String, StringLocation> currentGroups = getValidInputGroups(true);
		
		//compare groups one by one
		for (Enumeration<String> currentGroup = currentGroups.keys(); currentGroup.hasMoreElements() ;) 
		{
			String currentGroupKey = currentGroup.nextElement();
			//get the group value from the snapshot.
			//if the current group is not available in the snapshot
			//or if the values are different in both,
			//update the current value as it has been modified.
			StringLocation currentGroupContents = currentGroups.get(currentGroupKey);
			if(snapshotGroup.get(currentGroupKey) == null || !snapshotGroup.get(currentGroupKey).content.equalsIgnoreCase(currentGroupContents.content))
			{
				//currentGroupContents = currentGroupContents.replaceAll("\\s*!.*", " ").replace("\n", " ");
				//Check the format of the group here
				if(checkFormat(currentGroupKey, currentGroupContents))
						database.Overwrite(currentGroupKey.substring(1), currentGroupContents.content);
			}
			if(snapshotGroup.containsKey(currentGroupKey))
				snapshotGroup.remove(currentGroupKey);
		}
		
		//delete all the groups that are left in the snapshot
		for (Enumeration<String> existedGroup = snapshotGroup.keys(); existedGroup.hasMoreElements() ;) 
		{
			String currentGroupKey = existedGroup.nextElement();
			//remove the group from the database
			database.Remove(currentGroupKey.substring(1));
		}
		//reset the snapshot
		snapshotGroup = null;
		
		stopUpdate();
		
		MessageBox.excludes.UpdateList();
		MessageBox.requires.UpdateList();
	}
	
	public Hashtable<String, StringLocation> getValidInputGroups(boolean validate)
	{
		Hashtable<String, StringLocation> documentGroups = new Hashtable<String, StringLocation>();
		//get the all the paragraph element and get the groups
		StyledDocument inputDocument = GamessGUI.inputFilePane.getStyledDocument();
		Element rootElements = inputDocument.getDefaultRootElement();
		String currentGroup = null;
		int currentGroupStatringOffset = -1;

		try
		{
			for (int i = 0; i < rootElements.getElementCount(); i++) 
			{
				Element paragraphElement = rootElements.getElement(i);
	
				//skip the comment lines. 
				if(paragraphElement.getAttributes().containsAttributes(Cosmetics.COMMENTS_ATTRIBUTE))
					continue;
				
				//check if it has the group starting
				String CurrentLine = inputDocument.getText(paragraphElement.getStartOffset(), paragraphElement.getEndOffset() - paragraphElement.getStartOffset());
				//skip blank lines
				if(CurrentLine.trim().length() == 0)
					continue;
				if(currentGroup == null)
				{
					//There should be a group starting
					if(!CurrentLine.startsWith(" $"))
					{
						//The line has an error line
						if(validate)
						{
							//if the line starts with a $ then maybe the line is in correct format but there should be a space before it.
							if(CurrentLine.startsWith("$"))
							{
								Cosmetics.setUnderline(paragraphElement.getStartOffset(), (CurrentLine.indexOf(" ") != -1)?CurrentLine.indexOf(" "):CurrentLine.length() , Cosmetics.ERROR_UNDERLINE);
								Cosmetics.setTooltip(paragraphElement.getStartOffset(), (CurrentLine.indexOf(" ") != -1)?CurrentLine.indexOf(" "):CurrentLine.length() , "There should be a space before the group starting");
								isConsistent = false;
							}
							//The line is a junk of characters
							else
							{
								Cosmetics.setUnderline(paragraphElement.getStartOffset(), paragraphElement.getEndOffset() - paragraphElement.getStartOffset(), Cosmetics.ERROR_UNDERLINE);
								Cosmetics.setTooltip(paragraphElement.getStartOffset(), paragraphElement.getEndOffset() - paragraphElement.getStartOffset(), "Invalid line. The line should start with \" $\"");
								isConsistent = false;
							}
						}
						continue;
					}
				}
				
				//The group is a valid group
				//get all the groups in this line
				GroupRange range = null;
				for(int currentLineOffset = 0 ; (range = getGroupRange(CurrentLine, currentLineOffset) ).Start != -1; )
				{
					//Get the group
					String Group = CurrentLine.substring(range.Start, range.End);
					//check if the group is not a END group
					if(!Group.equalsIgnoreCase("$END"))
					{
						if(range.Start != 1)
						{
							Cosmetics.setUnderline(paragraphElement.getStartOffset() + range.Start, range.End - range.Start , Cosmetics.ERROR_UNDERLINE);
							Cosmetics.setTooltip(paragraphElement.getStartOffset() + range.Start, range.End - range.Start , "The group should start in a newline with a space before it");
							isConsistent = false;
							currentGroup = null;
						}
						else if(currentGroup != null)
						{
							//The current group does not have an $END
							if(validate)
							{
								Cosmetics.setUnderline(paragraphElement.getStartOffset() + range.Start, range.End - range.Start, Cosmetics.ERROR_UNDERLINE);
								Cosmetics.setTooltip(paragraphElement.getStartOffset() + range.Start, range.End - range.Start , "Expected group ending for group " + currentGroup);
								isConsistent = false;
							}
							
							currentGroup = null;
						}
						//check if the group is a valid one
						else if(isValidGroup(Group))
						{
							//Set the group as the current group.
							currentGroup = Group;
							//Set the offset as the current group offset.
							currentGroupStatringOffset = paragraphElement.getStartOffset() + range.End + 1;
						}
						else
						{
							//Set the current group to be an invalid group
							if(validate)
							{
								Cosmetics.setUnderline(paragraphElement.getStartOffset() + range.Start, range.End - range.Start , Cosmetics.ERROR_UNDERLINE);
								Cosmetics.setTooltip(paragraphElement.getStartOffset() + range.Start, range.End - range.Start , "Not a valid group");
								isConsistent = false;
							}
							
						}
					}
					//else the group is $END
					else
					{
						if(range.Start == 0)
						{
							Cosmetics.setUnderline(paragraphElement.getStartOffset() + range.Start, range.End - range.Start , Cosmetics.ERROR_UNDERLINE);
							Cosmetics.setTooltip(paragraphElement.getStartOffset() + range.Start, range.End - range.Start , "The group ending should not be the starting of the line");
							isConsistent = false;
							currentGroup = null;
						}
						else if(currentGroup != null)
						{
							String groupString = inputDocument.getText(currentGroupStatringOffset, paragraphElement.getStartOffset() + range.Start - currentGroupStatringOffset);
							if(documentGroups.get(currentGroup) == null)
								documentGroups.put(currentGroup, new StringLocation( currentGroupStatringOffset, groupString));
							else
							{
								//Warning the document already contains the group
								if(validate)
								{
									Cosmetics.setUnderline(currentGroupStatringOffset - currentGroup.length(), currentGroup.length(), Cosmetics.WARNING_UNDERLINE);
									Cosmetics.setTooltip(currentGroupStatringOffset - currentGroup.length(), currentGroup.length() , "The same group is available above");
									isConsistent = false;
								}
							}
							currentGroup = null;
						}
						else
						{
							//$End does not match any group starting
							if(validate)
							{
								Cosmetics.setUnderline(paragraphElement.getStartOffset() + range.Start, range.End - range.Start , Cosmetics.ERROR_UNDERLINE);
								Cosmetics.setTooltip(paragraphElement.getStartOffset() + range.Start, range.End - range.Start , "$END does not match any group starting");
								isConsistent = false;
							}
						}
					}
					currentLineOffset = range.End;
				}
			}
			if(currentGroup != null)
			{
				//$Group does not have an ending
				if(validate)
				{
					Cosmetics.setUnderline(currentGroupStatringOffset - currentGroup.length(), currentGroup.length() , Cosmetics.ERROR_UNDERLINE);
					Cosmetics.setTooltip(currentGroupStatringOffset - currentGroup.length(), currentGroup.length() , currentGroup + " does not have any group ending");
					isConsistent = false;
				}
			
			}
		}
		catch(BadLocationException e){}
		return documentGroups;
	}

	public GroupRange getGroupRange(String currentLine , int offset)
	{
		GroupRange range = new GroupRange();
		//Get the current group starting offset 
		range.Start = currentLine.indexOf("$" , offset);
		if(range.Start  == -1)
			return range;
		//Get the current group ending offset
		range.End = currentLine.indexOf(" ", range.Start + 1);
		if(range.End == -1)
			range.End = currentLine.length() - 1;
		
		return range;
	}
	
	private boolean isValidGroup(String group) 
	{
		return GlobalParameters.isGroupAvailable(group);
	}
	
	private class GroupRange
	{
		int Start = -1;
		int End = -1;
	}
	
	private class StringLocation
	{
		int startLocation = -1;
		String content = null;
		
		public StringLocation() {}
		public StringLocation(int start, String content) {
			startLocation = start;
			this.content = content;
		}
	}
	
	private boolean checkFormat(String group, StringLocation content)
	{
		//First check if the group should be processed
		//true - Proceed with validating keywords and value
		//false - Check if the group is valid. (Possible only if group = "TDHFX", "DATA" or ...
		if(shouldProcessGroup(group) == false)
		{
			return isGroupValid(group, content.content);
		}
		
		
		StringTokenizer tokens = new StringTokenizer(content.content , "=");
		String currentKeyword = null;
		int offset = 0;
		for (int i = 0 ; tokens.hasMoreTokens(); i++,offset++) {
			String token = tokens.nextToken();
			//Check if the loop is excuted first time
			if(i == 0)
			{
				//if this is the first round then this is a keyword
				if(token.trim().length() == 0)
				{
					Cosmetics.setUnderline(content.startLocation, 1 , Cosmetics.ERROR_UNDERLINE);
					Cosmetics.setTooltip(content.startLocation, 1 , "Invalid keyword starting");
					return false;
				}
				if(!isKeyword(group, token, content.startLocation))
					return false;
				currentKeyword = token;
			}
			//Check if the loop is executed last tim
			else if(tokens.countTokens() == 0)
			{
				if(!isValue(group, currentKeyword, token, content.startLocation + offset))
					return false;
				currentKeyword = null;
			}
			else
			{
				//Split the token into keyword and value
				String keyword = null , value = null;
				//Check if the keyword and a value is present.
				//else show error
				int seperationPosition = token.trim().lastIndexOf(" ");
				if(seperationPosition == -1)
				{
					seperationPosition = token.trim().lastIndexOf("\n");
					if(seperationPosition == -1)
					{
						//there is no keyword or value seperation
						Cosmetics.setUnderline(content.startLocation + offset, token.length() , Cosmetics.ERROR_UNDERLINE);
						Cosmetics.setTooltip(content.startLocation + offset, token.length() , "Invalid keyword/value");
						return false;
					}
				}
				seperationPosition++;
				keyword = token.trim().substring(seperationPosition);
				seperationPosition = token.indexOf(keyword);
				value = token.substring(0, seperationPosition);
				//check if the value is correct format.
				if(!isValue(group , currentKeyword, value, content.startLocation + offset))
					return false;
				currentKeyword = null;
				//check if the keyword is in the correct format
				offset += value.length();
				if(!isKeyword(group, keyword, content.startLocation + offset))
					return false;
				
				currentKeyword = keyword;
				token  = keyword;
			}
			offset += token.length();
		}
		if(currentKeyword != null)
		{
			Cosmetics.setUnderline(content.startLocation + offset - currentKeyword.length() - 1, currentKeyword.length() , Cosmetics.ERROR_UNDERLINE);
			Cosmetics.setTooltip(content.startLocation + offset - currentKeyword.length() - 1, currentKeyword.length() , "Keyword does not have an value");
			return false;
		}
		return true;
	}
	
	private boolean shouldProcessGroup(String group)
	{
		if(group.equalsIgnoreCase("TDHFX") || GlobalParameters.plainDataGroup.contains(group.substring(1)))
			return false;
		return true;
	}
	
	private boolean isGroupValid(String group, String content)
	{
		//TODO Hardcode the custom dialog validation
		if(group.equalsIgnoreCase("$DATA"))
		{
			((MolecularSpecification)Dictionary.dialogs.get("DATA")).ReadUpdate();
		}
		return true;
	}
	
	private boolean isKeyword(String group, String keyword, int offset)
	{
		if(keyword.trim().contains(" "))
		{
			//Keyword format not proper
			Cosmetics.setUnderline(offset, keyword.length() - 1 , Cosmetics.ERROR_UNDERLINE);
			Cosmetics.setTooltip(offset, keyword.length() - 1 , "Invalid keyword format");
			return false;
		}
		//HARDCODED for $DATA group read
		if(group.equalsIgnoreCase("$CONTRL") && (keyword.equalsIgnoreCase("ICHARG") || keyword.equalsIgnoreCase("MULT")))
			return true;

		//check if the keyword is valid
		if(Dictionary.get(group.substring(1).trim(), keyword.trim()) == null)
		{
			Cosmetics.setCharacterAttributes(offset, keyword.length(), Cosmetics.NORMAL_ATTRIBUTE, true);
			Cosmetics.setUnderline(offset, keyword.length(), Cosmetics.WARNING_UNDERLINE);
			Cosmetics.setTooltip(offset, keyword.length(), Cosmetics.getInputFileToolTip("This keyword is not available in the database.<br/>Possibilly not a valid keyword"));
		}
		else
			Cosmetics.setCharacterAttributes(offset, keyword.length(), Cosmetics.KEYWORD_ATTRIBUTE, true);
		return true;
	}
	
	private boolean isValue(String group, String keyword, String value, int offset)
	{
		//Check if the value is empty
		if(value.trim().length() == 0)
		{
			Cosmetics.setCharacterAttributes(offset, value.length(), Cosmetics.NORMAL_ATTRIBUTE, true);
			Cosmetics.setUnderline(offset, value.length(), Cosmetics.ERROR_UNDERLINE);
			Cosmetics.setTooltip(offset, value.length(), Cosmetics.getInputFileToolTip("Value is not valid<br/>Please enter the value or <br>Press <b>Ctrl + SPACE </b>to open the dialog"));
			return false;
		}
		
		//check if the value is valid format
		if(Dictionary.get(group.substring(1), keyword.trim(), value.trim()) == null)
		{
			//HARDCODED for $DATA group
			if(group.equalsIgnoreCase("$CONTRL") && (keyword.equalsIgnoreCase("ICHARG") || keyword.equalsIgnoreCase("MULT")))
				((MolecularSpecification)Dictionary.dialogs.get("DATA")).ReadUpdate();
			//The dictionary does not have the value for the specified keyword and the group.
			//So the value should be a (Grid/Text) dialog or can be a textValue
			//First check if the value is a TextValue
			else if(Dictionary.get(group.substring(1), keyword.trim(), Dictionary.TEXTBOX_VALUE) != null)
			{
				//This is a text value. get the format and test it
				FormatParser textFormat = FormatParser.getFormat(group.substring(1), keyword.trim(), null);
				if(textFormat.isConsistent(value.trim()) == false)
				{
					//Wrong format
					Cosmetics.setCharacterAttributes(offset, value.length(), Cosmetics.NORMAL_ATTRIBUTE, true);
					Cosmetics.setUnderline(offset, value.length(), Cosmetics.ERROR_UNDERLINE);
					Cosmetics.setTooltip(offset, value.length(), Cosmetics.getInputFileToolTip("This value is not in correct format.<br/>Please enter the value in correct format or <br>Press <b>Ctrl + SPACE </b>to open the dialog"));
					return false;
				}
				//Correct format
				else
					Cosmetics.setCharacterAttributes(offset, value.length(), Cosmetics.NORMAL_ATTRIBUTE, true);
			}
			//else check if the value is a dialog griddialog
			else if(Dictionary.get(group.substring(1), keyword.trim(), Dictionary.GRID_DIALOG) != null)
			{
				//This is a grid dialog. Convert it to matrix dialog and test it
				MatrixDialog dialog = (MatrixDialog)Dictionary.dialogs.get( Dictionary.getFormattedKeyword(group.substring(1) + " " + keyword.trim()) );
				if(dialog.testValue(group.substring(1), keyword, value) == false)
				{
					//Wrong format
					Cosmetics.setCharacterAttributes(offset, value.length(), Cosmetics.NORMAL_ATTRIBUTE, true);
					Cosmetics.setUnderline(offset, value.length(), Cosmetics.ERROR_UNDERLINE);
					Cosmetics.setTooltip(offset, value.length(), Cosmetics.getInputFileToolTip("This value is not in correct format.<br/>Please enter the value in correct format or <br>Press <b>Ctrl + SPACE </b>to open the dialog"));
					return false;
					//"This value is not available in the database for " + keyword + ".\nPossibilly not a valid value"
				}
				//Correct format
				else
					Cosmetics.setCharacterAttributes(offset, value.length(), Cosmetics.NORMAL_ATTRIBUTE, true);
			}
		}
		else
			Cosmetics.setCharacterAttributes(offset, value.length(), Cosmetics.NORMAL_ATTRIBUTE, true);
		return true;
	}
	
}// end of class


