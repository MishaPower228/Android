package com.example.myapplication.repository;

import android.util.Log;

import com.example.myapplication.model.SensorDataDto;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SensorRepository {

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private final String BASE_URL = "http://192.168.249.32:5210/api/DisplayData"; // заміни на свою адресу

    public interface SensorDataCallback {
        void onSuccess(SensorDataDto data);
        void onFailure(String message);
    }

    public interface RecommendationCallback {
        void onSuccess(String recommendation);
        void onFailure(String message);
    }

    public void fetchSensorData(String roomName, SensorDataCallback callback) {
        String url = BASE_URL + "/" + roomName + "/latest/DTO";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure("Код: " + response.code());
                    return;
                }

                String body = response.body().string();
                try {
                    SensorDataDto data = gson.fromJson(body, SensorDataDto.class);
                    callback.onSuccess(data);
                } catch (Exception ex) {
                    callback.onFailure("JSON помилка: " + ex.getMessage());
                }
            }
        });
    }

    public void fetchRecommendation(String roomName, RecommendationCallback callback) {
        String url = BASE_URL + "/" + roomName + "/recommendations";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure("Код: " + response.code());
                    return;
                }

                String body = response.body().string();
                try {
                    // Очікуємо JSON з полем "recommendation"
                    String reco = gson.fromJson(body, com.example.myapplication.model.ComfortRecommendation.class).getRecommendation();
                    callback.onSuccess(reco);
                } catch (Exception ex) {
                    callback.onFailure("JSON помилка: " + ex.getMessage());
                }
            }
        });
    }
}
