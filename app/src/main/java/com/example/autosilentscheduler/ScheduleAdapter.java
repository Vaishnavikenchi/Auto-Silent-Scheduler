package com.example.autosilentscheduler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<ScheduleModel> list;

    public ScheduleAdapter(List<ScheduleModel> list) { this.list = list; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.info.setText(list.get(position).getDateTime());
        holder.delete.setOnClickListener(v -> {
            list.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView info;
        ImageButton delete;
        public ViewHolder(View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.txtScheduleInfo);
            delete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
