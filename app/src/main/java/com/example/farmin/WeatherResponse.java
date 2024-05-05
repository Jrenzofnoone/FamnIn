package com.example.farmin;

import java.util.List;

public class WeatherResponse {
    private Main main ;
    private Wind wind;
    private Clouds clouds;
    private int visibility;
    private long sunrise;
    private long sunset;
    private List<Weather> weather;

    public WeatherResponse(Main main, Wind wind, Clouds clouds, int visibility, long sunrise, long sunset, List<Weather> weather) {
        this.main = main;
        this.wind = wind;
        this.clouds = clouds;
        this.visibility = visibility;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public class Main {
        private double temp;
        private double humidity;
        private double pressure;

        public Main(double temp, double humidity, double pressure) {
            this.temp = temp;
            this.humidity = humidity;
            this.pressure = pressure;
        }

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }

        public double getPressure() {
            return pressure;
        }

        public void setPressure(double pressure) {
            this.pressure = pressure;
        }
    }
    public class Wind {
        private double speed;
        private double deg;

        public Wind(double speed, double deg) {
            this.speed = speed;
            this.deg = deg;
        }

        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public double getDeg() {
            return deg;
        }

        public void setDeg(double deg) {
            this.deg = deg;
        }
    }
    public class Clouds {
        private int all;

        public Clouds(int all) {
            this.all = all;
        }

        public int getAll() {
            return all;
        }

        public void setAll(int all) {
            this.all = all;
        }
    }
    public class Weather {
        private int id;
        private String description;
        private String main;
        private String icon;

        public Weather(int id, String description, String main, String icon) {
            this.id = id;
            this.description = description;
            this.main = main;
            this.icon = icon;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}

