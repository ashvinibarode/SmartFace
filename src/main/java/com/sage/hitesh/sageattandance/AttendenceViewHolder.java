package com.sage.hitesh.sageattandance;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sage.hitesh.sageattandance.R;

public class AttendenceViewHolder extends RecyclerView.ViewHolder {
    TextView tvId,tvName;
    Button btnPresent;
    public AttendenceViewHolder(@NonNull View itemView) {
        super(itemView);
        tvId = itemView.findViewById(R.id.tvUid);
        tvName = itemView.findViewById(R.id.tvTName);
        btnPresent = itemView.findViewById(R.id.btnPresent);

    }
}
