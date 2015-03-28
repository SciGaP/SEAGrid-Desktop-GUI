/////////////////////////////////////////
// NcadMenu.java
// Aug 2001 by Andrew Knox
// Provides a menu that looks similar on different platforms (windows, mac)
/////////////////////////////////////////

package nanocad;

import java.awt.*;
//import java.awt.event.MouseListener;
import java.awt.event.*;

public class NcadMenu extends Label implements java.awt.event.MouseListener
{
    java.awt.PopupMenu theMenu;
    NcadMenuBar theMenuBar;
    public boolean menuShowing;

    public NcadMenu(String name)
    {
	super(name, Label.LEFT);
        
        setBackground(Color.lightGray);
	
	theMenu = new java.awt.PopupMenu(name);
	((Component)this).add(theMenu);
	addMouseListener(this);
	menuShowing = false;
    }

    public String toString()
    {
	return "NcadMenu";
    }

    public void add(MenuItem m)
    {
	theMenu.add(m);
    }

    public void setMenuBar(NcadMenuBar m)
    {
	theMenuBar = m;
    }

    public Dimension getMinimumSize()
    {
	Dimension d = super.getMinimumSize();
	d.height = this.getFont().getSize()+4;
	return d;
    }

    public Dimension getMaximumSize()
    {
	Dimension d = super.getMaximumSize(); 
	d.height = this.getFont().getSize()+4;
	return d;
    }
    
    public Dimension getPreferredSize()
    {
	Dimension d = super.getPreferredSize();
	d.height = this.getFont().getSize()+4;
	return d;
    }				
    
    public Menu getMenu()
    {
	return (Menu) theMenu;
    }
	
    public void show()
    {
	theMenu.show(this, 0, this.getSize().height + 2);
    }

    public void mousePressed(MouseEvent e)
    {
	this.setBackground(Color.yellow);
	theMenuBar.repaint(1);
	this.show();
    }

    public void mouseReleased(MouseEvent e)
    {;}

    public void mouseClicked(MouseEvent e)
    {;}

    public void mouseEntered(MouseEvent e)
    {;}

    public void mouseExited(MouseEvent e)
    {
	this.setBackground(Color.lightGray);
	theMenuBar.repaint();
    }
}
