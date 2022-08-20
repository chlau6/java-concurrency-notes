package listing14p15;

import annotation.ThreadSafe;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class ReentrantLockAQSDemo {
    private final Sync sync = new Sync();
    private Thread owner;

    private class Sync extends AbstractQueuedSynchronizer {
        protected boolean tryAcquire(int ignored) {
            final Thread current = Thread.currentThread();
            int c = getState();

            if (c == 0) {
                if (compareAndSetState(0, 1)) {
                    owner = current;
                    return true;
                }
            } else if (current == owner) {
                setState(c + 1);
                return true;
            }

            return false;
        }
    }


}

/*
ReentrantLock supports only exclusive acquisition, so it implements tryAcquire, tryRelease, and isHeldExclusively;
tryAcquire for the nonfair version is shown in Listing 14.15.
ReentrantLock uses the synchronization state to hold the lock acquisition count,
and maintains an owner variable holding the identity of the owning thread that
is modified only when the current thread has just acquired the lock or is just about to release it.
In tryRelease,
it checks the owner field to ensure that the current thread owns the lock before allowing an unlock to proceed;
in tryAcquire,
it uses this field to differentiate between a reentrant acquisition and a contended acquisition attempt.
When a thread attempts to acquire a lock, tryAcquire first consults the lock state. If it is unheld,
it tries to update the lock state to indicate that it is held.
Because the state could have changed since it was first inspected a few instructions ago,
tryAcquire uses compareAndSetState to attempt to atomically update the state to indicate that
the lock is now held and confirm that the state has not changed since last observed.
If the lock state indicates that it is already held, if the current thread is the owner of the lock,
the acquisition count is incremented; if the current thread is not the owner of the lock, the acquisition attempt fails.

ReentrantLock also takes advantage of AQS's built-in support for multiple condition variables and wait sets.
Lock.newCondition returns a new instance of ConditionObject, an inner class of AQS.
*/