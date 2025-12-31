package com.sage.hitesh.sageattandance;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class SeeInfoAdapter extends FirebaseRecyclerAdapter<SeeInfoModule,SeeInfoAdapter.SeeInfoViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SeeInfoAdapter(@NonNull FirebaseRecyclerOptions<SeeInfoModule> options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull SeeInfoViewHolder holder, int position, @NonNull SeeInfoModule model) {
        holder.tvId.setText(model.getId());
        /*Long present = Long.parseLong(model.getPresent());
        Long leave = Long.parseLong(model.getLeaves());
        long absent = 30-(present+leave);*/
        String present = ""+model.getPresentCont();
     try{
         holder.tvName.setText(model.getName());
//         holder.tvPresent.setText(model.getPresentCont());
         holder.tvPresent.setText(String.valueOf(model.getAttendance().size()));
         holder.tvLeave.setText(String.valueOf(model.getleave().size()));
         String absent= String.valueOf(30-(model.getAttendance().size()+model.getleave().size()));
         holder.tvAbsent.setText(absent);
         if(model.getleave().size()==0){
             holder.tvLeave.setText("0");
         }
         Log.d("Firebase Test", "onBindViewHolder: "+model.getName());
         Log.d("Firebase Test", "onBindViewHolder: "+model.getPresentCont());
         holder.btnShowLeave.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
          LayoutInflater inflater = LayoutInflater.from(v.getContext());
          View view= inflater.inflate(R.layout.custom_message_dialog,null);
          TextView tvTitleDate = view.findViewById(R.id.tvTitleDate);
          TextView tvStarteDate = view.findViewById(R.id.tvStartDate);
          TextView tvEndDate = view.findViewById(R.id.tvEndDate);
          TextView tvTotalDate = view.findViewById(R.id.tvLeaveTotal);
          DatabaseReference reference = FirebaseDatabase.getInstance().getReference("teachers").child(model.getId());
          Query query = reference.orderByChild("leave");
          query.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  Log.d("Firebase Test", "onDataChange inside alertbox: "+model.getleave());
                  Log.d("Firebase Test", "onDataChange inside alertbox: "+snapshot.getValue());
                  Query q = reference.child("leave").child(snapshot.getValue(String.class));
                  q.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          Log.d("Firebase Test", "onDataChange: inside leave"+snapshot.child("start_date").getValue());
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {

                      }
                  });



              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });

             }
         });
     }
     catch (Exception e){
         Log.d("Firebase Test", "onBindViewHolder: exception"+e.getLocalizedMessage());
     }
    }

    @NonNull
    @Override
    public SeeInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_layout_linear_2,parent,false);
        return new SeeInfoViewHolder(view);
    }

    public static class SeeInfoViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvId,tvLeave,tvAbsent,tvPresent;
        ImageButton btnShowLeave;
        public SeeInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvCname);
            tvId=itemView.findViewById(R.id.tvCid);
            tvPresent=itemView.findViewById(R.id.tvCpresent);
            tvAbsent=itemView.findViewById(R.id.tvCabsent);
            tvLeave=itemView.findViewById(R.id.tvCleave);
            btnShowLeave=itemView.findViewById(R.id.btnDetailed);

        }
    }
}
