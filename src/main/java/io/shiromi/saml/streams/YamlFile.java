package io.shiromi.saml.streams;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;

public class YamlFile {
    private final String fileName, filePath;

    private int length;

    private byte[] contentBytes;
    private char[] contentChars;

    private String content;

    private YamlFile(final String filePath, final String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public static @NotNull YamlFile read(@NotNull File file) throws IOException {
        if (!file.exists()) throw new FileNotFoundException(String.format("File '%s' does not exist.", file.getName()));

        StringBuilder sb = new StringBuilder();
        String s;

        BufferedReader br = new BufferedReader(new FileReader(file));

        while ((s = br.readLine()) != null) {
            sb.append(s);
        }

        char[] chars = new char[sb.length()];
        byte[] bytes = new byte[sb.length()];

        s = sb.toString();

        for (int i = 0; i < sb.length(); i++) {
            char c = s.charAt(i);
            chars[i] = c;
            bytes[i] = (byte) c;
        }

        YamlFile f = new YamlFile(file.getPath(), file.getName());
        f.length = s.length();
        f.contentBytes = bytes;
        f.contentChars = chars;
        f.content = s;

        return f;
    }

    public static @NotNull YamlFile create(@NotNull File file) throws FileAlreadyExistsException {
        if (file.exists()) throw new FileAlreadyExistsException(String.format("File '%s' already exists.", file.getName()));

        YamlFile f = new YamlFile(file.getPath(), file.getName());
        f.content = "";
        f.contentBytes = new byte[0];
        f.contentChars = new char[0];
        f.length = 0;

        return f;
    }

    public static YamlFile newInstance(File file) {
        YamlFile f = null;

        // If file cannot be read, create new file, even if it's empty, it will be registered as a file which exists.

        try {
            f = read(file);
        } catch (IOException e) {
            try {
                f = create(file);
            } catch (FileAlreadyExistsException ignored) {}
        } return f;
    }
}
