package com.example.farmin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Fragmenthome extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvTemp, tvDescrip, tvCloudiness, tvPressure, tvHumidity, tvWind;
    private WeatherApiService service;
    private static final String baseUrl = "https://api.openweathermap.org/data/2.5/";
    private static final int locationCode = 1001;
    private ImageView ivCloud;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_fragmenthome, container, false);
        ivCloud = rootView.findViewById(R.id.ivCloud);
        tvTemp = rootView.findViewById(R.id.tvTemp);
        tvDescrip = rootView.findViewById(R.id.tvDescrip);
        tvCloudiness = rootView.findViewById(R.id.tvCloudiness);
        tvPressure = rootView.findViewById(R.id.tvPressure);
        tvHumidity = rootView.findViewById(R.id.tvHumidity);
        tvWind = rootView.findViewById(R.id.tvWind);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        requestLocationPermission();
        getLocation();
        return rootView;
    }
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, locationCode);
        } else {
            getLocation(); // Call getLocation if permission already granted
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        VPadapter vpAdapter = new VPadapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new FragmentDbAll(), "All");
        vpAdapter.addFragment(new FragmentDbCrop(), "Crop");
        vpAdapter.addFragment(new FragmentDbSeed(), "Seed");
        viewPager.setAdapter(vpAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == locationCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                getLocation();
            }
        }
    }

    private void getLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longtitude = location.getLongitude();
                    fetchWeather(latitude, longtitude);
                }
            }
        });
    }

    private void fetchWeather(double latitude, double longtitude) {
        String apiKey = "2b1aacd3dcba886abc358d3dabf51aec";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(WeatherApiService.class);
//        service.getWeatherData(latitude, longtitude, apiKey).enqueue(new Callback<WeatherResponse>() {
//            @Override
//            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
//                if(response.isSuccessful() && response.body() != null) {
//                    WeatherResponse weatherResponse = response.body();
//                    displayWeather(weatherResponse);
//                }else {
//                    Log.d("not", "bruh"+response.code() + " " + response.message() +" " + response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WeatherResponse> call, Throwable t) {
//
//            }
//        });
        Call<WeatherResponse> call = service.getWeather(latitude, longtitude, apiKey);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    displayWeather(weatherResponse);
                } else {
                    Log.d("not", "bruh" + response.code() + " " + response.message() + " " + response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

            }
        });
    }

    private void displayWeather(WeatherResponse weatherResponse) {
        WeatherResponse.Main main = weatherResponse.getMain();
        WeatherResponse.Clouds clouds = weatherResponse.getClouds();
        WeatherResponse.Wind wind = weatherResponse.getWind();
        List<WeatherResponse.Weather> weatherList = weatherResponse.getWeather();
        if (main != null && weatherList != null && !weatherList.isEmpty() && clouds != null && wind != null) {
            WeatherResponse.Weather weather = weatherList.get(0);
            tvDescrip.setText(weather.getDescription());
            String iconCode = String.valueOf(weather.getIcon());
            if(iconCode.equals("01d")){
                ivCloud.setImageResource(R.drawable.sky_day);
            } else if (iconCode.equals("01n")) {
                ivCloud.setImageResource(R.drawable.sky_night);
            } else if (iconCode.equals("02d")) {
                ivCloud.setImageResource(R.drawable.few_clouds);
            }
            Log.d("code", iconCode);
            double formatFix = main.getTemp() - 273.15;
            String fixed = String.format("%.2f", formatFix);
            tvTemp.setText(fixed + "°C");
            tvHumidity.setText("Humidity: " + String.valueOf(main.getHumidity()));
            tvPressure.setText("Pressure: " + String.valueOf(main.getPressure()));
            tvWind.setText("Wind Speed: " + String.valueOf(wind.getSpeed()));
            tvCloudiness.setText("Cloudiness: " + String.valueOf(clouds.getAll()));
        }

    }
}