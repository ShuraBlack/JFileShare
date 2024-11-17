package de.shurablack.file;

import de.shurablack.util.Config;
import de.shurablack.util.Formatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        return sortJsonArray(result);
    }

    public static JSONArray sortJsonArray(JSONArray jsonArray) {
        List<JSONObject> jsonObjects = StreamSupport.stream(jsonArray.spliterator(), false)
                .map(JSONObject.class::cast)
                .sorted(Comparator
                        .comparing((JSONObject obj) -> obj.getString("type"))
                        .thenComparing(obj -> obj.getString("name")))
                .collect(Collectors.toList());

        jsonObjects.sort((o1, o2) -> {
            String type1 = o1.getString("type");
            String type2 = o2.getString("type");
            int typeCompare = type1.compareTo(type2);
            if (type1.equals("directory") && type2.equals("file")) {
                return -1;
            } else if (type1.equals("file") && type2.equals("directory")) {
                return 1;
            } else {
                return typeCompare;
            }
        });
        return new JSONArray(jsonObjects);
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
