public class Coordinates {
    private double latitude;
    private double longitude;

    public Coordinates(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates(String coordinate){
        String[] coordinates = coordinate.split(", ");
        this.latitude = Double.parseDouble(coordinates[0]);
        this.longitude = Double.parseDouble(coordinates[1]);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
