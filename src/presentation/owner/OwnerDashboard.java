
package presentation.owner;

import Infrastructure.TxtTripRepository;
import application.repository.TripRepositoryInterface;
import application.service.EmissionService;
import application.service.NotificationService;
import application.service.TripService;
import domain.location.Location;
import domain.maintenance.MaintenanceRecord;
import domain.maintenance.VehiclePart;
import domain.trip.Trip;
import domain.user.User;
import domain.vehicle.Vehicle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;



public class OwnerDashboard {

    private static final Scanner input = new Scanner(System.in); //kullanacağımız scanner

    public static void open(User user) throws IOException {
        Vehicle vehicle = user.getVehicle(); // sistemde sadece 1 araç var

        while (true) {
            System.out.println("\n📋 Owner Dashboard - " + user.getName());
            System.out.println("Araç Tipi: " + vehicle.getVehicleType());
            System.out.println("1. Show vehicle parts conditions");
            System.out.println("2. Add a maintenance request");
            System.out.println("3. Show maintenance history");
            System.out.println("4. Add trip");
            System.out.println("5. Show trip history");
            System.out.println("0. Exit");
            System.out.print("Your Choice: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    showParts(vehicle);
                    break;
                case "2":
                    addMaintenance(vehicle);
                    break;
                case "3":
                    showMaintenanceHistory(vehicle);
                    break;
                case "4":
                    addTrip(user);
                    break;
                case "5":
                    showTripHistory();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("❗ Invalid choice.");
            }
        }
    }

    private static void showParts(Vehicle vehicle) {
        System.out.println("\n🔩 Parts:");
        for (VehiclePart part : vehicle.getParts()) {
            System.out.println(" - " + part.getPartType() + " | Date of next maintenance: " + part.getNextMaintenanceDate());
            part.registerObserver(NotificationService.getInstance());
            part.checkMaintenance();
        }
    }

    private static void addMaintenance(Vehicle vehicle) throws IOException {
        List<VehiclePart> parts = vehicle.getParts();

        System.out.println("\n🛠️ Choose the vehicle part for the maintenance:");
        for (int i = 0; i < parts.size(); i++) {
            System.out.println((i + 1) + ". " + parts.get(i).getPartType());
        }

        System.out.print("Your choice: ");
        int index = Integer.parseInt(input.nextLine()) - 1;
        if (index < 0 || index >= parts.size()) {
            System.out.println("❗ Invalid choice.");
            return;
        }

        VehiclePart part = parts.get(index);

        // 🔹 Hazır açıklamalar
        List<String> options = part.getMaintenanceDescriptions(); // her vehicle partının kendi içinde saklı kendine ait olası maintenance açıklamaları var.


        System.out.println("\n Select a description:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
        System.out.println((options.size() + 1) + ". Write your own description");

        System.out.print("Choice: ");
        int choice = Integer.parseInt(input.nextLine());

        String desc;
        if (choice >= 1 && choice <= options.size()) {
            desc = options.get(choice - 1);
        } else {
            System.out.print("Your own description: ");
            desc = input.nextLine();
        }

        //  Kayıt oluştur ve parçaya ekle
        MaintenanceRecord record = new MaintenanceRecord(desc, part);
        part.addMaintenanceRecord(record);

        System.out.println(" Maintenance request has been successfully made.");
    }


    private static void showMaintenanceHistory(Vehicle vehicle) {
        List<VehiclePart> parts = vehicle.getParts();

        System.out.println("\n Choose the part you want to see the maintenance history:");
        for (int i = 0; i < parts.size(); i++) {
            System.out.println((i + 1) + ". " + parts.get(i).getPartType());
        }

        System.out.print("Your choice: ");
        int index = Integer.parseInt(input.nextLine()) - 1;
        if (index < 0 || index >= parts.size()) {
            System.out.println("❗Invalid choice.");
            return;
        }

        VehiclePart part = parts.get(index);
        List<MaintenanceRecord> history = part.getMaintenanceHistory();

        if (history.isEmpty()) {
            System.out.println(" No maintenance record yet.");
        } else {
            System.out.println("\n " + part.getPartType() + " maintenance history:");
            for (MaintenanceRecord r : history) {
                System.out.println(r.toString());
            }
        }
    }

    private static void addTrip(User user){

        Trip trip = null;

        TripRepositoryInterface repo = new TxtTripRepository("trip.txt");
        TripService tripService = new TripService(repo);

        boolean is_valid=false;



        System.out.println("\n How would you like to enter the trip?");
        System.out.println("1. Choose from predefined locations");
        System.out.println("2. Enter distance manually");
        System.out.print("Your choice: ");
        String choice = input.nextLine();

        if(Integer.parseInt(choice)==1){ //predefined locations

            Map<String, Location> predefinedLocations = Trip.getPredefinedLocations();
            List<String> locationNames = new ArrayList<>(predefinedLocations.keySet());

            System.out.println("Predefined locations: ");
            for(int i=0;i<predefinedLocations.size();i++){
                System.out.println((i+1)+" ->"+locationNames.get(i));
            }
            System.out.println("Please choose the starting location:");
            int startLocationIndex = Integer.parseInt(input.nextLine())-1;
            Location startLocation = Trip.getLocationByName(locationNames.get(startLocationIndex));

            System.out.println("Choose end location:");
            for (int i = 0; i < locationNames.size(); i++) {
                System.out.println((i + 1) + ". " + locationNames.get(i));
            }
            System.out.println("Please choose the starting location name:");
            int endLocationIndex = Integer.parseInt(input.nextLine())-1;
            Location endLocation = Trip.getLocationByName(locationNames.get(endLocationIndex));

            String description = "From "+locationNames.get(startLocationIndex)+" to "+locationNames.get(endLocationIndex); //otomatik description ataması
            double distanceKm = startLocation.distanceTo(endLocation); // aralarında kaç kilometre olduğunu döner.

            System.out.print("Enter average speed (km/h): ");
            double averageSpeed = Double.parseDouble(input.nextLine());

            long timeConsumed = startLocation.estimatedTravelTimeTo(endLocation,averageSpeed);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime end = now.plusMinutes(timeConsumed);

            trip = new Trip(now,end,description,startLocation,endLocation);
            is_valid=true;

        }
        else if (Integer.parseInt(choice)==2) { // manual

            System.out.println("Please enter the km you wish to travel: ");
            int km = Integer.parseInt(input.nextLine());

            System.out.print("Enter average speed (km/h): ");
            double averageSpeed = Double.parseDouble(input.nextLine());

            String description = "Manual trip - " + km + " km";

            double hours = km / averageSpeed;
            long timeConsumed =  Math.round(hours * 60); // dakikaya çevir

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime end = now.plusMinutes(timeConsumed);

            trip = new Trip(now,end,description,km);
            is_valid=true;
        }
        else{
            System.out.println("Invalid number!");
            is_valid=false;
        }
        if(is_valid){ //dosyaya kaydet

            EmissionService emissionService = new EmissionService();
            double co2 = emissionService.calculateEmission(trip.getDistance(), user.getVehicle());
            System.out.println("Estimated Emission: " + co2 + " grams of CO₂");
            tripService.saveTrip(trip);
        }
    }

    static void showTripHistory(){

        TripRepositoryInterface repo = new TxtTripRepository("trip.txt");
        TripService tripService = new TripService(repo);


        List<Trip> trips = tripService.getAllTrips(); // tripService ile repodan bütün tripleri list'e atarız.

        if (trips.isEmpty()) { // eğer boşsa geç
            System.out.println("\n No trip records found.");
            return;
        }
        System.out.println("\n Trip History:");
        for (Trip trip : trips) {
            String start = trip.getStart().toString();
            String end = trip.getEnd().toString();
            double distance = trip.getDistance();
            String desc = trip.getDescription() != null ? trip.getDescription() : "No description"; // nullsa no descript yaz değilse descripti yazdır.

            System.out.println(desc);
            System.out.println("    ➥ Start: " + start);
            System.out.println("    ➥ End:   " + end);
            System.out.println("    ➥ Distance: " + distance + " km\n");
        }

    }
}
