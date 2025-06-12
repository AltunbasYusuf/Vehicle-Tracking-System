package presentation.setup;

import Infrastructure.TxtVehicleRepository;
import application.repository.SellerVehicleRepositoryInterface;
import application.service.VehicleService;
import domain.maintenance.*;
import domain.vehicle.Vehicle;

import java.util.*;

public class Quick_Setup {
    private static final Scanner input = new Scanner(System.in);

    public static Vehicle quickSetup() {
        System.out.println("Welcome to Quick Setup! Please choose the vehicle:\n1. Car\n2. Motorcycle\n3. Truck");
        String vehicleType = VehicleType();

        List<VehiclePart> parts = new ArrayList<>();

        System.out.print("Enter vehicle brand: ");
        String brand = input.nextLine();
        System.out.print("Enter vehicle model: ");
        String model = input.nextLine();


        // 1. Motor
        System.out.println("Enter motor type (diesel, electric, hybrid):");
        String motorType = input.nextLine();
        System.out.println("Enter horsepower:");
        double horsePower = Double.parseDouble(input.nextLine());
        System.out.println("Enter fuel consumption (L/100km or kWh/100km):");
        double fuelConsumption = Double.parseDouble(input.nextLine());
        System.out.println("Enter CO2 emission (g/km):");
        double co2Emission = Double.parseDouble(input.nextLine());

        Map<String, Object> motorParams = new HashMap<>();
        motorParams.put("motorType", motorType);
        motorParams.put("horsePower", horsePower);
        motorParams.put("fuelConsumption", fuelConsumption);
        motorParams.put("co2Emission", co2Emission);

        VehiclePart motor = PartFactory.createPart("motor", motorParams);
        parts.add(motor);

        // 2. Tire
        System.out.println("Enter tire season type (summer, winter):");
        String seasonType = input.nextLine();
        System.out.println("Enter tread depth (mm):");
        double treadDepth = Double.parseDouble(input.nextLine());

        Map<String, Object> tireParams = new HashMap<>();
        tireParams.put("seasonType", seasonType);
        tireParams.put("treadDepth", treadDepth);

        VehiclePart tire = PartFactory.createPart("tire", tireParams);
        parts.add(tire);

        // 3. Battery
        /*
        System.out.println("Enter battery capacity (Ah):");
        int capacityAh = Integer.parseInt(input.nextLine());

        Map<String, Object> batteryParams = new HashMap<>();
        batteryParams.put("capacityAh", capacityAh);

        VehiclePart battery = PartFactory.createPart("battery", batteryParams);
        parts.add(battery);
        */

        // 4. Brake System
        System.out.println("Enter brake pad wear level (%):");
        double padWear = Double.parseDouble(input.nextLine());
        System.out.println("Does the vehicle have ABS? (true/false):");
        boolean absEnabled = Boolean.parseBoolean(input.nextLine());

        Map<String, Object> brakeParams = new HashMap<>();
        brakeParams.put("padWearLevel", padWear);
        brakeParams.put("absEnabled", absEnabled);

        VehiclePart brake = PartFactory.createPart("brake", brakeParams);
        parts.add(brake);

        // 5. Cooling System
        System.out.println("Enter antifreeze level (liters):");
        double antifreeze = Double.parseDouble(input.nextLine());
        System.out.println("Enter minimum required antifreeze level:");
        double minLevel = Double.parseDouble(input.nextLine());

        Map<String, Object> coolingParams = new HashMap<>();
        coolingParams.put("antifreezeLevel", antifreeze);
        coolingParams.put("minLevel", minLevel);

        VehiclePart cooling = PartFactory.createPart("cooling_system", coolingParams);
        parts.add(cooling);

        // Vehicle oluştur
        Vehicle vehicle = new Vehicle(vehicleType,brand,model);
        for (VehiclePart part : parts) { // oluşturlan parçalar burada eklenir.
            vehicle.addPart(part);
        }

        SellerVehicleRepositoryInterface repo = new TxtVehicleRepository("seller_vehicle_system.txt");
        VehicleService service = new VehicleService(repo);
        System.out.println("Quick setup complete. Vehicle and parts initialized.");

        //!!!! sellerin şu özellikli şu isimli şu aracım var dediği ve kaydettiği txt dosyassı.
        service.addVehicle(vehicle);
        System.out.println("Vehicle has been registered to the system.");

        return vehicle;

    }

    public static String VehicleType() {
        String vehicleType = input.nextLine();
        if (vehicleType.equalsIgnoreCase("car") || vehicleType.equals("1")) {
            return "Car";
        } else if (vehicleType.equalsIgnoreCase("motorcycle") || vehicleType.equals("2")) {
            return "Motorcycle";
        } else if (vehicleType.equalsIgnoreCase("truck") || vehicleType.equals("3")) {
            return "Truck";
        }
        return "Unknown";
    }
}
