package domain.vehicle;

public class EmissionCalculator {

    private double emissionFactor;
    private double fuelConsumption;
    private Vehicle vehicle;




    public static double emmisionCalculating(String fuelType){
        double emmisionFactor = 0;
        if(fuelType.equalsIgnoreCase("electric")){
            emmisionFactor=0.45;
        }
        if(fuelType.equalsIgnoreCase("gasoline")){
            emmisionFactor=2.31;
        }
        if(fuelType.equalsIgnoreCase("diesel")){
            emmisionFactor=2.68;
        }
        if(fuelType.equalsIgnoreCase("LPG")){
            emmisionFactor=1.51;
        }
        return emmisionFactor;
    }

    public static double calculatingCO2(double km, double emmisionFactor){
        double co2=km*emmisionFactor;
        return co2;
    }
}
