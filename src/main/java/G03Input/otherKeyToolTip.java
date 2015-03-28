/*

Copyright (c) 2005, Center for Computational Sciences, University of Kentucky.  All rights reserved.

Developed by:

Center for Computational Sciences, University of Kentucky

http://www.ccs.uky.edu/

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software"), to deal with the Software without 
restriction, including without limitation the rights to use, 
copy, modify, merge, publish, distribute, sublicense, and/or 
sell copies of the Software, and to permit persons to whom 
the Software is furnished to do so, subject to the following 
conditions:
1. Redistributions of source code must retain the above copyright notice, 
   this list of conditions and the following disclaimers.
2. Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimers in the documentation
   and/or other materials provided with the distribution.
3. Neither the names of Center for Computational Sciences, University of Kentucky, 
   nor the names of its contributors may be used to endorse or promote products 
   derived from this Software without specific prior written permission.
    
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS WITH THE SOFTWARE.
*/


/**
 * 
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta  @author Sandeep Kumar Seethaapathy 
 * 
 */


package G03Input;


public class otherKeyToolTip {
	public static String charSO = "<br><b> Standard Orientation :</b><br>Use the %KJob=L301 Link0 command to determine the standard orientation for the molecule.";
	public static String chkBasis = "<br><b> CheckBasis:</b><br>Used to request that the basis set be retrieved from the checkpoint" +
	"file instead of from the input stream. This is quite useful when running a large number of multi-step"+
	"jobs that all use the same basis set or multi-step jobs involving general basis sets (user-defined basis"+
	"functions and/or ECP's) since only a single copy of the basis set need be included in the input stream."+
	"<br>ChkBasis, by default, will also retrieve any density fitting set that is contained in the checkpoint file.";
	
	public static String complex = "<br><b>Complex: </b></br> Used to request that the molecular orbitals be allowed to become Complex. <br> " +
	"Note: This keyword may only be used for closed-shell singlet states.";
public static String counter = " <br><b> Counterpoise: </b><br> Used to compute counterposie corrections on an energy, optimization, or frequency calculation or" +
	"BOMD.This keyword takes an integer valuse, counterpoise = N, where N = the number of fragments or monomers in the " +
	"molecular structure. "+
	"Note: When using this keyword, an additional integer must be placed at the end of each atom specification in the " +
	"molecular structure section indicating which fragment or monomer the atom belong to.";



public static String Name = "<b><br>Name: </b></br> Specifies the username that is stroed in the archive entry for the calculation."; 

public static String densityFit = "<br><b>DensityFit:</br></b>Controls density fitting for the coulomb problem.<br>"+
	 "Note:Density fitting basis sets must be specified as part of the model chemistry in the route section of the" +
	 "input file";
public static String external = "<br><b>External:</br></b>Requests a calculation using an externalprogram primarily for use in ONIOM calculaitons";
public static String extrabasis = "<br><b> ExtraBasis: </br></b>Used to indicate that additional basis functions are being added to the basis set <br> " +
	 "specified in the route section of the input file. These extra basis functions are specified in a  separate section <br> "+
	 "of the input file."+
	 "Use this keyword primarily to supply basis functions for elements that are not defined in any of the G03 standard basis <br>"+
	 "sets. Note, however, that attempting to redefine any of the built-in basis sets already defined in G03 will result in error";
public static String extradensitybasis = "<br><b> ExtraDensityBasis:</br></b>Used to indicate that additional basis functions are being added to the density fitting basis set specified in the \n" +
			"route section of the input file.Note that this keyword will be ignored if no density fitting basis set has been specified \n" +
			"in the route section of the input file";
public static String fieldmplusn = "<br>Requests that a finite field involving electric multipoles be added to the calculation, where M identifies the multipole \n" +
	"and N specifies the magnitude ofthe field as N*0.0001 au.Note that the direction of the field(+/-) is parallel(+) o anti-parallel(-) to \n " +
	"the default direction determined by the stardard orientation for the molecule";

public static String fieldmn = "<br><br> Fieldmn:</br></b>Requests that a finite perturbation involving a Fermi contact term be added to the calculation, where M identifies the atom number \n"+
	"corresponding to the ordering in the molecular specification section of the input file and N specifies the magnitude of the perturbation \n "+
	"as N*0.0001 times the spin density on atom M.";
public static String fmm= "<br><b>FMM:</br></b>Forces the use of tge Fast Multipole Method if possible.\n"+
"Note that G0 will generally invoke FMM automatically if it gives even a modest gain in performance.Consequently,"+
"users will rarely need to control FMM maually in unusual special cases, such as computations on nearly linear" +
"polypeptides or long carbon nanotubes.";
public static String GFInput = "<br><b>GFInput: </br></b>This is an output generation keyword that is used to print the current basis set in a form"+
"suitable for use as general basis set input so that it can be used in adding to or modifying a standard basis set.";
public static String GFPrint = "<br><b>GFPrint: </br></b>This is an output generation keyword that is used to print the current basis set in tabular form.";

public static String Integral = "<br><b>Integral:</b><br>Used to modify the method of computation and use of two-electron integrals and their derivatives.";

public static String IOp = "<br><b>Iop: </b><br> Used to set internal options to specific values.\n"+
"The syntax for this keyword is IOp(Ov1/Op1=N1, Ov2/Ov2=N2,...), which sets option number Op1(Op2,...) to the value N1 \n"+
"(N2,...) for every occurence of overlay Ov1(Ov2,...).\n" +
"Note that <b>IOp</b> values that are explicitly set in the route section do not get passed to any subsequent automatically- \n"+
"generated job steps; e.g., the frequency job in a Opt Freq calculation or an inherently multi-step methods as those using \n"+
"the G2 or CBS method.\n" +
"Consequently, if you wanterd to specify, say, an alternative grid for a DFT Opt Freq run, you would have to use an option to \n"+
"the Int=Grid keyword in place of using the Iop keyword.\n"+
"Note also that archiving is automatically turned off when using this keyword.\n"+
"A complete list of options that can be used with this keyword can be found in the Gaussian 03 IOps Reference or online at <b> http://www.gaussian/com </b>";





public static String NMR = "<br><b>NMR: </b><br> Requests that NMR shielding tensors and magnetic susceptibilities be predicted."+
"Note that this properties keyword is applicable for HF, all DFT, and MP2 calculations and that <b>NMR</b> may be combined with the SCRF keyword in G03";


public static String SpinSpin = "<br><b>SpinSpin: </b><br> Compute spin-spin coupling constants in addition to the usual" +
	"NMR properties. This option is available onlyfor HF and DFT calculations.";


public static String CGST = "<br><b>CGST: </b><br> Compute NMR properties using the continous set of gauge transformations (CGST) methoid only.";

public static String GIAO = "<b><br>GIAO: </b><br> Compute NMR properties using Gauge-Independent Atomic Orbital(GIAO) method only. This is the default option for this keyword.";

public static String IGAIM = "<b><br>IGAIM: </b><br> Use atomic centers as guage origins";


public static String SingleOrigin = "<br><b>SingleOrigin: </b></br> Use a single gauge origin. Note that the use of the option is not recommended";


public static String All = "<b><br> All: </b></br> Compute properties using all three methods - <b>CGST,GIAO, and SingleOrigin. </b></br>";

public static String PrintEigenvectors = "<b><br>PrintEigenvectors: </b></br>Print the eigenvectors of te NMR shielding tenso for each atom in the molecular structure.";


	
	
	//To be integrated
	public static String tempTip="<br><b> Temperature: </b><br> Used to specify the temperature (in degrees Kelvin) to be used " +
	"for thermochemistry analysis.";
	//Tooltips for Other Keywords -> SYMMETRY
	public static String symmetryTip="<br><b> Symmetry:</b><br> Requests the sparse matrix storage be used to enhance";
	public static String symmpgTip="<br><b> Symmetry>PG:</b><br> Use no more symmetry than that found in the point group specified by group";
	public static String symmaxisTip="<br><b> Symmetry>Axis:</b><br> Specify axis (X|Y|Z) to help specify the subgroup";
	public static String symmonTip="<br><b> Symmetry>On: </b><br> Turn on symmetry when it would otherwise be turned off";
	public static String[] symmetryOptionTips={"<br><b>Symmetry>Int: </b><br> Enables use of integral symmetry.",
			"<br><b> Symmetry>NoInt: </b><br>Disables use of integral symmetry.",		
			"<br><b> Symmetry>Grad: </b><br> Enables use of symmetry in evaluation of integral derivatives.",
			"<br><b> Symmetry>NoGrad: </b><br> Disables use of symmetry in evaluation of integral derivatives.",
			"<br><b> Symmetry>SCF: </b><br> Enables use of N-cubed symmetry in SCF, which is used by default only" +
			"for GVB calculations",
			"<br><b> Symmetry>NoSCF:</b><br> Disables use of N-cubed symmetry in SCF",
			"<br><b> Symmetry>Loose:</b><br> Requests the use of looser cutoffs in determining symmetry at" +
			"the first point.\n This option is designed to be used with suboptimal geometries",
			"<br><b> Symmetry>Tight:</b><br> Requests the use of the regular (default) criteria at the first point",
			"<br><b> Symmetry>Follow:</b><br> Attempt to follow point group/orientation during optimization"
	};
	
	//Tooltips for Other Keywords -> SPARSE
	public static String sparseTip="<br><b> Sparse:</b><br>Requests that sparse matrix storage be used to enhance performance" +
			"of large calculations" +
			"<p>The cutoff value for considering a matrix element to be a zero is determined by the options for this keyword </p>";
	public static String[] sparseOptionTips={"<br><b>Sparse > Loose:</b><br>Set cutoff value at  5 * 10 <sup>-5</sup> ",
			"<br><b>Sparse > Medium:</b><br>Set cutoff value at  5 * 10 <sup>-7</sup>.<br> Default value for all semi-emperical methods ",
			"<br><b>Sparse > Tight:</b><br>Set cutoff value at  10 <sup>-10</sup>.<br> Default value for all DFT methods ",
			"<br><b>Sparse > N:</b><br>Set cutoff value at  10 <sup>-N</sup>"
			};
	
	public static String scaleTip="<br><b> Scale:</b><br> Used to specify the frequency scale factor to use for thermochemistry analysis";
	
	public static String punchTip="<br><b> Punch:</b><br> Request that useful information be \"punched\" at various points in" +
			"the computation and sent to a seperate output file. The selected options determines what information will be " +
			"output to this file. <p> Combinations of options are distinct and non-interacting in that <b>Punch (MO, GAMESSInput) </b>, " +
			"for example, will send both the MO and GAMESS input information to an output file but soed not format the MO" +
			"information in a GAMESS input format.</p>" +
			"<p> Note that all options for this keyword can be combined with the exception of <b> MO </b> and <b> Natural Orbitals</b>. ";
	
	public static String[] punchOptionTips={"<br><b> Punch > Archive </b><br> Requests that a summary of all of the important results in the computation be punched.",
			"<br><b> Punch > Title </b><br> Punches the title section",
			"<br><b> Punch > Coord </b><br> Punches the atomic numbers and Cartesian Coordinates in a form that can be read" +
			"back into Gaussian",
			"<br><b> Punch > Derivatives </b><br> Punches the energy, nuclear coordinate derivatives, and second derivatives" +
			"in a format (6F12.8) suitable for later use with <b> Opt= FCCArds</b>.",
			"<br><b> Punch > MO </b><br> Punches the orbitals in a format suitable for <b> Guess=Cards </b>",
			"<br><b> Punch > NaturalOrbitals </b><br> Punches the natural orbitals corresponding to the destiny specified with the " +
			"keyword <b> Destiny </b>.",
			"<br><b> Punch > HondoInput </b><br> Punches the input deck for Hondo.",
			"<br><b> Punch > GAMESSInput </b><br> Punches the input deck for GAMESS.",
			"<br><b> Punch > All </b><br> Punches everything with the exception of natural orbitals"};
	
	public static String pseudoTip="<br><b> Pseudo:</b><br> Requests that a model potential be substituted for the core electrons";
	
	public static String propTip="<br><b> Prop:</b><br> Requests computation of electrostatic properties." +
			"<p>The density used for this electrostatic analysis is controlled by the keyword <b>Density</b></p>.";
	
	
	public static String[] propOptionTips={"<br><b> Prop > EFG </b><br> Requests computation of the potential,field and field gradient(default).",
			"<br><b> Prop > Potential </b><br> Requests computation of the potential but not the field or the field gradient.",
			"<br><b> Prop > Field </b><br> Requests computation of the potential and field but not the field gradient.",
			"<br><b> Prop > EPR </b><br> Requests computation of the anisotropic hyperfine coupling constants (spin-dipole EPR terms).",
			"","",
			"<br><b> Prop > FitCharge </b><br> Fit atomic charges to the electrostatic potential at the Van der Waals surface.",
			"<br><b> Prop > Dipole </b><br> Constrain fitted charges to the dipole moment.",
			"",""
			};
	
	public static String pressTip="<br><b> Pressure: </b><br> Specifies the pressure (in atm) that is to be used for thermochemical analysis.";
	
	public static String outputTip="<br><b> Output: </b><br> Requests output of unformatted Fortran file whose content is controlled by the option used with this keyword.";

	public static String molecularTip="<br>Once you <b> Validate </b> the molecular specification, the molecular structure gets displayed in the GUI<br>"+
	"<b> View/Edit Molecular Structure --> File -> Validate </b>";
	public static String molTip="<br>To project the molecular structure onto a given plane, double click the axis perpendicular to that plane.<br>"+
		"To rotate the molecule about a given axis, right click on that axis and drag mouse across display screen.";

	
	public static void main(String args[])
	{
		
	}
	
	
	
}
