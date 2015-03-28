/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software") to deal with the Software without
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
/** Example 13-3 of 
    Ian F. Darwin's _Java Cookbook_ (O'Reilly & Associates, Sebastopol) 2001.

    WindowCloser watches for window closing events and follows them up
    with setVisible(false) and dispose().
 */
package org.gridchem.client;
import javax.swing.*; //for buttons, labels, and images
import java.awt.*;
import java.awt.event.*;

public class WindowCloser extends WindowAdapter {

  Window win;
  boolean doExit = false;
  
  public WindowCloser(Window w) {
    this(w, false);
  }

  public WindowCloser(Window w, boolean exit) {
    win = w;
    doExit = exit;
  }

  public void windowClosing(WindowEvent e) {
    win.setVisible(false);
    win.dispose();
    if (doExit) {
/*
      NOT SURE WHY THIS ISN'T WORKING;
      PERHAPS BECAUSE JAVA 
      JOptionPane.showMessageDialog(
	WindowCloser.this,
	"Code for saving data goes here...",
	"WindowCloser:TESTING",
	JOptionPane.INFORMATION_MESSAGE
	);
*/
      System.exit(0);
    }
  }
}
