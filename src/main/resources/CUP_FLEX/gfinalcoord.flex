import java_cup.runtime.*;

%%

%class GFinalCoordLexer
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
GFinalCoordCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f] 
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}
TraditionalComment = "/*" [^*] ~"*/"
EndOfLineComment = "//" {GFinalCoordCharacter}* {LineTerminator}
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
  "COORDINATES (BOHR)" {
	   yybegin(ITER);
	   return new Symbol(GFinalCoordSym.FOUNDITER); 
  }
}

<ITER>{
  "CHARGE         X                   Y                   Z" {
            yybegin(INPUT);
            return new Symbol(GFinalCoordSym.INPUT1);}

  "THE_END_OF_FILE" {
	     yybegin(IGNOREALL);
      return new Symbol(GFinalCoordSym.SCFDONE);}

   "---------------------" {
             yybegin(IGNOREALL);
      return new Symbol(GFinalCoordSym.SCFDONE);}


  .|\n {}

}


<INPUT> {
   {dec_int_id} {
  yybegin (INPUTA);
   return new Symbol(GFinalCoordSym.INPUT2);
}

  "INTERNUCLEAR DISTANCES"
   {
  yybegin (IGNOREALL);
   return new Symbol(GFinalCoordSym.SCFDONE);
}

}


<INPUTA> {
   {FLOAT} {
  yybegin (INPUTB);
   return new Symbol(GFinalCoordSym.INPUT3, new Float(yytext()));
}
}

<INPUTB> {
   {FLOAT} {
  yybegin (INPUTC);
   return new Symbol(GFinalCoordSym.INPUT4, new Float(yytext()));
}
}

<INPUTC> {
   {FLOAT} {
  yybegin (INPUTD);
   return new Symbol(GFinalCoordSym.INPUT5, new Float(yytext()));
}
}

<INPUTD> {
   {FLOAT} {
  yybegin (INPUT);
   return new Symbol(GFinalCoordSym.INPUT6, new Float(yytext()));
}
}



<IGNOREALL>{
  .|\n {}
}
 
.|\n {}
