package com.example.android.myweather.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by av.tavtorkin
 * On 17.02.2018.
 *Класс содержит данные о погоде
 */

public class Sys {

    @SerializedName("message")
    public float message;

    @SerializedName("country")
    public String country;

    @SerializedName("sunrise")
    public int sunrise;

    @SerializedName("sunset")
    public int sunset;
}
