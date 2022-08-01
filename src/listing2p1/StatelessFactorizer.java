package listing2p1;

import annotation.ThreadSafe;
import dummy.Servlet;
import dummy.ServletRequest;
import dummy.ServletResponse;

import java.math.BigInteger;

@ThreadSafe
public class StatelessFactorizer implements Servlet {
    @Override
    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
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
Listing 2.1 shows our simple factorization servlet.
It unpacks the number to be factored from the servlet request, factors it,
and packages the results into the servlet response.

StatelessFactorizer is stateless: it has no fields and references no fields from other classes.

The transient state for a particular computation exists solely in local variables that are stored on the thread's stack
and are accessible only to the executing thread.

One thread accessing a StatelessFactorizer cannot influence the result of another thread accessing the same
StatelessFactorizer; because the two threads do not share state, it is as if they were accessing different instances.

Since the actions of a thread accessing a stateless object cannot affect the correctness of operations in other threads,
STATELESS OBJECTS ARE THREADSAFE.
 */