package nanocad;                                                                                         
import nanocad.minimize.mm3.mm3MinimizeAlgorythm;  


import java.io.*;
import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
public class error extends Frame {

public boolean flagtoindicate=false;

  Panel plMain = new Panel();
  Label errormsg = new Label();
  Button buttonOK = new Button();
  String message;

  public error() { }
  public error(String msg) {
    //super();
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
       message = msg;
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setTitle("Error");
    this.add(plMain,null);
    //this.setLayout(null);//.getContentPane().setLayout(null);
     plMain.setLayout(null);
    this.setSize(new Dimension(400,200));
    plMain.setLayout(null);
    plMain.setBounds(new Rectangle(0,0,400,200));
    plMain.add(errormsg, null);
    plMain.add(buttonOK, null);
    errormsg.setText(message);
    errormsg.setBounds(30,10,320,40);
    buttonOK.setLabel("OK");
    buttonOK.setFont(new Font("SansSerif", 1, 12));
    buttonOK.setBounds(new Rectangle(125, 78, 40, 32));
    buttonOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonOK_actionPerformed(e);
      }
    });


    //this.getLayout().addLayoutComponent(plMain);//.getContentPane().add(plMain, null);



  }

  void buttonOK_actionPerformed(ActionEvent e)
   {
  this.dispose();
  }





 }


