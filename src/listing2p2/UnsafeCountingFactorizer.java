package listing2p2;

import annotation.NotThreadSafe;
import dummy.Servlet;
import dummy.ServletRequest;
import dummy.ServletResponse;

import java.math.BigInteger;

@NotThreadSafe
public class UnsafeCountingFactorizer implements Servlet {
    private long count = 0;

    public long getCount() {
        return count;
    }

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        ++count;
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
What happens when we add one element of state to what was a stateless object?
Suppose we want to add a "hit counter" that measures the number of requests processed.
The obvious approach is to add a long field to the servlet and increment it on each request,
as shown in UnsafeCountingFactorizer in Listing 2.2.

While the increment operation, ++count, may look like a single action because of its compact syntax, it is not ATOMIC,
which means that it does not execute as a single, indivisible operation.

Instead, it is a shorthand for a sequence of three discrete operations: fetch the current value, add one to it,
and write the new value back.

This is an example of a read-modify-write operation, in which the resulting state is derived from the previous state

If the counter is initially 9, with some unlucky timing each thread could read the value, see that it is 9,
add one to it, and each set the counter to 10.

This is clearly not what is supposed to happen; an increment got lost along the way,
and the hit counter is now permanently off by one.
 */
