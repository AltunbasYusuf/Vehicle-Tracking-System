package Infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    // Değerleri ortam değişkenlerinden okuyoruz.
    private static final String DATABASE_URL = System.getenv("DB_URL");
    private static final String DATABASE_USER = System.getenv("DB_USER");
    private static final String DATABASE_PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        // Değişkenlerin ayarlanıp ayarlanmadığını kontrol edelim
        if (DATABASE_URL == null || DATABASE_USER == null || DATABASE_PASSWORD == null) {
            throw new SQLException("Veritabanı ortam değişkenleri (DB_URL, DB_USER, DB_PASSWORD) ayarlanmamış!");
        }
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }
}