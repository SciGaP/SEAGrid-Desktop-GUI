/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

Permission is hereby granted, free of charge, to any person 
obtaining a copy of this software and associated documentation
files (the "Software") to deal with the Software without
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
3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
   University of Illinois at Urbana-Champaign, nor the names of its contributors 
   may be used to endorse or promote products derived from this Software without 
   specific prior written permission.
    
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS WITH THE SOFTWARE.

*/
package org.gridchem.client;
import java.util.*;


/*************************************************************************************
    <b>LayeredHashMap</b> has a member hashmap for storing keyword-centric 
    fields.  It also has a String label.  It's constructors set the initial 
    size of the hashmap to zero.

    Use <b>put</b> to put String objects or additional layers into LayeredHashMap.

    Use <b>get</b> to retrieve String objects or additional layers from LayeredHashMap.

    <ul>toString</ul> provides help for debugging, mostly.  

    @author John J. Lee, NCSA
    @version $Id: LayeredHashMap.java,v 1.1.1.1 2005/04/26 16:33:59 dooley Exp $
*/
public class LayeredHashMap {
  public HashMap map;
  public String label = "";


  //CONSTRUCTORS
  public LayeredHashMap() {
    map = new HashMap(0);
  }
  public LayeredHashMap(String s) {
    map = new HashMap(0);
    label = s;
  }


  public void put(String k, String v) {
    map.put(k, v);
  }
  public void put(String k, LayeredHashMap m) {
    map.put(k, m);
  }
  public void put(String k, String k2, String v) {
    try {// case:  m exists
      LayeredHashMap m = (LayeredHashMap)map.get(k);
      m.put(k2, v);
    } catch (Exception e) {// case:  m does not exist, 
             //        so create it with label k
      LayeredHashMap m = new LayeredHashMap(k);
      m.map.put(k2, v);
      map.put(k, m);
    }
  }


  public Object get(String k) {
    return map.get(k);
  }
  public Object get(String k, String k2) {
    LayeredHashMap m = (LayeredHashMap)map.get(k);
    return m.get(k2);
  }


  public int size() {
    return map.size();
  }
  public int size(String k) {
    LayeredHashMap m = (LayeredHashMap)map.get(k);
    return m.size();
  }


  public Set keySet() {
    return map.keySet();
  }


  public void clear() {
    map.clear();
    label = "";
  }


  public String toString(){
    Set keys = map.keySet();
    if (map.size() > 0) {
      System.out.println("LayeredHashMap:toString:  label --> "+label);
      for(Iterator i = keys.iterator(); i.hasNext();) {
	String k = (String)i.next();
	Object o = map.get(k);
	System.out.println("LayeredHashMap:toString:  key, value --> "+k+", "+o.toString());
      }
      return "LayeredHashMap:toString:  see System.out";
    } else {
      return "LayeredHashMap:toString:  LayeredHashMap is empty";
    }
  }


  public static void main(String[] argv) {
    System.out.println("TESTING LayeredHashMap..........");
    System.out.println(" ");
    //___________________________________________________________________________________
    System.out.println("simple test:  simple{a -> 1, b -> 2}");
    LayeredHashMap simple = new LayeredHashMap("simple");
    simple.put("a", "1");
    simple.put("b", "2");
    System.out.println("................................");
    System.out.println(simple.toString());
    System.out.println("................................");
    System.out.println("simple.get(\"a\") --> "+simple.get("a"));
    System.out.println("simple.get(\"b\") --> "+simple.get("b"));
    System.out.println("................................");
    System.out.println("simple.size() --> "+simple.size());
    System.out.println(" ");
    //___________________________________________________________________________________
    System.out.println("layered test:  layered{a -> 1, b -> 2, c -> {d -> 3, e -> 4}}");
    LayeredHashMap layered = new LayeredHashMap("layered");
    layered.put("a", "1");
    layered.put("b", "2");
    layered.put("c", "d", "3");
    layered.put("c", "e", "4");
    System.out.println("................................");
    System.out.println(layered.toString());
    System.out.println("................................");
    System.out.println("layered.get(\"a\") --> "+layered.get("a"));
    System.out.println("layered.get(\"b\") --> "+layered.get("b"));
    System.out.println("layered.get(\"c\", \"d\") --> "+layered.get("c", "d"));
    System.out.println("layered.get(\"c\", \"e\") --> "+layered.get("c", "e"));
    System.out.println("................................");
    System.out.println("layered.size() --> "+layered.size());
    System.out.println("layered.size(\"c\") --> "+layered.size("c"));
    System.out.println(" ");
    //___________________________________________________________________________________
    System.out.println("mapping test:  map1 -> {f -> 5}}; map2{a -> 1, map1, g -> 6}; ");
    LayeredHashMap map1 = new LayeredHashMap("map1");
    map1.put("f", "5");
    LayeredHashMap map2 = new LayeredHashMap("map2");
    map2.put("a", "1");
    map2.put("map1", map1);
    map2.put("g", "6");
    System.out.println("................................");
    System.out.println(map1.toString());
    System.out.println(map2.toString());
    System.out.println("................................");
    System.out.println("map1.get(\"f\") --> "+map1.get("f"));
    System.out.println("map2.get(\"a\") --> "+map2.get("a"));
    System.out.println("map2.get(\"map1\") --> "+map2.get("map1"));
    System.out.println("map2.get(\"map1\", \"f\") --> "+map2.get("map1", "f"));
    System.out.println("map2.get(\"g\") --> "+map2.get("g"));
    System.out.println("................................");
    System.out.println("map1.size() --> "+map1.size());
    System.out.println("map2.size() --> "+map2.size());
    System.out.println("map2.size(\"map1\") --> "+map2.size("map1"));
  }

}




