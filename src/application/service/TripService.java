package application.service;

import application.repository.TripRepositoryInterface;
import domain.trip.Trip;

import java.io.IOException;
import java.util.List;

public class TripService { // bu class trip reposuna trip eklemek veya bütün trip geçmişini çekmek için var,clean archa daha uygun bu yöntem.
    private final TripRepositoryInterface tripRepository;

    public TripService(TripRepositoryInterface tripRepository) {
        this.tripRepository = tripRepository;
    }

    public void saveTrip(Trip trip) {
        try {
            tripRepository.saveTrip(trip);
        } catch (IOException e) {
            System.out.println("❗ Trip could not be saved: " + e.getMessage());
        }
    }

    public List<Trip> getAllTrips() {
        try {
            return tripRepository.loadAllTrips();
        } catch (IOException e) {
            System.out.println("❗ Could not load trips: " + e.getMessage());
            return List.of();
        }
    }
}
