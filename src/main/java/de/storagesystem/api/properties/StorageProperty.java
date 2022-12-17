package de.storagesystem.api.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author Simon Brebeck on 24.11.2022
 */
@Validated
@Component
public class StorageProperty {
    @NotBlank
    private String path;

    @NotBlank
    private String issuer;

    @NotBlank
    private String privateKey;

    @NotBlank
    private String publicKey;

    public String privateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String publicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String issuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String path() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
