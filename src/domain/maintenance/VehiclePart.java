package domain.maintenance;

import application.service.PartObserver;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class VehiclePart {
    protected String name;
    protected LocalDate nextMaintenanceDate;
    private List<PartObserver> observers = new ArrayList<>(); // OBSERVER DESIGN PATTERNİ İÇİN

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

}
