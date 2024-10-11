package io.shiromi.saml.types;

import io.shiromi.saml.tools.ListSupport;
import io.shiromi.saml.tools.MathUtils;

import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Collection;
import java.util.NoSuchElementException;

public class ListType<E> extends AbstractListType<E> {

    public ListType(@Range(from = 0, to = MathUtils.INT_MAX) int initialCapacity) {
        super(initialCapacity);
    }

    public ListType(@NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Collection<? extends E> collection) {
        super(collection);
    }

    public ListType() {
        super();
    }

    @SuppressWarnings("unchecked")
    public @NotNull ListType<E> splice(int start, int end) {
        ListSupport.subListRangeCheck(start, end, size);
        if (isEmpty()) throw new NoSuchElementException("List is empty");

        ListType<E> cpy = new ListType<>();
        E[] es = (E[]) elementData;
        E[] sublist = (E[]) new Object[end - start];
        System.arraycopy(es, start, sublist, 0, end - start);

        cpy.addAll(sublist);
        cpy.modCount = 1;

        return cpy;
    }

    public @NotNull ListType<E> spliceStart(int to) {
        return splice(0, to);
    }

    public @NotNull ListType<E> spliceEnd(int from) {
        return splice(from, size - 1);
    }

    @Contract(" -> new")
    public static @NotNull ListType<GenericType> generic() {
        return new ListType<>();
    }

    @Contract("_ -> new")
    public static @NotNull ListType<GenericType> generic(int capacity) {
        return new ListType<>(capacity);
    }

    @Contract(" -> new")
    public static @NotNull ListType<StringType> strings() {
        return new ListType<>();
    }

    @Contract("_ -> new")
    public static @NotNull ListType<StringType> strings(int capacity) {
        return new ListType<>(capacity);
    }

    @Contract(" -> new")
    public static @NotNull ListType<BooleanType> booleans() {
        return new ListType<>();
    }

    @Contract("_ -> new")
    public static @NotNull ListType<BooleanType> booleans(int capacity) {
        return new ListType<>(capacity);
    }

    @Contract(" -> new")
    public static @NotNull ListType<NumberType> numbers() {
        return new ListType<>();
    }

    @Contract("_ -> new")
    public static @NotNull ListType<NumberType> numbers(int capacity) {
        return new ListType<>(capacity);
    }

    @Override
    public char[] toBuffer() {
        return toString().toCharArray();
    }
}
