package listing14p10;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface Condition {
    void await() throws InterruptedException;

    boolean await(long time, TimeUnit unit) throws InterruptedException;

    long awaitNanos(long nanosTimeout) throws InterruptedException;

    void awaitUninterruptibly();

    boolean awaitUntil(Date deadline) throws InterruptedException;

    void signal();

    void signalAll();
}

/*
As we saw in Chapter 13, explicit Locks can be useful in some situations where intrinsic locks are too inflexible.
Just as Lock is a generalization of intrinsic locks,
Condition (see Listing 14.10) is a generalization of intrinsic condition queues.

Intrinsic condition queues have several drawbacks. Each intrinsic lock can have only one associated condition queue,
which means that in classes like BoundedBuffer
multiple threads might wait on the same condition queue for different condition predicates,
and the most common pattern for locking involves exposing the condition queue object.
Both of these factors make it impossible to enforce the uniform waiter requirement for using notifyAll.
If you want to write a concurrent object with multiple condition predicates,
or you want to exercise more control over the visibility of the condition queue,
the explicit Lock and Condition classes offer a more flexible alternative to intrinsic locks and condition queues.

A Condition is associated with a single Lock, just as a condition queue is associated with a single intrinsic lock;
to create a Condition, call Lock.newCondition on the associated lock.
And just as Lock offers a richer feature set than intrinsic locking,
Condition offers a richer feature set than intrinsic condition queues:  multiple wait sets per lock,
interruptible and uninterruptible condition waits, deadline-based waiting, and a choice of fair or nonfair queueing.
*/