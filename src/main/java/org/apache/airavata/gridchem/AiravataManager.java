package org.apache.airavata.gridchem;

import org.apache.airavata.AiravataConfig;
import org.apache.airavata.model.appcatalog.appdeployment.ApplicationDeploymentDescription;
import org.apache.airavata.model.appcatalog.appdeployment.ApplicationModule;
import org.apache.airavata.model.appcatalog.appinterface.ApplicationInterfaceDescription;
import org.apache.airavata.model.appcatalog.computeresource.ComputeResourceDescription;
import org.apache.airavata.model.error.AiravataClientConnectException;
import org.apache.airavata.model.workspace.Project;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.airavata.model.workspace.experiment.ExperimentState;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.gridchem.client.common.Settings;
import org.gridchem.service.beans.*;
import org.gridchem.service.exceptions.*;
import org.json.JSONObject;
import java.util.*;

/**
 * @author Dimuthu
 */
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

        System.out.println("Successfully authenticated.  Access Token is " + accessToken);
        return true;
    }

    public static void logout() {
        accessToken = "";
        Settings.gridchemusername = null;
        Settings.authenticated = false;

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

    public static List<ApplicationDeploymentDescription> getAplicationDeployments() throws AiravataClientConnectException, TException {
        return getClient().getAllApplicationDeployments(AiravataConfig.getProperty(AiravataConfig.GATEWAY));
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
        return getClient().getComputeResource(id);
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
