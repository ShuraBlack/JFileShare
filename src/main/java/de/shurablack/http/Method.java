package de.shurablack.http;

public enum Method {
    GET, POST, PUT, DELETE, HEAD, UNSUPPORTED;

    public static Method fromString(String method) {
        switch (method) {
            case "GET":
                return GET;
            case "POST":
                return POST;
            case "PUT":
                return PUT;
            case "DELETE":
                return DELETE;
            case "HEAD":
                return HEAD;
            default:
                return UNSUPPORTED;
        }
    }
}
