package de.storagesystem.api.storage;

import com.auth0.jwt.interfaces.DecodedJWT;
import de.storagesystem.api.auth.Authentication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * @author Simon Brebeck
 */
@Service
public class StorageFileServiceImpl extends StorageService implements StorageFileService {

    /**
     * The {@link Logger} for this class
     */
    private static final Logger logger = LogManager.getLogger(StorageFileServiceImpl.class);

    /**
     * The {@link Authentication} to create and verify the JWT Token
     */
    private final Authentication auth;


    /**
     * Instantiates a new Storage file service.
     *
     * @param auth instance of the {@link Authentication} to create and verify the JWT Token
     */
    @Autowired
    public StorageFileServiceImpl(Authentication auth) {
        this.auth = auth;
    }

    /**
     * Initializes the {@link StorageFileService}
     */
    @PostConstruct
    public void init() {
        super.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Map<String, String>> storeFile(String serverAuth, long userId, MultipartFile file) {
        if(!verifyAccess(serverAuth)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        String fileName = file.getOriginalFilename();
        String storedPath = getFileStoragePath(userId) + File.separator + fileName;
        File storedFile = new File(storedPath);
        if(storedFile.exists()) {
            logger.error("File " + file.getOriginalFilename() + " already exists.");
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message","File already exists."));
        }

        try {
            file.transferTo(storedFile);
        } catch (IOException e) {
            logger.error("Could not store file: " + fileName, e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error while storing the file."
            ));
        }

        logger.info("File " + file.getOriginalFilename() + " stored in bucket " + storedPath);
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "path", storedPath,
                "message", "File stored."));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Map<String, String>> deleteFile(String serverAuth, long userId, String filePath) {
        if(!verifyAccess(serverAuth)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        String storedPath = root() + File.separator + filePath;
        File storedFile = new File(storedPath);

        if(!storedFile.exists()) {
            logger.error("File " + filePath + " does not exist.");
            return ResponseEntity.notFound().build();
        }
        boolean deleted = storedFile.delete();
        if(!deleted) {
            logger.error("Could not delete file " + filePath);
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Could not delete file."
            ));
        }
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "message", "File deleted."
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Resource> loadFile(String serverAuth, long userId, String filePath) {
        if(!verifyAccess(serverAuth)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ByteArrayResource file = getStorageFileResource(filePath);
        if(file == null) {
            logger.error("File " + filePath + " not found.");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .contentLength(file.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    private ByteArrayResource getStorageFileResource(String filePath) {
        String storedPath = root() + File.separator + filePath;
        ByteArrayResource resource = null;
        try (FileInputStream fileInputStream = new FileInputStream(storedPath)){
            resource = new ByteArrayResource(fileInputStream.readAllBytes());
        } catch (FileNotFoundException e) {
            logger.error("File " + filePath + " not found.", e);
        } catch (IOException e) {
            logger.error("Could not read file " + filePath, e);
        }
        return resource;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean verifyAccess(String serverAuth) {
        try {
            String token = auth.extractTokenFromBearer(serverAuth);
            DecodedJWT content = (DecodedJWT) auth.verifyToken(token);
            if(content == null
                    || !content.getClaim("server_type").asString().equalsIgnoreCase("storage-master-server")) {
                logger.error("The requester is not a storage master server.");
                return false;
            }
        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            logger.error("Error while verifying the token", e);
            return false;
        }
        return true;
    }


}
