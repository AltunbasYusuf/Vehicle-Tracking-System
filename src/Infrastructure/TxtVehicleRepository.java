package Infrastructure;

import application.repository.SellerVehicleRepositoryInterface;
import domain.maintenance.VehiclePart;
import domain.vehicle.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            writer.write("Parts:");
            writer.newLine();
            for (VehiclePart part : vehicle.getParts()) {
                writer.write(part.getPartType() + ":");
                for (String key : part.getProperties().keySet()) {
                    writer.write(key + "=" + part.getProperties().get(key) + ";");
                }
                writer.newLine();
            }
            writer.write("---");
            writer.newLine();
            writer.newLine();
        }
    }

    @Override
    public List<Vehicle> loadAllVehicles() throws IOException {
        List<Vehicle> vehicles = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 6) continue;

                String brand = parts[0];
                String model = parts[1];
                String segment = parts[2];
                String fuelType = parts[3];
                double HP = Double.parseDouble(parts[4].trim());
                double fuelConsumption = Double.parseDouble(parts[5].trim());

                Vehicle vehicle = new Vehicle(brand, model, segment);
                vehicle.setFuelType(fuelType);
                vehicle.setHP(HP);
                vehicle.setFuelConsumption(fuelConsumption);

                // Şimdi sıradaki satırlar parçalar
                while ((line = reader.readLine()) != null) {
                    if (line.equals("---")) break; // bir araç bitti
                    if (line.equals("Parts:")) continue;

                    String[] typeAndProps = line.split(":");
                    if (typeAndProps.length != 2) continue;

                    String partType = typeAndProps[0];
                    String[] propPairs = typeAndProps[1].split(";");
                    Map<String, Object> propMap = new HashMap<>();

                    for (String pair : propPairs) {
                        if (pair.trim().isEmpty()) continue;
                        String[] kv = pair.split("=");
                        if (kv.length != 2) continue;

                        String key = kv[0];
                        String value = kv[1];

                        Object parsed;
                        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                            parsed = Boolean.parseBoolean(value);
                        } else {
                            try {
                                parsed = Double.parseDouble(value);
                            } catch (NumberFormatException e) {
                                parsed = value;
                            }
                        }

                        propMap.put(key, parsed);
                    }

                    // PartFactory kullanarak parça üret
                    VehiclePart part = domain.maintenance.PartFactory.createPart(partType, propMap);
                    if (part != null) {
                        vehicle.addPart(part);
                    }
                }

                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }

}
