package de.shurablack.http.handler;

import de.shurablack.file.DirectoryReader;
import de.shurablack.http.Request;
import de.shurablack.http.RequestHandler;
import de.shurablack.session.Session;
import de.shurablack.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class DirectoryHandler extends RequestHandler {

    private static final Logger LOGGER = LogManager.getLogger(DirectoryHandler.class);

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

        final File file = new File(session.getCurrentDirectory());

        if (Config.isVerbose()) {
            LOGGER.info("<IP:{}> {} Request directory listing of {}",
                    request.getOrigin().getRemoteAddress().getAddress(),
                    request.getMethod(),
                    file.getAbsolutePath()
            );
        }

        final JSONObject response = new JSONObject();
        final JSONArray filesList = DirectoryReader.readDirectory(file);
        response.put("files", filesList);
        final File parent = file.getParentFile();
        response.put("back", parent != null && DirectoryReader.isDirectoryWithin(parent.getAbsolutePath(), Config.getRootDirectory()));
        response.put("workingDirectory", file.getAbsolutePath());

        request.sendJsonWithClose(200, response);
    }

    @Override
    public void post(Request request) {
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

        final String targetQuery = request.getQuery("target");
        File file = new File(session.getCurrentDirectory() + "/" + targetQuery);
        if (targetQuery.equals("$BACK")) {
            file = new File(session.getCurrentDirectory()).getParentFile();
        }

        if (!file.exists()) {
            this.logError(request, "File not found", 404);
            return;
        }

        if (!file.isDirectory()) {
            this.logError(request, "File is not a directory", 400);
            return;
        }

        if (Config.isVerbose()) {
            LOGGER.info("<IP:{}> {} Request directory change to {}",
                    request.getOrigin().getRemoteAddress().getAddress(),
                    request.getMethod(),
                    file.getAbsolutePath()
            );
        }

        if (!DirectoryReader.isDirectoryWithin(file.getAbsolutePath(), Config.getRootDirectory())) {
            this.logError(request, "Directory traversal detected", 403);
            return;
        }
        session.setCurrentDirectory(file.getAbsolutePath());
        get(request);
    }
}
