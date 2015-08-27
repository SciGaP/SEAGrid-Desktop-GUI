package org.apache.airavata.gridchem;

import org.apache.airavata.AiravataConfig;
import org.apache.airavata.model.appcatalog.appdeployment.ApplicationDeploymentDescription;
import org.apache.airavata.model.appcatalog.appdeployment.ApplicationModule;
import org.apache.airavata.model.appcatalog.appinterface.ApplicationInterfaceDescription;
import org.apache.airavata.model.appcatalog.appinterface.DataType;
import org.apache.airavata.model.appcatalog.appinterface.InputDataObjectType;
import org.apache.airavata.model.appcatalog.appinterface.OutputDataObjectType;
import org.apache.airavata.model.appcatalog.computeresource.ComputeResourceDescription;
import org.apache.airavata.model.error.AiravataClientConnectException;
import org.apache.airavata.model.util.ExperimentModelUtil;
import org.apache.airavata.model.workspace.Project;
import org.apache.airavata.model.workspace.experiment.ComputationalResourceScheduling;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.airavata.model.workspace.experiment.ExperimentState;
import org.apache.airavata.model.workspace.experiment.UserConfigurationData;
import org.apache.axis2.AxisFault;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.gridchem.client.common.Settings;
import org.gridchem.client.common.Status;
import org.gridchem.client.gui.filebrowser.commands.FileCommand;
import org.gridchem.client.gui.jobsubmission.commands.JobCommand;
import org.gridchem.client.interfaces.StatusListener;
import org.gridchem.service.beans.*;
import org.gridchem.service.exceptions.*;
import org.gridchem.service.stub.file.ExceptionException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.*;

public class AiravataManager {

    public static String accessToken="";
    private static Project currentProject;

    public static boolean login(String uname, String passwd) {
        try {
            accessToken = getClient().login(uname, passwd);
        }catch (Exception e){
            throw new SessionException(e.getMessage());
        }
        Settings.gridchemusername = uname;
        Settings.authenticated = true;

        //Preferences.updatePrefs();

        System.out.println("Successfully authenticated.  Access Token is " + accessToken);
        return true;
    }

    public static void logout() {

    }

    public static List<Project> getProjects() throws ProjectException {

        List<Project> airavataProjects;
        try {
            airavataProjects = getClient().getAllUserProjects(AiravataConfig.getProperty(AiravataConfig.GATEWAY),Settings.gridchemusername);
        }catch (Exception e) {
            //User does not exists in the system - creating default project
            try{
                getClient().createProject(AiravataConfig.GATEWAY, new Project("no-id",Settings.gridchemusername,"Default Project"));
                airavataProjects = getClient().getAllUserProjects(AiravataConfig.getProperty(AiravataConfig.GATEWAY),Settings.gridchemusername);
            }catch (Exception ex){
                throw new ProjectException(e.getMessage());
            }
        }
        return airavataProjects;
    }

    public static Project getProject(String projectId){
        try {
            return getClient().getProject(projectId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Project getCurrentProject() throws ProjectException {
        return currentProject;
    }

    public static List<ProjectBean> getProjects(JobCommand command) throws ProjectException {
        return null;
    }

    public static void setCurrentProject(Project p) throws SessionException {
        currentProject =p;
    }


    public static UserBean getProfile() throws UserException {
        String query;
        try {
            query = getClient().getProfile(accessToken);
        }catch(Exception e){
            throw new UserException(e.getMessage());
        }

        JSONObject json = new JSONObject(query);

        UserBean userBean = new UserBean();

        userBean.setUserName(Settings.gridchemusername);

        if(json.has("preferred_username"))
            userBean.setUserName(json.getString("preferred_username"));

        if(json.has("name"))
            userBean.setFirstName(json.getString("name"));

        if(json.has("family_name"))
            userBean.setLastName(json.getString("family_name"));

        if(json.has("email"))
            userBean.setEmail(json.getString("email"));

        if(json.has("phone_number"))
            userBean.setPhone(json.getString("phone_number"));

        return userBean;
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

    public static List<ApplicationDeploymentDescription> getAplicationDeployments() throws AiravataClientConnectException, TException {
        return getClient().getAllApplicationDeployments(AiravataConfig.getProperty(AiravataConfig.GATEWAY));
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
        String host = AiravataConfig.getProperty("thrift_host");
        int port = Integer.parseInt(AiravataConfig.getProperty("thrift_port"));
        try {

            TTransport transport = new TSocket(host, port);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            return new AiravataClient(protocol);
        } catch (TTransportException e) {
            throw new AiravataClientConnectException("Unable to connect to the server at "+host+":"+port);
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

    public static ArrayList<ComputeResourceDescription> getComputationalResources() throws AiravataClientConnectException, TException {
        Map <String,String> compResources = getClient().getAllComputeResourceNames();
        Iterator<String> it = compResources.keySet().iterator();

        ArrayList<ComputeResourceDescription> compList = new ArrayList<ComputeResourceDescription>();
        while (it.hasNext()){
            compList.add(getClient().getComputeResource(it.next()));
        }

        return compList;
    }

    public static  List<ApplicationDeploymentDescription> getAppDepDescriptionforMachine(String computeHostId) throws AiravataClientConnectException, TException {
        List<ApplicationDeploymentDescription> appDeployments = getClient().getAllApplicationDeployments(AiravataConfig.getProperty(AiravataConfig.GATEWAY));
        List<ApplicationDeploymentDescription> returnApps = new ArrayList<ApplicationDeploymentDescription>();
        for (ApplicationDeploymentDescription ad : appDeployments){
            if(ad.getComputeHostId().equals(computeHostId)){
                returnApps.add(ad);
            }
        }
        return returnApps;
    }

    public static List<ComputeResourceDescription> getCompResourcesForAppModule(String appModuleName) throws AiravataClientConnectException, TException {
        List<ComputeResourceDescription> computeResources = new ArrayList<>();
        List<ApplicationDeploymentDescription> appDeployments =  getClient().getAllApplicationDeployments(AiravataConfig.getProperty(AiravataConfig.GATEWAY));
        Set<String> comResourceids= new HashSet<>();
        for(ApplicationDeploymentDescription app:appDeployments){
            if(getClient().getApplicationModule(app.getAppModuleId()).getAppModuleName().equals(appModuleName)){
                comResourceids.add(app.getComputeHostId());
            }
        }

        Iterator<String> it = comResourceids.iterator();
        while(it.hasNext()){
            computeResources.add(getClient().getComputeResource(it.next()));
        }

        return computeResources;
    }

    public static List<ComputeResourceDescription> getCompResourcesForAppId(String appId) throws AiravataClientConnectException, TException {
        List<ComputeResourceDescription> computeResources = new ArrayList<>();
        ApplicationDeploymentDescription app =  getClient().getApplicationDeployment(appId);
        computeResources.add(getClient().getComputeResource(app.getComputeHostId()));
        return computeResources;
    }

    public static ComputeResourceDescription getComputeResourceDescriptionFromName(String hostName) throws AiravataClientConnectException, TException {

        Map<String,String> compMap= getClient().getAllComputeResourceNames();
        Iterator<String> it = compMap.keySet().iterator();
        while(it.hasNext()){
            String id =it.next();
            ComputeResourceDescription desc = getClient().getComputeResource(id);
            if(desc.getHostName().equals(hostName)){
                return desc;
            }
        }
        return null;

    }

    public static ComputeResourceDescription getComputeResourceDescriptionFromId(String id) throws AiravataClientConnectException, TException {
        ComputeResourceDescription desc = getClient().getComputeResource(id);
        return desc;

    }

    public static List<ComputeResourceDescription> getComputationalResources(String appInterfaceID){
        List<ComputeResourceDescription> compResourceList = new ArrayList<>();
        try {
            Map<String, String> comps = getClient().getAvailableAppInterfaceComputeResources(appInterfaceID);
            Iterator<String> it = comps.keySet().iterator();
            while(it.hasNext()){
                String comID = it.next();
                compResourceList.add(getComputeResourceDescriptionFromId(comID));
            }
            return compResourceList;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static ApplicationModule getApplicationModule(String id) throws AiravataClientConnectException, TException {
        return getClient().getApplicationModule(id);
    }

    public static ApplicationDeploymentDescription getApplicationDeploymentDescription(String id) throws AiravataClientConnectException, TException {
        return getClient().getApplicationDeployment(id);
    }

    public static List<ApplicationInterfaceDescription> getAllAppInterfaces(){
        try{
            return getClient().getAllApplicationInterfaces(AiravataConfig.getProperty(AiravataConfig.GATEWAY));
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static List<Experiment> getQueuedExperiments(String userName){
        List<Experiment> exp = new ArrayList<>();
        try{
            List<Experiment> allexp = getClient().getAllUserExperiments(AiravataConfig.getProperty(AiravataConfig.GATEWAY),userName);
            for(Experiment experiment:allexp){
                if(experiment.getExperimentStatus().getExperimentState()==ExperimentState.CREATED)
                    exp.add(experiment);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return exp;
    }

    public static List<Experiment> getLaunchedExperiments(String userName){
        List<Experiment> exp = new ArrayList<>();
        try{
            List<Experiment> allexp = getClient().getAllUserExperiments(AiravataConfig.getProperty(AiravataConfig.GATEWAY),userName);
            for(Experiment experiment:allexp){
                ExperimentState state = experiment.getExperimentStatus().getExperimentState();
                if(state!=ExperimentState.CREATED)
                    exp.add(experiment);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return exp;
    }

    public static void createExperiment() throws Exception{
        String appId = "Echo_e82aa96b-66ea-4f31-97e7-1182a32e55d2";

        List<InputDataObjectType> exInputs = new ArrayList<InputDataObjectType>();
        InputDataObjectType input = new InputDataObjectType();
        input.setName("Input_to_Echo");
        input.setType(DataType.STRING);
        input.setValue("Echoed_Output=Hello World");
        exInputs.add(input);

        List<OutputDataObjectType> exOut = new ArrayList<OutputDataObjectType>();
        OutputDataObjectType output = new OutputDataObjectType();
        output.setName("Echoed_Output");
        output.setType(DataType.STRING);
        output.setValue("");
        exOut.add(output);

        Experiment simpleExperiment = ExperimentModelUtil.createSimpleExperiment("default", "admin", "echoExperiment", "Echo Exp", appId, exInputs);
        simpleExperiment.setExperimentOutputs(exOut);

        Map<String,String> computeResources = getClient().getAvailableAppInterfaceComputeResources(appId);
        String id = computeResources.keySet().iterator().next();
        String resourceName = computeResources.get(id);
        //System.out.println();
        System.out.println(id);
        System.out.println(resourceName);
        ComputationalResourceScheduling scheduling = ExperimentModelUtil.createComputationResourceScheduling(id, 1, 1, 1, "normal", 30, 0, 1, "sds128");
        UserConfigurationData userConfigurationData = new UserConfigurationData();
        userConfigurationData.setAiravataAutoSchedule(false);
        userConfigurationData.setOverrideManualScheduledParams(false);
        userConfigurationData.setComputationalResourceScheduling(scheduling);
        simpleExperiment.setUserConfigurationData(userConfigurationData);

        String exp = getClient().createExperiment(AiravataConfig.getProperty(AiravataConfig.GATEWAY),simpleExperiment);
        ExperimentState s;
        System.out.println(getClient().getExperimentStatus(exp).getExperimentState().name());
        getClient().launchExperiment(exp, "sample");

        Thread.sleep(10000);
        System.out.println(getClient().getExperimentStatus(exp).getExperimentState().name());
    }



    public static void main(String [] args) {
        try {
            ApplicationInterfaceDescription a= getClient().getApplicationInterface("Echo_af847f04-ca31-48da-a92b-3225602db4e0");
            a.getApplicationOutputs();
            //getClient().getComputeResource().
            //ApplicationInterfaceDescription a= getClient().getApplicationInterface("Echo_2e539083-665d-40fd-aaa2-4a751028326b");
            //a.getApplicationInterfaceId();
            //Map<String,String> ifn = getClient().getAllApplicationInterfaceNames();
            //List<ApplicationInterfaceDescription> apd = getClient().getAllApplicationInterfaces();
            //ApplicationInterfaceDescription a = apd.get(0);
            //a.getApplication

            //Iterator<String> it = ifn.keySet().iterator();
            //while(it.hasNext()){
           ///     String id = it.next();
           //     System.out.println(id+ " "+ifn.get(id));
           // }
            //createExperiment();
            //GMS3.login("dimuthu","changeme@1",AccessType.COMMUNITY,new HashMap<String, String>());
            //FileBrowserAiravata fileBrowser = new FileBrowserAiravata();
            //fileBrowser.makeDirectory("/tmp/dimuthu2");
            //GMS3.getClient().getFileService().mkdir();
            //uploadFileToGridChem("dimuthu","exp1","job1","testinput.txt","/Users/dimuthuupeksha/testinput.txt");
            //Project project = ProjectModelUtil.createProject("project1", "dimuthu", "sample1");
            //getClient().createProject("pgascigap",project);
            //FileManager.uploadFile("/Users/dimuthuupeksha/Downloads/c4b4nhtwbs3.inp","/tmp/dimuthu/c4b4nhtwbs3.inp");
            //List<ApplicationDeploymentDescription> appDeployments =  getClient().getAllApplicationDeployments();
            //for(ApplicationDeploymentDescription app:appDeployments){
              //  System.out.println(app.getAppDeploymentDescription());

            //}
            //createExperiment();
            //getClient().getApplicationDeployment("Echo_dcd59b1a-b291-4750-8d89-87531e0739e6")
            //File file = new File("http://climatedataapi.worldbank.org/climateweb/rest/v1/country/mavg/pr/2020/2039/USA.csv");
            //System.out.println(file.exists());
        }catch(Exception e){
            e.printStackTrace();
        }
        //login("admin", "admin");
        //System.out.println(getProfile().getFirstName());
        //List<Project> projects = getProjects();
        //System.out.println(projects.size());

    }
}
