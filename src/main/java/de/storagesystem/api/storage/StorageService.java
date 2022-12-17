package de.storagesystem.api.storage;

import de.storagesystem.api.exceptions.StorageEntityCreationException;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Random;

/**
 * @author Simon Brebeck
 */
public class StorageService {
    /**
     * The {@link Logger} for this class
     */
    private static final Logger logger = LogManager.getLogger(StorageService.class);

    /**
     * The {@link Path} to the root directory of the storage
     */
    private static Path ROOT;

    /**
     * The max amount of subpartitions of users in a user-partition
     */
    private static final int maxSubpartitionsPerPartition = 2;

    /**
     * The max amount of user storage roots in a subpartition
     */
    private static final int maxUserRootsPerSubpartition = 2;

    /**
     * The max amount of folders in a folder
     */
    private static final int maxNumberOfPartitions = 1000;
    /**
     * The max amount of folders in a subfolder
     */
    private static final int maxNumberOfFilesInPartition = 10000;

    /**
     * The prefix of the server
     */
    private static String serverPrefix;

    /**
     * The default constructor of the {@link StorageService}
     */
    public StorageService() {

    }


    /**
     * Initializes the storage service
     */
    public void init() {
        Dotenv dotenv = Dotenv.load();
        // Load the storage root path from the .env file
        ROOT = Path.of(dotenv.get("STORAGE_ROOT"));
        // Load server prefix from the .env file
        serverPrefix = dotenv.get("SERVER_PREFIX");

        // Creates server storage folder if it does not exist
        File file = new File(root());
        if (file.exists()) return;

        if (file.mkdirs()) {
            logger.info("Created upload folder");
        } else {
            logger.error("Could not create upload folder");
            throw new StorageEntityCreationException("Could not create upload folder");
        }
    }

    /**
     * Get the path of a folder by its path string
     * @param pathString The path to search for
     * @return The path of the folder
     */
    @SuppressWarnings("unused")
    protected Path getFolderPathByString(String pathString) {
        Path path = Path.of(File.separator);
        if(pathString != null && !pathString.isEmpty()) {
            path = path.resolve(pathString);
        }
        return path;
    }

    /**
     * Generate a path (relative to ROOT) where a file can be stored for a user
     * @param userId The id of the user
     * @return The path where the file can be stored
     */
    public String getFileStoragePath(long userId) {
        Random random = new Random(System.currentTimeMillis());
        int partition = random.nextInt(maxNumberOfPartitions);
        int subpartition = random.nextInt(maxNumberOfPartitions);
        String path = getUserStoragePath(userId) + "/" + partition + "/" + subpartition;
        return createPartition(path);
    }


    /**
     * Generate the path (relative to ROOT) to the user root directory
     * @param userId The id of the user
     * @return The path to the user root directory
     */
    public String getUserStoragePath(long userId) {
        int partition = (int) (userId / (maxUserRootsPerSubpartition * maxSubpartitionsPerPartition)) + 1;
        int subpartition = (int) (userId / maxUserRootsPerSubpartition) + 1;
        String path = "/p" + partition + "/sub" + subpartition + "/u" + userId;
        return createPartition(path);
    }

    /**
     * Create a folder and all its parents if it does not exist
     * @param partitionPath The path to the folder to create
     * @return The path to the folder
     */
    public String createPartition(String partitionPath) {
        File file = new File(root() + partitionPath);
        if(!file.isDirectory() && !file.mkdirs()) {
            throw new StorageEntityCreationException("Could not create partition " + partitionPath);
        }
        return partitionPath;
    }

    /**
     * Get the root path of the storage
     * @return The root path of the storage
     */
    public String root() {
        return ROOT.toString();
    }

    /**
     * Get the server prefix
     * @return The server prefix
     */
    @SuppressWarnings("unused")
    public static String serverPrefix() {
        return serverPrefix;
    }
}
