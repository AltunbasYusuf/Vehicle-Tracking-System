package application.repository;

import domain.vehicle.Vehicle;

import java.io.IOException;
import java.util.List;

public interface SellerVehicleRepositoryInterface {
    // Aracı kaydederken hangi satıcının kaydettiğini belirtiyoruz.
    void saveVehicle(Vehicle vehicle, String sellerEmail) throws IOException;

    // Sadece belirli bir satıcıya ait araçları yükleyecek metot.
    List<Vehicle> loadAllVehiclesBySeller(String sellerEmail) throws IOException;

    // Bu metodu, elektrikli araç önerisi gibi tüm araçlara ihtiyaç duyan
    // servisler için koruyoruz.
    List<Vehicle> loadAllVehicles() throws IOException;
}
