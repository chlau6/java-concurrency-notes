package listing3p14;

import listing3p15.Holder;

public class UnsafePublication {
    // Unsafe publication
    public Holder holder;

    public void initialize() {
        holder = new Holder(42);
    }
}

/*
So far we have focused on ensuring that an object not be published,
such as when it is supposed to be confined to a thread or within another object.
Of course, sometimes we do want to share objects across threads, and in this case we must do so safely.

Unfortunately, simply storing a reference to an object into a public field, is not enough to publish that object safely.

Because of visibility problems, the Holder could appear to another thread to be in an inconsistent state,
even though its invariants were properly established by its constructor!

This improper publication could allow another thread to observe a partially constructed object.
 */
