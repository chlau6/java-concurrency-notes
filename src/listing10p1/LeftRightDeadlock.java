package listing10p1;

// Warning: deadlock-prone!
public class LeftRightDeadlock {
    private final Object left = new Object();
    private final Object right = new Object();
    public void leftRight() {
        synchronized (left) {
            synchronized (right) {
                doSomething();
            }
        }
    }

    public void rightLeft() {
        synchronized (right) {
            synchronized (left) {
                doSomethingElse();
            }
        }
    }

    private void doSomething() {
    }

    private void doSomethingElse() {
    }
}

/*
LeftRightDeadlock in Listing 10.1 is at risk for deadlock.
The leftRight and rightLeft methods each acquire the left and right locks.
If one thread calls leftRight and another calls rightLeft, and their actions are interleaved, they will deadlock.

The deadlock in LeftRightDeadlock came about because
the two threads attempted to acquire the same locks in a different order.
If they asked for the locks in the same order, there would be no cyclic locking dependency and therefore no deadlock.
If you can guarantee that every thread that needs locks L and M at the same time always
acquires L and M in the same order, there will be no deadlock.
 */