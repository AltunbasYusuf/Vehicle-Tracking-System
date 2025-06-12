package domain.vehicle;

import domain.maintenance.Motor;
import domain.maintenance.VehiclePart;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private String vehicleType;
    private List<VehiclePart> parts;
    private String brand;
    private String model;



    public Vehicle(String vehicleType, String brand, String model){
        this.vehicleType=vehicleType;
        this.parts = new ArrayList<>();
        this.brand=brand;
        this.model=model;
    }

    public void addPart(VehiclePart part) {
        this.parts.add(part);
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public List<VehiclePart> getParts() {
        return parts;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }


}