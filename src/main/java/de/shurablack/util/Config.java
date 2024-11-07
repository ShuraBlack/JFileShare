package de.shurablack.util;

import java.util.Properties;

/**
 * This class is used to store the configuration of the server.
 * It also contains the default values.
 *
 */
public class Config {

    /**
     * Configuration of the server.
     */
    private static final Properties APP_CONFIG = new Properties(8);

    /**
     * Private constructor to prevent instantiation.
     */
    private Config() { }

    /**
     * Initializes the configuration.
     */
    public static void init() {
        APP_CONFIG.put("IP_ADDRESS", "0.0.0.0");
        APP_CONFIG.put("PORT", "80");
        APP_CONFIG.put("THREAD_POOL_SIZE", "2");
        APP_CONFIG.put("ROOT_RESTRICTED", "true");
        APP_CONFIG.put("VERBOSE", "false");
        APP_CONFIG.put("ROOT_DIRECTORY", System.getProperty("user.dir"));
        APP_CONFIG.put("UPLOAD_ALLOWED", "false");
        APP_CONFIG.put("SHOW_HIDDEN_FILES", "false");
    }

    /**
     * Returns the properties of the server.
     * @return The properties
     */
    public static String getProperties() {
        return String.format("= Verbose: %-27s IP: %-59s =%n"
                        + "= Root Restriction: %-18s Port: %-57s =%n"
                        + "= Upload Allowed: %-20s Thread Pool Size: %-45s =%n"
                        + "= Show Hidden Files: %-17s Root: %-57s =",
                colorize(Config.isVerbose()),
                "\033[0;36m" + Config.getIpAddress() + "\033[0m",
                colorize(Config.isRootRestricted()),
                "\033[0;36m" + Config.getPort() + "\033[0m",
                colorize(Config.isUploadAllowed()),
                "\033[0;36m" + Config.getThreadPoolSize() + "\033[0m",
                colorize(Config.isShowHiddenFiles()),
                "\033[0;36m" + Config.getRootDirectory() + "\033[0m"
                );
    }

    /**
     * Colorizes the given boolean.
     * @param state The boolean to colorize.
     * @return The colorized boolean.
     */
    private static String colorize(boolean state) {
        if (state) {
            return "\033[0;32m" + state + "\033[0m";
        } else {
            return "\033[0;31m" + state + "\033[0m";
        }
    }

    /**
     * Sets the value for the given key.
     * @param key The key.
     * @param value The value.
     */
    public static void set(String key, String value) {
        if (APP_CONFIG.containsKey(key)) {
            APP_CONFIG.replace(key, value);
        } else {
            APP_CONFIG.put(key, value);
        }
    }

    /**
     * Get the IP address of the server.
     * @return The IP address.
     */
    public static String getIpAddress() {
        return APP_CONFIG.getProperty("IP_ADDRESS");
    }

    /**
     * Get the port of the server.
     * @return The port.
     */
    public static int getPort() {
        return Integer.parseInt(APP_CONFIG.getProperty("PORT"));
    }

    /**
     * Get the thread pool size of the server.
     * @return The thread pool size.
     */
    public static int getThreadPoolSize() {
        return Integer.parseInt(APP_CONFIG.getProperty("THREAD_POOL_SIZE"));
    }

    /**
     * Get the root restriction of the server.
     * @return The root restriction.
     */
    public static boolean isRootRestricted() {
        return Boolean.parseBoolean(APP_CONFIG.getProperty("ROOT_RESTRICTED"));
    }

    /**
     * Get the verbose mode of the server.
     * @return The verbose mode.
     */
    public static boolean isVerbose() {
        return Boolean.parseBoolean(APP_CONFIG.getProperty("VERBOSE"));
    }

    /**
     * Get the root directory of the server.
     * @return The root directory.
     */
    public static String getRootDirectory() {
        return APP_CONFIG.getProperty("ROOT_DIRECTORY");
    }

    /**
     * Get the upload mode of the server.
     * @return The upload mode.
     */
    public static boolean isUploadAllowed() {
        return Boolean.parseBoolean(APP_CONFIG.getProperty("UPLOAD_ALLOWED"));
    }

    /**
     * Get the show hidden files mode of the server.
     * @return The show hidden files mode.
     */
    public static boolean isShowHiddenFiles() {
        return Boolean.parseBoolean(APP_CONFIG.getProperty("SHOW_HIDDEN_FILES"));
    }
}