package com.sage.hitesh.sageattandance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.sage.hitesh.sageattandance.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        Log.d("Firebase Test", "Reference: "+reference.child("teachers"));
        reference.child("teachers").child("123456").child("attendance").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Firebase Test", "Error getting data", task.getException());
                }
                else {
                   //Gets the int value of ATTENDANCE
                    Log.d("Firebase Test", String.valueOf(task.getResult().getChildrenCount()));
                    //Gets all the string record of ATTENDANCE
                    Log.d("Firebase Test", String.valueOf(task.getResult().getValue()));
                    Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                    new Handler().postDelayed(new Runnable()
                    {@Override public void run() {
                        startActivity(i);
                        }},2000);
                }
            }
        });
    }

}