import java.util.Scanner;
public class Quick_Setup {
    private static final Scanner input = new Scanner(System.in);



    //Araç ve motor özelliklerini kullanıcıdan alacak Quick Setup arayüzü
    public static void quickSetup(){
        System.out.println("Welcome to Quick Setup! Please choose the vehicle:\n" + "1.Car\n" + "2.Motorcycle\n" + "3.Truck");
        String vehicleType=VehicleType();
        System.out.println("Please give the vehicle's motor type (diesel,electric or hybrid):");
        String motorType=motorType();
        System.out.println("Please give the horsepower of vehicle's motor:");
        double horsePower=horsePower();
        System.out.println("Please give the fuel consumption of vehicle's motor (L/100km or kWh/100km) :");
        double fuelConsumption=fuelConsumption();
        System.out.println("Please give the CO₂ Emission (g/km) of vehicle's motor:");
        double co2Emission=co2Emission();
        Motor vehicleMotor=new Motor(motorType,horsePower,fuelConsumption,co2Emission);
        Vehicle vehicle= new Vehicle(vehicleType,vehicleMotor);
    }







    public static double co2Emission(){
        double co2Emission= Double.parseDouble(input.nextLine());
        return co2Emission;
    }



    public static double fuelConsumption(){
        double fuelConsumption= Double.parseDouble(input.nextLine());
        return fuelConsumption;
    }


    public static double horsePower(){
        double horsePower= Double.parseDouble(input.nextLine());
        return horsePower;
    }


    public static String motorType() {
        String motorType=input.nextLine();
        if (motorType.equalsIgnoreCase("diesel"))
            motorType="diesel";
        if (motorType.equalsIgnoreCase("electric"))
            motorType="electric";
        if (motorType.equalsIgnoreCase("hybrid"))
            motorType="hybrid";
        return motorType;
    }


    public static String VehicleType(){
        String vehicleType= input.nextLine();
        if(vehicleType.equalsIgnoreCase("car")||vehicleType.equalsIgnoreCase("1")||vehicleType.equalsIgnoreCase("1.")){
            vehicleType="Car";
        }
        if(vehicleType.equalsIgnoreCase("motorcycle")||vehicleType.equalsIgnoreCase("2")||vehicleType.equalsIgnoreCase("2.")||vehicleType.equalsIgnoreCase("motor")){
            vehicleType="Motorcycle";
        }
        if(vehicleType.equalsIgnoreCase("truck")||vehicleType.equalsIgnoreCase("3")||vehicleType.equalsIgnoreCase("3.")){
            vehicleType="Truck";
        }
    return vehicleType;
    }
}
