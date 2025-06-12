package Infrastructure;

import application.repository.SellerVehicleRepositoryInterface;
import domain.vehicle.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TxtVehicleRepository implements SellerVehicleRepositoryInterface {

    private final String filePath;

    public TxtVehicleRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveVehicle(Vehicle vehicle) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(vehicle.getVehicleType() + "," + vehicle.getBrand() + "," + vehicle.getModel());
            writer.newLine();
        }
    }

    @Override
    public List<Vehicle> loadAllVehicles() throws IOException {
        List<Vehicle> vehicles = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                String type = parts[0];
                String brand = parts[1];
                String model = parts[2];

                Vehicle vehicle = new Vehicle(type, brand, model); // Part eklemeyi runtime’da yapabilirsin
                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }
}
