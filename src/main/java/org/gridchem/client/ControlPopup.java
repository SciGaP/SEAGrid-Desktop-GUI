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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/** @author John J. Lee, NCSA
    @version $Id: ControlPopup.java,v 1.1.1.1 2005/04/26 16:33:57 dooley Exp $
 */
public class ControlPopup extends JFrame {

  public ControlPopup(String title, String content) {
    super("Control Popup");
    Border blackline, etched, raisedbevel, loweredbevel, empty;
    Border paneEdge = BorderFactory.createEmptyBorder(0,10,10,10);
    blackline = BorderFactory.createLineBorder(Color.black);
    etched = BorderFactory.createEtchedBorder();
    raisedbevel = BorderFactory.createRaisedBevelBorder();
    loweredbevel = BorderFactory.createLoweredBevelBorder();
    empty = BorderFactory.createEmptyBorder();

    JPanel titledBorders = new JPanel();
    titledBorders.setBorder(paneEdge);
    titledBorders.setLayout(new BoxLayout(titledBorders,
					  BoxLayout.Y_AXIS));
    TitledBorder titled;

    titled = BorderFactory.createTitledBorder(title);
    addCompForBorder(titled,
		     content,
		     titledBorders);

    titled = BorderFactory.createTitledBorder(
      blackline, title);
    addCompForTitledBorder(titled,
			   content,
			   TitledBorder.CENTER,
			   TitledBorder.DEFAULT_POSITION,
			   titledBorders);

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Titled", null, titledBorders, null);
    tabbedPane.setSelectedIndex(0);
    
    getContentPane().add(tabbedPane, BorderLayout.CENTER);

  }

  void addCompForTitledBorder(TitledBorder border,
			      String description,
			      int justification,
			      int position,
			      Container container) {
    border.setTitleJustification(justification);
    border.setTitlePosition(position);
    addCompForBorder(border, description,
		     container);
  }

  void addCompForBorder(Border border,
			String description,
			Container container) {
    JPanel comp = new JPanel(false);
    JLabel label = new JLabel(description, JLabel.CENTER);
    comp.setLayout(new GridLayout(1, 1));
    comp.add(label);
    comp.setBorder(border);

    container.add(Box.createRigidArea(new Dimension(0, 10)));
    container.add(comp);
  }

  public static void main(String[] args) {
    JFrame frame = new ControlPopup("my title", "my content");
    frame.addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent e) {
	  System.exit(0);
	}
      });

    frame.pack();
    frame.setVisible(true);
  }
}
