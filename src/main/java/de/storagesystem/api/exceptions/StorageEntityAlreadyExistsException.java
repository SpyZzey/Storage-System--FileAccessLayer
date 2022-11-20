package de.storagesystem.api.exceptions;

/**
 * @author Simon Brebeck
 */
public class StorageEntityAlreadyExistsException extends RuntimeException {
    /**
     * Creates a new instance of <code>StorageEntityAlreadyExistsException</code>  with the specified detail message.
     * @param message the detail message. Stored for later retrieval by the {@link #getMessage()} method.
     */
    public StorageEntityAlreadyExistsException(String message) {
        super(message);
    }
}
