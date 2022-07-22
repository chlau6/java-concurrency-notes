package listing3p4;

public class CountingSheep extends Thread {
    volatile boolean asleep;

    public void run() {
        while (!asleep) {
            countSomeSheep();
        }
    }

    /*
    Dummy Implementation
     */
    private void countSomeSheep() {

    }

    public static void main(String[] args) {
        CountingSheep countingSheep = new CountingSheep();
        countingSheep.start();
        countingSheep.asleep = true;
    }
}

/*
The above example illustrates a typical use of volatile variables: checking a status flag to determine when to exit a loop.

In this example, our anthropomorphized thread is trying to get to sleep by the time-honored method of counting sheep.
For this example to work, the asleep flag must be volatile.
Otherwise, the thread might not notice when asleep has been set by another thread.

We could instead have used locking to ensure visibility of changes to asleep,
but that would have made the code more cumbersome.
 */
