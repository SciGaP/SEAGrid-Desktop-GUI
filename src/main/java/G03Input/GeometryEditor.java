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
 * Created on Sep 12, 2005
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
import java.math.*;
import java.lang.Math;

public class GeometryEditor extends JFrame
{
    public static String geom = new String();
    public static String validateGeom;
    public static String newGeom;
    GeometryReader geomReader;
    public static boolean readchk, readallchk, counterpoise;

    public GeometryEditor(){}
    public GeometryEditor(String geometryString )      // Constructor
    {
        geomReader = new GeometryReader(geometryString);
        atomCoordinateParser.getCoordinates();
        

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

    // Define GeometryReader class
public static  class GeometryReader
    { public static String[] atomSymb;
    	public static  double[] atomCoord;
        GeometryReader(String geometryString)      // Constructor
        {
          String str, strAtomID, strToken;
            StringBuffer sb, atomicSymbol;
            StringTokenizer stok01, stok1, stok2, stok3, stok4,removeExpo;
            ArrayList<String> nlineTokens, ncoordTokens, atomID; 
            ArrayList<String> geomLines, tmpgeomLine, geomTokens, geomCoordTokens, geomVarTokens, geomVariables;

            int i, j, k, ndx, index, index0, index1, charIndex, atomIndex, coordIndex, varIndex, coordTokenIndex;
            int bondedAtomIndex, alphaAtomIndex, gammaAtomIndex;
            int bondedAtomCoordIndex, alphaAtomCoordIndex, gammaAtomCoordIndex;
            int natoms, ncoord, nvarTokens, ntokensRead;
            int ngeomLines, ngeomVariables, ngeomTokens;
            int nelem, nchar, sblength;
            int[] ntokens;
            int[][] bondOrder;

            double bondDistance, alpha, gamma, x1, x2, y1, y2, z1, z2, r, nx, ny, nz;
           
            double[] prerotCoord;
            double[] rotCoord;
            double[] opTranslation = { 0.0, 0.0, 0.0 };
            double[][] rotMatrix;

            boolean[] formatZCartesian; 
            boolean finalCoordRead, foundChar;
            boolean formatCartesian;

            sb = new StringBuffer();
            atomicSymbol = new StringBuffer();

            rotCoord = new double[3];
            prerotCoord = new double[3]; 

            geomLines = new ArrayList<String>();
            geomTokens = new ArrayList<String>();
            tmpgeomLine = new ArrayList<String>();
            geomCoordTokens = new ArrayList<String>();
            geomVarTokens = new ArrayList<String>();
            geomVariables = new ArrayList<String>();

            ncoordTokens = new ArrayList<String>();

            counterpoise = false;
            readchk = false;
//          readchk = infile.readchk;
            readallchk = false;
//          readallchk = infile.readallchk;  
            finalCoordRead = false;
            formatCartesian = false;

            natoms = 0;
            nelem = 0;
            ntokensRead = 0;
            coordIndex = 0;
            bondedAtomIndex = 0;
            alphaAtomIndex = 0;
            gammaAtomIndex = 0;
            bondDistance = 0.0;
            alpha = 0.0;
            gamma = 0.0;           
           geom=geometryString;
           System.out.println("Geometry Specification"+geometryString);
           stok01 = new StringTokenizer(geom, "\n");
            
            

            
            newGeom = new String();
            String expo = new String();
            String expo1 = new String();
            while(stok01.hasMoreTokens())
            {
            	 expo = stok01.nextToken();
            	
            	 removeExpo = new StringTokenizer(expo);
            	 System.out.println("No of tokens on each line is"+removeExpo.countTokens());
            	 while(removeExpo.hasMoreTokens())
            	 {
            		 expo1 = removeExpo.nextToken();
            	
            		 int indexCount = expo1.indexOf("E");
            	//	 indexCount = expo1.indexOf("e");
            		 if(indexCount > 0)
            		 {
            			 newGeom += 0.0+" ";
            		 }
            		 else
            		 {
            			 newGeom += expo1+" ";
            		 }
            	 }
            	 newGeom += "\n";
            }
            
            
            
     stok1 = new StringTokenizer(newGeom,"\n");
            System.out.println("No of tokens"+stok1.countTokens());
            // Count total number of individual lines contained in geometry specification
            ngeomLines = stok1.countTokens();

            // Specify length of 'ntokens' integer array
            ntokens = new int[ngeomLines];

            // Set initial capacity of 'zmatLines' array list equal to total number
            // of lines in geometry specification 
            geomLines = new ArrayList<String>(ngeomLines);
                      
            while(stok1.hasMoreTokens())
            {
                geomLines.add(stok1.nextToken());
            }

            // Initialize 'formatCartesian' array
            formatZCartesian = new boolean[ngeomLines];

            for (j = 0; j < ngeomLines; j++)
            {
                formatZCartesian[j] = false;
            }

            // Set initial capacity of 'nlineTokens' array list equal to total number 
            // of lines in geometry specification
            nlineTokens = new ArrayList<String>(ngeomLines);

            // Separately tokenize each line of geometry specification
            // Initialize total number of tokens in geometry specification
            ngeomTokens = 0;

            // Set number of variables specified in geometry specification equal to zero
            ngeomVariables = 0;

            for (index = 0; index < ngeomLines; index++)
            {
                sb.delete(0, sb.length());
                sb.append(geomLines.get(index).toString());

                stok2 = new StringTokenizer(sb.toString());

                // Count number of tokens in each individual line of geometry specification
                // and place in 'nlineTokens' array list
                ntokens[index] = stok2.countTokens();

                // Set number of atoms equal to number of lines in geometry specification 
                // if there is no 'Variables' section in the Z-matrix
                if ((index == (ngeomLines - 1)) && (finalCoordRead == false))   // if reach end of geometry specification
                {                                                               // before final coordinate has been read  
                    natoms = ngeomLines;   // each line in geometry specification represents an individual atom in the molecule

                    finalCoordRead = true;
                }
                else if ((index > 1) && (ntokens[index] < 4))
                {
                    // Read variables section of geometry specification, if this section is present

                    if (finalCoordRead == false)
                    {
                      natoms = index;
                      finalCoordRead = true;
                    }

                    ndx = sb.indexOf("=");

                    if ((ntokens[index] == 1) && (ndx != -1))
                    {
                        geomVarTokens.add(sb.substring(0, ndx));
                        geomVarTokens.add(sb.substring(ndx+1));

                        ngeomVariables++;
                    }
                    
                    else if (ntokens[index] == 2)      
                    {
                        while (stok2.hasMoreTokens())
                        {     
                            geomVarTokens.add(stok2.nextToken());
                        }

                        ngeomVariables++;
                    }     
                    else if ((ntokens[index] == 3)&& (ndx != -1))
                    {
                        while (stok2.hasMoreTokens())
                        {
                            str = new String(stok2.nextToken());

                            if (!str.equals("="))
                            {
                                geomVarTokens.add(stok2.nextToken());
                            }
                        }

                        ngeomVariables++;
                    }  
                    if ((ntokens[index] == 1) && (ndx == -1))
                    {
                        // additional data unrelated to Z-matrix proper  
                    }
                }     
            }     

            ncoord = 3 * natoms;
            atomCoord = new double[ncoord];
            atomID = new ArrayList<String>(natoms);
            atomSymb = new String[natoms];

            // Initialize bond order matrix
            bondOrder = new int[natoms][natoms];

            for (j = 0; j < natoms; j++)
            {
                for (k = 0; k < natoms; k++)
                {
                    bondOrder[j][k] = 0;
                }
            }    

            // Currently have number of tokens present in each line of geometry specification, each separate tokens in the
            // coordinates section for the molecule, and each separate token in the variables section for the molecule

            // Read in each separate token in the geometry specification grouped according to number of tokens in each 
            // line of the coordinate section for the molecule

            for (atomIndex = 0; atomIndex < natoms; atomIndex++)
            {
                stok3 = new StringTokenizer(geomLines.get(atomIndex).toString());

                // Remove fragment numbers specification if run incorporates counterpoise corrections
                if (ntokens[atomIndex] <= 7)
                {
                    nelem = ntokens[atomIndex];
                }
                else if (ntokens[atomIndex] > 7)
                {
                    nelem = 7;
                }

                /*************
                *
                *  Note:  Must access counterpoise variable from InfileReader class:  'counterpoise = infile.counterpoise'
                *                                          // if ((nelem == 4) && (infile.counterpoise == false))
                *************/
//                if ((nelem == 4) && (infile.counterpoise == false))
                if ((nelem == 4) && (counterpoise == false))
                {
                    formatCartesian = true;
                }

                for (index = 0; index < nelem; index++)
                {
                    strToken = stok3.nextToken();

                    // Get number of characters present in this token
                    nchar = strToken.length();

                    switch(index)
                    {
                        case 0:  
                            // Read in first atom from line of molecular specification 
                                atomID.add(strToken);

                            for (j = 0; j < nchar; j++)
                            {
                                if (Character.isLetter(strToken.charAt(j)))
                                {
                                    atomicSymbol.append(strToken.charAt(j));
                                }
                            }

                            atomSymb[atomIndex] = new String(atomicSymbol.toString());

                            // Clear contents of string buffer
                            atomicSymbol.delete(0, atomicSymbol.length());

                            // If this is first atom in geometry specification, place 
                            // it at the origin of Cartesian coordinate frame
                            if ((atomIndex == 0) && (ntokens[atomIndex] == 1))
                            {
                                for (coordIndex = 0; coordIndex < 3; coordIndex++)
                                {
                                    atomCoord[coordIndex] = 0.0;
                                }
                            }
                            break;

                        case 1:
                            // If geometry format is given as a Z-matrix, read in second (bonded) atom
                            // from line of Z-matrix                             

                            // If, instead, geometry format is given in Cartesian coordinates, read in x-coordinate
                            if (formatCartesian == false)
                            {
                                // Check if second token is a variable or a numerical value
                                foundChar = false;

                                for (charIndex = 0; charIndex < nchar; charIndex++)
                                {
                                    if (Character.isLetter(strToken.charAt(charIndex)))
                                    {
                                         foundChar = true;
                                    }
                                }    

                                if (foundChar == false)
                                {
                                    // Check if this line of geometry specification is given in
                                    // Cartesian coordinate format
                                    if (Integer.parseInt(strToken) == 0)
                                    {
                                        formatZCartesian[atomIndex] = true;
                                    }
                                    else
                                    {
                                        // An integer is being used to identify the atom to which this atom is bonded  
                                        bondedAtomIndex = Integer.parseInt(strToken) - 1;
                                    }
                                }
                                else
                                {
                                    bondedAtomIndex = atomID.indexOf(strToken);
                                }    

                                bondOrder[bondedAtomIndex][atomIndex] = 1;
                            }
                            else    // If, instead, geometry format is given in Cartesian coordinates, read in
                            {       // x-coordinate directly from geometry specification  
                                // Read in x-coordinate of atom 

                                coordIndex = 3 * atomIndex;

                                // Check if token is a variable or a double value
                                foundChar = false;

                                for (charIndex = 0; charIndex < nchar; charIndex++)
                                {
                                    if (Character.isLetter(strToken.charAt(charIndex)))
                                    {
                                         foundChar = true;
                                    }
                                }

                                if (foundChar == true)
                                {
                                    // Retrieve index of x-coordinate variable in 'zmatVarTokens' array list
                                    varIndex = geomVariables.indexOf(strToken);

                                    // Retrieve value of x-coordinate from 'zmatVarTokens' array list
                                    atomCoord[coordIndex + (index - 1)] = Double.parseDouble(geomVarTokens.get(varIndex + 1).toString());
                                }
                                else
                                {
                                    // Read in value of x-coordinate directly from current line of Z-matrix
                                    atomCoord[coordIndex + (index - 1)] = Double.parseDouble(strToken);
                                }
                            }
                            break;

                        case 2:
                            // If geometry format is given as a Z-matrix, read in second (bonded) atom
                            // from line of Z-matrix
                            if (formatCartesian == false)
                            {
                                if (formatZCartesian[atomIndex] == true)
                                {
                                    coordIndex = 3 * atomIndex;
                               
                                    // Check if token is a variable or a double value
                                    foundChar = false;
 
                                    for (charIndex = 0; charIndex < nchar; charIndex++)
                                    {
                                        if (Character.isLetter(strToken.charAt(charIndex)))
                                        {
                                             foundChar = true;
                                        }
                                    }

                                    if (foundChar == true)
                                    {
                                        // Retrieve index of x-coordinate variable in 'zmatVarTokens' array list 
                                        varIndex = geomVariables.indexOf(strToken);

                                        // Retrieve value of x-coordinate from 'zmatVarTokens' array list
                                        atomCoord[coordIndex] = Double.parseDouble(geomVarTokens.get(varIndex + 1).toString());
                                    }
                                    else
                                    {
                                        // Read in value of x-coordinate directly from current line of Z-matrix
                                        atomCoord[coordIndex] = Double.parseDouble(strToken);
                                    }
                                }    
                                else                         // Third token in this line of Z-matrix is a bond distance
                                {
                                    // Read in bond distance directly from current line of Z-matrix
                                    bondDistance = Double.parseDouble(strToken);
  
                                    // If this is the second atom in the Z-matrix, place atom along the z-axis a distance
                                    // 'bondDistance' from the first atom
                                    if (nelem == 3)
                                    {
                                        bondedAtomCoordIndex = 3 * bondedAtomIndex;
   
                                        atomCoord[coordIndex] = atomCoord[bondedAtomCoordIndex];
                                        atomCoord[coordIndex + 1] = atomCoord[bondedAtomCoordIndex + 1];
                                        atomCoord[coordIndex + 2] = atomCoord[bondedAtomCoordIndex + 2] + bondDistance;
                                    }
                                }
                            }
                            else     // If, instead, geometry format is given in Cartesian coordinates, read in
                            {        // y-coordinate directly from geometry specification
                                // Read in y-coordinate of atom

                                coordIndex = 3 * atomIndex;

                                // Check if token is a variable or a double value
                                foundChar = false;

                                for (charIndex = 0; charIndex < nchar; charIndex++)
                                {
                                    if (Character.isLetter(strToken.charAt(charIndex)))
                                    {
                                         foundChar = true;
                                    }
                                }

                                if (foundChar == true)
                                {
                                    // Retrieve index of y-coordinate variable in 'zmatVarTokens' array list
                                    varIndex = geomVariables.indexOf(strToken);

                                    // Retrieve value of y-coordinate from 'zmatVarTokens' array list
                                    atomCoord[coordIndex + (index - 1)] = Double.parseDouble(geomVarTokens.get(varIndex + 1).toString());
                                }
                                else
                                {
                                    // Read in value of y-coordinate directly from current line of geometry specification
                                	System.out.println("Check for Expo"+strToken);
                                    atomCoord[coordIndex + (index - 1)] = Double.parseDouble(strToken);
                                }
                            }
                            break;

                        case 3:
                            // If current line of Z-matrix is in Cartesian format, read in y-coordinate
                            // for this atom directly from current line of Z-matrix
                            if (formatCartesian == false)
                            {
                                if (formatZCartesian[atomIndex] == true)
                                {
                                    coordIndex = (3 * atomIndex) + 1;
  
                                    // Check if token is a variable or a double value
                                    foundChar = false;
 
                                    for (charIndex = 0; charIndex < nchar; charIndex++)
                                    {
                                        if (Character.isLetter(strToken.charAt(charIndex)))
                                        {
                                             foundChar = true;
                                        }
                                    }
   
                                    if (foundChar == true)
                                    {
                                        // Retrieve index of y-coordinate variable in 'zmatVarTokens' array list
                                        varIndex = geomVariables.indexOf(strToken);
 
                                        // Retrieve value of y-coordinate from 'zmatVarTokens' array list
                                        atomCoord[coordIndex] = Double.parseDouble(geomVarTokens.get(varIndex + 1).toString());
                                    }
                                    else
                                    {
                                        // Read in value of y-coordinate directly from current line of Z-matrix
                                        atomCoord[coordIndex] = Double.parseDouble(strToken);
                                    } 
                                }
                                else                         // Fourth token in this line of Z-matrix is a third atom
                                {
                                    // Read in this third (alpha) atom directly from current line of Z-matrix
                                    // Check if fourth token is a variable or a numerical value
                                    foundChar = false;
    
                                    for (charIndex = 0; charIndex < nchar; charIndex++)
                                    {
                                        if (Character.isLetter(strToken.charAt(charIndex)))
                                        {
                                             foundChar = true;
                                        }
                                    }
  
                                    if (foundChar == true)
                                    {
                                        alphaAtomIndex = atomID.indexOf(strToken);
                                    }
                                    else
                                    {
                                        // An integer is being used to identify the atom to which this atom is bonded
                                        alphaAtomIndex = Integer.parseInt(strToken) - 1;
                                    }
                                }       
                            }
                            else          // If, instead, geometry format is given in Cartesian coordinates, read in
                            {             // z-coordinate directly from geometry specification
                                // Read in z-coordinate of atom

                                coordIndex = 3 * atomIndex;

                                // Check if token is a variable or a double value
                                foundChar = false;

                                for (charIndex = 0; charIndex < nchar; charIndex++)
                                {
                                    if (Character.isLetter(strToken.charAt(charIndex)))
                                    {
                                         foundChar = true;
                                    }
                                }

                                if (foundChar == true)
                                {
                                    // Retrieve index of z-coordinate variable in 'zmatVarTokens' array list
                                    varIndex = geomVariables.indexOf(strToken);

                                    // Retrieve value of z-coordinate from 'zmatVarTokens' array list
                                    atomCoord[coordIndex + (index - 1)] = Double.parseDouble(geomVarTokens.get(varIndex + 1).toString());
                                }
                                else
                                {
                                    // Read in value of z-coordinate directly from current line of Z-matrix
                                    atomCoord[coordIndex + (index - 1)] = Double.parseDouble(strToken);
                                }
                            }
                            break;

                        case 4:
                            // If current line of Z-matrix is in Cartesian format, read in z-coordinate
                            // for this atom directly from current line of Z-matrix
                            if (formatZCartesian[atomIndex] == true)
                            {
                                coordIndex = (3 * atomIndex) + 2;
 
                                // Check if token is a variable or a double value
                                foundChar = false;
 
                                for (charIndex = 0; charIndex < nchar; charIndex++)
                                {
                                    if (Character.isLetter(strToken.charAt(charIndex)))
                                    {
                                         foundChar = true;
                                    }
                                }
 
                                if (foundChar == true)
                                {
                                    // Retrieve index of z-coordinate variable in 'zmatVarTokens' array list
                                    varIndex = geomVariables.indexOf(strToken);
  
                                    // Retrieve value of z-coordinate from 'zmatVarTokens' array list
                                    atomCoord[coordIndex] = Double.parseDouble(geomVarTokens.get(varIndex + 1).toString());
                                }
                                else
                                {
                                    // Read in value of z-coordinate directly from current line of Z-matrix
                                    atomCoord[coordIndex] = Double.parseDouble(strToken);
                                }
                            }
                            else                         // Fifth token in this line of Z-matrix is a bond angle
                            {
                                // Read in bond angle from current line of Z-matrix
                                // Check if fifth token is a variable or a numerical value
                                foundChar = false;
 
                                for (charIndex = 0; charIndex < nchar; charIndex++)
                                {
                                    if (Character.isLetter(strToken.charAt(charIndex)))
                                    {
                                         foundChar = true;
                                    }
                                }
 
                                if (foundChar == true)
                                {
                                    // Retrieve index of bond angle variable in 'zmatVarTokens' array list
                                    varIndex = geomVariables.indexOf(strToken);
  
                                    // Retrieve value of bond angle from 'zmatVarTokens' array list
                                    alpha = Double.parseDouble(geomVarTokens.get(varIndex + 1).toString());
                                }
                                else
                                {
                                    // Read bond angle directly from this line of the Z-matrix
                                    alpha = Double.parseDouble(strToken);
                                } 
                            }     
                            break;
 
                        case 5:
                            // Check if sixth token is a variable or a numerical value
                            foundChar = false;
   
                            for (charIndex = 0; charIndex < nchar; charIndex++)
                            {
                                if (Character.isLetter(strToken.charAt(charIndex)))
                                {
                                     foundChar = true;
                                }
                            }
  
                            if (foundChar == true)
                            {
                                gammaAtomIndex = atomID.indexOf(strToken);
                            }
                            else
                            {
                                // An integer is being used to identify the atom to which this atom is bonded
                                gammaAtomIndex = Integer.parseInt(strToken) - 1;
                            }
                            break;

                        case 6:
                            coordIndex = 3 * atomIndex;
                            // Read in dihedral angle from current line of Z-matrix
                            // Check if fifth token is a variable or a numerical value
                            foundChar = false;
 
                            for (charIndex = 0; charIndex < nchar; charIndex++)
                            {
                                if (Character.isLetter(strToken.charAt(charIndex)))
                                {
                                     foundChar = true;
                                }
                            }
  
                            if (foundChar == true)
                            {
                                // Retrieve index of dihedral angle variable in 'zmatVarTokens' array list
                                varIndex = geomVariables.indexOf(strToken); 

                                // Retrieve value of dihedral angle from 'zmatVarTokens' array list
                                gamma = Double.parseDouble(geomVarTokens.get(varIndex + 1).toString());
                            }
                            else
                            {
                                // Read bond angle directly from this line of the Z-matrix
                                gamma = Double.parseDouble(strToken);
                            }
                            break;
                    }
                }  

                switch(nelem)
                {
                    case 5:
                        if (formatZCartesian[atomIndex] == false)
                        {
                            coordIndex = 3 * atomIndex;

                            // Get unit vectors (nx, ny, and nz) along bond from bonded atom (atom2) to alpha atom (atom1)
                            alphaAtomCoordIndex = 3 * alphaAtomIndex;
                            bondedAtomCoordIndex = 3 * bondedAtomIndex;

                            x1 = atomCoord[alphaAtomCoordIndex] - atomCoord[bondedAtomCoordIndex];
                            y1 = atomCoord[alphaAtomCoordIndex + 1] - atomCoord[bondedAtomCoordIndex + 1];
                            z1 = atomCoord[alphaAtomCoordIndex + 2] - atomCoord[bondedAtomCoordIndex + 2];

                            // Compute normalization factor
                            r = Math.sqrt( (x1 * x1) + (y1 * y1) + (z1 * z1) ); 

                            // Compute unit vectors along bond
                            nx = x1/r;
                            ny = y1/r;
                            nz = z1/r;

                            // Get coordinates of alpha atom relative to axis of rotation before rotation 
                            prerotCoord[0] = nx * bondDistance;
                            prerotCoord[1] = ny * bondDistance;
                            prerotCoord[2] = nz * bondDistance;

                            // Get unit vectors (nx, ny, and nz) along axis of bond angle rotation through bonded atom
                            // Rotate about an axis perpendicular to the z-axis and the direction of the bond

                            // Compute unit vectors along rotation axis
                            if (nz == 1.0)
                            {
                                nx = -1.0;     // rotate counterclockwise about rotation axis
                                ny = 0.0;
                                nz = 0.0;
                            }
                            else if (nz == -1.0)
                            {
                                nx = 1.0;
                                ny = 0.0;
                                nz = 0.0;
                            }
                            else
                            {
                                nx = -y1/r;
                                ny = x1/r;
                                nz = 0.0;
                            }

                            // Rotate coordinates counterclockwise by 'alpha' radians about axis of rotation
                            // Convert bond angle from degrees to radians
                            alpha = alpha * (Math.PI/180);

                            // Compute rotation matrix
                            rotMatrix = getRotMatrix(alpha, nx, ny, nz);

                            // Compute rotated coordinates
                            rotCoord = getRotatedCoordinates(rotMatrix, prerotCoord);

                            // Compute final atomic coordinates for this atom
                            for (j = 0; j < 3; j++)
                            {
                                atomCoord[coordIndex + j] = atomCoord[bondedAtomCoordIndex + j] + rotCoord[j]; 
                            }
                        }    
                        break;

                    case 7:
                        coordIndex = 3 * atomIndex;

                        // Get unit vectors (nx, ny, and nz) along bond from bonded atom (atom3) to alpha atom (atom2)
                        gammaAtomCoordIndex = 3 * gammaAtomIndex;
                        alphaAtomCoordIndex = 3 * alphaAtomIndex;
                        bondedAtomCoordIndex = 3 * bondedAtomIndex;

                        x1 = atomCoord[alphaAtomCoordIndex] - atomCoord[bondedAtomCoordIndex];
                        y1 = atomCoord[alphaAtomCoordIndex + 1] - atomCoord[bondedAtomCoordIndex + 1];
                        z1 = atomCoord[alphaAtomCoordIndex + 2] - atomCoord[bondedAtomCoordIndex + 2];

                        if (bondOrder[gammaAtomIndex][bondedAtomIndex] > 0)
                        {
                            rotCoord[0] = atomCoord[gammaAtomIndex];
                            rotCoord[1] = atomCoord[gammaAtomIndex + 1];
                            rotCoord[2] = atomCoord[gammaAtomIndex + 2];
                        }
                        else
                        { 
                            // Compute normalization factor
                            r = Math.sqrt( (x1 * x1) + (y1 * y1) + (z1 * z1) );
 
                            nx = x1/r;
                            ny = y1/r;
                            nz = z1/r;
 
                            // Get coordinates of alpha atom relative to axis of rotation before rotation
                            prerotCoord[0] = nx * bondDistance;
                            prerotCoord[1] = ny * bondDistance;
                            prerotCoord[2] = nz * bondDistance;
 
                            // Get unit vectors (nx, ny, and nz) along axis of bond angle rotation through bonded atom
                         
                            // Compute direction numbers for bond vector between gamma atom (atom 1)
                            // and alpha atom (atom 2) to point from alpha atom to bonded atom
                            x2 = atomCoord[gammaAtomCoordIndex] - atomCoord[alphaAtomCoordIndex];
                            y2 = atomCoord[gammaAtomCoordIndex + 1] - atomCoord[alphaAtomCoordIndex + 1];
                            z2 = atomCoord[gammaAtomCoordIndex + 2] - atomCoord[alphaAtomCoordIndex + 2];

                            // Compute direction numbers for rotation axis normal to the plane containing the gamma (atom 1),
                            // alpha (atom 2), and bonded atoms (atom 3) as cross-product of bond vectors: (r2) x (r1) 
                            nx = (y2 * z1) - (y1 * z2);
                            ny = (x1 * z2) - (x2 * z1);
                            nz = (x2 * y1) - (x1 * y2);
                         
                            // Compute normalization factor
                            r = Math.sqrt( (nx * nx) + (ny * ny) + (nz * nz) );
 
                            // Compute unit vectors along axis of rotation
                            nx = nx/r;
                            ny = ny/r;
                            nz = nz/r;

                            // Rotate coordinates counterclockwise by 'alpha' radians about axis of rotation
                            // Convert bond angle from degrees to radians
                            alpha = alpha * (Math.PI/180);

                            // Compute rotation matrix
                            rotMatrix = getRotMatrix(alpha, nx, ny, nz);

                            // Compute rotated coordinates
                            rotCoord = getRotatedCoordinates(rotMatrix, prerotCoord);
                        }

                        // If dihedral angle is not zero, rotate bond counterclockwise about an axis defined
                        // by bond vector pointing from alpha atom (atom 2) to bonded atom (atom 3).
                        // This is the dihedral rotation axis
                        if (gamma != 0)
                        {
                            // Set pre-rotated coordinates equal to coordinates rotated through the bond angle alpha
                            for (j = 0; j < 3; j++)
                            {
                                prerotCoord[j] = rotCoord[j];
                            }

                            // Direction numbers along bond between alpha atom and bonded atom are x1, y1, and z1

                            // Compute normalization factor
                            r = Math.sqrt( (x1 * x1) + (y1 * y1) + (z1 * z1) );    

                            // Compute unit vectors along dihedral angle rotation axis
                            nx = x1/r;
                            ny = y1/r;
                            nz = z1/r; 

                            // Rotate coordinates counterclockwise by 'gamma' radians about axis of rotation
                            // Convert dihedral angle from degrees to radians
                            gamma = gamma * (Math.PI/180);

                            // Compute rotation matrix
                            rotMatrix = getRotMatrix(gamma, nx, ny, nz);

                            // Compute rotated coordinates
                            rotCoord = getRotatedCoordinates(rotMatrix, prerotCoord);
                        }

                        // Compute final atomic coordinates for this atom
                        for (j = 0; j < 3; j++)
                        {
                            atomCoord[coordIndex + j] = atomCoord[bondedAtomCoordIndex + j] + rotCoord[j];
                        }
                        break;
                }
            }    

///////
            validateGeom = "";
            validateGeom += InputfileReader.charge+" "+InputfileReader.spinmult+"\n";
            
            //Eliminate "E-16" for too low values 
            
            for(int p=0;p<3*natoms;p++)
            	
            {	
            	String exponentialCheck = String.valueOf(atomCoord[p]);
            	int indexNo = exponentialCheck.indexOf("E");
            	System.out.println("In the function"+ indexNo);
            	System.out.println("In the function"+atomCoord[p]);
            	
            	if(indexNo>0)
            	{
            		atomCoord[p] = 0.0;
            	}
            	
            }
            
            
            
            
            
            System.out.println(" ");
            System.out.println("The coordinates of the atoms are:");
            for (j = 0; j < natoms; j++)
            {
                k = 3 * j;

                System.out.print("atom " + atomSymb[j] + ": " + atomCoord[k] + ", " + atomCoord[k + 1] + ", " + atomCoord[k + 2]);
                validateGeom += atomSymb[j]+" "+ atomCoord[k] + " " + atomCoord[k+1] + " " + atomCoord[k+2]+ "\n" ;
            }
///////            
System.out.println("The constructed structure is"+validateGeom);
        }    
  
        public double[][] getRotMatrix(double angle, double nx, double ny, double nz) 
        {
            double[][] rotMatrix = new double[3][3];
 
            // Rotation matrix for counterclockwise rotation about axis where the unit vectors
            // along the direction of the axis are nx, ny, and nz
            rotMatrix[0][0] = (nx * nx) + ( Math.cos(angle) * (1 - nx * nx) );
            rotMatrix[0][1] = ( (nx * ny) * (1 - Math.cos(angle)) ) + (nz * Math.sin(angle));
            rotMatrix[0][2] = ( (nz * nx) * (1 - Math.cos(angle)) ) - (ny * Math.sin(angle));

            rotMatrix[1][0] = ( (nx * ny) * (1 - Math.cos(angle)) ) - (nz * Math.sin(angle));
            rotMatrix[1][1] = (ny * ny) + ( Math.cos(angle) * (1 - ny * ny) );
            rotMatrix[1][2] = ( (ny * nz) * (1 - Math.cos(angle)) ) + (nx * Math.sin(angle));
 
            rotMatrix[2][0] = ( (nz * nx) * (1 - Math.cos(angle)) ) + (ny * Math.sin(angle));
            rotMatrix[2][1] = ( (ny * nz) * (1 - Math.cos(angle)) ) - (nx * Math.sin(angle));
            rotMatrix[2][2] = (nz * nz) + ( Math.cos(angle) * (1 - nz * nz) );
             
            return rotMatrix;
        }
                
        public double[] getRotatedCoordinates(double[][] rotMatrix, double[] prerotCoord)
        {
            double[] rotCoord = new double[3];

            // Rotate coordinates counterclockwise by the dihedral angle (in radians) about axis of rotation
            for (int j = 0; j < 3; j++)
            {
                rotCoord[j] = 0.0;

                for(int k = 0; k < 3; k++)
                {
                    rotCoord[j] += rotMatrix[j][k] * prerotCoord[k];
                }
            }

            return rotCoord;
        }    
    }

    public static void main(String[] args)
    {
        new GeometryEditor();
    } 
}   

