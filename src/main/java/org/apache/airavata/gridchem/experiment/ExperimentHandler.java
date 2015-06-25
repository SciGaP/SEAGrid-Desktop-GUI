package org.apache.airavata.gridchem.experiment;

import com.asprise.util.ui.progress.ProgressDialog;
import org.apache.airavata.gridchem.AiravataManager;
import org.apache.airavata.model.error.AiravataClientException;
import org.apache.airavata.model.error.AiravataSystemException;
import org.apache.airavata.model.error.ExperimentNotFoundException;
import org.apache.airavata.model.error.InvalidRequestException;
import org.apache.airavata.model.workspace.experiment.ExperimentState;
import org.apache.thrift.TException;

import javax.swing.*;
import java.util.Map;

/**
 * Created by dimuthuupeksha on 5/10/15.
 */
public class ExperimentHandler {

    public void bindUIs(JPanel reqPane){

    }

    public String createExperiment(Map<String,Object> params) throws ExperimentCreationException {
        return null;
    }

    public void launchExperiment(String expID,Map<String,Object> params) throws ExperimentCreationException {
        ProgressDialog progressDialog=null;
        if(params.containsKey("progressDialog")){
            progressDialog = (ProgressDialog)params.get("progressDialog");
            progressDialog.beginSubTask("Submitting experiment to queue ... ",2);
        }
        String sshTokenId = "2c308fa9-99f8-4baa-92e4-d062e311483c";
        try {
            AiravataManager.getClient().launchExperiment(expID, sshTokenId);
            ExperimentState state = AiravataManager.getClient().getExperiment(expID).getExperimentStatus().getExperimentState();
            while (ExperimentState.CREATED.equals(state)){
                Thread.sleep(1000);
                state = AiravataManager.getClient().getExperiment(expID).getExperimentStatus().getExperimentState();
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
