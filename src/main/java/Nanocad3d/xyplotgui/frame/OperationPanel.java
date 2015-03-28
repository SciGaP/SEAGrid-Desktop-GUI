package Nanocad3d.xyplotgui.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import Nanocad3d.xyplotgui.utils.ComponentUtil;
import Nanocad3d.xyplotgui.utils.GuiActionListener;




public class OperationPanel extends JPanel{
	
//	private JButton SaveRemoteFile = new JButton("SaveRemoteFile");
	private JButton SaveRemoteFileTo = new JButton("SaveRemoteFileTo");
	private JButton GenerateXYPlotFrom = new JButton("GenerateXYPlotFrom");
	private JLabel InfoLabel = new JLabel("Specify the Path of Remote File");
	private JTextField RemoteFilePath = new JTextField("Type in full path of remote file");
	private GridBagLayout PanelLayout = new GridBagLayout();
//	private static final Insets insets = new Insets(0,0,0,0);
	private final int XPanelSize = 500;
	private final int YPanelSize = 150;

	public JButton getSaveRemoteFileTo() {
		return SaveRemoteFileTo;
	}
	public JButton getGenerateXYPlotFrom() {
		return GenerateXYPlotFrom;
	}
	public JLabel getInfoLabel() {
		return InfoLabel;
	}
	public JTextField getRemoteFilePath() {
		return RemoteFilePath;
	}
	public GridBagLayout getPanelLayout() {
		return PanelLayout;
	}
	
	public OperationPanel() {
		this.setSize(XPanelSize, YPanelSize);
		this.setLayout(PanelLayout);
		this.setSize(XPanelSize, YPanelSize/2);
		this.InfoLabel.setName("InfoLabel");
		this.RemoteFilePath.setName("RemoteFilePath");
		this.RemoteFilePath.setToolTipText("Type in full path of remote file");
		this.RemoteFilePath.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "EnterAction");
		
//		Action EnterAction = new AbstractAction("EnterAction") {
//			public void actionPerformed(ActionEvent evt) {				
//			}
//		};
//		this.RemoteFilePath.getActionMap().put("EnterAction", EnterAction);
		
		



//		this.SaveRemoteFileTo.setName("SaveRemoteFileTo");
//		this.SaveRemoteFileTo.setToolTipText("Retrieve remote file to local disk at default gridchem file location");
		this.GenerateXYPlotFrom.setName("GenerateXYPlotFrom");
		this.GenerateXYPlotFrom.setToolTipText("Plot energy of quantum calculation and MM calculation");
//		this.getRootPane().setDefaultButton(this.GenerateXYPlotFrom);
		
//		this.SaveRemoteFileTo.addActionListener(new GuiActionListener(this.SaveRemoteFileTo.getName() + " : " + SwingUtilities.windowForComponent(this.InfoLabel)));
//		this.GenerateXYPlotFrom.addActionListener(new GuiActionListener(this.GenerateXYPlotFrom.getName()+ " : " + SwingUtilities.windowForComponent(this.GenerateXYPlotFrom)));		

//		this.SaveRemoteFileTo.addActionListener(new GuiActionListener(this.SaveRemoteFileTo.getName()));
		this.GenerateXYPlotFrom.addActionListener(new GuiActionListener(this.GenerateXYPlotFrom.getName()));		
		
		this.addGBComponent(InfoLabel, 0, 0, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.FIRST_LINE_START, new Insets(10,0,0,10), 0, 0);
		this.addGBComponent(RemoteFilePath, 1,0, 2, 0, GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.PAGE_START, new Insets(10,10,0,10), 0, 0);
//		this.addGBComponent(SaveRemoteFileTo, 0, 1, 1, 1, GridBagConstraints.NONE, 0, 1, GridBagConstraints.LINE_START, new Insets(10,0,10,10), 0, 0);
		this.addGBComponent(GenerateXYPlotFrom, 1, 1, 1, 1, GridBagConstraints.NONE, 0.2, 1, GridBagConstraints.LINE_END, new Insets(10,0,10,10), 0, 0);
		
	}
	
    private void addGBComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight,
            int fill, double weightx, double weighty, int anchor, Insets insets,
            int ipadx, int ipady) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.fill = fill;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.anchor = anchor;
        constraints.insets = insets;
        constraints.ipadx = ipadx;
        constraints.ipady = ipady;
        add(component, constraints);
    }

}
