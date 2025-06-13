package application.service;
import Infrastructure.ElectricVehicleFileRepository;
import domain.vehicle.ElectricVehicle;
import domain.vehicle.Vehicle;

import java.util.List;

public class ElectricVehicleSuggestionService {
    static ElectricVehicleFileRepository repo = new ElectricVehicleFileRepository();
    static List<ElectricVehicle> electricVehicles = repo.loadElectricVehicles();

    public static void electricVehicleSuggestion(Vehicle vehicle){
        if (vehicle.getFueltype().equalsIgnoreCase("electric")){
            System.out.println("Thanks for using electric vehicles and protect nature! ");
        }
        if (vehicle.getFueltype().equalsIgnoreCase("gasoline")){
            System.out.println("You may want to consider using an electric vehicle to protect nature and reduce your carbon footprint. Here is our electric vehicle recommendation for you!");
            System.out.println(findElectricVehicle(vehicle));
        }
        if (vehicle.getFueltype().equalsIgnoreCase("diesel")){
            System.out.println("You may want to consider using an electric vehicle to protect nature and reduce your carbon footprint. Here is our electric vehicle recommendation for you!");
            System.out.println(findElectricVehicle(vehicle));
        }
    }


    public static String findElectricVehicle(Vehicle vehicle){
        String userVehicleSegment= vehicle.getVehicleSegment();
        double userVehicleHP= vehicle.getHP();
        ElectricVehicle bestVehicle = null;
        double bestHPvalue=userVehicleHP*0.85;
        double HPdifference= Double.MAX_VALUE;
        for (int i=0; i<electricVehicles.size(); i++){
            if(electricVehicles.get(i).getSegment().equalsIgnoreCase(userVehicleSegment)){
                double tempHPdifference=Math.abs(electricVehicles.get(i).getHorsepower()-bestHPvalue);
                if (tempHPdifference<HPdifference){
                    bestVehicle = electricVehicles.get(i);
                    HPdifference=tempHPdifference;
                }
            }
        }
        return bestVehicle.getModel();
    }
}
