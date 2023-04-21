package io.shiromi.saml.exceptions;

public class CastException extends ClassCastException {
    @java.io.Serial
    private static final long serialVersionUID = -100;

    /**
     * Constructs a {@code CastException} with no detail message.
     */
    public CastException() {
        super();
    }

    /**
     * Constructs a {@code CastException} with the specified
     * detail message.
     *
     * @param s the detail message.
     */
    public CastException(String s) {
        super(s);
    }
}
