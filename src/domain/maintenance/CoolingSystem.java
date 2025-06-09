package domain.maintenance;

import java.time.LocalDate;

public class CoolingSystem extends VehiclePart {
    private double antifreezeLevel; // Litre
    private double minLevel;

    public CoolingSystem(double antifreezeLevel, double minLevel) {
        super("Cooling System");
        this.antifreezeLevel = antifreezeLevel;
        this.minLevel = minLevel;
    }

    @Override
    public String getPartType() {
        return "cooling_system";
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

}
