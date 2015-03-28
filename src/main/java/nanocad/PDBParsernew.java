///////////////////////////////////////////////////////
// PDBParsernew.java
// June 2006 by Sudhakar Pamidighantam
// A parser for PDB Files.  Extends Parser.java
///////////////////////////////////////////////////////

package nanocad;

import java.lang.System;
import java.io.StreamTokenizer;                                                                                                               
import java.io.*;     
import java.util.*;    

public class PDBParsernew extends Parser{
	  //static PDBEntry entry = null;		// Entry being parsed
 LineNumberReader input;
 int lineNumber;
 boolean done = false;
  boolean needLine;
   int lastRecordNum;
   String line;
   StreamTokenizer inStream;
   int firstAtom = 0;
   int currentAtom;
int columnNo = 0;
boolean eof = false;
boolean readingAtom = false;
boolean readingBond = false;
boolean newLine = true;
boolean noone = false;
boolean debug = true;
		

public PDBParsernew(String filename, String data){
	//System.out.println(data);
    	/*
    	for (int i =0; i<data.length(); i++){
    	    out.write(data.charAt(i));
    	}

    	in = new StringReader(out.toString()); 
    	
    	inStream = new StreamTokenizer(in);
    	inStream.commentChar('#');
		inStream.eolIsSignificant(true);
		inStream.slashSlashComments(false);
		inStream.slashStarComments(false);
		inStream.ordinaryChar('/');
		*/
        super(data);


      //entry = null;		// Entry being parsed
	  done = false;
	  needLine = true;
      String inFile=filename;
      try {
      
	  input = new LineNumberReader(new FileReader(inFile));
	  done = false;

	  for ( ; done == false; ) {
	    if (needLine) {
	      line = input.readLine();
	    } else {
	      needLine = true;
	    }
	    
	    lineNumber = input.getLineNumber();
	    System.out.println("Getting Line number "+lineNumber+" in Structure file ");
	    System.out.println(" Current line is \n"+line);
	    lineDispatch(line);
	  }
      } catch (Exception fnf){
      System.out.println("PDBParsernew:"+fnf);}
      
	  /*
	   
	   if (false) {
	    // Wait till you've parsed the PDB entry before creating the
	    // output file.
	    FileWriter fout = new FileWriter(outFile);

	    // Write out the PDB entry to the output file
	    // Base64.translate(entry, fout);
	    // fout.write(entry.toString());
	    MBParen.translate(entry, fout);
	    fout.close();
	  } else {
	    FileOutputStream fout = new FileOutputStream(outFile);
	    // ObjectOutputStream out = new ObjectOutputStream(fout);
	    // out.writeObject(entry);
	    int numNodes = entry.nSubterms();
	    Object a = entry.Subterm(1);
	    Object b = entry.Subterm(2);	      
	    MathBus2XML.parse2XML(entry, new FileWriter(outFile));;
	    fout.close();
	  }
	  */

    /*
     System.out.println();
    System.out.println("(press Enter to exit)");
    System.out.println("");
  
    System.in.read();
    
  } 
  catch (Exception e) {
    System.out.println();
    System.out.println("Exception caught: " + e.getMessage());
    e.printStackTrace();
    try {
	System.out.println();
	System.out.println("(press Enter to exit)");
	System.out.println("");
    
	System.in.read();
    }
    catch (IOException e1) {
	return;
    }        
    return;
  }
  */
}

public   void lineDispatch (String line) throws IOException {
  if (line.length() >=7) {System.out.println(" Line starts with "+ line.substring(0,7));}
  if (line.startsWith("HEADER")) readHEADER();
  else if (line.startsWith("OBSLTE")) ;
  else if (line.startsWith("TITLE ")) ;
  else if (line.startsWith("CAVEAT")) ;
  else if (line.startsWith("COMPND")) { readCOMPOUND();}
  else if (line.startsWith("SOURCE")) ;
  else if (line.startsWith("KEYWDS")) ;
  else if (line.startsWith("EXPDTA")) ;
  else if (line.startsWith("AUTHOR")) ;
  else if (line.startsWith("REVDAT")) ;
  else if (line.startsWith("JRNL  ")) ;
  else if (line.startsWith("REMARK")) ;
  else if (line.startsWith("DBREF ")) ;
  else if (line.startsWith("SEQADV")) ;
  else if (line.startsWith("SEQRES")) {readSEQRES();}
  else if (line.startsWith("MODRES")) ;
  else if (line.startsWith("HET   ")) ;
  else if (line.startsWith("HETNAM")) ;
  else if (line.startsWith("HETSYN")) ;
  else if (line.startsWith("FORMUL")) ;
  else if (line.startsWith("HELIX ")) ;
  else if (line.startsWith("SHEET ")) ;
  else if (line.startsWith("TURN  ")) ;
  else if (line.startsWith("SSBOND")) ;
  else if (line.startsWith("LINK  ")) ;
  else if (line.startsWith("SLTBRG")) ;
  else if (line.startsWith("SLTBRG")) ;
  else if (line.startsWith("CISPEP")) ;
  else if (line.startsWith("SITE  ")) ;
  else if (line.startsWith("CRYST1")) ;
  else if (line.startsWith("ORIGX")) ;
  else if (line.startsWith("SCALE")) ;
  else if (line.startsWith("MTRIX")) ;
  else if (line.startsWith("TVECT ")) ;
  else if (line.startsWith("MODEL ")) ;
  else if (line.startsWith("ATOM  ")) {System.out.println(" Will read Atom info"); readATOM();return;}
  else if (line.startsWith("SIGATM")) ;
  else if (line.startsWith("ANISOU")) ;
  else if (line.startsWith("SIGUIJ")) ;
  else if (line.startsWith("TER   ")) ;
  else if (line.startsWith("HETATM")) {System.out.println(" Will read HeteroAtom info");readATOM();return;}
  else if (line.startsWith("ENDMDL")) ;
  else if (line.startsWith("CONNECT")) {System.out.println(" Will read Bond info");readBONDS();return;}
  else if (line.startsWith("CONECT"))  {System.out.println(" Will read Bond info");readBONDS();return;}
  else if (line.startsWith("MASTER")) { eof= true; done = true;}
  else if (line.startsWith("END   ")) { eof= true;   done = true;  }
  else if (line.startsWith("")){ eof= true;  done = true; }
  else System.out.println("Unknown record type (" + line.substring(0,6) 
			    + ") in PDB entry at line " 
			    + lineNumber);
}

public  void readHEADER () 
throws IOException {
  if (line.length() >= 66){
	  String classification = line.substring(10, 50).trim();
  
  
  if (line.length() >= 66) {
  String IDcode = line.substring(62, 66);
  }
  
  int day = Integer.parseInt(line.substring(50,52));
  int month;
  String monthString = line.substring(53,56);
  int year = Integer.parseInt(line.substring(57,59));
  //MBDate date;
  
  if (monthString.equals("JAN")) month = 0;
  else if (monthString.equals("FEB")) month = 1;
  else if (monthString.equals("MAR")) month = 2;
  else if (monthString.equals("APR")) month = 3;
  else if (monthString.equals("MAY")) month = 4;
  else if (monthString.equals("JUN")) month = 5;
  else if (monthString.equals("JUL")) month = 6;
  else if (monthString.equals("AUG")) month = 7;
  else if (monthString.equals("SEP")) month = 8;
  else if (monthString.equals("OCT")) month = 9;
  else if (monthString.equals("NOV")) month = 10;
  else if (monthString.equals("DEC")) month = 11;
  else throw new IOException("Invalid month: " + monthString 
			       + "(at line: " + lineNumber + ")");

  year += (year > 70 ? 1900 : 2000);
  }
  else
  {
	  System.out.println(" This is a non standard Header"); 
  
	  String classification = " "; 
  }
  //date = new MBDate(year, month, day);


/*
  if (entry != null) 
    throw new IOException("Header in the middle of a PDB entry"
			    + "(at line: " + lineNumber + ")");
  else 
    entry = new PDBEntry(IDcode, date, classification);
}
*/
}

public   void readCOMPOUND() throws IOException {
  if (line.length() >= 69) {
  String compound = line.substring(10, 69).trim();
  }
  else
  {
	  String compound = line.substring(10);
  }
   
  //entry.title.setCompoundName(compound);
}	

public   void readSEQRES() throws IOException {
  // lastRecordNumber = 0;
  String residue;
  String numResidues = line.substring(13, 17).trim();
  int chainID = Character.getNumericValue(line.charAt(11));
 // MBNode res;

  for (int ind = 19; ind < 68; ind = ind + 4) {
    residue = line.substring(ind, ind+3);
    if (residue.equals("   ")) break;
    else {
	//res = new PDBResidue(Registry.getLabelInt(residue));
	//entry.primary.addResidue(new Integer(chainID), res);
    }
  }
}


public  void readATOM () throws IOException {
    /*
	inStream = new StreamTokenizer(in);
    inStream.commentChar('#');
    inStream.eolIsSignificant(true);
    inStream.slashSlashComments(false);
	inStream.slashStarComments(false);
	inStream.ordinaryChar('/');
	*/
  System.out.println("readAtom: line: "+line);
  String atomName = line.substring(12,16).trim();
  // The PDB atom names need to be properly typed. Since no protein force field ready convert 
  //them to regular atoms for now 
  // Sudhakar 8 Jan 08
  System.out.println("Atom:"+atomName);
  atomName= atomName.substring(0,1);// Truncate to first character
  //if (atomName.equals("CA")|atomName.equals("CB")|atomName.equals("CG")|atomName.equals("CE")|
	   //atomName.equals("CD1")|atomName.equals("CD2")|atomName.equals("CE1")|atomName.equals("CE2")|
	   //atomName.equals("CZ")|atomName.equals("CE3")) atomName="C";
  //if (atomName.equals("NH1")|atomName.equals("NH2")|atomName.equals("NE")|
		  //atomName.equals("NZ")) atomName="N";
  //if (atomName.equals("OE1")|atomName.equals("OE2")|atomName.equals("OXT")|atomName.equals("NZ")) atomName="N";
  
  
  sym.addElement(atomName);
  System.out.println(" Atom:"+atomName);
  int chainID = Character.getNumericValue(line.charAt(21));
  System.out.println("Chain Id:"+chainID);
  String reNum = line.substring(22,26);
  System.out.println("reNum:"+reNum+"#");
  int resSeq;
  if (reNum.equals("    ")){resSeq = 0;}
  else{ resSeq = Integer.parseInt(reNum.trim());}
  System.out.println("Res Sequence ID:"+resSeq);
  
  Double xCoord = new Double(line.substring(30,38));
  Double yCoord = new Double(line.substring(38,46));
  Double zCoord;
  //Some times the string may not be 54 characters long but still may be a valid input
  //then try to the end of the string only!
  if (line.length() < 54){
	   zCoord = new Double(line.substring(46, line.length()));
  }
  else {
   zCoord = new Double(line.substring(46,54));}

  atomX.addElement(xCoord);
  atomY.addElement(yCoord);
  atomZ.addElement(zCoord);

  System.out.println(" Atom "+atomName + " X:"+ xCoord+" Y:"+yCoord+" Z:"+zCoord);
  // Remember that sequences in the PDB are 1 based!
  //entry.primary.addAtom(new Integer(chainID), resSeq - 1, atom);
  return;
}
public   void readBONDS(){
	inStream = new StreamTokenizer(in);
    inStream.commentChar('#');
    inStream.eolIsSignificant(true);
    inStream.slashSlashComments(false);
	inStream.slashStarComments(false);
	inStream.ordinaryChar('/');
	
	String [] bondTokens = new String[7];
    StringTokenizer ojt = new StringTokenizer(line," ");
    int i = 0;
	while (ojt.hasMoreTokens())
	{
	    bondTokens[i] = ojt.nextToken();
	    i++;
	}
	System.out.println("Number of tokens in this file: "+i);
	if ( i==1 ) {
		System.out.println("No Atoms given for Connectivity");
	}
	else if (i==2){
		firstAtom = Integer.valueOf(bondTokens[1].trim()).intValue();
		System.out.println("First Atom "+firstAtom);
	}
	else if (i==3){
		firstAtom = Integer.valueOf(bondTokens[1].trim()).intValue();
		System.out.println("First Atom "+firstAtom);
		
		currentAtom = Integer.valueOf(bondTokens[2].trim()).intValue();
		System.out.println("Second Atom "+currentAtom);
	    updatebondorder();
	}
	else if (i==4){
		firstAtom = Integer.valueOf(bondTokens[1].trim()).intValue();
		System.out.println("First Atom "+firstAtom);
		
		currentAtom = Integer.valueOf(bondTokens[2].trim()).intValue();
		System.out.println("Second Atom "+currentAtom);
	    updatebondorder();
	    
	    currentAtom = Integer.valueOf(bondTokens[3].trim()).intValue();
		System.out.println("Third Atom "+currentAtom);
	    updatebondorder();
	}
	else if (i==5){
		firstAtom = Integer.valueOf(bondTokens[1].trim()).intValue();
		System.out.println("First Atom "+firstAtom);
		
		currentAtom = Integer.valueOf(bondTokens[2].trim()).intValue();
		System.out.println("Second Atom "+currentAtom);
	    updatebondorder();
	    
	    currentAtom = Integer.valueOf(bondTokens[3].trim()).intValue();
		System.out.println("Third Atom "+currentAtom);
	    updatebondorder();
	    
	    currentAtom = Integer.valueOf(bondTokens[4].trim()).intValue();
		System.out.println("Fourth Atom "+currentAtom);
	    updatebondorder();
		
	}
	else if (i==6){
		firstAtom = Integer.valueOf(bondTokens[1].trim()).intValue();
		System.out.println("First Atom "+firstAtom);
		
		currentAtom = Integer.valueOf(bondTokens[2].trim()).intValue();
		System.out.println("Second Atom "+currentAtom);
	    updatebondorder();
	    
	    currentAtom = Integer.valueOf(bondTokens[3].trim()).intValue();
		System.out.println("Third Atom "+currentAtom);
	    updatebondorder();
	    
	    currentAtom = Integer.valueOf(bondTokens[4].trim()).intValue();
		System.out.println("Fourth Atom "+currentAtom);
	    updatebondorder();
		
	    currentAtom = Integer.valueOf(bondTokens[5].trim()).intValue();
		System.out.println("Fifth Atom "+currentAtom);
	    updatebondorder();
	}
	else if (i==7){
		firstAtom = Integer.valueOf(bondTokens[1].trim()).intValue();
		System.out.println("First Atom "+firstAtom);
		
		currentAtom = Integer.valueOf(bondTokens[2].trim()).intValue();
		System.out.println("Second Atom "+currentAtom);
	    updatebondorder();
	    
	    currentAtom = Integer.valueOf(bondTokens[3].trim()).intValue();
		System.out.println("Third Atom "+currentAtom);
	    updatebondorder();
	    
	    currentAtom = Integer.valueOf(bondTokens[4].trim()).intValue();
		System.out.println("Fourth Atom "+currentAtom);
	    updatebondorder();
		
	    currentAtom = Integer.valueOf(bondTokens[5].trim()).intValue();
		System.out.println("Fifth Atom "+currentAtom);
	    updatebondorder();
	    
	    currentAtom = Integer.valueOf(bondTokens[6].trim()).intValue();
		System.out.println("Sixth Atom "+currentAtom);
	    updatebondorder();
	}
/*	
	if (line.length() >=11 ) {
	if ( line.substring(6,11).equals("") ) {
		System.out.println("No Atoms given for Connectivity");
	}
	else{
		firstAtom = Integer.valueOf(line.substring(6,11).trim()).intValue();
		System.out.println("First Atom "+firstAtom);
	}
	if (line.length() < 16) {
		int lci = line.length();
		currentAtom = Integer.valueOf(line.substring(11,lci).trim()).intValue();
		System.out.println("Second Atom "+currentAtom);
	    updatebondorder();
	}
	else{
	if (line.substring(11,16).equals("") ) {
		System.out.println("No Atoms given for Connectivity");
	}
	else{
		currentAtom = Integer.valueOf(line.substring(11,16).trim()).intValue();
		System.out.println("Second Atom "+currentAtom);
	    updatebondorder();
	}
	}
  }
	if ( line.length()>=21) {
		if (line.substring(16,21).equals("") ) {
		System.out.println("No Atoms given for Connectivity");
	}
	else{
		currentAtom = Integer.valueOf(line.substring(16,21).trim()).intValue();
		System.out.println("Third Atom "+currentAtom);
	    updatebondorder();
	}
  }
	if (line.length()>=26) {
		if (line.substring(21,26).equals("")  ){
		System.out.println("No Atoms given for Connectivity");
	}
	else{
		currentAtom = Integer.valueOf(line.substring(21,26).trim()).intValue();
		System.out.println("Fourth Atom "+currentAtom);
	    updatebondorder();
    }
  }
	if (line.length() >= 31) {
		if (line.substring(26,31).equals("") )  {
		System.out.println("No Atoms given for Connectivity");
	}
	else{	
		currentAtom = Integer.valueOf(line.substring(26,31).trim()).intValue();
		System.out.println("Fifth Atom "+currentAtom);
		updatebondorder();
	}
		}
		*/
}	
	   public void updatebondorder(){
		//firstAtom = (int)line.substring(6,11);
	    System.out.println("Bond:" + firstAtom+ " Totals BondedAtoms(1) "+bAtom1.size());
	    boolean bondExists = false;
          
	    for (int i = 0; i < bAtom1.size(); i++)
	    {
		   if ( ((Double)bAtom1.elementAt(i)).intValue() == new Double(firstAtom).intValue()    && 
		     ((Double)bAtom2.elementAt(i)).doubleValue() == new Double(currentAtom).intValue() )
	       {
			   int order = ((Double)bOrder.elementAt(i)).intValue();
		    order++;
		    bOrder.setElementAt(new Double(order), i);
		    bondExists = true;
		    System.out.println("upped " + firstAtom + " " + currentAtom + " order " + order); 
	        }		     
		}   
	    System.out.println("readBonds: Bonds"+bondExists+" firstAtom "+firstAtom+" CurrentAtom "+currentAtom);     
	    if (bondExists == false && firstAtom < currentAtom)
	    {
		       bAtom1.addElement( new Double(firstAtom));
		       bAtom2.addElement( new Double(currentAtom));
		       bOrder.addElement( new Double(1) );
		       System.out.println("added " + firstAtom + " " + currentAtom + " order 1"); 
	    }
	        
	    }

public void process() throws IOException
{
 
     	    
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
