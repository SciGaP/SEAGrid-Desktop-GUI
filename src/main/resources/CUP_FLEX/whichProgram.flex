

 
%%



%class WhichProgram
%public
%unicode
/*
%cup
%cupdebug
%ignorecase
*/

%state GETREVISION
%state GETVERSION
%state IGNOREALL

%standalone
%8bit

%{
  public static boolean DEBUG = false;
  public static String format;
%}

WORD           = [A-Za-z]+
INT            = [+|-]?[0-9]+
REVISIONNUM    = [A-Za-z0-9"."]+


 
%%



/* ___________
   Description */

<YYINITIAL>{
  "Gaussian 09," {if (Settings.DEBUG) System.out.println(yytext());
		  WhichProgram.format = "Gauss03";
  }
  "GAMESS VERSION =" {if (Settings.DEBUG) System.out.println(yytext());
		      WhichProgram.format = "GAMESS";
		      yybegin(GETVERSION);
  }

  "PROGRAM SYSTEM MOLPRO" {System.out.println(yytext());
                  WhichProgram.format = "Molpro";
                      yybegin(IGNOREALL);
  }


	       
  .|\n {}
}

<GETREVISION>{
  {REVISIONNUM} {if (Settings.DEBUG) System.out.println(yytext());
		 yybegin(IGNOREALL);
  }
}

<GETVERSION>{
  [0-9]{4} {if (Settings.DEBUG) System.out.println(yytext());
            yybegin(IGNOREALL);
  }
}

<IGNOREALL>{
  .|\n {}
}

.|\n {}
  
