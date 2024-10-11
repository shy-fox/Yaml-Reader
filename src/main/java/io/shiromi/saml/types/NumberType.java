package io.shiromi.saml.types;

import io.shiromi.saml.exceptions.RangeException;
import io.shiromi.saml.tools.MathUtils;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Instances of the class <code>NumberType</code> carry a <code>Number</code> as value, meaning any
 * numeric value Java allows, e.g. {@code int} could be used as an argument for the constructor, however, regardless
 * of the type given as parameter, it will always be cast to {@code double}, the reason why {@link #getValue()} is of type
 * <code>Double</code>, the primary use of this class is for {@link MathUtils} or <code>YamlNumber</code> objects as well
 * as a <code>YamlArray</code> of type <code>NumberType</code>
 * <p></p>
 * Each <code>NumberType</code> can have its value cast to the primitive number types defined by Java, which could also
 * be done via using the <code>MathUtils</code> <code>bindTo</code> functions.
 * <br>
 * Number type allows for using zero-division in constructors, however it is disabled by default. Using {@link #enableZeroDivSupport()}
 * or {@link #toggleZeroDivSupport()} enables it.
 * @author Shiromi
 * @version 2.0-221223-J
 */
public final class NumberType extends AbstractType<Number> {

    private static boolean supportsZeroDiv = false;


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
    public static final NumberType NaN = new NumberType(0.0d / 0.0, true);
    /**
     * This <code>NumberType</code> stores the value of <code>Infinity</code>, equal to the result of
     * <code>1.0d / 0.0</code>
     *
     * @see #NEGATIVE_INFINITY
     */
    @SuppressWarnings({"divzero", "NumericOverflow"})
    public static final NumberType POSITIVE_INFINITY = new NumberType(1.0d / 0.0, true);
    /**
     * This <code>NumberType</code> stores the value of <code>-Infinity</code>, equal to the result of
     * <code>-1.0d / 0.0</code>
     *
     * @see #POSITIVE_INFINITY
     */
    @SuppressWarnings({"divzero", "NumericOverflow"})
    public static final NumberType NEGATIVE_INFINITY = new NumberType(-1.0d / 0.0, true);

    /**
     * This <code>NumberType</code> is a constant, holding the value of <code>0.000000000d</code>
     */
    public static final NumberType ZERO = new NumberType(0.000000000d);

    @Contract("_, _ -> param1")
    private static @NotNull Number zeroDivisionCheck(@NotNull Number n, boolean b) throws UnsupportedOperationException {
        double d = n.doubleValue();
        boolean c = d != d ||
                ((d < 0 ? -d : d) > MathUtils.DOUBLE_MAX);
        if (b || (c && supportsZeroDiv) || !c) return n;
        throw new UnsupportedOperationException("Zero division support disabled for usage in constructors, please check value or enable it");
    }

    private NumberType(Number n, boolean b) {
        super(zeroDivisionCheck(n, b));
        this.v = (double) n;
    }

    private double v;

    private NumberType(Number n) {
        this(n, false);
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
     * Allows to enable or disable support for dividing by zero in constructors
     *
     * @param a_flag The flag to set the support to
     * @see #enableZeroDivSupport()
     * @see #disableZeroDivSupport()
     * @see #toggleZeroDivSupport()
     * @since 2.0-22724-J
     */
    public static void setZeroDivSupport(boolean a_flag) {
        supportsZeroDiv = a_flag;
    }

    /**
     * Enables support for dividing by zero in constructors
     *
     * @see #setZeroDivSupport(boolean)
     * @see #disableZeroDivSupport()
     * @see #toggleZeroDivSupport()
     * @since 2.0-22724-J
     */
    public static void enableZeroDivSupport() {
        setZeroDivSupport(true);
    }

    /**
     * Disables support for dividing by zero in constructors
     *
     * @see #setZeroDivSupport(boolean)
     * @see #enableZeroDivSupport()
     * @see #toggleZeroDivSupport()
     * @since 2.0-22724-J
     */
    public static void disableZeroDivSupport() {
        setZeroDivSupport(false);
    }

    /**
     * Enables or disables support for dividing by zero, based on the {@link #supportsZeroDiv() current state}.
     *
     * @see #setZeroDivSupport(boolean)
     * @see #enableZeroDivSupport()
     * @see #disableZeroDivSupport()
     * @since 2.0-22724-J
     */
    public static void toggleZeroDivSupport() {
        setZeroDivSupport(!supportsZeroDiv);
    }

    /**
     * Retrieves the current state and returns whether it currently supports zero division for constructors. <p />
     * Disabled by default.
     *
     * @return whether zero division support is enabled or not.
     * @see #setZeroDivSupport(boolean)
     * @since 2.0-22724-J
     */
    public static boolean supportsZeroDiv() {
        return supportsZeroDiv;
    }

    /**
     * Parses a string value if possible to a {@code NumberType} with the parsed value
     *
     * @param value a numeric string to parse
     * @return a new {@code NumberType} with the parsed value
     * @throws NumberFormatException if the value cannot be parsed
     * @see #parseDecimal(StringType)
     * @since 1.8-12623-J
     */
    @Contract("_ -> new")
    public static @NotNull NumberType parseDecimal(String value) throws NumberFormatException {
        return parseDecimal(new StringType(value));
    }

    /**
     * Parses a string value if possible to a {@code NumberType} with the parsed value
     *
     * @param value a numeric string to parse
     * @return a new {@code NumberType} with the parsed value
     * @throws NumberFormatException if the value cannot be parsed
     * @see #parseDecimal(String)
     * @since 1.8-12623-J
     */
    @Contract("_ -> new")
    public static @NotNull NumberType parseDecimal(@NotNull StringType value) throws NumberFormatException {
        double total = 0;
        boolean neg = value.startsWith('-');
        boolean dec = value.contains('.');

        if (neg) value = value.substring(1);
        if (value.startsWith('.')) value.insert('0', 0);
        if (dec) {
            int decIndex = value.indexOf('.');

            if (!(value.substring(0, decIndex).isDigits() || value.substring(decIndex + 1).isDigits()))
                throw new NumberFormatException("Expected only digits as characters");

            char[] front = value.substring(0, decIndex).toCharArray();
            char[] rear = value.substring(decIndex + 1).toCharArray();

            for (int i = front.length - 1, j = 0; i >= 0; i--, j++) {
                double v = (double) front[i] - 0x30;
                total += v * MathUtils.pow(10, j);
            }

            for (int i = 0; i < rear.length; i++) {
                double v = (double) rear[i] - 0x30;
                total += v * MathUtils.pow(10, -(i + 1));
            }

            return new NumberType(neg ? -total : total);
        }
        for (int i = value.length() - 1, j = 0; i >= 0; i--, j++) {
            if (!value.isDigits())
                throw new NumberFormatException("Expected only digits as characters");
            char[] digits = value.toCharArray();
            double v = (double) digits[i] - 0x30;
            total += v * MathUtils.pow(10, j);
        }

        return new NumberType(neg ? -total : total);
    }

    /**
     * Parses a string value if possible to a {@code NumberType} with the parsed value
     *
     * @param value a numeric string to parse
     * @return a new {@code NumberType} with the parsed value
     * @throws NumberFormatException if the value cannot be parsed
     * @see #parseHexadecimal(StringType)
     * @since 1.8-12623-J
     */
    @Contract("_ -> new")
    public static @NotNull NumberType parseHexadecimal(String value) throws NumberFormatException {
        return parseHexadecimal(new StringType(value));
    }

    /**
     * Parses a string value if possible to a {@code NumberType} with the parsed value
     *
     * @param value a numeric string to parse
     * @return a new {@code NumberType} with the parsed value
     * @throws NumberFormatException if the value cannot be parsed
     * @see #parseHexadecimal(String)
     * @since 1.8-12623-J
     */
    @Contract("_ -> new")
    public static @NotNull NumberType parseHexadecimal(@NotNull StringType value) throws NumberFormatException {
        if (value.startsWith('-')) throw new NumberFormatException("Expected unsigned, positive value");

        if (value.startsWith('#')) value = value.substring(1);
        else if (value.startsWith("0x")) value = value.substring(2);

        value = value.toUpperCase();

        char[] valid = {
                '0', '1', '2', '3',
                '4', '5', '6', '7',
                '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F'
        };

        char[] digits = value.toCharArray();

        double total = 0;
        for (int i = value.length() - 1, j = 0; i >= 0; i--, j++) {
            check:
            {
                for (char c : valid)
                    if (digits[i] == c) {
                        break check;
                    }
                throw new NumberFormatException("Expected a hexadecimal string!");
            }

            char c = digits[i];
            // check for digits
            if (0x30 <= c && c <= 0x39) {
                double v = (double) c - 0x30;
                total += v * MathUtils.pow(16, j);
            } else if (0x41 <= c && c <= 0x46) {
                double v = (double) c - 0x37;
                total += v * MathUtils.pow(16, j);
            }
        }

        return new NumberType(total);
    }

    /**
     * Parses a string value if possible to a {@code NumberType} with the parsed value
     *
     * @param value a numeric string to parse
     * @return a new {@code NumberType} with the parsed value
     * @throws NumberFormatException if the value cannot be parsed
     * @see #parseOctal(StringType)
     * @since 1.8-12623-J
     */
    @Contract("_ -> new")
    public static @NotNull NumberType parseOctal(String value) throws NumberFormatException {
        return parseOctal(new StringType(value));
    }

    /**
     * Parses a string value if possible to a {@code NumberType} with the parsed value
     *
     * @param value a numeric string to parse
     * @return a new {@code NumberType} with the parsed value
     * @throws NumberFormatException if the value cannot be parsed
     * @see #parseOctal(String)
     * @since 1.8-12623-J
     */
    @Contract("_ -> new")
    public static @NotNull NumberType parseOctal(@NotNull StringType value) throws NumberFormatException {
        if (value.startsWith("0o")) value = value.substring(2);
        if (!value.isDigits()) throw new NumberFormatException("Expected only digits as characters");
        if (value.contains('8', '9')) throw new NumberFormatException("Expected octal format");

        char[] digits = value.toCharArray();
        double total = 0;

        for (int i = digits.length - 1, j = 0; i >= 0; i--, j++) {
            double v = (double) digits[i] - 0x30;
            total += v * Math.pow(8, j);
        }

        return new NumberType(total);
    }

    /**
     * Parses a string value if possible to a {@code NumberType} with the parsed value
     *
     * @param value a numeric string to parse
     * @return a new {@code NumberType} with the parsed value
     * @throws NumberFormatException if the value cannot be parsed
     * @see #parseBinary(StringType)
     * @since 1.8-12623-J
     */
    @Contract("_ -> new")
    public static @NotNull NumberType parseBinary(String value) throws NumberFormatException {
        return parseBinary(new StringType(value));
    }

    /**
     * Parses a string value if possible to a {@code NumberType} with the parsed value
     *
     * @param value a numeric string to parse
     * @return a new {@code NumberType} with the parsed value
     * @throws NumberFormatException if the value cannot be parsed
     * @see #parseBinary(String)
     * @since 1.8-12623-J
     */
    @Contract("_ -> new")
    public static @NotNull NumberType parseBinary(@NotNull StringType value) throws NumberFormatException {
        if (value.startsWith("0b")) value = value.substring(2);
        if (!value.isDigits()) throw new NumberFormatException("Expected only digits as characters");

        for (char c : value) if (c != 0x31 && c != 0x30) throw new NumberFormatException("Expected a binary string");

        char[] digits = value.toCharArray();
        double total = 0;

        for (int i = digits.length - 1, j = 0; i >= 0; i--, j++) {
            double v = (double) digits[i] - 0x30;
            total += v * Math.pow(2, j);
        }

        return new NumberType(total);
    }

    // TODO: Check if this function actually works as intended

    /**
     * Parses a given input string bases on a given radix value and returns a valid {@code NumberType} if the
     * input can be parsed to a number, currently only works with decimals for {@code radix <= 10}
     *
     * @param s     the input {@link StringType} to parse
     * @param radix the radix to parse the number to
     * @return a {@code NumberType} containing the value if parsed successfully
     * @throws NumberFormatException    If the input string contains a character which extends beyond the scope of the
     *                                  given radix
     * @throws IllegalArgumentException If the radix is less than 2
     * @author Shiromi
     * @see #parseDecimal(String)
     * @see #parseDecimal(StringType)
     * @see #parseBinary(String)
     * @see #parseBinary(StringType)
     * @see #parseOctal(String)
     * @see #parseOctal(StringType)
     * @see #parseHexadecimal(String)
     * @see #parseHexadecimal(StringType)
     * @see #parseNumber(String, int)
     * @since 2.0-9124J
     */
    @Contract(pure = true)
    public static @NotNull NumberType parseNumber(@NotNull StringType s, int radix) throws NumberFormatException, IllegalArgumentException {
        while (s.contains('_')) s.remove('_');
        boolean neg = s.startsWith('-');
        if (neg) s = s.substring(1);

        if (radix < 2)
            throw new IllegalArgumentException("Expected value greater than 1 for radix, got " + radix + " instead");

        if (radix <= 10) {
            if (!s.isDigits()) {
                throw new NumberFormatException("Expected only digits as values; Invalid input: '" + s.value + "' for radix: " + radix);
            }

            int dec = s.find('.');
            if (dec >= 0) {
                StringType a = s.substring(0, dec);
                StringType b = s.substring(dec + 1);
                if (!(a.isDigits() || b.isDigits()))
                    throw new NumberFormatException("Expected only digits as values; Invalid input: '" + s.value + "' for radix: " + radix);

                double d = 0;

                char[] dint = a.toCharArray();
                char[] d_dec = b.toCharArray();

                for (int i = dint.length - 1, j = 0; i >= 0; i--, j++) {
                    if (dint[i] - 0x30 >= radix)
                        throw new NumberFormatException("Invalid input for radix " + radix);
                    d += dint[i] * MathUtils.pow(radix, j);
                }

                for (int i = 0; i < d_dec.length; i++) {
                    if (d_dec[i] - 0x30 >= radix)
                        throw new NumberFormatException("Invalid input for radix " + radix);
                    d += d_dec[i] * MathUtils.pow(radix, -(i + 1));
                }

                return new NumberType(d);
            } else {
                double d = 0;
                for (int i = s.length() - 1, j = 0; i >= 0; i--, j++) {
                    int k = s.charAt(i) - 0x30;
                    if (k >= radix) throw new NumberFormatException("Invalid input for radix " + radix);

                    d += k * MathUtils.pow(radix, j);
                }
                return new NumberType(d);
            }
        }

        char[] cs = s.toUpperCase().toCharArray();
        double d = 0;

        for (int i = cs.length - 1, j = 0; i >= 0; i--, j++) {
            char c = cs[i];
            int top = 0x41 + (radix - 11);
            if (0x30 <= c && c <= 0x39) d += (c - 0x30) * MathUtils.pow(radix, j);
            else if (0x41 <= c && c <= top) d += (c - 0x37) * MathUtils.pow(radix, j);
            else throw new NumberFormatException("Invalid input for radix " + radix);
        }

        return new NumberType(d);
    }

    /**
     * Parses a given input string bases on a given radix value and returns a valid {@code NumberType} if the
     * input can be parsed to a number, currently only works with decimals for {@code radix <= 10}
     *
     * @param s     the input {@link String} to parse
     * @param radix the radix to parse the number to
     * @return a {@code NumberType} containing the value if parsed successfully
     * @throws NumberFormatException    If the input string contains a character which extends beyond the scope of the
     *                                  given radix
     * @throws IllegalArgumentException If the radix is less than 2
     * @author Shiromi
     * @see #parseDecimal(String)
     * @see #parseDecimal(StringType)
     * @see #parseBinary(String)
     * @see #parseBinary(StringType)
     * @see #parseOctal(String)
     * @see #parseOctal(StringType)
     * @see #parseHexadecimal(String)
     * @see #parseHexadecimal(StringType)
     * @see #parseNumber(StringType, int)
     * @since 2.0-9124J
     */
    public static @NotNull NumberType parseNumber(@NotNull String s, int radix) throws NumberFormatException, IllegalArgumentException {
        return parseNumber(new StringType(s), radix);
    }

    /**
     * Creates a new NumberType with a random value
     *
     * @param min the minimum value the object should have
     * @param max the maximum value the object should have
     * @return a NumberType with a random value
     * @see #random(double)
     * @see #random()
     */
    @Contract("_, _ -> new")
    public static @NotNull NumberType random(double min, double max) {
        return new NumberType((new java.util.Random().nextDouble() + min) * max);
    }

    /**
     * Creates a new NumberType with a random value
     *
     * @param max the maximum value the object should have
     * @return a NumberType with a random value
     * @see #random(double, double)
     * @see #random()
     */
    @Contract("_ -> new")
    public static @NotNull NumberType random(double max) {
        return random(0, max);
    }

    /**
     * Creates a new NumberType with a random value
     *
     * @return a NumberType with a random value
     * @see #random(double, double)
     * @see #random(double)
     */
    @Contract(" -> new")
    public static @NotNull NumberType random() {
        return random(0, 1d);
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
     *
     * @return the value of this object
     */
    public double doubleValue() {
        return v;
    }

    // ********************** MATH FUNCTIONS **********************
    // Since 2.0-122223-J

    /**
     * Adds another <code>NumberType</code> to this one and returns the modified result of this value
     *
     * @param other the object of which the value should be added
     * @return <code>this</code> object with a modified value
     * @see #add(NumberType...)
     * @since 2.0-122223-J
     */
    public @NotNull NumberType add(@NotNull NumberType other) {
        return new NumberType(add(this, other.v));
    }

    public @NotNull NumberType add(char c) {
        return new NumberType(add(this, c));
    }

    public @NotNull NumberType add(byte b) {
        return new NumberType(add(this, b));
    }

    public @NotNull NumberType add(short s) {
        return new NumberType(add(this, s));
    }

    public @NotNull NumberType add(int i) {
        return new NumberType(add(this, i));
    }

    public @NotNull NumberType add(long l) {
        return new NumberType(add(this, l));
    }

    public @NotNull NumberType add(float f) {
        return new NumberType(add(this, f));
    }

    public @NotNull NumberType add(double d) {
        return new NumberType(add(this, d));
    }

    @Contract(pure = true)
    private static double add(@NotNull NumberType a, double b) {
        return a.v + b;
    }

    public @NotNull NumberType add(NumberType @NotNull ... others) {
        double[] b = new double[others.length];
        for (int i = 0; i < others.length; i++) b[i] = others[i].v;
        return new NumberType(add(this, b));
    }

    public @NotNull NumberType add(char @NotNull ... chars) {
        double[] b = new double[chars.length];
        for (int i = 0; i < chars.length; i++) b[i] = chars[i];
        return new NumberType(add(this, b));
    }

    public @NotNull NumberType add(byte @NotNull ... bytes) {
        double[] b = new double[bytes.length];
        for (int i = 0; i < bytes.length; i++) b[i] = bytes[i];
        return new NumberType(add(this, b));
    }

    public @NotNull NumberType add(short @NotNull ... shorts) {
        double[] b = new double[shorts.length];
        for (int i = 0; i < shorts.length; i++) b[i] = shorts[i];
        return new NumberType(add(this, b));
    }

    public @NotNull NumberType add(int @NotNull ... ints) {
        double[] b = new double[ints.length];
        for (int i = 0; i < ints.length; i++) b[i] = ints[i];
        return new NumberType(add(this, b));
    }

    public @NotNull NumberType add(long @NotNull ... longs) {
        double[] b = new double[longs.length];
        for (int i = 0; i < longs.length; i++) b[i] = longs[i];
        return new NumberType(add(this, b));
    }

    public @NotNull NumberType add(float @NotNull ... floats) {
        double[] b = new double[floats.length];
        for (int i = 0; i < floats.length; i++) b[i] = floats[i];
        return new NumberType(add(this, b));
    }

    public @NotNull NumberType add(double @NotNull ... doubles) {
        return new NumberType(add(this, doubles));
    }

    @Contract(pure = true)
    private static double add(@NotNull NumberType a, double @NotNull ... b) {
        for (int i = 0; i < b.length; i++) a.v += b[0];
        return a.v;
    }

    @Contract("_ -> new")
    public @NotNull NumberType sub(@NotNull NumberType other) {
        return new NumberType(this.v - other.v);
    }

    public @NotNull NumberType sub(NumberType @NotNull ... others) {
        double v = this.v;
        for (NumberType o : others) v -= o.v;
        return new NumberType(v);
    }

    public @NotNull NumberType mult(@NotNull NumberType other) {
        return new NumberType(this.v * other.v);
    }

    public @NotNull NumberType mult(NumberType @NotNull ... others) {
        double v = this.v;
        for (NumberType o : others) v *= o.v;
        return new NumberType(v);
    }

    public @NotNull NumberType div(@NotNull NumberType other) {
        return new NumberType(this.v / other.v);
    }

    @Contract("_ -> new")
    public @NotNull NumberType div(byte b) {
        return new NumberType(this.v / b);
    }

    @Contract("_ -> new")
    public @NotNull NumberType div(short s) {
        return new NumberType(this.v / s);
    }

    @Contract("_ -> new")
    public @NotNull NumberType div(int i) {
        return new NumberType(this.v / i);
    }

    @Contract("_ -> new")
    public @NotNull NumberType div(long l) {
        return new NumberType(this.v / l);
    }

    @Contract("_ -> new")
    public @NotNull NumberType div(float f) {
        return new NumberType(this.v / f);
    }

    @Contract("_ -> new")
    public @NotNull NumberType div(double d) {
        return new NumberType(this.v / d);
    }

    public @NotNull NumberType floorDiv(@NotNull NumberType other) {
        return new NumberType(this.v / other.v).floor();
    }

    @Contract("_ -> new")
    public @NotNull NumberType floorDiv(byte b) {
        return new NumberType(this.v / b).floor();
    }

    @Contract("_ -> new")
    public @NotNull NumberType floorDiv(short s) {
        return new NumberType(this.v / s).floor();
    }

    @Contract("_ -> new")
    public @NotNull NumberType floorDiv(int i) {
        return new NumberType(this.v / i).floor();
    }

    @Contract("_ -> new")
    public @NotNull NumberType floorDiv(long l) {
        return new NumberType(this.v / l).floor();
    }

    @Contract("_ -> new")
    public @NotNull NumberType floorDiv(float f) {
        return new NumberType(this.v / f).floor();
    }

    @Contract("_ -> new")
    public @NotNull NumberType floorDiv(double d) {
        return new NumberType(this.v / d).floor();
    }

    public @NotNull NumberType ceilDiv(@NotNull NumberType other) {
        return new NumberType(this.v / other.v).ceil();
    }

    @Contract("_ -> new")
    public @NotNull NumberType ceilDiv(byte b) {
        return new NumberType(this.v / b).ceil();
    }

    @Contract("_ -> new")
    public @NotNull NumberType ceilDiv(short s) {
        return new NumberType(this.v / s).ceil();
    }

    @Contract("_ -> new")
    public @NotNull NumberType ceilDiv(int i) {
        return new NumberType(this.v / i).ceil();
    }

    @Contract("_ -> new")
    public @NotNull NumberType ceilDiv(long l) {
        return new NumberType(this.v / l).ceil();
    }

    @Contract("_ -> new")
    public @NotNull NumberType ceilDiv(float f) {
        return new NumberType(this.v / f).ceil();
    }

    @Contract("_ -> new")
    public @NotNull NumberType ceilDiv(double d) {
        return new NumberType(this.v / d).ceil();
    }

    public @NotNull NumberType floor() {
        StringType t = new StringType(this);
        int i = t.indexOf('.');
        return NumberType.parseDecimal(t.substring(0, i));
    }

    public @NotNull NumberType ceil() {
        return floor().add(1);
    }

    public @NotNull NumberType round() {
        StringType t = new StringType(this);
        int i = t.indexOf('.');
        return (new NumberType(t.substring(i).first()).greaterEqual(5)) ? ceil() : floor();
    }

    public @NotNull NumberType round(int decimals) {
        decimals += 1;
        StringType t = new StringType(this);
        int i = t.indexOf('.');
        NumberType f = NumberType.parseDecimal(t.substring(0, i));
        StringType u = new StringType();
        int j = 0;
        while (j++ <= decimals) {
            if (i >= t.length()) u.append('0');
            u.append(t.charAt(i++));
        }
        NumberType g = NumberType.parseDecimal(u.substring(decimals, decimals + 1));
        NumberType k = NumberType.parseDecimal(u.substring(decimals - 1, decimals));

        if (g.greaterEqual(5)) k = k.add(1);

        StringType v = new StringType(u.substring(0, decimals - 1));
        v.append(k.intValue());

        return f.add(NumberType.parseDecimal(v));
    }

    /**
     * Parses a string, could be <code>Hexadecimal</code> or <code>Decimal</code> to a <code>NumberType</code>
     * <blockquote>
     * <pre>{@code
     *      NumberType a, b, c, e;
     *      a = NumberType.parse("0x1F"); // 31
     *      b = NumberType.parse("31"); // 31
     *      c = NumberType.parse("Number"); // throws error
     *      e = NumberType.parse(null); // 0
     *     }</pre>
     * </blockquote>
     *
     * @param s the string to parse
     * @return a <code>NumberType</code> with the parsed value
     * @throws NumberFormatException if the input <code>String</code> is not valid
     * @see #parse(StringType)
     */
    @Contract("_ -> new")
    public static @NotNull NumberType parse(@Nullable String s) throws NumberFormatException {
        final @RegExp String DOUBLE_HEX = "0x[A-Fa-f0-9]\\.(?:[A-Fa-f0-9]{1,4}_?){0,3}[A-Fa-f0-9]P[+-]?\\d+[dD]?";
        final @RegExp String FLOAT_HEX = "0x[A-Fa-f0-9]\\.[A-Fa-f0-9]{1,4}_?(?:[A-Fa-f0-9]{1,2})?P[+-]?\\d+[fF]";
        final @RegExp String DOUBLE = "(?!0x)[+-]?\\d+\\.?\\d*[dD]?";
        final @RegExp String FLOAT = "(?!0x)[+-]?\\d+\\.?\\d*[fF]";
        final @RegExp String LONG = "[+-]?\\d+[lL]";
        final @RegExp String INT = "[+-]?\\d+";
        final @RegExp String HEX = "(?:0x)?(?:[A-Fa-f0-9]{1,4}_?){1,2}";

        double v;

        if (s == null) return ZERO;

        if (s.equalsIgnoreCase("nan")) return NaN;
        else if (s.matches(INT)) v = Integer.parseInt(s);
        else if (s.matches(HEX)) return parseHexadecimal(s);
        else if (s.matches(LONG)) v = Long.parseLong(s);
        else if (s.matches(FLOAT) || s.matches(FLOAT_HEX)) v = Float.parseFloat(s);
        else if (s.matches(DOUBLE) || s.matches(DOUBLE_HEX)) v = Double.parseDouble(s);
        else
            throw new NumberFormatException("Expected a parse-able value which matches either: '" +
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

    /**
     * Parses a {@link StringType}, could be <code>Hexadecimal</code> or <code>Decimal</code> to a <code>NumberType</code>
     * <blockquote>
     * <pre>{@code
     *      NumberType a, b, c, e;
     *      a = NumberType.parse(new StringType("0x1F")); // 31
     *      b = NumberType.parse(new StringType("31")); // 31
     *      c = NumberType.parse(new StringType("Number")); // throws error
     *      e = NumberType.parse(null); // 0
     *     }</pre>
     * </blockquote>
     *
     * @param value the <code>StringType</code> to parse
     * @return a <code>NumberType</code> with the parsed value
     * @throws NumberFormatException if the input <code>StringType</code> is not valid
     * @see #parse(String)
     * @since 2.0-122223-J
     */
    public static @NotNull NumberType parse(@Nullable StringType value) throws NumberFormatException {
        if (value == null) return ZERO;
        return parse(value.value);
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

    @Contract(pure = true)
    public boolean equals(@NotNull NumberType other) {
        return equals(this.v, other.v);
    }

    public boolean equals(char c) {
        return equals(this.v, c);
    }

    public boolean equals(byte b) {
        return equals(this.v, b);
    }

    public boolean equals(short s) {
        return equals(this.v, s);
    }

    public boolean equals(int i) {
        return equals(this.v, i);
    }

    public boolean equals(long l) {
        return equals(this.v, l);
    }

    public boolean equals(float f) {
        return equals(this.v, f);
    }

    public boolean equals(double d) {
        return equals(this.v, d);
    }

    private static boolean equals(double a, double b) {
        if (a != a || b != b) return false;
        return a == b;
    }

    @Contract(pure = true)
    public boolean greater(@NotNull NumberType other) {
        return this.greater(other.v);
    }

    private boolean greater(double d) {
        if (d != d) return false;
        return this.doubleValue() > d;
    }

    @Contract(pure = true)
    public boolean less(@NotNull NumberType other) {
        return this.less(other.v);
    }


    private boolean less(double d) {
        if (d != d) return false;
        return this.doubleValue() < d;
    }

    @Contract(pure = true)
    public boolean greaterEqual(@NotNull NumberType other) {
        return this.greaterEqual(other.v);
    }

    @Contract(pure = true)
    private boolean greaterEqual(double d) {
        if (d != d) return false;
        boolean gt = this.doubleValue() > d;
        boolean eq = this.doubleValue() == d;
        return gt || eq;
    }

    @Contract(pure = true)
    public boolean lessEqual(@NotNull NumberType other) {
        return this.lessEqual(other.v);
    }

    private boolean lessEqual(double d) {
        if (d != d) return false;
        boolean lt = this.doubleValue() < d;
        boolean eq = this.doubleValue() == d;
        return lt || eq;
    }

    @Contract(pure = true)
    public boolean threshold(@NotNull NumberType start, @NotNull NumberType end) {
        return threshold(start.v, end.v, this.v);
    }

    private static boolean threshold(double start, double end, double d) {
        return start <= d && d <= end;
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

    @Contract(" -> new")
    @Override
    public char @NotNull [] toBuffer() {
        return Double.toString(doubleValue()).toCharArray();
    }
}
