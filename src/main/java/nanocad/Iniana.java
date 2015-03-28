package nanocad;

   
       import java.applet.*;
       import java.awt.*;
       import java.awt.event.*;
       import java.net.*;
       import java.io.*;
       import java.lang.*;
       import java.util.*;
       import java.awt.geom.*;
       import java.awt.image.*;

public class Iniana
  {
   public static String name,formula,a;
public static char g,j,t,special;
 public static  String query_string=null;
public static LinkedList tosend = new LinkedList();
    public static void main(String argv[])
    {
  //   System.out.println("In Java code");

    String atom1Name = null,atom1Num = null;
    String atom2Name = null,atom2Num = null;
    String atom3Name = null,atom3Num = null;
/*
     if(argv.length !=2){
      System.err.println("Usage: cmd compoundname formula");
      System.exit(1);
      }
  */  String search_str = argv[0];
    query_string = "";
    String chk = argv[1];
   //System.out.println(chk);
  // System.out.println(search_str); 
    if(chk.equals("0"))  // => we passed a name
       query_string ="http://www.iumsc.indiana.edu/db/search.jsp?start=1&compoundName=" + search_str+ "&raw=pdb";
    //System.out.println(" ***** "+ query_string);
    if(chk.equals("1")) //=> we passed a Formula
       {
           //Parse the formula string into alphabets and numbers
           // and then generate the query string reqd by Indiana
           int k=0;
     while(k<search_str.length()){
       Character asj;
       special =  search_str.charAt(0);
       if(Character.isDigit(special)){
       //System.out.println("Your search_str is wrong");
       System.exit(0);
       }
       g = search_str.charAt(k);

       if(Character.isUpperCase(g)){
        asj=new Character(g);
        tosend.add(asj.toString());
        k++;
      if(k>= search_str.length()){
      tosend.add("1");
      break ;
      }
     j = search_str.charAt(k);
    if(Character.isUpperCase(j)){
        tosend.add("1");
        asj = new Character(j);
        tosend.add(asj.toString());
        k++;

        if(k>= search_str.length()){
            tosend.add("1");
            break ;
             }
          else if(Character.isDigit(t = search_str.charAt(k))){
              asj = new Character(t);
              tosend.add(asj.toString());
              k++;
            }
        else if(Character.isLowerCase(t = search_str.charAt(k))){
        asj = new Character(t);
           String add = tosend.getLast().toString();
           String after = add + asj.toString();
            tosend.removeLast();
            tosend.addLast(after);
            k++;

             if(k>= search_str.length()){
              tosend.add("1");
              break ;
               }
         else if(Character.isDigit(t = search_str.charAt(k))){
              asj = new Character(t);
              tosend.add(asj.toString());
              k++;
            }
         else
           tosend.add("1");
                }
     }
        else if(Character.isDigit(j)){
        asj = new Character(j);
        tosend.add(asj.toString());
       // tosend.add("1");
        k++;
        if(k>= search_str.length()){
         break ;
      }
        }
        else{
            asj = new Character(j);
            String test = tosend.getLast().toString();
           String test1 = test + asj.toString();
            tosend.removeLast();
            tosend.addLast(test1);
            k++;

         if(k>= search_str.length()){
            tosend.add("1");
            break ;
            }

          else if(Character.isDigit(t = search_str.charAt(k))){
              asj = new Character(t);
              tosend.add(asj.toString());
              k++;
            }
        else
            tosend.add("1");

           }
       }  }



     atom1Name= tosend.get(0).toString();
     atom2Name= tosend.get(2).toString();
     atom1Num = tosend.get(1).toString();
     atom2Num = tosend.get(3).toString();


  /*     for(int o = 0;o<tosend.size();++o)
     System.out.println("Hello see the output"+tosend.get(o));
   */



 String query_string = "http://www.iumsc.indiana.edu/db/search.jsp?start=1&atom1Name=" + atom1Name + "&atom1Relation==atom1Num=" + atom1Num + "&&atom2Name=" + atom2Name + "&atom2Relation==atom2Num=" + atom2Num + "&&atom3Name=" + atom3Name + "&atom3Relation==atom3Num="  + atom3Num + "&raw=pdb";

     }

          try
        {
          URL url_indiana = new URL(query_string);
          URLConnection conn_indiana = url_indiana.openConnection();
          BufferedReader inFile = new BufferedReader (new InputStreamReader(conn_indiana.getInputStream()));

          String line = null;
          String final_file="";

          int counter = 0;
          while ((line = inFile.readLine()) !=null)
            {
                if(counter <5)
                    counter++;
                else
                    final_file += line;
            }
          System.out.println(final_file);

        }catch(Exception e1)
         { System.err.println(e1); }

      }
  }
