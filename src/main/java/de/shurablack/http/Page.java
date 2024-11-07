package de.shurablack.http;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Page {

    private static final Logger LOGGER = LogManager.getLogger(Page.class);

    private byte[] pageBytes;

    public Page(final String path, boolean resources) {
        try {
            if (resources) {
                final InputStream inputStream = Page.class.getClassLoader().getResourceAsStream(path);
                if (inputStream == null) {
                    LOGGER.error("Failed to load resource: {}", path);
                    pageBytes = new byte[0];
                    return;
                }
                this.pageBytes = inputStream.readAllBytes();
            } else {
                this.pageBytes = Files.readAllBytes(Path.of(path));
            }
            if (this.pageBytes.length == 0) {
                LOGGER.error("Empty page loaded: {}", path);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to read file: {}", e.getMessage());
            pageBytes = new byte[0];
        }
    }

    public byte[] getBytes() {
        return pageBytes;
    }
}
