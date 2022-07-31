package listing5p20;

import annotation.ThreadSafe;
import dummy.Servlet;
import dummy.ServletRequest;
import dummy.ServletResponse;
import listing5p16.Computable;
import listing5p19.Memoizer;

import java.math.BigInteger;

@ThreadSafe
public class Factorizer implements Servlet {
    private final Computable<BigInteger, BigInteger[]> c = new Computable<BigInteger, BigInteger[]>() {
        public BigInteger[] compute(BigInteger arg) {
            return factor(arg);
        }
    };

    private final Computable<BigInteger, BigInteger[]> cache = new Memoizer<BigInteger, BigInteger[]>(c);
    public void service(ServletRequest req, ServletResponse resp) {
        try {
            BigInteger i = extractFromRequest(req);
            encodeIntoResponse(resp, cache.compute(i));
        } catch (InterruptedException e) {
            encodeError(resp, "factorization interrupted");
        }
    }

    /*
    Dummy Implementation
     */
    private BigInteger[] factor(BigInteger arg) {
        return new BigInteger[] {};
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
    private void encodeError(ServletResponse resp, String factorizationInterrupted) {
    }

    /*
    Dummy Implementation
     */
    private void encodeIntoResponse(ServletResponse resp, BigInteger[] compute) {
    }
}
