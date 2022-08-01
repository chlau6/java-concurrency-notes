package listing3p4;

public class CountingSheep extends Thread {
    volatile boolean asleep;

    public void run() {
        while (!asleep) {
            countSomeSheep();
        }
    }

    /* Dummy Implementation */
    private void countSomeSheep() {

    }

    public static void main(String[] args) {
        CountingSheep countingSheep = new CountingSheep();
        countingSheep.start();
        countingSheep.asleep = true;
    }
}

/*
Listing 3.4 illustrates a typical use of volatile variables: checking a status flag to determine when to exit a loop.

In this example, our anthropomorphized thread is trying to get to sleep by the time-honored method of counting sheep.
For this example to work, the asleep flag must be volatile.
Otherwise, the thread might not notice when asleep has been set by another thread.

We could instead have used locking to ensure visibility of changes to asleep,
but that would have made the code more cumbersome.

Volatile variables are convenient, but they have limitations.
The most common use for volatile variables is as a completion, interruption, or status flag,
such as the asleep flag in Listing 3.4.

Volatile variables can be used for other kinds of state information, but more care is required when attempting this.
For example, the semantics of volatile are not strong enough to make the increment operation (count++) atomic,
unless you can guarantee that the variable is written only from a single thread.
 */
