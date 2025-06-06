public class Vehicle {
    private String vehicleType;
    private Motor motor;
    private String model;
    private String tires;
    private String fuelType;

    public Vehicle(String vehicleType, Motor motor, String model, String tires, String fuelType){
        this.vehicleType=vehicleType;
        this.motor=motor;
        this.model=model;
        this.tires=tires;
        this.fuelType=fuelType;
    }


    public String getVehicleType() {
        return vehicleType;
    }

    public Motor getMotor() {
        return motor;
    }

    public String getModel() {
        return model;
    }

    public String getTires() {
        return tires;
    }

    public String getFuelType() {
        return fuelType;
    }
}
