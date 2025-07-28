package domain.maintenance;

import java.util.HashMap;
import java.util.Map;

public class Brake extends VehiclePart {
    private double padWearLevel; // % cinsinden
    private boolean absEnabled;

    public Brake(double padWearLevel, boolean absEnabled) {
        super("Brake System");
        this.padWearLevel = padWearLevel;
        this.absEnabled = absEnabled;

        maintenanceDescriptions.add("Brake pad replacement");
        maintenanceDescriptions.add("ABS inspection");
    }

    @Override
    public String getPartType() {
        return "Brake System";
    }

    public double getPadWearLevel() {
        return padWearLevel;
    }

    public boolean isAbsEnabled() {
        return absEnabled;
    }

    @Override
    public int getDefaultMaintenanceInterval() {
        return 8;
    }


    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("padWearLevel", padWearLevel);
        props.put("absEnabled", absEnabled);
        return props;
    }
}
