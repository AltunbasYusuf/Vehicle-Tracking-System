package domain.maintenance;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Tire extends VehiclePart {
    private String seasonType; // "summer", "winter"
    private double treadDepth; // mm

    public Tire(String seasonType, double treadDepth) {
        super("Tire");
        this.seasonType = seasonType;
        this.treadDepth = treadDepth;

        maintenanceDescriptions.add("Tread depth check");
        maintenanceDescriptions.add("Tire rotation");
    }

    @Override
    public String getPartType() {
        return "tire";
    }

    public String getSeasonType() {
        return seasonType;
    }

    public double getTreadDepth() {
        return treadDepth;
    }

    @Override
    public int getDefaultMaintenanceInterval() {
        return 6;
    }

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("seasonType", seasonType);
        props.put("treadDepth", treadDepth);
        return props;
    }

}
