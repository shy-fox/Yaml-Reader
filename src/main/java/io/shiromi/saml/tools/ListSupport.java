package io.shiromi.saml.tools;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public interface ListSupport {
    int SOFT_MAX_ARRAY_LENGTH = MathUtils.INT_MAX - 8;

    static int newLength(int oldLength, int minGrowth, int prefGrowth) {
        assert oldLength >= 0;
        assert minGrowth > 0;

        int prefLength = oldLength + MathUtils.max(minGrowth, prefGrowth);
        if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) return prefLength;
        return hugeLength(oldLength, minGrowth);
    }

    private static int hugeLength(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) { // overflow
            throw new OutOfMemoryError(
                    "Required array length " + oldLength + " + " + minGrowth + " is too large");
        } else return MathUtils.max(minLength, SOFT_MAX_ARRAY_LENGTH);
    }

    static void subListRangeCheck(int from, int to, int size) {
        if (from < 0) throw new IndexOutOfBoundsException("from = " + from);
        if (to > size) throw new IndexOutOfBoundsException("to = " + to);
        if (from > to) throw new IllegalArgumentException("from(" + from + ") > to(" + to + ")");
    }
}
