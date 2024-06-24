package com.example.gamersparadise.customer.home.facility.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamersparadise.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private final List<String> timeSlots;
    private final Set<String> selectedTimeSlots;
    private final Set<String> reservedTimeSlots;
    private final TimeSlotClickListener listener;

    public interface TimeSlotClickListener {
        void onTimeSlotClick(String timeSlot);
    }

    public TimeSlotAdapter(TimeSlotClickListener listener) {
        this.timeSlots = new ArrayList<>();
        this.selectedTimeSlots = new HashSet<>();
        this.reservedTimeSlots = new HashSet<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        String timeSlot = timeSlots.get(position);
        holder.tvTimeSlot.setText(timeSlot);
        if (reservedTimeSlots.contains(timeSlot)) {
            holder.itemView.setEnabled(false);
            holder.tvTimeSlot.setBackgroundResource(R.drawable.rectangle_disabled);
        } else {
            holder.itemView.setEnabled(true);
            holder.tvTimeSlot.setBackgroundResource(selectedTimeSlots.contains(timeSlot) ? R.drawable.rectangle_cb : R.drawable.rectangle_tf);
            holder.tvTimeSlot.setTextColor(selectedTimeSlots.contains(timeSlot) ? holder.itemView.getContext().getResources().getColor(R.color.orange) : holder.itemView.getContext().getResources().getColor(R.color.black));
            holder.itemView.setOnClickListener(v -> {
                listener.onTimeSlotClick(timeSlot);
            });
        }
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public void setTimeSlots(List<String> timeSlots) {
        this.timeSlots.clear();
        this.timeSlots.addAll(timeSlots);
        notifyDataSetChanged();
    }

    public void setSelectedTimeSlots(Set<String> selectedTimeSlots) {
        this.selectedTimeSlots.clear();
        this.selectedTimeSlots.addAll(selectedTimeSlots);
        notifyDataSetChanged();
    }

    public void setReservedTimeSlots(List<String> reservedTimeSlots) {
        this.reservedTimeSlots.clear();
        this.reservedTimeSlots.addAll(reservedTimeSlots);
        notifyDataSetChanged();
    }

    public static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeSlot;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeSlot = itemView.findViewById(R.id.tv_time_slot);
        }
    }
}
