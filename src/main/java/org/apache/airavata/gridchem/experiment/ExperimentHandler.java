package org.apache.airavata.gridchem.experiment;

import com.asprise.util.ui.progress.ProgressDialog;
import org.apache.airavata.AiravataConfig;
import org.apache.airavata.ExpetimentConst;
import org.apache.airavata.gridchem.AiravataManager;
import org.apache.airavata.gridchem.file.FileBrowserAiravata;
import org.apache.airavata.gridchem.file.FileHandlerException;
import org.apache.airavata.model.application.io.DataType;
import org.apache.airavata.model.application.io.InputDataObjectType;
import org.apache.airavata.model.experiment.ExperimentModel;
import org.apache.airavata.model.experiment.UserConfigurationDataModel;
import org.apache.airavata.model.scheduling.ComputationalResourceSchedulingModel;
import org.apache.airavata.model.status.ExperimentState;
import org.apache.airavata.model.util.ExperimentModelUtil;
import org.gridchem.client.common.Settings;

import java.io.File;
import java.math.BigInteger;
import java.util.*;

public class ExperimentHandler {

    private final Random random = new Random();
    FileBrowserAiravata fb;

    public ExperimentHandler() {
        fb = new FileBrowserAiravata();
    }

    public String createExperiment(Map<String, Object> params) throws ExperimentCreationException {

        String projectID = null, userID = null, expName = null, expDesc = null, appId = null, gatewayId = null, hostID = null, queue = null, projectAccount = null;
        Integer cpuCount = null, nodeCount = null, wallTime = null, physicalMemory = null;
        List<InputDataObjectType> inputs = null;

        if (params.get(ExpetimentConst.PROJECT_ID) != null)
            projectID = (String) params.get(ExpetimentConst.PROJECT_ID);

        if (params.get(ExpetimentConst.USER_ID) != null)
            userID = (String) params.get(ExpetimentConst.USER_ID);

        if (params.get(ExpetimentConst.EXP_NAME) != null)
            expName = (String) params.get(ExpetimentConst.EXP_NAME);

        if (params.get(ExpetimentConst.APP_ID) != null)
            appId = (String) params.get(ExpetimentConst.APP_ID);

        if (params.get(ExpetimentConst.GATEWAY_ID) != null)
            gatewayId = (String) params.get(ExpetimentConst.GATEWAY_ID);

        if (params.get(ExpetimentConst.RESOURCE_HOST_ID) != null)
            hostID = (String) params.get(ExpetimentConst.RESOURCE_HOST_ID);

        if (params.get(ExpetimentConst.QUEUE) != null)
            queue = (String) params.get(ExpetimentConst.QUEUE);

        if (params.get(ExpetimentConst.CPU_COUNT) != null)
            cpuCount = (Integer) params.get(ExpetimentConst.CPU_COUNT);

        if (params.get(ExpetimentConst.NODE_COUNT) != null)
            nodeCount = (Integer) params.get(ExpetimentConst.NODE_COUNT);

        if (params.get(ExpetimentConst.WALL_TIME) != null)
            wallTime = (Integer) params.get(ExpetimentConst.WALL_TIME);

        if (params.get(ExpetimentConst.MEMORY) != null)
            physicalMemory = (Integer) params.get(ExpetimentConst.MEMORY);

        if(params.get(ExpetimentConst.INPUTS)!=null){
            inputs = (List<InputDataObjectType>)params.get(ExpetimentConst.INPUTS);
        }

        try {
            validateInputs(inputs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ExperimentModel exp =
                ExperimentModelUtil.createSimpleExperiment(null, null, null, null, null, null,null);
        exp.setProjectId(projectID);
        exp.setUserName(userID);
        exp.setExperimentName(expName);
        exp.setDescription(expDesc);
        exp.setExecutionId(appId);

        List<InputDataObjectType> applicationInputs = AiravataManager.getApplicationInputs(appId);
        Collections.sort(applicationInputs, new Comparator<InputDataObjectType>() {
            @Override
            public int compare(InputDataObjectType o1, InputDataObjectType o2) {
                return o1.getInputOrder() - o2.getInputOrder();
            }
        });
        for(int i=0;i < applicationInputs.size();i++){
            applicationInputs.get(i).setType(inputs.get(i).getType());
            applicationInputs.get(i).setValue(inputs.get(i).getValue());
        }
        exp.setExperimentInputs(applicationInputs);

        exp.setExperimentOutputs(AiravataManager.getApplicationOutputs(appId));
        exp.setGatewayId(gatewayId);
        try {
            exp.setExperimentOutputs(AiravataManager
                    .getClient()
                    .getApplicationInterface(AiravataManager.getAuthzToken(),appId)
                    .getApplicationOutputs());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ComputationalResourceSchedulingModel scheduling =
                ExperimentModelUtil.createComputationResourceScheduling(null, 0, 0, 0, null, 0, 0);

        scheduling.setResourceHostId(hostID);
        scheduling.setTotalCPUCount(cpuCount);
        scheduling.setNodeCount(nodeCount);
        scheduling.setQueueName(queue);
        scheduling.setWallTimeLimit(wallTime);
        scheduling.setTotalPhysicalMemory(physicalMemory);

        UserConfigurationDataModel userConfigurationData = new UserConfigurationDataModel();

        userConfigurationData.setAiravataAutoSchedule(false);
        userConfigurationData.setOverrideManualScheduledParams(false);
        userConfigurationData.setComputationalResourceScheduling(scheduling);
        exp.setUserConfigurationData(userConfigurationData);

        String experimentId = null;
        try {
            experimentId = AiravataManager.getClient().createExperiment(AiravataManager.getAuthzToken(),
                    AiravataConfig.getProperty(AiravataConfig.GATEWAY),exp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExperimentCreationException("Error at creating experiment "+expName+ " on machine "+hostID,e);
        }
        return experimentId;

    }

    private void validateInputs(List<InputDataObjectType> inputs) throws FileHandlerException{
        for (int i=0; i<inputs.size(); i++) {
            if (DataType.URI.equals(inputs.get(i).getType())) {

                File file = new File(inputs.get(i).getValue());
                if (file.exists()) {
                    String randSeq = new BigInteger(130, random).toString(32);
                    while (fb.hasProduct(randSeq)) {
                        randSeq = new BigInteger(130, random).toString(32);
                    }
                    String parent = file.getParent();
                    String fileName = file.getName();
                    String productId = fb.ingestFile(parent,fileName, Settings.gridchemusername+randSeq, "GenericFile");
                    System.out.println("Product ID : "+productId);
                    inputs.get(i).setValue("file://"+AiravataConfig.getProperty("data_archive_path")+"/"+
                            Settings.gridchemusername+randSeq+"/"+fileName);
                }
            }
        }
    }

    public void launchExperiment(String expID,Map<String,Object> params) throws ExperimentCreationException {
        ProgressDialog progressDialog=null;
        if(params.containsKey("progressDialog")){
            progressDialog = (ProgressDialog)params.get("progressDialog");
            progressDialog.beginSubTask("Submitting experiment to queue ... ",2);
        }
        String sshTokenId = AiravataConfig.getProperty("ssh_token_id");
        try {
            AiravataManager.getClient().launchExperiment(AiravataManager.getAuthzToken(), expID,
                    AiravataConfig.getProperty("gateway"));
            ExperimentState state = AiravataManager.getClient().getExperiment(AiravataManager.getAuthzToken(),
                    expID).getExperimentStatus().getState();
            while (ExperimentState.CREATED.equals(state)){
                Thread.sleep(1000);
                state = AiravataManager.getClient().getExperiment(AiravataManager.getAuthzToken(),expID)
                        .getExperimentStatus().getState();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ExperimentCreationException("Error at creating experiment "+expID +" with ssh "+sshTokenId,ex);
        }
        if(progressDialog!=null){
            progressDialog.finished();
        }
    }


}
