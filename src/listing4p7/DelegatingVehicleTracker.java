package listing4p7;

import annotation.ThreadSafe;
import listing4p6.Point;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ThreadSafe
public class DelegatingVehicleTracker {
    private final ConcurrentMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, Point> points) {
        locations = new ConcurrentHashMap<String, Point>(points);
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    public Map<String, Point> getLocations() {
        return unmodifiableMap;
    }

    public Point getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (locations.replace(id, new Point(x, y)) == null) {
            throw new IllegalArgumentException("invalid vehicle name: " + id);
        }
    }
}

/*
DelegatingVehicleTracker in Listing 4.7 does not use any explicit synchronization;
all access to state is managed by ConcurrentHashMap, and all the keys and values of the Map are immutable.

If we had used the original MutablePoint class instead of Point,
we would be breaking encapsulation by letting getLocations publish a reference to mutable state that is not thread-safe.

Notice that we’ve changed the behavior of the vehicle tracker class slightly;
while the monitor version returned a snapshot of the locations,
the delegating version returns an unmodifiable but “live” view of the vehicle locations.

This means that if thread A calls getLocations and thread B later modifies the location of some of the points,
those changes are reflected in the Map returned to thread A.

As we remarked earlier, this can be a benefit (more up-to-date data) or
a liability (potentially inconsistent view of the fleet), depending on your requirements.
 */
