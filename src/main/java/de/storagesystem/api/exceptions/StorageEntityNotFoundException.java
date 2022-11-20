package de.storagesystem.api.exceptions;

/**
 * @author Simon Brebeck
 */
public class StorageEntityNotFoundException extends RuntimeException {

    /**
     * Stores the Entity Name of the Entity that was not found.
     */
    private String entityName;

    /**
     * Creates a new instance of <code>StorageEntityCreationException</code>  with the specified detail message.
     * @param message the detail message. Stored for later retrieval by the {@link #getMessage()} method.
     */
    public StorageEntityNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of <code>StorageEntityCreationException</code>  with the specified detail message.
     * @param message the detail message. Stored for later retrieval by the {@link #getMessage()} method.
     * @param entityName the name of the entity that was not found. Stored for later retrieval by the {@link #entityName()} method.
     */
    public StorageEntityNotFoundException(String message, String entityName) {
        super(message);
        this.entityName = entityName;
    }

    /**
     * Returns the name of the entity that was not found.
     * @return the name of the entity that was not found.
     */
    @SuppressWarnings("unused")
    public String entityName() {
        return entityName;
    }
}
