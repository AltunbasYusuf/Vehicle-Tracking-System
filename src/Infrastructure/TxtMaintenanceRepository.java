package Infrastructure;

import application.repository.MaintenanceRepositoryInterface;
import domain.maintenance.MaintenanceRecord;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TxtMaintenanceRepository implements MaintenanceRepositoryInterface {

    String filePath;

     public TxtMaintenanceRepository(String filePath){
         this.filePath=filePath;
     }

    @Override
    public void addRecord(String partName, MaintenanceRecord record) throws IOException {

         //yazma formatı: vehicle part ismi(örnek:motor,tire),maintenance description, bir sonraki önerilen bakım zamanı.
         try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))){
             writer.write(partName + "," +
                     record.getCurrentDate() + "," +
                     record.getMaintenanceDescription() + "," +
                     record.getNextSuggestedMaintenanceDate());
             writer.newLine();
         }
         catch (IOException e){
             System.out.println("Something went wrong while writing the Maintenance record!");
         }

    }

    @Override
    public List<MaintenanceRecord> getRecords(String partName) throws IOException {

         List<MaintenanceRecord> result = new ArrayList<>();
         try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){

             String line;
             while((line = reader.readLine())!=null){
                 String[] parts = line.trim().split(",");
                 if(parts.length<4){
                     System.out.println("This maintenance record has been written wrongly, skipping...");
                 }
                 else if(parts.length==4){

                     String pName = parts[0];
                     LocalDate currentDate = LocalDate.parse(parts[1]);
                     String description = parts[2];
                     LocalDate nextSuggestedDate = LocalDate.parse(parts[3]);

                     MaintenanceRecord r = new MaintenanceRecord(currentDate,description,nextSuggestedDate);
                 }
             }

         }
         catch (IOException e){
             System.out.println("Something went wrong while reading the Maintenance record file ! (txt)");
         }

         return result;
    }
}
