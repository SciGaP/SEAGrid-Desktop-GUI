import java_cup.runtime.*;

%%

%class Method1Lexer
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
  "******************************" {
    if (Settings.DEBUG) System.out.println("MethodFlex: Found Gaussian 98");
           yybegin(ITER);
	   return new Symbol(MethodSym.FOUNDITER); 
  }
}

<ITER> {
  "OPTG" {
   System.out.println("MethodFlex: Found opt ");
   System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}

     yybegin(IGNOREALL);
     return new Symbol(MethodSym.RUNTYP); 
    }
/*
 "scf" {
  if  (Settings.DEBUG) System.out.println("MethodFlex: Found scf ");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
 
     yybegin(IGNOREALL);
     return new Symbol(MethodSym.RUNTYP);
    }
*/

    "G1" {
   if (Settings.DEBUG) System.out.println("MethodFlex: Found G1");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(MethodSym.RUNTYP);
    } 

    "G2" {
   if (Settings.DEBUG) System.out.println("MethodFlex: Found G2");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(MethodSym.RUNTYP);
    }

    "CBS-Q" {
   if (Settings.DEBUG) System.out.println("MethodFlex: Found CBS-Q");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(MethodSym.RUNTYP);
    }

    "RUNTYP=ENERGY" {
   if (Settings.DEBUG) System.out.println("MethodFlex: RUNTYP=ENERGY");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print("energy");
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(MethodSym.RUNTYP);
    }

    "RUNTYP=GRADIENT" {
   if (Settings.DEBUG) System.out.println("MethodFlex: RUNTYP=GRADIENT");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print("gradient");
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(MethodSym.RUNTYP);
}
  
    "RUNTYP=OPTIMIZE" {
   if (Settings.DEBUG) System.out.println("MethodFlex: RUNTYP=ENERGY");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print("optimize");
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(MethodSym.RUNTYP);
    }
/*
    "OPTG" {
    System.out.println("MethodFlex: Molpro OPTG");
   System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print("optimize");
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(MethodSym.RUNTYP);
    }
*/
}


<IGNOREALL>{
  .|\n {}
}
 
.|\n {}
