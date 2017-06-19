package com.hk47.realityoverlay.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hk47.realityoverlay.R;
import com.hk47.realityoverlay.data.Constants;

import static com.hk47.realityoverlay.data.Constants.BOTH_PERMISSIONS_REQUEST_CODE;
import static com.hk47.realityoverlay.data.Constants.CAMERA_PERMISSION_REQUEST_CODE;
import static com.hk47.realityoverlay.data.Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE;

public class PermissionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
    }

    public void onEyeClicked(View view) {
        requestPermissions();
    }

    public void requestPermissions() {
        if (!checkCameraPermission() && !checkLocationPermission()) {
            String[] bothPermissions = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, bothPermissions, BOTH_PERMISSIONS_REQUEST_CODE);
        } else if (!checkCameraPermission()) {
            String[] cameraPermission = {Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_PERMISSION_REQUEST_CODE);
        } else if (!checkLocationPermission()) {
            String[] locationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, locationPermission, FINE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkCameraPermission() {
        return ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED));
    }

    private boolean checkLocationPermission() {
        return ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (permissions.length == 0) {
            return;
        }

        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }

        if (allPermissionsGranted) {
            Toast.makeText(this, getString(R.string.permissions_both_granted), Toast.LENGTH_SHORT).show();
            Intent overlayIntent = new Intent(this, OverlayActivity.class);
            overlayIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(overlayIntent);
        }

        if (!allPermissionsGranted) {
            boolean somePermissionsForeverDenied = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    // Permission has been denied

                } else {
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                        // Permission has been granted

                    } else {
                        // Permission is set to never ask again
                        somePermissionsForeverDenied = true;
                    }
                }
            }

            if (somePermissionsForeverDenied) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getString(R.string.permissions_required))
                        .setMessage(getString(R.string.permissions_open_settings))
                        .setPositiveButton(getString(R.string.permissions_settings), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(getString(R.string.permissions_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setCancelable(false);

                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
                Button negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                Button positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            } else {
                switch (requestCode) {
                    case Constants.BOTH_PERMISSIONS_REQUEST_CODE:
                        if (checkCameraPermission() && !checkLocationPermission()) {
                            showSnackbar(getString(R.string.permissions_location_denied));
                        } else if (!checkCameraPermission() && checkLocationPermission()) {
                            showSnackbar(getString(R.string.permissions_camera_denied));
                        } else {
                            showSnackbar(getString(R.string.permissions_both_denied));
                        }
                        break;
                    case Constants.CAMERA_PERMISSION_REQUEST_CODE:
                        if (!checkCameraPermission()) {
                            showSnackbar(getString(R.string.permissions_camera_denied));
                        }
                        break;
                    case Constants.FINE_LOCATION_PERMISSION_REQUEST_CODE:
                        if (!checkLocationPermission()) {
                            showSnackbar(getString(R.string.permissions_location_denied));
                        }
                        break;
                }
            }
        }
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.permissions_container), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        snackbar.show();
    }
}
