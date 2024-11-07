package de.shurablack.http.handler;

import de.shurablack.http.Request;
import de.shurablack.http.RequestHandler;
import de.shurablack.session.Session;
import de.shurablack.util.Config;
import de.shurablack.util.TimeProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHandler extends RequestHandler {

    private static final Logger LOGGER = LogManager.getLogger(SessionHandler.class);

    private static final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();

    private static Session createSession() {
        final Session session = new Session(Config.getRootDirectory(), new TimeProvider());
        SESSIONS.put(session.getIdentifier(), session);
        return session;
    }

    public static Session getSession(String identifier) {
        final Session session = SESSIONS.get(identifier);
        if (session == null) {
            return null;
        }
        if (session.isExpired()) {
            return null;
        }
        return SESSIONS.get(identifier);
    }

    @Override
    public void get(Request request) {
        final String identifier = request.getHeader("identifier");

        if (identifier != null) {
            final Session session = getSession(identifier);
            if (session == null) {
                request.sendJsonWithClose(200, new JSONObject().put("valid",false));
                return;
            }
            request.sendJsonWithClose(200, new JSONObject().put("valid",true));
            return;
        }

        if (Config.isVerbose()) {
            LOGGER.info("<IP:{}> {} Request for new session",
                    request.getOrigin().getRemoteAddress().getAddress(),
                    request.getMethod()
            );
        }
        final Session session = createSession();

        request.sendJsonWithClose(200, session.toJson());
    }

    @Override
    public void put(Request request) {
        final String identifier = request.getHeader("identifier");

        if (identifier == null) {
            logError(request, "No session identifier", 401);
            return;
        }
        Session session = getSession(identifier);
        if (session == null) {
            session = createSession();
            request.sendJsonWithClose(200, session.toJson());

            if (Config.isVerbose()) {
                LOGGER.info("<IP:{}> {} Request for new session",
                        request.getOrigin().getRemoteAddress().getAddress(),
                        request.getMethod()
                );
            }
            return;
        }
        session.refresh();
        SESSIONS.remove(identifier);
        SESSIONS.replace(session.getIdentifier(), session);

        if (Config.isVerbose()) {
            LOGGER.info("<IP:{}> {} Request for session refresh",
                    request.getOrigin().getRemoteAddress().getAddress(),
                    request.getMethod()
            );
        }

        request.sendJsonWithClose(200, session.toJson());
    }
}
