package com.pritha.www.stepcount;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

import static com.pritha.www.stepcount.R.id.btn_start;

public class MainActivity extends AppCompatActivity{
    private TextView TvSteps,Tvcal,TvKm,Date;
    private Button BtnStart,Btnset,Dataview,logout;
    private EditText Set_goals;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps;
    private ProgressBar progressBar;
    private int max=100;
    private int progressBarStatus = 0;

    int a=0;
    public String username,username1,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent =getIntent();
        username =intent.getStringExtra("username");

        TvSteps =  findViewById(R.id.tv_steps);
        BtnStart =  findViewById(btn_start);
        Tvcal=findViewById(R.id.tv_cal);
        TvKm=findViewById(R.id.tv_km);
        Dataview=findViewById(R.id.dataview);
        Btnset=findViewById(R.id.btn_set);
        progressBar=findViewById(R.id.progress_bar);
        Set_goals=findViewById(R.id.Set_goals);
        logout=findViewById(R.id.btn_logout);
        Date=findViewById(R.id.date);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent next = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                next.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                next.setData(Uri.parse("package:" + packageName));
                startActivity(next);
            }
        }

        final SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
        username1=preferences.getString("username","");



        Dataview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DataActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        date = day + "/" + (month + 1) + "/" + year;

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                SharedPreferences.Editor editor=preferences.edit();
                editor.clear();
                editor.commit();
                startActivity(intent);

            }
        });

        Btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goals = Set_goals.getText().toString().trim();
                max=Integer.parseInt(goals);
                progressBar.setMax(max);

            }
        });



        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = 0;
                Intent serviceIntent = new Intent(MainActivity.this, ExampleService.class);
                serviceIntent.putExtra("inputExtra", count);
                ContextCompat.startForegroundService(MainActivity.this, serviceIntent);

            }
        });
    }


    public void stopService(View view) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);
        Log.e(" service stopped", numSteps + "");
        String timestamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Data").child(username1).child(timestamp);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Steps", numSteps + "  on  " + date );
        hashMap.put("Calorie", String.valueOf(numSteps*0.04));
        hashMap.put("KMs", String.valueOf((numSteps*0.0075)));
        hashMap.put("Username", username);
        mDatabaseRef.setValue(hashMap);

    }


    BroadcastReceiver serviceReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            numSteps = intent.getIntExtra("STEPSCOUNT", 0);
            Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setDuration(1000);
            TvSteps.setAnimation(animationSet);
            TvSteps.setText(TEXT_NUM_STEPS + numSteps);
            if (numSteps<=max)
            {
                progressBar.setProgress(numSteps);
            }
            TvKm.setText("" + numSteps*0.0075);
            Tvcal.setText("" + numSteps*0.04);

            Log.e("Steps in Class", numSteps + "");

//            saveindatabase(numSteps);
        }

    };

//    private void saveindatabase(int numSteps) {
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Data").child(username).child(timestamp);
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("Steps", String.valueOf(numSteps) + "  on  " + Date.getText().toString());
//        hashMap.put("Calorie", String.valueOf((numSteps*0.04)));
//        hashMap.put("KMs", String.valueOf((numSteps*0.0075)));
//        hashMap.put("Username", username);
//        mDatabaseRef.setValue(hashMap);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter stepDetect = new IntentFilter("COUNTER");
        registerReceiver(serviceReciver, stepDetect);
    }
}

