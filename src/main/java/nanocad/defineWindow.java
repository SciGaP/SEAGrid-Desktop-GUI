package nanocad;
import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import java.io.*;
import java.lang.*;
import java.util.*; 
import java.net.*;

public class defineWindow extends Frame {
  Panel plMain = new Panel();
  File filename = new File(".\\exelisting.txt");
  TextField CSD = new TextField(255);
  TextField Chime = new TextField(255);
  Label lChime =  new Label("Chime: ",Label.RIGHT);
  Label lCSD =  new Label("CSD: ",Label.RIGHT);
  Button browseChime = new Button("Find Chime");
  Button browseCSD = new Button("Find CSD");
  Button okbutt = new Button("OK");
  Button close = new Button("Close");
  textwin saveWin;
  private newNanocad nano;
  private boolean tocheck,type;
  public defineWindow(newNanocad n1) {
  if (filename.exists())
  {   InputStream inputStream = getClass().getResourceAsStream(".\\exelisting.txt");
      BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
      try {
      Chime.setText(in.readLine());
      CSD.setText(in.readLine());
      }
      catch (IOException ex) { System.out.println(ex.toString()); }
  }
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
    this.setTitle("Executable Definitions");
    this.add(plMain,null);//.getContentPane().add(plMain, null);
    this.setSize(new Dimension(400,250));
    plMain.setLayout(null);
    plMain.add(CSD,null);
    plMain.add(Chime,null);
    plMain.add(lCSD, null);
    plMain.add(lChime, null);
    plMain.add(browseCSD, null);
    plMain.add(browseChime, null);
    plMain.add(okbutt, null);
    plMain.add(close,null);
    lChime.setBounds(new Rectangle(34, 22, 60, 20));
    Chime.setBounds(new Rectangle(114, 22, 170, 20));
    lCSD.setBounds(new Rectangle(34, 52, 60, 20));
    CSD.setBounds(new Rectangle(114, 52, 170, 20));
    okbutt.setFont(new Font("SansSerif", 1, 12));
    okbutt.setBounds(new Rectangle(86, 120, 81, 25));
    okbutt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        okbutt_actionPerformed(e);
      }
    });
    close.setFont(new Font("SansSerif", 1, 12));
    close.setBounds(new Rectangle(166, 120, 81, 25));
    close.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        close_actionPerformed(e);
      }
    });
    browseCSD.setFont(new Font("SansSerif", 1, 12));
    browseCSD.setBounds(new Rectangle(86, 100, 81, 25));
    browseCSD.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        browseCSD_actionPerformed(e);
      }
    });
    browseChime.setFont(new Font("SansSerif", 1, 12));
    browseChime.setBounds(new Rectangle(166, 100, 81, 25));
    browseChime.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	browseChime_actionPerformed(e);
      }
    });
 }

   void close_actionPerformed(ActionEvent e)
   {
	this.dispose();
   }

   void browseCSD_actionPerformed(ActionEvent e)
   {
	FileDialog f = new FileDialog(this, "Find File", FileDialog.LOAD);
	f.show();
	String filename = f.getFile();
	String dirname = f.getDirectory();
	CSD.setText(dirname+filename);
   }

   void browseChime_actionPerformed(ActionEvent e)
   {
	FileDialog f = new FileDialog(this, "Find File", FileDialog.LOAD);
	f.show();
	String filename = f.getFile();
	String dirname = f.getDirectory();
	Chime.setText(dirname+filename);
   }

    void okbutt_actionPerformed(ActionEvent e)
    {
	String CSDaddress = CSD.getText();
	String Chimeaddress = Chime.getText();

	File info = new File(".\\exelisting.txt");
	try
	{
	    System.out.println("Attempting write to .\\exelisting.txt");
	    PrintWriter out1 = new PrintWriter((OutputStream)new FileOutputStream(info));
	    out1.print(CSDaddress+"\n");
	    out1.print(Chimeaddress+"\n");
	    out1.close();
	}
	catch (IOException ex)
	{   System.out.println(ex.toString());   }
    }
}

