package io.shiromi.saml.elements;

import io.shiromi.saml.functions.ListIterator;
import io.shiromi.saml.tools.MathUtils;
import io.shiromi.saml.types.ListType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

abstract class AbstractYamlArray<T> extends YamlElement<T> implements Iterable<YamlElement<T>> {
    protected AbstractYamlArray(String name) {
        super(name);
    }

    protected ListType<YamlElement<T>> data;

    protected AbstractYamlArray(String name, @Range(from = 0, to = MathUtils.INT_MAX) int size) {
        this(name);
        this.data = new ListType<>(size);
    }

    public final boolean append(YamlElement<T> e) {
        return this.data.add(e);
    }

    public final boolean add(int index, YamlElement<T> e) {
        this.data.add(index, e);
        return true;
    }

    public final int size() {
        return this.data.size();
    }

    public final boolean contains(YamlElement<T> e) {
        return this.data.contains(e);
    }

    public final int indexOf(YamlElement<T> e) {
        return this.data.indexOf(e);
    }

    public final int lastIndexOf(YamlElement<T> e) {
        return this.data.lastIndexOf(e);
    }

    public final YamlElement<T> get(int index) {
        return this.data.get(index);
    }

    public final YamlElement<T> set(int index, YamlElement<T> e) {
        return this.data.set(index, e);
    }

    @SafeVarargs
    public final boolean addAll(YamlElement<T> @NotNull ... elements) {
        return this.data.addAll(elements);
    }

    public YamlElement<T> remove(int index) {
        return this.data.remove(index);
    }

    public final boolean remove(YamlElement<T> e) {
        return this.data.remove(e);
    }

    public final void clear() {
        this.data.clear();
    }

    public final void forEach(ListIterator<YamlElement<T>> iterator) {
        for (int i = 0; i < this.size(); i++) iterator.apply(i, this.get(i));
    }

    @NotNull
    @Override
    public Iterator<YamlElement<T>> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<YamlElement<T>> {
        int cur;
        int lastRet = -1;

        Itr() {
        }

        public boolean hasNext() {
            return cur != size();
        }

        @SuppressWarnings("unchecked")
        public YamlElement<T> next() {
            int i = cur;
            if (i >= size()) throw new NoSuchElementException();

            Object[] t = AbstractYamlArray.this.data.toArray();

            if (i >= t.length) throw new ConcurrentModificationException();
            cur = i + 1;

            return (YamlElement<T>) t[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();

            try {
                AbstractYamlArray.this.remove(lastRet);
                cur = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super YamlElement<T>> action) {
            Objects.requireNonNull(action);
            final int size = size();
            int i = cur;

            if (i < size) {
                final Object[] e = AbstractYamlArray.this.data.toArray();
                if (i >= e.length) throw new ConcurrentModificationException();


                for (; i < size; i++) action.accept((YamlElement<T>) e[i]);

                cur = i;
                lastRet = i - 1;
            }
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof AbstractYamlArray<?> a) {
            if (!a.name.equals(this.name)) return false;
            return a.data.equals(this.data);
        }
        return false;
    }
}
