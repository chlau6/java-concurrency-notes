package listing16p7;

import annotation.NotThreadSafe;
import annotation.ThreadSafe;
import dummy.Resource;

@NotThreadSafe
public class DoubleCheckedLocking {
    private static Resource resource;
    public static Resource getInstance() {
        if (resource == null) {
            synchronized (DoubleCheckedLocking.class) {
                if (resource == null) {
                    resource = new Resource();
                }
            }
        }
        return resource;
    }
}

/*
DCL purported to offer the best of both worlds - lazy initialization without
paying the synchronization penalty on the common code path.
The way it worked was first to check whether initialization was needed without synchronizing,
and if the resource reference was not null, use it.
Otherwise, synchronize and check again if the Resource is initialized,
ensuring that only one thread actually initializes the shared Resource.
The common code path - fetching a reference to an already constructed Resource - doesn't use synchronization.
And that's where the problem is: as described in Section 16.2.1,
it is possible for a thread to see a partially constructed Resource.

The real problem with DCL is the assumption that the worst thing that can happen when
reading a shared object reference without synchronization is to erroneously see a stale value (in this case, null);
in that case the DCL idiom compensates for this risk by trying again with the lock held.
But the worst case is actually considerably worse -
it is possible to see a current value of the reference but stale values for the object's state,
meaning that the object could be seen to be in an invalid or incorrect state.
*/