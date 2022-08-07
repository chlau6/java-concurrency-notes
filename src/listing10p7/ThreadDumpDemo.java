package listing10p7;

import annotation.GuardedBy;
import annotation.ThreadSafe;
import dummy.Image;
import dummy.Point;

import java.util.HashSet;
import java.util.Set;

public class ThreadDumpDemo {
    /*
    Found one Java-level deadlock:
    =============================
    "ApplicationServerThread":
    waiting to lock monitor 0x080f0cdc (a MumbleDBConnection),
    which is held by "ApplicationServerThread"
    "ApplicationServerThread":
    waiting to lock monitor 0x080f0ed4 (a MumbleDBCallableStatement),
    which is held by "ApplicationServerThread"

    Java stack information for the threads listed above:
    "ApplicationServerThread":
    at MumbleDBConnection.remove_statement
    - waiting to lock <0x650f7f30> (a MumbleDBConnection)
    at MumbleDBStatement.close
    - locked <0x6024ffb0> (a MumbleDBCallableStatement)

    "ApplicationServerThread":
    at MumbleDBCallableStatement.sendBatch
    - waiting to lock <0x6024ffb0> (a MumbleDBCallableStatement)
    at MumbleDBConnection.commit
    - locked <0x650f7f30> (a MumbleDBConnection)
     */
}

/*
We’ve shown only the portion of the thread dump relevant to identifying the deadlock.
The JVM has done a lot of work for us in diagnosing the deadlock - which locks are causing the problem,
which threads are involved, which other locks they hold, and whether other threads are being indirectly inconvenienced.
One thread holds the lock on the MumbleDBConnection and is waiting to acquire the lock on the MumbleDBCallableStatement; 
the other holds the lock on the MumbleDBCallableStatement and is waiting for the lock on the MumbleDBConnection.
*/