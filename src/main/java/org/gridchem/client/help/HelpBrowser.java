/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on May 2, 2006
 * 
 * Developed by: CCT, Center for Computation and Technology, 
 * 				NCSA, University of Illinois at Urbana-Champaign
 * 				OSC, Ohio Supercomputing Center
 * 				TACC, Texas Advanced Computing Center
 * 				UKy, University of Kentucky
 * 
 * https://www.gridchem.org/
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal with the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimers.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimers in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the names of Chemistry and Computational Biology Group , NCSA, 
 *    University of Illinois at Urbana-Champaign, nor the names of its contributors 
 *    may be used to endorse or promote products derived from this Software without 
 *    specific prior written permission.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS WITH THE SOFTWARE.
*/

package org.gridchem.client.help;

import java.net.MalformedURLException;
import java.net.URL;

import org.gridchem.client.Invariants;
import org.gridchem.client.util.Env;

import oracle.help.Help;
import oracle.help.library.helpset.HelpSet;
import oracle.help.library.helpset.HelpSetParseException;
import oracle.help.topicDisplay.TopicDisplayException;

/**
 * Utility class instantiating an Oracle Help object with the GridChem
 * help documentation. In order to decrease the client download size,
 * the documentation is stored on the gridchem website.  The client 
 * then pulls the information from there as needed.
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class HelpBrowser {
    private static Help help;
    private static HelpSet book;
    
    public HelpBrowser() {
        boolean bookRead = false;
        java.security.Security.addProvider(
            new com.sun.net.ssl.internal.ssl.Provider() );
    
//        System.setProperty("java.protocol.handler.pkgs",
//            "com.sun.net.ssl.internal.www.protocol");
//    
//        System.setProperty("javax.net.ssl.trustStore",
//            Env.getGlobusCertDir() + Env.separator() + "ccgkeystore");
//              
        try {
            
            book = new HelpSet(new URL(Invariants.gridchemHelpLocation));
            
            bookRead = true;

        } catch (HelpSetParseException e) {
            e.printStackTrace();
            book = new HelpSet();
            bookRead = false;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // now load book into help browser            
        help = new Help(false,false,true);

        help.addBook(book);
        
        help.showNavigatorWindow();
        
        if (!bookRead) {
            help.showTopic(book,"onErrorTopic");
        }
        
        help.setVisible(true);
        
    }
    
    public void showHelp() {
        help.setVisible(true);
    }
    
    public void hideHelp() {
        help.setVisible(false);
    }
    
    public void showTopic(String topic) {
        try {
            help.showTopic(book,topic);
        } catch (TopicDisplayException e) {
            help.showTopic(book,"onErrorTopic");
        }
    }
}
