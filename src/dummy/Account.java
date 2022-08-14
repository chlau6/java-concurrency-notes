package dummy;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    public Lock lock = new ReentrantLock();

    public DollarAmount getBalance() {
        return new DollarAmount(0);
    }

    public void debit(DollarAmount amount) {
    }

    public void credit(DollarAmount amount) {
    }
}
