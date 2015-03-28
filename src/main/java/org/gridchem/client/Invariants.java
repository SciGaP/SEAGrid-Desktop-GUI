package org.gridchem.client;

import java.net.URL;

import org.gridchem.client.common.Settings;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/*Copyright (c) 2004,University of Illinois at Urbana-Champaign.  All rights reserved.

Developed by:
Chemistry and Computational Biology Group

NCSA, University of Illinois at Urbana-Champaign

http://swarna.ncsa.uiuc.edu/GridChem

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
    @version $Id: Invariants.java,v 1.6 2006/01/06 20:12:13 spamidig Exp $

    @see Settings
    @see Preferred
 */
public final class Invariants {

  /** 
      Fields useful for starting new jobs

      @see NewJob
      @see MyJFileChooser
  */
  public static final int jobNameFieldSize = 25;
  public static final int projectFieldSize = 25;
  public static final int machineFieldSize = 25;
  public static final int queueFieldSize = 25;
  public static final int inputFieldSize = 25;
  public static final int inputTextAreaWidth = 40;
  public static final int inputTextAreaHeight = 15;
  public static final int jlistEntrySize = 15;
  public static final int jlistWidth = 250;
  public static final int jlistHeight = 150;
  public static final int space = 1;

  /** Fields useful for CGI-handling classes

      @see BatchQueues
      @see SubmitJob
   */
  public static final String host = "modi4";

  public static String kerbGateway = "ccg-mw1.ncsa.uiuc.edu";
  public static String httpsGateway = "https://ccg-mw1.ncsa.uiuc.edu/cgi-bin/";
//  public static String wsGateway = "http://141.142.56.171:8080/wsrf/services/GMSService";
//  public static String wsGateway = "http://ccg-mw2.ncsa.uiuc.edu:9001/xmlrpc";
  public static String wsGateway = "http://localhost:9001/xmlrpc";
  public static String fileGateway = "http://ccg-mw2.ncsa.uiuc.edu:8080/gridchem/PersistentServlet";
  public static final String CCGRSSFeed = "http://download.gridchem.org:8668/exec/rss";
  public static final String gridchemHelpLocation = "https://www.gridchem.org/help/jhelpset.hs";
  
  public static final String XBayaJnlpURL = "http://gridchem.gateway.iu.teragrid.org/xbaya/xbaya.jnlp";
  public static final String JamberooJnlpURL = "http://dc2.apac.edu.au/~vvv900/cct/appl/jmoleditor/download/download.php?f=jamberoo.jnlp";
  public static final String JmolJnlpURL = "http://ccg-mw2.ncsa.uiuc.edu:8080/jmol/jmol.jnlp";

  // This is the old server.  It is not longer supported as of GridChem v0.1
  //public static  String kerbGateway = "swarna.ncsa.uiuc.edu";
  //public static String httpsGateway = "https://swarna.ncsa.uiuc.edu/GAUSMON/";
  
  // Is this the best location for these ? The application
  // CGI's are used only in SubmitJob.  srb aug 24 2006
  public static final String queueStatusCGI = "getqstat.cgi";
  public static final String listQueuesCGI = "getques1.cgi";
  public static final String kerberosCGI = "auth_check.cgi";
  public static final String globusCGI = "auth_myproxy.cgi";
  public static final String sshCGI = "auth_ssh.cgi";
  public static final String proxyCGI = "send_proxy_cred.cgi";
  public static final String gridchemCGI = "auth_gridchem.cgi";
  
  public static final String gauss98CGI = "gauss_launch2.cgi";
  public static final String gamessCGI = "gamess_launch.cgi";
  public static final String nwchemCGI = "nwchem_launch.cgi";
  public static final String molproCGI = "molpro_launch.cgi";
  public static final String adfCGI = "adf_launch.cgi";
  public static final String amberCGI = "amber_launch.cgi";
  public static final String aces3CGI = "aces3_launch.cgi";
  public static final String appLaunchCGI = "launch_generic_script.cgi" ;

  /** Fields useful for authentication classes &
      useful for URLEncoder.encode()

      @see LoginPanel
  */
  public static final String charEncoding = "UTF-8"; 
  public static final int loginFieldSize = 20;
  public static final int ONE_SECOND = 1000;
  public static final String globusIcon = "Images/globus-24.gif";
  public static final String noGlobusIcon = "Images/noglobus-24.gif";
  public static final String kerberosIcon = "Images/kerberos-24.gif";
  public static final String noKerberosIcon = "Images/nokerberos-24.gif";
  public static final String SSHIcon = "Images/ssh-24.gif";
  public static final String GridChemIcon = "Images/gridchem-24.gif";
  public static final String noGridChemIcon = "Images/nogridchem-24.gif";
  public static final String noSSHIcon = "Images/nossh-24.gif";
  public static final String brokenIcon = "Images/bomb24.gif";

  /***********************************
     Useful for making the GUI pretty

     @see MainPanel
   */
  public static final String logoName = "Images/1hu8_qcrjm2002.jpg";
  public static final int logoWidth = 300;
  public static final int logoHeight = 240;
  
  /***********************************
  Needed to support gridchem applications via ssh.

  @see editSSHJobPanel
  */
  // These application paths are not used.  srb Aug 24 2006
  public static final String AMBER = "/usr/local/bin/sander";
  public static final String GAUSSIAN_03 = "/usr/local/bin/g03";
  public static final String GAUSSIAN_98 = "/usr/local/bin/g98";
  public static final String GAMESS = "/usr/local/bin/qgamess";
  public static final String MOLPRO = "/usr/local/bin/mpro";
  public static final String NWCHEM = "/usr/local/bin/nwc";
  public static final String WIEN_2K = "/home1/dooley/bin/wien2k_submit";

  public static final String COMMUNITY_USER = "community";
  public static final String EXTERNAL_USER = "external";
  
  /** Fields useful for testing

  @see org.gridchem.test.SubmissionTest
  */
  public static final boolean GRIDCHEM_SUCCESS = true;
  public static final boolean GRIDCHEM_FAILURE = true;
  public static final boolean GRIDCHEM_SUBMISSION = true;

  /** 
   * Name list of Applications
   * 
   * Internal strings for the names of computational applications. 
   * These are not yet completely and consistently used.
   * srb Sep 27 2006
   * 
   * 
   * note 
   * All pre-defined strings for representing an application name is defined below. APP_NAME_XXX where XXX stands for an application.
   * Note that each string shoule be same to  "name" field of GMS database "Resources", "SoftwareResources",
   * "SoftwareInstallation"
   *     
   * Representation for an app package and modules are only supported in GUI level, which means all modules are dealt as a separate app in internal structure.    
   *     
   * The necessary mapping between a representative string for each application name and a pair of string (a package name, a module name) in GUI
   * is realized via sets of strings for module list for each app package listed below and two functions in
   * editingStuff.java (appPakcgeAndModuleName(String appName),  appName(String appPackageName, String moduleName) )  
   * (kimjh Nov. 19 2007)
  */
  
  
  // Representative string for each application program.(It should be same to "name" in Database. "Resources" table.
  public static final String APP_NAME_AMBER_SANDER = "AMBER_amber_sander";
  
  public static final String APP_NAME_DOCK = "DOCK";
  public static final String APP_NAME_AUTODOCK = "AutoDock";
  public static final String APP_NAME_NAMD = "NAMD";
  public static final String APP_NAME_MCCCSTOWHEE = "MCCCSTowhee";
  public static final String APP_NAME_GROMACS = "Gromacs";
  public static final String APP_NAME_DLPOLY = "DLPOLY";
  public static final String APP_NAME_LAMMPS = "Lammps";
  public static final String APP_NAME_TINKER = "TINKER";
  
  public static final String APP_NAME_UHBD = "UHBD";
  public static final String APP_NAME_APBS = "APBS";
  public static final String APP_NAME_ROSETTA = "ROSETTA";
  
  public static final String APP_NAME_CPMD = "CPMD";
  
  public static final String APP_NAME_GAMESS = "GAMESS";
  public static final String APP_NAME_GAMESS_XML = "Gamess-XML";
  public static final String APP_NAME_GAUSSIAN = "Gaussian";
  public static final String APP_NAME_MOLPRO = "Molpro";
  public static final String APP_NAME_NWCHEM = "NWChem";
  
  public static final String APP_NAME_WIEN2K = "WIEN2k";
  public static final String APP_NAME_DMOL3 = "DMol3";
  public static final String APP_NAME_CASTEP = "CASTEP";
  public static final String APP_NAME_ACES3 = "ACES3";
  
  public static final String APP_NAME_ADF = "ADF";
  public static final String APP_NAME_ADF_QUILD = "ADF_QUILD";  
  public static final String APP_NAME_ADF_ESR = "ADF_ESR";
  public static final String APP_NAME_ADF_DMPK = "ADF_DMPKF";
  public static final String APP_NAME_ADF_NMR = "ADF_NMR";
  
  public static final String APP_NAME_FLUENT = "fluent";
  public static final String APP_NAME_ABAQUS = "abaqus";
  
  public static final String APP_NAME_DDSCAT = "ddscat";
  
  public static final String APP_NAME_QCHEM = "Qchem";
  public static final String APP_NAME_QMCPACK = "QMCPack";
  
  public static final String APP_NAME_CRYSTAL = "Crystal";
  public static final String APP_NAME_COLUMBUS = "COLUMBUS";
  public static final String APP_NAME_NBO = "NBO";
  public static final String APP_NAME_MPQC = "MPQC";
  
  public static final String APP_NAME_QBOX = "Qbox";
  public static final String APP_NAME_GAUSSRATE = "GAUSSRATE";
  public static final String APP_NAME_GAMESOL = "GAMESOL";
  public static final String APP_NAME_POLYRATE = "POLYRATE";
  
  public static final String APP_NAME_CHARMM_MPI = "CHARMM_charmm_mpi";

  // Sets of Strings for mapping of application name and currently supported modules and each package
  // note that a module name SHOULD BE the same to 'APP_NAME_XXX" shown above except case.
  public static final String[] MODULES_AMBER = {"amber_sander","amber_ptraj", "amber_pmemd"};
  public static final String[] MODULES_ADF = {"adf","adf_quild","adf_dmpkf","adf_nmr","adf_epr"};  
  public static final String[] MODULES_GAUSSIAN = {"gaussian"};
  public static final String[] MODULES_GAMESS= {"gamess"}; 
  public static final String[] MODULES_MOLPRO = {"molpro"};
  public static final String[] MODULES_NWCHEM = {"nwchem"};
  public static final String[] MODULES_QMCPACK = {"qmcpack"};
  public static final String[] MODULES_ACES3 = {"aces3"};
  
}
