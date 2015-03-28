/*

Copyright (c) 2005, Center for Computational Sciences, University of Kentucky.  All rights reserved.

Developed by:

Center for Computational Sciences, University of Kentucky

http://www.ccs.uky.edu/

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software"), to deal with the Software without 
restriction, including without limitation the rights to use, 
copy, modify, merge, publish, distribute, sublicense, and/or 
sell copies of the Software, and to permit persons to whom 
the Software is furnished to do so, subject to the following 
conditions:
1. Redistributions of source code must retain the above copyright notice, 
   this list of conditions and the following disclaimers.
2. Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimers in the documentation
   and/or other materials provided with the distribution.
3. Neither the names of Center for Computational Sciences, University of Kentucky, 
   nor the names of its contributors may be used to endorse or promote products 
   derived from this Software without specific prior written permission.
    
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS WITH THE SOFTWARE.
*/


/**
 * Created on Sep 11, 2005
 * @author Michael Sheetz 
 * @author Sandeep Kumar Seethaapathy @author Shashank Jeedigunta  
 * 
 */
package G03Input;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.*;
import java.util.*;
import javax.accessibility.*;
public class BufferedDisplayMolecule extends JFrame
{
    DisplayCanvas canvas;

    public BufferedDisplayMolecule()      // Constructor
    {
        super("Geometric Structure");
        canvas = new DisplayCanvas();
        canvas.clearCanvas();
       try
	  {
     	G03MenuTree.molBrowser.remove(0);
	  }
      catch(RuntimeException e){
      	e.printStackTrace();
      }
      G03MenuTree.molBrowser.add(canvas);
      addWindowListener(new WindowEventHandler());
	  }
      
    

    // Define window listener class
    class WindowEventHandler extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            System.exit(0);
        }
    }

    // Define DisplayCanvas class
    class DisplayCanvas extends Canvas
    {
        int i, j, k, k1, k2, k3, index, minIndex;
        int natoms, nelem, nbonds, ncoord, natomCoord, nprojectedCoord, ndisplayCoord, nouterCoord, total_ncoord;
        int atomDiameter;
        int fontStyle, fontSize;
        int rotAxis;
        int[] bondedAtoms;
        int[] bondOrder;
        int[] displayOrder;

        double alpha, rotAngle, x_rotAngle, y_rotAngle, z_rotAngle;
        double delx, dely, xscale, yscale, scaleFactor, prcnt, min;
        double[] atomCoord;
        double[] transfCoord;
        double[] transfCoord_y;
        double[] projectedCoord;
        double[] displayCoord;
        double[] outerCoord;
        double[] imageBounds;
        double[] clippingBounds;
        double sqDist, dist;

        String atomArray;
        String coordArray;
        String fontName;
        String[] atomSymb;
        StringBuffer coordinates;

        Color background;
        Color xaxisColor, yaxisColor, zaxisColor, bondColor;
        Color[] atomColors;

        Ellipse2D atom;
        Rectangle2D xrec, yrec, zrec;
        Font font1, font2;
        boolean isRotatable;

        BufferedImage offscreenImage;

        // Specify size of display screen
        int displayWidth;
        int displayHeight;

        double lamda;
        double x1, y1, x2, y2, x3, y3, x4, y4;
        double x11, x21, y11, y21;
        double x12, x22, y12, y22;
        double x13, x23, y13, y23;
        double mx_init, mx_current, del_mx;
        Line2D line;

        Hashtable<String,String> bondLengths;

        public void clearCanvas()
        {
        Graphics g=canvas.offscreenImage.getGraphics();
        canvas.offscreenImage.flush();
        update(g);
        }

        DisplayCanvas()      // Constructor
        {
            displayWidth = 250;
            displayHeight = 250;

            // The coordinate array, coordArray, will be read in as a string from molecular specification section
            // of the G03 input file
            coordinates = new StringBuffer();
//            coordinates.append("0.000 0.000 0.000 0.000 0.000 1.505 1.047 0.000 -0.651 -1.000 -0.006 -0.484 ");
//            coordinates.append("-0.735 0.755 1.898 -0.295 -1.024 1.866 1.242 0.364 2.065 1.938 -0.001 1.499");
            
            
            for(int i=0;i< GeometryEditor.GeometryReader.atomCoord.length;i++)
            {
            	coordinates.append(GeometryEditor.GeometryReader.atomCoord[i]+" ");
            }
            
        /*    
            coordinates.append("0.00 0.00 0.00 1.93 0.00 0.00 -1.93 0.00 0.00 0.00 1.93 0.00 0.00 -1.93 0.00 0.00 0.00 1.93 0.00 0.00 -1.93 ");
            coordinates.append("3.07 0.00 0.00 -3.07 0.00 0.00 0.00 3.07 0.00 0.00 -3.07 0.00 0.00 0.00 -3.07 0.00 0.00 3.07");
          */  coordArray = new String(coordinates.toString());

            // Atomic symbols will be read in from molecule specification section of G03 input file
//            atomArray = new String("C C O H H H O H");
            atomArray = "";
            // atomSymb manipulated to get atom names (UKY)
            for(int p = 0 ;p < GeometryEditor.GeometryReader.atomSymb.length;p++)
            {
            atomArray += GeometryEditor.GeometryReader.atomSymb[p]+" ";	
            }
            //atomArray = new String("Ba C C C C C C O O O O O O");
//            atomArray = new String("F C C C C C C O O O O O O");

            // Get number of atoms in molecule
            natoms = getAtomCount(atomArray);

            ncoord = 3;
            natomCoord = ncoord * natoms;
            nelem = natoms * natoms;
            
            rotAxis = 0;
            nprojectedCoord = 2 * natoms;
            ndisplayCoord = 2 * natoms;
            nouterCoord = 4;
            prcnt = 0.60;
            alpha = Math.PI/6;
            rotAngle = 0;
            x_rotAngle = 0;
            y_rotAngle = 0;
            z_rotAngle = 0;
            rotAxis = 1;
            isRotatable = false;

            // Set diameter of atom in display
            atomDiameter = 35;

            // Set parameters of line segments used to draw the three axes
            x1 = 0.145 * displayWidth;                       // origin
            y1 = x1;
            x2 = 1.6 * x1;                                   // x-axis
            y2 = y1;
            x3 = x1;
            y3 = 0.4 * y1;                                   // z-axis
            x4 = x1 * 1.1 * (1.0 - (Math.cos(alpha))/2);
            y4 = y1 * (1.0 + (Math.sin(alpha))/2);           // y-axis

            background = new Color(255, 255, 255);
            xaxisColor = new Color(200, 0, 25);
            yaxisColor = new Color(200, 200, 200);
            zaxisColor = new Color(0, 110, 75);
            bondColor = new Color(155, 155, 155);
            atomColors = new Color[natoms];

            bondOrder = new int[nelem];
            displayOrder = new int[natoms];
            atomCoord = new double[natomCoord];
            projectedCoord = new double[nprojectedCoord];
            transfCoord = new double[natomCoord];
            transfCoord_y = new double[natoms];
            displayCoord = new double[ndisplayCoord];
            outerCoord = new double[nouterCoord];
            imageBounds = new double[4];
            clippingBounds = new double[4];

            offscreenImage = new BufferedImage(displayWidth, displayHeight, BufferedImage.TYPE_INT_RGB);

            addMouseListener(new MyMouseListener());
            addMouseMotionListener(new MyMouseMotionListener());
            setSize(displayWidth, displayWidth);
            setBackground(background);       

            // Set initial clipping bounds
            clippingBounds[0] = 0; clippingBounds[1] = 0; clippingBounds[2] = displayWidth; clippingBounds[3] = displayHeight;

            // Construct hashtable for retrieving bond lengths
            bondLengths = new Hashtable<String,String>();

            // All these new String's are redundant, ie, this works as well:
            // bondLengths.put("Ag-Ag", "2.89 0.2 0.1");
            bondLengths.put("Ag-Ag", new String("2.89 0.2 0.1"));

            bondLengths.put("Al-Al", new String("2.87 0.2 0.1"));
            bondLengths.put("Al-C", new String("1.96 0.2 0.1"));

            bondLengths.put("As-As", new String("2.11 0.2 0.1"));
            bondLengths.put("As-H", new String("1.52 0.2 0.1"));
            bondLengths.put("As-Br", new String("2.33 0.2 0.1"));
            bondLengths.put("As-C", new String("1.97 0.2 0.1"));
            bondLengths.put("As-Cl", new String("2.18 0.2 0.1"));
            bondLengths.put("As-F", new String("1.72 0.2 0.1"));
            bondLengths.put("As-H", new String("1.53 0.2 0.1"));
            bondLengths.put("As-O", new String("1.80 0.2 0.1"));

            bondLengths.put("Au-Au", new String("2.89 0.2 0.1"));

            bondLengths.put("B-B", new String("1.59 0.2 0.1")); 
            bondLengths.put("B-C", new String("1.58 0.2 0.1"));
            bondLengths.put("B-Cl", new String("1.78 0.2 0.1"));
            bondLengths.put("B-H", new String("1.22 0.2 0.1"));

            bondLengths.put("Be-Be", new String("2.23 0.2 0.1"));
            bondLengths.put("Be-C", new String("1.70 0.2 0.1"));

            bondLengths.put("Bi-Bi", new String("3.10 0.2 0.1"));
            bondLengths.put("Bi-C", new String("2.26 0.2 0.1"));
            bondLengths.put("Bi-Cl", new String("2.50 0.2 0.1"));

            bondLengths.put("Br-As", new String("2.33 0.2 0.1"));
            bondLengths.put("Br-Br", new String("2.29 0.2 0.1"));
            bondLengths.put("Br-C", new String("1.95 0.2 0.1"));
            bondLengths.put("Br-Cl", new String("2.15 0.2 0.1"));
            bondLengths.put("Br-F", new String("1.77 0.2 0.1"));
            bondLengths.put("Br-Ge", new String("2.31 0.2 0.1"));
            bondLengths.put("Br-H", new String("1.42 0.2 0.1"));
            bondLengths.put("Br-I", new String("2.47 0.2 0.1"));
            bondLengths.put("Br-N", new String("2.16 0.2 0.1"));
            bondLengths.put("Br-P", new String("2.22 0.2 0.1"));
            bondLengths.put("Br-S", new String("2.24 0.2 0.1"));
            bondLengths.put("Br-Sb", new String("2.53 0.2 0.1"));
            bondLengths.put("Br-Si", new String("2.19 0.2 0.1"));
            bondLengths.put("Br-Sn", new String("2.53 0.2 0.1"));

            bondLengths.put("C-Al", new String("1.97 0.2 0.1"));
            bondLengths.put("C-As", new String("1.97 0.2 0.1"));
            bondLengths.put("C-B", new String("1.58 0.2 0.1"));
            bondLengths.put("C-Be", new String("1.70 0.2 0.1"));
            bondLengths.put("C-Bi", new String("2.26 0.2 0.1"));
            bondLengths.put("C-Br", new String("1.95 0.2 0.1"));
            bondLengths.put("C-C", new String("1.54 1.35 1.21"));
            bondLengths.put("C-Cd", new String("2.11 0.2 0.1"));
            bondLengths.put("C-Cl", new String("1.80 0.2 0.1"));
            bondLengths.put("C-Co", new String("1.85 0.2 0.1"));
            bondLengths.put("C-Cr", new String("1.96 0.2 0.1"));
            bondLengths.put("C-F", new String("1.40 0.2 0.1"));
            bondLengths.put("C-Fe", new String("1.86 0.2 0.1"));
            bondLengths.put("C-Ge", new String("1.96 0.2 0.1"));
            bondLengths.put("C-H", new String("1.10 0.2 0.1"));
            bondLengths.put("C-Hg", new String("2.08 0.2 0.1"));
            bondLengths.put("C-I", new String("2.13 0.2 0.1"));
            bondLengths.put("C-In", new String("2.20 0.2 0.1"));
            bondLengths.put("C-Mo", new String("2.12 0.2 0.1"));
            bondLengths.put("C-N", new String("1.46 1.22 1.17"));
            bondLengths.put("C-Ni", new String("1.85 0.2 0.1"));
            bondLengths.put("C-O", new String("1.42 1.22 1.15"));
            bondLengths.put("C-P", new String("1.85 0.2 0.1"));
            bondLengths.put("C-Pb", new String("2.24 0.2 0.1"));
            bondLengths.put("C-Pd", new String("2.31 0.2 0.1"));
            bondLengths.put("C-S", new String("1.82 1.62 0.1"));
            bondLengths.put("C-Sb", new String("2.22 0.2 0.1"));
            bondLengths.put("C-Se", new String("1.95 0.2 0.1"));
            bondLengths.put("C-Si", new String("1.87 0.2 0.1"));
            bondLengths.put("C-Sn", new String("2.14 0.2 0.1"));
            bondLengths.put("C-Te", new String("2.19 0.2 0.1")); 
            bondLengths.put("C-W", new String("2.07 0.2 0.1")); 
            bondLengths.put("C-Zn", new String("1.93 0.2 0.1"));

            bondLengths.put("Cd-C", new String("2.11 0.2 0.1"));

            bondLengths.put("Cl-As", new String("2.18 0.2 0.1"));
            bondLengths.put("Cl-B", new String("1.78 0.2 0.1"));
            bondLengths.put("Cl-Bi", new String("2.50 0.2 0.1"));
            bondLengths.put("Cl-Br", new String("2.15 0.2 0.1"));
            bondLengths.put("Cl-C", new String("1.80 0.2 0.1"));
            bondLengths.put("Cl-Cl", new String("1.99 0.2 0.1"));
            bondLengths.put("Cl-Cr", new String("2.14 0.2 0.1"));
            bondLengths.put("Cl-H", new String("1.29 0.2 0.1"));
            bondLengths.put("Cl-Ge", new String("2.13 0.2 0.1"));
            bondLengths.put("Cl-F", new String("1.64 0.2 0.1"));
            bondLengths.put("Cl-I", new String("2.32 0.2 0.1"));
            bondLengths.put("Cl-N", new String("1.90 0.2 0.1"));
            bondLengths.put("Cl-O", new String("1.70 0.2 0.1"));
            bondLengths.put("Cl-P", new String("2.04 0.2 0.1"));
            bondLengths.put("Cl-Pb", new String("2.45 0.2 0.1"));
            bondLengths.put("Cl-S", new String("2.05 0.2 0.1"));
            bondLengths.put("Cl-Sb", new String("2.33 0.2 0.1"));
            bondLengths.put("Cl-Si", new String("2.05 0.2 0.1"));
            bondLengths.put("Cl-Sn", new String("2.28 0.2 0.1"));

            bondLengths.put("Cr-C", new String("1.96 0.2 0.1"));
            bondLengths.put("Cr-Cl", new String("2.14 0.2 0.1"));
            bondLengths.put("Cr-N", new String("2.30 0.2 0.1"));
            bondLengths.put("Cr-O", new String("1.93 0.2 0.1"));

            bondLengths.put("F-As", new String("1.72 0.2 0.1"));
            bondLengths.put("F-Br", new String("1.77 0.2 0.1"));
            bondLengths.put("F-C", new String("1.40 0.2 0.1"));
            bondLengths.put("F-Cl", new String("1.64 0.2 0.1"));
            bondLengths.put("F-F", new String("1.42 0.2 0.1"));
            bondLengths.put("F-Ge", new String("1.74 0.2 0.1"));
            bondLengths.put("F-H", new String("0.93 0.2 0.1"));
            bondLengths.put("F-I", new String("1.91 0.2 0.1"));
            bondLengths.put("F-N", new String("1.32 0.2 0.1"));
            bondLengths.put("F-O", new String("1.42 0.2 0.1"));
            bondLengths.put("F-P", new String("1.57 0.2 0.1"));
            bondLengths.put("F-S", new String("1.56 0.2 0.1"));
            bondLengths.put("F-Se", new String("1.71 0.2 0.1"));
            bondLengths.put("F-Si", new String("1.58 0.2 0.1"));
            bondLengths.put("F-Te", new String("1.82 0.2 0.1"));

            bondLengths.put("Ge-Br", new String("2.31 0.2 0.1"));
            bondLengths.put("Ge-C", new String("1.96 0.2 0.1"));
            bondLengths.put("Ge-Cl", new String("2.16 0.2 0.1"));
            bondLengths.put("Ge-F", new String("1.74 0.2 0.1"));
            bondLengths.put("Ge-Ge", new String("2.47 0.2 0.1"));
            bondLengths.put("Ge-H", new String("1.54 0.2 0.1"));
            bondLengths.put("Ge-I", new String("2.51 0.2 0.1"));

            bondLengths.put("H-As", new String("1.52 0.2 0.1"));
            bondLengths.put("H-B", new String("1.22 0.2 0.1"));
            bondLengths.put("H-C", new String("1.10 0.2 0.1"));
            bondLengths.put("H-Br", new String("1.42 0.2 0.1"));
            bondLengths.put("H-Cl", new String("1.29 0.2 0.1"));
            bondLengths.put("H-F", new String("0.93 0.2 0.1"));
            bondLengths.put("H-Ge", new String("1.54 0.2 0.1"));
            bondLengths.put("H-H", new String("0.75 0.2 0.1"));
            bondLengths.put("H-I", new String("1.61 2 0.1"));
            bondLengths.put("H-N", new String("1.02 0.2 0.1"));
            bondLengths.put("H-O", new String("0.96 0.2 0.1"));
            bondLengths.put("H-P", new String("1.42 0.2 0.1"));
            bondLengths.put("H-S", new String("1.32 0.2 0.1"));
            bondLengths.put("H-Sb", new String("1.70 0.2 0.1"));
            bondLengths.put("H-Se", new String("1.47 0.2 0.1"));
            bondLengths.put("H-Si", new String("1.48 0.2 0.1"));
            bondLengths.put("H-Sn", new String("1.71 0.2 0.1"));
            bondLengths.put("H-Te", new String("1.66 0.2 0.1"));

            bondLengths.put("He-He", new String("1.08 0.2 0.1"));

            bondLengths.put("Hg-C", new String("2.08 0.2 0.1"));
            bondLengths.put("Hg-Hg", new String("3.01 0.2 0.1"));

            bondLengths.put("I-Br", new String("2.47 0.2 0.1"));
            bondLengths.put("I-C", new String("2.13 0.2 0.1"));
            bondLengths.put("I-Cl", new String("2.32 0.2 0.1"));
            bondLengths.put("I-F", new String("1.91 0.2 0.1"));
            bondLengths.put("I-Ge", new String("2.51 0.2 0.1"));
            bondLengths.put("I-H", new String("1.61 0.2 0.1")); 
            bondLengths.put("I-I", new String("2.67 0.2 0.1"));
            bondLengths.put("I-Pb", new String("2.82 0.2 0.1"));
            bondLengths.put("I-Sb", new String("2.70 0.2 0.1"));
            bondLengths.put("I-Si", new String("2.44 0.2 0.1"));
            bondLengths.put("I-Sn", new String("2.67 0.2 0.1"));

            bondLengths.put("In-In", new String("3.26 0.2 0.1"));

            bondLengths.put("Ir-Ir", new String("2.72 0.2 0.1"));

            bondLengths.put("K-K", new String("4.55 0.2 0.1"));

            bondLengths.put("Li-Li", new String("3.05 0.2 0.1"));

            bondLengths.put("Mg-Mg", new String("3.20 0.2 0.1"));

            bondLengths.put("Mn-Mn", new String("2.74 0.2 0.1"));

            bondLengths.put("Mo-C", new String("2.12 0.2 0.1"));
            bondLengths.put("Mo-Mo", new String("2.74 0.2 0.1"));

            bondLengths.put("N-Br", new String("2.16 0.2 0.1"));
            bondLengths.put("N-C", new String("1.46 1.22 1.17"));
            bondLengths.put("N-Cl", new String("1.90 0.2 0.1"));
            bondLengths.put("N-Cr", new String("2.30 0.2 0.1"));
            bondLengths.put("N-F", new String("1.32 0.2 0.1"));
            bondLengths.put("N-H", new String("1.02 0.2 0.1"));
            bondLengths.put("N-N", new String("1.45 1.25 1.14"));
            bondLengths.put("N-O", new String("1.43 1.19 0.1"));
            bondLengths.put("N-P", new String("1.65 0.2 0.1"));

            bondLengths.put("Na-Na", new String("3.73 0.2 0.1"));

            bondLengths.put("Nb-Nb", new String("2.86 0.2 0.1"));

            bondLengths.put("Nd-Nd", new String("3.64 0.2 0.1"));

            bondLengths.put("Ni-Ni", new String("2.51 0.2 0.1"));

            bondLengths.put("O-As", new String("1.80 0.02 0.1"));
            bondLengths.put("O-C", new String("1.42 1.22 1.15"));
            bondLengths.put("O-Cl", new String("1.70 0.2 0.1"));
            bondLengths.put("O-Cr", new String("1.93 0.2 0.1"));
            bondLengths.put("O-F", new String("1.42 0.2 0.1"));
            bondLengths.put("O-H", new String("0.96 0.2 0.1"));
            bondLengths.put("O-N", new String("1.43 1.19 0.1"));
            bondLengths.put("O-O", new String("1.48 1.22 0.1"));
            bondLengths.put("O-P", new String("1.67 1.53 0.1"));
            bondLengths.put("O-S", new String("1.47 1.46 0.1"));
            bondLengths.put("O-Si", new String("1.63 0.2 0.1"));

            bondLengths.put("Os-Os", new String("2.68 0.2 0.1"));
 
            bondLengths.put("P-Br", new String("2.22 0.2 0.1"));
            bondLengths.put("P-C", new String("1.85 0.2 0.1"));
            bondLengths.put("P-Cl", new String("2.04 0.2 0.1"));
            bondLengths.put("P-F", new String("1.57 0.2 0.1"));
            bondLengths.put("P-H", new String("1.42 0.2 0.1"));
            bondLengths.put("P-P", new String("2.25 0.2 0.1"));
            bondLengths.put("P-N", new String("1.65 0.2 0.1"));
            bondLengths.put("P-O", new String("1.67 1.53 0.1"));
            bondLengths.put("P-S", new String("1.91 1.87 0.1"));

            bondLengths.put("Pb-C", new String("2.24 0.2 0.1"));
            bondLengths.put("Pb-Cl", new String("2.45 0.2 0.1"));
            bondLengths.put("Pb-I", new String("2.82 0.2 0.1"));
            bondLengths.put("Pb-Pb", new String("2.76 0.2 0.1"));

            bondLengths.put("Rb-Rb", new String("4.95 0.2 0.1"));

            bondLengths.put("S-Br", new String("2.24 0.2 0.1"));
            bondLengths.put("S-C", new String("1.82 1.62 0.1"));
            bondLengths.put("S-H", new String("1.32 0.2 0.1"));
            bondLengths.put("S-O", new String("1.47 1.46 0.1"));
            bondLengths.put("S-P", new String("1.91 1.87 0.1"));
            bondLengths.put("S-S", new String("2.04 1.52 0.1"));
            bondLengths.put("S-F", new String("1.56 0.2 0.1"));
            bondLengths.put("S-Cl", new String("2.05 0.2 0.1"));
            bondLengths.put("S-Si", new String("2.14 0.2 0.1"));

            bondLengths.put("Sb-Br", new String("2.53 0.2 0.1"));
            bondLengths.put("Sb-C", new String("2.22 0.2 0.1"));
            bondLengths.put("Sb-Cl", new String("2.33 0.2 0.1"));
            bondLengths.put("Sb-H", new String("1.70 0.2 0.1"));
            bondLengths.put("Sb-I", new String("2.70 0.2 0.1"));
            bondLengths.put("Sb-Sb", new String("2.91 0.2 0.1")); 

            bondLengths.put("Se-C", new String("1.95 0.2 0.1"));
            bondLengths.put("Se-F", new String("1.71 0.2 0.1"));
            bondLengths.put("Se-H", new String("1.47 0.2 0.1"));
            bondLengths.put("Se-Se", new String("2.33 2.15 0.1"));

            bondLengths.put("Si-H", new String("1.48 0.2 0.1"));
            bondLengths.put("Si-C", new String("1.87 0.2 0.1"));
            bondLengths.put("Si-Cl", new String("2.05 0.2 0.1"));
            bondLengths.put("Si-F", new String("1.58 0.2 0.1"));
            bondLengths.put("Si-Br", new String("2.19 0.2 0.1"));
            bondLengths.put("Si-I", new String("2.44 0.2 0.1"));
            bondLengths.put("Si-O", new String("1.63 0.2 0.1"));
            bondLengths.put("Si-S", new String("2.14 0.2 0.1"));
            bondLengths.put("Si-Si", new String("2.33 0.2 0.1"));
 
            bondLengths.put("Sn-Br", new String("2.53 0.2 0.1"));
            bondLengths.put("Sn-C", new String("2.14 0.2 0.1"));
            bondLengths.put("Sn-Cl", new String("2.28 0.2 0.1"));
            bondLengths.put("Sn-H", new String("1.71 0.2 0.1"));
            bondLengths.put("Sn-I", new String("2.67 0.2 0.1"));
 
            bondLengths.put("Te-C", new String("2.19 0.2 0.1"));
            bondLengths.put("Te-F", new String("1.82 0.2 0.1"));
            bondLengths.put("Te-H", new String("1.66 0.2 0.1"));

            bondLengths.put("W-C", new String("2.07 0.2 0.1"));

            bondLengths.put("Zn-C", new String("1.93 0.2 0.1"));


            // Construct image of molecule projected onto x,z-plane

            // Retrieve atomic symbols
            atomSymb = getAtomSymbols(atomArray, natoms);
           
            // Retrieve atom colors
            atomColors = getAtomColors(atomSymb, natoms);

            // Retrieve atom coordinates
            atomCoord = getAtomCoordinates(coordArray, natomCoord);

            // Set initial values of transformed coordinates equal to the values for atom coordinates  
            for (i = 0; i < natoms; i++)
            {
                for (j = 0; j < ncoord; j++)
                {
                    k = (3 * i) + j;

                    transfCoord[k] = atomCoord[k];
                }
            }    

            // Get display coordinates
            displayCoord = getDisplayCoord(transfCoord, natoms, alpha, displayWidth, displayHeight);

            // Get bond order for each pair of bonded atoms in molecule
            bondOrder = getBondOrder(atomCoord, atomSymb, natoms, bondLengths);
        }

        public int getAtomCount(String atomArray)
        {
            String atomList = atomArray.trim();

            StringTokenizer stok = new StringTokenizer(atomList);

            return stok.countTokens();
        }
 
        public String[] getAtomSymbols(String atomArray, int natoms)
        {
            String[] atomicSymbol = new String[natoms];
            String atomList = atomArray.trim();

            StringTokenizer stok = new StringTokenizer(atomList);
 
            int index = 0;

            while (stok.hasMoreTokens())
            {
                atomicSymbol[index] = stok.nextToken();

                index++;
            }

            return atomicSymbol;
        }     

        public double[] getAtomCoordinates(String coordArray, int natomCoord)
        {
            double[] atomCoordinates = new double[natomCoord];
            String coordList = coordArray.trim();

            StringTokenizer stok = new StringTokenizer(coordList);

            int index = 0;

            while (stok.hasMoreTokens())
            {
                atomCoordinates[index] = Double.parseDouble(stok.nextToken());

                index++;
            }
            return atomCoordinates;
        }

        public Color[] getAtomColors(String[] atomicSymbol, int natoms)
        {
              Color[] atomColors = new Color[natoms];

            for (j = 0; j < natoms; j++)
            {
                if (atomicSymbol[j].equals("Ag"))
                {
                    atomColors[j] = new Color(192, 192, 192);
                }
                else if (atomicSymbol[j].equals("Al"))
                {
                    atomColors[j] = new Color(221, 221, 221);
                }
                else if (atomicSymbol[j].equals("Ar"))
                {
                    atomColors[j] = new Color(0, 153, 153);
                }
                else if (atomicSymbol[j].equals("As"))
                {
                    atomColors[j] = new Color(0, 153, 255);
                }
                else if (atomicSymbol[j].equals("Au"))
                {
                    atomColors[j] = new Color(255, 230, 0);
                }
                else if (atomicSymbol[j].equals("B"))
                {
                    atomColors[j] = new Color(0, 153, 204);
                }
                else if (atomicSymbol[j].equals("Be"))
                {
                    atomColors[j] = new Color(153, 102, 255);
                }
                else if (atomicSymbol[j].equals("Br"))
                {
                    atomColors[j] = new Color(180, 40, 0);
                }
                else if (atomicSymbol[j].equals("C"))
                {
                    atomColors[j] = new Color(0, 0, 0);
                }
                else if (atomicSymbol[j].equals("Ca"))
                {
                    atomColors[j] = new Color(255, 255, 204);
                }
                else if (atomicSymbol[j].equals("Cd"))
                {
                    atomColors[j] = new Color(230, 200, 255);
                }
                else if (atomicSymbol[j].equals("Cl"))
                {
                    atomColors[j] = new Color(0, 230, 0);
                }
                else if (atomicSymbol[j].equals("Co"))
                {
                    atomColors[j] = new Color(255, 110, 165);
                }
                else if (atomicSymbol[j].equals("Cr"))
                {
                    atomColors[j] = new Color(51, 204, 204);
                }
                else if (atomicSymbol[j].equals("Cu"))
                {
                    atomColors[j] = new Color(255, 153, 0);
                }
                else if (atomicSymbol[j].equals("F"))
                {
                    atomColors[j] = new Color(255, 190, 255);
                }
                else if (atomicSymbol[j].equals("Fe"))
                {
                    atomColors[j] = new Color(204, 0, 0);
                }   
                else if (atomicSymbol[j].equals("Ga"))
                {
                    atomColors[j] = new Color(0, 205, 85);
                }
                else if (atomicSymbol[j].equals("Ge"))
                {
                    atomColors[j] = new Color(125, 204, 204);
                }
                else if (atomicSymbol[j].equals("H"))
                {
                    atomColors[j] = new Color(204, 255, 204);
                }
                else if (atomicSymbol[j].equals("He"))
                {
                    atomColors[j] = new Color(204, 236, 255);
                }
                else if (atomicSymbol[j].equals("Hg"))
                {
                    atomColors[j] = new Color(234, 243, 233);
                }
                else if (atomicSymbol[j].equals("I"))
                {
                    atomColors[j] = new Color(220, 30, 50);
                }
                else if (atomicSymbol[j].equals("In"))
                {
                    atomColors[j] = new Color(220, 180, 210);
                }
                else if (atomicSymbol[j].equals("K"))
                {
                    atomColors[j] = new Color(0, 50, 195);
                }
                else if (atomicSymbol[j].equals("Kr"))
                {
                    atomColors[j] = new Color(155, 255, 204);
                }
                else if (atomicSymbol[j].equals("Li"))
                {
                    atomColors[j] = new Color(204, 0, 255);
                }
                else if (atomicSymbol[j].equals("Mg"))
                {
                    atomColors[j] = new Color(155, 155, 155);
                }
                else if (atomicSymbol[j].equals("Mn"))
                {
                    atomColors[j] = new Color(0, 204, 153);
                }
                else if (atomicSymbol[j].equals("Mo"))
                {
                    atomColors[j] = new Color(204, 153, 255);
                }
                else if (atomicSymbol[j].equals("N"))
                {
                    atomColors[j] = new Color(0, 204, 255);
                }
                else if (atomicSymbol[j].equals("Na"))
                {
                    atomColors[j] = new Color(255, 255, 0);
                }
                else if (atomicSymbol[j].equals("Nb"))
                {
                    atomColors[j] = new Color(204, 255, 51);
                }
                else if (atomicSymbol[j].equals("Ne"))
                {
                    atomColors[j] = new Color(204, 255, 255);
                }
                else if (atomicSymbol[j].equals("Ni"))
                {
                    atomColors[j] = new Color(0, 95, 250);
                }
                else if (atomicSymbol[j].equals("O"))
                {
                    atomColors[j] = new Color(200, 0, 25);
                }
                else if (atomicSymbol[j].equals("P"))
                {
                    atomColors[j] = new Color(255, 0, 0);
                }
                else if (atomicSymbol[j].equals("Pb"))
                {
                    atomColors[j] = new Color(115, 115, 115);
                }
                else if (atomicSymbol[j].equals("Pd"))
                {
                    atomColors[j] = new Color(240, 240, 230);
                }
                else if (atomicSymbol[j].equals("Pt"))
                {
                    atomColors[j] = new Color(234, 234, 234);
                }
                else if (atomicSymbol[j].equals("Rb"))
                {
                    atomColors[j] = new Color(153, 0, 204);
                }
                else if (atomicSymbol[j].equals("Rh"))
                {
                    atomColors[j] = new Color(51, 102, 210);
                }
                else if (atomicSymbol[j].equals("Ru"))
                {
                    atomColors[j] = new Color(0, 210, 210);
                }
                else if (atomicSymbol[j].equals("S"))
                {
                    atomColors[j] = new Color(255, 230, 0);
                }
                else if (atomicSymbol[j].equals("Sb"))
                {
                    atomColors[j] = new Color(51, 165, 51);
                }
                else if (atomicSymbol[j].equals("Sc"))
                {
                    atomColors[j] = new Color(0, 255, 0);
                }
                else if (atomicSymbol[j].equals("Se"))
                {
                    atomColors[j] = new Color(204, 51, 10);
                }
                else if (atomicSymbol[j].equals("Si"))
                {
                    atomColors[j] = new Color(255, 255, 153);
                }
                else if (atomicSymbol[j].equals("Sn"))
                {
                    atomColors[j] = new Color(220, 230, 165);
                }
                else if (atomicSymbol[j].equals("Sr"))
                {
                    atomColors[j] = new Color(246, 250, 165);
                }
                else if (atomicSymbol[j].equals("Te"))
                {
                    atomColors[j] = new Color(204, 255, 153);
                }
                else if (atomicSymbol[j].equals("Tc"))
                {
                    atomColors[j] = new Color(51, 102, 153);
                }
                else if (atomicSymbol[j].equals("Ti"))
                {
                    atomColors[j] = new Color(0, 255, 255);
                }
                else if (atomicSymbol[j].equals("V"))
                {
                    atomColors[j] = new Color(255, 255, 102);
                }
                else if (atomicSymbol[j].equals("Xe"))
                {
                    atomColors[j] = new Color(0, 130, 75);
                }
                else if (atomicSymbol[j].equals("Y"))
                {
                    atomColors[j] = new Color(245, 130, 255);
                }
                else if (atomicSymbol[j].equals("Zn"))
                {
                    atomColors[j] = new Color(0, 204, 204);
                }
                else if (atomicSymbol[j].equals("Zr"))
                {
                    atomColors[j] = new Color(140, 204, 204);
                }
                else
                {
                    atomColors[j] = new Color(255, 255, 230);
                }
            }    
            return atomColors;
        }
        
        public int[] getBondOrder(double[] atomCoord, String[] atomSymb, int natoms, Hashtable<String,String> bondLengths)
        {
            int nelem, ntokens, index, k1, k2;
            double dx, dy, dz, dist;
            double[] length;
            int[] bondOrder;
            String atomPair, bondDistances;

            nelem = natoms * natoms;
            ntokens = 3;

            length = new double[ntokens];
            bondOrder = new int[nelem];

            for (i = 0; i < (natoms-1); i++)
            {
                k1 = 3 * i;

                for (j = (i+1); j < natoms; j++)
                {
                    index = (i * natoms) + j;
                    k2 = 3 * j;

                    // Compute distance between atom 'i' and atom 'j'
                    dx = atomCoord[k1] - atomCoord[k2];
                    dy = atomCoord[k1+1] - atomCoord[k2+1];
                    dz = atomCoord[k1+2] - atomCoord[k2+2];

                    dist = Math.sqrt( (dx*dx) + (dy*dy) + (dz*dz) );

                    atomPair = new String(atomSymb[i] + "-" + atomSymb[j]);

                    // Retrieve from hashtable the maximum bond length for atoms pair to be bonded
                    if (bondLengths.get(atomPair)!= null)
                    {
                        bondDistances = new String(bondLengths.get(atomPair));
                    }
                    else
                    {
                        bondDistances = "3.0 0.2 0.1";   // default bond distance for undefined atom pairs
                    }

                    if (bondDistances != null)
                    {
                        StringTokenizer stok = new StringTokenizer(bondDistances);
 
                        for (int t = 0; t < ntokens; t++)
                        {
                            length[t] = Double.parseDouble(stok.nextToken());
                        }

                        if (dist <= length[0])
                        {
                            if (dist > length[1])
                            {
                                bondOrder[index] = 1;
                            }
                            else if ( (dist <= length[1]) && (dist > length[2]) )
                            {
                                bondOrder[index] = 2;
                            }
                            else if ( (dist <= length[2]) && (dist > 0) )
                            {
                                bondOrder[index] = 3;
                            }
                        }    
                    }    
                }
            }
        
            return bondOrder;
        }    

        public double[] getDisplayCoord(double[] coordinates, int natoms, double alpha, int displayWidth, int displayHeight)
        {
            int ncoord, nelem, finalNdex;
            double xmax, ymax, xmin, ymin;
            double[] displayCoord;
            double[] projectedCoord;
            double[] outerCoord;
 
            ncoord = 2;
            nelem = ncoord * natoms;
            finalNdex = nelem - 1;
            
            projectedCoord = new double[nelem];  
            outerCoord = new double[4];
            displayCoord = new double[nelem];
 
            for (i = 0; i < natoms; i++)
            {
                j = 2 * i;
                k = 3 * i;

                projectedCoord[j] = coordinates[k] - (coordinates[k + 1] * Math.cos(alpha));
                projectedCoord[j + 1] = coordinates[k + 2] - (coordinates[k + 1] * Math.sin(alpha));
            }         

            // Retrieve coordinates with extreme values in the x- and y-directions
            xmax = projectedCoord[finalNdex-1];
            xmin = xmax;
            ymax = projectedCoord[finalNdex];
            ymin = ymax;

            for (j = 0; j < (natoms-1); j++)
            {
                k = 2 * j;

                if (projectedCoord[k] > xmax)
                {
                    xmax = projectedCoord[k];
                }

                if (projectedCoord[k] <  xmin)
                {
                    xmin = projectedCoord[k];
                }

                if (projectedCoord[k+1] > ymax)
                {
                    ymax = projectedCoord[k+1];
                }

                if (projectedCoord[k+1] < ymin)
                {
                    ymin = projectedCoord[k+1];
                }
            }

            outerCoord[0] = xmin;
            outerCoord[1] = xmax;
            outerCoord[2] = ymin;
            outerCoord[3] = ymax;

            // Set scale parameter for expanding size of molecule to cover 'prcnt' percent of the display screen
            xscale = (prcnt * displayWidth)/(outerCoord[1] - outerCoord[0]);      // x-coordinate scale factor
            yscale = (prcnt * displayHeight)/(outerCoord[3] - outerCoord[2]);     // y-coordinate scale factor

            // Select smallest scale factor for scaling in both the x- and y-directions
            if (xscale < yscale)
            {
                scaleFactor = xscale;
            }
            else
            {
                scaleFactor = yscale;
            }

            // Set translation parameter for centering molecule on display screen
            delx = (displayWidth - ((outerCoord[0] + outerCoord[1])*scaleFactor))/2;
            dely = (displayHeight - ((outerCoord[2] + outerCoord[3])*scaleFactor))/2;


            for (i = 0; i < natoms; i++)
            {
                j = 2 * i;

                displayCoord[j] = delx + scaleFactor * projectedCoord[j];
                displayCoord[j+1] = dely + scaleFactor * projectedCoord[j+1];
            }
            
            return displayCoord;
        }

        public double[] getImageBounds(double[] displayCoord, int natoms)
        {
            int ncoord, nelem, finalNdex;
            double xmax, ymax, xmin, ymin;

            double[] outerCoord;

            ncoord = 2;
            nelem = ncoord * natoms;
            finalNdex = nelem - 1;

            // Retrieve coordinates with extreme values in the x- and y-directions
            xmax = displayCoord[finalNdex-1];
            xmin = xmax;
            ymax = displayCoord[finalNdex];
            ymin = ymax;

            for (j = 0; j < (natoms-1); j++)
            {
                k = 2 * j;

                if (displayCoord[k] > xmax)
                {
                    xmax = displayCoord[k];
                }

                if (displayCoord[k] <  xmin)
                {
                    xmin = displayCoord[k];
                }

                if (displayCoord[k+1] > ymax)
                {
                    ymax = displayCoord[k+1];
                }

                if (displayCoord[k+1] < ymin)
                {
                    ymin = displayCoord[k+1];
                }
            }

            imageBounds[0] = xmin;
            imageBounds[1] = ymin;
            imageBounds[2] = xmax;
            imageBounds[3] = ymax;

            return imageBounds;
        }    

        public double[] getTransformedCoordinates(double[] coordinates, int natoms, int ncoord, double rotAngle, int rotAxis)
        {
            int nelem, matrixDim, i, j, k, k1, k2;
            double phi;
            double[] M;
            double[] transfCoord;

            nelem = ncoord * natoms;
            matrixDim = ncoord * ncoord;
            phi = rotAngle;

            M = new double[matrixDim];
            transfCoord = new double[nelem];

            switch(rotAxis)
            {
                case 1:
                    // Define transformation matrix for rotation about x-axis
                    double[] Mx = { 1, 0, 0, 0, Math.cos(phi), -Math.sin(phi), 0, Math.sin(phi), Math.cos(phi) };

                    for (int index = 0; index < matrixDim; index++)
                    {
                        M[index] = Mx[index];
                    }
                    break;
                case 2:
                    // Define transformation matrix for rotation about y-axis
                    double[] My = { Math.cos(phi), 0, Math.sin(phi), 0, 1, 0, -Math.sin(phi), 0, Math.cos(phi) };
                    for (int index = 0; index < matrixDim; index++)
                    {
                        M[index] = My[index];
                    }
                    break;

                case 3:
                    // Define transformation matrix for rotation about z-axis
                    double[] Mz = { Math.cos(phi), Math.sin(phi), 0, -Math.sin(phi), Math.cos(phi), 0, 0, 0, 1 };

                    for (int index = 0; index < matrixDim; index++)
                    {
                        M[index] = Mz[index];
                    }
                    break;
            }

            // Apply transformation matrix to coordinates
            for (j = 0; j < natoms; j++)
            {
                k = 3 * j;

                transfCoord[k] = M[0] * coordinates[k] + M[1] * coordinates[k + 1] + M[2] * coordinates[k + 2];
                transfCoord[k + 1] = M[3] * coordinates[k] + M[4] * coordinates[k + 1] + M[5] * coordinates[k + 2];
                transfCoord[k + 2] = M[6] * coordinates[k] + M[7] * coordinates[k + 1] + M[8] * coordinates[k + 2];
            }

            return transfCoord;
        }

        // Define paint method
        public void paint(Graphics g)
        {
            double lamda;
            String strAtomSymb;
            StringBuffer sbSymbol = new StringBuffer();

            // Retrieve graphics context for offscreen buffered image
            Graphics2D offscreen = offscreenImage.createGraphics();             

            // Set background color for offscreen image
            offscreen.setColor(background);
//            offscreen.fillRect((int)(0.9 * clippingBounds[0]), (int)(0.9 * clippingBounds[1]), (int)(1.1 * clippingBounds[2]), (int)(1.1 * clippingBounds[3]));
            offscreen.fillRect(0, 0, displayWidth, displayHeight);

            // Draw Cartesian coordinate axes

            // Draw bounding rectangles for Cartesian coordinate axes
            offscreen.setColor(background);   // make boundary lines invisible

            // Set parameters and draw rectangles bounding the three axes
            double x_rec, y_rec, w_rec, h_rec;

            // x-axis rectangle
            x_rec = x1 + 4;
            y_rec = y1 - 4;
            w_rec = x2 - x_rec;
            h_rec = 7;

            xrec = new Rectangle2D.Double(x_rec, y_rec, w_rec, h_rec);

            // y-axis rectangle
            x_rec = x4;
            y_rec = y1;
            w_rec = (x1 - x4) + 1;
            h_rec = (y4 - y1);

            yrec = new Rectangle2D.Double(x_rec, y_rec, w_rec, h_rec);

            // z-axis rectangle
            x_rec = x1 - 4;
            y_rec = y3 - 2;
            w_rec = 7;
            h_rec = (y1 - y3) - 6;

            zrec = new Rectangle2D.Double(x_rec, y_rec, w_rec, h_rec);

            // Set font parameters for text
            fontName = "Arial";
            fontStyle = Font.BOLD;
            fontSize = 11;
            font1 = new Font(fontName, fontStyle, fontSize);

            // Set font parameters for bonds
            fontSize = 14;
            font2 = new Font(fontName, fontStyle, fontSize);

            // Draw bonds between bonded atoms in molecule

            // Set color to be used for bonds 
            offscreen.setColor(bondColor);

            offscreen.setStroke(new BasicStroke(3));

            for (i = 0; i < (natoms-1); i++)
            {
                k1 = 2 * i;

                for (j = (i + 1); j < natoms; j++)
                {
                    k2 = 2 * j;  
                    index = (i * natoms) + j; 
               
                    if (bondOrder[index] == 1)
                    {
                        lamda = 0.5;

                        x11 = displayCoord[k1] + (lamda * atomDiameter);
                        y11 = displayCoord[k1 + 1] + (lamda * atomDiameter);
                        x21 = displayCoord[k2] + (lamda * atomDiameter);
                        y21 = displayCoord[k2 + 1] + (lamda * atomDiameter);

                        offscreen.draw(new Line2D.Double(x11, y11, x21, y21));
                    }     
                    else if (bondOrder[index] == 2)
                    {
                        lamda = 0.33;
 
                        x11 = displayCoord[k1] + (lamda * atomDiameter);
                        y11 = displayCoord[k1 + 1] + (lamda * atomDiameter);
                        x21 = displayCoord[k2] + (lamda * atomDiameter);
                        y21 = displayCoord[k2 + 1] + (lamda * atomDiameter);
 
                        x12 = displayCoord[k1] + ((1 - lamda) * atomDiameter);
                        y12 = displayCoord[k1 + 1] + ((1 - lamda) * atomDiameter);
                        x22 = displayCoord[k2] + ((1 - lamda) * atomDiameter);
                        y22 = displayCoord[k2 + 1] + ((1 - lamda) * atomDiameter);
 
                        offscreen.draw(new Line2D.Double(x11, y11, x21, y21));
                        offscreen.draw(new Line2D.Double(x12, y12, x22, y22));
                    }      
                    else if (bondOrder[index] == 3)
                    {
                        lamda = 0.25;
                        
                        x11 = displayCoord[k1] + (lamda * atomDiameter);
                        y11 = displayCoord[k1 + 1] + (lamda * atomDiameter);
                        x21 = displayCoord[k2] + (lamda * atomDiameter);
                        y21 = displayCoord[k2 + 1] + (lamda * atomDiameter);
 
                        x12 = displayCoord[k1] + (0.5 * atomDiameter);
                        y12 = displayCoord[k1 + 1] + (0.5 * atomDiameter);
                        x22 = displayCoord[k2] + (0.5 * atomDiameter);
                        y22 = displayCoord[k2 + 1] + (0.5 * atomDiameter);
 
                        x13 = displayCoord[k1] + ((1 - lamda) * atomDiameter);
                        y13 = displayCoord[k1 + 1] + ((1 - lamda) * atomDiameter);
                        x23 = displayCoord[k2] + ((1 - lamda) * atomDiameter);
                        y23 = displayCoord[k2 + 1] + ((1 - lamda) * atomDiameter);
 
                        offscreen.draw(new Line2D.Double(x11, y11, x21, y21));
                        offscreen.draw(new Line2D.Double(x12, y12, x22, y22));
                        offscreen.draw(new Line2D.Double(x13, y13, x23, y23));  
                    }    
                }     
            }    

            // Set background color
            offscreen.setColor(background);

            // Draw bounding rectangles for Cartesian coordinate axes
            offscreen.draw(xrec);
            offscreen.draw(yrec);
            offscreen.draw(zrec);

            offscreen.setStroke(new BasicStroke(2));
            offscreen.setFont(font1); 
            offscreen.setColor(xaxisColor);
            offscreen.draw(new Line2D.Double(x1, y1, x2, y2));  // draw x-axis
            offscreen.drawString("x", (int)(x2 + 5), (int)(y2 + 4));
            offscreen.setColor(yaxisColor);
            offscreen.draw(new Line2D.Double(x1, y1, x4, y4));  // draw y-axis 
            offscreen.drawString("y", (int)(x4 - 10), (int)(y4 + 6));
            offscreen.setColor(zaxisColor);
            offscreen.draw(new Line2D.Double(x1, y1, x3, y3));  // draw z-axis
            offscreen.drawString("z", (int)(x1 - 10),(int)(y3 - 1));


            // Order display coordinates so that background atoms are not drawn in front of foreground atoms
            // Retrieve y-coordinates of transformed coordinates
            transfCoord_y = new double[natoms];

            for (index = 0; index < natoms; index++)
            {
                k3 = (3 * index) + 1;

                transfCoord_y[index] = transfCoord[k3];
                displayOrder[index] = index;
            }

            // Arrange coordinates in the order in which atoms are to be drawn
            for (i = 0; i < (natoms - 1); i++)
            {
                min = transfCoord_y[i];
                minIndex = displayOrder[i];

                for (j = (i + 1); j < natoms; j++)
                {
                    if (transfCoord_y[j] < min)
                    {
                        min = transfCoord_y[j];
                        minIndex = displayOrder[j];

                        transfCoord_y[j] = transfCoord_y[i];
                        transfCoord_y[i] = min;

                        displayOrder[j] = displayOrder[i];
                        displayOrder[i] = minIndex;
                    }
                }
            }

            // Draw atoms in molecular structure in proper order
            for (j = 0; j < natoms; j++)
            {
                k = 2 * displayOrder[j];

                if ((atomColors[displayOrder[j]].getRed() == 255) && (atomColors[displayOrder[j]].getGreen() == 255) && (atomColors[displayOrder[j]].getBlue() == 230))
                {
                    offscreen.setColor(Color.black);
                    offscreen.draw(new Ellipse2D.Double(displayCoord[k], displayCoord[k+1], atomDiameter, atomDiameter)); 
                }

                offscreen.setColor(atomColors[displayOrder[j]]);
                offscreen.fill(new Ellipse2D.Double(displayCoord[k], displayCoord[k+1], atomDiameter, atomDiameter));  
 
                offscreen.setFont(font2);

                // Set text color as black for light-colored atoms and white for dark-colored atoms
                if ((atomSymb[displayOrder[j]].equals("Ag")) || (atomSymb[displayOrder[j]].equals("Ar")) || (atomSymb[displayOrder[j]].equals("As")) || (atomSymb[displayOrder[j]].equals("B")) || (atomSymb[displayOrder[j]].equals("Be")) || (atomSymb[displayOrder[j]].equals("Br")) || (atomSymb[displayOrder[j]].equals("C")) || (atomSymb[displayOrder[j]].equals("Cl")) || (atomSymb[displayOrder[j]].equals("Co")) || (atomSymb[displayOrder[j]].equals("Cr")) || (atomSymb[displayOrder[j]].equals("Cu")) || (atomSymb[displayOrder[j]].equals("Fe")) || (atomSymb[displayOrder[j]].equals("Ga")) || (atomSymb[displayOrder[j]].equals("Ge")) || (atomSymb[displayOrder[j]].equals("I")) || (atomSymb[displayOrder[j]].equals("K")) || (atomSymb[displayOrder[j]].equals("Li")) || (atomSymb[displayOrder[j]].equals("Mg")) || (atomSymb[displayOrder[j]].equals("Mn")) || (atomSymb[displayOrder[j]].equals("N")) || (atomSymb[displayOrder[j]].equals("Ni")) || (atomSymb[displayOrder[j]].equals("O")) || (atomSymb[displayOrder[j]].equals("P")) || (atomSymb[displayOrder[j]].equals("Pb")) || (atomSymb[displayOrder[j]].equals("As")) || (atomSymb[displayOrder[j]].equals("Rb")) || (atomSymb[displayOrder[j]].equals("Ru")) || (atomSymb[displayOrder[j]].equals("Sb")) || (atomSymb[displayOrder[j]].equals("Sc")) || (atomSymb[displayOrder[j]].equals("Se")) || (atomSymb[displayOrder[j]].equals("Tc")) || (atomSymb[displayOrder[j]].equals("Xe")) || (atomSymb[displayOrder[j]].equals("Zn")) || (atomSymb[displayOrder[j]].equals("Zr")) )
                {
                    offscreen.setColor(background);
                }
                else
                {
                    offscreen.setColor(Color.black);
                }

                // Set atom edge displacement factor used to center text over atom
                sbSymbol.delete(0, sbSymbol.length());
                sbSymbol.append(atomSymb[displayOrder[j]]);

                if (sbSymbol.length() == 2)
                {
                    if (displayOrder[j] > 9)
                    {
                        lamda = 0.10;
                    }
                    else
                    {
                        lamda = 0.15;
                    }
                }
                else
                {
                    if (displayOrder[j] > 9)
                    {
                        lamda = 0.15;
                    }
                    else
                    {
                        lamda = 0.25;
                    }
                }

                strAtomSymb = new String(atomSymb[displayOrder[j]] + Integer.toString(displayOrder[j]));

                offscreen.drawString(strAtomSymb, (int)(displayCoord[k] + (lamda*atomDiameter)), (int)(displayCoord[k+1] + 0.65 * atomDiameter));

///////
            }         

            // Draw on-screen image
            Graphics2D onscreen = (Graphics2D)g;

            onscreen.drawImage(offscreenImage, 0, 0, null);
        }   
    }   

    class MyMouseListener extends MouseAdapter
    {
        int rotAxis;
        double rotAngle;
        double mx_init;

        public void mouseClicked(MouseEvent e)
        {
            // Determine number of clicks
            if (((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) && (e.getClickCount() == 2))
            {
                if (canvas.isRotatable == true)
                {
                    canvas.isRotatable = false;
                } 

                if (canvas.xrec.contains(e.getX(), e.getY()))
                {
                    canvas.rotAxis = 1;
                    canvas.rotAngle = Math.PI/2;
                    canvas.x_rotAngle = 0;
                    canvas.y_rotAngle = 0;
                    canvas.z_rotAngle = 0;

                    // Get coordinate transformation
                    canvas.transfCoord = canvas.getTransformedCoordinates(canvas.atomCoord, canvas.natoms, canvas.ncoord, canvas.rotAngle, canvas.rotAxis);
                    // Get display coordinates
                    canvas.displayCoord = canvas.getDisplayCoord(canvas.transfCoord, canvas.natoms, canvas.alpha, canvas.displayWidth, canvas.displayHeight);
                    // Recolor coordinate axes
                    canvas.xaxisColor = new Color(200, 200, 200);
                    canvas.yaxisColor = new Color(0, 50, 195);
                    canvas.zaxisColor = new Color(0, 143, 0);
                }
                else if (canvas.yrec.contains(e.getX(), e.getY()))
                {
                    canvas.rotAxis = 2;
                    canvas.rotAngle = 0;
                    canvas.x_rotAngle = 0;
                    canvas.y_rotAngle = 0;
                    canvas.z_rotAngle = 0;

                    // Get coordinate transformation
                    canvas.transfCoord = canvas.getTransformedCoordinates(canvas.atomCoord, canvas.natoms, canvas.ncoord, canvas.rotAngle, canvas.rotAxis);
                    // Get display coordinates
                    canvas.displayCoord = canvas.getDisplayCoord(canvas.transfCoord, canvas.natoms, canvas.alpha, canvas.displayWidth, canvas.displayHeight);
                    // Recolor coordinate axes
                    canvas.xaxisColor = new Color(200, 0, 25);
                    canvas.yaxisColor = new Color(200, 200, 200);
                    canvas.zaxisColor = new Color(0, 143, 0);
                }
                else if (canvas.zrec.contains(e.getX(), e.getY()))
                {
                    canvas.rotAxis = 3;
                    canvas.rotAngle = Math.PI/2;
                    canvas.x_rotAngle = 0;
                    canvas.y_rotAngle = 0;
                    canvas.z_rotAngle = 0;

                    // Get coordinate transformation
                    canvas.transfCoord = canvas.getTransformedCoordinates(canvas.atomCoord, canvas.natoms, canvas.ncoord, canvas.rotAngle, canvas.rotAxis);
                    // Get display coordinates
                    canvas.displayCoord = canvas.getDisplayCoord(canvas.transfCoord, canvas.natoms, canvas.alpha, canvas.displayWidth, canvas.displayHeight);
                    // Recolor coordinate axes
                    canvas.xaxisColor = new Color(200, 0, 25);
                    canvas.yaxisColor = new Color(0, 50, 195);
                    canvas.zaxisColor = new Color(200, 200, 200);
                }

                // Redraw molecule projected onto the selected 2-dimensional plane
                canvas.clippingBounds[0] = 0; canvas.clippingBounds[1] = 0; canvas.clippingBounds[2] = canvas.displayWidth; canvas.clippingBounds[3] = canvas.displayHeight;
                canvas.repaint((int)canvas.clippingBounds[0], (int)canvas.clippingBounds[1], (int)canvas.clippingBounds[2], (int)canvas.clippingBounds[3]);
            }    
            else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)
            {
                // Set molecular image to be rotatable
                canvas.isRotatable = true;

                // Retrieve initial x-coordinate of mouse
                canvas.mx_init = e.getX();

                if (canvas.xrec.contains(e.getX(), e.getY()))
                {
                    canvas.rotAxis = 1;

                    // Recolor coordinate axes
                    canvas.xaxisColor = new Color(200, 0, 25);
                    canvas.yaxisColor = new Color(200, 200, 200);
                    canvas.zaxisColor = new Color(200, 200, 200);
                }
                else if (canvas.yrec.contains(e.getX(), e.getY()))
                {
                    canvas.rotAxis = 2;

                    // Recolor coordinate axes
                    canvas.xaxisColor = new Color(200, 200, 200);
                    canvas.yaxisColor = new Color(0, 50, 195);
                    canvas.zaxisColor = new Color(200, 200, 200);
                }
                else if (canvas.zrec.contains(e.getX(), e.getY()))
                {
                    canvas.rotAxis = 3;

                    // Recolor coordinate axes
                    canvas.xaxisColor = new Color(200, 200, 200);
                    canvas.yaxisColor = new Color(200, 200, 200);
                    canvas.zaxisColor = new Color(0, 143, 0);
                }

                // Redraw coordinate axes
                // Set clipping bounds for redrawing coordinate axes
                canvas.clippingBounds[0] = 0; canvas.clippingBounds[1] = 0; canvas.clippingBounds[2] = canvas.displayWidth; canvas.clippingBounds[3] = canvas.displayHeight;
                canvas.repaint((int)canvas.clippingBounds[0], (int)canvas.clippingBounds[1], (int)canvas.clippingBounds[2], (int)canvas.clippingBounds[3]);
            }
        }    
    }

    class MyMouseMotionListener extends MouseMotionAdapter
    {
        public void mouseDragged(MouseEvent e)
        {
            double  mx_current, del_mx, del_360, rotAngle, del_rotAngle;
            double[] currentClippingBounds = new double[4];

            del_360 = canvas.displayWidth;

            // Retrieve current x-coordinate of mouse
            mx_current = e.getX();

            // Compute corresponding angle at which to rotate molecule
            del_mx = mx_current - canvas.mx_init;

            del_rotAngle = 0.07 * ((del_mx / del_360) * (Math.PI/180));

            // Retrieve current clipping bounds
            for (int j = 0; j < 4; j++)
            {
                currentClippingBounds[j] = canvas.clippingBounds[j];
            }

            // Retrieve initial rotation angle of molecule and update by 'del_rotAngle'
            if (canvas.isRotatable == true)
            {
                if (canvas.rotAxis == 1)
                {
                    canvas.x_rotAngle += del_rotAngle;

                    // Get coordinate transformation
                    canvas.transfCoord = canvas.getTransformedCoordinates(canvas.transfCoord, canvas.natoms, canvas.ncoord, canvas.x_rotAngle, canvas.rotAxis);
                    // Get display coordinates
                    canvas.displayCoord = canvas.getDisplayCoord(canvas.transfCoord, canvas.natoms, canvas.alpha, canvas.displayWidth, canvas.displayHeight);
                    canvas.imageBounds = canvas.getImageBounds(canvas.displayCoord, canvas.natoms);
                } 
                else if (canvas.rotAxis == 2)
                {
                    canvas.y_rotAngle += del_rotAngle;

                    // Get coordinate transformation
                    canvas.transfCoord = canvas.getTransformedCoordinates(canvas.transfCoord, canvas.natoms, canvas.ncoord, canvas.y_rotAngle, canvas.rotAxis);
                    // Get display coordinates
                    canvas.displayCoord = canvas.getDisplayCoord(canvas.transfCoord, canvas.natoms, canvas.alpha, canvas.displayWidth, canvas.displayHeight);
                    canvas.imageBounds = canvas.getImageBounds(canvas.displayCoord, canvas.natoms);
                }
                else if (canvas.rotAxis == 3)
                {
                    canvas.z_rotAngle += del_rotAngle;

                    // Get coordinate transformation
                    canvas.transfCoord = canvas.getTransformedCoordinates(canvas.transfCoord, canvas.natoms, canvas.ncoord, canvas.z_rotAngle, canvas.rotAxis);
                    // Get display coordinates
                    canvas.displayCoord = canvas.getDisplayCoord(canvas.transfCoord, canvas.natoms, canvas.alpha, canvas.displayWidth, canvas.displayHeight);
                    canvas.imageBounds = canvas.getImageBounds(canvas.displayCoord, canvas.natoms);
                }
 
                // Get clipping bounds for redrawing image
                canvas.clippingBounds[0] = 0.70 * canvas.imageBounds[0];  
                canvas.clippingBounds[1] = 0.70 * canvas.imageBounds[1];
                canvas.clippingBounds[2] = 1.25 * canvas.imageBounds[2];
                canvas.clippingBounds[3] = 1.25 * canvas.imageBounds[3];

                // Redraw the molecular image rotated about the selected axis 
               canvas.repaint((int)(0.70 * currentClippingBounds[0]), (int)(0.70 * currentClippingBounds[1]), (int)(1.25 * currentClippingBounds[2]), (int)(1.25 * currentClippingBounds[3]));
            }
        }
    }    

   public void update(Graphics g)
    {
        paint(g);
    }

    public static void main(String[] args)
    {
        new BufferedDisplayMolecule();
    } 
}   

