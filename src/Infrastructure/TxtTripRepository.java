package Infrastructure;

import application.repository.TripRepositoryInterface;
import domain.location.Location;
import domain.trip.Trip;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TxtTripRepository  implements TripRepositoryInterface {

    String filePath;

    public TxtTripRepository(String fPath){
        this.filePath=fPath;
    }


    @Override
    public void saveTrip(String ownerEmail, Trip trip) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            StringBuilder line = new StringBuilder();
            line.append(ownerEmail).append(",") // ✅ email başta
                    .append(trip.getStart()).append(",")
                    .append(trip.getEnd()).append(",")
                    .append(trip.getDistance()).append(",")
                    .append(trip.getDescription() != null ? trip.getDescription() : "null");

            if (trip.getStartLocation() != null && trip.getEndLocation() != null) {
                line.append(",")
                        .append(trip.getStartLocation().getLatitude()).append(",")
                        .append(trip.getStartLocation().getLongitude()).append(",")
                        .append(trip.getEndLocation().getLatitude()).append(",")
                        .append(trip.getEndLocation().getLongitude());
            }

            writer.write(line.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("❗ Error writing trip record.");
        }
    }


    @Override
    public List<Trip> loadTripsForUser(String ownerEmail) throws IOException {
        List<Trip> trips = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String email = parts[0];
                if (!email.equals(ownerEmail)) continue; // sadece bu kullanıcıya ait kayıtlar

                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                double distance = Double.parseDouble(parts[3]);
                String description = parts[4].equals("null") ? null : parts[4];

                if (parts.length >= 9) {
                    double sLat = Double.parseDouble(parts[5]);
                    double sLon = Double.parseDouble(parts[6]);
                    double eLat = Double.parseDouble(parts[7]);
                    double eLon = Double.parseDouble(parts[8]);

                    Location startLoc = new Location(sLat, sLon);
                    Location endLoc = new Location(eLat, eLon);

                    trips.add(new Trip(start, end, description, startLoc, endLoc));
                } else {
                    trips.add(new Trip(start, end, description, distance));
                }
            }
        }

        return trips;
    }

}
