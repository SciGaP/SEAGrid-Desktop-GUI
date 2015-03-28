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
 * Created on Mar 29, 2005
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta @author Sandeep Kumar Seethaapathy 
 * 
 */

package G03Input;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;
/**
 * @author Shashank Jeedigunta
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ColorTheme extends DefaultMetalTheme 
{
    /*
    private final ColorUIResource primary1     = 
        new ColorUIResource(0x0, 0x33, 0x99);
     
 private final ColorUIResource primary2     = 
        new ColorUIResource(0xD0, 0xDD, 0xFF);
 private final ColorUIResource primary3     = 
        new ColorUIResource(0x0, 0x99, 0xFF); 
 
 private final ColorUIResource secondary1   = 
        new ColorUIResource(0x6F, 0x6F, 0x6F);
 private final ColorUIResource secondary2   = 
         new ColorUIResource(0x9F, 0x9F, 0x9F);
 private final ColorUIResource secondary3   = 
        new ColorUIResource(0x1f, 0x7f, 0xDC);
 */
  
    
    
    private final ColorUIResource primary3 		= new ColorUIResource(0xD9, 0xD9, 0xAA); 
    private final ColorUIResource primary2 		= new ColorUIResource(0xFF, 0xFF, 0xF0);
    private final ColorUIResource primary1 		= new ColorUIResource(69, 69, 33); //new ColorUIResource(0xF0, 0xF0, 0xE0);
    
    private final ColorUIResource secondary1 	= new ColorUIResource(0x6F, 0x6F, 0x6F);
    private final ColorUIResource secondary2 	= new ColorUIResource(0x9F, 0x9F, 0x9F);
    private final ColorUIResource secondary3 	= new ColorUIResource(236,233,216);//new ColorUIResource(0xDD, 0xDD, 0xCC);

    
    
       // the functions overridden from the base 
       // class => DefaultMetalTheme
 
 protected ColorUIResource getPrimary1() { return primary1; }  
 protected ColorUIResource getPrimary2() { return primary2; } 
 protected ColorUIResource getPrimary3() { return primary3; } 
 
 protected ColorUIResource getSecondary1() { return secondary1; }
 protected ColorUIResource getSecondary2() { return secondary2; }
 protected ColorUIResource getSecondary3() { return secondary3; }
    
    
    
}
