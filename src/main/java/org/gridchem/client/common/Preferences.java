/* Copyright (c) 2004, University of Illinois at Urbana-Champaign. 
 * All rights reserved.
 * 
 * Created on Sep 16, 2005
 * 
 * Developed by: CCT, Center for Computation and Technology, 
 *               NCSA, University of Illinois at Urbana-Champaign
 *               OSC, Ohio Supercomputing Center
 *               TACC, Texas Advanced Computing Center
 *               UKy, University of Kentucky
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
 *    University of Illinois at Urbana-Champaign, nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this Software without specific prior written permission.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  
 * IN NO EVENT SHALL THE CONTRIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS WITH THE SOFTWARE.
*/

package org.gridchem.client.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeListener;

import org.gridchem.client.GridChem;
import org.gridchem.client.PutFile;
import org.gridchem.client.Trace;
import org.gridchem.client.exceptions.GMSException;
import org.gridchem.client.exceptions.SynchronizationException;
import org.gridchem.client.util.Env;


/**
 * This class models a database of GridChem user preferences.  
 * Example preferences are the user name, the name of the myproxy server,
 * the names of the High Performance Computing machines, etc.
 * 
 * Preferences are persistent.
 * 
 * This persistence exists both on a local client machine and on the
 * mass storage server.  Kent Milfeld has outlined the persistence policy:
 * If a user changes the preferences, the local file should be updated,
 * and then IMMEDIATELY it should be written back to mss.  (When we use
 * the DataBase (DB), it should immediately update the DB.) This should
 * apply to other information that needs to be persistent-- update
 * immediately so that a user can kill the client without any consequences.
 * The information needs to be both local and at the server.
 * It needs to be at the server so that a user can use a client on
 * several machines.  The server will hold the latest updated preferences.  
 * It needs to be local, so that a user can run off line and the client
 * can know where an alternate (non-default) gridchem directory might
 * be located BEFORE authentication occurs.  Only after authentication
 * can the user pick up the latest preferences from the server.
 * Work on multi-platform support is in progress.
 *
 * The Singleton creation pattern is supported.  However, this class is
 * frequently used as a static entity via the static and
 * non java.util.prefs.Preferences methods getString and putString.
 * Sometime this should be reconciled.  Currently, there is no static
 * initializer because early construction occurs via instantiation in the
 * Settings constructor and Settings is instantiated in GridChem.main.
 * The implementation employs java.util.Properties which is essentially
 * a persistent java.util.Hashtable. 
 *
 * @author Rion Dooley < dooley [at] cct [dot] lsu [dot] edu >
 * @author Scott Brozell < srb [at] osc [dot] edu >
 *
 */
public class Preferences extends java.util.prefs.Preferences {

    /**
     * The filename (not the pathname) of the local preferences.
     */
    private static final String LOCAL_PREF_FILENAME = "preferences.hist";

    /**
     * The filename (not the pathname) of the remote (MSS) preferences.
     */
    private static final String REMOTE_PREF_FILENAME = "preferences.hist";

    private static final String PREFERENCES_FILE_BANNER =
        "###################################################################"
        + "\n" +
        "# This is the GridChem preferences file. This file was automatically"
        + "\n" +
        "# generated by the GridChem client. Do not modify this file."
        + "\n" +
        "###################################################################\n";

    /**
     * The full pathname of the local preferences.
     */
    private static String theLocalPrefPathname;

    /**
     * The full pathname of the remote (MSS) preferences.
     */
    private static String remotePrefPathname;

    /**
     * The container that holds all the data, i.e., the database.
     */
    private static Properties prefs;

    /**
     * A reference to the only possible instance of class Preferences.
     */
    private static Preferences singleton;
    
    /**
     * The constructor is private to support the Singleton creation
     * pattern by preventing instantiation by other classes. 
     */
    private Preferences() {
        Trace.entry();
        theLocalPrefPathname = Settings.defaultDirStr + Env.separator() +
            LOCAL_PREF_FILENAME;
        // the pathname of the remote preferences file is unknown
        // TODO delete this member
        remotePrefPathname = Settings.mss + ":/ERROR_UNKNOWN_PATH" +
            Env.separator() + REMOTE_PREF_FILENAME;
        prefs = new Properties();
        init();
        Trace.exit();
    }
    
    /**
     * Get the Preferences object.  This is the only way because the
     * Singleton creation pattern is employed.
     * 
     * @return the Preferences object
     */
    public synchronized static Preferences getInstance() {
        if ( null == singleton ) {
            singleton = new Preferences();
        }
        return singleton;
    }

    /**
     * Cloning is not supported because the Singleton creation pattern
     * is employed.
     * 
     * @return nothing
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
    // java.util.prefs.Preferences does not implement clone, but
    // this prevents bypassing the Singleton creation pattern.
        throw new CloneNotSupportedException(); 
    }
    
    /**
     * Associates the specified value with the specified key in the
     * preferences database.
     * This method of putting only modifies the preferences database
     * if the key does not exist in the database or if its value is different.
     * 
     * @param key   the name of the preference to be put.
     * @param value the associated preference data to be put.
     * @see forcedPut(java.lang.String, java.lang.String)
     * @see java.util.prefs.Preferences#put(java.lang.String, java.lang.String)
     */
    public void put(String key, String value) {
        String currentData = prefs.getProperty(key) ;
        if ( currentData == null || ! currentData.equals(value) ) {
            prefs.put(key,value);
            save();
        }
    }
    
    /**
     * Associates the specified value with the specified key in the
     * preferences database.
     * This method of putting always modifies the preferences database.
     * 
     * @param key   the name of the preference to be put.
     * @param value the associated preference data to be put.
     * @see put(java.lang.String, java.lang.String)
     * @see java.util.prefs.Preferences#put(java.lang.String, java.lang.String)
     */
    public void forcedPut(String key, String value) {
        prefs.put(key,value);
        save();
    }
    
    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#get(java.lang.String, java.lang.String)
     */
    public String get(String key, String def) {
        // TODO Auto-generated method stub
        return prefs.getProperty(key,def);
    }
    
    
    /** Static get method returning String object
     * @param key
     * @return
     */
    public static String getString(String key) {
        String retval = "";
        try{
            retval = (String) prefs.get(key);
            return retval;
        }catch(Exception e) {
            return "";
        }
    }
    
    public static void putString(String key, String value) {
        prefs.put(key,value);
        save();
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#remove(java.lang.String)
     */
    public void remove(String key) {
        // TODO Auto-generated method stub
        prefs.remove(key);
        save();
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#clear()
     */
    public void clear() throws BackingStoreException {
        // TODO Auto-generated method stub
        prefs.clear();
        new File(theLocalPrefPathname).delete();
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#putInt(java.lang.String, int)
     */
    public void putInt(String key, int value) {
        // TODO Auto-generated method stub
        prefs.put(key,Integer.toString(value));
        save();
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#getInt(java.lang.String, int)
     */
    public int getInt(String key, int def) {
        // TODO Auto-generated method stub
        return Integer.parseInt((String) prefs.get(key));
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#putLong(java.lang.String, long)
     */
    public void putLong(String key, long value) {
        // TODO Auto-generated method stub
        prefs.put(key,Long.toString(value));
        save();
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#getLong(java.lang.String, long)
     */
    public long getLong(String key, long def) {
        // TODO Auto-generated method stub
        return Long.parseLong(prefs.getProperty(key,Long.toString(def)));
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#putBoolean(java.lang.String, boolean)
     */
    public void putBoolean(String key, boolean value) {
        // TODO Auto-generated method stub
        if(value == true)
            prefs.put(key, new Integer(1));
        else
            prefs.put(key, new Integer(0));
        save();
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#getBoolean(java.lang.String, boolean)
     */
    public boolean getBoolean(String key, boolean def) {
        // TODO Auto-generated method stub
        int value = ((Integer) prefs.get(key)).intValue();
        if(value == 0)
            return false;
        else
            return true;
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#putFloat(java.lang.String, float)
     */
    public void putFloat(String key, float value) {
        // TODO Auto-generated method stub
        prefs.put(key,new Float(value));
        save();
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#getFloat(java.lang.String, float)
     */
    public float getFloat(String key, float def) {
        // TODO Auto-generated method stub
        return Float.parseFloat(prefs.getProperty(key, Float.toString(def)));
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#putDouble(java.lang.String, double)
     */
    public void putDouble(String key, double value) {
        // TODO Auto-generated method stub
        prefs.put(key,new Double(value));
        save();
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#getDouble(java.lang.String, double)
     */
    public double getDouble(String key, double def) {
        // TODO Auto-generated method stub
        return Double.parseDouble(prefs.getProperty(key,Double.toString(def)));
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#putByteArray(java.lang.String, byte[])
     */
    public void putByteArray(String key, byte[] value) {
        // TODO Auto-generated method stub
        prefs.put(key,new String(value));
        save();
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#getByteArray(java.lang.String, byte[])
     */
    public byte[] getByteArray(String key, byte[] def) {
        // TODO Auto-generated method stub
        byte[] value = ((String)prefs.get(key)).getBytes();
        return value;
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#keys()
     */
    public String[] keys() throws BackingStoreException {
        // TODO Auto-generated method stub
        String[] keys = null;
        int i=0;
        for(Enumeration e = prefs.keys(); e.hasMoreElements();) {
            keys[i] = (String) e.nextElement();
            i++;
        }
        return keys;
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#childrenNames()
     */
    public String[] childrenNames() throws BackingStoreException {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#parent()
     */
    public java.util.prefs.Preferences parent() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#node(java.lang.String)
     */
    public java.util.prefs.Preferences node(String pathName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#nodeExists(java.lang.String)
     */
    public boolean nodeExists(String pathName) throws BackingStoreException {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#removeNode()
     */
    public void removeNode() throws BackingStoreException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#name()
     */
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#absolutePath()
     */
    public String absolutePath() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#isUserNode()
     */
    public boolean isUserNode() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#toString()
     */
    public String toString() {
        // TODO Auto-generated method stub
        return prefs.toString();
    }
    
    public static void print() {
        System.out.println(prefs.toString());
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#flush()
     */
    public void flush() throws BackingStoreException {
        // TODO Auto-generated method stub
        save();
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#sync()
     */
    public void sync() throws BackingStoreException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#addPreferenceChangeListener(java.util.prefs.PreferenceChangeListener)
     */
    public void addPreferenceChangeListener(PreferenceChangeListener pcl) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#removePreferenceChangeListener(java.util.prefs.PreferenceChangeListener)
     */
    public void removePreferenceChangeListener(PreferenceChangeListener pcl) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#addNodeChangeListener(java.util.prefs.NodeChangeListener)
     */
    public void addNodeChangeListener(NodeChangeListener ncl) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#removeNodeChangeListener(java.util.prefs.NodeChangeListener)
     */
    public void removeNodeChangeListener(NodeChangeListener ncl) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#exportNode(java.io.OutputStream)
     */
    public void exportNode(OutputStream os) throws IOException, BackingStoreException {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.util.prefs.Preferences#exportSubtree(java.io.OutputStream)
     */
    public void exportSubtree(OutputStream os) throws IOException, BackingStoreException {
        // TODO Auto-generated method stub
        
    }

    public static void init() {
        Trace.entry();
        String oldPrefPathname = Settings.defaultDirStr + Env.separator() +
            getLocalPrefFilename();
        if ( loadLocalAndValidate(theLocalPrefPathname) ) {
        	;
        }
        Trace.exit();
    }
    
    public static void updatePrefs() {
    	Trace.entry();
    	Trace.note("Updating user preference file ");
    	
    	File prefFile = new File(Preferences.theLocalPrefPathname);
    	if (prefFile.length() == 0) {
    		initializeToDefaults();
    	} else {
    		prefs.put("gridchem_username", Settings.gridchemusername);
    		save();
    	}
    	Trace.exit();
    }
    
    /**
     * Load a local preferences file and validate its contents.
     * A valid file contains the "version" key and contains the correct
     * GridChem username and specifies a user data directory that exists.
     * In all other cases, including nonexistence of the file and/or
     * IO exceptions, the file is not valid.
     *
     * @param localPrefPathname load the preferences from this local pathname.
     * @return true if the file was successfully loaded and validated.
     */
    private static boolean loadLocalAndValidate(String localPrefPathname) {
        Trace.entry();
        Trace.note("localPrefPathname = " + localPrefPathname);
        //Trace.note("Settings.gridchemusername = " + Settings.gridchemusername);
        boolean isValid = false;
        File prefFile = new File(localPrefPathname);
        if(prefFile.exists()) {
            try {
                if (prefFile.length() != 0) {
                    
                	InputStream is = new FileInputStream(prefFile);
                	prefs.load(is);
                	is.close();
                	if (prefs.containsKey("gridchem_username")) {
                		Trace.note("Retrieved default username :" + prefs.get("gridchem_username"));
                		Settings.gridchemusername = (String) prefs.get("gridchem_username");
                	}
                	if (Settings.authenticated) {
                		isValid = prefs.containsKey("version") &&
                		//getString("gridchem_username").equals(Settings.gridchemusername) &&
                        (new File(getString("user_data_directory"))).exists();
                	} else {
                		isValid = true;
                	}
                }
                Trace.note("Found preferences and they " + ((isValid)?"are":"are not") + " valid.");
            } catch (IOException ioe) {
                System.err.println("Error loading and validating GridChem " +
                                   "preferences file: " + localPrefPathname);
                System.err.println("Searching for a valid preferences file.");
                ioe.printStackTrace();
            }
        } else {
        	try {
        		prefFile.createNewFile();
        		isValid=true;
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
        Trace.exit();
        return isValid;
    }

    /**
     * Load a remote preferences file and validate its contents.
     * Use the same validation criteria as loadLocalAndValidate.
     *
     * @param prefPathname load the preferences from this pathname.
     * @return true if the file was successfully loaded and validated.
     * @see loadLocalAndValidate
     */
    public static boolean loadRemoteAndValidate(String filePath) {
        Trace.entry();
//        
//        TODO: rewrite preferences since we're not handling them on the server side anymore
//        try {
//            String remotePrefs = GridChem.user.get
//            
//            if(remotePrefs.equals("") || remotePrefs == null) {
//                initializeToDefaults();
//            } else {
//                FileWriter prefFile = new FileWriter(filePath);
//    
//                prefFile.write(remotePrefs);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//            
//        }
//        Trace.exit();
        initializeToDefaults();
        
        return loadLocalAndValidate(filePath);
    }
    
    public static void loadRemotePrefs(String remotePrefs) {
        Trace.entry();
        
        String prefFilePath = Env.getGridchemDataDir() + 
            Env.separator() + getLocalPrefFilename();
        
        try {
            if(remotePrefs.equals("") || remotePrefs == null) {
                initializeToDefaults();
            } else {
                FileWriter prefFile = new FileWriter(prefFilePath);
    
                prefFile.write(remotePrefs);
            }
            init();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SynchronizationException("Could not write preferences to disk. " + 
                    "Preferences remain unchanged.",e);
        }
        
        Trace.exit();
    }
    /**
     * Save the preferences.  This is the API for and the preferred way
     * to save the preferences.  It follows the persistence policy of 
     * the preferences, and this interface hides all the implementation
     * details of persistence.
     */
    public static void save() {
        save(theLocalPrefPathname);
    }
    
    /**
     * Save the preferences both on a local client machine and on the
     * mass storage server.  This saving in both places should be but
     * is not guaranteed to be an atomic operation.  This is the
     * implementation of the API save method.  It uses the canonical
     * machine, path, and filename for the mass storage saving.
     *
     * @param localPathname save the preferences in this local pathname.
     */
    private static void save(String localPathname) {
        Trace.entry();
        Trace.note( "localPathname = " + localPathname );
        //Trace.printStackTrace();
        if (Settings.authenticated) {
            OutputStream fos;
            try {
                // Local and mss saving of preferences should be an atomic
                // operation.  However, PutFile catches but does not recover
                // from normal exceptions.  Thus, local saving could succeed
                // and mss saving could fail.
                File prefFile = new File(localPathname);
                prefFile.createNewFile(); // will create if not already present
                fos = new FileOutputStream(localPathname);
                prefs.store(fos, PREFERENCES_FILE_BANNER);
                fos.close();
                
                if (Settings.WEBSERVICE) {
            	    fos = new ByteArrayOutputStream();
                    prefs.store(fos,PREFERENCES_FILE_BANNER);
//                    GMS.getInstance().putPreferences(fos.toString());
                } else {
                    PutFile pf1 = new PutFile(theLocalPrefPathname,Settings.mss);
                }
            } catch (IOException ioe) {
                // Why are we not attempting to recover ?
                // This crude error reporting should be improved.
                System.err.println("Error writing GridChem preferences!!");
                System.err.println(ioe.toString());
                ioe.printStackTrace();
            } catch (Exception e) {
                GridChem.appendMessage(e.getMessage());
                e.printStackTrace();
            }
        }
        Trace.exit();
    }
    
    /**
     * Make a local (GridChem client machine) copy of the preferences.
     * This has nothing to do with the persistence policy of 
     * the preferences.
     *
     * @param localPathname copy the preferences into this local pathname.
     * @throws IOException
     */
    public static void makeLocalCopy(String localPathname) throws IOException {
        FileOutputStream fos = new FileOutputStream(localPathname);
        prefs.store(fos, PREFERENCES_FILE_BANNER);
        fos.close();
    }
    
    /**
     * Initialize the preferences to their default values.
     * Is it intentional that this does not clear all the preferences ?
     */
    public static void initializeToDefaults() {
        Trace.entry();
        //prefs.put("version","0.2alpha");
        prefs.put("version","2.0.0");  //2006/01/31 skk - is this needed?
        prefs.put("user_data_directory", Env.getUserDataDir());
        
     
        prefs.put("gridchem_username", Settings.gridchemusername);
        
        prefs.put("gridchem_usertype","community");
        
        prefs.put("mass_storage","mss.ncsa.uiuc.edu");
        
        
        prefs.put("research_project_name", Settings.gridchemusername + "_proj");
        
        prefs.put("job_name","default_test");
        prefs.put("application","gaussian");
        prefs.put("project","gaussian");
        
        save(theLocalPrefPathname);
        
        Trace.exit();
    }
    
    /**
     * Return the filename (not the pathname) of the local preferences.
     * 
     * @return the filename of the local preferences.
     */
    public static String getLocalPrefFilename() {
        return LOCAL_PREF_FILENAME;
    }
    
    /**
     * Indicate that an external event has caused the Preferences to
     * become stale.  Example events are clearing the login via
     * reauthentication.
     */
    public static void setStale() {
        singleton = null;
    }
    
    public static void main(String argv[]) {
        Trace.entry();
        Preferences p = Preferences.getInstance();
        Trace.note( "Test put and forcedPut." );
        Trace.note( "put( gridchem_username, srb )" );
        p.put( "gridchem_username", "srb" );
        Trace.note( "put( scott, junk )" );
        p.put( "scott", "junk" );
        Trace.note( "forcedPut( scott, junk )" );
        p.forcedPut( "scott", "junk" );
        Trace.exit();
    }
    

    public static boolean ws_deserializePrefs(String prefString) {
    		try {
    			System.out.println(prefString);
    		} catch (Exception e) {
    			if (Settings.DEBUG) 
    				e.printStackTrace();
    			GridChem.appendMessage(e.getMessage());
    			return false;
    		}
    		return true;
    }
    
    public static String ws_serializePrefs() {
		return prefs.toString();
	}
}
