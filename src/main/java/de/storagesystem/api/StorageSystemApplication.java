package de.storagesystem.api;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import de.storagesystem.api.properties.ServerProperty;
import de.storagesystem.api.properties.StorageServerConfigProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Simon Brebeck
 */
@SpringBootApplication
@EnableEncryptableProperties
@ConfigurationPropertiesScan("de.storagesystem.api.properties")
public class StorageSystemApplication  {

    private Environment env;

    /**
     * The {@link Logger} for this class
     */
    private static final Logger logger = LogManager.getLogger(StorageSystemApplication.class);

    private final StorageServerConfigProperty storageSystemConfigProperty;

    @Autowired
    public StorageSystemApplication(
            StorageServerConfigProperty storageSystemConfigProperty,
            Environment env) {
        this.storageSystemConfigProperty = storageSystemConfigProperty;
        this.env = env;
    }

    /**
     * The main method to start the application.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");

        logger.info("Starting server");
        SpringApplication.run(StorageSystemApplication.class, args);
    }


    /**
     * Start event
     */
    @PostConstruct
    public void onStart() {
        logger.info("Starting StorageSystem API Test");
        logger.info("StorageSystem API Test is running on " +
                storageSystemConfigProperty.server().protocol() + "://" +
                env.getProperty("server.address") + ":" +
                env.getProperty("server.port") + "/" +
                " as " + storageSystemConfigProperty.server().prefix() +
                 storageSystemConfigProperty.server().name());
    }

    /**
     * Exit event
     */
    @PreDestroy
    public void onExit() {
        logger.info("Stopping StorageSystem API Test");
    }

}
