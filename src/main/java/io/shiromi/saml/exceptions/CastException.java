package io.shiromi.saml.exceptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @version 1.9-22523-J.1
 */
public class CastException extends ClassCastException {
    @java.io.Serial
    private static final long serialVersionUID = -100;

    /**
     * Constructs a {@code CastException} with no detail message.
     */
    public CastException() {
        super();
    }

    /**
     * Constructs a {@code CastException} with the specified
     * detail message.
     *
     * @param s the detail message.
     */
    public CastException(String s) {
        super(s);
    }

    /**
     * Constructs a {@code CastException} with the error message:
     *
     * <p><code>Expected class '&lt;target_class&gt;', but got '&lt;actual_class&gt;'; Unable to cast.</code></p>
     *
     * @param actual the class of the object being cast
     * @param target the class which was supposed to be cast to
     * @see #CastException(Object, Class)
     * @since 1.9-22523-J.1
     */
    public CastException(@NotNull Class<?> actual, @NotNull Class<?> target) {
        this(actual == NullPointerException.class ?
                String.format("Tried to cast a 'NULL' object to class '%s'.", target.getName())
                : String.format("Expected class '%s', but got '%s'; Unable to cast.", target.getName(), actual.getName()));
    }

    /**
     * Constructs a {@code CastException} with the error message:
     *
     * <p><code>Expected class '&lt;target_class&gt;', but got '&lt;actual_class&gt;'; Unable to cast.</code></p>
     *
     * @param o      the object being cast
     * @param target the class which was supposed to be cast to
     * @see #CastException(Class, Class)
     * @since 1.9-22523-J.1
     */
    public CastException(@Nullable Object o, @NotNull Class<?> target) {
        this(o == null ? NullPointerException.class : o.getClass(), target);
    }
}
