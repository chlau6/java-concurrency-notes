package listing3p11;

import annotation.Immutable;

import java.util.HashSet;
import java.util.Set;

@Immutable
public final class ThreeStooges {
    private final Set<String> stooges = new HashSet<String>();
    public ThreeStooges() {
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
    }
    public boolean isStooge(String name) {
        return stooges.contains(name);
    }
}

/*
Immutable objects can still use mutable objects internally to manage their state.

While the Set that stores the names is mutable,
the design of ThreeStooges makes it impossible to modify that Set after construction.

The stooges reference is final, so all object state is reached through a final field.
The last requirement, proper construction,
is easily met since the constructor does nothing that would cause the this reference to become accessible
to code other than the constructor and its caller.
 */
