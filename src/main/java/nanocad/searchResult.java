package nanocad;
import nanocad.minimize.mm3.mm3MinimizeAlgorythm;
import java.awt.event.*;
import java.awt.*;
import java.applet.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import java.awt.List;

public class searchResult extends Frame {
    public int newget;
    public boolean flag=true;
    public String smallad,get,header;
    public String pdbfiles, outfile;
    public BufferedReader in1;
    public StringTokenizer l;
    public newNanocad nano;
    Panel plMain = new Panel();

    List slist = new List(50,false);
    Vector forstring = new Vector();

    Label def =  new Label("Following is the result of the CSD search: ");
    Label inst = new Label("Select a molecule and click OK to continue.");
    Button  okbutt = new Button("OK");
    Button close = new Button("Close");
    private boolean tocheck,type;
    
    public searchResult(String infile, newNanocad n) 
    {

	enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	try  
	{
	    this.pdbfiles = infile;
	    nano = n;
	    jbInit();
	}
	catch (Exception e) 
	{
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception 
    {
	this.setTitle("Search Result");
	this.setVisible(true);

	close.setBackground(Color.red);
	okbutt.setBackground(Color.red);
	plMain.setBackground(Color.pink);
	this.add(plMain,null);
	//.getContentPane().add(plMain, null);
	//    this.getContentPane().setLayout(null);
	this.setSize(new Dimension(400,550));
	plMain.setLayout(null);
	plMain.add(slist,null);
	plMain.add(def, null);
	plMain.add(inst, null);
	plMain.add(close, null);
	plMain.add(okbutt, null);
	slist.setBounds(110,90,150,140);
	def.setBounds(new Rectangle(46, 7, 324, 20));
	inst.setBounds(new Rectangle(46, 37, 324, 20));
	try
	{
	    in1 = new BufferedReader(new StringReader(pdbfiles));
	    get = in1.readLine();
	    //while(!get.equals(null))
	    {
		l = new StringTokenizer(get, "    ");
		while (l.hasMoreTokens() == true)
		{
		    header = l.nextToken();
		    //System.out.println("h:" + header);
		    if(header.equals("COMPND"))
		    { 
		        //System.out.println("found file");
		        smallad = l.nextToken();
		        slist.add(smallad);
		    }
		}
		get = in1.readLine();
	    }
	    in1.close();
	}
	catch(Exception e1)
	    { System.err.println(e1); }

	def.setFont(new java.awt.Font("SansSerif", 1, 16));
	okbutt.setFont(new Font("SansSerif", 1, 12));
	okbutt.setBounds(new Rectangle(80, 270, 81, 25));
	
	okbutt.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    okbutt_actionPerformed(e);
      		}
	});

	close.setFont(new Font("SansSerif", 1, 12));
	close.setBounds(new Rectangle(197, 270, 81, 25));
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
	try
	{
	    in1 = new BufferedReader(new StringReader(pdbfiles));
	    String item = slist.getSelectedItem();
	    
	    //while(flag)
	    get = in1.readLine();
	    StringTokenizer l = new StringTokenizer(get, "    ");
	    System.out.println(item);

	    while (l.hasMoreTokens() == true && !header.equals(item))
	    {
	    	header = l.nextToken();
	    }
	    outfile = new String();
	    while (l.hasMoreTokens() == true && !header.equals("END"))
	    {
		outfile += header;
		outfile += "     ";
		header = l.nextToken();
	    }

	    nano.drawFile(outfile, "pdb");
	    this.dispose();
	}
	catch(Exception e1)
	    { System.err.println(e1);}
    }

/*    public static void main(String argv[])
    {
	String filename = argv[0];
	searchResult obj = new searchResult(filename);
	obj.setVisible(true);
    }*/
}
