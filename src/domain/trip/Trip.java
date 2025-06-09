package domain.trip;

import domain.location.Location;

import java.time.LocalDateTime;

public class Trip {
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;
    private Location startLocation;
    private Location endLocation;

    public Trip(LocalDateTime start, LocalDateTime end, String description, Location startLocation, Location endLocation) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    public double getDistance() {
        if (startLocation == null || endLocation == null) return 0;
        return startLocation.distanceTo(endLocation);
    }
}
