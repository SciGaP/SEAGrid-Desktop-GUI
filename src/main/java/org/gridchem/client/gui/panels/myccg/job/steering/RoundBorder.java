/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Mar 20, 2007
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

package org.gridchem.client.gui.panels.myccg.job.steering;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.Border;

/**
 * Insert Template description here.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
class RoundBorder implements Border
{
    protected int etchType, radius;
    protected Color highlight, shadow;
    public static final int
        RAISED  = 0,
        LOWERED = 1;
 
    public RoundBorder()
    {
        this(LOWERED, 20);
    }
 
    public RoundBorder(int etchType, int radius)
    {
        this(etchType, radius, null, null);
    }
 
    public RoundBorder(int etchType, Color highlight, Color shadow)
    {
        this(etchType, 20, highlight, shadow);
    }
 
    public RoundBorder(int etchType, int radius, Color highlight, Color shadow)
    {
        this.etchType = etchType;
        this.radius = radius;
        this.highlight = highlight;
        this.shadow = shadow;
    }
 
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        int w = width;
        int h = height;
        int r = radius;
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(x, y);
 
        if(etchType == LOWERED)
        {
            g.setColor(getShadowColor(c));
            g.drawRoundRect(0, 0, w-2, h-2, 2*r, 2*r);
        }
 
        g.setColor(etchType == LOWERED ? getHighlightColor(c) : getShadowColor(c));
        g.drawLine(1, h-r-2, 1, r+1);
        g.drawLine(r+1, 1, w-r-2, 1);
 
        g.drawLine(r, h-1, w-r-1, h-1);
        g.drawLine(w-1, h-r-1, w-1, r);
 
        int ax = 1;
        int ay = 1;
        int arcSide = 2*r;
        int startAngle = 90;
        int arcAngle = 90;
        g.drawArc(ax, ay, arcSide, arcSide, startAngle, arcAngle); // nw
 
        ax = 1;
        ay = h-2*r-1;
        startAngle = 180;
        g.drawArc(ax, ay, arcSide, arcSide, startAngle, arcAngle); // sw
 
        ax = w-2*r-1;
        ay = h-2*r-1;
        startAngle = -90;
        g.drawArc(ax, ay, arcSide, arcSide, startAngle, arcAngle); // se
 
        ax = w-2*r-1;
        ay = 1;
        startAngle = 0;
        g.drawArc(ax, ay, arcSide, arcSide, startAngle, arcAngle); // ne
 
        if(etchType == RAISED)
        {
            g.setColor(getHighlightColor(c));
            g.drawRoundRect(0, 0, w-2, h-2, 2*r, 2*r);
        }
 
        g.translate(-x, -y);
    }
 
    public Insets getBorderInsets(Component c)
    {
        int d = (int)(radius * Math.cos(Math.PI/4) + 2);
        return new Insets(d,d,d,d);
    }
 
    public Insets getBorderInsets(Component c, Insets insets)
    {
        int d = (int)(radius * Math.cos(Math.PI/4) + 2);
        insets.left = insets.top = insets.right = insets.bottom = d;
        return insets;
    }
 
    public boolean isBorderOpaque() { return false; }
 
    public int getEtchType() { return etchType; }
 
    public Color getHighlightColor(Component c)
    {
        return highlight != null ? highlight : c.getBackground().brighter();
    }
 
    public Color getHighLightColor() { return highlight; }
 
    public Color getShadowColor(Component c)
    {
        return shadow != null ? shadow : c.getBackground().darker();
    }
 
    public Color getShadowColor() { return shadow; }
}