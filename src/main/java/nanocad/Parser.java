package nanocad;

import java.lang.System;
import java.io.StreamTokenizer;
import java.io.*;
import java.util.*;

public class Parser {

    StringWriter out = new StringWriter();
    boolean debug = false;
    StringReader in;

    //the numbers of words for the molecules. i.e, carbon dioxide is 2;
    int mol_name_num = 0; 
    //begin to count length of molecules
    boolean mol_name_flag = false; 
    
    boolean eof = false;

    //coordinates of atoms
    Vector atomX = new Vector();
    Vector atomY = new Vector();
    Vector atomZ = new Vector();
    Vector sym = new Vector();

    //bonds's atoms and order
    Vector bAtom1 = new Vector();
    Vector bAtom2= new Vector();
    Vector bOrder = new Vector();
    
    boolean begin_atom = false, begin_bond= false;
    int tmp, col_num_a =0, col_word=0, col_num_b = 0;
    StreamTokenizer inStream;

    /**
     *constructor
     */
    public Parser(String data){
	for (int i =0; i<data.length(); i++){
	    out.write(data.charAt(i));
	}

	in = new StringReader(out.toString()); 
	
	inStream = new StreamTokenizer(in);
	inStream.commentChar('#');
    }
    
    /**
     *process the stream token and words and symbols
     */
    public void process() throws IOException{
	do {
	    int token = inStream.nextToken();
	    switch(token){
	    case StreamTokenizer.TT_EOF :
		eof = true;
		break;

	    case StreamTokenizer.TT_EOL :
		break;
	
	    case StreamTokenizer.TT_WORD :
		if (debug == true)
		    System.out.println("Word :" + inStream.sval);
		if(inStream.sval.equals("MOLECULE")){
		    mol_name_num = 0;
		    mol_name_flag = true;
		   }
		else if(inStream.sval.equals("ATOM")){
		    begin_atom = true;
		    
		    col_word=0;
		    col_num_a =0;
		    
		}else if(inStream.sval.equals("BOND")){
		    begin_atom = false;
		    begin_bond = true;
		    col_num_b =0;
		    col_num_a =0;
		    col_word =0;
		}else if (inStream.sval.equals("SUBSTRUCTURE")){
		    begin_atom=false;
		    begin_bond = false;
		    col_num_b =0;
		    col_num_a =0;
		    col_word =0;
		}

		if(inStream.sval.equals("ATOM"))
		    col_word = 0;
		else {
		    col_word++;
		    if (debug == true)
		    	System.out.println("now the col_word is :"+col_word +"and the word is:"+inStream.sval+"\n");
		}

		if (mol_name_flag == true){
		   
		    mol_name_num++;
		}else if (begin_atom == true && col_word % (2+mol_name_num) == 1){
		    sym.addElement(inStream.sval);
		}
		
		break;

	    case StreamTokenizer.TT_NUMBER : 
		if (debug == true)
		    System.out.println("Number :" + inStream.nval);
		if(mol_name_flag == true){
		    mol_name_flag = false;
		    if(debug == true)
			System.out.println("the mol is :"+(mol_name_num-1) +" long\n");
		    mol_name_num = mol_name_num-1; //exclude the word "MOLECULE";

		}else if(begin_atom == true){
		    if(col_num_a % 6 ==1){
			atomX.addElement(new Double(inStream.nval));
		    }else if (col_num_a % 6 ==2){
			atomY.addElement(new Double(inStream.nval));
		    }else if (col_num_a % 6 ==3){
			atomZ.addElement(new Double(inStream.nval));
		    }
		}else if(begin_bond == true){
		    if(col_num_b % 4 ==1){
			 bAtom1.addElement(new Double(inStream.nval));
		     }else if (col_num_b % 4 ==2){
			 bAtom2.addElement(new Double(inStream.nval));
		     }else if(col_num_b % 4 ==3){
			 bOrder.addElement(new Double(inStream.nval));
		     }
		}

		col_num_a++;
		col_num_b++;
		    
		break;

	    default:
		if(debug == true)
		    System.out.println((char) token +"encountered \n");
		if (token == '!') eof = true;
	    }
	}while(!eof);
		
        sym.removeElementAt(sym.size()-1);
		
	if(debug == true)
	{
	for (int i=0; i< sym.size(); i++){
	    System.out.println("i is :"+i+"\n");
	    System.out.println("Sym contains : "+ sym.elementAt(i));
	    System.out.println(atomX.size() + " " + atomY.size() + " " + atomZ.size());
	    System.out.println("atomX : "+ atomX.elementAt(i));
	    System.out.println("atomY : "+ atomY.elementAt(i));
	    System.out.println("atomZ : "+ atomZ.elementAt(i));
	    System.out.println("\n");
	}

	for (int i=0; i<bAtom1.size(); i++){
	    System.out.println("bAtom1 : "+ bAtom1.elementAt(i));
	    System.out.println("bAtom2 : "+ bAtom2.elementAt(i));
	    System.out.println("bOrder : "+ bOrder.elementAt(i));
	    System.out.println("\n");
	}
	}
    }
 
    /**
     *read file
     */
    public static String readFile(String name){
	String s =null;
	
	try{
	    File file = new File(name);
	    char[] arr = new char[(int)file.length()];
	    FileReader in = new FileReader(file);
	    in.read(arr, 0, (int)file.length());
	    s = new String(arr);
	    
	}catch(IOException ex){
	    System.out.println(ex.toString());
	}
	return s;
    }
  
}





