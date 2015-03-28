/*Copyright (c) 2007, Center for Computational Sciences, University of Kentucky.  All rights reserved.

Developed by:

Center for Computational Sciences, University of Kentucky

http://www.ccs.uky.edu

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
3. Neither the names of Center for Computational Sciences, University of Kentucky 
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
 * @author Michael Sheetz 
 * @author Pavithra Koka
 * @author Shreeram Sridharan
 */

package Gamess.gamessGUI;

// Molecular Display - R. Michael Sheetz (Oct 2006)

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.lang.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;


import net.java.games.jogl.*;
import net.java.games.jogl.util.*;

// migrated to new package: javax.media.opengl;
/*import javax.media.opengl.*;
import javax.media.opengl.awt.*;
import javax.media.opengl.fixedfunc.GLLightingFunc;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.*;

import com.jogamp.opengl.util.gl2.*;
*/


public class MolDisplay extends JInternalFrame
{
     static public GLCanvas glcanvas = null;

     GL gl;				// commented by Narendra Kumar for adding "GL4bc"
     //GL4bc gl;		// added by Narendra Kumar

     GLU glu;
     GLUT glut = new GLUT();

     MolecularDisplay mdisplay; 

     int j, k, index, indx, ndx, atomIndex;
     int canvasWidth, canvasHeight;
     int cnvsWidth, cnvsHeight;
     int nmenus, npopupmenus, nsubmenus, natoms;
     int mbHeight, mbWidth, mbx, mby;
     int scaleRadius;
     int defaultConnSliderValue;
     int nsymm, naxis;

     int[] atomicNumber;

     double x, y, z;
     double x1, y1, x2, y2;
     double theta, phi, zloc;
     double x0, y0, z0;                // origin of Cartesian reference frame
     double delx, dely, delz;
     double axisSelectionDimen;
     double canvas2frameRatio, maxDisplayDimen, molScalingFactor;
     double max_x, max_y, max_z, min_x, min_y, min_z;
     double displayWidth, displayHeight, maxMolecularDimen;
     double defaultAtomRadius, unscaledAtomRadius, bondRadius, scaleFactor, bondParam; 

     double dx, dy, dz, r, bl;    
     double ar1, ar2, cr1, cr2;  

     double atomRadius;

     double[] atomCoords;
     double[] radius;
     double[] radiusScalingFactor;
     double[] scaledAtomRadius;
     double[] molDimen;
     double[] x_axisLen;
     double[] y_axisLen;
     double[] z_axisLen;

     double[][] zmatBondLength;
     double[][] bondLength;

     float bkg_red, bkg_green, bkg_blue, red, green, blue;
     float color_alphaValue, material_alphaValue;
     float[] atomColors;
     float[] bondMat1;
     float[] bondMat2;
     float[] material;
  
     boolean x_rot, y_rot, z_rot, coincidence;
     boolean displayGeom, retrievedGeom, displayMolecule, recomputeConnectivity;
     boolean bondsVisible, bondsColored;
     boolean initDisplay = true;          //initialize initDisplay
     boolean invokeSymm;

     String symm = new String();    
     String coordFormat;
     String[] atomID;

     ArrayList alGeom;

     Dimension screenSize, frameSize;

     JMenuBar menuBar;
     JMenu menu, fileMenu, editMenu, viewMenu, helpMenu;
     JMenu displayOptionsSubmenu, showBondsSubmenu, displayMOSubmenu, animationSubmenu, outputdataSubmenu;
     //JPopupMenu filePopupMenu, editPopupMenu, viewPopupMenu, helpPopupMenu;
     JMenuItem openItem, saveItem, saveAsItem, importItem, exportItem, printItem, closeItem;
     JMenuItem openPopupItem, savePopupItem, saveAsPopupItem, importPopupItem, exportPopupItem, printPopupItem, closePopupItem;
     JMenuItem undoItem, redoItem, cutItem, copyItem, pasteItem, clearItem;
     JMenuItem showBondsItem, hideBondsItem, colorBondsItem, uncolorBondsItem, adjustBondCriteriaItem;
     JMenuItem displayItem, displayOption1, displayOption2, displayOption3, displayOption4, displayOption5, displayOption6, displayOption7, displayOption8;
     JMenuItem chargeDensityItem, homoItem, lumoItem, electrostaticPotenialItem;

     //JPopupMenu displayOptionsPopupSubmenu, showBondsPopupSubmenu, displayMOPopupSubmenu, animationPopupSubmenu, outputdataPopupSubmenu;

     JDialog connectivityAdjustmentFrame , radiusScalingFrame;      
     Container container;
     JSlider connSlider, radiusSlider;
     JPanel connPanel, radiusPanel;
     JButton connDoneButton, radiusDoneButton;
     TitledBorder border;

     static JDesktopPane dp;
     String strGeom=new String();
     
     // Required for checking consistency of charge and mulitiplicity
     int nuclearCharge, nelectrons;
     String chargeStr, multiplicityStr;
     boolean CM_consistency;

     
     public MolDisplay()
     {
          super("Molecular Display");

          dp=new JDesktopPane();
          dp.setLayout(new BorderLayout());
       	  setPreferredSize(new Dimension(370,500));
       	         
          // Create molecular display
          mdisplay = new MolecularDisplay();
          
          // Step1: choose a GLProfile
          //GLProfile glp = GLProfile.getDefault();					// added by Narendra Kumar
          
          // Step2: Configure GLCapabilities
          GLCapabilities glcaps = new GLCapabilities() ;			// javax.media.opengl package needs an arugment - commented by Narendra Kumar           
          //GLCapabilities glcaps = new GLCapabilities(glp) ;		// parameter added to constructor method of GLCapabilities - added by Narendra Kumar
          glcaps.setDoubleBuffered(true);
          	
          // Step 3: create a GLCanvas 
          glcanvas = GLDrawableFactory.getFactory().createGLCanvas(glcaps);	// commented by Narendra Kumar
          //glcanvas = new GLCanvas (glcaps);								// GLCanvas can be instantiated using GLCapablities object - added by Narendra Kumare
          
          glcanvas.addGLEventListener(mdisplay);
          glcanvas.addMouseMotionListener(mdisplay);
          glcanvas.addMouseListener(new CanvasMouseListener());
          glcanvas.addKeyListener(new CanvasKeyListener());
          getContentPane().add(glcanvas, BorderLayout.CENTER); 
          // Set initial canvas dimension
          canvasHeight = 500; canvasWidth = 500;

          setSize(canvasWidth, canvasHeight);

          centerWindow(this);

          // Create menus
          menuBar = new JMenuBar();

          setJMenuBar(menuBar);

          // Create menus on main menu bar
          fileMenu = new JMenu("File");
          fileMenu.setEnabled(false);
          //fileMenu.addMouseListener(new menuMouseListener());
          editMenu = new JMenu("Edit");
          editMenu.setEnabled(false);
          //editMenu.addMouseListener(new menuMouseListener());
          viewMenu = new JMenu("View");
          //viewMenu.addMouseListener(new menuMouseListener());
          helpMenu = new JMenu("Help");
          //helpMenu.addMouseListener(new menuMouseListener());
          helpMenu.setEnabled(false);

          // Create file popup menu associated with file menu
          //filePopupMenu = new JPopupMenu();

          openItem = new JMenuItem("Open");
          //filePopupMenu.add(openItem);          
          //filePopupMenu.addSeparator();
          fileMenu.add(openItem);
          fileMenu.addSeparator();
          saveItem = new JMenuItem("Save");
          //filePopupMenu.add(saveItem);
          fileMenu.add(saveItem);
          saveAsItem = new JMenuItem("SaveAs");
          //filePopupMenu.add(saveAsItem);
          //filePopupMenu.addSeparator();
          fileMenu.add(saveAsItem);
          fileMenu.addSeparator();
          importItem = new JMenuItem("Import");
          //filePopupMenu.add(importItem);
          fileMenu.add(importItem);
          exportItem = new JMenuItem("Export");
          //filePopupMenu.add(exportItem);
          //filePopupMenu.addSeparator();
          fileMenu.add(exportItem);
          fileMenu.addSeparator();
          printItem = new JMenuItem("Print");
          //filePopupMenu.add(printItem);
          //filePopupMenu.addSeparator();
          fileMenu.add(printItem);
          fileMenu.addSeparator();
          closeItem = new JMenuItem("Close");
          //filePopupMenu.add(closeItem);
          fileMenu.add(closeItem);
          //filePopupMenu.setLightWeightPopupEnabled(false);         
          //filePopupMenu.setVisible(false);    

          menuBar.add(fileMenu);
 
          // Create edit popup menu associated with edit menu
          //editPopupMenu = new JPopupMenu();

          undoItem = new JMenuItem("Undo");
          //editPopupMenu.add(undoItem);          
          editMenu.add(undoItem);
          redoItem = new JMenuItem("Redo");
          //editPopupMenu.add(redoItem);
          //editPopupMenu.addSeparator();
          editMenu.add(redoItem);
          editMenu.addSeparator();
          cutItem = new JMenuItem("Cut");
          //editPopupMenu.add(cutItem);
          editMenu.add(cutItem);
          copyItem = new JMenuItem("Copy");
          //editPopupMenu.add(copyItem);
          editMenu.add(copyItem);
          pasteItem = new JMenuItem("Paste");
          //editPopupMenu.add(pasteItem);
          //editPopupMenu.addSeparator();
          editMenu.add(pasteItem);
          editMenu.addSeparator();
          clearItem = new JMenuItem("Clear");
          //editPopupMenu.add(clearItem);
          editMenu.add(clearItem);
          //editPopupMenu.setLightWeightPopupEnabled(false);
          //editPopupMenu.setVisible(false);    

          menuBar.add(editMenu);

          // Create view popup menu associated with edit menu
          //viewPopupMenu = new JPopupMenu();

          //displayItem = new JMenuItem("Display Molecule");
          //viewMenu.add(displayItem);

          displayOptionsSubmenu = new JMenu("Display Options");
          //displayOptionsSubmenu.addMouseListener(new displayOptionsSubmenuMouseListener());
          //displayOptionsPopupSubmenu = new JPopupMenu();
          //displayOptionsPopupSubmenu.addMouseListener(new displayOptionsSubmenuMouseListener());

               displayOption1 = new JMenuItem("Ball & Stick:");
               //displayOptionsPopupSubmenu.add(displayOption1);
               displayOptionsSubmenu.add(displayOption1);

               displayOption2 = new JMenuItem("      Scaled Radii");
               //displayOption2.addMouseListener(new displayOptionMouseListener());
               displayOption2.addActionListener(new displayOptionMouseListener());
               //displayOptionsPopupSubmenu.add(displayOption2);               
               displayOptionsSubmenu.add(displayOption2);

               displayOption3 = new JMenuItem("      Unscaled Radii");
               //displayOption3.addMouseListener(new displayOptionMouseListener());
               displayOption3.addActionListener(new displayOptionMouseListener());
               //displayOptionsPopupSubmenu.add(displayOption3);  
               displayOptionsSubmenu.add(displayOption3);

               //displayOptionsPopupSubmenu.addSeparator();          
               displayOptionsSubmenu.addSeparator();

               displayOption4 = new JMenuItem("Wireframe");
               //displayOption4.addMouseListener(new displayOptionMouseListener());
               displayOption4.addActionListener(new displayOptionMouseListener());
               //displayOptionsPopupSubmenu.add(displayOption4);
               displayOptionsSubmenu.add(displayOption4);

               displayOption5 = new JMenuItem("Space Filling");
               //displayOption5.addMouseListener(new displayOptionMouseListener());
               displayOption5.addActionListener(new displayOptionMouseListener());
               //displayOptionsPopupSubmenu.add(displayOption5);          
               displayOptionsSubmenu.add(displayOption5);

               //displayOptionsPopupSubmenu.addSeparator();          
               displayOptionsSubmenu.addSeparator();

               displayOption6 = new JMenuItem("Modify Atom Radius");
               //displayOption6.addMouseListener(new displayOptionMouseListener());
               displayOption6.addActionListener(new displayOptionMouseListener());
               //displayOptionsPopupSubmenu.add(displayOption6);  
               displayOptionsSubmenu.add(displayOption6);

               //displayOptionsPopupSubmenu.addSeparator();          
               displayOptionsSubmenu.addSeparator();

               displayOption7 = new JMenuItem("Modify Connectivity");
               //displayOption7.addMouseListener(new displayOptionMouseListener());
               displayOption7.addActionListener(new displayOptionMouseListener());
               //displayOptionsPopupSubmenu.add(displayOption7);  
               displayOptionsSubmenu.add(displayOption7);

               //displayOptionsPopupSubmenu.addSeparator();          
               displayOptionsSubmenu.addSeparator();

               displayOption8 = new JMenuItem("Label Atoms");
               //displayOption8.addMouseListener(new displayOptionMouseListener());
               displayOption8.addActionListener(new displayOptionMouseListener());
               //displayOptionsPopupSubmenu.add(displayOption8);          
               displayOptionsSubmenu.add(displayOption8);

          //displayOptionsPopupSubmenu.setLightWeightPopupEnabled(false);
          //displayOptionsPopupSubmenu.setVisible(false);    
          //viewPopupMenu.add(displayOptionsSubmenu);
          //viewPopupMenu.addSeparator();
          viewMenu.add(displayOptionsSubmenu);
          viewMenu.addSeparator();
          showBondsSubmenu = new JMenu("Show Bonds");
          //showBondsSubmenu.addMouseListener(new showBondsSubmenuMouseListener());
          showBondsSubmenu.addActionListener(new showBondsSubmenuMouseListener());
          //showBondsPopupSubmenu = new JPopupMenu();
          //showBondsPopupSubmenu.addMouseListener(new showBondsSubmenuMouseListener());

               colorBondsItem = new JMenuItem("Color Bonds");
               //colorBondsItem.addMouseListener(new colorBondsMouseListener());
               colorBondsItem.addActionListener(new showBondsSubmenuMouseListener());
               colorBondsItem.addActionListener(new colorBondsMouseListener());
               //showBondsPopupSubmenu.add(colorBondsItem);
               //showBondsPopupSubmenu.addSeparator();
               showBondsSubmenu.add(colorBondsItem);
               showBondsSubmenu.addSeparator();
               uncolorBondsItem = new JMenuItem("Uncolor Bonds");
               //uncolorBondsItem.addMouseListener(new uncolorBondsMouseListener());
               uncolorBondsItem.addActionListener(new showBondsSubmenuMouseListener());
               uncolorBondsItem.addActionListener(new uncolorBondsMouseListener());
               //showBondsPopupSubmenu.add(uncolorBondsItem);
               showBondsSubmenu.add(uncolorBondsItem);

          //showBondsPopupSubmenu.setLightWeightPopupEnabled(false);
          //showBondsPopupSubmenu.setVisible(false);    

          //viewPopupMenu.add(showBondsSubmenu);
          viewMenu.add(showBondsSubmenu);

          hideBondsItem = new JMenuItem("Hide Bonds");
          //hideBondsItem.addMouseListener(new hideBondsMouseListener());
          hideBondsItem.addActionListener(new hideBondsMouseListener());
          //viewPopupMenu.add(hideBondsItem);
          //viewPopupMenu.addSeparator();
          viewMenu.add(hideBondsItem);
          viewMenu.addSeparator();
 
          chargeDensityItem = new JMenuItem("Charge Density");
          //viewPopupMenu.add(chargeDensityItem);
          viewMenu.add(chargeDensityItem);
          electrostaticPotenialItem = new JMenuItem("Electrostatic Potenial");
          //viewPopupMenu.add(electrostaticPotenialItem);
          //viewPopupMenu.addSeparator();
          viewMenu.add(electrostaticPotenialItem);
          viewMenu.addSeparator();
 
          displayMOSubmenu = new JMenu("Display MOs");
          //displayMOSubmenu.addMouseListener(new displayMOSubmenuMouseListener());
          //displayMOPopupSubmenu = new JPopupMenu();
          //displayMOPopupSubmenu.addMouseListener(new displayMOSubmenuMouseListener());

               homoItem = new JMenuItem("HOMO");
               //displayMOPopupSubmenu.add(homoItem);
               displayMOSubmenu.add(homoItem);
               lumoItem = new JMenuItem("LUMO");
               //displayMOPopupSubmenu.add(lumoItem);
               displayMOSubmenu.add(lumoItem);

          //displayMOPopupSubmenu.setLightWeightPopupEnabled(false);
          //displayMOPopupSubmenu.setVisible(false);    
          //viewPopupMenu.add(displayMOSubmenu);
          //viewPopupMenu.addSeparator();
          viewMenu.add(displayMOSubmenu);
          viewMenu.addSeparator();
          animationSubmenu = new JMenu("Animation");
          animationSubmenu.addActionListener(new animationSubmenuMouseListener());
          //viewPopupMenu.add(animationSubmenu);
          viewMenu.add(animationSubmenu);
          outputdataSubmenu = new JMenu("Output Data");
          outputdataSubmenu.addActionListener(new outputdataSubmenuMouseListener());
          //viewPopupMenu.add(outputdataSubmenu);
          viewMenu.add(outputdataSubmenu);

          //viewPopupMenu.setLightWeightPopupEnabled(false);
          //viewPopupMenu.setVisible(false);    

          menuBar.add(viewMenu);

          // Create help popup menu associated with edit menu
          //helpPopupMenu = new JPopupMenu();

          menuBar.add(helpMenu);

          // Construct JFrame to be used for scaling atom radii
          int fwidth, fheight;
          fwidth = 250;
          fheight = 140;    

          radiusScalingFrame = new JDialog(GamessGUI.frame,"Modify Atom Radius",true);
          radiusScalingFrame.setSize(fwidth, fheight);
          radiusScalingFrame.setBackground(Color.white);
          radiusScalingFrame.setVisible(false);

          container = radiusScalingFrame.getContentPane();

          radiusSlider = new JSlider(JSlider.HORIZONTAL, 0, 300, 100);
          radiusSlider.setPaintTicks(true);
          radiusSlider.setMajorTickSpacing(50);
          radiusSlider.setMinorTickSpacing(10);
          radiusSlider.setPaintLabels(true);
          radiusSlider.addChangeListener(new SliderListener());

          radiusDoneButton = new JButton("Done");
          radiusDoneButton.addActionListener(new DoneButtonListener());       

          border = new TitledBorder(" ");

          radiusPanel= new JPanel();
          radiusPanel.add(radiusSlider, BorderLayout.CENTER);
          radiusPanel.add(radiusDoneButton, BorderLayout.SOUTH);
          radiusPanel.setBorder(border);

          container.add(radiusPanel);

          // Construct JFrame to be used for adjusting bonding criteria
          connectivityAdjustmentFrame = new JDialog(GamessGUI.frame,"Modify Connectivity",true);
          connectivityAdjustmentFrame.setSize(fwidth, fheight);
          connectivityAdjustmentFrame.setBackground(Color.white);

          container = connectivityAdjustmentFrame.getContentPane();

          defaultConnSliderValue = 40;
          connSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, defaultConnSliderValue);
          connSlider.setPaintTicks(true);
          connSlider.setMajorTickSpacing(20);
          connSlider.setMinorTickSpacing(5);
          connSlider.setPaintLabels(true);
          connSlider.addChangeListener(new SliderListener());

          connDoneButton = new JButton("Done");
          connDoneButton.addActionListener(new DoneButtonListener());

          border = new TitledBorder(" ");

          connPanel= new JPanel();
          connPanel.add(connSlider, BorderLayout.CENTER);
          connPanel.add(connDoneButton, BorderLayout.SOUTH);
          connPanel.setBorder(border);

          container.add(connPanel);

          dp.add(this);
          setVisible(true);
     }

     public boolean setStrGeom(String data,String _charge, String _multiplicity, boolean _invokeSymmetry, String _symm, int _naxis )
     {
          strGeom=data;
          chargeStr=_charge;
          multiplicityStr=_multiplicity;
          invokeSymm = _invokeSymmetry;
          symm = _symm;
          naxis = _naxis;
          
          boolean consistent= mdisplay.drawMolecule(data);
        
         // System.out.println(CM_consistency);
          return consistent; 	    
     }
     
     public void callDrawMolecule(){
    	 mdisplay.drawMolecule("");
     	 glcanvas.repaint();  
     }

     public void centerWindow(Component frame)
     {
          screenSize = Toolkit.getDefaultToolkit().getScreenSize();
          frameSize = frame.getSize();

          if (frameSize.width > screenSize.width)
          {
               frameSize.width = screenSize.width;
          }
          if (frameSize.height > screenSize.height)
          {
               frameSize.height = screenSize.height;
          }

          frame.setLocation(0, (screenSize.height - frameSize.height)/2);
     }

     public void parseDataFile(){
    	 
    	 String geomToken, var, value;
    	 
         ArrayList alGeomElements, alElement, alElem3, alElem4;

         alGeom = new ArrayList();

         StringTokenizer stok1, stok2;

         stok1 = new StringTokenizer(strGeom, "\n");

         while(stok1.hasMoreTokens())
         {
              geomToken = stok1.nextToken();

              if (geomToken.indexOf("=") == -1)
              {
                   alGeomElements = new ArrayList(); 

                   stok2 = new StringTokenizer(geomToken);                     
          
                   while(stok2.hasMoreTokens())
                   {
                        alGeomElements.add(stok2.nextToken());
                   }

                   alGeom.add(alGeomElements);
              }
              else
              {
                   stok2 = new StringTokenizer(geomToken, "=");             

                   var = stok2.nextToken();
                   value = stok2.nextToken();                         

                   for (index = 0; index < alGeom.size(); index++)
                   {
                        alElement = new ArrayList(); 

                        alElement = (ArrayList)(alGeom.get(index));

                        if (alElement.contains(var))
                        {
                             for (int ndx = 0; ndx < alElement.size(); ndx++)
                             {
                                  k = alElement.indexOf(var);

                                  if (k != -1)
                                  {
                                       alElement.set(k, value);
                                  }
                                  else
                                  {
                                       break;
                                  }
                             }

                             alGeom.set(index, alElement);
                        }
                   }
              }
         }
         
     }
     public class MolecularDisplay implements GLEventListener, MouseMotionListener
     {
          public void init(GLDrawable gldraw)
          {
              //parseDataFile(); 
        	  drawMolecule("");
          }
    	
          public boolean drawMolecule(String data)
          {
               bkg_red = 0.0f;
               bkg_green = 0.0f;
               bkg_blue = 0.0f;
               color_alphaValue = 1.0f;

               canvas2frameRatio = 0.8;
 
               CM_consistency=true;
               
               // Parse geometry specification string, convert each line separate of geometry input to an Array List 
               // (alGeomInputLine), and place each Array List not containing an "=" into a second Array List (alGeom)
               
               //Samples
               StringBuffer sbGeom = new StringBuffer();
               /*
                *  
               // formamide
               sbGeom.append("C" + "\n" + "O 1 rCO" + "\n" + "N 1 rCN  2 aNCO" + "\n" + "H 3 rNHa 1 aCNHa 2 0.0" + "\n");
               sbGeom.append("H 3 rNHb 1 aCNHb 2 180.0" + "\n" + "H 1 rCH  2 aHCO  4 180.0" + "\n");
               sbGeom.append("\n");
               sbGeom.append("rCO=1.1962565" + "\n" + "rCN=1.3534065" + "\n" + "rNHa=0.9948420" + "\n");
               sbGeom.append("rNHb=0.9921367" + "\n" + "rCH=1.0918368" + "\n" + "aNCO=124.93384" + "\n");
               sbGeom.append("aCNHa=119.16000" + "\n" + "aCNHb=121.22477" + "\n" + "aHCO=122.30822" + "\n");



               // formamide
               sbGeom.append("C 6 0.0  0.0  0.0" + "\n");
               sbGeom.append("O 8 0.0  0.0  1.1962565" + "\n");
               sbGeom.append("N 7 0.0   -1.109541342468682   -0.775001395866514" + "\n");               
               sbGeom.append("H 1 0.0   -2.004412477898668   -0.3403564189735936" + "\n");
               sbGeom.append("H 1 -1.0390099432170336E-16   -1.0453597426919177   -1.7650599566279395" + "\n");               
               sbGeom.append("H 1 -1.1301092880011223E-16   0.9228042638807764   -0.5835579563314158" + "\n");



               sbGeom.append("C 6 0.0  0.0  0.0" + "\n");
               sbGeom.append("O 8 0.0  0.0  1.01962565" + "\n");



               sbGeom.append("F" + "\n" + "N 1 rNF" + "\n" + "H 2 rNH  1 aFNH" + "\n" + "H 2 rNH 1 aFNH 3 aHNH" + "\n");
               sbGeom.append("O 2 rNO 3 aONH 4 aONH" + "\n" + "H 5 rOH  2 aHON  1 180.0" + "\n");
               sbGeom.append("\n");
               sbGeom.append("rNF=1.7125469" + "\n" + "rNH=0.9966981" + "\n" + "rNO=1.9359887" + "\n");
               sbGeom.append("rOH=0.9828978" + "\n" + "aFNH=90.18493" + "\n" + "aONH=79.34339" + "\n");
               sbGeom.append("aHON=100.78851" + "\n" + "aHNH=108.57000" + "\n");



               // methylnitrene
               sbGeom.append("N" + "\n" + "C 1 rCN" + "\n" + "H 2 rCH  1 aHCN" + "\n" + "H 2 rCH 1 aHCN 3 120.0" + "\n");
               sbGeom.append("H 2 rCH 1 aHCN 3 -120.0" + "\n");
               sbGeom.append("\n");
               sbGeom.append("rCN=1.4329216" + "\n" + "rCH=1.0876477" + "\n" + "aHCN=110.21928" + "\n");



               // BiCl3
               sbGeom.append("Bi" + "\n" + "Cl 1 rBiCl" + "\n" + "Cl 1 rBiCl  2 aClBiCl" + "\n" + "Cl 1 rBiCl 2 aClBiCl 3 aClBiCl" + "\n");
               sbGeom.append("\n");
               sbGeom.append("rBiCl=2.48" + "\n" + "aClBiCl=99.0" + "\n");



               sbGeom.append("N" + "\n" + "C 1 rCN" + "\n" + "H 2 rCH  1 aHCN" + "\n" + "H 2 rCH 1 aHCN 3 120.0" + "\n");
               sbGeom.append("\n");
               sbGeom.append("rCN=1.4329216" + "\n" + "rCH=1.0876477" + "\n" + "aHCN=110.21928" + "\n");



               // Water-ammonia dimer
               sbGeom.append("H" + "\n" + "O 1 rOH" + "\n" + "H 2 rOH  1 aHOH" + "\n" + "N 2 R 1 aHOH 3 0.0" + "\n");
               sbGeom.append("H 4 rNH 3 aNHaxis 1 180.0" + "\n" + "H 4 rNH 3 aNHaxis 5 120.0" + "\n");
               sbGeom.append("H 4 rNH 3 aNHaxis 5 -120.0" + "\n");
               sbGeom.append("\n");
               sbGeom.append("rOH=0.956" + "\n" + "aHOH=105.2" + "\n" + "rNH=1.0124" + "\n");
               sbGeom.append("aNHaxis=112.1451" + "\n" + "R=2.93" + "\n");


               // water trimer


               // arbitrary molecule
               sbGeom.append("O" + "\n" + "C 1 rCOa" + "\n" + "N 2 rCNa  1 aNCO" + "\n" + "C 3 rCNb 2 aCNC 1 wCNCO" + "\n");
               sbGeom.append("O 4 rCOb 3 aOCN 2 wOCNC" + "\n" + "C 2 rCC  1 aCCO 5 wCCOO" + "\n" + "H 6 rCH1 2 aHCC1 1 wHCCO1" + "\n");
               sbGeom.append("H 6 rCH2 2 aHCC2 1 wHCCO2" + "\n" + "H 6 rCH3 2 aHCC3 1 wHCCO3" + "\n" + "H 2 rCHa 1 aHCOa 5 wHCOOa" + "\n");
               sbGeom.append("H 3 rNH 2 aHNC 1 wHNCO" + "\n" + "H 4 rCHb 5 aHCOb 1 wHCOOb" + "\n" + "H 4 rCHc 5 aHCOc 1 wHCOOc" + "\n");
               sbGeom.append("\n");
               sbGeom.append("rCOa=1.43" + "\n" + "rCNa=1.47" + "\n" + "rCNb=1.47" + "\n" + "rCOb=1.43" + "\n");
               sbGeom.append("aNCO=106.0" + "\n" + "aCNC=104.0" + "\n" + "aOCN=106.0" + "\n" + "wCNCO=30.0" + "\n");
               sbGeom.append("wOCNC=-30.0" + "\n" + "rCC=1.54" + "\n" + "aCCO=110.0" + "\n" + "wCCOO=-150.0" + "\n");
               sbGeom.append("rCH1=1.09" + "\n" + "rCH2=1.09" + "\n" + "rCH3=1.09" + "\n" + "wHCC1=109.0" + "\n");
               sbGeom.append("aHCC1=109.0" + "\n" + "aHCC2=109.0" + "\n" + "aHCC3=109.0" + "\n" + "wHCCO1=60.0" + "\n" + "wHCCO2=-60.0" + "\n");
               sbGeom.append("wHCCO3=180.0" + "\n" + "rCHa=1.09" + "\n" + "aHCOa=110.0" + "\n" + "wHCOOa=100.0" + "\n");
               sbGeom.append("rNH=1.01" + "\n" + "aHNC=110.0" + "\n" + "wHNCO=170.0" + "\n" + "rCHb=1.09" + "\n");
               sbGeom.append("rCHc=1.09" + "\n" + "aHCOb=110.0" + "\n" + "aHCOc=110.0" + "\n" + "wHCOOb=150.0" + "\n");
               sbGeom.append("wHCOOc=-100.0" + "\n");


//               sbGeom.append("O" + "\n" + "H 1 0.965" + "\n");


//               sbGeom.append("O 8" + "\n");
//               sbGeom.append("H 1 0.965 1.206" + "\n");

//               sbGeom.append("C 6  0.0  0.0  0.0" + "\n");
//               sbGeom.append("O 8  0.0  0.0  1.43" + "\n");
//               sbGeom.append("H 1  0.965  1.206  1.05" + "\n");
               
*/


               // Testing for symmetry


/*

// point group Cs


               // Formaldehyde
               invokeSymm = true;
               symm = "Cs";
               sbGeom.append("O   8.0   0.01       -0.8669736       0.0" + "\n");
               sbGeom.append("C   6.0   0.0         0.3455497       0.0" + "\n");
               sbGeom.append("H   1.0  -0.01        0.9295804       0.9376713" + "\n");



               // Formaldehyde  (complete molecular structure - point group symmetry not used)
               sbGeom.append("C   8.0   0.0         0.0        0.0" + "\n");
               sbGeom.append("O   6.0   0.0         1.22       0.0" + "\n");
               sbGeom.append("H   1.0   0.94       -0.54       0.0" + "\n");
               sbGeom.append("H   1.0  -0.94       -0.54       0.0" + "\n");


// point group Ci


               // trans 1,2-difluoro 1,2-dichloro ethane
               invokeSymm = true;
               symm = "Ci";
               sbGeom.append("C   6.0  -0.0404314       0.0340784       0.7144152" + "\n");
               sbGeom.append("H   1.0  -0.5269567       0.8706125       1.0117693" + "\n");
               sbGeom.append("F   9.0  -0.6595780      -1.0335664       1.1306903" + "\n");
               sbGeom.append("Cl 17.0   1.5029963       0.0624642       1.3707514" + "\n");



               // trans 1,2-difluoro 1,2-dichloro ethane  (complete molecular structure - point group symmetry not used) 
               sbGeom.append("C   6.0  -0.0404314       0.0340784       0.7144152" + "\n");
               sbGeom.append("C   6.0   0.0404314      -0.0340784      -0.7144152" + "\n");
               sbGeom.append("H   1.0  -0.5269567       0.8706125       1.0117693" + "\n");
               sbGeom.append("H   1.0   0.5269567      -0.8706125      -1.0117693" + "\n");
               sbGeom.append("F   9.0  -0.6595780      -1.0335664       1.1306903" + "\n");
               sbGeom.append("F   9.0   0.6595780       1.0335664      -1.1306903" + "\n");
               sbGeom.append("Cl 17.0   1.5029963       0.0624642       1.3707514" + "\n");
               sbGeom.append("Cl 17.0  -1.5029963      -0.0624642      -1.3707514" + "\n");


// point group Cn


               // Hydrogen peroxide
               invokeSymm = true;
               symm = "Cn";
               naxis = 2;
//               sbGeom.append("O   8.0   0.0           1.3723898     -0.0996337" + "\n");
//               sbGeom.append("H   1.0   1.5576983     1.6930786      0.7970698" + "\n");

               sbGeom.append("O   8.0   0.0           0.6861949     -0.0498168" + "\n");
               sbGeom.append("H   1.0   1.5576983     0.8465393      0.3985349" + "\n");


               // Hydrogen peroxide   (complete molecular structure - point group symmetry not used) 
//               sbGeom.append("O   8.0   0.0           1.3723898     -0.0996337" + "\n");
//               sbGeom.append("O   8.0   0.0          -1.3723898     -0.0996337" + "\n");
//               sbGeom.append("H   1.0   1.5576983     1.6930786      0.7970698" + "\n");
//               sbGeom.append("H   1.0  -1.5576983    -1.6930786      0.7970698" + "\n");

               sbGeom.append("O   8.0   0.0           0.6861949     -0.0498168" + "\n");
               sbGeom.append("O   8.0   0.0          -0.6861949     -0.0498168" + "\n");
               sbGeom.append("H   1.0   1.5576983     0.8465393      0.3985349" + "\n");
               sbGeom.append("H   1.0  -1.5576983    -0.8465393      0.3985349" + "\n");


// point group Cnv


               // Water (Cartesian coordinates)
               invokeSymm = true;
               symm = "Cnv";
               naxis = 2;
               sbGeom.append("O   8.0  0.0     0.0          0.0" + "\n");
//               sbGeom.append("H   1.0  0.0     0.7572157    0.5865358" + "\n");
               sbGeom.append("H   1.0  0.0     1.428       -1.096" + "\n");



               // Water (Z-matrix)
               sbGeom.append("O" + "\n" + "H 1 rOH" + "\n" + "H 1 rOH  2 aHOH" + "\n");
               sbGeom.append("\n");
               sbGeom.append("rOH=0.95" + "\n" + "aHOH=104.5" + "\n");



               // Phosphine oxide
               invokeSymm = true;
               symm = "Cnv";
               naxis = 3;
               sbGeom.append("P  15.0  0.0         0.0          0.0" + "\n");
               sbGeom.append("O   8.0  0.0         0.0          1.4701" + "\n");
               sbGeom.append("H   1.0  1.23359     0.0         -0.642102" + "\n");



               // HCN
               invokeSymm = true;
               symm = "Cnv";
               naxis = 4;
               sbGeom.append("H   1.0   0.0       0.0     -1.0589956" + "\n");
               sbGeom.append("C   6.0   0.0       0.0      0.0" + "\n");
               sbGeom.append("N   7.0   0.0       0.0      1.1327718" + "\n");



               // Ammonia
               invokeSymm = true;
               symm = "Cnv";
               naxis = 3;
               sbGeom.append("N   7.0   0.0       0.0             0.16008228" + "\n");
               sbGeom.append("H   1.0   0.0       1.81793530     -0.37352532" + "\n");


// point group Cnh


               // Boron hydroxide
               invokeSymm = true;
               symm = "Cnh";
               naxis = 3;
               sbGeom.append("B   5.0     0.0           0.0             0.0" + "\n");
               sbGeom.append("O   8.0     0.0           1.30129442      0.0" + "\n");
               sbGeom.append("H   1.0    -0.80522102    1.72366565      0.0" + "\n");


// point group Dn


               // C(NO2)3
               invokeSymm = true;
               symm = "Dn";
               naxis = 3;
               sbGeom.append("C 6.0  0.0            0.0           0.0" + "\n");
               sbGeom.append("N 7.0  1.3285648      0.0           0.0" + "\n");
               sbGeom.append("O 8.0  1.8685582      0.8757526     0.4702412" + "\n");



// point group Dnd


               // Ethane
               invokeSymm = true;
               symm = "Dnd";
               naxis = 3;
               sbGeom.append("C 6.0  0.0      0.0      0.65" + "\n");
               sbGeom.append("H 1.0  1.81252  1.16252  1.16522" + "\n");


               // Test molecule for 'Dnd' point group
               invokeSymm = true;
               symm = "Dnd_test";
               naxis = 3;
               sbGeom.append("C 6.0  0.0      0.0      0.65" + "\n");
               sbGeom.append("H 1.0  0.0      1.211    1.211" + "\n");               


               // Ethane
               sbGeom.append("C 6.0  0.0      0.0      0.65" + "\n");
               sbGeom.append("H 1.0  1.62522  1.62522  1.62522" + "\n");           
               sbGeom.append("H 1.0  0.59487  -2.2201  1.62522" + "\n");
               sbGeom.append("H 1.0  -2.2201  0.59487  1.62522" + "\n");
               sbGeom.append("C 6.0  0.0      0.0      -0.65" + "\n");
               sbGeom.append("H 1.0 -1.62522 -1.62522 -1.62522" + "\n");           
               sbGeom.append("H 1.0 -0.59487  2.2201  -1.62522" + "\n");
               sbGeom.append("H 1.0  2.2201 -0.59487  -1.62522" + "\n");


               // Ethane  [ with 'z' --> ('-z') ]
               sbGeom.append("C 6.0  0.0      0.0     -0.65" + "\n");
               sbGeom.append("H 1.0  1.62522  1.62522  -1.62522" + "\n");           
               sbGeom.append("H 1.0  0.59487  -2.2201  -1.62522" + "\n");
               sbGeom.append("H 1.0  -2.2201  0.59487  -1.62522" + "\n");
               sbGeom.append("C 6.0  0.0      0.0      0.65" + "\n");
               sbGeom.append("H 1.0 -1.62522 -1.62522 1.62522" + "\n");           
               sbGeom.append("H 1.0 -0.59487  2.2201  1.62522" + "\n");
               sbGeom.append("H 1.0  2.2201 -0.59487  1.62522" + "\n");


               // Sodium crown ether
               invokeSymm = true;
               symm = "Dnd";
               naxis = 3;
               sbGeom.append("Na 23.0  0.0      0.0      0.0" + "\n");
               sbGeom.append("O   8.0  1.33848  4.99526  0.15441" + "\n");
               sbGeom.append("H   1.0  6.73421 -0.67239  2.65816" + "\n");
               sbGeom.append("C   6.0  6.75992 -0.48450  0.61366" + "\n");
               sbGeom.append("H   1.0  8.64976  0.07092  0.03454" + "\n");



               // Sodium crown ether (with shorter bond lengths)
               invokeSymm = true;
               symm = "Dnd";
               naxis = 3;
               sbGeom.append("Na 23.0  0.0        0.0        0.0" + "\n");
               sbGeom.append("O   8.0  0.44616    1.66509    0.05147" + "\n");
               sbGeom.append("H   1.0  2.24140   -0.22413    0.88605" + "\n");
               sbGeom.append("C   6.0  2.25331   -0.16150    0.20455" + "\n");
               sbGeom.append("H   1.0  2.88325    0.02364    0.01151" + "\n");


               // Ferrocene
               invokeSymm = true;
               symm = "Dnd";
               naxis = 5;
               sbGeom.append("Fe  53.0  0.0    0.0      0.0" + "\n");
               sbGeom.append("C    6.0  0.0    1.194    1.789" + "\n");
               sbGeom.append("H    1.0  0.0    2.256    1.789" + "\n");


               // CH2CCH2
               invokeSymm = true;
               symm = "Dnd";
               naxis = 2;
               sbGeom.append("C  6.0  0.0    0.0      0.0" + "\n");
               sbGeom.append("C  6.0  0.0    0.0      1.300" + "\n");
               sbGeom.append("H  1.0  0.656  0.656    1.857" + "\n");


// point group Dnh

               // C6H6 (benzene)
               invokeSymm = true;
               symm = "Dnh";
               naxis = 6;
               sbGeom.append("C  6.0  2.626720    0.0      0.0" + "\n");
               sbGeom.append("C  6.0  4.686522    0.0      1.300" + "\n");



// point group S2n


               // 1,3,5,7-tetrafluorocyclooctane ( C8H4F4 )
               invokeSymm = true;
               symm = "S2n";
               naxis = 2;
               sbGeom.append("C 6.0  1.12761874      1.12761874     -0.36111599" + "\n");
               sbGeom.append("C 6.0  0.02588201      1.58108002      0.32802516" + "\n");
               sbGeom.append("H 1.0  1.64192930      1.82938629     -0.87799154" + "\n");
               sbGeom.append("F 9.0  0.26098002      2.67908960      0.95452201" + "\n");



               // 1,3,5,7-tetrafluorocyclooctane (complete molecular structure)
               sbGeom.append("C 6.0 -1.12761874      1.12761874      0.36111599" + "\n");
               sbGeom.append("C 6.0  0.02588201      1.58108002      0.32802516" + "\n");
               sbGeom.append("C 6.0  1.12761874      1.12761874     -0.36111599" + "\n");
               sbGeom.append("C 6.0  1.58108002     -0.02588201     -0.32802516" + "\n");
               sbGeom.append("C 6.0  1.12761874     -1.12761874      0.36111599" + "\n");
               sbGeom.append("C 6.0 -0.02588201     -1.58108002      0.32802516" + "\n");
               sbGeom.append("C 6.0 -1.12761874     -1.12761874     -0.36111599" + "\n");
               sbGeom.append("C 6.0 -1.58108002      0.02588201     -0.32802516" + "\n");


               sbGeom.append("H 1.0 -1.82938629      1.64192930      0.87799154" + "\n");
               sbGeom.append("H 1.0  1.64192930      1.82938629     -0.87799154" + "\n");
               sbGeom.append("H 1.0  1.82938629     -1.64192930      0.87799154" + "\n");
               sbGeom.append("H 1.0 -1.64192930     -1.82938629     -0.87799154" + "\n");


               sbGeom.append("F 9.0  0.26098002      2.67908960      0.95452201" + "\n");
               sbGeom.append("F 9.0  2.67908960     -0.26098002     -0.95452201" + "\n");
               sbGeom.append("F 9.0 -0.26098002     -2.67908960      0.95452201" + "\n");
               sbGeom.append("F 9.0 -2.67908960      0.26098002     -0.95452201" + "\n");


// point group Td

               invokeSymm = true;
               symm = "Td";
               sbGeom.append("C 6.0  0.0      0.0      0.0" + "\n");
               sbGeom.append("H 1.0  0.62522  0.62522  0.62522" + "\n");


// point group Th

// point group O

// point group Oh


*/

///////////  Display the following molecule:



              // C(NO2)3
/*               invokeSymm = true;
               symm = "Dn";
               naxis = 3;
               sbGeom.append("C 6.0  0.0            0.0           0.0" + "\n");
               sbGeom.append("N 7.0  1.3285648      0.0           0.0" + "\n");
               sbGeom.append("O 8.0  1.8685582      0.8757526     0.4702412" + "\n");
*/


/*
               // Ferrocene
               invokeSymm = true;
               symm = "Dnd";
               naxis = 5;
               sbGeom.append("Fe  53.0  0.0    0.0      0.0" + "\n");
               sbGeom.append("C    6.0  0.0    1.194    1.789" + "\n");
               sbGeom.append("H    1.0  0.0    2.256    1.789" + "\n");
*/



               // 1,3,5,7-tetrafluorocyclooctane ( C8H4F4 )
/*               invokeSymm = true;
               symm = "S2n";
               naxis = 2;
               sbGeom.append("C 6.0  1.12761874      1.12761874     -0.36111599" + "\n");
               sbGeom.append("C 6.0  0.02588201      1.58108002      0.32802516" + "\n");
               sbGeom.append("H 1.0  1.64192930      1.82938629     -0.87799154" + "\n");
               sbGeom.append("F 9.0  0.26098002      2.67908960      0.95452201" + "\n");
*/
               //strGeom = sbGeom.toString();
 
///////////
               
               String geomToken, var, value;
               ArrayList alGeomElements, alElement, alElem3, alElem4;

               alGeom = new ArrayList();
 
               StringTokenizer stok1, stok2;

               stok1 = new StringTokenizer(strGeom, "\n");

               while(stok1.hasMoreTokens())
               {
                    geomToken = stok1.nextToken();

                    if (geomToken.indexOf("=") == -1)
                    {
                         alGeomElements = new ArrayList(); 

                         stok2 = new StringTokenizer(geomToken);                     
                
                         while(stok2.hasMoreTokens())
                         {
                              alGeomElements.add(stok2.nextToken());
                         }

                         alGeom.add(alGeomElements);
                    }
                    else
                    {
                         stok2 = new StringTokenizer(geomToken, "=");             

                         var = stok2.nextToken();
                         value = stok2.nextToken();                         

                         for (index = 0; index < alGeom.size(); index++)
                         {
                              alElement = new ArrayList(); 

                              alElement = (ArrayList)(alGeom.get(index));

                              if (alElement.contains(var))
                              {
                                   for (int ndx = 0; ndx < alElement.size(); ndx++)
                                   {
                                        k = alElement.indexOf(var);

                                        if (k != -1)
                                        {
                                             alElement.set(k, value);
                                        }
                                        else
                                        {
                                             break;
                                        }
                                   }

                                   alGeom.set(index, alElement);
                              }
                         }
                    }
               }

               
               natoms = alGeom.size();

               coordFormat = getCoordFormat(alGeom, natoms);

               // Convert ZMatrix to Cartesian coordinate format

               atomCoords = new double[3*natoms];
               atomCoords = getAtomCoords(alGeom, natoms, coordFormat);

               // Get identity of atoms in the molecule being displayed
               atomID = new String[natoms];
               atomID = getAtomID(alGeom, natoms);
               
////////       //


               if (invokeSymm == true)
               {
                    int maxndx;
           
                    double X0, Y0, Z0, X, Y, Z, XX, YY, ZZ, rotx, roty, rotz, symmx, symmy, symmz;
                    double rotangle;                                                     // rotation angle about principal axis
                    double del;  
                    double rfx, rfy, rfz;                   
                    double lamx, lamy, lamz;
                    double lambda = 0.0001;   

                    double[][] symMatrx = new double[3][3];
             
                    String atmsymbl;

                    ArrayList symmStructure;
                  
                    if (symm.equalsIgnoreCase("C1"))
                    {
                         // do nothing
                    }
                    else if (symm.equalsIgnoreCase("Cs"))
                    {
                         nsymm = 1;    
                    }
                    else if (symm.equalsIgnoreCase("Ci"))
                    {
                         nsymm = 2;    
                    }
                   else if (symm.equalsIgnoreCase("Cn"))
                    {
                         nsymm = 3;
                    }
                     else if (symm.equalsIgnoreCase("Cnv"))
                    {
                         nsymm = 4;
                    }
                     else if (symm.equalsIgnoreCase("Cnh"))
                    {
                         nsymm = 5;
                    }
                    else if (symm.equalsIgnoreCase("Dn"))
                    {
                         nsymm = 6;
                    }
                    else if (symm.equalsIgnoreCase("Dnd"))
                    {
                         nsymm = 7;
                    }
                    else if (symm.equalsIgnoreCase("Dnh"))
                    {
                         nsymm = 8;
                    }
                    else if (symm.equalsIgnoreCase("S2n"))
                    {
                         nsymm = 9;
                    }
                    else if (symm.equalsIgnoreCase("T"))
                    {
                         nsymm = 10;
                    }
                    else if (symm.equalsIgnoreCase("Td"))
                    {
                         nsymm = 11;
                    }
                    else if (symm.equalsIgnoreCase("Th"))
                    {
                         nsymm = 12;
                    }
                    else if (symm.equalsIgnoreCase("O"))
                    {
                         nsymm = 13;
                    }
                    else if (symm.equalsIgnoreCase("Oh"))
                    {
                         nsymm = 14;
                    }
                    else if (symm.equalsIgnoreCase("Dnd_test"))
                    {
                         nsymm = 100;
                    }
                    switch (nsymm)
                    {
                         case 1:         // point group 'Cs'

                                 symmStructure = new ArrayList();

                                 for (j = 0; j < natoms; j++)
                                 {
                                      symmStructure.add(atomID[j]);
            
                                      symmStructure.add(atomCoords[(3*j)]);
                                      symmStructure.add(atomCoords[(3*j) + 1]);
                                      symmStructure.add(atomCoords[(3*j) + 2]);
                                 }
                                   
                                 maxndx = natoms;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      coincidence = false;

                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = -Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );        

                                      for (ndx = 0; ndx < maxndx; ndx++)
                                      {
                                           XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );        
                                           YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );        
                                           ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );                                       

                                           lamx = Math.abs(X - XX);
                                           lamy = Math.abs(Y - YY);
                                           lamz = Math.abs(Z - ZZ);

                                           if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                           {
                                                coincidence = true;
                                           }
                                      }

                                      if (coincidence == false)
                                      {
                                           symmStructure.add(atmsymbl);

                                           symmStructure.add(X);
                                           symmStructure.add(Y);
                                           symmStructure.add(Z);

                                           maxndx += 1;                                      
                                      }
                                 }                            

                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                         case 2:         // point group 'Ci'

                                 symmStructure = new ArrayList();

                                 for (j = 0; j < natoms; j++)
                                 {
                                      symmStructure.add(atomID[j]);

                                      symmStructure.add(atomCoords[(3*j)]);
                                      symmStructure.add(atomCoords[(3*j) + 1]);
                                      symmStructure.add(atomCoords[(3*j) + 2]);
                                 }

                                 maxndx = natoms;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      coincidence = false;

                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = -Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = -Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = -Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );        

                                      for (ndx = 0; ndx < maxndx; ndx++)
                                      {
                                           XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );        
                                           YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );        
                                           ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );                                       

                                           lamx = Math.abs(X - XX);
                                           lamy = Math.abs(Y - YY);
                                           lamz = Math.abs(Z - ZZ);

                                           if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                           {
                                                coincidence = true;
                                           }
                                      }

                                      if (coincidence == false)
                                      {
                                           symmStructure.add(atmsymbl);

                                           symmStructure.add(X);
                                           symmStructure.add(Y);
                                           symmStructure.add(Z);

                                           maxndx += 1;                                      
                                      }
                                 }          

                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                         case 3:         // point group 'Cn' 

                                 symmStructure = new ArrayList();

                                 rotangle = 2*Math.PI/naxis;

                                 symMatrx[0][0] = Math.cos(rotangle);
                                 symMatrx[0][1] = Math.sin(rotangle);                       
                                 symMatrx[0][2] = 0.0;                                       
                                 symMatrx[1][0] = -Math.sin(rotangle);
                                 symMatrx[1][1] = Math.cos(rotangle);                       
                                 symMatrx[1][2] = 0.0;
                                 symMatrx[2][0] = 0.0;
                                 symMatrx[2][1] = 0.0; 
                                 symMatrx[2][2] = 1.0;
           
                                 for (j = 0; j < natoms; j++)
                                 {
                                      symmStructure.add(atomID[j]);
            
                                      symmStructure.add(atomCoords[(3*j)]);
                                      symmStructure.add(atomCoords[(3*j) + 1]);
                                      symmStructure.add(atomCoords[(3*j) + 2]);
                                 }

                                 maxndx = natoms;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );        
           
                                      for (k = 0; k < (naxis - 1); k++)
                                      {
                                           coincidence = false;
                                           
                                           rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                           roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                           rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                           for (ndx = 0; ndx < maxndx; ndx++)
                                           {
                                                XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );        
                                                YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );        
                                                ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );                                       

                                                lamx = Math.abs(rotx - XX);
                                                lamy = Math.abs(roty - YY);
                                                lamz = Math.abs(rotz - ZZ);

                                                if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                                {
                                                     coincidence = true;
                                                }
                                           }

                                           if (coincidence == false)
                                           {
                                                symmStructure.add(atmsymbl);

                                                symmStructure.add(rotx);
                                                symmStructure.add(roty);
                                                symmStructure.add(rotz);

                                                maxndx += 1;                                      
                                           }

                                           X = rotx;
                                           Y = roty;
                                           Z = rotz;
                                      }
                                 }

                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }


                                 break;
                         case 4:         // point group 'Cnv' 

                                 symmStructure = new ArrayList();

                                 rotangle = 2*Math.PI/naxis;

                                 symMatrx[0][0] = Math.cos(rotangle);
                                 symMatrx[0][1] = Math.sin(rotangle);                       
                                 symMatrx[0][2] = 0.0;                                       
                                 symMatrx[1][0] = -Math.sin(rotangle);
                                 symMatrx[1][1] = Math.cos(rotangle);                       
                                 symMatrx[1][2] = 0.0;
                                 symMatrx[2][0] = 0.0;
                                 symMatrx[2][1] = 0.0; 
                                 symMatrx[2][2] = 1.0;
           
                                 for (j = 0; j < natoms; j++)
                                 {
                                      symmStructure.add(atomID[j]);
            
                                      symmStructure.add(atomCoords[(3*j)]);
                                      symmStructure.add(atomCoords[(3*j) + 1]);
                                      symmStructure.add(atomCoords[(3*j) + 2]);
                                 }

                                 maxndx = natoms;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      coincidence = false;

                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );        


                                      for (ndx = 0; ndx < maxndx; ndx++)
                                      {
                                           XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );        
                                           YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );        
                                           ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );                                       

                                           lamx = Math.abs(X - XX);
                                           lamy = Math.abs(Y - YY);
                                           lamz = Math.abs(Z - ZZ);

                                           if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                           {
                                                coincidence = true;
                                           }
                                      }

                                      if (coincidence == false)
                                      {
                                           symmStructure.add(atmsymbl);

                                           symmStructure.add(X);
                                           symmStructure.add(Y);
                                           symmStructure.add(Z);

                                           maxndx += 1;                                      
                                      }
                                 }
               
                                 natoms = symmStructure.size()/4;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );        

                                      for (k = 0; k < (naxis - 1); k++)
                                      {
                                           coincidence = false;
                                           
                                           rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                           roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                           rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                           for (ndx = 0; ndx < maxndx; ndx++)
                                           {
                                                XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );        
                                                YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );        
                                                ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );                                       

                                                lamx = Math.abs(rotx - XX);
                                                lamy = Math.abs(roty - YY);
                                                lamz = Math.abs(rotz - ZZ);

                                                if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                                {
                                                     coincidence = true;
                                                }
                                           }

                                           if (coincidence == false)
                                           {
                                                symmStructure.add(atmsymbl);

                                                symmStructure.add(rotx);
                                                symmStructure.add(roty);
                                                symmStructure.add(rotz);

                                                maxndx += 1;                                      
                                           }

                                           X = rotx;
                                           Y = roty;
                                           Z = rotz;
                                      }
                                 }

                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                         case 5:         // point group 'Cnh' 

                                 symmStructure = new ArrayList();

                                 rotangle = 2*Math.PI/naxis;

                                 symMatrx[0][0] = Math.cos(rotangle);
                                 symMatrx[0][1] = Math.sin(rotangle);                       
                                 symMatrx[0][2] = 0.0;                                       
                                 symMatrx[1][0] = -Math.sin(rotangle);
                                 symMatrx[1][1] = Math.cos(rotangle);                       
                                 symMatrx[1][2] = 0.0;
                                 symMatrx[2][0] = 0.0;
                                 symMatrx[2][1] = 0.0; 
                                 symMatrx[2][2] = 1.0;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      symmStructure.add(atomID[j]);
            
                                      symmStructure.add(atomCoords[(3*j)]);
                                      symmStructure.add(atomCoords[(3*j) + 1]);
                                      symmStructure.add(atomCoords[(3*j) + 2]);
                                 }
                                 
                                 maxndx = natoms;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );        

                                      for (k = 0; k < (naxis - 1); k++)
                                      {
                                           coincidence = false;
                                           
                                           rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                           roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                           rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                           for (ndx = 0; ndx < maxndx; ndx++)
                                           {
                                                XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );        
                                                YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );        
                                                ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );                                       

                                                lamx = Math.abs(rotx - XX);
                                                lamy = Math.abs(roty - YY);
                                                lamz = Math.abs(rotz - ZZ);

                                                if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                                {
                                                     coincidence = true;
                                                }
                                           }

                                           if (coincidence == false)
                                           {
                                                symmStructure.add(atmsymbl);

                                                symmStructure.add(rotx);
                                                symmStructure.add(roty);
                                                symmStructure.add(rotz);  

                                                maxndx += 1;                                    
                                           }

                                           X = rotx;
                                           Y = roty;
                                           Z = rotz;
                                      }
                                 }

                                 natoms = symmStructure.size()/4;

                                 // Reflect all atoms in the x,y-plane
                                 for (j = 0; j < natoms; j++)
                                 {
                                      coincidence = false;

                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = -Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );        

                                      for (ndx = 0; ndx < natoms; ndx++)
                                      {
                                           XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );        
                                           YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );        
                                           ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );                                       

                                           lamx = Math.abs(X - XX);
                                           lamy = Math.abs(Y - YY);
                                           lamz = Math.abs(Z - ZZ);

                                           if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )                                 if ( (X == XX) && (Y == YY) && (X == ZZ) )
                                           {
                                                coincidence = true;
                                           }
                                      }

                                      if (coincidence == false)
                                      {
                                           symmStructure.add(atmsymbl);

                                           symmStructure.add(X);
                                           symmStructure.add(Y);
                                           symmStructure.add(Z);

                                           maxndx += 1;                                      
                                      }
                                 }      

                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                        case 6:                 // point group 'Dn'

                                 symmStructure = new ArrayList();

                                 // Rotate all coordinates by an angle of pi about x-axis and add all new coordinates.
                                 //  Then  rotate all coordinates by an angle of 2pi/naxis about z-axis (naxis - 1) times.

                                 rotangle = 2*Math.PI/naxis;

                                 symMatrx[0][0] = Math.cos(rotangle);
                                 symMatrx[0][1] = Math.sin(rotangle);                       
                                 symMatrx[0][2] = 0.0;                                       
                                 symMatrx[1][0] = -Math.sin(rotangle);
                                 symMatrx[1][1] = Math.cos(rotangle);                       
                                 symMatrx[1][2] = 0.0;
                                 symMatrx[2][0] = 0.0;
                                 symMatrx[2][1] = 0.0; 
                                 symMatrx[2][2] = 1.0;
           
                                 for (j = 0; j < natoms; j++)
                                 {
                                      X = atomCoords[(3*j)];
                                      Y = atomCoords[(3*j) + 1];
                                      Z = atomCoords[(3*j) + 2];

                                      symmStructure.add(atomID[j]);
                                      symmStructure.add(X);
                                      symmStructure.add(Y);
                                      symmStructure.add(Z);
                                 }                       

                                 // Rotate by pi about x-axis
                                 maxndx = natoms;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      coincidence = false;

                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = -Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = -Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );        

                                      for (ndx = 0; ndx < natoms; ndx++)
                                      {
                                           XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );        
                                           YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );        
                                           ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );                                       

                                           lamx = Math.abs(X - XX);
                                           lamy = Math.abs(Y - YY);
                                           lamz = Math.abs(Z - ZZ);

                                           if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                           {
                                                coincidence = true;
                                           }
                                      }

                                      if (coincidence == false)
                                      {
                                           symmStructure.add(atmsymbl);

                                           symmStructure.add(X);
                                           symmStructure.add(Y);
                                           symmStructure.add(Z);

                                           maxndx += 1;                                      
                                      }
                                 }          

                                 natoms = symmStructure.size()/4;

                                 // Rotate by 2pi/naxis about z-axis (naxis - 1) times
                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );        

                                      for (k = 0; k < (naxis - 1); k++)
                                      {
                                           coincidence = false;
                                           
                                           rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                           roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                           rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                           for (ndx = 0; ndx < natoms; ndx++)
                                           {
                                                XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );        
                                                YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );        
                                                ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );                                       

                                                lamx = Math.abs(rotx - XX);
                                                lamy = Math.abs(roty - YY);
                                                lamz = Math.abs(rotz - ZZ);

                                                if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                                {
                                                     coincidence = true;
                                                }
                                           }

                                           if (coincidence == false)
                                           {
                                                symmStructure.add(atmsymbl);

                                                symmStructure.add(rotx);
                                                symmStructure.add(roty);
                                                symmStructure.add(rotz);

                                                maxndx += 1;                                      
                                           }

                                           X = rotx;
                                           Y = roty;
                                           Z = rotz;
                                      }
                                 }

                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                        case 7:                 // point group 'Dnd'
                                 symmStructure = new ArrayList();

                                 for (j = 0; j < natoms; j++)
                                 {
                                      X = atomCoords[3*j];
                                      Y = atomCoords[(3*j) + 1];                                             
                                      Z = atomCoords[(3*j) + 2];

                                      symmStructure.add(atomID[j]);

                                      symmStructure.add(X);
                                      symmStructure.add(Y);
                                      symmStructure.add(Z);
                                 }

                                 // Rotate each atom by pi about x-axis 
                                 maxndx = natoms;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      // Reflect each atom in x,z-plane
                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      Y = -Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      Z = -Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );

                                      coincidence = false;

                                      for (ndx = 0; ndx < maxndx; ndx++)
                                      {
                                           XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                           YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                           ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                           lamx = Math.abs(X - XX);
                                           lamy = Math.abs(Y - YY);
                                           lamz = Math.abs(Z - ZZ);

                                           if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                           {
                                                coincidence = true;
                                           }
                                      }
           
                                      if ( coincidence == false)
                                      {
                                           symmStructure.add(atmsymbl);

                                           symmStructure.add(X);
                                           symmStructure.add(Y);
                                           symmStructure.add(Z);

                                           maxndx += 1;
                                      }
                                 }
                                 
                                 natoms = symmStructure.size()/4;

                                 // Carry out 'S2n' point group operation on each atom

                                 rotangle = Math.PI/naxis;

                                 symMatrx[0][0] =  Math.cos(rotangle);
                                 symMatrx[0][1] =  Math.sin(rotangle);                       
                                 symMatrx[0][2] =  0.0;                                       
                                 symMatrx[1][0] = -Math.sin(rotangle);
                                 symMatrx[1][1] =  Math.cos(rotangle);                       
                                 symMatrx[1][2] =  0.0;
                                 symMatrx[2][0] =  0.0;
                                 symMatrx[2][1] =  0.0; 
                                 symMatrx[2][2] =  -1.0;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );

                                      for (k = 0; k < ( (2*naxis) - 1 ); k++)
                                      {
                                           rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                           roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                           rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                           coincidence = false;

                                           for (ndx = 0; ndx < maxndx; ndx++)
                                           {
                                                XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                                YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                                ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                                lamx = Math.abs(rotx - XX);
                                                lamy = Math.abs(roty - YY);
                                                lamz = Math.abs(rotz - ZZ);

                                                if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                                {
                                                     coincidence = true;
                                                }
                                           }
           
                                           if ( coincidence == false)
                                           {
                                                symmStructure.add(atmsymbl);

                                                symmStructure.add(rotx);
                                                symmStructure.add(roty);
                                                symmStructure.add(rotz);

                                                maxndx += 1;
                                           }

                                           X = rotx;
                                           Y = roty;
                                           Z = rotz;
                                      }
                                 }                            

                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                        case 8:                // point group 'Dnh'
                                 symmStructure = new ArrayList();
                  
                                 maxndx = natoms;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      X = atomCoords[(3*j)];
                                      Y = atomCoords[(3*j) + 1];
                                      Z = atomCoords[(3*j) + 2];

                                      symmStructure.add(atomID[j]);
                                      symmStructure.add(X);
                                      symmStructure.add(Y);
                                      symmStructure.add(Z);
                                 }

                                 // Reflect all atoms in x,y-plane
                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString();

                                      X = Double.parseDouble((symmStructure.get((4*j) + 1)).toString());
                                      Y = Double.parseDouble((symmStructure.get((4*j) + 2)).toString());
                                      Z = -Double.parseDouble((symmStructure.get((4*j) + 3)).toString());
                                    
                                      coincidence = false;

                                      for (ndx = 0; ndx < maxndx; ndx++)
                                      {
                                           XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                           YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                           ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                           lamx = Math.abs(X - XX);
                                           lamy = Math.abs(Y - YY);
                                           lamz = Math.abs(Z - ZZ);

                                           if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                           {
                                                coincidence = true;
                                           }
                                      }
           
                                      if ( coincidence == false)
                                      {
                                           symmStructure.add(atmsymbl);

                                           symmStructure.add(X);
                                           symmStructure.add(Y);
                                           symmStructure.add(Z);

                                           maxndx += 1;
                                      }
                                 }

                                 natoms = symmStructure.size()/4;

                                 // Rotate each atom by pi about x-axis 

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      // Reflect each atom in x,z-plane
                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      Y = -Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      Z = -Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );

                                      coincidence = false;

                                      for (ndx = 0; ndx < maxndx; ndx++)
                                      {
                                           XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                           YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                           ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                           lamx = Math.abs(X - XX);
                                           lamy = Math.abs(Y - YY);
                                           lamz = Math.abs(Z - ZZ);

                                           if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                           {
                                                coincidence = true;
                                           }
                                      }
           
                                      if ( coincidence == false)
                                      {
                                           symmStructure.add(atmsymbl);

                                           symmStructure.add(X);
                                           symmStructure.add(Y);
                                           symmStructure.add(Z);

                                           maxndx += 1;
                                      }
                                 }
                                 
                                 natoms = symmStructure.size()/4;

                                 // Rotate by 2pi/naxis about z-axis (naxis - 1) times

                                 rotangle = 2*Math.PI/naxis;

                                 symMatrx[0][0] = Math.cos(rotangle);
                                 symMatrx[0][1] = Math.sin(rotangle);                       
                                 symMatrx[0][2] = 0.0;                                       
                                 symMatrx[1][0] = -Math.sin(rotangle);
                                 symMatrx[1][1] = Math.cos(rotangle);                       
                                 symMatrx[1][2] = 0.0;
                                 symMatrx[2][0] = 0.0;
                                 symMatrx[2][1] = 0.0; 
                                 symMatrx[2][2] = 1.0;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );        

                                      for (k = 0; k < (naxis - 1); k++)
                                      {
                                           coincidence = false;
                                           
                                           rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                           roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                           rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                           for (ndx = 0; ndx < natoms; ndx++)
                                           {
                                                XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );        
                                                YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );        
                                                ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );                                       

                                                lamx = Math.abs(rotx - XX);
                                                lamy = Math.abs(roty - YY);
                                                lamz = Math.abs(rotz - ZZ);

                                                if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                                {
                                                     coincidence = true;
                                                }
                                           }

                                           if (coincidence == false)
                                           {
                                                symmStructure.add(atmsymbl);

                                                symmStructure.add(rotx);
                                                symmStructure.add(roty);
                                                symmStructure.add(rotz);

                                                maxndx += 1;                                      
                                           }

                                           X = rotx;
                                           Y = roty;
                                           Z = rotz;
                                      }
                                 }

                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                        case 9:                // point group 'S2n'


                                 symmStructure = new ArrayList();

                                 for (j = 0; j < natoms; j++)
                                 {
                                      X = atomCoords[(3*j)];
                                      Y = atomCoords[(3*j) + 1];
                                      Z = atomCoords[(3*j) + 2];

                                      symmStructure.add(atomID[j]);
                                      symmStructure.add(X);
                                      symmStructure.add(Y);
                                      symmStructure.add(Z);
                                 }

                                 // Reflect each atom in x,y-plane and rotate by pi/naxis about z-axis

                                 rotangle = Math.PI/naxis;

                                 symMatrx[0][0] =  Math.cos(rotangle);
                                 symMatrx[0][1] =  Math.sin(rotangle);                       
                                 symMatrx[0][2] =  0.0;                                       
                                 symMatrx[1][0] = -Math.sin(rotangle);
                                 symMatrx[1][1] =  Math.cos(rotangle);                       
                                 symMatrx[1][2] =  0.0;
                                 symMatrx[2][0] =  0.0;
                                 symMatrx[2][1] =  0.0; 
                                 symMatrx[2][2] =  -1.0;

                                 maxndx = natoms;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      coincidence = false;

                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );      
                                
                                      rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                      roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                      rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                      for (ndx = 0; ndx < maxndx; ndx++)
                                      {
                                           XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );        
                                           YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );        
                                           ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );                                       

                                           lamx = Math.abs(rotx - XX);
                                           lamy = Math.abs(roty - YY);
                                           lamz = Math.abs(rotz - ZZ);

                                           if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                           {
                                                coincidence = true;
                                           }
                                      }

                                      if (coincidence == false)
                                      {
                                           symmStructure.add(atmsymbl);

                                           symmStructure.add(rotx);
                                           symmStructure.add(roty);
                                           symmStructure.add(rotz);

                                           maxndx += 1;                                      
                                      }
                                 }

                                 natoms = symmStructure.size()/4;

                                 // Rotate all atoms 2pi/naxis about z-axis (naxis - 1) times

                                 rotangle = 2*Math.PI/naxis;

                                 symMatrx[0][0] =  Math.cos(rotangle);
                                 symMatrx[0][1] =  Math.sin(rotangle);                       
                                 symMatrx[0][2] =  0.0;                                       
                                 symMatrx[1][0] = -Math.sin(rotangle);
                                 symMatrx[1][1] =  Math.cos(rotangle);                       
                                 symMatrx[1][2] =  0.0;
                                 symMatrx[2][0] =  0.0;
                                 symMatrx[2][1] =  0.0; 
                                 symMatrx[2][2] =  1.0;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );        

                                      for (k = 0; k < (naxis - 1); k++)
                                      {
                                           rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                           roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                           rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                           coincidence = false;

                                           for (ndx = 0; ndx < maxndx; ndx++)
                                           {
                                                XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                                YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                                ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                                lamx = Math.abs(rotx - XX);
                                                lamy = Math.abs(roty - YY);
                                                lamz = Math.abs(rotz - ZZ);

                                                if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                                {
                                                     coincidence = true;
                                                }
                                           }
           
                                           if ( coincidence == false)
                                           {
                                                symmStructure.add(atmsymbl);

                                                symmStructure.add(rotx);
                                                symmStructure.add(roty);
                                                symmStructure.add(rotz);

                                                maxndx += 1;
                                           }

                                           X = rotx;
                                           Y = roty;
                                           Z = rotz;
                                      }
                                 }             

                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                       case 10:    // point group 'T'

                                 symmStructure = new ArrayList();

                                 // Rotation matrix for S2n = S4 point group operation
                                 naxis = 2;

                                 symMatrx[0][0] =  0.0;
                                 symMatrx[0][1] =  1.0;                       
                                 symMatrx[0][2] =  0.0;                                       
                                 symMatrx[1][0] = -1.0;
                                 symMatrx[1][1] =  0.0;                       
                                 symMatrx[1][2] =  0.0;
                                 symMatrx[2][0] =  0.0;
                                 symMatrx[2][1] =  0.0; 
                                 symMatrx[2][2] = -1.0;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      X = atomCoords[3*j];
                                      Y = atomCoords[(3*j) + 1];                                             
                                      Z = atomCoords[(3*j) + 2];

                                      symmStructure.add(atomID[j]);

                                      symmStructure.add(X);
                                      symmStructure.add(Y);
                                      symmStructure.add(Z);
                                 }

                                 maxndx = natoms;

                                 // Apply S2n = S4 point group operation to all atoms
                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );      

                                      for (k = 0; k < ((2*naxis) - 1); k++)
                                      {
                                           rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                           roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                           rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                           coincidence = false;

                                           for (ndx = 0; ndx < maxndx; ndx++)
                                           {
                                                XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                                YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                                ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                                lamx = Math.abs(rotx - XX);
                                                lamy = Math.abs(roty - YY);
                                                lamz = Math.abs(rotz - ZZ);

                                                if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                                {
                                                     coincidence = true;
                                                }
                                           }
           
                                           if (coincidence == false)
                                           {
                                                symmStructure.add(atmsymbl);

                                                symmStructure.add(rotx);
                                                symmStructure.add(roty);
                                                symmStructure.add(rotz);

                                                maxndx += 1;                                      
                                           }

                                           X = rotx;
                                           Y = roty;
                                           Z = rotz;                                                                   
                                      }
                                 }
           
                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                       case 11:    // point group 'Td'

                                 symmStructure = new ArrayList();

                                 // Rotation matrix for S2n = S4 point group operation
                                 naxis = 2;

                                 symMatrx[0][0] =  0.0;
                                 symMatrx[0][1] =  1.0;                       
                                 symMatrx[0][2] =  0.0;                                       
                                 symMatrx[1][0] = -1.0;
                                 symMatrx[1][1] =  0.0;                       
                                 symMatrx[1][2] =  0.0;
                                 symMatrx[2][0] =  0.0;
                                 symMatrx[2][1] =  0.0; 
                                 symMatrx[2][2] = -1.0;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      X = atomCoords[3*j];
                                      Y = atomCoords[(3*j) + 1];                                             
                                      Z = atomCoords[(3*j) + 2];

                                      symmStructure.add(atomID[j]);

                                      symmStructure.add(X);
                                      symmStructure.add(Y);
                                      symmStructure.add(Z);
                                 }

                                 maxndx = natoms;

                                 // Rotate each atom by pi about the x-axis
                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = -Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = -Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );      

                                      coincidence = false;

                                      for (ndx = 0; ndx < maxndx; ndx++)
                                      {
                                           XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                           YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                           ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                           lamx = Math.abs(X - XX);
                                           lamy = Math.abs(Y - YY);
                                           lamz = Math.abs(Z - ZZ);

                                           if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                           {
                                                coincidence = true;
                                           }
                                      }

                                      if (coincidence == false)
                                      {
                                           symmStructure.add(atmsymbl);

                                           symmStructure.add(X);
                                           symmStructure.add(Y);
                                           symmStructure.add(Z);

                                           maxndx += 1;                                      
                                      }
                                 }

                                 natoms = symmStructure.size()/4;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );      

                                      // Apply S2n = S4 point group operation to all atoms
                                      for (k = 0; k < ((2*naxis) - 1); k++)
                                      {
                                           rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                           roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                           rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                           coincidence = false;

                                           for (ndx = 0; ndx < maxndx; ndx++)
                                           {
                                                XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                                YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                                ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                                lamx = Math.abs(rotx - XX);
                                                lamy = Math.abs(roty - YY);
                                                lamz = Math.abs(rotz - ZZ);

                                                if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                                {
                                                     coincidence = true;
                                                }
                                           }
           
                                           if (coincidence == false)
                                           {
                                                symmStructure.add(atmsymbl);

                                                symmStructure.add(rotx);
                                                symmStructure.add(roty);
                                                symmStructure.add(rotz);

                                                maxndx += 1;                                      
                                           }

                                           X = rotx;
                                           Y = roty;
                                           Z = rotz;                                                                   
                                      }
                                 }
           
                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                       case 12:    // point group 'Th'

                                 symmStructure = new ArrayList();

                                 // Rotation matrix for S2n = S4 point group operation
                                 naxis = 2;

                                 symMatrx[0][0] =  0.0;
                                 symMatrx[0][1] =  1.0;                       
                                 symMatrx[0][2] =  0.0;                                       
                                 symMatrx[1][0] = -1.0;
                                 symMatrx[1][1] =  0.0;                       
                                 symMatrx[1][2] =  0.0;
                                 symMatrx[2][0] =  0.0;
                                 symMatrx[2][1] =  0.0; 
                                 symMatrx[2][2] = -1.0;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      X = atomCoords[3*j];
                                      Y = atomCoords[(3*j) + 1];                                             
                                      Z = atomCoords[(3*j) + 2];

                                      symmStructure.add(atomID[j]);

                                      symmStructure.add(X);
                                      symmStructure.add(Y);
                                      symmStructure.add(Z);
                                 }

                                 maxndx = natoms;

                                 // Apply inversion point group operation to all atoms

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = -Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = -Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = -Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );      

                                      coincidence = false;

                                      for (ndx = 0; ndx < maxndx; ndx++)
                                      {
                                           XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                           YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                           ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                           lamx = Math.abs(X - XX);
                                           lamy = Math.abs(Y - YY);
                                           lamz = Math.abs(Z - ZZ);

                                           if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                           {
                                                coincidence = true;
                                           }
                                      }
           
                                      if (coincidence == false)
                                      {
                                           symmStructure.add(atmsymbl);

                                           symmStructure.add(X);
                                           symmStructure.add(Y);
                                           symmStructure.add(Z);

                                           maxndx += 1;                                      
                                      }
                                 }
               
                                 natoms = symmStructure.size()/4;

                                 // Apply S4 point group operation to all atoms

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );      

                                      for (k = 0; k < ((2*naxis) - 1); k++)
                                      {
                                           rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                           roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                           rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                           coincidence = false;

                                           for (ndx = 0; ndx < maxndx; ndx++)
                                           {
                                                XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                                YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                                ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                                lamx = Math.abs(rotx - XX);
                                                lamy = Math.abs(roty - YY);
                                                lamz = Math.abs(rotz - ZZ);

                                                if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                                {
                                                     coincidence = true;
                                                }
                                           }
           
                                           if (coincidence == false)
                                           {
                                                symmStructure.add(atmsymbl);

                                                symmStructure.add(rotx);
                                                symmStructure.add(roty);
                                                symmStructure.add(rotz);

                                                maxndx += 1;                                      
                                           }

                                           X = rotx;
                                           Y = roty;
                                           Z = rotz;                                                                   
                                      }
                                 }
           
                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                       case 13:    // point group 'O'
                                 symmStructure = new ArrayList();

                                 naxis = 4;

                                 symMatrx[0][0] =  0.0;
                                 symMatrx[0][1] =  1.0;                       
                                 symMatrx[0][2] =  0.0;                                       
                                 symMatrx[1][0] = -1.0;
                                 symMatrx[1][1] =  0.0;                       
                                 symMatrx[1][2] =  0.0;
                                 symMatrx[2][0] =  0.0;
                                 symMatrx[2][1] =  0.0; 
                                 symMatrx[2][2] =  1.0;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      X = atomCoords[3*j];
                                      Y = atomCoords[(3*j) + 1];                                             
                                      Z = atomCoords[(3*j) + 2];

                                      symmStructure.add(atomID[j]);

                                      symmStructure.add(X);
                                      symmStructure.add(Y);
                                      symmStructure.add(Z);
                                 }

                                 maxndx = natoms;

                                 // Rotate all atoms by pi/2 about the z-axis (naxis - 1) times
                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );      

                                      for (k = 0; k < (naxis - 1); k++)
                                      {
                                           rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                           roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                           rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                           coincidence = false;
           
                                           for (ndx = 0; ndx < maxndx; ndx++)
                                           {
                                                XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                                YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                                ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                                lamx = Math.abs(rotx - XX);
                                                lamy = Math.abs(roty - YY);
                                                lamz = Math.abs(rotz - ZZ);

                                                if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                                {
                                                     coincidence = true;
                                                }
                                           }
           
                                           if (coincidence == false)
                                           {
                                                symmStructure.add(atmsymbl);

                                                symmStructure.add(rotx);
                                                symmStructure.add(roty);
                                                symmStructure.add(rotz);

                                                maxndx += 1;                                      
                                           }

                                           X = rotx;
                                           Y = roty;
                                           Z = rotz;
                                      }
                                 }
           
                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                       case 14:    // point group 'Oh'
                                 symmStructure = new ArrayList();
                                  
                                 naxis = 4;

                                 symMatrx[0][0] =  0.0;
                                 symMatrx[0][1] =  1.0;                       
                                 symMatrx[0][2] =  0.0;                                       
                                 symMatrx[1][0] = -1.0;
                                 symMatrx[1][1] =  0.0;                       
                                 symMatrx[1][2] =  0.0;
                                 symMatrx[2][0] =  0.0;
                                 symMatrx[2][1] =  0.0; 
                                 symMatrx[2][2] =  1.0;

                                 for (j = 0; j < natoms; j++)
                                 {
                                      X = atomCoords[3*j];
                                      Y = atomCoords[(3*j) + 1];                                             
                                      Z = atomCoords[(3*j) + 2];

                                      symmStructure.add(atomID[j]);

                                      symmStructure.add(X);
                                      symmStructure.add(Y);
                                      symmStructure.add(Z);
                                 }

                                 maxndx = natoms;

                                 // Apply inversion point group operation to all atoms

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = -Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = -Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = -Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );      

                                      coincidence = false;

                                      for (ndx = 0; ndx < maxndx; ndx++)
                                      {
                                           XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                           YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                           ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                           lamx = Math.abs(X - XX);
                                           lamy = Math.abs(Y - YY);
                                           lamz = Math.abs(Z - ZZ);

                                           if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                           {
                                                coincidence = true;
                                           }
                                      }
           
                                      if (coincidence == false)
                                      {
                                           symmStructure.add(atmsymbl);

                                           symmStructure.add(X);
                                           symmStructure.add(Y);
                                           symmStructure.add(Z);

                                           maxndx += 1;                                      
                                      }
                                 }
               
                                 natoms = symmStructure.size()/4;

                                 // Rotate all atoms by pi/2 about the z-axis (naxis - 1) times
                                 for (j = 0; j < natoms; j++)
                                 {
                                      atmsymbl = (symmStructure.get(4*j)).toString(); 

                                      X = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );        
                                      Y = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );        
                                      Z = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );      

                                      for (k = 0; k < (naxis - 1); k++)
                                      {
                                           rotx = (symMatrx[0][0] * X) + (symMatrx[0][1] * Y) + (symMatrx[0][2] * Z);
                                           roty = (symMatrx[1][0] * X) + (symMatrx[1][1] * Y) + (symMatrx[1][2] * Z);
                                           rotz = (symMatrx[2][0] * X) + (symMatrx[2][1] * Y) + (symMatrx[2][2] * Z);

                                           coincidence = false;
           
                                           for (ndx = 0; ndx < maxndx; ndx++)
                                           {
                                                XX = Double.parseDouble( (symmStructure.get((4*ndx) + 1)).toString() );
                                                YY = Double.parseDouble( (symmStructure.get((4*ndx) + 2)).toString() );
                                                ZZ = Double.parseDouble( (symmStructure.get((4*ndx) + 3)).toString() );
                                           
                                                lamx = Math.abs(rotx - XX);
                                                lamy = Math.abs(roty - YY);
                                                lamz = Math.abs(rotz - ZZ);

                                                if ( (lamx < lambda) && (lamy < lambda) && (lamz < lambda) )
                                                {
                                                     coincidence = true;
                                                }
                                           }
           
                                           if (coincidence == false)
                                           {
                                                symmStructure.add(atmsymbl);

                                                symmStructure.add(rotx);
                                                symmStructure.add(roty);
                                                symmStructure.add(rotz);

                                                maxndx += 1;                                      
                                           }

                                           X = rotx;
                                           Y = roty;
                                           Z = rotz;
                                      }
                                 }
           
                                 natoms = symmStructure.size()/4;

                                 atomID = new String[natoms];
                                 atomCoords = new double[3*natoms];

                                 for (j = 0; j < natoms; j++)
                                 {
                                      atomID[j] = (symmStructure.get(4*j)).toString();

                                      atomCoords[(3*j)] = Double.parseDouble( (symmStructure.get((4*j) + 1)).toString() );
                                      atomCoords[(3*j) + 1] = Double.parseDouble( (symmStructure.get((4*j) + 2)).toString() );
                                      atomCoords[(3*j) + 2] = Double.parseDouble( (symmStructure.get((4*j) + 3)).toString() );
                                 }

                                 break;
                       default:  // do nothing here 
                                  break;
                    }  
               }


               // Get atomic number for each atom in the molecule being displayed
               atomicNumber = new int[natoms];
               atomicNumber = getAtomicNumber(atomID, natoms);

               // Get bond lengths between bonded atoms in the molecule being displayed
               // Note that this also generates an atom connectivity array
               zmatBondLength = new double[natoms][natoms];

               if (coordFormat.equals("zmat"))
               {
                    zmatBondLength = getZMatBondLength(alGeom, natoms);
               }
               else
               {
                    // Initialize zmatBondLength matrix elements to zero
                    for (j = 0; j < natoms; j++)
                    {
                         for (k = 0; k < natoms; k++)
                         {
                              zmatBondLength[j][k] = 0.0;
                         }
                    }
               }

               // Set default bonding parameter
               if(coordFormat.equals("zmat"))
            	   bondParam = 1.0;
               else
            	   bondParam = ((double)connSlider.getValue())/10.0;

               // Set recomputeConnectivity  
               recomputeConnectivity = false;               

               // Get complete connectivity for this molecule 

               if (natoms > 1)
               {
                    bondLength = new double[natoms][natoms];
                    bondLength = getBondLength(zmatBondLength, natoms, atomCoords, atomicNumber, bondParam);
               }

               // Set default radius for atom
               defaultAtomRadius = 0.35;

               scaledAtomRadius = new double[natoms];
               if(natoms>0){
            	   if (natoms > 1)
            	   {
                    	unscaledAtomRadius = getUnscaledAtomRadius(bondLength, natoms);

                    	scaledAtomRadius = new double[natoms];
                    	scaledAtomRadius = getScaledAtomRadius(atomicNumber, unscaledAtomRadius, natoms);
            	   }
            	   else
               		{
            		   unscaledAtomRadius = defaultAtomRadius;

            		   //scaledAtomRadius[0] = defaultAtomRadius;
               		}
               }
               // Scale atom radii by default
               scaleRadius = 1;       // 0 (unscaled), 1 (scaled), 2 (wireframe)

               // Set default scale factor
               scaleFactor = 1.0;

               // Get color of each atom in the molecule being displayed
               atomColors = new float[3*natoms];
               atomColors = getAtomColors(atomicNumber, natoms);

               // Set bonds to be visible by default (bondsVisible = true)
               bondsVisible = true;

               // Set bonds to be uncolored by default (bondsColored = false)
               bondsColored = false;

               // Set default bond radius
               bondRadius = 0.075;

               bondMat1 = new float[(3*natoms)];
               bondMat2 = new float[(3*natoms)];

               // Initialize angles specifying molecule's rotation
               theta = 0.0;
               phi = 0.0;
               
               // Set origin of coordinate reference frame
               x0 = 0.0;
               y0 = 0.0;
               z0 = 0.0;
               
               // Set distance of coordinate frame origin from camera
               zloc = 7.0;

               x1 = 0.0;
               y1 = 0.0;
 
               x_rot = false;
               y_rot = false;
               z_rot = false;
               
               if(natoms>0){
               // check for consistency of charge and multiplicity
               nuclearCharge = 0;
               
               
               // convert charge and multiplicity to integers
               Integer charge;
               Integer multiplicity;   
               
               for (j = 0; j < natoms; j++)
               {
                    nuclearCharge += atomicNumber[j];
               }

               if ( (chargeStr != "") && (multiplicityStr != "") )
               {
             	  
             	  charge=Integer.parseInt(chargeStr);
                   multiplicity=Integer.parseInt(multiplicityStr);
                   
                   System.out.println("charge:"+chargeStr+" multiplicity:"+multiplicityStr);
                   System.out.println("charge:"+charge+" multiplicity:"+multiplicity);
                   
                    nelectrons = nuclearCharge-charge;

                    System.out.println("nelectrons:"+nelectrons);
                    
                    if ( (nelectrons % 2 == 0) && (multiplicity % 2 == 0) )
                    {
                         CM_consistency = false;
                         

                   // Set corresponding boolean value in interface to false
                    }
                    else 
                 	   if ( (nelectrons % 2 == 1) && (multiplicity % 2 == 1) )
                 	   {
                 		   CM_consistency = false;
                 		   

                          // Set corresponding boolean value in interface to false
                 	   }
                    
               		} 
               }
               return CM_consistency;
          } // end of draw molecule

          public String[] getAtomID(ArrayList alGeom, int natoms)
          {
               int indx;

               Character chr;

               ArrayList listElement;

               StringBuffer sbSymbolID;

               String indexedAtoms;

               String strSymbolID;
               String[] symbolID = new String[natoms];   
 
               String[] atomicSymbol = {"H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg"};

               String[] elementName = {"Hydrogen", "Helium", "Lithium", "Beryllium", "Boron", "Carbon", "Nitrogen", "Oxygen", "Fluorine", "Neon", "Sodium", "Magnesium", "Aluminum", "Silicon", "Phosphorus", "Sulfur", "Chlorine", "Argon", "Potassium", "Calcium", "Scandium", "Titanium", "Vanadium", "Chromium", "Manganese", "Iron", "Cobalt", "Nickel", "Copper", "Zinc", "Gallium", "Germanium", "Arsenic", "Selenium", "Bromine", "Krypton", "Rubidium", "Strontium", "Yttrium", "Zirconium", "Niobium", "Molybdenum", "Technetium", "Ruthenium", "Rhodium", "Palladium", "Silver", "Cadmium", "Indium", "Tin", "Antimony", "Tellurium", "Iodine", "Xenon", "Cesium", "Barium", "Lanthanum", "Cerium", "Praseodymium", "Neodynium", "Promethium", "Samarium", "Europium","Gadolinium", "Terbium", "Dysprosium", "Holmium", "Erbium", "Thulium", "Ytterbium", "Lutetium", "Hafnium", "Tantalum", "Tungsten", "Rhenium", "Osmium", "Iridium", "Platinum", "Gold", "Mercury", "Thallium", "Lead", "Bismuth", "Polonium", "Astatine", "Radon", "Francium", "Radium", "Actinum", "Thorium", "Protactinium", "Uranium", "Neptunium", "Plutonium", "Americium", "Curium", "Berkelium", "Californium", "Einsteinium", "Fermium", "Mendelevium", "Nobelium", "Lawrencium", "Rutherfordium", "Dubnium", "Seaborgium", "Bohrium", "Hassium", "Meitnerium", "Darmstadtium", "Roentgenium"};

               for (j = 0; j < natoms; j++)
               {
                    listElement = new ArrayList();
                    listElement = (ArrayList)(alGeom.get(j));

                    strSymbolID = (listElement.get(0)).toString();

                    sbSymbolID = new StringBuffer();
                    
                    for (k = 0; k < strSymbolID.length(); k++)
                    {
                         chr = new Character(strSymbolID.charAt(k));

                         if (Character.isLetter(chr))
                         {
                              sbSymbolID.append(chr.toString());
                         }
                    }

                    if (sbSymbolID.length() > 2)
                    {
                         for (indx = 0; indx < elementName.length; indx++)
                         {
                              if ( (sbSymbolID.toString()).equalsIgnoreCase(elementName[indx]) )
                              {
                                   strSymbolID = atomicSymbol[indx];

                                   break;
                              }
                         }
                    }
                    else
                    {
                         strSymbolID = sbSymbolID.toString();
                    }

                    symbolID[j] = strSymbolID;
               }

               return symbolID;         
          }

          public String getCoordFormat(ArrayList alGeom, int natoms)
          {
               String geomFormat = new String();

               ArrayList elemList01,elemList02,elemList03,elemList04;

               switch(natoms)
               {
                    case 0: geomFormat = "undefined";
                            break;
                    case 1: elemList01 = new ArrayList();
                            elemList01 = (ArrayList)alGeom.get(0);

                            if (elemList01.size() == 1)
                            {
                                 geomFormat = "zmat";
                            }
                            else if (elemList01.size() > 1)
                            {
                                 geomFormat = "cartesian";
                            }
                            break;

                    case 2: elemList01 = new ArrayList();
                            elemList02 = new ArrayList();

                            elemList01 = (ArrayList)alGeom.get(0);
                            elemList02 = (ArrayList)alGeom.get(1);

                            if ((elemList01.size() == 1) && (elemList02.size() == 3) )
                            {
                                 geomFormat = "zmat";
                            }
                            else if ((elemList01.size() > 1) || (elemList02.size() == 5) )
                            {
                                 geomFormat = "cartesian";
                            }
                            break;

                    case 3: elemList02 = new ArrayList();
                            elemList03 = new ArrayList();

                            elemList02 = (ArrayList)alGeom.get(1);
                            elemList03 = (ArrayList)alGeom.get(2);

                            if ((elemList02.size() == 3) && (elemList03.size() == 5) )
                            {
                                 geomFormat = "zmat";
                            }
                            else if ((elemList02.size() == 5) || (elemList03.size() == 5) )
                            {
                                 geomFormat = "cartesian";
                            }

                            break;

                   default: elemList03 = new ArrayList();
                            elemList04 = new ArrayList();

                            elemList03 = (ArrayList)alGeom.get(2);
                            elemList04 = (ArrayList)alGeom.get(3);

                            if ((elemList03.size() == 5) && (elemList04.size() >= 7) )
                            {
                                 geomFormat = "zmat";
                            }
                            else if ((elemList03.size() == 5) || (elemList04.size() == 5) )
                            {
                                 geomFormat = "cartesian";
                            }

                            break;
               }
               return geomFormat;    
          }

          public double[] getAtomCoords(ArrayList alGeom, int natoms, String coordFormat)
          {
               int bondedAtomIndex, alphaAtomIndex, gammaAtomIndex;
               double bd, dx, dy, dz, dx1, dy1, dz1, dx2, dy2, dz2, r, nx, ny, nz, n, alpha, gamma;
               double[] prerotCoord = new double[3];
               double[] coords = new double[3*natoms];    
               double[] rotCoord = new double[3];    
               double[][] rotMatrix = new double[3][3];    
               ArrayList elemList;
               
               if (coordFormat.equals("cartesian"))
               {
                    for (j = 0; j < natoms; j++)
                    {
                         elemList = new ArrayList(); 
                         elemList = (ArrayList)alGeom.get(j);

                         if (elemList.size() == 2)
                         {
                              for (k = 0; k < 3; k++)
                              {
                                   coords[(3*j + k)] = 0.00;
                              }
                         }
                         else if (elemList.size() == 3)
                         {
                              coords[(3*j)] = Double.parseDouble((elemList.get(2)).toString() );

                              for (k = 1; k < 3; k++)
                              {
                                   coords[(3*j + k)] = 0.00;
                              }
                         }
                         else if (elemList.size() == 4)
                         {
                              coords[(3*j)] = Double.parseDouble((elemList.get(2)).toString() );
                              coords[(3*j) + 1] = Double.parseDouble((elemList.get(3)).toString() );
                              coords[(3*j) + 2] = 0.00; 
                         }
                         else if (elemList.size() == 5)
                         {
                        	 coords[(3*j)] = Double.parseDouble((elemList.get(2)).toString() );
                             coords[(3*j) + 1] = Double.parseDouble((elemList.get(3)).toString() );
                             coords[(3*j) + 2] = -Double.parseDouble((elemList.get(4)).toString() );
                         }
                    }
               }

               if (coordFormat.equals("zmat"))
               {
                    double dx12, dy12, dz12, dx23, dy23, dz23, dx34, dy34, dz34;
                    double r11, r22, r33, R;
                    double rx, ry, rz;
                    double n11, n22, n33;
                    double nx_prime, ny_prime, nz_prime;

                    int atom1Index, atom2Index, atom3Index, atom4Index; 


                    for (index = 0; index < natoms; index++)
                    {
                         elemList = new ArrayList();
       
                         switch(index)
                         {
                              case 0: coords[(3*index)] = 0.0;
                                      coords[(3*index) + 1] = 0.0;
                                      coords[(3*index) + 2] = 0.0;
                                      break;

                              case 1: elemList = (ArrayList)alGeom.get(index);
                                      bd = Double.parseDouble( (elemList.get(2)).toString() );
                                      coords[(3*index)] = 0.0;
                                      coords[(3*index) + 1] = 0.0;
                                      coords[(3*index) + 2] = bd;
                                      break;

                              case 2: elemList = (ArrayList)alGeom.get(index);                          

//////////////////////////
//                      //
//               o 3    //
//              /       //
//             /        //
//   1 o ---- o 2       //
//                      //
//////////////////////////

                                      atom3Index = index;
                                      atom2Index = (Integer.parseInt( (elemList.get(1)).toString() )) - 1;
                                      atom1Index = (Integer.parseInt( (elemList.get(3)).toString() )) - 1;

                                      // Compute difference in coordinates between atom 1 and atom 2, R12
                                      dx12 = coords[(3*atom2Index)] - coords[(3*atom1Index)];
                                      dy12 = coords[(3*atom2Index) + 1] - coords[(3*atom1Index) + 1];
                                      dz12 = coords[(3*atom2Index) + 2] - coords[(3*atom1Index) + 2];

                                      // Compute unit vector along bond from atom 2 to atom 1
                                      r = Math.sqrt( (dx12*dx12) + (dy12*dy12) + (dz12*dz12) );
                                     
                                      nx = -dx12/r;
                                      ny = -dy12/r;
                                      nz = -dz12/r;

                                      // Compute unit vector along axis of rotation (alpha angle) centered on atom 2
                                      n11 = -1.0;
                                      n22 = 0.0;
                                      n33 = 0.0;

                                      // Retrieve alpha rotation angle
                                      alpha = Double.parseDouble( (elemList.get(4)).toString() );

                                      // (1) Convert bond angle (alpha) from degrees to radians
                                      alpha = alpha * (Math.PI/180.0);

                                      // Compute rotation matrix
                                      rotMatrix[0][0] = (n11 * n11) + ( Math.cos(alpha) * (1 - n11 * n11) );
                                      rotMatrix[0][1] = ( (n11 * n22) * (1 - Math.cos(alpha)) ) + (n33 * Math.sin(alpha));
                                      rotMatrix[0][2] = ( (n33 * n11) * (1 - Math.cos(alpha)) ) - (n22 * Math.sin(alpha));

                                      rotMatrix[1][0] = ( (n11 * n22) * (1 - Math.cos(alpha)) ) - (n33 * Math.sin(alpha));
                                      rotMatrix[1][1] = (n22 * n22) + ( Math.cos(alpha) * (1 - n22 * n22) );
                                      rotMatrix[1][2] = ( (n22 * n33) * (1 - Math.cos(alpha)) ) + (n11 * Math.sin(alpha));
 
                                      rotMatrix[2][0] = ( (n33 * n11) * (1 - Math.cos(alpha)) ) + (n22 * Math.sin(alpha));
                                      rotMatrix[2][1] = ( (n22 * n33) * (1 - Math.cos(alpha)) ) - (n11 * Math.sin(alpha));
                                      rotMatrix[2][2] = (n33 * n33) + ( Math.cos(alpha) * (1 - n33 * n33) );
       
                                      // (3) Compute rotated unit vector along bond from atom 2 to atom 3
                                      nx_prime = rotMatrix[0][0] * nx + rotMatrix[0][1] * ny + rotMatrix[0][2] * nz;  
                                      ny_prime = rotMatrix[1][0] * nx + rotMatrix[1][1] * ny + rotMatrix[1][2] * nz;  
                                      nz_prime = rotMatrix[2][0] * nx + rotMatrix[2][1] * ny + rotMatrix[2][2] * nz;  
                                   
                                     // Compute final cartesian coordinates for this atom 
                                      bd = Double.parseDouble( (elemList.get(2)).toString() ); 

                                     coords[(3*index)] = coords[(3*atom2Index)] + (nx_prime * bd);  
                                     coords[(3*index) + 1] = coords[(3*atom2Index) + 1] + (ny_prime * bd);  
                                     coords[(3*index) + 2] = coords[(3*atom2Index) + 2] + (nz_prime * bd);  
                                    
                                     break;

                             default: elemList = (ArrayList)alGeom.get(index);

/////////////////////////////
//                         //
//                  o 4    //
//                 /       //
//                /        //
//      2 o ---- o 3       //
//       /                 //
//      /                  //
//   1 o                   //
//                         //  
/////////////////////////////

                                      atom4Index = index;
                                      atom3Index = (Integer.parseInt( (elemList.get(1)).toString() )) - 1;
                                      atom2Index = (Integer.parseInt( (elemList.get(3)).toString() )) - 1;
                                      atom1Index = (Integer.parseInt( (elemList.get(5)).toString() )) - 1;

                                      // Compute difference in coordinates between atom 1 and atom 2, R12
                                      dx12 = coords[(3*atom2Index)] - coords[(3*atom1Index)];
                                      dy12 = coords[(3*atom2Index) + 1] - coords[(3*atom1Index) + 1];
                                      dz12 = coords[(3*atom2Index) + 2] - coords[(3*atom1Index) + 2];

                                      // Compute difference in coordinates between atom 2 and atom 3, R23
                                      dx23 = coords[(3*atom3Index)] - coords[(3*atom2Index)];
                                      dy23 = coords[(3*atom3Index) + 1] - coords[(3*atom2Index) + 1];
                                      dz23 = coords[(3*atom3Index) + 2] - coords[(3*atom2Index) + 2];
                                      
                                      // Compute cross-product R12 x R23 = [ (dx12, dy12, dz12) X (dx23, dy23, dz23) ]
                                      rx = (dy12 * dz23) - (dy23 * dz12);
                                      ry = (dz12 * dx23) - (dz23 * dx12);
                                      rz = (dx12 * dy23) - (dx23 * dy12);

                                      // Compute unit vector along axis of rotation (alpha angle) normal to plane of atoms 1, 2, and 3 and centered on atom 3
                                      r = Math.sqrt(rx*rx + ry*ry + rz*rz);

                                      n11 = rx/r;
                                      n22 = ry/r;
                                      n33 = rz/r;

                                      // Compute unit vector along bond from atom 3 to atom 2
                                      r = Math.sqrt( (dx23*dx23) + (dy23*dy23) + (dz23*dz23) );
                                     
                                      nx = -dx23/r;
                                      ny = -dy23/r;
                                      nz = -dz23/r;

                                      // Retrieve alpha rotation angle
                                      alpha = Double.parseDouble( (elemList.get(4)).toString() );

                                      // (1) Convert bond angle (alpha) from degrees to radians
                                      alpha = alpha * (Math.PI/180.0);

                                      // Compute rotation matrix
                                      rotMatrix[0][0] = (n11 * n11) + ( Math.cos(alpha) * (1 - n11 * n11) );
                                      rotMatrix[0][1] = ( (n11 * n22) * (1 - Math.cos(alpha)) ) + (n33 * Math.sin(alpha));
                                      rotMatrix[0][2] = ( (n33 * n11) * (1 - Math.cos(alpha)) ) - (n22 * Math.sin(alpha));

                                      rotMatrix[1][0] = ( (n11 * n22) * (1 - Math.cos(alpha)) ) - (n33 * Math.sin(alpha));
                                      rotMatrix[1][1] = (n22 * n22) + ( Math.cos(alpha) * (1 - n22 * n22) );
                                      rotMatrix[1][2] = ( (n22 * n33) * (1 - Math.cos(alpha)) ) + (n11 * Math.sin(alpha));
 
                                      rotMatrix[2][0] = ( (n33 * n11) * (1 - Math.cos(alpha)) ) + (n22 * Math.sin(alpha));
                                      rotMatrix[2][1] = ( (n22 * n33) * (1 - Math.cos(alpha)) ) - (n11 * Math.sin(alpha));
                                      rotMatrix[2][2] = (n33 * n33) + ( Math.cos(alpha) * (1 - n33 * n33) );
       
                                      // (3) Compute rotated unit vector along bond from atom 2 to atom 3
                                      nx_prime = rotMatrix[0][0] * nx + rotMatrix[0][1] * ny + rotMatrix[0][2] * nz;  
                                      ny_prime = rotMatrix[1][0] * nx + rotMatrix[1][1] * ny + rotMatrix[1][2] * nz;  
                                      nz_prime = rotMatrix[2][0] * nx + rotMatrix[2][1] * ny + rotMatrix[2][2] * nz;  
                                   
                                      // Retrieve gamma rotation angle (dihedral angle)
                                      gamma = Double.parseDouble( (elemList.get(6)).toString() );

                                      // (1) Convert dihedral angle (gamma) from degrees to radians
                                      gamma = gamma * (Math.PI/180.0);

                                      // Compute unit vector along axis of rotation (dihedral angle) = unit vector along bond from atom 3 to atom 2
                                      n11 = -dx23/r;
                                      n22 = -dy23/r;
                                      n33 = -dz23/r;

                                      // Compute rotation matrix
                                      rotMatrix[0][0] = (n11 * n11) + ( Math.cos(gamma) * (1 - n11 * n11) );
                                      rotMatrix[0][1] = ( (n11 * n22) * (1 - Math.cos(gamma)) ) + (n33 * Math.sin(gamma));
                                      rotMatrix[0][2] = ( (n33 * n11) * (1 - Math.cos(gamma)) ) - (n22 * Math.sin(gamma));

                                      rotMatrix[1][0] = ( (n11 * n22) * (1 - Math.cos(gamma)) ) - (n33 * Math.sin(gamma));
                                      rotMatrix[1][1] = (n22 * n22) + ( Math.cos(gamma) * (1 - n22 * n22) );
                                      rotMatrix[1][2] = ( (n22 * n33) * (1 - Math.cos(gamma)) ) + (n11 * Math.sin(gamma));
 
                                      rotMatrix[2][0] = ( (n33 * n11) * (1 - Math.cos(gamma)) ) + (n22 * Math.sin(gamma));
                                      rotMatrix[2][1] = ( (n22 * n33) * (1 - Math.cos(gamma)) ) - (n11 * Math.sin(gamma));
                                      rotMatrix[2][2] = (n33 * n33) + ( Math.cos(gamma) * (1 - n33 * n33) );

                                      // Compute unit vector along bond from atom 3 to atom 4
                                      nx = nx_prime;
                                      ny = ny_prime;
                                      nz = nz_prime;

                                      // Compute rotated unit vector along bond from atom 3 to atom 4
                                      nx_prime = rotMatrix[0][0] * nx + rotMatrix[0][1] * ny + rotMatrix[0][2] * nz;  
                                      ny_prime = rotMatrix[1][0] * nx + rotMatrix[1][1] * ny + rotMatrix[1][2] * nz;  
                                      nz_prime = rotMatrix[2][0] * nx + rotMatrix[2][1] * ny + rotMatrix[2][2] * nz;  

                                     // Compute final cartesian coordinates for this atom 
                                      bd = Double.parseDouble( (elemList.get(2)).toString() ); 

                                     coords[(3*index)] = coords[(3*atom3Index)] + (nx_prime * bd);  
                                     coords[(3*index) + 1] = coords[(3*atom3Index) + 1] + (ny_prime * bd);  
                                     coords[(3*index) + 2] = coords[(3*atom3Index) + 2] + (nz_prime * bd);  
                                    
                                     break;
                         }
                    }
               }

               return coords;
          }

          public int[] getAtomicNumber(String[] atomID, int natoms)
          {
               int[] AN = new int[natoms];   
               String[] atomicSymbol = {"H", "He", "Li", "Be", "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S", "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr", "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In", "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd", "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu", "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb", "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U", "Np", "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Rf", "Db", "Sg", "Bh", "Hs", "Mt", "Ds", "Rg"};

               for (j = 0; j < natoms; j++)
               {
                    for (k = 0; k < atomicSymbol.length; k++)
                    {
                         if (atomicSymbol[k].equalsIgnoreCase(atomID[j]))
                         {
                              AN[j] = k + 1;
                              break;
                         }
                    }
               }

               return AN;
          }                   

          public double[][] getZMatBondLength(ArrayList alGeom, int natoms)
          {
               ArrayList listElem;
               int bondedAtomIndex;
               double[][] bondLen;

               bondLen = new double[natoms][natoms];

               for (j = 0; j < natoms; j++)
               {
                    for (k = 0; k < natoms; k++)
                    {
                         bondLen[j][k] = 0.0;
                    }
               }

               for (j = 1; j < natoms; j++)
               {
                    listElem = new ArrayList();
                    listElem = (ArrayList)alGeom.get(j);

                    bondedAtomIndex = (Integer.parseInt( (listElem.get(1)).toString() )) - 1;

                    bondLen[j][bondedAtomIndex] = Double.parseDouble( (listElem.get(2)).toString() );
               }

               return bondLen;
          }

          public double[][] getBondLength(double[][] zmatBondLength, int natoms, double[] atomCoords, int[] atomicNumber, double bondParam)
          {
               double dx, dy, dz, r, bl;    
               double ar1, ar2, cr1, cr2;               

               // VFI atomic radii (from CrystalMaker 1.4.0)
               double[ ] ar  =  { 0.46, 1.22, 1.55, 1.13, 0.91, 0.77, 0.71, 0.48, 0.42, 1.60, 1.89, 1.60, 1.43, 1.34, 1.30, 0.88, 0.79, 1.92, 2.36, 1.97, 1.64, 1.46, 1.34, 1.40, 1.31, 1.26, 1.25, 1.24, 1.28, 1.27, 1.39, 1.39, 1.48, 1.43, 0.94, 1.98, 2.48, 2.15, 1.81, 1.60, 1.45, 1.39, 1.36, 1.34, 1.34, 1.37, 1.44, 1.56, 1.66, 1.58, 1.61, 1.70, 1.15, 2.18, 2.68, 2.21, 1.87, 1.83, 1.82, 1.82, 2.05, 1.81, 2.02, 1.79, 1.77, 1.77, 1.76, 1.75, 1.75, 1.93, 1.74, 1.59, 1.46, 1.40, 1.37, 1.35, 1.35, 1.38, 1.44, 1.60, 1.71, 1.75, 1.82, 1.35, 1.27, 1.20, 1.00, 1.00, 2.03, 1.80, 1.62, 1.53, 1.50, 1.50, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 };
               double[ ] covrad  =  { 0.37, 0.32, 1.34, 0.90, 0.82, 0.77, 0.75, 0.73, 0.71, 0.69, 1.54, 1.30, 1.18, 1.11, 1.06, 1.02, 0.99, 0.97, 1.96, 1.74, 1.44, 1.36, 1.25, 1.27, 1.39, 1.25, 1.26, 1.21, 1.38, 1.31, 1.26, 1.22, 1.19, 1.16, 1.14, 1.10, 2.11, 1.92, 1.62, 1.48, 1.37, 1.45, 1.56, 1.26, 1.35, 1.31, 1.53, 1.48, 1.44, 1.41, 1.38, 1.35, 1.33, 1.30, 2.25, 1.98, 1.69, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.60, 1.50, 1.38, 1.46, 1.59, 1.28, 1.37, 1.28, 1.44, 1.49, 1.48, 1.47, 1.46, 1.00, 1.00, 1.45, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 };         

               double[][] connectivity;
               connectivity = new double[natoms][natoms];

               for (j = 1; j < natoms; j++)
               {
                    for (k = 0; k < j; k++)
                    {
                         connectivity[j][k] = zmatBondLength[j][k];
                    }
               }

               for (j = 1; j < natoms; j++)
               {
                    for (k = 0; k < j; k++)
                    {
                         if (connectivity[j][k] == 0.0)
                         {
                              dx = atomCoords[(3*j)] - atomCoords[(3*k)];
                              dy = atomCoords[(3*j) + 1] - atomCoords[(3*k) + 1];
                              dz = atomCoords[(3*j) + 2] - atomCoords[(3*k) + 2];

                              r = Math.sqrt(dx*dx + dy*dy + dz*dz);

                              ar1 = ar[(atomicNumber[j] - 1)];
                              ar2 = ar[(atomicNumber[k] - 1)];

                              if ( r <= 0.4*bondParam*(ar1 + ar2) )
                              {
                                   connectivity[j][k] = r;
                              }
                         }
                    }
               }

               return connectivity;
          }

          public double getUnscaledAtomRadius(double[][] bondLength, int natoms)
          {
               double unscaledRadius;
               int nbonds = 0;
                 
               for (j = 1; j < natoms; j++)
               {
                    for (k = 0; k < j; k++)
                    {
                         if (bondLength[j][k] > 0.0)
                         {
                              nbonds++;
                         }
                    }
               }

               if (nbonds > 0)
               {
                    double minBondLength = 1000.0;

                    for (j = 1; j < natoms; j++)
                    {
                         for (k = 0; k < j; k++)
                         {
                              if ( (bondLength[j][k] != 0.0) && (bondLength[j][k] < minBondLength) )
                              {
                                   minBondLength = bondLength[j][k];
                              }
                         }
                    }
 
                    unscaledAtomRadius = 0.4*minBondLength;
               }
               else
               {
                    unscaledAtomRadius = defaultAtomRadius;
               }

               return unscaledAtomRadius;
          }

          public double[] getScaledAtomRadius(int[] atomicNumber, double unscaledAtomRadius, int natoms)
          {
               double maxRadius;
               double[] scaledRadius;

               // VFI atomic radii (from CrystalMaker 1.4.0)
               double[ ] ar  =  { 0.46, 1.22, 1.55, 1.13, 0.91, 0.77, 0.71, 0.48, 0.42, 1.60, 1.89, 1.60, 1.43, 1.34, 1.30, 0.88, 0.79, 1.92, 2.36, 1.97, 1.64, 1.46, 1.34, 1.40, 1.31, 1.26, 1.25, 1.24, 1.28, 1.27, 1.39, 1.39, 1.48, 1.43, 0.94, 1.98, 2.48, 2.15, 1.81, 1.60, 1.45, 1.39, 1.36, 1.34, 1.34, 1.37, 1.44, 1.56, 1.66, 1.58, 1.61, 1.70, 1.15, 2.18, 2.68, 2.21, 1.87, 1.83, 1.82, 1.82, 2.05, 1.81, 2.02, 1.79, 1.77, 1.77, 1.76, 1.75, 1.75, 1.93, 1.74, 1.59, 1.46, 1.40, 1.37, 1.35, 1.35, 1.38, 1.44, 1.60, 1.71, 1.75, 1.82, 1.35, 1.27, 1.20, 1.00, 1.00, 2.03, 1.80, 1.62, 1.53, 1.50, 1.50, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 };
               double[ ] covrad  =  { 0.37, 0.32, 1.34, 0.90, 0.82, 0.77, 0.75, 0.73, 0.71, 0.69, 1.54, 1.30, 1.18, 1.11, 1.06, 1.02, 0.99, 0.97, 1.96, 1.74, 1.44, 1.36, 1.25, 1.27, 1.39, 1.25, 1.26, 1.21, 1.38, 1.31, 1.26, 1.22, 1.19, 1.16, 1.14, 1.10, 2.11, 1.92, 1.62, 1.48, 1.37, 1.45, 1.56, 1.26, 1.35, 1.31, 1.53, 1.48, 1.44, 1.41, 1.38, 1.35, 1.33, 1.30, 2.25, 1.98, 1.69, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.60, 1.50, 1.38, 1.46, 1.59, 1.28, 1.37, 1.28, 1.44, 1.49, 1.48, 1.47, 1.46, 1.00, 1.00, 1.45, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 };         

               scaledRadius = new double[natoms];
               maxRadius = ar[atomicNumber[0] - 1];

               for (j = 1; j < natoms; j++)
               {                
//                    if (ar[atomicNumber[j] - 1] > maxRadius)
                    if (covrad[atomicNumber[j] - 1] > maxRadius)
                    {
//                         maxRadius = ar[(atomicNumber[j] - 1)];
                         maxRadius = covrad[(atomicNumber[j] - 1)];
                    }
               }

               for (j = 0; j < natoms; j++)
               {   
//                    scaledRadius[j] = unscaledAtomRadius * (ar[(atomicNumber[j] - 1)]/maxRadius);
                    scaledRadius[j] = unscaledAtomRadius * (covrad[(atomicNumber[j] - 1)]/maxRadius);
               }

               return scaledRadius;
          }
                                 
          public float[] getAtomColors(int[] atomicNumber, int natoms)
          {
               float red, green, blue;
               red = 1.0f; green = 1.0f; blue = 1.0f;

               float[] colors = new float[(3*natoms)];
        
               for (j = 0; j < natoms; j++)
               {
                    switch(atomicNumber[j])
                    {
                         case 1: red = 0.600f;     // H
                                 green = 0.900f;
                                 blue = 0.600f;
                                 break;
                         case 2: red = 0.800f;     // He
                                 green = 0.925f;
                                 blue = 1.000f;
                                 break;
                         case 3: red = 0.800f;     // Li
                                 green = 0.000f;
                                 blue = 1.000f;
                                 break;
                         case 4: red = 0.600f;     // Be
                                 green = 0.400f;
                                 blue = 1.000f;
                                 break;
                         case 5: red = 0.000f;     // B
                                 green = 0.600f;
                                 blue = 0.500f;
                                 break;
                         case 6: red = 1.000f;     // C
                                 green = 1.000f;
                                 blue = 0.7f;
                                 break;
                         case 7: red = 0.000f;     // N
                                 green = 0.650f;
                                 blue = 0.850f;
                                 break;
                         case 8: red = 0.784f;     // O
                                 green = 0.000f;
                                 blue = 0.098f;
                                 break;
                         case 9: red = 1.000f;     // F
                                 green = 0.745f;
                                 blue = 1.000f;
                                 break;
                        case 10: red = 0.800f;     // Ne
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 11: red = 1.000f;     // Na
                                 green = 1.000f;
                                 blue = 0.000f;
                                 break;
                        case 12: red = 0.608f;     // Mg
                                 green = 0.608f;
                                 blue = 0.608f;
                                 break;
                        case 13: red = 0.867f;     // Al
                                 green = 0.867f;
                                 blue = 0.867f;
                                 break;
                        case 14: red = 1.000f;     // Si
                                 green = 1.000f;
                                 blue = 0.588f;
                                 break;
                        case 15: red = 1.000f;     // P
                                 green = 0.000f;
                                 blue = 0.000f;
                                 break;
                        case 16: red = 1.000f;     // S
                                 green = 0.902f;
                                 blue = 0.000f;
                                 break;
                        case 17: red = 0.000f;     // Cl
                                 green = 0.804f;
                                 blue = 0.333f;
                                 break;
                        case 18: red = 0.000f;     // Ar
                                 green = 0.600f;
                                 blue = 0.600f;
                                 break;
                        case 19: red = 0.000f;     // K
                                 green = 0.196f;
                                 blue = 0.765f;
                                 break;
                        case 20: red = 1.000f;     // Ca
                                 green = 1.000f;
                                 blue = 0.800f;
                                 break;
                        case 21: red = 0.000f;     // Sc
                                 green = 1.000f;
                                 blue = 0.000f;
                                 break;
                        case 22: red = 0.000f;     // Ti
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 23: red = 1.000f;     // V
                                 green = 1.000f;
                                 blue = 0.400f;
                                 break;
                        case 24: red = 0.200f;     // Cr
                                 green = 0.800f;
                                 blue = 0.800f;
                                 break;
                        case 25: red = 0.000f;     // Mn
                                 green = 0.800f;
                                 blue = 0.600f;
                                 break;
                        case 26: red = 0.800f;     // Fe
                                 green = 0.000f;
                                 blue = 0.000f;
                                 break;
                        case 27: red = 1.000f;     // Co
                                 green = 0.431f;
                                 blue = 0.647f;
                                 break;
                        case 28: red = 0.000f;     // Ni
                                 green = 0.373f;
                                 blue = 0.980f;
                                 break;
                        case 29: red = 1.000f;     // Cu
                                 green = 0.600f;
                                 blue = 0.000f;
                                 break;
                        case 30: red = 0.000f;     // Zn
                                 green = 0.800f;
                                 blue = 0.800f;
                                 break;
                        case 31: red = 0.000f;     // Ga
                                 green = 0.902f;
                                 blue = 0.000f;
                                 break;
                        case 32: red = 0.490f;     // Ge
                                 green = 0.800f;
                                 blue = 0.800f;
                                 break;
                        case 33: red = 0.000f;     // As
                                 green = 0.600f;
                                 blue = 1.000f;
                                 break;
                        case 34: red = 0.800f;     // Se
                                 green = 0.200f;
                                 blue = 0.039f;
                                 break;
                        case 35: red = 0.706f;     // Br
                                 green = 0.157f;
                                 blue = 0.000f;
                                 break;
                        case 36: red = 0.608f;     // Kr
                                 green = 1.000f;
                                 blue = 0.800f;
                                 break;
                        case 37: red = 0.600f;     // Rb
                                 green = 0.000f;
                                 blue = 0.800f;
                                 break;
                        case 38: red = 0.965f;     // Sr
                                 green = 0.980f;
                                 blue = 0.647f;
                                 break;
                        case 39: red = 0.961f;     // Y
                                 green = 0.510f;
                                 blue = 1.000f;
                                 break;
                        case 40: red = 0.549f;     // Zr
                                 green = 0.800f;
                                 blue = 0.800f;
                                 break;
                        case 41: red = 0.800f;     // Nb
                                 green = 1.000f;
                                 blue = 0.200f;
                                 break;
                        case 42: red = 0.800f;     // Mo
                                 green = 0.600f;
                                 blue = 1.000f;
                                 break;
                        case 43: red = 0.200f;     // Tc
                                 green = 0.400f;
                                 blue = 0.600f;
                                 break;
                        case 44: red = 0.000f;     // Ru
                                 green = 0.824f;
                                 blue = 0.824f;
                                 break;
                        case 45: red = 0.200f;     // Rh
                                 green = 0.400f;
                                 blue = 0.824f;
                                 break;
                        case 46: red = 0.451f;     // Pd
                                 green = 0.451f;
                                 blue = 0.451f;
                                 break;
                        case 47: red = 0.753f;     // Ag
                                 green = 0.753f;
                                 blue = 0.753f;
                                 break;
                        case 48: red = 0.902f;     // Cd
                                 green = 0.784f;
                                 blue = 1.000f;
                                 break;
                        case 49: red = 0.863f;     // In
                                 green = 0.706f;
                                 blue = 0.824f;
                                 break;
                        case 50: red = 0.863f;     // Sn
                                 green = 0.902f;
                                 blue = 0.647f;
                                 break;
                        case 51: red = 0.200f;     // Sb
                                 green = 0.647f;
                                 blue = 0.200f;
                                 break;
                        case 52: red = 0.800f;     // Te
                                 green = 1.000f;
                                 blue = 0.600f;
                                 break;
                        case 53: red = 0.863f;     // I
                                 green = 0.118f;
                                 blue = 0.196f;
                                 break;
                        case 54: red = 0.000f;     // Xe
                                 green = 0.510f;
                                 blue = 0.294f;
                                 break;
                        case 55: red = 1.000f;     // Cs
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 56: red = 1.000f;     // Ba
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 57: red = 1.000f;     // La
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 58: red = 1.000f;     // Ce
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 59: red = 1.000f;     // Pr
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 60: red = 1.000f;     // Nd
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 61: red = 1.000f;     // Pm
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 62: red = 1.000f;     // Sm
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 63: red = 1.000f;     // Eu
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 64: red = 1.000f;     // Gd
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 65: red = 1.000f;     // Tb
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 66: red = 1.000f;     // Dy
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 67: red = 1.000f;     // Ho
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 68: red = 1.000f;     // Er
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 69: red = 1.000f;     // Tm
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 70: red = 1.000f;     // Yb
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 71: red = 1.000f;     // Lu
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 72: red = 1.000f;     // Hf
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 73: red = 1.000f;     // Ta
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 74: red = 1.000f;     // W
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 75: red = 1.000f;     // Re
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 76: red = 1.000f;     // Os
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 77: red = 1.000f;     // Ir
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 78: red = 0.918f;     // Pt
                                 green = 0.918f;
                                 blue = 0.918f;
                                 break;
                        case 79: red = 1.000f;     // Au
                                 green = 0.902f;
                                 blue = 0.000f;
                                 break;
                        case 80: red = 0.918f;     // Hg
                                 green = 0.953f;
                                 blue = 0.914f;
                                 break;
                        case 81: red = 1.000f;     // Tl
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 82: red = 0.941f;     // Pb
                                 green = 0.941f;
                                 blue = 0.941f;
                                 break;
                        case 83: red = 1.000f;     // Bi
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 84: red = 1.000f;     // Po
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 85: red = 1.000f;     // At
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 86: red = 1.000f;     // Rn
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 87: red = 1.000f;     // Fr
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 88: red = 1.000f;     // Ra
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 89: red = 1.000f;     // Ac
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 90: red = 1.000f;     // Th
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 91: red = 1.000f;     // Pa
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 92: red = 1.000f;     // U
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 93: red = 1.000f;     // Np
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 94: red = 1.000f;     // Pu
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 95: red = 1.000f;     // Am
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 96: red = 1.000f;     // Cm
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 97: red = 1.000f;     // Bk
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 98: red = 1.000f;     // Cf
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                        case 99: red = 1.000f;     // Es
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                       case 100: red = 1.000f;     // Fm
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                       case 101: red = 1.000f;     // Md
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                       case 102: red = 1.000f;     // No
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                       case 103: red = 1.000f;     // Lr
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                       case 104: red = 1.000f;     // Rf
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                       case 105: red = 1.000f;     // Db
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                       case 106: red = 1.000f;     // Sg
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                       case 107: red = 1.000f;     // Bh
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                       case 108: red = 1.000f;     // Hs
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                       case 109: red = 1.000f;     // Mt
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                       case 110: red = 1.000f;     // Ds
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                       case 111: red = 1.000f;     // Rg
                                 green = 1.000f;
                                 blue = 1.000f;
                                 break;
                     }

                     colors[3*j] = red;
                     colors[(3*j) + 1] = green;
                     colors[(3*j) + 2] = blue;
               }    

               return colors;
          }

          
          // method definition "display" modified  - 1st parameter changed to GLAutoDrawable from GLDrawable
          //public void display(GLAutoDrawable gldraw)
          public void display(GLDrawable gldraw)          
          {
               double height, width, aspect;   
               double fov, near, far;
               boolean viewMolStructure;
               
               // parameter offsets			// added by Narendra Kumar
               int param_offsets = 0;		// added by Narendra Kumar for "glLightfv" & "glMaterialfv" method call                              

               //Set initial display to true
//               initDisplay = true;
               
               gl = gldraw.getGL();				// method call not available - commented by narendra kumar
               //gl = (GL4bc)gldraw.getGL();			// method call modified - added by narendra kumar
               //gl = gldraw.getGL().getGL4bc();

               glu = gldraw.getGLU();		// commented by Narendra Kumar
               //glu = new GLU();				// added by Narendra Kumar

               // Set up camera
               gl.glMatrixMode(GL.GL_PROJECTION);				// static variable's class call not available - commented by narendra kumar
               //gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);		// static variable's class call modified - added by narendra kumar
               gl.glLoadIdentity();

               // Aspect ratio
               height = ((Component)gldraw).getHeight();
               width = ((Component)gldraw).getWidth();
               aspect = width/height;
   
               fov = 60.0;
               near = 0.01;
               far = 1000.0;

               glu.gluPerspective(fov, aspect, near, far);
 
               gl.glMatrixMode(GL.GL_MODELVIEW);			// commented by narendra kumar
               //gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);	// static variable's class call modified - added by narendra kumar
               gl.glLoadIdentity();

               // Set background color of canvas
               gl.glClearColor(bkg_red, bkg_green, bkg_blue, color_alphaValue);
 
               // Define camera
               glu.gluLookAt(0, 0, 0, 0, 0, 20, 0, 1, 0);
    
               // Set up light(s)
               float position[] = {0.0f, 15.0f, -30.0f, 0.0f};

               float diffuse[] = {0.7f, 0.7f, 0.7f, 0.5f};
               float ambient[] = {0.25f, 0.25f, 0.25f, 0.5f};

               //add 4th parameter to method call "glLightfv" - param_offsets (int) -- Narendra Kumar
               gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position);
               //gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_POSITION, position,param_offsets);
                              
               // add 4th parameter to method call "glLightfv" - param_offsets (int) -- Narendra Kumar
               gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuse);
               //gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_DIFFUSE, diffuse,param_offsets);
                              
               // add 4th parameter to method call "glLightfv" - param_offsets (int) -- Narendra Kumar
               gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient);               
               //gl.glLightfv(GLLightingFunc.GL_LIGHT0, GLLightingFunc.GL_AMBIENT, ambient,param_offsets);
                              
               gl.glEnable(GL.GL_LIGHTING);		
               //gl.glEnable(GLLightingFunc.GL_LIGHTING);		// static variable's class call modified - added by narendra kumar
               
               gl.glEnable(GL.GL_LIGHT0);
               //gl.glEnable(GLLightingFunc.GL_LIGHT0);		// static variable's class call modified - added by narendra kumar
               
               gl.glEnable(GL.GL_DEPTH_TEST);	

               gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

               // Draw 3-D coordinate frame
               float[] x_axismat = {0.7f, 0.2f, 0.2f, 1.0f};
               float[] y_axismat = {0.2f, 0.7f, 0.2f, 1.0f};
               float[] z_axismat = {0.2f, 0.2f, 0.7f, 1.0f};

               gl.glLineWidth(3.5f);

               // added 4th parameter for method call "glMaterialfv" - param_offsets (int) && class call of static variable modified - narendra kumar               
               gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, x_axismat);
               //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, x_axismat, param_offsets);
               gl.glBegin(GL.GL_LINES);
               gl.glVertex3d(0.0, 0.0, 0.0);
               gl.glVertex3d(1.0, 0.0, 0.0);
               gl.glEnd();

               // added 4th parameter for method call "glMaterialfv" - param_offsets (int) && class call of static variable modified - narendra kumar
               gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, y_axismat);
               //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, y_axismat, param_offsets);
               gl.glBegin(GL.GL_LINES);
               gl.glVertex3d(0.0, 0.0, 0.0);
               gl.glVertex3d(0.0, 1.0, 0.0);
               gl.glEnd();

               // added 4th parameter for method call "glMaterialfv" - param_offsets (int) && class call of static variable modified - narendra kumar
               gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, z_axismat);
               //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, z_axismat, param_offsets);
               gl.glBegin(GL.GL_LINES);
               gl.glVertex3d(0.0, 0.0, 0.0);
               gl.glVertex3d(0.0, 0.0, 1.0);
               gl.glEnd();

               gl.glPushMatrix();
               gl.glTranslated(-1.0, -1.0, 0.0);
               gl.glPopMatrix();

               float rgb[] = {0.9f, 0.0f, 0.0f, 0.0f, 0.9f, 0.3f, 0.0f, 0.0f, 0.9f};  

               drawMolecule(gldraw, theta, phi, atomCoords, displayGeom, bondsVisible, bondsColored, scaleRadius, scaleFactor, recomputeConnectivity);
               
               if(initDisplay == true)
               {
            	   bondParam = ((double)connSlider.getValue())/10.0;
            	   recomputeConnectivity = true;
            	   glcanvas.repaint();
            	   initDisplay = false;
               }
          }
 
          // method definition "drawMolecule" modified  - 1st parameter changed to GLAutoDrawable from GLDrawable
          //public void drawMolecule(GLAutoDrawable gldrawable, double theta, double phi, double[] coords, boolean displayGeom, boolean bondsVisible, boolean bondsColored, int scaleRadius, double scaleFactor, boolean recomputeConnectivity)
          public void drawMolecule(GLDrawable gldrawable, double theta, double phi, double[] coords, boolean displayGeom, boolean bondsVisible, boolean bondsColored, int scaleRadius, double scaleFactor, boolean recomputeConnectivity)
          {
        	  gl = gldrawable.getGL();		// method call not available - commented by narendra kumar
        	  //gl = (GL4bc)gldrawable.getGL();	// method call modified - added by narendra kumar

              glu = gldrawable.getGLU();		// commented by Narendra Kumar
              //glu = new GLU();

               double radius =0 ;
               
               // parameter offsets			// added by Narendra Kumar
               int param_offsets = 0;		// added by Narendra Kumar for "glLightfv" & "glMaterialfv" method call                                                                          

               float[] defaultBondMat = {0.9f, 0.9f, 0.9f, 1.0f}; 

               gl.glTranslated(x0, y0, (z0 + zloc));

               gl.glRotated(theta, 0.0, 1.0, 0.0);
               gl.glRotated(phi, 1.0, 0.0, 0.0);
 
               // Draw atoms
               for (j = 0; j < natoms; j++)
               {
                    red = atomColors[(3*j)];
                    green = atomColors[(3*j) + 1];
                    blue = atomColors[(3*j) + 2];

                    material_alphaValue = 1.0f;

                    material = new float[4];

                    material[0] = red;
                    material[1] = green;
                    material[2] = blue;
                    material[3] = material_alphaValue; 

                    // add 4th parameter to method call "glMaterialfv"- param_offsets (int) -- Narendra Kumar
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, material);
                    //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, material,param_offsets);                    

                    gl.glPushMatrix();

                    gl.glTranslated(coords[(3*j)], coords[(3*j) + 1], coords[(3*j) + 2]);
                    
                    if (scaleRadius == 0)
                    {
                         if (scaleFactor*unscaledAtomRadius < bondRadius)
                         {
                              radius = bondRadius;
                         }
                         else
                         {
                              radius = scaleFactor*unscaledAtomRadius;
                         }

                         // commented by narendra kumar - 1st parameter not allowed for method call "glutSolidSphere"                         
                         glut.glutSolidSphere(glu, radius, 100, 360);
                         //glut.glutSolidSphere(radius, 100, 360);                         
                    }
                    else if (scaleRadius == 1)
                    {
                         if (scaleFactor*scaledAtomRadius[j] < bondRadius)
                         {
                              radius = bondRadius;   // don't draw atoms smaller than radius of a bond
                         }
                         else
                         {
                              radius = scaleFactor*scaledAtomRadius[j];
                         }

                         // commented by narendra kumar - 1st parameter not allowed for method call "glutSolidSphere"                         
                         glut.glutSolidSphere(glu, radius, 100, 360);
                         //glut.glutSolidSphere(radius, 100, 360);                         
                    }
                    else if (scaleRadius == 2)
                    {
                        // commented by narendra kumar - 1st parameter not allowed for method call "glutSolidSphere"                         
                        glut.glutSolidSphere(glu, radius, 100, 360);
                    	//glut.glutSolidSphere(radius, 100, 360);                         
                    }

                    gl.glPopMatrix();
               } 

               // Draw bonds
               if ( (bondsVisible == true) && (natoms > 1) )
               {
                    float[] bondmat = {0.9f, 0.9f, 0.9f, 1.0f};
                    // add 4th parameter to method call "glMaterialfv"- param_offsets (int) -- Narendra Kumar                    
                    gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondmat);
                    //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, bondmat,param_offsets);                                        

                    // VFI atomic radii (from CrystalMaker 1.4.0)
                    double[ ] ar  =  { 0.46, 1.22, 1.55, 1.13, 0.91, 0.77, 0.71, 0.48, 0.42, 1.60, 1.89, 1.60, 1.43, 1.34, 1.30, 0.88, 0.79, 1.92, 2.36, 1.97, 1.64, 1.46, 1.34, 1.40, 1.31, 1.26, 1.25, 1.24, 1.28, 1.27, 1.39, 1.39, 1.48, 1.43, 0.94, 1.98, 2.48, 2.15, 1.81, 1.60, 1.45, 1.39, 1.36, 1.34, 1.34, 1.37, 1.44, 1.56, 1.66, 1.58, 1.61, 1.70, 1.15, 2.18, 2.68, 2.21, 1.87, 1.83, 1.82, 1.82, 2.05, 1.81, 2.02, 1.79, 1.77, 1.77, 1.76, 1.75, 1.75, 1.93, 1.74, 1.59, 1.46, 1.40, 1.37, 1.35, 1.35, 1.38, 1.44, 1.60, 1.71, 1.75, 1.82, 1.35, 1.27, 1.20, 1.00, 1.00, 2.03, 1.80, 1.62, 1.53, 1.50, 1.50, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 };
                    double[ ] covrad  =  { 0.37, 0.32, 1.34, 0.90, 0.82, 0.77, 0.75, 0.73, 0.71, 0.69, 1.54, 1.30, 1.18, 1.11, 1.06, 1.02, 0.99, 0.97, 1.96, 1.74, 1.44, 1.36, 1.25, 1.27, 1.39, 1.25, 1.26, 1.21, 1.38, 1.31, 1.26, 1.22, 1.19, 1.16, 1.14, 1.10, 2.11, 1.92, 1.62, 1.48, 1.37, 1.45, 1.56, 1.26, 1.35, 1.31, 1.53, 1.48, 1.44, 1.41, 1.38, 1.35, 1.33, 1.30, 2.25, 1.98, 1.69, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.60, 1.50, 1.38, 1.46, 1.59, 1.28, 1.37, 1.28, 1.44, 1.49, 1.48, 1.47, 1.46, 1.00, 1.00, 1.45, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 };         

                    if (recomputeConnectivity == true)
                    {
                         for (j = 1; j < natoms; j++)
                         {
                              for (k = 0; k < j; k++)
                              {
                                   if (zmatBondLength[j][k] > 0.0)
                                   {
                                        bondLength[j][k] = zmatBondLength[j][k];
                                   }
                                   else
                                   {
                                        bondLength[j][k] = 0.0;
                                   }
                              }
                         }

                         for (j = 1; j < natoms; j++)
                         {
                              for (k = 0; k < j; k++)
                              {
                                   if (bondLength[j][k] == 0.0)
                                   {
                                        dx = atomCoords[(3*j)] - atomCoords[(3*k)];
                                        dy = atomCoords[(3*j) + 1] - atomCoords[(3*k) + 1];
                                        dz = atomCoords[(3*j) + 2] - atomCoords[(3*k) + 2];
     
                                        r = Math.sqrt(dx*dx + dy*dy + dz*dz);

                                        ar1 = ar[(atomicNumber[j] - 1)];
                                        ar2 = ar[(atomicNumber[k] - 1)];
 
                                        if ( r <= 0.3775*bondParam*(ar1 + ar2) )
                                        {
                                             bondLength[j][k] = r;
                                        }
                                   }
                              }
                         }

                         recomputeConnectivity = false;
                    }

                    // Draw cylindrical bonds
                    GLUquadric quadric = glu.gluNewQuadric();

                    double bd, r, r1, r2;
                    double angle, rot_x, rot_y, rot_z;                                        
                    double dx12, dy12, dz12;
                    double nx, ny, nz;
                    double n11, n22, n33;

                    gl.glTranslated(0.0, 0.0, 0.0);

                    for (j = 1; j < natoms; j++) 
                    {
                         for (k = 0; k < j; k++)
                         {   
                              if (bondLength[j][k] > 0.0)
                              { 
                                   bd = bondLength[j][k];

                                   gl.glPushMatrix();

                                   gl.glTranslated(coords[(3*k)], coords[(3*k) + 1], coords[(3*k) + 2]);
 
                                   dx12 = coords[(3*j)] - coords[(3*k)];
                                   dy12 = coords[(3*j) + 1] - coords[(3*k) + 1];
                                   dz12 = coords[(3*j) + 2] - coords[(3*k) + 2];
        
                                   r = Math.sqrt(dx12*dx12 + dy12*dy12);

                                   if (r > 0.0)
                                   {
                                        n11 = -dy12/r;
                                        n22 = dx12/r;
                                        n33 = 0.0;

                                        if (dz12 >= 0.0)
                                        {
                                             angle = (180.0/Math.PI)*Math.acos(Math.abs(dz12)/bd);
                                        }
                                        else     
                                        {
                                             angle = 180.0 - (180.0/Math.PI)*Math.acos(Math.abs(dz12)/bd);
                                        }
                                   }
                                   else
                                   {
                                        n11 = 0.0;
                                        n22 = 0.0;
                                        n33 = 1.0;

                                        if (dz12 >= 0.0)
                                        {
                                             angle = 0.0;
                                        }
                                        else     
                                        {
                                             angle = 180.0;
                                        }
                                   }

                                   if (bondsColored == false)
                                   {
                                        gl.glRotated(angle, n11, n22, n33);
 
                                        // add 4th parameter to method call "glMaterialfv"- param_offsets (int) -- Narendra Kumar                                        
                                        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, defaultBondMat);                                        
                                        //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, defaultBondMat,param_offsets);                                           
                                        glu.gluCylinder(quadric, bondRadius, bondRadius, bd, 20, 20); 
                                   }
                                   else
                                   {
                                        bondMat1[0] = atomColors[(3*k)];
                                        bondMat1[1] = atomColors[(3*k) + 1];
                                        bondMat1[2] = atomColors[(3*k) + 2];
                                        bondMat1[3] = material_alphaValue;

                                        bondMat2[0] = atomColors[(3*j)];
                                        bondMat2[1] = atomColors[(3*j) + 1];
                                        bondMat2[2] = atomColors[(3*j) + 2];
                                        bondMat2[3] = material_alphaValue;   

                                        gl.glRotated(angle, n11, n22, n33);

                                        // add 4th parameter to method call "glMaterialfv"- param_offsets (int) -- Narendra Kumar                                        
                                        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat1);                                        
                                        //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, bondMat1,param_offsets);                                                                                   
                                        glu.gluCylinder(quadric, bondRadius, bondRadius, bd/2, 20, 20);

                                        gl.glRotated(-angle, n11, n22, n33);
                                        gl.glTranslated(dx12/2, dy12/2, dz12/2);
                                        gl.glRotated(angle, n11, n22, n33);
  
                                        // add 4th parameter to method call "glMaterialfv"- param_offsets (int) -- Narendra Kumar                                        
                                        gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE, bondMat2);                                        
                                        //gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GLLightingFunc.GL_AMBIENT_AND_DIFFUSE, bondMat2,param_offsets);                                          

                                        glu.gluCylinder(quadric, bondRadius, bondRadius, bd/2, 20, 20);
                                   }

                                   gl.glPopMatrix();
                              }    
                         }               
                    }             
               }
          }

          public void mouseDragged(MouseEvent e)
          {
               axisSelectionDimen = 0.02;

               x2 = e.getX();
               y2 = e.getY();

               x_rot = true;

               if ( ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) && (x_rot == true) )
               {
                    if ( (x2 - x1) > 0 )
                    {
                         theta += 15.0;
                    }
                    else if ( (x2 - x1)< 0 )
                    {
                         theta -= 15.0;
                    }

                    x1 = x2;

                    if ( (y2 - y1) > 0 )
                    {
                         phi += 5.0;
                    }
                    else if ( (y2 - y1)< 0 )
                    {
                         phi -= 5.0;
                    }

                    y1 = y2;
               } 
               else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)
               {
                    if ( (y2 - y1) > 0 )
                    {
                         zloc -= 0.25;
                    }
                    else if ( (y2 - y1)< 0 )
                    {
                         zloc += 0.25;
                    }
 
                    y1 = y2;
               }     

/*               // Hide any popup mens that are visible
               filePopupMenu.setVisible(false);
               editPopupMenu.setVisible(false);
               viewPopupMenu.setVisible(false);
               helpPopupMenu.setVisible(false);

               // Hide any submenus that are visible
               displayOptionsPopupSubmenu.setVisible(false);
               showBondsPopupSubmenu.setVisible(false);
               displayMOPopupSubmenu.setVisible(false);
*/
               glcanvas.repaint();
          }

          public void mouseMoved(MouseEvent e)
          {
          }

          public void reshape(GLDrawable gldraw, int x, int y, int width, int height)
          {
          }

          public void displayChanged(GLDrawable gldraw, boolean modeChanged, boolean deviceChanged)
          {
          }
          
     }

     class CanvasMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
/*               // Hide any popup mens that are visible
               filePopupMenu.setVisible(false);
               editPopupMenu.setVisible(false);
               viewPopupMenu.setVisible(false);
               helpPopupMenu.setVisible(false);

               // Hide any submenus that are visible
               displayOptionsPopupSubmenu.setVisible(false);
               showBondsPopupSubmenu.setVisible(false);
               displayMOPopupSubmenu.setVisible(false);
*/
               // Determine number of clicks
               if (((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) && (e.getClickCount() == 2))
               {
               }
          }
     }

     class CanvasKeyListener extends KeyAdapter
     {
          public void keyPressed(KeyEvent e)
          {
               if (e.getKeyCode() == KeyEvent.VK_LEFT)
               {
                     x0 += 0.2;
                     glcanvas.repaint();
               }
               else if (e.getKeyCode() == KeyEvent.VK_R)
               {
                    theta += 2.0;
                    glcanvas.repaint();
               }
               else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
               {
                     x0 -= 0.2;
                     glcanvas.repaint();  
               }
               else if (e.getKeyCode() == KeyEvent.VK_L)
               {
                    theta -= 2.0;
                    glcanvas.repaint();
               }
               else if (e.getKeyCode() == KeyEvent.VK_UP)
               {
                     y0 += 0.2;
                     glcanvas.repaint();
               }
               else if (e.getKeyCode() == KeyEvent.VK_U)
               {
                    phi += 2.0;
                    glcanvas.repaint();
               }               
               else if (e.getKeyCode() == KeyEvent.VK_DOWN)
               {
                     y0 -= 0.2;
                     glcanvas.repaint();
               }
               else if (e.getKeyCode() == KeyEvent.VK_D)
               {
                    phi -= 2.0;
                    glcanvas.repaint();
               }

               /*// Hide all popup menus that are visible
               filePopupMenu.setVisible(false);
               editPopupMenu.setVisible(false);
               viewPopupMenu.setVisible(false);
               helpPopupMenu.setVisible(false);

               // Hide all popup submenus that are visible
               displayOptionsPopupSubmenu.setVisible(false);
               showBondsPopupSubmenu.setVisible(false);
               displayMOPopupSubmenu.setVisible(false);*/    
          }

          public void keyReleased(KeyEvent e)
          {
          }

          public void keyTyped(KeyEvent e)
          {
          }
     }
     /*     
     class menuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = (int)(0.5*mbWidth);
               mby = (int)(0.95*mbHeight);

               // Hide any submenus that are visible
               displayOptionsPopupSubmenu.setVisible(false);
               showBondsPopupSubmenu.setVisible(false);
               displayMOPopupSubmenu.setVisible(false);      

               if ((e.getComponent()).equals(fileMenu))
               {
                    editPopupMenu.setVisible(false);
                    viewPopupMenu.setVisible(false);
                    helpPopupMenu.setVisible(false);
                    connectivityAdjustmentFrame.setVisible(false);
                    radiusScalingFrame.setVisible(false);

                    if (filePopupMenu.isVisible() == false)
                    {
                         filePopupMenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         filePopupMenu.setVisible(false);
                    }          
               }
               else if ((e.getComponent()).equals(editMenu))
               {
                    filePopupMenu.setVisible(false);
                    viewPopupMenu.setVisible(false);
                    helpPopupMenu.setVisible(false);
                    connectivityAdjustmentFrame.setVisible(false);
                    radiusScalingFrame.setVisible(false);

                    if (editPopupMenu.isVisible() == false)
                    {
                         editPopupMenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         editPopupMenu.setVisible(false);
                    }          
               }
               else if ((e.getComponent()).equals(viewMenu))
               {
                    filePopupMenu.setVisible(false);
                    editPopupMenu.setVisible(false);
                    helpPopupMenu.setVisible(false);
                    connectivityAdjustmentFrame.setVisible(false);
                    radiusScalingFrame.setVisible(false);

                    if (viewPopupMenu.isVisible() == false)
                    {
                         viewPopupMenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         viewPopupMenu.setVisible(false);
                    }          
               }
               else if ((e.getComponent()).equals(helpMenu))
               {
                    filePopupMenu.setVisible(false);
                    editPopupMenu.setVisible(false);
                    viewPopupMenu.setVisible(false);
                    connectivityAdjustmentFrame.setVisible(false);
                    radiusScalingFrame.setVisible(false);

                    if (helpPopupMenu.isVisible() == false)
                    {
                         helpPopupMenu.show(e.getComponent(), mbx, mby);
                    }                    
                    else 
                    {
                         helpPopupMenu.setVisible(false);
                    }          
               }
          }
     }

     class submenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(0.5*mbHeight);
          }
     }

     class displayOptionsSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(0.5*mbHeight);

               if (displayOptionsSubmenu.contains(e.getX(), e.getY()))
               {
                    if (displayOptionsPopupSubmenu.isVisible() == false)
                    {
                         displayOptionsPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }
                    else
                    {
                         if (displayOptionsPopupSubmenu.isVisible() == true)
                         {
                              displayOptionsPopupSubmenu.setVisible(false);
                         }
                    }       

                    displayMOPopupSubmenu.setVisible(false);
                    showBondsPopupSubmenu.setVisible(false);
               }
          }
     }      
   */
     //class displayOptionMouseListener extends MouseAdapter
     class displayOptionMouseListener implements ActionListener
     {
//          public void mouseClicked(MouseEvent e)
    	  public void actionPerformed(ActionEvent e)    	 
          {
               //if ((e.getComponent()).equals(displayOption2))         // ball & stick (scaled radii)
    		   if ((e.getSource()).equals(displayOption2))         // ball & stick (scaled radii)
               {
                    radiusSlider.setValue(100);

                    scaleFactor = 1.0;
                    bondsVisible = true;
                    bondsColored = false;
                    scaleRadius = 1;
//                    viewPopupMenu.setVisible(false);
               }

               //if ((e.getComponent()).equals(displayOption3))         // ball & stick (unscaled radii)
    		   if ((e.getSource()).equals(displayOption3))         // ball & stick (unscaled radii)
               {
                    radiusSlider.setValue(100);

                    scaleFactor = 1.0;
                    bondsVisible = true;
                    bondsColored = false;
                    scaleRadius = 0;
//                    viewPopupMenu.setVisible(false);
               }
               //else if ((e.getComponent()).equals(displayOption4))    // wireframe
    		   else if ((e.getSource()).equals(displayOption4))    // wireframe
               {
                    scaleFactor = 1.0;
                    bondsVisible = true;
                    bondsColored = true;
                    scaleRadius = 2;
               }
               //else if ((e.getComponent()).equals(displayOption5))    // space filling
    		   else if ((e.getSource()).equals(displayOption5))    // space filling
               {
//                    radiusSlider.setValue((int)(100*scaleFactor));
                   radiusSlider.setValue(100);

                   scaleFactor = 1.0;
                   bondsVisible = true;
                   bondsColored = false;
                   scaleRadius = 1;

                    // Get maximum bond length
                    int J= 0, K = 0;
                    double maxBondLength = 0.0;

                    for (j = 1; j < natoms; j++)
                    {
                         for (k = 0; k < j; k++)
                         {
                              if (bondLength[j][k] > maxBondLength)
                              {
                                   maxBondLength = bondLength[j][k];
                                   J = j;
                                   K = k;
                              }
                         }
                    }

                    scaleFactor = (1.40 * maxBondLength)/(scaledAtomRadius[J] + scaledAtomRadius[K]);

//                    viewPopupMenu.setVisible(false);

                    glcanvas.repaint();
               }
               //else if ((e.getComponent()).equals(displayOption6))    // modify atom radius
    		   else if ((e.getSource()).equals(displayOption6))    // modify atom radius
               {
                    mbHeight = GamessGUI.frame.getHeight();
                    mbWidth = GamessGUI.frame.getWidth();

                    mbx = (int)((mbWidth/2)-(radiusScalingFrame.getWidth()/2));
                    mby = (int)((mbHeight/5)-(radiusScalingFrame.getHeight()/2));

                    Point P = GamessGUI.frame.getLocationOnScreen();
                    int Px = (int)P.getX();
                    int Py = (int)P.getY();
                    
                    radiusScalingFrame.setLocation(Px + mbx, Py + mby);

                    if (radiusScalingFrame.isVisible() == false)
                    {
                         radiusScalingFrame.setVisible(true);

//                         viewPopupMenu.setVisible(false);
                    }
                    else
                    {
                         radiusScalingFrame.setVisible(false);
                    }
               }
               //else if ((e.getComponent()).equals(displayOption7))    // modify connectivity
    		   else if ((e.getSource()).equals(displayOption7))    // modify connectivity
               {
            	   
                    mbHeight = (int)GamessGUI.frame.getHeight();
                    mbWidth = (int)GamessGUI.frame.getWidth();

                    mbx = (int)((mbWidth/2)-(connectivityAdjustmentFrame.getWidth()/2));
                    mby = (int)((mbHeight/5)-(connectivityAdjustmentFrame.getHeight()/2));

                    Point P = GamessGUI.frame.getLocationOnScreen();
                    int Px = (int)P.getX();
                    int Py = (int)P.getY();

                    connectivityAdjustmentFrame.setLocation(Px + mbx, Py + mby);

                    if (connectivityAdjustmentFrame.isVisible() == false)
                    {
                         connectivityAdjustmentFrame.setVisible(true);

//                         viewPopupMenu.setVisible(false);
                    }
                    else
                    {
                         connectivityAdjustmentFrame.setVisible(false);
                    }
               }
               //else if ((e.getComponent()).equals(displayOption8))    // label atoms
    		   else if ((e.getSource()).equals(displayOption8))    // label atoms
               {
               }

/*               // Hide all popup submenus that are visible
               displayOptionsPopupSubmenu.setVisible(false);      
               showBondsPopupSubmenu.setVisible(false);
               displayMOPopupSubmenu.setVisible(false);      
*/
               glcanvas.repaint();
          }
     }

//     class showBondsSubmenuMouseListener extends MouseAdapter
     class showBondsSubmenuMouseListener implements ActionListener
     {
//          public void mouseClicked(MouseEvent e)
    	  public void actionPerformed(ActionEvent e)
          {
/*               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(0.5*mbHeight);

               if (showBondsSubmenu.contains(e.getX(), e.getY()))
               {
               */
                    if (bondsVisible == false)
                    {
                         bondsVisible = true;
 
                        glcanvas.repaint();
                    }

/*                    if (showBondsPopupSubmenu.isVisible() == false)
                    {
                         showBondsPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }
                    else
                    {
                         if (showBondsPopupSubmenu.isVisible() == true)
                         {
                              showBondsPopupSubmenu.setVisible(false);
                         }
                    }       

                    displayOptionsPopupSubmenu.setVisible(false);
                    displayMOPopupSubmenu.setVisible(false);*/
//               }
          }
     }      

//     class colorBondsMouseListener extends MouseAdapter
     class colorBondsMouseListener implements ActionListener
     {
//          public void mouseClicked(MouseEvent e)
          public void actionPerformed(ActionEvent e)
          {
               if (bondsColored == false)
               {
                    bondsColored = true;

                    glcanvas.repaint();
               }

/*               // Hide all popup submenus that are visible
               displayOptionsPopupSubmenu.setVisible(false);
               showBondsPopupSubmenu.setVisible(false);
               displayMOPopupSubmenu.setVisible(false);*/      
          }
     }

//     class uncolorBondsMouseListener extends MouseAdapter
     class uncolorBondsMouseListener implements ActionListener
     {
//          public void mouseClicked(MouseEvent e)
    	  public void actionPerformed(ActionEvent e)
          {
               if (bondsColored == true)
               {
                    bondsColored = false;

                    glcanvas.repaint();
               }

/*               // Hide all popup submenus that are visible
               displayOptionsPopupSubmenu.setVisible(false);
               showBondsPopupSubmenu.setVisible(false);
               displayMOPopupSubmenu.setVisible(false); */      
          }
     }

//     class hideBondsMouseListener extends MouseAdapter
     class hideBondsMouseListener implements ActionListener
     {
          //public void mouseClicked(MouseEvent e)
    	  public void actionPerformed(ActionEvent e)
          {
               if (bondsVisible == true)
               {
                   bondsVisible = false;
 
                   glcanvas.repaint();
               }

/*               // Hide all popup submenus that are visible
               displayOptionsPopupSubmenu.setVisible(false);
               showBondsPopupSubmenu.setVisible(false);
               displayMOPopupSubmenu.setVisible(false);*/      
          }
     }
/*
     class displayMOSubmenuMouseListener extends MouseAdapter
     {
          public void mouseClicked(MouseEvent e)
          {
               mbHeight = (e.getComponent()).getHeight();
               mbWidth = (e.getComponent()).getWidth();

               mbx = mbWidth;
               mby = (int)(0.5*mbHeight);

               if (displayMOSubmenu.contains(e.getX(), e.getY()))
               {
                    if (displayMOPopupSubmenu.isVisible() == false)
                    {
                         displayMOPopupSubmenu.show(e.getComponent(), mbx, mby);
                    }
                    else
                    {
                         if (displayMOPopupSubmenu.isVisible() == true)
                         {
                              displayMOPopupSubmenu.setVisible(false);
                         }
                    }       

                    displayOptionsPopupSubmenu.setVisible(false);
                    showBondsPopupSubmenu.setVisible(false);
               }
          }
     }      
*/
//     class animationSubmenuMouseListener extends MouseAdapter
     class animationSubmenuMouseListener implements ActionListener
     {
          //public void mouseClicked(MouseEvent e)
    	  public void actionPerformed(ActionEvent e)
          {
/*               showBondsPopupSubmenu.setVisible(false);
               displayOptionsPopupSubmenu.setVisible(false);
               displayMOPopupSubmenu.setVisible(false); */
          }
     }

//     class outputdataSubmenuMouseListener extends MouseAdapter
     class outputdataSubmenuMouseListener implements ActionListener
     {
          //public void mouseClicked(MouseEvent e)
    	  public void actionPerformed(ActionEvent e)
          {
/*               showBondsPopupSubmenu.setVisible(false);
               displayOptionsPopupSubmenu.setVisible(false);
               displayMOPopupSubmenu.setVisible(false);*/
          }
     }

     class SliderListener implements ChangeListener
     {
          public void stateChanged(ChangeEvent e)
          { 
               JSlider jslider = (JSlider)e.getSource();

               if (jslider.equals(radiusSlider))
               {
                    scaleFactor = ((double)radiusSlider.getValue())/100.0;

                    glcanvas.repaint();
               }
               else if (jslider.equals(connSlider))
               {
                    bondParam = ((double)connSlider.getValue())/10.0;

                    recomputeConnectivity = true;

                    glcanvas.repaint();
               }
          }
     }

     class DoneButtonListener implements ActionListener
     {
          public void actionPerformed(ActionEvent e)
          { 
               JButton jbutton = (JButton)e.getSource();

               if (jbutton.equals(radiusDoneButton))
               {
                    radiusScalingFrame.setVisible(false);
               }
               else if (jbutton.equals(connDoneButton))
               {
                    recomputeConnectivity = false;

                    connectivityAdjustmentFrame.setVisible(false);
               }
          }
     }
}
