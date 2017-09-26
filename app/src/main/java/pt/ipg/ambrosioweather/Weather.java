package pt.ipg.ambrosioweather;

/**
 * Created by RT on 16/01/2017.
 */

public class Weather {

    String city;
    double temperature;

    public Weather() {

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
