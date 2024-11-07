package de.shurablack.http.handler;

import de.shurablack.http.Request;
import de.shurablack.http.RequestHandler;
import de.shurablack.session.Session;
import de.shurablack.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class DownloadHandler extends RequestHandler {

    private static final Logger LOGGER = LogManager.getLogger(DownloadHandler.class);

    @Override
    public void get(Request request) {
        final String identifier = request.getHeader("identifier");
        if (identifier == null) {
            this.logError(request, "No session identifier", 401);
            return;
        }

        final Session session = SessionHandler.getSession(identifier);
        if (session == null) {
            this.logError(request, "Session not found", 401);
            return;
        }

        final String fileQuery = request.getQuery("target");
        final File file = new File(session.getCurrentDirectory() + "/" + fileQuery);

        if (!file.exists()) {
            this.logError(request, "File not found", 404);
            return;
        }

        if (file.isDirectory()) {
            this.logError(request, "File is a directory", 400);
            return;
        }

        if (Config.isVerbose()) {
            LOGGER.info("<IP:{}> {} Request download of {}",
                    request.getOrigin().getRemoteAddress().getAddress(),
                    request.getMethod(),
                    file.getAbsolutePath()
            );
        }

        request.sendFileWithClose(200, file);
    }
}
