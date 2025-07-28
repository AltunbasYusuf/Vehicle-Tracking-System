package Infrastructure;

import application.repository.SellerVehicleRepositoryInterface;
import domain.maintenance.PartFactory;
import domain.maintenance.VehiclePart;
import domain.vehicle.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MySQLVehicleRepository implements SellerVehicleRepositoryInterface {

    private final Gson gson = new Gson();

    @Override
    public void saveVehicle(Vehicle vehicle) {
        String vehicleSql = "INSERT INTO vehicles(brand, model, segment, fuel_type, hp, fuel_consumption) VALUES(?, ?, ?, ?, ?, ?)";
        String partsSql = "INSERT INTO vehicle_parts(vehicle_id, part_type, properties) VALUES(?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection()) {
            // Transaction'ı başlat
            conn.setAutoCommit(false);

            try (PreparedStatement vehiclePstmt = conn.prepareStatement(vehicleSql, Statement.RETURN_GENERATED_KEYS)) {

                vehiclePstmt.setString(1, vehicle.getBrand());
                vehiclePstmt.setString(2, vehicle.getModel());
                vehiclePstmt.setString(3, vehicle.getVehicleSegment());
                vehiclePstmt.setString(4, vehicle.getFueltype());
                vehiclePstmt.setDouble(5, vehicle.getHP());
                vehiclePstmt.setDouble(6, vehicle.getFuelConsumption());
                vehiclePstmt.executeUpdate();

                // Oluşturulan aracın ID'sini al
                ResultSet generatedKeys = vehiclePstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int vehicleId = generatedKeys.getInt(1);

                    // Parçaları kaydet
                    try (PreparedStatement partsPstmt = conn.prepareStatement(partsSql)) {
                        for (VehiclePart part : vehicle.getParts()) {
                            partsPstmt.setInt(1, vehicleId);
                            partsPstmt.setString(2, part.getPartType());
                            // Parça özelliklerini JSON string'ine çevir
                            partsPstmt.setString(3, gson.toJson(part.getProperties()));
                            partsPstmt.addBatch();
                        }
                        partsPstmt.executeBatch();
                    }
                }
                conn.commit(); // Her şey başarılıysa onayla
            } catch (SQLException e) {
                conn.rollback(); // Hata olursa geri al
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Vehicle> loadAllVehicles() {
        String sql = "SELECT * FROM vehicles";
        List<Vehicle> vehicles = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("segment")
                );
                vehicle.setFuelType(rs.getString("fuel_type"));
                vehicle.setHP(rs.getDouble("hp"));
                vehicle.setFuelConsumption(rs.getDouble("fuel_consumption"));

                // Araç parçalarını yükle
                loadPartsForVehicle(conn, rs.getInt("vehicle_id"), vehicle);
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    private void loadPartsForVehicle(Connection conn, int vehicleId, Vehicle vehicle) throws SQLException {
        String sql = "SELECT * FROM vehicle_parts WHERE vehicle_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String partType = rs.getString("part_type");
                String propertiesJson = rs.getString("properties");

                // JSON string'ini Map'e çevir
                Map<String, Object> properties = gson.fromJson(propertiesJson, new TypeToken<Map<String, Object>>(){}.getType());

                // PartFactory kullanarak parçayı oluştur ve araca ekle
                VehiclePart part = PartFactory.createPart(partType, properties);
                vehicle.addPart(part);
            }
        }
    }
}