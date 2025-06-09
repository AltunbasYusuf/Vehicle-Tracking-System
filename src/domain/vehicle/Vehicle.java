package domain.vehicle;

import domain.maintenance.Motor;
import domain.maintenance.VehiclePart;

import java.util.List;

public class Vehicle {
    private String vehicleType;
    private List<VehiclePart> parts;

    public Vehicle(String vehicleType, List<VehiclePart> parts){
        this.vehicleType=vehicleType;
        this.parts = parts;
    }


    public String getVehicleType() {
        return vehicleType;
    }

    public List<VehiclePart> getParts() {
        return parts;
    }
}