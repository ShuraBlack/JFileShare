package de.shurablack.util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A utility class for formatting data.
 */
public class Formatter {
    /**
     * The time format for the last modified date.
     */
    private static final Format TIME_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    /**
     * Private constructor to prevent instantiation.
     */
    private Formatter() { }

    /**
     * Converts the given size in bytes to a human-readable string.
     * @param bytes The size in bytes.
     * @return The human-readable string.
     */
    public static String formatSize(long bytes) {
        if (bytes < 0) {
            return "0 B";
        }
        if (bytes < 1024) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    /**
     * Converts the given time in milliseconds to a human-readable string.
     * @param time The time in milliseconds.
     * @return The human-readable string.
     */
    public static String formatTime(long time){
        Date date = new Date(time);
        return TIME_FORMAT.format(date);
    }
}
