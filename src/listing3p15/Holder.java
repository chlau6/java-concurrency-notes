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
You cannot rely on the integrity of partially constructed objects.

An observing thread could see the object in an inconsistent state, and then later see its state suddenly change,
even though it has not been modified since publication.

In fact, if the Holder is published using the unsafe publication idiom,
and a thread other than the publishing thread were to call assertSanity, it could throw AssertionError!
 */

