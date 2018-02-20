package com.example.android.myweather.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by av.tavtorkin
 * On 17.02.2018.
 * Дополнительная информация о погоде.
 */

public class Weather {

    @SerializedName("id")
    public int id;

    @SerializedName("main")
    public String main;

    @SerializedName("description")
    public String description;

    /**
     * id картинки погоды
     */
    @SerializedName("icon")
    public String icon;

}
