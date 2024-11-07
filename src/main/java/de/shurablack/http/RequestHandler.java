package de.shurablack.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.shurablack.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RequestHandler implements HttpHandler {

    private static final Logger LOGGER = LogManager.getLogger(RequestHandler.class);

    @Override
    public void handle(HttpExchange exchange) {
        Request request = new Request(exchange);

        switch (request.getMethod()) {
            case GET: get(request);             break;
            case POST: post(request);           break;
            case PUT: put(request);             break;
            case DELETE: delete(request);       break;
            case HEAD: head(request);           break;
            case UNSUPPORTED:
                logInvalidRequest(request);
                request.sendEmptyResponse(405);
                break;
        }
    }

    public void get(Request request) {
        logInvalidRequest(request);
        request.sendEmptyResponse(405);
    }

    public void post(Request request) {
        logInvalidRequest(request);
        request.sendEmptyResponse(405);
    }

    public void put(Request request) {
        logInvalidRequest(request);
        request.sendEmptyResponse(405);
    }

    public void delete(Request request) {
        logInvalidRequest(request);
        request.sendEmptyResponse(405);
    }

    public void head(Request request) {
        logInvalidRequest(request);
        request.sendEmptyResponse(405);
    }

    public void logError(Request request, String message, int code) {
        if (Config.isVerbose()) {
            LOGGER.error("<IP:{}> {} Invalid Request - {}",
                    request.getOrigin().getRemoteAddress().getAddress(),
                    request.getMethod(), message
            );
        }
        request.sendEmptyResponse(code);
    }

    protected void logInvalidRequest(Request request) {
        if (Config.isVerbose()) {
            LOGGER.info("<IP:{}> {} Invalid request to {}",
                    request.getOrigin().getRemoteAddress().getAddress(),
                    request.getMethod(), request.getOrigin().getRequestURI()
            );
        }
    }
}
