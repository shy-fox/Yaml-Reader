package io.shiromi.saml.streams;

import io.shiromi.saml.functions.DataInputStream;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class YamlInputStream extends BufferedReader implements DataInputStream {
    private FileReader reader;

    private char[] buf;
    private int size;

    private YamlInputStream(File f) throws IOException {
        super(new FileReader(f));
        this.reader = new FileReader(f);
        this.buf = read(f);
        this.size = buf.length;
    }

    public char[] read(File f) throws IOException {
        char[] emptyData = new char[0];

        int c;
        while ((c = this.reader.read()) != -1) {
            emptyData = grow(emptyData, (char) c);
        }

        return emptyData;
    }

    private char @NotNull [] grow(char @NotNull [] data, char @NotNull ... newData) {
        char[] nData = new char[data.length + newData.length];
        System.arraycopy(data, 0, nData, 0, data.length);
        System.arraycopy(newData, 0, nData, data.length, newData.length);
        return nData;
    }
}
