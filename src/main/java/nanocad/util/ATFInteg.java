package nanocad.util;

import java.io.*;
import nanocad.newNanocad;

class ATFInteg
{
	public static void main(String args[])
	{
	String textLine = new String("");
	int i = 0;
	try
	{
	    System.out.println("aData1.dat integrity check.");
	    AtomTypeFile f = new AtomTypeFile();
	    //BufferedReader text = new BufferedReader(new FileReader("p.txt"));
	    BufferedReader text = new BufferedReader(new FileReader(newNanocad.txtDir + 
	    		newNanocad.fileSeparator + "p.txt"));
	    
	    while(text.ready())
	    {   
		textLine = text.readLine();
		String numText = textLine.substring(0, textLine.indexOf(" "));
		int TextAtomNum = Integer.parseInt(numText);
		atomProperty curProp = f.getProperty(i);
		//System.out.println(curProp.getANumber() + " " + textLine);
		
		if(curProp.getANumber() != TextAtomNum)
		{
		    System.out.print("ANumber mismatch for " + textLine + " (" + i + ")");
		    System.out.println(" - dat:" + curProp.getANumber() + " txt:" + TextAtomNum);
		    while(curProp.getANumber() != TextAtomNum)
		    {
			int j = 0;
			while(curProp.getANumber() > TextAtomNum)
			{
			    textLine = text.readLine();
			    numText = textLine.substring(0, textLine.indexOf(" "));
			    TextAtomNum = Integer.parseInt(numText);
			    j++;
			}
			while(curProp.getANumber() < TextAtomNum)
		        {
			    i++;
			    curProp = f.getProperty(i);
			    j++;
			}
			System.out.println(j + " items skipped.");
		    }
		}
		i++;
	    }
	}
	catch(StringIndexOutOfBoundsException e)
	{
	    System.out.println("Text parse error in line " + i + ": " + textLine);
	    e.printStackTrace();
	}
	catch(Exception e)
	{
	    System.out.println("ERROR");
	    System.out.println(i + ": " + textLine);
	    e.printStackTrace();
	}
	System.out.println("Integrity check concluded.");
	}
}
