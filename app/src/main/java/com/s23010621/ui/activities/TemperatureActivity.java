package com.s23010621.ui.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.s23010621.R;
import com.s23010621.Utils.ViewModelFactory;
import com.s23010621.repository.LoginRepository;
import com.s23010621.repository.SensorRepository;
import com.s23010621.ui.viewmodel.LoginVM;
import com.s23010621.ui.viewmodel.SensorVM;

public class TemperatureActivity extends AppCompatActivity {
    private SensorVM sensorViewModel;
    private MediaPlayer mediaPlayer;
    private boolean hasPlayedAudio = false;
    private final int THRESHOLD = 21; // Last 21 digits of SID
    private TextView temperatureText;
    private View redOverlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_temperature);
        initialView();
        initialVM();
        viewModelObserver();
    }

    private void viewModelObserver() {
        sensorViewModel.getTemperatureLiveData().observe(this, temp -> {
            temperatureText.setText("Temp: " + temp + "°C");

            if (temp >= THRESHOLD) {
                if (!hasPlayedAudio) {
                    playAudio();
                    hasPlayedAudio = true;
                }
                temperatureText.setTextColor(ContextCompat.getColor(this, R.color.white));
                showRedOverlay(redOverlay);
            } else {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                hasPlayedAudio = false;
                temperatureText.setTextColor(ContextCompat.getColor(this, R.color.black));
                hideRedOverlay(redOverlay);
            }
        });
    }

    private void initialView() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        temperatureText = findViewById(R.id.temperatureText);
        redOverlay = findViewById(R.id.redOverlay);
    }

    private void initialVM() {
        SensorRepository sensorRepository  = new SensorRepository(this);
        ViewModelFactory<SensorRepository> sensorRepositoryViewModelFactory = new ViewModelFactory<>(sensorRepository);
        sensorViewModel = new ViewModelProvider(this, sensorRepositoryViewModelFactory).get(SensorVM.class);
        sensorViewModel.initTemperature();
    }
    private void playAudio() {
        mediaPlayer = MediaPlayer.create(this, R.raw.alert_sound);
        mediaPlayer.start();
    }

    private void showRedOverlay(View overlay) {
        overlay.setVisibility(View.VISIBLE);
        overlay.animate().alpha(1f).setDuration(5000).start();
    }

    private void hideRedOverlay(View overlay) {
        overlay.animate().alpha(0f).setDuration(400).withEndAction(() ->
                overlay.setVisibility(View.GONE)).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorViewModel.unregisterListener();
        if (mediaPlayer != null) mediaPlayer.release();
    }
}