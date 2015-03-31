package com.example.rpetrov.sensorstest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private SensorManager mSensorManager;
    private static final int MAX_COUNT = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        final List<Sensor> listSensor = new ArrayList<>();
        listSensor.addAll(mSensorManager.getSensorList(Sensor.TYPE_ALL));



        ViewGroup listNames = (ViewGroup) findViewById(R.id.listNames);
        final ViewGroup testResult = (ViewGroup) findViewById(R.id.testResult);

        for (final Sensor sensor : listSensor) {
            View availableSensors = LayoutInflater.from(this).inflate(R.layout.sensor_name_item, listNames, false);
            listNames.addView(availableSensors);
            TextView availableSensorsText = (TextView) availableSensors.findViewById(R.id.name);
            availableSensorsText.setText(getTypeDescription(sensor.getType()));
            availableSensorsText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, getLineForSensor(sensor), Toast.LENGTH_LONG).show();
                }
            });
        }


        View run = findViewById(R.id.run);
        run.setOnClickListener(new View.OnClickListener() {

            int sensCount = 0;
            Sensor sensor = listSensor.get(0);




            @Override
            public void onClick(View view) {

                findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

                mSensorManager.registerListener(new SensorEventListener() {

                    private int count = 0;
                    private long startTime;

                    @Override
                    public void onSensorChanged(SensorEvent sensorEvent) {
                        if (count == 0)
                            startTime = System.currentTimeMillis();


                        if (count == MAX_COUNT) {
                            View res = LayoutInflater.from(MainActivity.this).inflate(R.layout.result_item, testResult, false);
                            testResult.addView(res);
                            TextView availableSensorsText = (TextView) res.findViewById(R.id.name);
                            availableSensorsText.setText(getTypeDescription(sensor.getType()));
                            TextView result = (TextView) res.findViewById(R.id.result);
                            double del = (System.currentTimeMillis() - startTime)/((double) MAX_COUNT);
                            double freq = 1000d/del;
                            result.setText(String.format("%.2f", freq));
                            ScrollView scroll = (ScrollView) findViewById(R.id.scroll);
                            scroll.fullScroll(View.FOCUS_DOWN);

                            mSensorManager.unregisterListener(this);
                            sensCount++;
                            if(listSensor.size() > sensCount){
                                sensor = listSensor.get(sensCount);
                                count = 0;
                                startTime = System.currentTimeMillis();
                                mSensorManager.registerListener(this, listSensor.get(sensCount), SensorManager.SENSOR_DELAY_FASTEST);

                            } else {
                                findViewById(R.id.progress_bar).setVisibility(View.GONE);
                            }
                        }
                        count++;
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int i) {

                    }
                }, sensor, SensorManager.SENSOR_DELAY_FASTEST);

            }
        });
    }

    private String getLineForSensor(Sensor sensor) {
        if (sensor == null)
            return null;

        return sensor.getName() + ", " + sensor.getVendor() + ", Resolution = " + sensor.getResolution();
    }

    public String getTypeDescription(int type) {
        String description = null;
        Context context = this;

        switch (type) {
            case Sensor.TYPE_ACCELEROMETER:
                description = context.getResources().getString(
                        R.string.accelerometer);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                description = context.getResources().getString(
                        R.string.ambient_temperature);
                break;
            case Sensor.TYPE_GRAVITY:
                description = context.getResources().getString(R.string.gravity);
                break;
            case Sensor.TYPE_GYROSCOPE:
                description = context.getResources().getString(R.string.gyroscope);
                break;
            case Sensor.TYPE_LIGHT:
                description = context.getResources().getString(R.string.light);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                description = context.getResources().getString(
                        R.string.linear_acceleration);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                description = context.getResources().getString(
                        R.string.magnetic_field);
                break;
            case Sensor.TYPE_ORIENTATION:
                description = context.getResources()
                        .getString(R.string.orientation);
                break;
            case Sensor.TYPE_PRESSURE:
                description = context.getResources().getString(R.string.pressure);
                break;
            case Sensor.TYPE_PROXIMITY:
                description = context.getResources().getString(R.string.proximity);
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                description = context.getResources().getString(
                        R.string.relative_humidity);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                description = context.getResources().getString(
                        R.string.rotation_vector);
                break;
            case Sensor.TYPE_TEMPERATURE:
                description = context.getResources()
                        .getString(R.string.temperature);
                break;
            default:
                description = context.getResources().getString(R.string.unknown);
                break;
        }

        return description;
    }
}
