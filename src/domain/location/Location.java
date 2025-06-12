package domain.location;


public class Location {
    private double latitude;   // Enlem
    private double longitude;  // Boylam

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("Lat: %.6f, Lon: %.6f", latitude, longitude);
    }

    /**
     * Haversine formülüyle iki nokta arasındaki mesafeyi kilometre cinsinden hesaplar.
     */
    public double distanceTo(Location other) {
        final int EARTH_RADIUS_KM = 6371;

        double lat1Rad = Math.toRadians(this.latitude);
        double lat2Rad = Math.toRadians(other.latitude);
        double deltaLat = Math.toRadians(other.latitude - this.latitude);
        double deltaLon = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    //bu fonksiyon iki location arası ortalama bir hızla gidildiğinde ne kadar zaman alacağını hesaplar.
    public long estimatedTravelTimeTo(Location other, double averageSpeedKmph) {
        double distance = this.distanceTo(other); // km cinsinden
        double hours = distance / averageSpeedKmph;
        return Math.round(hours * 60); // dakikaya çevir
    }


}
