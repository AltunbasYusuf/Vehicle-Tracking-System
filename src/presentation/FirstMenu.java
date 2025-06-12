package presentation;

import Infrastructure.TxtUserVehicleRepository;
import Infrastructure.TxtVehicleRepository;
import application.AuthInterface;
import application.AuthManager;
import application.repository.SellerVehicleRepositoryInterface;
import application.repository.UserRepositoryInterface;
import application.repository.UserVehicleRepositoryInterface;
import application.security.PasswordEncoderInterface;
import domain.user.User;
import Infrastructure.SimplePasswordEncoder;
import Infrastructure.TxtUserRepository;
import domain.vehicle.Vehicle;
import presentation.owner.OwnerDashboard;
import presentation.seller.SellerDashboard;

import java.io.IOException;
import java.util.Scanner;

public class FirstMenu {

    private static final Scanner input = new Scanner(System.in);

    public static void start() throws IOException {
        UserRepositoryInterface userRepo = new TxtUserRepository("users.txt");
        PasswordEncoderInterface encoder = new SimplePasswordEncoder();
        AuthInterface auth = new AuthManager(userRepo, encoder);

        while (true) {
            System.out.println("\n==============================");
            System.out.println("🚗 Vehicle Tracking System");
            System.out.println("==============================");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1" -> handleLogin(auth);
                case "2" -> handleSignup(auth);
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("❗ Invalid choice.");
            }
        }
    }

    private static void handleLogin(AuthInterface auth) {
        System.out.print("Email: ");
        String mail = input.nextLine();
        System.out.print("Password: ");
        String password = input.nextLine();

        User user = auth.login(mail, password);
        if (user == null) {
            System.out.println("❗ Login failed.");
            return;
        }

        System.out.println(" Welcome, " + user.getName());

        // Araç eşleşmesini yükle
        if (user.getRole() == User.UserRole.VEHICLE_OWNER) {
            // user.setVehicle(...) çağrısı burada yapılmalı,!!!!

            try {
                SellerVehicleRepositoryInterface vehicleRepo = new TxtVehicleRepository("vehicle_system.txt");
                UserVehicleRepositoryInterface userVehicleRepo = new TxtUserVehicleRepository("user_vehicle.txt", vehicleRepo);

                Vehicle vehicle = userVehicleRepo.getVehicleForUser(user.getMail());

                if (vehicle == null) {
                    System.out.println("❗ No vehicle assigned to this user.");
                    return;
                }

                user.setVehicle(vehicle);
            } catch (IOException e) {
                System.out.println("❗ Error loading vehicle for user: " + e.getMessage());
                return;
            }
            try {
                OwnerDashboard.open(user); // ownerin yapabildiği fonksiyonların olduğu menüye gider.
            } catch (IOException e) {
                System.out.println("❗ Error opening owner dashboard.");
            }
        } else {
            SellerDashboard.open(user); //sellerin yapbildiği fonksiyonların olduğu menüye gider.
        }
    }

    private static void handleSignup(AuthInterface auth) throws IOException {
        System.out.print("Email: ");
        String mail = input.nextLine();
        System.out.print("Password: ");
        String password = input.nextLine();
        System.out.print("Name: ");
        String name = input.nextLine();
        System.out.print("Surname: ");
        String surname = input.nextLine();
        System.out.print("Role (VEHICLE_OWNER / VEHICLE_SELLER): ");
        String role = input.nextLine();

        auth.signUp(mail, password, name, surname, role);


    }
}
