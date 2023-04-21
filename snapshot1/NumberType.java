package io.shiromi.saml.types;

import io.shiromi.saml.exceptions.RangeException;
import io.shiromi.saml.tools.MathUtils;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Instances of the class <code>NumberType</code> carry a <code>Number</code> as value, meaning any
 * numeric value Java allows, e.g. {@code int} could be used as an argument for the constructor, however, regardless
 * of the type given as parameter, it will always be cast to {@code double}, the reason why {@link #getValue()} is of type
 * <code>Double</code>, the primary use of this class is for {@link MathUtils} or <code>YamlNumber</code> objects as well
 * as a <code>YamlArray</code> of type <code>NumberType</code>
 * <p></p>
 * Each <code>NumberType</code> can have its value cast to the primitive number types defined by Java, which could also
 * be done via using the <code>MathUtils</code> <code>bindTo</code> functions.
 *
 * @author Shiromi
 * @version 1.0
 */
public final class NumberType extends AbstractType<Number> {

    /**
     * This <code>NumberType</code> stores the maximum double value <code>0x1.FFFFFFFFFFFFFP1023</code>, or in integer notation
     * (2-2<sup>-52</sup>)&times;2<sup>127</sup>, equal to the {@code double} max value
     *
     * @see #MIN_VALUE
     */
    public static final NumberType MAX_VALUE = new NumberType(0x1.FFFFFFFFFFFFFP1023);
    /**
     * This <code>NumberType</code> stores the minimum double value <code>0x0.0000000000001P-1022</code>, or in integer notation
     * 2<sup>-1074</sup>, equal to the {@code double} min value
     *
     * @see #MAX_VALUE
     */
    public static final NumberType MIN_VALUE = new NumberType(0x0.0000000000001P-1022);
    /**
     * This <code>NumberType</code> stores a Not-a-Number (<code>NaN</code>) value, equal to the result of
     * <code>0.0d / 0.0</code>
     */
    @SuppressWarnings("divzero")
    public static final NumberType NaN = new NumberType(0.0d / 0.0);
    /**
     * This <code>NumberType</code> stores the value of <code>Infinity</code>, equal to the result of
     * <code>1.0d / 0.0</code>
     *
     * @see #NEGATIVE_INFINITY
     */
    @SuppressWarnings({"divzero", "NumericOverflow"})
    public static final NumberType POSITIVE_INFINITY = new NumberType(1.0d / 0.0);
    /**
     * This <code>NumberType</code> stores the value of <code>-Infinity</code>, equal to the result of
     * <code>-1.0d / 0.0</code>
     *
     * @see #POSITIVE_INFINITY
     */
    @SuppressWarnings({"divzero", "NumericOverflow"})
    public static final NumberType NEGATIVE_INFINITY = new NumberType(-1.0d / 0.0);

    private double v;

    private NumberType(Number n) {
        super(n);
        this.v = (double) n;
    }

    /**
     * Creates a new instance of <code>NumberType</code> from a {@code char} by taking its integer value and casting it
     * to {@code double}
     *
     * @param c the character to take the integer value of
     * @see #NumberType()
     * @see #NumberType(byte)
     * @see #NumberType(short)
     * @see #NumberType(int)
     * @see #NumberType(long)
     * @see #NumberType(float)
     * @see #NumberType(double)
     */
    public NumberType(char c) {
        this((int) c);
    }

    /**
     * Creates a new instance of <code>NumberType</code> from a {@code byte} by taking its value and casting it
     * to {@code double}
     *
     * @param b the {@code byte} to take the value of
     * @see #NumberType()
     * @see #NumberType(char)
     * @see #NumberType(short)
     * @see #NumberType(int)
     * @see #NumberType(long)
     * @see #NumberType(float)
     * @see #NumberType(double)
     */
    public NumberType(byte b) {
        this((double) b);
    }

    /**
     * Creates a new instance of <code>NumberType</code> from a {@code short} by taking its value and casting it
     * to {@code double}
     *
     * @param s the {@code short} to take the value of
     * @see #NumberType()
     * @see #NumberType(char)
     * @see #NumberType(byte)
     * @see #NumberType(int)
     * @see #NumberType(long)
     * @see #NumberType(float)
     * @see #NumberType(double)
     */
    public NumberType(short s) {
        this((double) s);
    }

    /**
     * Creates a new instance of <code>NumberType</code> from a {@code int} by taking its value and casting it
     * to {@code double}
     *
     * @param i the {@code int} to take the value of
     * @see #NumberType()
     * @see #NumberType(char)
     * @see #NumberType(byte)
     * @see #NumberType(short)
     * @see #NumberType(long)
     * @see #NumberType(float)
     * @see #NumberType(double)
     */
    public NumberType(int i) {
        this((double) i);
    }

    /**
     * Creates a new instance of <code>NumberType</code> from a {@code long} by taking its value and casting it
     * to {@code double}
     *
     * @param l the {@code long} to take the value of
     * @see #NumberType()
     * @see #NumberType(char)
     * @see #NumberType(byte)
     * @see #NumberType(short)
     * @see #NumberType(int)
     * @see #NumberType(float)
     * @see #NumberType(double)
     */
    public NumberType(long l) {
        this((double) l);
    }

    /**
     * Creates a new instance of <code>NumberType</code> from a {@code float} by taking its value and casting it
     * to {@code double}
     *
     * @param f the {@code float} to take the value of
     * @see #NumberType()
     * @see #NumberType(char)
     * @see #NumberType(byte)
     * @see #NumberType(short)
     * @see #NumberType(int)
     * @see #NumberType(long)
     * @see #NumberType(double)
     */
    public NumberType(float f) {
        this((double) f);
    }

    /**
     * Creates a new instance of <code>NumberType</code> from a {@code double}
     *
     * @param d the {@code double} to take the value of
     * @see #NumberType()
     * @see #NumberType(char)
     * @see #NumberType(byte)
     * @see #NumberType(short)
     * @see #NumberType(int)
     * @see #NumberType(long)
     * @see #NumberType(float)
     */
    public NumberType(double d) {
        this((Number) d);
    }

    /**
     * Creates a new instance of <code>NumberType</code> with a default value of <code>0</code>
     *
     * @see #NumberType(char)
     * @see #NumberType(byte)
     * @see #NumberType(short)
     * @see #NumberType(int)
     * @see #NumberType(long)
     * @see #NumberType(float)
     * @see #NumberType(double)
     */
    public NumberType() {
        this(0);
    }

    /**
     * Resets the value of this object back to <code>0</code>
     */
    public void reset() {
        this.value = 0;
        this.v = 0;
    }

    /**
     * Checks if this object is a Not-a-Number (<code>NaN</code>) value
     *
     * @return whether it is a <code>NaN</code> value or not
     */
    public boolean isNaN() {
        return isNaN(this);
    }

    /**
     * Checks if this object is an infinite value
     *
     * @return whether it is an infinite value or not
     */
    public boolean isInfinite() {
        return isInfinite(this);
    }

    /**
     * Checks if this object is a finite value
     *
     * @return whether it is a finite value or not
     */
    public boolean isFinite() {
        return isFinite(this);
    }

    /**
     * Checks if the given is a Not-a-Number (<code>NaN</code>) value
     *
     * @param n the object to check
     * @return whether it is a <code>NaN</code> value or not
     */
    @Contract(pure = true)
    public static boolean isNaN(@NotNull NumberType n) {
        double value = n.v;
        return value != value;
    }

    /**
     * Checks if the given object is an infinite value
     *
     * @param n the object to check
     * @return whether it is an infinite value or not
     */
    public static boolean isFinite(NumberType n) {
        return MathUtils.inRange(NEGATIVE_INFINITY, POSITIVE_INFINITY, n);
    }

    /**
     * Checks if the given object is a finite value
     *
     * @param n the object to check
     * @return whether it is a finite value or not
     */
    public static boolean isInfinite(NumberType n) {
        return !isFinite(n);
    }

    /**
     * Casts this object's value to {@code byte}
     *
     * @return the {@code byte} representation of this object's value
     * @throws RangeException if this object contains a value larger than the {@code byte} range
     * @see MathUtils#bindToByteSafe(NumberType)
     */
    public byte byteValue() throws RangeException {
        return (byte) MathUtils.bindToByteSafe(this).v;
    }

    /**
     * Casts this object's value to {@code byte}, with overflow
     *
     * @return the {@code byte} representation of this object's value
     * @see MathUtils#bindToByteUnsafe(NumberType)
     */
    public byte byteValueUnsafe() {
        return (byte) MathUtils.bindToByteUnsafe(this).v;
    }


    /**
     * Casts this object's value to {@code short}
     *
     * @return the {@code short} representation of this object's value
     * @throws RangeException if this object contains a value larger than the {@code short} range
     * @see MathUtils#bindToShortSafe(NumberType)
     */
    public short shortValue() throws RangeException {
        return (short) MathUtils.bindToShortSafe(this).v;
    }

    /**
     * Casts this object's value to {@code short}, with overflow
     *
     * @return the {@code short} representation of this object's value
     * @see MathUtils#bindToShortUnsafe(NumberType)
     */
    public short shortValueUnsafe() {
        return (short) MathUtils.bindToShortUnsafe(this).v;
    }

    /**
     * Casts this object's value to {@code int}
     *
     * @return the {@code int} representation of this object's value
     * @throws RangeException if this object contains a value larger than the {@code int} range
     * @see MathUtils#bindToIntSafe(NumberType)
     */
    public int intValue() throws RangeException {
        return (int) MathUtils.bindToIntSafe(this).v;
    }

    /**
     * Casts this object's value to {@code int}, with overflow
     *
     * @return the {@code int} representation of this object's value
     * @see MathUtils#bindToIntUnsafe(NumberType)
     */
    public int intValueUnsafe() {
        return (int) MathUtils.bindToIntUnsafe(this).v;
    }

    /**
     * Casts this object's value to {@code long}
     *
     * @return the {@code long} representation of this object's value
     * @throws RangeException if this object contains a value larger than the {@code long} range
     * @see MathUtils#bindToLongSafe(NumberType)
     */
    public long longValue() throws RangeException {
        return (long) MathUtils.bindToLongSafe(this).v;
    }

    /**
     * Casts this object's value to {@code long}, with overflow
     *
     * @return the {@code long} representation of this object's value
     * @see MathUtils#bindToLongUnsafe(NumberType)
     */
    public long longValueUnsafe() {
        return (long) MathUtils.bindToLongUnsafe(this).v;
    }

    /**
     * Casts this object's value to {@code float}
     *
     * @return the {@code float} representation of this object's value
     * @throws RangeException if this object contains a value larger than the {@code float} range
     * @see MathUtils#bindToFloatSafe(NumberType)
     */
    public float floatValue() throws RangeException {
        return (float) MathUtils.bindToFloatSafe(this).v;
    }

    /**
     * Casts this object's value to {@code float}, with overflow
     *
     * @return the {@code float} representation of this object's value
     * @see MathUtils#bindToFloatUnsafe(NumberType)
     */
    public float floatValueUnsafe() {
        return (float) MathUtils.bindToFloatUnsafe(this).v;
    }

    /**
     * Gets the {@code double} value of this object
     * @return the value of this object
     */
    public double doubleValue() {
        return v;
    }

    /**
     * Parses a string, could be <code>Hexadecimal</code> or <code>Decimal</code> to a <code>NumberType</code>
     * <blockquote>
     *     <pre>{@code
     *      NumberType a, b, c;
     *      a = NumberType.parse("0x1F"); // 31
     *      b = NumberType.parse("31"); // 31
     *      c = NumberType.parse("Number") // throws error
     *     }</pre>
     * </blockquote>
     * @param s the string to parse
     * @return a <code>NumberType</code> with the parsed value
     * @throws NumberFormatException if the input <code>String</code> is not valid
     */
    @Contract("_ -> new")
    public static @NotNull NumberType parse(@NotNull String s) throws NumberFormatException {
        final @RegExp String DOUBLE_HEX = "0x[A-Fa-f0-9]\\.(?:[A-Fa-f0-9]{1,4}_?){0,3}[A-Fa-f0-9]P[+-]?\\d+[dD]?";
        final @RegExp String FLOAT_HEX = "0x[A-Fa-f0-9]\\.[A-Fa-f0-9]{1,4}_?(?:[A-Fa-f0-9]{1,2})?P[+-]?\\d+[fF]";
        final @RegExp String DOUBLE = "(?!0x)[+-]?\\d+\\.?\\d*[dD]?";
        final @RegExp String FLOAT = "(?!0x)[+-]?\\d+\\.?\\d*[fF]";
        final @RegExp String LONG = "[+-]?\\d+[lL]";
        final @RegExp String INT = "[+-]?\\d+";
        final @RegExp String HEX = "(?:0x)?(?:[A-Fa-f0-9]{1,4}_?){1,2}";
        final @RegExp String NAN = "^NaN$";

        double v;

        if (s.matches(NAN)) return NaN;
        else if (s.matches(INT)) v = Integer.parseInt(s);
        else if (s.matches(HEX)) v = hexToInt(s.replace("0x", "").toUpperCase());
        else if (s.matches(LONG)) v = Long.parseLong(s);
        else if (s.matches(FLOAT) || s.matches(FLOAT_HEX)) v = Float.parseFloat(s);
        else if (s.matches(DOUBLE) || s.matches(DOUBLE_HEX)) v = Double.parseDouble(s);
        else throw new NumberFormatException("Expected a parsable value which matches either: '" +
                    INT +
                    "', '" +
                    LONG +
                    "', '" +
                    FLOAT +
                    "' , '" +
                    DOUBLE +
                    "' or '" +
                    HEX +
                    '\'');

        return new NumberType(v);
    }

    private static int hexToInt(@NotNull String hex) {
        final char[] hexValues = {
                '0', '1', '2', '3',
                '4', '5', '6', '7',
                '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F'
        };
        char[] cs = hex.toCharArray();

        int value = 0;
        int length = hex.length();

        for (int i = length; i > 0; i--) {
            int j = -1;
            //noinspection StatementWithEmptyBody
            while (cs[length - i] != hexValues[++j]) {
            }
            value += j * MathUtils.signedPow(16, i - 1);
        }

        return value;
    }

    
    @Contract(" -> new")
    @Override
    public @NotNull NumberType clone() {
        try {
            return (NumberType) super.clone();
        } catch (CloneNotSupportedException e) {
            // shouldn't happen, but throw an error to be sure
            throw new InternalError(e);
        }
    }

    @Contract(" -> new")
    @Override
    public @NotNull NumberType copy() {
        Number n = value;
        return new NumberType(n);
    }
}
