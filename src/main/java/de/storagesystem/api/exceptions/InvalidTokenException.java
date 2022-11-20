package de.storagesystem.api.exceptions;

/**
 * @author Simon Brebeck
 */
public class InvalidTokenException extends RuntimeException {
    /**
     * Creates a new instance of <code>InvalidTokenException</code>  with the specified detail message.
     * @param message the detail message. Stored for later retrieval by the {@link #getMessage()} method.
     */
    public InvalidTokenException(String message) {
        super(message);
    }

}
