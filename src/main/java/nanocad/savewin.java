package nanocad;

/**
 * textwin.java - pop up a new frame to read or write text
 * Copyright (c) 1998 Will Ware, all rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *    or its derived works must display the following acknowledgement:
 * 	This product includes software developed by Will Ware.
 * 
 * This software is provided "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability
 * or fitness for any particular purpose are disclaimed. In no event shall
 * Will Ware be liable for any direct, indirect, incidental, special,
 * exemplary, or consequential damages (including, but not limited to,
 * procurement of substitute goods or services; loss of use, data, or
 * profits; or business interruption) however caused and on any theory of
 * liability, whether in contract, strict liability, or tort (including
 * negligence or otherwise) arising in any way out of the use of this
 * software, even if advised of the possibility of such damage.
 */

import java.awt.*;

public class savewin extends Frame implements java.awt.event.ActionListener 
{
  protected String contents = "Here's a debug window\n";
  protected TextField textWindow;
  protected boolean editable, visible;
  private Button cancel, save;
  protected Label name;
  private group grp;
  public newNanocad nano;
  public String urlname;
  public boolean flag = false;
  public savewin ()
	{
	  /* this one is only for inheritance purposes */
	}

  public savewin (String title, String initialContent, boolean edtbl, newNanocad n)
	{
	  Panel p1 = new Panel();
	  Panel p2 = new Panel();
	  nano = n;
	  editable = edtbl;
	  visible = false;
	  setTitle (title);
	  contents = initialContent;
          name = new Label("Enter the name of file to save");
	  textWindow = new TextField ("", 8);
	  //textWindow.setText (contents);
	  //textWindow.setEditable (true);
          p1.add("WEST",name);
	  p1.add ("North", textWindow);
	  cancel = new Button("Cancel");
	  cancel.addActionListener(this);
	  
	  p2.add ("West", cancel);
	  save = new Button("Save");
	  save.addActionListener(this);

	  p2.add ("East", save);
	  add("North", p1);
	  add("South", p2);
	  resize (350, 150);
	  //setSize(600,600);
	  addWindowListener(new java.awt.event.WindowAdapter() {
		  public void windowClosing(java.awt.event.WindowEvent e){
		      ((Window)e.getWindow()).dispose();
		  }
	      });   
	}
    
    //Ying, no, this is the one really invoked for handling the events;
    public void actionPerformed(java.awt.event.ActionEvent e){
	String s = e.getActionCommand();
	if(s.equals("Cancel")) {
	    update();
	    this.dispose();
	    	    
	    if (grp != null)
		grp.textWindowNotify(contents);
	}else if (s.equals("Save")) {
	   update();
           urlname="http://pine.ncsa.uiuc.edu/cgi-bin/savepdb.cgi";
	   nano.sendFile(urlname, ((group)nano.getGroup()).getPDB(true), (String)textWindow.getText() + ".pdb");
	   this.dispose();
	}
    }
    
    public void changeSize(int w, int h){
	resize(w,h);
    }
    
  public void clear()
	{
	  contents = "";
	  textWindow.setText (contents);
	}

    public String getText()
    {
	update();
	  return contents;
    }

  public boolean isEditable()
	{
	  return editable;
	}
  public boolean isVisible()
	{
	  return visible;
	}
    public void setEditable(boolean e)
	{
	    editable = e;
	    textWindow.setEditable(editable);
	}
    
    public void setGroup(group g)
    {
	  grp = g;
	}

    public void setText(String s)
    {
	clear();
	  write(s);
	}
  public void setVisible(boolean v)
	{
	  visible = v;
	  if (v)
	      show();
	  else
	      hide();
	}

    public void update()
    {
	if (editable) //for SaveXYZ or PDB, this should be false;
	    contents = textWindow.getText();
    }
    
    public void write(double x)
    {
	contents = contents + (new Double(x)).toString();
	textWindow.setText (contents);
    }
    
  public void write(int x)
	{
	  contents = contents + (new Integer(x)).toString();
	  textWindow.setText (contents);
	}
    public void write(String s)
    {
	contents = contents + s;
	textWindow.setText (contents);
    }
}
