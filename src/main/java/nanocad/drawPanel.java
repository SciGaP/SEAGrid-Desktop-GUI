///////////////////////////////////////////
// drawPanel.java
// July 2001 by Andrew Knox
// The panel class on which nanocad draws the molecule.
///////////////////////////////////////////

package nanocad;

import java.awt.*;

public class drawPanel extends Panel
{
    private group grp;
    private boolean geomMode;
    private double geomValue;
    private int geomSize;

    public void setGroupToPaint (group g)
    {
	grp = g;
    }    

    public void clear()
    {
	Graphics g = this.getGraphics();
	char[] name = {'x','y','z'};       
	Rectangle r = this.getBounds();
	g.clearRect (0, 0, r.width, r.height);
	g.setColor (Color.red);
	g.drawLine(550,100,600,100);
	g.drawChars(name,0,1,610,110);
	g.drawLine(550,100,550,50);
	g.drawChars(name,1,1,540,50);
	g.drawLine(550,100,510,120);
	g.drawChars(name,2,1,500,130);   
    }

    public void wireframePaint()
    {
	grp.wireframePaint();
    }

    public void wireframePaint(group g)
    {
	g.wireframePaint();
    }

    public void paint(Graphics g)
    {   
	this.clear();
	super.paint(g);
	grp.paint(g);
        if (geomMode == true)
	    paintGeometryInfo();
    }
    
    public void setGeometryMode(boolean v)
    {
	geomMode = v;
	if(v) paintGeometryInfo();
	}

    public void setGeometryValue(int s, double v)
    {
	geomSize = s;
	geomValue = v;
	geomMode = true;
    }

    //Andrew Knox 06/01/01
    public void paintGeometryInfo()
    {
        Graphics g = this.getGraphics();
          
        geomValue *= 100;
        geomValue = Math.round((float)geomValue);
        geomValue /= 100;

		g.drawString ("Geometry mode: molecule alteration restricted.", 50, 30);
        if (geomSize == 2)
            g.drawString ("bond length: " + geomValue, 50, 50);
        if (geomSize == 3)
            g.drawString ("bond angle: " + geomValue, 50, 50);
        if (geomSize == 4)
            g.drawString ("dihedral: " + geomValue, 50, 50);
    }             
}
