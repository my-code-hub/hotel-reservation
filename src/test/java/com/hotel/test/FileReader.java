package com.hotel.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileReader {

    public static String readFile(String path) throws IOException {
        return readFile(path, new Object[]{});
    }

    public static String readFile(String path, Object... placeholders) throws IOException {
        InputStream fileAsStream = FileReader.class.getResourceAsStream(preparePath(path));
        String content = new String(fileAsStream.readAllBytes(), StandardCharsets.UTF_8);
        return placeholders.length > 0 ? String.format(content, placeholders) : content;
    }

    private static String preparePath(String path) {
        return path.indexOf('/') > 0 ? "/" + path : path;
    }
}
