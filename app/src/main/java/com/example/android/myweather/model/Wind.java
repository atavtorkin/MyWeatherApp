package com.example.android.myweather.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by av.tavtorkin
 * On 17.02.2018.
 *Класс содержит данные о погоде
 */

public class Wind {

    @SerializedName("speed")
    public float speed;

    @SerializedName("deg")
    public float deg;
}
