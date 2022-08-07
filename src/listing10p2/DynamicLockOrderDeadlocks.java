package listing10p2;

import dummy.Account;
import dummy.DollarAmount;
import dummy.InsufficientFundsException;

public class DynamicLockOrderDeadlocks {
    // Warning: deadlock-prone!
    public void transferMoney(Account fromAccount, Account toAccount, DollarAmount amount)
            throws InsufficientFundsException {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }
    }
}

/*
Sometimes it is not obvious that you have sufficient control over lock ordering to prevent deadlocks.
Consider the harmless-looking code in Listing 10.2 that transfers funds from one account to another.
It acquires the locks on both Account objects before executing the transfer,
ensuring that the balances are updated atomically and
without violating invariants such as "an account cannot have a negative balance".

How can transferMoney deadlock? It may appear as if all the threads acquire their locks in the same order,
but in fact the lock order depends on the order of arguments passed to transferMoney,
and these in turn might depend on external inputs.
Deadlock can occur if two threads call transferMoney at the same time, one transferring from X to Y,
and the other doing the opposite:

A: transferMoney(myAccount, yourAccount, 10);
B: transferMoney(yourAccount, myAccount, 20);

With unlucky timing, A will acquire the lock on myAccount and wait for the lock on yourAccount,
while B is holding the lock on yourAccount and waiting for the lock on myAccount.

Deadlocks like this one can be spotted the same way as in Listing 10.1 - look for nested lock acquisitions.
Since the order of arguments is out of our control, to fix the problem we must induce an ordering on the locks and
acquire them according to the induced ordering consistently throughout the application.
 */