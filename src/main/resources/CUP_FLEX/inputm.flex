import java_cup.runtime.*;

%%

%class InputMLexer
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
%state INPUT
%state INPUTA
%state INPUTB
%state INPUTC
%state INPUTD
%state INPUTE
%state INPUTF
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
    if (Settings.DEBUG) System.out.println("InputFlex: Found Number of steps");
	   yybegin(ITER);
	   return new Symbol(InputSym.FOUNDITER); 
  }
}

<ITER>{
  "X           Y           Z" {
    if (Settings.DEBUG) System.out.println("InputFlex: Found Input Orientation");
            yybegin(INPUTF);
            return new Symbol(InputSym.INPUT1);}

  "THE_END_OF_FILE" {
	     yybegin(IGNOREALL);
      return new Symbol(InputSym.SCFDONE);}

  .|\n {}

}


<INPUTF> {
  "---------------------------------------------------------------------"
   {
 if  (Settings.DEBUG) System.out.println("InputFlex: Found the first dash");
  yybegin (INPUT);
   return new Symbol(InputSym.DASH1);
}
}     

<INPUT> {
   {INT} {
  if  (Settings.DEBUG) System.out.println("InputFlex: Found integer1 in input");
  yybegin (INPUTA);
   return new Symbol(InputSym.INPUT2, new Integer(yytext()));
}

  "---------------------------------------------------------------------"
   {
 if  (Settings.DEBUG) System.out.println("InputFlex: Found the second dash");
  yybegin (ITER);
   return new Symbol(InputSym.DASH2);
}

}


<INPUTA> {
   {INT} {
  if  (Settings.DEBUG) System.out.println("InputFlex: Found integer2 in input");
  yybegin (INPUTB);
   return new Symbol(InputSym.INPUT3, new Integer(yytext()));
}
}

<INPUTB> {
   {INT} {
  if  (Settings.DEBUG) System.out.println("InputFlex: Found integer3 in input");
  yybegin (INPUTC);
   return new Symbol(InputSym.INPUT4, new Integer(yytext()));
}
}

<INPUTC> {
   {FLOAT} {
  if  (Settings.DEBUG) System.out.println("InputFlex: Found x coord. in input");
  yybegin (INPUTD);
   return new Symbol(InputSym.INPUT5, new Float(yytext()));
}
}

<INPUTD> {
   {FLOAT} {
  if  (Settings.DEBUG) System.out.println("InputFlex: Found y coord. in input");
  yybegin (INPUTE);
   return new Symbol(InputSym.INPUT6, new Float(yytext()));
}
}


<INPUTE> {
   {FLOAT} {
  if  (Settings.DEBUG) System.out.println("InputFlex: Found z coord. in input");
  yybegin (INPUT);
   return new Symbol(InputSym.INPUT7, new Float(yytext()));
}
}




<IGNOREALL>{
  .|\n {}
}
 
.|\n {}
