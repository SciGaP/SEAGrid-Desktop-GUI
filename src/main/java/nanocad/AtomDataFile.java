package nanocad;

import java.io.*;
import java.util.*;


public class AtomDataFile
{
    private textwin debugWin;
    String s = new String("");
    boolean closed, dataFound;
    BufferedReader in;
    
    //Ying 04/03
    //data member;
    public static int atomicNum =0;
    public static double mass = 0;
    public static int color1 = 255, color2 =255, color3 = 255;
    public static  double covalentRadius = 0 , vdwEnergy =0, vdwRadius =0;
    public static int correctNumBonds =0;
    public static String symbol = "H", name = "Hydrogen";
    
    public AtomDataFile()
    {
	closed = true;
    }
    
    public AtomDataFile(String fname) throws IOException
    {
	open(fname);
    }
    
    public void close() throws IOException
    {
	if(!closed) {
	    in.close();
	    closed = true;
	}
    }
    
    public boolean findData(int a1, int offset1) throws IOException
    //reurns the valNumth value for the given atoms
    {
	String dataLine;
	dataFound =  readTo(a1, offset1);
	return dataFound;
    }
    
    public boolean findData(int a1, int a2, int offset1, int offset2) throws IOException
    //reurns the valNumth value for the given atom pair
    {
	if(readTo(a1, offset1))    
	     dataFound = readTo(a2, offset2);
	else
	    dataFound = false;
	return dataFound;
	}
    
    
    public boolean findData(int a1, int a2, int a3, int offset1, int offset2, int offset3) throws IOException
    //reurns the valNumth value for the given atom triplet
    {
	dataFound=false;
	if(readTo(a1, offset1))
	    if(readTo(a2, offset2))
		dataFound = readTo(a3, offset3);
	return dataFound;
    }
    
    public boolean findData(int a1, int a2, int a3, int a4, int offset1, int offset2, int offset3, int offset4) throws IOException
    //reurns the valNumth value for the given atom quadruplet
    {
	dataFound=false;
	if(readTo(a1, offset1))
	    if(readTo(a2, offset2))
		if(readTo(a3, offset3))
		    dataFound = readTo(a4, offset4);
	return dataFound;
    }
    
    public boolean findData(String symbol, int offset) throws IOException
    {
	return readTo(symbol, offset);
    }

    //04/02 Ying prelude to compare the symbol;
    
    public String contains(String tar){
	//System.out.println("in contains 0, the tar is :"+tar);
	//String tmp =null;

	//String modarate = "110 6 9 Ununnilium Uun 269 204 0 204 1.00 0.00 0.00";
		
	String symWord = null;
	Vector parsed = new Vector();
	char[] tmp = null;
	int length=0;
	length = tar.length();
	tmp= tar.toCharArray();
	char[] dest = new char[length+1];
	for (int i=0; i<length; i++){
	    dest[i] =tmp[i];
	}
	dest[length]= '\0';
	
	int start =0, end =0;
	String tem ="";
	
	for (int i=0; i<dest.length; i++){
	    int wordLength =0;
	    if (dest[i] == ' ' || dest[i] == '\0'){
		wordLength = start-end;
		tem= tar.substring(end,start);
		//System.out.println(tem);
		parsed.addElement(tem);
		end = end+wordLength +1;
	    }
	    start++;
	}
	
	symWord =(String)parsed.elementAt(4);
	return symWord;
    }
    
    
     public void parse(String line){
	 
	 Vector parsed = new Vector();
	 char[] tmp = null;
	 int length=0;
	 length = line.length();
	 tmp= line.toCharArray();
	 char[] dest = new char[length+1];
	 for (int i=0; i<length; i++){
	     dest[i] =tmp[i];
	 }
	 dest[length]= '\0';
	 
	 int start =0, end =0;
	 String tem ="";

	 for (int i=0; i<dest.length; i++){
	     int wordLength =0;
	     if (dest[i] == ' ' || dest[i] == '\0'){
		 wordLength = start-end;
		 tem= line.substring(end,start);
		 //System.out.println(tem);
		 parsed.addElement(tem);
		 end = end+wordLength +1;
	     }
	     start++;
	 }
	 
	atomicNum = (Integer.valueOf((String)parsed.elementAt(0))).intValue();
	mass = (Double.valueOf((String)parsed.elementAt(5))).doubleValue();
	color1 = (Integer.valueOf((String)parsed.elementAt(6))).intValue();
	color2 = (Integer.valueOf((String)parsed.elementAt(7))).intValue();
	color3 = (Integer.valueOf((String)parsed.elementAt(8))).intValue();
	
	covalentRadius = (Double.valueOf((String)parsed.elementAt(9))).doubleValue();
	
	vdwEnergy = (Double.valueOf((String)parsed.elementAt(10))).doubleValue();
	vdwRadius = (Double.valueOf((String)parsed.elementAt(11))).doubleValue();
        correctNumBonds = (Integer.valueOf((String)parsed.elementAt(12))).intValue();
	symbol =(String) (parsed.elementAt(4));
	name =(String)(parsed.elementAt(3));
     }
    
    //04/03 Ying
    public String dealSym(String sym){
	//remove the end;
	String realSym = null;
	int length = sym.length();
	
        int thereWasaDigit =0;
	for (int i=0; i<length; i++){
	//    System.out.println("sym at " + i+ "now\n");
	    if (Character.isDigit(sym.charAt(i))==true) {
		realSym = sym.substring(0, i);
                thereWasaDigit = 1;
		break;
	    }else 
		continue;
	}
        //System.out.println("Symbol NOW is : " + realSym);
	
        //System.out.println("!!!!!REALSYM = " + realSym);
	if (realSym == null) 
	    realSym = sym;
//	System.out.println("the realSym at dealSym is***"+realSym+"***" +"and the length is "+realSym.length());
	return realSym;
    }
    
    //03/31/ Ying
    public boolean determineData(String symbol)
    {
	
	String testStr;
	dataFound = false;
	String target=null, hy = null;
	String realSym;
	realSym =dealSym(symbol);


	//debug 1, correct
	//debugWin = new textwin("the determineData 1 at AtomDataFile","", false);
	//debugWin.setVisible(true);
	//debugWin.setText("the symbol to be tested is :"+symbol+"and the real symbol is :"+realSym);
	
	//System.out.println("the symbol to be tested after remove is***"+realSym+"***"+"and the length is :"+realSym.length());

	//Ying, try hydrogen first
	
	hy = contains(s);
	
	if (hy.equals(realSym)){
	    dataFound = true;
	    //System.out.println("hydrogen is found\n");
	    
	    if (dataFound == true){
		parse(s);
	    }
    
	}else {
	    try{

		while(in.ready()){
		    testStr = in.readLine();
		    target = contains(testStr);
		    
		    //System.out.println("target is :***"+target+"***\n");
		    //if (target.equals("Cu")) {
		    //if (target.equals("C")){
		    if (target.equals(realSym)){  
			dataFound = true;
			
			//System.out.println("********target ="+target+"and realSym ="+realSym+"**********");
		    }
		    
		    if (dataFound == true) {
			parse(testStr);
			//debug, correct ,too
			/*debugWin = new textwin("the determineData 3 at AtomDataFile","", false);
			  debugWin.setVisible(true);
			  debugWin.setText("data are : \n");
			  debugWin.setText("atomNum = "+atomicNum+"\n"+"mass = "+mass+"\n"+"correctNumBonds = "+correctNumBonds+"\n"+"name = "+name+"\n");
			*/
			
			break;
		    }
		}
		
	    }catch (IOException e){
		System.err.println(e);
		return false;
	    }
	    //dataFound = true;
	}
	//if (dataFound==true)
	    //System.out.println("dataFound is true\n");
	return dataFound;
    }
    
    
    public void open(String fname) throws IOException
    {
	InputStream inputStream = new FileInputStream(new File(fname));
	//InputStream inputStream = getClass().getResourceAsStream("/"+fname);
    in = new BufferedReader(new InputStreamReader(inputStream));
	closed = false;
	s = in.readLine();
    }
    
    public double parseDouble(int k)
    //returns the kth float in the string, seperated by spaces
    {
	int head=0;
	s = s + " "; //insures trailing space for easier computation
	int tail = s.indexOf(" ");
	for(int i = 0; i < k; i++)
	    {
		head = tail+1;
		tail = s.indexOf(" ",head);
	    }
	
	return (Double.valueOf(s.substring(head,tail))).doubleValue();
    }
    
    public int parseInt(int k)
    //returns the kth float in the string, seperated by spaces
    {
	int head=0;
	s = s + " "; //insures trailing space for easier computation
	int tail = s.indexOf(" ");
	for(int i = 0; i < k; i++)
	{
	    head = tail+1;
	    tail = s.indexOf(" ",head);
	}
  	//System.out.print("parsing " + s +"(" + k + ") = ");
	int q = (Integer.valueOf(s.substring(head,tail))).intValue();
	//System.out.println(q);
	return q;
    }

    public String parseString(int k)
    //returns the kth float in the string, seperated by spaces
    {
	int head=0;
	s = s + " "; //insures trailing space for easier computation
	int tail = s.indexOf(" ");
	for(int i = 0; i < k; i++)
	{
	    head = tail+1;
	    tail = s.indexOf(" ",head);
	}
  
		return s.substring(head,tail);
	}

    protected boolean readTo(int val, int k)
    //returns once a value of val in kth line position is found
    //assumes file is in increasing order
    //returns true if found, false if not found
    {
	try
	{
	
	    int current = parseInt(k);

	    while(in.ready() && (current < val))
	    {   
		s = in.readLine();
		//System.out.println("s equals to "+ s);
		current = parseInt(k);
		
	    }
	    if(current == val)
	    {
		return true;
	    }

	    return false;
	}	    
	catch (IOException e)
	{
	    e.printStackTrace();
		return false;
	}
	
    }

    protected boolean readTo (String symbol, int k)
    {
    try
    {
	String temp;
	while(in.ready())
	{
	    temp = parseString(k);
	    if (temp.equals(symbol))
		return true;
	    s = in.readLine(); // this statement goes here because open() reads the first line
	}
	return false;
    }
    catch (IOException e)
    {
	e.printStackTrace();
	return false;
    }
    }

    public String getLine()
    {
	return s;
    }

/**
 * Insert the method's description here.
 * Creation date: (6/19/00 10:55:59 AM)
 * @param numberOfLinesToSkip int
 */

    public void skipLines(int numberOfLinesToSkip) throws IOException {
	for (int i = 0; i < numberOfLinesToSkip; i++)
	    if (in.ready())
			s = in.readLine();
	    else
			return;
    }
    
    /*    
      public static void main(String [] agrv){
      try{
      AtomDataFile dFile = new AtomDataFile("pdata.txt");
      // boolean tet =dFile.findData(3, 4, 1, 2);
      
      if(dFile.determineData("Ag")==true){
      //if(dFile.determineData("Cu")==true){
				//should go the corresponding line,
	  System.out.println("the selected atom's name is : "+ name +"\n");
	  System.out.println("the selected AtomicNumber is : "+atomicNum+"\n");
	  System.out.println("the mass is :"+mass+"\n" +"the vdwEnergy is:"+vdwEnergy +" the vdwRadius is :"+vdwRadius+" the correctBonds" + correctNumBonds+" teh convalentRadius is :"+ covalentRadius + "color are "+ color1 + " " +color2+" "+color3+" ");
	  
      }
      }
      
      catch(IOException ex)
	  {ex.printStackTrace();}
      }
    */

}
