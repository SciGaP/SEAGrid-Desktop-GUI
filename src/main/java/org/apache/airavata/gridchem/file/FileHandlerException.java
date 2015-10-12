package org.apache.airavata.gridchem.file;

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
