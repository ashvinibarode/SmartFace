package com.sage.hitesh.sageattandance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sage.hitesh.sageattandance.R;
import com.lucky.attendenceapplication.items.AttendenceItems;

import java.util.List;

public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceViewHolder> {
    Context context;
    List<AttendenceItems> items;

    public AttendenceAdapter(Context context, List<AttendenceItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public AttendenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendenceViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.custom_layout_linear,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttendenceViewHolder holder, int position) {
         holder.tvName.setText(items.get(position).getName());
         holder.tvId.setText(items.get(position).getU_id());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
