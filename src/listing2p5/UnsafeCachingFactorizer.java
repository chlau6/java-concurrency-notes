package listing2p5;

import annotation.NotThreadSafe;
import dummy.Servlet;
import dummy.ServletRequest;
import dummy.ServletResponse;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

@NotThreadSafe
public class UnsafeCachingFactorizer implements Servlet {
    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<>();

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);

        if (i.equals(lastNumber.get())) {
            encodeIntoResponse(resp, lastFactors.get());
        } else {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            encodeIntoResponse(resp, factors);
        }
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
We used AtomicLong to manage the counter state in a thread-safe manner;
could we perhaps use its cousin, AtomicReference, to manage the last number and its factors?

Unfortunately, this approach does not work. Even though the atomic references are individually thread-safe,
UnsafeCachingFactorizer has race conditions that could make it produce the wrong answer.

The definition of thread safety requires that invariants be preserved regardless of
timing or interleaving of operations in multiple threads.

One invariant of UnsafeCachingFactorizer is that the product of the factors cached in lastFactors
equal the value cached in lastNumber; our servlet is correct only if this invariant always holds.

When multiple variables participate in an invariant, they are not independent:
the value of one constrains the allowed value(s) of the others.

Thus when updating one, you must update the others in the same atomic operation.

With some unlucky timing, UnsafeCachingFactorizer can violate this invariant.

Using atomic references, we cannot update both lastNumber and lastFactors simultaneously,
even though each call to set is atomic;
there is still a window of vulnerability when one has been modified and the other has not,
and during that time other threads could see that the invariant does not hold.

Similarly, the two values cannot be fetched simultaneously:
between the time when thread A fetches the two values, thread B could have changed them,
and again A may observe that the invariant does not hold.
 */
