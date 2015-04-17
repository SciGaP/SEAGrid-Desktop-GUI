package org.apache.airavata.gridchem;

import org.apache.airavata.api.client.AiravataClientFactory;
import org.apache.airavata.model.error.AiravataClientConnectException;
import org.apache.airavata.model.util.ProjectModelUtil;
import org.apache.airavata.model.workspace.Project;
import org.apache.axis2.AxisFault;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.gridchem.client.common.Status;
import org.gridchem.client.gui.filebrowser.commands.FileCommand;
import org.gridchem.client.gui.jobsubmission.commands.JobCommand;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.service.beans.*;
import org.gridchem.service.exceptions.*;
import org.gridchem.service.model.enumeration.AccessType;
import org.gridchem.service.stub.file.ExceptionException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dimuthuupeksha on 4/17/15.
 */
public class AiravataManager {

    private static final String THRIFT_SERVER_HOST = "127.0.0.1";
    private static final int THRIFT_SERVER_PORT = 8930;

    public static boolean login(String uname, String passwd, AccessType type, HashMap<String,String> authMap) {
        return false;
    }

    public static void logout() {

    }

    public static List<ProjectBean> getProjects() throws ProjectException {
        return null;
    }

    public static ProjectBean getCurrentProject() throws ProjectException {
        return null;
    }

    public static List<ProjectBean> getProjects(JobCommand command) throws ProjectException {
        return null;
    }

    public static void setCurrentProject(ProjectBean p) throws SessionException {

    }


    public static UserBean getProfile() throws UserException {
        return null;
    }

    /* ************************************************************************************* */
    /*                                                                                       */
    /*                          File Management Methods                                      */
    /*                                                                                       */
    /* ************************************************************************************* */



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
        return null;
    }


    public static List<FileBean> listCachedInputFiles(String path) throws FileManagementException {
        return null;
    }


    public static List<FileBean> listCachedInputFiles(String path, Long jobId) throws FileManagementException {
        return null;
    }

    /**
     * Download a cached job file from the server. There is no guarantee that the file will be on the
     * server due to purge policy, so it is recommended that the file existence be first checked
     * by calling listCachedInputFiles first.
     *
     * @param path
     */
    public static void getCachedInputFile(String path) throws FileManagementException {

    }

    /**
     * Retrieve a remote file on the remote host at the given path.  There is no guarantee that
     * the file will exist, so we recommend performing a list command first.
     *
     * @param host
     * @param path
     */
    public static File getFile(String host, String path) throws FileManagementException {
        return null;
    }

    public static void getFile1(String host, String path, JobCommand command) throws FileManagementException, InterruptedException {

    }

    //*************************************function added nikhil

    public static void getFile(String host, String path, JobCommand command) throws FileManagementException {

    }

    public static void getFile2(String host, String path, FileCommand command) throws FileManagementException, InterruptedException {

    }

    //*************************************************************

    public static void getFile(String host, String path, FileCommand command) throws FileManagementException {


    }

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

    }

    public static List<FileBean> list(String path) throws JobException {
        return null;
    }

    public static void mkdir(String path, FileCommand command) throws FileManagementException {

    }

    public static void rename(String path, FileCommand command) throws FileManagementException {

    }


    /* ************************************************************************************* */
    /*                                                                                       */
    /*                              Job Submission Methods                                   */
    /*                                                                                       */
    /* ************************************************************************************* */

    public static List<JobBean> listJobs(JobCommand command) throws JobException{
        return null;
    }

    public static List<JobBean> findJobs(JobCommand command) throws JobException {
        return null;
    }

    /**
     * Delete a job from the user's history.  This makes a call to the
     * gms_ws telling it to mark the user's job deleted.  This will delete
     * the data in mass storage and mark the job as deleted in the db.
     * @param command
     */
    public static void deleteJob(JobCommand command)  throws JobException{

    }

    /**
     * Hide a job from the user.  This makes a call to the
     * gms_ws telling it to mark the user's job hidden.  This will
     * cause the user's job to have it's "hidden" attribute set to
     * true, and thus, will not be displayed in the JobPanel of
     * MyCCG.
     */
    public static void hideJob(JobCommand command) throws JobException {

    }

    /**
     * Unhide a specific job from the user.  This makes a call to the
     * gms_ws telling it to mark the user's job visible.  This will
     * cause the user's job to have it's "hidden" attribute set to
     * false, and thus, will not be returned in subsequent calls to
     * retrieve file listings.
     */
    public static void unhideJob(JobCommand command) throws JobException {

    }

    /**
     * Delete a job from the user's history.  This makes a call to the
     * gms_ws telling it to mark the user's job deleted.  This will delete
     * the data in mass storage and mark the job as deleted in the db.
     * @param command
     */
    public static void showHiddenJobs(JobCommand command) throws JobException {

    }

    /**
     * This is not yet supported on the service side.  Currently the current time
     * is returned.
     *
     * @param command
     */
    public static Date predictJobStartTime(JobCommand command) throws JobException {
        return null;
    }

    /**
     * Submit a single job to the GMS service.  The service will update the user's vo
     * with the new job info, so the retrieveUserVO method must be called after this
     * to ensure up-to-date info.
     *
     * @param command
     * @throws Exception
     */
    public static void submitJob(final JobCommand command) {

    }

    /**
     * Submit multiple jobs to the GMS service.  The service will update the user's vo
     * with the new job info, so the retrieveUserVO method must be called after this
     * to ensure up-to-date info.
     *
     * @param command
     */
    public static void submitMultipleJobs(final JobCommand command) throws JobException {

    }

    /**
     * Stop the job from running by either removing it from teh queue or stopping it's process.
     *
     * @param command
     */
    public static void killJob(final JobCommand command) {

    }


    /* ************************************************************************************* */
    /*                                                                                       */
    /*                          Notification Management Methods                              */
    /*                                                                                       */
    /* ************************************************************************************* */

    public static void getNotifications(final JobCommand command) {

    }

    public static void addNotification(final JobCommand command) {

    }

    public static void removeNotification(final JobCommand command) {

    }

    public static void clearNotifications(final JobCommand command) {

    }

    public static void updateNotification(final JobCommand command) {

    }

    /* ************************************************************************************* */
    /*                                                                                       */
    /*                          Resource Management Methods                                */
    /*                                                                                       */
    /* ************************************************************************************* */

    public static ArrayList<ComputeBean> getHardware(JobCommand command) throws ResourceException {
        return null;
    }

    public static ArrayList<ComputeBean> getHardware(Long projectId) throws ResourceException {
        return null;
    }

    public static List<SoftwareBean> getSoftware() throws SoftwareException {
        return null;
    }

    public static List<SoftwareBean> getSoftware(JobCommand command) throws SoftwareException {
        return null;
    }


    public static void setStatus(Status status, StatusListener statusListener) {

    }

    public void setStatusListener(StatusListener statusListener) {

    }

    public StatusListener getStatusListener() {
        return null;
    }

    public static URLConnection getConnection(String args) throws MalformedURLException, IOException {
        return null;
    }

    public static AiravataClient getClient() throws AiravataClientConnectException {
        try {
            TTransport transport = new TSocket(THRIFT_SERVER_HOST, THRIFT_SERVER_PORT);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            return new AiravataClient(protocol);
        } catch (TTransportException e) {
            throw new AiravataClientConnectException("Unable to connect to the server at "+THRIFT_SERVER_HOST+":"+THRIFT_SERVER_PORT);
        }
    }

    public static List<CollaboratorBean> getCollaborators() throws ProjectException {
        return null;
    }

    public static List<CollaboratorBean> getCollaborators(Long projectId) throws ProjectException {
        return null;
    }


    public static void renewSession() throws SessionException {

    }

    public static void main(String [] args) {
        try {
            AiravataClient client = getClient();
            Project project = ProjectModelUtil.createProject("project3", "dimuthu", "test project");
            String id = client.createProject(project);
            System.out.println(id);
            List<Project> projects = client.getAllUserProjects("dimuthu");
            System.out.println(projects.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
