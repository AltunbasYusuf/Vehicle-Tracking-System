package application.service;

import domain.maintenance.Motor;
import domain.maintenance.VehiclePart;
import domain.vehicle.Vehicle;

public class EmissionService {

    public double calculateEmission(double distanceKm, Vehicle vehicle, double fuelConsumptionPer100Km) {
        double emmisionFactor = 0;
        if(vehicle.getFueltype().equalsIgnoreCase("electric")){
            emmisionFactor=0.45;
        }
        if(vehicle.getFueltype().equalsIgnoreCase("gasoline")){
            emmisionFactor=2.31;
        }
        if(vehicle.getFueltype().equalsIgnoreCase("diesel")){
            emmisionFactor=2.68;
        }
        if(vehicle.getFueltype().equalsIgnoreCase("LPG")){
            emmisionFactor=1.51;
        }
        return distanceKm*emmisionFactor*fuelConsumptionPer100Km/100;
    }
}
