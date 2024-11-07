package de.shurablack.file;

import de.shurablack.http.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class MediaEntry {

    private static final Logger LOGGER = LogManager.getLogger(MediaEntry.class);

    private final String path;
    private final String type;

    public MediaEntry(String path, String type) {
        this.path = path;
        this.type = type;
    }

    public byte[] readData() {
        try {
            final InputStream inputStream = Page.class.getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                LOGGER.error("Failed to load resource: {}", path);
                return new byte[0];
            }
            return inputStream.readAllBytes();
        } catch (IOException e) {
            LOGGER.error("Failed to read file: {}", e.getMessage());
            return new byte[0];
        }
    }

    public String getType() {
        return type;
    }
}
