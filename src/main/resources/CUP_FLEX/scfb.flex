import java_cup.runtime.*;

%%

%class SCFbLexer
%public
%unicode
%cup 
%cupdebug 
%state SCF1
%state SCF2
%state SCF3
%state EQ3
%state LENXVALUE
%state INTVALUE
%state FLOATVALUE
%state FLOATVALUE3
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
FLOAT          = [+|-]?{DIGIT}+"."{DIGIT}*(["D"|"d"|"E"|"e"]([+|-]?){DIGIT}+)?
INT            = [+|-]?{DIGIT}+
BOOL           = [T|F]
EQ             = "="
STRING         = [A-Z]+
GRAB           = [^(" "|\r|\n|\r\n| \t\f)]+

%%

<YYINITIAL>{
  "Closed shell SCF:" {
		       yybegin(SCF1);
		       return new Symbol(SCFbSym.FOUNDSCF); 
  }
  "open shell SCF:" {
		       yybegin(SCF1);
		       return new Symbol(SCFbSym.FOUNDSCF); 
  }
  .|\n {}
}

<SCF1>{
  "LenX=" {
	   yybegin(LENXVALUE);
	   return new Symbol(SCFbSym.FOUNDLENX); 
  }
  .|\n {}
}

<LENXVALUE>{
  {INT} {
	 yybegin(SCF2);
	 return new Symbol(SCFbSym.LENXINT, yytext()); 
  }
}

<SCF2>{
  "SCF Done:" {
	       yybegin(SCF3);
	       return new Symbol(SCFbSym.SCFDONE); 
  }
  .|\n {}
}

<SCF3>{
  "E(RHF)" {
            yybegin(EQ3);
	    return new Symbol(SCFbSym.ERHF); 
  }
  "A.U. after" {return new Symbol(SCFbSym.AU);}
  "cycles" {return new Symbol(SCFbSym.DUMMY);}
  "Convg" {
           yybegin(EQ3);
           return new Symbol(SCFbSym.CONVG); 
  }
  "-V/T" {
          yybegin(EQ3);
	  return new Symbol(SCFbSym.MINUSVT); 
  }
  "S**2" {
          yybegin(EQ3);
	  return new Symbol(SCFbSym.S2); 
  }
  "KE" {
        yybegin(EQ3);
	return new Symbol(SCFbSym.KE); 
  }
  "PE" {
        yybegin(EQ3);
	return new Symbol(SCFbSym.PE); 
  }
  "EE" {
	yybegin(EQ3);
	return new Symbol(SCFbSym.EE); 
  }
  "Leave Link" {
		yybegin(IGNOREALL);
		return new Symbol(SCFbSym.LEAVE); 
  }
  {INT} {return new Symbol(SCFbSym.INT, yytext());}
  .|\n {}
}

<INTVALUE>{
  {INT} {
	 yybegin(SCF2);
	 return new Symbol(SCFbSym.INT, yytext()); 
  }
}

<FLOATVALUE>{
  {FLOAT} {
	   yybegin(SCF2);
	   return new Symbol(SCFbSym.FLOAT, yytext()); 
  }
}

<EQ3>{
  {EQ} {
	   yybegin(FLOATVALUE3);
	   return new Symbol(SCFbSym.EQUALS); 
  }
}

<FLOATVALUE3>{
  {FLOAT} {
	   yybegin(SCF3);
	   return new Symbol(SCFbSym.FLOAT, yytext()); 
  }
}

<IGNOREALL>{
  .|\n {}
}

.|\n {}
