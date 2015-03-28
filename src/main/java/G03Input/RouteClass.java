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
 * Created on Apr 4, 2005
 * @author Michael Sheetz 
 * @author Shashank Jeedigunta  @author Sandeep Kumar Seethaapathy 
 * 
 */


package G03Input;


import java.util.regex.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;


public class RouteClass implements ActionListener{
    public static int initCount=0;
    public static StringBuffer routeBuffer= new StringBuffer();
    public static StringBuffer poundOpt=null;
    public static StringBuffer methodBuffer=null;
    public static StringBuffer basisBuffer=null;
    public static StringBuffer[] keywordBuffer=null;
    public static final int KEYWORD_BUFFER_LENGTH = 15;
    public static RouteClass rt; 
    public static int keyIndex;
    
    
    public static void createInput(){
       if(InputFile.inputfetched==0){ 
        try {
            InputFile.tempinput="";
                    if(!(G03MenuTree.noofspText.getText().length()<1))
                    {
                        InputFile.tempinput+="%nproc="+G03MenuTree.noofspText.getText();
                        InputFile.tempinput+="\n";
                    }
                    if(!(G03MenuTree.nooflpText.getText().length()<1))
                    {
                        InputFile.tempinput+="%nprocl="+G03MenuTree.nooflpText.getText();
                        InputFile.tempinput+="\n";
                    }
                    if(!(G03MenuTree.dynmemText.getText().length()<1))
                    {
                        InputFile.tempinput+="%mem="+G03MenuTree.dynmemText.getText()+"MB";
                        InputFile.tempinput+="\n";
                     }
                    if(!(G03MenuTree.filnamArea.getText().length()<1))
                    {
                        InputFile.tempinput+="%Chk="+G03MenuTree.filnamArea.getText();
                        InputFile.tempinput+="\n";
                    }
                } catch (NullPointerException exc) {
             
                    exc.printStackTrace();
                }
            	
               // InputFile.tempinput+="\n";	// Modified Sept 15,2005
                InputFile.tempinput+=routeBuffer;
                InputFile.tempinput+="\n\n";
                InputFile.tempinput+=G03MenuTree.jobArea.getText();
                InputFile.tempinput+="\n\n";
                
                //Newly added on Sept 15,2005 @ UKY after separating charge and mul from tempmol 
                          		
                InputFile.tempinput+=G03MenuTree.molCharge.getText()+" "+G03MenuTree.molMultiplicity.getText()+"\n";
                
                
                //End of Newly added on Sept 15,2005 @ UKY after separating charge and mul from tempmol
                
                System.out.println("In RouteClass"+showMolEditor.tempmol);
            	InputFile.tempinput+=showMolEditor.tempmol;
               
                InputFile.tempinput+="\n";
       }
            }
    
    public RouteClass()
    {
    	 
        routeBuffer = new StringBuffer();
        methodBuffer = new StringBuffer();
        basisBuffer = new StringBuffer();
        poundOpt = new StringBuffer("# ");
        keywordBuffer = new StringBuffer[KEYWORD_BUFFER_LENGTH];
    }

    public void actionPerformed(ActionEvent ae)
    {
        if(ae.getSource()==G03MenuTree.nRadio)
        {   
        	
        
        	poundOpt = new StringBuffer();
        	poundOpt.append("# ");
            //poundOpt.replace(0,1,"# ");
            //new RouteClass();
            writeRoute();
        }
        if(ae.getSource()==G03MenuTree.pRadio)
        {
        	
        
        	poundOpt = new StringBuffer();
        	poundOpt.append("#P ");
            //new RouteClass();
            writeRoute();
        }
        if(ae.getSource()==G03MenuTree.tRadio)
        {
        	
        
        	poundOpt = new StringBuffer();
        	poundOpt.append("#T ");
            //new RouteClass();
            writeRoute();
        }
    }
    public static void initBuffer()
    {
    	 RouteClass.keywordBuffer = new StringBuffer[KEYWORD_BUFFER_LENGTH];
         for(int i = 0; i < keywordBuffer.length; i++)
         {
         		RouteClass.keywordBuffer[i]=new StringBuffer();
         }
    }
    
    public static void rewriteRoute(String nodeName,String parent)
    {
        if(parent.equals("Methods"))
        {
            InsertNode.deleteChildren("Methods");
            MenuMethodListener.createHTable();
            MenuMethodListener.count=0;
            MenuMethodListener.exchngFlag=0;
            MenuMethodListener.corrFlag=0;
            
            
            methodBuffer = new StringBuffer();
            writeRoute();
        
        }
        else if(parent.equals("Basis Sets"))
        {
               
            basisBuffer= new StringBuffer();
            writeRoute();
        
        
        }
        else if(parent.equals("Job Types"))
        {
            if(nodeName.equals("Freq"))
            {
                keywordBuffer[FreqOptTable.freqFlag]=new StringBuffer(); 
                FreqOptTable.freqC=0;
                writeRoute();
            }
            
                     
            if(nodeName.equals("Opt  "))
            {
                keywordBuffer[OptTable.optFlag]=new StringBuffer(); 
                OptTable.optC=0;
               
                writeRoute();
            }
            
            if(nodeName.equals("OptFreq"))
            {
                	//  Direct Child but name is Different
              MenuListeners.optfreqflag=0;
              writeRoute();
            }
                    
            if(nodeName.equals("Opt_IRC-Max"))
               {
                MenuListeners.optircmaxflag=0;
                writeRoute();
               }
               
               if(nodeName.equals("Opt_Polar"))
               {
                   MenuListeners.optpolarflag=0;
                   writeRoute();
                
               
               }
               
               
               
            else{
            //Directly Children
            //keywordBuffer[RouteClass.keyIndex];
                int index;
                int lastindex;
                System.out.println("nodeName"+nodeName+"$$");
                for(int i=0;i<keyIndex;i++)
                {
                    if((index=keywordBuffer[i].indexOf(nodeName))!= -1)
                    {
                        keywordBuffer[i]=new StringBuffer();
                       // keywordBuffer[i+1]=new StringBuffer(); // " " 
                       /* 
                        // the nodeName exists
                        lastindex= index+nodeName.length();
                        keywordBuffer[keyIndex].delete(index,lastindex);
                       // keyIndex--;*/
                        break;
                    }
                
                
                }
               // lastindex= index+nodeName.length();
               // System.out.println("INDEX"+index+"last"+lastindex);
              //  keywordBuffer[keyIndex].delete(index,lastindex);
                writeRoute();
                
                
            }
            
        }
        
        else if(parent.equals("Keywords"))
        {
           if(nodeName.equals("Geom"))
           {
               keywordBuffer[geomOptTable.geomFlag]=new StringBuffer(); 
              geomOptTable.geomC=0;
               
           }
           
           else if(nodeName.equals("PBC"))
           {
               keywordBuffer[pbcTable.pbcFlag]=new StringBuffer(); 
               pbcTable.pbcC=0;
               writeRoute();
               
           }
           else if(nodeName.equals("Pop"))
           {
               keywordBuffer[popOptTable.popFlag]=new StringBuffer(); 
               popOptTable.popC=0;
               writeRoute();
            }

            
        }
        else  // it should be in either jobtypes or keywords subfolders
        {
            //if it is Freq
            if(parent.equals("Freq"))
            {
            int index;
            int lastindex;
            index=keywordBuffer[FreqOptTable.freqFlag].indexOf(nodeName);
            lastindex= index+nodeName.length();
            String comma;
           
            System.out.println(keywordBuffer[FreqOptTable.freqFlag].toString());
            System.out.println("index"+index+"lastindex"+lastindex+"commaindex");
           
            if(lastindex!=keywordBuffer[FreqOptTable.freqFlag].length())
            {
                lastindex++; 
            }
            if(lastindex==keywordBuffer[FreqOptTable.freqFlag].length())
            {
                lastindex--; 
                index--;
                
            }
          
            keywordBuffer[FreqOptTable.freqFlag].delete(index,lastindex);
            
            if(((keywordBuffer[FreqOptTable.freqFlag].length())-1)==5)
            {
                // only =) is present
                
                keywordBuffer[FreqOptTable.freqFlag].delete(4,6);
            
            }
            
            writeRoute();
            }
            
            //POP
            if(parent.equals("Pop"))
            {
            int index;
            int lastindex;
            index=keywordBuffer[popOptTable.popFlag].indexOf(nodeName);
            lastindex= index+nodeName.length();
            String comma;
           
            System.out.println(keywordBuffer[popOptTable.popFlag].toString());
            System.out.println("index"+index+"lastindex"+lastindex+"commaindex");
           
            if(lastindex!=keywordBuffer[popOptTable.popFlag].length())
            {
                lastindex++; 
            }
            if(lastindex==keywordBuffer[popOptTable.popFlag].length())
            {
                lastindex--; 
                index--;
                
            }
          
            keywordBuffer[popOptTable.popFlag].delete(index,lastindex);
            
            if(((keywordBuffer[popOptTable.popFlag].length())-1)==4)
            {
                // only =) is present
                
                keywordBuffer[popOptTable.popFlag].delete(3,5);
            
            }
            
            writeRoute();
            }

            
            
            
            //Density
            if(parent.equals("Density"))
            {
               keywordBuffer[MenuListeners.denFlag]=new StringBuffer(); 
               MenuListeners.denC=0;
               
               //delete from tree too...
              InsertNode.deleteNode("Density");
               
               
               
               
               writeRoute();
            }
            
            //Opt
            if(parent.equals("Opt  "))
            {
            int index;
            int lastindex;
            index=keywordBuffer[OptTable.optFlag].indexOf(nodeName);
            lastindex= index+nodeName.length();
            String comma;
           
            System.out.println(keywordBuffer[OptTable.optFlag].toString());
            System.out.println("index"+index+"lastindex"+lastindex+"commaindex");
           
            if(lastindex!=keywordBuffer[OptTable.optFlag].length())
            {
                lastindex++; 
            }
            if(lastindex==keywordBuffer[OptTable.optFlag].length())
            {
                lastindex--; 
                index--;
            }
            keywordBuffer[OptTable.optFlag].delete(index,lastindex);
            
            //if(((keywordBuffer[OptTable.optFlag].length())-1)==4)
            if(((keywordBuffer[OptTable.optFlag].length())-1)==4)
            {
                // only =) is present
                System.out.println("IN OPT DELETION");
                //keywordBuffer[OptTable.optFlag].delete(3,5);
              keywordBuffer[OptTable.optFlag].delete(3,7);
              //  keywordBuffer[OptTable.optFlag].append("  ");
            
            }
            
            writeRoute();
            }
            
            if(parent.equals("Guess"))
            {
            int index;
            int lastindex;
            index=keywordBuffer[GuessOptTable.guessFlag].indexOf(nodeName);
            lastindex= index+nodeName.length();
            String comma;
           
            System.out.println(keywordBuffer[GuessOptTable.guessFlag].toString());
            System.out.println("index"+index+"lastindex"+lastindex+"commaindex");
           
            if(lastindex!=keywordBuffer[GuessOptTable.guessFlag].length())
            {
                lastindex++; 
            }
            if(lastindex==keywordBuffer[GuessOptTable.guessFlag].length())
            {
                lastindex--; 
                index--;
            }
            keywordBuffer[GuessOptTable.guessFlag].delete(index,lastindex);
            
            if(((keywordBuffer[GuessOptTable.guessFlag].length())-1)==6)
            {
                // only =) is present
                
                keywordBuffer[GuessOptTable.guessFlag].delete(5,7);
            
            }
            
            writeRoute();
            }
            
            //Geom Children
            
            if(parent.equals("Geom"))
            {
            int index;
            int lastindex;
            index=keywordBuffer[geomOptTable.geomFlag].indexOf(nodeName);
            lastindex= index+nodeName.length();
            String comma;
           
            System.out.println(keywordBuffer[geomOptTable.geomFlag].toString());
            System.out.println("index"+index+"lastindex"+lastindex+"commaindex");
           
            if(lastindex!=keywordBuffer[geomOptTable.geomFlag].length())
            {
                lastindex++; 
            }
            if(lastindex==keywordBuffer[geomOptTable.geomFlag].length())
            {
                lastindex--; 
                index--;
            }
            keywordBuffer[geomOptTable.geomFlag].delete(index,lastindex);
            
            if(((keywordBuffer[geomOptTable.geomFlag].length())-1)==5)
            {
                // only =) is present
                // Geom Shud contain atleast one keyword... else remove every thing
                keywordBuffer[geomOptTable.geomFlag]=new StringBuffer(); 
                geomOptTable.geomC=0;
                //remove Geom From Tree...
                if(InsertNode.nodeExists("Geom"))
                {
                InsertNode.deleteNode("Geom");    
                
                }
            }
            writeRoute();
            }
            
            if(parent.equals("PBC"))
            {
                //Delete the appropriate Children
                
                int index;
                int lastindex;
                index=keywordBuffer[pbcTable.pbcFlag].indexOf(nodeName);
               // lastindex= index+nodeName.length();
               
               lastindex=keywordBuffer[pbcTable.pbcFlag].indexOf(",",index);
                if(lastindex==-1) //no comma
                    lastindex=keywordBuffer[pbcTable.pbcFlag].indexOf(")",index);
              
               
                if(lastindex!=keywordBuffer[pbcTable.pbcFlag].length())
                {
                    lastindex++; 
                }
                if(lastindex==keywordBuffer[pbcTable.pbcFlag].length())
                {
                    lastindex--; 
                    index--;
                }
                
                
                System.out.println("LI"+lastindex);
                
                keywordBuffer[pbcTable.pbcFlag].delete(index,lastindex);
                
                
             //  keywordBuffer[pbcTable.pbcFlag].delete(index-1,lastindex);
               
                
                if(((keywordBuffer[pbcTable.pbcFlag].length())-1)==4)
                {
                    // only =) is present
                    
                    keywordBuffer[pbcTable.pbcFlag].delete(3,5);
                
                }
                
                writeRoute();
                
            }
            
            
            
            
            
            
        }
    }
    public static void writeRoute()
    {
    	 
    routeBuffer=new StringBuffer();
    routeBuffer.append(poundOpt);   
   
    routeBuffer.append(methodBuffer);
    try {
      
        if(basisBuffer.length()>1)
        routeBuffer.append("/"+basisBuffer);
    } catch (NullPointerException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    routeBuffer.append(" ");
    System.out.println(keyIndex);
    /*if(keyIndex>1)
    {
    System.out.println("buffer"+keywordBuffer[keyIndex-1]);
    System.out.println("index"+keyIndex);
    System.out.println("0"+keywordBuffer[0]);
    System.out.println("1"+keywordBuffer[1]);
    System.out.println("2"+keywordBuffer[2]);
    }
    //routeBuffer.append(keywordBuffer[0][0]);*/
    for(int key=0;key<keyIndex;key++)
    {
    	routeBuffer.append(keywordBuffer[key]);
    	 routeBuffer.append(" ");
    }
    
    //For Other Keywords
    if(otherKeyTable.otherFlag>0)
    for(int key=0;key<otherKeyTable.otherKeys.size();key++)
    {
    	routeBuffer.append(otherKeyTable.otherKeys.get(key));
    	 routeBuffer.append(" ");
    }
    
    
    if(MenuListeners.optfreqflag==1) routeBuffer.append("Opt Freq");
    if(MenuListeners.optircmaxflag==1) routeBuffer.append("Opt IRC-Max");
    if(MenuListeners.optpolarflag==1) routeBuffer.append("Opt Polar");
    
   G03MenuTree.routeArea.setText(routeBuffer.toString());
   // JTextPane p =new JTextPane();
   //p.
    }
}
