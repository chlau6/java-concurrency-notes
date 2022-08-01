package listing5p19;

import listing5p16.Computable;

import java.util.concurrent.*;

import static listing5p13.UncheckedExceptionWrapper.launderThrowable;

public class Memoizer<A, V> implements Computable<A, V> {
    private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();
    private final Computable<A, V> c;

    public Memoizer(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(final A arg) throws InterruptedException {
        while (true) {
            Future<V> f = cache.get(arg);

            if (f == null) {
                Callable<V> eval = new Callable<V>() {
                    public V call() throws InterruptedException {
                        return c.compute(arg);
                    }
                };

                FutureTask<V> ft = new FutureTask<V>(eval);
                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }

            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg, f);
            } catch (ExecutionException e) {
                throw launderThrowable(e.getCause());
            }
        }
    }
}

/*
Memoizer3 is vulnerable to this problem because a compound action (put-if-absent) is performed on the backing map that
cannot be made atomic using locking.

Memoizer in Listing 5.19 takes advantage of the atomic putIfAbsent method of ConcurrentMap,
closing the window of vulnerability in Memoizer3.

Caching a Future instead of a value creates the possibility of cache pollution: if a computation is cancelled or fails,
future attempts to compute the result will also indicate cancellation or failure.

To avoid this, Memoizer removes the Future from the cache if it detects that the computation was cancelled;
it might also be desirable to remove the Future upon detecting a RuntimeException if
the computation might succeed on a future attempt.

Memoizer also does not address cache expiration,  but this could be accomplished by using a subclass of FutureTask that
associates an expiration time with each result and periodically scanning the cache for expired entries.
(Similarly, it does not address cache eviction,
where old entries are removed to make room for new ones so that the cache does not consume too much memory.)
 */
