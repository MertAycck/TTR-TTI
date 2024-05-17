package op220_gui;

public class Metric{

    public static double convertFT_M(double altitude) { return altitude * 0.3048; }
    public static double convertFT_KM(double altitude){ return altitude * 0.0003048; }
    public static double convertM_FT(double altitude){ return altitude / 0.3048; }
    public static double convertM_KM(double altitude){ return altitude / 1000; }
    public static double convertKM_M(double altitude){ return altitude * 1000; }
    public static double convertKM_FT(double altitude){ return altitude / 0.0003048; }

    public static double convertKN_Mps(double velocity) { return velocity * 0.51444;}
    public static double convertKN_KMH(double velocity){ return velocity * 1.85; }
    public static double convertMPS_KN(double velocity){ return velocity / 0.51444; }
    public static double convertMPS_KMH(double velocity){ return velocity / 0.277778; }
    public static double convertKMH_MPS(double velocity){ return velocity * 0.277778; }
    public static double convertKMH_KN(double velocity){ return velocity / 1.85; }

    public static double convertCelcius_Kelvin(double temperature){ return temperature * 273.15; }
    public static double convertKelvin_Celcius(double temperature){ return temperature / 273.15; }
}
