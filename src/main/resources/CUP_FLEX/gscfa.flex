import java_cup.runtime.*;

%%

%class GSCFaLexer
%public
%unicode
%cup 
%cupdebug 
%state SCF1
%state ITER
%state ITER2
%state ITER3
%state DASH
%state INTVALUE
%state FLOATVALUE
%state ITER4
%state ITER5
%state ITER6
%state ITER7
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
  "ITER EX DEM    TOTAL ENERGY       E CHANGE  DENSITY CHANGE    DIIS ERROR" {
    if (Settings.DEBUG) System.out.println("GSCFaFlex: Found ITER EX DEM etc");
	   yybegin(ITER);
	   return new Symbol(GSCFaSym.FOUNDITER); 
  }

 "ITER EX DEM    TOTAL ENERGY       E CHANGE  DENSITY CHANGE     ORB. GRAD" {
    if (Settings.DEBUG) System.out.println("GSCFaFlex: Found ITER EX DEM etc");
           yybegin(ITER);
           return new Symbol(GSCFaSym.FOUNDITER);
  }

  .|\n {}
}

<ITER>{
  {dec_int_lit} {
     if (Settings.DEBUG) System.out.println("GSCFaFlex: Found the first integer in the iteration");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER2);
   return new Symbol(GSCFaSym.INTCycle, new Integer(yytext()));}

  "-----------------" {
       if (Settings.DEBUG) System.out.println("GSCFaFlex: Found the dash");
               yybegin(DASH);}
  .|\n {}

}

<ITER2>{
  {dec_int_lit} {
     if (Settings.DEBUG) System.out.println("GSCFaFlex: Found the second integer in the iteration");
     if (Settings.DEBUG) System.out.println(yytext());
     yybegin(ITER3); 
      return new Symbol(GSCFaSym.INT1, new Integer(yytext()));} 
}

<ITER3>{
  {dec_int_lit} {
  if (Settings.DEBUG) System.out.println("GSCFaFlex: Found the third integer in the iteration");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER4);
    return new Symbol(GSCFaSym.INT2, new Integer(yytext()));} 
}

<ITER4>{
  {FLOAT} {
   if (Settings.DEBUG) System.out.println("GSCFaFlex: Found the energy");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER5);
   return new Symbol(GSCFaSym.ENERGY, new Float(yytext()));}
}

<ITER5>{
  {FLOAT} { if (Settings.DEBUG) System.out.println("GSCFaFlex: ITER5");
   if (Settings.DEBUG) System.out.println("GSCFaFlex: Found float1");
   yybegin (ITER6);
    return new Symbol(GSCFaSym.FLOAT1); 
}
}

<ITER6>{
  {FLOAT} {
  if  (Settings.DEBUG) System.out.println("GSCFaFlex: Found float2");
  yybegin (ITER7);
   return new Symbol(GSCFaSym.FLOAT2);
}
}

<ITER7>{
  {FLOAT} {
     if  (Settings.DEBUG) System.out.println("GSCFaFlex: Found float3");
     yybegin(ITER);
      return new Symbol(GSCFaSym.FLOAT3);} 
  .|\n {}   
}

<FLOATVALUE>{
  .|\n {if  (Settings.DEBUG) System.out.println("GSCFaFlex: Found new line");}
  {INT} {
   if  (Settings.DEBUG) System.out.println("GSCFaFlex: Found float value");
  yybegin(ITER); }
  "-----------------" {
               yybegin(DASH);
  }
}

<DASH>{
 "DENSITY CONVERGED" {
  yybegin(IGNOREALL);
  return new Symbol(GSCFaSym.SCFDONE);
  }
 .|\n {} 
}

<IGNOREALL>{
  .|\n {}
}

.|\n {}
