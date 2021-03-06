package org.apache.airavata.gridchem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.airavata.AiravataConfig;
import org.apache.airavata.model.appcatalog.appdeployment.ApplicationDeploymentDescription;
import org.apache.airavata.model.appcatalog.appdeployment.ApplicationModule;
import org.apache.airavata.model.appcatalog.appinterface.ApplicationInterfaceDescription;
import org.apache.airavata.model.appcatalog.computeresource.ComputeResourceDescription;
import org.apache.airavata.model.application.io.InputDataObjectType;
import org.apache.airavata.model.application.io.OutputDataObjectType;
import org.apache.airavata.model.error.AiravataClientException;
import org.apache.airavata.model.error.AiravataErrorType;
import org.apache.airavata.model.experiment.ExperimentModel;
import org.apache.airavata.model.experiment.ExperimentSearchFields;
import org.apache.airavata.model.experiment.ExperimentSummaryModel;
import org.apache.airavata.model.job.JobModel;
import org.apache.airavata.model.security.AuthzToken;
import org.apache.airavata.model.status.ExperimentState;
import org.apache.airavata.model.workspace.Project;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.gridchem.client.GridChem;
import org.gridchem.client.common.Settings;
import org.gridchem.service.beans.UserBean;
import org.gridchem.service.exceptions.ProjectException;
import org.gridchem.service.exceptions.SessionException;
import org.gridchem.service.exceptions.UserException;
import org.json.JSONObject;

import java.util.*;

public class AiravataManager {

    private static String username="";
    private static String password="";

    private static String accessToken="";
    private static String refreshToken="";

    private static AuthzToken authzToken;
    private static Project currentProject;

    public static AuthzToken getAuthzToken(){
        try {
            getClient().getAPIVersion(authzToken);
        } catch (TException e) {
            e.printStackTrace();
            AiravataManager.logout();
            AiravataManager.login(AiravataManager.username,AiravataManager.password);
        }
        return AiravataManager.authzToken;
    }

    public static boolean login(String uname, String passwd) {
        try {
            String[] response = getClient().login(uname, passwd);
            String profile = getClient().getProfile(response[0]);
            HashMap<String,Object> result =
                    new ObjectMapper().readValue(profile, HashMap.class);

            String[] roles = ((String)result.get("roles")).split(",");
            boolean authorized = false;
            for(String role : roles){
                if(AiravataConfig.getProperty("authorized_roles").contains(role)){
                    authorized = true;
                    break;
                }
            }
            if(!authorized){
                throw new SessionException("You are not authorized to use this client. Please contact gateway admin");
            }
            accessToken = response[0];
            refreshToken = response[1];

            authzToken = new AuthzToken();
            authzToken.setAccessToken(accessToken);
        }catch (Exception e){
            e.printStackTrace();
            throw new SessionException("Login failed! Please try again");
        }
        AiravataManager.username = uname;
        AiravataManager.password = passwd;


        Settings.gridchemusername = uname;
        Settings.authenticated = true;

        System.out.println("Successfully authenticated.  Access Token is " + accessToken);
        return true;
    }

    public static void logout() {
        accessToken = "";
        refreshToken = "";
        authzToken = null;

        Settings.gridchemusername = null;
        Settings.authenticated = false;
    }

    public static void createNewProject(String projectName) throws Exception{
        Project project = new Project();
        project.setName(projectName);
        project.setOwner(GridChem.user.getUserName());
        try{
            getClient().createProject(getAuthzToken(), AiravataConfig.getProperty(AiravataConfig.GATEWAY), project);
        }catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public static List<Project> getProjects() throws ProjectException {

        List<Project> airavataProjects;
        try {
            airavataProjects = getClient().getUserProjects(
                    getAuthzToken(), AiravataConfig.getProperty(AiravataConfig.GATEWAY)
                    , Settings.gridchemusername, -1, 0);
        }catch (Exception e) {
            try{
                getClient().createProject(getAuthzToken(), AiravataConfig.getProperty(AiravataConfig.GATEWAY)
                        , new Project("no-id",Settings.gridchemusername,"Default Project"));
                airavataProjects = getClient().getUserProjects(
                        getAuthzToken(), AiravataConfig.getProperty(AiravataConfig.GATEWAY)
                        , Settings.gridchemusername, -1, 0);
            }catch (Exception ex){
                throw new ProjectException(ex);
            }
        }
        return airavataProjects;
    }

    public static Project getProject(String projectId){
        try {
            return getClient().getProject(getAuthzToken(), projectId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Project getCurrentProject() throws ProjectException {
        return currentProject;
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

    public static List<ApplicationDeploymentDescription> getApplicationDeployments()
            throws AiravataClientException, TException {
        return getClient().getAllApplicationDeployments(
                getAuthzToken(), AiravataConfig.getProperty(AiravataConfig.GATEWAY));
    }

    public static AiravataClient getClient() throws AiravataClientException {
        String host = AiravataConfig.getProperty("thrift_host");
        int port = Integer.parseInt(AiravataConfig.getProperty("thrift_port"));
        try {

            TTransport transport = new TSocket(host, port);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            return new AiravataClient(protocol);
        } catch (TTransportException e) {
            throw new AiravataClientException(AiravataErrorType.UNKNOWN);
        }
    }

    public static ArrayList<ComputeResourceDescription> getComputationalResources()
            throws AiravataClientException, TException {
        Map <String,String> compResources = getClient().getAllComputeResourceNames(getAuthzToken());
        Iterator<String> it = compResources.keySet().iterator();

        ArrayList<ComputeResourceDescription> compList = new ArrayList<ComputeResourceDescription>();
        while (it.hasNext()){
            compList.add(getClient().getComputeResource(getAuthzToken(), it.next()));
        }

        return compList;
    }

    public static  List<ApplicationDeploymentDescription> getAppDepDescriptionforMachine(
            String computeHostId) throws AiravataClientException, TException {
        List<ApplicationDeploymentDescription> appDeployments = getClient()
                .getAllApplicationDeployments(getAuthzToken(), AiravataConfig
                        .getProperty(AiravataConfig.GATEWAY));
        List<ApplicationDeploymentDescription> returnApps = new ArrayList<ApplicationDeploymentDescription>();
        for (ApplicationDeploymentDescription ad : appDeployments){
            if(ad.getComputeHostId().equals(computeHostId)){
                returnApps.add(ad);
            }
        }
        return returnApps;
    }

    public static List<ComputeResourceDescription> getCompResourcesForAppModule(String appModuleName)
            throws AiravataClientException, TException {
        List<ComputeResourceDescription> computeResources = new ArrayList<>();
        List<ApplicationDeploymentDescription> appDeployments =  getClient()
                .getAllApplicationDeployments(getAuthzToken(), AiravataConfig.getProperty(
                        AiravataConfig.GATEWAY));
        Set<String> comResourceids= new HashSet<>();
        for(ApplicationDeploymentDescription app:appDeployments){
            if(getClient().getApplicationModule(getAuthzToken(), app.getAppModuleId())
                    .getAppModuleName().equals(appModuleName)){
                comResourceids.add(app.getComputeHostId());
            }
        }

        Iterator<String> it = comResourceids.iterator();
        while(it.hasNext()){
            computeResources.add(getClient().getComputeResource(getAuthzToken(), it.next()));
        }

        return computeResources;
    }

    public static List<ComputeResourceDescription> getCompResourcesForAppId(String appId)
            throws AiravataClientException, TException {
        List<ComputeResourceDescription> computeResources = new ArrayList<>();
        ApplicationDeploymentDescription app =  getClient().getApplicationDeployment(getAuthzToken(), appId);
        computeResources.add(getClient().getComputeResource(getAuthzToken(), app.getComputeHostId()));
        return computeResources;
    }

    public static ComputeResourceDescription getComputeResourceDescriptionFromName(String hostName)
            throws AiravataClientException, TException {

        Map<String,String> compMap= getClient().getAllComputeResourceNames(getAuthzToken());
        Iterator<String> it = compMap.keySet().iterator();
        while(it.hasNext()){
            String id =it.next();
            ComputeResourceDescription desc = getClient().getComputeResource(getAuthzToken(), id);
            if(desc.getHostName().equals(hostName)){
                return desc;
            }
        }
        return null;

    }

    public static ComputeResourceDescription getComputeResourceDescriptionFromId(String id)
            throws AiravataClientException, TException {
        return getClient().getComputeResource(getAuthzToken(), id);
    }

    public static List<ComputeResourceDescription> getComputationalResources(String appInterfaceID){
        List<ComputeResourceDescription> compResourceList = new ArrayList<>();
        try {
            Map<String, String> comps = getClient().getAvailableAppInterfaceComputeResources(
                    getAuthzToken(), appInterfaceID);
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

    public static ApplicationModule getApplicationModule(String id)
            throws AiravataClientException, TException {
        return getClient().getApplicationModule(getAuthzToken(), id);
    }

    public static ApplicationDeploymentDescription getApplicationDeploymentDescription(String id)
            throws AiravataClientException, TException {
        return getClient().getApplicationDeployment(getAuthzToken(), id);
    }

    public static List<ApplicationInterfaceDescription> getAllAppInterfaces(){
        try{
            return getClient().getAllApplicationInterfaces(getAuthzToken(), AiravataConfig
                    .getProperty(AiravataConfig.GATEWAY));
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static List<ExperimentSummaryModel> getQueuedExperiments(){
        List<ExperimentSummaryModel> exp = new ArrayList<>();
        try{
            Map<ExperimentSearchFields,String> filters = new HashMap<>();
            filters.put(ExperimentSearchFields.STATUS,"CREATED");
            exp = getClient().searchExperiments(
                    getAuthzToken(), AiravataConfig.getProperty(AiravataConfig.GATEWAY), GridChem.user.getUserName(), filters, -1, 0);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return exp;
    }

    public static List<ExperimentSummaryModel> getLaunchedExperiments(){
        List<ExperimentSummaryModel> allExps = new ArrayList<>();
        try{
            Map<ExperimentSearchFields,String> filters = new HashMap<>();
            filters.put(ExperimentSearchFields.STATUS,"LAUNCHED");
            List<ExperimentSummaryModel> exp = getClient().searchExperiments(
                    getAuthzToken(), AiravataConfig.getProperty(AiravataConfig.GATEWAY), GridChem.user.getUserName(), filters, -1, 0);
            allExps.addAll(exp);
            filters.put(ExperimentSearchFields.STATUS,"EXECUTING");
            exp = getClient().searchExperiments(
                    getAuthzToken(), AiravataConfig.getProperty(AiravataConfig.GATEWAY), GridChem.user.getUserName(), filters, -1, 0);
            allExps.addAll(exp);
            filters.put(ExperimentSearchFields.STATUS,"SCHEDULED");
            exp = getClient().searchExperiments(
                    getAuthzToken(), AiravataConfig.getProperty(AiravataConfig.GATEWAY), GridChem.user.getUserName(), filters, -1, 0);
            allExps.addAll(exp);

            Collections.sort(allExps, new Comparator<ExperimentSummaryModel>() {
                @Override
                public int compare(ExperimentSummaryModel o1, ExperimentSummaryModel o2) {
                    return (int)(o1.getStatusUpdateTime()-o2.getStatusUpdateTime());
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return allExps;
    }

    public static ExperimentModel getExperiment(String experimentId){
        try{
            ExperimentModel experimentModel = getClient().getExperiment(getAuthzToken(),experimentId);
            return experimentModel;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static List<ExperimentModel> getAllExperimentsInProject(String projectId){
        List<ExperimentModel> exp = new ArrayList<>();
        try{
            exp = getClient().getExperimentsInProject(
                    getAuthzToken(), projectId, -1, 0);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return exp;
    }

    public static List<ExperimentSummaryModel> getAllExperimentSummariesInProject(String projectId){
        List<ExperimentSummaryModel> exp = new ArrayList<>();
        try{
            Map<ExperimentSearchFields, String> fields = new HashMap<ExperimentSearchFields, String>();
            if(!projectId.equals("*")) {
                fields.put(ExperimentSearchFields.PROJECT_ID, projectId);
            }
            exp = getClient().searchExperiments(
                    getAuthzToken(), AiravataConfig.getProperty("gateway"), GridChem.user.getUserName(), fields, -1, 0);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return exp;
    }

    public static List<OutputDataObjectType> getApplicationOutputs(String appId) {
        try{
            return getClient().getApplicationOutputs(getAuthzToken(), appId);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static List<InputDataObjectType> getApplicationInputs(String appId) {
        try{
            return getClient().getApplicationInputs(getAuthzToken(), appId);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static String getLocalJobId(String experimentId){
        try{
            List<JobModel> jobs = getClient().getJobDetails(getAuthzToken(), experimentId);
            if(jobs!=null && jobs.size()>0){
                return jobs.get(0).getJobId();
            }
            return "";
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean cancelExperiment(String experimentId){
        try{
            getClient().terminateExperiment(getAuthzToken(), experimentId, AiravataConfig.getProperty("gateway"));
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean deleteExperiment(String experimentId){
        try{
            getClient().deleteExperiment(getAuthzToken(), experimentId);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public static String getJobStatus(String experimentId){
        try{
            List<JobModel> jobDetails = getClient().getJobDetails(getAuthzToken(), experimentId);
            if(jobDetails != null && jobDetails.size()>0){
                return jobDetails.get(0).getJobStatus().getJobState().toString();
            }
            return null;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
