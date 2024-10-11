package io.shiromi.saml.elements;

import io.shiromi.saml.exceptions.YamlParserException;
import io.shiromi.saml.types.GenericType;
import org.jetbrains.annotations.NotNull;

public class YamlGenericElement extends YamlElement<GenericType> {
    public YamlGenericElement(String name) {
        super(name);
        setValue(null);
    }

    public YamlGenericElement(String name, GenericType value) {
        this(name);
        setValue(value);
    }

    public YamlGenericElement(String name, Object value) {
        this(name, new GenericType(value));
    }

    @Override
    public char[] toBuffer() {
        return value.toBuffer();
    }

    public final Class<?> getTypeOfArray() {
        return value.getValue().getClass();
    }

    public final @NotNull String getTypeName() {
        return getTypeOfArray().getTypeName();
    }

    @Override
    public YamlGenericElement parse(String input) throws YamlParserException {
        return null;
    }
}
