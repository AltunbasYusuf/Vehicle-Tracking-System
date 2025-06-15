package Infrastructure;

import application.repository.MaintenanceRepositoryInterface;
import domain.maintenance.MaintenanceRecord;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TxtMaintenanceRepository implements MaintenanceRepositoryInterface {

    private final String filePath;

    public TxtMaintenanceRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void addRecord(String ownerEmail, String partName, MaintenanceRecord record) throws IOException {
        // Format: email,partName,currentDate,description,nextSuggestedDate
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(ownerEmail + "," +
                    partName + "," +
                    record.getCurrentDate() + "," +
                    record.getMaintenanceDescription() + "," +
                    record.getNextSuggestedMaintenanceDate());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("❗ Error writing maintenance record for " + ownerEmail);
        }
    }

    @Override
    public List<MaintenanceRecord> getRecords(String ownerEmail, String partName) throws IOException {
        List<MaintenanceRecord> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("maintenance.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length != 5) {
                    System.out.println("⚠ Invalid maintenance line format: " + line);
                    continue;
                }

                String email = parts[0];
                String pName = parts[1];

                if (!email.equalsIgnoreCase(ownerEmail) || !pName.equalsIgnoreCase(partName)) {
                    continue; // sadece bu kullanıcı ve bu parçaya ait kayıtlar
                }

                LocalDate currentDate = LocalDate.parse(parts[2]);
                String description = parts[3];
                LocalDate nextSuggestedDate = LocalDate.parse(parts[4]);

                MaintenanceRecord record = new MaintenanceRecord(currentDate, description, nextSuggestedDate);
                result.add(record);
            }
        } catch (IOException e) {
            System.out.println("❗ Error reading maintenance records from file!");
        }

        return result;
    }

}
