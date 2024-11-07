package de.shurablack.http;

import com.sun.net.httpserver.HttpExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Request {

    private static final Logger LOGGER = LogManager.getLogger(Request.class);

    private final HttpExchange origin;
    private final Method method;
    private Map<String, String> query;

    public Request(final HttpExchange origin) {
        this.origin = origin;
        this.method = Method.fromString(origin.getRequestMethod());
        parseQuery();
    }

    private void parseQuery() {
        String baseQuery = origin.getRequestURI().getQuery();
        if (baseQuery == null) {
            return;
        }

        this.query = new HashMap<>();
        String[] requestQuery = origin.getRequestURI().getQuery().split("&");
        for (String q : requestQuery) {
            String[] pair = q.split("=");

            if (pair.length == 1) {
                this.query.put(pair[0], "");
            } else {
                this.query.put(pair[0], pair[1]);
            }
        }
    }

    public HttpExchange getOrigin() {
        return origin;
    }

    public Method getMethod() {
        return method;
    }

    public Optional<String> bodyAsString() {
        try {
            return Optional.of(new String(origin.getRequestBody().readAllBytes()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<JSONObject> bodyAsJson() {
        try {
            return Optional.of(new JSONObject(new String(origin.getRequestBody().readAllBytes())));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean queryContains(final String key) {
        return query.containsKey(key);
    }

    public String getQuery(final String key) {
        return query.get(key);
    }

    public boolean hasQuery() {
        return query != null;
    }

    public String getPath() {
        return origin.getRequestURI().getPath();
    }

    public String getHeader(final String key) {
        return origin.getRequestHeaders().getFirst(key);
    }

    public void addResponseHeader(final String key, final String value) {
        origin.getResponseHeaders().add(key, value);
    }

    public void sendJsonWithClose(final int code, final JSONObject data) {
        String dataString = data.toString();
        addResponseHeader("Content-Type", "application/json");
        try {
            origin.sendResponseHeaders(code, dataString.length());
            origin.getResponseBody().write(dataString.getBytes());
            origin.close();
        } catch (Exception e) {
            LOGGER.error("Error while sending response", e);
        }
    }

    public void sendFileWithClose(final int code, final File file) {
        try {
            addResponseHeader("Content-Disposition", "attachment; filename=" + file.getName());
            final String contentType = URLConnection.guessContentTypeFromName(file.getName());
            addResponseHeader("Content-Type", Objects.requireNonNullElse(contentType, "application/octet-stream"));

            final byte[] data = Files.readAllBytes(file.toPath());
            origin.sendResponseHeaders(code, data.length);
            origin.getResponseBody().write(data);
            origin.close();
        } catch (Exception e) {
            LOGGER.error("Error while sending response", e);
        }
    }

    public void sendPageWithClose(final int code, final Page page) {
        byte[] pageBytes = page.getBytes();
        addResponseHeader("Content-Type", "text/html");
        try {
            origin.sendResponseHeaders(code, pageBytes.length);
            origin.getResponseBody().write(pageBytes);
            origin.close();
        } catch (Exception e) {
            LOGGER.error("Error while sending response", e);
        }
    }

    public void sendMediaWithClose(final int code, final byte[] bytes, final String contentType) {
        addResponseHeader("Content-Type", contentType);
        try {
            origin.sendResponseHeaders(code, bytes.length);
            origin.getResponseBody().write(bytes);
            origin.close();
        } catch (Exception e) {
            LOGGER.error("Error while sending response", e);
        }
    }

    public void sendEmptyResponse(final int code) {
        try {
            origin.sendResponseHeaders(code, -1);
            origin.close();
        } catch (Exception e) {
            LOGGER.error("Error while sending response", e);
        }

    }
}
