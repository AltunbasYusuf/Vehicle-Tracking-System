package domain.user;

public class VehicleSeller extends User {
    public VehicleSeller(String mail, String hashedPassword, String name, String surname) {
        super(mail, hashedPassword, name, surname, UserRole.VEHICLE_SELLER);
    }
}
