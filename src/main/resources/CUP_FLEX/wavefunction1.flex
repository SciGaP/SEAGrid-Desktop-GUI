import java_cup.runtime.*;
import java.io.*;

%%

%class Wavefunction1Lexer
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
  "************" {
    if (Settings.DEBUG) System.out.println("WavefunctionFlex: Found Gaussian 98");
           yybegin(ITER);
	   return new Symbol(WavefunctionSym.FOUNDITER); 
  }
}

<ITER> {
  "geom" {
  if  (Settings.DEBUG) System.out.println("WavefunctionFlex: Found geom ");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}

     yybegin(IGNOREALL);
     return new Symbol(WavefunctionSym.RUNTYP); 
    } 

  "casscf" {
  if  (Settings.DEBUG) System.out.println("WavefunctionFlex: Found geom ");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
 
     yybegin(IGNOREALL);
     return new Symbol(WavefunctionSym.RUNTYP);
    }


  "b3lyp" {
  if  (Settings.DEBUG) System.out.println("WavefunctionFlex: Found geom ");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
 
     yybegin(IGNOREALL);
     return new Symbol(WavefunctionSym.RUNTYP);
    }

    "RHF" {
   if (Settings.DEBUG) System.out.println("WavefunctionFlex: Found RHF");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(WavefunctionSym.RUNTYP);
    } 


    "SCF" {
   if (Settings.DEBUG) System.out.println("WavefunctionFlex: Found RHF");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(WavefunctionSym.RUNTYP);
    }

    "SCFTYP=MCSCF" {
   if (Settings.DEBUG) System.out.println("WavefunctionFlex: Found SCFTYP=MCSCF");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print("mcscf");
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(WavefunctionSym.RUNTYP);
    }

    "SCFTYP=GVB" {
   if (Settings.DEBUG) System.out.println("WavefunctionFlex: Found SCFTYP=GVB");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print("gvb");
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(WavefunctionSym.RUNTYP);
    }

    "MP2" {
   if (Settings.DEBUG) System.out.println("WavefunctionFlex: Found MP2");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(WavefunctionSym.RUNTYP);
    }

    "rhf" {
   if (Settings.DEBUG) System.out.println("WavefunctionFlex: Found rhf");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(WavefunctionSym.RUNTYP);
    }

    "HF-SCF" {
   if (Settings.DEBUG) System.out.println("WavefunctionFlex: Found HF-SCF");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(WavefunctionSym.RUNTYP);
    }

}



<IGNOREALL>{
  .|\n {}
}
 
.|\n {}
