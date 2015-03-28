import java_cup.runtime.*;

%%

%class MOptaLexer
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
%state ITER4
%state ITER5
%state ITER6
%state ITER7
%state ITER8
%state ITER9
%state ITER10
%state ITER11
%state ITER12
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
FLOAT          =  [+|-]?"."{DIGIT}+ |  [+|-]?{DIGIT}+"."{DIGIT}+
INT            = [+|-]?{DIGIT}+
BOOL           = [T|F]
EQ             = "="
STRING         = [A-Z]+
GRAB           = [^(" "|\r|\n|\r\n| \t\f)]+

%%

<YYINITIAL>{
  "ITER.   ENERGY(OLD)    ENERGY(NEW)      DE          GRADMAX     GRADNORM    GRADRMS     STEPMAX     STEPLEN     STEPRMS" {
    if (Settings.DEBUG) System.out.println("MOptaFlex: Found START OF");
	   yybegin(ITER);
	   return new Symbol(MOptaSym.FOUNDITER); 
  }
  .|\n {}
}


<ITER>{
   
  {dec_int_lit} {
     if (Settings.DEBUG) System.out.println("MOptaFlex: Found the first integer in the iteration");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER4);
   return new Symbol(MOptaSym.INTCycle, new Integer(yytext()));}

  "Geometry written to block  1 of record 700" {
       if (Settings.DEBUG) System.out.println("MOptaFlex: Found the dash");
               yybegin(DASH);}

}

<ITER4>{
  {FLOAT} {
   if (Settings.DEBUG) System.out.println("MOptaFlex: Found the energy");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER5);
   return new Symbol(MOptaSym.ENERGY, new Float(yytext()));}
}

<ITER5>{
  {FLOAT} { 
     if (Settings.DEBUG) System.out.println("MOptaFlex: ITER5");
   if (Settings.DEBUG) System.out.println("MOptaFlex: Found float1");
     System.out.println(yytext());
   yybegin (ITER6);
    return new Symbol(MOptaSym.FLOAT1);
}
}

<ITER6>{
  {FLOAT} {
  if  (Settings.DEBUG) System.out.println("MOptaFlex: Found float2");
  if (Settings.DEBUG) System.out.println(yytext()); 
  yybegin (ITER7);
   return new Symbol(MOptaSym.FLOAT2);
}
}

 
<ITER7>{
  {FLOAT} {
     if  (Settings.DEBUG) System.out.println("MOptaFlex: Found float3");
     if (Settings.DEBUG) System.out.println(yytext()); 
     yybegin(ITER8);
      return new Symbol(MOptaSym.FLOAT3);}
  .|\n {}
}



<ITER8>{
  {FLOAT} {
     if  (Settings.DEBUG) System.out.println("MOptaFlex: Found float4");
     if (Settings.DEBUG) System.out.println(yytext()); 
     yybegin(ITER9);
      return new Symbol(MOptaSym.FLOAT4);}
  .|\n {}
}

<ITER9>{
  {FLOAT} {
     if  (Settings.DEBUG) System.out.println("MOptaFlex: Found float5");
     if (Settings.DEBUG) System.out.println(yytext()); 
     yybegin(ITER10);
      return new Symbol(MOptaSym.FLOAT5);}
  .|\n {}
}

<ITER10>{
  {FLOAT} {
     if  (Settings.DEBUG) System.out.println("MOptaFlex: Found float6");
      if (Settings.DEBUG) System.out.println(yytext()); 
     yybegin(ITER11);
      return new Symbol(MOptaSym.FLOAT6);}
  .|\n {}
}

<ITER11>{
  {FLOAT} {
     if  (Settings.DEBUG) System.out.println("MOptaFlex: Found float7");
      if (Settings.DEBUG) System.out.println(yytext()); 
     yybegin(ITER12);
      return new Symbol(MOptaSym.FLOAT7);}
  .|\n {}
}

<ITER12>{
  {FLOAT} {
     if  (Settings.DEBUG) System.out.println("MOptaFlex: Found float8");
       if (Settings.DEBUG) System.out.println(yytext()); 
     yybegin(ITER);
      return new Symbol(MOptaSym.FLOAT8);}
  .|\n {}
}



<DASH>{
 "*********************" {
  yybegin(IGNOREALL);
  return new Symbol(MOptaSym.SCFDONE);
  }
 .|\n {} 
}

<IGNOREALL>{
  .|\n {}
}

.|\n {}
