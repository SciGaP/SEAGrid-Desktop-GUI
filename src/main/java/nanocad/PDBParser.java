///////////////////////////////////////////////////////
// PDBParser.java
// September 2001 by Andrew Knox
// A parser for PDB Files.  Extends Parser.java
///////////////////////////////////////////////////////

package nanocad;

import java.lang.System;
import java.io.StreamTokenizer;                                                                                                               
import java.io.*;     
import java.util.*;    

public class PDBParser extends Parser
{

    public PDBParser(String data)
    {
	super(data);
	//System.out.println(data);
    }

    public void process() throws IOException
    {
	int firstAtom = 0;
	int columnNo = 0;
	boolean eof = false;
	boolean readingAtom = false;
	boolean readingBond = false;
	boolean newLine = true;
	boolean noone = false;
	boolean debug = true;
	
	inStream.eolIsSignificant(true);
	inStream.slashSlashComments(false);
	inStream.slashStarComments(false);
	inStream.ordinaryChar('/');

	//System.out.println("parsing...");
	do
	{
    	    int token = inStream.nextToken();
	    switch(token)
    	    {
		case StreamTokenizer.TT_EOF:
		    eof = true;
	    	    break;

		case StreamTokenizer.TT_EOL:
		    //for whatever reason, streamTokenizer is never picking up eols; thus, this code isn't being reached
		    readingAtom = false;
		    readingBond = false;
		    newLine = true;
		    System.out.println("end of line");
		    break;

		case StreamTokenizer.TT_WORD:
		    if (inStream.sval.equals("MASTER"))
		    {
			eof = true;
		    }
		    if (inStream.sval.equals("ATOM") || inStream.sval.equals("HETATM"))
		    {
		        readingAtom = true;
		        readingBond = false;
		        newLine = false;
			columnNo = 1;
		    }
		    else if (inStream.sval.equals("CONNECT") || inStream.sval.equals("CONECT"))
		    {
			readingAtom = false;
			readingBond = true;
			newLine = false;
			columnNo = 1;
		    }
		    else if (inStream.sval.equals("UNK"))
		    {
			//dont' increment columnNo
			break;
		    }
		    else if (readingAtom == true && columnNo == 2)
		    {
			String symbol = inStream.sval;
			//System.out.println(symbol);
			//symbol = symbol.substring(1, symbol.length() - 3);
			sym.addElement(symbol);
			columnNo++;
		    }
		    else
		    {
			if (newLine == true)
			{
			    while (!inStream.sval.equals("ATOM") && !inStream.sval.equals("HETATM") && 
					!inStream.sval.equals("CONNECT") && !inStream.sval.equals("CONECT"))
			    {
				inStream.nextToken();
				while(inStream.sval == null)
				    inStream.nextToken();
				if (inStream.sval.equals("/"))
				    inStream.nextToken();
				System.out.println("Skipped token:" + inStream.sval);
			    }	
			    inStream.pushBack();
			}
			else
			    columnNo++;           //ignore word
			    //System.out.println("word " + inStream.sval + " ignored");
		    }
		    break;

		case StreamTokenizer.TT_NUMBER:
		    if (readingAtom == true)
		    {
			if (columnNo == 3)
			    if (inStream.nval != 1)
				noone = true;
			if ((columnNo == 4 && noone == false) || (columnNo == 3 && noone == true))
		    	    atomX.addElement(new Double(inStream.nval));
			if ((columnNo == 5 && noone == false) || (columnNo == 4 && noone == true))
			    atomY.addElement(new Double(inStream.nval));
			if ((columnNo == 6 && noone == false) || (columnNo == 5 && noone == true))
			{
			    atomZ.addElement(new Double(inStream.nval));
			    noone = false;
			}
			columnNo++;
		    }
		    else if (readingBond == true)
		    {
			if (columnNo == 1)
			    firstAtom = (int) inStream.nval;
			else
		        {
			    System.out.println("column:" + String.valueOf(columnNo));
			    boolean bondExists = false;
			    for (int i = 0; i < bAtom1.size(); i++)
			    {
				if ( ((Double)bAtom1.elementAt(i)).intValue() == firstAtom && 
				     ((Double)bAtom2.elementAt(i)).doubleValue() == inStream.nval)
			        {
				    int order = ((Double)bOrder.elementAt(i)).intValue();
				    order++;
				    bOrder.setElementAt(new Double(order), i);
				    bondExists = true;
				    System.out.println("upped " + firstAtom + " " + inStream.nval + " order " + order); 
				}
			    }
			    if (bondExists == false && firstAtom < inStream.nval)
			    {
				bAtom1.addElement( new Double(firstAtom));
				bAtom2.addElement( new Double(inStream.nval));
				bOrder.addElement( new Double(1) );
				System.out.println("added " + firstAtom + " " + inStream.nval + " order 1"); 
			    }
			}
		        columnNo++;
		    }
		    break;	

		default:
		    System.out.println("Random token:" + token);
		    break;
	    }
	} while(!eof);
	     	    
	//sym.removeElementAt(sym.size()-1);

	if(debug == false)     
        { 
		System.out.println(sym.size() + " " + atomX.size() + " " + atomY.size() + " " + atomZ.size());
		for (int i=0; i< sym.size(); i++) {
                	System.out.println("i is :"+i+"\n");
			System.out.println("Sym contains : "+ sym.elementAt(i));
			System.out.println(atomX.size() + " " + atomY.size() + " " + atomZ.size());
			System.out.println("atomX : "+ atomX.elementAt(i));
                        System.out.println("atomY : "+ atomY.elementAt(i));
                        System.out.println("atomZ : "+ atomZ.elementAt(i));
                        System.out.println("\n");
                }
		System.out.println(bAtom1.size() + " " + bAtom2.size() + " " + bOrder.size());           

		for (int i=0; i<bAtom1.size(); i++){ 
                        System.out.println("i: " + i);
			System.out.println("bAtom1 : "+ sym.elementAt(((Double)bAtom1.elementAt(i)).intValue() - 1) + " " + bAtom1.elementAt(i));
                        System.out.println("bAtom2 : "+ sym.elementAt(((Double)bAtom2.elementAt(i)).intValue() - 1) + " " + bAtom2.elementAt(i));
                        System.out.println("bOrder : "+ bOrder.elementAt(i));
                        System.out.println("\n");
                }
          }          
    }

}
