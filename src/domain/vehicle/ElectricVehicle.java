package domain.vehicle;

public class ElectricVehicle {
    private String model;
    private String segment;
    private int horsepower;
    private double fuelConsumption;

    public ElectricVehicle(String model, String segment, int horsepower, double fuelConsumption) {
        this.model = model;
        this.segment = segment;
        this.horsepower = horsepower;
        this.fuelConsumption = fuelConsumption;
    }

    public String getModel() {
        return model;
    }

    public String getSegment() {
        return segment;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public double getFuelConsumption() {return fuelConsumption;}

    @Override
    public String toString() {
        return model + ", " + segment + ", " + horsepower + " HP";
    }
}
