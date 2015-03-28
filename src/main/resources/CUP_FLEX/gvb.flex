import java_cup.runtime.*; 
 
%% 
 
%class GVBLexer 
%public 
%unicode 
%cup  
%cupdebug  
%state SCF1 
%state ITER 
%state ITER2 
%state ITER8 
%state DASH 
%state INTVALUE 
%state FLOATVALUE 
%state ITER4 
%state ITER5 
%state ITER6 
%state ITER7 
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
  "ITER EX     TOTAL ENERGY       E CHANGE        SQCDF       ORB. GRAD" { 
    if (Settings.DEBUG) System.out.println("GVBFlex: Found START OF"); 
	   yybegin(ITER); 
	   return new Symbol(GVBSym.FOUNDITER);  
  } 
  .|\n {} 
} 
 
 
<ITER>{ 
  {dec_int_lit} { 
     if (Settings.DEBUG) System.out.println("GVBFlex: Found the first integer in the iteration"); 
   if (Settings.DEBUG) System.out.println(yytext()); 
   yybegin(ITER2); 
   return new Symbol(GVBSym.INTCycle, new Integer(yytext()));} 
 
  "-----------------" { 
       if (Settings.DEBUG) System.out.println("GVBFlex: Found the dash"); 
               yybegin(DASH);} 
 
}

<ITER2>{                                                                                   {dec_int_lit} {                                                                             if (Settings.DEBUG) System.out.println("GVBFlex: Found the second integer in the iteration");                                                                                     if (Settings.DEBUG) System.out.println(yytext());                                        yybegin(ITER4);                                                                           return new Symbol(GVBSym.INT1, new Integer(yytext()));}                            }               
 
<ITER4>{ 
  {FLOAT} { 
   if (Settings.DEBUG) System.out.println("GVBFlex: Found the energy"); 
   if (Settings.DEBUG) System.out.println(yytext()); 
   yybegin(ITER5); 
   return new Symbol(GVBSym.ENERGY, new Float(yytext()));} 
} 
 
<ITER5>{ 
  {FLOAT} { if (Settings.DEBUG) System.out.println("GVBFlex: ITER5"); 
   if (Settings.DEBUG) System.out.println("GVBFlex: Found float1"); 
   yybegin (ITER6); 
    return new Symbol(GVBSym.FLOAT1); 
} 
} 
 
<ITER6>{ 
  {FLOAT} { 
  if  (Settings.DEBUG) System.out.println("GVBFlex: Found float2"); 
  yybegin (ITER7); 
   return new Symbol(GVBSym.FLOAT2); 
} 
} 
 
  
<ITER7>{ 
  {FLOAT} { 
     if  (Settings.DEBUG) System.out.println("GVBFlex: Found float3"); 
     yybegin(ITER); 
      return new Symbol(GVBSym.FLOAT3);} 
  .|\n {} 
} 
 
 
 
<ITER8>{ 
  {FLOAT} { 
     if  (Settings.DEBUG) System.out.println("GVBFlex: Found float4"); 
     yybegin(ITER); 
      return new Symbol(GVBSym.FLOAT4);} 
  .|\n {} 
} 
 
 
<FLOATVALUE>{ 
 
  .|\n {if  (Settings.DEBUG) System.out.println("GVBFlex: Found new line");} 
  {INT} { 
   if  (Settings.DEBUG) System.out.println("GVBFlex: Found float value"); 
  yybegin(ITER); } 
  "-----------------" { 
               yybegin(DASH); 
  } 
} 
 
<DASH>{ 
 "DENSITY CONVERGED" { 
  yybegin(IGNOREALL); 
  return new Symbol(GVBSym.SCFDONE); 
  } 
 .|\n {}  
} 
 
<IGNOREALL>{ 
  .|\n {} 
} 
 
.|\n {} 
