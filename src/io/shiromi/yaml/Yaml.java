package io.shiromi.yaml;

import io.shiromi.yaml.exception.CannotCastToYamlArrayException;
import io.shiromi.yaml.util.*;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A <em>Yaml</em> object used for reference and inheritance.
 *
 * @author Shiromi
 * @version 1.7
 */
public abstract class Yaml {
    /**
     * The name of the object
     */
    public String name;

    protected Yaml(String name) {
        this.name = name;
    }

    /**
     * Gets the value of the object
     *
     * @return the value specified by the object
     */
    public abstract Object get();

    /**
     * Gets the length of the value, if available
     *
     * @return the length if available
     * @see #size()
     */
    public abstract int length();

    /**
     * Gets the length of the value, if not specified otherwise.
     *
     * @return the length
     * @see #length()
     * @deprecated
     */
    @Deprecated
    public abstract int size();

    /**
     * Returns a string representation of the object
     *
     * @param tabs the amount of tabs to be inserted before
     * @return a string representation
     */
    public abstract @NotNull String stringify(int tabs);


    /**
     * Returns the default representation of this object as a string with no indents to start
     *
     * @return the string representation of this object
     * @see #stringify(int)
     */
    public final @NotNull String stringify() {
        return this.stringify(0) + '\n';
    }

    /**
     * Returns the string representation of the given argument
     *
     * @param y a list of <code>Yaml</code> type objects
     * @return the string representation of the given values
     * @see #stringify()
     */
    public static @NotNull String stringify(Yaml @NotNull ... y) {
        StringBuilder s = new StringBuilder();
        for (Yaml y1 : y) s.append(y1.stringify());
        return s.toString();
    }

    /**
     * Turns a string into yaml objects, example:
     * <blockquote>
     * <pre>{@code
     *      String yamlString = """
     *                 version: "1.0.0"
     *                 public: false
     *                 obj:
     *                   item1: "value"
     *                   item2: 2
     *                   item3: [ null ]
     *                 """;
     *      Yaml[] yamlItems = Yaml.fromString(yamlString);
     *     }</pre>
     * </blockquote>
     *
     * @param s the string to parse
     * @return an array of the yaml objects created based on the string
     * @see YamlArray#parse(String)
     * @see YamlBoolean#parse(String)
     * @see YamlNull#parse(String)
     * @see YamlNumber#parse(String)
     * @see YamlObject#parse(String)
     * @see YamlString#parse(String)
     */
    public static Yaml[] fromString(@NotNull String s) {
        String[] lines = s.split("\\n");
        Yaml[] items = new Yaml[0];

        Pattern leading = Pattern.compile("^(?<leading>\\s*)");
        Pattern group = Pattern.compile("^\\s*(?<name>[A-Za-z][\\w ]*):$");

        while (lines.length > 0) {
            if (Pattern.matches("^\\s*#.+$", lines[0])) {
                lines = shrink(lines, 1);
                continue;
            }
            Matcher m1 = group.matcher(lines[0]);
            if (m1.find()) {
                Matcher m2 = leading.matcher(lines[0]);
                int whitespaces = 0;
                if (m2.find()) whitespaces = m2.group("leading").length();


                StringBuilder s1 = new StringBuilder(lines[0] + '\n');

                int currentLevel = whitespaces;
                for (int i = 1; i < lines.length; i++) {
                    Matcher m3 = leading.matcher(lines[i]);
                    if (m3.find()) currentLevel = m3.group("leading").length();
                    if (currentLevel == whitespaces) break;
                    s1.append(lines[i]).append('\n');
                }
                YamlObject o = YamlObject.parse(s1.substring(0, s1.length() - 1));
                assert o != null;
                items = extend(items, o);
                lines = shrink(lines, o.absoluteSize() + 1);

            } else {
                String s1 = lines[0].trim();
                Yaml y = YamlString.parse(s1);
                if (y == null) y = YamlNumber.parse(s1);
                if (y == null) y = YamlBoolean.parse(s1);
                if (y == null) y = YamlNull.parse(s1);
                if (y == null) y = YamlArray.parse(s1);
                items = extend(items, y);
                lines = shrink(lines, 1);
            }
        }
        return items;
    }

    /**
     * Cast this object to a {@link YamlNull} type, effectively setting the value to <code>null</code>
     *
     * @param y the Yaml object to <em>nullify</em>
     * @return the <em>nullified</em> <code>Yaml</code> object, if it is a <code>YamlNull</code> object, returns the object, otherwise returns
     * a new <code>YamlNull</code> with the same name
     * @see YamlNull#YamlNull(String)
     * @see #toNull()
     * @since 1.6-b
     */
    public static @NotNull YamlNull toNull(@NotNull Yaml y) {
        if (!y.isNullType()) return new YamlNull(y.name);
        return (YamlNull) y;
    }

    /**
     * Cast this object to a {@link YamlNull} type, effectively setting the value to <code>null</code>
     *
     * @return the <em>nullified</em> object
     * @see #toNull(Yaml)
     * @see YamlNull#YamlNull(String)
     * @since 1.6-b
     */
    public YamlNull toNull() {
        return toNull(this);
    }

    /**
     * Casts the given object to a new {@link YamlArray} with the first element being the value of the passed in object
     *
     * @param y the object to cast to a <code>YamlArray</code>
     * @return a new <code>YamlArray</code> with the first element being the value of the given object
     * @throws CannotCastToYamlArrayException if the object given is already a <code>YamlArray</code> or if it is
     *                                        of type <code>YamlObject</code>
     * @since 1.6-b
     */
    @Contract("_ -> new")
    public static @NotNull YamlArray toArray(@NotNull Yaml y) throws CannotCastToYamlArrayException {
        if (!y.isPrimitive())
            throw new CannotCastToYamlArrayException("Cannot cast y to YamlObject because it is of type: " + y.getTypeName());
        return new YamlArray(y.name, y.get());
    }

    /**
     * Cast this object to a new {@link YamlArray} with the first element being the value of this object
     *
     * @return a new <code>YamlArray</code> with the name of this object and the first element being the value of
     * this object
     * @throws CannotCastToYamlArrayException if this object is a <code>YamlArray</code> or a <code>YamlObject</code>
     * @since 1.6-b
     */
    public YamlArray toArray() throws CannotCastToYamlArrayException {
        return toArray(this);
    }

    private static Yaml @NotNull [] extend(Yaml @NotNull [] array, Yaml y) {
        Yaml[] newArray = new Yaml[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = y;
        return newArray;
    }

    private static String @NotNull [] shrink(String @NotNull [] array, int amount) {
        if (amount > array.length) return new String[0];
        String[] newArray = new String[array.length - amount];
        System.arraycopy(array, amount, newArray, 0, array.length - amount);
        return newArray;
    }

    /**
     * Checks if the given object is a {@link YamlString}
     *
     * @param y the object to check
     * @return whether the object is a <code>YamlString</code> or not
     * @see #isString()
     */
    @Contract(pure = true)
    public static boolean isString(@NotNull Yaml y) {
        return y.getClass() == YamlString.class;
    }

    /**
     * Checks if the given object is a {@link YamlNumber}
     *
     * @param y the object to check
     * @return whether the object is a <code>YamlNumber</code> or not
     * @see #isNumber()
     */
    @Contract(pure = true)
    public static boolean isNumber(@NotNull Yaml y) {
        return y.getClass() == YamlNumber.class;
    }

    /**
     * Checks if the given object is a {@link YamlBoolean}
     *
     * @param y the object to check
     * @return whether the object is a <code>YamlBoolean</code> or not
     * @see #isBoolean()
     */
    @Contract(pure = true)
    public static boolean isBoolean(@NotNull Yaml y) {
        return y.getClass() == YamlBoolean.class;
    }

    /**
     * Checks if the given object is a {@link YamlNull}
     *
     * @param y the object to check
     * @return whether the object is a <code>YamlNull</code> or not
     * @see #isNullType()
     */
    @Contract(pure = true)
    public static boolean isNullType(@NotNull Yaml y) {
        return y.getClass() == YamlNull.class;
    }

    /**
     * Checks if the given object is a {@link YamlArray}
     *
     * @param y the object to check
     * @return whether the object is a <code>YamlArray</code> or not
     * @see #isArray()
     */
    @Contract(pure = true)
    public static boolean isArray(@NotNull Yaml y) {
        return y.getClass() == YamlArray.class;
    }

    /**
     * Checks if the given object is a {@link YamlObject}
     *
     * @param y the object to check
     * @return whether the object is a <code>YamlObject</code> or not
     * @see #isObject()
     */
    @Contract(pure = true)
    public static boolean isObject(@NotNull Yaml y) {
        return y.getClass() == YamlObject.class;
    }

    /**
     * Checks if the given object is a primitive Yaml type
     *
     * @param y the object to check
     * @return whether the object is a <code>YamlNumber</code>, <code>YamlString</code>, <code>YamlBoolean</code> or <code>YamlNull</code> or not
     * @see #isNumber()
     */
    public static boolean isPrimitive(Yaml y) {
        return !isArray(y) && !isObject(y);
    }

    /**
     * Gets the type of the object passed in
     *
     * @param y the object to get the type of
     * @return the type of the object, <code>Class&lt;? extends Yaml&gt;</code>
     */
    @Contract(pure = true)
    public static Class<? extends Yaml> getType(@NotNull Yaml y) {
        return y.getClass();
    }

    /**
     * Gets the name of this class
     *
     * @param y the object
     * @see #getTypeName()
     * @since 1.6-b
     */
    public static @NotNull String getTypeName(Yaml y) {
        return getType(y).getSimpleName();
    }

    /**
     * Gets the name of this object's class
     *
     * @see #getTypeName(Yaml)
     * @since 1.6-b
     */
    public String getTypeName() {
        return getTypeName(this);
    }

    /**
     * Checks if this object is a {@link YamlString} or not
     *
     * @return whether this type is a <code>YamlString</code> or not
     * @see #isString(Yaml)
     * @since 1.1
     */
    public final boolean isString() {
        return isString(this);
    }

    /**
     * Checks if this object is a {@link YamlNumber} or not
     *
     * @return whether this type is a <code>YamlNumber</code> or not
     * @see #isNumber(Yaml)
     * @since 1.1
     */
    public final boolean isNumber() {
        return isNumber(this);
    }

    /**
     * Checks if this object is a {@link YamlBoolean} or not
     *
     * @return whether this type is a <code>YamlBoolean</code> or not
     * @see #isBoolean(Yaml)
     * @since 1.1
     */
    public final boolean isBoolean() {
        return isBoolean(this);
    }

    /**
     * Checks if this object is a {@link YamlNull} or not
     *
     * @return whether this type is a <code>YamlNull</code> or not
     * @see #isNullType(Yaml)
     * @since 1.1
     */
    public final boolean isNullType() {
        return isNullType(this);
    }

    /**
     * Checks if this object is a {@link YamlArray} or not
     *
     * @return whether this type is a <code>YamlArray</code> or not
     * @see #isArray(Yaml)
     * @since 1.1
     */
    public final boolean isArray() {
        return isArray(this);
    }

    /**
     * Checks if this object is a {@link YamlObject} or not
     *
     * @return whether this type is a <code>YamlObject</code> or not
     * @see #isObject(Yaml)
     * @since 1.1
     */
    public final boolean isObject() {
        return isObject(this);
    }

    /**
     * Checks if this object is a primitive yaml type or not
     *
     * @return whether this type is a primitive type or not
     * @see #isPrimitive(Yaml)
     * @since 1.1
     */
    public final boolean isPrimitive() {
        return isPrimitive(this);
    }

    /**
     * Checks if the given argument is equal to this one
     *
     * @param other the object to compare this one to
     * @return whether both are full equal or not
     * @see #equals(Yaml, Yaml)
     */
    public final boolean equals(Yaml other) {
        if (other == null) return false;
        if (this.getClass() != other.getClass()) return false;
        return this.name.equals(other.name) && Objects.equals(this.get(), other.get());
    }

    /**
     * Compares 2 objects and checks if they are equal
     *
     * @param a the object to compare to
     * @param b the object to compare
     * @return whether the 2 items are equal or not
     * @see #equals(Yaml)
     * @since 1.2
     */
    public static boolean equals(@NotNull Yaml a, Yaml b) {
        return a.equals(b);
    }

    private static @NotNull String toString(@NotNull Class<? extends Yaml> cls, @NotNull Yaml y) {
        StringBuilder s = new StringBuilder(cls.getSimpleName() + '[');
        s.append("name: \"").append(y.name).append("\"");
        Field[] fields = cls.getFields();

        int iMax = fields.length - 1;
        if (iMax == 0) return s.append(']').toString();

        s.append(", ");
        for (int i = 0; ; i++) {
            try {
                Field f = fields[i];
                Object o = f.get(y);
                Class<?> type = f.getType();

                s.append(f.getName()).append(": ");
                if (type.isArray())
                    s.append(Arrays.toString((Object[]) o));
                else if (type == String.class || type == Character.class || type == char.class)
                    s.append('"').append(o).append('"');
                else
                    s.append(o);
            } catch (IllegalAccessException ignored) {
            }
            if (i == iMax - 1)
                return s.append(']').toString();
            s.append(", ");
        }
    }

    /**
     * A string representation of this object including class name and values
     *
     * @return a string similar to what <code>Arrays.toString</code> returns
     */
    @Override
    public final @NotNull String toString() {
        return toString(this.getClass(), this);
    }

    /**
     * Used to iterate through each element with index in a {@link YamlArray}
     *
     * @see YamlArray#forEach(YamlArrayIterator)
     * @since 1.2
     */
    public interface YamlArrayIterator {
        void item(int i, Object o);
    }

    /**
     * Used to iterate through each element with index and key in a {@link YamlObject}
     *
     * @see YamlObject#forEach(YamlObjectIterator)
     * @since 1.2
     */
    public interface YamlObjectIterator {
        void item(int i, String k, Yaml v);
    }
}
