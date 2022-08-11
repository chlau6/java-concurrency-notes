package listing11p4;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

@ThreadSafe
public class AttributeStore {
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
AttributeStore in Listing 11.4 shows an example of holding a lock longer than necessary.
The userLocationMatches method looks up the user's location in a Map and
uses regular expression matching to see if the resulting value matches the supplied pattern.
The entire userLocationMatches method is synchronized,
but the only portion of the code that actually needs the lock is the call to Map.get.
*/