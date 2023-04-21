package io.shiromi.saml.functions;

import io.shiromi.saml.elements.YamlElement;

import java.util.NoSuchElementException;

public interface DataOutputStream {

    char[] put(char[] buf);
    void put(YamlElement<?> element);

    void clearContent() throws NoSuchElementException;

    char get(int index) throws IndexOutOfBoundsException;

    String getContent();

    boolean isEmpty();
}
