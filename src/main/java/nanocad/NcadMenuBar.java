/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software", to deal with the Software without 
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
3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
   University of Illinois at Urbana-Champaign, nor the names of its contributors 
   may be used to endorse or promote products derived from this Software without 
   specific prior written permission.
    
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS WITH THE SOFTWARE.

*/

//////////////////////////////////////////
// NcadMenuBar.java
// Aug 2001 by Andrew Knox
// The menu bar for the cross-platform menu
// Note: it must be added to a frame like a panel
//////////////////////////////////////////

package nanocad;

import java.awt.*;
import java.awt.event.*;

public class NcadMenuBar extends Panel
{
    NcadMenu[] menus;
    Frame parent;
    boolean hasParent;

    public NcadMenuBar(NcadMenu[] m, int size)
    {
	super(new FlowLayout(FlowLayout.LEFT, 10, 0));
	menus = new NcadMenu[size];
	for (int i = 0; i < size; i++)
	{
	    menus[i] = m[i];
	    m[i].setMenuBar(this);
	    ((Panel)this).add(m[i]);
	    System.out.println("added");
	}
        this.setBackground(Color.lightGray);	
	hasParent = false;
	validate();
    }
    
    public void setParent(Frame f)
    {
	parent = f;
	hasParent = true;
    }

    public Dimension getMinimumSize()
    {
	if(hasParent == true)
	{
	    Dimension d = menus[0].getSize();
	    d.width = parent.getSize().width;
	    return d;
	}
	return this.getSize();
    }

    public Dimension getMaximumSize()
    {
	if(hasParent == true)
	{
	    Dimension d = menus[0].getSize();
	    d.width = parent.getSize().width;
	    return d;
	}
    	return this.getSize();
    }
	
    public Dimension getPreferedSize()
    {
	if(hasParent == true)
	{
	    Dimension d = menus[0].getSize();
	    d.width = parent.getSize().width;
	    return d;
	}
    	return this.getSize();
    }
	
}
