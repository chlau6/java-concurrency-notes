package listing15p5;

import annotation.ThreadSafe;
import dummy.PseudoRandom;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe
public class AtomicPseudoRandom extends PseudoRandom {
    private AtomicInteger seed;

    AtomicPseudoRandom(int seed) {
        this.seed = new AtomicInteger(seed);
    }

    public int nextInt(int n) {
        while (true) {
            int s = seed.get();
            int nextSeed = calculateNext(s);

            if (seed.compareAndSet(s, nextSeed)) {
                int remainder = s % n;
                return remainder > 0 ? remainder : remainder + n;
            }
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