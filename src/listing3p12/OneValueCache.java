package listing3p12;

import annotation.Immutable;

import java.math.BigInteger;
import java.util.Arrays;

@Immutable
public class OneValueCache {
    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;

    public OneValueCache(BigInteger i, BigInteger[] factors) {
        lastNumber = i;
        lastFactors = Arrays.copyOf(factors, factors.length);
    }

    public BigInteger[] getFactors(BigInteger i) {
        if (lastNumber == null || !lastNumber.equals(i)) {
            return null;
        } else {
            return Arrays.copyOf(lastFactors, lastFactors.length);
        }
    }
}

/*
In UnsafeCachingFactorizer on page, we tried to use two AtomicReferences to store the last number and last factors,
but this was not thread-safe because we could not fetch or update the two related values atomically.
Using volatile variables for these values would not be thread-safe for the same reason.
However, immutable objects can sometimes provide a weak form of atomicity.

The factoring servlet performs two operations that must be atomic:
updating the cached result and conditionally fetching the cached factors if
the cached number matches the requested number.
Whenever a group of related data items must be acted on atomically,
consider creating an immutable holder class for them, such as OneValueCache in Listing 3.12.

Race conditions in accessing or updating multiple related variables can be eliminated by
using an immutable object to hold all the variables.

With a mutable holder object, you would have to use locking to ensure atomicity;
with an immutable one, once a thread acquires a reference to it,
it need never worry about another thread modifying its state.

If the variables are to be updated, a new holder object is created,
but any threads working with the previous holder still see it in a consistent state.
 */
