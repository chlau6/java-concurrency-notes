package listing5p17;

import listing5p16.Computable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Memoizer2<A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new ConcurrentHashMap<A, V>();
    private final Computable<A, V> c;

    public Memoizer2(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);

        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }

        return result;
    }
}

/*
Memoizer2 in Listing 5.17 improves on the awful concurrent behavior of Memoizer1 by
replacing the HashMap with a ConcurrentHashMap.

Since ConcurrentHashMap is thread-safe, there is no need to synchronize when accessing the backing Map,
thus eliminating the serialization induced by synchronizing compute in Memoizer1.

The problem with Memoizer2 is that if one thread starts an expensive computation,
other threads are not aware that the computation is in progress and so may start the same computation.
 */