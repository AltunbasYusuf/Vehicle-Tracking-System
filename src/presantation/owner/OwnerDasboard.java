
//todo: maintanence record classını yaz.

package presantation.owner;

import application.service.NotificationService;
import domain.maintenance.MaintenanceRecord;
import domain.maintenance.VehiclePart;
import domain.user.User;
import domain.vehicle.Vehicle;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class OwnerDashboard {

    private static final Scanner input = new Scanner(System.in);

    public static void open(User user) {
        Vehicle vehicle = user.getVehicle(); // sistemde sadece 1 araç var

        while (true) {
            System.out.println("\n📋 Owner Dashboard - " + user.getName());
            System.out.println("Araç Tipi: " + vehicle.getVehicleType());
            System.out.println("1. Show vehicle parts conditions");
            System.out.println("2. Add a maintenance request");
            System.out.println("3. Show maintenance history");
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

    private static void addMaintenance(Vehicle vehicle) {
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
        System.out.print("Description of maintenance: ");
        String desc = input.nextLine();

        MaintenanceRecord record = new MaintenanceRecord(LocalDate.now(), desc, LocalDate.now().plusMonths(6));
        part.addMaintenanceRecord(record);

        System.out.println("✅ Maintenance request has been successfully made.");
    }

    private static void showMaintenanceHistory(Vehicle vehicle) {
        List<VehiclePart> parts = vehicle.getParts();

        System.out.println("\n📜 Choose the part you want to see the maintenance history:");
        for (int i = 0; i < parts.size(); i++) {
            System.out.println((i + 1) + ". " + parts.get(i).getPartType());
        }

        System.out.print("Seçim: ");
        int index = Integer.parseInt(input.nextLine()) - 1;
        if (index < 0 || index >= parts.size()) {
            System.out.println("❗Invalid choice.");
            return;
        }

        VehiclePart part = parts.get(index);
        List<MaintenanceRecord> history = part.getMaintenanceHistory();

        if (history.isEmpty()) {
            System.out.println("🚫 No maintenance record yet.");
        } else {
            System.out.println("\n📚 " + part.getPartType() + " maintenance history:");
            for (MaintenanceRecord r : history) {
                System.out.println(" - " + r.getDate() + ": " + r.getDescription() + " | Next maintenance: " + r.getSuggestedNextDate());
            }
        }
    }
}
