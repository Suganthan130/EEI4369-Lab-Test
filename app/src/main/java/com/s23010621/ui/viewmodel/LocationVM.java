package com.s23010621.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.s23010621.repository.LocationRepository;
import com.s23010621.repository.LoginRepository;

public class LocationVM extends ViewModel {

    private final MutableLiveData<LatLng> locationLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final LocationRepository locationRepository ;
    public LocationVM(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LiveData<LatLng> getLocationLiveData() {
        return locationLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void fetchLocation(String address) {
        locationRepository.getLocationFromAddress(address, new LocationRepository.LocationCallback() {
            @Override
            public void onSuccess(LatLng latLng) {
                locationLiveData.postValue(latLng);
            }

            @Override
            public void onError(String message) {
                errorLiveData.postValue(message);
            }
        });
    }
}

