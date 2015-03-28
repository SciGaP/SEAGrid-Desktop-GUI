/*Copyright (c) 2007, Center for Computational Sciences, University of Kentucky.  All rights reserved.

Developed by:

Center for Computational Sciences, University of Kentucky

http://www.ccs.uky.edu

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
3. Neither the names of Center for Computational Sciences, University of Kentucky 
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
 * @author Michael Sheetz 
 * @author Pavithra Koka
 * @author Shreeram Sridharan
 */

package Gamess.gamessGUI;

import java.util.ArrayList;

import org.gridchem.client.util.Env;
import org.w3c.dom.Document;

public class GlobalParameters {

	static String inputDocumentName= Env.getApplicationDataDir() + "/GamessFiles/GamessMenu.xml";
    static String inputDocumentName1= Env.getApplicationDataDir() + "/GamessFiles/GamessIncompatibilities.xml";
    static String GamessMenuNew= Env.getApplicationDataDir() + "/GamessFiles/GamessMenuNew.xml";
    static String HelpFile = "file:///" + Env.getApplicationDataDir() + "/GamessFiles/GamessHelp.html";
    static public Document doc = null;
    static public Document userNotesAndToolTip = null;
    static public boolean isProvisionalMode = false;
    
    public static void switchToProvisionalMode()
    {
    	isProvisionalMode = true;
    }
    
    public static void switchToNormalMode()
    {
    	isProvisionalMode = false;
    }
    
    static public UndoRedoHandler undoRedoHandle = new UndoRedoHandler();
    
    static private StringBuilder listOfGroups = new StringBuilder("|$END|$CONTRL|$SYSTEM|$BASIS|$DATA|$ZMAT|$LIBE|$SCF|$SCFMI|$DFT|$MP2|$CIS|$CISVEC|$CCINP|$EOMINP|$MOPAC|$GUESS|$VEC|$MOFRZ|$STATPT|$TRUDGE|$TRURST|$FORCE|$CPHF|$MASS|$HESS|$GRAD|$DIPDR|$VIB|$VIB2|$VSCF|$VIBSCF|$IRC|$DRC|$MEX|$MD|$GLOBOP|$GRADEX|$SURF|$LOCAL|$TWOEI|$TRUNCN|$ELMOM|$ELPOT|$ELDENS|$ELFLDG|$POINTS|$GRID|$PDC|$MOLGRF|$STONE|$RAMAN|$ALPDR|$NMR|$MOROKM|$FFCALC|$TDHF|$TDHFX|$EFRAG|$FRAGNAME|$FRGRPL|$PRTEFP|$DAMP|$DAMPGS|$PCM|$PCMGRD|$PCMCAV|$TESCAV|$NEWCAV|$IEFPCM|$PCMITR|$DISBS|$DISREP|$SVP|$SVPIRF|$COSGMS|$SCRF|$ECP|$MCP|$RELWFN|$EFIELD|$INTGRL|$FMM|$TRANS|$FMO|$FMOPRP|$FMOXYZ|$OPTFMO|$FMOLMO|$FMOBND|$FMOENM|$FMOEND|$OPTRST|$GDDI|$CIINP|$DET|$CIDET|$GEN|$CIGEN|$ORMAS|$GCILST|$SODET|$DRT|$CIDRT|$MCSCF|$MRMP|$DEMRPT|$MCQDPT|$CISORT|$GUGEM|$GUGDIA|$GUGDM|$GUGDM2|$LAGRAN|$TRFDM2|$TRANST|");
    
    public static boolean isGroupAvailable(String group)
    {
    	if(listOfGroups.indexOf("|" + group.toUpperCase() + "|") == -1)
    		return false;
    	else
    		return true;
    }
    
    
    public static ArrayList<String> plainDataGroup = new ArrayList<String>();
	public static ArrayList<String> gridGroup = new ArrayList<String>();
	static
	{
		//  Loading plain data group
		plainDataGroup.add("DATA");
		plainDataGroup.add("VEC");
		plainDataGroup.add("VIBSCF");
		plainDataGroup.add("CISVEC");
		
		//Loading
		gridGroup.add("ZMAT");
		gridGroup.add("LIBE");
	}
}
