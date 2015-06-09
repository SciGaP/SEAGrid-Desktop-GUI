package org.apache.airavata.gridchem.experiment;

import com.asprise.util.ui.progress.ProgressDialog;
import org.apache.airavata.AiravataConfig;
import org.apache.airavata.ExpetimentConst;
import org.apache.airavata.gridchem.AiravataManager;
import org.apache.airavata.model.appcatalog.appinterface.DataType;
import org.apache.airavata.model.appcatalog.appinterface.InputDataObjectType;
import org.apache.airavata.model.appcatalog.appinterface.OutputDataObjectType;
import org.apache.airavata.model.error.AiravataClientConnectException;
import org.apache.airavata.model.util.ExperimentModelUtil;
import org.apache.airavata.model.workspace.experiment.ComputationalResourceScheduling;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.airavata.model.workspace.experiment.UserConfigurationData;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dimuthuupeksha on 5/10/15.
 */
public class EchoExperimentHandler extends ExperimentHandler {
    @Override
    public String createExperiment(Map<String, Object> params) throws ExperimentCreationException {

        String projectID = null, userID = null, expName = null, expDesc = null, appId = null, hostID = null, queue = null, projectAccount = null;
        Integer cpuCount = null, threadCount = null, nodeCount = null, wallTime = null, startTime = null, physicalMemory = null;

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


        List<InputDataObjectType> exIputs = new ArrayList<>();
        InputDataObjectType input = new InputDataObjectType();
        input.setName("Input_to-Echo");
        input.setType(DataType.STRING);
        input.setValue("Echoed_Output=Hello World");
        exIputs.add(input);

        List<OutputDataObjectType> exOut = new ArrayList<>();
        OutputDataObjectType output = new OutputDataObjectType();
        output.setName("Echoed_Output");
        output.setType(DataType.STRING);
        output.setValue("");
        exOut.add(output);

        Experiment echoExp =
                ExperimentModelUtil.createSimpleExperiment(null, null, null, null, null, null);
        echoExp.setProjectID(projectID);
        echoExp.setUserName(userID);
        echoExp.setName(expName);
        echoExp.setDescription(expDesc);
        echoExp.setApplicationId(appId);
        echoExp.setExperimentInputs(exIputs);

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
        echoExp.setUserConfigurationData(userConfigurationData);

        String experimentId = null;
        try {
            experimentId = AiravataManager.getClient().createExperiment(AiravataConfig.getProperty(AiravataConfig.GATEWAY),echoExp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExperimentCreationException("Error at creating experiment "+expName+ " on machine "+hostID,e);
        }
        return experimentId;

    }
}
