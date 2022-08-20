package listing15p6;

import annotation.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class ConcurrentStack <E> {
    AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();

    public void push(E item) {
        Node<E> newHead = new Node<E>(item);
        Node<E> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }

    public E pop() {
        Node<E> oldHead;
        Node<E> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null) {
                return null;
            }

            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));

        return oldHead.item;
    }

    private static class Node <E> {
        public final E item;
        public Node<E> next;
        public Node(E item) {
            this.item = item;
        }
    }
}

/*
Nonblocking algorithms are considerably more complicated than their lock-based equivalents.
The key to creating nonblocking algorithms is figuring out how to limit the scope of atomic changes to
a single variable while maintaining data consistency.  In linked collection classes such as queues,
you can sometimes get away with expressing state transformations as changes to individual links and
using an AtomicReference to represent each link that must be updated atomically.

Stacks are the simplest linked data structure:
each element refers to only one other element and each element is referred to by only one object reference.
ConcurrentStack in Listing 15.6 shows how to construct a stack using atomic references.
The stack is a linked list of Node elements, rooted at top,
each of which contains a value and a link to the next element.
The push method prepares a new link node whose next field refers to the current top of the stack,
and then uses CAS to try to install it on the top of the stack.
If the same node is still on the top of the stack as when we started, the CAS succeeds;
if the top node has changed (because another thread has added or removed elements since we started),
the CAS fails and push updates the new node based on the current stack state and tries again.
In either case, the stack is still in a consistent state after the CAS.

CasCounter and ConcurrentStack illustrate characteristics of all nonblocking algorithms:
some work is done speculatively and may have to be redone.
In ConcurrentStack, when we construct the Node representing the new element,
we are hoping that the value of the next reference will still be correct by the time it is installed on the stack,
but are prepared to retry in the event of contention.

Nonblocking algorithms like ConcurrentStack derive their thread safety from the fact that, like locking,
compareAndSet provides both atomicity and visibility guarantees. When a thread changes the state of the stack,
it does so with a compareAndSet, which has the memory effects of a volatile write.
When a thread examines the stack, it does so by calling get on the same AtomicReference,
which has the memory effects of a volatile read.
So any changes made by one thread are safely published to any other thread that examines the state of the list.
And the list is modified with a compareAndSet that atomically either updates the top reference or
fails if it detects interference from another thread.
*/