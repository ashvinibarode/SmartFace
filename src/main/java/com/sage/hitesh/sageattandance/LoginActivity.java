package com.sage.hitesh.sageattandance;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.sage.hitesh.sageattandance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoginActivity extends Activity {
    EditText etUID;
    String u_id;
    Button btnLogin;
    FirebaseDatabase database ;
    DatabaseReference reference ;
    Intent i ;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        checkPermissions();
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        init();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                u_id = etUID.getText().toString();
                Log.d("Firebase Test", "Login Act Button:"+u_id);
                Query checkUser = reference.orderByChild("id").equalTo(u_id);
                if(u_id.equals("adminHIT")){
                   dismissProgress();
                    i=new Intent(LoginActivity.this,AdminActivity.class); startActivity(i);}
                else {
                    checkUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String name = snapshot.child(u_id).child("name").getValue(String.class);
                                Log.d("Firebase Test", "onDataChange: " + snapshot.getValue().toString());
                                Log.d("Firebase Test", "onDataChange: " + snapshot.child(u_id).child("attendance").getChildrenCount());
                                if (u_id.contains("admin" +
                                        "")) {
                                    dismissProgress();
                                    i = new Intent(getBaseContext(), AdminActivity.class);
                                } else {
                                    dismissProgress();
                                    i = new Intent(getBaseContext(), SelectorActivity.class);
                                }
                                Log.d("Firebase Test", "onDataChange: " + u_id);
                                i.putExtra("UID", u_id);
                                startActivity(i);
                            }
                            else {
                                dismissProgress();
                                Log.d("Firebase Test", "onDataChange: Failed ");
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("User id is not correct please try again")
                                        .setTitle("User Not Exist")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                return;
//                                           onBackPressed();
                                            }
                                        });
                                 AlertDialog ad = builder.create();
                                ad.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                         dismissProgress();
                        }
                    });
                }
                }
        });
    }
    void init()
    {
        etUID = (EditText) findViewById(R.id.etUID);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("teachers");
    }
    void checkPermissions()
    {
        if(isNetworkAvailable()==false)
        {

        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA},0);
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showProgress() {
        if (isProgressShowing()) {
            dismissProgress();
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("PLEASE WAIT USER");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public boolean isProgressShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }
}
