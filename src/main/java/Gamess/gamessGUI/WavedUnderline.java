package Gamess.gamessGUI;

import java.awt.Graphics;
import java.awt.Shape;

import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

public class WavedUnderline extends DefaultHighlighter
{

	@Override
	public void paintLayeredHighlights(Graphics g, int p0, int p1, Shape viewBounds, JTextComponent editor, View view) {
		super.paintLayeredHighlights(g, p0, p1, viewBounds, editor, view);
		WavedUnderlinePen pen = (WavedUnderlinePen)view.getAttributes().getAttribute("UNDERLINE");
		if(pen != null)
			pen.paintLayer(g, p0, p1, viewBounds, editor, view);
	}
	
}
