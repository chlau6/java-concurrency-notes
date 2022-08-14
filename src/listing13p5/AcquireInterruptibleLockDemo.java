package listing13p5;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class AcquireInterruptibleLockDemo {
    Lock lock = new ReentrantLock();

    public boolean sendOnSharedLine(String message) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            return cancellableSendOnSharedLine(message);
        } finally {
            lock.unlock();
        }
    }

    /* Dummy Implementation */
    private boolean cancellableSendOnSharedLine(String message) throws InterruptedException {
        return true;
    }
}

/*
Just as timed lock acquisition allows exclusive locking to be used within time-limited activities,
interruptible lock acquisition allows locking to be used within cancellable activities.
Section 7.1.6 identified several mechanisms, such as acquiring an intrinsic lock,
that are not responsive to interruption.
These noninterruptible blocking mechanisms complicate the implementation of cancellable tasks.
The lockInterruptibly method allows you to try to acquire a lock while remaining responsive to interruption,
and its inclusion in Lock avoids creating another category of non-interruptible blocking mechanisms.
*/