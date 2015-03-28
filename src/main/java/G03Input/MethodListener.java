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
 * Created on Mar 16, 2005
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta  @author Sandeep Kumar Seethaapathy 
 * 
 */



package G03Input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MethodListener implements ActionListener {


    public void actionPerformed(ActionEvent e)
    {
        if((e.getSource()==G03MenuTree.mhfItem)||(e.getSource()==G03MenuTree.mp2Item)||(e.getSource()==G03MenuTree.am1Item)||
        		(e.getSource()==G03MenuTree.pm3Item)||(e.getSource()==G03MenuTree.mindoItem)||(e.getSource()==G03MenuTree.mndoItem)||
				(e.getSource()==G03MenuTree.comB3ly)||(e.getSource()==G03MenuTree.comB3pw)||(e.getSource()==G03MenuTree.comB3p8)||
				(e.getSource()==G03MenuTree.comB1ly)||(e.getSource()==G03MenuTree.comPbe1)||(e.getSource()==G03MenuTree.comMpw1)||
				(e.getSource()==G03MenuTree.othB1b9)||(e.getSource()==G03MenuTree.othB98)||(e.getSource()==G03MenuTree.othB971)||
				(e.getSource()==G03MenuTree.othB972)||(e.getSource()==G03MenuTree.othBhan)||(e.getSource()==G03MenuTree.othBhlyp)||
				(e.getSource()==G03MenuTree.othLsda)||(e.getSource()==G03MenuTree.stdVsxc)||(e.getSource()==G03MenuTree.stdHcth)||
				(e.getSource()==G03MenuTree.stdHct9)||(e.getSource()==G03MenuTree.stdHct14)||(e.getSource()==G03MenuTree.stdHct40))
				
        {
        	RunFrame.createThreeOptionFrame();
        }
    
    if((e.getSource()==G03MenuTree.mp3Item)||(e.getSource()==G03MenuTree.mp4Item)||(e.getSource()==G03MenuTree.mp5Item)||(e.getSource()==G03MenuTree.ccdItem)||
    		(e.getSource()==G03MenuTree.ccsItem)||(e.getSource()==G03MenuTree.ccsdItem)||(e.getSource()==G03MenuTree.cidItem)||
			(e.getSource()==G03MenuTree.cisdcItem)||(e.getSource()==G03MenuTree.cisItem)||(e.getSource()==G03MenuTree.cisdItem)||
			(e.getSource()==G03MenuTree.sacItem)||(e.getSource()==G03MenuTree.tdItem)||(e.getSource()==G03MenuTree.tddItem)||
			(e.getSource()==G03MenuTree.bdItem)||(e.getSource()==G03MenuTree.gvbItem)||(e.getSource()==G03MenuTree.ovgfItem)||
			(e.getSource()==G03MenuTree.w1Item)||(e.getSource()==G03MenuTree.zinItem)||(e.getSource()==G03MenuTree.pm3mmItem)||
			(e.getSource()==G03MenuTree.cndoItem)||(e.getSource()==G03MenuTree.indoItem)||(e.getSource()==G03MenuTree.cbs4Item)||
			(e.getSource()==G03MenuTree.cbslItem)||(e.getSource()==G03MenuTree.cbsqItem)||(e.getSource()==G03MenuTree.cbsqbItem)||
			(e.getSource()==G03MenuTree.cbsaItem)||(e.getSource()==G03MenuTree.g1Item)||(e.getSource()==G03MenuTree.g2Item)||
			(e.getSource()==G03MenuTree.g2mp2Item)||(e.getSource()==G03MenuTree.g3Item)||(e.getSource()==G03MenuTree.g3mp2Item)||
			(e.getSource()==G03MenuTree.g3b3Item)||(e.getSource()==G03MenuTree.g3mp2b3Item))
    {
    	RunFrame.createTwoOptionFrame();
    }
}
}
