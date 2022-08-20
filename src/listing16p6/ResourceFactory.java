package listing16p6;

import annotation.ThreadSafe;
import dummy.Resource;

@ThreadSafe
public class ResourceFactory {
    private static class ResourceHolder {
        public static Resource resource = new Resource();
    }

    public static Resource getResource() {
        return ResourceHolder.resource;
    }
}

/*
The lazy initialization holder class idiom in Listing 16.6
uses a class whose only purpose is to initialize the Resource.
The JVM defers initializing the ResourceHolder class until it is actually used,
and because the Resource is initialized with a static initializer,
no additional synchronization is needed.
The first call to getResource by any thread causes ResourceHolder to be loaded and initialized,
at which time the initialization of the Resource happens through the static initializer.
*/