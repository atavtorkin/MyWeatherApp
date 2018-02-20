package com.example.android.myweather.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by av.tavtorkin
 * On 17.02.2018.
 *Класс содержит данные о погоде
 */

public class Main {

    @SerializedName("temp")
    public float temp;

    @SerializedName("pressure")
    public float pressure;

    @SerializedName("humidity")
    public int humidity;

    @SerializedName("temp_min")
    public float temp_min;

    @SerializedName("temp_max")
    public float temp_max;

    @SerializedName("sea_level")
    public float sea_level;

    @SerializedName("grnd_level")
    public float grnd_level;

}
