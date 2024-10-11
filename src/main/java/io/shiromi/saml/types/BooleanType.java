package io.shiromi.saml.types;

import io.shiromi.saml.exceptions.CastException;

import io.shiromi.saml.functions.ConditionalRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Instances of the class <code>BooleanType</code> have a {@code boolean} as value, therefore it carries the same
 * functionality as {@code boolean} does, however, the operations are in written form, e.g. {@link #and(BooleanType) AND}
 * is equal to {@code && other}, it also carries functionality for parsing from different types, such as <code>String</code>.
 * The main implementation of this class is for logical operations, like {@code and}, all of which are supported by this class.
 * <p></p>
 * The logical operators supported by this class are:
 * <table>
 * <thead style="text-align: center;"><tr>
 * <td style="width: 50px;"><strong>Operation</strong></td>
 * <td style="width: 70px;"><strong>Method</strong></td>
 * <td style="text-align: start;"><em>Short description</em></td>
 * </tr></thead>
 * <tbody style="text-align: start;">
 * <tr><td>&and;</td><td>{@link #and(BooleanType) and}</td><td>{@code true} if both are {@code true}</td></tr>
 * <tr><td>&and;...&and;</td><td>{@link #and(BooleanType...) and...and}</td><td>{@code true} if all are {@code true}</td></tr>
 * <tr><td>&or;</td><td>{@link #or(BooleanType) or}</td><td>{@code true} if either is {@code true}</td></tr>
 * <tr><td>&or;...&or;</td><td>{@link #or(BooleanType...) or...or}</td><td>{@code true} if any is {@code true}</td></tr>
 * <tr><td>&oplus;</td><td>{@link #xor(BooleanType) xor}</td><td>{@code true} if one is {@code true}</td></tr>
 * <tr><td>&oplus;...&oplus;</td><td>{@link #xor(BooleanType...) xor...xor}</td><td>{@code true} if an odd number is {@code true}</td></tr>
 * <tr><td>&not;&and;</td><td>{@link #nand(BooleanType) nand}</td><td>{@code false} if both are {@code true}</td></tr>
 * <tr><td>&not;(&and;...&and;)</td><td>{@link #nand(BooleanType...) nand...nand}</td><td>{@code false} if all are {@code true}</td></tr>
 * <tr><td>&not;&or;</td><td>{@link #nor(BooleanType) nor}</td><td>{@code false} if either is {@code true}</td></tr>
 * <tr><td>&not;(&or;...&or;)</td><td>{@link #nor(BooleanType...) nor...nor}</td><td>{@code false} if any is {@code true}</td></tr>
 * <tr><td>&not;&oplus;</td><td>{@link #xnor(BooleanType) xnor}</td><td>{@code false} if one is {@code true}</td></tr>
 * <tr><td>&not;(&oplus;...&oplus;)</td><td>{@link #xnor(BooleanType...) xnor...xnor}</td><td>{@code false} if an odd number is {@code true}</td></tr>
 * <tr><td>&not;</td><td>{@link #not() not}</td><td>{@code true} if {@code false} and vice-versa</td></tr>
 * </tbody>
 * </table>
 *
 * @author Shiromi
 * @version 2.0-11923-J
 */
public final class BooleanType extends AbstractType<Boolean> {

    /**
     * Creates a new instance of <code>BooleanType</code> with the specified value
     *
     * @param value the value
     * @see #BooleanType()
     */
    public BooleanType(boolean value) {
        super(value);
    }

    /**
     * Creates a new instance of <code>BooleanType</code> with a default value of {@code false}
     *
     * @see #BooleanType(boolean)
     */
    public BooleanType() {
        this(false);
    }

    /**
     * Applies the <code>AND</code> operator to both values, and returns {@code true} only if both are {@code true},
     * the truth table for this operation is:
     * <table style='table-style: none'>
     * <thead><tr>
     * <td style="width:30px;">{@code this}</td>
     * <td style="width:30px;">{@code other}</td>
     * <td style="width:30px;">{@code out}</td>
     * </tr></thead>
     * <tbody>
     * <tr><td>0</td><td>0</td><td>0</td></tr>
     * <tr><td>0</td><td>1</td><td>0</td></tr>
     * <tr><td>1</td><td>0</td><td>0</td></tr>
     * <tr><td>1</td><td>1</td><td>1</td></tr>
     * </tbody>
     * </table>
     *
     * @param other the other value
     * @return the logical <code>AND</code> operation of both objects' values
     * @see #and(BooleanType...)
     * @see #nand(BooleanType)
     * @see #nand(BooleanType...)
     */
    @Contract(pure = true)
    public @NotNull BooleanType and(BooleanType other) {
        return new BooleanType(value && other.value);
    }

    /**
     * Applies the <code>AND</code> operator to all values and returns {@code true} only if all items are {@code true},
     * it will cancel out the loop as soon as it encounters an object with value {@code false} to cut down on processing time
     *
     * @param items a list of items to iterate trough
     * @return {@code true} if all items are {@code true}, as soon as a single one is set to {@code false}, will return {@code false}
     * @see #and(BooleanType)
     * @see #nand(BooleanType)
     * @see #nand(BooleanType...)
     */
    @Contract("_ -> new")
    public @NotNull BooleanType and(BooleanType @NotNull ... items) {
        for (BooleanType t : items) if (!t.value) return new BooleanType(false);
        return new BooleanType(true);
    }

    /**
     * Applies the <code>OR</code> operator to both values, and returns {@code true} if either or both are {@code true},
     * the truth table for this operation is:
     * <table style='table-style: none'>
     * <thead><tr>
     * <td style="width:30px;">{@code this}</td>
     * <td style="width:30px;">{@code other}</td>
     * <td style="width:30px;">{@code out}</td>
     * </tr></thead>
     * <tbody>
     * <tr><td>0</td><td>0</td><td>0</td></tr>
     * <tr><td>0</td><td>1</td><td>1</td></tr>
     * <tr><td>1</td><td>0</td><td>1</td></tr>
     * <tr><td>1</td><td>1</td><td>1</td></tr>
     * </tbody>
     * </table>
     *
     * @param other the other value
     * @return the logical <code>OR</code> operation of both objects' values
     * @see #or(BooleanType...)
     * @see #nor(BooleanType)
     * @see #nor(BooleanType...)
     */
    @Contract("_ -> new")
    public @NotNull BooleanType or(BooleanType other) {
        return new BooleanType(value || other.value);
    }

    /**
     * Applies the <code>OR</code> operator to all values and returns {@code true} if any item is {@code true},
     * it might return {@code true} immediately, as the first value might be {@code true}
     *
     * @param items a list of items to iterate trough
     * @return {@code true} if any item is {@code true}, will return {@code false} if all of them are {@code false}
     * @see #or(BooleanType)
     * @see #nor(BooleanType)
     * @see #nor(BooleanType...)
     */
    @Contract("_ -> new")
    public @NotNull BooleanType or(BooleanType @NotNull ... items) {
        for (BooleanType t : items) if (t.value) return new BooleanType(true);
        return new BooleanType(false);
    }

    /**
     * Applies the <code>XOR</code> operator to both values, and returns {@code true} only if one of both is {@code true},
     * the truth table for this operation is:
     * <table style='table-style: none'>
     * <thead><tr>
     * <td style="width:30px;">{@code this}</td>
     * <td style="width:30px;">{@code other}</td>
     * <td style="width:30px;">{@code out}</td>
     * </tr></thead>
     * <tbody>
     * <tr><td>0</td><td>0</td><td>0</td></tr>
     * <tr><td>0</td><td>1</td><td>1</td></tr>
     * <tr><td>1</td><td>0</td><td>1</td></tr>
     * <tr><td>1</td><td>1</td><td>0</td></tr>
     * </tbody>
     * </table>
     *
     * @param other the other value
     * @return the logical <code>XOR</code> operation of both objects' values
     * @see #xor(BooleanType...)
     * @see #xnor(BooleanType)
     * @see #xnor(BooleanType...)
     */
    @Contract("_ -> new")
    public @NotNull BooleanType xor(BooleanType other) {
        return new BooleanType(and(other).not() && or(other).value);
    }

    /**
     * Applies the <code>XOR</code> operator to all values and returns {@code true} if an odd number of items greater than 0
     * is {@code true}, returns {@code false} if none of them are {@code true}
     *
     * @param items a list of items to iterate trough
     * @return {@code true} an odd number of items is {@code true}, will return {@code false} if an even
     * number of items or none of them are {@code true}
     * @see #xor(BooleanType)
     * @see #xnor(BooleanType)
     * @see #xnor(BooleanType...)
     */
    @Contract("_ -> new")
    public @NotNull BooleanType xor(BooleanType @NotNull ... items) {
        int i = intValue();
        for (BooleanType b : items) i += b.intValue();
        return new BooleanType(i % 2 != 0 && i > 0);
    }

    /**
     * Applies the <code>NAND</code> (logical inverse of <code>AND</code>) operator to both values, and returns {@code false} only if both are {@code true},
     * the truth table for this operation is:
     * <table style='table-style: none'>
     * <thead><tr>
     * <td style="width:30px;">{@code this}</td>
     * <td style="width:30px;">{@code other}</td>
     * <td style="width:30px;">{@code out}</td>
     * </tr></thead>
     * <tbody>
     * <tr><td>0</td><td>0</td><td>1</td></tr>
     * <tr><td>0</td><td>1</td><td>1</td></tr>
     * <tr><td>1</td><td>0</td><td>1</td></tr>
     * <tr><td>1</td><td>1</td><td>0</td></tr>
     * </tbody>
     * </table>
     *
     * @param other the other value
     * @return the logical <code>NAND</code> operation of both objects' values
     * @see #and(BooleanType)
     * @see #and(BooleanType...)
     * @see #nand(BooleanType...)
     */
    @Contract("_ -> new")
    public @NotNull BooleanType nand(BooleanType other) {
        return and(other).flip();
    }

    /**
     * Applies the <code>NAND</code> operator to all values and returns {@code false} only if all items are {@code true},
     * it will cancel out the loop as soon as it encounters an object with value {@code false} to cut down on processing time
     *
     * @param items a list of items to iterate trough
     * @return {@code false} only if all items are {@code true}, essentially will return {@code true} if any count of items is {@code false}
     * @see #and(BooleanType)
     * @see #and(BooleanType...)
     * @see #nand(BooleanType)
     */
    @Contract("_ -> new")
    public @NotNull BooleanType nand(BooleanType... items) {
        return and(items).flip();
    }

    /**
     * Applies the <code>NOR</code> (logical inverse of <code>OR</code>) operator to both values, and returns {@code true} only if both are {@code false},
     * the truth table for this operation is:
     * <table style='table-style: none'>
     * <thead><tr>
     * <td style="width:30px;">{@code this}</td>
     * <td style="width:30px;">{@code other}</td>
     * <td style="width:30px;">{@code out}</td>
     * </tr></thead>
     * <tbody>
     * <tr><td>0</td><td>0</td><td>1</td></tr>
     * <tr><td>0</td><td>1</td><td>0</td></tr>
     * <tr><td>1</td><td>0</td><td>0</td></tr>
     * <tr><td>1</td><td>1</td><td>0</td></tr>
     * </tbody>
     * </table>
     *
     * @param other the other value
     * @return the logical <code>NOR</code> operation of both objects' values
     * @see #or(BooleanType)
     * @see #or(BooleanType...)
     * @see #nor(BooleanType)
     */
    @Contract("_ -> new")
    public @NotNull BooleanType nor(BooleanType other) {
        return or(other).flip();
    }

    /**
     * Applies the <code>NOR</code> operator to all values and returns {@code true} if none of the items are {@code true},
     * it might return {@code false} immediately, as the first value might be {@code true}
     *
     * @param items a list of items to iterate trough
     * @return {@code false} if any item is {@code true}, will return {@code true} if all of them are {@code false}
     * @see #or(BooleanType)
     * @see #or(BooleanType...)
     * @see #nor(BooleanType)
     */
    @Contract("_ -> new")
    public @NotNull BooleanType nor(BooleanType... items) {
        return or(items).flip();
    }

    /**
     * Applies the <code>XNOR</code> (logical inverse of <code>XOR</code>) operator to both values,
     * and returns {@code false} only if one of both is {@code true},
     * the truth table for this operation is:
     * <table style='table-style: none'>
     * <thead><tr>
     * <td style="width:30px;">{@code this}</td>
     * <td style="width:30px;">{@code other}</td>
     * <td style="width:30px;">{@code out}</td>
     * </tr></thead>
     * <tbody>
     * <tr><td>0</td><td>0</td><td>1</td></tr>
     * <tr><td>0</td><td>1</td><td>0</td></tr>
     * <tr><td>1</td><td>0</td><td>0</td></tr>
     * <tr><td>1</td><td>1</td><td>1</td></tr>
     * </tbody>
     * </table>
     *
     * @param other the other value
     * @return the logical <code>XNOR</code> operation of both objects' values
     * @see #xor(BooleanType)
     * @see #xor(BooleanType...)
     * @see #xnor(BooleanType)
     */
    @Contract("_ -> new")
    public @NotNull BooleanType xnor(BooleanType other) {
        return xor(other).flip();
    }

    /**
     * Applies the <code>XNOR</code> operator to all values and returns {@code true} if an even number or none of the
     * items is {@code true}, returns {@code false} if an odd number of them are {@code true}
     *
     * @param items a list of items to iterate trough
     * @return {@code false} an odd number of items is {@code true}, will return {@code true} if an even
     * number of items or none of them are {@code true}
     * @see #xor(BooleanType)
     * @see #xor(BooleanType...)
     * @see #xnor(BooleanType)
     */
    @Contract("_ -> new")
    public @NotNull BooleanType xnor(BooleanType... items) {
        return xor(items).flip();
    }

    /**
     * Casts this object's value to an {@code int}
     *
     * @return <code>0</code> if the value is {@code false}, else returns <code>1</code>
     */
    public int intValue() {
        return value ? 1 : 0;
    }

    /**
     * Casts this object's value to a <code>String</code>
     *
     * @return <code>"true"</code> if this object's value is {@code true}, else <code>"false"</code>
     */
    @Contract(pure = true)
    public @NotNull String stringValue() {
        return value ? "true" : "false";
    }

    /**
     * Switches this object's value's state, e.g. {@code true} &rarr; {@code false}
     *
     * @see #flip()
     */
    public void toggle() {
        value = !value;
    }

    /**
     * Returns a new <code>BooleanType</code> with the inverse of this object's value,
     * e.g. {@code true} &rarr; {@code false}
     *
     * @return a new <code>BooleanType</code> with the inverse of this object's value
     * @see #toggle()
     */
    @Contract(" -> new")
    public @NotNull BooleanType flip() {
        return new BooleanType(!value);
    }

    /**
     * Returns the inverse of this object's value, e.g. {@code true} &rarr; {@code false}
     *
     * @return the inverse of this object's value
     * @see #is()
     */
    public boolean not() {
        return !value;
    }

    /**
     * Returns the value of this object, e.g. {@code true} &rarr; {@code true}
     *
     * @return this value of this object
     * @see #not()
     * @see #getValue()
     */
    public boolean is() {
        return value;
    }

    /**
     * Runs a conditional statement on this object and returns a value based on the current condition
     *
     * @param ifCondition   the function to run in a {@code true} case
     * @param elseCondition the function to run in a {@code false} case
     * @param values        the values to pass into the functions to run
     * @param <T>           the type of value to return
     * @return a value based on the conditions
     * @see #ifElse(BooleanType, ConditionalRunnable, ConditionalRunnable, Object...)
     * @since 2.0-73123-J
     */
    public <T> T ifElse(ConditionalRunnable<T> ifCondition, ConditionalRunnable<T> elseCondition, Object... values) {
        return is() ? ifCondition.run(values) : elseCondition.run(values);
    }

    /**
     * Runs a conditional statement on the given BooleanType value and returns a value based on the given condition
     *
     * @param b             the value to run the function on
     * @param ifCondition   the function to run in a {@code true} case
     * @param elseCondition the function to run in a {@code false} case
     * @param values        the values to pass into the functions to run
     * @param <T>           the type of value to return
     * @return a value based on the conditions
     * @see #ifElse(ConditionalRunnable, ConditionalRunnable, Object...)
     * @since 2.0-73123-J
     */
    public static <T> T ifElse(@NotNull BooleanType b, ConditionalRunnable<T> ifCondition, ConditionalRunnable<T> elseCondition, Object... values) {
        return b.ifElse(ifCondition, elseCondition, values);
    }

    @Contract("_ -> new")
    private static @NotNull BooleanType parseBoolean(@NotNull Number number) {
        return new BooleanType(!Double.isNaN((double) number) && number.doubleValue() > 0.0);
    }

    /**
     * Parses a <code>BooleanType</code> from a {@code byte}
     *
     * @param b the value to parse
     * @return a new <code>BooleanType</code> with the parsed value
     */
    public static @NotNull BooleanType parseBoolean(byte b) {
        return parseBoolean((Number) b);
    }

    /**
     * Parses a <code>BooleanType</code> from a {@code short}
     *
     * @param s the value to parse
     * @return a new <code>BooleanType</code> with the parsed value
     */
    public static @NotNull BooleanType parseBoolean(short s) {
        return parseBoolean((Number) s);
    }

    /**
     * Parses a <code>BooleanType</code> from an {@code int}
     *
     * @param i the value to parse
     * @return a new <code>BooleanType</code> with the parsed value
     */
    public static @NotNull BooleanType parseBoolean(int i) {
        return parseBoolean((Number) i);
    }

    /**
     * Parses a <code>BooleanType</code> from a {@code long}
     *
     * @param l the value to parse
     * @return a new <code>BooleanType</code> with the parsed value
     */
    public static @NotNull BooleanType parseBoolean(long l) {
        return parseBoolean((Number) l);
    }

    /**
     * Parses a <code>BooleanType</code> from a {@code float}
     *
     * @param f the value to parse
     * @return a new <code>BooleanType</code> with the parsed value
     */
    public static @NotNull BooleanType parseBoolean(float f) {
        return parseBoolean((Number) f);
    }

    /**
     * Parses a <code>BooleanType</code> from a {@code double}
     *
     * @param d the value to parse
     * @return a new <code>BooleanType</code> with the parsed value
     */
    public static @NotNull BooleanType parseBoolean(double d) {
        return parseBoolean((Number) d);
    }

    /**
     * Parses a <code>BooleanType</code> from a <code>String</code>
     *
     * @param string the <code>String</code> to parse
     * @return a new <code>YamlBoolean</code> with the value set to {@code true} if the <code>String</code> is equal to
     * {@code "true"}
     */
    @Contract("_ -> new")
    public static @NotNull BooleanType parseBoolean(String string) {
        return new BooleanType("true".equals(string));
    }

    /**
     * Parses another {@link AbstractType} to a <code>BooleanType</code> based on its value
     *
     * @param type the type to cast
     * @return a new <code>BooleanType</code> with the given type's value parsed to a boolean
     * @throws CastException if the value of the given type is {@code null} or the given type is an instance of {@link NullType}
     * @see #parseBoolean(AbstractType, boolean)
     */
    @Contract(pure = true)
    public static @NotNull BooleanType parseBoolean(AbstractType<?> type) throws CastException {
        if (type instanceof StringType t) return parseBoolean(t.value);
        else if (type.value == null || type instanceof NullType)
            throw new CastException((Object) null, BooleanType.class);
        else if (type instanceof GenericType t) return t.booleanType();
        else if (type instanceof NumberType t) return parseBoolean(t.value);
        else if (type instanceof BooleanType t) return t;
        throw new CastException("Cannot cast type to BooleanType as it is not a member of AbstractType, got " +
                type.getClass().getSimpleName() +
                '[' +
                type.getClass().getSuperclass().getSimpleName() +
                ']');
    }

    /**
     * <em>Safely</em> &mdash; depending on <code>nullable</code> being specified &mdash; parses another {@link AbstractType} to a <code>BooleanType</code> based on its value, if <code>nullable</code>
     * is set to {@code true}, will return {@code null} if the cast failed.
     *
     * @param type the type to cast
     * @return a new <code>BooleanType</code> with the given type's value parsed to a boolean
     * @see #parseBoolean(AbstractType)
     * @see #parseBooleanSafe(AbstractType)
     */
    @Contract(pure = true)
    public static @Nullable BooleanType parseBoolean(AbstractType<?> type, boolean nullable) {
        try {
            return parseBoolean(type);
        } catch (CastException e) {
            if (nullable) return null;
            throw e;
        }
    }

    /**
     * Safely parses another {@link AbstractType} to a <code>BooleanType</code> based on its value,
     * will return {@code null} if the cast failed.
     *
     * @param type the type to cast
     * @return a new <code>BooleanType</code> with the given type's value parsed to a boolean
     * @see #parseBoolean(AbstractType)
     * @see #parseBoolean(AbstractType, boolean)
     */
    public static @Nullable BooleanType parseBooleanSafe(AbstractType<?> type) {
        return parseBoolean(type, true);
    }

    @Contract(" -> new")
    @Override
    public @NotNull BooleanType clone() {
        try {
            return (BooleanType) super.clone();
        } catch (CloneNotSupportedException e) {
            // shouldn't happen
            throw new InternalError(e);
        }
    }

    @Override
    public @NotNull BooleanType copy() {
        BooleanType b = new BooleanType();
        b.value = value;
        return b;
    }

    @Contract(value = " -> new", pure = true)
    @Override
    public char @NotNull [] toBuffer() {
        return stringValue().toCharArray();
    }
}
