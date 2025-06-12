package application.service;

import domain.maintenance.Motor;
import domain.maintenance.VehiclePart;
import domain.vehicle.Vehicle;

public class EmissionService {

    public double calculateEmission(double distanceKm, Vehicle vehicle) {
        for (VehiclePart part : vehicle.getParts()) {
            if (part instanceof Motor motor) {
                return distanceKm * motor.getCo2Emission();
            }
        }
        return 0;
    }
}
