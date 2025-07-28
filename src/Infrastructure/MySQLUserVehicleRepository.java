package Infrastructure;

import application.repository.SellerVehicleRepositoryInterface;
import application.repository.UserVehicleRepositoryInterface;
import domain.vehicle.Vehicle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySQLUserVehicleRepository implements UserVehicleRepositoryInterface {

    // Bu sınıf, diğer araçları yükleyebilmek için SellerVehicleRepository'ye ihtiyaç duyar.
    private final SellerVehicleRepositoryInterface vehicleRepo;

    public MySQLUserVehicleRepository() {
        // Yeni MySQL tabanlı repository'mizi kullanıyoruz.
        this.vehicleRepo = new MySQLVehicleRepository();
    }

    @Override
    public void assignVehicleToUser(String email, Vehicle vehicle) throws IOException {
        // Önce aracın 'vehicles' tablosunda bir ID'si olduğundan emin olmalıyız.
        // Ancak bu mantık zaten 'saveVehicle' içinde halledildiği için,
        // burada sadece eşleştirme yapacağız. Aracın ID'sini bulmalıyız.
        int vehicleId = findVehicleId(vehicle);
        if (vehicleId == -1) {
            throw new IOException("Veritabanında araç bulunamadı, eşleştirme yapılamadı.");
        }

        String sql = "INSERT INTO user_vehicles (email, vehicle_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setInt(2, vehicleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Eğer kullanıcıya zaten bir araç atanmışsa (PRIMARY KEY ihlali),
            // belki güncelleme yapmak isteyebiliriz. Şimdilik hatayı yazdıralım.
            e.printStackTrace();
            throw new IOException("Kullanıcıya araç atanırken veritabanı hatası.", e);
        }
    }

    @Override
    public Vehicle getVehicleForUser(String email) throws IOException {
        String sql = "SELECT vehicle_id FROM user_vehicles WHERE email = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int vehicleId = rs.getInt("vehicle_id");
                // Şimdi bu ID'ye sahip aracı bulalım.
                // Bunun için tüm araçları yükleyip içinden arayabiliriz.
                List<Vehicle> allVehicles = vehicleRepo.loadAllVehicles();
                for (Vehicle v : allVehicles) {
                    // Bu karşılaştırma için Vehicle sınıfına bir getId() metodu eklemek
                    // veya buradaki gibi özellik karşılaştırması yapmak gerekir.
                    // Şimdilik en basit yol, ID'yi bulup tekrar sorgu atmak olabilir.
                    // Ya da `findVehicleById` gibi bir metod yazılabilir.
                    // `MySQLVehicleRepository`'de bu metod olmadığı için,
                    // şimdilik tüm araçlar içinde arama yapıyoruz.
                    // TODO: Daha verimli bir yol için `MySQLVehicleRepository`'e `findVehicleById` eklenebilir.
                    if (findVehicleId(v) == vehicleId) {
                        return v;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException("Kullanıcının aracı okunurken veritabanı hatası.", e);
        }
        return null;
    }

    // Yardımcı metod: Bir aracın ID'sini veritabanından bulur.
    private int findVehicleId(Vehicle vehicle) {
        String sql = "SELECT vehicle_id FROM vehicles WHERE brand = ? AND model = ? AND segment = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vehicle.getBrand());
            pstmt.setString(2, vehicle.getModel());
            pstmt.setString(3, vehicle.getVehicleSegment());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("vehicle_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Bulunamadı
    }
}