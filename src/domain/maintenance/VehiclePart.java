package domain.maintenance;

import Infrastructure.TxtMaintenanceRepository;
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
    static MaintenanceRepositoryInterface repo = new TxtMaintenanceRepository("maintenance.txt"); // bu parçanın eski maintenance geçmişini tutar.
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

    public void addMaintenanceRecord(MaintenanceRecord record) throws IOException {


        repo.addRecord(this.name,record);// yeni recordu liste ekliyoruz.
        this.nextMaintenanceDate = record.getNextSuggestedMaintenanceDate(); // yeni bakım tarihini güncelliyoruz.
    }

    public List<String> getMaintenanceDescriptions() {
        return maintenanceDescriptions;
    }

    public List<MaintenanceRecord> getMaintenanceHistory() {
        try {
            return repo.getRecords(this.getPartType());
        } catch (IOException e) {
            System.out.println("❗ Failed to read maintenance history from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}
