package com.example.android.myweather.app;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.android.myweather.api.OpenWeatherMapApi;
import com.example.android.myweather.db.WeatherDatabase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by av.tavtorkin
 * On 17.02.2018.
 * Создаем объекты типа Retrofit, WeatherDatabase
 */

public class MyWeatherApplication extends Application {

    /**
     * Ключ AP, полученный с ресурса openweathermap.org
     */
    public static final String APP_ID = "9211e0f7057eaa05a2986ce323fabeb5";

    private static OpenWeatherMapApi api;
    private Retrofit retrofit;

    private static WeatherDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        api = retrofit.create(OpenWeatherMapApi.class); //Создаем объект, при помощи которого будем выполнять запросы

        db = Room.databaseBuilder(getApplicationContext(), WeatherDatabase.class, "WeatherDatabase")
                .allowMainThreadQueries()
                .build();
    }

    /**
     * @return Возвращает обЪект, при помощи которого будем выполнять запросы
     * */
    public static OpenWeatherMapApi getApi() {
        return api;
    }
    /**
     * @return Возвращает обЪект, при помощи которого будем выполнять запись данных
     * */
    public static WeatherDatabase getDb() {
        return db;
    }



}
