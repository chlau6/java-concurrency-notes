package listing11p5;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@ThreadSafe
public class BetterAttributeStore {
    @GuardedBy("this")
    private final Map<String, String> attributes = new HashMap<String, String>();

    public synchronized boolean userLocationMatches(String name, String regexp) {
        String key = "users." + name + ".location";
        String location = attributes.get(key);
        if (location == null) {
            return false;
        } else {
            return Pattern.matches(regexp, location);
        }
    }
}

/*
BetterAttributeStore in Listing 11.5 rewrites AttributeStore to reduce significantly the lock duration.
The first step is to construct the Map key associated with the user's location,
a string of the form users.name.location.
This entails instantiating a StringBuilder object, appending several strings to it,
and instantiating the result as a String.
After the location has been retrieved, the regular expression is matched against the resulting location string.
Because constructing the key string and processing the regular expression do not access shared state,
they need not be executed with the lock held.
BetterAttributeStore factors these steps out of the synchronized block, thus reducing the time the lock is held.
*/