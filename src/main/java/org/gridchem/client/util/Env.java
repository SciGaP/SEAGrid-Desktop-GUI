/*
 * The University of Wales, Cardiff Triana Project Software License (Based
 * on the Apache Software License Version 1.1)
 *
 * Copyright (c) 2003 University of Wales, Cardiff. All rights reserved.
 *
 * Redistribution and use of the software in source and binary forms, with
 * or without modification, are permitted provided that the following
 * conditions are met:
 *
 * 1.  Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 * 2.  Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any,
 *    must include the following acknowledgment: "This product includes
 *    software developed by the University of Wales, Cardiff for the Triana
 *    Project (http://www.trianacode.org)." Alternately, this
 *    acknowledgment may appear in the software itself, if and wherever
 *    such third-party acknowledgments normally appear.
 *
 * 4. The names "Triana" and "University of Wales, Cardiff" must not be
 *    used to endorse or promote products derived from this software
 *    without prior written permission. For written permission, please
 *    contact triana@trianacode.org.
 *
 * 5. Products derived from this software may not be called "Triana," nor
 *    may Triana appear in their name, without prior written permission of
 *    the University of Wales, Cardiff.
 *
 * 6. This software may not be sold, used or incorporated into any product
 *    for sale to third parties.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN
 * NO EVENT SHALL UNIVERSITY OF WALES, CARDIFF OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ------------------------------------------------------------------------
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Triana Project. For more information on the
 * Triana Project, please see. http://www.trianacode.org.
 *
 * This license is based on the BSD license as adopted by the Apache
 * Foundation and is governed by the laws of England and Wales.
 */
package org.gridchem.client.util;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import nanocad.ZipExtractor;

import org.apache.log4j.Logger;
import org.gridchem.client.Trace;
import org.gridchem.client.common.Settings;

/**
 * Env allows a way of accessing various environment and system variables within
 * Triana. Some methods convert from the Java system properties to suitable
 * labels which are of more use within Triana. To get the particular variable
 * you want just call any of these functions from within any unit e.g. :-</p>
 * <p/>
 * <center> Env.trianahome() </center>
 * <p>
 * returns triana's home directory.
 * </p>
 * <p>
 * <p/>
 * The toString() method is also useful for identifying all the system
 * properties at run-time to make sure your system is set up correctly. This
 * message is printed out Triana or Triana is used.
 * 
 * @author Ian Taylor
 * @author Rion Dooley < dooley [at] cct [dot] lsu [dot] edu >
 * @version $Revision: 1.24 $
 * @created 9 Oct 1997
 * @date $Date: 2006/01/31 20:45:28 $ modified by $Author: kyriacou $
 */
public final class Env {

	static Logger logger = Logger.getLogger(Env.class);

	public static String TAB = "    ";

	public static final String DEFAULT_HISTORY_CLIPIN = "triana.types.clipins.DefaultHistoryClipIn";

	public static int BIG = 0;
	public static int LITTLE = 1;
	public static int endian = LITTLE; // windows machine by default ....

	private static boolean passwordOK = false;
	private static boolean runOut = false;
	// private static final String version = "0.2alpha";
	public static String version;
	private static final String verName = "GridChem";
	public static String CONFIG_VERSION = "1.0.0";

	private static Vector classpaths;
	private static Vector helpdirs;
	public static boolean isAnApplet = false;

	// property names and values
	public static String CONFIG_STR = "config";
	public static String CONFIG_VERSION_STR = "version";
	public static String TOOLBOXES_STR = "toolboxes";
	public static String TOOLBOX_STR = "toolbox";
	public static String TOOLBOX_TYPE_STR = "type";
	public static String COLORS_STR = "colors";
	public static String COLOR_STR = "color";
	public static String TYPE_STR = "type";
	public static String NAME_STR = "name";
	public static String VALUE_STR = "value";
	public static String RED_STR = "red";
	public static String GREEN_STR = "green";
	public static String BLUE_STR = "blue";
	public static String COMPILER_STR = "compiler";
	public static String CLASSPATH_STR = "classpath";
	public static String OPTIONS_STR = "options";
	public static String OPTION_STR = "option";
	public static String CODE_EDITOR_STR = "code_editor";
	public static String HELP_EDITOR_STR = "help_editor";
	public static String HELP_VIEWER_STR = "help_viewer";
	public static String HISTORY_TRACK = "history_track";
	public static String POPUP_DESC_STR = "popup_desc";
	public static String EXTENDED_POPUP = "extended_popup";
	public static String NODE_EDIT_ICONS = "node_edit_icons";
	public static String DEBUG_STR = "debug";
	public static String CONVERT_TO_DOUBLE_STR = "convert_to_double";
	public static String NONBLOCKING_OUT_STR = "nonblocking_out";
	public static String TIP_STR = "show_tip";
	public static String TIP_NUM_STR = "tip_number";
	public static String STATE_STR = "state_files";
	public static String OPEN_GROUPS = "open_groups";
	public static String FILES_STR = "files";
	public static String RECENT_STR = "recent";
	public static String FILE_STR = "file";
	public static String PARENT_STR = "parent";
	public static String CHILD_STR = "child";
	public static String DIRECTORY_STR = "directory";
	public static String WINDOW_POSITION_STR = "window_position";
	public static String WINDOW_SIZE_STR = "window_size";
	public static String DEBUG_VISIBLE_STR = "debug_visible";
	public static String DEBUG_POSITION_STR = "debug_position";
	public static String DEBUG_SIZE_STR = "debug_size";
	public static String ZOOM_FACTOR_STR = "zoom_factor";
	public static String FILE_READERS_STR = "file_readers";
	public static String FILE_WRITERS_STR = "file_writers";
	public static String READER_STR = "reader";
	public static String WRITER_STR = "writer";
	public static String FILE_EXTENSION_STR = "file_ext";
	public static String TOOL_NAME_STR = "tool_name";

	// directory types
	public static String DATA_DIRECTORY = "data";
	public static String TASKGRAPH_DIRECTORY = "taskgraph";
	public static String TOOL_DIRECTORY = "tool";
	public static String TOOLBOX_DIRECTORY = "toolbox";
	public static String UNIT_DIRECTORY = "unit";
	public static String COMPILER_DIRECTORY = "compiler";

	// default triana application size
	public static Dimension defaultsize = new Dimension(800, 600);

	/**
	 * the default number of recent items to remember
	 */
	private static int RECENT_ITEM_COUNT = 10;

	private static Hashtable options = new Hashtable(); // name, value pairs
	private static Vector recentFileItems = new Vector(10);
	private static Vector savedTaskgraphs = new Vector();

	/**
	 * Color Table Definitions
	 * 
	 * @todo this should replace the defunct cable colour stuff unless we are
	 *       going to reinstate
	 */
	private static Vector colorTableEntries = new Vector();
	public static final String COLOR_TABLE_STR = "colorTable";
	public static final String COLOR_TABLE_ENTRY_STR = "colorTableEntry";

	/**
	 * hashtables of qualified unit names for file readers/writers, keyed by
	 * file type
	 */
	private static Hashtable filereaders = new Hashtable();
	private static Hashtable filewriters = new Hashtable();

	// private static WriteConfigThread writeConfigThread = null;
	// private static WriteStateThread writeStateThread = null;

	private static boolean restoredFromDisk = false;

	public static String appletHome = null;

	/**
	 * The resource bundle to store the messages to display within triana. These
	 * are taken from the system/internationalization/triana_.._...properties
	 * file depending on which locale you are running in
	 */
	static ResourceBundle messages = null;

	static ResourceBundle tips = null;

	/**
	 * Reference to the home directory : Calculated in static {}
	 */
	public static String home = null;

	public static Applet applet = null;

	/**
	 * Files and String names used in the config files and the locking mechanism
	 */
	private static final String CONFIG_FILE = "gridchem.config";
	private static final String CONFIG_FILE_BAK = "gridchem_bak.config";
	private static final String PEER_CONFIG_FILE = "peer.config";
	private static File configFile;
	private static File configBakFile;
	private static final String LOCK_PREFIX = "config";
	private static final String LOCK_SUFFIX = "lock";

	private static final FileFilter lockFilter = new FileFilter() {
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(LOCK_SUFFIX);
		}
	};

	private static final long TIMEOUT = 20000;
	private static final String PROPERTIES_FILE = "cog.properties";

	private static String resourceDir = "";
	private static String tempDir = "";
	private static String logDir = "";
	private static String remoteToolDir = "";
	private static String dataToolDir = "";
	private static String userToolDir = "";
	private static String globusCertDir = "";
	private static String globusDir = "";
	private static String imagesDir = "";
	private static String propertiesDir = "";
	private static String gridchemDataDir = "";
	private static String userDataDir = "";
	private static String applicationDataDir = "";
	private static String applicationSoftwareTemplateDir = "";
	private static String trustedCaDir = "";
	private static String securityDir = "";
	private static String trustStore = "ccgkeystore";
	private static String gatAdaptorPath = "";
	private static boolean webStart = false;
	private static String macAddress = "";

	/**
	 * A private list of the user property change listeners
	 */
	private static ArrayList listeners = new ArrayList();

	/**
	 * A hashtable of the peer configuration states, keyed by peer type
	 */
	private static Hashtable peerconfig = new Hashtable();

	public final static int BUF_SIZE = 1024;

	/**
	 * Initializes the GridChem home
	 */
	static {
		Trace.entry();
		try {
			home = System.getProperty("gridchem.home");
			if (home == null) {
				URL url = Class.forName("org.gridchem.client.util.Env")
						.getResource("Env.class");
				if (os().equals("windows")) {
					Trace.note("Determining " + os() + " information");
					home = url.getFile();
					if (home.indexOf("file") != -1)
						home = home.substring(5);
					else
						home = home.substring(0);

					home = home.replaceAll("%20", " ");

					// get mac address of the windows box using ipconfig
					StringBuffer sb = new StringBuffer();
					InputStream in = Runtime
							.getRuntime()
							.exec("cmd.exe /c ipconfig/all | find \"Physical Address\"")
							.getInputStream();
					// System.out.println("Executed ipconfig on windows");
					Reader reader = new InputStreamReader(in, "UTF-8");
					int c;
					while ((c = in.read()) != -1)
						sb.append((char) c);

					macAddress = sb.toString();
					System.out.println(macAddress);
					if (macAddress.indexOf("\n") != -1) {
						macAddress = macAddress.substring(0,
								macAddress.indexOf("\n"));
						System.out
								.println("Found multiple macAddresses, using first");
					}
					macAddress = macAddress.substring(
							macAddress.indexOf("-") - 2,
							macAddress.lastIndexOf("-") + 2);
					macAddress = macAddress.replaceAll("-", "");
					Trace.note(os() + " MAC address is: " + macAddress);
					System.out.println("MAC Address is: " + macAddress);
				} else if (os().equals("osx")) {
					Trace.note("Determining " + os() + " information");

					home = url.getFile();

					// get mac address of the unix-type box using ifconfig
					StringBuffer sb = new StringBuffer();
					InputStream in = Runtime.getRuntime()
							.exec("/sbin/ifconfig").getInputStream();
					Reader reader = new InputStreamReader(in, "UTF-8");
					int c;
					while ((c = in.read()) != -1)
						sb.append((char) c);

					macAddress = sb.toString();
					macAddress = macAddress.substring(
							macAddress.indexOf("ether"),
							macAddress.indexOf("media"));
					macAddress = macAddress.substring(
							macAddress.indexOf(":") - 2,
							macAddress.lastIndexOf(":") + 2);
					macAddress = macAddress.replaceAll(":", "");
					Trace.note(os() + " MAC address is: " + macAddress);

				} else {
					Trace.note("Determining " + os() + " information");
					home = url.getFile();

					// get mac address of the linux box using ifconfig
					StringBuffer sb = new StringBuffer();
					InputStream in = Runtime.getRuntime()
							.exec("/sbin/ifconfig").getInputStream();
					// InputStream in = Runtime.getRuntime()
					// .exec("/sbin/ifconfig | perl -ne 'print $1 " +
					// "if (/.*HWaddr\\s*(..:..:..:..:..:..)\\s*/);' ").getInputStream();
					Reader reader = new InputStreamReader(in, "UTF-8");
					int c;
					while ((c = in.read()) != -1)
						sb.append((char) c);

					macAddress = sb.toString();
					macAddress = macAddress.substring(
							macAddress.indexOf("HWaddr") + 7,
							macAddress.indexOf("HWaddr") + 24);
					macAddress = macAddress.replaceAll(":", "");
					Trace.note(os() + " MAC address is: " + macAddress);
				}

				if (home.indexOf("src") != -1) {
					home = home.substring(0, home.indexOf("src"));
				} else if (home.indexOf("lib") != -1) {
					home = home.substring(0, home.indexOf("lib"));
				} else if (home.indexOf("!") != -1) {
					home = home.substring(0, home.lastIndexOf("!"));
					System.out.println("jar path is: " + home);
					home = home.substring(0, home.lastIndexOf("/"));
					System.out.println("jar revised path is: " + home);
				} else {
					home = home.substring(0, home.lastIndexOf(File.separator));
				}
				home = home.replace('/', File.separator.charAt(0));
			}

			if (!home.endsWith(File.separator)) {
				home = home + File.separator;
			}

			// readPeerConfig();
			// if (Settings.DEBUG)
			Trace.note("GridChem Home is " + home);
			Trace.exit();
		} catch (Exception e) { // Non Possible'
			logger.error("Error in static init block", e);
		}

		// Util.registerTransport();

	}

	private Env() {
		try {
			copyTemplateFiles();
			copyImageFiles();
			copyNanocadDataFiles();
			copyVibrationalAnalysisFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Env getInstance() {
		createGridChemDirectoryStructure();

		System.setProperty("java.protocol.handler.pkgs",
				"com.sun.net.ssl.internal.www.protocol");
		// Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		return new Env();
	}

	private File createTempZipFile(String resourceLocation, String filename) {

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(resourceLocation);

		File templatesFile = new File(getApplicationTempDir()
				+ "/" + filename);
		try {

			OutputStream out = new FileOutputStream(templatesFile);
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return templatesFile;
	}

	private void copyTemplateFiles() throws URISyntaxException, IOException {
		File templateDir = new File(getApplicationSoftwareTemplateDir());
		if (templateDir.exists()) {
			templateDir.delete();
		}

		templateDir.mkdirs();

		File templatesFile = createTempZipFile("etc/templates.zip", "templates.zip");

		unCompressZippedFile(templatesFile, templateDir.getAbsolutePath());

		templatesFile.delete();
	}

	private void copyImageFiles() throws URISyntaxException, IOException {
		File imageDir = new File(getImagesDir());
		if (imageDir.exists()) {
			imageDir.delete();
		}

		imageDir.mkdirs();
		
		File imagesFile = createTempZipFile("etc/images.zip", "images.zip");
		
		unCompressZippedFile(imagesFile, imageDir.getAbsolutePath());
		
		imagesFile.delete();
	}
	
	private void copyNanocadDataFiles() throws ZipException, IOException {
		File nanocadDataFile = createTempZipFile("etc/nanocaddata.zip", "nanocaddata.zip");
		
		unCompressZippedFile(nanocadDataFile, getApplicationDataDir());
		
		nanocadDataFile.delete();
		
		File txtDataFile = createTempZipFile("etc/txt.zip", "txt.zip");
		
		unCompressZippedFile(txtDataFile, getApplicationDataDir());
		
		txtDataFile.delete();
	}
	
	private void copyVibrationalAnalysisFiles() throws ZipException, IOException {
		File vibAnaFile = createTempZipFile("etc/vibrational_analysis.zip", "vibrational_analysis.zip");
		
		unCompressZippedFile(vibAnaFile, getApplicationDataDir());
		
		vibAnaFile.delete();
	}

	private void unCompressZippedFile(File zippedFile, String parentFolder)
			throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(zippedFile);
		Enumeration<? extends ZipEntry> files = zipFile.entries();
		File f = null;
		FileOutputStream fos = null;

		while (files.hasMoreElements()) {
			try {
				ZipEntry entry = (ZipEntry) files.nextElement();
				InputStream eis = zipFile.getInputStream(entry);
				byte[] buffer = new byte[1024];
				int bytesRead = 0;

				f = new File(parentFolder + File.separator + entry.getName());

				if (entry.isDirectory()) {
					f.mkdirs();
					continue;
				} else {
					f.getParentFile().mkdirs();
					f.createNewFile();
				}

				fos = new FileOutputStream(f);

				while ((bytesRead = eis.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesRead);
				}
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						// ignore
					}
				}
			}
		}
	}

	/**
	 * Initialise the user config
	 * 
	 * @param tools
	 *            the tool table
	 * @param write
	 *            a flag indicating whether changes to the config are written
	 *            back to the config file
	 * 
	 *            public static void initConfig(ToolTable tools, boolean write)
	 *            { logger.info("Configuring environment");
	 * 
	 *            // initialise the object marshaller
	 *            ObjectMarshaller.init(tools, new ToolImp());
	 * 
	 *            File layoutFile = new File(Env.getToolBoxLayoutFile()); if
	 *            (!layoutFile.exists()) { try { layoutFile.createNewFile(); }
	 *            catch (IOException e) {
	 *            logger.error("Error creating user tool box file", e); } }
	 * 
	 *            configFile = new File(Env.getResourceDir() +
	 *            Env.getFileSeparator() + CONFIG_FILE); configBakFile = new
	 *            File(Env.getResourceDir() + Env.getFileSeparator() +
	 *            CONFIG_FILE_BAK);
	 * 
	 *            if (!configFile.exists()) { // First time run so need to set
	 *            up paths restoreDefaultConfig(); } else { // read from the
	 *            file GUIEnv.loadDefaultColours(); try { Env.readConfig(tools,
	 *            configFile); } catch (Exception e) {
	 *            logger.warn("Corrupt config file, restoring from backup"); try
	 *            { Env.readConfig(tools, configBakFile); } catch (Exception e1)
	 *            { logger.warn("Corrupt backup file, restoring defaults");
	 *            restoreDefaultConfig(); } } } setToolBoxDefaults(tools); if
	 *            (write) { writeConfigThread = new WriteConfigThread(tools);
	 *            writeConfig(); writeStateThread = new WriteStateThread(); } }
	 */

	/**
	 * Sets up the type value pairs for the toolboxes if they are not already
	 * defined.
	 * 
	 * @param tools
	 *            the <code>ToolTable</code> that the toolboxes will be added
	 *            to.
	 * 
	 *            private static void setToolBoxDefaults(ToolTable tools) { if
	 *            (tools.getToolBox(ToolTable.DEFAULT_TOOLBOX) == null) { String
	 *            defaultToolbox1 = Env.home() + "toolboxes"; String
	 *            defaultToolbox2 = new File(Env.home()).getParent() +
	 *            separator() + "toolboxes";
	 * 
	 *            if ((new File(defaultToolbox1)).exists()) {
	 *            tools.setToolBoxType(defaultToolbox1,
	 *            ToolTable.DEFAULT_TOOLBOX); }
	 * 
	 *            if ((new File(defaultToolbox2)).exists()) {
	 *            tools.setToolBoxType(defaultToolbox2,
	 *            ToolTable.DEFAULT_TOOLBOX); } } if
	 *            (tools.getToolBox(ToolTable.USER_TOOLBOX) == null)
	 *            tools.setToolBoxType(Env.getUserToolDir(),
	 *            ToolTable.USER_TOOLBOX); if
	 *            (tools.getToolBox(ToolTable.DATA_TOOLBOX) == null)
	 *            tools.setToolBoxType(Env.getDataToolDir(),
	 *            ToolTable.DATA_TOOLBOX); if
	 *            (tools.getToolBox(ToolTable.REMOTE_TOOLBOX) == null)
	 *            tools.setToolBoxType(Env.getRemoteToolDir(),
	 *            ToolTable.REMOTE_TOOLBOX);
	 * 
	 *            }
	 */

	/**
	 * Restore the default user settings
	 * 
	 * private static void restoreDefaultConfig() { GUIEnv.loadDefaultColours();
	 * String defaultEditor = Env.getString("defaultEditor");
	 * setUserProperty(CODE_EDITOR_STR, defaultEditor);
	 * setUserProperty(HELP_EDITOR_STR, defaultEditor);
	 * setUserProperty(HELP_VIEWER_STR, Env.getString("defaultViewer")); }
	 */

	/**
	 * Turns the debug information printed out to the MSDOS window on or off.
	 * This works in Applets or Applications
	 * 
	 * public static void setDebug(String deb) { String oldVal; if
	 * (deb.equals("on")) { oldVal = (String) setUserProperty(DEBUG_STR,
	 * "true"); } else { oldVal = (String) setUserProperty(DEBUG_STR, "false");
	 * } if ((oldVal == null) || (!oldVal.equals(deb))) { writeConfig(); } }
	 */

	/**
	 * @return true if triana should convert to doubles whenever possible
	 * 
	 *         public static boolean getConvertToDouble() { String convert =
	 *         (String) getUserProperty(CONVERT_TO_DOUBLE_STR); if (convert ==
	 *         null) { setConvertToDouble(false); return false; } return (new
	 *         Boolean(convert)).booleanValue(); }
	 * 
	 *         /**
	 * @return true if triana should use non-blocking output nodes
	 * 
	 *         public static boolean isNonBlockingOutputNodes() { String
	 *         nonBlock = (String) getUserProperty(NONBLOCKING_OUT_STR); if
	 *         (nonBlock == null) { setNonBlockingOutputNodes(false); return
	 *         false; } return (new Boolean(nonBlock)).booleanValue(); }
	 * 
	 *         /** Enables/disables triana should convert to doubles whenever
	 *         possible
	 * 
	 *         public static void setNonBlockingOutputNodes(boolean state) {
	 *         setUserProperty(NONBLOCKING_OUT_STR, String.valueOf(state)); }
	 * 
	 *         /** Enables/disables triana should convert to doubles whenever
	 *         possible
	 * 
	 *         public static void setConvertToDouble(boolean state) {
	 *         setUserProperty(CONVERT_TO_DOUBLE_STR, String.valueOf(state)); }
	 * 
	 *         /** Adds a name, value pair to the user configuration properties.
	 *         The propValue object has to correctly override the equals()
	 *         method otherwise the property file will be written every time
	 *         this method is called, even if the new value is supposed to be
	 *         the same as the old value.
	 * 
	 * @return the old value or null if there was none.
	 * 
	 *         public static Object setUserProperty(String propName, Object
	 *         propValue) { Object oldVal = options.put(propName, propValue); if
	 *         ((oldVal == null) || (!oldVal.equals(propValue))) {
	 *         //notifyPropertyListeners(propName, propValue); writeConfig(); }
	 *         return oldVal; }
	 */

	/**
	 * Create the user's local GridChem directory structure: <user.home> /
	 * gridchem / certificates / data / preferences
	 * <p>
	 * Unpack the trusted CA directory from the jar file and place it in the
	 * gridchem directory structure on the user's machine.
	 */
	public static void createGridChemDirectoryStructure() {
		ZipExtractor uz;

		// create default properties directory
		if (Settings.DEBUG)
			System.out.println("Created Properties Directory: "
					+ getPropertiesDir());
		// create user data directory
		if (Settings.DEBUG)
			System.out.println("Created GridChem Data Directory: "
					+ getGridchemDataDir());
		// create application data directory
		if (Settings.DEBUG)
			System.out.println("Created Application Data Directory: "
					+ getApplicationDataDir());
		// create application temp directory
		if (Settings.DEBUG)
			System.out.println("Created Application Temp Directory: "
					+ getApplicationTempDir());
		// create user security directory to store credentials
		if (Settings.DEBUG)
			System.out.println("Created Security Directory: "
					+ getSecurityDir());
		// don't overwrite existing structure
		if (Settings.DEBUG)
			System.out.println("Created TrustedCA Directory: "
					+ getTrustedCaDir());
		// create globus directory
		if (Settings.DEBUG)
			System.out.println("Created Globus Directory: " + getGlobusDir());
		if (Settings.DEBUG)
			System.out.println("Created Images Directory: " + getImagesDir());
		String osName = System.getProperty("os.name");
		if (!(osName.startsWith("Windows"))) {
			String rasmolDir = applicationDataDir + separator() + "rasmol";
			File rasDir = new File(rasmolDir);
			if (!rasDir.exists()) {
				rasDir.mkdir();
				System.out
						.println("Created directory for installation of rasmol files");
			}
		}
	}

	/**
	 * Dynamically register the GMS Server Certificate from within the client.
	 * This allows GridChem to run on machines out of the box, even when the
	 * user does not have root access to their computer to install the server
	 * cert by hand.
	 */
	public static void loadGmsServerCert() {
		/*
		 * String cert = getGmsServerPath();
		 * System.setProperty("javax.net.ssl.trustStore", cert); if
		 * (Settings.DEBUG)
		 * System.out.println("Adding GMS server certificate \"" + cert +
		 * "\" to list of trusted certificates"); // Dynamically register the
		 * JSSE provider Security.addProvider(new
		 * com.sun.net.ssl.internal.ssl.Provider());
		 */
		if (!Settings.DEBUG) {
			// javax.net.ssl.TrustStore ts = new TrustStore();
			System.out.println("Updated list of trusted providers:");
			Provider[] provider = Security.getProviders();
			for (int i = 0; i < provider.length; i++) {
				System.out.println(provider[i].getName());
				provider[i].list(System.out);
			}
		}
	}

	/**
	 * @return the absolute path of the gms server certificate placed on the
	 *         user's local disc by GridChem.
	 */
	public static String getGmsServerPath() {
		return getTrustedCaDir() + separator() + Settings.gmsCertificateName;
	}

	/**
	 * Returns the user property, null if not found.
	 */
	public static Object getUserProperty(String propName) {
		if (options.containsKey(propName)) {
			return options.get(propName);
		} else {
			return null;
		}
	}

	/**
	 * @return the last toolbox to be used by the unit wizard or compile tool
	 * 
	 *         public static String getLastWorkingToolbox() { String toolbox =
	 *         (String) getUserProperty(DIRECTORY_STR + ":" +
	 *         TOOLBOX_DIRECTORY);
	 * 
	 *         if (toolbox != null) { return toolbox; } else {
	 *         setLastWorkingToolbox(Env.home() + "toolboxes"); return
	 *         Env.home() + "toolboxes"; } }
	 * 
	 *         /** Sets the last toolbox to be used by the unit wizard or
	 *         compile tool
	 * 
	 * @param toolbox
	 * 
	 *            public static void setLastWorkingToolbox(String toolbox) {
	 *            setDirectory(TOOLBOX_DIRECTORY, toolbox); }
	 * 
	 *            /** Wrapper for (@link #getUserProperty}
	 * 
	 * @return true if and only if the property exists and it has the value
	 *         "true"
	 */
	public static boolean getBooleanUserProperty(String propName) {
		Boolean prop = getBoolean(propName);
		if (prop != null) {
			return prop.booleanValue();
		}
		return false;
	}

	/**
	 * Method to sort out the difference between storing our boolean value as a
	 * string or a boolean.
	 */
	private static Boolean getBoolean(String propName) {
		Object value = getUserProperty(propName);
		if ((value == null) || (value instanceof Boolean)) {
			return (Boolean) value;
		} else if (value instanceof String) {
			return new Boolean((String) value);
		} else {
			return null;
		}
	}

	/**
	 * Wrapper for {@link #getUserProperty} if the property does not exist then
	 * it is set to the defaultValue and that is returned.
	 * 
	 * public static boolean getBooleanUserProperty(String propName, boolean
	 * defaultValue) { Boolean prop = getBoolean(propName); if (prop != null) {
	 * return prop.booleanValue(); } setUserProperty(propName, new
	 * Boolean(defaultValue)); return defaultValue; }
	 * 
	 * /** Returns Java's home directory
	 */
	public final static String javaHome() {
		String java = System.getProperty("java.home");

		if (java == null) {
			return "";
		} else if (java.endsWith(File.separator + "jre")) {
			return java.substring(0, java.lastIndexOf(File.separator + "jre"));
		} else {
			return java;
		}
	}

	/**
	 * Returns the user's home directory
	 */
	public final static String userHome() {
		if (isAnApplet) {
			return home();
		} else {
			String userHome = System.getProperty("user.home");
			if (userHome.equals("")) {
				userHome = File.separator;
			}
			if (userHome.equals("$")) {
				userHome = File.separator;
			}
			return userHome;
		}
	}

	/**
	 * sets the compiler command to the specified path
	 * 
	 * public static void setCompilerCommand(String cmd) {
	 * setUserProperty(COMPILER_STR, cmd); }
	 * 
	 * /** gets the compiler command
	 */
	public static String getCompilerCommand() {
		String compilerCmd = (String) getUserProperty(COMPILER_STR);
		if ((compilerCmd != null) && (!compilerCmd.equals(""))) {
			return compilerCmd;
		}
		return getDefaultCompilerCommand();
	}

	/**
	 * @return the default compiler (from java.home)
	 */
	public static String getDefaultCompilerCommand() {
		String home = Env.javaHome();

		File file = new File(home + Env.getFileSeparator() + "bin"
				+ Env.getFileSeparator() + "javac");
		if (file.exists()) {
			return file.getAbsolutePath();
		}

		file = new File(home + Env.getFileSeparator() + "bin"
				+ Env.getFileSeparator() + "javac.exe");
		if (file.exists()) {
			return file.getAbsolutePath();
		}

		if (home.lastIndexOf(Env.getFileSeparator()) > -1) {
			home = home.substring(0, home.lastIndexOf(Env.getFileSeparator()));

			file = new File(home + Env.getFileSeparator() + "bin"
					+ Env.getFileSeparator() + "javac");
			if (file.exists()) {
				return file.getAbsolutePath();
			}

			file = new File(home + Env.getFileSeparator() + "bin"
					+ Env.getFileSeparator() + "javac.exe");
			if (file.exists()) {
				return file.getAbsolutePath();
			}
		}

		return "";
	}

	public static String getJavacArgs() {
		return "";
	}

	/**
	 * Returns the file separator to use. If we are an applet then a "/" is
	 * returned otherwise a File.separator is used.
	 */
	public final static String separator() {
		if (isAnApplet) {
			return "/";
		} else {
			return File.separator;
		}
	}

	/**
	 * Returns the machines operating system. This returns a single word which
	 * identifies the particular operating system. The identifier is returned in
	 * lower case so it can be used to identify directories of where various
	 * things are stored for different platforms e.g. </p>
	 * <p>
	 * <ol>
	 * <li>Windows 95/NT - <i>windows</i> is returned
	 * <li>Solaris - <i>solaris</i> is returned
	 * <li>IRIX - <i>irix</i> is returned
	 * <li>DEC - <i>dec</i> is returned
	 * <li>LINUX - <i>linux</i> is returned</li>
	 */
	public final static String os() {
		/*
		 * if (GUIEnv.isAnApplet()) // Doesn't allow access of properties in JDK
		 * 1.3 { return "windows"; }
		 */

		String os = System.getProperty("os.name");

		if (os.startsWith("Windows")) {
			return "windows";
		}

		if ((os.equals("SunOS")) || (os.equals("Solaris"))) {
			return "solaris";
		}

		if (os.equals("Digital Unix")) {
			return "dec";
		}

		if (os.equals("Linux")) {
			return "linux";
		}

		if ((os.equals("Irix")) || (os.equals("IRIX"))) {
			return "irix";
		}

		if (os.equals("Mac OS X")) {
			return "osx";
		}

		return "Not Recognised";
	}

	public int getEndian() {
		String os = os();
		if ((os.equals("windows")) || (os.equals("linux"))
				|| (os.equals("dec"))) {
			endian = LITTLE;
		} else {
			endian = BIG;
		}
		return endian;
	}

	/**
	 * The ending for the native libraries i.e. .so on unix and .dll on window
	 * 95/NT, .jnilib for Mac OS X
	 */
	public final static String getSharedLibSuffix() {
		String os = os();
		if (os.equals("windows")) {
			return ".dll";
		} else if (os.equals("osx")) {
			return ".jnilib";
		} else {
			return ".so";
		}
	}

	/**
	 * The prefix for native libraries, nothing for windows, lib for *nix
	 * systems.
	 */
	public final static String getSharedLibPrefix() {
		String os = os();
		if (os.equals("windows")) {
			return "";
		} else {
			return "lib";
		}
	}

	/**
	 * Triana's platform specific directory for native shared libraries.
	 */
	public final static String getSharedLibPath() {
		return home() + "lib" + separator() + os() + separator();
	}

	/**
	 * Returns the location of the plugin directory used to store project
	 * specific plugins to be loaded at run time, for example filters and
	 * import/export tools.
	 * 
	 * @return the plugin directory location
	 */
	public final static String getPluginDir() {
		return home() + "plugins";
	}

	/**
	 * Returns the machine architecture. This returns a single word which
	 * identifies the particular platform e.g. </p>
	 * <p>
	 * <ol>
	 * <li>Windows 95/NT - <i>Windows</i> is returned
	 * <li>Solaris - <i>Solaris</i> is returned
	 * <li>IRIX - <i>Irix</i> is returned
	 * <li>DEC - <i>Dec</i> is returned
	 * <li>LINUX - <i>Linux</i> is returned</li>
	 */
	public final static String arch() {
		return System.getProperty("os.arch");
	}

	/**
	 * Returns the operating system version number
	 */
	public final static String osVer() {
		return System.getProperty("os.version");
	}
	
	public final static void setVersion(String ver) {
		version = ver;
	}

	/**
	 * Returns the GridChem version number
	 */
	public final static String getVersion() {

		return version;
	}

	/**
	 * Returns the java version number
	 */
	public final static String javaVersion() {
		return System.getProperty("java.version");
	}

	/**
	 * Returns the GridChem environment variable.
	 */
	public final static String home() {
		if (home != null) {
			return home;
		} else if (isAnApplet) {
			if (!appletHome.endsWith("/")) {
				appletHome = appletHome + "/";
			}
			home = appletHome;
			return appletHome;
		} else {
			logger.warn("Problem with calculating GridChem home");
			return null;
		}
	}

	/**
	 * Returns Java's CLASSPATH variable. This is the actual CLASSPATH
	 * environment variable.
	 */
	public final static String getClasspath() {
		String saved = (String) getUserProperty(CLASSPATH_STR);
		if ((saved == null) || saved.equals("")) {
			return System.getProperty("java.class.path");
		}
		return saved;
	}

	public final static String getSystemClasspath() {
		return System.getProperty("java.class.path");
	}

	/**
	 * @param path
	 *            the classpath to save.
	 * 
	 *            public final static void setClasspath(String path) {
	 *            setUserProperty(CLASSPATH_STR, path); }
	 * 
	 * 
	 *            /** Returns the Windows home directory. Obviously just works
	 *            for windows.
	 */
	public final static String windir() {
		return System.getProperty("windir").trim();
	}

	/**
	 * Returns the absolute path to the toolbox layout file.
	 */
	public static final String getToolBoxLayoutFile() {
		return getResourceDir() + separator() + "toolbox.layout";
	}

	/**
	 * @return the absolute path to the user's tool directory.
	 */
	private static final String getUserToolDir() {
		if (userToolDir.equals("")) {
			userToolDir = getResourceDir() + separator() + "GroupTools";
			File dir = new File(userToolDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return userToolDir;
	}

	/**
	 * @return The user name from the JDK "user.name"
	 */
	public static String getUserName() {
		return System.getProperty("user.name");
	}

	/**
	 * @return the absolute path to the user's data directory.
	 */
	private static final String getDataToolDir() {
		if (dataToolDir.equals("")) {
			dataToolDir = getResourceDir() + separator() + "Data";
			File dir = new File(dataToolDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return dataToolDir;
	}

	/**
	 * @return the absolute path to the user's remote service directory.
	 */
	private static final String getRemoteToolDir() {
		if (remoteToolDir.equals("")) {
			remoteToolDir = getResourceDir() + separator() + "Remote";
			File dir = new File(remoteToolDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return remoteToolDir;
	}

	/**
	 * @return the absolute path to the user's temp directory
	 */
	public static final String getTempDir() {
		if (tempDir.equals("")) {
			tempDir = getResourceDir() + separator() + "temp";
			File dir = new File(tempDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return tempDir;
	}

	/**
	 * @return the absolute path to the GridChem data directory
	 */
	public static final String getGridchemDataDir() {
		if (gridchemDataDir.equals("")) {
			gridchemDataDir = userHome() + separator() + "gridchem"
					+ separator() + "data";
			File dir = new File(gridchemDataDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return gridchemDataDir;
	}

	/**
	 * @return the absolute path to the GridChem data directory
	 */
	public static final String getGlobusDir() {
		if (globusDir.equals("")) {
			globusDir = userHome() + separator() + ".globus";
			File dir = new File(globusDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return globusDir;
	}

	/**
	 * @return the absolute path to the Images data directory
	 */
	public static final String getImagesDir() {
		if (imagesDir.equals("")) {
			imagesDir = getApplicationDataDir() + separator() + "images";
			File dir = new File(imagesDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return imagesDir;
	}

	/**
	 * @return the absolute path to the user's Globus certificate directory
	 */
	public static final String getGlobusCertDir() {
		if (globusCertDir.equals("")) {
			globusCertDir = getGridchemDataDir() + separator() + "certificates";
			File dir = new File(globusCertDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return globusCertDir;
	}

	/**
	 * @return the absolute path to the user's log directory
	 */
	public static final String getLogDir() {
		if (logDir.equals("")) {
			logDir = getResourceDir() + separator() + "temp";
			File dir = new File(logDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return logDir;
	}

	/**
	 * @return the absolute path to the user resource directory.
	 */
	public final static String getResourceDir() {
		if (resourceDir.equals("")) {
			resourceDir = userHome() + File.separator + "." + verName
					+ "Resources";
			File resDir = new File(resourceDir);
			if (!resDir.exists()) {
				resDir.mkdir();
			}
		}
		return resourceDir;
	}

	/**
	 * @return the last directory used to access the specified directory type (@see
	 *         triana.util.TFileChooser)
	 */
	public final static String getDirectory(String dirtype) {
		String lastdir = (String) getUserProperty(DIRECTORY_STR + ":" + dirtype);

		if (lastdir != null) {
			return lastdir;
		} else {
			return userHome();
		}
	}

	/**
	 * Sets the last directory used to access the specified directory type (@see
	 * triana.util.TFileChooser)
	 * 
	 * public final static void setDirectory(String dirtype, String dir) {
	 * setUserProperty(DIRECTORY_STR + ":" + dirtype, dir); }
	 * 
	 * 
	 * /** Sets the last screen position of the Triana application window
	 * 
	 * public final static void setWindowPosition(Point position) {
	 * setUserProperty(WINDOW_POSITION_STR, position); }
	 * 
	 * /**
	 * 
	 * @return the last screen position of the Triana window
	 */
	public final static Point getWindowPosition() {
		Object position = getUserProperty(WINDOW_POSITION_STR);

		if (position == null) {
			return new Point(0, 0);
		} else {
			return (Point) position;
		}
	}

	/**
	 * Sets the last dimension of the Triana application window
	 * 
	 * public final static void setWindowSize(Dimension size) {
	 * setUserProperty(WINDOW_SIZE_STR, size); }
	 * 
	 * /**
	 * 
	 * @return the last dimension of the Triana window
	 */
	public final static Dimension getWindowSize() {
		Object size = getUserProperty(WINDOW_SIZE_STR);

		if (size == null) {
			return defaultsize;
		} else {
			return (Dimension) size;
		}
	}

	/**
	 * Sets whether the debug console is visible
	 * 
	 * public final static void setDebugVisible(boolean state) {
	 * setUserProperty(DEBUG_VISIBLE_STR, String.valueOf(state)); }
	 * 
	 * /**
	 * 
	 * @return true if the debug console is visible
	 */
	public final static boolean isDebugVisible() {
		Object visible = getUserProperty(DEBUG_VISIBLE_STR);

		if (visible == null)
			return false;
		else
			return new Boolean((String) visible).booleanValue();
	}

	/**
	 * Sets the last screen position of the debug console
	 * 
	 * public final static void setDebugPosition(Point position) {
	 * setUserProperty(DEBUG_POSITION_STR, position); }
	 * 
	 * /**
	 * 
	 * @return the last screen position of the debug console
	 */
	public final static Point getDebugPosition() {
		Object position = getUserProperty(DEBUG_POSITION_STR);

		if (position == null) {
			return new Point(0, 0);
		} else {
			return (Point) position;
		}
	}

	/**
	 * Sets the last dimension of the debug console
	 * 
	 * public final static void setDebugSize(Dimension size) {
	 * setUserProperty(DEBUG_SIZE_STR, size); }
	 * 
	 * /**
	 * 
	 * @return the last dimension of the debug console
	 */
	public final static Dimension getDebugSize() {
		Object size = getUserProperty(DEBUG_SIZE_STR);

		if ((size == null) || (!(size instanceof Dimension))) {
			return defaultsize;
		} else {
			return (Dimension) size;
		}
	}

	/**
	 * Sets the current gui zoom factor
	 * 
	 * public final static void setZoomFactor(double zoom) {
	 * setUserProperty(ZOOM_FACTOR_STR, new Double(zoom)); }
	 * 
	 * /**
	 * 
	 * @return the current gui zoom factor
	 */
	public final static double getZoomFactor() {
		Object zoom = getUserProperty(ZOOM_FACTOR_STR);

		if ((zoom == null) || (!(zoom instanceof Double))) {
			return 1;
		} else {
			return ((Double) zoom).doubleValue();
		}
	}

	/**
	 * Sets the file reader for the specified file type
	 * 
	 * @param fileext
	 *            the file extension
	 * @param toolname
	 *            the qualified tool name of the reader
	 * 
	 * @return the old value (or null if not set)
	 * 
	 *         public final static String setFileReader(String fileext, String
	 *         toolname) { String oldVal = (String)
	 *         filereaders.put(fileext.toLowerCase(), toolname); if ((oldVal ==
	 *         null) || (!oldVal.equals(fileext.toLowerCase()))) writeConfig();
	 * 
	 *         return oldVal; }
	 * 
	 *         /** Sets the file reader for the specified file type
	 * 
	 * @param fileext
	 *            the file extension
	 * 
	 * @return the qualified tool name of the removed reader (or null if not
	 *         set)
	 */
	public final static String removeFileReader(String fileext) {
		return (String) filereaders.remove(fileext.toLowerCase());
	}

	/**
	 * @return the file reader for the specified file type (or null if not set)
	 */
	public final static String getFileReader(String fileext) {
		if (filereaders.containsKey(fileext.toLowerCase()))
			return (String) filereaders.get(fileext.toLowerCase());
		else
			return null;
	}

	/**
	 * @return an array of the file extensions for which readers are set
	 */
	public final static String[] getFileReaderExtensions() {
		return (String[]) filereaders.keySet().toArray(
				new String[filereaders.keySet().size()]);
	}

	/**
	 * @param fileext
	 *            the file extension
	 * 
	 * @return true if a file reader is set for the specified extension
	 */
	public final static boolean isFileReader(String fileext) {
		return filereaders.containsKey(fileext.toLowerCase());
	}

	/**
	 * Sets the file writer for the specified file type
	 * 
	 * @param fileext
	 *            the file extension
	 * @param toolname
	 *            the qualified tool name of the writer
	 * 
	 * @return the old value (or null if not set)
	 * 
	 *         public final static String setFileWriter(String fileext, String
	 *         toolname) { String oldVal = (String)
	 *         filewriters.put(fileext.toLowerCase(), toolname); if ((oldVal ==
	 *         null) || (!oldVal.equals(fileext.toLowerCase()))) writeConfig();
	 * 
	 *         return oldVal; }
	 * 
	 *         /** Sets the file writer for the specified file type
	 * 
	 * @param fileext
	 *            the file extension
	 * 
	 * @return the qualified tool name of the removed writer (or null if not
	 *         set)
	 */
	public final static String removeFileWriter(String fileext) {
		return (String) filewriters.remove(fileext.toLowerCase());
	}

	/**
	 * @return the file writer for the specified file type (or null if not set)
	 */
	public final static String getFileWriter(String fileext) {
		if (filewriters.containsKey(fileext.toLowerCase()))
			return (String) filewriters.get(fileext.toLowerCase());
		else
			return null;
	}

	/**
	 * @return an array of the file extensions for which writers are set
	 */
	public final static String[] getFileWriterExtensions() {
		return (String[]) filewriters.keySet().toArray(
				new String[filewriters.keySet().size()]);
	}

	/**
	 * @param fileext
	 *            the file extension
	 * 
	 * @return true if a file writer is set for the specified extension
	 */
	public final static boolean isFileWriter(String fileext) {
		return filewriters.containsKey(fileext.toLowerCase());
	}

	/**
	 * updates the initialisation file with the new variables i.e. does a save
	 * state.
	 * 
	 * public static void writeConfig() { if (writeConfigThread != null) {
	 * writeConfigThread.write(); } }
	 * 
	 * public static boolean stopConfigWriters() { if (writeStateThread != null)
	 * { writeStateThread.stopThread(); while (writeStateThread.isAlive()) { try
	 * { Thread.sleep(200); } catch (InterruptedException except) { } } }
	 * 
	 * if (writeConfigThread != null) { writeConfigThread.stopThread(); while
	 * (writeConfigThread.isAlive()) { try { Thread.sleep(200); } catch
	 * (InterruptedException except) { } } }
	 * 
	 * return true; }
	 * 
	 * /** Write the state of the desk top to disk for auto loading on restart
	 * 
	 * public static void saveWorkingState() { if (writeStateThread == null) {
	 * writeStateThread = new WriteStateThread(); } writeStateThread.write(); }
	 * 
	 * /** read in the last worked on task graphs from disk
	 * 
	 * @return true if task graphs are loaded, false otherwise
	 * 
	 *         public static boolean readStateFiles() { if
	 *         (savedTaskgraphs.size() > 0) { for (Iterator iterator =
	 *         savedTaskgraphs.iterator(); iterator.hasNext();) { OpenTaskGraph
	 *         info = (OpenTaskGraph) iterator.next(); String fullpath =
	 *         Env.getTempDir() + Env.getFileSeparator() +
	 *         info.getRootFileName(); if (!FileUtils.fileExists(fullpath)) {
	 *         logger.warn("Error loading previous work, file doesn't exist: " +
	 *         fullpath); restoredFromDisk = true; return false; }
	 *         TaskGraphInterface taskgraph =
	 *         TaskGraphFileHandler.openTaskgraph(new File(fullpath), false); if
	 *         (taskgraph != null) { OpenTaskGraph[] openChildren =
	 *         info.getChildren(); for (int i = 0; i < openChildren.length; i++)
	 *         { recursivelyOpenChildWindows(openChildren[i], taskgraph); } } }
	 *         restoredFromDisk = true; return true; } else { restoredFromDisk =
	 *         true; return false; } }
	 * 
	 *         private static void recursivelyOpenChildWindows(OpenTaskGraph
	 *         toOpen, TaskGraphInterface parent) { TaskInterface task =
	 *         parent.getTask(toOpen.getName()); if ((task != null) && (task
	 *         instanceof GroupTaskInterface)) { TaskGraphPanel panel =
	 *         GUIEnv.getTaskGraphPanelFor(parent);
	 *         GUIEnv.getApplicationFrame().addChildTaskGraphPanel((TaskGraph)
	 *         task, panel.getTrianaClient()); } OpenTaskGraph[] openChildren =
	 *         toOpen.getChildren(); for (int i = 0; i < openChildren.length;
	 *         i++) { recursivelyOpenChildWindows(openChildren[i], (TaskGraph)
	 *         task); } }
	 */

	/**
	 * Write all current open taskgraphs in the workspace to disc
	 * 
	 * private static void writeStateFiles() { if (GUIEnv.restoreLast()) { if
	 * (restoredFromDisk) { clearStateFiles(); TaskGraphPanel[] windows =
	 * GUIEnv.getApplicationFrame().getRootTaskGraphPanels(); // Add the parent
	 * task graphs for (int i = 0; i < windows.length; i++) { if
	 * (windows[i].getTaskComponentCount() > 0) { String savedPath =
	 * saveTaskGraphContainer(windows[i]); OpenTaskGraph info = new
	 * OpenTaskGraph(); info.setRootFileName(savedPath);
	 * savedTaskgraphs.add(info); TaskGraphPanel[] childwindows =
	 * GUIEnv.getApplicationFrame().getChildTaskGraphPanels(windows[i]); for
	 * (int j = 0; j < childwindows.length; j++) {
	 * recursivelySaveChildWindows(childwindows[j], info);
	 * 
	 * } } } } } else { clearStateFiles(); } }
	 * 
	 * private static void recursivelySaveChildWindows(TaskGraphPanel window,
	 * OpenTaskGraph parent) { OpenTaskGraph child = new OpenTaskGraph();
	 * child.setName(window.getTaskGraph().getGroupTask().getToolName());
	 * parent.addChild(child); TaskGraphPanel[] childwindows =
	 * GUIEnv.getApplicationFrame().getChildTaskGraphPanels(window); for (int i
	 * = 0; i < childwindows.length; i++) {
	 * recursivelySaveChildWindows(childwindows[i], child); } }
	 */

	/**
	 * remove all the old state files
	 */
	private static void clearStateFiles() {
		savedTaskgraphs.clear();
		File[] oldFiles = (new File(Env.getTempDir())).listFiles();
		for (int i = 0; i < oldFiles.length; i++) {
			oldFiles[i].delete();
		}
	}

	/**
	 * Save any open MainTriana window taskgraphs for reopening next time Triana
	 * is run. Only save a window if it is a top level taskgraph, i.e. don't
	 * save the taskgraphs for any windows that are displaying a sub group. If a
	 * group window is open then set the property that so that it will be
	 * reopened at start up once it's parent taskgraph is loaded.
	 * 
	 * @param cont
	 *            the MainTriana we are testing to save
	 * 
	 * @return The name of the temporary file to save to or null if nothing has
	 *         been saved.
	 * 
	 *         private static String saveTaskGraphContainer(TaskGraphPanel cont)
	 *         { String windowName =
	 *         GUIEnv.getApplicationFrame().getInternalFrameFor(cont)
	 *         .getTitle(); StringBuffer name = new StringBuffer(windowName);
	 *         name.append("_"); name.append(System.currentTimeMillis());
	 *         name.append(".taskgraph"); String fileName = Env.getTempDir() +
	 *         Env.getFileSeparator() + name.toString();
	 *         TaskGraphFileHandler.saveTaskGraphAs((ToolInterface)
	 *         cont.getTaskGraph(), fileName, null, false); return
	 *         name.toString(); }
	 */

	/**
	 * @return true if the specified peer type is enabled
	 */
	public static boolean isPeerEnabled(String type) {
		if (peerconfig.containsKey(type))
			return new Boolean((String) peerconfig.get(type)).booleanValue();
		else
			return true;
	}

	/**
	 * Sets whether the specified peer type is enabled
	 */
	public static void setPeerEnabled(String type, boolean state) {
		peerconfig.put(type, String.valueOf(state));
		// writePeerConfig();
	}

	/**
	 * reads the configuration / private static void readPeerConfig() { try {
	 * File configFile = new File(Env.getResourceDir() + Env.getFileSeparator()
	 * + PEER_CONFIG_FILE);
	 * 
	 * if (configFile.exists()) { Reader br = new BufferedReader(new
	 * FileReader(configFile));
	 * 
	 * SAXBuilder xmlBuilder = new SAXBuilder(); Document doc =
	 * xmlBuilder.build(br); Element root = doc.getRootElement();
	 * 
	 * List list = root.getChildren("peer"); Iterator iter = list.iterator();
	 * Element elem;
	 * 
	 * while (iter.hasNext()) { elem = (Element) iter.next();
	 * peerconfig.put(elem.getAttributeValue("type"), elem.getText()); } } }
	 * catch (JDOMException except) { except.printStackTrace(); } catch
	 * (IOException except) { except.printStackTrace(); } }
	 */

	/**
	 * writes out the peer configuration / private static void writePeerConfig()
	 * { File configFile = new File(Env.getResourceDir() +
	 * Env.getFileSeparator() + PEER_CONFIG_FILE);
	 * configFile.getParentFile().mkdirs();
	 * 
	 * try { Writer bw = new BufferedWriter(new FileWriter(configFile));
	 * 
	 * XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
	 * 
	 * // create the root element with the file format version Element root =
	 * new Element("peerconfig");
	 * 
	 * Enumeration enu = peerconfig.keys(); String label;
	 * 
	 * while (enu.hasMoreElements()) { label = (String) enu.nextElement();
	 * 
	 * Element elem = new Element("peer"); elem.setAttribute("type", label);
	 * elem.setText((String) peerconfig.get(label)); root.addContent(elem); }
	 * 
	 * Document configDoc = new Document(root); xmlOut.output(configDoc, bw); }
	 * catch (IOException except) { except.printStackTrace(); } }
	 */

	/**
	 * Write a marker file to indicate we are writing to the config file, this
	 * is a blocking call that is only called from with the write config file
	 * thread. Calling from anywhere else may have a detrimental effect on
	 * performance.
	 * 
	 * @return the name of the temporary marker file.
	 */
	private static File aquireConfigFileLock() {
		File lockFile = null;
		File resourceDir = new File(Env.getResourceDir());
		File[] locks = resourceDir.listFiles(lockFilter);
		boolean lockExists = locks.length > 0;
		while (lockExists) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			lockExists = false;
			for (int i = 0; i < locks.length; i++) {
				lockExists = locks[i].exists();
				if (lockExists
						&& ((System.currentTimeMillis() - locks[i]
								.lastModified()) > TIMEOUT)) {
					locks[i].delete();
					lockExists = false;
				}
			}
		}
		try {
			lockFile = File.createTempFile(LOCK_PREFIX, LOCK_SUFFIX,
					resourceDir);
			lockFile.deleteOnExit();
		} catch (IOException e) {
		}
		return lockFile;
	}

	/**
	 * Release the marker file.
	 * 
	 * @param marker
	 */
	private static void releaseConfigFileLock(File marker) {
		marker.delete();
	}

	/**
	 * Write the triana configuration files to disk
	 * 
	 * @param tools
	 * 
	 *            private static void writeConfigFile(ToolTable tools) { File
	 *            marker = aquireConfigFileLock(); PrintWriter bw = null; try {
	 *            bw = new PrintWriter(new BufferedWriter(new
	 *            FileWriter(configFile)));
	 * 
	 *            XMLOutputter xmlOut = new
	 *            XMLOutputter(Format.getPrettyFormat());
	 * 
	 *            // create the root element with the file format version
	 *            Element root = new Element(CONFIG_STR);
	 *            root.setAttribute(CONFIG_VERSION_STR, CONFIG_VERSION);
	 *            Document configDoc = new Document(root);
	 * 
	 *            // add the toolboxes Element toolboxesElem = new
	 *            Element(TOOLBOXES_STR); root.addContent(toolboxesElem);
	 * 
	 *            String[] toolboxes = tools.getToolBoxes(); for (int count = 0;
	 *            count < toolboxes.length; count++) { Element toolbox = new
	 *            Element(TOOLBOX_STR); toolbox.addContent(toolboxes[count]);
	 * 
	 *            toolbox.setAttribute(TOOLBOX_TYPE_STR,
	 *            tools.getToolBoxType(toolboxes[count]));
	 * 
	 *            toolboxesElem.addContent(toolbox); }
	 * 
	 *            // add tool colors Element colorsElem = new
	 *            Element(COLORS_STR); root.addContent(colorsElem); TreeMap
	 *            colourMap = GUIEnv.getCableColours(); Object[] keys =
	 *            colourMap.keySet().toArray(); for (int i = 0; i < keys.length;
	 *            i++) { String tooltype = (String) keys[i]; Color color =
	 *            (Color) colourMap.get(tooltype); Element colorElem = new
	 *            Element(COLOR_STR); colorsElem.addContent(colorElem); Element
	 *            typeElem = new Element(TYPE_STR);
	 *            typeElem.setAttribute(NAME_STR, tooltype);
	 *            colorElem.addContent(typeElem); Element valueElem = new
	 *            Element(VALUE_STR); valueElem.setAttribute(RED_STR,
	 *            String.valueOf(color.getRed()));
	 *            valueElem.setAttribute(GREEN_STR,
	 *            String.valueOf(color.getGreen()));
	 *            valueElem.setAttribute(BLUE_STR,
	 *            String.valueOf(color.getBlue()));
	 *            colorElem.addContent(valueElem); }
	 * 
	 *            // options Element optionsElem = new Element(OPTIONS_STR);
	 *            root.addContent(optionsElem);
	 * 
	 *            keys = options.keySet().toArray(); for (int i = 0; i <
	 *            keys.length; i++) { String key = (String) keys[i]; Element
	 *            optionElem = new Element(OPTION_STR);
	 * 
	 *            optionElem.setAttribute(NAME_STR, key);
	 *            optionElem.addContent(ObjectMarshaller
	 *            .marshallJavaToJDOMElement(options.get(key)));
	 *            optionsElem.addContent(optionElem); }
	 * 
	 *            //color table entries if (!colorTableEntries.isEmpty()) {
	 *            Element colorTableElem = new Element(COLOR_TABLE_STR);
	 *            root.addContent(colorTableElem);
	 * 
	 *            for (int i = 0; i < colorTableEntries.size(); i++) {
	 *            ColorTableEntry colorTableEntry = (ColorTableEntry)
	 *            colorTableEntries.elementAt(i); Element ctEntryElem = new
	 *            Element(COLOR_TABLE_ENTRY_STR);
	 *            colorTableElem.addContent(ctEntryElem);
	 *            ctEntryElem.setAttribute(NAME_STR,
	 *            colorTableEntry.getColorname());
	 *            ctEntryElem.addContent(ObjectMarshaller
	 *            .marshallJavaToJDOMElement(colorTableEntry.getColor())); } }
	 * 
	 * 
	 *            // files Element fileElem = null; String[] recent =
	 *            getRecentFilePaths();
	 * 
	 *            if (recent.length > 0) { fileElem = new Element(FILES_STR);
	 *            root.addContent(fileElem);
	 * 
	 *            for (int i = 0; i < recent.length; i++) { if
	 *            (!recent[i].equals("")) { Element recentElem = new
	 *            Element(RECENT_STR); fileElem.addContent(recentElem); Element
	 *            elem = new Element(FILE_STR); elem.setAttribute(VALUE_STR,
	 *            recent[i]); recentElem.addContent(elem); } } }
	 * 
	 *            if (savedTaskgraphs.size() > 0) { if (fileElem == null) {
	 *            fileElem = new Element(FILES_STR); root.addContent(fileElem);
	 *            } for (Iterator iterator = savedTaskgraphs.iterator();
	 *            iterator.hasNext();) { OpenTaskGraph info = (OpenTaskGraph)
	 *            iterator.next(); Element openElem = new Element(OPEN_GROUPS);
	 *            fileElem.addContent(openElem); Element elem = new
	 *            Element(FILE_STR); elem.setAttribute(VALUE_STR,
	 *            info.getRootFileName()); openElem.addContent(elem);
	 *            OpenTaskGraph[] openChildren = info.getChildren(); for (int i
	 *            = 0; i < openChildren.length; i++) {
	 *            recursivelySaveChildTaskGraphElems(openChildren[i], openElem);
	 *            } } }
	 * 
	 *            if (filereaders.size() > 0) { Element readersElem = new
	 *            Element(FILE_READERS_STR); root.addContent(readersElem);
	 * 
	 *            Enumeration en = filereaders.keys(); Element readerElem;
	 *            String ext;
	 * 
	 *            while (en.hasMoreElements()) { ext = (String)
	 *            en.nextElement();
	 * 
	 *            readerElem = new Element(READER_STR);
	 *            readerElem.setAttribute(FILE_EXTENSION_STR, ext);
	 *            readerElem.setAttribute(TOOL_NAME_STR, (String)
	 *            filereaders.get(ext)); readersElem.addContent(readerElem); } }
	 * 
	 *            if (filewriters.size() > 0) { Element writersElem = new
	 *            Element(FILE_WRITERS_STR); root.addContent(writersElem);
	 * 
	 *            Enumeration en = filewriters.keys(); Element writerElem;
	 *            String ext;
	 * 
	 *            while (en.hasMoreElements()) { ext = (String)
	 *            en.nextElement();
	 * 
	 *            writerElem = new Element(WRITER_STR);
	 *            writerElem.setAttribute(FILE_EXTENSION_STR, ext);
	 *            writerElem.setAttribute(TOOL_NAME_STR, (String)
	 *            filewriters.get(ext)); writersElem.addContent(writerElem); } }
	 * 
	 *            xmlOut.output(configDoc, bw);
	 *            FileUtils.copyFile(configFile.getAbsolutePath(),
	 *            configBakFile.getAbsolutePath(), FileUtils.ASCII);
	 * 
	 *            } catch (IOException e) {
	 *            logger.error("Error writing to user config file: " +
	 *            configFile.getAbsolutePath(), e); } finally {
	 *            FileUtils.closeWriter(bw); releaseConfigFileLock(marker); } }
	 */

	/*
	 * private static void recursivelySaveChildTaskGraphElems(OpenTaskGraph
	 * childTG, Element parent) { Element childElem = new Element(CHILD_STR);
	 * parent.addContent(childElem); Element nameElem = new Element(NAME_STR);
	 * nameElem.setAttribute(VALUE_STR, childTG.getName());
	 * childElem.addContent(nameElem); OpenTaskGraph[] children =
	 * childTG.getChildren(); for (int i = 0; i < children.length; i++) {
	 * recursivelySaveChildTaskGraphElems(children[i], childElem); } }
	 */

	/**
	 * Loads in the initialisation file and sets the relevant new variables i.e.
	 * does a save state.
	 * 
	 * private static void readConfig(ToolTable tools, File file) throws
	 * Exception { BufferedReader br = null; try { br = new BufferedReader(new
	 * FileReader(file)); logger.info("Restoring From Triana Property File : " +
	 * file.getAbsolutePath()); SAXBuilder xmlBuilder = new SAXBuilder();
	 * Document doc = null; doc = xmlBuilder.build(br); Element root =
	 * doc.getRootElement();
	 * 
	 * if (!root.getName().equals(CONFIG_STR) ||
	 * (root.getAttribute(CONFIG_VERSION_STR) == null)) { throw (new
	 * Exception("Corrupt config file: " + file.getAbsolutePath())); }
	 * 
	 * String versionStr = root.getAttributeValue(CONFIG_VERSION_STR); if
	 * (!versionStr.equals(CONFIG_VERSION)) {
	 * logger.warn("Current config version is " + CONFIG_VERSION +
	 * " Attempting to load your config version " + versionStr); }
	 * 
	 * List elementList = root.getChild(TOOLBOXES_STR).getChildren(); Iterator
	 * iter = elementList.iterator(); Element elem; String toolbox;
	 * 
	 * while (iter.hasNext()) { elem = ((Element) iter.next()); toolbox =
	 * elem.getTextTrim();
	 * 
	 * if (!new File(toolbox).exists()) { logger.error("Error: Toolbox " +
	 * toolbox + " doesn't exists removing from config"); } else if
	 * (elem.getAttribute(TOOLBOX_TYPE_STR) != null) {
	 * tools.setToolBoxType(elem.getTextTrim(),
	 * elem.getAttributeValue(TOOLBOX_TYPE_STR)); } else {
	 * tools.addToolBox(elem.getTextTrim()); } }
	 * 
	 * elementList = root.getChild(COLORS_STR).getChildren(); iter =
	 * elementList.iterator(); while (iter.hasNext()) { Element colorElem =
	 * (Element) iter.next(); String typeStr =
	 * colorElem.getChild(TYPE_STR).getAttributeValue(NAME_STR); Element valElem
	 * = colorElem.getChild(VALUE_STR); int redVal =
	 * Integer.parseInt(valElem.getAttributeValue(RED_STR)); int greenVal =
	 * Integer.parseInt(valElem.getAttributeValue(GREEN_STR)); int blueVal =
	 * Integer.parseInt(valElem.getAttributeValue(BLUE_STR)); Color c = new
	 * Color(redVal, greenVal, blueVal); GUIEnv.setCableColor(typeStr, c); }
	 * 
	 * elementList = root.getChild(OPTIONS_STR).getChildren(); iter =
	 * elementList.iterator(); while (iter.hasNext()) { Element optionElem =
	 * (Element) iter.next(); String nameStr =
	 * optionElem.getAttributeValue(NAME_STR);
	 * 
	 * Object value = ObjectMarshaller.marshallJDOMElementToJava(optionElem);
	 * setUserProperty(nameStr, value); }
	 * 
	 * // color table elements Element colorTableElem =
	 * root.getChild(COLOR_TABLE_STR); if (colorTableElem != null) { elementList
	 * = colorTableElem.getChildren(); iter = elementList.iterator(); while
	 * (iter.hasNext()) { Element ctEntryElem = (Element) iter.next(); String
	 * colorName = ctEntryElem.getAttributeValue(NAME_STR); Color value =
	 * (Color) ObjectMarshaller.marshallJDOMElementToJava(ctEntryElem);
	 * ColorTableEntry ctEntry = new ColorTableEntry(colorName, value);
	 * colorTableEntries.add(ctEntry); } }
	 * 
	 * Element filesElem = root.getChild(FILES_STR); if (filesElem != null) {
	 * elementList = filesElem.getChildren(RECENT_STR); iter =
	 * elementList.iterator(); while (iter.hasNext()) { Element recent =
	 * (Element) iter.next();
	 * addRecentFilePath(recent.getChild(FILE_STR).getAttributeValue
	 * (VALUE_STR)); }
	 * 
	 * elementList = filesElem.getChildren(OPEN_GROUPS); iter =
	 * elementList.iterator(); while (iter.hasNext()) { Element open = (Element)
	 * iter.next(); Element fileElem = open.getChild(FILE_STR); OpenTaskGraph
	 * info = new OpenTaskGraph();
	 * info.setRootFileName(fileElem.getAttributeValue(VALUE_STR)); List
	 * childElemList = open.getChildren(CHILD_STR); for (Iterator iterator =
	 * childElemList.iterator(); iterator.hasNext();) {
	 * recursivelyLoadChildTaskGraphElements(info, (Element) iterator.next()); }
	 * savedTaskgraphs.add(info); } }
	 * 
	 * Element readersElem = root.getChild(FILE_READERS_STR); Element reader;
	 * 
	 * if (readersElem != null) { elementList =
	 * readersElem.getChildren(READER_STR); iter = elementList.iterator(); while
	 * (iter.hasNext()) { reader = (Element) iter.next();
	 * filereaders.put(reader.getAttributeValue(FILE_EXTENSION_STR),
	 * reader.getAttributeValue(TOOL_NAME_STR)); } }
	 * 
	 * Element writersElem = root.getChild(FILE_WRITERS_STR); Element writer;
	 * 
	 * if (writersElem != null) { elementList =
	 * writersElem.getChildren(WRITER_STR); iter = elementList.iterator(); while
	 * (iter.hasNext()) { writer = (Element) iter.next();
	 * filewriters.put(writer.getAttributeValue(FILE_EXTENSION_STR),
	 * writer.getAttributeValue(TOOL_NAME_STR)); } }
	 * 
	 * // fix an old bug in some old config files if
	 * (getUserProperty(HELP_VIEWER_STR).equals(Env.getString("defaultEditor")))
	 * { setUserProperty(HELP_VIEWER_STR, Env.getString("defaultViewer")); }
	 * 
	 * } catch (Exception e) { throw(e); } finally { FileUtils.closeReader(br);
	 * } }
	 * 
	 * private static void recursivelyLoadChildTaskGraphElements(OpenTaskGraph
	 * parent, Element childElem) { Element nameElem =
	 * childElem.getChild(NAME_STR); OpenTaskGraph info = new OpenTaskGraph();
	 * info.setName(nameElem.getAttributeValue(VALUE_STR));
	 * parent.addChild(info); List childElemList =
	 * childElem.getChildren(CHILD_STR); for (Iterator iterator =
	 * childElemList.iterator(); iterator.hasNext();) {
	 * recursivelyLoadChildTaskGraphElements(info, (Element) iterator.next()); }
	 * }
	 */

	/**
	 * Returns all of the class paths Triana searches through.
	 */
	public final static Vector allClasspaths() {
		return classpaths;
	}

	/**
	 * adds an item to the classpath list
	 * 
	 * @return -1 if a invalid URL path is given
	 */
	public final static int addClasspath(Object classpath) {
		if (classpaths == null) {
			classpaths = new Vector(10);
		}
		return addToVar(classpaths, classpath);
	}

	/**
	 * Removes the classpath at the specified position within the classpaths
	 * Vector
	 */
	public final static void removeClasspath(int classpathNo) {
		classpaths.removeElementAt(classpathNo);
	}

	/**
	 * @return the default clip-in used for history tracking
	 * 
	 *         public final static HistoryClipIn getDefaultHistoryClipIn() { try
	 *         { Class cls = Class.forName(DEFAULT_HISTORY_CLIPIN); return
	 *         (HistoryClipIn) cls.newInstance(); } catch (Exception except) {
	 *         throw(new RuntimeException(
	 *         "Error instantiating default history tracking clip-in: " +
	 *         DEFAULT_HISTORY_CLIPIN)); } }
	 */

	/**
	 * Returns the Triana GRID_HELP environmental variable.
	 */
	public final static Vector helpdirs() {
		return helpdirs;
	}

	/**
	 * adds an item to the help directory list
	 * 
	 * @return -1 if a invalid URL path is given
	 */
	public final static int addHelpdir(String helpDir) {
		if (helpdirs == null) {
			helpdirs = new Vector(10);
		}
		return addToVar(helpdirs, helpDir);
	}

	/**
	 * Removes the help directory at the specified position within the helpDir
	 * Vector
	 */
	public final static void removeHelpdir(int helpDirNo) {
		helpdirs.removeElementAt(helpDirNo);
	}

	/**
	 * Adds the specific object to the given vector. The vectors can be
	 * Classpaths, toolboxes or help files. If the argument is a String then we
	 * check to see if its a network or a local file. It is stored as a URL if
	 * its networks or a String if its local. The CLASSPATH is already split up
	 * into the various objects
	 */
	private final static int addToVar(Vector toAddTo, Object item) {
		try {
			if (item instanceof String) { // unparsed string
				if ((((String) item).indexOf("http:") != -1)
						|| (toAddTo.indexOf("ftp:") != -1)) {
					toAddTo.addElement(new java.net.URL((String) item));
				} else {
					toAddTo.addElement(item);
				}
			} else {
				toAddTo.addElement(item); // already parsed
			}
			return 1;
		} catch (java.net.MalformedURLException ee) {
			new IllegalArgumentException(item + " not a URL !! ");
			return -1;
		}
	}

	/**
	 * Gets all the types compiled in the $TRIANA/classes/triana/types
	 * 
	 * @return a StringVector containing a Vector of every TrianaType (i.e. each
	 *         type stored as a String).
	 * 
	 *         public static StringVector getAllTrianaTypes() { if (allTypes ==
	 *         null) { allTypes = new StringVector(); } else { return
	 *         allTypes.copy(); }
	 * 
	 *         String sep;
	 * 
	 *         if (isAnApplet) { sep = "/"; } else { sep = File.separator; }
	 * 
	 *         String typePath; typePath = Env.home() + "classes" + sep +
	 *         "triana" + sep + "types" + sep; Listing listing =
	 *         ListUtils.listAllFiles(typePath, "*.class", false); String[] l =
	 *         new String[0];
	 * 
	 *         if (listing != null) { l =
	 *         listing.justFileList().convertToStrings(); for (int i = 0; i <
	 *         l.length; ++i) { String typ =
	 *         l[i].substring(l[i].lastIndexOf(sep) + 1, l[i].length() - 6);
	 *         allTypes.add(typ); } } return allTypes.copy();
	 * 
	 *         }
	 * 
	 *         /** Parse the system default tools and colors file in
	 *         $TRIANA_V3/SYSTEM/TYPES/TrianaTypes
	 * 
	 *         public static StringVector getTrianaTypesAndDefaultColors() {
	 *         StringVector defaults = FileUtils.readAndSplitFile(typesFile());
	 *         StringVector compiled = getAllTrianaTypes(); for (int j = 0; j <
	 *         compiled.size(); j++) { String temp = new String(compiled.at(j));
	 *         temp = temp + " " + defaults.at(j); compiled.setElementAt(temp,
	 *         j); } return compiled; }
	 */

	private static String templateDirectory() {
		return Env.home() + "system" + File.separator + "templates"
				+ File.separator;
	}

	private static String typesFile() {
		return Env.home() + "system" + File.separator + "types"
				+ File.separator + "TrianaTypes";
	}

	/**
	 * Gets Triana's reference to its icon file
	 */
	public static String getIcon() {
		String path = Env.home() + "system" + File.separator + "icons"
				+ File.separator + "triana.png";
		return path;
	}

	/**
	 * Returns UWCC's copyright found in $TRIANA/system/templates/UWCCCopyright
	 * 
	 * @return UWCC's copyright
	 * 
	 *         public static String getCopyright() { try { String template =
	 *         templateDirectory() + "Copyright"; return
	 *         FileUtils.readFile(FileUtils.createReader(template)); } catch
	 *         (IOException except) {
	 *         System.err.println("Error Reading Copyright: " +
	 *         except.getMessage()); return ""; } }
	 */

	/**
	 * Loads in a template found in $TRIANA/system/templates/ i.e. BasicWindow,
	 * UserWindow, WindowUnit etc.
	 * 
	 * @return a string containing the contents of the template file
	 * 
	 *         public static String getTemplate(String template) { try {
	 *         template = templateDirectory() + template; return
	 *         FileUtils.readFile(FileUtils.createReader(template)); } catch
	 *         (IOException except) {
	 *         System.err.println("Error Reading Template: " +
	 *         except.getMessage()); return ""; } }
	 */

	/**
	 * Gets the ResourceBundle, which store the internationalized messages to
	 * display within triana. These are taken from the
	 * system/internationalization/triana_.._...properties file depending on
	 * which locale you are running in.
	 * 
	 * public static ResourceBundle getResourceBundle() { if (messages == null)
	 * { logger.info("Getting Locale Settings ...."); String path = home() +
	 * "system" + File.separator + "locale" + File.separator;
	 * 
	 * StringVector locale = FileUtils.readAndSplitFile(path + "settings");
	 * logger.info("Language = " + locale.at(0)); logger.info("Country = " +
	 * locale.at(1)); Locale currentLocale; currentLocale = new
	 * Locale(locale.at(0), locale.at(1)); String file = path + "triana_" +
	 * locale.at(0) + "_" + locale.at(1) + ".properties";
	 * 
	 * logger.info("Internationalization Bundle File = " + file);
	 * 
	 * // Can't use the standard resource bundle searching scheme for 2 reasons
	 * // 1. It ONLY searches the classpath // 2. Doesn't allow them to work
	 * over a network // so I wrote a direct method which works with http or
	 * local files :-
	 * 
	 * try { InputStream is = FileUtils.createInputStream(file); messages = new
	 * PropertyResourceBundle(is); is.close(); } catch (IOException ee) {
	 * logger.error("Couldn't get Resources from " + file, ee); } }
	 * 
	 * return messages; }
	 */

	/**
	 * Gets the ResourceBundle, which store the internationalized messages to
	 * display within triana. These are taken from the
	 * system/internationalization/triana_.._...properties file depending on
	 * which locale you are running in.
	 * 
	 * public static ResourceBundle getTips() { String sep = getFileSeparator();
	 * if (tips == null) { logger.info("Getting Local Tips Settings ....");
	 * String file = home() + "system" + sep + "tips" + sep + "tips.properties";
	 * 
	 * logger.info("Tips Bundle File = " + file);
	 * 
	 * try { InputStream is = FileUtils.createInputStream(file); tips = new
	 * PropertyResourceBundle(is); is.close(); } catch (IOException ee) {
	 * logger.error("Couldn't get Resources from " + file, ee); } } return tips;
	 * }
	 */

	/**
	 * @return the File.separator if we're local or "/" if we're running as an
	 *         applet
	 */
	public static String getFileSeparator() {
		if (home().indexOf("http") != -1) {
			return "/";
		} else {
			return File.separator;
		}
	}

	/**
	 * @return the system dependent path separator
	 */
	public static String getPathSeparator() {
		return System.getProperty("path.separator");
	}

	/**
	 * Gets the string from the resource bundle which store the messages to
	 * display within triana. These are taken from the
	 * system/locale/triana_.._...properties file depending on which locale you
	 * are running in
	 * 
	 * public static String getString(String word) { if (messages == null) {
	 * getResourceBundle(); }
	 * 
	 * try { return messages.getString(word); } catch (MissingResourceException
	 * except) { except.printStackTrace(); return word; } }
	 * 
	 * /** Gets the next tip from the tip resource file
	 * 
	 * public static String getNextTip() { if (!GUIEnv.getTipOfTheDay()) {
	 * return null; // return null if they are disabled }
	 * 
	 * if (tips == null) { getTips(); } String t = null; try { t =
	 * tips.getString((String) getUserProperty(TIP_NUM_STR)); } catch (Exception
	 * e) { // must be at the end of tips setUserProperty(TIP_NUM_STR, "0"); t =
	 * tips.getString("0"); }
	 * 
	 * int i = Integer.parseInt((String) getUserProperty(TIP_NUM_STR));
	 * GUIEnv.setTipOfTheDay(i++);
	 * 
	 * return t; }
	 * 
	 * final static boolean passwordOK() { return passwordOK; }
	 * 
	 * final static boolean notRunOut() { return !runOut; }
	 * 
	 * 
	 * public final static void verifyPassword(String p) { if
	 * (!p.equals("objectcon")) { System.exit(0); } else { passwordOK = true; }
	 * }
	 * 
	 * /** The base for all Triana home page.
	 */
	public static String homePage() {
		return "https://www.gridchem.org";
	}

	/**
	 * location of gridchem properties and preferences
	 * 
	 * @return
	 */
	public static String getPropertiesDir() {
		if (propertiesDir.equals("")) {
			// propertiesDir = getGlobusDir();
			propertiesDir = getGridchemDataDir() + separator() + "properties";
			File dir = new File(propertiesDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return propertiesDir;
	}

	/**
	 * location of user credentials files
	 * 
	 * @return
	 */
	public static String getSecurityDir() {
		if (securityDir.equals("")) {
			securityDir = getGridchemDataDir() + separator() + "security";
			File dir = new File(securityDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return securityDir;
	}

	/**
	 * Default location of all downloaded user data
	 * 
	 * @return
	 */
	public static String getApplicationDataDir() {
		if (applicationDataDir.equals("")) {
			applicationDataDir = getGridchemDataDir() + separator()
					+ ".application_data";
			File appDataDir = new File(applicationDataDir);
			if (!appDataDir.exists()) {
				appDataDir.mkdirs();
			}
		}
		return applicationDataDir;
	}

	public static String getApplicationSoftwareTemplateDir() {
		if (applicationSoftwareTemplateDir.equals("")) {
			applicationSoftwareTemplateDir = getApplicationDataDir()
					+ separator() + "templates";
			File appSoftwareTemplateDir = new File(
					applicationSoftwareTemplateDir);
			if (!appSoftwareTemplateDir.exists()) {
				appSoftwareTemplateDir.mkdirs();
			}
		}
		return applicationSoftwareTemplateDir;
	}

	public static String getApplicationTempDir() {
		File appTempDir = new File(getApplicationDataDir() + separator()
				+ "temp");
		if (!appTempDir.exists()) {
			appTempDir.mkdirs();
		}
		return appTempDir.getAbsolutePath();
	}
	
	public static String getUserCustomedInputFilesDir() {
		File inputFilesDir = new File(getGridchemDataDir() + separator() + "user_customed_input_files");
		if (!inputFilesDir.exists()) {
			inputFilesDir.mkdirs();
		}
		return inputFilesDir.getAbsolutePath();
	}

	/**
	 * Default location of all downloaded user data
	 * 
	 * @return
	 */
	public static String getUserDataDir() {
		if (userDataDir.equals("")) {
			userDataDir = getGridchemDataDir();// + separator() +
			// "data"; + separator() + getUserName();
			File dir = new File(userDataDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
		return userDataDir;
	}

	/**
	 * location of default gridchem trusted ca signing policies distributed with
	 * the client
	 * 
	 * @return
	 */
	public static String getTrustedCaDir() {
		if (trustedCaDir.equals("")) {
			trustedCaDir = getGridchemDataDir() + separator() + "certificates";
			File dir = new File(trustedCaDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return trustedCaDir;
	}

	public static boolean isWebStartApp() {
		return webStart;
	}

	public static String getGatAdaptorPath() {
		if (gatAdaptorPath.equals("")) {
			if (!isWebStartApp()) {
				gatAdaptorPath = home() + "lib" + separator() + "gat";
			} else {
				gatAdaptorPath = home().replaceAll("%20", " ");
				if (gatAdaptorPath.endsWith(File.separator))
					gatAdaptorPath = gatAdaptorPath.substring(0,
							gatAdaptorPath.length() - 1);
				if (!os().equals("windows")) {
					gatAdaptorPath = gatAdaptorPath.substring(
							gatAdaptorPath.indexOf("/"),
							gatAdaptorPath.length());
				}
			}
		}
		return gatAdaptorPath;
	}

	/**
	 * @return location of gridchem logo as a String
	 */
	public static String getGridChemLogoLocation() {
		return getImagesDir() + separator() + "logos/CCG.jpg";
	}

	public static String getTrustStoreLocation() {
		return getTrustedCaDir() + separator() + trustStore;
	}

	/**
	 * @return
	 */
	public static String getMacAddress() {
		return macAddress;
	}

	/**
	 * Add a recently accessed file path to the set of recent items
	 * 
	 * @param path
	 *            full path to the file accessed
	 * 
	 *            public final static void addRecentFilePath(String path) { if
	 *            (recentFileItems.size() >= RECENT_ITEM_COUNT) {
	 *            recentFileItems.remove(0); } if
	 *            (!recentFileItems.contains(path)) { recentFileItems.add(path);
	 *            } writeConfig(); }
	 * 
	 *            /**
	 * @return the recent list of files accessed
	 * 
	 *         public final static String[] getRecentFilePaths() { if
	 *         (restoredFromDisk) { Vector validatedItems = new Vector(); for
	 *         (Iterator iterator = recentFileItems.iterator();
	 *         iterator.hasNext();) { String s = (String) iterator.next(); if
	 *         (FileUtils.fileExists(s)) { validatedItems.add(s); } }
	 *         recentFileItems = validatedItems;
	 * 
	 *         return (String[]) validatedItems.toArray(new
	 *         String[validatedItems.size()]); } else { return new String[0]; }
	 *         }
	 * 
	 *         /** Adds or replaces a color table entry
	 * 
	 *         public static void setColorTableEntry(ColorTableEntry entry) {
	 *         for (Iterator iterator = colorTableEntries.iterator();
	 *         iterator.hasNext();) { ColorTableEntry colorTableEntry =
	 *         (ColorTableEntry) iterator.next(); if
	 *         (colorTableEntry.getColorname().equals(entry.getColorname()))
	 *         iterator.remove(); } colorTableEntries.add(entry); }
	 * 
	 *         /**
	 * @return an array of the currently set colour table entries
	 * 
	 *         public static ColorTableEntry[] getColorTableEntries() { return
	 *         (ColorTableEntry[]) colorTableEntries.toArray(new
	 *         ColorTableEntry[colorTableEntries.size()]); }
	 * 
	 *         /** Inner class that acts as a timer for writing out the
	 *         currently open taskgraphs
	 * 
	 *         private static class WriteStateThread extends Thread {
	 * 
	 *         private boolean stop = false; protected int SLEEP_DELAY = 30000;
	 * 
	 *         public WriteStateThread() { this.setName("Triana State Saving");
	 *         this.setPriority(Thread.MIN_PRIORITY); this.start(); }
	 * 
	 *         /** Stop the thread
	 * 
	 *         public void stopThread() { if (!stop) {
	 *         setPriority(Thread.NORM_PRIORITY); }
	 * 
	 *         stop = true; interrupt(); }
	 * 
	 *         /**
	 * @return true if the thread has been stopped
	 * 
	 *         public boolean isStopped() { return stop; }
	 * 
	 *         /** Trigger the thread to (asynchronously)write out the config
	 *         file
	 * 
	 *         public void write() { interrupt(); }
	 * 
	 *         public void run() { while (!isStopped()) { writeStateFiles(); try
	 *         { sleep(SLEEP_DELAY); } catch (InterruptedException except) { } }
	 * 
	 *         writeStateFiles(); } }
	 * 
	 *         /** Inner class that acts as a timer to write the config files
	 * 
	 *         private static class WriteConfigThread extends Thread {
	 * 
	 *         private ToolTable tools; private boolean stop = false; protected
	 *         int SLEEP_DELAY = 30000;
	 * 
	 *         public WriteConfigThread(ToolTable tools) {
	 *         this.setName("Triana Config Writer");
	 *         this.setPriority(Thread.MIN_PRIORITY); this.tools = tools;
	 *         this.start(); }
	 * 
	 *         /** Stop the thread
	 * 
	 *         public void stopThread() { if (!stop) {
	 *         setPriority(Thread.NORM_PRIORITY); }
	 * 
	 *         stop = true; interrupt(); }
	 * 
	 *         /**
	 * @return true if the thread has been stopped
	 * 
	 *         public boolean isStopped() { return stop; }
	 * 
	 *         /** Trigger the thread to (asynchronsly)write out the config file
	 * 
	 *         public void write() { interrupt(); }
	 * 
	 *         public void run() { while (!isStopped()) {
	 *         writeConfigFile(tools); try { sleep(SLEEP_DELAY); } catch
	 *         (InterruptedException except) { } }
	 * 
	 *         writeConfigFile(tools); } }
	 */
}