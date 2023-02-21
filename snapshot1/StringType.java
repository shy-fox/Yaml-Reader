package io.shiromi.saml.types;

import io.shiromi.saml.exceptions.RangeException;

import io.shiromi.saml.functions.StringIterator;

import io.shiromi.saml.tools.Range;
import org.intellij.lang.annotations.RegExp;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Instances of the class <code>StringType</code> carry a value of type <code>String</code> and behave similar to it,
 * as they contain a string as value and allow for easy access via similar methods of <code>String</code>.
 * <p></p>
 * However, unlike the class <code>String</code>, this object it iterable and allows for other things <code>String</code>
 * can't do, such as inserting {@link #insert(char, int) characters} or {@link #insert(String, int) strings} at a specific point.
 * As already mentioned, the key difference to <code>String</code> is that this class implements <code>Iterable</code>, therefore
 * marking it as <em>iterable</em>, whereas <code>Strings</code> can only be iterated through by using a normal or enhanced {@code for loop}.
 * <p></p>
 * Valid arguments this object accepts for constructors are:
 * <ul>
 *     <li><code>&lt;no parameter&gt;</code></li>
 *     <li>{@code char}</li>
 *     <li>{@code char[]}</li>
 *     <li><code>String</code>, {@code boolean}</li>
 *     <li><code>String</code></li>
 *     <li><code>StringBuilder</code></li>
 * </ul>
 *
 * @author Shiromi
 * @version 1.0
 */
public final class StringType extends AbstractType<String> implements Iterable<Character> {
    private int lastIndex;              // store last index of string
    private int length;                 // store the length of ths string/chars

    private transient char[] chars;     // store each character
    private transient int modCount = 0; // count modifications, useful in Itr

    /**
     * Creates a new instance of a <code>StringType</code> with a value and optional trimming of the value, which will remove
     * leading and trailing whitespaces, essentially doing the same as {@code new StringType("  foo  ".trim())},
     * which saves using the method {@link #trim()} and changes not just the <code>{@link #value}</code>, but also other variables
     * this class has
     * <blockquote>
     * <pre>{@code
     *      StringType st = new StringType("  foo bar  ", true);    // will result in a value of "foo bar"
     *      st = new StringType("  foo bar  ").trim();              // does the same as above
     *     }</pre>
     * </blockquote>
     *
     * @param value the <code>String</code> value of this object
     * @param trim  optional trimming of the string, essentially applying the method {@link #trim()}
     * @see #StringType()
     * @see #StringType(char[])
     * @see #StringType(char)
     * @see #StringType(String)
     * @see #StringType(StringBuilder)
     */
    public StringType(String value, boolean trim) {
        super(value);
        length = value.length();
        lastIndex = length - 1;
        chars = value.toCharArray();

        if (trim) {
            char[] cs = value.toCharArray();
            value = trimSize(cs);
            this.value = value;
            length = value.length();
            lastIndex = length - 1;
            chars = value.toCharArray();
        }
    }

    /**
     * Creates a new instance of <code>StringType</code> with a value, which can be trimmed by using {@link #StringType(String, boolean)}
     * or {@link #trim()}, since this constructor does not trim the string by default. Use this if you want to append or insert in loops
     *
     * @param value the <code>String</code> value of this object
     * @see #StringType()
     * @see #StringType(char[])
     * @see #StringType(char)
     * @see #StringType(StringBuilder)
     * @see #StringType(String, boolean)
     */
    public StringType(String value) {
        this(value, false);
    }

    /**
     * Creates a new instance of <code>StringType</code> from a <code>StringBuilder</code>, essentially applying the method <code>toString()</code>
     * to the given <code>StringBuilder</code> and taking the value from it
     *
     * @param sb the <code>StringBuilder</code> to get the value from
     * @see #StringType()
     * @see #StringType(char[])
     * @see #StringType(char)
     * @see #StringType(String)
     * @see #StringType(String, boolean)
     */
    public StringType(@NotNull StringBuilder sb) {
        this(sb.toString());
    }

    /**
     * Creates a new instance of <code>StringType</code> by taking a {@code char[]} and setting it to this object's <em>char array</em>,
     * behaving essentially the same as {@link #StringType(String)}
     *
     * @param data an array of characters this object should have as value
     * @see #StringType()
     * @see #StringType(char)
     * @see #StringType(String)
     * @see #StringType(StringBuilder)
     * @see #StringType(String, boolean)
     */
    public StringType(char[] data) {
        this(String.valueOf(data));
    }

    /**
     * Creates a new instance of <code>StringType</code> by taking a {@code char} and setting it to this object's <em>char array</em>,
     * behaving essentially the same as {@link #StringType(String)}
     *
     * @param c an array of characters this object should have as value
     * @see #StringType()
     * @see #StringType(char[])
     * @see #StringType(String)
     * @see #StringType(StringBuilder)
     * @see #StringType(String, boolean)
     */
    public StringType(char c) {
        this("" + c);
    }

    /**
     * Creates a new instance of <code>StringType</code> with an emtpy <code>String</code> as value, setting it to {@code "" }.
     * Use this constructor for either appending to it inside a loop or if you want to set it later
     *
     * @see #StringType()
     * @see #StringType(char[])
     * @see #StringType(String)
     * @see #StringType(StringBuilder)
     * @see #StringType(String, boolean)
     */
    public StringType() {
        this("");
        length = 0;
        lastIndex = 0;
    }

    /**
     * Remove leading and trailing whitespaces from the value
     *
     * @return a new <code>StringType</code> with leading and trailing whitespaces removed
     */
    @Contract(" -> new")
    public @NotNull StringType trim() {
        char[] cs = chars;
        return new StringType(trimSize(cs));
    }

    private static @NotNull String trimSize(char @NotNull [] cs) {
        char c = cs[0];

        while (c == 0x00 || c == 0x09 || c == 0x20 || c == 0x0B) {
            char[] content = new char[cs.length - 1];
            System.arraycopy(cs, 1, content, 0, cs.length - 1);
            cs = content;
            c = cs[0];
        }

        c = cs[cs.length - 1];
        while (c == 0x00 || c == 0x09 || c == 0x20 || c == 0x0B) {
            char[] content = new char[cs.length - 1];
            System.arraycopy(cs, 0, content, 0, cs.length - 1);
            cs = content;
            c = cs[cs.length - 1];
        }

        return String.valueOf(cs);
    }

    @Contract(pure = true)
    private static char charAt(char @NotNull [] chars, int index) {
        return chars[index];
    }

    @Contract(pure = true)
    private static String @NotNull [] charRange(char @NotNull [] chars, int range) {
        int length = chars.length;
        int segCount = Math.ceilDiv(length, range);

        int cur = 0;

        String[] segments = new String[segCount];
        int cursor = 0;

        while (cursor < segCount) {

            int rem = length - cur;

            int r = Math.min(rem, range);

            char[] segment = new char[r];
            System.arraycopy(chars, cur, segment, 0, r);

            cur += range;
            cur = Math.min(cur, length);

            segments[cursor] = String.valueOf(segment);

            cursor++;
        }

        return segments;
    }

    /**
     * Checks whether this object contains the specified character
     *
     * @param c the {@code char} to look for
     * @return whether it contains it or not
     * @see #contains(String)
     */
    public boolean contains(char c) {
        return find(chars, c) >= 0;
    }

    /**
     * Checks whether this object contains the specified substring
     *
     * @param substring the <code>substring</code> to look for
     * @return whether this object contains the specified <code>String</code> or not
     * @see #contains(char)
     */
    @Contract(pure = true)
    public boolean contains(@NotNull String substring) {
        return indexOf(substring) > -1;
    }

    @Contract(pure = true)
    private static int find(char @NotNull [] cs, char c) {
        for (int i = 0; i < cs.length; i++) if (cs[i] == c) return i;
        return -1;
    }

    /**
     * Looks for the specified character and returns the index of the first occurrence in this <code>String</code>
     *
     * @param c the {@code char} to look for and get the index of
     * @return the index of the specified {@code char}, if it cannot find it, returns <code>-1</code>
     * @see #indexOf(char)
     * @see #indexesOf(char)
     * @see #findLast(char)
     * @see #lastIndexOf(char)
     */
    public int find(char c) {
        return indexOf(c);
    }

    /**
     * Looks for the specified <code>String</code> in this object and returns the range of the first match if found
     *
     * @param s the <code>String</code> to look for
     * @return a range of the first match if found, if the string isn't contained inside this object, returns {@code null}
     */
    public @Nullable Range find(String s) {
        int start = indexOf(s);
        if (start == -1) return null;
        return new Range(start, start + s.length());
    }

    /**
     * Looks for the specified character and returns the index of the last occurrence in this <code>String</code>
     *
     * @param c the {@code char} to look for and the index of the last occurrence of
     * @return the index of the last occurrence of the specified {@code char}, if it cannot find it, returns <code>-1</code>
     * @see #find(char)
     * @see #indexOf(char)
     * @see #indexesOf(char)
     * @see #lastIndexOf(char)
     */
    public int findLast(char c) {
        return lastIndexOf(c);
    }

    /**
     * Gets a list of all indexes of the specified {@code char}
     *
     * @param c the {@code char} to look for
     * @return an array containing each index
     * @see #indexesOf(char)
     */
    public int @NotNull [] findAll(char c) {
        return indexesOf(c);
    }

    /**
     * Returns an array of {@link Range ranges} containing the start and end index of the searched for <code>String</code>
     *
     * @param s the <code>String</code> to look for
     * @return an array of ranges containing the start and end indexes
     */
    public Range[] findAll(String s) {
        Range[] ranges = new Range[0];
        int lastIndexOf = 0;

        while ((lastIndexOf = indexOf(s, lastIndexOf)) > -1) {
            Range[] nRanges = new Range[ranges.length + 1];
            System.arraycopy(ranges, 0, nRanges, 0, ranges.length);
            nRanges[ranges.length] = new Range(lastIndexOf, lastIndexOf + s.length());
            lastIndexOf += s.length();
            ranges = nRanges;
        }

        return ranges;
    }

    /**
     * Returns a new <code>StringType</code> containing a section of the <code>value</code> starting at the specified index
     *
     * @param start the start index, inclusive
     * @return a new <code>StringType</code> with the specified substring
     * @see #substring(int, int)
     */
    public @NotNull StringType substring(int start) {
        return substring(start, length);
    }

    /**
     * Returns a new <code>StringType</code> containing a section of the <code>value</code> starting at the specified index
     * and ending at the specified ending index
     *
     * @param start the start index, inclusive
     * @param end   the end index. exclusive
     * @return a new <code>StringType</code> with the specified substring
     * @see #substring(int)
     */
    public @NotNull StringType substring(int start, int end) {
        if (start > end) throw new RangeException("Start: " + start + " > End: " + end);
        char[] cs = chars;
        StringBuilder s = new StringBuilder();
        for (int i = start; i < end; i++) s.append(cs[i]);
        StringType t = new StringType(s);
        t.modCount = 0;
        return t;
    }

    /**
     * Splits the <code>String</code> value of this object into sections of the specified length
     *
     * @param count the length of each section, should be greater than 0
     * @return an array containing sections of the specified length
     */
    @Contract(pure = true)
    public String @NotNull [] splitEach(int count) {
        char[] cs = chars;
        return charRange(cs, count);
    }

    /**
     * Checks if the <code>String</code> value of this object matches the specified pattern
     *
     * @param regex either a string or a regular expression the match this object to
     * @return whether this object matches to the specified value or not
     */
    public boolean matches(@RegExp String regex) {
        return Pattern.compile(regex).matcher(value).matches();
    }

    /**
     * Looks for the specified character and returns the index of the first occurrence in this <code>String</code>
     *
     * @param c the {@code char} to look for and get the index of
     * @return the index of the specified {@code char}, if it cannot find it, returns <code>-1</code>
     * @see #find(char)
     * @see #indexesOf(char)
     * @see #findLast(char)
     * @see #lastIndexOf(char)
     */
    public int indexOf(char c) {
        return indexOfRange(c, 0, length);
    }

    /**
     * Looks for the specified <code>String</code> and returns the index of the first occurrence in this <code>String</code>
     *
     * @param s the <code>String</code> to look for and get the index of
     * @return the index of the specified <code>String</code>, if it cannot find it, returns <code>-1</code>
     * @see #indexOf(String, int)
     */
    public int indexOf(@NotNull String s) {
        return indexOf(s, 0, lastIndex);
    }

    /**
     * Looks for the specified <code>String</code> and returns the index of the first occurrence after the specified index
     *
     * @param s    the <code>String</code> to look for
     * @param from the index from which to start searching
     * @return the index of the searched for <code>String</code>, if no occurrence is found, returns <code>-1</code>
     * @see #indexOf(String)
     */
    public int indexOf(String s, int from) {
        return indexOf(s, from, lastIndex);
    }

    /**
     * Returns the last index of the given <code>String</code>
     *
     * @param s the <code>String</code> to search for
     * @return the last index of the <code>String</code>, if no occurrence is found, returns <code>-1</code>
     */
    public int lastIndexOf(String s) {
        Range[] tmp = findAll(s);
        if (tmp.length == 0) return -1;
        return tmp[tmp.length - 1].start();
    }

    private int indexOf(@NotNull String s, int from, int to) {
        char[] cs = s.toCharArray();

        int[] groupAll = findAll(cs[0]);

        if (groupAll.length == 0 ||
                from == lastIndex ||
                from == to ||
                from + cs.length > length) return -1;


        if (groupAll.length == 1 && groupAll[0] < from) return -1;

        int countToRemove = 0, j = groupAll.length - 1, start;
        while (groupAll[countToRemove] < from) {
            if (countToRemove == j) return -1;
            countToRemove++;
        }


        start = countToRemove;
        while (groupAll[j] > to) {
            countToRemove++;
            j--;
        }

        int[] all = new int[groupAll.length - countToRemove];
        System.arraycopy(groupAll, start, all, 0, all.length);

        int index = -1;

        for (int idx : all) {
            if (idx + cs.length > length) continue;
            for (int i = 1; i < cs.length; i++) {
                if (cs[i] != chars[idx + i]) break;
                if (i == cs.length - 1) {
                    index = idx;
                    break;
                }
            }
            break;
        }

        return index;
    }

    /**
     * Looks for the specified character and returns the index of the last occurrence in this <code>String</code>
     *
     * @param c the {@code char} to look for and the index of the last occurrence of
     * @return the index of the last occurrence of the specified {@code char}, if it cannot find it, returns <code>-1</code>
     * @see #find(char)
     * @see #indexOf(char)
     * @see #indexesOf(char)
     * @see #findLast(char)
     */
    public int lastIndexOf(char c) {
        return lastIndexOfRange(c, 0, length);
    }

    /**
     * Gets a list of all indexes of the specified {@code char}
     *
     * @param c the {@code char} to look for
     * @return an array containing each index
     * @see #findAll(char)
     */
    public int @NotNull [] indexesOf(char c) {
        int lastIndex = 0;

        int[] indexes = new int[0];
        int i;

        while ((i = indexOfRange(c, lastIndex, length)) != -1) {
            indexes = grow(indexes, i);
            lastIndex = i + 1;
        }

        return indexes;
    }

    private int indexOfRange(char c, int from, int to) {
        char[] cs = chars;
        for (int i = from; i < to; i++) if (cs[i] == c) return i;
        return -1;
    }

    private int lastIndexOfRange(char c, int from, int to) {
        char[] cs = chars;
        for (int i = to - 1; i > from; i--) if (cs[i] == c) return i;
        return -1;
    }

    private int @NotNull [] grow(int @NotNull [] ints, int item) {
        int[] newInts = new int[ints.length + 1];
        int length = ints.length;
        System.arraycopy(ints, 0, newInts, 0, ints.length);
        newInts[length] = item;
        return newInts;
    }

    /**
     * Removes the character at the specified index
     *
     * @param index the index of the object to remove
     * @return the {@code char} which was removed
     * @see #remove(char)
     * @see #removeChars(char[])
     * @see #removeChars(int, int)
     * @see #removeString(String)
     */
    public char remove(int index) {
        Objects.checkIndex(index, length);
        final char[] cs = chars;

        char oldValue = cs[index];
        fastRemove(cs, index, 0);

        return oldValue;
    }

    /**
     * Removes the first occurrence of specified character
     *
     * @param c the character to look for and remove
     * @return whether the character is found and removed or not
     * @see #remove(int)
     * @see #removeChars(char[])
     * @see #removeChars(int, int)
     * @see #removeString(String)
     */
    public boolean remove(char c) {
        final char[] cs = chars;
        final int length = this.length;
        int i = 0;

        found:
        {
            for (; i < length; i++) if (cs[i] == c) break found;
            return false;
        }

        fastRemove(cs, i, 0);
        return true;
    }

    /**
     * Removes the characters in the specified range after the start index
     *
     * @param start the start index from which to remove
     * @param range the amount of items to remove
     * @return a {@code char} array containing the removed elements
     * @see #remove(char)
     * @see #remove(int)
     * @see #removeChars(char[])
     * @see #removeString(String)
     */
    public char @NotNull [] removeChars(int start, int range) {
        final char[] cs = chars;
        char[] oldChars = new char[range];
        System.arraycopy(chars, start, oldChars, 0, range);
        fastRemove(cs, start, range - 1);
        return oldChars;
    }

    /**
     * Removes all characters specified in the list, breaks loop if it cannot find a specified character
     *
     * @param chars an array of characters to remove, do not have to be in order
     * @return {@code true} if every item could be removed, {@code false} if one item failed to be removed
     * @see #remove(char)
     * @see #remove(int)
     * @see #removeChars(int, int)
     * @see #removeString(String)
     */
    public boolean removeChars(char @NotNull [] chars) {
        for (char c : chars) if (!remove(c)) return false;
        return true;
    }

    /**
     * Removes the specified <code>String</code>'s first occurrence from this object
     *
     * @param string the <code>String</code> to remove from this object
     * @return {@code true} if the specified <code>String</code> could be found and removed, if not returns {@code false}
     * @see #remove(char)
     * @see #remove(int)
     * @see #removeChars(char[])
     * @see #removeChars(int, int)
     */
    public boolean removeString(@NotNull String string) {
        char[] cs = chars;

        Matcher m = Pattern.compile(string).matcher(value);
        if (m.find()) {
            int l = string.length();
            int begin = m.start();

            fastRemove(cs, begin, l - 1);

            return true;
        }

        return false;
    }

    /**
     * Insert the specified {@code char} at the specified index
     *
     * @param c     the character to add
     * @param index the index at which to add the specified character
     * @see #add(char)
     * @see #add(char, int)
     * @see #insert(String, int)
     * @see #insert(String, String)
     */
    public void insert(char c, int index) {
        add(c, index);
    }

    /**
     * Inserts the specified <code>String</code> after the searched for <code>String</code>
     *
     * @param string the <code>String</code> to insert
     * @param after  the <code>String</code> to insert after
     * @see #add(char)
     * @see #add(char, int)
     * @see #insert(char, int)
     * @see #insert(String, int)
     */
    public void insert(String string, String after) {
        if (!contains(after)) return;
        char[] cs = after.toCharArray();

        // get last index of after
        int lastIndex = -1;
        for (int j : indexesOf(after.charAt(0))) {
            for (int i = 0; i < cs.length; i++) {
                if (cs[i] != chars[j + i]) break;
                if (i == cs.length - 1) lastIndex = i;
            }
            if (lastIndex != -1) break;
        }

        insert(string, lastIndex + 1);
    }

    /**
     * Inserts the specified <code>String</code> at the specified index
     *
     * @param string the <code>String</code> to insert
     * @param index  the index at which to insert, the first character will be at the specified index
     * @see #add(char)
     * @see #add(char, int)
     * @see #insert(char, int)
     * @see #insert(String, String)
     */
    public void insert(@NotNull String string, int index) {
        char[] cs = chars;
        char[] sc = string.toCharArray();
        fastAdd(cs, sc, index);
    }

    /**
     * Adds the specified {@code char} at the specified index
     *
     * @param c     the character to add
     * @param index the index at which to add the specified character
     * @return {@code true} in any case
     * @see #add(char)
     * @see #insert(String, int)
     * @see #insert(String, String)
     * @see #append(char)
     * @see #append(char[])
     * @see #append(String)
     * @see #appendLine(String)
     */
    public boolean add(char c, int index) {
        char[] cs = chars;
        fastAdd(cs, new char[]{c}, index);
        return true;
    }

    /**
     * Adds the specified {@code char} at the end of the <code>String</code>
     *
     * @param c the character to add
     * @return {@code true} in any case
     * @see #add(char, int)
     * @see #append(char)
     * @see #append(char[])
     * @see #append(String)
     */
    public boolean add(char c) {
        return add(c, length);
    }

    /**
     * Appends the specified {@code char} to this <code>String</code>
     *
     * @param c the character to add
     * @return {@code true} in any case
     * @see #add(char)
     * @see #append(char[])
     * @see #append(String)
     */
    public boolean append(char c) {
        return add(c);
    }

    /**
     * Appends the given {@link StringType} to this <code>String</code>
     *
     * @param value the value to add to the end of this string
     * @return {@code true} regardless
     * @see #append(char)
     * @see #append(char[])
     * @see #append(String)
     */
    @Contract(pure = true)
    public boolean append(@NotNull StringType value) {
        return append(value.chars);
    }

    /**
     * Appends the specified <code>String</code> to this <code>String</code>
     *
     * @param string the <code>String</code> to add
     * @return {@code true} in any case
     * @see #append(char)
     * @see #append(char[])
     * @see #append(StringType)
     * @see #appendLine(String)
     */
    @Contract(pure = true)
    public boolean append(@NotNull String string) {
        return append(string.toCharArray());
    }

    /**
     * Appends the specified <code>String</code> with a following <code>new line</code> character to this <code>String</code>
     *
     * @param string the <code>String</code> to add
     * @return {@code true} in any case
     * @see #append(char)
     * @see #append(char[])
     * @see #append(String)
     */
    public boolean appendLine(String string) {
        return append(string + "\n");
    }

    /**
     * Appends the specified {@code char[]} to this <code>String</code>
     *
     * @param chars the {@code char[]} to add
     * @return {@code true} in any case
     * @see #append(char)
     * @see #append(String)
     */
    public boolean append(char @NotNull [] chars) {
        char[] cs = this.chars;

        fastAdd(cs, chars, length);

        return true;
    }

    public boolean append(@NotNull Object o) {
        char[] cs = this.chars;
        char[] ocs = o.toString().toCharArray();

        fastAdd(cs, ocs, length);

        return true;
    }

    /**
     * Appends the specified {@code char} <code>count</code> times to this <code>String</code>
     *
     * @param c     the {@code char} to add
     * @param count the amount of times to add this character, should be greater than <code>0</code>
     * @return <code>count > 0</code>
     * @see #repeatAppend(String, int)
     */
    public boolean appendRepeat(char c, int count) {
        if (count <= 0) return false;
        char[] cs = new char[count];
        Arrays.fill(cs, c);
        return append(cs);
    }

    /**
     * Appends the specified <code>String</code> <code>count</code> times to this <code>String</code>
     *
     * @param string the <code>String</code> to add
     * @param count  the amount of times to add the given <code>String</code>, should be greater than <code>0</code>
     * @return <code>count > 0</code>
     * @see #appendRepeat(char, int)
     */
    @Contract(pure = true)
    public boolean repeatAppend(@NotNull String string, int count) {
        if (count <= 0) return false;
        int length = string.length();
        char[] cs = new char[length * count];
        char[] sc = string.toCharArray();
        for (int i = 0; i < cs.length; i++) cs[i] = sc[i % length];
        return append(cs);
    }

    @Contract(pure = true)
    private void fastAdd(char @NotNull [] cs, char @NotNull [] c, int i) {
        int range = c.length;
        Objects.checkIndex(i, cs.length + 1);

        modCount++;

        char[] copy = new char[cs.length + range];

        if (i >= 0) System.arraycopy(cs, 0, copy, 0, i);
        for (int j = i, k = 0; k < range; j++, k++) copy[j] = c[k];
        for (int j = i + range, k = i; k < cs.length; j++, k++) copy[j] = cs[k];

        chars = copy;
        update();
    }

    private void fastRemove(char[] content, int index, int range) {
        if (range < 0) throw new IndexOutOfBoundsException();
        modCount++;
        char[] newContent = new char[length - range - 1];

        int j = 0;

        if (length - range > index)
            for (int i = 0; i < length; i++) {
                if (i >= index && i <= index + range) continue;
                newContent[j++] = content[i];
            }
        else newContent = content;

        chars = newContent;
        update();
    }

    /**
     * Returns the character at the specified index
     *
     * @param index the index to get the character at
     * @return the character at given index
     */
    public char charAt(int index) {
        Objects.checkIndex(index, length);
        char[] cs = chars;
        return charAt(cs, index);
    }

    /**
     * Checks whether this object starts with the given prefix
     *
     * @param prefix the prefix
     * @return whether this object starts with the given prefix or not
     * @see #startsWith(char)
     */
    @Contract(pure = true)
    public boolean startsWith(@NotNull String prefix) {
        int firstIndex = 0;
        int i = 0;

        char[] p = prefix.toCharArray();

        while (firstIndex < p.length) {
            if (chars[firstIndex++] != p[i++]) return false;
        }

        return true;
    }

    /**
     * Checks whether this object starts with the given prefix
     *
     * @param prefix the prefix
     * @return whether this object starts with the given prefix or not
     * @see #startsWith(String)
     */
    public boolean startsWith(char prefix) {
        return chars[0] == prefix;
    }

    /**
     * Checks whether this object ends with the given suffix
     *
     * @param suffix the suffix
     * @return whether this object ends with the given suffix or not
     * @see #endsWith(char)
     */
    @Contract(pure = true)
    public boolean endsWith(@NotNull String suffix) {
        int lastIndex = length - suffix.length();
        int i = 0;

        char[] s = suffix.toCharArray();

        while (lastIndex < length) {
            if (chars[lastIndex++] != s[i++]) return false;
        }

        return true;
    }

    /**
     * Checks whether this object ends with the given suffix
     *
     * @param suffix the suffix
     * @return whether this object ends with the given suffix or not
     * @see #endsWith(String)
     */
    public boolean endsWith(char suffix) {
        return chars[lastIndex] == suffix;
    }

    /**
     * Returns ths length of this object's value
     *
     * @return the length of this object's value
     */
    public int length() {
        return length;
    }

    /**
     * Returns this object's character array
     *
     * @return this object's character array
     */
    public char[] toCharArray() {
        return chars;
    }

    /**
     * Returns the last character of this object's value
     *
     * @return the last character of this object's value
     */
    public char getLast() {
        return charAt(chars, lastIndex);
    }

    /**
     * Iterates through each character with a specified function and applies it to it
     *
     * @param iterator the iterator with the specified method {@link StringIterator#apply(String)}
     * @see #forCharRange(int, StringIterator)
     */
    public void forEachChar(StringIterator iterator) {
        forCharRange(1, iterator);
    }

    /**
     * Iterates through segments specified by {@link #splitEach(int) splitEach(range)} and applies the specified function to it
     *
     * @param range    the size of each segment
     * @param iterator the iterator with the specified method {@link StringIterator#apply(String)}
     */
    public void forCharRange(int range, StringIterator iterator) {
        for (String s : charRange(chars, range)) iterator.apply(s);
    }

    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Splits a string by a given delimiter and returns an array of substrings matched around the given delimiter,
     * the parameter <em>limit</em> specifies
     * the result threshold:
     * <blockquote>
     *     <ul>
     *         <li>If <em>limit</em> is positive, the delimiter will be applied to at most <em>limit - 1</em>,
     *         the rest of the string will be added to the last entry of the array</li>
     *         <li>if <em>limit</em> is equal to <code>0</code>, the delimiter will be applied as many time
     *         as possible, removing any trailing empty entries</li>
     *         <li>if <em>limit</em> is negative, the delimiter will be applied as many times as possible,
     *         while leaving any trailing empty spaces</li>
     *     </ul>
     * </blockquote>
     *
     * @param delimiter the delimiter to split by
     * @param limit     the result threshold, as described above
     * @return an array of substrings matched around the delimiter
     * @see #split(char)
     * @see #split(char, int)
     * @see #split(String)
     */
    public StringType @NotNull [] split(@NotNull String delimiter, int limit) {
        char[] split = delimiter.toCharArray();
        StringType[] container;

        int length = split.length;

        if (length == 1) {
            // mark as char
            int[] counts = findAll(split[0]);

            if (counts.length == 0) return new StringType[]{this};

            // check bound: limit = 0 -> all
            int range = limit <= 0 ? counts.length : Math.min(counts.length, limit);

            container = new StringType[]{substring(0, counts[0])};

            for (int i = 0; i < range - 1; i++) {
                StringType[] nContainer = new StringType[container.length + 1];
                System.arraycopy(container, 0, nContainer, 0, container.length);
                nContainer[container.length] = substring(counts[i] + 1, counts[i + 1]);
                container = nContainer;
            }

            StringType[] nContainer = new StringType[container.length + 1];
            System.arraycopy(container, 0, nContainer, 0, container.length);
            nContainer[range] = substring(counts[range - 1] + 1);
            container = nContainer;
        } else {
            Range[] ranges = findAll(delimiter);
            container = new StringType[]{substring(0, ranges[0].start())};

            // check bound: limit = 0 -> all
            int range = limit == 0 ? ranges.length : Math.max(0, Math.min(ranges.length, limit));

            for (int i = 0; i < range - 1; i++) {
                StringType[] nContainer = new StringType[container.length + 1];
                System.arraycopy(container, 0, nContainer, 0, container.length);
                nContainer[container.length] = substring(ranges[i].end(), ranges[i + 1].start());
                container = nContainer;
            }

            StringType[] nContainer = new StringType[container.length + 1];
            System.arraycopy(container, 0, nContainer, 0, container.length);
            nContainer[container.length] = substring(ranges[range - 1].end());
            container = nContainer;
        }

        if (limit == 0) {
            StringType[] rem = container;
            while (rem[rem.length - 1].isEmpty()) {
                StringType[] nRem = new StringType[rem.length - 1];
                System.arraycopy(rem, 0, nRem, 0, nRem.length);
                rem = nRem;
            }
            container = rem;
        }

        return container;
    }

    /**
     * Splits this object around matches of the given pattern.
     * The array returned by this method contains each substring of this object
     * that is terminated by another subsequence that matches the given pattern,
     * the parameter <em>limit</em> specifies the result threshold:
     * <blockquote>
     *     <ul>
     *         <li>If <em>limit</em> is positive, the delimiter will be applied to at most <em>limit - 1</em>,
     *         the rest of the string will be added to the last entry of the array</li>
     *         <li>if <em>limit</em> is equal to <code>0</code>, the delimiter will be applied as many time
     *         as possible, removing any trailing empty entries</li>
     *         <li>if <em>limit</em> is negative, the delimiter will be applied as many times as possible,
     *         while leaving any trailing empty spaces</li>
     *     </ul>
     * </blockquote>
     *
     * @param regex the pattern to split by
     * @param limit the result threshold, as described above
     * @return an array of substrings matched around the given pattern
     * @see #split(Pattern)
     * @see Pattern#split(CharSequence)
     * @see Pattern#split(CharSequence, int)
     */
    public StringType @NotNull [] split(Pattern regex, int limit) {
        if (limit < 0) throw new RangeException("limit has to be larger than 0");
        return array(regex.split(value, limit));
    }

    /**
     * Splits this object around matches of the given pattern.
     * The array returned by this method contains each substring of this object
     * that is terminated by another subsequence that matches the given pattern.
     *
     * @param regex the pattern to split by
     * @return an array of substrings matched around the given pattern, with no trailing zero-width matches
     * @see #split(Pattern, int)
     */
    public StringType @NotNull [] split(Pattern regex) {
        return split(regex, 0);
    }

    /**
     * Splits a string by a given delimiter and returns an array of substrings matched around the given delimiter
     *
     * @param delimiter the delimiter to split by
     * @return an array of substrings matched around the delimiter, with no trailing zero-width matches
     */
    public StringType @NotNull [] split(String delimiter) {
        return split(delimiter, 0);
    }

    /**
     * Splits a string by a given {@code char} delimiter and returns an array of substrings matched around it
     * the parameter <em>limit</em> specifies the result threshold:
     * <blockquote>
     *     <ul>
     *         <li>If <em>limit</em> is positive, the delimiter will be applied to at most <em>limit - 1</em>,
     *         the rest of the string will be added to the last entry of the array</li>
     *         <li>if <em>limit</em> is equal to <code>0</code>, the delimiter will be applied as many time
     *         as possible, removing any trailing empty entries</li>
     *         <li>if <em>limit</em> is negative, the delimiter will be applied as many times as possible,
     *         while leaving any trailing empty spaces</li>
     *     </ul>
     * </blockquote>
     *
     * @param delimiter the delimiter to split by
     * @param limit     the result threshold, as described above
     * @return an array of substrings matched around the given character
     * @see #split(char)
     * @see #split(String)
     * @see #split(String, int)
     */
    public StringType @NotNull [] split(char delimiter, int limit) {
        return split("" + delimiter, limit);
    }

    /**
     * Splits a string by a given delimiter and returns an array of substrings matched around the given delimiter
     *
     * @param delimiter the delimiter to split by
     * @return an array of substrings matched around the delimiter, with no trailing zero-width matches
     * @see #split(char, int)
     * @see #split(String)
     * @see #split(String, int)
     */
    public StringType @NotNull [] split(char delimiter) {
        return split(delimiter, 0);
    }

    /**
     * Returns an array of <code>StringType</code> containing each corresponding <code>String</code>
     *
     * @param array an array of <code>Strings</code> to parse into <code>StringTypes</code>
     * @return an array of <code>StringType</code> containing each corresponding <code>String</code>
     * @see #fromArray(String[], String)
     */
    public static StringType @NotNull [] array(String @NotNull [] array) {
        StringType[] s = new StringType[array.length];
        for (int i = 0; i < array.length; i++) s[i] = new StringType(array[i]);
        return s;
    }

    /**
     * Joins an array of strings by a given delimiter and puts them all into a single <code>StringType</code>
     *
     * @param strings   an array of strings to join
     * @param delimiter the delimiter to join by
     * @return a <code>StringType</code> with the array joined with the delimiter
     * @see #fromArray(String[])
     * @see #fromArray(String[], char)
     * @see #fromArray(String[], StringType)
     */
    @Contract("_, _ -> new")
    public static @NotNull StringType fromArray(String @NotNull [] strings, String delimiter) {
        StringType buf = new StringType();
        int total = strings.length - 1;
        for (int i = 0; ; i++) {
            buf.append(strings[i]);
            if (i == total) return buf;
            buf.append(delimiter);
        }
    }

    /**
     * Joins an array of strings with the delimiter <code>", "</code> and puts them all into a single <code>StringType</code>
     *
     * @param strings an array of strings to join
     * @return a <code>StringType</code> with the array joined by the delimiter
     * @see #fromArray(String[], String)
     * @see #fromArray(String[], char)
     * @see #fromArray(String[])
     */
    @Contract("_ -> new")
    public static @NotNull StringType fromArray(String[] strings) {
        return fromArray(strings, ", ");
    }

    /**
     * Joins an array of strings by a given delimiter and puts them all into a single <code>StringType</code>
     *
     * @param strings   an array of strings to join
     * @param delimiter the delimiter to join by
     * @return a <code>StringType</code> with the array joined with the delimiter
     * @see #fromArray(String[])
     * @see #fromArray(String[], String)
     * @see #fromArray(String[], StringType)
     */
    @Contract("_, _ -> new")
    public static @NotNull StringType fromArray(String[] strings, char delimiter) {
        return fromArray(strings, "" + delimiter);
    }

    /**
     * Joins an array of strings by a given delimiter and puts them all into a single <code>StringType</code>
     *
     * @param strings   an array of strings to join
     * @param delimiter the delimiter to join by
     * @return a <code>StringType</code> with the array joined with the delimiter
     * @see #fromArray(String[])
     * @see #fromArray(String[], String)
     * @see #fromArray(String[], char)
     */
    @Contract("_, _ -> new")
    public static @NotNull StringType fromArray(String[] strings, @NotNull StringType delimiter) {
        return fromArray(strings, delimiter.value);
    }

    /**
     * Joins an array of strings with this object's value nd puts them all into a single <code>StringType</code>
     *
     * @param strings an array of strings to join
     * @return a <code>StringType</code> with the array joined with the delimiter
     */
    @Contract("_ -> new")
    public @NotNull StringType join(String[] strings) {
        return fromArray(strings, this);
    }

    /**
     * Returns an iterator over each {@code char} of this object's value
     *
     * @return an iterator over each {@code char} of this object's value
     */
    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull Iterator<Character> iterator() {
        return new Itr();
    }

    /**
     * Returns a string representation of this object
     *
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return String.format("StringType { value: \"%s\" }", value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + lastIndex;
        result = 31 * result + length;
        result = 31 * result + Arrays.hashCode(chars);
        result = 31 * result + modCount;
        return result;
    }

    public @NotNull StringType clone() {
        return new StringType();
    }

    /**
     * Creates a deep copy of this object
     *
     * @return a new <code>StringType</code> with a copy of this object's values
     */
    @Contract(pure = true)
    @Override
    public @NotNull StringType copy() {
        StringType s = new StringType();
        s.value = value;
        s.chars = chars;
        s.length = length;
        s.lastIndex = lastIndex;
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;

        StringType t = (StringType) o;

        if (t.chars != chars) return false;
        if (t.length != length) return false;
        return t.lastIndex == lastIndex;
    }

    public StringType repeat(int count) {
        StringType tmp = this;
        int i = 0;
        while (i++ < count) tmp.append(this);
        return tmp;
    }


    private void update() {
        value = String.valueOf(chars);
        length = chars.length;
        lastIndex = length - 1;
    }

    private class Itr implements Iterator<Character> {
        int cursor;
        int lastRet = -1;
        int expectedModCount = modCount;

        Itr() {
        }

        @Override
        public boolean hasNext() {
            return cursor != length;
        }

        @Contract(pure = true)
        @Override
        public @Nullable Character next() {
            checkModCount();
            int i = cursor;
            if (i >= length) throw new NoSuchElementException();
            char[] value = StringType.this.value.toCharArray();
            if (i >= value.length) throw new ConcurrentModificationException();
            cursor = i + 1;
            return value[lastRet = i];
        }

        @Override
        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            checkModCount();
            try {
                StringType.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(java.util.function.Consumer<? super Character> action) {
            Objects.requireNonNull(action);
            final int length = StringType.this.length;
            int i = cursor;
            if (i < length) {
                final char[] cs = chars;
                if (i >= cs.length) throw new ConcurrentModificationException();
                for (; i < length && modCount == expectedModCount; i++)
                    action.accept(charAt(cs, i));
                cursor = i;
                lastRet = i - 1;
                checkModCount();
            }
        }

        private void checkModCount() {
            if (expectedModCount != modCount) throw new ConcurrentModificationException();
        }
    }
}
