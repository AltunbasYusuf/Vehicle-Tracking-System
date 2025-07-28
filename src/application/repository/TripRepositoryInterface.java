package application.repository;

import domain.trip.Trip;

import java.io.IOException;
import java.util.List;

public interface TripRepositoryInterface {
    void saveTrip(String ownerEmail, Trip trip) throws IOException;
    List<Trip> loadTripsForUser(String ownerEmail) throws IOException;
}

//tripleri savelemek kaldı