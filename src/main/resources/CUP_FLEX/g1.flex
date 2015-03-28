import java_cup.runtime.*;

%%

%class G1Lexer
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
    if (Settings.DEBUG) System.out.println("G1lex: Found Number of steps");
	   yybegin(ITER);
	   return new Symbol(G1Sym.FOUNDITER1); 
  }
}

<ITER>{
   "Step number" {
   if (Settings.DEBUG) System.out.println("G1Flex: Found the Step number");
   yybegin(INTVALUE);
   return new Symbol(G1Sym.NSearch1);}

  "SCF Done:  E(RHF) =" {
       if (Settings.DEBUG) System.out.println("G1Flex: Found the energy in ITER");
               yybegin(FLOATVALUE);
              return new Symbol(G1Sym.Energ1);}

  "Maximum Force" {
   if (Settings.DEBUG) System.out.println("G1Flex: Found Maximum Force");
            yybegin(FLOAT1);
            return new Symbol(G1Sym.MaxGrad1);}

  "RMS     Force"  {
   if (Settings.DEBUG) System.out.println("G1Flex: Found RMS Force");
            yybegin(FLOAT2);
            return new Symbol(G1Sym.RmsGrad1);}


  "Optimization completed" {
if (Settings.DEBUG) System.out.println("G1Flex: SCFDONE1, Optimization completed"); 
     yybegin(MP2);
      return new Symbol(G1Sym.SCFDONE1);}

 "THE_END_OF_FILE"  {
if (Settings.DEBUG) System.out.println("G1Flex: SCFDONE1, THE_END_OF_FILE");
     yybegin(MP2);
      return new Symbol(G1Sym.SCFDONE1);}

  .|\n {}

}


<FLOATVALUE>{
  {FLOAT} {
   if (Settings.DEBUG) System.out.println("G1Flex: Found the energy in FLOATVALUE");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER);
   return new Symbol(G1Sym.ENERGY1, new Float(yytext()));}
}

<FLOAT1>{
  {FLOAT} {
  if (Settings.DEBUG) System.out.println("G1lex: Found the maximum force");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER);
   return new Symbol(G1Sym.MGRAD1, new Float(yytext()));}
}

<FLOAT2>{
  {FLOAT} {
  if (Settings.DEBUG) System.out.println("G1Flex: Found the RMS force");   
if (Settings.DEBUG) System.out.println(yytext());
   yybegin(ITER);
   return new Symbol(G1Sym.RGRAD1, new Float(yytext()));}
}

<INTVALUE>{
  {INT} {
  if  (Settings.DEBUG) System.out.println("G1Flex: Found iteration");
   if (Settings.DEBUG) System.out.println(yytext());
  yybegin (ITER);
   return new Symbol(G1Sym.ITERATION1, new Integer(yytext()));
}
}

<MP2>{
  "MP2(Full)/6-31G(d)" {
  if (Settings.DEBUG) System.out.println("G1Flex: Found MP2(Full)");
            yybegin(MPOPT);
            return new Symbol(G1Sym.MPStart);}

 "THE_END_OF_FILE"  {
if (Settings.DEBUG) System.out.println("G1Flex: MPStart, THE_END_OF_FILE");
     yybegin(MPOPT);
      return new Symbol(G1Sym.MPStart);}
  }


<MPOPT>{

   "Step number" {
if (Settings.DEBUG) System.out.println("G1Flex: Found the Step number for MP");
   yybegin(INTMP);
   return new Symbol(G1Sym.NMP);}

  "EUMP2 = " {
       if (Settings.DEBUG) System.out.println("G1Flex: Found MP2 energy");
               yybegin(FLOATMP1);
              return new Symbol(G1Sym.MPEnerg);}

  "Maximum Force" {
   if (Settings.DEBUG) System.out.println("G1Flex: Found Maximum Force");
            yybegin(FLOATMP2);
            return new Symbol(G1Sym.MPMax);}

  "RMS     Force"  {
   if (Settings.DEBUG) System.out.println("G1Flex: Found RMS Force");
            yybegin(FLOATMP3);
            return new Symbol(G1Sym.MPRms);}


  "Optimization completed" {
     yybegin(IGNOREALL);
      return new Symbol(G1Sym.MPDONE);}

 "THE_END_OF_FILE"  {
if (Settings.DEBUG) System.out.println("G1Flex: MPDONE, THE_END_OF_FILE");
     yybegin(IGNOREALL);
      return new Symbol(G1Sym.MPDONE);}

  .|\n {}
  }


<FLOATMP1>{
  {FLOAT} {
   if (Settings.DEBUG) System.out.println("G1Flex: Found the energy");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(MPOPT);
   return new Symbol(G1Sym.MPENERGY, new Float(yytext()));}
}

<FLOATMP2>{
  {FLOAT} {
  if (Settings.DEBUG) System.out.println("G1Flex: Found the maximum force");
   if (Settings.DEBUG) System.out.println(yytext());
   yybegin(MPOPT);
   return new Symbol(G1Sym.MPMGRAD, new Float(yytext()));}
}

<FLOATMP3>{
  {FLOAT} {
  if (Settings.DEBUG) System.out.println("G1Flex: Found the RMS force");   
if (Settings.DEBUG) System.out.println(yytext());
   yybegin(MPOPT);
   return new Symbol(G1Sym.MPRGRAD, new Float(yytext()));}
}

<INTMP>{
  {INT} {
  if  (Settings.DEBUG) System.out.println("G1Flex: Found iteration");
   if (Settings.DEBUG) System.out.println(yytext());
  yybegin (MPOPT);
   return new Symbol(G1Sym.MPITER, new Integer(yytext()));}
}

<IGNOREALL>{
  .|\n {}
}
 
.|\n {}
