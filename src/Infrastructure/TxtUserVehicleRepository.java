package Infrastructure;

import application.repository.SellerVehicleRepositoryInterface;
import application.repository.UserVehicleRepositoryInterface;
import domain.vehicle.Vehicle;

import java.io.*;
import java.util.List;

public class TxtUserVehicleRepository implements UserVehicleRepositoryInterface {

    private final String filePath;
    private final SellerVehicleRepositoryInterface vehicleRepo;

    public TxtUserVehicleRepository(String filePath, SellerVehicleRepositoryInterface vehicleRepo) {
        this.filePath = filePath;
        this.vehicleRepo = vehicleRepo;
    }

    @Override
    public void assignVehicleToUser(String email, Vehicle vehicle) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(email + "," + vehicle.getVehicleType() + "," + vehicle.getBrand() + "," + vehicle.getModel());
            writer.newLine();
        }
    }

    @Override
    public Vehicle getVehicleForUser(String email) throws IOException {
        List<Vehicle> allVehicles = vehicleRepo.loadAllVehicles();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 4) continue;
                if (!parts[0].equals(email)) continue;

                String type = parts[1];
                String brand = parts[2];
                String model = parts[3];

                for (Vehicle v : allVehicles) {
                    if (v.getVehicleType().equals(type) &&
                            v.getBrand().equals(brand) &&
                            v.getModel().equals(model)) {
                        return v;
                    }
                }
            }
        }
        return null;
    }
}
