package listing13p4;

import dummy.Account;
import dummy.DollarAmount;
import dummy.InsufficientFundsException;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class TimedBudgetLockDemo {
    Lock lock = new ReentrantLock();
    public boolean trySendOnSharedLine(String message, long timeout, TimeUnit unit) throws InterruptedException {
        long nanosToLock = unit.toNanos(timeout) - estimatedNanosToSend(message);
        if (!lock.tryLock(nanosToLock, NANOSECONDS))
            return false;
        try {
            return sendOnSharedLine(message);
        } finally {
            lock.unlock();
        }
    }

    /* Dummy Implementation */
    private boolean sendOnSharedLine(String message) {
        return true;
    }

    /* Dummy Implementation */
    private long estimatedNanosToSend(String message) {
        return 0;
    }
}

/*
Timed locks are also useful in implementing activities that manage a time budget (see Section 6.3.7).
When an activity with a time budget calls a blocking method,
it can supply a timeout corresponding to the remaining time in the budget.
This lets activities terminate early if they cannot deliver a result within the desired time.
With intrinsic locks, there is no way to cancel a lock acquisition once it is started,
so intrinsic locks put the ability to implement time-budgeted activities at risk.

The travel portal example in Listing 6.17 creates a separate task for each car-rental company
from which it was soliciting bids.
Soliciting a bid probably involves some sort of network-based request mechanism, such as a web service request.
But soliciting a bid might also require exclusive access to a scarce resource,
such as a direct communications line to the company.

We saw one way to ensure serialized access to a resource in Section 9.5: a single-threaded executor.
Another approach is to use an exclusive lock to guard access to the resource.
The code in Listing 13.4 tries to send a message on a shared communications line guarded by a Lock,
but fails gracefully if it cannot do so within its time budget.
The timed tryLock makes it practical to incorporate exclusive locking into such a time-limited activity.
*/