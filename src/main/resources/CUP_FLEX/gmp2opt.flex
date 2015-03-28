import java_cup.runtime.*;

%%

%class GMP2OPTLexer
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
  "Number of steps in this run" {
    if (Settings.DEBUG) System.out.println("GOPTFlex: Found Number of steps");
	   yybegin(ITER);
	   return new Symbol(GOPTSym.FOUNDITER); 
  }
}

<ITER>{
   "Step number" {
   if (Settings.DEBUG) System.out.println("GOPTFlex: Found the Step number");
   yybegin(INTVALUE);
   return new Symbol(GOPTSym.NSearch);}

  "EUMP2 =" {
       if (Settings.DEBUG) System.out.println("GOPTFlex: Found the energy");
               yybegin(FLOATVALUE);
              return new Symbol(GOPTSym.Energ);}
   "EMP2 =" {
       if (Settings.DEBUG) System.out.println("GOPTFlex: Found the energy");
               yybegin(FLOATVALUE);
              return new Symbol(GOPTSym.Energ);}

  "Maximum Force" {
   if (Settings.DEBUG) System.out.println("GOPTFlex: Found Maximum Force");
            yybegin(FLOAT1);
            return new Symbol(GOPTSym.MaxGrad);}

  "RMS     Force"  {
   if (Settings.DEBUG) System.out.println("GOPTFlex: Found RMS Force");
            yybegin(FLOAT2);
            return new Symbol(GOPTSym.RmsGrad);}

  "THE_END_OF_FILE" {
     yybegin(IGNOREALL);
      return new Symbol(GOPTSym.SCFDONE);}


  "Optimization completed" {
     yybegin(IGNOREALL);
      return new Symbol(GOPTSym.SCFDONE);}

  .|\n {}

}


<FLOATVALUE>{
  {FLOAT} {
   if (Settings.DEBUG) System.out.println("GOPTFlex: Found the energy");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER);
   return new Symbol(GOPTSym.ENERGY, new Float(yytext()));}
}

<FLOAT1>{
  {FLOAT} {
  if (Settings.DEBUG) System.out.println("GOPTFlex: Found the maximum force");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER);
   return new Symbol(GOPTSym.MGRAD, new Float(yytext()));}
}

<FLOAT2>{
  {FLOAT} {
  if (Settings.DEBUG) System.out.println("GOPTFlex: Found the RMS force");   
if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER);
   return new Symbol(GOPTSym.RGRAD, new Float(yytext()));}
}

<INTVALUE>{
  {INT} {
  if  (Settings.DEBUG) System.out.println("GOPTFlex: Found iteration");
   if (Settings.DEBUG) System.out.println(yytext());
  yybegin (ITER);
   return new Symbol(GOPTSym.ITERATION, new Integer(yytext()));
}
}

<IGNOREALL>{
  .|\n {}
}
 
.|\n {}
