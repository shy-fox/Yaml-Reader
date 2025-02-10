package io.shiromi.saml.elements;

import io.shiromi.saml.exceptions.YamlParserException;
import io.shiromi.saml.tools.Parser;
import io.shiromi.saml.types.NumberType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class YamlNumberElement extends YamlElement<NumberType> {
    public YamlNumberElement(String name) {
        super(name);
        setType(NumberType.class);
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

    @Contract("_ -> new")
    @Override
    public @NotNull YamlNumberElement parse(String input) throws YamlParserException {
        return Parser.stringToYamlNumberElement(input);
    }
}
