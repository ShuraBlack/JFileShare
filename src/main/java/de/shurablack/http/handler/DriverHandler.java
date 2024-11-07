package de.shurablack.http.handler;

import de.shurablack.http.Request;
import de.shurablack.http.RequestHandler;
import de.shurablack.http.driver.LocalDriverData;
import de.shurablack.session.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

public class DriverHandler extends RequestHandler {

    private static final Logger LOGGER = LogManager.getLogger(DriverHandler.class);

    private static final List<LocalDriverData> DRIVERS = new ArrayList<>();
    private static long LAST_UPDATE;

    public DriverHandler() {
        super();
        loadDrivers();
    }

    public static void loadDrivers() {
        DRIVERS.clear();

        FileSystem fileSystem = FileSystems.getDefault();
        try {
            for (FileStore store : fileSystem.getFileStores()) {
                DRIVERS.add(new LocalDriverData(
                        store.toString(),
                        store.getTotalSpace(),
                        store.getTotalSpace() - store.getUsableSpace()
                ));
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get file system: {}", e.getMessage());
        }
        LAST_UPDATE = System.currentTimeMillis();
    }

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

        if (DRIVERS.isEmpty()) {
            loadDrivers();
        }

        JSONArray drivers = new JSONArray();
        for (LocalDriverData driver : DRIVERS) {
            drivers.put(driver.toJson());
        }

        request.sendJsonWithClose(200, new JSONObject().put("drivers", drivers).put("lastUpdate", LAST_UPDATE));
    }
}
