package io.shiromi.saml.elements;

import io.shiromi.saml.exceptions.YamlParserException;
import io.shiromi.saml.tools.ValuePair;

public class YamlObjectElement extends YamlElement<ValuePair<Object>> {
    private ValuePair<Object>[] data;

    public YamlObjectElement(String name) {
        super(name);
    }

    @Override
    public char[] toBuffer() {
        return new char[0];
    }

    @Override
    public YamlObjectElement parse(String input) throws YamlParserException {
        return null;
    }
}
