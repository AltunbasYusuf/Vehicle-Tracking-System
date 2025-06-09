package domain.maintenance;

public class Brake extends VehiclePart {
    private double padWearLevel; // % cinsinden
    private boolean absEnabled;

    public Brake(double padWearLevel, boolean absEnabled) {
        super("Brake System");
        this.padWearLevel = padWearLevel;
        this.absEnabled = absEnabled;
    }

    @Override
    public String getPartType() {
        return "brake";
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
}
