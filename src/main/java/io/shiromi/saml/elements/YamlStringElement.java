package io.shiromi.saml.elements;

import io.shiromi.saml.types.StringType;

public class YamlStringElement extends YamlElement<StringType> {

    public YamlStringElement(String name, StringType value) {
        super(name);
        setValue(value);
    }

    @Override
    public char[] toBuffer() {
        return new char[0];
    }
}
