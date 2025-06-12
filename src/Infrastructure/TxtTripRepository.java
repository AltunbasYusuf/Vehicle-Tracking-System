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
    public void saveTrip(Trip trip) throws IOException {

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))){

            StringBuilder line = new StringBuilder();
            line.append(trip.getStart()).append(",")
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

        }
        catch (IOException e){
            System.out.println("Something went wrong when writing to the Trip txt file!");
        }

    }

    @Override
    public List<Trip> loadAllTrips() throws IOException {

        List<Trip> trips = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length < 4) continue;

                LocalDateTime start = LocalDateTime.parse(parts[0]);
                LocalDateTime end = LocalDateTime.parse(parts[1]);
                double distance = Double.parseDouble(parts[2]);
                String description = parts[3].equals("null") ? null : parts[3];

                if (parts.length >= 8) {
                    double sLat = Double.parseDouble(parts[4]);
                    double sLon = Double.parseDouble(parts[5]);
                    double eLat = Double.parseDouble(parts[6]);
                    double eLon = Double.parseDouble(parts[7]);

                    Location startLoc = new Location(sLat, sLon);
                    Location endLoc = new Location(eLat, eLon);

                    trips.add(new Trip(start, end, description, startLoc, endLoc));
                } else {
                    trips.add(new Trip(start, end, description, distance));
                }
            }
        }
        catch (IOException e){
            System.out.println("Something went wrong when reading to the Trip txt file!");
        }

        return trips;
    }
}
