package com.sage.hitesh.sageattandance;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sage.hitesh.sageattandance.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewttendenceAdapter extends FirebaseRecyclerAdapter<RecordItems,NewttendenceAdapter.AttendenceViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
   public String current_date;
    public NewttendenceAdapter(@NonNull FirebaseRecyclerOptions<RecordItems> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AttendenceViewHolder holder, int position, @NonNull RecordItems model) {
       String latitude = "23.184466";
        String longitude ="77.522154" ;
        holder.tvId.setText(model.getId());
        Log.d("Firebase Test", "onBindViewHolder: "+model.getName());
       holder.tvName.setText(model.getName());
        getCurrentDate();
       holder.btnPrsnt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DatabaseReference reference = FirebaseDatabase.getInstance().getReference("teachers");
              Toast.makeText(v.getContext(), current_date, Toast.LENGTH_SHORT).show();
               Query query = reference.child(model.getId()).child("attendance");
               query.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.child(current_date).exists())
                       {
                           Toast.makeText(v.getContext(), "Already Marked", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           reference.child(model.getId()).child("attendance").child(getCurrentDate()).child("date").setValue(getCurrentDate());
                           reference.child(model.getId()).child("attendance").child(getCurrentDate()).child("status").setValue("present");
                           reference.child(model.getId()).child("attendance").child(getCurrentDate()).child("latitude").setValue(latitude);
                           reference.child(model.getId()).child("attendance").child(getCurrentDate()).child("longitude").setValue(longitude);
                           Toast.makeText(v.getContext(), "Attendance Marked Sucessfully", Toast.LENGTH_LONG).show();
                           return;
                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
           }
       });
    }

    @NonNull
    @Override
    public AttendenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view = LayoutInflater.from(parent.getContext())
             .inflate(R.layout.custom_layout_linear,parent,false);
        return new AttendenceViewHolder(view);
    }



    public static class AttendenceViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvId;
        Button btnPrsnt;
        public AttendenceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId=itemView.findViewById(R.id.tvUid);
            tvName=itemView.findViewById(R.id.tvTName);
            btnPrsnt = itemView.findViewById(R.id.btnPresent);
        }
    }
    private String getCurrentDate() {
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            current_date = simpleDateFormat.format(calendar.getTime()).toString();


        } catch (Exception e) {
            Log.d("Firebase Test", "onClick: " + e.toString());
        }
        return current_date;
    }
}
