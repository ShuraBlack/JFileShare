package de.shurablack.interpreter;

import de.shurablack.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.*;
import java.util.*;

/**
 * Interpreter for the command line arguments.
 * <br><br>
 * This class will interpret the command line arguments and set the corresponding values.
 */
public class ArgInterpreter {

    /**
     * Logger for the ArgsInterpreter class.
     */
    private static final Logger LOGGER = LogManager.getLogger(ArgInterpreter.class);

    /**
     * Private constructor to prevent instantiation.
     */
    private ArgInterpreter() {
    }

    /**
     * Interprets the command line arguments.
     * @param args Command line arguments.
     */
    public static void interpret(String[] args) {
        Map<String, String> params = new HashMap<>();

        for (String arg : args) {
            String[] split = arg.split("=");
            if (params.containsKey(split[0])) {
                continue;
            }
            if (split.length == 1) {
                params.put(split[0], null);
            } else {
                params.put(split[0], split[1]);
            }
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            switch (entry.getKey()) {
                case "-ip":         interpretIP(entry.getValue());                                  break;
                case "-port":
                case "-p":          interpretPort(entry.getValue());                                break;
                case "-verbose":
                case "-v":          interpretVerbose();                                             break;
                case "-noroot":
                case "-n":          interpretRootRestriction();                                     break;
                case "-threads":
                case "-t":          interpretThreads(entry.getValue());                             break;
                case "-root":
                case "-r":          interpretRoot(entry.getValue());                                break;
                case "-upload":
                case "-u":          interpretUpload();                                              break;
                case "-hidden":
                case "-h":          interpretHidden();                                              break;
                case "-help":       interpretHelp();                                                break;
                default:            LOGGER.warn("Unknown Argument {}!", entry.getKey());    break;
            }
        }
    }

    /**
     * Prints out all available network interfaces or sets the IP Address to the given network name.
     * @param value The network name or null.
     */
    private static void interpretIP(String value) {
        try {
            if (value == null) {
                Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
                StringBuilder builder = new StringBuilder();
                builder.append("\nNetwork Interfaces:\n(Search for the the local network name you use on the devices)\n");
                for (NetworkInterface netint : Collections.list(nets))
                    displayInterfaceInformation(builder, netint);

                LOGGER.info(builder.toString());
                System.exit(0);
            } else {
                NetworkInterface netint = NetworkInterface.getByName(value);
                if (netint == null) {
                    LOGGER.warn("Network Interface not found! Use default IP Address 0.0.0.0");
                } else {
                    Optional<InetAddress> inetAddress = Collections.list(netint.getInetAddresses())
                            .stream()
                            .filter(Inet4Address.class::isInstance)
                            .findFirst();
                    if (inetAddress.isPresent()) {
                        Config.set("IP_ADDRESS", inetAddress.get().getHostAddress());
                        LOGGER.info("Set IP Address to {}", Config.getIpAddress());
                    } else {
                        LOGGER.warn("No IPv4 Address found! Use default IP Address");
                    }
                }
            }
        } catch (SocketException e) {
            LOGGER.error("Couldnt get Network Interfaces!", e);
        }
    }

    /**
     * Sets the port to the given value.
     * @param value The port or null.
     */
    private static void interpretPort(String value) {
        if (value == null || value.isEmpty()) {
            LOGGER.warn("Port not found! Use default port 80.");
            return;
        }

        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warn( "Port is not a number! Use default port 80.", e);
            return;
        }

        Config.set("PORT", value);
        LOGGER.info("Set Port to {}", Config.getPort());
    }

    /**
     * Sets the verbose mode to the given value.
     */
    private static void interpretVerbose() {
        Config.set("VERBOSE", "true");
        LOGGER.info("Verbose got enabled");
    }

    /**
     * Sets the root limit to the given value.
     */
    private static void interpretRootRestriction() {
        Config.set("ROOT_RESTRICTED", "false");
        LOGGER.info("Root folder restriction got disabled");
    }

    /**
     * Sets the thread pool size to the given value.
     * @param value The size or null.
     */
    private static void interpretThreads(String value) {
        if (value == null || value.isEmpty()) {
            LOGGER.warn("Thread Pool Size not found! Use default thread pool size 3.");
            return;
        }
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warn("Thread Pool Size is not a number! Use default thread pool size 3.", e);
            return;
        }
        Config.set("THREAD_POOL_SIZE", value);
        LOGGER.info("Set Thread Pool Size to {}", Config.getThreadPoolSize());
    }

    /**
     * Sets the root to the given value.
     * @param value The path or null.
     */
    private static void interpretRoot(String value) {
        if (value == null) {
            LOGGER.warn("Root not found! Use default root.");
            return;
        }
        File file = new File(value);
        if (file.isFile()) {
            LOGGER.warn("selected a file, not a directory! Use default root.");
        }
        Config.set("ROOT_DIRECTORY", value.replaceAll("\\\\", "/"));
        LOGGER.info("Set Root to {}", Config.getRootDirectory());
    }

    private static void interpretUpload() {
        Config.set("UPLOAD_ALLOWED", "true");
        LOGGER.info("Upload got enabled");
    }

    private static void interpretHidden() {
        Config.set("SHOW_HIDDEN_FILES", "true");
        LOGGER.info("Show hidden files got enabled");
    }

    /**
     * Prints out the help.
     */
    private static void interpretHelp() {
        LOGGER.info("\nJFileShare Server 0.1.0\n\n" +
                "USAGE:\n\tjava -jar JFileShare.jar [options/flags]\n\n" +
                "\t-help\t\t\t\t\t\tShows this help\n" +
                "\t-ip\t\t\t\t\t\t\tShows all Network Interfaces\n\n" +
                "FLAGS:\n" +
                "\t-v, -verbose\t\t\t\tEnables verbose mode (more informations Server-side)\n" +
                "\t-n, -noroot\t\t\t\t\tDisables the root directory restriction (Access entire file browser)\n" +
                "\t-u, -upload\t\t\t\t\tEnables uploading to the host mashine\n" +
                "\t-h, -hidden\t\t\t\t\tAlso send informations about hidden files\n" +
                "\t-ip=<network_name>\t\t\tSets the IP Address to the given network name [default: 0.0.0.0]\n" +
                "\t-p, -port=<port>\t\t\tSets the Port [default: 80]\n" +
                "\t-t, -threads=<size>\t\t\tSets the Thread Pool Size [default: 3]\n" +
                "\t-r, -root=<path>\t\t\tSets the root folder [default: user.dir]\n");
        System.exit(0);
    }

    /**
     * Appends the network interface information to the given string builder.
     * @param builder The string builder.
     * @param netint The network interface.
     */
    private static void displayInterfaceInformation(StringBuilder builder, NetworkInterface netint) {
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            if (inetAddress instanceof Inet6Address) continue;
            builder.append(netint.getName()).append(" - ").append(inetAddress.getHostAddress()).append("\n");
        }
    }
}
