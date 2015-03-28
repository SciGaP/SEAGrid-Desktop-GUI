import java_cup.runtime.*;

%%

%class MFinalCoordLexer
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
MFinalCoordCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f] 
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}
TraditionalComment = "/*" [^*] ~"*/"
EndOfLineComment = "//" {MFinalCoordCharacter}* {LineTerminator}
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent = ( [^*] | \*+ [^/*] )*        /* adjust syntax font-coloring */
Identifier = [:jletter:] [:jletterdigit:]*
dec_int_lit    = 0 | [1-9][0-9]* 
dec_int_id     = [A-Za-z_] | [A-Za-z][0-9]* 
DIGIT          = [0-9]
FLOAT          = [+|-]?{DIGIT}+"."{DIGIT}+
INT            = [+|-]?{DIGIT}+
BOOL           = [T|F]
EQ             = "="
STRING         = [A-Z]+
GRAB           = [^(" "|\r|\n|\r\n| \t\f)]+

%%

<YYINITIAL>{
  "ATOMIC COORDINATES" {
	   yybegin(ITER);
	   return new Symbol(MFinalCoordSym.FOUNDITER); 
  }
   "Standard orientation:" {
           yybegin(ITER);
           return new Symbol(MFinalCoordSym.FOUNDITER);
  }
}

<ITER>{
  "NR  ATOM    CHARGE " {
            yybegin(INPUTF);
            return new Symbol(MFinalCoordSym.INPUT1);}

  "THE_END_OF_FILE" {
	     yybegin(IGNOREALL);
      return new Symbol(MFinalCoordSym.SCFDONE);}

  "Bond lengths in Bohr" {
             yybegin(IGNOREALL);
      return new Symbol(MFinalCoordSym.SCFDONE);}

  .|\n {}

}


<INPUTF> {
  "X              Y              Z"
   {
  yybegin (INPUT);
   return new Symbol(MFinalCoordSym.DASH1);
}
}     

<INPUT> {
   {INT} {
  yybegin (INPUTA);
   return new Symbol(MFinalCoordSym.INPUT2, new Integer(yytext()));
}

  "---------------------------------------------------------------------"
   {
  yybegin (ITER);
   return new Symbol(MFinalCoordSym.DASH2);
}

}


<INPUTA> {
   {dec_int_id} {
  yybegin (INPUTB);
   return new Symbol(MFinalCoordSym.INPUT3);
}
}

<INPUTB> {
   {FLOAT} {
  yybegin (INPUTC);
   return new Symbol(MFinalCoordSym.INPUT4, new Float(yytext()));
}
}

<INPUTC> {
   {FLOAT} {
  yybegin (INPUTD);
   return new Symbol(MFinalCoordSym.INPUT5, new Float(yytext()));
}
}

<INPUTD> {
   {FLOAT} {
  yybegin (INPUTE);
   return new Symbol(MFinalCoordSym.INPUT6, new Float(yytext()));
}
}


<INPUTE> {
   {FLOAT} {
  yybegin (INPUT);
   return new Symbol(MFinalCoordSym.INPUT7, new Float(yytext()));
}
}




<IGNOREALL>{
  .|\n {}
}
 
.|\n {}
