package de.storagesystem.api.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import de.storagesystem.api.exceptions.InvalidTokenException;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * @author Simon Brebeck
 */
public interface Authentication {

    /**
     * Extracts the token from the bearer string.
     *
     * @param bearer The bearer string.
     * @throws InvalidTokenException if the token is invalid
     * @return String The token.
     */
    String extractTokenFromBearer(String bearer) throws InvalidTokenException;

    /**
     * Creates, signs and returns a new token with a given payload.
     * @param payload The payload to add to the token.
     * @return String The token.
     */
    String createToken(Map<String, ?> payload);

    /**
     * Verify a given token.
     *
     * @param token The token to verify
     * @return Object an object containing the payload and other important information
     * if token is valid, null if token is invalid
     * @throws IOException if key file is not found
     * @throws NoSuchAlgorithmException if the algorithm is not supported
     * @throws InvalidKeySpecException if the key is invalid
     * @throws JWTVerificationException if token is invalid
     */
    Object verifyToken(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;


    /**
     * Loads the public key from the given path.
     * @param keyPath the path to the key file
     * @return PublicKey the key from the file in keyPath
     * @throws IOException if the key file could not be read
     * @throws NoSuchAlgorithmException if the algorithm is not supported
     * @throws InvalidKeySpecException if the key specification in the file in keyPath is invalid
     */
    PublicKey getPublicKey(String keyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * Loads the private key from the given path.
     * @param keyPath the path to the key file
     * @return PrivateKey the key from the file in keyPath
     * @throws IOException if the key file could not be read
     * @throws NoSuchAlgorithmException if the algorithm is not supported
     * @throws InvalidKeySpecException if the key specification in the file in keyPath is invalid
     */
    PrivateKey getPrivateKey(String keyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * Generates a key pair and saves it to the given path.
     *
     * @param publicKeyPath  The path to save the key public key to.
     * @param privateKeyPath The path to save the key private key to.
     * @throws NoSuchAlgorithmException If the algorithm is not supported.
     * @throws IOException If the key pair could not be saved.
     */
    void createKeys(String publicKeyPath, String privateKeyPath) throws NoSuchAlgorithmException, IOException;

    /**
     * Stores a given key in a file located at the given path.
     * @param path The path where the key should be stored.
     * @param key The key to be stored.
     * @throws IOException If the key could not be stored.
     */
    void storeKeyInFile(String path, Key key) throws IOException;

}
