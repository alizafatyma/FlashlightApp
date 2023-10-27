package com.example.flashlightapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Switch flashlightSwitch;
    private CameraManager cameraManager;
    private String cameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashlightSwitch = findViewById(R.id.switch1);

        if (!hasCameraFlash()) {
            Toast.makeText(this, "Your device doesn't support flash or the app doesn't have permission to access it.", Toast.LENGTH_SHORT).show();
            flashlightSwitch.setEnabled(false);
            return;
        }

        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to access the camera.", Toast.LENGTH_SHORT).show();
        }

        flashlightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, isChecked);
                    }
                } catch (CameraAccessException e) {
                    Toast.makeText(MainActivity.this, "Failed to toggle flashlight", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean hasCameraFlash() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }
}
