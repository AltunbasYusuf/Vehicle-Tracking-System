package domain.maintenance;


import application.repository.MaintenanceRepositoryInterface;
import application.service.PartObserver;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class VehiclePart {
    protected String name;
    protected LocalDate nextMaintenanceDate;
    private List<PartObserver> observers = new ArrayList<>(); // OBSERVER DESIGN PATTERNİ İÇİN
    static MaintenanceRepositoryInterface repo = new Infrastructure.MySQLMaintenanceRepository();
   protected List<String> maintenanceDescriptions = new ArrayList<>(); // her vehicle partın kendine özel bir bakım açıklaması olabilir.


    public VehiclePart(String name) {
        this.name = name;
        this.nextMaintenanceDate = LocalDate.now().plusMonths(getDefaultMaintenanceInterval());
    }

    // Alt sınıflar bunu override etmek zorunda
    public abstract int getDefaultMaintenanceInterval();

    public String getName() {
        return name;
    }

    public LocalDate getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public abstract String getPartType();

    public void registerObserver(PartObserver observer) {
        observers.add(observer);
    }

    protected void notifyObservers() {
        for (PartObserver observer : observers) {
            observer.onMaintenanceDue(this.name);
        }
    }

    public void checkMaintenance() { // bu metod o parçanın bakım zamanı gelmi mi diye bakıyor gelmişse notif gönderiyor.
        if (!LocalDate.now().isBefore(this.nextMaintenanceDate)) {
            notifyObservers();
        }
    }

    public void addMaintenanceRecord(String ownerEmail, MaintenanceRecord record) throws IOException {
        repo.addRecord(ownerEmail, this.name, record); // ✅ email + partName + record
        this.nextMaintenanceDate = record.getNextSuggestedMaintenanceDate(); // ✅ bakım tarihini güncelle
    }


    public List<String> getMaintenanceDescriptions() {
        return maintenanceDescriptions;
    }

    public List<MaintenanceRecord> getMaintenanceHistory(String ownerEmail) {
        try {
            return repo.getRecords(ownerEmail, this.getPartType());
        } catch (IOException e) {
            System.out.println("❗ Failed to read maintenance history from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    public abstract Map<String, Object> getProperties();


}
