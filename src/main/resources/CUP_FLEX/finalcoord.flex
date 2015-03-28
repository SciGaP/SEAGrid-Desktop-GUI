import java_cup.runtime.*;

%%

%class FinalCoordLexer
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
FinalCoordCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f] 
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}
TraditionalComment = "/*" [^*] ~"*/"
EndOfLineComment = "//" {FinalCoordCharacter}* {LineTerminator}
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
  "Stationary point found" {
	   yybegin(ITER);
	   return new Symbol(FinalCoordSym.FOUNDITER); 
  }
   "Z-Matrix orientation:" {
           yybegin(ITER);
           return new Symbol(FinalCoordSym.FOUNDITER);
  }
}

<ITER>{
  "X           Y           Z" {
            yybegin(INPUTF);
            return new Symbol(FinalCoordSym.INPUT1);}

  "THE_END_OF_FILE" {
	     yybegin(IGNOREALL);
      return new Symbol(FinalCoordSym.SCFDONE);}

  "Standard orientation:" {
             yybegin(IGNOREALL);
      return new Symbol(FinalCoordSym.SCFDONE);}

  .|\n {}

}


<INPUTF> {
  "---------------------------------------------------------------------"
   {
  yybegin (INPUT);
   return new Symbol(FinalCoordSym.DASH1);
}
}     

<INPUT> {
   {INT} {
  yybegin (INPUTA);
   return new Symbol(FinalCoordSym.INPUT2, new Integer(yytext()));
}

  "---------------------------------------------------------------------"
   {
  yybegin (ITER);
   return new Symbol(FinalCoordSym.DASH2);
}

}


<INPUTA> {
   {INT} {
  yybegin (INPUTB);
   return new Symbol(FinalCoordSym.INPUT3, new Integer(yytext()));
}
}

<INPUTB> {
   {INT} {
  yybegin (INPUTC);
   return new Symbol(FinalCoordSym.INPUT4, new Integer(yytext()));
}
}

<INPUTC> {
   {FLOAT} {
  yybegin (INPUTD);
   return new Symbol(FinalCoordSym.INPUT5, new Float(yytext()));
}
}

<INPUTD> {
   {FLOAT} {
  yybegin (INPUTE);
   return new Symbol(FinalCoordSym.INPUT6, new Float(yytext()));
}
}


<INPUTE> {
   {FLOAT} {
  yybegin (INPUT);
   return new Symbol(FinalCoordSym.INPUT7, new Float(yytext()));
}
}




<IGNOREALL>{
  .|\n {}
}
 
.|\n {}
