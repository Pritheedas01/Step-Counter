package com.pritha.www.stepcount;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText username,key;
    Button login,signup;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference reference;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        key = findViewById(R.id.key);
        login = findViewById(R.id.Login);
        signup = findViewById(R.id.sign);

        final SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
        if(preferences.getBoolean("login",false)){
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);

        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View promptsView = li.inflate(R.layout.layout_dialog, null);

                alertDialogBuilder.setView(promptsView);


                final EditText userInput = (EditText) promptsView.findViewById(R.id.username);
                final EditText userInput1 = (EditText) promptsView.findViewById(R.id.key);

                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Go", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //finish();


                        final String txt_email=userInput.getText().toString();
                        final String txt_password= userInput1.getText().toString();
                        if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                            Toast.makeText(Login.this,"all feilds are required",Toast.LENGTH_SHORT).show();
                        }else {
                            reference = FirebaseDatabase.getInstance().getReference("Userss");
                            Query usQuery = reference.orderByChild("username").equalTo(txt_email);
                            usQuery.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        String key = "" + ds.child("key").getValue();
                                        if (key.equals(txt_password)) {
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            intent.putExtra("username", txt_email);
                                            SharedPreferences.Editor editor=preferences.edit();
                                            editor.putString("username",txt_email);
                                            editor.putBoolean("login",true);
                                            editor.commit();
                                            startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });


                alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"You clicked on Cancel",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}