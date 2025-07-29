package Infrastructure;

import application.repository.ElectricVehicleCatalogRepositoryInterface;
import domain.vehicle.ElectricVehicle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLElectricVehicleCatalogRepository implements ElectricVehicleCatalogRepositoryInterface {

    @Override
    public List<ElectricVehicle> loadAllElectricVehicles() throws IOException {
        // Yeni tablomuzdan veri çekecek SQL sorgusu
        String sql = "SELECT brand, model, segment, hp, fuel_consumption FROM electric_vehicles_catalog";
        List<ElectricVehicle> electricVehicles = new ArrayList<>();

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String segment = rs.getString("segment");
                int hp = rs.getInt("hp");
                double fuelConsumption = rs.getDouble("fuel_consumption");

                // Veritabanından okunan verilerle yeni bir ElectricVehicle nesnesi oluştur
                ElectricVehicle ev = new ElectricVehicle(model, segment, hp, fuelConsumption);
                electricVehicles.add(ev);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Hatayı daha üst katmana bildirmek için IOException fırlatıyoruz
            throw new IOException("Elektrikli araç kataloğu okunurken veritabanı hatası oluştu.", e);
        }

        return electricVehicles;
    }
}