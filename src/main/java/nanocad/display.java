package nanocad;                                                                                          import nanocad.minimize.mm3.mm3MinimizeAlgorythm;    
import java.io.*;
import java.awt.*;
import java.awt.List;
import java.lang.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;

import java.util.*;
public class display extends Frame {

public boolean flagtoindicate=false;

  Panel plMain = new Panel();
  Button buttonOK = new Button("OK");
  Button buttonclose = new Button("Close");
  List todisplay = new List(50,false);
  String dirname=null,username=null,user,dir;
  List newlist;
  private newNanocad nano;
  textwin saveWin;

  public display(newNanocad n,List newlist,String dir,String user){
    //super();
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
            todisplay = newlist; 
            dirname= dir;
            username = user;
            nano = n;
	    jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void jbInit() throws Exception {
    this.setTitle("Load file");
    this.add(plMain,null);
    //this.setLayout(null);//.getContentPane().setLayout(null);
     plMain.setLayout(null);
    this.setSize(new Dimension(300,450));
    plMain.setLayout(null);
    plMain.setBounds(new Rectangle(0, 0, 100, 100));
   // plMain.add(errormsg, null);
   plMain.add(todisplay,null);
   todisplay.setBounds(40,10,200,200);


    plMain.add(buttonOK, null);
    plMain.add(buttonclose,null);
    todisplay.setVisible(true);
    buttonOK.setFont(new Font("SansSerif", 1, 12));
    buttonOK.setBounds(new Rectangle(100, 278, 40, 32));
    buttonOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonOK_actionPerformed(e);
      }
   } );
   buttonclose.setFont(new Font("SansSerif",1,12));
   buttonclose.setBounds(new Rectangle(160,278,40,32));
   buttonclose.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent e){
     buttonclose_actionPerformed(e);
   }
 } );    
    

   }
  void buttonclose_actionPerformed(ActionEvent e)
   {
      this.dispose();
    }  



  void buttonOK_actionPerformed(ActionEvent e)
   {
   String filename = todisplay.getSelectedItem();

   // Check User directory!  
   String dirname ="/work/csd/temp/"+ username +"/";

    //  To get rid of ".pdb" from the list of files returned by cgi script
    int pos;
    if( (pos = filename.indexOf("pdb")) != -1)
    {
	System.out.println("pdb");
	String ext = filename.substring(pos, filename.length());
	String data = nano.receiveFile(filename, ext, dirname);
	nano.drawFile(data,ext);
    }
  }

 /* public static void main(String args[])
  {
  try
  {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  }
  catch(Exception exp)
  {
  }
  display aa = new display();
  aa.setVisible(true);
  }*/




 }


