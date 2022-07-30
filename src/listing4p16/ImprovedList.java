package listing4p16;

import annotation.ThreadSafe;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@ThreadSafe
public class ImprovedList<T> implements List<T> {
    private final List<T> list;

    public ImprovedList(List<T> list) {
        this.list = list;
    }

    public synchronized boolean putIfAbsent(T x) {
        boolean absent = !list.contains(x);
        if (absent) {
            list.add(x);
        }

        return absent;
    }

    public synchronized void clear() {
        list.clear();
    }
    // ... similarly delegate other List methods

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public T get(int index) {
        return null;
    }

    @Override
    public T set(int index, T element) {
        return null;
    }

    @Override
    public void add(int index, T element) {

    }

    @Override
    public T remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }
}

/*
There is a less fragile alternative for adding an atomic operation to an existing class: composition.
ImprovedList in Listing 4.16 implements the List operations by delegating them to an underlying List instance,
and adds an atomic putIfAbsent method.

ImprovedList adds an additional level of locking using its own intrinsic lock.
It does not care whether the underlying List is thread-safe, because it provides its own consistent locking that
provides thread safety even if the List is not thread-safe or changes its locking implementation.

While the extra layer of synchronization may add some small performance penalty,
the implementation in ImprovedList is less fragile than attempting to mimic the locking strategy of another object.
In effect, we’ve used the Java monitor pattern to encapsulate an existing List,
and this is guaranteed to provide thread safety so long as
our class holds the only outstanding reference to the underlying List.
 */
