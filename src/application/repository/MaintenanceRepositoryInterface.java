package application.repository;

import domain.maintenance.MaintenanceRecord;

import java.io.IOException;
import java.util.List;

public interface MaintenanceRepositoryInterface { // bu interface maintenance geçirmiş vehicle partları kaydetmek için oluşturulmuş bir interface.

    void addRecord(String ownerEmail, String partName, MaintenanceRecord record) throws IOException;

    List<MaintenanceRecord> getRecords(String ownerEmail,String partName) throws IOException;
}
