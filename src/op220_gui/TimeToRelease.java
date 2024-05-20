package op220_gui;

public class TimeToRelease implements Runnable{

    private boolean threadCondition;
    private final int EARTH_RADIUS = 6371;
    private int timeToRelease = -1;
    private final Thread thread = new Thread(this);

    public void timeToRelease(){
        if(thread.isAlive()) {
            double altitude = GimbalConsts.instance.getAircraftAlt();
            double velocity = calculateVelocity(/* TODO gerekli */);
            Coordinates location = new Coordinates(GimbalConsts.instance.getAircraftCoordinates());
            Coordinates target = new Coordinates(GimbalConsts.instance.getAircraftCoordinates());

            double currDis = calculateCurrentDistance(location, target);
            double relDis = calculateReleaseDistance(altitude);

            double distance = currDis - relDis;
            double timeToRelease = distance / velocity;


            System.out.println("timeToRelease: " + timeToRelease);
        } else {
            thread.start();
        }

    }

    public void run(){
        threadCondition = true;

        while(threadCondition){
            timeToRelease();

            try {
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void stopCounter(){
        threadCondition = false;
    }

    public int getTimeToRelease() {
        return timeToRelease;
    }

    // Yardımcı metotlar

    private double calculateCurrentDistance(Coordinates location, Coordinates target){

        double locationLatitude = Math.toRadians(location.getLatitude());
        double locationLongitude = Math.toRadians(location.getLongitude());
        double targetLatitude = Math.toRadians(target.getLatitude());
        double targetLongitude = Math.toRadians(target.getLongitude());

        double x = (targetLongitude - locationLongitude) * Math.cos((locationLatitude + targetLatitude) / 2);
        double y = (targetLatitude - locationLatitude);

        return Math.sqrt(x * x + y * y) * EARTH_RADIUS * 1000;
    }

    private double calculateReleaseDistance(double altitude){
        return Math.sqrt(Math.pow(altitude * 1.3, 2) - Math.pow(altitude,2));
    }

    private double calculateVelocity(String velocityVectors){
        String[] velocityVectorsStrArr = velocityVectors.split("_");
        double[] velocityVectorsDoubleArr = new double[velocityVectorsStrArr.length];

        for (int i = 0; i < velocityVectorsStrArr.length; i++) {
            velocityVectorsDoubleArr[i] = Double.parseDouble(velocityVectorsStrArr[i]);
        }

        return Math.sqrt(Math.pow(velocityVectorsDoubleArr[0],2) + Math.pow(velocityVectorsDoubleArr[1],2));
    }
}
