package org.apache.airavata.gridchem.experiment;

/**
 * Created by dimuthuupeksha on 5/10/15.
 */
public class ExperimentCreationException extends Exception{
    public ExperimentCreationException () {

    }

    public ExperimentCreationException (String message) {
        super (message);
    }

    public ExperimentCreationException (Throwable cause) {
        super (cause);
    }

    public ExperimentCreationException (String message, Throwable cause) {
        super (message, cause);
    }
}
