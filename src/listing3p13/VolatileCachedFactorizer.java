package listing3p13;

import annotation.ThreadSafe;
import dummy.Servlet;
import dummy.ServletRequest;
import dummy.ServletResponse;
import listing3p12.OneValueCache;

import java.math.BigInteger;

@ThreadSafe
public class VolatileCachedFactorizer implements Servlet {
    private volatile OneValueCache cache = new OneValueCache(null, null);
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = cache.getFactors(i);
        if (factors == null) {
            factors = factor(i);
            cache = new OneValueCache(i, factors);
        }

        encodeIntoResponse(resp, factors);
    }

    /* Dummy Implementation */
    private BigInteger extractFromRequest(ServletRequest req) {
        return BigInteger.ONE;
    }

    /* Dummy Implementation */
    private BigInteger[] factor(BigInteger i) {
        return new BigInteger[] {};
    }

    /* Dummy Implementation */
    private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {

    }
}

/*
VolatileCachedFactorizer in Listing 3.13 uses a OneValueCache to store the cached number and factors.
When a thread sets the volatile cache field to reference a new OneValueCache,
the new cached data becomes immediately visible to other threads.

The cache-related operations cannot interfere with each other because
OneValueCache is immutable and the cache field is accessed only once in each of the relevant code paths.

This combination of an immutable holder object for multiple state variables related by an invariant,
and a volatile reference used to ensure its timely visibility,
allows VolatileCachedFactorizer to be thread-safe even though it does no explicit locking.
 */
