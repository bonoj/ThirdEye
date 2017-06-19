package com.hk47.realityoverlay.utils;


import android.hardware.SensorManager;

import static com.hk47.realityoverlay.data.Constants.LOW_PASS_FILTER_CONSTANT;

public class SensorUtilities {

    public static float[] filterSensors(float[] input, float[] current) {

        float[] output = new float[3];

        if (current == null) {
            output = input;
        } else {
            for (int i = 0; i < input.length; i++) {
                output[i] = current[i] + LOW_PASS_FILTER_CONSTANT * (input[i] - current[i]);
            }
        }

        return output;
    }

    public static float[] computeDeviceOrientation(float[] accelerometerReading, float[] magnetometerReading) {

        if (accelerometerReading == null || magnetometerReading == null) {
            return null;
        }

        final float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);

        // Remap the coordinates with the camera pointing along the Y axis.
        // This way, portrait and landscape orientation return the same azimuth to magnetic north.
        final float cameraRotationMatrix[] = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X,
                SensorManager.AXIS_Z, cameraRotationMatrix);

        final float[] orientationAngles = new float[3];
        SensorManager.getOrientation(cameraRotationMatrix, orientationAngles);

        // Return a float array containing [azimuth, pitch, roll]
        return orientationAngles;
    }
}
