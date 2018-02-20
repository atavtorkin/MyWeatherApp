package com.example.android.myweather.api;

import com.example.android.myweather.model.CityResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by av.tavtorkin
 * On 17.02.2018.
 * Интерфейс, определяющий возможные HTTP Операции (API)
 */
public interface OpenWeatherMapApi {

    /**
     * GET, который к базовому URL добавит /data/2.5/weather?q=”первый параметр”&appid=”второй параметр”.
     * @param cityName параметр запроса, название города
     * @param appId параметр авторизации, ключ API
     * @return Возвращаемое значение оборачивается в ответе в Call объект, параметризованный типом CityResponse
     */
    @GET("/data/2.5/weather/?units=metric")
    Call<CityResponse> getByCityName(@Query("q") String cityName, @Query("appid") String appId);

    @GET("/data/2.5/weather/?units=metric")
    Call<CityResponse> getByLocation(@Query("lon") float lon, @Query("lat") float lat, @Query("appid") String appId);

}
