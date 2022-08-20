package listing15p3;

import annotation.Immutable;

import java.util.concurrent.atomic.AtomicReference;

public class CasNumberRange {

    @Immutable
    private static class IntPair {
        final int lower; // Invariant: lower <= upper
        final int upper;

        private IntPair(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }
    }

    private final AtomicReference<IntPair> values = new AtomicReference<IntPair>(new IntPair(0, 0));

    public int getLower() {
        return values.get().lower;
    }

    public int getUpper() {
        return values.get().upper;
    }

    public void setLower(int i) {
        while (true) {
            IntPair oldv = values.get();
            if (i > oldv.upper) {
                throw new IllegalArgumentException("Can't set lower to " + i + " > upper");
            }

            IntPair newv = new IntPair(i, oldv.upper);
            if (values.compareAndSet(oldv, newv)) {
                return;
            }
        }
    }
    // similarly for setUpper
}

/*
We can combine the technique from OneValueCache with atomic references to close the race condition by
atomically updating the reference to an immutable object holding the lower and upper bounds.
CasNumberRange in Listing 15.3 uses an AtomicReference to an IntPair to hold the state;
by using compareAndSet it can update the upper or lower bound without the race conditions of NumberRange.
*/