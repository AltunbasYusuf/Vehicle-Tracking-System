package application.repository;

import domain.vehicle.ElectricVehicle;
import java.io.IOException;
import java.util.List;

/**
 * Bu arayüz, sisteme önceden tanımlanmış elektrikli araçların
 * kataloğundan veri okumak için kullanılır.
 */
public interface ElectricVehicleCatalogRepositoryInterface {

    /**
     * Veritabanındaki tüm elektrikli araçları bir liste olarak yükler.
     * @return Elektrikli araç nesnelerinden oluşan bir liste.
     * @throws IOException Veritabanı okuma hatası olursa fırlatılır.
     */
    List<ElectricVehicle> loadAllElectricVehicles() throws IOException;
}