package domain.user;


//TODO: verifyPassword() metodunun içini yaz.


import application.security.PasswordEncoderInterface;
import domain.vehicle.Vehicle;

public abstract class User {
    protected String mail;
    protected String hashedPassword;
    protected String name;
    protected String surname;
    protected UserRole role;
    protected Vehicle  vehicle;


    public enum UserRole {
        VEHICLE_OWNER,
        VEHICLE_SELLER
    }

    public User(String mail, String hashedPassword, String name, String surname, UserRole role) {
        this.mail = mail;
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getMail() {
        return mail;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean verifyPassword(String rawPassword, PasswordEncoderInterface encoder) {
        return encoder.matches(rawPassword, this.hashedPassword);
    }

}
