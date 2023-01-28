package io.shiromi.yaml;

import io.shiromi.yaml.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A file object containing Yaml objects
 *
 * @author Shiromi
 * @version 1.7
 */
public final class YamlFile {
    private final File file;
    private final String name;

    private Yaml[] content = new Yaml[0];

    /**
     * Creates a new instance from a file
     *
     * @param f the file, should include a path and end with <code>.yaml</code>
     * @see #YamlFile(String)
     * @see #YamlFile(String, String)
     */
    public YamlFile(@NotNull File f) {
        this.file = f;
        this.name = f.getName();
    }

    /**
     * Creates a new instance from a file name
     *
     * @param name the name of the file, no extension
     * @see #YamlFile(File)
     * @see #YamlFile(String, String)
     */
    public YamlFile(String name) {
        this(new File(name + ".yaml"));
    }

    /**
     * Creates a new instance from a file name and path
     *
     * @param path the path to the file, does not need to include the last backslash
     * @param name the name of the file, no extension
     * @see #YamlFile(File)
     * @see #YamlFile(String)
     */
    public YamlFile(@NotNull String path, String name) {
        this(new File(path.replace('\\', '/') + '/' + name + ".yaml"));
    }

    /**
     * Add a new item to the file
     *
     * @param item the item to add
     * @return the same instance to further add to if wanted
     * @see #write(Yaml...)
     * @see #write(String)
     */
    public YamlFile write(Yaml item) {
        Yaml[] newContent = new Yaml[this.itemCount() + 1];
        System.arraycopy(this.content, 0, newContent, 0, this.itemCount());
        newContent[this.itemCount()] = item;
        this.content = newContent;
        return this;
    }

    /**
     * Adds new items to this file
     *
     * @param items the items to add
     * @return the same instance to further add to if wanted
     * @see #write(Yaml)
     * @see #write(String)
     */
    public YamlFile write(Yaml @NotNull ... items) {
        YamlFile f = null;
        for (Yaml y : items) f = this.write(y);
        return f;
    }

    /**
     * Adds the string, if it could be parsed into yaml
     *
     * @param content the string to parse
     * @return the same instance to further add to if wanted
     * @see #write(Yaml)
     * @see #write(Yaml...)
     * @see Yaml#fromString(String)
     */
    public YamlFile write(String content) {
        return this.write(Yaml.fromString(content));
    }

    /**
     * Gets the path to the file, minus the name
     */
    public @NotNull String getPath() {
        return this.file.getPath().replace(this.name, "");
    }

    private @NotNull String createStringContent() {
        StringBuilder s = new StringBuilder();
        for (Yaml y : this.content) s.append(y.stringify()).append('\n');
        return s.toString();
    }

    /**
     * Creates a new file and writes the content to it
     *
     * @return whether the file could be created or written to
     */
    public boolean create() {
        try {
            if (!this.file.exists()) Files.createDirectories(Paths.get(this.getPath()));
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.file));
            writer.write(this.createStringContent());
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Reads the file given and parses it into a YamlFile
     *
     * @return a YamlFile with the contents parsed to a Yaml[]
     */
    public @Nullable YamlFile read() {
        if (!this.file.exists() && this.file.getName().endsWith(".yaml")) {
            return read(this.file);
        }
        return null;
    }

    /**
     * Reads the file passed in as an argument and parsed it into a YamlFile
     *
     * @param f the file to read from
     * @return a new YamlFile with the contents parsed to a Yaml[]
     */
    public static @Nullable YamlFile read(@NotNull File f) {
        if (!f.getName().endsWith(".yaml")) return null;
        StringBuilder s = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new FileReader(f));
            String s1;
            while ((s1 = r.readLine()) != null) s.append(s1).append('\n');
            r.close();
        } catch (IOException e) {
            System.err.printf("File %s either does not exist or cannot be opened\n", f);
            return null;
        }
        return new YamlFile(f).write(s.toString());
    }

    /**
     * Gets the amount of lines of the file read
     */
    public int lineCount() {
        return this.createStringContent().split("\\n").length;
    }

    /**
     * Gets the amount of items in this file
     */
    public int itemCount() {
        return this.content.length;
    }

    /**
     * Gets the length of content of the file
     */
    public int length() {
        return this.createStringContent().length();
    }

    /**
     * Gets the content turned into a string
     */
    public @NotNull String getContent() {
        return this.createStringContent();
    }

    /**
     * Gets the content
     */
    public Yaml[] get() {
        return this.content;
    }

    /**
     * Gets an item by name
     *
     * @param name the name of the item
     * @return the item if found, otherwise <code>null</code>
     * @see #get(int)
     * @see #get(String, Class)
     * @since 1.7
     */
    public @Nullable Yaml get(String name) {
        for (Yaml y : this.get()) if (y.name.equals(name)) return y;
        return null;
    }

    /**
     * Gets an item by its name, but applies a type filter to it
     *
     * @param name the name of the item
     * @param type the type of the item
     * @return the item if found and filter matches, otherwise returns <code>null</code>
     * @see #get(String)
     * @see #getString(String)
     * @see #getNumber(String)
     * @see #getBoolean(String)
     * @see #getNull(String)
     * @see #getArray(String)
     * @see #getObject(String)
     * @since 1.7
     */
    public @Nullable Yaml get(String name, Class<? extends Yaml> type) {
        Yaml y = this.get(name);
        if (y == null) return null;
        Class<? extends Yaml> cls = y.getClass();
        if (cls == type) return y;
        return null;
    }

    /**
     * Gets a <code>YamlString</code> by its name
     *
     * @param name the name of the string
     * @return the string if found, otherwise returns <code>null</code>
     * @see #get(String, Class)
     * @since 1.7
     */
    public YamlString getString(String name) {
        return (YamlString) this.get(name, YamlString.class);
    }

    /**
     * Gets a <code>YamlNumber</code> by its name
     *
     * @param name the name of the number
     * @return the number if found, otherwise returns <code>null</code>
     * @see #get(String, Class)
     * @since 1.7
     */
    public YamlNumber getNumber(String name) {
        return (YamlNumber) this.get(name, YamlNumber.class);
    }

    /**
     * Gets a <code>YamlBoolean</code> by its name
     *
     * @param name the name of the boolean
     * @return the boolean if found, otherwise returns <code>null</code>
     * @see #get(String, Class)
     * @since 1.7
     */
    public YamlBoolean getBoolean(String name) {
        return (YamlBoolean) this.get(name, YamlBoolean.class);
    }

    /**
     * Gets a <code>YamlNull</code> by its name
     *
     * @param name the name of the null type
     * @return the null type if found, otherwise returns <code>null</code>
     * @see #get(String, Class)
     * @since 1.7
     */
    public YamlNull getNull(String name) {
        return (YamlNull) this.get(name, YamlNull.class);
    }

    /**
     * Gets a <code>YamlArray</code> by its name
     *
     * @param name the name of the array
     * @return the array if found, otherwise returns <code>null</code>
     * @see #get(String, Class)
     * @since 1.7
     */
    public YamlArray getArray(String name) {
        return (YamlArray) this.get(name, YamlArray.class);
    }

    /**
     * Gets a <code>YamlObject</code> by its name
     *
     * @param name the name of the object
     * @return the object if found, otherwise returns <code>null</code>
     * @see #get(String, Class)
     * @since 1.7
     */
    public YamlObject getObject(String name) {
        return (YamlObject) this.get(name, YamlObject.class);
    }

    /**
     * Gets an item based on its index
     *
     * @param index the index of the item
     * @return the item at index, can be <code>null</code>
     * @see #get(String)
     * @since 1.7
     */
    public @Nullable Yaml get(int index) {
        if (0 > index || index > this.length() - 1) return null;
        return this.get()[index];
    }

    @Override
    public @NotNull String toString() {
        return "YamlFile[path=" + this.getPath() + "; name=" + this.name + "]\n" +
                "has content:\n" +
                this.getContent();
    }
}
