package org.gridchem.client;

import java.io.*;

public class PlotPlotML {

  public static boolean DEBUG = true;

  public void plotString (String plotBuffer) throws IOException {

    // set up tmp plot file that will be deleted on exit
    //___________________________________________________________________________
    File tmp;
    try{
      tmp = File.createTempFile("plot", ".xml");
      if (tmp.exists()) {
	System.err.println("PlotPlotML:plotString():  tmp.exists --> "+tmp.exists());
      }
      tmp.deleteOnExit();
    } catch (IOException e1) {
      System.err.println("PlotPlotML:plotString():  could not create File tmp");
      System.err.println(e1.toString());
      e1.printStackTrace();
      return;
    }
    String tmppath;
    String tmpname;
    try{
      tmppath = tmp.getCanonicalPath();
      tmpname = tmp.getName();
      System.out.println("PlotPlotML:plotString():  tmpplot canonical path --> "+
			 tmppath);
      System.out.println("PlotPlotML:plotString():  tmpplot name --> "+
			 tmpname);
    } catch (IOException e2) {
      System.err.println("PlotPlotML:plotString():  could not get tmp.getCanonicalPath()");
      System.err.println(e2.toString());
      e2.printStackTrace();
      return;
    }

    // write out plotBuffer using buffering
    //___________________________________________________________________________
    try{
      PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(tmpname)));
      pw.print(plotBuffer);
      pw.flush();
      pw.close();
      if (DEBUG) {
	System.out.println("PlotPlotML:plotString  pw.print(plotBuffer) ==>");
	System.out.print(plotBuffer);
      }
    } catch (IOException e3) {
      System.err.println("PlotPlotML:plotString():  could not instantiate PrintWriter pw");
      System.err.println(e3.toString());
      e3.printStackTrace();
      return;
    }
    String[] args = {tmpname};

    // call ptplot
    //___________________________________________________________________________
    try {
      PlotApplication plot = new PlotMLApplication(new Plot(), args);
    } catch (Exception ex) {
      System.err.println("PlotPlotML:plotString():  could not instantiate PlotApplication plot");
      System.err.println(ex.toString());
      ex.printStackTrace();
    }

  }// end public void plot

}
