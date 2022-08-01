package listing2p6;

import annotation.GuardedBy;
import annotation.ThreadSafe;
import dummy.Servlet;
import dummy.ServletRequest;
import dummy.ServletResponse;

import java.math.BigInteger;
import java.util.Vector;

@ThreadSafe
public class SynchronizedFactorizer implements Servlet {
    @GuardedBy("this")
    private BigInteger lastNumber;

    @GuardedBy("this")
    private BigInteger[] lastFactors;

    @Override
    public synchronized void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);

        if (i.equals(lastNumber)) {
            encodeIntoResponse(resp, lastFactors);
        } else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
            encodeIntoResponse(resp, factors);
        }
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
The machinery of synchronization makes it easy to restore thread safety to the factoring servlet.

Listing 2.6 makes the service method synchronized, so only one thread may enter service at a time.

SynchronizedFactorizer is now thread-safe; however, this approach is fairly extreme,
since it inhibits multiple clients from using the factoring servlet simultaneously at all,
resulting in unacceptably poor responsiveness.

This problem - which is a performance problem, not a thread safety problem.
 */
