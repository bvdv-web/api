package com.bvdv.bvdvapi.utils;

public class GoogleDriveUtil {
    public static Boolean isFile(String url) {
        return url.contains("/file/d/");
    }

    public static Boolean isFolder(String url) {
        return url.contains("/folders/");
    }

    public static String getIdFromUrl(String url) {
        if (url.contains("/file/d/")) {
            String result = url.substring(url.indexOf("/d/") + 3);
            int slash = result.indexOf("/");
            result = slash == -1 ? result : result.substring(0, slash);
            return result;
        } else if (url.contains("/folders/")) {
            String result = url.substring(url.indexOf("/folders/") + 9);
            int slash = result.indexOf("?");
            result = slash == -1 ? result : result.substring(0, slash);
            return result;
        } else {
            return null;
        }

    }
}
