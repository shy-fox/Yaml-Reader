package io.shiromi.yaml.util;

import io.shiromi.yaml.Yaml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A yaml object containing a list of other {@link Yaml} objects
 *
 * @author Shiromi
 * @version 1.5
 */
public final class YamlObject extends Yaml {
    /**
     * The items of this object
     */
    public Yaml[] values;

    /**
     * Creates a new instance of this object, with an initial capacity
     *
     * @param name            the name of this object
     * @param initialCapacity the amount of items to be stored
     * @see #YamlObject(String)
     * @see #YamlObject(String, Yaml...)
     */
    public YamlObject(String name, int initialCapacity) {
        super(name);
        this.values = new Yaml[initialCapacity];
        Arrays.fill(this.values, null);
    }


    /**
     * Creates a new instance of this object, with an emtpy array
     *
     * @param name the name of this object
     * @see #YamlObject(String, int)
     * @see #YamlObject(String, Yaml...)
     */
    public YamlObject(String name) {
        this(name, 0);
    }

    /**
     * Creates a new instance of this object, with a given array
     *
     * @param name  the name of this object
     * @param items the items to set this object's value to
     * @see #YamlObject(String)
     * @see #YamlObject(String, int)
     * @since 1.2
     */
    public YamlObject(String name, Yaml @NotNull ... items) {
        this(name, items.length);
        this.values = items;
    }

    /**
     * Sets the items of this array to the passed in items
     *
     * @since 1.2
     */
    public int set(Yaml... items) {
        this.values = items;
        return this.length();
    }

    /**
     * Add a new item after the specified item
     *
     * @param after the item to find and to insert/add this item after
     * @param y     the item to add/insert
     * @return the new length of the array
     * @see #add(Yaml, Yaml...)
     */
    public int add(Yaml after, Yaml y) {
        int index = this.length();
        if (this.contains(after)) index = this.find(after);
        int j = 0;
        Yaml[] newValues = new Yaml[this.length() + 1];
        for (int i = 0; i < newValues.length; i++) {
            if (i == index) {
                newValues[i] = y;
                continue;
            }
            newValues[i] = this.getAt(j++);
        }
        this.values = newValues;
        return this.length();
    }

    /**
     * Adds the new items after the specified item
     *
     * @param after the item to find and to insert/add after
     * @param y     the items to add/insert
     * @return the new length of this array
     * @see #add(Yaml, Yaml)
     * @since 1.2
     */
    public int add(Yaml after, Yaml @NotNull ... y) {
        int length = this.add(after, y[0]);
        for (int i = 1; i < y.length; i++) length = this.add(y[i - 1], y[i]);
        return length;
    }

    /**
     * Adds the specified item to the end of the array
     *
     * @param y the item to add to the end
     * @return the new length
     * @see #add(Yaml, Yaml)
     * @see #add(Yaml, Yaml...)
     * @see #append(Yaml...)
     */
    public int append(Yaml y) {
        return this.add(this.getLast(), y);
    }

    /**
     * Adds the specified items to the end of the array
     *
     * @param y the items to add to the end
     * @return the new length
     * @see #add(Yaml, Yaml)
     * @see #add(Yaml, Yaml...)
     * @see #append(Yaml)
     * @since 1.2
     */
    public int append(Yaml @NotNull ... y) {
        int length = 0;
        for (Yaml y1 : y) length = this.append(y1);
        return length;
    }

    /**
     * Replace the specified with the given new item
     *
     * @param y    the item to find and replace
     * @param newY the new item to replace with
     * @return the old item
     * @see #replace(int, Yaml)
     * @since 1.5
     */
    public @Nullable Yaml replace(Yaml y, Yaml newY) {
        return this.replace(this.find(y), newY);
    }

    /**
     * Replace the item at specified index with the new item
     *
     * @param index the index of the item to replace
     * @param y     the new item to replace with
     * @return the old item
     * @see #replace(Yaml, Yaml)
     * @since 1.5
     */
    public @Nullable Yaml replace(int index, Yaml y) {
        if (0 > index || index >= this.length()) return null;
        Yaml y1 = this.getAt(index);
        this.values[index] = y;
        return y1;
    }

    /**
     * Remove the item at given index
     *
     * @param index the index of the item to remove
     * @return the removed item
     * @see #remove(int, int)
     * @see #pop()
     */
    public @Nullable Yaml remove(int index) {
        if (0 > index || index >= this.length()) return null;
        Yaml y = null;
        Yaml[] newValues = new Yaml[this.length() - 1];
        int j = 0;
        for (int i = 0; i < this.length(); i++) {
            if (i == index) {
                y = this.getAt(i);
                continue;
            }
            newValues[j++] = this.getAt(i);
        }
        this.values = newValues;

        return y;
    }

    /**
     * Remove <strong><code>length</code></strong> items after the given index
     *
     * @param index  the index of the first element to remove
     * @param length the amount of items to remove
     * @return the removed items
     * @see #remove(int)
     * @see #pop()
     */
    public Yaml @Nullable [] remove(int index, int length) {
        if (0 > index || index + length >= this.length()) return null;
        Yaml[] y = new Yaml[length];
        for (int i = index, j = 0; i < index + length; i++, j++) y[j] = this.remove(i);
        return y;
    }

    /**
     * Removes and returns the last element of this object
     *
     * @see #remove(int)
     * @see #remove(int, int)
     * @since 1.2
     */
    public Yaml pop() {
        return this.remove(this.length() - 1);
    }

    /**
     * A safe version to find the given item in this list
     *
     * @param y the item to find
     * @return the index of the item, if not found returns <code>-1</code>
     * @see #get(Yaml)
     * @since 1.2
     */
    public int find(Yaml y) {
        for (int i = 0; i < this.length(); i++) if (this.values[i].equals(y)) return i;
        return -1;
    }

    /**
     * Gets the index of the given item in this list
     *
     * @param y the item to find
     * @return the item of the index
     * @throws IllegalArgumentException if the item is not in the list
     */
    public int get(Yaml y) throws IllegalArgumentException {
        for (int i = 0; i < this.length(); i++) if (this.values[i].equals(y)) return i;
        throw new IllegalArgumentException("Item '" + y + "' is not contained inside the object");
    }

    /**
     * Gets the item at the index
     *
     * @param index the index of the item
     * @return the item at the list, if index is out of range, returns <code>null</code>
     */
    public @Nullable Yaml getAt(int index) {
        if (0 > index || index >= this.length()) return null;
        return this.values[index];
    }

    /**
     * Checks whether this object contains the given item or not
     *
     * @param y the item to search for
     * @return whether the item is in the list or not
     */
    public boolean contains(Yaml y) {
        if (y == null) return false;
        for (Yaml y1 : this.get()) if (!y1.equals(y)) return false;
        return true;
    }

    /**
     * Iterates through this object and allows for operations on each item
     * <blockquote>
     * <pre>{@code
     *      YamlObject o = ...;
     *      // ...
     *      o.forEach((i, k, v) -> {
     *          System.out.printf("Item %s at index @s is: %s", i, k, v);
     *      });
     *     }</pre>
     * </blockquote>
     *
     * @param iterator the function to apply to each item
     * @see io.shiromi.yaml.Yaml.YamlObjectIterator#item(int, String, Yaml)
     * @see YamlArray#forEach(YamlArrayIterator)
     */
    public void forEach(YamlObjectIterator iterator) {
        for (int i = 0; i < this.length(); i++) iterator.item(i, this.values[i].name, this.getAt(i));
    }

    /**
     * Gets the items of this object
     *
     * @return {@link #values}
     */
    @Override
    public Yaml[] get() {
        return this.values;
    }

    /**
     * Gets the last item of this object
     */
    public Yaml getLast() {
        return this.getAt(this.length() - 1);
    }

    /**
     * Gets the length of this object
     *
     * @see #size()
     * @see #absoluteSize()
     */
    @Override
    public int length() {
        return this.values.length;
    }

    /**
     * Gets the length of this object
     *
     * @see #length()
     * @see #absoluteSize()
     * @deprecated 1.3
     */
    @Override
    @Deprecated
    public int size() {
        return this.length();
    }

    /**
     * Gets the size of this object added to the total size of each {@link #isPrimitive() non-primitive} item in this object, e.g.
     * <blockquote>
     * <pre>{@code
     *      YamlObject o = new YamlObject("object1");
     *      YamlArray a = new YamlArray("array", new Object[]{ null, null });
     *      YamlObject o1 = new YamlObject("object2",
     *          new YamlString("str"),
     *          new YamlNumber("int", 1));
     *      o.set(a, o1);
     *      System.out.println(o.absoluteSize()); // prints 6
     *     }</pre>
     * </blockquote>
     *
     * @return the total size of this object added to the length of all non-primitive objects inside this
     * object
     * @see #size()
     * @see #length()
     */
    public int absoluteSize() {
        Yaml[] content = new Yaml[0];
        int j = 0;
        for (Yaml v : this.get()) {
            if (!v.isPrimitive()) {
                Yaml[] newContent = new Yaml[content.length + 1];
                System.arraycopy(content, 0, newContent, 0, content.length);
                newContent[content.length] = v;
                content = newContent;
            }
        }
        int size = this.length();
        for (Yaml v : content) size += v.length();
        return size;
    }

    /**
     * Returns a string representation of this object
     *
     * @param tabs the amount of tabs to be inserted before
     * @return a string representing this object with its name and a string representation of each
     * contained object
     */
    @Override
    @NotNull
    public String stringify(int tabs) {
        StringBuilder s = new StringBuilder("\t".repeat(tabs) + this.name + ":\n");
        for (Yaml y : this.get()) s.append(y.stringify(tabs + 1)).append('\n');
        return s.substring(0, s.length() - 1);
    }

    /**
     * Creates new YamlObject based on a string
     *
     * @param s the string to parse
     * @return a new YamlObject with its contents if the string could be parsed, otherwise returns
     * <code>null</code>
     */
    public static @Nullable YamlObject parse(@NotNull String s) {
        String[] _items = s.split("\\n");
        String[] items = new String[_items.length - 1];
        System.arraycopy(_items, 1, items, 0, _items.length - 1);

        Matcher m = Pattern.compile("^\\s*(?<name>[a-z]\\w*):$").matcher(_items[0]);
        if (m.matches()) {
            YamlObject o = new YamlObject(m.group("name"));
            for (int i = 0; i < items.length; i++) {
                if (Pattern.matches("^\\s*(?<name>[a-z]\\w*):$", items[i])) {
                    int j = 0, prevJ;
                    StringBuilder sb = new StringBuilder();
                    for (int k = i; k < items.length; k++) {
                        Matcher m1 = Pattern.compile("(?<leading>^\\s*)").matcher(items[k]);
                        prevJ = j;
                        if (m1.find()) j = m1.group("leading").length();
                        if (prevJ > j) break;
                        sb.append(items[k]).append('\n');
                    }
                    YamlObject o1 = YamlObject.parse(sb.substring(0, sb.length() - 1));
                    o.append(o1);
                    assert o1 != null;
                    i += o1.absoluteSize();
                } else {
                    String s1 = items[i].trim();
                    Yaml y = YamlString.parse(s1);
                    if (y == null) y = YamlNumber.parse(s1);
                    if (y == null) y = YamlBoolean.parse(s1);
                    if (y == null) y = YamlNull.parse(s1);
                    if (y == null) y = YamlArray.parse(s1);
                    o.append(y);
                }
            }
            return o;
        }
        return null;
    }
}
