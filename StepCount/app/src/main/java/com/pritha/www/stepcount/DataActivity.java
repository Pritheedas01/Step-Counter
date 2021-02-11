package com.pritha.www.stepcount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterData adapterData;
    DatabaseReference reference;
    List<ModelData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        recyclerView = findViewById(R.id.recycler_view_data);
        recyclerView.setHasFixedSize(true);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        SharedPreferences preferences=getSharedPreferences("mydata",MODE_PRIVATE);
        final String username1=preferences.getString("username","");

        list=new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Data").child(username1);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    ModelData p = dataSnapshot1.getValue(ModelData.class);
                    String username1=""+dataSnapshot1.child("Username").getValue();
                    list.add(p);
                    // if (username1.equals(username)){
                    //     list.add(p);
                    //  }
                }
                adapterData = new AdapterData(DataActivity.this, list);
                recyclerView.setAdapter(adapterData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DataActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
}