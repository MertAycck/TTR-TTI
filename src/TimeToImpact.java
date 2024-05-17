import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class TimeToImpact implements Runnable{

    /*
     * TTI section
     * -------------------------------------------------------------------------------------------------------
     *      Tüm metotlardaki gerekli yükseklik ve hız parametreleri SI metrik sistemindeki birimlere göre ayarlanmıştır.
     *      Yükseklik : metre
     *      Hız : m/s
     */

    private HashMap<Integer,Double> airDensityTable;
    private final double DRAG_COEFFICIENT = 0.42;
    private final double GRAVITY = 9.81;
    private final double DIAMETER_OF_BOMB_METER = 0.16;
    private final int WEIGHT_OF_BOMB = 21;
    private double timeToImpact;

    public TimeToImpact(){
        timeToImpact = -1;
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

        while(true){
            double velocityWithWind = velocity - windSpeed;
            double area = Math.PI * Math.pow((DIAMETER_OF_BOMB_METER / 2),2);
            double dragForce = calculateDragForce(altitude,velocityWithWind,area);
            double netForce =   (WEIGHT_OF_BOMB * GRAVITY) - dragForce;
            double acceleration = netForce / WEIGHT_OF_BOMB;

            velocity += acceleration * dt;

            displacement += velocity * dt * Math.sin(Math.toRadians(Math.toDegrees(Math.asin(altitude / lrfValue))));
            timeToImpact += dt;
            if (displacement >= altitude) {
                break;
            }
        }

        Thread thread = new Thread(this);
        thread.start();
    }

    public void run(){
        while (timeToImpact > 0) {
            System.out.println((int)timeToImpact--);
            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch (Exception e) {

            }
        }
    }

    //    Yardımcı metotlar

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

    private double calculateDragForce(double altitude ,double velocity ,double area){
        double airDensity = getDensity(altitude);
        return 0.5 * DRAG_COEFFICIENT * airDensity * area *  Math.pow(velocity,2);
    }
}

