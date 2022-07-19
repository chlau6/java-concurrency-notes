package listing2p5;

import annotation.NotThreadSafe;

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

class ServletRequest {
}

class ServletResponse {
}

interface Servlet {
    void service(ServletRequest req, ServletResponse resp);
}

/*
Even though the atomic references are individually thread-safe,
UnsafeCachingFactorizer has race conditions that could make it produce the wrong answer.

The definition of thread safety requires that invariants be preserved regardless of
timing or interleaving of operations in multiple threads.

One invariant of UnsafeCachingFactorizer is that the product of the factors cached in lastFactors
equal the value cached in lastNumber; our servlet is correct only if this invariant always holds.

When multiple variables participate in an invariant, they are not independent:
the value of one constrains the allowed value(s) of the others.

Thus when updating one, you must update the others in the same atomic operation.
 */
