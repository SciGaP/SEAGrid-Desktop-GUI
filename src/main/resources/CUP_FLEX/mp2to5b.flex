/* MP2 to MP5 Keywords
   Last Update: 12/31/2000 
   http://www.gaussian.com/00000456.htm
*/

import java_cup.runtime.*;


 
%%



%class MP2to5bLexer
%public
%unicode
%cup
%cupdebug
/* %ignorecase */

%state FLOATVAL
%state IGNOREALL

%standalone
%8bit
%{
  public static boolean DEBUG = false;
%}

/* ______
   Macros */
LineTerminator = \r|\n|\r\n 
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f] 

/* ________
   Comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}
TraditionalComment = "/*" [^*] ~"*/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent = ( [^*] | \*+ [^/*] )*         /* adjust syntax font-lock */
Identifier = [:jletter:] [:jletterdigit:]*

/* ________________________________________________________________
   A literal integer is is a number beginning with a number between 
   one and nine followed by zero or more numbers between 
   zero and nine or just a zero. 

   A identifier integer is a word beginning a letter between A and Z, 
   a and z, or an underscore followed by zero or more letters between 
   A and Z, a and z, zero and nine, or an underscore. */ 
dec_int_lit    = 0 | [1-9][0-9]* 
dec_int_id     = [A-Za-z_][A-Za-z_0-9]* 
DIGIT          = [0-9]
FLOAT          = [+|-]?{DIGIT}+"."{DIGIT}*(["D"|"d"|"E"|"e"]([+|-]?){DIGIT}+)?
INT            = [+|-]?{DIGIT}+
BOOL           = [T|F]
WORD           = [A-Za-z]+
WORDLIST       = ["("]? [1A-Za-z]+ (","[A-Za-z]+)* [")"]?
GRAB           = [^(" "|\r|\n|\r\n| \t\f)]+
TOEOL          = ~(\r|\n|\r\n)


 
%%



/*
<YYINITIAL>{
  [R|U]?"MP5" {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP5"{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP5="{WORD} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP5="{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(SD)" {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(SD)"{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(SD)="{WORD} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(SD)="{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(DQ)" {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(DQ)"{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(DQ)="{WORD} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(DQ)="{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(SDQ)" {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(SDQ)"{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(SDQ)="{WORD} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(SDQ)="{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(SDTQ)" {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(SDTQ)"{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(SDTQ)="{WORD} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4(SDTQ)="{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4SDTQ" {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4SDTQ"{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4SDTQ="{WORD} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP4SDTQ="{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP"{INT} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP"{INT}{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP"{INT}"="{WORD} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  [R|U]?"MP"{INT}"="{WORDLIST} {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
.|\n {}			       
}
*/



<YYINITIAL>{
/* ___________________
   Frozen Core Options */

  "FC" {if(Settings.DEBUG) System.out.println("JFlex:mp2to5:  "+yytext());
	return new Symbol(MP2to5bSym.FCOPT, yytext());
  }
  "Full" {if(Settings.DEBUG) System.out.println("JFlex:mp2to5:  "+yytext());
	  return new Symbol(MP2to5bSym.FCOPT, yytext());
  }
  "RW" {if(Settings.DEBUG) System.out.println("JFlex:mp2to5:  "+yytext());
	return new Symbol(MP2to5bSym.FCOPT, yytext());
  }
  "0,0" {if(Settings.DEBUG) System.out.println("JFlex:mp2to5:  "+yytext());
	 return new Symbol(MP2to5bSym.FCOPT2, yytext());
  }
  "5,0" {if(Settings.DEBUG) System.out.println("JFlex:mp2to5:  "+yytext());
	 return new Symbol(MP2to5bSym.FCOPT2, yytext());
  }
  "5,-4" {if(Settings.DEBUG) System.out.println("JFlex:mp2to5:  "+yytext());
	  return new Symbol(MP2to5bSym.FCOPT2, yytext());
  }
  "6,22" {if(Settings.DEBUG) System.out.println("JFlex:mp2to5:  "+yytext());
	  return new Symbol(MP2to5bSym.FCOPT2, yytext());
  }
  "-6" {if(Settings.DEBUG) System.out.println("JFlex:mp2to5:  "+yytext());
	return new Symbol(MP2to5bSym.FCOPT2, yytext());
  }
  "ReadWindow" {if(Settings.DEBUG) System.out.println("JFlex:mp2to5:  "+yytext());
		return new Symbol(MP2to5bSym.FCOPT, yytext());
  }
  "Window=" {if(Settings.DEBUG) System.out.println("JFlex:mp2to5:  "+yytext());
	     return new Symbol(MP2to5bSym.FCOPT, yytext());
  }
  "CASSCF" {if(Settings.DEBUG) System.out.println("JFlex:mp2to5:  "+yytext());
	    return new Symbol(MP2to5bSym.FCOPT, yytext());
  }
  .|\n {}
}

<FLOATVAL>{
  {FLOAT} {if(Settings.DEBUG) System.out.println("JFlex:mp2to5:  "+yytext());
	   yybegin(YYINITIAL);
	   return new Symbol(MP2to5bSym.FLOAT, yytext());
  }
}


<IGNOREALL>{
  .|\n {}
}

.|\n {}
  
