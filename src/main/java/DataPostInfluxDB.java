public class DataPostInfluxDB {

    String zone;
    String geohash;

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public DataPostInfluxDB(String zone, String geo){
        this.geohash=geo;
        this.zone=zone;
    }
    public DataPostInfluxDB(){}

}
