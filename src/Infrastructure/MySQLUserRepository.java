package Infrastructure;

import application.repository.UserRepositoryInterface;
import domain.user.User;
import domain.user.VehicleOwner;
import domain.user.VehicleSeller;

import java.sql.*;

public class MySQLUserRepository implements UserRepositoryInterface {

    @Override
    public void save(User user) {
        // SQL INSERT komutu
        String sql = "INSERT INTO users (email, hashed_password, user_role, name, surname) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getMail());
            pstmt.setString(2, user.getHashedPassword());
            pstmt.setString(3, user.getRole().name()); // Enum'ı String'e çevirir
            pstmt.setString(4, user.getName());
            pstmt.setString(5, user.getSurname());

            pstmt.executeUpdate(); // Komutu çalıştır

        } catch (SQLException e) {
            e.printStackTrace(); // Hata olursa konsola yazdır
        }
    }

    @Override
    public boolean doesExist(String email) {
        // 'findByMail' metodu zaten kullanıcıyı buluyor, onu kullanabiliriz.
        return findByMail(email) != null;
    }

    @Override
    public User findByMail(String mail) {
        // SQL SELECT komutu
        String sql = "SELECT * FROM users WHERE email = ?";
        User user = null;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, mail); // SQL'deki '?' yerine mail değişkenini koyar

            ResultSet rs = pstmt.executeQuery(); // Sorguyu çalıştır ve sonuçları al

            if (rs.next()) { // Eğer bir sonuç bulunduysa
                String emailDb = rs.getString("email");
                String passwordDb = rs.getString("hashed_password");
                String nameDb = rs.getString("name");
                String surnameDb = rs.getString("surname");
                String roleDb = rs.getString("user_role");

                if (roleDb.equalsIgnoreCase("VEHICLE_SELLER")) {
                    user = new VehicleSeller(emailDb, passwordDb, nameDb, surnameDb);
                } else if (roleDb.equalsIgnoreCase("VEHICLE_OWNER")) {
                    user = new VehicleOwner(emailDb, passwordDb, nameDb, surnameDb);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}