package de.shurablack.http.handler;

import de.shurablack.http.Page;
import de.shurablack.http.Request;
import de.shurablack.http.RequestHandler;
import de.shurablack.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PageHandler extends RequestHandler {
    
    private static final Logger LOGGER = LogManager.getLogger(PageHandler.class);

    private final Page page = new Page("html/index.html", true);

    @Override
    public void get(Request request) {
        if (Config.isVerbose()) {
            LOGGER.info("<IP:{}> {} Request of index.html",
                    request.getOrigin().getRemoteAddress().getAddress(),
                    request.getMethod()
            );
        }

        request.sendPageWithClose(200, page);
    }
}
