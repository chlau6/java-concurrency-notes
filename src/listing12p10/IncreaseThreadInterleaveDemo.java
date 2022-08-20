package listing12p10;

import junit.framework.TestCase;

import java.util.Random;

class IncreaseThreadInterleaveDemo extends TestCase {
    private static final int THRESHOLD = 100;
    private final Random random = new Random();

    public synchronized void transferCredits(Account from, Account to, int amount) {
        from.setBalance(from.getBalance() - amount);
        if (random.nextInt(1000) > THRESHOLD)
            Thread.yield();
        to.setBalance(to.getBalance() + amount);
    }

    class Account {
        private int amount;

        public void setBalance(int amount) {
            this.amount = amount;
        }

        public int getBalance() {
            return amount;
        }
    }
}



/*
Since many of the potential failures in concurrent code are low-probability events,
testing for concurrency errors is a numbers game, but there are some things you can do to improve your chances.
We've already mentioned how running on multiprocessor systems with fewer processors than active threads can
generate more interleavings than either a single-processor system or one with many processors.
Similarly, testing on a variety of systems with different processor counts, operating systems,
and processor architectures can disclose problems that might not occur on all systems.

A useful trick for increasing the number of interleavings,
and therefore more effectively exploring the state space of your programs,
is to use Thread.yield to encourage more context switches during operations that access shared state.
(The effectiveness of this technique is platform-specific, since the JVM is free to treat Thread.yield as a no-op;
using a short but nonzero sleep would be slower but more reliable.)
The method in Listing 12.10 transfers credits from one account to another; between the two update operations,
invariants such as "sum of all accounts equals zero" do not hold. By sometimes yielding in the middle of an operation,
you may activate timing-sensitive bugs in code that does not use adequate synchronization to access state.
The inconvenience of adding these calls for testing and removing them for production can be reduced by
adding them using aspect-oriented programming (AOP) tools.
*/