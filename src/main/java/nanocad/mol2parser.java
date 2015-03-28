///////////////////////////////////////////////////////
// mol2parser.java
// June 2006 by Sudhakar Pamidighantam
// A parser for Mol2 Files.  Extends Parser.java
///////////////////////////////////////////////////////

package nanocad;

import java.lang.System;
import java.io.StreamTokenizer;                                                                                                               
import java.io.*;     
import java.util.*;    

public class mol2parser extends Parser{
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
boolean AtomSection = false;
boolean BondSection = false;
boolean SubStSection = false;
boolean MolSection = false;
boolean readingBond = false;
boolean newLine = true;
boolean noone = false;
boolean debug = true;
int Mollinn; int Atomlinn; int Bondlinn;int SubStlinn;
int atomVSize; int bondVSize; int substVSize;
		

public mol2parser(String filename, String data){
	 
        super(data);


      //entry = null;		// Entry being parsed
	  done = false;
	  needLine = true;
      String inFile=filename;
      try {
      
	  input = new LineNumberReader(new FileReader(inFile));
	  done = false;

	  for ( ; done == false; ) {
	    if (needLine) 
	    {
	      line = input.readLine();
	    } else {
	      needLine = true;
	    }
	    
	    lineNumber = input.getLineNumber();
	    //System.out.println("Getting Line number "+lineNumber+" in Structure file ");
	    if (line != null) {System.out.println(" Current line is \n"+line);
	    if (line.trim().equals("@<TRIPOS>MOLECULE"))
	    { MolSection = true; Mollinn = lineNumber;
	      System.out.println(" To Read Molecular Information at line number "+Mollinn);
	    }
	    else if (line.trim().equals("@<TRIPOS>ATOM"))
	    { MolSection= false; AtomSection = true; 
	      Atomlinn=lineNumber;
	      System.out.println("to Read Atom Information from line number "+lineNumber);
	    }
	    else if (line.trim().equals("@<TRIPOS>BOND")) 
	    { AtomSection=false; BondSection = true; 
	       Bondlinn=lineNumber;
	    }
	    else if (line.trim().equals("@<TRIPOS>SUBSTRUCTURE"))
	    {
	    	BondSection = false; 
	    	SubStSection = true; 
	    	SubStlinn = lineNumber;
	    	System.out.println("to Read Substructure Information from line number "+lineNumber);
	    }
	    //}
	    if (MolSection) {
	    	if (lineNumber > Mollinn){ System.out.println("calling readMolInfo ");readMolInfo();	}
	    }
	    if (AtomSection){
	    	if (line.equals("")){
	    	  //Ignore this line
              Atomlinn++;
              //System.out.println(" Blank Encountered. Starting Atom line number is incremented");
	    	}
	    	else{
	    	if (lineNumber > Atomlinn && lineNumber <= Atomlinn+atomVSize ) readATOM();}
	    }
	    if (BondSection){
	    	if (line.equals("")){
		    	  //Ignore this line
	              Bondlinn++;
	              //System.out.println(" Blank Encountered. Starting Atom line number is incremented");
		    	}
	    	else{
	    	if (lineNumber > Bondlinn && lineNumber <= Bondlinn+bondVSize) readBONDS();}
	    }
	    if (SubStSection){
	    	if (line.equals("")){
		    	  //Ignore this line
	              SubStlinn++;
	              //System.out.println(" Blank Encountered. Starting Atom line number is incremented");
		    	}else{
	    	if (lineNumber > SubStlinn && lineNumber <= SubStlinn+substVSize){
	    	readSubStruct();
	    	//line="";
	    	done=true;
	    	}}
	    }
	    
	  }
	    //return;
	    //lineDispatch(line);
	   }
      } catch (Exception fnf){System.out.println("mol2parser:"+fnf);}
  
}
      public void readSubStruct (){
    	  // Read Substructure Information
    	  StringTokenizer st = new StringTokenizer (line);
    	  if (st.countTokens() >= 1){
          int subsid = (new Integer (st.nextToken())).intValue();
    	  String subs = st.nextToken();
    	  int rootatom = (new Integer (st.nextToken())).intValue();
    	  System.out.println("SubStructure:"+ subs);
    	  }
      }
    	  
      public void readMolInfo (){
    	if (lineNumber == Mollinn+1) {
    		// Read Molecule Name
    		String compound = line;
    		System.out.println("Compound is"+compound);
    	}
    	else if (lineNumber == Mollinn+2){
    		//read Atom, Bond and SubStructure record sizes
    		//byte[] moldata = new byte[400];
    		//moldata = line.getBytes();
    		StringTokenizer st = new StringTokenizer(line);
    		int colcounter=0;
    		//while (st.hasMoreTokens()) {
    		 //int token = (new Integer (st.nextToken())).intValue();
    		do {
    		 colcounter++;
    		 //switch (token){
    		 //case StringTokenizer.TT_EOL :
    			//eof = true;
    			//break;
    		 //case StringTokenizer.TT_NUMBER :
    			if (st.countTokens() >= 1)
    			{  
    			  atomVSize = (new Integer(st.nextToken())).intValue();
    			  System.out.println("MolInfo: " + atomVSize);
    			}
    			//else if (colcounter == 2){ 
    				bondVSize = (new Integer(st.nextToken())).intValue();
    				System.out.println("MolInfo: " + bondVSize);
    			//}else if (colcounter == 3) { 
    		    		substVSize = (new Integer(st.nextToken())).intValue();
    		    	System.out.println("MolInfo: " + substVSize);
    		    	//}
    			System.out.println("MolInfo: " + atomVSize + bondVSize + substVSize);
    		 }while (st.countTokens() == 1); 
    		 
    		//}
    		//System.out.println("MolInfo: " + atomVSize + bondVSize + substVSize);
    		
    		//System.exit(-1);
    	  }
    	//System.out.println("MolInfo: " + atomVSize + bondVSize + substVSize);
    	// read other molecule records
      //}
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
/*
public   void lineDispatch (String line) throws IOException {
  
  if (line.length() >=6) {System.out.println(" Line starts with "+ line.substring(0,6));}
  if (line.startsWith("@<TRIPOS>MOLECULE")) readHEADER();
  else if (line.startsWith("@<TRIPOS>ATOM")) readATOM() ;
  else if (line.startsWith("@<TRIPOS>BOND")) readBONDS();
  else if (line.startsWith("@<TRIPOS>SUBSTRUCTURE")) reaSubStructure();
  else if (line.startsWith("ATOM  ")) {System.out.println(" Will read Atom info"); readATOM();return;}
  else if (line.startsWith("HETATM")) {System.out.println(" Will read HeteroAtom info");readATOM();return;}
  if (line.startsWith("CONNECT")) {System.out.println(" Will read Bond info");readBONDS();return;}
  else if (line.startsWith("CONECT"))  {System.out.println(" Will read Bond info");readBONDS();return;}
  else if (line.startsWith("MASTER")) { eof= true; done = true;}
  else if (line.startsWith("END   ")) { eof= true;   done = true;  }
  else if (line.startsWith("")){ eof= true;  done = true; }
  else System.out.println("Unknown record type (" + line.substring(0,6) 
			    + ") in PDB entry at line " 
			    + lineNumber);
}

*/
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
	String atomName=""; 
	double xCoord=0D;
	double yCoord=0D;
	double zCoord=0D;
  System.out.println("readAtom: line: "+line);
	//Byte[] atomdata = new Byte[400];
	//atomdata = line.getBytes();
	StringTokenizer st = new StringTokenizer(line);
	int colcounter=0;
	do {
	//int token = (new Integer (st.nextToken())).intValue();
	colcounter++;
	//switch (token){
	 //case StreamTokenizer.TT_EOF :
	 //	eof = true;
		//break;
	 //case StreamTokenizer.TT_NUMBER :
	    System.out.println(" Column Counter:"+colcounter);
		if (st.countTokens() >= 1) {int atomidn = (new Integer (st.nextToken())).intValue();
		System.out.println("AtomID:"+atomidn);
		}
		//else if (colcounter == 2) {
		    atomName = st.nextToken().trim();
			sym.addElement(atomName);
			  System.out.println(" Atom:"+atomName);
		//}
		//else if (colcounter ==  3){ 
		    xCoord = (new Double(st.nextToken())).doubleValue();
			atomX.addElement(new Double (xCoord));
	    //}else if (colcounter == 4) {
	    	yCoord = (new Double(st.nextToken())).doubleValue();
			atomY.addElement(new Double (yCoord));
	    //}else if (colcounter == 5) {
	        zCoord =  (new Double(st.nextToken())).doubleValue();
			atomZ.addElement(new Double (zCoord));
	    //} 		
		System.out.println(" Atom: "+atomName + ": X:"+ xCoord+" Y:"+yCoord+" Z:"+zCoord);
	 //}
	}while (st.countTokens() == 1);
  
	/*
  String atomName = line.substring(12,16).trim();
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
  Double zCoord = new Double(line.substring(46,54));

  atomX.addElement(xCoord);
  atomY.addElement(yCoord);
  atomZ.addElement(zCoord);
  System.out.println(" Atom "+atomName + " X:"+ xCoord+" Y:"+yCoord+" Z:"+zCoord);
  */
  // Remember that sequences in the PDB are 1 based!
  //entry.primary.addAtom(new Integer(chainID), resSeq - 1, atom);
  //return;
}
	
public void readBONDS(){
	
	 System.out.println("readBONDS: line: "+line);
		//Byte[] bonddata = new Byte[400];
		//bonddata = line.getBytes();
		StringTokenizer st = new StringTokenizer(line);
		int colcounter=0;
		do {
		//int token = (new Integer(st.nextToken())).intValue();
		colcounter++;
		//switch (token){
		// case StreamTokenizer.TT_EOF :
		//	eof = true;
		//	break;
		 //case StreamTokenizer.TT_NUMBER :
			if (st.countTokens() >= 1) {int bondidn = (new Integer(st.nextToken())).intValue() ;
			System.out.println(" Bond ID:"+bondidn);
			}
			//else if (colcounter==2) {
				double firstAtom = (new Double(st.nextToken())).doubleValue();
				bAtom1.addElement(new Double (firstAtom));
			//}else if (colcounter==3){ 
				double secondAtom = (new Double(st.nextToken())).doubleValue();
				bAtom2.addElement(new Double (secondAtom));
		    //}else if (colcounter==4) {
		    	 double b_order = (new Double(st.nextToken())).doubleValue();
		    	 bOrder.addElement(new Double (b_order));
		    	 System.out.println("Bond: Atom1 "+firstAtom+" Atom2:"+secondAtom+" Order:"+b_order);
		    //}else if (colcounter==4) {
		    	 if (st.countTokens() >= 1){
		    	 String b_status = st.nextToken();
		    	 }
		    //}
		//}
		
		}while (st.countTokens() == 1 );
	  
	/*
	inStream = new StreamTokenizer(in);
    inStream.commentChar('#');
    inStream.eolIsSignificant(true);
    inStream.slashSlashComments(false);
	inStream.slashStarComments(false);
	inStream.ordinaryChar('/');
	
	if (line.length() >=16 ) {
	if ( line.substring(6,11).equals("") ) {
		System.out.println("No Atoms given for Connectivity");
	}
	else{
		firstAtom = Integer.valueOf(line.substring(6,11).trim()).intValue();
		System.out.println("First Atom "+firstAtom);
	}
	if (line.substring(11,16).equals("") ) {
		System.out.println("No Atoms given for Connectivity");
	}
	else{
		currentAtom = Integer.valueOf(line.substring(11,16).trim()).intValue();
		System.out.println("Second Atom "+currentAtom);
	    updatebondorder();
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
		//updatebondorder();
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

if(debug == true)     
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
