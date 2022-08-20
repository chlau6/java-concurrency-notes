package listing15p4;

import annotation.Immutable;
import annotation.ThreadSafe;
import dummy.PseudoRandom;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe
public class ReentrantLockPseudoRandom extends PseudoRandom {
    private final Lock lock = new ReentrantLock(false);
    private int seed;

    ReentrantLockPseudoRandom(int seed) {
        this.seed = seed;
    }

    public int nextInt(int n) {
        lock.lock();
        try {
            int s = seed;
            seed = calculateNext(s);
            int remainder = s % n;
            return remainder > 0 ? remainder : remainder + n;
        } finally {
            lock.unlock();
        }
    }
}

/*
Listings 15.4 and 15.5 show two implementations of a thread-safe PRNG,
one using ReentrantLock and the other using AtomicInteger.
The test driver invokes each repeatedly; each iteration generates a random number
(which fetches and modifies the shared seed state) and
also performs a number of "busy-work" iterations that operate strictly on thread-local data.
This simulates typical operations that include some portion of operating on shared state and
some portion of operating on thread-local state.
*/