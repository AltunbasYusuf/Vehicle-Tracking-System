package application.service;

public class NotificationService implements PartObserver {

    private static NotificationService instance;

    private NotificationService() {}

    public static NotificationService getInstance() { // SINGLETONNNNNN
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    @Override
    public void onMaintenanceDue(String partName) {
        System.out.println("[NOTIFICATION] " + partName.toUpperCase() + " için bakım zamanı geldi!");
    }
}
