package io.shiromi.yaml.util;

import io.shiromi.yaml.Yaml;

import io.shiromi.yaml.exception.YamlElementAlreadyPresentException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A yaml object containing a list of other {@link Yaml} objects
 *
 * @author Shiromi
 * @version 1.7
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
     * @throws YamlElementAlreadyPresentException if the item is already present in the object
     * @see #add(Yaml, Yaml...)
     */
    public int add(Yaml after, Yaml y) throws YamlElementAlreadyPresentException {
        if (this.contains(y))
            throw new YamlElementAlreadyPresentException("Element " + y + " already exists in this object");
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
     * @throws YamlElementAlreadyPresentException if the item is already present in the object
     * @see #add(Yaml, Yaml)
     * @since 1.2
     */
    public int add(Yaml after, Yaml @NotNull ... y) throws YamlElementAlreadyPresentException {
        int length = this.add(after, y[0]);
        for (int i = 1; i < y.length; i++) length = this.add(y[i - 1], y[i]);
        return length;
    }

    /**
     * Adds the specified item to the end of the array
     *
     * @param y the item to add to the end
     * @return the new length
     * @throws YamlElementAlreadyPresentException if the item is already present in the object
     * @see #add(Yaml, Yaml)
     * @see #add(Yaml, Yaml...)
     * @see #append(Yaml...)
     */
    public int append(Yaml y) throws YamlElementAlreadyPresentException {
        return this.add(this.getLast(), y);
    }

    /**
     * Adds the specified items to the end of the array
     *
     * @param y the items to add to the end
     * @return the new length
     * @throws YamlElementAlreadyPresentException if the item is already present in the object
     * @see #add(Yaml, Yaml)
     * @see #add(Yaml, Yaml...)
     * @see #append(Yaml)
     * @since 1.2
     */
    public int append(Yaml @NotNull ... y) throws YamlElementAlreadyPresentException {
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
     * @throws YamlElementAlreadyPresentException if the item is already present in the object
     * @see #replace(int, Yaml)
     * @since 1.5
     */
    public @Nullable Yaml replace(Yaml y, Yaml newY) throws YamlElementAlreadyPresentException {
        return this.replace(this.find(y), newY);
    }

    /**
     * Replace the item at specified index with the new item
     *
     * @param index the index of the item to replace
     * @param y     the new item to replace with
     * @return the old item
     * @throws YamlElementAlreadyPresentException if the item is already present in the object
     * @see #replace(Yaml, Yaml)
     * @since 1.5
     */
    public @Nullable Yaml replace(int index, Yaml y) throws YamlElementAlreadyPresentException {
        if (this.contains(y))
            throw new YamlElementAlreadyPresentException("Element " + y + " already exists in this object");
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
     * Gets an item in the object by its name
     *
     * @param name the name of the object
     * @return the object, if not found, returns <code>null</code>
     * @see #getByName(String, Class)
     * @since 1.7
     */
    public @Nullable Yaml getByName(String name) {
        for (Yaml y : this.get()) if (y.name.equals(name)) return y;
        return null;
    }

    /**
     * Gets an item in the object by its name and makes sure it's the class passed in
     *
     * @param name the name of the object
     * @param type the type of the object
     * @return the object if both parameters apply to it, otherwise returns <code>null</code>
     * @see #getByName(String)
     * @see #getString(String)
     * @see #getNumber(String)
     * @see #getBoolean(String)
     * @see #getNull(String)
     * @see #getArray(String)
     * @see #getObject(String)
     */
    public @Nullable Yaml getByName(String name, Class<? extends Yaml> type) {
        Yaml y = this.getByName(name);
        if (y == null) return null;
        Class<? extends Yaml> cls = y.getClass();
        if (cls == type) return y;
        return null;
    }

    /**
     * Gets a <code>YamlString</code> inside this object by its name
     *
     * @param name the name of the object
     * @return the string if found, otherwise returns <code>null</code>
     * @see #getByName(String, Class)
     * @since 1.7
     */
    public YamlString getString(String name) {
        return (YamlString) this.getByName(name, YamlString.class);
    }

    /**
     * Gets a <code>YamlNumber</code> inside this object by its name
     *
     * @param name the name of the object
     * @return the number if found, otherwise returns <code>null</code>
     * @see #getByName(String, Class)
     * @since 1.7
     */
    public YamlNumber getNumber(String name) {
        return (YamlNumber) this.getByName(name, YamlNumber.class);
    }

    /**
     * Gets a <code>YamlBoolean</code> inside this object by its name
     *
     * @param name the name of the object
     * @return the boolean if found, otherwise returns <code>null</code>
     * @see #getByName(String, Class)
     * @since 1.7
     */
    public YamlBoolean getBoolean(String name) {
        return (YamlBoolean) this.getByName(name, YamlBoolean.class);
    }

    /**
     * Gets a <code>YamlNull</code> inside this object by its name
     *
     * @param name the name of the object
     * @return the null type if found, otherwise returns <code>null</code>
     * @see #getByName(String, Class)
     * @since 1.7
     */
    public YamlNull getNull(String name) {
        return (YamlNull) this.getByName(name, YamlNull.class);
    }

    /**
     * Gets a <code>YamlArray</code> inside this object by its name
     *
     * @param name the name of the object
     * @return the array if found, otherwise returns <code>null</code>
     * @see #getByName(String, Class)
     * @since 1.7
     */
    public YamlArray getArray(String name) {
        return (YamlArray) this.getByName(name, YamlArray.class);
    }

    /**
     * Gets a <code>YamlObject</code> inside this object by its name
     *
     * @param name the name of the object
     * @return the object if found, otherwise returns <code>null</code>
     * @see #getByName(String, Class)
     * @since 1.7
     */
    public YamlObject getObject(String name) {
        return (YamlObject) this.getByName(name, YamlObject.class);
    }

    /**
     * Checks whether this object contains the given item or not
     *
     * @param y the item to search for
     * @return whether the item is in the list or not
     */
    public boolean contains(Yaml y) {
        if (y == null) return false;
        if (this.length() == 0) return false;
        for (Yaml y1 : this.values) if (y1.equals(y)) return true;
        return false;
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
     * Checks whether this object has no elements or not
     *
     * @since 1.6-b
     */
    public boolean isEmpty() {
        return this.length() == 0;
    }

    /**
     * Counts the amount of items of the given class in this object
     *
     * @param item the type of object to look for
     * @return the amount of items of the given class
     * @see #getCountOfStrings()
     * @see #getCountOfNumbers()
     * @see #getCountOfNullTypes()
     * @see #getCountOfBooleans()
     * @see #getCountOfArrays()
     * @see #getCountOfObjects()
     * @since 1.6-b
     */
    public int getCountOf(Class<? extends Yaml> item) {
        int count = 0;
        for (Yaml y : this.get()) if (item == y.getClass()) count++;
        return count;
    }

    /**
     * Counts the amount of {@link YamlString Strings} inside this object
     *
     * @return the amount of <code>Strings</code>
     * @see #getCountOf(Class)
     * @since 1.6-b
     */
    public int getCountOfStrings() {
        return this.getCountOf(YamlString.class);
    }

    /**
     * Counts the amount of {@link YamlNumber Numbers} inside this object
     *
     * @return the amount of <code>Numbers</code>
     * @see #getCountOf(Class)
     * @since 1.6-b
     */
    public int getCountOfNumbers() {
        return this.getCountOf(YamlNumber.class);
    }

    /**
     * Counts the amount of {@link YamlNull Null Types} inside this object
     *
     * @return the amount of <code>Null Types</code>
     * @see #getCountOf(Class)
     * @since 1.6-b
     */
    public int getCountOfNullTypes() {
        return this.getCountOf(YamlNull.class);
    }

    /**
     * Counts the amount of {@link YamlBoolean Booleans} inside this object
     *
     * @return the amount of <code>Booleans</code>
     * @see #getCountOf(Class)
     * @since 1.6-b
     */
    public int getCountOfBooleans() {
        return this.getCountOf(YamlBoolean.class);
    }

    /**
     * Counts the amount of {@link YamlArray Arrays} inside this object
     *
     * @return the amount of <code>Arrays</code>
     * @see #getCountOf(Class)
     * @since 1.6-b
     */
    public int getCountOfArrays() {
        return this.getCountOf(YamlArray.class);
    }

    /**
     * Counts the amount of {@link YamlObject Objects} inside this object
     *
     * @return the amount of <code>Objects</code>
     * @see #getCountOf(Class)
     * @since 1.6-b
     */
    public int getCountOfObjects() {
        return this.getCountOf(YamlObject.class);
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
     *      System.out.println(o.absoluteSize()); // prints 2
     *     }</pre>
     * </blockquote>
     *
     * @return the total size of this object added to the length of all non-primitive objects inside this
     * object
     * @see #size()
     * @see #length()
     */
    public int absoluteSize() {
        YamlObject[] content = new YamlObject[0];
        for (Yaml v : this.get()) {
            if (v.isObject()) {
                YamlObject[] newContent = new YamlObject[content.length + 1];
                System.arraycopy(content, 0, newContent, 0, content.length);
                newContent[content.length] = (YamlObject) v;
                content = newContent;
            }
        }
        int size = this.length();
        for (YamlObject v : content) size += v.absoluteSize();
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
        String begin = _items[0];
        String[] items = new String[_items.length - 1];
        System.arraycopy(_items, 1, items, 0, _items.length - 1);

        Pattern leading = Pattern.compile("^(?<leading>\\s*)");
        Pattern group = Pattern.compile("^\\s*(?<name>[A-Za-z][\\w ]*):$");

        Matcher m = group.matcher(begin);
        if (m.find()) {
            YamlObject o = new YamlObject(m.group("name"));
            Matcher m1 = leading.matcher(items[0]);
            int whitespaces = 0;
            if (m1.find()) whitespaces = m1.group("leading").length();
            if (whitespaces == 0) return null;

            for (int i = 0; i < items.length; i++) {
                Matcher m2 = group.matcher(items[i]);
                if (m2.find()) {
                    StringBuilder s1 = new StringBuilder(items[i].substring(whitespaces) + '\n');
                    int currentLevel = whitespaces;
                    for (int j = i + 1; j < items.length; j++) {
                        Matcher m3 = leading.matcher(items[j]);
                        if (m3.find()) currentLevel = m3.group("leading").length();
                        if (currentLevel == whitespaces) break;
                        s1.append(items[j].substring(whitespaces)).append('\n');
                    }
                    YamlObject o1 = parse(s1.substring(0, s1.length() - 1));
                    assert o1 != null;
                    i += o1.absoluteSize();
                    try {
                        o.append(o1);
                    } catch (YamlElementAlreadyPresentException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    String s2 = items[i].trim();
                    Yaml y = YamlString.parse(s2);
                    if (y == null) y = YamlNumber.parse(s2);
                    if (y == null) y = YamlBoolean.parse(s2);
                    if (y == null) y = YamlNull.parse(s2);
                    if (y == null) y = YamlArray.parse(s2);
                    try {
                        o.append(y);
                    } catch (YamlElementAlreadyPresentException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return o;
        }
        return null;
    }
}
