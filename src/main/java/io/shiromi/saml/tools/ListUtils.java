package io.shiromi.saml.tools;

import io.shiromi.saml.types.ListType;

import org.jetbrains.annotations.NotNull;

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
}
