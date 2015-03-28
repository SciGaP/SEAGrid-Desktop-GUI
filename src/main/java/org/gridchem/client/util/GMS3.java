/* 
ƒ * Created on Mar 7, 2008
 * 
 * Developed by: Rion Dooley - dooley [at] tacc [dot] utexas [dot] edu
 * 				 TACC, Texas Advanced Computing Center
 * 
 * https://www.tacc.utexas.edu/
 */

package org.gridchem.client.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.swing.JOptionPane;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.gridchem.client.GridChem;
import org.gridchem.client.Invariants;
import org.gridchem.client.SubmitJobsWindow;
import org.gridchem.client.common.Preferences;
import org.gridchem.client.common.Settings;
import org.gridchem.client.common.Status;
import org.gridchem.client.common.StatusEvent;
import org.gridchem.client.gui.filebrowser.commands.FileCommand;
import org.gridchem.client.gui.jobsubmission.commands.JobCommand;
import org.gridchem.client.gui.jobsubmission.commands.SUBMITCommand;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.client.util.file.CalloutFileOutputStream;
import org.gridchem.client.util.file.FileUploader;
import org.gridchem.service.beans.CollaboratorBean;
import org.gridchem.service.beans.ComputeBean;
import org.gridchem.service.beans.FileBean;
import org.gridchem.service.beans.JobBean;
import org.gridchem.service.beans.LogicalFileBean;
import org.gridchem.service.beans.NotificationBean;
import org.gridchem.service.beans.ProjectBean;
import org.gridchem.service.beans.SoftwareBean;
import org.gridchem.service.beans.UserBean;
import org.gridchem.service.exceptions.FileException;
import org.gridchem.service.exceptions.FileManagementException;
import org.gridchem.service.exceptions.JobException;
import org.gridchem.service.exceptions.JobSubmissionException;
import org.gridchem.service.exceptions.ProjectException;
import org.gridchem.service.exceptions.ResourceException;
import org.gridchem.service.exceptions.SessionException;
import org.gridchem.service.exceptions.SoftwareException;
import org.gridchem.service.exceptions.UserException;
import org.gridchem.service.karnak.JobPredictionService;
import org.gridchem.service.karnak.Karnak;
import org.gridchem.service.model.enumeration.AccessType;
import org.gridchem.service.socket.FileDownloadThread;
import org.gridchem.service.stub.GMSClient;
import org.gridchem.service.stub.file.ExceptionException;
import org.gridchem.service.stub.file.FileServiceStub.DeleteDir;
import org.gridchem.service.stub.file.FileServiceStub.DeleteFile;
import org.gridchem.service.stub.file.FileServiceStub.GetCachedInputFile;
import org.gridchem.service.stub.file.FileServiceStub.GetRemoteFile;
import org.gridchem.service.stub.file.FileServiceStub.ListCachedInputFiles;
import org.gridchem.service.stub.file.FileServiceStub.ListCachedInputFilesForJob;
import org.gridchem.service.stub.file.FileServiceStub.Mkdir;
import org.gridchem.service.stub.file.FileServiceStub.PutCachedFile;
import org.gridchem.service.stub.file.FileServiceStub.Rename;
import org.gridchem.service.stub.job.JobServiceStub;
import org.gridchem.service.stub.job.JobServiceStub.Delete;
import org.gridchem.service.stub.job.JobServiceStub.Hide;
import org.gridchem.service.stub.job.JobServiceStub.Kill;
import org.gridchem.service.stub.job.JobServiceStub.PredictStartTime;
import org.gridchem.service.stub.job.JobServiceStub.Search;
import org.gridchem.service.stub.job.JobServiceStub.Submit;
import org.gridchem.service.stub.job.JobServiceStub.Unhide;
import org.gridchem.service.stub.job.JobServiceStub.UnhideAll;
import org.gridchem.service.stub.notification.NotificationServiceStub.GetNotifications;
import org.gridchem.service.stub.notification.NotificationServiceStub.Register;
import org.gridchem.service.stub.notification.NotificationServiceStub.Remove;
import org.gridchem.service.stub.notification.NotificationServiceStub.RemoveForJob;
import org.gridchem.service.stub.project.ProjectServiceStub.GetCollaborators;
import org.gridchem.service.stub.project.ProjectServiceStub.GetCurrentProject;
import org.gridchem.service.stub.project.ProjectServiceStub.GetProjectCollaborators;
import org.gridchem.service.stub.project.ProjectServiceStub.GetProjects;
import org.gridchem.service.stub.resource.ResourceServiceStub.GetComputeResources;
import org.gridchem.service.stub.session.SessionServiceStub;
import org.gridchem.service.stub.session.SessionServiceStub.CreateSession;
import org.gridchem.service.stub.session.SessionServiceStub.DestroySession;
import org.gridchem.service.stub.session.SessionServiceStub.RenewSession;
import org.gridchem.service.stub.session.SessionServiceStub.SetSessionProject;
import org.gridchem.service.stub.software.SoftwareServiceStub.GetAllSoftware;
import org.gridchem.service.stub.user.UserServiceStub.GetProfile;
import org.gridchem.service.util.crypt.SHA1;

import com.asprise.util.ui.progress.ProgressDialog;

import org.gridchem.client.util.file.*; //added nikhil

@SuppressWarnings("unchecked")
public class GMS3 {

    private static GMSClient client = null;
    public static String sessionKey = null;

    private StatusListener statusListener;
    
    /* ************************************************************************************* */
    /*                                                                                       */
    /*                          Authentication Methods                                       */
    /*                                                                                       */
    /* ************************************************************************************* */
    /**
     * Login to the GMS under the current session.  and forward the results to the
     * calling class.
     * 
     * @param uname user's CCG username
     * @param passwd user's CCG password
     * @param macAddress MAC address of the user's computer
     * @param accessType mechanism with which the user is authenticating
     * @return
     */

    
//	public static void login(LoginCommand command) throws SessionException {
//        
//		CreateSession params = new SessionServiceStub.CreateSession();
//		params.setArgs0((String)command.getArguments().get("username"));
//		params.setArgs1(SHA1.encrypt((String)command.getArguments().get("password")));
//		params.setArgs2(Settings.xstream.toXML((String)command.getArguments().get("map")));
//		params.setArgs3(((AccessType)command.getArguments().get("type")).name());
//        
//        try {
//			sessionKey = getClient().getSessionService().createSession(params).get_return();
//		} catch (java.lang.Exception e) {
//			setStatus(Status.FAILED, command);
//			throw new SessionException("Failed to create session", e);
//		} 
//        
//    }

    public static boolean login(String uname, String passwd, AccessType type, HashMap<String,String> authMap) {
        
    	CreateSession params = new SessionServiceStub.CreateSession();
    	params.setArgs0(uname);
    	params.setArgs1(SHA1.encrypt(passwd));
    	params.setArgs2(Settings.xstream.toXML(authMap));
    	params.setArgs3(type.name());
    	
    	try {
			sessionKey = getClient().getSessionService().createSession(params).get_return();
		} catch (java.lang.Exception e) {
			throw new SessionException(e.getMessage());
		} 
        Settings.gridchemusername = uname; //added nikhil
        Settings.authenticated = true;
        
        Preferences.updatePrefs();
        
        System.out.println("Successfully authenticated.  Key is " + sessionKey);
        return true;
        
    }
    
    public static void logout() {
    	DestroySession params = new DestroySession();
    	params.setArgs0(sessionKey);
    	
    	try {
    		
    		getClient().getSessionService().destroySession(params);
    	
    	} catch (java.lang.Exception e) {}
    	
    	sessionKey = null;
    }

    public static List<ProjectBean> getProjects() throws ProjectException {
        
    	GetProjects params = new GetProjects();
    	params.setArgs0(sessionKey);
    	
        String results;
		try {
			results = getClient().getProjectService().getProjects(params).get_return();
		} catch (java.lang.Exception e) {
			throw new ProjectException("Failed to retrieve user projects",e);
		}
        
        
        ArrayList<ProjectBean> projects = (ArrayList<ProjectBean>)Settings.xstream.fromXML(results);
        
        return projects;
    }
    
    public static ProjectBean getCurrentProject() throws ProjectException {
        
    	GetCurrentProject params = new GetCurrentProject();
    	params.setArgs0(sessionKey);
    	
        String results;
		try {
			results = getClient().getProjectService().getCurrentProject(params).get_return();
		} catch (java.lang.Exception e) {
			throw new ProjectException("Failed to retrieve user projects",e);
		}
        
        
        ProjectBean project = (ProjectBean)Settings.xstream.fromXML(results);
        
        return project;
    }

    public static List<ProjectBean> getProjects(JobCommand command) throws ProjectException {
        System.out.println("Key is " + sessionKey);
    	GetProjects getProjects = new GetProjects();
    	getProjects.setArgs0(sessionKey);
    	
        String results = null;
        try {
        	
        	results = getClient().getProjectService().getProjects(getProjects).get_return();
        	
        } catch (java.lang.Exception e) {
        	command.setOutput(results);
        	setStatus(Status.FAILED, command);
			throw new ProjectException("Failed to retrieve user projects",e);
		}
        
        if (results == null) throw new org.gridchem.service.exceptions.ProjectException("Failed to retrieve user projects");
        
        ArrayList<ProjectBean> projects = (ArrayList<ProjectBean>)Settings.xstream.fromXML(results);
        
        command.setOutput(projects);
        
        setStatus(Status.COMPLETED,command);
        
        return projects;
    }
    
//    public static void setCurrentProject(LoginCommand command) throws SessionException {
//        
//    	SetSessionProject params = new SetSessionProject();
//    	params.setArgs0(sessionKey);
//    	params.setArgs1(((ProjectBean)command.getArguments().get("project")).getId().toString());
//    	
//    	try {
//			
//    		getClient().getSessionService().setSessionProject(params);
//    		
//		} catch (java.lang.Exception e) {
//			setStatus(Status.FAILED, command);
//			throw new SessionException(e);
//		} 
//        
//    	GetProfile getProfile = new GetProfile();
//    	getProfile.setArgs0(sessionKey);
//    	
//    	String results = null;
//		try {
//			
//			results = getClient().getUserService().getProfile(getProfile).get_return();
//			
//		} catch (java.lang.Exception e) {
//			command.setOutput(results);
//			setStatus(Status.FAILED, command);
//			throw new SessionException("Failed to set session project", e);
//		} 
//    	
//    	GridChem.user = (UserBean)Settings.xstream.fromXML(results);
//        
//        setStatus(Status.COMPLETED,command);
//
//    }

    public static void setCurrentProject(ProjectBean p) throws SessionException {
    	SetSessionProject params = new SetSessionProject();
    	params.setArgs0(sessionKey);
    	params.setArgs1(p.getId().toString());

    	// Setter should not return a value, especially the UserBean associated
    	// with this session.  Call separately instead.
    	try {
			
    		getClient().getSessionService().setSessionProject(params);
    		
		} catch (java.lang.Exception e) {
			throw new SessionException("Failed to set session project", e);
		} 
    }

//    public static UserBean getProfile(LoginCommand command) throws UserException {
//    	GetProfile params = new GetProfile();
//    	params.setArgs0(sessionKey);
//    	
//    	String results = null;
//		try {
//			results = getClient().getUserService().getProfile(params).get_return();
//		} catch (java.lang.Exception e) {
//			command.setOutput(results);
//			setStatus(Status.FAILED,command);
//        	throw new UserException("Failed to retrieve user profile", e);
//		} 
//    	
//    	if (results == null) throw new org.gridchem.service.exceptions.UserException("Failed to retrieve user profile");
//    	
//    	UserBean user = (UserBean)Settings.xstream.fromXML(results);
//        
//        command.setOutput(user);
//        
//        setStatus(Status.COMPLETED,command);
//        
//        return user;
//        
//    }
    
    public static UserBean getProfile() throws UserException {
    	GetProfile params = new GetProfile();
    	params.setArgs0(sessionKey);
    	int count=0; //for retrying in case of failure
    	String results = null;
    	while (results == null && count < 10){ //added to retry ten times
		try {
			count++;
			results = getClient().getUserService().getProfile(params).get_return();
            System.out.println(" Count " + count + results + " \n ");
		} catch (java.lang.Exception e) {
        	throw new UserException("Failed to retrieve user profile", e);
		} 
    	}
		
    	if (results == null) throw new org.gridchem.service.exceptions.UserException("Failed to retrieve user profile");
    	
    	return (UserBean)Settings.xstream.fromXML(results);
    }
   
    /* ************************************************************************************* */
    /*                                                                                       */
    /*                          File Management Methods                                      */
    /*                                                                                       */
    /* ************************************************************************************* */

//    public static HashSet<LogicalFileBean> uploadInputFiles(JobCommand command)
//            {
//        HashSet<LogicalFileBean> linFiles = new HashSet<LogicalFileBean>();
//        
//        try {
//            JobBean job = (JobBean)command.getArguments().get("job");
//            HashSet<LogicalFileBean> inputFiles = job.getLogicalInputFiles();
//            
//            for (LogicalFileBean inFile: inputFiles) {
//                
//                File file = new File(inFile.getLocalPath());
//                if (!file.exists()) {
//                    throw new IOException("Could not locate file " + file.getName());
//                }
//                
//                wd.updateWarning("Uploading file " + file.getName());
//                
//                long size = file.length();
//                int blocksize = 1024;
//                int totalBlocks = (int)Math.ceil( size / blocksize);
//                
//                int blocksSent = 0;
//                pm.setMaximum(totalBlocks);
//                pm.setMinimum(0);
//                
//                Properties args = new Properties();
//                args.put("action", "upload");
//                args.put("key", GMSSession.getKey());
//                args.put("fileName", file.getName());
//                
//                String argString = "";  // default
//    
//                if (args != null) {
//                  argString = toEncodedString(args);
//                }
//                
//                URLConnection conn = getConnection("");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//                conn.setUseCaches(false);
//                conn.setRequestProperty("Content-Type",
//                    "application/x-www-form-urlencoded");
//                
//                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//                out.writeBytes(argString);
//                out.flush();
//                out.close();
//                
//    //            BufferedWriter out = 
//    //                new BufferedWriter( new OutputStreamWriter( conn.getOutputStream() ) );
//    //            out.write("action=download");
//    //            out.write("key=" + GMSSession.getKey());
//    //            out.write("uri=" + uri.toString());
//    //            out.flush();
//    //            out.close();
//                FileInputStream fis = new FileInputStream(file);
////                    new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
//                
//                
//                int response;
//                String result = "";
//                
////                FileOutputStream fos = new FileOutputStream(file);
//                BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( conn.getOutputStream() ) );
//                
//                byte[] ifbytes = new byte[1024];
//                
//                while ( (response = fis.read(ifbytes)) > 0 ) {
//                    bw.write(ifbytes.toString());
//                    pm.setProgress(blocksSent++);
//                }
//                bw.close();
//                
//                if(Settings.VERBOSE) System.out.println("Successfully wrote file " + 
//                        file.getName() + " to disk: " + file.length());
//            }
//            
//        } catch ( MalformedURLException ex ) {
//            throw ex;
//        }
//        catch ( IOException ex ) {
//            throw ex;
//        }
//        return linFiles;
//    }
    
    
    
    
    /**
     * Upload the passed file to the server and associate it with the given job. The 
     * job does not have to be an existing job, but it must have an experiment name
     * and a job name.
     * 
     * @param job
     * @param file
     * @return
     */
    public static LogicalFileBean putCachedFile(JobBean job, File file) throws FileManagementException {
    	
//    	throw new FileManagementException(new OperationNotSupportedException("Not yet implemented"));
    	
    	FileDataSource fileDataSource = new FileDataSource(file);
    	DataHandler handler = new DataHandler(fileDataSource);

    	PutCachedFile params = new PutCachedFile();
    	params.setArgs0(sessionKey);
    	params.setArgs1(job.getExperimentName());
    	params.setArgs2(job.getName());
    	params.setArgs3(file.getName());
    	params.setArgs4(handler);
    	
    	String result;
		try {
			
			result = getClient().getFileService().putCachedFile(params).get_return();
			
		} catch (Exception e) {
			throw new FileManagementException("Failed to upload file",e);
		}
    	
    	if (result == null) throw new FileManagementException("File upload failed");
    	
    	return (LogicalFileBean)Settings.xstream.fromXML(result);
    	
    }

    
	public static List<FileBean> listCachedInputFiles(String path) throws FileManagementException {
    	
    	List<FileBean> files = new ArrayList<FileBean>();
    	
    	ListCachedInputFiles params = new ListCachedInputFiles();
    	params.setArgs0(sessionKey);
    	
    	String results;
		try {
			
			results = getClient().getFileService().listCachedInputFiles(params).get_return();
		
		} catch (java.lang.Exception e) {
        	throw new FileManagementException("Failed to list cached input files", e);
		} 
    	
    	if (results != null) {
    		files = (List<FileBean>)Settings.xstream.fromXML(results);
    	}
    	
    	return files;
    }
    
    
	public static List<FileBean> listCachedInputFiles(String path, Long jobId) throws FileManagementException {
    	
    	List<FileBean> files = new ArrayList<FileBean>();
    	
    	ListCachedInputFilesForJob params = new ListCachedInputFilesForJob();
    	params.setArgs0(sessionKey);
    	params.setArgs1(jobId.toString());
    	
    	String results;
		try {
			
			results = getClient().getFileService().listCachedInputFilesForJob(params).get_return();
		
		} catch (java.lang.Exception e) {
			throw new FileManagementException("Failed to list cached input files for job", e);
		}
    	
    	if (results != null) {
    		files = (List<FileBean>)Settings.xstream.fromXML(results);
    	}
    	
    	return files;
    	
    }

    /**
     * Download a cached job file from the server. There is no guarantee that the file will be on the 
     * server due to purge policy, so it is recommended that the file existence be first checked
     * by calling listCachedInputFiles first.
     * 
     * @param path
     */
    public static void getCachedInputFile(String path) throws FileManagementException {
    	
//    	throw new FileManagementException(new OperationNotSupportedException("Not yet implemented"));
    	
    	GetCachedInputFile params = new GetCachedInputFile();
    	params.setArgs0(sessionKey);
    	params.setArgs1(path);
    	
    	DataHandler dataHandler;
		try {
			
			dataHandler = getClient().getFileService().getCachedInputFile(params).get_return();
			
		} catch (Exception e) {
			throw new FileManagementException("Failed to download cached input file",e);
		}
    	
    	// now write the content to disk
    	java.io.File file = new File(Env.getGridchemDataDir() + File.separator + path);
        
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

    	try {
    	
	    	FileOutputStream fos = new FileOutputStream(file);
			
	    	dataHandler.writeTo(fos);
			
		} catch (IOException e) {
			throw new FileManagementException("Failed writing remote file to disk",e);
		} 
    }
    
    /**
     * Retrieve a remote file on the remote host at the given path.  There is no guarantee that
     * the file will exist, so we recommend performing a list command first.
     * 
     * @param host
     * @param path
     */
    public static File getFile(String host, String path) throws FileManagementException {
    	
//    	throw new FileManagementException(new OperationNotSupportedException("Not yet implemented"));
    	
    	GetRemoteFile params = new GetRemoteFile();
    	params.setArgs0(sessionKey);
    	params.setArgs1(host);
    	params.setArgs2(path);
    	
    	DataHandler dataHandler;
		try {
			
			dataHandler = getClient().getFileService().getRemoteFile(params).get_return();
			
		} catch (Exception e) {
			throw new FileManagementException("Failed to download remote file",e);
		}
    	
    	// now write the content to disk
    	java.io.File file = new File(Env.getGridchemDataDir() + File.separator + path);
        
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        
        try {
        	
        	FileOutputStream fos = new FileOutputStream(file);
			
        	dataHandler.writeTo(fos);
			
		} catch (IOException e) {
			throw new FileManagementException("Failed writing remote file to disk",e);
		}
		
		return file;
    }
    
    public static void getFile1(String host, String path, JobCommand command) throws FileManagementException, InterruptedException {
    	
    	String savePath = (String) command.getArguments().get("localFile");
    	
    	System.out.println("*******************In getFile1 function: ");
    	
    	System.out.println(GridChem.user.getUserName());
    	System.out.println(GridChem.accessType.toString());
    	System.out.println(host);
    	if (GridChem.accessType.equals(AccessType.COMMUNITY)) {
    		System.out.println(path);
    	} else {
    		//path = path.replaceFirst(GridChem.externalUsername, "_USER_");
    		System.out.println(path);
    	}
    	System.out.println(savePath);
    	
    	
    	FileDownloadThread thread = new FileDownloadThread(
    			GridChem.user.getUserName(),
				GridChem.accessType.toString(),
				host,
				path,
				savePath);
		thread.start();

		synchronized (thread) {
			thread.wait();
		}
		
		command.setOutput(new File(savePath));
    	setStatus(Status.COMPLETED,command);

		return;
    }
    
    //*************************************function added nikhil
    
    public static void getFile(String host, String path, JobCommand command) throws FileManagementException {
    	GetRemoteFile params = new GetRemoteFile();
    	params.setArgs0(sessionKey);
    	params.setArgs1(host);
    	params.setArgs2(path);
    	int count = 0; //added -nik to retry if failed connection
        String dataFile = (String) command.getArguments().get("localFile");
    	DataHandler dataHandler = null;
    	while (dataHandler == null && count < 3){ // added nik
		try {
			
				count = count+1; 
				System.out.println(" This is the retry nik " + count);
				dataHandler = getClient().getFileService().getRemoteFile(params).get_return();
				Thread.sleep(8000);
			if (dataHandler == null && count >=3) {
				throw new FileNotFoundException("Remote file not found");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			if(count >=10)
			throw new FileManagementException("Failed to download remote file",e);
		}
    	}
    	
    	// now write the content to disk
//commented nik   	java.io.File file = new File(Env.getGridchemDataDir() + File.separator + path);
		java.io.File file = new File(dataFile);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        
        CalloutJobOutputStream fos = null;
        try {
        	
        	command.getArguments().put("totalBlocks", Integer.valueOf((int)Math.ceil(file.length()/4096)));
    		command.statusChanged(new StatusEvent(null,Status.DOWNLOADING));
    		
        	fos = new CalloutJobOutputStream(file, command);
			
        	dataHandler.writeTo(fos);
			
        	command.setOutput(file);
        	setStatus(Status.COMPLETED,command);
        	
		} catch (IOException e) {
			setStatus(Status.FAILED, command);
			throw new FileManagementException("Failed writing remote file to disk",e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {}
			}
		}
		
		
    }
    
    public static void getFile2(String host, String path, FileCommand command) throws FileManagementException, InterruptedException {
    	
    	String savePath = (String) command.getArguments().get("localFilePath");
    	
    	String [] pathList = path.split("/");
    	String fileName = pathList[pathList.length - 1];
    	
    	System.out.println("*******************In getFile2 function: ");
    	System.out.println(GridChem.user.getUserName());
    	System.out.println(GridChem.accessType.toString());
    	System.out.println(host);
    	System.out.println("/home/ccguser/mss/internal/" + GridChem.user.getUserName() + "/" + path);
    	System.out.println(savePath + File.separator + fileName);
    	
    	FileDownloadThread thread = new FileDownloadThread(
				GridChem.user.getUserName(),
				GridChem.accessType.toString(),
				host,
				"/home/ccguser/mss/internal/" + GridChem.user.getUserName() + "/" + path,
				savePath + File.separator + fileName);
		thread.start();

		synchronized (thread) {
			thread.wait();
		}
		
		command.setOutput(new File(savePath + File.separator + fileName));
    	setStatus(Status.COMPLETED,command);

		return;
    }
    
    //*************************************************************
    
    public static void getFile(String host, String path, FileCommand command) throws FileManagementException {
    	GetRemoteFile params = new GetRemoteFile();
    	params.setArgs0(sessionKey);
    	params.setArgs1(host);
    	params.setArgs2(path);
    	int count = 0; //added nik for retries
    	DataHandler dataHandler = null;
    	while (dataHandler == null && count < 3){ // added nik
		try {
			
				count = count+1; 
				System.out.println(" This is the retry nik " + count);
				dataHandler = getClient().getFileService().getRemoteFile(params).get_return();
				Thread.sleep(8000);
			if (dataHandler == null && count >= 3) {
				throw new FileNotFoundException("Remote file not found");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			if (count >=10)
			throw new FileManagementException("Failed to download remote file",e);
		}
    	}
    	// now write the content to disk
    	java.io.File file = new File(Env.getGridchemDataDir() + File.separator + path);
        
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        
        CalloutFileOutputStream fos = null;
        try {
        	
        	command.getArguments().put("totalBlocks", Long.valueOf((long)Math.ceil(file.length()/4096)));
    		command.statusChanged(new StatusEvent(null,Status.DOWNLOADING));
    		
        	fos = new CalloutFileOutputStream(file, command);
		
        	dataHandler.writeTo(fos);
             
        	command.setOutput(file);

        	setStatus(Status.COMPLETED,command);
        	
		} catch (IOException e) {
			setStatus(Status.FAILED, command);
			throw new FileManagementException("Failed writing remote file to disk",e);
		} finally {
			
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {}
			}
		}
		
		
    }
    
//    public static void getFile(URI uri, FileCommand command)
//            {
//        try {
//            
//            Properties args = new Properties();
//            args.put("action", "download");
//            args.put("key", GMSSession.getKey());
//            args.put("uri", uri.toString());
//            
//            String argString = "";  // default
//        
//            if (args != null) {
//              argString = "?" + toEncodedString(args);
//            }
//            
//            URLConnection conn = getConnection(argString);
//            
//        //    BufferedWriter out = 
//        //        new BufferedWriter( new OutputStreamWriter( conn.getOutputStream() ) );
//        //    out.write("action=download");
//        //    out.write("key=" + GMSSession.getKey());
//        //    out.write("uri=" + uri.toString());
//        //    out.flush();
//        //    out.close();
//            BufferedReader in = 
//                new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
//            
//            
//            String response;
//            String result = "";
//            
//            java.io.File file = new File(Env.getGridchemDataDir() + File.separator + 
//                    uri.toString().substring(uri.getPath().indexOf(GridChem.user.getUserName()),uri.getPath().length()));
//            
//            if (!file.getParentFile().exists()) {
//                file.getParentFile().mkdirs();
//            }
//            
//            FileOutputStream fos = new FileOutputStream(file);
//            
//            // now download the rest of the file in blocks, sending an update
//            // callback after every block.
//            double size = ((Long)command.getArguments().get("size")).doubleValue();
//            double blocksize = 1024;
//            double totalBlocks = Math.ceil( size / blocksize);
//            
//            long blocks = (long)totalBlocks;
//            
//            long blocksReceived = 0;
//            
//            command.getArguments().put("totalBlocks", new Long(blocks));
//            command.getArguments().put("blocksReceived", new Long(blocksReceived));
//            
//            System.out.println("There are " + totalBlocks + " to download.");
//            
//            setStatus(Status.DOWNLOADING,command);
//            
//            while ( (response = in.readLine()) != null ) {
////                System.out.println("Appended block " + blocksReceived + " to " + file);
//                blocksReceived++;
//        
//                command.getArguments().put("blocksReceived", new Long(blocksReceived));
//                
//                setStatus(Status.READY,command);
//                fos.write((response + "\n").getBytes() );
//            }
//            in.close();
//            
//            if(Settings.VERBOSE) System.out.println("Successfully wrote file " + 
//                    file.getName() + " to disk: " + file.length());
//            
//            
//            ((FileCommand)command).setOutput(file);
//        
//            setStatus(Status.COMPLETED,command);
//            
//        }
//        catch ( MalformedURLException ex ) {
//            throw ex;
//        }
//        catch ( IOException ex ) {
//            throw ex;
//        }
//    }
    
    /**
     * Delete the file at the given path from mass storage. There is no guarantee that
     * the file will exist, so we recommend performing a list command first.
     * 
     * @param path
     * @param isDirectory
     * @param command
     * @throws ExceptionException 
     * @throws RemoteException 
     * @throws AxisFault 
     */
    public static void delete(String path, boolean isDirectory, FileCommand command) throws FileManagementException {
    	try {
            if (isDirectory) {
            	DeleteDir params = new DeleteDir();
            	params.setArgs0(sessionKey);
            	params.setArgs1(path);
            	
            	getClient().getFileService().deleteDir(params);
            } else {
            	DeleteFile params = new DeleteFile();
            	params.setArgs0(sessionKey);
            	params.setArgs1(path);
            	
            	getClient().getFileService().deleteFile(params);
            }
            
            setStatus(Status.COMPLETED,command);

        } catch (java.lang.Exception e) {
			setStatus(Status.ERROR,command);
            throw new FileManagementException("Failed to delete folder/file", e);
		} 
    }
    

    
    /**
     * List the files at the remote path.
     * 
     * @param path relative path to the user's home space
     * @param command
     * @return java.util.List of FileBean objects representing the remote files.
     * @throws FileManagementException
     */
    public static void list(String path, FileCommand command) throws FileManagementException {
    	System.out.println("(&*(^*S&^*^&*&^*&^)(*)" + path);
    	List<FileBean> files = new ArrayList<FileBean>();
    	String results = null;
    	int count = 0; //added to count the retries
    	org.gridchem.service.stub.file.FileServiceStub.List params = new org.gridchem.service.stub.file.FileServiceStub.List();
    	params.setArgs0(sessionKey);
    	params.setArgs1(path);
        while(results == null && count < 3){
    	try {
    		Thread.sleep(1000);
            count = count +1; // added -nik
            System.out.println(" This is the retry nik " + count + " for: \t" + path);
			results = getClient().getFileService().list(params).get_return();
			//System.out.println(" This is the result nik " + results);
			files = (List<FileBean>)Settings.xstream.fromXML((String)results);

	        ((FileCommand)command).setOutput(files);

	        setStatus(Status.COMPLETED,command);
	        
    	} catch (java.lang.Exception e) {
    		
    		e.printStackTrace();
    		if (count >= 3){
    			command.setOutput(results);
    			setStatus(Status.ERROR,command);
    			throw new FileException("Failed to obtain file listing", e);
    		}
		} 
    }
    }

//    public static FileBean upDir(URI path, FileCommand command) {
//        return list(path,command);
//    }

    public static List<FileBean> list(String path) throws JobException {
    	List<FileBean> files = new ArrayList<FileBean>();
    	String results = null;
    	
    	org.gridchem.service.stub.file.FileServiceStub.List params = new org.gridchem.service.stub.file.FileServiceStub.List();
    	params.setArgs0(sessionKey);
    	params.setArgs1(path);
    	
    	try {
			
    		results = getClient().getFileService().list(params).get_return();
			
    		files = (List<FileBean>)Settings.xstream.fromXML((String)results);

			return files;
	        
    	} catch (java.lang.Exception e) {
        	throw new JobException("Failed to obtain file listing", e);
		} 
    	
    }

    public static void mkdir(String path, FileCommand command) throws FileManagementException {
    
    	Mkdir params = new Mkdir();
    	params.setArgs0(sessionKey);
    	params.setArgs1(path);
    	
    	// the result of a rename should not be a directory listing, so we
    	// removed this feature.  A list command will need to be called
    	// next to see the updated file/folder name.
    	try {
		
    		getClient().getFileService().mkdir(params);

        } catch (java.lang.Exception e) {
			setStatus(Status.ERROR,command);
            throw new JobException("Failed to create remote folder", e);
		} 
    	
        setStatus(Status.COMPLETED,command);
        
    }
    
    public static void rename(String path, FileCommand command) throws FileManagementException {
        
        Rename params = new Rename();
    	params.setArgs0(sessionKey);
    	params.setArgs1(path);
    	params.setArgs2((String)command.getArguments().get("name"));
    	
    	// the result of a rename should not be a directory listing, so we
    	// removed this feature.  A list command will need to be called
    	// next to see the updated file/folder name.
    	try {

    		getClient().getFileService().rename(params);

        } catch (java.lang.Exception e) {
			setStatus(Status.ERROR,command);
            throw new JobException("Failed to rename remote folder", e);
		} 
        
        setStatus(Status.COMPLETED,command);
            
        
    }


    /* ************************************************************************************* */
    /*                                                                                       */
    /*                              Job Submission Methods                                   */
    /*                                                                                       */
    /* ************************************************************************************* */

    public static List<JobBean> listJobs(JobCommand command) throws JobException{
        List<JobBean> jobs = new ArrayList<JobBean>();
        
        org.gridchem.service.stub.job.JobServiceStub.List params = new org.gridchem.service.stub.job.JobServiceStub.List();
        params.setArgs0(sessionKey);
        
        String results = null;
		try {
			
			results = getClient().getJobService().list(params).get_return();
		
		} catch (java.lang.Exception e) {
			e.printStackTrace();
        	command.setOutput(results);
			setStatus(Status.ERROR,command);
            throw new JobException("Failed to retrieve job history", e);
		} 
        
        if (results == null) throw new JobException("Failed to retrieve job history");
        
        try {
	        File f = new File("jobslist.xml");
	        f.createNewFile();
	        FileWriter fw = new FileWriter("jobslist.xml");
	        fw.write((String)results);
	        fw.close();
	      
        } catch (IOException e) {}
        
        jobs = (List<JobBean>)Settings.xstream.fromXML((String)results);
        
        command.setOutput(jobs);
              
        if(Settings.DEBUG) System.out.println("Search returned " + jobs.size() + " jobs:");
        //added -nik
        System.out.println("This is the first job "+jobs.get(0).getLocalId()
        		+jobs.get(0).getId());
        
        setStatus(Status.COMPLETED,command);
        
        return jobs;
    }
    
    public static List<JobBean> findJobs(JobCommand command) throws JobException {
    	
    	List<JobBean> jobs = new ArrayList<JobBean>();
        
    	Search params = new Search();
        params.setArgs0(sessionKey);
        params.setArgs1((String)Settings.xstream.toXML(command.getArguments().get("search")));
        
        String results = null;
		try {
			
			results = getClient().getJobService().search(params).get_return();
			

        } catch (java.lang.Exception e) {
        	command.setOutput(results);
			setStatus(Status.ERROR,command);
            throw new JobException("Failed to search for jobs", e);
		} 
        
        if (results != null) {
            
            jobs = (List<JobBean>)Settings.xstream.fromXML((String)results);

            command.setOutput(jobs);
            
            if(Settings.DEBUG)
                System.out.println("Search returned " + jobs.size() + " jobs:");
        }
        
        setStatus(Status.COMPLETED,command);
        
        return jobs;
            
    }

    /**
     * Delete a job from the user's history.  This makes a call to the
     * gms_ws telling it to mark the user's job deleted.  This will delete
     * the data in mass storage and mark the job as deleted in the db.
     * @param command
     */
    public static void deleteJob(JobCommand command)  throws JobException{
        
    	Delete params = new Delete();
        params.setArgs0(sessionKey);
        params.setArgs1((String)command.getArguments().get("jobIDs"));
        
        try {
			
			getClient().getJobService().delete(params);

        } catch (java.lang.Exception e) {
			setStatus(Status.ERROR,command);
            throw new JobException("Failed to delete job", e);
		} 
        
        setStatus(Status.COMPLETED,command);
        
    }

    /**
     * Hide a job from the user.  This makes a call to the
     * gms_ws telling it to mark the user's job hidden.  This will
     * cause the user's job to have it's "hidden" attribute set to
     * true, and thus, will not be displayed in the JobPanel of
     * MyCCG.
     */
    public static void hideJob(JobCommand command) throws JobException {
    	Hide params = new Hide();
        params.setArgs0(sessionKey);
        params.setArgs1((String)command.getArguments().get("jobIDs"));
        
        try {
			
			getClient().getJobService().hide(params);
			
		} catch (java.lang.Exception e) {
			setStatus(Status.ERROR,command);
            throw new JobException("Failed to hide job", e);
		} 
        
        setStatus(Status.COMPLETED,command);
    }

    /**
     * Unhide a specific job from the user.  This makes a call to the
     * gms_ws telling it to mark the user's job visible.  This will
     * cause the user's job to have it's "hidden" attribute set to
     * false, and thus, will not be returned in subsequent calls to
     * retrieve file listings.
     */
    public static void unhideJob(JobCommand command) throws JobException {
        
    	Unhide params = new Unhide();
        params.setArgs0(sessionKey);
        params.setArgs1((String)command.getArguments().get("jobIDs"));
        
        try {
        	
        	getClient().getJobService().unhide(params);
        	
        } catch (java.lang.Exception e) {
			setStatus(Status.ERROR,command);
            throw new JobException("Failed to unhide job", e);
		} 
        
        setStatus(Status.COMPLETED,command);
    }

    /**
     * Delete a job from the user's history.  This makes a call to the
     * gms_ws telling it to mark the user's job deleted.  This will delete
     * the data in mass storage and mark the job as deleted in the db.
     * @param command
     */
    public static void showHiddenJobs(JobCommand command) throws JobException {
        
        UnhideAll params = new UnhideAll();
        params.setArgs0(sessionKey);
        
        try {
        	
        	getClient().getJobService().unhideAll(params);
        
        } catch (java.lang.Exception e) {
			setStatus(Status.ERROR,command);
            throw new JobException("Failed to unhide all job", e);
		} 
        setStatus(Status.COMPLETED,command);
    }

    /**
     * This is not yet supported on the service side.  Currently the current time
     * is returned.
     * 
     * @param command
     */
    public static Date predictJobStartTime(JobCommand command) throws JobException {
        
        String results = null;
        
        try {
        	
        	JobBean job = (JobBean) command.getArguments().get("job");
        	results = JobPredictionService.predictQueuedStartTime(job);
        	
	    } catch (java.lang.Exception e) {
	    	command.setOutput(results);
			setStatus(Status.ERROR,command);
	        throw new JobException("Failed to predict start time", e);
		} 
        command.setOutput(results);
        
        setStatus(Status.COMPLETED,command);
        
        return (Date)Settings.xstream.fromXML(results);
        
    }
    
//    public static Long getDownloadSize(JobCommand command) 
//    throws IOException, MalformedURLException {
//        try {
//            Properties args = new Properties();
//            args.put("action", "getSize");
//            args.put("key", GMSSession.getKey());
//            args.put("jobId", ((Long)command.getArguments().get("jobId")).toString());
//            
//            String argString = "";  // default
//
//            if (args != null) {
//              argString = "?" + toEncodedString(args);
//            }
//            
//            URLConnection conn = getConnection(argString);
//            
////            BufferedWriter out = 
////                new BufferedWriter( new OutputStreamWriter( conn.getOutputStream() ) );
////            out.write(URLEncoder.encode(,"UTF-8"));
////            out.write(URLEncoder.encode(,"UTF-8"));
////            out.write(URLEncoder.encode("uri=" + uri,"UTF-8"));
////            out.flush();
////            out.close();
//            BufferedReader in = 
//                new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
//            
//            String response;
//            String result = "";
//            while ( (response = in.readLine()) != null ) {
//                result += response;
//            }
//            in.close();
//            
////            if(Settings.VERBOSE) System.out.println(result);
//            
//            
//            if (result != null) {
//                
//                if (result.indexOf("org.gridchem.service.gms.exceptions.") > -1) {
//                    throw new FileManagementException(result);
//                }
//                
//                return new Long((String)result);
//                
//            } else {
//                throw new GMSException("No files returned from GMS");
//            }
//        }
//        catch ( MalformedURLException ex ) {
//            throw ex;
//        }
//        catch ( IOException ex ) {
//            throw ex;
//        } 
//    }
    
//    public static void getJobInput(JobCommand command) throws JobException {
//        final ProgressDialog progressDialog = (ProgressDialog)command.getArguments().get("progressDialog");
//        
//        
//        JobBean job = (JobBean)command.getArguments().get("job");
//        
//        // progress bar will have one major task for the job and one subtask for
//        // each file download associated with the job.
//        progressDialog.beginTask("Downloading " + job.getInputFiles().size() + 
//                " input files for job " + job.getName(),
//                job.getInputFiles().size(), true);
//        
//        for (LogicalFileBean logicalFile: job.getInputFiles()) {
//            try {
//                // determine how many intervals the file download will take.
//                double blocksize = 1024;
//                double totalBlocks = Math.ceil( logicalFile.getLength() / blocksize);
//                int blocks = (int)totalBlocks;
//                long blocksReceived = 0;
//                
//                // get just the name of the file
//                String localFileName = logicalFile.getLocalPath();
//                localFileName = localFileName.substring(localFileName.lastIndexOf("/") + 1);
//                
//                // start the subtask for this file
//                progressDialog.beginSubTask("Downloading file " + localFileName,blocks);
//                
//                // begin the transfer
//                Properties args = new Properties();
//                args.put("action", "getJobInput");
//                args.put("key", GMSSession.getKey());
//                args.put("logicalFileId", logicalFile.getId().toString());
//                args.put("jobId", job.getId().toString());
//                
//                String argString = "";  // default
//            
//                if (args != null) {
//                  argString = "?" + toEncodedString(args);
//                }
//                
//                URLConnection conn = getConnection(argString);
//                BufferedReader in = 
//                    new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
//                
//                String response;
//                String result = "";
//                
//                
//                String localFilePath = (String)command.getArguments().get("localFile");
//                java.io.File localFile = new File(localFilePath);
//                
//                if (!localFile.getParentFile().exists()) {
//                    localFile.getParentFile().mkdirs();
//                }
//                
//                FileOutputStream fos = new FileOutputStream(localFile);
//                
//                // do a buffered download of the file
//                System.out.println("There are " + totalBlocks + " to download.");
//                
//                while ( (response = in.readLine()) != null ) {
//                    if (progressDialog.isCanceled()) {
//                        progressDialog.finished();
//                        command.getArguments().put("exception", new IOException("File download cancelled by the user."));
//                        setStatus(Status.ERROR,command);
//                    }
//                    progressDialog.subWorked(1);
//                    fos.write((response + "\n").getBytes() );
//                }
//                
//                in.close();
//                
//                // update the job's logical file info to reflect the new input file path
//                logicalFile.setUserPath(localFile.getAbsolutePath());
//                
//                // update the progress dialog
//                progressDialog.subFinished();
//                progressDialog.worked(1);
//                
//                if(Settings.VERBOSE) System.out.println("Successfully wrote file " + 
//                        localFile.getName() + " to disk: " + localFile.length());
//            
//            } catch (Exception e) {
//                command.getArguments().put("exception",((e.getCause() == null)?e:e.getCause()));
//                setStatus(Status.FAILED,command);
//                return;
//            }
//        }
//        progressDialog.finished();
//        setStatus(Status.COMPLETED,command);
//    }

//    /**
//     * Retrieves the output of a specified job.  The job may be running or stopped.
//     * Logic for this command is in the service.
//     * 
//     * @param command
//     */
//    public static void getJobOutput(String path, JobCommand command) {
//        try {
//            GetJobOutputFilesForJob params = new GetJobOutputFilesForJob();
//            params.setArg0(sessionKey);
//            params.setArg1(((Long)command.getArguments().get("jobId")).toString());
//            
//            DataHandler dataHandler = getClient().getFileService().g
//            
//            double size = GMS3.getDownloadSize(command).doubleValue();
//            double blocksize = 1024;
//            double totalBlocks = Math.ceil( size / blocksize);
//            long blocks = (long)totalBlocks;
//            long blocksReceived = 0;
//            
//            Properties args = new Properties();
//            args.put("action", "getJobOutput");
//            args.put("key", GMSSession.getKey());
//            args.put("jobId", ((Long)command.getArguments().get("jobId")).toString());
//            
//            String argString = "";  // default
//        
//            if (args != null) {
//              argString = "?" + toEncodedString(args);
//            }
//            
//            URLConnection conn = getConnection(argString);
//            
//        //    BufferedWriter out = 
//        //        new BufferedWriter( new OutputStreamWriter( conn.getOutputStream() ) );
//        //    out.write("action=download");
//        //    out.write("key=" + GMSSession.getKey());
//        //    out.write("uri=" + uri.toString());
//        //    out.flush();
//        //    out.close();
//            BufferedReader in = 
//                new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
//            
//            
//            String response;
//            String result = "";
//            
//            String fileLocation = (String)command.getArguments().get("localFile");
//            System.out.println("*****fileLocation: "+fileLocation);
//            java.io.File file = new File(fileLocation);
//            
//            if (!file.getParentFile().exists()) {
//                file.getParentFile().mkdirs();
//            }
//            
//            FileOutputStream fos = new FileOutputStream(file);
//            
//            // now download the rest of the file in blocks, sending an update
//            // callback after every block.
//            
//            command.getArguments().put("totalBlocks", new Long(blocks));
//            command.getArguments().put("blocksReceived", new Long(blocksReceived));
//            
//            System.out.println("There are " + totalBlocks + " to download.");
//            
//            setStatus(Status.DOWNLOADING,command);
//            
//            while ( (response = in.readLine()) != null ) {
////                System.out.println("Appended block " + blocksReceived + " to " + file);
//                blocksReceived++;
//        
//                command.getArguments().put("blocksReceived", new Long(blocksReceived));
//                
//                setStatus(Status.READY,command);
//                fos.write((response + "\n").getBytes() );
//            }
//            
//            in.close();
//            
//            if(Settings.VERBOSE) System.out.println("Successfully wrote file " + 
//                    file.getName() + " to disk: " + file.length());
//            
//            ((JobCommand)command).setOutput(file);
//        
//            setStatus(Status.COMPLETED,command);
//            
//        }
//        catch ( MalformedURLException ex ) {
//            throw ex;
//        }
//        catch ( IOException ex ) {
//            throw ex;
//        }
//        
//    }

    /**
     * Submit a single job to the GMS service.  The service will update the user's vo
     * with the new job info, so the retrieveUserVO method must be called after this
     * to ensure up-to-date info.
     * 
     * @param command
     * @throws Exception 
     */
    public static void submitJob(final JobCommand command) {
        
        // Start the progress bar. Major tasks are staging input files, making
        // connection to remote resource, staging job, submitting job, and cleaning
        // up.
        final ProgressDialog progressDialog = (ProgressDialog)command.getArguments().get("progressDialog");
       
//        try {
            final JobBean job = (JobBean)command.getArguments().get("job");
            //public JobBean job = (JobBean)command.getArguments().get("job");
            final int taskCount = (2*job.getInputFiles().size()) + 5;
            
            List<LogicalFileBean> linFiles = new ArrayList<LogicalFileBean>();
            final List<LogicalFileBean> old_inFiles = job.getInputFiles();
            
            progressDialog.beginTask("Uploading input files...",
                    taskCount, true);
            progressDialog.worked(1);
            long milis = new Date().getTime();
            for (LogicalFileBean inFile: job.getInputFiles()) {
            	try { 
            		String inFLocPath=inFile.getLocalPath();
            		//String InFLocPath=inFile.getRemotePath();
            		System.out.println("GMS3: Input file client local path is "+inFLocPath);
            		FileUploader uploader = new FileUploader(inFLocPath, job);
            		uploader.addProgressDialog(progressDialog);

            		LogicalFileBean lf = uploader.send1();
                    
            		if (lf == null) {
                        progressDialog.setTaskName("Job submission cancelled.");
                        progressDialog.finished();
                        command.getArguments().put("exception",
                                new IOException("Failed up upload input files to job submission server."));
                        setStatus(Status.FAILED,command);
                        return;
                    } else {
                        // preserve the local path of the input file
//                        lf.setLocalPath(inFile.getLocalPath());
                        linFiles.add(lf);
                        progressDialog.worked(2);
                    }
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                    progressDialog.setTaskName("Job submission failed during input file upload.");
                    progressDialog.finished();
                    
                    command.getArguments().put("exception",((e.getCause() == null)?e:e.getCause()));
                    setStatus(Status.FAILED,command);
                    return;
                }
            }
            
            job.setInputFiles(linFiles);
            
            progressDialog.setTaskName("Submitting " + job.getName());
            progressDialog.beginSubTask("Connecting to job submission server", 3);
            progressDialog.subWorked(1);
            
            progressDialog.setSubTaskName("Submitting job...");
            progressDialog.subWorked(1);
//                    progressDialog.updateSubStatus();
            
            final Flag flag = new Flag();
            
            Thread worker = new Thread() {
                private Object o = null;
                public void run() {
                    try {
                    	Submit params = new Submit();
                    	params.setArgs0(sessionKey);
                    	params.setArgs1(Settings.xstream.toXML(job));
                    	
                    	JobServiceStub jobService = getClient().getJobService();
                    	ServiceClient serviceClient = jobService._getServiceClient();
                    	Options options = serviceClient.getOptions();
                    	options.setProperty(HTTPConstants.SO_TIMEOUT, new Integer(300000));
                    	options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(300000));
                    	
                    	o = jobService.submit(params).get_return();
                    	//Thread.sleep(1000);
                    	//params.wait();
                    	job.setId((Long) o);
                        System.out.println("GMS3:(1497) Successfully submitted job " + o.toString());
                        progressDialog.setSubTaskName("Successfully submitted job " + o.toString());
                        //o.wait(1000);
                        //Thread.sleep(90000);// Timing issue for localjobID
                        System.out.println("Job Data: GMS jobid "+job.getId());
                        //job.wait();
                        System.out.println("GMS3:(1499) Local jobID: "+job.getLocalId());
                        progressDialog.worked(3);
                        progressDialog.finished();
                        setStatus(Status.COMPLETED,command);
                        flag.done = true;
                    } catch (java.lang.Exception e) {
                        flag.failed = true;
                        
                        e.printStackTrace();
                        command.getArguments().put("exception",((e.getCause() == null)?e:e.getCause()));
                        setStatus(Status.FAILED,command);
                        JOptionPane.showMessageDialog(null, "Failed to submit job", "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                    	/* Rescue the orignal input files paths in case job failed to be submitted */
                        job.setInputFiles(old_inFiles);
                        progressDialog.finished();
                        
                        notify();
                    }
                }
                
//                public void finished() {
//                    if (o != null) {
//                        System.out.println("Successfully submitted job " + (String)o);
//                        progressDialog.setSubTaskName("Successfully submitted job " + (String)o);
//                        progressDialog.worked(3);
//                        progressDialog.finished();
//                        setStatus(Status.COMPLETED,command);
//                    } else {
////                        command.getArguments().put("exception","org.gridchem.service.gms.exceptions.JobSubmissionException: Job was cancelled by the user.");
//                        setStatus(Status.FAILED,command);
//                    }
//                }
               
            };
            
            worker.start();
            
            try {
            	synchronized (worker) {
            		worker.wait();
            	}
				
				if (flag.failed) {
                    flag.done = true;
                    progressDialog.finished();
                    setStatus(Status.FAILED,command);
                    return;
                }
                if (progressDialog.isCanceled()) {
                    worker.interrupt();
                    flag.done = true;
                    command.getArguments().put("exception",new JobSubmissionException("Job was cancelled by the user."));
                    setStatus(Status.ERROR,command);
                    return;
                }
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            
            /*while (!flag.done) {
                if (flag.failed) {
                    flag.done = true;
                    progressDialog.finished();
                    setStatus(Status.FAILED,command);
                    return;
                }
                if (progressDialog.isCanceled()) {
                    worker.interrupt();
                    flag.done = true;
                    command.getArguments().put("exception",new JobSubmissionException("Job was cancelled by the user."));
                    setStatus(Status.ERROR,command);
                    return;
                }
            }*/
    }
       
    /**
     * Submit multiple jobs to the GMS service.  The service will update the user's vo
     * with the new job info, so the retrieveUserVO method must be called after this
     * to ensure up-to-date info.
     * 
     * @param command
     */
    public static void submitMultipleJobs(final JobCommand command) throws JobException {
        
        // Start the progress bar. Major tasks are staging input files, making
        // connection to remote resource, staging job, submitting job, and cleaning
        // up.
        final ProgressDialog progressDialog = (ProgressDialog)command.getArguments().get("progressDialog");
        
        
            final ArrayList<JobBean> jobs = (ArrayList<JobBean>)command.getArguments().get("jobs");
        
            // find number of input files for tracking progress
            int inputFileCount = 0;
            for (JobBean job: jobs) {
                inputFileCount += job.getInputFiles().size();
            }
            
            final int taskCount = (jobs.size()*((2*inputFileCount) + 5));
            final Flag flag = new Flag();
            Thread worker = null;
            
            for (JobBean job: jobs) {
                try {
                    List<LogicalFileBean> linFiles = new ArrayList<LogicalFileBean>();
                    
                    progressDialog.beginTask("Submitting job " + job.getName(),
                            taskCount, true);
                    progressDialog.worked(1);
                    long milis = new Date().getTime();
                    
                    // submit each job in turn using the single job submission interface
                    for (LogicalFileBean inFile: job.getInputFiles()) {
                        
                        FileUploader uploader = new FileUploader(inFile.getLocalPath(),job);
                        uploader.addProgressDialog(progressDialog);
                        try {
                            LogicalFileBean lf = uploader.send();
                            if (lf == null) {
                                progressDialog.setTaskName("Job submission cancelled.");
                                progressDialog.finished();
                                command.getArguments().put("exception",
                                    new IOException("Failed up upload input files to job submission server."));
                                setStatus(Status.FAILED,command);
                                return;
                            } else {
                                // preserve the local path of the input file
                                lf.setLocalPath(inFile.getLocalPath());
                                linFiles.add(lf);
                                progressDialog.worked(2);
                            }
                        } catch (java.lang.Exception e) {
                            e.printStackTrace();
                            progressDialog.setTaskName("Job submission failed.");
                            progressDialog.finished();
                            command.getArguments().put("exception",
                                    ((e.getCause() == null)?e:e.getCause()));
                            setStatus(Status.FAILED,command);
                            return;
                        }
                    }
                    
                    job.setInputFiles(linFiles);
                    
//                    progressDialog.setTaskName("Submitting " + job.getName());
                    progressDialog.beginSubTask("Connecting to job submission server", 3);
                    progressDialog.subWorked(1);
                    
                    progressDialog.setSubTaskName("Submitting job...");
                    progressDialog.subWorked(1);
        //                    progressDialog.updateSubStatus();
                    
                    
                    final JobBean bean = job;
                    worker = new Thread() {
                        private Object o = null;
                        public void run() {
                            try {
                            	Submit params = new Submit();
                            	params.setArgs0(sessionKey);
                            	params.setArgs1(Settings.xstream.toXML(bean));
                            	
                            	o = getClient().getJobService().submit(params).get_return();
                                
                            } catch (java.lang.Exception e) {
                                flag.failed = true;
                                e.printStackTrace();
                                command.getArguments().put("exception",((e.getCause() == null)?e:e.getCause()));
                            }
                            flag.done = true;
                        }
                          
                    };
                    
                    worker.start();
                    
                    Thread.sleep(10);
                    
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                    progressDialog.finished();
                    command.getArguments().put("exception",((e.getCause() == null)?e:e.getCause()));
                    setStatus(Status.FAILED,command);
                    return;
                }
            }
            
            while (!flag.done) {
                if (flag.failed) {
                    flag.done = true;
                    progressDialog.finished();
                    setStatus(Status.FAILED,command);
                    return;
                }
                if (progressDialog.isCanceled()) {
                	if (worker!=null)
                		worker.interrupt();
                    flag.done = true;
                    command.getArguments().put("exception",new JobSubmissionException("Job was cancelled by the user."));
                    setStatus(Status.ERROR,command);
                    return;
                }
            }
            
            System.out.println("Successfully submitted all jobs");
            progressDialog.setSubTaskName("Successfully submitted all jobs.");
            progressDialog.finished();
//            progressDialog.finished();
            setStatus(Status.COMPLETED,command);
    }

    /**
     * Stop the job from running by either removing it from teh queue or stopping it's process.
     * 
     * @param command
     */
    public static void killJob(final JobCommand command) {
        final Long jid = ((Long)command.getArguments().get("jobIDs"));
        
        System.out.println("Calling kill job");
        
        final ProgressDialog progressDialog = new ProgressDialog(SubmitJobsWindow.frame,
        "Job Submission Progress");
        progressDialog.millisToPopup = 0;
        progressDialog.millisToDecideToPopup = 0;
        progressDialog.beginTask("Killing jobs ", -1, true);
        
        final Flag flag = new Flag();
        
        Thread worker = new Thread() {
            public void run() {
                try {
                	Kill params = new Kill();
                	params.setArgs0(sessionKey);
                	params.setArgs1(jid.toString());
                	
                	getClient().getJobService().kill(params);
                    
                } catch (java.lang.Exception e) {
                    flag.failed = true;
                    e.printStackTrace();
                    command.getArguments().put("exception",((e.getCause() == null)?e:e.getCause()));
                } finally {
                	finished();
                }
                
                flag.done = true;
            }
            
            public void finished() {
                System.out.println("Successfully killed job " + jid);
                progressDialog.setSubTaskName("Successfully killed job " + jid);
                progressDialog.finished();
                setStatus(Status.COMPLETED,command);
            }
           
        };
        
        worker.start();
        
        while (!flag.done) {
            if (flag.failed) {
                progressDialog.finished();
                setStatus(Status.FAILED,command);
                return;
            }
            if (progressDialog.isCanceled()) {
                worker.interrupt();
                command.getArguments().put("exception",new JobSubmissionException("Kill task was cancelled by the user."));
                setStatus(Status.ERROR,command);
                return;
            }
        }
    }

    
    /* ************************************************************************************* */
    /*                                                                                       */
    /*                          Notification Management Methods                              */
    /*                                                                                       */
    /* ************************************************************************************* */
    
    public static void getNotifications(final JobCommand command) {
        final Long jid = ((JobBean)command.getArguments().get("job")).getId();
        final ProgressDialog progressDialog = ((ProgressDialog)command.getArguments().get("progressDialog"));
        
        System.out.println("Retrieving job notifications.");
        
        
        progressDialog.millisToPopup = 0;
        progressDialog.millisToDecideToPopup = 0;
        progressDialog.beginTask("Retrieving notifications for job " + jid, -1, true);
        
        final Flag flag = new Flag();
        
        Thread worker = new Thread() {
            private Object o = null;
            public void run() {
                try {
                	GetNotifications params = new GetNotifications();
                	params.setArgs0(sessionKey);
                	params.setArgs1(jid.toString());
                	
                	o = getClient().getNotificationService().getNotifications(params).get_return();
                    
                	if (o != null) {
                        command.setOutput((ArrayList<NotificationBean>)Settings.xstream.fromXML((String)o));
                    }
                    
                    System.out.println("Successfully retrived notifications for " + jid);
                    progressDialog.finished();
                    setStatus(Status.COMPLETED,command);
                    
                } catch (java.lang.Exception e) {
                    flag.failed = true;
                    e.printStackTrace();
                    command.getArguments().put("exception",((e.getCause() == null)?e:e.getCause()));
                    progressDialog.finished();
                    setStatus(Status.FAILED,command);
                }
                flag.done = true;
            }
           
        };
        
        worker.start();
        
        while (!flag.done) {
            if (flag.failed) {
                progressDialog.finished();
                setStatus(Status.FAILED,command);
                return;
            }
            if (progressDialog.isCanceled()) {
                worker.interrupt();
                command.getArguments().put("exception",new JobSubmissionException("Retrieving of notifications was cancelled by the user."));
                setStatus(Status.ERROR,command);
                return;
            }
        }
    }
    
    public static void addNotification(final JobCommand command) {
        final NotificationBean notification = ((NotificationBean)command.getArguments().get("notification"));
        final ProgressDialog progressDialog = ((ProgressDialog)command.getArguments().get("progressDialog"));
        
        System.out.println("Adding job notification " + ((NotificationBean)command.getArguments().get("notification")).toString());
        
        progressDialog.millisToPopup = 0;
        progressDialog.millisToDecideToPopup = 0;
        progressDialog.beginTask("Adding job notification ", -1, true);
        
        final Flag flag = new Flag();
        
        Thread worker = new Thread() {
            private String o = null;
            public void run() {
                try {
                	Register params = new Register();
                	params.setArgs0(sessionKey);
                	params.setArgs1(notification.getJobId().toString());
                	params.setArgs2(notification.getType().name());
                	
                	o = getClient().getNotificationService().register(params).get_return();
                    command.setOutput(new Long(o));
                    System.out.println("Successfully added job notification.");
                    progressDialog.finished();
                    setStatus(Status.COMPLETED,command);
                } catch (java.lang.Exception e) {
                    flag.failed = true;
                    e.printStackTrace();
                    command.getArguments().put("exception",((e.getCause() == null)?e:e.getCause()));
                    progressDialog.finished();
                    setStatus(Status.FAILED,command);
                }
                flag.done = true;
            }
        };
        
        worker.start();
        
        while (!flag.done) {
            if (flag.failed) {
                progressDialog.finished();
                setStatus(Status.FAILED,command);
                return;
            }
            if (progressDialog.isCanceled()) {
                worker.interrupt();
                command.getArguments().put("exception",new JobSubmissionException("Adding of notification was cancelled by the user."));
                setStatus(Status.ERROR,command);
                return;
            }
        }
    }
    
    public static void removeNotification(final JobCommand command) {
        final NotificationBean notification = ((NotificationBean)command.getArguments().get("notification"));
        final ProgressDialog progressDialog = ((ProgressDialog)command.getArguments().get("progressDialog"));
        
        System.out.println("Removing job notification " + notification.toString());
        
        progressDialog.millisToPopup = 0;
        progressDialog.millisToDecideToPopup = 0;
        progressDialog.beginTask("Removing job notification ", -1, true);
        
        final Flag flag = new Flag();
        
        Thread worker = new Thread() {
//            private Object o = null;
            public void run() {
                try {
                	Remove params = new Remove();
                	params.setArgs0(sessionKey);
                	params.setArgs1(notification.getJobId().toString());
                	params.setArgs2(notification.getType().name());
                	
                	getClient().getNotificationService().remove(params);
                    
                	System.out.println("Successfully removed job notification.");
                    progressDialog.finished();
                    setStatus(Status.COMPLETED,command);
                } catch (java.lang.Exception e) {
                    flag.failed = true;
                    e.printStackTrace();
                    command.getArguments().put("exception",((e.getCause() == null)?e:e.getCause()));
                    progressDialog.finished();
                    setStatus(Status.FAILED,command);
                }
                flag.done = true;
            }
           
        };
        
        worker.start();
        
        while (!flag.done) {
            if (flag.failed) {
                progressDialog.finished();
                setStatus(Status.FAILED,command);
                return;
            }
            if (progressDialog.isCanceled()) {
                worker.interrupt();
                command.getArguments().put("exception",new JobSubmissionException("Removing of notification was cancelled by the user."));
                setStatus(Status.ERROR,command);
                return;
            }
        }
    }
    
    public static void clearNotifications(final JobCommand command) {
        final Long jid = ((JobBean)command.getArguments().get("job")).getId();
        final ProgressDialog progressDialog = ((ProgressDialog)command.getArguments().get("progressDialog"));
        
        System.out.println("Retrieving job notifications.");
        
        
        progressDialog.millisToPopup = 0;
        progressDialog.millisToDecideToPopup = 0;
        progressDialog.beginTask("Clearing notifications for job " + jid, -1, true);
        
        final Flag flag = new Flag();
        
        Thread worker = new Thread() {
            public void run() {
                try {
                	RemoveForJob params = new RemoveForJob();
                	params.setArgs0(sessionKey);
                	params.setArgs1(jid.toString());
                	
                	getClient().getNotificationService().removeForJob(params);
                    
                	System.out.println("Successfully cleared notifications for " + jid);
                    progressDialog.finished();
                    setStatus(Status.COMPLETED,command);
                } catch (java.lang.Exception e) {
                    flag.failed = true;
                    e.printStackTrace();
                    command.getArguments().put("exception",((e.getCause() == null)?e:e.getCause()));
                    progressDialog.finished();
                    setStatus(Status.FAILED,command);
                }
                flag.done = true;
            }
           
           
        };
        
        worker.start();
        
        while (!flag.done) {
            if (flag.failed) {
                progressDialog.finished();
                setStatus(Status.FAILED,command);
                return;
            }
            if (progressDialog.isCanceled()) {
                worker.interrupt();
                command.getArguments().put("exception",new JobSubmissionException("Clearing of notifications was cancelled by the user."));
                setStatus(Status.ERROR,command);
                return;
            }
        }
    }

    public static void updateNotification(final JobCommand command) {
        final NotificationBean notification = ((NotificationBean)command.getArguments().get("notification"));
        final ProgressDialog progressDialog = ((ProgressDialog)command.getArguments().get("progressDialog"));
        
        System.out.println("Updating job notification " + ((NotificationBean)command.getArguments().get("notification")).toString());
       
        progressDialog.millisToPopup = 0;
        progressDialog.millisToDecideToPopup = 0;
        progressDialog.beginTask("Updating job notification ", -1, true);
        
       final Flag flag = new Flag();
        
        Thread worker = new Thread() {
            private String o = null;
            public void run() {
            	try {
                	Register params = new Register();
                	params.setArgs0(sessionKey);
                	params.setArgs1(notification.getJobId().toString());
                	params.setArgs2(notification.getType().name());
                	
                	o = getClient().getNotificationService().register(params).get_return();
                	
                    System.out.println("Successfully updated job notification.");
                    progressDialog.finished();
                    command.setOutput(new Long((String)o));
                    setStatus(Status.COMPLETED,command);
                } catch (java.lang.Exception e) {
                    flag.failed = true;
                    e.printStackTrace();
                    command.getArguments().put("exception",((e.getCause() == null)?e:e.getCause()));
                    progressDialog.finished();
                    setStatus(Status.FAILED,command);
                }
                flag.done = true;
            }
            
        };
        
        worker.start();
        
        while (!flag.done) {
            if (flag.failed) {
                progressDialog.finished();
                setStatus(Status.FAILED,command);
                return;
            }
            if (progressDialog.isCanceled()) {
                worker.interrupt();
                command.getArguments().put("exception",new JobSubmissionException("Updating of notification was cancelled by the user."));
                setStatus(Status.ERROR,command);
                return;
            }
        }
    }
    
    /* ************************************************************************************* */
    /*                                                                                       */
    /*                          Resource Management Methods                                */
    /*                                                                                       */
    /* ************************************************************************************* */
    
    public static ArrayList<ComputeBean> getHardware(JobCommand command) throws ResourceException {
        
    	GetComputeResources params = new GetComputeResources();
        params.setArgs0(sessionKey);
        
        String results = null;
		try {
		
			results = getClient().getResourceService().getComputeResources(params).get_return();
			
		} catch (java.lang.Exception e) {
			command.setOutput(results);
			setStatus(Status.ERROR,command);
            throw new ResourceException("Failed to retrieve hardware resources", e);
		} 
        
        ArrayList<ComputeBean> hws = (ArrayList<ComputeBean>)Settings.xstream.fromXML(results); 
         
        command.setOutput(hws);
        
        setStatus(Status.COMPLETED,command);
        
        return hws;
    }
    
    public static ArrayList<ComputeBean> getHardware(Long projectId) throws ResourceException {
        GetComputeResources params = new GetComputeResources();
        params.setArgs0(sessionKey);
        
        String results = null;
		try {
		
			results = getClient().getResourceService().getComputeResources(params).get_return();
        
	    } catch (java.lang.Exception e) {
			throw new ResourceException("Failed to retrieve hardware resources", e);
		} 
	    
        ArrayList<ComputeBean> hws = (ArrayList<ComputeBean>)Settings.xstream.fromXML(results); 
         
        return hws;
    }
    
    public static List<SoftwareBean> getSoftware() throws SoftwareException {
    	
    	GetAllSoftware params = new GetAllSoftware();
    	params.setArgs0(sessionKey);
    	
    	String results = null;
 		try {
 		
 			results = getClient().getSoftwareService().getAllSoftware(params).get_return();
    	
 		} catch (java.lang.Exception e) {
			throw new SoftwareException("Failed to retrieve software resources", e);
		} 
 		
        ArrayList<SoftwareBean> sws = (ArrayList<SoftwareBean>)Settings.xstream.fromXML(results); 
            
        return sws;
    }
    
    public static List<SoftwareBean> getSoftware(JobCommand command) throws SoftwareException {
        
    	List<SoftwareBean> software = null;
        try {
        
        	software = GMS3.getSoftware();
        	
        } catch (SoftwareException e) {
        	command.setOutput(e.getCause().getMessage());
        	setStatus(Status.FAILED, command);
        	throw e;
        }
        
        command.setOutput(software);
        setStatus(Status.COMPLETED,command);
        
        return software;
    }
    
    
    public static void setStatus(Status status, StatusListener statusListener) {
        statusListener.statusChanged(new StatusEvent(null,status));
    }

    public void setStatusListener(StatusListener statusListener) {
        this.statusListener = statusListener;
    }
    
    public StatusListener getStatusListener() {
        return this.statusListener;
    }
    
    public static URLConnection getConnection(String args) throws MalformedURLException, IOException {
        URL url = new URL(Invariants.fileGateway + args);
        URLConnection conn = url.openConnection();
//        conn.setDoOutput(true);
//        conn.setDoInput(true);
        conn.setUseCaches (false);
//        conn.setDefaultUseCaches (false);
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        return conn;
    }
    
    public static GMSClient getClient() {
    	if (client == null) {
    		// changed to point to ccg-mw2
    		//client = new GMSClient("http://ccg-mw2.ncsa.uiuc.edu:8080/axis2/services");
    		//client = new GMSClient("http://gridchem-mw.ncsa.illinois.edu:8080/axis2/services");
    		client = new GMSClient("http://gridchem.uits.iu.edu:8080/axis2/services");
    		//client = new GMSClient("http://localhost:8080/axis2/services");
    	}
    	
    	return client;
    }
    
// // Converts a Properties list to a URL-encoded query string
//    private static String toEncodedString(Properties args) throws UnsupportedEncodingException {
//      StringBuffer buf = new StringBuffer();
//      Enumeration names = args.propertyNames();
//      while (names.hasMoreElements()) {
//        String name = (String) names.nextElement();
//        String value = args.getProperty(name);
//        buf.append(URLEncoder.encode(name,"UTF-8") + "=" + URLEncoder.encode(value,"UTF-8"));
//        if (names.hasMoreElements()) buf.append("&");
//      }
//      return buf.toString();
//    }
//    
    public static List<CollaboratorBean> getCollaborators() throws ProjectException {
		GetCollaborators params = new GetCollaborators();
		params.setArgs0(sessionKey);

		String results = null;
		try {
			
			results = getClient().getProjectService().getCollaborators(params).get_return();
			
		} catch (java.lang.Exception e) {
			throw new ResourceException("Failed to retrieve project collaborators", e);
		} 
		
		return (List<CollaboratorBean>)Settings.xstream.fromXML(results);
	}
	
	public static List<CollaboratorBean> getCollaborators(Long projectId) throws ProjectException {

		GetProjectCollaborators params = new GetProjectCollaborators();
		params.setArgs0(sessionKey);
		params.setArgs1(projectId.toString());

		String results = null;
		try {
			
			results = getClient().getProjectService().getProjectCollaborators(params).get_return();
			
		} catch (java.lang.Exception e) {
			throw new ResourceException("Failed to retrieve project collaborators", e);
		} 
		
		return (List<CollaboratorBean>)Settings.xstream.fromXML(results);
	}

//	public static UsageBean getProjectUsage(Long projectId, Long userId) {
//		// TODO Service needs to return ProjectBean for the projec of the given user 
//		getClient().getProjectService().get
//		
//		return null;
//	}

	public static void renewSession() throws SessionException {
		RenewSession renewSession = new RenewSession();
		renewSession.setArgs0(sessionKey);
		
		try {
			long newTime = getClient().getSessionService().renewSession(renewSession).get_return();
			
			Date date = new Date(newTime);
			System.out.println("Session renewed. Session will now expire at " + date);
		} catch (java.lang.Exception e) {
			throw new SessionException("Failed to renew the user session.", e);
		}
	}
	
	public static void main(String [] args) {
		//System.out.println(SHA1.encrypt("s@m!nda12"));
		GMS3.sessionKey =  "136370871938400.705";
		GMS3.getProfile();
	}
    
} 

class Flag {
    public boolean done = false;
    public boolean failed = false;
    public Flag(){}
};
