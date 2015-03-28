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
 * @author Sandeep Kumar Seethaapathy @author Shashank Jeedigunta  
 * 
 */




package G03Input;
import java.util.*;


public class MolEditorHelp {
	
	public static Hashtable<String,Integer> htAtomicNumber;
	
	
	public static void hashFunction()
	{
    htAtomicNumber = new Hashtable<String,Integer>();

	htAtomicNumber.put("H", new Integer(1) );
	htAtomicNumber.put("He", new Integer(2) );
	htAtomicNumber.put("Li", new Integer(3) );
	htAtomicNumber.put("Be", new Integer(4) );
	htAtomicNumber.put("B", new Integer(5) );
	htAtomicNumber.put("C", new Integer(6) );
	htAtomicNumber.put("N", new Integer(7) );
	htAtomicNumber.put("O", new Integer(8) );
	htAtomicNumber.put("F", new Integer(9) );
	htAtomicNumber.put("Ne", new Integer(10) );
	htAtomicNumber.put("Na", new Integer(11) );
	htAtomicNumber.put("Mg", new Integer(12) );
	htAtomicNumber.put("Al", new Integer(13) );
	htAtomicNumber.put("Si", new Integer(14) );
	htAtomicNumber.put("P", new Integer(15) );
	htAtomicNumber.put("S", new Integer(16) );
	htAtomicNumber.put("Cl", new Integer(17) );
	htAtomicNumber.put("Ar", new Integer(18) );
	htAtomicNumber.put("K", new Integer(19) );
	htAtomicNumber.put("Ca", new Integer(20));
	htAtomicNumber.put("Sc", new Integer(21) );
	htAtomicNumber.put("Ti", new Integer(22) );
	htAtomicNumber.put("V", new Integer(23) );
	htAtomicNumber.put("Cr", new Integer(24) );
	htAtomicNumber.put("Mn", new Integer(25) );
	htAtomicNumber.put("Fe", new Integer(26) );
	htAtomicNumber.put("Co", new Integer(27) );
	htAtomicNumber.put("Ni", new Integer(28) );
	htAtomicNumber.put("Cu", new Integer(29) );
	htAtomicNumber.put("Zn", new Integer(30) );
	htAtomicNumber.put("Ga", new Integer(31) );
	htAtomicNumber.put("Ge", new Integer(32) );
	htAtomicNumber.put("As", new Integer(33) );
	htAtomicNumber.put("Se", new Integer(34) );
	htAtomicNumber.put("Br", new Integer(35) );
	htAtomicNumber.put("Kr", new Integer(36) );
	htAtomicNumber.put("Rb", new Integer(37) );
	htAtomicNumber.put("Sr", new Integer(38) );
	htAtomicNumber.put("Y", new Integer(39) );
	htAtomicNumber.put("Zr", new Integer(40) );
	htAtomicNumber.put("Nb", new Integer(41) );
	htAtomicNumber.put("Mo", new Integer(42) );
	htAtomicNumber.put("Tc", new Integer(43) );
	htAtomicNumber.put("Ru", new Integer(44) );
	htAtomicNumber.put("Rh", new Integer(45) );
	htAtomicNumber.put("Pd", new Integer(46) );
	htAtomicNumber.put("Ag", new Integer(47) );
	htAtomicNumber.put("Cd", new Integer(48) );
	htAtomicNumber.put("In", new Integer(49) );
	htAtomicNumber.put("Sn", new Integer(50) );
	htAtomicNumber.put("Sb", new Integer(51) );
	htAtomicNumber.put("Te", new Integer(52) );
	htAtomicNumber.put("I", new Integer(53) );
	htAtomicNumber.put("Xe", new Integer(54) );
	htAtomicNumber.put("Cs", new Integer(55) );
	htAtomicNumber.put("Ba", new Integer(56) );
	htAtomicNumber.put("La", new Integer(57) );
	htAtomicNumber.put("Ce", new Integer(58) );
	htAtomicNumber.put("Pr", new Integer(59) );
	htAtomicNumber.put("Nd", new Integer(60) );
	htAtomicNumber.put("Pm", new Integer(61) );
	htAtomicNumber.put("Sm", new Integer(62) );
	htAtomicNumber.put("Eu", new Integer(63) );
	htAtomicNumber.put("Gd", new Integer(64) );
	htAtomicNumber.put("Tb", new Integer(65) );
	htAtomicNumber.put("Dy", new Integer(66) );
	htAtomicNumber.put("Ho", new Integer(67) );
	htAtomicNumber.put("Er", new Integer(68) );
	htAtomicNumber.put("Tm", new Integer(69) );
	htAtomicNumber.put("Yb", new Integer(70) );
	htAtomicNumber.put("Lu", new Integer(71) );
	htAtomicNumber.put("Hf", new Integer(72) );
	htAtomicNumber.put("Ta", new Integer(73) );
	htAtomicNumber.put("W", new Integer(74) );
	htAtomicNumber.put("Re", new Integer(75) );
	htAtomicNumber.put("Os", new Integer(76) );
	htAtomicNumber.put("Ir", new Integer(77) );
	htAtomicNumber.put("Pt", new Integer(78) );
	htAtomicNumber.put("Au", new Integer(79) );
	htAtomicNumber.put("Hg", new Integer(80) );
	htAtomicNumber.put("Tl", new Integer(81) );
	htAtomicNumber.put("Pb", new Integer(82) );
	htAtomicNumber.put("Bi", new Integer(83) );
	htAtomicNumber.put("Po", new Integer(84) );
	htAtomicNumber.put("At", new Integer(85) );
	htAtomicNumber.put("Rn", new Integer(86) );
	htAtomicNumber.put("Fr", new Integer(87) );
	htAtomicNumber.put("Ra", new Integer(88) );
	htAtomicNumber.put("Ac", new Integer(89) );
	htAtomicNumber.put("Th", new Integer(90) );
	htAtomicNumber.put("Pa", new Integer(91) );
	htAtomicNumber.put("U", new Integer(92) );
	htAtomicNumber.put("Np", new Integer(93) );
	htAtomicNumber.put("Pu", new Integer(94) );
	htAtomicNumber.put("Am", new Integer(95) );
	htAtomicNumber.put("Cm", new Integer(96) );
	htAtomicNumber.put("Bk", new Integer(97) );
	htAtomicNumber.put("Cf", new Integer(98) );
	htAtomicNumber.put("Es", new Integer(99) );
	htAtomicNumber.put("Fm", new Integer(100) );
	htAtomicNumber.put("Md", new Integer(101) );
	htAtomicNumber.put("No", new Integer(102) );
	htAtomicNumber.put("Lr", new Integer(103) );
	htAtomicNumber.put("Tv", new Integer(0) );


	}

}
