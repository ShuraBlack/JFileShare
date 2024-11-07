package de.shurablack.session;

import de.shurablack.util.Config;
import de.shurablack.util.TimeProvider;
import org.json.JSONObject;

import java.util.UUID;

public class Session {

    private final TimeProvider timeProvider;

    private String identifier;

    private String currentDirectory;

    private long expirationTime;

    public Session(String currentDirectory, TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        this.identifier = UUID.randomUUID().toString();
        this.currentDirectory = currentDirectory;
        this.expirationTime = timeProvider.currentTimeMillis() + 1800000L;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setCurrentDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
    }

    public boolean isExpired() {
        return timeProvider.currentTimeMillis() > expirationTime;
    }

    public void refresh() {
        this.expirationTime = timeProvider.currentTimeMillis() + 1800000L;
        this.identifier = UUID.randomUUID().toString();
    }

    public JSONObject toJson() {
        final JSONObject json = new JSONObject();
        json.put("identifier", identifier);
        json.put("expirationTime", expirationTime);
        json.put("uploadAllowed", Config.isUploadAllowed());
        return json;
    }
}
