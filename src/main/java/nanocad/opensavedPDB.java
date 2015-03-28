//can be deleted if display.java works
package nanocad;
import nanocad.minimize.mm3.mm3MinimizeAlgorythm;
import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import java.io.*;
import java.lang.*;
import java.util.*; 
import java.net.*;

public class opensavedPDB extends Frame {
  Panel plMain = new Panel();
  TextField Formula = new TextField(7);
  TextField CompoundName = new TextField(7);
  Label lCompoundName =  new Label("Compound Name: ",Label.RIGHT);
  Label lFormula =  new Label("Formula: ",Label.RIGHT);
  Button  okbutt = new Button("OK");
  Button close = new Button("Close");
  textwin saveWin;
  private Indiana ind;
  private newNanocad nano;

  public opensavedPDB(newNanocad n1) {

   nano = n1;


   enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    }

  private void jbInit() throws Exception {
//  this.setTitle("indianadata");
  if(nano.callDatabase == 1) 
       this.setTitle("Indiana Database Search");
  if(nano.callDatabase == 2)
        this.setTitle("CSD Search");
 
    this.add(plMain,null);//.getContentPane().add(plMain, null);
//    this.getContentPane().setLayout(null);
    this.setSize(new Dimension(400,250));
    plMain.setLayout(null);
    plMain.add(CompoundName,null);
    plMain.add(Formula,null);
    plMain.add(lCompoundName,null);
    plMain.add(lFormula,null);
    plMain.add(okbutt, null);
    plMain.add(close,null);
    lFormula.setBounds(new Rectangle(34, 22, 60, 20));
    Formula.setBounds(new Rectangle(174, 22, 70, 20));
     lCompoundName.setBounds(new Rectangle(34, 52, 110, 20));
    CompoundName.setBounds(new Rectangle(174, 52, 70, 20));
    okbutt.setFont(new Font("SansSerif", 1, 12));
    okbutt.setBounds(new Rectangle(86, 100, 81, 25));
    okbutt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        okbutt_actionPerformed(e);
      }
    });
     
    close.setFont(new Font("SansSerif", 1, 12));
    close.setBounds(new Rectangle(166, 100, 81, 25));
    close.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        close_actionPerformed(e);
      }
    });
 }

   void close_actionPerformed(ActionEvent e)
{
  this.dispose();
}
    void okbutt_actionPerformed(ActionEvent e)
   {
    String formu = Formula.getText();
    String comp = CompoundName.getText();

    String chk = "5";
    String encodedchk = null;

    /*Generate query as required by Indiana Database and pass it to them,
               obtain PDB file and reload PDB in nanocad interface */ 


   // if(CompoundName.getText() != "")
   if( comp.length() > 1)
          chk = "0";        
    else  if(Formula.getText() != "")
               {
                  chk = "1";
		  //Process formula string
               }


    // To use CGI script to call Indiana.java and also to pass values of
    // compound name and formula

         String encodedCompoundName = URLEncoder.encode(CompoundName.getText());
         String encodedFormula = URLEncoder.encode(Formula.getText());
         encodedchk = URLEncoder.encode(chk);
         String databasecgi = null;  
         try{
              if(nano.callDatabase == 1) 
                   databasecgi= "http://chemviz.ncsa.uiuc.edu/cgi-bin/nanocad/indiana.cgi";

              System.out.println("Before calling csd.cgi");
              if(nano.callDatabase == 2) 
                   databasecgi= "http://chemviz.ncsa.uiuc.edu/cgi-bin/nanocad/csd.cgi";

              URL urlcgi = new URL(databasecgi);
              URLConnection conn1 = urlcgi.openConnection();
              conn1.setDoOutput(true);
              PrintWriter out1 = new PrintWriter(conn1.getOutputStream());
             if(chk.equals("0"))
                out1.println(encodedCompoundName + "= 0");
             else
                 if(chk.equals("1")) 
                     out1.println(encodedFormula + "= 1");

/*
//CHK if following needed I doubt!
              if(Formula.getText() != "")
                   out1.println(encodedFormula + "= 1");
              if(CompoundName.getText() != "")
                  {
                    out1.println(encodedCompoundName + "= 0");
                  }
//CHK if following needed I doubt!  TILL HERE
*/
              out1.close();


             //Reading file from the cgi output
		String line = null;
		BufferedReader inStr1 = new BufferedReader (new InputStreamReader(conn1.getInputStream()));
                String b = "";
		while ((line = inStr1.readLine()) !=null)
                    {
                        System.out.println(line);
                        b   = b + line;
		    }
               inStr1.close();



               nano.drawFile(b);
               
             }catch(MalformedURLException mx){
                   saveWin = new textwin("URL Exception","",false);
                   saveWin.setVisible(true);
                   saveWin.setText("Exception : "+ mx.toString());
             }catch ( IOException e1){
                   saveWin = new textwin("IO Exception","",false);
                   saveWin.setVisible(true);
                   saveWin.setText("Exception : "+e1.toString());    
             }

   this.dispose(); 
    }

}

