package domain.user;

//TODO: 1. MaintenanceRecord.java
//
//🔹 Konum: domain.maintenance
//
//LocalDate date
//
//String description
//
//LocalDate suggestedNextDate
//
//    Constructor ve getter’lar
//
//🛠️ 2. VehiclePart.java güncelle
//
//🔹 Konum: domain.maintenance
//
//List<MaintenanceRecord> maintenanceHistory alanını ekle
//
//void addMaintenanceRecord(MaintenanceRecord record) → listeye ekle + nextMaintenanceDate güncelle
//
//    List<MaintenanceRecord> getMaintenanceHistory() → geçmişi döndür
//
//📋 3. OwnerDashboard.java – Menü Sistemi
//
//🔹 Konum: presentation.owner
//Ana Menü Döngüsü:
//
//showVehicles(User user)
//
//    Kullanıcının araçlarını listele
//
//    Her aracın parçalarını ve bakım tarihlerini yazdır
//
//addMaintenance(User user)
//
//    Araç ve parça seçtir
//
//    Açıklama al
//
//    addMaintenanceRecord() çağır
//
//    “✅ Bakım kaydı oluşturuldu” mesajı bas
//
//    showMaintenanceHistory(User user)
//
//        Araç ve parça seçtir
//
//        getMaintenanceHistory() ile kayıtları yazdır
//
//💾 4. VehicleRepository.java
//
//🔹 Konum: application.repository
//
//    List<Vehicle> getVehiclesByOwner(String mail)
//
//        vehicle.txt dosyasından araçları oku
//
//        Sahip mail’i eşleşenleri filtrele
//
//    (İlk aşamada test için sahte List<Vehicle> döndürebilirsin)
//
//🧪 5. Demo Kullanıcılar ve Test
//
//1 adet VEHICLE_OWNER kullanıcı oluştur
//
//En az 1 araç oluştur ve ona parçalar ekle
//
//MaintenanceRecord test et (bakım kaydı oluştur, görüntüle)
//
//    System.out.println() açıklamalarıyla akışı net göster
//
//💬 6. HOCAYA AÇIKLAMA İÇİN
//
//    "Araç sahibi kullanıcı giriş yaptığında, sisteme kayıtlı kendi araçlarını ve parçalarını görebiliyor.
//    İstediği parçaya bakım kaydı ekleyebiliyor ve geçmişte yapılan bakımları detaylarıyla birlikte görüntüleyebiliyor.
//    Bu yapı tamamen kullanıcı rolüyle ilişkilendirilmiş ve OOP prensiplerine uygun olarak inşa edilmiştir."
//
//🎁 BONUS (Zorunlu Değil Ama HAVA ATILIK)
//
//MaintenanceRepository oluştur → bakım kayıtlarını dosyaya yaz/oku
//
//MaintenanceRecord.toString() → gösterim güzelleştir
//
//Parça seçimlerini input yerine menü sistemine dönüştür


public class VehicleOwner extends User {
    public VehicleOwner(String mail, String hashedPassword, String name, String surname) {
        super(mail, hashedPassword, name, surname, UserRole.VEHICLE_OWNER);
    }
}
