package application.service;

import Infrastructure.MySQLElectricVehicleCatalogRepository;
import Infrastructure.MySQLTripRepository;
import application.repository.ElectricVehicleCatalogRepositoryInterface;
import application.repository.TripRepositoryInterface;
import domain.trip.Trip;
import domain.vehicle.ElectricVehicle;
import domain.vehicle.Vehicle;

import java.io.IOException;
import java.util.List;

public class ElectricVehicleSuggestionService {

    // Artık 'allVehicles' listesine burada ihtiyacımız yok.
    static List<ElectricVehicle> electricVehicles;

    // static initializer bloğu: Sadece yeni Repository'den elektrikli araçları yükler.
    static {
        try {
            // Sadece elektrikli araç kataloğunu okuyan yeni repository'mizi kullanıyoruz
            ElectricVehicleCatalogRepositoryInterface catalogRepo = new MySQLElectricVehicleCatalogRepository();
            electricVehicles = catalogRepo.loadAllElectricVehicles();

        } catch (IOException e) {
            System.err.println("Elektrikli araç listesi veritabanından yüklenemedi!");
            e.printStackTrace();
            electricVehicles = new java.util.ArrayList<>();
        }
    }


    public static void electricVehicleSuggestion(String eMail, Vehicle vehicle) {
        TripRepositoryInterface repo2 = new MySQLTripRepository();
        TripService tripService = new TripService(repo2);
        List<Trip> trips = tripService.getAllTrips(eMail);
        double totalDistance = 0.0;
        for (Trip trip : trips) {
            totalDistance += trip.getDistance();
        }


        ElectricVehicle suggestedVehicle = findElectricVehicle(vehicle);


        if (suggestedVehicle == null) {
            System.out.println("\nSorry, we couldn't find a similar electric vehicle in our system.");
            System.out.println("The catalog may not have an electric vehicle with segment: " + vehicle.getVehicleSegment());
            return;
        }

        if (vehicle.getFueltype().equalsIgnoreCase("electric")) {
            System.out.println("Thanks for using electric vehicles and protecting nature!");
            System.out.println("A similar model in our catalog is: " + suggestedVehicle.getModel());
        } else {
            double costDifference = calculateCloseCostDifference(vehicle, totalDistance);
            double co2Difference = calculateCloseCO2Difference(vehicle, totalDistance);
            System.out.println("You may want to consider using an electric vehicle to reduce your carbon footprint, protect nature and pay less fuel cost. Here is our electric vehicle recommendation for you!");
            System.out.println(suggestedVehicle.getModel());
            System.out.printf("Considering your travel history, you would pay %.2f USD less if you chose this electric car \n", costDifference);
            System.out.printf("Considering your travel history, your car would release %.2f KG CO2 less if you chose this electric car. \n", co2Difference);
        }
    }

    public static ElectricVehicle findElectricVehicle(Vehicle vehicle) {
        String userVehicleSegment = vehicle.getVehicleSegment();
        double userVehicleHP = vehicle.getHP();
        ElectricVehicle bestVehicle = null;
        double bestHPvalue = userVehicleHP * 0.85;
        double HPdifference = Double.MAX_VALUE;

        for (ElectricVehicle ev : electricVehicles) {
            if (ev.getSegment().equalsIgnoreCase(userVehicleSegment)) {
                double tempHPdifference = Math.abs(ev.getHorsepower() - bestHPvalue);
                if (tempHPdifference < HPdifference) {
                    bestVehicle = ev;
                    HPdifference = tempHPdifference;
                }
            }
        }
        return bestVehicle;
    }

    public static ElectricVehicle copyCloseElectricVehicle(Vehicle vehicle) {
        return findElectricVehicle(vehicle);
    }

    public static double calculateCloseElectricCost(Vehicle vehicle, double distance) {
        ElectricVehicle closeElectricVehicle = copyCloseElectricVehicle(vehicle);
        if (closeElectricVehicle == null) {
            return 0.0;
        }
        return 0.20 * closeElectricVehicle.getFuelConsumption() * distance / 100;
    }

    public static double calculateCloseElectricCO2(Vehicle vehicle, double distance) {
        ElectricVehicle closeElectricVehicle = copyCloseElectricVehicle(vehicle);
        if (closeElectricVehicle == null) {
            return 0.0;
        }
        return 0.45 * closeElectricVehicle.getFuelConsumption() * distance / 100;
    }

    public static double calculateCloseCostDifference(Vehicle vehicle, double distance) {
        double electricVehicleCost = calculateCloseElectricCost(vehicle, distance);
        double vehicleCost = EmissionService.fuelCost(vehicle, distance);
        return vehicleCost - electricVehicleCost;
    }

    public static double calculateCloseCO2Difference(Vehicle vehicle, double distance) {
        double electricVehicleCO2 = calculateCloseElectricCO2(vehicle, distance);
        double vehicleCO2 = EmissionService.calculateEmission(distance, vehicle, vehicle.getFuelConsumption());
        return vehicleCO2 - electricVehicleCO2;
    }
}