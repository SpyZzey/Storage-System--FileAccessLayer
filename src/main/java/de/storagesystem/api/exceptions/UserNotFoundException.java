package de.storagesystem.api.exceptions;

/**
 * @author Simon Brebeck
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Creates a new instance of <code>UserNotFoundException</code>  with the specified detail message.
     * @param message the detail message. Stored for later retrieval by the {@link #getMessage()} method.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
