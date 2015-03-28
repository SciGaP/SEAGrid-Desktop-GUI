package Gamess.gamessGUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

public class WavedUnderlinePen extends DefaultHighlightPainter
{
	public WavedUnderlinePen(Color color) {
		super(color);
	}

	@Override
	public Shape paintLayer(Graphics g, int p0, int p1, Shape viewBounds, JTextComponent editor, View view) {
		if(viewBounds instanceof Rectangle)
		{
			//Get the bounds
			Rectangle underlineArea = viewBounds.getBounds();
			g.setColor(super.getColor());
			
			//Divide the total width into many division
			//Set the width and the height of the single division
			int divisionWidth = 2;
			int divisionHeight = underlineArea.height - 3;
			//get the number of division
			int count = underlineArea.width/divisionWidth;
			for (int i = 0; i < count; i++) {
				//Start x
				int x1, y1, x2 ,y2;
				
				x1 = i * divisionWidth + underlineArea.x;
				x2 = (i + 1) * divisionWidth + underlineArea.x;

				if(i%2 == 0)
				{
					y1 = underlineArea.y + underlineArea.height - 1;
					y2 = underlineArea.y + divisionHeight;
				}
				else
				{
					y1 = underlineArea.y + divisionHeight;
					y2 = underlineArea.y + underlineArea.height - 1;
				}
				 
				g.drawLine(x1, y1, x2, y2);
			}
			return underlineArea;
		}
		return super.paintLayer(g, p0, p1, viewBounds, editor, view);
	}
}