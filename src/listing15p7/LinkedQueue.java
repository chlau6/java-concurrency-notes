package listing15p7;

import annotation.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class LinkedQueue <E> {
    private static class Node <E> {
        final E item;
        final AtomicReference<Node<E>> next;

        public Node(E item, Node<E> next) {
            this.item = item;
            this.next = new AtomicReference<Node<E>>(next);
        }
    }

    private final Node<E> dummy = new Node<E>(null, null);
    private final AtomicReference<Node<E>> head = new AtomicReference<Node<E>>(dummy);
    private final AtomicReference<Node<E>> tail = new AtomicReference<Node<E>>(dummy);

    public boolean put(E item) {
        Node<E> newNode = new Node<E>(item, null);

        while (true) {
            Node<E> curTail = tail.get();
            Node<E> tailNext = curTail.next.get();

            if (curTail == tail.get()) {
                if (tailNext != null) { // A
                    // Queue in intermediate state, advance tail
                    tail.compareAndSet(curTail, tailNext); // B
                } else {
                    // In quiescent state, try inserting new node
                    if (curTail.next.compareAndSet(null, newNode)) { // C
                        // Insertion succeeded, try advancing tail
                        tail.compareAndSet(curTail, newNode); // D
                        return true;
                    }
                }
            }
        }
    }
}

/*
LinkedQueue in Listing 15.7 shows the insertion portion of the Michael-Scott nonblocking linked-queue algorithm
(Michael and Scott, 1996), which is used by ConcurrentLinkedQueue.
As in many queue algorithms, an empty queue consists of a "sentinel" or "dummy" node,
and the head and tail pointers are initialized to refer to the sentinel.
The tail pointer always refers to the sentinel (if the queue is empty), the last element in the queue,
or (in the case that an operation is in mid-update) the second-to-last element.

Inserting a new element involves updating two pointers.
The first links the new node to the end of the list by updating the next pointer of the current last element;
the second swings the tail pointer around to point to the new last element.
Between these two operations, the queue is in the intermediate state.
After the second update, the queue is again in the quiescent state.

The key observation that enables both of the required tricks is that if the queue is in the quiescent state,
the next field of the link node pointed to by tail is null, and if it is in the intermediate state,
tail.next is non-null. So any thread can immediately tell the state of the queue by examining tail.next.
Further, if the queue is in the intermediate state,
it can be restored to the quiescent state by advancing the tail pointer forward one node,
finishing the operation for whichever thread is in the middle of inserting an element.

LinkedQueue.put first checks to see if the queue is in the intermediate state before attempting to insert a new element
(step A). If it is, then some other thread is already in the process of inserting an element
(between its steps C and D). Rather than wait for that thread to finish,
the current thread helps it by finishing the operation for it, advancing the tail pointer (step B).
It then repeats this check in case another thread has started inserting a new element,
advancing the tail pointer until it finds the queue in the quiescent state so it can begin its own insertion.

The CAS at step C, which links the new node at the tail of the queue,
could fail if two threads try to insert an element at the same time. In that case, no harm is done:
no changes have been made, and the current thread can just reload the tail pointer and try again.
Once C succeeds, the insertion is considered to have taken effect;
the second CAS (step D) is considered "cleanup",
since it can be performed either by the inserting thread or by any other thread.
If D fails, the inserting thread returns anyway rather than retrying the CAS, because no retry is needed -
another thread has already finished the job in its step B!
This works because before any thread tries to link a new node into the queue,
it first checks to see if the queue needs cleaning up by checking if tail.next is non-null.
If it is, it advances the tail pointer first (perhaps multiple times) until the queue is in the quiescent state.
*/