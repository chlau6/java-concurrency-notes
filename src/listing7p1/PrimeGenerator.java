package listing7p1;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@ThreadSafe
public class PrimeGenerator implements Runnable {
    @GuardedBy("this")
    private final List<BigInteger> primes = new ArrayList<BigInteger>();
    private volatile boolean cancelled;

    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!cancelled) {
            p = p.nextProbablePrime();
            synchronized (this) {
                primes.add(p);
            }
        }
    }
    public void cancel() {
        cancelled = true;
    }

    public synchronized List<BigInteger> get() {
        return new ArrayList<BigInteger>(primes);
    }
}

/*
There is no safe way to preemptively stop a thread in Java, and therefore no safe way to preemptively stop a task.
There are only cooperative mechanisms,
by which the task and the code requesting cancellation follow an agreed-upon protocol.

One such cooperative mechanism is setting a "cancellation requested" flag that the task checks periodically;
if it finds the flag set, the task terminates early.
PrimeGenerator in Listing 7.1, which enumerates prime numbers until it is cancelled, illustrates this technique.
The cancel method sets the cancelled flag, and the main loop polls this flag before searching for the next prime number.
(For this to work reliably, cancelled must be volatile.)
 */
