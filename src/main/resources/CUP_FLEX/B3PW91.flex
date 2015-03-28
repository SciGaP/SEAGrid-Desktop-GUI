Fimport java_cup.runtime.*;

%%

%class B3PW91Lexer
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
%state MP2
%state MPOPT
%state FLOATMP1
%state FLOATMP2
%state FLOATMP3
%state IGNOREALL
%state INTMP
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
    if (Settings.DEBUG) System.out.println("B3PW91Flex: Found Number of steps");
	   yybegin(ITER);
	   return new Symbol(B3Pw91Sym.FOUNDITER1); 
  }
}

<ITER>{
   "Step number" {
   if (Settings.DEBUG) System.out.println("B3PW91Flex: Found the Step number");
   yybegin(INTVALUE);
   return new Symbol(B3Pw91Sym.NSearch1);}

  "SCF Done:  E(RB+HF-PW91) =" {
       if (Settings.DEBUG) System.out.println("B3PW91Flex: Found the energy in ITER");
               yybegin(FLOATVALUE);
              return new Symbol(B3Pw91Sym.Energ1);}

  "Maximum Force" {
   if (Settings.DEBUG) System.out.println("B3PW91Flex: Found Maximum Force");
            yybegin(FLOAT1);
            return new Symbol(B3Pw91Sym.MaxGrad1);}

  "RMS     Force"  {
   if (Settings.DEBUG) System.out.println("B3PW91Flex: Found RMS Force");
            yybegin(FLOAT2);
            return new Symbol(B3Pw91Sym.RmsGrad1);}


  "Optimization completed" {
if (Settings.DEBUG) System.out.println("B3PW91Flex: SCFDONE1, Optimization completed"); 
     yybegin(MP2);
      return new Symbol(B3Pw91Sym.SCFDONE1);}

  .|\n {}

}


<FLOATVALUE>{
  {FLOAT} {
   if (Settings.DEBUG) System.out.println("B3PW91Flex: Found the energy in FLOATVALUE");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER);
   return new Symbol(B3Pw91Sym.ENERGY1, new Float(yytext()));}
}

<FLOAT1>{
  {FLOAT} {
  if (Settings.DEBUG) System.out.println("B3PW91Flex: Found the maximum force");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER);
   return new Symbol(B3Pw91Sym.MGRAD1, new Float(yytext()));}
}

<FLOAT2>{
  {FLOAT} {
  if (Settings.DEBUG) System.out.println("B3PW91Flex: Found the RMS force");   
if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER);
   return new Symbol(B3Pw91Sym.RGRAD1, new Float(yytext()));}
}

<INTVALUE>{
  {INT} {
  if  (Settings.DEBUG) System.out.println("B3PW91Flex: Found iteration");
   if (Settings.DEBUG) System.out.println(yytext());
  yybegin (ITER);
   return new Symbol(B3Pw91Sym.ITERATION1, new Integer(yytext()));
}
}

<MP2>{
  "MP2/6-31G(d') Opt=RCFC" {
  if (Settings.DEBUG) System.out.println("B3PW91Flex: Found MP2(Full)");
            yybegin(MPOPT);
            return new Symbol(B3Pw91Sym.MPStart);}
  }


<MPOPT>{

   "Step number" {
if (Settings.DEBUG) System.out.println("B3PW91Flex: Found the Step number for MP");
   yybegin(INTMP);
   return new Symbol(B3Pw91Sym.NMP);}

  "EUMP2 = " {
       if (Settings.DEBUG) System.out.println("B3PW91Flex: Found MP2 energy");
               yybegin(FLOATMP1);
              return new Symbol(B3Pw91Sym.MPEnerg);}

  "Maximum Force" {
   if (Settings.DEBUG) System.out.println("B3PW91Flex: Found Maximum Force");
            yybegin(FLOATMP2);
            return new Symbol(B3Pw91Sym.MPMax);}

  "RMS     Force"  {
   if (Settings.DEBUG) System.out.println("B3PW91Flex: Found RMS Force");
            yybegin(FLOATMP3);
            return new Symbol(B3Pw91Sym.MPRms);}


  "Optimization completed" {
     yybegin(IGNOREALL);
      return new Symbol(B3Pw91Sym.MPDONE);}


  .|\n {}
  }


<FLOATMP1>{
  {FLOAT} {
   if (Settings.DEBUG) System.out.println("B3PW91Flex: MP2 Found the energy");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(MPOPT);
   return new Symbol(B3Pw91Sym.MPENERGY, new Float(yytext()));}
}

<FLOATMP2>{
  {FLOAT} {
  if (Settings.DEBUG) System.out.println("B3PW91Flex: MP2 Found the maximum force");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(MPOPT);
   return new Symbol(B3Pw91Sym.MPMGRAD, new Float(yytext()));}
}

<FLOATMP3>{
  {FLOAT} {
  if (Settings.DEBUG) System.out.println("B3PW91Flex: MP2 Found the RMS force");   
if (Settings.DEBUG) System.out.println(yytext());
   yybegin(MPOPT);
   return new Symbol(B3Pw91Sym.MPRGRAD, new Float(yytext()));}
}

<INTMP>{
  {INT} {
  if  (Settings.DEBUG) System.out.println("B3PW91Flex: MP2 Found iteration");
   if (Settings.DEBUG) System.out.println(yytext());
  yybegin (MPOPT);
   return new Symbol(B3Pw91Sym.MPITER, new Integer(yytext()));}
}

<IGNOREALL>{
  .|\n {}
}
 
.|\n {}
