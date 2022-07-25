package listing3p14;

public class UnsafePublication {
    // Unsafe publication
    public Holder holder;

    public void initialize() {
        holder = new Holder(42);
    }
}

class Holder {
    public Holder(int val) {

    }
}

/*
Simply storing a reference to an object into a public field, is not enough to publish that object safely.

Because of visibility problems, the Holder could appear to another thread to be in an inconsistent state,
even though its invariants were properly established by its constructor!

This improper publication could allow another thread to observe a partially constructed object.
 */
