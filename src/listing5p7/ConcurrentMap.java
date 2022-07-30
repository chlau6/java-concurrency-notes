package listing5p7;

import java.util.Map;

public interface ConcurrentMap<K,V> extends Map<K,V> {
    // Insert into map only if no value is mapped from K
    V putIfAbsent(K key, V value);

    // Remove only if K is mapped to V
    boolean remove(Object key, Object value);

    // Replace value only if K is mapped to oldValue
    boolean replace(K key, V oldValue, V newValue);

    // Replace value only if K is mapped to some value
    V replace(K key, V newValue);
}

/*
Since a ConcurrentHashMap cannot be locked for exclusive access,
we cannot use client-side locking to create new atomic operations such as put-if-absent,
as we did for Vector in Listing 4.15.

Instead, a number of common compound operations such as put-if-absent, remove-if-equal,
and replace-if-equal are implemented as atomic operations and specified by the ConcurrentMap interface,
shown in Listing 5.7.

If you find yourself adding such functionality to an existing synchronized Map implementation,
it is probably a sign that you should consider using a ConcurrentMap instead.
 */