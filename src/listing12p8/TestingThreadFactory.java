package listing12p8;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TestingThreadFactory implements ThreadFactory {
    public final AtomicInteger numCreated = new AtomicInteger();
    private final ThreadFactory factory = Executors.defaultThreadFactory();

    public Thread newThread(Runnable r) {
        numCreated.incrementAndGet();
        return factory.newThread(r);
    }
}

/*
Testing a thread pool involves testing a number of elements of execution policy:
that additional threads are created when they are supposed to, but not when they are not supposed to;
that idle threads get reaped when they are supposed to, etc.
Constructing a comprehensive test suite that covers all the possibilities is a major effort,
but many of them can be tested fairly simply individually.

We can instrument thread creation by using a custom thread factory.
TestingThreadFactory in Listing 12.8 maintains a count of created threads;
test cases can then verify the number of threads created during a test run.
TestingThreadFactory could be extended to return a custom Thread that also records when the thread terminates,
so that test cases can verify that threads are reaped in accordance with the execution policy.
*/