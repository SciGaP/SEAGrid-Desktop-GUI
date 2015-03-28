package Gamess.gamessGUI.InputFileHandlers;

import javax.swing.JTextPane;

public class PlainDataRW extends KeywordRW {

	private static PlainDataRW instance= null;
	
	protected PlainDataRW(JTextPane _inputFilePane) {
		super(_inputFilePane);
	}

	public static PlainDataRW getInstance(JTextPane inputFilePane) {
		if(instance == null)
		{
			instance = new PlainDataRW(inputFilePane);
		}
		return instance;
	}
	
	@Override
	void remove(String Group, String Data, int grpStart, int grpEnd) {
		removeTextAt(grpStart, grpEnd - grpStart);
		insertTextAt(grpStart, " ");
	}

	@Override
	void write(String Group, String Data, int grpStart, int grpEnd) {
		//Remove the complete text and rewrite the text completely
		removeTextAt(grpStart, grpEnd - grpStart);

		//Rewrite the text with the newline at start and end
		StringBuilder sb = new StringBuilder(Data);
		if(!Data.startsWith("\n"))
		{
			sb.insert( 0 , "\n");
		}
		if(!Data.endsWith("\n"))
		{
			sb.append("\n");
		}
		
		insertTextAt(grpStart, sb.toString());
	}

	@Override
	String read(String Group, int grpStart, int grpEnd) {
		loadText(Group, grpStart, grpEnd - grpStart);
		return CurrentGroupconText;
	}

	@Override
	String read(String Group, String Keyword, int grpStart, int grpEnd) {
		return read(Group, grpStart, grpEnd);
	}

}
