package com.example.android.myweather.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by av.tavtorkin
 * On 18.02.2018.
 * Описание структуры базы данных
 */
@Database(entities = {CityWeather.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {

    public abstract CityWeatherDAO getCityWeatherDao();

}
