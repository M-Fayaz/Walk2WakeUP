package com.sif.alarmwalk2wakeup.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.sif.alarmwalk2wakeup.R;
import com.sif.alarmwalk2wakeup.data.Alarm;
import com.sif.alarmwalk2wakeup.service.AlarmService;

import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RingActivity extends AppCompatActivity implements SensorEventListener{
    @BindView(R.id.activity_ring_dismiss) Button dismiss;
    @BindView(R.id.activity_ring_snooze) Button snooze;
    @BindView(R.id.activity_ring_clock) ImageView clock;

    private TextView textViewCounter,textViewDetector;
    private SensorManager sensorManager;
    private Sensor nStepsCounter,nStepDetector;
    private boolean isCounterSensorPresent,isDetectorSensorPresent;
    int stepCounter= 0,stepDetect=0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);


        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textViewCounter = findViewById(R.id.textViewCounter);
        textViewDetector = findViewById(R.id.textViewdetector);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        animateClock();





            dismiss.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(stepDetect>=25){
                        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                        getApplicationContext().stopService(intentService);
                        finish();
                    }else{
                        Toast.makeText(RingActivity.this, "Steps not completed please walk", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(RingActivity.this, "", Toast.LENGTH_SHORT).show();

                }
            });

            snooze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.add(Calendar.MINUTE, 10);

                    Alarm alarm = new Alarm(
                            new Random().nextInt(Integer.MAX_VALUE),
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            "Snooze",
                            System.currentTimeMillis(),
                            true,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false
                    );

                    alarm.schedule(getApplicationContext());

                    Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                    getApplicationContext().stopService(intentService);
                    finish();
                }
            });


        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            nStepsCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounterSensorPresent = true;
        } else {
            textViewCounter.setText(String.valueOf(stepCounter));
            textViewCounter.setText("Counter sensor is not present");
            isCounterSensorPresent = false;
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            nStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            isDetectorSensorPresent = true;
        } else {
            textViewDetector.setText("Detector sensor is not present");
        }
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor==nStepsCounter){
            stepCounter=(int) sensorEvent.values[0];
            textViewCounter.setText(String.valueOf(stepCounter));
        }else if(sensorEvent.sensor==nStepDetector){
            stepDetect=(int)(stepDetect+sensorEvent.values[0]);
            textViewDetector.setText(String.valueOf(stepDetect));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
            sensorManager.registerListener(this,nStepsCounter,SensorManager.SENSOR_DELAY_NORMAL);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null){
            sensorManager.registerListener(this,nStepDetector,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null)
            sensorManager.unregisterListener(this,nStepsCounter);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)!=null){
            sensorManager.unregisterListener(this,nStepDetector);
        }


    }



    private void animateClock() {
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(clock, "rotation", 0f, 20f, 0f, -20f, 0f);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimation.setDuration(800);
        rotateAnimation.start();
    }
}
