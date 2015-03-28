
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

/** @author John J. Lee
    @version $Id: AuthenticationIconsListener.java,v 1.1.1.1 2005/04/26 16:33:57 dooley Exp $

    @see LoginPanel
    @see AuthenticationIcons

    Private fields label and iconFile must be set by passing a JLabel object
    to one of setNextKerberosOn(), setNextKerberosOff(), 
    setNextGlobusOn(), setNextGlobusOff().

    This class implements ActionListener; implement this class 
    using addActionListener per the example of class RadioListener in 
    RadioButtonDemo:

         RadioListener myListener = new RadioListener();
         birdButton.addActionListener(myListener);

    @see RadioButtonDemo

    The hierarchy of convenience classes is:
    -- LoginPanel
    -- AuthenticationIcons
    -- AuthenticationIconsListener

    See the test() method for an example of implementation.
 */

package org.gridchem.client;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public final class AuthenticationIconsListener implements ActionListener {

  private JLabel label;
  private String iconFile;

  private AuthenticationIconsListener() {
    iconFile = Invariants.brokenIcon;
    label.setIcon(new ImageIcon(iconFile));
  }

  public static AuthenticationIconsListener getInstance() {
    return new AuthenticationIconsListener();
  }

  public void actionPerformed(ActionEvent e) {
    label.setIcon(new ImageIcon(iconFile));
  }

  /** mutate the passed JLabel object
      using actionPerformed
  */
  public void setNextKerberosOn(JLabel l) {
    label = l;
    iconFile = Invariants.kerberosIcon;
  }

  public void setNextKerberosOff(JLabel l) {
    label = l;
    iconFile = Invariants.noKerberosIcon;
  }

  public void setNextGlobusOn(JLabel l) {
    label = l;
    iconFile = Invariants.globusIcon;
  }

  public void setNextGlobusOff(JLabel l) {
    label = l;
    iconFile = Invariants.noGlobusIcon;
  }
  
  public void setNextSSHOn(JLabel l) {
    label = l;
    iconFile = Invariants.SSHIcon;
  }

  public void setNextSSHOff(JLabel l) {
    label = l;
    iconFile = Invariants.noSSHIcon;
  }

  //lixh_3/26/05
  public void setNextGridChemOn(JLabel l) {
    label = l;
    iconFile = Invariants.GridChemIcon;
  }

  public void setNextGridChemOff(JLabel l) {
    label = l;
    iconFile = Invariants.noGridChemIcon;
  }
  //
  
  private static void test() {
    JFrame f = new JFrame();
    Container cp = f.getContentPane();
    JPanel p = new JPanel();
    JLabel label = new JLabel();
    JButton button = new JButton("press to test");
    label.setIcon(new ImageIcon(Invariants.noKerberosIcon)); // need this
    AuthenticationIconsListener ail = 
      AuthenticationIconsListener.getInstance();            // need this
    ail.setNextKerberosOn(label);  // need this
    button.addActionListener(ail); // need this
    p.add(button);
    p.add(label);
    cp.add(p);
    f.pack();
    f.setVisible(true);
  }

  private static void sshtest() {
    JFrame f = new JFrame();
    Container cp = f.getContentPane();
    JPanel p = new JPanel();
    JLabel label = new JLabel();
    JButton button = new JButton("press to test");
    label.setIcon(new ImageIcon(Invariants.noSSHIcon)); // need this
    AuthenticationIconsListener ail = 
      AuthenticationIconsListener.getInstance();            // need this
    ail.setNextSSHOn(label);  // need this
    button.addActionListener(ail); // need this
    p.add(button);
    p.add(label);
    cp.add(p);
    f.pack();
    f.setVisible(true);
  }
  
  /* public static void main(String[] args) {
    test();
  }*/

}
