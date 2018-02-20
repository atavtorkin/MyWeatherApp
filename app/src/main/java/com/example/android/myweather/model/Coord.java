package com.example.android.myweather.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by av.tavtorkin
 * On 17.02.2018.
 * Класс содержит поля координат долготы и широты города.
 */
public class Coord {

    /**
     *lat координата широты
     */
    @SerializedName("lat")
    public float lat;

    /**
     *lon координата долготы
     */
    @SerializedName("lon")
    public float lon;
}
