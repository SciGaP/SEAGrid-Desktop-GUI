package GridChem;
import java.util.*;
import GridChem.*;
import GridChem.util.Settings;
 
%%



%class Conf
%public
%unicode
/*
%cup
%cupdebug
%ignorecase
*/

%state SEQ
%state DATAF
%state IGNOREALL

%standalone
%8bit

%{
  public static boolean DEBUG = false;
  public static Set datafileList;
%}

FNAME          = [A-Za-z0-9_"."" ":\-\\\/]+
DIGIT          = [0-9]
INT            = [+|-]?{DIGIT}+
FLOAT          = [+|-]?{DIGIT}+"."{DIGIT}*(["D"|"d"|"E"|"e"]([+|-]?){DIGIT}+)?

 
%%



<YYINITIAL>{
  "qcrjm2002" {if (Settings.DEBUG) System.out.println(yytext());
               Conf.datafileList = new TreeSet();
  }
  "datafile" {yybegin(SEQ);}
}

<SEQ>{
  "=" {yybegin(DATAF);}
}

<DATAF>{
  {FNAME} {Conf.datafileList.add(yytext());
	   for(Iterator myi = Conf.datafileList.iterator(); myi.hasNext();)
             if (Settings.DEBUG) System.out.println(myi.next());
	   if (Settings.DEBUG) System.out.println("----------");
           yybegin(YYINITIAL);
  }
}

<IGNOREALL>{
  .|\n {}
}

.|\n {}
  
