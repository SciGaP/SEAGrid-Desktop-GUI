package nanocad;

import java.io.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class getname extends Frame {
 
//  static LoginFailScreen frame3;
  private String s;
  JPanel plMain = new JPanel();
  JLabel labelfile = new JLabel();
   JTextField tffile= new JTextField();
 
  JButton buttonOK = new JButton();
  JButton buttonCancel = new JButton();
  String s1 = new String();
  



  public getname() {
    super();

     try  {
      jbInit();
    }
    catch (Exception e) {
      System.out.println("wrong");
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
   // this.getContentPane().setLayout(null);
    this.setSize(new Dimension(374, 226));
    plMain.setLayout(null);
    plMain.setBounds(new Rectangle(0, 0, 392, 273));
    labelfile.setText("Filename:");
    labelfile.setFont(new Font("SansSerif", 0, 12));
    labelfile.setBounds(new Rectangle(24, 22, 41, 15));
    
    tffile.setBounds(new Rectangle(96, 17, 120, 24));
   
    buttonOK.setText("OK");
    buttonOK.setBounds(new Rectangle(16, 147, 81, 25));
    buttonOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonOK_actionPerformed(e);
      }
    });
    buttonCancel.setText("Cancel");
    buttonCancel.setBounds(new Rectangle(112, 146, 81, 25));
    buttonCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        buttonCancel_actionPerformed(e);
      }
    });
    this.setTitle("Login Screen");
   // this.getContentPane().add(plMain, null);
    plMain.add(labelfile, null);
   
    plMain.add(tffile, null);
   
    plMain.add(buttonOK, null);
    plMain.add(buttonCancel, null);
   
  }
void buttonOK_actionPerformed(ActionEvent e)

   {
s1 = tffile.getText();

}

 void buttonCancel_actionPerformed(ActionEvent e)
  {
  System.exit(0);
  }
public static void main(String [] args){
getname togetname = new getname();
togetname.setVisible(true);
}
 

}
