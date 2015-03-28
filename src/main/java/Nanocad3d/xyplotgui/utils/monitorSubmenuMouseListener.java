package Nanocad3d.xyplotgui.utils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import Nanocad3d.xyplotgui.frame.OperationFrame;

public class monitorSubmenuMouseListener implements MouseListener {

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
//		OperationFrame.main("Start xyplot");
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				OperationFrame.startXYPlotFrame();
			}
		} );
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
