package de.storagesystem.api;

import de.storagesystem.api.auth.Authentication;
import de.storagesystem.api.auth.RSAAuthentication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * @author Simon Brebeck
 */
@SpringBootApplication
public class StorageSystemApplication  {

    /**
     * The {@link Logger} for this class
     */
    private static final Logger logger = LogManager.getLogger(StorageSystemApplication.class);

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
     * Exit event
     */
    @PreDestroy
    public void onExit() {
        logger.info("Stopping StorageSystem API Test");

    }

}
