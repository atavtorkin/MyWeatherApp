package com.example.android.myweather.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by av.tavtorkin
 * On 17.02.2018.
 * Класс-модель,
 * отражающий данные(координаты,температуру, эконку погоды и т.д),
 * которые будут приходить по сети.
 */
public class CityResponse {

    @SerializedName("coord")
    private Coord coord;
    @SerializedName("weather")
    private ArrayList<Weather> weather;
    @SerializedName("base")
    private String  base;
    @SerializedName("main")
    private Main main;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("dt")
    private int dt;
    @SerializedName("sys")
    private Sys sys;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("cod")
    private int cod;

    /**
     *
     * Метод вовращает координаты города
     * lat - широта
     * lon - долгода
     */
    public Coord getCoord() {
        return coord;
    }

    /**
     *
     * Метод возращает список,
     * содержащий больше дополнительной информации о погоде,
     * используем icon(id эконки погоды)
     */
    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public int getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }
}
