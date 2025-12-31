package com.sage.hitesh.sageattandance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sage.hitesh.sageattandance.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.InstallationTokenResult;

import java.util.ArrayList;

public class DashboardActivity extends Activity {
    TextView tvName,tvAbsent,tvPresent,tvLeave;
    ImageView imgBit;
    String name,uid,image;
    long present , absent , leave;
    Intent i ;
    byte[] bytes;
    Bitmap bitmap;
    FirebaseDatabase database ;
    DatabaseReference reference;
    PieChart pieChart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dashboard);
        init();
        i= getIntent();
        uid = i.getStringExtra("U_ID");
        Log.d("Firebase Test", "onCreate: "+uid);
        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue(String.class);
                present = snapshot.child("attendance").getChildrenCount();
                leave = snapshot.child("leave").getChildrenCount();
                absent = 30-(present + leave);
                image = snapshot.child("image").getValue(String.class);
                tvName.setText(name);
                tvAbsent.setText(""+absent);
                tvPresent.setText(""+present);
                tvLeave.setText(""+leave);
                try{
                    getPieChart(present,absent,leave);
                    bytes= Base64.decode(image,Base64.DEFAULT);
                    bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    imgBit.setImageBitmap(bitmap);
                }
                catch (Exception e){Log.d("RecogApi","Inside try of samagra"+e.getLocalizedMessage());}
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("Firebase Test", "onCreate: "+present);
//        getPieChart();
    }
    void init()
    {
        tvName = (TextView) findViewById(R.id.tvName);
        tvPresent = (TextView) findViewById(R.id.tvDaysPresent);
        tvAbsent = (TextView) findViewById(R.id.tvDaysAbesnt);
        tvLeave = (TextView) findViewById(R.id.tvDaysLeave);
        imgBit = (ImageView) findViewById(R.id.imgBit);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("teachers");
        pieChart = findViewById(R.id.pieChart);
    }
    void getPieChart(float present,float absent,float leave)
    {
        Log.d("Firebase Test", "getPieChart: Present "+present);
        ArrayList<PieEntry> attendance = new ArrayList();
        attendance.add(new PieEntry(present,"Pesent"));
        attendance.add(new PieEntry(absent,"Absent"));
        attendance.add(new PieEntry(leave,"Leave"));
        PieDataSet attendanceDataSet = new PieDataSet(attendance,"");
        attendanceDataSet.setColors(new int[] {Color.GREEN,Color.RED,Color.YELLOW});
        PieData pieData = new PieData(attendanceDataSet);
        pieChart.setData(pieData);
        pieChart.animateY(1400, Easing.EasingOption.EaseOutQuad);
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setCenterText("Attendence");
        pieChart.setTransparentCircleColor(Color.WHITE);


    }

}
