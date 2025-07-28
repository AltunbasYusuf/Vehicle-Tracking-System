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

    // ✅ Kullanıcı e-mail ile trip kaydı
    public void saveTrip(String email, Trip trip) {
        try {
            tripRepository.saveTrip(email, trip);
        } catch (IOException e) {
            System.out.println("❗ Trip could not be saved: " + e.getMessage());
        }
    }

    // ✅ Belirli kullanıcıya ait tüm tripleri getir
    public List<Trip> getAllTrips(String email) {
        try {
            return tripRepository.loadTripsForUser(email);
        } catch (IOException e) {
            System.out.println("❗ Could not load trips: " + e.getMessage());
            return List.of();
        }
    }
}
