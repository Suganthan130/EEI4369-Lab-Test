package com.s23010621.repository;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.s23010621.ui.Helper.DatabaseHelper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationRepository {
    private Context context;

    public LocationRepository(Context context) {
        this.context = context;
    }

    public interface LocationCallback {
        void onSuccess(LatLng latLng);
        void onError(String message);
    }

    public void getLocationFromAddress(String address, LocationCallback callback) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocationName(address, 1);
            if (list != null && !list.isEmpty()) {
                Address loc = list.get(0);
                callback.onSuccess(new LatLng(loc.getLatitude(), loc.getLongitude()));
            } else {
                callback.onError("Location not found");
            }
        } catch (IOException e) {
            callback.onError("Error: " + e.getMessage());
        }
    }
}
