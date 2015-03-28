/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

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

/** @author John J. Lee, NCSA
    @version $Id: Preferred.java,v 1.1.1.1 2005/04/26 16:34:01 dooley Exp $
    @see Settings

    This is the place for preferred public static final fields and 
    canonical, public objects that are used throughout 
    the QCRJM2002 application.

    TO DO:  make everything private so as to encourage minimal mutability,
            and implement public get/set methods.  
 */

package org.gridchem.client;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;

public final class Preferred {

  /********************************************************
      Methods for adding some spacing around a JPanel and 
      returning a modified JPanel.
   */
  public static JPanel mySpacing (JPanel interior) {
    int x = Invariants.space;
    return mySpacing(interior, x, x, x, x);
  }

  public static JPanel mySpacing (JPanel interior, 
				  int x1, 
				  int x2, 
				  int x3, 
				  int x4) {
    JPanel spacing = new JPanel();
    spacing.setBorder(BorderFactory.createEmptyBorder(x1, x2, x3, x4));
    spacing.add(interior);
    return spacing;
  }
  
  public static JPanel mySpacing (JScrollPane interior) {
    int x = Invariants.space;
    return mySpacing(interior, x, x, x, x);
  }
  
  public static JPanel mySpacing (JScrollPane interior,
				  int x1,
				  int x2, 
				  int x3, 
				  int x4) {
    JPanel spacing = new JPanel();
    spacing.setBorder(BorderFactory.createEmptyBorder(x1, x2, x3, x4));
    JPanel interiorPanel = new JPanel();
    interiorPanel.add(interior);
    spacing.add(interiorPanel);
    return spacing;
  }

  /********************************************************
     Factory methods that return various canonical JPanels
   */
  public static JPanel myPanelFactory (String title, JPanel interior) {
    int x = Invariants.space;
    return myPanelFactory(title, interior, x, x, x, x);
  }

  public static JPanel myPanelFactory (String title, 
				       JPanel interior,
                                       int x1,
                                       int x2,
                                       int x3,
                                       int x4) {
    JPanel myPanel = new JPanel();
    Border bevel = BorderFactory.createLoweredBevelBorder();
    TitledBorder border = BorderFactory.createTitledBorder(bevel, title);
    border.setTitleJustification(TitledBorder.DEFAULT_JUSTIFICATION);
    border.setTitlePosition(TitledBorder.ABOVE_TOP);
    myPanel.setBorder(border);
    myPanel.add(interior);
    JPanel mySpacedPanel = mySpacing(myPanel, x1, x2, x3, x4);
    return mySpacedPanel;
  }

  public static JPanel myPanelFactory (String title, JScrollPane interior) {
    int x = Invariants.space;
    return myPanelFactory(title, interior, x, x, x, x);
  }

  public static JPanel myPanelFactory (String title, 
				       JScrollPane interior,
                                       int x1,
                                       int x2,
                                       int x3,
                                       int x4) {
    JPanel myPanel = new JPanel();
    Border bevel = BorderFactory.createLoweredBevelBorder();
    TitledBorder border = BorderFactory.createTitledBorder(bevel, title);
    border.setTitleJustification(TitledBorder.DEFAULT_JUSTIFICATION);
    border.setTitlePosition(TitledBorder.ABOVE_TOP);
    myPanel.setBorder(border);
    JPanel interiorPanel = new JPanel();
    interiorPanel.add(interior);
    myPanel.add(interiorPanel);
    JPanel mySpacedPanel = mySpacing(myPanel, x1, x2, x3, x4);
    return mySpacedPanel;
  }

  public static JPanel myPanelFactory (JPanel interior) {
    int x = Invariants.space;
    return myPanelFactory(interior, x, x, x, x);
  }

  public static JPanel myPanelFactory (JPanel interior,
                                       int x1,
                                       int x2,
                                       int x3,
                                       int x4) {
    JPanel myPanel = new JPanel();
    Border border = BorderFactory.createLoweredBevelBorder();
    myPanel.setBorder(border);
    myPanel.add(interior);
    JPanel mySpacedPanel = mySpacing(myPanel, x1, x2, x3, x4);
    return mySpacedPanel;
  }

  public static JPanel myPanelFactory (JScrollPane interior) {
    int x = Invariants.space;
    return myPanelFactory(interior, x, x, x, x);
  }

  public static JPanel myPanelFactory (JScrollPane interior,
                                       int x1,
                                       int x2,
                                       int x3,
                                       int x4) {
    JPanel myPanel = new JPanel();
    Border border = BorderFactory.createLoweredBevelBorder();
    myPanel.setBorder(border);
    JPanel interiorPanel = new JPanel();
    interiorPanel.add(interior);
    myPanel.add(interiorPanel);
    JPanel mySpacedPanel = mySpacing(myPanel, x1, x2, x3, x4);
    return mySpacedPanel;
  }

  /***************
     Unit testing
   */
  private static void test() {

    JLabel l1 = new JLabel("test label 1");
    JButton b1 = new JButton("test button 1");
    JLabel l2 = new JLabel("test label 2");
    JButton b2 = new JButton("test button 2");
    JLabel l3 = new JLabel("test label 3");
    JButton b3 = new JButton("test button 3");
    JLabel l4 = new JLabel("test label 4");
    JButton b4 = new JButton("test button 4");

    JPanel p1 = new JPanel();
    p1.add(l1);
    p1.add(b1);
    JPanel p1_ = mySpacing(p1);

    JPanel p2 = new JPanel();
    p2.add(l2);
    p2.add(b2);
    JPanel p2_ = myPanelFactory("test title 2", p2);

    JPanel p3 = new JPanel();
    p3.add(l3);
    p3.add(b3);
    JPanel p3_ = myPanelFactory(p3);

    JPanel p4 = new JPanel();
    p4.add(l4);
    p4.add(b4);
    JPanel p4_ = mySpacing(myPanelFactory("test title 4", p4));

    // assemble f
    JPanel cp = new JPanel();
    cp.add(p1_);
    cp.add(p2_);
    cp.add(p4_);
    cp.add(p3_);
    JFrame f = new JFrame("Preferred:test");
    f.getContentPane().add(cp);
    f.pack();
    f.setVisible(true);
    f.addWindowListener(new WindowCloser(f, true));

  }

 /* public static void main(String[] args) {
    test();
  }*/

}
