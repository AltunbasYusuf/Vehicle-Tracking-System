package Infrastructure;

import domain.vehicle.ElectricVehicle;

import java.io.*;
import java.util.*;

public class ElectricVehicleFileRepository {

    private final String filePath = "./electric_vehicles.txt";

    public List<ElectricVehicle> loadElectricVehicles() {
        List<ElectricVehicle> vehicles = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                String model = parts[0].trim();
                String segment = parts[1].trim().toUpperCase();
                int hp = Integer.parseInt(parts[2].trim().split(" ")[0]);

                vehicles.add(new ElectricVehicle(model, segment, hp));
            }

        } catch (IOException e) {
            System.err.println("Error reading electric vehicle data: " + e.getMessage());
        }

        return vehicles;
    }
}
