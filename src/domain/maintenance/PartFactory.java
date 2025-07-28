package domain.maintenance;

import java.util.Map;

public class PartFactory {

    public static VehiclePart createPart(String type, Map<String, Object> parameters) {
        switch (type.toLowerCase()) {

            case "motor":
                return new Motor(
                        (String) parameters.get("motorType"),
                        (double) parameters.get("horsePower"),
                        (double) parameters.get("fuelConsumption")
                );

            case "tire":
                return new Tire(
                        (String) parameters.get("seasonType"),
                        (double) parameters.get("treadDepth")
                );

            case "brake system":
                return new Brake(
                        (double) parameters.get("padWearLevel"),
                        (boolean) parameters.get("absEnabled")
                );

            case "cooling system":
                return new CoolingSystem(
                        (double) parameters.get("antifreezeLevel"),
                        (double) parameters.get("minLevel")
                );

            default:
                throw new IllegalArgumentException("Unknown part type: " + type);
        }
    }
}
