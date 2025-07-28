
package presentation.owner;

import application.repository.TripRepositoryInterface;
import application.service.EmissionService;
import application.service.NotificationService;
import application.service.TripService;
import application.service.ElectricVehicleSuggestionService;
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
            System.out.println("1. Show vehicle parts conditions");
            System.out.println("2. Add a maintenance request");
            System.out.println("3. Show maintenance history");
            System.out.println("4. Add trip");
            System.out.println("5. Show trip history");
            System.out.println("6. Suggest an electric car similar to my car");
            System.out.println("0. Exit");
            System.out.print("Your Choice: ");
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    showParts(vehicle);
                    break;
                case "2":
                    addMaintenance(user.getMail(),vehicle);
                    break;
                case "3":
                    showMaintenanceHistory(user, vehicle);
                    break;
                case "4":
                    addTrip(user);
                    break;
                case "5":
                    showTripHistory(user);
                    break;
                case "6":
                    ElectricVehicleSuggestionService.electricVehicleSuggestion(user.getMail(),vehicle);
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

    private static void addMaintenance(String email,Vehicle vehicle) throws IOException {
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
        part.addMaintenanceRecord(email,record);

        System.out.println(" Maintenance request has been successfully made.");
    }


    private static void showMaintenanceHistory(User user,Vehicle vehicle) {
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
        List<MaintenanceRecord> history = part.getMaintenanceHistory(user.getMail());

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

        TripRepositoryInterface repo = new Infrastructure.MySQLTripRepository();
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
            double co2 = emissionService.calculateEmission(trip.getDistance(), user.getVehicle(),user.getVehicle().getFuelConsumption());
            double cost= emissionService.fuelCost(user.getVehicle(),trip.getDistance());
            System.out.printf("Estimated Emission: %.2f KG of CO₂",co2);
            System.out.printf("Fuel cost of this trip: %.2f USD",cost);
            tripService.saveTrip(user.getMail(), trip);
        }
    }

    static void showTripHistory(User user){

        TripRepositoryInterface repo = new Infrastructure.MySQLTripRepository();
        TripService tripService = new TripService(repo);


        List<Trip> trips = tripService.getAllTrips(user.getMail()); // tripService ile repodan bütün tripleri list'e atarız.

        if (trips.isEmpty()) { // eğer boşsa geç
            System.out.println("\n No trip records found.");
            return;
        }
        System.out.println("\n Trip History:");
        double totalCo2=0;
        double totalCost=0;
        double totalCostElectric=0;
        double totalCo2Electric=0;
        for (Trip trip : trips) {
            String start = trip.getStart().toString();
            String end = trip.getEnd().toString();
            double distance = trip.getDistance();
            String desc = trip.getDescription() != null ? trip.getDescription() : "No description"; // nullsa no descript yaz değilse descripti yazdır.
            EmissionService emissionService = new EmissionService();
            double co2 = emissionService.calculateEmission(trip.getDistance(), user.getVehicle(), user.getVehicle().getFuelConsumption());
            double cost = emissionService.fuelCost(user.getVehicle(),trip.getDistance());
            if (!user.getVehicle().getFueltype().equalsIgnoreCase("electric")) {
                double electricCost = ElectricVehicleSuggestionService.calculateCloseElectricCost(user.getVehicle(),trip.getDistance());
                totalCostElectric += electricCost;
                double electricCo2= ElectricVehicleSuggestionService.calculateCloseElectricCO2(user.getVehicle(),trip.getDistance());
                totalCo2Electric+=electricCo2;
            }
            totalCo2+=co2;
            totalCost+=cost;



            System.out.println(desc);
            System.out.println("    ➥ Start: " + start);
            System.out.println("    ➥ End:   " + end);
            System.out.println("    ➥ Distance: " + distance + " KM");
            System.out.printf("    ➥ CO2 Released: %.2f KG\n",co2);
        }
           double costDifference=totalCost-totalCostElectric;
           double co2Difference=totalCo2-totalCo2Electric;
           System.out.printf("Total CO2 released: %.2f  KG\n",totalCo2);
           System.out.printf("Total fuel cost: %.2f USD\n", totalCost);
        if (!user.getVehicle().getFueltype().equalsIgnoreCase("electric")){
            System.out.printf(
                    "If you prefer our electric car suggestion you pay %.2f USD less and your car release %.2f KG CO2 less.\n", costDifference, co2Difference);
           }
    }
}
