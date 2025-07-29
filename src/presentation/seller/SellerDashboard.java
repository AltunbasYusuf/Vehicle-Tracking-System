package presentation.seller;

import Infrastructure.MySQLVehicleRepository;
import application.service.VehicleService;
import domain.user.User;
import domain.vehicle.Vehicle;
import presentation.setup.Quick_Setup;

import java.util.List;
import java.util.Scanner;

public class SellerDashboard {
    private static final Scanner input = new Scanner(System.in);
    private static final VehicleService vehicleService = new VehicleService(new MySQLVehicleRepository());

    public static void open(User user) {
        System.out.println("👋 Welcome Seller: " + user.getName());

        while (true) {
            System.out.println("\n Seller Dashboard");
            System.out.println("1. Add new vehicle");
            System.out.println("2. List my vehicles");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1" -> addNewVehicle(user);
                case "2" -> listVehicles(user);
                case "0" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("❗ Invalid choice.");
            }
        }
    }

    private static void addNewVehicle(User user) {
        Vehicle vehicle = Quick_Setup.quickSetup();
        if (vehicle != null) {
            vehicleService.addVehicle(vehicle, user.getMail());
        }
    }

    private static void listVehicles(User user) {
        List<Vehicle> vehicles = vehicleService.getAllVehiclesBySeller(user.getMail());

        if (vehicles.isEmpty()) {
            System.out.println("Sisteme kayıtlı hiçbir aracınız bulunmamaktadır.");
            return;
        }

        System.out.println("\nRegistered Vehicles:");
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);
            System.out.println((i + 1) + " - " + v.getBrand() + " " + v.getModel() + " " + v.getVehicleSegment());
        }
    }
}