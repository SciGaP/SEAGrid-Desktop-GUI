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

package org.gridchem.client;
import javax.swing.*;
import javax.swing.border.*;

import org.gridchem.client.common.Settings;


import java.awt.*;
import java.awt.event.*;

/** @author John J. Lee, NCSA
    @version $Id: ControlPanel.java,v 1.2 2005/07/05 21:47:22 dooley Exp $
 */
public class ControlPanel extends JPanel {




  //_________________________________________________
  public ControlPanel(String title, String content) {
    ///Overwrites prior subpanels:
    this.setLayout(new BorderLayout());

    Border paneEdge = BorderFactory.createEmptyBorder(0,10,10,10);
    this.setBorder(paneEdge);

    //Needed for multiple subpanels:
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setTitleContent(title, content);
  }




  //_________________________________________________
  public ControlPanel(String title) {
    ///Overwrites prior subpanels:
    this.setLayout(new BorderLayout());
    this.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));

    //Needed for multiple subpanels:
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

  }




  //_________________________________________________________
  public void addToControlPanel(String title, JTable table) {
    JScrollPane sp = new JScrollPane(table);
    sp.setPreferredSize(Settings.CP_SCROLLPANE_DIM);
    sp.setMinimumSize(Settings.MIN_SIZE);
    this.setTitleContent(title, sp);
  }




  //
  //Cf. http://java.sun.com/docs/books/tutorial/uiswing/layout/box.html
  //______________________________________________________________________
  public void addToControlPanel(String title, JTable table, JButton bttn) {

    // scroll panel w/ spacing
    JScrollPane sp = new JScrollPane(table);
    sp.setPreferredSize(Settings.CP_SCROLLPANE_DIM);
    sp.setMinimumSize(Settings.MIN_SIZE);

    // button panel
    JPanel bp = new JPanel();
    bp.add(bttn);

    // assembly
    JPanel fine = new JPanel();
    fine.setLayout(new BoxLayout(fine, BoxLayout.Y_AXIS));
    fine.add(sp);
    fine.add(Box.createHorizontalGlue());
    fine.add(bp);
    fine.add(Box.createRigidArea(new Dimension(10, 0)));
    this.setTitleContent(title, fine);
  }




  //
  //Cf. http://java.sun.com/docs/books/tutorial/uiswing/components/button.html#checkbox
  //_________________________________________________________
  public void addToControlPanel(String title, 
				JTable table, 
				JCheckBox check, 
				JButton bttn) {

    // scroll panel w/ spacing
    JScrollPane sp = new JScrollPane(table);
    sp.setPreferredSize(Settings.CP_SCROLLPANE_DIM);
    sp.setMinimumSize(Settings.MIN_SIZE);

    // button panel
    JPanel bp = new JPanel();
    bp.setLayout(new BoxLayout(bp, BoxLayout.X_AXIS));
    bp.add(Box.createHorizontalGlue());
    bp.add(check);
    bp.add(Box.createRigidArea(new Dimension(10, 0)));
    bp.add(bttn);

    // assembly
    JPanel fine = new JPanel();
    fine.setLayout(new BoxLayout(fine, BoxLayout.Y_AXIS));
    fine.add(sp);
    fine.add(bp);
    this.setTitleContent(title, fine);
  }




  //__________________________________________________________
  public void setTitleContent (String title, String content) {
    Border bevel = BorderFactory.createLoweredBevelBorder();
    TitledBorder border = BorderFactory.createTitledBorder(bevel, title);
    addCompForTitledBorder(border,
                           content,
			   TitledBorder.DEFAULT_JUSTIFICATION,
			   TitledBorder.ABOVE_TOP);
  }




  //__________________________________________________________________
  public void setTitleContent (String title, JScrollPane sp) {
    Border bevel = BorderFactory.createLoweredBevelBorder();
    TitledBorder border = BorderFactory.createTitledBorder(bevel, title);
    addCompForTitledBorder(border,
                           sp,
			   TitledBorder.DEFAULT_JUSTIFICATION,
			   TitledBorder.ABOVE_TOP);
  }




  //__________________________________________________________________
  public void setTitleContent (String title, JPanel jp) {
    Border bevel = BorderFactory.createLoweredBevelBorder();
    TitledBorder border = BorderFactory.createTitledBorder(bevel, title);
    addCompForTitledBorder(border,
                           jp,
			   TitledBorder.DEFAULT_JUSTIFICATION,
			   TitledBorder.ABOVE_TOP);
  }




  //__________________________________________________________
  private void addCompForTitledBorder(TitledBorder border,
				      String desc,
				      int justification,
				      int position) {
    border.setTitleJustification(justification);
    border.setTitlePosition(position);
    addCompForBorder(border, desc);
  }
  private void addCompForTitledBorder(TitledBorder border,
				      JPanel jp,
				      int justification,
				      int position) {
    border.setTitleJustification(justification);
    border.setTitlePosition(position);
    addCompForBorder(border, jp);
  }
  private void addCompForTitledBorder(TitledBorder border,
				      JScrollPane sp,
				      int justification,
				      int position) {
    border.setTitleJustification(justification);
    border.setTitlePosition(position);
    addCompForBorder(border, sp);
  }




  //_________________________________________________
  private void addCompForBorder(Border b,
				String desc) {
    JPanel compPane = new JPanel();
    JLabel label = new JLabel(desc, JLabel.CENTER);
    compPane.setLayout(new GridLayout(1, 1));
    compPane.add(label);
    compPane.setBorder(b);
    this.add(Box.createRigidArea(Settings.BOX_RIGIDAREA));
    this.add(compPane);
  }
  private void addCompForBorder(Border b,
				JPanel jp) {
    JPanel compPane = new JPanel();
    compPane.setLayout(new BoxLayout(compPane, BoxLayout.Y_AXIS));
    compPane.add(Box.createRigidArea(Settings.BOX_RIGIDAREA));
    compPane.add(jp);
    compPane.setBorder(b);
    this.add(compPane);
  }
  private void addCompForBorder(Border b,
				JScrollPane sp) {
    JPanel compPane = new JPanel();
    compPane.setLayout(new BoxLayout(compPane, BoxLayout.Y_AXIS));
    compPane.add(Box.createRigidArea(Settings.BOX_RIGIDAREA));
    compPane.add(sp);
    compPane.setBorder(b);
    this.add(compPane);
  }




  //===================================================================
  public static void main(String[] args) {
    System.out.println("ControlPanel:main:  need to write unit tests");
  }




}
