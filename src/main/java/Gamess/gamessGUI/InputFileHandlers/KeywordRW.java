package Gamess.gamessGUI.InputFileHandlers;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public abstract class KeywordRW {

	private StyledDocument inputFile = null;
	protected int groupOffset = -1;
	protected String DefaultKeywordSeperator = " ";
	protected String CurrentGroupconText = null;
	protected String CurrentGroup = null;
	
	public KeywordRW(JTextPane _inputFilePane) {
		if(_inputFilePane != null)
			inputFile = _inputFilePane.getStyledDocument();
	} 

	protected final void loadText(String _Group, int _offset , int length)
	{
		CurrentGroupconText = null;
		CurrentGroup = _Group;
		groupOffset = _offset;
		try 
		{
			CurrentGroupconText = inputFile.getText(groupOffset, length);
		} catch (BadLocationException e) {}
	}

	protected final String getTextAt(int offset , int length)
	{
		String requestedText = null;
		try 
		{
			requestedText = inputFile.getText(offset, length);
		} catch (BadLocationException e) {}
		
		return requestedText;
	}

	
	protected final int insertTextAt(int pos , String text)
	{
		try
		{
			inputFile.insertString(pos, text , null);
		}
		catch (BadLocationException e) {}
		return pos + text.length();
	}
	
	protected final void replaceTextAt(int offset , int length , String replaceText)
	{
		try
		{
			inputFile.remove(offset, length);
			inputFile.insertString(offset, replaceText , null);
		}
		catch (BadLocationException e) {}
	}
	
	protected final void removeTextAt(int offset , int length)
	{
		try
		{
			inputFile.remove(offset, length);
		}
		catch (BadLocationException e) {}
	}
	
	abstract void write(String Group, String Data , int grpStart , int grpEnd);
	abstract void remove(String Group, String Data , int grpStart , int grpEnd);
	abstract String read(String Group, int grpStart , int grpEnd);;
	abstract String read(String Group, String Keyword , int grpStart , int grpEnd);
}
