package nanocad;

/**
 * bond.java - definition of a bond
 * Copyright (c) 1998 Will Ware, all rights reserved.
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

import java.util.Vector;

public class bond
{
  public static final String rcsid =
  "$Id: bond.java,v 1.2 2005/05/14 23:51:01 xli16 Exp $";
    private int _order;		// number of bonds represented
    private double apporder;	// actual order based on resonance
    public atom a1, a2;


  public bond (atom atm1, atom atm2)
	{
	  a1 = atm1;
	  a2 = atm2;
	  _order = 1;
	  apporder = 1;
	  a1.bonds.addElement (this);
	  a2.bonds.addElement (this);
	  a1.rehybridize();
	  a2.rehybridize();
	}

    //Ying new constructor with order
    public bond (atom atom1, atom atom2, int orders){
	{
	    a1 = atom1;
	    a2 = atom2;
	    _order = orders;
	    apporder = _order;
         try{
/*
          if (a1 == null)
          //    a1 = new atom();
              a1 = a2; 
          System.out.println("In bond");
          System.out.println(" a1 = " + atom1);
          System.out.println(" a2 = " + atom2);*/
	    a1.bonds.addElement(this);
	    a2.bonds.addElement(this);
         // System.out.println("After .....");
           } catch(Exception e1){ System.out.println("Exception in Bond.java " + e1); };

	    a1.rehybridize();
	    a2.rehybridize();
	}
    }

    //this constructor only copies the order, since we don't know what atoms the bond will go between
    public bond(bond b)
    {
	a1 = null;
	a2 = null;
	_order = b._order;
	apporder = _order;
    }
	

  public boolean contains (atom atm1)
	{
	  return (a1 == atm1 || a2 == atm1);
	}
  public void delete ()
	{
	  a1.bonds.removeElement (this);
	  a2.bonds.removeElement (this);
	}
  public void incrOrder ()
	{
	  if (_order < 3)
	_order++;
	apporder = _order;
	  /* else throw an exception?? */
	}

    //Ying

    public void setOrder (int o){
	_order= o;
	apporder = _order;
    }

    public void setAppOrder(double o){
	apporder = o;
    }
    
    public int order ()
    {
	return _order;
    }

    public double apporder(){
	return apporder;
    }

    public atom otherAtom (atom atm1)
    {
	if (atm1 == a1)
	    return a2;
	  if (atm1 == a2)
	return a1;
	  return null;
    }

    public String repr()
    {
		
	return "<Bond " +
	    (new Integer(a1.index())).toString() + " " +
	    (new Integer(a2.index())).toString() + " " +
	    (new Integer(_order)).toString() + ">";
    }
}
