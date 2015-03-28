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

/** @author John J. Lee, NCSA
    @version $Id: Settings.java,v 1.18 2006/02/03 02:11:25 kyriacou Exp $
    @see Preferred

    Settings should contain public static fields that are 
    routinely mutated in the course of running the application.
    Many of the public final fields should migrate to Preferred.

 */

package org.gridchem.client.common;

import java.awt.Dimension;
import java.io.File;
import java.net.URI;
import java.util.Properties;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.gridchem.client.JobList;
import org.gridchem.client.Preferred;
import org.gridchem.client.Trace;
import org.gridchem.client.util.Env;
import org.gridchem.service.beans.UserBean;
import org.gridchem.service.model.enumeration.AccessType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Settings {

  /******************************
     Settings used for debugging 
   */
  public static boolean DEBUG = true;
  public static boolean VERBOSE = true;
  public static boolean FAILOVER = false;
  public static boolean WEBSERVICE = false;
  public static boolean DEVEL = false;  // whether a -devel flag was given or not
  public static boolean LOCAL = false;
  // length of session with GMS
  public static final int SESSION_LENGTH_MINUTES = 120;
  
  // number of minutes in advance of timeout to warn the user
  public static final int SESSION_MINUTE_WARNING_BEFORE_TIMEOUT = 5;
  
  /**************************
      Settings used by Swing
   */
  public static final int INTRAPANEL_SPACING = 8;

  // used, e. g., in ParseSCF2; doesn't seem to make much difference
  public static final Dimension TABLE_VIEWPORT = new Dimension(60, 40);

  public static final Dimension FRAME_DIM = new Dimension(924, 668);

  // divider is aligned vertically:
  public static final int HORI_DIVIDER_LOC = 250;

  // divider is aligned horizontally:
  public static final int VERT_DIVIDER_LOC = 250;

  public static final Dimension MIN_SIZE = new Dimension(50, 50);

  // adjust controlpanel sizes here...
  public static final Dimension CP_SCROLLPANE_DIM = new Dimension(600, 86);
  public static final Dimension CP_SCROLLPANEPLUS_DIM = new Dimension(600, 130);
  public static final Dimension BOX_RIGIDAREA = new Dimension(0, 8);
  public static final int REFRESH_RATE = 120;
  
  ///public static ArrayList previousAccountIDs;

  /******************************
     Settings for authentication
   */
  public static String authMethodSelection = "<unassigned>";
  public static boolean authenticated = false;
  public static JTextField name;
  public static String resproj;
  public static String mss;
  public static boolean nameOK = false; 
  public static boolean authenticatedKerberos = false;
  public static boolean authenticatedGlobus = false;
  public static boolean authenticatedSSH = false;
  public static boolean authenticatedGridChem = false;
//  public static Session session; 			// session for ssh access
//  public static Channel channel; 			// channel for ssh communication
  public static Integer sshPort;			// default port for ssh communication
  public static JTextField sshhost; 		// remote hostname for ssh
  public static JPasswordField pass; 		// password for ssh access
  public static String identityFile;		// ssh identity file
  public static JTextField myproxyServer; 	// myproxy server which to use
  public static JTextField myproxyUsername; // myproxy username
  public static JTextField myproxyTag;		// tag of credential in myproxy server
  public static JPasswordField myproxyPass;	// myproxy pass phrase
  public static String gridchemusername; // GridChem user name
  public static String defaultMyproxyServer;
  public static int defaultMyproxyPort = 7512;
  public static JTextField kerberosRealm;          //Kerberos realm
  public static AccessType userType; 		// community or external
//  public static GlobusCredential communityGlobusCred;		// community credential
  
  public static UserBean user;
  
  /******************************
     Settings for I/O
   */
  public static String defaultDirStr = ""; 
  public static File defaultDirFile; // directory in which app. was launched
  public static File defaultTestFile; 
  public static String fileSeparator;
  public static String gmsCertificateName = "ccg_mw1.cer";
  public static String logoName = "ccglogo1.jpg";
  public static String globusClientWsddFilename = "client-config.wsdd";
  public static String nanocaddataFile = "nanocaddata.zip";
  public static String nanocadTextFile = "txt.zip";
  public static String vibrationFile = "vibrational_analysis.zip";
  public static String rasmacFile = "RasMac_PPC_32BIT";
  public static String rasmacFile1 = "RasMac_PPC_32BIT.bin";
  public static String rasmacFile2 = "rasmol_32BIT";
  public static String rasmacFile3 = "rasmol.hlp";
  public static String xbaya_proxy = "xbaya_proxy"; //-nik added for xbaya
  
  public static XStream xstream = new XStream(new DomDriver());
  /******************************
     Settings for jobs
   */
  public static URI defaultDirURI;
  public static Properties resourceProperties;

 /**************************
  * Settings for job list
  */
  public static JobList loj;
  public static String jobDir = "";

 /**************************
  * Settings for hist file
  */
  public static String histFilename;
  public static String jobHistFilename = "job.hist";
  public static String localhist = "qcrjm.hist";
  
  private Settings() {
    Trace.entry();
    fileSeparator = System.getProperty("file.separator");
    
    defaultDirStr = Env.getUserDataDir();
    
    Trace.note( "defaultDirStr = " + defaultDirStr );
    
    Preferences  preferences = Preferences.getInstance();
    Properties resourceProperties = new Properties();
    
    
    if (Preferences.getString("user_data_directory") != null &&
            !Preferences.getString("user_data_directory").equals("")) {
        defaultDirStr = Preferences.getString("user_data_directory");
    }
    
    defaultDirFile = new File(defaultDirStr);
    defaultTestFile = new File(defaultDirStr + 
			       fileSeparator + "DotCom" +
			       fileSeparator + "test000.com");
    defaultDirURI = defaultDirFile.toURI();
    histFilename = defaultDirStr + fileSeparator  + localhist;
    
    // KLUDGE:  do a little housekeeping that avoids run-time problems
    defaultMyproxyServer = Preferences.getString("myproxy_server");
    userType = ((Preferences.getString("gridchem_usertype") == null?AccessType.COMMUNITY:
        (Preferences.getString("gridchem_usertype").equals("COMMUNITY"))?
            AccessType.COMMUNITY : AccessType.EXTERNAL));
    
    if (Preferences.getString("ssh_port") != null &&
            !Preferences.getString("ssh_port").equals("")) {
        sshPort = new Integer(Preferences.getString("ssh_port"));
    } else {
        sshPort = new Integer(22);
    }
    
    name = new JTextField();
    if (Preferences.getString("ssh_port") != null &&
            !Preferences.getString("ssh_port").equals("")) {
        name.setText(Preferences.getString("gridchem_username"));
    } else {
        name.setText(Preferences.getString(System.getProperty("user.name")));
    }
    
    if (Preferences.getString("ssh_port") != null &&
            !Preferences.getString("ssh_port").equals("")) {
        resproj = new String(Preferences.getString("research_project_name"));
    } else {
        resproj = new String(System.getProperty("user.name") + "_proj");
    }
    
    if (Preferences.getString("ssh_port") != null &&
            !Preferences.getString("ssh_port").equals("")) {
        mss = new String(Preferences.getString("mass_storage"));
    } else {
        mss = "mss.ncsa.uiuc.edu";
        
    }
    
    loj = new JobList();
    user = new UserBean();
    
    xstream = initXStream();
     
    // Set the truststore system variable to let jvm know to use the cg keystore
    // for all ssl communication.
    // System.setProperty("javax.net.ssl.trsstStore",Env.getTrustStoreLocation());
    Trace.exit();
  }

  public static Settings getInstance() {
    return new Settings();
  }

  public void getSettingsFromFiles() {
    ///previousAccountIDs;
  }
  
  private XStream initXStream() {
      
      String defaultFormat = "MMM d, yyyy h:mm:ss a";
      
      String[] acceptableFormats = new String[] {
              "MM/dd/yyyy HH:mm:ss",
              "yyyy-MM-dd HH:mm:ss.S z",
              "yyyy-MM-dd HH:mm:ss.S a",
              "yyyy-MM-dd HH:mm:ssz",
              "yyyy-MM-dd HH:mm:ss z", // JDK 1.3 needs both prev versions
              "yyyy-MM-dd HH:mm:ssa" }; // backwards compatability

      xstream.registerConverter(new DateConverter(defaultFormat, acceptableFormats));
      
//      createAliases();
      
      return xstream;
  }
  
  /**
   * Create the alias mapping from via xstream from the xml to java objects.
   * These resources were created in the service are are leverage through 
   * the jar of the gms service here in the client.
   */
//  private void createAliases() {
//      // Bean objects
//      xstream.alias("VO",org.gridchem.service.beans.VO.class);
//      xstream.alias("FileBean",org.gridchem.service.beans.FileBean.class);
//      xstream.alias("ComputeBean",org.gridchem.service.beans.ComputeBean.class);
//      xstream.alias("JobBean",org.gridchem.service.beans.JobBean.class);
//      xstream.alias("ProjectBean",org.gridchem.service.beans.ProjectBean.class);
//      xstream.alias("QueueBean",org.gridchem.service.beans.QueueBean.class);
//      xstream.alias("SoftwareBean",org.gridchem.service.beans.SoftwareBean.class);
//      xstream.alias("UsageBean",org.gridchem.service.beans.UsageBean.class);
//      xstream.alias("UserBean",org.gridchem.service.beans.UserBean.class);
//      xstream.alias("LogicalFileBean",org.gridchem.service.beans.LogicalFileBean.class);
//
//      // Resource objects
//      xstream.alias("CCGResource",org.gridchem.service.gms.model.resource.CCGResource.class);
//      xstream.alias("ComputeResource",org.gridchem.service.gms.model.resource.ComputeResource.class);
//      xstream.alias("SoftwareResource",org.gridchem.service.gms.model.resource.SoftwareResource.class);
//      xstream.alias("StorageResource",org.gridchem.service.gms.model.resource.StorageResource.class);
//      xstream.alias("NetworkResource",org.gridchem.service.gms.model.resource.NetworkResource.class);
//      xstream.alias("VisualizationResource",org.gridchem.service.gms.model.resource.VisualizationResource.class);
//      xstream.alias("Queue",org.gridchem.service.gms.model.resource.ProjectStatus.class);
//      xstream.alias("Queue",org.gridchem.service.gms.model.resource.Queue.class);
//      xstream.alias("QueueStatus",org.gridchem.service.gms.model.resource.QueueStatus.class);
//      xstream.alias("ResourceStatus",org.gridchem.service.gms.model.resource.ResourceStatus.class);
//      xstream.alias("ResourceType",org.gridchem.service.gms.model.resource.ResourceType.class);
//      xstream.alias("Site",org.gridchem.service.gms.model.resource.Site.class);
//      xstream.alias("SoftwareInstallation",org.gridchem.service.gms.model.resource.SoftwareInstallation.class);
//      xstream.alias("SponsorType",org.gridchem.service.gms.model.resource.SponsorType.class);
//      xstream.alias("Project",org.gridchem.service.gms.model.resource.Project.class);
//      xstream.alias("Load",org.gridchem.service.gms.model.resource.Load.class);
//      xstream.alias("Usage",org.gridchem.service.gms.model.resource.Usage.class);
//      xstream.alias("UserProjectResource",org.gridchem.service.gms.model.UserProjectResource.class);
//      
//      // Job objects
//      xstream.alias("Job",org.gridchem.service.gms.model.job.Job.class);
//      xstream.alias("JobStatus",org.gridchem.service.gms.model.job.JobStatus.class);
//      xstream.alias("Allocation",org.gridchem.service.gms.model.job.Allocation.class);
//      
//      // User objects
//      xstream.alias("AccessType",org.gridchem.service.gms.model.user.AccessType.class);
//      xstream.alias("AccountStatus",org.gridchem.service.gms.model.user.AccountStatus.class);
//      xstream.alias("Address",org.gridchem.service.gms.model.user.Address.class);
//      xstream.alias("AdminType",org.gridchem.service.gms.model.user.AdminType.class);
//      xstream.alias("DN",org.gridchem.service.gms.model.user.DN.class);
//      xstream.alias("Preferences",org.gridchem.service.gms.model.user.Preferences.class);
//      xstream.alias("ResearchStatus",org.gridchem.service.gms.model.user.ResearchStatus.class);
//      xstream.alias("SecurityType",org.gridchem.service.gms.model.user.SecurityType.class);
//      xstream.alias("User",org.gridchem.service.gms.model.user.User.class);
//      xstream.alias("UserStatus",org.gridchem.service.gms.model.user.UserStatus.class);
//      
//      //java objects
//      xstream.alias("SQL-Date",java.sql.Date.class);
//      xstream.alias("SQL-Time", java.sql.Time.class);
//  }
   
}
