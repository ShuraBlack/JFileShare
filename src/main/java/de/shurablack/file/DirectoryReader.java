package de.shurablack.file;

import de.shurablack.util.Config;
import de.shurablack.util.Formatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class DirectoryReader {

    public static JSONArray readDirectory(final File directory) {
        final JSONArray result = new JSONArray();

        if (!directory.exists() || !directory.isDirectory()) {
            return result;
        }

        final File[] files = directory.listFiles();

        if (files == null) {
            return result;
        }

        for (File file : files) {
            final JSONObject entry = new JSONObject();
            if (file.isHidden() && !Config.isShowHiddenFiles()) {
                continue;
            }

            entry.put("name", file.getName());
            entry.put("hidden", file.isHidden());

            if (file.isDirectory()) {
                entry.put("type", "directory");
            } else {
                entry.put("type", "file");
                entry.put("size", Formatter.formatSize(file.length()));
                entry.put("lastModified", Formatter.formatTime(file.lastModified()));
            }
            result.put(entry);
        }
        return result;
    }

    public static boolean isDirectoryWithin(final String path, final String directory) {
        if (!Config.isRootRestricted()) {
            return true;
        }
        final File file = new File(path);
        final File dir = new File(directory);
        return file.exists() && file.isDirectory() && file.getAbsolutePath().startsWith(dir.getAbsolutePath());
    }
}
