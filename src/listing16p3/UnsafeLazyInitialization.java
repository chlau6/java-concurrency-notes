package listing16p3;

import annotation.NotThreadSafe;
import dummy.Resource;

@NotThreadSafe
public class UnsafeLazyInitialization {
    private static Resource resource;

    public static Resource getInstance() {
        if (resource == null) {
            resource = new Resource(); // unsafe publication
        }

        return resource;
    }
}

/*
Unsafe publication can happen as a result of an incorrect lazy initialization, as shown in Figure 16.3.
At first glance, the only problem here seems to be the race condition described in Section 2.2.2.
Under certain circumstances, such as when all instances of the Resource are identical,
you might be willing to overlook these (along with the inefficiency of possibly creating the Resource more than once).
Unfortunately, even if these defects are overlooked, UnsafeLazyInitialization is still not safe,
because another thread could observe a reference to a partially constructed Resource.

Suppose thread A is the first to invoke getInstance. It sees that resource is null, instantiates a new Resource,
and sets resource to reference it. When thread B later calls getInstance,
it might see that resource already has a non-null value and just use the already constructed Resource.
This might look harmless at first,
but there is no happens-before ordering between the writing of resource in A and the reading of resource in B.
A data race has been used to publish the object,
and therefore B is not guaranteed to see the correct state of the Resource.

The Resource constructor changes the fields of the freshly allocated Resource from their default values
(written by the Object constructor) to their initial values.
Since neither thread used synchronization, B could possibly see A's actions in a different order than A performed them.
So even though A initialized the Resource before setting resource to reference it,
B could see the write to resource as occurring before the writes to the fields of the Resource.
B could thus see a partially constructed Resource that may well be in an invalid state -
and whose state may unexpectedly change later.
*/