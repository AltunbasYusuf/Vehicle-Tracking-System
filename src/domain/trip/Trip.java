package domain.trip;

import application.repository.TripRepositoryInterface;
import domain.location.Location;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Trip {
    private LocalDateTime start;
    private LocalDateTime end;
    private String description;
    private Location startLocation;
    private Location endLocation;
    private Double manualDistance=null; // trip kullanıcı tarafından kilometre yazılarak eklenmişse diye var.
    private static final TripRepositoryInterface repo = new Infrastructure.MySQLTripRepository();
    private double CO2emission;


    private static final Map<String, Location> predefinedLocations = new HashMap<>();

    //bu lokasyonlar sisteme önceden girilmiş pre lokasyonlar.
    static {
        predefinedLocations.put("Home", new Location(38.42, 27.14));
        predefinedLocations.put("Office", new Location(38.45, 27.20));
        predefinedLocations.put("Airport", new Location(38.51, 27.00));
        predefinedLocations.put("Gym", new Location(38.44, 27.18));
    }

    public static Map<String, Location> getPredefinedLocations() {
        return predefinedLocations;
    }

    public static Location getLocationByName(String name) {
        return predefinedLocations.get(name);
    }

    //predefined lokasyonlar ile yapılan trip.
    public Trip(LocalDateTime start, LocalDateTime end, String description, Location startLocation, Location endLocation) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    //km manuel eklemeli trip
    public Trip(LocalDateTime start, LocalDateTime end, String description, double manualDistance) {
        this.start = start;
        this.end = end;
        this.description = description;
        this.manualDistance = manualDistance;
        this.startLocation = null;
        this.endLocation = null;
    }


    public double getDistance() {
        if(manualDistance!=null) return manualDistance; // eğer km eklenerek girilen bir tripse distance'ı doğru döndürsün.
        if (startLocation == null || endLocation == null) return 0;
        return startLocation.distanceTo(endLocation);
    }
    public void setCo2(double co2){this.CO2emission=co2;}

    public double getCo2(){return this.CO2emission;}

    public String getDescription() {
        return description;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public Double getManualDistance() {
        return manualDistance;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public Location getStartLocation() {
        return startLocation;
    }

}
