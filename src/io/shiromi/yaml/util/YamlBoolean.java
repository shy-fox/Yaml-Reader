package io.shiromi.yaml.util;

import io.shiromi.yaml.Yaml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A yaml object which can take a <code>boolean</code> value
 * @version 1.7
 * @author Shiromi
 */
public final class YamlBoolean extends Yaml {
    /**
     * The value of this boolean, is either <code>true</code> or <code>false</code>
     */
    public boolean value;

    /**
     * Creates a new instance with given name and value
     *
     * @param name  the name of this object
     * @param value the value of this object
     * @see #YamlBoolean(String)
     * @see #YamlBoolean(String, String)
     */
    public YamlBoolean(String name, boolean value) {
        super(name);
        this.value = value;
    }

    /**
     * Creates a new instance with value set to <code>false</code>
     *
     * @param name the name of this object
     * @see #YamlBoolean(String, String)
     * @see #YamlBoolean(String, boolean)
     */
    public YamlBoolean(String name) {
        this(name, false);
    }

    /**
     * Creates a new instance with given name and the value parsed to a boolean
     *
     * @param name  the name of this object
     * @param value the boolean value as a string
     */
    public YamlBoolean(String name, String value) {
        this(name, Boolean.parseBoolean(value));
    }

    /**
     * Changes the value of this object
     *
     * @param b the new value to set this object's value to
     * @return the old value
     * @see #toggle()
     */
    public boolean set(boolean b) {
        boolean oldValue = this.value;
        this.value = b;
        return oldValue;
    }

    /**
     * Changes the state of this object's value to the opposite
     *
     * @return the old state
     * @see #set(boolean)
     * @since 1.2
     */
    public boolean toggle() {
        return this.set(!this.value);
    }

    /**
     * Gets this objects value, returns <code>true</code> if set, otherwise returns <code>false</code>
     *
     * @return the same value a boolean can take, <code>true</code> or <code>false</code>
     */
    @Override
    public Boolean get() {
        return this.value;
    }

    /**
     * <strong>** DO NOT USE! **</strong><br>
     * Will only return -1
     *
     * @return <code>-1</code>
     * @hidden
     * @deprecated 1.0
     */
    @Override
    @Deprecated(forRemoval = true, since = "1.0")
    public int length() {
        return -1;
    }

    /**
     * <strong>** DO NOT USE! **</strong><br>
     * Will only return -1
     *
     * @return <code>-1</code>
     * @hidden
     * @deprecated 1.0
     */
    @Override
    @Deprecated(forRemoval = true, since = "1.0")
    public int size() {
        return this.length();
    }

    /**
     * Returns a string representation of this object, e.g.
     * <blockquote>
     * <pre>{@code
     *      YamlBoolean b = new YamlBoolean("Bool");
     *      System.out.println(b.stringify());
     *      // prints 'Bool: false'
     *     }</pre>
     * </blockquote>
     *
     * @param tabs the amount of tabs to be inserted before
     * @return a string of format <code>&lt;name&gt;: &lt;value&gt;</code>
     */
    @Override
    @NotNull
    public String stringify(int tabs) {
        return "\t".repeat(tabs) + this.name + ": " + this.value;
    }

    /**
     * Creates a new YamlBoolean based on a string
     *
     * @param s the string to parse
     * @return a new YamlBoolean if it could be parsed, otherwise returns <code>null</code>
     */
    public static @Nullable YamlBoolean parse(String s) {
        Matcher m = Pattern.compile("^\\s*(?<name>[a-z][\\w ]*):\\s?(?<value>true|false)").matcher(s);
        if (m.matches()) return new YamlBoolean(m.group("name"), m.group("value"));
        return null;
    }
}
