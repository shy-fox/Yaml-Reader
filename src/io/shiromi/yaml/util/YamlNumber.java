package io.shiromi.yaml.util;

import io.shiromi.yaml.Yaml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A yaml object which is able to hold a number, can have either an Integer or Float as a value
 * @version 1.5
 * @author Shiromi
 */
public final class YamlNumber extends Yaml {
    private Class<? extends Number> valueType;
    private int intValue = 0;
    private float floatValue = 0;

    /**
     * The value of this object, is a double to store a value of every number type
     */
    public double value;
    /**
     * The type of this object, is either <code>Integer</code> or <code>Float</code>
     */
    public String type;

    /**
     * Creates a new instance of this object, with a byte as value, which will be turned into an integer
     *
     * @param name  the name of this object
     * @param value the value of type <code>byte</code>, will then be turned into an <code>int</code>
     * @see #YamlNumber(String)
     * @see #YamlNumber(String, short)
     * @see #YamlNumber(String, int)
     * @see #YamlNumber(String, long)
     * @see #YamlNumber(String, float)
     * @see #YamlNumber(String, double)
     * @since 1.2
     */
    public YamlNumber(String name, byte value) {
        this(name, (int) value);
    }

    /**
     * Creates a new instance of this object, with a short as value, which will be turned into an integer
     *
     * @param name  the name of this object
     * @param value the value of type <code>short</code>, will then be turned into an <code>int</code>
     * @see #YamlNumber(String)
     * @see #YamlNumber(String, byte)
     * @see #YamlNumber(String, int)
     * @see #YamlNumber(String, long)
     * @see #YamlNumber(String, float)
     * @see #YamlNumber(String, double)
     * @since 1.2
     */
    public YamlNumber(String name, short value) {
        this(name, (int) value);
    }

    /**
     * Creates a new instance of this object, with an integer as value
     *
     * @param name  the name of this object
     * @param value the value of type <code>int</code>
     * @see #YamlNumber(String)
     * @see #YamlNumber(String, byte)
     * @see #YamlNumber(String, short)
     * @see #YamlNumber(String, long)
     * @see #YamlNumber(String, float)
     * @see #YamlNumber(String, double)
     */
    public YamlNumber(String name, int value) {
        super(name);
        this.valueType = Integer.class;
        this.type = this.valueType.getSimpleName();
        this.intValue = value;
        this.value = value;
    }

    /**
     * Creates a new instance of this object, with a long as value, which will be turned into a float
     *
     * @param name  the name of this object
     * @param value the value of type <code>long</code>, will then be turned into an <code>float</code>
     * @see #YamlNumber(String)
     * @see #YamlNumber(String, byte)
     * @see #YamlNumber(String, short)
     * @see #YamlNumber(String, int)
     * @see #YamlNumber(String, float)
     * @see #YamlNumber(String, double)
     * @since 1.2
     */
    public YamlNumber(String name, long value) {
        this(name, (float) value);
    }

    /**
     * Creates a new instance of this object, with a float as value
     *
     * @param name  the name of this object
     * @param value the value of type <code>float</code>
     * @see #YamlNumber(String)
     * @see #YamlNumber(String, byte)
     * @see #YamlNumber(String, short)
     * @see #YamlNumber(String, long)
     * @see #YamlNumber(String, int)
     * @see #YamlNumber(String, double)
     */
    public YamlNumber(String name, float value) {
        super(name);
        this.valueType = Float.class;
        this.type = this.valueType.getSimpleName();
        this.floatValue = value;
        this.value = value;
    }

    /**
     * Creates a new instance of this object, with a double as value, which will be turned into a float
     *
     * @param name  the name of this object
     * @param value the value of type <code>double</code>, will then be turned into an <code>float</code>
     * @see #YamlNumber(String)
     * @see #YamlNumber(String, byte)
     * @see #YamlNumber(String, short)
     * @see #YamlNumber(String, int)
     * @see #YamlNumber(String, long)
     * @see #YamlNumber(String, float)
     * @since 1.2
     */
    public YamlNumber(String name, double value) {
        this(name, (float) value);
    }

    /**
     * Creates a new instance of this object with <code>0</code> as value
     *
     * @param name the name of this object
     * @see #YamlNumber(String, byte)
     * @see #YamlNumber(String, short)
     * @see #YamlNumber(String, int)
     * @see #YamlNumber(String, long)
     * @see #YamlNumber(String, float)
     * @see #YamlNumber(String, double)
     * @since 1.3
     */
    public YamlNumber(String name) {
        this(name, 0);
    }

    /**
     * Change the value of this object to the new value, which will be cast to <code>int</code>
     *
     * @param b the new value to set this object's value to
     * @return the previous value
     * @see #set(short)
     * @see #set(int)
     * @see #set(long)
     * @see #set(float)
     * @see #set(double)
     * @see #resetInt()
     * @see #resetFloat()
     * @since 1.3
     */
    public int set(byte b) {
        return this.set((int) b);
    }

    /**
     * Change the value of this object to the new value, which will be cast to <code>int</code>
     *
     * @param s the new value to set this object's value to
     * @return the previous value
     * @see #set(byte)
     * @see #set(int)
     * @see #set(long)
     * @see #set(float)
     * @see #set(double)
     * @see #resetInt()
     * @see #resetFloat()
     * @since 1.3
     */
    public int set(short s) {
        return this.set((int) s);
    }

    /**
     * Change the value of this object to the new value
     *
     * @param i the new value to set this object's value to
     * @return the previous value
     * @see #set(byte)
     * @see #set(short)
     * @see #set(long)
     * @see #set(float)
     * @see #set(double)
     * @see #resetInt()
     * @see #resetFloat()
     */
    public int set(int i) {
        this.changeType(Integer.class);
        int oldVal = this.intValue;
        this.intValue = (int) (this.value = i);
        return oldVal;
    }

    /**
     * Change the value of this object to the new value, which will be cast to <code>float</code>
     *
     * @param l the new value to set this object's value to
     * @return the previous value
     * @see #set(byte)
     * @see #set(short)
     * @see #set(int)
     * @see #set(float)
     * @see #set(double)
     * @see #resetInt()
     * @see #resetFloat()
     * @since 1.3
     */
    public float set(long l) {
        return this.set((float) l);
    }

    /**
     * Change the value of this object to the new value
     *
     * @param f the new value to set this object's value to
     * @return the previous value
     * @see #set(byte)
     * @see #set(short)
     * @see #set(int)
     * @see #set(long)
     * @see #set(double)
     * @see #resetInt()
     * @see #resetFloat()
     */
    public float set(float f) {
        this.changeType(Float.class);
        float oldVal = this.floatValue;
        this.floatValue = (float) (this.value = f);
        return oldVal;
    }

    /**
     * Change the value of this object to the new value, which will be cast to <code>float</code>
     *
     * @param d the new value to set this object's value to
     * @return the previous value
     * @see #set(byte)
     * @see #set(short)
     * @see #set(int)
     * @see #set(long)
     * @see #set(float)
     * @see #resetInt()
     * @see #resetFloat()
     * @since 1.3
     */
    public float set(double d) {
        return this.set((float) d);
    }

    /**
     * Resets and returns the old int value of this object
     *
     * @see #set(int)
     * @see #resetFloat()
     * @since 1.4
     */
    public int resetInt() {
        return this.set(0);
    }

    /**
     * Resets and returns the old float value of this object
     *
     * @see #set(float)
     * @see #resetInt()
     * @since 1.4
     */
    public float resetFloat() {
        return this.set(0f);
    }

    private void changeType(Class<? extends Number> to) {
        if (this.valueType == to) return;
        this.valueType = to;
        this.type = to.getSimpleName();
    }

    /**
     * Gets this object's value
     *
     * @return {@link #value}
     */
    @Override
    public Double get() {
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
     * Returns this object's value cast to <code>int</code>
     * @return <code>{@link #value (int) value}</code>
     */
    @Override
    public int size() {
        return this.get().intValue();
    }

    /**
     * Returns a string representation of this object
     * @param tabs the amount of tabs to be inserted before
     * @return A string of format <code>&lt;name&gt;: &lt;intValue|floatValue&gt;</code>
     */
    @Override
    @NotNull
    public String stringify(int tabs) {
        return "\t".repeat(tabs) + this.name + ": " +
                (this.valueType == Integer.class ? this.intValue : this.floatValue);
    }

    /**
     * Creates a new YamlNumber based on a string
     * @param s the string to parse
     * @return a new YamlNumber object if it could be parsed, otherwise returns <code>null</code>
     */
    public static @Nullable YamlNumber parse(String s) {
        Matcher m = Pattern.compile("^\\s*(?<name>[a-z]\\w*):\\s?(?<value>[+-]?\\d+(?>\\.[0-9]+)?(?>[eE][+-]\\d+)?)")
                .matcher(s);
        if (m.matches()) {
            String name = m.group("name");
            String value = m.group("value");
            if (Pattern.matches("[+-]?\\d+$", value))
                return new YamlNumber(name, Integer.parseInt(value));
            else if (Pattern.matches("[+-]?\\d+(?>\\.[0-9])?(?>[eE][+-]\\d+)?", value))
                return new YamlNumber(name, Float.parseFloat(value));
        }
        return null;
    }
}
