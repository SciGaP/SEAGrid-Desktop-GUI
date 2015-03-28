/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.
 * 
 * Created on Jun 14, 2006
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

package org.gridchem.client.gui.filebrowser;

import java.io.FileInputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JFrame;

import org.gridchem.client.GridChem;
import org.gridchem.client.Invariants;
import org.gridchem.client.common.Settings;
import org.gridchem.client.util.Env;
import org.gridchem.client.util.GMS3;
import org.gridchem.service.beans.ProjectBean;
import org.gridchem.service.model.enumeration.AccessType;

/**
 * Beefed up file browser class enabling users to view their job output via
 * interaction with the GMS_WS.  All interaction including file transfers 
 * is done via secure message level communication with the service.  
 * 
 * @author Rion Dooley < dooley [at] tacc [dot] utexas [dot] edu >
 *
 */
public class FileBrowserTest {
    private static String username;
    private static String password;
    private static AccessType projectType;
    private static String myproxyUsername = "";
    private static String myproxyPassword = "";
    
    private static void create() {
        
        GridChem gc = new GridChem();
        
        Properties props = new Properties();
        
        Settings.WEBSERVICE = true;
        
        JFrame myFrame = new JFrame("GridChem File Browser");
        
        FileBrowserImpl browser = null;
        
        try {
            
            // Read in user information from the configuration file
            props.load(new FileInputStream("etc/test.properties"));
            
            username = props.getProperty("gridchem.username");
            
            password = props.getProperty("gridchem.password");
            
            // Authenticate with the GMS_WS
            if (Settings.DEBUG)
                System.out.println("Logging " + username + " into the CCG.");
            try {
                GMS3.login(username,password,AccessType.COMMUNITY,new HashMap<String,String>());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            Settings.authenticated = true;
            Settings.authenticatedGridChem = true;
            Settings.gridchemusername = username;
            
            System.out.println("project type: " + (String)props.getProperty("access.type"));
//          Load the user's resources into the session.
            ProjectBean project = null;
            if(((String)props.getProperty("access.type"))
                    .toUpperCase().equals(AccessType.COMMUNITY.toString())) {
                projectType = AccessType.COMMUNITY;
                System.out.println("selected a community project");
            } else {
                System.out.println("project type is " + (String)props.getProperty("access.type"));
                projectType = AccessType.EXTERNAL;
                myproxyUsername = props.getProperty("myproxy.username");
                myproxyPassword = props.getProperty("myproxy.password");
            }
            
            try {
                for(ProjectBean p: GMS3.getProjects()) {
                    if(p.getType().equals(projectType)) {
                        project = p;
                    }
                }
                
//                Thread.currentThread().sleep(500);
//                GMS3.setCurrentProject(project);
//                
//                if (Settings.DEBUG)
//                    System.out.println("Successfully loaded user's VO");
//                
//                Thread.currentThread().sleep(500);
//                GridChem.project = GMS3.getCurrentProject();
//                
//                Thread.currentThread().sleep(500);
//                GridChem.user = GMS3.getProfile();
//                
//                GridChem.userVO = GMS3.getUserVo();
                
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            
            // Start up the file browser in standalone mode
//            String basePath = "/UROOT/u/ac/ccguser/" + 
//            ((project.getType().equals(AccessType.COMMUNITY))
//                    ?"internal/":"external/") + username;
            browser = new FileBrowserImpl("");
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        myFrame.getContentPane().add(browser);
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                create();
            }
        });
    }
    
    private static void setMiddleware(String mode) {
        if (mode == null) {
            // do nothing, use production infrastructure
        } else if (mode.toLowerCase().equals("devel")) {
            Invariants.wsGateway = "http://129.114.4.7:8080/wsrf/services/GMSService";
        } else if (mode.toLowerCase().equals("failover")) {
            Invariants.wsGateway = "http://129.114.4.7:8443/wsrf/services/GMSService";
        } else if (mode.toLowerCase().equals("local")) {
            Invariants.wsGateway = "http://127.0.0.1:8080/wsrf/services/GMSService";
        } else {
            // do nothing, use production infrastructure
        } 
    }
    
}
