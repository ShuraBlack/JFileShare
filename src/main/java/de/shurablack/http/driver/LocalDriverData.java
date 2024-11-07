package de.shurablack.http.driver;

import org.json.JSONObject;

public class LocalDriverData {

    private final String name;

    private final long totalSpace;

    private final long usedSpace;

    public LocalDriverData(String name, long totalSpace, long usedSpace) {
        this.name = name;
        this.totalSpace = totalSpace;
        this.usedSpace = usedSpace;
    }

    public JSONObject toJson() {
        final JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("totalSpace", totalSpace);
        json.put("usedSpace", usedSpace);
        return json;
    }
}
