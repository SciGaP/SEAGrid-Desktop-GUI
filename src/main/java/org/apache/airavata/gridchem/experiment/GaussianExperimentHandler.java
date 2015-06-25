package org.apache.airavata.gridchem.experiment;

import org.apache.airavata.AiravataConfig;
import org.apache.airavata.ExpetimentConst;
import org.apache.airavata.gridchem.AiravataManager;
import org.apache.airavata.model.appcatalog.appinterface.DataType;
import org.apache.airavata.model.appcatalog.appinterface.InputDataObjectType;
import org.apache.airavata.model.appcatalog.appinterface.OutputDataObjectType;
import org.apache.airavata.model.util.ExperimentModelUtil;
import org.apache.airavata.model.workspace.experiment.ComputationalResourceScheduling;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.airavata.model.workspace.experiment.UserConfigurationData;
import org.apache.airavata.util.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dimuthuupeksha on 6/14/15.
 */
public class GaussianExperimentHandler extends ExperimentHandler {
    @Override
    public String createExperiment(Map<String, Object> params) throws ExperimentCreationException {
        String projectID = null, userID = null, expName = null, expDesc = null, appId = null, hostID = null, queue = null, projectAccount = null;
        Integer cpuCount = null, threadCount = null, nodeCount = null, wallTime = null, startTime = null, physicalMemory = null;
        List<File> inputFiles = null;

        if (params.get(ExpetimentConst.PROJECT_ID) != null)
            projectID = (String) params.get(ExpetimentConst.PROJECT_ID);

        if (params.get(ExpetimentConst.USER_ID) != null)
            userID = (String) params.get(ExpetimentConst.USER_ID);

        if (params.get(ExpetimentConst.EXP_NAME) != null)
            expName = (String) params.get(ExpetimentConst.EXP_NAME);

        if (params.get(ExpetimentConst.APP_ID) != null)
            appId = (String) params.get(ExpetimentConst.APP_ID);

        if (params.get(ExpetimentConst.RESOURCE_HOST_ID) != null)
            hostID = (String) params.get(ExpetimentConst.RESOURCE_HOST_ID);

        if (params.get(ExpetimentConst.QUEUE) != null)
            queue = (String) params.get(ExpetimentConst.QUEUE);

        if (params.get(ExpetimentConst.PROJECT_ACCOUNT) != null)
            projectAccount = (String) params.get(ExpetimentConst.PROJECT_ACCOUNT);

        if (params.get(ExpetimentConst.CPU_COUNT) != null)
            cpuCount = (Integer) params.get(ExpetimentConst.CPU_COUNT);

        if (params.get(ExpetimentConst.THREADS) != null)
            threadCount = (Integer) params.get(ExpetimentConst.THREADS);

        if (params.get(ExpetimentConst.NODE_COUNT) != null)
            nodeCount = (Integer) params.get(ExpetimentConst.NODE_COUNT);

        if (params.get(ExpetimentConst.WALL_TIME) != null)
            wallTime = (Integer) params.get(ExpetimentConst.WALL_TIME);

        if (params.get(ExpetimentConst.START_TIME) != null)
            startTime = (Integer) params.get(ExpetimentConst.START_TIME);

        if (params.get(ExpetimentConst.MEMORY) != null)
            physicalMemory = (Integer) params.get(ExpetimentConst.MEMORY);

        if(params.get(ExpetimentConst.INPUT_FILES)!=null){
            inputFiles = (List<File>)params.get(ExpetimentConst.INPUT_FILES);
        }

        if(inputFiles.size()==0){
            throw new ExperimentCreationException("For gaussian jobs there should be an input file");
        }
        int random = (int)Math.random()*10000000;
        String remotePath = "/tmp/gridchem_client_"+random+"/"+inputFiles.get(0).getName();
        System.out.println("Remote path "+ remotePath);
        System.out.println("Uploading file ....");
        //uploads file to server
        if(FileManager.uploadFile(inputFiles.get(0).getAbsolutePath(),remotePath)){
            System.out.println("Input file successfully uploaded");
        }else{
            throw new ExperimentCreationException("Input file upload failed");
        }


        List<InputDataObjectType> exIputs = new ArrayList<>();
        InputDataObjectType input = new InputDataObjectType();
        input.setName("MainInputFile");
        input.setType(DataType.URI);
        input.setValue(remotePath);
        exIputs.add(input);

        List<OutputDataObjectType> exOut = new ArrayList<>();
        OutputDataObjectType output = new OutputDataObjectType();
        output.setName("gaussian.out");
        output.setType(DataType.URI);
        output.setValue("");
        exOut.add(output);

        Experiment gaussianExp =
                ExperimentModelUtil.createSimpleExperiment(null, null, null, null, null, null);
        gaussianExp.setProjectID(projectID);
        gaussianExp.setUserName(userID);
        gaussianExp.setName(expName);
        gaussianExp.setDescription(expDesc);
        gaussianExp.setApplicationId(appId); //application interface ID
        gaussianExp.setExperimentInputs(exIputs);

        ComputationalResourceScheduling scheduling =
                ExperimentModelUtil.createComputationResourceScheduling(null, 0, 0, 0, null, 0, 0, 0, null);

        scheduling.setResourceHostId(hostID);
        scheduling.setTotalCPUCount(cpuCount);
        scheduling.setNodeCount(nodeCount);
        scheduling.setNumberOfThreads(threadCount);
        scheduling.setQueueName(queue);
        scheduling.setWallTimeLimit(wallTime);
        scheduling.setJobStartTime(startTime);
        scheduling.setTotalPhysicalMemory(physicalMemory);
        scheduling.setComputationalProjectAccount(projectAccount);

        UserConfigurationData userConfigurationData = new UserConfigurationData();

        userConfigurationData.setAiravataAutoSchedule(false);
        userConfigurationData.setOverrideManualScheduledParams(false);
        userConfigurationData.setComputationalResourceScheduling(scheduling);
        gaussianExp.setUserConfigurationData(userConfigurationData);

        String experimentId = null;
        try {
            experimentId = AiravataManager.getClient().createExperiment(AiravataConfig.getProperty(AiravataConfig.GATEWAY),gaussianExp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExperimentCreationException("Error at creating experiment "+expName+ " on machine "+hostID,e);
        }
        return experimentId;
    }
}
