public class Motor {
    private String motorType;
    private double horsePower;
    private double fuelConsumption;
    private double co2Emission;

    public Motor(String motorType, double horsePower, double fuelConsumption, double co2Emission) {
        this.motorType=motorType;
        this.fuelConsumption=fuelConsumption;
        this.co2Emission=co2Emission;
        this.horsePower=horsePower;
    }
}
