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


/* GridChem.java  by Rebecca Hartman-Baker */

package org.gridchem.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.gridchem.client.common.Settings;
import org.gridchem.client.util.Env;
import org.gridchem.client.util.GMS3;
import org.gridchem.service.beans.ComputeBean;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.ProjectBean;
import org.gridchem.service.beans.SoftwareBean;
import org.gridchem.service.beans.UserBean;
import org.gridchem.service.model.enumeration.AccessType;

@SuppressWarnings("static-access")
public class GridChem
{     // removed the job listings and refactored the submitjobswindow and job editor
//    public static JobList ListOfJobs = new JobList();
//    public static JobList ListOfJobsdone = new JobList();
    public static Settings settings;
    public static Env env;
    public static optsComponent oc;
//    public static VO userVO;
    
    public static UserBean user = null;
    public static ProjectBean project = null;
    
    public static List<JobBean> jobs = new ArrayList<JobBean>();
    public static List<ProjectBean> projects = new ArrayList<ProjectBean>();
    public static List<ComputeBean> systems = new ArrayList<ComputeBean>();
    public static List<SoftwareBean> applications = new ArrayList<SoftwareBean>();
    
    public static AccessType accessType = AccessType.COMMUNITY;
    public static String externalUsername = null;
    //public static UserVO userVO;
    
    public static Options options = new Options();
    public static String helpHeader;
    public static String customUsage;
    public static String helpFooter;
    
    public static final Option WEB_SERVICE =
        OptionBuilder.withArgName( "webservice" )
        .withDescription("Run GridChem client using CCG Web Service infrastructure")
        .withLongOpt("webservice")
        .create("w");

    public static final Option FAILOVER =
        OptionBuilder.withArgName( "failover" )
        .withDescription("Run GridChem client using failover web services")
        .withLongOpt("failover")
        .create("f");
    
    public static final Option DEVELOPMENT =
        OptionBuilder.withArgName( "development" )
        .withDescription("Run GridChem client using CCG development infrastructure")
        .withLongOpt("devel")
        .create("d");

    public static final Option LOCAL =
        OptionBuilder.withArgName( "local" )
        .withDescription("Run GridChem client using local infrastructure")
        .withLongOpt("local")
        .create("l");
    
    private static final Option HELP =
        OptionBuilder.withDescription("Displays help")
        .withLongOpt("help")
        .create("h");
    
    // lixh_add
    //public static String MyHPCsys;
    
    //Amr April 11th  Add field for resource discovery
    public static Hashtable<String,List<String>> resourceHash = new Hashtable<String,List<String>>();
    
    /**Default constructor
     * 
     */
    public GridChem() {
        init();
    }
    
	private static void init() {
        settings = Settings.getInstance();
        env = Env.getInstance();
//        SimpleHook hook = new SimpleHook();
        oc = new optsComponent();
    }
    
    public static void main (String[] args)
    {
        Trace.entry();
        
        try {
        		setCommandLineOptions();
        		parseCommandLine(args);
        } catch (Exception e) {
        		System.out.println("Invalid command line option given: " + 
        				e.getMessage());
        }
        
        //JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame();
		
		// Edited by Shashank & Sandeep @ CCS,UKY April 10 2005
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
	    /*MetalTheme theme = new G03Input.ColorTheme();  
	    MetalLookAndFeel.setCurrentTheme(theme);
	    try {
	        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	        SwingUtilities.updateComponentTreeUI(frame);
	    } catch(Exception e) {
	        System.out.println(e);
	    }*/
	   
		// Use the native look and feel since Java's is pretty lame.
	    try {
	        UIManager.setLookAndFeel(
	                UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	        // Ignore exceptions, which only result in the wrong look and feel.
	        System.out.println( "GridChem.main: an exception related to the" +
	                            " look and feel was ignored." );
	    }
		
	    init();
        
        frame.setTitle("GridChem " + Env.getVersion());
		frame.getContentPane().add(oc);
		frame.addWindowListener(new WindowListener() {

			public void windowActivated(WindowEvent arg0) {
			}

			public void windowClosed(WindowEvent arg0) {
			}

			public void windowClosing(WindowEvent arg0) {
				if (Settings.authenticated) {
					GMS3.logout();
				}
			}

			public void windowDeactivated(WindowEvent arg0) {	
			}

			public void windowDeiconified(WindowEvent arg0) {
			}

			public void windowIconified(WindowEvent arg0) {
			}

			public void windowOpened(WindowEvent arg0) {
			}
			
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		
		// Centering the frame on the screen
		Toolkit kit = frame.getToolkit();
		Dimension screenSize = kit.getScreenSize();
	    int screenWidth = screenSize.width;
	    int screenHeight = screenSize.height;
	    Dimension windowSize = frame.getSize();
	    int windowWidth = windowSize.width;
	    int windowHeight = windowSize.height;
	    int upperLeftX = (screenWidth - windowWidth)/2;
	    int upperLeftY = (screenHeight - windowHeight)/2;   
	    frame.setLocation(upperLeftX, upperLeftY);
	    frame.setVisible(true);
	    

        Trace.exit();
    }
        
    
    //Amr April 11,2005
    public static boolean isCCG_MW1Active(){
        //2005/09/06 stelios - this should always be true since now ccg-mw1 is the server -- I THINK WE SHOULD ACTUALLY REMOVE THIS CALL - TODO
        // 2005/09/06 OLD - return Invariants.httpsGateway.equalsIgnoreCase("https://ccg-mw1.ncsa.uiuc.edu/cgi-bin/");
    	 return Invariants.httpsGateway.equalsIgnoreCase(Invariants.httpsGateway);
    }
    
    //load resource description coming back from the GMS
    // Amr April 11th, 2005
    public static void loadResources(BufferedReader reader) throws IOException{
	    	String line,temp,key;
	    	line = reader.readLine();
	    	System.err.println(line);
	    	// this first line is a comma separated list of available applications
	    	StringTokenizer st;
	    	st = new StringTokenizer(line,",");
	    	ArrayList<String> apps = new ArrayList<String>();
        while (st.hasMoreTokens()){
            temp = st.nextToken();
            apps.add(temp);
        }
        //now add it to the hashTable
        GridChem.resourceHash.put("applications", apps);
        //now for each application reads its available machine
        int i;
        boolean isFirst = true;
        String application = "";
        ArrayList<String> arr;
        for (i=0;i<apps.size();i++){
	        	isFirst = true;
       		line = reader.readLine();
       		System.err.println(line);
       		//it has the following format
       		//app,machine1,machine2,machine3
       		st = new StringTokenizer(line,",");
       		arr = new ArrayList<String>();
       		while (st.hasMoreTokens()){
                temp = st.nextToken();
                if(isFirst){
                	isFirst =false;
                	application = temp; 
                }else{
                	arr.add(temp);
                	//for faster access add application_machine key
                	key = application +"_"+temp;
                	//System.err.println("Adding  "+ key );
                	GridChem.resourceHash.put(key,Arrays.asList("1"));
               }
       		}
       		//now add this array list to the hash table
       		GridChem.resourceHash.put(application,arr);
       		//for(i=0;i<arr.size();i++){
       		//	System.err.println((String)arr.get(i));
       		//}
        }
        
        //now lets read the list of machines
        line = reader.readLine();
        System.err.println(line);
        // this first line is a comma separated list of available machines
        st = new StringTokenizer(line,",");
        ArrayList<String> machines = new ArrayList<String>();
        while (st.hasMoreTokens()){
            temp = st.nextToken();
            machines.add(temp);
        }
        //now add it to the hashTable
        GridChem.resourceHash.put("machines", machines);
        
        //now read the available queues for each machine
        // followed by the available projects
        String machine="";
        for (i=0;i<machines.size();i++){
	        	isFirst = true;
	        	line = reader.readLine();
	        	System.err.println(line);
	        	if(Settings.DEBUG) System.err.println(line);
      		//it has the following format
       		//app,machine1,machine2,machine3
       		st = new StringTokenizer(line,",");
       		arr = new ArrayList<String>();
       		while (st.hasMoreTokens()){
                temp = st.nextToken();
                if(isFirst){
                	isFirst =false;
                	machine = temp; 
                }else{
                	arr.add(temp);
               }
       		}
       		//now add this array list to the hash table
       		GridChem.resourceHash.put(machine+"_"+"queues",arr);
       		
       		//Now get the list of available projects/allocations
       		line = reader.readLine();
       		System.err.println(line);
       		st = new StringTokenizer(line,",");
       		arr = new ArrayList<String>();
       		while (st.hasMoreTokens()){
                temp = st.nextToken();
                arr.add(temp);
       		}
       		//now add this array list to the hash table
       		GridChem.resourceHash.put(machine+"_"+"projects",arr);
        }
    }
    
    /**
     * Static utility class to parse resource list passed back from GMS.
     * This is done all wrong.  List should be parsed into a VO object which
     * contains a list of CCGResources.  CCGResource may then be populated with
     * info.  This makes querying easier as well as mapping the (eventual) XML
     * returned to objects. 
     * 
     * That being said, for now, the service returns the same thing in the same
     * format as the CGI.  An example return string is below:
     * 
     [java] Gaussian,GAMESS,NWChem,Molpro
     [java] Gaussian,tun.ncsa.uiuc.edu,cu.ncsa.uiuc.edu,ccg-login.ncsa.uiuc.edu,ccg-login.epn.osc.edu,tg-login1.osc.edu,mike4.cct.lsu.edu,Any,radium.ncsa.uiuc.edu,sdx.uky.edu
     [java] GAMESS,tun.ncsa.uiuc.edu,cu.ncsa.uiuc.edu,longhorn.tacc.utexas.edu,ccg-login.ncsa.uiuc.edu,ccg-login.epn.osc.edu,tg-login1.osc.edu,mike4.cct.lsu.edu,radium.ncsa.uiuc.edu,sdx.uky.edu
     [java] NWChem,cu.ncsa.uiuc.edu,longhorn.tacc.utexas.edu,ccg-login.ncsa.uiuc.edu
     [java] Molpro,cu.ncsa.uiuc.edu,ccg-login.ncsa.uiuc.edu
     [java] tun.ncsa.uiuc.edu,cu.ncsa.uiuc.edu,longhorn.tacc.utexas.edu,ccg-login.ncsa.uiuc.edu,ccg-login.epn.osc.edu,tg-login1.osc.edu,mike4.cct.lsu.edu,Any,radium.ncsa.uiuc.edu,sdx.uky.edu
     [java] tun.ncsa.uiuc.edu,normal
     [java] lhr
     [java] cu.ncsa.uiuc.edu, batch,debug
     [java] lhr
     [java] longhorn.tacc.utexas.edu, normal
     [java] gridchem
     [java] ccg-login.ncsa.uiuc.edu, gridchem,debug
     [java] lhr
     [java] ccg-login.epn.osc.edu, agt_pbs
     [java] gaussian
     [java] tg-login1.osc.edu, parallel,serial,altix,smp,bigmem
     [java] gaussian
     [java] mike4.cct.lsu.edu, workq,debug
     [java] gaussian
     [java] Any, NA
     [java] NA
     [java] radium.ncsa.uiuc.edu, condor
     [java] lhr
     [java] sdx.uky.edu, gauss
     [java] lhr
     * 
     * @param resources
     */
    public static void parseResources(String resources) {
    		/* get arraylist of apps, add with hash "applications"
    		 * get arraylist of application_machine combination, add with hash of app name
    		 * get arraylist of machines, add with has "machines"
    		 * get arraylist of machine_queues combination, add with hash of machine_queue values
    		 * get arraylist of machine_projects combination, add with hash of machine_project values
    		 */
    		//GridChem.resourceHash.put("applications", apps);
    		//GridChem.resourceHash.put(application,arr);
    		//GridChem.resourceHash.put("machines", machines);
    		//GridChem.resourceHash.put(machine+"_"+"queues",arr);
    		//GridChem.resourceHash.put(machine+"_"+"projects",arr);
	    	String line,temp,key;
	    	GridChem.resourceHash.clear();
	    	int lineCount = 0;
	    	line = resources.split("\n")[lineCount++];
	    	if (Settings.DEBUG) 	System.err.println(line);
	    	// this first line is a comma separated list of available applications
	    	StringTokenizer st;
	    	st = new StringTokenizer(line,",");
	    	ArrayList<String> apps = new ArrayList<String>();
	    while (st.hasMoreTokens()){
	        temp = st.nextToken();
	        apps.add(temp);
	    }
	    //now add it to the hashTable
	    GridChem.resourceHash.put("applications", apps);
	    //now for each application reads its available machine
	    int i;
	    boolean isFirst = true;
	    String application = "";
	    ArrayList<String> arr;
	    for (i=0;i<apps.size();i++){
	        	isFirst = true;
	        	line = resources.split("\n")[lineCount++];
	        	if (Settings.DEBUG) System.err.println(line);
	   		//it has the following format
	   		//app,machine1,machine2,machine3
	   		st = new StringTokenizer(line,",");
	   		arr = new ArrayList<String>();
	   		while (st.hasMoreTokens()){
	            temp = st.nextToken();
	            if(isFirst){
	            	isFirst =false;
	            	application = temp; 
	            }else{
	            	arr.add(temp);
	            	//for faster access add application_machine key
	            	key = application +"_"+temp;
	            	//System.err.println("Adding  "+ key );
	            	GridChem.resourceHash.put(key,Arrays.asList("1"));
	           }
	   		}
	   		//now add this array list to the hash table
	   		GridChem.resourceHash.put(application,arr);
	   		//for(i=0;i<arr.size();i++){
	   		//	System.err.println((String)arr.get(i));
	   		//}
	    }
	    
	    //now lets read the list of machines
	    line = resources.split("\n")[lineCount++];
	    if (Settings.DEBUG) System.err.println(line);
	    // this first line is a comma separated list of available machines
	    st = new StringTokenizer(line,",");
	    ArrayList<String> machines = new ArrayList<String>();
	    while (st.hasMoreTokens()){
	        temp = st.nextToken();
	        machines.add(temp);
	    }
	    //now add it to the hashTable
	    GridChem.resourceHash.put("machines", machines);
	    
	    //now read the available queues for each machine
	    // followed by the available projects
	    String machine="";
	    for (i=0;i<machines.size();i++){
	        	isFirst = true;
	        	line = resources.split("\n")[lineCount++];
	        	if(Settings.DEBUG) System.err.println(line);
	  		//it has the following format
	   		//app,machine1,machine2,machine3
	   		st = new StringTokenizer(line,",");
	   		arr = new ArrayList<String>();
	   		while (st.hasMoreTokens()){
	            temp = st.nextToken();
	            if(isFirst){
	            	isFirst =false;
	            	machine = temp; 
	            }else{
	            	arr.add(temp);
	           }
	   		}
	   		//now add this array list to the hash table
	   		GridChem.resourceHash.put(machine+"_"+"queues",arr);
	   		
	   		//Now get the list of available projects/allocations
	   		line = resources.split("\n")[lineCount++];
	   		if(Settings.DEBUG) System.err.println(line);
	   		st = new StringTokenizer(line,",");
	   		arr = new ArrayList<String>();
	   		while (st.hasMoreTokens()){
	            temp = st.nextToken();
	            arr.add(temp);
	   		}
	   		//now add this array list to the hash table
	   		GridChem.resourceHash.put(machine+"_"+"projects",arr);
	    }
    }
    
    //Amr April 11,2005
    public static boolean machineContainsApplication(String machine, String application){
	    	if(!GridChem.isCCG_MW1Active()){
	    		return true; 
	    	} else {
	    		String key = application+ "_" + machine;
	    		return (GridChem.resourceHash.get(key) != null);
	    	}
    }
    
    //Amr April 11,2005
    // Note that this method is not used in the web_service version
    // and presumably will be removed during refactoring.  srb aug 24 2006
    public static String[] getAvailableApplications() {
        if(GridChem.isCCG_MW1Active()) {
            ArrayList<String> arr= (ArrayList<String>) GridChem.resourceHash.get("applications");
            int i;
            String [] tmp = new String[arr.size()];
            for(i=0;i<arr.size();i++) {
                tmp[i]=(String)arr.get(i);
            }
            return tmp;
        } else {
            String [] tmp = {"Gaussian", "GAMESS","NWChem","Molpro", "Amber" };
            return tmp;
        }
    }
    
    //Amr April 11,2005
    public static String[] getMachineQueues(String machine){
	    	if(GridChem.isCCG_MW1Active()){
      		ArrayList<String> arr= (ArrayList<String>) GridChem.resourceHash.get(
      				machine + "_" + "queues");
      		int i;
      		String [] tmp = new String[arr.size()];
      		for(i=0;i<arr.size();i++) {
      			tmp[i]=(String)arr.get(i);
      		}
	        	return tmp;
	    	} else {
      		String [] tmp = {"debug","batch","dedicated","normal","standard","gridchem","workq"};
      		return tmp;
	    	}    	
    }
   
    //Amr April 11,2005
    public static ArrayList<String> getMachineQueuesList(String machine){
    		return (ArrayList<String>) GridChem.resourceHash.get(machine+"_"+"queues");
    }
   
    //Amr April 11,2005
    public static String[] getMachineProjects(String machine){
    		System.out.println("Getting projects for machine: " + machine + "\n");
	    	if(GridChem.isCCG_MW1Active()){
	    		Enumeration<String> keys = GridChem.resourceHash.keys();
	    		System.out.println("Keys are: \n");
	    		while (keys.hasMoreElements()){
	    			System.out.println("\t" + keys.nextElement() + "\n");
	    		}
	    		
      		ArrayList<String> arr= (ArrayList<String>) GridChem.resourceHash.get(
      				machine + "_" + "projects");
      		int i;
      		String [] tmp = new String[arr.size()];
      		for(i=0;i<arr.size();i++) {
      			tmp[i]=(String)arr.get(i);
      		}
	        	return tmp;
	    	} else {
			String [] tmp = {"mjk", "gaussian","A-gridchem"};
			return tmp;
	    	}    	
    }
    
    //Amr April 11,2005
    public static ArrayList<String> getMachineProjectsList(String machine){
    		return (ArrayList<String>) GridChem.resourceHash.get(machine+"_"+"projects");
    }
    
    public static List<ComputeBean> getMachineList(){
		return project.getSystems();
    }
    
    public static ComputeBean getMachine(String name) {
        ComputeBean hpc = null;
        
        for(ComputeBean machine: project.getSystems()) {
            if (machine.getHostname().equals(name)) {
                hpc = machine;
                break;
            }
        }
        
        return hpc;
    }
    
    public static ArrayList<ComputeBean> getSoftwareMachineList(String application) {
        ArrayList<ComputeBean> machineList = new ArrayList<ComputeBean>();
        
        for (ComputeBean hpc : project.getSystems()) {
            for (SoftwareBean sw : hpc.getSoftware()) {
                if (sw.getName().equalsIgnoreCase(application) && 
                        !machineList.contains(hpc.getName())) {
                    machineList.add(hpc);
                    break;
                }
            }
        }
        
        return machineList;
    }
    
    public static ArrayList<SoftwareBean> getSoftware() {
        ArrayList<SoftwareBean> apps = new ArrayList<SoftwareBean>();

        for (ComputeBean hpc : project.getSystems()) {
            for (SoftwareBean sw : hpc.getSoftware()) {
                if (!apps.contains(sw)) {
                    apps.add(sw);
                }
            }
        }
        
        return apps;
    }
    
    public static SoftwareBean getSoftware(String name) {
        for (ComputeBean hpc : project.getSystems()) {
            for (SoftwareBean sw : hpc.getSoftware()) {
                if (sw.getName().equals(name)) {
                    return sw;
                }
            }
        }
        
        return null;
    }
    
    public static List<String> getSoftwareNames() {
        ArrayList<String> appNames = new ArrayList<String>();
        
        for (ComputeBean hpc : project.getSystems()) {
            for (SoftwareBean sw : hpc.getSoftware()) {
                if (!appNames.contains(sw.getName().toUpperCase())) {
                    appNames.add(sw.getName().toUpperCase());
                }
            }
        }
        
        return appNames;
    }
    
    public static ArrayList<SoftwareBean> getSoftwareforMachine(String machine) {
        ArrayList<SoftwareBean> apps = new ArrayList<SoftwareBean>();
        
        for (ComputeBean hpc : project.getSystems()) {
            if (hpc.getName().equals(machine)) {
                for (SoftwareBean sw : hpc.getSoftware()) {
                    if (!apps.contains(sw)) {
                        apps.add(sw);
                    }
                }
            }
        }
        
        return apps;
    }
    
    public static ComputeBean getMachineByName(String name) {
        ComputeBean hpc = null;
        
        for(ComputeBean machine: project.getSystems()) {
            if (machine.getName().equals(name)) {
                hpc = machine;
            }
        }
        
        return hpc;
    }
    
    /**
     * Append the passed message to the main text box on the GridChem panel.
     * 
     * @param message String to append to the main GridChem panel
     */
    public static void appendMessage(String message) {
        oc.messageBoard.append(message + "\n");
        oc.messageBoard.setCaretPosition(
                oc.messageBoard.getDocument().getLength());
    }

    /**
     * Utility class to parse the command line.  Options are final static constants
     * defined in this class.  We use the apache command line tools to parse the 
     * command line used to run this app against the set of known options.  Appropriate
     * action is taken accordingly.
     * 
     * @param args
     * @throws Exception
     */
    private static void parseCommandLine(String[] args) throws Exception {
    		Trace.entry();
		
    		CommandLineParser parser = new PosixParser();
    		CommandLine line = parser.parse(options, args);

            Settings.WEBSERVICE = true;
            
            // all command line flags are exclusive. only one will be
            // accepted at a time.
    		if (line.getArgList().size() < 0) {
    			Settings.DEVEL = false;
    			Settings.FAILOVER = false;
    			Settings.DEBUG = false;
                Settings.LOCAL = false;
    			Trace.note("Using vanilla version of client");
    			return;
    		} else if (line.hasOption("f")) {
    			Settings.FAILOVER = true;
    			// change to the derrick or uky server
    			Invariants.httpsGateway = "https://derrick.tacc.utexas.edu/cgi-bin/";
    			Invariants.kerbGateway = "https://derrick.tacc.utexas.edu/cgi-bin/";
                Invariants.wsGateway = "http://129.114.4.7:8443/wsrf/services/GMSService";
    			Trace.note("Using development version of client");
    		} else if (line.hasOption("d")) {
 /*   			Settings.DEVEL = true;
    			Invariants.httpsGateway = Invariants.httpsGateway + "devel/";
    			Invariants.kerbGateway = Invariants.kerbGateway + "devel/";
    			Invariants.wsGateway = "http://129.114.4.7:8080/wsrf/services/GMSService";
    			Trace.note("Using development version of client");*/
    			Settings.DEVEL = true;
    			Invariants.httpsGateway = "https://ccg-mw2.ncsa.uiuc.edu/cgi-bin/devel/";
    			Invariants.kerbGateway = Invariants.kerbGateway + "devel/";
    			Invariants.wsGateway = "http://ccg-mw2.ncsa.uiuc.edu:8080/wsrf/services/GMSService";
    			Trace.note("Using development version of client");
    		} else if (line.hasOption("l")) {
                Settings.LOCAL = true;
                Invariants.wsGateway = "http://127.0.0.1:8080/wsrf/services/GMSService";
                Trace.note("Using development version of client with local services");
            } else if (line.hasOption("h")) {
    			displayUsage();
    			System.exit(0);
    		} else {
    			// ccg-mw1.ncsa.uiuc.edu as production server
    			Invariants.wsGateway = "http://141.142.56.171:8080/wsrf/services/GMSService";
                Trace.note("Using web service production version of client");
    		}
    	
    		if (line.hasOption("v")) {
    			Env.setVersion(line.getOptionValue("v"));
    		}
    		
    		Trace.exit();
    }
    
    /**
     * Utility class to set command line options.  Current options are
     * webservice (w,webservice)
     * failover (f,failover)
     * development (d,devel)
     */
    private static void setCommandLineOptions() {
    		options.addOption(WEB_SERVICE);
    		options.addOption(FAILOVER);
    		options.addOption(DEVELOPMENT);
            options.addOption(HELP);
            options.addOption(LOCAL);
            options.addOption("v", "version", true, "GridChem Client Version");
    }
    
    public static void displayUsage() {
        String usage = "java org.gridchem.client.GridChem" +
                       " -[w|f|d|l|h]";

        usage = (customUsage == null) ? usage : usage + customUsage;

        String header = (helpHeader == null) ?
                        "Options:" : helpHeader;
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(usage, header, options, null, false);
        if (helpFooter != null) {
            System.out.println(helpFooter);
        }
    }

}

// Moved to optsComponent.java @ CCS,UKy

    