package application.service;

import application.repository.SellerVehicleRepositoryInterface;
import application.repository.SellerVehicleRepositoryInterface;
import domain.vehicle.Vehicle;

import java.io.IOException;
import java.util.List;

public class VehicleService {
    private final SellerVehicleRepositoryInterface repo;

    public VehicleService(SellerVehicleRepositoryInterface repo) {
        this.repo = repo;
    }

    public void addVehicle(Vehicle v) {
        try {
            repo.saveVehicle(v);
        } catch (IOException e) {
            System.out.println("❗ Failed to save vehicle: " + e.getMessage());
        }
    }

    public List<Vehicle> getAllVehicles() {
        try {
            return repo.loadAllVehicles();
        } catch (IOException e) {
            System.out.println("❗ Failed to load vehicles: " + e.getMessage());
            return List.of();
        }
    }
}
