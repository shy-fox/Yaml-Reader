package io.shiromi.saml.elements;

import io.shiromi.saml.exceptions.YamlParserException;
import io.shiromi.saml.types.NumberType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class YamlNumberElement extends YamlElement<NumberType> {
    public YamlNumberElement(String name) {
        super(name);
    }

    public YamlNumberElement(String name, NumberType value) {
        this(name);
        setValue(value);
    }

    @Contract(value = " -> new", pure = true)
    @Override
    public char @NotNull [] toBuffer() {
        return new char[0];
    }

    @Contract(pure = true)
    @Override
    public @Nullable YamlNumberElement parse(String input) throws YamlParserException {
        return null;
    }
}
