package listing16p1;

public class PossibleReordering {
    static int x = 0, y = 0;
    static int a = 0, b = 0;
    public static void main(String[] args) throws InterruptedException {
        Thread one = new Thread(new Runnable() {
            public void run() {
                a = 1;
                x = b;
            }
        });

        Thread other = new Thread(new Runnable() {
            public void run() {
                b = 1;
                y = a;
            }
        });

        one.start();
        other.start();

        one.join();
        other.join();
        System.out.println("( "+ x + "," + y + ")");
    }
}
/*
In describing race conditions and atomicity failures in Chapter 2,
we used interaction diagrams depicting "unlucky timing" where
the scheduler interleaved operations so as to cause incorrect results in insufficiently synchronized programs.
To make matters worse,
the JMM can permit actions to appear to execute in different orders from the perspective of different threads,
making reasoning about ordering in the absence of synchronization even more complicated.
The various reasons why operations might be delayed or
appear to execute out of order can all be grouped into the general category of reordering.

PossibleReordering in Listing 16.1 illustrates how difficult it is to reason about
the behavior of even the simplest concurrent programs unless they are correctly synchronized.
It is fairly easy to imagine how PossibleReordering could print (1, 0), or (0, 1), or (1, 1):
thread A could run to completion before B starts, B could run to completion before A starts,
or their actions could be interleaved. But, strangely, PossibleReordering can also print (0, 0)!
The actions in each thread have no dataflow dependence on each other, and accordingly can be executed out of order.
(Even if they are executed in order, the timing by which caches are flushed to main memory can make it appear,
from the perspective of B, that the assignments in A occurred in the opposite order.)
Figure 16.1 shows a possible interleaving with reordering that results in printing (0, 0).

PossibleReordering is a trivial program, and it is still surprisingly tricky to enumerate its possible results.
Reordering at the memory level can make programs behave unexpectedly.
It is prohibitively difficult to reason about ordering in the absence of synchronization;
it is much easier to ensure that your program uses synchronization appropriately.
Synchronization inhibits the compiler, runtime,
and hardware from reordering memory operations in ways that would violate the visibility guarantees provided by the JMM.
*/