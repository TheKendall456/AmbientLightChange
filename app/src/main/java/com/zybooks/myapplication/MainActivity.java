//Kendall Goethals, Caden Sanders, Ebbett Grant




package com.zybooks.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private float maxLuxValue;
//on create of the application pulls default state
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sensor manager allows you to access the devices sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //the sensor type is pulling what kind of sensor is being used for the data
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        //if the light sensor is not empty it will set the max value from the max rangevof the sensor
        if (lightSensor != null) {
            maxLuxValue = lightSensor.getMaximumRange();
        } else {
            Toast.makeText(this, "No light sensor available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    //when sensor is changed it wil call this method which passes the sensor event through event
    @Override
    public void onSensorChanged(SensorEvent event) {

        //checks if sensor is equal to the right type
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

            //sets the lux value which is the ambient brightness equal to the Sensor events value
            //which is the lux value changed
            float lux = event.values[0];

            //sets the percentage based on lux value divded by the max value it can have which then turns it into a percentage value
            float percentage = (lux / maxLuxValue) * 100;

            // Update brightness level or UI based on ambient light level
            //by passing the percentage value
            updateBrightness(percentage);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
// Not used in this example
    }

    //method for updating brightness
    private void updateBrightness(float percentage) {

        //pulls the id of the brightness display text
        TextView DisplayText = (TextView) findViewById(R.id.BrightnessDisplay);

        //pulls the layout to be for the layout parameters to set later
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

        //changes the screen brightness from the percentage value given from sensor changed method
        layoutParams.screenBrightness = percentage / 100;

        //sets the attributes from the layout parameters changed
        getWindow().setAttributes(layoutParams);

        //changes the text in the view
        DisplayText.setText(MessageFormat.format("{0}%", percentage));
    }
}