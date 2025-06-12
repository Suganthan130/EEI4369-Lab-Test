package com.s23010621.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.s23010621.repository.SensorRepository;

public class SensorVM extends ViewModel {
    private final MutableLiveData<Float> temperatureLiveData = new MutableLiveData<>();
    private final SensorRepository sensorRepository;

    public SensorVM(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }
    public void initTemperature() {
        sensorRepository.setTemperatureCallback(temperatureLiveData::postValue);
    }

    public LiveData<Float> getTemperatureLiveData() {
        return temperatureLiveData;
    }

    public void unregisterListener() {
        if (sensorRepository != null)
            sensorRepository.unregister();
    }
}