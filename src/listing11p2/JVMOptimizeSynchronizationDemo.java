package listing11p2;

public class JVMOptimizeSynchronizationDemo {
    void demo() {
        synchronized (new Object()) {
            // do something
        }
    }
}

/*
Modern JVMs can reduce the cost of incidental synchronization by
optimizing away locking that can be proven never to contend.
If a lock object is accessible only to the current thread,
the JVM is permitted to optimize away a lock acquisition because
there is no way another thread could synchronize on the same lock.
For example, the lock acquisition in Listing 11.2 can always be eliminated by the JVM.
*/