package org.gridchem.client;



/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://ncsa.uiuc.edu/GridChem

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

/**@author John J. Lee
   @version $Id: MyLongTask.java,v 1.2 2005/07/05 21:47:22 dooley Exp $

   MyLongTask is largely based on LongTask which uses a SwingWorker to 
   perform a time-consuming task which is defined in another inner class
   named ActualTask.  

   To implement MyLongTask, define a subclass, SC, then write ActualTask 
   as an inner class of SC.
   @see LongTask
   @see http://java.sun.com/docs/books/tutorial/uiswing/components/progress.html

   For an example of implementation...
   @see KerberosTask
   @see LoginPanel
*/

public abstract class MyLongTask {

    public int lengthOfTask;
    public int current = 0;

    public MyLongTask() {
      //Compute length of task...
      lengthOfTask = 1;
    }

    /**
    Called from implemented class to start the task.  
    ================================================
    public void go() {
      current = 0;
      final SwingWorker worker = new SwingWorker() {
	  public Object construct() {
	    return new ActualTask();
	  }
	};
      worker.start();
    }
    */

    /**
       Called from parent class to find out how much work needs
       to be done.                                             */
    public int getLengthOfTask() {
      return lengthOfTask;
    }

    /**
       Called from parent class to find out how much has been done.  */
    public int getCurrent() {
      return current;
    }

    /* this entire class is the basis for how timers were
     * used in the original client.  There are still some
     * artificats of this around due to exactly the point
     * you are making here: tight coupling of otherwise
     * unrelated tasks. 
     * 
     * as of right now, i think the only reason this is here
     * is because mylongtask is primarily used by the login
     * routines.  thus, for whatever reason, when a task is
     * stopped, it should update the client's authenticated
     * status.  this was probably put here rather than in every
     * subclass to reduce code.  i might have done this, i'm 
     * not sure...sorry.  anyway, the nanocad issues you're 
     * experiencing are due to the sudden decision to bypass
     * our authentication framework in favor of providing  
     * nanocad from the main page.  i expect that there
     * will be quite a few more problems due to this decision 
     * as time goes by.
     * 
     * Rion Dooley Sept 26, 2006.
     */
    public void stop() {
        
//        LoginPanel.enableButtons(true);  // Why is this here ?
        
        GridChem.oc.updateAuthenticatedStatus();
        
        current = lengthOfTask;
    }

    /**
       Called from parent class to find out if the task has completed.  */
    public boolean done() {
      if (current >= lengthOfTask)
          return true;
      else
          return false;
    }

}// end public abstract class MyLongTask
