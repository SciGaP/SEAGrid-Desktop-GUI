/* CASSCF Keyword 
   Last Update: 6/26/2001 
   http://www.gaussian.com/00000419.htm
*/


 
%%



%class CASSCF
%public
%unicode
/*
%cup
%cupdebug
*/
%ignorecase

%state GETN
%state GETCOMMA
%state GETM
%state ITN
%state ITNFLOAT
%state FLOATVAL
%state INTVAL
%state IGNOREALL

%standalone
%8bit
%{
  public static boolean DEBUG = false;
%}

/* ______
   Macros */
LineTerminator = \r|\n|\r\n 
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f] 

/* ________
   Comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}
TraditionalComment = "/*" [^*] ~"*/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent = ( [^*] | \*+ [^/*] )*         /* adjust syntax font-lock */
Identifier = [:jletter:] [:jletterdigit:]*

/* ________________________________________________________________
   A literal integer is is a number beginning with a number between 
   one and nine followed by zero or more numbers between 
   zero and nine or just a zero. 

   A identifier integer is a word beginning a letter between A and Z, 
   a and z, or an underscore followed by zero or more letters between 
   A and Z, a and z, zero and nine, or an underscore. */ 
dec_int_lit    = 0 | [1-9][0-9]* 
dec_int_id     = [A-Za-z_][A-Za-z_0-9]* 
DIGIT          = [0-9]
FLOAT          = [+|-]?{DIGIT}+"."{DIGIT}*(["D"|"d"|"E"|"e"]([+|-]?){DIGIT}+)?
INT            = [+|-]?{DIGIT}+
BOOL           = [T|F]
WORD           = [A-Za-z]+
WORDLIST       = ["("]? [1A-Za-z]+ (","[A-Za-z]+)* [")"]?
GRAB           = [^(" "|\r|\n|\r\n| \t\f)]+
TOEOL          = ~(\r|\n|\r\n)


 
%%


<GETN>{ 
  /* get number of electrons */
  {INT} {if (Settings.DEBUG) System.out.println(yytext());
	 yybegin(GETCOMMA);
  }
}

<GETCOMMA>{
  "," {if (Settings.DEBUG) System.out.println(yytext());
	 yybegin(GETM);
  }
}

<GETM>{
  /* get number of orbitals */
  {INT} {if (Settings.DEBUG) System.out.println(yytext());
	 yybegin(YYINITIAL);
  }
}

<YYINITIAL>{
  "CASSCF(" {if (Settings.DEBUG) System.out.println(yytext());
	     yybegin(GETN);
  }
  "CAS(" {if (Settings.DEBUG) System.out.println(yytext());
	     yybegin(GETN);
  }
  "Guess=Alter" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Guess=Only" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Guess=Read,Alter" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Pop=Regular=" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Pop=Full" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "SCF=Conven" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "MP2" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "NRoot=" {if (Settings.DEBUG) System.out.println(yytext());
	    yybegin(INTVAL);
  }
  "StateAverage" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Opt=Conical" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Spin" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "OrbLocal" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "OrbLocal=" {if (Settings.DEBUG) System.out.println(yytext());
	       yybegin(INTVAL);
  }
  "DavidsonDiag" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "FullDiag" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "NoFullDiag" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "StateGuess=" {if (Settings.DEBUG) System.out.println(yytext());
		 yybegin(INTVAL);
  }
  "StateGuess=Read" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "OrbRoot" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "SlaterDet" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "HWDet" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "RFO" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "QC" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "UNO" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Guess=Read" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Pop=NaturalOrbital" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "NPairs=" {if (Settings.DEBUG) System.out.println(yytext());
		  yybegin(INTVAL);
  }
  "Restart" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "DoOff" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Thresh=" {if (Settings.DEBUG) System.out.println(yytext());
	     yybegin(INTVAL);
  }
  "MP2States=" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "NFC=" {if (Settings.DEBUG) System.out.println(yytext());
	  yybegin(INTVAL);
  }
  "NFV=" {if (Settings.DEBUG) System.out.println(yytext());
	  yybegin(INTVAL);
  }
  "UseL906" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Polar=Numer" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "ITN" {if (Settings.DEBUG) System.out.println(yytext());
	 yybegin(ITN);
  }
  "E2=" {if (Settings.DEBUG) System.out.println(yytext());
		 yybegin(FLOATVAL);
  }
  "EUMP2=" {if (Settings.DEBUG) System.out.println(yytext());
		 yybegin(FLOATVAL);
  }
  "QC" {if (Settings.DEBUG) System.out.println(yytext());
  }
  .|\n {}
}

<ITN>{
  "=" {if (Settings.DEBUG) System.out.println(yytext());	  
       yybegin(ITNFLOAT);
  }
  "MaxIt=" {if (Settings.DEBUG) System.out.println(yytext());
	  yybegin(ITNFLOAT);
  }
  "E=" {if (Settings.DEBUG) System.out.println(yytext());
	  yybegin(ITNFLOAT);
  }
  "DE=" {if (Settings.DEBUG) System.out.println(yytext());
	  yybegin(ITNFLOAT);
  }
  "Acc=" {if (Settings.DEBUG) System.out.println(yytext());
	  yybegin(ITNFLOAT);
  }
  "MCSCF converged." {if (Settings.DEBUG) System.out.println(yytext());
		      yybegin(YYINITIAL);
  }
}

<ITNFLOAT>{
  {FLOAT} {if (Settings.DEBUG) System.out.println(yytext());
	   yybegin(ITN);
  }
}

<FLOATVAL>{
  {FLOAT} {if (Settings.DEBUG) System.out.println(yytext());
	   yybegin(YYINITIAL);
  }
}

<INTVAL>{
  {INT} {if (Settings.DEBUG) System.out.println(yytext());
	   yybegin(YYINITIAL);
  }
}

<IGNOREALL>{
  .|\n {}
}

.|\n {}
  
