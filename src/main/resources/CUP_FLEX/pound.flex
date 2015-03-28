/* Currently handles Gauss98 test inputs test000.com -- test039.com
 */
   import java_cup.runtime.*;


 
%%



%class Pound
%public
%unicode
%cup
%cupdebug
%ignorecase

%state POUND
%state IGNOREALL

%standalone
%8bit

%{
  public static boolean DEBUG = false;
  LexerParserBroker pp = new LexerParserBroker();
  private String prefix = "file:" 
  + System.getProperty("user.dir")
    + System.getProperty("file.separator");
%}

DIGIT          = [0-9]
FLOAT          = [+|-]?{DIGIT}+"."{DIGIT}*(["D"|"d"|"E"|"e"]([+|-]?){DIGIT}+)?
INT            = [+|-]?{DIGIT}+
WORD           = [0-9A-Za-z"="".""("")""+""-"_]+
WORDLIST       = "("   {WORD} ( ","{WORD}+ )*   ")"


 
%%



/*
   =====================================================================
   Tokens for lexical analysis of pound-headers
   ===================================================================== 
*/

<YYINITIAL>{
  "--link"[0-9]"--" {yybegin(YYINITIAL);
                     return new Symbol(PoundSym.LINK);
  }
  "%"{WORD} {yybegin(YYINITIAL);
             return new Symbol(PoundSym.PERCENT_SPEC);
  }
  "# SP," {if (Settings.DEBUG) System.out.println("JFlex:pound:YYINITIAL: Found # SP,..........");
           pp.route("route section", yytext(), prefix + "00000471.htm");
	   yybegin(POUND); 
           return new Symbol(PoundSym.POUNDLINE);
  }
  "# " {if (Settings.DEBUG) System.out.println("JFlex:pound:YYINITIAL: Found #..........");
	pp.route("route section", yytext(), prefix + "00000414.htm");
	yybegin(POUND); 
        return new Symbol(PoundSym.POUNDLINE);
  }
  "#N" {if (Settings.DEBUG) System.out.println("JFlex:pound:YYINITIAL: Found #N..........");
	pp.route("route section", yytext(), prefix + "00000414.htm");
	yybegin(POUND); 
        return new Symbol(PoundSym.POUNDLINE);
  }
  "#P" {if (Settings.DEBUG) System.out.println("JFlex:pound:YYINITIAL: Found #P..........");
	pp.route("route section", yytext(), prefix + "00000414.htm");
	yybegin(POUND); 
        return new Symbol(PoundSym.POUNDLINE);
  }
  "#T" {if (Settings.DEBUG) System.out.println("JFlex:pound:YYINITIAL: Found #T..........");
	pp.route("route section", yytext(), prefix + "00000414.htm");
	yybegin(POUND); 
        return new Symbol(PoundSym.POUNDLINE);
  }
}

<POUND>{
/* ________
   obsolete
*/
"Alter" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"BD-T" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"BeckeHalfandHalf" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"Camp-King" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"CCSD-T" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"CubeDensity" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"Cube=Divergence" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"GridDensity" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"Guess=Restart" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"Opt=AddRedundant" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"OptCyc=" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"PlotDensity" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"Prop=Grid" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"QCISD-T" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"QCID" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"QCSCF" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"Save" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"SCFCon" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"SCFCyc" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"SCFDM" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"SCFQC" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.obsolete("obsolete procedure", yytext(), prefix + "00000473.htm");
  }
"Trajectory" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                pp.system("trajectory", yytext(), "trajkey.htm");
  }
/* _______
   methods
*/
  "HF" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "HF"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "HF="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "HF="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "RHF" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "restricted Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "RHF"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "restricted Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "RHF="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "restricted Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "RHF="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "restricted Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "UHF" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "unrestricted Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "UHF"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "unrestricted Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "UHF="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "unrestricted Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "UHF="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "unrestricted Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "rohf" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "spin-restricted open-shell Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "rohf"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "spin-restricted open-shell Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "rohf="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "spin-restricted open-shell Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "rohf="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "spin-restricted open-shell Hartree-Fock", yytext(), prefix + "00000445.htm");
  }
  "CPHF" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "coupled Hartree-Fock", yytext(), prefix + "00000429.htm");
  }
  "CPHF"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "coupled Hartree-Fock", yytext(), prefix + "00000429.htm");
  }
  "CPHF="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "coupled Hartree-Fock", yytext(), prefix + "00000429.htm");
  }
  "CPHF="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("hf", "coupled Hartree-Fock", yytext(), prefix + "00000429.htm");
  }
  "BD" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("bd", "Brueckner-Doubles", yytext(), prefix + "00000418.htm");
  }
  "BD"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("bd", "Brueckner-Doubles", yytext(), prefix + "00000418.htm");
  }
  "BD="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("bd", "Brueckner-Doubles", yytext(), prefix + "00000418.htm");
  }
  "BD="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("bd", "Brueckner-Doubles", yytext(), prefix + "00000418.htm");
  }
  "CBS-"{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("cbs", "complete basis set method", yytext(), prefix + "00000420.htm");
  }
  "CBSExtrapolate" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("cbs", "complete basis set extrapolation of MP2", yytext(), prefix + "00000421.htm");
  }
  "COMPLEX" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("complex", "complex molecular orbitals", yytext(), prefix + "00000428.htm");
  }
  "CCD" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ccd", "coupled clusters w/ double substitution", yytext(), prefix + "00000422.htm");
  }
  "CCD"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ccd", "coupled clusters w/ double substitution", yytext(), prefix + "00000422.htm");
  }
  "CCD="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ccd", "coupled clusters w/ double substitution", yytext(), prefix + "00000422.htm");
  }
  "CCD="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ccd", "coupled clusters w/ double substitution", yytext(), prefix + "00000422.htm");
  }
  "CCSD" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ccd", "coupled clusters w/ single/double substitution", yytext(), prefix + "00000422.htm");
  }
  "CCSD"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ccd", "coupled clusters w/ single/double substitution", yytext(), prefix + "00000422.htm");
  }
  "CCSD="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ccd", "coupled clusters w/ single/double substitution", yytext(), prefix + "00000422.htm");
  }
  "CCSD="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ccd", "coupled clusters w/ single/double substitution", yytext(), prefix + "00000422.htm");
  }
/* 
   DFT Methods Combine R or U for Restricted and Unrestricted with Exchange Functionals from 
     S (SLATER), XA(XALPHA), B88 (Becke88),B1, B3, PW91(Perdew-Wang91), MPW(ModifiedPW), G96(Gill96), PBE(PerdewBurkeErnzerof), O(OPTX[Handy]), TPSS (Tao etal..)  WITH CORRELATION FUNCTIONALS FROM 
     VWN, VWN5, LYP, PL, P86, PW91, B95, PBE, TPSS, VP86, V5LYP, VSCX, HCTH/[93,407,147] and Hybrids

    B3, B98, B1, B971, B972, PBE1, O3LYP, BHandH, BHandHLYP,  

*/
  [R|U]?["S"|"XA"|"B88"|"B1"|"B3"|"PW91"|"MPW"|"G96"|"PBE"|"O"|"TPSS"]?["VWN"|"VWN5"|"LYP"|"PL"|"P86"|"PW91"|"B95"|"PBE"|"TPSS"|"VP86"|"V5LYP"|"VSCX"|"HCTH93"|"HCTH147"|"HCTH407"] {if(Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("DFT", " STANDARD COMBINATION METHOD", yytext(), prefix + "00000422.htm");
  }
  [R|U]?["B98"|"B971"|"B972"|"PBE1"|"O3LYP"|"BHANDH"|"BHANDHLYP"]  {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("DFT", "HYBRID COMBINATION METHOD", yytext(), prefix + "00000422.htm");
}

  [R|U]?"CI" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ single & double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CI"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ single & double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CI="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ single & double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CI="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ single & double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CID" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CID"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CID="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CID="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CIS" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ single excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CIS"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ single excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CIS="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ single excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CIS="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ single excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CISD" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ single & double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CISD"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ single & double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CISD="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ single & double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"CISD="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "configuration interaction w/ single & double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"QCISD(T)" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "quadratic CI w/ single & double excitations & triples", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"QCISD(T)"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "quadratic CI w/ single & double excitations & triples", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"QCISD(T)="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "quadratic CI w/ single & double excitations & triples", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"QCISD(T)="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "quadratic CI w/ single & double excitations & triples", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"QCISD" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "quadratic CI w/ single & double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"QCISD"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "quadratic CI w/ single & double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"QCISD="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "quadratic CI w/ single & double excitations", yytext(), prefix + "00000423.htm");
  }
  [R|U]?"QCISD="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ci", "quadratic CI w/ single & double excitations", yytext(), prefix + "00000423.htm");
  }
  "FMM" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("fmm", "fast multipole method", yytext(), prefix + "fmmkey.htm");
  }
  "force" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("force", "energy gradient on nuclei", yytext(), prefix + "00000436.htm");
  }
  "FREQ" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("freq", "vibrational frequencies", yytext(), prefix + "00000437.htm");
  }	
  "FREQ"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("freq", "vibrational frequencies", yytext(), prefix + "00000437.htm");
  }	
  "FREQ="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("freq", "vibrational frequencies", yytext(), prefix + "00000437.htm");
  }	
  "FREQ="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("freq", "vibrational frequencies", yytext(), prefix + "00000437.htm");
  }	
  "Guess" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("guess", "initial guess for HF wavefunction", yytext(), prefix + "00000443.htm");
  }
  "Guess"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("guess", "initial guess for HF wavefunction", yytext(), prefix + "00000443.htm");
  }
  "Guess="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("guess", "initial guess for HF wavefunction", yytext(), prefix + "00000443.htm");
  }
  "Guess="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("guess", "initial guess for HF wavefunction", yytext(), prefix + "00000443.htm");
  }
  "G1" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("g1", "Gaussian-1 method", yytext(), prefix + "00000438.htm");
  }
  "G1"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("g1", "Gaussian-1 method", yytext(), prefix + "00000438.htm");
  }
  "G1="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("g1", "Gaussian-1 method", yytext(), prefix + "00000438.htm");
  }
  "G1="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("g1", "Gaussian-1 method", yytext(), prefix + "00000438.htm");
  }
  "G2" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("g1", "Gaussian-2 method", yytext(), prefix + "00000438.htm");
  }
  "G2"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("g1", "Gaussian-2 method", yytext(), prefix + "00000438.htm");
  }
  "G2="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("g1", "Gaussian-2 method", yytext(), prefix + "00000438.htm");
  }
  "G2="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("g1", "Gaussian-2 method", yytext(), prefix + "00000438.htm");
  }
  "G2MP2" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("g1", "Gaussian-2 w/ MP2 corrections to basis extensions", yytext(), prefix + "00000438.htm");
  }
  "G2MP2"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("g1", "Gaussian-2 w/ MP2 corrections to basis extensions", yytext(), prefix + "00000438.htm");
  }
  "G2MP2="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("g1", "Gaussian-2 w/ MP2 corrections to basis extensions", yytext(), prefix + "00000438.htm");
  }
  "G2MP2="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("g1", "Gaussian-2 w/ MP2 corrections to basis extensions", yytext(), prefix + "00000438.htm");
  }
  "GVB" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("gvb", "general valence bond", yytext(), prefix + "00000444.htm");
  }
  "GVB"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("gvb", "general valence bond", yytext(), prefix + "00000444.htm");
  }
  "GVB="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("gvb", "general valence bond", yytext(), prefix + "00000444.htm");
  }
  "GVB="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("gvb", "general valence bond", yytext(), prefix + "00000444.htm");
  }
  "INDO" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("indo", "semi-empirical INDO Hamiltonian", yytext(), prefix + "00000446.htm");
  }
  "INDO"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("indo", "semi-empirical INDO Hamiltonian", yytext(), prefix + "00000446.htm");
  }
  "INDO="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("indo", "semi-empirical INDO Hamiltonian", yytext(), prefix + "00000446.htm");
  }
  "INDO="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("indo", "semi-empirical INDO Hamiltonian", yytext(), prefix + "00000446.htm");
  }
  "Integral" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("integral", "two-electron integrals & derivatives", yytext(), prefix + "00000447.htm");
  }
  "Integral"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("integral", "two-electron integrals & derivatives", yytext(), prefix + "00000447.htm");
  }
  "Integral="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("integral", "two-electron integrals & derivatives", yytext(), prefix + "00000447.htm");
  }
  "Integral="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("integral", "two-electron integrals & derivatives", yytext(), prefix + "00000447.htm");
  }
  "MINDO3" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mindo3", "semi-empirical MINDO3 Hamiltonian", yytext(), prefix + "00000454.htm");
  }
  "MINDO3"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mindo3", "semi-empirical MINDO3 Hamiltonian", yytext(), prefix + "00000454.htm");
  }
  "MINDO3="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mindo3", "semi-empirical MINDO3 Hamiltonian", yytext(), prefix + "00000454.htm");
  }
  "MINDO3="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mindo3", "semi-empirical MINDO3 Hamiltonian", yytext(), prefix + "00000454.htm");
  }
  "MNDO" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mndo", "semi-empirical MNDO Hamiltonian", yytext(), prefix + "00000455.htm");
  }
  "MNDO"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mndo", "semi-empirical MNDO Hamiltonian", yytext(), prefix + "00000455.htm");
  }
  "MNDO="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mndo", "semi-empirical MNDO Hamiltonian", yytext(), prefix + "00000455.htm");
  }
  "MNDO="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mndo", "semi-empirical MNDO Hamiltonian", yytext(), prefix + "00000455.htm");
  }
  [R|U]?"MP5" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP5"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP5="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP5="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(SD)" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(SD)"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(SD)="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(SD)="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(DQ)" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(DQ)"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(DQ)="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(DQ)="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(SDQ)" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(SDQ)"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(SDQ)="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(SDQ)="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(SDTQ)" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(SDTQ)"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(SDTQ)="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4(SDTQ)="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4SDTQ" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4SDTQ"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4SDTQ="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP4SDTQ="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy w/ substitutions", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP"{INT} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy correction", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP"{INT}{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy correction", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP"{INT}"="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy correction", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"MP"{INT}"="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mp", "HF with Moeller-Plesset correlation energy correction", yytext(), prefix + "00000456.htm");
  }
  [R|U]?"OVGF" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ovgf", "outer-valence Green function", yytext(), prefix + "00000462.htm");
  }
  [R|U]?"OVGF"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ovgf", "outer-valence Green function", yytext(), prefix + "00000462.htm");
  }
  [R|U]?"OVGF="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ovgf", "outer-valence Green function", yytext(), prefix + "00000462.htm");
  }
  [R|U]?"OVGF="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("ovgf", "outer-valence Green function", yytext(), prefix + "00000462.htm");
  }
  "PM3" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("pm3", "semi-empirical PM3 Hamiltonian", yytext(), prefix + "00000463.htm");
  }
  "PM3MM" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("pm3", "semi-empirical PM3 Hamiltonian w/ molecular mechanics", yytext(), prefix + "00000463.htm");
  }
  "pop" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("pop", "molecular orbital & populations analysis", yytext(), prefix + "00000465.htm");
  }
  "pop"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("pop", "molecular orbital & populations analysis", yytext(), prefix + "00000465.htm");
  }
  "pop="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("pop", "molecular orbital & populations analysis", yytext(), prefix + "00000465.htm");
  }
  "pop="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("pop", "molecular orbital & populations analysis", yytext(), prefix + "00000465.htm");
  }
  "popu" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("pop", "molecular orbital & populations analysis", yytext(), prefix + "00000465.htm");
  }
  "popu"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("pop", "molecular orbital & populations analysis", yytext(), prefix + "00000465.htm");
  }
  "popu="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("pop", "molecular orbital & populations analysis", yytext(), prefix + "00000465.htm");
  }
  "popu="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("pop", "molecular orbital & populations analysis", yytext(), prefix + "00000465.htm");
  }
  "Scan" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("scan", "potential energy surface scan", yytext(), prefix + "00000472.htm.htm");
  }	
  "Sparse" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("sparse", "sparse matrix storage", yytext(), prefix + "sparsek.htm");
  }	
  "STABLE" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("stable", "test stability of Hartree-Fock or density functional", yytext(), prefix + "00000475.htm");
  }	
  "STABLE"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("stable", "test stability of Hartree-Fock or density functional", yytext(), prefix + "00000475.htm");
  }	
  "STABLE="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("stable", "test stability of Hartree-Fock or density functional", yytext(), prefix + "00000475.htm");
  }	
  "STABLE="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("stable", "test stability of Hartree-Fock or density functional", yytext(), prefix + "00000475.htm");
  }	
  "Symm" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("symm", "use molecular symmetry", yytext(), prefix + "00000476.htm");
  }	
  "TD" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("rpa", "time-dependent excited-state with random-phase approx.", yytext(), prefix + "rpak.htm");
  }	
  "TestMO" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
            pp.method("testmo", "test molecular orbital coefficients", yytext(), prefix + "00000477.htm");
  }
  "Transformation" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                    pp.method("trans", "algorithm for integral transformations", yytext(), prefix + "00000480.htm");
  }
  "Transformation"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                              pp.method("trans", "algorithm for integral transformations", yytext(), prefix + "00000480.htm");
  }
  "Transformation="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                           pp.method("trans", "algorithm for integral transformations", yytext(), prefix + "00000480.htm");
  }
  "Transformation="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                              pp.method("trans", "algorithm for integral transformations", yytext(), prefix + "00000480.htm");
  }
  "Zindo" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("zindo", "excited-state w/ ZINDO-1", yytext(), prefix + "zindok.htm");
  }
  "Zindo"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("zindo", "excited-state w/ ZINDO-1", yytext(), prefix + "zindok.htm");
  }
  "Zindo="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("zindo", "excited-state w/ ZINDO-1", yytext(), prefix + "zindok.htm");
  }
  "Zindo="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("zindo", "excited-state w/ ZINDO-1", yytext(), prefix + "zindok.htm");
  }
  "AM1" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("am1", "semi-empirical AM1 Hamiltonian", yytext(), prefix + "00000416.htm");
  }
  "Amber" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mm", "molecular mechanics w/ Amber force field", yytext(), prefix + "mmmk.htm");
  }
  "DREIDING" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mm", "molecular mechanics w/ DREIDING force field", yytext(), prefix + "mmmk.htm");
  }
  "UFF" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("mm", "molecular mechanics w/ UFF force field", yytext(), prefix + "mmmk.htm");
  }
  "/" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  "//" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  }
  "RPA" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("rpa", "time-dependent excited-state with random-phase approx.", yytext(), prefix + "rpak.htm");
  }
  "RPA"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("rpa", "time-dependent excited-state with random-phase approx.", yytext(), prefix + "rpak.htm");
  }
  "RPA="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("rpa", "time-dependent excited-state with random-phase approx.", yytext(), prefix + "rpak.htm");
  }
  "RPA="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.method("rpa", "time-dependent excited-state with random-phase approx.", yytext(), prefix + "rpak.htm");
  }
/* _____
   basis
*/
  "ChkBasis" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("read basis set from checkpoint file", yytext(), prefix + "00000425.htm");
  }
  "ExtraBasis" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("add additional basis functions", yytext(), prefix + "00000433.htm");
  }
  "gen" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("user-specified basis set", yytext(), prefix + "00000439.htm");
  }	
  "Massage" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("modify molecule specification after generation", yytext(), prefix + "00000452.htm");
  }
  {INT}"-"{INT}"G"["*"]?["*"]? {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("internally stored Gaussian 98 basis set", yytext(), prefix + "00000485.htm");
  }	
  "cc-pV"[DTQ56]"Z" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("Dunning's correlation consistent basis sets", yytext(), prefix + "00000485.htm");
  }	
  "CEP-"{INT}"G"["*"]?["*"]? {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("Stephens/Basch/Krauss ECP basis", yytext(), prefix + "00000485.htm");
  }	
  "D95"[V]? {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("Dunning/Huzinaga double zeta basis", yytext(), prefix + "00000485.htm");
  }	
  "Dcc-pV"[DTQ56]"Z" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("Dunning's correlation consistent basis sets", yytext(), prefix + "00000485.htm");
  }	
  "EPR" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("basis sets of Barone for hyperfine coupling constants", yytext(), prefix + "00000485.htm");
  }	
  "LanL2MB" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("Los Alamos ECP plus MBS on Na-Bi", yytext(), prefix + "00000485.htm");
  }	
  "LanL2DZ" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("Los Alamos ECP plus DZ on Na-Bi", yytext(), prefix + "00000485.htm");
  }	
  "LP-"{INT}"G"["*"]?["*"]? {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("LP-*G basis", yytext(), prefix + "00000485.htm");
  }	
  "MidiX" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("Midi! basis of Truhlar, et al.", yytext(), prefix + "00000485.htm");
  }	
  "STO-"{INT}"G"["*"]?["*"]? {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("internally stored Gaussian 98 basis set", yytext(), prefix + "00000485.htm");
  }	
  "SDD:D95"[V]? {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("Stuttgart/Dresden ECP basis", yytext(), prefix + "00000485.htm");
  }	
  "SEC" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("Goddard/Smedley ECP basis", yytext(), prefix + "00000485.htm");
  }	
  "SHC" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("Goddard/Smedley ECP basis", yytext(), prefix + "00000485.htm");
  }	
  "SV"[P]? {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("basis of Ahlrichs, et al.", yytext(), prefix + "00000485.htm");
  }	
  "TZV" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.basis("basis of Ahlrichs, et al.", yytext(), prefix + "00000485.htm");
  }	
/* ___
   SCF
*/
  "scf" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.scf("self-consistent field procedure", yytext(), prefix + "00000473.htm");
  }
  "scf"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.scf("self-consistent field procedure", yytext(), prefix + "00000473.htm");
  }
  "scf="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.scf("self-consistent field procedure", yytext(), prefix + "00000473.htm");
  }
  "scf="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.scf("self-consistent field procedure", yytext(), prefix + "00000473.htm");
  }
  "CASSCF" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.scf("casscf", yytext(), prefix + "00000419.htm");
  }
  "CASSCF"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.scf("casscf", yytext(), prefix + "00000419.htm");
  }
  "CASSCF="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.scf("casscf", yytext(), prefix + "00000419.htm");
  }
  "CASSCF="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.scf("casscf", yytext(), prefix + "00000419.htm");
  }
/* ___________________
   material properties
*/
  "AIM" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("molecular properties by theory of atoms in molecules", yytext(), prefix + "00000415.htm");
  }
  "AIM"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("molecular properties by theory of atoms in molecules", yytext(), prefix + "00000415.htm");
  }
  "AIM="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("molecular properties by theory of atoms in molecules", yytext(), prefix + "00000415.htm");
  }
  "AIM="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("molecular properties by theory of atoms in molecules", yytext(), prefix + "00000415.htm");
  }
  "Charge" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("background charge distribution", yytext(), prefix + "00000424.htm");
  }
  "Charge"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("background charge distribution", yytext(), prefix + "00000424.htm");
  }
  "Charge="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("background charge distribution", yytext(), prefix + "00000424.htm");
  }
  "Charge="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("background charge distribution", yytext(), prefix + "00000424.htm");
  }
  "DFT" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("density functional theory model",  yytext(), prefix + "00000432.htm");
  }
  "DFT"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("density functional theory model",  yytext(), prefix + "00000432.htm");
  }
  "DFT="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("density functional theory model",  yytext(), prefix + "00000432.htm");
  }
  "DFT="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("density functional theory model",  yytext(), prefix + "00000432.htm");
  }
  "Field" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("electric multipole or Fermi contact fields", yytext(), prefix + "00000434.htm");
  }
  "Field"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("electric multipole or Fermi contact fields", yytext(), prefix + "00000434.htm");
  }
  "Field="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("electric multipole or Fermi contact fields", yytext(), prefix + "00000434.htm");
  }
  "Field="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("electric multipole or Fermi contact fields", yytext(), prefix + "00000434.htm");
  }
  "IRC" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("follow reaction path", yytext(), prefix + "00000449.htm");
  }
  "IRC"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("follow reaction path", yytext(), prefix + "00000449.htm");
  }
  "IRC="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("follow reaction path", yytext(), prefix + "00000449.htm");
  }
  "IRC="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("follow reaction path", yytext(), prefix + "00000449.htm");
  }
  "IRCMax" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("IRCMax calculation of Petersson, et al.", yytext(), prefix + "ircmax.htm");
  }
  "LSDA" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("Local Spin Density Approximation", yytext(), prefix + "00000450.htm");
  }
  "NMR" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("NMR shielding tensors and magnetic susceptibilities by Hartree-Fock", yytext(), prefix + "00000458.htm");
  }
  "polar" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("dipole electric field polarizabilities", yytext(), prefix + "00000464.htm");
  }
  "polar"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("dipole electric field polarizabilities", yytext(), prefix + "00000464.htm");
  }
  "polar="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("dipole electric field polarizabilities", yytext(), prefix + "00000464.htm");
  }
  "polar="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("dipole electric field polarizabilities", yytext(), prefix + "00000464.htm");
  }
  "Prop" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("electrostatic properties", yytext(), prefix + "00000466.htm");
  }
  "Pseudo" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("substitute model potential for core electrons", yytext(), prefix + "00000467.htm");
  }
  "SCRF" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("Onsager model for presence of solvent", yytext(), prefix + "00000474.htm");
  }	
  "SCRF"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("calculate presence of solvent", yytext(), prefix + "00000474.htm");
  }	
  "SCRF"={WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("calculate presence of solvent", yytext(), prefix + "00000474.htm");
  }	
  "SCRF"={WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.material("calculate presence of solvent", yytext(), prefix + "00000474.htm");
  }	
  "Volume" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
            pp.material("molecular volume", yytext(), prefix + "00000482.htm");
  }
/* _____________________
   geometry optimization
*/
  "OPT" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
         pp.geometry("geometry optimization", yytext(), "00000460.htm");
  }
  "OPT"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                    pp.geometry("geometry optimization", yytext(), "00000460.htm");
  }
  "OPT="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                    pp.geometry("geometry optimization", yytext(), "00000460.htm");
  }
  "OPT="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                    pp.geometry("geometry optimization", yytext(), "00000460.htm");
  }
/* _______
   density
*/
  "Cube" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.density("evaluate density over a 3D grid (cube) of points", yytext(), prefix + "00000430.htm");
  }
  "Cube"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.density("evaluate density over a 3D grid of points", yytext(), prefix + "00000430.htm");
  }
  "Cube="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.density("evaluate density over a 3D grid of points", yytext(), prefix + "00000430.htm");
  }
  "Cube="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.density("evaluate density over a 3D grid of points", yytext(), prefix + "00000430.htm");
  }
  "density" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.density("generalized densities based on the Z-Vector", yytext(), prefix + "00000431.htm");
  }
  "density"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.density("generalized densities based on the Z-Vector", yytext(), prefix + "00000431.htm");
  }
  "density="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.density("generalized densities based on the Z-Vector", yytext(), prefix + "00000431.htm");
  }
  "density="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.density("generalized densities based on the Z-Vector", yytext(), prefix + "00000431.htm");
  }
/* ______
   system
*/
  "archive" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("place results from calculation into site archive", yytext(), prefix + "00000417.htm");
  }
  "FormCheck" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("write formatted version of checkpoint file", yytext(), prefix + "00000435.htm");
  }
  "FChk" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("write formatted version of checkpoint file", yytext(), prefix + "00000435.htm");
  }
  "FCheck" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("write formatted version of checkpoint file", yytext(), prefix + "00000435.htm");
  }
  "GFInput" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("format current basis set for general basis set input", yytext(), prefix + "00000441.htm");
  }
  "GFPrint" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("print current basis set in tabular form", yytext(), prefix + "00000442.htm");
  }
  "iop" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("set internal options", yytext(), prefix + "00000448.htm");
  }	
  "iop(" ({INT}"/"{INT}"="{INT}) (","{INT}"/"{INT}"="{INT}",")* ")" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("set internal options", yytext(), prefix + "00000448.htm");
  }	
  "MaxDisk" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("disk storage available for scratch data", yytext(), prefix + "00000453.htm");
  }
  "MaxDisk"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("disk storage available for scratch data", yytext(), prefix + "00000453.htm");
  }
  "MaxDisk="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("disk storage available for scratch data", yytext(), prefix + "00000453.htm");
  }
  "MaxDisk="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("disk storage available for scratch data", yytext(), prefix + "00000453.htm");
  }
  "Name" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("username stored in archive entry", yytext(), prefix + "00000457.htm");
  }
  "Name"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("username stored in archive entry", yytext(), prefix + "00000457.htm");
  }
  "Name="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("username stored in archive entry", yytext(), prefix + "00000457.htm");
  }
  "Name="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("username stored in archive entry", yytext(), prefix + "00000457.htm");
  }
  "Output" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
            pp.system("output unformatted Fortran files", yytext(), "00000461.htm");
  }
  "Output"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                      pp.system("output unformatted Fortran files", yytext(), "00000461.htm");
  }
  "Output="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                       pp.system("output unformatted Fortran files", yytext(), "00000461.htm");
  }
  "Output="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                       pp.system("output unformatted Fortran files", yytext(), "00000461.htm");
  }
  "punch" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("output information at various points in calculation", yytext(), prefix + "00000468.htm");
  }
  "punch"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("output information at various points in calculation", yytext(), prefix + "00000468.htm");
  }
  "punch="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("output information at various points in calculation",  yytext(), prefix + "00000468.htm");
  }
  "punch="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("output information at various points in calculation", yytext(), prefix + "00000468.htm");
  }
  "ReArchive" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.system("generate archive entry using info. on checkpoint file", yytext(), prefix + "00000470.htm");
  }	
  "TrackIO" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
            pp.system("provide statistics of I/O and CPU usage", yytext(), "00000479.htm");
  }
/* ___
   etc
*/
  "Geom" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.etc("source of the molecule specification input", yytext(), prefix + "00000440.htm");
  }
  "Geom"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.etc("source of the molecule specification input", yytext(), prefix + "00000440.htm");
  }
  "Geom="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.etc("source of the molecule specification input", yytext(), prefix + "00000440.htm");
  }
  "Geom="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
  pp.etc("source of the molecule specification input", yytext(), prefix + "00000440.htm");
  }
  "NONSTD" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
            pp.etc("nonstd", yytext(), "00000459.htm");
  }
  "OLDCONSTANTS" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                  pp.etc("use values of physical constants from Gaussian 86", yytext(), "00000459.htm");
  }
  "TEST" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
          pp.etc("suppress automatic creation of archive entry", yytext(), prefix + "00000477.htm");
  }
  "Units" {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                    pp.etc("units used in Z-matrix", yytext(), prefix + "00000481.htm");
  }
  "Units"{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                              pp.etc("units used in Z-matrix", yytext(), prefix + "00000481.htm");
  }
  "Units="{WORD} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                           pp.etc("units used in Z-matrix", yytext(), prefix + "00000481.htm");
  }
  "Units="{WORDLIST} {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: "+yytext());
                              pp.etc("units used in Z-matrix", yytext(), prefix + "00000481.htm");
  }
  \r|\n|\r\n {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: found EOL");
	      yybegin(IGNOREALL);
  } 
/* 
                 ___________________________________________
                 Only reading the first line after the pound! 

*/
  [ \t\n] {}
  . {if (Settings.DEBUG) System.out.println("JFlex:pound:POUND: could not analyze input " + yytext());
  pp.unknown("unknown keyword", yytext());
  }
}

<IGNOREALL>{
  .|\n {}
}

.|\n {}
  
			       
