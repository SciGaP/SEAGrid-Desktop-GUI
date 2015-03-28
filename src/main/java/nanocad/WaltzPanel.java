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

package nanocad;

import java.awt.*;
import java.awt.event.*;

public class WaltzPanel extends Panel implements ActionListener
{
//for the first page of waltz
    private Label Formula;
    private TextField theFormula;
    private Label Calculate;
    private Label Animate;
    private Label CalculationTypes;
    private Checkbox EDensity, EOrbital, None, Change, Position;
    private CheckboxGroup Calc, Anim;
    private Panel formPanel, calcPanel, animPanel, buttonPanel;
    private Button Continue, SOver;

//for the second page of waltz
    private Label DelayHead, OptimizeHead, ChargeHead, MultiplicityHead, OrbitalHead, BasisHead, CoordinatesHead;
    private Label DelayMessage, OptimizeMessage, Charge, Multiplicity, OrbitalMessage, Basis;
    private Label Initial, Final;
    private Label[] SymbolLabels, xLabels, yLabels, zLabels;
    private Checkbox VerySlow, Slow, Normal, Fast, No, Yes;
    private CheckboxGroup DelayCheck, OptimizeCheck;
    private TextField MultiplicityField, OrbitalField, InitialField, FinalField;
    private TextField[] AtomFields;
    private Choice chargeChoice, BasisChoice;
    private Button Gamess, RestartThis, RestartAll;
    private Panel topsixPanel, coordPanel, DelayPanel, OptimizePanel, ChargePanel, OrbitalPanel, 
			BasisPanel, MultiplicityPanel, ButtonPanel, DelayCheckPanel;
    private Panel[] atomPanels;

//for both pages    
    private int pageno;

    WaltzPanel()
    {
	pageno = 1;

	Formula = new Label("Formula");
	theFormula = new TextField(20);
	formPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
	formPanel.add(Formula);
	formPanel.add(theFormula);

	Calculate = new Label("Calculate");
	Calc = new CheckboxGroup();
	EDensity = new Checkbox("Electron Densities", Calc, true);
	EOrbital = new Checkbox("Electron Orbitals", Calc, false);
	calcPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
	calcPanel.add(Calculate);
	calcPanel.add(EDensity);
	calcPanel.add(EOrbital);

	Animate = new Label("Animate");
	Anim = new CheckboxGroup();
	None = new Checkbox("None", Anim, true);
	Change = new Checkbox("Change", Anim, false);
	Position = new Checkbox("Position", Anim, false);
	animPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
	animPanel.add(Animate);
	animPanel.add(None);
	animPanel.add(Position);
	animPanel.add(Change);

	Continue = new Button("Continue");
	SOver = new Button("Start Over");
	//addActionListener(Continue); //lixh_comment_out
	//addActionListener(SOver);
	buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
	buttonPanel.add(Continue);
	buttonPanel.add(SOver);

	CalculationTypes = new Label("Calculation Types");
	CalculationTypes.setFont(new Font("Times New Roman", Font.BOLD, 18));

	setLayout(new GridLayout(5, 1));
	add(formPanel);
	add(CalculationTypes);
	add(calcPanel);
	add(animPanel);
	add(buttonPanel);

	this.resize(200, 200);
    }

    public void actionPerformed(ActionEvent e)
    {
	String s = e.getActionCommand();
	if (pageno == 1)
	{
	    if (s.equals("Start Over"))
	    {
		EDensity.setState(true);
		None.setState(true);
		theFormula.setText("");
		
		//whatever other stuff we have to do
	    }
	    if (s.equals("Continue"))
	    {
		remove(formPanel);
		remove(calcPanel);
		remove(animPanel);
		remove(CalculationTypes);
		setupSecondPage();
	    }
	}
	theFormula.setText("Huh??????????");
    }

    public void setupSecondPage()
    {
	pageno = 2;
	
	DelayHead = new Label("Delay Between Frames");
	DelayHead.setFont(new Font("Times New Roman", Font.BOLD, 14));
	if (None.getState() == true)
	{
	    DelayMessage = new Label("Frame Delay is only selectable in an Animation");
	    DelayPanel = new Panel();
	    DelayPanel.setLayout(new GridLayout(1, 2));
	    DelayPanel.add(DelayHead);
	    DelayPanel.add(DelayMessage);
	}
	else
	{
	    DelayMessage = new Label("Here you may increase or decrease the speed of the animation.");
	    DelayCheck = new CheckboxGroup();
	    VerySlow = new Checkbox("Very Slow", DelayCheck, false);
	    Slow = new Checkbox("Slow", DelayCheck, false);
	    Normal = new Checkbox("Normal", DelayCheck, true);
	    Fast = new Checkbox("Fast", DelayCheck, false);
	    DelayCheckPanel = new Panel();
	    DelayCheckPanel.add(VerySlow);
	    DelayCheckPanel.add(Slow);
	    DelayCheckPanel.add(Normal);
	    DelayCheckPanel.add(Fast);
	    DelayPanel = new Panel();
	    DelayPanel.setLayout(new GridLayout(1, 3));
	    DelayPanel.add(DelayHead);
	    DelayPanel.add(DelayMessage);
	    DelayPanel.add(DelayCheckPanel);
	}
	
	OptimizeHead = new Label("Geometry Optimization");
	OptimizeHead.setFont(new Font("Times New Roman", Font.BOLD, 14));
	if (Position.getState() == true)
	{
	    OptimizeMessage = new Label("Geometry cannot be optimized for a Position Animation");
	    OptimizePanel = new Panel();
	    OptimizePanel.setLayout(new GridLayout(1, 2));
	    OptimizePanel.add(OptimizeHead);
	    OptimizePanel.add(OptimizeMessage);
	}
	else
	{
	    OptimizeMessage = new Label("Optimize Geometry?");
	    OptimizeCheck = new CheckboxGroup();
	    No = new Checkbox("No", OptimizeCheck, true);
	    Yes = new Checkbox("Yes", OptimizeCheck, false);
	    OptimizePanel = new Panel();
	    OptimizePanel.setLayout(new GridLayout(1, 4));
	    OptimizePanel.add(OptimizeHead);
	    OptimizePanel.add(OptimizeMessage);
	    OptimizePanel.add(No);
	    OptimizePanel.add(Yes);
	}
    

	topsixPanel = new Panel();
	topsixPanel.setLayout(new GridLayout(3, 2));
	topsixPanel.add(DelayPanel);
	topsixPanel.add(OptimizePanel);

	setLayout(new GridLayout(1,2));
	add(topsixPanel);

    }
}
		
