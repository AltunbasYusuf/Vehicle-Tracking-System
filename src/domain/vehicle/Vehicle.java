package domain.vehicle;

import domain.maintenance.Motor;
import domain.maintenance.VehiclePart;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    private List<VehiclePart> parts;
    private String brand;
    private String model;
    private String segment;
    private String fueltype;
    private double HP;
    private double fuelConsumption;



    public Vehicle(String brand, String model, String segment){
        this.parts = new ArrayList<>();
        this.brand=brand;
        this.model=model;
        this.segment=segment;
    }

    public void addPart(VehiclePart part) {
        this.parts.add(part);
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

    public String getVehicleSegment(){return this.segment;}

    public void setFuelType(String fueltype){
        this.fueltype=fueltype;
    }

    public String getFueltype(){return this.fueltype; }

    public void setHP(double HP){
        this.HP=HP;
    }

    public double getHP(){return this.HP; }

    public void setFuelConsumption(double fuelConsumption){
        this.fuelConsumption=fuelConsumption;
    }

    public double getFuelConsumption(){return this.fuelConsumption; }


}