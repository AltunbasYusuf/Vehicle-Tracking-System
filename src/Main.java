import application.service.ElectricVehicleSuggestionService;
import domain.vehicle.Vehicle;
import presentation.FirstMenu;
import presentation.setup.Quick_Setup;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        FirstMenu.start();
        Vehicle vehicle = new Vehicle(
                "Toyota",         // brand
                "Corolla",            // model
                "sedan"        // segment
        );

        vehicle.setFuelType("gasoline");
        vehicle.setHP(132); // örnek HP

        // Elektrikli araç önerisi
        ElectricVehicleSuggestionService.electricVehicleSuggestion(vehicle);
    }
}