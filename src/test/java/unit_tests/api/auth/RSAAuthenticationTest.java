package unit_tests.api.auth;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.storagesystem.api.auth.RSAAuthentication;
import de.storagesystem.api.exceptions.InvalidTokenException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RSAAuthenticationTest {

    /**
     * Create private and public key for testing
     *
     * @throws IOException if an I/O exception occurred
     * @throws NoSuchAlgorithmException if the key generation algorithm is not supported
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    @Test
    public void createAndReadRSAKeys() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyPath = "./src/test/keys/test_key.pub";
        String privateKeyPath = "./src/test/keys/test_key.key";
        RSAAuthentication auth = new RSAAuthentication();

        RSAPublicKey publicKey = auth.getPublicKey(publicKeyPath);
        RSAPrivateKey privateKey = auth.getPrivateKey(privateKeyPath);

        assertDoesNotThrow(() -> auth.createKeys(publicKeyPath, privateKeyPath));
        assertNotNull(publicKey);
        assertNotNull(privateKey);
    }


    /**
     * Create a token and verify it
     * @throws InvalidTokenException if the token is invalid
     * @throws NoSuchAlgorithmException if the key generation algorithm is not supported
     * @throws InvalidKeySpecException if the key specification is invalid
     * @throws IOException if an I/O exception occurred
     */
    @Test
    public void createTokenAndExtractContentTest() throws InvalidTokenException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        RSAAuthentication auth = new RSAAuthentication();

        Map<String, String> payload = Map.of("KeyA", "ValueA", "KeyB", "ValueB");
        String token = auth.createToken(payload);
        String bearer = "Bearer " + token;
        DecodedJWT content = auth.verifyToken(token);
        Map<String, Claim> decodedPayload = content.getClaims();

        assertEquals(token, auth.extractTokenFromBearer(bearer));
        for(String key : payload.keySet()) {
            assertEquals(payload.get(key), decodedPayload.get(key).asString());
        }
    }
}
