package com.sage.hitesh.sageattandance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
//Made By Hitesh
import androidx.annotation.Nullable;



public class AdminActivity extends Activity {
    LinearLayout btnSeeAllInfo,btnAttendence;
    Intent i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_admin);
        btnAttendence=(LinearLayout) findViewById(R.id.btnAttendence);
        btnSeeAllInfo=(LinearLayout) findViewById(R.id.btnAllInfo);

        btnAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    i=new Intent(AdminActivity.this,AdminAttendenceActivity.class);
                    startActivity(i);
                }
                catch(Exception e){ Log.d("Firebase Test", e.getLocalizedMessage()); }
            }
        });
        btnSeeAllInfo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               i= new Intent(AdminActivity.this,InforActivity.class);
               startActivity(i);
           }
       });
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    i=new Intent(AdminActivity.this,RegistrationActivity.class);
                    startActivity(i);
                }
                catch(Exception e){ Log.d("Firebase Test", e.getLocalizedMessage()); }
            }
        });
    }
}
