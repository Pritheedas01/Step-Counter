package com.pritha.www.stepcount;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private EditText username,key,adjust_weight,adjust_height;
    private Button sign;
    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username = findViewById(R.id.username);
        key = findViewById(R.id.key);
        adjust_height = findViewById(R.id.adjust_height);
        adjust_weight = findViewById(R.id.adjust_width);
        sign = findViewById(R.id.sign);

        auth = FirebaseAuth.getInstance();

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_key = key.getText().toString();
                String txt_height = adjust_height.getText().toString();
                String txt_width= adjust_weight.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_key) || TextUtils.isEmpty(txt_height) || TextUtils.isEmpty(txt_width)) {
                    Toast.makeText(SignupActivity.this, "all data are required", Toast.LENGTH_SHORT).show();
                } else if (txt_key.length() < 4) {
                    Toast.makeText(SignupActivity.this, "password is too short", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_username, txt_key, txt_height,txt_width);
                }
            }
        });
    }

    public void register(final String username, final String key, final String height,final  String weight) {

        final DatabaseReference chatref1=FirebaseDatabase.getInstance().getReference("Userss").child(username);
        chatref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    reference = FirebaseDatabase.getInstance().getReference("Userss").child(username);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("username", username);
                    hashMap.put("key", key);
                    hashMap.put("height", height);
                    hashMap.put("weight", weight);
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                intent.putExtra("username", username);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(SignupActivity.this,"register not done",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignupActivity.this,"user already exist",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

