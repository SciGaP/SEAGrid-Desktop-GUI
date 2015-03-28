package nanocad;

import java.awt.*;
import java.applet.*;

public class ListDemo extends Applet {
  List browser;
  String msg = "";
  
  public void init() {
  browser = new List(4,false);
  
  browser.addItem("First");
  browser.addItem("Second");
  browser.addItem("Third");
  browser.addItem("Fourth");

  add(browser);
}

//Repaint whn user double clicks
public boolean action(Event e,Object arg) {
  if(e.target instanceof List) {
     repaint();
     return true;
  }
  return false;
}

public void paint(Graphics g) {
 int idx[];

 msg = "Hi there";
 msg += browser.getSelectedItem();
 g.drawString(msg,6,140);
}
}
