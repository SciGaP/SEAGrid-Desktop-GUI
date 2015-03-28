/* Density Functional Methods (DFT) Keywords
   Last Update: 6/26/2001 
   http://www.gaussian.com/00000432.htm
*/


 
%%



%class DFT
%public
%unicode
/*
%cup
%cupdebug
*/
%ignorecase

%state FLOATVAL
%state INTVAL
%state MMMM
%state NNNN
%state AU
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



/* ____________________
   Exchange Functionals */

<YYINITIAL>{
  "HFS" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "XAlpha" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "HFB" {if (Settings.DEBUG) System.out.println(yytext());
  }
}

/* ______________________________________________
   Exchange Functionals Combined with Correlation */

<YYINITIAL>{
  "PW91" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "S" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "XA" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "B" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "MPW" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "G96" {if (Settings.DEBUG) System.out.println(yytext());
  }
}

/* ___________________________________________________________
   Correlation Functionals:  must be combined with an exchange */

<YYINITIAL>{
  "VWN" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "LSDA" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "VWN5" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "LYP" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "PL" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "P86" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "PW91" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "B96" {if (Settings.DEBUG) System.out.println(yytext());
  }
}

/* __________________
   Hybrid Functionals */

<YYINITIAL>{
  "B3LYP" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "B3P86" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "B3PW91" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "B1B96" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "B1LHYP" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "MPW1PW91" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "G961LYP" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "BHandH" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "BHandHLYP" {if (Settings.DEBUG) System.out.println(yytext());
  }
}

/* ____________________________________________________________
   User-defined, local exchange, non-local exchange corrections */

<YYINITIAL>{
  "IOp(5/45=" {yybegin(MMMM); 
	       if (Settings.DEBUG) System.out.println(yytext());
  }
  "IOp(5/46=" {yybegin(MMMM); 
	       if (Settings.DEBUG) System.out.println(yytext());
  }
  "IOp(5/47=" {yybegin(MMMM); 
	       if (Settings.DEBUG) System.out.println(yytext());
  }
  .|\n {}
}

<MMMM>{
  {INT} {yybegin(NNNN);
	 if (Settings.DEBUG) System.out.println(yytext());
  }
}

<NNNN>{
  {INT} {yybegin(YYINITIAL);
	 if (Settings.DEBUG) System.out.println(yytext());
  }
}

/* __________________________________________________
   Accuracy considerations; convergence and stability */

<YYINITIAL>{
  "Int=FineGrid" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Int(Grid=" {if (Settings.DEBUG) System.out.println(yytext());
	    yybegin(INTVAL); 
  }
  "SCF=Tight" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "SCF=VShift" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "SCF=QC" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Stable=Opt" {if (Settings.DEBUG) System.out.println(yytext());
  }
  .|\n {}
}

/* ________________
   Examples, energy */

<YYINITIAL>{
  {FLOAT} {if (Settings.DEBUG) System.out.println(yytext());
	   yybegin(AU);
  }
}

<AU>{
  "A.U. after" {if (Settings.DEBUG) System.out.println(yytext());
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
  
