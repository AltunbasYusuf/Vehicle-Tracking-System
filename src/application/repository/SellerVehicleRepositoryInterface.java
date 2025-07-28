package application.repository;

import domain.vehicle.Vehicle;

import java.io.IOException;
import java.util.List;

public interface SellerVehicleRepositoryInterface { // bu repo sadece sellerin göreceği bir repo olacak

    void saveVehicle(Vehicle vehicle) throws IOException;
    List<Vehicle> loadAllVehicles() throws IOException;
}
