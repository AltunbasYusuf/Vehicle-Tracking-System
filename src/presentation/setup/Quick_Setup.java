package presentation.setup;

import domain.maintenance.*;
import domain.vehicle.Vehicle;

import java.util.*;

public class Quick_Setup {
    private static final Scanner input = new Scanner(System.in);

    public static Vehicle quickSetup() {
        System.out.println("Welcome to Quick Setup! Please introduce your vehicle to the program with quick setup.");

        List<VehiclePart> parts = new ArrayList<>();

        System.out.print("Enter vehicle brand: ");
        String brand = input.nextLine();
        System.out.print("Enter vehicle model: ");
        String model = input.nextLine();
        System.out.println("Enter vehicle segment (HATCHBACK, SEDAN, SUV, PICKUP, VAN)");
        String segment = VehicleSegment();

        // 1. Motor
        System.out.println("Enter fuel type (gasoline, diesel, electric, LPG):");
        String motorType = motorType();
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

        // 3. Brake System
        System.out.println("Enter brake pad wear level (%):");
        double padWear = Double.parseDouble(input.nextLine());
        System.out.println("Does the vehicle have ABS? (true/false):");
        boolean absEnabled = Boolean.parseBoolean(input.nextLine());

        Map<String, Object> brakeParams = new HashMap<>();
        brakeParams.put("padWearLevel", padWear);
        brakeParams.put("absEnabled", absEnabled);

        VehiclePart brake = PartFactory.createPart("brake system", brakeParams);
        parts.add(brake);

        // 4. Cooling System
        System.out.println("Enter antifreeze level (liters):");
        double antifreeze = Double.parseDouble(input.nextLine());
        System.out.println("Enter minimum required antifreeze level:");
        double minLevel = Double.parseDouble(input.nextLine());

        Map<String, Object> coolingParams = new HashMap<>();
        coolingParams.put("antifreezeLevel", antifreeze);
        coolingParams.put("minLevel", minLevel);

        VehiclePart cooling = PartFactory.createPart("cooling system", coolingParams);
        parts.add(cooling);

        // Vehicle oluştur
        Vehicle vehicle = new Vehicle(brand, model, segment);
        for (VehiclePart part : parts) { // oluşturlan parçalar burada eklenir.
            vehicle.addPart(part);
        }

        vehicle.setFuelType(motorType);
        vehicle.setHP(horsePower);
        vehicle.setFuelConsumption(fuelConsumption);

        System.out.println("Quick setup complete. Vehicle object created.");

        // Bu kısımdaki veritabanına kaydetme kodları tamamen kaldırıldı.
        // Bu metodun görevi sadece araç nesnesini oluşturup döndürmektir.
        return vehicle;
    }

    public static String VehicleSegment() {
        String vehicleSegment = input.nextLine();
        if (vehicleSegment.equalsIgnoreCase("suv")) {
            return "SUV";
        } else if (vehicleSegment.equalsIgnoreCase("sedan")) {
            return "SEDAN";
        } else if (vehicleSegment.equalsIgnoreCase("hatchback")) {
            return "HATCHBACK";
        } else if (vehicleSegment.equalsIgnoreCase("pickup") || vehicleSegment.equalsIgnoreCase("PICKUP")) {
            return "PICKUP";
        } else if (vehicleSegment.equalsIgnoreCase("van")) {
            return "VAN";
        } else {
            System.out.println("Undefined segment, please specify an existing segment (SUV,SEDAN,HATCHBACK,PICKUP,VAN)");
            return VehicleSegment();
        }
    }

    public static String motorType() {
        String motorType = input.nextLine();
        if (motorType.equalsIgnoreCase("gasoline") || motorType.equalsIgnoreCase("gasolıne")) {
            return "gasoline";
        } else if (motorType.equalsIgnoreCase("diesel") || motorType.equalsIgnoreCase("dıesel")) {
            return "diesel";
        } else if (motorType.equalsIgnoreCase("electric") || motorType.equalsIgnoreCase("electrıc")) {
            return "electric";
        } else if (motorType.equalsIgnoreCase("lpg")) {
            return "LPG";
        } else {
            System.out.println("Undefined fuel type, please specify an existing fuel type (gasoline, dieseL, electric, LPG");
            return motorType();
        }
    }
}