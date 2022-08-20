package listing15p8;

import annotation.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

@ThreadSafe
public class AtomicFieldUpdaterDemo<E> {
    private static class Node<E> {
        private final E item;
        private volatile Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }
    private static AtomicReferenceFieldUpdater<Node, Node> nextUpdater =
            AtomicReferenceFieldUpdater.newUpdater(Node.class, Node.class, "next");
}

/*
Listing 15.7 illustrates the algorithm used by ConcurrentLinkedQueue, but the actual implementation is a bit different.
Instead of representing each Node with an atomic reference,
ConcurrentLinkedQueue uses an ordinary volatile reference and
updates it through the reflection-based AtomicReferenceFieldUpdater, as shown in Listing 15.8.

The atomic field updater classes (available in Integer, Long, and Reference versions)
represent a reflection-based "view" of an existing volatile field so that CAS can be used on existing volatile fields.
The updater classes have no constructors; to create one, you call the newUpdater factory method,
specifying the class and field name. The field updater classes are not tied to a specific instance;
one can be used to update the target field for any instance of the target class.
The atomicity guarantees for the updater classes are weaker than for the regular atomic classes because
you cannot guarantee that the underlying fields will not be modified directly -
the compareAndSet and arithmetic methods guarantee atomicity
only with respect to other threads using the atomic field updater methods.
*/