package de.shurablack;

import com.sun.net.httpserver.HttpServer;
import de.shurablack.http.handler.*;
import de.shurablack.interpreter.ArgInterpreter;
import de.shurablack.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {

    private static final Logger LOGGER = LogManager.getLogger(Server.class);

    private HttpServer http;

    public Server(String[] args) {
        Config.init();
        ArgInterpreter.interpret(args);

        try {
            if (Config.isVerbose()) {
                LOGGER.info("Prepare server for port {}", Config.getPort());
            }

            this.http = HttpServer.create(new InetSocketAddress(Config.getIpAddress(), Config.getPort()), 2);
            this.http.setExecutor(Executors.newFixedThreadPool(Config.getThreadPoolSize()));

            if (Config.isVerbose()) {
                LOGGER.info("Create default contexts");
            }
            this.http.createContext("/", new PageHandler());
            this.http.createContext("/session", new SessionHandler());
            this.http.createContext("/media", new MediaHandler());
            this.http.createContext("/download", new DownloadHandler());
            this.http.createContext("/delete", new DeleteHandler());
            this.http.createContext("/dir", new DirectoryHandler());
            this.http.createContext("/driver", new DriverHandler());
            if (Config.isUploadAllowed()) {
                this.http.createContext("/upload", new UploadHandler());
            }

        } catch (IOException e) {
            LOGGER.error("Failed to create server: {}", e.getMessage());
        }
    }

    public void start() {
        if (this.http == null) {
            LOGGER.error("Server not created");
            return;
        }

        LOGGER.info("Starting server...");
        System.out.println(
                "==================================================================================\n"
              + "= JFileShare - Version 0.1.0                                                     =\n"
                        + "= Written by ShuraBlack                                                          =\n"
                        + "==================================================================================\n"
                        + Config.getProperties() + "\n"
                        + "==================================================================================\n"
                        + "= Ctrl+C -> will close the server                                                =\n"
                        + "==================================================================================\n");
        this.http.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Stopping server...");
            this.http.stop(0);
        }));
    }
}
