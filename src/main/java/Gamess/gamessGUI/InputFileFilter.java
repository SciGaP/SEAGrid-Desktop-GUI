package Gamess.gamessGUI;


import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

public class InputFileFilter extends DocumentFilter 
{
	StyledDocument styledDocument = null;
	JTextPane textPane = null;

	public InputFileFilter(JTextPane _textPane) 
	{
		textPane = _textPane;
		styledDocument = textPane.getStyledDocument();
		textPane.setHighlighter(new WavedUnderline());
	}
	
	@Override
	public void insertString(FilterBypass fb, int offset, String str, AttributeSet a) throws BadLocationException 
	{
		//Insert is normally called by the MenuBuilder or by the Dialogs
		if(UndoRedoHandler.isLocked() == false)
		{
			UndoRedoHandler.toggleGroupClassifier();
			UpdateInputFile.startUpdate();
		}
		
		//Highlight the keyword with color
		if(UndoRedoHandler.isLocked() && str.contains("="))
		{
			//This happens form the menubuilder
			int splitIndex = str.indexOf("=");
			String keyword = str.substring(0,splitIndex);
			if(keyword.trim().indexOf(" ") != -1)
			{
				splitIndex = 0;
				keyword = "";
			}
			if(keyword.startsWith(" "))
			{
				super.insertString(fb, offset, " " , Cosmetics.NORMAL_ATTRIBUTE);
				keyword = keyword.substring(1);
				offset++;
			}
			super.insertString(fb, offset, keyword, Cosmetics.KEYWORD_ATTRIBUTE);
			super.insertString(fb, offset + keyword.length(), str.substring(splitIndex), Cosmetics.NORMAL_ATTRIBUTE);
			return;
		}
		//First insert the text and parse the text
		super.insertString(fb, offset, str, Cosmetics.NORMAL_ATTRIBUTE);

		//Check if the string inserted is having a group
		//if it is then colour the group
		setAttributes(offset,offset + str.length() + str.split("\n").length);
	}
	
	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		//Remove can be called from (MenuBuilder|Dialogs) or Filemenu or by Handtyping
		//Do nothing if it is called by (MenuBuilder|Dialogs)
		//If the undoredohandler is locked then it is done by MenuBuilder/some Dialogs
		if(UndoRedoHandler.isLocked() == false)
		{
			UndoRedoHandler.toggleGroupClassifier();
			UpdateInputFile.startUpdate();
		}
		super.remove(fb, offset, length);
		Element elem = styledDocument.getParagraphElement(offset);

		setAttributes(elem);
	}
	
	private int previousInsertOffset = -2;
	
	@Override
	public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet a) throws BadLocationException {
		//Replace is called from mainly from Handtyping
		//Filter the single character typing
		UpdateInputFile.startUpdate();
		
		//if typed a character or a number or a ','  and if it is typed in a sequence 
		//then classify it as a same group otherwise change the group
		if(!(str.length() == 1 && (Character.isLetterOrDigit(str.toCharArray()[0]) || str.equals(",")) && previousInsertOffset == offset-1))
			UndoRedoHandler.toggleGroupClassifier();
		previousInsertOffset = offset;
		
		super.replace(fb, offset, length, str, Cosmetics.NORMAL_ATTRIBUTE);

		setAttributes(offset, offset + str.length());
	}
	
	/*static final String tooltip = "ToolTip";
	public void setGroupAttributes(String str, int offset)
	{
		int strOffset = 0;
		while(str.indexOf("$" , strOffset) != -1)
		{
			//get the starting position of the $ sign
			int startOffset = str.indexOf("$",strOffset);
			
			//get the ending position of the $ sign
			int endOffset = str.indexOf(" ", startOffset);
			if(endOffset == -1)
				endOffset = str.length();

			Element currentGroupElement = styledDocument.getCharacterElement(offset + startOffset);
			
			//Check if the group is valid group contained in the list of group
			if(GlobalParameters.listOfGroups.indexOf("|" + str.substring(startOffset, endOffset).toUpperCase().trim() + "|") == -1)
			{
				try 
				{
					//Group is not available. Set normal attributes
					//Set the tooltip
					SimpleAttributeSet normalErrorAttributes = Cosmetics.getCustomAttribute(tooltip ,"Invalid group identifier",Cosmetics.NORMAL_ATTRIBUTE);

					//Add the highlight and add its tag
					//Remove the underline if present

					Object HighlightTagAttribute = null;
					if( (HighlightTagAttribute = currentGroupElement.getAttributes().getAttribute("HighlightTag")) != null)
					{
						textPane.getHighlighter().changeHighlight(HighlightTagAttribute, offset + startOffset , offset + endOffset);
						normalErrorAttributes = Cosmetics.getCustomAttribute("HighlightTag", HighlightTagAttribute, normalErrorAttributes);
					}
					else
						normalErrorAttributes = Cosmetics.getCustomAttribute("HighlightTag", textPane.getHighlighter().addHighlight(offset + startOffset , offset + endOffset, Cosmetics.ERROR_UNDERLINE) , normalErrorAttributes);

					//Set the character attributes
					styledDocument.setCharacterAttributes(offset + startOffset ,endOffset - startOffset + 1, normalErrorAttributes , true);
				} 
				catch (BadLocationException e){}
				
				strOffset = endOffset;
				continue;
			}

			Object HighlightTagAttribute = null;
			if( (HighlightTagAttribute = currentGroupElement.getAttributes().getAttribute("HighlightTag")) != null)
				textPane.getHighlighter().removeHighlight(HighlightTagAttribute);

			//Also remove highlight
			
			//Set the Group attribute to the String
			styledDocument.setCharacterAttributes(offset + startOffset ,endOffset - startOffset + 1, Cosmetics.GROUP_ATTRIBUTE , true);
			strOffset = endOffset;
		}
	}*/
	
	private void setAttributes(int StartOffset, int EndOffset)
	{
		do
		{
			//Check if the end offset is greater than the actual end
			if(EndOffset > styledDocument.getLength())
				EndOffset = styledDocument.getLength();
			//
			Element paragraph = styledDocument.getParagraphElement(StartOffset);
			setAttributes(paragraph);
			StartOffset = paragraph.getEndOffset() + 1;
		}
		while(EndOffset > StartOffset);
	}
	
	///This is used to set the comment attribute and group attributes
	private void setAttributes(Element paragraphElement)
	{
		String line = null;
		try {
			line = styledDocument.getText(paragraphElement.getStartOffset() , paragraphElement.getEndOffset() - paragraphElement.getStartOffset());
		} catch (BadLocationException e1) {}
		
		String trimmedLine = line.trim();
		
		//Skip blankline
		if(trimmedLine.length() == 0)
			return;
		
		//Check if the line is a comment
		if(trimmedLine.startsWith("!"))
		{
			//set the comments attribute on all the characters
			Cosmetics.setCharacterAttributes(paragraphElement.getStartOffset() , paragraphElement.getEndOffset() - paragraphElement.getStartOffset(), Cosmetics.COMMENTS_ATTRIBUTE, true);
			//set the comments attribute on the paragraph element for identifying that this paragraph is compeately a comment line.
			//setting this attribute will help in other places to identify that this paragraphElement is a commented line.
			Cosmetics.setParagraphAttributes(paragraphElement.getStartOffset() , paragraphElement.getEndOffset() - paragraphElement.getStartOffset(), Cosmetics.COMMENTS_ATTRIBUTE);
			return;
		}

		//if the line is changed from comment to normal group reset the group to normal
		if(paragraphElement.getAttributes().containsAttributes(Cosmetics.COMMENTS_ATTRIBUTE))
		{
			Cosmetics.setParagraphAttributes(paragraphElement.getStartOffset() , paragraphElement.getEndOffset() - paragraphElement.getStartOffset(), Cosmetics.NORMAL_ATTRIBUTE);
			Cosmetics.setCharacterAttributes(paragraphElement.getStartOffset() , paragraphElement.getEndOffset() - paragraphElement.getStartOffset(), Cosmetics.NORMAL_ATTRIBUTE,true);
		}

		//Set the attributes of the group in the line 
		for(int startOffset = 0, endOffset = -1, lineCurrentOffset = 0; (startOffset = line.indexOf("$" , lineCurrentOffset)) != -1; lineCurrentOffset = endOffset)
		{
			//get the ending position of the $ sign
			endOffset = line.indexOf(" ", startOffset);
			if(endOffset == -1)
				endOffset = line.length()-1;

			//Check if the group is valid and group contained in the list of group
			if(GlobalParameters.isGroupAvailable(line.substring(startOffset, endOffset)))
			{
				//Set the Group attribute to the String
				styledDocument.setCharacterAttributes(paragraphElement.getStartOffset() + startOffset , endOffset - startOffset , Cosmetics.GROUP_ATTRIBUTE, true);
			}
		}

		//Reset all the invalid group elements 
		for (int i = 0; i < paragraphElement.getElementCount(); i++) {
			Element currentElement = paragraphElement.getElement(i);
			if(currentElement.getAttributes().containsAttributes(Cosmetics.GROUP_ATTRIBUTE))
			{
				//find the group starting
				int startOffset = currentElement.getStartOffset() - paragraphElement.getStartOffset();
				//find the end of the group
				int endOffset = line.indexOf(" ", startOffset);
				if(endOffset == -1)
					endOffset = line.length() - 1;
				//Get the group value
				String group =  line.substring(startOffset , endOffset);
				//See if the group is valid
				if(!GlobalParameters.isGroupAvailable(group))
						Cosmetics.setCharacterAttributes(startOffset + paragraphElement.getStartOffset(), endOffset - startOffset, Cosmetics.NORMAL_ATTRIBUTE, true);
				
				//Reset the counter if the endOffset has crossed the current element boundary
				if(paragraphElement.getStartOffset() + endOffset > currentElement.getEndOffset())
					i = paragraphElement.getElementIndex(paragraphElement.getStartOffset() + endOffset);
			}
		}
	}
}
