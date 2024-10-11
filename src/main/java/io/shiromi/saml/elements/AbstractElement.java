package io.shiromi.saml.elements;

import io.shiromi.saml.exceptions.YamlParserException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

abstract class AbstractElement<T> implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = 100L;

    protected final String name;
    protected T value = null;

    protected Class<? extends T> type;

    protected AbstractElement(String name) {
        this.name = name;
    }

    protected AbstractElement(String name, T value) {
        this(name);
        this.setValue(value);
    }

    @SuppressWarnings("unchecked")
    public @Nullable T setValue(T newT) {
        if (value == null) return value = newT;
        T oldValue = value;
        value = newT;
        this.type = (Class<? extends T>) value.getClass();
        return oldValue;
    }

    public abstract char[] toBuffer();

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }

    public static <E extends AbstractElement<?>> boolean equals(@NotNull E element1, @Nullable E element2) {
        if (element2 == null) return false;
        return (element1.name.equals(element2.name) &&
                Objects.equals(element1.value, element2.value) &&
                element1.type == element2.type);
    }

    public <E extends AbstractElement<?>> boolean equals(@Nullable E element) {
        return equals(this, element);
    }

    public static boolean same(@NotNull AbstractElement<?> element1, @NotNull AbstractElement<?> element2) {
        if (element1.type != element2.type) return false;
        return equals(element1, element2);
    }

    public boolean same(AbstractElement<?> element) {
        return same(this, element);
    }

    public abstract AbstractElement<T> parse(String input) throws YamlParserException;
}
