package org.apache.airavata.gridchem.experiment;

import org.apache.airavata.gridchem.AiravataManager;
import org.apache.airavata.model.appcatalog.appinterface.ApplicationInterfaceDescription;

import java.util.List;

/**
 * @author Dimuthu
 */
public class ExperimentHandlerUtils {
    public static ExperimentHandler getExperimentHandler(String appInterfaceDescID){
        return new ExperimentHandler();
    }
}
