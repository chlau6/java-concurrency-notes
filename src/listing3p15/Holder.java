package listing3p15;

public class Holder {
    private int n;

    public Holder(int n) {
        this.n = n;
    }

    public void assertSanity() {
        if (n != n) {
            throw new AssertionError("This statement is false.");
        }
    }
}

/*
Because synchronization was not used to make the Holder visible to other threads,
we say the Holder was not properly published.

Two things can go wrong with improperly published objects.

Other threads could see a stale value for the holder field,
and thus see a null reference or other older value even though a value has been placed in holder.

But far worse, other threads could see an up-to-date value for the holder reference,
but stale values for the state of the Holder.

To make things even less predictable,
a thread may see a stale value the first time it reads a field and then a more up-to-date value the next time,
which is why assertSanity can throw AssertionError.
 */

