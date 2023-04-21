package io.shiromi.saml.elements;

abstract class AbstractYamlArray<T> extends YamlElement<T> {
    protected AbstractYamlArray(String name) {
        super(name);
    }
}
