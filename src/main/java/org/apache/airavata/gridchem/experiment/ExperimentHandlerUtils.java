package org.apache.airavata.gridchem.experiment;

import org.apache.airavata.gridchem.AiravataManager;
import org.apache.airavata.model.appcatalog.appinterface.ApplicationInterfaceDescription;

import java.util.List;

/**
 * Created by dimuthuupeksha on 6/13/15.
 */
public class ExperimentHandlerUtils {
    public static ExperimentHandler getExperimentHandler(String appInterfaceDescID){

        List<ApplicationInterfaceDescription> appInterfaces = AiravataManager.getAllAppInterfaces();
        String applicationName =null;
        for(ApplicationInterfaceDescription desc : appInterfaces){
            if(desc.getApplicationInterfaceId().equals(appInterfaceDescID)){
                applicationName = desc.getApplicationName();
                break;
            }
        }
        switch (applicationName){
            case "Echo":
                return new EchoExperimentHandler();
            case "Gaussian":
                return new GaussianExperimentHandler();
            default:
                return new ExperimentHandler();
        }
    }
}
