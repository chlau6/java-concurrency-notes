package listing2p8;

import annotation.GuardedBy;
import annotation.ThreadSafe;
import dummy.Servlet;
import dummy.ServletRequest;
import dummy.ServletResponse;

import java.math.BigInteger;

@ThreadSafe
public class CachedFactorizer implements Servlet {
    @GuardedBy("this")
    private BigInteger lastNumber;

    @GuardedBy("this")
    private BigInteger[] lastFactors;

    @GuardedBy("this")
    private long hits;

    @GuardedBy("this")
    private long cacheHits;

    public synchronized long getHits() {
        return hits;
    }

    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double) hits;
    }

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = null;
        synchronized (this) {
            ++hits;
            if (i.equals(lastNumber)) {
                ++cacheHits;
                factors = lastFactors.clone();
            }
        }

        if (factors == null) {
            factors = factor(i);
            synchronized (this) {
                lastNumber = i;
                lastFactors = factors.clone();
            }
        }

        encodeIntoResponse(resp, factors);
    }

    /* Dummy Implementation */
    private BigInteger extractFromRequest(ServletRequest req) {
        return BigInteger.ONE;
    }

    /* Dummy Implementation */
    private BigInteger[] factor(BigInteger i) {
        return new BigInteger[] {BigInteger.ONE};
    }

    /* Dummy Implementation */
    private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {

    }
}

/*
CachedFactorizer in Listing 2.8 restructures the servlet to use two separate synchronized blocks,
each limited to a short section of code.

One guards the check-then-act sequence that tests whether we can just return the cached result,
and the other guards updating both the cached number and the cached factors.

As a bonus, we've reintroduced the hit counter and added a "cache hit" counter as well,
updating them within the initial synchronized block.

Because these counters constitute shared mutable state as well,
we must use synchronization everywhere they are accessed.

The portions of code that are outside the synchronized blocks operate exclusively on local (stack-based) variables,
which are not shared across threads and therefore do not require synchronization.
 */
