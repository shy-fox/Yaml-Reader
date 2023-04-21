package io.shiromi.saml.types;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.Objects;


/**
 * Instances of the class <code>AbstractType</code> represent a type value assignable to a <code>YamlElement</code>'s value,
 * each separate type has its own methods and implementation, however they can all be cast to {@link GenericType} as it has the
 * parameter <code>&lt;T&gt;</code> set to <code>Object</code>, therefore each subtype of this class can be cast to it.
 * <p></p>
 * Subtypes of this class behave a lot like their Java counterparts, e.g. {@link StringType} behaves similar to <code>String</code>,
 * this class itself only exists for generalization and unification of types throughout, as well as basic methods
 * implemented by each subclass.
 *
 * @param <T> the type of object this class carries as values
 * @author Shiromi
 * @version 1.0
 */
abstract class AbstractType<T> implements java.io.Serializable {

    @java.io.Serial
    private static final long serialVersionUID = 10L;

    /**
     * The value of the type, usually set via the constructor, but allowed to carry the value of {@code null}
     */
    protected T value;                  // set to protected for easier access

    /**
     * The <code>Class</code> of the value, used mostly in {@link GenericType}
     */
    protected Class<T> classOfT;        // set to protected for easier access


    /**
     * Creates a new instance with a set value and sets the {@link #classOfT} to the <code>Class</code> of the given value
     *
     * @param value the value this type carries
     */
    @SuppressWarnings("unchecked")
    @Contract(pure = true)
    protected AbstractType(T value) {
        this.value = value;
        classOfT = value == null ? (Class<T>) Object.class : (Class<T>) value.getClass();
    }

    /**
     * Updates the field {@link #value} to the new value
     *
     * @param value the value to update <code>value</code> to
     */
    public void set(T value) {
        this.value = value;
    }

    /**
     * Retrieves the value of the class
     *
     * @return the value
     */
    public T getValue() {
        return value;
    }

    /**
     * Casts this object to a <code>GenericType</code>, mostly for array handling and implementation which
     * requires objects as values
     *
     * @return a new <code>GenericType</code> carrying the same value, cast to a <code>Object</code>
     */
    @Contract(" -> new")
    public final @NotNull GenericType genericType() {
        return new GenericType(value);
    }

    /**
     * Casts this object to a <code>NullType</code>, effectively setting the value to {@code null}
     * @return a new <code>NullType</code>
     */
    @Contract(" -> new")
    public final @NotNull NullType nullType() {
        return new NullType();
    }

    /**
     * Returns a <code>String</code> representation of this object, only carrying the <code>simple name</code> of the class and value
     *
     * @return a <code>String</code> representation of this object
     */
    @Override
    public String toString() {
        return String.format("%s { value: %s }", getClass().getSimpleName(), value);
    }

    /**
     * Returns a <code>String</code> representation of this object's value
     * @return a <code>String</code> representation of this object's value
     */
    public final String valueToString() {
        return value.toString();
    }

    /**
     * Returns the hash code of this object
     *
     * @return the hash code of this object
     */
    public int hashCode() {
        return 31 * classOfT.getName().length() << serialVersionUID;
    }

    /**
     * Returns a hexadecimal representation of this object's hash code value returned by {@link #hashCode()}
     *
     * @return a hexadecimal representation of this object's hash code value
     */
    public final @NotNull String hex() {
        return hex(false);
    }

    /**
     * Returns a hexadecimal representation starting with <code>0x</code>, if specified by setting <code>doPrefix</code> to true,
     * otherwise returns the default hexadecimal representation of this object's hash code value returned by {@link #hashCode()}
     *
     * @return a hexadecimal representation starting with <code>0x</code>, if specified by setting <code>doPrefix</code> to true,
     * otherwise returns the default hexadecimal representation of this object's hash code value
     */
    public final @NotNull String hex(boolean doPrefix) {
        String hex = toHexString(hashCode());
        return doPrefix ? "0x" + hex : hex;
    }

    public final @NotNull String hex(int length) {
        StringBuilder buf = new StringBuilder();
        String hex = hex();
        while (buf.length() + hex.length() < length) buf.append('0');
        return buf.append(hex).toString();
    }

    /**
     * Creates a shallow copy of this object, not cloning variables
     *
     * @return a cloned instance of this object
     */
    @SuppressWarnings("unchecked")
    @Override
    public AbstractType<T> clone() throws CloneNotSupportedException {
        return (AbstractType<T>) super.clone();
    }

    /**
     * Creates a deep copy of this object
     *
     * @return a deep copy of this object
     * @implNote Check class specifications for info
     */
    public abstract AbstractType<T> copy();

    private @NotNull String toHexString(int i) {
        if (i < 0) i *= -1;
        int r;
        StringBuilder h = new StringBuilder();
        char[] hex = {
                '0', '1', '2', '3',
                '4', '5', '6', '7',
                '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F'
        };

        while (i > 0) {
            r = i % 16;
            h.append(hex[r]);
            i /= 16;
        }

        return h.toString();
    }

    /**
     * Checks if the given object is equal to this one
     *
     * @param other the object to compare
     * @return whether they are equal or not
     */
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (other == null || other.getClass().getSuperclass() != AbstractType.class) return false;
        AbstractType<?> a = (AbstractType<?>) other;

        Class<?> otherClassOfT = a.classOfT;
        Class<T> classOfT = this.classOfT;

        if (otherClassOfT != classOfT) return false;

        T otherValue = (T) a.value;

        return Objects.equals(value, otherValue) && hashCode() == a.hashCode();
    }

    @Serial
    private void writeObject(@NotNull ObjectOutputStream s) throws IOException {
        s.writeObject(value);
        s.writeObject(classOfT);
    }

    @Serial
    @SuppressWarnings("unchecked")
    private void readObject(@NotNull ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();

        Object o = s.readObject();

        if (o == null) classOfT = (Class<T>) Object.class;
        else classOfT = (Class<T>) s.readObject();
    }
}
