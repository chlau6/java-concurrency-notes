package listing2p4;

import annotation.ThreadSafe;
import dummy.Servlet;
import dummy.ServletRequest;
import dummy.ServletResponse;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

@ThreadSafe
public class CountingFactorizer implements Servlet {
    private final AtomicLong count = new AtomicLong(0);

    public long getCount() {
        return count.get();
    }

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        count.incrementAndGet();
        encodeIntoResponse(resp, factors);
    }

    /*
    Dummy Implementation
     */
    private BigInteger extractFromRequest(ServletRequest req) {
        return BigInteger.ONE;
    }

    /*
    Dummy Implementation
     */
    private BigInteger[] factor(BigInteger i) {
        return new BigInteger[] {BigInteger.ONE};
    }

    /*
    Dummy Implementation
     */
    private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {

    }
}

/*
To ensure thread safety, check-then-act operations (like lazy initialization)
and read-modify-write operations (like increment) must always be atomic.
We refer collectively to check-then-act and read-modify-write sequences as compound actions:
sequences of operations that must be executed atomically in order to remain thread-safe.

CountingFactorizer in Listing 2.4 uses the java.util.concurrent.atomic package that contains atomic variable classes
for effecting atomic state transitions on numbers and object references.

By replacing the long counter with an AtomicLong, we ensure that all actions that access the counter state are atomic.

Because the state of the servlet is the state of the counter and the counter is thread-safe,
our servlet is once again thread-safe.
 */
