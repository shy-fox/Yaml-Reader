package io.shiromi.saml.elements;

import io.shiromi.saml.exceptions.YamlParserException;
import io.shiromi.saml.tools.Parser;
import io.shiromi.saml.types.NumberType;
import io.shiromi.saml.types.StringType;

public class YamlStringElement extends YamlElement<StringType> {

    public YamlStringElement(String name) {
        super(name);
    }

    public YamlStringElement(String name, StringType value) {
        this(name);
        setValue(value);
    }

    public YamlStringElement(String name, String value) {
        this(name, new StringType(value));
    }

    public void toUppercase() {
        value.toUpperCase();
    }

    public void toLowerCase() {
        value.toLowerCase();
    }

    public void capitalize() {
        char c = value.first();
        if (0x61 <= c && c <= 0x7A) c -= 0x20;
        value.replaceAt(0, c);
    }

    public boolean isDigits() {
        return value.isDigits();
    }

    public YamlNumberElement asNumber() {
        return new YamlNumberElement(name, NumberType.parse(value));
    }

    @Override
    public char[] toBuffer() {
        return value.toCharArray();
    }

    @Override
    public YamlStringElement parse(String input) throws YamlParserException {
        return Parser.stringToYamlStringElement(input);
    }
}
