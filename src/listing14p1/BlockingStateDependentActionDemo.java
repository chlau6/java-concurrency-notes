package listing14p1;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BlockingStateDependentActionDemo<K,V> {
    /*
    void blockingAction() throws InterruptedException {
        acquire lock on object state
        while (precondition does not hold) {
            release lock
            wait until precondition might hold
            optionally fail if interrupted or timeout expires
            reacquire lock
        }
        perform action
    }
     */
}

/*
State-dependent operations that block until the operation can proceed are more convenient and
less error-prone than those that simply fail.
The built-in condition queue mechanism enables threads to block until an object has entered a state that allows progress
and to wake blocked threads when they may be able to make further progress.

We cover the details of condition queues in Section 14.2,
but to motivate the value of an efficient condition wait mechanism,
we first show how state dependence might be (painfully) tackled using polling and sleeping.

A blocking state-dependent action takes the form shown in Listing 14.1.
The pattern of locking is somewhat unusual in that the lock is released and reacquired in the middle of the operation.
The state variables that make up the precondition must be guarded by the object’s lock,
so that they can remain constant while the precondition is tested.
But if the precondition does not hold, the lock must be released so another thread can modify the object state -
otherwise the precondition will never become true.
The lock must then be reacquired before testing the precondition again.
*/