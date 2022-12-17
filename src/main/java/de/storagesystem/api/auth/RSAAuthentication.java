package de.storagesystem.api.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.storagesystem.api.exceptions.InvalidTokenException;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * @author Simon Brebeck
 */
public class RSAAuthentication implements Authentication {

    /**
     * An {@link RSAPublicKey} instance that is used to verify the signature of the JWT.
     */
    private final RSAPublicKey publicKey;
    /**
     * An {@link RSAPrivateKey} used to sign the JWTs.
     */
    private final RSAPrivateKey privateKey;

    /**
     * The issuer of the JWT.
     */
    private final String issuer;

    /**
     * Creates a new Authentication object with publicKeyPath and
     * privateKeyPath as paths to the private/public encryption key.
     *
     * @param token_issuer The issuer of the JWT.
     * @param publicKeyPath path to the public key
     * @param privateKeyPath path to the private key
     * @throws IOException if the public or private key file could not be read from the  key path stored in .env
     * @throws NoSuchAlgorithmException if the RSA algorithm is not supported by the system
     * @throws InvalidKeySpecException if the public or private key file is not a valid RSA key
     */
    public RSAAuthentication(String token_issuer, String publicKeyPath, String privateKeyPath)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        this.issuer = token_issuer;
        this.publicKey = getPublicKey(publicKeyPath);
        this.privateKey = getPrivateKey(privateKeyPath);
    }

    /**
     * Creates a new Authentication object with publicKey and
     * privateKey as the private/public encryption key.
     *
     * @param publicKey public key
     * @param privateKey private key
     */
    public RSAAuthentication(String token_issuer, RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.issuer = token_issuer;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String extractTokenFromBearer(String bearer) throws InvalidTokenException {
        if(bearer == null) throw new InvalidTokenException("Bearer is null");
        if(!bearer.startsWith("Bearer ")) throw new InvalidTokenException("Token is not a bearer token");

        return bearer.substring(7);
    }

    /**
     * {@inheritDoc}
     *
     * Implementation of {@link Authentication#verifyToken(String)} using JSON Web Tokens and the
     * RSA256 algorithm.
     * @return String - RSA256 JSON Web Token
     */
    @Override
    public String createToken(Map<String, ?> payload) {
        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
        return JWT.create()
                .withIssuer(issuer)
                .withPayload(payload)
                .sign(algorithm);
    }
     /**
     * {@inheritDoc}
     *
     * @return DecodedJWT if token is valid, null if token is invalid
     */
    @Override
    public DecodedJWT verifyToken(String token) throws
            JWTVerificationException {
        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
        return JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public RSAPublicKey getPublicKey(String keyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec ks =  new X509EncodedKeySpec(readKeyBytes(keyPath));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(ks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RSAPrivateKey getPrivateKey(String keyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec ks =  new PKCS8EncodedKeySpec(readKeyBytes(keyPath));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(ks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createKeys(String publicKeyPath, String privateKeyPath) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        storeKeyInFile(publicKeyPath, kp.getPublic());
        storeKeyInFile(privateKeyPath, kp.getPrivate());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void storeKeyInFile(String path, Key key) throws IOException {
        File file = new File(path);
        if(!file.getParentFile().isDirectory() && !file.mkdirs()) {
            throw new IOException("Could not create directory for key file");
        }

        FileOutputStream out = new FileOutputStream(path);
        out.write(key.getEncoded());
        out.close();
    }

    /**
     * Returns the bytes of a given key file
     * @param keyPath the path to the key file
     * @throws IOException if the key file could not be read
     * @return byte[] the bytes of the key file
     */
    private byte[] readKeyBytes(String keyPath) throws IOException {
        Path path = Paths.get(keyPath);
        return Files.readAllBytes(path);
    }

    public String issuer() {
        return issuer;
    }
}
