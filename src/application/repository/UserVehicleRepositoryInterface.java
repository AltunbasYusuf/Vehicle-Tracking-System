package application.repository;


import domain.vehicle.Vehicle;

import java.io.IOException;

public interface UserVehicleRepositoryInterface {
    void assignVehicleToUser(String email, Vehicle vehicle) throws IOException;

    Vehicle getVehicleForUser(String email) throws IOException;
}
