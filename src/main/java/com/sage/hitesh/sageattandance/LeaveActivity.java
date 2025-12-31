package com.sage.hitesh.sageattandance;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sage.hitesh.sageattandance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LeaveActivity extends Activity {
    EditText start_dd,start_mm,start_yyyy,end_dd,end_mm,end_yyyy,etReason;
    String sMM,eMM,sDD,eDD,sYYYY,eYYYY,startLeave,endLeave,reason,user_id;
    Button btnSubmit;
    FirebaseDatabase database;
    DatabaseReference reference;
    Intent i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_leave);
        init();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDD = start_dd.getText().toString();
                eDD = end_dd.getText().toString();
                eMM = end_mm.getText().toString();
                sYYYY = start_yyyy.getText().toString();
                eYYYY = end_yyyy.getText().toString();
                reason = etReason.getText().toString();
                sMM = start_mm.getText().toString();
                startLeave = sYYYY+"-"+sMM+"-"+sDD;
                endLeave = eYYYY+"-"+eMM+"-"+eDD;
//                user_id = "123456";
                user_id = i.getStringExtra("U_ID");
                Log.d("Firebase Test", "onClick: "+user_id);
                Query q = reference.child(user_id).child("leave");
                 q.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                     if(!snapshot.child(startLeave).exists())
                     {
                   reference.child(user_id).child("leave").child(startLeave).child("start_date").setValue(startLeave);
                   reference.child(user_id).child("leave").child(startLeave).child("end_date").setValue(endLeave);
                   reference.child(user_id).child("leave").child(startLeave).child("reason").setValue(reason);
                         AlertDialog.Builder builder = new AlertDialog.Builder(LeaveActivity.this);
                         builder.setMessage("Sucessfully marked your leave")
                                 .setTitle("Leave Marked")
                                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
//                                                    return;
                                         onBackPressed();
                                     }
                                 });
                         AlertDialog ad = builder.create();
                         ad.show();
                     }
                     else
                     {
                         AlertDialog.Builder builder = new AlertDialog.Builder(LeaveActivity.this);
                         builder.setMessage("You already marked your leave")
                                 .setTitle("Already Marked")
                                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
//                                                    return;
                                         onBackPressed();
                                     }
                                 });
                         AlertDialog ad = builder.create();
                         ad.show();
                     }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });
            }
        });

    }
    void init()
    {
        start_dd = (EditText) findViewById(R.id.etStartDD);
        start_mm = (EditText) findViewById(R.id.etStartMM);
        start_yyyy = (EditText) findViewById(R.id.etStartYYYY);
        end_dd = (EditText) findViewById(R.id.etEndDD);
        end_mm = (EditText) findViewById(R.id.etEndMM);
        end_yyyy = (EditText) findViewById(R.id.etEndYYYY);
        etReason = (EditText) findViewById(R.id.etReason);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("teachers");
        i = getIntent();
    }
}
