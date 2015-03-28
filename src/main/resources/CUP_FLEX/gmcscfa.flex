import java_cup.runtime.*;

%%

%class GMCSCFaLexer
%public
%unicode
%cup 
%cupdebug 
%state SCF1
%state ITER
%state ITER2
%state ITER8
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
  "ITER     TOTAL ENERGY        DEL(E)    LAG.ASYMM.  SQCDF  MICIT   DAMP" {
    if (Settings.DEBUG) System.out.println("GMCSCFaFlex: Found START OF");
	   yybegin(ITER);
	   return new Symbol(GMCSCFaSym.FOUNDITER); 
  }
  .|\n {}
}


<ITER>{
  {dec_int_lit} {
     if (Settings.DEBUG) System.out.println("GMCSCFaFlex: Found the first integer in the iteration");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER4);
   return new Symbol(GMCSCFaSym.INTCycle, new Integer(yytext()));}

  "-----------------" {
       if (Settings.DEBUG) System.out.println("GMCSCFaFlex: Found the dash");
               yybegin(DASH);}

}

<ITER4>{
  {FLOAT} {
   if (Settings.DEBUG) System.out.println("GMCSCFaFlex: Found the energy");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER5);
   return new Symbol(GMCSCFaSym.ENERGY, new Float(yytext()));}
}

<ITER5>{
  {FLOAT} { if (Settings.DEBUG) System.out.println("GMCSCFaFlex: ITER5");
   if (Settings.DEBUG) System.out.println("GMCSCFaFlex: Found float1");
   yybegin (ITER6);
    return new Symbol(GMCSCFaSym.FLOAT1);
}
}

<ITER6>{
  {FLOAT} {
  if  (Settings.DEBUG) System.out.println("GMCSCFaFlex: Found float2");
  yybegin (ITER7);
   return new Symbol(GMCSCFaSym.FLOAT2);
}
}

 
<ITER7>{
  {FLOAT} {
     if  (Settings.DEBUG) System.out.println("GMCSCFaFlex: Found float3");
     yybegin(ITER2);
      return new Symbol(GMCSCFaSym.FLOAT3);}
  .|\n {}
}


<ITER2>{
  {dec_int_lit} {
     if (Settings.DEBUG) System.out.println("GMCSCFaFlex: Found the second integer in the iteration");
     if (Settings.DEBUG) System.out.println(yytext());
     yybegin(ITER8); 
      return new Symbol(GMCSCFaSym.INT1, new Integer(yytext()));} 
}

<ITER8>{
  {FLOAT} {
     if  (Settings.DEBUG) System.out.println("GMCSCFaFlex: Found float4");
     yybegin(ITER);
      return new Symbol(GMCSCFaSym.FLOAT4);}
  .|\n {}
}


<FLOATVALUE>{

  .|\n {if  (Settings.DEBUG) System.out.println("GMCSCFaFlex: Found new line");}
  {INT} {
   if  (Settings.DEBUG) System.out.println("GMCSCFaFlex: Found float value");
  yybegin(ITER); }
  "-----------------" {
               yybegin(DASH);
  }
}

<DASH>{
 "LAGRANGIAN CONVERGED" {
  yybegin(IGNOREALL);
  return new Symbol(GMCSCFaSym.SCFDONE);
  }
 .|\n {} 
}

<IGNOREALL>{
  .|\n {}
}

.|\n {}
