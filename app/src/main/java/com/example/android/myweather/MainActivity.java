package com.example.android.myweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myweather.app.MyWeatherApplication;
import com.example.android.myweather.db.CityWeather;
import com.example.android.myweather.location.LocationLiveData;
import com.example.android.myweather.model.CityResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final String IMAGE_FORMAT = "http://openweathermap.org/img/w/%s.png";

    private ImageView iconView;
    private TextView temperatureView;
    private TextView cityNameView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconView = findViewById(R.id.image);
        temperatureView = findViewById(R.id.temperature);
        cityNameView = findViewById(R.id.cityName);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("TEST", "swipeRefreshLayout working");

                getWeatherFromServer(cityName);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });


        List<CityWeather> list = MyWeatherApplication.getDb().getCityWeatherDao().getAllCityWeather();
        if (list != null && !list.isEmpty()) {
            for (CityWeather weather : list) {
                Log.d("TEST", "Has weather for city = " + weather.getCityName());
            }
        } else {
            Log.d("TEST", "City weather list is empty");
        }

        if (checkLocationPermissions()) {
            Log.d("TEST", "Permissions is granted, just get user location");
            getUserLocation();
        } else {
            Log.d("TEST", "No permissions, ask user");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            Log.d("TEST", "Permissions request result");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TEST", "Location permissions is granted");
                getUserLocation();
            } else {
                cityNameView.setText("Нет разрешений на получение местоположения пользователя.");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Проверям, получили ли мы данные о метоположении,если получили
     * вызываем метод getWeather();
     */
    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        Log.d("TEST", "Try to get last known location");
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d("TEST", "We have last location, get weather");
                    getWeather(location);
                } else {
                    Log.d("TEST", "We have not last location, try to listen location service");
                    new LocationLiveData(MainActivity.this).observe(MainActivity.this, new Observer<Location>() {
                        @Override
                        public void onChanged(@Nullable Location location) {
                            if (location != null) {
                                getWeather(location);
                            } else {
                                Log.d("TEST", "Received null location");
                            }
                        }
                    });
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TEST", "Get last location error", e);
            }
        });
    }

    /**
     * Метод с помощью геокодера определяет имя города,
     * затем вычисляем  когда последний раз был запрос погоды(больше или меньше 3 часов),
     * если больше, то делаем запрос с сервера,меньше подгружаем данные с базы данных
     * @param location объект типа Location который содержит данные о местоположении.
     */
    private void getWeather(Location location) {
        Log.d("TEST", "Received location: lat - " + location.getLatitude() + ", lon - " + location.getLongitude());
        try {
            Geocoder gc = new Geocoder(this);
            List<Address> list = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (list != null && !list.isEmpty()) {
                cityName = list.get(0).getLocality();

                List<CityWeather> weatherList = MyWeatherApplication.getDb().getCityWeatherDao().getCityWeather(cityName);
                if (weatherList != null && !weatherList.isEmpty()) {
                    CityWeather weather = weatherList.get(0);
                    if (System.currentTimeMillis() - weather.getLastUpdate() > 1000 * 60 * 60 * 3) {// 3 hours
                        getWeatherFromServer(cityName);
                    } else {
                        Log.d("TEST", "Show weather from db");
                        showWeather(weather.getCityName(), weather.getTemp(), weather.getIcon());
                    }
                } else {
                    getWeatherFromServer(cityName);
                }
            } else {
                Log.d("TEST", "Geocoder unknown city name");
            }
        } catch (Exception e) {
            Log.e("TEST", "Geocoder error", e);
        }
    }

    /**
     * Получение температу и эконки с сервера
     * @param cityName название города
     */
    private void getWeatherFromServer(final String cityName) {
        Log.d("TEST", "Get weather from server");
        MyWeatherApplication.getApi().getByCityName(cityName, MyWeatherApplication.APP_ID)
                .enqueue(new Callback<CityResponse>() {
                    @Override
                    public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                        saveCityWeather(cityName, response.body());
                        showWeather(cityName,
                                response.body().getMain().temp,
                                response.body().getWeather().get(0).icon);
                    }

                    @Override
                    public void onFailure(Call<CityResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Get weather problem", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Метод устаналивает текущую погода, эконку и название города.
     * @param cityName название города
     * @param temp текущая температура
     * @param icon иконка погоды
     */
    private void showWeather(String cityName, double temp, String icon) {
        Picasso.with(MainActivity.this)
                .load(String.format(Locale.getDefault(), IMAGE_FORMAT, icon))
                .into(iconView);
        temperatureView.setText(String.format(Locale.getDefault(), "%.1fºC", temp));
        cityNameView.setText(cityName);
    }

    /**
     * Сохранение данных о погоде в базу данных
     * @param cityName название города
     * @param response класс модель данных о погоде
     */
    private void saveCityWeather(String cityName, CityResponse response) {
        Log.d("TEST", "Save new weather data to db");

        CityWeather weather = new CityWeather();
        weather.setCityName(cityName);
        weather.setIcon(response.getWeather().get(0).icon);
        weather.setTemp(response.getMain().temp);
        weather.setLat(response.getCoord().lat);
        weather.setLon(response.getCoord().lon);
        weather.setLastUpdate(System.currentTimeMillis());

        MyWeatherApplication.getDb().getCityWeatherDao().insertWeather(weather);
    }

    /**
     * проверка на разрешение определения координат
     * @return возвращаем true если разрешения включены
     */
    private boolean checkLocationPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

}
