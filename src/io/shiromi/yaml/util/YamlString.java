package io.shiromi.yaml.util;

import io.shiromi.yaml.Yaml;

import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A yaml object containing a <code>String</code> as value
 *
 * @author Shiromi
 * @version 1.6-a
 */
public final class YamlString extends Yaml {

    /**
     * The string, aka value of this object
     */
    public String value;

    /**
     * Creates a new instance of this object with the given name and value
     *
     * @param name  the name of this object
     * @param value the value this object should have
     * @see #YamlString(String)
     */
    public YamlString(String name, String value) {
        super(name);
        this.value = value;
    }

    /**
     * Creates a new instance of this object with an empty string as value
     *
     * @param name the name of this object
     * @see #YamlString(String, String)
     */
    public YamlString(String name) {
        this(name, "");
    }

    /**
     * Returns a new YamlString with the name <code><strong>name</strong>-><strong>beginIndex</strong></code><br>
     * Examples: <br>
     * <blockquote>
     * <pre>{@code
     *      YamlString s = new YamlString("string", "foo bar");
     *      YamlString sub1 = s.substring(4);
     *      // returns a new YamlString called "string->4" with content "bar"
     *      YamlString sub2 = s.substring(7);
     *      // returns a new YamlString called "string->7" with content ""
     *     }</pre>
     * </blockquote>
     *
     * @param beginIndex the beginning index, inclusive
     * @return a new YamlString with the name <code><strong>name</strong>-><strong>index</strong></code> and the content being a substring of
     * the original content
     * @see #substring(int, int)
     * @since 1.5
     */
    @Contract(pure = true)
    public @NotNull YamlString substring(int beginIndex) {
        return new YamlString(this.name + "->" + beginIndex, this.value.substring(beginIndex));
    }

    /**
     * Returns a new YamlString with the name <code><strong>name</strong>[<strong>beginIndex</strong>-<strong>endIndex</strong>]</code><br>
     * Examples: <br>
     * <blockquote>
     * <pre>{@code
     *      YamlString s = new YamlString("string", "foo bar");
     *      YamlString sub1 = s.substring(1, 2);
     *      // returns a new YamlString called "string[1-2]" with content "oo"
     *      YamlString sub2 = s.substring(0, 2);
     *      // returns a new YamlString called "string[0-2]" with content "foo"
     *     }</pre>
     * </blockquote>
     *
     * @param beginIndex the beginning index, inclusive
     * @param endIndex   the end index, inclusive
     * @return a new YamlString with the name <code><strong>name</strong>[<strong>start</strong>-<strong>end</strong>]</code> and the content being a substring of
     * the original content
     * @see #substring(int)
     * @since 1.5
     */
    @Contract(pure = true)
    public @NotNull YamlString substring(int beginIndex, int endIndex) {
        return new YamlString(this.name, this.value.substring(beginIndex, endIndex));
    }

    /**
     * Returns a new YamlString with the name <code><strong>name</strong>[<strong>old</strong>-><strong>new</strong>]</code><br>
     * Examples:<br>
     * <blockquote>
     * <pre>{@code
     *      YamlString s = new YamlString("string", "foo:bar:boo");
     *      YamlString r1 = s.replace(":", "-");
     *      // returns a new YamlString called "string[:->-]" and the content "foo-bar-boo"
     *      YamlString r2 = s.replace("o", "x");
     *      // returns a new YamlString called "string[o->x]" and the content "fxx:bar:bxx"
     *     }</pre>
     * </blockquote>
     *
     * @param oldChar the original character, the target
     * @param newChar the replacement value
     * @return a new YamlString with the name <code><strong>name</strong>[<strong>old</strong>-><strong>new</strong>]</code> and the content having applied the
     * replacement to
     * @since 1.5
     */
    @Contract("_, _ -> new")
    public @NotNull YamlString replace(String oldChar, String newChar) {
        return new YamlString(this.name, this.value.replace(oldChar, newChar));
    }

    /**
     * Creates a new {@link YamlArray} with the name <code><strong>name</strong>_split</code><br>
     * Examples: The YamlString s <code> = new YamlString("string", "foo:bar:boo")</code> returns the following with these expressions:
     * <blockquote><table class="plain">
     * <caption style="display:none">Split examples showing regex and result</caption>
     * <thead>
     * <tr>
     * <th scope="col">Regex</th>
     * <th scope="col">Result</th>
     * </tr>
     * </thead>
     * <tr><th scope="row" style="text-weight:normal">:</th>
     * <td>{@code { "foo", "bar", "boo" }}</td></tr>
     * <tr><th scope="row" style="text-weight:normal">o</th>
     * <td>{@code { "f", "", "bar", "b" , "" }}</td></tr>
     * </table></blockquote>
     *
     * @param delimiter the delimiter to split by, can be <code>regex</code>
     * @return a new YamlArray with the split contents
     */
    @Contract("_ -> new")
    public @NotNull YamlArray split(@NotNull @RegExp String delimiter) {
        return new YamlArray(this.name + "_split", (Object) this.value.split(delimiter));
    }

    /**
     * Gets this object's value
     *
     * @return {@link #value}
     */
    @Override
    public String get() {
        return this.value;
    }

    /**
     * Gets this object's value's length
     */
    @Override
    public int length() {
        return this.value.length();
    }

    /**
     * Gets this object's value's length
     */
    @Override
    public int size() {
        return this.length();
    }

    /**
     * Returns a string representation of this object
     *
     * @param tabs the amount of tabs to be inserted before
     * @return a string of format <code>&lt;name&gt;: "&lt;value&gt;"</code>
     */
    @Override
    @NotNull
    public String stringify(int tabs) {
        return "\t".repeat(tabs) + this.name + ": \"" + this.value + '"';
    }

    /**
     * Creates a new YamlString based on a string
     *
     * @param s the string to parse
     * @return a new YamlString if it could be parsed, otherwise returns <code>null</code>
     */
    public static @Nullable YamlString parse(String s) {
        Matcher m = Pattern.compile("^\\s*(?<name>[a-z]\\w*):\\s?\"(?<value>[^\"]*)\"").matcher(s);
        if (m.matches()) return new YamlString(m.group("name"), m.group("value"));
        return null;
    }
}
