package de.storagesystem.api.storage;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Simon Brebeck
 */
public interface StorageFileService {

    /**
     * Initializes the storage file service.
     */
    void init();

    /**
     * Stores a given file in the storage under a given bucket and directory.
     *
     * @param serverAuth    The authentication token of the master server.
     * @param userId        The user id of the user the file belongs to.
     * @param file          The file to store
     * @return {@code ResponseEntity<Map<String, String>>}
     */
    ResponseEntity<Map<String, String>> storeFile(String serverAuth, long userId, MultipartFile file);

    /**
     * Deletes a file from a user bucket
     *
     * @param serverAuth    The authentication token of the master server.
     * @param userId        The user id of the user the file belongs to.
     * @param filePath      The path of the file.
     * @return {@code ResponseEntity<Map<String, String>>}
     */
    ResponseEntity<Map<String, String>> deleteFile(String serverAuth, long userId, String filePath);


    /**
     * Loads a file from a user as a resource.
     *
     * @param serverAuth     The authentication token of the master server.
     * @param userId         The user id of the user the file belongs to.
     * @param filePath       The path to the file.
     * @return {@code ResponseEntity<Map<String, String>>}
     */
    ResponseEntity<Resource> loadFile(String serverAuth, long userId, String filePath);
}
