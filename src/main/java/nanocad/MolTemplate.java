package nanocad;

/**
 * aspirin.java
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

import java.awt.Panel;
import java.util.*;

public class MolTemplate extends group
{
    public static final String rcsid =
      "$Id: MolTemplate.java,";

    /**
     *contructor 
     */
    public MolTemplate (Vector atomV, Vector bondV, drawPanel p)
    {
	mypanel = p;
	for (int i=0; i<atomV.size(); i++)
	    addAtom((atom)atomV.elementAt(i));
	for (int i = 0; i < bondV.size(); i++)
	    bondList.addElement((bond)bondV.elementAt(i));	
    }
}








