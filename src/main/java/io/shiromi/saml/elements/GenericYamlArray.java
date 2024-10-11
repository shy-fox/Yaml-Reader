package io.shiromi.saml.elements;

import io.shiromi.saml.exceptions.YamlParserException;
import org.intellij.lang.annotations.MagicConstant;

public class GenericYamlArray extends AbstractYamlArray<YamlGenericElement> {
    public GenericYamlArray(String name) {
        super(name);
    }

    @Override
    public char[] toBuffer() {
        return new char[0];
    }

    @Override
    public GenericYamlArray parse(String input) throws YamlParserException {
        return null;
    }

    public GenericYamlArray(String name, int size) {
        super(name, size);
    }

    public final int countElement(YamlElement<?> element) {
        int i = 0;
        for (YamlElement<?> e : this) if (YamlElement.typeEqual(e, element)) i++;
        return i;
    }

    public boolean containsType(@MagicConstant(intValues = {ELEMENT_STRING, ELEMENT_NUMBER_INT,
            ELEMENT_NUMBER_FLOAT, ELEMENT_ARRAY, ELEMENT_TYPED_ARRAY,
            ELEMENT_OBJECT, ELEMENT_NULL, ELEMENT_ANY}) byte type) {
        for (YamlElement<?> e : this) if (!e.isType(type)) return false;
        return true;
    }

    public boolean containsType(Class<? extends YamlElement<?>> type) {
        for (YamlElement<?> e : this) if (e.getClass() != type) return false;
        return true;
    }
}
