package com.sage.hitesh.sageattandance;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sage.hitesh.sageattandance.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InforActivity extends Activity {
    RecyclerView rlContainer2;
    DatabaseReference reference;
    SeeInfoAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_info);
        rlContainer2 = findViewById(R.id.rlContainer2);
        reference = FirebaseDatabase.getInstance().getReference("teachers");
        //Todo by telegram user
        rlContainer2.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        FirebaseRecyclerOptions<SeeInfoModule> options = new FirebaseRecyclerOptions.Builder<SeeInfoModule>()
                .setQuery(reference,SeeInfoModule.class).build();
        adapter = new SeeInfoAdapter(options);
        rlContainer2.setAdapter(adapter);
        adapter.startListening();
        //Todo Ended

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }
    void notToUSe()
    {
        //        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot:snapshot.getChildren())
//                {
//                    SeeInfoModule items = dataSnapshot.getValue(SeeInfoModule.class);
//                    items.setId(dataSnapshot.getKey());
//                    Query query = reference.child(items.getId());
//                    query.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            items.setName(snapshot.child("name").getValue(String.class));
////                            items.setPresent(snapshot.child("attendance").getValue(String.class));
//                            long present = snapshot.child("attendance").getChildrenCount();
//                            long leave = snapshot.child("leave").getChildrenCount();
//                            items.setPresentCont(""+present);
//                            items.setLeaveCount(snapshot.child("leave").getChildren().toString());
//                            long absent = 30 - (present + leave);
//                            items.setAbsentCount(""+absent);
//                            items.setMobile(snapshot.child("phone").getValue(String.class));
//                            Log.d("Firebase Test", "onDataChange:\n" + "item from firebase" + items.getPresentCont());
//
//                        }
//
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }
}
