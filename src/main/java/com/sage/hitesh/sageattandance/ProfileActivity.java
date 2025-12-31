package com.sage.hitesh.sageattandance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sage.hitesh.sageattandance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends Activity {
    TextView tvNam, tvMobile,tvAddress,tvEmail;
    FirebaseDatabase database;
    DatabaseReference reference;
    byte[] bytes;
    String name,address,mobile,image,email,uid;
    Bitmap bitmap;
    ImageView imgImg;
    Intent i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        i = getIntent();
        uid=i.getStringExtra("U_ID");
        Log.d("Firebase Test", "onClick: "+uid);
//        uid ="123456";
        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue(String.class);
                email = snapshot.child("email").getValue(String.class);
                address = snapshot.child("address").getValue(String.class);
                mobile = snapshot.child("phone").getValue(String.class);
                image = snapshot.child("image").getValue(String.class);
                setData();
                try{
                    bytes= Base64.decode(image,Base64.DEFAULT);
                    bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    imgImg.setImageBitmap(bitmap);
                   }
                catch (Exception e){
                    Log.d("RecogApi","Inside try of samagra"+e.getLocalizedMessage());}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Problem in database", Toast.LENGTH_SHORT).show();
            }
        });

    }
    void init()
    {
        tvNam = (TextView) findViewById(R.id.tvName);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("teachers");
        imgImg = (ImageView) findViewById(R.id.imgIMG);

    }
    void setData()
    {
        tvEmail.setText(email);
        tvNam.setText(name);
        tvMobile.setText(mobile);
        tvAddress.setText(address);
    }
}
