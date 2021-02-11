package com.pritha.www.stepcount;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.pritha.www.stepcount.App.CHANNEL_ID;
import static java.lang.StrictMath.sqrt;

public class ExampleService extends Service implements SensorEventListener {
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private int numSteps;
    Sensor sensor;
    private float[] cAccelerometer = {0, 0, 0};
    private float cachedAc = 0;
    private static final float ALPHA = (float) 0.7;
    private long lastStepCountTime = 0;


    public static final String MY_TAG = "MyTag";

    @Override
    public void onCreate() {
        super.onCreate();
        showNotification(numSteps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Device not Compatible!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        numSteps = intent.getIntExtra("inputExtra", 0);
        showNotification(numSteps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Device not Compatible!", Toast.LENGTH_LONG).show();
        }
//        //do heavy work on a background thread
//        Thread thread=new Thread(new Runnable() {
//
//
//            @Override
//            public void run() {
//                Log.d(MY_TAG,"run");
//                int i=0;
//                while (i<=10){
//                    Log.d(MY_TAG,""+(i+1));
//                    try {
//                        Thread.sleep(9000);
//                    }catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
//                    i++;
//
//
//                }
//                Log.d(MY_TAG,"running");
//                stopForeground(true);
//                stopSelf();
//
//            }
//        });
//        thread.start();;
        //return START_STICKY;
        //stopSelf();
        return START_STICKY;

    }

    private void showNotification(int numSteps) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra( "Notify" ,numSteps) ;
        notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
        notificationIntent.setAction(Intent. ACTION_MAIN ) ;
        notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(numSteps + "")
                .setSmallIcon(R.drawable.footprint)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currTime = System.currentTimeMillis();
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        cachedAc = (float) ((1 - ALPHA) * cachedAc + ALPHA * sqrt(x * x + y * y + z * z));
        System.arraycopy(event.values, 0, cAccelerometer, 0, event.values.length);
        if (cachedAc > 11.5) {
            // There needs to be at least 300ms between two peaks, otherwise it isn't a step.
            if (currTime - lastStepCountTime > 1000) {
                numSteps++;
                lastStepCountTime = currTime;
                showNotification(numSteps);
                sendCount(numSteps);



            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void sendCount(int numSteps) {
        Intent sendbroad = new Intent();
        sendbroad.putExtra("STEPSCOUNT", numSteps);
        sendbroad.setAction("COUNTER");
        sendBroadcast(sendbroad);
    }
}
