package io.shiromi.saml.tools;

import io.shiromi.saml.types.ListType;

import io.shiromi.saml.types.NumberType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ListUtils {
    static <T> @NotNull ListType<T> copy(@NotNull ListType<T> src, int srcPos, int destPos, int length) {
        if (srcPos > src.size())
            throw new ArrayIndexOutOfBoundsException("Attempting to access element outside of array");

        ListType<T> dest = new ListType<>(destPos + length);
        if (destPos > 0) fill(dest, null, destPos);

        for (int i = srcPos; i < srcPos + length; i++) {
            T t = src.get(i);
            dest.add(t);
        }

        return dest;
    }

    static <T> void fill(@NotNull ListType<T> list, T value, int amount) {
        if (list.isEmpty()) {
            for (int i = 0; i < amount; i++) list.add(value);
        } else {
            for (int i = 0; i < list.size(); i++) if (list.isNull(i)) list.set(i, value);
        }
    }

    static <T> void fill(ListType<T> list, T value) {
        fill(list, value, list.size());
    }

    @Contract("_ -> param1")
    static @NotNull ListType<NumberType> sort(@NotNull ListType<NumberType> nums) {
        int s = nums.size();
        NumberType[] n = nums.toArray(NumberType[]::new);
        for (int i = 0; i < s - 1; i++) {
            for (int j = 0; j < s - i - 1; j++) {
                if (MathUtils.compare(n[j], n[j + 1]) == -1) {
                    NumberType t = n[j];
                    n[j] = n[j + 1];
                    n[j + 1] = t;
                }
            }
        }
        return new ListType<>(List.of(n));
    }

    static @NotNull ListType<NumberType> random(int length, double min, double max) {
        ListType<NumberType> r = ListType.numbers(length);
        for (int i = 0; i < length; i++) {
            r.set(i, NumberType.random(min, max));
        }
        return r;
    }

    static @NotNull ListType<NumberType> random(int length, double max) {
        return random(length, 0, max);
    }

    static @NotNull ListType<NumberType> random(int length) {
        return random(length, 1);
    }
}
