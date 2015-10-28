package org.apache.airavata.gridchem.file;

public class FileSizeTooLargeException extends Exception {
    public FileSizeTooLargeException() {

    }

    public FileSizeTooLargeException(String message) {
        super (message);
    }

    public FileSizeTooLargeException(Throwable cause) {
        super (cause);
    }

    public FileSizeTooLargeException(String message, Throwable cause) {
        super (message, cause);
    }
}
