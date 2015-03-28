package nanocad;

import java.awt.*;

public class InstPanel extends Panel
{
    Font boldFont;
    Font plainFont;
	String minimizeData;
	boolean isWarning = false;

    InstPanel(LayoutManager m)
    {
	super(m);
    }

    public void InstPanel()
    {
	boldFont = new Font( "Serif", Font.BOLD, 12 );
	plainFont = new Font( "Serif", Font.PLAIN, 12 );
    }

    public Dimension getMinimumSize()
    {
		return new Dimension(500, 50);
    }

    public void paint(Graphics g)
    {
	boldFont = new Font( "Serif", Font.BOLD, 12 );
	plainFont = new Font( "Serif", Font.PLAIN, 12 );
	g.setFont(boldFont);
	g.drawString( "Summary of Nanocad Commands:", 225, 15 );
	g.drawString( "Rotate:", 5, 30 );
	g.drawString( "Move Atom:", 5, 45);
	g.drawString( "Add Bond:", 5, 60);
	g.drawString( "Add double bond:", 5, 75);
	g.drawString( "Translate:", 205, 30);
	g.drawString( "Add Atom:", 205, 45);
	g.drawString( "Delete Bond:", 205, 60);
	g.drawString( "Zoom:", 405, 30);
	g.drawString( "Delete Atom:", 405, 45);
	g.drawString( "Select Atom:", 405, 60);
	g.drawString( "Select Group:", 405, 75);
	g.setFont(plainFont);
	g.drawString( "drag gray space", 75, 30 );
	g.drawString( "drag atom", 75, 45);
	g.drawString( "Shift-drag atom to atom", 75, 60);
	g.drawString( "Shift-drag between bonded atoms", 100, 75);
	g.drawString( "Shift-drag gray space", 280, 30);
	g.drawString( "Shift-click gray space", 280, 45);
	g.drawString( "Ctrl-drag atom to atom", 280, 60);
	g.drawString( "Ctrl-drag gray space", 480, 30);
	g.drawString( "Shift-click atom", 480, 45);
	g.drawString( "Alt-click atom", 480, 60);
	g.drawString( "Ctrl-Alt-click atom", 480, 75);

//	g.drawLine( 0, ((Dimension)this.getSize()).height - 21, 700, ((Dimension)this.getSize()).height - 21 );
//	if(isWarning) g.setColor(Color.red); // Changes text to the color defined if warning
//	g.drawString( minimizeData, 50, 98);
//	if(isWarning) g.setColor(Color.black); 

	g.drawLine( 0, ((Dimension)this.getSize()).height - 1, 700, ((Dimension)this.getSize()).height - 1 );
    }

	public String getMinData()
	{	return minimizeData;	}

	public void setMinData(String minData)
	{	minimizeData = minData;
		repaint();
	}

	public void setWarning(boolean warningflag)
	{	isWarning = warningflag;
	}
}
