package application.service;

public interface PartObserver {
    void onMaintenanceDue(String partName); // bu OBSERVER design patterni ille araç parçalarının tamamının ne zaman bakım zamanı geleceğini bekliyor.
}
