package de.storagesystem.api.exceptions;

/**
 * @author Simon Brebeck
 */
public class RootAlreadySetException extends Exception {

    /**
     * Creates a new instance of <code>RootAlreadySetException</code>  with the specified detail message.
     * @param message the detail message. Stored for later retrieval by the {@link #getMessage()} method.
     */
    public RootAlreadySetException(String message) {
        super(message);
    }

}
