package io.shiromi.saml.functions;

public interface ConditionalRunnable<T> {
    public T run(Object... values);
}
