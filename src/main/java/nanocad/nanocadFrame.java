package nanocad;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class nanocadFrame extends Frame implements ActionListener
{
    public boolean handleEvent (Event e)
    {
	if(e.id == Event.WINDOW_DESTROY)
	{
	dispose();
	System.exit(0);
	return true;
	}
	else
	return false;
    }

    public nanocadFrame()
    {
	super("Nanocad");
    
/*	addWindowListener( new java.awt.event.WindowAdapter() {
	    public void WindowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});
*/	
	MenuBar mb = new MenuBar();
	setMenuBar(mb);

	Menu fileMenu = new Menu("File");
	mb.add(fileMenu);
	MenuItem exitMenuItem = new MenuItem("Exit");
	fileMenu.add(exitMenuItem);
	exitMenuItem.addActionListener (this);

	newNanocad nano = new newNanocad();
	add(nano, BorderLayout.CENTER);


//	WaltzPanel test = new WaltzPanel();
//	add(test, BorderLayout.CENTER);
	
	nano.init();
	nano.start();
    }

    public void actionPerformed(ActionEvent evt)
    {

	if (evt.getID() == java.awt.Event.WINDOW_DESTROY)
	    System.exit(0);
	if (evt.getSource() instanceof MenuItem)
	{
	    String menuLabel = ((MenuItem)evt.getSource()).getLabel();

	    if(menuLabel.equals("Exit"))
	    {
        	dispose();
        	System.exit(0);
	    } 
	}
    } 

}


