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
            writer.write( vehicle.getBrand() + "," + vehicle.getModel()+ "," + vehicle.getVehicleSegment()+","+ vehicle.getFueltype() + "," + vehicle.getHP() + "," + vehicle.getFuelConsumption());
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
                if (parts.length != 6) continue;

                String brand = parts[0];
                String model = parts[1];
                String segment = parts[2];
                String fuelType=parts[3];
                double HP = Double.parseDouble(parts[4].trim());
                double fuelConsumption = Double.parseDouble(parts[5].trim());

                Vehicle vehicle = new Vehicle(brand, model,segment); // Part eklemeyi runtime’da yapabilirsin
                vehicle.setFuelType(fuelType);
                vehicle.setHP(HP);
                vehicle.setFuelConsumption(fuelConsumption);
                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }
}
