package com.sage.hitesh.sageattandance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.sage.hitesh.sageattandance.R;

public class SelectorActivity extends Activity implements View.OnClickListener {
    String Uid;
    Intent i,intent;
    CardView btnAttandance,btnProfile,btnLeave,btnPerformance;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_selection);
        btnAttandance=findViewById(R.id.btnMark);
        btnLeave=findViewById(R.id.btnLeave);
        btnPerformance= findViewById(R.id.btnPerformane);
        btnProfile= findViewById(R.id.btnProfile);
        btnAttandance.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        btnPerformance.setOnClickListener(this);
        btnLeave.setOnClickListener(this);
        i=getIntent();
        Uid = i.getStringExtra("UID");
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnMark) {
            Log.d("Firebase Test", "onClick: ");
            intent = new Intent(this, MarkAttActivity.class);
            intent.putExtra("U_ID", Uid);
            startActivity(intent);
        }
         else if(v.getId()==R.id.btnProfile)
            {
                intent = new Intent(this,ProfileActivity.class);
                intent.putExtra("U_ID",Uid);
                Log.d("Firebase Test", "onClick: "+Uid);
                startActivity(intent);
            }
        else if(v.getId()== R.id.btnPerformane)
            {
                intent = new Intent(this,DashboardActivity.class);
                intent.putExtra("U_ID",Uid);
                startActivity(intent);

            }
        else if(v.getId()== R.id.btnLeave)
            {
                intent = new Intent(this,LeaveActivity.class);
                intent.putExtra("U_ID",Uid);
                startActivity(intent);
            }
        }
    }

