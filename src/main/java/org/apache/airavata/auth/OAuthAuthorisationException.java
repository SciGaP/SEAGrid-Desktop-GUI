package org.apache.airavata.auth;

/**
 * Created by dimuthuupeksha on 4/16/15.
 */
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
