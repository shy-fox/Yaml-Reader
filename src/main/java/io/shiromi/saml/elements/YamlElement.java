package io.shiromi.saml.elements;

import org.jetbrains.annotations.Nullable;

public abstract class YamlElement<T> implements java.io.Serializable {

    @java.io.Serial
    private static final long serialVersionUID = 100L;

    private final String name;
    private T value = null;

    protected YamlElement(String name) {
        this.name = name;
    }

    public @Nullable T setValue(T newT) {
        if (value == null) return value = newT;
        T oldValue = value;
        value = newT;
        return oldValue;
    }

    public abstract char[] toBuffer();
}
