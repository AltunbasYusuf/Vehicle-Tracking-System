package domain.maintenance;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CoolingSystem extends VehiclePart {
    private double antifreezeLevel; // Litre
    private double minLevel;

    public CoolingSystem(double antifreezeLevel, double minLevel) {
        super("Cooling System");
        this.antifreezeLevel = antifreezeLevel;
        this.minLevel = minLevel;

        maintenanceDescriptions.add("Antifreeze level check");
        maintenanceDescriptions.add("Radiator cleaning");
    }

    @Override
    public String getPartType() {
        return "Cooling System";
    }

    public boolean isAntifreezeLow() {
        return antifreezeLevel < minLevel;
    }

    public double getAntifreezeLevel() {
        return antifreezeLevel;
    }

    @Override
    public int getDefaultMaintenanceInterval() {
        return 10;
    }

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("antifreezeLevel", antifreezeLevel);
        props.put("minLevel", minLevel);
        return props;
    }

}
