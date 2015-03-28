package Nanocad3d.xyplotgui.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Nanocad3d.xyplotgui.frame.OperationPanel;


public class OperationFrame extends JFrame {
	
	private final int XFrameSize = 500;
	private final int YFrameSize = 500;
	private JLabel statusLabel = new JLabel("Running status shown here");
	

	public OperationFrame(String string) {
		this.setTitle(string);
		this.setSize(XFrameSize, YFrameSize);
		this.statusLabel.setName("xyplotStatusBar");
		
	}
	
	public static void startXYPlotFrame() {
		OperationFrame frame1 = new OperationFrame("XYPlot");
		
		JPanel OperationPanel = new OperationPanel();
		frame1.getRootPane().setDefaultButton(((OperationPanel) OperationPanel).getGenerateXYPlotFrom());
		
		OperationPanel.setBorder(BorderFactory.createTitledBorder("Operation Panel"));
		frame1.add(OperationPanel,BorderLayout.PAGE_START);
		
		frame1.statusLabel.setText("Start XYPlot");
		frame1.add(frame1.statusLabel,BorderLayout.SOUTH);
		frame1.setVisible(true);
	}


	public static void main(String[] args) {
		OperationFrame frame1 = new OperationFrame("XYPlot");
//		File DataFile = new File("data.txt");
//		System.out.println(DataFile.getAbsolutePath());
		
		JPanel OperationPanel = new OperationPanel();
		OperationPanel.setBorder(BorderFactory.createTitledBorder("Operation Panel"));
		
		HashMap OperationPanelHash = new HashMap<String,Component>();
		OperationPanelHash = Nanocad3d.xyplotgui.utils.ComponentUtil.createComponentMap(OperationPanel);
		
		
//		JPanel XYplotPanel = Nanocad3d.xyplotgui.frame.XYSeriesPlotPanel.createDemoPanel(DataFile);
//		XYplotPanel.setBorder(BorderFactory.createTitledBorder("XYPlot Panel"));
		
//		gui.utils.ComponentUtil.AppendComponentMap(XYplotPanel, OperationPanelHash);
		
		frame1.add(OperationPanel,BorderLayout.PAGE_START);
		
//		frame1.add(XYplotPanel,BorderLayout.CENTER);
		frame1.setVisible(true);
	}
}
