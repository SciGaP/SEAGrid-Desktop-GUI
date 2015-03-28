package nanocad;

//package nanocad;
//import nanocad.minimize.mm3.mm3MinimizeAlgorythm;
import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import java.io.*;
import java.lang.*;
import java.util.*; 
import java.net.*;
public class indianadata extends Frame {
 Panel plMain = new Panel();
 TextField Formula = new TextField(7);
  TextField CompoundName = new TextField(7);
  Label lCompoundName =  new Label("Compound Name: ",Label.RIGHT);
  Label lFormula =  new Label("Formula: ",Label.RIGHT);
  Button  okbutt = new Button("OK");
  Button close = new Button("Close");
  public indianadata() {

   enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    }
  private void jbInit() throws Exception {
  this.setTitle("indianadata");
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

    //textwin testWin = new textwin("ANUKaps","",false);
    //testWin.setVisible(true);
    //newNanocad inst=null;
    
    String query_string = null;
    /*Generate query as required by Indiana Database and pass it to them,
               obtain PDB file and reload PDB in nanocad interface */ 

    if(CompoundName.getText() != "")
      {
          query_string = "http://www.iumsc.indiana.edu/db/search.jsp?start=1&compoundName=" + CompoundName.getText() + "&raw=pdb";
            
    //testWin.setText(query_string);
      }
             // for parsing formula here atom1Relation is assumed to be "=" 
             // similarly for all other atoms


               if(Formula.getText() != "")
               {
                  //Parse the formula string into alphabets and numbers
		 // and then generate the query string reqd by Indiana

             	 String atom1Name = null,atom1Num = null; 
                 String atom2Name = null,atom2Num = null; 
                 String atom3Name = null,atom3Num = null; 
                
                 query_string = "http://www.iumsc.indiana.edu/db/search.jsp?start=1&atom1Name=" + atom1Name + "&atom1Relation==atom1Num=" + atom1Num + "&&atom2Name=" + atom2Name + "&atom2Relation==atom2Num=" + atom2Num + "&&atom3Name=" + atom3Name + "&atom3Relation==atom3Num="  + atom3Num + "&raw=pdb";

               }
           try{
               URL url_indiana = new URL(query_string);
               URLConnection conn_indiana = url_indiana.openConnection();
               BufferedReader inFile = new BufferedReader (new InputStreamReader(conn_indiana.getInputStream()));


//    testWin.setText(query_string);
               String line = null;
               String final_file = null;
               int counter =0;
               while ((line = inFile.readLine()) !=null)
                 {

                    if(counter < 5)           // we do not need 5 initial line REMARKS in the PDB file
                      counter++;                                                                         else                  
                       final_file += line;
                 }
      System.out.println(final_file);
    //testWin.setText(final_file);
  //  testWin.setVisible(true);
              // inst.drawFile(final_file);
}catch(Exception e1)
{  System.err.println(e1); }

                              /* If Indiana people return us a file line by line, then
                  we can receive it as above and copy paste some 20 lines
                  of code from loadpdb() BUT if they give a file as a whole
                  then we should save it in some area on disc may be use Case 6
                  save a file function and then call loadPdb(); */

/*
                loadpdb(complete_path_of_file);
*/


    System.exit(0);
    }



     public static void main(String args[])
  {
 /* try
  {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  }
  catch(Exception exp)
  {
  }*/ 
  indianadata aa = new indianadata();
 // aa.addWindowListener(new WindowAdapter(){
  aa.setVisible(true);
   }
}

