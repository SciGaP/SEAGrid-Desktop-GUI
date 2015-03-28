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
 * 
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta @author Sandeep Kumar Seethaapathy  
 * 
 */
package G03Input;


import java.util.regex.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.io.*;
import java.lang.*;
public class atomCoordinateParser {
public static int count;
public static String atomArray=" ";	
public static String coordinates=" ";	
public static int atomIndex=0;
public static void getCoordinates()
{
//StringReader reader=new StringReader(G03Input.showMolEditor.tempmolFromInput);
	StringReader reader=new StringReader(GeometryEditor.validateGeom);
BufferedReader br= new BufferedReader(reader);
BufferedReader br1= new BufferedReader(reader); // Buffered Reader to Read the no. of lines

String temp="";
try {
		
	atomArray="";
	coordinates="";
	atomIndex=0;
	temp=br.readLine();
		boolean matched= Pattern.matches("[\\s]*[\\+|\\-]?[0-9]+\\.?[0-9]*[\\s]*[\\+|\\-]?[0-9]+\\.?[0-9]*[\\s]*",temp); //Charge and Multiplicity
		if(matched){
			temp=br.readLine();
			while(temp!=null){
				boolean molmatched=
					Pattern.matches("[A-Z][a-z]?[\\s]+[\\+|\\-]?[0-9]+\\.?[0-9]+[\\s]+[\\+|\\-]?[0-9]+\\.?[0-9]+[\\s]+[\\+|\\-]?[0-9]+\\.?[0-9]+[\\s]*",temp);
				String[] tempArray=temp.split("[\\s]+");
			System.out.println(tempArray);
			atomArray+=tempArray[0]+" ";  //Stores the Atom Name
			coordinates+=tempArray[1]+" "+tempArray[2]+" "+tempArray[3]+" ";
			atomIndex++;
			if((molmatched==false)){
			JOptionPane.showMessageDialog(null,"Incoherent Molecular Specification","Error",JOptionPane.ERROR_MESSAGE);
			System.out.println("G03GUI:atomCoordintaeParser:Matched incoherent input line:"+temp);
			break;
			}
			temp=br.readLine();
			}
     new BufferedDisplayMolecule();  // Show the new Molecule
			 
		}
		else{
			JOptionPane.showMessageDialog(null,"Incoherent Charge and Multiplicity","Error",JOptionPane.ERROR_MESSAGE);
		}
	
} catch (IOException e) {
	// TODO Auto-generated catch blockzx
	e.printStackTrace();
}
catch(ArrayIndexOutOfBoundsException e)
{
	e.printStackTrace();
}
}
/*
	public static void main(String args[])
	{
		getCoordinates();
		
	}
*/	
}
