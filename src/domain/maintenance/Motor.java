package domain.maintenance;

import domain.vehicle.Vehicle;

import java.time.LocalDate;
import java.util.Map;

public class Motor extends VehiclePart {

    private String motorType;
    private double horsePower;
    private double fuelConsumption;
    private double co2Emission;

    public Motor(String motorType, double horsePower, double fuelConsumption) {
        super("Motor");
        this.motorType = motorType;
        this.horsePower = horsePower;
        this.fuelConsumption = fuelConsumption;

        maintenanceDescriptions.add("Oil change");
        maintenanceDescriptions.add("Filter cleaning");
        maintenanceDescriptions.add("Engine check");
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

    private static double calculateEmission(double distanceKm, Vehicle vehicle) {
        for (VehiclePart part : vehicle.getParts()) {
            if (part instanceof Motor motor) {
                return distanceKm * motor.getCo2Emission(); // g/km * km = gram
            }
        }
        return 0;
    }

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("motorType", motorType);
        map.put("horsePower", horsePower);
        map.put("fuelConsumption", fuelConsumption);
        map.put("co2Emission", co2Emission);
        return map;
    }


}
