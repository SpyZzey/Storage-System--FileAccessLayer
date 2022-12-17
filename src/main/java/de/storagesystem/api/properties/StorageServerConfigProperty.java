package de.storagesystem.api.properties;

import org.apache.catalina.Server;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author Simon Brebeck on 24.11.2022
 */
@Validated
@ConfigurationProperties("storagesystem")
public class StorageServerConfigProperty {
    @NotNull
    private ServerProperty server;

    @NotNull
    private StorageProperty storage;

    public StorageProperty storage() {
        return storage;
    }

    public ServerProperty server() {
        return server;
    }

    public void setServer(ServerProperty server) {
        this.server = server;
    }

    public void setStorage(StorageProperty storage) {
        this.storage = storage;
    }
}
