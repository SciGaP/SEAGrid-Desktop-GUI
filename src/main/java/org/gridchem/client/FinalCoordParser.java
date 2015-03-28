package org.gridchem.client;

import java_cup.runtime.*;
import javax.swing.*;
import org.gridchem.client.*;
import org.gridchem.client.common.Settings;
import org.gridchem.client.util.*;
import java.util.*;
import java.io.*;
import java.text.*;
import java.lang.*;

/** CUP v0.10j generated parser.
  * @version Fri Nov 01 14:36:12 CST 2002
  */
public class FinalCoordParser extends java_cup.runtime.lr_parser {
  
  /** Default constructor. */
  public FinalCoordParser() {super();}

  /** Constructor which sets the default scanner. */
  public FinalCoordParser(java_cup.runtime.Scanner s) {super(s);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\016\000\002\003\005\000\002\002\004\000\002\004" +
    "\003\000\002\005\004\000\002\005\003\000\002\006\006" +
    "\000\002\017\004\000\002\017\003\000\002\020\010\000" +
    "\002\012\003\000\002\013\003\000\002\014\003\000\002" +
    "\015\003\000\002\016\003" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\032\000\004\005\005\001\002\000\004\004\010\001" +
    "\002\000\004\004\uffff\001\002\000\004\002\007\001\002" +
    "\000\004\002\000\001\002\000\004\007\015\001\002\000" +
    "\006\004\010\006\013\001\002\000\006\004\ufffd\006\ufffd" +
    "\001\002\000\004\002\001\001\002\000\006\004\ufffe\006" +
    "\ufffe\001\002\000\004\011\016\001\002\000\004\012\ufff8" +
    "\001\002\000\004\012\025\001\002\000\006\010\ufffa\011" +
    "\ufffa\001\002\000\006\010\022\011\016\001\002\000\006" +
    "\004\ufffc\006\ufffc\001\002\000\006\010\ufffb\011\ufffb\001" +
    "\002\000\004\013\026\001\002\000\004\013\ufff7\001\002" +
    "\000\004\016\027\001\002\000\004\017\ufff6\001\002\000" +
    "\004\017\031\001\002\000\004\020\ufff5\001\002\000\004" +
    "\020\033\001\002\000\006\010\ufff4\011\ufff4\001\002\000" +
    "\006\010\ufff9\011\ufff9\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\032\000\006\003\005\004\003\001\001\000\006\005" +
    "\010\006\011\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\004\006\013\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\010\012\016\017\020\020\017\001\001\000\002\001" +
    "\001\000\004\013\023\001\001\000\002\001\001\000\006" +
    "\012\016\020\022\001\001\000\002\001\001\000\002\001" +
    "\001\000\002\001\001\000\002\001\001\000\004\014\027" +
    "\001\001\000\002\001\001\000\004\015\031\001\001\000" +
    "\002\001\001\000\004\016\033\001\001\000\002\001\001" +
    "\000\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$FinalCoordParser$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions() throws IOException
    {
      action_obj = new CUP$FinalCoordParser$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$FinalCoordParser$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}

}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$FinalCoordParser$actions {
	public static double multiple = 1.0000;  //multiple=0.52918 if units are in bohr's

 
  //__________________________________
  public static boolean DEBUG = true;
  public ParseGSCF2a temp;
  public ParseGSCF2a temp1; 
  private static JTable table;               
  private static final String tableLabel = "SCF Intermediate Results:";
// private static String cycle = "0";
 
  
  public static JTable getTable() {
    return table;
  }

  public static String getTableLabel() {
    return tableLabel;
  }

//   }

  private final FinalCoordParser parser;

  /** Constructor */
  CUP$FinalCoordParser$actions(FinalCoordParser parser) throws IOException {
    this.parser = parser;
    //temp = new ParseGSCF2a(Settings.defaultDirStr + 
    		temp = new ParseGSCF2a(Env.getApplicationDataDir() +
     		Settings.fileSeparator + "final.pdb");
    //temp1 = new ParseGSCF2a(Settings.defaultDirStr + 
    		temp1 = new ParseGSCF2a(Env.getApplicationDataDir() + 
	       		Settings.fileSeparator + "connect.pdb");
    temp1.putField("a");
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$FinalCoordParser$do_action(
    int                        CUP$FinalCoordParser$act_num,
    java_cup.runtime.lr_parser CUP$FinalCoordParser$parser,
    java.util.Stack            CUP$FinalCoordParser$stack,
    int                        CUP$FinalCoordParser$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$FinalCoordParser$result;

      /* select the action based on the action number */
      switch (CUP$FinalCoordParser$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 13: // inp7 ::= INPUT7 
            {
              Object RESULT = null;
		int in7left = ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left;
		int in7right = ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right;
		Float in7 = (Float)((java_cup.runtime.Symbol) CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).value;
		 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:Input:  z coordinate "+in7);
             float zcoor = in7.floatValue();
      
      double zca = multiple*zcoor;
      double zc = zca*1000;
      float zc1 = Math.round(zc);
      float zcoor1 = zc1/1000;
      
      NumberFormat df=NumberFormat.getNumberInstance();
      df.setMaximumFractionDigits(3);
      String ztext=df.format(zca);
      /*
      String ztextSub= ztext.substring(ztext.indexOf("."));
      if (ztextSub.length()==3){
    	  ztext=ztext+"0";
      }else if (ztextSub.length()==2)
      {
    	  ztext=ztext+"00";
      }
      */
      if (zcoor1 >= 0.0) {temp.putField(" ");} 
      if (zcoor1 == 0.0) {temp.putField("0.000");
                          temp1.putField("0.000");}
      else {temp.putField(ztext);
            temp1.putField(ztext);}
      temp.putField("\n");
      temp1.putField("\n");

              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(12/*inp7*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // inp6 ::= INPUT6 
            {
              Object RESULT = null;
		int in6left = ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left;
		int in6right = ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right;
		Float in6 = (Float)((java_cup.runtime.Symbol) CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).value;
		 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:Input:  y coordinate "+in6);
            float ycoor = in6.floatValue();
      
      double yca = multiple*ycoor;
      double yc = yca*1000;
      float yc1 = Math.round(yc);
      float ycoor1 = yc1/1000;
      
      NumberFormat df=NumberFormat.getNumberInstance();
      df.setMaximumFractionDigits(3);
      String ytext=df.format(yca);
      System.out.println("yca, ytext: "+yca+","+ytext);
      if (ytext.contains(".")){
      String ytextSub= ytext.substring(ytext.indexOf("."));
      
      if (ytextSub.length()==3){
    	  ytext=ytext+"0";
      }else if (ytextSub.length()==2)
      {
    	  ytext=ytext+"00";
      }
      }
      if (ycoor1 >= 0.0) {temp.putField(" ");} 
      if (ycoor1 == 0.0) {temp.putField("0.000");
                           temp1.putField("0.000");}
      else {temp.putField(ytext);
            temp1.putField(ytext);}
      temp.putField("  ");
      temp1.putField("\n");
              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(11/*inp6*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // inp5 ::= INPUT5 
            {
              Object RESULT = null;
		int in5left = ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left;
		int in5right = ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right;
		Float in5 = (Float)((java_cup.runtime.Symbol) CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).value;
		 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:Input:  x kkcoordinate "+in5);
               float xcoor = in5.floatValue();
      double xca = multiple*xcoor;
      double xc = xca*1000;
      float xc1 = Math.round(xc);
      float xcoor1 = xc1/1000;
      
      NumberFormat df=NumberFormat.getNumberInstance();
      df.setMaximumFractionDigits(3);
      String xtext=df.format(xca);
      System.out.println("*********xtext:string:"+xtext);
      if (xtext.contains("."))
      {
      String xtextSub= xtext.substring(xtext.indexOf("."));
      
      if (xtextSub.length()==3){
    	  xtext=xtext+"0";
      }else if (xtextSub.length()==2)
      {
    	  xtext=xtext+"00";
      }
      }
     if (xcoor1 >= 0.0) {temp.putField(" ");}
     if (xcoor1 == 0.0) {temp.putField("0.000");
                          temp1.putField("0.000");}
      else {temp.putField(xtext);
            temp1.putField(xtext);}
      temp.putField("  ");
      temp1.putField("\n");

              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(10/*inp5*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // inp3 ::= INPUT3 
            {
              Object RESULT = null;
		int in3left = ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left;
		int in3right = ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right;
		Integer in3 = (Integer)((java_cup.runtime.Symbol) CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).value;
		 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:Input:  atomic number "+in3); 
 
// Why are CUP generated files being hand modified ?
// Apparently, classes like ParseGSCF2a are communicating data;
// for example to nanocad in PDB format ?
// Please document this interface.
// Why are there both ParseGSCF2 and ParseGSCF2a which only differ trivially ?
// Let's create a PDB Writer class if that is their purpose.
// This translation of atomic numbers into atomic symbols occurs in
// at least two places.  Let's encapsulate it and reuse it.
// SRB.

     int  atom = in3.intValue();
      temp1.putField(atom);
      temp1.putField("\n");
      if (atom == 6) { temp.putField("C       1          ");}
      else if (atom == 1) { temp.putField("H       1          ");}
        else if (atom == 8) { temp.putField("O       1          ");}
      else if (atom == 2) { temp.putField("He       1         ");}
      else if (atom == 3) { temp.putField("Li       1         ");}
      else if (atom == 4) { temp.putField("Be       1         ");}
      else if (atom == 5) { temp.putField("B       1          ");}
      else if (atom == 7) { temp.putField("N       1          ");}
      else if (atom == 9) { temp.putField("F       1          ");}
      else if (atom == 10) { temp.putField("Ne       1          ");}
      else if (atom == 11.0) { temp.putField("Na       1          "); 
      //temp1.putField(11);}
      }
      else if (atom == 12.0) { temp.putField("Mg       1          ");
      //temp1.putField(12);}
      }
      else if (atom == 13.0) { temp.putField("Al       1          ");
      //temp1.putField(13);}
      }
      else if (atom == 14.0) { temp.putField("Si       1          ");
      //temp1.putField(14);}
      }
      else if (atom == 15.0) { temp.putField("P        1          ");
      //temp1.putField(15);}
      }
      else if (atom == 16.0) { temp.putField("S        1          ");
      //temp1.putField(16);}
      }
      else if (atom == 17.0) { temp.putField("Cl       1          ");
      //temp1.putField(17);}
      }
      else if (atom == 18.0) { temp.putField("Ar       1          ");
      //temp1.putField(18);}
      }
      else if (atom == 19.0) { temp.putField("K        1          ");
      //temp1.putField(19);}
      }
      else if (atom == 20.0) { temp.putField("Ca       1          ");
      //temp1.putField(20);}
      }
      else if (atom == 21.0) { temp.putField("Sc       1          ");
      //temp1.putField(21);}
      }
      else if (atom == 22.0) { temp.putField("Ti       1          ");
      //temp1.putField(22);}
      }
      else if (atom == 23.0) { temp.putField("V        1          ");
      //temp1.putField(23);}
      }
      else if (atom == 24.0) { temp.putField("Cr       1          ");
      //temp1.putField(24);}
      }
      else if (atom == 25.0) { temp.putField("Mn       1          ");
      //temp1.putField(25);}
      }
      else if (atom == 26.0) { temp.putField("Fe       1          ");
      //temp1.putField(26);}
      }
      else if (atom == 27.0) { temp.putField("Co       1          ");
      //temp1.putField(27);}
      }
      else if (atom == 28.0) { temp.putField("Ni       1          ");
      //temp1.putField(28);}
      }
      else if (atom == 29.0) { temp.putField("Cu       1          ");
      //temp1.putField(29);}
      }
      else if (atom == 30.0) { temp.putField("Zn       1          ");
      //temp1.putField(30);}
      }
      else if (atom == 31.0) { temp.putField("Ga       1          ");
      //temp1.putField(31);}
      }
      else if (atom == 32.0) { temp.putField("Ge       1          ");
      //temp1.putField(32);}
      }
      else if (atom == 33.0) { temp.putField("As       1          ");
      //temp1.putField(33);}
      }
      else if (atom == 34.0) { temp.putField("Se       1          ");
      //temp1.putField(34);}
      }
      else if (atom == 35.0) { temp.putField("Br       1          ");
      //temp1.putField(35);}
      }
      else if (atom == 36.0) { temp.putField("Kr       1          ");
      //temp1.putField(36);}
      }
      else if (atom == 37.0) { temp.putField("Rb       1          ");
      //temp1.putField(37);}
      }
      else if (atom == 38.0) { temp.putField("Sr       1          ");
      //temp1.putField(38);}
      }
      else if (atom == 39.0) { temp.putField("Y        1          ");
      //temp1.putField(39);}
      }
      else if (atom == 40.0) { temp.putField("Zr       1          ");
      //temp1.putField(40);}
      }
      else if (atom == 41.0) { temp.putField("Nb       1          ");
      //temp1.putField(41);}
      }
      else if (atom == 42.0) { temp.putField("Mo       1          ");
      //temp1.putField(42);}
      }
      else if (atom == 43.0) { temp.putField("Tc       1          ");
      //temp1.putField(43);}
      }
      else if (atom == 44.0) { temp.putField("Ru       1          ");
      //temp1.putField(44);}
      }
      else if (atom == 45.0) { temp.putField("Rh       1          ");
      //temp1.putField(45);}
      }
      else if (atom == 46.0) { temp.putField("Pd       1          ");
      //temp1.putField(46);}
      }
      else if (atom == 47.0) { temp.putField("Ag       1          ");
      //temp1.putField(47);}
      }
      else if (atom == 48.0) { temp.putField("Cd       1          ");
      //temp1.putField(48);}
      }
      else if (atom == 49.0) { temp.putField("In       1          ");
      //temp1.putField(49);}
      }
      else if (atom == 50.0) { temp.putField("Sn       1          ");
      //temp1.putField(50);}
      }
      else if (atom == 51.0) { temp.putField("Sb       1          ");
      //temp1.putField(51);}
      }
      else if (atom == 52.0) { temp.putField("Te       1          ");
      //temp1.putField(52);}
      }
      else if (atom == 53.0) { temp.putField("I        1          ");
      //temp1.putField(53);}
      }
      else if (atom == 54.0) { temp.putField("Xe       1          ");
      //temp1.putField(54);}
      }
      else if (atom == 55.0) { temp.putField("Cs       1          ");
      //temp1.putField(55);}
      }
      else if (atom == 56.0) { temp.putField("Ba       1          ");
      //temp1.putField(56);}
      }
      else if (atom == 57.0) { temp.putField("La       1          ");
      //temp1.putField(57);}
      }
      else if (atom == 58.0) { temp.putField("Ce       1          ");
      //temp1.putField(58);}
      }
      else if (atom == 59.0) { temp.putField("Pr       1          ");
      //temp1.putField(59);}
      }
      else if (atom == 60.0) { temp.putField("Nd       1          ");
      //temp1.putField(60);}
      }
      else if (atom == 61.0) { temp.putField("Pm       1          ");
      //temp1.putField(61);}
      }
      else if (atom == 72.0) { temp.putField("Hf       1          ");
      //temp1.putField(72);}
      }
      else if (atom == 73.0) { temp.putField("Ta       1          ");
      //temp1.putField(73);}
      }
      else if (atom == 74.0) { temp.putField("W        1          ");
      //temp1.putField(74);}
      }
      else if (atom == 75.0) { temp.putField("Re       1          ");
      //temp1.putField(75);}
      }
      else if (atom == 76.0) { temp.putField("Os       1          ");
      //temp1.putField(76);}
      }
      else if (atom == 77.0) { temp.putField("Ir       1          ");
      //temp1.putField(77);}
      }
      else if (atom == 78.0) { temp.putField("Pt       1          ");
      //temp1.putField(78);}
      }
      else if (atom == 79.0) { temp.putField("Au       1          ");
      //temp1.putField(79);}
      }
      else if (atom == 80.0) { temp.putField("Hg       1          ");
      //temp1.putField(80);}
      }
      else if (atom == 81.0) { temp.putField("Tl       1          ");
      //temp1.putField(81);}
      }
      else if (atom == 82.0) { temp.putField("Pb       1          ");
      //temp1.putField(82);}
      }
      else if (atom == 83.0) { temp.putField("Bi       1          ");
      //temp1.putField(83);}
      }
      else if (atom == 84.0) { temp.putField("Po       1          ");
      //temp1.putField(84);}
      }
      else if (atom == 85.0) { temp.putField("At       1          ");
      //temp1.putField(85);}
      }
      else if (atom == 86.0) { temp.putField("Rn       1          ");
      //temp1.putField(86);}
      }
      else if (atom == 87.0) { temp.putField("Fr       1          ");
      //temp1.putField(87);}
      }
         CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(9/*inp3*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // inp2 ::= INPUT2 
            {
              Object RESULT = null;
		int in2left = ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left;
		int in2right = ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right;
		Integer in2 = (Integer)((java_cup.runtime.Symbol) CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).value;
		 //___________________________________________________________________
   if (DEBUG) System.out.println("CUP:Input:  center number "+in2);
   int cnum = in2.intValue();
   // If cnum greather than 9 and < 100 Space of Atom need to change
   // etc to conform to PDB format
   if (cnum > 9 && cnum < 100)
   { temp.putField("ATOM     ");
   }
   else if (cnum > 99 && cnum < 1000)
   { temp.putField("ATOM    ");
   }
   else{
   temp.putField("ATOM      ");
   }
   temp.putField(cnum);
   temp1.putField(cnum);
   temp1.putField("\n");
   temp.putField("  ");

              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(8/*inp2*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // cycle2 ::= inp2 inp3 INPUT4 inp5 inp6 inp7 
            {
              Object RESULT = null;

              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(14/*cycle2*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-5)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // cycle1 ::= cycle2 
            {
              Object RESULT = null;

              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(13/*cycle1*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // cycle1 ::= cycle1 cycle2 
            {
              Object RESULT = null;

              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(13/*cycle1*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // scfcycle ::= INPUT1 DASH1 cycle1 DASH2 
            {
              Object RESULT = null;

              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(4/*scfcycle*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-3)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // scfpat ::= scfcycle 
            {
              Object RESULT = null;

              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(3/*scfpat*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // scfpat ::= scfpat scfcycle 
            {
              Object RESULT = null;
		 if (DEBUG) System.out.println("CUP:Input: in scfpat"); 
              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(3/*scfpat*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // scfintro ::= FOUNDITER 
            {
              Object RESULT = null;
		 if (DEBUG) System.out.println("CUP:Input:  found the start of Iteration"); 
              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(2/*scfintro*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= startpt EOF 
            {
              Object RESULT = null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-1)).value;
		RESULT = start_val;
              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(0/*$START*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-1)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          /* ACCEPT */
          CUP$FinalCoordParser$parser.done_parsing();
          return CUP$FinalCoordParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // startpt ::= scfintro scfpat SCFDONE 
            {
              Object RESULT = null;

              CUP$FinalCoordParser$result = new java_cup.runtime.Symbol(1/*startpt*/, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-2)).left, ((java_cup.runtime.Symbol)CUP$FinalCoordParser$stack.elementAt(CUP$FinalCoordParser$top-0)).right, RESULT);
            }
          return CUP$FinalCoordParser$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

