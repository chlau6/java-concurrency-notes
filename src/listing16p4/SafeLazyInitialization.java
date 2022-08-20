package listing16p4;

import annotation.NotThreadSafe;
import annotation.ThreadSafe;
import dummy.Resource;

@ThreadSafe
public class SafeLazyInitialization {
    private static Resource resource;

    public synchronized static Resource getInstance() {
        if (resource == null) {
            resource = new Resource();
        }

        return resource;
    }
}

/*
It sometimes makes sense to defer initialization of objects that are expensive to initialize until
they are actually needed, but we have seen how the misuse of lazy initialization can lead to trouble.
UnsafeLazyInitialization can be fixed by making the getResource method synchronized, as shown in Listing 16.4.
Because the code path through getInstance is fairly short
(a test and a predicted branch), if getInstance is not called frequently by many threads,
there is little enough contention for the SafeLazyInitialization lock that this approach offers adequate performance.

The treatment of static fields with initializers (or fields whose value is initialized in a static initialization block)
is somewhat special and offers additional thread-safety guarantees.
Static initializers are run by the JVM at class initialization time,
after class loading but before the class is used by any thread.
Because the JVM acquires a lock during initialization and
this lock is acquired by each thread at least once to ensure that the class has been loaded,
memory writes made during static initialization are automatically visible to all threads.
Thus statically initialized objects require no explicit synchronization either
during construction or when being referenced.
However, this applies only to the as-constructed state - if the object is mutable,
synchronization is still required by both readers and writers to make subsequent modifications visible and
to avoid data corruption.
*/