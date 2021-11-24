package com.example.lab3levykin;

public class Forecast {
    public int id;
    public String date;
    public  String city;
    public  float temp;
    public  float wind;
    public  float pressure;
    public  float precipitation;
    public  int hum;
    public  int cloud;

    public String toString()
    {
        return id + "  " + date + "  " + city
                + "\nТемпература: " + temp + " °C" + "    Ветер: " + wind + " км/ч"
                + "\nДавление: " + pressure + " мбар" + "    Осадки: " + precipitation + " мм"
                + "\nВлажность: " + hum + " %" + "    Облака: " + cloud + " %";
    }
}
