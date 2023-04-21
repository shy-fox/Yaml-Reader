package io.shiromi.saml.types;

import io.shiromi.saml.tools.ListSupport;
import io.shiromi.saml.tools.MathUtils;

import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * The wrapper-class <em>AbstractListType</em> is the backbone for list classes, e.g. {@link ListType}, it is a similar
 * class to {@link ArrayList}, but with a few changes, but the majority is taken from it
 * <br>
 * It has similar functionality to Java's default arrays or its built in ArrayList, however it does have some differences
 * @param <E>
 */
abstract class AbstractListType<E> extends AbstractType<E[]> implements List<E>, Iterable<E> {

    protected int size;
    protected Object[] elementData;
    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] DEFAULT_CAP_EMPTY_ELEMENT_DATA = {};
    private static final Object[] EMPTY_ELEMENT_DATA = {};
    protected transient int modCount = 0;

    public AbstractListType(@Range(from = 0, to = MathUtils.INT_MAX) int initialCapacity) {
        super(null);
        if (initialCapacity > 0) elementData = new Object[initialCapacity];
        else if (initialCapacity == 0) elementData = new Object[DEFAULT_CAPACITY];
        else throw new IllegalArgumentException("Illegal capacity: " +
                    initialCapacity);
    }

    public AbstractListType() {
        super(null);
        elementData = DEFAULT_CAP_EMPTY_ELEMENT_DATA;
    }

    public AbstractListType(@NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Collection<? extends E> c) {
        super(null);
        Object[] a = c.toArray();
        if ((size = a.length) != 0) {
            if (c instanceof AbstractListType) {
                elementData = a;
            } else {
                elementData = Arrays.copyOf(a, size, Object[].class);
            }
        } else {
            elementData = EMPTY_ELEMENT_DATA;
        }
    }

    public void trimToSize() {
        modCount++;
        if (size < elementData.length)
            elementData = (size == 0) ?
                    EMPTY_ELEMENT_DATA :
                    Arrays.copyOf(elementData, size);
    }

    public void ensureCapacity(int minCapacity) {
        if (minCapacity > elementData.length
                && !(elementData == DEFAULT_CAP_EMPTY_ELEMENT_DATA
                && minCapacity <= DEFAULT_CAPACITY)) {
            modCount++;
            grow(minCapacity);
        }
    }

    private Object[] grow(int minCapacity) {
        int oldCapacity = elementData.length;
        if (oldCapacity > 0 || elementData != DEFAULT_CAP_EMPTY_ELEMENT_DATA) {
            int newCapacity = ListSupport.newLength(oldCapacity,
                    minCapacity - oldCapacity,
                    oldCapacity >> 1);
            return elementData = Arrays.copyOf(elementData, newCapacity);
        }
        return elementData = new Object[MathUtils.max(DEFAULT_CAPACITY, minCapacity)];
    }

    private Object[] grow() {
        return grow(size + 1);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isNull(int index) {
        rangeCheck(index);
        return elementData(index) == null;
    }

    public boolean contains(Object object) {
        return indexOf(object) >= 0;
    }

    public int indexOf(Object o) {
        return indexOfRange(o, 0, size);
    }

    int indexOfRange(Object o, int start, int end) {
        Object[] es = elementData;
        if (o == null) {
            for (int i = start; i < end; i++) {
                if (es[i] == null) return i;
            }
        } else {
            for (int i = start; i < end; i++) {
                if (o.equals(es[i])) return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object object) {
        return lastIndexOfRange(object, 0, size);
    }

    int lastIndexOfRange(Object o, int start, int end) {
        Object[] es = elementData;
        if (o == null) {
            for (int i = end - 1; i >= start; i--) {
                if (es[i] == null) return i;
            }
        } else {
            for (int i = end - 1; i >= start; i--) {
                if (o.equals(es[i])) return i;
            }
        }
        return -1;
    }

    public AbstractListType<E> clone() {
        try {
            AbstractListType<E> v = (AbstractListType<E>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen since we should - hopefully - be Cloneable
            throw new InternalError(e);
        }
    }

    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T @NotNull [] a) {
        if (a.length < size) {
            // make a new array of a with the contents from this object
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    // positional access
    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    static <E> E elementAt(Object @NotNull [] es, int index) {
        return (E) es[index];
    }


    public E get(int index) {
        Objects.checkIndex(index, size);
        return elementData(index);
    }

    public E set(int index, E element) {
        Objects.checkIndex(index, size);
        E old = elementData(index);
        elementData[index] = element;
        return old;
    }

    private void add(E e, Object @NotNull [] elementData, int s) {
        if (s == elementData.length)
            elementData = grow();
        elementData[s] = e;
        size = s + 1;
    }

    public boolean add(E element) {
        modCount++;
        add(element, elementData, size);
        return true;
    }

    public boolean addAll(E @NotNull ... elements) {
        final int mc = modCount;
        for (E e : elements) add(e);
        modCount = mc + 1;
        return true;
    }

    public void add(int index, E element) {
        rangeCheck(index);
        modCount++;
        final int s;

        Object[] elementData;
        if ((s = size) == (elementData = this.elementData).length)
            elementData = grow();
        System.arraycopy(elementData, index, elementData, index + 1, s - index);
        elementData[index] = element;
        size = s + 1;
    }

    public E remove(int index) {
        Objects.checkIndex(index, size);
        final Object[] es = elementData;

        @SuppressWarnings("unchecked") E old = (E) es[index];
        fastRemove(es, index);

        return old;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof List)) return false;

        final int emc = modCount;
        boolean eq = (o instanceof AbstractListType<?> a) ?
                equalsAbstractList(a) :
                equalsRange((List<?>) o, 0, size);

        checkMod(emc);
        return eq;
    }

    private boolean equalsRange(List<?> other, int start, int end) {
        final Object[] es = elementData;
        if (end > es.length) throw new ConcurrentModificationException();
        var oit = other.iterator();
        for (; start < end; start++) if (!oit.hasNext() || !Objects.equals(es[start], oit.next())) return false;
        return !oit.hasNext();
    }

    private boolean equalsAbstractList(@NotNull AbstractListType<?> other) {
        final int omc = other.modCount;
        final int s = size;
        boolean equal;

        if (equal = (s == other.size)) {
            final Object[] oes = other.elementData;
            final Object[] es = elementData;

            if (s > es.length || s > oes.length) throw new ConcurrentModificationException();
            for (int i = 0; i < s; i++)
                if (!Objects.equals(es[i], oes[i])) {
                    equal = false;
                    break;
                }
        }

        other.checkMod(omc);
        return equal;
    }

    private void checkMod(final int emc) {
        if (modCount != emc) throw new ConcurrentModificationException();
    }

    public int hashCode() {
        int emc = modCount;
        int h = hashCodeRange(0, size);
        checkMod(emc);
        return h;
    }

    int hashCodeRange(int start, int end) {
        final Object[] es = elementData;
        if (end > es.length) throw new ConcurrentModificationException();
        int hc = 1;
        for (int i = start; i < end; i++) {
            Object e = es[i];
            hc = 31 * hc + (e == null ? 0 : e.hashCode());
        }
        return hc;
    }

    public boolean remove(Object o) {
        final Object[] es = elementData;
        final int size = this.size;
        int i = 0;

        found:
        {
            if (o == null) {
                for (; i < size; i++)
                    if (es[i] == null)
                        break found;
            } else {
                for (; i < size; i++) {
                    if (o.equals(es[i]))
                        break found;
                }
            }
            return false;
        }

        fastRemove(es, i);
        return true;
    }

    private void fastRemove(Object[] es, int i) {
        modCount++;
        final int n;
        if ((n = size - 1) > i)
            System.arraycopy(es, i + 1, es, i, n - i);
        es[size = n] = null;
    }

    public void clear() {
        modCount++;
        final Object[] es = elementData;
        for (int to = size, i = size = 0; i < to; i++)
            es[i] = null;
    }

    public boolean addAll(@NotNull Collection<? extends E> collection) {
        Object[] a = collection.toArray();
        modCount++;
        int nn = a.length;
        if (nn == 0) return false;

        Object[] elementData;
        final int s;
        if (nn > (elementData = this.elementData).length - (s = size))
            elementData = grow(s + nn);
        System.arraycopy(a, 0, elementData, s, nn);
        size = s + nn;
        return true;
    }

    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        rangeCheck(index);

        Object[] a = c.toArray();
        modCount++;
        int nn = a.length;
        if (nn == 0) return false;

        Object[] elementData;
        final int s;
        if (nn > (elementData = this.elementData).length - (s = size))
            elementData = grow(s + nn);

        int nm = s - index;
        if (nm > 0)
            System.arraycopy(elementData,
                    index,
                    elementData,
                    index + nn,
                    nm);
        System.arraycopy(a, 0, elementData, index, nn);
        size = s + nn;
        return true;
    }

    protected void removeRange(int start, int end) {
        if (start > end) throw new IndexOutOfBoundsException(
                outOfBoundsMsg(start, end));
        modCount++;
        shiftTailOverGap(elementData, start, end);
    }

    private void shiftTailOverGap(Object[] es, int lo, int hi) {
        System.arraycopy(es, hi, es, lo, size - hi);
        for (int to = size, i = (size -= hi - lo); i < to; i++)
            es[i] = null;
    }

    public boolean removeAll(@NotNull Collection<?> c) {
        return batchRemove(c, false, 0, size);
    }

    public boolean retainAll(@NotNull Collection<?> c) {
        return batchRemove(c, true, 0, size);
    }

    boolean batchRemove(Collection<?> c, boolean cpt, final int f, final int e) {
        Objects.requireNonNull(c);
        final Object[] es = elementData;
        int r;
        for (r = f; ; r++) {
            if (r == e) return false;
            if (c.contains(es[r]) != cpt) break;
        }

        int w = r++;
        try {
            for (Object o; r < e; r++)
                if (c.contains(o = es[r]) == cpt)
                    es[w++] = o;
        } catch (Throwable th) {
            System.arraycopy(es, r, es, w, e - r);
            w += e - r;
            throw th;
        } finally {
            modCount += e - w;
            shiftTailOverGap(es, w, e);
        }
        return true;
    }

    public ListIterator<E> listIterator(int index) {
        rangeCheck(index);
        return new LstItr(index);
    }

    public ListIterator<E> listIterator() {
        return new LstItr(0);
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    public boolean containsAll(@NotNull Collection<?> c) {
        return true;
    }

    private class Itr implements Iterator<E> {
        int cur;
        int lastRet = -1;
        int emc = modCount;

        Itr() {
        }

        public boolean hasNext() {
            return cur != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            checkMod();
            int i = cur;
            if (i >= size) throw new NoSuchElementException();

            Object[] elementData = AbstractListType.this.elementData;

            if (i >= elementData.length) throw new ConcurrentModificationException();

            cur = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            checkMod();

            try {
                AbstractListType.this.remove(lastRet);
                cur = lastRet;
                lastRet = -1;
                emc = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int size = AbstractListType.this.size;
            int i = cur;
            if (i < size) {
                final Object[] es = elementData;
                if (i >= es.length) throw new ConcurrentModificationException();

                for (; i < size && modCount == emc; i++) action.accept(elementAt(es, i));

                cur = i;
                lastRet = i - 1;
                checkMod();
            }
        }

        final void checkMod() {
            if (modCount != emc) throw new ConcurrentModificationException();
        }
    }

    private class LstItr extends Itr implements ListIterator<E> {
        LstItr(int index) {
            super();
            cur = index;
        }

        public boolean hasPrevious() {
            return cur != 0;
        }

        public int nextIndex() {
            return cur;
        }

        public int previousIndex() {
            return cur - 1;
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            checkMod();
            int i = cur - 1;
            if (i < 0) throw new NoSuchElementException();

            Object[] elementData = AbstractListType.this.elementData;

            if (i >= elementData.length) throw new ConcurrentModificationException();

            cur = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            if (lastRet < 0) throw new IllegalStateException();
            checkMod();

            try {
                AbstractListType.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            checkMod();

            try {
                int i = cur;
                AbstractListType.this.add(i, e);
                cur = i + 1;
                lastRet = -1;
                emc = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public List<E> subList(int from, int to) {
        return new SubList<>(this, from, to);
    }

    private static class SubList<E> extends AbstractList<E> implements RandomAccess {
        private final AbstractListType<E> root;
        private final SubList<E> parent;
        private final int offset;
        private int size;

        public SubList(@NotNull AbstractListType<E> root, int from, int to) {
            this.root = root;
            this.parent = null;
            this.offset = from;
            this.size = to - from;
            this.modCount = root.modCount;
        }

        public SubList(SubList<E> parent, int from, int to) {
            this.root = parent.root;
            this.parent = parent;
            this.offset = parent.offset + from;
            this.size = to - from;
            this.modCount = parent.modCount;
        }

        public E set(int index, E element) {
            Objects.checkIndex(index, size);
            checkMod();
            E old = root.elementData(offset + index);
            root.elementData[offset + index] = element;
            return old;
        }

        public E get(int index) {
            Objects.checkIndex(index, size);
            checkMod();
            return root.elementData(offset + index);
        }

        public int size() {
            checkMod();
            return size;
        }

        public void add(int index, E element) {
            checkRange(index);
            checkMod();
            root.add(offset + index, element);
            updateSizeAndModCount(1);
        }

        public E remove(int index) {
            Objects.checkIndex(index, size);
            checkMod();
            E res = root.remove(offset + index);
            updateSizeAndModCount(-1);
            return res;
        }

        protected void removeRange(int start, int end) {
            checkMod();
            root.removeRange(offset + start, offset + end);
            updateSizeAndModCount(start - end);
        }

        public boolean addAll(@NotNull Collection<? extends E> c) {
            return addAll(this.size, c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            checkRange(index);
            int cs = c.size();
            if (cs == 0) return false;

            checkMod();
            root.addAll(offset + index, c);
            updateSizeAndModCount(cs);
            return true;
        }

        public void replaceAll(UnaryOperator<E> operator) {
            root.replaceAllRange(operator, offset, offset + size);
        }

        public boolean removeAll(@NotNull Collection<?> c) {
            return batchRemove(c, false);
        }

        public boolean retainAll(@NotNull Collection<?> c) {
            return batchRemove(c, true);
        }

        private boolean batchRemove(Collection<?> c, boolean complement) {
            checkMod();
            int os = root.size;
            boolean mod = root.batchRemove(c, complement, offset, offset + size);
            if (mod) updateSizeAndModCount(root.size - os);
            return mod;
        }

        public boolean removeIf(Predicate<? super E> filter) {
            checkMod();
            int os = root.size;
            boolean mod = root.removeIf(filter, offset, offset + size);
            if (mod) updateSizeAndModCount(root.size - os);
            return mod;
        }

        public Object @NotNull [] toArray() {
            checkMod();
            return Arrays.copyOfRange(root.elementData, offset, offset + size);
        }

        @SuppressWarnings("unchecked")
        public <T> T @NotNull [] toArray(T @NotNull [] a) {
            checkMod();
            if (a.length < size)
                return (T[]) Arrays.copyOfRange(root.elementData, offset, offset + size, a.getClass());
            System.arraycopy(root.elementData, offset, a, 0, size);
            if (a.length > size) a[size] = null;
            return a;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof List)) return false;
            boolean eq = root.equalsRange((List<?>) o, offset, offset + size);
            checkMod();
            return eq;
        }

        public int hashCode() {
            int h = root.hashCodeRange(offset, offset + size);
            checkMod();
            return h;
        }

        public int indexOf(Object o) {
            int i = root.indexOfRange(o, offset, offset + size);
            checkMod();
            return i >= 0 ? i - offset : -1;
        }

        public int lastIndexOf(Object o) {
            int i = root.lastIndexOfRange(o, offset, offset + size);
            checkMod();
            return i >= 0 ? i - offset : -1;
        }

        public boolean contains(Object o) {
            return indexOf(o) >= 0;
        }

        public @NotNull ListIterator<E> listIterator() {
            return listIterator(0);
        }

        public @NotNull ListIterator<E> listIterator(int index) {
            checkMod();
            checkRange(index);

            return new ListIterator<E>() {
                int cur = index;
                int lastRet = -1;
                int emc = SubList.this.modCount;

                public boolean hasNext() {
                    return cur != SubList.this.size;
                }

                @SuppressWarnings("unchecked")
                public E next() {
                    checkMod();
                    int i = cur;
                    if (i >= SubList.this.size)
                        throw new NoSuchElementException();
                    Object[] elementData = root.elementData;
                    if (offset + i >= elementData.length) throw new ConcurrentModificationException();

                    cur = i + 1;

                    return (E) elementData[offset + (lastRet = i)];
                }

                public boolean hasPrevious() {
                    return cur != 0;
                }

                @SuppressWarnings("unchecked")
                public E previous() {
                    checkMod();
                    int i = cur - 1;
                    if (i < 0) throw new NoSuchElementException();

                    Object[] es = root.elementData;
                    if (offset + i >= es.length) throw new ConcurrentModificationException();
                    cur = i;
                    return (E) es[offset + (lastRet = i)];
                }

                public void forEachRemaining(Consumer<? super E> action) {
                    Objects.requireNonNull(action);
                    final int size = SubList.this.size;
                    int i = cur;

                    if (i < size) {
                        final Object[] es = root.elementData;
                        if (offset + i >= es.length) throw new ConcurrentModificationException();
                        for (; i < size && root.modCount == emc; i++)
                            action.accept(elementAt(es, offset + i));

                        cur = i;
                        lastRet = i - 1;
                        checkMod();
                    }
                }

                public int nextIndex() {
                    return cur;
                }

                public int previousIndex() {
                    return cur - 1;
                }

                public void remove() {
                    if (lastRet < 0) throw new IllegalStateException();
                    checkMod();

                    try {
                        SubList.this.remove(lastRet);
                        cur = lastRet;
                        lastRet = -1;
                        emc = SubList.this.modCount;
                    } catch (IndexOutOfBoundsException e) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void set(E e) {
                    if (lastRet < 0) throw new IllegalStateException();
                    checkMod();

                    try {
                        root.set(offset + lastRet, e);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void add(E e) {
                    checkMod();

                    try {
                        int i = cur;
                        SubList.this.add(i, e);
                        cur = i + 1;
                        lastRet = -1;
                        emc = SubList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                void checkMod() {
                    if (root.modCount != emc) throw new ConcurrentModificationException();
                }
            };
        }

        @Contract("_, _ -> new")
        public @NotNull List<E> subList(int from, int to) {
            ListSupport.subListRangeCheck(from, to, size);
            return new SubList<>(this, from, to);
        }

        private void checkRange(int index) {
            if (index < 0 || index > this.size) throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        @Contract(pure = true)
        private @NotNull String outOfBoundsMsg(int index) {
            return "Index: " + index + ", Size:" + this.size;
        }

        final void checkMod() {
            if (root.modCount == modCount)
                throw new ConcurrentModificationException();
        }

        private void updateSizeAndModCount(int change) {
            SubList<E> s = this;
            do {
                s.size += change;
                s.modCount = root.modCount;
                s = s.parent;
            } while (s != null);
        }

        public Spliterator<E> spliterator() {
            checkMod();

            return new Spliterator<E>() {
                private int index = offset;
                private int fence = -1;
                private int emc;

                private int getFence() {
                    int hi;
                    if ((hi = fence) < 0) {
                        emc = modCount;
                        hi = fence = offset + size;
                    }

                    return hi;
                }

                public AbstractListType<E>.ListSpliterator trySplit() {
                    int hi = getFence(), lo = index, mid = (hi + lo) >>> 1;
                    return (lo >= mid) ? null :
                            root.new ListSpliterator(lo, index = mid, emc);
                }

                public boolean tryAdvance(Consumer<? super E> action) {
                    Objects.requireNonNull(action);

                    int hi = getFence(), i = index;

                    if (i < hi) {
                        index = i + 1;
                        @SuppressWarnings("unchecked") E e = (E) root.elementData[i];
                        action.accept(e);
                        if (root.modCount != emc) throw new ConcurrentModificationException();
                        return true;
                    }

                    return false;
                }

                public void forEachRemaining(Consumer<? super E> action) {
                    Objects.requireNonNull(action);
                    int i, hi, mc;

                    AbstractListType<E> listType = root;
                    Object[] a;

                    if ((a = listType.elementData) != null) {
                        if ((hi = fence) < 0) {
                            mc = modCount;
                            hi = offset + size;
                        }
                        else
                            mc = emc;

                        if ((i = index) >= 0 && (index = hi) <= a.length) {
                            for (; i < hi; ++i) {
                                @SuppressWarnings("unchecked") E e = (E) a[i];
                                action.accept(e);
                            }

                            if (listType.modCount == mc) return;
                        }
                    }

                    throw new ConcurrentModificationException();
                }

                public long estimateSize() {
                    return getFence() - index;
                }

                public int characteristics() {
                    return ORDERED | SIZED | SUBSIZED;
                }
            };
        }

        public @NotNull String toString() {
            return root.toString(offset, offset + size);
        }
    }


    private void rangeCheck(int index) {
        if (index > size || index < 0) throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    @Contract(pure = true)
    private static @NotNull String outOfBoundsMsg(int start, int end) {
        return "From Index: " + start + " > To Index: " + end;
    }

    @Contract(pure = true)
    private @NotNull String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    public void replaceAll(UnaryOperator<E> operator) {
        replaceAllRange(operator, 0, size);
    }

    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        final int emc = modCount;
        final Object[] es = elementData;
        final int size = this.size;

        for (int i = 0; modCount == emc && i < size; i++) {
            action.accept(elementAt(es, i));
        }

        if (modCount != emc) throw new ConcurrentModificationException();
    }


    final class ListSpliterator implements Spliterator<E> {
        private int index;
        private int fence;
        private int emc;

        ListSpliterator(int origin, int fence, int emc) {
            index = origin;
            this.fence = fence;
            this.emc = emc;
        }

        private int getFence() {
            int hi;

            if ((hi = fence) < 0) {
                emc = modCount;
                hi = fence = size;
            }

            return hi;
        }

        public @Nullable ListSpliterator trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null :
                    new ListSpliterator(lo, index = mid, emc);
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            if (action == null) throw new NullPointerException();
            int hi = getFence(), i = index;
            if (i < hi) {
                index = i + 1;
                @SuppressWarnings("unchecked") E e = (E) elementData[i];
                action.accept(e);
                if (modCount != emc) throw new ConcurrentModificationException();
                return true;
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            int i, hi, mc;
            Object[] a;

            if (action == null) throw new NullPointerException();

            if ((a = elementData) != null) {
                if ((hi = fence) < 0) {
                    mc = modCount;
                    hi = size;
                } else
                    mc = emc;

                if ((i = index) >= 0 && (index = hi) <= a.length) {
                    for (; i < hi; ++i) {
                        @SuppressWarnings("unchecked") E e = (E) a[i];
                        action.accept(e);
                    }
                    if (modCount == mc) return;
                }
            }

            throw new ConcurrentModificationException();
        }

        public long estimateSize() {
            return getFence() - index;
        }

        public int characteristics() {
            return ORDERED | SIZED | SUBSIZED;
        }
    }

    @Contract(value = "_ -> new", pure = true)
    private static long @NotNull [] lb(int n) {
        return new long[((n - 1) >> 6) + 1];
    }

    private static void setBit(long @NotNull [] b, int i) {
        b[i >> 6] |= 1L << i;
    }

    private static boolean isClear(long @NotNull [] b, int i) {
        return (b[i >> 6] & (1L << i)) == 0;
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return removeIf(filter, 0, size);
    }

    boolean removeIf(Predicate<? super E> filter, int i, final int end) {
        Objects.requireNonNull(filter);
        int emc = modCount;
        final Object[] es = elementData;
        // noinspection StatementWithEmptyBody
        for (; i < end && !filter.test(elementAt(es, i)); i++) ;

        if (i < end) {
            final int b = i;
            final long[] dr = lb(end - b);
            dr[0] = 1L;
            for (i = b + 1; i < end; i++)
                if (filter.test(elementAt(es, i)))
                    setBit(dr, i - b);
            if (modCount != emc)
                throw new ConcurrentModificationException();
            modCount++;

            int w = b;
            for (i = b; i < end; i++)
                if (isClear(dr, i - b))
                    es[w++] = es[i];
            shiftTailOverGap(es, w, end);
            return true;
        } else {
            if (modCount != emc)
                throw new ConcurrentModificationException();
            return false;
        }
    }

    private void replaceAllRange(UnaryOperator<E> operator, int i, int end) {
        Objects.requireNonNull(operator);
        final int emc = modCount;
        final Object[] es = elementData;
        for (; modCount == emc && i < end; i++)
            es[i] = operator.apply(elementAt(es, i));
        if (modCount != emc)
            throw new ConcurrentModificationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super E> c) {
        final int emc = modCount;
        Arrays.sort((E[]) elementData, 0, size, c);
        if (modCount != emc) throw new ConcurrentModificationException();
        modCount++;
    }

    public AbstractListType<E> copy() {
        AbstractListType<E> lst = clone();
        lst.modCount = 0;
        lst.elementData = elementData;
        return lst;
    }

    @SuppressWarnings("unchecked")
    public E[] getValue() {
        return (E[]) elementData;
    }

    public String toString() {
        if (isEmpty()) return "ListType { contents: [] }";
        return toString(0, size - 1);
    }

    private @NotNull String toString(int from, int to) {
        StringBuilder s = new StringBuilder("ListType { contents: [");
        ListSupport.subListRangeCheck(from, to, size);

        for (; ; from++) {
            s.append(elementData[from] == null ? "null" : elementData[from].toString());
            if (from == to) return s.append("] }").toString();
            s.append(", ");
        }
    }
}
