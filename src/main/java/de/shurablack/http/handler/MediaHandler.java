package de.shurablack.http.handler;

import de.shurablack.file.MediaEntry;
import de.shurablack.http.Request;
import de.shurablack.http.RequestHandler;
import de.shurablack.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class MediaHandler extends RequestHandler {

    private static final Logger LOGGER = LogManager.getLogger(MediaHandler.class);

    private final Map<String, MediaEntry> mediaMap = Map.of(
            "/media/file.png", new MediaEntry("media/file.png", "image/png"),
            "/media/directory.png", new MediaEntry("media/directory.png", "image/png"),
            "/media/directory-open.png", new MediaEntry("media/directory-open.png", "image/png"),
            "/media/back.png", new MediaEntry("media/back.png", "image/png"),
            "/media/download.png", new MediaEntry("media/download.png", "image/png"),
            "/media/upload.png", new MediaEntry("media/upload.png", "image/png"),
            "/media/drive.png", new MediaEntry("media/drive.png", "image/png"),
            "/media/delete.png", new MediaEntry("media/delete.png", "image/png")
    );

    @Override
    public void get(Request request) {
        String path = request.getPath();
        MediaEntry mediaEntry = mediaMap.get(path);
        if (mediaEntry == null) {
            if (Config.isVerbose()) {
                LOGGER.error("<IP:{}> {} Request media not found: {}",
                        request.getOrigin().getRemoteAddress().getAddress(),
                        request.getMethod(),
                        path
                );
            }
            request.sendEmptyResponse(404);
            return;
        }

        if (Config.isVerbose()) {
            LOGGER.debug("<IP:{}> {} Request media: {}",
                    request.getOrigin().getRemoteAddress().getAddress(),
                    request.getMethod(),
                    path
            );
        }

        request.sendMediaWithClose(200, mediaEntry.readData(), mediaEntry.getType());
    }
}
