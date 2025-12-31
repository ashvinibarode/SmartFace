package com.sage.hitesh.sageattandance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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

public class RegistrationActivity extends Activity {
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_registration);
        EditText etName,etID,etEmail,etMobile,etAddress;
        etName = findViewById(R.id.etRegName);
        etID = findViewById(R.id.etTeacherID);
        etEmail = findViewById(R.id.etRegEmail);
        etMobile = findViewById(R.id.etRegMobile);
        etAddress = findViewById(R.id.etRegAddress);
        findViewById(R.id.btnRegNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,id,address,email,mobile;
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                id = etID.getText().toString();
                mobile = etMobile.getText().toString();
                address = etAddress.getText().toString();
                showProgress();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference();
                Query q = reference.child("teachers");
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.child(id).exists()){
                            reference.child("teachers").child(id).child("id").setValue(id);
                            reference.child("teachers").child(id).child("name").setValue(name);
                            reference.child("teachers").child(id).child("address").setValue(address);
                            reference.child("teachers").child(id).child("phone").setValue(mobile);
                            reference.child("teachers").child(id).child("email").setValue(email);
                            dismissProgress();
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                            builder.setMessage("You sucessful added a teacher")
                                    .setTitle("Added Sucessfully")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                                    return;

                                            onBackPressed();
                                        }
                                    });
                            AlertDialog  ad = builder.create();

                            ad.show();
                        }
                        else{
                            dismissProgress();
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                            builder.setMessage("This Teacher had already been added")
                                    .setTitle("Already Added")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
//                                                    return;
                                            onBackPressed();
                                        }
                                    });
                            AlertDialog  ad = builder.create();

                            ad.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        dismissProgress();
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setMessage("Someting Went Wrong Please try later")
                                .setTitle("Someting Wrong")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                                    return;
                                        onBackPressed();
                                    }
                                });
                        AlertDialog  ad = builder.create();

                        ad.show();
                    }
                });

            }
        });
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
