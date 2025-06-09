package application;

import application.repository.UserRepositoryInterface;
import application.security.PasswordEncoderInterface;
import domain.user.User;
import domain.user.VehicleOwner;
import domain.user.VehicleSeller;

import java.io.IOException;

import static domain.user.User.UserRole.VEHICLE_OWNER;
import static domain.user.User.UserRole.VEHICLE_SELLER;

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
        return true;
    }

}
