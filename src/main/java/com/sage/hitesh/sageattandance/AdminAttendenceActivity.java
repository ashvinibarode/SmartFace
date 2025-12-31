package com.sage.hitesh.sageattandance;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sage.hitesh.sageattandance.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminAttendenceActivity extends Activity {
  RecyclerView rvAttendence;
  FirebaseDatabase database;
  DatabaseReference reference;
    NewttendenceAdapter adapter;
    List<String> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_attendence_admin);
        rvAttendence = findViewById(R.id.rlContainer);
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        try{
            reference = database.getReference("teachers");
            Query q = reference.orderByChild("id");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        RecordItems items = dataSnapshot.getValue(RecordItems.class);
                        items.setId(dataSnapshot.getKey());
                        Log.d("Firebase Test", "onDataChange: id  "+items.getId());
                        Query query = reference.child(items.getId());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                             items.setName(snapshot.child("name").getValue(String.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Log.d("Firebase Test", "onDataChange: getName in Admin"+items.getName());
                        rvAttendence.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        FirebaseRecyclerOptions<RecordItems> options = new FirebaseRecyclerOptions.Builder<RecordItems>()
                                .setQuery(reference,RecordItems.class).build();
                        adapter = new NewttendenceAdapter(options);
                        rvAttendence.setAdapter(adapter);
                        adapter.startListening();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            /* Log.d("Firebase Test", "onCreate: 1111"+reference.get());
            rvAttendence.setLayoutManager(new LinearLayoutManager(this));
            FirebaseRecyclerOptions<RecordItems> options = new FirebaseRecyclerOptions.Builder<RecordItems>()
                    .setQuery(reference,RecordItems.class).build();
            adapter = new NewttendenceAdapter(options);
            rvAttendence.setAdapter(adapter);
            adapter.startListening();*/
        }
        catch (Exception e){Log.d("Firebase Test",e.getLocalizedMessage());}
    }

    @Override
    protected void onStart() {
        super.onStart();
//        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
