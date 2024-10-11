package io.shiromi.saml.tools;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Instances of the class <code>ValuePair</code> emulate an entry in {@link java.util.HashMap}, having a <code>String</code>
 * as name a given type value as value, each value stores a non-mutable value.
 *
 * @param name  the name of the entry
 * @param value the value to store
 * @param <V>   the type of the value to store
 * @author Shiromi
 * @version 1.0
 */
public record ValuePair<V>(@NotNull String name, V value) {

    /**
     * Returns the name of the entry
     *
     * @return the name of the entry
     */
    public String name() {
        return name;
    }

    /**
     * Returns the value of the entry
     *
     * @return the value of the entry
     */
    public V value() {
        return value;
    }

    /**
     * Compares 2 values and checks if they are equal
     *
     * @param o the reference object with which to compare.
     * @return whether they are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValuePair<?> valuePair = (ValuePair<?>) o;

        if (!Objects.equals(name, valuePair.name)) return false;
        return Objects.equals(value, valuePair.value);
    }

    /**
     * Creates a new <code>ValuePair</code> with the name of the entry being the name of the class of the value
     *
     * @param value the value to store
     * @param <V>   the type of value to store
     * @return a new <code>ValuePair</code>
     */
    @Contract("_ -> new")
    public static <V> @NotNull ValuePair<V> create(@NotNull V value) {
        String name = value.getClass().getSimpleName();
        return new ValuePair<>(name, value);
    }

    @Contract(" -> new")
    public static @NotNull ValuePair<?> nullPair() {
        return new ValuePair<>("NULL", null);
    }

    /**
     * Creates a new array with the first element always being a {@link #nullPair()}
     * @return a new array
     */
    @Contract(" -> new")
    public static ValuePair<?> @NotNull [] create() {
        return new ValuePair[]{nullPair()};
    }

    public static ValuePair<?> @NotNull[] empty() {
        return new ValuePair[0];
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
