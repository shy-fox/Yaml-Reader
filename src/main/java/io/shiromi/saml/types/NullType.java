package io.shiromi.saml.types;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Instances of the class <code>NullType</code> all have a value of {@code null},
 * but can all be cast to the different types, it does not carry any special methods
 * or has any special functionality
 *
 * @author Shiromi
 * @version 1.0
 */
public final class NullType extends AbstractType<Object> {

    /**
     * Creates a new instance of a <code>NullType</code>
     */
    public NullType() {
        super(null);
    }

    /**
     * Casts this object to a <code>StringType</code>
     *
     * @return a new <code>StringType</code> with the default value
     */
    @Contract(" -> new")
    public @NotNull StringType stringType() {
        return new StringType();
    }

    /**
     * Casts this object to a <code>BooleanType</code>
     *
     * @return a new <code>BooleanType</code> with the default value
     */
    @Contract(" -> new")
    public @NotNull BooleanType booleanType() {
        return new BooleanType();
    }

    /**
     * Casts this object to a <code>NumberType</code>
     *
     * @return a new <code>NumberType</code> with the default value
     */
    @Contract(" -> new")
    public @NotNull NumberType numberType() {
        return new NumberType();
    }

    /**
     * {@inheritDoc}
     */
    @Contract(" -> new")
    public @NotNull NullType clone() {
        try {
            return (NullType) super.clone();
        } catch (CloneNotSupportedException e) {
            // if this gets thrown, IDK what broke
            throw new InternalError();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Contract(" -> new")
    public @NotNull NullType copy() {
        return clone();
    }
}
