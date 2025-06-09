package domain.maintenance;

import java.time.LocalDate;

public class Motor extends VehiclePart {

    private String motorType;
    private double horsePower;
    private double fuelConsumption;
    private double co2Emission;

    public Motor(String motorType, double horsePower, double fuelConsumption, double co2Emission) {
        super("Motor");
        this.motorType = motorType;
        this.horsePower = horsePower;
        this.fuelConsumption = fuelConsumption;
        this.co2Emission = co2Emission;
    }

    @Override
    public String getPartType() {
        return "motor";
    }

    public String getMotorType() {
        return motorType;
    }

    public double getHorsePower() {
        return horsePower;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public double getCo2Emission() {
        return co2Emission;
    }

    @Override
    public int getDefaultMaintenanceInterval() {
        return 12;
    }

}
