package de.storagesystem.api.storage;

/**
 * @author Simon Brebeck
 */
public interface StorageInputValidation {

    /**
     * Checks if the given string is a valid path for a file.
     *
     * @param filePath      The path of the file to validate.
     * @return true if the name is valid, false otherwise.
     */
    boolean validateFilePath(String filePath);

}
