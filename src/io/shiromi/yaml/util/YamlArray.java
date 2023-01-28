package io.shiromi.yaml.util;

import io.shiromi.yaml.Yaml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A yaml object in form of an array, takes <code>Objects</code> as values
 *
 * @author Shiromi
 * @version 1.7
 */
public class YamlArray extends Yaml {
    /**
     * The items of this array
     */
    public Object[] values;

    /**
     * Creates a new instance with 2 arguments
     *
     * @param name   the name of the array
     * @param values the values of the array
     * @see #YamlArray(String)
     */
    public YamlArray(String name, Object... values) {
        super(name);
        this.values = values;
    }

    /**
     * Creates a new instance with an empty array as values
     *
     * @param name the name of the array
     * @see #YamlArray(String, Object...)
     */
    public YamlArray(String name) {
        this(name, new Object[]{});
    }

    /**
     * Adds the given item at the index
     *
     * @param index the index to add the item to, can be the same as the length of values
     * @param o     the object to add/insert
     * @return the new length of the array if it was added, otherwise returns -1
     * @see #add(int, Object...)
     * @see #append(Object)
     * @see #append(Object...)
     */
    public int add(int index, Object o) {
        if (0 > index || index >= this.length() + 1) return -1;
        Object[] newValues = new Object[this.length() + 1];
        int j = 0;
        for (int i = 0; i < newValues.length; i++) {
            if (i == index) {
                newValues[i] = o;
                continue;
            }
            newValues[i] = this.get(j++);
        }
        this.values = newValues;
        return this.length();
    }

    /**
     * Adds the items at the specified index
     *
     * @param index the index to add the elements after
     * @param o     an array of objects to add
     * @return the new length of the array if they were added, if they couldn't be added, returns -1
     */
    public int add(int index, Object @NotNull ... o) {
        int length = 0;
        for (int i = 0; i < o.length; i++) length = this.add(index + i, o[i]);
        return length;
    }

    /**
     * Appends the item to the end of the array
     *
     * @param o the item to append
     * @return the new length of the array
     * @see #append(Object...)
     * @since 1.1
     */
    public int append(Object o) {
        return this.add(this.length(), o);
    }

    /**
     * Appends the items to the end of the array
     *
     * @param o the list of items to add
     * @return the new length of this array
     * @see #append(Object)
     * @since 1.1
     */
    public int append(Object @NotNull ... o) {
        int length = 0;
        for (Object o1 : o) length = this.append(o1);
        return length;
    }

    /**
     * Replace the object at the given index with a new value, if <code>newValue</code> is <code>Null</code>, will set value to <code>null</code>
     *
     * @param index    the index to replace the item at
     * @param newValue the new value to set the item to, can be <code>Null</code>
     * @return the original object if it could be replaced, otherwise returns <code>null</code>
     * @since 1.3
     */
    public @Nullable Object replace(int index, @Nullable Object newValue) {
        if (0 > index || index >= this.length()) return null;
        Object o = this.get(index);
        this.values[index] = newValue;
        return o;
    }

    /**
     * Removes the item at the given index
     *
     * @param index the index to remove the item at
     * @return the item if it could be removed, otherwise returns <code>null</code>
     */
    public @Nullable Object remove(int index) {
        if (0 > index || index >= this.length()) return null;
        Object[] newValues = new Object[this.length() - 1];
        Object o = null;
        int j = 0;
        for (int i = 0; i < this.length(); i++) {
            if (i == index) {
                o = this.get(i);
                continue;
            }
            newValues[j++] = this.get(i);
        }
        this.values = newValues;

        return o;
    }

    /**
     * Removes <strong><code>length</code></strong> items after the index, and returns them
     *
     * @param index  the start index to remove from
     * @param length the amount of items to remove
     * @return an array of the removed items if they could be removed, otherwise returns <code>null</code>
     */
    public @Nullable Object[] remove(int index, int length) {
        if (0 > index || index + length >= this.length()) return null;
        Object[] o = new Object[length];
        for (int i = index, j = 0; i < index + length; i++, j++) o[j] = this.remove(i);
        return o;
    }

    /**
     * Removes and returns the last item of the list
     */
    public Object pop() {
        return this.remove(this.length() - 1);
    }

    /**
     * Iterates through the array and allows for operations on each array item
     * <blockquote>
     * <pre>{@code
     *      YamlArray array = ...;
     *      // ...
     *      array.forEach((i, o) -> {
     *          System.out.printf("Item '%s' is at index %s", o, i);
     *      });
     *     }</pre>
     * </blockquote>
     *
     * @param iterator the function to apply to each item
     * @see io.shiromi.yaml.Yaml.YamlArrayIterator#item(int, Object)
     * @see YamlObject#forEach(YamlObjectIterator)
     */
    public void forEach(YamlArrayIterator iterator) {
        for (int i = 0; i < this.length(); i++) iterator.item(i, this.get(i));
    }

    /**
     * Gets the item at given index, returns <code>null</code> if index is out of range
     *
     * @param index the index of the item to retrieve
     */
    public @Nullable Object get(int index) {
        if (0 > index || index >= this.length()) return null;
        return this.values[index];
    }

    /**
     * Gets the type of the array
     *
     * @since 1.6-b
     */
    public String getType() {
        String r = "";
        if (this.isStringArray()) r = "String[]";
        else if (this.isNumArray()) r = "Number[]";
        else if (this.isNullArray()) r = "null[]";
        else if (this.isBoolArray()) r = "Boolean[]";
        else if (this.isAnyArray()) r = "Any[]";

        return r;
    }

    /**
     * Checks whether all element in this array are a <code>String</code> or not
     *
     * @see #getType()
     * @since 1.6-b
     */
    public boolean isStringArray() {
        for (Class<?> cls : this.getFieldTypes()) if (cls != String.class) return false;
        return true;
    }

    /**
     * Checks whether all elements in this array are <code>Numbers</code> or not
     *
     * @see #getType()
     * @since 1.6-b
     */
    public boolean isNumArray() {
        for (Class<?> cls : this.getFieldTypes()) if (cls.getSuperclass() != Number.class) return false;
        return true;
    }

    /**
     * Checks whether all elements in this array are <code>null</code> or not
     *
     * @see #getType()
     * @since 1.6-b
     */
    public boolean isNullArray() {
        for (Class<?> cls : this.getFieldTypes()) if (cls != Object.class) return false;
        return true;
    }

    /**
     * Checks whether all elements in this array are a <code>Boolean</code> or not
     *
     * @see #getType()
     * @since 1.6-b
     */
    public boolean isBoolArray() {
        for (Class<?> cls : this.getFieldTypes()) if (cls != Boolean.class) return false;
        return true;
    }

    /**
     * Checks if this array has mixed types as content
     *
     * @return whether this array contains only mixed types and not a single one
     * @see #getType()
     * @since 1.6-b
     */
    public boolean isAnyArray() {
        return !this.isStringArray() && !this.isNumArray() && !this.isNullArray() && !this.isBoolArray();
    }

    private Class<?> @NotNull [] getFieldTypes() {
        Class<?>[] types = new Class[this.length()];
        for (int i = 0; i < this.length(); i++)
            types[i] = this.get(i) == null ? Object.class : this.values[i].getClass();
        return types;
    }

    /**
     * Checks whether this array has no elements or not
     *
     * @since 1.6-b
     */
    public boolean isEmpty() {
        return this.length() == 0;
    }

    /**
     * Counts the occurrences of the given object in this array
     *
     * @param o the object to look for
     * @return the amount of times the item is in the array
     * @since 1.6-b
     */
    public int getCountOf(@Nullable Object o) {
        int count = 0;
        for (Object o1 : this.get()) {
            if (o == null && o1 == null) count++;
            else if (o1 != null && o1.equals(o)) count++;
        }
        return count;
    }

    /**
     * Checks whether the given item occurs only once
     *
     * @param o the object to check
     * @return whether the item occurs once or not
     * @see #getCountOf(Object)
     * @see #has(Object)
     * @see #contains(Object)
     * @since 1.6-b
     */
    public boolean occursOnce(@Nullable Object o) {
        return this.getCountOf(o) == 1;
    }

    /**
     * Checks whether this array contains the element or not
     *
     * @param o the element to look for
     * @return whether the item occurs or not
     * @see #contains(Object)
     * @since 1.6-b
     */
    public boolean has(@Nullable Object o) {
        return this.getCountOf(o) > 0;
    }

    /**
     * Checks whether this array contains the element or not
     *
     * @param o the element to look for
     * @return whether the item occurs or not
     * @see #has(Object)
     * @since 1.6-b
     */
    public boolean contains(@Nullable Object o) {
        return this.getCountOf(o) > 0;
    }

    /**
     * Gets the items of the array
     *
     * @see #size()
     */
    @Override
    public Object[] get() {
        return this.values;
    }

    /**
     * Gets the length of the array
     */
    @Override
    public int length() {
        return this.values.length;
    }

    /**
     * Gets the length of the array
     *
     * @see #length()
     */
    @Override
    public int size() {
        return this.length();
    }


    /**
     * Returns a string representation of this array
     *
     * @param tabs the amount of tabs to be inserted before
     * @return a list of the items, coupled with the name of this object
     */
    @Override
    public @NotNull String stringify(int tabs) {
        StringBuilder s = new StringBuilder("\t".repeat(tabs) + this.name + ": [ ");
        int iMax = this.length() - 1;
        if (iMax == -1) return s.append("\b]").toString();
        for (int i = 0; ; i++) {
            s.append(this.get(i));
            if (i == iMax)
                return s.append(" ]").toString();
            s.append(", ");
        }
    }

    /**
     * Creates a new YamlArray based on a string
     *
     * @param s the string to parse
     * @return a new YamlArray if the string could be parsed, otherwise returns <code>null</code>
     */
    public static @Nullable YamlArray parse(String s) {
        Matcher m = Pattern.compile("^\\s*(?<name>[A-Za-z][\\w ]*):\\s?\\[(?<value>[^]]+)\\s?]").matcher(s);
        if (m.matches()) {
            String[] sValues = m.group("value").split(",");
            Object[] values = new Object[sValues.length];
            for (int i = 0; i < sValues.length; i++) {
                sValues[i] = sValues[i].trim();
                if (Pattern.matches("\"[^\"]+\"", sValues[i]))
                    values[i] = sValues[i];
                else if (Pattern.matches("[+-]?\\d+$", sValues[i]))
                    values[i] = Integer.parseInt(sValues[i]);
                else if (Pattern.matches("[+-]?\\d+(?>\\.[0-9]+)?(?>[eE][+-]\\d+)?", sValues[i]))
                    values[i] = Float.parseFloat(sValues[i]);
                else
                    values[i] = null;
            }
            return new YamlArray(m.group("name"), values);
        }
        return null;
    }
}
