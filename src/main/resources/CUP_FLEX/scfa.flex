import java_cup.runtime.*;

%%

%class SCFaLexer
%public
%unicode
%cup 
%cupdebug 
%state ITER
%state ITER2
%state ITER3
%state INTVALUE
%state FLOATVALUE
%state FLOAT1
%state FLOAT2
%state IGNOREALL
%standalone
%8bit

/* ___________________________________________
   Copied verbatim into generated lexer class:
*/
%{
  public static boolean DEBUG = false;
%}

LineTerminator = \r|\n|\r\n 
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f] 
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}
TraditionalComment = "/*" [^*] ~"*/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent = ( [^*] | \*+ [^/*] )*        /* adjust syntax font-coloring */
Identifier = [:jletter:] [:jletterdigit:]*
dec_int_lit    = 0 | [1-9][0-9]* 
dec_int_id     = [A-Za-z_][A-Za-z_0-9]* 
DIGIT          = [0-9]
FLOAT          = [+|-]?{DIGIT}+"."{DIGIT}+
INT            = [+|-]?{DIGIT}+
BOOL           = [T|F]
EQ             = "="
STRING         = [A-Z]+
GRAB           = [^(" "|\r|\n|\r\n| \t\f)]+

%%

<YYINITIAL>{
  " shell SCF:" {
    if (Settings.DEBUG) System.out.println("SCFaFlex: Found Shell SCF");
	   yybegin(ITER);
	   return new Symbol(SCFaSym.FOUNDITER); 
  }
}

<ITER>{
   "Cycle" {
   if (Settings.DEBUG) System.out.println("SCFaFlex: Found the cycle number");
   yybegin(INTVALUE);
   return new Symbol(SCFaSym.NSearch);}

  " E=" {
       if (Settings.DEBUG) System.out.println("SCFaFlex: Found the energy");
               yybegin(FLOATVALUE);
              return new Symbol(SCFaSym.Energ);}
   "SCF Done:  E(UHF) =" {
       if (Settings.DEBUG) System.out.println("SCFaFlex: Done");
               yybegin(IGNOREALL);
              return new Symbol(SCFaSym.SCFDONE);}

   "SCF Done:  E(RHF) =" {
       if (Settings.DEBUG) System.out.println("SCFaFlex: Found the energy");
               yybegin(IGNOREALL);
              return new Symbol(SCFaSym.SCFDONE);}
  .|\n {}

}


<FLOATVALUE>{
  {FLOAT} {
   if (Settings.DEBUG) System.out.println("SCFaFlex: Found in FLOATVALUE the energy");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER);
   return new Symbol(SCFaSym.ENERGY, new Float(yytext()));}
}


<INTVALUE>{
  {INT} {
  if  (Settings.DEBUG) System.out.println("SCFaFlex: Found iteration");
   if (Settings.DEBUG) System.out.println(yytext());
  yybegin (ITER);
   return new Symbol(SCFaSym.ITERATION, new Integer(yytext()));
}
}

<IGNOREALL>{
  .|\n {}
}
 
.|\n {}
