package application.service;
import Infrastructure.MySQLTripRepository;
import Infrastructure.MySQLVehicleRepository;
import application.repository.SellerVehicleRepositoryInterface;
import application.repository.TripRepositoryInterface;
import domain.trip.Trip;
import domain.vehicle.ElectricVehicle;
import domain.vehicle.Vehicle;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ElectricVehicleSuggestionService {
    // Bu listeleri önce boş olarak tanımlıyoruz.
    static List<Vehicle> allVehicles;
    static List<ElectricVehicle> electricVehicles;

    // static initializer bloğu: Bu sınıf ilk kullanıldığında sadece bir kere çalışır.
    static {
        // "Riskli" kodu try bloğunun içine alıyoruz.
        try {
            SellerVehicleRepositoryInterface vehicleRepo = new MySQLVehicleRepository();
            allVehicles = vehicleRepo.loadAllVehicles();

            electricVehicles = allVehicles.stream()
                    .filter(v -> v.getFueltype().equalsIgnoreCase("electric"))
                    .map(v -> new ElectricVehicle(v.getModel(), v.getVehicleSegment(), (int)v.getHP(), v.getFuelConsumption()))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            // Eğer veritabanından veri okunurken bir hata olursa, programın çökmesini engelliyoruz.
            // Hatayı konsola yazdırıp, listeleri boş olarak başlatıyoruz ki program çalışmaya devam edebilsin.
            System.err.println("Elektrikli araç listesi veritabanından yüklenemedi!");
            e.printStackTrace();
            allVehicles = new java.util.ArrayList<>();
            electricVehicles = new java.util.ArrayList<>();
        }
    }


    public static void electricVehicleSuggestion(String eMail,Vehicle vehicle){
        TripRepositoryInterface repo2 = new MySQLTripRepository();
        TripService tripService = new TripService(repo2);
        List<Trip> trips = tripService.getAllTrips(eMail);
        double totalDistance = 0.0;
        for (Trip trip : trips)
        {
            totalDistance += trip.getDistance();
        }
        if (vehicle.getFueltype().equalsIgnoreCase("electric")){
            System.out.println("Thanks for using electric vehicles and protect nature! ");
            System.out.println(findElectricVehicle(vehicle));
        }
        else{
            double costDifference = calculateCloseCostDifference(vehicle,totalDistance);
            double co2Difference = calculateCloseCO2Difference(vehicle, totalDistance);
            System.out.println("You may want to consider using an electric vehicle to reduce your carbon footprint, protect nature and pay less fuel cost. Here is our electric vehicle recommendation for you!");
            System.out.println(findElectricVehicle(vehicle));
            System.out.printf("Considering your travel history, you would pay %.2f USD if you chose this electric car \n", costDifference);
            System.out.printf("Considering your travel history, your car would release %.2f KG CO2 less if you chose this electric car. \n", co2Difference);

        }
    }


    public static String findElectricVehicle(Vehicle vehicle){
        String userVehicleSegment= vehicle.getVehicleSegment();
        double userVehicleHP= vehicle.getHP();
        ElectricVehicle bestVehicle = null;
        double bestHPvalue;
        if (!vehicle.getFueltype().equalsIgnoreCase("electric")) {
             bestHPvalue = userVehicleHP * 0.85;
        }
        else {
            bestHPvalue = userVehicleHP;
        }
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

    public static ElectricVehicle copyCloseElectricVehicle(Vehicle vehicle){
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
        return bestVehicle;
    }


    public static double calculateCloseElectricCost(Vehicle vehicle, double distance){
        ElectricVehicle closeElectricVehicle =  copyCloseElectricVehicle(vehicle);
        return 0.20 * closeElectricVehicle.getFuelConsumption() * distance/100;
    }

    public static double calculateCloseElectricCO2(Vehicle vehicle, double distance){
        ElectricVehicle closeElectricVehicle =  copyCloseElectricVehicle(vehicle);
        return  0.45 * closeElectricVehicle.getFuelConsumption() * distance/100;
    }

    public static double calculateCloseCostDifference(Vehicle vehicle,double distance){
        double electricVehicleCost= calculateCloseElectricCost(vehicle,distance);
        double vehicleCost = EmissionService.fuelCost(vehicle,distance);
        return vehicleCost-electricVehicleCost;
    }

    public static double calculateCloseCO2Difference(Vehicle vehicle,double distance){
        double electricVehicleCO2= calculateCloseElectricCO2(vehicle,distance);
        double vehicleCO2 = EmissionService.calculateEmission(distance, vehicle, vehicle.getFuelConsumption());
        return vehicleCO2-electricVehicleCO2 ;
    }




}
