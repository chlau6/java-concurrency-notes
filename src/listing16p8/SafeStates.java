package listing16p8;

import annotation.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

@ThreadSafe
public class SafeStates {
    private final Map<String, String> states;

    public SafeStates() {
        states = new HashMap<String, String>();
        states.put("alaska", "AK");
        states.put("alabama", "AL");
        states.put("wyoming", "WY");
    }

    public String getAbbreviation(String s) {
        return states.get(s);
    }
}

/*
For objects with final fields,
initialization safety prohibits reordering any part of construction with the initial load of a reference to that object.
All writes to final fields made by the constructor, as well as to any variables reachable through those fields,
become "frozen" when the constructor completes, and any thread that obtains a reference to that object is guaranteed to
see a value that is at least as up to date as the frozen value.
Writes that initialize variables reachable through final fields are not reordered with
operations following the post-construction freeze.

Initialization safety means that SafeStates in Listing 16.8 could be safely published even through
unsafe lazy initialization or stashing a reference to a SafeStates in a public static field with no synchronization,
even though it uses no synchronization and relies on the non-thread-safe HashSet.

However, a number of small changes to SafeStates would take away its thread safety.
If states were not final, or if any method other than the constructor modified its contents,
initialization safety would not be strong enough to safely access SafeStates without synchronization.
If SafeStates had other nonfinal fields, other threads might still see incorrect values of those fields.
And allowing the object to escape during construction invalidates the initialization-safety guarantee.
*/