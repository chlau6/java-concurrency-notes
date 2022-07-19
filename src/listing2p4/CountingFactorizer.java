package listing2p4;

import annotation.ThreadSafe;

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

class ServletRequest {
}

class ServletResponse {
}

interface Servlet {
    void service(ServletRequest req, ServletResponse resp);
}

/*
The java.util.concurrent.atomic package contains atomic variable classes
for effecting atomic state transitions on numbers and object references.

By replacing the long counter with an AtomicLong, we ensure that all actions that access the counter state are atomic.

Because the state of the servlet is the state of the counter and the counter is thread-safe,
our servlet is once again thread-safe.
 */
