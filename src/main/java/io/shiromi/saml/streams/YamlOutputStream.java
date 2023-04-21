package io.shiromi.saml.streams;

import io.shiromi.saml.elements.YamlElement;
import io.shiromi.saml.functions.DataOutputStream;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.NoSuchElementException;

public final class YamlOutputStream extends BufferedWriter implements DataOutputStream {
    private final FileWriter writer;

    private char[] buf;
    private int size;

    private YamlOutputStream(File f) throws IOException {
        super(new FileWriter(f));
        this.writer = new FileWriter(f);
        this.buf = new char[0];
        this.size = buf.length;
    }

    public boolean putContent() throws IOException {
        assert writer != null;
        for (char c : buf) writer.write(c);
        return true;
    }

    public char @NotNull [] put(char @NotNull [] buf) {
        char[] nBuf = grow(buf.length);
        System.arraycopy(buf, 0, nBuf, size, buf.length);

        update(nBuf);
        return nBuf;
    }

    public void put(@NotNull YamlElement<?> element) {
        put(element.toBuffer());
    }

    @Contract(pure = true)
    public @NotNull String getContent() {
        return String.valueOf(buf);
    }

    public void clearContent() throws NoSuchElementException {

        for (int i = 0; i < size; i++) buf[i] = 0;
    }

    public char get(int index) throws IndexOutOfBoundsException {
        return buf[index];
    }

    private char @NotNull [] grow(int length) {
        char[] nBuf = new char[size + length];
        System.arraycopy(buf, 0, nBuf, 0, size);
        return nBuf;
    }

    public boolean isEmpty() {
        for (char c : buf)
            if (c != 0) return false;
        return true;
    }

    @Contract(mutates = "this")
    private void update(char @NotNull [] buf) {
        this.buf = buf;
        size = buf.length;
    }

    @Contract("_ -> new")
    public static @NotNull YamlOutputStream getInstance(File f) throws IOException {
        return new YamlOutputStream(f);
    }

    public static @NotNull YamlOutputStream getInstance(String s) throws IOException {
        File f = new File(s);
        if (!f.exists()) throw new FileNotFoundException("File " + s + " does not exist");
        return getInstance(f);
    }
}
