/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Feb 16, 2007
 * 
 * Developed by: CCT, Center for Computation and Technology, 
 * 				NCSA, University of Illinois at Urbana-Champaign
 * 				OSC, Ohio Supercomputing Center
 * 				TACC, Texas Advanced Computing Center
 * 				UKy, University of Kentucky
 * 
 * https://www.gridchem.org/
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal with the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimers.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimers in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
 *    University of Illinois at Urbana-Champaign, nor the names of its contributors 
 *    may be used to endorse or promote products derived from this Software without 
 *    specific prior written permission.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS WITH THE SOFTWARE.
*/

package org.gridchem.client.gui.panels.myccg.resource;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gridchem.client.GridChem;
import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.gui.charts.ChartDataset;
import org.gridchem.client.gui.charts.ChartDataset.ChartType;
import org.gridchem.client.gui.charts.ChartDataset.LoadType;
import org.gridchem.client.gui.jobsubmission.commands.GETHARDWARECommand;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.util.Env;
import org.gridchem.service.beans.ComputeBean;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.util.SortOrder;

/**
 * Display area to hold various charts and graphs associated with the selected resource(s)
 * in the ResourcePanel.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class HPCChartPanel extends JPanel {

    private String DEFAULT_MESSAGE = "<html><p align=\"center\"><em>No resource selected.</em></p></html>";
    
    private static ChartType CURRENT_CHARTTYPE = ChartType.SUMMARY;
    private static LoadType CURRENT_LOADTYPE = LoadType.SUMMARY;
    
    private HashSet<ComputeBean> resources = new HashSet<ComputeBean>();
    
    protected JPanel chartPanel;
    protected JPanel navPanel;
    protected JButton nextButton;
    protected JButton previousButton;
    protected JButton reloadButton;
    protected JComboBox chartTypeComboBox;
    protected JComboBox loadTypeComboBox;
    
    protected static boolean zoomOnSingleChart = false;
    
    private static StatusListener statusListener;
    
    
    public HPCChartPanel(StatusListener statusListener) {
        super();
        this.statusListener = statusListener;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        
        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new GridLayout(1,2));
        captionPanel.add(new JLabel());
        captionPanel.add(new JLabel(DEFAULT_MESSAGE));
        captionPanel.add(new JLabel());
        add(captionPanel,c);
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
        
        navPanel = createSelectionBar();
        
        add(navPanel,c);
        enableSelectionBar(false);
        revalidate();
        
    }
    
    /**
     * Create a JPanel containing Meter charts representing the loads
     * of the given ComputeBean
     * @param resource
     */
    public HPCChartPanel(ComputeBean resource) {
        super();
        
        this.resources.add(resource);
        
        navPanel = createSelectionBar();
        
        init();
        
    }
    
    /**
     * Create a JPanel containing 'chartType' charts representing the loads
     * of the given ComputeBean
     * @param resource
     */
    public HPCChartPanel(ComputeBean resource,ChartType chartType) {
        super();
        
        CURRENT_CHARTTYPE = chartType;
        
        this.resources.add(resource);
        
        navPanel = createSelectionBar();
        
        init();
    }
    
    /**
     * Create a JPanel containing Meter charts representing the loads
     * of the given ComputeBeans
     * @param resource
     */
    public HPCChartPanel(HashSet<ComputeBean> resources) {
        super();
        
        this.resources = resources;
        
        navPanel = createSelectionBar();
        
        init();
    }
    
    /**
     * Create a JPanel containing 'chartType' charts representing the loads
     * of the given ComputeBeans
     * @param resource
     */
    public HPCChartPanel(HashSet<ComputeBean> resources,ChartType chartType) {
        super();
        
        CURRENT_CHARTTYPE = chartType;
        
        this.resources = resources;
        
        init();
    }
    
    private void init() {
        removeAll();
        chartPanel = createChartPanel();
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        add(chartPanel,c);
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
        add(navPanel,c);
        revalidate();
        
    }
    
    /**
     * Populate the display area with a multiple rows of graphs representing
     * the loads on each ComputeBean object within the HashSet.
     * @param resource
     * @param chartType
     */
    private JPanel createChartPanel() {
        
        JPanel chartPanel = new JPanel();
        
        if (CURRENT_CHARTTYPE.equals(ChartType.METER) || 
                CURRENT_CHARTTYPE.equals(ChartType.PIE)) {
            
            chartPanel.setLayout(new GridLayout(resources.size(),1));
            
            LoadType[] loadTypes;
            
            if (CURRENT_LOADTYPE.equals(LoadType.SUMMARY)) {
                loadTypes = LoadType.values();
            } else {
                loadTypes = new LoadType[1];
                loadTypes[0] = CURRENT_LOADTYPE;
            }
            
            for(ComputeBean resource: resources) {
                // create a row to insert in the chartPanel
                JPanel multiChartPanelRow = new JPanel();
                multiChartPanelRow.setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                
                // first part is name of the row
                c.weightx = 0;
                c.weighty = 0;
                c.gridx = 0;
                c.gridy = 0;
                c.fill = GridBagConstraints.NONE;
                multiChartPanelRow.add(new JLabel(resource.getName()));
                
                // chart row will take up the remainder of this panel.
                c.weightx = 1.0;
                c.weighty = 1.0;
                c.gridx = 0;
                c.gridy = 1;
                c.fill = GridBagConstraints.BOTH;
                
                JPanel chartRow = new JPanel();
                chartRow.setLayout(new GridLayout(1,(LoadType.values().length - 1)));
                
                
                for(int i=0; i<((loadTypes.length == 1)?1:loadTypes.length-1); i++) {
                    JFreeChart chart = createChart(resource,CURRENT_CHARTTYPE,loadTypes[i]);
    
                    ChartPanel cp = new ChartPanel(chart);
                    
                    cp.setPreferredSize(new Dimension(40, 45));
    
                    cp.addMouseListener(new MouseListener() {
                        public void mouseClicked(MouseEvent event) {
                            // if they double click on the graph, then zoom in on it and
                            // make it the single display in the screen.
                            if (event.getClickCount() == 2) {
                                if (zoomOnSingleChart) {
                                    zoomOnSingleChart = false;
                                    setChartDisplayType(CURRENT_CHARTTYPE);
                                } else {
                                    zoomOnSingleChart = true;
                                    removeAll();
                                    GridBagConstraints c = new GridBagConstraints();
                                    c.weightx = 1.0;
                                    c.weighty = 1.0;
                                    c.gridx = 0;
                                    c.gridy = 0;
                                    c.fill = GridBagConstraints.BOTH;
                                    add((ChartPanel)event.getSource(),c);
                                    c.weightx = 0;
                                    c.weighty = 0;
                                    c.gridx = 0;
                                    c.gridy = 1;
                                    add(createSelectionBar(),c);
                                    revalidate();
                                }
                            }
                            
                        }
                        public void mousePressed(MouseEvent arg0) {}
                        public void mouseReleased(MouseEvent arg0) {}
                        public void mouseEntered(MouseEvent arg0) {}
                        public void mouseExited(MouseEvent arg0) {}
                    });
                    
                    chartRow.add(cp);
                }
                multiChartPanelRow.add(chartRow,c);
                chartPanel.add(multiChartPanelRow);
                
            }
        } else {
            chartPanel.setLayout(new GridLayout(1,1));
            JFreeChart chart = createChart(resources,CURRENT_CHARTTYPE,CURRENT_LOADTYPE);
            ChartPanel cp = new ChartPanel(chart);
            cp.setPreferredSize(new Dimension(40, 45));
            chartPanel.add(cp);
        }
        
        return chartPanel;
        
    }
    
    private JPanel createSelectionBar() {
        ActionListener chartNavigationAL = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               // change the chart display via a circular scrolling
               // list of chart types.
               if (event.getSource() == nextButton) {
                   CURRENT_CHARTTYPE = CURRENT_CHARTTYPE.next();
                   chartTypeComboBox.setSelectedItem(CURRENT_CHARTTYPE);
               } else if (event.getSource() == previousButton) {
                   CURRENT_CHARTTYPE = CURRENT_CHARTTYPE.previous();
                   chartTypeComboBox.setSelectedItem(CURRENT_CHARTTYPE);
               } else if (event.getSource() == reloadButton) {
                   // TODO:call GMS.getUserVO and refresh the user's vo
                   // before refreshing this screen
                   GETHARDWARECommand command = 
                       new GETHARDWARECommand(statusListener);
                   command.getArguments().put("project.id", GridChem.project.getId());
                   statusListener.statusChanged(new StatusEvent(command,Status.START));
               }
            }
        };
        
        // create buttons with left and right icons to scroll through
        // the possible chart types, changing the chart display with
        // each click
        nextButton = new JButton(new ImageIcon(Env.getImagesDir() + "/navigation/forward.jpg"));
        nextButton.addActionListener(chartNavigationAL);
        
        previousButton = new JButton(new ImageIcon(Env.getImagesDir() + "/navigation/back.jpg"));
        previousButton.addActionListener(chartNavigationAL);
        
        reloadButton = new JButton(new ImageIcon(Env.getImagesDir() + "/navigation/reload.jpg"));
        reloadButton.addActionListener(chartNavigationAL);
        reloadButton.setPreferredSize(new Dimension(35,35));
            
        // create chart type selection combo box
        JLabel chartTypeLabel = new JLabel("Chart Type");
        chartTypeLabel.setLabelFor(chartTypeComboBox);
        chartTypeComboBox = new JComboBox(ChartType.values());
        chartTypeComboBox.setEditable(false);
        chartTypeComboBox.addActionListener(new ActionListener() {
            // change the charts depending on the selection of the chart type
            public void actionPerformed(ActionEvent e) {
                setChartDisplayType((ChartType)((JComboBox)e.getSource()).getSelectedItem());
            }
        });
        
//      create chart type selection combo box
        JLabel loadTypeLabel = new JLabel("Load Type");
        loadTypeLabel.setLabelFor(loadTypeComboBox);
        loadTypeComboBox = new JComboBox(LoadType.values());
        loadTypeComboBox.setEditable(false);
        loadTypeComboBox.addActionListener(new ActionListener() {
            // change the charts depending on the selection of the chart type
            public void actionPerformed(ActionEvent e) {
                setChartDisplayType((LoadType)((JComboBox)e.getSource()).getSelectedItem());
            }
        });
        
        JPanel buttonPanel = new JPanel();
        
        buttonPanel.add(previousButton,BorderLayout.LINE_END);
        buttonPanel.add(reloadButton,BorderLayout.LINE_END);
        buttonPanel.add(nextButton,BorderLayout.LINE_END);
        buttonPanel.add(loadTypeLabel,BorderLayout.LINE_END);
        buttonPanel.add(loadTypeComboBox,BorderLayout.LINE_END);
        buttonPanel.add(chartTypeLabel,BorderLayout.LINE_END);
        buttonPanel.add(chartTypeComboBox,BorderLayout.LINE_END);
        
        return buttonPanel;
    }
    
    private JFreeChart createChart(HashSet<ComputeBean> resources, ChartType chartType, LoadType loadType) {
        JFreeChart chart = null;
        Plot plot;
        
        AbstractDataset dataset;
        
        if (loadType.equals(LoadType.SUMMARY)) {
            dataset = ChartDataset.createDataset(resources, chartType);
        } else {
            dataset = ChartDataset.createDataset(resources, chartType,loadType);
        }
        
        if(chartType.equals(ChartType.SUMMARY)) {
            
            chart = ChartFactory.createBarChart(
                    "",  // chart title
                    "Resources",                        // domain axis label
                    createTitle(loadType),          // range axis label
                    (CategoryDataset)dataset, // data
                    PlotOrientation.VERTICAL,           // orientation
                    true,                               // include legend
                    true,                               // tooltips?
                    false                               // URLs?
            );
            
            renderBarChart(chart);
            
        } else if(chartType.equals(ChartType.PIE)) {
            
            chart = ChartFactory.createPieChart(
                    createTitle(loadType),  // chart title
                    (DefaultPieDataset)dataset, // data
                    false,                               // include legend
                    true,                               // tooltips?
                    false                               // URLs?
            );

        } else if(chartType.equals(ChartType.BAR)) {
            
            chart = ChartFactory.createBarChart(
                    "",  // chart title
                    "Resources",                        // domain axis label
                    createTitle(loadType),          // range axis label
                    (CategoryDataset)dataset, // data
                    PlotOrientation.VERTICAL,           // orientation
                    true,                               // include legend
                    true,                               // tooltips?
                    false                               // URLs?
                );
                
            renderBarChart(chart);

        } else if(chartType.equals(ChartType.LAYERED)) {
            
            plot = new CategoryPlot((CategoryDataset)dataset,
                    new CategoryAxis("Resources"), 
                    new NumberAxis(createTitle(loadType)), 
                    new LayeredBarRenderer());

            ((CategoryPlot) plot).setOrientation(PlotOrientation.VERTICAL);
            
            chart = new JFreeChart(
                    "", 
                    JFreeChart.DEFAULT_TITLE_FONT, 
                    plot, 
                    true
                );
            
            renderLayeredBarChart(chart);

        } else if(chartType.equals(ChartType.STACKED)) {
            
            chart = ChartFactory.createStackedBarChart(
                    "",  // chart title
                    "Resources",                        // domain axis label
                    createTitle(loadType),          // range axis label
                    (CategoryDataset)dataset, // data
                    PlotOrientation.VERTICAL,           // orientation
                    true,                               // include legend
                    true,                               // tooltips?
                    false                               // URLs?
            );
            
            renderStackedBarChart(chart);
            
            
        } else if(chartType.equals(ChartType.BAR)) {
           chart = ChartFactory.createBarChart(
               "",  // chart title
               "Resources",                        // domain axis label
               createTitle(loadType),          // range axis label
               (CategoryDataset)dataset,// data
               PlotOrientation.VERTICAL,           // orientation
               true,                               // include legend
               true,                               // tooltips?
               false                               // URLs?
           );
        }
        
        return chart;
        
    }
    
    private JFreeChart createChart(ComputeBean hpc, ChartType chartType,LoadType loadType) {
        
        JFreeChart chart = null;
        Plot plot;
        if(chartType.equals(ChartType.SUMMARY)) {
            
            chart = ChartFactory.createBarChart(
                    "",  // chart title
                    "Resources",                        // domain axis label
                    createTitle(loadType),          // range axis label
                    (CategoryDataset)ChartDataset.createDataset(hpc, chartType), // data
                    PlotOrientation.VERTICAL,           // orientation
                    false,                               // include legend
                    true,                               // tooltips?
                    false                               // URLs?
            );
            
            renderBarChart(chart);
            
        } else if(chartType.equals(ChartType.PIE)) {
            
            chart = ChartFactory.createPieChart(
                    createTitle(loadType),  // chart title
                    (DefaultPieDataset)ChartDataset.createDataset(hpc, chartType,loadType), // data
                    false,                               // include legend
                    true,                               // tooltips?
                    false                               // URLs?
            );
            
        } else if(chartType.equals(ChartType.METER)) {
            
            plot = new MeterPlot(
                    (ValueDataset)ChartDataset.createDataset(hpc, chartType,loadType));
            
            chart = new JFreeChart(createTitle(loadType),
                    JFreeChart.DEFAULT_TITLE_FONT, 
                    plot, 
                    false); 
            
            renderMeterChart(chart, loadType, hpc);
            
        } else if(chartType.equals(ChartType.BAR)) {
            
            chart = ChartFactory.createBarChart(
                    "",  // chart title
                    "Resources",                        // domain axis label
                    createTitle(loadType),          // range axis label
                    (CategoryDataset)ChartDataset.createDataset(hpc, chartType), // data
                    PlotOrientation.VERTICAL,           // orientation
                    false,                               // include legend
                    true,                               // tooltips?
                    false                               // URLs?
                );
                
            renderBarChart(chart);

        } else if(chartType.equals(ChartType.LAYERED)) {
            
            plot = new CategoryPlot((CategoryDataset) ChartDataset.createDataset(hpc, chartType),
                    new CategoryAxis("Resources"), 
                    new NumberAxis(createTitle(loadType)), 
                    new LayeredBarRenderer());

            ((CategoryPlot) plot).setOrientation(PlotOrientation.VERTICAL);
            
            chart = new JFreeChart(
                    "", 
                    JFreeChart.DEFAULT_TITLE_FONT, 
                    plot, 
                    false
                );
            
            renderLayeredBarChart(chart);

        } else if(chartType.equals(ChartType.STACKED)) {
            
            chart = ChartFactory.createStackedBarChart(
                    "",  // chart title
                    "Resources",                        // domain axis label
                    createTitle(loadType),          // range axis label
                    (CategoryDataset)ChartDataset.createDataset(hpc, chartType), // data
                    PlotOrientation.VERTICAL,           // orientation
                    false,                               // include legend
                    true,                               // tooltips?
                    false                               // URLs?
            );
            
            renderStackedBarChart(chart);
            
            
        } else if(chartType.equals(ChartType.BAR)) {
           chart = ChartFactory.createBarChart(
               "",  // chart title
               "Resources",                        // domain axis label
               createTitle(loadType),          // range axis label
               (CategoryDataset) ChartDataset.createDataset(hpc, chartType),// data
               PlotOrientation.VERTICAL,           // orientation
               false,                               // include legend
               true,                               // tooltips?
               false                               // URLs?
           );
           
           renderBarChart(chart);
        }
        
        
        return chart;
    }
    
    
    /**
     * Renders the bar chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart renderBarChart(JFreeChart chart) {

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setUpperBound(100);
        
        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, Color.black
        );
        final GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, Color.black
        );
        final GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, Color.black
        );
        final GradientPaint gp3 = new GradientPaint(
                0.0f, 0.0f, Color.orange, 
                0.0f, 0.0f, Color.black
            );
        
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        renderer.setSeriesPaint(3, gp3);
        
//        final CategoryAxis domainAxis = plot.getDomainAxis();
//        domainAxis.setCategoryLabelPositions(
//            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
//        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        return chart;
    }
    
    /**
     * This lays the layers over each other
     * @param chart
     */
    private void renderLayeredBarChart(JFreeChart chart) {
//      get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setRowRenderingOrder(SortOrder.DESCENDING);
        
        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setUpperBound(100);
        
        // disable bar outlines...
        final LayeredBarRenderer renderer = (LayeredBarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        
        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, Color.black
        );
        final GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, Color.black
        );
        final GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, Color.black
        );
        final GradientPaint gp3 = new GradientPaint(
                0.0f, 0.0f, Color.orange, 
                0.0f, 0.0f, Color.black
            );
        
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        renderer.setSeriesPaint(3, gp3);
        
    }
    
    private void renderMeterChart(JFreeChart chart, LoadType loadType, ComputeBean hpc) {
        
        MeterPlot plot = (MeterPlot)chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        
        plot.addInterval(new MeterInterval("High", new Range(85.0, 100.0),Color.RED,new BasicStroke(),Color.RED));
        plot.addInterval(new MeterInterval("Low", new Range(0.0, 85.0)));
        plot.setDialOutlinePaint(Color.blue);
        plot.setNeedlePaint(Color.WHITE);
        plot.setDialBackgroundPaint(Color.BLACK);
        plot.setTickPaint(Color.WHITE);
        plot.setTickLabelsVisible(true);
        plot.setTickLabelPaint(Color.WHITE);
        if (loadType.equals(LoadType.DISK)) {
            plot.setUnits("% peak (" + hpc.getLoad().getDisk() + 
                    "GB/" + hpc.getTotalDisk() + "GB)");
        } else if (loadType.equals(LoadType.CPU)) {
            plot.setUnits("% peak (" + hpc.getLoad().getCpu() + 
                    "/" + hpc.getTotalCpu() + ")");
        } else if (loadType.equals(LoadType.QUEUE)){
            plot.setUnits("% peak (" + hpc.getLoad().getJobsRunning() + 
                    "R/" + (hpc.getLoad().getJobsOther() + hpc.getLoad().getJobsQueued()) +
                    ")O");
        } else {
            plot.setUnits("% Relative to Max");
        }
        
    }
    
    private void renderStackedBarChart(JFreeChart chart) {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        
        
        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setUpperBound(100);
        
        StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);
        renderer.setMaximumBarWidth(1.0);
        
        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, Color.black
        );
        final GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, Color.black
        );
        final GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, Color.black
        );
        final GradientPaint gp3 = new GradientPaint(
                0.0f, 0.0f, Color.orange, 
                0.0f, 0.0f, Color.black
            );
        
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        renderer.setSeriesPaint(3, gp3);
        
    }
    
    protected void enableSelectionBar(boolean enabled) {
        nextButton.setEnabled(enabled);
        previousButton.setEnabled(enabled);
        reloadButton.setEnabled(enabled);
        chartTypeComboBox.setEnabled(enabled);
        loadTypeComboBox.setEnabled(enabled);
    }
    
    protected String createTitle(LoadType loadType) {
        String title = "";
        if (loadType.equals(LoadType.DISK)) {
            // disk dataset
            title =  "% Globally Used Disk";
        } else if (loadType.equals(LoadType.CPU)) {
            // queue dataset
            title = "% Available CPU's";
        } else if (loadType.equals(LoadType.QUEUE)){
            title = "Ratio of Running vs Pending";
        } else {
            title = "Relative Percentage";
        }
        
        return title;
    }
    
    
    public void setChartDisplayType(ChartType chartType) {
        this.removeAll();
        
        CURRENT_CHARTTYPE = chartType;
        
        init();

        revalidate();
    }
    
    public void setChartDisplayType(LoadType loadType) {
        this.removeAll();
        
        CURRENT_LOADTYPE = loadType;
        
        init();
        
        revalidate();
    }
    
    public void addResource(ComputeBean hpc) {
        this.resources.add(hpc);
        
        init();
        
        enableSelectionBar(true);
        
        revalidate();
    }
    
    public void setResources(HashSet<ComputeBean> resources) {
        this.resources.clear();
        this.resources.addAll(resources);
        
        init();
        
        enableSelectionBar(true);
        
        revalidate();
    }
    
    public void setResource(ComputeBean resource) {
        this.resources.clear();
        this.resources.add(resource);
        
        init();
        
        enableSelectionBar(true);
        
        revalidate();
    }
    
    public void clearCharts() {
        this.resources.clear();
        
        removeAll();
        
        enableSelectionBar(false);
        
        revalidate();
    }
    
}
