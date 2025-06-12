package domain.user;



public class VehicleOwner extends User {
    public VehicleOwner(String mail, String hashedPassword, String name, String surname) {
        super(mail, hashedPassword, name, surname, UserRole.VEHICLE_OWNER);
    }
}
