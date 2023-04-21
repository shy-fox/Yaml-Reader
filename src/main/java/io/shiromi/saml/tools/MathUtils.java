package io.shiromi.saml.tools;

import io.shiromi.saml.exceptions.RangeException;
import io.shiromi.saml.types.NumberType;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface MathUtils {

    byte BYTE_MAX = (byte) 0x7F;
    byte BYTE_MIN = (byte) 0x80;
    short SHORT_MAX = (short) 0x7FFF;
    short SHORT_MIN = (short) 0x8000;
    int INT_MAX = 0x7FFF_FFFF;
    int INT_MIN = 0x8000_0000;
    long LONG_MAX = 0x7FFF_FFFF_FFFF_FFFFL;
    long LONG_MIN = 0x8000_0000_0000_0000L;
    float FLOAT_MAX = 0x1.FFFF_FEP127F;
    float FLOAT_MIN = 0x0.0000_02P-126F;
    double DOUBLE_MAX = 0x1.FFFF_FFFF_FFFF_FP+1023;
    double DOUBLE_MIN = 0x0.0000_0000_0000_1P-1022;

    // maxima/minima
    @Contract(pure = true)
    static int max(int @NotNull ... ints) {
        int r = INT_MIN;
        for (int i : ints) if (i > r) r = i;
        return r;
    }

    static int min(int @NotNull ... ints) {
        int r = INT_MAX;
        for (int i : ints) if (i < r) r = i;
        return r;
    }

    // comparison function
    static int compare(@NotNull NumberType a, @NotNull NumberType b) {
        return a.doubleValue() < b.doubleValue() ? 1 : a.doubleValue() == b.doubleValue() ? 0 : -1;
    }

    // ranging

    static boolean inRange(NumberType start, NumberType end, NumberType value) {
        return compare(start, value) == 1 && compare(end, value) == -1;
    }

    @Contract(pure = true)
    static boolean inRange(double start, double end, @NotNull NumberType value) {
        double d = value.doubleValue();
        return start <= d && d <= end;
    }

    // functions copied from NumberType
    static boolean isNaN(@NotNull NumberType n) {
        return n.isNaN();
    }

    static boolean isFinite(@NotNull NumberType n) {
        return n.isFinite();
    }

    static boolean isInfinite(@NotNull NumberType n) {
        return n.isInfinite();
    }

    @Contract("_, _, _, _ -> new")
    private static @NotNull NumberType constrain(double start, @NotNull NumberType v, double end, boolean safe) throws RangeException {
        if (safe) {
            if (!inRange(start, end, v)) throw new RangeException("Supplied value v is out of bounds: " +
                    start +
                    "<= v[" +
                    v.doubleValue() +
                    "] <= " +
                    end);
            else return new NumberType(Math.max(start, Math.min(v.doubleValue(), end)));
        }

        double d = v.doubleValue();
        // top bound check
        if (d > end) {
            //noinspection StatementWithEmptyBody
            while ((d -= start) > end) {
            }
            return new NumberType(d);
        } // bottom bound check
        else if (d < start) {
            double diff = Math.abs(d - start);
            //noinspection StatementWithEmptyBody
            while ((diff = end - diff) < start) {
            }
            return new NumberType(diff);
        } else return new NumberType(Math.max(start, Math.min(v.doubleValue(), end)));
    }

    @Contract("_ -> new")
    static @NotNull NumberType bindToByteSafe(NumberType v) throws RangeException  {
        return constrain(BYTE_MIN, v, BYTE_MAX, true);
    }

    @Contract("_ -> new")
    static @NotNull NumberType bindToByteUnsafe(NumberType v) {
        return constrain(BYTE_MIN, v, BYTE_MAX, false);
    }

    @Contract("_ -> new")
    static @NotNull NumberType bindToShortSafe(NumberType v) {
        return constrain(SHORT_MIN, v, SHORT_MAX, true);
    }

    @Contract("_ -> new")
    static @NotNull NumberType bindToShortUnsafe(NumberType v) {
        return constrain(SHORT_MIN, v, SHORT_MIN, false);
    }

    @Contract("_ -> new")
    static @NotNull NumberType bindToIntSafe(NumberType v) throws RangeException  {
        return constrain(INT_MIN, v, INT_MAX, true);
    }

    @Contract("_ -> new")
    static @NotNull NumberType bindToIntUnsafe(NumberType v) {
        return constrain(INT_MIN, v, INT_MAX, false);
    }

    @Contract("_ -> new")
    static @NotNull NumberType bindToLongSafe(NumberType v) throws RangeException  {
        return constrain(LONG_MIN, v, LONG_MAX, true);
    }

    @Contract("_ -> new")
    static @NotNull NumberType bindToLongUnsafe(NumberType v) {
        return constrain(LONG_MIN, v, LONG_MAX, false);
    }

    @Contract("_ -> new")
    static @NotNull NumberType bindToFloatSafe(NumberType v) throws RangeException  {
        return constrain(FLOAT_MIN, v, FLOAT_MAX, true);
    }

    @Contract("_ -> new")
    static @NotNull NumberType bindToFloatUnsafe(NumberType v) {
        return constrain(FLOAT_MIN, v, FLOAT_MAX, false);
    }

    @Contract("_ -> new")
    static @NotNull NumberType bindToDouble(NumberType v) {
        return constrain(DOUBLE_MIN, v, DOUBLE_MAX, true);
    }

    // non NumberType functions

    static int signedPow(int base, int exp) {
        if (exp < 0) return 0;
        if (base == 0 && exp == 0) return 1;
        if (base < 0 && exp % 2 != 0) return 0;
        int value = 1;
        for (int i = 0; i < exp; i++) value *= base;
        return value;
    }

    static double pow(int base, int exp) {
        if (exp < 0) return 1d / signedPow(base, exp);
        return signedPow(base, exp);
    }

    static double pow(double base, double exp) {
        return Math.pow(base, exp);
    }


}
