package com.s23010621.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.s23010621.R;
import com.s23010621.Utils.ViewModelFactory;
import com.s23010621.repository.LocationRepository;
import com.s23010621.repository.LoginRepository;
import com.s23010621.ui.viewmodel.LocationVM;
import com.s23010621.ui.viewmodel.LoginVM;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private TextInputLayout materialEditTextAddress;
    private Button buttonShow;
    private LocationVM locationVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_google_map);
        initialView();
        initialVM();
        initialGoogleMap();
        setOnClick();
        viewModelObserver();
        setListeners();

    }

    private void setListeners() {
        if (materialEditTextAddress.getEditText()!=null){
            materialEditTextAddress.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().isEmpty()){
                        materialEditTextAddress.setError(null);
                    } else {
                        materialEditTextAddress.setError("Please enter you address");
                    }

                }
            });
        }

    }

    private void viewModelObserver() {

        locationVM.getLocationLiveData().observe(this, latLng -> {
            if (mMap != null) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });

        locationVM.getErrorLiveData().observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });
    }

    private void setOnClick() {
        buttonShow.setOnClickListener(v -> {
            String address = materialEditTextAddress.getEditText().getText().toString();
            if (!address.isEmpty()) {
                locationVM.fetchLocation(address);
            }
        });

        buttonShow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(GoogleMapActivity.this,TemperatureActivity.class));
                return false;
            }
        });

    }

    private void initialGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    private void initialVM() {
        LocationRepository locationRepository = new LocationRepository(this);
        ViewModelFactory<LocationRepository> locationRepositoryViewModelFactory = new ViewModelFactory<>(locationRepository);
        locationVM = new ViewModelProvider(this, locationRepositoryViewModelFactory).get(LocationVM.class);

    }

    private void initialView() {
        materialEditTextAddress = findViewById(R.id.material_edit_text_address);
        buttonShow = findViewById(R.id.show_location_button);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}