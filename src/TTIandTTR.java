import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class TTIandTTR {

    /*
     * TTR & TTI section
     * -------------------------------------------------------------------------------------------------------
     *      Tüm metotlardaki gerekli yükseklik ve hız parametreleri SI metrik sistemindeki birimlere göre ayarlanmıştır.
     *      Yükseklik : metre
     *      Hız : m/s
     */
    private HashMap<Integer,Double> airDensityTable;
    private final int EARTH_RADIUS = 6371;
    private final double DRAG_COEFFICIENT = 0.42;
    private final double GRAVITY = 9.81;
    private final double DIAMETER_OF_BOMB_METER = 0.16;
    private final int WEIGHT_OF_BOMB = 21;

    public TTIandTTR(){
        airDensityTable = new HashMap<>();
        airDensityTable.put(0,1.225);
        airDensityTable.put(1000,1.112);
        airDensityTable.put(2000,1.007);
        airDensityTable.put(3000,0.9093);
        airDensityTable.put(4000,0.8194);
        airDensityTable.put(5000,0.7364);
        airDensityTable.put(6000,0.6601);
        airDensityTable.put(7000,0.5900);
        airDensityTable.put(8000,0.5258);
        airDensityTable.put(9000,0.4671);
        airDensityTable.put(10000,0.4135);
        airDensityTable.put(15000,0.1948);
    }

    public void timeToImpact(double altitude, double velocity, double windSpeed, int lrfValue){
        double dt = 0.01;
        double displacement = 0;
        double time = 0;

        while(true){
            double velocityWithWind = velocity - windSpeed;
            double area = Math.PI * Math.pow((DIAMETER_OF_BOMB_METER / 2),2);
            double dragForce = calculateDragForce(altitude,velocityWithWind,area);
            double netForce =   (WEIGHT_OF_BOMB * GRAVITY) - dragForce;
            double acceleration = netForce / WEIGHT_OF_BOMB;

            velocity += acceleration * dt;

            displacement += velocity * dt * Math.sin(Math.toRadians(Math.toDegrees(Math.asin(altitude / lrfValue))));
            time += dt;
            if (displacement >= altitude) {
                break;
            }
        }
        startCountdown((int)time);
    }

    public int timeToRelease(double altitude, double velocity, Coorditanes location, Coorditanes target){
        double currDis = calculateCurrentDistance(location, target);
        double relDis = calculateReleaseDistance(altitude);

        double distance = currDis - relDis;
        double timeToRelease = distance / velocity;

        return (timeToRelease < 0) ? 0 : (int) timeToRelease;
    }

    //    Yardımcı metotlar
    private double calculateCurrentDistance(Coorditanes location, Coorditanes target){

        double locationLatitude = Math.toRadians(location.getLatitude());
        double locationLongitude = Math.toRadians(location.getLongitude());
        double targetLatitude = Math.toRadians(target.getLatitude());
        double targetLongitude = Math.toRadians(target.getLongitude());

        double x = (targetLongitude - locationLongitude) * Math.cos((locationLatitude + targetLatitude) / 2);
        double y = (targetLatitude - locationLatitude);

        return Math.sqrt(x * x + y * y) * EARTH_RADIUS * 1000;
    }

    private double getDensity(double altitude){
        double value = altitude / 1000;
        int key = (int)Math.round(value);

        if(key >= 13){
            key = 15;
        } else if(key >= 10){
            key = 10;
        }

        return airDensityTable.get(key*1000);
    }

    private double calculateReleaseDistance(double altitude){
        return Math.sqrt(Math.pow(altitude * 1.3, 2) - Math.pow(altitude,2));
    }

    private double calculateDragForce(double altitude ,double velocity ,double area){
        double airDensity = getDensity(altitude);
        return 0.5 * DRAG_COEFFICIENT * airDensity * area *  Math.pow(velocity,2);
    }

    private void startCountdown(int value){
        while (value > 0) {
            System.out.println(value--);
            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (Exception e) {

            }
        }
    }

    private double convertFeetToMeter(int altitudeFeet) {return altitudeFeet * 0.3048; /* 1 feet 0.3048 meter */}

    private double convertKnotToMps(int velocity_knots) { return velocity_knots * 1.85 * 0.277778; /* 1 knot 1.85 km/h and 1 km/h 0.277778 mp/s*/}

    static class Coorditanes{
        private double latitude;
        private double longitude;

        public Coorditanes(Double latitude, Double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Coorditanes(String coordinate){
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
}

