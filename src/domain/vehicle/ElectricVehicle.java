package domain.vehicle;

public class ElectricVehicle {
    private String model;
    private String segment;
    private int horsepower;

    public ElectricVehicle(String model, String segment, int horsepower) {
        this.model = model;
        this.segment = segment;
        this.horsepower = horsepower;
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

    @Override
    public String toString() {
        return model + ", " + segment + ", " + horsepower + " HP";
    }
}
