package Infrastructure;

import application.repository.MaintenanceRepositoryInterface;
import domain.maintenance.MaintenanceRecord;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MySQLMaintenanceRepository implements MaintenanceRepositoryInterface {

    @Override
    public void addRecord(String ownerEmail, String partName, MaintenanceRecord record) throws IOException {
        String sql = "INSERT INTO maintenance_records(owner_email, part_name, record_date, description, next_suggested_date) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ownerEmail);
            pstmt.setString(2, partName);
            // java.time.LocalDate'i java.sql.Date'e çeviriyoruz
            pstmt.setDate(3, Date.valueOf(record.getCurrentDate()));
            pstmt.setString(4, record.getMaintenanceDescription());
            pstmt.setDate(5, Date.valueOf(record.getNextSuggestedMaintenanceDate()));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException("Bakım kaydı eklenirken veritabanı hatası oluştu.", e);
        }
    }

    @Override
    public List<MaintenanceRecord> getRecords(String ownerEmail, String partName) throws IOException {
        List<MaintenanceRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM maintenance_records WHERE owner_email = ? AND part_name = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ownerEmail);
            pstmt.setString(2, partName);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // java.sql.Date'i java.time.LocalDate'e çeviriyoruz
                LocalDate recordDate = rs.getDate("record_date").toLocalDate();
                String description = rs.getString("description");
                LocalDate nextDate = rs.getDate("next_suggested_date").toLocalDate();

                MaintenanceRecord record = new MaintenanceRecord(recordDate, description, nextDate);
                records.add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException("Bakım kayıtları okunurken veritabanı hatası oluştu.", e);
        }

        return records;
    }
}