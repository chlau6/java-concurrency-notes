package listing13p3;

import dummy.Account;
import dummy.DollarAmount;
import dummy.InsufficientFundsException;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class TryLockDemo {
    Random rnd = new Random();

    public boolean transferMoney(Account fromAcct, Account toAcct, DollarAmount amount, long timeout, TimeUnit unit)
            throws InsufficientFundsException, InterruptedException {
        long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
        long randMod = getRandomDelayModulusNanos(timeout, unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);

        while (true) {
            if (fromAcct.lock.tryLock()) {
                try {
                    if (toAcct.lock.tryLock()) {
                        try {
                            if (fromAcct.getBalance().compareTo(amount) < 0) {
                                throw new InsufficientFundsException();
                            } else {
                                fromAcct.debit(amount);
                                toAcct.credit(amount);
                                return true;
                            }
                        } finally {
                            toAcct.lock.unlock();
                        }
                    }
                } finally {
                    fromAcct.lock.unlock();
                }
            }

            if (System.nanoTime() < stopTime) {
                return false;
            }

            NANOSECONDS.sleep(fixedDelay + rnd.nextLong() % randMod);
        }
    }

    /* Dummy Implementation */
    private long getRandomDelayModulusNanos(long timeout, TimeUnit unit) {
        return 0;
    }

    /* Dummy Implementation */
    private long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {
        return 0;
    }
}

/*
The timed and polled lock-acqusition modes provided by tryLock
allow more sophisticated error recovery than unconditional acquisition.
With intrinsic locks, a deadlock is fatal—the only way to recover is to restart the application,
and the only defense is to construct your program so that inconsistent lock ordering is impossible.
Timed and polled locking offer another option: probabalistic deadlock avoidance.

Using timed or polled lock acquisition (tryLock) lets you regain control if you cannot acquire all the required locks,
release the ones you did acquire, and try again (or at least log the failure and do something else).
Listing 13.3 shows an alternate way of addressing the dynamic ordering deadlock from Section 10.1.2:
use tryLock to attempt to acquire both locks, but back off and retry if they cannot both be acquired.
The sleep time has a fixed component and a random component to reduce the likelihood of livelock.
If the locks cannot be acquired within the specified time,
transferMoney returns a failure status so that the operation can fail gracefully.
*/