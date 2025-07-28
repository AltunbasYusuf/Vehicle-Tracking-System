package domain.maintenance;

import java.time.LocalDate;

public class MaintenanceRecord {


    private String maintenanceDescription;
    private LocalDate currentDate;
    private LocalDate nextSuggestedMaintenanceDate;


    //kullanıcıdan yeni bir kayıt geldiğinde
    public MaintenanceRecord(String mDescription ,VehiclePart vehiclePart){

        currentDate = LocalDate.now();
        nextSuggestedMaintenanceDate = currentDate.plusMonths(vehiclePart.getDefaultMaintenanceInterval());
        maintenanceDescription = mDescription;

    }

    // Dosyadan okurken
    public MaintenanceRecord(LocalDate date, String description, LocalDate nextDate) {
        this.currentDate = date;
        this.maintenanceDescription = description;
        this.nextSuggestedMaintenanceDate = nextDate;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public String getMaintenanceDescription() {
        return maintenanceDescription;
    }

    public LocalDate getNextSuggestedMaintenanceDate() {
        return nextSuggestedMaintenanceDate;
    }

    @Override
    public String toString() {
        return currentDate + ": " + maintenanceDescription+ " | Next: " + nextSuggestedMaintenanceDate;
    }
}


