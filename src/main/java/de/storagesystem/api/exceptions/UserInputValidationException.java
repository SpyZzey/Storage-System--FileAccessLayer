package de.storagesystem.api.exceptions;

/**
 * @author Simon Brebeck
 */
public class UserInputValidationException extends Exception {
    /**
     * Creates a new instance of <code>UserInputValidationException</code>  with the specified detail message.
     * @param message the detail message. Stored for later retrieval by the {@link #getMessage()} method.
     */
    public UserInputValidationException(String message) {
        super(message);
    }
}
