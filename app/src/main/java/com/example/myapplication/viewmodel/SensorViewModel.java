package com.example.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.SensorDataDto;
import com.example.myapplication.repository.SensorRepository;

public class SensorViewModel extends ViewModel {

    private final MutableLiveData<SensorDataDto> sensorData = new MutableLiveData<>();
    private final MutableLiveData<String> recommendation = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final SensorRepository repository = new SensorRepository();

    public LiveData<SensorDataDto> getSensorData() {
        return sensorData;
    }

    public LiveData<String> getRecommendation() {
        return recommendation;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadData(String roomName) {
        repository.fetchSensorData(roomName, new SensorRepository.SensorDataCallback() {
            @Override
            public void onSuccess(SensorDataDto data) {
                sensorData.postValue(data);
            }

            @Override
            public void onFailure(String message) {
                error.postValue("Помилка даних: " + message);
            }
        });

        repository.fetchRecommendation(roomName, new SensorRepository.RecommendationCallback() {
            @Override
            public void onSuccess(String reco) {
                recommendation.postValue(reco);
            }

            @Override
            public void onFailure(String message) {
                error.postValue("Помилка рекомендації: " + message);
            }
        });
    }
}
