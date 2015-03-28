package Nanocad3d.xyplotgui.frame;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class XYSeriesPlotPanel extends JPanel
{
  /**
	 * 
	 */
	private ChartPanel localChartPanel;

public XYSeriesPlotPanel(String paramString,File dataFile)
  {
    super();
    XYDataset localXYDataset = createDataset(dataFile);
    JFreeChart localJFreeChart = createChart(localXYDataset);
    this.localChartPanel = new ChartPanel(localJFreeChart);
    this.localChartPanel.setPreferredSize(new Dimension(500, 270));
    
//    setContentPane(localChartPanel);
  }

  private static JFreeChart createChart(XYDataset paramXYDataset)
  {
    JFreeChart localJFreeChart = ChartFactory.createXYLineChart("XY Series Demo1", "X", "Y", paramXYDataset, PlotOrientation.VERTICAL, true, true, false);
    localJFreeChart.setTitle("Comparision between QM energy and MM energy");
    XYPlot plot = (XYPlot) localJFreeChart.getPlot();
    NumberAxis xAxis = new NumberAxis("xTitle");
    NumberAxis yAxis = new NumberAxis("yTitle");
    xAxis.setTickUnit(new NumberTickUnit(1.0, new java.text.DecimalFormat()));
    plot.setDomainAxis(xAxis);
    plot.setRangeAxis(yAxis);
    return localJFreeChart;
  }

  private static XYDataset createDataset(File dataFile)
  {
	    Scanner sc = null;
	    System.out.println(System.getProperty("user.dir"));
	    try {
			sc = new Scanner(dataFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int rowStep = 0;
		int xySeriesNumber = 0;
		boolean addtempXYSeries = true;
		ArrayList<XYSeries> XYSeriesArrayList= new ArrayList<XYSeries>();
	    while (sc.hasNextLine()){
	    	String line = sc.nextLine();
	    	String[] xyString = line.split("\\s+");
	    	xySeriesNumber = xyString.length;
	    	for (int col = 0;col < xySeriesNumber;col++){
	    		XYSeries tempXYSeries = new XYSeries("Column"+col);
	    		if(addtempXYSeries){
	    			XYSeriesArrayList.add(tempXYSeries);	    			
	    		}	    		
	    		XYSeriesArrayList.get(col).add((double)rowStep, Double.parseDouble(xyString[col]));
	    	}
	    	rowStep++;
	    	addtempXYSeries = false;
	    }    

	    
	    XYSeriesCollection localXYSeriesCollection = new XYSeriesCollection();
	    for (int i = 0 ; i<xySeriesNumber ; i++){
	    	localXYSeriesCollection.addSeries(XYSeriesArrayList.get(i));
	    }
	    return localXYSeriesCollection;
  }

  public static JPanel createDemoPanel(File InputFile)
  {
    JFreeChart localJFreeChart = createChart(createDataset(InputFile));
    return new ChartPanel(localJFreeChart);
  }

}
