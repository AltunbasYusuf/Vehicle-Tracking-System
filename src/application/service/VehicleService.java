package application.service;

import application.repository.SellerVehicleRepositoryInterface;
import domain.vehicle.Vehicle;

import java.io.IOException;
import java.util.List;

public class VehicleService {
    private final SellerVehicleRepositoryInterface repo;

    public VehicleService(SellerVehicleRepositoryInterface repo) {
        this.repo = repo;
    }

    // Metot artık satıcı e-postasını da alıyor.
    public void addVehicle(Vehicle v, String sellerEmail) {
        try {
            // Repository'e satıcı bilgisiyle birlikte gönderiyoruz.
            repo.saveVehicle(v, sellerEmail);
        } catch (IOException e) {
            System.out.println("❗ Failed to save vehicle: " + e.getMessage());
        }
    }

    // Sadece belirli bir satıcının araçlarını getiren yeni metot.
    public List<Vehicle> getAllVehiclesBySeller(String sellerEmail) {
        try {
            return repo.loadAllVehiclesBySeller(sellerEmail);
        } catch (IOException e) {
            System.out.println("❗ Failed to load vehicles: " + e.getMessage());
            return List.of();
        }
    }

    // Tüm araçları getiren eski metot, sistemin başka yerlerinde
    // (örn: elektrikli araç tavsiyesi) gerekebilir diye duruyor.
    public List<Vehicle> getAllVehicles() {
        try {
            return repo.loadAllVehicles();
        } catch (IOException e) {
            System.out.println("❗ Failed to load vehicles: " + e.getMessage());
            return List.of();
        }
    }
}