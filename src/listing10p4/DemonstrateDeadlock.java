package listing10p4;

import dummy.Account;
import dummy.DollarAmount;
import dummy.InsufficientFundsException;

import java.util.Random;

public class DemonstrateDeadlock {
    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1000000;

    public static void main(String[] args) {
        final Random rnd = new Random();
        final Account[] accounts = new Account[NUM_ACCOUNTS];

        for (int i = 0; i < accounts.length; i++) {
            accounts[i] = new Account();
        }

        class TransferThread extends Thread {
            public void run() {
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    int fromAcct = rnd.nextInt(NUM_ACCOUNTS);
                    int toAcct = rnd.nextInt(NUM_ACCOUNTS);

                    DollarAmount amount = new DollarAmount(rnd.nextInt(1000));
                    transferMoney(accounts[fromAcct], accounts[toAcct], amount);
                }
            }

            private void transferMoney(Account fromAccount, Account toAccount, DollarAmount amount) {
                synchronized (fromAccount) {
                    synchronized (toAccount) {
                        fromAccount.debit(amount);
                        toAccount.credit(amount);
                    }
                }
            }
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            new TransferThread().start();
        }
    }
}

/*
You may think we’re overstating the risk of deadlock because locks are usually held only briefly,
but deadlocks are a serious problem in real systems.
A production application may perform billions of lock acquire-release cycles per day.
Only one of those needs to be timed just wrong to bring the application to deadlock,
and even a thorough load-testing regimen may not disclose all latent deadlocks.
DemonstrateDeadlock in Listing 10.4 deadlocks fairly quickly on most systems.
 */