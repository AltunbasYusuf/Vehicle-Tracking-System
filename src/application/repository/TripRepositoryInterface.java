package application.repository;

import domain.trip.Trip;

import java.io.IOException;
import java.util.List;

public interface TripRepositoryInterface {
    void saveTrip(Trip trip) throws IOException;
    List<Trip> loadAllTrips() throws IOException;
}
//tripleri savelemek kaldı