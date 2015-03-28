 

%%


%state SCF
%state ITERLABEL
%state ITER
%state TOTALENERGYLABEL
%state TOTALENERGY
%state END
%standalone
/* 
   The name of the class JFlex will create will be Lexer.
   Will write the code to the file Lexer.java. 
*/ 
%class prepPtplot
/* 
   The current line number can be accessed with the variable yyline 
   and the current column number with the variable yycolumn. 
*/ 
%line 
%column 
/* 
   Will switch to a CUP compatibility mode to interface with a CUP 
   generated parser. 
%cup 
*/ 


%8bit


%{
  public static boolean DEBUG = false;
  String name;
%}


/* A line terminator is a \r (carriage return), \n (line feed), or \r\n. */ 
LineTerminator = \r|\n|\r\n 
/* White space is a line terminator, space, tab, or line feed. */ 
WhiteSpace     = {LineTerminator} | [ \t\f] 
/* A literal integer is is a number beginning with a number between 
   one and nine followed by zero or more numbers between 
   zero and nine or just a zero. */ 
dec_int_lit    = 0 | [1-9][0-9]* 
/*  ID declaration  */
ID=[a-zA-Z][a-zA-Z0-9_]*
/*  One digit declaration  */
DIGIT=[0-9]
/*  Other declarations  */
BOOL=[T|F]
INT=[+|-]?{DIGIT}+
FLOAT=[+|-]?{DIGIT}+"."{DIGIT}*(["E"|"e"]([+|-]?){DIGIT}+)?
STRING=[A-Z]+

 
%%


/* here it is */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * */ 
<YYINITIAL>"<SCF>" {
	 System.out.println("<?xml version=\"1.0\" standalone=\"yes\"?>");
	 System.out.println("<!DOCTYPE plot PUBLIC \"-//UC Berkeley//DTD PlotML 1//EN\"");
	 System.out.println("    \"http://ptolemy.eecs.berkeley.edu/xml/dtd/PlotML_1.dtd\">");
	 System.out.println("<plot>");
	 System.out.println("<!-- Ptolemy plot, version 5.1p2 , PlotML format. -->");
	 System.out.println("<title>GAMESS TOTALENERGY plot</title>");
	 System.out.println("<xLabel>iteration</xLabel>");
	 System.out.println("<yLabel>TOTALENERGY</yLabel>");
	 System.out.println("<xRange min=\"0.0\" max=\"15.0\"/>");
	 System.out.println("<yRange min=\"-641.4\" max=\"-639.6\"/>");
	 System.out.println("<default stems=\"yes\"/>");
	 System.out.println("<dataset>");
	 System.out.println("<m x=\"0.0\" y=\"-639.6\"/>");
	 yybegin(ITERLABEL);
}
<ITERLABEL>"<ITER =" {
	 yybegin(ITER);
}
<ITER>{DIGIT}+ {
	 System.out.print("<p x=\""+yytext()+"\" y=\"");
	 yybegin(TOTALENERGYLABEL);
}
<TOTALENERGYLABEL>"<TOTALENERGY>" {
	 yybegin(TOTALENERGY);
}
<TOTALENERGY>{FLOAT} {
	 System.out.println(yytext()+"\"/>");
	 yybegin(ITERLABEL);
}
 
/* End of iteration listings in parseGamess.xml */
<ITERLABEL>"</SCF>" {
	 System.out.println("</dataset>");
	 System.out.println("</plot>");
	 yybegin(END);
}



<YYINITIAL>{ID}	{}
<YYINITIAL>{DIGIT}+ {}
<YYINITIAL>{FLOAT} {}
<YYINITIAL>"#". {}
<YYINITIAL>"'"(.|"\'")*"'" {}



/*  Whitespaces  */
[ \t\n] {}



/*  Other cases  */
. {}
  
