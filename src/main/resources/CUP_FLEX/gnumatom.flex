import java_cup.runtime.*;

%%

%class GNumAtomLexer
%public
%unicode
%cup 
%cupdebug 
%state ITER
%state ITER1
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
  "TOTAL NUMBER OF ATOMS                        =" {
    if (Settings.DEBUG) System.out.println("GNumAtomFlex: Found Gamess Version");
           yybegin(ITER);
	   return new Symbol(GNumAtomSym.FOUNDITER); 
  }
}

<ITER> {
  {dec_int_lit} {
  if  (Settings.DEBUG) System.out.println("GNumAtomFlex: Found total # of atoms ");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}

     yybegin(IGNOREALL);
     return new Symbol(GNumAtomSym.RUNTYP); 
    } 
}

<IGNOREALL>{
  .|\n {}
}
 
.|\n {}
