package com.sage.hitesh.sageattandance;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.sage.hitesh.sageattandance.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MarkAttActivity extends Activity implements LocationListener{
    private ProgressDialog mProgressDialog;
    FrameLayout frmCamera;
    private Bitmap bitmap;
    ImageView imgBit;
    Camera camera;
    ShowCamera showCamera;
    ImageButton btnMarkAttendance;
    String current_date,distance;
    String user_id;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    FirebaseDatabase database;
    DatabaseReference reference;
    Intent intent;
    double currentLat, currentLong;
    boolean marked;
    AlertDialog ad;
    double longitude,latitude;
    float[] floats;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_markatt);
        imgBit = (ImageView) findViewById(R.id.imgBit);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("teachers");
        intent = getIntent();
        distance="far";
        setFrame();
        btnMarkAttendance = (ImageButton) findViewById(R.id.btnMarkAttendance);
        try{
            btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getLocation();
                    Log.d("Firebase Test", "onClick: " + getCurrentDate()+distance);

                    captureImage();
                    Log.d("Firebase Test", "onClick: "+distance);
                    user_id = intent.getStringExtra("U_ID");
//                    user_id = "123456";




                }

            });

        }
        catch(Exception e){
            Log.d("Firebase Test", "onClick: "+ e.getLocalizedMessage());
        }
    }

    //init and camera preview
    private void setFrame() {
        frmCamera = (FrameLayout) findViewById(R.id.frmCamera);
        camera = openFrontFacingCamera();
        showCamera = new ShowCamera(this, camera);
        frmCamera.addView(showCamera);
    }

    private Camera openFrontFacingCamera() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e("RecogCam", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }

    private void captureImage() {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera cameras) {
                Log.d("RecogApi", "onPictureTaken: ");
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                frmCamera.setVisibility(View.GONE);
                camera.stopPreview();
                imgBit.setVisibility(View.VISIBLE);
                frmCamera.setVisibility(View.GONE);
                imgBit.setImageBitmap(bitmap);
                imgBit.setRotationX(180);
                imgBit.setRotation(90);
                // Detect face before marking attendance
                detectFace(bitmap);
            }
        });
    }

    private void detectFace(Bitmap bitmap) {
        try {
            FaceDetectorOptions highAccuracyOpts =
                    new FaceDetectorOptions.Builder()
                            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                            .build();

            FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);
            InputImage image = InputImage.fromBitmap(bitmap, 0);

            detector.process(image)
                    .addOnSuccessListener(faces -> {
                        if (faces.size() > 0) {
                            // At least one face detected
                            Log.d("Firebase Test", "Face detected, marking attendance...");
                            // Update the overlay with detected faces
                            FaceOverlayView overlayView = findViewById(R.id.faceOverlay);
                            overlayView.setFaces(faces);
                            markAttendance();
                        } else {
                            // No face detected, show error
                            showErrorDialog("No face detected", "Please make sure your face is clearly visible in the frame.");
                            Log.d("Firebase Test", "No face detected");


                        }
                    })
                    .addOnFailureListener(e -> {
                        showErrorDialog("Error detecting face", e.getLocalizedMessage());
                        Log.d("Firebase Test", "Error detecting face: " + e.getLocalizedMessage());
                    });
        } catch (Exception e) {
            Log.d("Firebase Test", "detectFace: " + e.getLocalizedMessage());
            showErrorDialog("Error", "An error occurred while processing the image.");
        }
    }

    //Getting current date of sysytem
    private String getCurrentDate() {
        try {
            calendar = Calendar.getInstance();
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            current_date = simpleDateFormat.format(calendar.getTime()).toString();


        } catch (Exception e) {
            Log.d("Firebase Test", "onClick: " + e.toString());
        }
        return current_date;
    }

    /* show progress */
    public void showProgress() {
        try {
            if (isProgressShowing())
                dismissProgress();
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("PLEASE WAIT USER");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* dismiss progress */
    public void dismissProgress() {
        try {
            if (mProgressDialog != null) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // this code will be executed after 2 seconds
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }
                }, 5000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isProgressShowing() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            return true;
        else return false;
    }
    //Getting current Location and comparing it to given location
    private void getLocation() {
        showProgress();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            AlertDialog.Builder builder = new AlertDialog.Builder(MarkAttActivity.this);
            builder.setMessage("Please allow us to get you location")
                    .setTitle("Permission Check")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

//                                           onBackPressed();
                        }
                    });
            ad = builder.create();
            dismissProgress();
            ad.show();

        }
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                Log.d("Firebase Test", "onLocationChanged: "+longitude + latitude);
            }
        };
        try{
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
                Log.d("Firebase Test", "getLocation: after location = null");
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        Log.d("Firebase Test", "getLocation: "+latitude+longitude);
        Location location1 = new Location("Test Location");

        location1.setLongitude(77.522154);
        location1.setLatitude(23.184466);
            /*location1.setLongitude(77.3453920);
            location1.setLatitude(23.2786990);*/

           if((location.distanceTo(location1)/1000)<=2){
               Log.d("Firebase Test", "getLocation: near"+location.distanceTo(location1)/1000);
               distance = "near";
           }
           else {
               Log.d("Firebase Test", "getLocation: far"+location.distanceTo(location1)/1000 + "\n"+location1.getLatitude()+"\n"+
                       location1.getLongitude()+"\n"+latitude+"\n"+longitude);
               distance = "far";
           }
       }
       catch(Exception e){Log.d("Firebase Test", e.getLocalizedMessage());}
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        location.getAccuracy();
       longitude = location.getLongitude();
       latitude = location.getLatitude();
    }

    @Override
    protected void onStart() {
        super.onStart();
        distance="far";
    }
    //network checking
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    //internet checking
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
    private void showErrorDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MarkAttActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) ->  MarkAttActivity.this.finish());
        ad = builder.create();
        dismissProgress();
        ad.show();
    }
    private void markAttendance() {
        getLocation();
        Log.d("Firebase Test", "onClick: " + getCurrentDate() + distance);
        user_id = intent.getStringExtra("U_ID");

        if (isNetworkConnected()) {
            if (distance.equals("near")) {
                Query q = reference.child(user_id).child("attendance");
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.child(getCurrentDate()).exists()) {
                            Log.d("Firebase Test", "onDataChange: doesnt exist");
                            reference.child(user_id).child("attendance").child(getCurrentDate()).child("date").setValue(getCurrentDate());
                            reference.child(user_id).child("attendance").child(getCurrentDate()).child("status").setValue("present");
                            reference.child(user_id).child("attendance").child(getCurrentDate()).child("latitude").setValue(latitude);
                            reference.child(user_id).child("attendance").child(getCurrentDate()).child("longitude").setValue(longitude);
                            showSuccessDialog("Marked Successfully", "You successfully marked your attendance.");
                        } else {
                            showErrorDialog("Already Present", "You already marked your attendance.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showErrorDialog("Error", "Failed to check attendance. Please try again.");
                    }
                });
            } else {
                showErrorDialog("Far From Location", "You are too far from the location. Please go to the correct location and try again.");
            }
        } else {
            showErrorDialog("Network Error", "Check your internet connection.");
        }
    }
    private void showSuccessDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MarkAttActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    ad.dismiss();
                    onBackPressed();
                });
        ad = builder.create();
        dismissProgress();
        ad.show();
    }
}

