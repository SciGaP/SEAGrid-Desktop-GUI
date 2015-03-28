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
 * Created on Apr 14, 2005
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta  @author Sandeep Kumar Seethaapathy 
 * 
 */

package G03Input;

import javax.swing.JOptionPane;

public class RouteValidator {

  public static boolean validateRoute()
    {
        StringBuffer molBuffer;
        molBuffer=new StringBuffer(showMolEditor.tempmol);
       
     
        //Check if TV is selected along with MP2 or QCISD
        
        if(isPresent(molBuffer,"TV"))
        {  
            System.out.println("TV Present");
            if(isPresent(RouteClass.methodBuffer,"MP2") ||
                    isPresent(RouteClass.methodBuffer,"QCISD"))     
            {
                
                JOptionPane.showMessageDialog(
			  		G03MenuTree.mainFrame,
			  			"The keyword PBC cannot be used with MP2 or QCISD ",
			  			"Incompatibilty",
			  			JOptionPane.WARNING_MESSAGE
			  			);
                
                return false;    
                
            }
       }
        
        //Check if SCF=QC is selected along with RO 
       if(isPresent(RouteClass.routeBuffer,"SCF=QC"))
       {
           
           if(RouteClass.methodBuffer.substring(0,2).equals("RO") && 
                   !(RouteClass.methodBuffer.substring(0,5).equals("ROVGF")))
           {
               JOptionPane.showMessageDialog(
   			  		G03MenuTree.mainFrame,
   			  			"The keyword-option combination SCF=QC is not available " +
   			  			"for restricted open-shell (RO) calculations",
   			  			"Incompatibilty",
   			  			JOptionPane.WARNING_MESSAGE
   			  			);
                   
                   return false;    
               
           }
           
           
       }
        
        
        return true;
    }
    
  static  boolean isPresent(StringBuffer buffer,String str)
    {
        int index;
        
        
        if(buffer.toString().toLowerCase().indexOf(str.toLowerCase())>-1)
        return true;
                
        return false;
    }
    
    
}
