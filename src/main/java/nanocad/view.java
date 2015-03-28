package nanocad;

/**
 * view.java - camera angle, XYZ-to-screen coord translation, perspective
 * Copyright (c) 1997,1998 Will Ware, all rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and other materials provided with the distribution.
 * 
 * This software is provided "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability
 * or fitness for any particular purpose are disclaimed. In no event shall
 * Will Ware be liable for any direct, indirect, incidental, special,
 * exemplary, or consequential damages (including, but not limited to,
 * procurement of substitute goods or services; loss of use, data, or
 * profits; or business interruption) however caused and on any theory of
 * liability, whether in contract, strict liability, or tort (including
 * negligence or otherwise) arising in any way out of the use of this
 * software, even if advised of the possibility of such damage.
 */

import java.lang.Math;
import java.lang.Double;

public class view
{
  public static final String rcsid =
	"$Id: view.java,v 1.2 2005/05/14 23:51:01 xli16 Exp $";
    public double zoomFactor = 25; // pixels per angstrom
    public double perspDist = 400; // z distance for perspective, in pixels
    private double xCenter = 200;
    private double yCenter = 200;
    private double zCenter = 0;
    private int renorm_counter = 0;
    private textwin tw;
    public void pan (int dx, int dy)
    {
	  xCenter += dx;
	  yCenter += dy;
	  /*	  tw = new textwin("in view", "", false);
		  tw.setVisible(true);
		  tw.setText("xCent moves = "+ dx +" yCent = " +dy);
	  */
	}

  public double perspectiveFactor (double[] scrPos)
	{
	  return perspectiveFactor (scrPos[2] - zCenter);
	}
  public double perspectiveFactor (double z)
	{
	  double denom = perspDist - z;
	  if (denom < 5) denom = 5;
	  return perspDist / denom;
	}
	  
  public double[] screenToXyz (double[] vec)
	{
	  double x, y, z, denom;
	  double[] rvec = new double[3];
	  // undo translation
	  x = vec[0] - xCenter;
	  y = vec[1] - yCenter;
	  z = vec[2] - zCenter;
	  // undo persective
	  double perspective = perspectiveFactor (z);
	  x /= perspective;
	  y /= perspective;
	  // undo zoom
	  rvec[0] = x / zoomFactor;
	  rvec[1] = y / zoomFactor;
	  rvec[2] = z / zoomFactor;
	  
	  return rvec;
	}
  public void updateSize (int x, int y)
	{
	  xCenter = x / 2;
	  yCenter = y / 2;
	  zCenter = 0;
	}
  
  public double[] xyzToScreen (double[] xyz)
	{
	  double x, y, z, denom;
	  double[] rvec = new double[3];
	  // zoom
	  x = zoomFactor * xyz[0];
	  y = zoomFactor * xyz[1];
	  z = zoomFactor * xyz[2];
	  // perspective
	  double perspective = perspectiveFactor (z);
	  x *= perspective;
	  y *= perspective;
	  // translation
	  rvec[0] = x + xCenter;
	  rvec[1] = y + yCenter;
	  rvec[2] = z + zCenter;
	  return rvec;
	}
 
}
