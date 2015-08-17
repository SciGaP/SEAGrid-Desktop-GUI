package org.apache.airavata.gridchem;

/**
 * Created by dimuthuupeksha on 8/17/15.
 */
public class FileHandlerException extends Exception {
    public FileHandlerException () {

    }

    public FileHandlerException (String message) {
        super (message);
    }

    public FileHandlerException (Throwable cause) {
        super (cause);
    }

    public FileHandlerException (String message, Throwable cause) {
        super (message, cause);
    }
}
