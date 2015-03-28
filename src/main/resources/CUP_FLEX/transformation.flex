/* Transformation Keyword
   Last Update: 12/31/2000 
   http://www.gaussian.com/00000480.htm
*/
 


%%



/* ___________________________________________________
   The name of the generated Java class will be lexer. */ 

%class Transformation
%public
%unicode

/* _______________________________________________________________
   Will switch to a CUP compatibility mode to interface with a CUP 
   generated parser. 

%cup 
*/ 

/* ________________________________________________________________
   The current line number can be accessed with the variable yyline 
   and the current column number with the variable yycolumn. */ 

%line 
%column 
%standalone
%8bit

/* ___________________________________________
   Copied verbatim into generated lexer class: */

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
CommentContent = ( [^*] | \*+ [^/*] )*
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
STRING         = [A-Z]+
GRAB           = [^(" "|\r|\n|\r\n| \t\f)]+


 
%%


/* _________________________________________
   Integral Transformation Algorithm Options
*/

<YYINITIAL>{
  "Direct" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "InCore" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "FullDirect" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "SemiDirect" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Conventional" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "Old2PDM" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "New2PDM" {if (Settings.DEBUG) System.out.println(yytext());
  }
}


/* __________________________
   Integral Selection Options
*/

<YYINITIAL>{
  "Full" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "IJAB" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "IAJB" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "IJKL" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "IJKA" {if (Settings.DEBUG) System.out.println(yytext());
  }
  "IABC" {if (Settings.DEBUG) System.out.println(yytext());
  }
}

.|\n {}
  
