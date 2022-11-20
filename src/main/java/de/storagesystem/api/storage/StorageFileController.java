package de.storagesystem.api.storage;

import de.storagesystem.api.exceptions.UserInputValidationException;
import de.storagesystem.api.exceptions.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
/**
 * @author Simon Brebeck
 */
@Controller
@RequestMapping("/api/files/")
public class StorageFileController {

    /**
     * The {@link Logger} for this class
     */
    private static final Logger logger = LogManager.getLogger(StorageFileController.class);

    /**
     * The {@link StorageFileService} to access the storage files
     */
    private final StorageFileService storageService;


    /**
     * Instantiates a new Storage file controller.
     * @param storageService the storage service to access the storage files
     */
    @Autowired
    public StorageFileController(StorageFileService storageService) {
        this.storageService = storageService;
    }

    /**
     * Gets the file from a folder inside a bucket with a given name for a user
     *
     * @param serverAuthentication the authentication token of the master server.
     * @param userId the id of the user who owns the file
     * @param path the path of the file
     * @return the file as a {@code  ResponseEntity<Resource>}
     * @throws UserInputValidationException if the bucket, folder or file name is invalid
     */
    @GetMapping("/{userId}/{path}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String serverAuthentication,
            @PathVariable long userId,
            @PathVariable String path)
            throws
            UserInputValidationException {
        StorageInputValidation inputValidation = new StorageInputValidationImpl();
        if(!inputValidation.validateFilePath(path))
            throw new UserInputValidationException("Invalid folder path");
        logger.info("Serving file " + path + " for user " + userId);

        return storageService.loadFile(serverAuthentication, userId, path);
    }

    /**
     * Deletes a file from disk, given the path and the user id
     *
     * @param serverAuthentication the authentication token of the master server.
     * @param userId the id of the user who owns the file
     * @param path the path of the file
     * @return the file as a {@code  ResponseEntity<Resource>}
     * @throws UserInputValidationException if the file path is invalid
     */
    @DeleteMapping("/{userId}/{path}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteFile(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String serverAuthentication,
            @PathVariable long userId,
            @PathVariable String path)
            throws
            UserInputValidationException {
        StorageInputValidation inputValidation = new StorageInputValidationImpl();
        if(!inputValidation.validateFilePath(path))
            throw new UserInputValidationException("Invalid folder path");
        logger.info("Deleting file " + path + " for user " + userId);

        return storageService.deleteFile(serverAuthentication, userId, path);
    }

    /**
     * Uploads a file to a folder inside a bucket for a user
     *
     * @param serverAuthentication the authentication token of the master server.
     * @param userId the id of the user who owns the file
     * @param file the file to upload.
     * @return the response as a {@code Map<String, String>}
     * @throws MaxUploadSizeExceededException if the file is too big.
     * @throws UserNotFoundException if the user does not exist.
     */
    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, String>> handleFileUploadByName(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String serverAuthentication,
            @PathVariable long userId,
            @RequestParam("file") MultipartFile file)
            throws
            MaxUploadSizeExceededException {
        logger.info("Uploading file " + file.getOriginalFilename() + " for user " + userId);
        return storageService.storeFile(serverAuthentication, userId, file);
    }
}
