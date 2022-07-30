package listing4p12;

import annotation.ThreadSafe;
import listing4p11.SafePoint;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
public class PublishingVehicleTracker {
    private final Map<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiableMap;
    public PublishingVehicleTracker(Map<String, SafePoint> locations) {
        this.locations = new ConcurrentHashMap<String, SafePoint>(locations);
        this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
    }

    public Map<String, SafePoint> getLocations() {
        return unmodifiableMap;
    }

    public SafePoint getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (!locations.containsKey(id)) {
            throw new IllegalArgumentException("invalid vehicle name: " + id);
        }

        locations.get(id).set(x, y);
    }
}

/*
PublishingVehicleTracker derives its thread safety from delegation to an underlying ConcurrentHashMap,
but this time the contents of the Map are thread-safe mutable points rather than immutable ones.

The getLocation method returns an unmodifiable copy of the underlying Map.
Callers cannot add or remove vehicles,
but could change the location of one of the vehicles by mutating the SafePoint values in the returned Map.

Again, the “live” nature of the Map may be a benefit or a drawback, depending on the requirements.

PublishingVehicleTracker is thread-safe,
but would not be so if it imposed any additional constraints on the valid values for vehicle locations.

If it needed to be able to “veto” changes to vehicle locations or to take action when a location changes,
the approach taken by PublishingVehicleTracker would not be appropriate.
 */
