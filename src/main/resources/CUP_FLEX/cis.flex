/* CIS Keyword
   Last Update: 6/26/2001 
   http://www.gaussian.com/00000426.htm
*/


 
%%



%class CIS
%public
%unicode
/*
%cup
%cupdebug
*/
%ignorecase

%state ITERATIONVAL
%state DIMENSIONVAL
%state ROOTVAL
%state AFTERROOT
%state DELTAVAL
%state ROOTEV
%state CHANGEIS
%state CHANGEVAL
%state EXCITATION
%state EXCITEDSTATE
%state SYMMETRY
%state EXCITENERGY
%state EV
%state FREQ
%state NM
%state FVAL
%state STRENGTH
%state TRANSITION
%state CISENERGY
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



/* ____________________________________
   Description, state selection options */

<YYINITIAL>{
  "Density" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Singlets" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Triplets" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "50-50" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Root=" {if (Settings.DEBUG) System.out.println(yytext());
	   yybegin(INTVAL);
  }
  "NStates=" {if (Settings.DEBUG) System.out.println(yytext());
	      yybegin(INTVAL);
  }
  "Add=" {if (Settings.DEBUG) System.out.println(yytext());
	  yybegin(INTVAL);
  }
  "Densities" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "TransitionDensities" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "AllTransitionDensities" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Direct" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "MO" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "AO" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "FC" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Conver=" {if (Settings.DEBUG) System.out.println(yytext());
	     yybegin(INTVAL);
  }
  "Read" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Restart" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "RWFRestart" {if (Settings.DEBUG) System.out.println(yytext());
  }
  .|\n {}
}

/* _________________
   Debugging options */
<YYINITIAL>{
  "ICDiag" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "MaxDiag=" {if (Settings.DEBUG) System.out.println(yytext());
	      yybegin(INTVAL);
  }
  "MaxDavidson" {if (Settings.DEBUG) System.out.println(yytext());
  }
  .|\n {}
}

/* __________________
   Examples, Energies */

<YYINITIAL>{
  "DE(CI)=" {if (Settings.DEBUG) System.out.println(yytext());
	     yybegin(FLOATVAL); 
  }
  "Iteration" {if (Settings.DEBUG) System.out.println(yytext());
	       yybegin(ITERATIONVAL); 
  }
  "Dimension" {if (Settings.DEBUG) System.out.println(yytext());
	       yybegin(DIMENSIONVAL); 
  }
  "Root" {if (Settings.DEBUG) System.out.println(yytext());
	  yybegin(ROOTVAL);
  }
  "Excitation Energies [eV] at current iteration:" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Excited States From <AA,BB:AA,BB> singles matrix:" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Excitation energies and oscillator strengths:" {if (Settings.DEBUG) System.out.println(yytext());
						   yybegin(EXCITATION);
  }
  .|\n {}
}

<ITERATIONVAL>{
  {INT} {if (Settings.DEBUG) System.out.println(yytext());
	 yybegin(YYINITIAL);
  }
}

<DIMENSIONVAL>{
  {INT} {if (Settings.DEBUG) System.out.println(yytext());
	 yybegin(YYINITIAL);
  }
}



/* ROOTVAL and related states */

<ROOTVAL>{
  {INT} {if (Settings.DEBUG) System.out.println(yytext());
	 yybegin(AFTERROOT);
  }
}

<AFTERROOT>{
  "not converged, maximum delta is" {if (Settings.DEBUG) System.out.println(yytext());
				     yybegin(DELTAVAL);
  }
  ":" {if (Settings.DEBUG) System.out.println(yytext());
	  yybegin(ROOTEV);
  }
}

<DELTAVAL>{
  {FLOAT} {if (Settings.DEBUG) System.out.println(yytext());
	   yybegin(YYINITIAL);
  }
}

<ROOTEV>{
  {FLOAT} {if (Settings.DEBUG) System.out.println(yytext());
	   yybegin(CHANGEIS);
  }
}

<CHANGEIS>{
  "Change is" {if (Settings.DEBUG) System.out.println(yytext());
	       yybegin(CHANGEVAL);
  }
}

<CHANGEVAL>{
  {FLOAT} {if (Settings.DEBUG) System.out.println(yytext());
	   yybegin(YYINITIAL);
  }
}





/* EXCITATION and related states */

<EXCITATION>{
  "Excited State" {if (Settings.DEBUG) System.out.println(yytext());
		   yybegin(EXCITEDSTATE);
  }
}

<EXCITEDSTATE>{
  {INT}":" {if (Settings.DEBUG) System.out.println(yytext());
	    yybegin(SYMMETRY);
  }
}

<SYMMETRY>{
  {WORD} {if (Settings.DEBUG) System.out.println(yytext());
	  yybegin(EXCITENERGY);
  }
}

<EXCITENERGY>{
  {FLOAT} {if (Settings.DEBUG) System.out.println(yytext());
	   yybegin(EV);
  }
}

<EV>{
  "eV" {if (Settings.DEBUG) System.out.println(yytext());
	yybegin(FREQ);
  }
}

<FREQ>{
  {FLOAT} {if (Settings.DEBUG) System.out.println(yytext());
	   yybegin(NM);
  }
}

<NM>{
  "nm" {if (Settings.DEBUG) System.out.println(yytext());
	yybegin(FVAL);
  }
}

<FVAL>{
  "f=" {if (Settings.DEBUG) System.out.println(yytext());
	yybegin(STRENGTH);
  }
}

<STRENGTH>{
  {FLOAT} {if (Settings.DEBUG) System.out.println(yytext());
	   yybegin(TRANSITION);
  }
}

<TRANSITION>{
  {INT}"->"{INT} {if (Settings.DEBUG) System.out.println(yytext());
  }
  {FLOAT} {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Total Energy, E(Cis) =" {if (Settings.DEBUG) System.out.println(yytext());
			    yybegin(CISENERGY);
  }
}

<CISENERGY>{
  {FLOAT} {if (Settings.DEBUG) System.out.println(yytext());
	   yybegin(IGNOREALL);
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
  
