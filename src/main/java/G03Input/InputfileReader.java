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
 * Created on Sep 15, 2005
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

public class InputfileReader extends JFrame
{
    public static int nproc, mbRequested, charge, spinmult;
    String chkfileName = new String(); 
    public static String route = new String(); 
    String title = new String();
    public static String geom = new String();
    public static String chrgStr,mulStr;
    boolean readchk = false;
    boolean readallchk = false;

    InfileReader infile;

    public InputfileReader()      // Constructor
    {
        infile = new InfileReader();

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

    // Define readZmatrix class
    class InfileReader
    {
        InfileReader()      // Constructor
        {
            int i, j, k, index;

//////
            StringBuffer sbInfile = new StringBuffer();
          /*  sbInfile.append("%nproc=8\n");
            sbInfile.append("%mem=2500MB\n");
            sbInfile.append("%chk=ZnO_wurtzite\n");
            sbInfile.append("#HF/6-31G(d) Opt=ModRedundant\n");
//            sbInfile.append("#HF/6-31G(d) Opt=ModRedundant Geom=Check\n");
//            sbInfile.append("#HF/6-31G(d) Opt=ModRedundant Geom=AllCheck\n");
            sbInfile.append("\n");
            sbInfile.append("Optimization of wurtzite crystal structure\n");
            sbInfile.append("\n");
            sbInfile.append("0  1\n");
 /*           sbInfile.append("C1\n");
            sbInfile.append("C2  C1  1.34\n");
            sbInfile.append("C3  C2  1.52  C1  120.0 \n");
            sbInfile.append("H1  C1  1.09  C2  120.0  C3  0.0\n");
            sbInfile.append("H2  C1  1.09  C2  120.0  C3  180.0\n");
            sbInfile.append("H3  C2  1.09  C1  120.0  C3  180.0\n"); 
            sbInfile.append("H4  C3  1.09  C2  109.5  C1  180.0\n");
            sbInfile.append("H5  C3  1.09  C2  109.5  C1  60.0\n");
            sbInfile.append("H6  C3  1.09  C2  109.5  C1  -60.0\n");
            sbInfile.append("\n");
            
            */
        /*    sbInfile.append("C 0.07707692307692304 -0.13453846153846155 -0.41484615384615386"+"\n"+
            		"C -1.1679230769230768 0.7184615384615385 -0.19184615384615383"+"\n"+
					"N 0.11807692307692305 -1.2305384615384616 0.6011538461538461");      */ 
//////
        
//          Note: 'strInfile' will be read in directly from JTextArea instead of from the above StringBuffer 'sbInfile' 
           
            String strInfile="";
            strInfile = new String(InputFile.tempinput.toString().trim());// + "\n" + "\n");
           // strInfile = new String(sbInfile.toString().trim());// + "\n" + "\n");
            String substr, strInfileLine, strToken, strRouteToken; 

            String[] strInputFile;
            String[] routeTokens;

            StringBuffer sbMem = new StringBuffer();
            StringBuffer sbMemUnits = new StringBuffer();
            StringBuffer sbQSM = new StringBuffer();
            StringBuffer sbGeom = new StringBuffer();

            StringTokenizer stok;

            ArrayList<String> inputFileTokens = new ArrayList<String>();
            ArrayList<String> blankLineIndex = new ArrayList<String>();

            double memconvfactor = 0.0;

            int nblankLines, nInfileTokens, nInfileLines, nrouteTokens;
            int chrIndex, tokenIndex, geomIndex;
            int nblanklinesRead = 0;

            int[] lineNumbers;
            int[] newlineIndex;
            int[] blanklineTerminatedLineNumbers;

            boolean foundRoute = false;
	    boolean foundTitle = false;
            boolean foundQSM = false;
            boolean foundGeom = false;
            boolean counterpoise = false;

            // Tokenize input file on newline only as delimiter and return delimiter (number
            // of consecutive newline characters will count number of blank lines) 
            stok = new StringTokenizer(strInfile, "\n", true);

            // Determine total number of tokens in input file
             nInfileTokens = stok.countTokens();

            lineNumbers = new int[nInfileTokens];
            newlineIndex = new int[nInfileTokens];

            // Initialize newlineIndex
            for (j = 0; j < nInfileTokens; j++)
            {
                newlineIndex[j] = 0;
            }

            while (stok.hasMoreTokens())
            {
                strToken = new String(stok.nextToken());
                
                inputFileTokens.add(strToken);
            }

            index = 0;
            nblankLines = 0;

            for (j = 1; j < nInfileTokens; j++)
            {
                 if (inputFileTokens.get(j).toString().equals("\n"))
                 {
                     newlineIndex[index] = j; 

                     if (index > 0)
                     {
                         if (((newlineIndex[index] - newlineIndex[index - 1]) == 1) && (newlineIndex[index] != 0))
                         {
                             nblankLines++;
                         }
                     }    

                     index++;
                 }
            }

            blanklineTerminatedLineNumbers = new int[nblankLines];

            k = 0;

            for (j = 1; j < nInfileTokens; j++)
            {
                if (((newlineIndex[j] - newlineIndex[j - 1]) == 1) && (newlineIndex[j] != 0))    
                {
                    blanklineTerminatedLineNumbers[k] = (newlineIndex[j] - k)/2;

                    k++;
                }
            }    

            // Tokenize input file a second time using newline only as delimiter but do not return newline delimiter
            stok = new StringTokenizer(strInfile, "\n");

            // Determine total number of tokens in input file
             nInfileLines = stok.countTokens();

            // Read infile tokens into the string array 'strInputFile'
            strInputFile = new String[nInfileLines];

            index = 0;

            while (stok.hasMoreTokens())
            {
                strInputFile[index] = new String(stok.nextToken());

                index++;
            }

            for (index = 0; index < nInfileLines; index++)
            {
                if (Character.toString(strInputFile[index].charAt(0)).equals("%"))
                {
                    if ((strInputFile[index].substring(1, 6)).equals("nproc"))
                    {
                        substr = new String(strInputFile[index].substring((strInputFile[index].indexOf("=") + 1)));

                        nproc = Integer.parseInt(substr);
                    }
                    else if ((strInputFile[index].substring(1, 4)).equals("mem")) 
                    {
                        substr = new String(strInputFile[index].substring((strInputFile[index].indexOf("=") + 1)));

                        for (k = 0; k < substr.length(); k++)
                        {
                            if (Character.isDigit(substr.charAt(k)))
                            {
                                sbMem.append(Character.toString(substr.charAt(k)));
                            }
                            else if (Character.isLetter(substr.charAt(k)))  
                            {
                                sbMemUnits.append(Character.toString(substr.charAt(k)));
                            }
                        }

                        
                        if ((sbMemUnits.toString()).equalsIgnoreCase("mb"))
                        {
                            memconvfactor = 1.0;
                        }
                        else if ((sbMemUnits.toString()).equalsIgnoreCase("gb"))
                        {
                            memconvfactor = 1000.0;
                        }
                        else if ((sbMemUnits.toString()).equalsIgnoreCase("kb"))
                        {
                            memconvfactor = 0.001;
                        }
                        else if ((sbMemUnits.toString()).equalsIgnoreCase("mw"))
                        {
                            memconvfactor = 8.0;
                        }
                        else if ((sbMemUnits.toString()).equalsIgnoreCase("gw"))
                        {
                            memconvfactor = 8000.0;
                        }
                        else if ((sbMemUnits.toString()).equalsIgnoreCase("kw"))
                        {
                            memconvfactor = 0.008;
                        }
 
                        mbRequested = (int)(memconvfactor * Integer.parseInt(sbMem.toString()));
                    }
                }
                else if (Character.toString(strInputFile[index].charAt(0)).equals("#"))
                {
                    route = new String(strInputFile[index]);

                    foundRoute = true;
                    nblanklinesRead++;

                    // Tokenize route section of input file
                    stok = new StringTokenizer(route);

                    nrouteTokens = stok.countTokens();
                    routeTokens = new String[nrouteTokens];
  
                    tokenIndex = 0;                   
 
                    while (stok.hasMoreTokens())
                    {
                        routeTokens[tokenIndex] = stok.nextToken();
 
                        tokenIndex++;
                    }
 
                    for (tokenIndex = 0; tokenIndex < nrouteTokens; tokenIndex++)
                    {
                        strRouteToken = new String(routeTokens[tokenIndex].toLowerCase());
  
                        if (strRouteToken.indexOf("geom") != -1)
                        {
                            if (strRouteToken.indexOf("check") != -1)
                            {
                                if (strRouteToken.indexOf("allcheck") != -1)
                                {
                                    readchk = true;
                                    foundTitle = true;
                                    foundQSM = true;
                                    foundGeom = true;
                                }
                                else
                                {
                                    readallchk = true;
                                    foundGeom = true;
                                }
                            }
                        }          
                        if (strRouteToken.indexOf("counterpoise") != -1)
                        {
                            counterpoise = true;
                        }
                    }      
                }
                else if ((foundRoute == true) && (foundTitle == false))
                {
                    title = new String(strInputFile[index]);

                    foundTitle = true;
                    nblanklinesRead++;
                }
                else if ((foundTitle == true) && (foundQSM == false))
                {
                    sbQSM.append(strInputFile[index]);

                    while (sbQSM.indexOf(",") != -1)
                    {
                        k = sbQSM.indexOf(",");

                        sbQSM.replace(k, k+1, " ");
                    }

                    stok = new StringTokenizer(sbQSM.toString());

                    // Retrieve first two tokens - charge on molecule and spin multiplicity
                    
                    try {
						chrgStr=stok.nextToken();
						charge = Integer.parseInt(chrgStr);
						
						System.out.println("Charge"+chrgStr+"><");
						//charge = Integer.parseInt(stok.nextToken());
						mulStr=stok.nextToken();
						System.out.println("Mul"+mulStr+"><");
						spinmult = Integer.parseInt(mulStr);
					} catch (NoSuchElementException e) {
						// TODO Auto-generated catch block
						chrgStr=null;
						mulStr=null;
						System.out.println("Charge Multiplicity not entered");
						e.printStackTrace();
					}
                    
                    
                    foundQSM = true;
                }
                else if ((foundQSM == true) && (foundGeom == false))    
                {
                    geomIndex = (2 * (blanklineTerminatedLineNumbers[(nblanklinesRead - 1)]) + (nblanklinesRead + 2));
                    System.out.println("Index is$$$###"+geomIndex);
                    for (k = geomIndex; k < nInfileTokens; k++)
                    {
                        sbGeom.append(inputFileTokens.get(k).toString());
                    }

                    while (sbGeom.indexOf(",") != -1)
                    {
                        k = sbGeom.indexOf(",");

                        sbGeom.replace(k, k+1, " ");
                    }

                    geom = sbGeom.toString();
                    System.out.println("Molecular Co-ordinates\n" + geom);

                    foundGeom = true;
                }
            }     

            if (!geom.equals(""))
            {
                new GeometryEditor(geom);  // uncommented on Sep 13 @ 3.40
            }      
        }  
    }

    public static void main(String[] args)
    {
        new InputfileReader();
    } 
}   

