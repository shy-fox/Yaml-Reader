package io.shiromi.yaml.util;

import io.shiromi.yaml.Yaml;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A yaml object with the value null
 * @version 1.5
 * @author Shiromi
 */
public final class YamlNull extends Yaml {
    /**
     * Creates a new instance of this object
     * @param name the name of this object
     */
    public YamlNull(String name) {
        super(name);
    }

    /**
     * Cast this object to any other object, e.g.
     * <blockquote>
     *     <pre>{@code
     *      YamlNull n = new YamlNull("name");
     *      YamlString s = (YamlString) n.cast(YamlString.class);
     *     }</pre>
     * </blockquote>
     * @param to the class to cast this object to
     * @return a new object with the same name and the default value of the class given
     * @since 1.3
     */
    public @Nullable Yaml cast(@NotNull Class<? extends Yaml> to) {
        try {
            return to.getConstructor(String.class).newInstance(this.name);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    /**
     * Returns null, as this object is called <code>YamlNull</code>
     */
    @Contract(pure = true)
    @Override
    public @Nullable Object get() {
        return null;
    }

    /**
     * <strong>** DO NOT USE! **</strong><br>
     * Will only return 0
     *
     * @return <code>0</code>
     * @hidden
     * @deprecated 1.0
     */
    @Override
    @Deprecated(forRemoval = true)
    public int length() {
        return 0;
    }

    /**
     * <strong>** DO NOT USE! **</strong><br>
     * Will only return 0
     *
     * @return <code>0</code>
     * @hidden
     * @deprecated 1.0
     */
    @Override
    @Deprecated(forRemoval = true)
    public int size() {
        return 0;
    }

    /**
     * Returns a string representation of this object
     * @param tabs the amount of tabs to be inserted before
     * @return a string of format <code>&lt;name&gt;: null</code>
     */
    @Override
    @NotNull
    public String stringify(int tabs) {
        return "\t".repeat(tabs) + this.name + ": null";
    }

    /**
     * Creates a new YamlNull object based on a string
     * @param s the string to parse
     * @return a new YamlNull object if it could be parsed, otherwise returns <code>null</code>
     */
    public static @Nullable YamlNull parse(String s) {
        Matcher m = Pattern.compile("^\\s*(?<name>[a-z]\\w*): null").matcher(s);
        if (m.matches()) return new YamlNull(m.group("name"));
        return null;
    }
}
