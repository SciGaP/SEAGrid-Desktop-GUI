import java_cup.runtime.*;

%%

%class GaussianLexer
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
  "----------------------" {
    if (Settings.DEBUG) System.out.println("GaussianFlex: Found Gaussian 98");
           yybegin(ITER);
	   return new Symbol(GaussianSym.FOUNDITER); 
  }
}

<ITER> {
  "opt " {
  if  (Settings.DEBUG) System.out.println("GaussianFlex: Found opt ");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}

     yybegin(ITER1);
     return new Symbol(GaussianSym.RUNTYP); 
    }

  "MP2" {  
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found MP2");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(ITER1);
     return new Symbol(GaussianSym.RUNTYP);
    }
   
  "MP4" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found MP4");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(ITER1);
     return new Symbol(GaussianSym.RUNTYP);
    }

   "RHF" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found RHF");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(ITER1);
     return new Symbol(GaussianSym.RUNTYP);
    }


   "hf" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found hf");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(ITER1);
     return new Symbol(GaussianSym.RUNTYP);
    }
 
   "rhf" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found rhf");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(ITER1);
     return new Symbol(GaussianSym.RUNTYP);
    }

   "uhf" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found uhf");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(ITER1);
     return new Symbol(GaussianSym.RUNTYP);
    }

    "G1" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found G1");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype1"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(ITER1);
     return new Symbol(GaussianSym.RUNTYP);
    } 

/*
 "---------------------------------------" {
   yybegin(IGNOREALL);
     return new Symbol(GaussianSym.SCFDONE);
   } */
}


 
<ITER1> {
  "opt" {
  if  (Settings.DEBUG) System.out.println("GaussianFlex: Found opt ");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);   
     return new Symbol(GaussianSym.RUNTYP1);
    }
 
  "MP2" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found MP2");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);   
     return new Symbol(GaussianSym.RUNTYP1);
    }
 
   "RHF" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found RHF");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
      yybegin(IGNOREALL);
     return new Symbol(GaussianSym.RUNTYP1);
    }

   "hf" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found rhf");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(GaussianSym.RUNTYP1);
    }
 
   "rhf" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found rhf");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(GaussianSym.RUNTYP1);
    }
   "uhf" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found uhf");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(GaussianSym.RUNTYP1);
    }

   "scf=" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found scf=");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
      yybegin(IGNOREALL);
     return new Symbol(GaussianSym.RUNTYP1);
    }

   "geom" {
   if (Settings.DEBUG) System.out.println("GaussianFlex: Found geom");
   if (Settings.DEBUG) System.out.println(yytext());
 try{
     PrintStream temp = new PrintStream(new FileOutputStream("runtype2"));
     temp.print(yytext());
     System.out.println(yytext());}
   catch (IOException ie){ System.out.println("Error in Gaussian Lexer");}
     yybegin(IGNOREALL);
     return new Symbol(GaussianSym.RUNTYP1);
    }
 
"----------------------" {
   yybegin(IGNOREALL);
    return new Symbol(GaussianSym.SCFDONE);
   }
}


<IGNOREALL>{
  .|\n {}
}
 
.|\n {}
