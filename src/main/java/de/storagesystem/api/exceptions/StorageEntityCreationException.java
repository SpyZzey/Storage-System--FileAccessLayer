package de.storagesystem.api.exceptions;

/**
 * @author Simon Brebeck
 */
public class StorageEntityCreationException extends RuntimeException {
    /**
     * Creates a new instance of <code>StorageEntityCreationException</code>  with the specified detail message.
     * @param message the detail message. Stored for later retrieval by the {@link #getMessage()} method.
     */
    public StorageEntityCreationException(String message) {
        super(message);
    }
}
