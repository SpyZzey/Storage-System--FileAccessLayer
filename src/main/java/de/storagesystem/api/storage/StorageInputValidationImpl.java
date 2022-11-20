package de.storagesystem.api.storage;

/**
 * @author Simon Brebeck on 17.11.2022
 */
public class StorageInputValidationImpl implements StorageInputValidation {

    /**
     * The default constructor of the {@link StorageInputValidationImpl}
     */
    public StorageInputValidationImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateFilePath(String filePath) {
        if(filePath == null) return false;
        if(filePath.isEmpty()) return false;
        if(filePath.startsWith("/")) return false;
        if(filePath.endsWith("/")) return false;

        return filePath.length() < 2048;
    }

}
