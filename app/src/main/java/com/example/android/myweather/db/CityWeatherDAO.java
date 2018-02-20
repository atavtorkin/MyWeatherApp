package com.example.android.myweather.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by av.tavtorkin
 * On 18.02.2018.
 * Интерфейс DAO для класса CityWeather
 */

@Dao
public interface CityWeatherDAO {

    // Добавление данных о погоде в бд
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeather(CityWeather weather);

    // Получение данных о погоде из бд с условием
    @Query("SELECT * FROM cityWeather t WHERE t.cityName = :cityName")
    List<CityWeather> getCityWeather(String cityName);

    // Получение всех данных о погоде из бд
    @Query("SELECT * FROM cityweather")
    List<CityWeather> getAllCityWeather();

}
