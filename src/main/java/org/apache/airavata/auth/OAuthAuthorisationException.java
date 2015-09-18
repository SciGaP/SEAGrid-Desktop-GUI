package org.apache.airavata.auth;


public class OAuthAuthorisationException extends Exception{
    public OAuthAuthorisationException () {

    }

    public OAuthAuthorisationException (String message) {
        super (message);
    }

    public OAuthAuthorisationException (Throwable cause) {
        super (cause);
    }

    public OAuthAuthorisationException (String message, Throwable cause) {
        super (message, cause);
    }
}
