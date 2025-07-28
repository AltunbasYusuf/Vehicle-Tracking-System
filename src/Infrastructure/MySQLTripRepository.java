package Infrastructure;

import application.repository.TripRepositoryInterface;
import domain.location.Location;
import domain.trip.Trip;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MySQLTripRepository implements TripRepositoryInterface {

    @Override
    public void saveTrip(String ownerEmail, Trip trip) throws IOException {
        String sql = "INSERT INTO trips(owner_email, start_time, end_time, distance, description, start_lat, start_lon, end_lat, end_lon) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ownerEmail);
            // java.time.LocalDateTime'ı java.sql.Timestamp'e çeviriyoruz
            pstmt.setTimestamp(2, Timestamp.valueOf(trip.getStart()));
            pstmt.setTimestamp(3, Timestamp.valueOf(trip.getEnd()));
            pstmt.setDouble(4, trip.getDistance());
            pstmt.setString(5, trip.getDescription());

            // Konum bilgileri null olabilir, kontrol ediyoruz
            if (trip.getStartLocation() != null) {
                pstmt.setDouble(6, trip.getStartLocation().getLatitude());
                pstmt.setDouble(7, trip.getStartLocation().getLongitude());
            } else {
                pstmt.setNull(6, Types.DOUBLE);
                pstmt.setNull(7, Types.DOUBLE);
            }

            if (trip.getEndLocation() != null) {
                pstmt.setDouble(8, trip.getEndLocation().getLatitude());
                pstmt.setDouble(9, trip.getEndLocation().getLongitude());
            } else {
                pstmt.setNull(8, Types.DOUBLE);
                pstmt.setNull(9, Types.DOUBLE);
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException("Seyahat kaydı eklenirken veritabanı hatası oluştu.", e);
        }
    }

    @Override
    public List<Trip> loadTripsForUser(String ownerEmail) throws IOException {
        List<Trip> trips = new ArrayList<>();
        String sql = "SELECT * FROM trips WHERE owner_email = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ownerEmail);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("end_time").toLocalDateTime();
                String description = rs.getString("description");

                // Konum bilgisi var mı diye kontrol et
                // getDouble() metodu null dönerse 0.0 verir, bu yüzden getObject() ile kontrol ediyoruz.
                if (rs.getObject("start_lat") != null) {
                    Location startLoc = new Location(rs.getDouble("start_lat"), rs.getDouble("start_lon"));
                    Location endLoc = new Location(rs.getDouble("end_lat"), rs.getDouble("end_lon"));
                    trips.add(new Trip(startTime, endTime, description, startLoc, endLoc));
                } else {
                    // Sadece mesafe ile kaydedilmiş seyahat
                    double distance = rs.getDouble("distance");
                    trips.add(new Trip(startTime, endTime, description, distance));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException("Seyahat kayıtları okunurken veritabanı hatası oluştu.", e);
        }

        return trips;
    }
}