package listing16p5;

import annotation.ThreadSafe;
import dummy.Resource;

@ThreadSafe
public class EagerInitialization {
    private static Resource resource = new Resource();

    public static Resource getResource() {
        return resource;
    }
}

/*
Using eager initialization, shown in Listing 16.5,
eliminates the synchronization cost incurred on each call to getInstance in SafeLazyInitialization.
This technique can be combined with the JVM's lazy class loading to
create a lazy initialization technique that does not require synchronization on the common code path.
*/