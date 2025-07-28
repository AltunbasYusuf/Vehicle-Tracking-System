package application;

import application.repository.SellerVehicleRepositoryInterface;
import application.repository.UserRepositoryInterface;
import application.repository.UserVehicleRepositoryInterface;
import application.security.PasswordEncoderInterface;
import domain.user.User;
import domain.user.VehicleOwner;
import domain.user.VehicleSeller;
import domain.vehicle.Vehicle;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static domain.user.User.UserRole.VEHICLE_OWNER;

public class AuthManager implements AuthInterface{

    private final UserRepositoryInterface userRepository;
    private final PasswordEncoderInterface passwordEncoder;

    public AuthManager(UserRepositoryInterface userRepository, PasswordEncoderInterface passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User login(String mail, String rawPassword) {
        User user = userRepository.findByMail(mail);
        if (user == null) return null;

        if (user.verifyPassword(rawPassword, passwordEncoder)) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public boolean signUp(String mail, String rawPassword, String name, String surname, String role) throws IOException {
        if (userRepository.doesExist(mail)) {
            System.out.println("This email is already registered.");
            return false;
        }
        if (rawPassword.length() < 6) {
            System.out.println("Password must be at least 6 characters.");
            return false;
        }

        String hashed = passwordEncoder.hash(rawPassword);

        User.UserRole userRole;
        try {
            userRole = User.UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role.");
            return false;
        }

        User user;
        switch (userRole) {
            case VEHICLE_OWNER:
                user = new VehicleOwner(mail, hashed, name, surname);
                break;
            case VEHICLE_SELLER:
                user = new VehicleSeller(mail, hashed, name, surname);
                break;
            default:
                System.out.println("Unknown role.");
                return false;
        }

        userRepository.save(user);
        System.out.println("Sign up successful.");

        if (userRole == VEHICLE_OWNER) {
            // Seller repo'ya artık burada doğrudan ihtiyacımız yok, çünkü MySQLUserVehicleRepository onu kendi içinde çağırıyor.
            UserVehicleRepositoryInterface userVehicleRepo = new Infrastructure.MySQLUserVehicleRepository();
           // Araç listesini yüklemek için yine de bir vehicleRepo'ya ihtiyacımız var.
            SellerVehicleRepositoryInterface vehicleRepo = new Infrastructure.MySQLVehicleRepository();

            List<Vehicle> allVehicles = vehicleRepo.loadAllVehicles();
            if (allVehicles.isEmpty()) {
                System.out.println("❗ No vehicles available in the system. Contact a seller.");
                return false;
            }

            System.out.println("\nPlease select your vehicle from the list:");
            for (int i = 0; i < allVehicles.size(); i++) {
                domain.vehicle.Vehicle v = allVehicles.get(i);
                System.out.println((i + 1) + ". " + "Car" + " - " + v.getBrand() + " " + v.getModel() + " " + v.getVehicleSegment());
            }

            System.out.print("Your choice: ");
            Scanner input = new Scanner(System.in);
            int index = Integer.parseInt(input.nextLine()) - 1;

            if (index < 0 || index >= allVehicles.size()) {
                System.out.println("❗ Invalid vehicle selection.");
                return false;
            }

            Vehicle selected = allVehicles.get(index);
            userVehicleRepo.assignVehicleToUser(mail, selected);
            System.out.println("✅ Vehicle assigned successfully.");
        }
        return true;
    }
}
