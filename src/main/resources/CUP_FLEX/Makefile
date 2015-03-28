# GridChem CUP_FLEX Makefile.
# First order draft.  Perhaps this will be replaced by ant.
# Scott Brozell, OSC, May 10, 2005

JAVA=java
JAVAC=javac
JAVA_CUP=java_cup
JAVA_CUP_MAIN=$(JAVA_CUP).Main
JAVA_CUP_PARENT_PATH=..
JFLEX=JFlex
JFLEX_MAIN=$(JFLEX).Main
JFLEX_PARENT_PATH=..
LEXER_CLASS_NAME_TAIL=Lexer
PARSER_CLASS_NAME_TAIL=Parser
SCANNER_PARSER_BASENAMES=whichProgram
SYMBOLS_CLASS_NAME_TAIL=Sym

# This target builds all the parsers.
# It assumes that java_cup and JFlex are built, but they could be dependencies.
all: $(SCANNER_PARSER_BASENAMES)

# This target builds the Java parser generator CUP.
java_cup: 
	cd $(JAVA_CUP_PARENT_PATH); \
	$(JAVAC) $(JAVA_CUP)/*.java $(JAVA_CUP)/runtime/*.java

# This target builds the Java lexical analyzer generator JFlex.
# The java_cup runtime package is required; thus the classpath specification.
jflex: 
	cd $(JFLEX_PARENT_PATH); \
	$(JAVAC) -classpath . $(JFLEX)/*.java $(JFLEX)/gui/*.java

# These targets build parsers by invoking CUP, via the method java_cup.Main.
# Note the annoying lack of consistency between file and class names.

SCFa: scfaLexer scfaParserandSym 

whichProgram: WhichProgram

WhichProgram: whichProgram.cup whichProgram.flex
	$(JAVA) -classpath $(JAVA_CUP_PARENT_PATH) $(JAVA_CUP_MAIN) \
	    -parser $@$(PARSER_CLASS_NAME_TAIL) \
	    -symbols $@$(SYMBOLS_CLASS_NAME_TAIL) < whichProgram.cup
	$(JAVA) -classpath $(JFLEX_PARENT_PATH) $(JFLEX_MAIN) whichProgram.flex


%$(LEXER_CLASS_NAME_TAIL): %.flex
	$(JAVA) -classpath $(JFLEX_PARENT_PATH) $(JFLEX_MAIN) $<

%$(PARSER_CLASS_NAME_TAIL)and$(SYMBOLS_CLASS_NAME_TAIL): %.cup
	$(JAVA) -classpath $(JAVA_CUP_PARENT_PATH) $(JAVA_CUP_MAIN) \
	    -parser $*$(PARSER_CLASS_NAME_TAIL) \
	    -symbols $*$(SYMBOLS_CLASS_NAME_TAIL) < $<

install: all
	mv *.java ../GridChem/

clean:
	-$(RM) *.java *.java~

